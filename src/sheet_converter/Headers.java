package sheet_converter;

/**
 * This class contains all the headers names of the 
 * catalogue in workbook format (xlsx).
 * Note that not all the headers can be statically
 * determined because some names depends on other names,
 * as the attributes values or the hierarchies parent
 * child relationships.
 * @author avonva
 *
 */
public class Headers {

	// workbook sheet names
	public static final String CAT_SHEET_NAME = "catalogue";
	public static final String ATTR_SHEET_NAME = "attribute";
	public static final String HIER_SHEET_NAME = "hierarchy";
	public static final String TERM_SHEET_NAME = "term";
	public static final String NOTES_SHEET_NAME = "releaseNotes";
	
	// general headers
	public static final String CODE = "code";
	public static final String NAME = "name";
	public static final String LABEL = "label";
	public static final String SCOPENOTE = "scopeNote";
	public static final String LAST_UPDATE = "lastUpdate";
	public static final String VALID_FROM = "validFrom";
	public static final String VALID_TO = "validTo";
	public static final String STATUS = "status";
	public static final String DEPRECATED = "deprecated";
	public static final String VERSION = "version";
	
	// headers specific for catalogues
	public static final String CAT_CODE_MASK = "termCodeMask";
	public static final String CAT_CODE_LENGTH = "termCodeLength";
	public static final String CAT_MIN_CODE = "termMinCode";
	public static final String CAT_ACCEPT_NOT_STD = "acceptNonStandardCodes";
	public static final String CAT_GEN_MISSING = "generateMissingCodes";
	public static final String CAT_GROUPS = "catalogueGroups";
	
	// headers specific for hierarchies
	public static final String HIER_APPL = "hierarchyApplicability";
	public static final String HIER_ORDER = "hierarchyOrder";
	public static final String HIER_GROUPS = "hierarchyGroups";
	
	/**
	 * Prefix code used for the master hierarchy in
	 * the columns of the term sheet related to the
	 * parent child relations (masterFlag, masterParentCode..)
	 */
	public static final String PREFIX_MASTER_CODE = "master";
	
	// headers specific for attributes
	public static final String ATTR_REPORT = "attributeReportable";
	public static final String ATTR_VISIB = "attributeVisible";
	public static final String ATTR_SEARCH = "attributeSearchable";
	public static final String ATTR_ORDER = "attributeOrder";
	public static final String ATTR_TYPE = "attributeType";
	public static final String ATTR_MAX_LENGTH = "attributeMaxLength";
	public static final String ATTR_PRECISION = "attributePrecision";
	public static final String ATTR_SCALE = "attributeScale";
	public static final String ATTR_CAT_CODE = "attributeCatalogueCode";
	public static final String ATTR_SR = "attributeSingleOrRepeatable";
	public static final String ATTR_INHERIT = "attributeInheritance";
	public static final String ATTR_UNIQUE = "attributeUniqueness";
	public static final String ATTR_ALIAS = "attributeTermCodeAlias";
	
	// headers specific for terms
	public static final String TERM_CODE = "termCode";
	public static final String TERM_EXT_NAME = "termExtendedName";
	public static final String TERM_SHORT_NAME = "termShortName";
	public static final String TERM_SCOPENOTE = "termScopeNote";
	
	// headers specific for release notes
	public static final String NOTES_DESCRIPTION = "noteDescription";
	public static final String NOTES_DATE = "noteDate";
	public static final String NOTES_VERSION = "noteInternalVersion";
	public static final String NOTES_NOTE = "internalVersionNote";
	
	// headers specific for release notes operations
	public static final String OP_INFO = "operationInfo";
	public static final String OP_NAME = "operationName";
	public static final String OP_DATE = "operationDate";
	public static final String OP_GROUP = "operationGroupId";
	
	// headers suffixes for applicabilities (parent-child relations)
	// e.g. for the report hierarchy we would have:
	// reportParentCode, reportOrder, reportReportable, reportFlag
	public static final String SUFFIX_PARENT_CODE = "ParentCode";
	public static final String SUFFIX_ORDER = "Order";
	public static final String SUFFIX_REPORT = "Reportable";
	public static final String SUFFIX_FLAG = "Flag";
	public static final String SUFFIX_HIER_CODE = "HierarchyCode";
}
