package com.kony.dbpalerts.alertsprocess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbpalerts.alertsutils.AlertConstants;
import com.kony.dbpalerts.alertsutils.AlertsErrorMessages;
import com.kony.dbpalerts.alertsutils.Event;
import com.kony.dbpalerts.alertsutils.StaticDataHolder;
import com.kony.dbpalerts.alertsutils.AlertsUtils;
import com.kony.dbpalerts.dbconnectionutils.ExecuteQueryWorkerJobs;
import com.kony.dbpalerts.kmsapi.KmsInvoke;

public class SendAlertAndLog {
	private SendAlertAndLog() {

	}

	private static final String ALERTHISTORYFEILDS = "id,EventId,AlertSubTypeId,AlertTypeId,AlertCategoryId,AlertStatusId,Customer_Id,coreCustomerId,LanguageCode,ChannelId,Status,Subject,Message,SenderName,SenderEmail,ReferenceNumber,DispatchDate,ErrorMessage,createdby,modifiedby,createdts,lastmodifiedts,synctimestamp,softdeleteflag";
	private static String[] alerthhistory = ALERTHISTORYFEILDS.split(",");

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	JsonObject mailresponce = null;
	JsonObject smsresponce = null;
	JsonObject pushresponce = null;
	JsonObject notifyresponce = null;

	public static void pushAlertAndUpdateLog(Event event, Map<String, JsonObject> commtemplate,
			boolean isalertneedstobepushed) {
		try {
			if (!isalertneedstobepushed || event.getRecipientErrMsg() != null) {
				String errmsg = !isalertneedstobepushed ? AlertsErrorMessages.ERRMSG6 : null;
				if (event.getRecipientErrMsg() != null) {
					errmsg = errmsg != null ? errmsg + " " + event.getRecipientErrMsg() : event.getRecipientErrMsg();
				}
				if (event.getOtherErrorMessage() == null) {
					event.setNotificationmessage(errmsg);
					event.setPushmessage(errmsg);
					event.setSmsmessage(errmsg);
					event.setMailmessage(errmsg);
				}
				isalertneedstobepushed = false;
				addAlertLog(event, commtemplate, isalertneedstobepushed);
				return;
			}
			String[] channelarray = { AlertConstants.CH_SMS, AlertConstants.CH_PUSH_NOTIFICATION,
					AlertConstants.CH_NOTIFICATION_CENTER, AlertConstants.CH_EMAIL };
			for (int i = 0; i < channelarray.length; i++) {
				KmsInvoke.processChannels(channelarray[i], event, commtemplate);
			}
			addAlertLog(event, commtemplate, isalertneedstobepushed);

		} catch (Exception e) {
			alert.prepareError("Error occured", e).log();
		}
	}

	private static Map<String, Object> addEmailToQueue(Map<String, JsonObject> commtemplate,
			boolean isalertneedstobepushed, Event event) {
		Map<String, Object> map = new HashMap<>();
		String errmessage = event.getMailmessage();
		if (!event.getChemail()) {
			isalertneedstobepushed = false;
			errmessage = errmessage != null ? errmessage + " " + AlertsErrorMessages.ERRMSG7
					: AlertsErrorMessages.ERRMSG7;
		}
		if (event.getEmail() == null) {
			isalertneedstobepushed = false;
			errmessage = errmessage != null ? errmessage + " " + AlertsErrorMessages.ERRMSG8
					: AlertsErrorMessages.ERRMSG8;
		}
		String channel = "";
		String refid = "";
		try {
			channel = AlertConstants.CH_EMAIL;
			System.out.println("--emailQueue--"+channel);
			refid = event.getMailrefid();
			map = generateQueryMap(event, channel, refid, errmessage, commtemplate, isalertneedstobepushed);
			if (map != null && !map.isEmpty()) {
				ExecuteQueryWorkerJobs.insertToAlertHistory(map);
				addNameParams(map, event);
			}
		} catch (Exception e) {
			alert.prepareError("ExceptionOccured ", e).log();
		}
		return map;
	}

