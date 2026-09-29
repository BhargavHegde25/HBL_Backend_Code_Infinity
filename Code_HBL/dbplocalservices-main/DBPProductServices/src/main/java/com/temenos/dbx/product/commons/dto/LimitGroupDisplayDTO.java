package com.temenos.dbx.product.commons.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LimitGroupDisplayDTO implements DBPDTO {

	private static final long serialVersionUID = 8878386431550146224L;
	
	private String limitGroupId;
	private String localeId;
	@JsonAlias({"displayName","limitGroupName"})
	private String limitGroupName;
	private String displayDescription;
	
	public String getLimitGroupId() {
		return limitGroupId;
	}
	
	public void setLimitGroupId(String limitGroupId) {
		this.limitGroupId = limitGroupId;
	}
	
	public String getLocaleId() {
		return localeId;
	}
	
	public void setLocaleId(String localeId) {
		this.localeId = localeId;
	}
	
	public String getLimitGroupName() {
		return limitGroupName;
	}
	
	public void setLimitGroupName(String limitGroupName) {
		this.limitGroupName = limitGroupName;
	}
	
	public String getDisplayDescription() {
		return displayDescription;
	}
	
	public void setDisplayDescription(String displayDescription) {
		this.displayDescription = displayDescription;
	}
	
	public LimitGroupDisplayDTO() {
		super();
	}
	
	public LimitGroupDisplayDTO(String limitGroupId, String localeId, String displayName, String displayDescription) {
		super();
		this.limitGroupId = limitGroupId;
		this.localeId = localeId;
		this.limitGroupName = displayName;
		this.displayDescription = displayDescription;
	}
}
