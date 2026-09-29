/**
 * 
 */
package com.temenos.infinity.wealthorder.t24.processor.pre;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.sessionmanager.SessionScope;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;
import com.temenos.infinity.wealthorder.common.util.OrderServiceUtils;

/**
 * @author muthukumarv
 *
 */
public class CancelOrderOrchPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			diagnostic.prepareDebug("==========>  CancelOrderOrchPreProcessor T24 - Entered").log();
			Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
			if (!userPermissions.contains("WEALTH_ORDER_MGMT_ORDER_CANCEL")) {
				result.addParam("opstatus", "1582");
				result.addParam("status", TemenosConstants.FAILURE);
				result.addParam("error", "Logged in user not authorized to perform this action");
				diagnostic.prepareDebug("==========> CancelOrderOrchPreProcessor T24 - No User permission").log();
				return false;
			} else {
				diagnostic.prepareDebug("==========> CancelOrderOrchPreProcessor T24 - User has permission ").log();
				String portfolioId = null, assetType = null;

				if (inputMap.get(TemenosConstants.PORTFOLIOID) != null
						&& inputMap.get(TemenosConstants.PORTFOLIOID).toString().trim().length() > 0) {
					portfolioId = inputMap.get(TemenosConstants.PORTFOLIOID).toString();
					// input.put(TemenosConstants.PORTFOLIOID, portfolioId);
				} else {
					return OrderServiceUtils.validateMandatoryFields(result, TemenosConstants.PORTFOLIOID);
				}

				if (inputMap.get(TemenosConstants.ORDER_ID) != null
						&& inputMap.get(TemenosConstants.ORDER_ID).toString().trim().length() > 0) {
					// orderID = inputMap.get(TemenosConstants.ORDER_ID).toString();
					// input.put(TemenosConstants.ORDER_ID, orderID);
				} else {
					return OrderServiceUtils.validateMandatoryFields(result, TemenosConstants.ORDER_ID);
				}

				if (inputMap.get(TemenosConstants.ASSETTYPE) != null) {
					assetType = inputMap.get(TemenosConstants.ASSETTYPE).toString();
					// inputMap.put(TemenosConstants.ASSETTYPE, assetType);
				}
				inputMap.put("cancelormodify_order", true);
				request.addRequestParam_("cancelormodify_order", "true");
				List<String> allportfoliosList = PortfolioWealthUtils.getAllPortfoliosFromCache(request);

				if (allportfoliosList.contains(portfolioId)) {
				} else {
					alert.prepareError("Portfolio ID " + portfolioId + " does not exist for the Customer").log();
					alert.prepareError("Invalid request").log();
					result.addParam("status", "Failure");
					result.addParam("error", "Unauthorized Access");
					diagnostic.prepareDebug("==========>  CancelOrderOrchPreProcessor T24 - Error:: Unauthorized Access").log();
					return false;

				}
				diagnostic.prepareDebug("==========>  CancelOrderOrchPreProcessor T24 - Exited").log();
				return true;
			}

		} catch (Exception e) {
			alert.prepareError("==========> CancelOrderOrchPreProcessor T24 - Error: " + e.getMessage()).log();
			return false;
		}
	}

}
