package com.bct.javaservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.bct.utilities.HBLCommonUtility;
import com.bct.utilities.IntegrationType;
import com.bct.utilities.TokenCacheManager;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.neovisionaries.i18n.CountryCode;

public class CrossBorderConsentCreate implements JavaService2 {
	private static final Logger logger = LogManager.getLogger(CrossBorderConsentCreate.class);

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

			String vpaId = request.getParameter("vpaId");
			String consent = request.getParameter("consent");
			String userName = request.getParameter("userName");
			String fullName = request.getParameter("fullName");

			String documentType = request.getParameter("IDType");
			String documentNumber = request.getParameter("IDValue");
			String nationalityId = request.getParameter("nationalityId");
			String direction = request.getParameter("direction");

			// String customerId = HBLCommonUtility.getCustomerIDFromUsername(request,
			// userName);
			// String id = HBLCommonUtility.getIdfromCustomerAccTable(customerId, vpaId);

			// String documentType = ArrangementsUtils.getUserAttributeFromIdentity(request,
			// "IDType_id");
			// String documentNumber =
			// ArrangementsUtils.getUserAttributeFromIdentity(request, "IDValue");
			// String nationalityId =
			// ArrangementsUtils.getUserAttributeFromIdentity(request, "nationalityId");

			// String documentType = "PAN";
			// String documentNumber = "12345";
			// String nationalityId = "NP";

			logger.debug("documentType##:" + documentType);
			logger.debug("documentNumber##:" + documentNumber);
			logger.debug("nationalityId##:" + nationalityId);
			logger.debug("direction##:" + direction);

			CountryCode code = CountryCode.getByCode(nationalityId);
			logger.debug("ISO 3166-1 numeric code = " + code.getNumeric());
			String countryCode = Integer.toString(code.getNumeric());
			// String countryCode = "356";
			logger.debug("countryCode##:" + countryCode);

			// String refreshToken =
			// com.bct.utilities.NCHLNpixTransactionUAT.getRefreshToken(NPIX_AUTH_URL,base64Credentials,
			// username,
			// password);
			// logger.debug("refreshToken:" + refreshToken);

			// String accessToken =
			// com.bct.utilities.NCHLNpixTransactionUAT.getAccessToken(NPIX_AUTH_URL,
			// base64Credentials, refreshToken);
			String accessToken = TokenCacheManager.getAccessToken(IntegrationType.CROSS_BORDER_CONSENT, NPIX_AUTH_URL,
					base64Credentials, username, password, request);
			logger.debug("accessToken:" + accessToken);

			// String consentBody = "{\"issuedDate\":\"10/7/2005 12:00:00
			// AM\",\"bankCode\":\"0701\",\"documentType\":\"CTZN\",\"documentNumber\":\"27280\",\"fullName\":\"SABINA
			// JOSHI\",\"instrument\":\"MB\",\"consent\":\"APPROVED\",\"vpaId\":\"92028250010\",\"nationality\":\"IN\",\"countryCode\":\"356\",\"uniqueTransactingId\":\"15482559\",\"issuedPlace\":\"BHAKTAPUR\",\"participantCode\":\"HBL\",\"participantService\":\"REQ_CONSENT\",\"direction\":\"OUTWARD\"}";
			String consentBody = HBLCommonUtility.getConsentCreateReqBody(consent, vpaId, fullName, documentType,
					documentNumber, nationalityId, countryCode, direction);
			String consentResult = com.bct.utilities.NCHLNpixTransactionUAT.createConsent(request, PFX_FILE_PASS,
					NPIX_URL, accessToken, PFX_FILE_PATH, vpaId, consentBody);
			JSONObject jsonObject = new JSONObject(consentResult);
			logger.debug("Create consent Res:" + consentResult);

			// JSONArray lineItems = jsonObject.getJSONArray("responseData");

			String responseCode = jsonObject.getString("responseCode").trim();
			String responseMessage = jsonObject.getString("responseMessage").trim();
			logger.debug("responseCode ##" + responseCode);
			logger.debug("responseMessage ##" + responseMessage);

