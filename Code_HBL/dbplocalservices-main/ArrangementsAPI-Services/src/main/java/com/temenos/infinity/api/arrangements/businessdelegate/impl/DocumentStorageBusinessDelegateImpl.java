package com.temenos.infinity.api.arrangements.businessdelegate.impl;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;
import com.temenos.infinity.api.arrangements.config.ServerConfigurations;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.arrangements.backenddelegate.api.DocumentStorageBackendDelegate;
import com.temenos.infinity.api.arrangements.businessdelegate.api.DocumentStorageBusinessDelegate;
import com.temenos.infinity.api.arrangements.dto.DocumentStorage;
import com.temenos.infinity.api.arrangements.dto.DocumentStorageEvidence;
import com.temenos.infinity.api.arrangements.exception.DMSException;
import com.temenos.infinity.api.arrangements.utils.CommonUtils;

public class DocumentStorageBusinessDelegateImpl implements DocumentStorageBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	
	
	@Override
	public DocumentStorage downloadDocument(DocumentStorage documentInfo, DataControllerRequest request) throws Exception {
		DocumentStorageBackendDelegate documentStorageBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(DocumentStorageBackendDelegate.class);
		Map<String, Object> documentDetails = new HashMap<>();
		final int SIZE_OF_RANDOM_GENERATED_STRING = 10;
		documentDetails.put("documentId", documentInfo.getDocumentId());
		documentDetails.put("authorizationKey",ServerConfigurations.VIEWDOC_AUTH_KEY.getValue());
		documentDetails.put("documentGroup",ServerConfigurations.DOCUMENTGROUP.getValue());
		documentDetails.put("ownerSystemId",ServerConfigurations.OWNERSYSTEMID.getValue());
		Map<String, Object> backendResponse = documentStorageBackendDelegate.downloadDocument(documentDetails, request);
		DocumentStorage docInfo = null;
		if (backendResponse != null && backendResponse.get("data") != null && backendResponse.get("fileName") != null) {
			byte[] data = (byte[]) backendResponse.get("data");
			String base64 = Base64.getEncoder().encodeToString(data);
			String documentName = backendResponse.get("fileName").toString();
			String fileId = HelperMethods.getUniqueNumericString(SIZE_OF_RANDOM_GENERATED_STRING);
			docInfo = new DocumentStorage();
			docInfo.setDocumentName(documentName);
			docInfo.setFileId(fileId);
			docInfo.setContent(base64);
			MemoryManager.saveIntoCache(fileId, data, 120);
		}
		return docInfo;
	}
	
	
	
	public List<DocumentStorage> searchDocument(DocumentStorage documentInfo, DataControllerRequest request) throws Exception {
		DocumentStorageBackendDelegate documentStorageBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(DocumentStorageBackendDelegate.class);
		Map<String, Object> documentDetails = new HashMap<>();
		if (documentInfo.getReferenceId() != null) {
			documentDetails.put("referenceId", documentInfo.getReferenceId());
		}
		if (documentInfo.getUserId() != null) {
			documentDetails.put("userId", documentInfo.getUserId());
		}
		if (documentInfo.getFileInfo() != null) {
			documentDetails.put("fileInfo", documentInfo.getFileInfo());
		}
		if (documentInfo.getCategory() != null) {
			documentDetails.put("category", documentInfo.getCategory());
		}
		if (documentInfo.getVersion() != null) {
			documentDetails.put("version", documentInfo.getVersion());
		}
		if (documentInfo.getDocumentName() != null) {
			documentDetails.put("fileName", documentInfo.getDocumentName());
		}
		if (documentInfo.getDocumentId() != null) {
			documentDetails.put("documentId", documentInfo.getDocumentId());
		}
		if (documentInfo.getIsSystemGenerated() != null) {
			documentDetails.put("isSystemGenerated", documentInfo.getIsSystemGenerated());
		}
		if (documentInfo.getCollateralId() != null) {
			documentDetails.put("collateralId", documentInfo.getCollateralId());
		}
		if (documentInfo.getArrangementId() != null) {
			documentDetails.put("arrangementId", documentInfo.getArrangementId());
		}
		documentDetails.put("authorizationKey",ServerConfigurations.VIEWDOC_AUTH_KEY.getValue());
		documentDetails.put("documentGroup",ServerConfigurations.DOCUMENTGROUP.getValue());
		documentDetails.put("ownerSystemId",ServerConfigurations.OWNERSYSTEMID.getValue());
		JSONArray documentsList = documentStorageBackendDelegate.searchDocument(documentDetails, request);
		List<DocumentStorage> docInfo = null;
		if (documentsList != null) {
			docInfo = new ArrayList<DocumentStorage>();
			for (int i = 0; i < documentsList.length(); i++) {
				DocumentStorage docStorage = new DocumentStorage();
				JSONObject object = documentsList.getJSONObject(i);
				Iterator key = object.keys();
				while (key.hasNext()) {
					String k = key.next().toString();
					switch (k) {
					case "documentId":
						docStorage.setDocumentId(object.getString(k));
						break;
					case "fileName":
						docStorage.setDocumentName(object.getString(k));
						break;
					case "fileInfo":
						docStorage.setFileInfo(object.getString(k));
						break;
					case "metaFileName":
						docStorage.setMetaDocumentName(object.getString(k));
						break;
					case "category":
						docStorage.setCategory(object.getString(k));
						break;
					case "documentStatus":
						docStorage.setDocumentStatus(object.getString(k));
						break;
					case "documentType":
						docStorage.setDocumentType(object.getString(k));
						break;
					case "lastChangeUserId":
						docStorage.setLastChangeUserId(object.getString(k));
						break;
					case "referenceId":
						docStorage.setReferenceId(object.getString(k));
						break;
					case "fileSize":
						docStorage.setFileSize(object.getString(k));
						break;
					case "isSystemGenerated":
						docStorage.setIsSystemGenerated(object.getString(k));
						break;
					case "collateralId":
						docStorage.setCollateralId(object.getString(k));
						break;
					case "arrangementId":
						docStorage.setArrangementId(object.getString(k));
					
					}
				}
				docInfo.add(docStorage);
			}
		}
		return docInfo;
	}
}
