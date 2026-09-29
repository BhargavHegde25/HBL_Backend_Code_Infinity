/**
 * 
 */
package com.temenos.infinity.api.wealth.backenddelegate.impl;

import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealth.backenddelegate.api.DailyMarketBackendDelegate;
import com.temenos.infinity.api.wealth.config.PortfolioWealthAPIServices;
import com.temenos.infinity.api.wealth.constants.OperationName;
import com.temenos.infinity.api.wealth.constants.ServiceId;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;

/**
 * @author himaja.sridhar
 *
 */
public class DailyMarketBackendDelegateImpl implements DailyMarketBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Result getDailyMarket(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {

		Map<String, Object> inputMap = new HashMap<>();
		Object marketIndexObj = TemenosConstants.MARKETINDEX;
		String marketIndex = null;
		String inputValue = "";

		if (marketIndexObj != null && marketIndexObj.toString().trim().length() > 0) {
			marketIndex = (TemenosConstants.MARKETINDEX).toString();
			String marketsArr[] = marketIndex.toUpperCase().trim().split("\\s*,\\s*");
			for (String marketsVal : marketsArr) {
				String forQuotes = "\"";
				inputValue = inputValue.concat(forQuotes.concat(marketsVal.concat("\",")));
			}
			inputValue = inputValue.substring(0, inputValue.length() - 1);
			inputMap.put(TemenosConstants.MARKETS, inputValue);
		} else {
			PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.MARKETS);
		}

		try {

			String createResponse = null;
			String serviceName = ServiceId.WEALTHMKTORCHESTRATION;
			String operationName = OperationName.GETDAILYMARKET;

			try {
				createResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
						.withOperationId(operationName).withRequestParameters(inputMap).withRequestHeaders(headersMap)
						.withDataControllerRequest(request).build().getResponse();
				JSONObject resultJSON = new JSONObject(createResponse);
				return Utilities.constructResultFromJSONObject(resultJSON);
			} catch (Exception e) {
				alert.prepareError("Error while invoking Service - "
						+ PortfolioWealthAPIServices.WEALTH_GETMARKETDATA.getOperationName() + "  : " + e).log();
			}

			return null;

		} catch (Exception e) {
			alert.prepareError("Error while invoking Transact - " + OperationName.GETDAILYMARKET + "  : " + e).log();
			return null;

		}
	}

}
