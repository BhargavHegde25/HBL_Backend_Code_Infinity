package com.temenos.infinity.api.srmstransactions.businessdelegate.impl;

import java.util.List;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.srmstransactions.backenddelegate.api.SrmsTransactionsAPIBackendDelegate;
import com.temenos.infinity.api.srmstransactions.businessdelegate.api.SrmsTransactionsBusinessDelegate;
import com.temenos.infinity.api.srmstransactions.dto.SRMSTransactionDTO;

public class SrmsTransactionsBusinessDelegateImpl implements SrmsTransactionsBusinessDelegate {

    @Override
    public List<SRMSTransactionDTO> getSRMSOneTimeTransfers(SRMSTransactionDTO inputDTO, DataControllerRequest request)
            throws ApplicationException {
        SrmsTransactionsAPIBackendDelegate srmsBackendDelegate = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(SrmsTransactionsAPIBackendDelegate.class);
        List<SRMSTransactionDTO> oneTimeTransactions = srmsBackendDelegate.getOneTimeTransactions(inputDTO, request);
        return oneTimeTransactions;
    }

    @Override
    public List<SRMSTransactionDTO> getSRMSOneTimeTransferById(SRMSTransactionDTO inputDTO,
            DataControllerRequest request) throws ApplicationException {
        SrmsTransactionsAPIBackendDelegate srmsBackendDelegate = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(SrmsTransactionsAPIBackendDelegate.class);
        List<SRMSTransactionDTO> oneTimeTransactions = srmsBackendDelegate.getOneTimeTransactionById(inputDTO, request);
        return oneTimeTransactions;
    }

}