package data_transformation;

/**
 * Model a <hierarchyAssignment> xml node and its children
 * concerning the catalogue xml data related to terms
 * @author avonva
 *
 */
public class HierarchyAssignment {

	private String hierarchyCode;
	private String parentCode;
	private String order;
	private String reportable;
	
	public void setHierarchyCode(String hierarchyCode) {
		this.hierarchyCode = hierarchyCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public void setReportable(String reportable) {
		this.reportable = reportable;
	}
	
	public String getHierarchyCode() {
		return hierarchyCode;
	}
	public String getParentCode() {
		return parentCode;
	}
	public String getOrder() {
		return order;
	}
	public String getReportable() {
		return reportable;
	}
	
	/**
	 * The assignment exists therefore the flag is set to 1
	 * @return
	 */
	public String getFlag() {
		return "1";
	}
	
	/**
	 * Get the flag column name
	 * @return
	 */
	public String getFlagColumn() {
		return hierarchyCode + "Flag";
	}
	
	/**
	 * Get the parent code column name
	 * @return
	 */
	public String getParentCodeColumn() {
		return hierarchyCode + "ParentCode";
	}
	
	/**
	 * Get the order column name
	 * @return
	 */
	public String getOrderColumn() {
		return hierarchyCode + "Order";
	}
	
	/**
	 * Get the reportable column name
	 * @return
	 */
	public String getReportableColumn() {
		return hierarchyCode + "Reportable";
	}
	
	
	@Override
	public String toString() {

		return "ASSIGN : code " + hierarchyCode + " parentCode " + getParentCodeColumn() + 
				" flag " + getFlagColumn() + " order " + getOrderColumn() + 
				" report " + getReportableColumn();
	}
}
