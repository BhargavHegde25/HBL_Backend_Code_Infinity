package com.bct.custom.businessdeligate.api;

import java.util.Map;

import org.json.JSONArray;

import com.bct.custom.dto.MerchantCategoriesDTO;
import com.dbp.core.api.BusinessDelegate;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public interface GetAllMerchantOperationsBusinessDeligate extends BusinessDelegate{
	public JSONArray getAllMerchantCategories(String category, Map<String, Object> headerMap) throws ApplicationException;
	public JSONArray getMerchantSubCategoriesByCategoryName(String categoryName, Map<String, Object> headerMap) throws ApplicationException;
	public JSONArray getAllMerchants(Map<String, Object> headerMap)throws ApplicationException;
	public JSONArray getMerchantDetailsByCategory(String categoryName,
			Map<String, Object> headerMap)throws ApplicationException;
	public JSONArray getMerchantFields(String merchantName,
			Map<String, Object> headerMap)throws ApplicationException;
	public JSONArray getMerchantDetails(String code, Map<String, Object> headerMap) throws ApplicationException;
	public JSONArray getMerchantSubCategoriesForAdmin(String category, String subCategory,
			Map<String, Object> headerMap) throws ApplicationException;
	public JSONArray getMerchantSubCategoriesForAdmin(String categoryName, Map<String, Object> headerMap)
			throws ApplicationException;

}
