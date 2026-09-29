package com.temenos.infinity.api.wealth.preandpostprocessors;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealth.config.PortfolioWealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * (INFO) If status is set as a part of the request , the operation is exited 
 * 		  else operation is executed.
 * @author balaji.krishnan
 *
 */

public class GetUserFavouriteInstrumentsPostProcessor implements DataPostProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		try {
			String instrumentId = result.getParamValueByName("favInstrumentIds").toString();
			if (instrumentId != null && !instrumentId.equals("")) {
				instrumentId = instrumentId.replace(":", " ");
				result.addParam(TemenosConstants.INSTRUMENTID, instrumentId);
			}
			return result;
		} catch (Exception e) {

			alert.prepareError("Error while invoking OrderBlotterPostProcessor - "
					+ PortfolioWealthAPIServices.WEALTH_GETUSERFAVOURITES.getOperationName() + "  : " + e).log();
		}

		return result;
	}
}
