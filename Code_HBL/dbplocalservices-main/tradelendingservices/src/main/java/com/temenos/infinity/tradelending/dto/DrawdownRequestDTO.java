/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2024. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradelending.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.json.JSONArray;

import static com.temenos.infinity.tradelending.constants.TradeLendingConstants.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class DrawdownRequestDTO implements DBPDTO{

	private String facilityId;
    private String loanProduct;
    private String currency;
    private String loanAmount;
    private String drawdownTermDays;
    private String drawdownTermMonths;
    private String drawdownTermYears;
    private JSONArray customerDetails;
    private JSONArray payInDetails;
    private JSONArray payOutDetails;	
  
	
	@JsonAlias(PARAM_CREATED_DATE)
    private String createdDate;
    @JsonAlias({PARAM_RECORD_ID})
    private String drawdownRequestId;
    @JsonAlias({PARAM_UPDATED_DATE})
    private String updatedDate;
    private String dbpErrCode;
    private String dbpErrMsg;
    
    
   
    
	public JSONArray getCustomerDetails() {
		return customerDetails;
	}
	public void setCustomerDetails(JSONArray customerDetails) {
		this.customerDetails = customerDetails;
	}
	public JSONArray getPayInDetails() {
		return payInDetails;
	}
	public void setPayInDetails(JSONArray payInDetails) {
		this.payInDetails = payInDetails;
	}
	public JSONArray getPayOutDetails() {
		return payOutDetails;
	}
	public void setPayOutDetails(JSONArray payOutDetails) {
		this.payOutDetails = payOutDetails;
	}
	public String getFacilityId() {
		return facilityId;
	}
	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getLoanAmount() {
		return loanAmount;
	}
	public void setLoanAmount(String loanAmount) {
		this.loanAmount = loanAmount;
	}
	
	public String getCreatedDate() {
		return createdDate;
	}
	
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getDrawdownRequestId() {
		return drawdownRequestId;
	}
	public void setDrawdownRequestId(String drawdownRequestId) {
		this.drawdownRequestId = drawdownRequestId;
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
	
	public String getLoanProduct() {
		return loanProduct;
	}
	public void setLoanProduct(String loanProduct) {
		this.loanProduct = loanProduct;
	}
	public String getDrawdownTermDays() {
		return drawdownTermDays;
	}
	public void setDrawdownTermDays(String drawdownTermDays) {
		this.drawdownTermDays = drawdownTermDays;
	}
	public String getDrawdownTermMonths() {
		return drawdownTermMonths;
	}
	public void setDrawdownTermMonths(String drawdownTermMonths) {
		this.drawdownTermMonths = drawdownTermMonths;
	}
	public String getDrawdownTermYears() {
		return drawdownTermYears;
	}
	public void setDrawdownTermYears(String drawdownTermYears) {
		this.drawdownTermYears = drawdownTermYears;
	}
	
}
