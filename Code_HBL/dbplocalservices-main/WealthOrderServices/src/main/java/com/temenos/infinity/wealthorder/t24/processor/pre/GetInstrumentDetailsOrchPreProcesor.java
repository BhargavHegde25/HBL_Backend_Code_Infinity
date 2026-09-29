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
public class GetInstrumentDetailsOrchPreProcesor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		diagnostic.prepareDebug("==========>  GetInstrumentDetailsOrchPreProcesor TAP - Entered").log();
		try {
			Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
			if (!userPermissions.contains("WEALTH_PRODUCT_DETAILS_INSTRUMENT_VIEW")) {
				result.addParam("opstatus", "1582");
				result.addParam("status", TemenosConstants.FAILURE);
				result.addParam("error", "Logged in user not authorized to perform this action");
				diagnostic.prepareDebug("==========> GetInstrumentDetailsOrchPreProcesor T24 - No User permission").log();
				return false;
			} else {
				diagnostic.prepareDebug("==========> GetInstrumentDetailsOrchPreProcesor T24 - User has permission").log();
				if (inputMap.get(TemenosConstants.RICCODE) != null) {
					String ricCode = inputMap.get(TemenosConstants.RICCODE).toString();
					inputMap.put("instrumentsCode", ricCode);
					inputMap.put(TemenosConstants.RICCODE, ricCode);
					diagnostic.prepareDebug("==========> GetInstrumentDetailsOrchPreProcesor T24 - Entering integration").log();
					return true;
				} else {
					diagnostic.prepareDebug("==========> GetInstrumentDetailsOrchPreProcesor T24 - Riccode empty/null").log();
					return OrderServiceUtils.unauthAccess(result, TemenosConstants.RICCODE);
				}
			}
		} catch (Exception e) {
			alert.prepareError("==========> GetInstrumentDetailsOrchPreProcesor T24 - Error: " + e.getMessage()).log();
			return false;
		}
	}

}
