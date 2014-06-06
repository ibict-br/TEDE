package org.dspace.file.utils;

import java.io.File;

import org.junit.Test;

import junit.framework.Assert;


/**
 * Testa funcionalidades sob {@link FileUtils}
 * @author Márcio Ribeiro Gurgel do Amaral
 *
 */
public class FileUtilsTest {
	
	
	/**
	 * Efetua teste do método {@link FileUtils#searchRecursive(File, String)}
	 */
	@Test
	public void testSearchRecursive()
	{
		Assert.assertEquals(true, FileUtils.searchRecursive(new File("/work/dspace/install/tede/exports/20140429081811"), "dublin_core.xml"));
	}
	
	
}
