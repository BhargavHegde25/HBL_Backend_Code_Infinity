package com.kony.contentproductservices;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.contentproductservices.utils.ContentManagementConstants;
import com.kony.contentproductservices.utils.ContentManagementUtils;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;
import com.temenos.infinity.api.commons.constants.FabricConstants;

public class GetCustomerTermsAndConditions implements JavaService2 {

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
			String language = DBPUtilitiesConstants.TNC_DEFAULT_LANGUAGE;
			String versionIdfromAdmin = null;
			String versionId = null;
			String filter = null;
			String lastLoginTime = null;
			String tNCRefreshPeriod = null;
			String lastModifiedTSforTNCRefresh = null;
			String customerId = HelperMethods.getCustomerIdFromSession(dcRequest);
			Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
			String termAndConditionCode = null, languageCode = null, termAndConditonId = null, appId = null;
			String termNConditionAppsId = null, consentTypeId = null, retentionPeriod = null, consentMandatory = null;

			String leid = dcRequest.getParameter("legalEntityId");
			if (StringUtils.isBlank(leid)) {
				leid = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(dcRequest);
			}
			diagnostic.prepareDebug(" legal entity id for the service excution is : " + leid).log();

			if (StringUtils.isNotBlank(inputParams.get("languageCode"))) {
				language = inputParams.get("languageCode");
			}
			termsAndConditionsCode = inputParams.get("termsAndConditionsCode");
			if (inputParams.get("lastLoginTime") != null && StringUtils.isNotBlank(inputParams.get("lastLoginTime"))) {
				lastLoginTime = inputParams.get("lastLoginTime");
			}
			if (inputParams.get("tNCRefreshPeriod") != null && StringUtils.isNotBlank(inputParams.get("tNCRefreshPeriod"))) {
				tNCRefreshPeriod = inputParams.get("tNCRefreshPeriod");
			}
			if (inputParams.get("lastModifiedTSforTNCRefresh") != null && StringUtils.isNotBlank(inputParams.get("lastModifiedTSforTNCRefresh"))) {
				lastModifiedTSforTNCRefresh = inputParams.get("lastModifiedTSforTNCRefresh");
			}

			if (StringUtils.isBlank(termsAndConditionsCode)) {
				ErrorCodeEnum.ERR_10184.setErrorCode(result);
				return result;
			}

			if (StringUtils.isBlank(leid)) {
				ErrorCodeEnum.ERR_29040.setErrorCode(result);
				return result;
			}

			if (StringUtils.isBlank(languageCode)) {
				languageCode = DEFAULT_LANGUAGE_CODE;
			}

			appId = HelperMethods.getAppId(dcRequest);
			if (StringUtils.isBlank(appId)) {
				appId = DEFAULT_APP;
			}
            
			result = getTNC(termsAndConditionsCode, leid, appId, dcRequest, languageCode);
			
