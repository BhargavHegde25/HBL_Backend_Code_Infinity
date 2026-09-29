/**
 * 
 */
package com.temenos.infinity.wealth.tap.processor.pre;

import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.tap.preandpostprocessors.TAPTokenGenPreProcessor;

/**
 * @author himaja.sridhar
 *
 */
public class GetAccountActivityTAPPreProcessor implements DataPreProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetAccountActivityTAPPreProcessor TAP - Entered ").log();
			if (request.getParameter(TemenosConstants.WEALTH_CORE) != null
					&& (request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("TAP,Refinitiv")
							|| request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("TAP"))) {
				diagnostic.prepareDebug("==========>  GetAccountActivityTAPPreProcessor TAP - Begin:Manipulation of input parameters").log();
				String dateFrom = request.getParameter(TemenosConstants.DATEFROM).toString();
				String dateTo = request.getParameter(TemenosConstants.DATETO).toString();
				String startDate = dateFrom.substring(0, 4) + "-" + dateFrom.substring(4, 6) + "-"
						+ dateFrom.substring(6, 8);
				String endDate = dateTo.substring(0, 4) + "-" + dateTo.substring(4, 6) + "-" + dateTo.substring(6, 8);
				inputMap.put(TemenosConstants.DATEFROM, startDate);
				inputMap.put(TemenosConstants.DATETO, endDate);
				String orderBy = inputMap.get(TemenosConstants.ORDERBY).toString();
				String orderType = inputMap.get(TemenosConstants.SORTTYPE).toString().toLowerCase();
				orderBy = orderBy.concat("%20").concat(orderType);
				inputMap.put(TemenosConstants.ORDERBY, orderBy);
				diagnostic.prepareDebug("==========>  GetAccountActivityTAPPreProcessor TAP - End:Manipulation of input parameters").log();
				TAPTokenGenPreProcessor obj = new TAPTokenGenPreProcessor();
				obj.execute(inputMap, request, response, result);
				diagnostic.prepareDebug("==========>  GetAccountActivityTAPPreProcessor TAP - Token Generation Succeeded").log();
				return true;
			} else {
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				diagnostic.prepareDebug("==========>  GetAccountActivityTAPPreProcessor TAP - Exiting without Token Generation").log();
				return false;
			}
		} catch (Exception e) {
			alert.prepareError("==========>  GetAccountActivityTAPPreProcessor TAP - Error: " + e.getMessage()).log();
			e.getMessage();
		}
		return false;
	}
}
