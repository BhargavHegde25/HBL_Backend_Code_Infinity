package com.kony.adminconsole.utilities;

import java.io.IOException;
import java.util.*;

import com.kony.adminconsole.dto.Permission;
import com.kony.adminconsole.handler.PermissionHandler;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONObject;

import com.kony.adminconsole.commons.handler.EnvironmentConfigurationsHandler;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;

public class ApprovalUtils {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");

	/*
	 * Make a call to update approval request workflow to update record Id after
	 * success response
	 */
	public static void updateapprovalrequest(String requestId, String recordId, DataControllerRequest requestInstance) {
		if (StringUtils.isBlank(requestId) || StringUtils.isBlank(recordId)) {
			return;
		}

		Map<String, String> inputMap = new HashMap<>();
		inputMap.put(ApprovalConstants.REQUEST_ID, requestId);
		inputMap.put(ApprovalConstants.RECORD_ID, recordId);
		inputMap.put(ApprovalConstants.CONTEXT, ApprovalConstants.CONTEXT_SUCCESS);
		String result = Executor.invokeService(ServiceURLEnum.UPDATE_APPROVAL_REQUEST_WORKFLOW, inputMap, null,
				requestInstance);
		alert.prepareError("Response from update:" + result).log();
	}

	/*
	 * Make a call to delete approval request workflow to delete record Id after
	 * failure response
	 */
	public static void deleteapprovalrequest(String requestId, DataControllerRequest requestInstance) {
		if (StringUtils.isBlank(requestId)) {
			return;
		}

		Map<String, String> inputMap = new HashMap<>();
		inputMap.put(ApprovalConstants.REQUEST_ID, requestId);
		inputMap.put(ApprovalConstants.CONTEXT, ApprovalConstants.CONTEXT_FAILURE);
		Executor.invokeService(ServiceURLEnum.DELETE_APPROVAL_REQUEST_WORKFLOW, inputMap, null, requestInstance);
	}

