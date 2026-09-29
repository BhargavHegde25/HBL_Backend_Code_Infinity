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

public class MemberCreate implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        Map<String, Object> inputParams = CommonUtilities.getInputMapFromInputArray(inputArray);
        String userName = (String) inputParams.get("userName");

        String memberId = requestInstance.getParameter("memberId");
        String userfirstname = requestInstance.getParameter("firstName");
        String usermiddlename = requestInstance.getParameter("middleName");
        String userlastname = requestInstance.getParameter("lastName");
        String email = requestInstance.getParameter("email");
        String phone = requestInstance.getParameter("phone");

        JSONObject getResponse = memberCreate(memberId, userName, userfirstname, usermiddlename, userlastname, email,
                phone, requestInstance);

        if (getResponse.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) != 0) {
            String authToken = AdminConsoleOperations.login(requestInstance);
            ServiceConfig.setValue("Auth_Token", authToken);
            getResponse = memberCreate(memberId, userName, userfirstname, usermiddlename, userlastname, email, phone,
                    requestInstance);
        }
        Result processedResult = HelperMethods.constructResultFromJSONObject(getResponse);
        return processedResult;

    }

    public JSONObject memberCreate(String memberId, String userName, String userfirstname, String usermiddlename,
            String userlastname, String email, String phone, DataControllerRequest dcRequest) {

        Map<String, Object> postParametersMap = new HashMap<>();
        postParametersMap.put("MemberId", memberId);
        postParametersMap.put("FirstName", userfirstname);
        postParametersMap.put("MiddleName", usermiddlename);
        postParametersMap.put("LastName", userlastname);
        postParametersMap.put("Username", userName);
        postParametersMap.put("Email", email);
        postParametersMap.put("ContactNumber", phone);

        String getResponseString = HelperMethods.invokeC360ServiceAndGetString(dcRequest, postParametersMap,
                new HashMap<>(), "memberCreate");
        return CommonUtilities.getStringAsJSONObject(getResponseString);
    }

}
