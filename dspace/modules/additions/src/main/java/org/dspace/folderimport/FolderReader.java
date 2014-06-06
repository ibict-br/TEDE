package org.dspace.folderimport;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.dspace.file.utils.FileUtils;
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
	 */
    public FolderAnalyseResult buildTree()
    {
    	Map<Long, String> userReadble = new LinkedHashMap<Long, String>();
    	Map<Long, File> serverReadble = new LinkedHashMap<Long, File>();
    	Map<File, Long> reverseServerReadble = new LinkedHashMap<File, Long>();
    	Map<Long, Long> mappingParent = new HashMap<Long, Long>();
    	
    	Set<Long> keyControl = new HashSet<Long>();

		try 
		{
			/** "new file" para assegurar que "root" e "searchBase" sejam objetos distintos **/
			innerBuild(new File(root.getCanonicalPath()), userReadble, serverReadble, keyControl, reverseServerReadble, mappingParent);
		} 
		catch (IOException e) 
		{
			logger.error(e.getMessage(), e);
		}
    
    	return new FolderAnalyseResult(userReadble, serverReadble, mappingParent);
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
