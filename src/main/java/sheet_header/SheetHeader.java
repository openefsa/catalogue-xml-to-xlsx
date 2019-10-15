package sheet_header;

/**
 * Model a sheet header. In particular we need the information related to:
 * - the name of the column, that is, the header name
 * - the index of the column in the sheet
 * @author avonva
 *
 */
public class SheetHeader {
	
	private int columnIndex;
	private String columnName;

	/**
	 * Create a sheet header
	 * @param columnIndex the index that the column will obtain once created
	 * @param columnName the name of the new column
	 */
	public SheetHeader( int columnIndex, String columnName ) {
		this.columnIndex = columnIndex;
		this.columnName = columnName;
	}
	public int getColumnIndex() {
		return columnIndex;
	}
	public String getColumnName() {
		return columnName;
	}

	@Override
	public boolean equals(Object obj) {

		if ( !( obj instanceof SheetHeader ) )
			return false;

		SheetHeader header = (SheetHeader) obj;

		// if same column name or same index we say that two sheet headers are equal
		return header.getColumnName().equals( columnName ) || header.getColumnIndex() == columnIndex;
	}
	
	@Override
	public String toString() {
		return "HEADER: index=" + columnIndex + ";name=" + columnName;
	}
}
