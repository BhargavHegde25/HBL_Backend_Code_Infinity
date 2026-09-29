package com.bct.preprocessor;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class QRConnectIPSAuthorizationPreprocessor extends TemenosBasePreProcessor {
	private static final Logger LOG = LogManager.getLogger(QRConnectIPSAuthorizationPreprocessor.class);
	private static final String contentType = "application/x-www-form-urlencoded";
	private static final String connectIPSserviceId = "HBLQRNepalPay";
	private String cipsAuthorization = "";
	private String cips_apiuser = "";
	private String cips_apipw = "";

	@SuppressWarnings("rawtypes")
	@Override
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		cipsAuthorization = EnvironmentConfigurationsHandler.getServerProperty("QR_CONNECTIPS_AUTHORIZATION");
		cips_apiuser = EnvironmentConfigurationsHandler.getServerProperty("QR_CONNECTIPS_API_USER");
		cips_apipw = EnvironmentConfigurationsHandler.getServerProperty("QR_CONNECTIPS_API_PASSWORD");
		if (StringUtils.isBlank(cipsAuthorization) || StringUtils.isBlank(cips_apiuser)
				|| StringUtils.isBlank(cips_apipw)) {
			result.addParam("errMsg", "Invalid CIPS details");
			return false;
		}
		// String accessToken = getAccessToken(request);
		String accessToken = (String) MemoryManager.getDataFromCache(request, "QR_CIPS_ACCESS_TOKEN");
		LOG.debug("HBL::QRConnectIPSAuthorizationPreprocessor:: access_token:" + accessToken);
		if (StringUtils.isBlank(accessToken)) {
			result.addParam("errMsg", "failed to generate CIPS accessToken");
			return false;
		}
//		accessToken = "Bearer " + accessToken;
//		LOG.debug("HBL::QRConnectIPSAuthorizationPreprocessor:: access_token1:" + accessToken);
		request.getHeaderMap().put("Content-Type", "application/json");
		request.getHeaderMap().put(TemenosConstants.PARAM_AUTHORIZATION, accessToken);
		request.addRequestParam_(TemenosConstants.PARAM_AUTHORIZATION, accessToken);
		return true;
	}

	public String getAccessToken(DataControllerRequest request) {
		Map<String, Object> inputmap = new HashMap<String, Object>();
		Map<String, Object> inputmap1 = new HashMap<String, Object>();
		String access_token = "";
		StringBuilder sb = new StringBuilder();
		sb.append("username").append("=").append(cips_apiuser);
		sb.append("&");
		sb.append("password").append("=").append(cips_apipw);
		sb.append("&");
		sb.append("grant_type").append("=").append("password");
		inputmap.put("filter", sb.toString());
		Map<String, Object> headermap = new HashMap<String, Object>();
		headermap.put("Content-Type", contentType);
		headermap.put("Authorization", cipsAuthorization);
		try {
			Result res = callInternalServiceAndGetResult(connectIPSserviceId, "getAccessToken", inputmap, headermap);
			if (res.getHttpStatusCodeParamValue().equals("200")) {
				String refreshToken = res.getParamValueByName("refresh_token");
				LOG.debug("HBL::QRConnectIPSAuthorizationPreprocessor: callInternalServiceAndGetString: refreshToken:"
						+ refreshToken);
				if (StringUtils.isNotBlank(refreshToken)) {
					sb = new StringBuilder();
					sb.append("grant_type").append("=").append("refresh_token");
					sb.append("&");
					sb.append("refresh_token").append("=").append(refreshToken);
					inputmap1.put("filter", sb.toString());
					Result response = callInternalServiceAndGetResult(connectIPSserviceId, "getAccessToken", inputmap1,
							headermap);
					if (response.getHttpStatusCodeParamValue().equals("200")) {
						access_token = response.getParamValueByName("access_token");
						LOG.debug(
								"HBL::QRConnectIPSAuthorizationPreprocessor: callInternalServiceAndGetString: access_token:"
										+ access_token);
					}
				}
			}
		} catch (Exception e) {
			LOG.debug("Error occured in getRefreshToken", e);
		}
		return access_token;
	}

	private static Result callInternalServiceAndGetResult(String serviceid, String operationid,
			Map<String, Object> inputmap, Map<String, Object> headers) throws DBPApplicationException {
		LOG.debug("HBL::QRConnectIPSAuthorizationPreprocessor: callInternalServiceAndGetString: inputmap:"
				+ inputmap.toString());
		LOG.debug("HBL::QRConnectIPSAuthorizationPreprocessor: callInternalServiceAndGetString: headers:"
				+ headers.toString());
		Result res = DBPServiceExecutorBuilder.builder().withOperationId(operationid).withRequestParameters(inputmap)
				.withServiceId(serviceid).withRequestHeaders(headers).build().getResult();
		LOG.debug("HBL::QRConnectIPSAuthorizationPreprocessor: callInternalServiceAndGetString: response:"
				+ res.getHttpStatusCodeParamValue());

		return res;
	}

}
