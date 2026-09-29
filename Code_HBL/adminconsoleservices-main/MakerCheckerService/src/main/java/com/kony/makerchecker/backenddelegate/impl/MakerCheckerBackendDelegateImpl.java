package com.kony.makerchecker.backenddelegate.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.exception.DBPAuthenticationException;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.DBPServices;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.kony.makerchecker.backenddelegate.api.MakerCheckerBackendDelegate;
import com.kony.makerchecker.javaservice.LoadMakerCheckerConfigData;
import com.kony.makerchecker.utils.ErrorCodesEnum;
import com.kony.makerchecker.utils.MakerCheckerUtils;
import com.kony.makerchecker.utils.UtilConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.exceptions.MiddlewareException;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class MakerCheckerBackendDelegateImpl implements MakerCheckerBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule(UtilConstants.INFINITY, UtilConstants.SPOTLIGHT);
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule(UtilConstants.INFINITY, UtilConstants.SPOTLIGHT);

	@Override
	public Map<String,String> isMakerCheckerEnabled(DataControllerRequest request, String expApiOperationName, String legalEntityId) {
		try {
			JSONArray makerCheckerData = new JSONArray();
			makerCheckerData = LoadMakerCheckerConfigData.getMakerCheckerConfigData();
			List<Object> makerCheckerList = makerCheckerData.toList().stream().filter(item -> {
	            HashMap<String, String> obj = (HashMap<String, String>) item;
	            return obj.containsKey(UtilConstants.EXPAPIOPNAME) && obj.get(UtilConstants.EXPAPIOPNAME).equals(expApiOperationName)
	                    &&(StringUtils.isNotBlank(legalEntityId) && 
    			obj.containsKey(UtilConstants.COMPANY_LEGAL_UNIT) && obj.get(UtilConstants.COMPANY_LEGAL_UNIT)
				.equals(legalEntityId));
	        }).collect(Collectors.toList());
			if(makerCheckerList.size()>0) {
//				for(int i=0;i<makerCheckerList.size();i++) {
//					Map<String, String> makerCheckerElement = (HashMap<String, String>) makerCheckerList.get(i);
//					String isApprovalRequired = makerCheckerElement.get(UtilConstants.IS_APPROVAL_REQ);
//
//					if(isApprovalRequired.equalsIgnoreCase(UtilConstants.TRUE)||isApprovalRequired.equals("1")) {
//						return makerCheckerElement;
//					}
//				}
				return (HashMap<String, String>) makerCheckerList.get(0);
			}
		}
		catch(Exception e) {
			alert.prepareError("Caught exception at isMakerCheckerEnabled: " + e).log();
		}

		return null;
	}

	public JSONObject storePayloadForRequest(DataControllerRequest request, Map<String, String> inputParams) {
		String serviceName = UtilConstants.MAKERCHECKERCRUD;
		String operationName = UtilConstants.APPROVAL_REQUESTS_CREATE ;

		String permissionapprovalsResponse = null;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		String reqId = UtilConstants.REQ + CommonUtilities.getRandomId();
		String payload = request.getParameter(UtilConstants.PAYLOAD);
		String recordId = request.getParameter(UtilConstants.RECORD_ID);
		String expApiOperationName = request.getParameter(UtilConstants.EXPAPIOPERATIONNAME);
		String companyLegalUnit = request.getParameter(UtilConstants.LEGAL_ENTITY_ID);
		String module = request.getParameter(UtilConstants.MODULE);
		String action = request.getParameter(UtilConstants.ACTION);
		String username = request.getParameter(UtilConstants.CREATED_BY);
		String permissionName = request.getParameter(UtilConstants.PERMISSION_NAME);
		
		requestParameters.put(UtilConstants.REQUEST_ID, reqId);
		requestParameters.put(UtilConstants.REQ_PAYLOAD, payload);
		requestParameters.put(UtilConstants.RECORD_ID, recordId);
		requestParameters.put(UtilConstants.EXPAPIOPNAME, expApiOperationName);
		requestParameters.put(UtilConstants.STATUS,UtilConstants.SID_PENDING);
		requestParameters.put(UtilConstants.MODULE, module);
		requestParameters.put(UtilConstants.ACTION, action);
		requestParameters.put(UtilConstants.COMPANY_LEGAL_UNIT, companyLegalUnit);
		requestParameters.put(UtilConstants.CREATED_BY, username);
		requestParameters.put(UtilConstants.PERMISSION_NAME, permissionName);
		try {
			permissionapprovalsResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName)
					.withObjectId(null).withOperationId(operationName).withRequestParameters(requestParameters).build()
					.getResponse();
			JSONObject responseObj = new JSONObject(permissionapprovalsResponse);
			if (responseObj != null && responseObj.has(UtilConstants.APPROVAL_REQUESTS)) {
				JSONArray jsonArray = responseObj.optJSONArray(UtilConstants.APPROVAL_REQUESTS);
				return jsonArray.optJSONObject(0);
			}

		} catch (JSONException e) {
			diagnostic.prepareDebug("Failed to create data in approvalrequests: " + e).log();
			return null;
		} catch (Exception e) {
			diagnostic.prepareDebug("Caught exception at storePayloadForRequest: " + e).log();
			return null;
		}
		return null;
	}

	public JSONObject getDashboardCounts(DataControllerRequest request, Map<String, String> inputParams) {
		JSONObject resultJson = new JSONObject();
		Map<String, String> userAttributes = CommonUtilities.getLoggedInUserAttributes(request);

		String username = "";
		String legalEntityIds = "";

		JSONObject obj;
		try {
			obj = CommonUtilities.getStringAsJSONObject(request.getServicesManager().getIdentityHandler()
					.getSecurityAttributes().get(UtilConstants.RAW_RESPONSE).toString());

			legalEntityIds = CommonUtilities.getStringAsJSONObject(obj.get(UtilConstants.USER_ATTRIBUTES).toString())
					.getString(UtilConstants.LEGAL_ENTITY_ID);
			username = CommonUtilities.getStringAsJSONObject(obj.get(UtilConstants.USER_ATTRIBUTES).toString())
					.getString(UtilConstants.USERNAME);

			diagnostic.prepareDebug("userAttributes " + userAttributes).log();
			Set<String> legalEntitySet = new HashSet<String>(Arrays.asList(legalEntityIds.split(",")));
		
			JSONObject makerRequests = new JSONObject();
			makerRequests = getMakerRequests(request, username, legalEntityIds);
			
			diagnostic.prepareDebug("makerRequests : " + makerRequests.toString()).log();

			resultJson.put(UtilConstants.MAKER_REQUESTS, makerRequests);
			
			HashMap<String, Set<String>> leWisePermissionMap = new HashMap<>();
			leWisePermissionMap = MakerCheckerUtils.getLEWisePermissions(request, legalEntitySet);
			
			if(!leWisePermissionMap.isEmpty()) {
//				diagnostic.prepareDebug("User do not have the required permissions.").log();
//				return ErrorCodesEnum.ERR_10011.setErrorCode(new JSONObject());
				String leWithCheckerPermissions = "";
				for(String le: leWisePermissionMap.keySet()) {
					leWithCheckerPermissions+=le+",";
				}
				
				diagnostic.prepareDebug("leWisePermissionMap " + leWisePermissionMap).log();
				
				JSONObject checkerRequests = new JSONObject();
				checkerRequests = getCheckerRequests(leWisePermissionMap, username);

				diagnostic.prepareDebug("checkerRequests : " + checkerRequests.toString()).log();

				
				resultJson.put(UtilConstants.CHECKER_REQUESTS, checkerRequests);
				resultJson.put("leWithCheckerPermissions", leWithCheckerPermissions.substring(0,leWithCheckerPermissions.length()-1));
			}
			
			return resultJson;
		} catch (MiddlewareException e) {
			diagnostic.prepareDebug("Exception occured while trying to get dashboard counts in backend delegate layer").log();

		}
		return null;

	}

	private JSONObject getCheckerRequests(HashMap<String, Set<String>> leWisePermissionMap, String username) {
		String serviceName = UtilConstants.AC_MAKER_CHECKER_CRUD;
		String operationName = UtilConstants.DB_GET_MC_APPROVALREQUESTS_VIEW;

		String permissionapprovalsResponse = null;
		Map<String, Object> requestParameters = new HashMap<String, Object>();

		String filter = "";

		for (Map.Entry<String, Set<String>> entry : leWisePermissionMap.entrySet()) {
			String key = entry.getKey();
			Set<String> val = entry.getValue();
			filter = filter + "( "+UtilConstants.COMPANY_LEGAL_UNIT+" eq '" + key + "' and " +
					UtilConstants.CREATED_BY +" ne '"+username+"' and " +UtilConstants.STATUS +" eq 'SID_PENDING' and ";
			filter += "(" + UtilConstants.APPROVAL_PERMISSION_NAME + " eq "
					+ String.join(" or " + UtilConstants.APPROVAL_PERMISSION_NAME + " eq ", val) + ") )";
			filter = filter + (" or ");
		}
		
		if(filter.length()>0)
			filter = filter.substring(0, filter.length() - 4);
		diagnostic.prepareDebug(" filterafter " + filter).log();
		requestParameters.put(ODataQueryConstants.FILTER, filter);

		try {
			permissionapprovalsResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName)
					.withObjectId(null).withOperationId(operationName).withRequestParameters(requestParameters).build()
					.getResponse();
			JSONObject responseObj = new JSONObject(permissionapprovalsResponse);
			diagnostic.prepareDebug(" permissionapprovalsResponse2 " + permissionapprovalsResponse).log();
			if (responseObj != null && responseObj.has(UtilConstants.GET_MC_APPROVALREQUESTS_VIEW)) {
				JSONArray jsonArray = responseObj.optJSONArray(UtilConstants.GET_MC_APPROVALREQUESTS_VIEW);
				return responseObj;
			}
			else if(responseObj != null && responseObj.has(UtilConstants.ERRORCODE)){
				if(responseObj.get(UtilConstants.ERRORCODE)=="20024") {
					diagnostic.prepareDebug("Exception occured while trying to get data from get_mc_approvalrequests_view.").log();
					return ErrorCodesEnum.ERR_10012.setErrorCode(new JSONObject());
				}
			}

		} catch (Exception e) {
			diagnostic.prepareDebug("Caught exception while fetching data from approvalrequests: " + e).log();
			return null;
		}
		return null;
	}

	private JSONObject getMakerRequests(DataControllerRequest request, String username, String legalEntityIds) {
		String serviceName = UtilConstants.AC_MAKER_CHECKER_CRUD;
		String operationName = UtilConstants.DB_GET_MC_APPROVALREQUESTS_VIEW;

		String permissionapprovalsResponse = null;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		Set<String> leSet = Stream.of(legalEntityIds.trim().split(",")).collect(Collectors.toSet());
		if(leSet.size() > 0) {
			leSet.add(UtilConstants.MAKER_CHECKER_SHARED_LEGAL_ENTITY);
		}
		String filter = UtilConstants.CREATED_BY+" eq '" + username + "' and "+UtilConstants.STATUS+" eq 'SID_PENDING' and ";
		filter = filter + "(" + UtilConstants.COMPANY_LEGAL_UNIT + " eq " + String.join(" or " + UtilConstants.COMPANY_LEGAL_UNIT + " eq ", leSet)
				+ ")";
		JSONObject responseObj = new JSONObject();
		requestParameters.put(ODataQueryConstants.FILTER, filter);
		diagnostic.prepareDebug(" filter1 " + filter).log();
		try {
			permissionapprovalsResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.build()
					.getResponse();
			responseObj = new JSONObject(permissionapprovalsResponse);
			diagnostic.prepareDebug(" permissionapprovalsResponse1 " + permissionapprovalsResponse).log();
			if (responseObj != null || responseObj.has(UtilConstants.GET_MC_APPROVALREQUESTS_VIEW)) {
				JSONArray jsonArray = responseObj.optJSONArray(UtilConstants.GET_MC_APPROVALREQUESTS_VIEW);
				return responseObj;
			}

		} catch (Exception e) {
			diagnostic.prepareDebug("Exception occured while trying to get data from get_mc_approvalrequests_view." + e).log();
			return ErrorCodesEnum.ERR_10010.setErrorCode(responseObj);
		}

		return null;
	}
	
	@Override
	public JSONObject getMakerCheckerPendingRequests(DataControllerRequest request, Map<String, String> inputParams) {
		JSONObject responseObj = new JSONObject();
		try {
			String serviceName = UtilConstants.MAKER_CHECKER_CRUD;
			String operationName = UtilConstants.DB_GET_PENDINGREQUESTS;
			String pendingrequestsResponse = null;

			Map<String, Object> requestParameters = new HashMap<String, Object>();
			requestParameters.put("_module", inputParams.get(UtilConstants.MODULE));
			requestParameters.put("_action", inputParams.get(UtilConstants.ACTION));
			requestParameters.put("_record", inputParams.get(UtilConstants.RECORD_ID));
			requestParameters.put("_companyLegalUnitId", inputParams.get(UtilConstants.COMPANY_LEGAL_UNIT));

			pendingrequestsResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(requestParameters).build().getResponse();
			responseObj = new JSONObject(pendingrequestsResponse);

			if (responseObj != null) {
				return responseObj;
			}
			diagnostic.prepareDebug(" pendingrequestsResponse " + pendingrequestsResponse).log();
		} catch (Exception e) {
			diagnostic.prepareDebug(
							"Exception occured while trying to get data from dbxdb_checkforpendingrequests_proc." + e).log();
			return ErrorCodesEnum.ERR_10014.setErrorCode(responseObj);
		}
		return null;
	}
	
	public JSONObject approvalRequestViewDetails(DataControllerRequest request, Map<String, String> inputParams) {
		try {
			HashMap<String, String> operationName = new HashMap<String, String>();
			JSONArray makerCheckerData = new JSONArray();
			JSONObject responseObj = new JSONObject();
			makerCheckerData = LoadMakerCheckerConfigData.getMakerCheckerConfigData();
			List<Object> makerCheckerList = makerCheckerData.toList().stream().filter(item -> {
	            HashMap<String, String> obj = (HashMap<String, String>) item;
	            return obj.containsKey(UtilConstants.MODULE) && obj.get(UtilConstants.MODULE).equals(inputParams.get(UtilConstants.MODULE))
	                    && obj.containsKey(UtilConstants.ACTION) && obj.get(UtilConstants.ACTION).equals(inputParams.get(UtilConstants.ACTION));
	        }).collect(Collectors.toList());
			if (makerCheckerList.size() > 0) {
				operationName = (HashMap<String, String>) makerCheckerList.get(0);
				if (StringUtils.isNotBlank(operationName.get(UtilConstants.VIEWDETAILS_API))) {
					String viewdetailsAPI = operationName.get(UtilConstants.VIEWDETAILS_API);
					String[] params = viewdetailsAPI.split(":");
					Map<String,Object> payload = new HashMap<String,Object>();
					String approvalsAPIResponse = null;
					diagnostic.prepareDebug(" Target API details= " + params[0] + "&" + params[1]).log();
					if(StringUtils.isNotBlank(operationName.get("excludedParams"))) {
						payload.put("excludedParams", operationName.get("excludedParams").toString());
					}
					payload.put("requestId", inputParams.get("requestId"));
					approvalsAPIResponse = DBPServiceExecutorBuilder.builder().withServiceId(params[0])
							.withObjectId(null).withOperationId(params[1]).withRequestParameters(payload)
							.build().getResponse();
					responseObj = new JSONObject(approvalsAPIResponse);
					diagnostic.prepareDebug(" Backend API Response= " + approvalsAPIResponse).log();

					if (responseObj != null) {
						return responseObj;
					}
				}
			}
		}
		catch(Exception e) {
			alert.prepareError("Caught exception while fetching View Approval request: " + e).log();
		}

		return null;
	}
	
	public void callExpAPIAsyncAndGetResult(DataControllerRequest request, String service, String object, String operation,
			String requestId, HashMap<String, Object> payload, String username) {
		
		MakerCheckerUtils.asyncCallExpAPI(request, service, object, operation, requestId, payload, username);
	}


	

	@Override
	public JSONObject getRequestsHistory(Map<String, Object> inputPayload) {
		String serviceName = UtilConstants.AC_MAKER_CHECKER_CRUD;
		String operationName = UtilConstants.DB_GET_MC_REQUESTSHISTORY_VIEW;
		String requestsResponse = null;
		try {
			requestsResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName)
					.withObjectId(null).withOperationId(operationName).withRequestParameters(inputPayload).build()
					.getResponse();
			JSONObject responseObj = new JSONObject(requestsResponse);
			diagnostic.prepareDebug(" requests response history " + requestsResponse).log();
			if (responseObj != null && responseObj.has(UtilConstants.GET_MC_REQUESTSHISTORY_VIEW)) {
				return responseObj;
			} else if (responseObj != null && responseObj.has(UtilConstants.ERRORCODE)) {
				if (responseObj.get(UtilConstants.ERRORCODE) == "20024") {
					diagnostic
							.prepareDebug(
									"Exception occured while trying to get data from get_mc_requestshistory_view.")
							.log();
					return ErrorCodesEnum.ERR_10012.setErrorCode(new JSONObject());
				}
			}
		} 
		catch (Exception e) {
			diagnostic.prepareDebug("Caught exception while fetching data from requestshistory: " + e).log();
			return null;
		}
		return null;
	}

	@Override
	public void updateApprovalRequestStatus(DataControllerRequest request, String requestId, String status,
			String comments, String currentStatus, String username) {
		
		MakerCheckerUtils.updateApprovalRequestStatus(request, requestId, status, comments, currentStatus, username);
		
	}
	
	@Override
	public JSONObject getMakerCheckerConfig(DataControllerRequest request, Map<String, String> inputParams) {

		JSONObject responseObj = new JSONObject();
		String serviceName = UtilConstants.AC_MAKER_CHECKER_CRUD;
		String operationName = UtilConstants.DB_MAKER_CHECKER_CONFIG_VIEW;
		String getMakerCheckerConfigResponse = null;
		String legalEntityId = inputParams.get(UtilConstants.LEGAL_ENTITY_ID);
		String languageCode = inputParams.get(UtilConstants.LANGUAGE_CODE);

		try {
			Map<String, Object> requestParameters = new HashMap<String, Object>();
			String filter = UtilConstants.LANGUAGE_CODE + " eq '" + languageCode + "' and "
					+ UtilConstants.COMPANY_LEGAL_UNIT + " eq '" + legalEntityId + "'";
			requestParameters.put(ODataQueryConstants.FILTER, filter);

			getMakerCheckerConfigResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName)
					.withOperationId(operationName).withRequestParameters(requestParameters).build().getResponse();
			diagnostic.prepareDebug(" getMakerCheckerConfigResponse " + getMakerCheckerConfigResponse).log();
			responseObj = new JSONObject(getMakerCheckerConfigResponse);
			if (responseObj != null && responseObj.has(UtilConstants.MAKER_CHECKER_CONFIG_VIEW)) {
				return responseObj;
			} else if (responseObj != null && responseObj.has(UtilConstants.ERRORCODE)) {
				if ("20024".equals(responseObj.opt(UtilConstants.ERRORCODE))) {
					diagnostic.prepareDebug("Exception occured while trying to get data from dbxdb_makercheckerconfig_view_get.").log();
					return ErrorCodesEnum.ERR_10012.setErrorCode(new JSONObject());
				}
			}
		} catch (Exception e) {
			diagnostic.prepareDebug("Exception occured while trying to get data from dbxdb_makercheckerconfig_view_get." + e).log();
			return ErrorCodesEnum.ERR_10024.setErrorCode(responseObj);
		}
		return null;
	}
	
	@Override
	public JSONObject updateMakerCheckerConfig(DataControllerRequest request, Map<String, Object> inputParams) {
		JSONObject responseObj = new JSONObject();
		String serviceName = UtilConstants.AC_MAKER_CHECKER_CRUD;
		String operationName = UtilConstants.DB_UPDATE_MAKER_CHECKER_CONFIG_PROC;
		String updateMakerCheckerConfigResponse = null;
		
		JSONArray configArray = (JSONArray) inputParams.get("mcConfigData");
		JSONObject inputObj = new JSONObject();
		inputObj.put("updates", configArray);

		try {
			Map<String, Object> requestParameters = new HashMap<String, Object>();
			requestParameters.put("_input", inputObj.toString());

			updateMakerCheckerConfigResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName)
					.withOperationId(operationName).withRequestParameters(requestParameters).build().getResponse();
			diagnostic.prepareDebug(" updateMakerCheckerConfigResponse " + updateMakerCheckerConfigResponse).log();
			responseObj = new JSONObject(updateMakerCheckerConfigResponse);
			if (responseObj != null && Integer.valueOf(responseObj.optString(UtilConstants.OPSTATUS)) == 0
					&& Integer.valueOf(responseObj.optString(UtilConstants.HTTP_STATUS_CODE)) == 0) {
				AuditHandler.auditAdminActivity(request, ModuleNameEnum.MAKERCHECKERCONFIGURATIONS, EventEnum.UPDATE,
						ActivityStatusEnum.SUCCESSFUL, "Maker Checker Configurations update success");
				LoadMakerCheckerConfigData loadMakerCheckerConfigData = new LoadMakerCheckerConfigData();
				loadMakerCheckerConfigData.loadDataFromDB();
				return responseObj;
			} else if (responseObj != null && Integer.valueOf(responseObj.optString(UtilConstants.OPSTATUS)) != 0) {
				diagnostic.prepareDebug("Exception occured while trying to get data from dbxdb_update_makercheckerconfig_proc.").log();
				AuditHandler.auditAdminActivity(request, ModuleNameEnum.MAKERCHECKERCONFIGURATIONS, EventEnum.UPDATE,
						ActivityStatusEnum.FAILED, "Maker Checker Configurations update failed");
				return ErrorCodesEnum.ERR_10012.setErrorCode(new JSONObject());
			}
		} catch (Exception e) {
			diagnostic.prepareDebug("Exception occured while trying to get data from dbxdb_update_makercheckerconfig_proc." + e).log();
			AuditHandler.auditAdminActivity(request, ModuleNameEnum.MAKERCHECKERCONFIGURATIONS, EventEnum.UPDATE,
					ActivityStatusEnum.FAILED, "Maker Checker Configurations update failed");
			return ErrorCodesEnum.ERR_10040.setErrorCode(responseObj);
		}
		return null;
	}
	
	@Override
	public JSONObject getApprovalRequests(DataControllerRequest request, String reqId) {
		String serviceName = UtilConstants.MAKER_CHECKER_CRUD;
		String operationName = UtilConstants.APPROVAL_REQUESTS_GET;

		String approvalRequestResponse = null;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = UtilConstants.REQUEST_ID +" eq '" + reqId + "'" ;
		JSONObject responseObj = new JSONObject();
		requestParameters.put(ODataQueryConstants.FILTER, filter);
		diagnostic.prepareDebug(" filter " + filter).log();
		try {
			approvalRequestResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.build()
					.getResponse();
			responseObj = new JSONObject(approvalRequestResponse);
			diagnostic.prepareDebug(" approvalRequestResponse " + approvalRequestResponse).log();
			if (responseObj != null || responseObj.has(UtilConstants.APPROVAL_REQUESTS_GET_RECORD)) {
				JSONArray jsonArray = responseObj.optJSONArray(UtilConstants.APPROVAL_REQUESTS_GET_RECORD);
				return responseObj;
			}

		} catch (Exception e) {
			diagnostic.prepareDebug("Exception occured while trying to get data from approvalrequests" + e).log();
			return ErrorCodesEnum.ERR_10009.setErrorCode(responseObj);
		}

		return null;
	}

	public JSONObject getCheckerApprovalRequests(DataControllerRequest request, Map<String, String> inputParams) {
		JSONObject finalResponseObj = new JSONObject();
		try {
			Map<String, Object> requestParameters = new HashMap<String, Object>();
			JSONObject responseObj = new JSONObject();

			String serviceName = UtilConstants.MAKER_CHECKER_CRUD;
			String operationName = UtilConstants.DB_GET_CHECKERPENDING_REQUESTS_PROC;
			String approvalsAPIResponse = null;

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
			requestParameters.put("_userName",
					StringUtils.isNotBlank(inputParams.get(UtilConstants.USER_NAME))
							? inputParams.get(UtilConstants.USER_NAME)
							: null);
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

			diagnostic.prepareDebug("Input payload for get_checkerpending_requests_proc=" + requestParameters).log();
			approvalsAPIResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(requestParameters).build().getResponse();

			responseObj = new JSONObject(approvalsAPIResponse);

			diagnostic.prepareDebug(" Backend API Response in getCheckerApprovalRequests= " + approvalsAPIResponse)
					.log();
			if (responseObj != null && responseObj.has(UtilConstants.RECORDS)) {
				JSONArray jsonArray = responseObj.optJSONArray(UtilConstants.RECORDS1);
				JSONArray jsonArray1 = responseObj.optJSONArray(UtilConstants.RECORDS);
				String totalNumberOfRecords = jsonArray1.getJSONObject(0).getString(UtilConstants.TOTALRECORDS);
				finalResponseObj.put(UtilConstants.APPROVARPENDINGREQUESTS, jsonArray);
				finalResponseObj.put(UtilConstants.TOTALNUMBEROFRECORDS, totalNumberOfRecords);
				return finalResponseObj;
			}

		} catch (Exception e) {
			alert.prepareError(
					"Caught exception while fetching Pending Checker Approval request in get_checkerpending_requests_proc: "
							+ e)
					.log();
			return ErrorCodesEnum.ERR_10034.setErrorCode(finalResponseObj);
		}

		return null;
	}
	
	@SuppressWarnings({ "null", "unused" })
	@Override
	public JSONObject getAllMakerPendingRequests(DataControllerRequest request, Map<String, Object> inputParams) {
		JSONObject responseObj = new JSONObject();
		JSONObject finalResponseObj = new JSONObject();
		try {
			String serviceName = UtilConstants.MAKERCHECKERCRUD;
			String operationName = UtilConstants.DB_FETCH_MAKER_PENDING_REQUESTS_PROC;
			String makerRequestsAPIResponse = null;
			
			makerRequestsAPIResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
                    .withOperationId(operationName).withRequestParameters(inputParams).build().getResponse();
 
            responseObj = new JSONObject(makerRequestsAPIResponse);
				if (responseObj != null) {
					JSONArray jsonArray = responseObj.optJSONArray("records1");
					JSONArray jsonArray1 = responseObj.optJSONArray("records");
					String totalNumberOfRecords = jsonArray1.getJSONObject(0).getString("totalRecords");;
					finalResponseObj.put("makerPendingRequests", jsonArray);
					finalResponseObj.put("totalNumberOfRecords", totalNumberOfRecords);
					return finalResponseObj;
				}
			diagnostic.prepareDebug("makerRequestsAPIResponse :" + makerRequestsAPIResponse).log();
		} catch (Exception e) {
			diagnostic.prepareDebug(
							"Exception occured while trying to get data from dbxdb_fetch_maker_pending_requests_proc." + e).log();
			return ErrorCodesEnum.ERR_10029.setErrorCode(finalResponseObj);
		}
		return null;
	}
	
	@Override
	public JSONObject getMCModuleActionOperation(DataControllerRequest request, Map<String, String> inputParams) {
		JSONObject responseObj = new JSONObject();
		try {
			String serviceName = UtilConstants.MAKER_CHECKER_CRUD;
			String operationName = UtilConstants.DB_GET_MC_MODULEACTIONNAME_VIEW_GET;
			String moduleActionNames = null;

			Map<String, Object> requestParameters = new HashMap<String, Object>();

			String filter = UtilConstants.LANGUAGECODE + " eq " + inputParams.get(UtilConstants.LANGUAGECODE);
			requestParameters.put(ODataQueryConstants.FILTER, filter);

			moduleActionNames = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(requestParameters).build().getResponse();
			responseObj = new JSONObject(moduleActionNames);

			if (responseObj != null) {
				return responseObj;
			}
			diagnostic.prepareDebug(" Module Action Names Backend Response = " + moduleActionNames).log();
		} catch (Exception e) {
			diagnostic
					.prepareDebug(
							"Exception occured while trying to get data from dbxdb_get_mc_moduleactionname_view." + e)
					.log();
			return ErrorCodesEnum.ERR_10039.setErrorCode(responseObj);
		}
		return null;
	}

	@Override
	public JSONObject getEnrollCustomerViewDetails(DataControllerRequest request, Map<String, String> inputParams) {
		JSONObject finalResponseObj = new JSONObject();
		try {
			Map<String, Object> requestParameters = new HashMap<String, Object>();
			JSONObject responseObj = new JSONObject();

			requestParameters.put("_companyLegalUnit", inputParams.getOrDefault(UtilConstants.LEGAL_ENTITY_ID, null));
			requestParameters.put("_roleId", inputParams.getOrDefault("roleIds", null));
			requestParameters.put("_cif", inputParams.getOrDefault("cifIds", null));
			requestParameters.put("_servicedefId", inputParams.getOrDefault("serviceDefIds", null));

			String serviceName = UtilConstants.MAKER_CHECKER_CRUD;
			String operationName = UtilConstants.DB_GET_ENROLLCUSTOMER_VIEWDETAILS_PROC;
			String enrollCustomerViewDetailsResponse = null;

			enrollCustomerViewDetailsResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName)
					.withOperationId(operationName).withRequestParameters(requestParameters).build().getResponse();
			diagnostic.prepareDebug("enrollCustomerViewDetailsResponse :" + enrollCustomerViewDetailsResponse).log();
			responseObj = new JSONObject(enrollCustomerViewDetailsResponse);
			if (responseObj != null && responseObj.has(UtilConstants.RECORDS)) {
				return responseObj;
			}

		} catch (Exception e) {
			alert.prepareError("Exception occured while fetching enroll customer view details " + e).log();
			return ErrorCodesEnum.ERR_10049.setErrorCode(finalResponseObj);
		}
		return null;
	}
	
	@Override
	public JSONObject getEditCustomerViewDetails(DataControllerRequest request, Map<String, String> inputParams) {
		JSONObject finalResponseObj = new JSONObject();
		try {
			Map<String, Object> requestParameters = new HashMap<String, Object>();
			JSONObject responseObj = new JSONObject();

			requestParameters.put("_legalEntityId", inputParams.getOrDefault(UtilConstants.LEGAL_ENTITY_ID, null));
			requestParameters.put("_customerId", inputParams.get("customerId"));

			String serviceName = UtilConstants.MAKER_CHECKER_CRUD;
			String operationName = UtilConstants.DB_GET_EDITCUSTOMER_VIEWDETAILS_PROC;
			String editCustomerViewDetailsResponse = null;

			editCustomerViewDetailsResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName)
					.withOperationId(operationName).withRequestParameters(requestParameters).build().getResponse();
			diagnostic.prepareDebug("editCustomerViewDetailsResponse :" + editCustomerViewDetailsResponse).log();
			responseObj = new JSONObject(editCustomerViewDetailsResponse);
			if (responseObj != null && responseObj.has(UtilConstants.RECORDS)) {
				return responseObj;
			}

		} catch (Exception e) {
			alert.prepareError("Exception occured while fetching edit customer view details " + e).log();
			return ErrorCodesEnum.ERR_10052.setErrorCode(finalResponseObj);
		}
		return null;
	}
	
	@Override
	public JSONObject getCreateApprovalRuleBySGViewDetails(DataControllerRequest request, Map<String, String> inputParams) {
		JSONObject finalResponseObj = new JSONObject();
		try {
			Map<String, Object> requestParameters = new HashMap<String, Object>();
			JSONObject responseObj = new JSONObject();

			requestParameters.put("_legalEntityId", inputParams.getOrDefault(UtilConstants.LEGAL_ENTITY_ID, null));
			requestParameters.put("_contractId", inputParams.get("contractId"));
			requestParameters.put("_coreCustomerId", inputParams.get("coreCustomerId"));
			requestParameters.put("_groupIds", inputParams.get("groupIds"));
			requestParameters.put("_actionId", inputParams.get("actionId"));
			requestParameters.put("_accountId", inputParams.getOrDefault("accountId", null));
			diagnostic.prepareDebug("payload for get_create_approvalrule_req_view_details proc : "+requestParameters.toString());
			String serviceName = UtilConstants.MAKER_CHECKER_CRUD;
			String operationName = UtilConstants.DB_GET_CREATE_APPROVALRULE_REQ_VIEWDETAILS_PROC;
			String createApprovalRuleReqViewDetailsResponse = null;

			createApprovalRuleReqViewDetailsResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName)
					.withOperationId(operationName).withRequestParameters(requestParameters).build().getResponse();
			diagnostic.prepareDebug("createApprovalRuleReqViewDetailsResponse :" + createApprovalRuleReqViewDetailsResponse).log();
			responseObj = new JSONObject(createApprovalRuleReqViewDetailsResponse);
			if (responseObj != null && responseObj.has(UtilConstants.RECORDS)) {
				return responseObj;
			}

		} catch (Exception e) {
			alert.prepareError("Exception occured while fetching edit customer view details " + e).log();
			return ErrorCodesEnum.ERR_10052.setErrorCode(finalResponseObj);
		}
		return null;
	}
	
	@Override
	public JSONObject updateApprovalRequests(DataControllerRequest request, Map<String, Object> requestParameters) {
		JSONObject responseObj = new JSONObject();
		String serviceName = UtilConstants.MAKERCHECKERCRUD;
		String operationName = UtilConstants.APPROVAL_REQUESTS_UPDATE;
		String updateApprovalRequests = null;

		try {
			updateApprovalRequests = DBPServiceExecutorBuilder.builder().withServiceId(serviceName)
					.withOperationId(operationName).withRequestParameters(requestParameters).build().getResponse();
			diagnostic.prepareDebug(" updateApprovalRequests " + updateApprovalRequests).log();
			responseObj = new JSONObject(updateApprovalRequests);
			if (responseObj != null && responseObj.has(UtilConstants.APPROVAL_REQUESTS)) {
				JSONArray jsonArray = responseObj.optJSONArray(UtilConstants.APPROVAL_REQUESTS);
				return jsonArray.optJSONObject(0);
			}
		} catch (Exception e) {
			diagnostic.prepareDebug("Exception occured while trying to update data in dbxdb_approvalrequests_update." + e)
						.log();
			return ErrorCodesEnum.ERR_10051.setErrorCode(responseObj);
		}
		return null;
	}

	@Override
	public JSONObject getContractDetails(DataControllerRequest request, Map<String, Object> inputParams)
			throws DBPAuthenticationException {
		JSONObject responseObj = new JSONObject();

		String dbpServicesClaimsToken = null;
		dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(request);

		Map<String, Object> headerMap = new HashMap<>();
		headerMap.put("backendToken", dbpServicesClaimsToken);

		String serviceName = UtilConstants.DBPSERVICE;
		String operationName = UtilConstants.GET_CONTRACT_DETAILS;
		String contractDetailsResponse = null;

		try {
			contractDetailsResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName)
					.withOperationId(operationName).withRequestParameters(inputParams).withPassThroughOutput(true)
					.withRequestHeaders(headerMap).build().getResponse();
			diagnostic.prepareDebug(" contractDetailsResponse " + contractDetailsResponse).log();
			contractDetailsResponse.replaceAll("actionId", "id");
			responseObj = new JSONObject(contractDetailsResponse);

		} catch (Exception e) {
			diagnostic.prepareDebug(ErrorCodesEnum.ERR_10054.getMessage() + e).log();
			return ErrorCodesEnum.ERR_10054.setErrorCode(responseObj);
		}
		return responseObj;
	}

	@Override
	public JSONObject getContractFeatureActionLimits(DataControllerRequest request, Map<String, Object> inputParams)
			throws DBPAuthenticationException {

		String dbpServicesClaimsToken = null;
		dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(request);

		Map<String, Object> headerMap = new HashMap<>();
		headerMap.put("backendToken", dbpServicesClaimsToken);

		JSONObject responseObj = new JSONObject();
		String serviceName = UtilConstants.DBPSERVICE;
		String operationName = UtilConstants.GET_CONTRACT_FAL;
		String contractFeatureActionLimitsResponse = null;

		try {
			contractFeatureActionLimitsResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName)
					.withOperationId(operationName).withRequestParameters(inputParams).withPassThroughOutput(true)
					.withRequestHeaders(headerMap).build().getResponse();
			diagnostic.prepareDebug(" contractFeatureActionLimitsResponse " + contractFeatureActionLimitsResponse)
					.log();
			responseObj = new JSONObject(contractFeatureActionLimitsResponse);
			if (responseObj != null) {
				return responseObj;
			}
		} catch (Exception e) {
			diagnostic.prepareDebug(ErrorCodesEnum.ERR_10055.getMessage() + e).log();
			return ErrorCodesEnum.ERR_10055.setErrorCode(responseObj);
		}
		return null;
	}
	
	@Override
	public JSONObject getSignatoryViewDetails(DataControllerRequest request, Map<String, Object> inputParams) {
		JSONObject responseObj = new JSONObject();
		JSONObject finalResponseObj = new JSONObject();
		try {
			String serviceName = UtilConstants.MAKER_CHECKER_CRUD;
			String operationName = UtilConstants.DB_FETCH_SIGNATORY_VIEWDETAILS_PROC;
			String makerRequestsAPIResponse = null;
			
			makerRequestsAPIResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
                    .withOperationId(operationName).withRequestParameters(inputParams).build().getResponse();
			
			diagnostic.prepareDebug("makerRequestsAPIResponse :" + makerRequestsAPIResponse).log();
            responseObj = new JSONObject(makerRequestsAPIResponse);
				
            if (responseObj != null) {				
				JSONArray jsonArray = responseObj.optJSONArray("records");
				//finalResponseObj = jsonArray.getJSONObject(0);
				
				JSONArray signJsonArray = responseObj.optJSONArray("records1");
				
				String action = inputParams.get("action") != null ? inputParams.get("action").toString() : "";
				if("EDIT_SIGNATORY_GROUP".equals(action)) {
					JSONArray editSignJsonArray2 = responseObj.optJSONArray("records2");
					finalResponseObj.put("editSignatories", editSignJsonArray2);
				}
				
				finalResponseObj.put("signDetails", jsonArray.getJSONObject(0));
				finalResponseObj.put("signatories", signJsonArray);				
			}
            return finalResponseObj;
			
		} catch (Exception e) {
			diagnostic.prepareDebug(
							"Exception occured while trying to get data from dbxdb_fetch_makerchecker_viewdetails_proc." + e).log();
			return ErrorCodesEnum.ERR_10029.setErrorCode(finalResponseObj);
		}
	}

}