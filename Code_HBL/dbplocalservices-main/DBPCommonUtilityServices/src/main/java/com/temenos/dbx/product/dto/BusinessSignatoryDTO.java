package com.temenos.dbx.product.dto;

import com.dbp.core.api.DBPDTO;

public class BusinessSignatoryDTO implements DBPDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6620055314952376095L;

	private String businessTypeId;
	private String signatoryId;

	public String getBusinessTypeId() {
		return businessTypeId;
	}

	public void setBusinessTypeId(String businessTypeId) {
		this.businessTypeId = businessTypeId;
	}

	public String getSignatoryId() {
		return signatoryId;
	}

	public void setSignatoryId(String signatoryId) {
		this.signatoryId = signatoryId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
