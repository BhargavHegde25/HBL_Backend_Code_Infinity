package com.temenos.infinity.api.wealth.mockdata;

import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
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
public class GetPortfolioPerformanceMock implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	@SuppressWarnings("unchecked")
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		WealthMockUtil mockUtil = new WealthMockUtil();
		Map<String, Object> inputMap = new HashMap<>();
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		Object portfolioIdobj = inputParams.get(TemenosConstants.PORTFOLIOID);
		Object dateFromObj = inputParams.get(TemenosConstants.DATEFROM);
		Object dateToObj = inputParams.get(TemenosConstants.DATETO);
		Object benchMarkObj=inputParams.get(TemenosConstants.BENCHMARK);
		Object durationObj=inputParams.get(TemenosConstants.DURATION);
		Object sortByObj = inputParams.get(TemenosConstants.SORTBY);
		Object sortTypeObj = inputParams.get(TemenosConstants.SORTORDER);
		Object limitObj = inputParams.get(TemenosConstants.PAGESIZE);
		Object offsetObj = inputParams.get(TemenosConstants.PAGEOFFSET);
		String dateFrom = null, dateTo = null, portfolioId = null, benchMark=null,duration=null,sortBy=null,sortType=null,
				limit=null,offset=null;
		if (portfolioIdobj != null) {
			portfolioId = inputParams.get(TemenosConstants.PORTFOLIOID).toString();
			inputMap.put(TemenosConstants.PORTFOLIOID, portfolioId);
		}
		if (dateFromObj != null) {
			dateFrom = inputParams.get(TemenosConstants.DATEFROM).toString();
			inputMap.put(TemenosConstants.DATEFROM, dateFrom);
		}
		if (dateToObj != null) {
			dateTo = inputParams.get(TemenosConstants.DATETO).toString();
			inputMap.put(TemenosConstants.DATETO, dateTo);
		}
		if (benchMarkObj != null) {
			benchMark = inputParams.get(TemenosConstants.BENCHMARK).toString();
			inputMap.put(TemenosConstants.BENCHMARK, benchMark);
		}
		if (durationObj != null) {
			duration = inputParams.get(TemenosConstants.DURATION).toString();
			inputMap.put(TemenosConstants.DURATION, duration);
		}
		if (sortByObj != null) {
			sortBy = inputParams.get(TemenosConstants.SORTBY).toString();
			inputMap.put(TemenosConstants.SORTBY, sortBy);
		}
		if (sortTypeObj != null) {
			sortType = inputParams.get(TemenosConstants.SORTORDER).toString();
			inputMap.put(TemenosConstants.SORTORDER, sortType);
		}
		if (limitObj != null) {
			limit = inputParams.get(TemenosConstants.PAGESIZE).toString();
			inputMap.put(TemenosConstants.PAGESIZE, limit);
		}
		if (offsetObj != null) {
			offset = inputParams.get(TemenosConstants.PAGEOFFSET).toString();
			inputMap.put(TemenosConstants.PAGEOFFSET, offset);
		}
		try {
			JSONObject resultJSON = mockUtil.mockPortfolioPerformanceList(inputMap);
			return Utilities.constructResultFromJSONObject(resultJSON);
		}
		catch(Exception e) {
			alert.prepareError("Caught exception at invoke of getPortfolioPerformanceList: ", e).log();
			return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
		}
		
	}

}
