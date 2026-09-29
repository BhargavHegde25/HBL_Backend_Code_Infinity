/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.json.JSONArray;

import static com.temenos.infinity.tradesupplyfinance.constants.TradeSupplyFinanceConstants.*;

/**
 * @author k.meiyazhagan
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class AnchorInvoiceDTO implements DBPDTO {
    private String billReference;
    private String billType;
    private String buyerId;
    private String buyerName;
    @JsonAlias(PARAM_CREATED_DATE)
    private String createdDate;
    private String invoiceAmount;
    private String invoiceCurrency;
    private String invoiceDocuments;
    @JsonAlias({PARAM_SRMSID, PARAM_RECORD_ID})
    private String invoiceReference;
    private String issueDate;
    private String maturityDate;
    private String role;
    private String status;
    private String supplierId;
    private String supplierName;
    @JsonAlias({PARAM_LASTUPDATEDTIMESTAMP, PARAM_UPDATED_DATE})
    private String updatedDate;
    private String uploadedFrom;
    private String dbpErrCode;
    private String dbpErrMsg;
    @JsonIgnore
    private String errorDetails;

    public String getBillReference() {
        return billReference;
    }

    public void setBillReference(String billReference) {
        this.billReference = billReference;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(String invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getInvoiceCurrency() {
        return invoiceCurrency;
    }

    public void setInvoiceCurrency(String invoiceCurrency) {
        this.invoiceCurrency = invoiceCurrency;
    }

    public String getInvoiceDocuments() {
        return invoiceDocuments;
    }

    public void setInvoiceDocuments(String invoiceDocuments) {
        this.invoiceDocuments = new JSONArray(invoiceDocuments).toString();
    }

    public String getInvoiceReference() {
        return invoiceReference;
    }

    public void setInvoiceReference(String invoiceReference) {
        this.invoiceReference = invoiceReference;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(String maturityDate) {
        this.maturityDate = maturityDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getUploadedFrom() {
        return uploadedFrom;
    }

    public void setUploadedFrom(String uploadedFrom) {
        this.uploadedFrom = uploadedFrom;
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

    public String getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }
}
