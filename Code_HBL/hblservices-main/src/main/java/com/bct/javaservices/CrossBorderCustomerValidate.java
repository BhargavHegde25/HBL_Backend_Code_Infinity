package com.bct.javaservices;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;
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
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;
import com.bct.eSewa.eSewaLimitCheck;
import com.bct.utilities.HBLCommonUtility;
import com.bct.utilities.IntegrationType;
import com.bct.utilities.NCHLNpixTransactionUAT;
import com.bct.utilities.TokenCacheManager;
import com.kony.dbputilities.util.ErrorCodeEnum;

public class CrossBorderCustomerValidate implements JavaService2 {
	private static final Logger logger = LogManager.getLogger(CrossBorderCustomerValidate.class);

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
			String PFX_FILE_PATH = paramHelper.getServerProperty("NPIX_CERT_PATH");
			String NPIX_AUTH_URL = paramHelper.getServerProperty("NPIX_AUTH_URL");
			String NPIX_URL = paramHelper.getServerProperty("NPIX_URL");
			String PFX_FILE_PASS = paramHelper.getServerProperty("PFX_FILE_PASS");

			String channel = "";
			String plat = "";
			String amount = request.getParameter("amount");
			String purpose = request.getParameter("purpose");
			String countryCode = request.getParameter("countryCode");
			String chargeAmount = request.getParameter("chargeAmount");
			String remarks = request.getParameter("remarks");
			String payeeName = request.getParameter("payeeName");
			String payeeRelationShip = request.getParameter("payeeRelationship");
			String payeeVPA = request.getParameter("payeeVPA");
			String payerName = request.getParameter("payerName");
			String payerVPA = request.getParameter("payerVPA");
			String payerAccType = request.getParameter("payerAccType");
			String payerAccNumber = request.getParameter("payerAccNumber");
			String currency = request.getParameter("currency");
			String latitude = request.getParameter("lat");
			String longitude = request.getParameter("long");

			logger.debug("latitude:" + latitude);
			logger.debug("longitude:" + longitude);

			String documentType = ArrangementsUtils.getUserAttributeFromIdentity(request, "IDType_id");
			String documentNumber = ArrangementsUtils.getUserAttributeFromIdentity(request, "IDValue");
			logger.debug("documentType##:" + documentType);
			logger.debug("documentNumber##:" + documentNumber);

			// String Organization_Id =
			// ArrangementsUtils.getUserAttributeFromIdentity(request, "Organization_Id");
			// String branchCode = Organization_Id.substring(Organization_Id.length() - 4);

			try {
				String reportingParams = request.getHeader("X-Kony-ReportingParams");
				if (StringUtils.isNotBlank(reportingParams)) {
					JSONObject reportingParamsJson = null;
					reportingParamsJson = new JSONObject(
							URLDecoder.decode(reportingParams, StandardCharsets.UTF_8.name()));
					if (null != reportingParamsJson) {
						// channel = reportingParamsJson.optString("chnl");
						channel = eSewaLimitCheck.getCurrentChannel(request);
						logger.debug("channel ##" + channel);
						plat = reportingParamsJson.optString("plat");
					}
					logger.debug("channel: ##" + channel);
					logger.debug("plat: ##" + plat);
				}
			} catch (Exception e) {
				logger.debug("Caught exception while Getting mfaname from reporting Params: ", e);
			}

			// String refreshToken =
			// NCHLNpixTransactionUAT.getRefreshToken(NPIX_AUTH_URL,base64Credentials,
			// username,
			// password);
			// logger.debug("refreshToken:" + refreshToken);

			// String accessToken =
			// NCHLNpixTransactionUAT.getAccessToken(NPIX_AUTH_URL,base64Credentials,
			// refreshToken);
			String accessToken = TokenCacheManager.getAccessToken(IntegrationType.CROSS_BORDER_CONSENT, NPIX_AUTH_URL,
					base64Credentials, username, password, request);
			logger.debug("accessToken:" + accessToken);

			String consentBody = HBLCommonUtility.getCustomerValidateReqPayload(channel, plat, amount, purpose,
					countryCode, chargeAmount, remarks, payeeName, payeeRelationShip, payeeVPA, payerName, payerVPA,
					payerAccType, payerAccNumber, currency, latitude, longitude, documentType, documentNumber);
			logger.debug("consentBody:" + consentBody);
			String consentResult = NCHLNpixTransactionUAT.validateCustomer(request, PFX_FILE_PASS, NPIX_URL,
					accessToken, PFX_FILE_PATH, payerVPA, consentBody);
			JSONObject jsonObject = new JSONObject(consentResult);
			logger.debug("Validate Customer Res:" + consentResult);

			// JSONArray lineItems = jsonObject.getJSONArray("responseData");
			if (jsonObject.getString("responseCode").equalsIgnoreCase("000")
					&& jsonObject.getString("responseMessage").equalsIgnoreCase("Success")) {
				logger.debug("orgRequestUniqueId:" + jsonObject.getString("orgRequestUniqueId"));
				logger.debug("endToEndId:" + jsonObject.getString("endToEndId"));
				logger.debug("amount:" + jsonObject.getBigDecimal("amount").toString());
				logger.debug("chargeAmount:" + jsonObject.getBigDecimal("chargeAmount").toString());

				result.setParam(new Param("orgRequestUniqueId", jsonObject.getString("orgRequestUniqueId")));
				result.setParam(new Param("endToEndId", jsonObject.getString("endToEndId")));
				result.setParam(new Param("amount", jsonObject.getBigDecimal("amount").toString()));
				result.setParam(new Param("chargeAmount", jsonObject.getBigDecimal("chargeAmount").toString()));
			} else {
				result.setParam(new Param("responseData", consentResult));
			}

			result.setParam(new Param("responseCode", jsonObject.getString("responseCode")));
			result.setParam(new Param("responseMessage", jsonObject.getString("responseMessage")));
			// result.setParam(new Param("responseData", consentResult));
			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));
		} catch (Exception e) {
			logger.error("Exception occured in CrossBorderCustomerValidate:::" + e.getMessage(), e);
			ErrorCodeEnum.ERR_10021.setErrorCode(result);
			result.addParam(new Param("dbpErrMsg", e.getLocalizedMessage()));
			result.addParam(new Param("success", "false"));
		}
		return result;
	}

}
