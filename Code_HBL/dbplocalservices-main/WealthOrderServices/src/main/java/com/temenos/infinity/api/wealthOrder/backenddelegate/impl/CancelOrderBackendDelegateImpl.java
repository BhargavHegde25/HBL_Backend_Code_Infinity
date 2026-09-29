/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.backenddelegate.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.CancelOrderBackendDelegate;
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
public class CancelOrderBackendDelegateImpl implements CancelOrderBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	WealthMockUtil wealthMockUtil = new WealthMockUtil();

	@Override
	public Result cancelOrder(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Map<String, Object> inputMap = new HashMap<>();
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];

		String orderID = null, portfolioId = null, assetType = null;

		if (inputParams.get(TemenosConstants.PORTFOLIOID) != null
				&& inputParams.get(TemenosConstants.PORTFOLIOID).toString().trim().length() > 0) {
			portfolioId = inputParams.get(TemenosConstants.PORTFOLIOID).toString();
			inputMap.put(TemenosConstants.PORTFOLIOID, portfolioId);
		} else {
			return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.PORTFOLIOID);
		}

		if (inputParams.get(TemenosConstants.ORDER_ID) != null
				&& inputParams.get(TemenosConstants.ORDER_ID).toString().trim().length() > 0) {
			orderID = inputParams.get(TemenosConstants.ORDER_ID).toString();
			inputMap.put(TemenosConstants.ORDER_ID, orderID);
		} else {
			return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.ORDER_ID);
		}

		if (inputParams.get(TemenosConstants.ASSETTYPE) != null) {
			assetType = inputParams.get(TemenosConstants.ASSETTYPE).toString();
			inputMap.put(TemenosConstants.ASSETTYPE, assetType);
		}
		inputMap.put("cancelormodify_order", true);

		List<String> allportfoliosList = PortfolioWealthUtils.getAllPortfoliosFromCache(request);

		if (allportfoliosList.contains(portfolioId)) {
			try {
				String serviceName = ServiceId.WEALTHORCHESTRATION;
				String operationName = OperationName.CANCELORDERDETAILS;

				return PortfolioWealthUtils.backendResponse(serviceName, operationName, inputMap, headersMap, request);

			} catch (Exception e) {
				alert.prepareError("Error while invoking Transact - " + WealthAPIServices.WEALTH_CANCELORDER.getOperationName()
						+ "  : " + e).log();
				return null;

			}
		} else {

			return PortfolioWealthUtils.UnauthorizedAccess();

		}

	}

}
