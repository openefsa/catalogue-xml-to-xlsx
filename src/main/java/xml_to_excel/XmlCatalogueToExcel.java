package xml_to_excel;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import naming_convention.Headers;
import sheet_converter.AttributeSheetConverter;
import sheet_converter.CatalogueSheetConverter;
import sheet_converter.HierarchySheetConverter;
import sheet_converter.NotesSheetConverter;
import sheet_converter.SheetConverter;
import sheet_converter.TermSheetConverter;
import sheet_converter.XmlNodes;

/**
 * Convert a catalogue from XML format to excel format. The excel contains 5
 * sheets: - catalogue - hierarchy - attribute - term - releaseNotes
 * 
 * @author shahaal
 * @author avonva
 *
 */
public class XmlCatalogueToExcel {

	private static final Logger LOGGER = LogManager.getLogger(XmlCatalogueToExcel.class);

	// the name of the xslt which filter only the catalogue information from the
	// input xml
	public static final String CATALOGUE_XSLT_NAME = "catalogue.xslt";

	// the name of the xslt which filter only the hierarchy information from the
	// input xml
	public static final String HIERARCHY_XSLT_NAME = "hierarchy.xslt";

	// the name of the xslt which filter only the attribute information from the
	// input xml
	public static final String ATTRIBUTE_XSLT_NAME = "attribute.xslt";

	// the name of the xslt which filter only the term information from the input
	// xml
	public static final String TERM_XSLT_NAME = "term.xslt";

	// the name of the xslt which filter only the release notes information from the
	// input xml
	public static final String NOTES_XSLT_NAME = "releaseNotes.xslt";

	// the xml file which has to be converted
	private String inputXml;

	// the xlsx file which has to be created
	private String outputXlsx;

	/**
	 * Start the converter from command line
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		LOGGER.info(
				"#### Remember to increase the RAM max limit if you are converting big catalogues! (e.g. -Xms1024m) ####");

		if (args.length != 2) {

			LOGGER.error(
					"Wrong number of arguments. Please specify the input catalogue xml and the output xlsx file path "
							+ "(example: java -jar xmlToExcel.jar D:\\catalogue.xml D:\\output.xlsx)");

			return;
		}

		// convert the xml to excel
		XmlCatalogueToExcel converter = new XmlCatalogueToExcel(args[0], args[1]);
		try {
			converter.convertXmlToExcel();
		} catch (TransformerException e) {	
			LOGGER.error("Cannot convert xml to xlsx", e);
			e.printStackTrace();
		}
	}

	/**
	 * Constructor Convert the catalogue from the .XML format to the .xlsx format
	 * (excel workbook) The input parameter is the xml file to be converted
	 * 
	 * @parm XmlFilename, the file to be converted
	 */
	public XmlCatalogueToExcel(String inputXml, String outputXlsx) {
		this.inputXml = inputXml;
		this.outputXlsx = outputXlsx;
	}

	/**
	 * Convert the xml catalogue file into an excel file with 4 sheet
	 * 
	 * @author shahaal
	 * @author avonva
	 * @throws TransformerException
	 */
	public void convertXmlToExcel() throws TransformerException {

		// create a new workbook
		try (SXSSFWorkbook workbook = new SXSSFWorkbook()) {

			// convert catalogue sheet
			final ConversionPerformer cat = new ConversionPerformer(workbook, inputXml, CATALOGUE_XSLT_NAME) {

				@Override
				public SheetConverter getConverter(String filename) {
					// create a catalogue sheet converter to parse the xml file
					CatalogueSheetConverter catConverter = new CatalogueSheetConverter(filename,
							XmlNodes.CATALOGUE_ROOT_NODE);
					return catConverter;
				}

				@Override
				public void makePreliminarOperations(SheetConverter converter, Sheet sheet) {
				}
			};

			cat.convert(Headers.CAT_SHEET_NAME);

			// convert hierarchy sheet
			final ConversionPerformer hier = new ConversionPerformer(workbook, inputXml, HIERARCHY_XSLT_NAME) {

				@Override
				public SheetConverter getConverter(String inputFilename) {
					// create a hierarchy sheet converter to parse the xml file
					HierarchySheetConverter hierarchyConverter = new HierarchySheetConverter(inputFilename,
							XmlNodes.HIERARCHY_ROOT_NODE);
					return hierarchyConverter;
				}

				@Override
				public void makePreliminarOperations(SheetConverter converter, Sheet sheet) {

					if (converter instanceof HierarchySheetConverter)
						((HierarchySheetConverter) converter).addMasterHierarchy(cat.getSheet());
				}
			};

			hier.convert(Headers.HIER_SHEET_NAME);

			// convert attr sheet
			final ConversionPerformer attr = new ConversionPerformer(workbook, inputXml, ATTRIBUTE_XSLT_NAME) {

				@Override
				public SheetConverter getConverter(String inputFilename) {

					// create a attribute sheet converter to parse the xml file
					AttributeSheetConverter attrConverter = new AttributeSheetConverter(inputFilename,
							XmlNodes.ATTRIBUTE_ROOT_NODE);

					return attrConverter;
				}

				@Override
				public void makePreliminarOperations(SheetConverter converter, Sheet sheet) {
				}
			};

			attr.convert(Headers.ATTR_SHEET_NAME);

			// convert term sheet
			ConversionPerformer term = new ConversionPerformer(workbook, inputXml, TERM_XSLT_NAME) {

				@Override
				public SheetConverter getConverter(String inputFilename) {

					// create a term converter, we need the hierarchy and attribute sheet to
					// create the term sheet
					TermSheetConverter termConverter = new TermSheetConverter(inputFilename, XmlNodes.TERM_ROOT_NODE,
							hier.getSheet(), attr.getSheet());

					// set as master hierarchy code the catalogue code
					termConverter
							.setMasterHierarchyCode(SheetConverter.getSheetColumn(cat.getSheet(), Headers.CODE).get(0));

					return termConverter;
				}

				@Override
				public void makePreliminarOperations(SheetConverter converter, Sheet sheet) {
				}
			};

			term.convert(Headers.TERM_SHEET_NAME);

			// create release notes sheet
			ConversionPerformer notes = new ConversionPerformer(workbook, inputXml, NOTES_XSLT_NAME) {

				@Override
				public SheetConverter getConverter(String inputFilename) {
					NotesSheetConverter notesConverter = new NotesSheetConverter(inputFilename, XmlNodes.OP_INFO);

					return notesConverter;
				}

				@Override
				public void makePreliminarOperations(SheetConverter converter, Sheet sheet) {
				}
			};

			notes.convert(Headers.NOTES_SHEET_NAME);

			LOGGER.info("Writing the excel file...");

			// solve memory leak, save the results into the excel file
			try (FileOutputStream fileOut = new FileOutputStream(outputXlsx)) {

				// remove limits of dimensions for the workbook
				ZipSecureFile.setMinInflateRatio(0);

				workbook.write(fileOut);
				LOGGER.info("Done");

				fileOut.flush();
				fileOut.close();
			}

		} catch (IOException e) {
			LOGGER.error("Cannot convert xml to xlsx", e);
			e.printStackTrace();
		}
	}
}
