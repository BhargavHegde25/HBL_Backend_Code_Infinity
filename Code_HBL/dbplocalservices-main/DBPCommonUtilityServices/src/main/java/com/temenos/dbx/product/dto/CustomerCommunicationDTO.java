package com.temenos.dbx.product.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.dbx.product.api.DBPDTOEXT;
import com.temenos.dbx.product.utils.DTOUtils;

public class CustomerCommunicationDTO implements DBPDTOEXT {

    /**
     * 
     */
    private static final long serialVersionUID = 8034633732574011187L;
    private String id;
    private String type_id;
    private String customer_id;
    private Boolean isPrimary;
    private String value;
    private String extension;
    private String description;
    private String isTypeBusiness;
    private Boolean isPreferredContactMethod;
    private String preferredContactTime;
    private String createdby;
    private String modifiedby;
    private String createdts;
    private String lastmodifiedts;
    private String synctimestamp;
    private Boolean softdeleteflag;
    private String type;
    private String countryType;
    private String receivePromotions;
    private String phoneCountryCode;
    private boolean isNew;
    private boolean isChanged;
    private boolean isdeleted;
    private boolean isAlertsRequired;
    private String companyLegalUnit;

    /**
     * @param isNew
     *            the isNew to set
     */
    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    /**
     * @return the isChanged
     */
    public Boolean getIsChanged() {
        return isChanged;
    }
    
    public String getCompanyLegalUnit() {
        return companyLegalUnit;
    }

    public void setCompanyLegalUnit(String companyLegalUnit) {
        this.companyLegalUnit = companyLegalUnit;
    }

    /**
     * @param isChanged
     *            the isChanged to set
     */
    public void setIsChanged(Boolean isChanged) {
        this.isChanged = isChanged;
    }

