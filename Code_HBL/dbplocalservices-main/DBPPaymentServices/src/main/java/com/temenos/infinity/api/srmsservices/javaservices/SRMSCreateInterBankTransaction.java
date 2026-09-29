package com.temenos.infinity.api.srmsservices.javaservices;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.util.JSONUtils;
import com.google.gson.Gson;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.transactionservices.dto.InterBankFundTransferBackendDTO;
import com.temenos.dbx.product.transactionservices.dto.InterBankFundTransferDTO;
import com.temenos.infinity.api.srmsservices.constants.SRMSConstants;
import com.temenos.infinity.api.srmstransactions.config.SRMSTypeSubTypeConfiguration;
import com.temenos.infinity.api.srmstransactions.dto.SRMSInterBankDTO;

public class SRMSCreateInterBankTransaction implements SRMSConstants, JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		@SuppressWarnings("unchecked")
		Map<String, Object> requestparameters = (Map<String, Object>) inputArray[1];
		HashMap<String, Object> srmsParams = new HashMap<String, Object>();
		
		InterBankFundTransferBackendDTO input1 = JSONUtils.parse(new JSONObject(requestparameters).toString(), InterBankFundTransferBackendDTO.class);
		SRMSInterBankDTO interBankDTOInput = new SRMSInterBankDTO().convert(input1);
		Map<String, Object> filteredSRMSParams = JSONUtils.parseAsMap(new JSONObject(interBankDTOInput).toString(), String.class, Object.class);
		
		String isPending = requestparameters.get("isPending")!=null ? "1" : "0";
		if (isPending.equals("1")) {
			filteredSRMSParams.put(PARAM_NUMBER_OF_AUTHORISERS, "1");
		}

		String frequency = (requestparameters.get(PARAM_FREQUENCY_TYPE) != null)
				? requestparameters.get(PARAM_FREQUENCY_TYPE).toString()
				: "";
		String accountId = (requestparameters.get(PARAM_FROM_ACCOUNT_NUMBER) != null)
				? requestparameters.get(PARAM_FROM_ACCOUNT_NUMBER).toString()
				: "";
		String paymentType = (requestparameters.get(PARAM_PAYMENT_TYPE) != null)
				? requestparameters.get(PARAM_PAYMENT_TYPE).toString()
				: "";

		// Identify Type and SubType
		if (FREQUENCY_ONCE.equalsIgnoreCase(frequency)) {
			switch (paymentType) {
			// Case statements
			case TYPE_INSTPAY:
				srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.ONE_TIME_INTER_INSTANT_TRANSFER.getType());
				srmsParams.put(PARAM_SUB_TYPE,
						SRMSTypeSubTypeConfiguration.ONE_TIME_INTER_INSTANT_TRANSFER.getSubType());
				break;
			case TYPE_SEPA:
				srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.ONE_TIME_INTER_SEPA_TRANSFER.getType());
				srmsParams.put(PARAM_SUB_TYPE, SRMSTypeSubTypeConfiguration.ONE_TIME_INTER_SEPA_TRANSFER.getSubType());
				break;
			case TYPE_FEDWIRE:
				srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.ONE_TIME_INTER_FEDWIRE_TRANSFER.getType());
				srmsParams.put(PARAM_SUB_TYPE,
						SRMSTypeSubTypeConfiguration.ONE_TIME_INTER_FEDWIRE_TRANSFER.getSubType());
				break;
			case TYPE_ACH:
				srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.ONE_TIME_INTER_ACH_TRANSFER.getType());
				srmsParams.put(PARAM_SUB_TYPE, SRMSTypeSubTypeConfiguration.ONE_TIME_INTER_ACH_TRANSFER.getSubType());
				break;
			case TYPE_CHAPS:
				srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.ONE_TIME_INTER_CHAPS_TRANSFER.getType());
				srmsParams.put(PARAM_SUB_TYPE, SRMSTypeSubTypeConfiguration.ONE_TIME_INTER_CHAPS_TRANSFER.getSubType());
				break;
			case TYPE_FASTER_PAYMENT:
				srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.ONE_TIME_INTER_FASTER_TRANSFER.getType());
				srmsParams.put(PARAM_SUB_TYPE,
						SRMSTypeSubTypeConfiguration.ONE_TIME_INTER_FASTER_TRANSFER.getSubType());
				break;
			case TYPE_RINGS:
				srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.ONE_TIME_INTER_RINGS_TRANSFER.getType());
				srmsParams.put(PARAM_SUB_TYPE, SRMSTypeSubTypeConfiguration.ONE_TIME_INTER_RINGS_TRANSFER.getSubType());
				break;
			// Default case statement
			default:
				srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.ONE_TIME_INTER_INSTANT_TRANSFER.getType());
				srmsParams.put(PARAM_SUB_TYPE,
						SRMSTypeSubTypeConfiguration.ONE_TIME_INTER_INSTANT_TRANSFER.getSubType());
				break;
			}
		} else {
			switch (paymentType) {
			// Case statements
			case TYPE_INSTPAY:
				srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.RECURRING_INTER_INSTANT_TRANSFER.getType());
				srmsParams.put(PARAM_SUB_TYPE,
						SRMSTypeSubTypeConfiguration.RECURRING_INTER_INSTANT_TRANSFER.getSubType());
				break;
			case TYPE_SEPA:
				srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.RECURRING_INTER_SEPA_TRANSFER.getType());
				srmsParams.put(PARAM_SUB_TYPE, SRMSTypeSubTypeConfiguration.RECURRING_INTER_SEPA_TRANSFER.getSubType());
				break;
			case TYPE_FEDWIRE:
				srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.RECURRING_INTER_FEDWIRE_TRANSFER.getType());
				srmsParams.put(PARAM_SUB_TYPE,
						SRMSTypeSubTypeConfiguration.RECURRING_INTER_FEDWIRE_TRANSFER.getSubType());
				if(filteredSRMSParams.get("paymentType").equals("FEDWIRE")){
					filteredSRMSParams.put("paymentType","FEDCTI");
				}
				break;
			case TYPE_ACH:
				srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.RECURRING_INTER_ACH_TRANSFER.getType());
				srmsParams.put(PARAM_SUB_TYPE, SRMSTypeSubTypeConfiguration.RECURRING_INTER_ACH_TRANSFER.getSubType());
				if(filteredSRMSParams.get("paymentType").equals("ACH")){
					filteredSRMSParams.put("paymentType","ACHCTI");
				}
				break;
			case TYPE_CHAPS:
				srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.RECURRING_INTER_CHAPS_TRANSFER.getType());
				srmsParams.put(PARAM_SUB_TYPE,
						SRMSTypeSubTypeConfiguration.RECURRING_INTER_CHAPS_TRANSFER.getSubType());
				break;
			case TYPE_FASTER_PAYMENT:
				srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.RECURRING_INTER_FASTER_TRANSFER.getType());
				srmsParams.put(PARAM_SUB_TYPE,
						SRMSTypeSubTypeConfiguration.RECURRING_INTER_FASTER_TRANSFER.getSubType());
				break;
			case TYPE_RINGS:
				srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.RECURRING_INTER_RINGS_TRANSFER.getType());
				srmsParams.put(PARAM_SUB_TYPE,
						SRMSTypeSubTypeConfiguration.RECURRING_INTER_RINGS_TRANSFER.getSubType());
				break;
			// Default case statement
			default:
				srmsParams.put(PARAM_TYPE, SRMSTypeSubTypeConfiguration.RECURRING_INTER_INSTANT_TRANSFER.getType());
				srmsParams.put(PARAM_SUB_TYPE,
						SRMSTypeSubTypeConfiguration.RECURRING_INTER_INSTANT_TRANSFER.getSubType());
				break;
			}
		}

		// Capitalize externalAccountNumber - ExternalAccountNumber
		String ExternalAccountNumber = (filteredSRMSParams.get(PARAM_EXT_ACCT_NUMBER) != null)
				? filteredSRMSParams.get(PARAM_EXT_ACCT_NUMBER).toString()
				: "";
		if (StringUtils.isNotBlank(ExternalAccountNumber)) {
			filteredSRMSParams.remove(PARAM_EXT_ACCT_NUMBER);
			filteredSRMSParams.put(StringUtils.capitalize(PARAM_EXT_ACCT_NUMBER), ExternalAccountNumber);
		}

		// Add Account Number in the request
		if (StringUtils.isNotBlank(accountId))
			srmsParams.put(ACCOUNT_ID, accountId);

		// Add Backend Request Body to the request
		Gson gson = new Gson();
		String json = gson.toJson(filteredSRMSParams); // Convert Map to JSONObject
		srmsParams.put(PARAM_REQUEST_BODY, json.toString().replaceAll("\"", "'"));

		alert.prepareError("Inter Bank Account Transfer SRMS Input :" + srmsParams.toString()).log();
		InterBankFundTransferDTO input;
		Result result = new Result();
		try {
			diagnostic.prepareDebug("Inter Account Create Order Request :" + srmsParams.toString()).log();
			String response1 = Util.createOrder(srmsParams, request);
			diagnostic.prepareDebug("Inter Account Create Order Response :" + response1).log();
			input = JSONUtils.parse(response1, InterBankFundTransferDTO.class);
		} catch (JSONException | IOException e) {
			alert.prepareError("Failed to create inter account fund transfer without approval: ", e).log();
			result.addParam(new Param("dbpErrCode", "{\"errormsg\":\"" + e.getMessage() + "\"}"));
			return result;
		}
		// SRMSInterBankDTO interAccountDTOInput = new
		// SRMSInterBankDTO().convert(input);

		try {
			JSONObject resObj = new JSONObject(input);
			String res = resObj.toString();
			result = JSONToResult.convert(res);
			return result;
			// return JSONUtils.parseAsMap(new JSONObject(interAccountDTOInput).toString(),
			// String.class, Object.class);
		} catch (Exception e) {
			alert.prepareError("Error occured while fetching the input params: ", e).log();
			result.addParam(new Param("dbpErrCode", "{\"errormsg\":\"" + e.getMessage() + "\"}"));
			return result;
		}
	}

}
