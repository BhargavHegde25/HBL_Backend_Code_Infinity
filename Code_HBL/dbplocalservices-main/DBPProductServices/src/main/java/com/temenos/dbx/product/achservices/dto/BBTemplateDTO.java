package com.temenos.dbx.product.achservices.dto;

import java.text.ParseException;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.temenos.dbx.product.constants.Constants;
import com.kony.dbputilities.util.HelperMethods;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author KH2626
 * @version 1.0
 * **/
@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BBTemplateDTO implements DBPDTO{

	private static final long serialVersionUID = 6266805954857542014L;

	//@JsonAlias("TemplateId")
	private long templateId;
	
	@JsonProperty("TemplateId")
	private long tId;
	
	//@JsonAlias("TemplateName")
	private String templateName;
	
	@JsonProperty("TemplateName")
	private String tName;
	
	//@JsonAlias("TemplateDescription")
	private String templateDescription;
	
	@JsonProperty("TemplateDescription")
	private String tDesc;
	
	//@JsonAlias("DebitAccount")
	private String fromAccount;
	
	@JsonProperty("DebitAccount")
	private String dAcct;
	
	//@JsonAlias("EffectiveDate")
	private String effectiveDate;
	
	@JsonProperty("EffectiveDate")
	private String eDate;
	
	//@JsonAlias("MaxAmount")
	private Double maxAmount;
	
	@JsonProperty("MaxAmount")
	private Double maxAmt;
	
	//@JsonAlias("RequestId")
	private long requestId;
	
	@JsonProperty("RequestId")
	private long reqId;
	
	private String records;
	
	@JsonProperty("Records")
	private String rcrds;
	
	private String createdby;
	private String createdts;
	private String status;
	private String updatedBy;
	private String updatedts;
	
	//@JsonAlias("TransactionType_id")
	private long transactionType_id;
	
	@JsonProperty("TransactionType_id")
	private long tTypeId;
	
	//@JsonAlias("TemplateType_id")
	private long templateType_id;
	
	@JsonProperty("TemplateType_id")
	private long tempType_id;
	
	private int softDelete;
	private String actedBy;
	
	//@JsonAlias("TotalAmount")
	private double totalAmount;
	
	@JsonProperty("TotalAmount")
	private double tAmt;
	
	//@JsonAlias("FeatureActionId")
	private String featureActionId;
	
	@JsonProperty("FeatureActionId")
	private String featActionId;
	
	//@JsonAlias("TemplateRequestType_id")
	private long templateRequestType_id;
	
	@JsonProperty("TemplateRequestType_id")
	private long tempRequestType_id;
	
	private String accountName;
	private String userName;
	private String transactionTypeName;
	private String templateTypeName;
	private String templateRequestTypeName;
	private String companyName;
	private String companyId;
	private String roleId;
	private String receivedApprovals;
	private String requiredApprovals;
	
	private String amICreator;
	private String amIApprover;
	
	public BBTemplateDTO() {
		super();
	}	

	public BBTemplateDTO(long templateId, long tId, String templateName, String tName, String templateDescription,
			String tDesc, String fromAccount, String dAcct, String effectiveDate, String eDate, Double maxAmount,
			Double maxAmt, long requestId, long reqId, String createdby, String createdts, String status,
			String updatedBy, String updatedts, long transactionType_id, long tTypeId, long templateType_id,
			long tempType_id, int softDelete, String actedBy, double totalAmount, double tAmt, String featureActionId,
			String featActionId, long templateRequestType_id, long tempRequestType_id, String accountName,
			String userName, String transactionTypeName, String templateTypeName, String templateRequestTypeName,
			String companyName, String companyId, String roleId, String receivedApprovals, String requiredApprovals, String records, String amICreator, String amIApprover) {
		super();
		this.templateId = templateId;
		this.tId = tId;
		this.templateName = templateName;
		this.tName = tName;
		this.templateDescription = templateDescription;
		this.tDesc = tDesc;
		this.fromAccount = fromAccount;
		this.dAcct = dAcct;
		this.effectiveDate = effectiveDate;
		this.eDate = eDate;
		this.maxAmount = maxAmount;
		this.maxAmt = maxAmt;
		this.requestId = requestId;
		this.reqId = reqId;
		this.createdby = createdby;
		this.createdts = createdts;
		this.status = status;
		this.updatedBy = updatedBy;
		this.updatedts = updatedts;
		this.transactionType_id = transactionType_id;
		this.tTypeId = tTypeId;
		this.templateType_id = templateType_id;
		this.tempType_id = tempType_id;
		this.softDelete = softDelete;
		this.actedBy = actedBy;
		this.totalAmount = totalAmount;
		this.tAmt = tAmt;
		this.featureActionId = featureActionId;
		this.featActionId = featActionId;
		this.templateRequestType_id = templateRequestType_id;
		this.tempRequestType_id = tempRequestType_id;
		this.accountName = accountName;
		this.userName = userName;
		this.transactionTypeName = transactionTypeName;
		this.templateTypeName = templateTypeName;
		this.templateRequestTypeName = templateRequestTypeName;
		this.companyName = companyName;
		this.companyId = companyId;
		this.roleId = roleId;
		this.receivedApprovals = receivedApprovals;
		this.requiredApprovals = requiredApprovals;
		this.records = records;
		this.amIApprover = amIApprover;
		this.amICreator = amICreator;
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

	public String getTemplateTypeName() {
		return templateTypeName;
	}

	public void setTemplateTypeName(String templateTypeName) {
		this.templateTypeName = templateTypeName;
	}

	public String getTemplateRequestTypeName() {
		return templateRequestTypeName;
	}

	public void setTemplateRequestTypeName(String templateRequestTypeName) {
		this.templateRequestTypeName = templateRequestTypeName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplateDescription() {
		return templateDescription;
	}

	public void setTemplateDescription(String templateDescription) {
		this.templateDescription = templateDescription;
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

	public Double getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(Double maxAmount) {
		this.maxAmount = maxAmount;
	}

	public long getRequestId() {
		return requestId;
	}

	public void setRequestId(long requestId) {
		this.requestId = requestId;
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

	public long getTemplateRequestType_id() {
		return templateRequestType_id;
	}

	public void setTemplateRequestType_id(long templateRequestType_id) {
		this.templateRequestType_id = templateRequestType_id;
	}

	public void settId(long tId) {
		this.tId = 0;
		this.setTemplateId(tId);
	}

	public void settName(String tName) {
		this.tName = null;
		this.setTemplateName(tName);
	}

	public void settDesc(String tDesc) {
		this.tDesc = null;
		this.setTemplateDescription(tDesc);
	}
	
	public void setdAcct(String dAcct) {
		this.dAcct = null;
		this.setFromAccount(dAcct);
	}

	public void seteDate(String eDate) {
		this.eDate = null;
		this.setEffectiveDate(eDate);
	}

	public void setMaxAmt(Double maxAmt) {
		this.maxAmt = 0.0;
		this.setMaxAmount(maxAmt);
	}
	
	public void setReqId(long reqId) {
		this.reqId = 0;
		this.setRequestId(reqId);
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
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

	public void settTypeId(long tTypeId) {
		this.tTypeId = 0;
		this.setTransactionType_id(tTypeId);
	}

	public void setTempType_id(long tempType_id) {
		this.tempType_id = 0;
		this.setTemplateType_id(tempType_id);
	}

	public int getSoftDelete() {
		return softDelete;
	}

	public void setSoftDelete(int softDelete) {
		this.softDelete = softDelete;
	}

	public String getActedBy() {
		return actedBy;
	}

	public void setActedBy(String actedBy) {
		this.actedBy = actedBy;
	}

	public void settAmt(double tAmt) {
		this.tAmt = 0.0;
		this.setTotalAmount(tAmt);
	}

	public void setFeatActionId(String featActionId) {
		this.featActionId = null;
		this.setFeatureActionId(featActionId);
	}

	public void setTempRequestType_id(long tempRequestType_id) {
		this.templateRequestType_id = 0;
		this.setTemplateRequestType_id(tempRequestType_id);
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getRecords() {
		return records;
	}

	public void setRecords(String records) {
		this.records = records;
	}

	public String getRcrds() {
		return this.getRecords();
	}

	public void setRcrds(String rcrds) {
		this.rcrds = null;
		this.setRecords(rcrds);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountName == null) ? 0 : accountName.hashCode());
		result = prime * result + ((actedBy == null) ? 0 : actedBy.hashCode());
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
		result = prime * result + ((companyName == null) ? 0 : companyName.hashCode());
		result = prime * result + ((createdby == null) ? 0 : createdby.hashCode());
		result = prime * result + ((createdts == null) ? 0 : createdts.hashCode());
		result = prime * result + ((effectiveDate == null) ? 0 : effectiveDate.hashCode());
		result = prime * result + ((featureActionId == null) ? 0 : featureActionId.hashCode());
		result = prime * result + ((fromAccount == null) ? 0 : fromAccount.hashCode());
		result = prime * result + ((maxAmount == null) ? 0 : maxAmount.hashCode());
		result = prime * result + ((receivedApprovals == null) ? 0 : receivedApprovals.hashCode());
		result = prime * result + ((requiredApprovals == null) ? 0 : requiredApprovals.hashCode());
		result = prime * result + softDelete;
		result = prime * result + ((templateDescription == null) ? 0 : templateDescription.hashCode());
		result = prime * result + (int) (templateId ^ (templateId >>> 32));
		result = prime * result + ((templateName == null) ? 0 : templateName.hashCode());
		result = prime * result + ((templateRequestTypeName == null) ? 0 : templateRequestTypeName.hashCode());
		result = prime * result + (int) (templateRequestType_id ^ (templateRequestType_id >>> 32));
		result = prime * result + ((templateTypeName == null) ? 0 : templateTypeName.hashCode());
		result = prime * result + (int) (templateType_id ^ (templateType_id >>> 32));
		long temp;
		temp = Double.doubleToLongBits(totalAmount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((transactionTypeName == null) ? 0 : transactionTypeName.hashCode());
		result = prime * result + (int) (transactionType_id ^ (transactionType_id >>> 32));
		result = prime * result + ((updatedBy == null) ? 0 : updatedBy.hashCode());
		result = prime * result + ((updatedts == null) ? 0 : updatedts.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		result = prime * result + ((records == null) ? 0 : records.hashCode());
		result = prime * result + ((amIApprover == null) ? 0 : amIApprover.hashCode());
		result = prime * result + ((amICreator == null) ? 0 : amICreator.hashCode());
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
		BBTemplateDTO other = (BBTemplateDTO) obj;
		if (templateId != other.templateId)
			return false;
		return true;
	}
}