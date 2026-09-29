package com.kony.adminconsole.service.productmanagement.javaservices;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.adminconsole.commons.handler.EnvironmentConfigurationsHandler;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.constants.TemenosConstantsC360;
import com.kony.adminconsole.jwt.auth.utils.CommonUtilsC360;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.ServiceId;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

public class GetProductLines implements JavaService2 {
	
	public static final String PARAM_AUTHORIZATION = "Authorization";
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		Map<String, Object> headerMap = new HashMap<>();
		Map<String, Object> postParametersMap = new HashMap<>();
		
		String backendToken = request.getHeader("x-kony-authorization");
        headerMap.put("x-kony-authorization", backendToken);
        String marketingCatalogBackend = EnvironmentConfigurationsHandler.getServerAppPropertyValue("MARKETING_CATALOG_BACKEND",request);
        try {
        if(marketingCatalogBackend.equals("MS")) {
        CommonUtilsC360.setAuthenticationHeader(request,
             		TemenosConstantsC360.POST_LOGIN_FLOW, "ms");
        String authToken = request.getParameter(PARAM_AUTHORIZATION);
        headerMap.put(PARAM_AUTHORIZATION, authToken);
        postParametersMap.put(PARAM_AUTHORIZATION, authToken);
	   	String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.PRODUCTMANAGEMENTMS)
                        .withOperationId(OperationName.OP_GET_PRODUCT_LINES)
                        .withRequestParameters(postParametersMap)
                        .withRequestHeaders(headerMap)
                        .build()
                        .getResponse();
	   	result = CommonUtilities.constructResultFromJSONObject(CommonUtilities.getStringAsJSONObject(serviceResponse));
        } else {
        	String serviceResponse = DBPServiceExecutorBuilder.builder().
                    withServiceId(ServiceId.CRUDLAYER).
                    withOperationId(OperationName.DB_GET_PRODUCT_LINES).
                    withRequestParameters(postParametersMap).
                    build().getResponse();
        	result = CommonUtilities.constructResultFromJSONObject(CommonUtilities.getStringAsJSONObject(serviceResponse));
        }		
		return result;        
	} catch (Exception e) {
        Result errorResult = new Result();
        alert.prepareError("Runtime Exception.Exception Trace:", e).log();
        ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
        return errorResult;
    }
  }
}
