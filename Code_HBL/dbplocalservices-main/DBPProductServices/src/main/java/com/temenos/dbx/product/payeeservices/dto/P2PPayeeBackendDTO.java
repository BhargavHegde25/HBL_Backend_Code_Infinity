package com.temenos.dbx.product.payeeservices.dto;





import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.temenos.dbx.product.commonsutils.ValidationUtils;


@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class P2PPayeeBackendDTO  implements DBPDTO {
	private static final long serialVersionUID = 3544964821313584766L;
	
	@JsonAlias({"Id"})
	private String id;
	@JsonAlias({"PayPersonId"})
	private String payeeId;
	private String firstName;
	private String lastName;
	private String phone;
	private String email;
	@JsonAlias({"User_id","customerId"})
	private String userId;
	private String secondaryEmail;
	private String secondaryEmail2;
	private String secondoryPhoneNumber;
	private String secondaryPhoneNumber2;
	private String primaryContactForSending;
	private String nickName;
	private String name;
	private String isSoftDelete;
	private String phoneExtension;
	private String phoneCountryCode;
	private  String dbpErrCode;
    @JsonAlias({"errorMessage", "errmsg"})
    private  String dbpErrMsg;
    private Integer updatedRecords;
    private Integer deletedRecords;
    private String contractId;
    private String cif;
    private String noOfCustomersLinked;
    

	public P2PPayeeBackendDTO() {
		super();
	}
    
    public P2PPayeeBackendDTO(String id, String payeeId, String firstName, String lastName, String phone, String email,
			String userId, String secondaryEmail, String secondaryEmail2, String secondoryPhoneNumber,
			String secondaryPhoneNumber2, String primaryContactForSending, String nickName, String name,
			String isSoftDelete, String phoneExtension, String phoneCountryCode, String dbpErrCode, String dbpErrMsg,
			Integer updatedRecords, Integer deletedRecords, String contractId, String cif, String noOfCustomersLinked) {
		super();
		this.id = id;
		this.payeeId = payeeId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.email = email;
		this.userId = userId;
		this.secondaryEmail = secondaryEmail;
		this.secondaryEmail2 = secondaryEmail2;
		this.secondoryPhoneNumber = secondoryPhoneNumber;
		this.secondaryPhoneNumber2 = secondaryPhoneNumber2;
		this.primaryContactForSending = primaryContactForSending;
		this.nickName = nickName;
		this.name = name;
		this.isSoftDelete = isSoftDelete;
		this.phoneExtension = phoneExtension;
		this.phoneCountryCode = phoneCountryCode;
		this.dbpErrCode = dbpErrCode;
		this.dbpErrMsg = dbpErrMsg;
		this.updatedRecords = updatedRecords;
		this.deletedRecords = deletedRecords;
		this.contractId = contractId;
		this.cif = cif;
		this.noOfCustomersLinked = noOfCustomersLinked;
	}

	public String getCif() {
		return cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDbpErrCode() {
		return dbpErrCode;
	}

	public void setDbpErrCode(String dbpErrCode) {
		this.dbpErrCode = dbpErrCode;
	}

	public String getDbpErrMsg() {
		return dbpErrMsg;
	}

	public void setDbpErrMsg(String dbpErrMsg) {
		this.dbpErrMsg = dbpErrMsg;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getPayeeId() {
		return payeeId;
	}
	
	public void setPayeeId(String payeeId) {
		this.payeeId = payeeId;
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
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getUserid() {
		return userId;
	}
	
	public void setUserid(String userid) {
		this.userId = userid;
	}
	
	public String getSecondaryEmail() {
		return secondaryEmail;
	}
	
	public void setSecondaryEmail(String secondaryEmail) {
		this.secondaryEmail = secondaryEmail;
	}
	
	public String getSecondaryEmail2() {
		return secondaryEmail2;
	}
	
	public void setSecondaryEmail2(String secondaryEmail2) {
		this.secondaryEmail2 = secondaryEmail2;
	}
	
	public String getSecondoryPhoneNumber() {
		return secondoryPhoneNumber;
	}
	
	public void setSecondoryPhoneNumber(String secondoryPhoneNumber) {
		this.secondoryPhoneNumber = secondoryPhoneNumber;
	}
	
	public String getSecondaryPhoneNumber2() {
		return secondaryPhoneNumber2;
	}
	
	public void setSecondaryPhoneNumber2(String secondaryPhoneNumber2) {
		this.secondaryPhoneNumber2 = secondaryPhoneNumber2;
	}
	
	public String getPrimaryContactForSending() {
		return primaryContactForSending;
	}
	
	public void setPrimaryContactForSending(String primaryContactForSending) {
		this.primaryContactForSending = primaryContactForSending;
	}
	
	public String getNickName() {
		return nickName;
	}
	
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getIsSoftDelete() {
		return isSoftDelete;
	}
	
	public void setIsSoftDelete(String isSoftDelete) {
		this.isSoftDelete = isSoftDelete;
	}
	
	public String getPhoneExtension() {
		return phoneExtension;
	}
	
	public void setPhoneExtension(String phoneExtension) {
		this.phoneExtension = phoneExtension;
	}
	public String getPhoneCountryCode() {
		return phoneCountryCode;
	}
	
	public void setPhoneCountryCode(String phoneCountryCode) {
		this.phoneCountryCode = phoneCountryCode;
	}
	
	public Integer getUpdatedRecords() {
		return updatedRecords;
	}
	
	public void setUpdatedRecords(Integer updatedRecords) {
		this.updatedRecords = updatedRecords;
	}
	
	public Integer getDeletedRecords() {
		return deletedRecords;
	}
	
	public void setDeletedRecords(Integer deletedRecords) {
		this.deletedRecords = deletedRecords;
	}
	
	public String getNoOfCustomersLinked() {
		return noOfCustomersLinked;
	}

	public void setNoOfCustomersLinked(String noOfCustomersLinked) {
		this.noOfCustomersLinked = noOfCustomersLinked;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((payeeId == null) ? 0 : payeeId.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result + ((nickName == null) ? 0 : nickName.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((isSoftDelete == null) ? 0 : isSoftDelete.hashCode());
		result = prime * result + ((phoneCountryCode == null) ? 0 : phoneCountryCode.hashCode());
		result = prime * result + ((cif == null) ? 0 : cif.hashCode());
		result = prime * result + ((dbpErrCode == null) ? 0 : dbpErrCode.hashCode());
		result = prime * result + ((dbpErrMsg == null) ? 0 : dbpErrMsg.hashCode());
		result = prime * result + ((noOfCustomersLinked == null) ? 0 : noOfCustomersLinked.hashCode());
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
		P2PPayeeBackendDTO other = (P2PPayeeBackendDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public boolean isValidInput() {
    	if(StringUtils.isNotBlank(this.name) && !ValidationUtils.isValidName(this.name) ) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(this.nickName) && !ValidationUtils.isValidName(this.nickName) ) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(this.email) && !ValidationUtils.isValidEmail(this.email) ) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(this.secondaryEmail) && !ValidationUtils.isValidEmail(this.secondaryEmail) ) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(this.secondaryEmail2) && !ValidationUtils.isValidEmail(this.secondaryEmail2) ) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(this.phone) && !ValidationUtils.isValidPhoneNumber(this.phone) ) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(this.secondoryPhoneNumber) && !ValidationUtils.isValidPhoneNumber(this.secondoryPhoneNumber) ) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(this.secondaryPhoneNumber2) && !ValidationUtils.isValidPhoneNumber(this.secondaryPhoneNumber2) ) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(this.primaryContactForSending) && !(ValidationUtils.isValidEmail(this.primaryContactForSending) || ValidationUtils.isValidPhoneNumber(this.primaryContactForSending)) ) {
    		return false;
    	}
    	return true;
    }
	
}