/**
 * 
 */
package com.temenos.infinity.api.wealth.preandpostprocessors;

import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.dbputilities.util.CommonUtils;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Dataset;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealth.util.WealthMockUtil;

/**
 * @author eivanov
 *
 */
public class GetTopMarketNewsPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		WealthMockUtil wealthMockUtil = new WealthMockUtil();		
		String errMsg = result.getParamValueByName("errmsg");
		if (errMsg != null && !errMsg.equals("")) {
			JSONObject topMarketObj = new JSONObject();
			topMarketObj.put("GetSummaryByTopic_Response_1", new JSONObject());
			topMarketObj.put("totalcount", 0);
			Result res = Utilities.constructResultFromJSONObject(topMarketObj);
			res.addOpstatusParam("0");
			res.addHttpStatusCodeParam("200");
			res.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			return res;
		} else {
			int totalCount = 0;
			Record resultRec = result.getRecordById("GetSummaryByTopic_Response_1");
			Record storyMLResponse = resultRec.getRecordById("StoryMLResponse");
			Record storyML = storyMLResponse.getRecordById("STORYML");
			Dataset bodyDataset = storyML.getDatasetById("HL");
			JSONArray arrJSON = CommonUtils.convertDatasetToJSONArray(bodyDataset);

			String limitVal = request.getParameter(TemenosConstants.PAGESIZE);
			String offsetVal = request.getParameter(TemenosConstants.PAGEOFFSET);
			int limit = (limitVal != null && limitVal.trim().length() > 0) ? Integer.parseInt(limitVal) : 0;
			int offset = (offsetVal != null && offsetVal.trim().length() > 0) ? Integer.parseInt(offsetVal) : 0;

			totalCount = arrJSON.length();
			if (limit > 0 && offset >= 0) {
				arrJSON = wealthMockUtil.pagination(arrJSON, limit, offset);
				Dataset bodyDatasetPagination = Utilities.constructDatasetFromJSONArray(arrJSON);
				bodyDatasetPagination.setId("HL");
				storyML.removeDatasetById("HL");
				storyML.addDataset(bodyDatasetPagination);
			}
			result.addIntParam("totalCount", totalCount);
			return result;
		}

	}

}
