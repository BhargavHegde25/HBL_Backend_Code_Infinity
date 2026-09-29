package com.temenos.dbx.product.commons.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author KH1755
 * @version 1.0
 * **/
@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PermissionDTO implements DBPDTO{
	
	private static final long serialVersionUID = 974799292698712721L;
	
	@JsonAlias({"Customer_id"})
	private String customer_id;
	
	@JsonAlias({"Account_id"})
	private String account_id;
	
	private String isAllowed;
	
	private boolean isCombinedPermission;
	
	@JsonAlias({"Action_id"})
	private String action_id;
	
	private String isAccountLevel;
	
	@JsonAlias({"Feature_Status_id"})
	private String feature_Status_id;
	
	@JsonAlias({"Feature_id"})
	private String feature_id;
	
	@JsonAlias({"RoleType_id"})
	private String roleType_id;
	
	@JsonAlias({"LimitType_id"})
	private String limitType_id;
	
	private String value;

	public PermissionDTO(String customer_id, String account_id, String isAllowed, String action_id,
			String isAccountLevel, String feature_Status_id, String feature_id, String roleType_id, String limitType_id,
			String value) {
		super();
		this.customer_id = customer_id;
		this.account_id = account_id;
		this.isAllowed = isAllowed;
		this.action_id = action_id;
		this.isAccountLevel = isAccountLevel;
		this.feature_Status_id = feature_Status_id;
		this.feature_id = feature_id;
		this.roleType_id = roleType_id;
		this.limitType_id = limitType_id;
		this.value = value;
		this.isCombinedPermission = false;
	}
	
	public boolean isCombinedPermission() {
		return isCombinedPermission;
	}

	public void setCombinedPermission(boolean isCombinedPermission) {
		this.isCombinedPermission = isCombinedPermission;
	}

	public PermissionDTO() {
		this.isCombinedPermission = false;
	}
	
	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getAccount_id() {
		return account_id;
	}

	public void setAccount_id(String account_id) {
		this.account_id = account_id;
	}


	public String getAction_id() {
		return action_id;
	}

	public void setAction_id(String action_id) {
		this.action_id = action_id;
	}

	public String getFeature_Status_id() {
		return feature_Status_id;
	}

	public void setFeature_Status_id(String feature_Status_id) {
		this.feature_Status_id = feature_Status_id;
	}

	public String getFeature_id() {
		return feature_id;
	}

	public void setFeature_id(String feature_id) {
		this.feature_id = feature_id;
	}

	public String getRoleType_id() {
		return roleType_id;
	}

	public void setRoleType_id(String roleType_id) {
		this.roleType_id = roleType_id;
	}

	public String getLimitType_id() {
		return limitType_id;
	}

	public void setLimitType_id(String limitType_id) {
		this.limitType_id = limitType_id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String getIsAllowed() {
		return isAllowed;
	}

	public void setIsAllowed(String isAllowed) {
		this.isAllowed = isAllowed;
	}

	public String getIsAccountLevel() {
		return isAccountLevel;
	}

	public void setIsAccountLevel(String isAccountLevel) {
		this.isAccountLevel = isAccountLevel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((account_id == null) ? 0 : account_id.hashCode());
		result = prime * result + ((action_id == null) ? 0 : action_id.hashCode());
		result = prime * result + ((customer_id == null) ? 0 : customer_id.hashCode());
		result = prime * result + ((feature_Status_id == null) ? 0 : feature_Status_id.hashCode());
		result = prime * result + ((feature_id == null) ? 0 : feature_id.hashCode());
		result = prime * result + ((isAccountLevel == null) ? 0 : isAccountLevel.hashCode());
		result = prime * result + ((isAllowed == null) ? 0 : isAllowed.hashCode());
		result = prime * result + ((limitType_id == null) ? 0 : limitType_id.hashCode());
		result = prime * result + ((roleType_id == null) ? 0 : roleType_id.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		PermissionDTO other = (PermissionDTO) obj;
		if (account_id == null) {
			if (other.account_id != null)
				return false;
		} else if (!account_id.equals(other.account_id))
			return false;
		if (action_id == null) {
			if (other.action_id != null)
				return false;
		} else if (!action_id.equals(other.action_id))
			return false;
		if (customer_id == null) {
			if (other.customer_id != null)
				return false;
		} else if (!customer_id.equals(other.customer_id))
			return false;
		if (feature_Status_id == null) {
			if (other.feature_Status_id != null)
				return false;
		} else if (!feature_Status_id.equals(other.feature_Status_id))
			return false;
		if (feature_id == null) {
			if (other.feature_id != null)
				return false;
		} else if (!feature_id.equals(other.feature_id))
			return false;
		if (isAccountLevel == null) {
			if (other.isAccountLevel != null)
				return false;
		} else if (!isAccountLevel.equals(other.isAccountLevel))
			return false;
		if (isAllowed == null) {
			if (other.isAllowed != null)
				return false;
		} else if (!isAllowed.equals(other.isAllowed))
			return false;
		if (limitType_id == null) {
			if (other.limitType_id != null)
				return false;
		} else if (!limitType_id.equals(other.limitType_id))
			return false;
		if (roleType_id == null) {
			if (other.roleType_id != null)
				return false;
		} else if (!roleType_id.equals(other.roleType_id))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
}
