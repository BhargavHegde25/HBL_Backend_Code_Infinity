package com.bct.custom.businessdeligate.impl;

import java.util.ArrayList;
import java.util.Map;

import com.bct.custom.backenddeligate.api.MerchantCategoriesBackendDeligate;
import com.bct.custom.backenddeligate.api.MerchantDetailsBackendDeligate;
import com.bct.custom.businessdeligate.api.MerchantCategoriesBusinessDeligate;
import com.bct.custom.dto.CIPSMerchantCategoriesDTO;
import com.bct.custom.dto.MerchantCategoriesDTO;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public class MerchantCategoriesBusinessDeligateImpl implements MerchantCategoriesBusinessDeligate{

	@Override
	public Result updateMerchantCategories(MerchantCategoriesDTO dto, DataControllerRequest request,
			Map<String, Object> inputs) throws ApplicationException {
		MerchantCategoriesBackendDeligate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(MerchantCategoriesBackendDeligate.class);
		return backendDelegate.updateMerchantCategories(dto, request, inputs);
	}

	@Override
	public Result createMerchantCategories(MerchantCategoriesDTO dto, DataControllerRequest request,
			Map<String, Object> inputs) throws ApplicationException {
		MerchantCategoriesBackendDeligate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(MerchantCategoriesBackendDeligate.class);
		return backendDelegate.createMerchantCategories(dto, request, inputs);
	}
	
	@Override
	public Result createCIPSMerchantCategories(CIPSMerchantCategoriesDTO dto, DataControllerRequest request,
			Map<String, Object> inputs) throws ApplicationException {
		MerchantCategoriesBackendDeligate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(MerchantCategoriesBackendDeligate.class);
		return backendDelegate.createCIPSMerchantCategories(dto, request, inputs);
	}

	@Override
	public Result createMerchantCategories(ArrayList<MerchantCategoriesDTO> categoriesArray,
			DataControllerRequest dcRequest, Map<String, Object> otherInput) throws ApplicationException {
		MerchantCategoriesBackendDeligate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(MerchantCategoriesBackendDeligate.class);
		return backendDelegate.createMerchantCategories(categoriesArray, dcRequest, otherInput);
	}
	@Override
	public Boolean checkCodeAvailableInDatabase(String code, String tableName, DataControllerRequest dcRequest) {
		MerchantCategoriesBackendDeligate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(MerchantCategoriesBackendDeligate.class);
		return backendDelegate.checkCodeAvailableInDatabase(code, tableName, dcRequest);
	
	}

}
