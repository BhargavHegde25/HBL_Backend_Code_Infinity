package com.temenos.infinity.api.wealthservices.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.nimbusds.jose.JWSAlgorithm;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.temenos.infinity.api.commons.invocation.Executor;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.config.ServerConfigurations;
import com.temenos.infinity.api.wealthservices.config.PortfolioWealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.constants.TemenosErrorConstants;
import com.temenos.infinity.transact.tokenmanager.dto.BackendCertificate;
import com.temenos.infinity.wealth.common.util.UserAttributesUtil;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.URLConstants;

public class PortfolioWealthUtils {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	public static BackendCertificate getCertFromDB(String backendName) {
		BackendCertificate backendCertificate = new BackendCertificate();
		Map<String, Object> inputMap = new HashMap<>();
		StringBuffer queryString = new StringBuffer();
		queryString.append("BackendName" + " ");
		queryString.append("eq" + " ");
		queryString.append(backendName);

		inputMap.put("$filter", queryString.toString());
		String backendCertResponse = null;
		try {
			backendCertResponse = Executor.invokeService(PortfolioWealthAPIServices.SERVICE_BACKEND_CERTIFICATE,
					inputMap, null);
			diagnostic.prepareDebug("SuryaaBC" + backendCertResponse).log();
		} catch (Exception e1) {
			alert.prepareError("Service call to dbxdb failed", e1.toString()).log();
		}
		JSONObject serviceResponseJSON = Utilities.convertStringToJSON(backendCertResponse);
		if (serviceResponseJSON == null) {
			alert.prepareError("EmptyResponse no backend certificate for t24").log();
		}
		if (serviceResponseJSON.has("backendcertificate")
				&& serviceResponseJSON.getJSONArray("backendcertificate").length() > 0) {
			JSONObject certificateObj = serviceResponseJSON.getJSONArray("backendcertificate").getJSONObject(0);
			backendCertificate.setBackendName(certificateObj.getString("BackendName"));
			String certificateEncryptionKey = new String();
			try {
				certificateEncryptionKey = ServerConfigurations.T24_PRIVATE_ENCRYPTION_KEY.getValue();
			} catch (Exception e) {
				alert.prepareError("Couldnt parse T24_PRIVATE_ENCRYPTION_KEY from environment", e.toString()).log();

			}

			if (StringUtils.isNotBlank(certificateEncryptionKey))
				backendCertificate.setCertificateEncryptionKey(certificateEncryptionKey);
			backendCertificate.setCertificatePrivateKey(certificateObj.getString("CertPrivateKey"));
			backendCertificate.setCertificatePublicKey(certificateObj.getString("CertPublicKey"));
			backendCertificate.setGetPublicKeyServiceURL("/sample/service");
			backendCertificate.setId(certificateObj.getString("id"));
			backendCertificate.setJwsAlgorithm(JWSAlgorithm.RS256);

		}

		return backendCertificate;

	}

