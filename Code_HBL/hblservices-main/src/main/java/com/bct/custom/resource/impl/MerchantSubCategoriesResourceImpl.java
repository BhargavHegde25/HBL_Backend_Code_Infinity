package com.bct.custom.resource.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bct.custom.businessdeligate.api.MerchantSubCategoriesBusinessDelegate;
import com.bct.custom.resource.api.MerchantSubCategoriesResource;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class MerchantSubCategoriesResourceImpl implements MerchantSubCategoriesResource{
	private static final Logger logger = LogManager.getLogger(MerchantSubCategoriesResourceImpl.class);
	@Override
	public Result getMerchantSubCategoriesForAdmin(String methodID, Object[] inputArray,
			DataControllerRequest dcRequest, DataControllerResponse dcResponse) throws ApplicationException {
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String category = inputParams.get("category");
		String subCategory = inputParams.get("subCategory");
		logger.debug("HBL::MerchantSubCategoriesResourceImpl:getMerchantSubCategoriesForAdmin:category:"+category+",subCategory:"+subCategory); 
		Result result = new Result();
		if (StringUtils.isBlank(category)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10755);
		}
		try {
			MerchantSubCategoriesBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(MerchantSubCategoriesBusinessDelegate.class);
			JSONArray merchantSubCategories = businessDelegate.getMerchantSubCategoriesForAdmin(category,subCategory,
					dcRequest.getHeaderMap());
			logger.debug("BCT::MerchantSubCategoriesResourceImpl::getMerchantSubCategoriesForAdmin:"
					+ merchantSubCategories.toString());
				JSONObject merchantCategoriesForAdmin = new JSONObject();
				JSONArray newMerchantSubCategories=merchantSubCategoriesForAdmin(merchantSubCategories);
				merchantCategoriesForAdmin.put("subCategories", newMerchantSubCategories);
                result = JSONToResult.convert(merchantCategoriesForAdmin.toString());
			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));
		} catch (Exception e) {
			// TODO: handle exception
			result.addParam(new Param("dbpErrCode", e.toString()));
			result.addParam(new Param("dbpErrMsg", "10002"));
		}
		return result;
	}
	public JSONArray merchantSubCategoriesForAdmin(JSONArray merchantCategories) {
		JSONArray newMerchantCategories = new JSONArray();
		for(int i=0;i<merchantCategories.length();i++) {
			JSONObject obj =merchantCategories.getJSONObject(i);// new JSONObject();
			/*String subCatLogoUrl=merchantCategories.getJSONObject(i).has("subcategoryLogourl") && merchantCategories.getJSONObject(i).get("subcategoryLogourl") != null ? merchantCategories.getJSONObject(i).getString("subcategoryLogourl"):"";
			obj.put("labelText", merchantCategories.getJSONObject(i).getString("labelText"));
			obj.put("code", merchantCategories.getJSONObject(i).getString("code"));
			obj.put("subcategoryof", merchantCategories.getJSONObject(i).getString("subcategoryof"));
			obj.put("logoUrl", subCatLogoUrl);
			*/
			newMerchantCategories.put(obj);
		}
		return newMerchantCategories;
	}


}
