package user_interface;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class InputFileForm {

	private ConvertionStartedListener listener;
	
	private String inputName = "";
	private String outputName = "";
	
	public Shell shell;
	public Display display;
	private Button convertBtn;
	private Text inputText;
	private Text outputText;
	
	public InputFileForm( Shell shell, Display display ) {
		this.shell = shell;
		this.display = display;
	}
	
	/**
	 * Enable or disable the entire form
	 * @param enabled
	 */
	public void setEnabled ( boolean enabled ) {
		convertBtn.setEnabled( enabled );
		inputText.setEnabled( enabled );
		outputText.setEnabled( enabled );
		( (Button) inputText.getData() ).setEnabled( enabled );
		( (Button) outputText.getData() ).setEnabled( enabled );
	}
	
	/**
	 * Listener used when the ok button is pressed
	 * @param listener
	 */
	public void setListener(ConvertionStartedListener listener) {
		this.listener = listener;
	}
	
	public void display( String title, String inputFormat, String outputFormat ) {
		
		// set the application name in the shell
		shell.setText( title );
		shell.setLayout( new GridLayout( 1 , false ) );
		shell.addPaintListener( new PaintListener() {
			public void paintControl ( PaintEvent e ) {
				shell.layout();
			}
		} );
		
		
		// input filename
		
		Group inputGroup = new Group ( shell, SWT.NONE );
		inputGroup.setText( "Select the " + inputFormat + " input filename:" );
		inputGroup.setLayout( new GridLayout( 2, false ) );
		
		inputText = addFilenameGroup( inputGroup, 
				"Choose the input file", inputFormat );
		inputText.addModifyListener( new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent arg0) {
				
				inputName = inputText.getText();
				
				convertBtn.setEnabled( !inputName.isEmpty() && !outputName.isEmpty() );
			}
		});
		
		
		// Output filename
		
		Group outputGroup = new Group ( shell, SWT.NONE );
		outputGroup.setText( "Choose the " + outputFormat + " output filename:" );
		outputGroup.setLayout( new GridLayout( 2, false ) );
		
		outputText = addFilenameGroup( outputGroup, 
				"Choose the output filename", outputFormat );
		
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
				
				String input = inputText.getText();
				String output = outputText.getText();

				if ( listener != null )
					listener.started( input, output );
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
	private Text addFilenameGroup ( Composite parent, 
			String title, String extension ) {
		
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
		
		filenameText.setData( openDialogBtn );
		
		return filenameText;
	}
	
	/**
	 * Listener called if the conversion was started
	 * @author avonva
	 *
	 */
	public interface ConvertionStartedListener {
		public void started( String inputFilename, String outputFilename );
	}
}
