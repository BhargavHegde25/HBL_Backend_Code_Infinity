package com.infinity.dbx.temenos.transactions;

import static com.infinity.dbx.temenos.transactions.TransactionConstants.ERR_EMPTY_RESPONSE;
import static com.infinity.dbx.temenos.transactions.TransactionConstants.PARAM_TRANSACTION;
import static com.infinity.dbx.temenos.transactions.TransactionConstants.PARAM_VALUE_PENDING;
import static com.infinity.dbx.temenos.transactions.TransactionConstants.TRANSACTION;
import static com.infinity.dbx.temenos.transactions.TransactionConstants.TRANS_TYPE_OTHERS;

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

public class PendingTransactionsForAdminPostProcessor extends TemenosBasePostProcessor {
    @Override
    public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {
	Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

        try {
            TemenosUtils temenosUtils = TemenosUtils.getInstance();
            temenosUtils.loadTransactionTypeProperties(request);
            Dataset transactionsDS = result.getDatasetById(TRANSACTION);
            List<Record> transactionRecords = transactionsDS != null ? transactionsDS.getAllRecords() : null;
            if (transactionRecords == null || transactionRecords.isEmpty()) {
                diagnostic.prepareDebug(ERR_EMPTY_RESPONSE).log();
                return TemenosUtils.getEmptyResult(PARAM_TRANSACTION);
            } else {

                if (transactionRecords.size() != 0) {
                    for (Record record : transactionRecords) {
                        record.addParam(PARAM_STATUS_DESCRIPTION, PARAM_VALUE_PENDING);
                        String transactionType = record.getParamValueByName(PARAM_TRANSACTION_TYPE);
                        if (transactionType != StringUtils.EMPTY) {
                            transactionType = temenosUtils.transactionTypesMap.get(transactionType);
                            if (transactionType == null) {
                                transactionType = TRANS_TYPE_OTHERS;
                            }
                        }
                        record.addParam(PARAM_TRANSACTION_TYPE, transactionType);
                        record.addParam(PARAM_STATUS_DESCRIPTION, TransactionConstants.PARAM_VALUE_PENDING);
                        record.addParam(TransactionConstants.PARAM_IS_SCHEDULED, "false");
                    }
                }
            }

            result.getDatasetById(TRANSACTION).setId(PARAM_TRANSACTION);
            result.addOpstatusParam(0);
            result.addHttpStatusCodeParam(200);

        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            CommonUtils.setErrMsg(result, e.toString());
        }
        return result;
    }

}