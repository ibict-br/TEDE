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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.dspace.app.bulkedit.MetadataImportException;
import org.dspace.app.webui.util.JSPManager;
import org.dspace.authorize.AuthorizeException;
import org.dspace.content.Collection;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Context;
import org.dspace.file.utils.FileUtils;
import org.dspace.folderimport.FolderMetadataProcessor;
import org.dspace.folderimport.FolderReader;
import org.dspace.folderimport.constants.FolderMetadataImportConstants;
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
		put(ImportType.TEST.getId(), ImportType.TEST.getKey());
		put(ImportType.WORKFLOW.getId(), ImportType.WORKFLOW.getKey());
		put(ImportType.IMPORT.getId(), ImportType.IMPORT.getKey());
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
		
		if(request.getParameter("submit_selection") != null && request.getParameter("submit_import") == null)
		{
			/** Usuário solicitou apresentação desta página **/
			preparePage(context, request, response);
		}
		else if(request.getParameter("submit_import") != null)
		{
			/** Usuário solicitou importação **/
			String[] selectedFolders = request.getParameterValues("selected_folders");
			String importTypeStr = request.getParameter("import_type");
			String[] collectionsStr = request.getParameterValues("collection");
			
			@SuppressWarnings("unchecked")
			Map<Long, String> availableFolders = (Map<Long, String>) request.getSession().getAttribute(FolderMetadataImportConstants.USER_DATA_READBLE_KEY);
			
			/** Efetua validação no que tange o preenchimento dos campos **/
			if(((availableFolders.size() > 0 && selectedFolders != null) || availableFolders.size() == 0) && importTypeStr != null && collectionsStr != null)
			{
				/** Dados inseridos em tela **/
				List<String> folderStringList = selectedFolders != null ? Arrays.asList(selectedFolders) : null;
				Integer idImportType = NumberUtils.isNumber(importTypeStr) ? NumberUtils.createInteger(importTypeStr) : null;
				ImportType importType = ImportType.getById(idImportType);
				Collection[] selectedCollections = buildCollectionsArray(context, collectionsStr);
				
				/** Recupera mapeamento de pastas "pai" **/
				Object parentFolderMappingCandidate = request.getSession().getAttribute(FolderMetadataImportConstants.PARENT_FOLDER_MAPPPING);
				List<Long> filesToExport = null;
				if(folderStringList != null)
				{
					@SuppressWarnings("unchecked")
					Map<Long, Long> parentMapping = parentFolderMappingCandidate != null ? (Map<Long, Long>) parentFolderMappingCandidate : null;
					filesToExport = identifiesImportableFolders(folderStringList, parentMapping);
				}
				
				try 
				{
					doImport(request, response, context, importType, selectedCollections, filesToExport);
					handlePageReturnAfterAction(context, request, response, importType.getSuccessMessageKey(), false);
				} 
				catch (Exception e) 
				{
					logger.error(e.getMessage(), e);
					handlePageReturnAfterAction(context, request, response, FolderMetadataImportConstants.KEY_MESSAGE_ERROR_IMPORT, true);
				}
			}
			else
			{
				handlePageReturnAfterAction(context, request, response, FolderMetadataImportConstants.KEY_MESSAGE_REQUIRED_FIELDS, true);
			}
			
		}
		else
		{
			/** Usuário tentou acessar diretamente a página sem passar pela seleção **/
			response.sendRedirect(request.getContextPath() + "/dspace-admin/foldermetadataselection");
		}
		
	}

	/**
	 * Efetua operações comuns de apresentação de página para o usuário
	 * @param context
	 * @param request
	 * @param response
	 * @param message
	 * @param hasError
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 */
	private void handlePageReturnAfterAction(Context context, HttpServletRequest request, HttpServletResponse response, String message, boolean hasError)
			throws SQLException, ServletException, IOException {
		
		request.setAttribute("has-error", hasError);
		request.setAttribute("message", message);

		preparePage(context, request, response);
	}

	/**
	 * Efetua importação efetivamente
	 * @param request 
	 * @param response 
	 * @param context
	 * @param importType
	 * @param selectedCollections
	 * @param filesToExport
	 * @throws Exception 
	 * @throws IOException 
	 */
	private void doImport(HttpServletRequest request, HttpServletResponse response, Context context, ImportType importType, Collection[] selectedCollections, List<Long> filesToExport) throws IOException, Exception {
		
		FolderMetadataProcessor myloader = new FolderMetadataProcessor();
		@SuppressWarnings("unchecked")
		Map<Long, File> serverFolderData = (Map<Long, File>) request.getSession().getAttribute(FolderMetadataImportConstants.SERVER_DATA_READBLE);

		if(filesToExport != null)
		{
			for(Long selectedFolder : filesToExport)
			{
				try
				{
					List<File> storageList = FileUtils.searchRecursiveAddingDirs(serverFolderData.get(selectedFolder), "dublin_core.xml", 2);
					
					for(File suitableDirectory : storageList)
					{
						myloader.addItems(context, selectedCollections, suitableDirectory.getCanonicalPath(), getMappingFileLocation(), false, 
								importType.equals(ImportType.TEST), false, false, importType.equals(ImportType.WORKFLOW));
					}
				}
				catch (MetadataImportException mie)
				{
					logger.error(mie.getMessage(), mie);
					handlePageReturnAfterAction(context, request, response, mie.getMessage(), true);
				}
			} 
		}
		else
		{
			myloader.addItems(context, selectedCollections, getRootSelectedFolder(request, getRootSelectedFolderId(request)).getCanonicalPath(), getMappingFileLocation(), false, 
					importType.equals(ImportType.TEST), false, false, importType.equals(ImportType.WORKFLOW));

		}
	}

	/**
	 * Itera sob os identificadores de array e os utiliza como critério para recuperação de coleções junto à base de dados.
	 * @param context Contexto do DSpace
	 * @param collectionsStr Lista de coleções oriundas de seleção do usuário
	 * @return Array de coleções
	 * @throws SQLException
	 */
	private Collection[] buildCollectionsArray(Context context, String[] collectionsStr) throws SQLException {
		Collection[] selectedCollections = new Collection[collectionsStr.length];
		
		int control = 0;
		for(String selectedCollection : collectionsStr)
		{
			selectedCollections[control] =  Collection.find(context,  NumberUtils.createInteger(selectedCollection));
			control++;
		}
		return selectedCollections;
	}

	/**
	 * Identifica pastas <i>aptas</i> para importação, a fim de evitar que um mesmo item seja importado mais de uma vez.
	 * @param folderStringList Listagem de identificadores de diretórios informada pelo usuário
	 * @param parentMapping Mapeamento de pastas pai/filho
	 * @return Identificadores de pastas aptas para importação
	 */
	private List<Long> identifiesImportableFolders(List<String> folderStringList, Map<Long, Long> parentMapping) {
		List<Long> filesToExport = new ArrayList<Long>();
		
		for(String selectedFolder : folderStringList)
		{
			Long folderId = Long.valueOf(selectedFolder);
			
			/** filho com diretório pai não selecioado, Caso seja diretório "pai" ou **/
			if(!(parentMapping.get(folderId) != null && folderStringList.contains(String.valueOf(parentMapping.get(folderId)))) || parentMapping.get(folderId) == null)
			{
				filesToExport.add(folderId);
			}
		}
		
		return filesToExport;
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
		mappingBuilder.append(FolderMetadataImportConstants.FOLDERIMPORT_MAPPING_FILE_PREFIX);
		mappingBuilder.append(new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()));
		
		return mappingBuilder.toString();
	}

	/**
	 * Efetua ações necessárias para renderização da página
	 * @param context
	 * @param request
	 * @param response
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 */
	private void preparePage(Context context, HttpServletRequest request,
			HttpServletResponse response) throws SQLException,
			ServletException, IOException {
		
    	
    	HttpSession session = request.getSession();

		Long rootBaseFolderSelected = getRootSelectedFolderId(request);
		File selectedFolder = getRootSelectedFolder(request, rootBaseFolderSelected );
    	
    	/** Assegura erros de conversão **/
		if(selectedFolder != null && selectedFolder.exists())
		{
			@SuppressWarnings("unchecked")
			Map<Long, String> userReadbleRoot = (Map<Long, String>)session.getAttribute(FolderMetadataImportConstants.USER_DATA_READBLE_KEY_ROOT);
			
			FolderReader folderReader = new FolderReader(selectedFolder);
			FolderAnalyseResult buildTree = folderReader.buildTree();
			
			String rootImportFolder = userReadbleRoot.get(Long.valueOf(rootBaseFolderSelected));

			if(!buildTree.isAllImportsAreFinished())
			{
				session.setAttribute(FolderMetadataImportConstants.USER_DATA_READBLE_KEY, buildTree.getUserReadble());
				session.setAttribute(FolderMetadataImportConstants.SERVER_DATA_READBLE, buildTree.getServerReadble());
				session.setAttribute(FolderMetadataImportConstants.PARENT_FOLDER_MAPPPING, buildTree.getMappingParent());
				
				request.setAttribute("exportDateTime", rootImportFolder);
			}
			else
			{
				request.setAttribute("has-error", false);
				request.setAttribute("blockpage", true);
				request.setAttribute("message", FolderMetadataImportConstants.KEY_MESSAGE_ALL_FOLDERS_IMPORTED);
				request.setAttribute("messageParam", rootImportFolder);
			}
		}
    	
    	/** Recupera todas coleçõs **/
    	fillRequestAttributes(context, request);
    	JSPManager.showJSP(request, response, "/dspace-admin/foldermetadataimport.jsp");
	}
	
	private Long getRootSelectedFolderId(HttpServletRequest request)
	{
    	String baseFolderNumber = request.getParameter("selectedFolder");
    	return NumberUtils.isNumber(baseFolderNumber) ? Long.valueOf(baseFolderNumber) : null;
	}
	
	private File getRootSelectedFolder(HttpServletRequest request, Long rootBaseFolderSelected)
	{
		@SuppressWarnings("unchecked")
		Map<Long, File> serverMappingRoot = (Map<Long, File>) request.getSession().getAttribute(FolderMetadataImportConstants.SERVER_DATA_READBLE_KEY_ROOT);
		return serverMappingRoot.get(Long.valueOf(rootBaseFolderSelected));
	}

	
	/**
	 * Preenche atributos a serem apresentados em tela os quais tem tempo de vida igual a "request"
	 * @param context Contexto do DSpace
	 * @param request HttpServletRequest
	 * @throws SQLException
	 */
	private void fillRequestAttributes(Context context,
			HttpServletRequest request) throws SQLException {
		request.setAttribute("selectedFolder", request.getParameter("selectedFolder"));
		request.setAttribute("collections", Arrays.asList(Collection.findAll(context)));
    	request.setAttribute("supportedTypes", supportedImportTypes);
	}

    
}