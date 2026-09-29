package com.temenos.infinity.api.srmstransactions.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public interface SrmsTransactionsResource extends Resource {

    Result getSrmsOneTimeTransactionsResource(String txnPeriod, String firstRecordNumber, String lastRecordNumber,
            DataControllerRequest request) throws ApplicationException;

    Result getSrmsOneTimeTransactionByIdResource(String txnId, DataControllerRequest request)
            throws ApplicationException;

}
