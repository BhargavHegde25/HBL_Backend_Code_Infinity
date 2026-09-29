package com.bct.custom.backenddeligate.api;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;

import com.bct.custom.dto.MerchantFieldsDTO;
import com.bct.custom.dto.MerchantPaymentCharges;
import com.dbp.core.api.BackendDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;


public interface MerchantChargesBackendDelegate extends BackendDelegate {
	public Result updateMerchantCharges(MerchantPaymentCharges dto, DataControllerRequest request, Map<String, Object> inputs)
			throws ApplicationException;
	public Result createMerchantCharges(ArrayList<MerchantPaymentCharges> dtoList, DataControllerRequest dcRequest,
			Map<String, Object> inputParams) throws ApplicationException;
	public JSONArray getMerchantCharges(String code, Map<String, Object> inputMap, Map<String, Object> headerMap);
	public Result updateMerchantCharges(ArrayList<MerchantPaymentCharges> merchantCharges, DataControllerRequest dcRequest,
			Map<String, Object> inputs) throws ApplicationException;
	Result deleteMerchantCharges(ArrayList<MerchantPaymentCharges> merchantCharges, DataControllerRequest request,
			Map<String, Object> inputs) throws ApplicationException;

}
