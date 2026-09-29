package com.temenos.dbx.product.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PartyLifeCycle implements DBPDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8230463995568049807L;
	private String lifeCycleStatusType;
	private String startDate;
	private String endDate;
	private String changeReason;
	
	public PartyLifeCycle() {
	}
	
	/**
	 * @return the lifeCycleStatusType
	 */
	public String getLifeCycleStatusType() {
		return lifeCycleStatusType;
	}
	/**
	 * @param lifeCycleStatusType the lifeCycleStatusType to set
	 */
	public void setLifeCycleStatusType(String lifeCycleStatusType) {
		this.lifeCycleStatusType = lifeCycleStatusType;
	}
	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
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
	 * @return the changeReason
	 */
	public String getChangeReason() {
		return changeReason;
	}
	/**
	 * @param changeReason the changeReason to set
	 */
	public void setChangeReason(String changeReason) {
		this.changeReason = changeReason;
	}
	
}
