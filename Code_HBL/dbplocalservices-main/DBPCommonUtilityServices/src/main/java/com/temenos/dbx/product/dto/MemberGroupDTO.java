package com.temenos.dbx.product.dto;

import com.dbp.core.api.DBPDTO;

public class MemberGroupDTO implements DBPDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 655465035939275480L;

	private String id;
	private String typeId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