    /**
     * @return the isNew
     */
    public Boolean getIsNew() {
        return isNew;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the type_id
     */
    public String getType_id() {
        return type_id;
    }

    /**
     * @param type_id
     *            the type_id to set
     */
    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    /**
     * @return the customer_id
     */
    public String getCustomer_id() {
        return customer_id;
    }

    /**
     * @param customer_id
     *            the customer_id to set
     */
    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    /**
     * @return the isPrimary
     */
    public Boolean getIsPrimary() {
        return isPrimary;
    }

    /**
     * @param isPrimary
     *            the isPrimary to set
     */
    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the extension
     */
    public String getExtension() {
        return extension;
    }

    /**
     * @param extension
     *            the extension to set
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the isPreferredContactMethod
     */
    public Boolean getIsPreferredContactMethod() {
        return isPreferredContactMethod;
    }

    /**
     * @param isPreferredContactMethod
     *            the isPreferredContactMethod to set
     */
    public void setIsPreferredContactMethod(Boolean isPreferredContactMethod) {
        this.isPreferredContactMethod = isPreferredContactMethod;
    }

    /**
     * @return the preferredContactTime
     */
    public String getPreferredContactTime() {
        return preferredContactTime;
    }

    /**
     * @param preferredContactTime
     *            the preferredContactTime to set
     */
    public void setPreferredContactTime(String preferredContactTime) {
        this.preferredContactTime = preferredContactTime;
    }

    /**
     * @return the createdby
     */
    public String getCreatedby() {
        return createdby;
    }

    /**
     * @param createdby
     *            the createdby to set
     */
    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    /**
     * @return the modifiedby
     */
    public String getModifiedby() {
        return modifiedby;
    }

    /**
     * @param modifiedby
     *            the modifiedby to set
     */
    public void setModifiedby(String modifiedby) {
        this.modifiedby = modifiedby;
    }

    /**
     * @return the createdts
     */
    public String getCreatedts() {
        return createdts;
    }

    /**
     * @param createdts
     *            the createdts to set
     */
    public void setCreatedts(String createdts) {
        this.createdts = createdts;
    }

    /**
     * @return the lastmodifiedts
     */
    public String getLastmodifiedts() {
        return lastmodifiedts;
    }

    /**
     * @param lastmodifiedts
     *            the lastmodifiedts to set
     */
    public void setLastmodifiedts(String lastmodifiedts) {
        this.lastmodifiedts = lastmodifiedts;
    }

    /**
     * @return the synctimestamp
     */
    public String getSynctimestamp() {
        return synctimestamp;
    }

    /**
     * @param synctimestamp
     *            the synctimestamp to set
     */
    public void setSynctimestamp(String synctimestamp) {
        this.synctimestamp = synctimestamp;
    }

    /**
     * @return the softdeleteflag
     */
    public Boolean getSoftdeleteflag() {
        return softdeleteflag;
    }

    /**
     * @param softdeleteflag
     *            the softdeleteflag to set
     */
    public void setSoftdeleteflag(Boolean softdeleteflag) {
        this.softdeleteflag = softdeleteflag;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the countryType
     */
    public String getCountryType() {
        return countryType;
    }

    /**
     * @param countryType
     *            the countryType to set
     */
    public void setCountryType(String countryType) {
        this.countryType = countryType;
    }

    /**
     * @return the receivePromotions
     */
    public String getReceivePromotions() {
        return receivePromotions;
    }

    /**
     * @param receivePromotions
     *            the receivePromotions to set
     */
    public void setReceivePromotions(String receivePromotions) {
        this.receivePromotions = receivePromotions;
    }

    /**
     * @return the phoneCountryCode
     */
    public String getPhoneCountryCode() {
        return phoneCountryCode;
    }

    /**
     * @param phoneCountryCode
     *            the phoneCountryCode to set
     */
    public void setPhoneCountryCode(String phoneCountryCode) {
        this.phoneCountryCode = phoneCountryCode;
    }

    public String getIsTypeBusiness() {
        return isTypeBusiness;
    }

    public void setIsTypeBusiness(String isTypeBusiness) {
        this.isTypeBusiness = isTypeBusiness;
    }

    @Override
    public boolean persist(Map<String, Object> input, Map<String, Object> headers) {
        if (!isNew && isChanged) {
            return !ServiceCallHelper.invokeServiceAndGetJson(input, headers, URLConstants.CUSTOMERCOMMUNICATION_UPDATE)
                    .has("errmsg");
        }

        if (isNew) {
            return !ServiceCallHelper.invokeServiceAndGetJson(input, headers, URLConstants.CUSTOMERCOMMUNICATION_CREATE)
                    .has("errmsg");
        }

        if (isdeleted) {
            return !ServiceCallHelper.invokeServiceAndGetJson(input, headers, URLConstants.CUSTOMERCOMMUNICATION_DELETE)
                    .has("errmsg");
        }

        return true;
    }

    @Override
    public Object loadDTO(String id) {
        String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + id;
        List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();

        DTOUtils.loadDTOListfromDB(exts, filter, URLConstants.CUSTOMERCOMMUNICATION_GET, true, true);

        return exts;
    }

    /**
     * @return the isdeleted
     */
    public boolean isIsdeleted() {
        return isdeleted;
    }

    /**
     * @param isdeleted
     *            the isdeleted to set
     */
    public void setIsdeleted(boolean isdeleted) {
        this.isdeleted = isdeleted;
    }

    @Override
    public Object loadDTO() {

        List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();
        if (StringUtils.isNotBlank(customer_id)) {
            String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customer_id;
            DTOUtils.loadDTOListfromDB(exts, filter, URLConstants.CUSTOMERCOMMUNICATION_GET, true, true);
        }

        return exts;
    }

    public boolean getIsAlertsRequired() {
        return isAlertsRequired;
    }

    public void setIsAlertsRequired(boolean isAlertsRequired) {
        this.isAlertsRequired = isAlertsRequired;
    }

}