			if (responseCode.equalsIgnoreCase("000") && responseMessage.equalsIgnoreCase("Success")) {
				logger.debug("accountID from request ##:" + request.getParameter("accountID"));
				logger.debug("internationalInwardConsent from request ##:"
						+ request.getParameter("internationalInwardConsent"));
				logger.debug("internationalOutwardConsent from request ##:"
						+ request.getParameter("internationalOutwardConsent"));
				logger.debug(
						"domesticOutwardConsent from request ##:" + request.getParameter("domesticOutwardConsent"));
				logger.debug("domesticInwardConsent from request ##:" + request.getParameter("domesticInwardConsent"));
				HashMap<String, Object> headers = new HashMap<String, Object>();
				HashMap<String, Object> inputmap = new HashMap<String, Object>();
				String accountID = "";
				String internationalInwardConsent = "";
				String internationalOutwardConsent = "";
				String domesticOutwardConsent = "";
				String domesticInwardConsent = "";

				accountID = (!StringUtils.isBlank(request.getParameter("accountID")))
						? request.getParameter("accountID")
						: " ";
				internationalInwardConsent = (!StringUtils.isBlank(request.getParameter("internationalInwardConsent")))
						? request.getParameter("internationalInwardConsent")
						: " ";
				internationalOutwardConsent = (!StringUtils
						.isBlank(request.getParameter("internationalOutwardConsent")))
								? request.getParameter("internationalOutwardConsent")
								: " ";
				domesticOutwardConsent = (!StringUtils.isBlank(request.getParameter("domesticOutwardConsent")))
						? request.getParameter("domesticOutwardConsent")
						: " ";
				domesticInwardConsent = (!StringUtils.isBlank(request.getParameter("domesticInwardConsent")))
						? request.getParameter("domesticInwardConsent")
						: " ";

				internationalInwardConsent = internationalInwardConsent.equalsIgnoreCase("APPROVED") ? "YES"
						: internationalInwardConsent.equalsIgnoreCase("DECLINED") ? "NO" : "PENDING";
				internationalOutwardConsent = internationalOutwardConsent.equalsIgnoreCase("APPROVED") ? "YES"
						: internationalOutwardConsent.equalsIgnoreCase("DECLINED") ? "NO" : "PENDING";
				domesticOutwardConsent = domesticOutwardConsent.equalsIgnoreCase("APPROVED") ? "YES"
						: domesticOutwardConsent.equalsIgnoreCase("DECLINED") ? "NO" : "PENDING";
				domesticInwardConsent = domesticInwardConsent.equalsIgnoreCase("APPROVED") ? "YES"
						: domesticInwardConsent.equalsIgnoreCase("DECLINED") ? "NO" : "PENDING";

				inputmap.put("accountID", accountID);
				inputmap.put("internationalInwardConsent", internationalInwardConsent);
				inputmap.put("internationalOutwardConsent", internationalOutwardConsent);
				inputmap.put("domesticOutwardConsent", domesticOutwardConsent);
				inputmap.put("domesticInwardConsent", domesticInwardConsent);
				Result res = HBLCommonUtility.updateTransactConsentStatus(inputmap, headers);

				logger.debug("Update consent staus at transact:" + ResultToJSON.convert(res));

				if (res.getParamValueByName("status").equalsIgnoreCase("success")) {
					result.setParam(new Param("status", "status updated successfully at Core"));
				} else {
					result.setParam(new Param("status", "status updated failed at Core"));
				}

			} else if (responseCode.equalsIgnoreCase("E002")
					&& responseMessage.equalsIgnoreCase("Duplicate consent.")) {
				Result res = new Result();
				logger.debug("Calling update service in create consent ##:");
				HashMap<String, Object> inputParams = new HashMap<>();
				res = CommonUtils.callIntegrationService(request, inputParams, request.getHeaderMap(),
						"CrossBorderPayments", "updateConsent", true);

				logger.debug("Calling update service in create consent result ##:" + ResultToJSON.convert(res));

				result.setParam(new Param("responseCode", res.getParamValueByName("responseCode")));
				result.setParam(new Param("responseMessage", res.getParamValueByName("responseMessage")));
				result.setParam(new Param("responseData", res.getParamValueByName("responseData")));
				result.setParam(new Param("status", res.getParamValueByName("status")));
				result.setParam(new Param("opstatus", "0"));
				result.setParam(new Param("httpStatusCode", "200"));

				return result;
			}
			// HBLCommonUtility.updateConsentStatus(request, vpaId, consent, id);
			result.setParam(new Param("responseCode", responseCode));
			result.setParam(new Param("responseMessage", responseMessage));
			result.setParam(new Param("responseData", consentResult));
			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));
			// result.setParam(new Param("responseErrors",
			// jsonObject.getString("responseErrors")));
		} catch (Exception e) {
			logger.error("Exception occured in CrossBorderConsentCreate:::" + e.getMessage(), e);
			ErrorCodeEnum.ERR_10021.setErrorCode(result);
			result.addParam(new Param("dbpErrMsg", e.getLocalizedMessage()));
			result.addParam(new Param("success", "false"));
		}
		return result;
	}
	
}
