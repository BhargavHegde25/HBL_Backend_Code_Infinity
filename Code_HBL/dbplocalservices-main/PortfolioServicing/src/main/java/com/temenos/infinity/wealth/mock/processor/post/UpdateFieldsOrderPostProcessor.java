/**
 * 
 */
package com.temenos.infinity.wealth.mock.processor.post;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.wealth.common.util.PortfolioServiceUtils;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

/**
 * @author himaja.sridhar
 *
 */
public class UpdateFieldsOrderPostProcessor implements DataPostProcessor2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		diagnostic.prepareDebug("==========> UpdateFieldsOrderPostProcessor Mock - Entered ").log();
		String portfolioId = request.getParameter(TemenosConstants.PORTFOLIOID).toString();
		String userId = request.getParameter(TemenosConstants.USERID).toString();
		String fieldOrder = request.getParameter(TemenosConstants.FIELDORDER).toString();
		Map<String, Object> inputMap = new HashMap<>();
		inputMap.put(TemenosConstants.PORTFOLIOID, portfolioId);
		inputMap.put(TemenosConstants.USERID, userId);
		inputMap.put(TemenosConstants.FIELDORDER, fieldOrder);
		
		Result final_Result = new Result();
		Result getResult = PortfolioServiceUtils.getPreferences(portfolioId, userId, request);

		diagnostic.prepareDebug("==========> UpdateFieldsOrderPostProcessor Mock - Preferences fetced ").log();
		if (HelperMethods.hasRecords(getResult)) {
			List<Record> existingRecords = getResult.getAllDatasets().get(0).getAllRecords();
			for (Record record : existingRecords) {
				String id = HelperMethods.getFieldValue(record, "id");
				inputMap.put("id", id);
				HelperMethods.removeNullValues(inputMap);
				HelperMethods.callApi(request, inputMap, HelperMethods.getHeaders(request),
						URLConstants.WEALTH_USER_PREFERENCES_UPDATE);
				final_Result.addParam("status", "Success");
				final_Result.addParam("msg", "User preferences updated successfully");
				final_Result.addParam("opstatus", "0");
				final_Result.addParam("httpStatusCode", "200");
				diagnostic.prepareDebug("==========> UpdateFieldsOrderPostProcessor Mock - Exited ").log();
				return final_Result;
			}
			diagnostic.prepareDebug("==========> UpdateFieldsOrderPostProcessor Mock - Exited ").log();
			return final_Result;
		} else {
			inputMap.put("id", HelperMethods.getNumericId() + "");
			HelperMethods.removeNullValues(inputMap);
			HelperMethods.callApi(request, inputMap, HelperMethods.getHeaders(request),
					URLConstants.WEALTH_USER_PREFERENCES_CREATE);
			final_Result.addParam("status", "Success");
			final_Result.addParam("msg", "User preferences created successfully");
			final_Result.addParam("opstatus", "0");
			final_Result.addParam("httpStatusCode", "200");
			diagnostic.prepareDebug("==========> UpdateFieldsOrderPostProcessor Mock - Exited ").log();
			return final_Result;
		}
	}

}
