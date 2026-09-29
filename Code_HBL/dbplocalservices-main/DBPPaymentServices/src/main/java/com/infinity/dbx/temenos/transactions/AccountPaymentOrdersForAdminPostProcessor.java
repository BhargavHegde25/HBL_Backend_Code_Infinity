package com.infinity.dbx.temenos.transactions;

import java.util.List;


import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.infinity.dbx.temenos.TemenosBasePostProcessor;
import com.infinity.dbx.temenos.transfers.TransferConstants;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class AccountPaymentOrdersForAdminPostProcessor extends TemenosBasePostProcessor {
    @Override
    public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {
		Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
		Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

        try {
        	Dataset transactionsDS = result.getDatasetById(TransactionConstants.TRANSACTION);

    		List<Record> transactionRecords = transactionsDS != null ? transactionsDS.getAllRecords() : null;
    		if (transactionRecords == null || transactionRecords.isEmpty()) {
    			return TemenosUtils.getEmptyResult(TransactionConstants.PARAM_TRANSACTION);
    		} else {
    			if (transactionRecords.size() != 0) {
    				
    				for (Record record : transactionRecords) {
    					Dataset credits = record.getDatasetById(TransactionConstants.PAYMENT_ORDER_CREDITS_ARRAY_KEY);
    					if (credits != null && !credits.getAllRecords().isEmpty()) {
    						record.addStringParam(PARAM_TO_ACCOUNT_NUMBER,
    								CommonUtils.getParamValue(credits.getRecord(0), PARAM_TO_ACCOUNT_NUMBER));
    						record.addStringParam(PARAM_TO_ACCOUNT_NAME,
    								CommonUtils.getParamValue(credits.getRecord(0), PARAM_TO_ACCOUNT_NAME));
    					}
    					Dataset beneficiaries = record
    							.getDatasetById(TransactionConstants.PAYMENT_ORDER_BENEFICIARIES_ARRAY_KEY);
    					if (beneficiaries != null && !beneficiaries.getAllRecords().isEmpty()) {
    						record.addStringParam(PARAM_TO_ACCOUNT_NUMBER, CommonUtils.getParamValue(
    								beneficiaries.getRecord(0), TransactionConstants.PARAM_COUNTER_PARTY_ID));
    						record.addStringParam(PARAM_TO_ACCOUNT_NAME, CommonUtils
    								.getParamValue(beneficiaries.getRecord(0), TransferConstants.PARAM_BENEFICIARY_NAME));
    						record.addStringParam(TransferConstants.PARAM_BENEFICIARY_NAME, CommonUtils
    								.getParamValue(beneficiaries.getRecord(0), TransferConstants.PARAM_BENEFICIARY_NAME));

    					}
    					String transactionType = CommonUtils.getParamValue(record, PARAM_TRANSACTION_TYPE);
    					if (transactionType.equalsIgnoreCase(TransactionConstants.ACTRF_PRODUCT_ID)) {
    						
    						record.addParam(PARAM_TRANSACTION_TYPE, TransactionConstants.INTERNAL_TRANSFER);
    					} else {
    						record.addParam(PARAM_TRANSACTION_TYPE, TransactionConstants.EXTERNAL_TRANSFER);
    						
    					}
    					record.addStringParam(PARAM_FREQUENCY_TYPE, Constants.FREQUENCY_ONCE);
    					record.addParam(TransactionConstants.PARAM_IS_SCHEDULED, Constants.TRUE);
    					String description = record.getParamValueByName(TransactionConstants.PARAM_VALUE_TO_ACCOUNT_NAME);

    					description = TransactionConstants.TRANSFER_TO + description;
    					record.addParam(TransactionConstants.PARAM_DESCRIPTION, description);
    					record.addParam(PARAM_STATUS_DESCRIPTION, TransactionConstants.PARAM_VALUE_PENDING);
    				}
    			}
    		}
    		
    		result.getDatasetById(TransactionConstants.TRANSACTION).setId(TransactionConstants.PARAM_TRANSACTION);
			result.addOpstatusParam(0);
			result.addHttpStatusCodeParam(200);
            
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            CommonUtils.setErrMsg(result, e.toString());
        }
        return result;
    }

}
