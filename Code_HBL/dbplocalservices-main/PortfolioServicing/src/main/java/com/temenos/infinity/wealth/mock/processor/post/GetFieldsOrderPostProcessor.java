/**
 * 
 */
package com.temenos.infinity.wealth.mock.processor.post;

import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * @author himaja.sridhar
 *
 */
public class GetFieldsOrderPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		diagnostic.prepareDebug("==========> GetFieldsOrderPostProcessor Mock - Entered ").log();
		String portfolioId = request.getParameter(TemenosConstants.PORTFOLIOID).toString();
		String userId = request.getParameter(TemenosConstants.USERID).toString();
		Map<String, String> input = new HashMap<>();
		String filter = "userId" + DBPUtilitiesConstants.EQUAL + userId + DBPUtilitiesConstants.AND + "portfolioId"
				+ DBPUtilitiesConstants.EQUAL + portfolioId;
		input.put(DBPUtilitiesConstants.FILTER, filter);
		try {
			diagnostic.prepareDebug("==========> GetFieldsOrderPostProcessor Mock - Exited").log();
			return HelperMethods.callGetApi(request, input.get(DBPUtilitiesConstants.FILTER),
					HelperMethods.getHeaders(request), URLConstants.WEALTH_USER_PREFERENCES_GET);
		} catch (Exception e) {
			alert.prepareError("==========> GetFieldsOrderPostProcessor Mock - Error: " + e.getMessage()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_21001);
		}
	}

}
