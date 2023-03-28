package sheet_converter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import data_transformation.DateTrimmer;
import sheet_header.SheetHeader;


/**
 * Abstract class to define the structure of a sheet converter (i.e. a class which
 * takes an xml and convert it into an excel sheet). In particular the class parses the input
 * xml filename and read all the nodes names and values. The nodes and values are passed to
 * the child class to process them. This abstraction was created in order to customize
 * the process of analyzing nodes, in order to make this class of general use.
 * General notes:
 * the root node is the xml node which identifies the beginning of the object we are interested in in the xml.
 * For example, if we want to analyze terms and the xml contains a node <term></term> for each term,
 * then the root node will be "term". (the nodes outside the root node are not considered!)
 * @author avonva
 *
 */
public abstract class SheetConverter {

	private static final Logger LOGGER = LogManager.getLogger(SheetConverter.class);
	
	// print each 500 rows an echo to the console
	private static final int printRowCount = 500;
	private int printCount = 0;

	// the considered fields of the xml which will be the sheet columns
	private HashMap<String, SheetHeader> headers;

	// the xml input file
	private String inputFilename;

	// entry point for parsing the xml
	private String rootNode;

	// if we are analyzing a root node or not
	private boolean isRootNode = false;

	// content of the xml node
	private StringBuilder lastContent;

	// the current row which the program is adding
	private Row currentRow;

	// the current number of rows
	private int rowNum = 0;

	// the sheet which is created with buildSheet()
	private Sheet sheet;

	private CellStyle style;

	/**
	 * Convert a xml file into an xml sheet
	 * @param inputFilename, the xml file to be parsed
	 * @param rootNode, the root node of the xml from which starting the parsing operation
	 * @param headers, the xml nodes which has to be considered (for each 
	 * node a new column in the sheet is created)
	 */
	public SheetConverter( String inputFilename, String rootNode ) {

		this.inputFilename = inputFilename;
		this.rootNode = rootNode;
		lastContent = new StringBuilder();
	}

	/**
	 * Get the current XML filename from which we are taking the data
	 * @return
	 */
	public String getInputFilename() {
		return inputFilename;
	}

	/**
	 * Get the XML root node to be analyzed
	 * @return
	 */
	public String getRootNode() {
		return rootNode;
	}

	/**
	 * Check if we are parsing a root node or not
	 * @return
	 */
	public boolean isParsingRootNode() {
		return isRootNode;
	}

	/**
	 * Create a new cell if not present, otherwise add the content $ separated
	 * to the previous cell value
	 * @param row
	 * @param name
	 * @return
	 */
	public static Cell createCell ( int columnIndex, Row row, String value ) {

		if ( columnIndex < 0 )
			return null;

		Cell cell = row.createCell( columnIndex );
		cell.setCellValue( value );
		return cell;
	}

	/**
	 * Create a cell using the XmlNodeName instead of the column index
	 * Knowing the xml tag we can retrieve the related header an then the column index
	 * @param headerName
	 * @param row
	 * @param value
	 * @return
	 */
	public Cell createCell ( String XmlNodeName, Row row, String value ) {

		SheetHeader header = headers.get( XmlNodeName );
		
		// if the header is not in the headers hashmap return
		if ( header == null ) {
			return null;
		}

		// otherwise create the cell
		Cell cell = row.createCell( header.getColumnIndex() );
		cell.setCellValue( value );
		return cell;
	}

