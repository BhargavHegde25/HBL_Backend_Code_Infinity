package com.temenos.infinity.api.arrangements.resource.impl;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.MWConstants;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.resource.impl.DocumentStorageResourceImpl;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.infinity.api.arrangements.businessdelegate.api.DocumentStorageBusinessDelegate;
import com.temenos.infinity.api.arrangements.dto.DocumentStorage;
import com.temenos.infinity.api.arrangements.dto.DocumentStorageEvidence;
import com.temenos.infinity.api.arrangements.exception.DMSException;
import com.temenos.infinity.api.arrangements.resource.api.DocumentStorageResource;

public class DocumentStorageResourceImpl implements DocumentStorageResource {


	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
		@Override
		public Result downloadDocument(String methodID, Object[] inputArray, DataControllerRequest request,
				DataControllerResponse response) throws Exception {
			Result result = null;
			DocumentStorageBusinessDelegate documentStorageBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(DocumentStorageBusinessDelegate.class);

			try {
				String backend = EnvironmentConfigurationsHandler.getServerAppProperty("DOCUMENTS_BACKEND");
				DocumentStorage downloadResponse = null;
				if("MS".equalsIgnoreCase(backend)) {
					DocumentStorage docInfo = createDocumentStorageDto(request);
					downloadResponse = documentStorageBusinessDelegate.downloadDocument(docInfo, request);
				}else {
					downloadResponse = downloadDocumentFromDB(request);
				}

				if (downloadResponse != null) {
					
					result = JSONToResult.convert(JSONUtils.stringify(downloadResponse));
					
					
					
					Map<String, String> customHeaders = new HashMap<>();
		            customHeaders.put(HttpHeaders.CONTENT_TYPE, "application/pdf");
		            customHeaders.put("Content-Disposition", "attachment; filename=\"" + downloadResponse.getDocumentName() + "\"");
		            response.getHeaders().putAll(customHeaders);
					result.addOpstatusParam(0);
					result.addHttpStatusCodeParam(200);
					
					result.addStringParam("message", "Document downloaded successfully");
				} else {
					result = new Result();
					result.addOpstatusParam(-1);
					result.addHttpStatusCodeParam(500);
					result.addErrMsgParam("Internal Error: Document download failed");
				}
			} catch (IOException ie) {
				alert.prepareError("Exception occured while downloading document", ie).log();
				result = new Result();
				result.addOpstatusParam(-1);
				result.addHttpStatusCodeParam(500);
				result.addErrMsgParam("Internal Error: "+ie.getMessage());
			} catch (DMSException de) {
				result = de.constructResultObject();
			}
			return result;
		}
		
		public Result searchDocument(String methodID, Object[] inputArray, DataControllerRequest request,
				DataControllerResponse response) throws Exception {
			Result result = new Result();
			DocumentStorageBusinessDelegate documentStorageBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(DocumentStorageBusinessDelegate.class);

			try {
				List<DocumentStorage> searchResponse = null;
				String backend = EnvironmentConfigurationsHandler.getServerAppProperty("DOCUMENTS_BACKEND");
				
				if("MS".equalsIgnoreCase(backend)) {
					DocumentStorage docInfo = createDocumentStorageDto(request);
					 searchResponse = documentStorageBusinessDelegate.searchDocument(docInfo, request);
				}else {//DBXDB
					searchResponse = getDocumentsFromDB( request);
				}
					if (searchResponse != null) {
						Map<String, List<DocumentStorage>> documentsList = new HashMap<String, List<DocumentStorage>>();
						documentsList.put("documentsList", searchResponse);
						result = JSONToResult.convert(JSONUtils.stringify(documentsList));
						result.addOpstatusParam(0);
						result.addHttpStatusCodeParam(200);
					} else {
						result = new Result();
						result.addOpstatusParam(-1);
						result.addHttpStatusCodeParam(500);
						result.addErrMsgParam("Internal Error: Document search failed");
					}
				

			} catch (IOException ie) {
				alert.prepareError("Exception occured while searching document", ie).log();
				result = new Result();
				result.addOpstatusParam(-1);
				result.addHttpStatusCodeParam(500);
				result.addErrMsgParam("Internal Error: "+ie.getMessage());
			}catch (DMSException de) {
				result = de.constructResultObject();
			}
			return result;
		}
		
