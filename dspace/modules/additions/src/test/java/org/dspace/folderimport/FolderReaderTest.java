package org.dspace.folderimport;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import junit.framework.Assert;

import org.dspace.folderimport.dto.FolderAnalyseResult;
import org.junit.Test;


/**
 * Testa funcionalidades sob {@link FolderReader}
 * @author Márcio Ribeiro Gurgel do Amaral
 *
 */
public class FolderReaderTest {

	
	/**
	 * Efetua teste do método {@link FolderReader#listAvailableExport()}
	 */
	@Test
	public void testListAvailableExport()
	{
		
		FolderReader folderReader = new FolderReader(new File("/work/dspace/install/tede/exports"));
		FolderAnalyseResult listAvailableExport = folderReader.listAvailableExport(new Locale("pt", "BR"), "exportados");
		
		Assert.assertNotNull(listAvailableExport);
		Assert.assertNotNull(listAvailableExport.getServerReadble());
		Assert.assertNotNull(listAvailableExport.getUserReadble());
		Assert.assertTrue(listAvailableExport.getUserReadble().size() > 0);
		Assert.assertTrue(listAvailableExport.getServerReadble().size() > 0);
		
	}
	
	/**
	 * Efetua teste do método {@link FolderReader#buildTree()}
	 */
	@Test
	public void testBuildTree()
	{
		
		FolderReader folderReader = new FolderReader(new File("/work/dspace/install/tede/exports/20140429091811/exportados"));
		FolderAnalyseResult buildTreeResult = folderReader.buildTree();
		
		Map<Long, File> serverReadble = buildTreeResult.getServerReadble();
		Assert.assertNotNull(buildTreeResult);
		Assert.assertNotNull(serverReadble);
		Assert.assertNotNull(buildTreeResult.getUserReadble());
		Assert.assertTrue(buildTreeResult.getUserReadble().size() > 0);
		Assert.assertTrue(serverReadble.size() > 0);
		
	}

	
}
