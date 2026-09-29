package com.temenos.dbx.mfa.dto;

import com.dbp.core.api.DBPDTO;

public class MFAServiceDTO implements DBPDTO {

    /**
     * 
     */
    private static final long serialVersionUID = 3251495421428336287L;
    private String serviceKey;
    private String serviceName;
    private String user_id;
    private String createddts;
    private String retryCount;
    private String payload;
    private String securityQuestions;
    private String isVerified;

    /**
     * @return the serviceKey
     */
    public String getServiceKey() {
        return serviceKey;
    }

    /**
     * @param serviceKey
     *            the serviceKey to set
     */
    public void setServiceKey(String serviceKey) {
        this.serviceKey = serviceKey;
    }

    /**
     * @return the serviceName
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * @param serviceName
     *            the serviceName to set
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * @return the user_
     */
    public String getUser_() {
        return user_id;
    }

    /**
     * @param user_
     *            the user_ to set
     */
    public void setUser_(String user_id) {
        this.user_id = user_id;
    }

    /**
     * @return the createddts
     */
    public String getCreateddts() {
        return createddts;
    }

    /**
     * @param createddts
     *            the createddts to set
     */
    public void setCreateddts(String createddts) {
        this.createddts = createddts;
    }

    /**
     * @return the retryCount
     */
    public String getRetryCount() {
        return retryCount;
    }

    /**
     * @param retryCount
     *            the retryCount to set
     */
    public void setRetryCount(String retryCount) {
        this.retryCount = retryCount;
    }

    /**
     * @return the payload
     */
    public String getPayload() {
        return payload;
    }

    /**
     * @param payload
     *            the payload to set
     */
    public void setPayload(String payload) {
        this.payload = payload;
    }

    /**
     * @return the securityQuestions
     */
    public String getSecurityQuestions() {
        return securityQuestions;
    }

    /**
     * @param securityQuestions
     *            the securityQuestions to set
     */
    public void setSecurityQuestions(String securityQuestions) {
        this.securityQuestions = securityQuestions;
    }

    /**
     * @return the isVerified
     */
    public String getIsVerified() {
        return isVerified;
    }

    /**
     * @param isVerified
     *            the isVerified to set
     */
    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }

}
