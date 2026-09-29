package com.kony.adminconsole.handler;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class ReportManagementHandler {

	public static void removeUsers(JSONArray removedCustomers, String reportId, String authToken,
			DataControllerRequest requestInstance, Result result) {
		Param result_param = new Param();
		Map<String, String> postParametersMap = new HashMap<String, String>();
		postParametersMap.clear();
		for (int i = 0; i < removedCustomers.length(); i++) {

			String userId = ((JSONObject) removedCustomers.get(i)).getString("id");
			postParametersMap.put("reportId", reportId);
			postParametersMap.put("userId", userId);
			String deleteGroupCustomersResponse = Executor.invokeService(ServiceURLEnum.SHAREDREPORT_DELETE,
					postParametersMap, null, requestInstance);
			JSONObject deleteGroupCustomersResponseJSON = CommonUtilities
					.getStringAsJSONObject(deleteGroupCustomersResponse);
			if (deleteGroupCustomersResponseJSON != null
					&& deleteGroupCustomersResponseJSON.has(FabricConstants.OPSTATUS)
					&& deleteGroupCustomersResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
				continue;
			}
			ErrorCodeEnum.ERR_21868.setErrorCode(result);
			return;
		}
		result_param = new Param("Status", "Success", FabricConstants.STRING);
		result.addParam(result_param);
		return;

	}

	public static void createUsers(JSONArray addedCustomers, String reportId, String authToken,
			DataControllerRequest requestInstance, Result result) {
		
		Param result_param = new Param();

		Map<String, String> postParametersMap = new HashMap<String, String>();
		for (int i = 0; i < addedCustomers.length(); i++) {
			postParametersMap.clear();
			postParametersMap.put("reportId", reportId);
			postParametersMap.put("userId", ((JSONObject) addedCustomers.get(i)).getString("id"));
			String createSharedReportResponse = Executor.invokeService(ServiceURLEnum.SHAREDREPORT_CREATE,
					postParametersMap, null, requestInstance);
			JSONObject createSharedReportResponseJSON = CommonUtilities
					.getStringAsJSONObject(createSharedReportResponse);
			if (createSharedReportResponseJSON != null && createSharedReportResponseJSON.has(FabricConstants.OPSTATUS)
					&& createSharedReportResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
				result_param = new Param("Status", "Success", FabricConstants.STRING);
				result.addParam(result_param);
			} else {
				ErrorCodeEnum.ERR_21868.setErrorCode(result);
			}
		}
		return;

	}

	public static void addRoles(JSONArray addedRoles, String reportId, String authToken,
			DataControllerRequest requestInstance, Result result) {		
		Param result_param = new Param();

		Map<String, String> postParametersMap = new HashMap<String, String>();
		for (int i = 0; i < addedRoles.length(); i++) {
			postParametersMap.clear();
			postParametersMap.put("reportId", reportId);
			postParametersMap.put("roleId", ((JSONObject) addedRoles.get(i)).getString("id"));
			String createSharedReportResponse = Executor.invokeService(ServiceURLEnum.SHAREDREPORT_CREATE,
					postParametersMap, null, requestInstance);
			JSONObject createSharedReportResponseJSON = CommonUtilities
					.getStringAsJSONObject(createSharedReportResponse);
			if (createSharedReportResponseJSON != null && createSharedReportResponseJSON.has(FabricConstants.OPSTATUS)
					&& createSharedReportResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
				result_param = new Param("Status", "Success", FabricConstants.STRING);
				result.addParam(result_param);
			} else {
				ErrorCodeEnum.ERR_21869.setErrorCode(result);
			}
		}
		return;

	}

	public static void removeRoles(JSONArray removedRoles, String reportId, String authToken,
			DataControllerRequest requestInstance, Result result) {
		// TODO Auto-generated method stub		
		Param result_param = new Param();

		Map<String, String> postParametersMap = new HashMap<String, String>();
		postParametersMap.clear();
		for (int i = 0; i < removedRoles.length(); i++) {

			String userId = ((JSONObject) removedRoles.get(i)).getString("id");
			postParametersMap.put("reportId", reportId);
			postParametersMap.put("roleId", userId);
			String deleteGroupCustomersResponse = Executor.invokeService(ServiceURLEnum.SHAREDREPORT_DELETE,
					postParametersMap, null, requestInstance);
			JSONObject deleteGroupCustomersResponseJSON = CommonUtilities
					.getStringAsJSONObject(deleteGroupCustomersResponse);
			if (deleteGroupCustomersResponseJSON != null
					&& deleteGroupCustomersResponseJSON.has(FabricConstants.OPSTATUS)
					&& deleteGroupCustomersResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
				continue;
			}
			ErrorCodeEnum.ERR_21869.setErrorCode(result);
			return;
		}
		result_param = new Param("Status", "Success", FabricConstants.STRING);
		result.addParam(result_param);
		return;
	}

}
