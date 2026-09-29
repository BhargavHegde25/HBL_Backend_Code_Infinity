package com.bct.custom.businessdeligate.impl;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;

import com.bct.custom.backenddeligate.api.MerchantDetailsBackendDeligate;
import com.bct.custom.backenddeligate.api.PaymentAggregatorBackendDeligate;
import com.bct.custom.businessdeligate.api.MerchantDetailsBusinessDeligate;
import com.bct.custom.dto.MerchantDTO;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public class MerchantDetailsBusinessDeligateImpl implements MerchantDetailsBusinessDeligate{

	@Override
	public Result updateMerchantDetails(MerchantDTO dto, DataControllerRequest request, Map<String, Object> inputs)
			throws ApplicationException {
		MerchantDetailsBackendDeligate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(MerchantDetailsBackendDeligate.class);
		return backendDelegate.updateMerchantDetails(dto, request, inputs);
		
	}

	@Override
	public Result createMerchantDetails(ArrayList<MerchantDTO> merchants, DataControllerRequest request, Map<String, Object> inputs)
			throws ApplicationException {
		MerchantDetailsBackendDeligate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(MerchantDetailsBackendDeligate.class);
		return backendDelegate.createMerchantDetails(merchants, request, inputs);
	}
	
	@Override
	public Result createMerchantDetails(MerchantDTO dto, DataControllerRequest request, Map<String, Object> inputs)
			throws ApplicationException {
		MerchantDetailsBackendDeligate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(MerchantDetailsBackendDeligate.class);
		return backendDelegate.createMerchantDetails(dto, request, inputs);
	}

	@Override
	public JSONArray getMerchantDetails(String code, Map<String, Object> headerMap) throws ApplicationException {
		// TODO Auto-generated method stub
		MerchantDetailsBackendDeligate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(MerchantDetailsBackendDeligate.class);
		return backendDelegate.getMerchantDetails(code, headerMap);
	}

}
