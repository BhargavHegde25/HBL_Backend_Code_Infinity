package com.temenos.infinity.api.usermanagement.dto;

import java.io.Serializable;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class CustomerDetailsDTO implements Serializable, DBPDTO {

	private static final long serialVersionUID = 653792502420487769L;

	private String id;
	private String Extension;
	private String isPrimary;
	private String isAlertsRequired;
	private String isTypeBusiness;
	private String userName;
	private String modifiedByName;
	private String deleteCommunicationID;

	private String phoneNumber;
	private String phoneCountryCode;

	private String value;

	private String addr_id;
	private String addr_type;
	private String addrLine1;
	private String addrLine2;
	private String city_id;
	private String zipCode;
	private String region_id;
	private String deleteAddressID;
	private String countryCode;
	private String customerName;
	private String customerId;
	private String addressId;
	private String addressType;
	private String primaryFlag;
	private String requestDetails;
	private String supportingDocumentIds;

	private String detailToBeUpdated;
	private String operation;

	private String orderId;
	private String status;

	private String code;
	private String message;
	private String errorMessage;
	private String legalEntityId;
	public String getCustomerName() {
		return customerName;
	}
	
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
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
	
	public String getPrimaryFlag() {
		return primaryFlag;
	}
	
	public void setPrimaryFlag(String primaryFlag) {
		this.primaryFlag = primaryFlag;
	}
	
	public String getRequestDetails() {
		return requestDetails;
	}
	
	public void setRequestDetails(String requestDetails) {
		this.requestDetails = requestDetails;
	}
	
	public String getSupportingDocumentIds() {
		return supportingDocumentIds;
	}

	public void setSupportingDocumentIds(String supportingDocumentIds) {
		this.supportingDocumentIds = supportingDocumentIds;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getExtension() {
		return Extension;
	}

	public void setExtension(String extension) {
		Extension = extension;
	}

	public String getIsPrimary() {
		return isPrimary;
	}

	public void setIsPrimary(String isPrimary) {
		this.isPrimary = isPrimary;
	}

	public String getIsAlertsRequired() {
		return isAlertsRequired;
	}

	public void setIsAlertsRequired(String isAlertsRequired) {
		this.isAlertsRequired = isAlertsRequired;
	}

	public String getIsTypeBusiness() {
		return isTypeBusiness;
	}

	public void setIsTypeBusiness(String isTypeBusiness) {
		this.isTypeBusiness = isTypeBusiness;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getModifiedByName() {
		return modifiedByName;
	}

	public void setModifiedByName(String modifiedByName) {
		this.modifiedByName = modifiedByName;
	}

	public String getDeleteCommunicationID() {
		return deleteCommunicationID;
	}

	public void setDeleteCommunicationID(String deleteCommunicationID) {
		this.deleteCommunicationID = deleteCommunicationID;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneCountryCode() {
		return phoneCountryCode;
	}

	public void setPhoneCountryCode(String phoneCountryCode) {
		this.phoneCountryCode = phoneCountryCode;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getAddr_id() {
		return addr_id;
	}

	public void setAddr_id(String addr_id) {
		this.addr_id = addr_id;
	}

	public String getAddr_type() {
		return addr_type;
	}

	public void setAddr_type(String addr_type) {
		this.addr_type = addr_type;
	}

	public String getAddrLine1() {
		return addrLine1;
	}

	public void setAddrLine1(String addrLine1) {
		this.addrLine1 = addrLine1;
	}

	public String getAddrLine2() {
		return addrLine2;
	}

	public void setAddrLine2(String addrLine2) {
		this.addrLine2 = addrLine2;
	}

	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getRegion_id() {
		return region_id;
	}

	public void setRegion_id(String region_id) {
		this.region_id = region_id;
	}

	public String getDeleteAddressID() {
		return deleteAddressID;
	}

	public void setDeleteAddressID(String deleteAddressID) {
		this.deleteAddressID = deleteAddressID;
	}

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

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Extension == null) ? 0 : Extension.hashCode());
		result = prime * result + ((addrLine1 == null) ? 0 : addrLine1.hashCode());
		result = prime * result + ((addrLine2 == null) ? 0 : addrLine2.hashCode());
		result = prime * result + ((addr_id == null) ? 0 : addr_id.hashCode());
		result = prime * result + ((addr_type == null) ? 0 : addr_type.hashCode());
		result = prime * result + ((city_id == null) ? 0 : city_id.hashCode());
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((countryCode == null) ? 0 : countryCode.hashCode());
		result = prime * result + ((deleteAddressID == null) ? 0 : deleteAddressID.hashCode());
		result = prime * result + ((deleteCommunicationID == null) ? 0 : deleteCommunicationID.hashCode());
		result = prime * result + ((detailToBeUpdated == null) ? 0 : detailToBeUpdated.hashCode());
		result = prime * result + ((errorMessage == null) ? 0 : errorMessage.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((isAlertsRequired == null) ? 0 : isAlertsRequired.hashCode());
		result = prime * result + ((isPrimary == null) ? 0 : isPrimary.hashCode());
		result = prime * result + ((isTypeBusiness == null) ? 0 : isTypeBusiness.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((modifiedByName == null) ? 0 : modifiedByName.hashCode());
		result = prime * result + ((operation == null) ? 0 : operation.hashCode());
		result = prime * result + ((orderId == null) ? 0 : orderId.hashCode());
		result = prime * result + ((phoneCountryCode == null) ? 0 : phoneCountryCode.hashCode());
		result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
		result = prime * result + ((region_id == null) ? 0 : region_id.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		result = prime * result + ((zipCode == null) ? 0 : zipCode.hashCode());
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
		CustomerDetailsDTO other = (CustomerDetailsDTO) obj;
		if (Extension == null) {
			if (other.Extension != null)
				return false;
		} else if (!Extension.equals(other.Extension))
			return false;
		if (addrLine1 == null) {
			if (other.addrLine1 != null)
				return false;
		} else if (!addrLine1.equals(other.addrLine1))
			return false;
		if (addrLine2 == null) {
			if (other.addrLine2 != null)
				return false;
		} else if (!addrLine2.equals(other.addrLine2))
			return false;
		if (addr_id == null) {
			if (other.addr_id != null)
				return false;
		} else if (!addr_id.equals(other.addr_id))
			return false;
		if (addr_type == null) {
			if (other.addr_type != null)
				return false;
		} else if (!addr_type.equals(other.addr_type))
			return false;
		if (city_id == null) {
			if (other.city_id != null)
				return false;
		} else if (!city_id.equals(other.city_id))
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (countryCode == null) {
			if (other.countryCode != null)
				return false;
		} else if (!countryCode.equals(other.countryCode))
			return false;
		if (deleteAddressID == null) {
			if (other.deleteAddressID != null)
				return false;
		} else if (!deleteAddressID.equals(other.deleteAddressID))
			return false;
		if (deleteCommunicationID == null) {
			if (other.deleteCommunicationID != null)
				return false;
		} else if (!deleteCommunicationID.equals(other.deleteCommunicationID))
			return false;
		if (detailToBeUpdated == null) {
			if (other.detailToBeUpdated != null)
				return false;
		} else if (!detailToBeUpdated.equals(other.detailToBeUpdated))
			return false;
		if (errorMessage == null) {
			if (other.errorMessage != null)
				return false;
		} else if (!errorMessage.equals(other.errorMessage))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isAlertsRequired == null) {
			if (other.isAlertsRequired != null)
				return false;
		} else if (!isAlertsRequired.equals(other.isAlertsRequired))
			return false;
		if (isPrimary == null) {
			if (other.isPrimary != null)
				return false;
		} else if (!isPrimary.equals(other.isPrimary))
			return false;
		if (isTypeBusiness == null) {
			if (other.isTypeBusiness != null)
				return false;
		} else if (!isTypeBusiness.equals(other.isTypeBusiness))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (modifiedByName == null) {
			if (other.modifiedByName != null)
				return false;
		} else if (!modifiedByName.equals(other.modifiedByName))
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
		if (phoneCountryCode == null) {
			if (other.phoneCountryCode != null)
				return false;
		} else if (!phoneCountryCode.equals(other.phoneCountryCode))
			return false;
		if (phoneNumber == null) {
			if (other.phoneNumber != null)
				return false;
		} else if (!phoneNumber.equals(other.phoneNumber))
			return false;
		if (region_id == null) {
			if (other.region_id != null)
				return false;
		} else if (!region_id.equals(other.region_id))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		if (zipCode == null) {
			if (other.zipCode != null)
				return false;
		} else if (!zipCode.equals(other.zipCode))
			return false;
		return true;
	}

	public String getLegalEntityId() {
		return legalEntityId;
	}

	public void setLegalEntityId(String legalEntityId) {
		this.legalEntityId = legalEntityId;
	}

}