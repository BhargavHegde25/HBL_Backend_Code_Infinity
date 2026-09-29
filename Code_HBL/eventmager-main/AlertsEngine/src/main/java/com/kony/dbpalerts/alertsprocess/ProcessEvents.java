package com.kony.dbpalerts.alertsprocess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbpalerts.alertsutils.AccountHelper;
import com.kony.dbpalerts.alertsutils.AlertConstants;
import com.kony.dbpalerts.alertsutils.CommunicationDataDTO;
import com.kony.dbpalerts.alertsutils.Event;
import com.kony.dbpalerts.alertsutils.AlertsUtils;
import com.kony.dbpalerts.dbconnectionutils.FetchCustomerData;
import com.kony.dbpalerts.dbconnectionutils.GetCoreCustomerId;
import com.kony.dbpalerts.dbconnectionutils.InitialDbProcess;
import com.kony.dbpalerts.dbconnectionutils.PreProcessingQueries;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

public class ProcessEvents {
	private ProcessEvents() {

	}

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	public static List<Event> processAllEvents(JsonArray eventsarray) {
		List<Event> eventdata = null;
		eventdata = fetchEventSpecificData(eventsarray);
		if (eventdata.isEmpty())
			return eventdata;
		getAccountNumber(eventdata, eventsarray);
		getCustomerId(eventdata, eventsarray);
		getUserName(eventdata, eventsarray);
		getCoreId(eventdata, eventsarray);
		fillCustomerIdFromCoreId(eventdata);
		getLegalEntityId(eventdata, eventsarray);
		return eventdata;
	}

	public static void processprimaryData(JsonArray eventsarray, List<Event> eventdata) {
		// fillCustomerIdFromAccountNumber(eventdata);
		fillCustomerDetails(eventdata);
		fillCustomerDetailsFromCore(eventdata);
	}

	private static void fillCustomerDetailsFromCore(List<Event> events) {
		if (events == null)
			return;
		Result res = null;
		Set<String> custset = new HashSet<>();
		Set<String> companylegalunit = new HashSet<>();
		for (Event e : events) {
			if (e.getCustomerid() != null) {
				custset.add(e.getCustomerid());
				companylegalunit.add(e.getCompanyLegalUnit());
			}
		}
		if (custset.isEmpty()||companylegalunit.isEmpty())
			return;
		Map<String, Object> requestParameters = new HashMap<>();
		requestParameters.put("loop_count", custset.size());
		requestParameters.put("loop_seperator", ",");
		requestParameters.put("customerId", custset.stream().collect(Collectors.joining(",")));
		requestParameters.put("legalEntityId", companylegalunit.stream().collect(Collectors.joining(",")));
		try {
			res = DBPServiceExecutorBuilder.builder().withOperationId("getCustomerDetails_Orch")
					.withRequestParameters(requestParameters).withServiceId("CustomerData_Orch").build().getResult();
		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured", e).log();
		}
		if (res != null)
			diagnostic.prepareDebug(ResultToJSON.convert(res)).log();
		if (res != null)
			processCustomerData(events, res);

	}

	private static void processMobileRecords(Dataset commdataset, CommunicationDataDTO c) {
		List<Record> phonerecords = commdataset.getAllRecords();
		if (phonerecords != null) {
			for (Record custcommdata : phonerecords) {
				boolean isPrimary = custcommdata.getParamValueByName(AlertConstants.ISPRIMARY) != null
						&& custcommdata.getParamValueByName(AlertConstants.ISPRIMARY).equals(AlertConstants.TRUE);
				boolean isAlertsRequired = custcommdata.getParamValueByName(AlertConstants.ISALERTSREQUIRED) != null
						&& custcommdata.getParamValueByName(AlertConstants.ISALERTSREQUIRED)
								.equals(AlertConstants.TRUE);
				String countryCode = custcommdata.getParamValueByName(AlertConstants.PHONECOUNTRYCODE) != null
						? custcommdata.getParamValueByName(AlertConstants.PHONECOUNTRYCODE)
						: "";
				if (c.getPhone() == null && (isPrimary || isAlertsRequired)) {
					c.setPhone(new StringBuilder().append(countryCode)
							.append(custcommdata.getParamValueByName(AlertConstants.VAL)).toString());
				} else {
					if (isAlertsRequired)
						c.setPhone(new StringBuilder().append(countryCode)
								.append(custcommdata.getParamValueByName(AlertConstants.VAL)).toString());
				}
			}
		}
	}

