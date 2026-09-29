/**
 * 
 */
package com.temenos.infinity.api.wealth.preandpostprocessors;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.dbputilities.util.CommonUtils;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * @author himaja.sridhar
 *
 */
public class GetPortofolioListPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		JSONObject portfolioJSON = new JSONObject();
		JSONArray portfolioArr = new JSONArray();
		JSONObject portfolioListObj = new JSONObject();

		Record headerRec = result.getRecordById("header");
		String statusVal = headerRec.getParamValueByName("status");
		if (statusVal == null || !statusVal.equals("success")) {
			JSONObject response1 = new JSONObject();
			Record errorRec = result.getRecordById("error");
			String code = errorRec.getParamValueByName("code");
			if (code == null || code.equalsIgnoreCase("TGVCP-007")) {
				portfolioListObj.put("portfolioList", portfolioArr);
				portfolioJSON.put("PortfolioList", portfolioListObj);
				Result portfolioRes = Utilities.constructResultFromJSONObject(portfolioJSON);
				portfolioRes.addOpstatusParam("0");
				portfolioRes.addHttpStatusCodeParam("200");
				portfolioRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				return portfolioRes;
			} else {
				response1.put("errormessage", errorRec.getParamValueByName("message"));
				Result errorRes = Utilities.constructResultFromJSONObject(response1);
				return errorRes;
			}
		}

		Dataset bodyDataset = result.getDatasetById("body");
		if (bodyDataset == null) {
			portfolioListObj.put("portfolioList", portfolioArr);
			portfolioJSON.put("PortfolioList", portfolioListObj);
			Result portfolioRes = Utilities.constructResultFromJSONObject(portfolioJSON);
			portfolioRes.addOpstatusParam("0");
			portfolioRes.addHttpStatusCodeParam("200");
			portfolioRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			return portfolioRes;
		}
		List<Record> drecords = bodyDataset.getAllRecords();
		JSONArray bodyArray = new JSONArray();
		int index = 0;
		for (int j = 0; j < drecords.size(); j++) {
			JSONObject portfolioObj = new JSONObject();
			Record drecord = drecords.get(j);
			portfolioObj = CommonUtils.convertRecordToJSONObject(drecord);
			if (portfolioObj.has(TemenosConstants.PORTFOLIOID)) {
				bodyArray.put(portfolioObj);
			} else if (portfolioObj.has(TemenosConstants.JOINTHOLDERID)) {
				index = bodyArray.length() - 1;
				JSONObject lastPortfolio = bodyArray.getJSONObject(index);
				String jointId = lastPortfolio.getString(TemenosConstants.JOINTHOLDERID);
				String jointName = lastPortfolio.getString(TemenosConstants.JOINTHOLDERS);
				if (jointId.equals("")) {
					jointId = portfolioObj.getString(TemenosConstants.JOINTHOLDERID);
					jointName = portfolioObj.getString(TemenosConstants.JOINTHOLDERS);
				} else {
					jointId = jointId + "," + portfolioObj.getString(TemenosConstants.JOINTHOLDERID);
					jointName = jointName + "," + portfolioObj.getString(TemenosConstants.JOINTHOLDERS);
				}
				lastPortfolio.put(TemenosConstants.JOINTHOLDERID, jointId);
				lastPortfolio.put(TemenosConstants.JOINTHOLDERS, jointName);
			}

		}

		String[] colArray = new String[] { TemenosConstants.PORTFOLIOID, TemenosConstants.CUSTOMERID,
				TemenosConstants.REFERENCECURRENCY, TemenosConstants.ACCOUNTNAME, TemenosConstants.PRIMARYHOLDER,
				TemenosConstants.JOINTHOLDERS, TemenosConstants.MARKETVALUE, TemenosConstants.INVESTMENTTYPE,
				TemenosConstants.JOINTHOLDERID, TemenosConstants.UNREALIZEDPLPERCENTAGE,
				TemenosConstants.UNREALIZEDPLAMOUNT };

		for (int i = 0; i < bodyArray.length(); i++) {
			JSONObject portfolioObj = bodyArray.getJSONObject(i);
			for (String key : colArray) {
				if (portfolioObj.has(key)) {
					portfolioObj.put(key, portfolioObj.get(key));
					if (key.equalsIgnoreCase(TemenosConstants.PORTFOLIOID)) {
						portfolioObj.put(TemenosConstants.ACCOUNTID, portfolioObj.get(key));
					}
					if (key.equalsIgnoreCase(TemenosConstants.UNREALIZEDPLPERCENTAGE)) {
						double uPLPercentage = portfolioObj.get(key) != null
								? Double.parseDouble(portfolioObj.get(key).toString())
								: 0.00f;
						uPLPercentage = Math.abs(uPLPercentage);
						String unRealizedPLPercentage = String.format("%.2f", uPLPercentage);
						portfolioObj.put(key, unRealizedPLPercentage);
					}
					if (key.equalsIgnoreCase(TemenosConstants.UNREALIZEDPLAMOUNT)) {
						double uPLAmount = portfolioObj.get(key) != null
								? Double.parseDouble(portfolioObj.get(key).toString())
								: 0.00f;
						String unRealizedPL = uPLAmount >= 0 ? "P" : "L";
						portfolioObj.put(TemenosConstants.UNREALIZEDPL, unRealizedPL);
						uPLAmount = Math.abs(uPLAmount);
						String unRealizedPLAmount = String.format("%.2f", uPLAmount);
						portfolioObj.put(key, unRealizedPLAmount);
					}
					if (key.equalsIgnoreCase(TemenosConstants.REFERENCECURRENCY)) {
						portfolioObj.put("portfolioCCY", portfolioObj.get(key));
					}

				} else {
					portfolioObj.put(key, "");
				}
			}
			portfolioArr.put(portfolioObj);
		}

		portfolioListObj.put("portfolioList", portfolioArr);
		portfolioJSON.put("PortfolioList", portfolioListObj);
		Result portfolioRes = Utilities.constructResultFromJSONObject(portfolioJSON);
		portfolioRes.addOpstatusParam("0");
		portfolioRes.addHttpStatusCodeParam("200");
		portfolioRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		portfolioRes.addParam(TemenosConstants.ISTAPINTEGRATION, "false");

		return portfolioRes;
	}

}
