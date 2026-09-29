/**
 * 
 */
package com.temenos.infinity.api.wealth.tap.preandpostprocessors;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.Record;


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
		
		double str1 = Double.parseDouble(portVal);
		double str2 = Double.parseDouble(unrealPl);
		
		double str3 = (str2 / str1) * 100;
		
		String unRealizedPer = String.format("%.2f", str3);
		
		unrealPl = String.valueOf(Math.abs(Double.parseDouble(unrealPl)));
		
		result.getDatasetById("instrumentTotal").getRecord(0).addParam("marketValue", portVal);
		result.getDatasetById("instrumentTotal").getRecord(0).addParam("unRealizedPLAmount", unrealPl);
		result.getDatasetById("instrumentTotal").getRecord(0).addParam("unRealizedPL", unRealizedPL);
		result.getDatasetById("instrumentTotal").getRecord(0).addParam("unRealizedPLPercentage", unRealizedPer);
		result.removeRecordById("portfolioValues");
		
		return result;
	}

}
