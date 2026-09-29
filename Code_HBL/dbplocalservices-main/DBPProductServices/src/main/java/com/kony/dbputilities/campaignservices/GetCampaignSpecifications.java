package com.kony.dbputilities.campaignservices;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONObject;

import com.dbp.core.constants.DBPConstants;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.dbp.dto.CampaignSpecification;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.utils.ThreadExecutor;

public class GetCampaignSpecifications implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest, DataControllerResponse dcResponse) throws Exception {

		Result result = new Result();

		try {
			Map<String, String> campaignCounts = getCampaignCount(dcRequest);
			
			String channelId = null;
			String deviceId = null;

			String reportingParams = (String) HelperMethods.getHeadersWithReportingParams(dcRequest)
					.get(DBPUtilitiesConstants.X_KONY_REPORTING_PARAMS);

			if (StringUtils.isNotBlank(reportingParams)) {
				JSONObject reportingParamsJson = new JSONObject(URLDecoder.decode(reportingParams, StandardCharsets.UTF_8.name()));

				channelId = reportingParamsJson.optString("chnl");
				deviceId = reportingParamsJson.optString("did");
			}

			if (StringUtils.isBlank(channelId)) {
				return ErrorCodeEnum.ERR_12451.setErrorCode(result);
			}
			else if (!(channelId.equalsIgnoreCase("mobile") || channelId.equalsIgnoreCase("desktop"))) {
				return ErrorCodeEnum.ERR_12452.setErrorCode(result);
			} 
			else if (methodID.contains("PreLogin") && !(channelId.equalsIgnoreCase("mobile"))) {
				return ErrorCodeEnum.ERR_12452.setErrorCode(result);
			}

			Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

			String screenName = methodID.contains("PreLogin") ? "PRE_LOGIN" : inputParams.get("screenName");
			if (StringUtils.isBlank(screenName)) {
				return ErrorCodeEnum.ERR_12456.setErrorCode(result);
			}

			String scale = inputParams.get("scale");
			if (StringUtils.isBlank(scale)) {
				return ErrorCodeEnum.ERR_12457.setErrorCode(result);
			}

			String channelName = channelId.equalsIgnoreCase("mobile") ? "MOBILE" : "WEB";
			String placeholderName = channelName + "_" + screenName;

			int campaignCount = -1;
			if(campaignCounts.get(placeholderName) != null) {
				campaignCount = Integer.parseInt(campaignCounts.get(placeholderName));
			}
			else {
				return ErrorCodeEnum.ERR_12450.setErrorCode(result);
			}

			String username = null;
			if (!methodID.contains("PreLogin")) {
				Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromIdentityService(dcRequest);
				username = loggedInUserInfo.get("UserName");

				if (StringUtils.isBlank(username)) {
					return ErrorCodeEnum.ERR_12455.setErrorCode(result);
				}
			}

			Dataset campaignspecificationsDataset = new Dataset();
			campaignspecificationsDataset.setId("campaignSpecifications");

			Map<String, String> procParameterMap = new HashMap<>();
			procParameterMap.put("_currentTimestamp", getCurrentDate());
			procParameterMap.put("_scale", scale);
			if (methodID.contains("PreLogin")) {
				procParameterMap.put("_deviceId", deviceId);
			} else {
				procParameterMap.put("_username", username);
				procParameterMap.put("_channel", channelName);
				procParameterMap.put("_screen", screenName);
			}

			JsonObject procResponse = HelperMethods.callApiJson(dcRequest, procParameterMap,
					HelperMethods.getHeaders(dcRequest), methodID.contains("PreLogin") ? 
							URLConstants.CAMPAIGN_DBP_SPECIFICATION_PRELOGIN_GET_PROC : URLConstants.CAMPAIGN_DBP_SPECIFICATION_GET_PROC);

			if (!HelperMethods.isJsonNotNull(procResponse) || procResponse.get(DBPConstants.FABRIC_OPSTATUS_KEY).getAsInt() != 0) {
				return ErrorCodeEnum.ERR_12450.setErrorCode(result);
			}

			JsonArray procResponseJSONArray = HelperMethods.isJsonNotNull(procResponse.get("records"))
					? procResponse.get("records").getAsJsonArray() : new JsonArray();

					int iterationCount = procResponseJSONArray.size() > campaignCount ? campaignCount : procResponseJSONArray.size();


					List<CampaignSpecification> campaignSpecifications = new ArrayList<CampaignSpecification>();

					for (int i = 0; i < iterationCount; ++i) {

						JsonObject readResponseJSONObject = procResponseJSONArray.get(i).getAsJsonObject();
						Record campaignspecificationRecord = new Record();

						CampaignSpecification campaignSpecification = new CampaignSpecification();
						campaignSpecification.setCampaignId(readResponseJSONObject.get("campaign_id").getAsString());
						campaignSpecification.setCampaignPlaceholderId(readResponseJSONObject.get("campaignplaceholder_id").getAsString());
						campaignSpecification.setImageIndex(readResponseJSONObject.get("image_index").getAsString());
						campaignSpecification.setImageURL(readResponseJSONObject.get("image_url").getAsString());
						campaignSpecification.setDestinationURL(HelperMethods.isJsonNotNull(readResponseJSONObject.get("destination_url"))
								? readResponseJSONObject.get("destination_url").getAsString() : "");

						campaignspecificationRecord.addParam(new Param("campaignId", campaignSpecification.getCampaignId(), "string"));
						campaignspecificationRecord.addParam(new Param("campaignPlaceholderId", campaignSpecification.getCampaignPlaceholderId(), "string"));
						campaignspecificationRecord.addParam(new Param("imageIndex", campaignSpecification.getImageIndex(), "string"));
						campaignspecificationRecord.addParam(new Param("imageURL", campaignSpecification.getImageURL(), "string"));
						campaignspecificationRecord.addParam(new Param("destinationURL", campaignSpecification.getDestinationURL(), "string"));

						campaignspecificationsDataset.addRecord(campaignspecificationRecord);

						campaignSpecifications.add(campaignSpecification);
					}

					result.addDataset(campaignspecificationsDataset);


					// ** Update Campaign Display Count **
					Callable<List<String>> incrementCampaignDisplayCountCallable = new Callable<List<String>>() {
						@Override
						public List<String> call() throws Exception {
							return CampaignHandler.incrementCampaignDisplayCount(dcRequest, campaignSpecifications);
						}
					};

					ThreadExecutor.getExecutor(dcRequest).execute(incrementCampaignDisplayCountCallable);
		} 
		catch (Exception e) {
			alert.prepareError("Exception occured in GetCampaignSpecifications JAVA service. Error: ", e).log();
			ErrorCodeEnum.ERR_12000.setErrorCode(result);
		}

