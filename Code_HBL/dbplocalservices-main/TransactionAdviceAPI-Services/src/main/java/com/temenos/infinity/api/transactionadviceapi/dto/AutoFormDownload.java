package com.temenos.infinity.api.transactionadviceapi.dto;

import java.io.Serializable;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class AutoFormDownload implements Serializable, DBPDTO {

	private static final long serialVersionUID = 659792503420487769L;
	private String documentId;
	private String revision;

	public AutoFormDownload() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getRevision() {
		return revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	public AutoFormDownload(String documentId, String revision) {
		super();
		this.documentId = documentId;
		this.revision = revision;
	}

}
