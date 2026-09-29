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
public class ApplicationDTO implements DBPDTO{
	
	private static final long serialVersionUID = 374789292798712799L;
	private String timeZoneOffset;
	private String currencyCode;
	//Added as part of  ADP-2810
	private boolean isSelfApprovalEnabled;
	@JsonAlias("stateManagementAvailable")
	private boolean isStateManagementAvailable;

	public String getTimeZoneOffset() {
		return timeZoneOffset;
	}

	public void setTimeZoneOffset(String timeZoneOffset) {
		this.timeZoneOffset = timeZoneOffset;
	}
	
	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	/**
     * Start: Added as part of ADP-2810
     */
	public boolean isSelfApprovalEnabled() {
		return isSelfApprovalEnabled;
	}

	public void setIsSelfApprovalEnabled(boolean isSelfApprovalEnabled) {
		this.isSelfApprovalEnabled = isSelfApprovalEnabled;
	}
	/**
	 * End: Added as part of ADP-2810
	 */
	

	public ApplicationDTO() {
		super();
		this.timeZoneOffset = "UTC+00:00";
		this.isSelfApprovalEnabled = true;
		this.currencyCode = "USD";
		this.isStateManagementAvailable = false;
	}
	
	
	public boolean isStateManagementAvailable() {
		return isStateManagementAvailable;
	}
	
	public void setStateManagementAvailable(boolean isStateManagementAvailable) {
		this.isStateManagementAvailable = isStateManagementAvailable;
	}

	public ApplicationDTO(String timeZoneOffset, String currencyCode, boolean isSelfApprovalEnabled,
			boolean isStateManagementAvailable) {
		super();
		this.timeZoneOffset = timeZoneOffset;
		this.currencyCode = currencyCode;
		this.isSelfApprovalEnabled = isSelfApprovalEnabled;
		this.isStateManagementAvailable = isStateManagementAvailable;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currencyCode == null) ? 0 : currencyCode.hashCode());
		result = prime * result + (isSelfApprovalEnabled ? 1231 : 1237);
		result = prime * result + (isStateManagementAvailable ? 1231 : 1237);
		result = prime * result + ((timeZoneOffset == null) ? 0 : timeZoneOffset.hashCode());
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
		ApplicationDTO other = (ApplicationDTO) obj;
		if (currencyCode == null) {
			if (other.currencyCode != null)
				return false;
		} else if (!currencyCode.equals(other.currencyCode))
			return false;
		if (isSelfApprovalEnabled != other.isSelfApprovalEnabled)
			return false;
		if (isStateManagementAvailable != other.isStateManagementAvailable)
			return false;
		if (timeZoneOffset == null) {
			if (other.timeZoneOffset != null)
				return false;
		} else if (!timeZoneOffset.equals(other.timeZoneOffset))
			return false;
		return true;
	}

	

}
