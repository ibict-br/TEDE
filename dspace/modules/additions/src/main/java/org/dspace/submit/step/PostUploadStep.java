package org.dspace.submit.step;

import java.io.IOException;
import java.sql.SQLException;

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
 * 
 * @author Márcio Ribeiro Gurgel do Amaral (marcio.rga@gmail.com)
 *
 */
public class PostUploadStep extends AbstractProcessingStep {

	private static final int NO_PRIMARY_BISTREAM = -1;
	private static final int NO_ITEM_OR_PAGES = 0;

	/**
	 * @see AbstractProcessingStep#doProcessing(Context, HttpServletRequest, HttpServletResponse, SubmissionInfo)
	 */
	@Override
	public int doProcessing(Context context, HttpServletRequest request,
			HttpServletResponse response, SubmissionInfo subInfo)
			throws ServletException, IOException, SQLException,
			AuthorizeException {
		
		 Item item = subInfo.getSubmissionItem().getItem();
		 
		 int primaryBitstreamId = NO_PRIMARY_BISTREAM;
		 
	     for(Bundle bundle : item.getBundles("ORIGINAL"))
         {
	    	 /** Garante que verificação não será feita a toda iteração de bundles **/
	    	 if(primaryBitstreamId == NO_PRIMARY_BISTREAM && bundle.getPrimaryBitstreamID() != NO_PRIMARY_BISTREAM)
	    	 {
	    		primaryBitstreamId = bundle.getPrimaryBitstreamID();
	    	 }
         	
	    	 for(Bitstream bitstream : bundle.getBitstreams())
     		 {
         		if((bitstream.getID() == primaryBitstreamId) || 
         				/** Caso bitstream seja igual a zero, significa que não houve seleção de bistream primário, logo o formato do primeiro bundle é selecionado **/
         				(primaryBitstreamId == NO_PRIMARY_BISTREAM))
         		{
         			BitstreamFormat format = bitstream.getFormat();
         			if(format != null)
         			{
         				item.clearMetadata("dc", "format", null, Item.ANY);
         				item.addMetadata("dc", "format", null, Item.ANY, format.getMIMEType());
         				item.update();
         			}
         			break;
         		}
         	}
         }
		
		return NO_ITEM_OR_PAGES;
	}

	/**
	 * @see AbstractProcessingStep#getNumberOfPages(HttpServletRequest, SubmissionInfo)
	 */
	@Override
	public int getNumberOfPages(HttpServletRequest request,
			SubmissionInfo subInfo) throws ServletException {
		/** Esta funcionalidade atua somente na camada de serviço **/
		return NO_ITEM_OR_PAGES;
	}

}
