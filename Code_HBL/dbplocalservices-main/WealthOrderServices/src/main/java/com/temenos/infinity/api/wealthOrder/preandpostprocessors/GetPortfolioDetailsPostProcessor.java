package com.temenos.infinity.api.wealthOrder.preandpostprocessors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.dbputilities.sessionmanager.SessionScope;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthOrder.config.WealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * (INFO) If status is set as a part of the request , the operation is exited
 * else operation is executed.
 * 
 * @author balaji.krishnan
 *
 */

public class GetPortfolioDetailsPostProcessor implements DataPostProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		try {
			Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);

			String[] portfolioIdArr = null, graphDurationArr = null;
			double marketValCount = 0, totalMarketValue = 0, cashBalance = 0.00;
			String portfolioId = "", assetType = "", assetDesc = "", referenceCurrency = "",
					totalCashBalanceCurrency = "", tempMarketVal = "", tempassetGroup = "", unRealizedPLAmount = "",
					unRealizedPLPercentage = "", unRealizedPL = "", graphDuration = "", marketVal = "",
					totalCashBalance = "0.00", currency = "", accountName = "", accountNumber = "",
					accountNameWithCurrency = "",accountNameHyphenWithCurrency = "";

			JSONObject responseJSON = new JSONObject();
			JSONObject instotalObj = new JSONObject();
			JSONArray finalArr = new JSONArray();
			JSONArray assetobjArray = new JSONArray();
			JSONArray instotalobjArray = new JSONArray();

			JSONArray assetArray = new JSONArray();
			LinkedHashSet<String> lh = new LinkedHashSet<String>();
			ArrayList<String> assetGroupList = new ArrayList<String>();
			ArrayList<String> marketValueList = new ArrayList<String>();

			portfolioIdArr = request.getParameterValues(TemenosConstants.PORTFOLIOID);
			graphDurationArr = request.getParameterValues(TemenosConstants.GRAPHDURATION);
			if (portfolioIdArr != null && portfolioIdArr.length > 0) {
				portfolioId = portfolioIdArr[0].trim();
			}

			if (graphDurationArr != null && graphDurationArr.length > 0) {
				graphDuration = graphDurationArr[0].trim();
			}

			Record headerRec = result.getRecordById("header");
			String statusVal = headerRec.getParamValueByName("status");

			if (statusVal != null && statusVal.trim().equalsIgnoreCase("success")) {

				Dataset ds = result.getDatasetById("body");
				if (ds != null) {

					JSONObject resultObj = new JSONObject();
					resultObj.put("Field",
							Utilities.convertStringToJSONArray(ResultToJSON.convertDataset(ds).toString()));
					JSONArray refnitivArray = resultObj.getJSONArray("Field");

					try {
						if (refnitivArray != null && refnitivArray.length() > 0) {
							List<String> assetcolArray = Arrays.asList("referenceCurrency", "assetType",
									"assetTypeDesc", "valueInReferenceCurrency");

							String[] insTotalcolArray = new String[] { "referenceCurrency", "marketValue",
									"unRealizedPLAmount", "unRealizedPLPercentage", "portfolioId", "portfolioName" };

							String[] cashAcccolArray = new String[] { "balance", "accountName",
									"referenceCurrencyValue", "currency", "accountNumber", "referenceCurrency" };

							for (int i = 0; i < refnitivArray.length(); i++) {
								// Assets
//								JSONObject assetObjNew = refnitivArray.getJSONObject(i);
								JSONObject assetObj = refnitivArray.getJSONObject(i);
								JSONObject cashAccountsObj = refnitivArray.getJSONObject(i);
								instotalObj = refnitivArray.getJSONObject(i);
//								Set<String> keys = assetObjNew.keySet();
//								for(String key:keys) {
//									if (!assetcolArray.contains(key)) {
//										assetObj.remove(key);
//									}
//								}
//								
								if (assetObj != null && assetObj.length() > 0) {
									for (String key : assetcolArray) {
										if (!assetObj.has(key)) {
											assetObj.put(key, "");
										}
										if (key.trim().equalsIgnoreCase("assetType")) {
											assetType = assetObj.get(key).toString();
											if (assetType.trim().length() > 0) {
												lh.add(assetType);
											}
										}
										if (key.trim().equalsIgnoreCase("referenceCurrency")) {
											if (assetObj.get(key).toString().trim().length() > 0) {
												referenceCurrency = assetObj.get(key).toString();
											}
										}
									}
									assetobjArray.put(assetObj);
								}

								// Instrument Total

								if (instotalObj != null && instotalObj.length() > 0) {

									for (String key : insTotalcolArray) {
										if (!instotalObj.has(key)) {
											instotalObj.put(key, "");
										}
										if (key.trim().equalsIgnoreCase("unRealizedPLAmount")) {
											unRealizedPLAmount = instotalObj.get(key).toString().trim().length() > 0
													? (instotalObj.get(key).toString())
													: "0";
											unRealizedPL = Double.parseDouble(unRealizedPLAmount) >= 0 ? "P" : "L";
											instotalObj.put("unRealizedPL", unRealizedPL);
											instotalObj.put(key, unRealizedPLAmount);
										}
										if (key.trim().equalsIgnoreCase("marketValue")) {
											marketVal = instotalObj.get(key).toString().trim().length() > 0
													? (instotalObj.get(key).toString())
													: "0";
											instotalObj.put(key, marketVal);
										}
										if (key.trim().equalsIgnoreCase("unRealizedPLPercentage")) {
											unRealizedPLPercentage = String.format("%.2f",
													Double.parseDouble(
															instotalObj.get(key).toString().trim().length() > 0
																	? (instotalObj.get(key).toString())
																	: "0"));
											instotalObj.put("unRealizedPLPercentage", unRealizedPLPercentage);
										}
//										if (key.trim().equalsIgnoreCase("portfolioId")) {
//											if (instotalObj.get(key).toString().trim().length() > 0) {
//												instotalObj.put("accountNumber", instotalObj.get(key).toString());
//											}
//										}
//										if (key.trim().equalsIgnoreCase("portfolioName")) {
//											if (instotalObj.get(key).toString().trim().length() > 0) {
//												instotalObj.put("accountName", instotalObj.get(key).toString());
//											}
//										}
									}
									if ((instotalObj.get("portfolioId").toString().length() > 0)
											&& (instotalObj.get("portfolioName").toString().length() > 0)) {

										JSONArray objGraph = new JSONArray();
										instotalObj.put("portfolioID", portfolioId);
										instotalObj.put("todayPL", "");
										instotalObj.put("todayPLAmount", "");
										instotalObj.put("todayPLPercentage", "");
										instotalObj.put(graphDuration, objGraph);
										instotalObj.put("graphDuration", graphDuration);
										instotalObj.put("opstatus", "0");
										instotalObj.put("httpStatusCode", "200");
										instotalObj.put("status", statusVal);
										instotalobjArray.put(instotalObj);

									}

								}

								//////////// Cash Accounts

								if (cashAccountsObj != null && cashAccountsObj.length() > 0) {
									for (String key : cashAcccolArray) {
										if (!cashAccountsObj.has(key)) {
											cashAccountsObj.put(key, "");
										}
										if (key.trim().equalsIgnoreCase("referenceCurrency")) {
											if (cashAccountsObj.get(key).toString().length() > 0) {
												totalCashBalanceCurrency = cashAccountsObj.get("referenceCurrency")
														.toString();
											}
										}
									}

									if ((cashAccountsObj.get("accountName").toString().length() > 0)
											&& (cashAccountsObj.get("currency").toString().length() > 0)
											&& (cashAccountsObj.get("accountNumber").toString().length() > 0)) {

										String amountInRefCurrency = cashAccountsObj.get("referenceCurrencyValue")
												.toString().trim().length() > 0
														? cashAccountsObj.get("referenceCurrencyValue").toString()
														: "0";

										cashBalance = Double.parseDouble(totalCashBalance)
												+ Double.parseDouble(amountInRefCurrency);
										totalCashBalance = String.format("%.2f", cashBalance);

										currency = cashAccountsObj.get("currency").toString();
										accountName = cashAccountsObj.get("accountName").toString();
										accountNumber = cashAccountsObj.get("accountNumber").toString();
										if (accountName.length() > 26) {
											if (accountNumber.length() >= 4) {
												accountNameWithCurrency = currency + " " + accountName.substring(0, 23)
														+ "..." + accountNumber.substring((accountNumber.length() - 4),
																accountNumber.length());
												accountNameHyphenWithCurrency = currency + " "
														+ accountName.substring(0, 23) + "-" + accountNumber.substring(
																(accountNumber.length() - 4), accountNumber.length());
											} else {

												accountNameWithCurrency = currency + " " + accountName.substring(0, 23)
														+ "..." + accountNumber;
												accountNameHyphenWithCurrency = currency + " "
														+ accountName.substring(0, 23) + "-" + accountNumber;

											}

										} else {
											if (accountNumber.length() >= 4) {
												accountNameWithCurrency = currency + " " + accountName + "..."
														+ accountNumber.substring((accountNumber.length() - 4),
																accountNumber.length());

												accountNameHyphenWithCurrency = currency + " " + accountName + "-"
														+ accountNumber.substring((accountNumber.length() - 4),
																accountNumber.length());
											} else {
												accountNameWithCurrency = currency + " " + accountName + "..."
														+ accountNumber;

												accountNameHyphenWithCurrency = currency + " " + accountName + "-"
														+ accountNumber;
											}
										}

										cashAccountsObj.put("accountNameHyphenWithCurrency", accountNameHyphenWithCurrency);
										cashAccountsObj.put("accountNameWithCurrency", accountNameWithCurrency);

										finalArr.put(cashAccountsObj);
									}
								}

							}

							// Asset Allocation
							for (String distinctAssetType : lh) {
								marketValCount = 0;
								for (int i = 0; i < assetobjArray.length(); i++) {
									JSONObject assetdistinctObj = assetobjArray.getJSONObject(i);
									assetType = assetdistinctObj.get("assetType").toString();
									if (distinctAssetType.trim().equalsIgnoreCase(assetType)) {
										marketValCount = marketValCount + Double.parseDouble(assetdistinctObj
												.get("valueInReferenceCurrency").toString().trim().length() > 0
														? assetdistinctObj.get("valueInReferenceCurrency").toString()
														: "0");
										assetDesc = assetdistinctObj.get("assetTypeDesc").toString();

									}

								}
								assetGroupList.add(assetDesc);
								marketValueList.add(String.format("%.2f", marketValCount));
								totalMarketValue = totalMarketValue + marketValCount;
							}

							String assetGroup[] = assetGroupList.toArray(new String[assetGroupList.size()]);
							String marketValue[] = marketValueList.toArray(new String[marketValueList.size()]);

							for (int i = 0; i < marketValue.length; i++) {
								for (int j = i + 1; j < marketValue.length; j++) {
									if (Math.abs(Double.parseDouble(marketValue[i])) < Math
											.abs(Double.parseDouble(marketValue[j]))) {
										tempMarketVal = marketValue[i];
										marketValue[i] = marketValue[j];
										marketValue[j] = tempMarketVal;

										tempassetGroup = assetGroup[i];
										assetGroup[i] = assetGroup[j];
										assetGroup[j] = tempassetGroup;
									}
								}
							}

							for (int i = 0; i < assetGroup.length; i++) {
								JSONObject assetObj = new JSONObject();
								if (Double.parseDouble(marketValue[i]) != 0) {
									assetObj.put(TemenosConstants.ASSETGROUP, assetGroup[i]);
									assetObj.put(TemenosConstants.MARKETVALUE, marketValue[i]);
									assetArray.put(assetObj);
								}
							}

							responseJSON.put(TemenosConstants.REFERENCECURRENCY, referenceCurrency);
							responseJSON.put(TemenosConstants.TOTALMARKETVALUE, totalMarketValue);
							responseJSON.put(TemenosConstants.ASSETS, assetArray);

							// Instrument Total

							responseJSON.put("instrumentTotal", instotalobjArray);

							// Cash Accounts
							responseJSON.put("opstatus", "0");
							responseJSON.put("totalCashBalance", totalCashBalance);
							responseJSON.put("totalCashBalanceCurrency", totalCashBalanceCurrency);
							responseJSON.put("portfolioID", portfolioId);
							responseJSON.put("httpStatusCode", "200");
							responseJSON.put("cashAccounts", finalArr);
							responseJSON.put("status", statusVal);

							responseJSON.put("opstatus", "0");
							responseJSON.put("httpStatusCode", "200");
							responseJSON.put("status", statusVal);

							if (!userPermissions.contains("WEALTH_PORTFOLIO_DETAILS_ASSET_ALLOCATION_VIEW")) {
								responseJSON.put(TemenosConstants.ASSETS, new JSONArray());
							}
							if (!userPermissions.contains("WEALTH_PORTFOLIO_DETAILS_CASH_BALANCE_VIEW")) {
								responseJSON.put("cashAccounts", new JSONArray());
							}
							if (!userPermissions.contains("WEALTH_PORTFOLIO_DETAILS_SUMMARY_VIEW")) {
								responseJSON.put("instrumentTotal", new JSONArray());
							}

						}
					} catch (Exception e) {
						e.getMessage();
					}
				}
			} else {

				Record errorRec = result.getRecordById("error");
				if (errorRec != null) {
					Record error = errorRec.getAllRecords().get(0);

					responseJSON.put("errormessage", error.getParamValueByName("message"));
				} else {
					responseJSON.put("errormessage", "");
				}
				responseJSON.put("status", statusVal);
			}

			return Utilities.constructResultFromJSONObject(responseJSON);
		} catch (

		Exception e) {

			alert.prepareError("Error while invoking GetOrderDetailsPostProcessor - "
					+ WealthAPIServices.WEALTHSERVICESORCHESTRATION.getOperationName() + "  : " + e).log();
		}

		return result;
	}

}
