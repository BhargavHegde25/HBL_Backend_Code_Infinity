package com.kony.adminconsole.service.customermanagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class CustomerSearchByPartyMs implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {

		Result resultPS = null;
		try {
			Map<String, String> postParametersMap = new HashMap<>();
			Result emptyResult = new Result();
			Result dbxRes = null;

			emptyResult = constructEmptyResut(emptyResult);
			if (true) {
				dbxRes = CustomerSearch.CustomerSearchByUserName(request);
			}
			/*
			 * if (dbxRes.getDatasetById("records").getAllRecords().size() == 1) { String id
			 * = getCustomerIdfromResult(dbxRes); postParametersMap.put("partyId", id);
			 * String endPointResponse =
			 * Executor.invokeService(ServiceURLEnum.CUSTOMER_SEARCH_PARTY_MS,
			 * postParametersMap, null, request); if (endPointResponse != null) { JSONObject
			 * serviceResponseJSON =
			 * CommonUtilities.getStringAsJSONObject(endPointResponse); Record record = new
			 * Record(); JSONObject customerBasicInfoViewJSON = serviceResponseJSON
			 * .getJSONObject("customerbasicinfo_view"); record =
			 * CommonUtilities.constructRecordFromJSONObject(customerBasicInfoViewJSON);
			 * record.setId("customerbasicinfo_view"); dbxRes.addRecord(record); JSONObject
			 * ConfigurationJSON = serviceResponseJSON.getJSONObject("Configuration");
			 * Record recordConf = new Record(); recordConf =
			 * CommonUtilities.constructRecordFromJSONObject(ConfigurationJSON);
			 * recordConf.setId("Configuration"); dbxRes.addRecord(recordConf); } }
			 */ else if (request.getParameter("_companyId") != null) {
				dbxRes = CustomerSearch.CustomerSearchByUserName(request);
			}
			/*
			 * if (dbxRes.getDatasetById("records").getAllRecords().size() == 1) { String id
			 * = getCustomerIdfromResult(dbxRes); postParametersMap.put("partyId", id);
			 * String endPointResponse =
			 * Executor.invokeService(ServiceURLEnum.CUSTOMER_SEARCH_PARTY_MS,
			 * postParametersMap, null, request); if (endPointResponse != null) { JSONObject
			 * serviceResponseJSON =
			 * CommonUtilities.getStringAsJSONObject(endPointResponse); Record record = new
			 * Record(); JSONObject customerBasicInfoViewJSON = serviceResponseJSON
			 * .getJSONObject("customerbasicinfo_view"); record =
			 * CommonUtilities.constructRecordFromJSONObject(customerBasicInfoViewJSON);
			 * record.setId("customerbasicinfo_view"); dbxRes.addRecord(record); JSONObject
			 * ConfigurationJSON = serviceResponseJSON.getJSONObject("Configuration");
			 * Record recordConf = new Record(); recordConf =
			 * CommonUtilities.constructRecordFromJSONObject(ConfigurationJSON);
			 * recordConf.setId("Configuration"); dbxRes.addRecord(recordConf); } }
			 */

			// PartyMs
			String username = request.getParameter("_username");
			String id = request.getParameter("_id");
			String accountId = request.getParameter("_cardorAccountnumber");
			String partyId = StringUtils.EMPTY;
			String CustomerIdfromApp = StringUtils.EMPTY;
			String accountPartyId = StringUtils.EMPTY;
			;
			String appid = request.getParameter("_applicationId");
			if (appid != null) {
				CustomerIdfromApp = getCustomerIdfromApp(appid, request);
				if (CustomerIdfromApp != null) {
					CustomerIdfromApp = getPartyId(CustomerIdfromApp, request);
				} else {
					return emptyResult;
				}
			}
			if (accountId != null) {
				accountPartyId = getPartyIdFromAccounId(accountId, request);
			}

			if (username != null) {

				String Customer_id = getCustomerId(username, request);
				if (Customer_id == null) {
					return emptyResult;
				}
				if (Customer_id != null) {
					partyId = getPartyId(Customer_id, request);
				}
			}

			// Checking with application Id
			if (StringUtils.isBlank(id) && StringUtils.isNotBlank(CustomerIdfromApp)) {
				id = CustomerIdfromApp;
			} else {
				if (StringUtils.isNotBlank(id) && !id.equalsIgnoreCase(CustomerIdfromApp)
						&& StringUtils.isNotBlank(CustomerIdfromApp)) {
					return emptyResult;
				}
			}

			// Checking with account number
			if (StringUtils.isBlank(id) && StringUtils.isNotBlank(accountPartyId)) {
				id = accountPartyId;
			} else {
				if (StringUtils.isNotBlank(id) && !id.equalsIgnoreCase(accountPartyId)
						&& StringUtils.isNotBlank(accountPartyId)) {
					return emptyResult;
				}
			}

			// Checking with the user name
			if (StringUtils.isBlank(id) && StringUtils.isNotBlank(partyId)) {
				id = partyId;
			} else {
				if (StringUtils.isNotBlank(id) && !id.equalsIgnoreCase(partyId) && StringUtils.isNotBlank(partyId)) {
					return emptyResult;
				}
			}

			String email = request.getParameter("_email");
			String phone = request.getParameter("_phone");
			String Tin = request.getParameter("_TIN");
			String SSN = request.getParameter("_SSN");
			String IdType = request.getParameter("_IDType");
			String IdValue = request.getParameter("_IDValue");
			String name = request.getParameter("_name");
			if (id != null || email != null || phone != null || Tin != null || SSN != null || IdType != null
					|| IdValue != null || name != null) {
				postParametersMap.put("_id", id);
				postParametersMap.put("_phone", phone);
				postParametersMap.put("_email", email);
				postParametersMap.put("_TIN", Tin);
				postParametersMap.put("_SSN", SSN);
				postParametersMap.put("_pageOffset", request.getParameter("_pageOffset"));
				postParametersMap.put("_pageSize", request.getParameter("_pageSize"));
				postParametersMap.put("_sortVariable", request.getParameter("_sortVariable"));
				postParametersMap.put("_sortDirection", request.getParameter("_sortDirection"));
				postParametersMap.put("_searchType", request.getParameter("_searchType"));
				postParametersMap.put("_IDType", IdType);
				postParametersMap.put("_IDValue", IdValue);
				postParametersMap.put("_name", name);

				String serviceResponse = Executor.invokeService(ServiceURLEnum.CUSTOMER_SEARCH_BY_ID, postParametersMap,
						null, request);
				if (serviceResponse != null) {
					JSONObject serviceResponseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);
					resultPS = CommonUtilities.getResultObjectFromJSONObject(serviceResponseJSON);
				}

			}
			if (resultPS != null) {
				return resultPS;
			} else if (resultPS == null && dbxRes != null) {
				return dbxRes;

			} else if (resultPS == null && dbxRes == null) {
				return emptyResult;
			} else {
				// fetch partyIds
				ArrayList<String> partyMs = getPartyIds(resultPS);
				ArrayList<String> dbxPartyId = getPartyIds(dbxRes);

				// find commonIds
				partyMs.retainAll(dbxPartyId);

				// remove unwanted records from result
				Dataset resultPSDS = resultPS.getDatasetById("records");
				List<Record> records = resultPSDS.getAllRecords();
				for (Record record : records) {
					String PartyId = record.getParamValueByName("partyId");
					if (!partyMs.contains(PartyId)) {
						records.remove(record);
					}
				}
				return resultPS;
			}
		} catch (Exception e) {
			resultPS.addParam(new Param("FailureReason", e.getMessage()));
			ErrorCodeEnum.ERR_20001.setErrorCode(resultPS);
			return resultPS;
		}
	}

	private String getCustomerIdfromApp(String appid, DataControllerRequest request) {

		Map<String, String> postParamsMap = new HashMap<>();
		postParamsMap.put(ODataQueryConstants.FILTER, "ApplicationId eq '" + appid + "'");
		postParamsMap.put(ODataQueryConstants.SELECT, "Customer_id");

		String readBackendIdentifierResponse = Executor.invokeService(ServiceURLEnum.CUSTOMERAPPLICATION_READ,
				postParamsMap, null, request);
		JSONObject serviceResponseJSON = CommonUtilities.getStringAsJSONObject(readBackendIdentifierResponse);

		JSONArray serviceResponseArray = serviceResponseJSON.optJSONArray("customerapplication");
		if (serviceResponseArray.length() != 0) {
			String id = serviceResponseArray.optJSONObject(0).optString("Customer_id");
			return id;
		}

		return null;
	}

	private String getCustomerIdfromResult(Result dbxRes) {
		Dataset ds = dbxRes.getDatasetById("records");
		List<Record> records = ds.getAllRecords();
		for (Record record : records) {
			String id = record.getParamValueByName("partyId");
			return id;
		}
		return null;
	}

	private Result updatePartyId(Result dbxRes, DataControllerRequest request) {

		Dataset recordDS = dbxRes.getDatasetById("records");
		if (recordDS != null) {
			int i = 0;
			List<Record> recordList = recordDS.getAllRecords();
			for (Record record : recordList) {
				String customerId = record.getParamValueByName("id");
				if (customerId != null) {
					String partyId = getPartyId(customerId, request);
					record.addParam("partyId", partyId);
				}
			}
			return dbxRes;
		}
		return null;
	}

	private ArrayList<String> getPartyIds(Result result) {
		ArrayList<String> al = new ArrayList<String>();
		Dataset recordDS = result.getDatasetById("records");
		if (recordDS != null) {
			List<Record> recordList = recordDS.getAllRecords();
			for (Record record : recordList) {
				al.add(record.getParamValueByName("partyId"));
			}
		}
		return al;
	}

	private Result constructEmptyResut(Result emptyResult) {

		emptyResult.addParam("Status", "Records returned: 0");
		emptyResult.addParam("TotalResultsFound", "0");
		Record record = new Record();
		record.setId("records");
		emptyResult.addRecord(record);
		emptyResult.addParam("SortVariable", "name");
		emptyResult.addParam("SortDirection", "ASC");
		return emptyResult;
	}

	private String getPartyId(String Customer_id, DataControllerRequest requestInstance) {
		Map<String, String> postParamsMap = new HashMap<>();
		postParamsMap.put(ODataQueryConstants.FILTER, "Customer_id eq '" + Customer_id + "'");
		postParamsMap.put(ODataQueryConstants.SELECT, "BackendId");

		String readBackendIdentifierResponse = Executor.invokeService(ServiceURLEnum.BACKENDIDENTIFIER_READ,
				postParamsMap, null, requestInstance);
		JSONObject serviceResponseJSON = CommonUtilities.getStringAsJSONObject(readBackendIdentifierResponse);

		JSONArray serviceResponseArray = serviceResponseJSON.optJSONArray("backendidentifier");
		if (serviceResponseArray.length() != 0) {
			String id = serviceResponseArray.optJSONObject(0).optString("BackendId");
			return id;
		}

		return null;
	}

	private String getCustomerId(String username, DataControllerRequest requestInstance) {

		Map<String, String> postParamsMap = new HashMap<>();
		postParamsMap.put(ODataQueryConstants.FILTER, "UserName eq '" + username + "'");
		postParamsMap.put(ODataQueryConstants.SELECT, "id");

		String serviceResponse = Executor.invokeService(ServiceURLEnum.CUSTOMER_READ, postParamsMap, null,
				requestInstance);
		JSONObject serviceResponseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);

		JSONArray serviceResponseArray = serviceResponseJSON.optJSONArray("customer");
		if (serviceResponseArray.length() != 0) {
			String Username = serviceResponseArray.optJSONObject(0).optString("id");
			return Username;
		}
		return null;
	}

	private String getPartyIdFromAccounId(String accountId, DataControllerRequest request) {
		String partyId = StringUtils.EMPTY;
		Map<String, String> postParametersMap = new HashMap<>();
		postParametersMap.put("accountId", accountId);
		String serviceResponse = Executor.invokeService(ServiceURLEnum.ARRANGEMENT_DETAILS, postParametersMap, null,
				request);
		JSONObject serviceJson = CommonUtilities.getStringAsJSONObject(serviceResponse);
		JSONArray readResponseJSONArray = serviceJson.optJSONArray("roles");

		for (int indexVar = 0; indexVar < readResponseJSONArray.length(); indexVar++) {
			JSONObject currJSONObject = readResponseJSONArray.getJSONObject(indexVar);
			if (currJSONObject.length() != 0) {
				for (String currKey : currJSONObject.keySet()) {
					if (currJSONObject.has(currKey) && currKey.equalsIgnoreCase("partyId")) {
						partyId = currJSONObject.getString("partyId");
					}
				}
			}
		}
		return partyId;
	}
}
