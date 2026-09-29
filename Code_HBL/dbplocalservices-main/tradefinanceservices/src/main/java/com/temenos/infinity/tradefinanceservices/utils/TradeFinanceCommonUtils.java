/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.utils;

import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.kony.dbputilities.util.EnvironmentConfigurationsHandler.getValue;

public class TradeFinanceCommonUtils implements TradeFinanceConstants {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final NumberFormat amountFormat = NumberFormat.getInstance();

    public TradeFinanceCommonUtils() {
    }

    public static JSONArray getFirstOccuringArray(JSONObject obj) {

        if (StringUtils.isNotBlank(obj.optString(PARAM_DBP_ERR_MSG))) {
            JSONArray array = new JSONArray();
            array.put(obj);
            return array;
        }

        Iterator<String> keys = obj.keySet().iterator();
        while (keys.hasNext()) {
            try {
                return obj.getJSONArray(keys.next());
            } catch (JSONException e) {
                //do nothing;
            }
        }
        return null;
    }

    /*
     * This method is used to filter the list with filterByParam and filterByValue parameters.
     * Modified filterBy method from FilterDTO based on trade finance requirements.
     */
    public static <T> List<T> filterBy(List<T> inputList, String filterByParam, String filterByValue) {
        List<T> filteredList = new ArrayList();
        Map<String, Set<String>> filterParams = getFilterParamMap(filterByParam, filterByValue);

        try {
            int i = 0;

            for (Iterator iter = filterParams.keySet().iterator(); iter.hasNext(); ++i) {
                String key = (String) iter.next();
                List<T> temp = new ArrayList();
                Iterator iterator;
                Object obj;
                Map objMap;
                List<String> filterString;
                String dataString;
                if (i == 0) {
                    iterator = inputList.iterator();
                } else {
                    iterator = filteredList.iterator();
                }
                while (iterator.hasNext()) {
                    obj = iterator.next();
                    objMap = JSONUtils.parseAsMap((new JSONObject(obj)).toString(), String.class, Object.class);
                    filterString = filterParams.get(key) != null ? Arrays.asList(filterParams.get(key).toArray(new String[0])) : null;
                    dataString = objMap.get(key) != null ? objMap.get(key).toString().toLowerCase().trim() : null;
                    if (filterString.contains(dataString)) {
                        temp.add((T) obj);
                    }
                }

                filteredList = temp;
            }
        } catch (IOException e) {
            alert.prepareError("Failed on filtering by params", e).log();
        }

        return filteredList;
    }

    private static Map<String, Set<String>> getFilterParamMap(String params, String values) {
        Map<String, Set<String>> map = new HashMap();
        int i = 0;

        String key;
        String value;
        for (int j = 0; i < params.length() - 1 && j < values.length() - 1; map.get(key.trim()).add(value.trim())) {
            int temp = params.indexOf(44, i);
            if (temp == -1) {
                temp = params.length();
            }

            key = params.substring(i, temp);
            i = temp + 1;
            temp = values.indexOf(44, j);
            if (temp == -1) {
                temp = values.length();
            }


            value = values.substring(j, temp).toLowerCase();
            j = temp + 1;
            if (!map.containsKey(key)) {
                map.put(key, new HashSet());
            }
        }

        return map;
    }

    /*
     * This method is used to filter the list with searchString parameter.
     * Modified searchBy method from FilterDTO based on trade finance requirements.
     */
    public static <T> List<T> searchBy(List<T> inputlist, String searchString) {

        List<T> filteredList = new ArrayList<T>();
        Map<String, Object> objMap;
        try {
            for (T obj : inputlist) {
                objMap = JSONUtils.parseAsMap((new JSONObject(obj)).toString(), String.class, Object.class);
                String obString = JSONUtils.stringify(objMap.values());
                if (StringUtils.isNotEmpty(obString) && obString.toUpperCase().contains(searchString.toUpperCase())) {
                    filteredList.add(obj);
                }
            }
        } catch (Exception e) {
            alert.prepareError("Failed on filtering by search", e).log();
        }

        return filteredList;
    }

