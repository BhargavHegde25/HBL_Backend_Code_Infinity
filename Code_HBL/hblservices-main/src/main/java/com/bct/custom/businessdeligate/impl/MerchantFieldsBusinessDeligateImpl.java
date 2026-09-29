package com.bct.custom.businessdeligate.impl;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;

import com.bct.custom.backenddeligate.api.MerchantChargesBackendDelegate;
import com.bct.custom.backenddeligate.api.MerchantFieldsBackendDeligate;
import com.bct.custom.backenddeligate.api.PaymentAggregatorBackendDeligate;
import com.bct.custom.businessdeligate.api.MerchantFieldsBusinessDeligate;
import com.bct.custom.dto.MerchantDTO;
import com.bct.custom.dto.MerchantFieldsDTO;
import com.bct.custom.dto.MerchantPaymentCharges;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public class MerchantFieldsBusinessDeligateImpl implements MerchantFieldsBusinessDeligate{

	

	@Override
	public Result updateMerchantFileds(MerchantFieldsDTO dto, DataControllerRequest request, Map<String, Object> inputs)
			throws ApplicationException {
		MerchantFieldsBackendDeligate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(MerchantFieldsBackendDeligate.class);
		return backendDelegate.updateMerchantFileds(dto, request, inputs);
	}
	
	@Override
	public Result updateMerchantFileds(ArrayList<MerchantFieldsDTO> dtoList, DataControllerRequest request, Map<String, Object> inputs)
			throws ApplicationException {
		MerchantFieldsBackendDeligate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(MerchantFieldsBackendDeligate.class);
		return backendDelegate.updateMerchantFileds(dtoList, request, inputs);
	}

	@Override
	public Result createMerchantFileds(MerchantFieldsDTO dto, DataControllerRequest request, Map<String, Object> inputs)
			throws ApplicationException {
		MerchantFieldsBackendDeligate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(MerchantFieldsBackendDeligate.class);
		return backendDelegate.createMerchantFileds(dto, request, inputs);
	}

	@Override
	public Result createMerchantFileds(ArrayList<MerchantFieldsDTO> dtoList, DataControllerRequest dcRequest,
			Map<String, Object> inputParams) throws ApplicationException {
		MerchantFieldsBackendDeligate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(MerchantFieldsBackendDeligate.class);
		return backendDelegate.createMerchantFileds(dtoList, dcRequest, inputParams);
	}
	
	@Override
	public JSONArray getMerchantFileds(String code, Map<String, Object> inputMap, Map<String, Object> headerMap) throws ApplicationException {
		MerchantFieldsBackendDeligate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(MerchantFieldsBackendDeligate.class);
		return backendDelegate.getMerchantFileds(code, inputMap, headerMap);
	}
	@Override
	public Result deleteMerchantFileds(ArrayList<MerchantFieldsDTO> dtoList, DataControllerRequest request,
			Map<String, Object> inputs) throws ApplicationException {
		MerchantFieldsBackendDeligate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(MerchantFieldsBackendDeligate.class);
		return backendDelegate.deleteMerchantFileds(dtoList, request, inputs);
	}

	

}
