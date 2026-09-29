package com.kony.makerchecker.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.commons.utils.ThreadExecutor;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.PermissionHandler;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.registry.AppRegistryException;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class MakerCheckerUtils {
	
	private static final Alert alert = Logger.forAlert().forModule(UtilConstants.INFINITY, UtilConstants.SPOTLIGHT);
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule(UtilConstants.INFINITY, UtilConstants.SPOTLIGHT);

	
	public static HashMap<String, Set<String>> getLEWisePermissions(DataControllerRequest request,
			Set<String> legalEntitySet) {
		try {

			List<String> makerCheckerPermissions = new ArrayList<>();
			makerCheckerPermissions = fetchMakerCheckerPermissionsFromDB(request);
			diagnostic.prepareDebug(" makerCheckerPermissions ----" + makerCheckerPermissions.toString()).log();
			String makerCheckerPermissionsStr = "";
			for (String s : makerCheckerPermissions) {
				makerCheckerPermissionsStr += s + "\t";
			}
			diagnostic.prepareDebug(" makerCheckerPermissionsStr ----" + makerCheckerPermissionsStr.toString())
					.log();
			HashMap<String, Set<String>> leWisePermissionMap = new HashMap<>();

			String roles = LoggedInUserHandler.getUserDetails(request.getServicesManager()).getRoleId();
			JSONObject responseObject = PermissionHandler.getRolesGrantedPermissionsWithLEInfo(roles, request);

			String leToRoleMapping = responseObject.getString(UtilConstants.LE_TO_ROLE_MAPPING);
			diagnostic.prepareDebug(" leToRoleMapping ----" + leToRoleMapping).log();
			JSONObject legalEntityDataJson = new JSONObject(leToRoleMapping);
			diagnostic.prepareDebug(" legalEntityDataJson ----" + legalEntityDataJson).log();

			Set<String> sharedPermissionsSet = new HashSet<>();
			for (String leId : legalEntitySet) {
				String permissions = legalEntityDataJson.getString(leId);
				diagnostic.prepareDebug(" permissions----" + permissions).log();
				Set<String> permissionsSet = new HashSet<String>(Arrays.asList(permissions.split(",")));
				diagnostic.prepareDebug(" permissionsSet----" + permissionsSet).log();
				permissionsSet.retainAll(makerCheckerPermissions);
				sharedPermissionsSet.addAll(permissionsSet);
				diagnostic.prepareDebug(" permissionsSet after intersection----" + permissionsSet).log();
				if(!permissionsSet.isEmpty())
					leWisePermissionMap.put(leId, permissionsSet);
				diagnostic.prepareDebug(" leWisePermissionMap----" + leWisePermissionMap).log();

			}
			if(sharedPermissionsSet.size()>0) {
				leWisePermissionMap.put(UtilConstants.MAKER_CHECKER_SHARED_LEGAL_ENTITY, sharedPermissionsSet);
			}
			return leWisePermissionMap;

		} catch (AppRegistryException e) {
			diagnostic.prepareDebug("Caught AppRegistryException").log();
			diagnostic.prepareDebug("Exception occured while trying to fetch LE wise approval permissions.").log();
		} catch (ApplicationException e) {
			diagnostic.prepareDebug("Caught ApplicationException").log();
			diagnostic.prepareDebug("Exception occured while trying to fetch LE wise approval permissions.").log();
		} catch (IOException e) {
			diagnostic.prepareDebug("Caught IOException").log();
			diagnostic.prepareDebug("Exception occured while trying to fetch LE wise approval permissions.").log();
		}
		return null;
	}
	
	private static List<String> fetchMakerCheckerPermissionsFromDB(DataControllerRequest request) {

		String serviceName = UtilConstants.AC_MAKER_CHECKER_CRUD;
		String operationName = UtilConstants.PERMISSIONS_GET;
		List<String> approvalPermissions = new ArrayList<>();
		String response = null;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		requestParameters.put(ODataQueryConstants.FILTER, UtilConstants.TYPE_ID+" eq 'PER_TYPE_MAKERCHECKER'");
		requestParameters.put(ODataQueryConstants.SELECT, UtilConstants.NAME);

		try {
			response = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(requestParameters).build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			diagnostic.prepareDebug(" responseObj4----" + responseObj).log();
			if (responseObj != null && responseObj.has(UtilConstants.PERMISSION)) {
				JSONArray jsonArray = responseObj.optJSONArray(UtilConstants.PERMISSION);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject obj = new JSONObject();
					obj = jsonArray.getJSONObject(i);
					diagnostic.prepareDebug(" obj----" + obj).log();
					if (null != obj && null != obj.getString(UtilConstants.NAME)) {
						diagnostic.prepareDebug(" jsonObj4----" + jsonArray.getJSONObject(i).getString(UtilConstants.NAME))
								.log();
						approvalPermissions.add(jsonArray.getJSONObject(i).getString(UtilConstants.NAME));
					}
				}
				diagnostic.prepareDebug(" approvalPermissions----" + approvalPermissions).log();
				return approvalPermissions;
			}

		} catch (Exception e) {
			diagnostic.prepareDebug("Caught exception while fetching data from approvalrequests: " + e).log();
			return null;
		}

		return null;
	}
	
	public static JSONObject asyncCallExpAPI(DataControllerRequest request, String service, String object, 
			String operation, String requestId, HashMap<String, Object> payload, String username) {
		
		Callable<JSONObject> callable = new Callable<JSONObject>() {
			String expAPIResponse = null;
			JSONObject responseObj = new JSONObject();
			
			@Override
			public JSONObject call() {
				try {
					expAPIResponse = DBPServiceExecutorBuilder.builder()
							.withServiceId(service)
							.withObjectId(object)
							.withDataControllerRequest(request)
							.withOperationId(operation)
							.withRequestParameters(payload)
							.build()
							.getResponse();
					responseObj  = new JSONObject(expAPIResponse);
					diagnostic.prepareDebug(" expAPIResponse " + expAPIResponse).log();
					
					if (responseObj != null 
							&& responseObj.has(UtilConstants.OPSTATUS) 
							&& responseObj.getInt(UtilConstants.OPSTATUS)==0
							&& !responseObj.has("dbpErrCode")) {
						responseObj.put(UtilConstants.STATUS, UtilConstants.SUCCESS);
					}else {
						responseObj.put(UtilConstants.STATUS, UtilConstants.FAILURE);
					}
					
					if(responseObj.getString(UtilConstants.STATUS).equalsIgnoreCase(UtilConstants.SUCCESS)) {
						updateApprovalRequestStatus(request, requestId, UtilConstants.SID_COMPLETED, expAPIResponse , UtilConstants.SID_PROCESSING, username);
					}
					else if(responseObj.getString(UtilConstants.STATUS).equalsIgnoreCase(UtilConstants.FAILURE)){
						updateApprovalRequestStatus(request, requestId, UtilConstants.SID_COMPLETED_WITH_ERRORS, expAPIResponse, UtilConstants.SID_PROCESSING, username);
					}
				} catch (Exception e) {
					diagnostic.prepareDebug("Caught exception while calling the experience api " + e.getMessage()).log();				
				}
				return responseObj;
			}
		};
		
		try {
			
			ThreadExecutor.execute(callable);
			diagnostic.prepareDebug("Async API triggered").log();
		} catch (InterruptedException e) {
			alert.prepareError("Caught exception while Executing Thread :", e.getMessage()).log();
			Thread.currentThread().interrupt();
		}
		return null;
	}
	
	public static JSONObject updateApprovalRequestStatus(DataControllerRequest request, String requestId, String status,
			String comments, String currentStatus, String checkedBy) {
		String response = null;
		JSONObject responseObj = new JSONObject();
		JSONObject resultObj = new JSONObject();
		HashMap<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put(UtilConstants._STATUS, status);
		inputMap.put(UtilConstants._REASON, comments);
		inputMap.put(UtilConstants._REQUESTID, requestId);
		inputMap.put(UtilConstants._CURRENTSTATUS, currentStatus);
		inputMap.put(UtilConstants.CHECKEDBY, checkedBy);

		try {
			response = DBPServiceExecutorBuilder.builder().withServiceId(UtilConstants.CRUDLAYER).withObjectId(null)
					.withDataControllerRequest(request).withOperationId(UtilConstants.DB_APPROVALREQUESTS_UPDATE_PROC)
					.withRequestParameters(inputMap).build().getResponse();
			responseObj = new JSONObject(response);
			diagnostic.prepareDebug(" dbxdb_update_approvalrequests_proc input : " + inputMap.toString()).log();
			diagnostic.prepareDebug(" dbxdb_update_approvalrequests_proc: " + response).log();
			if (responseObj != null && responseObj.has(UtilConstants.OPSTATUS)
					&& responseObj.getInt(UtilConstants.OPSTATUS) == 0) {
				if (responseObj.has(UtilConstants.RECORDS)
						&& responseObj.getJSONArray(UtilConstants.RECORDS).getJSONObject(0).getString("result").equals("1"))
					resultObj.put(UtilConstants.STATUS, UtilConstants.SUCCESS);
			} else {
				resultObj.put(UtilConstants.SUCCESS, UtilConstants.FAILURE);
			}
		} catch (Exception e) {
			alert.prepareError("Exception occured while updating approvalrequest table :", e.getMessage()).log();
		}

		return resultObj;
	}
	
	public static HashMap<String, String> getApprovalRequestDetails(DataControllerRequest request, String requestId) {
		String approvalRequestResponse = null;
		JSONObject responseObj = new JSONObject();
		HashMap<String, String> map = new HashMap<String,String>();
		HashMap<String, Object> inputMap = new HashMap<String,Object>();
		inputMap.put(UtilConstants.FILTER, "requestId eq '"+ requestId + "'");
		
		try {
			approvalRequestResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(UtilConstants.CRUDLAYER)
					.withObjectId(null)
					.withDataControllerRequest(request)
					.withOperationId(UtilConstants.DB_GET_REQUESTSHISTORY)
					.withRequestParameters(inputMap)
					.build()
					.getResponse();
			responseObj  = new JSONObject(approvalRequestResponse);
			diagnostic.prepareDebug(" approvalRequestResponse " + approvalRequestResponse).log();
			if (responseObj != null && responseObj.has(UtilConstants.APPROVAL_REQUESTS) && responseObj.getJSONArray(UtilConstants.APPROVAL_REQUESTS).length()>0) {
				JSONArray jsonArray = responseObj.optJSONArray(UtilConstants.APPROVAL_REQUESTS);
				JSONObject approvalRequest = jsonArray.getJSONObject(0);
				String expAPIOperationName = approvalRequest.getString(UtilConstants.EXPAPIOPNAME);
				String reqPayload = approvalRequest.getString(UtilConstants.REQ_PAYLOAD);
				String legalEntity = approvalRequest.getString(UtilConstants.COMPANY_LEGAL_UNIT);
				String permissionName = approvalRequest.getString(UtilConstants.PERMISSION_NAME);
				map.put(UtilConstants.EXPAPIOPNAME, expAPIOperationName);
				map.put(UtilConstants.REQ_PAYLOAD, reqPayload);
				map.put(UtilConstants.PERMISSION_NAME, permissionName);
				map.put(UtilConstants.COMPANY_LEGAL_UNIT, legalEntity);
				return map;
			}

		} catch (Exception e) {
			diagnostic.prepareDebug("Exception occured while trying to get data from approvalrequests." + e.getMessage()).log();
		}
		return map;
	}
	
	public static String calculateOffset(DataControllerRequest requestInstance) {
        diagnostic.prepareDebug("Calculating offset").log();
        String recordsPerPage = requestInstance.getParameter("pageSize");
        String currentPage = Objects.toString(requestInstance.getParameter("pageOffset"), "");
        try {
            int pageIndex = Integer.parseInt(currentPage);
            int offset = Integer.parseInt(recordsPerPage) * (pageIndex - 1);
            return String.valueOf(offset);
        } catch (NumberFormatException ex) {
            alert.prepareError("Exception In Calculating Offset. Exception:", ex).log();
            return String.valueOf(0);
        }
    }
}
