package com.temenos.infinity.api.chequemanagement.businessdelegate.api;

import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.chequemanagement.dto.BBRequestDTO;
import com.temenos.infinity.api.chequemanagement.dto.ChequeBook;
import com.temenos.infinity.api.commons.exception.ApplicationException;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public interface GetChequeBookBusinessDelegate extends BusinessDelegate {

    /**
     * method to get the Blocked Funds return result Object
     * 
     * @param request
     */
    List<ChequeBook> getChequeBook(ChequeBook chequeBook, DataControllerRequest request)
            throws Exception; 
    
    public List<BBRequestDTO> getBBRequests(String accountId);

	List<ChequeBook> getChequeBookFromT24(ChequeBook chequeBook, DataControllerRequest request) throws Exception;
}