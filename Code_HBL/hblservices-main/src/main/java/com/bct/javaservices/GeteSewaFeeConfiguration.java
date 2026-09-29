package com.bct.javaservices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;

public class GeteSewaFeeConfiguration implements JavaService2 {

	public static LoggerUtil logger = new LoggerUtil(GeteSewaFeeConfiguration.class);

	@Override
	public Object invoke(String arg0, Object[] arg1, DataControllerRequest request, DataControllerResponse arg3)
			throws Exception {
		logger.debug("GeteSewaFeeConfiguration:");
		Result result = new Result();
		try {
			JSONArray feeConfigurationArray = geteSewaFeeConfiguration(request);
			logger.debug("feeConfigurationArray:" + feeConfigurationArray);
			if (isValidFeeConfiguration(feeConfigurationArray)) {
				JSONObject resultObj = new JSONObject();
				resultObj.put("eSewaLoadFee", feeConfigurationArray);
				result = JSONToResult.convert(resultObj.toString());
				result.setParam(new Param("opstatus", "0"));
				result.setParam(new Param("httpStatusCode", "200"));
				result.addParam(new Param("status", "success"));
			}
		} catch (JSONException e) {
			result.addParam(new Param("dbpErrCode", "20005"));
			result.addParam(new Param("dbpErrMsg", e.getMessage()));
			result.addParam(new Param("errorMessage", "Invalid Fee Configuration"));
			result.addParam(new Param("status", "failed"));
		} catch (Exception e) {
			result.addParam(new Param("dbpErrCode", "20005"));
			result.addParam(new Param("dbpErrMsg", e.getMessage()));
			result.addParam(new Param("errorMessage", "Invalid Fee Configuration"));
			result.addParam(new Param("status", "failed"));
		}
		return result;
	}


	private JSONArray geteSewaFeeConfiguration(DataControllerRequest request) throws Exception {

		JSONObject supportedAccs = ArrangementsUtils.getBundleConfigurations(TemenosConstants.ACCOUNT_TYPE_BUNDLE_NAME,
				"ESEWA_TRANSFER_FEE", request);
		JSONArray dataobj = new JSONArray();
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
		logger.debug("geteSewaFeeConfiguration data::::" + data);
		dataobj = new JSONArray(data);
		return dataobj;
	}

	public Boolean isValidFeeConfiguration(JSONArray array) throws JSONException, Exception {
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = array.getJSONObject(i);
			Double minRange = obj.getDouble("minRange");
			Double maxRange = obj.getDouble("maxRange");
			Double fee = obj.getDouble("feeValue");
		}
		return true;
	}

}
