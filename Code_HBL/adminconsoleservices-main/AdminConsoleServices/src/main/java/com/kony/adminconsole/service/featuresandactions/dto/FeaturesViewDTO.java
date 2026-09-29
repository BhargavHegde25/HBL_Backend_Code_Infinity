package com.kony.adminconsole.service.featuresandactions.dto;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeaturesViewDTO implements DBPDTO{
	
	private static final long serialVersionUID = 8472269987834614561L;
	
	private String id;
	private String name;
	private String description;
	@JsonAlias({"Type_id"})
	private String typeId;
	@JsonAlias({"Service_Fee"})
	private String serviceFee;
	@JsonAlias({"Status_id"})
	private String statusId;
	private String roleTypeId;
	private String roleTypeName;
	private String languageId;
	private String displayName;
	private String displayDescription;
	private String monetaryActions;
	private String nonMonetaryActions;
	private String companyLegalUnit;
	private Map<String,String> roleTypes;
	private Map<String,Map<String,String>> featureDisplayName;
	
	public FeaturesViewDTO() {
		roleTypes = new HashMap<>();
		featureDisplayName = new HashMap<>();
	}

	public FeaturesViewDTO(String id, String name, String description, String typeId, String serviceFee,
			String statusId, String roleTypeId, String roleTypeName, String languageId, String displayName,
			String displayDescription, String monetaryActions, String nonMonetaryActions, Map<String, String> roleTypes,
			Map<String, Map<String, String>> featureDisplayName) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.typeId = typeId;
		this.serviceFee = serviceFee;
		this.statusId = statusId;
		this.roleTypeId = roleTypeId;
		this.roleTypeName = roleTypeName;
		this.languageId = languageId;
		this.displayName = displayName;
		this.displayDescription = displayDescription;
		this.monetaryActions = monetaryActions;
		this.nonMonetaryActions = nonMonetaryActions;
		this.roleTypes = roleTypes;
		this.featureDisplayName = featureDisplayName;
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

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getServiceFee() {
		return serviceFee;
	}

	public void setServiceFee(String serviceFee) {
		this.serviceFee = serviceFee;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getRoleTypeId() {
		return roleTypeId;
	}

	public void setRoleTypeId(String roleTypeId) {
		this.roleTypeId = roleTypeId;
	}

	public String getLanguageId() {
		return languageId;
	}

	public void setLanguageId(String languageId) {
		this.languageId = languageId;
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

	public String getMonetaryActions() {
		return monetaryActions;
	}

	public void setMonetaryActions(String monetaryActions) {
		this.monetaryActions = monetaryActions;
	}

	public String getNonMonetaryActions() {
		return nonMonetaryActions;
	}

	public void setNonMonetaryActions(String nonMonetaryActions) {
		this.nonMonetaryActions = nonMonetaryActions;
	}

	public Map<String, String> getRoleTypes() {
		return roleTypes;
	}

	public void setRoleTypes(Map<String, String> roleTypes) {
		this.roleTypes = roleTypes;
	}

	public Map<String, Map<String, String>> getFeatureDisplayName() {
		return featureDisplayName;
	}

	public void setFeatureDisplayName(Map<String, Map<String, String>> featureDisplayName) {
		this.featureDisplayName = featureDisplayName;
	}
	
	public void insertRoleType(String id,String name) {
		roleTypes.put(id,name);
	}
	public void insertFeatureDisplayName(String languageId,String id, String name) {
		Map<String,String> displayMap = new HashMap<>();
		displayMap.put(id,name);
		featureDisplayName.put(languageId,displayMap);
	}

	public String getRoleTypeName() {
		return roleTypeName;
	}

	public void setRoleTypeName(String roleTypeName) {
		this.roleTypeName = roleTypeName;
	}
	
	public String getCompanyLegalUnit() {
		return companyLegalUnit;
	}

	public void setCompanyLegalUnit(String companyLegalUnit) {
		this.companyLegalUnit = companyLegalUnit;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((displayDescription == null) ? 0 : displayDescription.hashCode());
		result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
		result = prime * result + ((featureDisplayName == null) ? 0 : featureDisplayName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((languageId == null) ? 0 : languageId.hashCode());
		result = prime * result + ((monetaryActions == null) ? 0 : monetaryActions.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((nonMonetaryActions == null) ? 0 : nonMonetaryActions.hashCode());
		result = prime * result + ((roleTypeId == null) ? 0 : roleTypeId.hashCode());
		result = prime * result + ((roleTypeName == null) ? 0 : roleTypeName.hashCode());
		result = prime * result + ((roleTypes == null) ? 0 : roleTypes.hashCode());
		result = prime * result + ((serviceFee == null) ? 0 : serviceFee.hashCode());
		result = prime * result + ((statusId == null) ? 0 : statusId.hashCode());
		result = prime * result + ((typeId == null) ? 0 : typeId.hashCode());
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
		FeaturesViewDTO other = (FeaturesViewDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
}