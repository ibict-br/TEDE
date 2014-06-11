package org.dspace.file.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import junit.framework.Assert;

import org.dspace.folderimport.constants.FolderMetadataImportConstants;
import org.junit.Test;


/**
 * Testa funcionalidades sob {@link FileUtils}
 * @author Márcio Ribeiro Gurgel do Amaral
 *
 */
public class FileUtilsTest {
	
	
	/**
	 * Efetua teste do método {@link FileUtils#searchRecursive(File, String)}
	 * @throws EmptyFolderException 
	 */
	@Test
	public void testSearchRecursive() 
	{
		Assert.assertEquals(true, FileUtils.searchRecursive(new File("/work/dspace/install/tede/exports/20140429081811"), "dublin_core.xml"));
	}
	
	/**
	 * Efetua teste do método {@link FileUtils#searchRecursive(File, String)}
	 */
	@Test
	public void testSearchRecursiveWithParent()
	{
		List<File> storageList = FileUtils.searchRecursiveAddingDirs(new File("/work/dspace/install/tede/exports/20140610141126"), "dublin_core.xml", 2, false);
		Assert.assertEquals(true, !storageList.isEmpty());
	}
	
	@Test
	public void testSearchRecusriveToFindMappings()
	{
		List<File> storageList = FileUtils.searchRecursiveAddingDirs(new File("/work/dspace/install/tede/imports"), FolderMetadataImportConstants.FOLDERIMPORT_MAPPING_FILE_PREFIX, 0, true);
		Assert.assertEquals(true, !storageList.isEmpty());
	}
	
	@Test
	public void testReadFile() throws IOException
	{
		List<String> readFile = FileUtils.readFile(new File("/work/dspace/install/tede/imports/mapping-20140607114707"), 1);
		Assert.assertEquals(true, !readFile.isEmpty());
	}
	
	
	
}
