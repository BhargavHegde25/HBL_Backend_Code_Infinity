package com.kony.contentproductservices.preprocessor;

import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.kony.contentproductservices.utils.ContentManagementConstants;
import com.kony.contentproductservices.utils.ContentManagementUtils;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.TokenUtils;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.processor.IdentityHandler;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class TermsNConditionsTokenPreprocessor implements DataPreProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public boolean execute(HashMap arg0, DataControllerRequest dcRequest, DataControllerResponse dcResponse,
			Result result) throws Exception {
		Log4j2Configurator.getInstance();
		try {
			String authToken = TokenUtils.getCommonMSAuthToken();
			diagnostic.prepareInfo("### authToken :" + authToken).log();
			addCompanyIDToHeaders(dcRequest);
			dcRequest.addRequestParam_("Authorization", authToken);
			setCloudAuthenticationHeaders(dcRequest, ContentManagementConstants.CONSENT_DEPLOYMENT_PLATFORM,
					ContentManagementConstants.CONSENT_AUTHORIZATION_KEY);
			return true;
		} catch (Exception e) {
			alert.prepareError("Exception in generating JWT authToken for Consent Management", e).log();
			return false;
		}
	}

	public static void setCloudAuthenticationHeaders(DataControllerRequest request, String deployementPlatform,
			String autorizationKey) {
		String clouddeploymentType = EnvironmentConfigurationsHandler.getServerProperty(deployementPlatform);
		String autorizationValue = EnvironmentConfigurationsHandler.getServerProperty(autorizationKey);
		diagnostic.prepareDebug("clouddeploymentType : " + clouddeploymentType).log();
		diagnostic.prepareDebug("autorizationValue : " + autorizationValue).log();

		if (ContentManagementConstants.AWS.equalsIgnoreCase(clouddeploymentType)) {
			request.addRequestParam_(ContentManagementConstants.X_API_KEY, autorizationValue);
		} else if (ContentManagementConstants.AZURE.equalsIgnoreCase(clouddeploymentType)) {
			request.addRequestParam_(ContentManagementConstants.X_FUNCTIONS_KEY, autorizationValue);
		}
	}

	public static void addCompanyIDToHeaders(DataControllerRequest request) {
		String legalEntityId;
		if (request.getParameter("legalEntityId") != null) {
			legalEntityId = request.getParameter("legalEntityId");
		} else {
			legalEntityId = getLegalEntityId(request);
		}
		Map<String, Object> headerMap = request.getHeaderMap();
		if (headerMap != null) {
			headerMap.put("companyId", legalEntityId);
		}
	}

	public static String getLegalEntityId(DataControllerRequest request) {
		try {
			if (request == null) {
				return "";
			}
			ServicesManager servicesManager = request.getServicesManager();
			if (servicesManager == null) {
				return "";
			}
			IdentityHandler identityHandler = servicesManager.getIdentityHandler();
			if (identityHandler == null) {
				return "";
			}
			Map<String, Object> securityAttributes = identityHandler.getSecurityAttributes();
			if (securityAttributes == null || securityAttributes.isEmpty()) {
				return "";
			}
			JSONObject obj = ContentManagementUtils
					.getStringAsJSONObject(securityAttributes.get("raw_response").toString());
			Object loggedInUserLegalEntityID = null;
			if (obj != null && obj.length() > 0) {
				JSONObject user_attributes = ContentManagementUtils
						.getStringAsJSONObject(obj.optString("user_attributes"));
				if (user_attributes != null && user_attributes.length() > 0) {
					loggedInUserLegalEntityID = user_attributes.optString("legalEntityId");
				}
			}
			if (loggedInUserLegalEntityID != null && StringUtils.isNotBlank(loggedInUserLegalEntityID.toString())) {
				diagnostic.prepareDebug("Adding legalEntityId from user attributes").log();
				return loggedInUserLegalEntityID.toString();
			}
			Map<String, Object> userAttributes = identityHandler.getUserAttributes();
			if (userAttributes == null) {
				return "";
			}
			loggedInUserLegalEntityID = userAttributes.get("legalEntityId");
			if (loggedInUserLegalEntityID != null && StringUtils.isNotBlank(loggedInUserLegalEntityID.toString())) {
				diagnostic.prepareDebug("Adding legalEntityId from user attributes").log();
				return loggedInUserLegalEntityID.toString();
			}
			diagnostic.prepareDebug("Unable to get legalEntityId from user attributes, defaulting to blank").log();
		} catch (Exception e) {
			alert.prepareError("Error while fetching legalEntityId from user attributes").log();
		}
		return "";
	}

}
