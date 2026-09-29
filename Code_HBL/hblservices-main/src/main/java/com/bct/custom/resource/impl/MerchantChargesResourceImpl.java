package com.bct.custom.resource.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;

import com.bct.custom.businessdeligate.api.MerchantChargesBusinessDelegate;
import com.bct.custom.businessdeligate.api.MerchantFieldsBusinessDeligate;
import com.bct.custom.dto.MerchantFieldsDTO;
import com.bct.custom.dto.MerchantPaymentCharges;
import com.bct.custom.resource.api.MerchantChargesResource;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class MerchantChargesResourceImpl implements MerchantChargesResource{
	private static final Logger logger = LogManager.getLogger(MerchantChargesResourceImpl.class);
	@Override
	public Result merchantChargesCRUDOperation(String methodID, Object[] inputArray,
			DataControllerRequest dcRequest, DataControllerResponse dcResponse) throws ApplicationException {
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		String request = inputParams.get("merchantPaymentCharges").toString();
		Result result = new Result();
		MerchantChargesBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(MerchantChargesBusinessDelegate.class);
		if (methodID.equalsIgnoreCase("createMerchantCharges")) {
			try {
				JSONArray requestArray = new JSONArray(request);
				if(requestArray.length()>0) {
				ArrayList<MerchantPaymentCharges> dtoList = validateCreateMerchantCharges(requestArray, dcRequest);
				result = businessDelegate.createMerchantCharges(dtoList, dcRequest, inputParams);
				logger.debug("BCT::MerchantChargesResourceImpl::createMerchantCharges: response:" + result.toString());
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
		if (methodID.equalsIgnoreCase("updateMerchantCharges")) {
			try {
				JSONArray requestArray = new JSONArray(request);
				if(requestArray.length()>0) {
				ArrayList<MerchantPaymentCharges> dtoList = validateEditMerchantCharges(requestArray, dcRequest);
				result = businessDelegate.updateMerchantCharges(dtoList.get(0), dcRequest, inputParams);
				logger.debug("BCT::MerchantChargesResourceImpl::createMerchantCharges: response:" + result.toString());
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
		if (methodID.equalsIgnoreCase("deleteMerchantCharges")) {
			try {
				JSONArray requestArray = new JSONArray(request);
				ArrayList<MerchantPaymentCharges> dtoList = validateDeleteMerchantCharges(requestArray, dcRequest);
				result = businessDelegate.deleteMerchantCharges(dtoList, dcRequest, inputParams);
				logger.debug("BCT::MerchantChargesResourceImpl::deleteMerchantCharges: response:" + result.toString());
				
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
		return result;
	}

	@Override
	public ArrayList<MerchantPaymentCharges> validateCreateMerchantCharges(JSONArray array,
			DataControllerRequest dcRequest) throws ApplicationException, IOException {
		MerchantPaymentCharges dto;
		ArrayList<MerchantPaymentCharges> merchantList = new ArrayList<MerchantPaymentCharges>();
		for(int i=0;i<array.length();i++) {
			String request=array.getJSONObject(i).toString();
			dto = JSONUtils.parse(request, MerchantPaymentCharges.class);
			if(validateCreateMerchantCharges(dto, dcRequest)) {
				merchantList.add(dto);
			}else {
				throw new ApplicationException(ErrorCodeEnum.ERR_10755, "Invalid limits And Charges");
				}
		}
		if(merchantList.size()==0) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10755, "Invalid limits And Charges");
		}
		
		return merchantList;
	}
	public boolean validateCreateMerchantCharges(MerchantPaymentCharges dto, DataControllerRequest dcRequest) {
		boolean isValid=true;
		logger.debug("BCT::MerchantChargesResourceImpl::createMerchantCharges: MerchantFieldsDTO:" + dto.toString());
		if (StringUtils.isBlank(dto.getMerchantCode()) || dto.getMinAmount()==null
				|| dto.getMaxAmount()==null || dto.getFee()==null ) {
			isValid= false;
		}
		return isValid;
	}
	@Override
	public ArrayList<MerchantPaymentCharges> validateEditMerchantCharges(JSONArray array,
			DataControllerRequest dcRequest) throws ApplicationException, IOException {
		MerchantPaymentCharges dto;
		ArrayList<MerchantPaymentCharges> merchantList = new ArrayList<MerchantPaymentCharges>();
		for(int i=0;i<array.length();i++) {
			String request=array.getJSONObject(i).toString();
			dto = JSONUtils.parse(request, MerchantPaymentCharges.class);
			if((dto.getIsNew()==false || dto.getIsNew()==null)&&  validateEditMerchantCharges(dto, dcRequest)) {
				merchantList.add(dto);
			}else if(dto.getIsNew()==true &&  validateCreateMerchantCharges(dto, dcRequest)) {
				merchantList.add(dto);
			}
			else {
				throw new ApplicationException(ErrorCodeEnum.ERR_10755, "Invalid MerchantCharges in validateEditMerchantCharges");
				}
		}
		if(merchantList.size()==0) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10755, "Invalid MerchantCharges in validateEditMerchantCharges");
		}
		
		return merchantList;
	}
	public boolean validateEditMerchantCharges(MerchantPaymentCharges dto, DataControllerRequest dcRequest) {
		boolean isValid=true;
		logger.debug("BCT::MerchantChargesResourceImpl::validateEditMerchantCharges: MerchantFieldsDTO:" + dto.toString());
		if (StringUtils.isBlank(dto.getMerchantCode()) || dto.getId()==null) {
			isValid= false;
		}
		return isValid;
	}
	
	@Override
	public ArrayList<MerchantPaymentCharges> validateDeleteMerchantCharges(JSONArray array, DataControllerRequest dcRequest) throws ApplicationException, IOException {
		boolean isValid=true;
		MerchantPaymentCharges dto;
		ArrayList<MerchantPaymentCharges> merchantList = new ArrayList<MerchantPaymentCharges>();
		for(int i=0;i<array.length();i++) {
			String jsonString=array.getJSONObject(i).toString();
			dto = JSONUtils.parse(jsonString, MerchantPaymentCharges.class);
		logger.debug("BCT::MerchantChargesResourceImpl::validateDeleteMerchantCharges: MerchantFieldsDTO:" + dto.toString());
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