	private static void addNameParams(Map<String, Object> map, Event event) {
		map.put(AlertConstants.ALERTCATEGORYNAME, event.getAlertCategoryName());
		map.put(AlertConstants.ALERTGROUPNAME, event.getAlertGroupName());
		map.put(AlertConstants.ALERTNAME, event.getAlertName());

	}

	private static Map<String, Object> addSMSToQueue(Map<String, JsonObject> commtemplate,
			boolean isalertneedstobepushed, Event event) {
		Map<String, Object> map = new HashMap<>();
		String errmessage = event.getSmsmessage();
		if (!event.getChsms()) {
			isalertneedstobepushed = false;
			errmessage = errmessage != null ? errmessage + " " + AlertsErrorMessages.ERRMSG7
					: AlertsErrorMessages.ERRMSG7;
		}
		if (event.getPhone() == null) {
			isalertneedstobepushed = false;
			errmessage = errmessage != null ? errmessage + " " + AlertsErrorMessages.ERRMSG8
					: AlertsErrorMessages.ERRMSG8;
		}
		String channel = "";
		String refid = "";
		try {
			channel = AlertConstants.CH_SMS;
			System.out.println("--smsQueue--"+channel);
			refid = event.getSmsrefid();
			map = generateQueryMap(event, channel, refid, errmessage, commtemplate, isalertneedstobepushed);
			if (map != null && !map.isEmpty()) {
				ExecuteQueryWorkerJobs.insertToAlertHistory(map);
				addNameParams(map, event);
			}
		} catch (Exception e) {
			alert.prepareError("Exception Occured : ", e).log();
		}
		return map;

	}

	private static Map<String, Object> addNotificationToQueue(Map<String, JsonObject> commtemplate,
			boolean isalertneedstobepushed, Event event) {
		Map<String, Object> map = new HashMap<>();
		String errmessage = event.getNotificationmessage();
		if (!event.getChnotification()) {
			isalertneedstobepushed = false;
			errmessage = errmessage != null ? errmessage + " " + AlertsErrorMessages.ERRMSG7
					: AlertsErrorMessages.ERRMSG7;
		}
		if (event.getAppid() == null) {
			isalertneedstobepushed = false;
			errmessage = errmessage != null ? errmessage + " " + AlertsErrorMessages.ERRMSG9
					: AlertsErrorMessages.ERRMSG9;
		}
		String channel = "";
		try {
			channel = AlertConstants.CH_NOTIFICATION_CENTER;
			map = generateQueryMap(event, channel, event.getNotificationrefid(), errmessage, commtemplate,
					isalertneedstobepushed);
			if (map != null && !map.isEmpty()) {
				Map<String, Object> inputmap = new HashMap<>();
				Map<String, Object> inputparams = new HashMap<>();
				if (event.getNotificationobj2() != null && isalertneedstobepushed) {
					inputmap.put(AlertConstants.NOTIFYQUERY, event.getNotificationobj2().getNotifyQuery());
					inputmap.put(AlertConstants.USERNOTIFYQUERY, event.getNotificationobj2().getUserNotifyquery());
					inputmap.put(AlertConstants.ALERTHISTORYQUERY, map);
					inputparams.put(AlertConstants.INPUTPARAMS, JSONUtils.stringify(inputmap));
					String responseString = AlertsUtils.callInternalServiceAndGetJson(inputparams,
							AlertConstants.KMSINVOKESERVICE, AlertConstants.SENDNOTIFICATIONOPERATION, null);
					JsonObject res = new JsonParser().parse(responseString).getAsJsonObject();
					if (res.has(AlertConstants.REFERENCENUMBER) && res.get(AlertConstants.REFERENCENUMBER) != null)
						map.put("ReferenceNumber", res.get(AlertConstants.REFERENCENUMBER).getAsString());
				} else {
					ExecuteQueryWorkerJobs.insertToAlertHistory(map);
				}
				addNameParams(map, event);
			}
		} catch (Exception e) {
			alert.prepareError("Exception occured ", e).log();
		}
		return map;
	}

