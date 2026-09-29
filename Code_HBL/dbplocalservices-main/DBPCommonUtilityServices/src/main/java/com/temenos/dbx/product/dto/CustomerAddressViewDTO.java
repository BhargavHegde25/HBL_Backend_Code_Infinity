package com.temenos.dbx.product.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.dbx.product.api.DBPDTOEXT;
import com.temenos.dbx.product.utils.DTOUtils;

public class CustomerAddressViewDTO implements DBPDTOEXT {

    /**
     * 
     */
    private static final long serialVersionUID = -8093811896904344630L;
    private String customerId;
    private String addressId;
    private String addressType;
    private String addressLine1;
    private String addressLine2;
    private String zipCode;
    private String cityName;
    private String cityId;
    private String regionName;
    private String regionId;
    private String regionCode;
    private String countryName;
    private String countryId;
    private String countryCode;
    private String isPrimary;
    private String isTypeBusiness;
    private String companyLegalUnit;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(String isPrimary) {
        this.isPrimary = isPrimary;
    }

	@Override
	public boolean persist(Map<String, Object> input, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object loadDTO(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object loadDTO() {
		List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();
        if (StringUtils.isNotBlank(customerId)) {
            String filter = "CustomerId" + DBPUtilitiesConstants.EQUAL + customerId;
            DTOUtils.loadDTOListfromDB(exts, filter, URLConstants.CUSTOMER_ADDRESS_VIEW_GET, true, true);
        }

        return exts;
	}

	public String getIsTypeBusiness() {
		return isTypeBusiness;
	}

	public void setIsTypeBusiness(String isTypeBusiness) {
		this.isTypeBusiness = isTypeBusiness;
	}

	public String getCompanyLegalUnit() {
		return companyLegalUnit;
	}

	public void setCompanyLegalUnit(String companyLegalUnit) {
		this.companyLegalUnit = companyLegalUnit;
	}

}
