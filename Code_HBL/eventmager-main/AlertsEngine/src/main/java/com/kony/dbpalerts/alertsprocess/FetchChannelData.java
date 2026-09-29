package com.kony.dbpalerts.alertsprocess;

import java.util.List;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbpalerts.alertsutils.AlertConstants;
import com.kony.dbpalerts.alertsutils.Event;
import com.kony.dbpalerts.alertsutils.RecipientUtil;
import com.kony.dbpalerts.alertsutils.UserAlertDTO;
import com.kony.dbpalerts.dbconnectionutils.CustomerAlertsQueries;
import com.kony.dbpalerts.dbconnectionutils.GlobalAlertQueries;

public class FetchChannelData {
	private FetchChannelData() {

	}

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	public static List<Event> fillGlobalAlertTypeData(Map<String, Map<String, String>> resultsetmap,
			List<Event> events) {
		for (Event event : events) {
			Map<String, String> eventsdata = resultsetmap.get(event.getAlertsubtype());
			if (eventsdata == null)
				continue;
			event.setAlertcategory(eventsdata.get(AlertConstants.ALERTCATEGORYID));
			event.setAlerttypestatus(eventsdata.get(AlertConstants.ALERTTYPE_STATUS_ID));
			event.setAlertsubtypestatus(eventsdata.get(AlertConstants.ALERTSUBTYPETYPE_STATUS_ID));
			event.setAlertcategorystatus(eventsdata.get(AlertConstants.ALERTCATEGORY_STATUS_ID));
			event.setAlertconditionid(eventsdata.get(AlertConstants.ALERTCONDITIONID));
			event.setRecipientType(eventsdata.get(AlertConstants.RECIPIENTTYPE));
			event.setAttributeid(eventsdata.get(AlertConstants.ATTRIBUTEID));
			event.setAlertCategoryName(eventsdata.get(AlertConstants.ALERTCATEGORYNAME));
			event.setAlertGroupName(eventsdata.get(AlertConstants.ALERTGROUPNAME));
			event.setAlertName(eventsdata.get(AlertConstants.ALERTNAME));
			if (eventsdata.containsKey(AlertConstants.ISGLOBAL) && eventsdata.get(AlertConstants.ISGLOBAL) != null)
				event.setIsglobal(!(eventsdata.get(AlertConstants.ISGLOBAL).equals("0")
						|| eventsdata.get(AlertConstants.ISGLOBAL).equals("false")));
			if (eventsdata.containsKey(AlertConstants.ACCOUNTLEVEL)
					&& eventsdata.get(AlertConstants.ACCOUNTLEVEL) != null)
				event.setIsaccountLevel(!(eventsdata.get(AlertConstants.ACCOUNTLEVEL).equals("0")
						|| eventsdata.get(AlertConstants.ACCOUNTLEVEL).equals("false")));
			event.setChemail(eventsdata.containsKey(AlertConstants.CH_EMAIL));
			event.setChnotification(eventsdata.containsKey(AlertConstants.CH_NOTIFICATION_CENTER));
			event.setChpush(eventsdata.containsKey(AlertConstants.CH_PUSH_NOTIFICATION));
			event.setChsms(eventsdata.containsKey(AlertConstants.CH_SMS));
			System.out.println("--smsGlobal--"+event.getChsms());
			System.out.println("--emailGlobal--"+event.getChemail());
			if (eventsdata.containsKey(AlertConstants.EXTERNALSYSTEM)
					&& (eventsdata.get(AlertConstants.EXTERNALSYSTEM).equals("1")
							|| eventsdata.get(AlertConstants.EXTERNALSYSTEM).equals("true")))
				event.setIsExternalSystem(true);
			if (event.getIsglobal()) {
				event.setValue1(eventsdata.get(AlertConstants.VALUE1));
				event.setValue2(eventsdata.get(AlertConstants.VALUE2));
			}
		}
		
		return events;
	}

	private static String generateKey(Event event) {
		String key = null;
		if (event.getIsaccountLevel() && event.getIsaccounttypelevel() && event.getAccountTypeId() != null
				&& event.getAccountid() != null) {
			key = event.getAlertsubtype() + event.getCustomerid() + "*" + event.getAccountTypeId();
		} else if (event.getIsaccountLevel() && event.getAccountid() != null) {
			key = event.getAlertsubtype() + event.getCustomerid() + event.getAccountid() + "*";
		} else {
			key = event.getAlertsubtype() + event.getCustomerid() + "**";
		}
		return key;
	}

	private static void fillUserAlertData(Map<String, UserAlertDTO> useralertdata, List<Event> events) {
		for (Event event : events) {
			if (event.getAlertsubtype() == null || event.getCustomerid() == null || event.getIsglobal())
				continue;
			String key = generateKey(event);
			try {
				UserAlertDTO userdto = useralertdata.get(key);
				if (userdto == null) {
					if (!event.getIsglobal()) {
						event.setChemail(false);
						event.setChnotification(false);
						event.setChsms(false);
						event.setChpush(false);
					}
				} else {
					event.setValue1(userdto.getValue1());
					event.setValue2(userdto.getValue2());
					event.setChemail(event.getChemail() && userdto.getChannels().contains(AlertConstants.CH_EMAIL));
					event.setChnotification(event.getChnotification()
							&& userdto.getChannels().contains(AlertConstants.CH_NOTIFICATION_CENTER));
					event.setChpush(
							event.getChpush() && userdto.getChannels().contains(AlertConstants.CH_PUSH_NOTIFICATION));
					event.setChsms(event.getChsms() && userdto.getChannels().contains(AlertConstants.CH_SMS));
					System.out.println("--smsUser--"+AlertConstants.CH_SMS);
					System.out.println("--emailUser--"+AlertConstants.CH_EMAIL);
				}
			} catch (Exception e) {
				diagnostic.prepareDebug("Error occured while filling up account level data", e).log();
			}
		}
	}

	public static List<Event> fetchAlertMasterData(List<Event> events, String alertConfigLevel) {
		List<Event> eventList = events;
		try {
			Map<String, Map<String, String>> resultsetmap = GlobalAlertQueries.processGlogalData(events,
					alertConfigLevel);
			fillGlobalAlertTypeData(resultsetmap, events);
			eventList = RecipientUtil.fetchRecipientListAndUpdateEvents(events);
		} catch (Exception e) {
			alert.prepareError("Error occured", e).log();
		}
		return eventList;
	}

	protected static void fetchEventSpecificDataConfig(List<Event> events, Integer isaccountlevel,
			String alertConfigLevel) {
		try {
			Map<String, UserAlertDTO> useralertdata;
			useralertdata = CustomerAlertsQueries.processUserLevelNonAccountAlert(events, isaccountlevel,
					alertConfigLevel);
			fillUserAlertData(useralertdata, events);
		} catch (Exception e) {
			alert.prepareError("Error occured", e).log();
		}
	}
}
