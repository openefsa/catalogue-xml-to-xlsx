package sheet_converter;

import java.util.Date;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Row;
import org.xml.sax.Attributes;

import data_transformation.DateTrimmer;
import naming_convention.Headers;
import sheet_header.SheetHeader;

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
		
		// create the cell in the current row for the
		// two information (two different columns will
		// be created
		createCell( XmlNodes.OP_NAME, row, lastName );
		createCell( XmlNodes.OP_DATE, row, lastDate );
		createCell( XmlNodes.OP_INFO, row, opInfo );
		createCell( XmlNodes.OP_GROUP, row, String.valueOf( groupId ) );
	}
	
	@Override
	public void startElement(Row row, String nodeName, Attributes attr) {
		// if operation detail node, we need to read its attributes
		// to get the operation name and the operation date information
		switch ( nodeName ) {
		case XmlNodes.OP_DETAIL:

			// get the node attributes
			lastName = attr.getValue( XmlNodes.OP_NAME );
			String opDate = attr.getValue( XmlNodes.OP_DATE );
			lastDate = DateTrimmer.trimDate( opDate );
			break;
		}
	}

	@Override
	public void addElement(Row row, String nodeName, String value) {
		
		switch ( nodeName ) {

			// operation info node, we group the operation info
			// with values grouper
		case XmlNodes.OP_INFO:
			addInfoRow( row, value );
			break;
			
			// the group is finished
		case XmlNodes.OP_DETAIL:
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

		headers.put( XmlNodes.OP_NAME, new SheetHeader(0, Headers.OP_NAME ) );
		headers.put( XmlNodes.OP_DATE, new SheetHeader(1, Headers.OP_DATE ) );
		headers.put( XmlNodes.OP_INFO, new SheetHeader(2, Headers.OP_INFO ) );
		headers.put( XmlNodes.OP_GROUP, new SheetHeader(3, Headers.OP_GROUP ) );
		return headers;
	}
}
