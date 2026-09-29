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
public class BBActedRequestDTO implements DBPDTO{
	
	private static final long serialVersionUID = -77760501235380533L;
	
	private String requestId;
	private String approvalId;
	private String comments;
 	private String action;
 	@JsonAlias({"createdts"})
 	private String approvalDate;
	private String companyLegalUnit;
	
	public BBActedRequestDTO() {
		super();
	}
	
	public BBActedRequestDTO(String requestId, String approvalId, String comments, String action, String approvalDate,
			String companyLegalUnit) {
		super();
		this.requestId = requestId;
		this.approvalId = approvalId;
		this.comments = comments;
		this.action = action;
		this.approvalDate = approvalDate;
		this.companyLegalUnit = companyLegalUnit;
	}

	public String getApprovalDate() {
		try {
			return HelperMethods.convertDateFormat(approvalDate, Constants.TIMESTAMP_FORMAT);
		} catch (ParseException e) {
			return null;
		}
	}

	public void setApprovalDate(String actionts) {
		this.approvalDate = actionts;
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

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	public String getCompanyLegalUnit() {
		return companyLegalUnit;
	}

	public void setCompanyLegalUnit(String companyLegalUnit) {
		this.companyLegalUnit = companyLegalUnit;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + ((approvalDate == null) ? 0 : approvalDate.hashCode());
		result = prime * result + ((approvalId == null) ? 0 : approvalId.hashCode());
		result = prime * result + ((comments == null) ? 0 : comments.hashCode());
		result = prime * result + ((requestId == null) ? 0 : requestId.hashCode());
		result = prime * result + ((companyLegalUnit == null) ? 0 : companyLegalUnit.hashCode());
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
		BBActedRequestDTO other = (BBActedRequestDTO) obj;
		if (approvalId == null) {
			if (other.approvalId != null)
				return false;
		} else if (!approvalId.equals(other.approvalId))
			return false;
		if (companyLegalUnit == null) {
			if (other.companyLegalUnit != null)
				return false;
		} else if (!companyLegalUnit.equals(other.companyLegalUnit))
			return false;
		return true;
	}
	
}