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
public class ModifyOrderOrchPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		diagnostic.prepareDebug("==========> ModifyOrderOrchPreProcessor T24 - Entered ").log();
		try {
			// Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
			// Map<String, Object> inputJSON = new HashMap<>();

			String orderId = null, quantity = null, orderType = null, price = null, limitPrice = null, validate = null,
					validity = null, portfolioId = null, regexAmount = "^\\d*\\.?\\d+$", customerId = null,
					tradeCurrency = null, instrumentId = null;

			if (inputMap.get(TemenosConstants.PORTFOLIOID) != null
					&& inputMap.get(TemenosConstants.PORTFOLIOID).toString().trim().length() > 0) {
				portfolioId = inputMap.get(TemenosConstants.PORTFOLIOID).toString();
				// inputJSON.put(TemenosConstants.PORTFOLIOID, portfolioId);
			} else {
				return OrderServiceUtils.validateMandatoryFields(result, TemenosConstants.PORTFOLIOID);
			}

			if (inputMap.get(TemenosConstants.ORDER_ID) != null
					&& inputMap.get(TemenosConstants.ORDER_ID).toString().trim().length() > 0) {
				orderId = inputMap.get(TemenosConstants.ORDER_ID).toString();
				// inputJSON.put(TemenosConstants.ORDER_ID, orderId);
			} else {
				return OrderServiceUtils.validateMandatoryFields(result, TemenosConstants.ORDER_ID);
			}

			if (inputMap.get(TemenosConstants.ORDERTYPE) != null
					&& inputMap.get(TemenosConstants.ORDERTYPE).toString().trim().length() > 0) {
				orderType = inputMap.get(TemenosConstants.ORDERTYPE).toString();
				// inputJSON.put(TemenosConstants.ORDERTYPE, orderType);
			} else {
				return OrderServiceUtils.validateMandatoryFields(result, TemenosConstants.ORDERTYPE);
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

			if (inputMap.get(TemenosConstants.TRADECURRENCY) != null) {
				tradeCurrency = inputMap.get(TemenosConstants.TRADECURRENCY).toString();
				// inputJSON.put(TemenosConstants.TRADECURRENCY, tradeCurrency);
			}

			if (inputMap.get(TemenosConstants.INSTRUMENTID) != null
					&& inputMap.get(TemenosConstants.INSTRUMENTID).toString().trim().length() > 0) {
				instrumentId = inputMap.get(TemenosConstants.INSTRUMENTID).toString();
				// inputJSON.put(TemenosConstants.INSTRUMENTID, instrumentId);
			}

			if (orderType != null && orderType.equalsIgnoreCase(TemenosConstants.LIMIT_TYPE)) {
				if (inputMap.get(TemenosConstants.LIMITPRICE) != null
						&& inputMap.get(TemenosConstants.LIMITPRICE).toString().trim().length() > 0) {
					if (inputMap.get(TemenosConstants.LIMITPRICE).toString().matches(regexAmount)) {
						limitPrice = inputMap.get(TemenosConstants.LIMITPRICE).toString();
						// inputJSON.put(TemenosConstants.LIMITPRICE, limitPrice);
					} else {
						return OrderServiceUtils.validateMandatoryFields(result, TemenosConstants.LIMITPRICE);
					}
				} else {
					return OrderServiceUtils.validateMandatoryFields(result, TemenosConstants.LIMITPRICE);
				}
			}

			if (orderType != null && orderType.equalsIgnoreCase(TemenosConstants.STOPLOSS_TYPE)) {
				if (inputMap.get(TemenosConstants.PRICE) != null
						&& inputMap.get(TemenosConstants.PRICE).toString().trim().length() > 0)
					if (inputMap.get(TemenosConstants.PRICE).toString().matches(regexAmount)) {
						price = inputMap.get(TemenosConstants.PRICE).toString();
						// inputJSON.put(TemenosConstants.PRICE, price);
					} else {
						return OrderServiceUtils.validateMandatoryFields(result, TemenosConstants.PRICE);
					}
				else {
					return OrderServiceUtils.validateMandatoryFields(result, TemenosConstants.PRICE);
				}
			}

			if (orderType != null && orderType.equalsIgnoreCase(TemenosConstants.STOPLIMIT_TYPE)) {
				if (inputMap.get(TemenosConstants.PRICE) != null
						&& inputMap.get(TemenosConstants.PRICE).toString().trim().length() > 0) {
					if (inputMap.get(TemenosConstants.PRICE).toString().matches(regexAmount)) {
						price = inputMap.get(TemenosConstants.PRICE).toString();
						// inputJSON.put(TemenosConstants.PRICE, price);
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
						// inputJSON.put(TemenosConstants.LIMITPRICE, limitPrice);
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
					// inputJSON.put(TemenosConstants.LIMITPRICE, limitPrice.replace("$", ""));
				}
				if (inputMap.get(TemenosConstants.PRICE) != null) {
					price = inputMap.get(TemenosConstants.PRICE).toString();
					// inputJSON.put(TemenosConstants.PRICE, price.replace("$", ""));
				}
			}

			if (inputMap.get(TemenosConstants.QUANTITY) != null
					&& inputMap.get(TemenosConstants.QUANTITY).toString().trim().length() > 0) {
				if (inputMap.get(TemenosConstants.QUANTITY).toString().matches(regexAmount)) {
					quantity = inputMap.get(TemenosConstants.QUANTITY).toString();
					// inputJSON.put(TemenosConstants.QUANTITY, quantity);
				} else {
					return OrderServiceUtils.validateMandatoryFields(result, TemenosConstants.QUANTITY);
				}
			}

			if (inputMap.get(TemenosConstants.VALIDITY) != null
					&& inputMap.get(TemenosConstants.VALIDITY).toString().trim().length() > 0) {
				validity = inputMap.get(TemenosConstants.VALIDITY).toString();
				inputMap.put(TemenosConstants.LIMITTYPE, validity);
				request.addRequestParam_(TemenosConstants.LIMITTYPE, validity);
			} else {
				return OrderServiceUtils.validateMandatoryFields(result, TemenosConstants.VALIDITY);
			}

			if (inputMap.get(TemenosConstants.VALIDATEONLY) != null) {
				validate = inputMap.get(TemenosConstants.VALIDATEONLY).toString();
				// inputJSON.put(TemenosConstants.VALIDATEONLY, validate);
			}
			if (inputMap.get(TemenosConstants.MARKETPRICE) != null
					&& inputMap.get(TemenosConstants.MARKETPRICE).toString().trim().length() > 0) {
				// inputJSON.put(TemenosConstants.MARKETPRICE,
				// inputMap.get(TemenosConstants.MARKETPRICE).toString().replace("$", ""));
			}
			inputMap.put("cancelormodify_order", true);
			request.addRequestParam_("cancelormodify_order", "true");
			diagnostic.prepareDebug("==========> ModifyOrderOrchPreProcessor T24 - Entering into integration ").log();
			return true;

		} catch (Exception e) {
			alert.prepareError("==========> ModifyOrderOrchPreProcessor T24 - Error: " + e.getMessage()).log();
			return false;
		}
	}

}
