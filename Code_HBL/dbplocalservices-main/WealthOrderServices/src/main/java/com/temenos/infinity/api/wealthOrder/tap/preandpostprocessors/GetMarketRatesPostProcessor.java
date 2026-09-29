/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.tap.preandpostprocessors;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/**
 * @author himaja.sridhar
 *
 */
public class GetMarketRatesPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Dataset bodySet=result.getDatasetById("body");
		String exchangeRate = bodySet.getRecord(0).getParamValueByName("fxClientRateN");
		result.addParam("marketRate", exchangeRate);
		result.clearRecords();
		result.clearDatasets();
		return result;
	}

}
