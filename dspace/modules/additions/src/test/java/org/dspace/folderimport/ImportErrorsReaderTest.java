package org.dspace.folderimport;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import junit.framework.Assert;

import org.dspace.folderimport.dto.ErrorImportRegistry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Unit test class for {@link ImportErrorsReader}
 * @author Márcio Ribeiro Gurgel do Amaral (marcio.rga@gmail.com)
 *
 */
@RunWith(JUnit4.class)
public class ImportErrorsReaderTest {

	
	@Test
	public void testListErrorsImport() throws IOException
	{
		Map<Long, ErrorImportRegistry> listErrorsImport = ImportErrorsReader.listErrorsImport(new File("/work/dspace/install/tede/exports/20140701120219/"));
		Assert.assertTrue(listErrorsImport.size() > 0);
	}
	
}
