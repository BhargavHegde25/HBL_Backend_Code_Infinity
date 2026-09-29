package com.temenos.dbx.product.signatorygroupservices.dto;


import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.stream.IntStream;

import com.dbp.core.api.DBPDTO;


public class ApprovalModeDTO implements Serializable, DBPDTO {
	
	private static final long serialVersionUID = 6443421325830121880L;
	
	private String coreCustomerId;
	private String contractId;
	private Boolean isGroupLevel;
	private String id;
	private int deletedRecords;
	private int updatedRecords;
	
	

	public ApprovalModeDTO() {
			super();
		}
	    
	    public ApprovalModeDTO(String coreCustomerId, String contractId, String id, Boolean isGroupLevel, int deletedRecords, int updatedRecords) {
			super();
			this.contractId = contractId;
			this.coreCustomerId = coreCustomerId;
			this.id = id;
			this.isGroupLevel = isGroupLevel;
			this.deletedRecords = deletedRecords;
			this.updatedRecords = updatedRecords;
		}
	    
	    public int getUpdatedRecords() {
			return updatedRecords;
		}

		public void setUpdatedRecords(int updatedRecords) {
			this.updatedRecords = updatedRecords;
		}
	    
	    public int getDeletedRecords() {
			return deletedRecords;
		}

		public void setDeletedRecords(int deletedRecords) {
			this.deletedRecords = deletedRecords;
		}
	
	public String getId() {
	return id;
	}
	public void setId(String id) {
	this.id = id;
	}
	public String getCoreCustomerId() {
	return coreCustomerId;
	}
	public void setCoreCustomerId(String coreCustomerId) {
	this.coreCustomerId = coreCustomerId;
	}
	public String getContractId() {
	return contractId;
	}
	public void setContractId(String contractId) {
	this.contractId = contractId;
	}
	public Boolean getIsGroupLevel() {
	return isGroupLevel;
	}
	public void setIsGroupLevel(Boolean isGroupLevel) {
	this.isGroupLevel = isGroupLevel;
	}

	
}