package com.kony.eum.dbputilities.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutor;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.AdminUtil;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.eum.product.constants.OperationName;
import com.temenos.dbx.eum.product.constants.ServiceId;
import com.temenos.dbx.product.utils.ThreadExecutor;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class UnsubscribeAlertsForRemovedAccounts {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	public static void unsubscribeAlerts(HashMap<String, String> customerAccountMap) throws Exception {
		try {
			for (Entry<String, String> entry : customerAccountMap.entrySet()) {
				String customerId = entry.getKey();
				String accId = entry.getValue();
				List<String> alertReqIds = getAlertRequestIds(customerId, accId);
				for (String reqId : alertReqIds) {
					String status = unsubscribeT24(entry.getKey(), reqId);
					diagnostic.prepareDebug(
							"Status for customerid " + customerId + " and account id " + accId + " is " + status);
				}

				boolean isDbxCustomerAlertEntitlementDeleted = deleteDataFromDb(
						OperationName.DB_DBXCUSTOMERALERTENTITLEMENT_DELETE, customerId, accId);
				boolean isCustomerAlertChannelDeleted = deleteDataFromDb(OperationName.DB_CUSTOMERALERTCHANNEL_DELETE,
						customerId, accId);
				boolean isCustomerAlertSwitchDeleted = deleteDataFromDb(OperationName.DB_CUSTOMERALERTSWITCH_DELETE,
						customerId, accId);
				diagnostic.prepareDebug("Delete Status for customerid " + customerId + " and account id " + accId
						+ " is " + (isDbxCustomerAlertEntitlementDeleted && isCustomerAlertChannelDeleted
								&& isCustomerAlertSwitchDeleted));
			}
		} catch (Exception e) {
			alert.prepareError("error while unsubscribing alerts for removed accounts").log();
		}

	}

	private static String unsubscribeT24(String customerId, String reqId) {
		Map<String, Object> inputmap = new HashMap<>();
		inputmap.put("alertRequestId", reqId);
		inputmap.put("externalUserId", customerId);
		inputmap.put("subscribe", "NO");
		String status = null;
		Map<String, Object> headerMap = new HashMap<>();
		try {
			String result = DBPServiceExecutorBuilder.builder().withServiceId("T24Alerts").withObjectId(null)
					.withOperationId("update").withRequestParameters(inputmap)
					.build().getResponse();

			if (diagnostic.isDebugEnabled()) {
				diagnostic.prepareDebug(result.toString()).log();
			}

			JSONObject responseObj = new JSONObject(result);
			JSONObject header = responseObj.optJSONObject("header");

			status = header.optString("status");

			if (!status.equalsIgnoreCase("success")) {
				alert.prepareError("error msg : " + responseObj.optString("errorDetailsMessage")).log();
				alert.prepareError("error code : " + responseObj.optString("errorDetailsCode")).log();
			}
		} catch (Exception e) {
			alert.prepareError("error in unsubscribe , configuring only internal").log();
		}

		return status;
	}

	public static List<String> getAlertRequestIds(String customerId, String accountId) {

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_DBXCUSTOMERALERTENTITLEMENT_GET;
		List<String> reqIds = new ArrayList<>();

		String dbxCustomerAlertEntitlementResponse = null;
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("$filter", "Customer_id eq " + customerId + " and AccountId eq " + accountId);
		params.put("$select", "alertRequestId");

		try {
			dbxCustomerAlertEntitlementResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName)
					.withObjectId(null).withOperationId(operationName).withRequestParameters(params).build()
					.getResponse();
			JSONObject responseObj = new JSONObject(dbxCustomerAlertEntitlementResponse);
			JSONArray jsonArray = responseObj.optJSONArray("dbxcustomeralertentitlement");
			for (int i = 0; i < jsonArray.length(); i++) {
				String reqId = jsonArray.getJSONObject(i).getString("alertRequestId");
				reqIds.add(reqId);
			}

		} catch (JSONException e) {
			alert.prepareError("Failed to fetch request ids from table: " + e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at getAlertRequestIds: " + e).log();
			return null;
		}

		return reqIds;
	}

	public static boolean deleteDataFromDb(String operation, String customerId, String accountId) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;

		Map<String, Object> requestParameters = new HashMap<String, Object>();
		if (operation.equals(OperationName.DB_DBXCUSTOMERALERTENTITLEMENT_DELETE)) {
			requestParameters.put("$filter", "Customer_id eq " + customerId + " and AccountId eq " + accountId);
		} else if (operation.equals(OperationName.DB_CUSTOMERALERTCHANNEL_DELETE)) {
			requestParameters.put("$filter", "customerId eq " + customerId + " and accountId eq " + accountId);
		} else if (operation.equals(OperationName.DB_CUSTOMERALERTSWITCH_DELETE)) {
			requestParameters.put("$filter", "Customer_id eq " + customerId + " and AccountID eq " + accountId);
		}
		String deleteResponse = null;
		try {
			deleteResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operation).withRequestParameters(requestParameters).build().getResponse();

			JSONObject jsonRsponse = new JSONObject(deleteResponse);
			if (jsonRsponse.getInt("opstatus") == 0 && jsonRsponse.getInt("httpStatusCode") == 0
					&& jsonRsponse.getInt("deletedRecords") > 0) {
				return true;
			}
		} catch (JSONException e) {
			alert.prepareError("Failed to delete data : " + e.getMessage()).log();
			return false;
		} catch (Exception e) {
			alert.prepareError("Caught exception at delete action : " + e).log();
			return false;
		}

		return false;
	}

	public static void asyncUnsubscribeAlerts(HashMap<String, String> customerAccountMap) {
		Callable<Result> callable = new Callable<Result>() {
			@Override
			public Result call() {
				try {
					unsubscribeAlerts(customerAccountMap);
				} catch (Exception e) {
					alert.prepareError(e.getMessage()).log();
				}
				return new Result();
			}
		};
		try {
			ThreadExecutor.getExecutor().execute(callable);

		} catch (InterruptedException e) {
			alert.prepareError("Caught exception while Executing Thread :", e.getMessage()).log();
			Thread.currentThread().interrupt();
		}
	}
}
