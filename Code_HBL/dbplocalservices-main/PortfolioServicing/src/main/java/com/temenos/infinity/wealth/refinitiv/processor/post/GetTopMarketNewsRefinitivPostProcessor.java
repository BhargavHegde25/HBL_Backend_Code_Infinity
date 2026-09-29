package com.temenos.infinity.wealth.refinitiv.processor.post;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealth.config.PortfolioWealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;

public class GetTopMarketNewsRefinitivPostProcessor implements DataPostProcessor2 {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetTopMarketNewsRefinitivPostProcessor Refinitiv - Entered ").log();
			Record body_Record = result.getRecordById("GetSummaryByTopic_Response_1");
			String limitVal = request.getParameter(TemenosConstants.PAGESIZE).toString();
			String offsetVal = request.getParameter(TemenosConstants.PAGEOFFSET);
			int limit = (limitVal != null && limitVal.trim().length() > 0) ? Integer.parseInt(limitVal) : 0;
			int offset = (offsetVal != null && offsetVal.trim().length() > 0) ? Integer.parseInt(offsetVal):0;
			//String str = body_Record.toString();
			JSONObject portObj = ResultToJSON.convertRecord(body_Record);
			JSONObject jsonPagination = pagination(portObj, limit, offset);
			String str = jsonPagination.toString();
			Result final_result= Utilities.constructResultFromJSONObject(jsonPagination);
			//String str2 = portObj.toString();
			//JSONObject resultJSON = new JSONObject(portObj);
			//Result final_result = Utilities.constructResultFromJSONObject(resultJSON);
			final_result.addOpstatusParam("0");
			final_result.addHttpStatusCodeParam("200");
			final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			final_result.addParam("GetSummaryByTopic_Response_1", str);
			result.appendResult(final_result);
		} catch (Exception e) {
			e.getMessage();
			alert.prepareError("==========> GetTopMarketNewsRefinitivPostProcessor Refinitiv - Error: " + e.getMessage()).log();
		}
		diagnostic.prepareDebug("==========> GetTopMarketNewsRefinitivPostProcessor Refinitiv - Exited ").log();
		return result;
	}
	private JSONObject pagination(JSONObject jsonResult, int limit, int offset) {
		String[] objectVal = new String[] { "StoryMLResponse", "STORYML" };
		JSONObject objJson = jsonResult;
		for (int i = 0; i < objectVal.length; i++) {
			objJson = objJson.getJSONObject(objectVal[i]);
		}
		JSONArray jsonArray = objJson.getJSONArray("HL");
		JSONObject response = new JSONObject();
		JSONObject responseSTORYML = new JSONObject();
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
		response.put("StoryMLResponse", responseSTORYML);
		//response.put("GetSummaryByTopic_Response_1", storyMLResponse);
		response.put("totalCount", totalcount);
		return response;
	}
}

