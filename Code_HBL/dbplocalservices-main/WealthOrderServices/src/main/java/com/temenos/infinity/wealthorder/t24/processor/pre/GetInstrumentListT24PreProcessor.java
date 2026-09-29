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

/**
 * @author himaja.sridhar
 *
 */
public class GetInstrumentListT24PreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		diagnostic.prepareDebug("==========> GetInstrumentListT24PreProcessor T24 - Entered ").log();
		try {
			String instrumentName = "";
			instrumentName = ("%27" + request.getParameter(TemenosConstants.SEARCHBYINSTRUMENTNAME).toString().trim()
					+ "%27").replace(" ", "%20");
			inputMap.put("instrumentName", instrumentName);
			inputMap.put("paramValue", instrumentName);
			request.addRequestParam_("paramValue", instrumentName);
			diagnostic.prepareDebug("==========> GetInstrumentListT24PreProcessor T24 - Entering into integration ").log();
			return true;

		} catch (Exception e) {
			alert.prepareError("==========> GetInstrumentListT24PreProcessor T24 - Error: " + e.getMessage()).log();
			e.getMessage();
			return false;
		}
	}

}
