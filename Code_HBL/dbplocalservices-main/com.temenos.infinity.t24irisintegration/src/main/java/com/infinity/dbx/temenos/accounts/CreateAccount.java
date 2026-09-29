package com.infinity.dbx.temenos.accounts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.infinity.dbx.temenos.auth.Authentication;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.utils.ProductLine;
import com.kony.dbputilities.util.TokenUtils;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class CreateAccount implements JavaService2, AccountsConstants {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {

		Result result = new Result();
		try {
			HashMap<String, Object> params = (HashMap<String, Object>) inputArray[1];

			String applicantCif = CommonUtils.getParamValue(params, "applicantCif");
			String coApplicantCif = CommonUtils.getParamValue(params, "coApplicantCif");
			String productId = CommonUtils.getParamValue(params, "productId");
			String productLineStr = CommonUtils.getParamValue(params, "productLine");
			ProductLine productLine = ProductLine.valueOf(productLineStr.toUpperCase());
			String currencyId = CommonUtils.getParamValue(params, "currencyId");
			String featuresStr = CommonUtils.getParamValue(params, "features");
			String companyId = CommonUtils.getParamValue(params, "companyId"); 
			JsonArray customerIds = new JsonArray();
			JsonArray coApplicants = new JsonArray();
			JsonObject applicantObj = new JsonObject();
			String limitAmount = CommonUtils.getParamValue(params, "limitAmount");
			String purpose = CommonUtils.getParamValue(params, "purpose");
			String settlementAccount = CommonUtils.getParamValue(params, "settlementAccount");
			
			JSONArray features = new JSONArray();
			if (StringUtils.isNotEmpty(featuresStr) && !featuresStr.equalsIgnoreCase("$features"))
				features = new JSONArray(featuresStr);

			String term = CommonUtils.getParamValue(params, "term");
			String amount = CommonUtils.getParamValue(params, "amount");
			String disbursedAmount = CommonUtils.getParamValue(params, "disbursedAmount");

			// Basic details for Arrangement creation
			Map<String, Object> payload = new HashMap<String, Object>();
			applicantObj.addProperty("customerId", applicantCif);
			applicantObj.addProperty("customerRole", CommonUtils.getProperty(ACCOUNT_PROPS, "ARRANGEMENT", "APPLICANT", "ROLE"));
			customerIds.add(applicantObj);
			
			boolean additionalOwnerExists = false;
			if (StringUtils.isNotEmpty(coApplicantCif)) {
				coApplicants = new JsonParser().parse(coApplicantCif).getAsJsonArray();
				for (JsonElement coApplicant : coApplicants) {
					JsonObject coApplicantObj = coApplicant.getAsJsonObject();
					applicantObj = new JsonObject();
					applicantObj.addProperty("customerId", coApplicantObj.get("cif").getAsString());
					applicantObj.addProperty("customerRole", coApplicantObj.get("type").getAsString());
					if(coApplicantObj.get("type").getAsString().equalsIgnoreCase("OWNER")) {
						additionalOwnerExists = true;
					}
					customerIds.add(applicantObj);
				}
			}

			if (additionalOwnerExists) {
				JSONArray customerArray = new JSONArray();
				JSONObject customerDataObj = new JSONObject();
				customerDataObj.put("taxLiabilityPercentage", "100");
				customerArray.put(customerDataObj);
				for (int i = 0; i < new JsonParser().parse(coApplicantCif).getAsJsonArray().size(); i++) {
					customerDataObj = new JSONObject();
					customerDataObj.put("taxLiabilityPercentage", "0");
					customerArray.put(customerDataObj);
				}
				payload.put("Customer", customerArray.toString());
			}
			payload.put("CustomerIds", customerIds.toString());
			payload.put("ProductId", productId);
			payload.put("CurrencyId", currencyId);
			payload.put("ActivityId", productLine.getNewActivity());
			payload.put("LimitAmount", limitAmount);
			payload.put("Reason", purpose);
			payload.put("SettlementAccount", settlementAccount);

			// Adding payload for facilities property
			if (features.length() > 0) {
				JSONArray productFeatures = new JSONArray();
				features.forEach(item -> {
					JSONObject featureSelected = (JSONObject) item;
					if (featureSelected.getString(FEATURE_ID).equalsIgnoreCase("Printing.Option")) {
						if (featureSelected.getString(FEATURE_VALUE).equalsIgnoreCase("OPT-IN"))
							payload.put("StatementType", "E.STATEMENT");
					} else if (featureSelected.getString(FEATURE_ID).equalsIgnoreCase("Overdraft protection")) {
						if (featureSelected.getString(FEATURE_VALUE).equalsIgnoreCase("OPT-IN"))
							try {
								payload.put("LimitAmount",
										CommonUtils.getProperty(ACCOUNT_PROPS, "ARRANGEMENT", "LIMIT", "AMOUNT"));
							} catch (Exception e) {
								alert.prepareError("Error in CreateAccount:" + e.getMessage()).log();
							}
					} else {
						Integer index = featureSelected.getInt(SEQUENCE_NO) - 1;
						productFeatures.put(index,
								new JSONObject().put("customerOption", featureSelected.getString(FEATURE_VALUE)));
					}
				});

				/*
				 * In the above for each loop, when JSONArray.put(index, value) is invoked, if
				 * index is greater than the current length of JSONArray, it puts JSONArray.NULL
				 * in those indices which contain no values. The below logic is to replace such
				 * occurrences
				 */
				for (int count = 0; count < productFeatures.length(); count++) {
					Object feature = productFeatures.get(count);
					if (feature.equals(JSONObject.NULL)) {
						productFeatures.put(count, new JSONObject().put("customerOption", ""));
					}
				}

				payload.put("Features", productFeatures);
			}

			// Adding payload for deposits product line
			if (productLine == ProductLine.DEPOSITS || productLine == ProductLine.LENDING) {
				payload.put("Term", term);
				payload.put("Amount", amount);
				payload.put("DisbursedAmount", disbursedAmount);
			}
			HashMap<String, Object> headers = new HashMap<String, Object>();
			request.addRequestParam_(TemenosConstants.FLOW_TYPE, TemenosConstants.PRE_LOGIN_FLOW);
			headers.put(TemenosConstants.PARAM_AUTHORIZATION, TokenUtils.getT24AuthToken(request));
			headers.put("companyId", companyId);

			String serviceResponse = CommonUtils.callPassthroughIntegrationService(request, payload, headers,
					TemenosConstants.SERVICE_T24IS_ONBOARDING_ACCOUNTS, TemenosConstants.OP_CREATE_ACCOUNT, false);

			JSONObject responseJSON = new JSONObject(serviceResponse);
			if (responseJSON.getJSONObject(HEADER).getString(TRANSACTION_STATUS).equalsIgnoreCase(LIVE)) {
				JSONObject accountDetails = getAccountDetails(responseJSON);
				String accountNumber = accountDetails.optString("accountId");
				if (accountNumber != null) {
					result.addStringParam("accountId", accountNumber);
					result.addStringParam("effectiveDate", accountDetails.optString("effectiveDate"));
				} else {
					result.addStringParam("dbpErrMsg", "Failed to get account number");
					result.addStringParam("dbpErrCode", "4001");
				}
			} else {
				alert.prepareError("[CreateAccount] Account creation failed with: " + responseJSON.toString()).log();
				result.addStringParam("dbpErrMsg", "Account creation failed");
				result.addStringParam("dbpErrCode", "4000");
				JSONObject errorDetails;
				if (responseJSON.has("error")) {
					errorDetails = (JSONObject) responseJSON.getJSONObject("error").getJSONArray("errorDetails").get(0);
					result.addStringParam("dbpErrMsg", errorDetails.optString("message"));
					result.addStringParam("dbpErrCode", errorDetails.optString("code"));
					alert.prepareError("[CreateAccount] Account creation failed with :" + errorDetails).log();
				}
			}
		} catch (Exception e) {
			alert.prepareError("[CreateAccount] Account creation failed:" + e.toString()).log();
			result.addStringParam("dbpErrMsg", "Account creation failed");
			result.addStringParam("dbpErrCode", "4000");
		}
		return result;

	}

	/**
	 * @param responseJSON AAA Service response
	 * @return account number from AAA service response
	 */
	private JSONObject getAccountDetails(JSONObject responseJSON) {
		JSONObject accountDetails = new JSONObject();
		try {
			JSONObject propertiesObject = responseJSON.getJSONObject(BODY).getJSONObject(PROPERTIES);
			JSONObject arrangementActObject = responseJSON.getJSONObject(BODY).getJSONObject(ARRANGEMENT_ACTIVITY);
			List<String> propertyNames = propertiesObject.keySet().stream().collect(Collectors.toList());
			diagnostic.prepareDebug("[CreateAccount] property names returned:" + propertyNames).log();
			diagnostic.prepareDebug("[CreateAccount] arrangementActivity Object returned:" + arrangementActObject).log();
			String accountProps = CommonUtils.getProperty(ACCOUNT_PROPS, "ARRANGEMENT", "ACCOUNT", "PROPERTIES");
			String[] accountPropertyNames = accountProps.split(",");
			for (String accountProperty : accountPropertyNames) {
				if (propertiesObject.has(accountProperty)) {
					accountDetails.put("accountId",propertiesObject.getJSONObject(accountProperty).getJSONObject(BODY).getString(ACC_ID));
					break;
				}
			}
			accountDetails.put("effectiveDate", arrangementActObject.optString("effectiveDate"));
		} catch (Exception e) {
			alert.prepareError("[CreateAccount] Failed to get get account number" + e.toString()).log();
			return null;
		}
		return accountDetails;
	}

}
