package com.temenos.infinity.api.chequemanagement.javaservice;
import com.temenos.infinity.api.chequemanagement.config.ChequeManagementAPIServices;
import com.temenos.infinity.api.chequemanagement.resource.api.GetCommandNameResource;
import com.kony.dbputilities.util.Log4j2Configurator;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;


public class GetServiceRequestOperation implements JavaService2{
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		String SRresponse = new String();
		Result result =new Result();
		Map<String, Object> inputMap = (HashMap<String, Object>) inputArray[1];
		Map<String, Object> headerMap = new HashMap<>();
		try {
			String operation=request.getParameter("operation");
			if(operation.equalsIgnoreCase("create")) {
				inputMap.put("subtype", request.getParameter("subType"));
				inputMap.remove("subType");
				SRresponse=DBPServiceExecutorBuilder.builder()
	                    .withServiceId(ChequeManagementAPIServices.SERVICEREQUESTJAVA_CREATEORDER.getServiceName())
	                    .withOperationId(ChequeManagementAPIServices.SERVICEREQUESTJAVA_CREATEORDER.getOperationName())
	                    .withRequestParameters(inputMap).withRequestHeaders(headerMap).withDataControllerRequest(request)
	                    .build().getResponse();
				JSONObject json = new JSONObject(SRresponse);
				result = JSONToResult.convert(json.toString());
				return result;
			}
			else {
				SRresponse=DBPServiceExecutorBuilder.builder()
	                    .withServiceId(ChequeManagementAPIServices.SERVICEREQUESTJAVA_GETORDERDETAILS.getServiceName())
	                    .withOperationId(ChequeManagementAPIServices.SERVICEREQUESTJAVA_GETORDERDETAILS.getOperationName())
	                    .withRequestParameters(inputMap).withRequestHeaders(headerMap).withDataControllerRequest(request)
	                    .build().getResponse();
				JSONObject json = new JSONObject(SRresponse);
				result = JSONToResult.convert(json.toString());
				return result;
				
			}
		}
		catch(Exception e) {
			diagnostic.prepareInfo("error  in selecting the ServiceRequestOperation", e).log();
		}
		
		return null;
	}
}