	private static Map<String, Object> addPushMessageToQueue(Map<String, JsonObject> commtemplate,
			boolean isalertneedstobepushed, Event event) {
		Map<String, Object> map = new HashMap<>();
		String errmessage = event.getPushmessage();
		if (!event.getChpush()) {
			isalertneedstobepushed = false;
			errmessage = errmessage != null ? errmessage + " " + AlertsErrorMessages.ERRMSG7
					: AlertsErrorMessages.ERRMSG7;
		}
		if (event.getAppid() == null) {
			isalertneedstobepushed = false;
			errmessage = errmessage != null ? errmessage + " " + AlertsErrorMessages.ERRMSG9
					: AlertsErrorMessages.ERRMSG9;
		}
		String channel = "";
		String refid = "";
		try {
			channel = AlertConstants.CH_PUSH_NOTIFICATION;
			refid = event.getPushrefid();
			map = generateQueryMap(event, channel, refid, errmessage, commtemplate, isalertneedstobepushed);
			if (map != null && !map.isEmpty()) {
				ExecuteQueryWorkerJobs.insertToAlertHistory(map);
				addNameParams(map, event);
			}
		} catch (Exception e) {
			alert.prepareError("Exception occured ", e).log();
		}
		return map;
	}

	private static void addAlertLog(Event event, Map<String, JsonObject> commtemplate, boolean isalertneedstobepushed) {
		List<Map<String, Object>> inputlist = new ArrayList<>();
		try {
			if (event.getOtherErrorMessage() == null) {
				inputlist.add(addEmailToQueue(commtemplate, isalertneedstobepushed, event));

				inputlist.add(addSMSToQueue(commtemplate, isalertneedstobepushed, event));

				inputlist.add(addNotificationToQueue(commtemplate, isalertneedstobepushed, event));

				inputlist.add(addPushMessageToQueue(commtemplate, isalertneedstobepushed, event));
				processExternalAlerts(inputlist);
				return;
			}
			inputlist.add(addOtherErrorMessageToQueue(isalertneedstobepushed, event));
		} catch (Exception e) {
			alert.prepareError("Exception Occured", e).log();
		}
		processExternalAlerts(inputlist);
	}

	private static void processExternalAlerts(List<Map<String, Object>> inputlist) {
		if (Boolean.parseBoolean(StaticDataHolder.getExternalAlerts())) {
			Map<String, Object> inputmap = new HashMap<>();
			try {
				inputmap.put(AlertConstants.INPUTPARAMS, JSONUtils.stringify(inputlist));
				AlertsUtils.callInternalServiceAndGetJson(inputmap, AlertConstants.EXTERNALALERTSSERVICEID,
						AlertConstants.EXTERNALALERTSOPERATIONID, null);
			} catch (IOException e) {
				alert.prepareError("Exception occured", e).log();
			}

		}

	}

	private static Map<String, Object> addOtherErrorMessageToQueue(boolean isalertneedstobepushed, Event event) {
		Map<String, Object> map = new HashMap<>();
		if (isalertneedstobepushed)
			return map;
		String errmessage = "";
		try {
			errmessage = event.getOtherErrorMessage();
			map = generateQueryMap(event, null, null, errmessage, null, isalertneedstobepushed);
			if (map != null && !map.isEmpty()) {
				ExecuteQueryWorkerJobs.insertToAlertHistory(map);
				addNameParams(map, event);
			}
		} catch (Exception e) {
			alert.prepareError("Exception occured ", e).log();
		}
		return map;

	}

	private static String replacespecials(String text) {
		if (text == null)
			return text;
		text = text.replaceAll("\'", "\\\\'");
		text = text.replaceAll("\"", "\\\\\"");
		return text;
	}

