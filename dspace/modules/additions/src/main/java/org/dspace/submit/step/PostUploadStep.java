package org.dspace.submit.step;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dspace.app.util.SubmissionInfo;
import org.dspace.authorize.AuthorizeException;
import org.dspace.content.Bitstream;
import org.dspace.content.BitstreamFormat;
import org.dspace.content.Bundle;
import org.dspace.content.Item;
import org.dspace.core.Context;
import org.dspace.submit.AbstractProcessingStep;

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
		
		item.update();

		return NO_ITEM_OR_PAGES;
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