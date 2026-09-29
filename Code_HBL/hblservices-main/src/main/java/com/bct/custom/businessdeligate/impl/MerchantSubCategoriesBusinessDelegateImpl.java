package com.bct.custom.businessdeligate.impl;

import java.util.Map;

import org.json.JSONArray;

import com.bct.custom.backenddeligate.api.GetAllMerchantOperationsBackendDeligate;
import com.bct.custom.backenddeligate.api.MerchantSubCategoriesBackendDelegate;
import com.bct.custom.businessdeligate.api.MerchantSubCategoriesBusinessDelegate;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public class MerchantSubCategoriesBusinessDelegateImpl implements MerchantSubCategoriesBusinessDelegate{

	@Override
	public JSONArray getMerchantSubCategoriesForAdmin(String category, String subCategory,
			Map<String, Object> headerMap) throws ApplicationException {
		MerchantSubCategoriesBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(MerchantSubCategoriesBackendDelegate.class);
		return backendDelegate.getMerchantSubCategoriesForAdmin(category, subCategory, headerMap);
	}

}
