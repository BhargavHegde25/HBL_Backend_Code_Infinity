/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.utils;

import com.google.gson.JsonObject;
import com.kony.dbputilities.util.BundleConfigurationHandler;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.infinity.tradesupplyfinance.constants.ErrorCodeEnum;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import static com.kony.dbputilities.util.BundleConfigurationHandler.BUDLENAME_DBP;
import static com.kony.dbputilities.util.EnvironmentConfigurationsHandler.getValue;
import static com.kony.dbputilities.util.HelperMethods.getHeaders;
import static com.temenos.infinity.tradesupplyfinance.constants.TradeSupplyFinanceConstants.*;

/**
 * @author k.meiyazhagan
 */
public class TradeSupplyFinanceCommonUtils {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    private TradeSupplyFinanceCommonUtils() {
    }

    public static String fetchCustomerIdFromSession(DataControllerRequest request) {
        Map<String, Object> customer = CustomerSession.getCustomerMap(request);
        return CustomerSession.getCustomerId(customer);
    }

    public static String getCurrentDateTimeUTF() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIMESTAMP_FORMAT);
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static boolean updateErrorResponse(FabricResponseManager fabricResponseManager, ErrorCodeEnum errEnum) {
        JsonObject resPayload = null;
        if (!HelperMethods.isJsonEleNull(fabricResponseManager.getPayloadHandler().getPayloadAsJson())) {
            resPayload = fabricResponseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
        }
        resPayload = errEnum.setErrorCode(resPayload);
        fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
        return false;
    }

    public static boolean isValidDate(SimpleDateFormat dateFormat, String inputDate) {
        try {
            Date parsedDate = dateFormat.parse(inputDate);
            return parsedDate != null && dateFormat.format(parsedDate).equals(inputDate);
        } catch (Exception e) {
            alert.prepareError("Error occurred while validating date", e).log();
            return false;
        }
    }

    public static boolean isDateGreater(SimpleDateFormat dateFormat, String fromDate, String toDate) {
        try {
            return dateFormat.parse(fromDate).compareTo(dateFormat.parse(toDate)) < 0;
        } catch (Exception e) {
            alert.prepareError("Error occurred while comparing date", e).log();
            return false;
        }
    }

    public static HashMap<String, Object> getHeadersMap(DataControllerRequest request) {
        HashMap<String, Object> headerMap = new HashMap<>();
        headerMap.put(HTTP_HEADER_X_KONY_AUTHORIZATION, request.getHeader(HTTP_HEADER_X_KONY_AUTHORIZATION));
        headerMap.put(HTTP_HEADER_X_KONY_REPORTING_PARAMS, request.getHeader(HTTP_HEADER_X_KONY_REPORTING_PARAMS));
        return headerMap;
    }

    public static boolean areValidDocuments(String inputDocuments, JSONArray allDocumentTypes) {
        Pattern fileNameMax200 = Pattern.compile("^[a-zA-Z0-9 ._()/-]{0,200}$");
        try {
            JSONArray documents = new JSONArray(inputDocuments);
            Set<String> allDocumentNames = new HashSet<>();
            for (int i = 0; i < documents.length(); i++) {
                JSONObject document = documents.getJSONObject(i);
                allDocumentNames.add(document.getString(PARAM_DOCUMENT_NAME).toLowerCase());
                String[] temp = StringUtils.split(document.getString(PARAM_DOCUMENT_NAME), ".");
                if (!fileNameMax200.matcher(temp[0]).matches()
                        || !allDocumentTypes.toList().contains(temp[1].toLowerCase())
                        || StringUtils.isBlank(document.getString(PARAM_DOCUMENT_REFERENCE))
                        || document.keySet().size() != 2) {
                    return false;
                }
            }
            return allDocumentNames.size() == documents.length();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isInvoiceApprovalRequired(DataControllerRequest request, String uploadedBy, String role) {
        boolean result = false;
        try {
            JSONArray bundleConfig = new JSONArray(Objects.requireNonNull(BundleConfigurationHandler.fetchBundleConfigurations(BUDLENAME_DBP, PARAM_INVOICE_APPROVAL_KEY, getHeadersMap(request))));
            for (Object obj : bundleConfig) {
                JSONObject config = new JSONObject(obj.toString());
                if (StringUtils.equalsIgnoreCase(config.getString("Approval Required"), PARAM_OPTION_YES)
                        && StringUtils.equalsIgnoreCase(config.getString("Uploaded by"), uploadedBy)
                        && StringUtils.equalsIgnoreCase(config.getString("Role"), role)) {
                    result = true;
                    break;
                }
            }
        } catch (Exception e) {
            alert.prepareError("Error occurred while validating approval bundles - invoice").log();
        }
        return result;
    }

    public static JSONObject getBundleConfigAsJson(FabricRequestManager requestManager, String key) {
        JSONObject bundleConfig = new JSONObject();
        try {
            bundleConfig = new JSONObject(Objects.requireNonNull(BundleConfigurationHandler.fetchBundleConfigurations(BUDLENAME_DBP, key, getHeaders(requestManager.getHeadersHandler()))));
        } catch (Exception e) {
            alert.prepareError("Error occurred while fetching DBP bundle: " + key, e);
        }
        return bundleConfig;
    }

    public static JSONObject getBundleConfigAsJson(DataControllerRequest request, String key) {
        JSONObject bundleConfig = new JSONObject();
        try {
            bundleConfig = new JSONObject(Objects.requireNonNull(BundleConfigurationHandler.fetchBundleConfigurations(BUDLENAME_DBP, key, getHeadersMap(request))));
        } catch (Exception e) {
            alert.prepareError("Error occurred while fetching DBP bundle: " + key, e);
        }
        return bundleConfig;
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

    public static String getCoreCustomerId(FabricRequestManager requestManager, List<String> availableCustomers) {
        String backendId = null;
        try {
            // TODO: Get logged in customer id from object preprocessor
            List<String> allowedCustomerIds = Arrays.asList("100100", "100110", "100112", "100114", "100115");
            for (String customerId : availableCustomers) {
                if (StringUtils.isNotBlank(customerId) && !allowedCustomerIds.contains(customerId)) {
                    backendId = customerId;
                    break;
                }
            }
        } catch (Exception e) {
            alert.prepareError("Error occurred while fetching customer information", e);
        }
        return backendId;
    }

    public static String getScfBackend() {
        return getValue(PROPERTY_SCF_BACKEND).toUpperCase();
    }

    public static String getUniqueIdParamName(String scfBackend) {
        if (StringUtils.isBlank(scfBackend))
            scfBackend = getScfBackend();
        return StringUtils.equals(scfBackend, PARAM_SCF_BACKEND_DBXDB) ? PARAM_RECORD_ID : PARAM_ORDER_ID;
    }
}