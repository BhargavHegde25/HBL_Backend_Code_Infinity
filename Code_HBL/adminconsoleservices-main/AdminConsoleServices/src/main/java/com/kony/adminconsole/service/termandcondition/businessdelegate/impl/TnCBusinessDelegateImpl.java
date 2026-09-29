package com.kony.adminconsole.service.termandcondition.businessdelegate.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.service.termandcondition.businessdelegate.api.TnCBusinessDelegate;
import com.kony.adminconsole.utilities.ACConstants;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.ServiceId;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.kony.adminconsole.utilities.StatusEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class TnCBusinessDelegateImpl implements TnCBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	private static final String DEFAULT_LANGUAGE_CODE = "en-US";
	private static final String DEFAULT_DRAFT_VERSION = "N/A";
	private static final String MIN_VERSION = "1.0";

	private static final String TANDC_DRAFT_STATUS_ID = StatusEnum.SID_TANDC_DRAFT.name();
	private static final String TANDC_ARCHIVED_STATUS_ID = StatusEnum.SID_TANDC_ARCHIVED.name();
	private static final String TANDC_ACTIVE_STATUS_ID = StatusEnum.SID_TANDC_ACTIVE.name();
	private static final String TANDC_MS_ACTIVE_STATUS_ID = "ACTIVE";
	private static final String ACTIVE_STATUS_ID = "SID_ACTIVE";
	private static final String DEFAULT_APP_ID = "RETAIL_AND_BUSINESS_BANKING";

	@Override
	public Result deleteTermsAndConditionsVersion(Map<String, Object> postParametersMap, String backendToken)
			throws DBPApplicationException {

		Result result = new Result();

		try {
			String termAndConditionCode =postParametersMap.get("termAndConditionCode").toString();
			String languageCode =postParametersMap.get("languageCode").toString();
			String leId =(String) postParametersMap.get("legalEntityId");

			String termAndConditionId = null;
			// Fetching T&C id from termandcondition table
			Set<String> TncAppIds = new HashSet<>();
			Map<String, Object> inputBodyMap = new HashMap<String, Object>();
			inputBodyMap.put("tncCodeIdRef", termAndConditionCode);	
			
			Map<String, Object> headerMap = new HashMap<>();
			headerMap.put("backendToken", backendToken);
			headerMap.put("companyid", leId);
			
			String readTermAndConditionResponse =
					DBPServiceExecutorBuilder.builder()
					.withServiceId(ServiceId.CONSENTMS)
					.withOperationId(OperationName.OP_GET_TERMS_AND_CONDITIONS_CODE)
					.withRequestParameters(inputBodyMap)
					.withRequestHeaders(headerMap)
					.build()
					.getResponse();
			
			JSONObject readTermAndConditionResponseJSON = CommonUtilities
					.getStringAsJSONObject(readTermAndConditionResponse);
			if (readTermAndConditionResponseJSON != null
					&& readTermAndConditionResponseJSON.has(FabricConstants.OPSTATUS)
					&& readTermAndConditionResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readTermAndConditionResponseJSON.has("termsNConditionCodes")) {
				JSONArray readTermAndConditionJSONArray = readTermAndConditionResponseJSON
						.optJSONArray("termsNConditionCodes");
				if (readTermAndConditionJSONArray == null || readTermAndConditionJSONArray.length() < 1) {
					alert.prepareError("Failed to get Terms and Conditions").log();
					ErrorCodeEnum.ERR_20264.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				} else {
					JSONObject termAndConditionJSONObject = null;
					for (int indexVar = 0; indexVar < readTermAndConditionJSONArray.length(); indexVar++) {
						termAndConditionJSONObject = readTermAndConditionJSONArray.getJSONObject(indexVar);
						String code = termAndConditionJSONObject.getString("termConditionCode");
						if(code.equals(termAndConditionCode))
						{
							termAndConditionId = termAndConditionJSONObject.getString("termCondCodeId");
							break;
						}						
					}
				}
			}

			//Fetch term and condition id  to apps mapping
			inputBodyMap.clear();
			
			headerMap = new HashMap<>();
			headerMap.put("backendToken", backendToken);
			headerMap.put("companyid", leId);
			
			String readTermAndConditionAppsResponse =
					DBPServiceExecutorBuilder.builder()
					.withServiceId(ServiceId.CONSENTMS)
					.withOperationId(OperationName.OP_GET_TERMS_AND_CONDITIONS_APPS)
					.withRequestParameters(inputBodyMap)
					.withRequestHeaders(headerMap)
					.build()
					.getResponse();
			
			JSONObject readTermAndConditionAppsResponseJSON = CommonUtilities
					.getStringAsJSONObject(readTermAndConditionAppsResponse);
			if (readTermAndConditionAppsResponseJSON != null
					&& readTermAndConditionAppsResponseJSON.has(FabricConstants.OPSTATUS)
					&& readTermAndConditionAppsResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readTermAndConditionAppsResponseJSON.has("termNConditionsApps")) {
				JSONArray readTermAndConditionAppsJSONArray = readTermAndConditionAppsResponseJSON
						.optJSONArray("termNConditionsApps");
				if (readTermAndConditionAppsJSONArray == null || readTermAndConditionAppsJSONArray.length() < 1) {
					alert.prepareError("Failed to get Terms and Conditions").log();
					ErrorCodeEnum.ERR_20264.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				} else {
					for (int i=0;i<readTermAndConditionAppsJSONArray.length();i++)
					{
						JSONObject currTermAndConditionAppRecord = readTermAndConditionAppsJSONArray.getJSONObject(i);						
						String termConditionCodeId = currTermAndConditionAppRecord.getString("termConditionCodeId");					
						if(termConditionCodeId.equals(termAndConditionId))
						{
							String termNConditionappId = currTermAndConditionAppRecord.getString("termNConditionAppsId");
							TncAppIds.add(termNConditionappId);
						}
					}
				}
			} else {
				alert.prepareError("Failed to get Terms and Conditions").log();
				ErrorCodeEnum.ERR_20264.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}
			inputBodyMap.clear();
			
			headerMap = new HashMap<>();
			headerMap.put("backendToken", backendToken);
			headerMap.put("companyid", leId);
			
			String readTermAndConditionTextResponse =
					DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CONSENTMS)
					.withOperationId(OperationName.OP_GET_TERMS_AND_CONDITIONS_CONTENTS)
					.withRequestParameters(inputBodyMap)
					.withRequestHeaders(headerMap)
					.build()
					.getResponse();
			
			JSONObject readTermAndConditionTextResponseJSON = CommonUtilities
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
					ErrorCodeEnum.ERR_20265.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				}else{
					Set<String> tncContentIds = new HashSet<String>();
					for(int j=0;j<readTermAndConditionTextJSONArray.length();j++)
					{

						JSONObject _currTermAndConditionTextRecord = readTermAndConditionTextJSONArray
								.getJSONObject(j);
						String language = _currTermAndConditionTextRecord.getString("language");
						String termConditionAppsId = _currTermAndConditionTextRecord.getString("termConditionAppsId");
						String status = _currTermAndConditionTextRecord.getString("status");
						if(language.equals(languageCode) && TncAppIds.contains(termConditionAppsId) && status.equals("DRAFT"))
						{
							String contentId = _currTermAndConditionTextRecord.getString("termsNConditionContentId");
							tncContentIds.add(contentId);
						}
					}
					for (String contnentId : tncContentIds) {
						inputBodyMap.clear();
						inputBodyMap.put("tncContentIdRef",contnentId);
						
						headerMap = new HashMap<>();
						headerMap.put("backendToken", backendToken);
						headerMap.put("companyid", leId);
						
						String deleteTermAndConditionTextResponse =
								DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CONSENTMS)
								.withOperationId(OperationName.OP_DELETE_TERMS_AND_CONDITIONS_CONTENT)
								.withRequestParameters(inputBodyMap)
								.withRequestHeaders(headerMap)
								.build()
								.getResponse();

						JSONObject deleteTermAndConditionTextResponseJSON = CommonUtilities
								.getStringAsJSONObject(deleteTermAndConditionTextResponse);
						if (!(deleteTermAndConditionTextResponseJSON != null
								&& deleteTermAndConditionTextResponseJSON.has(FabricConstants.OPSTATUS)
								&& deleteTermAndConditionTextResponseJSON.getInt(FabricConstants.OPSTATUS) == 0)) {
							result.addParam(new Param("status", "Failure", FabricConstants.STRING));
							ErrorCodeEnum.ERR_20277.setErrorCode(result);
							alert.prepareError("Failed to delete Terms and Conditions "+ contnentId).log();
						}

					}
				}
				result.addParam(new Param("status", "Success", FabricConstants.STRING));
				return result;
			}
		} catch (Exception e) {
			alert.prepareError("Failed to create Terms and Conditions version").log();
			ErrorCodeEnum.ERR_20277.setErrorCode(result);
			return result;
		}
		return result;
	}

	@Override
	public Result getTermsAndConditions(Map<String, Object> postParametersMap, String backendToken)
			throws DBPApplicationException {
		Result result = new Result();
		Result tempResult = new Result();
		try {
			String termAndConditionCode = null, languageCode = null, termAndConditonId = null, appId = null, leId = null;
			String termNConditionAppsId = null, consentTypeId = null, retentionPeriod =null, consentMandatory = null;
			termAndConditionCode = postParametersMap.get("termAndConditionCode").toString();
			languageCode = postParametersMap.get("languageCode").toString();
			appId = postParametersMap.get("appId").toString();
			leId = (String) postParametersMap.get("legalEntityId");

			//{{HTTP-Protocol}}://{{Host-URL}}/ms-consent-api/api/v2.0.0/reference/termsNConditionsCodes?termNConditionCode=DBX_Default_TnC

			// Fetching T&C id from termandcondition table
			Map<String, Object> inputBodyMap = new HashMap<String, Object>();
			inputBodyMap.put("tncCodeIdRef",termAndConditionCode);
			
			Map<String, Object> headerMap = new HashMap<>();
			headerMap.put("backendToken", backendToken);
			headerMap.put("companyid", leId);
			
			String readTermAndConditionResponse =
					DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CONSENTMS)
					.withOperationId(OperationName.OP_GET_TERMS_AND_CONDITIONS_CODE)
					.withRequestParameters(inputBodyMap)
					.withRequestHeaders(headerMap)
					.build()
					.getResponse();
			
			JSONObject readTermAndConditionResponseJSON = CommonUtilities
					.getStringAsJSONObject(readTermAndConditionResponse);
			if (readTermAndConditionResponseJSON != null
					&& readTermAndConditionResponseJSON.has(FabricConstants.OPSTATUS)
					&& readTermAndConditionResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readTermAndConditionResponseJSON.has("termsNConditionCodes")) {
				JSONArray readTermAndConditionJSONArray = readTermAndConditionResponseJSON
						.optJSONArray("termsNConditionCodes");
				if (readTermAndConditionJSONArray == null || readTermAndConditionJSONArray.length() < 1) {
					alert.prepareError("Failed to get Terms and Conditions").log();
					ErrorCodeEnum.ERR_20264.setErrorCode(result);
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
				ErrorCodeEnum.ERR_20264.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}
			//{{HTTP-Protocol}}://{{Host-URL}}/ms-consent-api/api/v2.0.0/reference/termsConditionsApps
			// get tncappid based on appId and term and condition id

			//get active term and condition content 			
			//{{HTTP-Protocol}}://{{Host-URL}}/ms-consent-api/api/v2.0.0/holdings/termsConditionsContents/active/TCA2312354
			//filter on language
			//			Fetch active T&C Content from termandconditiontext table
			//			{{HTTP-Protocol}}://{{Host-URL}}/ms-consent-api/api/v2.0.0/holdings/termsConditionsContents/active/TCA2312354
			//			Filter on language
			inputBodyMap.clear();
			headerMap = new HashMap<>();
			headerMap.put("backendToken", backendToken);
			headerMap.put("companyid", leId);
			
			String readTermAndConditionAppsResponse =
					DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CONSENTMS)
					.withOperationId(OperationName.OP_GET_TERMS_AND_CONDITIONS_APPS)
					.withRequestParameters(inputBodyMap)
					.withRequestHeaders(headerMap)
					.build()
					.getResponse();
			
			JSONObject readTermAndConditionAppsResponseJSON = CommonUtilities
					.getStringAsJSONObject(readTermAndConditionAppsResponse);
			if (readTermAndConditionAppsResponseJSON != null
					&& readTermAndConditionAppsResponseJSON.has(FabricConstants.OPSTATUS)
					&& readTermAndConditionAppsResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readTermAndConditionAppsResponseJSON.has("termNConditionsApps")) {
				JSONArray readTermAndConditionAppsJSONArray = readTermAndConditionAppsResponseJSON
						.optJSONArray("termNConditionsApps");
				if (readTermAndConditionAppsJSONArray == null || readTermAndConditionAppsJSONArray.length() < 1) {
					alert.prepareError("Failed to get Terms and Conditions").log();
					ErrorCodeEnum.ERR_20264.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				} else {
					for(int i=0;i<readTermAndConditionAppsJSONArray.length();i++)
					{
						JSONObject currTermAndConditionAppRecord = readTermAndConditionAppsJSONArray.getJSONObject(i);
						String termConditionCodeId = currTermAndConditionAppRecord.getString("termConditionCodeId");
						String termNConditionappId = currTermAndConditionAppRecord.getString("appId");
						if (termNConditionappId.equals(appId) && termConditionCodeId.equals(termAndConditonId))
						{
							termNConditionAppsId = currTermAndConditionAppRecord.getString("termNConditionAppsId");
							if(currTermAndConditionAppRecord.has("isConsentMandatory") && StringUtils.isNotBlank(currTermAndConditionAppRecord.optString("isConsentMandatory"))) {
								consentMandatory = currTermAndConditionAppRecord.optString("isConsentMandatory");
							}
							break;
						}
					}
				}
			} else {
				alert.prepareError("Failed to get Terms and Conditions").log();
				ErrorCodeEnum.ERR_20264.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}
			inputBodyMap.clear();
			inputBodyMap.put("tncContentIdRef",termNConditionAppsId);
			
			headerMap = new HashMap<>();
			headerMap.put("backendToken", backendToken);
			headerMap.put("companyid", leId);
			
			String readTermAndConditionTextResponse =
					DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CONSENTMS)
					.withOperationId(OperationName.OP_GET_ACTIVE_TERMS_AND_CONDITIONS_CONSENTS)
					.withRequestParameters(inputBodyMap)
					.withRequestHeaders(headerMap)
					.build()
					.getResponse();
			
			JSONObject readTermAndConditionTextResponseJSON = CommonUtilities
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
					ErrorCodeEnum.ERR_20265.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				}else{
					for(int i=0;i<readTermAndConditionTextJSONArray.length();i++)
					{
						JSONObject _currTermAndConditionTextRecord = readTermAndConditionTextJSONArray
								.getJSONObject(i);
						String language = _currTermAndConditionTextRecord.getString("language");
						if(language.equals(languageCode))
						{
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
							
							String termAndConditionContentId = _currTermAndConditionTextRecord.getString("termsNConditionContentId");
							Param contentId_Param = new Param("contentId", termAndConditionContentId,
									FabricConstants.STRING);
							result.addParam(contentId_Param);
							Param termAndConditonCodeId = new Param("termAndConditonCodeId", termAndConditonId,
									FabricConstants.STRING);
							result.addParam(termAndConditonCodeId);
							Param retentionPeriodd = new Param("retentionPeriod", retentionPeriod,
									FabricConstants.STRING);
							result.addParam(retentionPeriodd);
							Param consentTypeIdd = new Param("consentType", consentTypeId,
									FabricConstants.STRING);
							result.addParam(consentTypeIdd);
							Param consentMandatory_Param = new Param("isConsentMandatory", consentMandatory,
									FabricConstants.STRING);
							result.addParam(consentMandatory_Param);
							result.addParam(new Param("status", "Success", FabricConstants.STRING));
							return result;
						} else if(language.equals(DEFAULT_LANGUAGE_CODE)) {							
							Param _currTermAndConditionContentParam = new Param("termsAndConditionsContent",
									_currTermAndConditionTextRecord.optString("content"), FabricConstants.STRING);
							tempResult.addParam(_currTermAndConditionContentParam);
							String termAndConditionContentTypeId = _currTermAndConditionTextRecord
									.getString("contentType");
							Param contentTypeId_Param = new Param("contentTypeId", termAndConditionContentTypeId,
									FabricConstants.STRING);
							tempResult.addParam(contentTypeId_Param);
							String termAndConditionVersionId = _currTermAndConditionTextRecord.getString("versionNo");
							Param versionId_Param = new Param("versionId", termAndConditionVersionId,
									FabricConstants.STRING);
							tempResult.addParam(versionId_Param);
							String termAndConditionContentId = _currTermAndConditionTextRecord.getString("termsNConditionContentId");
							Param contentId_Param = new Param("contentId", termAndConditionContentId,
									FabricConstants.STRING);
							tempResult.addParam(contentId_Param);
							String contentModifiedOn = _currTermAndConditionTextRecord.getString("lastModifiedDate");
							Param contentModifiedOn_Param = new Param("contentModifiedOn", contentModifiedOn,
									FabricConstants.STRING);
							tempResult.addParam(contentModifiedOn_Param);
							Param termAndConditonCodeId = new Param("termAndConditonCodeId", termAndConditonId,
									FabricConstants.STRING);
							tempResult.addParam(termAndConditonCodeId);
							Param retentionPeriodd = new Param("retentionPeriod", retentionPeriod,
									FabricConstants.STRING);
							tempResult.addParam(retentionPeriodd);
							Param consentTypeIdd = new Param("consentType", consentTypeId,
									FabricConstants.STRING);
							tempResult.addParam(consentTypeIdd);
							Param consentMandatory_Param = new Param("isConsentMandatory", consentMandatory,
									FabricConstants.STRING);
							result.addParam(consentMandatory_Param);
							tempResult.addParam(new Param("status", "Success", FabricConstants.STRING));						
						}
					}
					return tempResult;		
				}
			}
		} catch (Exception e) {
			alert.prepareError("Failed to get Terms and Conditions text. Exception: ", e).log();
			result.addParam(new Param("status", "Failure", FabricConstants.STRING));
			ErrorCodeEnum.ERR_20265.setErrorCode(result);
		}		
		return result;
	}

	@Override
	public Result editTermsAndConditions(Map<String, Object> postParametersMap, String backendToken)
			throws DBPApplicationException {

		Result result = new Result();

		try {
			// Fetching T&C id from termandcondition table
			String termAndConditionId = null, termAndConditionTitle = null, termAndConditionDescription = null;
			String consentTypeId = null, retentionPeriod = null;
			JSONObject currTermAndConditionRecord;
			String termAndConditionCode = postParametersMap.get("termAndConditionCode").toString();
			String leId = (String) postParametersMap.get("legalEntityId");
			//{{HTTP-Protocol}}://{{Host-URL}}/ms-consent-api/api/v2.0.0/reference/termsNConditionsCodes?termNConditionCode=DBX_Default_TnC

			Map<String, Object> inputBodyMap = new HashMap<String, Object>();
			inputBodyMap.put("tncCodeIdRef",termAndConditionCode);
			
			Map<String, Object> headerMap = new HashMap<>();
			headerMap.put("backendToken", backendToken);
			headerMap.put("companyid", leId);
			
			String readTermAndConditionResponse =
					DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CONSENTMS)
					.withOperationId(OperationName.OP_GET_TERMS_AND_CONDITIONS_CODE)
					.withRequestParameters(inputBodyMap)
					.withRequestHeaders(headerMap)
					.build()
					.getResponse();
			
			JSONObject readTermAndConditionResponseJSON = CommonUtilities
					.getStringAsJSONObject(readTermAndConditionResponse);
			if (readTermAndConditionResponseJSON != null
					&& readTermAndConditionResponseJSON.has(FabricConstants.OPSTATUS)
					&& readTermAndConditionResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readTermAndConditionResponseJSON.has("termsNConditionCodes")) {
				JSONArray readTermAndConditionJSONArray = readTermAndConditionResponseJSON
						.optJSONArray("termsNConditionCodes");
				if (readTermAndConditionJSONArray == null || readTermAndConditionJSONArray.length() < 1) {
					alert.prepareError("Failed to get Terms and Conditions").log();
					ErrorCodeEnum.ERR_20264.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				} else {
					currTermAndConditionRecord = readTermAndConditionJSONArray.getJSONObject(0);
					termAndConditionId = currTermAndConditionRecord.getString("termCondCodeId");
					consentTypeId = currTermAndConditionRecord.getString("consentTypeId");
					retentionPeriod = currTermAndConditionRecord.getString("retentionPeriod");
					JSONArray titles = currTermAndConditionRecord.getJSONArray("titles");
					JSONArray descriptions = currTermAndConditionRecord.getJSONArray("descriptions");
					termAndConditionTitle = titles!=null?titles.getString(0):"";
					termAndConditionDescription = descriptions!=null?descriptions.getString(0):"";					
				}
			} else {
				alert.prepareError("Failed to get Terms and Conditions").log();
				ErrorCodeEnum.ERR_20264.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}

			inputBodyMap.clear();
			termAndConditionTitle =postParametersMap.get("termAndConditionTitle").toString();
			termAndConditionDescription =postParametersMap.get("termAndConditionDescription").toString();
			inputBodyMap.put("tncCodeRef",termAndConditionId);
			inputBodyMap.put("termConditionCode", termAndConditionCode);
			inputBodyMap.put("consentType", consentTypeId);
			inputBodyMap.put("retentionPeriod", retentionPeriod);			
			//			JSONArray titleArray = new JSONArray();
			//			titleArray.put(termAndConditionTitle);
			//			JSONArray descriptionArray = new JSONArray();
			//			descriptionArray.put(termAndConditionDescription);
			inputBodyMap.put("title", termAndConditionTitle);			
			inputBodyMap.put("description", termAndConditionDescription);
			
			headerMap = new HashMap<>();
			headerMap.put("backendToken", backendToken);
			headerMap.put("companyid", leId);
			
			String editTermAndConditionResponse =
					DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CONSENTMS)
					.withOperationId(OperationName.OP_UPDATE_TERMS_AND_CONDITIONS_CODE)
					.withRequestParameters(inputBodyMap)
					.withRequestHeaders(headerMap)
					.build()
					.getResponse();

			JSONObject editTermAndConditionResponseJSON = CommonUtilities
					.getStringAsJSONObject(editTermAndConditionResponse);
			if (!(editTermAndConditionResponseJSON != null
					&& editTermAndConditionResponseJSON.has(FabricConstants.OPSTATUS)
					&& editTermAndConditionResponseJSON.getInt(FabricConstants.OPSTATUS) == 0)) {
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				ErrorCodeEnum.ERR_20269.setErrorCode(result);
				alert.prepareError("Failed to Edit Terms and Conditions").log();
				return result;
			}


			result.addParam(new Param("status", "Success", FabricConstants.STRING));

		} catch (Exception e) {
			alert.prepareError("Failed to Edit Terms and Conditions text").log();
			ErrorCodeEnum.ERR_20271.setErrorCode(result);			
			return result;
		}
		return result;
	}
	@Override
	public Result getAllTermsAndConditions(Map<String, Object> postParametersMap, String backendToken)
			throws DBPApplicationException {

		Result result = new Result();

		try {

			// Fetching appId and appName from app table
			Map<String, Object> inputBodyMap = new HashMap<String, Object>();
			Map<String, String> appIdNameMap = new HashMap<String, String>();
			String leId = (String) postParametersMap.get("legalEntityId");

			Map<String, Object> headerMap = new HashMap<>();
			headerMap.put("backendToken", backendToken);
			headerMap.put("companyid", leId);
			
			String readAppResponse =
					DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CONSENTMS)
					.withOperationId(OperationName.OP_GET_INFINITY_APPS)
					.withRequestParameters(inputBodyMap)
					.withRequestHeaders(headerMap)
					.build()
					.getResponse();
			
			JSONObject readAppResponseJSON = CommonUtilities.getStringAsJSONObject(readAppResponse);
			if (readAppResponseJSON != null && readAppResponseJSON.has(FabricConstants.OPSTATUS)
					&& readAppResponseJSON.getInt(FabricConstants.OPSTATUS) == 0 && readAppResponseJSON.has("infinityApps")) {
				JSONArray readAppJSONArray = readAppResponseJSON.optJSONArray("infinityApps");
				if ((readAppJSONArray != null) && (readAppJSONArray.length() > 0)) {
					for (int indexVar = 0; indexVar < readAppJSONArray.length(); indexVar++) {
						JSONObject appJSONObject = readAppJSONArray.getJSONObject(indexVar);
						String appId = appJSONObject.getString("appId");
						JSONArray names = appJSONObject.getJSONArray("names");
						appIdNameMap.put(appId, names.getString(0));
					}
				}
			}

			// Fetching contentTypeId and contentTypeName from contenttype table
			inputBodyMap.clear();
			Map<String, String> contentTypeIdNameMap = new HashMap<String, String>();
			inputBodyMap.put(ODataQueryConstants.SELECT, "id, Name");
			//			String readContentTypeResponse = Executor.invokeService(ServiceURLEnum.CONTENTTYPE_READ, inputBodyMap, null,
			//					requestInstance);

			//TODO:: Confirm if contenttype and locale need to be fetched from consent ms
			String readContentTypeResponse =
					DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
					.withOperationId(OperationName.DB_CONTENTTYPE_GET).withRequestParameters(inputBodyMap).build()
					.getResponse();

			JSONObject readContentTypeResponseJSON = CommonUtilities.getStringAsJSONObject(readContentTypeResponse);
			if (readContentTypeResponseJSON != null && readContentTypeResponseJSON.has(FabricConstants.OPSTATUS)
					&& readContentTypeResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readContentTypeResponseJSON.has("contenttype")) {
				JSONArray readContentTypeJSONArray = readContentTypeResponseJSON.optJSONArray("contenttype");
				if ((readContentTypeJSONArray != null) && (readContentTypeJSONArray.length() > 0)) {
					for (int indexVar = 0; indexVar < readContentTypeJSONArray.length(); indexVar++) {
						JSONObject contentTypeJSONObject = readContentTypeJSONArray.getJSONObject(indexVar);
						String contentTypeId = contentTypeJSONObject.getString("id");
						String contentTypeName = contentTypeJSONObject.getString("Name");
						contentTypeIdNameMap.put(contentTypeId, contentTypeName);
					}
				}
			}

			// Fetching languageId and languageName from locale table
			inputBodyMap.clear();
			Map<String, String> languageIdNameMap = new HashMap<String, String>();
			inputBodyMap.put(ODataQueryConstants.SELECT, "Code, Language");			
			String readlanguageResponse =
					DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
					.withOperationId(OperationName.DB_LOCALE_GET).withRequestParameters(inputBodyMap).build()
					.getResponse();

			JSONObject readlanguageResponseJSON = CommonUtilities.getStringAsJSONObject(readlanguageResponse);
			if (readlanguageResponseJSON != null && readlanguageResponseJSON.has(FabricConstants.OPSTATUS)
					&& readlanguageResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readlanguageResponseJSON.has("locale")) {
				JSONArray readlanguageJSONArray = readlanguageResponseJSON.optJSONArray("locale");
				if ((readlanguageJSONArray != null) && (readlanguageJSONArray.length() > 0)) {
					for (int indexVar = 0; indexVar < readlanguageJSONArray.length(); indexVar++) {
						JSONObject languageJSONObject = readlanguageJSONArray.getJSONObject(indexVar);
						String languageId = languageJSONObject.getString("Code");
						String languageName = languageJSONObject.getString("Language");						
						languageIdNameMap.put(languageId, languageName);

					}
				}
			}

			// Fetching T&C id, Code, Title, Description, ContentModifiedBy and
			// ContentModifiedOn from termandcondition table
			String termAndConditionId = null, termAndConditionCode = null, termAndConditionTitle = null,
					termAndConditionDescription = null;
			Set<String> tncIds = new HashSet<>();
			Map<String,JSONObject> idToTnc = new HashMap<>();
			Map<String,Dataset> idToAppPreference = new HashMap<>();
			Map<String,String> idToTncAppIds = new HashMap<>();
			Map<String,String> idToApps = new HashMap<>();
			
			headerMap = new HashMap<>();
			headerMap.put("backendToken", backendToken);
			headerMap.put("companyid", leId);
			
			String readTermAndConditionResponse =
					DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CONSENTMS)
					.withOperationId(OperationName.OP_GET_TERMS_AND_CONDITIONS_CODE)
					.withRequestParameters(inputBodyMap)
					.withRequestHeaders(headerMap)
					.build()
					.getResponse();
			
			JSONObject readTermAndConditionResponseJSON = CommonUtilities
					.getStringAsJSONObject(readTermAndConditionResponse);
			if (readTermAndConditionResponseJSON != null
					&& readTermAndConditionResponseJSON.has(FabricConstants.OPSTATUS)
					&& readTermAndConditionResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readTermAndConditionResponseJSON.has("termsNConditionCodes")) {
				JSONArray readTermAndConditionJSONArray = readTermAndConditionResponseJSON
						.optJSONArray("termsNConditionCodes");
				if (readTermAndConditionJSONArray == null || readTermAndConditionJSONArray.length() < 1) {
					alert.prepareError("Failed to get Terms and Conditions").log();
					ErrorCodeEnum.ERR_20264.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				} else {
					JSONObject termAndConditionJSONObject = null;

					for (int indexVar = 0; indexVar < readTermAndConditionJSONArray.length(); indexVar++) {
						termAndConditionJSONObject = readTermAndConditionJSONArray.getJSONObject(indexVar);
						termAndConditionId = termAndConditionJSONObject.getString("termCondCodeId");						
						tncIds.add(termAndConditionId);
						idToTnc.put(termAndConditionId, termAndConditionJSONObject);
					}
				}
			}

			//Fetch term and condition id  to apps mapping
			inputBodyMap.clear();
			
			headerMap = new HashMap<>();
			headerMap.put("backendToken", backendToken);
			headerMap.put("companyid", leId);
			
			String readTermAndConditionAppsResponse =
					DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CONSENTMS)
					.withOperationId(OperationName.OP_GET_TERMS_AND_CONDITIONS_APPS)
					.withRequestParameters(inputBodyMap)
					.withRequestHeaders(headerMap)
					.build()
					.getResponse();
			
			JSONObject readTermAndConditionAppsResponseJSON = CommonUtilities
					.getStringAsJSONObject(readTermAndConditionAppsResponse);
			if (readTermAndConditionAppsResponseJSON != null
					&& readTermAndConditionAppsResponseJSON.has(FabricConstants.OPSTATUS)
					&& readTermAndConditionAppsResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readTermAndConditionAppsResponseJSON.has("termNConditionsApps")) {
				JSONArray readTermAndConditionAppsJSONArray = readTermAndConditionAppsResponseJSON
						.optJSONArray("termNConditionsApps");
				if (readTermAndConditionAppsJSONArray == null || readTermAndConditionAppsJSONArray.length() < 1) {
					alert.prepareError("Failed to get Terms and Conditions").log();
					ErrorCodeEnum.ERR_20264.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				} else {
					for (int i=0;i<readTermAndConditionAppsJSONArray.length();i++)
					{
						JSONObject currTermAndConditionAppRecord = readTermAndConditionAppsJSONArray.getJSONObject(i);
						String termConditionCodeId = currTermAndConditionAppRecord.getString("termConditionCodeId");
						String termNConditionappId = currTermAndConditionAppRecord.getString("termNConditionAppsId");
						String appId = currTermAndConditionAppRecord.getString("appId");
						idToTncAppIds.put(termConditionCodeId, termNConditionappId);
						idToApps.put(termConditionCodeId,appId);
						Dataset appPreferencesDataset = idToAppPreference.get(termConditionCodeId);
						if(appPreferencesDataset == null)
						{
							appPreferencesDataset = new Dataset();
							appPreferencesDataset.setId("appPreferences");
						}
						if ((appIdNameMap != null) && (!appIdNameMap.isEmpty())) {
							String app = (String) appIdNameMap.get(appId);											
							Record appRecord = new Record();
							Param appId_Param = new Param("appId", appId, FabricConstants.STRING);
							appRecord.addParam(appId_Param);
							Param appName_Param = new Param("appName", appIdNameMap.get(appId),
									FabricConstants.STRING);
							appRecord.addParam(appName_Param);
							Param isSupported_Param = new Param("isSupported", "true", FabricConstants.STRING);
							appRecord.addParam(isSupported_Param);
							appPreferencesDataset.addRecord(appRecord);

						}
						idToAppPreference.put(termConditionCodeId, appPreferencesDataset);
					}
				}
			} else {
				alert.prepareError("Failed to get Terms and Conditions").log();
				ErrorCodeEnum.ERR_20264.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}

			headerMap = new HashMap<>();
			headerMap.put("backendToken", backendToken);
			headerMap.put("companyid", leId);
			
			String readTermAndConditionTextResponse =
					DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CONSENTMS)
					.withOperationId(OperationName.OP_GET_TERMS_AND_CONDITIONS_CONTENTS)
					.withRequestHeaders(headerMap)
					.withRequestParameters(inputBodyMap)
					.build()
					.getResponse();
			
			JSONObject readTermAndConditionTextResponseJSON = CommonUtilities
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
					ErrorCodeEnum.ERR_20265.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				}else{
					Set<String> keysWithDefaultLanguage = new HashSet<String>();
					Map<String,Dataset> keyToContentDataset = new HashMap<>();
					Map<String,Record> keyToActiveContentRecord = new HashMap<>();
					for(int j=0;j<readTermAndConditionTextJSONArray.length();j++)
					{

						JSONObject _currTermAndConditionTextRecord = readTermAndConditionTextJSONArray
								.getJSONObject(j);
						String language = _currTermAndConditionTextRecord.getString("language");
						String termConditionAppsId = _currTermAndConditionTextRecord.getString("termConditionAppsId");
						String termConditionCodeId = _currTermAndConditionTextRecord.getString("termConditionCodeId");

						String key = termConditionCodeId+"-"+termConditionAppsId;						
						if(language.equals(DEFAULT_LANGUAGE_CODE))
						{
							keysWithDefaultLanguage.add(key);
						}
						Dataset termsAndConditionsVersionDataset = keyToContentDataset.get(key);
						Record termsAndConditionsActiveContentRecord = keyToActiveContentRecord.get(key);
						if(termsAndConditionsVersionDataset == null)
						{
							termsAndConditionsVersionDataset = new Dataset();
							termsAndConditionsVersionDataset.setId("termsAndConditionsVersion");
						}
						if(termsAndConditionsActiveContentRecord == null)
						{
							termsAndConditionsActiveContentRecord = new Record();
							termsAndConditionsActiveContentRecord.setId("termsAndConditionsContent");
						}
						String languageCode = _currTermAndConditionTextRecord.optString("language");
						String termAndConditionContent = _currTermAndConditionTextRecord
								.optString("content");
						String termAndConditionContentTypeId = _currTermAndConditionTextRecord
								.optString("contentType");
						String termsAndConditionsContentModifiedBy = "admin";							
						String contentModifiedOn = _currTermAndConditionTextRecord.optString("lastModifiedDate");
						String termsAndConditionsStatusId = _currTermAndConditionTextRecord
								.optString("status");
						String termsAndConditionsVersionId = null;
						if(!termsAndConditionsStatusId.equals("DRAFT"))
						{
							termsAndConditionsVersionId = _currTermAndConditionTextRecord
								.optString("versionNo");
						}
						String termsAndConditionsVersionDescription = _currTermAndConditionTextRecord
								.optString("description");

						String termAndConditionContentTypeName = null;
						if (contentTypeIdNameMap.containsKey(termAndConditionContentTypeId))
							termAndConditionContentTypeName = contentTypeIdNameMap
							.get(termAndConditionContentTypeId);
						String languageName = null;
						if (languageIdNameMap.containsKey(languageCode))
							languageName = languageIdNameMap.get(languageCode);

						Record contentLangRecord = new Record();
						contentLangRecord.setId(languageCode);
						Param content_Param = new Param("content", termAndConditionContent,
								FabricConstants.STRING);
						contentLangRecord.addParam(content_Param);
						Param contentTypeId_Param = new Param("contentTypeId",
								termAndConditionContentTypeId, FabricConstants.STRING);
						contentLangRecord.addParam(contentTypeId_Param);
						Param contentType_Param = new Param("contentTypeName",
								termAndConditionContentTypeName, FabricConstants.STRING);
						contentLangRecord.addParam(contentType_Param);
						Param languageCode_Param = new Param("languageCode", languageCode,
								FabricConstants.STRING);
						contentLangRecord.addParam(languageCode_Param);
						Param languageName_Param = new Param("languageName", languageName,
								FabricConstants.STRING);
						contentLangRecord.addParam(languageName_Param);
						Param contentModifiedBy_Param = new Param("contentModifiedBy",
								termsAndConditionsContentModifiedBy, FabricConstants.STRING);
						contentLangRecord.addParam(contentModifiedBy_Param);
						Param contentModifiedOn_Param = new Param("contentModifiedOn", contentModifiedOn,
								FabricConstants.STRING);
						contentLangRecord.addParam(contentModifiedOn_Param);
						Param statusId_Param = new Param("statusId", termsAndConditionsStatusId,
								FabricConstants.STRING);
						contentLangRecord.addParam(statusId_Param);
						if(!termsAndConditionsStatusId.equals("DRAFT"))
						{
						Param versionId_Param = new Param("versionId", termsAndConditionsVersionId,
								FabricConstants.STRING);
						contentLangRecord.addParam(versionId_Param);
						}
						Param versionDescription_Param = new Param("versionDescription",
								termsAndConditionsVersionDescription, FabricConstants.STRING);
						contentLangRecord.addParam(versionDescription_Param);
						termsAndConditionsVersionDataset.addRecord(contentLangRecord);
						if(termsAndConditionsStatusId.equals(TANDC_MS_ACTIVE_STATUS_ID))
						{
							termsAndConditionsActiveContentRecord.addRecord(contentLangRecord);
							keyToActiveContentRecord.put(key, termsAndConditionsActiveContentRecord);
						}
						keyToContentDataset.put(key, termsAndConditionsVersionDataset);

					}
					Dataset finalDataset = new Dataset("termsAndConditions");
					Map<String,Dataset> codeToAppDataset = new HashMap<>();
					Map<String,Record> codeToTncRecord = new HashMap<>();
					for(int j=0;j<readTermAndConditionTextJSONArray.length();j++)
					{
						JSONObject termAndConditionTextJSONObject = readTermAndConditionTextJSONArray
								.getJSONObject(j);
						//						Record currTncRecord = new Record();
						Record appRecord = new Record();
						String appId = termAndConditionTextJSONObject.getString("appId");						

						String termConditionCodeId = termAndConditionTextJSONObject.getString("termConditionCodeId");
						String termConditionAppsId = termAndConditionTextJSONObject.getString("termConditionAppsId");						
						String language = termAndConditionTextJSONObject.getString("language");
						String key = termConditionCodeId+"-"+termConditionAppsId;
						String termsAndConditionsStatusId = termAndConditionTextJSONObject
								.optString("status");


						if(keysWithDefaultLanguage.contains(key) && language.equals(DEFAULT_LANGUAGE_CODE))
						{
							appRecord.addParam(new Param("appId", appId,FabricConstants.STRING));
							appRecord.addDataset(keyToContentDataset.get(key));
							JSONObject tncJson = idToTnc.get(termConditionCodeId);
							String id = tncJson.optString("termCondCodeId");
							String code = tncJson.optString("termConditionCode");
							String consentTypeId = tncJson.optString("consentTypeId");
							String retentionPeriod = tncJson.optString("retentionPeriod");
							JSONArray titles = tncJson.getJSONArray("titles");
							JSONArray descriptions = tncJson.getJSONArray("descriptions");
							termAndConditionTitle = titles!=null?titles.optString(0):"";
							termAndConditionDescription = descriptions!=null?descriptions.optString(0):"";
							Record currTncRecord = codeToTncRecord.get(termConditionCodeId);
							if(currTncRecord == null)
							{
								currTncRecord = new Record();						
							}
							currTncRecord.addParam(new Param("id", id,FabricConstants.STRING));							
							currTncRecord.addParam(new Param("code", code,FabricConstants.STRING));
							currTncRecord.addParam(new Param("consentTypeId", consentTypeId,FabricConstants.STRING));
							currTncRecord.addParam(new Param("retentionPeriod", retentionPeriod,FabricConstants.STRING));
							currTncRecord.addParam(new Param("title", termAndConditionTitle,FabricConstants.STRING));
							currTncRecord.addParam(new Param("description", termAndConditionDescription,FabricConstants.STRING));

							String languageCode = termAndConditionTextJSONObject.optString("language");
							String termAndConditionContentTypeId = termAndConditionTextJSONObject.optString("contentType");
							String termAndConditionContentTypeName = null;
							if (contentTypeIdNameMap.containsKey(termAndConditionContentTypeId)){
								termAndConditionContentTypeName = contentTypeIdNameMap.get(termAndConditionContentTypeId);
								String languageName = null;
								if (languageIdNameMap.containsKey(languageCode))
									languageName = languageIdNameMap.get(languageCode);
								appRecord.addRecord(keyToActiveContentRecord.get(key));
								if(termsAndConditionsStatusId.equals(TANDC_MS_ACTIVE_STATUS_ID)){
									Dataset appDataset = codeToAppDataset.get(termConditionCodeId);
									if(appDataset == null)
									{
										appDataset = new Dataset();
										appDataset.setId("apps");
									}
									appDataset.addRecord(appRecord);
									codeToAppDataset.put(termConditionCodeId, appDataset);
									if(codeToTncRecord.get(termConditionCodeId) == null)
									{
										currTncRecord.addDataset(idToAppPreference.get(termConditionCodeId));										
										currTncRecord.addDataset(appDataset);										
										codeToTncRecord.put(termConditionCodeId, currTncRecord);
										finalDataset.addRecord(currTncRecord);
									}
								}
							}

						}

					}
					result.addDataset(finalDataset);
				}
			}
			result.addParam(new Param("status", "Success", FabricConstants.STRING));
			return result;
		} catch (Exception e) {
			alert.prepareError("Failed to get Terms and Conditions text. Exception: ", e).log();
			result.addParam(new Param("status", "Failure", FabricConstants.STRING));
			ErrorCodeEnum.ERR_20265.setErrorCode(result);
		}
		return result;
	}
	
	
	@Override
	public Result getAllTermsAndConditions(Map<String, Object> postParametersMap, String backendToken,
			DataControllerRequest requestInstance, boolean isDBXDBIntegrated) throws DBPApplicationException {

		Result result = new Result();

		try {
			// Fetching appId and appName from app table
			Map<String, String> inputBodyMap = new HashMap<String, String>();
			Map<String, String> appIdNameMap = new HashMap<String, String>();
			inputBodyMap.put(ODataQueryConstants.SELECT, "id, Name");
			String readAppResponse = Executor.invokeService(ServiceURLEnum.APP_READ, inputBodyMap, null,
					requestInstance);
			JSONObject readAppResponseJSON = CommonUtilities.getStringAsJSONObject(readAppResponse);
			if (readAppResponseJSON != null && readAppResponseJSON.has(FabricConstants.OPSTATUS)
					&& readAppResponseJSON.getInt(FabricConstants.OPSTATUS) == 0 && readAppResponseJSON.has("app")) {
				JSONArray readAppJSONArray = readAppResponseJSON.optJSONArray("app");
				if ((readAppJSONArray != null) && (readAppJSONArray.length() > 0)) {
					for (int indexVar = 0; indexVar < readAppJSONArray.length(); indexVar++) {
						JSONObject appJSONObject = readAppJSONArray.getJSONObject(indexVar);
						String appId = appJSONObject.getString("id");
						String appName = appJSONObject.getString("Name");
						appIdNameMap.put(appId, appName);
					}
				}
			}

			// Fetching contentTypeId and contentTypeName from contenttype table
			inputBodyMap.clear();
			Map<String, String> contentTypeIdNameMap = new HashMap<String, String>();
			inputBodyMap.put(ODataQueryConstants.SELECT, "id, Name");
			String readContentTypeResponse = Executor.invokeService(ServiceURLEnum.CONTENTTYPE_READ, inputBodyMap, null,
					requestInstance);
			JSONObject readContentTypeResponseJSON = CommonUtilities.getStringAsJSONObject(readContentTypeResponse);
			if (readContentTypeResponseJSON != null && readContentTypeResponseJSON.has(FabricConstants.OPSTATUS)
					&& readContentTypeResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readContentTypeResponseJSON.has("contenttype")) {
				JSONArray readContentTypeJSONArray = readContentTypeResponseJSON.optJSONArray("contenttype");
				if ((readContentTypeJSONArray != null) && (readContentTypeJSONArray.length() > 0)) {
					for (int indexVar = 0; indexVar < readContentTypeJSONArray.length(); indexVar++) {
						JSONObject contentTypeJSONObject = readContentTypeJSONArray.getJSONObject(indexVar);
						String contentTypeId = contentTypeJSONObject.getString("id");
						String contentTypeName = contentTypeJSONObject.getString("Name");
						contentTypeIdNameMap.put(contentTypeId, contentTypeName);
					}
				}
			}

			// Fetching languageId and languageName from locale table
			inputBodyMap.clear();
			Map<String, String> languageIdNameMap = new HashMap<String, String>();
			inputBodyMap.put(ODataQueryConstants.SELECT, "Code, Language");
			String readlanguageResponse = Executor.invokeService(ServiceURLEnum.LOCALE_READ, inputBodyMap, null,
					requestInstance);
			JSONObject readlanguageResponseJSON = CommonUtilities.getStringAsJSONObject(readlanguageResponse);
			if (readlanguageResponseJSON != null && readlanguageResponseJSON.has(FabricConstants.OPSTATUS)
					&& readlanguageResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readlanguageResponseJSON.has("locale")) {
				JSONArray readlanguageJSONArray = readlanguageResponseJSON.optJSONArray("locale");
				if ((readlanguageJSONArray != null) && (readlanguageJSONArray.length() > 0)) {
					for (int indexVar = 0; indexVar < readlanguageJSONArray.length(); indexVar++) {
						JSONObject languageJSONObject = readlanguageJSONArray.getJSONObject(indexVar);
						String languageId = languageJSONObject.getString("Code");
						String languageName = languageJSONObject.getString("Language");
						languageIdNameMap.put(languageId, languageName);
					}
				}
			}

			// Fetching T&C id, Code, Title, Description, ContentModifiedBy and
			// ContentModifiedOn from termandcondition table
			String termAndConditionId = null, termAndConditionCode = null, termAndConditionTitle = null,
					termAndConditionDescription = null;
			inputBodyMap.clear();
			inputBodyMap.put(ODataQueryConstants.SELECT, "id, Code, Title, Description");
			String readTermAndConditionResponse = Executor.invokeService(ServiceURLEnum.TERMANDCONDITION_READ,
					inputBodyMap, null, requestInstance);
			JSONObject readTermAndConditionResponseJSON = CommonUtilities
					.getStringAsJSONObject(readTermAndConditionResponse);
			if (readTermAndConditionResponseJSON != null
					&& readTermAndConditionResponseJSON.has(FabricConstants.OPSTATUS)
					&& readTermAndConditionResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readTermAndConditionResponseJSON.has("termandcondition")) {
				JSONArray readTermAndConditionJSONArray = readTermAndConditionResponseJSON
						.optJSONArray("termandcondition");
				if ((readTermAndConditionJSONArray != null) && (readTermAndConditionJSONArray.length() > 0)) {
					Dataset termsAndConditionsDataset = new Dataset();
					termsAndConditionsDataset.setId("termsAndConditions");
					JSONObject termAndConditionJSONObject = null;
					for (int indexVar = 0; indexVar < readTermAndConditionJSONArray.length(); indexVar++) {
						termAndConditionJSONObject = readTermAndConditionJSONArray.getJSONObject(indexVar);
						Record currRecord = new Record();
						termAndConditionId = termAndConditionJSONObject.getString("id");
						termAndConditionCode = termAndConditionJSONObject.getString("Code");
						termAndConditionTitle = termAndConditionJSONObject.getString("Title");
						termAndConditionDescription = termAndConditionJSONObject.getString("Description");

						// Skipping records that does not have content for default language
						inputBodyMap.clear();
						inputBodyMap.put(ODataQueryConstants.FILTER,
								"TermAndConditionId eq '" + termAndConditionId + "' and LanguageCode eq '"
										+ DEFAULT_LANGUAGE_CODE + "' and Status_id eq '" + TANDC_ACTIVE_STATUS_ID + "'");
						inputBodyMap.put(ODataQueryConstants.SELECT, "Content");
						String _readTermAndConditionTextResponse = Executor.invokeService(
								ServiceURLEnum.TERMANDCONDITIONTEXT_READ, inputBodyMap, null, requestInstance);
						JSONObject _readTermAndConditionTextResponseJSON = CommonUtilities
								.getStringAsJSONObject(_readTermAndConditionTextResponse);
						if (_readTermAndConditionTextResponseJSON != null
								&& _readTermAndConditionTextResponseJSON.has(FabricConstants.OPSTATUS)
								&& _readTermAndConditionTextResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
								&& _readTermAndConditionTextResponseJSON.has("termandconditiontext")) {
							JSONArray _readTermAndConditionTextJSONArray = _readTermAndConditionTextResponseJSON
									.optJSONArray("termandconditiontext");
							if ((_readTermAndConditionTextJSONArray != null)
									&& (_readTermAndConditionTextJSONArray.length() > 0)) {
								String content = _readTermAndConditionTextJSONArray.getJSONObject(0)
										.optString("Content");
								if (StringUtils.isEmpty(content))
									continue;
							}
						}

						Param termAndConditionCode_Param = new Param("code", termAndConditionCode,
								FabricConstants.STRING);
						currRecord.addParam(termAndConditionCode_Param);
						Param termAndConditionTitle_Param = new Param("title", termAndConditionTitle,
								FabricConstants.STRING);
						currRecord.addParam(termAndConditionTitle_Param);
						Param termAndConditionDescription_Param = new Param("description", termAndConditionDescription,
								FabricConstants.STRING);
						currRecord.addParam(termAndConditionDescription_Param);

						// Fetch supportedApps from termandconditionapp
						Set<String> supportedAppsSet = new HashSet<String>();
						Dataset appPreferencesDataset = new Dataset();
						appPreferencesDataset.setId("appPreferences");
						inputBodyMap.clear();
						inputBodyMap.put(ODataQueryConstants.FILTER, "TermAndConditionId eq '" + termAndConditionId
								+ "'");
						inputBodyMap.put(ODataQueryConstants.SELECT, "AppId");
						String readTermAndConditionAppResponse = Executor.invokeService(
								ServiceURLEnum.TERMANDCONDITIONAPP_READ, inputBodyMap, null, requestInstance);
						JSONObject readTermAndConditionAppResponseJSON = CommonUtilities
								.getStringAsJSONObject(readTermAndConditionAppResponse);
						if (readTermAndConditionAppResponseJSON != null
								&& readTermAndConditionAppResponseJSON.has(FabricConstants.OPSTATUS)
								&& readTermAndConditionAppResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
								&& readTermAndConditionAppResponseJSON.has("termandconditionapp")) {
							JSONArray readTermAndConditionAppResponseJSONArray = readTermAndConditionAppResponseJSON
									.optJSONArray("termandconditionapp");
							if ((readTermAndConditionAppResponseJSONArray != null)
									&& (readTermAndConditionAppResponseJSONArray.length() > 0)) {
								for (int index = 0; index < readTermAndConditionAppResponseJSONArray
										.length(); index++) {
									// Adding supported apps records
									String appId = readTermAndConditionAppResponseJSONArray.getJSONObject(index)
											.optString("AppId");
									Record appRecord = new Record();
									Param appId_Param = new Param("appId", appId, FabricConstants.STRING);
									appRecord.addParam(appId_Param);
									if (appIdNameMap.containsKey(appId)) {
										Param appName_Param = new Param("appName", appIdNameMap.get(appId),
												FabricConstants.STRING);
										appRecord.addParam(appName_Param);
									}
									Param isSupported_Param = new Param("isSupported", "true", FabricConstants.STRING);
									appRecord.addParam(isSupported_Param);
									supportedAppsSet.add(appId);
									appPreferencesDataset.addRecord(appRecord);
								}
							}
						}

						// Adding unsupported apps record
						if ((appIdNameMap != null) && (!appIdNameMap.isEmpty())) {
							for (Map.Entry<String, String> appElement : appIdNameMap.entrySet()) {
								String appId = (String) appElement.getKey();
								if (!supportedAppsSet.contains(appId)) {
									Record appRecord = new Record();
									Param appId_Param = new Param("appId", appId, FabricConstants.STRING);
									appRecord.addParam(appId_Param);
									Param appName_Param = new Param("appName", appIdNameMap.get(appId),
											FabricConstants.STRING);
									appRecord.addParam(appName_Param);
									Param isSupported_Param = new Param("isSupported", "false", FabricConstants.STRING);
									appRecord.addParam(isSupported_Param);
									appPreferencesDataset.addRecord(appRecord);
								}
							}
						}

						currRecord.addDataset(appPreferencesDataset);

						// For backward compatibilty we need below 2 param from termandconditiontext
						// table
						String contentModifiedByForOneRecord = null, contentModifiedOnForOneRecord = null;

						// Fetch T&C Content, languageCode and T&C content type from
						// termandconditiontext table
						inputBodyMap.clear();
						inputBodyMap.put(ODataQueryConstants.FILTER, "TermAndConditionId eq '" + termAndConditionId
								+ "'");
						inputBodyMap.put(ODataQueryConstants.SELECT,
								"LanguageCode, Content, ContentType_id, ContentModifiedBy, ContentModifiedOn, Status_id, Version_Id, Description");
						String readTermAndConditionTextResponse = Executor.invokeService(
								ServiceURLEnum.TERMANDCONDITIONTEXT_READ, inputBodyMap, null, requestInstance);
						JSONObject readTermAndConditionTextResponseJSON = CommonUtilities
								.getStringAsJSONObject(readTermAndConditionTextResponse);

						if (readTermAndConditionAppResponseJSON.optJSONArray("termandconditionapp").length() > 0) {
							if (readTermAndConditionTextResponseJSON != null
									&& readTermAndConditionTextResponseJSON.has(FabricConstants.OPSTATUS)
									&& readTermAndConditionTextResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
									&& readTermAndConditionTextResponseJSON.has("termandconditiontext")) {
								JSONArray readTermAndConditionTextJSONArray = readTermAndConditionTextResponseJSON
										.optJSONArray("termandconditiontext");

								if ((readTermAndConditionTextJSONArray != null)
										&& (readTermAndConditionTextJSONArray.length() > 0)) {
									Dataset termsAndConditionsVersionDataset = createTermsAndConditionsVersionDataset(
											readTermAndConditionTextJSONArray, contentTypeIdNameMap, languageIdNameMap);
									// Not changing below logic to support backward compatibility
									JSONObject termAndConditionTextJSONObject = null;
									Record termsAndConditionsContentRecords = new Record();
									termsAndConditionsContentRecords.setId("termsAndConditionsContent");
									Record appRecord = new Record();
									for (int index = 0; index < readTermAndConditionTextJSONArray.length(); index++) {
										termAndConditionTextJSONObject = readTermAndConditionTextJSONArray
												.getJSONObject(index);

										if (index == 0) {
											contentModifiedByForOneRecord = readTermAndConditionTextJSONArray
													.getJSONObject(0).getString("ContentModifiedBy");
											contentModifiedOnForOneRecord = readTermAndConditionTextJSONArray
													.getJSONObject(0).getString("ContentModifiedOn");
										}

										String languageCode = termAndConditionTextJSONObject.getString("LanguageCode");
										String termAndConditionContent = termAndConditionTextJSONObject
												.optString("Content");
										String termAndConditionContentTypeId = termAndConditionTextJSONObject
												.getString("ContentType_id");
										String termsAndConditionsContentModifiedBy = termAndConditionTextJSONObject
												.getString("ContentModifiedBy");
										String termsAndConditionsContentModifiedOn = termAndConditionTextJSONObject
												.getString("ContentModifiedOn");
										String termsAndConditionsStatusId = termAndConditionTextJSONObject
												.getString("Status_id");

										if (TANDC_ACTIVE_STATUS_ID
												.equalsIgnoreCase(termAndConditionTextJSONObject.getString("Status_id"))
												|| ACTIVE_STATUS_ID.equalsIgnoreCase(
														termAndConditionTextJSONObject.getString("Status_id"))) {
											termsAndConditionsStatusId = TANDC_MS_ACTIVE_STATUS_ID;
										}

										String termsAndConditionsVersionId = termAndConditionTextJSONObject
												.getString("Version_Id");
										String termsAndConditionsVersionDescription = termAndConditionTextJSONObject
												.optString("Description");

										String termAndConditionContentTypeName = null;
										if (contentTypeIdNameMap.containsKey(termAndConditionContentTypeId))
											termAndConditionContentTypeName = contentTypeIdNameMap
													.get(termAndConditionContentTypeId);
										String languageName = null;
										if (languageIdNameMap.containsKey(languageCode))
											languageName = languageIdNameMap.get(languageCode);

										Record contentLangRecord = new Record();
										contentLangRecord.setId(languageCode);
										Param content_Param = new Param("content", termAndConditionContent,
												FabricConstants.STRING);
										contentLangRecord.addParam(content_Param);
										Param contentTypeId_Param = new Param("contentTypeId",
												termAndConditionContentTypeId, FabricConstants.STRING);
										contentLangRecord.addParam(contentTypeId_Param);
										Param contentType_Param = new Param("contentTypeName",
												termAndConditionContentTypeName, FabricConstants.STRING);
										contentLangRecord.addParam(contentType_Param);
										Param languageCode_Param = new Param("languageCode", languageCode,
												FabricConstants.STRING);
										contentLangRecord.addParam(languageCode_Param);
										Param languageName_Param = new Param("languageName", languageName,
												FabricConstants.STRING);
										contentLangRecord.addParam(languageName_Param);
										Param contentModifiedBy_Param = new Param("contentModifiedBy",
												termsAndConditionsContentModifiedBy, FabricConstants.STRING);
										contentLangRecord.addParam(contentModifiedBy_Param);
										Param contentModifiedOn_Param = new Param("contentModifiedOn",
												termsAndConditionsContentModifiedOn, FabricConstants.STRING);
										contentLangRecord.addParam(contentModifiedOn_Param);
										Param statusId_Param = new Param("statusId", termsAndConditionsStatusId,
												FabricConstants.STRING);
										contentLangRecord.addParam(statusId_Param);
										Param versionId_Param = new Param("versionId", termsAndConditionsVersionId,
												FabricConstants.STRING);
										contentLangRecord.addParam(versionId_Param);
										Param versionDescription_Param = new Param("versionDescription",
												termsAndConditionsVersionDescription, FabricConstants.STRING);
										contentLangRecord.addParam(versionDescription_Param);
										termsAndConditionsContentRecords.addRecord(contentLangRecord);
									}

									appRecord.addRecord(termsAndConditionsContentRecords);
									appRecord.addDataset(termsAndConditionsVersionDataset);
									Dataset appDataset = new Dataset();
									appDataset.setId("apps");
									appDataset.addRecord(appRecord);
									currRecord.addDataset(appDataset);
								}

							} else {
								alert.prepareError("Failed to get Term and Condition text").log();
								ErrorCodeEnum.ERR_20265.setErrorCode(result);
								result.addParam(new Param("status", "Failure", FabricConstants.STRING));
								return result;
							}
						}
						// For Backward compatibility
						if (readTermAndConditionAppResponseJSON.optJSONArray("termandconditionapp").length() > 0) {
							Param termAndConditionContentModifiedBy_Param = new Param("contentModifiedBy",
									contentModifiedByForOneRecord, FabricConstants.STRING);
							currRecord.addParam(termAndConditionContentModifiedBy_Param);
							Param termAndConditionContentModifiedOn_Param = new Param("contentModifiedOn",
									contentModifiedOnForOneRecord, FabricConstants.STRING);
							currRecord.addParam(termAndConditionContentModifiedOn_Param);

							termsAndConditionsDataset.addRecord(currRecord);
						}
					}

					result.addDataset(termsAndConditionsDataset);
				}
			}
			result.addParam(new Param("status", "Success", FabricConstants.STRING));
			return result;

		} catch (Exception e) {
			alert.prepareError("Failed to get Terms and Conditions text. Exception: ", e).log();
			result.addParam(new Param("status", "Failure", FabricConstants.STRING));
			ErrorCodeEnum.ERR_20265.setErrorCode(result);
		}
		return result;
	}

	@Override
	public Result createTermsAndConditionsVersion(Map<String, Object> postParametersMap, String backendToken)
			throws Exception {
		String consentBackend = EnvironmentConfigurationsHandler.getServerAppProperty("CONSENT_BACKEND");
		if (ACConstants.DBXDB_BACKEND.equalsIgnoreCase(consentBackend)) {
			return createTermsAndConditionsVersionDBXDB(postParametersMap, backendToken);
		}
		Result result = new Result();
		try{
			String termAndConditionCode =postParametersMap.get("termAndConditionCode").toString();
			String languageCode =postParametersMap.get("languageCode").toString();
			String contentType =postParametersMap.get("contentType").toString();
			String termAndConditionContent =postParametersMap.get("termAndConditionContent").toString();
			String termAndConditionVersionDescription =postParametersMap.get("termAndConditionVersionDescription").toString();
			String loggedInUserDetailsName =postParametersMap.get("loggedInUserDetailsName").toString();
			String loggedInUserId =postParametersMap.get("loggedInUserId").toString();
			String isSave =postParametersMap.get("isSave").toString();
			String leId =(String) postParametersMap.get("legalEntityId");
			String termAndConditionId = null;

			Map<String,String> idToTncAppIds = new HashMap<>();
			Map<String, Object> inputBodyMap = new HashMap<String, Object>();
			inputBodyMap.put("tncCodeIdRef", termAndConditionCode);	
			
			Map<String, Object> headerMap = new HashMap<>();
	        headerMap.put("backendToken", backendToken);
	        headerMap.put("companyid", leId);
	        
			String readTermAndConditionResponse =
					DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CONSENTMS)
					.withOperationId(OperationName.OP_GET_TERMS_AND_CONDITIONS_CODE)
					.withRequestParameters(inputBodyMap)
					.withRequestHeaders(headerMap)
					.build()
					.getResponse();
			
			JSONObject readTermAndConditionResponseJSON = CommonUtilities
					.getStringAsJSONObject(readTermAndConditionResponse);
			if (readTermAndConditionResponseJSON != null
					&& readTermAndConditionResponseJSON.has(FabricConstants.OPSTATUS)
					&& readTermAndConditionResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readTermAndConditionResponseJSON.has("termsNConditionCodes")) {
				JSONArray readTermAndConditionJSONArray = readTermAndConditionResponseJSON
						.optJSONArray("termsNConditionCodes");
				if (readTermAndConditionJSONArray == null || readTermAndConditionJSONArray.length() < 1) {
					alert.prepareError("Failed to get Terms and Conditions").log();
					ErrorCodeEnum.ERR_20264.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				} else {
					JSONObject termAndConditionJSONObject = null;
					for (int indexVar = 0; indexVar < readTermAndConditionJSONArray.length(); indexVar++) {
						termAndConditionJSONObject = readTermAndConditionJSONArray.getJSONObject(indexVar);
						String code =  termAndConditionJSONObject.getString("termConditionCode");
						String termCondCodeId = termAndConditionJSONObject.getString("termCondCodeId");
						if(code.equals(termAndConditionCode) || termCondCodeId.equals(termAndConditionCode))
						{
							termAndConditionId = termAndConditionJSONObject.getString("termCondCodeId");
							break;
						}						
					}
				}
			}

			//Fetch term and condition id  to apps mapping

			inputBodyMap.clear();
			headerMap = new HashMap<>();
			headerMap.put("backendToken", backendToken);
			headerMap.put("companyid", leId);

			String readTermAndConditionAppsResponse =
					DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CONSENTMS)
					.withOperationId(OperationName.OP_GET_TERMS_AND_CONDITIONS_APPS)
					.withRequestParameters(inputBodyMap)
					.withRequestHeaders(headerMap)
					.build()
					.getResponse();
			
			JSONObject readTermAndConditionAppsResponseJSON = CommonUtilities
					.getStringAsJSONObject(readTermAndConditionAppsResponse);
			if (readTermAndConditionAppsResponseJSON != null
					&& readTermAndConditionAppsResponseJSON.has(FabricConstants.OPSTATUS)
					&& readTermAndConditionAppsResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readTermAndConditionAppsResponseJSON.has("termNConditionsApps")) {
				JSONArray readTermAndConditionAppsJSONArray = readTermAndConditionAppsResponseJSON
						.optJSONArray("termNConditionsApps");
				if (readTermAndConditionAppsJSONArray == null || readTermAndConditionAppsJSONArray.length() < 1) {
					alert.prepareError("Failed to get Terms and Conditions").log();
					ErrorCodeEnum.ERR_20264.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				} else {
					for (int i=0;i<readTermAndConditionAppsJSONArray.length();i++)
					{
						JSONObject currTermAndConditionAppRecord = readTermAndConditionAppsJSONArray.getJSONObject(i);						
						String termConditionCodeId = currTermAndConditionAppRecord.getString("termConditionCodeId");					
						if(termConditionCodeId.equals(termAndConditionId))
						{
							String termNConditionappId = currTermAndConditionAppRecord.getString("termNConditionAppsId");
							idToTncAppIds.put(termConditionCodeId, termNConditionappId);
						}
					}
				}
			} else {
				alert.prepareError("Failed to get Terms and Conditions").log();
				ErrorCodeEnum.ERR_20264.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}
			inputBodyMap.clear();

			for(Entry<String, String> entry:idToTncAppIds.entrySet())
			{
				inputBodyMap.put("tncAppIdRef",entry.getValue());
				inputBodyMap.put("language", languageCode);
				inputBodyMap.put("contentType", contentType);
				inputBodyMap.put("content", termAndConditionContent);
				inputBodyMap.put("description", termAndConditionVersionDescription);				
				inputBodyMap.put("saveAndPublish", !(Boolean.parseBoolean(isSave)));
				
				headerMap = new HashMap<>();
				headerMap.put("backendToken", backendToken);
				headerMap.put("companyid", leId);
				
				String createTermAndConditionTextResponse =
						DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CONSENTMS)
						.withOperationId(OperationName.OP_CREATE_TERM_AND_CONDITIONS_CONTENT)
						.withRequestParameters(inputBodyMap)
						.withRequestHeaders(headerMap)
						.build()
						.getResponse();

				JSONObject createTermAndConditionTextResponseJSON = CommonUtilities
						.getStringAsJSONObject(createTermAndConditionTextResponse);
				if (!(createTermAndConditionTextResponseJSON != null
						&& createTermAndConditionTextResponseJSON.has(FabricConstants.OPSTATUS)
						&& createTermAndConditionTextResponseJSON.getInt(FabricConstants.OPSTATUS) == 0)) {
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					ErrorCodeEnum.ERR_20277.setErrorCode(result);
					alert.prepareError("Failed to create Terms and Conditions version").log();
				}

			}
			result.addParam(new Param("status", "Success", FabricConstants.STRING));
			return result;

		} catch (Exception e) {
			alert.prepareError("Failed to create Terms and Conditions version").log();
			ErrorCodeEnum.ERR_20277.setErrorCode(result);
			return result;
		}
	}

	public Result getTermsAndConditionsDBXDB(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {
		Result result = new Result();

		try {

			String termAndConditionCode = null, languageCode = null, termAndConditonId = null, appId = null;
			termAndConditionCode = postParametersMap.get("termAndConditionCode").toString();
			languageCode = postParametersMap.get("languageCode").toString();
			appId = postParametersMap.get("appId").toString();
			//{{HTTP-Protocol}}://{{Host-URL}}/ms-consent-api/api/v2.0.0/reference/termsNConditionsCodes?termNConditionCode=DBX_Default_TnC

			// Fetching T&C id from termandcondition table
			Map<String, Object> inputBodyMap = new HashMap<String, Object>();
			inputBodyMap.put(ODataQueryConstants.FILTER, "Code eq '" + termAndConditionCode + "'");
			inputBodyMap.put(ODataQueryConstants.SELECT, "id");
			//			String readTermAndConditionResponse = Executor.invokeService(ServiceURLEnum.TERMANDCONDITION_READ,
			//					inputBodyMap, null, requestInstance);

			
			String readTermAndConditionResponse =
					DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
					.withOperationId(OperationName.DB_TNC_GET)
					.withRequestParameters(inputBodyMap)
					.build()
					.getResponse();
			
			JSONObject readTermAndConditionResponseJSON = CommonUtilities
					.getStringAsJSONObject(readTermAndConditionResponse);
			if (readTermAndConditionResponseJSON != null
					&& readTermAndConditionResponseJSON.has(FabricConstants.OPSTATUS)
					&& readTermAndConditionResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readTermAndConditionResponseJSON.has("termandcondition")) {
				JSONArray readTermAndConditionJSONArray = readTermAndConditionResponseJSON
						.optJSONArray("termandcondition");
				if (readTermAndConditionJSONArray == null || readTermAndConditionJSONArray.length() < 1) {
					alert.prepareError("Failed to get Terms and Conditions").log();
					ErrorCodeEnum.ERR_20264.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				} else {
					JSONObject currTermAndConditionRecord = readTermAndConditionJSONArray.getJSONObject(0);
					termAndConditonId = currTermAndConditionRecord.getString("id");
				}
			} else {
				alert.prepareError("Failed to get Terms and Conditions").log();
				ErrorCodeEnum.ERR_20264.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}

			inputBodyMap.clear();
			inputBodyMap.put(ODataQueryConstants.FILTER, "TermAndConditionId eq '" + termAndConditonId
					+ "' and LanguageCode eq '" + languageCode + "' and Status_id eq '" + TANDC_ACTIVE_STATUS_ID + "'");
			inputBodyMap.put(ODataQueryConstants.SELECT, "Content, ContentType_id, Version_Id");
			String readTermAndConditionTextResponse =
					DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
					.withOperationId(OperationName.DB_TNCTEXT_GET).withRequestParameters(inputBodyMap).build()
					.getResponse();
			JSONObject readTermAndConditionTextResponseJSON = CommonUtilities
					.getStringAsJSONObject(readTermAndConditionTextResponse);
			if (readTermAndConditionTextResponseJSON != null
					&& readTermAndConditionTextResponseJSON.has(FabricConstants.OPSTATUS)
					&& readTermAndConditionTextResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readTermAndConditionTextResponseJSON.has("termandconditiontext")) {
				JSONArray readTermAndConditionTextJSONArray = readTermAndConditionTextResponseJSON
						.optJSONArray("termandconditiontext");
				if (readTermAndConditionTextJSONArray == null || readTermAndConditionTextJSONArray.length() < 1) {
					// If content is null returning content for default language
					inputBodyMap.clear();
					inputBodyMap.put(ODataQueryConstants.FILTER,
							"TermAndConditionId eq '" + termAndConditonId + "' and LanguageCode eq '"
									+ DEFAULT_LANGUAGE_CODE + "' and Status_id eq '" + TANDC_ACTIVE_STATUS_ID + "'");
					inputBodyMap.put(ODataQueryConstants.SELECT, "Content, ContentType_id, Version_Id");
					String _readTermAndConditionTextResponse =
							DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
							.withOperationId(OperationName.DB_TNCTEXT_GET).withRequestParameters(inputBodyMap).build()
							.getResponse();
					JSONObject _readTermAndConditionTextResponseJSON = CommonUtilities
							.getStringAsJSONObject(_readTermAndConditionTextResponse);
					if (_readTermAndConditionTextResponseJSON != null
							&& _readTermAndConditionTextResponseJSON.has(FabricConstants.OPSTATUS)
							&& _readTermAndConditionTextResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
							&& _readTermAndConditionTextResponseJSON.has("termandconditiontext")) {
						JSONArray _readTermAndConditionTextJSONArray = _readTermAndConditionTextResponseJSON
								.optJSONArray("termandconditiontext");
						if (_readTermAndConditionTextJSONArray == null
								|| _readTermAndConditionTextJSONArray.length() < 1) {
							alert.prepareError("Failed to get Terms and Conditions text").log();
							ErrorCodeEnum.ERR_20265.setErrorCode(result);
							result.addParam(new Param("status", "Failure", FabricConstants.STRING));
							return result;
						} else {
							JSONObject _currTermAndConditionTextRecord = _readTermAndConditionTextJSONArray
									.getJSONObject(0);
							Param _currTermAndConditionContentParam = new Param("termsAndConditionsContent",
									_currTermAndConditionTextRecord.optString("Content"), FabricConstants.STRING);
							result.addParam(_currTermAndConditionContentParam);
							String termAndConditionContentTypeId = _currTermAndConditionTextRecord
									.getString("ContentType_id");
							Param contentTypeId_Param = new Param("contentTypeId", termAndConditionContentTypeId,
									FabricConstants.STRING);
							result.addParam(contentTypeId_Param);
							String termAndConditionVersionId = _currTermAndConditionTextRecord.getString("Version_Id");
							Param versionId_Param = new Param("versionId", termAndConditionVersionId,
									FabricConstants.STRING);
							result.addParam(versionId_Param);
							result.addParam(new Param("status", "Success", FabricConstants.STRING));
							return result;
						}
					} else {
						alert.prepareError("Failed to get Terms and Conditions text").log();
						ErrorCodeEnum.ERR_20265.setErrorCode(result);
						result.addParam(new Param("status", "Failure", FabricConstants.STRING));
						return result;
					}
				} else {
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
				}
			} else {
				alert.prepareError("Failed to get Terms and Conditions text").log();
				ErrorCodeEnum.ERR_20265.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}
		} catch (Exception e) {
			alert.prepareError("Failed to get Terms and Conditions text. Exception: ", e).log();
			result.addParam(new Param("status", "Failure", FabricConstants.STRING));
			ErrorCodeEnum.ERR_20265.setErrorCode(result);
		}		
		return result;
	}


	public Result editTermsAndConditionsDBXDB(Map<String, Object> postParametersMap, String dbpServicesClaimsToken, UserDetailsBean loggedInUserDetails)
			throws DBPApplicationException {

		Result result = new Result();

		try {
			// Fetching T&C id from termandcondition table
			String termAndConditionId = null;
			String termAndConditionCode = postParametersMap.get("termAndConditionCode").toString();
			Map<String, Object> inputBodyMap = new HashMap<String, Object>();
			inputBodyMap.put(ODataQueryConstants.FILTER, "Code eq '" + termAndConditionCode + "'");
			inputBodyMap.put(ODataQueryConstants.SELECT, "id");					
			String readTermAndConditionResponse = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
					.withOperationId(OperationName.DB_TNC_GET).withRequestParameters(inputBodyMap).build()
					.getResponse();
			JSONObject readTermAndConditionResponseJSON = CommonUtilities
					.getStringAsJSONObject(readTermAndConditionResponse);
			if (readTermAndConditionResponseJSON != null
					&& readTermAndConditionResponseJSON.has(FabricConstants.OPSTATUS)
					&& readTermAndConditionResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readTermAndConditionResponseJSON.has("termandcondition")) {
				JSONArray readTermAndConditionJSONArray = readTermAndConditionResponseJSON
						.optJSONArray("termandcondition");
				if (readTermAndConditionJSONArray == null || readTermAndConditionJSONArray.length() < 1) {
					alert.prepareError("Failed to get Term and Condition").log();
					ErrorCodeEnum.ERR_20264.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				} else {
					JSONObject currTermAndConditionRecord = readTermAndConditionJSONArray.getJSONObject(0);
					termAndConditionId = currTermAndConditionRecord.getString("id");
				}
			} else {
				alert.prepareError("Failed to get Terms and Conditions").log();
				ErrorCodeEnum.ERR_20264.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}
			inputBodyMap.clear();
			String termAndConditionTitle =postParametersMap.get("termAndConditionTitle").toString();
			String termAndConditionDescription =postParametersMap.get("termAndConditionDescription").toString();

			inputBodyMap.put("id", termAndConditionId);
			inputBodyMap.put("Title", termAndConditionTitle);
			inputBodyMap.put("Description", termAndConditionDescription);
			inputBodyMap.put("modifiedby", loggedInUserDetails.getUserName());
			inputBodyMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());
			String editTermAndConditionResponse =
					DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
					.withOperationId(OperationName.DB_TNC_UPDATE).withRequestParameters(inputBodyMap).build()
					.getResponse();

			JSONObject editTermAndConditionResponseJSON = CommonUtilities
					.getStringAsJSONObject(editTermAndConditionResponse);
			if (!(editTermAndConditionResponseJSON != null
					&& editTermAndConditionResponseJSON.has(FabricConstants.OPSTATUS)
					&& editTermAndConditionResponseJSON.getInt(FabricConstants.OPSTATUS) == 0)) {
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				ErrorCodeEnum.ERR_20269.setErrorCode(result);
				alert.prepareError("Failed to Edit Terms and Conditions").log();
				return result;
			}
			
			// Edit Term and condition App Preferences
			if (null != postParametersMap.get("appPreferences")) {
				String appPreferencesStr =postParametersMap.get("appPreferences").toString();
				JSONObject appPreferencesJSON = CommonUtilities.getStringAsJSONObject(appPreferencesStr);
				Set<String> supportedAppsSet = new HashSet<>();
				Set<String> unSupportedAppsSet = new HashSet<>();
				for (String key : appPreferencesJSON.keySet()) {
					if ((!supportedAppsSet.contains(key)) && (!unSupportedAppsSet.contains(key))) {
						if (StringUtils.equalsIgnoreCase(appPreferencesJSON.optString(key), "TRUE")) {
							supportedAppsSet.add(key);
						} else if (StringUtils.equalsIgnoreCase(appPreferencesJSON.optString(key), "FALSE")) {
							unSupportedAppsSet.add(key);
						}
					} else {
						result.addParam(new Param("status", "Failure", FabricConstants.STRING));
						ErrorCodeEnum.ERR_20273.setErrorCode(result);
						alert.prepareError("Duplicate entry of app for applicable apps").log();
						return result;
					}
				}
				diagnostic.prepareDebug("Setting Terms and conditions App Preferences").log();

				// Fetch Existing Association
				Set<String> associatedAppsSet = new HashSet<>();
				Map<String, String> associatedTermAndConditionAppIdMap = new HashMap<String, String>();
				inputBodyMap.clear();
				inputBodyMap.put(ODataQueryConstants.FILTER, "TermAndConditionId eq '" + termAndConditionId + "'");
				inputBodyMap.put(ODataQueryConstants.SELECT, "AppId, id");
				String readTermAndConditionAppResponse =
						DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
						.withOperationId(OperationName.DB_TNCAPP_GET).withRequestParameters(inputBodyMap).build()
						.getResponse();

				JSONObject readTermAndConditionAppResponseJSON = CommonUtilities
						.getStringAsJSONObject(readTermAndConditionAppResponse);
				if (readTermAndConditionAppResponseJSON == null
						|| !readTermAndConditionAppResponseJSON.has(FabricConstants.OPSTATUS)
						|| readTermAndConditionAppResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
						|| !readTermAndConditionAppResponseJSON.has("termandconditionapp")) {
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					ErrorCodeEnum.ERR_20274.setErrorCode(result);
					alert.prepareError("Failed to update terms and conditions applicable app").log();
					return result;
				}

				JSONArray termAndConditionAppJSONArray = readTermAndConditionAppResponseJSON
						.optJSONArray("termandconditionapp");
				for (Object currObj : termAndConditionAppJSONArray) {
					if (currObj instanceof JSONObject) {
						JSONObject currJSON = (JSONObject) currObj;
						if (currJSON.has("AppId")) {
							associatedAppsSet.add(currJSON.optString("AppId"));
							associatedTermAndConditionAppIdMap.put(currJSON.optString("AppId"),
									currJSON.optString("id"));
						}
					}
				}

				// Handle Supported Apps
				if ((supportedAppsSet != null) && (!supportedAppsSet.isEmpty())) {
					for (String appId : supportedAppsSet) {
						if (!associatedAppsSet.contains(appId)) {
							// Creating term and condition app record

							inputBodyMap.clear();
							String termAndConditionAppId = Long.toString(CommonUtilities.getNumericId());
							inputBodyMap.put("id", termAndConditionAppId);
							inputBodyMap.put("TermAndConditionId", termAndConditionId);
							inputBodyMap.put("AppId", appId);
							inputBodyMap.put("createdby", loggedInUserDetails.getUserId());
							inputBodyMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
							String createTermAndConditionAppResponse =
									DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
									.withOperationId(OperationName.DB_TNCAPP_CREATE).withRequestParameters(inputBodyMap).build()
									.getResponse();
							JSONObject createTermAndConditionAppResponseJSON = CommonUtilities
									.getStringAsJSONObject(createTermAndConditionAppResponse);
							if (createTermAndConditionAppResponseJSON == null
									|| !createTermAndConditionAppResponseJSON.has(FabricConstants.OPSTATUS)
									|| createTermAndConditionAppResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
								result.addParam(new Param("status", "Failure", FabricConstants.STRING));
								ErrorCodeEnum.ERR_20275.setErrorCode(result);
								alert.prepareError("Failed to create terms and conditions applicable app record").log();
								return result;
							}
						}
					}
				}
				// Handle UnSupported Apps
				if ((unSupportedAppsSet != null) && (!unSupportedAppsSet.isEmpty())) {
					for (String appId : unSupportedAppsSet) {
						if (associatedAppsSet.contains(appId)) {
							// Deleting term and condition app record
							inputBodyMap.clear();
							inputBodyMap.put("id", associatedTermAndConditionAppIdMap.get(appId));
							//							String deleteTermAndConditionAppResponse = Executor.invokeService(
							//									ServiceURLEnum.TERMANDCONDITIONAPP_DELETE, inputBodyMap, null, requestInstance);
							String deleteTermAndConditionAppResponse =
									DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
									.withOperationId(OperationName.DB_TNCAPP_DELETE).withRequestParameters(inputBodyMap).build()
									.getResponse();

							JSONObject deleteTermAndConditionAppResponseJSON = CommonUtilities
									.getStringAsJSONObject(deleteTermAndConditionAppResponse);
							if (deleteTermAndConditionAppResponseJSON == null
									|| !deleteTermAndConditionAppResponseJSON.has(FabricConstants.OPSTATUS)
									|| deleteTermAndConditionAppResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
								result.addParam(new Param("status", "Failure", FabricConstants.STRING));
								ErrorCodeEnum.ERR_20276.setErrorCode(result);
								alert.prepareError("Failed to delete terms and conditions applicable app record").log();
								return result;
							}
						}
					}
				}

				diagnostic.prepareDebug("Terms and conditions App Preferences Set").log();
			}

			result.addParam(new Param("status", "Success", FabricConstants.STRING));

		} catch (Exception e) {
			alert.prepareError("Failed to Edit Terms and Conditions text").log();
			ErrorCodeEnum.ERR_20271.setErrorCode(result);			
			return result;
		}
		return result;
	}

	public Result deleteTermsAndConditionsVersionDBXDB(Map<String, Object> postParametersMap)	
			throws DBPApplicationException {

		Result result = new Result();

		try {
			String termAndConditionCode =postParametersMap.get("termAndConditionCode").toString();
			String languageCode =postParametersMap.get("languageCode").toString();
			String leId =(String) postParametersMap.get("legalEntityId");
			if(StringUtils.isBlank(leId)) {
				leId = EnvironmentConfigurationsHandler.getServerAppProperty("BRANCH_ID_REFERENCE");
			}
			// Fetching T&C id from termandcondition table
			String termAndConditionId = null;
			termAndConditionId = getTermAndConditionId(termAndConditionCode, leId);
			if (termAndConditionId == null) {
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				ErrorCodeEnum.ERR_20264.setErrorCode(result);
				alert.prepareError("Failed to get Terms and Conditions").log();
				alert.prepareError("Failed to delete Terms and Conditions draft version").log();
				return result;
			}

			String termAndConditionTextId = null;
			termAndConditionTextId = getTermAndConditionTextId(termAndConditionId, languageCode,
					TANDC_DRAFT_STATUS_ID, result, leId);
			if (termAndConditionTextId == null) {
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				ErrorCodeEnum.ERR_20265.setErrorCode(result);
				alert.prepareError("Failed to get Terms and Conditions text").log();
				alert.prepareError("Failed to delete Terms and Conditions draft version").log();
				return result;
			}

			Map<String, Object> inputMap = new HashMap<String, Object>();
			inputMap.put("id", termAndConditionTextId);
			String deleteTermAndConditionTextResponse =
					DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
					.withOperationId(OperationName.DB_TNCTEXT_DELETE).withRequestParameters(inputMap).build()
					.getResponse();

			JSONObject deleteTermAndConditionTextResponseJSON = CommonUtilities
					.getStringAsJSONObject(deleteTermAndConditionTextResponse);
			if (deleteTermAndConditionTextResponseJSON != null
					&& deleteTermAndConditionTextResponseJSON.has(FabricConstants.OPSTATUS)
					&& deleteTermAndConditionTextResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
				alert.prepareError("Successfully deleted Terms and Conditions draft version").log();
				result.addParam(new Param("status", "Success", FabricConstants.STRING));                
			} else {
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				ErrorCodeEnum.ERR_20280.setErrorCode(result);
				alert.prepareError("Failed to delete Terms and Conditions draft version").log();                
			}

		} catch (Exception e) {
			ErrorCodeEnum.ERR_20280.setErrorCode(result);
			alert.prepareError("Failed to delete Terms and Conditions draft version", e).log();           
		}
		return result;
	}
	/*public Result getAllTermsAndConditionsDBXDB(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {

		Result result = new Result();

		try {

			// Fetching appId and appName from app table
			Map<String, Object> inputBodyMap = new HashMap<String, Object>();
			Map<String, String> appIdNameMap = new HashMap<String, String>();
			inputBodyMap.put(ODataQueryConstants.SELECT, "id, Name");
			//			String readAppResponse = Executor.invokeService(ServiceURLEnum.APP_READ, inputBodyMap, null,
			//					requestInstance);
			String readAppResponse =
					DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
					.withOperationId(OperationName.DB_APP_GET).withRequestParameters(inputBodyMap).build()
					.getResponse();

			JSONObject readAppResponseJSON = CommonUtilities.getStringAsJSONObject(readAppResponse);
			if (readAppResponseJSON != null && readAppResponseJSON.has(FabricConstants.OPSTATUS)
					&& readAppResponseJSON.getInt(FabricConstants.OPSTATUS) == 0 && readAppResponseJSON.has("app")) {
				JSONArray readAppJSONArray = readAppResponseJSON.optJSONArray("app");
				if ((readAppJSONArray != null) && (readAppJSONArray.length() > 0)) {
					for (int indexVar = 0; indexVar < readAppJSONArray.length(); indexVar++) {
						JSONObject appJSONObject = readAppJSONArray.getJSONObject(indexVar);
						String appId = appJSONObject.getString("id");
						String appName = appJSONObject.getString("names");
						appIdNameMap.put(appId, appName);
					}
				}
			}

			// Fetching contentTypeId and contentTypeName from contenttype table
			inputBodyMap.clear();
			Map<String, String> contentTypeIdNameMap = new HashMap<String, String>();
			inputBodyMap.put(ODataQueryConstants.SELECT, "id, Name");
			//			String readContentTypeResponse = Executor.invokeService(ServiceURLEnum.CONTENTTYPE_READ, inputBodyMap, null,
			//					requestInstance);
			String readContentTypeResponse =
					DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
					.withOperationId(OperationName.DB_CONTENTTYPE_GET).withRequestParameters(inputBodyMap).build()
					.getResponse();

			JSONObject readContentTypeResponseJSON = CommonUtilities.getStringAsJSONObject(readContentTypeResponse);
			if (readContentTypeResponseJSON != null && readContentTypeResponseJSON.has(FabricConstants.OPSTATUS)
					&& readContentTypeResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readContentTypeResponseJSON.has("contenttype")) {
				JSONArray readContentTypeJSONArray = readContentTypeResponseJSON.optJSONArray("contenttype");
				if ((readContentTypeJSONArray != null) && (readContentTypeJSONArray.length() > 0)) {
					for (int indexVar = 0; indexVar < readContentTypeJSONArray.length(); indexVar++) {
						JSONObject contentTypeJSONObject = readContentTypeJSONArray.getJSONObject(indexVar);
						String contentTypeId = contentTypeJSONObject.getString("id");
						String contentTypeName = contentTypeJSONObject.getString("Name");
						contentTypeIdNameMap.put(contentTypeId, contentTypeName);
					}
				}
			}

			// Fetching languageId and languageName from locale table
			inputBodyMap.clear();
			Map<String, String> languageIdNameMap = new HashMap<String, String>();
			inputBodyMap.put(ODataQueryConstants.SELECT, "Code, Language");			
			String readlanguageResponse =
					DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
					.withOperationId(OperationName.DB_LOCALE_GET).withRequestParameters(inputBodyMap).build()
					.getResponse();

			JSONObject readlanguageResponseJSON = CommonUtilities.getStringAsJSONObject(readlanguageResponse);
			if (readlanguageResponseJSON != null && readlanguageResponseJSON.has(FabricConstants.OPSTATUS)
					&& readlanguageResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readlanguageResponseJSON.has("locale")) {
				JSONArray readlanguageJSONArray = readlanguageResponseJSON.optJSONArray("locale");
				if ((readlanguageJSONArray != null) && (readlanguageJSONArray.length() > 0)) {
					for (int indexVar = 0; indexVar < readlanguageJSONArray.length(); indexVar++) {
						JSONObject languageJSONObject = readlanguageJSONArray.getJSONObject(indexVar);
						String languageId = languageJSONObject.getString("Code");
						String languageName = languageJSONObject.getString("Language");
						languageIdNameMap.put(languageId, languageName);
					}
				}
			}

			// Fetching T&C id, Code, Title, Description, ContentModifiedBy and
			// ContentModifiedOn from termandcondition table
			String termAndConditionId = null, termAndConditionCode = null, termAndConditionTitle = null,
					termAndConditionDescription = null;
			inputBodyMap.clear();
			inputBodyMap.put(ODataQueryConstants.SELECT, "id, Code, Title, Description");

			String readTermAndConditionResponse =
					DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
					.withOperationId(OperationName.DB_TNC_GET).withRequestParameters(inputBodyMap).build()
					.getResponse();			
			JSONObject readTermAndConditionResponseJSON = CommonUtilities
					.getStringAsJSONObject(readTermAndConditionResponse);
			if (readTermAndConditionResponseJSON != null
					&& readTermAndConditionResponseJSON.has(FabricConstants.OPSTATUS)
					&& readTermAndConditionResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readTermAndConditionResponseJSON.has("termandcondition")) {
				JSONArray readTermAndConditionJSONArray = readTermAndConditionResponseJSON
						.optJSONArray("termandcondition");
				if ((readTermAndConditionJSONArray != null) && (readTermAndConditionJSONArray.length() > 0)) {
					Dataset termsAndConditionsDataset = new Dataset();
					termsAndConditionsDataset.setId("termsAndConditions");
					JSONObject termAndConditionJSONObject = null;
					for (int indexVar = 0; indexVar < readTermAndConditionJSONArray.length(); indexVar++) {
						termAndConditionJSONObject = readTermAndConditionJSONArray.getJSONObject(indexVar);
						Record currRecord = new Record();
						termAndConditionId = termAndConditionJSONObject.getString("id");
						termAndConditionCode = termAndConditionJSONObject.getString("Code");
						termAndConditionTitle = termAndConditionJSONObject.getString("Title");
						termAndConditionDescription = termAndConditionJSONObject.getString("Description");

						// Skipping records that does not have content for default language
						inputBodyMap.clear();
						inputBodyMap.put(ODataQueryConstants.FILTER,
								"TermAndConditionId eq '" + termAndConditionId + "' and LanguageCode eq '"
										+ DEFAULT_LANGUAGE_CODE + "' and Status_id eq '" + TANDC_ACTIVE_STATUS_ID
										+ "'");
						inputBodyMap.put(ODataQueryConstants.SELECT, "Content");
						String _readTermAndConditionTextResponse =
								DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
								.withOperationId(OperationName.DB_TNCTEXT_GET).withRequestParameters(inputBodyMap).build()
								.getResponse();			
						JSONObject _readTermAndConditionTextResponseJSON = CommonUtilities
								.getStringAsJSONObject(_readTermAndConditionTextResponse);
						if (_readTermAndConditionTextResponseJSON != null
								&& _readTermAndConditionTextResponseJSON.has(FabricConstants.OPSTATUS)
								&& _readTermAndConditionTextResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
								&& _readTermAndConditionTextResponseJSON.has("termandconditiontext")) {
							JSONArray _readTermAndConditionTextJSONArray = _readTermAndConditionTextResponseJSON
									.optJSONArray("termandconditiontext");
							if ((_readTermAndConditionTextJSONArray != null)
									&& (_readTermAndConditionTextJSONArray.length() > 0)) {
								String content = _readTermAndConditionTextJSONArray.getJSONObject(0)
										.optString("Content");
								if (StringUtils.isEmpty(content))
									continue;
							}
						}

						Param termAndConditionCode_Param = new Param("code", termAndConditionCode,
								FabricConstants.STRING);
						currRecord.addParam(termAndConditionCode_Param);
						Param termAndConditionTitle_Param = new Param("title", termAndConditionTitle,
								FabricConstants.STRING);
						currRecord.addParam(termAndConditionTitle_Param);
						Param termAndConditionDescription_Param = new Param("description", termAndConditionDescription,
								FabricConstants.STRING);
						currRecord.addParam(termAndConditionDescription_Param);

						// Fetch supportedApps from termandconditionapp
						Set<String> supportedAppsSet = new HashSet<String>();
						Dataset appPreferencesDataset = new Dataset();
						appPreferencesDataset.setId("appPreferences");
						inputBodyMap.clear();
						inputBodyMap.put(ODataQueryConstants.FILTER,
								"TermAndConditionId eq '" + termAndConditionId + "'");
						inputBodyMap.put(ODataQueryConstants.SELECT, "AppId");
						String readTermAndConditionAppResponse =
								DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
								.withOperationId(OperationName.DB_TNCAPP_GET).withRequestParameters(inputBodyMap).build()
								.getResponse();
						JSONObject readTermAndConditionAppResponseJSON = CommonUtilities
								.getStringAsJSONObject(readTermAndConditionAppResponse);
						if (readTermAndConditionAppResponseJSON != null
								&& readTermAndConditionAppResponseJSON.has(FabricConstants.OPSTATUS)
								&& readTermAndConditionAppResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
								&& readTermAndConditionAppResponseJSON.has("termandconditionapp")) {
							JSONArray readTermAndConditionAppResponseJSONArray = readTermAndConditionAppResponseJSON
									.optJSONArray("termandconditionapp");
							if ((readTermAndConditionAppResponseJSONArray != null)
									&& (readTermAndConditionAppResponseJSONArray.length() > 0)) {
								for (int index = 0; index < readTermAndConditionAppResponseJSONArray
										.length(); index++) {
									// Adding supported apps records
									String appId = readTermAndConditionAppResponseJSONArray.getJSONObject(index)
											.optString("AppId");
									Record appRecord = new Record();
									Param appId_Param = new Param("appId", appId, FabricConstants.STRING);
									appRecord.addParam(appId_Param);
									if (appIdNameMap.containsKey(appId)) {
										Param appName_Param = new Param("appName", appIdNameMap.get(appId),
												FabricConstants.STRING);
										appRecord.addParam(appName_Param);
									}
									Param isSupported_Param = new Param("isSupported", "true", FabricConstants.STRING);
									appRecord.addParam(isSupported_Param);
									supportedAppsSet.add(appId);
									appPreferencesDataset.addRecord(appRecord);
								}
							}
						}

						// Adding unsupported apps record
						if ((appIdNameMap != null) && (!appIdNameMap.isEmpty())) {
							for (Map.Entry<String, String> appElement : appIdNameMap.entrySet()) {
								String appId = (String) appElement.getKey();
								if (!supportedAppsSet.contains(appId)) {
									Record appRecord = new Record();
									Param appId_Param = new Param("appId", appId, FabricConstants.STRING);
									appRecord.addParam(appId_Param);
									Param appName_Param = new Param("appName", appIdNameMap.get(appId),
											FabricConstants.STRING);
									appRecord.addParam(appName_Param);
									Param isSupported_Param = new Param("isSupported", "false", FabricConstants.STRING);
									appRecord.addParam(isSupported_Param);
									appPreferencesDataset.addRecord(appRecord);
								}
							}
						}

						currRecord.addDataset(appPreferencesDataset);

						// For backward compatibilty we need below 2 param from termandconditiontext
						// table
						String contentModifiedByForOneRecord = null, contentModifiedOnForOneRecord = null;

						// Fetch T&C Content, languageCode and T&C content type from
						// termandconditiontext table
						inputBodyMap.clear();
						inputBodyMap.put(ODataQueryConstants.FILTER,
								"TermAndConditionId eq '" + termAndConditionId + "'");
						inputBodyMap.put(ODataQueryConstants.SELECT,
								"LanguageCode, Content, ContentType_id, ContentModifiedBy, ContentModifiedOn, Status_id, Version_Id, Description");
						//						String readTermAndConditionTextResponse = Executor.invokeService(
						//								ServiceURLEnum.TERMANDCONDITIONTEXT_READ, inputBodyMap, null, requestInstance);
						String readTermAndConditionTextResponse =
								DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
								.withOperationId(OperationName.DB_TNCTEXT_GET).withRequestParameters(inputBodyMap).build()
								.getResponse();

						JSONObject readTermAndConditionTextResponseJSON = CommonUtilities
								.getStringAsJSONObject(readTermAndConditionTextResponse);
						if (readTermAndConditionTextResponseJSON != null
								&& readTermAndConditionTextResponseJSON.has(FabricConstants.OPSTATUS)
								&& readTermAndConditionTextResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
								&& readTermAndConditionTextResponseJSON.has("termandconditiontext")) {
							JSONArray readTermAndConditionTextJSONArray = readTermAndConditionTextResponseJSON
									.optJSONArray("termandconditiontext");
							if ((readTermAndConditionTextJSONArray != null)
									&& (readTermAndConditionTextJSONArray.length() > 0)) {
								Dataset termsAndConditionsVersionDataset = createTermsAndConditionsVersionDataset(
										readTermAndConditionTextJSONArray, contentTypeIdNameMap, languageIdNameMap);
								// Not changing below logic to support backward compatibility
								JSONObject termAndConditionTextJSONObject = null;
								Record termsAndConditionsContentRecords = new Record();
								termsAndConditionsContentRecords.setId("termsAndConditionsContent");
								for (int index = 0; index < readTermAndConditionTextJSONArray.length(); index++) {
									termAndConditionTextJSONObject = readTermAndConditionTextJSONArray
											.getJSONObject(index);

									if (index == 0) {
										contentModifiedByForOneRecord = readTermAndConditionTextJSONArray
												.getJSONObject(0).getString("ContentModifiedBy");
										contentModifiedOnForOneRecord = readTermAndConditionTextJSONArray
												.getJSONObject(0).getString("ContentModifiedOn");
									}

									String languageCode = termAndConditionTextJSONObject.getString("LanguageCode");
									String termAndConditionContent = termAndConditionTextJSONObject
											.optString("Content");
									String termAndConditionContentTypeId = termAndConditionTextJSONObject
											.optString("ContentType_id");
									String termsAndConditionsContentModifiedBy = termAndConditionTextJSONObject
											.optString("ContentModifiedBy");
									String termsAndConditionsContentModifiedOn = termAndConditionTextJSONObject
											.optString("ContentModifiedOn");
									String termsAndConditionsStatusId = termAndConditionTextJSONObject
											.optString("Status_id");
									String termsAndConditionsVersionId = termAndConditionTextJSONObject
											.optString("Version_Id");
									String termsAndConditionsVersionDescription = termAndConditionTextJSONObject
											.optString("Description");

									String termAndConditionContentTypeName = null;
									if (contentTypeIdNameMap.containsKey(termAndConditionContentTypeId))
										termAndConditionContentTypeName = contentTypeIdNameMap
										.get(termAndConditionContentTypeId);
									String languageName = null;
									if (languageIdNameMap.containsKey(languageCode))
										languageName = languageIdNameMap.get(languageCode);

									Record contentLangRecord = new Record();
									contentLangRecord.setId(languageCode);
									Param content_Param = new Param("content", termAndConditionContent,
											FabricConstants.STRING);
									contentLangRecord.addParam(content_Param);
									Param contentTypeId_Param = new Param("contentTypeId",
											termAndConditionContentTypeId, FabricConstants.STRING);
									contentLangRecord.addParam(contentTypeId_Param);
									Param contentType_Param = new Param("contentTypeName",
											termAndConditionContentTypeName, FabricConstants.STRING);
									contentLangRecord.addParam(contentType_Param);
									Param languageCode_Param = new Param("languageCode", languageCode,
											FabricConstants.STRING);
									contentLangRecord.addParam(languageCode_Param);
									Param languageName_Param = new Param("languageName", languageName,
											FabricConstants.STRING);
									contentLangRecord.addParam(languageName_Param);
									Param contentModifiedBy_Param = new Param("contentModifiedBy",
											termsAndConditionsContentModifiedBy, FabricConstants.STRING);
									contentLangRecord.addParam(contentModifiedBy_Param);
									Param contentModifiedOn_Param = new Param("contentModifiedOn",
											termsAndConditionsContentModifiedOn, FabricConstants.STRING);
									contentLangRecord.addParam(contentModifiedOn_Param);
									Param statusId_Param = new Param("statusId", termsAndConditionsStatusId,
											FabricConstants.STRING);
									contentLangRecord.addParam(statusId_Param);
									Param versionId_Param = new Param("versionId", termsAndConditionsVersionId,
											FabricConstants.STRING);
									contentLangRecord.addParam(versionId_Param);
									Param versionDescription_Param = new Param("versionDescription",
											termsAndConditionsVersionDescription, FabricConstants.STRING);
									contentLangRecord.addParam(versionDescription_Param);
									termsAndConditionsContentRecords.addRecord(contentLangRecord);
								}
								currRecord.addRecord(termsAndConditionsContentRecords);
								currRecord.addDataset(termsAndConditionsVersionDataset);
							}
						} else {
							alert.prepareError("Failed to get Term and Condition text").log();
							ErrorCodeEnum.ERR_20265.setErrorCode(result);
							result.addParam(new Param("status", "Failure", FabricConstants.STRING));
							return null;
						}
						// For Backward compatibility
						Param termAndConditionContentModifiedBy_Param = new Param("contentModifiedBy",
								contentModifiedByForOneRecord, FabricConstants.STRING);
						currRecord.addParam(termAndConditionContentModifiedBy_Param);
						Param termAndConditionContentModifiedOn_Param = new Param("contentModifiedOn",
								contentModifiedOnForOneRecord, FabricConstants.STRING);
						currRecord.addParam(termAndConditionContentModifiedOn_Param);

						termsAndConditionsDataset.addRecord(currRecord);
					}
					result.addDataset(termsAndConditionsDataset);
				}
			}
			result.addParam(new Param("status", "Success", FabricConstants.STRING));
			return result;
		} catch (Exception e) {
			alert.prepareError("Failed to get Terms and Conditions text. Exception: ", e).log();
			result.addParam(new Param("status", "Failure", FabricConstants.STRING));
			ErrorCodeEnum.ERR_20265.setErrorCode(result);
		}
		return result;
	}

	*/

	public Dataset createTermsAndConditionsVersionDataset(JSONArray readTermAndConditionTextJSONArray,
			Map<String, String> contentTypeIdNameMap, Map<String, String> languageIdNameMap) {

		Dataset termsAndConditionsVersionDataset = new Dataset();
		termsAndConditionsVersionDataset.setId("termsAndConditionsVersion");
		JSONObject termAndConditionTextJSONObject = null;

		for (int index = 0; index < readTermAndConditionTextJSONArray.length(); index++) {

			termAndConditionTextJSONObject = readTermAndConditionTextJSONArray.getJSONObject(index);

			String languageCode = termAndConditionTextJSONObject.getString("LanguageCode");
			String termAndConditionContent = termAndConditionTextJSONObject.optString("Content");
			String termAndConditionVersionDescription = termAndConditionTextJSONObject.optString("Description");
			String termAndConditionContentTypeId = termAndConditionTextJSONObject.getString("ContentType_id");
			String termsAndConditionsContentModifiedBy = termAndConditionTextJSONObject.getString("ContentModifiedBy");
			String termsAndConditionsContentModifiedOn = termAndConditionTextJSONObject.getString("ContentModifiedOn");
			String termsAndConditionsStatusId = termAndConditionTextJSONObject.getString("Status_id");
			String termsAndConditionsVersionId = termAndConditionTextJSONObject.getString("Version_Id");

			String termAndConditionContentTypeName = null;
			if (contentTypeIdNameMap.containsKey(termAndConditionContentTypeId))
				termAndConditionContentTypeName = contentTypeIdNameMap.get(termAndConditionContentTypeId);
			String languageName = null;
			if (languageIdNameMap.containsKey(languageCode))
				languageName = languageIdNameMap.get(languageCode);

			Record termsAndConditionsVersionRecord = new Record();
			termsAndConditionsVersionRecord.setId(languageCode);
			Param content_Param = new Param("content", termAndConditionContent, FabricConstants.STRING);
			termsAndConditionsVersionRecord.addParam(content_Param);
			Param contentTypeId_Param = new Param("contentTypeId", termAndConditionContentTypeId,
					FabricConstants.STRING);
			termsAndConditionsVersionRecord.addParam(contentTypeId_Param);
			Param contentType_Param = new Param("contentTypeName", termAndConditionContentTypeName,
					FabricConstants.STRING);
			termsAndConditionsVersionRecord.addParam(contentType_Param);
			Param languageCode_Param = new Param("languageCode", languageCode, FabricConstants.STRING);
			termsAndConditionsVersionRecord.addParam(languageCode_Param);
			Param languageName_Param = new Param("languageName", languageName, FabricConstants.STRING);
			termsAndConditionsVersionRecord.addParam(languageName_Param);
			Param contentModifiedBy_Param = new Param("contentModifiedBy", termsAndConditionsContentModifiedBy,
					FabricConstants.STRING);
			termsAndConditionsVersionRecord.addParam(contentModifiedBy_Param);
			Param contentModifiedOn_Param = new Param("contentModifiedOn", termsAndConditionsContentModifiedOn,
					FabricConstants.STRING);
			termsAndConditionsVersionRecord.addParam(contentModifiedOn_Param);
			if (TANDC_ACTIVE_STATUS_ID
					.equalsIgnoreCase(termAndConditionTextJSONObject.getString("Status_id"))
					|| ACTIVE_STATUS_ID.equalsIgnoreCase(
							termAndConditionTextJSONObject.getString("Status_id"))) {
				termsAndConditionsStatusId = TANDC_MS_ACTIVE_STATUS_ID;
			}
			Param statusId_Param = new Param("statusId", termsAndConditionsStatusId, FabricConstants.STRING);
			termsAndConditionsVersionRecord.addParam(statusId_Param);
			Param versionId_Param = new Param("versionId", termsAndConditionsVersionId, FabricConstants.STRING);
			termsAndConditionsVersionRecord.addParam(versionId_Param);
			Param versionDescription_Param = new Param("versionDescription", termAndConditionVersionDescription,
					FabricConstants.STRING);
			termsAndConditionsVersionRecord.addParam(versionDescription_Param);
			termsAndConditionsVersionDataset.addRecord(termsAndConditionsVersionRecord);
		}
		return termsAndConditionsVersionDataset;
	}


	public Result createTermsAndConditionsVersionDBXDB(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {
		Result result = new Result();
		try{
			String termAndConditionCode =postParametersMap.get("termAndConditionCode").toString();
			String languageCode =postParametersMap.get("languageCode").toString();
			String contentType =postParametersMap.get("contentType").toString();
			String termAndConditionContent =postParametersMap.get("termAndConditionContent").toString();
			String termAndConditionVersionDescription =postParametersMap.get("termAndConditionVersionDescription").toString();
			String loggedInUserDetailsName =postParametersMap.get("loggedInUserDetailsName").toString();
			String loggedInUserId =postParametersMap.get("loggedInUserId").toString();
			String isSave =postParametersMap.get("isSave").toString();
			String leId =(String) postParametersMap.get("legalEntityId");
			if(StringUtils.isBlank(leId)) {
				leId = EnvironmentConfigurationsHandler.getServerAppProperty("BRANCH_ID_REFERENCE");
			}
			String termAndConditionId = null;
			termAndConditionId = getTermAndConditionId(termAndConditionCode, leId);
			if (termAndConditionId == null) {
				alert.prepareError("Failed to get Terms and Conditions Id").log();
				ErrorCodeEnum.ERR_20264.setErrorCode(result);
				return result;
			}

			if (StringUtils.equalsIgnoreCase(isSave, "true")) {
				String termAndConditionTextId = null;
				termAndConditionTextId = getTermAndConditionTextId(termAndConditionId, languageCode,
						TANDC_DRAFT_STATUS_ID, result, leId);
				if (termAndConditionTextId == null) {
					createTermAndConditionText(termAndConditionId, languageCode, contentType,
							termAndConditionContent, termAndConditionVersionDescription, DEFAULT_DRAFT_VERSION,
							TANDC_DRAFT_STATUS_ID, loggedInUserDetailsName, loggedInUserId, result, leId);
					if (result.getParamByName(ErrorCodeEnum.ERROR_CODE_KEY) != null) {
						alert.prepareError("Terms and Conditions draft version create failed").log();
						return result;
					}
				} else {
					editTermAndConditionText(termAndConditionTextId, termAndConditionId, languageCode,
							contentType, termAndConditionContent, termAndConditionVersionDescription,
							DEFAULT_DRAFT_VERSION, TANDC_DRAFT_STATUS_ID, loggedInUserDetailsName, loggedInUserId, result, leId);
					if (result.getParamByName(ErrorCodeEnum.ERROR_CODE_KEY) != null) {
						alert.prepareError("Terms and Conditions draft version update failed").log();
						return result;
					}
				}
			} else {
				String termAndConditionActiveTextId = null;
				String termAndConditionDraftTextId = null;
				String termAndConditionArchivedTextId = null;
				termAndConditionActiveTextId = getTermAndConditionTextId(termAndConditionId,
						languageCode, TANDC_ACTIVE_STATUS_ID, result, leId);
				termAndConditionDraftTextId = getTermAndConditionTextId(termAndConditionId,
						languageCode, TANDC_DRAFT_STATUS_ID, result, leId);
				termAndConditionArchivedTextId = getTermAndConditionTextId(termAndConditionId,
						languageCode, TANDC_ARCHIVED_STATUS_ID, result, leId);

				// Creating 1.0 version record
				if (termAndConditionActiveTextId == null && termAndConditionArchivedTextId == null) {
					if (termAndConditionDraftTextId != null) {
						editTermAndConditionText(termAndConditionDraftTextId, termAndConditionId, languageCode,
								contentType, termAndConditionContent, termAndConditionVersionDescription, MIN_VERSION,
								TANDC_ACTIVE_STATUS_ID, loggedInUserDetailsName, loggedInUserId, result, leId);
						if (result.getParamByName(ErrorCodeEnum.ERROR_CODE_KEY) != null) {
							return result;
						}
					} else {
						createTermAndConditionText(termAndConditionId, languageCode, contentType,
								termAndConditionContent, termAndConditionVersionDescription, MIN_VERSION,
								TANDC_ACTIVE_STATUS_ID, loggedInUserDetailsName, loggedInUserId, result, leId);
						if (result.getParamByName(ErrorCodeEnum.ERROR_CODE_KEY) != null) {
							return result;
						}
					}
					return result;
				}

				if (termAndConditionActiveTextId == null) {
					alert.prepareError("Terms and conditions has no previous active version").log();
					return result;
				}
				//Not required for MS
				String newVersionId = null;
				newVersionId = getNewVersion(termAndConditionId, languageCode, result, leId);

				if (newVersionId == null) {
					alert.prepareError("Failed to create Terms and Conditions version").log();
					return result;
				}

				if (termAndConditionDraftTextId != null) {
					editTermAndConditionText(termAndConditionDraftTextId, termAndConditionId,
							languageCode, contentType, termAndConditionContent, termAndConditionVersionDescription,
							newVersionId, TANDC_ACTIVE_STATUS_ID, loggedInUserDetailsName, loggedInUserId, result, leId);
					if (result.getParamByName(ErrorCodeEnum.ERROR_CODE_KEY) != null) {
						return result;
					}
				} else {
					createTermAndConditionText(termAndConditionId, languageCode, contentType,
							termAndConditionContent, termAndConditionVersionDescription, newVersionId,
							TANDC_ACTIVE_STATUS_ID, loggedInUserDetailsName, loggedInUserId, result, leId);
					if (result.getParamByName(ErrorCodeEnum.ERROR_CODE_KEY) != null) {
						return result;
					}
				}
				editTermAndConditionTextStatus(termAndConditionActiveTextId, TANDC_ARCHIVED_STATUS_ID, loggedInUserDetailsName,
						result, leId);
				if (result.getParamByName(ErrorCodeEnum.ERROR_CODE_KEY) != null) {
					return result;
				}
			}

			result.addParam(new Param("status", "Success", FabricConstants.STRING));
		} catch (Exception e) {
			alert.prepareError("Failed to create Terms and Conditions version").log();
			ErrorCodeEnum.ERR_20277.setErrorCode(result);
			return result;
		}
		return result;
	}

	public void editTermAndConditionText(String termAndConditionTextId, String termAndConditionId, String languageCode,
			String contentType, String termAndConditionContent, String termAndConditionVersionDescription,
			String version, String statusId, String loggedInUserDetailsName, String loggedInUserId, Result result,
			String leId) {

		try {
			Map<String, Object> inputBodyMap = new HashMap<String, Object>();
			inputBodyMap.put("id", termAndConditionTextId);
			inputBodyMap.put("TermAndConditionId", termAndConditionId);
			inputBodyMap.put("LanguageCode", languageCode);
			inputBodyMap.put("ContentType_id", contentType);
			inputBodyMap.put("Content", termAndConditionContent);
			inputBodyMap.put("Description", termAndConditionVersionDescription);
			inputBodyMap.put("ContentModifiedBy", loggedInUserDetailsName);
			inputBodyMap.put("ContentModifiedOn", CommonUtilities.getISOFormattedLocalTimestamp());
			inputBodyMap.put("rtx", "Content");
			inputBodyMap.put("Version_Id", version);
			inputBodyMap.put("Status_id", statusId);
			inputBodyMap.put("modifiedby", loggedInUserDetailsName);
			inputBodyMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());
			String editTermAndConditionTextResponse =
					DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
					.withOperationId(OperationName.DB_TNCTEXT_UPDATE).withRequestParameters(inputBodyMap).build()
					.getResponse();			
			JSONObject editTermAndConditionTextResponseJSON = CommonUtilities
					.getStringAsJSONObject(editTermAndConditionTextResponse);
			if (!(editTermAndConditionTextResponseJSON != null
					&& editTermAndConditionTextResponseJSON.has(FabricConstants.OPSTATUS)
					&& editTermAndConditionTextResponseJSON.getInt(FabricConstants.OPSTATUS) == 0)) {
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				ErrorCodeEnum.ERR_20271.setErrorCode(result);
				alert.prepareError("Failed to update Terms and Conditions text").log();
				return;
			}
		} catch (Exception e) {
			alert.prepareError("Failed to edit Terms and Conditions version").log();
			ErrorCodeEnum.ERR_20278.setErrorCode(result);
			return;
		}
	}
	public void editTermAndConditionTextMS(String termAndConditionTextId,
			String termAndConditionId, String languageCode, String contentType, String termAndConditionContent,
			String termAndConditionVersionDescription, String version, String statusId, String loggedInUserDetails,String loggedInUserDetailsName,Result result) {

		try {

			//   		UserDetailsBean loggedInUserDetails = LoggedInUserHandler.getUserDetails(requestInstance);
			//          TODO: PUT call to  update exiting draft version 
			//			reference/termsConditionsContents/{termConditionAppsId}

			//			{
			//			  "language": "GB",
			//			  "description": "string",
			//			  "content": "string",
			//			  "contentType": "TEXT",
			//			  "saveAndPublish": true
			//			}

			Map<String, Object> inputBodyMap = new HashMap<String, Object>();
			inputBodyMap.put("language", termAndConditionTextId);
			inputBodyMap.put("description", termAndConditionId);
			inputBodyMap.put("content", languageCode);
			inputBodyMap.put("contentType", contentType);
			inputBodyMap.put("saveAndPublish",
					CommonUtilities.encodeToBase64(CommonUtilities.encodeURI(termAndConditionContent)));
			String editTermAndConditionTextResponse =
					DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
					.withOperationId(OperationName.DB_TNCTEXT_UPDATE).withRequestParameters(inputBodyMap).build()
					.getResponse();			
			JSONObject editTermAndConditionTextResponseJSON = CommonUtilities
					.getStringAsJSONObject(editTermAndConditionTextResponse);
			if (!(editTermAndConditionTextResponseJSON != null
					&& editTermAndConditionTextResponseJSON.has(FabricConstants.OPSTATUS)
					&& editTermAndConditionTextResponseJSON.getInt(FabricConstants.OPSTATUS) == 0)) {
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				ErrorCodeEnum.ERR_20271.setErrorCode(result);
				alert.prepareError("Failed to update Terms and Conditions text").log();
				return;
			}
		} catch (Exception e) {
			alert.prepareError("Failed to edit Terms and Conditions version").log();
			ErrorCodeEnum.ERR_20278.setErrorCode(result);
			return;
		}
	}

	public void editTermAndConditionTextStatus(String termAndConditionTextId,
			String statusId, String loggedInUserDetailsName, Result result, String leId) {

		try {

			Map<String, Object> inputBodyMap = new HashMap<String, Object>();
			inputBodyMap.put("id", termAndConditionTextId);
			inputBodyMap.put("Status_id", statusId);
			inputBodyMap.put("modifiedby", loggedInUserDetailsName);
			inputBodyMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());
			String editTermAndConditionTextResponse =
					DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
					.withOperationId(OperationName.DB_TNCTEXT_UPDATE).withRequestParameters(inputBodyMap).build()
					.getResponse();
			JSONObject editTermAndConditionTextResponseJSON = CommonUtilities
					.getStringAsJSONObject(editTermAndConditionTextResponse);
			if (!(editTermAndConditionTextResponseJSON != null
					&& editTermAndConditionTextResponseJSON.has(FabricConstants.OPSTATUS)
					&& editTermAndConditionTextResponseJSON.getInt(FabricConstants.OPSTATUS) == 0)) {
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				ErrorCodeEnum.ERR_20271.setErrorCode(result);
				alert.prepareError("Failed to update Terms and Conditions text").log();
				return;
			}
		} catch (Exception e) {
			alert.prepareError("Failed to edit Terms and Conditions version").log();
			ErrorCodeEnum.ERR_20278.setErrorCode(result);
			return;
		}
	}

	public void createTermAndConditionText(String termAndConditionId, String languageCode, String contentType,
			String termAndConditionContent, String termAndConditionVersionDescription, String version, String statusId,
			String loggedInUserDetailsName, String loggedInUserId, Result result, String leId) {

		try {			

			// Create termandconditiontext record
			String termAndConditionTextId = Long.toString(CommonUtilities.getNumericId());
			Map<String, Object> inputBodyMap = new HashMap<String, Object>();
			inputBodyMap.put("id", termAndConditionTextId);
			inputBodyMap.put("TermAndConditionId", termAndConditionId);
			inputBodyMap.put("LanguageCode", languageCode);
			inputBodyMap.put("ContentType_id", contentType);
			inputBodyMap.put("Content", CommonUtilities.encodeToBase64(CommonUtilities.encodeURI(termAndConditionContent)));
			inputBodyMap.put("Description", termAndConditionVersionDescription);
			inputBodyMap.put("ContentModifiedBy", loggedInUserDetailsName);
			inputBodyMap.put("ContentModifiedOn", CommonUtilities.getISOFormattedLocalTimestamp());
			inputBodyMap.put("rtx", "Content");
			inputBodyMap.put("Version_Id", version);
			inputBodyMap.put("Status_id", statusId);
			inputBodyMap.put("createdby", loggedInUserId);
			inputBodyMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
			String createTermAndConditionTextResponse =
					DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
					.withOperationId(OperationName.DB_TNCTEXT_CREATE).withRequestParameters(inputBodyMap).build()
					.getResponse();

			JSONObject createTermAndConditionTextResponseJSON = CommonUtilities
					.getStringAsJSONObject(createTermAndConditionTextResponse);
			if (!(createTermAndConditionTextResponseJSON != null
					&& createTermAndConditionTextResponseJSON.has(FabricConstants.OPSTATUS)
					&& createTermAndConditionTextResponseJSON.getInt(FabricConstants.OPSTATUS) == 0)) {
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				ErrorCodeEnum.ERR_20277.setErrorCode(result);
				alert.prepareError("Failed to create Terms and Conditions version").log();
				return;
			}
		} catch (Exception e) {
			alert.prepareError("Failed to create Terms and Conditions version").log();
			ErrorCodeEnum.ERR_20277.setErrorCode(result);
			return;
		}
	}

	public String getTermAndConditionTextId(String termAndConditionId, String languageCode, String statusId,
			Result result, String leId) throws DBPApplicationException {
		String termAndConditionTextId = null;
		Map<String, Object> inputBodyMap = new HashMap<String, Object>();
		inputBodyMap.put(ODataQueryConstants.FILTER,
				"TermAndConditionId eq '" + termAndConditionId + "' and LanguageCode eq '" + languageCode
						+ "' and Status_id eq '" + statusId + "'");
		inputBodyMap.put(ODataQueryConstants.SELECT, "id");
		String readTermAndConditionTextResponse =
				DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
				.withOperationId(OperationName.DB_TNCTEXT_GET).withRequestParameters(inputBodyMap).build()
				.getResponse();
		JSONObject readTermAndConditionTextResponseJSON = CommonUtilities
				.getStringAsJSONObject(readTermAndConditionTextResponse);
		if (readTermAndConditionTextResponseJSON != null
				&& readTermAndConditionTextResponseJSON.has(FabricConstants.OPSTATUS)
				&& readTermAndConditionTextResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
				&& readTermAndConditionTextResponseJSON.has("termandconditiontext")) {
			JSONArray readTermAndConditionTextJSONArray = readTermAndConditionTextResponseJSON
					.optJSONArray("termandconditiontext");
			if (!(readTermAndConditionTextJSONArray == null || readTermAndConditionTextJSONArray.length() < 1)) {
				JSONObject currTermAndConditionTextRecord = readTermAndConditionTextJSONArray.getJSONObject(0);
				termAndConditionTextId = currTermAndConditionTextRecord.getString("id");
			}
		}

		return termAndConditionTextId;
	}
	public String getTermAndConditionId(String termAndConditionCode, String leId) throws DBPApplicationException {

		String termAndConditionId = null;
		Map<String, Object> inputBodyMap = new HashMap<String, Object>();
		inputBodyMap.put(ODataQueryConstants.FILTER,
				"Code eq '" + termAndConditionCode + "'");
		inputBodyMap.put(ODataQueryConstants.SELECT, "id");
		Map<String, Object> headerMap = new HashMap<>();
		String readTermAndConditionResponse =
				DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
				.withOperationId(OperationName.DB_TNC_GET).withRequestHeaders(headerMap)
				.withRequestParameters(inputBodyMap).build()
				.getResponse();
		JSONObject readTermAndConditionResponseJSON = CommonUtilities
				.getStringAsJSONObject(readTermAndConditionResponse);
		if (readTermAndConditionResponseJSON != null && readTermAndConditionResponseJSON.has(FabricConstants.OPSTATUS)
				&& readTermAndConditionResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
				&& readTermAndConditionResponseJSON.has("termandcondition")) {
			JSONArray readTermAndConditionJSONArray = readTermAndConditionResponseJSON.optJSONArray("termandcondition");
			if (!(readTermAndConditionJSONArray == null || readTermAndConditionJSONArray.length() < 1)) {
				JSONObject currTermAndConditionRecord = readTermAndConditionJSONArray.getJSONObject(0);
				termAndConditionId = currTermAndConditionRecord.getString("id");
			}
		}

		return termAndConditionId;
	}
	public String getNewVersion(String termAndConditionId, String languageCode,
			Result result, String leId) throws DBPApplicationException {
		// Fetching termAndConditionText id from termandconditiontext table
		String latestVersion = null;
		String newVersionId = null;
		Map<String, Object> inputBodyMap = new HashMap<String, Object>();
		inputBodyMap.put(ODataQueryConstants.FILTER,
				"TermAndConditionId eq '" + termAndConditionId + "' and LanguageCode eq '" + languageCode
						+ "' and Status_id eq '" + TANDC_ACTIVE_STATUS_ID + "'");
		inputBodyMap.put(ODataQueryConstants.SELECT, "Version_Id");
		String readTermAndConditionTextResponse =
				DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
				.withOperationId(OperationName.DB_TNCTEXT_GET).withRequestParameters(inputBodyMap).build()
				.getResponse();
		JSONObject readTermAndConditionTextResponseJSON = CommonUtilities
				.getStringAsJSONObject(readTermAndConditionTextResponse);
		if (readTermAndConditionTextResponseJSON != null
				&& readTermAndConditionTextResponseJSON.has(FabricConstants.OPSTATUS)
				&& readTermAndConditionTextResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
				&& readTermAndConditionTextResponseJSON.has("termandconditiontext")) {
			JSONArray readTermAndConditionTextJSONArray = readTermAndConditionTextResponseJSON
					.optJSONArray("termandconditiontext");
			if (readTermAndConditionTextJSONArray == null || readTermAndConditionTextJSONArray.length() < 1) {
				alert.prepareError("Failed to get Terms and Conditions text").log();
				ErrorCodeEnum.ERR_20265.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return newVersionId;
			} else {
				JSONObject currTermAndConditionTextRecord = readTermAndConditionTextJSONArray.getJSONObject(0);
				latestVersion = currTermAndConditionTextRecord.getString("Version_Id");
			}
		} else {
			alert.prepareError("Failed to get Terms and Conditions text").log();
			ErrorCodeEnum.ERR_20265.setErrorCode(result);
			result.addParam(new Param("status", "Failure", FabricConstants.STRING));
			return newVersionId;
		}
		newVersionId = String.valueOf(round(Double.parseDouble(latestVersion) + 0.1, 1));
		return newVersionId;
	}
	public double round(double value, int precision) {
		int scale = (int) Math.pow(10, precision);
		return (double) Math.round(value * scale) / scale;
	}

	public Result getRequiredTermsAndConditions(Map<String, Object> postParametersMap, String backendToken)
			throws DBPApplicationException {
		Result result = new Result();
		try {
			final String termConditionCodeId;
			String consentType = "";
			JSONArray readTermAndConditionContentJSONArray = new JSONArray();

			// Fetching T&C id from termandcondition table
			Map<String, Object> inputBodyMap = new HashMap<String, Object>();
			String leId = (String) postParametersMap.get("legalEntityId");

			Map<String, Object> headerMap = new HashMap<>();
			headerMap.put("backendToken", backendToken);
			headerMap.put("companyid", leId);

			String readTermAndConditionContentResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(ServiceId.CONSENTMS)
					.withOperationId(OperationName.OP_GET_TERMS_AND_CONDITIONS_CONTENTS)
					.withRequestParameters(inputBodyMap).withRequestHeaders(headerMap).build().getResponse();

			JSONObject readTermAndConditionContentsResponseJSON = CommonUtilities
					.getStringAsJSONObject(readTermAndConditionContentResponse);
			if (readTermAndConditionContentsResponseJSON != null
					&& readTermAndConditionContentsResponseJSON.has(FabricConstants.OPSTATUS)
					&& readTermAndConditionContentsResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readTermAndConditionContentsResponseJSON.has("termsNConditionContents")) {
				readTermAndConditionContentJSONArray = readTermAndConditionContentsResponseJSON
						.optJSONArray("termsNConditionContents");
				if (readTermAndConditionContentJSONArray == null || readTermAndConditionContentJSONArray.length() < 1) {
					alert.prepareError("Failed to get Terms and Conditions").log();
					ErrorCodeEnum.ERR_20264.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				} else {
					Optional<Object> currTermNcondcontentRecord = StreamSupport
							.stream(readTermAndConditionContentJSONArray.spliterator(), true)
							.filter(item -> StringUtils.equalsIgnoreCase(
									((JSONObject) item).optString("termsNConditionContentId"),
									postParametersMap.get("termAndCondContentId").toString()))
							.findAny();
					JSONObject currTermNcondcontentRecordObj = new JSONObject();
					if (currTermNcondcontentRecord.isPresent()) {
						currTermNcondcontentRecordObj = (JSONObject) currTermNcondcontentRecord.get();
						termConditionCodeId = currTermNcondcontentRecordObj.optString("termConditionCodeId");
					} else {
						alert.prepareError("Failed to get Terms and Conditions").log();
						ErrorCodeEnum.ERR_20264.setErrorCode(result);
						result.addParam(new Param("status", "Failure", FabricConstants.STRING));
						return result;
					}
				}
			} else {
				alert.prepareError("Failed to get Terms and Conditions").log();
				ErrorCodeEnum.ERR_20264.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}

			inputBodyMap.clear();
			inputBodyMap.put("tncCodeIdRef", termConditionCodeId);
			headerMap = new HashMap<>();
			headerMap.put("backendToken", backendToken);
			headerMap.put("companyid", leId);

			String readTermAndConditionCodeResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(ServiceId.CONSENTMS).withOperationId(OperationName.OP_GET_TERMS_AND_CONDITIONS_CODE)
					.withRequestParameters(inputBodyMap).withRequestHeaders(headerMap).build().getResponse();

			JSONObject readTermAndConditionCodeResponseJSON = CommonUtilities
					.getStringAsJSONObject(readTermAndConditionCodeResponse);
			if (readTermAndConditionCodeResponseJSON != null
					&& readTermAndConditionCodeResponseJSON.has(FabricConstants.OPSTATUS)
					&& readTermAndConditionCodeResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readTermAndConditionCodeResponseJSON.has("termsNConditionCodes")) {
				JSONArray readTermAndConditionCodeJSONArray = readTermAndConditionCodeResponseJSON
						.optJSONArray("termsNConditionCodes");
				if (readTermAndConditionCodeJSONArray == null || readTermAndConditionCodeJSONArray.length() < 1) {
					alert.prepareError("Failed to get Terms and Conditions").log();
					ErrorCodeEnum.ERR_20264.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				} else {
					JSONObject currTermAndConditionCodeRecord = readTermAndConditionCodeJSONArray.optJSONObject(0);
					consentType = currTermAndConditionCodeRecord.optString("consentTypeId");
				}
			} else {
				alert.prepareError("Failed to get Terms and Conditions").log();
				ErrorCodeEnum.ERR_20264.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}

			Optional<Object> termAndConditionRecord = StreamSupport
					.stream(readTermAndConditionContentJSONArray.spliterator(), true)
					.filter(item -> ((JSONObject) item).optString("language")
							.equalsIgnoreCase(postParametersMap.get("languageCode").toString())
							&& ((JSONObject) item).optString("termConditionCodeId")
									.equalsIgnoreCase(termConditionCodeId)
							&& ((JSONObject) item).optString("versionNo")
									.equalsIgnoreCase(postParametersMap.get("version").toString()))
					.findAny();

			JSONObject termAndConditionRecordObj = new JSONObject();
			if (termAndConditionRecord.isPresent()) {
				termAndConditionRecordObj = (JSONObject) termAndConditionRecord.get();
				Param _currTermAndConditionContentParam = new Param("termsAndConditionsContent",
						termAndConditionRecordObj.optString("content"), FabricConstants.STRING);
				result.addParam(_currTermAndConditionContentParam);
				Param contentTypeId_Param = new Param("contentTypeId",
						termAndConditionRecordObj.optString("contentType"), FabricConstants.STRING);
				result.addParam(contentTypeId_Param);
				Param versionId_Param = new Param("versionId", termAndConditionRecordObj.optString("versionNo"),
						FabricConstants.STRING);
				result.addParam(versionId_Param);
				Param consentTypeId_Param = new Param("consentType", consentType, FabricConstants.STRING);
				result.addParam(consentTypeId_Param);
				result.addParam(new Param("status", "Success", FabricConstants.STRING));

				return result;
			} else {
				alert.prepareError("Failed to get Terms and Conditions").log();
				ErrorCodeEnum.ERR_20264.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}
		} catch (Exception e) {
			alert.prepareError("Failed to get Terms and Conditions text. Exception: ", e).log();
			result.addParam(new Param("status", "Failure", FabricConstants.STRING));
			ErrorCodeEnum.ERR_20265.setErrorCode(result);
		}
		return result;
	}
}
