package sheet_converter;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import data_transformation.ValuesGrouper;

/**
 * Parse the catalogue xml data related to hierarchies and create the hierarchy sheet
 * @author avonva
 *
 */
public class HierarchySheetConverter extends ExtendedSheetConverter {

	private boolean isGroupNode;  // true if we are in a hierarchyGroups node
	ValuesGrouper group;          // used to group the values of the hierarchy groups into a single one

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
		addCatFieldToMaster ( catSheet, row, "code" );
		addCatFieldToMaster ( catSheet, row, "name" );
		addCatFieldToMaster ( catSheet, row, "label" );
		addCatFieldToMaster ( catSheet, row, "scopeNote" );
		addCatFieldToMaster ( catSheet, row, "version" );
		addCatFieldToMaster ( catSheet, row, "lastUpdate" );
		addCatFieldToMaster ( catSheet, row, "validFrom" );
		addCatFieldToMaster ( catSheet, row, "status" );

		// here there is a differences between the names => we need to specify them
		addCatFieldToMaster ( catSheet, row, "catalogueGroups", "hierarchyGroups", true );

		// add default values for applicability and order
		addElement( row, "hierarchyApplicability", "both" );
		addElement( row, "hierarchyOrder", "0" );
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
	public void startElement(Row row, String nodeName) {

		// if we found a hierarchy groups element
		if ( nodeName.equals( "hierarchyGroups" ) ) {
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
		
		switch ( nodeName ) {

			// we are closing a hierarchy groups => we save all the 
			// group values into the cell
		case "hierarchyGroups":
			// add as groups all the groups which were found, $ separated
			if ( isGroupNode )
				createCell( nodeName, row, group.getCompactValues() );

			isGroupNode = false;
			group = null;
			break;

			// if we find a hierarchyGroup node and we are indeed inside
			// a HierarchyGroups node => add the value
		case "hierarchyGroup":

			if ( isGroupNode )
				group.addValue( value );
			break;

		default:
			// create a cell for the current row in the right column
			// we use the xml node name as key for identifying the related header
			createCell( nodeName, row, value );
		}
	}

	/**
	 * Get the headers of the catalogue sheet
	 * @return
	 */
	public HashMap<String, SheetHeader> getHeaders() {

		HashMap<String, SheetHeader> headers = new HashMap<>();

		headers.put("code", new SheetHeader(0, "code") );
		headers.put("name", new SheetHeader(1, "name") );
		headers.put("label", new SheetHeader(2, "label") );
		headers.put("scopeNote", new SheetHeader(3, "scopeNote" ) );
		headers.put("hierarchyApplicability", new SheetHeader(4, "hierarchyApplicability") );
		headers.put("hierarchyOrder", new SheetHeader(5, "hierarchyOrder") );
		headers.put("version", new SheetHeader(6, "version") );
		headers.put("lastUpdate", new SheetHeader(7, "lastUpdate") );
		headers.put("validFrom", new SheetHeader(8, "validFrom") );
		headers.put("validTo", new SheetHeader(9, "validTo") );
		headers.put("status", new SheetHeader(10, "status") );
		headers.put("deprecated", new SheetHeader(11, "deprecated") );
		headers.put("hierarchyGroups", new SheetHeader(12, "hierarchyGroups") );

		return headers;
	}
}
