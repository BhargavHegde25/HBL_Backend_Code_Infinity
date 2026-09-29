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
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * @author himaja.sridhar
 *
 */
public class GetInstrumentListTAPPreProcessor implements DataPreProcessor2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		diagnostic.prepareDebug("==========>  GetInstrumentListTAPPreProcessor TAP - Entered").log();
		String search = request.getParameter(TemenosConstants.SEARCHBYINSTRUMENTNAME).toString().trim();
		search =search.replace(" ", "%20");
		search = ("*").concat(search.concat("*"));
		inputMap.put(TemenosConstants.INSTRUMENTNAME, search);
		diagnostic.prepareDebug("==========>  GetInstrumentListTAPPreProcessor TAP - Exited").log();
		return true;
	}

}