    public static JSONObject mergeJSONObjects(JSONObject json1, JSONObject json2) {
        JSONObject mergedJSON = new JSONObject();
        try {
            mergedJSON = new JSONObject(json1, JSONObject.getNames(json1));
            for (String key : JSONObject.getNames(json2)) {
                mergedJSON.put(key, json2.get(key));
            }
        } catch (JSONException e) {
            alert.prepareError("Error occurred while merging JSON objects").log();
        }
        return mergedJSON;
    }

    public static String generateTradeFinanceFileID(String prefix) {
        prefix = prefix != null ? prefix + "-" : null;
        String uniqueId = CommonUtils.generateUniqueID(32);
        return prefix + uniqueId.substring(0, 8) + "-" + uniqueId.substring(8, 16) + "-" + uniqueId.substring(16, 24)
                + "-" + uniqueId.substring(24, 32);
    }

    public static String getAttachmentName(String prefix) {
        String attachmentName = MODULENAME_TRADEFINANCE;
        switch (prefix) {
            case PREFIX_IMPORT_LOC:
                attachmentName = MODULENAME_IMPORT_LOC;
                break;
            case PREFIX_IMPORT_DRAWING:
                attachmentName = MODULENAME_IMPORT_DRAWING;
                break;
            case PREFIX_IMPORT_AMENDMENT:
                attachmentName = MODULENAME_IMPORT_AMENDMENT;
                break;
            case PREFIX_EXPORT_LOC:
                attachmentName = MODULENAME_EXPORT_LOC;
                break;
            case PREFIX_EXPORT_DRAWING:
                attachmentName = MODULENAME_EXPORT_DRAWING;
                break;
            case PREFIX_EXPORT_AMENDMENT:
                attachmentName = MODULENAME_EXPORT_AMENDMENT;
                break;
            case PREFIX_GUARANTEE_LOC:
                attachmentName = MODULENAME_GUARANTEE_LOC;
                break;
            case PREFIX_GUARANTEE_AMENDMENT:
                attachmentName = MODULENAME_GUARANTEE_AMENDMENT;
                break;
            case PREFIX_RECEIVED_GUARANTEE:
                attachmentName = MODULENAME_RECEIVED_GUARANTEE;
                break;
            case PREFIX_RECEIVED_GUARANTEE_AMENDMENT:
                attachmentName = MODULENAME_RECEIVED_GUARANTEE_AMENDMENT;
                break;
            case PREFIX_RECEIVED_GUARANTEE_CLAIM:
                attachmentName = MODULENAME_RECEIVED_GUARANTEE_CLAIM;
                break;
            case PREFIX_ISSUED_GUARANTEE_CLAIM:
                attachmentName = MODULENAME_CLAIMS_RECEIVED;
                break;
            case PREFIX_INWARD_COLLECTIONS:
                attachmentName = MODULENAME_INWARD_COLLECTIONS;
                break;
            case PREFIX_INWARD_COLLECTION_AMENDMENT:
                attachmentName = MODULENAME_INWARD_COLLECTION_AMENDMENT;
                break;
            case PREFIX_OUTWARD_COLLECTION:
                attachmentName = MODULENAME_OUTWARD_COLLECTION;
                break;
            case PREFIX_OUTWARD_COLLECTION_AMENDMENT:
                attachmentName = MODULENAME_OUTWARD_COLLECTION_AMENDMENT;
                break;
        }
        return attachmentName;
    }

