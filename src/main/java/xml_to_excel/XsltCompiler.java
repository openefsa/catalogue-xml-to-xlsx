package xml_to_excel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class XsltCompiler {

	private static final Logger LOGGER = LogManager.getLogger(XsltCompiler.class);

	private String inputFilename;
	private String xsltFilename;
	private String outputFilename;

	/**
	 * Apply a XSLT transformation to the XLM input file. The created XML output
	 * filename is specified with the outputFilename string.
	 * 
	 * @param inputFilename
	 * @param xsltFilename
	 * @param outputFilename
	 * @throws TransformerException
	 */
	public XsltCompiler(String inputFilename, String xsltFilename, String outputFilename) {

		this.inputFilename = inputFilename;
		this.xsltFilename = xsltFilename;
		this.outputFilename = outputFilename;
	}

	/**
	 * Transform the input xml file using the xslt file
	 * 
	 * @throws TransformerException
	 */
	public void compile() throws TransformerException {

		// prepare xslt source file
		InputStream stream = XsltCompiler.class.getClassLoader().getResourceAsStream(xsltFilename);
		Source xslt = new StreamSource(stream);

		// Get a factory instance to apply the xslt
		TransformerFactory factory = TransformerFactory.newInstance();

		// set the transformer
		Transformer transformer = factory.newTransformer(xslt);

		// transform the input with the xslt and create the output
		StreamSource text = new StreamSource(new File(inputFilename));
		StreamResult output = new StreamResult(new File(outputFilename));
		transformer.transform(text, output);

		try {
			transformer.reset();
			stream.close();
		} catch (IOException e) {
			LOGGER.error("Cannot apply xslt", e);
			e.printStackTrace();
		}
	}
}
