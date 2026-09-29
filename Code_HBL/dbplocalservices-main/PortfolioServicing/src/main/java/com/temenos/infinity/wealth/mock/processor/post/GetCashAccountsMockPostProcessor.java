/**
 * 
 */
package com.temenos.infinity.wealth.mock.processor.post;

import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

/**
 * @author himaja.sridhar
 *
 */
public class GetCashAccountsMockPostProcessor implements DataPostProcessor2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		
		diagnostic.prepareDebug("==========> GetCashAccountsMockPostProcessor Mock - Entered ").log();
		JSONObject responseVal = new JSONObject();
		JSONArray cashArray = new JSONArray();
		String portfolioId = (String) request.getParameter(TemenosConstants.PORTFOLIOID);
		String[] currency = null, balance = null, accountName = null, currencyName = null,
				referenceCurrencyValue = null, accountNumber;
		String totalCashBalance = "", totalCashBalanceCurrency = "";
		if (portfolioId.equalsIgnoreCase("100777-1")) {
			currency = new String[] { "USD", "EUR" };
			balance = new String[] { "10312.11", "4142.79" };
			accountName = new String[] { "John Bailey A/c 1", "John Bailey A/c 2" };
			accountNumber = new String[] { "11098", "13465" };
			currencyName = new String[] { "United States Dollar", "Euro" };
			referenceCurrencyValue = new String[] { "10312.11", "5016.50" };
			totalCashBalance = "15328.61";
			totalCashBalanceCurrency = "USD";
		} else if (portfolioId.equalsIgnoreCase("100777-2")) {
			currency = new String[] { "USD", "EUR" };
			balance = new String[] { "172.40", "112.79" };
			accountName = new String[] { "John Bailey A/c 1", "John Bailey A/c 2" };
			accountNumber = new String[] { "11156", "16325" };
			currencyName = new String[] { "United States Dollar", "Euro" };
			referenceCurrencyValue = new String[] { "172.40", "136.58" };
			totalCashBalance = "308.98";
			totalCashBalanceCurrency = "USD";
		} else if (portfolioId.equalsIgnoreCase("100777-3")) {
			currency = new String[] { "USD" };
			balance = new String[] { "9560.00" };
			accountName = new String[] { "John Bailey A/c 3" };
			accountNumber = new String[] { "12573" };
			currencyName = new String[] { "United States Dollar" };
			referenceCurrencyValue = new String[] { "9560.00" };
			totalCashBalance = "9560.00";
			totalCashBalanceCurrency = "USD";
		} else if (portfolioId.equalsIgnoreCase("100777-4")) {
			currency = new String[] { "USD", "EUR" };
			balance = new String[] { "10312.11", "4142.79" };
			accountName = new String[] { "John Bailey A/c 4", "John Bailey A/c 5" };
			accountNumber = new String[] { "120057", "120065" };
			currencyName = new String[] { "United States Dollar", "Euro" };
			referenceCurrencyValue = new String[] { "10312.11", "5016.50" };
			totalCashBalance = "15328.61";
			totalCashBalanceCurrency = "USD";
		} else if (portfolioId.equalsIgnoreCase("100777-5")) {
			currency = new String[] { "USD", "EUR" };
			balance = new String[] { "172.40", "112.79" };
			accountName = new String[] { "John Bailey A/c 6", "John Bailey A/c 7" };
			accountNumber = new String[] { "11992", "11982" };
			currencyName = new String[] { "United States Dollar", "Euro" };
			referenceCurrencyValue = new String[] { "172.40", "136.58" };
			totalCashBalance = "308.98";
			totalCashBalanceCurrency = "USD";
		} else {
			currency = new String[] { "USD", "EUR" };
			balance = new String[] { "10312.11", "4142.79" };
			accountName = new String[] { "John Bailey A/c 1", "John Bailey A/c 2" };
			accountNumber = new String[] { "11098", "13465" };
			currencyName = new String[] { "United States Dollar", "Euro" };
			referenceCurrencyValue = new String[] { "10312.11", "5016.50" };
			totalCashBalance = "15328.61";
			totalCashBalanceCurrency = "USD";
		}

		for (int i = 0; i < currency.length; i++) {
			JSONObject cashObj = new JSONObject();
			cashObj.put(TemenosConstants.CURRENCY, currency[i]);
			cashObj.put(TemenosConstants.BALANCE, balance[i]);
			cashObj.put(TemenosConstants.ACCOUNTNAME, accountName[i]);
			cashObj.put(TemenosConstants.ACCOUNTID, accountNumber[i]);
			cashObj.put(TemenosConstants.CURRENCYNAME, currencyName[i]);
			cashObj.put(TemenosConstants.REFERENCECURRENCYVALUE, referenceCurrencyValue[i]);
			cashArray.put(cashObj);
		}
		responseVal.put("opstatus", "0");
		responseVal.put("totalCashBalance", totalCashBalance);
		responseVal.put("totalCashBalanceCurrency", totalCashBalanceCurrency);
		responseVal.put("portfolioID", portfolioId);
		responseVal.put("httpStatusCode", "200");
		responseVal.put("cashAccounts", cashArray);

		diagnostic.prepareDebug("==========> GetCashAccountsMockPostProcessor Mock -  No. of cash accounts returned: "+cashArray.length()).log();

		Result final_result = Utilities.constructResultFromJSONObject(responseVal);
		final_result.addOpstatusParam("0");
		final_result.addHttpStatusCodeParam("200");
		final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		diagnostic.prepareDebug("==========> GetCashAccountsMockPostProcessor Mock - Exited ").log();
		return final_result;

	}

}
