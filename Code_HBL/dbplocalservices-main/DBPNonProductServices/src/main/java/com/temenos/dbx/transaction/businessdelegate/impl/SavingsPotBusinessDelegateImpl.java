package com.temenos.dbx.transaction.businessdelegate.impl;

import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.dbp.core.util.JSONUtils;
import com.temenos.dbx.product.transactionservices.businessdelegate.impl.BulkWireTransactionsBusinessDelegateImpl;
import com.kony.dbputilities.util.CommonUtils;
import com.temenos.dbx.transaction.businessdelegate.api.SavingsPotBusinessDelegate;
import com.temenos.dbx.transaction.dto.SavingsPotDTO;

public class SavingsPotBusinessDelegateImpl implements SavingsPotBusinessDelegate{

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	public SavingsPotDTO createSavingsPot(Map<String, Object> inputParams) {
		JSONObject jsonResponse = new JSONObject();
		SavingsPotDTO savingsPotDTO = null;
		JSONObject inputJSON = new JSONObject(); 
	    inputJSON.put("accountId", inputParams.get("fundingAccountId"));
	    inputJSON.put("potName", inputParams.get("potName"));
	    inputJSON.put("potType", inputParams.get("potType"));
	    inputJSON.put("savingType", inputParams.get("savingsType")  );
	    inputJSON.put("startDate", inputParams.get("startDate"));
	    inputJSON.put("endDate", inputParams.get("endDate"));
	    inputJSON.put("frequency",inputParams.get("frequency"));
	    inputJSON.put("targetAmount", inputParams.get("targetAmount"));
		inputJSON.put("scheduledPaymentAmount", inputParams.get("periodicContribution"));
		//sending  inputJSON as input to create savings Pot service will fetch us the following response
	    
	    try {
		String potContractId = CommonUtils.generateUniqueIDHyphenSeperated(4,16);
		jsonResponse.put("potContractId",potContractId);
		jsonResponse.put("status","Success" );
		jsonResponse.put("message", "Operation completed Successfully");
	    savingsPotDTO = JSONUtils.parse(jsonResponse.toString(), SavingsPotDTO.class);
		}catch(Exception e){
			alert.prepareError("Error In Business Delegate : " + e).log();
            return null;
		}
		return savingsPotDTO;
	}

	@Override
	public SavingsPotDTO closeSavingsPot(String potContractId) {
		JSONObject jsonResponse = new JSONObject();
		SavingsPotDTO savingsPotDTO = null;
		//sending potcontractId to the close service will close the savings pot contract 
		//and expected response after closing successfully is as mentioned below
	    try {
		jsonResponse.put("status","Success" );
		jsonResponse.put("message", "Operation completed Successfully");
	    savingsPotDTO = JSONUtils.parse(jsonResponse.toString(), SavingsPotDTO.class);
		}catch(Exception e){
			alert.prepareError("Error In Business Delegate : " + e).log();
            return null;
		}
		return savingsPotDTO;
	}

	@Override
	public SavingsPotDTO updateSavingsPotBalance(String potContractId, String amount, String isCreditDebit) {
		JSONObject inputJson = new JSONObject();
		//input is assigned according to what transact api is expecting
		//{
        //"potContractId":"ACLK19295039",
        //"transactionAmount" : "-150"
        //}

		inputJson.put("potContractId", potContractId);
		if(isCreditDebit.equalsIgnoreCase("Debit"))
			amount = "-"+amount;
		inputJson.put("transactionAmount", amount);
		JSONObject jsonResponse = new JSONObject();
		SavingsPotDTO savingsPotDTO = null;
		//sending potcontractId to the update service will fund to/withdraw from  the savings pot contract with the amount mentioned in the input.
		//Expected response after closing successfully is as mentioned below
	    try {
		jsonResponse.put("status","Success" );
		jsonResponse.put("message", "Operation completed Successfully");
	    savingsPotDTO = JSONUtils.parse(jsonResponse.toString(), SavingsPotDTO.class);
		}catch(Exception e){
			alert.prepareError("Error In Business Delegate : " + e).log();
            return null;
		}
		return savingsPotDTO;
	}

	@Override
	public SavingsPotDTO updateSavingsPot(Map<String, Object> updateSavingsPotMap) {
		JSONObject jsonResponse = new JSONObject();
		SavingsPotDTO savingsPotDTO = null;
		//sending the updateSavingsPotMap as input to update savings Pot service will fetch us the following response 
		try{
		jsonResponse.put("status","Success" );
		jsonResponse.put("message", "Operation completed Successfully");
	    savingsPotDTO = JSONUtils.parse(jsonResponse.toString(), SavingsPotDTO.class);
		}catch(Exception e){
			alert.prepareError("Error In Business Delegate : " + e).log();
            return null;
		}
		return savingsPotDTO;
	}

}
