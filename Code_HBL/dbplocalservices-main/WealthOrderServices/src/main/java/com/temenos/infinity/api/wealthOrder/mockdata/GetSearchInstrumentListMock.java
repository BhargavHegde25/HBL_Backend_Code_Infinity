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
 *
 */
public class GetSearchInstrumentListMock implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("unchecked")
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {

		WealthMockUtil mockUtil = new WealthMockUtil();
		Map<String, Object> inputMap = new HashMap<>();
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		Object searchObj = inputParams.get(TemenosConstants.SEARCHBYINSTRUMENTNAME);
		Object sortByObj = inputParams.get(TemenosConstants.SORTBY);
		Object portfolioIdobj = inputParams.get(TemenosConstants.PORTFOLIOID);
		String search = null;
		String sortBy = null;
		String portfolioId = null;
		if (portfolioIdobj != null) {
			portfolioId = inputParams.get(TemenosConstants.PORTFOLIOID).toString();
			inputMap.put(TemenosConstants.PORTFOLIOID, portfolioId);
		}
		if (sortByObj != null) {
			sortBy = inputParams.get(TemenosConstants.SORTBY).toString();
			inputMap.put(TemenosConstants.SORTBY, sortBy);
		}
		if (searchObj != null) {
			search = inputParams.get(TemenosConstants.SEARCHBYINSTRUMENTNAME).toString();
			inputMap.put(TemenosConstants.SEARCHBYINSTRUMENTNAME, search);
		}
		try {
			JSONObject cashBalance = mockUtil.mockGetSearchList(inputMap);
			return Utilities.constructResultFromJSONObject(cashBalance);
		} catch (Exception e) {
			alert.prepareError("Caught exception at invoke of GetAssetAllocationMock: ", e).log();
			return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
		}
	}

}
