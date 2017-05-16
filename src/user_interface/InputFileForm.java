package user_interface;

import javax.xml.transform.TransformerException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import xml_to_excel.XmlCatalogueToExcel;

public class InputFileForm {

	private String inputName = "";
	private String outputName = "";
	
	public Shell shell;
	public Display display;
	private Button convertBtn;
	
	public InputFileForm( Shell shell, Display display ) {
		this.shell = shell;
		this.display = display;
	}
	
	public void display() {
		
		// set the application name in the shell
		shell.setText( "Xml to excel - Catalogue converter" );
		shell.setLayout( new GridLayout( 1 , false ) );
		shell.addPaintListener( new PaintListener() {
			public void paintControl ( PaintEvent e ) {
				shell.layout();
			}
		} );
		
		
		// input filename
		
		Group inputGroup = new Group ( shell, SWT.NONE );
		inputGroup.setText( "Select the xml input filename:" );
		inputGroup.setLayout( new GridLayout( 2, false ) );
		
		final Text inputText = addFilenameGroup( inputGroup, "Choose the input file", ".xml" );
		inputText.addModifyListener( new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent arg0) {
				
				inputName = inputText.getText();
				
				convertBtn.setEnabled( !inputName.isEmpty() && !outputName.isEmpty() );
			}
		});
		
		
		// Output filename
		
		Group outputGroup = new Group ( shell, SWT.NONE );
		outputGroup.setText( "Choose the xlsx output filename:" );
		outputGroup.setLayout( new GridLayout( 2, false ) );
		
		final Text outputText = addFilenameGroup( outputGroup, "Choose the output filename", ".xlsx" );
		outputText.addModifyListener( new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent arg0) {
				
				outputName = outputText.getText();
				
				convertBtn.setEnabled( !inputName.isEmpty() && !outputName.isEmpty() );
			}
		});
		
		convertBtn = new Button ( shell, SWT.PUSH );
		convertBtn.setText( "Start conversion" );
		convertBtn.setEnabled( false );
		
		// start the conversion
		convertBtn.addSelectionListener( new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				String title = "";
				String message = "";
				
				try {
					startConversion( inputText.getText(), outputText.getText() );
					
					title = "Done!";
					message = "The conversion was correctly terminated";
					
				} catch (TransformerException e) {
					title = "Error!";
					message = "Something went wrong during the conversion. Check the filenames!";
				}
				
				MessageBox mb = new MessageBox( shell, SWT.OK );
				mb.setText( title );
				mb.setMessage( message );
				mb.open();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
		shell.pack();
	}
	
	/**
	 * Add a group which allows to choose a file and display the selected filename
	 * @param parent
	 * @param title
	 * @param extension
	 * @return
	 */
	private Text addFilenameGroup ( Composite parent, String title, String extension ) {
		
		GridData gridData = new GridData();
		gridData.widthHint = 250;

		// create text to visualize selected file
		Text filenameText = new Text( parent, SWT.NONE );
		filenameText.setEditable( false );
		filenameText.setLayoutData( gridData );
		
		// button to open the dialog and ask filename
		Button openDialogBtn = new Button ( parent, SWT.PUSH );
		openDialogBtn.setText( "Open explorer" );
		openDialogBtn.addSelectionListener( new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				// ask the input filename
				String filename = ChooseFileDialog.showFileDialog( shell, 
						title, extension );
				
				if ( filename == null )
					return;
				
				filenameText.setText( filename );
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
		return filenameText;
	}
	
	/**
	 * Start the conversion
	 * @param input
	 * @param output
	 * @throws TransformerException 
	 */
	private void startConversion ( String input, String output ) throws TransformerException {
		
		setCursor ( SWT.CURSOR_WAIT );
		
		XmlCatalogueToExcel converter = new XmlCatalogueToExcel ( input, output );
		converter.convertXmlToExcel();
		
		setCursor ( SWT.CURSOR_ARROW );
	}
	
	/**
	 * Update the shell cursor
	 * @param cursorType
	 */
	private void setCursor ( int cursorType ) {
		
		// dispose the cursor if there is one
		if ( shell.getCursor() != null )
			shell.getCursor().dispose();

		// change the cursor to the new cursor
		shell.setCursor( new Cursor( shell.getDisplay() , cursorType ) );
	}
	
	
	/**
	 * Start the application
	 * @param args
	 */
	public static void main ( String[] args ) { 
		
		// create a new shell for the ui
		Display display = new Display();
		Shell shell = new Shell ( display );
		
		// open the form
		InputFileForm form = new InputFileForm( shell, display );
		
		form.display();
		
		// show the shell
		shell.open();

		// loop for updating ui
		while ( !form.shell.isDisposed() ) {
			if ( !form.display.readAndDispatch() )
				form.display.sleep();
		}
		
		// dispose display
		display.dispose();
	}
}
