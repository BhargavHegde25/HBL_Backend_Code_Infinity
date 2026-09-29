package com.temenos.dbx.product.achservices.dto;

import com.dbp.core.api.DBPDTO;

/**
 * @author KH2381
 * @version 1.0
 */
public class ACHTaxTypeDTO implements DBPDTO {	

	private static final long serialVersionUID = 4119046620046754713L;
	private int id;	
	private String taxType;
	
	public ACHTaxTypeDTO () {
		super();
	}
	
	public ACHTaxTypeDTO ( int id, String taxType ) {
		super();
		this.id = id ; 
		this.taxType = taxType ;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		ACHTaxTypeDTO other = (ACHTaxTypeDTO) obj;
		if (id != other.id)
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
	
	public String getTaxType() {
		return taxType;
	}
	
	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}
	

}
