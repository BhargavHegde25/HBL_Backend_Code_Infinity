/**
 * 
 */
package com.temenos.infinity.wealthorder.t24.processor.pre;

import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;
import com.temenos.infinity.wealthorder.common.util.OrderServiceUtils;

/**
 * @author muthukumarv
 *
 */
public class CreateOrderOrchPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			diagnostic.prepareDebug("==========> CreateOrderOrchPreProcessor T24 - Entered ").log();
			String customerId = null, tradeCurrency = null, limitPrice = null, price = null, order = null,
					orderType = null, quantity = null, validity = null, validate = null, funcResultCode = null,
					regexAmount = "^\\d*\\.?\\d+$";

			if (inputMap.get(TemenosConstants.PORTFOLIOID) != null
					&& inputMap.get(TemenosConstants.PORTFOLIOID).toString().trim().length() > 0) {
				// portfolioId = inputMap.get(TemenosConstants.PORTFOLIOID).toString();
				// inputMap.put(TemenosConstants.PORTFOLIOID, portfolioId);
			} else {
				return OrderServiceUtils.validateMandatoryFields(result, TemenosConstants.PORTFOLIOID);
			}

			if (inputMap.get(TemenosConstants.INSTRUMENTID) != null
					&& inputMap.get(TemenosConstants.INSTRUMENTID).toString().trim().length() > 0) {
				// instrumentId = inputMap.get(TemenosConstants.INSTRUMENTID).toString();
				// inputMap.put(TemenosConstants.INSTRUMENTID, instrumentId);
			} else {
				return OrderServiceUtils.validateMandatoryFields(result, TemenosConstants.INSTRUMENTID);
			}
			
			/* Checking whether coreCustomerId is present in request or not */
			String coreCustomerId = null;
			if (inputMap.get(TemenosConstants.CORECUSTOMERID) != null
					&& inputMap.get(TemenosConstants.CORECUSTOMERID).toString().trim().length() > 0) {
				/*CorecustomerId is present in request so entering Multi customer flow*/
				coreCustomerId=inputMap.get(TemenosConstants.CORECUSTOMERID).toString();
				inputMap.put(TemenosConstants.CUSTOMERID, coreCustomerId);
				request.addRequestParam_(TemenosConstants.CUSTOMERID, coreCustomerId);
			} else {
				/*CorecustomerId is not present in request so entering Single customer flow*/
				coreCustomerId = PortfolioWealthUtils.getCustomerFromCache(request);
				inputMap.put(TemenosConstants.CUSTOMERID, coreCustomerId);
				request.addRequestParam_(TemenosConstants.CUSTOMERID, coreCustomerId);
			}
