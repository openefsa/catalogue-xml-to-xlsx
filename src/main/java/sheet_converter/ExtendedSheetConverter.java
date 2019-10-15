package sheet_converter;

import java.util.Date;

import org.apache.poi.ss.usermodel.Row;

import data_transformation.BooleanConverter;
import data_transformation.DateTrimmer;
import naming_convention.Headers;
import naming_convention.SpecialValues;

/**
 * This class adds some peculiar processes to the sheet converter, in order to make it
 * more specific for catalogues xml. In particular, we added an initial phase where dates
 * and the status field are treated (this part is common to every catalogue and therefore
 * we create another class to avoid code repetition)
 * @author avonva
 *
 */
public abstract class ExtendedSheetConverter extends SheetConverter {

	public ExtendedSheetConverter(String inputFilename, String rootNode) {
		super(inputFilename, rootNode);
	}

	@Override
	public void addElement(Row row, String nodeName, String value) {
		
		// Perform checks which are common to all sheets
		// trim dates only to year month and day
		switch ( nodeName ) {
		
		case XmlNodes.VALID_FROM:
		case XmlNodes.VALID_TO:
		case XmlNodes.LAST_UPDATE:
		case XmlNodes.NOTES_VERSION_DATE:
			
			Date date = DateTrimmer.trimDate( value );
			
			// create a cell with a date not with a string
			createCell ( nodeName, row, date );
			break;

			// set the deprecated column according to the status
		case XmlNodes.STATUS:

			// create the deprecated cell, if the value is DEPRECATED, then set to 1, otherwise 0
			createCell ( Headers.DEPRECATED, row, BooleanConverter.equals( value, SpecialValues.STATUS_DEPRECATED ) );
			
			// create the cell for the status
			createCell ( nodeName, row, value );
			break;
		}
	}
}