	public static String getUserAttributeFromIdentity(DataControllerRequest request, String attribute) {
		// TODO Auto-generated method stub
		try {
			if (request.getServicesManager() != null && request.getServicesManager().getIdentityHandler() != null) {
				Map<String, Object> userMap = request.getServicesManager().getIdentityHandler().getUserAttributes();
				if (userMap.get(attribute) != null) {
					String attributeValue = userMap.get(attribute) + "";

					return attributeValue;
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.getMessage();

		}

		return "";
	}

	public static JSONArray objectToJSONArray(Object object) {
		Object json = null;
		JSONArray jsonArray = null;
		try {
			json = new JSONTokener(object.toString()).nextValue();
		} catch (JSONException e) {
			e.getMessage();
		}
		if (json instanceof JSONArray) {
			jsonArray = (JSONArray) json;
		}
		return jsonArray;
	}

	public static void savePortfoliosIntoSession(String allPortfolios, String customerId) {
		if (StringUtils.isNotBlank(allPortfolios)) {
			MemoryManager.saveIntoCache(TemenosConstants.WEALTHPORTFOLIOS + customerId, allPortfolios);
		}
	}
	
	public static void saveCoreCustomerIdIntoSession(String coreCustomerId) {
		if (StringUtils.isNotBlank(coreCustomerId)) {
			MemoryManager.saveIntoCache(TemenosConstants.CORECUSTOMERID, coreCustomerId);
		}
	}

	public static List<String> getAllPortfoliosFromCache(DataControllerRequest request) {
		String backendId =	(String) MemoryManager.getFromCache(TemenosConstants.CORECUSTOMERID);
		//String backendId = PortfolioWealthUtils.getCustomerFromCache(request);
		String cacheKey = TemenosConstants.WEALTHPORTFOLIOS + backendId;
		String cacheValue = (String) MemoryManager.getFromCache(cacheKey);

		List<String> allportfoliosList = Arrays.asList(cacheValue.split(","));

		return allportfoliosList;

	}

	public static String getCustomerFromCache(DataControllerRequest request) {

		//String backendIdentifiers = PortfolioWealthUtils.getUserAttributeFromIdentity(request, "backendIdentifiers");
		String backendIdentifiers = null;
		try {
			backendIdentifiers = UserAttributesUtil.getBackendId(request);
		} catch (Exception e) {
			alert.prepareError("==========> Exception occurred while getting backendId: " + e.getMessage()).log();
		}
		JSONObject backendObj = Utilities.convertStringToJSON(backendIdentifiers);
		JSONArray coreVal = PortfolioWealthUtils.objectToJSONArray(backendObj.get("CORE"));
		String backendId ="";
		
		if(EnvironmentConfigurationsHandler.getValue(TemenosConstants.INF_WLTH_CORE,
				request).equalsIgnoreCase("Mock")) {
			backendId = "100777";
			return backendId;
		}
		String companyIdentifier = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(request);
		for(int i=0;i<coreVal.length();i++) {
			String companyVal = coreVal.getJSONObject(i).get("CompanyId").toString();
			if(companyVal.equals(companyIdentifier))
			{
				backendId = coreVal.getJSONObject(i).get("BackendId").toString();
			}
		}
		return backendId;

	}

	
	public static String getContractFromCache(DataControllerRequest request) throws Exception {
		Map<String, String> input = new HashMap<>();
		Map<String, String> input1 = new HashMap<>();
		Map<String, String> input2 = new HashMap<>();
		String serviceType = "";
		String userId = UserAttributesUtil.getCustomerId(request);
		String filter = TemenosConstants.PARAM_USERNAME + DBPUtilitiesConstants.EQUAL + UserAttributesUtil.getCustomerId(request);
		JSONObject resultJson = new JSONObject();
		input.put(DBPUtilitiesConstants.FILTER, filter);
		try {
		Result custResult = HelperMethods.callGetApi(request, input.get(DBPUtilitiesConstants.FILTER),
				HelperMethods.getHeaders(request), URLConstants.CUSTOMER_GET);

		if (HelperMethods.hasRecords(custResult)) {
			List<Dataset> dataset = custResult.getAllDatasets();
			List<Record> drecords = dataset.get(0).getAllRecords();
			resultJson = CommonUtils.convertRecordToJSONObject(drecords.get(0));
			String customerId = resultJson.get("id").toString();
			if (customerId != null && customerId.length() > 0) {
				String filter1 = "customerId" + DBPUtilitiesConstants.EQUAL + customerId;
				input1.put(DBPUtilitiesConstants.FILTER, filter1);
				Result contractCustResult = HelperMethods.callGetApi(request,
						input1.get(DBPUtilitiesConstants.FILTER), HelperMethods.getHeaders(request),
						URLConstants.CONTRACT_CUSTOMERS_GET);
				if (HelperMethods.hasRecords(contractCustResult)) {
					List<Dataset> dataset1 = contractCustResult.getAllDatasets();
					List<Record> drecords1 = dataset1.get(0).getAllRecords();
					resultJson = CommonUtils.convertRecordToJSONObject(drecords1.get(0));
					String contractId = resultJson.get("contractId").toString();
					if (contractId != null && contractId.length() > 0) {
						String filter2 = "id" + DBPUtilitiesConstants.EQUAL + contractId;
						input2.put(DBPUtilitiesConstants.FILTER, filter2);
						Result contractResult = HelperMethods.callGetApi(request,
								input2.get(DBPUtilitiesConstants.FILTER), HelperMethods.getHeaders(request),
								URLConstants.CONTRACT_GET);

						if (HelperMethods.hasRecords(contractResult)) {
							List<Dataset> dataset2 = contractResult.getAllDatasets();
							List<Record> drecords2 = dataset2.get(0).getAllRecords();
							resultJson = CommonUtils.convertRecordToJSONObject(drecords2.get(0));
							serviceType = resultJson.get("serviceType").toString();
						}
					}
				}
			}
		}
		}catch (Exception e) {
			alert.prepareError("==========> GetWatchlistDBPostProcessor Mock - Error: " + e.getMessage()).log();

		}
         return serviceType;
	}
	
	public static String getCompanyFromCache(DataControllerRequest request) {

		String companyIdentifier = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(request);
		if(StringUtils.isEmpty(companyIdentifier)) {
			String companyId = PortfolioWealthUtils.getUserAttributeFromIdentity(request, TemenosConstants.COMPANYID);
	        String legalEntityId = PortfolioWealthUtils.getUserAttributeFromIdentity(request, TemenosConstants.LEGALENTITYID);
	        companyIdentifier = (legalEntityId != null && !legalEntityId.equals(""))? legalEntityId : companyId;
		}
        return companyIdentifier;

	}

	public static Result UnauthorizedAccess() {

		alert.prepareError("Invalid request").log();
		JSONObject result = new JSONObject();
		result.put("status", "Failure");
		result.put("error", "Unauthorized Access");
		return Utilities.constructResultFromJSONObject(result);
	}

	public static boolean UnauthAccess() {

		alert.prepareError("Invalid request").log();
		JSONObject result = new JSONObject();
		result.put("status", "Failure");
		result.put("error", "Unauthorized Access");
		return false;
	}

	public static Result backendResponse(String serviceName, String operationName, Map<String, Object> inputMap,
			Map<String, Object> headersMap, DataControllerRequest request) {

		try {
			String createResponse = null;

			createResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(inputMap).withRequestHeaders(headersMap)
					.withDataControllerRequest(request).build().getResponse();
			JSONObject resultJSON = new JSONObject(createResponse);
			return Utilities.constructResultFromJSONObject(resultJSON);
		} catch (Exception e) {
			alert.prepareError("Error while invoking the operation" + e).log();
		}
		return null;

	}

	public static Result return_ErrorResponse(Result result, JSONArray errResponseArr, JSONArray additionalParamArr) {
		JSONObject responseJSON = new JSONObject();
		responseJSON.put("errorDetails", errResponseArr.toString());
		responseJSON.put(TemenosConstants.STATUS, TemenosConstants.FAILURE);
		responseJSON.put("opstatus", result.getParamValueByName("opstatus"));
		responseJSON.put("httpStatusCode", result.getParamValueByName("httpStatusCode"));
		responseJSON.put(TemenosErrorConstants.DBP_ERR_CODE, TemenosErrorConstants.T24_GENERIC_ERR_CODE);
		responseJSON.put(TemenosErrorConstants.DBP_ERR_MESSAGE, TemenosErrorConstants.T24_GENERIC_ERR_MESSAGE);
		responseJSON.put(TemenosErrorConstants.DBP_ERR_RESULT, TemenosErrorConstants.TRUE);

		if (additionalParamArr != null && additionalParamArr.length() > 0) {
			for (int i = 0; i < additionalParamArr.length(); i++) {
				JSONObject jsonObj = additionalParamArr.getJSONObject(i);
				if (jsonObj != null) {
					String key = jsonObj.keys().next();
					responseJSON.put(key, jsonObj.get(key));
				}
			}
		}

		result.clearDatasets();
		result.clearRecords();
		result.clearParams();
		return Utilities.constructResultFromJSONObject(responseJSON);
	}

	public static Result validateMandatoryFields(String inputParam) {
		alert.prepareError("Error:Invalid input. Mandatory fields not given").log();
		JSONObject resultJSON = new JSONObject();
		resultJSON.put("status", "Failure");
		resultJSON.put("error", "Invalid Input! " + inputParam + " is mandatory.");
		return Utilities.constructResultFromJSONObject(resultJSON);
	}

	public static boolean validate_MandatoryFields(String inputParam) {
		alert.prepareError("Error:Invalid input. Mandatory fields not given").log();
		JSONObject resultJSON = new JSONObject();
		resultJSON.put("status", "Failure");
		resultJSON.put("error", "Invalid Input! " + inputParam + " is mandatory.");
		return false;
	}

	public static Result validateFormat(String inputParam) {
		JSONObject resultJSON = new JSONObject();
		resultJSON.put("status", "Failure");
		resultJSON.put("error", inputParam + " is not in a valid format.");
		return Utilities.constructResultFromJSONObject(resultJSON);
	}

	public static boolean validateDateFormat(String dateformat, String startDate) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
		sdf.setLenient(false);
		try {
			sdf.parse(startDate);
		} catch (ParseException pe) {
			return false;
		}
		return true;
	}

	public static void saveAccountIntoSession(String allAccounts, String portfolioId) {
		if (StringUtils.isNotBlank(allAccounts)) {
			MemoryManager.saveIntoCache(TemenosConstants.WEALTHACCOUNTS + portfolioId, allAccounts);
		}
	}

	public static List<String> getAllAccountsFromCache(DataControllerRequest request) {

		String portfolioId = request.getParameter("portfolioId");

		String cacheKey = TemenosConstants.WEALTHACCOUNTS + portfolioId;
		String cacheValue = (String) MemoryManager.getFromCache(cacheKey);

		List<String> allAccounts = Arrays.asList(cacheValue.split(","));

		return allAccounts;

	}

	public ArrayList<HashMap<String, String>> returnSearch(ArrayList<HashMap<String, String>> finalList,
			String searchValue, String searchKey) {
		ArrayList<HashMap<String, String>> searchList = null;
		try {
			searchList = (ArrayList<HashMap<String, String>>) finalList.stream().filter(obj -> {
				String instruments = obj.get(searchKey).toLowerCase();
				if (instruments.contains(searchValue.toLowerCase())) {
					return true;
				}
				return false;
			}).collect(Collectors.toList());
			return searchList;
		} catch (Exception e) {
			e.getMessage();
			return searchList;
		}
	}

	public ArrayList<HashMap<String, String>> sortArray(ArrayList<HashMap<String, String>> finalList, String sortBy,
			String sortType) {

		try {

			if (sortBy.equalsIgnoreCase("currentWeight") || sortBy.equalsIgnoreCase("targetWeight")
					|| sortBy.equalsIgnoreCase("orderQty") || sortBy.equalsIgnoreCase("orderAmount")) {

				finalList.sort((a, b) -> {
					if (a.containsKey(sortBy) && b.containsKey(sortBy)
							&& a.get(sortBy).toString().equalsIgnoreCase("")) {
						return (b.get(sortBy).toString().equalsIgnoreCase("")) ? 0 : -1;
					}
					if (b.containsKey(sortBy) && b.get(sortBy).toString().equalsIgnoreCase("")) {
						return 1;
					}

					Double dbl1 = (a.containsKey(sortBy) && a.get(sortBy).toString().length() > 0)
							? Double.parseDouble((a.get(sortBy)).toString())
							: 0;
					Double dbl2 = (b.containsKey(sortBy) && b.get(sortBy).toString().length() > 0)
							? Double.parseDouble((b.get(sortBy)).toString())
							: 0;
					return dbl1.compareTo(dbl2);
				});

			} else {
				finalList.sort((h1, h2) -> h1.get(sortBy).compareToIgnoreCase(h2.get(sortBy)));
			}

			if (sortType != null && sortType.equalsIgnoreCase(TemenosConstants.DESCENDING)) {
				Collections.reverse(finalList);
			}
			return finalList;
		} catch (Exception e) {
			e.getMessage();
			return finalList;
		}

	}

	public ArrayList<HashMap<String, String>> pagination(int offset, int limit,
			ArrayList<HashMap<String, String>> finalList) {
		try {
			finalList = (ArrayList<HashMap<String, String>>) finalList.stream().skip(offset).limit(limit)
					.collect(Collectors.toList());
			return finalList;

		} catch (Exception e) {
			e.getMessage();
			return finalList;
		}
	}

	public ArrayList<HashMap<String, String>> filterDate(ArrayList<HashMap<String, String>> finalList, String startDate,
			String endDate, String key, String dateFormat) {
		SimpleDateFormat sdformat = new SimpleDateFormat(dateFormat);
		ArrayList<HashMap<String, String>> filteredList = new ArrayList<HashMap<String, String>>();

		finalList.forEach(obj -> {
			try {
				Date sdt = sdformat.parse(startDate);
				Date edt = sdformat.parse(endDate);
				Date tdt = sdformat.parse(obj.get(key).substring(0, 10));
				if (sdt.compareTo(tdt) * tdt.compareTo(edt) >= 0) {
					filteredList.add(obj);
				}
			} catch (Exception e) {
				e.getMessage();
			}
		});

		return filteredList;

	}

	public static void saveWealthCoreIntoSession(DataControllerRequest request) {
		String wealthCore = EnvironmentConfigurationsHandler.getValue(TemenosConstants.INF_WLTH_CORE, request);

		if (StringUtils.isNotBlank(wealthCore)) {
			MemoryManager.saveIntoCache(TemenosConstants.INF_WLTH_CORE, wealthCore);
		}
	}

	public static String getWealthCoreFromCache(DataControllerRequest request) {

		String cacheKey = TemenosConstants.INF_WLTH_CORE;
		String cacheValue = (String) MemoryManager.getFromCache(cacheKey);

		return cacheValue;

	}

	public static void saveTokenIntoSession(String backendToken, String companyId) {
		if (StringUtils.isNotBlank(backendToken)) {
			MemoryManager.saveIntoCache(TemenosConstants.TOKEN + companyId, backendToken);
		}
	}

	public static String getTokenFromCache(DataControllerRequest request) {

		String companyId = PortfolioWealthUtils.getUserAttributeFromIdentity(request, TemenosConstants.COMPANYID);
		String legalEntityId = PortfolioWealthUtils.getUserAttributeFromIdentity(request,
				TemenosConstants.LEGALENTITYID);
		String companyIdentifier = (legalEntityId != null && !legalEntityId.equals("")) ? legalEntityId : companyId;
		String cacheKey = TemenosConstants.TOKEN + companyIdentifier;
		String cacheValue = (String) MemoryManager.getFromCache(cacheKey);
		return cacheValue;
	}

	public static Result validateValue(String inputParam) {
		alert.prepareError("Invalid request").log();
		JSONObject resultJSON = new JSONObject();
		resultJSON.put("status", "Failure");
		resultJSON.put("error", inputParam + " value is not valid.");
		return Utilities.constructResultFromJSONObject(resultJSON);
	}

	public static Result validateInputParams() {
		alert.prepareError("Invalid request").log();
		JSONObject resultJSON = new JSONObject();
		resultJSON.put("status", "Failure");
		resultJSON.put("error", "Invalid Input Params!");
		return Utilities.constructResultFromJSONObject(resultJSON);
	}

	public static Result validateParams(String inputParam) {
		alert.prepareError("Invalid request").log();
		JSONObject resultJSON = new JSONObject();
		resultJSON.put("status", "Failure");
		resultJSON.put("error", "Invalid Input! " + inputParam + " cannot be null or empty");
		return Utilities.constructResultFromJSONObject(resultJSON);
	}

	public static Result inputValidationsForDownload(DataControllerRequest request) {
		
		return null;
	}

	public static JSONArray pagination(JSONArray sortedJSON, int limit, int offset) {

		JSONArray paginationJSON = new JSONArray();

		int j = 0;
		for (int i = offset; i < sortedJSON.length(); i++) {
			if (j == limit) {
				break;
			} else {
				paginationJSON.put(sortedJSON.get(i));
			}
			j++;
		}

		return paginationJSON;

	}
}
