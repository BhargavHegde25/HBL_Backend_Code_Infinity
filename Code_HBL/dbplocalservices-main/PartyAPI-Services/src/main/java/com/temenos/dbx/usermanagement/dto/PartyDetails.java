package com.temenos.dbx.usermanagement.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class PartyDetails implements Serializable {

	private static final long serialVersionUID = 653792502420487769L;

	private String detailToBeUpdated;
	private String operation;

	private String orderId;
	private String status;

	private String code;
	private String message;
	
	private String communicationNature;
	private String communicationType;
	private String addressType;
	private String primary;
	private String electronicAddress;
	private String phoneNo;
	private String addressesReference;
	
	
	private String countryCode;
	private String buildingName;
	private String streetName;
	private String town;
	private String countrySubdivision;
	private String postalOrZipCode;
	private String regionCode;
	

	
	public String getDetailToBeUpdated() {
		return detailToBeUpdated;
	}
	public void setDetailToBeUpdated(String detailToBeUpdated) {
		this.detailToBeUpdated = detailToBeUpdated;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCommunicationNature() {
		return communicationNature;
	}
	public void setCommunicationNature(String communicationNature) {
		this.communicationNature = communicationNature;
	}
	public String getCommunicationType() {
		return communicationType;
	}
	public void setCommunicationType(String communicationType) {
		this.communicationType = communicationType;
	}
	public String getAddressType() {
		return addressType;
	}
	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}
	public String getPrimary() {
		return primary;
	}
	public void setPrimary(String primary) {
		this.primary = primary;
	}
	public String getElectronicAddress() {
		return electronicAddress;
	}
	public void setElectronicAddress(String electronicAddress) {
		this.electronicAddress = electronicAddress;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getAddressesReference() {
		return addressesReference;
	}
	public void setAddressesReference(String addressesReference) {
		this.addressesReference = addressesReference;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getBuildingName() {
		return buildingName;
	}
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	public String getStreetName() {
		return streetName;
	}
	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}
	public String getTown() {
		return town;
	}
	public void setTown(String town) {
		this.town = town;
	}
	public String getCountrySubdivision() {
		return countrySubdivision;
	}
	public void setCountrySubdivision(String countrySubdivision) {
		this.countrySubdivision = countrySubdivision;
	}
	public String getPostalOrZipCode() {
		return postalOrZipCode;
	}
	public void setPostalOrZipCode(String postalOrZipCode) {
		this.postalOrZipCode = postalOrZipCode;
	}
	public String getRegionCode() {
		return regionCode;
	}
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((addressType == null) ? 0 : addressType.hashCode());
		result = prime * result + ((addressesReference == null) ? 0 : addressesReference.hashCode());
		result = prime * result + ((buildingName == null) ? 0 : buildingName.hashCode());
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((communicationNature == null) ? 0 : communicationNature.hashCode());
		result = prime * result + ((communicationType == null) ? 0 : communicationType.hashCode());
		result = prime * result + ((countryCode == null) ? 0 : countryCode.hashCode());
		result = prime * result + ((countrySubdivision == null) ? 0 : countrySubdivision.hashCode());
		result = prime * result + ((detailToBeUpdated == null) ? 0 : detailToBeUpdated.hashCode());
		result = prime * result + ((electronicAddress == null) ? 0 : electronicAddress.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((operation == null) ? 0 : operation.hashCode());
		result = prime * result + ((orderId == null) ? 0 : orderId.hashCode());
		result = prime * result + ((phoneNo == null) ? 0 : phoneNo.hashCode());
		result = prime * result + ((postalOrZipCode == null) ? 0 : postalOrZipCode.hashCode());
		result = prime * result + ((primary == null) ? 0 : primary.hashCode());
		result = prime * result + ((regionCode == null) ? 0 : regionCode.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((streetName == null) ? 0 : streetName.hashCode());
		result = prime * result + ((town == null) ? 0 : town.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PartyDetails other = (PartyDetails) obj;
		if (addressType == null) {
			if (other.addressType != null)
				return false;
		} else if (!addressType.equals(other.addressType))
			return false;
		if (addressesReference == null) {
			if (other.addressesReference != null)
				return false;
		} else if (!addressesReference.equals(other.addressesReference))
			return false;
		if (buildingName == null) {
			if (other.buildingName != null)
				return false;
		} else if (!buildingName.equals(other.buildingName))
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (communicationNature == null) {
			if (other.communicationNature != null)
				return false;
		} else if (!communicationNature.equals(other.communicationNature))
			return false;
		if (communicationType == null) {
			if (other.communicationType != null)
				return false;
		} else if (!communicationType.equals(other.communicationType))
			return false;
		if (countryCode == null) {
			if (other.countryCode != null)
				return false;
		} else if (!countryCode.equals(other.countryCode))
			return false;
		if (countrySubdivision == null) {
			if (other.countrySubdivision != null)
				return false;
		} else if (!countrySubdivision.equals(other.countrySubdivision))
			return false;
		if (detailToBeUpdated == null) {
			if (other.detailToBeUpdated != null)
				return false;
		} else if (!detailToBeUpdated.equals(other.detailToBeUpdated))
			return false;
		if (electronicAddress == null) {
			if (other.electronicAddress != null)
				return false;
		} else if (!electronicAddress.equals(other.electronicAddress))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (operation == null) {
			if (other.operation != null)
				return false;
		} else if (!operation.equals(other.operation))
			return false;
		if (orderId == null) {
			if (other.orderId != null)
				return false;
		} else if (!orderId.equals(other.orderId))
			return false;
		if (phoneNo == null) {
			if (other.phoneNo != null)
				return false;
		} else if (!phoneNo.equals(other.phoneNo))
			return false;
		if (postalOrZipCode == null) {
			if (other.postalOrZipCode != null)
				return false;
		} else if (!postalOrZipCode.equals(other.postalOrZipCode))
			return false;
		if (primary == null) {
			if (other.primary != null)
				return false;
		} else if (!primary.equals(other.primary))
			return false;
		if (regionCode == null) {
			if (other.regionCode != null)
				return false;
		} else if (!regionCode.equals(other.regionCode))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (streetName == null) {
			if (other.streetName != null)
				return false;
		} else if (!streetName.equals(other.streetName))
			return false;
		if (town == null) {
			if (other.town != null)
				return false;
		} else if (!town.equals(other.town))
			return false;
		return true;
	}


}