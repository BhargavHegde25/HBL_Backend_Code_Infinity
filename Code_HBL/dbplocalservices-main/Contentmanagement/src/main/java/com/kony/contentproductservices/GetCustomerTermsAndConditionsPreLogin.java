package com.kony.contentproductservices;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.contentproductservices.utils.ContentManagementUtils;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;
import com.temenos.infinity.api.commons.constants.FabricConstants;

public class GetCustomerTermsAndConditionsPreLogin implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private static final String DEFAULT_LANGUAGE_CODE = "en-US";
	private static final String DEFAULT_APP = "RETAIL_AND_BUSINESS_BANKING";

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		try {
			String termsAndConditionsCode = null;
			String language = null;
			String termAndConditionCode = null, languageCode = null, termAndConditonId = null, appId = null,
					leId = null;
			String termNConditionAppsId = null, consentTypeId = null, retentionPeriod = null, consentMandatory = null;
			boolean status = false;
			Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
			language = inputParams.get("languageCode");
			termsAndConditionsCode = inputParams.get("termsAndConditionsCode");
			if (StringUtils.isBlank(termsAndConditionsCode)) {
				ErrorCodeEnum.ERR_10189.setErrorCode(result);
				return result;
			}
			Set<String> scenarios = new HashSet<>();
			scenarios.add(DBPUtilitiesConstants.TNC_ENROLL);
			scenarios.add(DBPUtilitiesConstants.TNC_COMMON);
			scenarios.add(DBPUtilitiesConstants.TNC_HAMBURGER);
			scenarios.add(DBPUtilitiesConstants.TNC_ONBOARDING);
			scenarios.add(DBPUtilitiesConstants.TNC_BUSINESSENROLL);
			scenarios.add(DBPUtilitiesConstants.TNC_ONBOARDING_OFAC);
			scenarios.add(DBPUtilitiesConstants.TNC_ONBOARDING_ESIGN_AGREEMENT);
			scenarios.add(DBPUtilitiesConstants.TNC_ONBOARDING_GOOGLEMAPS_DISCLOSURE);
			scenarios.add(DBPUtilitiesConstants.TNC_ONBOARDING_FUNDING_OTP_DISCLAIMER);
			scenarios.add(DBPUtilitiesConstants.TNC_ONBOARDING_FUNDING_DISCLAIMER);
			scenarios.add(DBPUtilitiesConstants.TNC_ONBOARDING_FUNDING_ACKNOWLEDGEMENT_DISCLAIMER);
			scenarios.add(DBPUtilitiesConstants.TNC_ONBOARDING_IDSCAN_DISCLAIMER);
			scenarios.add(DBPUtilitiesConstants.TNC_ONBOARDING_PRODUCTDASHBOARD_DISCLAIMER);
			scenarios.add(DBPUtilitiesConstants.TNC_ONBOARDING_UPDATE_PASSWORD_RULES);
			scenarios.add(DBPUtilitiesConstants.TNC_ONBOARDING_COAPPLICANT_SECTION);
        	scenarios.add(DBPUtilitiesConstants.TNC_THIRDPARTY_AUTH);

			for (String scenario : scenarios) {
				if (termsAndConditionsCode.equals(scenario)) {
					status = true;
					break;
				}
			}

			if (!status) {
				ErrorCodeEnum.ERR_10189.setErrorCode(result);
				return result;
			}

			String leid = dcRequest.getParameter("legalEntityId");
			if (StringUtils.isNotBlank(leid)) {
				inputParams.put("legalEntityId", leid);
			}else {
				leid = EnvironmentConfigurationsHandler.getServerAppProperty("BRANCH_ID_REFERENCE");
			}

			appId = HelperMethods.getAppId(dcRequest);
			if (StringUtils.isBlank(appId)) {
				appId = DEFAULT_APP;
			}

			try {
				result = ContentManagementUtils.getTermsAndConditions(termsAndConditionsCode, leid, appId, language);
			}catch(Exception e){
				alert.prepareError("Caught exception while fetching terms and conditions: ",  e.toString()).log();
			}

			if (HelperMethods.hasDBPErrorMSG(result)) {
				ErrorCodeEnum.ERR_10190.setErrorCode(result);
				return result;
			}

		} catch (Exception e) {
			alert.prepareError("Caught exception while fetching terms and conditions: ", e.toString()).log();
		}
		return result;
	}
}
