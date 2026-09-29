package com.bct.preprocessor;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.constants.TemenosConstants;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;

public class payeeEligibleCheckPreprocessor implements DataPreProcessor2 {
	private static final Logger LOG = LogManager.getLogger(payeeEligibleCheckPreprocessor.class);

	@SuppressWarnings("unchecked")
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {

		String accountNumber = request.getParameter("accountNumber");
		String isSameBankAccount = request.getParameter("isSameBankAccount");
		LOG.debug("Result accountNumber:###" + accountNumber);
		LOG.debug("Request isSameBankAccount:###" + isSameBankAccount);
		if(isSameBankAccount.equalsIgnoreCase("true")) {
		String categoryId = getAccCategoryID(request, accountNumber);
		LOG.debug("Result categoryId:###" + categoryId);
		String isSupport = getTransferSupportFlag(categoryId, "Cr", request);
		LOG.debug("Result isSupport:###" + isSupport);

		if (isSupport.equalsIgnoreCase("0")) {
			result.addParam(new Param("supportTransferTo", "0"));
			result.addParam(new Param("errmsg", "The account number entered is not eligible to receive funds. Kindly try using a different Account number."));

			return false;
		}
		}

		return true;
	}

	public String getAccCategoryID(DataControllerRequest request, String accountNumber) throws Exception {

		Result result = new Result();
		String categoryId = "";

		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		HashMap<String, Object> headerParams = new HashMap<String, Object>();
		headerParams.put("accountNumber", accountNumber);
		inputParams.put("accountNumber", accountNumber);
		result = CommonUtils.callIntegrationService(request, inputParams, headerParams, "T24ISPaymentsView",
				"getBeneficiaryName", false);

		LOG.debug("getAccCategoryID Result:###" + ResultToJSON.convert(result));
		categoryId = result.getParamValueByName("categoryId");
		LOG.debug("categoryId Result:###" + categoryId);

		return categoryId;

	}

	private String getTransferSupportFlag(String productId, String supportType, DataControllerRequest request) {
		String isSupported;

		LOG.debug("getTransferSupportFlag productId::::" + productId);
		LOG.debug("getTransferSupportFlag supportType::::" + supportType);

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
		LOG.debug("getTransferSupportFlag data::::" + data);

		JSONObject dataobj = new JSONObject(data);
		String supportedKeyVal = dataobj.has(productId) && dataobj.get(productId) != null ? dataobj.getString(productId)
				: "";
		LOG.debug("getTransferSupportFlag dataobj::::" + dataobj.toString());
		LOG.debug("getTransferSupportFlag supportedKeyVal::::" + supportedKeyVal);
		if (StringUtils.isNotBlank(supportedKeyVal)) {
			isSupported = (supportedKeyVal.contains(supportType)) ? "1" : "0";
		} else {
			isSupported = "0";
		}

		return isSupported;
	}

}
