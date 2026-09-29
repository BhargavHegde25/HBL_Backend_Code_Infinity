/**
 * 
 */
package com.temenos.infinity.wealth.t24.processor.post;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;
import com.temenos.infinity.wealth.common.util.PortfolioServiceUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;


/**
 * @author himaja.sridhar
 *
 */
public class GetAccountActivityOrchPostProcessor implements DataPostProcessor2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@SuppressWarnings("unused")
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		diagnostic.prepareDebug("==========> GetAccountActivityOrchPostProcessor T24 - Entered ").log();
		Boolean isTAP  = (EnvironmentConfigurationsHandler.getValue(TemenosConstants.INF_WLTH_CORE,
				request).contains("TAP") ? true : false);
		String portfolioId = (String) request.getParameter(TemenosConstants.PORTFOLIOID);
		String accountId = (String) request.getParameter(TemenosConstants.ACCID);
		String listType = (String) request.getParameter(TemenosConstants.LISTTYPE);
		String dateFrom = (String) request.getParameter(TemenosConstants.DATEFROM);
		String dateTo = (String) request.getParameter(TemenosConstants.DATETO);
		String sortBy = (String) request.getParameter(TemenosConstants.SORTBY);
		String sortType = (String) request.getParameter(TemenosConstants.SORTORDER);
		String searchVal = (String) request.getParameter(TemenosConstants.SEARCHBYINSTRUMENTNAME);
		String limitVal = (String) request.getParameter(TemenosConstants.PAGESIZE);
		String offsetVal = (String) request.getParameter(TemenosConstants.PAGEOFFSET);
		
		int limit = (limitVal != null && limitVal.trim().length() > 0) ? Integer.parseInt(limitVal) : 0;
		int offset = (offsetVal != null && offsetVal.trim().length() > 0) ? Integer.parseInt(offsetVal) : 0;
		
		int totalCount = 0;
		JSONObject recordObject = new JSONObject();
		JSONArray accListArr = new JSONArray();
		JSONObject responseVal = new JSONObject();
		JSONObject status = new JSONObject();
		JSONObject accountActivity = new JSONObject();
		String search =  (searchVal != null && searchVal.trim().length() > 0) ? searchVal : "";
		if (result.getAllDatasets().size()>0 ) {
			diagnostic.prepareDebug("==========> GetAccountActivityOrchPostProcessor T24 - No. of records returned initially: "+ result.getAllDatasets().size()).log();
			List<Dataset> dataset = result.getAllDatasets();
			List<Record> drecords = dataset.get(0).getAllRecords();
			for (int j = 0; j < drecords.size(); j++) {
				JSONObject actListObj = new JSONObject();
				Record drecord = drecords.get(j);
				actListObj = CommonUtils.convertRecordToJSONObject(drecord);
				if (actListObj.get(TemenosConstants.BOOKINGDATE) != null
						&& !actListObj.get(TemenosConstants.BOOKINGDATE).equals("")) {
					actListObj.put(TemenosConstants.BOOKINGDATE,
							actListObj.get(TemenosConstants.BOOKINGDATE).toString().replace("-", ""));
					if (actListObj.get(TemenosConstants.VALUEDATE) != null
							&& !actListObj.get(TemenosConstants.VALUEDATE).equals("")) {
						actListObj.put(TemenosConstants.VALUEDATE,
								actListObj.get(TemenosConstants.VALUEDATE).toString().replace("-", ""));
					}
					accListArr.put(actListObj);
				}
			}
		}else {
			List<Record> records = result.getAllRecords();
			List<Dataset> dataset;
			diagnostic.prepareDebug("==========> GetAccountActivityOrchPostProcessor T24 - No. of records returned initially: "+ records.size()).log();
			for (int i = 0; i < records.size(); i++) {
				Record record = records.get(i);
				recordObject = CommonUtils.convertRecordToJSONObject(record);
				dataset = record.getAllDatasets();
				List<Record> drecords = dataset.get(0).getAllRecords();
				for (int j = 0; j < drecords.size(); j++) {
					JSONObject actListObj = new JSONObject();
					Record drecord = drecords.get(j);
					actListObj = CommonUtils.convertRecordToJSONObject(drecord);
					if (actListObj.get(TemenosConstants.BOOKINGDATE) != null
							&& !actListObj.get(TemenosConstants.BOOKINGDATE).equals("")) {
						actListObj.put(TemenosConstants.BOOKINGDATE,
								actListObj.get(TemenosConstants.BOOKINGDATE).toString().replace("-", ""));
						if (actListObj.get(TemenosConstants.VALUEDATE) != null
								&& !actListObj.get(TemenosConstants.VALUEDATE).equals("")) {
							actListObj.put(TemenosConstants.VALUEDATE,
									actListObj.get(TemenosConstants.VALUEDATE).toString().replace("-", ""));
						}
						accListArr.put(actListObj);
					}
				}
			}
		}
		JSONArray sortedArray = new JSONArray();
		sortedArray = accListArr;
		diagnostic.prepareDebug("==========> GetAccountActivityOrchPostProcessor T24 - No. of records returned for the account: "+ sortedArray.length()).log();
		if (sortBy != null && isTAP ==false) {
			sortedArray = PortfolioServiceUtils.sortAccountActivityArray(sortedArray, sortBy, sortType);
			diagnostic.prepareDebug("==========> GetAccountActivityOrchPostProcessor T24 - No. of records returned for the account after sort: " + sortedArray.length() ).log();
		} else {
		}
		if (search.equals("")) {
			diagnostic.prepareDebug("==========> GetAccountActivityOrchPostProcessor T24 - search not implemented").log();

		} else {
			sortedArray = PortfolioServiceUtils.returnAccountActivitySearch(sortedArray, search);
			diagnostic.prepareDebug("==========> GetAccountActivityOrchPostProcessor T24 - No. of records returned for the account after search: " + sortedArray.length() ).log();
		}
		
		totalCount = sortedArray.length();
		
		if (limit > 0 && offset >= 0) {
			sortedArray = PortfolioWealthUtils.pagination(sortedArray, limit, offset);
			diagnostic.prepareDebug("==========> GetAccountActivityOrchPostProcessor T24 - No. of records returned for the account after pagination: " + sortedArray.length() ).log();
		}
		
		status.put(TemenosConstants.STATUS, "success");
		responseVal.put("body", sortedArray);
		responseVal.put("header", status);
		accountActivity.put("accountActivityList", responseVal);
		accountActivity.put("totalCount", totalCount);
		String accountActStr = responseVal.toString();
		Result res = new Result();
		//res = Utilities.constructResultFromJSONObject(accountActStr);
		res.addOpstatusParam("0");
		res.addHttpStatusCodeParam("200");
		res.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		res.addParam("accountActivityList",accountActStr);
		res.addIntParam("totalCount", totalCount);
		return res;
	}

}
