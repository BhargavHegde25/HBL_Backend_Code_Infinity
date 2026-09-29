package com.temenos.auth.admininteg.operation;


import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.utils.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetPasswordRulesAndPolicy implements JavaService2 {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	
	@Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
        try {
            return AdminUtil.invokeAPI(HelperMethods.getInputParamMap(inputArray), URLConstants.GET_PASSWORD_RULES_AND_POLICIES, requestInstance);
        } catch (Exception e) {
        	alert.prepareError("Caught exception : ", e).log();
           return new Result();
        }
    }
	
//    @Override
//    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
//            DataControllerResponse responseInstance) throws Exception {
//
//        try {
//            Map<String, Object> map = new HashMap<>();
//            Iterator<String> iterator = requestInstance.getParameterNames();
//            String key;
//
//            while (iterator.hasNext()) {
//                key = iterator.next();
//                map.put(key, requestInstance.getParameter(key));
//            }
//
//            JSONObject getResponse = updateAlerts(map, requestInstance);
//
//            if (getResponse.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) != 0) {
//                String authToken = AdminConsoleOperations.login(requestInstance);
//                ServiceConfig.setValue("Auth_Token", authToken);
//                getResponse = updateAlerts(map, requestInstance);
//            }
//            Result processedResult = HelperMethods.constructResultFromJSONObject(getResponse);
//            return processedResult;
//        } catch (Exception e) {
//            Result res = new Result();
//            res.addParam(new Param("errmsgg", e.getMessage(), "string"));
//            return res;
//        }
//    }
//
//    public JSONObject updateAlerts(Map<String, Object> map, DataControllerRequest dcRequest) {
//
//        HashMap<String, Object> customHeaders = new HashMap<>();
//        customHeaders.put("Accept-Language", dcRequest.getHeader("Accept-Language"));
//        String getResponseString = HelperMethods.invokeC360ServiceAndGetString(dcRequest, map, customHeaders,
//                "getpasswordrulesandpolicy");
//        return CommonUtilities.getStringAsJSONObject(getResponseString);
//
//    }
}
