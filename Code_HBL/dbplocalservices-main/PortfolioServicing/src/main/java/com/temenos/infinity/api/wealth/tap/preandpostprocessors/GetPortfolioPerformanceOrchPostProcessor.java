/**
 * 
 */
package com.temenos.infinity.api.wealth.tap.preandpostprocessors;

import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealth.backenddelegate.impl.PortfolioPerformanceBackendDelegateImpl;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealth.util.WealthMockUtil;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;

/**
 * @author himaja.sridhar
 *
 */
public class GetPortfolioPerformanceOrchPostProcessor implements DataPostProcessor2 {
	WealthMockUtil wealthMockUtil = new WealthMockUtil();
	PortfolioPerformanceBackendDelegateImpl portfolioPerformanceBackendDelegateImpl = new PortfolioPerformanceBackendDelegateImpl();
	
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		JSONArray sortedJSON = new JSONArray();
		JSONObject portfolioPerf = new JSONObject();
		JSONArray portfolioPerfArr = new JSONArray();
		JSONArray startArr = new JSONArray();
		JSONArray overviewArr = new JSONArray();
		JSONArray benchMarkList = new JSONArray();
		JSONObject benchMarkObj = new JSONObject();
		String selectedBench = "";
		int totalCount = 0;
		
		String sortBy = request.getParameter(TemenosConstants.SORTBY);
		String sortType = request.getParameter(TemenosConstants.SORTORDER);
		String limitVal = request.getParameter(TemenosConstants.PAGESIZE);
		String offsetVal = request.getParameter(TemenosConstants.PAGEOFFSET);

		
		Dataset overviewSet = result.getDatasetById("monthlyOverview");
		Dataset benchSet = result.getDatasetById("performance");
		Dataset stratSet = result.getDatasetById("strategy");
		
		if(overviewSet != null) {
		overviewArr = ResultToJSON.convertDataset(overviewSet);
		if(benchSet == null)
		{
			for (int i = 0; i < overviewArr.length(); i++) {
				JSONObject overviewObj = overviewArr.getJSONObject(i);
				String dateVal = overviewObj.getString(TemenosConstants.DATE_TIME).replace("-", "");
				overviewObj.put(TemenosConstants.DATE_TIME, dateVal);
				overviewObj.put(TemenosConstants.BENCHMARK,"");
				overviewObj.remove("PERIOD_DISPLAY");
				overviewObj.remove("PERIOD_INITIAL_DATE");
				portfolioPerfArr.put(overviewObj);	
			}
		}
		else {
		JSONArray benchArr = ResultToJSON.convertDataset(benchSet);
		for (int i = 0; i < overviewArr.length(); i++) {
			JSONObject overviewObj = overviewArr.getJSONObject(i);
			String init_d = overviewObj.getString("PERIOD_INITIAL_DATE");
			String final_d = overviewObj.getString(TemenosConstants.DATE_TIME);
			String disp_d = overviewObj.getString("PERIOD_DISPLAY");
			for (int j = 0; j < benchArr.length(); j++) {
				JSONObject bechObj = benchArr.getJSONObject(i);
				String init_b = bechObj.getString("init_d");
				String final_b = bechObj.getString("final_d");
				String disp_b = bechObj.getString("PERIOD_DISPLAY");
				if(init_d.equalsIgnoreCase(init_b) && final_d.equalsIgnoreCase(final_b) && disp_d.equalsIgnoreCase(disp_b)) {
					if(bechObj.has("BENCH_PERFORMANCE")) {
						Double benchPer = Double.parseDouble(bechObj.getString("BENCH_PERFORMANCE"));	
						overviewObj.put(TemenosConstants.BENCHMARK, String.format("%.2f", benchPer));
					}
					else {
						overviewObj.put(TemenosConstants.BENCHMARK,"");
					}
					
				}
				String dateVal = overviewObj.getString(TemenosConstants.DATE_TIME).replace("-", "");
				overviewObj.put(TemenosConstants.DATE_TIME, dateVal);
				overviewObj.remove("PERIOD_DISPLAY");
				overviewObj.remove("PERIOD_INITIAL_DATE");
			}
			portfolioPerfArr.put(overviewObj);
		}
		}
		
		
		int limit = (limitVal != null && limitVal.trim().length() > 0) ? Integer.parseInt(limitVal) : 0;
		int offset = (offsetVal != null && offsetVal.trim().length() > 0) ? Integer.parseInt(offsetVal) : 0;

		if (sortBy != null) {
			sortedJSON = portfolioPerformanceBackendDelegateImpl.sortArray(portfolioPerfArr, sortBy, sortType);
		}
		totalCount = sortedJSON.length();
		if (limit > 0 || offset >= 0) {
			sortedJSON = wealthMockUtil.pagination(sortedJSON, limit, offset);
		}
	

		portfolioPerf.put("monthlyOverview", portfolioPerfArr);
		portfolioPerf.put("sortedMonthlyOverview", sortedJSON);
		
		
		if(stratSet!= null ) {
			startArr = ResultToJSON.convertDataset(stratSet);
			selectedBench = startArr.getJSONObject(0).getString("selectedBenchMark");
			benchMarkObj.put("benchMarkId", startArr.getJSONObject(0).getString("id"));
			benchMarkObj.put("benchMark", selectedBench);
		}
		benchMarkList.put(benchMarkObj);
		portfolioPerf.put("benchMarkList", benchMarkList);
		portfolioPerf.put(TemenosConstants.SORTBY, sortBy);
		portfolioPerf.put(TemenosConstants.SORTORDER, sortType);
		portfolioPerf.put(TemenosConstants.PAGESIZE, limitVal);
		portfolioPerf.put(TemenosConstants.PAGEOFFSET, offsetVal);
		portfolioPerf.put("totalCount", totalCount);
		
		Result performanceRes = Utilities.constructResultFromJSONObject(portfolioPerf);
		performanceRes.addRecord(result.getRecordById("performanceList"));
		performanceRes.addParam("selectedBenchMark",selectedBench);
		performanceRes.addParam("portfolioID", request.getParameter("portfolioId").toString());
		performanceRes.addParam(TemenosConstants.REFERENCECURRENCY, result.getParamValueByName("referenceCurrency"));
		performanceRes.addOpstatusParam("0");
		performanceRes.addHttpStatusCodeParam("200");
		performanceRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		return performanceRes;
		} 
		else {
			JSONObject perfObj = new JSONObject();
			portfolioPerf.put("monthlyOverview", portfolioPerfArr);
			portfolioPerf.put("sortedMonthlyOverview", sortedJSON);
			portfolioPerf.put("performanceList", perfObj);
			benchMarkList.put(benchMarkObj);
			portfolioPerf.put("benchMarkList", benchMarkList);
			portfolioPerf.put(TemenosConstants.SORTBY, sortBy);
			portfolioPerf.put(TemenosConstants.SORTORDER, sortType);
			portfolioPerf.put(TemenosConstants.PAGESIZE, limitVal);
			portfolioPerf.put(TemenosConstants.PAGEOFFSET, offsetVal);
			portfolioPerf.put("totalCount", totalCount);
			Result performanceRes = Utilities.constructResultFromJSONObject(portfolioPerf);
			performanceRes.addParam("selectedBenchMark","");
			performanceRes.addParam("portfolioID", request.getParameter("portfolioId").toString());
			performanceRes.addParam(TemenosConstants.REFERENCECURRENCY, "");
			performanceRes.addOpstatusParam("0");
			performanceRes.addHttpStatusCodeParam("200");
			performanceRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			return performanceRes;
		}
	}

}
