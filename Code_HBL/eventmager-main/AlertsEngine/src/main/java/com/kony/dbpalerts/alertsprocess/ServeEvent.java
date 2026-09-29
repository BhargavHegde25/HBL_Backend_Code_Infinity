package com.kony.dbpalerts.alertsprocess;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbpalerts.alertsutils.AlertConstants;
import com.kony.dbpalerts.alertsutils.AlertsErrorMessages;
import com.kony.dbpalerts.alertsutils.Event;
import com.kony.dbpalerts.alertsutils.StaticDataHolder;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class ServeEvent implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	private String checkEventInValid(Event event) {
		if (event == null)
			return AlertsErrorMessages.ERRMSG11;
		if (event.getAlertcategory() == null)
			return AlertsErrorMessages.ERRMSG12;
		if (event.getAlerttype() == null)
			return AlertsErrorMessages.ERRMSG13;
		if (event.getAlertsubtype() == null)
			return AlertsErrorMessages.ERRMSG14;
		if (event.getCustomerid() == null)
			return AlertsErrorMessages.ERRMSG15;
		if (event.getUsername() == null)
			return AlertsErrorMessages.ERRMSG16;
		return null;
	}

	private boolean checkIsAccountLevelInValid(Event event) {
		return (!event.getIsglobal() && event.getIsaccountLevel() && event.getAccountid() == null);
	}

	public boolean isAppLevelValid(Map<String, String> appleveldata, Map<String, String> sessiondata,
			Map<String, String> alertcontentfieldsfromotherdata, Event event, Map<String, String> mfinfoappleveldata) {
		try {
			String appkey = AlertConstants.APPID_LOWER;
			String appidvalue = null;
			String mfappkey = AlertConstants.AID_LOWER;
			if (alertcontentfieldsfromotherdata.containsKey(appkey))
				appidvalue = (alertcontentfieldsfromotherdata.get(appkey) == null) ? null
						: alertcontentfieldsfromotherdata.get(appkey);
			else if (sessiondata.containsKey(mfappkey) && mfinfoappleveldata.containsKey(sessiondata.get(mfappkey))) {
				appidvalue = mfinfoappleveldata.get(sessiondata.get(mfappkey));
			} else {
				return true;
			}

			if (appidvalue == null || appidvalue.equals("")) {
				return true;
			}
			event.setAppid(appidvalue);
			if (appleveldata.containsKey(appidvalue)) {
				if (appleveldata.get(appidvalue) != null
						&& !appleveldata.get(appidvalue).contains(event.getAlertsubtype()))
					return false;
			} else {
				return false;
			}

		} catch (Exception e) {
			alert.prepareError("Exception occured", e).log();
		}
		return true;
	}

	boolean isCustomerTypeisValid(Event event, Map<String, String> custtypedata) {
		try {
			if (event.getCustomertype() == null)
				return true;

			if (custtypedata.containsKey(event.getCustomertype())) {
				if (custtypedata.get(event.getCustomertype()) != null
						&& !custtypedata.get(event.getCustomertype()).contains(event.getAlertsubtype()))
					return false;
			} else {
				return false;
			}
		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
		}
		return true;
	}

	private boolean custSwithDisabled(Event event, Map<String, String> custswitchdata) {

		String key = "";
		if (!event.getIsaccountLevel() && event.getCustomerid() != null && event.getAlertcategory() != null) {
			key = event.getCustomerid() + AlertConstants.ASTERISK + event.getAlertcategory() + AlertConstants.ASTERISK;
		}

		else if (event.getIsaccountLevel() && event.getCustomerid() != null && event.getAlertcategory() != null
				&& event.getAccountid() != null && event.getIsaccounttypelevel()) {
			key = event.getCustomerid() + AlertConstants.ASTERISK + event.getAlertcategory() + event.getAccountTypeId();
		}

		else if (event.getIsaccountLevel() && event.getCustomerid() != null && event.getAlertcategory() != null
				&& event.getAccountid() != null && !event.getIsaccounttypelevel()) {
			key = event.getCustomerid() + event.getAccountid() + event.getAlertcategory() + AlertConstants.ASTERISK;
		} else {
			return true;
		}
		diagnostic.prepareDebug("custswitchdata"+custswitchdata).log();
		diagnostic.prepareDebug("key"+key).log();
		return !custswitchdata.containsKey(key) || !custswitchdata.get(key).equals(AlertConstants.SUBSCRIBED);

	}

	private boolean checkIsAlertInActive(Event event) {
		return (event.getAlerttypestatus() == null || !event.getAlerttypestatus().equals(AlertConstants.SUCCESS_STATUS))
				|| event.getAlertcategorystatus() == null
				|| !event.getAlertcategorystatus().equals(AlertConstants.SUCCESS_STATUS)
				|| event.getAlertsubtypestatus() == null
				|| !event.getAlertsubtypestatus().equals(AlertConstants.SUCCESS_STATUS);
	}

	private static void replaceCommunicationParamsIfPassed(Event event, Map<String, String> otherdata) {
		diagnostic.prepareDebug("otherdata "+otherdata).log();
		try {
			Set<String> phone = new HashSet<>();
			Set<String> email = new HashSet<>();
			if (otherdata.containsKey(AlertConstants.INCLUDEPREFERREDCONTACT)
					&& otherdata.get(AlertConstants.INCLUDEPREFERREDCONTACT) != null
					&& Boolean.parseBoolean(otherdata.get(AlertConstants.INCLUDEPREFERREDCONTACT))) {
				phone = event.getPhone();
				email = event.getEmail();
			}
			if (otherdata.containsKey(AlertConstants.EXTERNALMOBILE)
					&& otherdata.get(AlertConstants.EXTERNALMOBILE) != null) {
				Set<String> phonenumbers = new HashSet<>(
						Arrays.asList(otherdata.get(AlertConstants.EXTERNALMOBILE).split(",")));
				phone.addAll(phonenumbers);
				event.setPhone(phone);
			}
			if (otherdata.containsKey(AlertConstants.EXTERNALEMAIL)
					&& otherdata.get(AlertConstants.EXTERNALEMAIL) != null) {
				Set<String> emailids = new HashSet<>(
						Arrays.asList(otherdata.get(AlertConstants.EXTERNALEMAIL).split(",")));
				email.addAll(emailids);
				event.setEmail(email);
			}

		} catch (Exception e) {
			alert.prepareError("Error occured while re-registering user with KMS:", e).log();
		}

	}

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {

		Result result = new Result();
		Map<String, Map<String, JsonObject>> communicationdata = StaticDataHolder.getGlobalcommunicationdata();
		Map<String, String> custswitchdata = StaticDataHolder.getGlobalcustswitchdata();

		Map<String, String> appleveldata = StaticDataHolder.getAppleveldata();

		Map<String, String> customertypedata = StaticDataHolder.getCustomertypedata();
		Map<String, String> mfinfoappleveldata = StaticDataHolder.getMfinfoappleveldata();
		String input = request.getParameter(AlertConstants.INPUT_EVENTS);
		Event event = null;
		if (input != null) {
			event = JSONUtils.parse(input, Event.class);
		}

		if (event == null) {
			diagnostic.prepareDebug("No input is passed").log();
			event = new Event();
			event.setOtherErrMsg("No input is passed");
			SendAlertAndLog.pushAlertAndUpdateLog(event, null, false);
			return result;
		}
		diagnostic.prepareDebug(Boolean.toString(event.getChsms())).log();
		diagnostic.prepareDebug(Boolean.toString(event.getChemail())).log();
		diagnostic.prepareDebug(Boolean.toString(event.getChnotification())).log();
		diagnostic.prepareDebug(Boolean.toString(event.getChpush())).log();
		try {
			String eventInvalidErrMsg = checkEventInValid(event);
			if (eventInvalidErrMsg != null) {
				event.setOtherErrMsg(eventInvalidErrMsg);
				SendAlertAndLog.pushAlertAndUpdateLog(event, null, false);
				diagnostic.prepareDebug("This event is invalid...!").log();
				diagnostic.prepareDebug(eventInvalidErrMsg).log();
				return result;
			}
			if (checkIsAccountLevelInValid(event)) {
				event.setOtherErrMsg("Account number is not passed for this account Alerts...!");
				SendAlertAndLog.pushAlertAndUpdateLog(event, null, false);
				diagnostic.prepareDebug("Account number is not passed for this account Alerts...!").log();
				return result;
			}
			if (!event.getIsglobal() && custSwithDisabled(event, custswitchdata)) {
				event.setOtherErrMsg("Alerts not subscribed for this event category.");
				SendAlertAndLog.pushAlertAndUpdateLog(event, null, false);
				diagnostic.prepareDebug("Alerts not subscribed for this event category.").log();
				return result;
			}
			Map<String, JsonObject> communicationtemplate;
			Map<String, String> alertcontentfieldsfromeventdata = new HashMap<>();
			Map<String, String> alertcontentfieldsfromotherdata = new HashMap<>();
			Map<String, String> sessiondata = new HashMap<>();
			if (checkIsAlertInActive(event)) {
				event.setOtherErrMsg("This alert is inactive.");
				SendAlertAndLog.pushAlertAndUpdateLog(event, null, false);
				diagnostic.prepareDebug("This alert is inactive.").log();
				return result;
			}
			JsonElement eventjson = new JsonParser().parse(event.getEventJson());
			PreProcessAlert.getAllPairsFromJson(eventjson, alertcontentfieldsfromeventdata, AlertConstants.EVENTDATA);
			PreProcessAlert.getAllPairsFromJson(eventjson, alertcontentfieldsfromotherdata, AlertConstants.OTHERDATA);
			PreProcessAlert.getAllPairsFromJson(eventjson, sessiondata, AlertConstants.SESSION);

			if (!isAppLevelValid(appleveldata, sessiondata, alertcontentfieldsfromotherdata, event,
					mfinfoappleveldata)) {
				event.setOtherErrMsg("Alert is disabled for this at application Level.");
				SendAlertAndLog.pushAlertAndUpdateLog(event, null, false);
				diagnostic.prepareDebug("Alert is disabled for this at application Level.").log();
				return result;
			}
			if (!isCustomerTypeisValid(event, customertypedata)) {
				event.setOtherErrMsg("Alert is disabled for this customer type Level.");
				SendAlertAndLog.pushAlertAndUpdateLog(event, null, false);
				diagnostic.prepareDebug("Alert is disabled for this customer type Level.").log();
				return result;
			}
			replaceCommunicationParamsIfPassed(event, alertcontentfieldsfromotherdata);
			communicationtemplate = PreProcessAlert.fetchCommunicationTemplate(event, communicationdata,
					alertcontentfieldsfromeventdata, alertcontentfieldsfromotherdata, sessiondata);
			if (communicationtemplate == null || communicationtemplate.isEmpty()) {
				event.setOtherErrMsg("communication template is not present for event");
				SendAlertAndLog.pushAlertAndUpdateLog(event, null, false);
				diagnostic.prepareDebug("communication template is not present for event").log();
				return result;
			}
			if (ProcessAlert.isConditionMet(event, alertcontentfieldsfromeventdata, alertcontentfieldsfromotherdata)) {
				SendAlertAndLog.pushAlertAndUpdateLog(event, communicationtemplate, true);
			} else {
				SendAlertAndLog.pushAlertAndUpdateLog(event, communicationtemplate, false);
				diagnostic.prepareDebug("Event conditions are not satisfied so not pushing this alert").log();
			}
		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured in running thread:", e).log();
		}
		diagnostic.prepareDebug("End of Service").log();
		return result;
	}
}
