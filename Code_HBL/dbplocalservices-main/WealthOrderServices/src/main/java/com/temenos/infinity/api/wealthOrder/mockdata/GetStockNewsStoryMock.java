/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.mockdata;

import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONObject;

import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.ErrorCodeEnum;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthOrder.util.WealthMockUtil;

/**
 * (INFO) Prepares Result object from the mock data returned by the Util.
 * @author himaja.sridhar
 *
 */
public class GetStockNewsStoryMock implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		WealthMockUtil wealthmMockUtil = new WealthMockUtil();
		try {
			String storyid = request.getParameter(TemenosConstants.STORYID);
			Map<String, Object> inputMap = new HashMap<>();
			inputMap.put("StoryId", storyid);
			JSONObject stockNews = wealthmMockUtil.mockGetStockNewsStory(inputMap);
			return Utilities.constructResultFromJSONObject(stockNews);
		} catch (Exception e) {
			alert.prepareError("Caught exception at invoke of getPricingData: ", e).log();
			return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
		}
	}

}
