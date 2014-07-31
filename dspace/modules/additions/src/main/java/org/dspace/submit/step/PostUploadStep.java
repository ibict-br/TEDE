package org.dspace.submit.step;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dspace.app.util.SubmissionInfo;
import org.dspace.authorize.AuthorizeException;
import org.dspace.authorize.AuthorizeManager;
import org.dspace.authorize.ResourcePolicy;
import org.dspace.content.Bitstream;
import org.dspace.content.BitstreamFormat;
import org.dspace.content.Bundle;
import org.dspace.content.Item;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Context;
import org.dspace.license.CreativeCommons;
import org.dspace.submit.AbstractProcessingStep;
import org.dspace.submit.step.domain.EmbargoOption;

import com.ibm.icu.text.SimpleDateFormat;

/**
 * Efetua rotinas necessárias "pós upload". <br>
 * Dentre as principais funcionalidades:
 * <ul>
 * 	<li>Registro de <i>mimeTypes</i> no metadado <i>dc.format</i></li>
 * 	<li>Registro do metadado <i>dc.rights</i> de acordo com embargo</li>
 * </ul>
 * @author Márcio Ribeiro Gurgel do Amaral (marcio.rga@gmail.com)
 * 
 */
public class PostUploadStep extends AbstractProcessingStep {

	private static final int NO_ITEM_OR_PAGES = 0;

	/**
	 * @see AbstractProcessingStep#doProcessing(Context, HttpServletRequest,
	 *      HttpServletResponse, SubmissionInfo)
	 */
	@Override
	public int doProcessing(Context context, HttpServletRequest request,
			HttpServletResponse response, SubmissionInfo subInfo)
			throws ServletException, IOException, SQLException,
			AuthorizeException {

		Item item = subInfo.getSubmissionItem().getItem();

		/** Fill "dc.format" **/
		fillFormat(item);
		
		/** Fill "dc.rights.uri" **/
		fillLicenseURL(item);
		
		fillEmbargoMetadata(context, item);
		
		item.update();

		return NO_ITEM_OR_PAGES;
	}

	private void fillEmbargoMetadata(Context context, Item item) throws SQLException 
	{
		item.clearMetadata("dc", "rights", null, Item.ANY);
		item.clearMetadata("dc", "date", "available", Item.ANY);

		Date greaterPolicyDate = null;
		for(ResourcePolicy resourcePolicy : getResourcePolicies(context, item))
		{
			Date startDate = resourcePolicy.getStartDate();
			if(startDate != null)
			{
				if(greaterPolicyDate == null || startDate.after(greaterPolicyDate))
				{
					greaterPolicyDate = startDate;
				}
			}
		}
		
		String embargoMetadataValue = null;
		String rightsMetadataValue = null;
		
			
		if(greaterPolicyDate != null && greaterPolicyDate.equals(EmbargoOption.RESTRICTED.getAssociatedDate()))
		{
			embargoMetadataValue =  new SimpleDateFormat("yyyy-MM-dd").format(EmbargoOption.RESTRICTED.getAssociatedDate());
			rightsMetadataValue = EmbargoOption.RESTRICTED.getKey();
		}
		else if(greaterPolicyDate != null && greaterPolicyDate.after(new Date()))
		{
			embargoMetadataValue =  new SimpleDateFormat("yyyy-MM-dd").format(greaterPolicyDate);
			rightsMetadataValue = EmbargoOption.EMBARGOED.getKey();
		}
		else
		{
			embargoMetadataValue =  new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			rightsMetadataValue = EmbargoOption.FREE.getKey();
		}
		
		
		String language = ConfigurationManager.getProperty("defatult.language.iso6392");
		item.addMetadata("dc", "date", "available", language, embargoMetadataValue);
		item.addMetadata("dc", "rights", null, language, rightsMetadataValue);
	}
	
    private List<ResourcePolicy> getResourcePolicies(Context context, Item item) throws SQLException
    {
		Bundle[] originalBundles = item.getBundles("ORIGINAL");
		List<ResourcePolicy> result = new ArrayList<ResourcePolicy>();
		
		if(originalBundles != null && originalBundles.length > 0)
		{
			
			for(Bundle bundle : originalBundles)
			{
				Bitstream[] bitstreams = bundle.getBitstreams();
				
				if(bitstreams != null && bitstreams.length > 0)
				{
					
					for(Bitstream bitstream : bitstreams)
					{
						result.addAll(AuthorizeManager.findPoliciesByDSOAndType(context, bitstream, ResourcePolicy.TYPE_CUSTOM));
					}
				}
			}
		}
		
		return result;
    }

	/**
	 * Clear and fill <i>dc.rights.uri</i> with the URL of the license {@link CreativeCommons#getLicenseURL(Item)}
	 * @param item Item beeing submited
	 * @throws SQLException
	 * @throws IOException
	 * @throws AuthorizeException
	 */
	private void fillLicenseURL(Item item) throws SQLException, IOException,
			AuthorizeException {
		String licenseURL = CreativeCommons.getLicenseURL(item);
		if(licenseURL != null)
		{
			item.clearMetadata("dc", "rights", "uri", Item.ANY);
			item.addMetadata("dc", "rights", "uri", null, licenseURL);
		}
	}

	private void fillFormat(Item item) throws SQLException {
		item.clearMetadata("dc", "format", null, Item.ANY);
		Set<String> uniqueFormats = new HashSet<String>();

		Bundle[] originalBundles = item.getBundles("ORIGINAL");
		
		/** Itera sob bundles a fim de identificar unicidade de formatos **/
		for (Bundle bundle : originalBundles) 
		{
			for (Bitstream bitstream : bundle.getBitstreams())
			{
				BitstreamFormat format = bitstream.getFormat();
				if (format != null) 
				{
					uniqueFormats.add(format.getMIMEType());
				}
			}
		}

		/** Itera sob os formatos encontrados **/
		for (String uniqueFormat : uniqueFormats) 
		{
			item.addMetadata("dc", "format", null, Item.ANY, uniqueFormat);
		}
	}

	/**
	 * @see AbstractProcessingStep#getNumberOfPages(HttpServletRequest,
	 *      SubmissionInfo)
	 */
	@Override
	public int getNumberOfPages(HttpServletRequest request,
			SubmissionInfo subInfo) throws ServletException {
		/** Esta funcionalidade atua somente na camada de serviço **/
		return NO_ITEM_OR_PAGES;
	}

}