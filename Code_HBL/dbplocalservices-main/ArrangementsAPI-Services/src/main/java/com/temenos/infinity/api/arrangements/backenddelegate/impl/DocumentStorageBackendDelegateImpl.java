package com.temenos.infinity.api.arrangements.backenddelegate.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
//import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.util.JSONUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.exceptions.MiddlewareException;
import com.temenos.infinity.api.arrangements.backenddelegate.api.DocumentStorageBackendDelegate;
import com.temenos.infinity.api.arrangements.exception.DMSException;
import com.temenos.infinity.api.arrangements.constants.Constants;
import com.temenos.infinity.api.arrangements.utils.EnvironmentConfigurationProvider;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;

public class DocumentStorageBackendDelegateImpl implements DocumentStorageBackendDelegate {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	
	
	
	
	private String getDocumentStorageBaseURL(DataControllerRequest request) throws Exception {
		String documentMsBaseUrl;
		try {
			documentMsBaseUrl = EnvironmentConfigurationsHandler.getServerAppProperty("DOCUMENT_MS_BASE_URL", request);
		} catch (MiddlewareException e) {
			alert.prepareError("Exception occured while fetching DOCUMENT_MS_HEADER_OWNER_SYSTEM_ID", e).log();
			return null;
		}
		return documentMsBaseUrl;
	}
	
	private void getAuthorizationkey(HttpMessage httpMessage) {
		String authKey;
		String AWS = "aws";
		String AZURE = "azure";
		try {
			String deploymentPlatform = EnvironmentConfigurationProvider
					.getConfiguredServerProperty("DOCMS_DEPLOYEMENT_PLATFORM");
			if (StringUtils.isNotBlank(deploymentPlatform)) {
				if (StringUtils.equals(deploymentPlatform, AWS)) {
					authKey = EnvironmentConfigurationProvider.getConfiguredServerProperty("DOCMS_AUTHORIZATION_KEY");
					httpMessage.addHeader("x-api-key", authKey);
				}
				if (StringUtils.equals(deploymentPlatform, AZURE)) {
					authKey = EnvironmentConfigurationProvider.getConfiguredServerProperty("DOCMS_AUTHORIZATION_KEY");
					httpMessage.addHeader("x-functions-key", authKey);
				}
			}
		} catch (MiddlewareException e) {
			alert.prepareError("Exception occured while fetching DOCMS_DEPLOYEMENT_PLATFORM", e).log();

		}

	}

	private String getDocumentStorageHeaderRoleId() {
		String roleId;
		try {
			roleId = EnvironmentConfigurationProvider.getConfiguredServerProperty("CORPORATE_LOS_DOCUMENT_MS_HEADER_ROLE_ID");
		} catch (MiddlewareException e) {
			alert.prepareError("Exception occured while fetching DOCUMENT_MS_HEADER_ROLE_ID", e).log();
			return null;
		}
		return roleId;
	}
	
	private String getDocumentStorageHeaderOwnerSystemId() {
		String ownerSystemId;
		try {
			ownerSystemId = EnvironmentConfigurationProvider.getConfiguredServerProperty("CORPORATE_LOS_DOCUMENT_MS_HEADER_OWNER_SYSTEM_ID");
		} catch (MiddlewareException e) {
			alert.prepareError("Exception occured while fetching DOCUMENT_MS_HEADER_OWNER_SYSTEM_ID", e).log();
			return null;
		}
		return ownerSystemId;
	}
	
	private String getDocumentStorageHeaderUserId() {
		String userId;
		try {
			userId = EnvironmentConfigurationProvider.getConfiguredServerProperty("CORPORATE_LOS_DOCUMENT_MS_HEADER_USER_ID");
		} catch (MiddlewareException e) {
			alert.prepareError("Exception occured while fetching DOCUMENT_MS_HEADER_USER_ID", e).log();
			return null;
		}
		return userId;
	}

