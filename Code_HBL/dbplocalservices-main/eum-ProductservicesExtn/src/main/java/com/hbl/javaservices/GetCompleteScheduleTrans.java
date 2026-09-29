package com.hbl.javaservices;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.api.OperationData;
import com.konylabs.middleware.api.ServiceRequest;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetCompleteScheduleTrans implements JavaService2{
	private static LoggerUtil logger = new LoggerUtil(GetCompleteScheduleTrans.class);
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
		// TODO Auto-generated method stub
		
		Result result = new Result();
         //page_size=100&currentPaymentState=Complete&debitAccountId=105538
		result = (Result) callIntegrationService(request,null, null, "ArrangementsT24Services",
				"getUserPaymentOrders", true);
		System.out.println("Result1: "+ result);
		//listType=COMPLETED&accountId=105538&transactionCode=All&dateFrom=&page_size=100&page_start=1&dateTo=&minimumAmount=1&maximumAmount=100
		result = (Result) callIntegrationService(request,null, null, "ArrangementsT24ISTransactions",
				"getCompletedTransactions", true);
		System.out.println("Result2: "+ result);
		
		return result;
	}
	
	public static Result callIntegrationService(DataControllerRequest request, Map<String, Object> params,
			Map<String, Object> headers, String serviceName, String operationName, boolean setRequest)
			throws Exception {

		// Create service data
		OperationData serviceData = createOperationData(request, serviceName, operationName);

		// Create service request
		ServiceRequest serviceRequest = createServiceRequest(request, serviceData, params, headers, setRequest);

		// Ok now call the service
		Result result = serviceRequest.invokeServiceAndGetResult();
		return result;
	}
	
	private static OperationData createOperationData(DataControllerRequest request, String service, String operation)
			throws Exception {

		// Generate OperationData object
		OperationData serviceData = request.getServicesManager().getOperationDataBuilder().withServiceId(service)
				.withOperationId(operation).build();

		return serviceData;
	}
	
	private static ServiceRequest createServiceRequest(DataControllerRequest request, OperationData serviceData,
			Map<String, Object> params, Map<String, Object> headers, boolean setRequest) throws Exception {
		ServiceRequest serviceRequest;
		if (setRequest) {
			serviceRequest = request.getServicesManager().getRequestBuilder(serviceData).withInputs(params)
					.withDCRRequest(request).withHeaders(headers).build();
		} else {
			Enumeration<String> attributeNames = (Enumeration<String>) request.getSession().getAttributeNames();
			Map<String, Object> sessionMap = new HashMap<>();
			while (attributeNames.hasMoreElements()) {
				String attributeName = (String) attributeNames.nextElement();
				sessionMap.put(attributeName, request.getSession().getAttribute(attributeName));
			}
			String authToken = request.getHeader("X-Kony-Authorization");
			//For GET requests we need to take token from Auth_Token
            if (StringUtils.isBlank(authToken)) {
                authToken = request.getParameter("Auth_Token");
                request.getHeaderMap().put("X-Kony-Authorization", authToken);
                logger.error(request.getHeader("X-Kony-Authorization"));
            }
			serviceRequest = request.getServicesManager().getRequestBuilder(serviceData).withInputs(params)
					.withSessionMap(sessionMap).withAuthorizationToken(authToken).withHeaders(headers).build();
		}
		// Generate ServiceRequest object

		return serviceRequest;
	}

}
