package com.kony.adminconsole.service.mfa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service to Manage Requests related to MFAScenario
 * 
 * @author Chandan Gupta - KH2516
 *
 */

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.handler.EnvironmentConfigurationsHandler;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.core.config.EnvironmentConfiguration;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class MFAScenarioManageService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	private static final String GET_MFA_MODE_METHOD_NAME = "getMFAMode";
	private static final String GET_APP_METHOD_NAME = "getApp";
	private static final String GET_MFA_TYPE_METHOD_NAME = "getMFAType";
	private static final String GET_FREQUENCY_TYPE_METHOD_NAME = "getFrequencyType";
	private static final String GET_ACTION_METHOD_NAME = "getAction";
	private static final String CREATE_MFA_SCENARIO_METHOD_NAME = "createMFAScenario";
	private static final String EDIT_MFA_SCENARIO_METHOD_NAME = "editMFAScenario";
	private static final String DELETE_MFA_SCENARIO_METHOD_NAME = "deleteMFAScenario";
	private static final String GET_MFA_SCENARIO_METHOD_NAME = "getMFAScenario";
	private static final String GET_MFA_VARIABLE_REFERENCE_METHOD_NAME = "getMFAVariableReference";
	private static final String GET_MFA_FEATURE_METHOD_NAME = "getMFAFeature";

	// Adding below static variables for SCA Implementation.
	private static final String GET_SCA_MODE_METHOD_NAME = "getSCAMode";
	private static final String GET_SCA_SCENARIO_METHOD_NAME = "getSCAScenario";
	private static final String GET_SCA_ACTION_METHOD_NAME = "getSCAAction";
	private static final String GET_SCA_FEATURE_METHOD_NAME = "getSCAFeature";
	private static final String CREATE_SCA_SCENARIO_METHOD_NAME = "createSCAScenario";
	private static final String EDIT_SCA_SCENARIO_METHOD_NAME = "editSCAScenario";
	private static final String DELETE_SCA_SCENARIO_METHOD_NAME = "deleteSCAScenario";

	private static final String DEFAULT_FREQUENCY_TYPE_ID = "ALWAYS";

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		try {

			if (StringUtils.equalsIgnoreCase(methodID, GET_MFA_MODE_METHOD_NAME)) {
				return getMFAMode(requestInstance);
			} else if (StringUtils.equalsIgnoreCase(methodID, GET_SCA_MODE_METHOD_NAME)) {
				return getSCAMode(requestInstance);
			} else if (StringUtils.equalsIgnoreCase(methodID, GET_APP_METHOD_NAME)) {
				return getApp(requestInstance);
			} else if (StringUtils.equalsIgnoreCase(methodID, GET_MFA_TYPE_METHOD_NAME)) {
				return getMFAType(requestInstance);
			} else if (StringUtils.equalsIgnoreCase(methodID, GET_FREQUENCY_TYPE_METHOD_NAME)) {
				return getFrequencyType(requestInstance);
			} else if (StringUtils.equalsIgnoreCase(methodID, GET_ACTION_METHOD_NAME)) {
				return getAction(requestInstance);
			} else if (StringUtils.equalsIgnoreCase(methodID, GET_SCA_ACTION_METHOD_NAME)) {
				return getSCAAction(requestInstance);
			} else if (StringUtils.equalsIgnoreCase(methodID, CREATE_MFA_SCENARIO_METHOD_NAME)) {
				return createOrEditMFAScenario(requestInstance, CREATE_MFA_SCENARIO_METHOD_NAME);
			} else if (StringUtils.equalsIgnoreCase(methodID, CREATE_SCA_SCENARIO_METHOD_NAME)) {
				return createOrEditSCAScenario(requestInstance, CREATE_SCA_SCENARIO_METHOD_NAME);
			} else if (StringUtils.equalsIgnoreCase(methodID, EDIT_MFA_SCENARIO_METHOD_NAME)) {
				return createOrEditMFAScenario(requestInstance, EDIT_MFA_SCENARIO_METHOD_NAME);
			} else if (StringUtils.equalsIgnoreCase(methodID, EDIT_SCA_SCENARIO_METHOD_NAME)) {
				return createOrEditSCAScenario(requestInstance, EDIT_SCA_SCENARIO_METHOD_NAME);
			} else if (StringUtils.equalsIgnoreCase(methodID, DELETE_MFA_SCENARIO_METHOD_NAME)) {
				return deleteMFAScenario(requestInstance);
			} else if (StringUtils.equalsIgnoreCase(methodID, DELETE_SCA_SCENARIO_METHOD_NAME)) {
				return deleteSCAScenario(requestInstance);
			} else if (StringUtils.equalsIgnoreCase(methodID, GET_MFA_SCENARIO_METHOD_NAME)) {
				return getMFAScenario(requestInstance);
			} else if (StringUtils.equalsIgnoreCase(methodID, GET_MFA_VARIABLE_REFERENCE_METHOD_NAME)) {
				return getMFAVariableReference(requestInstance);
			} else if (StringUtils.equalsIgnoreCase(methodID, GET_SCA_SCENARIO_METHOD_NAME)) {
				return getSCAScenario(requestInstance);
			} else if (StringUtils.equalsIgnoreCase(methodID, GET_MFA_FEATURE_METHOD_NAME))
				return getMFAFeature(requestInstance);
			else if (StringUtils.equalsIgnoreCase(methodID, GET_SCA_FEATURE_METHOD_NAME))
				return getSCAFeature(requestInstance);
			return null;
		} catch (Exception e) {
			Result errorResult = new Result();
			diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
			ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
			return errorResult;
		}
	}

	public static JSONObject getClientAppIdMap(DataControllerRequest requestInstance) {
		try {
			String clientAppIdMapping = EnvironmentConfiguration.AC_APPID_TO_APP_MAPPING.getValue(requestInstance);
			if (StringUtils.isNotBlank(clientAppIdMapping)) {
				JSONObject clientAppIdMappingJson = new JSONObject(clientAppIdMapping);
				return clientAppIdMappingJson;
			}
		} catch (Exception e) {
			alert.prepareError("Failed while parsing runtime configuration AC_APPID_TO_APP_MAPPING", e).log();
		}
		return null;
	}

	public Result getMFAMode(DataControllerRequest requestInstance) {
		Result result = new Result();
		try {
			// Validate AppId
			String clientAppId = requestInstance.getParameter("appId");
			String appId = null;
			if (StringUtils.isBlank(clientAppId)) {
				ErrorCodeEnum.ERR_21318.setErrorCode(result);
				alert.prepareError("App Id cannot be empty").log();
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}
			JSONObject clientAppIdToServerMap = getClientAppIdMap(requestInstance);
			if (clientAppIdToServerMap == null) {
				ErrorCodeEnum.ERR_20887.setErrorCode(result);
				return result;
			}

			if (clientAppIdToServerMap.has(clientAppId)) {
				appId = clientAppIdToServerMap.getString(clientAppId);
			} else {
				ErrorCodeEnum.ERR_20888.setErrorCode(result);
				return result;
			}
			// Validate ActionId
			String actionId = requestInstance.getParameter("actionId");
			if (StringUtils.isBlank(actionId)) {
				ErrorCodeEnum.ERR_20866.setErrorCode(result);
				alert.prepareError("Action Id is a mandatory input").log();
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}

			// Fetch MFAId from featureaction table
			Map<String, String> inputMap = new HashMap<String, String>();
			inputMap.put(ODataQueryConstants.FILTER, "App_id eq '" + appId + "' and id eq '" + actionId + "'");
			inputMap.put(ODataQueryConstants.SELECT, "MFA_id, isMFAApplicable, Feature_id");

			String readFeatureActionResponse = Executor.invokeService(ServiceURLEnum.FEATUREACTION_READ, inputMap, null,
					requestInstance);

			String mfaId = null;
			String isMFAApplicable = null;
			String featureId = null;

			JSONObject readFeatureActionResponseJSON = CommonUtilities.getStringAsJSONObject(readFeatureActionResponse);
			if (readFeatureActionResponseJSON != null && readFeatureActionResponseJSON.has(FabricConstants.OPSTATUS)
					&& readFeatureActionResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readFeatureActionResponseJSON.has("featureaction")) {
				JSONArray readFeatureActionJSONArray = readFeatureActionResponseJSON.optJSONArray("featureaction");
				if (readFeatureActionJSONArray == null || readFeatureActionJSONArray.length() < 1) {
					ErrorCodeEnum.ERR_21334.setErrorCode(result);
					alert.prepareError("Invalid FeatureActionId").log();
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				} else {
					JSONObject currFeatureActionRecord = readFeatureActionJSONArray.getJSONObject(0);
					mfaId = currFeatureActionRecord.optString("MFA_id");
					isMFAApplicable = currFeatureActionRecord.getString("isMFAApplicable");
					featureId = currFeatureActionRecord.getString("Feature_id");
				}
			} else {
				ErrorCodeEnum.ERR_21334.setErrorCode(result);
				alert.prepareError("Invalid FeatureActionId").log();
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}

			if (StringUtils.equals(isMFAApplicable, "false")) {
				ErrorCodeEnum.ERR_21349.setErrorCode(result);
				alert.prepareError("MFA is not applicable").log();
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}

			// Fetch FeatureStatus from feature table
			inputMap.clear();
			inputMap.put(ODataQueryConstants.FILTER, "id eq '" + featureId + "'");
			inputMap.put(ODataQueryConstants.SELECT, "Status_id");

			String readFeatureResponse = Executor.invokeService(ServiceURLEnum.FEATURE_READ, inputMap, null,
					requestInstance);

			String featureStatus = null;

			JSONObject readFeatureResponseJSON = CommonUtilities.getStringAsJSONObject(readFeatureResponse);
			if (readFeatureResponseJSON != null && readFeatureResponseJSON.has(FabricConstants.OPSTATUS)
					&& readFeatureResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readFeatureResponseJSON.has("feature")) {
				JSONArray readFeatureJSONArray = readFeatureResponseJSON.optJSONArray("feature");
				if (readFeatureJSONArray == null || readFeatureJSONArray.length() < 1) {
					ErrorCodeEnum.ERR_21352.setErrorCode(result);
					alert.prepareError("Failed to fetch feature").log();
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				} else {
					JSONObject currFeatureRecord = readFeatureJSONArray.getJSONObject(0);
					featureStatus = currFeatureRecord.optString("Status_id");
					if (!StringUtils.equals(featureStatus, "SID_FEATURE_ACTIVE")) {
						ErrorCodeEnum.ERR_21353.setErrorCode(result);
						alert.prepareError("Feature is not active").log();
						result.addParam(new Param("status", "Feature is not active", FabricConstants.STRING));
						return result;
					}
				}
			} else {
				ErrorCodeEnum.ERR_21352.setErrorCode(result);
				alert.prepareError("Failed to fetch feature").log();
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}

			// Fetch mfa scenarios from mfa table
			inputMap.clear();
			inputMap.put(ODataQueryConstants.FILTER,
					"id eq '" + mfaId + "' and App_id eq '" + appId + "' and Action_id eq '" + actionId + "'");
			inputMap.put(ODataQueryConstants.SELECT,
					"Status_id, FrequencyType_id, FrequencyValue, PrimaryMFAType, SecondaryMFAType, SMSText, EmailSubject, EmailBody");

			String readMFAResponse = Executor.invokeService(ServiceURLEnum.MFA_READ, inputMap, null, requestInstance);

			String isMFARequired = null;
			String frequencyTypeId = null;
			String frequencyValue = null;
			String primaryMFATypeId = null;
			String secondaryMFATypeId = null;
			String smsText = null;
			String emailSubject = null;
			String emailBody = null;

			JSONObject readMFAResponseJSON = CommonUtilities.getStringAsJSONObject(readMFAResponse);
			if (readMFAResponseJSON != null && readMFAResponseJSON.has(FabricConstants.OPSTATUS)
					&& readMFAResponseJSON.getInt(FabricConstants.OPSTATUS) == 0 && readMFAResponseJSON.has("mfa")) {
				JSONArray readMFAJSONArray = readMFAResponseJSON.optJSONArray("mfa");
				if (readMFAJSONArray == null || readMFAJSONArray.length() < 1) {
					ErrorCodeEnum.ERR_21341.setErrorCode(result);
					alert.prepareError("Failed to fetch MFA Scenario").log();
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				} else {
					JSONObject currAppActionRecord = readMFAJSONArray.getJSONObject(0);
					String statusId = currAppActionRecord.optString("Status_id");
					if (statusId.equals("SID_ACTIVE"))
						isMFARequired = "true";
					else
						isMFARequired = "false";
					frequencyTypeId = currAppActionRecord.optString("FrequencyType_id");
					frequencyValue = currAppActionRecord.optString("FrequencyValue");
					primaryMFATypeId = currAppActionRecord.optString("PrimaryMFAType");
					secondaryMFATypeId = currAppActionRecord.optString("SecondaryMFAType");
					smsText = currAppActionRecord.optString("SMSText");
					emailSubject = currAppActionRecord.optString("EmailSubject");
					emailBody = currAppActionRecord.optString("EmailBody");
				}
			} else {
				alert.prepareError("MFA Scenario is empty").log();
				result.addParam(new Param("status", "Success", FabricConstants.STRING));
				return result;
			}

			Param isMFARequired_Param = new Param("isMFARequired", isMFARequired, FabricConstants.STRING);
			result.addParam(isMFARequired_Param);
			Param frequencyTypeId_Param = new Param("frequencyTypeId", frequencyTypeId, FabricConstants.STRING);
			result.addParam(frequencyTypeId_Param);
			Param frequencyValue_Param = new Param("frequencyValue", frequencyValue, FabricConstants.STRING);
			result.addParam(frequencyValue_Param);
			Param primaryMFATypeId_Param = new Param("primaryMFATypeId", primaryMFATypeId, FabricConstants.STRING);
			result.addParam(primaryMFATypeId_Param);
			Param secondaryMFATypeId_Param = new Param("secondaryMFATypeId", secondaryMFATypeId,
					FabricConstants.STRING);
			result.addParam(secondaryMFATypeId_Param);

			// Fetch mfaTypeIds from mfatype table
			inputMap.clear();
			inputMap.put(ODataQueryConstants.FILTER,
					"id eq '" + primaryMFATypeId + "' or id eq '" + secondaryMFATypeId + "'");
			inputMap.put(ODataQueryConstants.SELECT, "id, Name");

			String readMFATypeResponse = Executor.invokeService(ServiceURLEnum.MFATYPE_READ, inputMap, null,
					requestInstance);

			JSONObject readMFATypeResponseJSON = CommonUtilities.getStringAsJSONObject(readMFATypeResponse);
			if (readMFATypeResponseJSON != null && readMFATypeResponseJSON.has(FabricConstants.OPSTATUS)
					&& readMFATypeResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readMFATypeResponseJSON.has("mfatype")) {
				JSONArray readMFATypeJSONArray = readMFATypeResponseJSON.optJSONArray("mfatype");
				Dataset mfaTypeDataset = new Dataset();
				mfaTypeDataset.setId("mfaTypes");
				if ((readMFATypeJSONArray == null) || (readMFATypeJSONArray.length() < 2)) {
					ErrorCodeEnum.ERR_21340.setErrorCode(result);
					alert.prepareError("Primary mfa type and secondary mfa type is invalid").log();
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				} else {
					for (int indexVar = 0; indexVar < readMFATypeJSONArray.length(); indexVar++) {
						JSONObject mfaTypeJSONObject = readMFATypeJSONArray.getJSONObject(indexVar);
						Record currRecord = new Record();
						String mfaTypeId = mfaTypeJSONObject.getString("id");
						Param mfaTypeId_Param = new Param("mfaTypeId", mfaTypeId, FabricConstants.STRING);
						currRecord.addParam(mfaTypeId_Param);
						String mfaTypeName = mfaTypeJSONObject.getString("Name");
						Param mfaTypeName_Param = new Param("mfaTypeName", mfaTypeName, FabricConstants.STRING);
						currRecord.addParam(mfaTypeName_Param);

						// Adding MFA Scenarios fields if mfaType is Secure Access Code
						if (mfaTypeId.equals("SECURE_ACCESS_CODE")) {
							Param smsText_Param = new Param("smsText", smsText, FabricConstants.STRING);
							currRecord.addParam(smsText_Param);
							Param emailSubject_Param = new Param("emailSubject", emailSubject, FabricConstants.STRING);
							currRecord.addParam(emailSubject_Param);
							Param emailBody_Param = new Param("emailBody", emailBody, FabricConstants.STRING);
							currRecord.addParam(emailBody_Param);
						}

						// Fetching configurations
						inputMap.clear();
						inputMap.put(ODataQueryConstants.FILTER,
								"MFA_id eq '" + mfaTypeJSONObject.getString("id") + "'");
						inputMap.put(ODataQueryConstants.SELECT, "MFAKey_id, value");

						String readMFAConfigResponse = Executor.invokeService(ServiceURLEnum.MFACONFIGURATIONS_READ,
								inputMap, null, requestInstance);

						Dataset mfaConfigurationsDataset = new Dataset();
						mfaConfigurationsDataset.setId("mfaConfigurations");

						JSONObject readMFAConfigResponseJSON = CommonUtilities
								.getStringAsJSONObject(readMFAConfigResponse);
						if (readMFAConfigResponseJSON != null && readMFAConfigResponseJSON.has(FabricConstants.OPSTATUS)
								&& readMFAConfigResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
								&& readMFAConfigResponseJSON.has("mfaconfigurations")) {
							JSONArray readMFAConfigJSONArray = readMFAConfigResponseJSON
									.optJSONArray("mfaconfigurations");
							if ((readMFAConfigJSONArray != null) && (readMFAConfigJSONArray.length() > 0)) {
								for (int indxVar = 0; indxVar < readMFAConfigJSONArray.length(); indxVar++) {
									Record mfaKeyRecord = new Record();
									JSONObject mfaConfigJSONObject = readMFAConfigJSONArray.getJSONObject(indxVar);
									String mfaKeyId = mfaConfigJSONObject.getString("MFAKey_id");
									Param mfaKeyId_Param = new Param("mfaKey", mfaKeyId, FabricConstants.STRING);
									mfaKeyRecord.addParam(mfaKeyId_Param);
									String mfaKeyValue = mfaConfigJSONObject.optString("value");
									Param mfaKeyValue_Param = new Param("mfaValue", mfaKeyValue,
											FabricConstants.STRING);
									mfaKeyRecord.addParam(mfaKeyValue_Param);
									mfaConfigurationsDataset.addRecord(mfaKeyRecord);
								}
							}
						} else {
							result.addParam(new Param("message", readMFAConfigResponse, FabricConstants.STRING));
							alert.prepareError("Failed to Fetch MFAConfig Response: " + readMFAConfigResponse).log();
							result.addParam(new Param("status", "Failure", FabricConstants.STRING));
							ErrorCodeEnum.ERR_21304.setErrorCode(result);
							return result;
						}

						currRecord.addDataset(mfaConfigurationsDataset);
						mfaTypeDataset.addRecord(currRecord);
					}
					result.addDataset(mfaTypeDataset);
					return result;
				}
			} else {
				ErrorCodeEnum.ERR_21340.setErrorCode(result);
				alert.prepareError("Primary mfa type and secondary mfa type is invalid").log();
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}
		} catch (Exception e) {
			alert.prepareError("Unexepected Error in Fetching mfaConfiguration and Scenario. Exception: ", e).log();
			result.addParam(new Param("status", "Failure", FabricConstants.STRING));
			ErrorCodeEnum.ERR_21342.setErrorCode(result);
		}
		return result;
	}

	public Result getApp(DataControllerRequest requestInstance) {
		Result result = new Result();
		try {

			// Fetch app data from app table
			Map<String, String> inputMap = new HashMap<String, String>();
			inputMap.put(ODataQueryConstants.SELECT, "id, Name, Status_id");

			String readAppResponse = Executor.invokeService(ServiceURLEnum.APP_READ, inputMap, null, requestInstance);

			JSONObject readAppResponseJSON = CommonUtilities.getStringAsJSONObject(readAppResponse);
			if (readAppResponseJSON != null && readAppResponseJSON.has(FabricConstants.OPSTATUS)
					&& readAppResponseJSON.getInt(FabricConstants.OPSTATUS) == 0 && readAppResponseJSON.has("app")) {
				JSONArray readAppJSONArray = readAppResponseJSON.optJSONArray("app");
				Dataset appDataset = new Dataset();
				appDataset.setId("apps");
				if ((readAppJSONArray != null) && (readAppJSONArray.length() > 0)) {
					for (int indexVar = 0; indexVar < readAppJSONArray.length(); indexVar++) {
						JSONObject appJSONObject = readAppJSONArray.getJSONObject(indexVar);
						Record currRecord = new Record();
						String appId = appJSONObject.getString("id");
						Param appId_Param = new Param("appId", appId, FabricConstants.STRING);
						currRecord.addParam(appId_Param);
						String appName = appJSONObject.getString("Name");
						Param appName_Param = new Param("appName", appName, FabricConstants.STRING);
						currRecord.addParam(appName_Param);
						String appStatusId = appJSONObject.getString("Status_id");
						Param appStatusId_Param = new Param("statusId", appStatusId, FabricConstants.STRING);
						currRecord.addParam(appStatusId_Param);
						appDataset.addRecord(currRecord);
					}
					result.addDataset(appDataset);
					return result;
				}
			} else {
				result.addParam(new Param("message", readAppResponse, FabricConstants.STRING));
				alert.prepareError("Failed to Fetch App Response: " + readAppResponse).log();
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				ErrorCodeEnum.ERR_21308.setErrorCode(result);
				return result;
			}
		} catch (Exception e) {
			alert.prepareError("Unexepected Error in Fetching App data. Exception: ", e).log();
			result.addParam(new Param("status", "Failure", FabricConstants.STRING));
			ErrorCodeEnum.ERR_21308.setErrorCode(result);
		}
		return result;
	}

	public Result getMFAType(DataControllerRequest requestInstance) {
		Result result = new Result();
		try {

			// Fetch mfaType from mfatype table
			Map<String, String> inputMap = new HashMap<String, String>();
			inputMap.put(ODataQueryConstants.SELECT, "id, Name");

			String readMFATypeResponse = Executor.invokeService(ServiceURLEnum.MFATYPE_READ, inputMap, null,
					requestInstance);

			JSONObject readMFATypeResponseJSON = CommonUtilities.getStringAsJSONObject(readMFATypeResponse);
			if (readMFATypeResponseJSON != null && readMFATypeResponseJSON.has(FabricConstants.OPSTATUS)
					&& readMFATypeResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readMFATypeResponseJSON.has("mfatype")) {
				JSONArray readMFATypeJSONArray = readMFATypeResponseJSON.optJSONArray("mfatype");
				Dataset mfaTypeDataset = new Dataset();
				mfaTypeDataset.setId("mfaTypes");
				if ((readMFATypeJSONArray != null) && (readMFATypeJSONArray.length() > 0)) {
					for (int indexVar = 0; indexVar < readMFATypeJSONArray.length(); indexVar++) {
						JSONObject mfaTypeJSONObject = readMFATypeJSONArray.getJSONObject(indexVar);
						Record currRecord = new Record();
						String mfaTypeId = mfaTypeJSONObject.getString("id");
						Param mfaTypeId_Param = new Param("mfaTypeId", mfaTypeId, FabricConstants.STRING);
						currRecord.addParam(mfaTypeId_Param);
						String mfaTypeName = mfaTypeJSONObject.getString("Name");
						Param mfaTypeName_Param = new Param("mfaTypeName", mfaTypeName, FabricConstants.STRING);
						currRecord.addParam(mfaTypeName_Param);
						mfaTypeDataset.addRecord(currRecord);
					}
					result.addDataset(mfaTypeDataset);
					return result;
				}
			} else {
				result.addParam(new Param("message", readMFATypeResponse, FabricConstants.STRING));
				alert.prepareError("Failed to Fetch MFA Type Response: " + readMFATypeResponse).log();
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				ErrorCodeEnum.ERR_21305.setErrorCode(result);
				return result;
			}
		} catch (Exception e) {
			alert.prepareError("Unexepected Error in Fetching MFA Type. Exception: ", e).log();
			result.addParam(new Param("status", "Failure", FabricConstants.STRING));
			ErrorCodeEnum.ERR_21305.setErrorCode(result);
		}
		return result;
	}

	public Result getFrequencyType(DataControllerRequest requestInstance) {
		Result result = new Result();
		try {

			// Fetch frequencytype from frequencytype table
			Map<String, String> inputMap = new HashMap<String, String>();
			inputMap.put(ODataQueryConstants.SELECT, "id, Description");

			String readFrequencyTypeResponse = Executor.invokeService(ServiceURLEnum.FREQUENCYTYPE_READ, inputMap, null,
					requestInstance);

			JSONObject readFrequencyTypeResponseJSON = CommonUtilities.getStringAsJSONObject(readFrequencyTypeResponse);
			if (readFrequencyTypeResponseJSON != null && readFrequencyTypeResponseJSON.has(FabricConstants.OPSTATUS)
					&& readFrequencyTypeResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readFrequencyTypeResponseJSON.has("frequencytype")) {
				JSONArray readFrequencyTypeJSONArray = readFrequencyTypeResponseJSON.optJSONArray("frequencytype");
				Dataset frequencyTypeDataset = new Dataset();
				frequencyTypeDataset.setId("frequencyTypes");
				if ((readFrequencyTypeJSONArray != null) && (readFrequencyTypeJSONArray.length() > 0)) {
					for (int indexVar = 0; indexVar < readFrequencyTypeJSONArray.length(); indexVar++) {
						JSONObject frequencyTypeJSONObject = readFrequencyTypeJSONArray.getJSONObject(indexVar);
						Record currRecord = new Record();
						String frequencyTypeId = frequencyTypeJSONObject.getString("id");
						Param frequencyTypeId_Param = new Param("frequencyTypeId", frequencyTypeId,
								FabricConstants.STRING);
						currRecord.addParam(frequencyTypeId_Param);
						String frequencyTypeName = frequencyTypeJSONObject.getString("Description");
						Param frequencyTypeName_Param = new Param("frequencyTypeName", frequencyTypeName,
								FabricConstants.STRING);
						currRecord.addParam(frequencyTypeName_Param);
						frequencyTypeDataset.addRecord(currRecord);
					}
					result.addDataset(frequencyTypeDataset);
					return result;
				}
			} else {
				result.addParam(new Param("message", readFrequencyTypeResponse, FabricConstants.STRING));
				alert.prepareError("Failed to Fetch Frequency Type Response: " + readFrequencyTypeResponse).log();
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				ErrorCodeEnum.ERR_21309.setErrorCode(result);
				return result;
			}
		} catch (Exception e) {
			alert.prepareError("Unexepected Error in Fetching Frequency Type. Exception: ", e).log();
			result.addParam(new Param("status", "Failure", FabricConstants.STRING));
			ErrorCodeEnum.ERR_21309.setErrorCode(result);
		}
		return result;
	}

	public Result getAction(DataControllerRequest requestInstance) {
		Result result = new Result();
		try {

			String actionType = requestInstance.getParameter("actionType");

			String featureId = requestInstance.getParameter("featureId");
			if (StringUtils.isBlank(featureId)) {
				ErrorCodeEnum.ERR_21350.setErrorCode(result);
				alert.prepareError("Feature Id cannot be empty").log();
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}

			// Fetch action list from featureaction table
			Map<String, String> inputMap = new HashMap<String, String>();
			if (!StringUtils.isBlank(actionType))
				inputMap.put(ODataQueryConstants.FILTER,
						"Type_id eq '" + actionType + "' and Feature_id eq '" + featureId + "'");
			else
				inputMap.put(ODataQueryConstants.FILTER, "Feature_id eq '" + featureId + "'");
			inputMap.put(ODataQueryConstants.SELECT, "id, name, isMFAApplicable, Type_id, MFA_id");

			String readActionResponse = Executor.invokeService(ServiceURLEnum.FEATUREACTION_READ, inputMap, null,
					requestInstance);

			JSONObject readActionResponseJSON = CommonUtilities.getStringAsJSONObject(readActionResponse);
			if (readActionResponseJSON != null && readActionResponseJSON.has(FabricConstants.OPSTATUS)
					&& readActionResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readActionResponseJSON.has("featureaction")) {
				JSONArray readActionJSONArray = readActionResponseJSON.optJSONArray("featureaction");
				Dataset actionDataset = new Dataset();
				actionDataset.setId("actions");
				if ((readActionJSONArray != null) && (readActionJSONArray.length() > 0)) {
					for (int indexVar = 0; indexVar < readActionJSONArray.length(); indexVar++) {
						JSONObject actionJSONObject = readActionJSONArray.getJSONObject(indexVar);
						String isMFAApplicable = null;
						isMFAApplicable = actionJSONObject.optString("isMFAApplicable");
						String mfaId = null;
						mfaId = actionJSONObject.optString("MFA_id");
						if (StringUtils.equals(isMFAApplicable, "false") || (!StringUtils.equals(mfaId, "")))
							continue;
						Record currRecord = new Record();
						String actionId = actionJSONObject.getString("id");
						Param actionId_Param = new Param("actionId", actionId, FabricConstants.STRING);
						currRecord.addParam(actionId_Param);
						String actionName = actionJSONObject.getString("name");
						Param actionName_Param = new Param("actionName", actionName, FabricConstants.STRING);
						currRecord.addParam(actionName_Param);
						String type = actionJSONObject.getString("Type_id");
						Param actionType_Param = new Param("actionType", type, FabricConstants.STRING);
						currRecord.addParam(actionType_Param);
						actionDataset.addRecord(currRecord);
					}
					result.addDataset(actionDataset);
					return result;
				}
			} else {
				result.addParam(new Param("message", readActionResponse, FabricConstants.STRING));
				alert.prepareError("Failed to Fetch Action Response: " + readActionResponse).log();
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				ErrorCodeEnum.ERR_21310.setErrorCode(result);
				return result;
			}
		} catch (Exception e) {
			alert.prepareError("Unexepected Error in Fetching Action. Exception: ", e).log();
			result.addParam(new Param("status", "Failure", FabricConstants.STRING));
			ErrorCodeEnum.ERR_21310.setErrorCode(result);
		}
		return result;
	}

	public Result getMFAFeature(DataControllerRequest requestInstance) {
		Result result = new Result();
		try {

			// Fetch feature list from feature and featureaction table
			Map<String, String> inputMap = new HashMap<String, String>();
			inputMap.put(ODataQueryConstants.SELECT, "feature_id, feature_name,App_id");

			String readFeatureResponse = Executor.invokeService(ServiceURLEnum.MFA_C360_FEATURE_GET_PROC, inputMap,
					null, requestInstance);
			diagnostic.prepareDebug("readFeatureResponse ##"+ readFeatureResponse.toString()).log();

			JSONObject readFeatureResponseJSON = CommonUtilities.getStringAsJSONObject(readFeatureResponse);
			diagnostic.prepareDebug("readFeatureResponse JSON##"+ readFeatureResponseJSON.toString()).log();
			if (readFeatureResponseJSON != null && readFeatureResponseJSON.has(FabricConstants.OPSTATUS)
					&& readFeatureResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readFeatureResponseJSON.has("records")) {
				JSONArray readFeatureJSONArray = readFeatureResponseJSON.optJSONArray("records");
				diagnostic.prepareDebug("readFeatureJSONArray records##"+ readFeatureJSONArray.toString()).log();
				Dataset featureDataset = new Dataset();
				featureDataset.setId("features");
				if ((readFeatureJSONArray != null) && (readFeatureJSONArray.length() > 0)) {
					for (int indexVar = 0; indexVar < readFeatureJSONArray.length(); indexVar++) {
						JSONObject featureJSONObject = readFeatureJSONArray.getJSONObject(indexVar);
						Record currRecord = new Record();
						String featureId = featureJSONObject.getString("feature_id");
						String appId = featureJSONObject.getString("App_id");
						diagnostic.prepareDebug("appId##"+indexVar + "###" +appId).log();
						diagnostic.prepareDebug("featureId##"+indexVar + "###" + featureId).log();
						// Checking for already existing mfa records
						inputMap.clear();
						inputMap.put(ODataQueryConstants.SELECT, "id");
						inputMap.put(ODataQueryConstants.FILTER,
								"MFA_id eq NULL and isMFAApplicable eq true and Feature_id eq'" + featureId
										+ "'and App_id eq'" + appId + "'");
						String readActionResponse = Executor.invokeService(ServiceURLEnum.FEATUREACTION_READ, inputMap,
								null, requestInstance);
						diagnostic.prepareDebug("readActionResponse##"+indexVar + "###" + readActionResponse).log();
						JSONObject readActionResponseJSON = CommonUtilities.getStringAsJSONObject(readActionResponse);
						if (readActionResponseJSON != null && readActionResponseJSON.has(FabricConstants.OPSTATUS)
								&& readActionResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
								&& readActionResponseJSON.has("featureaction")) {
							JSONArray readActionJSONArray = readActionResponseJSON.optJSONArray("featureaction");
							if (!((readActionJSONArray != null) && (readActionJSONArray.length() > 0)))
								continue;
						}
						Param featureId_Param = new Param("featureId", featureId, FabricConstants.STRING);
						currRecord.addParam(featureId_Param);
						String featureName = featureJSONObject.getString("feature_name");
						Param featureName_Param = new Param("featureName", featureName, FabricConstants.STRING);
						currRecord.addParam(featureName_Param);
						Param appId_Param = new Param("appId", appId, FabricConstants.STRING);
						currRecord.addParam(appId_Param);

						featureDataset.addRecord(currRecord);
					}
					result.addDataset(featureDataset);
					return result;
				}
			} else {
				result.addParam(new Param("message", readFeatureResponse, FabricConstants.STRING));
				alert.prepareError("Failed to Fetch Feature Response: " + readFeatureResponse).log();
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				ErrorCodeEnum.ERR_21352.setErrorCode(result);
				return result;
			}
		} catch (Exception e) {
			alert.prepareError("Unexepected Error in Fetching Feature. Exception: ", e).log();
			result.addParam(new Param("status", "Failure", FabricConstants.STRING));
			ErrorCodeEnum.ERR_21352.setErrorCode(result);
		}
		return result;
	}

	public Result createOrEditMFAScenario(DataControllerRequest requestInstance, String methodName) {

		Result result = new Result();
		EventEnum event = EventEnum.UPDATE;
		String eventFailureDescription = "MFA Scenario update failed";

		try {
			String loggedInUserId = null;
			UserDetailsBean loggedInUserDetails = LoggedInUserHandler.getUserDetails(requestInstance);
			if (loggedInUserDetails != null) {
				loggedInUserId = loggedInUserDetails.getId();
			}

			if (methodName.equals(CREATE_MFA_SCENARIO_METHOD_NAME)) {
				event = EventEnum.CREATE;
				eventFailureDescription = "MFA Scenario create failed";
			}

			// Validate AppId
			String appId = requestInstance.getParameter("appId");
			if (StringUtils.isBlank(appId)) {
				alert.prepareError("App Id cannot be empty").log();
				ErrorCodeEnum.ERR_21318.setErrorCode(result);
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
						ActivityStatusEnum.FAILED, eventFailureDescription);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}
			// Validate ActionId
			String appName = null;
			Map<String, String> inputMap = new HashMap<String, String>();
			inputMap.put(ODataQueryConstants.FILTER, "id eq '" + appId + "'");
			inputMap.put(ODataQueryConstants.SELECT, "id, Name");

			String readAppResponse = Executor.invokeService(ServiceURLEnum.APP_READ, inputMap, null, requestInstance);

			JSONObject readAppResponseJSON = CommonUtilities.getStringAsJSONObject(readAppResponse);
			if (readAppResponseJSON != null && readAppResponseJSON.has(FabricConstants.OPSTATUS)
					&& readAppResponseJSON.getInt(FabricConstants.OPSTATUS) == 0 && readAppResponseJSON.has("app")) {
				JSONArray readAppJSONArray = readAppResponseJSON.optJSONArray("app");
				if (readAppJSONArray == null || readAppJSONArray.length() < 1) {
					alert.prepareError("Invalid App Id").log();
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
							ActivityStatusEnum.FAILED, eventFailureDescription);
					ErrorCodeEnum.ERR_21319.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				} else {
					JSONObject currAppRecord = readAppJSONArray.getJSONObject(0);
					appName = currAppRecord.getString("Name");
				}
			} else {
				alert.prepareError("Invalid App Id").log();
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
						ActivityStatusEnum.FAILED, eventFailureDescription);
				ErrorCodeEnum.ERR_21319.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}
			String actionId = requestInstance.getParameter("actionId");
			if (StringUtils.isBlank(actionId)) {
				alert.prepareError("Action Id is a mandatory input").log();
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
						ActivityStatusEnum.FAILED, eventFailureDescription);
				ErrorCodeEnum.ERR_20866.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}

			// Validate ActionType
			String actionType = requestInstance.getParameter("actionType");
			if (StringUtils.isBlank(actionType)) {
				alert.prepareError("Action type cannot be empty").log();
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
						ActivityStatusEnum.FAILED, eventFailureDescription);
				ErrorCodeEnum.ERR_20783.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}

			// Fetch ActionName and MFAId from featureaction table
			String actionName = null;
			String mfaId = null;
			String isMFAApplicable = null;
			inputMap.put(ODataQueryConstants.FILTER, "id eq '" + actionId + "' and Type_id eq '" + actionType + "'");
			inputMap.put(ODataQueryConstants.SELECT, "name, MFA_id, isMFAApplicable");

			String readActionResponse = Executor.invokeService(ServiceURLEnum.FEATUREACTION_READ, inputMap, null,
					requestInstance);

			JSONObject readActionResponseJSON = CommonUtilities.getStringAsJSONObject(readActionResponse);
			if (readActionResponseJSON != null && readActionResponseJSON.has(FabricConstants.OPSTATUS)
					&& readActionResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readActionResponseJSON.has("featureaction")) {
				JSONArray readActionJSONArray = readActionResponseJSON.optJSONArray("featureaction");
				if (readActionJSONArray == null || readActionJSONArray.length() < 1) {
					alert.prepareError("No record found for action id and action type").log();
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
							ActivityStatusEnum.FAILED, eventFailureDescription);
					ErrorCodeEnum.ERR_21320.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				} else {
					JSONObject currActionRecord = readActionJSONArray.getJSONObject(0);
					actionName = currActionRecord.getString("name");
					mfaId = currActionRecord.optString("MFA_id");
					isMFAApplicable = currActionRecord.optString("isMFAApplicable");
				}
			} else {
				alert.prepareError("No record found for action id and action type").log();
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
						ActivityStatusEnum.FAILED, eventFailureDescription);
				ErrorCodeEnum.ERR_21320.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}

			if (StringUtils.equals(isMFAApplicable, "false")) {
				ErrorCodeEnum.ERR_21349.setErrorCode(result);
				alert.prepareError("MFA is not applicable").log();
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}

			String frequencyTypeId = null, frequencyValue = null, frequencyTypeDescription = "Always";

			if (actionType.equals("MONETARY")) {

				// Validate FrequencyTypeId
				frequencyTypeId = requestInstance.getParameter("frequencyTypeId");
				if (StringUtils.isBlank(frequencyTypeId)) {
					alert.prepareError("Frequency Type Id cannot be empty").log();
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
							ActivityStatusEnum.FAILED, eventFailureDescription);
					ErrorCodeEnum.ERR_21321.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				}

				// Fetch frequencyTypeId from frequencytype table
				inputMap.clear();
				inputMap.put(ODataQueryConstants.FILTER, "id eq '" + frequencyTypeId + "'");
				inputMap.put(ODataQueryConstants.SELECT, "id, Description");

				String readFrequencyTypeResponse = Executor.invokeService(ServiceURLEnum.FREQUENCYTYPE_READ, inputMap,
						null, requestInstance);

				JSONObject readFrequencyTypeResponseJSON = CommonUtilities
						.getStringAsJSONObject(readFrequencyTypeResponse);
				if (readFrequencyTypeResponseJSON != null && readFrequencyTypeResponseJSON.has(FabricConstants.OPSTATUS)
						&& readFrequencyTypeResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
						&& readFrequencyTypeResponseJSON.has("frequencytype")) {
					JSONArray readFrequencyTypeJSONArray = readFrequencyTypeResponseJSON.optJSONArray("frequencytype");
					if (readFrequencyTypeJSONArray == null || readFrequencyTypeJSONArray.length() < 1) {
						ErrorCodeEnum.ERR_21322.setErrorCode(result);
						alert.prepareError("Invalid Frequency Type Id").log();
						AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
								ActivityStatusEnum.FAILED, eventFailureDescription);
						result.addParam(new Param("status", "Failure", FabricConstants.STRING));
						return result;
					} else {
						JSONObject currFrequencyTypeRecord = readFrequencyTypeJSONArray.getJSONObject(0);
						frequencyTypeDescription = currFrequencyTypeRecord.getString("Description");
					}
				} else {
					ErrorCodeEnum.ERR_21322.setErrorCode(result);
					alert.prepareError("Invalid Frequency Type Id").log();
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
							ActivityStatusEnum.FAILED, eventFailureDescription);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				}

				if (frequencyTypeId.equals("VALUE_BASED")) {
					// Validate FrequencyValue
					frequencyValue = requestInstance.getParameter("frequencyValue");
					if (StringUtils.isBlank(frequencyValue)) {
						ErrorCodeEnum.ERR_21323.setErrorCode(result);
						alert.prepareError("Frequency Value cannot be empty").log();
						AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
								ActivityStatusEnum.FAILED, eventFailureDescription);
						result.addParam(new Param("status", "Failure", FabricConstants.STRING));
						return result;
					}
				}
			} else {
				frequencyTypeId = DEFAULT_FREQUENCY_TYPE_ID;
			}

			// Validate mfaScenarioDescription
			String mfaScenarioDescription = requestInstance.getParameter("mfaScenarioDescription");
			if (StringUtils.isBlank(mfaScenarioDescription)) {
				ErrorCodeEnum.ERR_21324.setErrorCode(result);
				alert.prepareError("MFA Scenario Description cannot be empty").log();
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
						ActivityStatusEnum.FAILED, eventFailureDescription);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}

			// Validate PrimaryMFATypeId
			String primaryMFATypeId = requestInstance.getParameter("primaryMFATypeId");
			if (StringUtils.isBlank(primaryMFATypeId)) {
				alert.prepareError("MFAType cannot be empty").log();
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
						ActivityStatusEnum.FAILED, eventFailureDescription);
				ErrorCodeEnum.ERR_21300.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}

			// Validate SecondaryMFATypeId
			String secondaryMFATypeId = requestInstance.getParameter("secondaryMFATypeId");
			if (StringUtils.isBlank(secondaryMFATypeId)) {
				alert.prepareError("MFAType cannot be empty").log();
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
						ActivityStatusEnum.FAILED, eventFailureDescription);
				ErrorCodeEnum.ERR_21300.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}

			if (primaryMFATypeId.equals(secondaryMFATypeId)) {
				alert.prepareError("Primary MFA Type and Secondary MFA Type should be different").log();
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
						ActivityStatusEnum.FAILED, eventFailureDescription);
				ErrorCodeEnum.ERR_21325.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}

			// Fetch mfaTypeId from mfatype table
			String primaryMFATypeName = null, secondaryMFATypeName = null;
			inputMap.clear();
			inputMap.put(ODataQueryConstants.FILTER,
					"id eq '" + primaryMFATypeId + "' or id eq '" + secondaryMFATypeId + "'");
			inputMap.put(ODataQueryConstants.SELECT, "id, Name");

			String readMFATypeResponse = Executor.invokeService(ServiceURLEnum.MFATYPE_READ, inputMap, null,
					requestInstance);

			JSONObject readMFATypeResponseJSON = CommonUtilities.getStringAsJSONObject(readMFATypeResponse);
			if (readMFATypeResponseJSON != null && readMFATypeResponseJSON.has(FabricConstants.OPSTATUS)
					&& readMFATypeResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readMFATypeResponseJSON.has("mfatype")) {
				JSONArray readMFATypeJSONArray = readMFATypeResponseJSON.optJSONArray("mfatype");
				if (readMFATypeJSONArray == null || readMFATypeJSONArray.length() < 2) {
					alert.prepareError("Invalid MFA Type").log();
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
							ActivityStatusEnum.FAILED, eventFailureDescription);
					ErrorCodeEnum.ERR_21326.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				} else {
					String currMFATypeId = null;
					for (int i = 0; i < readMFATypeJSONArray.length(); i++) {
						JSONObject readMFATypeObj = readMFATypeJSONArray.getJSONObject(i);
						currMFATypeId = readMFATypeObj.getString("id");
						if (currMFATypeId.equals(primaryMFATypeId)) {
							primaryMFATypeName = readMFATypeObj.getString("Name");
						} else if (currMFATypeId.equals(secondaryMFATypeId)) {
							secondaryMFATypeName = readMFATypeObj.getString("Name");
						}
					}
				}
			} else {
				alert.prepareError("Invalid MFA Type").log();
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
						ActivityStatusEnum.FAILED, eventFailureDescription);
				ErrorCodeEnum.ERR_21326.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}

			String smsText = "";
			String emailSubject = "";
			String emailBody = "";

			if (primaryMFATypeId.equals("SECURE_ACCESS_CODE") || secondaryMFATypeId.equals("SECURE_ACCESS_CODE")) {

				// Validate SMS Text
				smsText = requestInstance.getParameter("smsText");
				if (StringUtils.isBlank(smsText)) {
					ErrorCodeEnum.ERR_21329.setErrorCode(result);
					alert.prepareError("SMS Text cannot be empty").log();
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
							ActivityStatusEnum.FAILED, eventFailureDescription);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				}

				// Validate EmailSubject
				emailSubject = requestInstance.getParameter("emailSubject");
				if (StringUtils.isBlank(emailSubject)) {
					ErrorCodeEnum.ERR_21330.setErrorCode(result);
					alert.prepareError("Email Subject cannot be empty").log();
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
							ActivityStatusEnum.FAILED, eventFailureDescription);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				}

				// Validate EmailBody
				emailBody = requestInstance.getParameter("emailBody");
				if (StringUtils.isBlank(emailBody)) {
					ErrorCodeEnum.ERR_21331.setErrorCode(result);
					alert.prepareError("Email Body cannot be empty").log();
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
							ActivityStatusEnum.FAILED, eventFailureDescription);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				}
			}

			// Validate MFA Scenario Status Id
			String mfaScenarioStatusId = requestInstance.getParameter("mfaScenarioStatusId");
			if (StringUtils.isBlank(mfaScenarioStatusId)) {
				ErrorCodeEnum.ERR_21332.setErrorCode(result);
				alert.prepareError("MFA Scenario StatusId cannot be empty").log();
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
						ActivityStatusEnum.FAILED, eventFailureDescription);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}

			String mfaScenarioStatusName = null;
			if (mfaScenarioStatusId.equals("SID_ACTIVE"))
				mfaScenarioStatusName = "Active";
			else
				mfaScenarioStatusName = "Inactive";

			if (!(mfaScenarioStatusId.equals("SID_ACTIVE") || mfaScenarioStatusId.equals("SID_INACTIVE"))) {
				ErrorCodeEnum.ERR_21333.setErrorCode(result);
				alert.prepareError("Invalid status ID for MFA scenario").log();
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
						ActivityStatusEnum.FAILED, eventFailureDescription);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}

			inputMap.clear();

			inputMap.put("App_id", appId);
			inputMap.put("Action_id", actionId);
			inputMap.put("FrequencyType_id", frequencyTypeId);
			inputMap.put("FrequencyValue", frequencyValue);
			inputMap.put("Status_id", mfaScenarioStatusId);
			inputMap.put("Description", mfaScenarioDescription);
			inputMap.put("PrimaryMFAType", primaryMFATypeId);
			inputMap.put("SecondaryMFAType", secondaryMFATypeId);
			inputMap.put("SMSText", smsText);
			inputMap.put("EmailSubject", emailSubject);
			inputMap.put("EmailBody", emailBody);

			if (methodName.equals(CREATE_MFA_SCENARIO_METHOD_NAME)) {
				if (!StringUtils.equals(mfaId, "")) {
					alert.prepareError("MFA Scenario already present for ActionId").log();
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
							ActivityStatusEnum.FAILED, eventFailureDescription);
					ErrorCodeEnum.ERR_21345.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				}

				// Creating MFA Scenario
				String legalUnit = EnvironmentConfigurationsHandler.getServerAppPropertyValue("BRANCH_ID_REFERENCE", requestInstance);
				String mfaScenarioId = Long.toString(CommonUtilities.getNumericId());
				inputMap.put("id", mfaScenarioId);
				inputMap.put("createdby", loggedInUserId);
				inputMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
				inputMap.put("companyLegalUnit", legalUnit);

				String createMFAResponse = Executor.invokeService(ServiceURLEnum.MFA_CREATE, inputMap, null,
						requestInstance);

				JSONObject createMFAResponseJSON = CommonUtilities.getStringAsJSONObject(createMFAResponse);
				if ((createMFAResponseJSON != null) && createMFAResponseJSON.has(FabricConstants.OPSTATUS)
						&& createMFAResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
					diagnostic.prepareDebug("MFA Scenario created successfully.").log();
					if (frequencyTypeId.equals("ALWAYS"))
						frequencyValue = "NA";
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
							ActivityStatusEnum.SUCCESSFUL,
							"MFA Scenario created successfully. " + "/" + "App:" + appName + "Status:"
									+ mfaScenarioStatusName + "/" + "Action type:" + actionType + "/" + "Action:"
									+ actionName + "/" + "Frequency:" + frequencyTypeDescription + "/" + "Value:"
									+ frequencyValue + "/" + "MFA scenario description:" + mfaScenarioDescription + "/"
									+ "Primary MFA:" + primaryMFATypeName + "/" + "Secondary MFA:"
									+ secondaryMFATypeName);
					result.addParam(new Param("status", "Success", FabricConstants.STRING));
				} else {
					alert.prepareError("Failed to create MFA Scenario").log();
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
							ActivityStatusEnum.FAILED, eventFailureDescription);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					ErrorCodeEnum.ERR_21335.setErrorCode(result);
					return result;
				}

				// Updating MFAId in featureaction table
				inputMap.clear();
				inputMap.put("id", actionId);
				inputMap.put("MFA_id", mfaScenarioId);
				inputMap.put("companyLegalUnit", legalUnit);
				diagnostic.prepareDebug("inputMap ##"+ inputMap.toString()).log();
				Executor.invokeService(ServiceURLEnum.FEATUREACTION_UPDATE, inputMap, null, requestInstance);

			} else if (methodName.equals(EDIT_MFA_SCENARIO_METHOD_NAME)) {

				String currentAppId = requestInstance.getParameter("appId");
				if (StringUtils.isBlank(currentAppId)) {
					ErrorCodeEnum.ERR_21343.setErrorCode(result);
					alert.prepareError("App Id cannot be empty").log();
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
							ActivityStatusEnum.FAILED, eventFailureDescription);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				}
				String currentActionId = requestInstance.getParameter("actionId");
				if (StringUtils.isBlank(currentActionId)) {
					ErrorCodeEnum.ERR_21343.setErrorCode(result);
					alert.prepareError("AppActionId cannot be empty").log();
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
							ActivityStatusEnum.FAILED, eventFailureDescription);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				}
				// Getting mfa scenario data from mfa table
				Map<String, String> inputParameterMap = new HashMap<String, String>();

				inputParameterMap.put(ODataQueryConstants.FILTER, "id eq '" + mfaId + "' and App_id eq '" + currentAppId
						+ "' and Action_id eq '" + currentActionId + "'");
				inputParameterMap.put(ODataQueryConstants.SELECT,
						"id, FrequencyType_id, FrequencyValue, Status_id, Description, PrimaryMFAType, SecondaryMFAType");

				String readMFAResponse = Executor.invokeService(ServiceURLEnum.MFA_READ, inputParameterMap, null,
						requestInstance);

				String oldFrequencyTypeId = null;
				String oldFrequencyValue = null;
				String oldStatusId = null;
				String oldDescription = null;
				String oldPrimaryMFAType = null;
				String oldSecondaryMFAType = null;

				JSONObject readMFAResponseJSON = CommonUtilities.getStringAsJSONObject(readMFAResponse);
				if (readMFAResponseJSON != null && readMFAResponseJSON.has(FabricConstants.OPSTATUS)
						&& readMFAResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
						&& readMFAResponseJSON.has("mfa")) {
					JSONArray readMFAJSONArray = readMFAResponseJSON.optJSONArray("mfa");
					if (readMFAJSONArray == null || readMFAJSONArray.length() < 1) {
						ErrorCodeEnum.ERR_21336.setErrorCode(result);
						alert.prepareError("MFA scenario is absent").log();
						AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
								ActivityStatusEnum.FAILED, eventFailureDescription);
						result.addParam(new Param("status", "Failure", FabricConstants.STRING));
						return result;
					} else {
						JSONObject mfaRecord = readMFAJSONArray.getJSONObject(0);
						oldFrequencyTypeId = mfaRecord.getString("FrequencyType_id");
						if (oldFrequencyTypeId.equals("VALUE_BASED"))
							oldFrequencyValue = mfaRecord.getString("FrequencyValue");
						oldStatusId = mfaRecord.getString("Status_id");
						oldDescription = mfaRecord.getString("Description");
						oldPrimaryMFAType = mfaRecord.getString("PrimaryMFAType");
						oldSecondaryMFAType = mfaRecord.getString("SecondaryMFAType");
					}
				} else {
					ErrorCodeEnum.ERR_21336.setErrorCode(result);
					alert.prepareError("MFA scenario is absent").log();
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
							ActivityStatusEnum.FAILED, eventFailureDescription);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				}

				// Checking if AppId and ActionId combination already present in appactionmfa
				// table with different id
				inputParameterMap.clear();
				inputParameterMap.put(ODataQueryConstants.FILTER,
						"App_id eq '" + appId + "' and Action_id eq '" + actionId + "'");
				inputParameterMap.put(ODataQueryConstants.SELECT, "id");

				readMFAResponse = Executor.invokeService(ServiceURLEnum.MFA_READ, inputParameterMap, null,
						requestInstance);

				readMFAResponseJSON = CommonUtilities.getStringAsJSONObject(readMFAResponse);
				if (readMFAResponseJSON != null && readMFAResponseJSON.has(FabricConstants.OPSTATUS)
						&& readMFAResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
						&& readMFAResponseJSON.has("mfa")) {
					JSONArray readMFAJSONArray = readMFAResponseJSON.optJSONArray("mfa");
					if (!(readMFAJSONArray == null || readMFAJSONArray.length() < 1)) {
						JSONObject MFAJSONObj = readMFAJSONArray.getJSONObject(0);
						String MFAId = MFAJSONObj.getString("id");
						if (!MFAId.equals(mfaId)) {
							ErrorCodeEnum.ERR_21345.setErrorCode(result);
							alert.prepareError("MFA Scenario already present for AppActionId").log();
							AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
									ActivityStatusEnum.FAILED, eventFailureDescription);
							result.addParam(new Param("status", "Failure", FabricConstants.STRING));
							return result;
						}
					}
				}
				// Editing MFA Scenario
				inputMap.put("id", mfaId);
				inputMap.put("modifiedby", loggedInUserDetails.getUserName());
				inputMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());

				String editMFAResponse = Executor.invokeService(ServiceURLEnum.MFA_UPDATE, inputMap, null,
						requestInstance);

				JSONObject editMFAResponseJSON = CommonUtilities.getStringAsJSONObject(editMFAResponse);
				if ((editMFAResponseJSON != null) && editMFAResponseJSON.has(FabricConstants.OPSTATUS)
						&& editMFAResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
					diagnostic.prepareDebug("MFA Scenario updated successfully.").log();
					String auditSuccessDescription = "MFA Scenario updated successfully. " + "App:" + appName + "/"
							+ "Action type:" + actionType + "/" + "Action:" + actionName;
					if (!mfaScenarioStatusId.equals(oldStatusId))
						auditSuccessDescription += "/" + "Status:" + mfaScenarioStatusName;
					if (!frequencyTypeId.equals(oldFrequencyTypeId))
						auditSuccessDescription += "/" + "Frequency:" + frequencyTypeDescription;
					if ((frequencyTypeId.equals("VALUE_BASED")) && (!frequencyValue.equals(oldFrequencyValue)))
						auditSuccessDescription += "/" + "Value:" + frequencyValue;
					if (!mfaScenarioDescription.equals(oldDescription))
						auditSuccessDescription += "/" + "MFA scenario description:" + mfaScenarioDescription;
					if (!primaryMFATypeId.equals(oldPrimaryMFAType))
						auditSuccessDescription += "/" + "Primary MFA:" + primaryMFATypeName;
					if (!secondaryMFATypeId.equals(oldSecondaryMFAType))
						auditSuccessDescription += "/" + "Secondary MFA:" + secondaryMFATypeName;

					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
							ActivityStatusEnum.SUCCESSFUL, auditSuccessDescription);
					result.addParam(new Param("status", "Success", FabricConstants.STRING));
				} else {
					alert.prepareError("Failed to edit MFA Scenario").log();
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
							ActivityStatusEnum.FAILED, eventFailureDescription);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					ErrorCodeEnum.ERR_21337.setErrorCode(result);
					return result;
				}
			}

			result.addParam(new Param("status", "Success", FabricConstants.STRING));

		} catch (Exception e) {
			alert.prepareError("Unexepected Error in create or edit MFA Scenario flow. Exception: ", e).log();
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
					ActivityStatusEnum.FAILED, eventFailureDescription);
			ErrorCodeEnum.ERR_21338.setErrorCode(result);
			result.addParam(new Param("status", "Failure", FabricConstants.STRING));
		}

		return result;
	}

	public Result deleteMFAScenario(DataControllerRequest requestInstance) {
		Result result = new Result();
		try {

			// Validate actionId
			String appId = requestInstance.getParameter("appId");
			if (StringUtils.isBlank(appId)) {
				ErrorCodeEnum.ERR_20866.setErrorCode(result);
				alert.prepareError("ActionId cannot be empty").log();
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, EventEnum.DELETE,
						ActivityStatusEnum.FAILED, "MFA Scenario delete failed");
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}
			String actionId = requestInstance.getParameter("actionId");
			if (StringUtils.isBlank(actionId)) {
				ErrorCodeEnum.ERR_20866.setErrorCode(result);
				alert.prepareError("ActionId cannot be empty").log();
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, EventEnum.DELETE,
						ActivityStatusEnum.FAILED, "MFA Scenario delete failed");
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}

			// Fetch MFAId from featureaction table
			String mfaId = null;
			String actionName = null;
			String isMFAApplicable = null;
			Map<String, String> inputMap = new HashMap<String, String>();
			inputMap.put(ODataQueryConstants.FILTER, "id eq '" + actionId + "' and App_id eq '" + appId + "'");
			inputMap.put(ODataQueryConstants.SELECT, "MFA_id, name, isMFAApplicable");

			String readActionResponse = Executor.invokeService(ServiceURLEnum.FEATUREACTION_READ, inputMap, null,
					requestInstance);

			JSONObject readActionResponseJSON = CommonUtilities.getStringAsJSONObject(readActionResponse);
			if (readActionResponseJSON != null && readActionResponseJSON.has(FabricConstants.OPSTATUS)
					&& readActionResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readActionResponseJSON.has("featureaction")) {
				JSONArray readActionJSONArray = readActionResponseJSON.optJSONArray("featureaction");
				if (readActionJSONArray == null || readActionJSONArray.length() < 1) {
					alert.prepareError("No record found for Action Id").log();
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, EventEnum.DELETE,
							ActivityStatusEnum.FAILED, "MFA Scenario delete failed");
					ErrorCodeEnum.ERR_21351.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				} else {
					JSONObject currActionRecord = readActionJSONArray.getJSONObject(0);
					mfaId = currActionRecord.optString("MFA_id");
					actionName = currActionRecord.optString("name");
					isMFAApplicable = currActionRecord.optString("isMFAApplicable");

				}
			}
			inputMap.clear();
			inputMap.put(ODataQueryConstants.FILTER, "id eq '" + appId + "'");
			inputMap.put(ODataQueryConstants.SELECT, "Name");
			String readAppResponse = Executor.invokeService(ServiceURLEnum.APP_READ, inputMap, null, requestInstance);
			JSONObject readAppResponseJSON = CommonUtilities.getStringAsJSONObject(readAppResponse);
			if (!(readAppResponseJSON != null && readAppResponseJSON.has(FabricConstants.OPSTATUS)
					&& readAppResponseJSON.getInt(FabricConstants.OPSTATUS) == 0)) {
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				ErrorCodeEnum.ERR_21308.setErrorCode(result);
				alert.prepareError("Failed to read App").log();
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, EventEnum.DELETE,
						ActivityStatusEnum.FAILED, "MFA Scenario delete failed");
				ErrorCodeEnum.ERR_21351.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}
			/*
			 * else { JSONArray readAppResponseJSONArray =
			 * readAppResponseJSON.optJSONArray("app"); if (!(readAppResponseJSONArray ==
			 * null || readAppResponseJSONArray.length() < 1)) { JSONObject currAppRecord =
			 * readAppResponseJSONArray.getJSONObject(0); String appName =
			 * currAppRecord.getString("Name"); } }
			 */

			if (StringUtils.equals(isMFAApplicable, "false")) {
				ErrorCodeEnum.ERR_21349.setErrorCode(result);
				alert.prepareError("MFA is not applicable for this action").log();
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}
			String legalUnit = EnvironmentConfigurationsHandler.getServerAppPropertyValue("BRANCH_ID_REFERENCE", requestInstance);
			// Updating MFAId in featureaction table to null
			inputMap.clear();
			inputMap.put("id", actionId);
			inputMap.put("App_id", appId);
			inputMap.put("MFA_id", "NULL");
			inputMap.put("companyLegalUnit", legalUnit);
			String editActionResponse = Executor.invokeService(ServiceURLEnum.FEATUREACTION_UPDATE, inputMap, null,
					requestInstance);

			JSONObject editActionResponseJSON = CommonUtilities.getStringAsJSONObject(editActionResponse);
			if (!(editActionResponseJSON != null && editActionResponseJSON.has(FabricConstants.OPSTATUS)
					&& editActionResponseJSON.getInt(FabricConstants.OPSTATUS) == 0)) {
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				ErrorCodeEnum.ERR_21339.setErrorCode(result);
				alert.prepareError("Error in deleting mfa scenario.").log();
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, EventEnum.DELETE,
						ActivityStatusEnum.FAILED, "MFA Scenario delete failed");
			}

			// Deleting mfa scenario record
			inputMap.clear();
			inputMap.put("id", mfaId);
			inputMap.put("Action_id", actionId);
			inputMap.put("App_id", appId);

			String deleteMFAResponse = Executor.invokeService(ServiceURLEnum.MFA_DELETE, inputMap, null,
					requestInstance);