		private List<DocumentStorage> getDocumentsFromDB(DataControllerRequest request) throws Exception{
			
			HashMap<String, String> data = new HashMap<String, String>();
			String arrangementId = request.getParameter("arrangementId");
			
			data.put(Constants.$FILTER, "referenceId  eq " + arrangementId) ;
			Result getStatementData = null;
			try {
				getStatementData = HelperMethods.callApi(request, data, HelperMethods.getHeaders(request),
						URLConstants.DOCUMENTS_GET);
			} catch (HttpCallException e) {
				// TODO Auto-generated catch block
				alert.prepareError(e.getMessage()).log();
			}
			List<DocumentStorage> searchResponse = null;
			Dataset documents = getStatementData.getDatasetById("Documents");
			if (documents.getAllRecords().size() > 0) {
				Result result = new Result();
				Map<String, List<DocumentStorage>> documentsList = new HashMap<String, List<DocumentStorage>>();
				searchResponse = new ArrayList<DocumentStorage>();
				for(Record rec:documents.getAllRecords()) {
					DocumentStorage ds = new DocumentStorage();
					ds.setDocumentId(rec.getParamValueByName("documentId"));
					ds.setDocumentName(rec.getParamValueByName("documentName"));
					ds.setDocumentStatus(rec.getParamValueByName("status"));
					ds.setDocumentType(rec.getParamValueByName("mimeType"));
					ds.setMetaDocumentName("");
					searchResponse.add(ds);
				}
				
				documentsList.put("documentsList", searchResponse);
				result = JSONToResult.convert(JSONUtils.stringify(documentsList));
				result.addOpstatusParam(0);
				result.addHttpStatusCodeParam(200);
			}
			return searchResponse;
		}
		
private DocumentStorage downloadDocumentFromDB(DataControllerRequest request) throws Exception{
			
			DocumentStorage docInfo = null;
			HashMap<String, String> data = new HashMap<String, String>();
			
			String fileId = request.getParameter("documentId");

			String fileType = null;
			String fileName = null;

		

			data.put("documentId", fileId);
			data.put("fieldName", "content");
			Result getStatementFileContent = HelperMethods.callApi(request, data, HelperMethods.getHeaders(request),
					URLConstants.DOCUMENTS_GETBINARY);

			String documentId = request.getParameter("documentId");
			
			data.put(Constants.$FILTER, "documentId  eq " + documentId) ;
			Result getStatementData = null;
			try {
				getStatementData = HelperMethods.callApi(request, data, HelperMethods.getHeaders(request),
						URLConstants.DOCUMENTS_GET);
			} catch (HttpCallException e) {
				// TODO Auto-generated catch block
				alert.prepareError(e.getMessage()).log();
			}

			Dataset documents = getStatementData.getDatasetById("Documents");
			if (documents.getAllRecords().size() > 0) {

				
				String base64 = getStatementFileContent.getParamValueByName("data");
				
				byte[] bytes = Base64.getMimeDecoder().decode(base64);
				
				//String base64 = Base64.getEncoder().encodeToString(data);
				String documentName = documents.getRecord(0).getParamValueByName("documentName").toString();
			 fileId = HelperMethods.getUniqueNumericString(10);
				docInfo = new DocumentStorage();
				docInfo.setDocumentName(documentName);
				docInfo.setFileId(fileId);
				docInfo.setContent(base64);
				MemoryManager.saveIntoCache(fileId, bytes, 120);
				

			}
			return docInfo;
		}
		
