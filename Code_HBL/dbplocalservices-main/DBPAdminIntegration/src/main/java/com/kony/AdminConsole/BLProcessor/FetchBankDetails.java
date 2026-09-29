package com.kony.AdminConsole.BLProcessor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.dbp.core.constants.DBPConstants;
import com.kony.AdminConsole.Utilities.AdminConsoleOperations;
import com.kony.AdminConsole.Utilities.CommonUtilities;
import com.kony.AdminConsole.Utilities.ServiceCallHelper;
import com.kony.AdminConsole.Utilities.ServiceConfig;
import com.kony.dbputilities.utils.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class FetchBankDetails implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        try {
            String routingNumber = requestInstance.getParameter("routingNumber");
            String serviceName = requestInstance.getParameter("serviceName");
            String swiftCode = requestInstance.getParameter("swiftCode");
            String iban = requestInstance.getParameter("IBAN");
            JSONObject getResponse = updateAlerts(routingNumber, serviceName, swiftCode, iban, requestInstance);
            if (getResponse.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) != 0) {
                String authToken = AdminConsoleOperations.login(requestInstance);
                ServiceConfig.setValue("Auth_Token", authToken);
                getResponse = updateAlerts(routingNumber, serviceName, swiftCode, iban, requestInstance);
            }
            Result processedResult = HelperMethods.constructResultFromJSONObject(getResponse);
            if (StringUtils.isNotBlank(iban)) {
                String bankName = getFieldValue(processedResult, "bankName");
                Param param = null;
                if (bankName == null || bankName.isEmpty()) {
                    bankName = "Infinity";
                }
                param = new Param("bankName", bankName, DBPConstants.FABRIC_STRING_CONSTANT_KEY);
                processedResult = new Result();
                processedResult.addParam(param);
                processedResult.addParam(new Param(DBPConstants.FABRIC_HTTP_STATUS_CODE_KEY, "200", "int"));
            }
            return processedResult;

        } catch (Exception e) {
            Result res = new Result();
            res.addParam(new Param("errmsgg", e.getMessage(), "string"));
            return res;
        }

    }

    public static String getFieldValue(Result result, String fieldName) {
        String id = "";
        if (hasRecords(result)) {
            Dataset ds = result.getAllDatasets().get(0);
            id = getParamValue(ds.getRecord(0).getParam(fieldName));
        }
        return id;
    }

    public static String getParamValue(Param p) {
        String value = "";
        if (null != p) {
            value = p.getValue();
        }
        return (null == value) ? "" : value;
    }

    public static boolean hasRecords(Result result) {
        if (hasError(result) || null == result.getAllDatasets() || result.getAllDatasets().isEmpty()) {
            return false;
        }
        Dataset ds = result.getAllDatasets().get(0);
        return null != ds && null != ds.getAllRecords() && ds.getAllRecords().size() > 0;
    }

    public static boolean hasError(Result result) {
        boolean status = false;
        if (null == result || null != result.getParamByName("errmsg")) {
            status = true;
        }
        return status;
    }

    public JSONObject updateAlerts(String routingNumber, String serviceName, String swiftCode, String iban,
            DataControllerRequest dcRequest) {
        Map<String, Object> postParametersMap = new HashMap<>();
        String getResponseString = "";
        if (StringUtils.isNotBlank(iban)) {
            postParametersMap.put("$filter", "IBAN eq " + iban);
            getResponseString = ServiceCallHelper.invokeServiceAndGetString(dcRequest, postParametersMap,
                    new HashMap<>(), "fetchBankDetailsdbp");
        } else {
            if (routingNumber != null) {
                postParametersMap.put("routingNumber", routingNumber);
            }
            if (serviceName != null) {
                postParametersMap.put("serviceName", serviceName);
            }
            if (swiftCode != null) {
                postParametersMap.put("swiftCode", swiftCode);
            }
            getResponseString = HelperMethods.invokeC360ServiceAndGetString(dcRequest, postParametersMap,
                    new HashMap<>(), "fetchBankDetails");
        }

        return CommonUtilities.getStringAsJSONObject(getResponseString);
    }

}
