package com.temenos.infinity.tradefinanceservices.dto;

import java.io.Serializable;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LetterOfCreditsActionDTO implements Serializable, DBPDTO{

	private static final long serialVersionUID = 653792342420487768L;
	
	private String requestId;
    private String signatoryAction;
    private String message;
    private String comments;
    private String serviceReqStatus;
    private Integer dbpErrCode;
    private String dbpErrMsg;
    
	public LetterOfCreditsActionDTO() {
		super();
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getSignatoryAction() {
		return signatoryAction;
	}

	public void setSignatoryAction(String signatoryAction) {
		this.signatoryAction = signatoryAction;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getServiceReqStatus() {
		return serviceReqStatus;
	}

	public void setServiceReqStatus(String serviceReqStatus) {
		this.serviceReqStatus = serviceReqStatus;
	}
	
	public Integer getDbpErrCode() {
		return dbpErrCode;
	}

	public void setDbpErrCode(Integer dbpErrCode) {
		this.dbpErrCode = dbpErrCode;
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
		result = prime * result + ((comments == null) ? 0 : comments.hashCode());
		result = prime * result + ((dbpErrCode == null) ? 0 : dbpErrCode.hashCode());
		result = prime * result + ((dbpErrMsg == null) ? 0 : dbpErrMsg.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((requestId == null) ? 0 : requestId.hashCode());
		result = prime * result + ((serviceReqStatus == null) ? 0 : serviceReqStatus.hashCode());
		result = prime * result + ((signatoryAction == null) ? 0 : signatoryAction.hashCode());
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
		LetterOfCreditsActionDTO other = (LetterOfCreditsActionDTO) obj;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (dbpErrCode == null) {
			if (other.dbpErrCode != null)
				return false;
		} else if (!dbpErrCode.equals(other.dbpErrCode))
			return false;
		if (dbpErrMsg == null) {
			if (other.dbpErrMsg != null)
				return false;
		} else if (!dbpErrMsg.equals(other.dbpErrMsg))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (requestId == null) {
			if (other.requestId != null)
				return false;
		} else if (!requestId.equals(other.requestId))
			return false;
		if (serviceReqStatus == null) {
			if (other.serviceReqStatus != null)
				return false;
		} else if (!serviceReqStatus.equals(other.serviceReqStatus))
			return false;
		if (signatoryAction == null) {
			if (other.signatoryAction != null)
				return false;
		} else if (!signatoryAction.equals(other.signatoryAction))
			return false;
		return true;
	}

	public LetterOfCreditsActionDTO(String requestId, String signatoryAction, String message, String comments,
			String serviceReqStatus, Integer dbpErrCode, String dbpErrMsg) {
		super();
		this.requestId = requestId;
		this.signatoryAction = signatoryAction;
		this.message = message;
		this.comments = comments;
		this.serviceReqStatus = serviceReqStatus;
		this.dbpErrCode = dbpErrCode;
		this.dbpErrMsg = dbpErrMsg;
	}	
}
