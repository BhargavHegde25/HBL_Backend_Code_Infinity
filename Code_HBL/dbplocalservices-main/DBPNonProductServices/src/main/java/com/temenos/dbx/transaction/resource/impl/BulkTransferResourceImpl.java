package com.temenos.dbx.transaction.resource.impl;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.transaction.businessdelegate.api.BulkTransferBusinessDelegate;
import com.temenos.dbx.transaction.resource.api.BulkTransferResource;


public class BulkTransferResourceImpl implements BulkTransferResource{
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Result createBulkTransfer(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		
		Result result = new Result();
		// Initialization of business Delegate Class
		BulkTransferBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(BulkTransferBusinessDelegate.class);
		
		try {
			
			@SuppressWarnings("unchecked")
			Map<String, Object> inputDetails = (HashMap<String, Object>) inputArray[1];
//			JsonObject convertedObject = new Gson().fromJson(inputDetails.get("bulkTransferString").toString(),
//					JsonObject.class);
//			convertedObject.getAsJsonArray("bulkTransferString");
			JsonArray resultarray = new Gson().fromJson(inputDetails.get("bulkTransferString").toString(),JsonArray.class);
			Map<String, Object> bulkTransferparsedRequest = new HashMap<String, Object>();
			List<Map<String, Object>> inputs = getFormattedInput(resultarray);			
			if (inputs.size() >= 1) {
				_parseBulkTransferRequestInput(inputs, bulkTransferparsedRequest);
			} else {
				bulkTransferparsedRequest = inputs.get(0);
			}
			
			JSONArray resArray = businessDelegate.createBulkTransfer(bulkTransferparsedRequest, request);
			if (resArray == null) {
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
			}
			JSONObject responseObj = new JSONObject();
			responseObj.put("response", resArray);
			result = JSONToResult.convert(responseObj.toString());
		} catch (Exception e) {
			alert.prepareError("Caught exception at CreateBulkTransfer " + e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(result);
		}
		
		return result;
	}
	
	private List<Map<String, Object>> getFormattedInput(JsonArray jArray) {
		List<Map<String, Object>> inputs = new ArrayList<>();
		Type type = new TypeToken<Map<String, Object>>() {
		}.getType();
		Gson gson = new Gson();
		if (jArray.isJsonArray()) {
			for (int i = 0; i < jArray.size(); i++) {
				@SuppressWarnings("unchecked")
				Map<String, Object> temp = (Map<String, Object>) gson.fromJson(jArray.get(i), type);
				inputs.add(temp);
			}
		}
		return inputs;
	}
	
	private void _parseBulkTransferRequestInput(List<Map<String, Object>> records,
			Map<String, Object> bulkTransferparsedRequest) {
			StringBuffer amount = new StringBuffer();
			StringBuffer fromAccountNumber = new StringBuffer();
			StringBuffer frequencyEndDate = new StringBuffer();
			StringBuffer frequencyType = new StringBuffer();
			StringBuffer scheduledDate = new StringBuffer();
			StringBuffer transactionsNotes = new StringBuffer();
			StringBuffer transactionType = new StringBuffer();
			StringBuffer swiftCode = new StringBuffer();
			StringBuffer isScheduled = new StringBuffer();
			StringBuffer toAccountNumber = new StringBuffer();
			StringBuffer paymentType = new StringBuffer();
			StringBuffer paidBy = new StringBuffer();
			StringBuffer beneficiaryName = new StringBuffer();
			StringBuffer beneficiaryAddressNickName = new StringBuffer();
			StringBuffer beneficiaryAddressLine1 = new StringBuffer();
			StringBuffer beneficiaryCity = new  StringBuffer();
			StringBuffer beneficiaryZipcode = new StringBuffer();
			StringBuffer beneficiarycountry = new StringBuffer();
			StringBuffer numberOfRecurrences = new StringBuffer();
			StringBuffer iban = new StringBuffer();
			StringBuffer bankName = new StringBuffer();
			StringBuffer transactionCurrency = new StringBuffer();
			StringBuffer fromAccountCurrency = new StringBuffer();
			StringBuffer toAccountCurrency = new StringBuffer();
			StringBuffer feeCurrency = new StringBuffer();
			StringBuffer ExternalAccountNumber = new StringBuffer();
			StringBuffer frequencyStartDate = new StringBuffer();
			StringBuffer feeAmount = new StringBuffer();
			StringBuffer initiationId = new StringBuffer();
			StringBuffer serviceName = new StringBuffer();

			int id = 0;

			for (Map<String, Object> input : records) {
				amount = amount.append(input.get("amount") + ",");
				fromAccountNumber = fromAccountNumber.append(input.get("fromAccountNumber") + ",");
				scheduledDate = scheduledDate.append(input.get("scheduledDate") + ",");
				transactionsNotes = transactionsNotes.append(input.get("transactionsNotes") + ",");
				transactionType = transactionType.append(input.get("transactionType") + ",");
				frequencyEndDate = frequencyEndDate.append(input.get("frequencyEndDate") + ",");
				frequencyType = frequencyType.append(input.get("frequencyType") + ",");
				isScheduled = isScheduled.append(input.get("isScheduled") + ",");
				toAccountNumber = toAccountNumber.append(input.get("toAccountNumber") + ",");
				swiftCode = swiftCode.append(input.get("swiftCode") + ",");
				paymentType = paymentType.append(input.get("paymentType") + ",");
				paidBy = paidBy.append(input.get("paidBy") + ",");
				beneficiaryName = beneficiaryName.append(input.get("beneficiaryName") + ",");
				bankName = bankName.append(input.get("bankName") + ",");
				feeAmount = feeAmount.append(input.get("feeAmount") + ",");
				frequencyStartDate = frequencyStartDate.append(input.get("frequencyStartDate") + ",");
				ExternalAccountNumber = ExternalAccountNumber.append(input.get("ExternalAccountNumber") + ",");
				fromAccountCurrency = fromAccountCurrency.append(input.get("fromAccountCurrency") + ",");
				toAccountCurrency = toAccountCurrency.append(input.get("toAccountCurrency") + ",");
				feeCurrency = feeCurrency.append(input.get("feeCurrency") + ",");
				transactionCurrency = transactionCurrency.append(input.get("transactionCurrency") + ",");
				iban = iban.append(input.get("iban") + ",");
				numberOfRecurrences = numberOfRecurrences.append(input.get("numberOfRecurrences") + ",");
				beneficiarycountry = beneficiarycountry.append(input.get("beneficiarycountry") + ",");
				beneficiaryZipcode = beneficiaryZipcode.append(input.get("beneficiaryZipcode") + ",");
				beneficiaryCity = beneficiaryCity.append(input.get("beneficiaryCity") + ",");
				beneficiaryAddressLine1 = beneficiaryAddressLine1.append(input.get("beneficiaryAddressLine1") + ",");
				beneficiaryAddressNickName = beneficiaryAddressNickName.append(input.get("beneficiaryAddressNickName") + ",");
				initiationId = initiationId.append(id + ",");
				serviceName = serviceName.append(input.get("serviceName") + ",");
				id++;
			}
			bulkTransferparsedRequest.put("amount", amount.toString());
			bulkTransferparsedRequest.put("fromAccountNumber", fromAccountNumber.toString());
			bulkTransferparsedRequest.put("scheduledDate", scheduledDate.toString());
			bulkTransferparsedRequest.put("transactionsNotes", transactionsNotes.toString());
			bulkTransferparsedRequest.put("transactionType", transactionType.toString());
			bulkTransferparsedRequest.put("frequencyEndDate", frequencyEndDate.toString());
			bulkTransferparsedRequest.put("frequencyType", frequencyType.toString());
			bulkTransferparsedRequest.put("beneficiaryAddressNickName", beneficiaryAddressNickName.toString());
			bulkTransferparsedRequest.put("beneficiaryName", beneficiaryName.toString());
			bulkTransferparsedRequest.put("swiftCode", swiftCode.toString());
			bulkTransferparsedRequest.put("beneficiaryCity", beneficiaryCity.toString());
			bulkTransferparsedRequest.put("beneficiaryZipcode", beneficiaryZipcode.toString());
			bulkTransferparsedRequest.put("beneficiarycountry", beneficiarycountry.toString());
			bulkTransferparsedRequest.put("bankName", bankName.toString());
			bulkTransferparsedRequest.put("numberOfRecurrences", numberOfRecurrences.toString());
			bulkTransferparsedRequest.put("iban", iban.toString());
			bulkTransferparsedRequest.put("transactionCurrency", transactionCurrency.toString());
			bulkTransferparsedRequest.put("fromAccountCurrency", fromAccountCurrency.toString());
			bulkTransferparsedRequest.put("toAccountCurrency", toAccountCurrency.toString());
			bulkTransferparsedRequest.put("ExternalAccountNumber", ExternalAccountNumber.toString());
			bulkTransferparsedRequest.put("frequencyStartDate", frequencyStartDate.toString());
			bulkTransferparsedRequest.put("feeAmount", feeAmount.toString());
			bulkTransferparsedRequest.put("feeCurrency", feeCurrency.toString());
			bulkTransferparsedRequest.put("paidBy", paidBy.toString());
			bulkTransferparsedRequest.put("paymentType", paymentType.toString());
			bulkTransferparsedRequest.put("toAccountNumber", toAccountNumber.toString());
			bulkTransferparsedRequest.put("isScheduled", isScheduled.toString());
			bulkTransferparsedRequest.put("initiationId", initiationId.toString());
			bulkTransferparsedRequest.put("loop_count", Integer.toString(id));
			bulkTransferparsedRequest.put("serviceName", serviceName.toString());
		}

}
