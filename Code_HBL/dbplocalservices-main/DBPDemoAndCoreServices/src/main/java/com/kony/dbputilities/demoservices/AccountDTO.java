package com.kony.dbputilities.demoservices;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.temenos.dbx.product.dto.LimitDTO;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountDTO implements DBPDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5301648954433399739L;
	
	private String id;
	private String isEnabled;
	private LimitDTO limits;
	
	public AccountDTO() {
		super();
	}
	
	public AccountDTO(String id, String isEnabled, LimitDTO limits) {
		super();
		this.id = id;
		this.isEnabled = isEnabled;
		this.limits = limits;
	}
	
	@Override
	public String toString() {
		String retValue= "\"id\":\"" + this.id + "\", \"isEnabled\":\"" + this.isEnabled + "\"";
		if(this.limits != null) {
			retValue = retValue + ", \"limits\":" + this.limits.toString();
		}
		return "{" +retValue+ "}";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((isEnabled == null) ? 0 : isEnabled.hashCode());
		result = prime * result + ((limits == null) ? 0 : limits.hashCode());
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
		AccountDTO other = (AccountDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isEnabled == null) {
			if (other.isEnabled != null)
				return false;
		} else if (!isEnabled.equals(other.isEnabled))
			return false;
		if (limits == null) {
			if (other.limits != null)
				return false;
		} else if (!limits.equals(other.limits))
			return false;
		return true;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getIsEnabled() {
		return isEnabled;
	}
	
	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	public LimitDTO getLimits() {
		return limits;
	}
	
	public void setLimits(LimitDTO limits) {
		this.limits = limits;
	}
}

