/**
 * 
 */
package com.temenos.infinity.wealthorder.refinitiv.processor.pre;

import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * @author himaja.sridhar
 *
 */
public class GetPricingDataRefinitivPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetPricingDataRefinitivPreProcessor Refinitiv - Entered ").log();
			String wealthCore = EnvironmentConfigurationsHandler.getValue(TemenosConstants.WEALTH_CORE, request);

			if (wealthCore != null
					&& (wealthCore.equalsIgnoreCase("TAP,Refinitiv") || wealthCore.equalsIgnoreCase("T24,Refinitiv"))) {
				inputMap.put("fields",
						"CF_BID:BIDSIZE:CF_ASK:ASKSIZE:CF_VOLUME:CF_OPEN:CF_CLOSE:52WK_HIGH:52WK_LOW:CF_LAST:CF_CURRENCY");
				inputMap.put("objName", "pricingDetails");
				request.addRequestParam_("objName", "pricingDetails");
				inputMap.put("instrumentsCode", request.getParameter(TemenosConstants.RICCODE));
				diagnostic.prepareDebug("==========> GetPricingDataRefinitivPreProcessor Refinitiv - Entering integration ").log();
				return true;
			} else {
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				diagnostic.prepareDebug("==========> GetPricingDataRefinitivPreProcessor Refinitiv - Exiting integration ").log();
				return false;
			}
		} catch (Exception e) {
			alert.prepareError("==========> GetPricingDataRefinitivPreProcessor Refinitiv - Error: " + e.getMessage()).log();
			return false;
		}

	}

}
