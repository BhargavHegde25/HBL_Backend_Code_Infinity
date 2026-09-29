/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.temenos.infinity.tradesupplyfinance.constants.TradeSupplyFinanceConstants.*;

/**
 * @author k.meiyazhagan
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class AnchorFundingRequestDTO implements DBPDTO {
    private String buyerId;
    @JsonAlias(PARAM_CREATED_DATE)
    private String createdDate;
    private String currency;
    private String facilityAvailableLimit;
    private String facilityCurrency;
    private String facilityId;
    private String facilityUtilisedLimit;
    private String financingDate;
    private String fundingDocuments;
    private String fundingRequestAmount;
    @JsonAlias({PARAM_SRMSID, PARAM_RECORD_ID})
    private String fundingRequestId;
    private String invoiceReferences;
    private String productId;
    private String productName;
    private String programName;
    private String status;
    private String supplierId;
    @JsonAlias({PARAM_LASTUPDATEDTIMESTAMP, PARAM_UPDATED_DATE})
    private String updatedDate;
    private String dbpErrCode;
    private String dbpErrMsg;

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getFacilityAvailableLimit() {
        return facilityAvailableLimit;
    }

    public void setFacilityAvailableLimit(String facilityAvailableLimit) {
        this.facilityAvailableLimit = facilityAvailableLimit;
    }

    public String getFacilityCurrency() {
        return facilityCurrency;
    }

    public void setFacilityCurrency(String facilityCurrency) {
        this.facilityCurrency = facilityCurrency;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String getFacilityUtilisedLimit() {
        return facilityUtilisedLimit;
    }

    public void setFacilityUtilisedLimit(String facilityUtilisedLimit) {
        this.facilityUtilisedLimit = facilityUtilisedLimit;
    }

    public String getFinancingDate() {
        return financingDate;
    }

    public void setFinancingDate(String financingDate) {
        this.financingDate = financingDate;
    }

    public String getFundingDocuments() {
        return fundingDocuments;
    }

    public void setFundingDocuments(String fundingDocuments) {
        this.fundingDocuments = new JSONObject(fundingDocuments).toString();
    }

    public String getFundingRequestAmount() {
        return fundingRequestAmount;
    }

    public void setFundingRequestAmount(String fundingRequestAmount) {
        this.fundingRequestAmount = fundingRequestAmount;
    }

    public String getFundingRequestId() {
        return fundingRequestId;
    }

    public void setFundingRequestId(String fundingRequestId) {
        this.fundingRequestId = fundingRequestId;
    }

    public String getInvoiceReferences() {
        return invoiceReferences;
    }

    public void setInvoiceReferences(String invoiceReferences) {
        this.invoiceReferences = new JSONArray(invoiceReferences).toString();
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
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