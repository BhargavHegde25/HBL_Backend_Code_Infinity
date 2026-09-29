package com.kony.adminconsole.campaign.utilities;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.adminconsole.commons.handler.EnvironmentConfigurationsHandler;
import com.kony.adminconsole.utilities.ACConstants;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.ServiceId;
import com.konylabs.middleware.api.OperationData;
import com.konylabs.middleware.api.ServiceRequest;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.konylabs.middleware.exceptions.MiddlewareException;

public final class CampaignUtil {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
    public static final String DBP_ERROR_MESSAGE = "dbpErrMsg";
    public static final String DBP_ERROR_CODE = "dbpErrCode";
    public static final String LOOP_SEPERATOR = "loop_seperator";
    public static final String LOOP_COUNT = "loop_count";
    public static final String LOOP_DATASET = "LoopDataset";

    public static Result invokeService(String serviceId, String operationId, Map<String, Object> inputMap)
            throws MiddlewareException {
        if (diagnostic.isDebugEnabled()) {
            diagnostic.prepareDebug("Service is being hit" + serviceId + " " + operationId + "with input Map" + inputMap).log();
        }
        ServiceRequest serviceRequest = ServicesManagerHelper.getServicesManager()
                .getRequestBuilder(getOperationData(serviceId, operationId)).withInputs(inputMap).build();
        Result res = serviceRequest.invokeServiceAndGetResult();
        if (diagnostic.isDebugEnabled()) {
            diagnostic.prepareDebug(operationId + "response is " + ResultToJSON.convert(res));
        }
        return res;
    }

    public static OperationData getOperationData(String serviceId, String operationId) throws MiddlewareException {
        try {
            return ServicesManagerHelper.getServicesManager().getOperationDataBuilder().withServiceId(serviceId)
                    .withOperationId(operationId).build();
        } catch (MiddlewareException e) {
            alert.prepareError("Error while invoking operation " + operationId + " of service " + serviceId).log();
            throw e;
        }
    }

    public static String getServerProperty(String propertyName, String defaultValue) {
        String value = null;
        try {
            value = ServicesManagerHelper.getServicesManager().getConfigurableParametersHelper()
                    .getServerProperty(propertyName);
        } catch (MiddlewareException e) {
            alert.prepareError("Error while fetching server property " + propertyName).log();

        }
        return value != null ? value : defaultValue;
    }

    public static int getIntServerProperty(String propertyName, int defaultValue) {
        return Integer.parseInt(CampaignUtil.getServerProperty(propertyName, String.valueOf(defaultValue)));
    }

    public static String decodeValue(String value) throws UnsupportedEncodingException {
        return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
    }

    public static void addDBPErrCodeAndmsg(Result campaignsSelected, String errmsgString, int errorCode) {
        campaignsSelected.addParam(DBP_ERROR_MESSAGE, errmsgString);
        campaignsSelected.addIntParam(DBP_ERROR_CODE, errorCode);
    }

    public static Result invokeServiceUpdateUserForSegments(JsonArray segmentUpdateObj) throws MiddlewareException {
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put(ACConstants.PROFILES, segmentUpdateObj);
        String campaignBackend = getServerProperty("CAMPAIGNS_BACKEND", "DBXDB");
        if(campaignBackend.equalsIgnoreCase("DBXDB")) {
        	return updateUsersForSegmemtDbOperation(inputMap);
        }
        return CampaignUtil.invokeService(ACConstants.CAMPAIGN_MS_SERVICE_NAME, ACConstants.UPDATE_USERS_FOR_SEGMENT,
                inputMap);
    }

    private static Result updateUsersForSegmemtDbOperation(Map<String, Object> inputMap) throws MiddlewareException {
		Result result = new Result();
		StringBuilder sb = new StringBuilder(); 
		JsonArray profiles = (JsonArray)inputMap.get(ACConstants.PROFILES);
		for(JsonElement ele : profiles) {
			JsonObject profile = ele.getAsJsonObject();
			sb.append(",");
			sb.append(profile.get(ACConstants.PROFILE_ID).getAsString());
			sb.append(":");
			sb.append(profile.get(ACConstants.NUMBER_OF_USERS).getAsString());
		}
		sb.deleteCharAt(0);
		inputMap.clear();
		inputMap.put("_profileDataCSV", sb.toString());
		diagnostic.prepareDebug("inputMap:"+inputMap).log();
		invokeService(ServiceId.CRUDLAYER, OperationName.DB_UPDATE_PROFILE_USERS_PROC, inputMap);
		return result;
	}

	public static boolean invokeUpdateUsersForSegment(JsonArray segmentUpdateObj) {
        try {
            Result res = CampaignUtil.invokeServiceUpdateUserForSegments(segmentUpdateObj);
            if (Integer.valueOf(res.getParamByName(ACConstants.OPSTATUS).getValue()) != 0) {
                alert.prepareError("UpdateUsers failed " + ErrorCodeEnum.ERR_21801.getMessage()
                        + res.getParamValueByName(ACConstants.ERRMSG) + res.getParamValueByName(ACConstants.ERRCODE)).log();
                return false;
            }
        } catch (MiddlewareException e) {
            alert.prepareError("Async Update task error " + ErrorCodeEnum.ERR_21801.getMessage(), e).log();
            return false;
        }

        return true;

    }

    private CampaignUtil() {
    }

}
