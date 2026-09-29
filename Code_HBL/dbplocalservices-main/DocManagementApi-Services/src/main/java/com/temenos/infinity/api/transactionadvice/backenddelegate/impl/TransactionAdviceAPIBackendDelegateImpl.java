package com.temenos.infinity.api.transactionadvice.backenddelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.temenos.infinity.api.commons.constants.FabricConstants;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.commons.invocation.Executor;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.transactionadvice.backenddelegate.api.TransactionAdviceAPIBackendDelegate;
import com.temenos.infinity.api.transactionadvice.config.TransactionAdviceAPIServices;
import com.temenos.infinity.api.transactionadvice.constants.ErrorCodeEnum;
import com.temenos.dbx.product.utils.DTOConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.ServerConfigurations;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.exceptions.HttpCallException;

public class TransactionAdviceAPIBackendDelegateImpl implements TransactionAdviceAPIBackendDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	public static final String ERROR_CODE_KEY = "dbpErrCode";
	public static final String ERROR_MESSAGE_KEY = "dbpErrMsg";
	public static final String UNAUTHORIZED = "Unauthorized";
	public static volatile JSONObject tokenInfo;

	@Override
	public byte[] download(String documentId, String revision, String authToken) {
		byte[] serviceResponse = null;
		try {
			Map<String, Object> inputMap = new HashMap<>();
			Map<String, Object> headerMap = new HashMap<>();
			inputMap.put("documentId", documentId);
			inputMap.put("revision", revision);
			// Login Operation
			JSONObject loginServiceResponseJSON = getToken(authToken,false);
			headerMap=getHeadersFromLoginService(loginServiceResponseJSON,authToken);
			diagnostic.prepareDebug("Header=" + headerMap.toString()).log();
			serviceResponse = Executor.invokePassThroughServiceAndGetBytes(
					TransactionAdviceAPIServices.TRANSACTIONADVICEJSON_DOWNLOAD, inputMap, headerMap);
			String serviceResponseStr=new String(serviceResponse);
			diagnostic.prepareDebug("ServiceResponse=" + serviceResponse).log();
			if (serviceResponseStr.contains(UNAUTHORIZED)) {
				diagnostic.prepareDebug("login token expired genarating again").log();
				loginServiceResponseJSON = getToken(authToken,true);
				headerMap=getHeadersFromLoginService(loginServiceResponseJSON,authToken);
				serviceResponse = Executor.invokePassThroughServiceAndGetBytes(
						TransactionAdviceAPIServices.TRANSACTIONADVICEJSON_DOWNLOAD, inputMap, headerMap);
			}
			if (serviceResponse == null)
				alert.prepareError("efs file download failed").log();
		} catch (ApplicationException e) {
			alert.prepareError("efs file download failed" + e).log();
		} catch (Exception e) {
			alert.prepareError("efs file download failed" + e).log();
		}
		return serviceResponse;
	}
