package com.temenos.dbx.product.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.DBPDTO;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.temenos.dbx.product.api.DBPDTOEXT;
import com.temenos.dbx.product.utils.DTOUtils;

public class CustomerAddressDTO implements DBPDTOEXT {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3802881561763297334L;
	private String customer_id;
	private String address_id;
	private String type_id;
	private Boolean isPrimary;
	private String durationOfStay;
	private String homeOwnership;
	private String createdby;
	private String modifiedby;
	private String createdts;
	private String lastmodifiedts;
	private String synctimestamp;
	private Boolean softdeleteflag;
	private AddressDTO addressDTO;
	private boolean isNew;
	private boolean isChanged;
	private boolean isdeleted;
	private String isTypeBusiness;
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
	 * @return the address_id
	 */
	public String getAddress_id() {
		return address_id;
	}
	/**
	 * @param address_id the address_id to set
	 */
	public void setAddress_id(String address_id) {
		this.address_id = address_id;
	}
	
	public String getCompanyLegalUnit() {
        return companyLegalUnit;
    }

    public void setCompanyLegalUnit(String companyLegalUnit) {
        this.companyLegalUnit = companyLegalUnit;
    }
	/**
	 * @return the type_id
	 */
	public String getType_id() {
		return type_id;
	}
	/**
	 * @param type_id the type_id to set
	 */
	public void setType_id(String type_id) {
		this.type_id = type_id;
	}
	/**
	 * @return the isPrimary
	 */
	public Boolean getIsPrimary() {
		return isPrimary;
	}
	/**
	 * @param isPrimary the isPrimary to set
	 */
	public void setIsPrimary(Boolean isPrimary) {
		this.isPrimary = isPrimary;
	}
	/**
	 * @return the durationOfStay
	 */
	public String getDurationOfStay() {
		return durationOfStay;
	}
	/**
	 * @param durationOfStay the durationOfStay to set
	 */
	public void setDurationOfStay(String durationOfStay) {
		this.durationOfStay = durationOfStay;
	}
	/**
	 * @return the homeOwnership
	 */
	public String getHomeOwnership() {
		return homeOwnership;
	}
	/**
	 * @param homeOwnership the homeOwnership to set
	 */
	public void setHomeOwnership(String homeOwnership) {
		this.homeOwnership = homeOwnership;
	}
	/**
	 * @return the createdby
	 */
	public String getCreatedby() {
		return createdby;
	}
	/**
	 * @param createdby the createdby to set
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
	 * @param modifiedby the modifiedby to set
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
	 * @return the synctimestamp
	 */
	public String getSynctimestamp() {
		return synctimestamp;
	}
	/**
	 * @param synctimestamp the synctimestamp to set
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
	 * @param softdeleteflag the softdeleteflag to set
	 */
	public void setSoftdeleteflag(Boolean softdeleteflag) {
		this.softdeleteflag = softdeleteflag;
	}
	/**
	 * @return the addressDTO
	 */
	public AddressDTO getAddressDTO() {
		return addressDTO;
	}
	/**
	 * @param addressDTO the addressDTO to set
	 */
	public void setAddressDTO(AddressDTO addressDTO) {
		this.addressDTO = addressDTO;
	}

	@Override
	public boolean persist(Map<String, Object> input, Map<String, Object> headers) {
		if(!isNew && isChanged){
			return !ServiceCallHelper.invokeServiceAndGetJson(input, headers, URLConstants.CUSTOMER_ADDRESS_UPDATE).has("errmsg");
		}

		if(isNew){
			return !ServiceCallHelper.invokeServiceAndGetJson(input, headers, URLConstants.CUSTOMER_ADDRESS_CREATE).has("errmsg");
		}

		if(isdeleted) {
			if(!ServiceCallHelper.invokeServiceAndGetJson(input, headers, URLConstants.CUSTOMER_ADDRESS_DELETE).has("errmsg")) {
				input = new HashMap<String, Object>();
				input.put("id", address_id);
				addressDTO.persist(input, headers);
			}
		}

		return true;
	}

	@Override
    public Object loadDTO(String id) {
        String filter = "Customer_id"+ DBPUtilitiesConstants.EQUAL + id;
        List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();

        DTOUtils.loadDTOListfromDB(exts, filter, URLConstants.CUSTOMER_ADDRESS_GET, true, true);

        if(exts != null && exts.size() >0) {
            for(int i=0; i<exts.size(); i++) {
                CustomerAddressDTO customerAddressDTO = (CustomerAddressDTO)exts.get(i);
                customerAddressDTO.setAddressDTO((AddressDTO)(new AddressDTO().loadDTO(customerAddressDTO.getAddress_id())));
            }
        }

        return exts;
    }
	
	/**
	 * @return the isdeleted
	 */
	public boolean isIsdeleted() {
		return isdeleted;
	}


	/**
	 * @param isdeleted the isdeleted to set
	 */
	public void setIsdeleted(boolean isdeleted) {
		this.isdeleted = isdeleted;
	}


	@Override
	public Object loadDTO() {

		List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();
		
		List<CustomerAddressDTO> customerAddressDTOs = new ArrayList<CustomerAddressDTO>();
		
		if(StringUtils.isNotBlank(customer_id)) {
			String filter = "Customer_id"+ DBPUtilitiesConstants.EQUAL + customer_id;
			DTOUtils.loadDTOListfromDB(exts, filter, URLConstants.CUSTOMER_ADDRESS_GET, true, true);
			if(exts != null && exts.size() >0) {
				for(int i=0; i<exts.size(); i++) {
					CustomerAddressDTO customerAddressDTO = (CustomerAddressDTO)exts.get(i);
					AddressDTO addressDTO = new AddressDTO();
					addressDTO.setId(customerAddressDTO.getAddress_id());
					customerAddressDTO.setAddressDTO((AddressDTO)(addressDTO.loadDTO()));
					customerAddressDTOs.add(customerAddressDTO);
				}
			}
		}

		return customerAddressDTOs;
	}


	public String getIsTypeBusiness() {
		return isTypeBusiness;
	}


	public void setIsTypeBusiness(String isTypeBusiness) {
		this.isTypeBusiness = isTypeBusiness;
	}

}
