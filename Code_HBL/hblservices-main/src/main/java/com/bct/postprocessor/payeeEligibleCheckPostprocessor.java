package com.bct.postprocessor;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.dbx.BasePostProcessor;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.constants.TemenosConstants;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;

public class payeeEligibleCheckPostprocessor extends BasePostProcessor {
	Logger logger = LogManager.getLogger(payeeEligibleCheckPostprocessor.class);

	@Override
	public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {

			logger.debug("Result:###" + ResultToJSON.convert(result));
			String categoryId = result.getParamValueByName("categoryId");
			logger.debug("categoryId of the account:###" + categoryId);
			String isSupport = getTransferSupportFlag(categoryId, "Cr", request);
			
			logger.debug("Account is support credit:###" + isSupport);
			
			if(isSupport.equalsIgnoreCase("1")) {
				result.setParam(new Param("supportTransferTo", "1"));
			}else {
				result.setParam(new Param("supportTransferTo", "0"));
			}

		} catch (Exception e) {
			logger.error(e);
			CommonUtils.setErrMsg(result, e.toString());
		}
		return result;
	}

	private String getTransferSupportFlag(String productId, String supportType, DataControllerRequest request) {
		String isSupported;

		logger.debug("getTransferSupportFlag productId::::" + productId);
		logger.debug("getTransferSupportFlag supportType::::" + supportType);

		JSONObject supportedAccs = ArrangementsUtils.getBundleConfigurations(TemenosConstants.ACCOUNT_TYPE_BUNDLE_NAME,
				"TRANSFER_SUPPORTED_ACCOUNTS", request);

		JSONObject configData = new JSONObject();
		String data = "";
		if (supportedAccs != null) {
			JSONArray configurations = supportedAccs.optJSONArray(TemenosConstants.CONFIGURATIONS);
			if (configurations != null && configurations.length() > 0) {
				configData = configurations.optJSONObject(0);
				if (configData.has(TemenosConstants.DBP_CONFIG_TABLE_VALUE))
					data = configData.getString(TemenosConstants.DBP_CONFIG_TABLE_VALUE);
			}
		}
		logger.debug("getTransferSupportFlag data::::" + data);

		JSONObject dataobj = new JSONObject(data);
		String supportedKeyVal = dataobj.has(productId) && dataobj.get(productId) != null ? dataobj.getString(productId)
				: "";
		logger.debug("getTransferSupportFlag dataobj::::" + dataobj.toString());
		logger.debug("getTransferSupportFlag supportedKeyVal::::" + supportedKeyVal);
		if (StringUtils.isNotBlank(supportedKeyVal)) {
			isSupported = (supportedKeyVal.contains(supportType)) ? "1" : "0";
		} else {
			isSupported = "0";
		}

		return isSupported;
	}
}
