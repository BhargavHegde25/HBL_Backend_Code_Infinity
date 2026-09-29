package com.temenos.infinity.wealthorder.tap.processor.pre;

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
 * 
 * @author muthukumarv
 *
 */

public class ModifyOrdersPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		diagnostic.prepareDebug("==========>  ModifyOrdersPreProcessor TAP - Entered").log();
		try {
			if (request.getParameter("OrderID_Authentication") != null
					&& request.getParameter("OrderID_Authentication").equalsIgnoreCase("true")) {

				if (request.getParameter(TemenosConstants.ORDER_ID) != null) {
					inputMap.put("orderId", request.getParameter(TemenosConstants.ORDER_ID).toString());
				}
				// if (request.getParameter(TemenosConstants.QUANTITY) != null) {
				// inputMap.put("quantityN",
				// request.getParameter(TemenosConstants.QUANTITY).toString());
				// }
				if (request.getParameter(TemenosConstants.LIMITTYPE) != null) {
					inputMap.put("orderValidityNatE",
							request.getParameter(TemenosConstants.LIMITTYPE).toString().equalsIgnoreCase("GTD")
									? "Day Order"
									: "GTC");
				}
				if (request.getParameter(TemenosConstants.PRICE) != null
						&& request.getParameter(TemenosConstants.PRICE).toString().length() > 0) {
					inputMap.put("stopQuoteN", request.getParameter(TemenosConstants.PRICE));
				} else {
					inputMap.put("stopQuoteN", "null");
				}

				if (request.getParameter(TemenosConstants.LIMITPRICE) != null
						&& request.getParameter(TemenosConstants.LIMITPRICE).toString().length() > 0) {
					inputMap.put("limitQuoteN", request.getParameter(TemenosConstants.LIMITPRICE));
				} else {
					inputMap.put("limitQuoteN", "null");
				}
				if (request.getParameter(TemenosConstants.ORDERTYPE) != null) {
					String ordertype = request.getParameter(TemenosConstants.ORDERTYPE).toString();
					if (ordertype.equalsIgnoreCase("MARKET")) {
						inputMap.put("orderNatE", "Market");
					} else if (ordertype.equalsIgnoreCase("LIMIT")) {
						inputMap.put("orderNatE", "Limit");
					} else if (ordertype.equalsIgnoreCase("STOP")) {
						inputMap.put("orderNatE", "Stop");
					} else if (ordertype.equalsIgnoreCase("STOP.LIMIT")) {
						inputMap.put("orderNatE", "Stop-Limit");
					}
				}
				if (request.getParameter(TemenosConstants.VALIDATEONLY) != null) {
					inputMap.put("validate_only", request.getParameter(TemenosConstants.VALIDATEONLY));
				}
				TAPTokenGenPreProcessor obj = new TAPTokenGenPreProcessor();
				obj.execute(inputMap, request, response, result);
				diagnostic.prepareDebug("==========>  ModifyOrdersPreProcessor TAP - Token Generation Succeeded").log();
				return true;
			} else {
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				diagnostic.prepareDebug("==========>  ModifyOrdersPreProcessor TAP - Exiting with OrderID_Authentication false").log();
				return false;
			}

		} catch (Exception e) {
			alert.prepareError("==========>  ModifyOrdersPreProcessor TAP - Error: " + e.getMessage()).log();
			e.getMessage();
			return false;
		}
	}

}
