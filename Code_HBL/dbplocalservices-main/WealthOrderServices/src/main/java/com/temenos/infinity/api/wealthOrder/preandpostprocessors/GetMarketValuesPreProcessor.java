/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.preandpostprocessors;

import java.util.HashMap;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * (INFO) If currenncypairs exists as a part of the request , the operation is
 * executed else control is passed to the next operation.
 * 
 * @author himaja.sridhar
 *
 */
public class GetMarketValuesPreProcessor implements DataPreProcessor2 {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		String wealthCore = EnvironmentConfigurationsHandler.getValue(TemenosConstants.WEALTH_CORE, request);
		if (wealthCore != null && (wealthCore.equalsIgnoreCase("T24,Refinitiv") || wealthCore.equalsIgnoreCase("TAP,Refinitiv")) && (request.getParameter("flagHis")=="false")) {
		if (request.getParameter(TemenosConstants.CURRENCYPAIRS) != null) {
			return true;
		} else {
			result.addOpstatusParam("0");
			result.addHttpStatusCodeParam("200");
			result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			return false;
		} }else {
			result.addOpstatusParam("0");
			result.addHttpStatusCodeParam("200");
			result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			return false;
		}
	}

}
