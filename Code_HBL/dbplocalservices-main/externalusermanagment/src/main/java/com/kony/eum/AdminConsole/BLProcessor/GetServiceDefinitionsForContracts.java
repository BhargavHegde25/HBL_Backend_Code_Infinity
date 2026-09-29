package com.kony.eum.AdminConsole.BLProcessor;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.constants.DBPConstants;
import com.kony.AdminConsole.Utilities.AdminConsoleOperations;
import com.kony.AdminConsole.Utilities.CommonUtilities;
import com.kony.AdminConsole.Utilities.ServiceConfig;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.utils.HelperMethods;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetServiceDefinitionsForContracts implements JavaService2 {
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
        JSONObject getResponse = getServiceDefinitionsForContracts(requestInstance);
        if (getResponse.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) != 0) {
            String authToken = AdminConsoleOperations.login(requestInstance);
            ServiceConfig.setValue("Auth_Token", authToken);
            getResponse = getServiceDefinitionsForContracts(requestInstance);
        }
        Result processedResult = HelperMethods.constructResultFromJSONObject(getResponse);
        return processedResult;

    }

    public JSONObject getServiceDefinitionsForContracts(DataControllerRequest dcRequest) {
        Map<String, Object> postParametersMap = new HashMap<>();
        postParametersMap.put("legalEntityId", dcRequest.getParameter("legalEntityId"));
        String getResponseString = HelperMethods.invokeC360ServiceAndGetString(dcRequest, postParametersMap,
                new HashMap<>(), "getServiceDefinitionsForContracts");
        return CommonUtilities.getStringAsJSONObject(getResponseString);
    }

}
