package com.bct.javaservices;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.bct.utilities.HBLCommonUtility;
import com.bct.utilities.IntegrationType;
import com.bct.utilities.TokenCacheManager;
import com.kony.dbputilities.util.ErrorCodeEnum;

public class GetCrossBorderCheckLimit implements JavaService2 {
	private static final Logger logger = LogManager.getLogger(GetCrossBorderCheckLimit.class);

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {

		Result result = new Result();
		try {
			ServicesManager sm = request.getServicesManager();
			ConfigurableParametersHelper paramHelper = sm.getConfigurableParametersHelper();
			String base64Credentials = paramHelper.getServerProperty("CROSSBORDER_BASE64_CRED");
			String username = paramHelper.getServerProperty("CROSSBORDER_AUTH_USER");
			String password = paramHelper.getServerProperty("CROSSBORDER_AUTH_PSWD");
			String PFX_FILE_PATH = paramHelper.getServerProperty("CROSSBORDER_PFX_FILE_PATH");
			String NPIX_AUTH_URL = paramHelper.getServerProperty("NPIX_AUTH_URL");
			String NPIX_URL = paramHelper.getServerProperty("NPIX_URL");
			String PFX_FILE_PASS = paramHelper.getServerProperty("PFX_FILE_PASS");

			String amount = request.getParameter("amount");
			String vpa = request.getParameter("vpa");

			// String refreshToken =
			// com.bct.utilities.NCHLNpixTransactionUAT.getRefreshToken(NPIX_AUTH_URL,base64Credentials,
			// username,
			// password);
			// logger.debug("refreshToken:" + refreshToken);

			// String accessToken =
			// com.bct.utilities.NCHLNpixTransactionUAT.getAccessToken(NPIX_AUTH_URL,base64Credentials,
			// refreshToken);
			String accessToken = TokenCacheManager.getAccessToken(IntegrationType.CROSS_BORDER_CONSENT, NPIX_AUTH_URL,
					base64Credentials, username, password, request);
			logger.debug("accessToken:" + accessToken);

			// String limtRawBody =
			// "{\"amount\":\"200.00\",\"vpa\":\"86085045802\",\"service\":\"XP2P_UPI\"}";
			String limtRawBody = HBLCommonUtility.getLimitReqBody(amount, vpa);
			String checkLimit = com.bct.utilities.NCHLNpixTransactionUAT.limitCheck(request, PFX_FILE_PASS, NPIX_URL,
					accessToken, PFX_FILE_PATH, vpa, limtRawBody);
			JSONObject jsonObject = new JSONObject(checkLimit);
			logger.debug("relationship Res:" + checkLimit);

			// JSONArray lineItems = jsonObject.getJSONArray("responseData");

			result.setParam(new Param("responseCode", jsonObject.getString("responseCode")));
			result.setParam(new Param("responseMessage", jsonObject.getString("responseMessage")));
			result.setParam(new Param("responseData", checkLimit));
			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));
		} catch (Exception e) {
			logger.error("Exception occured in GetCrossBorderCheckLimit:::" + e.getMessage(), e);
			ErrorCodeEnum.ERR_10021.setErrorCode(result);
			result.addParam(new Param("dbpErrMsg", e.getLocalizedMessage()));
			result.addParam(new Param("success", "false"));
		}
		return result;
	}

}