	/*
	 * Make a call to workflow to fetch whether approval for this feature is
	 * required or not Whether any pending requests already exists And insert data
	 * into approvalrequest table when approval is required
	 */
	public static JSONObject getApprovalDetails(String recordId, String module, String feature, String userId,
			String context, DataControllerRequest requestInstance) {
		// Checking whether maker checker is applicable or not
		String makerCheckerEnabled = EnvironmentConfigurationsHandler
				.getServerAppPropertyValue(ApprovalConstants.IS_MAKER_CHECKER_AVAILABLE, requestInstance);
		Boolean isApprovalRequired = StringUtils.isNotBlank(makerCheckerEnabled)
				&& "true".equalsIgnoreCase(makerCheckerEnabled) ? true : false;
		JSONObject serviceResponseJSON = new JSONObject();
		if (isApprovalRequired) {
			if (StringUtils.isBlank(module)) {
				return new JSONObject();
			}

			Map<String, String> inputMap = new HashMap<>();
			inputMap.put(ApprovalConstants.FEATURE, feature);
			inputMap.put(ApprovalConstants.MODULE, module);
			inputMap.put(ApprovalConstants.CONTEXT, context);
			inputMap.put(ApprovalConstants.RECORD_ID, recordId);

			if (StringUtils.isBlank(userId)) {
				UserDetailsBean userDetailsBeanInstance;
				try {
					userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(requestInstance);
					if (userDetailsBeanInstance != null) {
						userId = userDetailsBeanInstance.getUserId();
					}
				} catch (ApplicationException e) {
					alert.prepareError("Error occurred: ", e).log();				}
			}

			inputMap.put(ApprovalConstants.CREATEDBY, userId);
			inputMap.put(ApprovalConstants.USER, userId);

			String operationName = fetchExperienceAPIName(requestInstance);
			operationName = operationName.replace(" ", "_");
			inputMap.put(ApprovalConstants.EXPAPIOPERATIONNAME, operationName);
			inputMap.put(ApprovalConstants.OPERATIONNAME, operationName);

			/*
			 * String serviceResponse =
			 * Executor.invokeService(ServiceURLEnum.CHECK_APPROVAL_REQUIRED, inputMap,
			 * null, requestInstance);
			 */
			String serviceResponse = Executor.invokeService(ServiceURLEnum.CHECK_APPROVAL_REQUIRED_WORKFLOW, inputMap,
					null, requestInstance);
			serviceResponseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);
		} else {
			serviceResponseJSON.put(ApprovalConstants.IS_APPROVAL_REQUIRED, ApprovalConstants.FALSE);
		}
		return serviceResponseJSON;
	}

	/*
	 * To fetch experience API name
	 */
	public static String fetchExperienceAPIName(DataControllerRequest requestInstance) {
		StringBuilder sb = new StringBuilder();
		sb.append(requestInstance.getParameter(ApprovalConstants.APPID));
		sb.append("_");
		sb.append(requestInstance.getParameter(ApprovalConstants.OBJECTID));
		sb.append("_");
		sb.append(requestInstance.getParameter(ApprovalConstants.OPERATIONID));
		return sb.toString();
	}

	/**
	 * @description to fetch the filter parameters passed for the List View API's
	 * @param {Map<String, Object>} inputMap - from input array
	 * @return {Map<String, Object>} filterParams
	 */
	public static Map<String, Object> getFilterParams(Map<String, Object> inputMap){
		// filter params sample structure:
		// 	{
		//      "module" : "",    // pass as CSV string - eg "Customer Management,Employee Management"
		//      "feature" : "",   // pass as CSV string - eg "Role,User,Permission"
		//      "action" : "",    // pass as CSV string - eg "Create Role,Manage Role"
		//      "searchStartDate" : "ASC/DESC"
		//      "searchEndDate" : "ASC/DESC"
		//      "sortOrder" : "ASC/DESC"
		//      "sortParam" : "STATUS/APPROVED_DATE"
		//      "pageSize" : 15    // pass integer
		//      "pageOffset" : 0 // pass integer - 0 indexed
		//  }
		Map<String, Object> filterParams = new HashMap<>();
		try{
			if(inputMap.containsKey(ApprovalConstants.MODULE_FILTER) && !org.apache.commons.lang.StringUtils.isBlank((String) inputMap.get(ApprovalConstants.MODULE_FILTER))){
				filterParams.put(
						ApprovalConstants.MODULE_FILTER,
						new HashSet<>(Arrays.asList(((String) inputMap.get(ApprovalConstants.MODULE_FILTER)).split("\\s*,\\s*")))
				);
			}
			if(inputMap.containsKey(ApprovalConstants.FEATURE_FILTER) && !org.apache.commons.lang.StringUtils.isBlank((String) inputMap.get(ApprovalConstants.FEATURE_FILTER))){
				filterParams.put(
						ApprovalConstants.FEATURE_FILTER,
						new HashSet<>(Arrays.asList(((String) inputMap.get(ApprovalConstants.FEATURE_FILTER)).split("\\s*,\\s*")))
				);
			}
			if(inputMap.containsKey(ApprovalConstants.ACTION_FILTER) && !org.apache.commons.lang.StringUtils.isBlank((String) inputMap.get(ApprovalConstants.ACTION_FILTER))){
				filterParams.put(
						ApprovalConstants.ACTION_FILTER,
						new HashSet<>(Arrays.asList(((String) inputMap.get(ApprovalConstants.ACTION_FILTER)).split("\\s*,\\s*")))
				);
			}
			if(inputMap.containsKey(ApprovalConstants.START_DATE_FILTER) && !org.apache.commons.lang.StringUtils.isBlank((String) inputMap.get(ApprovalConstants.START_DATE_FILTER))){
				filterParams.put(ApprovalConstants.START_DATE_FILTER, inputMap.get(ApprovalConstants.START_DATE_FILTER));
			}
			if(inputMap.containsKey(ApprovalConstants.END_DATE_FILTER) && !org.apache.commons.lang.StringUtils.isBlank((String) inputMap.get(ApprovalConstants.END_DATE_FILTER))){
				filterParams.put(ApprovalConstants.END_DATE_FILTER, inputMap.get(ApprovalConstants.END_DATE_FILTER));
			}
			if(inputMap.containsKey(ApprovalConstants.SORT_ORDER_FILTER) && !org.apache.commons.lang.StringUtils.isBlank((String) inputMap.get(ApprovalConstants.SORT_ORDER_FILTER))){
				filterParams.put(ApprovalConstants.SORT_ORDER_FILTER, inputMap.get(ApprovalConstants.SORT_ORDER_FILTER));
			}
			if(inputMap.containsKey(ApprovalConstants.SORT_PARAM_FILTER) && !org.apache.commons.lang.StringUtils.isBlank((String) inputMap.get(ApprovalConstants.SORT_PARAM_FILTER))){
				filterParams.put(ApprovalConstants.SORT_PARAM_FILTER, inputMap.get(ApprovalConstants.SORT_PARAM_FILTER));
			}
			if(inputMap.containsKey(ApprovalConstants.PAGE_SIZE_FILTER) && !org.apache.commons.lang.StringUtils.isBlank((String) inputMap.get(ApprovalConstants.PAGE_SIZE_FILTER))){
				filterParams.put(ApprovalConstants.PAGE_SIZE_FILTER, Integer.parseInt((String) inputMap.get(ApprovalConstants.PAGE_SIZE_FILTER)));
			}
			if(inputMap.containsKey(ApprovalConstants.PAGE_OFFSET_FILTER) && !org.apache.commons.lang.StringUtils.isBlank((String) inputMap.get(ApprovalConstants.PAGE_OFFSET_FILTER))){
				filterParams.put(ApprovalConstants.PAGE_OFFSET_FILTER, Integer.parseInt((String) inputMap.get(ApprovalConstants.PAGE_OFFSET_FILTER)));
			}
		} catch (Exception e){
			alert.prepareError("Error parsing filter parameters: " + e).log();
		}
		return filterParams;
	}

	/**
	 * @description to fetch the filter parameters passed for the List View API's
	 * @param {DataControllerRequest} dcRequest - the http request abstraction object
	 * @return {Map<String, Object>} filterParams
	 */
	public static Map<String, Object> getFilterParams(DataControllerRequest dcRequest){
		// filter params sample structure:
		// 	{
		//      "module" : "",    // pass as CSV string - eg "Customer Management,Employee Management"
		//      "feature" : "",   // pass as CSV string - eg "Role,User,Permission"
		//      "action" : "",    // pass as CSV string - eg "Create Role,Manage Role"
		//      "searchStartDate" : "ASC/DESC"
		//      "searchEndDate" : "ASC/DESC"
		//      "sortOrder" : "ASC/DESC"
		//      "sortParam" : "STATUS/APPROVED_DATE"
		//      "pageSize" : 15    // pass integer
		//      "pageOffset" : 0 // pass integer - 0 indexed
		//  }
		Map<String, Object> filterParams = new HashMap<>();
		try{
			if(dcRequest.containsKeyInRequest(ApprovalConstants.MODULE_FILTER) && !org.apache.commons.lang.StringUtils.isBlank(dcRequest.getParameter(ApprovalConstants.MODULE_FILTER))){
				filterParams.put(
						ApprovalConstants.MODULE_FILTER,
						new HashSet<>(Arrays.asList(dcRequest.getParameter(ApprovalConstants.MODULE_FILTER).split("\\s*,\\s*")))
				);
			}
			if(dcRequest.containsKeyInRequest(ApprovalConstants.FEATURE_FILTER) && !org.apache.commons.lang.StringUtils.isBlank(dcRequest.getParameter(ApprovalConstants.FEATURE_FILTER))){
				filterParams.put(
						ApprovalConstants.FEATURE_FILTER,
						new HashSet<>(Arrays.asList(dcRequest.getParameter(ApprovalConstants.FEATURE_FILTER).split("\\s*,\\s*")))
				);
			}
			if(dcRequest.containsKeyInRequest(ApprovalConstants.ACTION_FILTER) && !org.apache.commons.lang.StringUtils.isBlank(dcRequest.getParameter(ApprovalConstants.ACTION_FILTER))){
				filterParams.put(
						ApprovalConstants.ACTION_FILTER,
						new HashSet<>(Arrays.asList(dcRequest.getParameter(ApprovalConstants.ACTION_FILTER).split("\\s*,\\s*")))
				);
			}
			if(dcRequest.containsKeyInRequest(ApprovalConstants.START_DATE_FILTER) && !org.apache.commons.lang.StringUtils.isBlank(dcRequest.getParameter(ApprovalConstants.START_DATE_FILTER))){
				filterParams.put(ApprovalConstants.START_DATE_FILTER, dcRequest.getParameter(ApprovalConstants.START_DATE_FILTER));
			}
			if(dcRequest.containsKeyInRequest(ApprovalConstants.END_DATE_FILTER) && !org.apache.commons.lang.StringUtils.isBlank(dcRequest.getParameter(ApprovalConstants.END_DATE_FILTER))){
				filterParams.put(ApprovalConstants.END_DATE_FILTER, dcRequest.getParameter(ApprovalConstants.END_DATE_FILTER));
			}
			if(dcRequest.containsKeyInRequest(ApprovalConstants.SORT_ORDER_FILTER) && !org.apache.commons.lang.StringUtils.isBlank(dcRequest.getParameter(ApprovalConstants.SORT_ORDER_FILTER))){
				filterParams.put(ApprovalConstants.SORT_ORDER_FILTER, dcRequest.getParameter(ApprovalConstants.SORT_ORDER_FILTER));
			}
			if(dcRequest.containsKeyInRequest(ApprovalConstants.SORT_PARAM_FILTER) && !org.apache.commons.lang.StringUtils.isBlank(dcRequest.getParameter(ApprovalConstants.SORT_PARAM_FILTER))){
				filterParams.put(ApprovalConstants.SORT_PARAM_FILTER, dcRequest.getParameter(ApprovalConstants.SORT_PARAM_FILTER));
			}
			if(dcRequest.containsKeyInRequest(ApprovalConstants.PAGE_SIZE_FILTER) && !org.apache.commons.lang.StringUtils.isBlank(dcRequest.getParameter(ApprovalConstants.PAGE_SIZE_FILTER))){
				filterParams.put(ApprovalConstants.PAGE_SIZE_FILTER, Integer.parseInt(dcRequest.getParameter(ApprovalConstants.PAGE_SIZE_FILTER)));
			}
			if(dcRequest.containsKeyInRequest(ApprovalConstants.PAGE_OFFSET_FILTER) && !org.apache.commons.lang.StringUtils.isBlank(dcRequest.getParameter(ApprovalConstants.PAGE_OFFSET_FILTER))){
				filterParams.put(ApprovalConstants.PAGE_OFFSET_FILTER, Integer.parseInt(dcRequest.getParameter(ApprovalConstants.PAGE_OFFSET_FILTER)));
			}
		} catch(Exception e){
			alert.prepareError("Error parsing filter parameters: " + e).log();
		}
		return filterParams;
	}

	public static Set<String> getUserPermissionsWhenKeyCloakIsEnabled(DataControllerRequest requestInstance, String roleIds) {
		Set<String> permissionSet = new HashSet<String>();
		if (StringUtils.isNotBlank(roleIds)) {
			List<Permission> permissionsList;
			try {
				permissionsList = PermissionHandler.getRolesGrantedPermissions(roleIds, requestInstance);
				if (permissionsList != null && !permissionsList.isEmpty()) {
					for (Permission permission : permissionsList) {
						permissionSet.add(permission.getName());
					}
				}
			} catch (ApplicationException ae) {
				alert.prepareError("Application Exception Occured while fetching permissions based on roleId: " + ae).log();
			} catch (IOException ioe) {
				alert.prepareError("IO Exception Occured while fetching permissions based on roleId: " + ioe).log();
			}
		}
		return permissionSet;
	}
}
