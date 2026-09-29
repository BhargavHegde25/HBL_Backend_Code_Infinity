package com.kony.dbpalerts.dbconnectionutils;

import java.util.List;
import java.util.Map;
import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbpalerts.alertsutils.AlertsDBServiceConstants;
import com.kony.dbpalerts.alertsutils.AlertsUtils;
import com.kony.dbpalerts.alertsutils.NotificationQueryClass;
import com.kony.dbpalerts.alertsutils.StaticDataHolder;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class ExecuteQueryWorkerJobs {
	private ExecuteQueryWorkerJobs() {

	}

	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	public static synchronized String insertToNotification(NotificationQueryClass tempnotifyquery) {
		String id = null;
		Map<String, Object> requestParameters = tempnotifyquery.getNotifyQuery();

		try {

			AlertsUtils.callInternalService(requestParameters, AlertsDBServiceConstants.EVENTDBDBSERVICE, AlertsUtils
					.replaceSchemaName(AlertsDBServiceConstants.NOTIFICATION_CREATE, StaticDataHolder.getSchemaname()),
					null);

		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured :", e).log();
		}
		Result responce = null;
		try {
			responce = AlertsUtils.callInternalService(requestParameters, AlertsDBServiceConstants.EVENTDBDBSERVICE,
					AlertsUtils.replaceSchemaName(AlertsDBServiceConstants.DBPALERTS_GETNOTIFICATIONID,
							StaticDataHolder.getSchemaname()),
					null);
		} catch (Exception e) {
			diagnostic.prepareDebug("Error Occured:", e).log();
		}
		if (responce != null) {
			Dataset ds = responce.getDatasetById("records");
			if (ds != null) {
				List<Record> records = ds.getAllRecords();
				if (records != null && !records.isEmpty()) {
					String lastid = records.get(0).getParamValueByName("lastid");
					if (lastid != null)
						return lastid;
				}
			}
		}
		return id;
	}

	public static void insertToUserNotificationAndAlertHistory(NotificationQueryClass tempnotifyquery, String id) {
		Map<String, Object> reqParams = tempnotifyquery.getUserNotifyquery();

		reqParams.put("notification_id", id);
		try {

			AlertsUtils.callInternalService(reqParams, AlertsDBServiceConstants.EVENTDBDBSERVICE, AlertsUtils.replaceSchemaName(
					AlertsDBServiceConstants.USERNOTIFICATION_CREATE, StaticDataHolder.getSchemaname()), null);
		} catch (Exception e) {
			diagnostic.prepareDebug("Error Occured :", e).log();
		}
		reqParams = tempnotifyquery.getAlertHistoryQuery();
		reqParams.put("ReferenceNumber", id);
		try {
			AlertsUtils.callInternalService(reqParams, AlertsDBServiceConstants.EVENTDBDBSERVICE, AlertsUtils.replaceSchemaName(
					AlertsDBServiceConstants.ALERTHISTORY_CREATE, StaticDataHolder.getSchemaname()), null);
		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured", e).log();
		}
	}

	public static void insertToAlertHistory(Map<String, Object> tempalerthisquery) {

		Map<String, Object> requestParameters = tempalerthisquery;
		try {

			AlertsUtils.callInternalService(requestParameters, AlertsDBServiceConstants.EVENTDBDBSERVICE, AlertsUtils
					.replaceSchemaName(AlertsDBServiceConstants.ALERTHISTORY_CREATE, StaticDataHolder.getSchemaname()),
					null);
		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured", e).log();
		}
	}
}
