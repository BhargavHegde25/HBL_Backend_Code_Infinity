package com.temenos.dbx.product.commons.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LimitGroupDTO implements DBPDTO{

	private static final long serialVersionUID = -92737819867392247L;

	
	@JsonAlias({"id","limitGroupId"})
	private String limitGroupId;
	
	@JsonAlias({"name","limitGroupName","displayName"})
	private String limitGroupName;
	
	@JsonAlias({"description","displayDescription"})
	private String description;
	
	
	public LimitGroupDTO() {
		super();
	}


	public LimitGroupDTO(String limitGroupId, String limitGroupName, String description) {
		super();
		this.limitGroupId = limitGroupId;
		this.limitGroupName = limitGroupName;
		this.description = description;
	}
	
	public String getLimitGroupId() {
		return limitGroupId;
	}


	public void setLimitGroupId(String limitGroupId) {
		this.limitGroupId = limitGroupId;
	}


	public String getLimitGroupName() {
		return limitGroupName;
	}


	public void setLimitGroupName(String limitGroupName) {
		this.limitGroupName = limitGroupName;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((limitGroupId == null) ? 0 : limitGroupId.hashCode());
		result = prime * result + ((limitGroupName == null) ? 0 : limitGroupName.hashCode());
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
		LimitGroupDTO other = (LimitGroupDTO) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (limitGroupId == null) {
			if (other.limitGroupId != null)
				return false;
		} else if (!limitGroupId.equals(other.limitGroupId))
			return false;
		if (limitGroupName == null) {
			if (other.limitGroupName != null)
				return false;
		} else if (!limitGroupName.equals(other.limitGroupName))
			return false;
		return true;
	}

}