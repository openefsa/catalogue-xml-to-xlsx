package sheet_converter;

import java.util.HashMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import sheet_header.SheetHeader;

/**
 * This class handle the writing procedure of an excel sheet. Extend this class and provide the
 * methods getHeaders and insertData to have a complete sheet writer.
 * @author avonva
 *
 */
public abstract class SheetWriter {

	// the considered fields of the xml which will be the sheet columns
	public HashMap<String, SheetHeader> headers;

	// the current number of rows
	private int rowNum = 0;

	// the sheet which will be created
	private Sheet sheet;


	/**
	 * Create the sheet
	 * @param workbook
	 * @param sheetName
	 */
	public SheetWriter( Workbook workbook, String sheetName ) {
		
		// create the sheet
		sheet = workbook.createSheet( sheetName );
	}
	
	/**
	 * Create a new cell in the sheet.
	 * @param columIndex, index of the column in which the cell is created
	 * @param row, row where the cell is created
	 * @param value, cell value
	 * @return
	 */
	public Cell createCell ( int columnIndex, Row row, String value ) {

		if ( columnIndex < 0 )
			return null;

		Cell cell = row.createCell( columnIndex );
		cell.setCellValue( value );
		return cell;
	}

	/**
	 * Create a new cell in the sheet.
	 * @param headerName, name of the column in which the cell is created
	 * @param row, row where the cell is created
	 * @param value, cell value
	 * @return
	 */
	public Cell createCell ( String columnLabel, Row row, String value ) {

		// get the current header
		SheetHeader header = headers.get( columnLabel );

		// if the header is not in the headers hashmap return
		if ( header == null ) {
			return null;
		}

		// create the cell
		Cell cell = createCell( header.getColumnIndex(), row, value );
		return cell;
	}

	/**
	 * Create a new row in the selected sheet
	 * @param sheet
	 * @return
	 */
	public Row createRow( Sheet sheet ) {
		return sheet.createRow( rowNum++ );
	}
	
	/**
	 * Write into the sheet the headers and the data
	 */
	public void write() {
		
		// prepare the headers
		headers = getHeaders();
		
		// insert headers
		insertHeaders( sheet );
		
		// insert data
		insertData( sheet );
	}

	/**
	 * Insert the headers as first line of the sheet
	 * @param sheet
	 */
	private void insertHeaders ( Sheet sheet ) {

		// get the headers
		this.headers = getHeaders();

		// create a row for the headers
		Row row = createRow( sheet );

		// insert the headers
		for ( SheetHeader header : headers.values() )
			createCell ( header.getColumnIndex(), row, header.getColumnName() );
	}
	
	/**
	 * Get the sheet headers, we need to implement this method to choose which 
	 * columns we want to insert into the sheet. Note that for terms this is
	 * a bit complicated since their columns names depend on hierarchies,
	 * attributes and on the catalogue code (to discover which is the master hierarchy).
	 * @return, hashmap of headers, the string is an identifier of the header (usually can be
	 * used the xml node if it is unique, otherwise a naming convention should be declared! You
	 * can use also the column names if they are unique, or a combination of xml node names and
	 * excel column names. It is sufficient that you are aware of the keys)
	 */
	public abstract HashMap<String, SheetHeader> getHeaders ();
	
	/**
	 * Procedure to insert the data into the sheet
	 */
	public abstract void insertData ( Sheet sheet );
}
