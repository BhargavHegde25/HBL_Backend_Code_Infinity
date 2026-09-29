package com.temenos.infinity.api.accountaggregation.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class TermsDTO implements Serializable, DBPDTO {

	private static final long serialVersionUID = 5075768588721749075L;

	private String versionId;
	private String termsAndConditionsContent;
	private String contentTypeId;
	private String status;
	private String operation;
	private String bankCode;
	private List<String> bank_scope = new ArrayList<>();

	public TermsDTO() {
		super();

	}

	public TermsDTO(String versionId, String termsAndConditionsContent, String contentTypeId, String status,
			String operation, String bankCode, List<String> bank_scope) {
		super();
		this.versionId = versionId;
		this.termsAndConditionsContent = termsAndConditionsContent;
		this.contentTypeId = contentTypeId;
		this.status = status;
		this.operation = operation;
		this.bankCode = bankCode;
		this.bank_scope = bank_scope;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public String getTermsAndConditionsContent() {
		return termsAndConditionsContent;
	}

	public void setTermsAndConditionsContent(String termsAndConditionsContent) {
		this.termsAndConditionsContent = termsAndConditionsContent;
	}

	public String getContentTypeId() {
		return contentTypeId;
	}

	public void setContentTypeId(String contentTypeId) {
		this.contentTypeId = contentTypeId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<String> getBank_scope() {
		return bank_scope;
	}

	public void setBank_scope(List<String> bank_scope) {
		this.bank_scope = bank_scope;
	}
}
