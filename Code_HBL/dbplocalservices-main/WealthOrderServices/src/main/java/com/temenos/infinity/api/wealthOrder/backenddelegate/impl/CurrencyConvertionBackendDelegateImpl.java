/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.backenddelegate.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.CurrencyConvertionBackendDelegate;
import com.temenos.infinity.api.wealthOrder.config.WealthAPIServices;
import com.temenos.infinity.api.wealthOrder.constants.OperationName;
import com.temenos.infinity.api.wealthOrder.constants.ServiceId;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthOrder.util.WealthMockUtil;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;
/**
 * @author balaji.kk
 *
 */
public class CurrencyConvertionBackendDelegateImpl implements CurrencyConvertionBackendDelegate {

	WealthMockUtil wealthMockUtil = new WealthMockUtil();

	@Override
	public Result createCurrencyConvertion(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Map<String, Object> inputMap = new HashMap<>();
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];

		String portfolioId = null, amount = null, buyCurrency = null, buyaccount = null, sellaccount = null,
				validate_only = null;

		if (inputParams.get(TemenosConstants.PORTFOLIOID) != null
				&& inputParams.get(TemenosConstants.PORTFOLIOID).toString().trim().length() > 0) {
			portfolioId = inputParams.get(TemenosConstants.PORTFOLIOID).toString();
			inputMap.put(TemenosConstants.PORTFOLIOID, portfolioId);
		} else {
			return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.PORTFOLIOID);
		}

		if (inputParams.get(TemenosConstants.AMOUNT) != null
				&& inputParams.get(TemenosConstants.AMOUNT).toString().trim().length() > 0) {
			amount = inputParams.get(TemenosConstants.AMOUNT).toString();
			inputMap.put(TemenosConstants.AMOUNT, amount);
		} else {
			return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.AMOUNT);
		}

		if (inputParams.get(TemenosConstants.BUYCURRENCY) != null
				&& inputParams.get(TemenosConstants.BUYCURRENCY).toString().trim().length() > 0) {
			buyCurrency = inputParams.get(TemenosConstants.BUYCURRENCY).toString();
			inputMap.put(TemenosConstants.BUYCURRENCY, buyCurrency);
		} else {
			return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.BUYCURRENCY);
		}

		if (inputParams.get(TemenosConstants.BUYACCOUNT) != null
				&& inputParams.get(TemenosConstants.BUYACCOUNT).toString().trim().length() > 0) {
			buyaccount = inputParams.get(TemenosConstants.BUYACCOUNT).toString();
			inputMap.put(TemenosConstants.BUYACCOUNT, buyaccount);
		}
		if (inputParams.get(TemenosConstants.SELLACCOUNT) != null
				&& inputParams.get(TemenosConstants.SELLACCOUNT).toString().trim().length() > 0) {
			sellaccount = inputParams.get(TemenosConstants.SELLACCOUNT).toString();
			inputMap.put(TemenosConstants.SELLACCOUNT, sellaccount);
		}
		if (inputParams.get(TemenosConstants.VALIDATEONLY) != null) {
			validate_only = inputParams.get(TemenosConstants.VALIDATEONLY).toString();
			inputMap.put(TemenosConstants.VALIDATEONLY, validate_only);
		}
		inputMap.put("paymentOrderProductId", "ACTRF");
/*
		List<String> allportfoliosList = WealthUtils.getAllPortfoliosFromCache(request);

		if (allportfoliosList.contains(portfolioId)) {
			try {*/
				String serviceName = ServiceId.WEALTHORCHESTRATION;
				String operationName = OperationName.CREATE_CURRENCY_CONVERTION;

				return PortfolioWealthUtils.backendResponse(serviceName, operationName, inputMap, headersMap, request);
/*
			} catch (Exception e) {
				alert.prepareError("Error while invoking Transact - " + WealthAPIServices.WEALTH_CANCELORDER.getOperationName()
						+ "  : " + e).log();
				return null;

			}
		} else {

			return WealthUtils.UnauthorizedAccess();

		}*/

	}

}
