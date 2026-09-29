package com.temenos.dbx.product.resource.impl;

import org.apache.commons.lang3.StringUtils;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.ConvertJsonToResult;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.businessdelegate.api.FeatureBusinessDelegate;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.resource.api.FeatureResource;

public class FeatureResourceImpl implements FeatureResource {

	/**
	 * @author sowmya.vandanapu 
	 * @version 1.0
	 */
	
	@Override
	public Result getFeatures(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) {
		Result result = new Result();
		DBXResult responseDTO = new DBXResult();

		try {
			FeatureBusinessDelegate featureBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(FeatureBusinessDelegate.class);
			responseDTO = featureBusinessDelegate.getFeatures(dcRequest.getHeaderMap());
		} catch (Exception e) {
			responseDTO.setError(ErrorCodeEnum.ERR_10226);
		}

		if (StringUtils.isNotBlank(responseDTO.getDbpErrMsg()) || StringUtils.isNotBlank(responseDTO.getDbpErrCode())) {
			ErrorCodeEnum.ERR_10000.setErrorCode(result, responseDTO.getDbpErrCode(), responseDTO.getDbpErrMsg());
		} else if (responseDTO.getResponse() != null) {
			JsonObject json = (JsonObject) responseDTO.getResponse();
			if (!json.has("opstatus") || json.get("opstatus").getAsInt() != 0 || !json.has("feature")) {
				ErrorCodeEnum.ERR_10026.setErrorCode(result);
			}
			if (json.get("feature").getAsJsonArray().size() == 0) {
				ErrorCodeEnum.ERR_10027.setErrorCode(result);
			} else {
				result = ConvertJsonToResult.convert(json);
			}
		}
		return result;
	}

}
