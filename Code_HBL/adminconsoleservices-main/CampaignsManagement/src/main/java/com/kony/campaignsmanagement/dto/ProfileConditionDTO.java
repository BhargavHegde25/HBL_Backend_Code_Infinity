package com.kony.campaignsmanagement.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kony.adminconsole.service.featuresandactions.dto.FeatureDTO;
import com.kony.adminconsole.utilities.ErrorCodeEnum;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfileConditionDTO implements DBPDTO {

	private static final long serialVersionUID = -6752872430817746149L;

	private String id;
	private String profileConditionId;
	private String conditionExpression;
	private String dataContextId;
	private String dataContextName;
	private String dataContextEndPoints;
	private String dataContextDescription;
	private String dbpErrCode;
	private String dbpErrMsg;
	private ErrorCodeEnum errCode;

	public ProfileConditionDTO() {
		super();
	}

	public ProfileConditionDTO(String profileConditionId, String conditionExpression, String dataContextId,
			String dataContextName, String dataContextEndPoints, String dataContextDescription, String dbpErrCode,
			String dbpErrMsg, ErrorCodeEnum errCode, String id) {
		super();
		this.id = id;
		this.profileConditionId = profileConditionId;
		this.conditionExpression = conditionExpression;
		this.dataContextId = dataContextId;
		this.dataContextName = dataContextName;
		this.dataContextDescription = dataContextDescription;
		this.dataContextEndPoints = dataContextEndPoints;
		this.dbpErrCode = dbpErrCode;
		this.dbpErrMsg = dbpErrMsg;
		this.errCode = errCode;
	}
	
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((profileConditionId == null) ? 0 : profileConditionId.hashCode());
		result = prime * result + ((conditionExpression == null) ? 0 : conditionExpression.hashCode());
		result = prime * result + ((dataContextId == null) ? 0 : dataContextId.hashCode());
		result = prime * result + ((dataContextName == null) ? 0 : dataContextName.hashCode());
		result = prime * result + ((dataContextDescription == null) ? 0 : dataContextDescription.hashCode());
		result = prime * result + ((dataContextEndPoints == null) ? 0 : dataContextEndPoints.hashCode());
		result = prime * result + ((dbpErrCode == null) ? 0 : dbpErrCode.hashCode());
		result = prime * result + ((dbpErrMsg == null) ? 0 : dbpErrMsg.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		FeatureDTO other = (FeatureDTO) obj;
		if (id == null) {
			if (other.getId() != null)
				return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProfileConditionId() {
		return profileConditionId;
	}

	public void setProfileConditionId(String profileConditionId) {
		this.profileConditionId = profileConditionId;
	}

	public String getConditionExpression() {
		return conditionExpression;
	}

	public void setConditionExpression(String conditionExpression) {
		this.conditionExpression = conditionExpression;
	}

	public String getDataContextId() {
		return dataContextId;
	}

	public void setDataContextId(String dataContextId) {
		this.dataContextId = dataContextId;
	}

	public String getDataContextName() {
		return dataContextName;
	}

	public void setDataContextName(String dataContextName) {
		this.dataContextName = dataContextName;
	}

	public String getDataContextEndPoints() {
		return dataContextEndPoints;
	}

	public void setDataContextEndPoints(String dataContextEndPoints) {
		this.dataContextEndPoints = dataContextEndPoints;
	}

	public String getDataContextDescription() {
		return dataContextDescription;
	}

	public void setDataContextDescription(String dataContextDescription) {
		this.dataContextDescription = dataContextDescription;
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
	
	

}
