/**
 * 
 */
package com.temenos.infinity.wealthorder.refinitiv.processor.post;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * @author himaja.sridhar
 *
 */
public class GetNewsDetailsRefinitivPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		diagnostic.prepareDebug("==========> GetNewsDetailsRefinitivPostProcessor Refinitiv - Entered ").log();
		try {
			Dataset ds = result.getDatasetById(TemenosConstants.STORYIDS);
			if (ds != null) {
				String jsonString = ResultToJSON.convertDataset(ds).toString();
				result.addParam("StoryId", jsonString);
			}

		} catch (Exception e) {

			alert.prepareError("==========> GetNewsDetailsRefinitivPostProcessor Refinitiv - Error: " + e.getMessage()).log();
		}
		diagnostic.prepareDebug("==========> GetNewsDetailsRefinitivPostProcessor Refinitiv - Exiting with success ").log();
		return result;
	}

}
