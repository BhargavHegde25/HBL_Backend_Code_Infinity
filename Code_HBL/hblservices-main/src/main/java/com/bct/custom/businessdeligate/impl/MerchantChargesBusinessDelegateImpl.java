package com.bct.custom.businessdeligate.impl;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;

import com.bct.custom.backenddeligate.api.MerchantChargesBackendDelegate;
import com.bct.custom.backenddeligate.api.MerchantFieldsBackendDeligate;
import com.bct.custom.businessdeligate.api.MerchantChargesBusinessDelegate;
import com.bct.custom.dto.MerchantPaymentCharges;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;


public class MerchantChargesBusinessDelegateImpl implements MerchantChargesBusinessDelegate{

	@Override
	public Result updateMerchantCharges(MerchantPaymentCharges dto, DataControllerRequest request,
			Map<String, Object> inputs) throws ApplicationException {
		MerchantChargesBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(MerchantChargesBackendDelegate.class);
		return backendDelegate.updateMerchantCharges(dto, request, inputs);
	}
	
	@Override
	public Result updateMerchantCharges(ArrayList<MerchantPaymentCharges> dtoList, DataControllerRequest request,
			Map<String, Object> inputs) throws ApplicationException {
		MerchantChargesBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(MerchantChargesBackendDelegate.class);
		return backendDelegate.updateMerchantCharges(dtoList, request, inputs);
	}

	@Override
	public Result createMerchantCharges(ArrayList<MerchantPaymentCharges> dtoList, DataControllerRequest dcRequest,
			Map<String, Object> inputParams) throws ApplicationException {
		MerchantChargesBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(MerchantChargesBackendDelegate.class);
		return backendDelegate.createMerchantCharges(dtoList, dcRequest, inputParams);
	}

	@Override
	public JSONArray getMerchantCharges(String code, Map<String, Object> inputMap, Map<String, Object> headerMap) throws ApplicationException {
		MerchantChargesBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(MerchantChargesBackendDelegate.class);
		return backendDelegate.getMerchantCharges(code, inputMap, headerMap);
	}
	
	@Override
	public Result deleteMerchantCharges(ArrayList<MerchantPaymentCharges> dtoList, DataControllerRequest request,
			Map<String, Object> inputs) throws ApplicationException {
		MerchantChargesBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(MerchantChargesBackendDelegate.class);
		return backendDelegate.deleteMerchantCharges(dtoList, request, inputs);
	}

}
