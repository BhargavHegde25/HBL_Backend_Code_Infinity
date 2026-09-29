package com.temenos.infinity.api.arrangements.dto;

import com.dbp.core.api.DBPDTO;

public class DocumentStorage implements DBPDTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4736781025195630440L;
	private String userId;
	private String fileId;
	private String documentId;
	private String referenceId;
	private String documentName;
	private String content;
	private String version;
	private String category;
	private String fileInfo;
	private String authorizationKey;
	private String applicationId;
	private String documentType;
	public String documentGroup;
	public String ownerSystemId;
	private String metaDocumentName;
	private String action;
	private String documentStatus;
	private String lastChangeUserId;
	private String fileSize;
	private String newDocumentGroup;
	private String newOwnerSystemId;
	private String isSystemGenerated;
	private String collateralId;
	private String arrangementId;
	
	public String getArrangementId() {
		return arrangementId;
	}
	public void setArrangementId(String arrangementId) {
		this.arrangementId = arrangementId;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public String getIsSystemGenerated() {
		return isSystemGenerated;
	}
	public void setIsSystemGenerated(String isSystemGenerated) {
		this.isSystemGenerated = isSystemGenerated;
	}
	public String getCollateralId() {
		return collateralId;
	}
	public void setCollateralId(String collateralId) {
		this.collateralId = collateralId;
	}
	public String getNewDocumentGroup() {
		return newDocumentGroup;
	}
	public void setNewDocumentGroup(String newDocumentGroup) {
		this.newDocumentGroup = newDocumentGroup;
	}
	public String getNewOwnerSystemId() {
		return newOwnerSystemId;
	}
	public void setNewOwnerSystemId(String newOwnerSystemId) {
		this.newOwnerSystemId = newOwnerSystemId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFileInfo() {
		return fileInfo;
	}
	public void setFileInfo(String fileInfo) {
		this.fileInfo = fileInfo;
	}
	public String getDocumentId() {
		return documentId;
	}
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	public String getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}
	public String getDocumentName() {
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getAuthorizationKey() {
		return authorizationKey;
	}
	public void setAuthorizationKey(String authorizationKey) {
		this.authorizationKey = authorizationKey;
	}
	public String getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
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
	public String getMetaDocumentName() {
		return metaDocumentName;
	}
	public void setMetaDocumentName(String metaDocumentName) {
		this.metaDocumentName = metaDocumentName;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getDocumentStatus() {
		return documentStatus;
	}
	public void setDocumentStatus(String documentStatus) {
		this.documentStatus = documentStatus;
	}
	public String getLastChangeUserId() {
		return lastChangeUserId;
	}
	public void setLastChangeUserId(String lastChangeUserId) {
		this.lastChangeUserId = lastChangeUserId;
	}
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	
}