	@Override
	public Map<String, Object> downloadDocument(Map<String, Object> documentInfo, DataControllerRequest request) throws Exception {
		String documentId = (String) documentInfo.get("documentId");
		String authorizationKey = "";
		String documentGroup = "";
		String ownerSystemId = "";
		if (documentInfo.get("authorizationKey") != null)
			authorizationKey = documentInfo.get("authorizationKey").toString();
		if (documentInfo.get("documentGroup") != null)
			documentGroup = documentInfo.get("documentGroup").toString();
		if (documentInfo.get("ownerSystemId") != null)
			ownerSystemId = documentInfo.get("ownerSystemId").toString();
		
		if (documentId == null)
			return null;
		String documentStorageBaseUrl = getDocumentStorageBaseURL(request);
		if (documentStorageBaseUrl == null)
			return null;

		HttpGet httpGet = new HttpGet(documentStorageBaseUrl+Constants.DOCUMENT_API+Constants.DOCUMENT_APIVERSION+ "/documents/" + documentId);
		addHttpMessageHeaders(httpGet, authorizationKey, documentGroup, ownerSystemId);
		
		CloseableHttpResponse httpResponse = null;
		Map<String, Object> document = null;
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity responseEntity = httpResponse.getEntity();
				String fileName = parseDownloadedFileName(httpResponse.getFirstHeader("Content-Disposition"));
				byte[] documentContent = EntityUtils.toByteArray(responseEntity);
				document = new HashMap<>();
				document.put("data", documentContent);
				document.put("fileName", fileName);
			} else {
				throwDMSException(httpResponse);
			}
		} catch (IOException e) {
			alert.prepareError("Error occured while downloading document: ", e).log();
			throw new DMSException("500", e.getMessage());
		} finally {
			if (httpResponse != null) {
				try {
					httpResponse.close();
				} catch (IOException e) {
					alert.prepareError("Exception occured while closing HttpResponse", e).log();
				}
			}
		}
		return document;
	}
	
	public JSONArray searchDocument(Map<String, Object> documentInfo, DataControllerRequest request) throws Exception {
		JSONArray documentsArray = new JSONArray();
		String fileName = "";
		String referenceId = "";
		String category = "";
		String fileInfo = "";
		String ownerId = "";
		String version = "";
		String documentStorageBaseUrl = getDocumentStorageBaseURL(request);
		String authorizationKey = "";
		String documentGroup = "";
		String ownerSystemId = "";
		String isSystemGenerated = "";
		String collateralId = "";
		String arrangementId ="";
		
		if(documentInfo.get("authorizationKey") != null)
			authorizationKey = documentInfo.get("authorizationKey").toString();
		if (documentInfo.get("documentGroup") != null)
			documentGroup = documentInfo.get("documentGroup").toString();
		if (documentInfo.get("ownerSystemId") != null)
			ownerSystemId = documentInfo.get("ownerSystemId").toString();
		
		String query = "{";
		
		if(documentInfo.get("fileName") != null) {
			fileName = documentInfo.get("fileName").toString();
			if(!fileName.isEmpty()) {
				query += "\"fileName\": \""+fileName+"\",";
			}
		}
		if(documentInfo.get("category") != null) {
			category = documentInfo.get("category").toString();
			if(!category.isEmpty()) {
				query += "\"category\": \""+ category +"\",";
			}
		}
		if(documentInfo.get("referenceId") != null) {
			referenceId = documentInfo.get("referenceId").toString();
			if(!referenceId.isEmpty()) {
				query += "\"referenceId\": \""+ referenceId +"\",";
			}
		}
		if(documentInfo.get("fileInfo") != null) {
			fileInfo = documentInfo.get("fileInfo").toString();
			if(!fileInfo.isEmpty()) {
				query += "\"fileInfo\": \""+fileInfo+"\",";
			}
		}
		if(documentInfo.get("userId") != null) {
			ownerId = documentInfo.get("userId").toString();
			if(!ownerId.isEmpty()) {
				query += "\"ownerId\": \""+ ownerId +"\","; 
			}
		}
		if(documentInfo.get("version") != null) {
			version = documentInfo.get("version").toString();
			if(!version.isEmpty()) {
				query += "\"version\": \""+ version +"\",";
			}
		}
		if (documentInfo.get("isSystemGenerated") != null) {
			isSystemGenerated = documentInfo.get("isSystemGenerated").toString();
			if (!isSystemGenerated.isEmpty()) {
				query += "\"isSystemGenerated\": \"" + isSystemGenerated + "\",";
			}
		}
		
		if (documentInfo.get("collateralId") != null) {
			collateralId = documentInfo.get("collateralId").toString();
			if (!collateralId.isEmpty()) {
				query += "\"collateralId\": \"" + collateralId + "\",";
			}
		}

		if (documentInfo.get("arrangementId") != null) {
			arrangementId = documentInfo.get("arrangementId").toString();
			if (!arrangementId.isEmpty()) {
				query += "\"arrangementId\": \"" + arrangementId + "\",";
			}
		}
		query = query.substring(0,query.length()-1);
		query += "}";
		if(documentStorageBaseUrl == null)
			return null;
		try {
			query = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			alert.prepareError("Error occured while encoding search query: ", e).log();
		}
		HttpGet httpGet = new HttpGet(documentStorageBaseUrl+ Constants.DOCUMENT_API+Constants.DOCUMENT_APIVERSION + "/documents"+ "?search=" +query);
		addHttpMessageHeaders(httpGet,authorizationKey,documentGroup, ownerSystemId);
		
		CloseableHttpResponse httpResponse = null;
		try(CloseableHttpClient httpClient = HttpClients.createDefault()){
			httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity responseEntity = httpResponse.getEntity();
				String response = EntityUtils.toString(responseEntity);
				JSONArray responseObjArray = new JSONArray(response);
		        for (int i = 0; i < responseObjArray.length(); i++)
				{
					JSONObject responseObj = responseObjArray.getJSONObject(i);
					JSONObject fileMetaObj = new JSONObject();
					if(responseObj.has("fileMetadata"))
						fileMetaObj = (JSONObject) responseObj.get("fileMetadata");
					JSONObject documentObj = (JSONObject) responseObj.get("searchMetadata");
					JSONObject document = new JSONObject();
					// documentObj.optString("");
					if (documentObj.has("fileName"))
						document.put("fileName", documentObj.get("fileName"));
					if (responseObj.has("documentId"))
						document.put("documentId", responseObj.get("documentId"));
					if (documentObj.has("fileInfo"))
						document.put("fileInfo", documentObj.get("fileInfo"));
					if (documentObj.has("metaFileName"))
						document.put("metaFileName", documentObj.get("metaFileName"));
					if (documentObj.has("category"))
						document.put("category", documentObj.get("category"));
					if (documentObj.has("referenceId"))
						document.put("referenceId", documentObj.get("referenceId"));
					if (documentObj.has("arrangementId"))
						document.put("arrangementId", documentObj.get("arrangementId"));
					if (documentObj.has("documentStatus"))
						document.put("documentStatus", documentObj.get("documentStatus"));
					if (documentObj.has("documentType"))
						document.put("documentType", documentObj.get("documentType"));
					if (documentObj.has("lastChangeUserId"))
						document.put("lastChangeUserId", documentObj.get("lastChangeUserId"));
					if (documentObj.has("isSystemGenerated"))
						document.put("isSystemGenerated", documentObj.get("isSystemGenerated"));
					if (documentObj.has("collateralId"))
						document.put("collateralId", documentObj.get("collateralId"));
					if(fileMetaObj.has("fileName") && !document.has("fileName"))
						document.put("fileName", fileMetaObj.get("fileName"));
					if(fileMetaObj.has("fileSize"))
						document.put("fileSize", String.valueOf(fileMetaObj.getInt("fileSize")));
					documentsArray.put(document);
				}
			} else {
				throwDMSException(httpResponse);
			}
		} catch (IOException e) {
			alert.prepareError("Error occured while searching documents: ", e).log();
			throw new DMSException("500", e.getMessage());
		} finally {
			if(httpResponse != null) {
				try {
					httpResponse.close();
				} catch (IOException e) {
					alert.prepareError("Error occured while closing HttpResponse: ", e).log();
				}
			}
		}
		return documentsArray;
	}
	
	
	private String parseDownloadedFileName(Header contentDispositionHeader) {
		String fileName = null;
		NameValuePair filenameParameter = null;
		if(contentDispositionHeader != null) {
			HeaderElement[] contentDispositionHeaderElements = contentDispositionHeader.getElements();
			if(contentDispositionHeaderElements.length > 0) {
				if(contentDispositionHeaderElements[0].getParameter(0).getName().equals("filename*")) {
					filenameParameter = contentDispositionHeaderElements[0].getParameterByName("filename*");
				}
				else if(contentDispositionHeaderElements[0].getParameter(0).getName().equals("filename")) {
					filenameParameter = contentDispositionHeaderElements[0].getParameterByName("filename");
				}
				if(filenameParameter != null)
					fileName = filenameParameter.getValue();
			}
			if(fileName.contains("'")) {
				String[] fileNameArray = fileName.split("'");
				fileName = fileNameArray[2];
			} 
		}
		return fileName;
	}
	
	private void addHttpMessageHeaders(HttpMessage httpMessage, String authorizationKey, String documentGroup, String ownerSystemId) {
		httpMessage.addHeader("roleId", getDocumentStorageHeaderRoleId());
		httpMessage.addHeader("ownerSystemId", ownerSystemId);
		httpMessage.addHeader("userId", getDocumentStorageHeaderUserId());
		httpMessage.addHeader("Authorization", authorizationKey);
		httpMessage.addHeader("documentGroup", documentGroup);
		getAuthorizationkey(httpMessage);
		}
	
	private void throwDMSException(HttpResponse httpResponse) throws DMSException{
		String httpStatusCode = ""+httpResponse.getStatusLine().getStatusCode();
		HttpEntity responseEntity = httpResponse.getEntity();
		Header contentTypeHeader = responseEntity.getContentType();
		String contentType = contentTypeHeader.getValue();
		try {
			String errorMessage = "Unknown Error.";
			if(contentType.equals(ContentType.APPLICATION_JSON.getMimeType())) {
				String response;
				response = EntityUtils.toString(responseEntity);
				List<Map> responseList = JSONUtils.parseAsList(response, Map.class);
				errorMessage = errorMessage + " No error message received from backend";
				if(responseList != null && !responseList.isEmpty()) {
					Map<String, String> responseMap = responseList.get(0);
					errorMessage = responseMap.get("code") + " : " + responseMap.get("message");
				}
			}
			throw new DMSException(httpStatusCode, errorMessage);
		} catch(IOException e) {
			alert.prepareError("Error in DocumentStorageBackendDelegateImpl : throwDMSException"+ e.getMessage()).log();
			throw new DMSException(httpStatusCode, e.getMessage());
		}
	}

	
	
	

	private String prepareFulfilmentPayload(Map<String, Object> payloadMap) {
		JSONObject payloadJSON = new JSONObject();
		if(payloadMap.containsKey("applicationId")) payloadJSON.put("applicationId", payloadMap.get("applicationId").toString());
		if(payloadMap.containsKey("ownerId")) payloadJSON.put("ownerId", payloadMap.get("ownerId").toString());
		if(payloadMap.containsKey("ownerType")) payloadJSON.put("ownerType", payloadMap.get("ownerType").toString());
		if(payloadMap.containsKey("description")) payloadJSON.put("description", payloadMap.get("description").toString());
		if(payloadMap.containsKey("requirements")) {
			JSONArray requirements = new JSONArray(payloadMap.get("requirements").toString());
			payloadJSON.put("requirements", requirements);
		}
		return payloadJSON.toString();
	}
	
}