diagnostic.prepareDebug("deleteMFAResponse ###"+ deleteMFAResponse).log();

			JSONObject deleteMFAResponseJSON = CommonUtilities.getStringAsJSONObject(deleteMFAResponse);
			if (deleteMFAResponseJSON != null && deleteMFAResponseJSON.has(FabricConstants.OPSTATUS)
					&& deleteMFAResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
				result.addParam(new Param("status", "Success", FabricConstants.STRING));
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, EventEnum.DELETE,
						ActivityStatusEnum.SUCCESSFUL, "MFA Scenario delete successful. Action: " + actionName);
			} else {
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				ErrorCodeEnum.ERR_21339.setErrorCode(result);
				alert.prepareError("Error in deleting mfa scenario.").log();
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, EventEnum.DELETE,
						ActivityStatusEnum.FAILED, "MFA Scenario delete failed");
			}

		} catch (Exception e) {
			ErrorCodeEnum.ERR_21339.setErrorCode(result);
			alert.prepareError("Error in deleting mfa scenario.", e).log();
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, EventEnum.DELETE,
					ActivityStatusEnum.FAILED, "MFA Scenario delete failed");
		}
		return result;
	}

	public Result getMFAScenario(DataControllerRequest requestInstance) {
		Result result = new Result();
		try {

			// Fetch mfa scenario from mfa table
			String serverAppId = requestInstance.getParameter("appId");
			String serverActionId = requestInstance.getParameter("actionId");
			Map<String, String> inputMap = new HashMap<String, String>();

			if (StringUtils.isNotBlank(serverAppId) && StringUtils.isNotBlank(serverActionId)) {
				// Fetch MFAId from featureaction table
				String mfaId = null;
				String isMFAApplicable = null;
				inputMap.put(ODataQueryConstants.FILTER,
						"App_id eq '" + serverAppId + "' and id eq '" + serverActionId + "'");
				inputMap.put(ODataQueryConstants.SELECT, "MFA_id, isMFAApplicable");

				String readActionResponse = Executor.invokeService(ServiceURLEnum.FEATUREACTION_READ, inputMap, null,
						requestInstance);

				JSONObject readActionResponseJSON = CommonUtilities.getStringAsJSONObject(readActionResponse);
				if (readActionResponseJSON != null && readActionResponseJSON.has(FabricConstants.OPSTATUS)
						&& readActionResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
						&& readActionResponseJSON.has("featureaction")) {
					JSONArray readActionJSONArray = readActionResponseJSON.optJSONArray("featureaction");
					if (readActionJSONArray == null || readActionJSONArray.length() < 1) {
						alert.prepareError("No record found for Action Id").log();
						AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, EventEnum.DELETE,
								ActivityStatusEnum.FAILED, "MFA Scenario delete failed");
						ErrorCodeEnum.ERR_21351.setErrorCode(result);
						result.addParam(new Param("status", "Failure", FabricConstants.STRING));
						return result;
					} else {
						JSONObject currActionRecord = readActionJSONArray.getJSONObject(0);
						mfaId = currActionRecord.optString("MFA_id");
						isMFAApplicable = currActionRecord.optString("isMFAApplicable");
						if (StringUtils.equals(isMFAApplicable, "false")) {
							ErrorCodeEnum.ERR_21349.setErrorCode(result);
							alert.prepareError("MFA is not applicable for this action").log();
							result.addParam(new Param("status", "Failure", FabricConstants.STRING));
							return result;
						}
					}
				} else {
					alert.prepareError("No record found for Action Id").log();
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, EventEnum.DELETE,
							ActivityStatusEnum.FAILED, "MFA Scenario delete failed");
					ErrorCodeEnum.ERR_21351.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				}

				inputMap.clear();
				inputMap.put(ODataQueryConstants.FILTER, "id eq '" + mfaId + "' and App_id eq '" + serverAppId
						+ "' and Action_id eq '" + serverActionId + "'");
			}

			inputMap.put(ODataQueryConstants.SELECT,
					"id, App_id,Action_id,FrequencyType_id, FrequencyValue, Status_id, Description, PrimaryMFAType, SecondaryMFAType, SMSText, EmailSubject, EmailBody");

			String readMFAResponse = Executor.invokeService(ServiceURLEnum.MFA_READ, inputMap, null, requestInstance);

			JSONObject readMFAResponseJSON = CommonUtilities.getStringAsJSONObject(readMFAResponse);
			if (readMFAResponseJSON != null && readMFAResponseJSON.has(FabricConstants.OPSTATUS)
					&& readMFAResponseJSON.getInt(FabricConstants.OPSTATUS) == 0 && readMFAResponseJSON.has("mfa")) {
				JSONArray readMFAJSONArray = readMFAResponseJSON.optJSONArray("mfa");
				Dataset mfaDataset = new Dataset();
				mfaDataset.setId("mfaScenarios");
				if ((readMFAJSONArray != null) && (readMFAJSONArray.length() > 0)) {
					for (int indexVar = 0; indexVar < readMFAJSONArray.length(); indexVar++) {
						JSONObject mfaJSONObject = readMFAJSONArray.getJSONObject(indexVar);
						Record currRecord = new Record();

						String mfaId = mfaJSONObject.optString("id");
						String frequencyTypeId = mfaJSONObject.optString("FrequencyType_id");
						String frequencyValue = mfaJSONObject.optString("FrequencyValue");
						String mfaScenarioStatusId = mfaJSONObject.optString("Status_id");
						String mfaScenarioDescription = mfaJSONObject.optString("Description");
						String primaryMFATypeId = mfaJSONObject.optString("PrimaryMFAType");
						String secondaryMFATypeId = mfaJSONObject.optString("SecondaryMFAType");
						String smsText = mfaJSONObject.optString("SMSText");
						String emailSubject = mfaJSONObject.optString("EmailSubject");
						String emailBody = mfaJSONObject.optString("EmailBody");
						// Fetch appid from app table
						inputMap.clear();
						inputMap.put(ODataQueryConstants.FILTER, "App_id eq '" + serverAppId + "'");
						inputMap.put(ODataQueryConstants.FILTER, "id eq '" + serverActionId + "'");

						// Fetching actionId, actionName and actionType from featureaction table
						inputMap.put(ODataQueryConstants.FILTER, "MFA_id eq '" + mfaId + "'");
						inputMap.put(ODataQueryConstants.SELECT,
								"id, Feature_id, Type_id, name, isMFAApplicable,App_id");

						String readActionResponse = Executor.invokeService(ServiceURLEnum.FEATUREACTION_READ, inputMap,
								null, requestInstance);

						String appId = null;
						String actionId = null;
						String featureId = null;
						String actionName = null;
						String actionType = null;
						String featureName = null;
						String featureStatus = null;
						String isMFAApplicable = null;

						JSONObject readActionResponseJSON = CommonUtilities.getStringAsJSONObject(readActionResponse);
						if (readActionResponseJSON != null && readActionResponseJSON.has(FabricConstants.OPSTATUS)
								&& readActionResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
								&& readActionResponseJSON.has("featureaction")) {
							JSONArray readActionJSONArray = readActionResponseJSON.optJSONArray("featureaction");
							if (readActionJSONArray == null || readActionJSONArray.length() < 1) {
								alert.prepareError("Failed to Fetch Action").log();
								ErrorCodeEnum.ERR_21310.setErrorCode(result);
								result.addParam(new Param("status", "Failure", FabricConstants.STRING));
								return result;
							} else {
								JSONObject currActionRecord = readActionJSONArray.getJSONObject(0);
								actionName = currActionRecord.optString("name");
								appId = currActionRecord.getString("App_id");
								actionType = currActionRecord.optString("Type_id");
								actionId = currActionRecord.optString("id");
								featureId = currActionRecord.optString("Feature_id");
								isMFAApplicable = currActionRecord.optString("isMFAApplicable");
								if (StringUtils.equals(isMFAApplicable, "false"))
									continue;
							}
						} else {
							alert.prepareError("Failed to Fetch Action").log();
							ErrorCodeEnum.ERR_21310.setErrorCode(result);
							result.addParam(new Param("status", "Failure", FabricConstants.STRING));
							return result;
						}
						inputMap.clear();
						inputMap.put(ODataQueryConstants.FILTER, "id eq '" + appId + "'");
						inputMap.put(ODataQueryConstants.SELECT, "Name, Status_id");

						// Creating appRecord

						String readAppResponse = Executor.invokeService(ServiceURLEnum.APP_READ, inputMap, null,
								requestInstance);

						String appName = null;
						String appStatusId = null;
						JSONObject readAppResponseJSON = CommonUtilities.getStringAsJSONObject(readAppResponse);
						if (readAppResponseJSON != null && readAppResponseJSON.has(FabricConstants.OPSTATUS)
								&& readAppResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
								&& readAppResponseJSON.has("app")) {
							JSONArray readAppJSONArray = readAppResponseJSON.optJSONArray("app");
							if (readAppJSONArray == null || readAppJSONArray.length() < 1) {
								ErrorCodeEnum.ERR_21319.setErrorCode(result);
								alert.prepareError("Invalid App Id").log();
								result.addParam(new Param("status", "Failure", FabricConstants.STRING));
								return result;
							} else {
								JSONObject currAppRecord = readAppJSONArray.getJSONObject(0);
								appName = currAppRecord.optString("Name");
								appStatusId = currAppRecord.optString("Status_id");
							}
						} else {
							ErrorCodeEnum.ERR_21319.setErrorCode(result);
							alert.prepareError("Invalid App Id").log();
							result.addParam(new Param("status", "Failure", FabricConstants.STRING));
							return result;
						}
						Record appRecord = new Record();
						appRecord.setId("app");
						Param appId_Param = new Param("appId", appId, FabricConstants.STRING);
						appRecord.addParam(appId_Param);
						Param appName_Param = new Param("appName", appName, FabricConstants.STRING);
						appRecord.addParam(appName_Param);
						Param appStatusId_Param = new Param("appStatusId", appStatusId, FabricConstants.STRING);
						appRecord.addParam(appStatusId_Param);

						currRecord.addRecord(appRecord);

						// Fetching featureName from feature table
						inputMap.clear();
						inputMap.put(ODataQueryConstants.FILTER, "id eq '" + featureId + "'");
						inputMap.put(ODataQueryConstants.SELECT, "name, Status_id");

						String readFeatureResponse = Executor.invokeService(ServiceURLEnum.FEATURE_READ, inputMap, null,
								requestInstance);

						JSONObject readFeatureResponseJSON = CommonUtilities.getStringAsJSONObject(readFeatureResponse);
						if (readFeatureResponseJSON != null && readFeatureResponseJSON.has(FabricConstants.OPSTATUS)
								&& readFeatureResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
								&& readFeatureResponseJSON.has("feature")) {
							JSONArray readFeatureJSONArray = readFeatureResponseJSON.optJSONArray("feature");
							if (readFeatureJSONArray == null || readFeatureJSONArray.length() < 1) {
								alert.prepareError("Failed to Fetch Feature").log();
								ErrorCodeEnum.ERR_21352.setErrorCode(result);
								result.addParam(new Param("status", "Failure", FabricConstants.STRING));
								return result;
							} else {
								JSONObject currFeatureRecord = readFeatureJSONArray.getJSONObject(0);
								featureName = currFeatureRecord.optString("name");
								featureStatus = currFeatureRecord.optString("Status_id");
								if (!StringUtils.equals(featureStatus, "SID_FEATURE_ACTIVE"))
									continue;
							}
						} else {
							alert.prepareError("Failed to Fetch Action").log();
							ErrorCodeEnum.ERR_21310.setErrorCode(result);
							result.addParam(new Param("status", "Failure", FabricConstants.STRING));
							return result;
						}

						// Creating actionRecord
						Record actionRecord = new Record();
						actionRecord.setId("action");

						Param actionId_Param = new Param("actionId", actionId, FabricConstants.STRING);
						actionRecord.addParam(actionId_Param);
						Param featureId_Param = new Param("featureId", featureId, FabricConstants.STRING);
						actionRecord.addParam(featureId_Param);
						Param featureName_Param = new Param("featureName", featureName, FabricConstants.STRING);
						actionRecord.addParam(featureName_Param);
						Param actionName_Param = new Param("actionName", actionName, FabricConstants.STRING);
						actionRecord.addParam(actionName_Param);
						Param actionType_Param = new Param("actionType", actionType, FabricConstants.STRING);
						actionRecord.addParam(actionType_Param);

						currRecord.addRecord(actionRecord);

						// Fetching frequencyTypeName from frequencytype table
						inputMap.clear();
						inputMap.put(ODataQueryConstants.FILTER, "id eq '" + frequencyTypeId + "'");
						inputMap.put(ODataQueryConstants.SELECT, "Description");

						String readFrequencyTypeResponse = Executor.invokeService(ServiceURLEnum.FREQUENCYTYPE_READ,
								inputMap, null, requestInstance);

						String frequencyTypeName = null;

						JSONObject readFrequencyTypeResponseJSON = CommonUtilities
								.getStringAsJSONObject(readFrequencyTypeResponse);
						if (readFrequencyTypeResponseJSON != null
								&& readFrequencyTypeResponseJSON.has(FabricConstants.OPSTATUS)
								&& readFrequencyTypeResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
								&& readFrequencyTypeResponseJSON.has("frequencytype")) {
							JSONArray readFrequencyTypeJSONArray = readFrequencyTypeResponseJSON
									.optJSONArray("frequencytype");
							if (readFrequencyTypeJSONArray == null || readFrequencyTypeJSONArray.length() < 1) {
								ErrorCodeEnum.ERR_21309.setErrorCode(result);
								result.addParam(new Param("status", "Failure", FabricConstants.STRING));
								alert.prepareError("Failed to Fetch Frequency Type").log();
								return result;
							} else {
								JSONObject currFrequencyTypeRecord = readFrequencyTypeJSONArray.getJSONObject(0);
								frequencyTypeName = currFrequencyTypeRecord.optString("Description");
							}
						} else {
							ErrorCodeEnum.ERR_21309.setErrorCode(result);
							result.addParam(new Param("status", "Failure", FabricConstants.STRING));
							alert.prepareError("Failed to Fetch Frequency Type").log();
							return result;
						}

						// Creating frequencyTypeRecord
						Record frequencyTypeRecord = new Record();
						frequencyTypeRecord.setId("frequencyType");

						Param frequencyTypeId_Param = new Param("frequencyTypeId", frequencyTypeId,
								FabricConstants.STRING);
						frequencyTypeRecord.addParam(frequencyTypeId_Param);
						Param frequencyTypeName_Param = new Param("frequencyTypeName", frequencyTypeName,
								FabricConstants.STRING);
						frequencyTypeRecord.addParam(frequencyTypeName_Param);
						Param frequencyValue_Param = new Param("frequencyValue", frequencyValue,
								FabricConstants.STRING);
						frequencyTypeRecord.addParam(frequencyValue_Param);

						currRecord.addRecord(frequencyTypeRecord);

						Param appActionId_Param = new Param("actionId", actionId, FabricConstants.STRING);
						Param appsId_Param = new Param("appId", serverAppId, FabricConstants.STRING);

						currRecord.addParam(appActionId_Param);
						currRecord.addParam(appsId_Param);
						Param mfaScenarioDescription_Param = new Param("mfaScenarioDescription", mfaScenarioDescription,
								FabricConstants.STRING);
						currRecord.addParam(mfaScenarioDescription_Param);
						Param primaryMFATypeId_Param = new Param("primaryMFATypeId", primaryMFATypeId,
								FabricConstants.STRING);
						currRecord.addParam(primaryMFATypeId_Param);
						Param secondaryMFATypeId_Param = new Param("secondaryMFATypeId", secondaryMFATypeId,
								FabricConstants.STRING);
						currRecord.addParam(secondaryMFATypeId_Param);
						Param smsText_Param = new Param("smsText", smsText, FabricConstants.STRING);
						currRecord.addParam(smsText_Param);
						Param emailSubject_Param = new Param("emailSubject", emailSubject, FabricConstants.STRING);
						currRecord.addParam(emailSubject_Param);
						Param emailBody_Param = new Param("emailBody", emailBody, FabricConstants.STRING);
						currRecord.addParam(emailBody_Param);
						Param mfaScenarioStatusId_Param = new Param("mfaScenarioStatusId", mfaScenarioStatusId,
								FabricConstants.STRING);
						currRecord.addParam(mfaScenarioStatusId_Param);

						// Fetching primaryMFAType and secondaryMFAType from mfatype table
						inputMap.clear();
						inputMap.put(ODataQueryConstants.FILTER,
								"id eq '" + primaryMFATypeId + "' or id eq '" + secondaryMFATypeId + "'");
						inputMap.put(ODataQueryConstants.SELECT, "id, Name");

						String readMFATypeResponse = Executor.invokeService(ServiceURLEnum.MFATYPE_READ, inputMap, null,
								requestInstance);

						JSONObject readMFATypeResponseJSON = CommonUtilities.getStringAsJSONObject(readMFATypeResponse);
						if (readMFATypeResponseJSON != null && readMFATypeResponseJSON.has(FabricConstants.OPSTATUS)
								&& readMFATypeResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
								&& readMFATypeResponseJSON.has("mfatype")) {
							JSONArray readMFATypeJSONArray = readMFATypeResponseJSON.optJSONArray("mfatype");
							Record mfaTypeRecord = new Record();
							mfaTypeRecord.setId("mfaTypes");
							if ((readMFATypeJSONArray == null) || (readMFATypeJSONArray.length() < 2)) {
								ErrorCodeEnum.ERR_21340.setErrorCode(result);
								alert.prepareError("Primary mfa type and secondary mfa type is invalid").log();
								result.addParam(new Param("status", "Failure", FabricConstants.STRING));
								return result;
							} else {
								for (int index = 0; index < readMFATypeJSONArray.length(); index++) {
									JSONObject mfaTypeJSONObject = readMFATypeJSONArray.getJSONObject(index);
									Record currMFATypeRecord = new Record();
									currMFATypeRecord.setId(mfaTypeJSONObject.getString("id"));
									Param mfaTypeId_Param = new Param("mfaTypeId", mfaTypeJSONObject.getString("id"),
											FabricConstants.STRING);
									currMFATypeRecord.addParam(mfaTypeId_Param);
									String mfaTypeName = mfaTypeJSONObject.getString("Name");
									Param mfaTypeName_Param = new Param("mfaTypeName", mfaTypeName,
											FabricConstants.STRING);
									currMFATypeRecord.addParam(mfaTypeName_Param);

									mfaTypeRecord.addRecord(currMFATypeRecord);
								}
								currRecord.addRecord(mfaTypeRecord);
							}
						} else {
							ErrorCodeEnum.ERR_21340.setErrorCode(result);
							alert.prepareError("Primary mfa type and secondary mfa type is invalid").log();
							result.addParam(new Param("status", "Failure", FabricConstants.STRING));
							return result;
						}
						
						if(actionId.equalsIgnoreCase("LOGIN") || actionId.equalsIgnoreCase("PASSWORD_UPDATE") || actionId.equalsIgnoreCase("OPEN_FIXED_DEPOSIT_ACTIVATE") || 
								actionId.equalsIgnoreCase("CROSS_BORDER_CONSENT") || actionId.equalsIgnoreCase("CARD_MANAGEMENT_ACTIVATE_CARD") || actionId.equalsIgnoreCase("CARD_MANAGEMENT_UNLOCK_CARD") || actionId.equalsIgnoreCase("CARD_MANAGEMENT_LOCK_CARD") || actionId.equalsIgnoreCase("CARD_MANAGEMENT_CHANGE_PIN") ||
								actionId.equalsIgnoreCase("CARD_MANAGEMENT_REPORT_CARD_STOLEN") || actionId.equalsIgnoreCase("CARD_MANAGEMENT_EMI_REQUEST") || actionId.equalsIgnoreCase("CARD_MANAGEMENT_APPLY_FOR_VIRTUAL_DOLLAR_CARD") || actionId.equalsIgnoreCase("CARD_MANAGEMENT_APPLY_FOR_DEBIT_CARD") || actionId.equalsIgnoreCase("CARD_MANAGEMENT_APPLY_FOR_PREPAID_CARD") ||
								 actionId.equalsIgnoreCase("CARD_MANAGEMENT_PREPAID_DOMESTIC_TOPUP") || actionId.equalsIgnoreCase("CARD_MANAGEMENT_VIRTUAL_DOLLAR_CARD_TOPUP") || actionId.equalsIgnoreCase("CARD_MANAGEMENT_CARD_PAYMENT") || actionId.equalsIgnoreCase("CARD_MANAGEMENT_REPLACE_CARD") || actionId.equalsIgnoreCase("CARD_MANAGEMENT_CANCEL_CARD") || 
								actionId.equalsIgnoreCase("BILL_PAY_CREATE") || actionId.equalsIgnoreCase("TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE") || actionId.equalsIgnoreCase("INTRA_BANK_FUND_TRANSFER_CREATE") || actionId.equalsIgnoreCase("INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE") || actionId.equalsIgnoreCase("INTERNATIONAL_VPA_TRANSFER") || actionId.equalsIgnoreCase("QR_PAYMENTS_CREATE") || 
								actionId.equalsIgnoreCase("WITHDRAW_CASH_CARDLESS_CASH") || actionId.equalsIgnoreCase("ONLINE_BANKING_ACCESS_DISABLE") || actionId.equalsIgnoreCase("ESEWA_TOPUP_ACTIVATE") || actionId.equalsIgnoreCase("CANT_SIGN_IN_ACTIVATE"))
						mfaDataset.addRecord(currRecord);
					}
					result.addDataset(mfaDataset);
				}
			} else {
				result.addParam(new Param("message", readMFAResponse, FabricConstants.STRING));
				alert.prepareError("Failed to Fetch MFA Scenario Response: " + readMFAResponse).log();
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				ErrorCodeEnum.ERR_21341.setErrorCode(result);
				return result;
			}

			result.addParam(new Param("status", "Success", FabricConstants.STRING));
			return result;

		} catch (Exception e) {
			alert.prepareError("Unexepected Error in Fetching MFA Scenario. Exception: ", e).log();
			result.addParam(new Param("status", "Failure", FabricConstants.STRING));
			ErrorCodeEnum.ERR_21341.setErrorCode(result);
		}
		return result;
	}

	public Result getMFAVariableReference(DataControllerRequest requestInstance) {
		Result result = new Result();
		try {

			// Fetch mfavariablereference data from mfavariablereference table
			Map<String, String> inputMap = new HashMap<String, String>();
			inputMap.put(ODataQueryConstants.SELECT, "Code, Name");

			String readMFAVariableReferenceResponse = Executor.invokeService(ServiceURLEnum.MFAVARIABLEREFERENCE_READ,
					inputMap, null, requestInstance);

			JSONObject readMFAVariableReferenceResponseJSON = CommonUtilities
					.getStringAsJSONObject(readMFAVariableReferenceResponse);
			if (readMFAVariableReferenceResponseJSON != null
					&& readMFAVariableReferenceResponseJSON.has(FabricConstants.OPSTATUS)
					&& readMFAVariableReferenceResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readMFAVariableReferenceResponseJSON.has("mfavariablereference")) {
				JSONArray readMFAVariableReferenceJSONArray = readMFAVariableReferenceResponseJSON
						.optJSONArray("mfavariablereference");
				Dataset mfavariableReferenceDataset = new Dataset();
				mfavariableReferenceDataset.setId("mfavariablereference");
				if ((readMFAVariableReferenceJSONArray != null) && (readMFAVariableReferenceJSONArray.length() > 0)) {
					for (int indexVar = 0; indexVar < readMFAVariableReferenceJSONArray.length(); indexVar++) {
						JSONObject mfaVariableReferenceJSONObject = readMFAVariableReferenceJSONArray
								.getJSONObject(indexVar);
						Record currRecord = new Record();
						String code = mfaVariableReferenceJSONObject.getString("Code");
						Param code_Param = new Param("Code", code, FabricConstants.STRING);
						currRecord.addParam(code_Param);
						String name = mfaVariableReferenceJSONObject.getString("Name");
						Param name_Param = new Param("Name", name, FabricConstants.STRING);
						currRecord.addParam(name_Param);
						mfavariableReferenceDataset.addRecord(currRecord);
					}
					result.addDataset(mfavariableReferenceDataset);
					return result;
				}
			} else {
				result.addParam(new Param("message", readMFAVariableReferenceResponse, FabricConstants.STRING));
				alert.prepareError("Failed to Fetch MFA Variable Reference Response: " + readMFAVariableReferenceResponse).log();
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				ErrorCodeEnum.ERR_21346.setErrorCode(result);
				return result;
			}
		} catch (Exception e) {
			alert.prepareError("Unexepected Error in Fetching MFA Variable Reference data. Exception: ", e).log();
			result.addParam(new Param("status", "Failure", FabricConstants.STRING));
			ErrorCodeEnum.ERR_21346.setErrorCode(result);
		}
		return result;
	}

	// Adding the below methods for SCA Implentation

	public Result getSCAMode(DataControllerRequest requestInstance) {
		Result result = new Result();
		try {
			// Validate AppId
			String clientAppId = requestInstance.getParameter("appId");
			if (StringUtils.isBlank(clientAppId)) {
				ErrorCodeEnum.ERR_21318.setErrorCode(result);
				alert.prepareError("App Id cannot be empty").log();
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}
			// Validate ActionId
			String actionId = requestInstance.getParameter("actionId");
			if (StringUtils.isBlank(actionId)) {
				ErrorCodeEnum.ERR_20866.setErrorCode(result);
				alert.prepareError("Action Id is a mandatory input").log();
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}

			// Fetch MFAId from featureaction table
			Map<String, String> inputMap = new HashMap<String, String>();
			inputMap.put(ODataQueryConstants.FILTER, "id eq '" + actionId + "' and App_id eq '" + clientAppId + "'");
			inputMap.put(ODataQueryConstants.SELECT, "MFA_id, Feature_id");

			String readFeatureActionResponse = Executor.invokeService(ServiceURLEnum.EXTERNAL_FEATUREACTION_READ,
					inputMap, null, requestInstance);

			String scaId = null;
			String featureId = null;

			JSONObject readFeatureActionResponseJSON = CommonUtilities.getStringAsJSONObject(readFeatureActionResponse);
			if (readFeatureActionResponseJSON != null && readFeatureActionResponseJSON.has(FabricConstants.OPSTATUS)
					&& readFeatureActionResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readFeatureActionResponseJSON.has("external_feature_actions")) {
				JSONArray readFeatureActionJSONArray = readFeatureActionResponseJSON
						.optJSONArray("external_feature_actions");
				if (readFeatureActionJSONArray == null || readFeatureActionJSONArray.length() < 1) {
					ErrorCodeEnum.ERR_21334.setErrorCode(result);
					alert.prepareError("Invalid FeatureActionId").log();
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				} else {
					JSONObject currFeatureActionRecord = readFeatureActionJSONArray.getJSONObject(0);
					scaId = currFeatureActionRecord.optString("MFA_id");
					featureId = currFeatureActionRecord.getString("Feature_id");
				}
			} else {
				ErrorCodeEnum.ERR_21334.setErrorCode(result);
				alert.prepareError("Invalid FeatureActionId").log();
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}

			// Fetch FeatureStatus from feature table
			inputMap.clear();
			inputMap.put(ODataQueryConstants.FILTER, "id eq '" + featureId + "'");
			inputMap.put(ODataQueryConstants.SELECT, "Status_id");

			String readFeatureResponse = Executor.invokeService(ServiceURLEnum.FEATURE_READ, inputMap, null,
					requestInstance);

			String featureStatus = null;

			JSONObject readFeatureResponseJSON = CommonUtilities.getStringAsJSONObject(readFeatureResponse);
			if (readFeatureResponseJSON != null && readFeatureResponseJSON.has(FabricConstants.OPSTATUS)
					&& readFeatureResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readFeatureResponseJSON.has("feature")) {
				JSONArray readFeatureJSONArray = readFeatureResponseJSON.optJSONArray("feature");
				if (readFeatureJSONArray == null || readFeatureJSONArray.length() < 1) {
					ErrorCodeEnum.ERR_21352.setErrorCode(result);
					alert.prepareError("Failed to fetch feature").log();
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				} else {
					JSONObject currFeatureRecord = readFeatureJSONArray.getJSONObject(0);
					featureStatus = currFeatureRecord.optString("Status_id");
					if (!StringUtils.equals(featureStatus, "SID_FEATURE_ACTIVE")) {
						ErrorCodeEnum.ERR_21353.setErrorCode(result);
						alert.prepareError("Feature is not active").log();
						result.addParam(new Param("status", "Feature is not active", FabricConstants.STRING));
						return result;
					}
				}
			} else {
				ErrorCodeEnum.ERR_21352.setErrorCode(result);
				alert.prepareError("Failed to fetch feature").log();
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}

			inputMap.clear();
			inputMap.put(ODataQueryConstants.FILTER,
					"id eq '" + scaId + "' and App_id eq '" + clientAppId + "' and Action_id eq '" + actionId + "'");
			inputMap.put(ODataQueryConstants.SELECT, "Status_id, risk_score");

			String readMFAResponse = Executor.invokeService(ServiceURLEnum.SCA_READ, inputMap, null, requestInstance);

			String isSCARequired = null;
			String riskScore = null;
			JSONObject readMFAResponseJSON = CommonUtilities.getStringAsJSONObject(readMFAResponse);
			if (readMFAResponseJSON != null && readMFAResponseJSON.has(FabricConstants.OPSTATUS)
					&& readMFAResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readMFAResponseJSON.has("sca_actions")) {
				JSONArray readMFAJSONArray = readMFAResponseJSON.optJSONArray("sca_actions");
				if (readMFAJSONArray == null || readMFAJSONArray.length() < 1) {
					ErrorCodeEnum.ERR_21341.setErrorCode(result);
					alert.prepareError("Failed to fetch SCA Scenario").log();
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				} else {
					JSONObject currAppActionRecord = readMFAJSONArray.getJSONObject(0);
					String statusId = currAppActionRecord.optString("Status_id");
					riskScore = currAppActionRecord.optString("risk_score");
					if (statusId.equals("SID_ACTIVE"))
						isSCARequired = "true";
					else
						isSCARequired = "false";
				}
			} else {
				alert.prepareError("SCA Scenario is empty").log();
				result.addParam(new Param("status", "Success", FabricConstants.STRING));
				return result;
			}

			Param isSCARequired_Param = new Param("isSCARequired", isSCARequired, FabricConstants.STRING);
			result.addParam(isSCARequired_Param);
			Param riskScore_Param = new Param("risk_score", riskScore, "String");
			result.addParam(riskScore_Param);
		} catch (Exception e) {
			alert.prepareError("Unexepected Error in Fetching getSCAMode.Exception is: ", e).log();
			result.addParam(new Param("status", "Failure", FabricConstants.STRING));
			ErrorCodeEnum.ERR_21342.setErrorCode(result);
		}
		return result;
	}

	public Result getSCAAction(DataControllerRequest requestInstance) {
		Result result = new Result();
		try {

			String actionType = requestInstance.getParameter("actionType");
			String featureId = requestInstance.getParameter("featureId");
			if (StringUtils.isBlank(featureId)) {
				ErrorCodeEnum.ERR_21350.setErrorCode(result);
				alert.prepareError("Feature Id cannot be empty").log();
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}

			// Fetch action list from featureaction table
			Map<String, String> inputMap = new HashMap<String, String>();
			if (!StringUtils.isBlank(actionType))
				inputMap.put(ODataQueryConstants.FILTER,
						"Type_id eq '" + actionType + "' and Feature_id eq '" + featureId + "'");
			else
				inputMap.put(ODataQueryConstants.FILTER, "Feature_id eq '" + featureId + "'");
			inputMap.put(ODataQueryConstants.SELECT, "id, name, Type_id, MFA_id");

			String readActionResponse = Executor.invokeService(ServiceURLEnum.EXTERNAL_FEATUREACTION_READ, inputMap,
					null, requestInstance);

			JSONObject readActionResponseJSON = CommonUtilities.getStringAsJSONObject(readActionResponse);
			if (readActionResponseJSON != null && readActionResponseJSON.has(FabricConstants.OPSTATUS)
					&& readActionResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readActionResponseJSON.has("external_feature_actions")) {
				JSONArray readActionJSONArray = readActionResponseJSON.optJSONArray("external_feature_actions");

				Dataset actionDataset = new Dataset();
				actionDataset.setId("actions");
				if ((readActionJSONArray != null) && (readActionJSONArray.length() > 0)) {
					for (int indexVar = 0; indexVar < readActionJSONArray.length(); indexVar++) {
						JSONObject actionJSONObject = readActionJSONArray.getJSONObject(indexVar);
						String scaId = null;
						scaId = actionJSONObject.getString("MFA_id");
						if (StringUtils.isNotBlank(scaId))
							continue;

						Record currRecord = new Record();
						String actionId = actionJSONObject.getString("id");
						Param actionId_Param = new Param("actionId", actionId, FabricConstants.STRING);
						currRecord.addParam(actionId_Param);
						String actionName = actionJSONObject.getString("name");
						Param actionName_Param = new Param("actionName", actionName, FabricConstants.STRING);
						currRecord.addParam(actionName_Param);
						String type = actionJSONObject.getString("Type_id");
						Param actionType_Param = new Param("actionType", type, FabricConstants.STRING);
						currRecord.addParam(actionType_Param);
						actionDataset.addRecord(currRecord);
					}
					result.addDataset(actionDataset);
					return result;
				}
			} else {
				result.addParam(new Param("message", readActionResponse, FabricConstants.STRING));
				alert.prepareError("Failed to Fetch Action Response: " + readActionResponse).log();
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				ErrorCodeEnum.ERR_21310.setErrorCode(result);
				return result;
			}
		} catch (Exception e) {
			alert.prepareError("Unexepected Error in Fetching Action. Exception: ", e).log();
			result.addParam(new Param("status", "Failure", FabricConstants.STRING));
			ErrorCodeEnum.ERR_21310.setErrorCode(result);
		}
		return result;
	}

	public Result getSCAFeature(DataControllerRequest requestInstance) {
		Result result = new Result();
		try {

			// Fetch feature list from feature and featureaction table
			Map<String, String> inputMap = new HashMap<String, String>();
			inputMap.put(ODataQueryConstants.SELECT, "feature_id, feature_name,App_id");

			String readFeatureResponse = Executor.invokeService(ServiceURLEnum.MFA_C360_FEATURE_GET_PROC, inputMap,
					null, requestInstance);

			JSONObject readFeatureResponseJSON = CommonUtilities.getStringAsJSONObject(readFeatureResponse);
			if (readFeatureResponseJSON != null && readFeatureResponseJSON.has(FabricConstants.OPSTATUS)
					&& readFeatureResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readFeatureResponseJSON.has("records")) {
				JSONArray readFeatureJSONArray = readFeatureResponseJSON.optJSONArray("records");
				Dataset featureDataset = new Dataset();
				featureDataset.setId("features");
				if ((readFeatureJSONArray != null) && (readFeatureJSONArray.length() > 0)) {
					for (int indexVar = 0; indexVar < readFeatureJSONArray.length(); indexVar++) {
						JSONObject featureJSONObject = readFeatureJSONArray.getJSONObject(indexVar);
						Record currRecord = new Record();
						String featureId = featureJSONObject.getString("feature_id");
						String appId = featureJSONObject.getString("App_id");
						// Checking for already existing mfa records
						inputMap.clear();
						inputMap.put(ODataQueryConstants.SELECT, "id");
						inputMap.put(ODataQueryConstants.FILTER,
								"Feature_id eq'" + featureId + "'and App_id eq'" + appId + "'");
						String readActionResponse = Executor.invokeService(ServiceURLEnum.EXTERNAL_FEATUREACTION_READ,
								inputMap, null, requestInstance);
						JSONObject readActionResponseJSON = CommonUtilities.getStringAsJSONObject(readActionResponse);
						if (readActionResponseJSON != null && readActionResponseJSON.has(FabricConstants.OPSTATUS)
								&& readActionResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
								&& readActionResponseJSON.has("featureaction")) {
							JSONArray readActionJSONArray = readActionResponseJSON.optJSONArray("featureaction");
							if (!((readActionJSONArray != null) && (readActionJSONArray.length() > 0)))
								continue;
						}
						Param featureId_Param = new Param("featureId", featureId, FabricConstants.STRING);
						currRecord.addParam(featureId_Param);
						String featureName = featureJSONObject.getString("feature_name");
						Param featureName_Param = new Param("featureName", featureName, FabricConstants.STRING);
						currRecord.addParam(featureName_Param);
						Param appId_Param = new Param("appId", appId, FabricConstants.STRING);
						currRecord.addParam(appId_Param);

						featureDataset.addRecord(currRecord);
					}
					result.addDataset(featureDataset);
					return result;
				}
			} else {
				result.addParam(new Param("message", readFeatureResponse, FabricConstants.STRING));
				alert.prepareError("Failed to Fetch Feature Response: " + readFeatureResponse).log();
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				ErrorCodeEnum.ERR_21352.setErrorCode(result);
				return result;
			}
		} catch (Exception e) {
			alert.prepareError("Unexepected Error in Fetching Feature. Exception: ", e).log();
			result.addParam(new Param("status", "Failure", FabricConstants.STRING));
			ErrorCodeEnum.ERR_21352.setErrorCode(result);
		}
		return result;
	}

	public Result createOrEditSCAScenario(DataControllerRequest requestInstance, String methodName) {

		Result result = new Result();
		EventEnum event = EventEnum.UPDATE;
		String eventFailureDescription = "SCA Scenario update failed";

		try {
			String loggedInUserId = null;
			UserDetailsBean loggedInUserDetails = LoggedInUserHandler.getUserDetails(requestInstance);
			if (loggedInUserDetails != null) {
				loggedInUserId = loggedInUserDetails.getId();
			}

			if (methodName.equals(CREATE_SCA_SCENARIO_METHOD_NAME)) {
				event = EventEnum.CREATE;
				eventFailureDescription = "SCA Scenario create failed";
			}

			// Validate AppId
			String appId = requestInstance.getParameter("appId");
			if (StringUtils.isBlank(appId)) {
				alert.prepareError("App Id cannot be empty").log();
				ErrorCodeEnum.ERR_21318.setErrorCode(result);
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
						ActivityStatusEnum.FAILED, eventFailureDescription);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}
			String featureId = requestInstance.getParameter("featureId");
			if (StringUtils.isBlank(featureId)) {
				alert.prepareError("Feature Id cannot be empty").log();
				ErrorCodeEnum.ERR_21318.setErrorCode(result);
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
						ActivityStatusEnum.FAILED, eventFailureDescription);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}
			String riskScore = requestInstance.getParameter("riskScore");
			int riskScoreValue = Integer.parseInt(riskScore);
			if (riskScoreValue < 0 || riskScoreValue > 10) {
				alert.prepareError("riskScore should be between 0 and 10").log();
				ErrorCodeEnum.ERR_21318.setErrorCode(result);
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
						ActivityStatusEnum.FAILED, eventFailureDescription);
				result.addParam(new Param("status", "Risk Score should be between 0 and 10", FabricConstants.STRING));
				return result;
			}

			String mfaScenarioDescription = requestInstance.getParameter("mfaScenarioDescription");
			String regEx= "[^a-z0-9 ]";
			Pattern pattern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(mfaScenarioDescription);
			boolean res = matcher.find();

			if (res) {
				alert.prepareError("The description contains Special Characters").log();
				ErrorCodeEnum.ERR_21318.setErrorCode(result);
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
						ActivityStatusEnum.FAILED, eventFailureDescription);
				result.addParam(
						new Param("status", "Discription contains special charectors.", FabricConstants.STRING));
				return result;
			} else
				alert.prepareError("The Description does not contain special charectors.").log();

			String appName = null;
			Map<String, String> inputMap = new HashMap<String, String>();
			inputMap.put(ODataQueryConstants.FILTER, "id eq '" + appId + "'");
			inputMap.put(ODataQueryConstants.SELECT, "id, Name");

			String readAppResponse = Executor.invokeService(ServiceURLEnum.APP_READ, inputMap, null, requestInstance);

			JSONObject readAppResponseJSON = CommonUtilities.getStringAsJSONObject(readAppResponse);
			if (readAppResponseJSON != null && readAppResponseJSON.has(FabricConstants.OPSTATUS)
					&& readAppResponseJSON.getInt(FabricConstants.OPSTATUS) == 0 && readAppResponseJSON.has("app")) {
				JSONArray readAppJSONArray = readAppResponseJSON.optJSONArray("app");
				if (readAppJSONArray == null || readAppJSONArray.length() < 1) {
					alert.prepareError("Invalid App Id").log();
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
							ActivityStatusEnum.FAILED, eventFailureDescription);
					ErrorCodeEnum.ERR_21319.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				} else {
					JSONObject currAppRecord = readAppJSONArray.getJSONObject(0);
					appName = currAppRecord.getString("Name");
				}
			} else {
				alert.prepareError("Invalid App Id").log();
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
						ActivityStatusEnum.FAILED, eventFailureDescription);
				ErrorCodeEnum.ERR_21319.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}
			String actionId = requestInstance.getParameter("actionId");
			if (StringUtils.isBlank(actionId)) {
				alert.prepareError("Action Id is a mandatory input").log();
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
						ActivityStatusEnum.FAILED, eventFailureDescription);
				ErrorCodeEnum.ERR_20866.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}

			// Validate ActionType
			String actionType = requestInstance.getParameter("actionType");
			if (StringUtils.isBlank(actionType)) {
				alert.prepareError("Action type cannot be empty").log();
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
						ActivityStatusEnum.FAILED, eventFailureDescription);
				ErrorCodeEnum.ERR_20783.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}
			String mfaScenarioStatusId = requestInstance.getParameter("mfaScenarioStatusId");

			if (methodName.equals(CREATE_SCA_SCENARIO_METHOD_NAME)) {
				// Updating MFAId in featureaction table

				inputMap.clear();
				inputMap.put("App_id", appId);
				inputMap.put("Action_id", actionId);
				inputMap.put("Status_id", mfaScenarioStatusId);
				inputMap.put("Description", mfaScenarioDescription);
				inputMap.put("risk_score", riskScore);

				String createSCAResponse = Executor.invokeService(ServiceURLEnum.SCA_CREATE, inputMap, null,
						requestInstance);

				JSONObject createSCAResponseJSON = CommonUtilities.getStringAsJSONObject(createSCAResponse);
				if ((createSCAResponseJSON != null) && createSCAResponseJSON.has(FabricConstants.OPSTATUS)
						&& createSCAResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {

					inputMap.clear();
					inputMap.put(ODataQueryConstants.SELECT, "id, App_id,Action_id, Status_id, Description,risk_score");
					String mfaId = null;
					String readSCAResponse = Executor.invokeService(ServiceURLEnum.SCA_READ, inputMap, null,
							requestInstance);

					JSONObject readSCAResponseJSON = CommonUtilities.getStringAsJSONObject(readSCAResponse);
					if (readSCAResponseJSON != null && readSCAResponseJSON.has(FabricConstants.OPSTATUS)
							&& readSCAResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
							&& readSCAResponseJSON.has("sca_actions")) {
						JSONArray readMFAJSONArray = readSCAResponseJSON.optJSONArray("sca_actions");
						if ((readMFAJSONArray != null) && (readMFAJSONArray.length() > 0)) {
							for (int indexVar = 0; indexVar < readMFAJSONArray.length(); indexVar++) {
								JSONObject mfaJSONObject = readMFAJSONArray.getJSONObject(indexVar);
								mfaId = mfaJSONObject.optString("id");
							}
						}

							result.addParam(
									new Param("status", "SCA Scenario created successfully", FabricConstants.STRING));
							// Updating MFAId in SCA featureaction table
							inputMap.clear();
							inputMap.put("id", actionId);
							inputMap.put("MFA_id", mfaId);
							
							String editMFAResponse = Executor.invokeService(ServiceURLEnum.EXTERNAL_FEATUREACTION_UPDATE, inputMap, null,
									requestInstance);

							JSONObject editMFAResponseJSON = CommonUtilities.getStringAsJSONObject(editMFAResponse);
							if ((editMFAResponseJSON != null) && editMFAResponseJSON.has(FabricConstants.OPSTATUS)
									&& editMFAResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
								result.addParam(new Param("status", "Success", FabricConstants.STRING));
								diagnostic.prepareDebug("mfaid is updated successfully.").log();
							}
								else {
									result.addParam(new Param("status", "Failure", FabricConstants.STRING));
									diagnostic.prepareDebug("mfaid updation failed.").log();
								}
						
							diagnostic.prepareDebug("SCA Scenario created successfully.").log();
						
					} else {
						alert.prepareError("Failed to create SCA Scenario").log();
						AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
								ActivityStatusEnum.FAILED, eventFailureDescription);
						result.addParam(new Param("status", "Failure", FabricConstants.STRING));
						ErrorCodeEnum.ERR_21335.setErrorCode(result);
						return result;
					}

				}
			} else if (methodName.equals(EDIT_SCA_SCENARIO_METHOD_NAME)) {

				String currentAppId = requestInstance.getParameter("appId");
				String riskScoreEdit = requestInstance.getParameter("riskScore");
				if (StringUtils.isBlank(currentAppId)) {
					ErrorCodeEnum.ERR_21343.setErrorCode(result);
					alert.prepareError("App Id cannot be empty").log();
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
							ActivityStatusEnum.FAILED, eventFailureDescription);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				}
				String currentActionId = requestInstance.getParameter("actionId");
				if (StringUtils.isBlank(currentActionId)) {
					ErrorCodeEnum.ERR_21343.setErrorCode(result);
					alert.prepareError("AppActionId cannot be empty").log();
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
							ActivityStatusEnum.FAILED, eventFailureDescription);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				}
				String currentfeatureId = requestInstance.getParameter("featureId");
				if (StringUtils.isBlank(currentfeatureId)) {
					alert.prepareError("Feature Id cannot be empty").log();
					ErrorCodeEnum.ERR_21318.setErrorCode(result);
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
							ActivityStatusEnum.FAILED, eventFailureDescription);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				}

				// Getting mfa scenario data from mfa table
				Map<String, String> inputParameterMap = new HashMap<String, String>();

				inputParameterMap.put(ODataQueryConstants.FILTER,
						"App_id eq '" + currentAppId + "' and Action_id eq '" + currentActionId + "'");
				inputParameterMap.put(ODataQueryConstants.SELECT, "id, Status_id, Description");

				String readSCAResponse = Executor.invokeService(ServiceURLEnum.SCA_READ, inputParameterMap, null,
						requestInstance);

				String oldStatusId = null;
				String oldDescription = null;
				String scaId = null;

				JSONObject readSCAResponseJSON = CommonUtilities.getStringAsJSONObject(readSCAResponse);
				if (readSCAResponseJSON != null && readSCAResponseJSON.has(FabricConstants.OPSTATUS)
						&& readSCAResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
						&& readSCAResponseJSON.has("sca_actions")) {
					JSONArray readSCAJSONArray = readSCAResponseJSON.optJSONArray("sca_actions");
					if (readSCAJSONArray == null || readSCAJSONArray.length() < 1) {
						ErrorCodeEnum.ERR_21336.setErrorCode(result);
						AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
								ActivityStatusEnum.FAILED, eventFailureDescription);
						result.addParam(new Param("status", "Failure", FabricConstants.STRING));
						return result;
					} else {
						JSONObject mfaRecord = readSCAJSONArray.getJSONObject(0);
						oldStatusId = mfaRecord.getString("Status_id");
						oldDescription = mfaRecord.getString("Description");
						scaId = mfaRecord.getString("id");
					}
				} else {
					ErrorCodeEnum.ERR_21336.setErrorCode(result);
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
							ActivityStatusEnum.FAILED, eventFailureDescription);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				}

				inputParameterMap.clear();
				/*
				 * inputParameterMap.put(ODataQueryConstants.FILTER, "App_id eq '" + appId +
				 * "' and Action_id eq '" + actionId + "' and risk_score eq '" + riskScoreEdit +
				 * "'"); inputParameterMap.put(ODataQueryConstants.SELECT, "id");
				 * 
				 * readSCAResponse = Executor.invokeService(ServiceURLEnum.SCA_READ,
				 * inputParameterMap, null, requestInstance);
				 * 
				 * readSCAResponseJSON = CommonUtilities.getStringAsJSONObject(readSCAResponse);
				 * if (readSCAResponseJSON != null &&
				 * readSCAResponseJSON.has(FabricConstants.OPSTATUS) &&
				 * readSCAResponseJSON.getInt(FabricConstants.OPSTATUS) == 0 &&
				 * readSCAResponseJSON.has("sca_actions")) { JSONArray readSCAJSONArray =
				 * readSCAResponseJSON.optJSONArray("sca_actions"); if (!(readSCAJSONArray ==
				 * null || readSCAJSONArray.length() < 1)) { JSONObject SCAJSONObj =
				 * readSCAJSONArray.getJSONObject(0); String SCAId = SCAJSONObj.getString("id");
				 * } }
				 */
				// Editing MFA Scenario
				inputMap.put("App_id", currentAppId);
				inputMap.put("Action_id", actionId);
				inputMap.put("Status_id", mfaScenarioStatusId);
				inputMap.put("Description", mfaScenarioDescription);
				inputMap.put("risk_score", riskScoreEdit);

				inputMap.put("id", scaId);
				inputMap.put("modifiedby", loggedInUserDetails.getUserName());
				inputMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());

				String editMFAResponse = Executor.invokeService(ServiceURLEnum.SCA_UPDATE, inputMap, null,
						requestInstance);

				JSONObject editMFAResponseJSON = CommonUtilities.getStringAsJSONObject(editMFAResponse);
				if ((editMFAResponseJSON != null) && editMFAResponseJSON.has(FabricConstants.OPSTATUS)
						&& editMFAResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
					diagnostic.prepareDebug("SCA Scenario updated successfully.").log();
					result.addParam(new Param("status", "Success", FabricConstants.STRING));
				} else {
					alert.prepareError("Failed to edit SCA Scenario").log();
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
							ActivityStatusEnum.FAILED, eventFailureDescription);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					ErrorCodeEnum.ERR_21337.setErrorCode(result);
					return result;
				}
			}
		} catch (Exception e) {
			alert.prepareError("Unexepected Error in create or edit SCA Scenario flow. Exception: ", e).log();
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, event,
					ActivityStatusEnum.FAILED, eventFailureDescription);
			ErrorCodeEnum.ERR_21338.setErrorCode(result);
			result.addParam(new Param("status", "Failure", FabricConstants.STRING));
		}
		return result;
	}

	public Result deleteSCAScenario(DataControllerRequest requestInstance) {
		Result result = new Result();
		try {

			// Validate actionId
			String appId = requestInstance.getParameter("appId");
			if (StringUtils.isBlank(appId)) {
				ErrorCodeEnum.ERR_20866.setErrorCode(result);
				alert.prepareError("ActionId cannot be empty").log();
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, EventEnum.DELETE,
						ActivityStatusEnum.FAILED, "SCA Scenario delete failed");
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}
			String actionId = requestInstance.getParameter("actionId");
			if (StringUtils.isBlank(actionId)) {
				ErrorCodeEnum.ERR_20866.setErrorCode(result);
				alert.prepareError("ActionId cannot be empty").log();
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, EventEnum.DELETE,
						ActivityStatusEnum.FAILED, "MFA Scenario delete failed");
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}

			// Fetch MFAId from featureaction table
			String mfaId = null;
			String actionName = null;
			String isMFAApplicable = null;
			Map<String, String> inputMap = new HashMap<String, String>();
			inputMap.put(ODataQueryConstants.FILTER, "id eq '" + actionId + "' and App_id eq '" + appId + "'");
			inputMap.put(ODataQueryConstants.SELECT, "MFA_id, name, isMFAApplicable");

			String readActionResponse = Executor.invokeService(ServiceURLEnum.EXTERNAL_FEATUREACTION_READ, inputMap,
					null, requestInstance);

			JSONObject readActionResponseJSON = CommonUtilities.getStringAsJSONObject(readActionResponse);
			if (readActionResponseJSON != null && readActionResponseJSON.has(FabricConstants.OPSTATUS)
					&& readActionResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readActionResponseJSON.has("external_feature_actions")) {
				JSONArray readActionJSONArray = readActionResponseJSON.optJSONArray("external_feature_actions");
				if (readActionJSONArray == null || readActionJSONArray.length() < 1) {
					alert.prepareError("No record found for Action Id").log();
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, EventEnum.DELETE,
							ActivityStatusEnum.FAILED, "MFA Scenario delete failed");
					ErrorCodeEnum.ERR_21351.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				} else {
					JSONObject currActionRecord = readActionJSONArray.getJSONObject(0);
					mfaId = currActionRecord.optString("MFA_id");
					actionName = currActionRecord.optString("name");
					isMFAApplicable = currActionRecord.optString("isMFAApplicable");

				}
			}
			inputMap.clear();
			inputMap.put(ODataQueryConstants.FILTER, "id eq '" + appId + "'");
			inputMap.put(ODataQueryConstants.SELECT, "Name");
			String readAppResponse = Executor.invokeService(ServiceURLEnum.APP_READ, inputMap, null, requestInstance);
			JSONObject readAppResponseJSON = CommonUtilities.getStringAsJSONObject(readAppResponse);
			if (!(readAppResponseJSON != null && readAppResponseJSON.has(FabricConstants.OPSTATUS)
					&& readAppResponseJSON.getInt(FabricConstants.OPSTATUS) == 0)) {
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				ErrorCodeEnum.ERR_21308.setErrorCode(result);
				alert.prepareError("Failed to read App").log();
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, EventEnum.DELETE,
						ActivityStatusEnum.FAILED, "SCA Scenario delete failed");
				ErrorCodeEnum.ERR_21351.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}
			/*
			 * else { JSONArray readAppResponseJSONArray =
			 * readAppResponseJSON.optJSONArray("app"); if (!(readAppResponseJSONArray ==
			 * null || readAppResponseJSONArray.length() < 1)) { JSONObject currAppRecord =
			 * readAppResponseJSONArray.getJSONObject(0); String appName =
			 * currAppRecord.getString("Name"); } }
			 */

			/*
			 * if (StringUtils.equals(isMFAApplicable, "false")) {
			 * ErrorCodeEnum.ERR_21349.setErrorCode(result);
			 * alert.prepareError("MFA is not applicable for this action").log(); result.addParam(new
			 * Param("status", "Failure", FabricConstants.STRING)); return result; }
			 */

			// Updating MFAId in featureaction table to null
			
			inputMap.clear();
			inputMap.put("id", actionId);
			inputMap.put("MFA_id", "");
			
			String editMFAResponse = Executor.invokeService(ServiceURLEnum.EXTERNAL_FEATUREACTION_UPDATE, inputMap, null,
					requestInstance);

			JSONObject editMFAResponseJSON = CommonUtilities.getStringAsJSONObject(editMFAResponse);
			if ((editMFAResponseJSON != null) && editMFAResponseJSON.has(FabricConstants.OPSTATUS)
					&& editMFAResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
				result.addParam(new Param("status", "Success", FabricConstants.STRING));
				diagnostic.prepareDebug("SCA Scenario updated successfully.").log();
			}
				else {
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					diagnostic.prepareDebug("SCA Scenario updation failed.").log();
				}
			// Deleting mfa scenario record
			inputMap.clear();
			inputMap.put("id", mfaId);
			inputMap.put("Action_id", actionId);
			inputMap.put("App_id", appId);

			String deleteMFAResponse = Executor.invokeService(ServiceURLEnum.SCA_DELETE, inputMap, null,
					requestInstance);

			JSONObject deleteMFAResponseJSON = CommonUtilities.getStringAsJSONObject(deleteMFAResponse);
			if (deleteMFAResponseJSON != null && deleteMFAResponseJSON.has(FabricConstants.OPSTATUS)
					&& deleteMFAResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
				result.addParam(new Param("status", "Success", FabricConstants.STRING));

			} else {
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				ErrorCodeEnum.ERR_21339.setErrorCode(result);
				alert.prepareError("Error in deleting sca scenario.").log();

			}

		} catch (Exception e) {
			ErrorCodeEnum.ERR_21339.setErrorCode(result);
			alert.prepareError("Error in deleting sca scenario.", e).log();
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, EventEnum.DELETE,
					ActivityStatusEnum.FAILED, "SCA Scenario delete failed");
		}
		return result;
	}

	public Result getSCAScenario(DataControllerRequest requestInstance) {
		Result result = new Result();
		try {

			// Fetch mfa scenario from mfa table
			String serverAppId = requestInstance.getParameter("appId");
			String serverActionId = requestInstance.getParameter("actionId");
			Map<String, String> inputMap = new HashMap<String, String>();
			String type_id = null;
			String feature_id = null;
			String action_name = null;
			String featureName = null;
			if (StringUtils.isNotBlank(serverAppId) && StringUtils.isNotBlank(serverActionId)) {
				// Fetch MFAId from featureaction table
				String mfaId = null;

				inputMap.put(ODataQueryConstants.FILTER,
						"App_id eq '" + serverAppId + "' and id eq '" + serverActionId + "'");
				inputMap.put(ODataQueryConstants.SELECT, "MFA_id,Type_id,Feature_id,name");

				String readActionResponse = Executor.invokeService(ServiceURLEnum.EXTERNAL_FEATUREACTION_READ, inputMap,
						null, requestInstance);

				JSONObject readActionResponseJSON = CommonUtilities.getStringAsJSONObject(readActionResponse);
				if (readActionResponseJSON != null && readActionResponseJSON.has(FabricConstants.OPSTATUS)
						&& readActionResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
						&& readActionResponseJSON.has("external_feature_actions")) {
					JSONArray readActionJSONArray = readActionResponseJSON.optJSONArray("external_feature_actions");
					if (readActionJSONArray == null || readActionJSONArray.length() < 1) {
						alert.prepareError("No record found for Action Id").log();
						AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, EventEnum.DELETE,
								ActivityStatusEnum.FAILED, "MFA Scenario delete failed");
						ErrorCodeEnum.ERR_21351.setErrorCode(result);
						result.addParam(new Param("status", "Failure", FabricConstants.STRING));
						return result;
					} else {
						JSONObject currActionRecord = readActionJSONArray.getJSONObject(0);
						mfaId = currActionRecord.optString("MFA_id");
						type_id = currActionRecord.optString("Type_id");
						feature_id = currActionRecord.optString("Feature_id");
						action_name = currActionRecord.optString("name");
					}
				} else {
					alert.prepareError("No record found for Action Id").log();
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS, EventEnum.DELETE,
							ActivityStatusEnum.FAILED, "SCA Scenario delete failed");
					ErrorCodeEnum.ERR_21351.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				}

				inputMap.clear();
				inputMap.put(ODataQueryConstants.FILTER, "id eq '" + feature_id + "'");
				inputMap.put(ODataQueryConstants.SELECT, "name, Status_id");

				String readFeatureResponse = Executor.invokeService(ServiceURLEnum.FEATURE_READ, inputMap, null,
						requestInstance);

				JSONObject readFeatureResponseJSON = CommonUtilities.getStringAsJSONObject(readFeatureResponse);
				if (readFeatureResponseJSON != null && readFeatureResponseJSON.has(FabricConstants.OPSTATUS)
						&& readFeatureResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
						&& readFeatureResponseJSON.has("feature")) {
					JSONArray readFeatureJSONArray = readFeatureResponseJSON.optJSONArray("feature");
					if (readFeatureJSONArray == null || readFeatureJSONArray.length() < 1) {
						alert.prepareError("Failed to Fetch Feature").log();
						ErrorCodeEnum.ERR_21352.setErrorCode(result);
						result.addParam(new Param("status", "Failure", FabricConstants.STRING));
						return result;
					} else {
						JSONObject currFeatureRecord = readFeatureJSONArray.getJSONObject(0);
						featureName = currFeatureRecord.optString("name");
					}
				} else {
					alert.prepareError("Failed to Fetch Action").log();
					ErrorCodeEnum.ERR_21310.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				}

				inputMap.clear();
				inputMap.put(ODataQueryConstants.FILTER, "id eq '" + mfaId + "' and App_id eq '" + serverAppId
						+ "' and Action_id eq '" + serverActionId + "'");
			}

			inputMap.put(ODataQueryConstants.SELECT, "id, App_id,Action_id, Status_id, Description,risk_score");

			String readSCAResponse = Executor.invokeService(ServiceURLEnum.SCA_READ, inputMap, null, requestInstance);

			JSONObject readSCAResponseJSON = CommonUtilities.getStringAsJSONObject(readSCAResponse);
			if (readSCAResponseJSON != null && readSCAResponseJSON.has(FabricConstants.OPSTATUS)
					&& readSCAResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readSCAResponseJSON.has("sca_actions")) {
				JSONArray readMFAJSONArray = readSCAResponseJSON.optJSONArray("sca_actions");
				Dataset scaDataset = new Dataset();
				scaDataset.setId("scaScenarios");
				if ((readMFAJSONArray != null) && (readMFAJSONArray.length() > 0)) {
					for (int indexVar = 0; indexVar < readMFAJSONArray.length(); indexVar++) {
						JSONObject mfaJSONObject = readMFAJSONArray.getJSONObject(indexVar);
						Record currRecord = new Record();

						String mfaScenarioStatusId = mfaJSONObject.optString("Status_id");
						String mfaScenarioDescription = mfaJSONObject.optString("Description");
						String riskScore_read = mfaJSONObject.optString("risk_score");
						while (riskScore_read.length() < 2) {
							riskScore_read = "0" + riskScore_read;
						}
						String appId_read = mfaJSONObject.optString("App_id");
						String actionId_read = mfaJSONObject.optString("Action_id");
						// Fetch appid from app table

						inputMap.clear();
						inputMap.put(ODataQueryConstants.FILTER,
								"App_id eq '" + appId_read + "' and id eq '" + actionId_read + "'");
						inputMap.put(ODataQueryConstants.SELECT, "MFA_id,Type_id,Feature_id,name");

						String readExtActionResponse = Executor.invokeService(
								ServiceURLEnum.EXTERNAL_FEATUREACTION_READ, inputMap, null, requestInstance);

						JSONObject readExtActionResponseJSON = CommonUtilities
								.getStringAsJSONObject(readExtActionResponse);
						if (readExtActionResponseJSON != null && readExtActionResponseJSON.has(FabricConstants.OPSTATUS)
								&& readExtActionResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
								&& readExtActionResponseJSON.has("external_feature_actions")) {
							JSONArray readExtActionJSONArray = readExtActionResponseJSON
									.optJSONArray("external_feature_actions");
							if (readExtActionJSONArray == null || readExtActionJSONArray.length() < 1) {
								alert.prepareError("No record found for Action Id").log();
								AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MFASCENARIOS,
										EventEnum.DELETE, ActivityStatusEnum.FAILED, "SCA Scenario delete failed");
								ErrorCodeEnum.ERR_21351.setErrorCode(result);
								result.addParam(new Param("status", "Failure", FabricConstants.STRING));
								return result;
							} else {
								JSONObject currActionRecord = readExtActionJSONArray.getJSONObject(0);
								type_id = currActionRecord.optString("Type_id");
								feature_id = currActionRecord.optString("Feature_id");
								action_name = currActionRecord.optString("name");
							}
						}

						Param actionId_Param = new Param("Action_id", actionId_read, FabricConstants.STRING);
						currRecord.addParam(actionId_Param);
						Param actionName1_Param = new Param("actionName", action_name, FabricConstants.STRING);
						currRecord.addParam(actionName1_Param);
						Param featureId_Param = new Param("App_id", appId_read, FabricConstants.STRING);
						currRecord.addParam(featureId_Param);
						Param featureName_Param = new Param("Description", mfaScenarioDescription,
								FabricConstants.STRING);
						currRecord.addParam(featureName_Param);
						Param actionName_Param = new Param("risk_score", riskScore_read, FabricConstants.STRING);
						currRecord.addParam(actionName_Param);
						Param actionType_Param = new Param("Status_id", mfaScenarioStatusId, FabricConstants.STRING);
						currRecord.addParam(actionType_Param);
						Param actionType_Paramm = new Param("actionType", type_id, FabricConstants.STRING);
						currRecord.addParam(actionType_Paramm);
						Param feature_idParam = new Param("featureId", feature_id, FabricConstants.STRING);
						currRecord.addParam(feature_idParam);

						inputMap.clear();
						inputMap.put(ODataQueryConstants.FILTER, "id eq '" + feature_id + "'");
						inputMap.put(ODataQueryConstants.SELECT, "name, Status_id");

						String readFeatureResponse = Executor.invokeService(ServiceURLEnum.FEATURE_READ, inputMap, null,
								requestInstance);

						JSONObject readFeatureResponseJSON = CommonUtilities.getStringAsJSONObject(readFeatureResponse);
						if (readFeatureResponseJSON != null && readFeatureResponseJSON.has(FabricConstants.OPSTATUS)
								&& readFeatureResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
								&& readFeatureResponseJSON.has("feature")) {
							JSONArray readFeatureJSONArray = readFeatureResponseJSON.optJSONArray("feature");
							if (readFeatureJSONArray == null || readFeatureJSONArray.length() < 1) {
								alert.prepareError("Failed to Fetch Feature").log();
								ErrorCodeEnum.ERR_21352.setErrorCode(result);
								result.addParam(new Param("status", "Failure", FabricConstants.STRING));
								return result;
							} else {
								JSONObject currFeatureRecord = readFeatureJSONArray.getJSONObject(0);
								featureName = currFeatureRecord.optString("name");
							}
						} else {
							alert.prepareError("Failed to Fetch Action").log();
							ErrorCodeEnum.ERR_21310.setErrorCode(result);
							result.addParam(new Param("status", "Failure", FabricConstants.STRING));
							return result;
						}

						Param feature_Param = new Param("featureName", featureName, FabricConstants.STRING);
						currRecord.addParam(feature_Param);
						String appName = null;

						inputMap.clear();
						inputMap.put(ODataQueryConstants.FILTER, "id eq '" + appId_read + "'");
						inputMap.put(ODataQueryConstants.SELECT, "Name, Status_id");

						String readAppResponse = Executor.invokeService(ServiceURLEnum.APP_READ, inputMap, null,
								requestInstance);

						JSONObject readAppResponseJSON = CommonUtilities.getStringAsJSONObject(readAppResponse);
						if (readAppResponseJSON != null && readAppResponseJSON.has(FabricConstants.OPSTATUS)
								&& readAppResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
								&& readAppResponseJSON.has("app")) {
							JSONArray readAppJSONArray = readAppResponseJSON.optJSONArray("app");
							if (readAppJSONArray == null || readAppJSONArray.length() < 1) {
								ErrorCodeEnum.ERR_21319.setErrorCode(result);
								alert.prepareError("Invalid App Id").log();
								result.addParam(new Param("status", "Failure", FabricConstants.STRING));
								return result;
							} else {
								JSONObject currAppRecord = readAppJSONArray.getJSONObject(0);
								appName = currAppRecord.optString("Name");
							}
						} else {
							ErrorCodeEnum.ERR_21319.setErrorCode(result);
							alert.prepareError("Invalid App Id").log();
							result.addParam(new Param("status", "Failure", FabricConstants.STRING));
							return result;
						}

						Param appName_Param = new Param("appName", appName, FabricConstants.STRING);
						currRecord.addParam(appName_Param);
						scaDataset.addRecord(currRecord);
					}
					result.addDataset(scaDataset);
				}
			} else {
				result.addParam(new Param("message", readSCAResponse, FabricConstants.STRING));
				alert.prepareError("Failed to Fetch SCA Scenario Response: " + readSCAResponse).log();
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				ErrorCodeEnum.ERR_21341.setErrorCode(result);
				return result;
			}

			result.addParam(new Param("status", "Success", FabricConstants.STRING));
			return result;

		} catch (Exception e) {
			alert.prepareError("Unexepected Error in Fetching SCA Scenario. Exception: ", e).log();
			result.addParam(new Param("status", "Failure", FabricConstants.STRING));
			ErrorCodeEnum.ERR_21341.setErrorCode(result);
		}
		return result;
	}
}