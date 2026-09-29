package com.temenos.dbx.product.dto;

import java.util.Map;

import com.temenos.dbx.product.api.DBPDTOEXT;

public class GroupBusinessTypeDTO implements DBPDTOEXT{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1351676109504942551L;

	@Override
	public boolean persist(Map<String, Object> input, Map<String, Object> headers) {
		return false;
	}

	@Override
	public Object loadDTO(String id) {
		return null;
	}

	@Override
	public Object loadDTO() {
		return null;
	}

}
