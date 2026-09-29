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
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.utils.CustomerSessionsUtil;

public class CreateCardRequest implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        String CardAccountNumber = requestInstance.getParameter("CardAccountNumber");
        String CardAccountName = requestInstance.getParameter("CardAccountName");

        String Username = (String) CustomerSessionsUtil.getLoggedInUserAttributesMap(requestInstance).get("UserName");
        
        String AccountType = requestInstance.getParameter("AccountType");
        String RequestCode = requestInstance.getParameter("RequestCode");
        String RequestReason = requestInstance.getParameter("RequestReason");
        String Channel = requestInstance.getParameter("Channel");
        String Address_id = requestInstance.getParameter("Address_id");
        String communication_id = requestInstance.getParameter("communication_id");
        String AdditionalNotes = requestInstance.getParameter("AdditionalNotes");
        JSONObject getResponse = createCardRequest(CardAccountNumber, CardAccountName, Username, AccountType,
                RequestCode, RequestReason, Channel, Address_id, communication_id, AdditionalNotes, requestInstance);

        if (getResponse.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) != 0) {
            String authToken = AdminConsoleOperations.login(requestInstance);
            ServiceConfig.setValue("Auth_Token", authToken);
            getResponse = createCardRequest(CardAccountNumber, CardAccountName, Username, AccountType, RequestCode,
                    RequestReason, Channel, Address_id, communication_id, AdditionalNotes, requestInstance);
        }
        Result processedResult = HelperMethods.constructResultFromJSONObject(getResponse);
        return processedResult;

    }

    public JSONObject createCardRequest(String CardAccountNumber, String CardAccountName, String Username,
            String AccountType, String RequestCode, String RequestReason, String Channel, String Address_id,
            String communication_id, String AdditionalNotes, DataControllerRequest dcRequest) {

        Map<String, Object> postParametersMap = new HashMap<>();
        postParametersMap.put("CardAccountNumber", CardAccountNumber);
        postParametersMap.put("CardAccountName", CardAccountName);
        postParametersMap.put("Username", Username);
        postParametersMap.put("AccountType", AccountType);
        postParametersMap.put("RequestCode", RequestCode);
        postParametersMap.put("RequestReason", RequestReason);
        postParametersMap.put("Channel", Channel);
        postParametersMap.put("Address_id", Address_id);
        postParametersMap.put("communication_id", communication_id);
        postParametersMap.put("AdditionalNotes", AdditionalNotes);

        String getResponseString = HelperMethods.invokeC360ServiceAndGetString(dcRequest, postParametersMap,
                new HashMap<>(), "createCardRequest");

        return CommonUtilities.getStringAsJSONObject(getResponseString);
    }

}
