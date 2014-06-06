package org.dspace.file.utils;

import java.io.File;

/**
 * Dispõe de funcionalidades utilitárias para manuseio de arquivos.
 * @author Márcio Ribeiro Gurgel do Amaral
 *
 */
public class FileUtils {

	
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
	
}
