/**
 * 
 */
package com.temenos.infinity.api.wealth.tap.preandpostprocessors;

import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/**
 * @author himaja.sridhar
 *
 */
public class GetCashAccountsOrchPostProcessor implements DataPostProcessor2 {

	@SuppressWarnings("unused")
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Dataset paySet = result.getDatasetById("payInstructions");
		Dataset cashSet = result.getDatasetById("cashAccounts");
		String accNumb = result.getParamValueByName("accNumb");
		String currency = "", accountName = "", accountNameWithCurrency = "", accountNameHyphenWithCurrency = "";

		if (cashSet != null) {
			JSONArray cashArr = ResultToJSON.convertDataset(cashSet);
			if (paySet != null) {
				JSONArray payArr = ResultToJSON.convertDataset(paySet);
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

		return result;
	}

}
