package com.bct.custom.backenddeligate.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bct.custom.backenddeligate.api.MerchantCategoriesBackendDeligate;
import com.bct.custom.backenddeligate.api.MerchantDetailsBackendDeligate;
import com.bct.custom.constants.HBLURLConstants;
import com.bct.custom.dto.MerchantCategoriesDTO;
import com.bct.custom.dto.MerchantDTO;
import com.bct.utilities.HBLCommonUtility;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbx.util.HikariConfiguration;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public class MerchantDetailsBackendDeligateImpl implements MerchantDetailsBackendDeligate{
	private static final Logger logger = LogManager.getLogger(MerchantDetailsBackendDeligateImpl.class);

	@Override
	public Result updateMerchantDetails(MerchantDTO dto, DataControllerRequest request, Map<String, Object> inputs)
			throws ApplicationException {
		Result response = new Result();
		 Map<String, Object> inputParams = new HashMap<>();
		 	if(dto.getId()==null) {
		 		response.addParam(new Param("dbpErrCode", "Missing Merchant Id"));
		 		response.addParam(new Param("dbpErrMsg", "Merchant Id is mandatory"));
				response.setParam(new Param("success", "false"));
				return response;
		 	}
		 	inputParams.put("id", dto.getId());
		 	if(StringUtils.isNotBlank(dto.getCode()))
	        inputParams.put("code", dto.getCode());
		 	if(StringUtils.isNotBlank(dto.getCategoryof()))
	        inputParams.put("categoryof", dto.getCategoryof());
		 	if(StringUtils.isNotBlank(dto.getLogoUrl()))
		    inputParams.put("logoUrl", dto.getLogoUrl());
		 	if(StringUtils.isNotBlank(dto.getPaymentaggregator()))
			inputParams.put("paymentaggregator", dto.getPaymentaggregator());
		 	if(dto.getIsActive()!=null)
	        inputParams.put("isActive", dto.getIsActive());
		 	if(StringUtils.isNotBlank(dto.getLabelText()))
		        inputParams.put("labelText", dto.getLabelText());
	        if(StringUtils.isNotBlank(dto.getFieldSetup()))
		        inputParams.put("fieldSetup", dto.getFieldSetup());
	        //if(StringUtils.isNotBlank(dto.getDisclimerMessage()))
		        inputParams.put("disclimerMessage", dto.getDisclimerMessage());
	        //if(StringUtils.isNotBlank(dto.getOutageMessage()))
		        inputParams.put("outageMessage", dto.getOutageMessage());
	        if(StringUtils.isNotBlank(dto.getSubcategory()))
		        inputParams.put("subcategory", dto.getSubcategory());
	        if(StringUtils.isNotBlank(dto.getSubcategoryCode()))
		        inputParams.put("subcategoryCode", dto.getSubcategoryCode());
	        if(StringUtils.isNotBlank(dto.getCategoryCode()))
		        inputParams.put("categoryCode", dto.getCategoryCode());
	        if(StringUtils.isNotBlank(dto.getPlaceholderText()))
		        inputParams.put("placeholderText", dto.getPlaceholderText());
	        if(StringUtils.isNotBlank(dto.getMaxTransactionLimit()))
		        inputParams.put("maxTransactionLimit", dto.getMaxTransactionLimit());
	        

			logger.debug("BCT::MerchantDetailsBackendDeligateImpl:updateMerchantDetails: dto:"+
					inputParams.toString());
			try {
			String dbResponse = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.UPDATE_MERCHANT_DETAILS_OPERATION)
					.withRequestParameters(inputParams).withServiceId(HBLURLConstants.C360_SERVICEID)
					.withRequestHeaders(request.getHeaderMap()).build().getResponse();
			logger.debug("BCT::MerchantDetailsBackendDeligateImpl:updateMerchantDetails: response:"+
					dbResponse);
				JSONObject responseJSON = new JSONObject(dbResponse);
				if(responseJSON.has("errmsg")) {
					response.setParam(new Param("httpStatusCode", responseJSON.get("httpStatusCode").toString()));
					response.addParam(new Param("dbpErrMsg", responseJSON.get("errmsg").toString()));
					response.addParam(new Param("dbpErrCode", responseJSON.get("opstatus").toString()));
					response.setParam(new Param("success", "false"));
				}
				else if(responseJSON.has("merchantdetails")&& responseJSON.getJSONArray("merchantdetails").length()>0) {
				JSONArray merchantDetails = responseJSON.getJSONArray("merchantdetails");
					Dataset dataset = HelperMethods.constructDatasetFromJSONArray(merchantDetails);
					dataset.setId("merchantdetails");
					response.addDataset(dataset);
					updateMerchantCategories(dto, request, inputs);
				}
			}catch (Exception e) {
				logger.error("Exception caught while updateMerchantDetails:" +e.toString());
				
			}
			return response;
	}

	@Override
	public Result createMerchantDetails(MerchantDTO dto, DataControllerRequest request, Map<String, Object> inputs)
			throws ApplicationException {
		Result response = new Result();
		 Map<String, Object> inputParams = new HashMap<>();
	        inputParams.put("code", dto.getCode());
	        inputParams.put("categoryof", dto.getCategoryof());
		    inputParams.put("logoUrl", dto.getLogoUrl());
			inputParams.put("paymentaggregator", dto.getPaymentaggregator());
	        inputParams.put("isActive", dto.getIsActive());
	        inputParams.put("labelText", dto.getLabelText());
	        inputParams.put("fieldSetup", dto.getFieldSetup());
	        inputParams.put("disclimerMessage", dto.getDisclimerMessage());
	        inputParams.put("outageMessage", dto.getOutageMessage());
	        inputParams.put("subcategory", dto.getSubcategory());
	        inputParams.put("subcategoryCode", dto.getSubcategoryCode());
	        inputParams.put("categoryCode", dto.getCategoryCode());
	        inputParams.put("placeholderText", dto.getPlaceholderText());
	        inputParams.put("maxTransactionLimit", dto.getMaxTransactionLimit());

			logger.debug("BCT::MerchantDetailsBackendDeligateImpl:createMerchantDetails: inputParams:"+
					inputParams.toString());
			try {
			String dbResponse = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.CREATE_MERCHANT_DETAILS_OPERATION)
					.withRequestParameters(inputParams).withServiceId(HBLURLConstants.C360_SERVICEID)
					.withRequestHeaders(request.getHeaderMap()).build().getResponse();
			logger.debug("BCT::MerchantDetailsBackendDeligateImpl:createMerchantDetails: response:"+
					dbResponse);
				JSONObject responseJSON = new JSONObject(dbResponse);
				if(responseJSON.has("errmsg")) {
					response.setParam(new Param("httpStatusCode", responseJSON.get("httpStatusCode").toString()));
					response.addParam(new Param("dbpErrMsg", responseJSON.get("errmsg").toString()));
					response.addParam(new Param("dbpErrCode", responseJSON.get("opstatus").toString()));
					response.setParam(new Param("success", "false"));
				}
				else if(responseJSON.has("merchantdetails")&& responseJSON.getJSONArray("merchantdetails").length()>0) {
				JSONArray merchantDetails = responseJSON.getJSONArray("merchantdetails");
					Dataset dataset = HelperMethods.constructDatasetFromJSONArray(merchantDetails);
					dataset.setId("merchantdetails");
					response.addDataset(dataset);
				}
			}catch (Exception e) {
				logger.error("Exception caught while createMerchantDetails:" +e.toString());
				
			}
			return response;
	}
	
	@Override
	public Result createMerchantDetails(ArrayList<MerchantDTO> merchants, DataControllerRequest request, Map<String, Object> inputs)
			throws ApplicationException {
		Result response = new Result();
		MerchantDTO dto;
		String[] arr = new String[merchants.size()];
		String tableName=HBLURLConstants.TAB_MERCHANT_DETAILS;
		String colSpec="(code,categoryof,logoUrl,paymentaggregator,isActive,labelText,fieldSetup,disclimerMessage,outageMessage,subcategory,subcategoryCode,categoryCode,placeholderText,maxTransactionLimit)";
		if(merchants.size()>0) {
		for(int i=0;i<merchants.size();i++) {
			dto=merchants.get(i);
		
		 Map<String, Object> inputParams = new HashMap<>();
	        inputParams.put("code", dto.getCode());
	        inputParams.put("categoryof", dto.getCategoryof());
		    inputParams.put("logoUrl", dto.getLogoUrl());
			inputParams.put("paymentaggregator", dto.getPaymentaggregator());
	        inputParams.put("isActive", dto.getIsActive());
	        inputParams.put("labelText", dto.getLabelText());
	        inputParams.put("fieldSetup", dto.getFieldSetup());
	        inputParams.put("disclimerMessage", dto.getDisclimerMessage());
	        inputParams.put("outageMessage", dto.getOutageMessage());
	        inputParams.put("subcategory", dto.getSubcategory());
	        inputParams.put("subcategoryCode", dto.getSubcategoryCode());
	        inputParams.put("categoryCode", dto.getCategoryCode());
	        inputParams.put("placeholderText", dto.getPlaceholderText());
	        inputParams.put("maxTransactionLimit", dto.getMaxTransactionLimit());
			logger.debug("BCT::MerchantDetailsBackendDeligateImpl:createMerchantDetails: inputParams:"+
					inputParams.toString());
			//response.appendResult(makeServiceCall(inputs, request));
			String qryValues="('"+dto.getCode()+"','"+dto.getCategoryof()+"','"+dto.getLogoUrl()+"'"; 
			if(StringUtils.isNotBlank(dto.getPaymentaggregator()))
				qryValues=qryValues+",'"+dto.getPaymentaggregator()+"'";
			if(StringUtils.isBlank(dto.getPaymentaggregator())) 
				qryValues=qryValues+",''";
			
				qryValues=qryValues+","+dto.getIsActive();
				
				if(StringUtils.isNotBlank(dto.getLabelText()))
					qryValues=qryValues+",'"+dto.getLabelText()+"'";
				if(StringUtils.isBlank(dto.getLabelText())) 
					qryValues=qryValues+",''";
				
				if(StringUtils.isNotBlank(dto.getFieldSetup()))
					qryValues=qryValues+",'"+dto.getFieldSetup()+"'";
				if(StringUtils.isBlank(dto.getFieldSetup())) 
					qryValues=qryValues+",''";
				if(StringUtils.isNotBlank(dto.getDisclimerMessage()))
					qryValues=qryValues+",'"+dto.getDisclimerMessage()+"'";
				if(StringUtils.isBlank(dto.getDisclimerMessage())) 
					qryValues=qryValues+",''";
				if(StringUtils.isNotBlank(dto.getOutageMessage()))
					qryValues=qryValues+",'"+dto.getOutageMessage()+"'";
				if(StringUtils.isBlank(dto.getOutageMessage())) 
					qryValues=qryValues+",''";
				if(StringUtils.isNotBlank(dto.getSubcategory()))
					qryValues=qryValues+",'"+dto.getSubcategory()+"'";
				if(StringUtils.isBlank(dto.getSubcategory())) 
					qryValues=qryValues+",''";
				
				if(StringUtils.isNotBlank(dto.getSubcategoryCode()))
					qryValues=qryValues+",'"+dto.getSubcategoryCode()+"'";
				if(StringUtils.isBlank(dto.getSubcategoryCode())) 
					qryValues=qryValues+",''";
				if(StringUtils.isNotBlank(dto.getCategoryCode()))
					qryValues=qryValues+",'"+dto.getCategoryCode()+"'";
				if(StringUtils.isBlank(dto.getCategoryCode())) 
					qryValues=qryValues+",''";
				
				if(StringUtils.isNotBlank(dto.getPlaceholderText()))
					qryValues=qryValues+",'"+dto.getPlaceholderText()+"'";
				if(StringUtils.isBlank(dto.getPlaceholderText())) 
					qryValues=qryValues+",''";
				
				if(StringUtils.isNotBlank(dto.getMaxTransactionLimit()))
					qryValues=qryValues+",'"+dto.getMaxTransactionLimit()+"'";
				if(StringUtils.isBlank(dto.getMaxTransactionLimit())) 
					qryValues=qryValues+",''";
				
			
				
				 qryValues=qryValues+");";
				 
			arr[i]=qryValues;
		}
		
		response.appendResult(HBLCommonUtility.batchInsert(tableName, colSpec, arr, request));
		}
			return response;
	}
	
	//@Override
	public Result createMerchantDetails1(ArrayList<MerchantDTO> merchants, DataControllerRequest request, Map<String, Object> inputs)
			throws ApplicationException {
		logger.debug("BCT::MerchantDetailsBackendDeligateImpl:createMerchantDetails: dto:"+
				merchants.toString());
		Result response = new Result();
		MerchantDTO dto;
		String tableName=HBLURLConstants.TAB_MERCHANT_DETAILS;
		String colSpec="(code,categoryof,logoUrl,paymentaggregator,isActive,maxTransactionAmount,labelText,fieldSetup,disclimerMessage,outageMessage,subcategory,subcategoryCode,categoryCode,placeholderText,maxTransactionLimit)";
		String[] arr=prepareMerchantDetailsrequest(merchants);
		response.appendResult(HBLCommonUtility.batchInsert(tableName, colSpec, arr, request));
		
			return response;
	}
	public String[] prepareMerchantDetailsrequest(ArrayList<MerchantDTO> merchants) {
		MerchantDTO dto;
		String[] arr = new String[merchants.size()];
		if(merchants.size()>0) {
		for(int i=0;i<merchants.size();i++) {
			dto=merchants.get(i);
		
		 Map<String, Object> inputParams = new HashMap<>();
	        inputParams.put("code", dto.getCode());
	        inputParams.put("categoryof", dto.getCategoryof());
		    inputParams.put("logoUrl", dto.getLogoUrl());
			inputParams.put("paymentaggregator", dto.getPaymentaggregator());
	        inputParams.put("isActive", dto.getIsActive());
	        inputParams.put("labelText", dto.getLabelText());
	        inputParams.put("fieldSetup", dto.getFieldSetup());
	        inputParams.put("disclimerMessage", dto.getDisclimerMessage());
	        inputParams.put("outageMessage", dto.getOutageMessage());
	        inputParams.put("subcategory", dto.getSubcategory());
	        inputParams.put("categoryCode", dto.getCategoryCode());
	        inputParams.put("placeholderText", dto.getPlaceholderText());
	        inputParams.put("maxTransactionLimit", dto.getMaxTransactionLimit());
			logger.debug("BCT::MerchantDetailsBackendDeligateImpl:createMerchantDetails: inputParams:"+
					inputParams.toString());
			//response.appendResult(makeServiceCall(inputs, request));
			String qryValues="('"+dto.getCode()+"','"+dto.getCategoryof()+"','"+dto.getLogoUrl()+"'"; 
			if(StringUtils.isNotBlank(dto.getPaymentaggregator()))
				qryValues=qryValues+",'"+dto.getPaymentaggregator()+"'";
			if(StringUtils.isBlank(dto.getPaymentaggregator())) 
				qryValues=qryValues+",''";
			
				qryValues=qryValues+","+dto.getIsActive();
				
				if(StringUtils.isNotBlank(dto.getLabelText()))
					qryValues=qryValues+",'"+dto.getLabelText()+"'";
				if(StringUtils.isBlank(dto.getLabelText())) 
					qryValues=qryValues+",''";
				
				if(StringUtils.isNotBlank(dto.getFieldSetup()))
					qryValues=qryValues+",'"+dto.getFieldSetup()+"'";
				if(StringUtils.isBlank(dto.getFieldSetup())) 
					qryValues=qryValues+",''";
				if(StringUtils.isNotBlank(dto.getDisclimerMessage()))
					qryValues=qryValues+",'"+dto.getDisclimerMessage()+"'";
				if(StringUtils.isBlank(dto.getDisclimerMessage())) 
					qryValues=qryValues+",''";
				if(StringUtils.isNotBlank(dto.getOutageMessage()))
					qryValues=qryValues+",'"+dto.getOutageMessage()+"'";
				if(StringUtils.isBlank(dto.getOutageMessage())) 
					qryValues=qryValues+",''";
				if(StringUtils.isNotBlank(dto.getSubcategory()))
					qryValues=qryValues+",'"+dto.getSubcategory()+"'";
				if(StringUtils.isBlank(dto.getSubcategory())) 
					qryValues=qryValues+",''";
				if(StringUtils.isNotBlank(dto.getCategoryCode()))
					qryValues=qryValues+",'"+dto.getCategoryCode()+"'";
				if(StringUtils.isBlank(dto.getCategoryCode())) 
					qryValues=qryValues+",''";
				if(StringUtils.isNotBlank(dto.getPlaceholderText()))
					qryValues=qryValues+",'"+dto.getPlaceholderText()+"'";
				if(StringUtils.isBlank(dto.getPlaceholderText())) 
					qryValues=qryValues+",''";
				if(StringUtils.isNotBlank(dto.getMaxTransactionLimit()))
					qryValues=qryValues+",'"+dto.getMaxTransactionLimit()+"'";
				if(StringUtils.isBlank(dto.getMaxTransactionLimit())) 
					qryValues=qryValues+",''";
				
				 qryValues=qryValues+");";
				 
			arr[i]=qryValues;
		}
		}
		return arr;
		
	}
	public Result makeServiceCall(Map<String, Object> inputParams, DataControllerRequest request ) {
		Result response = new Result();
		try {
			String dbResponse = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.CREATE_MERCHANT_DETAILS_OPERATION)
					.withRequestParameters(inputParams).withServiceId(HBLURLConstants.C360_SERVICEID)
					.withRequestHeaders(request.getHeaderMap()).build().getResponse();
			logger.debug("BCT::MerchantDetailsBackendDeligateImpl:createMerchantDetails: response:"+
					dbResponse);
				JSONObject responseJSON = new JSONObject(dbResponse);
				if(responseJSON.has("errmsg")) {
					response.setParam(new Param("httpStatusCode", responseJSON.get("httpStatusCode").toString()));
					response.addParam(new Param("dbpErrMsg", responseJSON.get("errmsg").toString()));
					response.addParam(new Param("dbpErrCode", responseJSON.get("opstatus").toString()));
					response.setParam(new Param("success", "false"));
				}
				else if(responseJSON.has("merchantdetails")&& responseJSON.getJSONArray("merchantdetails").length()>0) {
				JSONArray merchantDetails = responseJSON.getJSONArray("merchantdetails");
					Dataset dataset = HelperMethods.constructDatasetFromJSONArray(merchantDetails);
					dataset.setId("merchantdetails");
					response.addDataset(dataset);
				}
			}catch (Exception e) {
				logger.error("Exception caught while createMerchantDetails:" +e.toString());
				
			}
			return response;
		
	}
	
	@Override
	public JSONArray getMerchantDetails(String code,
			Map<String, Object> headerMap)throws ApplicationException {
		// TODO Auto-generated method stub
		Map<String, Object> inputmap = new HashMap<>();
		String filter = "code eq '" + code + "'";
		inputmap.put(HBLURLConstants.FILTER, filter);
		logger.debug("BCT::MerchantCategoriesBackendDeligateImpl: getMerchantDetails inputmap:"+
				inputmap.toString());
		JSONArray types = new JSONArray();
		try {
			String response = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.GET_MERCHANT_DETAILS_OPERATION)
					.withRequestParameters(inputmap).withServiceId(HBLURLConstants.HBL_OLB_CRUD_OPERATION_SERVICE)
					.withRequestHeaders(headerMap).build().getResponse();
			logger.debug("BCT::MerchantCategoriesBackendDeligateImpl: getMerchantDetails response:"+response);
			JSONObject responseJSON = new JSONObject(response);
			types = responseJSON.getJSONArray("merchantdetails");
		}catch (Exception e) {
			logger.error("Exception caught while fetching merchant details:" +e.toString());
			
		}
		return types;
	}
	@Override
	public JSONArray getAllMerchants(Map<String, Object> headerMap)throws ApplicationException {
		// TODO Auto-generated method stub
		Map<String, Object> inputmap = new HashMap<>();
		JSONArray types = new JSONArray();
		try {
			String response = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.GET_MERCHANT_DETAILS_OPERATION)
					.withRequestParameters(inputmap).withServiceId(HBLURLConstants.HBL_OLB_CRUD_OPERATION_SERVICE)
					.withRequestHeaders(headerMap).build().getResponse();
			logger.debug("BCT::MerchantCategoriesBackendDeligateImpl: getAllMerchants response:"+response);
			JSONObject responseJSON = new JSONObject(response);
			types = responseJSON.getJSONArray("merchantdetails");
		}catch (Exception e) {
			logger.error("Exception caught while fetching merchant details:" +e.toString());
			
		}
		return types;
	}
	
	public int updateMerchantCategories(MerchantDTO dto, DataControllerRequest request, Map<String, Object> inputs){
		MerchantCategoriesBackendDeligate categoryBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(MerchantCategoriesBackendDeligate.class);
				 logger.debug("BCT::MerchantDetailsBackendDeligateImpl: updateMerchantCategories payload:"+dto.toString());
				 try {
				 String categoryId= getMerchantCategoryId(dto.getCode(), request, inputs);
				 logger.debug("BCT::MerchantDetailsBackendDeligateImpl: updateMerchantCategories categoryId:"+categoryId);
				 if(StringUtils.isNotBlank(categoryId)) {
					 Integer id= Integer.parseInt(categoryId);
					 logger.debug("BCT::MerchantDetailsBackendDeligateImpl: updateMerchantCategories id:"+id);
					 MerchantCategoriesDTO categoryDto= new MerchantCategoriesDTO();
				 		if(StringUtils.isNotBlank(dto.getCode()))
						categoryDto.setCode(dto.getCode());
				 		if(StringUtils.isNotBlank(dto.getLogoUrl()))
				 		categoryDto.setLogoUrl(dto.getLogoUrl());
				 		if(StringUtils.isNotBlank(dto.getSubcategoryCode()))
				 		categoryDto.setSubcategoryof(dto.getSubcategoryCode());
				 		if(dto.getIsActive()!=null)
				 		categoryDto.setIsActive(dto.getIsActive());
				 		categoryDto.setCategory("APP");
				 		if(StringUtils.isNotBlank(dto.getLabelText()))
				 		categoryDto.setLabelText(dto.getLabelText());
				 		categoryDto.setId(id);
				 		
					categoryBackendDelegate.updateMerchantCategories(categoryDto, request, getOtherData(request));
				 }else {
					 logger.debug("BCT::MerchantDetailsBackendDeligateImpl: failed to update  Merchant categories:");
				 }
				} catch (ApplicationException e) {
					logger.error("BCT::MerchantDetailsBackendDeligateImpl: Exception caught while updating merchant categories:" +e.toString());
				} catch (Exception e) {
					logger.error("BCT::MerchantDetailsBackendDeligateImpl: Exception caught while updating merchant categories:" +e.toString());
				}
				 
		return 0;
		
	}
	
	public String getMerchantCategoryId(String code, DataControllerRequest dcRequest, Map<String, Object> inputs) {
		
		Map<String, Object> inputmap = new HashMap<>();
		JSONArray types = new JSONArray();
		Boolean isCodeAvailable=null;
		String key="";
		String categoryId ="";
		try {
			String filter = "code eq '" + code + "'";
			inputmap.put(HBLURLConstants.FILTER, filter);
			String tableName=HBLURLConstants.GET_ALL_MERCHANT_CATEGORIES_OPERATION;
			key="merchantcategory";
			logger.debug("BCT::MerchantDetailsBackendDeligateImpl: getMerchantCategoryId payload:"+inputmap);
			String response = DBPServiceExecutorBuilder.builder()
					.withOperationId(tableName)
					.withRequestParameters(inputmap).withServiceId(HBLURLConstants.HBL_OLB_CRUD_OPERATION_SERVICE)
					.withRequestHeaders(dcRequest.getHeaderMap()).build().getResponse();
			logger.debug("BCT::MerchantDetailsBackendDeligateImpl: getCategoryId response:"+response);
			JSONObject responseJSON = new JSONObject(response);
			types = responseJSON.getJSONArray(key);
			if(types.length()>0) {
				for(int i=0;i<types.length();i++) {
				logger.debug("BCT::MerchantDetailsBackendDeligateImpl: available CategoryId:"+types.getJSONObject(i));
				 categoryId=types.getJSONObject(i).getString("id");
				}
			}
		}catch (Exception e) {
			logger.error("Exception caught while fetching merchant category:" +e.toString());
			
		}
		return categoryId;
		
	}
	
	public Map<String, Object> getOtherData( DataControllerRequest dcRequest)
			throws ApplicationException {
		Map<String, Object> inputParams = new HashMap<String, Object>();
		  String loggedInUser = null; 
		  try { 
			  Map<String, String> userProfile =HelperMethods.getCustomerFromIdentityService(dcRequest); 
		  loggedInUser =userProfile.get("UserName"); 
		  } catch (Exception e) {
		  logger.error("BCT::MerchantCategoriesCRUDResourceImpl::validateInputs: exception:" +e.toString()); 
		  } 
		  inputParams.put("modifiedby", loggedInUser);
		  inputParams.put("currenttime", HelperMethods.getCurrentTimeStamp());
		 
		return inputParams;
	}
	

	

}
