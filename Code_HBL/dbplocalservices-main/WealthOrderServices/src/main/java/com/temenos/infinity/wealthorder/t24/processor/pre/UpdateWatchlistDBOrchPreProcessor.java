package com.temenos.infinity.wealthorder.t24.processor.pre;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.sessionmanager.SessionScope;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthOrder.constants.WealthFeatureAction;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;
import com.temenos.infinity.wealthorder.common.util.OrderServiceUtils;

public class UpdateWatchlistDBOrchPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		// TODO Auto-generated method stub
		diagnostic.prepareDebug("==========> UpdateWatchlistDBOrchPreProcessor T24 - Entered ").log();
		try {
			Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
			if (!userPermissions.contains(WealthFeatureAction.WEALTH_WATCHLIST_INSTRUMENT_CREATE)) {
				result.addParam("opstatus", "1582");
				result.addParam("status", TemenosConstants.FAILURE);
				result.addParam("error", "Logged in user not authorized to perform this action");
				diagnostic.prepareDebug("==========> UpdateWatchlistDBOrchPreProcessor T24 - No user permission ").log();
				return false;
			}
			diagnostic.prepareDebug("==========> UpdateWatchlistDBOrchPreProcessor T24 - User has permission ").log();
			return true;
		} catch (Exception e) {
			alert.prepareError("==========> UpdateWatchlistDBOrchPreProcessor T24 - Error: " + e.getMessage()).log();
			e.getMessage();
			return false;
		}

	}

}
