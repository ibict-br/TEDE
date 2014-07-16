/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.content.authority;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.dspace.app.util.DCInputsReader;
import org.dspace.app.util.DCInputsReaderException;
import org.dspace.core.SelfNamedPlugin;

/**
 * ChoiceAuthority source that reads the same input-forms which drive
 * configurable submission.
 *
 * Configuration:
 *   This MUST be configured aas a self-named plugin, e.g.:
 *     plugin.selfnamed.org.dspace.content.authority.ChoiceAuthority = \
 *        org.dspace.content.authority.DCInputAuthority
 *
 * It AUTOMATICALLY configures a plugin instance for each <value-pairs>
 * element (within <form-value-pairs>) of the input-forms.xml.  The name
 * of the instance is the "value-pairs-name" attribute, e.g.
 * the element: <value-pairs value-pairs-name="common_types" dc-term="type">
 * defines a plugin instance "common_types".
 *
 * IMPORTANT NOTE: Since these value-pairs do NOT include authority keys,
 * the choice lists derived from them do not include authority values.
 * So you should not use them as the choice source for authority-controlled
 * fields.
 */
public class DCInputAuthority extends SelfNamedPlugin implements ChoiceAuthority
{
    private static Logger log = Logger.getLogger(DCInputAuthority.class);

    private Map<String, String> valueAndLabel;
    private Map<String, Long> valueAndHash;
    private Set<Long> usedHashes = new HashSet<Long>();

    private MessageDigest messageDigest;
    
    private static DCInputsReader dci = null;
    private static String pluginNames[] = null;

    public DCInputAuthority()
    {
        super();
        initMessageDigest();
    }

    /**
     * Initializes {@link #messageDigest}, using <i>MD5</i> hash
     */
	private void initMessageDigest() {
		try 
        {
        	messageDigest = MessageDigest.getInstance("MD5");
        } 
        catch (NoSuchAlgorithmException e) 
        {
        	log.error(e.getMessage(), e);
        }
	}

    public static String[] getPluginNames()
    {
        if (pluginNames == null)
        {
            initPluginNames();
        }
        
        return (String[]) ArrayUtils.clone(pluginNames);
    }

    private static synchronized void initPluginNames()
    {
        if (pluginNames == null)
        {
            try
            {
                if (dci == null)
                {
                    dci = new DCInputsReader();
                }
            }
            catch (DCInputsReaderException e)
            {
                log.error("Failed reading DCInputs initialization: ",e);
            }
            List<String> names = new ArrayList<String>();
            Iterator<String> pi = dci.getPairsNameIterator();
            while (pi.hasNext())
            {
                names.add(pi.next());
            }

            pluginNames = names.toArray(new String[names.size()]);
            log.debug("Got plugin names = "+Arrays.deepToString(pluginNames));
        }
    }

    // once-only load of values and labels
    private void init()
    {
        if (valueAndLabel == null && valueAndHash == null)
        {
        	valueAndLabel = new LinkedHashMap<String, String>();
        	valueAndHash = new LinkedHashMap<String, Long>();
        	
            String pname = this.getPluginInstanceName();
            List<String> pairs = dci.getPairs(pname);
            if (pairs != null)
            {
                for (int i = 0; i < pairs.size(); i += 2)
                {
                	/** Map key (value) **/
                	String value = pairs.get(i+1);
					String label = pairs.get(i);
					
					valueAndLabel.put(value, label);
                	valueAndHash.put(value, generateHashUniqueFromString(value, null));
                }
                log.debug("Found pairs for name="+pname);
            }
            else
            {
                log.error("Failed to find any pairs for name=" + pname, new IllegalStateException());
            }
        }
    }


  

	public Choices getMatches(String field, String query, int collection, int start, int limit, String locale)
    {
        init();

        int dflt = -1;
        
        List<Choice> choices = new ArrayList<Choice>();
        
        int i = 0;
        for(Map.Entry<String, String> currentValue : valueAndLabel.entrySet())
        {
        	if (currentValue.getValue().toLowerCase().contains(query.toLowerCase()))
        	{
        		choices.add(new Choice(String.valueOf(valueAndHash.get(currentValue.getKey())), currentValue.getValue(), currentValue.getValue()));
        		dflt = i++;
        	}
        }
        Choice[] choicesArray = new Choice[choices.size()];
		return new Choices(choices.toArray(choicesArray), 0, choices.size(), Choices.CF_AMBIGUOUS, false, dflt);
    }

	

    public Choices getBestMatch(String field, String text, int collection, String locale)
    {
        init();
        
        for(Map.Entry<String, String> currentValue : valueAndLabel.entrySet())
        {
        	if (text.equalsIgnoreCase(currentValue.getValue()))
        	{
        		Choice v[] = new Choice[1];
        		v[0] = new Choice(String.valueOf(valueAndHash.get(currentValue.getKey())), currentValue.getValue(), currentValue.getValue());
        		return new Choices(v, 0, v.length, Choices.CF_UNCERTAIN, false, 0);
        	}
        	
        }
        return new Choices(Choices.CF_NOTFOUND);
    }

    public String getLabel(String field, String key, String locale)
    {
        init();
        
        for(Map.Entry<String, Long> currentIteration : valueAndHash.entrySet())
        {
        	if(String.valueOf(currentIteration.getValue()).equals(key))
        	{
        		return valueAndLabel.get(currentIteration.getKey());
        	}
        }
        
        return "";
    }
    
    /**
     * Evicts two or more string different strings to recieve same generated hash
     * @param originalValue Original value
     * @param changedValue Changed value, used to distinguish a given String
     * @return Unique hash
     */
    private Long generateHashUniqueFromString(String originalValue, String changedValue) 
    {
    	Long generateHashFromString = changedValue != null ? generateHashFromString(changedValue) : generateHashFromString(originalValue);
    	if(!usedHashes.contains(generateHashFromString))
    	{
    		usedHashes.add(generateHashFromString);
    		valueAndHash.put(originalValue, generateHashFromString);
    		return generateHashFromString;
    	}
    	else
    	{
    		/** Change the original String to get a different hash **/
    		return generateHashUniqueFromString(originalValue, changedValue + "::");
    	}
	}
    
    /**
     * Generate a {@link Long} using {@link #messageDigest} plus a String value
     * @param value
     * @return Long value (converted to String)
     */
    private Long generateHashFromString(String value) 
	{
		ByteBuffer byteBuffer = ByteBuffer.wrap(messageDigest.digest(value.getBytes()));
		return byteBuffer.getLong();
	}
}
