package com.temenos.infinity.wealth.mock.processor.post;


import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealth.config.PortfolioWealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;

public class GetDailyMarketMockPostProcessor implements DataPostProcessor2 {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetDailyMarketMockPostProcessor Mock - Entered ").log();
			String markets = request.getParameter(TemenosConstants.MARKETS);
			String marketsArr[] = markets.toUpperCase().trim().split("\\s*,\\s*");
			JSONObject resp = new JSONObject();
			JSONArray itemArray = new JSONArray();
			JSONObject item = new JSONObject();
			JSONArray itemResponseArr = new JSONArray();
			JSONObject itemResponse = new JSONObject();
			JSONObject simpleDataResult = new JSONObject();
			String[] name = null, dataType = null, value = null;
			for (String marketsVal : marketsArr) {
				JSONArray fieldsArr = new JSONArray();
				JSONObject fieldObj = new JSONObject();
				JSONObject fieldsObj = new JSONObject();
				if (marketsVal.equalsIgnoreCase(".DJI")) {
					name = new String[] { "DSPLY_NAME", "CF_NETCHNG", "PCTCHNG", "CF_LAST", "CF_CURRENCY" };
					dataType = new String[] { "Utf8String", "Double", "Double", "Double", "Utf8String" };
					value = new String[] { "DJ INDU AVERG", "-219.75", "-0.75", "29263.48", "USD" };
					for (int i = 0; i < name.length; i++) {
						JSONObject fields = new JSONObject();
						fields.put(TemenosConstants.DATATYPE, dataType[i]);
						fields.put(TemenosConstants.NAME, name[i]);
						fields.put(dataType[i], value[i]);
						fieldsArr.put(fields);
					}
					fieldObj.put("Field", fieldsArr);
					fieldsObj.put("Fields", fieldObj);
					itemArray.put(fieldsObj);
				} else if (marketsVal.equalsIgnoreCase(".SPX")) {
					name = new String[] { "DSPLY_NAME", "CF_NETCHNG", "PCTCHNG", "CF_LAST", "CF_CURRENCY" };
					dataType = new String[] { "Utf8String", "Double", "Double", "Double", "Utf8String" };
					value = new String[] { "S&P 500 INDEX", "-24.33", "-0.679254", "3557.54", "USD" };
					for (int i = 0; i < name.length; i++) {
						JSONObject fields = new JSONObject();
						fields.put(TemenosConstants.DATATYPE, dataType[i]);
						fields.put(TemenosConstants.NAME, name[i]);
						fields.put(dataType[i], value[i]);
						fieldsArr.put(fields);
					}
					fieldObj.put("Field", fieldsArr);
					fieldsObj.put("Fields", fieldObj);
					itemArray.put(fieldsObj);
				} else if (marketsVal.equalsIgnoreCase(".IXIC")) {
					name = new String[] { "DSPLY_NAME", "CF_NETCHNG", "PCTCHNG", "CF_LAST", "CF_CURRENCY" };
					dataType = new String[] { "Utf8String", "Double", "Double", "Double", "Utf8String" };
					value = new String[] { "NASDAQ COMPOSITE", "0.0", "0.0", "11854.97", "USD" };
					for (int i = 0; i < name.length; i++) {
						JSONObject fields = new JSONObject();
						fields.put(TemenosConstants.DATATYPE, dataType[i]);
						fields.put(TemenosConstants.NAME, name[i]);
						fields.put(dataType[i], value[i]);
						fieldsArr.put(fields);
					}
					fieldObj.put("Field", fieldsArr);
					fieldsObj.put("Fields", fieldObj);
					itemArray.put(fieldsObj);
				}
			}
			diagnostic.prepareDebug("==========> GetDailyMarketMockPostProcessor Mock -  No. of records returned: "+itemArray.length()).log();
			item.put("Item", itemArray);
			itemResponseArr.put(item);
			itemResponse.put("ItemResponse", itemResponseArr);
			simpleDataResult.put("SimpleDataResult", itemResponse);
			//resp.put("GetSimpleData_Response_2", simpleDataResult);
			//resp.put("opstatus", "0");
			//resp.put("httpStatusCode", "200");
			String str = simpleDataResult.toString();
			Result final_result = Utilities.constructResultFromJSONObject(resp);
			final_result.addOpstatusParam("0");
			final_result.addHttpStatusCodeParam("200");
			final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			final_result.addParam("GetSimpleData_Response_2", str);
			diagnostic.prepareDebug("==========> GetDailyMarketMockPostProcessor Mock - Exited ").log();
			result.appendResult(final_result);

		} catch (Exception e) {
			e.getMessage();
			alert.prepareError("==========> GetDailyMarketMockPostProcessor Mock - Error: " + e.getMessage()).log();	
		}
		return result;
	}
}
