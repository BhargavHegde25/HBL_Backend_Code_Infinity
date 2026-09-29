package com.bct.custom.backenddeligate.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bct.custom.backenddeligate.api.PaymentAggregatorBackendDeligate;
import com.bct.custom.constants.HBLURLConstants;
import com.bct.custom.dto.PaymentAggregatorDTO;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public class PaymentAggregatorBackendDeligateImpl implements PaymentAggregatorBackendDeligate{
	private static final Logger logger = LogManager.getLogger(PaymentAggregatorBackendDeligateImpl.class);

	

	@Override
	public JSONArray getAllPaymentAggregator(Map<String, Object> headerMap) throws ApplicationException {
		Map<String, Object> inputmap = new HashMap<>();
		//String filter = "Name eq 'NULL'";
		//inputmap.put(HBLURLConstants.FILTER, filter);
		logger.debug("BCT::PaymentAggregatorBackendDeligateImpl:getAllPaymentAggregator: inputmap:"+
				inputmap.toString());
		JSONArray types = new JSONArray();
		try {
			String response = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.GET_PAYMENT_AGGREGATOR_OPERATION)
					.withRequestParameters(inputmap).withServiceId(HBLURLConstants.C360_SERVICEID)
					.withRequestHeaders(headerMap).build().getResponse();
			logger.debug("BCT::PaymentAggregatorBackendDeligateImpl: getAllPaymentAggregator response:"+response);
			JSONObject responseJSON = new JSONObject(response);
			types = responseJSON.getJSONArray("paymentaggregator");
		}catch (Exception e) {
			logger.error("Exception caught while fetching PaymentAggregators:" +e.toString());
			
		}
		return types;
	}

	@Override
	public JSONArray getPaymentAggregator(String id,
			Map<String, Object> headerMap)throws ApplicationException {
		// TODO Auto-generated method stub
		Map<String, Object> inputmap = new HashMap<>();
		String filter = "id eq '" + id + "'";
		inputmap.put(HBLURLConstants.FILTER, filter);
		logger.debug("BCT::PaymentAggregatorBackendDeligateImpl: getPaymentAggregator inputmap:"+
				inputmap.toString());
		JSONArray types = new JSONArray();
		try {
			String response = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.GET_PAYMENT_AGGREGATOR_OPERATION)
					.withRequestParameters(inputmap).withServiceId(HBLURLConstants.HBL_OLB_CRUD_OPERATION_SERVICE)
					.withRequestHeaders(headerMap).build().getResponse();
			logger.debug("BCT::PaymentAggregatorBackendDeligateImpl: getPaymentAggregator response:"+response);
			JSONObject responseJSON = new JSONObject(response);
			types = responseJSON.getJSONArray("paymentaggregator");
		}catch (Exception e) {
			logger.error("Exception caught while fetching PaymentAggregators:" +e.toString());
			
		}
		return types;
	}

	@Override
	public Result updatePaymentAggregator(PaymentAggregatorDTO dto, Map<String, Object> headerMap, Map<String, Object> inputs)
			throws ApplicationException {
		logger.debug("BCT::PaymentAggregatorBackendDeligateImpl:updatePaymentAggregator: inputs:"+
				inputs.toString());
		Result response = new Result();
		 Map<String, Object> inputParams = new HashMap<>();
		 	inputParams.put("id", dto.getId());
	        inputParams.put("name", dto.getName());
	        inputParams.put("isActive", inputs.get("isActive"));
	        inputParams.put("modifiedby", inputs.get("modifiedby"));
	        inputParams.put("lastmodifiedts",inputs.get("currenttime").toString());
			inputParams.put("synctimestamp",inputs.get("currenttime").toString());
			logger.debug("BCT::PaymentAggregatorBackendDeligateImpl:updatePaymentAggregator: inputParams:"+
					inputParams.toString());
			try {
			String dbResponse = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.UPDATE_PAYMENT_AGGREGATOR_OPERATION)
					.withRequestParameters(inputParams).withServiceId(HBLURLConstants.C360_SERVICEID)
					.withRequestHeaders(headerMap).build().getResponse();
			logger.debug("BCT::PaymentAggregatorBackendDeligateImpl:updatePaymentAggregator: response:"+
					dbResponse);
				JSONObject responseJSON = new JSONObject(dbResponse);
				if(responseJSON.has("errmsg")) {
					response.setParam(new Param("httpStatusCode", responseJSON.get("httpStatusCode").toString()));
					response.addParam(new Param("dbpErrMsg", responseJSON.get("errmsg").toString()));
					response.addParam(new Param("dbpErrCode", responseJSON.get("opstatus").toString()));
					response.setParam(new Param("success", "false"));
				}
				else if(responseJSON.has("paymentaggregator")&& responseJSON.getJSONArray("paymentaggregator").length()>0) {
				JSONArray paymentaggregator = responseJSON.getJSONArray("paymentaggregator");
					Dataset dataset = HelperMethods.constructDatasetFromJSONArray(paymentaggregator);
					dataset.setId("paymentaggregator");
					response.addDataset(dataset);
				}
			}catch (Exception e) {
				logger.error("Exception caught while updatePaymentAggregator:" +e.toString());
				
			}
			return response;
	}

	@Override
	public Result createPaymentAggregator(PaymentAggregatorDTO dto, Map<String, Object> headerMap,
			Map<String, Object> inputs) throws ApplicationException {
		logger.debug("BCT::PaymentAggregatorBackendDeligateImpl:createPaymentAggregator: inputs:"+
				inputs.toString());
		Result response = new Result();
		 Map<String, Object> inputParams = new HashMap<>();
	        inputParams.put("name", dto.getName());
	        inputParams.put("isActive", inputs.get("isActive"));
	        inputParams.put("createdby", inputs.get("modifiedby"));
	        inputParams.put("modifiedby", inputs.get("modifiedby"));
	        inputParams.put("softdeleteflag", inputs.get("modifiedby"));
	        inputParams.put("createdts",inputs.get("currenttime").toString());
	        inputParams.put("lastmodifiedts",inputs.get("currenttime").toString());
			inputParams.put("synctimestamp",inputs.get("currenttime").toString());
			logger.debug("BCT::PaymentAggregatorBackendDeligateImpl:updatePaymentAggregator: inputParams:"+
					inputParams.toString());
			try {
			String dbResponse = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.CREATE_PAYMENT_AGGREGATOR_OPERATION)
					.withRequestParameters(inputParams).withServiceId(HBLURLConstants.C360_SERVICEID)
					.withRequestHeaders(headerMap).build().getResponse();
			logger.debug("BCT::PaymentAggregatorBackendDeligateImpl:createPaymentAggregator: response:"+
					dbResponse);
				JSONObject responseJSON = new JSONObject(dbResponse);
				if(responseJSON.has("errmsg")) {
					response.setParam(new Param("httpStatusCode", responseJSON.get("httpStatusCode").toString()));
					response.addParam(new Param("dbpErrMsg", responseJSON.get("errmsg").toString()));
					response.addParam(new Param("dbpErrCode", responseJSON.get("opstatus").toString()));
					response.setParam(new Param("success", "false"));
				}
				else if(responseJSON.has("paymentaggregator")&& responseJSON.getJSONArray("paymentaggregator").length()>0) {
				JSONArray paymentaggregator = responseJSON.getJSONArray("paymentaggregator");
					Dataset dataset = HelperMethods.constructDatasetFromJSONArray(paymentaggregator);
					dataset.setId("paymentaggregator");
					response.addDataset(dataset);
				}
			}catch (Exception e) {
				logger.error("Exception caught while createPaymentAggregator:" +e.toString());
				
			}
			return response;
	}

	
}
