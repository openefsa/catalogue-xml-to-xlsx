package xml_to_excel;

import java.io.File;

import javax.xml.transform.TransformerException;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import sheet_converter.SheetConverter;

public abstract class ConversionPerformer {

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
	 * @param sheetname the name of the new sheet which 
	 * will be created in the workbook
	 */
	public void convert ( String sheetname ) {
		
		System.out.println ( "Creating " + sheetname + " sheet..." );
		
		// filter the input xml to get only the data related to the catalogue
		String outputFilename = filterXml( workbook, inputXml, XsltFilename, sheetname );
		
		SheetConverter converter = getConverter( outputFilename );

		// create the empty sheet
		sheet = converter.buildSheet( workbook, sheetname );
		
		makePreliminarOperations( converter, sheet );
		
		// parse the xml and insert the data
		converter.parse();
		
		// delete the temp file
		deleteFile( outputFilename );
		
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
	 */
	private String filterXml ( Workbook workbook, String inputXml, String XsltFilename, String outputName ) {
		
		String outputFilename = outputName + ".xml";
		
		// filter the input xml into a smaller xml
		XsltCompiler compiler = new XsltCompiler( inputXml, XsltFilename, 
				outputFilename );
		try {
			compiler.compile();
		} catch (TransformerException e) {
			e.printStackTrace();
			return null;
		}
		
		return outputFilename;
	}

	/**
	 * Delete the file contained in the path
	 * @param path
	 * @return
	 */
	private boolean deleteFile ( String path ) {
		File file = new File ( path );
		return file.delete();
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
