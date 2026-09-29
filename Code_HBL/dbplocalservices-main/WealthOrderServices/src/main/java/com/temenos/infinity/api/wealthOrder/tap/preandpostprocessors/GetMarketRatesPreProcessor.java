
/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.tap.preandpostprocessors;

import java.util.HashMap;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * @author himaja.sridhar
 *
 */
public class GetMarketRatesPreProcessor implements DataPreProcessor2 {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		inputMap.put("natureE", "Sell");
		inputMap.put("completeOnly","true");
		inputMap.put("orderTypeCode", "PCK_TCIB_FXSPOT_CON");
		inputMap.put("fxRateDirectionF", "false");
		inputMap.put("portfolioCode", request.getParameter(TemenosConstants.PORTFOLIOID));
		inputMap.put(TemenosConstants.SELLCURRENCY,request.getParameter(TemenosConstants.CURRENCYPAIRS).substring(0, 3));
		inputMap.put(TemenosConstants.BUYCURRENCY,request.getParameter(TemenosConstants.CURRENCYPAIRS).substring(3));
		return true;
	}
}
