package com.temenos.dbx.product.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Customership implements DBPDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8540895441199243304L;
	private String companyLegalUnit;
	private String localResponsibleUnit;
	private String companyLevelResponsibleUnit;
	private String centralResponsibleUnit;
	private String localResponsibleOfficer;
	private String companyResponsibleOfficer;
	private String centralResponsibleOfficer;
	private String customershipStartDate;
	private String customershipEndDate;
	private String customershipStatus;
	
	public Customership() {
	}
	
	/**
	 * @return the companyLegalUnit
	 */
	public String getCompanyLegalUnit() {
		return companyLegalUnit;
	}
	/**
	 * @param companyLegalUnit the companyLegalUnit to set
	 */
	public void setCompanyLegalUnit(String companyLegalUnit) {
		this.companyLegalUnit = companyLegalUnit;
	}
	/**
	 * @return the localResponsibleUnit
	 */
	public String getLocalResponsibleUnit() {
		return localResponsibleUnit;
	}
	/**
	 * @param localResponsibleUnit the localResponsibleUnit to set
	 */
	public void setLocalResponsibleUnit(String localResponsibleUnit) {
		this.localResponsibleUnit = localResponsibleUnit;
	}
	/**
	 * @return the companyLevelResponsibleUnit
	 */
	public String getCompanyLevelResponsibleUnit() {
		return companyLevelResponsibleUnit;
	}
	/**
	 * @param companyLevelResponsibleUnit the companyLevelResponsibleUnit to set
	 */
	public void setCompanyLevelResponsibleUnit(String companyLevelResponsibleUnit) {
		this.companyLevelResponsibleUnit = companyLevelResponsibleUnit;
	}
	/**
	 * @return the centralResponsibleUnit
	 */
	public String getCentralResponsibleUnit() {
		return centralResponsibleUnit;
	}
	/**
	 * @param centralResponsibleUnit the centralResponsibleUnit to set
	 */
	public void setCentralResponsibleUnit(String centralResponsibleUnit) {
		this.centralResponsibleUnit = centralResponsibleUnit;
	}
	/**
	 * @return the localResponsibleOfficer
	 */
	public String getLocalResponsibleOfficer() {
		return localResponsibleOfficer;
	}
	/**
	 * @param localResponsibleOfficer the localResponsibleOfficer to set
	 */
	public void setLocalResponsibleOfficer(String localResponsibleOfficer) {
		this.localResponsibleOfficer = localResponsibleOfficer;
	}
	/**
	 * @return the companyResponsibleOfficer
	 */
	public String getCompanyResponsibleOfficer() {
		return companyResponsibleOfficer;
	}
	/**
	 * @param companyResponsibleOfficer the companyResponsibleOfficer to set
	 */
	public void setCompanyResponsibleOfficer(String companyResponsibleOfficer) {
		this.companyResponsibleOfficer = companyResponsibleOfficer;
	}
	/**
	 * @return the centralResponsibleOfficer
	 */
	public String getCentralResponsibleOfficer() {
		return centralResponsibleOfficer;
	}
	/**
	 * @param centralResponsibleOfficer the centralResponsibleOfficer to set
	 */
	public void setCentralResponsibleOfficer(String centralResponsibleOfficer) {
		this.centralResponsibleOfficer = centralResponsibleOfficer;
	}
	/**
	 * @return the customershipStartDate
	 */
	public String getCustomershipStartDate() {
		return customershipStartDate;
	}
	/**
	 * @param customershipStartDate the customershipStartDate to set
	 */
	public void setCustomershipStartDate(String customershipStartDate) {
		this.customershipStartDate = customershipStartDate;
	}
	/**
	 * @return the customershipEndDate
	 */
	public String getCustomershipEndDate() {
		return customershipEndDate;
	}
	/**
	 * @param customershipEndDate the customershipEndDate to set
	 */
	public void setCustomershipEndDate(String customershipEndDate) {
		this.customershipEndDate = customershipEndDate;
	}
	/**
	 * @return the customershipStatus
	 */
	public String getCustomershipStatus() {
		return customershipStatus;
	}
	/**
	 * @param customershipStatus the customershipStatus to set
	 */
	public void setCustomershipStatus(String customershipStatus) {
		this.customershipStatus = customershipStatus;
	}
	
}
