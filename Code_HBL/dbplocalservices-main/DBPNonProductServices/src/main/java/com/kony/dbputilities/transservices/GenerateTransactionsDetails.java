package com.kony.dbputilities.transservices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.constants.DBPConstants;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.fileutil.FileGenerator;
import com.kony.dbputilities.fileutil.FileGeneratorFactory;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.util.ConvertJsonToResult;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GenerateTransactionsDetails implements JavaService2 {
    private final String TRANSACTION_ARRAY = "Transactions";
    private final String ACCOUNTS_ARRAY = "Accounts";
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse response) throws Exception {
        Result result = new Result();
        String format = "MM/dd/YYYY";
        final int SIZE_OF_RANDOM_GENERATED_STRING = 10;
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        if (validateFileType(inputParams, dcRequest, result)) {
            FileGenerator generator = FileGeneratorFactory.getFileGenerator(inputParams.get("fileType"));
            // to get identity handler
            dcRequest.addRequestParam_(DBPUtilitiesConstants.X_KONY_AUTHORIZATION, inputParams.get("Auth_Token"));
            dcRequest.getHeaderMap().put(DBPUtilitiesConstants.X_KONY_AUTHORIZATION, inputParams.get("Auth_Token"));

            JsonObject transactions = new JsonObject();
            String transactionType = (String) inputParams.get(DBPUtilitiesConstants.SEARCH_TRANS_TYPE);
            String installmentType = (String) inputParams.get(DBPUtilitiesConstants.INSTALLMENT_TYPE);
            String paymentDateFormat = (String) inputParams.get(DBPUtilitiesConstants.DATE_FORMAT);
            if (transactionType.equalsIgnoreCase("LoanSchedule")) {
                transactions = getLoanTransactions(inputParams, dcRequest);
            } else {
                transactions = getTransactions(inputParams, dcRequest);
            }
            paymentDateFormat = updatePaymentFormat(paymentDateFormat);
            if ("0".equals(transactions.get(DBPConstants.FABRIC_OPSTATUS_KEY).getAsString())) {
                JsonArray data = getJsonArray(transactions, TRANSACTION_ARRAY);
                byte[] bytes;
                if (!(StringUtils.isNotBlank(transactionType)
                        && transactionType.equals(DBPUtilitiesConstants.TRANSACTION_TYPE_LOAN_SCHEDULE))) {
                    bytes = generator.generateFile(data, inputParams.get("title"), inputParams.get("generatedBy"),
                            HelperMethods.convertDateFormat(inputParams.get("searchStartDate"), format),
                            HelperMethods.convertDateFormat(inputParams.get("searchEndDate"), format),
                            getFieldList(transactionType),
                            getOtherRequiredData(inputParams, dcRequest), inputParams.get("filters"),transactionType,inputParams);
                } else {
                    Map<String, String> summaryDetails = new HashMap<String, String>();
                    summaryDetails = getLoanScheduleSummaryDetails(inputParams, dcRequest);
                    bytes = generator.generateLoanFile(data, inputParams.get("title"),
                            inputParams.get("generatedBy"), getFieldList(transactionType),
                            getOtherRequiredData(inputParams, dcRequest), inputParams.get("filters"), installmentType,
                            summaryDetails, paymentDateFormat);
                }

                String fileId = HelperMethods.getUniqueNumericString(SIZE_OF_RANDOM_GENERATED_STRING);
                MemoryManager.saveIntoCache(fileId, bytes, 120);
                result.addParam("fileId", fileId, DBPUtilitiesConstants.STRING_TYPE);

            } else {
                result = ConvertJsonToResult.convert(transactions);
            }
        }
        return result;
    }

    private String updatePaymentFormat(String paymentDateFormat) {
        if (StringUtils.isNotBlank(paymentDateFormat)) {
            if (paymentDateFormat.equals("m/d/Y")) {
                paymentDateFormat = "MM/dd/yyyy";
            } else if (paymentDateFormat.equals("d/m/Y")) {
                paymentDateFormat = "dd/MM/yyyy";
            } else if (paymentDateFormat.equals("d.m.Y")) {
                paymentDateFormat = "dd.MM.yyyy";
            } else if (paymentDateFormat.equals("Y-m-d")) {
                paymentDateFormat = "yyyy-MM-dd";
            } else {
                paymentDateFormat = "MM/dd/yyyy";
            }
        } else {
            paymentDateFormat = "MM/dd/yyyy";
        }
        return paymentDateFormat;
    }

    private Map<String, String> getLoanScheduleSummaryDetails(Map<String, String> inputParams,
            DataControllerRequest dcRequest) throws HttpCallException {
        Map<String, String> summaryResult = new HashMap<String, String>();
        if (inputParams.containsKey("accountNumber")) {
            inputParams.put("accountID", inputParams.get("accountNumber"));
        }
        JsonObject accountDetails =
                callApi(dcRequest, inputParams, getHeaders(dcRequest, inputParams), URLConstants.ACCOUNT_DETAILS_GET);
        if (accountDetails.has("Accounts")) {
            JsonArray accountsArray = (JsonArray) accountDetails.get("Accounts");
            if (accountsArray.size() > 0) {
                JsonObject accounts = (JsonObject) accountsArray.get(0);
                if (accounts.has("futureInstallmentsCount") && accounts.get("futureInstallmentsCount") != null) {
                    summaryResult.put("FutureInstallmentsCount", accounts.get("futureInstallmentsCount").getAsString());
                }
                if (accounts.has("paidInstallmentsCount") && accounts.get("paidInstallmentsCount") != null) {
                    summaryResult.put("PaidInstallmentsCount", accounts.get("paidInstallmentsCount").getAsString());
                }
                if (accounts.has("overDueInstallmentsCount") && accounts.get("overDueInstallmentsCount") != null) {
                    summaryResult.put("OverDueInstallmentsCount",
                            accounts.get("overDueInstallmentsCount").getAsString());
                }
            }
        }
        return summaryResult;
    }

    private JsonArray getJsonArray(JsonObject jsonObject, String fieldName) {
        if (jsonObject.has(fieldName) && jsonObject.get(fieldName).isJsonArray()) {
            return jsonObject.getAsJsonArray(fieldName);
        }
        // logger.error("No data for..."+fieldName);
        return new JsonArray();
    }

    private Map<String, Object> getOtherRequiredData(Map<String, String> inputParams, DataControllerRequest dcRequest)
            throws HttpCallException {
        if ("qfx".equalsIgnoreCase(inputParams.get("fileType"))) {
            return getOtherDataForQFX(inputParams, dcRequest);
        } else if ("qbo".equalsIgnoreCase(inputParams.get("fileType"))) {
            return getOtherDataForQBO(inputParams, dcRequest);
        } else if ("pdf".equalsIgnoreCase(inputParams.get("fileType"))) {
            return getOtherDataForPDF(inputParams, dcRequest);
        } else {
            return getOtherData(inputParams, dcRequest);
        }
    }

    private Map<String, Object> getOtherDataForQFX(Map<String, String> inputParams, DataControllerRequest dcRequest)
            throws HttpCallException {
        Map<String, Object> otherData = new HashMap<>();
        otherData.put("intuitBuid", EnvironmentConfigurationsHandler.getValue("QFX_INTUIT_BUILD", dcRequest));
        otherData.put("financeId", EnvironmentConfigurationsHandler.getValue("QFX_FINANCE_ID", dcRequest));
        otherData.put("orgName", EnvironmentConfigurationsHandler.getValue("QFX_ORG_NAME", dcRequest));
        otherData.put("userName", EnvironmentConfigurationsHandler.getValue("QFX_USER_NAME", dcRequest));
        JsonObject accounts = getAccounts(inputParams, dcRequest);
        if ("0".equals(accounts.get(DBPConstants.FABRIC_OPSTATUS_KEY).getAsString())) {
            JsonArray accountList = getJsonArray(accounts, ACCOUNTS_ARRAY);
            otherData.put("accounts", accountList);
        }
        return otherData;
    }

    private Map<String, Object> getOtherDataForQBO(Map<String, String> inputParams, DataControllerRequest dcRequest)
            throws HttpCallException {
        Map<String, Object> otherData = new HashMap<>();
        otherData.put("intuitBuid", EnvironmentConfigurationsHandler.getValue("QBO_INTUIT_BUILD", dcRequest));
        otherData.put("financeId", EnvironmentConfigurationsHandler.getValue("QBO_FINANCE_ID", dcRequest));
        otherData.put("orgName", EnvironmentConfigurationsHandler.getValue("QBO_ORG_NAME", dcRequest));
        otherData.put("userName", EnvironmentConfigurationsHandler.getValue("QBO_USER_NAME", dcRequest));
        JsonObject accounts = getAccounts(inputParams, dcRequest);
        if ("0".equals(accounts.get(DBPConstants.FABRIC_OPSTATUS_KEY).getAsString())) {
            JsonArray accountList = getJsonArray(accounts, ACCOUNTS_ARRAY);
            otherData.put("accounts", accountList);
        }
        return otherData;
    }

    private Map<String, Object> getOtherDataForPDF(Map<String, String> inputParams, DataControllerRequest dcRequest)
            throws HttpCallException {
        Map<String, Object> otherData = new HashMap<>();
        otherData.put("imgFileName",
                EnvironmentConfigurationsHandler.getValue(URLConstants.ACCOUNT_STMNT_IMG, dcRequest));
        JsonObject userData = getUser(dcRequest, inputParams);
        if ("0".equals(userData.get(DBPConstants.FABRIC_OPSTATUS_KEY).getAsString())) {
            JsonObject user = (JsonObject) userData.getAsJsonArray("ExternalUsers").get(0);
            user.addProperty("account", inputParams.get("accountNumber"));
            if (inputParams.containsKey("accountName")) {
                user.addProperty("accountName", inputParams.get("accountName"));
            }
            otherData.put("userDetails", user);
        }
        return otherData;
    }

    private Map<String, Object> getOtherData(Map<String, String> inputParams, DataControllerRequest dcRequest)
            throws HttpCallException {
        Map<String, Object> otherData = new HashMap<>();
        JsonObject userData = getUser(dcRequest, inputParams);
        if ("0".equals(userData.get(DBPConstants.FABRIC_OPSTATUS_KEY).getAsString())) {
            JsonObject user = (JsonObject) userData.getAsJsonArray("ExternalUsers").get(0);
            otherData.put("accountNumber", inputParams.get("accountNumber"));
            if (HelperMethods.isJsonNotNull(user.get("bankName"))) {
                otherData.put("bankName", user.get("bankName").getAsString());
            }
        }
        if (inputParams.containsKey("accountName")) {
            otherData.put("accountName", inputParams.get("accountName"));
        }
        return otherData;
    }

    private JsonObject getUser(DataControllerRequest dcRequest, Map<String, String> inputParams)
            throws HttpCallException {
        return callApi(dcRequest, null, getHeaders(dcRequest, inputParams), URLConstants.USER_DETAILS);
    }

    private boolean validateFileType(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result) {
        String fileType = inputParams.get("fileType");
        boolean status = false;
        if (StringUtils.isNotBlank(fileType)) {
            switch (fileType) {
                case "xls":
                case "xlsx":
                case "csv":
                case "qfx":
                case "qbo":
                case "pdf":
                    status = true;
            }
        }
        if (!status) {
            HelperMethods.setValidationMsg("file type is either empty or not supported", dcRequest, result);
        }
        return status;
    }

    private JsonObject getTransactions(Map<String, String> inputParams, DataControllerRequest dcRequest)
            throws HttpCallException {
        return callApi(dcRequest, inputParams, getHeaders(dcRequest, inputParams), URLConstants.TRANSACTIONS_POST);
    }

    private JsonObject getLoanTransactions(Map<String, String> inputParams, DataControllerRequest dcRequest)
            throws HttpCallException {
        if (inputParams.containsKey("accountNumber")) {
            inputParams.put("accountID", inputParams.get("accountNumber"));
        }
        inputParams.put("isFutureRequired", "true");
        return callApi(dcRequest, inputParams, getHeaders(dcRequest, inputParams), URLConstants.LOAN_SCHEDULE_GET);
    }

    private JsonObject getAccounts(Map<String, String> inputParams, DataControllerRequest dcRequest)
            throws HttpCallException {
        Map<String, String> input = new HashMap<>();
        input.put("accountID", inputParams.get("accountNumber"));
        Map<String, String> headerMap = getHeaders(dcRequest, inputParams);
        headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        return HelperMethods.callApiJson(dcRequest, input, headerMap, URLConstants.ACCOUNTS_POST);
    }

    public JsonObject callApi(DataControllerRequest dcRequest, Map<String, String> inputParams,
            Map<String, String> headerParams, String url) throws HttpCallException {
        JsonObject jsonResponse = HelperMethods.callApiJson(dcRequest, inputParams, headerParams, url);
        return (null == jsonResponse) ? new JsonObject() : jsonResponse;
    }

    public Map<String, String> getHeaders(DataControllerRequest dcRequest, Map<String, String> inputParams) {
        Map<String, String> headers = new HashMap<>();
        Map<String, Object> reqHeader = dcRequest.getHeaderMap();
        for (Map.Entry<String, Object> entry : reqHeader.entrySet()) {
            headers.put(entry.getKey(), (String) entry.getValue());
        }
        headers.put(DBPUtilitiesConstants.X_KONY_AUTHORIZATION, inputParams.get("Auth_Token"));
        headers.put(DBPUtilitiesConstants.X_KONY_REPORTING_PARAMS,
                dcRequest.getParameter(DBPUtilitiesConstants.X_KONY_REPORTING_PARAMS));
        return headers;
    }

    private Map<String, String> getFieldList(String transactionType) {
        String resourceName = "TransactionsHeader.properties";
        if (StringUtils.isNotBlank(transactionType)
                && transactionType.equals(DBPUtilitiesConstants.TRANSACTION_TYPE_LOAN_SCHEDULE)) {
            resourceName = "LoanShedule.properties";
        }
        Map<String, String> fieldList = new LinkedHashMap<>();
        try (InputStream is = URLFinder.class.getClassLoader().getResourceAsStream(resourceName);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));) {
            String line = null;
            String[] words = null;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("#")) {
                    words = line.split("=");
                    fieldList.put(words[0], words[1]);
                }
            }
        } catch (IOException e) {
            alert.prepareError("Error on reading TransactionsHeader.properties..." + e).log();
        }
        return fieldList;
    }
}
