package com.infinity.dbx.temenos.paylater;

import static com.infinity.dbx.temenos.transactions.TransactionConstants.TRANSACTION;

import java.util.List;

import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.BasePostProcessor;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetPaymentDetailsPostProcessor extends BasePostProcessor {
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
                        Dataset installmentsDS = record.getDatasetById(PayLaterConstants.DATASET_INST_PROD);
                        List<Record> installmentsRecords =
                                installmentsDS != null ? installmentsDS.getAllRecords() : null;
                        if (installmentsRecords.size() != 0) {
                            for (Record recordInstallments : installmentsRecords) {
                                String installmentStartDate = recordInstallments
                                        .getParamValueByName(PayLaterConstants.PARAM_INSTALLMENT_START_DATE);
                                installmentStartDate =
                                        temenosUtils.getDateFormat(installmentStartDate, PayLaterConstants.DATE_FORMAT);
                                recordInstallments.addParam(PayLaterConstants.PARAM_INSTALLMENT_START_DATE,
                                        installmentStartDate);

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
