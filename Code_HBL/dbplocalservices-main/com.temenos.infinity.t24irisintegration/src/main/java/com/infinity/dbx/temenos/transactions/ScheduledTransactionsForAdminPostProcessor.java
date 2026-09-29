package com.infinity.dbx.temenos.transactions;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.infinity.dbx.temenos.TemenosBasePostProcessor;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class ScheduledTransactionsForAdminPostProcessor extends TemenosBasePostProcessor implements TransactionConstants {
    @Override
    public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {
	Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

        try {
        	
        	Dataset transactionsDS = result.getDatasetById(TRANSACTION);

			List<Record> transactionRecords = transactionsDS != null ? transactionsDS.getAllRecords() : null;
			if (transactionRecords == null || transactionRecords.isEmpty()) {
				diagnostic.prepareDebug(ERR_EMPTY_RESPONSE).log();
				return TemenosUtils.getEmptyResult(PARAM_TRANSACTION);
			}

			HashMap<String, String> transactions = new HashMap<String, String>();
			if (transactionRecords.size() != 0) {
				for (Record record : transactionRecords) {
					String id = record.getParamValueByName(PARAM_TRANSACTION_ID);
					String fromAccountNumber = id.substring(0, id.indexOf("."));
					String description = record.getParamValueByName(PARAM_VALUE_TO_ACCOUNT_NAME);

					description = TRANSFER_TO + description;
					record.addParam(PARAM_DESCRIPTION, description);
					record.addParam(TransactionConstants.PARAM_STATUS_DESCRIPTION, PARAM_VALUE_PENDING);
					String transactionType = record.getParamValueByName(STANDING_ORDER_PRODUCT_NAME_KEY);
					if (transactionType != StringUtils.EMPTY) {
						if (TransactionConstants.INATIONAL_PRODUCT_ID.equalsIgnoreCase(transactionType)) {
							transactionType = EXTERNAL_TRANSFER;
							record.addParam(TransactionConstants.PARAM_TO_EXTERNAL_ACCOUNT_NUMBER,
									record.getParamValueByName(Constants.PARAM_TO_ACCOUNT_NUMBER));

							record.addParam(TemenosConstants.PARAM_BENEFICARY_NAME,
									record.getParamValueByName(Constants.PARAM_TO_ACCOUNT_NAME));
						} else if (TransactionConstants.DOMESTIC_PRODUCT_ID.equalsIgnoreCase(transactionType) || TransactionConstants.INSTA_PAY_PRODUCT_ID.equalsIgnoreCase(transactionType)) {
							transactionType = EXTERNAL_TRANSFER;
							record.addParam(TemenosConstants.PARAM_BENEFICARY_NAME,
									record.getParamValueByName(Constants.PARAM_TO_ACCOUNT_NAME));
							if (StringUtils
									.isAlphanumeric(result.getParamValueByName(Constants.PARAM_TO_ACCOUNT_NUMBER))) {
								record.addParam(TemenosConstants.PARAM_IBAN,
										record.getParamValueByName(Constants.PARAM_TO_ACCOUNT_NUMBER));

							} else {

								record.addParam(TransactionConstants.PARAM_TO_EXTERNAL_ACCOUNT_NUMBER,
										record.getParamValueByName(Constants.PARAM_TO_ACCOUNT_NUMBER));
							}
						} else if (transactionType.equalsIgnoreCase(TransactionConstants.ACTRF_PRODUCT_ID)) {
							transactionType = INTERNAL_TRANSFER;

						} 

						record.addParam(TransactionConstants.PARAM_TRANSACTION_TYPE, transactionType);
						record.addParam(PARAM_IS_SCHEDULED, Constants.TRUE);
						record.addParam(PARAM_FREQUENCY_TYPE, TransactionUtils.convertT24FrequencyToDbxFrequencyType(
								record.getParamValueByName(PARAM_FREQUENCY_TYPE)));
					}
					record.addParam(
							new Param(PARAM_FROM_ACCOUNT_NUMBER, fromAccountNumber, Constants.PARAM_DATATYPE_STRING));

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
