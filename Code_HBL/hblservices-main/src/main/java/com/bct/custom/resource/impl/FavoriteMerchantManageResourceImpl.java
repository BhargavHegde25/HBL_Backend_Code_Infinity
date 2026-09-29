package com.bct.custom.resource.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bct.custom.businessdeligate.api.BranchDetailsBusinessDelegate;
import com.bct.custom.businessdeligate.api.FavoriteMerchantBusinessDelegate;
import com.bct.custom.dto.FavoriteMerchantDTO;
import com.bct.custom.dto.MerchantDTO;
import com.bct.custom.resource.api.FavoriteMerchantManageResource;
import com.bct.javaservices.FavoriteMerchantManageService;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.kony.dbputilities.util.ErrorCodeEnum;

public class FavoriteMerchantManageResourceImpl implements FavoriteMerchantManageResource{
	private static final Logger LOG = LogManager.getLogger(FavoriteMerchantManageResourceImpl.class);
	@Override
	public Result favoriteMerchantCRUDOperations(String methodId, Object[] inputArray, DataControllerRequest dcRequest)
			throws ApplicationException {
		Result result = new Result();
		Map<String, Object> inputParams = HelperMethods.getInputParamObjectMap(inputArray);
		FavoriteMerchantBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(FavoriteMerchantBusinessDelegate.class);
		LOG.debug("BCT::FavoriteMerchantManageResourceImpl:methodId:"+methodId);
		try {
			if(methodId.equals("createFavoriteMerchant")) {
				String request = inputParams.get("favoriteMerchant").toString();
				JSONArray requestArray = new JSONArray(request);
				if(requestArray.length()>0) {
					request=requestArray.get(0).toString();
				FavoriteMerchantDTO dto = JSONUtils.parse(request, FavoriteMerchantDTO.class);
				JSONArray payees = businessDelegate.getFavoriteMerchants(null, null, dcRequest);
				if(payees.length()<5) {
				Result response=businessDelegate.createFavoriteMerchant(dto, inputParams, dcRequest);
				if(response.getParamValueByName("dbpErrCode") == null ) {
					String payeeId = response.getParamValueByName("payeeId");
					if (null != payeeId) {
						result.setParam(new Param("payeeId", payeeId));
						result.setParam(new Param("opstatus", "0"));
						result.setParam(new Param("httpStatusCode", "200"));
						result.setParam(new Param("success", "true"));
					}
				}else {
					result.addParam(new Param("dbpErrCode", response.getParamValueByName("dbpErrCode")));
					result.addParam(new Param("dbpErrMsg", response.getParamValueByName("dbpErrMsg")));
					result.setParam(new Param("success", "false"));
				}
				}else {
					result.addParam(new Param("dbpErrCode", "Favorite_Merchant_Max_Limit_Exceded"));
					result.addParam(new Param("dbpErrMsg", "Favorite merchant miximum limit reached"));
					result.setParam(new Param("success", "false"));
				}
				}else {
					LOG.error("HBL:FavoriteMerchantManageResourceImpl:Invalid createFavoriteMerchant payload");
					throw new ApplicationException(ErrorCodeEnum.ERR_20049, "Invalid Payload");
				}
			}else if(methodId.equals("updateFavoriteMerchant")) {
				if(isValidPayload(inputParams)) {
				Result updateResponse = businessDelegate.updateFavoriteMerchant(null, inputParams, dcRequest);
				if(updateResponse.getParamValueByName("dbpErrCode") == null ) {
					String payeeId = updateResponse.getParamValueByName("payeeId");
					if (null != payeeId) {
						result.setParam(new Param("payeeId", payeeId));
						result.setParam(new Param("opstatus", "0"));
						result.setParam(new Param("success", "true"));
						result.setParam(new Param("httpStatusCode", "200"));
					}
				}else {
					result.addParam(new Param("dbpErrCode", updateResponse.getParamValueByName("dbpErrCode")));
					result.addParam(new Param("dbpErrMsg", updateResponse.getParamValueByName("dbpErrMsg")));
					result.setParam(new Param("success", "false"));
				}
				}else {
					LOG.error("HBL:FavoriteMerchantManageResourceImpl:Invalid updateFavoriteMerchant payload");
					throw new ApplicationException(ErrorCodeEnum.ERR_20049, "Invalid Payload");
				}
			}else if(methodId.equals("getFavoriteMerchants")) {
				JSONArray favoriteMerchants = businessDelegate.getFavoriteMerchants(methodId, null, dcRequest);	
				if(favoriteMerchants!=null ) {
				JSONObject favoriteMerchantsObj = new JSONObject();
				favoriteMerchantsObj.put("favoriteMerchants", favoriteMerchants);
                result = JSONToResult.convert(favoriteMerchantsObj.toString());
                result.setParam(new Param("opstatus", "0"));
                result.setParam(new Param("httpStatusCode", "200"));
				}
			}
			else if(methodId.equals("deleteFavoriteMerchant")) {
				if(inputParams.get("payeeId")!=null) {
				Result deleteResponse = businessDelegate.deleteFavoriteMerchant(inputParams, dcRequest);
				if(deleteResponse.getParamValueByName("dbpErrCode") == null ) {
					String payeeId = deleteResponse.getParamValueByName("payeeId");
					if (null != payeeId) {
						result.setParam(new Param("payeeId", payeeId));
						result.setParam(new Param("opstatus", "0"));
						result.setParam(new Param("success", "true"));
						result.setParam(new Param("httpStatusCode", "200"));
					}
				}else {
					result.addParam(new Param("dbpErrCode", deleteResponse.getParamValueByName("dbpErrCode")));
					result.addParam(new Param("dbpErrMsg", deleteResponse.getParamValueByName("dbpErrMsg")));
					result.setParam(new Param("success", "false"));
				}
				}else {
					LOG.error("HBL:FavoriteMerchantManageResourceImpl:Invalid deleteFavoriteMerchant payload");
					throw new ApplicationException(ErrorCodeEnum.ERR_20049, "Invalid Payload");
				}
			}
		
	} catch (ApplicationException ae) {
		LOG.error("Exception Occured at FavoriteMerchantManageResourceImpl:"+ae.getMessage());
		result.addParam(new Param("dbpErrCode", ae.getErrorCodeEnum().getErrorCodeAsString()));
		result.addParam(new Param("dbpErrMsg", ae.getErrorCodeEnum().getMessage()));
		result.setParam(new Param("success", "false"));
	}
		catch (Exception e) {
		LOG.error("Exception Occured at FavoriteMerchantManageResourceImpl:"+e.getMessage());
		result.addParam(new Param("dbpErrCode", e.getLocalizedMessage()));
		result.addParam(new Param("dbpErrMsg", e.getMessage()));
		result.setParam(new Param("success", "false"));
	}
		return result;
	}
	public boolean isValidPayload(Map<String, Object> inputParams) {
		boolean isValid=false;
		try {
		String isFavoriteMerchant= inputParams.get("isFavoriteMerchant")!=null?inputParams.get("isFavoriteMerchant").toString():"";
		Boolean isFavoriteMerchant1 = Boolean.valueOf(isFavoriteMerchant);
		if(inputParams.get("payeeId")!=null ) {
				if(isFavoriteMerchant!=null) {
					inputParams.put("isManuallyAdded", isFavoriteMerchant1);
					isValid= true;
				}
		}
		}catch (Exception e) {
			LOG.error("Exception Occured at FavoriteMerchantManageResourceImpl:invalid payload"+e.getMessage());
		}
		return isValid;
	}
}