		private DocumentStorage createDocumentStorageDto(DataControllerRequest request) {
			DocumentStorage documentStorageDto = new DocumentStorage();
			documentStorageDto.setUserId(request.getParameter("userId"));
			documentStorageDto.setDocumentId(request.getParameter("documentId"));
			documentStorageDto.setReferenceId(request.getParameter("referenceId"));
			documentStorageDto.setCategory(request.getParameter("category"));
			documentStorageDto.setDocumentName(request.getParameter("documentName"));
			documentStorageDto.setContent(request.getParameter("content"));
			documentStorageDto.setVersion(request.getParameter("version"));
			documentStorageDto.setAuthorizationKey(request.getParameter("authorization"));
			documentStorageDto.setOwnerSystemId(request.getParameter("ownerSystemId"));
			documentStorageDto.setApplicationId(request.getParameter("applicationId"));
			documentStorageDto.setDocumentType(request.getParameter("documentType"));
			documentStorageDto.setDocumentGroup(request.getParameter("documentGroup"));
			documentStorageDto.setMetaDocumentName(request.getParameter("metaDocumentName"));
			documentStorageDto.setDocumentStatus(request.getParameter("documentStatus"));
			documentStorageDto.setLastChangeUserId(request.getParameter("lastChangeUserId"));
			documentStorageDto.setAction(request.getParameter("action"));
			documentStorageDto.setNewDocumentGroup(request.getParameter("newDocumentGroup"));
			documentStorageDto.setNewOwnerSystemId(request.getParameter("newOwnerSystemId"));
			documentStorageDto.setIsSystemGenerated(request.getParameter("isSystemGenerated"));
			documentStorageDto.setCollateralId(request.getParameter("collateralId"));
			documentStorageDto.setArrangementId(request.getParameter("arrangementId"));
			String content = request.getParameter("content");
			String documentName = request.getParameter("documentName");
			
			if(StringUtils.isNotBlank(content) && StringUtils.isNotBlank(documentName) && documentStorageDto.getCollateralId() == null) {
				int fileSize = (int)(content.length() * 0.75);
				String[] extensionArray = documentName.split("[.]", 0);
				String fileType = null;
				if (extensionArray.length == 2)
					fileType = extensionArray[1];
				String fileInfo = "{'size':'" + fileSize + "','type':'" + fileType + "'}";
				documentStorageDto.setFileInfo(fileInfo);
			}
			return documentStorageDto;
		}
		private DocumentStorageEvidence createDocumentStorageEvidenceDto(DataControllerRequest request) {
			DocumentStorageEvidence documentStorageEvidenceDto = new DocumentStorageEvidence();
			documentStorageEvidenceDto.setContent(request.getParameter("fileContent"));
			documentStorageEvidenceDto.setEvidenceType(request.getParameter("evidenceType"));
			documentStorageEvidenceDto.setAuthorizationKey(request.getParameter("authorization"));
			documentStorageEvidenceDto.setOwnerSystemId(request.getParameter("ownerSystemId"));
			documentStorageEvidenceDto.setFulfilmentId(request.getParameter("fulfilmentId"));
			documentStorageEvidenceDto.setForRequirements(request.getParameter("forRequirements"));
			documentStorageEvidenceDto.setFileName(request.getParameter("fileName"));
			documentStorageEvidenceDto.setDocumentGroup(request.getParameter("documentGroup"));
	        documentStorageEvidenceDto.setAppEvidenceId(request.getParameter("appEvidenceId"));
			
			return documentStorageEvidenceDto;
		}
		private Map<String, Object> prepareFulfilmentPayload(DataControllerRequest request) {
			Map<String, Object> payloadMap = new HashMap<String, Object>();
			if (request.containsKeyInRequest("applicationId"))
				payloadMap.put("applicationId", request.getParameter("applicationId").toString());
			if (request.containsKeyInRequest("ownerId"))
				payloadMap.put("ownerId", request.getParameter("ownerId").toString());
			if (request.containsKeyInRequest("ownerType"))
				payloadMap.put("ownerType", request.getParameter("ownerType").toString());
			if (request.containsKeyInRequest("description"))
				payloadMap.put("description", request.getParameter("description").toString());
			if (request.containsKeyInRequest("requirements"))
				payloadMap.put("requirements", request.getParameter("requirements"));
			if (request.containsKeyInRequest("ownerSystemId"))
				payloadMap.put("ownerSystemId", request.getParameter("ownerSystemId").toString());
			if (request.containsKeyInRequest("documentGroup"))
				payloadMap.put("documentGroup", request.getParameter("documentGroup").toString());
			if (request.containsKeyInRequest("Authorization"))
				payloadMap.put("Authorization", request.getParameter("Authorization").toString());
			return payloadMap;
		}
	}