	private static void processEmailRecords(Dataset commdataset, CommunicationDataDTO c) {
		List<Record> phonerecords = commdataset.getAllRecords();
		if (phonerecords != null) {
			for (Record custcommdata : phonerecords) {
				boolean isPrimary = custcommdata.getParamValueByName(AlertConstants.ISPRIMARY) != null
						&& custcommdata.getParamValueByName(AlertConstants.ISPRIMARY).equals(AlertConstants.TRUE);
				boolean isAlertsRequired = custcommdata.getParamValueByName(AlertConstants.ISALERTSREQUIRED) != null
						&& custcommdata.getParamValueByName(AlertConstants.ISALERTSREQUIRED)
								.equals(AlertConstants.TRUE);
				if (c.getEmail() == null && (isPrimary || isAlertsRequired)) {
					c.setEmail(custcommdata.getParamValueByName(AlertConstants.VAL));
				} else {
					if (isAlertsRequired)
						c.setEmail(custcommdata.getParamValueByName(AlertConstants.VAL));
				}
			}
		}
	}

	private static void fillCommunicationData(List<Event> events, Map<String, CommunicationDataDTO> customerdatamap) {
		for (Event e : events) {
			if (e.getCustomerid() != null && customerdatamap.containsKey(e.getCustomerid())) {
				CommunicationDataDTO c = customerdatamap.get(e.getCustomerid());
				Set<String> phone = new HashSet<>();
				Set<String> email = new HashSet<>();
				email.add(c.getEmail());
				phone.add(c.getPhone());
				e.setPhone(phone);
				e.setEmail(email);
				e.setFirstname(c.getFirstName());
				e.setLastname(c.getLastName());
				e.setMiddlename(c.getMiddleName());
				e.setCustomertype(c.getCustomertypeid());
			}
		}
	}

	private static void processCustomerData(List<Event> events, Result res) {

		Dataset ds = res.getDatasetById("LoopDataset");
		if (ds == null)
			return;
		Map<String, CommunicationDataDTO> customerdatamap = new HashMap<>();
		List<Record> records = ds.getAllRecords();
		for (Record rec : records) {
			Dataset custdataset = rec.getDatasetById("customer");
			if (custdataset == null)
				continue;
			for (Record r : custdataset.getAllRecords()) {
				String custid = r.getParamValueByName("id");
				if (custid == null)
					continue;
				CommunicationDataDTO c = new CommunicationDataDTO();
				c.setFirstName(r.getParamValueByName("FirstName"));
				c.setLastName(r.getParamValueByName("LastName"));
				c.setCustomertypeid(r.getParamValueByName("CustomerType_id"));
				Dataset commdataset = r.getDatasetById("ContactNumbers");
				if (commdataset != null)
					processMobileRecords(commdataset, c);
				commdataset = r.getDatasetById("EmailIds");
				if (commdataset != null)
					processEmailRecords(commdataset, c);
				customerdatamap.put(custid, c);
			}
		}
		fillCommunicationData(events, customerdatamap);
	}

	public static List<Event> assignAccountLevelConf(List<Event> events, int isaccountidlevel) {
		for (Event event : events) {
			if (isaccountidlevel == 0)
				event.setIsaccounttypelevel(true);
		}
		return events;
	}

