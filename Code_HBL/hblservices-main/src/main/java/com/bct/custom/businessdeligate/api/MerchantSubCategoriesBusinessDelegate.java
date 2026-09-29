package com.bct.custom.businessdeligate.api;

import java.util.Map;

import org.json.JSONArray;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public interface MerchantSubCategoriesBusinessDelegate extends BusinessDelegate {

	public JSONArray getMerchantSubCategoriesForAdmin(String category, String subCategory,
			Map<String, Object> headerMap) throws ApplicationException;
}
