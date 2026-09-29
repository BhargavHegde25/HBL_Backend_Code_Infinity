/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2024. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradelending.dto;

import static com.temenos.infinity.tradelending.constants.TradeLendingConstants.PARAM_CREATED_DATE;
import static com.temenos.infinity.tradelending.constants.TradeLendingConstants.PARAM_RECORD_ID;
import static com.temenos.infinity.tradelending.constants.TradeLendingConstants.PARAM_UPDATED_DATE;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author mrunalini.adepu
 *
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class RolloverRequestDTO implements DBPDTO{

	private String facilityId;
	private String loanId;
    private String rolloverDays;
    private String rolloverMonths;
    private String rolloverYears;
    @JsonAlias(PARAM_CREATED_DATE)
    private String createdDate;
    @JsonAlias({PARAM_RECORD_ID})
    private String rolloverRequestId;
    @JsonAlias({PARAM_UPDATED_DATE})
    private String updatedDate;
    private String dbpErrCode;
    private String dbpErrMsg;
    
	public String getFacilityId() {
		return facilityId;
	}
	
	public void setFacilityd(String facilityd) {
		this.facilityId = facilityId;
	}
	
	public String getLoanId() {
		return loanId;
	}
	
	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}
	
	public String getRolloverDays() {
		return rolloverDays;
	}
	
	public void setRolloverDays(String rolloverDays) {
		this.rolloverDays = rolloverDays;
	}
	
	public String getRolloverMonths() {
		return rolloverMonths;
	}
	
	public void setRolloverMonths(String rolloverMonths) {
		this.rolloverMonths = rolloverMonths;
	}
	
	public String getRolloverYears() {
		return rolloverYears;
	}
	
	public void setRolloverYears(String rolloverYears) {
		this.rolloverYears = rolloverYears;
	}
	
	public String getCreatedDate() {
		return createdDate;
	}
	
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	
	public String getRolloverRequestId() {
		return rolloverRequestId;
	}
	
	public void setRolloverRequestId(String rolloverRequestId) {
		this.rolloverRequestId = rolloverRequestId;
	}
	
	public String getUpdatedDate() {
		return updatedDate;
	}
	
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	public String getDbpErrCode() {
		return dbpErrCode;
	}
	
	public void setDbpErrCode(String dbpErrCode) {
		this.dbpErrCode = dbpErrCode;
	}
	
	public String getDbpErrMsg() {
		return dbpErrMsg;
	}
	
	public void setDbpErrMsg(String dbpErrMsg) {
		this.dbpErrMsg = dbpErrMsg;
	}
    
    
}
