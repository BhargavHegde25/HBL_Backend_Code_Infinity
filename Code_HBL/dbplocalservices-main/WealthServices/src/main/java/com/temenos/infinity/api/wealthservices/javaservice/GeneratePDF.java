/**
 * 
 */
package com.temenos.infinity.api.wealthservices.javaservice;

import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;

/**
 * @author himaja.sridhar
 *
 */
public class GeneratePDF implements JavaService2 {
	Map<String, String> responseHeaders = new HashMap<String, String>();

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
				return response;

	}

}
