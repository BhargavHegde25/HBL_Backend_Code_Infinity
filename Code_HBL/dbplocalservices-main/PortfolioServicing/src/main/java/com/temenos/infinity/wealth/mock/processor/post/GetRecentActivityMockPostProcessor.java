package com.temenos.infinity.wealth.mock.processor.post;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealth.config.PortfolioWealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * Mock data construction for GetRecentActivity service
 * 
 * @author muthukumarv
 */
public class GetRecentActivityMockPostProcessor implements DataPostProcessor2 {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetRecentActivityMockPostProcessor Mock - Entered ").log();
			JSONObject responseAck = new JSONObject();
			JSONArray recentActArray = new JSONArray();
			String[] orderType = new String[] { "Buy", "Sell", "Buy", "Buy" };
			String[] quantity = new String[] { "4", "20", "6", "2" };
			String[] description = new String[] { "Alphabet Inc Class A", "iShares Core S&P 500", "LVMH", "Walmart Inc" };
			String[] instrumentId = new String[] { "100044-000", "100015-000", "100051-000", "100014-000" };
			Calendar cal = Calendar.getInstance();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			for (int i = 0; i < description.length; i++) {
				JSONObject recentActObj = new JSONObject();
				recentActObj.put(TemenosConstants.DESCRIPTION, description[i]);
				recentActObj.put(TemenosConstants.INSTRUMENTID, instrumentId[i]);
				recentActObj.put(TemenosConstants.ORDERTYPE, orderType[i]);
				recentActObj.put(TemenosConstants.QUANTITY, quantity[i]);
				cal.add(Calendar.HOUR, -2);
				recentActObj.put(TemenosConstants.TRADEDATE, df.format(cal.getTime()).concat("+05:30"));
				recentActArray.put(recentActObj);
			}
			responseAck.put("recentActivity", recentActArray);
			Result final_result = Utilities.constructResultFromJSONObject(responseAck);
			final_result.addOpstatusParam("0");
			final_result.addHttpStatusCodeParam("200");
			final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			result.appendResult(final_result);
			diagnostic.prepareDebug("==========> GetRecentActivityMockPostProcessor Mock - Exited ").log();
		} catch (Exception e) {
			e.getMessage();
			alert.prepareError("==========> GetRecentActivityMockPostProcessor Mock - Error: " + e.getMessage()).log();
		}
		return result;
	}
}

