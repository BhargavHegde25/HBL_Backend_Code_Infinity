package com.infinity.dbx.temenos.paylater;

import static com.infinity.dbx.temenos.transactions.TransactionConstants.PARAM_VALUE_PENDING;
import static com.infinity.dbx.temenos.transactions.TransactionConstants.TRANSACTION;
import static com.infinity.dbx.temenos.transactions.TransactionConstants.TRANS_TYPE_OTHERS;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.BasePostProcessor;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetPendingTransactionsPostProcessor extends BasePostProcessor {
    @Override
    public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {
        TemenosUtils temenosUtils = TemenosUtils.getInstance();
        temenosUtils.loadTransactionTypeProperties(request);
        Dataset transactionsDS = result.getDatasetById(TRANSACTION);
        List<Record> transactionRecords = transactionsDS != null ? transactionsDS.getAllRecords() : null;
        if (transactionRecords.isEmpty()) {
            return TemenosUtils.getEmptyResult(TRANSACTION);
        } else {
            if (transactionRecords.size() != 0) {
                for (Record record : transactionRecords) {
                    String amount = CommonUtils.getParamValue(record, PARAM_AMOUNT);
                    if (!"".equalsIgnoreCase(amount)) {
                        record.addParam(PARAM_STATUS_DESCRIPTION, PARAM_VALUE_PENDING);
                        String transactionType = record.getParamValueByName(PARAM_TRANSACTION_TYPE);
                        String description = StringUtils.EMPTY;
                        String transactionDate = record.getParamValueByName(PayLaterConstants.PARAM_TRANS_DATE);
                        transactionDate = temenosUtils.getDateFormat(transactionDate, PayLaterConstants.DATE_FORMAT);
                        record.addParam(PayLaterConstants.PARAM_TRANS_DATE, transactionDate);
                        String transactionDescription = record.getParamValueByName(PayLaterConstants.PARAM_TRANS_DESC);
                        if (StringUtils.isNotBlank(transactionDescription)) {
                            description = transactionDescription;
                        }
                        String transactionDescription1 =
                                record.getParamValueByName(PayLaterConstants.PARAM_TRANS_DESC_1);
                        if (StringUtils.isNotBlank(transactionDescription1)) {
                            description = description + " " + transactionDescription1;
                        }
                        record.addParam(PayLaterConstants.PARAM_DESCRIPTION, description);
                        record.removeParamByName(PayLaterConstants.PARAM_TRANS_DESC);
                        record.removeParamByName(PayLaterConstants.PARAM_TRANS_DESC_1);
                        if (transactionType != StringUtils.EMPTY) {
                            transactionType = temenosUtils.transactionTypesMap.get(transactionType);
                            if (transactionType == null) {
                                transactionType = TRANS_TYPE_OTHERS;
                            }
                        }
                        record.addParam(PARAM_TRANSACTION_TYPE, transactionType);
                    } else {
                        return TemenosUtils.getEmptyResult(TRANSACTION);
                    }
                }
            }
        }
        return result;
    }
}
