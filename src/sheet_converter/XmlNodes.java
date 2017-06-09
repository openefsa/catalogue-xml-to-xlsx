package sheet_converter;

/**
 * Class which contains the names of the xml nodes of a catalogue
 * @author avonva
 *
 */
public class XmlNodes {
	
	public static final String CATALOGUE_ROOT_NODE = "message";
	public static final String HIERARCHY_ROOT_NODE = "hierarchy";
	public static final String ATTRIBUTE_ROOT_NODE = "attribute";
	public static final String TERM_ROOT_NODE = "term";
	
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

	public static final String CAT_CODE_MASK = "termCodeMask";
	public static final String CAT_CODE_LENGTH = "termCodeLength";
	public static final String CAT_MIN_CODE = "termMinCode";
	public static final String CAT_ACCEPT_NOT_STD = "acceptNonStandardCodes";
	public static final String CAT_GEN_MISSING = "generateMissingCodes";
	public static final String CAT_GROUPS = "catalogueGroups";
	public static final String CAT_GROUP = "catalogueGroup";
	
	// tags specific for release notes
	public static final String RELEASE_NOTES = "releaseNotes";
	public static final String NOTES_DESCRIPTION = "noteDescription";
	public static final String NOTES_DATE = "noteDate";
	public static final String NOTES_VERSION = "noteInternalVersion";
	public static final String NOTES_NOTE = "internalVersionNote";
	public static final String NOTES_VERSION_ATTRIBUTE_NAME = "internalVersion";

	public static final String HIER_APPL = "hierarchyApplicability";
	public static final String HIER_ORDER = "hierarchyOrder";
	public static final String HIER_GROUPS = "hierarchyGroups";
	public static final String HIER_GROUP = "hierarchyGroup";
	public static final String HIER_VERSION = "hierarchyVersion";
	
	public static final String HIER_ASSIGNMENT = "hierarchyAssignment";
	public static final String ASS_PARENT_CODE = "parentCode";
	public static final String ASS_ORDER = "order";
	public static final String ASS_REPORT = "reportable";
	public static final String ASS_HIER_CODE = "hierarchyCode";
	
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
	
	public static final String IMPLICIT_ATTR = "implicitAttribute";
	public static final String IMPLICIT_CODE = "attributeCode";
	public static final String IMPLICIT_VALUE = "attributeValue";
	
	public static final String TERM_CODE = "termCode";
	public static final String TERM_EXT_NAME = "termExtendedName";
	public static final String TERM_SHORT_NAME = "termShortName";
	public static final String TERM_SCOPENOTE = "termScopeNote";
	
	// headers specific for release notes operations
	public static final String OP_INFO = "operationInfo";
	public static final String OP_NAME = "operationName";
	public static final String OP_DATE = "operationDate";
	public static final String OP_GROUP = "operationGroupId";
	public static final String OP_DETAIL = "operationsDetail";
	
	public static final String NOTES_VERSION_DATE = "versionDate";
}
