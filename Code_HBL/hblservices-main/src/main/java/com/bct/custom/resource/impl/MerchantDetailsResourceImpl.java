package com.bct.custom.resource.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bct.custom.businessdeligate.api.GetAllMerchantOperationsBusinessDeligate;
import com.bct.custom.businessdeligate.api.MerchantChargesBusinessDelegate;
import com.bct.custom.businessdeligate.api.MerchantDetailsBusinessDeligate;
import com.bct.custom.businessdeligate.api.MerchantFieldsBusinessDeligate;
import com.bct.custom.constants.HBLConstants;
import com.bct.custom.dto.MerchantDTO;
import com.bct.custom.dto.MerchantFieldsDTO;
import com.bct.custom.dto.MerchantPaymentCharges;
import com.bct.custom.resource.api.MerchantCategoriesCRUDResource;
import com.bct.custom.resource.api.MerchantChargesResource;
import com.bct.custom.resource.api.MerchantDetailsResource;
import com.bct.custom.resource.api.MerchantFieldsResource;
import com.bct.utilities.HBLCommonUtility;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.constants.DBPConstants;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonArray;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class MerchantDetailsResourceImpl implements MerchantDetailsResource {
	private static final Logger logger = LogManager.getLogger(MerchantDetailsResourceImpl.class);
	ArrayList<MerchantFieldsDTO> requestFieldsDtoList= null;
	ArrayList<MerchantFieldsDTO> responseFieldsDtoList= null;
	ArrayList<MerchantFieldsDTO> merchantFormFieldsDtoList=null;
	ArrayList<MerchantPaymentCharges> chargesDtoList= null;
	ArrayList<MerchantPaymentCharges> editChargesDtoList= new ArrayList<MerchantPaymentCharges>();
	ArrayList<MerchantFieldsDTO> editRequestFieldsDtoList= new ArrayList<MerchantFieldsDTO>();
	ArrayList<MerchantFieldsDTO> editResponseFieldsDtoList= new ArrayList<MerchantFieldsDTO>();
	MerchantFieldsResource merchantFieldsResource = DBPAPIAbstractFactoryImpl.getResource(MerchantFieldsResource.class);
	MerchantChargesResource chargesResource = DBPAPIAbstractFactoryImpl.getResource(MerchantChargesResource.class);
	MerchantDetailsBusinessDeligate businessDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(MerchantDetailsBusinessDeligate.class);
	MerchantChargesBusinessDelegate chargesBusinessDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(MerchantChargesBusinessDelegate.class);
	MerchantFieldsBusinessDeligate fieldsBusinessDelegate = DBPAPIAbstractFactoryImpl
	.getBusinessDelegate(MerchantFieldsBusinessDeligate.class);
	
	
	@Override
	public Result merchantsCRUDOperation(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		Result result = new Result();
		MerchantDTO dto;
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		
		JSONArray requestArray =null;
		ArrayList<MerchantDTO> multipleMerchants= null;
		
		if (methodID.equalsIgnoreCase("updateMerchantDetails")) {
			try {
				JSONObject merchantDetailsRequest = validateEditMerchantRequest(dcRequest, inputParams);
				logger.debug("BCT::MerchantDetailsResourceImpl::validateEditMerchantRequest: merchantDetails:" + merchantDetailsRequest.toString());
				if(merchantDetailsRequest.has("merchantDetails")) {
					 dto = JSONUtils.parse(merchantDetailsRequest.getJSONObject("merchantDetails").toString(), MerchantDTO.class);
					result = businessDelegate.updateMerchantDetails(dto, dcRequest, null);
					logger.debug("BCT::MerchantDetailsResourceImpl::updateMerchantDetails: response:" + result.toString());
				}
				if(result.getParamByName("dbpErrCode") == null && merchantDetailsRequest.has("limitsAndCharges") ) {
					result =chargesBusinessDelegate.updateMerchantCharges(editChargesDtoList, dcRequest, inputParams);
					logger.debug("BCT::MerchantDetailsResourceImpl::updateMerchantCharges: response:" + result.toString());
				}
				/*
				if(result.getParamByName("dbpErrCode") == null && merchantDetailsRequest.has(HBLConstants.REQUEST_MERCHANT_FIELDS) && merchantDetailsRequest.has(HBLConstants.RESPONSE_MERCHANT_FIELDS)) {
					 merchantFormFieldsDtoList=mergeRequestResponseFormFields(editRequestFieldsDtoList, editResponseFieldsDtoList);
					result =fieldsBusinessDelegate.updateMerchantFileds(merchantFormFieldsDtoList, dcRequest, inputParams);
					logger.debug("BCT::MerchantDetailsResourceImpl::updateMerchantFileds: response:" + result.toString());
				}*/
				if(result.getParamByName("dbpErrCode") == null && merchantDetailsRequest.has(HBLConstants.REQUEST_MERCHANT_FIELDS)) {
					result =fieldsBusinessDelegate.updateMerchantFileds(editRequestFieldsDtoList, dcRequest, inputParams);
					logger.debug("BCT::MerchantDetailsResourceImpl::updateMerchantFileds: response:" + result.toString());
				}
				if(result.getParamByName("dbpErrCode") == null && merchantDetailsRequest.has(HBLConstants.RESPONSE_MERCHANT_FIELDS)) {
					result =fieldsBusinessDelegate.updateMerchantFileds(editResponseFieldsDtoList, dcRequest, inputParams);
					logger.debug("BCT::MerchantDetailsResourceImpl::updateMerchantFileds: response:" + result.toString());
				}
				if (result.getParamByName("dbpErrCode") == null) {
					result = new Result();
					result.setParam(new Param("opstatus", "0"));
					result.setParam(new Param("httpStatusCode", "200"));
					result.setParam(new Param("success", "true"));
				} else {
					result.setParam(new Param("success", "false"));
				}
			} catch (ApplicationException e) {
				result.addParam(new Param("dbpErrCode", e.getErrorCodeEnum().getErrorCodeAsString()));
				result.addParam(new Param("dbpErrMsg", e.getErrorCodeEnum().getMessage()));
			} catch (Exception e) {
				result.addParam(new Param("dbpErrCode", e.toString()));
				result.addParam(new Param("dbpErrMsg", e.getMessage()));
			}
		} else if (methodID.equalsIgnoreCase("createMerchantDetails")) {
			try {
				Boolean checkIfAnyMerchantExist=false;
				chargesDtoList= new ArrayList<MerchantPaymentCharges>();
				requestFieldsDtoList= new ArrayList<MerchantFieldsDTO>();
				responseFieldsDtoList= new ArrayList<MerchantFieldsDTO>();
				String request = inputParams.get("merchantDetails").toString();
				
				logger.debug("BCT::MerchantDetailsResourceImpl::merchantsCRUDOperation: merchantsCRUDOperationRequest:"+ request);
				requestArray = new JSONArray(request);
				if(requestArray.length()>0) {
				 multipleMerchants = validateCreateMerchantRequest(requestArray, dcRequest);
				 checkIfAnyMerchantExist=checkIfMerchantsExist(multipleMerchants, dcRequest);
				 merchantFormFieldsDtoList=mergeRequestResponseFormFields(requestFieldsDtoList, responseFieldsDtoList);
				}
				if (multipleMerchants!=null && multipleMerchants.size()>0 && checkIfAnyMerchantExist==false) {
				result = insertIntoMerchantCategory(multipleMerchants,methodID, inputArray, dcRequest, dcResponse);
				logger.debug("BCT::MerchantDetailsResourceImpl::insertIntoMerchantCategory response:" + ResultToJSON.convert(result));
				if(result.getParamByName("dbpErrCode") == null) {
				result = businessDelegate.createMerchantDetails(multipleMerchants, dcRequest, inputParams);
				logger.debug("BCT::MerchantDetailsResourceImpl::createMerchantDetails: response:" + result.toString());
				}
				if (result.getParamByName("dbpErrCode") == null && chargesDtoList!=null &&chargesDtoList.size()>0) {
					result = chargesBusinessDelegate.createMerchantCharges(chargesDtoList, dcRequest, inputParams);
				}
				if (result.getParamByName("dbpErrCode") == null && merchantFormFieldsDtoList !=null && merchantFormFieldsDtoList.size()>0) {
					result = fieldsBusinessDelegate.createMerchantFileds(merchantFormFieldsDtoList, dcRequest, inputParams);
				}
				logger.debug("BCT::MerchantDetailsResourceImpl::All tables inserted successfully:" + ResultToJSON.convert(result));
				if (result.getParamByName("dbpErrCode") == null) {
				result.setParam(new Param("opstatus", "0"));
				result.setParam(new Param("httpStatusCode", "200"));
				result.setParam(new Param("success", "true"));
				}
				}
			} catch (ApplicationException e) {
				result.addParam(new Param("dbpErrCode", e.getErrorCodeEnum().toString()));
				result.addParam(new Param("dbpErrMsg", e.getMessage()));
			}catch (IOException e) {
				result.addParam(new Param("dbpErrCode", e.toString()));
				result.addParam(new Param("dbpErrMsg", e.getMessage()));
			} 
			catch (Exception e) {
				result.addParam(new Param("dbpErrCode", e.toString()));
				result.addParam(new Param("dbpErrMsg", e.getMessage()));
			}

		}
		if (methodID.equalsIgnoreCase("deleteMerchantCharges")) {
			try {
				String request1 = inputParams.get("merchantPaymentCharges").toString();
				JSONArray requestJsonArray = new JSONArray(request1);
				ArrayList<MerchantPaymentCharges> dtoList = chargesResource.validateDeleteMerchantCharges(requestJsonArray, dcRequest);
				if(dtoList.size()>0) {
				result = chargesBusinessDelegate.deleteMerchantCharges(dtoList, dcRequest, inputParams);
				logger.debug("BCT::MerchantDetailsResourceImpl::deleteMerchantCharges: response:" + result.toString());
				}
				if (result.getParamByName("dbpErrCode") == null) {
					result.setParam(new Param("opstatus", "0"));
					result.setParam(new Param("httpStatusCode", "200"));
					result.setParam(new Param("success", "true"));
				}
			} 
			catch (ApplicationException e) {
				result.addParam(new Param("dbpErrCode", e.getErrorCodeEnum().getErrorCodeAsString()));
				result.addParam(new Param("dbpErrMsg", e.getErrorCodeEnum().getMessage()));
			}
			catch (Exception e) {
				result.addParam(new Param("dbpErrCode", e.toString()));
				result.addParam(new Param("dbpErrMsg", e.getMessage()));
			}

		}
		else if (methodID.equalsIgnoreCase("deleteMerchantFields")) {
			try {
				String request1 = inputParams.get("merchantFormFields").toString();
				logger.debug("BCT::MerchantFieldsResourceImpl::deleteMerchantFields: request1:"+request1);
				requestArray = new JSONArray(request1);
				ArrayList<MerchantFieldsDTO> inputs = merchantFieldsResource.validateDeleteMerchantFields(requestArray, dcRequest);
				if(inputs!=null && inputs.size()>0) {
				result = fieldsBusinessDelegate.deleteMerchantFileds(inputs, dcRequest, inputParams);
				logger.debug("BCT::MerchantDetailsResourceImpl::deleteMerchantFields: response:" + result.toString());
			    }
				if (result.getParamByName("dbpErrCode") == null) {
					result.setParam(new Param("opstatus", "0"));
					result.setParam(new Param("httpStatusCode", "200"));
					result.setParam(new Param("success", "true"));
				}
			} catch (ApplicationException e) {
				result.addParam(new Param("dbpErrCode", e.getErrorCodeEnum().getErrorCodeAsString()));
				result.addParam(new Param("dbpErrMsg", e.getErrorCodeEnum().getMessage()));
			}
			catch (Exception e) {
				result.addParam(new Param("dbpErrCode", e.toString()));
				result.addParam(new Param("dbpErrMsg", e.getMessage()));
			}
		} 
		return result;
	}
		
	
	
	public Result editOrUpdateMerchantCharges(ArrayList<MerchantPaymentCharges> editChargesDtoList, DataControllerRequest dcRequest, Map<String, Object> inputParams) throws ApplicationException {
		Result result= new Result();
		if(editChargesDtoList!=null && editChargesDtoList.size()>0) {
			for(int i=0;i<editChargesDtoList.size();i++) {
				MerchantPaymentCharges dto = editChargesDtoList.get(i);
				if(dto.getId()==null) {
					result = chargesBusinessDelegate.createMerchantCharges(chargesDtoList, dcRequest, inputParams);
				}else if(dto.getId()!=null) {
					result =chargesBusinessDelegate.updateMerchantCharges(editChargesDtoList.get(i), dcRequest, inputParams);
				}
			}
		}
		
		
		return null;
		
	}
	public Map<String, Object> validateInputs(MerchantDTO dto, DataControllerRequest dcRequest)
			throws ApplicationException {
		if (dto.getId() == null) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10755);
		}
		Map<String, Object> inputParams = new HashMap<String, Object>();
		return inputParams;
	}
	
	public Map<String, Object> validateCreateMerchantInputs(MerchantDTO dto, DataControllerRequest dcRequest)
			throws ApplicationException {
		if (StringUtils.isBlank(dto.getCode()) || StringUtils.isBlank(dto.getCategoryof())
				|| StringUtils.isBlank(dto.getLogoUrl())
				|| StringUtils.isBlank(dto.getLabelText()) ) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10755);
		}
		Map<String, Object> inputParams = new HashMap<String, Object>();
		/*
		 * String loggedInUser = null; try { Map<String, String> userProfile =
		 * HelperMethods.getCustomerFromIdentityService(dcRequest); loggedInUser =
		 * userProfile.get("UserName"); } catch (Exception e) { logger.
		 * error("BCT::MerchantDetailsResourceImpl::validateCreateMerchantInputs: exception:"
		 * + e.toString()); } inputParams.put("modifiedby", loggedInUser);
		 * inputParams.put("currenttime", HelperMethods.getCurrentTimeStamp());
		 */
		return inputParams;
	}

	public static Dataset constructDatasetFromJSONArray(JSONArray JSONArray) {
		Dataset dataset = new Dataset();
		for (int count = 0; count < JSONArray.length(); count++) {
			Record record = constructRecordFromJSONObject((JSONObject) JSONArray.get(count));
			dataset.addRecord(record);
		}
		return dataset;
	}

	public static Record constructRecordFromJSONObject(JSONObject JSONObject) {
		Record response = new Record();
		if (JSONObject == null || JSONObject.length() == 0) {
			return response;
		}
		Iterator<String> keys = JSONObject.keys();

		while (keys.hasNext()) {
			String key = keys.next();
			if (JSONObject.get(key) instanceof String) {
				Param param = new Param(key, JSONObject.getString(key), DBPConstants.FABRIC_STRING_CONSTANT_KEY);
				response.addParam(param);

			} else if (JSONObject.get(key) instanceof Integer) {
				Param param = new Param(key, JSONObject.get(key).toString(), DBPConstants.FABRIC_INT_CONSTANT_KEY);
				response.addParam(param);

			} else if (JSONObject.get(key) instanceof Boolean) {
				Param param = new Param(key, JSONObject.get(key).toString(), DBPConstants.FABRIC_BOOLEAN_CONSTANT_KEY);
				response.addParam(param);

			} else if (JSONObject.get(key) instanceof JSONArray) {
				Dataset dataset = constructDatasetFromJSONArray(JSONObject.getJSONArray(key));
				dataset.setId(key);
				response.addDataset(dataset);
			}
		}

		return response;
	}
	public ArrayList<MerchantDTO> validateCreateMerchantRequest(JSONArray array, DataControllerRequest dcRequest)
			throws ApplicationException, IOException {
		MerchantDTO dto;
		ArrayList<MerchantDTO> merchantList = new ArrayList<MerchantDTO>();
		for(int i=0;i<array.length();i++) {
			JSONObject merchantObj=array.getJSONObject(i);
			JSONArray requestFieldsArray= merchantObj.has("requestFormFields") ?merchantObj.getJSONArray("requestFormFields"):new JSONArray();
			JSONArray limitsArray=merchantObj.has("limitsAndCharges")? merchantObj.getJSONArray("limitsAndCharges"):new JSONArray();
			JSONArray responseFieldsArray= merchantObj.has("responseFormFields") ?merchantObj.getJSONArray("responseFormFields"):new JSONArray();
			try {
				if(merchantObj.has("requestFormFields"))
					merchantObj.remove("requestFormFields");
				if(merchantObj.has("limitsAndCharges"))
					merchantObj.remove("limitsAndCharges");
				if(merchantObj.has("responseFormFields"))
					merchantObj.remove("responseFormFields");
				String merchantRequest=merchantObj.toString();
				logger.debug("BCT::MerchantDetailsResourceImpl::validateCreateMerchantRequest: before parse MerchantDTO request:" + merchantRequest);
				dto = JSONUtils.parse(merchantRequest, MerchantDTO.class);
			if(dto!=null && validateCreateMerchant(dto, limitsArray,requestFieldsArray, responseFieldsArray, dcRequest)) {
				merchantList.add(dto);
			}
			}catch (ApplicationException e) {
				logger.error("BCT::MerchantDetailsResourceImpl::ApplicationException occured at:validateCreateMerchantRequest:"+e.getLocalizedMessage());
				throw new ApplicationException(e.getErrorCodeEnum(), e.getMessage());
			}catch (Exception e) {
				logger.error("BCT::MerchantDetailsResourceImpl::Exception occured at:validateCreateMerchantRequest:"+e.getMessage());
				throw new ApplicationException(ErrorCodeEnum.ERR_10755, "Invalid merchant details");
			}
		}
		if(merchantList.size()==0) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10755, "Invalid merchant details");
		}
		
		return merchantList;
	}
	public boolean validateCreateMerchant(MerchantDTO dto, JSONArray chargesArray, JSONArray requestFieldsArray, JSONArray responseFieldsArray, DataControllerRequest dcRequest) throws ApplicationException, IOException {
		boolean isValid=true;
		if (StringUtils.isBlank(dto.getCode()) || StringUtils.isBlank(dto.getCategoryof())
				|| StringUtils.isBlank(dto.getLogoUrl())
				|| StringUtils.isBlank(dto.getLabelText()) ) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10755, "Invalid merchant details");
		}
		if(chargesArray.length()>0 ){
			chargesDtoList= chargesResource.validateCreateMerchantCharges(chargesArray, dcRequest);
		}
		if(requestFieldsArray.length()>0){
			requestFieldsDtoList=merchantFieldsResource.validateCreateMerchantFieldsRequest(requestFieldsArray, dcRequest);
		}
		if(responseFieldsArray.length()>0){
			responseFieldsDtoList=merchantFieldsResource.validateCreateMerchantFieldsRequest(responseFieldsArray, dcRequest);
		}
		return isValid;
	}
	public JSONObject validateEditMerchantRequest( DataControllerRequest dcRequest, Map<String, Object> inputParams)
			throws ApplicationException, IOException {
		JSONObject merchantDetails = new JSONObject();
		String request = inputParams.get("merchantDetails").toString();
		JSONArray requestArray = new JSONArray(request);
		JSONObject merchantObj = requestArray.getJSONObject(0);
		MerchantDTO dto = JSONUtils.parse(merchantObj.toString(), MerchantDTO.class);
		if (dto.getId() == null ) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10755);
		}
		merchantDetails.put("merchantDetails", merchantObj);
		if(merchantObj.has("limitsAndCharges")) {
			editChargesDtoList= chargesResource.validateEditMerchantCharges(merchantObj.getJSONArray("limitsAndCharges"), dcRequest);
			merchantDetails.put("limitsAndCharges", editChargesDtoList);
		}
		if(merchantObj.has(HBLConstants.REQUEST_MERCHANT_FIELDS)) {
		    editRequestFieldsDtoList=merchantFieldsResource.validateEditMerchantFields(merchantObj.getJSONArray(HBLConstants.REQUEST_MERCHANT_FIELDS), dcRequest);
		    merchantDetails.put(HBLConstants.REQUEST_MERCHANT_FIELDS, editRequestFieldsDtoList);	
		}
		if(merchantObj.has(HBLConstants.RESPONSE_MERCHANT_FIELDS)) {
		    editResponseFieldsDtoList=merchantFieldsResource.validateEditMerchantFields(merchantObj.getJSONArray(HBLConstants.RESPONSE_MERCHANT_FIELDS), dcRequest);
		    merchantDetails.put(HBLConstants.RESPONSE_MERCHANT_FIELDS, editResponseFieldsDtoList);
		}
		return merchantDetails;
	}

	@Override
	public Result getMerchantDetails(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String appCode = inputParams.get("appCode");
		Result result = new Result();
		if (StringUtils.isBlank(appCode)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10755);
		}
		try {
			MerchantDetailsBusinessDeligate businessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(MerchantDetailsBusinessDeligate.class);
			JSONArray merchantCategories = businessDelegate.getMerchantDetails(appCode,
					dcRequest.getHeaderMap());
			logger.debug("BCT::MerchantDetailsResourceImpl::getMerchantDetailsByCategory:"
					+ merchantCategories.toString());

			Dataset ds = new Dataset();
			ds = constructDatasetFromJSONArray(merchantCategories);
			ds.setId("merchants");
			result.addDataset(ds);
			logger.debug("Result categories:" + ds);
			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));
		} catch (Exception e) {
			// TODO: handle exception
			result.addParam(new Param("dbpErrCode", e.toString()));
			result.addParam(new Param("dbpErrMsg", "10002"));
		}
		return result;
	}
	public ArrayList<MerchantFieldsDTO> mergeRequestResponseFormFields(ArrayList<MerchantFieldsDTO> requestFieldsDtoList, ArrayList<MerchantFieldsDTO> responseFieldsDtoList ){
		ArrayList<MerchantFieldsDTO> merchantFormFields = new ArrayList<MerchantFieldsDTO>();
		for(int i=0;i<requestFieldsDtoList.size();i++) {
			merchantFormFields.add(requestFieldsDtoList.get(i));
		}
		for(int j=0;j<responseFieldsDtoList.size();j++) {
			merchantFormFields.add(responseFieldsDtoList.get(j));
		}
		return merchantFormFields;
	}
	public Result insertIntoMerchantCategory(ArrayList<MerchantDTO> multipleMerchants, String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException, IOException, Exception {
		MerchantCategoriesCRUDResource catImpl = DBPAPIAbstractFactoryImpl.getResource(MerchantCategoriesCRUDResource.class);
		JSONArray jsonArray= prepareCategoryPayload(multipleMerchants, dcRequest);
		logger.debug("BCT::MerchantDetailsResourceImpl::insertIntoMerchantCategory:payload jsonArray:"
				+ jsonArray);
		return catImpl.createMerchantCategories(jsonArray, dcRequest);
	}
	
	public JSONArray prepareCategoryPayload(ArrayList<MerchantDTO> multipleMerchants, DataControllerRequest dcRequest) throws JSONException, ApplicationException, IOException {
		JSONArray jsonArray= new JSONArray();
		logger.debug("BCT::checkIfMerchantsExist::prepareCategoryPayload: multipleMerchants:"+multipleMerchants);
		String tableName="merchantcategory";
		if(multipleMerchants!=null) {
			for(int i=0;i<multipleMerchants.size();i++) {
				
				// Category 
				String categoryCode=multipleMerchants.get(i).getCategoryCode();
				if(!HBLCommonUtility.checkCodeAvailableInDatabase(categoryCode, tableName, dcRequest)) {
				JSONObject cat= new JSONObject();
				cat.put("category","CATEGORY");
				//cat.put("logoUrl",multipleMerchants.get(i).getLogoUrl());
				if(multipleMerchants.get(i).getCategoryLogoUrl()!=null)
				cat.put("logoUrl",multipleMerchants.get(i).getCategoryLogoUrl());
				cat.put("subcategoryof","ALL");
				cat.put("isActive","true");
				cat.put("createdby","admin");
				cat.put("labelText",multipleMerchants.get(i).getCategoryof());
				cat.put("code",multipleMerchants.get(i).getCategoryCode());
				cat.put("merchantType",multipleMerchants.get(i).getFieldSetup());
				cat.put("paymentaggregator",multipleMerchants.get(i).getPaymentaggregator());
				jsonArray.put(cat);
				}
				
				
				// Sub category is optional
				String subCategoryCode=multipleMerchants.get(i).getSubcategoryCode();
				String merchantCode=multipleMerchants.get(i).getCode();
				// if Sub category is not empty create merchant under sub category
				if(!subCategoryCode.isEmpty()) {
				if(!HBLCommonUtility.checkCodeAvailableInDatabase(subCategoryCode, tableName, dcRequest)) {
				JSONObject subCat= new JSONObject();
				subCat.put("category","CATEGORY");
				if(multipleMerchants.get(i).getSubCategoryLogoUrl()!=null)
				subCat.put("logoUrl",multipleMerchants.get(i).getSubCategoryLogoUrl());
				subCat.put("subcategoryof",multipleMerchants.get(i).getCategoryCode());
				subCat.put("isActive","true");
				subCat.put("createdby","admin");
				subCat.put("labelText",multipleMerchants.get(i).getSubcategory());
				subCat.put("code",multipleMerchants.get(i).getSubcategoryCode());
				subCat.put("merchantType",multipleMerchants.get(i).getFieldSetup());
				subCat.put("paymentaggregator",multipleMerchants.get(i).getPaymentaggregator());
				jsonArray.put(subCat);
				}
				//Merchant 
				if(!HBLCommonUtility.checkCodeAvailableInDatabase(merchantCode, tableName, dcRequest)) {
				JSONObject merchant= new JSONObject();
				merchant.put("subcategoryof",multipleMerchants.get(i).getSubcategoryCode());
				merchant.put("code",multipleMerchants.get(i).getCode());
				merchant.put("labelText",multipleMerchants.get(i).getLabelText());
				merchant.put("category","APP");
				merchant.put("logoUrl",multipleMerchants.get(i).getLogoUrl());
				merchant.put("isActive","true");
				merchant.put("createdby","admin");
				merchant.put("merchantType",multipleMerchants.get(i).getFieldSetup());
				merchant.put("paymentaggregator",multipleMerchants.get(i).getPaymentaggregator());
				jsonArray.put(merchant);
				}else {
					throw new ApplicationException(ErrorCodeEnum.ERR_10021, "Duplicate Record Exist in "+ tableName );
				}
				}
				// if Sub category is empty create merchant under category
				else if(subCategoryCode.isEmpty()){ 
				if(!HBLCommonUtility.checkCodeAvailableInDatabase(merchantCode, tableName, dcRequest)) {
					JSONObject merchantWithoutSubCat= new JSONObject();
					merchantWithoutSubCat.put("category","APP");
					merchantWithoutSubCat.put("logoUrl",multipleMerchants.get(i).getLogoUrl());
					merchantWithoutSubCat.put("subcategoryof",multipleMerchants.get(i).getCategoryCode());
					merchantWithoutSubCat.put("isActive","true");
					merchantWithoutSubCat.put("createdby","admin");
					merchantWithoutSubCat.put("labelText",multipleMerchants.get(i).getLabelText());
					merchantWithoutSubCat.put("code",multipleMerchants.get(i).getCode());
					merchantWithoutSubCat.put("merchantType",multipleMerchants.get(i).getFieldSetup());
					merchantWithoutSubCat.put("paymentaggregator",multipleMerchants.get(i).getPaymentaggregator());
					jsonArray.put(merchantWithoutSubCat);
					}
					else {
					throw new ApplicationException(ErrorCodeEnum.ERR_10021, "Duplicate Record Exist in "+ tableName );
					}	
				}
			}
		}
		return jsonArray;
	}
	public Boolean checkIfMerchantsExist(ArrayList<MerchantDTO> multipleMerchants,  DataControllerRequest dcRequest) throws ApplicationException {
		Boolean isExist=false;
		String tableName="merchantdetails";
		if(multipleMerchants!=null) {
			for(int i=0;i<multipleMerchants.size();i++) {
				String merchantCode=multipleMerchants.get(i).getCode();
				isExist=HBLCommonUtility.checkCodeAvailableInDatabase(merchantCode, tableName, dcRequest);
				logger.debug("BCT::MerchantDetailsResourceImpl::checkIfMerchantsExist:Response:isExist:"+isExist+":code:"+merchantCode);
				if(isExist==true) {
					throw new ApplicationException(ErrorCodeEnum.ERR_10021, "Merchant Exist" );
				}
			}
		}
		
		return isExist;
	}
}
