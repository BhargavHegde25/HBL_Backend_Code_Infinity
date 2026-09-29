package com.temenos.infinity.api.srmstransactions.javaservice;

import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import org.json.JSONObject;
import com.kony.dbputilities.util.Log4j2Configurator;

import java.util.HashMap;

public class SRMSReturnMockDetailsOperation implements JavaService2 {
    @Override
    public Object invoke(String s, Object[] inputArray, DataControllerRequest request,
                         DataControllerResponse dataControllerResponse) throws Exception {
		Log4j2Configurator.getInstance();
        HashMap<String, Object> reqParams = (HashMap<String, Object>) inputArray[1];
        if(reqParams.containsKey("transactionId")){
            reqParams.put("referenceId", reqParams.get("transactionId"));
        }
        else if(request.containsKeyInRequest("transactionId")){
            reqParams.put("referenceId", request.getParameter("transactionId"));
        }
        JSONObject j = new JSONObject(reqParams);
        return JSONToResult.convert(j.toString());
    }
}
