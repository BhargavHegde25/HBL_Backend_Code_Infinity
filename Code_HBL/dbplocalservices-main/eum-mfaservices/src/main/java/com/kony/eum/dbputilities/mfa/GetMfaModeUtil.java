package com.kony.eum.dbputilities.mfa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.dbp.core.constants.DBPConstants;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetMfaModeUtil {

    private GetMfaModeUtil() {

    }

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    public static JsonObject getMfaMode(String clientAppId, String actionId, DataControllerRequest requestInstance) {
        JsonObject result = new JsonObject();
        try {
            String appId = null;
            //To be removed later
            boolean isOnboarding = clientAppId.equalsIgnoreCase("Onboarding");
            
            if (StringUtils.isBlank(clientAppId)) {
                ErrorCodeEnum.ERR_10718.setErrorCode(result);
                alert.prepareError("App Id cannot be empty").log();
                return result;
            }
            JSONObject clientAppIdToServerMap = getClientAppIdMap();
            if (clientAppIdToServerMap == null) {
                ErrorCodeEnum.ERR_10719.setErrorCode(result);
                return result;
            }

            if (clientAppIdToServerMap.has(clientAppId)) {
                appId = clientAppIdToServerMap.getString(clientAppId);
            } else {
                ErrorCodeEnum.ERR_10720.setErrorCode(result);
                return result;
            }
            
            // Validate ActionId
            if (StringUtils.isBlank(actionId)) {
                ErrorCodeEnum.ERR_10721.setErrorCode(result);
                alert.prepareError("Action Id is a mandatory input").log();
                return result;
            }

            // Fetch MFAId from featureaction table
            Map<String, String> inputMap = new HashMap<>();
            inputMap.put(DBPUtilitiesConstants.FILTER,
                    "App_id" + DBPUtilitiesConstants.EQUAL + appId + DBPUtilitiesConstants.AND + "id"
                            + DBPUtilitiesConstants.EQUAL + actionId);
            inputMap.put(DBPUtilitiesConstants.SELECT, "MFA_id, isMFAApplicable, Feature_id");

            Result readFeatureActionResponse = HelperMethods.callApi(requestInstance, inputMap,
                    HelperMethods.getHeaders(requestInstance), URLConstants.FEATURE_ACTION_GET);

            String mfaId = null;
            String isMFAApplicable = null;
            String featureId = null;

            if (HelperMethods.hasRecords(readFeatureActionResponse)) {
                Record record = readFeatureActionResponse.getAllDatasets().get(0).getAllRecords().get(0);
                mfaId = HelperMethods.getFieldValue(record, "MFA_id");
                isMFAApplicable = HelperMethods.getFieldValue(record, "isMFAApplicable");
                featureId = HelperMethods.getFieldValue(record, "Feature_id");

            } else {
                ErrorCodeEnum.ERR_10722.setErrorCode(result);
                alert.prepareError("Invalid FeatureActionId").log();
                return result;

            }

            if (StringUtils.equals(isMFAApplicable, "false")) {
                ErrorCodeEnum.ERR_10723.setErrorCode(result);
                alert.prepareError("MFA is not applicable").log();
                return result;
            }

            // Fetch FeatureStatus from feature table
            inputMap.clear();
            inputMap.put(DBPUtilitiesConstants.FILTER, "id" + DBPUtilitiesConstants.EQUAL + featureId);
            inputMap.put(DBPUtilitiesConstants.SELECT, "Status_id");

            Result readFeatureResponse =
                    HelperMethods.callApi(requestInstance, inputMap, HelperMethods.getHeaders(requestInstance),
                            URLConstants.FEATURE_GET);
            String featureStatus = null;

            if (HelperMethods.hasRecords(readFeatureResponse)) {
                Record record = readFeatureResponse.getAllDatasets().get(0).getAllRecords().get(0);
                featureStatus = HelperMethods.getFieldValue(record, "Status_id");
                if (!StringUtils.equals(featureStatus, "SID_FEATURE_ACTIVE")) {
                    ErrorCodeEnum.ERR_10725.setErrorCode(result);
                    alert.prepareError("Feature is not active").log();
                    return result;
                }

            } else {
                ErrorCodeEnum.ERR_10724.setErrorCode(result);
                alert.prepareError("Failed to fetch feature").log();
                return result;
            }

            // Fetch mfa scenarios from mfa table
            inputMap.clear();
            inputMap.put(DBPUtilitiesConstants.FILTER,
                    "id" + DBPUtilitiesConstants.EQUAL + mfaId + DBPUtilitiesConstants.AND +
                            "App_id" + DBPUtilitiesConstants.EQUAL + appId + DBPUtilitiesConstants.AND +
                            "Action_id" + DBPUtilitiesConstants.EQUAL + actionId);
            inputMap.put(DBPUtilitiesConstants.SELECT,
                    "Status_id, FrequencyType_id, FrequencyValue, PrimaryMFAType, SecondaryMFAType, SMSText, EmailSubject, EmailBody");

            Result readMFAResponse = HelperMethods.callApi(requestInstance, inputMap,
                    HelperMethods.getHeaders(requestInstance), URLConstants.MFA_GET);
            String isMFARequired = null;
            String frequencyTypeId = null;
            String frequencyValue = null;
            String primaryMFATypeId = null;
            String secondaryMFATypeId = null;
            String smsText = null;
            String emailSubject = null;
            String emailBody = null;

            if (HelperMethods.hasRecords(readMFAResponse)) {
                Record record = readMFAResponse.getAllDatasets().get(0).getAllRecords().get(0);
                String statusId = HelperMethods.getFieldValue(record, "Status_id");
                if (statusId.equals("SID_ACTIVE"))
                	//To be replaced with commented line later
                    isMFARequired = isOnboarding ? "false" : "true";
                	//isMFARequired = "true";	
                else
                    isMFARequired = "false";

                frequencyTypeId = HelperMethods.getFieldValue(record, "FrequencyType_id");
                frequencyValue = HelperMethods.getFieldValue(record, "FrequencyValue");
                primaryMFATypeId = HelperMethods.getFieldValue(record, "PrimaryMFAType");
                secondaryMFATypeId = HelperMethods.getFieldValue(record, "SecondaryMFAType");
                smsText = HelperMethods.getFieldValue(record, "SMSText");
                emailSubject = HelperMethods.getFieldValue(record, "EmailSubject");
                emailBody = HelperMethods.getFieldValue(record, "EmailBody");

            } else {
                alert.prepareError("MFA Scenario is empty").log();
                ErrorCodeEnum.ERR_10730.setErrorCode(result);
                return result;

            }
            result.addProperty("isMFARequired", isMFARequired);
            result.addProperty("frequencyTypeId", frequencyTypeId);
            result.addProperty("frequencyValue", frequencyValue);
            result.addProperty("primaryMFATypeId", primaryMFATypeId);
            result.addProperty("secondaryMFATypeId", secondaryMFATypeId);

            // Fetch mfaTypeIds from mfatype table
            inputMap.clear();
            inputMap.put(DBPUtilitiesConstants.FILTER, "id" + DBPUtilitiesConstants.EQUAL + primaryMFATypeId +
                    DBPUtilitiesConstants.OR + "id" + DBPUtilitiesConstants.EQUAL + secondaryMFATypeId);
            inputMap.put(DBPUtilitiesConstants.SELECT, "id, Name");
            Result readMFATypeResponse =
                    HelperMethods.callApi(requestInstance, inputMap, HelperMethods.getHeaders(requestInstance),
                            URLConstants.MFATYPE_GET);

            if (HelperMethods.hasRecords(readMFATypeResponse)) {
                List<Record> records = readMFATypeResponse.getAllDatasets().get(0).getAllRecords();
                JsonArray mfaTypes = new JsonArray();

                for (Record record : records) {
                    JsonObject currentObject = new JsonObject();
                    String mfaTypeId = record.getParamValueByName("id");
                    currentObject.addProperty("mfaTypeId", mfaTypeId);
                    String mfaTypeName = record.getParamValueByName("Name");
                    currentObject.addProperty("mfaTypeName", mfaTypeName);

                    if ("SECURE_ACCESS_CODE".equals(mfaTypeId)) {
                        currentObject.addProperty("smsText", smsText);
                        currentObject.addProperty("emailSubject", emailSubject);
                        currentObject.addProperty("emailBody", emailBody);
                    }

                    inputMap.clear();
                    inputMap.clear();
                    inputMap.put(DBPUtilitiesConstants.FILTER, "MFA_id" + DBPUtilitiesConstants.EQUAL + mfaTypeId);
                    inputMap.put(DBPUtilitiesConstants.SELECT, "MFAKey_id, value");
                    Result readMFAConfigResponse = HelperMethods.callApi(requestInstance, inputMap,
                            HelperMethods.getHeaders(requestInstance), URLConstants.MFACONFIGURATIONS_GET);
                    if (HelperMethods.hasRecords(readMFAConfigResponse)) {
                        List<Record> mfaConfigRecords = readMFAConfigResponse.getAllDatasets().get(0).getAllRecords();
                        JsonArray mfaConfigurations = new JsonArray();
                        for (Record mfaconfigRecord : mfaConfigRecords) {
                            JsonObject object = new JsonObject();
                            String mfaKeyId = HelperMethods.getFieldValue(mfaconfigRecord, "MFAKey_id");
                            String mfaKeyValue = HelperMethods.getFieldValue(mfaconfigRecord, "value");
                            object.addProperty("mfaKey", mfaKeyId);
                            object.addProperty("mfaValue", mfaKeyValue);
                            mfaConfigurations.add(object);

                        }
                        currentObject.add("mfaConfigurations", mfaConfigurations);
                        mfaTypes.add(currentObject);

                    } else {
                        result.addProperty("message",
                                readMFAConfigResponse.getParamValueByName(DBPConstants.DBP_ERROR_CODE_KEY));
                        alert.prepareError("Failed to Fetch MFAConfig Response: ").log();
                        ErrorCodeEnum.ERR_10727.setErrorCode(result);
                        return result;

                    }

                }
                result.add("mfaTypes", mfaTypes);
            } else {
                ErrorCodeEnum.ERR_10728.setErrorCode(result);
                alert.prepareError("Primary mfa type and secondary mfa type is invalid").log();
                return result;
            }

        } catch (Exception e) {
            alert.prepareError("Unexepected Error in Fetching mfaConfiguration and Scenario. Exception: ", e).log();
            ErrorCodeEnum.ERR_10729.setErrorCode(result);
        }
        return result;

    }

    private static JSONObject getClientAppIdMap() {
        try {
            String clientAppIdMapping =
                    EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.ADMIN_CONSOLE_APPIDAPP_MAPPING_KEY);
            if (StringUtils.isNotBlank(clientAppIdMapping)) {
                return new JSONObject(clientAppIdMapping);
            }
        } catch (Exception e) {
            alert.prepareError("Failed while parsing runtime configuration AC_APPID_TO_APP_MAPPING", e).log();
        }
        return null;
    }

}
