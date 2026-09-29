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

public class AddressDTO implements DBPDTOEXT {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2018206770665209854L;
	private String id;
	private String region_id;
	private String city_id;
	private String addressLine1;
	private String addressLine2;
	private String addressLine3;
	private String zipCode;
	private String latitude;
	private String logitude;
	private Boolean isPreferredAddress;
	private String cityName;
	private String user_id;
	private String country;
	private String type;
	private String state;
	private String createdby;
	private String modifiedby;
	private String createdts;
	private String lastmodifiedts;
	private String synctimestamp;
	private Boolean softdeleteflag;
	private boolean isNew;
	private boolean isChanged;
	private boolean isdeleted;
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
	/**
	 * @return the region_id
	 */
	public String getRegion_id() {
		return region_id;
	}
	/**
	 * @param region_id the region_id to set
	 */
	public void setRegion_id(String region_id) {
		this.region_id = region_id;
	}
	/**
	 * @return the city_id
	 */
	public String getCity_id() {
		return city_id;
	}
	/**
	 * @param city_id the city_id to set
	 */
	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}
	/**
	 * @return the addressLine1
	 */
	public String getAddressLine1() {
		return addressLine1;
	}
	/**
	 * @param addressLine1 the addressLine1 to set
	 */
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	/**
	 * @return the addressLine2
	 */
	public String getAddressLine2() {
		return addressLine2;
	}
	/**
	 * @param addressLine2 the addressLine2 to set
	 */
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}
	/**
	 * @return the addressLine3
	 */
	public String getAddressLine3() {
		return addressLine3;
	}
	/**
	 * @param addressLine3 the addressLine3 to set
	 */
	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
	}
	/**
	 * @return the zipCode
	 */
	public String getZipCode() {
		return zipCode;
	}
	/**
	 * @param zipCode the zipCode to set
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	/**
	 * @return the latitude
	 */
	public String getLatitude() {
		return latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	/**
	 * @return the logitude
	 */
	public String getLogitude() {
		return logitude;
	}
	/**
	 * @param logitude the logitude to set
	 */
	public void setLogitude(String logitude) {
		this.logitude = logitude;
	}
	/**
	 * @return the isPreferredAddress
	 */
	public Boolean getIsPreferredAddress() {
		return isPreferredAddress;
	}
	/**
	 * @param isPreferredAddress the isPreferredAddress to set
	 */
	public void setIsPreferredAddress(Boolean isPreferredAddress) {
		this.isPreferredAddress = isPreferredAddress;
	}
	/**
	 * @return the cityName
	 */
	public String getCityName() {
		return cityName;
	}
	/**
	 * @param cityName the cityName to set
	 */
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	/**
	 * @return the user_id
	 */
	public String getUser_id() {
		return user_id;
	}
	/**
	 * @param user_id the user_id to set
	 */
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
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
	
	public String getCompanyLegalUnit() {
        return companyLegalUnit;
    }

    public void setCompanyLegalUnit(String companyLegalUnit) {
        this.companyLegalUnit = companyLegalUnit;
    }

	@Override
	public boolean persist(Map<String, Object> input, Map<String, Object> headers) {
		if(!isNew && isChanged){
			return !ServiceCallHelper.invokeServiceAndGetJson(input, headers, URLConstants.ADDRESS_UPDATE).has("errmsg");
		}

		if(isNew){
			return !ServiceCallHelper.invokeServiceAndGetJson(input, headers, URLConstants.ADDRESS_CREATE).has("errmsg");
		}

		//        if(isdeleted) {
		//            return !ServiceCallHelper.invokeServiceAndGetJson(input, headers, URLConstants.ADDRESS_DELETE).has("errmsg");
		//        }
		return true;
	}

	 @Override
	    public Object loadDTO(String id) {
	        String filter = "id"+ DBPUtilitiesConstants.EQUAL + id;
	        List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();

	        DTOUtils.loadDTOListfromDB(exts, filter, URLConstants.ADDRESS_GET, true, true);

	        if(exts != null && exts.size() >0) {
	            return exts.get(0);
	        }

	        return null;
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
		if(StringUtils.isNotBlank(id)) {
			String filter = "id"+ DBPUtilitiesConstants.EQUAL + id;
			List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();

			DTOUtils.loadDTOListfromDB(exts, filter, URLConstants.ADDRESS_GET, true, true);

			if(exts != null && exts.size() >0) {
				return exts.get(0);
			}
		}

		return null;
	}

}
