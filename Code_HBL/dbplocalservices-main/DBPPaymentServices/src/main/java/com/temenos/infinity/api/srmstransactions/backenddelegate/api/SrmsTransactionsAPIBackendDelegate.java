package com.temenos.infinity.api.srmstransactions.backenddelegate.api;

import java.util.List;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.srmstransactions.dto.SRMSTransactionDTO;

public interface SrmsTransactionsAPIBackendDelegate extends BackendDelegate {

    List<SRMSTransactionDTO> getOneTimeTransactions(SRMSTransactionDTO inputDTO, DataControllerRequest request)
            throws ApplicationException;
    
    List<SRMSTransactionDTO> getOneTimeTransactionById(SRMSTransactionDTO inputDTO, DataControllerRequest request)
            throws ApplicationException;

}
