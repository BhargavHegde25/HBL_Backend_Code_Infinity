package com.temenos.dbx.product.transactionservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author KH2301
 * @version 1.0
 * implements {@link DBPDTO}
 * 
 *
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class CountryListDTO implements DBPDTO{

	/**
	 * CountryListDTO is a serializable class, to serialize the getting and setting.
	 */
	private static final long serialVersionUID = 7812293300851692687L;

	/**
	 * These are fields in country Table
	 * Generate  Constructor from Superclass , Constructor using Fields ,Getters and Setters for the following fields
	 */
	@JsonProperty("Name")
	private String name;
	private String id;
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public CountryListDTO(String name, String id) {
		super();
		this.name = name;
		this.id = id;
	}
	public CountryListDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		CountryListDTO other = (CountryListDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

	
}
