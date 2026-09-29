/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.tap.preandpostprocessors;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/**
 * @author himaja.sridhar
 *
 */
public class GetPortfolioDetailsPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Record portfolioValuesRec = result.getRecordById("portfolioValues");
		String portVal = portfolioValuesRec.getParamValueByName("portVal");
		String unrealPl = portfolioValuesRec.getParamValueByName("unrealPl");
		String unRealizedPL = Double.parseDouble(unrealPl) >= 0 ? "P" : "L";
		unrealPl = String.valueOf(Math.abs(Double.parseDouble(unrealPl)));
		
		result.getDatasetById("instrumentTotal").getRecord(0).addParam("marketValue", portVal);
		result.getDatasetById("instrumentTotal").getRecord(0).addParam("unRealizedPLAmount", unrealPl);
		result.getDatasetById("instrumentTotal").getRecord(0).addParam("unRealizedPL", unRealizedPL);
		result.removeRecordById("portfolioValues");
		
		return result;
	}

}
