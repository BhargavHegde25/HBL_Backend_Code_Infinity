package com.temenos.dbx.product.bulkpaymentservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kony.dbputilities.util.ErrorCodeEnum;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BulkPaymentPODTO implements DBPDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1993127781601127374L;
	
	private String paymentOrderId;
	private String recordId;
	private String confirmationNumber;
	private String recipientName;
	private String accountNumber;
	private String fromAccount;
	private String bankName;
	private String swift;
	private String featureActionId;
	private String companyId;
	private String roleId;
	private String status;
	private String currency;
	private double amount;
	private String feesPaidBy;
	private String paymentReference;
	private String createdby;
	private String modifiedby;
	private String createdts;
	private String lastmodifiedts;
	private String synctimestamp;
	private String paymentMethod;
	private String accType;
	private String batchMode;
	private String paymentOrderProduct;
	private String paymentStatus;
	private String errorDescription;
	
	private ErrorCodeEnum dbpErrorCode;
    @JsonAlias({"errorMessage", "errmsg", "ErrorMessage"})
	private String dbpErrMsg;
    private String errorDetails;
	private String messageDetails;
	
	public BulkPaymentPODTO() {
		super();
	}
	
	public BulkPaymentPODTO(String paymentOrderId, String recordId, String confirmationNumber, String recipientName,
			String accountNumber, String fromAccount, String bankName, String swift, String featureActionId, String companyId, String roleId,
			String status, String paymentStatus, String currency, double amount, String feesPaidBy, String paymentReference, String createdby,
			String modifiedby, String createdts, String lastmodifiedts, String synctimestamp, String paymentMethod,
			String accType, String batchMode, String paymentOrderProduct, ErrorCodeEnum DbpErrorCode, String DbpErrMsg, String errorDetails, String messageDetails) {
		super();
		this.paymentOrderId = paymentOrderId;
		this.recordId = recordId;
		this.confirmationNumber = confirmationNumber;
		this.recipientName = recipientName;
		this.accountNumber = accountNumber;
		this.fromAccount = fromAccount;
		this.bankName = bankName;
		this.swift = swift;
		this.featureActionId = featureActionId;
		this.companyId = companyId;
		this.roleId = roleId;
		this.status = status;
		this.paymentStatus = paymentStatus;
		this.currency = currency;
		this.amount = amount;
		this.feesPaidBy = feesPaidBy;
		this.paymentReference = paymentReference;
		this.createdby = createdby;
		this.modifiedby = modifiedby;
		this.createdts = createdts;
		this.lastmodifiedts = lastmodifiedts;
		this.synctimestamp = synctimestamp;
		this.paymentMethod = paymentMethod;
		this.accType = accType;
		this.batchMode = batchMode;
		this.paymentOrderProduct = paymentOrderProduct;
		this.dbpErrorCode = DbpErrorCode;
		this.dbpErrMsg = DbpErrMsg;
		this.messageDetails = messageDetails;
		this.errorDetails = errorDetails;
	}
	
	public String getPaymentOrderId() {
		return paymentOrderId;
	}
	public void setPaymentOrderId(String paymentOrderId) {
		this.paymentOrderId = paymentOrderId;
	}
	public String getRecordId() {
		return recordId;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
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
	public String getFromAccount() {
		return fromAccount;
	}

	public void setFromAccount(String fromAccount) {
		this.fromAccount = fromAccount;
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
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
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
	
	public String getBatchMode() {
		return batchMode;
	}

	public void setBatchMode(String batchMode) {
		this.batchMode = batchMode;
	}

	public String getPaymentOrderProduct() {
		return paymentOrderProduct;
	}

	public void setPaymentOrderProduct(String paymentOrderProduct) {
		this.paymentOrderProduct = paymentOrderProduct;
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
	
	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	
	public String getMessageDetails() {
	    return messageDetails;
    }
	
	public void setMessageDetails(String messageDetails) {
        this.messageDetails = messageDetails;
	}
	
	public String geterrorDetails() {
		return errorDetails;
	}

	public void seterrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountNumber == null) ? 0 : accountNumber.hashCode());
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((bankName == null) ? 0 : bankName.hashCode());
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((confirmationNumber == null) ? 0 : confirmationNumber.hashCode());
		result = prime * result + ((createdby == null) ? 0 : createdby.hashCode());
		result = prime * result + ((createdts == null) ? 0 : createdts.hashCode());
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + ((fromAccount == null) ? 0 : fromAccount.hashCode());
		result = prime * result + ((featureActionId == null) ? 0 : featureActionId.hashCode());
		result = prime * result + ((feesPaidBy == null) ? 0 : feesPaidBy.hashCode());
		result = prime * result + ((lastmodifiedts == null) ? 0 : lastmodifiedts.hashCode());
		result = prime * result + ((modifiedby == null) ? 0 : modifiedby.hashCode());
		result = prime * result + ((paymentOrderId == null) ? 0 : paymentOrderId.hashCode());
		result = prime * result + ((paymentReference == null) ? 0 : paymentReference.hashCode());
		result = prime * result + ((recipientName == null) ? 0 : recipientName.hashCode());
		result = prime * result + ((recordId == null) ? 0 : recordId.hashCode());
		result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((paymentStatus == null) ? 0 : paymentStatus.hashCode());
		result = prime * result + ((swift == null) ? 0 : swift.hashCode());
		result = prime * result + ((synctimestamp == null) ? 0 : synctimestamp.hashCode());
		result = prime * result + ((paymentMethod == null) ? 0 : paymentMethod.hashCode());
		result = prime * result + ((accType == null) ? 0 : accType.hashCode());
		result = prime * result + ((batchMode == null) ? 0 : batchMode.hashCode());
		result = prime * result + ((paymentOrderProduct == null) ? 0 : paymentOrderProduct.hashCode());
		result = prime * result + ((errorDescription == null) ? 0 : errorDescription.hashCode());
		result = prime * result + ((dbpErrorCode == null) ? 0 : dbpErrorCode.hashCode());
		result = prime * result + ((dbpErrMsg == null) ? 0 : dbpErrMsg.hashCode());
		result = prime * result + ((messageDetails == null) ? 0 : messageDetails.hashCode());
		result = prime * result + ((errorDetails == null) ? 0 : errorDetails.hashCode());
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
		BulkPaymentPODTO other = (BulkPaymentPODTO) obj;
		if (paymentOrderId == null) {
			if (other.paymentOrderId != null)
				return false;
		} else if (!paymentOrderId.equals(other.paymentOrderId))
			return false;
		return true;
	}
	
	
	
}
