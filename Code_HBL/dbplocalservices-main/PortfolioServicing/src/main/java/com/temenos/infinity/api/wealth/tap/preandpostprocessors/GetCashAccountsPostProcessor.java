package com.temenos.infinity.api.wealth.tap.preandpostprocessors;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealth.config.PortfolioWealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * (INFO) Builds the result in desired format for the TAP service
 * 
 * @author himaja.sridhar
 *
 */

public class GetCashAccountsPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("unused")
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			JSONObject cashAccJSON = new JSONObject();
			Record headerRec = result.getRecordById("header");
			Dataset bodyData = result.getDatasetById("body");
			JSONArray cashAccArr = new JSONArray();
			String referenceCurr = "";
			Double marketVal = 0.0, totalMarketVal = 0.0;
			String accountNumber = "", currency = "", accountName = "", accountNameWithCurrency = "",
					accountNameHyphenWithCurrency = "";
			if (bodyData != null) {
				JSONArray cashAcc = ResultToJSON.convertDataset(bodyData);
				for (int i = 0; i < cashAcc.length(); i++) {
					JSONObject cashJSON = cashAcc.getJSONObject(i);
					if (!cashJSON.getString("natureOfInstrument").equalsIgnoreCase("Cash Account")) {
					} else {
						String acc = cashJSON.get("accountNumber").toString();
						accountNumber = accountNumber.equals("") ? accountNumber.concat(acc)
								: accountNumber.concat(",").concat(acc);
						referenceCurr = cashJSON.getString("referenceCurrency");
						marketVal = marketVal
								+ Double.parseDouble(cashJSON.get("ValueValCcy").toString().trim().length() > 0
										? cashJSON.get("ValueValCcy").toString()
										: "0");
						cashJSON.remove("natureOfInstrument");
						cashJSON.remove("referenceCurrency");
						cashJSON.remove("ValueValCcy");
						cashJSON.put("referenceCurrencyValue", "");

						currency = cashJSON.has("currency") ? cashJSON.get("currency").toString() : "";
						accountName = cashJSON.has("accountName") ? cashJSON.get("accountName").toString() : "";
						if (accountName.length() > 26) {
							if (acc.length() >= 4) {
								accountNameWithCurrency = currency + " " + accountName.substring(0, 23) + "..."
										+ acc.substring((acc.length() - 4), acc.length());
								accountNameHyphenWithCurrency = currency + " " + accountName.substring(0, 23) + "-"
										+ acc.substring((acc.length() - 4), acc.length());
							} else {
								accountNameWithCurrency = currency + " " + accountName.substring(0, 23) + "..." + acc;
								accountNameHyphenWithCurrency = currency + " " + accountName.substring(0, 23) + "-"
										+ acc;
							}

						} else {
							if (acc.length() >= 4) {
								accountNameWithCurrency = currency + " " + accountName + "..."
										+ acc.substring((acc.length() - 4), acc.length());

								accountNameHyphenWithCurrency = currency + " " + accountName + "-"
										+ acc.substring((acc.length() - 4), acc.length());
							} else {
								accountNameWithCurrency = currency + " " + accountName + "..." + acc;

								accountNameHyphenWithCurrency = currency + " " + accountName + "-" + acc;
							}
						}

						cashJSON.put("accountNameWithCurrency", accountNameWithCurrency);
						cashJSON.put("accountNameHyphenWithCurrency", accountNameHyphenWithCurrency);

						cashAccArr.put(cashJSON);
					}
				}
			} else {
				cashAccArr = new JSONArray();
			}
			totalMarketVal = totalMarketVal + marketVal;
			// cashAccJSON.put("totalCashBalance", String.format("%.2f",totalMarketVal));
			cashAccJSON.put("totalCashBalance", "");
			cashAccJSON.put("cashAccounts", cashAccArr);
			cashAccJSON.put("totalCashBalanceCurrency", "");
			// cashAccJSON.put("totalCashBalanceCurrency", referenceCurr);
			cashAccJSON.put("portfolioID", request.getParameter("portfolioId"));
			cashAccJSON.put("accNumb", accountNumber);

			Result cashRes = Utilities.constructResultFromJSONObject(cashAccJSON);
			cashRes.addOpstatusParam("0");
			cashRes.addHttpStatusCodeParam("200");
			cashRes.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			return cashRes;

		} catch (Exception e) {
			alert.prepareError("Error while invoking GetCashAccountsPostProcessor - "
					+ PortfolioWealthAPIServices.WEALTHSERVICESORCHESTRATION.getOperationName() + "  : " + e).log();
		}

		return null;
	}

}
