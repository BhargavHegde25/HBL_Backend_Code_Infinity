package com.kony.AdminConsole.BLProcessor;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.constants.DBPConstants;
import com.kony.AdminConsole.Utilities.AdminConsoleOperations;
import com.kony.AdminConsole.Utilities.CommonUtilities;
import com.kony.AdminConsole.Utilities.ServiceConfig;
import com.kony.dbputilities.utils.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class UpgradeRolesAndEntiltlements implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        String userName = requestInstance.getParameter("UserName");
        JSONObject getResponse = upgradeRolesAndEntitlements(userName, requestInstance);

        if (getResponse.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) != 0) {
            String authToken = AdminConsoleOperations.login(requestInstance);
            ServiceConfig.setValue("Auth_Token", authToken);
            getResponse = upgradeRolesAndEntitlements(userName, requestInstance);
        }
        Result processedResult = HelperMethods.constructResultFromJSONObject(getResponse);
        processedResult.addParam(new Param("id", requestInstance.getParameter("id"), "String"));
        processedResult.addParam(new Param("FirstName", requestInstance.getParameter("FirstName"), "String"));
        processedResult.addParam(new Param("LastName", requestInstance.getParameter("LastName"), "String"));
        processedResult.addParam(new Param("Email", requestInstance.getParameter("Email"), "String"));
        processedResult.addParam(new Param("UserName", requestInstance.getParameter("UserName"), "String"));
        return processedResult;

    }

    public JSONObject upgradeRolesAndEntitlements(String userName, DataControllerRequest dcRequest) {

        Map<String, Object> postParametersMap = new HashMap<>();
        postParametersMap.put("userName", userName);
        postParametersMap.put("UserName", userName);

        String getResponseString = HelperMethods.invokeC360ServiceAndGetString(dcRequest, postParametersMap,
                new HashMap<>(), "upgraderolesandentitlements");
        return CommonUtilities.getStringAsJSONObject(getResponseString);
    }

}
