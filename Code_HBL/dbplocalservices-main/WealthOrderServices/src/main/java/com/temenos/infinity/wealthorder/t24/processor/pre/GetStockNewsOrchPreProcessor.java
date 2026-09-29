/**
 * 
 */
package com.temenos.infinity.wealthorder.t24.processor.pre;

import java.util.HashMap;
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
import com.temenos.infinity.wealthorder.common.util.OrderServiceUtils;

/**
 * @author himaja.sridhar
 *
 */
public class GetStockNewsOrchPreProcessor implements DataPreProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		diagnostic.prepareDebug("==========> GetStockNewsOrchPreProcessor T24 - Entered ").log();
		try {
			Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
			if (!userPermissions.contains("WEALTH_NEWS_AND_DOCUMENTS_STOCK_NEWS_VIEW")) {
				result.addParam("opstatus", "1582");
				result.addParam("status", TemenosConstants.FAILURE);
				result.addParam("error", "Logged in user not authorized to perform this action");
				diagnostic.prepareDebug("==========> GetStockNewsOrchPreProcessor T24 - No User permission ").log();
				return false;
			} else {
				diagnostic.prepareDebug("==========> GetStockNewsOrchPreProcessor T24 - User has permission ").log();
				Object pageSizeObj = inputMap.get(TemenosConstants.PAGESIZE);
				Object offsetObj = inputMap.get(TemenosConstants.PAGEOFFSET);
				String ricCode = null;
				String pageSizeVal = null;
				String offsetVal = null;
				if (inputMap.get(TemenosConstants.RICCODE) != null) {
					ricCode = inputMap.get(TemenosConstants.RICCODE).toString();
					inputMap.put("instrumentCode", ricCode);
					request.addRequestParam_("instrumentCode", ricCode);
					inputMap.put("languageCode", "en");

					if (pageSizeObj != null) {
						pageSizeVal = inputMap.get(TemenosConstants.PAGESIZE).toString();
					} else {
						pageSizeVal = "0";
					}
					inputMap.put(TemenosConstants.PAGESIZE, pageSizeVal);
					request.addRequestParam_(TemenosConstants.PAGESIZE, pageSizeVal);

					if (offsetObj != null) {
						offsetVal = inputMap.get(TemenosConstants.PAGEOFFSET).toString();
					} else {
						offsetVal = "0";
					}
					inputMap.put(TemenosConstants.PAGEOFFSET, offsetVal);
					request.addRequestParam_(TemenosConstants.PAGEOFFSET, offsetVal);
					diagnostic.prepareDebug("==========> GetStockNewsOrchPreProcessor T24 - Entering into integration").log();

				} else {
					diagnostic.prepareDebug("==========> GetStockNewsOrchPreProcessor T24 - Exiting with riccode null ").log();
					return OrderServiceUtils.unauthAccess(result, TemenosConstants.RICCODE);
				}
				return true;
			}
		} catch (Exception e) {
			alert.prepareError("==========> GetStockNewsOrchPreProcessor T24 - Error: " + e.getMessage()).log();
			return false;
		}
	}

}
