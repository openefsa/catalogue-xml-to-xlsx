package sheet_converter;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.xml.sax.Attributes;

import data_transformation.ValuesGrouper;
import naming_convention.Headers;
import naming_convention.SpecialValues;
import sheet_header.SheetHeader;

/**
 * Parse the catalogue xml data related to hierarchies and create the hierarchy sheet
 * @author avonva
 *
 */
public class HierarchySheetConverter extends ExtendedSheetConverter {

	private boolean isGroupNode;  // true if we are in a hierarchyGroups node
	private ValuesGrouper group;          // used to group the values of the hierarchy groups into a single one
	
	public HierarchySheetConverter( String inputFilename, String rootNode ) {
		super(inputFilename, rootNode );
		isGroupNode = false;
	}
	
	/**
	 * Add the master hierarchy using the catalogue sheet data ( standard rule of the DCF )
	 * In fact in the XML the master hierarchy is not present, therefore we need to create it
	 * @param catSheet
	 */
	public void addMasterHierarchy ( Sheet catSheet ) {

		Row row = createRow( getSheet() );

		// get the information from the catalogue sheet and add them to the master hierarchy
		addCatFieldToMaster ( catSheet, row, Headers.CODE );
		addCatFieldToMaster ( catSheet, row, Headers.NAME );
		addCatFieldToMaster ( catSheet, row, Headers.LABEL );
		addCatFieldToMaster ( catSheet, row, Headers.SCOPENOTE );
		addCatFieldToMaster ( catSheet, row, Headers.VERSION );
		addCatFieldToMaster ( catSheet, row, Headers.LAST_UPDATE );
		addCatFieldToMaster ( catSheet, row, Headers.VALID_FROM );
		addCatFieldToMaster ( catSheet, row, Headers.STATUS );

		// here there is a differences between the names => we need to specify them
		addCatFieldToMaster ( catSheet, row, Headers.HIER_GROUPS, XmlNodes.HIER_GROUPS, true );

		// add default values for applicability and order
		addElement( row, XmlNodes.HIER_APPL, SpecialValues.ATTR_APPL_BOTH );
		addElement( row, XmlNodes.HIER_ORDER, SpecialValues.MASTER_ORDER );
	}

	/**
	 * Add one catalogue field to the hierarchy sheet, used only with the master hierarchy
	 * Here the name of the xml node in the hierarchy XML is equal to the name of the catalogue column
	 * @param catSheet
	 * @param row
	 * @param field
	 */
	private void addCatFieldToMaster ( Sheet catSheet, Row row, String catColumnName ) {
		this.addCatFieldToMaster(catSheet, row, catColumnName, catColumnName, false);
	}

	/**
	 * Add one catalogue field to the hierarchy field. Specify the name of the xml node which has to
	 * be found in the XML hierarchy data and specify the name of the catalogue column from which getting the data
	 * @param catSheet
	 * @param row
	 * @param catColumnName
	 * @param XmlNodeName
	 */
	private void addCatFieldToMaster ( Sheet catSheet, Row row, 
			String catColumnName, String XmlNodeName, boolean isGroup ) {

		// get the values contained into the catalogue sheet for the selected header
		ArrayList<String> values = getSheetColumn( catSheet, catColumnName );

		// if the field is indeed retrieved
		if ( !values.isEmpty() ) {
			
			String value = values.get(0);
			
			// if it is a group we need to initialize the group!
			if ( isGroup ) {
				this.isGroupNode = isGroup;
				group = new ValuesGrouper();
				group.addValue( value );
			}
	
			addElement( row, XmlNodeName, value );
		}
	}


	@Override
	public void startElement(Row row, String nodeName, Attributes attr) {

		// if we found a hierarchy groups element
		if ( nodeName.equals( XmlNodes.HIER_GROUPS ) ) {
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
		case XmlNodes.VALID_FROM:
		case XmlNodes.VALID_TO:
		case XmlNodes.LAST_UPDATE:
		case XmlNodes.STATUS:
			return;
		}
		
		switch ( nodeName ) {

			// we are closing a hierarchy groups => we save all the 
			// group values into the cell
		case XmlNodes.HIER_GROUPS:
			// add as groups all the groups which were found, $ separated
			if ( isGroupNode )
				createCell( nodeName, row, group.getCompactValues() );

			isGroupNode = false;
			group = null;
			break;

			// if we find a hierarchyGroup node and we are indeed inside
			// a HierarchyGroups node => add the value
		case XmlNodes.HIER_GROUP:

			if ( isGroupNode )
				group.addValue( value );
			break;

		default:
			// create a cell for the current row in the right column
			// we use the xml node name as key for identifying the related header
			createCell( nodeName, row, value );
			break;
		}
	}

	/**
	 * Get the headers of the catalogue sheet
	 * @return
	 */
	@Override
	public HashMap<String, SheetHeader> getHeaders() {

		HashMap<String, SheetHeader> headers = new HashMap<>();

		headers.put( XmlNodes.CODE, new SheetHeader(0, Headers.CODE ) );
		headers.put( XmlNodes.NAME, new SheetHeader(1, Headers.NAME ) );
		headers.put( XmlNodes.LABEL, new SheetHeader(2, Headers.LABEL ) );
		headers.put( XmlNodes.SCOPENOTE, new SheetHeader(3, Headers.SCOPENOTE ) );
		headers.put( XmlNodes.HIER_APPL, new SheetHeader(4, Headers.HIER_APPL ) );
		headers.put( XmlNodes.HIER_ORDER, new SheetHeader(5, Headers.HIER_ORDER ) );
		headers.put( XmlNodes.VERSION, new SheetHeader(6, Headers.VERSION ) );
		headers.put( XmlNodes.LAST_UPDATE, new SheetHeader(7, Headers.LAST_UPDATE ) );
		headers.put( XmlNodes.VALID_FROM, new SheetHeader(8, Headers.VALID_FROM ) );
		headers.put( XmlNodes.VALID_TO, new SheetHeader(9, Headers.VALID_TO ) );
		headers.put( XmlNodes.STATUS, new SheetHeader(10, Headers.STATUS ) );
		headers.put( XmlNodes.DEPRECATED, new SheetHeader(11, Headers.DEPRECATED ) );
		headers.put( XmlNodes.HIER_GROUPS, new SheetHeader(12, Headers.HIER_GROUPS ) );

		return headers;
	}
}
