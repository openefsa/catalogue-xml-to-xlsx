package sheet_converter;

import java.util.Date;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Row;
import org.xml.sax.Attributes;

import data_transformation.DateTrimmer;

/**
 * Parse the catalogue xml data related to release notes.
 * In particular, a new sheet called releaseNotes is created. In this
 * sheet the information related to the operation info, name and date
 * are inserted. If more than one operation info is present, additional
 * records reporting the same name and date but different op info are added.
 * @author avonva
 *
 */
public class NotesSheetConverter extends SheetConverter {

	// nodes which need to be parsed
	public static final String OP_DETAIL_NODE = "operationsDetail";
	public static final String OP_INFO_NODE = "operationInfo";
	public static final String OP_NAME_NODE = "operationName";
	public static final String OP_DATE_NODE = "operationDate";
	public static final String OP_GROUP_NODE = "operationGroupId";

	// save the last name and date of an operation
	private String lastName;
	private Date lastDate;
	private int groupId = 0;
	
	public NotesSheetConverter( String inputFilename, String rootNode ) {
		super(inputFilename, rootNode );
	}
	
	/**
	 * Add an operation info row into the sheet.
	 * @param row
	 * @param opInfo
	 */
	private void addInfoRow( Row row, String opInfo ) {
		
		// create a new row for each operationsDetail node
		//Row row = createRow( getSheet() );
		
		// create the cell in the current row for the
		// two information (two different columns will
		// be created
		createCell( OP_NAME_NODE, row, lastName );
		createCell( OP_DATE_NODE, row, lastDate );
		createCell( OP_INFO_NODE, row, opInfo );
		createCell( OP_GROUP_NODE, row, String.valueOf( groupId ) );
	}
	
	@Override
	public void startElement(Row row, String nodeName, Attributes attr) {
		
		// if operation detail node, we need to read its attributes
		// to get the operation name and the operation date information
		switch ( nodeName ) {
		case OP_DETAIL_NODE:

			// get the node attributes
			lastName = attr.getValue( OP_NAME_NODE );
			
			String opDate = attr.getValue( OP_DATE_NODE );
			lastDate = DateTrimmer.trimDate( opDate );

			break;
		}
	}

	@Override
	public void addElement(Row row, String nodeName, String value) {
		
		switch ( nodeName ) {

			// operation info node, we group the operation info
			// with values grouper
		case OP_INFO_NODE:
			addInfoRow( row, value );
			break;
			
			// the group is finished
		case OP_DETAIL_NODE:
			lastDate = null;
			lastName = null;
			groupId++;
			break;
		default:
			break;
		}
	}

	/**
	 * Get the headers of the release note sheet
	 * @return
	 */
	public HashMap<String, SheetHeader> getHeaders() {

		HashMap<String, SheetHeader> headers = new HashMap<>();

		headers.put( OP_NAME_NODE, new SheetHeader(0, OP_NAME_NODE) );
		headers.put( OP_DATE_NODE, new SheetHeader(1, OP_DATE_NODE) );
		headers.put( OP_INFO_NODE, new SheetHeader(2, OP_INFO_NODE ) );
		headers.put( OP_GROUP_NODE, new SheetHeader(3, OP_GROUP_NODE ) );
		return headers;
	}
}
