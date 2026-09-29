package com.temenos.dbx.product.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PartyAssessment implements DBPDTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1566670532598326913L;
	private String assessmentType;
	private String assessmentStatus;
	private String assessmentLevel;
	private String assessmentScore;
	private String assessmentBody;
	private String assessmentConclusionDate;
	private String assessmentDetails;
	private String assessmentNature;
	private String reasonOfTheAssessment;
	private String nextAssessmentDate;
	private String assessmentExpirydate;
	
	public PartyAssessment() {
	}
	
	/**
	 * @return the assessmentType
	 */
	public String getAssessmentType() {
		return assessmentType;
	}
	/**
	 * @param assessmentType the assessmentType to set
	 */
	public void setAssessmentType(String assessmentType) {
		this.assessmentType = assessmentType;
	}
	/**
	 * @return the assessmentStatus
	 */
	public String getAssessmentStatus() {
		return assessmentStatus;
	}
	/**
	 * @param assessmentStatus the assessmentStatus to set
	 */
	public void setAssessmentStatus(String assessmentStatus) {
		this.assessmentStatus = assessmentStatus;
	}
	/**
	 * @return the assessmentLevel
	 */
	public String getAssessmentLevel() {
		return assessmentLevel;
	}
	/**
	 * @param assessmentLevel the assessmentLevel to set
	 */
	public void setAssessmentLevel(String assessmentLevel) {
		this.assessmentLevel = assessmentLevel;
	}
	/**
	 * @return the assessmentScore
	 */
	public String getAssessmentScore() {
		return assessmentScore;
	}
	/**
	 * @param assessmentScore the assessmentScore to set
	 */
	public void setAssessmentScore(String assessmentScore) {
		this.assessmentScore = assessmentScore;
	}
	/**
	 * @return the assessmentBody
	 */
	public String getAssessmentBody() {
		return assessmentBody;
	}
	/**
	 * @param assessmentBody the assessmentBody to set
	 */
	public void setAssessmentBody(String assessmentBody) {
		this.assessmentBody = assessmentBody;
	}
	/**
	 * @return the assessmentConclusionDate
	 */
	public String getAssessmentConclusionDate() {
		return assessmentConclusionDate;
	}
	/**
	 * @param assessmentConclusionDate the assessmentConclusionDate to set
	 */
	public void setAssessmentConclusionDate(String assessmentConclusionDate) {
		this.assessmentConclusionDate = assessmentConclusionDate;
	}
	/**
	 * @return the assessmentDetails
	 */
	public String getAssessmentDetails() {
		return assessmentDetails;
	}
	/**
	 * @param assessmentDetails the assessmentDetails to set
	 */
	public void setAssessmentDetails(String assessmentDetails) {
		this.assessmentDetails = assessmentDetails;
	}
	/**
	 * @return the assessmentNature
	 */
	public String getAssessmentNature() {
		return assessmentNature;
	}
	/**
	 * @param assessmentNature the assessmentNature to set
	 */
	public void setAssessmentNature(String assessmentNature) {
		this.assessmentNature = assessmentNature;
	}
	/**
	 * @return the reasonOfTheAssessment
	 */
	public String getReasonOfTheAssessment() {
		return reasonOfTheAssessment;
	}
	/**
	 * @param reasonOfTheAssessment the reasonOfTheAssessment to set
	 */
	public void setReasonOfTheAssessment(String reasonOfTheAssessment) {
		this.reasonOfTheAssessment = reasonOfTheAssessment;
	}
	/**
	 * @return the nextAssessmentDate
	 */
	public String getNextAssessmentDate() {
		return nextAssessmentDate;
	}
	/**
	 * @param nextAssessmentDate the nextAssessmentDate to set
	 */
	public void setNextAssessmentDate(String nextAssessmentDate) {
		this.nextAssessmentDate = nextAssessmentDate;
	}
	/**
	 * @return the assessmentExpirydate
	 */
	public String getAssessmentExpirydate() {
		return assessmentExpirydate;
	}
	/**
	 * @param assessmentExpirydate the assessmentExpirydate to set
	 */
	public void setAssessmentExpirydate(String assessmentExpirydate) {
		this.assessmentExpirydate = assessmentExpirydate;
	}
	
}
