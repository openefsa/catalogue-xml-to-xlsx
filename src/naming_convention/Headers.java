package naming_convention;

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


	// The following headers identify columns in the new
	// xlsx export which will be used to recreate the
	// old FoodEx2 xlsx format. Attributes have their
	// name as headers, while hierarchies follow the
	// convention hierCode_columnCode

	// DATA sheet
	public static final String FOODEX_OLD_CODE = "foodexOldCode";
	public static final String MATRIX_CODE = "matrixCode";
	public static final String GEMS_CODE = "GEMSCode";
	public static final String LANGUAL_CODE = "LangualCode";
	public static final String IMPLICIT_FACETS = "implicitFacets";
	public static final String ALL_FACETS = "allFacets";
	public static final String SCIENTIFIC_NAMES = "scientificNames";
	public static final String COMMON_NAMES = "commonNames";
	public static final String TERM_TYPE = "termType";
	public static final String DETAIL_LEVEL = "detailLevel";
	public static final String FACET_SCHEMA = "facetSchema";
	public static final String FACET_APPL = "facetApplicability";
	public static final String LAST_VERSION = "lastVersion";

	// hierarchy code mapper from new to old
	// hierarchies codes (ordered)
	public static final String REPORT = "report";
	public static final String MASTER = "master";
	public static final String PEST = "pest";
	public static final String BIOMO = "biomo";
	public static final String FEED = "feed";
	public static final String EXPO = "expo";
	public static final String VET_DRUG = "vetdrug";
	public static final String BIOTANIC = "botanic";
	public static final String PART = "part";
	public static final String SOURCE = "source";
	public static final String RAC_SOURCE = "racsource";
	public static final String PROCESS = "process";
	public static final String INGRED = "ingred";
	public static final String MEDIUM = "medium";
	public static final String SWEET = "sweet";
	public static final String FORT = "fort";
	public static final String QUAL = "qual";
	public static final String COOK_EXT = "cookext";
	public static final String GEN = "gen";
	public static final String PROD = "prod";
	public static final String PACK_FORMAT = "packformat";
	public static final String PACK_MAT = "packmat";
	public static final String STATE = "state";
	public static final String FAT = "fat";
	public static final String ALCOHOL = "alcohol";
	public static final String DOUGH = "dough";
	public static final String COOK_METH = "cookmeth";
	public static final String PREP = "prep";
	public static final String PRESERV = "preserv";
	public static final String TREAT = "treat";
	public static final String PART_CON = "partcon";
	public static final String PLACE = "place";
	public static final String TARGET_CON = "targcon";
	public static final String USE = "use";
	public static final String RISK_INGRED = "riskingred";
	public static final String F_PURPOSE = "fpurpose";
	public static final String REPLEV = "replev";
	public static final String ANIMAGE = "animage";
	public static final String GENDER = "gender";
	public static final String LEGIS = "legis";
}