	/**
	 * Create a cell using the XmlNodeName instead of the column index
	 * Knowing the xml tag we can retrieve the related header an then the column index
	 * This is used with dates
	 * @param XmlNodeName
	 * @param row
	 * @param value
	 * @return
	 */
	public Cell createCell ( String XmlNodeName, Row row, java.util.Date value ) {

		SheetHeader header = headers.get( XmlNodeName );

		// if the header is not in the headers hashmap return
		if ( header == null ) {
			return null;
		}

		// otherwise create the cell
		Cell cell = row.createCell( header.getColumnIndex() );
		
		// create the data cell style if needed
		if ( style == null ) {
			style = sheet.getWorkbook().createCellStyle();
			short df = sheet.getWorkbook().createDataFormat().getFormat("yyyy/MM/dd");
			style.setDataFormat(df);
		}

		// set the cell style
		cell.setCellStyle( style );
		
		// set the date value
		cell.setCellValue( DateTrimmer.dateToString( value ) );
		
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
	 * Create a new sheet into the workbook and insert the headers.
	 * @return the new sheet
	 */
	public Sheet buildSheet( Workbook workbook, String sheetName ) {

		// create the sheet
		sheet = workbook.createSheet( sheetName );

		// insert the headers into the sheet
		insertHeaders( sheet );

		return sheet;
	}


	/**
	 * Populate the sheet with data, start the parsing procedure
	 */
	public void parse () {

		// insert the data into the sheet
		insertData( sheet, rootNode );
	}

	/**
	 * Insert the headers as first line of the sheet
	 * @param sheet
	 */
	private void insertHeaders ( Sheet sheet ) {

		// get the headers
		this.headers = getHeaders();

		// prepare empty sheet with headers
		Row row = createRow( sheet );

		// insert the headers
		for ( SheetHeader header : headers.values() )
			createCell ( header.getColumnIndex(), row, header.getColumnName() );
	}


	/**
	 * Insert the data into the excel sheet. In particular we start the parsing action
	 * and we analyze the xml nodes
	 * @param rootNode
	 * @param headers
	 */
	private void insertData( final Sheet sheet, final String rootNode ) {

		// instantiate the SAX parser
		SAXParserFactory factory = SAXParserFactory.newInstance();
		javax.xml.parsers.SAXParser saxParser = null;
		try {
			saxParser = factory.newSAXParser();
		} catch (ParserConfigurationException | SAXException e) {
			LOGGER.error("Cannot insert data", e);
			e.printStackTrace();
		}

		// create the parser handler
		DefaultHandler handler = new DefaultHandler() {

			// when a node is encountered
			public void startElement(String uri, String localName,String qName, 
					Attributes attributes) throws SAXException {
				startAnalyzingNode( qName, attributes );
			}

			@Override
			// when the end of a node is encountered
			public void endElement(String uri, String localName, String qName) throws SAXException {
				endAnalyzingNode ( qName );
			}

			@Override
			public void characters(char[] ch, int start, int length) throws SAXException {

				// ADD CONTENT TO LAST CONTENT! because the parser sometimes gives only
				// a small piece of the value and not the entire value in a single call!!!

				String newPiece = new String(ch, start, length);

				// add the new piece if it is not the new line
				// get the data only if we are in a root node
				if ( !newPiece.equals( "\n" ) )//&& isRootNode )
					lastContent.append( newPiece );
			}
		};

		// Parse the xml document
		try {

			File file = new File ( inputFilename ); 

			saxParser.parse( file, handler );

		} catch ( SAXParseException e ) {
			//e.printStackTrace();
			// sheet with no data => an exception is thrown
			LOGGER.info("Sheet with no data found");
		} catch (IOException e) {
			LOGGER.error("IO", e);
			e.printStackTrace();
		} catch (SAXException e) {
			LOGGER.error("SAX", e);
			e.printStackTrace();
		}
	}


	/**
	 * Analyze an xml node at its opening (e.g. <term> ... </term> => we invoke this
	 * method when we found <term>.
	 * @param qName
	 */
	void startAnalyzingNode( String qName, Attributes attr ) {
		
		// if we have a root node then create a new row
		// and set that we have to parse the entire node
		if ( qName.equals( rootNode ) ) {
			isRootNode = true;
			currentRow = createRow( sheet );
			
			// reset the content
			lastContent.setLength(0);
		}

		// if we are inside a root node tell to the child class to start
		// processing the current node
		//if ( isRootNode )
		SheetConverter.this.startElement( currentRow, qName, attr );
	}
	
	/**
	 * Analyze an xml node at its end (e.g. <term> ... </term> => we invoke this
	 * method when we found </term>. Here we know the value of the node (we can
	 * get it from the last content)
	 * @param qName
	 */
	void endAnalyzingNode ( String qName ) {
		
		// return if no content, no current node, no available row or not important node
		if ( currentRow == null  )
			return;

		// if we find a root node then we have finished parsing
		// therefore we set that we ended parsing the current root node
		if ( qName.equals( rootNode ) ) {
			isRootNode = false;
		}

		// process the node and add the element to the excel
		SheetConverter.this.addElement( currentRow, qName, lastContent.toString() );

		// reset contents
		lastContent.setLength(0);

		// Diagnostic: print every printRowCount rows
		if ( rowNum >= printCount + printRowCount ) {
			printCount = rowNum;
			LOGGER.info ( "Processed " + rowNum + " " + rootNode );
		}
	}

	/**
	 * Get the sheet we are creating with this converter
	 * @return
	 */
	public Sheet getSheet() {
		return sheet;
	}

	/**
	 * Get the index of the column using the header name in the selected sheet
	 * @param sheet
	 * @param columnName
	 * @return
	 */
	public static int getColumnIdFromName ( Sheet sheet, String columnName ) {

		// get the first row, the headers
		Iterator<Cell> iterator = sheet.getRow(0).cellIterator();

		// iterate over all the headers
		while ( iterator.hasNext() ) {

			Cell cell = iterator.next();

			// if we have found the right header get its index
			if ( cell.getStringCellValue().equals( columnName ) ) {
				return cell.getColumnIndex();
			}
		}

		// if not found
		return -1;
	}

	/**
	 * Get an array list of values which are contained in one column of the
	 * excel sheet. NOTE THAT headers are excluded from the array list!
	 * @param sheet
	 * @param field
	 * @return
	 */
	public static ArrayList<String> getSheetColumn ( Sheet sheet, String field ) {

		ArrayList<String> values = new ArrayList<>();

		// iterate all the sheet rows
		Iterator<Row> iterator = sheet.rowIterator();

		// get the column index of the chosen field
		int colIndex = getColumnIdFromName( sheet, field );

		int count = 0;

		while ( iterator.hasNext() ) {

			// skip header
			count++;

			// get the current row
			Row row = iterator.next();

			// if first row skip (we have headers here)
			if ( count == 1 )
				continue;

			// add the retrieved value to the output list
			if ( colIndex >= 0 ) {
				
				if ( row.getCell( colIndex ) != null )
					values.add( row.getCell( colIndex ).getStringCellValue() );
				else
					values.add("");  // add empty string if not found
			}
		}

		return values;
	}


	/**
	 * Start processing the current node (nodeName) for inserting the current row
	 * @param row
	 * @param nodeName
	 * @param attr the attribute related to the xml node
	 */
	public abstract void startElement ( Row row, String nodeName, Attributes attr );

	/**
	 * Add the current node (nodename) in the current row using value as value to be inserted
	 * @param row
	 * @param nodeName
	 * @param value

	 */
	public abstract void addElement ( Row row, String nodeName, String value );

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
}
