/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.tap.preandpostprocessors;

import java.util.HashMap;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * @author himaja.sridhar
 *
 */
public class CreateForexOrderPreProcessor implements DataPreProcessor2 {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		inputMap.put("orderTypeCode", "PCK_TCIB_FXSPOT_CON");
		inputMap.put("communicationTypeDenom", "Internet");
		inputMap.put("commPartyTypeDenom", "Account Holder");
		inputMap.put("clientInitE", "Client initiated");
		inputMap.put("natureE", "SELL");
		return true;
	}

}
