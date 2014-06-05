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
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.dspace.app.webui.servlet.constants.FolderMetadataImportConstants;
import org.dspace.app.webui.util.JSPManager;
import org.dspace.authorize.AuthorizeException;
import org.dspace.content.Collection;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Context;
import org.dspace.folderimport.FolderMetadataProcessor;
import org.dspace.folderimport.FolderReader;
import org.dspace.folderimport.domain.ImportType;
import org.dspace.folderimport.dto.FolderAnalyseResult;


/**
 * Servlet responsável por tratar requisições de importação de metadados. <br>
 * A referida funcionalidade pode ser intepretada como uma <i>interface gráfica</i> para o comando <code>dspace import</code>
 * @author Márcio Ribeiro Gurgel do Amaral
 *
 */
public class FolderMetadataImportServlet extends DSpaceServlet
{

	private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(MetadataImportServlet.class);
    
	/**
	 * Registra possibilidades de importação
	 */
	private static Map<Integer, String> supportedImportTypes = new LinkedHashMap<Integer, String>(){
		private static final long serialVersionUID = 1L;
	{
		put(ImportType.TEST.getId(), ImportType.TEST.get18nKey());
		put(ImportType.WORKFLOW.getId(), ImportType.WORKFLOW.get18nKey());
		put(ImportType.IMPORT.getId(), ImportType.IMPORT.get18nKey());
	}};
	

	@Override
	protected void doDSGet(Context context, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			SQLException, AuthorizeException {
		
		doDSPost(context, request, response);
	}

	/**
	 * @see DSpaceServlet#doDSPost(Context, HttpServletRequest, HttpServletResponse)a
	 */
	@Override
	protected void doDSPost(Context context, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			SQLException, AuthorizeException 
	{
		
		if(request.getParameter("submit_selection") != null)
		{
			/** Usuário solicitou apresentação desta página **/
			preparePage(context, request, response);
		}
		else if(request.getParameter("submit_import") != null)
		{
			/** Usuário solicitou importação **/
			String folderLocation = request.getParameter("folder_location");
			String importTypeStr = request.getParameter("import_type");
			String collectionStr = request.getParameter("collection");
			
			Integer idImportType = NumberUtils.isNumber(importTypeStr) ? NumberUtils.createInteger(importTypeStr) : null;
			Integer idCollection = NumberUtils.isNumber(collectionStr) ? NumberUtils.createInteger(collectionStr) : null;
			
			ImportType importType = ImportType.getById(idImportType);
			
			Collection foundCollection = Collection.find(context, idCollection);
			
			FolderMetadataProcessor myloader = new FolderMetadataProcessor();
			
			try 
			{
				myloader.addItems(context, new Collection[]{foundCollection}, folderLocation, getMappingFileLocation(), false, 
						importType.equals(ImportType.TEST), false, false, importType.equals(ImportType.WORKFLOW));
			} 
			catch (Exception e) 
			{
				logger.error(e.getMessage(), e);
			}
		}
		
	}

	/**
	 * Constrói localização de arquivo de mapeamento
	 * @return
	 */
	private String getMappingFileLocation() {
		
		StringBuilder mappingBuilder = new StringBuilder();
		mappingBuilder.append(ConfigurationManager.getProperty("dspace.dir"));
		mappingBuilder.append(File.separator);
		mappingBuilder.append("imports");
		mappingBuilder.append(File.separator);
		mappingBuilder.append("mapping");
		
		return mappingBuilder.toString();
	}

	
	private void preparePage(Context context, HttpServletRequest request,
			HttpServletResponse response) throws SQLException,
			ServletException, IOException {
		
    	String baseFolderNumber = request.getParameter("selectedFolder");
    	Map<Long, Long> parentsMapping = new HashMap<Long, Long>();
    	
    	HttpSession session = request.getSession();
		@SuppressWarnings("unchecked")
		Map<Long, File> serverMappingRoot = (Map<Long, File>)session.getAttribute(FolderMetadataImportConstants.SERVER_DATA_READBLE_KEY_ROOT);
    	@SuppressWarnings("unchecked")
		Map<Long, String> userReadbleRoot = (Map<Long, String>)session.getAttribute(FolderMetadataImportConstants.USER_DATA_READBLE_KEY_ROOT);
    	
    	/** Assegura erros de conversão **/
		if(NumberUtils.isNumber(baseFolderNumber))
		{
			File selectedFolder = serverMappingRoot.get(Long.valueOf(baseFolderNumber));
			if(selectedFolder != null)
			{
				
				/** O diretório base DEVE exitir **/
				if(selectedFolder.exists())
				{
					FolderReader folderReader = new FolderReader(selectedFolder);
					FolderAnalyseResult buildTree = folderReader.buildTree();
					
					session.setAttribute(FolderMetadataImportConstants.USER_DATA_READBLE_KEY, buildTree.getUserReadble());
					session.setAttribute(FolderMetadataImportConstants.SERVER_DATA_READBLE, buildTree.getServerReadble());
					session.setAttribute(FolderMetadataImportConstants.PARENT_FOLDER_MAPPPING, buildTree.getMappingParent());
				}
				
			}
			request.setAttribute("exportDateTime", userReadbleRoot.get(Long.valueOf(baseFolderNumber)));
		}
    	
    	/** Recupera todas coleçõs **/
    	request.setAttribute("collections", Arrays.asList(Collection.findAll(context)));
    	request.setAttribute("supportedTypes", supportedImportTypes);
    	JSPManager.showJSP(request, response, "/dspace-admin/foldermetadataimport.jsp");
	}

    
}