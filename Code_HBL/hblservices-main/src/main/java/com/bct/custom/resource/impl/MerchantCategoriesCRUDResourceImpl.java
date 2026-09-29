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
import org.json.JSONObject;

import com.bct.custom.businessdeligate.api.GetAllMerchantOperationsBusinessDeligate;
import com.bct.custom.businessdeligate.api.MerchantCategoriesBusinessDeligate;
import com.bct.custom.businessdeligate.api.MerchantDetailsBusinessDeligate;
import com.bct.custom.businessdeligate.api.MerchantSubCategoriesBusinessDelegate;
import com.bct.custom.dto.CIPSMerchantCategoriesDTO;
import com.bct.custom.dto.MerchantCategoriesDTO;
import com.bct.custom.dto.MerchantDTO;
import com.bct.custom.resource.api.MerchantCategoriesCRUDResource;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.constants.DBPConstants;
import com.dbp.core.util.JSONUtils;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class MerchantCategoriesCRUDResourceImpl implements MerchantCategoriesCRUDResource{
	private static final Logger logger = LogManager.getLogger(MerchantCategoriesCRUDResourceImpl.class);
	@Override
	public Result merchantCategoriesCRUDOperation(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		Result result = new Result();
		MerchantCategoriesDTO dto;
		JSONArray requestArray =null;
		ArrayList<MerchantCategoriesDTO> categoriesArray;
		Map<String, Object> inputParams1 = (HashMap<String, Object>) inputArray[1];
		String request = inputParams1.get("merchantCategories").toString();
		logger.debug("BCT::MerchantCategoriesCRUDResourceImpl::merchantCategoriesCRUDOperation: merchantCategoriesCRUDOperationRequest:"
				+ request);
		MerchantCategoriesBusinessDeligate businessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(MerchantCategoriesBusinessDeligate.class);
		if (methodID.equalsIgnoreCase("updateMerchantCategories")) {
			try {
				requestArray = new JSONArray(request);
				request=requestArray.getJSONObject(0).toString();
				dto = JSONUtils.parse(request, MerchantCategoriesDTO.class);
				Map<String, Object> inputs = validateInputs(dto, dcRequest);
				logger.debug("BCT::MerchantCategoriesCRUDResourceImpl::updateMerchantCategories: MerchantCategoriesDTO:" + dto.toString());
				result = businessDelegate.updateMerchantCategories(dto, dcRequest, inputs);
				logger.debug("BCT::MerchantCategoriesCRUDResourceImpl::updateMerchantCategories: response:" + result.toString());
				if (result.getParamByName("dbpErrCode") == null) {
					result.setParam(new Param("opstatus", "0"));
					result.setParam(new Param("httpStatusCode", "200"));
					result.setParam(new Param("success", "true"));
				}
			} catch (ApplicationException e) {
				result.addParam(new Param("dbpErrCode", e.getErrorCodeEnum().getErrorCodeAsString()));
				result.addParam(new Param("dbpErrMsg", e.getErrorCodeEnum().getMessage()));
			} catch (Exception e) {
				result.addParam(new Param("dbpErrCode", e.toString()));
				result.addParam(new Param("dbpErrMsg", e.getMessage()));
			}
		} else if (methodID.equalsIgnoreCase("createMerchantCategories")) {
			try {
				requestArray = new JSONArray(request);
				logger.debug("BCT::MerchantCategoriesCRUDResourceImpl::createMerchantCategories: MerchantCategoriesDTO:" + requestArray.toString());
				result = createMerchantCategories(requestArray, dcRequest);
				/*if(requestArray.length()>0) {
					categoriesArray = validateCreateMerchantCategoriesRequest(requestArray, dcRequest);
				logger.debug("BCT::MerchantCategoriesCRUDResourceImpl::createMerchantCategories: MerchantCategoriesDTO:" + categoriesArray.toString());
				result = businessDelegate.createMerchantCategories(categoriesArray, dcRequest, getOtherInput(dcRequest));
				logger.debug("BCT::MerchantCategoriesCRUDResourceImpl::createMerchantCategories: response:" + result.toString());
				if (result.getParamByName("dbpErrCode") == null) {
					result.setParam(new Param("opstatus", "0"));
					result.setParam(new Param("httpStatusCode", "200"));
					result.setParam(new Param("success", "true"));
				}
				}
				*/
			} catch (ApplicationException e) {
				result.addParam(new Param("dbpErrCode", e.getErrorCodeEnum().toString()));
				result.addParam(new Param("dbpErrMsg", e.getMessage()));
			} catch (Exception e) {
				result.addParam(new Param("dbpErrCode", e.toString()));
				result.addParam(new Param("dbpErrMsg", e.getMessage()));
			}

		}
		else if (methodID.equalsIgnoreCase("createCIPSMerchantCategories")) {
			try {
				requestArray = new JSONArray(request);
				request=requestArray.getJSONObject(0).toString();
				CIPSMerchantCategoriesDTO dto1 = JSONUtils.parse(request, CIPSMerchantCategoriesDTO.class);
				Map<String, Object> inputs = validateCIPSCreateMerchantInputs(dto1, dcRequest);
				logger.debug("BCT::MerchantCategoriesCRUDResourceImpl::createMerchantCategories: MerchantCategoriesDTO:" + dto1.toString());
				result = businessDelegate.createCIPSMerchantCategories(dto1, dcRequest, inputs);
				logger.debug("BCT::MerchantCategoriesCRUDResourceImpl::createMerchantCategories: response:" + result.toString());
				if (result.getParamByName("dbpErrCode") == null) {
					result.setParam(new Param("opstatus", "0"));
					result.setParam(new Param("httpStatusCode", "200"));
					result.setParam(new Param("success", "true"));
				}
			} catch (ApplicationException e) {
				result.addParam(new Param("dbpErrCode", e.getErrorCodeEnum().toString()));
				result.addParam(new Param("dbpErrMsg", e.getMessage()));
			} catch (Exception e) {
				result.addParam(new Param("dbpErrCode", e.toString()));
				result.addParam(new Param("dbpErrMsg", e.getMessage()));
			}

		}
		return result;
	}

	public Map<String, Object> validateInputs(MerchantCategoriesDTO dto, DataControllerRequest dcRequest)
			throws ApplicationException {
		if (dto.getId() == null) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10755);
		}
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

	public Map<String, Object> validateCreateMerchantInputs(MerchantCategoriesDTO dto, DataControllerRequest dcRequest)
			throws ApplicationException {
		if (StringUtils.isBlank(dto.getSubcategoryof()) || StringUtils.isBlank(dto.getCategory()) || StringUtils.isBlank(dto.getCode()) || StringUtils.isBlank(dto.getLabelText())) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10755);
		}
		Map<String, Object> inputParams = new HashMap<String, Object>();
		 String loggedInUser = null; 
		  try { 
			  Map<String, String> userProfile =HelperMethods.getCustomerFromIdentityService(dcRequest); 
		  loggedInUser =userProfile.get("UserName"); 
		  } catch (Exception e) {
		  logger.error("BCT::MerchantCategoriesCRUDResourceImpl::validateCreateMerchantInputs: exception:" +e.toString()); 
		  } 
		  inputParams.put("modifiedby", loggedInUser);
		  inputParams.put("currenttime", HelperMethods.getCurrentTimeStamp());
		 
		return inputParams;
	}
	
	public Map<String, Object> validateCIPSCreateMerchantInputs(CIPSMerchantCategoriesDTO dto, DataControllerRequest dcRequest)
			throws ApplicationException {
		if ( StringUtils.isBlank(dto.getSubcategoryof()) || StringUtils.isBlank(dto.getCategory()) || StringUtils.isBlank(dto.getLogoUrl()) || StringUtils.isBlank(dto.getCode()) || StringUtils.isBlank(dto.getLabelText() )) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10755);
		}
		Map<String, Object> inputParams = new HashMap<String, Object>();
		 String loggedInUser = null; 
		  try { 
			  Map<String, String> userProfile =HelperMethods.getCustomerFromIdentityService(dcRequest); 
		  loggedInUser =userProfile.get("UserName"); 
		  } catch (Exception e) {
		  logger.error("BCT::MerchantCategoriesCRUDResourceImpl::validateCreateMerchantInputs: exception:" +e.toString()); 
		  } 
		  inputParams.put("modifiedby", loggedInUser);
		  inputParams.put("currenttime", HelperMethods.getCurrentTimeStamp());
		 
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
	public ArrayList<MerchantCategoriesDTO> validateCreateMerchantCategoriesRequest(JSONArray array, DataControllerRequest dcRequest)
			throws ApplicationException, IOException {
		String request="";
		MerchantCategoriesDTO dto;
		ArrayList<MerchantCategoriesDTO> categoriesList = new ArrayList<MerchantCategoriesDTO>();
		for(int i=0;i<array.length();i++) {
			request=array.getJSONObject(i).toString();
			dto = JSONUtils.parse(request, MerchantCategoriesDTO.class);
			logger.debug("BCT::MerchantCategoriesCRUDResourceImpl::validateCreateMerchantCategoriesRequest: MerchantCategoriesDTO:" + dto.toString());
			if(validateCreateMerchantCategories(dto, dcRequest)) {
				categoriesList.add(dto);
			}else {
				throw new ApplicationException(ErrorCodeEnum.ERR_10755);
				}
		}
		if(categoriesList.size()==0) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10755);
		}
		
		return categoriesList;
	}
	public boolean validateCreateMerchantCategories(MerchantCategoriesDTO dto, DataControllerRequest dcRequest) throws ApplicationException {
		boolean isValid=true;
		if (StringUtils.isBlank(dto.getSubcategoryof()) || StringUtils.isBlank(dto.getCategory()) || StringUtils.isBlank(dto.getCode()) || StringUtils.isBlank(dto.getLabelText())) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10755);
		}
		logger.debug("BCT::MerchantCategoriesCRUDResourceImpl::validateCreateMerchantCategories: isValid:" + isValid);
		return isValid;
	}
	public Map<String, Object> getOtherInput(DataControllerRequest dcRequest)
			throws ApplicationException {
		Map<String, Object> inputParams = new HashMap<String, Object>();
		  String loggedInUser = null; 
		  try { 
			  Map<String, String> userProfile =HelperMethods.getCustomerFromIdentityService(dcRequest); 
		  loggedInUser =userProfile.get("UserName"); 
		  } catch (Exception e) {
		  logger.error("BCT::MerchantCategoriesCRUDResourceImpl::getOtherInput: exception:" +e.toString()); 
		  } 
		  inputParams.put("modifiedby", loggedInUser);
		  inputParams.put("currenttime", HelperMethods.getCurrentTimeStamp());
		 
		return inputParams;
	}
	@Override
	public Result createMerchantCategories(JSONArray requestArray ,DataControllerRequest dcRequest) throws ApplicationException, IOException, Exception {
		Result result = new Result();
		if(requestArray.length()>0) {
		MerchantCategoriesBusinessDeligate businessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(MerchantCategoriesBusinessDeligate.class);
		ArrayList<MerchantCategoriesDTO> categoriesArray = validateCreateMerchantCategoriesRequest(requestArray, dcRequest);
		logger.debug("BCT::MerchantCategoriesCRUDResourceImpl::createMerchantCategories: MerchantCategoriesDTO:" + categoriesArray.toString());
		result = businessDelegate.createMerchantCategories(categoriesArray, dcRequest, getOtherInput(dcRequest));
		logger.debug("BCT::MerchantCategoriesCRUDResourceImpl::createMerchantCategories: response:" + result.toString());
		if (result.getParamByName("dbpErrCode") == null) {
			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));
			result.setParam(new Param("success", "true"));
		}else {
			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));
			result.setParam(new Param("success", "false"));
		}
		}
		return result;
	}
	
	@Override
	public Boolean checkCodeAvailableInDatabase(String code, String tableName, DataControllerRequest dcRequest) {
		MerchantCategoriesBusinessDeligate businessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(MerchantCategoriesBusinessDeligate.class);
		return businessDelegate.checkCodeAvailableInDatabase(code, tableName, dcRequest);
		
	}
	
}
