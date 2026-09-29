/**
 * 
 */
package com.temenos.infinity.api.wealthservices.preandpostprocessors;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.util.T24ErrorAndOverrideHandling;

/**
 * @author himaja.sridhar
 *
 */
public class T24ErrorAndOverrideHandlingPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
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
		
		T24ErrorAndOverrideHandling errorHandlingutil = new T24ErrorAndOverrideHandling();
		result = errorHandlingutil.ProcessT24Error(result, request, response);
		return result;
	}

}
