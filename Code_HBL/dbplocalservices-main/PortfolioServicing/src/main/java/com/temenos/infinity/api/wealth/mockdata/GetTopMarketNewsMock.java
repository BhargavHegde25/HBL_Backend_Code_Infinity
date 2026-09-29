package com.temenos.infinity.api.wealth.mockdata;

import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.ErrorCodeEnum;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealth.util.WealthMockUtil;

/**
 * (INFO) Prepares Result object from the mock data returned by the Util.
 *
 */
public class GetTopMarketNewsMock implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("unchecked")
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		WealthMockUtil mockUtil = new WealthMockUtil();

		Map<String, Object> inputMap = new HashMap<>();
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		Object topicObj = inputParams.get(TemenosConstants.TOPIC);
		Object limitObj = inputParams.get(TemenosConstants.PAGESIZE);
		Object offsetObj = inputParams.get(TemenosConstants.PAGEOFFSET);
		String topic = null, limitVal = null, offsetVal = null;
		String maxCount = "10";

		if (topicObj != null) {
			topic = inputParams.get(TemenosConstants.TOPIC).toString();
			inputMap.put(TemenosConstants.TOPIC, topic);
		}

		if (limitObj != null) {
			limitVal = inputParams.get(TemenosConstants.PAGESIZE).toString();
			inputMap.put(TemenosConstants.PAGESIZE, limitVal);
		}
		if (offsetObj != null) {
			offsetVal = inputParams.get(TemenosConstants.PAGEOFFSET).toString();
			inputMap.put(TemenosConstants.PAGEOFFSET, offsetVal);
		}
		if (limitObj != null && offsetObj != null) {
			maxCount = "100";
		}
		
		inputMap.put(TemenosConstants.MAXCOUNT, maxCount);
		inputMap.put("ReturnPrivateNetworkURL", "false");
		int limit = (limitVal != null && limitVal.trim().length() > 0) ? Integer.parseInt(limitVal) : 0;
		int offset = (offsetVal != null && offsetVal.trim().length() > 0) ? Integer.parseInt(offsetVal) : 0;

		try {

			JSONObject jsonResult = mockUtil.mockGetTopMarketNews(inputMap);
			if (limit > 0 && offset >= 0) {
				JSONObject jsonPagination = pagination(jsonResult, limit, offset);
				return Utilities.constructResultFromJSONObject(jsonPagination);
			} else {
				return Utilities.constructResultFromJSONObject(jsonResult);
			}
		} catch (Exception e) {
			alert.prepareError("Caught exception at invoke of assetList: ", e).log();
			return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
		}
	}

	private JSONObject pagination(JSONObject jsonResult, int limit, int offset) {
		String[] objectVal = new String[] { "GetSummaryByTopic_Response_1", "StoryMLResponse", "STORYML" };
		JSONObject objJson = jsonResult;
		for (int i = 0; i < objectVal.length; i++) {
			objJson = objJson.getJSONObject(objectVal[i]);
		}
		JSONArray jsonArray = objJson.getJSONArray("HL");
		JSONObject response = new JSONObject();
		JSONObject responseSTORYML = new JSONObject();
		JSONObject storyMLResponse = new JSONObject();
		JSONObject responseHL = new JSONObject();
		JSONArray paginationJSON = new JSONArray();

		int j = 0;
		for (int i = offset; i < jsonArray.length(); i++) {
			if (j == limit) {
				break;
			} else {
				paginationJSON.put(jsonArray.get(i));
			}
			j++;
		}
		int totalcount = jsonArray.length();
		responseHL.put("HL", paginationJSON);
		responseSTORYML.put("STORYML", responseHL);
		storyMLResponse.put("StoryMLResponse", responseSTORYML);
		response.put("GetSummaryByTopic_Response_1", storyMLResponse);
		response.put("httpStatusCode", "200");
		response.put("totalCount", totalcount);
		return response;
	}

}
