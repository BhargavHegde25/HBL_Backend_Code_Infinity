package com.kony.contentproductservices;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.kony.contentproductservices.utils.ContentManagementConstants;
import com.kony.contentproductservices.utils.ContentManagementUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.UserAgentUtil;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class CreateCustomerTNCForLogin implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private static final String DEFAULT_LANGUAGE_CODE = "en-US";
	private static final String DEFAULT_APP = "RETAIL_AND_BUSINESS_BANKING";

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		String termsAndConditionsCode = null;
		String language = DBPUtilitiesConstants.TNC_DEFAULT_LANGUAGE;
		String customerId = HelperMethods.getCustomerIdFromSession(dcRequest);
		Result result = new Result();
		String appId = null;
		String globalContentId = null;
		Result termsAndConditionsFromAdmin = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		termsAndConditionsCode = dcRequest.getParameter("termsAndConditionsCode");
		
		if(StringUtils.isBlank(termsAndConditionsCode)) {
			termsAndConditionsCode = DBPUtilitiesConstants.TNC_LOGIN;
		}
		
		String globalTNCContentId = null;

		String leid = dcRequest.getParameter("legalEntityId");
		if (StringUtils.isBlank(leid)) {
			leid = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(dcRequest);
		}

		if (StringUtils.isBlank(leid)) {
			ErrorCodeEnum.ERR_29040.setErrorCode(result);
			return result;
		}

		if (StringUtils.isNotBlank(inputParams.get("languageCode"))) {
			language = inputParams.get("languageCode");
		}

		appId = HelperMethods.getAppId(dcRequest);
		if (StringUtils.isBlank(appId)) {
			appId = DEFAULT_APP;
		}

		inputParams.put("termsAndConditionsCode", termsAndConditionsCode);
		inputParams.put("languageCode", language);
		inputParams.put("appId", HelperMethods.getAppId(dcRequest));
		inputParams.put("legalEntityId", leid);

		String consentBackend = EnvironmentConfigurationsHandler.getServerAppProperty("CONSENT_BACKEND");
		if (!ContentManagementConstants.CONSENT_BACKEND_DBXDB.equalsIgnoreCase(consentBackend)) {
			if (null == dcRequest.getSession().getAttribute("globalTNCContentId")) {
				try {
					termsAndConditionsFromAdmin = ContentManagementUtils.getTermsAndConditions(termsAndConditionsCode,
							leid, appId, language);
					globalContentId = termsAndConditionsFromAdmin.getParamValueByName("contentId");
				} catch (Exception e) {
					alert.prepareError("Caught exception while fetching terms and conditions: ", e.toString()).log();
				}
				if (HelperMethods.hasDBPErrorMSG(termsAndConditionsFromAdmin)) {
					ErrorCodeEnum.ERR_10185.setErrorCode(result);
					return result;
				}
			} else {
				globalTNCContentId = dcRequest.getSession().getAttribute("globalTNCContentId").toString();
				globalContentId = globalTNCContentId;
			}

			Map<String, Object> params = new HashMap<>();
			Map<String, String> header = HelperMethods.getHeaders(dcRequest);

			if (!"EarlyPayOffSimulation_TnC".equalsIgnoreCase(termsAndConditionsCode)) {
				params.clear();
				params.put("partyId", customerId);
				header.put("companyid", leid);

				Result partyConsentDetails = ServiceCallHelper.invokeServiceAndGetResult(params, header,
						URLConstants.GET_PARTY_CONSENT_DETAILS, "");

				if (partyConsentDetails != null) {
					Dataset ds = partyConsentDetails.getDatasetById("partyConsentDetails");
					if (ds != null && ds.getAllRecords().size() > 0) {
						if (globalTNCContentId == null) {
							termsAndConditionsFromAdmin = ContentManagementUtils
									.getTermsAndConditions(termsAndConditionsCode, leid, appId, language);
							globalContentId = termsAndConditionsFromAdmin.getParamValueByName("contentId");
						} else {
							globalContentId = globalTNCContentId;
						}
						for (Record record : ds.getAllRecords()) {
							String contentId = record.getParamValueByName("termsNConditionContentId");
							String consentLeId = HelperMethods.getFieldValue(record, "legalEntityId");
							diagnostic
									.prepareDebug(
											" consent legal entity id for the service excution is : " + consentLeId)
									.log();
							if (contentId != null && globalContentId != null
									&& contentId.equalsIgnoreCase(globalContentId)
									&& StringUtils.isNotBlank(consentLeId) && consentLeId.equalsIgnoreCase(leid)) {
								result.addParam(new Param("alreadySigned", "true", DBPUtilitiesConstants.STRING_TYPE));
								return result;
							}
						}
					}
				}
			}

			params.clear();
			params.put("masterConsentId", DBPUtilitiesConstants.CONSENT_MASTER_ID);
			params.put("bankName", "Infinity Bank");
			params.put("externalUserId", customerId);
			params.put("retentionPeriod", "30");
			params.put("status", "ACTIVE");
			params.put("backendIdentifier", customerId);
			params.put("channelId", "Online");
			params.put("termsNConditionContentId", globalContentId);
			params.put("consentGiven", "true");
			params.put("partyId", customerId);

			diagnostic.prepareDebug("create party consent details params " + params).log();
			HelperMethods.removeNullValues(params);
			result = ServiceCallHelper.invokeServiceAndGetResult(dcRequest, params, header,
					URLConstants.CREATE_PARTY_CONSENT_DETAILS);

			if (result.hasParamByName("partyConsentId")) {
				result.removeParamByName("dbpErrCode");
				result.removeParamByName("dbpErrMsg");
			}
			return result;
		} else {
			termsAndConditionsFromAdmin = ContentManagementUtils.getTermsAndConditions(termsAndConditionsCode, leid,
					appId, language);
			if (HelperMethods.hasDBPErrorMSG(termsAndConditionsFromAdmin)) {
				ErrorCodeEnum.ERR_10185.setErrorCode(result);
				return result;
			}
			String versionIdfromAdmin = termsAndConditionsFromAdmin.getParamValueByName("versionId");
			String legalEntityId = dcRequest.getParameter("legalEntityId");
			if (StringUtils.isBlank(legalEntityId)) {
				legalEntityId = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(dcRequest);
			}

			if (StringUtils.isBlank(versionIdfromAdmin)) {
				ErrorCodeEnum.ERR_10187.setErrorCode(result);
				return result;
			}

			if (!"EarlyPayOffSimulation_TnC".equalsIgnoreCase(termsAndConditionsCode)) {
				String filter = "customerId" + DBPUtilitiesConstants.EQUAL + customerId + DBPUtilitiesConstants.AND
						+ "termsAndConditionsCode" + DBPUtilitiesConstants.EQUAL + termsAndConditionsCode
						+ DBPUtilitiesConstants.AND + "languageCode" + DBPUtilitiesConstants.EQUAL + language
						+ DBPUtilitiesConstants.AND + "companyLegalUnit" + DBPUtilitiesConstants.EQUAL + legalEntityId;

				Result termsAndConditions = HelperMethods.callGetApi(dcRequest, filter,
						HelperMethods.getHeaders(dcRequest), URLConstants.CUSTOMER_TERMSANDCONDITIONS_GET);

				if (HelperMethods.hasRecords(termsAndConditions)) {
					String versionId = HelperMethods.getFieldValue(termsAndConditions, "versionId");
					String leId = HelperMethods.getFieldValue(termsAndConditions, "companyLegalUnit");
					if (StringUtils.isNotBlank(versionIdfromAdmin) && versionIdfromAdmin.equals(versionId)
							&& StringUtils.isNotBlank(legalEntityId) && legalEntityId.equals(leId)) {
						ErrorCodeEnum.ERR_10186.setErrorCode(result);
						return result;
					} else {
						inputParams.put("id", HelperMethods.getFieldValue(termsAndConditions, "id"));
						inputParams.put("versionId", versionIdfromAdmin);
						result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
								URLConstants.CUSTOMER_TERMSANDCONDITIONS_UPDATE);
						return result;
					}
				}
			}

			UserAgentUtil ua = new UserAgentUtil(dcRequest);

			inputParams.put("id", UUID.randomUUID().toString());
			inputParams.put("customerId", customerId);
			inputParams.put("versionId", versionIdfromAdmin);
			inputParams.put("languageCode", language);
			inputParams.put("appId", ua.getAppID());
			inputParams.put("channel", ua.getChannel());
			inputParams.put("platform", ua.getPlatform());
			inputParams.put("browser", ua.getBrowser());
			inputParams.put("companyLegalUnit", legalEntityId);

			HelperMethods.removeNullValues(inputParams);
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMER_TERMSANDCONDITIONS_CREATE);

			return result;
		}
	}

}
