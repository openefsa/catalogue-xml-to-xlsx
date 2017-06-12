package user_interface;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;


public class ChooseFileDialog {

	/**
	 * Open a file dialog to select a filename. We check if the extension was inserted or not. If not add it.
	 * @param shell
	 * @param text
	 * @param extension
	 * @param inputFile
	 * @return
	 */
	public static String showFileDialog ( Shell shell, String text, String extension ) {

		FileDialog dialog = new FileDialog( shell, SWT.OPEN );
		
		// set dialog text
		dialog.setText( text );
		dialog.setFilterExtensions( new String[] { "*" + extension } );
		
		String filename = dialog.open();
		
		// if no extension, add the extension
		if ( getFileExtension( filename ).isEmpty() )
			filename = filename + extension;
		
		return filename;
	}
	
	/**
	 * Get the filename extension
	 * @param filename
	 * @return
	 */
	private static String getFileExtension( String filename ) {

	    try {
	        return filename.substring( filename.lastIndexOf(".") );
	    } catch (Exception e) {
	        return "";
	    }
	}
}
