package com.bct.preprocessor;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class KUKLGetCustomerBillInfoPreProcessor implements DataPreProcessor2 {
	public static LoggerUtil logger = new LoggerUtil(KUKLGetCustomerBillInfoPreProcessor.class);
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response, Result result)
			throws Exception {
		   String userId = EnvironmentConfigurationsHandler.getServerProperty("KUKL_BILLINFO_USERNAME");
	       String password = EnvironmentConfigurationsHandler.getServerProperty("KUKL_BILLINFO_PASSWORD");
	       //inputMap.put("userId", neaPaymentsUserId);
	       //inputMap.put("password", neaPaymentsPassword);
		   //Map<String, Object> headermap = new HashMap<String, Object>();
		   //headermap.put("Authorization", AUTHORIZATION);
	       String Authorization=generateBasicAuthorization(userId, password);
	       String inputType=inputMap.get("InputType")!=null?inputMap.get("InputType").toString():"";
	       String inputValue=inputMap.get("InputValue")!=null?inputMap.get("InputValue").toString():"";
	       logger.debug("HBL:KUKLGetCustomerBillInfo:inputType:"+inputType);
	       logger.debug("HBL:KUKLGetCustomerBillInfo:inputValue:"+inputValue);
	       logger.debug("HBL:KUKLGetCustomerBillInfo:inputMap:"+inputMap);
	       if(StringUtils.isNotBlank(inputType)) {
	    	   if(inputType.equalsIgnoreCase("connectionNo")) {
	    		   inputMap.put("connectionNo", inputValue);
	    	   }else if(inputType.equalsIgnoreCase("customerNo")) {
	    		   inputMap.put("customerNo", inputValue);
	    	   }else {
	    		result.addParam(new Param("dbpErrCode","20000"));
	   			result.addParam(new Param("dbpErrMsg", "Invalid Input Params"));
	    		   return false;
	    	   }
	    	   inputMap.remove("InputType");
	    	   inputMap.remove("InputValue");
	       }else {
	    	    result.addParam(new Param("dbpErrCode","20000"));
	   			result.addParam(new Param("dbpErrMsg", "Invalid Input Params"));
	       }
	       logger.debug("HBL:KUKLGetCustomerBillInfo:payload:"+inputMap);
	       request.getHeaderMap().put(TemenosConstants.PARAM_AUTHORIZATION, Authorization);
		return true;
	}
	
	public String generateBasicAuthorization(String userName, String password) {
		String authorization=userName+":"+password;
		authorization= Base64.getEncoder().encodeToString(authorization.getBytes());
		return "Basic "+authorization;
	}

}
