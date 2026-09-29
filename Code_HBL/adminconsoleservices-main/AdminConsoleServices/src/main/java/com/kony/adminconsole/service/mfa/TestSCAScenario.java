package com.kony.adminconsole.service.mfa;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import org.slf4j.LoggerFactory;

public class TestSCAScenario implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
  
  public Object invoke(String s, Object[] objects, DataControllerRequest requestInstance, DataControllerResponse dataControllerResponse) throws Exception {
    Result result = new Result();
    try {
      alert.prepareError("Start of TestSCAScenario " + objects[1]).log();
      HashMap<String, String> hm = (HashMap<String, String>)objects[1];
      String clientAppId = ((String)hm.get("appId")).toString();
      if (StringUtils.isBlank(clientAppId)) {
        ErrorCodeEnum.ERR_21318.setErrorCode(result);
        alert.prepareError("App Id cannot be empty").log();
        result.addParam(new Param("status", "Failure", "string"));
        return result;
      } 
      String actionId = ((String)hm.get("actionId")).toString();
      if (StringUtils.isBlank(actionId)) {
        ErrorCodeEnum.ERR_20866.setErrorCode(result);
        alert.prepareError("Action Id is a mandatory input").log();
        result.addParam(new Param("status", "Failure", "string"));
        return result;
      } 
      Map<String, String> inputMap = new HashMap<>();
      inputMap.put("$filter", "id eq '" + actionId + "' and App_id eq '" + clientAppId + "'");
      inputMap.put("$select", "MFA_id, Feature_id");
      String readFeatureActionResponse = Executor.invokeService(ServiceURLEnum.EXTERNAL_FEATUREACTION_READ, inputMap, 
          null, requestInstance);
      alert.prepareError("readFeatureActionResponse" + readFeatureActionResponse).log();
      String mfaId = null;
      String featureId = null;
      JSONObject readFeatureActionResponseJSON = CommonUtilities.getStringAsJSONObject(readFeatureActionResponse);
      if (readFeatureActionResponseJSON != null && readFeatureActionResponseJSON.has("opstatus") && 
        readFeatureActionResponseJSON.getInt("opstatus") == 0 && 
        readFeatureActionResponseJSON.has("external_feature_actions")) {
        JSONArray readFeatureActionJSONArray = readFeatureActionResponseJSON
          .optJSONArray("external_feature_actions");
        if (readFeatureActionJSONArray == null || readFeatureActionJSONArray.length() < 1) {
          ErrorCodeEnum.ERR_21334.setErrorCode(result);
          alert.prepareError("Invalid FeatureActionId").log();
          result.addParam(new Param("status", "Failure", "string"));
          return result;
        } 
        JSONObject currFeatureActionRecord = readFeatureActionJSONArray.getJSONObject(0);
        mfaId = currFeatureActionRecord.optString("MFA_id");
        featureId = currFeatureActionRecord.getString("Feature_id");
      } else {
        ErrorCodeEnum.ERR_21334.setErrorCode(result);
        alert.prepareError("Invalid FeatureActionId").log();
        result.addParam(new Param("status", "Failure", "string"));
        return result;
      } 
      inputMap.clear();
      inputMap.put("$filter", "id eq '" + featureId + "'");
      inputMap.put("$select", "Status_id");
      String readFeatureResponse = Executor.invokeService(ServiceURLEnum.FEATURE_READ, inputMap, null, 
          requestInstance);
      String featureStatus = null;
      JSONObject readFeatureResponseJSON = CommonUtilities.getStringAsJSONObject(readFeatureResponse);
      if (readFeatureResponseJSON != null && readFeatureResponseJSON.has("opstatus") && 
        readFeatureResponseJSON.getInt("opstatus") == 0 && 
        readFeatureResponseJSON.has("feature")) {
        JSONArray readFeatureJSONArray = readFeatureResponseJSON.optJSONArray("feature");
        if (readFeatureJSONArray == null || readFeatureJSONArray.length() < 1) {
          ErrorCodeEnum.ERR_21352.setErrorCode(result);
          alert.prepareError("Failed to fetch feature").log();
          result.addParam(new Param("status", "Failure", "string"));
          return result;
        } 
        JSONObject currFeatureRecord = readFeatureJSONArray.getJSONObject(0);
        featureStatus = currFeatureRecord.optString("Status_id");
        if (!StringUtils.equals(featureStatus, "SID_FEATURE_ACTIVE")) {
          ErrorCodeEnum.ERR_21353.setErrorCode(result);
          alert.prepareError("Feature is not active").log();
          result.addParam(new Param("status", "Feature is not active", "string"));
          return result;
        } 
      } else {
        ErrorCodeEnum.ERR_21352.setErrorCode(result);
        alert.prepareError("Failed to fetch feature").log();
        result.addParam(new Param("status", "Failure", "string"));
        return result;
      } 
      inputMap.clear();
      inputMap.put("$filter", 
          "id eq '" + mfaId + "' and App_id eq '" + clientAppId + "' and Action_id eq '" + actionId + "'");
      inputMap.put("$select", "Status_id,risk_score");
      String readMFAResponse = Executor.invokeService(ServiceURLEnum.SCA_READ, inputMap, null, requestInstance);
      String isSCARequired = null;
      String riskScore = null;
      JSONObject readMFAResponseJSON = CommonUtilities.getStringAsJSONObject(readMFAResponse);
      if (readMFAResponseJSON != null && readMFAResponseJSON.has("opstatus") && 
        readMFAResponseJSON.getInt("opstatus") == 0 && 
        readMFAResponseJSON.has("sca_actions")) {
        JSONArray readMFAJSONArray = readMFAResponseJSON.optJSONArray("sca_actions");
        if (readMFAJSONArray == null || readMFAJSONArray.length() < 1) {
          ErrorCodeEnum.ERR_21341.setErrorCode(result);
          alert.prepareError("Failed to fetch SCA Scenario").log();
          result.addParam(new Param("status", "Failure", "string"));
          return result;
        } 
        JSONObject currAppActionRecord = readMFAJSONArray.getJSONObject(0);
        String statusId = currAppActionRecord.optString("Status_id");
        riskScore = currAppActionRecord.optString("risk_score");
        if (statusId.equals("SID_ACTIVE")) {
          isSCARequired = "true";
        } else {
          isSCARequired = "false";
        } 
      } else {
        alert.prepareError("SCA Scenario is empty").log();
        result.addParam(new Param("status", "Success", "string"));
        return result;
      } 
      Param isSCARequired_Param = new Param("isSCARequired", isSCARequired, "string");
      result.addParam(isSCARequired_Param);
      Param riskScore_Param = new Param("risk_score", riskScore, "String");
      result.addParam(riskScore_Param);
    } catch (Exception e) {
      alert.prepareError("Unexepected Error in Fetching scaConfiguration and Scenario. Exception: ", e).log();
      result.addParam(new Param("status", "Failure", "string"));
      ErrorCodeEnum.ERR_21342.setErrorCode(result);
    } 
    return result;
  }
}
