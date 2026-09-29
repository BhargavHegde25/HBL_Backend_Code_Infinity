/**
 * 
 */
package com.temenos.infinity.wealth.tap.processor.post;

import com.temenos.logger.Logger;
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

/**
 * @author himaja.sridhar
 *
 */
public class GetCashAccountsTAPOrchPostProcessor implements DataPostProcessor2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		diagnostic.prepareDebug("==========> GetCashAccountsTAPOrchPostProcessor TAP - Entered ").log();
		Dataset paySet = result.getDatasetById("payInstructions");
		Dataset cashSet = result.getDatasetById("cashAccounts");
		String accNumb = result.getParamValueByName("accNumb");
		String currency = "", accountName = "", accountNameWithCurrency = "", accountNameHyphenWithCurrency = "";

		if (cashSet != null) {
			JSONArray cashArr = ResultToJSON.convertDataset(cashSet);
			diagnostic.prepareDebug("==========> GetCashAccountsTAPOrchPostProcessor TAP - Cash accounts available.Count: "+cashArr.length()).log();
			if (paySet != null) {
				JSONArray payArr = ResultToJSON.convertDataset(paySet);
				diagnostic.prepareDebug("==========> GetCashAccountsTAPOrchPostProcessor TAP - Pay instructions accounts available.Count: "+payArr.length()).log();
				for (int k = 0; k < payArr.length(); k++) {
					JSONObject payObj = payArr.getJSONObject(k);
					String payString = payObj.get("accountNumber").toString();
					if (accNumb.contains(payString)) {
					} else {
						String acc = payObj.get("accountNumber").toString();
						currency = payObj.has("currency") ? payObj.get("currency").toString() : "";
						accountName = payObj.has("accountName") ? payObj.get("accountName").toString() : "";
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

						payObj.put("accountNameWithCurrency", accountNameWithCurrency);
						payObj.put("accountNameHyphenWithCurrency", accountNameHyphenWithCurrency);
						payObj.put("balance", "");
						payObj.put("referenceCurrencyValue", "");
						Record payRec = Utilities.constructRecordFromJSONObject(payObj);
						result.getDatasetById("cashAccounts").addRecord(payRec);
					}
				}
			} else {

			}
			result.removeDatasetById("payInstructions");
			result.removeParamByName("accNumb");
		}
		diagnostic.prepareDebug("==========> GetCashAccountsTAPOrchPostProcessor TAP - Exited ").log();
		return result;
	}
}
