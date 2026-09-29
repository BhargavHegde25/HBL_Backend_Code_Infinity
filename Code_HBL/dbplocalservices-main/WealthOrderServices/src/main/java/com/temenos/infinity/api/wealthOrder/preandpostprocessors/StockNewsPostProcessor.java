package com.temenos.infinity.api.wealthOrder.preandpostprocessors;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.wealthOrder.backenddelegate.impl.StockNewsWebBackendDelegateImpl;
import com.temenos.infinity.api.wealthOrder.config.WealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 *  (INFO) Builds the Result in the desired format for the Refinitiv service.
 *
 */

public class StockNewsPostProcessor implements DataPostProcessor2 {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		try {
			Dataset ds = result.getDatasetById(TemenosConstants.STORYIDS);
			if (ds != null) {
				String jsonString = ResultToJSON.convertDataset(ds).toString();
				result.addParam("StoryId", jsonString);
			}

		} catch (Exception e) {

			alert.prepareError("Error while invoking StockNewsPostProcessor - "
					+ WealthAPIServices.WEALTHSERVICESORCHESTRATION.getOperationName() + "  : " + e).log();
		}

		return result;
	}
}
