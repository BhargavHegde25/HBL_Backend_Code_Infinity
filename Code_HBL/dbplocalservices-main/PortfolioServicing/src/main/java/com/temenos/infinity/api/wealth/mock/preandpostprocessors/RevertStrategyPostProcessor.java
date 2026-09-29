/**
 * 
 */
package com.temenos.infinity.api.wealth.mock.preandpostprocessors;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * @author himaja.sridhar
 *
 */
public class RevertStrategyPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Result revertStrategy = new Result();
		revertStrategy.addParam("message","Delete succeeded");
		revertStrategy.addOpstatusParam("0");
		revertStrategy.addHttpStatusCodeParam("200");
		revertStrategy.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		return revertStrategy;
	}

}
