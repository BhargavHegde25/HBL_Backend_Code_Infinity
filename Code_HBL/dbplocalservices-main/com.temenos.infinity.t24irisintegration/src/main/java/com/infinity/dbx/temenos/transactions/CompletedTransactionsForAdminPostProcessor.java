package com.infinity.dbx.temenos.transactions;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.infinity.dbx.temenos.TemenosBasePostProcessor;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class CompletedTransactionsForAdminPostProcessor extends TemenosBasePostProcessor {
    @Override
    public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {
	    Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	    Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

        try {
            TemenosUtils temenosUtils = TemenosUtils.getInstance();
            temenosUtils.loadTransactionTypeProperties(request);
            Dataset transactionsDS = result.getDatasetById(TransactionConstants.TRANSACTION);
            List<Record> transactionRecords = transactionsDS != null ? transactionsDS.getAllRecords() : null;
            if (transactionRecords == null || transactionRecords.isEmpty()) {
                diagnostic.prepareDebug(TransactionConstants.ERR_EMPTY_RESPONSE).log();
                return TemenosUtils.getEmptyResult(TransactionConstants.PARAM_TRANSACTION);
            } else {
                if (transactionRecords.size() != 0) {
                    for (Record record : transactionRecords) {
                        record.addParam(PARAM_STATUS_DESCRIPTION, TransactionConstants.PARAM_VALUE_SUCCESSFUL);
                        String transactionType = record.getParamValueByName(PARAM_TRANSACTION_TYPE);
                        if (transactionType != StringUtils.EMPTY) {
                            transactionType = temenosUtils.transactionTypesMap.get(transactionType);
                            if (transactionType == null) {
                                transactionType = TransactionConstants.TRANS_TYPE_OTHERS;
                            }
                        }
                        record.addParam(PARAM_TRANSACTION_TYPE, transactionType);
                        record.addParam(PARAM_STATUS_DESCRIPTION, TransactionConstants.PARAM_COMPLETED);
                        record.addParam(TransactionConstants.PARAM_IS_SCHEDULED, "false");
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