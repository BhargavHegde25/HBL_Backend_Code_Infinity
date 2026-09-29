package com.temenos.dbx.product.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.dbx.product.api.DBPDTOEXT;
import com.temenos.dbx.product.utils.DTOUtils;
import com.temenos.dbx.product.utils.InfinityConstants;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractCustomersDTO implements DBPDTOEXT {
    /**
     * 
     */
    private static final long serialVersionUID = 75825807604093713L;

    private String id;
    private String contractId;
    private String customerId;
    private String coreCustomerId;
    private boolean isAdmin;
    private boolean isOwner;
    private boolean isPrimary;
    private boolean isAuthSignatory;
    private boolean autoSyncAccounts;
    private String createdby;
    private String modifiedby;
    private String createdts;
    private String lastmodifiedts;
    private String synctimestamp;
    private String softdeleteflag;
    private String companyLegalUnit;
    private String FavouriteStatus;
    
    

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

    public String getSynctimestamp() {
        return synctimestamp;
    }

    public void setSynctimestamp(String synctimestamp) {
        this.synctimestamp = synctimestamp;
    }

    public String getSoftdeleteflag() {
        return softdeleteflag;
    }

    public void setSoftdeleteflag(String softdeleteflag) {
        this.softdeleteflag = softdeleteflag;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
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
     * @return the contractId
     */
    public String getContractId() {
        return contractId;
    }

    /**
     * @param contractId
     *            the contractId to set
     */
    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    /**
     * @return the customerId
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId
     *            the customerId to set
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the coreCustomerId
     */
    public String getCoreCustomerId() {
        return coreCustomerId;
    }
    /**
     * @return the companyLegalUnit
     */
    public String getCompanyLegalUnit() {
        return companyLegalUnit;
    }

    /**
     * @param coreCustomerId
     *            the coreCustomerId to set
     */
    public void setCoreCustomerId(String coreCustomerId) {
        this.coreCustomerId = coreCustomerId;
    }

    /**
     * @return the isAdmin
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * @param isAdmin the isAdmin to set
     */
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    /**
     * @return the isOwner
     */
    public boolean isOwner() {
        return isOwner;
    }

    /**
     * @param isOwner the isOwner to set
     */
    public void setOwner(boolean isOwner) {
        this.isOwner = isOwner;
    }

    /**
     * @return the isPrimary
     */
    public boolean isPrimary() {
        return isPrimary;
    }

    /**
     * @param isPrimary the isPrimary to set
     */
    public void setPrimary(boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    /**
     * @return the isAuthSignatory
     */
    public boolean isAuthSignatory() {
        return isAuthSignatory;
    }

    /**
     * @param isAuthSignatory the isAuthSignatory to set
     */
    public void setAuthSignatory(boolean isAuthSignatory) {
        this.isAuthSignatory = isAuthSignatory;
    }

    /**
     * @return the autoSyncAccounts
     */
    public boolean isAutoSyncAccounts() {
        return autoSyncAccounts;
    }

    /**
     * @param autosyncaccounts the autoSyncAccounts to set
     */
    public void setAutoSyncAccounts(boolean autoSyncAccounts) {
        this.autoSyncAccounts = autoSyncAccounts;
    }
    /**
     * @param companyLegalUnit
     *            the companyLegalUnit to set
     */
    public void setCompanyLegalUnit(String companyLegalUnit) {
        this.companyLegalUnit = companyLegalUnit;
    }
    
    public String getFavouriteStatus() {
		return FavouriteStatus;
	}

	public void setFavouriteStatus(String favouriteStatus) {
		FavouriteStatus = favouriteStatus;
	}
	
    @Override
    public boolean persist(Map<String, Object> input, Map<String, Object> headers) {
        return !ServiceCallHelper.invokeServiceAndGetJson(input, headers, URLConstants.CONTRACT_CUSTOMERS_CREATE)
                .has("errmsg");
    }

    @Override
    public Object loadDTO(String id) {
        String filter = "id" + DBPUtilitiesConstants.EQUAL + id;
        List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();

        DTOUtils.loadDTOListfromDB(exts, filter, URLConstants.CONTRACT_CUSTOMERS_GET, true, true);

        if (exts != null && exts.size() > 0) {
            return exts.get(0);
        }

        return null;
    }

    @Override
    public Object loadDTO() {

        List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();
        String filter = "";
        if (StringUtils.isNotBlank(id)) {
            filter = "id" + DBPUtilitiesConstants.EQUAL + id;

        } else if (StringUtils.isNotBlank(contractId)) {
            filter = InfinityConstants.contractId + DBPUtilitiesConstants.EQUAL + contractId;
        } else if (StringUtils.isNotBlank(customerId)) {
            filter = InfinityConstants.customerId + DBPUtilitiesConstants.EQUAL + customerId;
        } else if (StringUtils.isNotBlank(coreCustomerId)) {
            filter = InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL + customerId;
        }

        DTOUtils.loadDTOListfromDB(exts, filter, URLConstants.CONTRACT_CUSTOMERS_GET, true, true);

        return exts;
    }

}