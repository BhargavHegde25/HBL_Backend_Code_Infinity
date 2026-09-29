package com.kony.adminconsole.service.usermanagement.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kony.adminconsole.utilities.ErrorCodeEnum;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InternalUserFeaturesDTO implements DBPDTO{

private static final long serialVersionUID = 4098654324678129826L;
	
	private String id;
	@JsonAlias({"Feature_id"})
	private String featureId;
	@JsonAlias({"Status_id"})
	private String statusId;
	private String localeId;
	private String displayName;
	private String displayDescription;
	private String lastmodifiedts;
	private String modifiedby;
	private String dbpErrCode;
	private String dbpErrMsg;
	private ErrorCodeEnum errCode;
	
	public InternalUserFeaturesDTO() {
		super();
	}

	public InternalUserFeaturesDTO(String id, String featureId, String statusId, String localeId,
			String displayName, String displayDescription, String lastmodifiedts, String modifiedby, String dbpErrCode,
			String dbpErrMsg, ErrorCodeEnum errCode) {
		super();
		this.id = id;
		this.featureId = featureId;
		this.statusId = statusId;
		this.localeId = localeId;
		this.displayName = displayName;
		this.displayDescription = displayDescription;
		this.lastmodifiedts = lastmodifiedts;
		this.modifiedby = modifiedby;
		this.dbpErrCode = dbpErrCode;
		this.dbpErrMsg = dbpErrMsg;
		this.errCode = errCode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFeatureId() {
		return featureId;
	}

	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getLocaleId() {
		return localeId;
	}

	public void setLocaleId(String localeId) {
		this.localeId = localeId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayDescription() {
		return displayDescription;
	}

	public void setDisplayDescription(String displayDescription) {
		this.displayDescription = displayDescription;
	}

	public String getLastmodifiedts() {
		return lastmodifiedts;
	}

	public void setLastmodifiedts(String lastmodifiedts) {
		this.lastmodifiedts = lastmodifiedts;
	}

	public String getModifiedby() {
		return modifiedby;
	}

	public void setModifiedby(String modifiedby) {
		this.modifiedby = modifiedby;
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

	public ErrorCodeEnum getErrCode() {
		return errCode;
	}

	public void setErrCode(ErrorCodeEnum errCode) {
		this.errCode = errCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dbpErrCode == null) ? 0 : dbpErrCode.hashCode());
		result = prime * result + ((dbpErrMsg == null) ? 0 : dbpErrMsg.hashCode());
		result = prime * result + ((displayDescription == null) ? 0 : displayDescription.hashCode());
		result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
		result = prime * result + ((errCode == null) ? 0 : errCode.hashCode());
		result = prime * result + ((featureId == null) ? 0 : featureId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lastmodifiedts == null) ? 0 : lastmodifiedts.hashCode());
		result = prime * result + ((localeId == null) ? 0 : localeId.hashCode());
		result = prime * result + ((modifiedby == null) ? 0 : modifiedby.hashCode());
		result = prime * result + ((statusId == null) ? 0 : statusId.hashCode());
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
		InternalUserFeaturesDTO other = (InternalUserFeaturesDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
