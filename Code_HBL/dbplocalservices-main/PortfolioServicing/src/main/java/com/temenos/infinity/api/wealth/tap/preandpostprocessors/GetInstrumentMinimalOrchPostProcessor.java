/**
 * 
 */
package com.temenos.infinity.api.wealth.tap.preandpostprocessors;


import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * @author himaja.sridhar
 *
 */
public class GetInstrumentMinimalOrchPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		String ric = result.getParamValueByName("RICCode");
		result.removeParamByName("RICCode");
		if (result.getDatasetById("instrumentMinimal").getRecord(0).getParamByName("id") == null) {

		} else {
			result.getDatasetById("instrumentMinimal").getRecord(0).addParam("RICCode", ric);
		}
		result.addOpstatusParam("0");
		result.addHttpStatusCodeParam("200");
		result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		return result;
	}

}
