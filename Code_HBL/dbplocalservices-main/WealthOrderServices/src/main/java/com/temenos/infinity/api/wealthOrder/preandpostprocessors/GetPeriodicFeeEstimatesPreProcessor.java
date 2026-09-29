/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.preandpostprocessors;

import java.util.HashMap;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * (INFO) Generates Transact Token. Needs to be included for all transact
 * services. Prepares the input in the desired format.
 * 
 * @author balaji.kk
 *
 */
public class GetPeriodicFeeEstimatesPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			String validate = request.getParameter(TemenosConstants.VALIDATEONLY) != null
					? request.getParameter(TemenosConstants.VALIDATEONLY).toString()
					: "";
			if (validate.equals("")) {
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				return false;
			} else {
				//TransactTokenGenPreProcessor obj = new TransactTokenGenPreProcessor();
				//obj.execute(inputMap, request, response, result);

				if (request.getParameter(TemenosConstants.PORTFOLIOID) != null) {
					inputMap.put(TemenosConstants.PORTFOLIOID,
							request.getParameter(TemenosConstants.PORTFOLIOID).toString());
				}
				if (request.getParameter(TemenosConstants.INSTRUMENTID) != null) {
					inputMap.put(TemenosConstants.INSTRUMENTID,
							request.getParameter(TemenosConstants.INSTRUMENTID).toString());
				}
				if (request.getParameter(TemenosConstants.DEPOSITORYID) != null) {
					inputMap.put(TemenosConstants.DEPOSITORYID,
							request.getParameter(TemenosConstants.DEPOSITORYID).toString());
				}
				if (request.getParameter(TemenosConstants.TRADECURRENCY) != null) {
					inputMap.put(TemenosConstants.TRADECURRENCY,
							request.getParameter(TemenosConstants.TRADECURRENCY).toString());
				}
				if (request.getParameter(TemenosConstants.QUANTITY) != null) {
					inputMap.put(TemenosConstants.QUANTITY, request.getParameter(TemenosConstants.QUANTITY).toString());
				}

				if (request.getParameter(TemenosConstants.ORDERTYPE) != null) {
					String ordertype = request.getParameter(TemenosConstants.ORDERTYPE).toString();
					if (ordertype.equalsIgnoreCase("MARKET")) {
						inputMap.put(TemenosConstants.AMOUNT, request.getParameter(TemenosConstants.MARKETPRICE));
					} else if (ordertype.equalsIgnoreCase("LIMIT")) {
						inputMap.put(TemenosConstants.AMOUNT, request.getParameter(TemenosConstants.LIMITPRICE));
					} else if (ordertype.equalsIgnoreCase("STOP LOSS") || ordertype.equalsIgnoreCase("STOP")) {
						inputMap.put(TemenosConstants.AMOUNT, request.getParameter(TemenosConstants.PRICE));
					} else if (ordertype.equalsIgnoreCase("STOP LIMIT") || ordertype.equalsIgnoreCase("STOP.LIMIT")) {
						inputMap.put(TemenosConstants.AMOUNT, request.getParameter(TemenosConstants.LIMITPRICE));
					}
				}

				if (request.getParameter(TemenosConstants.CUSTOMERID) != null) {
					inputMap.put(TemenosConstants.CUSTOMERID,
							request.getParameter(TemenosConstants.CUSTOMERID).toString());
				}

				return true;
			}
		} catch (Exception e) {
			alert.prepareError("Error in GetPeriodicFeeEstimatesPreProcessor" + e).log();
			return false;
		}
	}

}
