package xml_to_excel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.transform.TransformerException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import sheet_converter.SheetConverter;

public abstract class ConversionPerformer {
	
	private static final Logger LOGGER = LogManager.getLogger(ConversionPerformer.class);
	
	private static final String TEMP_FOLDER = "temp" 
			+ System.getProperty("file.separator");
	
	private Workbook workbook;
	private String inputXml;
	private String XsltFilename;
	private Sheet sheet;
	
	/**
	 * Initialize the conversion performer
	 * @param workbook the workbook in which we want to create the sheet
	 * @param inputXml the catalogue in .xml format
	 * @param XsltFilename the xlst which will used to filter the data related
	 * to this sheet
	 */
	public ConversionPerformer( Workbook workbook, String inputXml, String XsltFilename ) {
		this.workbook = workbook;
		this.inputXml = inputXml;
		this.XsltFilename = XsltFilename;
	}
	
	/**
	 * Start the conversion from .xml to .xslx
	 * @param sheetName the name of the new sheet which 
	 * will be created in the workbook
	 * @throws TransformerException 
	 */
	public void convert ( String sheetName ) throws TransformerException {
		
		LOGGER.info ( "Creating " + sheetName + " sheet..." );
		
		// create temp folder if it does not exist
		File folder = new File ( TEMP_FOLDER );
		if ( !folder.exists() )
			folder.mkdir();
		
		// filter the input xml to get only the data related to the catalogue
		String outputFilename = filterXml( workbook, inputXml, 
				XsltFilename, TEMP_FOLDER + sheetName );
		
		LOGGER.info ( sheetName + ": Created xml in " + outputFilename );
		
		SheetConverter converter = getConverter( outputFilename );
		
		// create the empty sheet
		sheet = converter.buildSheet( workbook, sheetName );

		makePreliminarOperations( converter, sheet );

		// parse the xml and insert the data
		converter.parse();

		try {
			Files.delete( Paths.get(outputFilename) );
		} catch (IOException e) {
			LOGGER.error("Error during delete ", e);
			e.printStackTrace();
		}

		// save the created sheet
		sheet = converter.getSheet();
	}
	
	/**
	 * Filter the input xml into a smaller one to select only the relevant information
	 * then convert the retrieved information into an excel sheet. We use an XSLT to
	 * make the filter.
	 * @param workbook, the workbook which will contain the new sheet
	 * @param XsltFilename, the xslt transformation to be applied to the input xml file
	 * @param outputName, the name of the output file
	 * @throws TransformerException 
	 */
	private String filterXml ( Workbook workbook, String inputXml, 
			String XsltFilename, String outputName ) throws TransformerException {

		// use current time to create different files (support threads)
		String outputFilename = outputName + "_" + System.nanoTime() + ".xml";
		
		// filter the input xml into a smaller xml
		XsltCompiler compiler = new XsltCompiler( inputXml, XsltFilename, 
				outputFilename );
		
		compiler.compile();

		return outputFilename;
	}
	
	/**
	 * Get the created sheet. Call this after {@link #convert(String)}
	 * otherwise get null.
	 * @return
	 */
	public Sheet getSheet() {
		return sheet;
	}
	
	/**
	 * Get the converter object which is needed to 
	 * transform the xml data into a worksheet
	 * @param inputFilename the name of the file which needs to be
	 * converted by the SheetConverter
	 * @return
	 */
	public abstract SheetConverter getConverter( String inputFilename );
	
	/**
	 * Make preliminary operations on the sheet before starting adding
	 * data. Note that here the sheet is already created with headers.
	 * @param converter
	 * @param sheet
	 */
	public abstract void makePreliminarOperations ( SheetConverter converter, Sheet sheet );
}
