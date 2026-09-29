package com.bct.custom.backenddeligate.api;

import java.util.Map;

import org.json.JSONArray;

import com.dbp.core.api.BackendDelegate;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public interface MerchantSubCategoriesBackendDelegate extends BackendDelegate {

	public JSONArray getMerchantSubCategoriesForAdmin(String category, String subCategory,
			Map<String, Object> headerMap)throws ApplicationException;
}
