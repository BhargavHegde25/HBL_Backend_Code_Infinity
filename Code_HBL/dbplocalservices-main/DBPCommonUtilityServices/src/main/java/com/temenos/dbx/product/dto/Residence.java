package com.temenos.dbx.product.dto;

import org.json.JSONObject;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.temenos.dbx.product.utils.DTOUtils;

@JsonInclude(Include.NON_NULL)
public class Residence implements DBPDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6756455053155858391L;
	private String type;
	private String country;
	private String status;
	private String region;
	private String statusComments;
	private boolean statutoryRequirementMet;
	private String endDate;
	private JSONObject extensionData;
	
	public Residence() {
	}
	
	/**
	 * @return the residenceType
	 */
	public String getResidenceType() {
		return type;
	}
	/**
	 * @param residenceType the residenceType to set
	 */
	public void setResidenceType(String residenceType) {
		this.type = residenceType;
	}
	/**
	 * @return the countryOfResidence
	 */
	public String getCountryOfResidence() {
		return country;
	}
	/**
	 * @param countryOfResidence the countryOfResidence to set
	 */
	public void setCountryOfResidence(String countryOfResidence) {
		this.country = countryOfResidence;
	}
	/**
	 * @return the residentialStatus
	 */
	public String getResidentialStatus() {
		return status;
	}
	/**
	 * @param residentialStatus the residentialStatus to set
	 */
	public void setResidentialStatus(String residentialStatus) {
		this.status = residentialStatus;
	}
	/**
	 * @return the residenceRegion
	 */
	public String getResidenceRegion() {
		return region;
	}
	/**
	 * @param residenceRegion the residenceRegion to set
	 */
	public void setResidenceRegion(String residenceRegion) {
		this.region = residenceRegion;
	}
	/**
	 * @return the residenceStatusComments
	 */
	public String getResidenceStatusComments() {
		return statusComments;
	}
	/**
	 * @param residenceStatusComments the residenceStatusComments to set
	 */
	public void setResidenceStatusComments(String residenceStatusComments) {
		this.statusComments = residenceStatusComments;
	}
	
	/**
	 * @return the statutoryRequirementMet
	 */
	public boolean getStatutoryRequirementMet() {
		return statutoryRequirementMet;
	}
	/**
	 * @param statutoryRequirementMet the statutoryRequirementMet to set
	 */
	public void setStatutoryRequirementMet(boolean statutoryRequirementMet) {
		this.statutoryRequirementMet = statutoryRequirementMet;
	}
	
	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	/**
	 * @return the extensionData
	 */
	public JSONObject getExtensionData() {
		return extensionData;
	}
	/**
	 * @param extensionData the extensionData to set
	 */
	public void setExtensionData(JSONObject extensionData) {
		this.extensionData = extensionData;
	}
	
}
