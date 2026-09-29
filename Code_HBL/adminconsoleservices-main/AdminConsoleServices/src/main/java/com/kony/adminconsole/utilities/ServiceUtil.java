package com.kony.adminconsole.utilities;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.fabric.extn.DBPServiceExecutor;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.AlertManagementHandler;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public final class ServiceUtil {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	
	public static Result invokeService(ServiceURLEnum serviceURL, Map<String, Object> requestParameters,
	           Map<String, Object> requestHeaders,DataControllerRequest request) {
		   try {	                  
	           DBPServiceExecutor serviceExecutor = DBPServiceExecutorBuilder.builder()
	        		   .withServiceId(serviceURL.getServiceName())
	                   .withOperationId(serviceURL.getOperationName(request))
	                   .withRequestParameters(requestParameters).withRequestHeaders(requestHeaders)
	                   .withDataControllerRequest(request).build();
	           return serviceExecutor.getResult();
	       } catch (Exception e) {
	           alert.prepareError("Exception in invokeService", e).log();
	           return null;
	       }
	   }

	private ServiceUtil() {		
	}
	
	public static Result invokeGetService(DataControllerRequest requestInstance , 
			ServiceURLEnum serviceName, String filterStr,
			String selectStr, String tableName, ErrorCodeEnum errorcode) throws ApplicationException {
		Map<String, Object> inputMap = new HashMap<>();
		if(StringUtils.isNotBlank(filterStr) ) {
			inputMap.put(ODataQueryConstants.FILTER, filterStr);
		}
		if(StringUtils.isNotBlank(selectStr)) {
			inputMap.put(ODataQueryConstants.SELECT, selectStr);
		}
		Result alertsubTypeResult = ServiceUtil.invokeService(serviceName, inputMap, null,
				requestInstance);
		AlertManagementHandler.isOperationSuccessful(alertsubTypeResult,serviceName,tableName,errorcode);
		return alertsubTypeResult;
	}
}
