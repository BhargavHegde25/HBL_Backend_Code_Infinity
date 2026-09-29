/**
 * 
 */
package com.temenos.infinity.wealthorder.tap.processor.pre;

import java.util.HashMap;
import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * (INFO) Prepares the input for the TAP service in the desired format.
 * 
 * @author himaja.sridhar
 *
 */
public class GetSynonymsTAPOrchPreProcessor implements DataPreProcessor2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		diagnostic.prepareDebug("==========>  GetSynonymsTAPOrchPreProcessor TAP - Entered").log();
		inputMap.put("loop_count", request.getParameter("idCnt"));
		diagnostic.prepareDebug("==========>  GetSynonymsTAPOrchPreProcessor TAP - Exiting with loopcount"
				+ request.getParameter("idCnt")).log();
		return true;
	}

}
