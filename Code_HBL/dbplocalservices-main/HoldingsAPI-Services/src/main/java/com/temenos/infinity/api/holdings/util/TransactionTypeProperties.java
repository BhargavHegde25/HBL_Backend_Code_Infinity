package com.temenos.infinity.api.holdings.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.Property;

import com.google.gson.Gson;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.commons.invocation.Executor;
import com.temenos.infinity.api.holdings.config.HoldingsAPIServices;
import com.temenos.infinity.api.holdings.constants.ErrorCodeEnum;

/**
 * <p>
 * Class to load and access values of transactiontype.properties file
 * </p>
 * 
 * @author Aditya Mankal
 *
 */
public class TransactionTypeProperties {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static Properties PROPS = null;
    public Map<String, String> transactionTypesMap = new HashMap<>();

    public TransactionTypeProperties(DataControllerRequest request) {
        PROPS = loadProps(request);
    }

    private static Properties loadProps(DataControllerRequest request) {
        Properties properties = new Properties();
/*
        try (InputStream inputStream = TransactionTypeProperties.class.getClassLoader()
                .getResourceAsStream("transactiontype.properties")) {
            properties.load(inputStream);
            return properties;
        } catch (Exception e) {
            alert.prepareError("Error while loading transactiontype.properties", e).log();
        }*/

        // Load properties from DB
        properties = loadTransactionTypeProperties(request);
        return properties;
    }

    /**
     * Returns the transaction type associated with this key
     * 
     * @param key
     * @return
     */
    public static String getValue(String transactionTypeCode) {
        return PROPS.getProperty(transactionTypeCode);
    }

    public static String getCategorizationId(String transactionType) {
        Enumeration<String> enums = (Enumeration<String>) PROPS.propertyNames();
    	StringBuilder categorizationIds = new StringBuilder();
        while (enums.hasMoreElements()) {
            String key = enums.nextElement();
            String value = PROPS.getProperty(key);
            if (value.equalsIgnoreCase(transactionType)) {
                // After we support multiple transactionType remove break and
                // uncommented below line
//                 categorizationIds=categorizationIds+" "+ key;
                 categorizationIds.append(key).append(" ");

                // remove below after ms supports multiple transaction types
//                categorizationIds = categorizationIds + key;
//                break;
            }
        }
        
        return categorizationIds.toString();
    }

    @SuppressWarnings("unchecked")
    public static Properties loadTransactionTypeProperties(DataControllerRequest request) {
        String result = "";
        Properties properties = new Properties();
        final String CACHE_KEY_TRANSATIONTYPE_MAPPING = "transactionTypeMapping";
        final int CACHE_TIME = 10 * 60; // 10 minutes
        HashMap<String, Object> inputParams = new HashMap<>();
        HashMap<String, Object> headerParams = new HashMap<>();
        Map<String, String> transactionTypesMap = new HashMap<>();

        try {
            Object object = HoldingsUtils.getDataFromCache(request, CACHE_KEY_TRANSATIONTYPE_MAPPING);
            if (null != object) {
                if (StringUtils.isNotBlank(object.toString())) {
                    JSONObject txnTypes = new JSONObject(object.toString());
                    if (txnTypes.length() != 0) {
                        transactionTypesMap = new Gson().fromJson(txnTypes.toString(), Map.class);
                        properties = Property.toProperties(txnTypes);
                    }
                }
                if (!properties.isEmpty()) {
                    return properties;
                }
            }
            try {
                result = Executor.invokeService(HoldingsAPIServices.DBXDB_GET_TRANSACTIONTYPEMAPPING, inputParams,
                        headerParams);
            } catch (Exception e) {
                throw new ApplicationException(ErrorCodeEnum.ERR_20047);
            }

            if (StringUtils.isNotBlank(result)) {
                JSONObject txnMapping = new JSONObject(result);
                if (txnMapping != null && txnMapping.has("transactiontypemapping")) {
                    JSONArray txnArray = txnMapping.getJSONArray("transactiontypemapping");
                    if (txnArray != null && txnArray.length() > 0) {
                        for (Object o : txnArray) {
                            JSONObject txn = (JSONObject) o;
                            String key = StringUtils.isNotBlank(txn.getString("backendTransactionTypeId"))
                                    ? txn.getString("backendTransactionTypeId") : "";
                            String value = StringUtils.isNotBlank(txn.getString("dbxTransactionType"))
                                    ? txn.getString("dbxTransactionType") : "";
                            transactionTypesMap.put(key, value);
                            properties.setProperty(key, value);
                        }
                    }
                }
            }
            HoldingsUtils.insertDataIntoCache(request, transactionTypesMap, CACHE_KEY_TRANSATIONTYPE_MAPPING,
                    CACHE_TIME);

        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
        }
        return properties;
    }

}
