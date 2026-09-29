package com.kony.contentproductservices.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;
import com.temenos.infinity.api.commons.constants.FabricConstants;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

public class ContentManagementUtils {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final String DEFAULT_LANGUAGE_CODE = "en-US";
	private static final String DEFAULT_APP = "RETAIL_AND_BUSINESS_BANKING";
	private static final String TANDC_ACTIVE_STATUS_ID = "SID_TANDC_ACTIVE";

	/**
	 * @param jsonString
	 * @return
	 */
	public static JSONObject getStringAsJSONObject(String jsonString) {
		JSONObject generatedJSONObject = new JSONObject();
		if (StringUtils.isBlank(jsonString))
			return null;
		try {
			generatedJSONObject = new JSONObject(jsonString);
			return generatedJSONObject;
		} catch (JSONException e) {
			alert.prepareError("Unexpected error has occurred", e).log();
			return null;
		}
	}

	public static Result getTermsAndConditions(String termsAndConditionsCode, String leid, String appId,
			String language) throws Exception {
		String consentBackend = EnvironmentConfigurationsHandler.getServerAppProperty("CONSENT_BACKEND");
		if (ContentManagementConstants.CONSENT_BACKEND_DBXDB.equalsIgnoreCase(consentBackend)) {
			return getTermsAndConditionsFromDB(termsAndConditionsCode, leid, appId, language);
		}
		Result result = new Result();
		try {

			Map<String, Object> inputBodyMap = new HashMap<String, Object>();
			inputBodyMap.put("tncCodeIdRef", termsAndConditionsCode);
			String termAndConditonId = null, consentTypeId = null, retentionPeriod = null, consentMandatory = null,
					termNConditionAppsId = null, languageCode = null;
			Map<String, Object> headerMap = new HashMap<>();
			headerMap.put("companyid", leid);

			String readTermAndConditionResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId("ConsentManagementJSONServices").withOperationId("getTermsNConditionsCode")
					.withRequestParameters(inputBodyMap).withRequestHeaders(headerMap).build().getResponse();

			JSONObject readTermAndConditionResponseJSON = ContentManagementUtils
					.getStringAsJSONObject(readTermAndConditionResponse);
			if (readTermAndConditionResponseJSON != null
					&& readTermAndConditionResponseJSON.has(FabricConstants.OPSTATUS)
					&& readTermAndConditionResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readTermAndConditionResponseJSON.has("termsNConditionCodes")) {
				JSONArray readTermAndConditionJSONArray = readTermAndConditionResponseJSON
						.optJSONArray("termsNConditionCodes");
				if (readTermAndConditionJSONArray == null || readTermAndConditionJSONArray.length() < 1) {
					alert.prepareError("Failed to get Terms and Conditions").log();
					ErrorCodeEnum.ERR_29062.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				} else {
					JSONObject currTermAndConditionRecord = readTermAndConditionJSONArray.getJSONObject(0);
					termAndConditonId = currTermAndConditionRecord.getString("termCondCodeId");
					retentionPeriod = currTermAndConditionRecord.getString("retentionPeriod");
					consentTypeId = currTermAndConditionRecord.getString("consentTypeId");
				}
			} else {
				alert.prepareError("Failed to get Terms and Conditions").log();
				ErrorCodeEnum.ERR_29062.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}

			inputBodyMap.clear();
			headerMap = new HashMap<>();
			headerMap.put("companyid", leid);

			String readTermAndConditionAppsResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId("ConsentManagementJSONServices").withOperationId("getTermsNConditionsApps")
					.withRequestParameters(inputBodyMap).withRequestHeaders(headerMap).build().getResponse();

			JSONObject readTermAndConditionAppsResponseJSON = ContentManagementUtils
					.getStringAsJSONObject(readTermAndConditionAppsResponse);
			if (readTermAndConditionAppsResponseJSON != null
					&& readTermAndConditionAppsResponseJSON.has(FabricConstants.OPSTATUS)
					&& readTermAndConditionAppsResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readTermAndConditionAppsResponseJSON.has("termNConditionsApps")) {
				JSONArray readTermAndConditionAppsJSONArray = readTermAndConditionAppsResponseJSON
						.optJSONArray("termNConditionsApps");
				if (readTermAndConditionAppsJSONArray == null || readTermAndConditionAppsJSONArray.length() < 1) {
					alert.prepareError("Failed to get Terms and Conditions").log();
					ErrorCodeEnum.ERR_29062.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				} else {
					for (int i = 0; i < readTermAndConditionAppsJSONArray.length(); i++) {
						JSONObject currTermAndConditionAppRecord = readTermAndConditionAppsJSONArray.getJSONObject(i);
						String termConditionCodeId = currTermAndConditionAppRecord.getString("termConditionCodeId");
						String termNConditionappId = currTermAndConditionAppRecord.getString("appId");
						if (termNConditionappId.equals(appId) && termConditionCodeId.equals(termAndConditonId)) {
							termNConditionAppsId = currTermAndConditionAppRecord.getString("termNConditionAppsId");
							if (currTermAndConditionAppRecord.has("isConsentMandatory") && StringUtils
									.isNotBlank(currTermAndConditionAppRecord.optString("isConsentMandatory"))) {
								consentMandatory = currTermAndConditionAppRecord.optString("isConsentMandatory");
							}
							break;
						}
					}
				}
			} else {
				alert.prepareError("Failed to get Terms and Conditions").log();
				ErrorCodeEnum.ERR_29062.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}

			inputBodyMap.clear();
			inputBodyMap.put("tncContentIdRef", termNConditionAppsId);
			headerMap = new HashMap<>();
			headerMap.put("companyid", leid);

			String readTermAndConditionTextResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId("ConsentManagementJSONServices").withOperationId("getActiveTermsNConditonsContents")
					.withRequestParameters(inputBodyMap).withRequestHeaders(headerMap).build().getResponse();

			JSONObject readTermAndConditionTextResponseJSON = ContentManagementUtils
					.getStringAsJSONObject(readTermAndConditionTextResponse);
			if (readTermAndConditionTextResponseJSON != null
					&& readTermAndConditionTextResponseJSON.has(FabricConstants.OPSTATUS)
					&& readTermAndConditionTextResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readTermAndConditionTextResponseJSON.has("termsNConditionContents")) {
				JSONArray readTermAndConditionTextJSONArray = readTermAndConditionTextResponseJSON
						.optJSONArray("termsNConditionContents");
				if (readTermAndConditionTextJSONArray == null || readTermAndConditionTextJSONArray.length() < 1) {
					// If content is null returning content for default language
					alert.prepareError("Failed to get Terms and Conditions text").log();
					ErrorCodeEnum.ERR_29063.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				} else {
					for (int i = 0; i < readTermAndConditionTextJSONArray.length(); i++) {
						JSONObject _currTermAndConditionTextRecord = readTermAndConditionTextJSONArray.getJSONObject(i);
						String lang = _currTermAndConditionTextRecord.getString("language");
						if (lang.equals(languageCode)) {
							Param _currTermAndConditionContentParam = new Param("termsAndConditionsContent",
									_currTermAndConditionTextRecord.optString("content"), FabricConstants.STRING);
							result.addParam(_currTermAndConditionContentParam);
							String termAndConditionContentTypeId = _currTermAndConditionTextRecord
									.getString("contentType");
							Param contentTypeId_Param = new Param("contentTypeId", termAndConditionContentTypeId,
									FabricConstants.STRING);
							result.addParam(contentTypeId_Param);
							String termAndConditionVersionId = _currTermAndConditionTextRecord.getString("versionNo");
							Param versionId_Param = new Param("versionId", termAndConditionVersionId,
									FabricConstants.STRING);
							result.addParam(versionId_Param);
							String contentModifiedOn = _currTermAndConditionTextRecord.getString("lastModifiedDate");
							Param contentModifiedOn_Param = new Param("contentModifiedOn", contentModifiedOn,
									FabricConstants.STRING);
							result.addParam(contentModifiedOn_Param);

							String termAndConditionContentId = _currTermAndConditionTextRecord
									.getString("termsNConditionContentId");
							Param contentId_Param = new Param("contentId", termAndConditionContentId,
									FabricConstants.STRING);
							result.addParam(contentId_Param);
							Param termAndConditonCodeId = new Param("termAndConditonCodeId", termAndConditonId,
									FabricConstants.STRING);
							result.addParam(termAndConditonCodeId);
							Param retentionPeriodd = new Param("retentionPeriod", retentionPeriod,
									FabricConstants.STRING);
							result.addParam(retentionPeriodd);
							Param consentTypeIdd = new Param("consentType", consentTypeId, FabricConstants.STRING);
							result.addParam(consentTypeIdd);
							Param consentMandatory_Param = new Param("isConsentMandatory", consentMandatory,
									FabricConstants.STRING);
							result.addParam(consentMandatory_Param);
							result.addParam(new Param("status", "Success", FabricConstants.STRING));
						} else if (lang.equals(DEFAULT_LANGUAGE_CODE)) {
							Param _currTermAndConditionContentParam = new Param("termsAndConditionsContent",
									_currTermAndConditionTextRecord.optString("content"), FabricConstants.STRING);
							result.addParam(_currTermAndConditionContentParam);
							String termAndConditionContentTypeId = _currTermAndConditionTextRecord
									.getString("contentType");
							Param contentTypeId_Param = new Param("contentTypeId", termAndConditionContentTypeId,
									FabricConstants.STRING);
							result.addParam(contentTypeId_Param);
							String termAndConditionVersionId = _currTermAndConditionTextRecord.getString("versionNo");
							Param versionId_Param = new Param("versionId", termAndConditionVersionId,
									FabricConstants.STRING);
							result.addParam(versionId_Param);
							String termAndConditionContentId = _currTermAndConditionTextRecord
									.getString("termsNConditionContentId");
							Param contentId_Param = new Param("contentId", termAndConditionContentId,
									FabricConstants.STRING);
							result.addParam(contentId_Param);
							String contentModifiedOn = _currTermAndConditionTextRecord.getString("lastModifiedDate");
							Param contentModifiedOn_Param = new Param("contentModifiedOn", contentModifiedOn,
									FabricConstants.STRING);
							result.addParam(contentModifiedOn_Param);
							Param termAndConditonCodeId = new Param("termAndConditonCodeId", termAndConditonId,
									FabricConstants.STRING);
							result.addParam(termAndConditonCodeId);
							Param retentionPeriodd = new Param("retentionPeriod", retentionPeriod,
									FabricConstants.STRING);
							result.addParam(retentionPeriodd);
							Param consentTypeIdd = new Param("consentType", consentTypeId, FabricConstants.STRING);
							result.addParam(consentTypeIdd);
							Param consentMandatory_Param = new Param("isConsentMandatory", consentMandatory,
									FabricConstants.STRING);
							result.addParam(consentMandatory_Param);
							result.addParam(new Param("status", "Success", FabricConstants.STRING));
						}
					}
				}
			}
		} catch (Exception e) {
			alert.prepareError("Caught exception while fetching terms and conditions: ", e.toString()).log();
		}
		return result;
	}

