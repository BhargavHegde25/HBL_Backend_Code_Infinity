/**
 * 
 */
package com.temenos.infinity.wealth.tap.processor.post;



import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealth.util.WealthMockUtil;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * @author muthukumarv
 *
 */
public class UpdateEntityTAPPostProcessor implements DataPostProcessor2 {
	
	WealthMockUtil wealthMockUtil = new WealthMockUtil();
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			diagnostic.prepareDebug("==========> UpdateEntityTAPPostProcessor TAP - Entered ").log();
			Dataset bodyData = result.getDatasetById("body");
			Result finalResult = new Result();
			if (bodyData != null) {
				finalResult.addOpstatusParam("0");
				finalResult.addHttpStatusCodeParam("200");
				finalResult.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				diagnostic.prepareDebug("==========> UpdateEntityTAPPostProcessor TAP - Exited ").log();
				return finalResult;
				
			} else {
				finalResult.addOpstatusParam("0");
				finalResult.addHttpStatusCodeParam("200");
				finalResult.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				diagnostic.prepareDebug("==========> UpdateEntityTAPPostProcessor TAP - Exited ").log();
				return finalResult;
			}

		} catch (Exception e) {
			alert.prepareError("==========> UpdateEntityTAPPostProcessor TAP - Error: " + e.getMessage()).log();
			return null;
		}
		
	}
}
