package com.temenos.infinity.wealthorder.mock.processor.pre;

import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.wealthorder.common.util.OrderServiceUtils;

public class GetWatchlistDBPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("rawtypes")
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		diagnostic.prepareDebug("==========> GetWatchlistDBPreProcessor Mock - Entered ").log();
		try {
			String userId = HelperMethods.getUserIdFromSession(request);
			request.addRequestParam_(TemenosConstants.USER_ID, userId);

			if (userId == null || userId.equals("")) {
				diagnostic.prepareDebug("==========> GetWatchlistDBPreProcessor Mock - Invalid request ").log();
				return OrderServiceUtils.unauthAccess(result, TemenosConstants.USER_ID);
			} else {
				diagnostic.prepareDebug("==========> GetWatchlistDBPreProcessor Mock - Entering into mock integration ").log();
				return true;
			}

		} catch (Exception e) {
			alert.prepareError("==========> GetWatchlistDBPreProcessor Mock - Error: " + e.getMessage()).log();
			e.getMessage();
			return false;
		}

	}
}
