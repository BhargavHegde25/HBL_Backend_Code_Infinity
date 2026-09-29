package com.dbp.batchprocessengine.businessdelegate.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.dbp.batchprocessengine.businessdelegate.api.DataUpdateJobBusinessDelegate;
import com.dbp.batchprocessengine.dto.BatchalertobjectDTO;
import com.dbp.batchprocessengine.utils.BatchProcessEngineConstants;
import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class DataUpdateJobBusinessDelegateImpl implements DataUpdateJobBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");
	static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

	@Override
	public Set<String> getSubscribers(List<String> eventtypes) {
		Map<String, Object> inputmap = new HashMap<>();

		JsonElement res = null;
		StringBuilder sb = new StringBuilder();
		JsonArray alerttypesobj;
		ListIterator<String> it = eventtypes.listIterator();
		while (it.hasNext()) {
			String s = it.next();
			if (sb.length() == 0)
				sb.append(s);
			else
				sb.append("," + s);
		}
		String responce;
		inputmap.put("alertTypes", sb.toString());
		Set<String> subscribersset = new HashSet<>();
		DataControllerRequest dc = null;
		try {
			responce = DBPServiceInvocationWrapper.invokeServiceAndGetJSON("BatchProcessingObjects", "AlertSubscribers",
					"getAlertSubscribers", inputmap, null, dc);

		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured  ", e).log();
			return subscribersset;
		}
		if (responce == null)
			return subscribersset;
		try {
			res = new JsonParser().parse(responce);
			if (res == null || res.getAsJsonObject().get(BatchProcessEngineConstants.ALERTSUBTYPES) == null)
				return subscribersset;
			JsonElement alerttypesselement = res.getAsJsonObject().get(BatchProcessEngineConstants.ALERTSUBTYPES);
			if (alerttypesselement == null || !alerttypesselement.isJsonArray())
				return subscribersset;
			alerttypesobj = alerttypesselement.getAsJsonArray();
			for (JsonElement alerttype : alerttypesobj) {
				processAlertType(alerttype, subscribersset);
			}

		} catch (Exception e) {
			alert.prepareError("Error Occured:", e).log();
		}
		diagnostic.prepareDebug("subscriber set " + subscribersset).log();
		return subscribersset;

	}

	private static void processAlertType(JsonElement alerttypejsonelem, Set<String> subscribersset) {

		if (alerttypejsonelem == null || !alerttypejsonelem.isJsonObject())
			return;

		JsonElement subscriberselem = alerttypejsonelem.getAsJsonObject()
				.get(BatchProcessEngineConstants.CORECUSTOMERS);
		if (subscriberselem == null)
			return;
		subscriberselem = new JsonParser().parse(subscriberselem.toString());
		if (subscriberselem == null || !subscriberselem.isJsonArray())
			return;
		JsonArray subscribersarry = subscriberselem.getAsJsonArray();
		for (JsonElement subscriber : subscribersarry) {
			if (!subscriber.isJsonObject())
				continue;
			JsonObject customerlevel = subscriber.getAsJsonObject();
			String coreid = customerlevel.get(BatchProcessEngineConstants.CORECUSTOMERID).getAsString();
			processCustomerAccounts(customerlevel.get(BatchProcessEngineConstants.ATTRIBUTES), subscribersset, coreid);
		}
	}

	private static void processCustomerAccounts(JsonElement accounts, Set<String> subscribersset, String customer) {
		if (!accounts.isJsonArray())
			return;
		for (JsonElement account : accounts.getAsJsonArray()) {
			if (!account.isJsonObject())
				return;
			processAccount(account.getAsJsonObject(), subscribersset, customer);
		}
	}

	private static void processAccount(JsonObject account, Set<String> subscribersset, String customer) {
		JsonElement accid = account.getAsJsonObject().get(BatchProcessEngineConstants.ACCOUNT_ID);
		JsonElement companyLegalUnit = account.getAsJsonObject().get("companyLegalUnit");
		JsonObject record = new JsonObject();
		String custid = customer;
		custid = custid.replace("\"", "");
		String companyLegalUnitStr= companyLegalUnit.toString();
		companyLegalUnitStr=companyLegalUnitStr.replace("\"", "");
		record.addProperty("customerId", custid);
		if (accid != null) {
			String acc = accid.toString();

			acc = acc.replace("\"", "");
			if (!acc.equals("")) {
				subscribersset.add(custid + "###" + acc + "###" + companyLegalUnitStr);
				record.addProperty(BatchProcessEngineConstants.ACCOUNT_ID, acc);
			} else {
				subscribersset.add(custid);
			}
		} else if(companyLegalUnitStr!=null && !companyLegalUnitStr.equals("")) {
			subscribersset.add(custid + "###" + "CMPLU"+ companyLegalUnitStr);
		}
		else {
			subscribersset.add(custid);
		}
	}

	private static String formatSyncTime(String synctime) {

		if (synctime == null || synctime.equals(""))
			return synctime;
		LocalDateTime time = LocalDateTime.parse(synctime.replace(" ", "T"));
		return dtf.format(time);

	}

	@Override
	public JsonObject callCoreService(Set<String> input, String servicetype) {
		JsonObject retval = new JsonObject();
		if (input.isEmpty())
			return retval;

		BatchalertobjectDTO batchdto = getServiceData(servicetype);
		String lastsynctime = batchdto.getLastsync();
		String operationId = batchdto.getOperation();
		if (operationId == null) {
			retval.addProperty(BatchProcessEngineConstants.DBPERRMSG, "Unable to fetch operation for this service.");
			return retval;
		}
		lastsynctime = formatSyncTime(lastsynctime);
		StringBuilder customerstr = new StringBuilder();
		StringBuilder accstr = new StringBuilder();
		StringBuilder companyLegalUnitStr = new StringBuilder();
		for (String s : input) {
			String[] arr = s.split("###");
			if (arr.length >= 1) {
				customerstr.append((customerstr.length() == 0) ? arr[0] : "," + arr[0]);
				if (arr.length >= 2) {
					if(arr[1].contains("CMPLU")) {
						companyLegalUnitStr.append((companyLegalUnitStr.length() == 0) ? arr[1].substring(5) : "," + arr[1].substring(5));
					}
					else
					accstr.append((accstr.length() == 0) ? arr[1] : "," + arr[1]);
				if(arr.length >= 3) {
					companyLegalUnitStr.append((companyLegalUnitStr.length() == 0) ? arr[2] : "," + arr[2]);
				}
				}
				else {
					accstr.append((accstr.length() == 0) ? "null" : "," + "null");
				}
			}
		}
		if (accstr.length() == 0 && customerstr.length() == 0) {
			retval.addProperty(BatchProcessEngineConstants.DBPERRMSG, "Invalid Subscribers");
			return retval;
		}
		Map<String, Object> inputmap = new HashMap<>();
		inputmap.put("loop_count", input.size());
		inputmap.put("loop_seperator", ",");
		inputmap.put("customerId", customerstr);
		inputmap.put("lastsynctime", lastsynctime);
		inputmap.put("accountId", accstr);
		inputmap.put("companyLegalUnit", companyLegalUnitStr);

		LocalDateTime currtime = LocalDateTime.now();
		inputmap.put("currenttimestamp", dtf.format(currtime));
		if (batchdto.getObjectid() != null)
			inputmap.put("objectType", batchdto.getObjectid().toUpperCase());
		diagnostic.prepareDebug("inputmap" + inputmap).log();
		DataControllerRequest dc = null;
		try {
			DBPServiceInvocationWrapper.invokeServiceAndGetJSON("BatchProcessing_Orch", null, operationId, inputmap,
					null, dc);
		} catch (Exception e) {
			diagnostic.prepareDebug("Error Occured:", e).log();
			retval.addProperty(BatchProcessEngineConstants.DBPERRMSG, e.getMessage());
			return retval;
		}
		updateLastSync(servicetype, currtime);
		return retval;
	}

	public BatchalertobjectDTO getServiceData(String servicetype) {
		BatchalertobjectDTO batchobj = new BatchalertobjectDTO();
		Result response = null;
		Map<String, Object> requestParameters = new HashMap<>();
		DataControllerRequest dc = null;
		try {

			requestParameters.put(BatchProcessEngineConstants.FILTER, "objectType eq " + servicetype.toUpperCase());
			response = DBPServiceInvocationWrapper.invokeServiceAndGetResult(BatchProcessEngineConstants.BATCHDBSERVICE,
					null, "dbxdb_batchalertobject_get", requestParameters, null, dc);

			if (response == null)
				return batchobj;
			Dataset rr = response.getDatasetById("batchalertobject");
			if (rr == null)
				return batchobj;
			List<Record> rec = rr.getAllRecords();
			if (rec == null || rec.isEmpty())
				return null;
			if (rec.get(0).getParamByName(BatchProcessEngineConstants.LASTSYNCTIMESTAMP) != null
					&& rec.get(0).getParam(BatchProcessEngineConstants.LASTSYNCTIMESTAMP).getValue() != null) {
				batchobj.setLastsync(
						rec.get(0).getParamByName(BatchProcessEngineConstants.LASTSYNCTIMESTAMP).getValue());

			}
			if (rec.get(0).getParamByName(BatchProcessEngineConstants.OPERATION_NAME) != null
					&& rec.get(0).getParam(BatchProcessEngineConstants.OPERATION_NAME).getValue() != null) {
				batchobj.setOperation(rec.get(0).getParamByName(BatchProcessEngineConstants.OPERATION_NAME).getValue());

			}
			if (rec.get(0).getParamByName(BatchProcessEngineConstants.OBJECT_TYPE) != null
					&& rec.get(0).getParam(BatchProcessEngineConstants.OBJECT_TYPE).getValue() != null) {
				batchobj.setObjectid(rec.get(0).getParamByName(BatchProcessEngineConstants.OBJECT_TYPE).getValue());

			}
		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured in fetching last sync time stamp", e).log();

		}
		return batchobj;
	}

	public void updateLastSync(String servicetype, LocalDateTime curtime) {
		Map<String, Object> requestParameters = new HashMap<>();
		requestParameters.put("objectType", servicetype.toUpperCase());
		requestParameters.put(BatchProcessEngineConstants.LASTSYNCTIMESTAMP, curtime);
		DataControllerRequest dc = null;
		try {
			DBPServiceInvocationWrapper.invokeServiceAndGetResult(BatchProcessEngineConstants.BATCHDBSERVICE, null,
					"dbxdb_batchalertobject_update", requestParameters, null, dc);

		} catch (Exception e) {
			diagnostic.prepareDebug("Error Occured", e).log();
		}
	}

	@Override
	public List<String> getEventTypes(String servicetype) {

		List<String> alerttypes = new ArrayList<>();
		Map<String, Object> requestMap = new HashMap<>();
		try {
			requestMap.put("$select", BatchProcessEngineConstants.ALERTTYPE);
			requestMap.put(BatchProcessEngineConstants.FILTER, "objectType eq " + servicetype.toUpperCase());
			DataControllerRequest dc = null;
			Result res1 = DBPServiceInvocationWrapper.invokeServiceAndGetResult(
					BatchProcessEngineConstants.BATCHDBSERVICE, null, "dbxdb_batchalertdefinition_get", requestMap,
					null, dc);
			Dataset ds = res1.getDatasetById("batchalertdefinition");
			if (ds == null)
				return alerttypes;
			List<Record> recs = ds.getAllRecords();
			if (recs == null)
				return alerttypes;
			for (Record rec : recs) {
				if (rec.getParamByName(BatchProcessEngineConstants.ALERTTYPE) != null
						&& rec.getParamByName(BatchProcessEngineConstants.ALERTTYPE).getValue() != null)
					alerttypes.add(rec.getParamByName(BatchProcessEngineConstants.ALERTTYPE).getValue());
			}
		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured", e).log();
		}
		return alerttypes;
	}
}
