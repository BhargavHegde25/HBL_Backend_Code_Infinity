package com.temenos.dbx.product.dto;

import java.util.ArrayList;
import java.util.List;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.temenos.dbx.product.utils.DTOUtils;

@JsonInclude(Include.NON_NULL)
public class PartyAddress implements DBPDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3228463059202106456L;
	
	private String addressesReference;
	private String partyId;
	private String communicationNature;
	private String communicationType;
	private String addressType;
	private String electronicAddress;
	private String iddPrefixPhone;
	private String phoneNo;
	private String countryCode;
	private String flatNumber;
	private String buildingNumber;
    private String buildingName;
	private String floor;
	private String streetName;
	private String town;
	private String regionCode;
	private String postalOrZipCode;
    private String postBoxNumber;
    private String countrySubdivision;
    private String district;
    private String department;
    private String subDepartment;
    private String validatedBy;
    private String usePurpose;
    private String landmark;
    private String website;
    private Boolean isChanged;
    private String primary;
	private String legalEntityId;
    
	/**
     * @return the addressesReference
     */
    public String getAddressesReference() {
        return addressesReference;
    }




    /**
     * @param addressesReference the addressesReference to set
     */
    public void setAddressesReference(String addressesReference) {
        this.addressesReference = addressesReference;
    }




    /**
     * @return the partyId
     */
    public String getPartyId() {
        return partyId;
    }




    /**
     * @param partyId the partyId to set
     */
    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }




    /**
     * @return the communicationNature
     */
    public String getCommunicationNature() {
        return communicationNature;
    }




    /**
     * @param communicationNature the communicationNature to set
     */
    public void setCommunicationNature(String communicationNature) {
        this.communicationNature = communicationNature;
    }




    /**
     * @return the communicationType
     */
    public String getCommunicationType() {
        return communicationType;
    }




    /**
     * @param communicationType the communicationType to set
     */
    public void setCommunicationType(String communicationType) {
        this.communicationType = communicationType;
    }




    /**
     * @return the addressType
     */
    public String getAddressType() {
        return addressType;
    }




    /**
     * @param addressType the addressType to set
     */
    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }




    /**
     * @return the electronicAddress
     */
    public String getElectronicAddress() {
        return electronicAddress;
    }




    /**
     * @param electronicAddress the electronicAddress to set
     */
    public void setElectronicAddress(String electronicAddress) {
        this.electronicAddress = electronicAddress;
    }




    /**
     * @return the iddPrefixPhone
     */
    public String getIddPrefixPhone() {
        return iddPrefixPhone;
    }




    /**
     * @param iddPrefixPhone the iddPrefixPhone to set
     */
    public void setIddPrefixPhone(String iddPrefixPhone) {
        this.iddPrefixPhone = iddPrefixPhone;
    }




    /**
     * @return the phoneNo
     */
    public String getPhoneNo() {
        return phoneNo;
    }




    /**
     * @param phoneNo the phoneNo to set
     */
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }




    /**
     * @return the website
     */
    public String getWebsite() {
        return website;
    }




    /**
     * @param website the website to set
     */
    public void setWebsite(String website) {
        this.website = website;
    }




    public PartyAddress() {
	}


	/**
	 * @return the countryCode
	 */
	public String getCountryCode() {
		return countryCode;
	}




	/**
	 * @param countryCode the countryCode to set
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}




	/**
	 * @return the flatNumber
	 */
	public String getFlatNumber() {
		return flatNumber;
	}




	/**
	 * @param flatNumber the flatNumber to set
	 */
	public void setFlatNumber(String flatNumber) {
		this.flatNumber = flatNumber;
	}




	/**
	 * @return the floor
	 */
	public String getFloor() {
		return floor;
	}




	/**
	 * @param floor the floor to set
	 */
	public void setFloor(String floor) {
		this.floor = floor;
	}




	/**
	 * @return the buildingNumber
	 */
	public String getBuildingNumber() {
		return buildingNumber;
	}




	/**
	 * @param buildingNumber the buildingNumber to set
	 */
	public void setBuildingNumber(String buildingNumber) {
		this.buildingNumber = buildingNumber;
	}




	/**
	 * @return the buildingName
	 */
	public String getBuildingName() {
		return buildingName;
	}




	/**
	 * @param buildingName the buildingName to set
	 */
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}




	/**
	 * @return the streetName
	 */
	public String getStreetName() {
		return streetName;
	}




	/**
	 * @param streetName the streetName to set
	 */
	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}




	/**
	 * @return the town
	 */
	public String getTown() {
		return town;
	}




	/**
	 * @param town the town to set
	 */
	public void setTown(String town) {
		this.town = town;
	}




	/**
	 * @return the countrySubdivision
	 */
	public String getCountrySubdivision() {
		return countrySubdivision;
	}




	/**
	 * @param countrySubdivision the countrySubdivision to set
	 */
	public void setCountrySubdivision(String countrySubdivision) {
		this.countrySubdivision = countrySubdivision;
	}




	/**
	 * @return the postalOrZipCode
	 */
	public String getPostalOrZipCode() {
		return postalOrZipCode;
	}




	/**
	 * @param postalOrZipCode the postalOrZipCode to set
	 */
	public void setPostalOrZipCode(String postalOrZipCode) {
		this.postalOrZipCode = postalOrZipCode;
	}




	/**
	 * @return the validatedBy
	 */
	public String getValidatedBy() {
		return validatedBy;
	}




	/**
	 * @param validatedBy the validatedBy to set
	 */
	public void setValidatedBy(String validatedBy) {
		this.validatedBy = validatedBy;
	}




	/**
	 * @return the postBoxNumber
	 */
	public String getPostBoxNumber() {
		return postBoxNumber;
	}




	/**
	 * @param postBoxNumber the postBoxNumber to set
	 */
	public void setPostBoxNumber(String postBoxNumber) {
		this.postBoxNumber = postBoxNumber;
	}




	/**
	 * @return the usePurpose
	 */
	public String getUsePurpose() {
		return usePurpose;
	}




	/**
	 * @param usePurpose the usePurpose to set
	 */
	public void setUsePurpose(String usePurpose) {
		this.usePurpose = usePurpose;
	}




	/**
	 * @return the regionCode
	 */
	public String getRegionCode() {
		return regionCode;
	}




	/**
	 * @param regionCode the regionCode to set
	 */
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}




	/**
	 * @return the district
	 */
	public String getDistrict() {
		return district;
	}




	/**
	 * @param district the district to set
	 */
	public void setDistrict(String district) {
		this.district = district;
	}




	/**
	 * @return the department
	 */
	public String getDepartment() {
		return department;
	}




	/**
	 * @param department the department to set
	 */
	public void setDepartment(String department) {
		this.department = department;
	}




	/**
	 * @return the subDepartment
	 */
	public String getSubDepartment() {
		return subDepartment;
	}




	/**
	 * @param subDepartment the subDepartment to set
	 */
	public void setSubDepartment(String subDepartment) {
		this.subDepartment = subDepartment;
	}




	/**
	 * @return the landmark
	 */
	public String getLandmark() {
		return landmark;
	}




	/**
	 * @param landmark the landmark to set
	 */
	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}




	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}




	public void loadFromJson(JsonObject jsonObject) {
		DTOUtils.loadDTOFromJson(this, jsonObject);
	}

	public static List<PartyAddress> loadFromJsonArray(JsonArray jsonArray) {

		List<PartyAddress> list = new ArrayList<PartyAddress>();
		for(int i=0; i<jsonArray.size(); i++) {

			JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
			PartyAddress contactAddress = new PartyAddress();
			contactAddress.loadFromJson(jsonObject);
			list.add(contactAddress);
		}

		return list;
	}

	public JsonObject toStringJson() {
		return DTOUtils.getJsonObjectFromObject(this);
	}




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




	public String getPrimary() {
		return primary;
	}




	public void setPrimary(String primary) {
		this.primary = primary;
	}

	public String getLegalEntityId() {
		return legalEntityId;
	}

	public void setLegalEntityId(String legalEntityId) {
		this.legalEntityId = legalEntityId;
	}
	
}
