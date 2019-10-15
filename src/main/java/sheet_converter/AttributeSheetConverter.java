package sheet_converter;

import java.util.HashMap;

import org.apache.poi.ss.usermodel.Row;
import org.xml.sax.Attributes;

import naming_convention.Headers;
import sheet_header.SheetHeader;

/**
 * Parse the catalogue xml data related to attributes and create the attribute sheet
 * @author avonva
 *
 */
public class AttributeSheetConverter extends ExtendedSheetConverter {

	public AttributeSheetConverter(String inputFilename, String rootNode ) {
		super(inputFilename, rootNode );
	}

	@Override
	public void startElement(Row row, String nodeName, Attributes attr) {}

	@Override
	public void addElement(Row row, String nodeName, String value) {	
		
		// call parent add element
		super.addElement( row, nodeName, value );
		
		// these nodes were already processed by the parent call
		switch ( nodeName ) {
		case Headers.VALID_FROM:
		case Headers.VALID_TO:
		case Headers.LAST_UPDATE:
		case Headers.STATUS:
			return;
		}
		
		// create a cell for the current row in the right column
		// the attributes xml nodes are all unique in the same attribute node,
		// therefore we can simply use the xml node as identifier of the header

		createCell( nodeName, row, value );
	}
	
	/**
	 * Get the headers of the catalogue sheet
	 * @return
	 */
	public HashMap<String, SheetHeader> getHeaders() {

		// the attributes xml nodes are all unique in the same attribute node,
		// therefore we can simply use the xml node as identifier of the header

		HashMap<String, SheetHeader> headers = new HashMap<>();
		
		headers.put( XmlNodes.CODE, new SheetHeader(0, Headers.CODE ) );
		headers.put( XmlNodes.NAME, new SheetHeader(1, Headers.NAME ) );
		headers.put( XmlNodes.LABEL, new SheetHeader(2, Headers.LABEL ) );
		headers.put( XmlNodes.SCOPENOTE, new SheetHeader(3, Headers.SCOPENOTE ) );
		headers.put( XmlNodes.ATTR_REPORT, new SheetHeader(4, Headers.ATTR_REPORT ) );
		headers.put( XmlNodes.ATTR_VISIB, new SheetHeader(5, Headers.ATTR_VISIB ) );
		headers.put( XmlNodes.ATTR_SEARCH, new SheetHeader(6, Headers.ATTR_SEARCH ) );
		headers.put( XmlNodes.ATTR_ORDER, new SheetHeader(7, Headers.ATTR_ORDER ) );
		headers.put( XmlNodes.ATTR_TYPE, new SheetHeader(8, Headers.ATTR_TYPE ) );
		headers.put( XmlNodes.ATTR_MAX_LENGTH, new SheetHeader(9, Headers.ATTR_MAX_LENGTH ) );
		headers.put( XmlNodes.ATTR_PRECISION, new SheetHeader(10, Headers.ATTR_PRECISION ) );
		headers.put( XmlNodes.ATTR_SCALE, new SheetHeader(11, Headers.ATTR_SCALE ) );
		headers.put( XmlNodes.ATTR_CAT_CODE, new SheetHeader(12, Headers.ATTR_CAT_CODE ) );
		headers.put( XmlNodes.ATTR_SR, new SheetHeader(13, Headers.ATTR_SR ) );
		headers.put( XmlNodes.ATTR_INHERIT, new SheetHeader(14, Headers.ATTR_INHERIT ) );
		headers.put( XmlNodes.ATTR_UNIQUE, new SheetHeader(15, Headers.ATTR_UNIQUE ) );
		headers.put( XmlNodes.ATTR_ALIAS, new SheetHeader(16, Headers.ATTR_ALIAS ) );
		headers.put( XmlNodes.VERSION, new SheetHeader(17, Headers.VERSION ) );
		headers.put( XmlNodes.LAST_UPDATE, new SheetHeader(18, Headers.LAST_UPDATE ) );
		headers.put( XmlNodes.VALID_FROM, new SheetHeader(19, Headers.VALID_FROM ) );
		headers.put( XmlNodes.VALID_TO, new SheetHeader(20, Headers.VALID_TO ) );
		headers.put( XmlNodes.STATUS, new SheetHeader(21, Headers.STATUS ) );
		headers.put( XmlNodes.DEPRECATED, new SheetHeader(22, Headers.DEPRECATED ) );
		return headers;
	}
}
