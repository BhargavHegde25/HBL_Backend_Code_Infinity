package com.kony.adminconsole.commons.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.konylabs.middleware.api.processor.IdentityHandler;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.transact.tokenmanager.jwt.TokenGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kony.adminconsole.commons.handler.EnvironmentConfigurationsHandler;

public class AuthenticationC360 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	
	public static final String DEFAULT_ISSUER_FABRIC = "fabric";
	public static final String PARAM_USERNAME = "userName";
	public static final String PARAM_DBX_USER_ID = "userId";
	
	// JWT Authorization
	public static final String BACKENDCERTNAME = "T24";
	public static final String HOLDINGS_BACKENDCERTNAME = "HOLDINGSMS";
	public static final String PRE_AUTHENTICATION_USER_NAME = "T24";
	public static final String PRE_AUTHENTICATION_ROLE_ID = "INFINITY.RETAIL";
	public static final String DMS_DOCUMENT_OWNER_ROLE = "DocumentOwner";
	public static final String PRE_AUTHENTICATION_USER_ID = "0001";
	public static final String HOLDINGS_AUTHENTICATION_USER_NAME = "FABRICUSER";
	public static final String TOKEN_ISSUER = "Fabric";
	public static final String ROLE_ID = "ADMIN";
	public static final String AUDIENCE_TYPE_DOCMS = "documents";
			
	// Login Flows
	public static final String FLOW_TYPE = "flowType";
	public static final String PRE_LOGIN_FLOW = "PreLogin";
	public static final String LOGIN_FLOW = "Login";
	public static final String POST_LOGIN_FLOW = "PostLogin";

	public static String getAuthToken(DataControllerRequest dcRequest) throws Exception {
		Map<String, Object> authParams = new HashMap<>();
		String authToken = "";
		try {
			Long tokenValidity = Long.parseLong(EnvironmentConfigurationsHandler
					.getServerAppPropertyValue("MS_T24_AUTH_TOKEN_VALIDITY", dcRequest));
			authParams.put("productType", FabricConstants.PRODUCT_TYPE_MS);
			String flowType = dcRequest.getParameter(FLOW_TYPE);
			switch (flowType) {
			case PRE_LOGIN_FLOW:
				authToken = TokenGenerator.generateAuthToken(BACKENDCERTNAME,
						PRE_AUTHENTICATION_USER_NAME, PRE_AUTHENTICATION_USER_ID,
						PRE_AUTHENTICATION_ROLE_ID, tokenValidity, true, authParams);
				break;

			case POST_LOGIN_FLOW:
				Map<String, Object> userInfo = FetchUserNameAndUserId(dcRequest);
				authToken = TokenGenerator.generateAuthToken(BACKENDCERTNAME,
						userInfo.get(PARAM_USERNAME).toString(), userInfo.get(PARAM_DBX_USER_ID).toString(),
						PRE_AUTHENTICATION_ROLE_ID, tokenValidity, true, authParams);
				break;
			}
		} catch (Exception e) {
			alert.prepareError("Error in AuthenticationCorporate : getAuthToken" + e).log();
		}
		return authToken;
	}

	public static String getDMSAuthToken(DataControllerRequest dcRequest, String userId){
		Map<String, Object> authParams = new HashMap<>();
		String authToken = "";
		try {
			Long tokenValidity = Long.parseLong(EnvironmentConfigurationsHandler
					.getServerAppPropertyValue("MS_T24_AUTH_TOKEN_VALIDITY", dcRequest));
			authParams.put("productType", FabricConstants.PRODUCT_TYPE_DOCMS);
			authParams.put("defaultIssuer", DEFAULT_ISSUER_FABRIC);
			authParams.put("audienceType", AUDIENCE_TYPE_DOCMS);
			if (StringUtils.isBlank(userId)) {
				userId = EnvironmentConfigurationsHandler.getServerAppPropertyValue("MS_T24_AUTH_TOKEN_VALIDITY",
						dcRequest);
			}
			return TokenGenerator.generateAuthToken(BACKENDCERTNAME,
					PRE_AUTHENTICATION_USER_NAME, userId, DMS_DOCUMENT_OWNER_ROLE,
					tokenValidity, true, authParams);
		} catch (Exception e) {
			alert.prepareError("Error in AuthenticationCorporate : getDMSAuthToken" + e).log();
		}
		return authToken;
	}

	public static String getT24AuthToken(DataControllerRequest dcRequest) {
		Map<String, Object> authParams = new HashMap<>();
		String authToken = "";
		try {
			Long tokenValidity = Long.parseLong(EnvironmentConfigurationsHandler
					.getServerAppPropertyValue("MS_T24_AUTH_TOKEN_VALIDITY", dcRequest));
			authParams.put("productType", FabricConstants.PRODUCT_TYPE_T24);
			authToken = TokenGenerator.generateAuthToken(BACKENDCERTNAME,
					PRE_AUTHENTICATION_USER_NAME, PRE_AUTHENTICATION_USER_ID,
					PRE_AUTHENTICATION_ROLE_ID, tokenValidity, true, authParams);

		} catch (Exception e) {
			alert.prepareError("Error in AuthenticationCorporate : getT24AuthToken" + e).log();
		}
		return authToken;
	}
	
	private static Map<String, Object> FetchUserNameAndUserId(DataControllerRequest dcRequest) {
		Map<String, Object> finalResponse = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			IdentityHandler identityHandler = dcRequest.getServicesManager().getIdentityHandler();
			Map<String, Object> userAttributesInfo = identityHandler.getUserAttributes();
			String userInfo = (String) userAttributesInfo.get("_provider_profile");
			Map<String, Object> userAttributes = mapper.readValue(userInfo, new TypeReference<Map<String, Object>>() {
			});
			
			if (userAttributes != null && userAttributes.size() > 0) {
				String id = userAttributes.get("userId").toString();
				finalResponse.put(PARAM_USERNAME, PRE_AUTHENTICATION_USER_NAME);
				finalResponse.put(PARAM_DBX_USER_ID, id);
			}
		} catch (Exception e) {
			alert.prepareError("Error in AuthenticationCorporate : FetchUserNameAndUserId" + e).log();
		}
		return finalResponse;
	}

}
