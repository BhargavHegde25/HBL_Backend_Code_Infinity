package com.temenos.dbx.product.achservices.dto;

import com.dbp.core.api.DBPDTO;

public class BBTemplateRequestTypeDTO implements DBPDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3476324355876065450L;

	// @JsonAlias("TemplateRequestType_id")
	private long templateRequestType_id;

	// @JsonAlias("TemplateRequestTypeName")
	private String templateRequestTypeName;

	// @JsonAlias("TransactionType_id")
	private long transactionType_id;

	public BBTemplateRequestTypeDTO(long templateRequestType_id, String templateRequestTypeName,
			long transactionType_id) {
		super();
		this.templateRequestType_id = templateRequestType_id;
		this.templateRequestTypeName = templateRequestTypeName;
		this.transactionType_id = transactionType_id;
	}

	public BBTemplateRequestTypeDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (templateRequestType_id ^ (templateRequestType_id >>> 32));
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
		BBTemplateRequestTypeDTO other = (BBTemplateRequestTypeDTO) obj;
		if (templateRequestType_id != other.templateRequestType_id)
			return false;
		return true;
	}

	public long getTemplateRequestType_id() {
		return templateRequestType_id;
	}

	public void setTemplateRequestType_id(long templateRequestType_id) {
		this.templateRequestType_id = templateRequestType_id;
	}

	public String getTemplateRequestTypeName() {
		return templateRequestTypeName;
	}

	public void setTemplateRequestTypeName(String templateRequestTypeName) {
		this.templateRequestTypeName = templateRequestTypeName;
	}

	public long getTransactionType_id() {
		return transactionType_id;
	}

	public void setTransactionType_id(long transactionType_id) {
		this.transactionType_id = transactionType_id;
	}

}
