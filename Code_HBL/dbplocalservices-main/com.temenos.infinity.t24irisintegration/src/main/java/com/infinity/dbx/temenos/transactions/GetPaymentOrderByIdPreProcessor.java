package com.infinity.dbx.temenos.transactions;

import static com.infinity.dbx.temenos.transactions.TransactionConstants.EXTERNAL_ID_KEY;
import static com.infinity.dbx.temenos.transactions.TransactionConstants.TRANSACTION_ID_KEY;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class GetPaymentOrderByIdPreProcessor extends TemenosBasePreProcessor {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @SuppressWarnings("rawtypes")
    public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
            Result result) throws Exception {

        super.execute(params, request, response, result);
        String externalId = CommonUtils.getParamValue(params, EXTERNAL_ID_KEY);
        alert.prepareError("Transaction id :" + CommonUtils.getParamValue(params, TRANSACTION_ID_KEY)).log();
        if (StringUtils.isNotBlank(externalId)) {
            request.addRequestParam_(TRANSACTION_ID_KEY, externalId);
            alert.prepareError("ExternalId From SRMS "+externalId).log();
        }
        return Boolean.TRUE;
    }
}