	private static void fillCustomerIdFromAccountNumbers(Map<String, List<String>> accountcustomermap,
			List<Event> events) {
		List<Event> newevents = new ArrayList<>();
		if (accountcustomermap == null || accountcustomermap.isEmpty())
			return;
		for (Event x : events) {
			if (x.getCustomerid() == null && x.getUsername() == null && x.getAccountid() != null
					&& accountcustomermap.containsKey(x.getAccountid())) {
				List<String> custlist = accountcustomermap.get(x.getAccountid());
				if (custlist != null && !custlist.isEmpty()) {
					x.setCustomerid(custlist.get(0));
					for (int i = 1; i < custlist.size(); i++) {
						Event newevent = createNewEventObject(x);
						newevent.setCustomerid(custlist.get(i));
						newevents.add(newevent);
					}
				}
			}
		}
		if (!newevents.isEmpty())
			events.addAll(newevents);
	}

	public static Event createNewEventObject(Event e) {
		Event newevent = new Event();
		newevent.setEventid(e.getEventid());
		newevent.setAlerttype(e.getAlerttype());
		newevent.setAlertsubtype(e.getAlertsubtype());
		newevent.setCommstatusid(e.getCommstatusid());
		newevent.setEventJson(e.getEventJson());
		newevent.setLanguagecode(e.getLanguagecode());
		newevent.setAccountid(e.getAccountid());
		newevent.setCorecustomerid(e.getCorecustomerid());
		return newevent;
	}

	private static void fillAccountTypeInfo(Map<String, String> accountcustomermap, List<Event> events) {
		if (accountcustomermap != null && !accountcustomermap.isEmpty())
			for (Event x : events) {
				if (x.getAccountid() != null && accountcustomermap.containsKey(x.getAccountid())) {
					x.setAccountTypeId(accountcustomermap.get(x.getAccountid()));
				}
			}
	}

	public static void fillCustomerIdFromAccountNumber(List<Event> events) {
		AccountHelper accountinfo = PreProcessingQueries.accountCustomerIdMapping(events);
		if (accountinfo == null)
			return;
		fillCustomerIdFromAccountNumbers(accountinfo.getAccountcustomerinfo(), events);
		fillAccountTypeInfo(accountinfo.getAccounttypeinfo(), events);

	}

	public static void fillCustomerIdFromCoreId(List<Event> events) {
		Map<String, Set<String>> coreidcustomermap = GetCoreCustomerId.getCoreIdentityMap(events);
		List<Event> newevents = new ArrayList<>();
		if (coreidcustomermap == null)
			return;
		for (Event x : events) {
			if (x.getCustomerid() == null && x.getUsername() == null && x.getCorecustomerid() != null
					&& coreidcustomermap.containsKey(x.getCorecustomerid())) {
				Set<String> custlistset = coreidcustomermap.get(x.getCorecustomerid());
				List<String>custlist = new ArrayList<>(custlistset);
				if (custlist != null && !custlist.isEmpty()) {
					x.setCustomerid(custlist.get(0));
					for (int i = 1; i < custlist.size(); i++) {
						Event newevent = createNewEventObject(x);
						newevent.setCustomerid(custlist.get(i));
						newevents.add(newevent);
					}
				}
			}
		}
		if (!newevents.isEmpty())
			events.addAll(newevents);
	}

	public static List<Event> fetchEventSpecificData(JsonArray eventsarray) {
		List<Event> events = new ArrayList<>();
		String alerttype = "";
		String alertsubtype = "";
		String eventid = "";
		String commstatusid = "";
		if (eventsarray == null)
			return events;
		String languagecode = null;
		try {
			languagecode = AlertsUtils.getConfigProperty(AlertConstants.ALERTS_DEFAULT_LANGUAGE);
		} catch (Exception e1) {
			// property not configured
		}
		if (languagecode == null)
			languagecode = AlertConstants.DEFAULT_LANGUAGE;
		for (JsonElement event : eventsarray) {
			try {
				alerttype = AlertsUtils.getJsonObjects(event.getAsJsonObject(), AlertConstants.EVENTTYPE, true);
				alertsubtype = AlertsUtils.getJsonObjects(event.getAsJsonObject(), AlertConstants.EVENTSUBTYPE, true);
				eventid = AlertsUtils.getJsonObjects(event.getAsJsonObject(), AlertConstants.EVENTID, true);
				commstatusid = AlertsUtils.getJsonObjects(event.getAsJsonObject(), AlertConstants.STATUS, true);
				Event e = new Event();
				e.setEventid(eventid);
				e.setAlerttype(alerttype);
				e.setAlertsubtype(alertsubtype);
				e.setCommstatusid(commstatusid);

				try {
					e.setEventJson(event.getAsString());
				} catch (Exception ex) {
					e.setEventJson(event.toString());
				}

				e.setLanguagecode(languagecode);
				events.add(e);
			} catch (Exception e) {
				alert.prepareError("Error in fetching alert data for event:" + event, e).log();
				return events;
			}
		}
		return events;
	}

