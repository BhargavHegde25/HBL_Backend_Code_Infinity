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
public class UpdateFieldsOrderPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			diagnostic.prepareDebug("==========> UpdateFieldsOrderPreProcessor Mock - Entered ").log();
			Object portfolioIdObj = inputMap.get(TemenosConstants.PORTFOLIOID);
			Object userIdObj = inputMap.get(TemenosConstants.USERID);
			Object fieldOrderObj = inputMap.get(TemenosConstants.FIELDORDER);
			String portfolioId = null, userId = null, fieldOrder = null;
			if (fieldOrderObj == null || fieldOrderObj.equals("")) {
				return PortfolioServiceUtils.unauthAccess(result, TemenosConstants.FIELDORDER);
			} 
			if (userIdObj == null || userIdObj.equals("")) {
				return PortfolioServiceUtils.unauthAccess(result, TemenosConstants.USERID);
			}
			if (portfolioIdObj == null || portfolioIdObj.equals("")) {
				return PortfolioServiceUtils.unauthAccess(result, TemenosConstants.PORTFOLIOID);
			} 
			else {
				if (portfolioIdObj != null) {
					portfolioId = inputMap.get(TemenosConstants.PORTFOLIOID).toString();
					inputMap.put(TemenosConstants.PORTFOLIOID, portfolioId);
				}
				if (userIdObj != null) {
					userId = inputMap.get(TemenosConstants.USERID).toString();
					inputMap.put(TemenosConstants.USERID, userId);
				}
				if (fieldOrderObj != null) {
					fieldOrder = inputMap.get(TemenosConstants.FIELDORDER).toString();
					inputMap.put(TemenosConstants.FIELDORDER, fieldOrder);
				}
			}
			List<String> allportfoliosList = PortfolioWealthUtils.getAllPortfoliosFromCache(request);

			if (allportfoliosList.contains(portfolioId)) {
				diagnostic.prepareDebug("==========> UpdateFieldsOrderPreProcessor Mock - Portfolio exists for the customer").log();
				return true;
			} else {
				alert.prepareError("Portfolio ID " + portfolioId + " does not exist for the Customer").log();
				alert.prepareError("Invalid request").log();
				result.addParam("status", "Failure");
				result.addParam("error", "Unauthorized Access");
				return false;
			}
		} catch (Exception e) {
			alert.prepareError("==========> UpdateFieldsOrderPreProcessor Mock - Error: " + e.getMessage()).log();
			e.getMessage();
			return false;
		}
	}

}
