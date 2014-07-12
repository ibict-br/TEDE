package org.dspace.export;

import java.sql.SQLException;

import junit.framework.Assert;

import org.dspace.content.Item;
import org.dspace.core.Context;
import org.dspace.export.domain.ExportType;
import org.junit.Test;

/**
 * Test class for {@link ItemExportFormats}
 * @author Márcio Ribeiro Gurgel do Amaral (marcio.rga@gmail.com)
 *
 */
public class ItemExportFormatsTest {

	
	/**
	 * Test for method {@link ItemExportFormats#process(org.dspace.content.Item, org.dspace.export.domain.ExportType)}
	 * @throws SQLException 
	 */
	@Test
	public void testProcess() throws SQLException
	{
		Item testItem = Item.find(new Context(), 3598);
		ItemExportFormats itemExportFormats = new ItemExportFormats();
		ItemExportDTO process = itemExportFormats.process(testItem, ExportType.CITATION);

		Assert.assertNotNull(process);
		
	}
	
	
}
