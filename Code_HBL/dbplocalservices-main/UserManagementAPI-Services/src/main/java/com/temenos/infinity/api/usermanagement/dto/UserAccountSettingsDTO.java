package com.temenos.infinity.api.usermanagement.dto;

import java.io.Serializable;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class UserAccountSettingsDTO implements Serializable, DBPDTO {

	private static final long serialVersionUID = 653792502420487768L;

	private String accountID;
	private String nickName;
	private String favouriteStatus;
	private String eStatementEnable;
	private String email;
	private String code;
	private String message;
	private String status;
	private String Id;
	private String errorMessage;

	/**
	 * Constructor using super class
	 */
	public UserAccountSettingsDTO() {
		super();
	}

	public String getAccountID() {
		return accountID;
	}

	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getFavouriteStatus() {
		return favouriteStatus;
	}

	public void setFavouriteStatus(String favouriteStatus) {
		this.favouriteStatus = favouriteStatus;
	}

	public String geteStatementEnable() {
		return eStatementEnable;
	}

	public void seteStatementEnable(String eStatementEnable) {
		this.eStatementEnable = eStatementEnable;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Id == null) ? 0 : Id.hashCode());
		result = prime * result + ((accountID == null) ? 0 : accountID.hashCode());
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((eStatementEnable == null) ? 0 : eStatementEnable.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((errorMessage == null) ? 0 : errorMessage.hashCode());
		result = prime * result + ((favouriteStatus == null) ? 0 : favouriteStatus.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((nickName == null) ? 0 : nickName.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		UserAccountSettingsDTO other = (UserAccountSettingsDTO) obj;
		if (Id == null) {
			if (other.Id != null)
				return false;
		} else if (!Id.equals(other.Id))
			return false;
		if (accountID == null) {
			if (other.accountID != null)
				return false;
		} else if (!accountID.equals(other.accountID))
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (eStatementEnable == null) {
			if (other.eStatementEnable != null)
				return false;
		} else if (!eStatementEnable.equals(other.eStatementEnable))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (errorMessage == null) {
			if (other.errorMessage != null)
				return false;
		} else if (!errorMessage.equals(other.errorMessage))
			return false;
		if (favouriteStatus == null) {
			if (other.favouriteStatus != null)
				return false;
		} else if (!favouriteStatus.equals(other.favouriteStatus))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (nickName == null) {
			if (other.nickName != null)
				return false;
		} else if (!nickName.equals(other.nickName))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}

	public UserAccountSettingsDTO(String accountID, String nickName, String favouriteStatus, String eStatementEnable,
			String email, String code, String message, String status, String id, String errorMessage) {
		super();
		this.accountID = accountID;
		this.nickName = nickName;
		this.favouriteStatus = favouriteStatus;
		this.eStatementEnable = eStatementEnable;
		this.email = email;
		this.code = code;
		this.message = message;
		this.status = status;
		Id = id;
		this.errorMessage=errorMessage;
	}

}