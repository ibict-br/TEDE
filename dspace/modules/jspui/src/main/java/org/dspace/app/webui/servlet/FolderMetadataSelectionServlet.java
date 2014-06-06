/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.app.webui.servlet;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.dspace.app.webui.servlet.constants.FolderMetadataImportConstants;
import org.dspace.app.webui.util.JSPManager;
import org.dspace.app.webui.util.UIUtil;
import org.dspace.authorize.AuthorizeException;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Context;
import org.dspace.folderimport.FolderReader;
import org.dspace.folderimport.dto.FolderAnalyseResult;



/**
 * @author Márcio Ribeiro Gurgel do Amaral
 *
 */
public class FolderMetadataSelectionServlet extends DSpaceServlet
{

	private static final String INTERMEDIATE_FOLDER = "exportados";
	private static final long serialVersionUID = 1L;

	/**
	 * @see DSpaceServlet#doDSGet(Context, HttpServletRequest, HttpServletResponse)
	 */
	@Override
	protected void doDSGet(Context context, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			SQLException, AuthorizeException {
		
		HttpSession session = request.getSession();
		
		session.setAttribute(FolderMetadataImportConstants.USER_DATA_READBLE_KEY, null);
		session.setAttribute(FolderMetadataImportConstants.SERVER_DATA_READBLE, null);
		session.setAttribute(FolderMetadataImportConstants.PARENT_FOLDER_MAPPPING, null);
		session.setAttribute(FolderMetadataImportConstants.USER_DATA_READBLE_KEY_ROOT, null);
		session.setAttribute(FolderMetadataImportConstants.SERVER_DATA_READBLE_KEY_ROOT, null);

		File root = new File(ConfigurationManager.getProperty("org.dspace.app.itemexport.work.dir"));
		FolderReader folderReader = new FolderReader(root);
		
		FolderAnalyseResult listAvailableExport = folderReader.listAvailableExport(UIUtil.getSessionLocale(request), INTERMEDIATE_FOLDER);
		if(listAvailableExport.getUserReadble() != null && !listAvailableExport.getUserReadble().isEmpty())
		{
			session.setAttribute(FolderMetadataImportConstants.USER_DATA_READBLE_KEY_ROOT, listAvailableExport.getUserReadble());
			session.setAttribute(FolderMetadataImportConstants.SERVER_DATA_READBLE_KEY_ROOT, listAvailableExport.getServerReadble());
			
			if(listAvailableExport.getUserReadble().size() > 1)
			{
		    	JSPManager.showJSP(request, response, "/dspace-admin/foldermetadataselection.jsp");
			}
			else
			{
				/** Segue para próxima página **/
				response.sendRedirect(response.encodeRedirectURL(request
	                    .getContextPath() + "/dspace-admin/foldermetadataimport?submit_selection"));
			}
		}
		else
		{
			request.setAttribute("has-error", Boolean.TRUE);
			request.setAttribute("message", FolderMetadataImportConstants.KEY_MESSAGE_NO_FOLDER_EXISTS);
			JSPManager.showJSP(request, response, "/dspace-admin/foldermetadataselection.jsp");
		}
		
	}

	/**
	 * @see DSpaceServlet#doDSPost(Context, HttpServletRequest, HttpServletResponse)
	 */
	@Override
	protected void doDSPost(Context context, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			SQLException, AuthorizeException {
		// TODO Auto-generated method stub
		super.doDSPost(context, request, response);
	}


	
	
}

	

