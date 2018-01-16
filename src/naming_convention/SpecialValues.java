package naming_convention;

/**
 * This class contains all the special values which
 * have a special meaning into the catalogue workbook.
 * @author avonva
 *
 */
public class SpecialValues {

	/**
	 * If a term does not have a parent because it is
	 * in the first level of the tree, then it has this
	 * value as parent code (convention)
	 */
	public static final String NO_PARENT = "ROOT";
	
	public static final String TERM_TYPE_NAME = "termType";
	
	public static final String DETAIL_LEVEL_NAME = "detailLevel";
	
	public static final String IMPLICIT_FACETS_NAME = "implicitFacets";
	
	public static final String ALL_FACETS_NAME = "allFacets";
	
	// type of an attribute of type catalogue
	public static final String ATTR_CAT_TYPE = "catalogue";
	public static final String ATTR_APPL_BOTH = "both";
	
	// the order that will be assigned to the master
	public static final String MASTER_ORDER = "0";
	
	public static final String STATUS_DEPRECATED = "DEPRECATED";
}
