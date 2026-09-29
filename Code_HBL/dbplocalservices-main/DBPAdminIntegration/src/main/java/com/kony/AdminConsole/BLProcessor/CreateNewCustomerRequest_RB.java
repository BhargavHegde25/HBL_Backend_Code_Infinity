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
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class CreateNewCustomerRequest_RB implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
         Result processedResult=new Result();
        Map<String, Object> inputMap = CommonUtilities.getInputMapFromInputArray(inputArray);
        String username = (String) inputMap.get("username");
        String requestsubject = requestInstance.getParameter("Requestsubject");
        String requestcategory_id = requestInstance.getParameter("requestcategory_id");
        String priority = requestInstance.getParameter("Priority");
        String messagedescription = requestInstance.getParameter("messagedescription");
        String createdby = (String) inputMap.get("username");
        String requestid = requestInstance.getParameter("requestid");

        JSONObject getResponse = createNewRequestMessage(username, requestsubject, requestcategory_id, priority,
                messagedescription, createdby, requestid, requestInstance);

        if (getResponse.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) != 0) {
            String authToken = AdminConsoleOperations.login(requestInstance);
            ServiceConfig.setValue("Auth_Token", authToken);
            getResponse = createNewRequestMessage(username, requestsubject, requestcategory_id, priority,
                    messagedescription, createdby, requestid, requestInstance);
        }
      //  Result processedResult = HelperMethods.constructResultFromJSONObject(getResponse);
	    JSONObject createdbyObj = getResponse.getJSONObject(createdby);
        JSONObject reqIdObj = createdbyObj.getJSONObject("customerRequest");
        Record record = CommonUtilities.constructRecordFromJSONObject(reqIdObj);
        record.setId("customerRequest");
        processedResult.addRecord(record);
        JSONObject reqMsgObj = createdbyObj.getJSONObject("requestMessage");
        Record rec = CommonUtilities.constructRecordFromJSONObject(reqMsgObj);
        rec.setId("requestMessage");
        processedResult.addRecord(rec);
        return processedResult;

    }

    public JSONObject createNewRequestMessage(String username, String requestsubject, String requestcategory_id,
            String priority, String messagedescription, String createdby, String requestid,
            DataControllerRequest dcRequest) {

        Map<String, Object> postParametersMap = new HashMap<>();
        postParametersMap.put("username", username);
        if (requestsubject != null) {
            postParametersMap.put("requestsubject", requestsubject);
        }
        if (requestcategory_id != null) {
            postParametersMap.put("requestcategory_id", requestcategory_id);
        }
        postParametersMap.put("messagedescription", messagedescription);
        postParametersMap.put("priority", priority);
        if (createdby != null) {
            postParametersMap.put("createdby", createdby);
        }
        postParametersMap.put("requestid", requestid);
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("username", username);
        String getResponseString = HelperMethods.invokeC360ServiceAndGetString(dcRequest, postParametersMap, headerMap,
                "createCustomerRequestWithOutAttachment");
        return CommonUtilities.getStringAsJSONObject(getResponseString);

    }
}