	      /*  if (!termsAndConditionsCode.equals(DBPUtilitiesConstants.TNC_LOGIN)
					|| (termsAndConditionsCode.equals(DBPUtilitiesConstants.TNC_LOGIN) && lastLoginTime == null)) {
				result = getTNC(termsAndConditionsCode, leid, appId, dcRequest);
			} else {
				DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MMM-dd");
				LocalDate lastLoginDate = LocalDate.parse(lastLoginTime);
				LocalDate expiryDate = LocalDate.parse(lastModifiedTSforTNCRefresh).plusDays(Integer.parseInt(tNCRefreshPeriod));
				LocalDate currentDate = LocalDate.now();
				if (currentDate.isBefore(expiryDate) || (currentDate.isAfter(expiryDate) && lastLoginDate.isAfter(expiryDate))) {
					result.addParam(new Param("alreadySigned", "true", DBPUtilitiesConstants.STRING_TYPE));
				} else {
					result = getTNC(termsAndConditionsCode, leid, appId, dcRequest);
				}
			} */
			return result;
		} catch (Exception e) {
			alert.prepareError("Caught exception while fetching terms and conditions: ", e.toString()).log();
		}
		return result;
	}
	
	private Result getTNC(String termsAndConditionsCode, String leid, String appId, DataControllerRequest dcRequest,
			String languageCode) throws Exception {
		Result result = new Result();
		String customerId = HelperMethods.getCustomerIdFromSession(dcRequest);
		try {
			result = ContentManagementUtils.getTermsAndConditions(termsAndConditionsCode, leid, appId, languageCode);
		} catch (Exception e) {
			alert.prepareError("Caught exception while fetching terms and conditions: ", e.toString()).log();
		}

		if (HelperMethods.hasDBPErrorMSG(result)) {
			ErrorCodeEnum.ERR_10188.setErrorCode(result);
			return result;
		}

		String consentBackend = EnvironmentConfigurationsHandler.getServerAppProperty("CONSENT_BACKEND");

		if (!ContentManagementConstants.CONSENT_BACKEND_DBXDB.equalsIgnoreCase(consentBackend)) {
			dcRequest.getSession().setAttribute("globalTNCContentId", result.getParamValueByName("contentId"));
			Map<String, Object> params = new HashMap<>();
			params.clear();
			params.put("partyId", customerId);
			Map<String, String> header = HelperMethods.getHeaders(dcRequest);
			header.put("companyid", leid);

			Result partyConsentDetails = ServiceCallHelper.invokeServiceAndGetResult(params, header,
					URLConstants.GET_PARTY_CONSENT_DETAILS, "");

			if (partyConsentDetails != null) {
				Dataset ds = partyConsentDetails.getDatasetById("partyConsentDetails");
				if (ds != null && ds.getAllRecords().size() > 0) {
					String globalContentId = result.getParamValueByName("contentId");
					for (Record record : ds.getAllRecords()) {
						String contentId = record.getParamValueByName("termsNConditionContentId");
						String consentLeId = HelperMethods.getFieldValue(record, "legalEntityId");
						diagnostic.prepareDebug(" consent legal entity id for the service excution is : " + consentLeId)
								.log();
						if (contentId != null && globalContentId != null && contentId.equalsIgnoreCase(globalContentId)
								&& StringUtils.isNotBlank(consentLeId) && consentLeId.equalsIgnoreCase(leid)) {
							
							if(termsAndConditionsCode.equalsIgnoreCase("EarlyPayOffSimulation_TnC")) {
								result
								.addParam(new Param("alreadySigned", "true", DBPUtilitiesConstants.STRING_TYPE));
							}else {
								
								Result updatedResult = new Result();
								updatedResult
										.addParam(new Param("alreadySigned", "true", DBPUtilitiesConstants.STRING_TYPE));
								return updatedResult;
							}

						}
					}
				}
			}
			return result;
		}else {
			String versionIdfromAdmin = result.getParamValueByName("versionId");
			
			String legalEntityId = dcRequest.getParameter("legalEntityId");
			if (StringUtils.isBlank(legalEntityId)) {
				legalEntityId = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(dcRequest) != null
						? LegalEntityUtil.getLegalEntityIdFromSessionOrCache(dcRequest)
						: EnvironmentConfigurationsHandler.getServerAppProperty("BRANCH_ID_REFERENCE");
			}
			
			String filter = "customerId" + DBPUtilitiesConstants.EQUAL + customerId + DBPUtilitiesConstants.AND +
	                "termsAndConditionsCode" + DBPUtilitiesConstants.EQUAL + termsAndConditionsCode
	                + DBPUtilitiesConstants.AND +
	                "languageCode" + DBPUtilitiesConstants.EQUAL + languageCode +
	                DBPUtilitiesConstants.AND + "companyLegalUnit" + DBPUtilitiesConstants.EQUAL + legalEntityId;
			
			Result termsAndConditions = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
	                URLConstants.CUSTOMER_TERMSANDCONDITIONS_GET);
			
			if (HelperMethods.hasRecords(termsAndConditions)) {
	            String versionId = HelperMethods.getFieldValue(termsAndConditions, "versionId");
	            String leId = HelperMethods.getFieldValue(termsAndConditions, "companyLegalUnit");
				if (StringUtils.isNotBlank(versionIdfromAdmin) && versionIdfromAdmin.equals(versionId)
						&& StringUtils.isNotBlank(legalEntityId) && legalEntityId.equals(leId)) {
					if(termsAndConditionsCode.equalsIgnoreCase("EarlyPayOffSimulation_TnC")) {
						result
						.addParam(new Param("alreadySigned", "true", DBPUtilitiesConstants.STRING_TYPE));
					}else {
						Result updatedResult = new Result();
						updatedResult.addParam(new Param("alreadySigned", "true", DBPUtilitiesConstants.STRING_TYPE));
						return updatedResult;
					}

				}
	        }
					
			return result;
		}
		
	}
}
