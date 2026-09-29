package com.temenos.infinity.tradefinanceservices.businessdelegate.api;

import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.tradefinanceservices.dto.ApprovalRequestDTO;
import com.temenos.infinity.tradefinanceservices.dto.LetterOfCreditsActionDTO;


public interface LetterOfCreditsBusinessDelegate extends BusinessDelegate{

	/**
     * method to get the reject Letter of credit return result Object
     * 
     * @param request
     */
    LetterOfCreditsActionDTO rejectLetterOfCredit(DataControllerRequest request)
            throws ApplicationException; 
    
    LetterOfCreditsActionDTO withdrawLetterOfCredit( DataControllerRequest request) throws ApplicationException;

	List<ApprovalRequestDTO> fetchLetterOfCreditDetails(DataControllerRequest request)
			throws ApplicationException;
    
	
}