	public static void getAccountNumber(List<Event> events, JsonArray eventsarray) {
		String eventid = null;
		String accnum = null;
		JsonObject otherdata = null;
		Map<String, String> accountdata = new HashMap<>();
		for (JsonElement event : eventsarray) {
			try {
				otherdata = AlertsUtils.getJsonObjects(AlertConstants.OTHERDATA, event.getAsJsonObject(), false);
				eventid = AlertsUtils.getJsonObjects(event.getAsJsonObject(), AlertConstants.EVENTID, true);

			} catch (Exception e) {
				alert.prepareError("Error in fetching other data for event  :" + event, e).log();
			}
			if (otherdata != null) {
				try {
					accnum = AlertsUtils.getJsonObjects(otherdata, AlertConstants.ACCOUNTNUMBER, false);
				} catch (Exception e1) {
					diagnostic.prepareDebug(e1.toString()).log();
				}
			}
			if (accnum != null && eventid != null)
				accountdata.put(eventid, accnum);
		}
		for (Event x : events) {
			if (accountdata.containsKey(x.getEventid())) {
				x.setAccountid(accountdata.get(x.getEventid()));
			}
		}
	}

	public static void fillCustomerDetails(List<Event> events) {
        Map<String, String> customeruser = FetchCustomerData.getCustomerDataMap(events);
        for (Event x : events) {
            x.setCountrycode(AlertConstants.USA_CODE);
            if (x.getCustomerid() != null || x.getUsername() != null) {
                if (x.getUsername() == null && customeruser.containsKey(x.getCustomerid().toLowerCase())) {
                    String[] custDet = customeruser.get(x.getCustomerid().toLowerCase()).split(",");
                    x.setUsername(custDet[0]);
                    if(x.getCompanyLegalUnit()==null || x.getCompanyLegalUnit().isEmpty() )
                    {
                     x.setCompanyLegalUnit(custDet[1]);
                    }

                } else if (x.getCustomerid() == null && customeruser.containsKey(x.getUsername().toLowerCase())) {
                    String[] custDet = customeruser.get(x.getUsername().toLowerCase()).split(",");
                    x.setCustomerid(custDet[0]);                 

                    if(x.getCompanyLegalUnit()==null || x.getCompanyLegalUnit().isEmpty() )
                    {
                     x.setCompanyLegalUnit(custDet[1]);
                    }

                }
            }

        }
    }

	public static synchronized Map<String, String> fetchCustomerTypeData(Map<String, String> customertypedata) {

		return InitialDbProcess.fetchAlertSubtypeCustomerTypeData();

	}

	public static synchronized Map<String, String> fetchAppLevelData(Map<String, String> appleveldata) {

		return InitialDbProcess.fetchAppData();

	}

	public static synchronized Map<String, String> mfInfoFetchAppLevelData(Map<String, String> mfinfoappleveldata) {
		if (mfinfoappleveldata == null)
			return InitialDbProcess.fetchMfAppData();
		return mfinfoappleveldata;
	}