//			customerId = PortfolioWealthUtils.getCustomerFromCache(request);
//			inputMap.put(TemenosConstants.CUSTOMERID, customerId);
//			request.addRequestParam_(TemenosConstants.CUSTOMERID, customerId);

			if (inputMap.get(TemenosConstants.STOCKEXCHANGE) != null) {
				// stockExchange = inputMap.get(TemenosConstants.STOCKEXCHANGE).toString();
				// inputMap.put(TemenosConstants.STOCKEXCHANGE, stockExchange);
			}
			if (inputMap.get(TemenosConstants.TRADECURRENCY) != null) {
				tradeCurrency = inputMap.get(TemenosConstants.TRADECURRENCY).toString();
				inputMap.put(TemenosConstants.TRADECURRENCY, tradeCurrency);
			}

			if (inputMap.get(TemenosConstants.ORDER) != null
					&& inputMap.get(TemenosConstants.ORDER).toString().trim().length() > 0) {
				if (inputMap.get(TemenosConstants.ORDER).toString().equalsIgnoreCase("BUY")
						|| inputMap.get(TemenosConstants.ORDER).toString().equalsIgnoreCase("SEL")) {
					order = inputMap.get(TemenosConstants.ORDER).toString();
					inputMap.put(TemenosConstants.ORDER, order);
					request.addRequestParam_(TemenosConstants.TRANSACTIONTYPE, order);
					inputMap.put(TemenosConstants.TRANSACTIONTYPE, order);
				} else {
					return OrderServiceUtils.validateMandatoryFields(result, TemenosConstants.ORDER);
				}
			} else {
				return OrderServiceUtils.validateMandatoryFields(result, TemenosConstants.ORDER);
			}

			if (inputMap.get(TemenosConstants.ORDERTYPE) != null
					&& inputMap.get(TemenosConstants.ORDERTYPE).toString().trim().length() > 0) {
				orderType = inputMap.get(TemenosConstants.ORDERTYPE).toString();
				inputMap.put(TemenosConstants.ORDERTYPE, orderType);
			} else {
				return OrderServiceUtils.validateMandatoryFields(result, TemenosConstants.ORDERTYPE);
			}

			if (inputMap.get(TemenosConstants.QUANTITY) != null
					&& inputMap.get(TemenosConstants.QUANTITY).toString().trim().length() > 0) {
				if (inputMap.get(TemenosConstants.QUANTITY).toString().matches(regexAmount)) {
					quantity = inputMap.get(TemenosConstants.QUANTITY).toString();
					inputMap.put(TemenosConstants.QUANTITY, quantity);
				} else {
					return OrderServiceUtils.validateMandatoryFields(result, TemenosConstants.QUANTITY);
				}
			} else {
				return OrderServiceUtils.validateMandatoryFields(result, TemenosConstants.QUANTITY);
			}

			if (orderType != null && orderType.equalsIgnoreCase(TemenosConstants.LIMIT_TYPE)) {
				if (inputMap.get(TemenosConstants.LIMITPRICE) != null
						&& inputMap.get(TemenosConstants.LIMITPRICE).toString().trim().length() > 0) {
					if (inputMap.get(TemenosConstants.LIMITPRICE).toString().matches(regexAmount)) {
						limitPrice = inputMap.get(TemenosConstants.LIMITPRICE).toString();
						inputMap.put(TemenosConstants.LIMITPRICE, limitPrice);
					} else {
						return OrderServiceUtils.validateMandatoryFields(result, TemenosConstants.LIMITPRICE);
					}
				} else {
					return OrderServiceUtils.validateMandatoryFields(result, TemenosConstants.LIMITPRICE);
				}
			}

			if (orderType != null && orderType.equalsIgnoreCase(TemenosConstants.STOPLOSS_TYPE)) {
				if (inputMap.get(TemenosConstants.PRICE) != null
						&& inputMap.get(TemenosConstants.PRICE).toString().trim().length() > 0) {
					if (inputMap.get(TemenosConstants.PRICE).toString().matches(regexAmount)) {
						price = inputMap.get(TemenosConstants.PRICE).toString();
						inputMap.put(TemenosConstants.PRICE, price);
					} else {
						return OrderServiceUtils.validateMandatoryFields(result, TemenosConstants.PRICE);
					}
				} else {
					return OrderServiceUtils.validateMandatoryFields(result, TemenosConstants.PRICE);
				}
			}

			if (orderType != null && orderType.equalsIgnoreCase(TemenosConstants.STOPLIMIT_TYPE)) {
				if (inputMap.get(TemenosConstants.PRICE) != null
						&& inputMap.get(TemenosConstants.PRICE).toString().trim().length() > 0) {
					if (inputMap.get(TemenosConstants.PRICE).toString().matches(regexAmount)) {
						price = inputMap.get(TemenosConstants.PRICE).toString();
						inputMap.put(TemenosConstants.PRICE, price);
					} else {
						return OrderServiceUtils.validateMandatoryFields(result, TemenosConstants.PRICE);
					}
				} else {
					return OrderServiceUtils.validateMandatoryFields(result, TemenosConstants.PRICE);
				}

				if (inputMap.get(TemenosConstants.LIMITPRICE) != null
						&& inputMap.get(TemenosConstants.LIMITPRICE).toString().trim().length() > 0) {
					if (inputMap.get(TemenosConstants.LIMITPRICE).toString().matches(regexAmount)) {
						limitPrice = inputMap.get(TemenosConstants.LIMITPRICE).toString();
						inputMap.put(TemenosConstants.LIMITPRICE, limitPrice);
					} else {
						return OrderServiceUtils.validateMandatoryFields(result, TemenosConstants.LIMITPRICE);
					}
				} else {
					return OrderServiceUtils.validateMandatoryFields(result, TemenosConstants.LIMITPRICE);
				}
			}

			if (orderType != null) {
				if (inputMap.get(TemenosConstants.LIMITPRICE) != null) {
					limitPrice = inputMap.get(TemenosConstants.LIMITPRICE).toString();
					inputMap.put(TemenosConstants.LIMITPRICE, limitPrice.replace("$", ""));
				}
				if (inputMap.get(TemenosConstants.PRICE) != null) {
					price = inputMap.get(TemenosConstants.PRICE).toString();
					inputMap.put(TemenosConstants.PRICE, price.replace("$", ""));
				}
			}

			if (inputMap.get(TemenosConstants.VALIDITY) != null
					&& inputMap.get(TemenosConstants.VALIDITY).toString().trim().length() > 0) {
				if (inputMap.get(TemenosConstants.VALIDITY).toString().equalsIgnoreCase("GTD")
						|| inputMap.get(TemenosConstants.VALIDITY).toString().equalsIgnoreCase("GTC")) {
					validity = inputMap.get(TemenosConstants.VALIDITY).toString();
					request.addRequestParam_(TemenosConstants.LIMITTYPE, validity);
					inputMap.put(TemenosConstants.LIMITTYPE, validity);
				} else {
					return OrderServiceUtils.validateMandatoryFields(result, TemenosConstants.VALIDITY);
				}
			} else {
				return OrderServiceUtils.validateMandatoryFields(result, TemenosConstants.VALIDITY);
			}

			if (inputMap.get(TemenosConstants.VALIDATEONLY) != null) {
				validate = inputMap.get(TemenosConstants.VALIDATEONLY).toString();
				inputMap.put(TemenosConstants.VALIDATEONLY, validate);
			}

			if (inputMap.get(TemenosConstants.FUNCRESULTCODE) != null) {
				funcResultCode = inputMap.get(TemenosConstants.FUNCRESULTCODE).toString();
				inputMap.put(TemenosConstants.FUNCRESULTCODE, funcResultCode);
			}

			if (inputMap.get(TemenosConstants.MARKETPRICE) != null
					&& inputMap.get(TemenosConstants.MARKETPRICE).toString().trim().length() > 0) {
				inputMap.put(TemenosConstants.MARKETPRICE,
						inputMap.get(TemenosConstants.MARKETPRICE).toString().replace("$", ""));
			}
			diagnostic.prepareDebug("==========> CreateOrderOrchPreProcessor T24 - Exited ").log();
			return true;

		} catch (Exception e) {
			alert.prepareError("==========> CreateOrderOrchPreProcessor T24 - Error: " + e.getMessage()).log();
			return false;
		}
	}

}
