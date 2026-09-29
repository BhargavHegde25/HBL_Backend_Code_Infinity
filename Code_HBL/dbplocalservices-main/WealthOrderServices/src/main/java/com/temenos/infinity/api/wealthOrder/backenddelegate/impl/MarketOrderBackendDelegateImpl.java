package com.temenos.infinity.api.wealthOrder.backenddelegate.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.MarketOrderBackendDelegate;
import com.temenos.infinity.api.wealthOrder.config.WealthAPIServices;
import com.temenos.infinity.api.wealthOrder.constants.OperationName;
import com.temenos.infinity.api.wealthOrder.constants.ServiceId;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;


public class MarketOrderBackendDelegateImpl implements MarketOrderBackendDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("unchecked")
	@Override
	public Result createMarketOrder(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		Map<String, Object> inputJSON = new HashMap<>();

		String portfolioId = null, instrumentId = null, customerId = null, stockExchange = null, tradeCurrency = null,
				limitPrice = null, price = null, order = null, orderType = null, quantity = null, validity = null,
				validate = null, funcResultCode = null, regexAmount = "^\\d*\\.?\\d+$";

		if (inputParams.get(TemenosConstants.PORTFOLIOID) != null
				&& inputParams.get(TemenosConstants.PORTFOLIOID).toString().trim().length() > 0) {
			portfolioId = inputParams.get(TemenosConstants.PORTFOLIOID).toString();
			inputJSON.put(TemenosConstants.PORTFOLIOID, portfolioId);
		} else {
			return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.PORTFOLIOID);
		}

		if (inputParams.get(TemenosConstants.INSTRUMENTID) != null
				&& inputParams.get(TemenosConstants.INSTRUMENTID).toString().trim().length() > 0) {
			instrumentId = inputParams.get(TemenosConstants.INSTRUMENTID).toString();
			inputJSON.put(TemenosConstants.INSTRUMENTID, instrumentId);
		} else {
			return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.INSTRUMENTID);
		}

		customerId = PortfolioWealthUtils.getCustomerFromCache(request);
		inputJSON.put(TemenosConstants.CUSTOMERID, customerId);
		
		if (inputParams.get(TemenosConstants.STOCKEXCHANGE) != null) {
			stockExchange = inputParams.get(TemenosConstants.STOCKEXCHANGE).toString();
			inputJSON.put(TemenosConstants.STOCKEXCHANGE, stockExchange);
		}
		if (inputParams.get(TemenosConstants.TRADECURRENCY) != null) {
			tradeCurrency = inputParams.get(TemenosConstants.TRADECURRENCY).toString();
			inputJSON.put(TemenosConstants.TRADECURRENCY, tradeCurrency);
		}

		if (inputParams.get(TemenosConstants.ORDER) != null
				&& inputParams.get(TemenosConstants.ORDER).toString().trim().length() > 0) {
			if (inputParams.get(TemenosConstants.ORDER).toString().equalsIgnoreCase("BUY")
					|| inputParams.get(TemenosConstants.ORDER).toString().equalsIgnoreCase("SEL")) {
				order = inputParams.get(TemenosConstants.ORDER).toString();
				inputJSON.put(TemenosConstants.ORDER, order);
				inputJSON.put(TemenosConstants.TRANSACTIONTYPE, order);
			} else {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.ORDER);
			}
		} else {
			return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.ORDER);
		}

		if (inputParams.get(TemenosConstants.ORDERTYPE) != null
				&& inputParams.get(TemenosConstants.ORDERTYPE).toString().trim().length() > 0) {
			orderType = inputParams.get(TemenosConstants.ORDERTYPE).toString();
			inputJSON.put(TemenosConstants.ORDERTYPE, orderType);
		} else {
			return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.ORDERTYPE);
		}

		if (inputParams.get(TemenosConstants.QUANTITY) != null
				&& inputParams.get(TemenosConstants.QUANTITY).toString().trim().length() > 0) {
			if (inputParams.get(TemenosConstants.QUANTITY).toString().matches(regexAmount)) {
				quantity = inputParams.get(TemenosConstants.QUANTITY).toString();
				inputJSON.put(TemenosConstants.QUANTITY, quantity);
			} else {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.QUANTITY);
			}
		} else {
			return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.QUANTITY);
		}

		if (orderType != null && orderType.equalsIgnoreCase(TemenosConstants.LIMIT_TYPE)) {
			if (inputParams.get(TemenosConstants.LIMITPRICE) != null
					&& inputParams.get(TemenosConstants.LIMITPRICE).toString().trim().length() > 0) {
				if (inputParams.get(TemenosConstants.LIMITPRICE).toString().matches(regexAmount)) {
					limitPrice = inputParams.get(TemenosConstants.LIMITPRICE).toString();
					inputJSON.put(TemenosConstants.LIMITPRICE, limitPrice);
				} else {
					return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.LIMITPRICE);
				}
			} else {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.LIMITPRICE);
			}
		}

		if (orderType != null && orderType.equalsIgnoreCase(TemenosConstants.STOPLOSS_TYPE)) {
			if (inputParams.get(TemenosConstants.PRICE) != null
					&& inputParams.get(TemenosConstants.PRICE).toString().trim().length() > 0) {
				if (inputParams.get(TemenosConstants.PRICE).toString().matches(regexAmount)) {
					price = inputParams.get(TemenosConstants.PRICE).toString();
					inputJSON.put(TemenosConstants.PRICE, price);
				} else {
					return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.PRICE);
				}
			} else {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.PRICE);
			}
		}

		if (orderType != null && orderType.equalsIgnoreCase(TemenosConstants.STOPLIMIT_TYPE)) {
			if (inputParams.get(TemenosConstants.PRICE) != null
					&& inputParams.get(TemenosConstants.PRICE).toString().trim().length() > 0) {
				if (inputParams.get(TemenosConstants.PRICE).toString().matches(regexAmount)) {
					price = inputParams.get(TemenosConstants.PRICE).toString();
					inputJSON.put(TemenosConstants.PRICE, price);
				} else {
					return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.PRICE);
				}
			} else {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.PRICE);
			}

			if (inputParams.get(TemenosConstants.LIMITPRICE) != null
					&& inputParams.get(TemenosConstants.LIMITPRICE).toString().trim().length() > 0) {
				if (inputParams.get(TemenosConstants.LIMITPRICE).toString().matches(regexAmount)) {
					limitPrice = inputParams.get(TemenosConstants.LIMITPRICE).toString();
					inputJSON.put(TemenosConstants.LIMITPRICE, limitPrice);
				} else {
					return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.LIMITPRICE);
				}
			} else {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.LIMITPRICE);
			}
		}

		if (orderType != null) {
			if (inputParams.get(TemenosConstants.LIMITPRICE) != null) {
				limitPrice = inputParams.get(TemenosConstants.LIMITPRICE).toString();
				inputJSON.put(TemenosConstants.LIMITPRICE, limitPrice.replace("$", ""));
			}
			if (inputParams.get(TemenosConstants.PRICE) != null) {
				price = inputParams.get(TemenosConstants.PRICE).toString();
				inputJSON.put(TemenosConstants.PRICE, price.replace("$", ""));
			}
		}

		if (inputParams.get(TemenosConstants.VALIDITY) != null
				&& inputParams.get(TemenosConstants.VALIDITY).toString().trim().length() > 0) {
			if (inputParams.get(TemenosConstants.VALIDITY).toString().equalsIgnoreCase("GTD")
					|| inputParams.get(TemenosConstants.VALIDITY).toString().equalsIgnoreCase("GTC")) {
				validity = inputParams.get(TemenosConstants.VALIDITY).toString();
				inputJSON.put(TemenosConstants.LIMITTYPE, validity);
			} else {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.VALIDITY);
			}
		} else {
			return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.VALIDITY);
		}

		if (inputParams.get(TemenosConstants.VALIDATEONLY) != null) {
			validate = inputParams.get(TemenosConstants.VALIDATEONLY).toString();
			inputJSON.put(TemenosConstants.VALIDATEONLY, validate);
		}

		if (inputParams.get(TemenosConstants.FUNCRESULTCODE) != null) {
			funcResultCode = inputParams.get(TemenosConstants.FUNCRESULTCODE).toString();
			inputJSON.put(TemenosConstants.FUNCRESULTCODE, funcResultCode);
		}

		if (inputParams.get(TemenosConstants.MARKETPRICE) != null
				&& inputParams.get(TemenosConstants.MARKETPRICE).toString().trim().length() > 0) {
			inputJSON.put(TemenosConstants.MARKETPRICE,
					inputParams.get(TemenosConstants.MARKETPRICE).toString().replace("$", ""));
		}

		String returnResponse = null;
		String serviceName = ServiceId.WEALTHORCHESTRATION;
		String operationName = OperationName.CREATE_MARKET_ORDER;
		List<String> allportfoliosList = PortfolioWealthUtils.getAllPortfoliosFromCache(request);

		if (allportfoliosList.contains(portfolioId)) {

			try {
				returnResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
						.withOperationId(operationName).withRequestParameters(inputJSON).withRequestHeaders(headersMap)
						.withDataControllerRequest(request).build().getResponse();
				JSONObject resultJSON = new JSONObject(returnResponse);
				return Utilities.constructResultFromJSONObject(resultJSON);

			} catch (Exception e) {
				alert.prepareError("Error while invoking Transact - " + WealthAPIServices.CREATE_MARKET_ORDER.getOperationName()
						+ "  : " + e).log();
				return null;

			}
		} else {
			alert.prepareError("Portfolio ID " + portfolioId + " does not exist for the Customer").log();
			return PortfolioWealthUtils.UnauthorizedAccess();
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public Result modifyOrder(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		Map<String, Object> inputJSON = new HashMap<>();

		String orderId = null, quantity = null, orderType = null, price = null, limitPrice = null, validate = null,
				validity = null, portfolioId = null, regexAmount = "^\\d*\\.?\\d+$", customerId = null,
				tradeCurrency = null, instrumentId = null;

		if (inputParams.get(TemenosConstants.PORTFOLIOID) != null
				&& inputParams.get(TemenosConstants.PORTFOLIOID).toString().trim().length() > 0) {
			portfolioId = inputParams.get(TemenosConstants.PORTFOLIOID).toString();
			inputJSON.put(TemenosConstants.PORTFOLIOID, portfolioId);
		} else {
			return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.PORTFOLIOID);
		}

		if (inputParams.get(TemenosConstants.ORDER_ID) != null
				&& inputParams.get(TemenosConstants.ORDER_ID).toString().trim().length() > 0) {
			orderId = inputParams.get(TemenosConstants.ORDER_ID).toString();
			inputJSON.put(TemenosConstants.ORDER_ID, orderId);
		} else {
			return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.ORDER_ID);
		}

		if (inputParams.get(TemenosConstants.ORDERTYPE) != null
				&& inputParams.get(TemenosConstants.ORDERTYPE).toString().trim().length() > 0) {
			orderType = inputParams.get(TemenosConstants.ORDERTYPE).toString();
			inputJSON.put(TemenosConstants.ORDERTYPE, orderType);
		} else {
			return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.ORDERTYPE);
		}
		
		customerId = PortfolioWealthUtils.getCustomerFromCache(request);
		inputJSON.put(TemenosConstants.CUSTOMERID, customerId);

		if (inputParams.get(TemenosConstants.TRADECURRENCY) != null) {
			tradeCurrency = inputParams.get(TemenosConstants.TRADECURRENCY).toString();
			inputJSON.put(TemenosConstants.TRADECURRENCY, tradeCurrency);
		}

		if (inputParams.get(TemenosConstants.INSTRUMENTID) != null
				&& inputParams.get(TemenosConstants.INSTRUMENTID).toString().trim().length() > 0) {
			instrumentId = inputParams.get(TemenosConstants.INSTRUMENTID).toString();
			inputJSON.put(TemenosConstants.INSTRUMENTID, instrumentId);
		}

		if (orderType != null && orderType.equalsIgnoreCase(TemenosConstants.LIMIT_TYPE)) {
			if (inputParams.get(TemenosConstants.LIMITPRICE) != null
					&& inputParams.get(TemenosConstants.LIMITPRICE).toString().trim().length() > 0) {
				if (inputParams.get(TemenosConstants.LIMITPRICE).toString().matches(regexAmount)) {
					limitPrice = inputParams.get(TemenosConstants.LIMITPRICE).toString();
					inputJSON.put(TemenosConstants.LIMITPRICE, limitPrice);
				} else {
					return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.LIMITPRICE);
				}
			} else {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.LIMITPRICE);
			}
		}

		if (orderType != null && orderType.equalsIgnoreCase(TemenosConstants.STOPLOSS_TYPE)) {
			if (inputParams.get(TemenosConstants.PRICE) != null
					&& inputParams.get(TemenosConstants.PRICE).toString().trim().length() > 0)
				if (inputParams.get(TemenosConstants.PRICE).toString().matches(regexAmount)) {
					price = inputParams.get(TemenosConstants.PRICE).toString();
					inputJSON.put(TemenosConstants.PRICE, price);
				} else {
					return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.PRICE);
				}
			else {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.PRICE);
			}
		}

		if (orderType != null && orderType.equalsIgnoreCase(TemenosConstants.STOPLIMIT_TYPE)) {
			if (inputParams.get(TemenosConstants.PRICE) != null
					&& inputParams.get(TemenosConstants.PRICE).toString().trim().length() > 0) {
				if (inputParams.get(TemenosConstants.PRICE).toString().matches(regexAmount)) {
					price = inputParams.get(TemenosConstants.PRICE).toString();
					inputJSON.put(TemenosConstants.PRICE, price);
				} else {
					return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.PRICE);
				}
			} else {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.PRICE);
			}

			if (inputParams.get(TemenosConstants.LIMITPRICE) != null
					&& inputParams.get(TemenosConstants.LIMITPRICE).toString().trim().length() > 0) {
				if (inputParams.get(TemenosConstants.LIMITPRICE).toString().matches(regexAmount)) {
					limitPrice = inputParams.get(TemenosConstants.LIMITPRICE).toString();
					inputJSON.put(TemenosConstants.LIMITPRICE, limitPrice);
				} else {
					return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.LIMITPRICE);
				}
			} else {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.LIMITPRICE);
			}
		}

		if (orderType != null) {
			if (inputParams.get(TemenosConstants.LIMITPRICE) != null) {
				limitPrice = inputParams.get(TemenosConstants.LIMITPRICE).toString();
				inputJSON.put(TemenosConstants.LIMITPRICE, limitPrice.replace("$", ""));
			}
			if (inputParams.get(TemenosConstants.PRICE) != null) {
				price = inputParams.get(TemenosConstants.PRICE).toString();
				inputJSON.put(TemenosConstants.PRICE, price.replace("$", ""));
			}
		}

		if (inputParams.get(TemenosConstants.QUANTITY) != null
				&& inputParams.get(TemenosConstants.QUANTITY).toString().trim().length() > 0) {
			if (inputParams.get(TemenosConstants.QUANTITY).toString().matches(regexAmount)) {
				quantity = inputParams.get(TemenosConstants.QUANTITY).toString();
				inputJSON.put(TemenosConstants.QUANTITY, quantity);
			} else {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.QUANTITY);
			}
		}

		if (inputParams.get(TemenosConstants.VALIDITY) != null
				&& inputParams.get(TemenosConstants.VALIDITY).toString().trim().length() > 0) {
			validity = inputParams.get(TemenosConstants.VALIDITY).toString();
			inputJSON.put(TemenosConstants.LIMITTYPE, validity);
		} else {
			return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.VALIDITY);
		}

		if (inputParams.get(TemenosConstants.VALIDATEONLY) != null) {
			validate = inputParams.get(TemenosConstants.VALIDATEONLY).toString();
			inputJSON.put(TemenosConstants.VALIDATEONLY, validate);
		}
		if (inputParams.get(TemenosConstants.MARKETPRICE) != null
				&& inputParams.get(TemenosConstants.MARKETPRICE).toString().trim().length() > 0) {
			inputJSON.put(TemenosConstants.MARKETPRICE,
					inputParams.get(TemenosConstants.MARKETPRICE).toString().replace("$", ""));
		}
		inputJSON.put("cancelormodify_order", true);

		String returnResponse = null;
		String serviceName = ServiceId.WEALTHORCHESTRATION;
		String operationName = OperationName.MODIFYORDER;

		List<String> allportfoliosList = PortfolioWealthUtils.getAllPortfoliosFromCache(request);

		if (allportfoliosList.contains(portfolioId)) {

			try {
				returnResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
						.withOperationId(operationName).withRequestParameters(inputJSON).withRequestHeaders(headersMap)
						.withDataControllerRequest(request).build().getResponse();
				JSONObject resultJSON = new JSONObject(returnResponse);
				return Utilities.constructResultFromJSONObject(resultJSON);

			} catch (Exception e) {
				alert.prepareError("Error while invoking Transact - " + WealthAPIServices.MODIFYORDER.getOperationName() + "  : "
						+ e).log();
				return null;

			}

		} else {
			alert.prepareError("Portfolio ID " + portfolioId + " does not exist for the Customer").log();
			return PortfolioWealthUtils.UnauthorizedAccess();
		}

	}
}
