package com.temenos.infinity.api.savingspot.dto;

import java.io.Serializable;

import com.dbp.core.api.DBPDTO;

/**
 * @author muthukumarv- 19459
 *
 */
public class SavingsPotCategoriesDTO implements Serializable, DBPDTO{

	private static final long serialVersionUID = 6443421325830121880L;
	
	private String name;
    private String description;
    
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param value the name to set
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
 
}
