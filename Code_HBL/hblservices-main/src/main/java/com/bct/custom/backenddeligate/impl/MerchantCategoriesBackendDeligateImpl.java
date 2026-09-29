package com.bct.custom.backenddeligate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bct.custom.backenddeligate.api.MerchantCategoriesBackendDeligate;
import com.bct.custom.constants.HBLURLConstants;
import com.bct.custom.dto.CIPSMerchantCategoriesDTO;
import com.bct.custom.dto.MerchantCategoriesDTO;
import com.bct.custom.dto.MerchantDTO;
import com.bct.custom.resource.impl.MerchantCategoriesCRUDResourceImpl;
import com.bct.utilities.HBLCommonUtility;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public class MerchantCategoriesBackendDeligateImpl implements MerchantCategoriesBackendDeligate{
	private static final Logger logger = LogManager.getLogger(MerchantCategoriesBackendDeligateImpl.class);

	@Override
	public Result updateMerchantCategories(MerchantCategoriesDTO dto, DataControllerRequest request,
			Map<String, Object> inputs) throws ApplicationException {
		logger.debug("BCT::MerchantCategoriesBackendDeligateImpl:updateMerchantCategories: inputs:"+
				inputs);
		Result response = new Result();
		 Map<String, Object> inputParams = new HashMap<>();
		 	inputParams.put("id", dto.getId());
		 	if(dto.getCategory()!=null)
	        inputParams.put("category", dto.getCategory());
		 	if(dto.getLogoUrl()!=null)
		    inputParams.put("logoUrl", dto.getLogoUrl());
		 	if(dto.getSubcategoryof()!=null)
		        inputParams.put("subcategoryof", dto.getSubcategoryof());
			if(dto.getIsActive()!=null)
	        inputParams.put("isActive", dto.getIsActive());
			if(dto.getLabelText()!=null)
		        inputParams.put("labelText", dto.getLabelText());
			if(dto.getCode()!=null)
		        inputParams.put("code", dto.getCode());
			if(dto.getSequence()!=null)
		        inputParams.put("sequence", dto.getSequence());
			if(dto.getPaymentaggregator()!=null)
			inputParams.put("paymentaggregator", dto.getPaymentaggregator());
			if(inputs.get("modifiedby")!=null)
		     inputParams.put("modifiedby", inputs.get("modifiedby"));
			if(inputs.get("currenttime")!=null) {
			 inputParams.put("lastmodifiedts", inputs.get("currenttime"));
			inputParams.put("synctimestamp", inputs.get("currenttime"));
			}
			if(dto.getMerchantType()!=null)
		        inputParams.put("merchantType", dto.getMerchantType());
			logger.debug("BCT::MerchantCategoriesBackendDeligateImpl:updateMerchantCategories: dto:"+
					inputParams.toString());
			try {
			String dbResponse = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.UPDATE_MERCHANT_CATEGORIES_OPERATION)
					.withRequestParameters(inputParams).withServiceId(HBLURLConstants.C360_SERVICEID)
					.withRequestHeaders(request.getHeaderMap()).build().getResponse();
			logger.debug("BCT::MerchantCategoriesBackendDeligateImpl:updateMerchantCategories: response:"+
					dbResponse);
				JSONObject responseJSON = new JSONObject(dbResponse);
				if(responseJSON.has("errmsg")) {
					response.setParam(new Param("httpStatusCode", responseJSON.get("httpStatusCode").toString()));
					response.addParam(new Param("dbpErrMsg", responseJSON.get("errmsg").toString()));
					response.addParam(new Param("dbpErrCode", responseJSON.get("opstatus").toString()));
					response.setParam(new Param("success", "false"));
				}
				else if(responseJSON.has("merchantcategory")&& responseJSON.getJSONArray("merchantcategory").length()>0) {
				JSONArray merchantDetails = responseJSON.getJSONArray("merchantcategory");
					Dataset dataset = HelperMethods.constructDatasetFromJSONArray(merchantDetails);
					dataset.setId("merchantcategory");
					response.addDataset(dataset);
				}
			}catch (Exception e) {
				logger.error("Exception caught while updateMerchantCategories:" +e.toString());
				
			}
			return response;
	}

	@Override
	public Result createMerchantCategories(MerchantCategoriesDTO dto, DataControllerRequest request,
			Map<String, Object> inputs) throws ApplicationException {
		logger.debug("BCT::MerchantCategoriesBackendDeligateImpl:createMerchantCategories: inputs:"+
				inputs.toString());
		Result response = new Result();
		 Map<String, Object> inputParams = new HashMap<>();
		 	inputParams.put("category", dto.getCategory());
		    inputParams.put("logoUrl", dto.getLogoUrl());
		    inputParams.put("subcategoryof", dto.getSubcategoryof());
	        inputParams.put("isActive", dto.getIsActive());
	        inputParams.put("labelText", dto.getLabelText());
		    inputParams.put("code", dto.getCode());
		    inputParams.put("sequence", dto.getSequence());
		    inputParams.put("createdby", inputs.get("modifiedby"));
	        inputParams.put("modifiedby", inputs.get("modifiedby"));
	        inputParams.put("merchantType", dto.getMerchantType());
	        inputParams.put("paymentaggregator", dto.getPaymentaggregator());
			if(inputs.get("currenttime")!=null) {
			 inputParams.put("createdts", inputs.get("currenttime"));
			 inputParams.put("lastmodifiedts", inputs.get("currenttime"));
			inputParams.put("synctimestamp", inputs.get("currenttime"));
			}
			logger.debug("BCT::MerchantCategoriesBackendDeligateImpl:createMerchantCategories: dto:"+
					inputParams.toString());
			try {
			String dbResponse = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.CREATE_MERCHANT_CATEGORIES_OPERATION)
					.withRequestParameters(inputParams).withServiceId(HBLURLConstants.C360_SERVICEID)
					.withRequestHeaders(request.getHeaderMap()).build().getResponse();
			logger.debug("BCT::MerchantCategoriesBackendDeligateImpl:createMerchantCategories: response:"+
					dbResponse);
				JSONObject responseJSON = new JSONObject(dbResponse);
				if(responseJSON.has("errmsg")) {
					response.setParam(new Param("httpStatusCode", responseJSON.get("httpStatusCode").toString()));
					response.addParam(new Param("dbpErrMsg", responseJSON.get("errmsg").toString()));
					response.addParam(new Param("dbpErrCode", responseJSON.get("opstatus").toString()));
					response.setParam(new Param("success", "false"));
				}
				else if(responseJSON.has("merchantcategory")&& responseJSON.getJSONArray("merchantcategory").length()>0) {
				JSONArray merchantDetails = responseJSON.getJSONArray("merchantcategory");
					Dataset dataset = HelperMethods.constructDatasetFromJSONArray(merchantDetails);
					dataset.setId("merchantcategory");
					response.addDataset(dataset);
				}
			}catch (Exception e) {
				logger.error("Exception caught while createMerchantCategories:" +e.toString());
				
			}
			return response;
	}
	
	@Override
	public Result createCIPSMerchantCategories(CIPSMerchantCategoriesDTO dto, DataControllerRequest request,
			Map<String, Object> inputs) throws ApplicationException {
		logger.debug("BCT::MerchantCategoriesBackendDeligateImpl:createMerchantCategories: inputs:"+
				inputs.toString());
		Result response = new Result();
		 Map<String, Object> inputParams = new HashMap<>();
	        inputParams.put("category", dto.getCategory());
		    inputParams.put("logoUrl", dto.getLogoUrl());
		    inputParams.put("subcategoryof", dto.getSubcategoryof());
	        inputParams.put("isActive", dto.getIsActive());
	        inputParams.put("labelText", dto.getLabelText());
		    inputParams.put("code", dto.getCode());
		    inputParams.put("sequence", dto.getSequence());
		    inputParams.put("createdby", inputs.get("modifiedby"));
	        inputParams.put("modifiedby", inputs.get("modifiedby"));
	        inputParams.put("merchantType", dto.getMerchantType());
			if(inputs.get("currenttime")!=null) {
			 inputParams.put("createdts", inputs.get("currenttime"));
			 inputParams.put("lastmodifiedts", inputs.get("currenttime"));
			inputParams.put("synctimestamp", inputs.get("currenttime"));
			}
			logger.debug("BCT::MerchantCategoriesBackendDeligateImpl:createMerchantCategories: dto:"+
					inputParams.toString());
			try {
			String dbResponse = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.CREATE_MERCHANT_CATEGORIES_OPERATION)
					.withRequestParameters(inputParams).withServiceId(HBLURLConstants.C360_SERVICEID)
					.withRequestHeaders(request.getHeaderMap()).build().getResponse();
			logger.debug("BCT::MerchantCategoriesBackendDeligateImpl:createMerchantCategories: response:"+
					dbResponse);
				JSONObject responseJSON = new JSONObject(dbResponse);
				if(responseJSON.has("errmsg")) {
					response.setParam(new Param("httpStatusCode", responseJSON.get("httpStatusCode").toString()));
					response.addParam(new Param("dbpErrMsg", responseJSON.get("errmsg").toString()));
					response.addParam(new Param("dbpErrCode", responseJSON.get("opstatus").toString()));
					response.setParam(new Param("success", "false"));
				}
				else if(responseJSON.has("merchantcategory")&& responseJSON.getJSONArray("merchantcategory").length()>0) {
				JSONArray merchantDetails = responseJSON.getJSONArray("merchantcategory");
					Dataset dataset = HelperMethods.constructDatasetFromJSONArray(merchantDetails);
					dataset.setId("merchantcategory");
					response.addDataset(dataset);
				}
			}catch (Exception e) {
				logger.error("Exception caught while createMerchantCategories:" +e.toString());
				
			}
			return response;
	}

	@Override
	public Result createMerchantCategories(ArrayList<MerchantCategoriesDTO> categoriesArray,
			DataControllerRequest dcRequest, Map<String, Object> otherInput) throws ApplicationException {
			logger.debug("BCT::MerchantCategoriesBackendDeligateImpl:createMerchantCategories: dto:"+
					categoriesArray.toString());
			Result response = new Result();
			MerchantCategoriesDTO dto;
			String nullObj;
			String[] arr = new String[categoriesArray.size()];
			String tableName=HBLURLConstants.TAB_MERCHANT_CATEGORIES;
			String colSpec="(category,logoUrl,subcategoryof,isActive,createdby,labelText,code,sequence,merchantType,paymentaggregator)";
			if(categoriesArray.size()>0) {
			for(int i=0;i<categoriesArray.size();i++) {
				dto=categoriesArray.get(i);
			
			 Map<String, Object> inputParams = new HashMap<>();
			 inputParams.put("category", dto.getCategory());
			    inputParams.put("logoUrl", dto.getLogoUrl());
			    inputParams.put("subcategoryof", dto.getSubcategoryof());
		        inputParams.put("isActive", dto.getIsActive());
			    inputParams.put("createdby", otherInput.get("modifiedby"));
				inputParams.put("labelText", dto.getLabelText());
			    inputParams.put("code", dto.getCode());
			    inputParams.put("sequence", dto.getSequence());
			    inputParams.put("merchantType", dto.getMerchantType());
				logger.debug("BCT::MerchantCategoriesBackendDeligateImpl:createMerchantCategories: inputParams:"+
						inputParams.toString());
				//response.appendResult(makeServiceCall(inputs, request));
				String qryValues="('"+dto.getCategory()+"','"+dto.getLogoUrl()+"','"+dto.getSubcategoryof()+"',"+dto.getIsActive()+" ";
				if(inputParams.get("createdby")!=null)
					qryValues=qryValues+",'"+inputParams.get("createdby").toString()+"'";
				if(inputParams.get("createdby")==null)
					qryValues=qryValues+",''";
					if(dto.getLabelText()!=null)
						qryValues=qryValues+",'"+dto.getLabelText()+"'";
					if(dto.getLabelText()==null) 
						qryValues=qryValues+",''";
					if(StringUtils.isNotBlank(dto.getCode()))
						qryValues=qryValues+",'"+dto.getCode()+"'";
					 if(StringUtils.isBlank(dto.getCode())) 
						qryValues=qryValues+",''";
					 
					    qryValues=qryValues+","+dto.getSequence();
					    
					if(StringUtils.isNotBlank(dto.getMerchantType()))
							qryValues=qryValues+",'"+dto.getMerchantType()+"'";
					if(StringUtils.isBlank(dto.getMerchantType())) 
							qryValues=qryValues+",''";
					if(StringUtils.isNotBlank(dto.getPaymentaggregator()))
						qryValues=qryValues+",'"+dto.getPaymentaggregator()+"'";
					if(StringUtils.isBlank(dto.getPaymentaggregator())) 
						qryValues=qryValues+",''";
					
					 qryValues=qryValues+");";
				
					//dto.getPaymentaggregator()!=null ?qryValues+",'"+dto.getPaymentaggregator()+"' "+",''" ;'"+dto.getIsActive()+"','"+dto.getCharges()+"',
				arr[i]=qryValues;
			}
			
			response.appendResult(HBLCommonUtility.batchInsert(tableName, colSpec, arr, dcRequest));
			}
				return response;
		}

	@Override
	public Boolean checkCodeAvailableInDatabase(String code, String tableName, DataControllerRequest dcRequest) {
		Map<String, Object> inputmap = new HashMap<>();
		JSONArray types = new JSONArray();
		Boolean isCodeAvailable=null;
		String key="";
		try {
			String filter = "code eq '" + code + "'";
			inputmap.put(HBLURLConstants.FILTER, filter);
			if(tableName.equalsIgnoreCase("merchantCategory")) {
				tableName=HBLURLConstants.GET_ALL_MERCHANT_CATEGORIES_OPERATION;
				key="merchantcategory";
			}
			logger.debug("BCT::MerchantCategoriesBackendDeligateImpl: checkCodeAvailableInDatabase payload:"+inputmap);
			String response = DBPServiceExecutorBuilder.builder()
					.withOperationId(tableName)
					.withRequestParameters(inputmap).withServiceId(HBLURLConstants.HBL_OLB_CRUD_OPERATION_SERVICE)
					.withRequestHeaders(dcRequest.getHeaderMap()).build().getResponse();
			logger.debug("BCT::MerchantCategoriesBackendDeligateImpl: checkCodeAvailableInDatabase response:"+response);
			JSONObject responseJSON = new JSONObject(response);
			types = responseJSON.getJSONArray(key);
			if(types.length()>0) {
				isCodeAvailable=true;
			}else {
				isCodeAvailable=false;
			}
		}catch (Exception e) {
			logger.error("Exception caught while fetching merchant category:" +e.toString());
			
		}
		return isCodeAvailable;
	}

}
