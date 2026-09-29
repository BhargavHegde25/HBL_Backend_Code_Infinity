package com.temenos.dbx.product.transactionservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author KH2144
 * @version 1.0
 * implements {@link DBPDTO}
 * 
 *
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class AccountTypesDTO implements DBPDTO{

	/** AccountTypesDTO is a serializable class, to serialize the getting and setting.
     */
	private static final long serialVersionUID = 2257170259553504996L;
	
	/**
     * These are fields in accounttype Table
     * Generate  Constructor from Superclass , Constructor using Fields ,Getters and Setters for the following fields
     */
	@JsonProperty("TypeID")
	private String typeid;
	@JsonProperty("TypeDescription")
	private String typeDescription;
	
	public AccountTypesDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AccountTypesDTO(String typeid, String typeDescription) {
		super();
		this.typeid = typeid;
		this.typeDescription = typeDescription;
	}

	public String getTypeid() {
		return typeid;
	}

	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}

	public String getTypeDescription() {
		return typeDescription;
	}

	public void setTypeDescription(String typeDescription) {
		this.typeDescription = typeDescription;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((typeDescription == null) ? 0 : typeDescription.hashCode());
		result = prime * result + ((typeid == null) ? 0 : typeid.hashCode());
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
		AccountTypesDTO other = (AccountTypesDTO) obj;
		if (typeDescription == null) {
			if (other.typeDescription != null)
				return false;
		} else if (!typeDescription.equals(other.typeDescription))
			return false;
		if (typeid == null) {
			if (other.typeid != null)
				return false;
		} else if (!typeid.equals(other.typeid))
			return false;
		return true;
	}
	
}