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
public class GetInstrumentDetailsRefinitivPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetInstrumentDetailsRefinitivPreProcessor Refinitiv - Entered ").log();
			String wealthCore = EnvironmentConfigurationsHandler.getValue(TemenosConstants.WEALTH_CORE, request);

			if (wealthCore != null
					&& (wealthCore.equalsIgnoreCase("TAP,Refinitiv") || wealthCore.equalsIgnoreCase("T24,Refinitiv"))) {
				inputMap.put("objName", "instrumentDetails");
				inputMap.put("fields",
						"CF_CURRENCY:CF_EXCHNG:ISIN_CODE:TRADE_DATE:CF_NAME:PRCTCK_1:BID:ASK:TRDPRC_1:PCTCHNG:CF_CLOSE:CF_NETCHNG:CF_DATE:CF_TIME");
				inputMap.put("instrumentsCode",request.getParameter("RICCode").toString());
				request.addRequestParam_("objName", "instrumentDetails");
				diagnostic.prepareDebug("==========> GetInstrumentDetailsRefinitivPreProcessor Refinitiv - Entering into integration ").log();
				return true;
			} else {
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				diagnostic.prepareDebug("==========> GetInstrumentDetailsRefinitivPreProcessor Refinitiv - Exiting into integration ").log();
				return false;
			}
		} catch (Exception e) {
			alert.prepareError("==========> GetInstrumentDetailsRefinitivPreProcessor Refinitiv - Error: " + e.getMessage()).log();
			return false;
		}
	}

}
