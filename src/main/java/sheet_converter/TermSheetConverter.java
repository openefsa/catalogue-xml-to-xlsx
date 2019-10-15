package sheet_converter;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.xml.sax.Attributes;

import data_transformation.BooleanConverter;
import data_transformation.HierarchyAssignment;
import data_transformation.ValuesGrouper;
import naming_convention.Headers;
import naming_convention.SpecialValues;
import sheet_header.SheetHeader;

/**
 * Used to convert the xml catalogue data related to terms into a term sheet
 * @author avonva
 *
 */
public class TermSheetConverter extends ExtendedSheetConverter {

	private static final Logger LOGGER = LogManager.getLogger(TermSheetConverter.class);
	
	private String masterHierarchyCode;     // which is the hierarchy code of the master hierarchy?
	private boolean isHierarchyAssignment;  // true if we are processing a hierarchy assignment node
	private boolean isImplicitAttribute;    // true if we are processing an implicit attribute node
	private HierarchyAssignment assignment; // the current hierarchy assignment (only set if isHierarchyAssignment = true)
	private Sheet hierarchySheet;           // the hierarchy sheet (used to retrieve hierarchy codes and compute term columns)
	private Sheet attributeSheet;           // the attribute sheet (used to retrieve attributes names and compute term columns)
	private ValuesGrouper groups;           // group to group multiple value related to the repeatable attributes
	
	/**
	 * Initialize the converter
	 * @param inputFilename
	 * @param rootNode
	 * @param hierarchySheet
	 * @param attributeSheet
	 */
	public TermSheetConverter(String inputFilename, String rootNode, 
			Sheet hierarchySheet, Sheet attributeSheet ) {
		super(inputFilename, rootNode );
		
		isHierarchyAssignment = false;
		isImplicitAttribute = false;
		this.hierarchySheet = hierarchySheet;
		this.attributeSheet = attributeSheet;
		this.assignment = new HierarchyAssignment();
		this.groups = new ValuesGrouper();
		
	}

