package sheet_converter;

import java.util.HashMap;

import org.apache.poi.ss.usermodel.Row;
import org.xml.sax.Attributes;

import data_transformation.ValuesGrouper;

/**
 * Parse the catalogue xml data related to release notes.
 * In particular, a new sheet called releaseNotes is created. In this
 * sheet the information related to the operation info, name and date
 * are inserted. Since the operation info is a repeatable information
 * we save it in a dollar separated format in a single cell.
 * @author avonva
 *
 */
public class NotesSheetConverter extends SheetConverter {

	// nodes which need to be parsed
	public static final String OP_DETAIL_NODE = "operationsDetail";
	private static final String OP_INFO_NODE = "operationInfo";
	private static final String OP_NAME_NODE = "operationName";
	private static final String OP_DATE_NODE = "operationDate";
	
	private boolean isGroupNode;  // true if we are in operationDetail node
	private ValuesGrouper group;  // used to group the operation info data into a single row
	
	public NotesSheetConverter( String inputFilename, String rootNode ) {
		super(inputFilename, rootNode );
	}

	@Override
	public void startElement(Row row, String nodeName, Attributes attr) {

		// if operation detail node, we need to read its attributes
		// to get the operation name and the operation date information
		switch ( nodeName ) {
		case OP_DETAIL_NODE:
			
			// create a new row for each operationsDetail node
			createRow( getSheet() );
			
			// set that operation info nodes will start
			isGroupNode = true;
			group = new ValuesGrouper();

			// get the node attributes
			String opName = attr.getValue( OP_NAME_NODE );
			String opDate = attr.getValue( OP_DATE_NODE );
			
			// create the cell in the current row for the
			// two information (two different columns will
			// be created
			createCell( OP_NAME_NODE, row, opName );
			createCell( OP_DATE_NODE, row, opDate );

			break;
		}
	}

	@Override
	public void addElement(Row row, String nodeName, String value) {
		
		switch ( nodeName ) {

			// operation info node, we group the operation info
			// with values grouper
		case OP_INFO_NODE:

			if ( isGroupNode )
				group.addValue( value );

			break;

			// if operation detail ends, we group the values
		case OP_DETAIL_NODE:

			if ( isGroupNode )
				createCell( OP_INFO_NODE, row, group.getCompactValues() );

			isGroupNode = false;
			group = null;
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
		return headers;
	}
}
