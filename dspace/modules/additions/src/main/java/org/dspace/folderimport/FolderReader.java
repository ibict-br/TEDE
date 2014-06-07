package org.dspace.folderimport;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.dspace.core.ConfigurationManager;
import org.dspace.file.utils.FileUtils;
import org.dspace.folderimport.constants.FolderMetadataImportConstants;
import org.dspace.folderimport.dto.FolderAnalyseResult;


/**
 * Engrenagem responsável pela leitura e lógica de identificação de diretórios passíveis de importação de metadados.
 * @author Márcio Ribeiro Gurgel do Amaral
 *
 */
public class FolderReader {
	
	private static Logger logger = Logger.getLogger(FolderReader.class);
	
	private static final int LENGTH_BASE_TO_IDENTIFY_DATE_PATTERN = 12;
	private static final String DUBLIN_CORE_XML_FILE_NAME = "dublin_core.xml";
	private File root;
	
	public FolderReader(File root)
	{
		this.root = root;
	}

	/**
	 * Constrói árvore de diretórios aptos para a importação de itens
	 * @param fileBase Diretório base para construção da árvore
	 * @return Objeto contendo resultado da análise de diretórios
	 * @throws IOException 
	 */
    public FolderAnalyseResult buildTree() throws IOException
    {
    	Map<Long, String> userReadble = new LinkedHashMap<Long, String>();
    	Map<Long, File> serverReadble = new LinkedHashMap<Long, File>();
    	Map<File, Long> reverseServerReadble = new LinkedHashMap<File, Long>();
    	Map<Long, Long> mappingParent = new HashMap<Long, Long>();
    	
    	Set<Long> keyControl = new HashSet<Long>();
    	boolean allImportsAreFinished = false;

		try 
		{
			/** "new file" para assegurar que "root" e "searchBase" sejam objetos distintos **/
			innerBuild(new File(root.getCanonicalPath()), userReadble, serverReadble, keyControl, reverseServerReadble, mappingParent);
			
			/** A existência de valores em "mappingParent" garante a existência de diretórios **/
			if(!mappingParent.isEmpty())
			{
				allImportsAreFinished = removeImportedFolders(userReadble, serverReadble, reverseServerReadble, mappingParent);
			}
		} 
		catch (IOException e) 
		{
			logger.error(e.getMessage(), e);
			throw e;
		}
    
    	FolderAnalyseResult folderAnalyseResult = new FolderAnalyseResult(userReadble, serverReadble, mappingParent);
    	folderAnalyseResult.setAllImportsAreFinished(allImportsAreFinished);
		return folderAnalyseResult;
    }

	private boolean removeImportedFolders(Map<Long, String> userReadble, Map<Long, File> serverReadble, 
			Map<File, Long> reverseServerReadble, Map<Long, Long> mappingParent) throws IOException {
		List<File> listPreviousImport = listPreviousImport();
		
		Map<Long, Long> mappingParentCopy = new HashMap<Long, Long>(mappingParent);
		boolean hasInitialContent = !serverReadble.isEmpty();
		
		if(listPreviousImport != null && !listPreviousImport.isEmpty())
		{
			for(File importedDir : listPreviousImport)
			{
				clearMaps(userReadble, serverReadble, reverseServerReadble, mappingParent, reverseServerReadble.get(importedDir));
			}
			
			for(Map.Entry<Long, Long> parentCandidate : mappingParentCopy.entrySet())
			{
				
				if(parentCandidate.getValue() == null)
				{
					boolean allChildrenIsImported = true;
					/** Trata-se de um diretório pai, logo, verifica se todos seus filhos já foram importados **/
					File parentFolder = serverReadble.get(parentCandidate.getKey());
					List<File> parentChildren = new ArrayList<File>();
					FileUtils.searchRecursiveAddingDirs(parentChildren, parentFolder, DUBLIN_CORE_XML_FILE_NAME, 2, false);
					
					for(File folderChild : parentChildren)
					{
						if(!listPreviousImport.contains(folderChild))
						{
							allChildrenIsImported = false;
							break;
						}
					}
					
					if(allChildrenIsImported)
					{
						clearMaps(userReadble, serverReadble, reverseServerReadble, mappingParent, parentCandidate.getKey());
					}
					
				}
			}
		}
		
		return hasInitialContent && serverReadble.isEmpty();
	}

	private void clearMaps(Map<Long, String> userReadble,
			Map<Long, File> serverReadble,
			Map<File, Long> reverseServerReadble,
			Map<Long, Long> mappingParent, Long idToRemove) {
		
		
		reverseServerReadble.remove(serverReadble.get(idToRemove));
		userReadble.remove(idToRemove);
		serverReadble.remove(idToRemove);
		mappingParent.remove(idToRemove);
	}
	
	private int countChildren(Long idParent, Map<Long, Long> mappingParent)
	{
		int counter = 0;
		for(Map.Entry<Long, Long> parentMapping : mappingParent.entrySet())
		{
			if(parentMapping.getValue() != null && parentMapping.getValue().equals(idParent))
			{
				counter++;
			}
		}
		
		return counter;
	}

