package com.temenos.dbx.product.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PartyClassification implements DBPDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3897159779786182704L;
	private String classificationScheme;
	private String classificationCode;
	private String classificationDate;
	private String classificationReason;
	private String classificationInformationSource;
	
	public PartyClassification() {
	}
	
	/**
	 * @return the classificationScheme
	 */
	public String getClassificationScheme() {
		return classificationScheme;
	}
	/**
	 * @param classificationScheme the classificationScheme to set
	 */
	public void setClassificationScheme(String classificationScheme) {
		this.classificationScheme = classificationScheme;
	}
	/**
	 * @return the classificationCode
	 */
	public String getClassificationCode() {
		return classificationCode;
	}
	/**
	 * @param classificationCode the classificationCode to set
	 */
	public void setClassificationCode(String classificationCode) {
		this.classificationCode = classificationCode;
	}
	/**
	 * @return the classificationDate
	 */
	public String getClassificationDate() {
		return classificationDate;
	}
	/**
	 * @param classificationDate the classificationDate to set
	 */
	public void setClassificationDate(String classificationDate) {
		this.classificationDate = classificationDate;
	}
	/**
	 * @return the classificationReason
	 */
	public String getClassificationReason() {
		return classificationReason;
	}
	/**
	 * @param classificationReason the classificationReason to set
	 */
	public void setClassificationReason(String classificationReason) {
		this.classificationReason = classificationReason;
	}
	/**
	 * @return the classificationInformationSource
	 */
	public String getClassificationInformationSource() {
		return classificationInformationSource;
	}
	/**
	 * @param classificationInformationSource the classificationInformationSource to set
	 */
	public void setClassificationInformationSource(String classificationInformationSource) {
		this.classificationInformationSource = classificationInformationSource;
	}
	
}
