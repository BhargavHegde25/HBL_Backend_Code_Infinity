package com.temenos.dbx.product.dto;

import com.dbp.core.api.DBPDTO;

public class CustomerApplicationDTO implements DBPDTO {

    /**
     * 
     */
    private static final long serialVersionUID = 6446610505298636156L;

    private String Customer_id;
    private String Party_id;
    private String CoreCustomer_id;
    private String ApplicationId;
    private String ProductId;
    private String ApplicationStatus;
    private String createdby;
    private String modifiedby;
    private String createdts;
    private String lastmodifiedts;
    private String softdeleteflag;
    private String RequestKey;
    private String JSESSIONID;
    private String SaveChallengeAnswer;

    public String getPartyId() {
        return Party_id;
    }

    public void setPartyId(String partyId) {
        Party_id = partyId;
    }

    public String getCoreCustomerId() {
        return CoreCustomer_id;
    }

    public void setCoreCustomerId(String coreCustomerId) {
        CoreCustomer_id = coreCustomerId;
    }

    public String getCustomerId() {
        return Customer_id;
    }

    public void setCustomerId(String customerId) {
        Customer_id = customerId;
    }

    public String getApplicationId() {
        return ApplicationId;
    }

    public void setApplicationId(String applicationId) {
        ApplicationId = applicationId;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getApplicationStatus() {
        return ApplicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        ApplicationStatus = applicationStatus;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public String getModifiedby() {
        return modifiedby;
    }

    public void setModifiedby(String modifiedby) {
        this.modifiedby = modifiedby;
    }

    public String getCreatedts() {
        return createdts;
    }

    public void setCreatedts(String createdts) {
        this.createdts = createdts;
    }

    public String getLastmodifiedts() {
        return lastmodifiedts;
    }

    public void setLastmodifiedts(String lastmodifiedts) {
        this.lastmodifiedts = lastmodifiedts;
    }

    public String getSoftdeleteflag() {
        return softdeleteflag;
    }

    public void setSoftdeleteflag(String softdeleteflag) {
        this.softdeleteflag = softdeleteflag;
    }

    public String getRequestKey() {
        return RequestKey;
    }

    public void setRequestKey(String requestKey) {
        RequestKey = requestKey;
    }

    public String getJSESSIONID() {
        return JSESSIONID;
    }

    public void setJSESSIONID(String jSESSIONID) {
        JSESSIONID = jSESSIONID;
    }

    public String getSaveChallengeAnswer() {
        return SaveChallengeAnswer;
    }

    public void setSaveChallengeAnswer(String saveChallengeAnswer) {
        SaveChallengeAnswer = saveChallengeAnswer;
    }

}
