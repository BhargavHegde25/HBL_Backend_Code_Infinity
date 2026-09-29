package com.bct.custom.backenddeligate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bct.custom.backenddeligate.api.MerchantFieldsBackendDeligate;
import com.bct.custom.constants.HBLURLConstants;
import com.bct.custom.dto.MerchantDTO;
import com.bct.custom.dto.MerchantFieldsDTO;
import com.bct.custom.dto.MerchantPaymentCharges;
import com.bct.utilities.HBLCommonUtility;
import com.dbp.core.error.DBPError;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public class MerchantFieldsBackendDeligateImpl implements MerchantFieldsBackendDeligate {
	private static final Logger logger = LogManager.getLogger(MerchantFieldsBackendDeligateImpl.class);

	@Override
	public Result updateMerchantFileds(MerchantFieldsDTO dto, DataControllerRequest request, Map<String, Object> inputs)
			throws ApplicationException {
		logger.debug("BCT::MerchantFieldsBackendDeligateImpl:updateMerchantFileds: inputs:" + inputs.toString());
		Result response = new Result();
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("id", dto.getId());
		if (dto.getMerchantCode() != null)
			inputParams.put("merchantCode", dto.getMerchantCode());
		if (dto.getFieldname() != null)
			inputParams.put("fieldname", dto.getFieldname());
		if (dto.getFieldi18n() != null)
			inputParams.put("fieldi18n", dto.getFieldi18n());
		if (dto.getFieldtype() != null)
			inputParams.put("fieldtype", dto.getFieldtype());
		if (dto.getFieldvalue() != null)
			inputParams.put("fieldvalue", dto.getFieldvalue());
		if (dto.getFieldindex() != null)
			inputParams.put("fieldindex", dto.getFieldindex());
		if (dto.getIsMandatory() != null)
			inputParams.put("isMandatory", dto.getIsMandatory());
		if (dto.getFieldname_np() != null)
			inputParams.put("fieldname_np", dto.getFieldname_np());
		if (dto.getPlaceholder() != null)
			inputParams.put("placeholder", dto.getPlaceholder());
		if (dto.getFieldcategory() != null)
		inputParams.put("fieldcategory", dto.getFieldcategory());
		if (dto.getParamKey() != null)
			inputParams.put("paramKey", dto.getParamKey());
		logger.debug(
				"BCT::MerchantFieldsBackendDeligateImpl:updateMerchantFileds: dtoRequest:" + inputParams.toString());
		try {
			String dbResponse = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.UPDATE_MERCHANT_FIELDS_OPERATION)
					.withRequestParameters(inputParams).withServiceId(HBLURLConstants.C360_SERVICEID)
					.withRequestHeaders(request.getHeaderMap()).build().getResponse();
			logger.debug("BCT::updateMerchantFileds:updateMerchantFileds: response:" + dbResponse);
			JSONObject responseJSON = new JSONObject(dbResponse);
			if (responseJSON.has("errmsg")) {
				response.setParam(new Param("httpStatusCode", responseJSON.get("httpStatusCode").toString()));
				response.addParam(new Param("dbpErrMsg", responseJSON.get("errmsg").toString()));
				response.addParam(new Param("dbpErrCode", responseJSON.get("opstatus").toString()));
				response.setParam(new Param("success", "false"));
			} else if (responseJSON.has("merchantfields") && responseJSON.getJSONArray("merchantfields").length() > 0) {
				JSONArray merchantDetails = responseJSON.getJSONArray("merchantfields");
				Dataset dataset = HelperMethods.constructDatasetFromJSONArray(merchantDetails);
				dataset.setId("merchantfields");
				response.addDataset(dataset);
				response.setParam(new Param("success", "true"));
			}
		} catch (Exception e) {
			logger.error("Exception caught while updateMerchantFileds:" + e.toString());

		}
		return response;
	}
	@Override
	public Result updateMerchantFileds(ArrayList<MerchantFieldsDTO> merchantFields, DataControllerRequest dcRequest,
			Map<String, Object> inputs) throws ApplicationException {
		Result response = new Result();
		Result updateResponse = null; 
		MerchantFieldsDTO dto;
		int updateSuccessCount=0;
		int updateRequestCount=0;
		ArrayList<MerchantFieldsDTO> createMerchantFields=new ArrayList<MerchantFieldsDTO>();
			for(int i=0;i<merchantFields.size();i++) {
				dto=merchantFields.get(i);
				logger.debug("BCT::updateMerchantFileds:updateMerchantFileds: dto Request:"+dto.toString());
				if(dto.getIsNew()==true) {
					createMerchantFields.add(dto);
				}else {
					updateRequestCount++;
					updateResponse=updateMerchantFileds(dto, dcRequest, inputs);
					logger.debug("BCT::updateMerchantFileds:updateMerchantFileds: response:"+i+" is Success:" + updateResponse.getParamValueByName("success"));
					if(updateResponse.getParamValueByName("success").equalsIgnoreCase("true")) {
						updateSuccessCount++;
					}
				}
			}
				if(updateRequestCount==updateSuccessCount) {
					if( createMerchantFields.size()>0 ) {
					response=createMerchantFileds(createMerchantFields, dcRequest, inputs);
					if(response.getErrMsgParamValue()!=null) {
						response.addParam(new Param("dbpErrCode", "HBL-5022"));
						response.addParam(new Param("dbpErrMsg", "Failed: to create merchant field requests"));
					}
					}
				}else {
					response.addParam(new Param("dbpErrCode", "HBL-5021"));
					response.addParam(new Param("dbpErrMsg", "Failed: one of the update merchant field request"));
				}
				
				
				return response;
		
	}

	@Override
	public Result createMerchantFileds(MerchantFieldsDTO dto, DataControllerRequest request, Map<String, Object> inputs)
			throws ApplicationException {
		Result response = new Result();
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("merchantCode", dto.getMerchantCode());
		inputParams.put("fieldname", dto.getFieldname());
		inputParams.put("fieldi18n", dto.getFieldi18n());
		inputParams.put("fieldtype", dto.getFieldtype());
		inputParams.put("fieldvalue", dto.getFieldvalue());
		inputParams.put("fieldindex", dto.getFieldindex());
		inputParams.put("isMandatory", dto.getIsMandatory());
		inputParams.put("fieldname_np", dto.getFieldname_np());
		inputParams.put("placeholder", dto.getPlaceholder());
		inputParams.put("fieldcategory", dto.getFieldcategory());
		inputParams.put("paramKey", dto.getParamKey());
		logger.debug(
				"BCT::MerchantFieldsBackendDeligateImpl:createMerchantFileds: dtoRequest:" + inputParams.toString());
		try {
			String dbResponse = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.CREATE_MERCHANT_FIELDS_OPERATION)
					.withRequestParameters(inputParams).withServiceId(HBLURLConstants.C360_SERVICEID)
					.withRequestHeaders(request.getHeaderMap()).build().getResponse();
			logger.debug("BCT::updateMerchantFileds:createMerchantFileds: response:" + dbResponse);
			JSONObject responseJSON = new JSONObject(dbResponse);
			if (responseJSON.has("errmsg")) {
				response.setParam(new Param("httpStatusCode", responseJSON.get("httpStatusCode").toString()));
				response.addParam(new Param("dbpErrMsg", responseJSON.get("errmsg").toString()));
				response.addParam(new Param("dbpErrCode", responseJSON.get("opstatus").toString()));
				response.setParam(new Param("success", "false"));
			} else if (responseJSON.has("merchantfields") && responseJSON.getJSONArray("merchantfields").length() > 0) {
				JSONArray merchantDetails = responseJSON.getJSONArray("merchantfields");
				Dataset dataset = HelperMethods.constructDatasetFromJSONArray(merchantDetails);
				dataset.setId("merchantfields");
				response.addDataset(dataset);
			}
		} catch (Exception e) {
			logger.error("Exception caught while createMerchantFileds:" + e.toString());

		}
		return response;
	}

	@Override
	public Result createMerchantFileds(ArrayList<MerchantFieldsDTO> merchants, DataControllerRequest dcRequest,
			Map<String, Object> inputs) throws ApplicationException {
		logger.debug("BCT::MerchantFieldsBackendDeligateImpl:createMerchantFileds: dto:"+
				merchants.toString());
		Result response = new Result();
		MerchantFieldsDTO dto;
		String[] arr = new String[merchants.size()];
		String tableName=HBLURLConstants.TAB_MERCHANT_FIELDS;
		String colSpec="(merchantCode,fieldname,fieldi18n,fieldtype,fieldindex,fieldvalue,isMandatory,fieldname_np,placeholder,fieldcategory,paramKey)";
		
		if(merchants.size()>0) {
		for(int i=0;i<merchants.size();i++) {
			dto=merchants.get(i);
		
		 Map<String, Object> inputParams = new HashMap<>();
	        inputParams.put("merchantCode", dto.getMerchantCode());
	        inputParams.put("fieldname", dto.getFieldname());
		    inputParams.put("fieldtype", dto.getFieldtype());
			inputParams.put("fieldindex", dto.getFieldindex());
	        inputParams.put("fieldvalue", dto.getFieldvalue());
	        inputParams.put("isMandatory", dto.getIsMandatory());
	        inputParams.put("fieldi18n", dto.getFieldi18n());
	        inputParams.put("fieldname_np", dto.getFieldname_np());
	        inputParams.put("placeholder", dto.getPlaceholder());
	        inputParams.put("fieldcategory", dto.getFieldcategory());
	        inputParams.put("paramKey", dto.getParamKey());
			logger.debug("BCT::MerchantFieldsBackendDeligateImpl:createMerchantFileds: inputParams:"+
					inputParams.toString());
			String qryValues="('"+dto.getMerchantCode()+"','"+dto.getFieldname()+"','"+dto.getFieldi18n()+"','"+dto.getFieldtype()+"',"+dto.getFieldindex()+"";
			
			if(StringUtils.isNotBlank(dto.getFieldvalue()))
				qryValues=qryValues+",'"+dto.getFieldvalue()+"'";
			if(StringUtils.isBlank(dto.getFieldvalue())) 
				qryValues=qryValues+",''";
			
				qryValues=qryValues+","+dto.getIsMandatory();
				
				if(StringUtils.isNotBlank(dto.getFieldname_np()))
					qryValues=qryValues+",'"+dto.getFieldname_np()+"'";
				if(StringUtils.isBlank(dto.getFieldname_np())) 
					qryValues=qryValues+",''";
				if(StringUtils.isNotBlank(dto.getPlaceholder()))
					qryValues=qryValues+",'"+dto.getPlaceholder()+"'";
				if(StringUtils.isBlank(dto.getPlaceholder())) 
					qryValues=qryValues+",''";
				if(StringUtils.isNotBlank(dto.getFieldcategory()))
					qryValues=qryValues+",'"+dto.getFieldcategory()+"'";
				if(StringUtils.isBlank(dto.getFieldcategory())) 
					qryValues=qryValues+",''";
				if(StringUtils.isNotBlank(dto.getParamKey()))
					qryValues=qryValues+",'"+dto.getParamKey()+"'";
				if(StringUtils.isBlank(dto.getParamKey())) 
					qryValues=qryValues+",''";
				 qryValues=qryValues+");";
				 
			arr[i]=qryValues;
		}
		
		
		response.appendResult(HBLCommonUtility.batchInsert(tableName, colSpec, arr, dcRequest));
		}
			return response;
	}

	@Override
	public JSONArray getMerchantFileds(String code, Map<String, Object> inputMap, Map<String, Object> headerMap)
			throws ApplicationException {
		Map<String, Object> inputmap = new HashMap<>();
		String filter = "merchantCode eq '" + code + "'";
		inputmap.put(HBLURLConstants.FILTER, filter);
		logger.debug("BCT::MerchantCategoriesBackendDeligateImpl: getMerchantFields inputmap:"+
				inputmap.toString());
		JSONArray types = new JSONArray();
		try {
			String response = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.GET_MERCHANT_FIELDS_OPERATION)
					.withRequestParameters(inputmap).withServiceId(HBLURLConstants.C360_SERVICEID)
					.withRequestHeaders(headerMap).build().getResponse();
			logger.debug("BCT::MerchantCategoriesBackendDeligateImpl: getMerchantFields response:"+response);
			JSONObject responseJSON = new JSONObject(response);
			types = responseJSON.getJSONArray("merchantfields");
		}catch (Exception e) {
			logger.error("Exception caught while fetching merchant fileds:" +e.toString());
			
		}
		return types;
	}
	
	@Override
	public Result deleteMerchantFileds(ArrayList<MerchantFieldsDTO> merchantFields, DataControllerRequest request,
			Map<String, Object> inputs) throws ApplicationException {
		logger.debug("BCT::MerchantFieldsBackendDeligateImpl:deleteMerchantFileds: inputs:" + inputs.toString());
		Result response = new Result();
		Map<String, Object> inputParams = new HashMap<>();
		MerchantFieldsDTO dto = merchantFields.get(0);
		inputParams.put("id", dto .getId());
		/*if (dto.getMerchantCode() != null)
			inputParams.put("merchantCode", dto.getMerchantCode());
			*/
		logger.debug(
				"BCT::MerchantFieldsBackendDeligateImpl:deleteMerchantCharges: dtoRequest:" + inputParams.toString());
		try {
			String dbResponse = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.DELETE_MERCHANT_FIELDS)
					.withRequestParameters(inputParams).withServiceId(HBLURLConstants.C360_SERVICEID)
					.withRequestHeaders(request.getHeaderMap()).build().getResponse();
			logger.debug("BCT::MerchantFieldsBackendDeligateImpl:deleteMerchantFileds: response:" + dbResponse);
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
