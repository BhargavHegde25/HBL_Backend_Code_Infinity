package com.temenos.infinity.api.srmstransactions.businessdelegate.api;

import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.srmstransactions.dto.SRMSTransactionDTO;

public interface SrmsTransactionsBusinessDelegate extends BusinessDelegate {

    List<SRMSTransactionDTO> getSRMSOneTimeTransfers(SRMSTransactionDTO inputDTO, DataControllerRequest request)
            throws ApplicationException;
    
    List<SRMSTransactionDTO> getSRMSOneTimeTransferById(SRMSTransactionDTO inputDTO, DataControllerRequest request)
            throws ApplicationException;

}
