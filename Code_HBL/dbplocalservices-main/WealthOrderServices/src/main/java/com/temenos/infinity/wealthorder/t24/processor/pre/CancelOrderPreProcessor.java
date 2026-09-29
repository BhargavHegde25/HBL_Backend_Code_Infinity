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
 * (INFO) If status is set as a part of the request , the operation is exited
 * else operation is executed.
 * 
 * @author balaji.krishnan
 *
 */
public class CancelOrderPreProcessor implements DataPreProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public boolean execute(@SuppressWarnings("rawtypes") HashMap inputMap, DataControllerRequest request,
			DataControllerResponse response, Result result) throws Exception {

		try {
			diagnostic.prepareDebug("==========>  CancelOrderPreProcessor T24 - Entered").log();
			if (request.getParameter(TemenosConstants.WEALTH_CORE) != null
					&& (request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("T24,Refinitiv")
							|| request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("T24"))) {
				request.addRequestParam_("channelName", "INFINITY");
				request.addRequestParam_(TemenosConstants.ORDERSVIEW_TYPE, "OPEN");
				request.addRequestParam_("dealStatus", "CANCELLED");
				alert.prepareError("==========> CancelOrderPreProcessor T24 - Entering t24 integration ").log();
				return true;
			} else {
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				diagnostic.prepareDebug("==========>  CancelOrderPreProcessor T24 - Exited").log();
				return false;
			}
		} catch (Exception e) {
			alert.prepareError("==========> CancelOrderPreProcessor T24 - Error: " + e.getMessage()).log();
			return false;
		}
	}

}
