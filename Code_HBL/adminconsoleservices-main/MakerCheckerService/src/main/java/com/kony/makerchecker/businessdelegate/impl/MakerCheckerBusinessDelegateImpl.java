package com.kony.makerchecker.businessdelegate.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.exception.DBPAuthenticationException;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.DBPServices;
import com.kony.adminconsole.utilities.LegalEntityUtil;
import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.ServiceId;
import com.kony.makerchecker.backenddelegate.api.MakerCheckerBackendDelegate;
import com.kony.makerchecker.businessdelegate.api.MakerCheckerBusinessDelegate;
import com.kony.makerchecker.javaservice.LoadMakerCheckerConfigData;
import com.kony.makerchecker.utils.ErrorCodesEnum;
import com.kony.makerchecker.utils.MakerCheckerUtils;
import com.kony.makerchecker.utils.UtilConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.exceptions.MiddlewareException;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class MakerCheckerBusinessDelegateImpl implements MakerCheckerBusinessDelegate {

	private static final Alert alert = Logger.forAlert().forModule(UtilConstants.INFINITY, UtilConstants.SPOTLIGHT);
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule(UtilConstants.INFINITY, UtilConstants.SPOTLIGHT);
	private static HashSet<String> editedList = new HashSet<String>();
	JSONObject enrollCusDetails = new JSONObject();
	
	
	@Override
	public Map<String,String> isMakerCheckerEnabled(DataControllerRequest request, String expApiOperationName,
			String legalEntityId) throws Exception {
		MakerCheckerBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class).getBackendDelegate(MakerCheckerBackendDelegate.class);
		return backendDelegate.isMakerCheckerEnabled(request, expApiOperationName, legalEntityId);
	}

	@Override
	public JSONObject storePayloadForRequest(DataControllerRequest request, Map<String, String> inputParams) {
		MakerCheckerBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class).getBackendDelegate(MakerCheckerBackendDelegate.class);
		return backendDelegate.storePayloadForRequest(request, inputParams);
	}

	public JSONObject getDashboardCounts(DataControllerRequest request, Map<String, String> inputParams) 
			throws Exception{
		MakerCheckerBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class).getBackendDelegate(MakerCheckerBackendDelegate.class);

		JSONObject finalResult = new JSONObject();
		JSONObject resultMakerObj = new JSONObject();
		JSONObject resultCheckerObj = new JSONObject();
		try {

			String[] leArray = getUserLegalEntityIds(request);
			
			if(leArray == null) {
				ErrorCodesEnum.ERR_10008.setErrorCode(finalResult);
			}
			
			JSONObject jsonObj = backendDelegate.getDashboardCounts(request, inputParams);

			diagnostic.prepareDebug("backendDelegate response " + jsonObj).log();
			JSONObject makerRequestsObj = jsonObj.optJSONObject(UtilConstants.MAKER_REQUESTS);
			JSONObject checkerRequestsObj = jsonObj.optJSONObject(UtilConstants.CHECKER_REQUESTS);
			String leWithCheckerPermissions = jsonObj.optString("leWithCheckerPermissions");
			String[] leWithCheckerPermissionsArray;
			if(leWithCheckerPermissions.length()>0) {
				leWithCheckerPermissionsArray = leWithCheckerPermissions.split(",");
				for (String leId : leWithCheckerPermissionsArray) {
					resultMakerObj.put(leId, 0);
					resultCheckerObj.put(leId, 0);
				}
			}
			for(String le : leArray) {
				resultMakerObj.put(le, 0);
			}
			
			JSONArray makerReqsArray = makerRequestsObj.optJSONArray(UtilConstants.GET_MC_APPROVALREQUESTS_VIEW);
			diagnostic.prepareDebug("makerReqsArray " + makerReqsArray.toString()).log();
			
			HashMap<String, Integer> makerRequestsMap = new HashMap<>();
			
			for (int i = 0; i < makerReqsArray.length(); i++) {
				JSONObject makerReqObj = new JSONObject();
				makerReqObj = makerReqsArray.getJSONObject(i);
				makerRequestsMap.put(makerReqObj.getString(UtilConstants.COMPANY_LEGAL_UNIT), 
						makerRequestsMap.getOrDefault(makerReqObj.getString(UtilConstants.COMPANY_LEGAL_UNIT), 0)+ Integer.valueOf(makerReqObj.getString(UtilConstants.REQUEST_COUNT)));
		
				resultMakerObj.put(makerReqObj.getString(UtilConstants.COMPANY_LEGAL_UNIT), makerRequestsMap.getOrDefault(makerReqObj.getString(UtilConstants.COMPANY_LEGAL_UNIT), 0));
			}
			
			diagnostic.prepareDebug("resultMakerObj: " + resultMakerObj.toString()).log();
			finalResult.put(UtilConstants.MAKER, resultMakerObj);
			
			JSONArray checkerReqsArray = null;
			if(!(checkerRequestsObj==null)) {
				checkerReqsArray = checkerRequestsObj.optJSONArray(UtilConstants.GET_MC_APPROVALREQUESTS_VIEW);
				diagnostic.prepareDebug("checkerReqsArray " + checkerReqsArray.toString()).log();
				
				HashMap<String, Integer> checkerRequestsMap = new HashMap<>();
				
				for (int i = 0; i < checkerReqsArray.length(); i++) {
					JSONObject checkerReqObj = new JSONObject();
					checkerReqObj = checkerReqsArray.getJSONObject(i);
					checkerRequestsMap.put(checkerReqObj.getString(UtilConstants.COMPANY_LEGAL_UNIT), 
							checkerRequestsMap.getOrDefault(checkerReqObj.getString(UtilConstants.COMPANY_LEGAL_UNIT), 0)+ Integer.valueOf(checkerReqObj.getString(UtilConstants.REQUEST_COUNT)));
					
					resultCheckerObj.put(checkerReqObj.getString(UtilConstants.COMPANY_LEGAL_UNIT), checkerRequestsMap.getOrDefault(checkerReqObj.getString(UtilConstants.COMPANY_LEGAL_UNIT), 0));
				}
				
				diagnostic.prepareDebug("resultCheckerObj: " + resultCheckerObj.toString()).log();
				finalResult.put(UtilConstants.CHECKER, resultCheckerObj);
			}
			else {
				finalResult.put(UtilConstants.CHECKER, new JSONObject());
			}

			diagnostic.prepareDebug("finalResult: " + finalResult.toString()).log();
			return finalResult;
		} catch (Exception e) {
			diagnostic.prepareInfo("Exception occurred in invoking CheckIfMakerCheckerEnabled").log();
			return ErrorCodesEnum.ERR_10009.setErrorCode(finalResult);
		}

	}

	private String[] getUserLegalEntityIds(DataControllerRequest request){
			
			String[] leArray = null;
			try {
				leArray = null;

				String legalEntityIds;
				JSONObject obj = CommonUtilities.getStringAsJSONObject(request.getServicesManager().getIdentityHandler()
						.getSecurityAttributes().get(UtilConstants.RAW_RESPONSE).toString());
				legalEntityIds = CommonUtilities.getStringAsJSONObject(obj.get(UtilConstants.USER_ATTRIBUTES).toString())
						.getString(UtilConstants.LEGAL_ENTITY_ID);
				leArray = legalEntityIds.split(",");
				return leArray;
			} catch (JSONException | MiddlewareException e) {
				diagnostic.prepareInfo("Exception occured while trying to fetch user legal entities").log();
				return null;
			}
	}
		
	@Override
	public JSONObject getMakerCheckerPendingRequests(DataControllerRequest request,
			Map<String, String> inputParams) throws Exception {
		MakerCheckerBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class).getBackendDelegate(MakerCheckerBackendDelegate.class);
		return backendDelegate.getMakerCheckerPendingRequests(request, inputParams);
	}

	@Override
	public JSONObject approvalRequestViewDetails(DataControllerRequest request, Map<String, String> inputParams) {
		MakerCheckerBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class).getBackendDelegate(MakerCheckerBackendDelegate.class);
		JSONObject viewDetailsResponseJson = checkViewAPIResponseAvailable(request,
				inputParams.get("requestId").toString());
		if (viewDetailsResponseJson != null && viewDetailsResponseJson.length() > 0) {
			return viewDetailsResponseJson;
		} else {
			JSONObject response = backendDelegate.approvalRequestViewDetails(request, inputParams);
			if (response != null && !response.has("dbpErrCode")) {
				Map<String, Object> requestParameters = new HashMap<String, Object>();
				requestParameters.put("requestId", inputParams.get("requestId").toString());
				requestParameters.put("viewDetailsResponse", response.toString());
				backendDelegate.updateApprovalRequests(request, requestParameters);
			}
			return response;
		}
	}

	public JSONObject checkViewAPIResponseAvailable(DataControllerRequest request, String requestId) {
		MakerCheckerBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class).getBackendDelegate(MakerCheckerBackendDelegate.class);
		JSONObject viewDetailsResponseJson = new JSONObject();
		JSONObject requestData = backendDelegate.getApprovalRequests(request, requestId);
		if (requestData != null && requestData.has(UtilConstants.APPROVAL_REQUESTS_GET_RECORD)) {
			String viewDetailsResponse = requestData.optJSONArray(UtilConstants.APPROVAL_REQUESTS_GET_RECORD)
					.optJSONObject(0).optString("viewDetailsResponse");
			viewDetailsResponseJson = StringUtils.isNotBlank(viewDetailsResponse) ? new JSONObject(viewDetailsResponse)
					: null;
		}
		return viewDetailsResponseJson;
	}
	
	@Override
	public JSONObject approveRejectRequest(DataControllerRequest request, String requestId, String action,
			String comments, HashMap<String, String> approvalRequestDetails) {

		MakerCheckerBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class).getBackendDelegate(MakerCheckerBackendDelegate.class);

		JSONObject resultObject = new JSONObject();
		try {
		JSONObject rawresponse = CommonUtilities.getStringAsJSONObject(request.getServicesManager().getIdentityHandler()
				.getSecurityAttributes().get(UtilConstants.RAW_RESPONSE).toString());
		String userName = CommonUtilities.getStringAsJSONObject(rawresponse.get(UtilConstants.USER_ATTRIBUTES).toString())
				.getString(UtilConstants.USERNAME);
		
		
		JSONArray makerCheckerConfigArray = LoadMakerCheckerConfigData.getMakerCheckerConfigData();
		
		List<Object> makerCheckerConfigList = makerCheckerConfigArray.toList().stream().filter(item -> {
            HashMap<String, String> obj = (HashMap<String, String>) item;
            return obj.containsKey(UtilConstants.EXPAPIOPNAME) && obj.get(UtilConstants.EXPAPIOPNAME).equals(approvalRequestDetails.get(UtilConstants.EXPAPIOPNAME))
                    && obj.containsKey(UtilConstants.COMPANY_LEGAL_UNIT) && obj.get(UtilConstants.COMPANY_LEGAL_UNIT).equals(approvalRequestDetails.get(UtilConstants.COMPANY_LEGAL_UNIT));
        }).collect(Collectors.toList());
		
		HashMap<String,String> configmap = new HashMap<String,String>();
		
		if(makerCheckerConfigArray.length()>0) {
			configmap = (HashMap<String, String>) makerCheckerConfigList.get(0);
		}
		
		String module = configmap.get("auditModule");
		String event = configmap.get("auditEvent");
		
		
		if (UtilConstants.SID_APPROVED.equalsIgnoreCase(action)) {
			String service = null, object = null, operation = null, payloadString = null;
			HashMap<String, Object> payload = new HashMap<String, Object>();
			
			service = approvalRequestDetails.get(UtilConstants.EXPAPIOPNAME).split("_")[0];
			object = approvalRequestDetails.get(UtilConstants.EXPAPIOPNAME).split("_")[1];
			operation = approvalRequestDetails.get(UtilConstants.EXPAPIOPNAME).split("_")[2];
			payloadString = approvalRequestDetails.get(UtilConstants.REQ_PAYLOAD);
			
			
			payload = payloadToInputMap(payloadString, service, object);
			
			JSONObject responseObj = MakerCheckerUtils.updateApprovalRequestStatus(request, requestId,
					UtilConstants.SID_PROCESSING, null, UtilConstants.SID_PENDING, userName);

			if (responseObj != null && !responseObj.isEmpty() && responseObj.has(UtilConstants.STATUS)) {
				if (responseObj.getString(UtilConstants.STATUS).equalsIgnoreCase(UtilConstants.SUCCESS)) {

					resultObject.put(UtilConstants.STATUS, "Request is approved and submitted for processing.");
					backendDelegate.callExpAPIAsyncAndGetResult(request, service, object, operation, requestId,
							payload, userName);
					AuditHandler.auditAdminActivity(request, module, "Approve "+event,
							ActivityStatusEnum.SUCCESSFUL, " " +event + " "+ module + " Request Approved and submitted for processing.");
				} else if (responseObj.getString(UtilConstants.STATUS).equalsIgnoreCase(UtilConstants.FAILURE)) {
					AuditHandler.auditAdminActivity(request, module, "Approve "+event,
							ActivityStatusEnum.SUCCESSFUL, " " +event + " "+ module + " Request Approve failed.");
					return ErrorCodesEnum.ERR_10023.setErrorCode(responseObj);
				}
			} else {
				AuditHandler.auditAdminActivity(request, module, "Approve "+event,
						ActivityStatusEnum.SUCCESSFUL, " " +event + " "+ module + "Request Approve failed.");
				return ErrorCodesEnum.ERR_10026.setErrorCode(responseObj);
			}
		} else if (UtilConstants.SID_REJECTED.equalsIgnoreCase(action)) {

			resultObject = MakerCheckerUtils.updateApprovalRequestStatus(request, requestId, UtilConstants.SID_REJECTED,
					comments, UtilConstants.SID_PENDING, userName);

			if (resultObject != null && !resultObject.isEmpty() && resultObject.has(UtilConstants.STATUS)) {
				if (resultObject.getString(UtilConstants.STATUS).equalsIgnoreCase(UtilConstants.SUCCESS)) {

					resultObject.put(UtilConstants.STATUS, "Request is rejected successfully");
					AuditHandler.auditAdminActivity(request, module, "Reject "+event,
							ActivityStatusEnum.SUCCESSFUL, " " +event + " "+ module + " Request Rejected successfully.");
				} else if (resultObject.getString(UtilConstants.STATUS).equalsIgnoreCase(UtilConstants.FAILURE)) {
					AuditHandler.auditAdminActivity(request, module, "Reject "+event,
							ActivityStatusEnum.SUCCESSFUL, " " +event + " "+ module + " Request Reject failed.");
					return ErrorCodesEnum.ERR_10022.setErrorCode(resultObject);
				}
			} else {
				AuditHandler.auditAdminActivity(request, module, "Reject "+event,
						ActivityStatusEnum.SUCCESSFUL, " " +event + " "+ module + " Request Reject failed.");
				return ErrorCodesEnum.ERR_10026.setErrorCode(resultObject);
			}
		}
		return resultObject;
		}
		catch(Exception e) {
			diagnostic.prepareDebug("Exception while approving/rejecting the request. "+ e.getMessage()).log();
			
		}
		return resultObject;
	}

	private HashMap<String, Object> payloadToInputMap(String payload, String service, String object) {
		HashMap<String, Object> inputMap = new HashMap<String, Object>();
		try {
			inputMap = (HashMap<String, Object>) new ObjectMapper().readValue(payload, new TypeReference<Map<String, Object>>(){});
			if (service.equalsIgnoreCase(UtilConstants.APPROVALMATRIX_SERVICE)
					&& object.equalsIgnoreCase(UtilConstants.APPROVALMATRIX_OBJECT) 
					&& inputMap.containsKey("limits")) {
				Gson gson = new Gson();
				String limitsStr = gson.toJson(inputMap.get("limits"));
				inputMap.put("limits", limitsStr);
			}else if (service.equalsIgnoreCase(UtilConstants.STATIC_CONTENT_OBJ_SERVICE)
					&& object.equalsIgnoreCase(UtilConstants.OUTAGE_MESSAGE) 
					&& inputMap.containsKey("OutageMessageIds")) {
				Gson gson = new Gson();
				String outageMsgIds = gson.toJson(inputMap.get("OutageMessageIds"));
				inputMap.put("OutageMessageIds", outageMsgIds);
			}
		} catch (Exception e) {
			diagnostic.prepareDebug("Exception while parsing the payload. "+ e.getMessage()).log();
		
		}
		return inputMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getRequestsHistory(DataControllerRequest request, Map<String, Object> inputParams)
			throws Exception {
		MakerCheckerBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class).getBackendDelegate(MakerCheckerBackendDelegate.class);
		String username = "";
		Set<String> userAssociatedLegalEntityIds = null;
		JSONObject userIdentityInfo;
		JSONObject finalResult = new JSONObject();
		JSONObject response = new JSONObject();
		JSONObject totalRecords = new JSONObject();
		Map<String, Object> requestPayload = new HashMap<>();
		HashMap<String, Set<String>> leWisePermissionMap = new HashMap<>();
		try {
			Map<String, String> userAttributes = CommonUtilities.getLoggedInUserAttributes(request);
			userIdentityInfo = CommonUtilities.getStringAsJSONObject(request.getServicesManager().getIdentityHandler()
					.getSecurityAttributes().get(UtilConstants.RAW_RESPONSE).toString());

			username = CommonUtilities
					.getStringAsJSONObject(userIdentityInfo.get(UtilConstants.USER_ATTRIBUTES).toString())
					.getString(UtilConstants.USERNAME);
			userAssociatedLegalEntityIds = new HashSet<String>(Arrays.asList(CommonUtilities
					.getStringAsJSONObject(userIdentityInfo.get(UtilConstants.USER_ATTRIBUTES).toString())
					.getString(UtilConstants.LEGAL_ENTITY_ID).split(",")));
			diagnostic.prepareDebug("userAttributes " + userAttributes).log();
			String status = UtilConstants.MAKER.equalsIgnoreCase(inputParams.get("userType").toString())
					? UtilConstants.REQUESTHISTORY_STATUS.concat(", " + UtilConstants.SID_WITHDRAWN)
					: UtilConstants.REQUESTHISTORY_STATUS;
			inputParams.put("username", username);
			inputParams.put("status", status);
			leWisePermissionMap = MakerCheckerUtils.getLEWisePermissions(request, userAssociatedLegalEntityIds);
			Set<String> currLEPermissionsSet = leWisePermissionMap.get(inputParams.get("companyLegalUnit"));

			if (userAssociatedLegalEntityIds.size() > 0) {
				if(leWisePermissionMap.containsKey(UtilConstants.MAKER_CHECKER_SHARED_LEGAL_ENTITY)) {
					currLEPermissionsSet.addAll(leWisePermissionMap.get(UtilConstants.MAKER_CHECKER_SHARED_LEGAL_ENTITY));
				}
				inputParams.put("isMultiEntityUser", true);
			}
			if (UtilConstants.CHECKER.equalsIgnoreCase(inputParams.get("userType").toString())) {
				requestPayload = getCheckerRequestsPayload(currLEPermissionsSet, inputParams);
			} else if (UtilConstants.MAKER.equalsIgnoreCase(inputParams.get("userType").toString())) {
				inputParams.put("legalEntityIds", inputParams.get("companyLegalUnit").toString());
				requestPayload = getMakerRequestsPayload(inputParams);
			}
			response = backendDelegate
					.getRequestsHistory((Map<String, Object>) requestPayload.get("requestWithPaginationParams"));
			totalRecords = backendDelegate
					.getRequestsHistory((Map<String, Object>) requestPayload.get("requestWithOutPaginationParams"));

			finalResult.put("totalNumberOfRecords",
					totalRecords.optJSONArray(UtilConstants.GET_MC_REQUESTSHISTORY_VIEW).length());
			finalResult.put("requests", response.optJSONArray(UtilConstants.GET_MC_REQUESTSHISTORY_VIEW));
		} catch (Exception e) {
			diagnostic.prepareInfo("Exception occurred in MakerCheckerBusinessDelegateImpl:getRequestsHistory" + e)
					.log();
			return ErrorCodesEnum.ERR_10036.setErrorCode(finalResult);
		}
		return finalResult;

	}

	private Map<String, Object> getCheckerRequestsPayload(Set<String> currLEPermissionsSet,
			Map<String, Object> inputPayload) {
		String filter = "";
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		List<String> legalEntityIdList = new ArrayList<>();

		try {
			String username = (String) inputPayload.get("username");
			List<String> status = Arrays.asList(inputPayload.get("status").toString().split(","));
			legalEntityIdList.add(inputPayload.get("companyLegalUnit").toString());
			if (inputPayload.containsKey("isMultiEntityUser") && (boolean) inputPayload.get("isMultiEntityUser")) {
				legalEntityIdList.add(UtilConstants.MAKER_CHECKER_SHARED_LEGAL_ENTITY);
			}

			filter = filter + "(" + UtilConstants.COMPANY_LEGAL_UNIT + " eq "
					+ String.join(" or " + UtilConstants.COMPANY_LEGAL_UNIT + " eq ", legalEntityIdList) + ") and "
					+ UtilConstants.CREATED_BY + " ne '" + username + "' and " + "(" + UtilConstants.STATUS + " eq "
					+ String.join(" or " + UtilConstants.STATUS + " eq ", status) + ")" + " and ";
			filter += "(" + UtilConstants.APPROVAL_PERMISSION_NAME + " eq "
					+ String.join(" or " + UtilConstants.APPROVAL_PERMISSION_NAME + " eq ", currLEPermissionsSet) + ")";
			requestParameters = formatRequestHistoryPayload(inputPayload, filter, requestParameters);

		} catch (Exception e) {
			diagnostic.prepareDebug("Exception occured while formatting Checker Request History payload" + e).log();
		}
		return requestParameters;
	}

	private Map<String, Object> getMakerRequestsPayload(Map<String, Object> inputPayload) {
		String filter = "";
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		List<String> legalEntityIdList = new ArrayList<>();
		try {
			String username = (String) inputPayload.get("username");
			List<String> status = Arrays.asList(inputPayload.get("status").toString().split(","));
			legalEntityIdList.add(inputPayload.get("companyLegalUnit").toString());
			if (inputPayload.containsKey("isMultiEntityUser") && (boolean) inputPayload.get("isMultiEntityUser")) {
				legalEntityIdList.add(UtilConstants.MAKER_CHECKER_SHARED_LEGAL_ENTITY);

			}
			filter = UtilConstants.CREATED_BY + " eq '" + username + "' and " + "(" + UtilConstants.STATUS + " eq "
					+ String.join(" or " + UtilConstants.STATUS + " eq ", status) + ") and ";
			filter = filter + "(" + UtilConstants.COMPANY_LEGAL_UNIT + " eq "
					+ String.join(" or " + UtilConstants.COMPANY_LEGAL_UNIT + " eq ", legalEntityIdList) + ")";
			requestParameters = formatRequestHistoryPayload(inputPayload, filter, requestParameters);
		} catch (Exception e) {
			diagnostic.prepareDebug("Exception occured while formatting Maker Request History payload" + e).log();
		}
		return requestParameters;
	}

	private Map<String, Object> formatRequestHistoryPayload(Map<String, Object> inputPayload, String filter,
			Map<String, Object> requestParameters) {
		Map<String, Object> requestWithPaginationParams = null;
		Map<String, Object> requestWithOutPaginationParams = null;
		try {
			if (inputPayload.containsKey("module") && inputPayload.get("module") != null
					&& !inputPayload.get("module").toString().isEmpty()) {
				filter = filter + " and " + UtilConstants.MODULE + " eq " + inputPayload.get("module").toString();
			}
			if (inputPayload.containsKey("action") && inputPayload.get("action") != null
					&& !inputPayload.get("action").toString().isEmpty()) {
				List<String> actions = Arrays.asList(inputPayload.get("action").toString().split(","));
				filter = filter + " and " + "(" + UtilConstants.ACTION + " eq "
						+ String.join(" or " + UtilConstants.ACTION + " eq ", actions) + ")";
			}
			if (inputPayload.containsKey("date") && inputPayload.get("date") != null
					&& !inputPayload.get("date").toString().isEmpty()) {
				filter = filter + " and " + UtilConstants.CREATED_DATE + " eq '" + inputPayload.get("date").toString()
						+ "'";
			}
			requestParameters.put(ODataQueryConstants.ORDER_BY,
					UtilConstants.ACTIONED_TIMESTAMP + " " + UtilConstants.ORDER_BY_DESC);
			requestParameters.put(ODataQueryConstants.FILTER, filter);
			requestWithOutPaginationParams = new HashMap<>(requestParameters);
			if (inputPayload.containsKey("PageSize") && inputPayload.get("PageSize") != null
					&& inputPayload.containsKey("PageOffset") && inputPayload.get("PageOffset") != null) {
				int offsetValue = Integer.parseInt(inputPayload.get("PageSize").toString())
						* (Integer.parseInt(inputPayload.get("PageOffset").toString()) - 1);
				requestParameters.put(ODataQueryConstants.TOP, inputPayload.get("PageSize").toString());
				requestParameters.put(ODataQueryConstants.SKIP, offsetValue);
			}
			requestWithPaginationParams = new HashMap<>(requestParameters);
			requestParameters.put("requestWithPaginationParams", requestWithPaginationParams);
			requestParameters.put("requestWithOutPaginationParams", requestWithOutPaginationParams);
		} catch (Exception e) {
			diagnostic.prepareDebug("Exception occured while formatting Request History payload" + e).log();
		}
		return requestParameters;
	}

	@Override
	public JSONObject getMakerCheckerConfig(DataControllerRequest request, Map<String, String> inputParams) {
		MakerCheckerBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class).getBackendDelegate(MakerCheckerBackendDelegate.class);
		return backendDelegate.getMakerCheckerConfig(request, inputParams);
	}
	
	@Override
	public JSONObject updateMakerCheckerConfig(DataControllerRequest request, Map<String, Object> inputParams) {
		MakerCheckerBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class).getBackendDelegate(MakerCheckerBackendDelegate.class);

		JSONArray configArray = (JSONArray) inputParams.get("mcConfigData");
		configArray.forEach((item) -> {
			JSONObject itemObj = (JSONObject) item;
			boolean isApprovalRequired = "true".equals(itemObj.optString("isApprovalRequired"));
			int approvalValue = isApprovalRequired ? 1 : 0;
			itemObj.put("isApprovalRequired", approvalValue);
			itemObj.put("id", Integer.parseInt(itemObj.optString("id")));
		});
		inputParams.put("mcConfigData", configArray);
		return backendDelegate.updateMakerCheckerConfig(request, inputParams);
	}

	public String getApprovalRequests(DataControllerRequest request, String reqId, List<String> excludedParams) {
		JSONObject parseRes = null;
		MakerCheckerBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class).getBackendDelegate(MakerCheckerBackendDelegate.class);

		try {
			parseRes = backendDelegate.getApprovalRequests(request, reqId);
			parseRes = parseApprovalRequestDetails(parseRes, excludedParams);
			StringBuilder sb = new StringBuilder();
			convertJSONObjectToHTML(parseRes, sb);
			return sb.toString();
		} catch (Exception e) {
			diagnostic.prepareInfo("Exception occured while processing approval requests data.").log();
			return ErrorCodesEnum.ERR_10028.setErrorCode(parseRes).toString();
		}
	}

	public JSONObject parseApprovalRequestDetails(JSONObject obj, List<String> excludedParams) {
		JSONObject parsedObj = new JSONObject();
		try {
			String payload = obj.optJSONArray(UtilConstants.APPROVAL_REQUESTS_GET_RECORD).optJSONObject(0)
					.optString("reqPayload");
			JsonObject payloadObj = new Gson().fromJson(payload, JsonObject.class);
			
			for (String name : payloadObj.keySet()) {
				if (null == excludedParams) {
					String value = payloadObj.get(name).toString();
					parseResponse(name, value, parsedObj);
				}else if(!excludedParams.contains(name)){
					String value = payloadObj.get(name).toString();
					parseResponse(name, value, parsedObj);
				}
			}

		} catch (Exception e) {
			return null;
		}
		return parsedObj;
	}
	
	public String parseRequestAndFetchLegalEntityId(String str) {
		JSONObject parsedObj = new JSONObject();
		try {
			
			JsonObject payloadObj = new Gson().fromJson(str, JsonObject.class);
			
			for (String name : payloadObj.keySet()) {
				String value = payloadObj.get(name).toString();
				parseResponse(name, value, parsedObj);
			}

		} catch (Exception e) {
			return null;
		}

		return parsedObj.optString(UtilConstants.LEGAL_ENTITY_ID,"").replaceAll("\'", "").replaceAll("\"", "");
	}

	private void parseResponse(String name, String value, JSONObject obj) {
		try {
			if (value.startsWith("\"{") && value.indexOf("[") == -1) {
				String c = value.substring(1, value.length() - 1).replaceAll("\\\\", "");
				JSONObject jo = new JSONObject(c);
				Iterator<String> itr = jo.keys();
				JSONObject jsonObject = new JSONObject();
				while (itr.hasNext()) {
					String key = itr.next();
					jsonObject.put(key, jo.optString(key));
					if(key.equalsIgnoreCase("legalEntityId"))
						obj.put("legalEntityId", jo.optString(key));
				}
				obj.put(name, jsonObject);
			} else if (value.startsWith("\"{") && value.indexOf("[") != -1) {
				String c = value.substring(1, value.length() - 1).replaceAll("\\\\", "").replaceAll("\"\\[", "[")
						.replaceAll("\\]\"", "]").replaceAll("\"\\{", "{").replaceAll("\\}\"", "}");
				JSONObject jo = new JSONObject(c);
				obj.put(name, jo);
			} else if (value.startsWith("\"[")) {
				String c = value.substring(1, value.length() - 1).replaceAll("\\\\", "").replaceAll("\"\\[", "[")
						.replaceAll("\\]\"", "]").replaceAll("\"\\{", "{").replaceAll("\\}\"", "}");
				JSONArray jo = new JSONArray(c);
				obj.put(name, jo);
			} else if (value.startsWith("\"")) {
				obj.put(name, value.replace("\"", ""));
			} else if(isValidJsonArray(value)) {
				JSONArray ja = new JSONArray(value);
				obj.put(name, ja);
			} else if(isValidJsonObject(value)) {
				JSONObject jo = new JSONObject(value);
				Iterator<String> itr = jo.keys();
				while (itr.hasNext()) {
					String key = itr.next();
					obj.put(key, jo.optString(key));
				}
			}
		} catch (Exception e) {
			diagnostic.prepareInfo("Exception occured while processing approval requests data.").log();
		}
	}
	
	private static void convertJSONObjectToHTML(JSONObject jsonObject, StringBuilder htmlStringBuilder) {
        htmlStringBuilder.append("<ul>");
        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);
            if (value instanceof JSONObject) {
                htmlStringBuilder.append("<li><b>").append(key).append("</b>: ");
                convertJSONObjectToHTML((JSONObject) value, htmlStringBuilder);
                htmlStringBuilder.append("</li>");
            } else if (value instanceof JSONArray) {
                htmlStringBuilder.append("<li><b>").append(key).append("</b>: ");
                convertJSONArrayToHTML((JSONArray) value, htmlStringBuilder);
                htmlStringBuilder.append("</li>");
            } else {
                htmlStringBuilder.append("<li><b>").append(key).append("</b>: ").append(StringEscapeUtils.escapeHtml(value.toString())).append("</li>");
            }
        }
        htmlStringBuilder.append("</ul>");
    }
	
	private static void convertJSONArrayToHTML(JSONArray jsonArray, StringBuilder htmlStringBuilder) {
        htmlStringBuilder.append("<ol>");
        for (int i = 0; i < jsonArray.length(); i++) {
            Object element = jsonArray.get(i);
            if (element instanceof JSONObject) {
                htmlStringBuilder.append("<li>");
                convertJSONObjectToHTML((JSONObject) element, htmlStringBuilder);
                htmlStringBuilder.append("</li>");
            } else if (element instanceof JSONArray) {
                htmlStringBuilder.append("<li>");
                convertJSONArrayToHTML((JSONArray) element, htmlStringBuilder);
                htmlStringBuilder.append("</li>");
            } else {
                htmlStringBuilder.append("<li>").append(StringEscapeUtils.escapeHtml(element.toString())).append("</li>");
            }
        }
        htmlStringBuilder.append("</ol>");
    }
	
	private boolean isValidJsonArray(String input) {
		try {
			JsonParser parser = new JsonParser();
			JsonElement jsonElement = parser.parse(input);
			if (jsonElement.isJsonArray()) {
				StreamSupport.stream(jsonElement.getAsJsonArray().spliterator(), true).map(n -> (JsonElement) n)
						.forEach((n) -> {
							// Validating each JSON Object inside JSON Array
							if (!n.isJsonObject()) {
								throw new JsonParseException("Not a JSON Object");
							}
						});
			} else {
				return false;
			}
		} catch (JsonParseException e) {
			return false;
		}
		return true;
	}
	
	private boolean isValidJsonObject(String input) {
		try {
			JsonParser parser = new JsonParser();
			JsonElement jsonElement = parser.parse(input);
			if (jsonElement.isJsonObject()) {
				return true;
			} else {
				return false;
			}
		} catch (JsonParseException e) {
			return false;
		}
	}
	
	@Override
	public JSONObject getCheckerApprovalRequests(DataControllerRequest request, Map<String, String> inputParams) {

		MakerCheckerBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class).getBackendDelegate(MakerCheckerBackendDelegate.class);

		JSONObject finalResult = new JSONObject();

		try {

			//boolean isAllowed = false;
			JSONObject objIdentity = CommonUtilities.getStringAsJSONObject(request.getServicesManager()
					.getIdentityHandler().getSecurityAttributes().get(UtilConstants.RAW_RESPONSE).toString());

			String legalEntityIds = CommonUtilities
					.getStringAsJSONObject(objIdentity.get(UtilConstants.USER_ATTRIBUTES).toString())
					.getString(UtilConstants.LEGAL_ENTITY_ID);
			String username = CommonUtilities
					.getStringAsJSONObject(objIdentity.get(UtilConstants.USER_ATTRIBUTES).toString())
					.getString(UtilConstants.USERNAME);

			inputParams.put(UtilConstants.USER_NAME, StringUtils.isEmpty(username) ? null : username);

			Set<String> legalEntitySet = new HashSet<String>(Arrays.asList(legalEntityIds.split(",")));

			HashMap<String, Set<String>> leWisePermissionMap = new HashMap<>();
			leWisePermissionMap = MakerCheckerUtils.getLEWisePermissions(request, legalEntitySet);
			Set<String> currLEPermissionsSet = leWisePermissionMap.get(inputParams.get(UtilConstants.LEGAL_ENTITY_ID));
			if(legalEntitySet.size() > 0) {
				if(leWisePermissionMap.containsKey(UtilConstants.MAKER_CHECKER_SHARED_LEGAL_ENTITY)) {
					currLEPermissionsSet.addAll(leWisePermissionMap.get(UtilConstants.MAKER_CHECKER_SHARED_LEGAL_ENTITY));
				}
				inputParams.put(UtilConstants.LEGAL_ENTITY_ID, inputParams.get(UtilConstants.LEGAL_ENTITY_ID) + "," + UtilConstants.MAKER_CHECKER_SHARED_LEGAL_ENTITY);
			}
			
			if(currLEPermissionsSet == null || currLEPermissionsSet.isEmpty()) {
				return ErrorCodesEnum.ERR_10031.setErrorCode(finalResult);
			}
			
			diagnostic.prepareDebug("leWisePermissionMap " + leWisePermissionMap).log();

			/*JSONArray makerCheckerData = new JSONArray();

			makerCheckerData = LoadMakerCheckerConfigData.getMakerCheckerConfigData();
			List<Object> makerCheckerList = makerCheckerData.toList().stream().filter(item -> {
				HashMap<String, String> obj = (HashMap<String, String>) item;
				return obj.containsKey(UtilConstants.COMPANY_LEGAL_UNIT) && obj.get(UtilConstants.COMPANY_LEGAL_UNIT)
						.equals(inputParams.get(UtilConstants.LEGAL_ENTITY_ID));
			}).collect(Collectors.toList());*/

			JSONArray filteredReqs = new JSONArray();
			JSONObject checkerApprovalRequestsData = backendDelegate.getCheckerApprovalRequests(request, inputParams);
			diagnostic.prepareDebug("checkerApprovalRequestsData " + checkerApprovalRequestsData).log();
			JSONArray checkerReqsArray = checkerApprovalRequestsData.optJSONArray(UtilConstants.APPROVARPENDINGREQUESTS);
			diagnostic.prepareDebug("checkerReqsArray " + checkerReqsArray).log();
			for(int i=0; checkerReqsArray!=null && i<checkerReqsArray.length(); i++) {
				JSONObject checkerRequest = checkerReqsArray.optJSONObject(i);
				if(checkerRequest!=null && currLEPermissionsSet.contains(checkerRequest.get("permissionName"))) {
					filteredReqs.put(checkerRequest);
				}
			}
			finalResult.put(UtilConstants.APPROVARPENDINGREQUESTS, filteredReqs);
			finalResult.put(UtilConstants.TOTALNUMBEROFRECORDS, checkerApprovalRequestsData.optString(UtilConstants.TOTALNUMBEROFRECORDS));
			
			return finalResult;
		} catch (Exception e) {
			diagnostic.prepareInfo("Exception occurred in invoking getCheckerApprovalRequests").log();
			return ErrorCodesEnum.ERR_10032.setErrorCode(finalResult);
		}
	}

	@Override
	public JSONObject getAllMakerPendingRequests(DataControllerRequest request, Map<String, String> inputParams)
			throws Exception {
		try {
			JSONObject obj = CommonUtilities.getStringAsJSONObject(request.getServicesManager().getIdentityHandler()
					.getSecurityAttributes().get(UtilConstants.RAW_RESPONSE).toString());
			String userName = CommonUtilities.getStringAsJSONObject(obj.get(UtilConstants.USER_ATTRIBUTES).toString())
					.getString(UtilConstants.USERNAME);
			String legalEntityIds = CommonUtilities
					.getStringAsJSONObject(obj.get(UtilConstants.USER_ATTRIBUTES).toString())
					.getString(UtilConstants.LEGAL_ENTITY_ID);
			Set<String> legalEntitySet = new HashSet<String>(Arrays.asList(legalEntityIds.split(",")));

			if (legalEntitySet.size() > 0) {
				inputParams.put(UtilConstants.LEGAL_ENTITY_ID,
						inputParams.get(UtilConstants.LEGAL_ENTITY_ID) + "," + UtilConstants.MAKER_CHECKER_SHARED_LEGAL_ENTITY);
			}
			Map<String, Object> requestParameters = new HashMap<String, Object>();
			requestParameters.put("_legalEntityId",
					StringUtils.isNotBlank(inputParams.get(UtilConstants.LEGAL_ENTITY_ID))
							? inputParams.get(UtilConstants.LEGAL_ENTITY_ID)
							: null);
			requestParameters.put("_module",
					StringUtils.isNotBlank(inputParams.get(UtilConstants.MODULE))
							? inputParams.get(UtilConstants.MODULE)
							: null);
			requestParameters.put("_action",
					StringUtils.isNotBlank(inputParams.get(UtilConstants.REQUESTTYPE))
							? inputParams.get(UtilConstants.REQUESTTYPE)
							: null);
			requestParameters.put("_userName", StringUtils.isEmpty(userName) ? null : userName);
			requestParameters.put("_submittedDate",
					StringUtils.isNotBlank(inputParams.get(UtilConstants.SUBMITTEDDATE))
							? inputParams.get(UtilConstants.SUBMITTEDDATE)
							: null);
			requestParameters.put("_pageSize",
					StringUtils.isNotBlank(inputParams.get(UtilConstants.PAGESIZE))
							? inputParams.get(UtilConstants.PAGESIZE)
							: 10);
			requestParameters.put("_pageOffset",
					StringUtils.isNotBlank(inputParams.get(UtilConstants.PAGEOFFSET))
							? MakerCheckerUtils.calculateOffset(request)
							: 0);

			MakerCheckerBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BackendDelegateFactory.class)
					.getBackendDelegate(MakerCheckerBackendDelegate.class);
			return backendDelegate.getAllMakerPendingRequests(request, requestParameters);
		} catch (Exception e) {
			diagnostic.prepareDebug("Exception occured while trying to get Maker Requests data" + e).log();
		}
		return null;
	}
	
	@Override
	public JSONObject getMCModuleActionOperation(DataControllerRequest request, Map<String, String> inputParams) {
		MakerCheckerBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class).getBackendDelegate(MakerCheckerBackendDelegate.class);
		JSONObject finalResult = new JSONObject();
		try {

			JSONArray moduleDataArr = new JSONArray();
			JSONObject backendResult = backendDelegate.getMCModuleActionOperation(request, inputParams);

			if (backendResult != null && backendResult.has(UtilConstants.GET_MC_MODULEACTIONNAME_VIEW)) {
				JSONArray jsonArray = backendResult.getJSONArray(UtilConstants.GET_MC_MODULEACTIONNAME_VIEW);
				Set<String> moduleSet = new HashSet<>();
				
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject currModule = jsonArray.getJSONObject(i);
					
					if (!moduleSet.contains(currModule.optString(UtilConstants.MODULEID))) {
						JSONArray actionDataArr = new JSONArray();
						JSONObject moduleData = new JSONObject();
						
						moduleSet.add(currModule.optString(UtilConstants.MODULEID));
						moduleData.put(UtilConstants.MODULE_ID, currModule.optString(UtilConstants.MODULEID));
						moduleData.put(UtilConstants.MODULE_NAME,currModule.optString(UtilConstants.MODULENAME));
						
						for (int j = 0; j < jsonArray.length(); j++) {
							if (StringUtils.equals(jsonArray.getJSONObject(j).get(UtilConstants.MODULEID).toString(),
									moduleData.get(UtilConstants.MODULE_ID).toString())) {
								
								JSONObject actionData = new JSONObject();
								actionData.put(UtilConstants.REQUEST_ID,
										jsonArray.getJSONObject(j).get(UtilConstants.ACTIONID));
								actionData.put(UtilConstants.REQUESTNAME,
										jsonArray.getJSONObject(j).get(UtilConstants.ACTIONNAME));
								actionDataArr.put(actionData);
							}
						}
						
						moduleData.put(UtilConstants.REQUESTTYPE, actionDataArr);
						moduleDataArr.put(moduleData);
					}
				}
				finalResult.put(UtilConstants.MOUDLES, moduleDataArr);
			}

			return finalResult;

		}

		catch (Exception e) {
			diagnostic.prepareDebug("Exception occured while Fetching Module & Action Names" + e).log();
		}
		return null;
	}

	@Override
	public JSONObject withdrawRequest(DataControllerRequest request, Map<String, Object> inputParams) {
		JSONObject resultObject = new JSONObject();
		try {
			JSONObject rawresponse = CommonUtilities.getStringAsJSONObject(request.getServicesManager()
					.getIdentityHandler().getSecurityAttributes().get(UtilConstants.RAW_RESPONSE).toString());
			String userName = CommonUtilities
					.getStringAsJSONObject(rawresponse.get(UtilConstants.USER_ATTRIBUTES).toString())
					.getString(UtilConstants.USERNAME);
			String requestId = inputParams.getOrDefault("requestId", "").toString();
			String action = inputParams.getOrDefault("action", "").toString();
			HashMap<String, String> approvalRequestDetails = (HashMap<String, String>) inputParams
					.get("approvalRequestDetails");
			HashMap<String, String> configmap = extractMakerCheckerConfigMap(approvalRequestDetails);
			String module = configmap.get("auditModule");
			String event = configmap.get("auditEvent");
			
			if (UtilConstants.SID_WITHDRAWN.equalsIgnoreCase(action)) {
				JSONObject responseObj = MakerCheckerUtils.updateApprovalRequestStatus(request, requestId,
						UtilConstants.SID_WITHDRAWN, null, UtilConstants.SID_PENDING, userName);
				if (responseObj != null && !responseObj.isEmpty() && responseObj.has(UtilConstants.STATUS)) {
					if (responseObj.getString(UtilConstants.STATUS).equalsIgnoreCase(UtilConstants.SUCCESS)) {
						resultObject.put(UtilConstants.STATUS, "Request withdrawn successfully.");
						AuditHandler.auditAdminActivity(request, module, "Withdraw " + event,
								ActivityStatusEnum.SUCCESSFUL,
								" " + event + " " + module + " Request withdrawn successfully.");
					} else if (responseObj.getString(UtilConstants.STATUS).equalsIgnoreCase(UtilConstants.FAILURE)) {
						AuditHandler.auditAdminActivity(request, module, "Withdraw " + event, ActivityStatusEnum.FAILED,
								" " + event + " " + module + " Request withdraw failed.");
						return ErrorCodesEnum.ERR_10045.setErrorCode(responseObj);
					}
				} else {
					AuditHandler.auditAdminActivity(request, module, "Withdraw " + event, ActivityStatusEnum.FAILED,
							" " + event + " " + module + "Request withdraw failed.");
					return ErrorCodesEnum.ERR_10026.setErrorCode(responseObj);
				}
			} else {
				AuditHandler.auditAdminActivity(request, module, "Withdraw " + event, ActivityStatusEnum.FAILED,
						" " + event + " " + module + "Request withdraw failed.");
				return ErrorCodesEnum.ERR_10046.setErrorCode(resultObject);
			}
			
			return resultObject;
		} catch (Exception e) {
			diagnostic.prepareDebug("Exception while withdrawing the request. " + e.getMessage()).log();
		}
		return resultObject;
	}

	private HashMap<String, String> extractMakerCheckerConfigMap(HashMap<String, String> approvalRequestDetails) {
		JSONArray makerCheckerConfigArray = LoadMakerCheckerConfigData.getMakerCheckerConfigData();
		List<Object> makerCheckerConfigList = makerCheckerConfigArray.toList().stream().filter(item -> {
			HashMap<String, String> obj = (HashMap<String, String>) item;
			return obj.containsKey(UtilConstants.EXPAPIOPNAME)
					&& obj.get(UtilConstants.EXPAPIOPNAME)
							.equals(approvalRequestDetails.get(UtilConstants.EXPAPIOPNAME))
					&& obj.containsKey(UtilConstants.COMPANY_LEGAL_UNIT) && obj.get(UtilConstants.COMPANY_LEGAL_UNIT)
							.equals(approvalRequestDetails.get(UtilConstants.COMPANY_LEGAL_UNIT));
		}).collect(Collectors.toList());

		HashMap<String, String> configmap = new HashMap<String, String>();
		if (makerCheckerConfigArray.length() > 0) {
			configmap = (HashMap<String, String>) makerCheckerConfigList.get(0);
		}
		return configmap;
	}

	@Override
	public JSONObject viewCustomerDetails(DataControllerRequest request, DataControllerResponse response,
			Map<String, Object> inputParams) {
		MakerCheckerBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class).getBackendDelegate(MakerCheckerBackendDelegate.class);
		editedList.clear();
		JSONObject finalResult = new JSONObject();
		JSONObject generalInfo = new JSONObject();
		ExecutorService executor = Executors.newFixedThreadPool(2);
		try {
			JSONObject approvalRequestPayload = backendDelegate.getApprovalRequests(request,
					inputParams.get(UtilConstants.REQUEST_ID).toString());
			JSONObject parsedApprovalRequestPayload = parseApprovalRequestDetails(approvalRequestPayload, null);
			JSONObject userDetails = parsedApprovalRequestPayload.getJSONObject("userDetails");

			String customerId = userDetails.optString("id");
			String legalEntityId = userDetails.optString("legalEntityId");

			Map<String, String> reqPayload = new HashMap<String, String>();
			reqPayload.put("customerId", customerId);
			reqPayload.put("legalEntityId", legalEntityId);

			JSONObject editCustomerViewDetailsRes = backendDelegate.getEditCustomerViewDetails(request, reqPayload);

			JSONArray editCustomerViewDetailsArray = editCustomerViewDetailsRes.optJSONArray("records");
			
			if(editCustomerViewDetailsArray.length() > 0) {
				JSONObject customerObj = editCustomerViewDetailsArray.optJSONObject(0);
				String formattedCustomerSince = Arrays.asList(customerObj.get("customerSince").toString().split(" "))
						.get(0);
				generalInfo.put("userName", customerObj.get("userName"));
				generalInfo.put("customerTypeId", customerObj.get("customerTypeId"));
				generalInfo.put("customerTypeName", customerObj.get("customerTypeName"));
				generalInfo.put("customerTypeDescription", customerObj.get("customerTypeDescription"));
				generalInfo.put("primaryCustomerId", customerObj.get("customerId"));
				generalInfo.put("customerSince", formattedCustomerSince);
				generalInfo.put("customerStatus", customerObj.get("customerStatusName"));
				generalInfo.put("customerStatusId", customerObj.get("customerStatusId"));
				generalInfo.put("lastName", customerObj.get("lastName"));
				generalInfo.put("firstName", customerObj.get("firstName"));
			}
			
			finalResult.put("userDetails" , generalInfo);

			Map<String, Object> contractMapInfo = new HashMap<>();
			for (int i = 0; i < editCustomerViewDetailsArray.length(); i++) {
				JSONObject eachObj = editCustomerViewDetailsArray.getJSONObject(i);
				if (!contractMapInfo.containsKey(eachObj.get("contractId"))) {
					Map<String, Object> custObj = new HashMap<>();
					custObj.put("id", eachObj.get("customerId"));
					custObj.put("coreCustomerId", eachObj.get("coreCustomerId"));
					custObj.put("contractId", eachObj.get("contractId"));
					custObj.put("legalEntityId", eachObj.get("legalEntityId"));

					contractMapInfo.put(eachObj.get("contractId").toString(), custObj);
				}
			}
			ArrayList<Map<String, Object>> contractsPayload = new ArrayList<>();
			if (contractMapInfo.keySet().size() > 0) {
				for (String key : contractMapInfo.keySet()) {
					contractsPayload.add((Map<String, Object>) contractMapInfo.get(key));
				}

			}
			JSONArray finalAllUsersResponse = new JSONArray();
			List<CompletableFuture<Void>> futures = contractsPayload.stream()
					.map(requestBody -> CompletableFuture
							.supplyAsync(() -> fetchUserDetails(requestBody, request, response), executor)
							.thenAccept(userResponse -> {
								try {
									if (userResponse.length() > 0) {
										finalAllUsersResponse.put(userResponse);
									}
								} catch (Exception e) {

								}
							}))
					.collect(Collectors.toList());

			CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();
			
			request.addRequestParam_("customerDetails", editCustomerViewDetailsArray.toString());
			compareUserDetails(finalAllUsersResponse, parsedApprovalRequestPayload, finalResult);
			compareCompanyList(request, finalAllUsersResponse, parsedApprovalRequestPayload, finalResult);
			compareAccountLevelPermissions(finalAllUsersResponse, parsedApprovalRequestPayload, finalResult);
			compareGlobalLevelPermissions(finalAllUsersResponse, parsedApprovalRequestPayload, finalResult);
			compareTransactionLimits(finalAllUsersResponse, parsedApprovalRequestPayload, finalResult);
			finalResult.put("editedList",
					editedList.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", ""));
		} catch (Exception e) {
			diagnostic.prepareDebug("Exception while viewing the customer details. " + e.getMessage()).log();
		} finally {
			executor.shutdown();
		}
		return finalResult;
	}

	private void compareUserDetails(JSONArray oldObject, JSONObject newObject, JSONObject finalResult) {
		try {
			if (newObject.has("removedCompanies")) {
				JSONArray removedCompaniesArr = new JSONArray(newObject.get("removedCompanies").toString());
				if (removedCompaniesArr.length() > 0) {
					JSONObject removedCompaniesList = (JSONObject) newObject.getJSONArray("removedCompanies").get(0);
					finalResult.put("editedCustomer", removedCompaniesList.get("cif"));
					editedList.add(UtilConstants.COMPANY_LIST);
					finalResult.put("operationType", UtilConstants.DELETE);
				} else {
					JSONObject companyList = (JSONObject) newObject.getJSONArray("companyList").get(0);
					finalResult.put("editedCustomer", companyList.get("cif"));
					finalResult.put("operationType", UtilConstants.EDIT);
				}
			}
		} catch (Exception e) {
			diagnostic.prepareDebug("Exception while compareUserDetails. " + e.getMessage()).log();
		}
	}

	private void compareCompanyList(DataControllerRequest request, JSONArray oldObject, JSONObject newObject, JSONObject finalResult) {
		try {
			JSONObject existingCompanyInfoToCompare = new JSONObject();
			JSONArray remainingExistingCustomerList = new JSONArray();
			JSONArray existingCustomerList = new JSONArray();

			for (int i = 0; i < oldObject.length(); i++) {
				JSONObject eachOldObject = (JSONObject) oldObject.get(i);
				JSONArray companyListArray = eachOldObject.getJSONArray("companyList");
				for (int j = 0; j < companyListArray.length(); j++) {
					JSONObject eachCompanyInfo = (JSONObject) companyListArray.get(j);
					if (eachCompanyInfo.get("cif").equals(finalResult.get("editedCustomer"))
							&& !UtilConstants.DELETE.equals(finalResult.get("operationType"))) {
						existingCompanyInfoToCompare = new JSONObject(eachCompanyInfo.toString());
					} else {
						remainingExistingCustomerList.put(eachCompanyInfo);
					}
				}
			}
			existingCustomerList.putAll(remainingExistingCustomerList);
			existingCustomerList.put(existingCompanyInfoToCompare);
			if (!existingCompanyInfoToCompare.isEmpty()) {
				JSONObject newObjectCompanyList = (JSONObject) newObject.getJSONArray("companyList").get(0);
				JSONArray newAccountsList = newObjectCompanyList.getJSONArray("accounts");
				JSONArray oldAccountsList = existingCompanyInfoToCompare.getJSONArray("accounts");
				JSONArray newExcludedAccountsList = newObjectCompanyList.optJSONArray("excludedAccounts");
				JSONArray oldExcludedAccountsList = existingCompanyInfoToCompare.optJSONArray("excludedAccounts");
				String[] changedKeys = compareObject(existingCompanyInfoToCompare, newObjectCompanyList);
				if (changedKeys !=null && changedKeys.length > 0) {
					editedList.add(UtilConstants.COMPANY_LIST);
					JSONObject changedObject = new JSONObject();
					for (String key : changedKeys) {
						changedObject.put(key, existingCompanyInfoToCompare.get(key));
					}
					finalResult.put("companyListOld", changedObject);
				}
				finalResult.put("addedAccounts", getNewlyAddedAccounts(oldAccountsList, newAccountsList));
				finalResult.put("excludedAccounts", getExcludedAccounts(oldExcludedAccountsList, newExcludedAccountsList));
				if (!finalResult.get("addedAccounts").toString().isBlank()
						|| !finalResult.get("excludedAccounts").toString().isBlank()) {
					editedList.add(UtilConstants.COMPANY_LIST);
				}
				remainingExistingCustomerList.put(newObjectCompanyList);

			} else if(!UtilConstants.DELETE.equals(finalResult.get("operationType"))){
				finalResult.put("operationType", UtilConstants.ADD);
				editedList.add(UtilConstants.COMPANY_LIST);
				JSONObject companyWithAllDetails = getMissingDetailsFromSP(request, (JSONObject) newObject.getJSONArray("companyList").get(0), UtilConstants.COMPANY_LIST);
				remainingExistingCustomerList.put(companyWithAllDetails);
			}
			finalResult.put("companyList", remainingExistingCustomerList);
			mapRoleName(finalResult, existingCustomerList);
			mapAccountStatus(request, finalResult);

		} catch (Exception e) {
			diagnostic.prepareDebug("Exception while compareCompanyList. " + e.getMessage()).log();
		}

	}
	
	private void compareAccountLevelPermissions(JSONArray oldObject, JSONObject newObject, JSONObject finalResult) {
		try {
			JSONObject existingAccountLevelPermissionToCompare = new JSONObject();
			JSONArray remainingExistingAccountLevelPermissions = new JSONArray();
			JSONArray changedFeaturePermissions = new JSONArray();
			JSONObject newObjectAccLevelPermission = (JSONObject) newObject.getJSONArray("accountLevelPermissions")
					.get(0);
			for (int i = 0; i < oldObject.length(); i++) {
				JSONObject eachOldObject = (JSONObject) oldObject.get(i);
				JSONArray oldAccountLevelPermissionsArray = eachOldObject.getJSONArray("accountLevelPermissions");
				JSONObject oldAccountLevelPermissions = (JSONObject) oldAccountLevelPermissionsArray.get(0);
				if (oldAccountLevelPermissions.get("cif").equals(finalResult.get("editedCustomer"))
						&& !UtilConstants.DELETE.equals(finalResult.get("operationType"))) {
					existingAccountLevelPermissionToCompare = new JSONObject(oldAccountLevelPermissions.toString());
				} else {
					remainingExistingAccountLevelPermissions.put(oldAccountLevelPermissions);
				}
			}

			if (!existingAccountLevelPermissionToCompare.isEmpty()) {

				JSONArray existingAccounts = existingAccountLevelPermissionToCompare.getJSONArray("accounts");
				compareAccountPermissions(remainingExistingAccountLevelPermissions, changedFeaturePermissions,
						existingAccounts, newObjectAccLevelPermission, finalResult.get("editedCustomer").toString());

			} else if (!UtilConstants.DELETE.equals(finalResult.get("operationType"))) {
				editedList.add(UtilConstants.ACCOUNT_LEVEL_PERMISSIONS);
				JSONObject accountLevelPermissionsWithAllDetails = getMissingDetailsFromSP(null,
						newObjectAccLevelPermission, UtilConstants.ACCOUNT_LEVEL_PERMISSIONS);
				remainingExistingAccountLevelPermissions.put(accountLevelPermissionsWithAllDetails);
			}

			if (changedFeaturePermissions.length() > 0) {
				finalResult.put("accountLevelPermissionsOld", changedFeaturePermissions);
			}
			finalResult.put("accountLevelPermissions", remainingExistingAccountLevelPermissions);

		} catch (Exception e) {
			diagnostic.prepareDebug("Exception while compareAccountLevelPermissions. " + e.getMessage()).log();
		}
	}
	
	private void compareAccountPermissions(JSONArray remainingExistingAccountLevelPermissions,
			JSONArray changedFeaturePermissions, JSONArray existingAccounts, JSONObject newObjectAccLevelPermission, String coreCustomerId)
			throws JSONException {
		
		JSONArray newAccounts = newObjectAccLevelPermission.getJSONArray("accounts");

		Iterator<Object> existingAccountsIterator = existingAccounts.iterator();

		while (existingAccountsIterator.hasNext()) {

			JSONObject eachExistingAccount = (JSONObject) existingAccountsIterator.next();

			Optional<Object> isAccountPresent = StreamSupport.stream(newAccounts.spliterator(), true)
					.filter(item -> StringUtils.equalsIgnoreCase(((JSONObject) item).optString("accountId"),
							eachExistingAccount.optString("accountId")))
					.findAny();

			JSONObject eachNewAccount = new JSONObject();

			if (isAccountPresent.isPresent()) {
				eachNewAccount = (JSONObject) isAccountPresent.get();
				JSONArray existingFeaturePermissions = eachExistingAccount.getJSONArray("featurePermissions");
				JSONArray newFeaturePermissions = eachNewAccount.getJSONArray("featurePermissions");
				Iterator<Object> existingFeaturePermissionsIterator = existingFeaturePermissions.iterator();

				while (existingFeaturePermissionsIterator.hasNext()) {

					JSONObject eachExistingFeaturePermissions = (JSONObject) existingFeaturePermissionsIterator
							.next();
					JSONObject changedObject = new JSONObject();

					Optional<Object> isFeaturePermissionPresent = StreamSupport
							.stream(newFeaturePermissions.spliterator(), true)
							.filter(item -> StringUtils.equalsIgnoreCase(
									((JSONObject) item).optString("featureId"),
									eachExistingFeaturePermissions.optString("featureId")))
							.findAny();

					JSONObject eachNewFeaturePermissions = new JSONObject();

					if (isFeaturePermissionPresent.isPresent()) {
						eachNewFeaturePermissions = (JSONObject) isFeaturePermissionPresent.get();
						String[] changedKeys = compareObject(eachExistingFeaturePermissions,
								eachNewFeaturePermissions);
						if (null != changedKeys) {
							changedObject.put("featureId", eachExistingFeaturePermissions.get("featureId"));
							changedObject.put("accountId", eachExistingAccount.optString("accountId"));
							changedObject.put("featureId",
									eachExistingFeaturePermissions.optString("featureId"));
							changedObject.put("featureName",
									eachExistingFeaturePermissions.optString("featureName"));
							changedObject.put("featureDescription",
									eachExistingFeaturePermissions.optString("featureDescription"));
							for (String key : changedKeys) {
								changedObject.put(key, eachExistingFeaturePermissions.get(key));
							}
							changedObject.put("coreCustomerId" , coreCustomerId);
							changedFeaturePermissions.put(changedObject);
							editedList.add(UtilConstants.ACCOUNT_LEVEL_PERMISSIONS);
						}
						JSONArray existingPermissions = eachExistingFeaturePermissions
								.optJSONArray("permissions");
						JSONArray newPermissions = eachNewFeaturePermissions.optJSONArray("permissions");
						Iterator<Object> existingPermissionsIterator = existingPermissions.iterator();

						while (existingPermissionsIterator.hasNext()) {

							JSONObject eachExistingPermissions = (JSONObject) existingPermissionsIterator
									.next();

							Optional<Object> isPermissionPresent = StreamSupport
									.stream(newPermissions.spliterator(), true)
									.filter(item -> StringUtils.equalsIgnoreCase(
											((JSONObject) item).optString("id"),
											eachExistingPermissions.optString("id")))
									.findAny();

							JSONObject eachNewPermissions = new JSONObject();

							if (isPermissionPresent.isPresent()) {
								eachNewPermissions = (JSONObject) isPermissionPresent.get();
								String[] changedPermissionKeys = compareObject(eachExistingPermissions,
										eachNewPermissions);
								if (null != changedPermissionKeys) {
									JSONObject changedPermissionObject = new JSONObject();
									changedPermissionObject.put("id", eachExistingPermissions.get("id"));
									changedPermissionObject.put("accountId",
											eachExistingAccount.optString("accountId"));
									changedPermissionObject.put("featureId",
											eachExistingFeaturePermissions.optString("featureId"));
									changedPermissionObject.put("featureName",
											eachExistingFeaturePermissions.optString("featureName"));
									changedPermissionObject.put("featureDescription",
											eachExistingFeaturePermissions.optString("featureDescription"));
									for (String key : changedPermissionKeys) {
										changedPermissionObject.put(key, eachExistingPermissions.get(key));
									}
									changedPermissionObject.put("coreCustomerId" , coreCustomerId);
									changedFeaturePermissions.put(changedPermissionObject);
									editedList.add(UtilConstants.ACCOUNT_LEVEL_PERMISSIONS);
								}
							}
						}
					}

				}

			}

		}
		remainingExistingAccountLevelPermissions.put(newObjectAccLevelPermission);
	}
	
	private void compareGlobalLevelPermissions(JSONArray oldObject, JSONObject newObject, JSONObject finalResult) {
		try {
			JSONObject existingGlobalLevelPermissionToCompare = new JSONObject();
			JSONArray remainingExistingGlobalLevelPermissions = new JSONArray();
			JSONArray changedGlobalLevelFeaturePermissions = new JSONArray();
			JSONObject newObjectGlobalLevelPermission = (JSONObject) newObject.getJSONArray("globalLevelPermissions")
					.get(0);

			for (int i = 0; i < oldObject.length(); i++) {
				JSONObject eachOldObject = (JSONObject) oldObject.get(i);
				JSONArray oldGlobalLevelPermissionsArray = eachOldObject.getJSONArray("globalLevelPermissions");
				JSONObject oldGlobalLevelPermissions = (JSONObject) oldGlobalLevelPermissionsArray.get(0);
				if (oldGlobalLevelPermissions.get("cif").equals(finalResult.get("editedCustomer"))
						&& !UtilConstants.DELETE.equals(finalResult.get("operationType"))) {
					existingGlobalLevelPermissionToCompare = new JSONObject(oldGlobalLevelPermissions.toString());
				} else {
					remainingExistingGlobalLevelPermissions.put(oldGlobalLevelPermissions);
				}
			}

			if (!existingGlobalLevelPermissionToCompare.isEmpty()) {

				JSONArray existingGlobalLevelFeatures = existingGlobalLevelPermissionToCompare.getJSONArray("features");
				compareGlobalPermissions(remainingExistingGlobalLevelPermissions, changedGlobalLevelFeaturePermissions,
						existingGlobalLevelFeatures, newObjectGlobalLevelPermission, finalResult.get("editedCustomer").toString());
				
			} else if (!UtilConstants.DELETE.equals(finalResult.get("operationType"))) {
				editedList.add(UtilConstants.GLOBAL_LEVEL_PERMISSIONS);
				JSONObject globalLevelPermissionsWithAllDetails = getMissingDetailsFromSP(null,
						newObjectGlobalLevelPermission, UtilConstants.GLOBAL_LEVEL_PERMISSIONS);
				remainingExistingGlobalLevelPermissions.put(globalLevelPermissionsWithAllDetails);
			}

			if (changedGlobalLevelFeaturePermissions.length() > 0) {
				finalResult.put("globalLevelPermissionsOld", changedGlobalLevelFeaturePermissions);
			}
			finalResult.put("globalLevelPermissions", remainingExistingGlobalLevelPermissions);

		} catch (Exception e) {
			diagnostic.prepareDebug("Exception while compareglobalLevelPermissions. " + e.getMessage()).log();
		}
	}
	
	private void compareGlobalPermissions(JSONArray remainingExistingGlobalLevelPermissions,
			JSONArray changedGlobalLevelFeaturePermissions, JSONArray existingGlobalLevelFeatures,
			JSONObject newObjectGlobalLevelPermission, String coreCustomerId) throws JSONException {
		JSONArray newFeatures = newObjectGlobalLevelPermission.getJSONArray("features");

		Iterator<Object> existingGlobalLevelFeaturesIterator = existingGlobalLevelFeatures.iterator();				

		while (existingGlobalLevelFeaturesIterator.hasNext()) {

			JSONObject eachExistingFeature = (JSONObject) existingGlobalLevelFeaturesIterator.next();

			Optional<Object> isFeaturePresent = StreamSupport.stream(newFeatures.spliterator(), true)
					.filter(item -> StringUtils.equalsIgnoreCase(((JSONObject) item).optString("featureId"),
							eachExistingFeature.optString("featureId")))
					.findAny();

			JSONObject eachNewFeature = new JSONObject();

			if (isFeaturePresent.isPresent()) {
				eachNewFeature = (JSONObject) isFeaturePresent.get();
				String[] changedKeys = compareObject(eachExistingFeature, eachNewFeature);
				JSONObject changedObject = new JSONObject();
				if (null != changedKeys) {
					changedObject.put("featureId", eachExistingFeature.get("featureId"));
					changedObject.put("featureId", eachExistingFeature.optString("featureId"));
					changedObject.put("featureName", eachExistingFeature.optString("featureName"));
					changedObject.put("featureDescription",
							eachExistingFeature.optString("featureDescription"));
					for (String key : changedKeys) {
						changedObject.put(key, eachExistingFeature.get(key));
					}
					changedObject.put("coreCustomerId" , coreCustomerId);
					changedGlobalLevelFeaturePermissions.put(changedObject);
					editedList.add(UtilConstants.GLOBAL_LEVEL_PERMISSIONS);
				}

				JSONArray existingPermissions = eachExistingFeature.optJSONArray("permissions");
				JSONArray newPermissions = eachNewFeature.optJSONArray("permissions");
				Iterator<Object> existingPermissionsIterator = existingPermissions.iterator();

				while (existingPermissionsIterator.hasNext()) {

					JSONObject eachExistingPermissions = (JSONObject) existingPermissionsIterator.next();

					Optional<Object> isPermissionPresent = StreamSupport
							.stream(newPermissions.spliterator(), true)
							.filter(item -> StringUtils.equalsIgnoreCase(((JSONObject) item).optString("id"),
									eachExistingPermissions.optString("id")))
							.findAny();

					JSONObject eachNewPermissions = new JSONObject();

					if (isPermissionPresent.isPresent()) {
						eachNewPermissions = (JSONObject) isPermissionPresent.get();
						String[] changedPermissionKeys = compareObject(eachExistingPermissions,
								eachNewPermissions);
						if (null != changedPermissionKeys) {
							JSONObject changedPermissionObject = new JSONObject();
							changedPermissionObject.put("id", eachExistingPermissions.get("id"));
							changedPermissionObject.put("featureId",
									eachExistingFeature.optString("featureId"));
							changedPermissionObject.put("featureName",
									eachExistingFeature.optString("featureName"));
							changedPermissionObject.put("featureDescription",
									eachExistingFeature.optString("featureDescription"));
							for (String key : changedPermissionKeys) {
								changedPermissionObject.put(key, eachExistingPermissions.get(key));
							}
							changedPermissionObject.put("coreCustomerId" , coreCustomerId);
							changedGlobalLevelFeaturePermissions.put(changedPermissionObject);
							editedList.add(UtilConstants.GLOBAL_LEVEL_PERMISSIONS);
						}
					}
				}
			}
		}
		remainingExistingGlobalLevelPermissions.put(newObjectGlobalLevelPermission);
	}
	
	private void mapRoleName(JSONObject finalResult, JSONArray existingCustomerList) {
		try {
			if (existingCustomerList.length() > 0) {
				JSONObject companyObj = (JSONObject) existingCustomerList.get(0);
				JSONArray validRoles = companyObj.getJSONArray("validRoles");
				Map<String, String> roleMap = StreamSupport.stream(validRoles.spliterator(), true)
						.map(role -> (JSONObject) role).collect(Collectors.toMap(role -> role.get("roleId").toString(),
								role -> role.get("userRole").toString()));

				if (finalResult.has("companyList") && finalResult.getJSONArray("companyList").length() > 0) {
					JSONArray companyList = finalResult.getJSONArray("companyList");

					StreamSupport.stream(companyList.spliterator(), true).map(eachCompany -> (JSONObject) eachCompany)
							.forEach(eachCompany -> {
								eachCompany.put("roleName", roleMap.get(eachCompany.getString("roleId")));

							});
				}
				if (finalResult.has("companyListOld") && !finalResult.getJSONObject("companyListOld").isEmpty()) {
					JSONObject companyListOld = finalResult.getJSONObject("companyListOld");
					companyListOld.put("roleName", roleMap.get(companyListOld.getString("roleId")));
				}
			}
		} catch (Exception e) {
			diagnostic.prepareDebug("Exception while mapRoleName. " + e.getMessage()).log();
		}
	}
	
	private void mapAccountStatus(DataControllerRequest request, JSONObject finalResult) {
		try {
			String customerDetails = request.getParameter("customerDetails");
			JSONArray customerDetailsArray = new JSONArray(customerDetails);
			JSONArray companyListArray = finalResult.getJSONArray("companyList");
			for (int i = 0; i < companyListArray.length(); i++) {
				JSONObject companyObject = (JSONObject) companyListArray.get(i);
				JSONArray accounts = companyObject.getJSONArray("accounts");
				Iterator<Object> accountsIterator = accounts.iterator();
				while (accountsIterator.hasNext()) {
					JSONObject accountObject = (JSONObject) accountsIterator.next();
					StreamSupport.stream(customerDetailsArray.spliterator(), true)
							.map(customerDetail -> (JSONObject) customerDetail).forEach(customerDetail -> {
								if (customerDetail.get("accountId").equals(accountObject.get("accountId"))) {
									accountObject.put("accountStatus", customerDetail.get("accountStatus"));
								}
							});
				}
			}
			
			for (int i = 0; i < companyListArray.length(); i++) {
				JSONObject companyObject = (JSONObject) companyListArray.get(i);
				JSONArray excludedAccounts = companyObject.optJSONArray("excludedAccounts");
				if (excludedAccounts.length() > 1) {
					Iterator<Object> excludedAccountsIterator = excludedAccounts.iterator();
					while (excludedAccountsIterator.hasNext()) {
						JSONObject excludedAccountObject = (JSONObject) excludedAccountsIterator.next();
						StreamSupport.stream(customerDetailsArray.spliterator(), true)
								.map(customerDetail -> (JSONObject) customerDetail).forEach(customerDetail -> {
									if (customerDetail.get("accountId")
											.equals(excludedAccountObject.get("accountId"))) {
										excludedAccountObject.put("accountStatus", customerDetail.get("accountStatus"));
									}
								});
					}
				}
			}
		} catch (Exception e) {
			diagnostic.prepareDebug("Exception while mapAccountStatus. " + e.getMessage()).log();
		}
	}

	private void compareTransactionLimits(JSONArray oldObject, JSONObject newObject, JSONObject finalResult) {
		JSONObject changedTransactionLimits = new JSONObject();
		try {
			JSONObject existingTransactionLimitToCompare = new JSONObject();
			JSONArray remainingExistingTransactionList = new JSONArray();

			JSONObject newObjectTransactionLimit = (JSONObject) newObject.getJSONArray("transactionLimits").get(0);
			for (int i = 0; i < oldObject.length(); i++) {
				JSONObject eachOldObject = (JSONObject) oldObject.get(i);
				JSONArray transactionLimitArray = eachOldObject.getJSONArray("transactionLimits");
				JSONObject oldTransactionLimit = (JSONObject) transactionLimitArray.get(0);
				if (oldTransactionLimit.get("cif").equals(finalResult.get("editedCustomer"))
						&& !"delete".equals(finalResult.get("operationType"))) {
					existingTransactionLimitToCompare = new JSONObject(oldTransactionLimit.toString());
				} else {
					remainingExistingTransactionList.put(oldTransactionLimit);
				}
			}

			if (!existingTransactionLimitToCompare.isEmpty()) {
				

				// To compare and format global level limits
				JSONArray changedLimits = compareAndFormatLimitGroups(existingTransactionLimitToCompare,
						newObjectTransactionLimit);
				if (changedLimits.length() > 0) {
					changedTransactionLimits.put("limitGroups", changedLimits);
				}

				// To compare and format account level limits
				JSONArray changedAccountLimits = compareAndFormatAccountLimits(existingTransactionLimitToCompare,
						newObjectTransactionLimit);
				if (changedAccountLimits.length() > 0) {
					changedTransactionLimits.put("accounts", changedAccountLimits);
				}
				remainingExistingTransactionList.put(newObjectTransactionLimit);
				if (changedTransactionLimits.length() > 0) {
					finalResult.put("transactionLimitsOld", changedTransactionLimits);
					editedList.add(UtilConstants.TRANSACTION_LIMITS);
				}	
			} else if(!UtilConstants.DELETE.equals(finalResult.get("operationType"))){
				editedList.add(UtilConstants.TRANSACTION_LIMITS);
				JSONObject transactionLimitsWithAllDetails = getMissingDetailsFromSP(null, newObjectTransactionLimit, UtilConstants.TRANSACTION_LIMITS);
				remainingExistingTransactionList.put(transactionLimitsWithAllDetails);
			}
			finalResult.put("transactionLimits", remainingExistingTransactionList);
		} catch (Exception e) {
			diagnostic.prepareDebug("Exception while compareTransactionLimits. " + e.getMessage()).log();
		}
	}

	private JSONArray compareAndFormatLimitGroups(JSONObject existingTransactionLimitToCompare,
			JSONObject newObjectTransactionLimit) {
		JSONArray changedLimits = new JSONArray();
		try {
			JSONArray existingLimitGroups = existingTransactionLimitToCompare.getJSONArray("limitGroups");
			JSONArray newLimitGroups = newObjectTransactionLimit.getJSONArray("limitGroups");

			Iterator<Object> existingLimitGroupsIterator = existingLimitGroups.iterator();

			while (existingLimitGroupsIterator.hasNext()) {
				JSONObject existingLimits = (JSONObject) existingLimitGroupsIterator.next();
				StreamSupport.stream(newLimitGroups.spliterator(), true).map(limitGroup -> (JSONObject) limitGroup)
						.forEach(limitGroup -> {
							if (!existingLimits.get("limitGroupId").toString().isBlank() && existingLimits
									.get("limitGroupId").equals(limitGroup.optString("limitGroupId"))) {
								limitGroup.put("limitGroupName",existingLimits.optString("limitGroupName"));
								JSONArray existingLimitsArray = existingLimits.getJSONArray("limits");
								JSONArray newLimitsArray = limitGroup.getJSONArray("limits");
								Iterator<Object> existingLimitsArrayIterator = existingLimitsArray.iterator();

								while (existingLimitsArrayIterator.hasNext()) {
									JSONObject eachExistingLimit = (JSONObject) existingLimitsArrayIterator.next();
									StreamSupport.stream(newLimitsArray.spliterator(), true)
											.map(eachLimit -> (JSONObject) eachLimit).forEach(eachLimit -> {
												JSONObject changedObject = new JSONObject();
												if (eachExistingLimit.get("id").equals(eachLimit.get("id"))) {
													String[] changedKeys = compareObject(eachExistingLimit, eachLimit);
													if (changedKeys != null && changedKeys.length > 0) {
														changedObject.put("id", eachExistingLimit.get("id"));
														changedObject.put("limitGroupId",
																existingLimits.get("limitGroupId"));
														changedObject.put("limitGroupName",
																existingLimits.get("limitGroupName"));
														changedObject.put("limitGroupDescription",
																existingLimits.get("limitGroupDescription"));
														for (String key : changedKeys) {
															changedObject.put(key, eachExistingLimit.get(key));
														}
														changedLimits.put(changedObject);
													}
												}
											});
								}
							}
						});

			}
		} catch (Exception e) {
			diagnostic.prepareDebug("Exception while compareAndFormatLimitGroups. " + e.getMessage()).log();
		}
		return changedLimits;
	}

	private JSONArray compareAndFormatAccountLimits(JSONObject existingTransactionLimitToCompare,
			JSONObject newObjectTransactionLimit) {
		JSONObject changedAccounts = new JSONObject();
		JSONArray changedAccountsList = new JSONArray();
		try {
			JSONArray existingAccounts = existingTransactionLimitToCompare.getJSONArray("accounts");
			JSONArray newAccounts = newObjectTransactionLimit.getJSONArray("accounts");
			JSONArray changedFeaturePermissionlimits = new JSONArray();
			JSONArray changedAccountlimits = new JSONArray();

			Iterator<Object> existingAccountsIterator = existingAccounts.iterator();
			while (existingAccountsIterator.hasNext()) {
				JSONObject existingAccount = (JSONObject) existingAccountsIterator.next();
				Optional<JSONObject> isAccountPresent = StreamSupport.stream(newAccounts.spliterator(), true)
						.map(newAccount -> (JSONObject) newAccount).filter(newAccount -> existingAccount
								.get("accountId").equals(newAccount.optString("accountId")))
						.findAny();
				if (isAccountPresent.isPresent()) {
					JSONObject newAccount = isAccountPresent.get();
					JSONArray existingFeaturePermissions = existingAccount.getJSONArray("featurePermissions");
					Iterator<Object> existingFeaturePermissionsIterator = existingFeaturePermissions.iterator();
					JSONArray newFeaturePermissions = newAccount.getJSONArray("featurePermissions");

					changedFeaturePermissionlimits = new JSONArray();
					while (existingFeaturePermissionsIterator.hasNext()) {

						JSONObject existingFeaturePermission = (JSONObject) existingFeaturePermissionsIterator.next();

						Optional<JSONObject> isFeaturePermissionPresent = StreamSupport
								.stream(newFeaturePermissions.spliterator(), true)
								.map(featurePermission -> (JSONObject) featurePermission)
								.filter(featurePermission -> existingFeaturePermission.get("featureId")
										.equals(featurePermission.get("featureId"))
										&& existingFeaturePermission.get("actionId")
												.equals(featurePermission.get("actionId")))
								.findAny();

						if (isFeaturePermissionPresent.isPresent()) {
							JSONObject featurePermission = isFeaturePermissionPresent.get();
							JSONArray existingLimits = existingFeaturePermission.getJSONArray("limits");
							Iterator<Object> existingLimitsIterator = existingLimits.iterator();
							JSONArray newLimits = featurePermission.getJSONArray("limits");
							featurePermission.put("featureName", existingFeaturePermission.get("featureName"));
							
							changedAccountlimits = new JSONArray();
							while (existingLimitsIterator.hasNext()) {
								JSONObject eachExistingLimit = (JSONObject) existingLimitsIterator.next();
								Optional<JSONObject> isLimitPresent = StreamSupport
										.stream(newLimits.spliterator(), true).map(limit -> (JSONObject) limit)
										.filter(limit -> eachExistingLimit.get("id").equals(limit.get("id"))).findAny();

								JSONObject changedObject = new JSONObject();
								if (isLimitPresent.isPresent()) {
									String[] changedKeys = compareObject(eachExistingLimit, isLimitPresent.get());
									if (changedKeys != null && changedKeys.length > 0) {
										changedObject.put("id", eachExistingLimit.get("id"));

										for (String key : changedKeys) {
											changedObject.put(key, eachExistingLimit.get(key));
										}
										changedAccountlimits.put(changedObject);
									}
								}
							}
							if (changedAccountlimits.length() > 0) {
								existingFeaturePermission.put("limits", changedAccountlimits);
								changedFeaturePermissionlimits.put(existingFeaturePermission);
							}
						}
					}
					if (changedFeaturePermissionlimits.length() > 0) {
						changedAccounts.put("featurePermissions", changedFeaturePermissionlimits);
						changedAccounts.put("accountName", existingAccount.optString("accountName"));
						changedAccounts.put("accountId", existingAccount.optString("accountId"));
						changedAccounts.put("accountType", existingAccount.optString("accountType"));
						changedAccounts.put("ownerType", existingAccount.optString("ownerType"));
					}

				}
				if(changedAccounts.length() > 0) {
					changedAccountsList.put(changedAccounts);
				}
			}
		} catch (Exception e) {
			diagnostic.prepareDebug("Exception while compareAndFormatAccountLimits. " + e.getMessage()).log();
		}
		return changedAccountsList;
	}
	
	private JSONObject getMissingDetailsFromSP(DataControllerRequest request, JSONObject list, String calledFrom) {
		MakerCheckerBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class).getBackendDelegate(MakerCheckerBackendDelegate.class);
		try {

			switch (calledFrom) {
			case UtilConstants.COMPANY_LIST:

				Map<String, String> inputParams = new HashMap<>();

				inputParams.put("legalEntityId", list.optString("legalEntityId"));
				inputParams.put("serviceDefIds", list.optString("serviceDefinition"));
				inputParams.put("roleIds", list.optString("roleId"));
				inputParams.put("cifIds", list.optString("cif"));

				if (enrollCusDetails.isEmpty()) {
					enrollCusDetails = backendDelegate.getEnrollCustomerViewDetails(request, inputParams);
				}

				JSONArray servicedefRecord = enrollCusDetails.optJSONArray(UtilConstants.RECORDS);
				JSONArray roleRecord = enrollCusDetails.optJSONArray(UtilConstants.RECORDS1);
				JSONArray accountsRecord = enrollCusDetails.optJSONArray("records2");

				JSONObject serviceDefinitionObj = (JSONObject) servicedefRecord.get(0);
				JSONObject roleObj = (JSONObject) roleRecord.get(0);

				list.put("serviceDefinitionName", serviceDefinitionObj.get("serviceDefinitionName"));
				list.put("contractType", serviceDefinitionObj.get("serviceType"));
				list.put("serviceTypeName", serviceDefinitionObj.get("serviceTypeName"));
				list.put("roleName", roleObj.get("roleName"));

				JSONArray companyListAccounts = list.getJSONArray("accounts");
				Iterator<Object> accountsIterator = companyListAccounts.iterator();
				while (accountsIterator.hasNext()) {
					JSONObject eachAccount = (JSONObject) accountsIterator.next();
					StreamSupport.stream(accountsRecord.spliterator(), true)
							.map(accountFromSP -> (JSONObject) accountFromSP).forEach(accountFromSP -> {
								if (accountFromSP.get("accountId").equals(eachAccount.get("accountId"))) {
									eachAccount.put("accountStatus", accountFromSP.get("accountStatus"));
									eachAccount.put("ownerType", accountFromSP.get("ownerType"));
								}
							});
				}
				break;

			case UtilConstants.GLOBAL_LEVEL_PERMISSIONS:
				JSONArray featureActionsRecord = enrollCusDetails.optJSONArray("records4");
				JSONArray features = list.getJSONArray("features");
				features = addFeaturesData(featureActionsRecord, features);
				break;

			case UtilConstants.ACCOUNT_LEVEL_PERMISSIONS:
				JSONArray featureActionsRecord1 = enrollCusDetails.optJSONArray("records4");

				JSONArray accounts = list.getJSONArray("accounts");
				accounts.forEach(item -> {
					JSONObject accountInfo = (JSONObject) item;
					JSONArray featurePermissions = accountInfo.getJSONArray("featurePermissions");
					featurePermissions = addFeaturesData(featureActionsRecord1, featurePermissions);
				});

				break;

			case UtilConstants.TRANSACTION_LIMITS:

				JSONArray limitsRecord = enrollCusDetails.optJSONArray("records5");

				JSONArray limitGroups = list.getJSONArray("limitGroups");
				Iterator<Object> limitGroupsIterator = limitGroups.iterator();
				while (limitGroupsIterator.hasNext()) {
					JSONObject eachLimitGroup = (JSONObject) limitGroupsIterator.next();
					StreamSupport.stream(limitsRecord.spliterator(), true).map(limitFromSP -> (JSONObject) limitFromSP)
							.forEach(limitFromSP -> {
								if (limitFromSP.optString("limitGroupId")
										.equals(eachLimitGroup.optString("limitGroupId"))) {
									eachLimitGroup.put("limitGroupName", limitFromSP.optString("limitGroupName"));
									eachLimitGroup.put("limitGroupDescription",
											limitFromSP.optString("limitGroupDescription"));
								}
							});
				}

				break;
			}

		} catch (Exception e) {
			diagnostic.prepareDebug("Exception while getMissingDetailsFromSP. " + e.getMessage()).log();
		}
		return list;
	}
	
	private String getNewlyAddedAccounts(JSONArray oldAccountsList, JSONArray newAccountsList) {
		Set<String> addedAccounts = new HashSet<>();
		try {
			Iterator<Object> existingIterator = oldAccountsList.iterator();
			while (existingIterator.hasNext()) {
				JSONObject existingObject = (JSONObject) existingIterator.next();
				StreamSupport.stream(newAccountsList.spliterator(), true).map(newObject -> (JSONObject) newObject)
						.forEach(newObject -> {
							if (existingObject.get("accountId").equals(newObject.get("accountId"))) {
								compareObject(existingObject, newObject);
								if (!existingObject.get("isEnabled").equals(newObject.get("isEnabled"))) {
									addedAccounts.add(existingObject.get("accountId").toString());
								}
							}
						});
			}
		} catch (Exception e) {
			diagnostic.prepareDebug("Exception while getNewlyAddedAccounts. " + e.getMessage()).log();
		}
		return String.join(",", addedAccounts);
	}
	
	private String getExcludedAccounts(JSONArray oldAccountsList, JSONArray newAccountsList) {
		Set<String> commonExcludedAccounts = new HashSet<>();
		List<String> newExcludedAccountIds = new ArrayList<>();
		try {
			if (newAccountsList.length() > 0) {
				newExcludedAccountIds = IntStream.range(0, newAccountsList.length())
						.mapToObj(newAccountsList::getJSONObject).map(jsonObject -> jsonObject.getString("accountId"))
						.collect(Collectors.toList());
			}

			if (oldAccountsList.length() > 0 && newAccountsList.length() > 0) {
				Iterator<Object> existingIterator = oldAccountsList.iterator();
				while (existingIterator.hasNext()) {
					JSONObject existingObject = (JSONObject) existingIterator.next();
					Optional<JSONObject> isExcludedAccountPresent = StreamSupport
							.stream(newAccountsList.spliterator(), true).map(newObject -> (JSONObject) newObject)
							.filter(newObject -> existingObject.get("accountId").equals(newObject.get("accountId")))
							.findAny();
					if (isExcludedAccountPresent.isPresent()) {
						commonExcludedAccounts.add(isExcludedAccountPresent.get().optString("accountId"));
					}
				}

				newExcludedAccountIds.removeAll(commonExcludedAccounts);
			} 

		} catch (Exception e) {
			diagnostic.prepareDebug("Exception while getNewlyAddedAccounts. " + e.getMessage()).log();
		}
		return String.join(",", newExcludedAccountIds);
	}

	private String[] compareObject(JSONObject oldObject, JSONObject newObject) {
		String[] changedKeys = null;
		try {
			StringBuilder sb = new StringBuilder();
			if (!oldObject.isEmpty() && !newObject.isEmpty()) {
				String[] keyNames = JSONObject.getNames(oldObject);
				for (String key : keyNames) {
					if (oldObject.has(key) && newObject.has(key)) {
						if (!oldObject.get(key).toString().startsWith("{")
								&& !oldObject.get(key).toString().startsWith("[")) {
							if (!oldObject.get(key).equals(newObject.get(key))) {
								sb.append(key + ",");
							}
						}
					} else if (!newObject.has(key)
							&& Arrays.asList(UtilConstants.EDIT_CUSTOMER_CONST_LIST.split(",")).contains(key)) {
						newObject.put(key, oldObject.get(key));
					} 
				}
			}
			if(sb.length() > 0) {
				changedKeys = sb.toString().split(",");
			}
		} catch (Exception e) {
			diagnostic.prepareDebug("Exception while compareObject. " + e.getMessage()).log();
		}
		return changedKeys;
	}
	
	
	private JSONObject fetchUserDetails(Map<String, Object> payload, DataControllerRequest request,
			DataControllerResponse response) {
		String dbpServicesClaimsToken = null;
		try {
			dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(request);
		} catch (DBPAuthenticationException e) {
			diagnostic.prepareDebug("Exception in fetchUserDetails" + e.getMessage()).log();
		}

		Map<String, Object> headerMap = new HashMap<>();
		headerMap.put("backendToken", dbpServicesClaimsToken);

		String serviceResponse = null;
		try {
			serviceResponse = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
					.withOperationId(OperationName.OP_GETINFINITYUSER).withRequestHeaders(headerMap)
					.withRequestParameters(payload).withPassThroughOutput(true).build().getResponse();
		} catch (DBPApplicationException e) {
			diagnostic.prepareDebug("Exception in fetchUserDetails" + e.getMessage()).log();
		}

		return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}

	@Override
	public JSONObject enrollCustomerViewDetails(DataControllerRequest request, Map<String, String> inputParams) {
		JSONObject enrollCusDetails = new JSONObject();
		JSONObject parsedApprovalResponse = new JSONObject();
		try {
			String requestId = inputParams.get("requestId");

			MakerCheckerBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BackendDelegateFactory.class)
					.getBackendDelegate(MakerCheckerBackendDelegate.class);
			JSONObject approvalReqResponse = backendDelegate.getApprovalRequests(request, requestId);
			if (approvalReqResponse != null && approvalReqResponse.getJSONArray("approvalrequests").length() > 0 ) {
				parsedApprovalResponse = parseApprovalRequestDetails(approvalReqResponse, null);
				JSONObject userDetails = parsedApprovalResponse.getJSONObject("userDetails");
				JSONArray companyList = parsedApprovalResponse.getJSONArray("companyList");

				String serviceDefIds = StreamSupport.stream(companyList.spliterator(), false)
						.map(obj -> (JSONObject) obj).map(obj -> obj.getString("serviceDefinition"))
						.collect(Collectors.joining(","));
				String roleIds = StreamSupport.stream(companyList.spliterator(), false).map(obj -> (JSONObject) obj)
						.map(obj -> obj.getString("roleId")).collect(Collectors.joining(","));
				String cifIds = StreamSupport.stream(companyList.spliterator(), false).map(obj -> (JSONObject) obj)
						.map(obj -> obj.getString("cif")).collect(Collectors.joining(","));

				inputParams.put("legalEntityId", userDetails.optString("legalEntityId"));
				inputParams.put("serviceDefIds", serviceDefIds);
				inputParams.put("roleIds", roleIds);
				inputParams.put("cifIds", cifIds);

				enrollCusDetails = backendDelegate.getEnrollCustomerViewDetails(request, inputParams);

				if (enrollCusDetails != null) {
					try {
						JSONObject updatedPayload = updateEnrollCustomerPayload(enrollCusDetails,
								parsedApprovalResponse);
						updatedPayload.keySet().forEach(key -> {
							updatedPayload.put(key, updatedPayload.opt(key).toString());
						});
						return updatedPayload;
					} catch (Exception e) {
						diagnostic.prepareDebug("Exception occured while parsing enroll customer view details data"+ e.getMessage())
								.log();
						return ErrorCodesEnum.ERR_10050.setErrorCode(new JSONObject());
					}
				} else {
					diagnostic.prepareDebug("Error occurred while fetching data for enroll customer view details")
							.log();
					return ErrorCodesEnum.ERR_10049.setErrorCode(new JSONObject());
				}
			} else {
				diagnostic.prepareDebug("Exception occured while fetching approval requests data.").log();
				return ErrorCodesEnum.ERR_10009.setErrorCode(new JSONObject());
			}
		} catch (Exception e) {
			diagnostic.prepareDebug("Error occurred while fetching data for enroll customer view details " + e.getMessage())
					.log();
			return ErrorCodesEnum.ERR_10049.setErrorCode(new JSONObject());
		}
	}

	public JSONObject updateEnrollCustomerPayload(JSONObject enrollCusDetails, JSONObject parsedApprovalResponse) {
		Map<String, String> cifRoleMap = new HashMap<String, String>();

		JSONArray servicedefRecord = enrollCusDetails.optJSONArray(UtilConstants.RECORDS);
		JSONArray roleRecord = enrollCusDetails.optJSONArray(UtilConstants.RECORDS1);
		JSONArray accountsRecord = enrollCusDetails.optJSONArray("records2");
		JSONArray signatoryGrpRecord = enrollCusDetails.optJSONArray("records3");
		JSONArray featureActionsRecord = enrollCusDetails.optJSONArray("records4");
		JSONArray limitGroupRecord = enrollCusDetails.optJSONArray("records5");

		JSONArray companyList = parsedApprovalResponse.getJSONArray("companyList");
		JSONArray accountLevelPermissions = parsedApprovalResponse.getJSONArray("accountLevelPermissions");
		JSONArray globalLevelPermissions = parsedApprovalResponse.getJSONArray("globalLevelPermissions");
		JSONArray transactionLimits = parsedApprovalResponse.getJSONArray("transactionLimits");
		JSONObject userDetails = parsedApprovalResponse.getJSONObject("userDetails");

		// companyList
		Map<String, Object> companyResultMap = addCompanyData(servicedefRecord, roleRecord, accountsRecord,
				signatoryGrpRecord, companyList);
		cifRoleMap = (Map<String, String>) companyResultMap.get("cifRoleMap");
		companyList = (JSONArray) companyResultMap.get("companyList");
		parsedApprovalResponse.put("companyList", companyList);
		
		// userdetails
		Set<String> customerTypeSet = StreamSupport.stream(companyList.spliterator(), true)
				.map(item -> ((JSONObject) item).optString("serviceTypeName"))
				.collect(Collectors.toSet());
		String customerType = String.join(",", customerTypeSet);
		userDetails.put("customerType", customerType);
		parsedApprovalResponse.put("userDetails", userDetails);

		// accountLevelPermissions
		for (Object accountLevelPermission : accountLevelPermissions) {
			JSONObject accountLevelPermissionObj = (JSONObject) accountLevelPermission;
			accountLevelPermissionObj.put("roleName", cifRoleMap.get(accountLevelPermissionObj.optString("cif")));
			JSONArray accounts = accountLevelPermissionObj.getJSONArray("accounts");
			accounts.forEach(item -> {
				JSONObject accountInfo = (JSONObject) item;
				JSONArray featurePermissions = accountInfo.getJSONArray("featurePermissions");
				featurePermissions = addFeaturesData(featureActionsRecord, featurePermissions);
			});
		}
		parsedApprovalResponse.put("accountLevelPermissions", accountLevelPermissions);

		// globalLevelPermissions
		for (Object globalLevelPermission : globalLevelPermissions) {
			JSONObject globalLevelPermissionObj = (JSONObject) globalLevelPermission;
			globalLevelPermissionObj.put("roleName", cifRoleMap.get(globalLevelPermissionObj.optString("cif")));
			JSONArray features = globalLevelPermissionObj.getJSONArray("features");
			features = addFeaturesData(featureActionsRecord, features);
		}
		parsedApprovalResponse.put("globalLevelPermissions", globalLevelPermissions);

		// transactionLimits
		transactionLimits = addTransLimitsData(featureActionsRecord, limitGroupRecord, transactionLimits, cifRoleMap);
		parsedApprovalResponse.put("transactionLimits", transactionLimits);
		
		return parsedApprovalResponse;
	}

	public Map<String, Object> addCompanyData(JSONArray servicedefRecord, JSONArray roleRecord,
			JSONArray accountsRecord, JSONArray signatoryGrpRecord, JSONArray companyList) {
		Map<String, String> cifRoleMap = new HashMap<String, String>();
		Map<String, Object> map = new HashMap<String, Object>();

		for (Object company : companyList) {
			JSONObject companyObj = (JSONObject) company;
			// add servicedef related fields
			String serviceDefinitionId = companyObj.optString("serviceDefinition");
			StreamSupport.stream(servicedefRecord.spliterator(), true).map(serviceDef -> (JSONObject) serviceDef)
					.forEach(serviceDef -> {
						if (serviceDefinitionId.equalsIgnoreCase(serviceDef.optString("id"))) {
							companyObj.put("serviceDefinitionName", serviceDef.optString("serviceDefinitionName"));
							companyObj.put("serviceType", serviceDef.optString("serviceType"));
							companyObj.put("serviceTypeName", serviceDef.optString("serviceTypeName"));
						}
					});

			// add roleName related fields
			String roleId = companyObj.optString("roleId");
			StreamSupport.stream(roleRecord.spliterator(), true).map(role -> (JSONObject) role).forEach(role -> {
				if (roleId.equalsIgnoreCase(role.optString("id"))) {
					companyObj.put("roleName", role.optString("roleName"));
				}
			});
			cifRoleMap.put(companyObj.optString("cif"), companyObj.optString("roleName"));

			// add accounts related fields
			JSONArray accounts = companyObj.getJSONArray("accounts");
			IntStream.range(0, accounts.length()).mapToObj(index -> accounts.getJSONObject(index)).forEach(account -> {
				String accountId = account.optString("accountId");
				StreamSupport.stream(accountsRecord.spliterator(), true).map(accountRec -> (JSONObject) accountRec)
						.forEach(accountRec -> {
							if (accountId.equalsIgnoreCase(accountRec.optString("accountId"))) {
								account.put("accountStatus", accountRec.optString("accountStatus"));
								account.put("ownerType", accountRec.optString("ownerType"));
							}
						});
			});

			// add signatory data
			String contractId = companyObj.optString("contractId");
			JSONArray signatryGrpArray = new JSONArray();
			if (signatoryGrpRecord.length() > 0) {
				for (Object item : signatoryGrpRecord) {
					JSONObject signatryGrpObj = (JSONObject) item;
					JSONObject sigGrpJson = new JSONObject();
					if (contractId.equalsIgnoreCase(signatryGrpObj.optString("contractId")) && companyObj
							.optString("cif").equalsIgnoreCase(signatryGrpObj.optString("coreCustomerId"))) {
						sigGrpJson.put("contractId", signatryGrpObj.optString("contractId"));
						sigGrpJson.put("signatoryGroupName", signatryGrpObj.optString("signatoryGroupName"));
						sigGrpJson.put("cif", signatryGrpObj.optString("coreCustomerId"));
					}
					signatryGrpArray.put(sigGrpJson);
				}
			}
			companyObj.put("signatoryGroups", signatryGrpArray);
		}
		map.put("companyList", companyList);
		map.put("cifRoleMap", cifRoleMap);
		return map;
	}

	public JSONArray addFeaturesData(JSONArray featureActionsRecord, JSONArray featurePermissions) {
		featurePermissions.forEach(feature -> {
			JSONObject featureObj = (JSONObject) feature;
			String featureId = featureObj.optString("featureId");

			featureActionsRecord.forEach(featureAction -> {
				JSONObject featureActionRec = (JSONObject) featureAction;
				if (featureActionRec.optString("feature").equals(featureId)) {
					featureObj.put("featureStatus", featureActionRec.getString("featureStatus"));
				}
			});

			JSONArray permissions = featureObj.getJSONArray("permissions");
			IntStream.range(0, permissions.length()).mapToObj(index -> permissions.getJSONObject(index))
					.forEach(permission -> {
						String actionId = StringUtils.isNotBlank(permission.optString("id")) ? permission.optString("id") : permission.optString("actionId");
						StreamSupport.stream(featureActionsRecord.spliterator(), true)
								.map(featureActionsRec -> (JSONObject) featureActionsRec).forEach(featureActionsRec -> {
									if (actionId.equalsIgnoreCase(featureActionsRec.optString("action"))) {
										permission.put("actionStatus", featureActionsRec.optString("actionStatus"));
										permission.put("actionType", featureActionsRec.optString("actionType"));
										if(StringUtils.isNotBlank(permission.optString("id"))) {
											permission.put("actionDesc", featureActionsRec.optString("actionDesc"));
											permission.put("actionName", featureActionsRec.optString("actionName"));
										}
									}
								});
					});
		});
		return featurePermissions;
	}

	public JSONArray addTransLimitsData(JSONArray featureActionsRecord, JSONArray limitGroupRecord,
			JSONArray transactionLimits, Map<String, String> cifRoleMap) {
		for (Object transactionLimit : transactionLimits) {
			JSONObject transactionLimitObj = (JSONObject) transactionLimit;
			transactionLimitObj.put("roleName", cifRoleMap.get(transactionLimitObj.optString("cif")));
			JSONArray accounts = transactionLimitObj.getJSONArray("accounts");
			accounts.forEach(item -> {
				JSONObject accountInfo = (JSONObject) item;
				JSONArray featurePermissions = accountInfo.getJSONArray("featurePermissions");

				IntStream.range(0, featurePermissions.length())
						.mapToObj(index -> featurePermissions.getJSONObject(index)).forEach(featurePermission -> {
							String featureId = featurePermission.optString("featureId");
							String actionId = featurePermission.optString("actionId");
							StreamSupport.stream(featureActionsRecord.spliterator(), true)
									.map(featureActionsRec -> (JSONObject) featureActionsRec)
									.forEach(featureActionsRec -> {
										if (featureId.equalsIgnoreCase(featureActionsRec.optString("feature"))) {
											featurePermission.put("featureName",
													featureActionsRec.optString("featureName"));
											featurePermission.put("featureStatus",
													featureActionsRec.optString("featureStatus"));
										}
										if (actionId.equalsIgnoreCase(featureActionsRec.optString("action"))) {
											featurePermission.put("actionType",
													featureActionsRec.optString("actionType"));
										}
									});
						});

			});
			JSONArray limitGroups = transactionLimitObj.getJSONArray("limitGroups");
			limitGroups.forEach(item -> {
				JSONObject limitGroup = (JSONObject) item;
				String limitGroupId = limitGroup.optString("limitGroupId");
				StreamSupport.stream(limitGroupRecord.spliterator(), true)
						.map(limitGroupRec -> (JSONObject) limitGroupRec).forEach(limitGroupRec -> {
							if (limitGroupId.equalsIgnoreCase(limitGroupRec.optString("limitGroupId"))) {
								limitGroup.put("limitGroupName", limitGroupRec.optString("limitGroupName"));
							}
						});
			});
		}
		return transactionLimits;
	}

	@Override
	public JSONObject createContractViewDetails(DataControllerRequest request, Map<String, String> inputParams) {
		JSONObject viewDetails = new JSONObject();
		JSONObject parsedApprovalResponse = new JSONObject();
		try {
			String requestId = inputParams.get("requestId");

			MakerCheckerBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BackendDelegateFactory.class)
					.getBackendDelegate(MakerCheckerBackendDelegate.class);
			JSONObject approvalReqResponse = backendDelegate.getApprovalRequests(request, requestId);
			if (approvalReqResponse != null && approvalReqResponse.getJSONArray("approvalrequests").length() > 0) {
				parsedApprovalResponse = parseApprovalRequestDetails(approvalReqResponse, null);

				inputParams.put("legalEntityId", parsedApprovalResponse.optString("legalEntityId"));
				inputParams.put("serviceDefIds", parsedApprovalResponse.optString("serviceDefinitionId"));

				viewDetails = backendDelegate.getEnrollCustomerViewDetails(request, inputParams);

				if (viewDetails != null) {
					JSONObject updatedPayload = updateCreateContractPayload(viewDetails, parsedApprovalResponse);
					updatedPayload.keySet().forEach(key -> {
						updatedPayload.put(key, updatedPayload.opt(key).toString());
					});
					return updatedPayload;
				} else {
					diagnostic.prepareDebug("Error occurred while fetching data for dbxdb_GetEnrollCustomerViewDetailsInfo")
							.log();
					return ErrorCodesEnum.ERR_10049.setErrorCode(new JSONObject());
				}
			} else {
				diagnostic.prepareDebug("Exception occured while fetching approval requests data.").log();
				return ErrorCodesEnum.ERR_10009.setErrorCode(new JSONObject());
			}
		} catch (Exception e) {
			diagnostic.prepareDebug("Error occurred while fetching data for create contract view details " + e.getMessage())
					.log();
			return ErrorCodesEnum.ERR_10056.setErrorCode(new JSONObject());
		}
	}

	public JSONObject updateCreateContractPayload(JSONObject viewDetails, JSONObject parsedApprovalResponse) {
		JSONArray globalLevelPermissions1 = parsedApprovalResponse.getJSONArray("globalLevelPermissions1");
		JSONArray transactionLimits1 = parsedApprovalResponse.getJSONArray("transactionLimits1");

		JSONArray servicedefRecord = viewDetails.optJSONArray(UtilConstants.RECORDS);
		JSONArray featureActionsRecord = viewDetails.optJSONArray("records4");

		// Add serviceType & serviceType name
		if (servicedefRecord.optJSONObject(0).keySet().size() > 0) {
			parsedApprovalResponse.put("serviceType", servicedefRecord.optJSONObject(0).optString("serviceType"));
			parsedApprovalResponse.put("serviceTypeName",
					servicedefRecord.optJSONObject(0).optString("serviceTypeName"));
		}
		parsedApprovalResponse.put("contractCustomers", new JSONArray(parsedApprovalResponse
				.optJSONArray("contractCustomers").toString().replaceAll("coreCustomerId", "cif")));
		parsedApprovalResponse.put("accountLevelPermissions",
				new JSONArray(parsedApprovalResponse.optJSONArray("accountLevelPermissions1").toString()
						.replaceAll("actionId", "id").replaceAll("coreCustomerId", "cif")));
		parsedApprovalResponse.remove("accountLevelPermissions1");

		// globalLevelPermissions1
		for (Object globalLevelPermission : globalLevelPermissions1) {
			JSONObject globalLevelPermissionObj = (JSONObject) globalLevelPermission;
			JSONArray features = globalLevelPermissionObj.getJSONArray("features");
			features = addFeaturesData(featureActionsRecord, features);
		}
		parsedApprovalResponse.put("globalLevelPermissions", new JSONArray(
				globalLevelPermissions1.toString().replaceAll("actionId", "id").replaceAll("coreCustomerId", "cif")));
		parsedApprovalResponse.remove("globalLevelPermissions1");

		// transactionLimits1
		for (Object transactionLimits : transactionLimits1) {
			JSONObject transactionLimitsObj = (JSONObject) transactionLimits;
			JSONArray featurePermissions = transactionLimitsObj.getJSONArray("featurePermissions");
			IntStream.range(0, featurePermissions.length()).mapToObj(index -> featurePermissions.getJSONObject(index))
					.forEach(featurePermission -> {
						String featureId = featurePermission.optString("featureId");
						String actionId = featurePermission.optString("actionId");
						StreamSupport.stream(featureActionsRecord.spliterator(), true)
								.map(featureActionsRec -> (JSONObject) featureActionsRec).forEach(featureActionsRec -> {
									if (featureId.equalsIgnoreCase(featureActionsRec.optString("feature"))) {
										featurePermission.put("featureName", featureActionsRec.optString("featureName"));
										featurePermission.put("featureStatus", featureActionsRec.optString("featureStatus"));
									}
									if (actionId.equalsIgnoreCase(featureActionsRec.optString("action"))) {
										featurePermission.put("actionType", featureActionsRec.optString("actionType"));
									}
								});
					});
		}
		parsedApprovalResponse.put("transactionLimits",
				new JSONArray(transactionLimits1.toString().replaceAll("coreCustomerId", "cif")));
		parsedApprovalResponse.remove("transactionLimits1");
		return parsedApprovalResponse;
	}

	@Override
	public JSONObject viewEditContractDetails(DataControllerRequest request, DataControllerResponse response,
			String requestId) {
		MakerCheckerBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class).getBackendDelegate(MakerCheckerBackendDelegate.class);
		JSONObject finalResult = new JSONObject();
		editedList.clear();
		try {
			
			JSONObject approvalRequestPayload = backendDelegate.getApprovalRequests(request, requestId);
			JSONObject parsedApprovalRequestPayload = parseApprovalRequestDetails(approvalRequestPayload, null);

			String contractId = parsedApprovalRequestPayload.optString("contractId");
			String legalEntityId = parsedApprovalRequestPayload.optString("legalEntityId");

			Map<String, Object> reqPayload = new HashMap<>();
			reqPayload.put("contractId", contractId);
			reqPayload.put("legalEntityId", legalEntityId);
			diagnostic.prepareDebug("reqPayload : " + reqPayload.toString()).log();

			JSONObject contractDetails = backendDelegate.getContractDetails(request, reqPayload);
			JSONObject contractFeaturesActionsLimits = backendDelegate.getContractFeatureActionLimits(request,
					reqPayload);
			diagnostic.prepareDebug("contractDetails : " + contractDetails).log();
			diagnostic.prepareDebug("contractFeaturesActionsLimits : " + contractFeaturesActionsLimits).log();
			
			contractDetails.put("contractId", contractDetails.optString("id"));
			contractDetails.put("contractName", contractDetails.optString("name"));
			contractDetails.put("faxId", contractDetails.optString("faxId"));

			contractDetails.put("globalLevelPermissions1", new JSONArray(contractFeaturesActionsLimits
					.opt("globalLevelPermissions").toString().replaceAll("actionId", "id")));

			contractDetails.put("accountLevelPermissions1", new JSONArray(contractFeaturesActionsLimits
					.opt("accountLevelPermissions").toString().replaceAll("actionId", "id")));
			
			contractDetails.put("transactionLimits1", contractFeaturesActionsLimits.opt("transactionLimits"));
			String contractDetailsStr = contractDetails.toString();

			String tempParsedApprovalRequestPayload = parsedApprovalRequestPayload.toString();
			parsedApprovalRequestPayload = new JSONObject(tempParsedApprovalRequestPayload);

			diagnostic.prepareDebug("contractDetails : " + contractDetails).log();

			compareData(contractDetails, parsedApprovalRequestPayload, finalResult);
			diagnostic.prepareDebug("finalResult1 : " + finalResult).log();
			compareAddressOrCommunicationData(contractDetails.optJSONArray("communication"),
					parsedApprovalRequestPayload.optJSONArray("communication"), finalResult, "communication");
			diagnostic.prepareDebug("finalResult2 : " + finalResult).log();
			compareAddressOrCommunicationData(contractDetails.optJSONArray("address"), parsedApprovalRequestPayload.optJSONArray("address"),
					finalResult, "address");
			diagnostic.prepareDebug("finalResult3 : " + finalResult).log();
			
			if (parsedApprovalRequestPayload.optJSONArray("contractCustomers").length() > 0) {
			compareContractCustomersData(contractDetails.optJSONArray("contractCustomers"),
						parsedApprovalRequestPayload.optJSONArray("contractCustomers"), finalResult,
						parsedApprovalRequestPayload.optJSONArray("deletedCustomers"));
			} else {
				finalResult.put("contractCustomers", contractDetails.optJSONArray("contractCustomers"));
			}
				
			diagnostic.prepareDebug("finalResult4 : " + finalResult).log();
			compareAccountLevelPermissionsForContract(contractDetails, parsedApprovalRequestPayload, finalResult);
			diagnostic.prepareDebug("finalResult5 : " + finalResult).log();
			
			JSONArray globalLevelPermsArray = parsedApprovalRequestPayload.optJSONArray("globalLevelPermissions1");
			if (globalLevelPermsArray.length() > 0) {
				compareGlobalLevelPermissionsForContract(contractDetails, parsedApprovalRequestPayload, finalResult);
			} else {
				finalResult.put("globalLevelPermissions", new JSONArray(contractFeaturesActionsLimits
						.opt("globalLevelPermissions").toString().replaceAll("actionId", "id")));
			}
			diagnostic.prepareDebug("finalResult6 : " + finalResult).log();
			compareTransactionsLimitsForContract(contractDetails, parsedApprovalRequestPayload, finalResult);
			finalResult.put("deletedCustomers", parsedApprovalRequestPayload.optJSONArray("deletedCustomers"));
			finalResult.put("editedList",
					editedList.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", ""));
			diagnostic.prepareDebug("finalResult7 : " + finalResult).log();
		} catch (Exception e) {
			diagnostic.prepareDebug("Exception while viewing the customer details. " + e.getMessage()).log();
		}
		return finalResult;
	}
	
	@Override
	public JSONObject createSignatoryGroupViewDetails(DataControllerRequest request, DataControllerResponse response,
			String requestId) {
		MakerCheckerBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class).getBackendDelegate(MakerCheckerBackendDelegate.class);
		JSONObject finalResult = new JSONObject();
		try {
			JSONObject approvalRequestPayload = backendDelegate.getApprovalRequests(request, requestId);
			String payload = approvalRequestPayload.optJSONArray(UtilConstants.APPROVAL_REQUESTS_GET_RECORD).optJSONObject(0)
					.optString("reqPayload");
			JSONObject payloadObj = CommonUtilities.getStringAsJSONObject(payload);

			String contractId = payloadObj.optString("contractId");
			//String legalEntityId = payloadObj.optString("legalEntityId");
			String coreCustomerId = payloadObj.optString("coreCustomerId");

			Map<String, Object> reqPayload = new HashMap<>();
			reqPayload.put("contractId", contractId);
			//reqPayload.put("legalEntityId", legalEntityId);
			reqPayload.put("coreCustomerId", coreCustomerId);
			reqPayload.put("signatoryGroupId", null);
			reqPayload.put("action", request.getParameter("action"));
			
			List<String> list = new ArrayList<String>();
			JSONArray signatories = new JSONArray(payloadObj.get("signatories").toString());
			
			for(int i=0; i<signatories.length(); i++) {
				list.add(signatories.getJSONObject(i).get("customerId").toString());
			}
			
			String signList = StringUtils.join(list.toArray(), ",");
			
			reqPayload.put("customerIdList", signList);
			
			diagnostic.prepareDebug("reqPayload : " + reqPayload.toString()).log();
			alert.prepareError("reqPayload : " + reqPayload.toString()).log();
			
			JSONObject vieweDetails = new JSONObject();
			vieweDetails = backendDelegate.getSignatoryViewDetails(request, reqPayload);
			
			if(null != vieweDetails) {
				JSONObject signDetails = vieweDetails.getJSONObject("signDetails");
				alert.prepareError("signDetails : " + signDetails.toString()).log();
				
				if(null != signDetails) {
					finalResult.put("contractName", signDetails.optString("contractName").toString());
					finalResult.put("serviceTypeName", signDetails.optString("serviceTypeName").toString());
					finalResult.put("serviceType", signDetails.optString("serviceType").toString());
					finalResult.put("serviceName", signDetails.optString("serviceName").toString());
					finalResult.put("customerName", signDetails.optString("coreCustomerName").toString());
				}
							
				finalResult.put("signatories", vieweDetails.optJSONArray("signatories"));
				
				finalResult.put("coreCustomerId", payloadObj.optString("coreCustomerId").toString());
				finalResult.put("contractId", payloadObj.optString("contractId").toString());
				finalResult.put("legalEntityId", payloadObj.optString("legalEntityId").toString());
				finalResult.put("signatoryGroupName", payloadObj.optString("signatoryGroupName").toString());
				finalResult.put("signatoryGroupDescription", payloadObj.optString("signatoryGroupDescription").toString());
				
				
				finalResult.put("opstatus", 0);
				finalResult.put("httpStatusCode", 0);
				
			}			
			
		} catch (Exception e) {
			diagnostic.prepareDebug("Exception while viewing the customer details. " + e.getMessage()).log();
		}
		return finalResult;
	}
	
	@Override
	public JSONObject deleteSignatoryGroupViewDetails(DataControllerRequest request, DataControllerResponse response,
			String requestId) {
		MakerCheckerBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class).getBackendDelegate(MakerCheckerBackendDelegate.class);
		JSONObject finalResult = new JSONObject();
		try {
			JSONObject approvalRequestPayload = backendDelegate.getApprovalRequests(request, requestId);
			String payload = approvalRequestPayload.optJSONArray(UtilConstants.APPROVAL_REQUESTS_GET_RECORD).optJSONObject(0)
					.optString("reqPayload");
			JSONObject payloadObj = CommonUtilities.getStringAsJSONObject(payload);

			//String contractId = payloadObj.optString("contractId");
			//String coreCustomerId = payloadObj.optString("coreCustomerId");
			String signatoryGroupId = payloadObj.optString("signatoryGroupId");
			
			Map<String, Object> reqPayload = new HashMap<>();
			reqPayload.put("contractId", null);
			reqPayload.put("coreCustomerId", null);
			reqPayload.put("customerIdList", null);
			reqPayload.put("signatoryGroupId", signatoryGroupId);
			reqPayload.put("action", request.getParameter("action"));
			
						
			diagnostic.prepareDebug("reqPayload : " + reqPayload.toString()).log();
			alert.prepareError("reqPayload : " + reqPayload.toString()).log();
			
			JSONObject vieweDetails = new JSONObject();
			vieweDetails = backendDelegate.getSignatoryViewDetails(request, reqPayload);
			
			if(null != vieweDetails) {
				JSONObject signDetails = vieweDetails.getJSONObject("signDetails");
				alert.prepareError("signDetails : " + signDetails.toString()).log();
				
				if(null != signDetails) {
					finalResult.put("contractName", signDetails.optString("contractName").toString());
					finalResult.put("serviceTypeName", signDetails.optString("serviceTypeName").toString());
					finalResult.put("serviceType", signDetails.optString("serviceType").toString());
					finalResult.put("serviceName", signDetails.optString("serviceName").toString());
					finalResult.put("customerName", signDetails.optString("coreCustomerName").toString());
				}
							
				finalResult.put("signatories", vieweDetails.optJSONArray("signatories"));
				
				finalResult.put("coreCustomerId", signDetails.optString("coreCustomerId").toString());
				finalResult.put("contractId", signDetails.optString("contractId").toString());
				finalResult.put("legalEntityId", payloadObj.optString("legalEntityId").toString());
				finalResult.put("signatoryGroupName", signDetails.optString("signatoryGroupName").toString());
				finalResult.put("signatoryGroupDescription", signDetails.optString("signatoryGroupDescription").toString());
				
				
				finalResult.put("opstatus", 0);
				finalResult.put("httpStatusCode", 0);
				
			}			
			
		} catch (Exception e) {
			diagnostic.prepareDebug("Exception while viewing the customer details. " + e.getMessage()).log();
		}
		return finalResult;
	}
	
	@Override
	public JSONObject editSignatoryGroupViewDetails(DataControllerRequest request, DataControllerResponse response,
			String requestId) {
		MakerCheckerBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class).getBackendDelegate(MakerCheckerBackendDelegate.class);
		JSONObject finalResult = new JSONObject();
		try {
			JSONObject approvalRequestPayload = backendDelegate.getApprovalRequests(request, requestId);
			String payload = approvalRequestPayload.optJSONArray(UtilConstants.APPROVAL_REQUESTS_GET_RECORD).optJSONObject(0)
					.optString("reqPayload");
			JSONObject payloadObj = CommonUtilities.getStringAsJSONObject(payload);

			String contractId = payloadObj.optString("contractId");
			String signatoryGroupId = payloadObj.optString("signatoryGroupId");
			String coreCustomerId = payloadObj.optString("coreCustomerId");
			
			String action = request.getParameter("action");
			diagnostic.prepareDebug("action : " + action).log();
			alert.prepareError("action : " + action).log();
			if(StringUtils.isEmpty(action)) {
				action = "EDIT_SIGNATORY_GROUP";
			}

			Map<String, Object> reqPayload = new HashMap<>();
			reqPayload.put("contractId", contractId);
			reqPayload.put("coreCustomerId", coreCustomerId);
			reqPayload.put("signatoryGroupId", signatoryGroupId);
			reqPayload.put("action", action);
			
			List<String> list = new ArrayList<String>();
			JSONArray signatories = new JSONArray(payloadObj.get("signatories").toString());
			
			for(int i=0; i<signatories.length(); i++) {
				list.add(signatories.getJSONObject(i).get("customerId").toString());
			}
			
			String signList = StringUtils.join(list.toArray(), ",");
			
			reqPayload.put("customerIdList", signList);
			
			diagnostic.prepareDebug("reqPayload : " + reqPayload.toString()).log();
			alert.prepareError("reqPayload : " + reqPayload.toString()).log();
			
			JSONObject vieweDetails = new JSONObject();
			vieweDetails = backendDelegate.getSignatoryViewDetails(request, reqPayload);
			
			diagnostic.prepareDebug("vieweDetails : " + vieweDetails.toString()).log();
			alert.prepareError("vieweDetails : " + vieweDetails.toString()).log();
			
			if(null != vieweDetails) {
				JSONObject approvedSignDetails = vieweDetails.getJSONObject("signDetails");
				alert.prepareError("signDetails : " + approvedSignDetails.toString()).log();
				//editable fields, signatoryGroupName,signatoryGroupDescription, users
				
				if(null != approvedSignDetails) {
					finalResult.put("contractName", approvedSignDetails.optString("contractName"));
					finalResult.put("serviceTypeName", approvedSignDetails.optString("serviceTypeName"));
					finalResult.put("serviceType", approvedSignDetails.optString("serviceType"));
					finalResult.put("serviceName", approvedSignDetails.optString("serviceName"));
					finalResult.put("customerName", approvedSignDetails.optString("coreCustomerName"));
				}				
				
				finalResult.put("coreCustomerId", payloadObj.optString("coreCustomerId"));
				finalResult.put("contractId", payloadObj.optString("contractId"));
				finalResult.put("legalEntityId", payloadObj.optString("legalEntityId"));
				finalResult.put("signatoryGroupName", payloadObj.optString("signatoryGroupName"));
				finalResult.put("signatoryGroupDescription", payloadObj.optString("signatoryGroupDescription"));				
				
				if(!approvedSignDetails.optString("signatoryGroupName").equals(payloadObj.optString("signatoryGroupName"))) {
					finalResult.put("signatoryGroupNameOld", approvedSignDetails.optString("signatoryGroupName"));
				}
				
				if(!approvedSignDetails.optString("signatoryGroupDescription").equals(payloadObj.optString("signatoryGroupDescription"))) {
					finalResult.put("signatoryGroupDescriptionOld", approvedSignDetails.optString("signatoryGroupDescription"));
				}
				
				JSONArray approvedUsers = vieweDetails.optJSONArray("signatories");
				JSONArray editUsers = vieweDetails.optJSONArray("editSignatories");
				
				JSONArray editUsersFinal = new JSONArray();
				JSONArray signatoriesOld = new JSONArray();
				JSONArray deleteUsers = signatories;
				
				List<String> deleteCustIds = new ArrayList<String>();
				List<String> addedCustIds = new ArrayList<String>();
				
				if(null != deleteUsers && deleteUsers.length() > 0) {
					for(int i=0;i<deleteUsers.length();i++) {
						JSONObject obj = deleteUsers.getJSONObject(i);
						boolean isUserRemoved = obj.optBoolean("isUserRemoved");
						if(isUserRemoved) {
							deleteCustIds.add(obj.optString("customerId"));
						}else {
							addedCustIds.add(obj.optString("customerId"));
						}
						
					}
					//to add deleted users to signatoriesOld
					if(!deleteCustIds.isEmpty()) {
						if(null != approvedUsers && approvedUsers.length() > 0) {
							for(int i=0;i<approvedUsers.length();i++) {
								JSONObject obj = approvedUsers.getJSONObject(i);
								String custId = obj.optString("Customer_id");
								if(deleteCustIds.contains(custId)) {
									signatoriesOld.put(obj);
								}
							}
						}
					}
				}
				
				List<String> approvedCustIds = new ArrayList<String>();
				
				if(approvedUsers.length() > 0) {
					for(int i=0;i<approvedUsers.length();i++) {
						JSONObject obj = approvedUsers.getJSONObject(i);
						approvedCustIds.add(obj.optString("Customer_id"));
					}
				}
				
				//to add new users
				if(editUsers.length() > 0) {
					for(int i=0;i<editUsers.length();i++) {
						JSONObject obj1 = editUsers.getJSONObject(i);				
						String custId = obj1.optString("Customer_id");
						if(addedCustIds.size() > 0) {
							if(addedCustIds.contains(custId)) {
								obj1.put("operationType", "ADD");
								editUsersFinal.put(obj1);
							}
						}
					}
				}
				
				//to fetch unchanges users
				if(approvedUsers.length() > 0) {
					for(int i=0;i<approvedUsers.length();i++) {
						JSONObject obj1 = approvedUsers.getJSONObject(i);				
						String custId = obj1.optString("Customer_id");
						if(approvedCustIds.size() > 0) {
							if(!deleteCustIds.contains(custId)) {
								editUsersFinal.put(obj1);
							}
						}
					}
				}				
				
				finalResult.put("signatories", editUsersFinal);
				finalResult.put("signatoriesOld", signatoriesOld);
				
				
				finalResult.put("opstatus", 0);
				finalResult.put("httpStatusCode", 0);
				
			}			
			
		} catch (Exception e) {
			diagnostic.prepareDebug("Exception while viewing the customer details. " + e.getMessage()).log();
		}
		return finalResult;
	}
		
		
	@Override
	public JSONObject createApprovalRuleBySignatoryGroupViewDetails(DataControllerRequest request, DataControllerResponse response,
			String requestId) {
		MakerCheckerBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class).getBackendDelegate(MakerCheckerBackendDelegate.class);
		JSONObject finalResult = new JSONObject();
		JSONObject parsedApprovalReqPayload = new JSONObject();
		
		JSONArray contractInfo = new JSONArray();
		JSONArray featureActionInfo = new JSONArray();
		JSONArray signatoryGroupsInfo = new JSONArray();
		JSONArray accountInfo = new JSONArray();
		try {
			JSONObject approvalRequestPayload = backendDelegate.getApprovalRequests(request, requestId);
			HashMap<String, String> inputParams = new HashMap<>();
			
			if (approvalRequestPayload != null && approvalRequestPayload.getJSONArray("approvalrequests").length() > 0 ) {
				parsedApprovalReqPayload = parseApprovalRequestDetails(approvalRequestPayload, null);
			}
			
			String contractId = parsedApprovalReqPayload.optString("contractId");
			String coreCustomerId = parsedApprovalReqPayload.optString("cif");
			String actionId = parsedApprovalReqPayload.optString("actionId");
			JSONArray limitsArray = parsedApprovalReqPayload.optJSONArray("limits");
			String groupIds = limitsArray.getJSONObject(0).optString("groupList");
			String legalEntityId = parsedApprovalReqPayload.optString("legalEntityId");
	
			groupIds = groupIds.substring(1, groupIds.length()-1);

			inputParams.put("contractId", contractId);
			inputParams.put("coreCustomerId", coreCustomerId);
			inputParams.put("actionId", actionId);
			inputParams.put("groupIds", groupIds);
			inputParams.put("legalEntityId", legalEntityId);
			if(parsedApprovalReqPayload.has("accountId")) {
				inputParams.put("accountId", parsedApprovalReqPayload.optString("accountId"));
			}
			

			diagnostic.prepareDebug("inputParams : " + inputParams.toString());
			JSONObject approvalReqDetails = backendDelegate.getCreateApprovalRuleBySGViewDetails(request, inputParams);

			if(approvalReqDetails!=null) {
				contractInfo = (JSONArray) approvalReqDetails.opt("records");
				featureActionInfo = (JSONArray) approvalReqDetails.opt("records1");
				signatoryGroupsInfo = (JSONArray) approvalReqDetails.opt("records2");
				accountInfo = (JSONArray) approvalReqDetails.opt("records3");
			}
			HashMap<String,String> signatoryGroupsMap =  new HashMap<>();
	
			if (contractInfo.length() > 0) {
				parsedApprovalReqPayload.put("contractName", contractInfo.optJSONObject(0).optString("contractName"));
				parsedApprovalReqPayload.put("serviceType", contractInfo.optJSONObject(0).optString("serviceType"));
				parsedApprovalReqPayload.put("servicedefinitionName",
						contractInfo.optJSONObject(0).optString("servicedefinitionName"));
				parsedApprovalReqPayload.put("isPrimary", contractInfo.optJSONObject(0).optString("isPrimary"));
				parsedApprovalReqPayload.put("coreCustomerName",
						contractInfo.optJSONObject(0).optString("coreCustomerName"));
			}

			if (featureActionInfo.length() > 0) {
				parsedApprovalReqPayload.put("Feature_id",
						featureActionInfo.optJSONObject(0).optString("Feature_id"));
				parsedApprovalReqPayload.put("feature_name",
						featureActionInfo.optJSONObject(0).optString("feature_name"));
				parsedApprovalReqPayload.put("feature_status_id",
						featureActionInfo.optJSONObject(0).optString("feature_status_id"));
				parsedApprovalReqPayload.put("feature_Type_id",
						featureActionInfo.optJSONObject(0).optString("feature_Type_id"));
				parsedApprovalReqPayload.put("action_name",
						featureActionInfo.optJSONObject(0).optString("action_name"));
				parsedApprovalReqPayload.put("action_Type_id",
						featureActionInfo.optJSONObject(0).optString("action_Type_id"));
			}
			
			if (accountInfo.length() > 0) {
				parsedApprovalReqPayload.put("ownerType",
						accountInfo.optJSONObject(0).optString("ownerType"));
				parsedApprovalReqPayload.put("accountName",
						accountInfo.optJSONObject(0).optString("accountName"));
				parsedApprovalReqPayload.put("accountType",
						accountInfo.optJSONObject(0).optString("displayName"));
			}
			String legalEntityCurrency = LegalEntityUtil.getCurrencyForLegalEntity(legalEntityId);
			parsedApprovalReqPayload.put("currency", legalEntityCurrency);
			parsedApprovalReqPayload.put("groupsInfo", signatoryGroupsInfo);
			
			diagnostic.prepareDebug("parsedApprovalReqPayload : " + parsedApprovalReqPayload.toString());

			return parsedApprovalReqPayload;
			
		} catch (Exception e) {
			diagnostic.prepareDebug("Exception while viewing the customer details. " + e.getMessage()).log();
		}
		return parsedApprovalReqPayload;
	}


	private void compareGlobalLevelPermissionsForContract(JSONObject contractDetails, JSONObject payloadObj,
			JSONObject finalResult) {

		JSONArray newPermissionsArray = new JSONArray(
				payloadObj.opt("globalLevelPermissions1").toString().replaceAll("actionId", "id"));
		JSONObject newPermissionsObj = new JSONObject();
		for (int i = 0; i < newPermissionsArray.length(); i++) {
			JSONObject currPermissionObj = newPermissionsArray.optJSONObject(i);
			newPermissionsObj.put(currPermissionObj.optString("coreCustomerId"), currPermissionObj);
		}
		JSONArray globalLevelPermissions = contractDetails.optJSONArray("globalLevelPermissions1");
		JSONArray remainingGlobalLevelPermissions = new JSONArray();
		JSONArray changedGlobalLevelPermissions = null;

		for (int i = 0; i < globalLevelPermissions.length(); i++) {
			JSONObject globalLevelPermissionsObj = globalLevelPermissions.optJSONObject(i);
			if (newPermissionsObj.has(globalLevelPermissionsObj.optString("coreCustomerId"))) {
				changedGlobalLevelPermissions = new JSONArray();
				JSONArray existingFeatures = globalLevelPermissionsObj.getJSONArray("features");
				compareGlobalPermissions(remainingGlobalLevelPermissions, changedGlobalLevelPermissions,
						existingFeatures,
						newPermissionsObj.optJSONObject(globalLevelPermissionsObj.optString("coreCustomerId")), globalLevelPermissionsObj.optString("coreCustomerId"));
			}else {
				remainingGlobalLevelPermissions.put(globalLevelPermissionsObj);
			}
		}

		if (changedGlobalLevelPermissions.length() > 0) {
			finalResult.put("globalLevelPermissionsOld", changedGlobalLevelPermissions);
		}
		finalResult.put("globalLevelPermissions", remainingGlobalLevelPermissions);

	}
	
	private void compareTransactionsLimitsForContract(JSONObject contractDetails, JSONObject payloadObj,
			JSONObject finalResult) {
		try {
			
			JSONArray newTransactionLimitsArray = new JSONArray(payloadObj.opt("transactionLimits1").toString());
			JSONObject newTransactionLimitsObj = new JSONObject();
			for (int i = 0; i < newTransactionLimitsArray.length(); i++) {
				JSONObject currTransactionLimitObj = newTransactionLimitsArray.optJSONObject(i);
				newTransactionLimitsObj.put(currTransactionLimitObj.optString("coreCustomerId"), currTransactionLimitObj);
			}
			
			JSONArray oldTransactionLimitsArr = contractDetails.optJSONArray("transactionLimits1");
			JSONArray remainingTransactionLimits = new JSONArray();
			JSONArray changedTransactionLimits = new JSONArray();
			
			for (int i = 0; i < oldTransactionLimitsArr.length(); i++) {
				JSONObject currOldTransactionLimitsObj = oldTransactionLimitsArr.optJSONObject(i);
				if (newTransactionLimitsObj.has(currOldTransactionLimitsObj.optString("coreCustomerId"))) {
					JSONArray existingFeaturePermissions = currOldTransactionLimitsObj.getJSONArray("featurePermissions");
					compareContractTransactionLimitsValues(remainingTransactionLimits, changedTransactionLimits,
							existingFeaturePermissions,
							newTransactionLimitsObj.optJSONObject(currOldTransactionLimitsObj.optString("coreCustomerId")), currOldTransactionLimitsObj.optString("coreCustomerId"));
				}else {
					remainingTransactionLimits.put(currOldTransactionLimitsObj);
				}
			}

			if (changedTransactionLimits.length() > 0) {
				finalResult.put("transactionLimitsOld", changedTransactionLimits);
			}
			finalResult.put("transactionLimits", remainingTransactionLimits);
			

		} catch (Exception e) {
			diagnostic.prepareDebug("Exception in MakerCheckerBusinessDelegateImpl:compareTransactionsLimitsForContract " + e.getMessage()).log();
		}

	}
	
	private void compareContractTransactionLimitsValues(JSONArray remainingExistingGlobalLevelPermissions,
			JSONArray changedTransactionLimits, JSONArray existingGlobalLevelFeatures,
			JSONObject newObjectGlobalLevelPermission, String coreCustomerId) throws JSONException {
		
		JSONArray newFeaturesPermissions = newObjectGlobalLevelPermission.getJSONArray("featurePermissions");
		Iterator<Object> existingGlobalLevelFeaturesIterator = existingGlobalLevelFeatures.iterator();

		try {
			while (existingGlobalLevelFeaturesIterator.hasNext()) {

				JSONObject eachExistingFeature = (JSONObject) existingGlobalLevelFeaturesIterator.next();

				Optional<Object> isFeaturePresent = StreamSupport.stream(newFeaturesPermissions.spliterator(), true)
						.filter(item -> StringUtils.equalsIgnoreCase(((JSONObject) item).optString("featureId"),
								eachExistingFeature.optString("featureId")))
						.findAny();

				JSONObject eachNewFeature = new JSONObject();

				if (isFeaturePresent.isPresent()) {
					eachNewFeature = (JSONObject) isFeaturePresent.get();
					String[] changedKeys = compareObject(eachExistingFeature, eachNewFeature);
					JSONObject changedObject = new JSONObject();
					if (null != changedKeys) {
						changedObject.put("featureId", eachExistingFeature.optString("featureId"));
						changedObject.put("featureName", eachExistingFeature.optString("featureName"));
						changedObject.put("featureDescription", eachExistingFeature.optString("featureDescription"));
						for (String key : changedKeys) {
							changedObject.put(key, eachExistingFeature.get(key));
						}
						changedObject.put("coreCustomerId", coreCustomerId);
						changedTransactionLimits.put(changedObject);
						editedList.add("transactionLimits");
					}

					JSONArray existingLimits = eachExistingFeature.optJSONArray("limits");
					JSONArray newLimits = eachNewFeature.optJSONArray("limits");
					Iterator<Object> existingLimitsIterator = existingLimits.iterator();

					while (existingLimitsIterator.hasNext()) {

						JSONObject eachExistingLimit = (JSONObject) existingLimitsIterator.next();

						Optional<Object> isLimitPresent = StreamSupport.stream(newLimits.spliterator(), true)
								.filter(item -> StringUtils.equalsIgnoreCase(((JSONObject) item).optString("id"),
										eachExistingLimit.optString("id")))
								.findAny();

						JSONObject eachNewLimit = new JSONObject();

						if (isLimitPresent.isPresent()) {
							eachNewLimit = (JSONObject) isLimitPresent.get();
							String[] changedPermissionKeys = compareObject(eachExistingLimit, eachNewLimit);
							if (null != changedPermissionKeys) {
								JSONObject changedLimitObject = new JSONObject();
								changedLimitObject.put("id", eachExistingLimit.get("id"));
								changedLimitObject.put("value", eachExistingLimit.optString("value"));
								changedLimitObject.put("featureId", eachExistingFeature.optString("featureId"));
								changedLimitObject.put("featureName", eachExistingFeature.optString("featureName"));
								changedLimitObject.put("actionId", eachExistingFeature.optString("actionId"));
								changedLimitObject.put("actionStatus", eachExistingFeature.optString("actionStatus"));
								changedLimitObject.put("actionDescription", eachExistingFeature.optString("actionDescription"));
								for (String key : changedPermissionKeys) {
									changedLimitObject.put(key, eachExistingLimit.get(key));
								}
								changedLimitObject.put("coreCustomerId", coreCustomerId);
								changedTransactionLimits.put(changedLimitObject);
								editedList.add("transactionLimits");
							}
						}
					}
				}
			}
			remainingExistingGlobalLevelPermissions.put(newObjectGlobalLevelPermission);
		} catch (Exception e) {
			diagnostic.prepareDebug(
					"Exception in MakerCheckerBusinessDelegateImpl:compareTransactionLimitsValues " + e.getMessage())
					.log();
		}
	}

	private void compareAccountLevelPermissionsForContract(JSONObject contractDetails, JSONObject payloadObj,
			JSONObject finalResult) {

		JSONArray newPermissionsArray = new JSONArray(
				payloadObj.opt("accountLevelPermissions1").toString().replaceAll("actionId", "id"));
		JSONObject newPermissionsObj = new JSONObject();
		for (int i = 0; i < newPermissionsArray.length(); i++) {
			JSONObject currPermissionObj = newPermissionsArray.optJSONObject(i);
			newPermissionsObj.put(currPermissionObj.optString("coreCustomerId"), currPermissionObj);
		}
		JSONArray accLevelPermissions = contractDetails.optJSONArray("accountLevelPermissions1");
		JSONArray remainingAccLevelPermissions = new JSONArray();
		JSONArray changedAccLevelPermissions = null;

		for (int i = 0; i < accLevelPermissions.length(); i++) {
			JSONObject accLevelPermissionsObj = accLevelPermissions.optJSONObject(i);
			if (newPermissionsObj.has(accLevelPermissionsObj.optString("coreCustomerId"))) {
				changedAccLevelPermissions = new JSONArray();
				JSONArray existingAccounts = accLevelPermissionsObj.getJSONArray("accounts");
				compareAccountPermissions(remainingAccLevelPermissions, changedAccLevelPermissions, existingAccounts,
						newPermissionsObj.optJSONObject(accLevelPermissionsObj.optString("coreCustomerId")),
						accLevelPermissionsObj.optString("coreCustomerId"));
				
			}else {
				remainingAccLevelPermissions.put(accLevelPermissionsObj);
			}
		}

		if (changedAccLevelPermissions.length() > 0) {
			finalResult.put("accountLevelPermissionsOld", changedAccLevelPermissions);
		}
		finalResult.put("accountLevelPermissions", remainingAccLevelPermissions);

	}

	private void compareContractCustomersData(JSONArray oldData, JSONArray newData, JSONObject finalResult,
			JSONArray deletedCustomers) {
		JSONObject newObj = new JSONObject();
		JSONObject oldObj = new JSONObject();
		boolean isDiff = false;
		for (int i = 0; i < newData.length(); i++) {
			JSONObject coreCustomer = newData.optJSONObject(i);
			newObj.put(coreCustomer.optString("coreCustomerId"), coreCustomer);
		}
		for (int i = 0; i < oldData.length(); i++) {
			JSONObject coreCustomer = oldData.optJSONObject(i);
			oldObj.put(coreCustomer.optString("coreCustomerId"), coreCustomer);
		}
		
		for (String key : newObj.keySet()) {
			if (!oldObj.has(key)) {
				isDiff = true;
			} else if (oldObj.optJSONObject(key).optBoolean("isPrimary") != newObj.optJSONObject(key)
					.optBoolean("isPrimary")
					|| compareCoreCustomerAccounts(oldObj.optJSONObject(key).optJSONArray("accounts"),
							newObj.optJSONObject(key).optJSONArray("accounts"))) {
				isDiff = true;
			}
		}

		if (deletedCustomers.length() > 0) {
			isDiff = true;
		}

		if (isDiff) {
			editedList.add("contractCustomers");
			finalResult.put("contractCustomersOld", oldData);
		}
		finalResult.put("contractCustomers", newData);
	}

	private boolean compareCoreCustomerAccounts(JSONArray oldData, JSONArray newData) {
		if (oldData.length() != newData.length())
			return true;
		List<String> accountList = new ArrayList<>();
		for (int i = 0; i < oldData.length(); i++) {
			String accId = oldData.optJSONObject(i).optString("accountId");
			accountList.add(accId);
		}
		for (int i = 0; i < newData.length(); i++) {
			String accId = newData.optJSONObject(i).optString("accountId");
			if (!accountList.contains(accId)) {
				return true;
			}
		}
		return false;
	}

	private void compareAddressOrCommunicationData(JSONArray oldData, JSONArray newData, JSONObject finalResult,
			String param) {

		JSONObject addressJSON = newData.optJSONObject(0);
		JSONObject oldAddressJSON = oldData.optJSONObject(0);
		JSONObject oldChangedKeys = new JSONObject();
		boolean isChanged = false;
		for (String key : addressJSON.keySet()) {
			if (!oldAddressJSON.has(key)) {
				isChanged = true;
			} else if (!oldAddressJSON.optString(key).equals(addressJSON.optString(key))) {
				isChanged = true;
				oldChangedKeys.put(key, oldAddressJSON.optString(key));
			}
		}
		if (isChanged) {
			finalResult.put(param + "Old", oldChangedKeys);
			editedList.add(param);
		}
		finalResult.put(param, newData);

	}

	private void compareData(JSONObject oldData, JSONObject newData, JSONObject finalResult) {
		String[] changedKeys = compareObject(oldData, newData);
		
		for (String key : newData.keySet()) {
			if(Arrays.asList(UtilConstants.EDIT_CONTRACT_MC_LIST.split(",")).contains(key))
				finalResult.put(key, newData.optString(key));
		}
		if (changedKeys != null) {
			for (String key : changedKeys) {
				finalResult.put(key + "Old", oldData.optString(key));
				editedList.add(key);

			}
		}

	}

}