	private static Map<String, Object> generateQueryMap(Event event, String channel, String refid, String message,
			Map<String, JsonObject> commtemplate, boolean isalertneedstobepushed) {
		Map<String, Object> input = new HashMap<>();
		try {
			JsonObject params = null;
			if (commtemplate != null)
				params = commtemplate.get(channel);
			if (params == null && event.getOtherErrorMessage() == null) {
				isalertneedstobepushed = false;
				message = message != null ? message + " " + AlertsErrorMessages.ERRMSG10 : AlertsErrorMessages.ERRMSG10;
			}
			String text = null;
			String subject = null;
			String sendername = null;
			String sendermail = null;
			if (params != null) {
				text = replacespecials(AlertsUtils.getJsonObjects(params, AlertConstants.TEXT, false));
				subject = replacespecials(AlertsUtils.getJsonObjects(params, AlertConstants.SUBJECT, false));
				sendername = replacespecials(AlertsUtils.getJsonObjects(params, AlertConstants.SENDERNAME, false));
				sendermail = replacespecials(AlertsUtils.getJsonObjects(params, AlertConstants.SENDERMAIL, false));
			}
			if (sendername == null || sendername.equals(AlertConstants.NULL)
					|| sendername.equals(AlertConstants.EMPTYSTRING))
				sendername = AlertsUtils.getConfigProperty("ALERT_EMAIL_SENDER_NAME");
			String eventid = event.getEventid();
			String alertsubtype = event.getAlertsubtype();
			String alerttype = event.getAlerttype();
			String alertcategory = event.getAlertcategory();
			String langcode = event.getLanguagecode();
			String alertid = UUID.randomUUID().toString().replace("-", "");
			String status = "";
			if (!isalertneedstobepushed)
				status = AlertConstants.SID_DELIVERY_NOTSUBMITTED;
			else if ((refid == null || refid.equals("") || refid.equals("-1"))
					&& !channel.equals(AlertConstants.CH_NOTIFICATION_CENTER))
				status = AlertConstants.SID_DELIVERYFAILED;
			else {
				status = AlertConstants.SID_DELIVERY_SUBMITTED;
				message = "";
			}
			String username = "";
			String customerid = "";
			String flag = "0";
			Calendar calendar = Calendar.getInstance();
			java.sql.Timestamp timestamp = new java.sql.Timestamp(calendar.getTime().getTime());
			customerid = event.getCustomerid();
			String coreCustomerId = event.getCorecustomerid();
			username = event.getUsername();
			String commstatusid = "";
			commstatusid = event.getCommstatusid();
			input.put("id", alertid);
			input.put("EventId", eventid);
			input.put("AlertSubTypeId", alertsubtype);
			input.put("AlertTypeId", alerttype);
			input.put("AlertCategoryId", alertcategory);
			input.put("AlertStatusId", commstatusid);
			input.put("Customer_Id", customerid);
			input.put("LanguageCode", langcode);
			input.put("ChannelId", channel);
			input.put("Status", status);
			input.put("DispatchDate", timestamp);
			input.put("createdby", username);
			input.put("modifiedby", username);
			input.put("createdts", timestamp);
			input.put("lastmodifiedts", timestamp);
			input.put("synctimestamp", timestamp);
			input.put("softdeleteflag", flag);
			input.put("Message", text);
			input.put("AlertGroupName", event.getAlertGroupName());
			input.put("AlertCategoryName", event.getAlertCategoryName());
			input.put("AlertName", event.getAlertName());
			for (int i = 0; i < alerthhistory.length; i++) {
				if (alerthhistory[i].equals("Subject")) {
					if (subject != null && !subject.equals("")) {
						input.put("Subject", subject);
					}
				} else if (alerthhistory[i].equals("SenderName")) {
					if (sendername != null && !sendername.equals("")) {
						input.put("SenderName", sendername);
					}
				} else if (alerthhistory[i].equals("SenderEmail")) {
					if (sendermail != null && !sendermail.equals("")) {
						input.put("SenderEmail", sendermail);
					}
				} else if (alerthhistory[i].equals("ReferenceNumber")) {
					if (refid != null && !refid.equals("")) {
						input.put("ReferenceNumber", refid);
					}
				} else if (alerthhistory[i].equals("ErrorMessage") && message != null && !message.equals("")) {
					input.put("ErrorMessage", message);
				} else if (alerthhistory[i].equals("coreCustomerId") && coreCustomerId != null
						&& !coreCustomerId.equals("")) {
					input.put("coreCustomerId", coreCustomerId);
				}

			}

		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured", e).log();
			return input;
		}
		return input;
	}
}