	public static void getCustomerId(List<Event> eventdata, JsonArray eventsarray) {
		String eventid = null;
		String custid = null;
		JsonObject otherdata = null;
		Map<String, String> customerdata = new HashMap<>();
		for (JsonElement event : eventsarray) {
			try {
				otherdata = AlertsUtils.getJsonObjects(AlertConstants.OTHERDATA, event.getAsJsonObject(), false);
				eventid = AlertsUtils.getJsonObjects(event.getAsJsonObject(), AlertConstants.EVENTID, true);
			} catch (Exception e) {
				alert.prepareError("Error while fetching other data for event:" + event, e).log();
			}
			if (otherdata != null) {
				try {
					custid = AlertsUtils.getJsonObjects(otherdata, AlertConstants.CUSTOMERID_LOWER, false);
				} catch (Exception e1) {
					diagnostic.prepareDebug(e1.toString()).log();
				}
			}
			if (custid != null && eventid != null)
				customerdata.put(eventid, custid);
		}
		for (Event x : eventdata) {
			if (customerdata.containsKey(x.getEventid()))
				x.setCustomerid(customerdata.get(x.getEventid()));
		}
	}

	public static void getUserName(List<Event> events, JsonArray eventsarray) {
		String eventid = null;
		String username = null;
		JsonObject otherdata = null;
		Map<String, String> customerdata = new HashMap<>();
		for (JsonElement event : eventsarray) {
			try {
				otherdata = AlertsUtils.getJsonObjects(AlertConstants.OTHERDATA, event.getAsJsonObject(), false);
				eventid = AlertsUtils.getJsonObjects(event.getAsJsonObject(), AlertConstants.EVENTID, true);
			} catch (Exception e) {
				alert.prepareError("Error in fetching otherdata for event::" + event, e).log();
			}
			if (otherdata != null) {
				try {
					username = AlertsUtils.getJsonObjects(otherdata, AlertConstants.USER, false);
				} catch (Exception e1) {
					diagnostic.prepareDebug(e1.toString()).log();
				}
			}
			if (username != null && eventid != null)
				customerdata.put(eventid, username);
		}
		for (Event x : events) {
			if (customerdata.containsKey(x.getEventid()))
				x.setUsername(customerdata.get(x.getEventid()));
		}
	}

	public static void getCoreId(List<Event> events, JsonArray eventsarray) {
		String eventid = null;
		String backendid = null;
		JsonObject otherdata = null;
		Map<String, String> customerdata = new HashMap<>();
		for (JsonElement event : eventsarray) {
			try {
				otherdata = AlertsUtils.getJsonObjects(AlertConstants.OTHERDATA, event.getAsJsonObject(), false);
				eventid = AlertsUtils.getJsonObjects(event.getAsJsonObject(), AlertConstants.EVENTID, true);
			} catch (Exception e) {
				alert.prepareError("Error in fetching otherdata for event::" + event, e).log();
			}
			if (otherdata != null) {
				try {
					backendid = AlertsUtils.getJsonObjects(otherdata, AlertConstants.CORECUSTOMERID, false);
				} catch (Exception e1) {
					diagnostic.prepareDebug(e1.toString()).log();
				}
			}
			if (backendid != null && eventid != null)
				customerdata.put(eventid, backendid);
		}
		for (Event x : events) {
			if (customerdata.containsKey(x.getEventid()))
				x.setCorecustomerid(customerdata.get(x.getEventid()));
		}
	}
	public static void getLegalEntityId(List<Event> eventdata, JsonArray eventsarray) {
		String eventid = null;
		String legalEntityId = null;
		JsonObject otherdata = null;
		Map<String, String> companydata = new HashMap<>();
		for (JsonElement event : eventsarray) {
			try {
				otherdata = AlertsUtils.getJsonObjects(AlertConstants.OTHERDATA, event.getAsJsonObject(), false);
				eventid = AlertsUtils.getJsonObjects(event.getAsJsonObject(), AlertConstants.EVENTID, true);
			} catch (Exception e) {
				alert.prepareError("Error while fetching other data for event:" + event, e).log();
			}
			if (otherdata != null) {
				try {
					legalEntityId = AlertsUtils.getJsonObjects(otherdata, AlertConstants.LEGALENTITYID, false);
				} catch (Exception e1) {
					diagnostic.prepareDebug(e1.toString()).log();
				}
			}
			if (legalEntityId != null && eventid != null)
				companydata.put(eventid, legalEntityId);
		}
		for (Event x : eventdata) {
			if (companydata.containsKey(x.getEventid()))
				x.setCompanyLegalUnit(companydata.get(x.getEventid()));
		}
	}
}

