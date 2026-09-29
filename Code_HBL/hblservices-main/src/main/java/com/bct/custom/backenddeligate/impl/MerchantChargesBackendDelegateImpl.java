package com.bct.custom.backenddeligate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bct.custom.backenddeligate.api.MerchantChargesBackendDelegate;
import com.bct.custom.constants.HBLURLConstants;
import com.bct.custom.dto.MerchantFieldsDTO;
import com.bct.custom.dto.MerchantPaymentCharges;
import com.bct.custom.resource.impl.MerchantChargesResourceImpl;
import com.bct.utilities.HBLCommonUtility;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;


public class MerchantChargesBackendDelegateImpl implements MerchantChargesBackendDelegate{
	private static final Logger logger = LogManager.getLogger(MerchantChargesBackendDelegateImpl.class);
	@Override
	public Result updateMerchantCharges(MerchantPaymentCharges dto, DataControllerRequest request,
			Map<String, Object> inputs) throws ApplicationException {
		logger.debug("BCT::MerchantChargesBackendDelegateImpl:updateMerchantCharges: inputs:" + inputs.toString());
		Result response = new Result();
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("id", dto.getId());
		if (dto.getMerchantCode() != null)
			inputParams.put("merchantCode", dto.getMerchantCode());
		if (dto.getMinAmount() != null)
			inputParams.put("minAmount", dto.getMinAmount());
		if (dto.getMaxAmount() != null)
			inputParams.put("maxAmount", dto.getMaxAmount());
		if (dto.getFee() != null)
			inputParams.put("fee", dto.getFee());
		if (dto.getChargesType() != null)
			inputParams.put("chargesType", dto.getChargesType());
		if (dto.getMaxTransactionAmount() != null)
		inputParams.put("maxTransactionAmount", dto.getMaxTransactionAmount());
		logger.debug(
				"BCT::MerchantChargesBackendDelegateImpl:updateMerchantCharges: dtoRequest:" + inputParams.toString());
		try {
			String dbResponse = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.UPDATE_MERCHANT_PAYMENT_CHARGES_OPERATION)
					.withRequestParameters(inputParams).withServiceId(HBLURLConstants.C360_SERVICEID)
					.withRequestHeaders(request.getHeaderMap()).build().getResponse();
			logger.debug("BCT::MerchantChargesBackendDelegateImpl:updateMerchantCharges: response:" + dbResponse);
			JSONObject responseJSON = new JSONObject(dbResponse);
			if (responseJSON.has("errmsg")) {
				response.setParam(new Param("httpStatusCode", responseJSON.get("httpStatusCode").toString()));
				response.addParam(new Param("dbpErrMsg", responseJSON.get("errmsg").toString()));
				response.addParam(new Param("dbpErrCode", responseJSON.get("opstatus").toString()));
				response.setParam(new Param("success", "false"));
			} else if (responseJSON.has("merchantpaymentcharges") && responseJSON.getJSONArray("merchantpaymentcharges").length() > 0) {
				JSONArray merchantDetails = responseJSON.getJSONArray("merchantpaymentcharges");
				Dataset dataset = HelperMethods.constructDatasetFromJSONArray(merchantDetails);
				dataset.setId("merchantpaymentcharges");
				response.addDataset(dataset);
				response.setParam(new Param("success", "true"));
			}
		} catch (Exception e) {
			logger.error("Exception caught while updateMerchantCharges:" + e.toString());

		}
		return response;
	}

	@Override
	public Result createMerchantCharges(ArrayList<MerchantPaymentCharges> merchants, DataControllerRequest dcRequest,
			Map<String, Object> inputs) throws ApplicationException {
		logger.debug("BCT::MerchantChargesBackendDelegateImpl:createMerchantCharges: dto:"+
				merchants.toString());
		Result response = new Result();
		MerchantPaymentCharges dto;
		String[] arr = new String[merchants.size()];
		String tableName=HBLURLConstants.TAB_MERCHANT_PAYMENT_CHARGES;
		String colSpec="(merchantCode,minAmount,maxAmount,fee,chargesType,maxTransactionAmount)";
		if(merchants.size()>0) {
		for(int i=0;i<merchants.size();i++) {
			dto=merchants.get(i);
		
		 Map<String, Object> inputParams = new HashMap<>();
	        inputParams.put("merchantCode", dto.getMerchantCode());
	        inputParams.put("minAmount", dto.getMinAmount());
		    inputParams.put("maxAmount", dto.getMaxAmount());
			inputParams.put("fee", dto.getFee());
			inputParams.put("maxTransactionAmount", dto.getMaxTransactionAmount());
	        inputParams.put("chargesType", dto.getChargesType());
			logger.debug("BCT::MerchantChargesBackendDelegateImpl:createMerchantCharges: inputParams:"+
					inputParams.toString());
			String qryValues="('"+dto.getMerchantCode()+"','"+dto.getMinAmount()+"','"+dto.getMaxAmount()+"','"+dto.getFee()+"'";
			if(StringUtils.isNotBlank(dto.getChargesType()))
				qryValues=qryValues+",'"+dto.getChargesType()+"'";
			if(StringUtils.isBlank(dto.getChargesType())) 
				qryValues=qryValues+",''";

				qryValues=qryValues+","+dto.getMaxTransactionAmount();

				 qryValues=qryValues+");";
				 
			arr[i]=qryValues;
		}
		
		response.appendResult(HBLCommonUtility.batchInsert(tableName, colSpec, arr, dcRequest));
		}
			return response;
	}

	@Override
	public JSONArray getMerchantCharges(String code, Map<String, Object> inputMap, Map<String, Object> headerMap) {
		Map<String, Object> inputmap = new HashMap<>();
		String filter = "merchantCode eq '" + code + "'";
		inputmap.put(HBLURLConstants.FILTER, filter);
		logger.debug("BCT::MerchantCategoriesBackendDeligateImpl: getMerchantCharges inputmap:"+
				inputmap.toString());
		JSONArray types = new JSONArray();
		try {
			String response = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.GET_MERCHANT_PAYMENT_CHARGES)
					.withRequestParameters(inputmap).withServiceId(HBLURLConstants.C360_SERVICEID)
					.withRequestHeaders(headerMap).build().getResponse();
			logger.debug("BCT::MerchantCategoriesBackendDeligateImpl: getMerchantCharges response:"+response);
			JSONObject responseJSON = new JSONObject(response);
			types = responseJSON.getJSONArray("merchantpaymentcharges");
		}catch (Exception e) {
			logger.error("Exception caught while fetching merchant fileds:" +e.toString());
			
		}
		return types;
	}
	@Override
	public Result updateMerchantCharges(ArrayList<MerchantPaymentCharges> merchantCharges, DataControllerRequest dcRequest,
			Map<String, Object> inputs) throws ApplicationException {
		Result response = new Result();
		Result updateResponse = null;
		int updateSuccessCount=0;
		int updateRequestCount=0;
		MerchantPaymentCharges dto;
		ArrayList<MerchantPaymentCharges> createMerchantCharges=new ArrayList<MerchantPaymentCharges>();
			for(int i=0;i<merchantCharges.size();i++) {
				dto=merchantCharges.get(i);
				if(dto.getIsNew()==true) {
					createMerchantCharges.add(dto);
				}else {
					updateRequestCount++;
					updateResponse=updateMerchantCharges(dto, dcRequest, inputs);
					logger.debug("BCT::MerchantChargesBackendDelegateImpl:updateMerchantCharges: response:"+i+" is Success:" + updateResponse.getParamValueByName("success"));
					if(updateResponse.getParamValueByName("success").equalsIgnoreCase("true")) {
						updateSuccessCount++;
					}
				}
			}
			if(updateRequestCount==updateSuccessCount) {
				if(createMerchantCharges.size()>0 ) {
					response=createMerchantCharges(createMerchantCharges, dcRequest, inputs);
					if(response.getErrMsgParamValue()!=null) {
						response.addParam(new Param("dbpErrCode", "HBL-5024"));
						response.addParam(new Param("dbpErrMsg", "Failed: to create merchant charges requests"));
					}
				}
			}else {
				response.addParam(new Param("dbpErrCode", "HBL-5023"));
				response.addParam(new Param("dbpErrMsg", "Failed: one of the update merchant charges request"));
			}
				
				
				return response;
		
	}

	
	@Override
	public Result deleteMerchantCharges(ArrayList<MerchantPaymentCharges> merchantCharges, DataControllerRequest request,
			Map<String, Object> inputs) throws ApplicationException {
		logger.debug("BCT::MerchantChargesBackendDelegateImpl:deleteMerchantCharges: inputs:" + inputs.toString());
		Result response = new Result();
		Map<String, Object> inputParams = new HashMap<>();
		MerchantPaymentCharges dto = merchantCharges.get(0);
		inputParams.put("id", dto .getId());
		/*if (dto.getMerchantCode() != null)
			inputParams.put("merchantCode", dto.getMerchantCode());
			*/
		logger.debug(
				"BCT::MerchantChargesBackendDelegateImpl:deleteMerchantCharges: dtoRequest:" + inputParams.toString());
		try {
			String dbResponse = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.DELETE_MERCHANT_PAYMENT_CHARGES)
					.withRequestParameters(inputParams).withServiceId(HBLURLConstants.C360_SERVICEID)
					.withRequestHeaders(request.getHeaderMap()).build().getResponse();
			logger.debug("BCT::MerchantChargesBackendDelegateImpl:updateMerchantCharges: response:" + dbResponse);
			JSONObject responseJSON = new JSONObject(dbResponse);
			if (responseJSON.has("errmsg")) {
				response.setParam(new Param("httpStatusCode", responseJSON.get("httpStatusCode").toString()));
				response.addParam(new Param("dbpErrMsg", responseJSON.get("errmsg").toString()));
				response.addParam(new Param("dbpErrCode", responseJSON.get("opstatus").toString()));
				response.setParam(new Param("success", "false"));
			} else if (responseJSON.has("deletedRecords")) {
				String count= responseJSON.getString("deletedRecords");
				response.setParam(new Param("success", "true"));
				response.setParam(new Param("deletedRecords",count));
			}
		} catch (Exception e) {
			logger.error("Exception caught while updateMerchantCharges:" + e.toString());

		}
		return response;
	}

}
