package org.dspace.export;

import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.dspace.content.DCValue;
import org.dspace.content.Item;
import org.dspace.export.domain.ExportType;

/**
 * Get item's metadata and transforms it to a file.<br>
 * It uses <i>Apache Velocity</i> for file conversion. 
 * The template files can be found at: <b>src/main/resources/item-export</b><br>
 * Currently the supported formats are (also registered in {@link ExportType}:
 * <ul>
 * 	<li>Endnote</li>
 * 	<li>BibTex</li>
 * </ul>
 * 
 * 
 * @author Márcio Ribeiro Gurgel do Amaral (marcio.rga@gmail.com)
 *
 */
@SuppressWarnings("deprecation")
public class ItemExportFormats 
{
	
	private static final int ZERO_CHARACTER = 0;
	private static final int YEAR_LENGTH = 4;
	private static final int YEARH_LENGTH_COMPARATOR = 3;

	/**
	 * Convert instance of {@link Item} on a representative String
	 * @param item Item to be exported
	 * @param exportType Type or export (endnote, bibtext, etc...)
	 */
	public String process(Item item, ExportType exportType)
	{
		VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		velocityEngine.init();
		
		Template template = velocityEngine.getTemplate(exportType.getFileLocation());
		VelocityContext context = new VelocityContext();
		
		Map<String, String> itemMetadata = new LinkedHashMap<String, String>();
		
		for(DCValue currentMetadata : item.getMetadata("dc", Item.ANY, Item.ANY, Item.ANY))
		{
			String value = null;
			
			/** BibTex requires only "year" for date.issued **/
			if(currentMetadata.getField().equals("dc.date.issued") && exportType.equals(ExportType.BIBTEX))
			{
				value = currentMetadata.value != null && currentMetadata.value.length() > 
					YEARH_LENGTH_COMPARATOR ? currentMetadata.value.substring(ZERO_CHARACTER, YEAR_LENGTH) : "";
			}
			else
			{
				value = currentMetadata.value != null ? currentMetadata.value.replaceAll("\n", " ") : "";
			}
			itemMetadata.put(currentMetadata.getField(), value);
		}
		
		context.put("itemMetadata", itemMetadata);
		
		StringWriter stringWriter = new StringWriter();
		template.merge(context, stringWriter);
		
		return stringWriter.toString();
	}
	
}
