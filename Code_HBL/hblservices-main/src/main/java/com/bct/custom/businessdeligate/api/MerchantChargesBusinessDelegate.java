package com.bct.custom.businessdeligate.api;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;

import com.bct.custom.dto.MerchantPaymentCharges;
import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;


public interface MerchantChargesBusinessDelegate extends BusinessDelegate {
	public Result updateMerchantCharges(MerchantPaymentCharges dto, DataControllerRequest request, Map<String, Object> inputs)
			throws ApplicationException;
	public Result updateMerchantCharges(ArrayList<MerchantPaymentCharges> dto, DataControllerRequest request,
			Map<String, Object> inputs) throws ApplicationException;
	public Result createMerchantCharges(ArrayList<MerchantPaymentCharges> dtoList, DataControllerRequest dcRequest,
			Map<String, Object> inputParams) throws ApplicationException;
	public JSONArray getMerchantCharges(String code, Map<String, Object> inputMap,
			Map<String, Object> headerMap)throws ApplicationException;
	Result deleteMerchantCharges(ArrayList<MerchantPaymentCharges> dtoList, DataControllerRequest request,
			Map<String, Object> inputs) throws ApplicationException;

}
