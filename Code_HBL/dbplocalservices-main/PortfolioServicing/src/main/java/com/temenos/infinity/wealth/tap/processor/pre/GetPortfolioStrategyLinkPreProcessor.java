/**
 * 
 */
package com.temenos.infinity.wealth.tap.processor.pre;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * @author himaja.sridhar
 *
 */
public class GetPortfolioStrategyLinkPreProcessor implements DataPreProcessor2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		diagnostic.prepareDebug("==========> GetPortfolioStrategyLinkPreProcessor TAP - Entered ").log();
		if (request.getParameter(TemenosConstants.WEALTH_CORE) != null
				&& (request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("TAP,Refinitiv")
						|| request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("TAP"))) {
		diagnostic.prepareDebug("==========>  GetPortfolioStrategyLinkPreProcessor TAP - Begin:Manipulation of input parameters").log();
		String endDate,startDate;
		Calendar edt = Calendar.getInstance();
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
		endDate = sdformat.format(edt.getTime());
		
		Calendar sdt = Calendar.getInstance();
		sdt.add(Calendar.YEAR, 1);
		//startDate = sdformat.format(sdt.getTime());
		
		inputMap.put("strategyNatureE", "Investment Profile");
		inputMap.put("beginD", endDate);
		//inputMap.put("endD", null);
		diagnostic.prepareDebug("==========>  GetPortfolioStrategyLinkPreProcessor TAP - End:Manipulation of input parameters").log();
		return true;
	}
		else {
			result.addOpstatusParam("0");
			result.addHttpStatusCodeParam("200");
			result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			diagnostic.prepareDebug("==========> GetPortfolioStrategyLinkPreProcessor TAP - Exiting without Token Generation").log();
			return false;
		}
	}

}
