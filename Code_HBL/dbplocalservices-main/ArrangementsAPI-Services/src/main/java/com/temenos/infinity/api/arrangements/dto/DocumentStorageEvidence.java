package com.temenos.infinity.api.arrangements.dto;

import com.dbp.core.api.DBPDTO;

public class DocumentStorageEvidence implements DBPDTO {
	
	private String evidenceType;
	private String forRequirements;
	private String documentGroup;
	private String ownerSystemId;
	private String authorizationKey;
	private String fileName;
	private String content;
	private String documentId;
	private String fulfilmentId;
	private String requirementKey;
	private String status;
	private String evidenceId;
	private String appEvidenceId;
	
	
	
	public String getAppEvidenceId() {
		return appEvidenceId;
	}
	public void setAppEvidenceId(String appEvidenceId) {
		this.appEvidenceId = appEvidenceId;
	}
	public String getRequirementKey() {
		return requirementKey;
	}
	public void setRequirementKey(String requirementKey) {
		this.requirementKey = requirementKey;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEvidenceId() {
		return evidenceId;
	}
	public void setEvidenceId(String evidenceId) {
		this.evidenceId = evidenceId;
	} 
	public String getFulfilmentId() {
		return fulfilmentId;
	}
	public void setFulfilmentId(String fulfilmentId) {
		this.fulfilmentId = fulfilmentId;
	}
	public String getEvidenceType() {
		return evidenceType;
	}
	public void setEvidenceType(String evidenceType) {
		this.evidenceType = evidenceType;
	}
    public String getForRequirements() {
		return forRequirements;
	}
	public void setForRequirements(String forRequirements) {
		this.forRequirements = forRequirements;
	}

	public void setDocumentGroup(String documentGroup) {
		this.documentGroup = documentGroup;
	}
	public String getDocumentGroup() {
		return documentGroup;
	}
	public String getOwnerSystemId() {
		return ownerSystemId;
	}
	public void setOwnerSystemId(String ownerSystemId) {
		this.ownerSystemId = ownerSystemId;
		
	}
	public String getAuthorizationKey() {
		return authorizationKey;
	}
	public void setAuthorizationKey(String authorizationKey) {
		this.authorizationKey = authorizationKey;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDocumentId() {
		return documentId;
	}
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

}