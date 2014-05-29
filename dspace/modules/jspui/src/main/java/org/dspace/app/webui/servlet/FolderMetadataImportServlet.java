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
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;
import org.dspace.app.webui.util.JSPManager;
import org.dspace.authorize.AuthorizeException;
import org.dspace.content.Collection;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Context;
import org.dspace.itemimport.FolderMetadataImport;


/**
 * Servlet responsável por tratar requisições de importação de metadados. <br>
 * A referida funcionalidade pode ser intepretada como uma <i>interface gráfica</i> para o comando <code>dspace import</code>
 * @author Márcio Ribeiro Gurgel do Amaral
 *
 */
public class FolderMetadataImportServlet extends DSpaceServlet
{
	
	
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
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doDSPost(Context context, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			SQLException, AuthorizeException {
		
		String folderLocation = request.getParameter("folder_location");
		String importTypeStr = request.getParameter("import_type");
		String collectionStr = request.getParameter("collection");
		
		Integer idImportType = NumberUtils.isNumber(importTypeStr) ? NumberUtils.createInteger(importTypeStr) : null;
		Integer idCollection = NumberUtils.isNumber(collectionStr) ? NumberUtils.createInteger(collectionStr) : null;
		
		ImportType importType = ImportType.getById(idImportType);
		
		Collection foundCollection = Collection.find(context, idCollection);

		FolderMetadataImport myloader = new FolderMetadataImport();
		try {
			myloader.addItems(context, new Collection[]{foundCollection}, folderLocation, getMappingFileLocation(), false, 
					importType.equals(ImportType.TEST), false, false, importType.equals(ImportType.WORKFLOW));
		} catch (Exception e1) {
			e1.printStackTrace();
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

	
	/**
	 *   if ("add".equals(command))
                {
                    myloader.addItems(c, mycollections, sourcedir, mapfile, template);
                }
                else if ("replace".equals(command))
                {
                    myloader.replaceItems(c, mycollections, sourcedir, mapfile, template);
                }
                else if ("delete".equals(command))
                {
                    myloader.deleteItems(c, mapfile);
                }
                else if ("add-bte".equals(command))
                {
                    myloader.addBTEItems(c, mycollections, sourcedir, mapfile, template, bteInputType, null);
                }

	 */
	
	/**
	 * Recupera informações necessárias para renderização de tela a encaminha para renderização.
	 * 
	 * @param context Contexto do DSpace
	 * @param request requisição HTTP
	 * @param response reposta HTTP
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException
	 * @throws AuthorizeException
	 */
    protected void doDSGet(Context context, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException
    {
    	/** Recupera todas coleçõs **/
    	request.setAttribute("collections", Arrays.asList(Collection.findAll(context)));
    	request.setAttribute("supportedTypes", supportedImportTypes);
    	JSPManager.showJSP(request, response, "/dspace-admin/foldermetadataimport.jsp");
    }

}

/**
 * Registra tipos possíveis de importação
 * @author Márcio Ribeiro Gurgel do Amaral
 *
 */
enum ImportType {
	

	TEST(1, "jsp.dspace-admin.foldermetadataimport.type.test"),
	WORKFLOW(2, "jsp.dspace-admin.foldermetadataimport.type.workflow"),
	IMPORT(3, "jsp.dspace-admin.foldermetadataimport.type.import");
	
	private Integer id;
	private String key;

	private ImportType(Integer id, String key) {
		this.id = id;
		this.key = key;
	}

	/**
	 * Recupera instância de enum tendo como base (de busca) o <i>ID</i>
	 * @param id Parâmetro-base para busca
	 * @return Instância encontrada
	 */
	public static ImportType getById(Integer id) {
		
		for(ImportType importType : EnumSet.allOf(ImportType.class)) {
			
			if(importType.getId().equals(id)) {
				
				return importType;
			}
		}
		return null;
	}

	public Integer getId() {
		return id;
	}

	public String get18nKey() {
		return key;
	}
	
}