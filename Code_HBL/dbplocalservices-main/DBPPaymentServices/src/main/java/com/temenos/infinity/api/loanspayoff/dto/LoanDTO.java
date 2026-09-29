package com.temenos.infinity.api.loanspayoff.dto;

import java.io.Serializable;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class LoanDTO implements Serializable, DBPDTO {

	private static final long serialVersionUID = 6597925078705187769L;
	private String status;
	private String backendOverride;
	private String backendError;

	public LoanDTO(String status, String backendOverride, String backendError) {
		super();
		this.status = status;
		this.backendOverride = backendOverride;
		this.backendError = backendError;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBackendOverride() {
		return backendOverride;
	}

	public void setBackendOverride(String backendOverride) {
		this.backendOverride = backendOverride;
	}

	public String getBackendError() {
		return backendError;
	}

	public void setBackendError(String backendError) {
		this.backendError = backendError;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((backendError == null) ? 0 : backendError.hashCode());
		result = prime * result + ((backendOverride == null) ? 0 : backendOverride.hashCode());
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
		LoanDTO other = (LoanDTO) obj;
		if (backendError == null) {
			if (other.backendError != null)
				return false;
		} else if (!backendError.equals(other.backendError))
			return false;
		if (backendOverride == null) {
			if (other.backendOverride != null)
				return false;
		} else if (!backendOverride.equals(other.backendOverride))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}

	public LoanDTO() {
		super();
	}

}
