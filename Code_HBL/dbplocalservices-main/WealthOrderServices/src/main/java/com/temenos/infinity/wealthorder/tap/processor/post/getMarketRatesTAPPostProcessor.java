package com.temenos.infinity.wealthorder.tap.processor.post;




import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthOrder.config.WealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

public class getMarketRatesTAPPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			diagnostic.prepareDebug("==========> getMarketRatesTAPPostProcessor TAP -  Entered ").log();
			Dataset bodySet=result.getDatasetById("body");
			String exchangeRate = bodySet.getRecord(0).getParamValueByName("fxClientRateN");
			result.addParam("marketRate", exchangeRate);
			result.clearRecords();
			result.clearDatasets();
			result.addOpstatusParam("0");
			result.addHttpStatusCodeParam("200");
			result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			diagnostic.prepareDebug("==========> getMarketRatesTAPPostProcessor TAP -  Exiting with success ").log();
			return result;
			
		}
		 catch (Exception e) {
			 alert.prepareError("==========> getMarketRatesTAPPostProcessor TAP - Error: " + e.getMessage()).log();
			}
		return result;
	}
}


