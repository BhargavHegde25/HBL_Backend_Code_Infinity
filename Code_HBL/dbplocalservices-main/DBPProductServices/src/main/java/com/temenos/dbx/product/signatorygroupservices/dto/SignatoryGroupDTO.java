package com.temenos.dbx.product.signatorygroupservices.dto;


import java.io.Serializable;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignatoryGroupDTO implements Serializable, DBPDTO {

	private static final long serialVersionUID = 8885464581952519129L;
	

	private String coreCustomerId;
	private String signatoryGroupId;
    private String customerId;
    private String contractId;
    private String signatoryGroupName;
    private String signatoryGroupDescription;
    private String createdts;
    private String lastmodifiedts;
    private String createdby;
    private String modifiedby;
    private String customerSignatoryGroupId;
    private String synctimestamp;
    private String softdeleteflag;
	private String userId;
    private String userName;
    private String fullName;
    private String noOfUsers;
    private String contractName;
    private String coreCustomerName;
    private String customerRole;
    private String signatoryaddedts;
    private boolean isAssociated;
    
    private String firstName;
    private String lastName;
    private String role;
    private String isCombinedUser;
    private String status;
    private String userImage;

    
    
	public SignatoryGroupDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
    
	public SignatoryGroupDTO(String coreCustomerId, String signatoryGroupId, String customerId, String contractId,
			String signatoryGroupName, String signatoryGroupDescription, String createdts, String lastmodifiedts,
			String createdby, String modifiedby, String customerSignatoryGroupId, String synctimestamp,
			String softdeleteflag, String userId, String userName, String fullName, String noOfUsers,
			String contractName, String coreCustomerName, String customerRole, String signatoryaddedts,
			boolean isAssociated, String firstName, String lastName, String role, String isCombinedUser,
			String status, String userImage) {
		super();
		this.coreCustomerId = coreCustomerId;
		this.signatoryGroupId = signatoryGroupId;
		this.customerId = customerId;
		this.contractId = contractId;
		this.signatoryGroupName = signatoryGroupName;
		this.signatoryGroupDescription = signatoryGroupDescription;
		this.createdts = createdts;
		this.lastmodifiedts = lastmodifiedts;
		this.createdby = createdby;
		this.modifiedby = modifiedby;
		this.customerSignatoryGroupId = customerSignatoryGroupId;
		this.synctimestamp = synctimestamp;
		this.softdeleteflag = softdeleteflag;
		this.userId = userId;
		this.userName = userName;
		this.fullName = fullName;
		this.noOfUsers = noOfUsers;
		this.contractName = contractName;
		this.coreCustomerName = coreCustomerName;
		this.customerRole = customerRole;
		this.signatoryaddedts = signatoryaddedts;
		this.isAssociated = isAssociated;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
		this.isCombinedUser = isCombinedUser;
		this.status = status;
		this.userImage = userImage;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	
	public String getCoreCustomerId() {
		return coreCustomerId;
	}


	public void setCoreCustomerId(String coreCustomerId) {
		this.coreCustomerId = coreCustomerId;
	}


	public String getSignatoryGroupId() {
		return signatoryGroupId;
	}


	public void setSignatoryGroupId(String signatoryGroupId) {
		this.signatoryGroupId = signatoryGroupId;
	}


	public String getCustomerId() {
		return customerId;
	}


	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}


	public String getContractId() {
		return contractId;
	}


	public void setContractId(String contractId) {
		this.contractId = contractId;
	}


	public String getSignatoryGroupName() {
		return signatoryGroupName;
	}

	public void setSignatoryGroupName(String signatoryGroupName) {
		this.signatoryGroupName = signatoryGroupName;
	}

	public String getSignatoryGroupDescription() {
		return signatoryGroupDescription;
	}

	public void setSignatoryGroupDescription(String signatoryGroupDescription) {
		this.signatoryGroupDescription = signatoryGroupDescription;
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


	public String getCustomerSignatoryGroupId() {
		return customerSignatoryGroupId;
	}


	public void setCustomerSignatoryGroupId(String customerSignatoryGroupId) {
		this.customerSignatoryGroupId = customerSignatoryGroupId;
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

	public String getNoOfUsers() {
		return noOfUsers;
	}

	public void setNoOfUsers(String noOfUsers) {
		this.noOfUsers = noOfUsers;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getCoreCustomerName() {
		return coreCustomerName;
	}

	public void setCoreCustomerName(String coreCustomerName) {
		this.coreCustomerName = coreCustomerName;
	}


	public String getSignatoryaddedts() {
		return signatoryaddedts;
	}

	public void setSignatoryaddedts(String signatoryaddedts) {
		this.signatoryaddedts = signatoryaddedts;
	}

	public String getCustomerRole() {
		return customerRole;
	}

	public void setCustomerRole(String customerRole) {
		this.customerRole = customerRole;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getIsCombinedUser() {
		return isCombinedUser;
	}

	public void setIsCombinedUser(String isCombinedUser) {
		this.isCombinedUser = isCombinedUser;
	}

	public boolean isAssociated() {
		return isAssociated;
	}

	public void setAssociated(boolean isAssociated) {
		this.isAssociated = isAssociated;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserImage() {
		return userImage;
	}

	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contractId == null) ? 0 : contractId.hashCode());
		result = prime * result + ((contractName == null) ? 0 : contractName.hashCode());
		result = prime * result + ((coreCustomerId == null) ? 0 : coreCustomerId.hashCode());
		result = prime * result + ((coreCustomerName == null) ? 0 : coreCustomerName.hashCode());
		result = prime * result + ((createdby == null) ? 0 : createdby.hashCode());
		result = prime * result + ((createdts == null) ? 0 : createdts.hashCode());
		result = prime * result + ((customerId == null) ? 0 : customerId.hashCode());
		result = prime * result + ((customerRole == null) ? 0 : customerRole.hashCode());
		result = prime * result + ((customerSignatoryGroupId == null) ? 0 : customerSignatoryGroupId.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((fullName == null) ? 0 : fullName.hashCode());
		result = prime * result + (isAssociated ? 1231 : 1237);
		result = prime * result + ((isCombinedUser == null) ? 0 : isCombinedUser.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((lastmodifiedts == null) ? 0 : lastmodifiedts.hashCode());
		result = prime * result + ((modifiedby == null) ? 0 : modifiedby.hashCode());
		result = prime * result + ((noOfUsers == null) ? 0 : noOfUsers.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((signatoryGroupDescription == null) ? 0 : signatoryGroupDescription.hashCode());
		result = prime * result + ((signatoryGroupId == null) ? 0 : signatoryGroupId.hashCode());
		result = prime * result + ((signatoryGroupName == null) ? 0 : signatoryGroupName.hashCode());
		result = prime * result + ((signatoryaddedts == null) ? 0 : signatoryaddedts.hashCode());
		result = prime * result + ((softdeleteflag == null) ? 0 : softdeleteflag.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((synctimestamp == null) ? 0 : synctimestamp.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		result = prime * result + ((userImage == null) ? 0 : userImage.hashCode());
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
		SignatoryGroupDTO other = (SignatoryGroupDTO) obj;
		if (contractId == null) {
			if (other.contractId != null)
				return false;
		} else if (!contractId.equals(other.contractId))
			return false;
		if (contractName == null) {
			if (other.contractName != null)
				return false;
		} else if (!contractName.equals(other.contractName))
			return false;
		if (coreCustomerId == null) {
			if (other.coreCustomerId != null)
				return false;
		} else if (!coreCustomerId.equals(other.coreCustomerId))
			return false;
		if (coreCustomerName == null) {
			if (other.coreCustomerName != null)
				return false;
		} else if (!coreCustomerName.equals(other.coreCustomerName))
			return false;
		if (createdby == null) {
			if (other.createdby != null)
				return false;
		} else if (!createdby.equals(other.createdby))
			return false;
		if (createdts == null) {
			if (other.createdts != null)
				return false;
		} else if (!createdts.equals(other.createdts))
			return false;
		if (customerId == null) {
			if (other.customerId != null)
				return false;
		} else if (!customerId.equals(other.customerId))
			return false;
		if (customerRole == null) {
			if (other.customerRole != null)
				return false;
		} else if (!customerRole.equals(other.customerRole))
			return false;
		if (customerSignatoryGroupId == null) {
			if (other.customerSignatoryGroupId != null)
				return false;
		} else if (!customerSignatoryGroupId.equals(other.customerSignatoryGroupId))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (fullName == null) {
			if (other.fullName != null)
				return false;
		} else if (!fullName.equals(other.fullName))
			return false;
		if (isAssociated != other.isAssociated)
			return false;
		if (isCombinedUser == null) {
			if (other.isCombinedUser != null)
				return false;
		} else if (!isCombinedUser.equals(other.isCombinedUser))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (lastmodifiedts == null) {
			if (other.lastmodifiedts != null)
				return false;
		} else if (!lastmodifiedts.equals(other.lastmodifiedts))
			return false;
		if (modifiedby == null) {
			if (other.modifiedby != null)
				return false;
		} else if (!modifiedby.equals(other.modifiedby))
			return false;
		if (noOfUsers == null) {
			if (other.noOfUsers != null)
				return false;
		} else if (!noOfUsers.equals(other.noOfUsers))
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		if (signatoryGroupDescription == null) {
			if (other.signatoryGroupDescription != null)
				return false;
		} else if (!signatoryGroupDescription.equals(other.signatoryGroupDescription))
			return false;
		if (signatoryGroupId == null) {
			if (other.signatoryGroupId != null)
				return false;
		} else if (!signatoryGroupId.equals(other.signatoryGroupId))
			return false;
		if (signatoryGroupName == null) {
			if (other.signatoryGroupName != null)
				return false;
		} else if (!signatoryGroupName.equals(other.signatoryGroupName))
			return false;
		if (signatoryaddedts == null) {
			if (other.signatoryaddedts != null)
				return false;
		} else if (!signatoryaddedts.equals(other.signatoryaddedts))
			return false;
		if (softdeleteflag == null) {
			if (other.softdeleteflag != null)
				return false;
		} else if (!softdeleteflag.equals(other.softdeleteflag))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (synctimestamp == null) {
			if (other.synctimestamp != null)
				return false;
		} else if (!synctimestamp.equals(other.synctimestamp))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		if (userImage == null) {
			if (other.userImage != null)
				return false;
		} else if (!userImage.equals(other.userImage))
			return false;
		return true;
	}

}