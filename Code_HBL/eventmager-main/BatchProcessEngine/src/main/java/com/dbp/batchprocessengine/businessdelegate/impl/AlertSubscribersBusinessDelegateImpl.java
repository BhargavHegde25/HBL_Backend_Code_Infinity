package com.dbp.batchprocessengine.businessdelegate.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.dbp.batchprocessengine.businessdelegate.api.AlertSubscribersBusinessDelegate;
import com.dbp.batchprocessengine.dto.AlertSubtypeDTO;
import com.dbp.batchprocessengine.dto.EntitlementDTO;
import com.dbp.batchprocessengine.utils.BatchProcessEngineConstants;
import com.dbp.batchprocessengine.utils.BatchProcessEngineHelperMethods;
import com.dbp.batchprocessengine.utils.BatchProcessingEngineDBConstants;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

public class AlertSubscribersBusinessDelegateImpl implements AlertSubscribersBusinessDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	@Override
	public JsonArray getSubscribers(String alerttypes, String corecustids) {

		JsonArray finalres = new JsonArray();
		try {
			if (BatchProcessEngineHelperMethods.getSchemaname() == null)
				BatchProcessEngineHelperMethods.setSchemaname(
						BatchProcessEngineHelperMethods.getConfigProperty(BatchProcessEngineConstants.DBX_SCHEMA_NAME));
		} catch (Exception e) {
			alert.prepareError("Error in fetchning " + BatchProcessEngineConstants.DBX_SCHEMA_NAME, e).log();
			return finalres;
		}
		Map<String, AlertSubtypeDTO> subtypepreferences = getAlertSubtypePreferences(alerttypes);
		boolean isglobalexists = false;
		boolean isnonglobalexists = false;
		Set<Entry<String, AlertSubtypeDTO>> entryset = subtypepreferences.entrySet();
		for (Entry<String, AlertSubtypeDTO> entry : entryset) {
			if (entry.getValue().isIsglobal())
				isglobalexists = true;
			else
				isnonglobalexists = true;
		}
		Map<String, Set<String>> allcoretodbxmap = new HashMap<>();
		if (isglobalexists) {
			allcoretodbxmap = loadAllCustInfo();
		}
		finalres = generateGlobalPayload(subtypepreferences, allcoretodbxmap);
		if (isnonglobalexists) {
			Set<String> dbxcustids = new HashSet<>();
			if (corecustids != null) {
				if (!allcoretodbxmap.isEmpty()) {
					String[] corecustidsarr = corecustids.split(",");
					for (String coreid : corecustidsarr) {
						if (allcoretodbxmap.containsKey(coreid))
							dbxcustids.addAll(allcoretodbxmap.get(coreid));
					}
				} else {
					dbxcustids = getDbxCustidsFromCore(corecustids);
				}
			}
			Map<String, List<EntitlementDTO>> entitlements = getCustomerEntitlements(alerttypes, dbxcustids);
			if (dbxcustids.isEmpty()) {
				dbxcustids = getCustomerDataFromEntitleMents(entitlements);
			}
			Map<String, Set<String>> dbxtocoremap = getCoreCustDataFromDbxIds(dbxcustids);

			addNonGlobalSubtypesToResult(entitlements, subtypepreferences, finalres, generateSetFromString(corecustids),
					dbxtocoremap);
		}

		return finalres;
	}

	private Set<String> generateSetFromString(String data) {
		Set<String> res = new HashSet<>();
		if (data == null)
			return res;
		String[] corecustidsarr = data.split(",");
		res.addAll(Arrays.asList(corecustidsarr));
		return res;

	}

	private Map<String, Set<String>> getCoreCustDataFromDbxIds(Set<String> dbxcustids) {

		Map<String, Set<String>> dbxtocoremap = new HashMap<>();
		Map<String, Object> requestParameters = new HashMap<>();
		if (dbxcustids != null && !dbxcustids.isEmpty()) {
			requestParameters.put("_dbxids", dbxcustids.stream().collect(Collectors.joining(",")));
		}
		String coretype = null;
		String operationid = null;
		try {
			coretype = BatchProcessEngineHelperMethods
					.getConfigProperty(BatchProcessEngineConstants.SUBSCRIBER_CORE_TYPE);
		} catch (Exception e1) {
			diagnostic.prepareDebug("Error in getting " + BatchProcessEngineConstants.SUBSCRIBER_CORE_TYPE, e1).log();
		}
		if (coretype != null) {
			requestParameters.put("_coretype", coretype);
			operationid = BatchProcessEngineHelperMethods.replaceSchemaName(
					BatchProcessingEngineDBConstants.SUBSCRIBER_GETCOREIDFROMDBXIDS_CORESPECIFIC,
					BatchProcessEngineHelperMethods.getSchemaname());

		} else {
			operationid = BatchProcessEngineHelperMethods.replaceSchemaName(
					BatchProcessingEngineDBConstants.SUBSCRIBER_GETCOREIDFROMDBXIDS,
					BatchProcessEngineHelperMethods.getSchemaname());
		}

		diagnostic.prepareDebug(requestParameters.toString()).log();
		Result responce = null;
		// _backendids
		try {
			responce = DBPServiceExecutorBuilder.builder().withOperationId(operationid)
					.withRequestParameters(requestParameters).withServiceId(BatchProcessEngineConstants.BATCHDBSERVICE)
					.build().getResult();
			if (responce == null)
				return dbxtocoremap;
			Dataset ds = responce.getDatasetById(BatchProcessEngineConstants.RECORDS);
			if (ds == null)
				return dbxtocoremap;

			List<Record> records = ds.getAllRecords();
			if (records == null)
				return dbxtocoremap;
			for (Record rec : records) {
				String dbxcustid = rec.getParamValueByName(BatchProcessEngineConstants.CUSTOMER_ID);
				String corecustid = rec.getParamValueByName(BatchProcessEngineConstants.BACKENDID);
				if (dbxtocoremap.containsKey(dbxcustid)) {
					dbxtocoremap.get(dbxcustid).add(corecustid);
				} else {
					Set<String> corecustset = new HashSet<>();
					corecustset.add(corecustid);
					dbxtocoremap.put(dbxcustid, corecustset);
				}
			}
		} catch (Exception e) {
			alert.prepareError("Error occured in reading core customer data: ", e).log();
		}
		return dbxtocoremap;
	}

	private Set<String> getCustomerDataFromEntitleMents(Map<String, List<EntitlementDTO>> entitlements) {
		Set<String> dbxcustids = new HashSet<>();

		Set<Entry<String, List<EntitlementDTO>>> entryset = entitlements.entrySet();
		for (Entry<String, List<EntitlementDTO>> entry : entryset) {
			List<EntitlementDTO> entitlement = entry.getValue();
			for (EntitlementDTO e : entitlement) {
				if (e.getCustomerid() != null)
					dbxcustids.add(e.getCustomerid());
			}
		}
		return dbxcustids;
	}

	private void addNonGlobalSubtypesToResult(Map<String, List<EntitlementDTO>> entitlements,
			Map<String, AlertSubtypeDTO> subtypepreferences, JsonArray finalres, Set<String> coreinput,
			Map<String, Set<String>> dbxtocoremap) {
		Set<Entry<String, AlertSubtypeDTO>> entryset = subtypepreferences.entrySet();
		for (Entry<String, AlertSubtypeDTO> entry : entryset) {
			if (entitlements.containsKey(entry.getKey()))
				processNonGlobalSubtype(entry.getValue(), entitlements.get(entry.getKey()), finalres, coreinput,
						dbxtocoremap);
		}
	}

	private JsonObject generateJsonPayload(String dbxid, AlertSubtypeDTO subtypedto, EntitlementDTO entitle) {
		JsonObject js = new JsonObject();
		js.addProperty("customerId", dbxid);
		js.addProperty("attributeId", subtypedto.getAttributeid());
		js.addProperty("alertConditionId", subtypedto.getAlertconditionid());
		if (entitle.getAccountid() != null && !entitle.getAccountid().equals("*"))
			js.addProperty("accountId", entitle.getAccountid());
		if (entitle.getAccounttype() != null && !entitle.getAccounttype().equals("*"))
			js.addProperty("accountTypeId", entitle.getAccounttype());
		if (entitle.getValue1() != null)
			js.addProperty("value1", entitle.getValue1());
		if (entitle.getValue2() != null)
			js.addProperty("value2", entitle.getValue2());
		if (entitle.getCompanyLegalUnit() != null )
			js.addProperty("companyLegalUnit", entitle.getCompanyLegalUnit());
		return js;
	}

	private void addToResultSet(Set<String> coreids, Map<String, JsonArray> resset, String dbxid,
			AlertSubtypeDTO subtypedto, EntitlementDTO entitle, Set<String> coreinput) {
		boolean coreinputempty = false;
		if (coreinput.isEmpty())
			coreinputempty = true;
		if (coreids == null)
			return;
		for (String coreid : coreids) {
			if (coreinputempty || coreinput.contains(coreid)) {
				if (resset.containsKey(coreid)) {
					JsonArray res = resset.get(coreid);
					JsonObject js = generateJsonPayload(dbxid, subtypedto, entitle);
					res.add(js);
				} else {
					JsonArray res = new JsonArray();
					JsonObject js = generateJsonPayload(dbxid, subtypedto, entitle);
					res.add(js);
					resset.put(coreid, res);
				}
			}
		}
	}

	private void processNonGlobalSubtype(AlertSubtypeDTO subtypedto, List<EntitlementDTO> subtypeallentitlements,
			JsonArray finalres, Set<String> coreinput, Map<String, Set<String>> dbxtocoremap) {

		if (subtypedto == null || subtypeallentitlements == null)
			return;

		JsonArray coreCustomers = new JsonArray();

		Map<String, JsonArray> resset = new HashMap<>();

		for (EntitlementDTO entitle : subtypeallentitlements) {
			String dbxid = entitle.getCustomerid();
			Set<String> coreids = dbxtocoremap.get(dbxid);
			addToResultSet(coreids, resset, dbxid, subtypedto, entitle, coreinput);
		}
		if (resset.isEmpty())
			return;
		JsonObject subtypejson = new JsonObject();
		subtypejson.addProperty("AlertType", subtypedto.getAlerttypeid());
		subtypejson.addProperty("AlertSubType", subtypedto.getAlertsubtypeid());
		Set<Entry<String, JsonArray>> entryset = resset.entrySet();
		for (Entry<String, JsonArray> entry : entryset) {
			JsonObject onecore = new JsonObject();
			onecore.addProperty("coreCustomerId", entry.getKey());
			onecore.add("attributes", entry.getValue());
			coreCustomers.add(onecore);
		}
		subtypejson.add("coreCustomers", coreCustomers);
		finalres.add(subtypejson);
	}

	private Map<String, List<EntitlementDTO>> getCustomerEntitlements(String alerttypes, Set<String> dbxcustids) {
		Map<String, List<EntitlementDTO>> res = new HashMap<>();
		Map<String, Object> requestParameters = new HashMap<>();
		String operationid = BatchProcessEngineHelperMethods.replaceSchemaName(
				BatchProcessingEngineDBConstants.SUBSCRIBER_GETENTITLEMENTSNOCUSTOMER,
				BatchProcessEngineHelperMethods.getSchemaname());
		if (dbxcustids != null && !dbxcustids.isEmpty()) {
			requestParameters.put("custids", dbxcustids.stream().collect(Collectors.joining(",")));
			operationid = BatchProcessEngineHelperMethods.replaceSchemaName(
					BatchProcessingEngineDBConstants.SUBSCRIBER_GETENTITLEMENTSWITHCUSTOMER,
					BatchProcessEngineHelperMethods.getSchemaname());
		}

		requestParameters.put("alerttypes", alerttypes);

		Result responce = null;
		try {
			responce = DBPServiceExecutorBuilder.builder().withOperationId(operationid)
					.withRequestParameters(requestParameters).withServiceId(BatchProcessEngineConstants.BATCHDBSERVICE)
					.build().getResult();
			if (responce == null)
				return res;
			Dataset ds = responce.getDatasetById(BatchProcessEngineConstants.RECORDS);
			if (ds == null)
				return res;
			List<EntitlementDTO> list = JSONUtils.parseAsList(ResultToJSON.convertDataset(ds).toString(),
					EntitlementDTO.class);
			for (EntitlementDTO e : list) {
				if (res.containsKey(e.getAlertsubtypeid())) {
					res.get(e.getAlertsubtypeid()).add(e);
				} else {
					List<EntitlementDTO> entlist = new ArrayList<>();
					entlist.add(e);
					res.put(e.getAlertsubtypeid(), entlist);
				}
			}
		} catch (Exception e) {
			alert.prepareError("Error occured in reading ", e).log();
		}
		return res;

	}

	private Set<String> getDbxCustidsFromCore(String corecustids) {
		Set<String> dbxids = new HashSet<>();

		Map<String, Object> requestParameters = new HashMap<>();
		requestParameters.put("_backendids", corecustids);
		Result responce = null;
		// _backendids
		try {
			responce = DBPServiceExecutorBuilder.builder()
					.withOperationId(BatchProcessEngineHelperMethods.replaceSchemaName(
							BatchProcessingEngineDBConstants.SUBSCRIBER_GETCUSTIDFROMCORE,
							BatchProcessEngineHelperMethods.getSchemaname()))
					.withRequestParameters(requestParameters).withServiceId(BatchProcessEngineConstants.BATCHDBSERVICE)
					.build().getResult();
			if (responce == null)
				return dbxids;
			Dataset ds = responce.getDatasetById(BatchProcessEngineConstants.RECORDS);
			if (ds == null)
				return dbxids;

			List<Record> records = ds.getAllRecords();
			if (records == null)
				return dbxids;
			for (Record rec : records) {
				String dbxcustid = rec.getParamValueByName(BatchProcessEngineConstants.CUSTOMER_ID);
				if (dbxcustid != null)
					dbxids.add(dbxcustid);
			}
		} catch (Exception e) {
			alert.prepareError("Error occured in reading ", e).log();
		}
		return dbxids;

	}

	private JsonArray generateGlobalPayload(Map<String, AlertSubtypeDTO> subtypepreferences,
			Map<String, Set<String>> custdata) {

		JsonArray arr = new JsonArray();
		Set<Entry<String, AlertSubtypeDTO>> entryset = subtypepreferences.entrySet();
		for (Entry<String, AlertSubtypeDTO> entry : entryset) {
			AlertSubtypeDTO subtypedto = entry.getValue();
			if (!subtypedto.isIsglobal())
				continue;
			JsonObject js = new JsonObject();

			JsonArray corecustomers = new JsonArray();

			Set<Entry<String, Set<String>>> custdataentryset = custdata.entrySet();
			for (Entry<String, Set<String>> custdataentry : custdataentryset) {
				JsonObject corecustidobj = new JsonObject();
				corecustidobj.addProperty("coreCustomerId", custdataentry.getKey());
				Set<String> dbxids = custdataentry.getValue();
				JsonArray attributesobj = new JsonArray();
				for (String dbxid : dbxids) {
					JsonObject attribute = generateGlobalPayload(dbxid, subtypedto);
					attributesobj.add(attribute);
				}
				corecustidobj.add("attributes", attributesobj);
				corecustomers.add(corecustidobj);
			}
			if (corecustomers.size() != 0) {
				js.add("coreCustomers", corecustomers);
				js.addProperty("AlertSubType", subtypedto.getAlertsubtypeid());
				js.addProperty("AlertType", subtypedto.getAlerttypeid());
				arr.add(js);
			}
		}
		return arr;

	}

	private JsonObject generateGlobalPayload(String dbxid, AlertSubtypeDTO subtypedto) {
		JsonObject attribute = new JsonObject();
		attribute.addProperty("customerId", dbxid);
		if (subtypedto.getAttributeid() != null)
			attribute.addProperty("attributeId", subtypedto.getAttributeid());
		if (subtypedto.getAlertconditionid() != null)
			attribute.addProperty("alertConditionId", subtypedto.getAlertconditionid());
		if (subtypedto.getValue1() != null)
			attribute.addProperty("value1", subtypedto.getValue1());
		if (subtypedto.getValue2() != null)
			attribute.addProperty("value2", subtypedto.getValue2());
		return attribute;
	}

	private Map<String, Set<String>> loadAllCustInfo() {

		Map<String, Set<String>> res = new HashMap<>();
		Map<String, Object> requestParameters = new HashMap<>();
		String coretype = null;
		try {
			coretype = BatchProcessEngineHelperMethods
					.getConfigProperty(BatchProcessEngineConstants.SUBSCRIBER_CORE_TYPE);
		} catch (Exception e1) {
			diagnostic.prepareDebug("Error in getting " + BatchProcessEngineConstants.SUBSCRIBER_CORE_TYPE, e1).log();
		}
		if (coretype == null)
			requestParameters.put("$filter", "BackendType eq null");
		else
			requestParameters.put("$filter", "BackendType eq " + coretype);

		Result responce = null;
		try {
			responce = DBPServiceExecutorBuilder.builder()
					.withOperationId(BatchProcessEngineHelperMethods.replaceSchemaName(
							BatchProcessingEngineDBConstants.BACKENDIDENTIFIER_GET,
							BatchProcessEngineHelperMethods.getSchemaname()))
					.withRequestParameters(requestParameters).withServiceId(BatchProcessEngineConstants.BATCHDBSERVICE)
					.build().getResult();
			if (responce == null)
				return res;
			Dataset ds = responce.getDatasetById("backendidentifier");
			if (ds == null)
				return res;

			List<Record> records = ds.getAllRecords();
			if (records == null)
				return res;
			for (Record rec : records) {
				String coreid = rec.getParamValueByName(BatchProcessEngineConstants.BACKENDID);
				String dbxcustid = rec.getParamValueByName(BatchProcessEngineConstants.CUSTOMER_ID);
				if (coreid != null && dbxcustid != null) {
					if (res.containsKey(coreid))
						res.get(coreid).add(dbxcustid);
					else {
						Set<String> dbxids = new HashSet<>();
						dbxids.add(dbxcustid);
						res.put(coreid, dbxids);
					}
				}
			}
		} catch (Exception e) {
			alert.prepareError("Error occured in reading subtype preferences", e).log();
		}
		return res;
	}

	private Map<String, AlertSubtypeDTO> getAlertSubtypePreferences(String alerttypes) {
		Map<String, AlertSubtypeDTO> res = new HashMap<>();
		Map<String, Object> requestParameters = new HashMap<>();
		requestParameters.put("alerttypes", alerttypes);
		Result responce = null;

		try {
			responce = DBPServiceExecutorBuilder.builder()
					.withOperationId(BatchProcessEngineHelperMethods.replaceSchemaName(
							BatchProcessingEngineDBConstants.SUBSCRIBER_GETALERTSUBTYPEPREFERENCES,
							BatchProcessEngineHelperMethods.getSchemaname()))
					.withRequestParameters(requestParameters).withServiceId(BatchProcessEngineConstants.BATCHDBSERVICE)
					.build().getResult();
			if (responce == null)
				return res;
			Dataset ds = responce.getDatasetById(BatchProcessEngineConstants.RECORDS);
			if (ds == null)
				return res;
			List<AlertSubtypeDTO> list = JSONUtils.parseAsList(ResultToJSON.convertDataset(ds).toString(),
					AlertSubtypeDTO.class);
			for (AlertSubtypeDTO obj : list) {
				res.put(obj.getAlertsubtypeid(), obj);
			}
		} catch (Exception e) {
			alert.prepareError("Error occured in reading subtype preferences", e).log();
		}
		return res;
	}
}
