package sheet_converter;

import java.util.HashMap;

import org.apache.poi.ss.usermodel.Row;
import org.xml.sax.Attributes;

import data_transformation.ValuesGrouper;
import naming_convention.Headers;
import sheet_header.SheetHeader;

/**
 * Xml Converter for catalogue sheet. We parse the xml to retrieve the catalogue data.
 * The catalogue data are then inserted into the catalogue sheet
 * @author avonva
 *
 */
public class CatalogueSheetConverter extends ExtendedSheetConverter {
	
	private boolean isGroupNode;  // if we are analyzing a catalogueGroups node or not
	private boolean parsingNotes;  // true if we are parsing the release notes
	private ValuesGrouper group;  // used to save multiple values into a single cell
	
	public CatalogueSheetConverter(String inputFilename, String rootNode ) {
		super(inputFilename, rootNode);
		isGroupNode = false;
		parsingNotes = false;
	}
	
	@Override
	public void startElement( Row row, String nodeName, Attributes attr ) {

		// if we found a catalogue groups element
		switch ( nodeName ) {
		
		case XmlNodes.CAT_GROUPS:
			isGroupNode = true;
			group = new ValuesGrouper();
			break;
			
		case XmlNodes.RELEASE_NOTES:
			parsingNotes = true;
			break;
			
		case XmlNodes.VERSION:
			
			// if release note version, get its attribute
			// we need to handle the nodeName in an exceptional
			// way, since the header "version" is already used
			// by the catalogue version
			if ( parsingNotes )
				createCell( XmlNodes.NOTES_VERSION, row, attr.getValue( 
						XmlNodes.NOTES_VERSION_ATTRIBUTE_NAME ) );

			break;
		default:
			break;
		}
	}
	

	@Override
	public void addElement(Row row, String nodeName, String value) {
		
		// call parent add element
		super.addElement( row, nodeName, value );
		
		// these nodes were already processed by the parent call
		switch ( nodeName ) {
		case XmlNodes.VALID_FROM:
		case XmlNodes.VALID_TO:
		case XmlNodes.LAST_UPDATE:
		case XmlNodes.STATUS:
			return;
		}
		
		// process node
		switch ( nodeName ) {
		case XmlNodes.CAT_GROUPS:
			// add as groups all the groups which were found, $ separated
			if ( isGroupNode )
				createCell( nodeName, row, group.getCompactValues() );
			
			isGroupNode = false;
			group = null;
			break;
			
		case XmlNodes.CAT_GROUP:
			
			if ( isGroupNode )
				group.addValue( value );
			break;

			// end release note parsing
		case XmlNodes.RELEASE_NOTES:
			parsingNotes = false;
			break;
			
		case XmlNodes.VERSION:
			// create the version cell only for the catalogue version
			if ( !parsingNotes )
				createCell( nodeName, row, value );
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
		headers.put( XmlNodes.CODE, new SheetHeader(0, Headers.CODE) );
		headers.put( XmlNodes.NAME, new SheetHeader(1, Headers.NAME) );
		headers.put( XmlNodes.LABEL, new SheetHeader(2, Headers.LABEL) );
		headers.put( XmlNodes.SCOPENOTE, new SheetHeader(3, Headers.SCOPENOTE) );
		headers.put( XmlNodes.CAT_CODE_MASK, new SheetHeader(4, Headers.CAT_CODE_MASK) );
		headers.put( XmlNodes.CAT_CODE_LENGTH, new SheetHeader(5, Headers.CAT_CODE_LENGTH) );
		headers.put( XmlNodes.CAT_MIN_CODE, new SheetHeader(6, Headers.CAT_MIN_CODE) );
		headers.put( XmlNodes.CAT_ACCEPT_NOT_STD, new SheetHeader(7, Headers.CAT_ACCEPT_NOT_STD) );
		headers.put( XmlNodes.CAT_GEN_MISSING, new SheetHeader(8, Headers.CAT_GEN_MISSING) );
		headers.put( XmlNodes.VERSION, new SheetHeader(9, Headers.VERSION) );
		headers.put( XmlNodes.CAT_GROUPS, new SheetHeader(10, Headers.CAT_GROUPS) );
		headers.put( XmlNodes.LAST_UPDATE, new SheetHeader(11, Headers.LAST_UPDATE) );
		headers.put( XmlNodes.VALID_FROM, new SheetHeader(12, Headers.VALID_FROM) );
		headers.put( XmlNodes.VALID_TO, new SheetHeader(13, Headers.VALID_TO) );
		headers.put( XmlNodes.STATUS, new SheetHeader(14, Headers.STATUS) );
		headers.put( XmlNodes.DEPRECATED, new SheetHeader(15, Headers.DEPRECATED) );
		
		// release note information (they will be inserted into the catalogue sheet)
		headers.put( XmlNodes.NOTES_DESCRIPTION, new SheetHeader(16, Headers.NOTES_DESCRIPTION) );
		headers.put( XmlNodes.NOTES_DATE, new SheetHeader(17, Headers.NOTES_DATE) );
		headers.put( XmlNodes.NOTES_VERSION, new SheetHeader(18, Headers.NOTES_VERSION) );
		headers.put( XmlNodes.NOTES_NOTE, new SheetHeader(19, Headers.NOTES_NOTE) );
		return headers;
	}
}
