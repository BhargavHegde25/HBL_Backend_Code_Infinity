package com.temenos.dbx.product.achservices.dto;

import java.text.ParseException;
import java.util.List;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.temenos.dbx.product.constants.Constants;
import com.kony.dbputilities.util.HelperMethods;


/**
 * 
 * @author KH2317
 * @version 1.0
 * implements {@link DBPDTO}
 * 
 */

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ACHTransactionDTO implements DBPDTO {

	private static final long serialVersionUID = 3731832727896499245L;

	//@JsonAlias({"Transaction_id"})
	private String transaction_id;
	
	@JsonProperty("Transaction_id")
	private String tId;
	
	//@JsonAlias({"DebitAccount"})
	private String fromAccount;
	
	@JsonProperty("DebitAccount")
	private String dAcc;
	
	//@JsonAlias({"EffectiveDate"})
	private String effectiveDate;
	
	@JsonProperty("EffectiveDate")
	private String eDate;	
	
	//@JsonAlias({"Request_id"})
	private String requestId;
	
	@JsonProperty("Request_id")
	private long rId;
	
	private String createdby;
	
	private String createdts;
	
	//@JsonAlias({"MaxAmount"})
	private double maxAmount;
	
	@JsonProperty("MaxAmount")
	private double mAmnt;
	
	//@JsonAlias({"Status"})
	private String status;
	
	@JsonProperty("Status")
	private String sts;
	
	private List<ACHTransactionRecordDTO> transactionRecords;
	
	//@JsonAlias({"TransactionType_id"})
	private long transactionType_id;
	
	@JsonProperty("TransactionType_id")
	private long trTypeId;
	
	//@JsonAlias({"TemplateType_id"})
	private long templateType_id;
	
	@JsonProperty("TemplateType_id")
	private long teTypeId;
	
	//@JsonAlias({"Company_id"})
	private String companyId;
	private String roleId;
	
	@JsonProperty("Company_id")
	private String cId;
	
	//@JsonAlias({"TemplateRequestType_id"})
	private long templateRequestType_id;
	
	@JsonProperty("TemplateRequestType_id")
	private long teReqTypeId;
	
	@JsonProperty("TemplateName")
	private String tName;
	
	private int softDelete;
	private String templateName;
	private String confirmationNumber;
	private String actedBy;
	
	//@JsonAlias({"Template_id"})
	private long template_id;
	
	@JsonProperty("Template_id")
	private long teId;
	
	private String updatedts;	
	
	//@JsonAlias({"TotalAmount"})
	private double totalAmount;
	
	@JsonProperty("TotalAmount")
	private double tAmnt;
	
	private String featureActionId;
	
	@JsonAlias({"ReferenceID", "referenceID", "referenceId"})
	private String referenceID;
	
	private String accountName;
	private String templateTypeName;
	private String companyName;
	private String requestCreatedby;
	private String userName;
	private String transactionTypeName;
	private String templateRequestTypeName;
	private String receivedApprovals;
	private String requiredApprovals;
	private String transactionCurrency;
	private String transactionAmount;
	private String serviceCharge;
	
	
	private String amICreator;
	private String amIApprover;
	
	@JsonAlias({"errorMessage", "errmsg"})
	private String dbpErrMsg;
	
	private String dbpErrCode;
	
	public ACHTransactionDTO() {
		super();
	}

	public ACHTransactionDTO(String transaction_id, String fromAccount, String effectiveDate, String requestId,
			String createdby, String createdts, double maxAmount, String status, long transactionType_id,
			long templateType_id, String companyId, String roleId, long templateRequestType_id, int softDelete, String templateName,
			String confirmationNumber, String actedBy, long template_id, String updatedts, double totalAmount,
			String featureActionId, String referenceID, String accountName, String templateTypeName, String companyName,
			String requestCreatedby, String userName, String transactionTypeName, String templateRequestTypeName,
			String receivedApprovals, String requiredApprovals, String amICreator, String amIApprover, List<ACHTransactionRecordDTO> transactionRecords, String dbpErrMsg,String dbpErrCode,
			String transactionCurrency,String transactionAmount, String serviceCharge) {
		super();
		this.transaction_id = transaction_id;
		this.fromAccount = fromAccount;
		this.effectiveDate = effectiveDate;
		this.requestId = requestId;
		this.createdby = createdby;
		this.createdts = createdts;
		this.maxAmount = maxAmount;
		this.status = status;
		this.transactionType_id = transactionType_id;
		this.templateType_id = templateType_id;
		this.companyId = companyId;
		this.roleId = roleId;
		this.templateRequestType_id = templateRequestType_id;
		this.softDelete = softDelete;
		this.templateName = templateName;
		this.confirmationNumber = confirmationNumber;
		this.actedBy = actedBy;
		this.template_id = template_id;
		this.updatedts = updatedts;
		this.totalAmount = totalAmount;
		this.featureActionId = featureActionId;
		this.referenceID = referenceID;
		this.accountName = accountName;
		this.templateTypeName = templateTypeName;
		this.companyName = companyName;
		this.requestCreatedby = requestCreatedby;
		this.userName = userName;
		this.transactionTypeName = transactionTypeName;
		this.templateRequestTypeName = templateRequestTypeName;
		this.receivedApprovals = receivedApprovals;
		this.requiredApprovals = requiredApprovals;
		this.amIApprover = amIApprover;
		this.amICreator = amICreator;
		this.transactionRecords = transactionRecords;
		this.dbpErrMsg = dbpErrMsg;
		this.dbpErrCode= dbpErrCode;
		this.transactionCurrency = transactionCurrency;
		this.transactionAmount = transactionAmount;
		this.serviceCharge = serviceCharge;
	}
	
	public String getDbpErrMsg() {
		return dbpErrMsg;
	}

	public void setDbpErrMsg(String dbpErrMsg) {
		this.dbpErrMsg = dbpErrMsg;
	}
	
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getAmICreator() {
		return amICreator;
	}

	public void setAmICreator(String amICreator) {
		this.amICreator = amICreator;
	}

	public String getAmIApprover() {
		return amIApprover;
	}

	public void setAmIApprover(String amIApprover) {
		this.amIApprover = amIApprover;
	}

	public String getReceivedApprovals() {
		return receivedApprovals;
	}

	public void setReceivedApprovals(String receivedApprovals) {
		this.receivedApprovals = receivedApprovals;
	}

	public String getRequiredApprovals() {
		return requiredApprovals;
	}

	public void setRequiredApprovals(String requiredApprovals) {
		this.requiredApprovals = requiredApprovals;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	public List<ACHTransactionRecordDTO> getTransactionRecords() {
		return transactionRecords;
	}

	public void setTransactionRecords(List<ACHTransactionRecordDTO> records) {
		this.transactionRecords = records;
	}

	public String getTemplateTypeName() {
		return templateTypeName;
	}

	public void setTemplateTypeName(String templateTypeName) {
		this.templateTypeName = templateTypeName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getRequestCreatedby() {
		return requestCreatedby;
	}

	public void setRequestCreatedby(String requestCreatedby) {
		this.requestCreatedby = requestCreatedby;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTransactionTypeName() {
		return transactionTypeName;
	}

	public void setTransactionTypeName(String transactionTypeName) {
		this.transactionTypeName = transactionTypeName;
	}

	public String getTemplateRequestTypeName() {
		return templateRequestTypeName;
	}

	public void setTemplateRequestTypeName(String templateRequestTypeName) {
		this.templateRequestTypeName = templateRequestTypeName;
	}

	public void setTemplateRequestType_id(long templateRequestType_id) {
		this.templateRequestType_id = templateRequestType_id;
	}

	public String getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}
	public String getFromAccount() {
		return fromAccount;
	}
	public void setFromAccount(String fromAccount) {
		this.fromAccount = fromAccount;
	}
	public String getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getCreatedby() {
		return createdby;
	}
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}
	public String getCreatedts() {
		try {
			return HelperMethods.convertDateFormat(createdts, Constants.TIMESTAMP_FORMAT);
		} catch (ParseException e) {
			return null;
		}
	}
	public void setCreatedts(String createdts) {
		this.createdts = createdts;
	}
	public double getMaxAmount() {
		return maxAmount;
	}
	public void setMaxAmount(double maxAmount) {
		this.maxAmount = maxAmount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public long getTransactionType_id() {
		return transactionType_id;
	}
	public void setTransactionType_id(long transactionType_id) {
		this.transactionType_id = transactionType_id;
	}
	public long getTemplateType_id() {
		return templateType_id;
	}
	public void setTemplateType_id(long templateType_id) {
		this.templateType_id = templateType_id;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public long getTemplateRequestType_id() {
		return templateRequestType_id;
	}
	public void setTemplateRequestType_Id(long templateRequestType_id) {
		this.templateRequestType_id = templateRequestType_id;
	}
	public int getSoftDelete() {
		return softDelete;
	}
	public void setSoftDelete(int softDelete) {
		this.softDelete = softDelete;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getConfirmationNumber() {
		return confirmationNumber;
	}
	public void setConfirmationNumber(String confirmationNumber) {
		this.confirmationNumber = confirmationNumber;
	}
	public String getActedBy() {
		return actedBy;
	}
	public void setActedBy(String actedBy) {
		this.actedBy = actedBy;
	}
	public long getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(long template_id) {
		this.template_id = template_id;
	}
	public String getUpdatedts() {
		try {
			return HelperMethods.convertDateFormat(updatedts, Constants.TIMESTAMP_FORMAT);
		} catch (ParseException e) {
			return null;
		}
	}
	public void setUpdatedts(String updatedts) {
		this.updatedts = updatedts;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getFeatureActionId() {
		return featureActionId;
	}
	public void setFeatureActionId(String featureActionId) {
		this.featureActionId = featureActionId;
	}
	
	public String getReferenceID() {
		return this.referenceID;
	}

	public void setReferenceID(String referenceID) {
		this.referenceID = referenceID;
	}	
	
	public void settId(String tId) {
		this.tId = null;
		this.setTransaction_id(tId);
	}

	public void setdAcc(String dAcc) {
		this.dAcc = null;
		this.setFromAccount(dAcc);
	}

	public void seteDate(String eDate) {
		this.eDate = null;
		this.setEffectiveDate(eDate);
	}

	public void setrId(String rId) {
		this.rId = 0;
		this.setRequestId(rId);
	}

	public void setmAmnt(double mAmnt) {
		this.mAmnt = 0;
		this.setMaxAmount(mAmnt);
	}

	public void setSts(String sts) {
		this.sts = null;
		this.setStatus(sts);
	}

	public void setTrTypeId(long trTypeId) {
		this.trTypeId = 0;
		this.setTransactionType_id(trTypeId);
	}
	
	public void setTeTypeId(long teTypeId) {
		this.teTypeId = 0;
		this.setTemplateType_id(teTypeId);
	}

	public void setcId(String cId) {
		this.cId = null;
		this.setCompanyId(cId);
	}

	public void setTeReqTypeId(long teReqTypeId) {
		this.teReqTypeId = 0;
		this.setTemplateRequestType_Id(teReqTypeId);
	}

	public void setTeId(long teId) {
		this.teId = 0;
		this.setTemplate_id(teId);
	}
	
	public void settAmnt(double tAmnt) {
		this.tAmnt = 0;
		this.setTotalAmount(tAmnt);
	}

	public void settName(String tName) {
		this.setTemplateName(tName);
		this.tName = null;
	}

	public String getDbpErrCode() {
		return dbpErrCode;
	}

	public void setDbpErrCode(String dbpErrCode) {
		this.dbpErrCode = dbpErrCode;
	}
	
	public String getTransactionCurrency() {
		return transactionCurrency;
	}

	public void setTransactionCurrency(String transactionCurrency) {
		this.transactionCurrency = transactionCurrency;
	}

	public String getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(String transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getServiceCharge() {
		return serviceCharge;
	}

	public void setServiceCharge(String serviceCharge) {
		this.serviceCharge = serviceCharge;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((referenceID == null) ? 0 : referenceID.hashCode());
		result = prime * result + ((accountName == null) ? 0 : accountName.hashCode());
		result = prime * result + ((actedBy == null) ? 0 : actedBy.hashCode());
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
		result = prime * result + ((companyName == null) ? 0 : companyName.hashCode());
		result = prime * result + ((confirmationNumber == null) ? 0 : confirmationNumber.hashCode());
		result = prime * result + ((createdby == null) ? 0 : createdby.hashCode());
		result = prime * result + ((createdts == null) ? 0 : createdts.hashCode());
		result = prime * result + ((transactionRecords == null) ? 0 : transactionRecords.hashCode());
		result = prime * result + ((effectiveDate == null) ? 0 : effectiveDate.hashCode());
		result = prime * result + ((dbpErrMsg == null) ? 0 : dbpErrMsg.hashCode());
		result = prime * result + ((dbpErrCode == null) ? 0 : dbpErrCode.hashCode());
		result = prime * result + ((featureActionId == null) ? 0 : featureActionId.hashCode());
		result = prime * result + ((fromAccount == null) ? 0 : fromAccount.hashCode());
		long temp;
		temp = Double.doubleToLongBits(maxAmount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((receivedApprovals == null) ? 0 : receivedApprovals.hashCode());
		result = prime * result + ((requestCreatedby == null) ? 0 : requestCreatedby.hashCode());
		result = prime * result + ((requestId == null) ? 0 : requestId.hashCode());
		result = prime * result + ((requiredApprovals == null) ? 0 : requiredApprovals.hashCode());
		result = prime * result + softDelete;
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((templateName == null) ? 0 : templateName.hashCode());
		result = prime * result + ((templateRequestTypeName == null) ? 0 : templateRequestTypeName.hashCode());
		result = prime * result + (int) (templateRequestType_id ^ (templateRequestType_id >>> 32));
		result = prime * result + ((templateTypeName == null) ? 0 : templateTypeName.hashCode());
		result = prime * result + (int) (templateType_id ^ (templateType_id >>> 32));
		result = prime * result + (int) (template_id ^ (template_id >>> 32));
		temp = Double.doubleToLongBits(totalAmount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((transactionTypeName == null) ? 0 : transactionTypeName.hashCode());
		result = prime * result + (int) (transactionType_id ^ (transactionType_id >>> 32));
		result = prime * result + ((transaction_id == null) ? 0 : transaction_id.hashCode());
		result = prime * result + ((updatedts == null) ? 0 : updatedts.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		result = prime * result + ((amIApprover == null) ? 0 : amIApprover.hashCode());
		result = prime * result + ((amICreator == null) ? 0 : amICreator.hashCode());
		result = prime * result + ((transactionCurrency == null) ? 0 : transactionCurrency.hashCode());
		result = prime * result + ((transactionAmount == null) ? 0 : transactionAmount.hashCode());
		result = prime * result + ((serviceCharge == null) ? 0 : serviceCharge.hashCode());
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
		ACHTransactionDTO other = (ACHTransactionDTO) obj;
		if (transaction_id != other.transaction_id)
			return false;
		return true;
	}
	
}
