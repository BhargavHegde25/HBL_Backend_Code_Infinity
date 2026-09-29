/**
 * 
 */
package com.temenos.infinity.wealthorder.t24.processor.pre;

import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

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
public class GetNewsStoryOrchPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("rawtypes")
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		diagnostic.prepareDebug("==========> GetNewsStoryOrchPreProcessor T24 - Entered ").log();
		try {
			Object storyIdObj = inputMap.get(TemenosConstants.STORYID);
			if (storyIdObj != null) {
				diagnostic.prepareDebug("==========> GetNewsStoryOrchPreProcessor T24 - Entering into integration ").log();
				return true;
			} else {
				diagnostic.prepareDebug("==========> GetNewsStoryOrchPreProcessor T24 - Exiting with storyid empty/null ").log();
				return OrderServiceUtils.unauthAccess(result, TemenosConstants.STORYID);
			}
		} catch (Exception e) {
			alert.prepareError("==========> GetNewsStoryOrchPreProcessor T24 - Error: " + e.getMessage()).log();
			return false;
		}
	}

}
