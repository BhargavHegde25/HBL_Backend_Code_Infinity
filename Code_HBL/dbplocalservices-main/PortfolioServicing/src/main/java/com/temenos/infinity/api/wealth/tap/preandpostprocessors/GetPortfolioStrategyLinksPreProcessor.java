/**
 * 
 */
package com.temenos.infinity.api.wealth.tap.preandpostprocessors;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
public class GetPortfolioStrategyLinksPreProcessor implements DataPreProcessor2 {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		if (request.getParameter(TemenosConstants.WEALTH_CORE) != null
				&& (request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("TAP,Refinitiv")
						|| request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("TAP"))) {
		String endDate;
		Calendar edt = Calendar.getInstance();
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
		endDate = sdformat.format(edt.getTime());
		inputMap.put("id", request.getParameter("idVal"));
		inputMap.put("endD", endDate);
		return true;
	}
	else {
		result.addOpstatusParam("0");
		result.addHttpStatusCodeParam("200");
		result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		return false;
	}	
	}
}
