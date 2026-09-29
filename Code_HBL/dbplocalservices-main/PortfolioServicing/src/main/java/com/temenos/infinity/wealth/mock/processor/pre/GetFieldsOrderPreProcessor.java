/**
 * 
 */
package com.temenos.infinity.wealth.mock.processor.pre;

import java.util.HashMap;
import java.util.List;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;
import com.temenos.infinity.wealth.common.util.PortfolioServiceUtils;

/**
 * @author himaja.sridhar
 *
 */
public class GetFieldsOrderPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "rawtypes", "unused" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetFieldsOrderPreProcessor Mock - Entered ").log();
			Object portfolioIdObj = inputMap.get(TemenosConstants.PORTFOLIOID);
			Object userIdObj = inputMap.get(TemenosConstants.USERID);
			String portfolioId = null, userId = null;
			if (userIdObj == null || userIdObj.equals("")) {
				return PortfolioServiceUtils.unauthAccess(result, TemenosConstants.USERID);
			}
			if (portfolioIdObj == null || portfolioIdObj.equals("")) {
				return PortfolioServiceUtils.unauthAccess(result, TemenosConstants.PORTFOLIOID);
			} else {
				if (portfolioIdObj != null) {
					portfolioId = inputMap.get(TemenosConstants.PORTFOLIOID).toString();
				}
				if (userIdObj != null) {
					userId = inputMap.get(TemenosConstants.USERID).toString();
				}
			}
			List<String> allportfoliosList = PortfolioWealthUtils.getAllPortfoliosFromCache(request);

			if (allportfoliosList.contains(portfolioId)) {
				diagnostic.prepareDebug("==========> GetFieldsOrderPreProcessor Mock - Portfolio exists for the customer").log();
				return true;
			} else {
				alert.prepareError("Portfolio ID " + portfolioId + " does not exist for the Customer").log();
				alert.prepareError("Invalid request").log();
				result.addParam("status", "Failure");
				result.addParam("error", "Unauthorized Access");
				return false;
			}
		} catch (Exception e) {
			alert.prepareError("==========> GetFieldsOrderPreProcessor Mock - Error: " + e.getMessage()).log();
			e.getMessage();
			return false;
		}
	}
}
