package com.kony.dbputilities.util;

import java.security.SecureRandom;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Locale.LanguageRange;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.http.HttpConnector;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public final class CommonUtils {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    private CommonUtils() {
    }

    public static String getUserCompanyId(DataControllerRequest dcRequest) throws HttpCallException {

        return SessionScope.getUserCompanyId(dcRequest);
    }

    public static String getUserName(DataControllerRequest dcRequest) throws HttpCallException {

        return SessionScope.getUserName(dcRequest);

    }

    public static String getUserId(DataControllerRequest dcRequest) throws HttpCallException {

        return SessionScope.getUserId(dcRequest);
    }

    public static Map<String, String> getCompanyIdFilter(DataControllerRequest dcRequest) throws HttpCallException {

        Map<String, String> dataMap = new HashMap<>();

        String company_id = CommonUtils.getUserCompanyId(dcRequest);

        String filter = "Company_id" + DBPUtilitiesConstants.EQUAL + company_id;

        dataMap.put(DBPUtilitiesConstants.FILTER, filter);

        return dataMap;
    }

    /**
     * @param inputParams
     * @param dcr
     * @return
     * @throws Exception
     * @description Helper function for removing bbrequest template of a BB User
     */
    public static Result deleteBBRequestHelper(Map<String, String> inputParams, DataControllerRequest dcr)
            throws Exception {
        inputParams.put("softDelete", "1");
        return HelperMethods.callApi(dcr, inputParams, HelperMethods.getHeaders(dcr), URLConstants.BB_REQUEST_UPDATE);

    }

    public static String getStatusid(DataControllerRequest dcRequest, String status) throws HttpCallException {
        return SessionScope.getStatusIds(dcRequest).get(status);
    }

    public static Map<String, String> addCompanyIdFilter(DataControllerRequest dcRequest,
            Map<String, String> dataMap) throws HttpCallException {

        String company_id = null;
        String updatedFilter = "";
        company_id = getUserCompanyId(dcRequest);
        if (dataMap.containsKey(DBPUtilitiesConstants.FILTER)) {
            updatedFilter = dataMap.get(DBPUtilitiesConstants.FILTER) + DBPUtilitiesConstants.AND;
        }
        updatedFilter = updatedFilter + "Company_id" + DBPUtilitiesConstants.EQUAL + company_id;
        dataMap.put(DBPUtilitiesConstants.FILTER, updatedFilter);
        return dataMap;
    }

    public static Map<String, String> getTemplateIdFilter(Map<String, String> inputParams) {
        Map<String, String> dataMap = new HashMap<>();
        String template_id = inputParams.get("Template_id");
        String filter = "Template_id" + DBPUtilitiesConstants.EQUAL + template_id;
        dataMap.put(DBPUtilitiesConstants.FILTER, filter);
        return dataMap;
    }

    public static Map<String, String> getTransactionIdFilter(Map<String, String> inputParams) {
        Map<String, String> dataMap = new HashMap<>();
        String transaction_id = inputParams.get("Transaction_id");
        String filter = "Transaction_id" + DBPUtilitiesConstants.EQUAL + transaction_id;
        dataMap.put(DBPUtilitiesConstants.FILTER, filter);
        return dataMap;
    }

    public static String getFilterForAList(Set<String> accounts, String key) {
        String filter = " ( ";

        Iterator<String> itr = accounts.iterator();
        while (itr.hasNext()) {
            filter = filter + key + DBPUtilitiesConstants.EQUAL + itr.next() + DBPUtilitiesConstants.OR;
        }

        filter = filter + key + DBPUtilitiesConstants.EQUAL + "0" + " ) ";

        return filter;
    }

    public static HashMap<String, String> toMap(JSONObject object) throws JSONException {
        HashMap<String, String> map = new HashMap<>();

        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            map.put(key, value.toString());
        }
        return map;
    }

    public static JSONObject toJSON(HashMap<String, String> map) throws JSONException {
        JSONObject json = new JSONObject();

        Iterator<String> keysItr = map.keySet().iterator();

        while (keysItr.hasNext()) {
            String key = keysItr.next();
            String value = map.get(key);

            json.put(key, value);
        }
        return json;
    }

    public static String getTransactionType(DataControllerRequest dcRequest, String transaction_id)
            throws HttpCallException {

        HashMap<String, String> dataMap = new HashMap<>();

        String filter = "TransactionType_id" + DBPUtilitiesConstants.EQUAL + transaction_id;

        dataMap.put(DBPUtilitiesConstants.FILTER, filter);

        Result result = HelperMethods.callApi(dcRequest, dataMap, HelperMethods.getHeaders(dcRequest),
                URLConstants.BB_TRANSACTION_TYPE_GET);
        return HelperMethods.getFieldValue(result, "TransactionTypeName");

    }

    public static HashSet<String> getAllAccounts(DataControllerRequest dcRequest) throws HttpCallException {
        HashSet<String> accounts = new HashSet<>();
        String userId = getUserId(dcRequest);
        diagnostic.prepareDebug("User ID in getAllAccounts: " + userId).log();
        if (StringUtils.isNotBlank(userId)) {
            String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + userId;
            Result userResult = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CUSTOMERACCOUNTS_GET);
            List<Record> result = userResult.getAllDatasets().get(0).getAllRecords();
            diagnostic.prepareDebug("Size of Accounts in getAllAccounts: " + result.size()).log();
            for (int i = 0; i < result.size(); i++) {
                accounts.add(result.get(i).getParam("Account_id").getValue());
            }
        }

        return accounts;
    }

    /**
     *
     * @param dcRequest
     * @param inputParams
     * @return
     * @throws HttpCallException
     */
    public static Map<String, String> addBasicParamsForCreateService(DataControllerRequest dcRequest,
            Map<String, String> inputParams) throws HttpCallException {

        String company_id = CommonUtils.getUserCompanyId(dcRequest);
        diagnostic.prepareDebug("addBasicParamsForCreateService: getCompanyID: " + company_id).log();
        inputParams.put("Company_id", company_id);
        inputParams.put("createdby", CommonUtils.getUserId(dcRequest));
        inputParams.put("createdts", HelperMethods.getCurrentTimeStamp());
        inputParams.put("Status", CommonUtils.getStatusid(dcRequest, "New"));

        return inputParams;
    }

    /**
     *
     * @param dcRequest
     * @param amount
     * @return boolean
     * @throws HttpCallException
     */
    /**
     * function to check if the amount is exceeding the limit
     */

    public static Map<String, String> convertRecordToMap(Record record) {

        Map<String, String> map = new HashMap<>();

        List<Param> arList = record.getAllParams();

        Iterator<Param> it = arList.iterator();
        while (it.hasNext()) {
            Param p = it.next();
            map.put(p.getName(), p.getValue());
        }
        return map;
    }

    public static JSONObject convertRecordToJSONObject(Record record) {

        JSONObject jsonObj = new JSONObject();

        List<Param> arList = record.getAllParams();

        Iterator<Param> it = arList.iterator();
        while (it.hasNext()) {
            Param p = it.next();
            String key = p.getName();
            jsonObj.put(key, p.getValue());
        }
        return jsonObj;
    }

    public static JSONArray extractRecordsFromResult(Result templateSubRecords) {

        JSONArray subRecordsJsonArray = new JSONArray();

        if (HelperMethods.hasRecords(templateSubRecords)) {
            List<Record> subRecords = templateSubRecords.getAllDatasets().get(0).getAllRecords();

            for (int j = 0; j < subRecords.size(); j++) {
                Record subRecord = templateSubRecords.getAllDatasets().get(0).getAllRecords().get(j);
                subRecordsJsonArray.put(CommonUtils.convertRecordToJSONObject(subRecord));
            }
        }
        return subRecordsJsonArray;
    }

    public static Result invokeHTTPCall(DataControllerRequest dcRequest, Map<?, ?> inputParams,
            Map<String, String> headerParams, String url) throws HttpCallException {

        HttpConnector httpConn = new HttpConnector();
        String fullFormedURL = URLFinder.getCompleteUrl(dcRequest, URLConstants.DBP_HOST_URL, url);

        JsonObject jsonResponse = httpConn.invokeHttpPost(fullFormedURL, inputParams, headerParams);
        return (null == jsonResponse) ? new Result() : ConvertJsonToResult.convert(jsonResponse);
    }

    public static HashMap<String, String> getAllTypeMappings(DataControllerRequest dcRequest) throws HttpCallException {
        try {
            HashMap<String, String> transactions = new HashMap<>();

            Result result = HelperMethods.callApi(dcRequest, null, HelperMethods.getHeaders(dcRequest),
                    URLConstants.BB_GENERALTRANSACTION_TYPE_GET);

            List<Record> records = result.getAllDatasets().get(0).getAllRecords();
            for (int i = 0; i < records.size(); i++) {
                Record record = records.get(i);
                String serviceName = HelperMethods.getFieldValue(record, "TransactionType");
                String id = HelperMethods.getFieldValue(record, "id");
                transactions.put(serviceName.trim(), id);
            }
            return transactions;
        } catch (HttpCallException exp) {
            throw new HttpCallException("Fetching BBGeneral Transaction Types Failed");
        }
    }

    public static String getQueryForApprovals(DataControllerRequest dcr, HashMap<String, JSONObject> approveList,
            HashMap<String, JSONObject> selfApproveList, String id, String key) throws HttpCallException {

        String query = "";
        String userId = CommonUtils.getUserId(dcr);

        boolean isApproveEnabled = approveList.containsKey(id);
        boolean isSelfApproveEnabled = selfApproveList.containsKey(id);

        if (!isApproveEnabled && !isSelfApproveEnabled) {
            query = " ( " + key + DBPUtilitiesConstants.EQUAL + "0" + " ) ";
        } else if (!isApproveEnabled && isSelfApproveEnabled) {
            query = " ( " + key + DBPUtilitiesConstants.EQUAL + id + DBPUtilitiesConstants.AND + "createdby"
                    + DBPUtilitiesConstants.EQUAL + userId + " ) ";
        } else if (!isSelfApproveEnabled && isApproveEnabled) {
            query = " ( " + key + DBPUtilitiesConstants.EQUAL + id + DBPUtilitiesConstants.AND + "createdby"
                    + DBPUtilitiesConstants.NOT_EQ + userId + " ) ";
        } else {
            query = " ( " + key + DBPUtilitiesConstants.EQUAL + id + " ) ";
        }

        return query;

    }

    public static Map<String, String> addFilter(Map<String, String> inputParams, String newfilter) {

        String filter = inputParams.get(DBPUtilitiesConstants.FILTER);

        if (filter != null) {
            filter = " ( " + filter + " ) " + DBPUtilitiesConstants.AND + newfilter;
        } else {
            filter = newfilter;
        }

        inputParams.put(DBPUtilitiesConstants.FILTER, filter);

        return inputParams;
    }

    public static Map<String, String> addSoftDeleteFilter(Map<String, String> inputParams) {

        String filter = inputParams.get(DBPUtilitiesConstants.FILTER);

        if (filter != null) {
            filter = " ( " + filter + " ) " + DBPUtilitiesConstants.AND + "softDelete" + DBPUtilitiesConstants.NOT_EQ
                    + "1";
        } else {
            filter = "softDelete" + DBPUtilitiesConstants.NOT_EQ + "1";
        }

        inputParams.put(DBPUtilitiesConstants.FILTER, filter);

        return inputParams;
    }

    public static HashMap<String, String> getMyRequestsFilter(DataControllerRequest dcr) throws HttpCallException {

        HashMap<String, String> dataMap = new HashMap<>();
        String filter = "createdby" + DBPUtilitiesConstants.EQUAL + CommonUtils.getUserId(dcr)
                + DBPUtilitiesConstants.AND + "Status" + DBPUtilitiesConstants.EQUAL
                + CommonUtils.getStatusid(dcr, "Pending") + DBPUtilitiesConstants.AND + "Request_id"
                + DBPUtilitiesConstants.NOT_EQ + "null";
        dataMap.put(DBPUtilitiesConstants.FILTER, filter);

        return dataMap;
    }

    public static Result addAnEntryToActedRequest(DataControllerRequest dcr, Map<String, String> inputParams)
            throws HttpCallException {

        HashMap<String, String> dataMap1 = new HashMap<>();
        HashMap<String, String> dataMap2 = new HashMap<>();
        String action = inputParams.get("Action");

        dataMap1.put("Status", CommonUtils.getStatusid(dcr, action));
        dataMap1.put("Request_id", inputParams.get("Request_id"));
        HelperMethods.callApi(dcr, dataMap1, HelperMethods.getHeaders(dcr), URLConstants.BB_REQUEST_UPDATE);

        dataMap2.put(DBPUtilitiesConstants.FILTER,
                "Request_id" + DBPUtilitiesConstants.EQUAL + inputParams.get("Request_id"));
        Result requestEntry = HelperMethods.callApi(dcr, dataMap2, HelperMethods.getHeaders(dcr),
                URLConstants.BB_REQUEST_GET);

        Map<String, String> actedRequests = CommonUtils
                .convertRecordToMap(requestEntry.getAllDatasets().get(0).getRecord(0));
        actedRequests.put("Comments", inputParams.get("Comments"));
        actedRequests = CommonUtils.addBasicParamsForCreateService(dcr, (HashMap<String, String>) actedRequests);
        actedRequests.put("Action", inputParams.get("Action"));
        actedRequests.put("Status", CommonUtils.getStatusid(dcr, action));

        return HelperMethods.callApi(dcr, actedRequests, HelperMethods.getHeaders(dcr),
                URLConstants.BB_ACTED_REQUEST_CREATE);
    }

    public static String getMaxTranscationLimit(DataControllerRequest dcRequest, String bbGeneralTransactionType_id)
            throws HttpCallException {

        String id = bbGeneralTransactionType_id;

        if ("8".equals(id)) {
            id = "6";
        }

        HashMap<String, JSONObject> list = SessionScope.getUserCreateServices(dcRequest);

        if (list.containsKey(id)) {
            return list.get(id).getString("maxTransferLimit");
        } else {
            return "0";
        }
    }

    public static Map<String, String> addEntryToRequestsIfLimitExceeds(DataControllerRequest dcRequest,
            String maxAmount, Map<String, String> inputParams) throws HttpCallException, ParseException {

        if (CommonUtils.isMaxTransactionLimitExceeding(dcRequest, maxAmount,
                inputParams.get("BBGeneralTransactionType_id"))) {

            Map<String, String> inputForRequests = new HashMap<>();
            String typeId = inputParams.get("BBGeneralTransactionType_id");
            inputForRequests = CommonUtils.addBasicParamsForCreateService(dcRequest, inputForRequests);
            inputForRequests.put("RequestType_id", typeId);
            inputForRequests.put("Status", CommonUtils.getStatusid(dcRequest, "Pending"));

            if ("8".equals(typeId)) {
                inputForRequests.put("accountId", "NA");
            } else if ("6".equals(typeId) || "7".equals(typeId)) {
                inputForRequests.put("accountId", inputParams.get("DebitAccount"));
            } else {
                inputForRequests.put("accountId", inputParams.get("DebitOrCreditAccount"));
            }

            Result request = HelperMethods.callApi(dcRequest, inputForRequests, HelperMethods.getHeaders(dcRequest),
                    URLConstants.BB_REQUEST_CREATE);

            inputParams.put("Status", CommonUtils.getStatusid(dcRequest, "Pending"));
            inputParams.put("Request_id", HelperMethods.getFieldValue(request, "Request_id"));
        }

        return inputParams;
    }

    /**
     *
     * @param dcRequest
     * @param amount
     * @return boolean
     * @throws HttpCallException
     */
    /**
     * function to check if the amount is exceeding the limit
     */
    public static boolean isMaxTransactionLimitExceeding(DataControllerRequest dcRequest, String amount, String type_id)
            throws HttpCallException {
        String MaxLimit = getMaxTranscationLimit(dcRequest, type_id);
        return !(Double.parseDouble(amount) <= Double.parseDouble(MaxLimit));
    }

    public static JSONArray convertDatasetToJSONArray(Dataset dataset) {
        JSONArray array = new JSONArray();
        List<Record> records = new ArrayList<>();

        if (dataset != null && dataset.getAllRecords().size() != 0) {
            records = dataset.getAllRecords();
        }

        for (int i = 0; i < records.size(); i++) {
            array.put(CommonUtils.convertRecordToJSONObject(records.get(i)));
        }
        return array;
    }

    public static String getTotalAmount(JSONArray records, boolean doesSubRecordExists) {

        double totalAmount = 0.0;

        for (int i = 0; i < records.length(); i++) {

            double recordAmount = 0.0;
            JSONObject record = records.getJSONObject(i);

            if (doesSubRecordExists) {

                JSONArray subRecords = new JSONArray();
                try {
                    subRecords = record.getJSONArray("SubRecords");
                } catch (NullPointerException exp) {
                    exp.getMessage();
                } catch (JSONException exp1) {
                    exp1.getMessage();
                }

                for (int j = 0; j < subRecords.length(); j++) {
                    JSONObject subrecord = subRecords.getJSONObject(j);
                    recordAmount = recordAmount + Double.parseDouble(subrecord.getString("Amount"));
                }
            } else {
                recordAmount = Double.parseDouble(record.getString("Amount"));
            }

            totalAmount = totalAmount + recordAmount;
        }

        return totalAmount + "";
    }
    
    public static String formatLanguageIdentifier(String languageIdentifier) {
        diagnostic.prepareDebug("Formatting Language Identifier. Recieved Value:" + languageIdentifier).log();
        if (StringUtils.isBlank(languageIdentifier)) {
            return StringUtils.EMPTY;
        }
        languageIdentifier = languageIdentifier.replace("_", "-");
        List<LanguageRange> languageRange = Locale.LanguageRange.parse(languageIdentifier);
        if (languageRange != null && !languageRange.isEmpty()) {
            String formattedIdentifier = languageRange.get(0).getRange();
            formattedIdentifier = formattedIdentifier.replace("_", "-");
            diagnostic.prepareDebug("Formatted Language Identifier. Result Value:" + formattedIdentifier).log();
            return formattedIdentifier;
        }
        return StringUtils.EMPTY;
    }
    
	/*
	 * Generates unique id as string with hyphen in intervals(EX - for interval 4 -> egft-er4g-ert64-kkio, for interval 2 -> er-u7-s2,etc)
	 * SecureRandom is used instead of Random to withstand a cryptographic attack.
	 */
	public static String generateUniqueIDHyphenSeperated(int interval, int length) {
		try {
			String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
	        String CHAR_UPPER = CHAR_LOWER.toUpperCase();
	        String NUMBER = "0123456789";
	        String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;
	       
	        SecureRandom secureRandomGenerator = SecureRandom.getInstance("SHA1PRNG", "SUN");	        		
	        
	        if (length < 1) throw new IllegalArgumentException();
	        StringBuilder sb = new StringBuilder(length);
	        int intervalCheck = 0;
	        for (int i = 0; i < length; i++) {
	            // 0-62 (exclusive), random returns 0-61
	            int rndCharAt = secureRandomGenerator.nextInt(DATA_FOR_RANDOM_STRING.length());
	            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);
	            intervalCheck++;
	            if(intervalCheck == interval)
	            	{
	            	  sb.append("-");
	            	  intervalCheck = 0;
	            	}
	            sb.append(rndChar);
	        }
	        return sb.toString();	
		} catch (Exception e) {
			return null;
		}
	}
	
	
	public static JSONArray getFirstOccuringArray(JSONObject obj) {
		
		if(StringUtils.isNotBlank(obj.optString("dbpErrMsg"))) {
			JSONArray array = new JSONArray();
			array.put(obj);
			return array;
		}

		Iterator<String> keys = obj.keySet().iterator();
		while(keys.hasNext()) {
			try {
				return obj.getJSONArray(keys.next());
			}
			catch(JSONException e) {
				//do nothing;
			}
		}
		return null;
	}

}