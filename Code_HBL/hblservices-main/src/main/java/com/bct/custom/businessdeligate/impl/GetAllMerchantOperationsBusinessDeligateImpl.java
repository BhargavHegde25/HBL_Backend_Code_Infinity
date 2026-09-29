package com.bct.custom.businessdeligate.impl;

import java.util.Map;

import org.json.JSONArray;

import com.bct.custom.backenddeligate.api.GetAllMerchantOperationsBackendDeligate;
import com.bct.custom.backenddeligate.api.MerchantSubCategoriesBackendDelegate;
import com.bct.custom.businessdeligate.api.GetAllMerchantOperationsBusinessDeligate;
import com.bct.custom.dto.MerchantCategoriesDTO;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public class GetAllMerchantOperationsBusinessDeligateImpl implements GetAllMerchantOperationsBusinessDeligate {

	@Override
	public JSONArray getAllMerchantCategories(String code, Map<String, Object> headerMap) throws ApplicationException {
		GetAllMerchantOperationsBackendDeligate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(GetAllMerchantOperationsBackendDeligate.class);
		return backendDelegate.getAllMerchantCategories(code, headerMap);
	}

	@Override
	public JSONArray getMerchantSubCategoriesByCategoryName(String categoryName,
			Map<String, Object> headerMap)throws ApplicationException {
		GetAllMerchantOperationsBackendDeligate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(GetAllMerchantOperationsBackendDeligate.class);
		return backendDelegate.getMerchantSubCategoriesByCategoryName(categoryName, headerMap);
	}
	
	@Override
	public JSONArray getAllMerchants(Map<String, Object> headerMap)throws ApplicationException {
		GetAllMerchantOperationsBackendDeligate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(GetAllMerchantOperationsBackendDeligate.class);
		return backendDelegate.getAllMerchants(headerMap);
	}
	@Override
	public JSONArray getMerchantDetailsByCategory(String code,
			Map<String, Object> headerMap)throws ApplicationException {
		GetAllMerchantOperationsBackendDeligate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(GetAllMerchantOperationsBackendDeligate.class);
		return backendDelegate.getMerchantDetailsByCategory(code, headerMap);
	}
	@Override
	public JSONArray getMerchantFields(String merchantCode,
			Map<String, Object> headerMap)throws ApplicationException {
		GetAllMerchantOperationsBackendDeligate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(GetAllMerchantOperationsBackendDeligate.class);
		return backendDelegate.getMerchantFields(merchantCode, headerMap);
	}
	@Override
	public JSONArray getMerchantDetails(String code,
			Map<String, Object> headerMap)throws ApplicationException {
		GetAllMerchantOperationsBackendDeligate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(GetAllMerchantOperationsBackendDeligate.class);
		return backendDelegate.getMerchantDetails(code, headerMap);
	}

	@Override
	public JSONArray getMerchantSubCategoriesForAdmin(String category, String subCategory,
			Map<String, Object> headerMap) throws ApplicationException {
		MerchantSubCategoriesBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(MerchantSubCategoriesBackendDelegate.class);
		return backendDelegate.getMerchantSubCategoriesForAdmin(category, subCategory, headerMap);
	}
	
	@Override
	public JSONArray getMerchantSubCategoriesForAdmin(String categoryName,
			Map<String, Object> headerMap)throws ApplicationException {
		GetAllMerchantOperationsBackendDeligate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(GetAllMerchantOperationsBackendDeligate.class); 
		return backendDelegate.getMerchantSubCategoriesForAdmin(categoryName, headerMap);
	}

	

}
