package xml_to_excel;

import java.io.File;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;


public class XsltCompiler {

	String inputFilename;
	String xsltFilename;
	String outputFilename;
	
	/**
	 * Apply a XSLT transformation to the XLM input file. The created XML output filename is
	 * specified with the outputFilename string.
	 * @param inputFilename
	 * @param xsltFilename
	 * @param outputFilename
	 * @throws TransformerException
	 */
	public XsltCompiler( String inputFilename, String xsltFilename, String outputFilename ) {

		this.inputFilename = inputFilename;
		this.xsltFilename = xsltFilename;
		this.outputFilename = outputFilename;
	}
	
	/**
	 * Transform the input xml file using the xslt file
	 * @throws TransformerException 
	 */
	public void compile () throws TransformerException {
		
		// Get a factory instance to apply the xslt
		TransformerFactory factory = TransformerFactory.newInstance();
		
		Source xslt = new StreamSource( this.getClass().getClassLoader().getResourceAsStream( xsltFilename ) );

		// set the transformer
		Transformer transformer = factory.newTransformer( xslt );

		// transform the input with the xslt and create the output
		Source text = new StreamSource( new File( inputFilename ) );
		transformer.transform( text, new StreamResult( new File( outputFilename ) ) );
	}
}
