package com.kony.adminconsole.service.usermanagement.dto;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InternalUserFeaturesViewDTO implements DBPDTO{
	
private static final long serialVersionUID = 675409876583112398L;
	
	private String id;
	private String name;
	private String description;
	@JsonAlias({"Status_id"})
	private String statusId;
	private String displaySequence;
	private String isPrimary;
	private String languageId;
	private String displayName;
	private String displayDescription;
	private String numberOfActions;

	private Map<String,Map<String,String>> featureDisplayName;

	public InternalUserFeaturesViewDTO() {
		
		featureDisplayName = new HashMap<>();
	}
	
	public InternalUserFeaturesViewDTO(String id, String name, String description, String statusId, String displaySequence, String isPrimary,
			String languageId, String displayName, String displayDescription, String numberOfActions, 
			Map<String, Map<String, String>> featureDisplayName) {
		
		super();
		
		this.id = id;
		this.name = name;
		this.description = description;
		this.statusId = statusId;
		this.displaySequence = displaySequence;
		this.isPrimary = isPrimary;
		this.languageId = languageId;
		this.displayName = displayName;
		this.displayDescription = displayDescription;
		this.numberOfActions = numberOfActions;
		this.featureDisplayName = featureDisplayName;
	}
	
	
	
	
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the statusId
	 */
	public String getStatusId() {
		return statusId;
	}

	/**
	 * @param statusId the statusId to set
	 */
	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	
	/**
	 * @return the displaySequence
	 */
	public String getDisplaySequence() {
		return displaySequence;
	}

	/**
	 * @param displaySequence the displaySequence to set
	 */
	public void setDisplaySequence(String displaySequence) {
		this.displaySequence = displaySequence;
	}

	/**
	 * @return the isPrimary
	 */
	public String getIsPrimary() {
		return isPrimary;
	}

	/**
	 * @param isPrimary the isPrimary to set
	 */
	public void setIsPrimary(String isPrimary) {
		this.isPrimary = isPrimary;
	}

	/**
	 * @return the languageId
	 */
	public String getLanguageId() {
		return languageId;
	}

	/**
	 * @param languageId the languageId to set
	 */
	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the displayDescription
	 */
	public String getDisplayDescription() {
		return displayDescription;
	}

	/**
	 * @param displayDescription the displayDescription to set
	 */
	public void setDisplayDescription(String displayDescription) {
		this.displayDescription = displayDescription;
	}

	/**
	 * @return the numberOfActions
	 */
	public String getNumberOfActions() {
		return numberOfActions;
	}

	/**
	 * @param numberOfActions the numberOfActions to set
	 */
	public void setNumberOfActions(String numberOfActions) {
		this.numberOfActions = numberOfActions;
	}

	/**
	 * @return the featureDisplayName
	 */
	public Map<String, Map<String, String>> getFeatureDisplayName() {
		return featureDisplayName;
	}

	/**
	 * @param featureDisplayName the featureDisplayName to set
	 */
	public void setFeatureDisplayName(Map<String, Map<String, String>> featureDisplayName) {
		this.featureDisplayName = featureDisplayName;
	}

	public void insertFeatureDisplayName(String languageId,String id, String name) {
		Map<String,String> displayMap = new HashMap<>();
		displayMap.put(id,name);
		featureDisplayName.put(languageId,displayMap);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((displayDescription == null) ? 0 : displayDescription.hashCode());
		result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
		result = prime * result + ((displaySequence == null) ? 0 : displaySequence.hashCode());
		result = prime * result + ((featureDisplayName == null) ? 0 : featureDisplayName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((isPrimary == null) ? 0 : isPrimary.hashCode());
		result = prime * result + ((languageId == null) ? 0 : languageId.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((numberOfActions == null) ? 0 : numberOfActions.hashCode());
		result = prime * result + ((statusId == null) ? 0 : statusId.hashCode());
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
		InternalUserFeaturesViewDTO other = (InternalUserFeaturesViewDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		
		return true;
	}

	
	
	
	

}
