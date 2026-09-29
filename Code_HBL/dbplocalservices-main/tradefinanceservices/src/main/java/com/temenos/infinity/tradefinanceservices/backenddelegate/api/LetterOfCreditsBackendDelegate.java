package com.temenos.infinity.tradefinanceservices.backenddelegate.api;

import java.util.List;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.tradefinanceservices.dto.ApprovalRequestDTO;
import com.temenos.infinity.tradefinanceservices.dto.LetterOfCreditsActionDTO;
import com.temenos.infinity.tradefinanceservices.dto.LetterOfCreditsDTO;

public interface LetterOfCreditsBackendDelegate extends BackendDelegate{
	/**
     * method to reject Letter of credit
     * 
     * @param request
     */
	LetterOfCreditsActionDTO rejectLetterOfCredit(DataControllerRequest request)
            throws ApplicationException;
	
	LetterOfCreditsActionDTO withdrawLetterOfCredit(DataControllerRequest request) throws ApplicationException;
	
	/**
     * method to approve cheque book request
     * 
     * @param request
     */
	LetterOfCreditsActionDTO approveLetterOfCredit(LetterOfCreditsActionDTO letterOfCredit, DataControllerRequest request)
            throws ApplicationException;

	List<ApprovalRequestDTO> fetchLetterOfCreditDetails(DataControllerRequest request)
			 throws ApplicationException;
	
}