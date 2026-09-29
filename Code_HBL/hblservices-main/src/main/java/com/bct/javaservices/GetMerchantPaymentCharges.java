package com.bct.javaservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bct.custom.businessdeligate.api.MerchantChargesBusinessDelegate;
import com.bct.custom.businessdeligate.api.MerchantDetailsBusinessDeligate;
import com.bct.custom.businessdeligate.api.PaymentAggregatorBusinessDeligate;
import com.bct.custom.resource.api.GetAllMerchantOperationsResource;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class GetMerchantPaymentCharges implements JavaService2 {
	private static final Logger LOG = LogManager.getLogger(GetMerchantPaymentCharges.class);
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		 LOG.debug("BCT::GetMerchantPaymentCharges::");
		 JSONArray arrayResponse= new JSONArray();
		 JSONObject responseObj= new JSONObject();
        try {
        	MerchantChargesBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
    				.getBusinessDelegate(MerchantChargesBusinessDelegate.class);
        	Map<String, Object> inputParams = HelperMethods.getInputParamObjectMap(inputArray);
    		String merchantCode = inputParams.get("merchantCode")!=null?inputParams.get("merchantCode").toString():"";
    		LOG.debug("BCT::GetMerchantPaymentCharges:merchantCode:"+merchantCode);
    		if(StringUtils.isNotBlank(merchantCode)) {
    			arrayResponse = businessDelegate.getMerchantCharges(merchantCode, inputParams, request.getHeaderMap());
    			LOG.debug("BCT::GetMerchantPaymentCharges:response:"+arrayResponse);
        		responseObj.put("paymentCharges", arrayResponse);
        		responseObj.put("maxTransactionLimit",getMaxTransactionLimit(merchantCode, request));	
    			result = JSONToResult.convert(responseObj.toString());
    		}else {
    			result.addParam(new Param("dbpErrCode","HBL-100"));
    			result.addParam(new Param("dbpErrMsg", "Invalid Merchant code! Please provide merchant code"));
    		}
    		
        } catch (ApplicationException e) {
			e.getErrorCodeEnum().setErrorCode(result);
			LOG.error("Exception occured while fetching the merchant payment charges  :" + e.getMessage(), e);
		} catch (Exception e) {
			LOG.error("Exception occured while fetching the merchant payment charges  :" + e.getMessage(), e);
			ErrorCodeEnum.ERR_10021.setErrorCode(result);
		}
			return result;
	}
	public String getMaxTransactionLimit(String merchantCode, DataControllerRequest request) throws com.temenos.infinity.api.commons.exception.ApplicationException{
		MerchantDetailsBusinessDeligate businessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(MerchantDetailsBusinessDeligate.class);
		String maxTransactionLimit="";
		JSONArray merchantdetails = businessDelegate.getMerchantDetails(merchantCode, request.getHeaderMap());
		for(int i=0;i<merchantdetails.length();i++) {
			JSONObject merchant = merchantdetails.getJSONObject(i);
			maxTransactionLimit= merchant.get("maxTransactionLimit")!=null?merchant.get("maxTransactionLimit").toString():"";
		}
		return maxTransactionLimit;
		
	}

}
