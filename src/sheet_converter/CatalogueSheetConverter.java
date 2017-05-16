package sheet_converter;

import java.util.HashMap;

import org.apache.poi.ss.usermodel.Row;
import data_transformation.ValuesGrouper;

/**
 * Xml Converter for catalogue sheet. We parse the xml to retrieve the catalogue data.
 * The catalogue data are then inserted into the catalogue sheet
 * @author avonva
 *
 */
public class CatalogueSheetConverter extends ExtendedSheetConverter {
	
	boolean isGroupNode;  // if we are analyzing a catalogueGroups node or not
	ValuesGrouper group;  // used to save multiple values into a single cell
	
	public CatalogueSheetConverter(String inputFilename, String rootNode ) {
		super(inputFilename, rootNode);
		isGroupNode = false;
	}
	
	@Override
	public void startElement( Row row, String nodeName ) {

		// if we found a catalogue groups element
		if ( nodeName.equals( "catalogueGroups" ) ) {
			isGroupNode = true;
			group = new ValuesGrouper();
		}
	}
	

	@Override
	public void addElement(Row row, String nodeName, String value) {
		
		// call parent add element
		super.addElement( row, nodeName, value );
		
		// these nodes were already processed by the parent call
		switch ( nodeName ) {
		case "validFrom":
		case "validTo":
		case "lastUpdate":
		case "status":
			return;
		}
		
		// process node
		switch ( nodeName ) {
		case "catalogueGroups":
			// add as groups all the groups which were found, $ separated
			if ( isGroupNode )
				createCell( nodeName, row, group.getCompactValues() );
			
			isGroupNode = false;
			group = null;
			break;
			
		case "catalogueGroup":
			
			if ( isGroupNode )
				group.addValue( value );
			break;
			
		default:
			
			// create a cell for the current row in the right column
			createCell( nodeName, row, value );
			break;
		}
	}

	
	/**
	 * Get the headers of the catalogue sheet
	 * @return
	 */
	public HashMap<String, SheetHeader> getHeaders() {
		
		HashMap<String, SheetHeader> headers = new HashMap<>();
		
		// prepare headers for the catalogue sheet
		// the order of headers in the headers array reflect
		// the order of the excel columns
		headers.put("code", new SheetHeader(0, "code") );
		headers.put("name", new SheetHeader(1, "name") );
		headers.put("label", new SheetHeader(2, "label") );
		headers.put("scopeNote", new SheetHeader(3, "scopeNote") );
		headers.put("termCodeMask", new SheetHeader(4, "termCodeMask") );
		headers.put("termCodeLength", new SheetHeader(5, "termCodeLength") );
		headers.put("termMinCode", new SheetHeader(6, "termMinCode") );
		headers.put("acceptNonStandardCodes", new SheetHeader(7, "acceptNonStandardCodes") );
		headers.put("generateMissingCodes", new SheetHeader(8, "generateMissingCodes") );
		headers.put("version", new SheetHeader(9, "version") );
		headers.put("catalogueGroups", new SheetHeader(10, "catalogueGroups") );
		headers.put("lastUpdate", new SheetHeader(11, "lastUpdate") );
		headers.put("validFrom", new SheetHeader(12, "validFrom") );
		headers.put("validTo", new SheetHeader(13, "validTo") );
		headers.put("status", new SheetHeader(14, "status") );
		headers.put("deprecated", new SheetHeader(15, "deprecated") );

		return headers;
	}

}
