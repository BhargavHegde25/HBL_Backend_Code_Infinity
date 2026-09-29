package com.kony.dbp.alertsenginejwtgeneration.jwt.auth.utils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.util.EntityUtils;

import com.konylabs.middleware.api.OperationData;
import com.konylabs.middleware.api.ServiceRequest;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

public class CommonUtils{
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");

	public static String replaceSchemaName(String operationid, String schemaname) {
	    if (operationid == null || schemaname == null)
	      return operationid; 
	    if (operationid.contains("{schema_name}"))
	      operationid = operationid.replace("{schema_name}", schemaname); 
	    return operationid;
	  }

    public static Object callInternalService(String serviceID, String operationID, HashMap<String, Object> inputmap,
            HashMap<String, Object> headermap, DataControllerRequest request, int resultType, boolean setRequest) {
        Result result = null;
        String response = null;
        try {
            OperationData operationData = request.getServicesManager().getOperationDataBuilder()
                    .withServiceId(serviceID).withOperationId(operationID).build();

            ServiceRequest serviceRequest = null;
            if (setRequest) {
                serviceRequest = request.getServicesManager()
                        .getRequestBuilder(operationData).withDCRRequest(request).withInputs(inputmap)
                        .withHeaders(headermap)
                        .build();
            } else {
                serviceRequest = request.getServicesManager()
                        .getRequestBuilder(operationData).withInputs(inputmap).withHeaders(headermap)
                        .build();
            }

            switch (resultType) {
                case 1:
                    result = serviceRequest.invokeServiceAndGetResult();
                    break;
                case 2:
                    BufferedHttpEntity bufferedResponse = serviceRequest.invokePassThroughServiceAndGetEntity();
                    response = EntityUtils.toString(bufferedResponse, StandardCharsets.UTF_8);
                    break;
                default:
                    result = serviceRequest.invokeServiceAndGetResult();
                    break;
            }
        } catch (Exception e) {
            // TODO: handle exception
        	alert.prepareError("Error occurred: ", e).log();
        }
        switch (resultType) {
            case 1:
                return result;
            case 2:
                return response;
            default:
                return result;
        }
    }

}