    public static <T> List<T> filterByTimeFrameForDashboard(List<T> inputList, String _timeParam, String _timeValue) {
        List<T> filteredList = new ArrayList<T>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date currentDate = dateFormat.parse(_timeValue);
            for (T ob : inputList) {
                Map<String, Object> objMap = JSONUtils.parseAsMap(new JSONObject(ob).toString(), String.class, Object.class);
                if (objMap.containsKey(_timeParam) && objMap.get(_timeParam) != null && StringUtils.isNotEmpty(objMap.get(_timeParam).toString())) {
                    Date recordDate = dateFormat.parse(objMap.get(_timeParam).toString());
                    String paymentStatus = objMap.get("paymentStatus").toString();
                    if (recordDate.equals(currentDate) || (recordDate.before(currentDate) && paymentStatus.equalsIgnoreCase("Overdue"))) {
                        filteredList.add(ob);
                    }
                }
            }
        } catch (ParseException e) {
            alert.prepareError("Failed on filtering by period due to date format", e).log();
        } catch (IOException e) {
            alert.prepareError("Failed on filtering by period while object parsing", e).log();
        }
        return filteredList;
    }

    public static String getCurrencySymbol(String currencyCode) {
        Currency currency = Currency.getInstance(currencyCode);
        return currency.getSymbol();
    }

    public static JSONObject getAccountsFromSession(String customerId) {
        String userAccountDetails = (String) MemoryManager.getFromCache("INTERNAL_BANK_ACCOUNTS" + customerId);
        return userAccountDetails != null ? new JSONObject(userAccountDetails) : new JSONObject();
    }

    public static String getAccountDetails(String customerId, String accountNumber, String key) {
        if (accountNumber != null) {
            JSONObject accountObject = getAccountsFromSession(customerId);
            if (accountObject.has(accountNumber))
                return accountObject.getJSONObject(accountNumber).optString(key);
        }
        return null;
    }

    public static String getMaskedAccountDetails(String customerId, String accountNumber) {
        String maskedAccount = "";
        try {
            String accountName = getAccountDetails(customerId, accountNumber, "accountName");
            accountName = accountName != null ? accountName : "";
            maskedAccount = StringUtils.join(accountName, "....", accountNumber.substring(accountNumber.length() - 4));
        } catch (Exception e) {
            alert.prepareError("Error in masking account details. ", e).log();
        }
        return maskedAccount;
    }

    public static String getAccountCurrency(String customerId, String accountNumber) {
        return getAccountDetails(customerId, accountNumber, "currencyCode");
    }

    public static String getAvailableAccountBalance(String customerId, String accountNumber) {
        return getAccountDetails(customerId, accountNumber, "availableBalance");
    }

    public static String formatAmount(String amount) {
        double amountInDouble = Double.parseDouble(amount.replaceAll(",", ""));
        amountFormat.setMinimumFractionDigits(2);
        return amountFormat.format(amountInDouble);
    }

    public static String getAmountWithCurrency(String currency, String amount, boolean isSymbolRequired) {
        return StringUtils.isNotBlank(currency) && StringUtils.isNotBlank(amount)
                ? StringUtils.join(isSymbolRequired ? getCurrencySymbol(currency) : currency, " ", formatAmount(amount))
                : "NA";
    }

    public static String getMaturityPeriod(String dateInString) {
        String maturityDays = "";
        try {
            long daysDiff = LocalDate.now().until(LocalDate.parse(dateInString), ChronoUnit.DAYS);
            if (daysDiff <= 0) {
                maturityDays = "Overdue";
            } else {
                maturityDays = daysDiff + " days left";
            }
        } catch (Exception e) {
            alert.prepareError("Error in calculating maturity period ", e).log();
        }
        return maturityDays;
    }

    public static String getDaysInBetween(String startDate, String endDate) {
        String result = "";
        try {
            LocalDate date1 = LocalDate.parse(startDate);
            LocalDate date2 = LocalDate.parse(endDate);
            result = String.valueOf(ChronoUnit.DAYS.between(date1, date2));
        } catch (Exception e) {
            alert.prepareError("Error in calculating days between").log();
        }
        return result;
    }

    public static String _formatDate(String dateInString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = "";
        try {
            Date date = format.parse(dateInString);
            result = new SimpleDateFormat("MM/dd/yyyy").format(date);
        } catch (Exception e) {
            alert.prepareError("Error in formatting date. ", e).log();
        }

        return result;
    }

    public static String formatTransactDate(String inputDate) {
        try {
            Date date = new SimpleDateFormat(TRANSACT_DATE_FORMAT).parse(inputDate);
            return new SimpleDateFormat(STANDARDTIMESTAMP_FORMAT).format(date);
        } catch (ParseException e) {
            alert.prepareError("Error in formatting date. ", e).log();
            return null;
        }
    }

    public static String formatDate2(String dateInString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(dateInString);
        } catch (ParseException e) {
            format = new SimpleDateFormat("MM/dd/yyyy");
            try {
                date = format.parse(dateInString);
            } catch (ParseException ex) {
                alert.prepareError("Error in formatting date. ", ex).log();
            }
        }
        SimpleDateFormat dt1 = new SimpleDateFormat(TIMESTAMP_FORMAT);
        return dt1.format(date);
    }

    public static String getCurrentDateTimeUTF() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIMESTAMP_FORMAT);
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static HashMap<String, Object> getTypeAndSubType(String type, String subType, boolean isCreate) {
        HashMap<String, Object> inputMap = new HashMap<>();
        Properties props = TradeFinanceProperties.loadProps(PARAM_PROPERTY);
        inputMap.put(PARAM_TYPE, props.getProperty(type));
        inputMap.put(isCreate ? PARAM_subtype : PARAM_subType, props.getProperty(subType));
        inputMap.put(PARAM_PAGE_SIZE, DEFAULT_PAGE_SIZE);
        return inputMap;
    }

    public static HashMap<String, Object> getHeadersMap(DataControllerRequest request) {
        HashMap<String, Object> headerMap = new HashMap<>();
        headerMap.put(HTTP_HEADER_X_KONY_AUTHORIZATION, request.getHeader(HTTP_HEADER_X_KONY_AUTHORIZATION));
        headerMap.put(HTTP_HEADER_X_KONY_REPORTING_PARAMS, request.getHeader(HTTP_HEADER_X_KONY_REPORTING_PARAMS));
        return headerMap;
    }

    public static String fetchCustomerFromSession(DataControllerRequest request) {
        Map<String, Object> customer = CustomerSession.getCustomerMap(request);
        return CustomerSession.getCustomerId(customer);
    }

    public static void setAlertDataInResult(Result result, AlertsEnum alert, String orderId) {
        if (alert != null && StringUtils.isNotBlank(orderId)) {
            JSONObject alertsConfig = new JSONObject();
            alertsConfig.put(PARAM_ORDER_ID, orderId);
            alertsConfig.put(PARAM_ALERT_NAME, alert.name());
            result.addParam(PARAM_ALERT_DATA, String.valueOf(alertsConfig));
        }
    }

    public static void validateRecordWithUser(JSONObject responseObj, DataControllerRequest request) {
        if (!StringUtils.equals(responseObj.getString(PARAM_PARTY_ID), fetchCustomerFromSession(request))) {
            throw new SecurityException(ErrorCodeEnum.ERRTF_29070.getErrorMessage());
        }
    }

    public static boolean isTransactEnabled() {
        return Boolean.parseBoolean(getValue(PROPERTY_TF_IS_TRANSACT_ENABLED));
    }

    public static String getCoreCustomerId(DataControllerRequest request) {
        String backendId = null;
        try {
            Map<String, Object> customer = CustomerSession.getCustomerMap(request);
            JSONObject obj = new JSONObject(customer.get("backendIdentifiers").toString());
            backendId = obj.getJSONArray("CORE").getJSONObject(0).getString("BackendId");
        } catch (Exception e) {
            alert.prepareError("Error occurred while fetching customer information", e);
        }
        return backendId;
    }

    public static String getTfBackend() {
        return getValue(PROPERTY_TF_BACKEND).toUpperCase();
    }

    public static String getUniqueIdParamName(String tfBackend) {
        if (StringUtils.isBlank(tfBackend))
            tfBackend = getTfBackend();
        return StringUtils.equals(tfBackend, PARAM_TF_BACKEND_DBXDB) ? PARAM_RECORD_ID : PARAM_ORDER_ID;
    }

    public static JSONObject convertToJSONObject(Object obj) {
        JSONObject json = new JSONObject();
        try {
            Class<?> clazz = obj.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                json.put(field.getName(), field.get(obj));
            }
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }
        return json;
    }
}