package com.temenos.infinity.wealthorder.t24.processor.post;





import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
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

public class GetNoDataReturnedPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			JSONObject responsemain = new JSONObject();
			Result portfolioRes = Utilities.constructResultFromJSONObject(responsemain);
			portfolioRes.addOpstatusParam("0");
			portfolioRes.addHttpStatusCodeParam("200");
			portfolioRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			return portfolioRes;
			
		}
		 catch (Exception e) {
			 alert.prepareError("Error while invoking GetHistoricalDataPostProcessor - "
						+ WealthAPIServices.WEALTHSERVICESORCHESTRATION.getOperationName() + "  : " + e).log();
			}
		return result;
	}
}


