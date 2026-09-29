package com.temenos.infinity.product.bulkpaymentservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kony.dbputilities.util.ErrorCodeEnum;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BulkPaymentTemplatePODTO implements DBPDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8179219530040154753L;
	
	private String paymentrequestPOId;
	private String paymentrequestId;
	private String paymentOrderId;
	private String templateId;
	private String confirmationNumber;
	private String recipientName;
	private String accountNumber;
	private String bankName;
	private String swift;
	private String featureActionId;
	private String companyId;
	private String roleId;
	private String status;
	private String currency;
	private String amount;
	private String feesPaidBy;
	private String paymentReference;
	private String debitAccountIBAN;
	private String beneficiaryIBAN;
	private String beneficiaryName;
	private String beneficiaryNickName;
	private String beneficiaryAddress;
	private String accountWithBankBIC;
	private String customer;
	private String paymentMethod;
	private String accType;
	private String createdby;
	private String modifiedby;
	private String createdts;
	private String lastmodifiedts;
	private String synctimestamp;
	
	private String beneficiaryType;
	private String addToExistingFlag;
	
	private ErrorCodeEnum dbpErrorCode;
	@JsonAlias({ "errorMessage", "errmsg", "ErrorMessage" })
	private String dbpErrMsg;
	
	public BulkPaymentTemplatePODTO() {
		super();
	}

	public BulkPaymentTemplatePODTO(String paymentrequestPOId, String paymentrequestId, String paymentOrderId, String templateId,
			String confirmationNumber, String recipientName, String accountNumber, String bankName, String swift,
			String featureActionId, String companyId, String roleId, String status, String currency, String amount,
			String feesPaidBy, String paymentReference, String debitAccountIBAN, String beneficiaryIBAN,
			String beneficiaryName, String beneficiaryNickName, String beneficiaryAddress, String accountWithBankBIC,
			String customer, String paymentMethod, String accType, String createdby, String modifiedby,
			String createdts, String lastmodifiedts, String synctimestamp, ErrorCodeEnum dbpErrorCode,
			String dbpErrMsg, String beneficiaryType, String addToExistingFlag) {
		super();
		this.paymentrequestPOId = paymentrequestPOId;
		this.paymentrequestId = paymentrequestId;
		this.paymentOrderId = paymentOrderId;
		this.templateId = templateId;
		this.confirmationNumber = confirmationNumber;
		this.recipientName = recipientName;
		this.accountNumber = accountNumber;
		this.bankName = bankName;
		this.swift = swift;
		this.featureActionId = featureActionId;
		this.companyId = companyId;
		this.roleId = roleId;
		this.status = status;
		this.currency = currency;
		this.amount = amount;
		this.feesPaidBy = feesPaidBy;
		this.paymentReference = paymentReference;
		this.debitAccountIBAN = debitAccountIBAN;
		this.beneficiaryIBAN = beneficiaryIBAN;
		this.beneficiaryName = beneficiaryName;
		this.beneficiaryNickName = beneficiaryNickName;
		this.beneficiaryAddress = beneficiaryAddress;
		this.accountWithBankBIC = accountWithBankBIC;
		this.customer = customer;
		this.paymentMethod = paymentMethod;
		this.accType = accType;
		this.createdby = createdby;
		this.modifiedby = modifiedby;
		this.createdts = createdts;
		this.lastmodifiedts = lastmodifiedts;
		this.synctimestamp = synctimestamp;
		this.dbpErrorCode = dbpErrorCode;
		this.dbpErrMsg = dbpErrMsg;
		this.beneficiaryType = beneficiaryType;
		this.addToExistingFlag = addToExistingFlag;
	}
	
	public String getPaymentrequestPOId() {
		return paymentrequestPOId;
	}

	public void setPaymentrequestPOId(String paymentrequestPOId) {
		this.paymentrequestPOId = paymentrequestPOId;
	}

	public String getBeneficiaryType() {
		return beneficiaryType;
	}

	public void setBeneficiaryType(String beneficiaryType) {
		this.beneficiaryType = beneficiaryType;
	}
	
	public String getAddToExistingFlag() {
		return addToExistingFlag;
	}

	public void setAddToExistingFlag(String addToExistingFlag) {
		this.addToExistingFlag = addToExistingFlag;
	}

	public String getPaymentrequestId() {
		return paymentrequestId;
	}

	public void setPaymentrequestId(String paymentrequestId) {
		this.paymentrequestId = paymentrequestId;
	}

	public String getPaymentOrderId() {
		return paymentOrderId;
	}

	public void setPaymentOrderId(String paymentOrderId) {
		this.paymentOrderId = paymentOrderId;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getConfirmationNumber() {
		return confirmationNumber;
	}

	public void setConfirmationNumber(String confirmationNumber) {
		this.confirmationNumber = confirmationNumber;
	}

	public String getRecipientName() {
		return recipientName;
	}

	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getSwift() {
		return swift;
	}

	public void setSwift(String swift) {
		this.swift = swift;
	}

	public String getFeatureActionId() {
		return featureActionId;
	}

	public void setFeatureActionId(String featureActionId) {
		this.featureActionId = featureActionId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getFeesPaidBy() {
		return feesPaidBy;
	}

	public void setFeesPaidBy(String feesPaidBy) {
		this.feesPaidBy = feesPaidBy;
	}

	public String getPaymentReference() {
		return paymentReference;
	}

	public void setPaymentReference(String paymentReference) {
		this.paymentReference = paymentReference;
	}

	public String getDebitAccountIBAN() {
		return debitAccountIBAN;
	}

	public void setDebitAccountIBAN(String debitAccountIBAN) {
		this.debitAccountIBAN = debitAccountIBAN;
	}

	public String getBeneficiaryIBAN() {
		return beneficiaryIBAN;
	}

	public void setBeneficiaryIBAN(String beneficiaryIBAN) {
		this.beneficiaryIBAN = beneficiaryIBAN;
	}

	public String getBeneficiaryName() {
		return beneficiaryName;
	}

	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}

	public String getBeneficiaryNickName() {
		return beneficiaryNickName;
	}

	public void setBeneficiaryNickName(String beneficiaryNickName) {
		this.beneficiaryNickName = beneficiaryNickName;
	}

	public String getBeneficiaryAddress() {
		return beneficiaryAddress;
	}

	public void setBeneficiaryAddress(String beneficiaryAddress) {
		this.beneficiaryAddress = beneficiaryAddress;
	}

	public String getAccountWithBankBIC() {
		return accountWithBankBIC;
	}

	public void setAccountWithBankBIC(String accountWithBankBIC) {
		this.accountWithBankBIC = accountWithBankBIC;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getAccType() {
		return accType;
	}

	public void setAccType(String accType) {
		this.accType = accType;
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

	public ErrorCodeEnum getDbpErrorCode() {
		return dbpErrorCode;
	}

	public void setDbpErrorCode(ErrorCodeEnum dbpErrorCode) {
		this.dbpErrorCode = dbpErrorCode;
	}

	public String getDbpErrMsg() {
		return dbpErrMsg;
	}

	public void setDbpErrMsg(String dbpErrMsg) {
		this.dbpErrMsg = dbpErrMsg;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accType == null) ? 0 : accType.hashCode());
		result = prime * result + ((accountNumber == null) ? 0 : accountNumber.hashCode());
		result = prime * result + ((accountWithBankBIC == null) ? 0 : accountWithBankBIC.hashCode());
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((bankName == null) ? 0 : bankName.hashCode());
		result = prime * result + ((beneficiaryAddress == null) ? 0 : beneficiaryAddress.hashCode());
		result = prime * result + ((beneficiaryIBAN == null) ? 0 : beneficiaryIBAN.hashCode());
		result = prime * result + ((beneficiaryName == null) ? 0 : beneficiaryName.hashCode());
		result = prime * result + ((beneficiaryNickName == null) ? 0 : beneficiaryNickName.hashCode());
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((confirmationNumber == null) ? 0 : confirmationNumber.hashCode());
		result = prime * result + ((createdby == null) ? 0 : createdby.hashCode());
		result = prime * result + ((createdts == null) ? 0 : createdts.hashCode());
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + ((customer == null) ? 0 : customer.hashCode());
		result = prime * result + ((dbpErrMsg == null) ? 0 : dbpErrMsg.hashCode());
		result = prime * result + ((dbpErrorCode == null) ? 0 : dbpErrorCode.hashCode());
		result = prime * result + ((debitAccountIBAN == null) ? 0 : debitAccountIBAN.hashCode());
		result = prime * result + ((featureActionId == null) ? 0 : featureActionId.hashCode());
		result = prime * result + ((feesPaidBy == null) ? 0 : feesPaidBy.hashCode());
		result = prime * result + ((lastmodifiedts == null) ? 0 : lastmodifiedts.hashCode());
		result = prime * result + ((modifiedby == null) ? 0 : modifiedby.hashCode());
		result = prime * result + ((paymentMethod == null) ? 0 : paymentMethod.hashCode());
		result = prime * result + ((paymentOrderId == null) ? 0 : paymentOrderId.hashCode());
		result = prime * result + ((paymentReference == null) ? 0 : paymentReference.hashCode());
		result = prime * result + ((paymentrequestId == null) ? 0 : paymentrequestId.hashCode());
		result = prime * result + ((recipientName == null) ? 0 : recipientName.hashCode());
		result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((swift == null) ? 0 : swift.hashCode());
		result = prime * result + ((synctimestamp == null) ? 0 : synctimestamp.hashCode());
		result = prime * result + ((templateId == null) ? 0 : templateId.hashCode());
		result = prime * result + ((paymentrequestPOId == null) ? 0 : paymentrequestPOId.hashCode());
		result = prime * result + ((beneficiaryType == null) ? 0 : beneficiaryType.hashCode());
		result = prime * result + ((addToExistingFlag == null) ? 0 : addToExistingFlag.hashCode());
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
		BulkPaymentTemplatePODTO other = (BulkPaymentTemplatePODTO) obj;
		if (paymentOrderId == null) {
			if (other.paymentOrderId != null)
				return false;
		} else if (!paymentOrderId.equals(other.paymentOrderId))
			return false;
		return true;
	}	

}