/*
 * Old search method
	@Override
	public ArrayList<JSONObject> search(DataControllerRequest request,HashMap<String, Object> paramMap, String authToken)
			throws ApplicationException {
		ArrayList<JSONObject> docsList = new ArrayList<JSONObject>();
		try {
			Map<String, Object> inputMap = paramMap;
			String serviceResponse = new String();
			JSONObject loginServiceResponseJSON;
			Map<String, Object> headerMap = new HashMap<>();
//			 String accountNnumberConfigValue = EnvironmentConfigurationsHandler.getValue("EFS_ACCOUNT_NUMBER");
//			 String customerNumberConfigValue = EnvironmentConfigurationsHandler.getValue("EFS_CUSTOMERID");
			
			 String corecustomerId = getBackendId(request,inputMap.get("customerNumber").toString());
//			 if (accountNnumberConfigValue != null && accountNnumberConfigValue != ""
//			 		&& customerNumberConfigValue != null && customerNumberConfigValue != "") {
//				 inputMap.replace("accountNumber", accountNnumberConfigValue);
//				 inputMap.replace("customerNumber", customerNumberConfigValue);
			 if(corecustomerId != null && corecustomerId != "") {
			     inputMap.replace("customerNumber",corecustomerId);
				// Login Operation
				loginServiceResponseJSON = getToken(authToken,false);
				headerMap=getHeadersFromLoginService(loginServiceResponseJSON,authToken);
				serviceResponse = Executor.invokePassThroughServiceAndGetString(
						TransactionAdviceAPIServices.TRANSACTIONADVICEJSON_SEARCH, inputMap, headerMap);
				diagnostic.prepareDebug("EFS doc search service response=" + serviceResponse).log();
				if (serviceResponse.contains(UNAUTHORIZED)) {
					diagnostic.prepareDebug("login token expired genarating again").log();
					loginServiceResponseJSON = getToken(authToken,true);
					headerMap=getHeadersFromLoginService(loginServiceResponseJSON,authToken);
					serviceResponse = Executor.invokePassThroughServiceAndGetString(
							TransactionAdviceAPIServices.TRANSACTIONADVICEJSON_SEARCH, inputMap, headerMap);
				}
				docsList = getSearchResults(serviceResponse);
				return docsList;
			 }
			 else {
				 alert.prepareError("core customerNumber are not present").log();
				 return docsList;
			 }
//			 } else {
//			 	alert.prepareError("efs accountNumber customerNumber are not present").log();
//			 	return docsList;
//			 }
		} catch (ApplicationException e) {
			alert.prepareError(e.toString()).log();
			throw e;
		} catch (Exception e) {
			alert.prepareError("efs search failed" + e).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_25001);
		}
	}
*/
	@Override
	public ArrayList<JSONObject> search(DataControllerRequest request,HashMap<String, Object> paramMap, String authToken)
			throws ApplicationException {
		ArrayList<JSONObject> docsList = new ArrayList<JSONObject>();
		try {
			Map<String, Object> inputMap = paramMap;
			String serviceResponse = new String();
			JSONObject loginServiceResponseJSON;
			Map<String, Object> headerMap = new HashMap<>();
		    String corecustomerId = getBackendId(request,inputMap.get("customerNumber").toString());
			 if(corecustomerId != null && corecustomerId != "") {
			     inputMap.replace("customerNumber",corecustomerId);
			     String DM_API_KEY = ServerConfigurations.DM_API_KEY.getValueIfExists();
			     diagnostic.prepareDebug("TransactionAdviceAPIBackendDelegateImpl Search API Key :"+DM_API_KEY).log();
			     if(DM_API_KEY!=null && !"".equals(DM_API_KEY)) {
			    	 Map<String, Object> headersMap = new HashMap<>();
			    	 headersMap.put("DM-API-KEY", DM_API_KEY);
			    	 serviceResponse = Executor.invokePassThroughServiceAndGetString(
								TransactionAdviceAPIServices.TRANSACTIONADVICEJSON_SEARCH, inputMap, headersMap);
			    	 diagnostic.prepareDebug("TransactionAdviceAPIBackendDelegateImpl Search API Response:"+serviceResponse).log();
			     }else {
			    	 alert.prepareError("TransactionAdviceAPIBackendDelegateImpl Search API Key(DM_API_KEY) is not found :").log();
			    	 throw new ApplicationException(ErrorCodeEnum.ERR_25007);
			     }
				docsList = getSearchResults(serviceResponse);
				return docsList;
			 }
			 else {
				 alert.prepareError("core customerNumber are not present").log();
				 return docsList;
			 }
		} catch (ApplicationException e) {
			alert.prepareError(e.toString()).log();
			throw e;
		} catch (Exception e) {
			alert.prepareError("efs search failed" + e).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_25001);
		}
	}
	
	
	String getBackendId(DataControllerRequest request, String customerId) {
		String filter = DTOConstants.CUSTOMER_ID + DBPUtilitiesConstants.EQUAL + customerId
		+ DBPUtilitiesConstants.AND + DTOConstants.BACKENDTYPE + DBPUtilitiesConstants.EQUAL;
	String ARRANGEMENTS_BACKEND = ServerConfigurations.ARRANGEMENTS_BACKEND.getValueIfExists();
	String btype = DTOConstants.T24;
	if (StringUtils.isNotBlank(ARRANGEMENTS_BACKEND)) {
		if (ARRANGEMENTS_BACKEND.equals("MOCK")) {
			btype=DTOConstants.CORE;
		}
	}
	filter+=btype;
	

		Result result = new Result();
		try {
			result = HelperMethods.callGetApi(request, filter, HelperMethods.getHeaders(request),
					URLConstants.BACKENDIDENTIFIER_GET);
		} catch (HttpCallException e) {
			alert.prepareError("Caught exception while getting backend identifier: ", e).log();
		}
		return HelperMethods.getFieldValue(result, DTOConstants.BACKENDID);
    }
	
	private Map<String, Object> getHeadersFromLoginService(JSONObject loginServiceResponseJSON,String authToken) throws ApplicationException {
		String xsrf;
		String jsessionid;
		String cookie;
		Map<String, Object> headerMap = new HashMap<>();
		xsrf = loginServiceResponseJSON.getString("DM-XSRF-TOKEN");
		//xsrf="f70fbc90-b050-491d-a137-468b60c84749" //Expired one.
		jsessionid = loginServiceResponseJSON.getString("JSESSIONID");
		if (StringUtils.isBlank(xsrf) || StringUtils.isBlank(jsessionid)) {
			diagnostic.prepareDebug("EFS--xsrf jsessionid are blank Login Failed").log();
			throw new ApplicationException(ErrorCodeEnum.ERR_25004);
		}
		headerMap.put("X-XSRF-TOKEN", xsrf);
		cookie = "DM-XSRF-TOKEN=" + xsrf + "; " + "JSESSIONID=" + jsessionid;
		//cookie="DM-XSRF-TOKEN=f70fbc90-b050-491d-a137-468b60c84749; JSESSIONID=D3Z6gC0tSXoQc5Uu-I2xSU4qlFQISghD4ayk2B7F.standalone-dm"//Expired one
		headerMap.put("COOKIE", cookie);
		if (!StringUtils.isBlank(authToken)) {
			headerMap.put(FabricConstants.X_KONY_AUTHORIZATION_HEADER, authToken);
		}
		return headerMap;
	}

	private ArrayList<JSONObject> getSearchResults(String serviceResponse) throws ApplicationException {

		try {
			ArrayList<JSONObject> docsList = new ArrayList<JSONObject>();
			JSONObject outputJSONObject;
			diagnostic.prepareDebug("EFS-search service response=" + serviceResponse).log();
			if(serviceResponse.contains("Unauthorized")) {
				alert.prepareError("EFS login Unauthorized" +serviceResponse).log();
				throw new ApplicationException(ErrorCodeEnum.ERR_25004);
			}
			if (serviceResponse == null || serviceResponse.equals("[]")) {
				alert.prepareError("efs search  service failed"+serviceResponse).log();
				throw new ApplicationException(ErrorCodeEnum.ERR_25003);
			} else {
				diagnostic.prepareDebug("SearchServiceResponse=" + serviceResponse).log();
				JSONArray responseArray = new JSONArray(serviceResponse);
				int size = responseArray.length();
				for (int i = 0; i < size; i++) {
					JSONObject obj = responseArray.getJSONObject(i);
					if (obj.has("fileProperties")) {
						outputJSONObject = new JSONObject();
						JSONObject properties = obj.getJSONObject("properties");
						JSONObject fileProperties = obj.getJSONObject("fileProperties");
						JSONObject keys = obj.getJSONObject("keys");
						outputJSONObject.put("documentId", properties.get("id").toString());
						outputJSONObject.put("revision", properties.get("revision").toString());
						outputJSONObject.put("month", keys.get("month").toString());
						outputJSONObject.put("year", keys.get("year").toString());
						outputJSONObject.put("documentDate", keys.get("document-date").toString());
						outputJSONObject.put("accountNumber", keys.get("account-number").toString());
						outputJSONObject.put("customerNumber", keys.get("customer-number").toString());
						outputJSONObject.put("fileExtension", fileProperties.get("extension").toString());
						outputJSONObject.put("subType", keys.get("sub-type").toString());
						docsList.add(outputJSONObject);
					}
				}
				if (docsList.size() <= 0) {
					alert.prepareError("efs search  no recordds found").log();
					throw new ApplicationException(ErrorCodeEnum.ERR_25003);
				} else {
					return docsList;
				}
			}
		} catch (ApplicationException e) {
			alert.prepareError(e.toString()).log();
			throw e;
		}catch (Exception e) {
			alert.prepareError("efs search failed" + e).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_25001);
		}
	}

	private static JSONObject getToken(String authToken,boolean isGenarateNewToken) throws ApplicationException,Exception {
		boolean isTokenAvailable=(tokenInfo != null) ? true:false;
		boolean newTokenRequired= !isTokenAvailable || isGenarateNewToken;
		if (newTokenRequired) {
			synchronized (TransactionAdviceAPIBackendDelegateImpl.class) {
					Map<String, Object> inputMap = new HashMap<>();
					Map<String, Object> headerMap = new HashMap<>();
					String userNameValue = EnvironmentConfigurationsHandler.getValue("EFS_USERNAME");
					String passwordValue = EnvironmentConfigurationsHandler.getValue("EFS_PASSWORD");
					if (userNameValue != null && passwordValue != null) {
						inputMap.put("username", userNameValue);
						inputMap.put("password", passwordValue);
						headerMap.put("X-XSRF-TOKEN", "");
						if (!StringUtils.isBlank(authToken)) {
							diagnostic.prepareDebug("efs login -Using Auth Token from Param-Login").log();
							headerMap.put(FabricConstants.X_KONY_AUTHORIZATION_HEADER, authToken);
						}
						diagnostic.prepareDebug("Attempting for efs login -Using Auth Token from Param-Login").log();
						String serviceResponse = Executor.invokeService(
								TransactionAdviceAPIServices.TRANSACTIONADVICEJSON_LOGIN, inputMap, headerMap);
						tokenInfo = Utilities.convertStringToJSON(serviceResponse);
						if (tokenInfo == null) {
							alert.prepareError("efs login failed").log();
						} else {
							return tokenInfo;
						}
					}
					else {
						alert.prepareError("efs userName and passowrd are null").log();
						throw new ApplicationException(ErrorCodeEnum.ERR_25005);
				}
			}
		}
		return tokenInfo;
	}

}