	private List<File> listPreviousImport()
			throws IOException {
		List<File> folderWithImport = null;
    	/** Identifica diretórios já carregados **/
		StringBuilder mappingBuilder = new StringBuilder();
		mappingBuilder.append(ConfigurationManager.getProperty("dspace.dir"));
		mappingBuilder.append(File.separator);
		mappingBuilder.append("imports");
		
		List<File> foundImportMappings = new ArrayList<File>();
		FileUtils.searchRecursiveAddingDirs(foundImportMappings, new File(mappingBuilder.toString()), FolderMetadataImportConstants.FOLDERIMPORT_MAPPING_FILE_PREFIX, 0, true);

		
		if(!foundImportMappings.isEmpty())
		{
			/** Já existem importações **/
			for(File foundMappingFile : foundImportMappings)
			{
				
				List<String> firstLineMappingFile = FileUtils.readFile(foundMappingFile, 1);
				if(firstLineMappingFile != null && !firstLineMappingFile.isEmpty())
				{
					String lineFolderCandidate = firstLineMappingFile.get(0);
					if(lineFolderCandidate != null && !firstLineMappingFile.isEmpty())
					{
						File exportedFolder = new File(lineFolderCandidate.trim());
						if(exportedFolder.exists() && exportedFolder.isDirectory())
						{
							if(folderWithImport == null)
							{
								folderWithImport = new ArrayList<File>();
							}
							
							folderWithImport.add(exportedFolder);
						}
					}
						
				}
				
			}
		}
		
		return folderWithImport;
	}
    
    
    /**
     * Identifica diretórios aptos a serem marcados para importação
     * @param folderToAdd Indica, via nome, se algum conjunto de diretório deve ser considerado para compor o diretório final.
     * @return DTO contendo informações da exportação
     */
    public FolderAnalyseResult listAvailableExport(Locale locale, String folderToAdd)
    {
    	Map<Long, String> userReadble = new LinkedHashMap<Long, String>();
    	Map<Long, File> serverReadble = new LinkedHashMap<Long, File>();
    	Set<Long> keyControl = new HashSet<Long>();

    	for(File folder : root.listFiles())
    	{
    		if(folder.isDirectory())
    		{
    			boolean metadataFound = FileUtils.searchRecursive(folder, DUBLIN_CORE_XML_FILE_NAME);
    			if(metadataFound)
    			{
    				long garanteeUnsedKey = garanteeUnusedKey(keyControl);
					userReadble.put(garanteeUnsedKey, formatFolderName(folder, locale));
					try {
						serverReadble.put(garanteeUnsedKey, folderToAdd != null ? new File(folder.getCanonicalPath() + File.separatorChar + folderToAdd) : folder);
					} catch (IOException e) {
						logger.error(e.getMessage(), e);
					}
    			}
    		}
    	}
    	
    	return new FolderAnalyseResult(userReadble, serverReadble, null);
    }

    
    /**
     * Verifica se determinada pasta tem seu nome composto por data no padrão <i>yyyyMMddhhmmss</i><br>
     * Caso positivo, efetua conversão para formato legível a humano
     * @param folder Diretório a ser analisado
     * @param locale Indicativo de local do usuário logado na aplicação
     * @return Caso o padrão de data seja válido, data formatada, caso contrário o nome da pasta
     */
	private String formatFolderName(File folder, Locale locale) 
	{
		if(NumberUtils.isNumber(folder.getName()) && folder.getName().length() >= LENGTH_BASE_TO_IDENTIFY_DATE_PATTERN)
		{
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
			String exportDateTime = null;
			try{
				exportDateTime = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.DEFAULT, locale).format(simpleDateFormat.parse(folder.getName()));
			} catch (ParseException e) {
				logger.error(e.getMessage(), e);
			}
			
			return exportDateTime;
		}
		return folder.getName();
	}

    
    /**
     * Efetua chamadas recursivas para identificações de diretórios passíveis de seleção para exportação
     * @param fileBase Arquivo base
     * @param userReadble Mapa de dados legíveis ao usuário
     * @param serverReadble Dados guardados ao servidor de aplicações, por motivo de segurança
     * @param keyControl Controle de chaves identificadoras
     * @param mappingParent 
     */
	private void innerBuild(File fileBase, Map<Long, String> userReadble, Map<Long, File> serverReadble, Set<Long> keyControl, Map<File, Long> reverseServerReadble, Map<Long, Long> mappingParent) {
		
		if(!FileUtils.searchFileNoDepth(fileBase, DUBLIN_CORE_XML_FILE_NAME) && FileUtils.searchRecursive(fileBase, DUBLIN_CORE_XML_FILE_NAME))
    	{
    		if(!fileBase.equals(root))
    		{
    			long keyAssotiation = garanteeUnusedKey(keyControl);
    			boolean isParentFolder = fileBase.getParentFile() != null && !fileBase.getParentFile().equals(root);
    			serverReadble.put(keyAssotiation, fileBase);
    			reverseServerReadble.put(fileBase, keyAssotiation);
    			mappingParent.put(keyAssotiation, reverseServerReadble.get(fileBase.getParentFile()));
				userReadble.put(keyAssotiation, (isParentFolder ? fileBase.getParentFile().getName() + "/" : "") + fileBase.getName());
    		}
    		
    		for(File currentFile : fileBase.listFiles())
    		{
    			innerBuild(currentFile, userReadble, serverReadble, keyControl, reverseServerReadble, mappingParent);
    		}
    	}
	}

	/**
	 * Garante unicidade na obtenção de de números randômicos únicos
	 * @param keyControl Controle de chaves
	 * @return Chave única
	 */
	private long garanteeUnusedKey(Set<Long> keyControl) {
		
		long keyAssotiation = new Random().nextLong();
		if(keyControl.contains(keyAssotiation))
		{
			return garanteeUnusedKey(keyControl);
		}
		
		keyControl.add(keyAssotiation);
		return keyAssotiation;
	}
    
}
