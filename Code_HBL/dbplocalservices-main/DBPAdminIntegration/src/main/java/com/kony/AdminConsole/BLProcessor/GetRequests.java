package com.kony.AdminConsole.BLProcessor;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.dbp.core.constants.DBPConstants;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.AdminConsole.Utilities.AdminConsoleOperations;
import com.kony.AdminConsole.Utilities.CommonUtilities;
import com.kony.AdminConsole.Utilities.ServiceConfig;
import com.kony.AdminConsole.Utilities.URLConstants;
import com.kony.dbputilities.utils.ConvertJsonToResult;
import com.kony.dbputilities.utils.HelperMethods;
import com.kony.dbputilities.utils.HttpCallException;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.utils.CustomerSessionsUtil;

public class GetRequests implements JavaService2 {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final String ARRAY_KEY = "customerrequests_view";

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
        try {
            Map<String, Object> inputparams = CommonUtilities.getInputMapFromInputArray(inputArray);
            String userName = "";
            Map<String, Object> customer = CustomerSessionsUtil.getLoggedInUserAttributesMap(requestInstance);
            if (customer.get("UserName") != null)
            	userName = customer.get("UserName").toString();
            String softDeleteFlag = requestInstance.getParameter("softDeleteFlag");
            Result response = getRequestsFromDBP(userName, softDeleteFlag, requestInstance);
            if (response.getDatasetById(ARRAY_KEY) != null) {
                return response;
            } else {
                JSONObject getResponse = getRequests(userName, softDeleteFlag, requestInstance);

                if (getResponse.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) != 0) {
                    String authToken = AdminConsoleOperations.login(requestInstance);
                    ServiceConfig.setValue("Auth_Token", authToken);
                    getResponse = getRequests(userName, softDeleteFlag, requestInstance);
                }
                Result processedResult = HelperMethods.constructResultFromJSONObject(getResponse);
                return processedResult;
            }

        } catch (Exception e) {
            Result processedResult = new Result();
            processedResult.addParam(new Param("Error", e.getMessage(), "String"));
            return processedResult;
        }

    }

    public Result getRequestsFromDBP(String customerUsername, String softdeleteflag,
            DataControllerRequest requestInstance) throws HttpCallException {

        Result processedResult = new Result();
        String filter = null;

        if (StringUtils.isBlank(customerUsername)) {
            processedResult.addParam(new Param("Error", "Username is mandatory", "String"));
            return processedResult;
        }

        filter = "username eq '" + customerUsername + "'";
        if (StringUtils.isNotBlank(softdeleteflag)) {
            filter += " and softdeleteflag eq '" + softdeleteflag + "'";
        }
        filter += " and status_id ne 'Hard Deleted'";
        filter += " and status_id ne 'Draft'";

        final String finalFilter = filter;

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("$filter", finalFilter);
        paramMap.put("$orderby", "recentMsgDate desc");
        String res = HelperMethods.invokeServiceAndGetString(requestInstance, paramMap,
                HelperMethods.getHeaders(requestInstance), URLConstants.CUSTOMERREQUESTS_VIEW_READ);
        JsonObject operationResponse = HelperMethods.getJsonObject(res);
        if (operationResponse == null || !operationResponse.has(DBPConstants.FABRIC_OPSTATUS_KEY)
                && operationResponse.get(DBPConstants.FABRIC_OPSTATUS_KEY).getAsInt() != 0
                && !operationResponse.has(ARRAY_KEY)) {
            processedResult.addParam(new Param("Error", "Failed to fetch customer request details", "String"));
            return processedResult;
        }

        JsonArray response = operationResponse.get(ARRAY_KEY).getAsJsonArray();
        Dataset resultDataset = new Dataset();
        resultDataset.setId(ARRAY_KEY);
        for (int i = 0; i < response.size(); i++) {
            JsonObject object = response.get(i).getAsJsonObject();

            object.addProperty("recentMsgDate", HelperMethods.convertTimetoISO8601Format(
                    HelperMethods.parseDateStringToDate(object.get("recentMsgDate").getAsString(), DATE_FORMAT)));
            object.addProperty("requestCreatedDate", HelperMethods.convertTimetoISO8601Format(
                    HelperMethods.parseDateStringToDate(object.get("requestCreatedDate").getAsString(), DATE_FORMAT)));

            object.remove("accountid");
            resultDataset.addRecord(ConvertJsonToResult.constructRecord(object));
        }
        processedResult.addDataset(resultDataset);

        return processedResult;
    }

    public JSONObject getRequests(String username, String softDeleteFlag, DataControllerRequest dcRequest) {

        Map<String, Object> postParametersMap = new HashMap<>();

        postParametersMap.put("$filter", "softdeleteflag eq " + softDeleteFlag + " and username eq '" + username
                + "' and status_id ne 'Hard Deleted' and status_id ne 'Draft'");
        postParametersMap.put("username", username);
        postParametersMap.put("softdeleteflag", softDeleteFlag);

        String getResponseString = HelperMethods.invokeC360ServiceAndGetString(dcRequest, postParametersMap,
                new HashMap<>(), "GetRequestsURL");
        return CommonUtilities.getStringAsJSONObject(getResponseString);
    }

}