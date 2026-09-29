package com.temenos.dbx.product.approvalservices.dto;

import java.text.ParseException;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.temenos.dbx.product.constants.Constants;
import com.kony.dbputilities.util.HelperMethods;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestHistoryDTO implements DBPDTO{
	
	private static final long serialVersionUID = -777605019546480533L;
	
	private String requestId;
	private String approvalId;
	private String companyId;
	private String status;
	private String comments;
	@JsonAlias({"createdby", "requestActedby"})
	private String createdby;
 	private String action;
 	@JsonAlias({"createdts", "actionts"})
 	private String actionts;
	private String userName;
	private String createdts;
	private String lastmodifiedts;
	private String synctimestamp;
	private String softdeleteflag;
	private String customerName;
	private String customerFullName;
	private String groupName;
	private String groupId;
	private String pendingApprovers;

	public RequestHistoryDTO() {
		super();
	}
	
	public RequestHistoryDTO(String approvalId, String requestId, String companyId, String status, String comments,
			String createdby, String action, String modifiedby, String createdts, String lastmodifiedts,
			String synctimestamp, String softdeleteflag, String actionts, String customerName, String customerFullName, String groupName,
			String pendingApprovers, String groupId) {
		super();
		this.approvalId = approvalId;
		this.requestId = requestId;
		this.companyId = companyId;
		this.status = status;
		this.comments = comments;
		this.createdby = createdby;
		this.action = action;
		this.userName = modifiedby;
		this.createdts = createdts;
		this.lastmodifiedts = lastmodifiedts;
		this.synctimestamp = synctimestamp;
		this.softdeleteflag = softdeleteflag;
		this.actionts = actionts;
		this.customerName = customerName;
		this.customerFullName = customerFullName;
		this.groupName = groupName;
		this.groupId = groupId;
		this.pendingApprovers = pendingApprovers;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerFullName() {
		return customerFullName;
	}

	public void setCustomerFullName(String customerFullName) {
		this.customerFullName = customerFullName;
	}

	public String getActionts() {
		try {
			return HelperMethods.convertDateFormat(actionts, Constants.TIMESTAMP_FORMAT);
		} catch (ParseException e) {
			return null;
		}
	}

	public void setActionts(String actionts) {
		this.actionts = actionts;
	}

	public String getApprovalId() {
		return approvalId;
	}

	public void setApprovalId(String approvalId) {
		this.approvalId = approvalId;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getCreatedby() {
		return createdby;
	}

	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getModifiedby() {
		return userName;
	}

	public void setModifiedby(String modifiedby) {
		this.userName = modifiedby;
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

	public String getSoftdeleteflag() {
		return softdeleteflag;
	}

	public void setSoftdeleteflag(String softdeleteflag) {
		this.softdeleteflag = softdeleteflag;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getPendingApprovers() {
		return pendingApprovers;
	}

	public void setPendingApprovers(String pendingApprovers) {
		this.pendingApprovers = pendingApprovers;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + ((actionts == null) ? 0 : actionts.hashCode());
		result = prime * result + ((approvalId == null) ? 0 : approvalId.hashCode());
		result = prime * result + ((comments == null) ? 0 : comments.hashCode());
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((createdts == null) ? 0 : createdts.hashCode());
		result = prime * result + ((customerFullName == null) ? 0 : customerFullName.hashCode());
		result = prime * result + ((customerName == null) ? 0 : customerName.hashCode());
		result = prime * result + ((lastmodifiedts == null) ? 0 : lastmodifiedts.hashCode());
		result = prime * result + ((createdby == null) ? 0 : createdby.hashCode());
		result = prime * result + ((requestId == null) ? 0 : requestId.hashCode());
		result = prime * result + ((softdeleteflag == null) ? 0 : softdeleteflag.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((synctimestamp == null) ? 0 : synctimestamp.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		result = prime * result + ((pendingApprovers == null) ? 0 : pendingApprovers.hashCode());
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
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
		RequestHistoryDTO other = (RequestHistoryDTO) obj;
		if (approvalId == null) {
			if (other.approvalId != null)
				return false;
		} else if (!approvalId.equals(other.approvalId))
			return false;
		return true;
	}
	
}