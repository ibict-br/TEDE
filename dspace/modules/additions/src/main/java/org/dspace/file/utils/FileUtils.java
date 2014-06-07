package org.dspace.file.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
	 */
	public static boolean searchRecursive(File metadataFoldersCandidate, String searchName) {
		
		for(File metadataContainerCandidate : metadataFoldersCandidate.listFiles())
		{
			if(metadataContainerCandidate.isDirectory())
			{
				return searchRecursive(metadataContainerCandidate, searchName);
			}
			else
			{
				if(metadataContainerCandidate.getName().equals(searchName))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Efetua busca recursiva em cima de diretório a fim de identificar existência de arquivo <b>searchName</b> nas subpastas do diretório recebido via parâmetro.<br>
	 * As ocorrências encontradas serão registradas em lista
	 * @param storageList Lista para armazenamento dos resultados encontrados
	 * @param metadataFoldersCandidate Diretório a ser verificado
	 * @param searchName Critério de busca
	 * @param useContains Indicador para utilização de <i>contains</i> ao invés de <i>equals</i>
	 * @return Identificador de arquivo encontrado
	 */
	public static void searchRecursiveAddingDirs(List<File> storageList, File metadataFoldersCandidate, String searchName, int depthToStore, boolean useContains) {
		
		for(File metadataContainerCandidate : metadataFoldersCandidate.listFiles())
		{
			if(metadataContainerCandidate.isDirectory())
			{
				searchRecursiveAddingDirs(storageList, metadataContainerCandidate, searchName, depthToStore, useContains);
			}
			else
			{
				if(!useContains ? metadataContainerCandidate.getName().equals(searchName) : metadataContainerCandidate.getName().contains(searchName))
				{
					if(depthToStore > 0)
					{
						File parentFile = null;
						for(int i = 0; i < depthToStore; i++)
						{
							parentFile = (parentFile == null) ? metadataContainerCandidate.getParentFile() : parentFile.getParentFile();
						}
						if(!storageList.contains(parentFile))
						{
							storageList.add(parentFile);
						}
					}
					else
					{
						if(!storageList.contains(metadataContainerCandidate))
						{
							storageList.add(metadataContainerCandidate);
						}
					}
				}
			}
		}
	}
	
}
