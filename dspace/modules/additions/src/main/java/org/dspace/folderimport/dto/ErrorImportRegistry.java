package org.dspace.folderimport.dto;

import java.io.File;
import java.io.Serializable;
import java.util.Map;


/**
 * DTO para registro de erros de importação
 * @author Márcio Ribeiro Gurgel do Amaral (marcio.rga@gmail.com)
 *
 */
public class ErrorImportRegistry implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String title;
	private Map<Long, File> itemFiles;

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Map<Long, File> getItemFiles() {
		return itemFiles;
	}
	public void setItemFiles(Map<Long, File> itemFiles) {
		this.itemFiles = itemFiles;
	}
	
}
