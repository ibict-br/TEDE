package org.dspace.folderimport.dto;

import java.io.File;
import java.util.Map;


public class ErrorImportRegistry {
	
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
