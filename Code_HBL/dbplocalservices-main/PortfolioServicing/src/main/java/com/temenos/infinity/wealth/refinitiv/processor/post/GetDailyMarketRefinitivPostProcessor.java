package com.temenos.infinity.wealth.refinitiv.processor.post;



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
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealth.config.PortfolioWealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;

public class GetDailyMarketRefinitivPostProcessor implements DataPostProcessor2 {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetDailyMarketRefinitivPostProcessor Refinitiv - Entered ").log();
			Record body_Record = result.getRecordById("GetSimpleData_Response_2");
			//String str = body_Record.toString();
			JSONObject portObj = ResultToJSON.convertRecord(body_Record);
			String str1 = portObj.toString();
			JSONObject resultJSON = new JSONObject(portObj);
			Result final_result = Utilities.constructResultFromJSONObject(resultJSON);
			final_result.addOpstatusParam("0");
			final_result.addHttpStatusCodeParam("200");
			final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			final_result.addParam("GetSimpleData_Response_2", str1);
			diagnostic.prepareDebug("==========> GetDailyMarketRefinitivPostProcessor Refinitiv - Exited ").log();
			result.appendResult(final_result);

		} catch (Exception e) {
			e.getMessage();
			alert.prepareError("==========> GetDailyMarketRefinitivPostProcessor Refinitiv - Error: " + e.getMessage()).log();
		}
		return result;
	}
}

