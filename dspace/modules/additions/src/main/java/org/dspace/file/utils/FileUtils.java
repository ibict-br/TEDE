package org.dspace.file.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.FileFilterUtils;

/**
 * Dispõe de funcionalidades utilitárias para manuseio de arquivos.
 * @author Márcio Ribeiro Gurgel do Amaral
 *
 */
public class FileUtils {

	
	/**
	 * Efetua busca em diretório, considerando a penas o diretório corrente.
	 * @param metadataFoldersCandidate Arquivo base para busca
	 * @param searchName Critério de busca
	 * @return Indicativo de arquivo encontrado
	 */
	public static boolean searchFileNoDepth(File metadataFoldersCandidate, String searchName) {
		
		for(File metadataContainerCandidate : metadataFoldersCandidate.listFiles())
		{
			if(metadataContainerCandidate.getName().equals(searchName))
			{
				return true;
			}
		}
		return false;
	}
	/**
	 * Efetua busca em diretório, considerando a penas o diretório corrente.
	 * @param metadataFoldersCandidate Arquivo base para busca
	 * @param searchName Critério de busca
	 * @return Listagem contendo arquivos encontrados com intermédio do operador "contains"
	 */
	public static List<File> searchFileNoDepthListReturn(File metadataFoldersCandidate, String searchName) {
		
		List<File> result = new ArrayList<File>();
		for(File metadataContainerCandidate : metadataFoldersCandidate.listFiles())
		{
			if(metadataContainerCandidate.getName().contains(searchName))
			{
				result.add(metadataContainerCandidate);
			}
		}
		return result;
	}
	
	
	/**
	 * 
	 * @param file
	 * @param linesToRead
	 * @return
	 * @throws IOException 
	 */
	public static List<String> readFile(File file, int linesToRead) throws IOException
	{
		if(!file.isDirectory())
		{
			List<String> foundLines = new ArrayList<String>();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
			String lineContent = null;
			int loopControl = 0;
			while((lineContent = bufferedReader.readLine()) != null && loopControl < linesToRead)
			{
				foundLines.add(lineContent);
				loopControl++;
			}
			
			bufferedReader.close();
			return foundLines;
		}
		
		return null;
	}
	
	/**
	 * Efetua busca recursiva em cima de diretório a fim de identificar existência de arquivo <b>searchName</b> em alguma
	 * sub pasta do diretório recebido via parâmetro.
	 * @param metadataFoldersCandidate Diretório a ser verificado
	 * @param searchName Critério de busca
	 * @return Identificador de arquivo encontrado
	 * @throws EmptyFolderException 
	 */
	public static boolean searchRecursive(File metadataFoldersCandidate, final String searchName)  {
		
		Collection<File> foundFiles = org.apache.commons.io.FileUtils.listFiles(metadataFoldersCandidate, FileFilterUtils.nameFileFilter(searchName, IOCase.SENSITIVE), FileFilterUtils.directoryFileFilter());
		return foundFiles != null && !foundFiles.isEmpty();
	}
	
	/**
	 * Efetua busca recursiva em cima de diretório a fim de identificar existência de arquivo <b>searchName</b> nas subpastas do diretório recebido via parâmetro.<br>
	 * As ocorrências encontradas serão registradas em lista
	 * @param metadataFoldersCandidate Diretório a ser verificado
	 * @param searchName Critério de busca
	 * @return Identificador de arquivo encontrado
	 */
	public static List<File> searchRecursiveAddingDirs(File metadataFoldersCandidate, final String searchName, int depthToStore) {
		
		List<File> listStoredValues = new ArrayList<File>();
		Collection<File> foundFiles = org.apache.commons.io.FileUtils.listFiles(metadataFoldersCandidate, FileFilterUtils.nameFileFilter(searchName, IOCase.SENSITIVE), FileFilterUtils.directoryFileFilter());
		
		for(File foundFile : foundFiles)
		{
			if(depthToStore > 0)
			{
				File parentFile = null;
				for(int i = 0; i < depthToStore; i++)
				{
					parentFile = (parentFile == null) ? foundFile.getParentFile() : parentFile.getParentFile();
				}
				if(!listStoredValues.contains(parentFile))
				{
					listStoredValues.add(parentFile);
				}
			}
			else
			{
				if(!listStoredValues.contains(foundFile))
				{
					listStoredValues.add(foundFile);
				}
			}
		}
		
		return listStoredValues;
	
	}
	
}
