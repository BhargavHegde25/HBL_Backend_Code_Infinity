package com.temenos.dbx.product.achservices.dto;

import com.dbp.core.api.DBPDTO;

public class ACHTaxSubTypeDTO implements DBPDTO {

	public ACHTaxSubTypeDTO() {
		super();
	}

	public ACHTaxSubTypeDTO(int id, String taxSubType, String taxType) {
		super();
		this.id = id;
		this.taxSubType = taxSubType;
		this.taxType = taxType;
	}

	private static final long serialVersionUID = -2719232673717940502L;
	private int id;
	private String taxSubType;
	private String taxType;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((taxSubType == null) ? 0 : taxSubType.hashCode());
		result = prime * result + ((taxType == null) ? 0 : taxType.hashCode());
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
		ACHTaxSubTypeDTO other = (ACHTaxSubTypeDTO) obj;
		if (id != other.id)
			return false;
		if (taxSubType == null) {
			if (other.taxSubType != null)
				return false;
		} else if (!taxSubType.equals(other.taxSubType))
			return false;
		if (taxType == null) {
			if (other.taxType != null)
				return false;
		} else if (!taxType.equals(other.taxType))
			return false;
		return true;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getTaxSubType() {
		return taxSubType;
	}
	
	public void setTaxSubType(String taxSubType) {
		this.taxSubType = taxSubType;
	}
	
	public String getTaxType() {
		return taxType;
	}
	
	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}
	
	
	
}
