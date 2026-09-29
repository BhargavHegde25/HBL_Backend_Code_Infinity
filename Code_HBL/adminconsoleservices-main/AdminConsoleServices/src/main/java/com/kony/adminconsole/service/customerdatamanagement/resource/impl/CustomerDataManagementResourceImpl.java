package com.kony.adminconsole.service.customerdatamanagement.resource.impl;

import java.util.HashMap;
import java.util.Map;

import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.service.customerdatamanagement.businessdelegate.api.CustomerDataManagementBusinessDelegate;
import com.kony.adminconsole.service.customerdatamanagement.resource.api.CustomerDataManagementResource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class CustomerDataManagementResourceImpl implements CustomerDataManagementResource {
	CustomerDataManagementBusinessDelegate customerDataManagementBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
	            .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(CustomerDataManagementBusinessDelegate.class);

	public Result getSDPReport(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		Dataset failureMessagesSet = new Dataset("failureMessages");
		Record record = new Record();
		try {
			String body = request.getParameter("body");
			String params = request.getParameter("params");
			if (body == null || body.isEmpty() || params == null || params.isEmpty()) {
				throw new Exception("Invalid Input payload");
			}
			Map<String, Object> payloadMap = new HashMap<String, Object>();
			JSONObject payloadObj = new JSONObject(body);
			JSONObject paramsObj = new JSONObject(params);

			if(paramsObj.optJSONArray("reportType") == null || paramsObj.getJSONArray("reportType").isEmpty()) {
				throw new Exception("ReportType is Invalid / Not Available");
			}
			if(paramsObj.optJSONArray("requestId") == null || paramsObj.getJSONArray("requestId").isEmpty()) {
				throw new Exception("RequestId is Invalid / Not Available");
			}

			payloadMap.put("_input", payloadObj);
			payloadMap.put("params", paramsObj);

			JSONObject serviceResponse = customerDataManagementBusinessDelegate.getSDPReport(payloadMap, request);
			if (serviceResponse != null) {
				record = CommonUtilities.constructRecordFromJSONObject(serviceResponse);
				result.addOpstatusParam(serviceResponse.optInt("opstatus"));
				if(serviceResponse.optInt("httpStatusCode") != -1) {
					record.addParam("statusCode", "200");
					record.setId("body");
					result.addHttpStatusCodeParam(200);
					result.addRecord(record);
					result.addParam("success","true","boolean");
					result.addIntParam("status",200);
				} else {
					throw new Exception(StringUtils.isEmpty(serviceResponse.optString("errmsg"))?"Internal error occurred": serviceResponse.optString("errmsg"));
				}
			} else {
				result.addOpstatusParam(-1);
				throw new Exception("Get SDP Report from database failed");
			}
		} catch (Exception e) {
			record.addParam("errorMessage",e.getMessage());
			record.removeParamByName("errmsg");
			record.removeParamByName("opstatus");
			record.removeParamByName("httpStatusCode");
			record.addParam("statusCode", "400");
			failureMessagesSet.addRecord(record);
			result.addHttpStatusCodeParam(400);
			result.addParam("success","false","boolean");
			result.addIntParam("status",400);
		}
		result.addDataset(failureMessagesSet);
		return result;
	}
	
	@Override
	public Result triggerErasure(DataControllerRequest dataControllerRequest) {
		Result result = new Result();
		Dataset set = new Dataset("failureMessages");
		Record record = new Record();
		try {
			String body = dataControllerRequest.getParameter("body");
			String params = dataControllerRequest.getParameter("params");
			if (body == null || body.isEmpty() || params == null || params.isEmpty()) {
				throw new Exception("Invalid Input payload");
			}
			JSONObject payloadObj = new JSONObject(body);
			JSONObject paramsObj = new JSONObject(params);
			String erasureRequestId;
			if(paramsObj.optJSONArray("erasureRequestId") != null && !paramsObj.getJSONArray("erasureRequestId").isEmpty()) {
				erasureRequestId = paramsObj.getJSONArray("erasureRequestId").getString(0);
			} else {
				throw new Exception("Erasure Request Id is Invalid / Not Available");
			}
			Map<String, Object> payloadMap = new HashMap<>();
			payloadMap.put("_input", body);
			JSONObject responseObject = customerDataManagementBusinessDelegate.triggerErasure(payloadMap, dataControllerRequest);
			record.addParam("partyId", payloadObj.optString("partyId"));
			record.addParam("customerId", payloadObj.optString("customerId"));
			record.addParam("erasureRequestId", erasureRequestId);
			record.addParam("serviceId", payloadObj.optString("serviceId"));
			if (responseObject != null) {
				result.addOpstatusParam(responseObject.optInt("opstatus"));
				if (responseObject.optInt("httpStatusCode") != -1) {
					record.addParam("statusCode","200");
					result.addIntParam("status",200);
					result.addParam("success","true","boolean");
					result.addHttpStatusCodeParam(200);
					record.setId("body");
					result.addRecord(record);
				} else {
					throw new Exception(StringUtils.isEmpty(responseObject.optString("errmsg"))?"Internal error occurred": responseObject.optString("errmsg"));
				}
			} else {
				throw new Exception("Error occurred while erasing data");
			}
		} catch (Exception e) {
			result.addHttpStatusCodeParam(400);
			record.addParam("statusCode","400");
			record.addParam("errorMessage", e.getMessage());
			result.addIntParam("status",400);
			result.addParam("success","false","boolean");
			set.addRecord(record);
		}
		result.addDataset(set);
		return result;
	}

	public Result updateCustomerErasureStatus(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		try {
			Map<String, Object> payloadMap = new HashMap<String, Object>();
			String body = request.getParameter("body");
			if (body == null || body.isEmpty()) {
				throw new Exception("Invalid Input payload");
			}
			JSONObject bodyObj = new JSONObject(body);

			String customerId = bodyObj.optString("partyId");
			String erasureStatus = bodyObj.optString("erasureStatus");
			if (customerId == null || customerId.isEmpty() || erasureStatus == null || erasureStatus.isEmpty()) {
				throw new Exception("Invalid Input payload");
			}
			result.addParam("partyId", customerId);
			if(erasureStatus.equals("ERASED") || erasureStatus.equals("ERASURE.IN.PROGRESS")){
				payloadMap.put("_customerId", customerId);
				payloadMap.put("_erasureStatus", erasureStatus);
				JSONObject responseObject = customerDataManagementBusinessDelegate.updateCustomerErasureStatus(payloadMap, request);
				if (responseObject != null) {
					if (responseObject.optInt("httpStatusCode") != -1) {
						if(responseObject.optJSONArray("records") != null) {
							String errorMessage = responseObject.optJSONArray("records").optJSONObject(0).optString("errmsg");
							if(!StringUtils.isEmpty(errorMessage)) {
								throw new Exception(errorMessage.equals("ALREADY_ERASED") ? "Customer data has already been erased" : "Erasure Not initiated for Customer");
							}
						}
						result.addParam("statusCode", "200");
						result.addOpstatusParam(0);
						result.addHttpStatusCodeParam(200);
					} else {
						throw new Exception("Update Customer Erasure Status Failed");
					}
				} else {
					throw new Exception("Update Customer Erasure Status Failed");
				}
			} else {
				throw new Exception("Invalid Customer Erasure Status in Payload");
			}
		} catch (Exception e) {
			result.addErrMsgParam(e.getMessage());
			result.addParam("statusCode", "400");
			result.addOpstatusParam(-1);
			result.addHttpStatusCodeParam(400);
		}
		return result;
	}

	public Result applicationPurge(String methodId, Object[] inputArray, DataControllerRequest request,
								   DataControllerResponse response) throws Exception {
		Result result = new Result();
		try {
			String body = request.getParameter("body");
			if (body == null || body.isEmpty()) {
				throw new Exception("Invalid Input payload");
			}
			JSONObject payloadObj = new JSONObject(body);

			if(!payloadObj.has("entry") || payloadObj.getString("entry").isEmpty()) {
				throw new Exception("Invalid Input payload");
			}

			JSONArray entryArray = new JSONArray(payloadObj.getString("entry"));
			String digitalProfileId = "";

			for(Object entry : entryArray) {
				JSONObject entryObj = (JSONObject) entry;
				if(entryObj.has("entry") && !entryObj.get("entry").toString().isEmpty()){
					JSONObject entryJSONObj = new JSONObject(entryObj.get("entry").toString());
					if(entryJSONObj.has("DigitalProfileId") && !entryJSONObj.getString("DigitalProfileId").trim().isEmpty()){
						digitalProfileId = entryJSONObj.getString("DigitalProfileId");
						break;
					}
				}
			}

			if(!digitalProfileId.isEmpty()) {
				Map<String, Object> payloadMap = new HashMap<>();
				payloadMap.put("_input", digitalProfileId);
				result.addParam("digitalProfileId",digitalProfileId);
				JSONObject responseObject = customerDataManagementBusinessDelegate.applicationPurge(payloadMap, request);
				if (responseObject != null) {
					if (responseObject.optInt("httpStatusCode") != -1) {
						result.addParam("statusCode", "200");
						result.addOpstatusParam(0);
						result.addHttpStatusCodeParam(200);
					} else {
						throw new Exception("Application Purge Operation Failed");
					}
				} else {
					throw new Exception("Application Purge Operation Failed");
				}
			} else {
				throw new Exception("digitalProfileId is not available in Payload");
			}
		} catch(Exception e){
			result.addErrMsgParam(e.getMessage());
			result.addParam("statusCode", "400");
			result.addOpstatusParam(-1);
			result.addHttpStatusCodeParam(400);
		}
		return result;
	}

}
