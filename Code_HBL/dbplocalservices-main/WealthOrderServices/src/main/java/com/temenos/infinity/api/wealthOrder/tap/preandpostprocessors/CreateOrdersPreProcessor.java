package com.temenos.infinity.api.wealthOrder.tap.preandpostprocessors;

import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.tap.preandpostprocessors.TAPTokenGenPreProcessor;

/**
 * (INFO) If status is set as a part of the request , the operation is exited
 * else operation is executed.
 * 
 * @author balaji.krishnan
 *
 */

public class CreateOrdersPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "rawtypes", "unchecked" })
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
				
				if (request.getParameter(TemenosConstants.ORDER) != null) {
					if (request.getParameter(TemenosConstants.ORDER).toString().equalsIgnoreCase("BUY")) {
						inputMap.put("natureE", "BUY");
					} else {
						inputMap.put("natureE", "SELL");
					}
				}
				if (request.getParameter(TemenosConstants.QUANTITY) != null) {
					inputMap.put("quantityN", request.getParameter(TemenosConstants.QUANTITY).toString());
				}
				if (request.getParameter(TemenosConstants.LIMITTYPE) != null) {
					if (request.getParameter(TemenosConstants.LIMITTYPE).toString().equalsIgnoreCase("GTD")) {
						inputMap.put("orderValidityNatE", "Day Order");
					} else {
						// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
						inputMap.put("orderValidityNatE", "GTC");
						// inputMap.put("orderLimitD", format.format(new Date()));
					}
				}
				if (request.getParameter(TemenosConstants.INSTRUMENTID) != null) {
					inputMap.put("instrCode", request.getParameter(TemenosConstants.INSTRUMENTID).toString());
				}
				if (request.getParameter(TemenosConstants.PRICE) != null) {
					inputMap.put("stopQuoteN", request.getParameter(TemenosConstants.PRICE));
				}
				if (request.getParameter(TemenosConstants.LIMITPRICE) != null) {
					inputMap.put("limitQuoteN", request.getParameter(TemenosConstants.LIMITPRICE));
				}
				if (request.getParameter(TemenosConstants.ORDERTYPE) != null) {
					String ordertype = request.getParameter(TemenosConstants.ORDERTYPE).toString();
					if (ordertype.equalsIgnoreCase("MARKET")) {
						inputMap.remove("stopQuoteN");
						inputMap.remove("limitQuoteN");
						inputMap.put("orderNatE", "Market");
					} else if (ordertype.equalsIgnoreCase("LIMIT")) {
						inputMap.remove("stopQuoteN");
						inputMap.put("orderNatE", "Limit");
					} else if (ordertype.equalsIgnoreCase("STOP LOSS")) {
						inputMap.remove("limitQuoteN");
						inputMap.put("orderNatE", "Stop");
					} else if (ordertype.equalsIgnoreCase("STOP LIMIT")) {
						inputMap.put("orderNatE", "Stop-Limit");
					}
				}

				inputMap.put(TemenosConstants.ORDERTYPECODE, "PCK_TCIB_SECURITIES");
				inputMap.put("targetNatureE", "Quantity");
				return true;
			}
		} catch (Exception e) {
			alert.prepareError("Error in CreateOrdersPreProcessor" + e).log();
			e.getMessage();
		}
		return false;
	}

}
