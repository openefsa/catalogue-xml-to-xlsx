package data_transformation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Manage date format in standard format yyyy-MM-dd
 * @author avonva
 *
 */
public class DateTrimmer {
	
	private static final Logger LOGGER = LogManager.getLogger(DateTrimmer.class);

	/**
	 * Trim a timestamp only to year-month-day fields
	 * @param stringDate
	 * @return
	 */
	public static Date trimDate ( String stringDate ) {

		if ( stringDate.isEmpty() )
			return null;
		
		// try first format
		DateFormat format = new SimpleDateFormat( "yyyy-MM-dd" );
		Date date;
		try {
			
			date = format.parse( stringDate );
			
			return date;
			
		} catch (ParseException e) {
			
			// if it did not work => try second format
			
			DateFormat format2 = new SimpleDateFormat( "yyyy/MM/dd" );
			try {
				date = format2.parse( stringDate );
				return date;
			} catch (ParseException e1) {
				LOGGER.error("Error during date parsing ", e1);
				e1.printStackTrace();
			}
		}
		
		return null;
	}
	
	/**
	 * Convert a date into string with yyyy/MM/dd format
	 * @param date
	 * @return
	 */
	public static String dateToString ( Date date ) {
		
		if ( date == null )
			return "";
		
		DateFormat format = new SimpleDateFormat( "yyyy/MM/dd" );
		
		LOGGER.debug("Date after format is: " + format.format( date ));
		return format.format( date );
	}
}
