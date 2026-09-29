/**
 * 
 */
package com.temenos.infinity.api.wealth.tap.preandpostprocessors;

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
public class GetTransactionDetailsPreProcessor implements DataPreProcessor2 {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		String orderBy = inputMap.get(TemenosConstants.ORDERBY).toString();
		String orderType = inputMap.get(TemenosConstants.SORTTYPE).toString().toString().toLowerCase();
		orderBy = orderBy.concat("%20").concat(orderType);
		inputMap.put(TemenosConstants.ORDERBY,orderBy);
		return true;
	}

}