	public static Result getTermsAndConditionsFromDB(String termAndConditionCode, String leid, String appId,
			String languageCode) {
		Result result = new Result();
		try {

			String termAndConditonId = null;

			// Fetching T&C id from termandcondition table
			Map<String, Object> inputBodyMap = new HashMap<String, Object>();
			inputBodyMap.put(ODataQueryConstants.FILTER,
					"Code eq '" + termAndConditionCode + "'");
			inputBodyMap.put(ODataQueryConstants.SELECT, "id");

			String readTermAndConditionResponse = DBPServiceExecutorBuilder.builder().withServiceId("TNCDBServices")
					.withOperationId("dbxdb_termandcondition_get").withRequestParameters(inputBodyMap).build()
					.getResponse();

			JSONObject readTermAndConditionResponseJSON = ContentManagementUtils
					.getStringAsJSONObject(readTermAndConditionResponse);

			if (readTermAndConditionResponseJSON != null
					&& readTermAndConditionResponseJSON.has(FabricConstants.OPSTATUS)
					&& readTermAndConditionResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readTermAndConditionResponseJSON.has("termandcondition")) {
				JSONArray readTermAndConditionJSONArray = readTermAndConditionResponseJSON
						.optJSONArray("termandcondition");
				if (readTermAndConditionJSONArray == null || readTermAndConditionJSONArray.length() < 1) {
					alert.prepareError("Failed to get Terms and Conditions").log();
					ErrorCodeEnum.ERR_29063.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				} else {
					JSONObject currTermAndConditionRecord = readTermAndConditionJSONArray.getJSONObject(0);
					termAndConditonId = currTermAndConditionRecord.getString("id");
				}
			} else {
				alert.prepareError("Failed to get Terms and Conditions");
				ErrorCodeEnum.ERR_29063.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}

			// Fetch T&C Content from termandconditiontext table
			inputBodyMap.clear();
			inputBodyMap.put(ODataQueryConstants.FILTER,
					"TermAndConditionId eq '" + termAndConditonId + "' and LanguageCode eq '" + languageCode
							+ "' and Status_id eq '" + TANDC_ACTIVE_STATUS_ID + "'");
			inputBodyMap.put(ODataQueryConstants.SELECT, "Content, ContentType_id, Version_Id");

			String readTermAndConditionTextResponse = DBPServiceExecutorBuilder.builder().withServiceId("TNCDBServices")
					.withOperationId("dbxdb_termandconditiontext_get").withRequestParameters(inputBodyMap).build()
					.getResponse();

			JSONObject readTermAndConditionTextResponseJSON = ContentManagementUtils
					.getStringAsJSONObject(readTermAndConditionTextResponse);
			JSONArray readTermAndConditionTextJSONArray = readTermAndConditionTextResponseJSON
					.optJSONArray("termandconditiontext");
			JSONObject currTermAndConditionTextRecord = readTermAndConditionTextJSONArray.getJSONObject(0);

			Param currTermAndConditionContentParam = new Param("termsAndConditionsContent",
					currTermAndConditionTextRecord.optString("Content"), FabricConstants.STRING);
			result.addParam(currTermAndConditionContentParam);
			String termAndConditionContentTypeId = currTermAndConditionTextRecord.getString("ContentType_id");
			Param contentTypeId_Param = new Param("contentTypeId", termAndConditionContentTypeId,
					FabricConstants.STRING);
			result.addParam(contentTypeId_Param);
			String termAndConditionVersionId = currTermAndConditionTextRecord.getString("Version_Id");
			Param versionId_Param = new Param("versionId", termAndConditionVersionId, FabricConstants.STRING);
			result.addParam(versionId_Param);
			
			result.addParam(new Param("status", "Success", FabricConstants.STRING));
			return result;

		} catch (Exception e) {
			alert.prepareError("Caught exception while fetching terms and conditions: ", e.toString()).log();
		}
		return result;
	}

}
