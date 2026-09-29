package com.temenos.infinity.wealthorder.tap.processor.pre;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.tap.preandpostprocessors.TAPTokenGenPreProcessor;

/**
 * 
 * @author muthukumarv
 *
 */

public class CancelNDMAOrdersPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("rawtypes")
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		diagnostic.prepareDebug("==========> CancelNDMAOrdersPreProcessor TAP - Entered ").log();
		try {
			if (request.getParameter("OrderID_Authentication") != null
					&& request.getParameter("OrderID_Authentication").equalsIgnoreCase("true")) {
				if (inputMap.get(TemenosConstants.ASSETTYPE) != null
						&& StringUtils.isNotBlank(inputMap.get(TemenosConstants.ASSETTYPE).toString())) {
					String assetType = inputMap.get(TemenosConstants.ASSETTYPE).toString();
					if (assetType.equalsIgnoreCase("Fund Share")) {
						TAPTokenGenPreProcessor obj = new TAPTokenGenPreProcessor();
						obj.execute(inputMap, request, response, result);
						diagnostic.prepareDebug("==========>  CancelNDMAOrdersPreProcessor TAP - Token Generation Succeeded").log();
						return true;
					} else {
						result.addOpstatusParam("0");
						result.addHttpStatusCodeParam("200");
						result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
						diagnostic.prepareDebug("==========>  CancelNDMAOrdersPreProcessor TAP - Exiting with non fund share").log();
						return false;
					}
				} else {
					result.addOpstatusParam("0");
					result.addHttpStatusCodeParam("200");
					result.addParam(TemenosConstants.STATUS, "Failure");
					result.addParam("error", "Invalid Input! " + TemenosConstants.ASSETTYPE + " is mandatory.");
					diagnostic.prepareDebug("==========>  CancelNDMAOrdersPreProcessor TAP - Exiting with assettype missing").log();
					return false;
				}
			} else {
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				diagnostic.prepareDebug("==========>  CancelNDMAOrdersPreProcessor TAP - Exiting with OrderID_Authentication false").log();
				return false;
			}
		} catch (Exception e) {
			alert.prepareError("==========>  CancelNDMAOrdersPreProcessor TAP - Error: " + e.getMessage()).log();
			e.getMessage();
			return false;
		}
	}

}
