package com.temenos.dbx.product.approvalservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UploadedAttachments implements DBPDTO{
	
	private static final long serialVersionUID = 1L;
	
	@JsonAlias({"paymentFileName", "fileName"})
	private String fileNameValue;
	@JsonAlias({"documentId", "paymentFileID", "fileID"})
	private String fileId;
	
	public UploadedAttachments() {
		
	}

	public UploadedAttachments(String fileNameValue, String fileId) {
		super();
		this.fileNameValue = fileNameValue;
		this.fileId = fileId;
	}

	public String getFileNameValue() {
		return fileNameValue;
	}

	public void setFileNameValue(String fileNameValue) {
		this.fileNameValue = fileNameValue;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fileNameValue == null) ? 0 : fileNameValue.hashCode());
		result = prime * result + ((fileId == null) ? 0 : fileId.hashCode());
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
		UploadedAttachments other = (UploadedAttachments) obj;
		if (fileNameValue != other.fileNameValue)
			return false;
		if (fileId != other.fileId)
			return false;
		
		return true;
	}

}
