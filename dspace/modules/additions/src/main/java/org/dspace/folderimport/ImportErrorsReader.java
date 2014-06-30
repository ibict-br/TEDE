package org.dspace.folderimport;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.dspace.app.itemupdate.DtoMetadata;
import org.dspace.app.itemupdate.MetadataUtilities;
import org.dspace.file.utils.FileUtils;
import org.dspace.folderimport.constants.FolderMetadataImportConstants;
import org.dspace.folderimport.dto.ErrorImportRegistry;

/**
 * Utilidade que visam tornar visíveis itens que por motivo de erro não foram importados na funcionalidade <b>FolderMetadataImport</b>
 * @author Márcio Ribeiro Gurgel do Amaral (marcio.rga@gmail.com)
 *
 */
public class ImportErrorsReader {

	private static Logger logger = Logger.getLogger(ImportErrorsReader.class);
	private static DocumentBuilder builder;
	
	
	/**
	 * Constrói DTO representativo de itens (e seus arquivos) que sofreram erro no processo de importação.
	 * @return DTO preenchido com informações de itens importados com erro
	 * @throws IOException
	 */
	public static Map<Long, ErrorImportRegistry> listErrorsImport() throws IOException
	{
		Map<Long, ErrorImportRegistry> analisysResult = null;
		
		File importFolder = new File(FileUtils.getImportFolderName());
		/** O diretório de importação não é nativo do DSpace, logo se faz necessária sua criação **/
		if(!importFolder.exists())
		{
			importFolder.mkdirs();
		}
			
		/** Recupera arquivos de registro de erros **/
		List<File> foundFiles = FileUtils.searchFileNoDepthListReturn(importFolder, FolderMetadataImportConstants.FOLDERIMPORT_ERROR_MAPPING_FILE_SUFFIX);
		
		if(!foundFiles.isEmpty())
		{
			List<String> foldersContainingErrorItems = new ArrayList<String>();
			
			/** Cada arquivo "fundFiles" contém referências as pastas contenedoras de itens com problema de importação **/
			for(File foundFile : foundFiles)
			{
				foldersContainingErrorItems.addAll(FileUtils.readFile(foundFile, Integer.MAX_VALUE));
			}
			
			/** Devem existir registros nos arquivos **/
			if(!foldersContainingErrorItems.isEmpty())
			{
				if(analisysResult == null)
				{
					analisysResult = new LinkedHashMap<Long, ErrorImportRegistry>();
				}
				
				Set<Long> identifiersForFolders = new HashSet<Long>();
				
				for(String folderItemContainerCandidate : foldersContainingErrorItems)
				{
					File folderItemContainer = new File(folderItemContainerCandidate);
					
					/** Somente diretórios armazenam conjunto de arquivos para exportação **/
					if(folderItemContainer.exists() && folderItemContainer.isDirectory())
					{
						try 
						{
							/** Recupera título **/
							File dublinCoreXmlFile = new File(folderItemContainer, FolderMetadataImportConstants.DUBLIN_CORE_XML);
							List<DtoMetadata> loadDublinCore = MetadataUtilities.loadDublinCore(getDocumentBuilder(), new FileInputStream(dublinCoreXmlFile));
							String title = searchMetadata(loadDublinCore, "dc.title");
							
							if(title != null)
							{
								ErrorImportRegistry errorImportRegistry = new ErrorImportRegistry();
								errorImportRegistry.setTitle(title);
								
								LinkedHashMap<Long, File> itemRegistry = new LinkedHashMap<Long, File>();
								
								Set<Long> identifiersForFiles = new HashSet<Long>();

								
								for(File itemContent : folderItemContainer.listFiles())
								{
									if(itemContent.isFile())
									{
										long unusedKey = FileUtils.garanteeUnusedKey(identifiersForFiles);
										itemRegistry.put(unusedKey, itemContent);
									}
								}
								
								errorImportRegistry.setItemFiles(itemRegistry);
								
								analisysResult.put(FileUtils.garanteeUnusedKey(identifiersForFolders), errorImportRegistry);
							}
							
						} catch (Exception e) {
							logger.error(e.getMessage(), e);
						}
						
					}
				}
			}
		
		}
		
		return analisysResult;
	}
	
	
	/**
	 * Itera sob a lista de metadados recebidos, na intenção de encontrar a 
	 * ocorrência de metadado informada por parâmetro.
	 * @param registries Lista contenedora de metadados de um item
	 * @param metadata Metadado a ser recuperado
	 * @return Valor do metadado encontrado
	 */
	private static String searchMetadata(List<DtoMetadata> registries, String metadata) {
		
		for(DtoMetadata currentDto : registries)
		{
			if(currentDto.matches(metadata, false))
			{
				return currentDto.getValue();
			}
		}
		
		return null;
	}


	private static DocumentBuilder getDocumentBuilder()
	throws ParserConfigurationException
	{
		if (builder == null)
		{
		    builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		}
		return builder;
	}
	
	
}
