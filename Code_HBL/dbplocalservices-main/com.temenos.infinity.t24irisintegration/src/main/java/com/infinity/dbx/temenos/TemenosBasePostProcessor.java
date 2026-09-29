package com.infinity.dbx.temenos;

import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.utils.T24ErrorAndOverrideHandling;
import com.kony.dbx.BasePostProcessor;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class TemenosBasePostProcessor extends BasePostProcessor implements TemenosConstants {
	@Override
	public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		if (result == null) {
			throw new Exception("Result object provided is null");
		}
		if (request == null) {
			throw new Exception("Request object provided is null");
		}
		if (response == null) {
			throw new Exception("Response object provided is null");
		}
		// First run the BasePostProcessor
		super.execute(result, request, response);
		T24ErrorAndOverrideHandling errorHandlingutil = T24ErrorAndOverrideHandling.getInstance();
		result = errorHandlingutil.ProcessT24Error(result, request, response);
		return result;
	}
}
