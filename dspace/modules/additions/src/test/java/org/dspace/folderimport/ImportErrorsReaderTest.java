package org.dspace.folderimport;

import java.io.IOException;
import java.util.Map;

import junit.framework.Assert;

import org.dspace.folderimport.dto.ErrorImportRegistry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ImportErrorsReaderTest {

	
	@Test
	public void testListErrorsImport() throws IOException
	{
		Map<Long, ErrorImportRegistry> listErrorsImport = ImportErrorsReader.listErrorsImport();
		Assert.assertTrue(listErrorsImport.size() > 0);
	}
	
}