		return result;
	}

	public Map<String, String> getCampaignCount(DataControllerRequest dcRequest) throws HttpCallException {

		Map<String, String> campaignCounts = new HashMap<String, String>();
		Map<String, String> procParameterMap = new HashMap<>();

		JsonObject procResponse = HelperMethods.callApiJson(dcRequest, procParameterMap, HelperMethods.getHeaders(dcRequest), URLConstants.CAMPAIGN_DBP_COUNT_PROC);

		if (HelperMethods.isJsonNotNull(procResponse) && HelperMethods.isJsonNotNull(procResponse.get("records"))
				&& procResponse.get(DBPConstants.FABRIC_OPSTATUS_KEY).getAsInt() == 0) {

			JsonArray procResponseJsonArray = procResponse.get("records").getAsJsonArray();

			for (int i = 0; i < procResponseJsonArray.size(); ++i) {

				JsonObject procResponseJsonObject = procResponseJsonArray.get(i).getAsJsonObject();

				String campaignCountKey = procResponseJsonObject.get("channel").getAsString() + "_" + procResponseJsonObject.get("screen").getAsString();
				String campaignCount = procResponseJsonObject.get("campaign_count").getAsString();

				campaignCounts.put(campaignCountKey, campaignCount);
			}
		}

		return campaignCounts;
	}

	public static String getCurrentDate() {
		DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		return LocalDateTime.now(Clock.systemUTC()).format(dateTimeFormat);
	}
}