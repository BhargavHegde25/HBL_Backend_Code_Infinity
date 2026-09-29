package com.infinity.dbx.temenos.user;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.infinity.dbx.dbp.jwt.auth.AuthConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.utils.DTOConstants;
import com.temenos.dbx.product.utils.HTTPOperations;

public class GetAssetLiabilities implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {

		Result result = new Result();
		try {
			Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
			String coreCustomerID = inputParams.get(DTOConstants.CORECUSTOMERID);
			if (StringUtils.isBlank(coreCustomerID)) {
				ErrorCodeEnum.ERR_12458.setErrorCode(result, "coreCustomerID is empty");
				return result;
			}
			Map<String, Object> headers = dcRequest.getHeaderMap();
			headers = HelperMethods.addMSJWTAuthHeader(dcRequest, headers, AuthConstants.PRE_LOGIN_FLOW);

			String serviceURL = UserConstants.ASSET_LIABILITIES_SERVICE_URL.trim().replace("{party_id}", coreCustomerID);

			JSONArray assetLiabArray = new JSONArray();
			String dueDiligenceURL = URLFinder.getServerRuntimeProperty(UserConstants.DUE_DILIGENCE_MS_BASE_URL)
					+ serviceURL;
			DBXResult dbxResult = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET, dueDiligenceURL, null,
					headers);
			try {
				if (dbxResult.getResponse() != null) {
					assetLiabArray = new JSONObject((String) dbxResult.getResponse()).getJSONArray("assetLiabDetails");
				}
			} catch (Exception e) {
				alert.prepareError("Caught exception while fetching dueDiligence Data for " + serviceURL + ": ", e).log();
				dbxResult.setDbpErrMsg(dbxResult.getDbpErrMsg());
			}
			JSONObject assetLiabDetails = new JSONObject();
			assetLiabDetails.put("assetLiabDetails", assetLiabArray);
			
			result = JSONToResult.convert(assetLiabDetails.toString());
			
		} catch (Exception e) {
			ErrorCodeEnum.ERR_29061.setErrorCode(result);
		}
		return result;
	}
}