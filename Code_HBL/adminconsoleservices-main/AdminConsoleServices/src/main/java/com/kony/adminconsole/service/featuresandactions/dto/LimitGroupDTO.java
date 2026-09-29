package com.kony.adminconsole.service.featuresandactions.dto;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LimitGroupDTO implements DBPDTO {
	
	private static final long serialVersionUID = 8472269187834613901L;
	
	private String id;
	private String name;
	private String description;
	private String localeId;
	private String displayName;
	private String displayDescription;
	private String companyLegalUnit;
	private Map<String,Map<String,String>> limitGroupDisplayName;
	
	public LimitGroupDTO() {
		limitGroupDisplayName = new HashMap<>();
	}

	public LimitGroupDTO(String id, String name, String description, String localeId, String displayName,
			String displayDescription, Map<String, Map<String, String>> limitGroupDisplayName) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.localeId = localeId;
		this.displayName = displayName;
		this.displayDescription = displayDescription;
		this.limitGroupDisplayName = limitGroupDisplayName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
	
	public String getCompanyLegalUnit() {
        return companyLegalUnit; 
    }

    public void setCompanyLegalUnit(String companyLegalUnit) {
        this.companyLegalUnit = companyLegalUnit;
    }

	public Map<String, Map<String, String>> getLimitGroupDisplayName() {
		return limitGroupDisplayName;
	}

	public void setLimitGroupDisplayName(Map<String, Map<String, String>> limitGroupDisplayName) {
		this.limitGroupDisplayName = limitGroupDisplayName;
	}

	public void insertLimitDisplayName(String localeId,String id, String name) {
		Map<String,String> displayMap = new HashMap<>();
		displayMap.put(id,name);
		this.limitGroupDisplayName.put(localeId,displayMap);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((displayDescription == null) ? 0 : displayDescription.hashCode());
		result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((limitGroupDisplayName == null) ? 0 : limitGroupDisplayName.hashCode());
		result = prime * result + ((localeId == null) ? 0 : localeId.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}