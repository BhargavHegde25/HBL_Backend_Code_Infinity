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

import com.bct.custom.businessdeligate.api.MerchantFieldsBusinessDeligate;
import com.bct.custom.dto.MerchantDTO;
import com.bct.custom.dto.MerchantFieldsDTO;
import com.bct.custom.dto.MerchantPaymentCharges;
import com.bct.custom.resource.api.MerchantFieldsResource;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.constants.DBPConstants;
import com.dbp.core.util.JSONUtils;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class MerchantFieldsResourceImpl implements MerchantFieldsResource {
	private static final Logger logger = LogManager.getLogger(MerchantFieldsResourceImpl.class);

	@Override
	public Result merchantFieldsCRUDOperation(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		Result result = new Result();
		MerchantFieldsDTO dto;
		JSONArray requestArray =null;
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		String request = inputParams.get("requestFormFields").toString();
		MerchantFieldsBusinessDeligate businessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(MerchantFieldsBusinessDeligate.class);
		if (methodID.equalsIgnoreCase("updateMerchantFields")) {
			try {
				requestArray = new JSONArray(request);
				ArrayList<MerchantFieldsDTO> inputs = validateEditMerchantFields(requestArray, dcRequest);
				result = businessDelegate.updateMerchantFileds(inputs.get(0), dcRequest, inputParams);
				logger.debug("BCT::MerchantFieldsResourceImpl::updateMerchantFields: response:" + result.toString());
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
		} else if (methodID.equalsIgnoreCase("createMerchantFields")) {
			try {
				requestArray = new JSONArray(request);
				if(requestArray.length()>0) {
				ArrayList<MerchantFieldsDTO> dtoList = validateCreateMerchantFieldsRequest(requestArray, dcRequest);
				result = businessDelegate.createMerchantFileds(dtoList, dcRequest, inputParams);
				logger.debug("BCT::MerchantFieldsResourceImpl::createMerchantFields: response:" + result.toString());
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
		
		else if (methodID.equalsIgnoreCase("createMerchantFields_old")) {
			try {
				requestArray = new JSONArray(request);
				request=requestArray.getJSONObject(0).toString();
				dto = JSONUtils.parse(request, MerchantFieldsDTO.class);
				Map<String, Object> inputs = validateCreateMerchantFieldsInputs(dto, dcRequest);
				logger.debug("BCT::MerchantFieldsResourceImpl::createMerchantFields: MerchantDTO:" + dto.toString());
				result = businessDelegate.createMerchantFileds(dto, dcRequest, inputParams);
				logger.debug("BCT::MerchantFieldsResourceImpl::createMerchantFields: response:" + result.toString());
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
				ArrayList<MerchantFieldsDTO> inputs = validateDeleteMerchantFields(requestArray, dcRequest);
				if(inputs!=null && inputs.size()>0) {
				result = businessDelegate.deleteMerchantFileds(inputs, dcRequest, inputParams);
				logger.debug("BCT::MerchantFieldsResourceImpl::deleteMerchantFields: response:" + result.toString());
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
	
	@Override
	public ArrayList<MerchantFieldsDTO> validateEditMerchantFields(JSONArray array, DataControllerRequest dcRequest)
			throws ApplicationException, IOException {
		MerchantFieldsDTO dto;
		ArrayList<MerchantFieldsDTO> merchantList = new ArrayList<MerchantFieldsDTO>();
		for(int i=0;i<array.length();i++) {
			String request=array.getJSONObject(i).toString();
			logger.debug("BCT::MerchantFieldsResourceImpl::validateCreateMerchantFieldsRequest: before parse MerchantFieldsDTO request:" + request);
			dto = JSONUtils.parse(request, MerchantFieldsDTO.class);
			if((dto.getIsNew()==false || dto.getIsNew()==null) && validateEditMerchantFields(dto, dcRequest)) {
				merchantList.add(dto);
			}else if(dto.getIsNew()==true && validateCreateMerchantFields(dto, dcRequest)) {
				merchantList.add(dto);
			}
			else {
				throw new ApplicationException(ErrorCodeEnum.ERR_10755, "Invalid update/create merchant fields");
				}
		}
		
		if(merchantList.size()==0) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10755, "Invalid merchant fields");
		}
		
		return merchantList;
	}

	public boolean validateEditMerchantFields(MerchantFieldsDTO dto, DataControllerRequest dcRequest)
			throws ApplicationException {
		boolean isValid= true;
		if (dto.getId()==null || StringUtils.isBlank(dto.getMerchantCode())) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10755);
		}
		/*Map<String, Object> inputParams = new HashMap<String, Object>();
		
		String loggedInUser = null;
		try {
			Map<String, String> userProfile = HelperMethods.getCustomerFromIdentityService(dcRequest);
			loggedInUser = userProfile.get("UserName");
		} catch (Exception e) {
			logger.error("BCT::MerchantDetailsResourceImpl::validateInputs: exception:" + e.toString());
		}
		inputParams.put("modifiedby", loggedInUser);
		inputParams.put("currenttime", HelperMethods.getCurrentTimeStamp());
		*/
		return isValid;
	}

	public Map<String, Object> validateCreateMerchantFieldsInputs(MerchantFieldsDTO dto, DataControllerRequest dcRequest)
			throws ApplicationException {
		if (StringUtils.isBlank(dto.getMerchantCode()) || StringUtils.isBlank(dto.getFieldname())
				|| StringUtils.isBlank(dto.getFieldtype()) || StringUtils.isBlank(dto.getFieldvalue()) || dto.getFieldindex()==null || dto.getIsMandatory() ==null) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10755);
		}
		Map<String, Object> inputParams = new HashMap<String, Object>();
		/*String loggedInUser = null;
		try {
			Map<String, String> userProfile = HelperMethods.getCustomerFromIdentityService(dcRequest);
			loggedInUser = userProfile.get("UserName");
		} catch (Exception e) {
			logger.error("BCT::MerchantDetailsResourceImpl::validateCreateMerchantInputs: exception:" + e.toString());
		}
		inputParams.put("modifiedby", loggedInUser);
		inputParams.put("currenttime", HelperMethods.getCurrentTimeStamp());
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
	
	@Override
	public ArrayList<MerchantFieldsDTO> validateCreateMerchantFieldsRequest(JSONArray array, DataControllerRequest dcRequest)
			throws ApplicationException, IOException {
		MerchantFieldsDTO dto;
		ArrayList<MerchantFieldsDTO> merchantList = new ArrayList<MerchantFieldsDTO>();
		for(int i=0;i<array.length();i++) {
			String fieldValues=array.getJSONObject(i).has("fieldvalue")? array.getJSONObject(i).getString("fieldvalue"):"";
			array.getJSONObject(i).remove("fieldvalue");
			String request=array.getJSONObject(i).toString();
			dto = JSONUtils.parse(request, MerchantFieldsDTO.class);
			logger.debug("BCT::MerchantFieldsResourceImpl::validateCreateMerchantFieldsRequest: fieldValues:" + fieldValues);
			dto.setFieldvalue(fieldValues);
			if(validateCreateMerchantFields(dto, dcRequest)) {
				merchantList.add(dto);
			}else {
				throw new ApplicationException(ErrorCodeEnum.ERR_10755, "Invalid create merchant fields");
				}
		}
		if(merchantList.size()==0) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10755, "Invalid create merchant fields");
		}
		
		return merchantList;
	}
	public boolean validateCreateMerchantFields(MerchantFieldsDTO dto, DataControllerRequest dcRequest) {
		boolean isValid=true;
		if (StringUtils.isBlank(dto.getMerchantCode()) || StringUtils.isBlank(dto.getFieldname())
				|| StringUtils.isBlank(dto.getFieldtype()) || dto.getFieldindex()==null || dto.getIsMandatory() ==null || dto.getFieldcategory() ==null) {
			isValid= false;
		}
		return isValid;
	}
	@Override
	public ArrayList<MerchantFieldsDTO> validateDeleteMerchantFields(JSONArray array, DataControllerRequest dcRequest) throws ApplicationException, IOException {
		boolean isValid=true;
		MerchantFieldsDTO dto;
		ArrayList<MerchantFieldsDTO> merchantList = new ArrayList<MerchantFieldsDTO>();
		for(int i=0;i<array.length();i++) {
			String jsonString=array.getJSONObject(i).toString();
			dto = JSONUtils.parse(jsonString, MerchantFieldsDTO.class);
		logger.debug("BCT::MerchantChargesResourceImpl::validateDeleteMerchantFields: MerchantFieldsDTO:" + dto.toString());
		if (StringUtils.isBlank(dto.getMerchantCode()) || dto.getId()==null) {
			isValid= false;
			throw new ApplicationException(ErrorCodeEnum.ERR_10755, "Invalid id or merchant code");
		}
		else {
			merchantList.add(dto);
			}
		}
		return merchantList;
	}

}
