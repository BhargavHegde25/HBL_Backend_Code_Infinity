package com.kony.kmsinvoke.businessdelegate.impl;

import java.util.Calendar;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.kmsinvoke.businessdelegate.api.NotificationBusinessDelegate;
import com.kony.kmsinvoke.util.HelperMethods;
import com.kony.kmsinvoke.util.KmsInvokeConstants;
import com.kony.kmsinvoke.util.StaticDataHolder;

public class NotificationBusinessDelegateImpl implements NotificationBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	@SuppressWarnings("unchecked")
	@Override
	public JsonObject sendNotification(Map<String, Object> inputparams) {
		String id = null;
		if (inputparams != null) {
			if (inputparams.get(KmsInvokeConstants.NOTIFYQUERY) != null
					&& inputparams.get(KmsInvokeConstants.USERNOTIFYQUERY) != null
					&& inputparams.get(KmsInvokeConstants.ALERTHISTORYQUERY) != null) {
				Map<String, Object> notifyQuery = (Map<String, Object>) inputparams.get(KmsInvokeConstants.NOTIFYQUERY);
				Map<String, Object> userNotifyQuery = (Map<String, Object>) inputparams
						.get(KmsInvokeConstants.USERNOTIFYQUERY);
				Map<String, Object> alertHistoryQuery = (Map<String, Object>) inputparams
						.get(KmsInvokeConstants.ALERTHISTORYQUERY);
				replaceTimestamp(notifyQuery, userNotifyQuery, alertHistoryQuery);
				id = insertToNotification(notifyQuery);
				insertToUserNotificationAndAlertHistory(userNotifyQuery, alertHistoryQuery, id);
			} else if (inputparams.get(KmsInvokeConstants.ALERTHISTORYQUERY) != null) {
				insertToAlertHistory(inputparams);
			}
		}
		JsonObject result = HelperMethods.returnResultJSonObject(true, null);
		if (id != null)
			result.addProperty("referenceId", id);
		return result;
	}

	private void replaceTimestamp(Map<String, Object> notifyQuery, Map<String, Object> userNotifyQuery,
			Map<String, Object> alertHistoryQuery) {
		String[] timestampKeys = { KmsInvokeConstants.RECEIVEDDATE, KmsInvokeConstants.CREATEDTS,
				KmsInvokeConstants.DISPATCHDATE, KmsInvokeConstants.LASTMODIFIEDTS, KmsInvokeConstants.SYNCTIMESTAMP };
		Calendar calendar = Calendar.getInstance();
		java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());
		for (String key : timestampKeys) {
			if (notifyQuery != null && notifyQuery.containsKey(key))
				notifyQuery.put(key, ourJavaTimestampObject);
			if (userNotifyQuery != null && userNotifyQuery.containsKey(key))
				userNotifyQuery.put(key, ourJavaTimestampObject);
			if (alertHistoryQuery != null && alertHistoryQuery.containsKey(key))
				alertHistoryQuery.put(key, ourJavaTimestampObject);
		}

	}

	private static void insertToAlertHistory(Map<String, Object> inputparams) {
		@SuppressWarnings("unchecked")
		Map<String, Object> requestParameters = (Map<String, Object>) inputparams
				.get(KmsInvokeConstants.ALERTHISTORYQUERY);
		try {

			DBPServiceExecutorBuilder.builder().withOperationId(KmsInvokeConstants.ALERTHISTORY_CREATE)
					.withRequestParameters(requestParameters).withServiceId(KmsInvokeConstants.EVENTDBDBSERVICE).build()
					.getResponse();
		} catch (Exception e) {
			alert.prepareError("Error occured", e).log();
		}

	}

	private static void insertToUserNotificationAndAlertHistory(Map<String, Object> userNotifyQuery,
			Map<String, Object> alertHistoryQuery, String id) {

		userNotifyQuery.put("notification_id", id);
		try {
			DBPServiceExecutorBuilder.builder()
					.withOperationId(HelperMethods.replaceSchemaName(KmsInvokeConstants.USERNOTIFICATION_CREATE,
							StaticDataHolder.getSchemaname()))
					.withRequestParameters(userNotifyQuery).withServiceId(KmsInvokeConstants.EVENTDBDBSERVICE).build()
					.getResponse();

		} catch (Exception e) {
			alert.prepareError("Error Occured :", e).log();
		}
		alertHistoryQuery.put("ReferenceNumber", id);
		try {
			DBPServiceExecutorBuilder.builder()
					.withOperationId(HelperMethods.replaceSchemaName(KmsInvokeConstants.ALERTHISTORY_CREATE,
							StaticDataHolder.getSchemaname()))
					.withRequestParameters(alertHistoryQuery).withServiceId(KmsInvokeConstants.EVENTDBDBSERVICE).build()
					.getResponse();
		} catch (Exception e) {
			alert.prepareError("Error occured", e).log();
		}

	}

	private static String insertToNotification(Map<String, Object> notifyQuery) {
		String id = null;
		String res = null;
		try {
			res = DBPServiceExecutorBuilder.builder()
					.withOperationId(HelperMethods.replaceSchemaName(KmsInvokeConstants.NOTIFICATION_CREATE,
							StaticDataHolder.getSchemaname()))
					.withRequestParameters(notifyQuery).withServiceId(KmsInvokeConstants.EVENTDBDBSERVICE).build()
					.getResponse();
			diagnostic.prepareDebug("response " + res).log();
			id = new JsonParser().parse(res).getAsJsonObject().get("notification").getAsJsonArray().get(0)
					.getAsJsonObject().get("notificationId").getAsString();
		} catch (Exception e) {
			alert.prepareError("Error occured :", e).log();
		}
		return id;
	}

}
