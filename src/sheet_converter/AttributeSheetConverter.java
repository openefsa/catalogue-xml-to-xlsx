package sheet_converter;

import java.util.HashMap;

import org.apache.poi.ss.usermodel.Row;
import org.xml.sax.Attributes;

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
		case "validFrom":
		case "validTo":
		case "lastUpdate":
		case "status":
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
		
		headers.put("code", new SheetHeader(0, "code") );
		headers.put("name", new SheetHeader(1, "name") );
		headers.put("label", new SheetHeader(2, "label") );
		headers.put("scopeNote", new SheetHeader(3, "scopeNote") );
		headers.put("attributeReportable", new SheetHeader(4, "attributeReportable" ) );
		headers.put("attributeVisible", new SheetHeader(5, "attributeVisible" ) );
		headers.put("attributeSearchable", new SheetHeader(6, "attributeSearchable") );
		headers.put("attributeOrder", new SheetHeader(7, "attributeOrder") );
		headers.put("attributeType", new SheetHeader(8, "attributeType") );
		headers.put("attributeMaxLength", new SheetHeader(9, "attributeMaxLength") );
		headers.put("attributePrecision", new SheetHeader(10, "attributePrecision") );
		headers.put("attributeScale", new SheetHeader(11, "attributeScale") );
		headers.put("attributeCatalogueCode", new SheetHeader(12, "attributeCatalogueCode") );
		headers.put("attributeSingleOrRepeatable", new SheetHeader(13, "attributeSingleOrRepeatable") );
		headers.put("attributeInheritance", new SheetHeader(14, "attributeInheritance" ) );
		headers.put("attributeUniqueness", new SheetHeader(15, "attributeUniqueness") );
		headers.put("attributeTermCodeAlias", new SheetHeader(16, "attributeTermCodeAlias") );
		headers.put("version", new SheetHeader(17, "version") );
		headers.put("lastUpdate", new SheetHeader(18, "lastUpdate") );
		headers.put("validFrom", new SheetHeader(19, "validFrom") );
		headers.put("validTo", new SheetHeader(20, "validTo") );
		headers.put("status", new SheetHeader(21, "status") );
		headers.put("deprecated", new SheetHeader(22, "deprecated") );
		return headers;
	}
}
