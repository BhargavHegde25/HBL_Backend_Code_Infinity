package com.infinity.dbx.temenos.paylater;

import static com.infinity.dbx.temenos.transactions.TransactionConstants.TRANSACTION;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.BasePostProcessor;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetDrawingDetailsPostProcessor extends BasePostProcessor {
    @Override
    public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {
        try {
            TemenosUtils temenosUtils = TemenosUtils.getInstance();
            temenosUtils.loadTransactionTypeProperties(request);
            Dataset transactionsDS = result.getDatasetById(TRANSACTION);
            List<Record> transactionRecords = transactionsDS != null ? transactionsDS.getAllRecords() : null;
            if (transactionRecords.isEmpty()) {
                return TemenosUtils.getEmptyResult(TRANSACTION);
            } else {

                if (transactionRecords.size() != 0) {
                    for (Record record : transactionRecords) {
                        String dueOn = record.getParamValueByName(PayLaterConstants.PARAM_DUE_ON);
                        if (StringUtils.isNotBlank(dueOn)) {
                            dueOn = temenosUtils.getDateFormat(dueOn, PayLaterConstants.DATE_FORMAT);
                            record.addParam(PayLaterConstants.PARAM_DUE_ON, dueOn);
                        }
                        Dataset installmentsDS = record.getDatasetById(PayLaterConstants.DATASET_INSTALLMENTS);
                        List<Record> installmentsRecords = installmentsDS != null ? installmentsDS.getAllRecords()
                                : null;
                        if (installmentsRecords.size() != 0) {
                            for (Record recordInstallments : installmentsRecords) {
                                String maturityDate = recordInstallments
                                        .getParamValueByName(PayLaterConstants.PARAM_MATURITY_DATE);
                                if (StringUtils.isNotBlank(maturityDate)) {
                                    maturityDate = temenosUtils.getDateFormat(maturityDate,
                                            PayLaterConstants.DATE_FORMAT);
                                    recordInstallments.addParam(PayLaterConstants.PARAM_MATURITY_DATE, maturityDate);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            CommonUtils.setErrMsg(result, e.toString());
        }
        return result;
    }
}