	@Override
	public void startElement(Row row, String nodeName, Attributes attr) {
		
		// if a node hierarchy assignment starts (i.e. a node which says if the term is
		// contained in the hierarchy or not)
		if ( nodeName.equals( XmlNodes.HIER_ASSIGNMENT ) ) {
			isHierarchyAssignment = true;
			assignment.reset();
		}
		
		// if implicit attribute node start analyzing its data
		// i.e. a node which says which attributes the term has
		else if ( nodeName.equals( XmlNodes.IMPLICIT_ATTR ) ) {
			isImplicitAttribute = true;
			groups.reset();
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
		
		// switch on the xml node name
		switch ( nodeName ) {
		
		// if we are closing an hierarchy assignment field then we
		// have to add the retrieved hierarchy assignment data to the excel
		case XmlNodes.HIER_ASSIGNMENT:
			
			// create the cells for the current hierarchy assignment in the right columns
			createCell( assignment.getFlagColumn(), row, assignment.getFlag() );
			createCell( assignment.getParentCodeColumn(), row, assignment.getParentCode() );
			createCell( assignment.getOrderColumn(), row, assignment.getOrder() );
			createCell( assignment.getReportableColumn(), row, assignment.getReportable() );
			
			isHierarchyAssignment = false;
			assignment.reset();
			break;
		
			// if hierarchy code and we are inside a hierarchy assignment node =>
			// we start collecting the data related to the term hierarchy assignment
		case  XmlNodes.ASS_HIER_CODE:
			if ( isHierarchyAssignment ) {
				
				// if we have set a master hierarchy code and the hierarchy code is 
				// actually the master hierarchy code, we set as code "master"
				if ( masterHierarchyCode != null && masterHierarchyCode.equals( value ) )
					assignment.setHierarchyCode( Headers.PREFIX_MASTER_CODE );
				else
					assignment.setHierarchyCode( value );
			}
			break;
			
			// same as above
		case XmlNodes.ASS_PARENT_CODE:
			if ( isHierarchyAssignment )
				assignment.setParentCode( value );
			break;
			
			// same as above
		case XmlNodes.ASS_ORDER:
			if ( isHierarchyAssignment )
				assignment.setOrder( value );
			break;
			
			// same as above
		case XmlNodes.ASS_REPORT:
			if ( isHierarchyAssignment ) {
				
				// convert boolean true false into 1/0
				value = BooleanConverter.toNumericBoolean( value );
				
				assignment.setReportable( value );
			}
			break;
			
			// if we are closing an implicit attribute node => finish analyzing data and save values
			// we get the attribute code from the groups data
			// we get all the attribute values from the groups getGroups
		case XmlNodes.IMPLICIT_ATTR:
			
			if ( isImplicitAttribute ) {
				createCell( (String) groups.getData(), row, groups.getCompactValues() );
				isImplicitAttribute = false;
			}
			
			break;
			
			// if we are in the implicit attribute node and we found the attribute
			// code field then we save the code into the groups
		case XmlNodes.IMPLICIT_CODE:
			if (isImplicitAttribute )
				groups.setData( value );
			break;
			
			// if attribute value save the value in the groups
		case XmlNodes.IMPLICIT_VALUE:
			if (isImplicitAttribute )
				groups.addValue( value );
			break;
			
			// default behaviour, create the cell using the xml
			// node name as key to retrieve the header column name and index
			// we use this only for xml nodes which are unique
		default:
			
			createCell( nodeName, row, value );
			break;
		}
	}
	
	/**
	 * Set which is the master hierarchy code. If set, the converter will
	 * check for each hierarchy assignment if the hierarchy code is the master code
	 * and if it is so, the converter will give as hierarchy code "master" instead
	 * of the retrieved hierarchy code. This has to be used if we identify the master
	 * hierarchy using a variable hierarchy code!
	 * @param masterHierarchyCode
	 */
	public void setMasterHierarchyCode(String masterHierarchyCode) {
		this.masterHierarchyCode = masterHierarchyCode;
	}

	/**
	 * Get all the attributes codes and names which are not facet categories
	 * @param sheet
	 * @param field
	 * @return
	 */
	private HashMap<String, String> getSimpleAttributes ( Sheet attrSheet, Sheet hierSheet ) {

		HashMap<String, String> attrs = new HashMap<>();
		
		ArrayList<String> hierCodes = SheetConverter.getSheetColumn( hierSheet, Headers.CODE );
		
		// get the names and the types of the attributes
		ArrayList<String> codes = SheetConverter.getSheetColumn( attrSheet, Headers.CODE );
		ArrayList<String> names = SheetConverter.getSheetColumn( attrSheet, Headers.NAME );
		ArrayList<String> types = SheetConverter.getSheetColumn( attrSheet, Headers.ATTR_TYPE );
		ArrayList<String> catCodes = SheetConverter.getSheetColumn( attrSheet, Headers.ATTR_CAT_CODE );
		
		if ( codes.size() != names.size() || names.size() != types.size() ) {
			LOGGER.error( "Wrong number of rows for attribute sheet" );
			return null;
		}
		
		// For each pair of name and type
		for ( int i = 0; i < names.size(); i++ ) {
			
			boolean addIt = false;
			
			// if catalogue attribute
			if ( types.get(i).equals( SpecialValues.ATTR_CAT_TYPE ) ) {
				
				// check if hierarchy is present into the catalogue
				String[] split = catCodes.get(i).split("\\.");
				
				if ( split.length > 1 ) {
					
					// get hierarchy code from the composite code
					String hierarchyCode = split[1];
					
					// add the attribute just if we have a catalogue
					// attribute with a hierarchy that does not belong 
					// to the current catalogue
					if ( !hierCodes.contains( hierarchyCode ) )
						addIt = true;
				}
			}
			else {
				addIt = true;
			}
			
			// if the type is not a catalogue type add the name
			// to the list identified by the code
			if ( addIt ) {
				attrs.put( codes.get(i), names.get(i) );
			}
		}
		
		return attrs;
	}
	

	@Override
	public HashMap<String, SheetHeader> getHeaders() {

		HashMap<String, SheetHeader> headers = new HashMap<>();
		
		int columnIndex = 0;
		
		// prepare headers for the term sheet
		// the order of headers in the headers array reflect
		// the order of the excel columns
		
		headers.put(XmlNodes.TERM_CODE, new SheetHeader(columnIndex++, Headers.TERM_CODE) );
		headers.put(XmlNodes.TERM_EXT_NAME, new SheetHeader(columnIndex++, Headers.TERM_EXT_NAME) );
		headers.put(XmlNodes.TERM_SHORT_NAME, new SheetHeader(columnIndex++, Headers.TERM_SHORT_NAME) );
		headers.put(XmlNodes.TERM_SCOPENOTE, new SheetHeader(columnIndex++, Headers.TERM_SCOPENOTE) );
		headers.put(XmlNodes.DEPRECATED, new SheetHeader(columnIndex++, Headers.DEPRECATED) );
		headers.put(XmlNodes.VERSION, new SheetHeader(columnIndex++, Headers.VERSION) );
		headers.put(XmlNodes.LAST_UPDATE, new SheetHeader(columnIndex++, Headers.LAST_UPDATE) );
		headers.put(XmlNodes.VALID_FROM, new SheetHeader(columnIndex++, Headers.VALID_FROM) );
		headers.put(XmlNodes.VALID_TO, new SheetHeader(columnIndex++, Headers.VALID_TO) );
		headers.put(XmlNodes.STATUS, new SheetHeader(columnIndex++, Headers.STATUS) );
		
		//
		// Add the attributes columns to the term
		// we identify attributes by their code in the headers
		// but their excel column name actually is the attribute name
		//
		
		HashMap<String, String> attributes = getSimpleAttributes( attributeSheet, hierarchySheet );
		
		for ( String key : attributes.keySet() ) {
			headers.put( key, new SheetHeader( columnIndex++, attributes.get( key ) ) );
		}
		
		//
		// Add Hierarchies columns headers to the term sheet
		//
		
		// for each hierarchy code we put columns to the term sheet
		for ( String code : getSheetColumn( hierarchySheet, Headers.CODE ) ) {
			
			// we create a hierarchy assignment in order to automatically
			// get the right headers column names starting from the hierarchy code
			HierarchyAssignment assignment = new HierarchyAssignment();
			
			// if master hierarchy set the hierarchy code to master
			if ( code.equals( masterHierarchyCode ) )
				assignment.setHierarchyCode( Headers.PREFIX_MASTER_CODE );
			else
				assignment.setHierarchyCode( code );

			// set as xml code the assignment fields because using the xml node here will imply override
			// each header since the xml tags are always the same
			headers.put( assignment.getFlagColumn(), 
					new SheetHeader( columnIndex++, assignment.getFlagColumn() ) );
			
			headers.put( assignment.getParentCodeColumn(), 
					new SheetHeader( columnIndex++, assignment.getParentCodeColumn() ) );
			
			headers.put( assignment.getOrderColumn(), 
					new SheetHeader( columnIndex++, assignment.getOrderColumn() ) );
			
			headers.put( assignment.getReportableColumn(), 
					new SheetHeader( columnIndex++, assignment.getReportableColumn() ) );
		}
		
		return headers;
	}
}
