package com.temenos.infinity.wealth.t24.processor.post;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
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
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;
/**
 *
 * 
 * @author muthukumarv
 *
 */

public class GetRecentActivityT24OrchPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("deprecation")
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetRecentActivityT24OrchPostProcessor T24 - Entered ").log();
			int pageSize = 4;
			SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
			Date currentDate = new Date();
			String bankDate =null;
			JSONObject recentActivityObj = new JSONObject();
			if (null != request.getParameter("bankDate")
					&& request.getParameter("bankDate").toString().trim().length() > 0) {
				bankDate = request.getParameter("bankDate").toString();
				currentDate = sdformat.parse(bankDate);
			}
			String endDate = sdformat.format(currentDate);
			Calendar cal = Calendar.getInstance();
			cal.setTime(currentDate);
			int no_of_days = Integer.parseInt(EnvironmentConfigurationsHandler
					.getValue(TemenosConstants.INF_WLTH_RCNT_ACTY_DAYS, request).toString().trim() == null ? "7"
							: EnvironmentConfigurationsHandler.getValue(TemenosConstants.INF_WLTH_RCNT_ACTY_DAYS, request)
									.toString().trim());
			cal.add(Calendar.DATE, -no_of_days);
			diagnostic.prepareDebug("==========>  GetRecentActivityT24OrchPostProcessor T24 - Environment variables fetched and set").log();
			String startDate = sdformat.format(cal.getTime());
			Dataset ds = result.getDatasetById("transactionList");
			if (ds == null) {
				return result;
			}
			List<Record> drecords = ds.getAllRecords();
			diagnostic.prepareDebug("==========> GetRecentActivityT24OrchPostProcessor T24 - No. of records returned initially: "+ drecords.size()).log();
			if (drecords == null || drecords.size() == 0) {
				JSONArray emptyArr = new JSONArray();
				recentActivityObj.put("recentActivity", emptyArr);
				result = Utilities.constructResultFromJSONObject(recentActivityObj);
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				return result;
			}

			JSONArray recentActArray = new JSONArray();

			for (int i = 0; i < drecords.size(); i++) {
				Record transactionRec = drecords.get(i);
				JSONObject transactionObj = CommonUtils.convertRecordToJSONObject(transactionRec);
				JSONObject recentActObj = new JSONObject();
				recentActObj.put(TemenosConstants.DESCRIPTION, transactionObj.getString(TemenosConstants.DESCRIPTION));
				recentActObj.put(TemenosConstants.INSTRUMENTID, transactionObj.getString(TemenosConstants.INSTRUMENTID));
				recentActObj.put(TemenosConstants.ORDERTYPE, transactionObj.getString(TemenosConstants.TRANSACTIONTYPE));
				recentActObj.put(TemenosConstants.QUANTITY, transactionObj.getString(TemenosConstants.QUANTITY));
				recentActObj.put(TemenosConstants.TRADEDATE, transactionObj.getString(TemenosConstants.TRADEDATE));
				recentActArray.put(recentActObj);
			}
			

			JSONArray sortedRecentActArray = filterDate(recentActArray, startDate, endDate);
			diagnostic.prepareDebug("==========> GetRecentActivityT24OrchPostProcessor T24 - No. of activities returned after filter: " + sortedRecentActArray.length() ).log();
			if (sortedRecentActArray.length() > 0) {
				sortedRecentActArray = getSortedJsonArray(sortedRecentActArray);
				diagnostic.prepareDebug("==========> GetRecentActivityT24OrchPostProcessor T24 - No. of activities returned after sort: " + sortedRecentActArray.length() ).log();
			}
			
			int arrSize = sortedRecentActArray.length();
			if (arrSize > pageSize) {
				for (int i = arrSize - 1; i >= pageSize; i--) {
					sortedRecentActArray.remove(i);
				}
			}
			recentActivityObj.put("recentActivity", sortedRecentActArray);

			Result portfolioRes = Utilities.constructResultFromJSONObject(recentActivityObj);
			portfolioRes.addOpstatusParam("0");
			portfolioRes.addHttpStatusCodeParam("200");
			portfolioRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);

			return portfolioRes;
			
		} catch (Exception e) {
			alert.prepareError("==========> GetRecentActivityT24OrchPostProcessor T24 - Error: " + e.getMessage()).log();
			e.getMessage();
		}
		return result;
	}

	public JSONArray filterDate(JSONArray array, String startDate, String endDate) {
		JSONArray filteredArray = new JSONArray();
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < array.length(); ++i) {
			JSONObject obj = array.getJSONObject(i);
			try {
				Date sdt = sdformat.parse(startDate);
				Date edt = sdformat.parse(endDate);
				Date tdt = sdformat.parse(obj.getString(TemenosConstants.TRADEDATE));
				// if (tdt.after(sdt) && tdt.before(edt)) {
				if (sdt.compareTo(tdt) * tdt.compareTo(edt) >= 0) {
					filteredArray.put(obj);
				}
			} catch (Exception e) {

				return null;
			}
		}
		return filteredArray;
	}

	private JSONArray getSortedJsonArray(JSONArray jsonArr) {

		JSONArray sortedJsonArr = new JSONArray();

		String sortValue = TemenosConstants.TRADEDATE;

		List<JSONObject> jsonValues = new ArrayList<JSONObject>();
		for (int i = 0; i < jsonArr.length(); i++) {
			jsonValues.add(jsonArr.getJSONObject(i));
		}
		Collections.sort(jsonValues, new Comparator<JSONObject>() {

			private final String KEY_NAME = sortValue;

			@Override
			public int compare(JSONObject a, JSONObject b) {
				String str1 = new String();
				String str2 = new String();
				str1 = a.has(KEY_NAME) ? (String) a.get(KEY_NAME) : "";
				str2 = b.has(KEY_NAME) ? (String) b.get(KEY_NAME) : "";
				return str1.compareToIgnoreCase(str2);
			}

		});

		for (int i = jsonArr.length() - 1; i >= 0; i--) {
			sortedJsonArr.put(jsonValues.get(i));
		}

		return sortedJsonArr;
	}
}
