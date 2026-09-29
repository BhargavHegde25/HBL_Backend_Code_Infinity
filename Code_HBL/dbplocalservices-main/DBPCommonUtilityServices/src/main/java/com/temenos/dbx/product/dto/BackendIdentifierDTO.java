package com.temenos.dbx.product.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.dbx.product.api.DBPDTOEXT;
import com.temenos.dbx.product.utils.DTOConstants;
import com.temenos.dbx.product.utils.DTOUtils;

public class BackendIdentifierDTO implements DBPDTOEXT {


	/**
	 * 
	 */
	private static final long serialVersionUID = 908651236189166105L;
	private String id;
	private String customer_id;
	private String sequenceNumber;
	private String backendId;
	private String backendType;
	private String identifier_name;
	private String createdts;
	private String lastmodifiedts;
	private String contractId;
	private boolean isNew;
	private boolean isChanged;
	private String companyId;
	private String contractTypeId;
	private String companyLegalUnit;
	



	/**
	 * @return the isChanged
	 */
	public Boolean getIsChanged() {
		return isChanged;
	}


	/**
	 * @param isChanged the isChanged to set
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
	 * @param isNew the isNew to set
	 */
	public void setIsNew(Boolean isNew) {
		this.isNew = isNew;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	public String getCompanyLegalUnit() {
        return companyLegalUnit;
    }

    public void setCompanyLegalUnit(String companyLegalUnit) {
        this.companyLegalUnit = companyLegalUnit;
    }
	/**
	 * @return the customer_id
	 */
	public String getCustomer_id() {
		return customer_id;
	}
	/**
	 * @param customer_id the customer_id to set
	 */
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
	/**
	 * @return the sequenceNumber
	 */
	public String getSequenceNumber() {
		return sequenceNumber;
	}
	/**
	 * @param sequenceNumber the sequenceNumber to set
	 */
	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	/**
	 * @return the backendId
	 */
	public String getBackendId() {
		return backendId;
	}
	/**
	 * @param backendId the backendId to set
	 */
	public void setBackendId(String backendId) {
		this.backendId = backendId;
	}
	/**
	 * @return the backendType
	 */
	public String getBackendType() {
		return backendType;
	}
	/**
	 * @param backendType the backendType to set
	 */
	public void setBackendType(String backendType) {
		this.backendType = backendType;
	}
	/**
	 * @return the identifier_name
	 */
	public String getIdentifier_name() {
		return identifier_name;
	}
	/**
	 * @param identifier_name the identifier_name to set
	 */
	public void setIdentifier_name(String identifier_name) {
		this.identifier_name = identifier_name;
	}
	/**
	 * @return the createdts
	 */
	public String getCreatedts() {
		return createdts;
	}
	/**
	 * @param createdts the createdts to set
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
	 * @param lastmodifiedts the lastmodifiedts to set
	 */
	public void setLastmodifiedts(String lastmodifiedts) {
		this.lastmodifiedts = lastmodifiedts;
	}
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getCompanyId() {
		return companyId;
	}


	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}


	@Override
	public boolean persist(Map<String, Object> input, Map<String, Object> headers) {
		if(!isNew && isChanged){
			return !ServiceCallHelper.invokeServiceAndGetJson(input, headers, URLConstants.BACKENDIDENTIFIER_CREATE).has("errmsg");
		}

		if(isNew){
			return !ServiceCallHelper.invokeServiceAndGetJson(input, headers, URLConstants.BACKENDIDENTIFIER_CREATE).has("errmsg");
		}

		return true;
	}

	@Override
    public Object loadDTO(String id) {
        String filter = "Customer_id"+ DBPUtilitiesConstants.EQUAL + id;
        List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();

        DTOUtils.loadDTOListfromDB(exts, filter, URLConstants.BACKENDIDENTIFIER_GET, true, true);

        return null;
    }

	@Override
	public Object loadDTO() {
	    String filter = "";
	    
		if(StringUtils.isNotBlank(customer_id)) {
			filter = "Customer_id"+ DBPUtilitiesConstants.EQUAL + customer_id;
		}
		if(StringUtils.isNotBlank(backendType)) {
		    if(StringUtils.isNotBlank(filter)) {
		        filter += DBPUtilitiesConstants.AND;
		    }
            filter += DTOConstants.BACKENDTYPE+ DBPUtilitiesConstants.EQUAL + backendType;
        }
		if(StringUtils.isNotBlank(backendId)) {
		    if(StringUtils.isNotBlank(filter)) {
                filter += DBPUtilitiesConstants.AND;
            }
            filter += DTOConstants.BACKENDID+ DBPUtilitiesConstants.EQUAL + backendId;
        }
		if(StringUtils.isNotBlank(companyLegalUnit)) {
		    if(StringUtils.isNotBlank(filter)) {
                filter += DBPUtilitiesConstants.AND;
            }
            filter += DTOConstants.LEGALENTITYID+ DBPUtilitiesConstants.EQUAL + companyLegalUnit;
        }
		
		if(StringUtils.isNotBlank(filter)) {
			List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();
			DTOUtils.loadDTOListfromDB(exts, filter, URLConstants.BACKENDIDENTIFIER_GET, true, true);

			if(exts != null && exts.size() >0 ) {
				return exts.get(exts.size()-1);
			}
		}

		return null;
	}


    /**
     * @return the contractId
     */
    public String getContractId() {
        return contractId;
    }


    /**
     * @param contractId the contractId to set
     */
    public void setContractId(String contractId) {
        this.contractId = contractId;
    }


    /**
     * @return the contractTypeId
     */
    public String getContractTypeId() {
        return contractTypeId;
    }


    /**
     * @param contractTypeId the contractTypeId to set
     */
    public void setContractTypeId(String contractTypeId) {
        this.contractTypeId = contractTypeId;
    }

}
