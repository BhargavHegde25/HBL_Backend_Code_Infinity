package com.temenos.infinity.tradefinanceservices.businessdelegate.impl;

import java.util.List;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.LetterOfCreditsBackendDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.LetterOfCreditsBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.dto.ApprovalRequestDTO;
import com.temenos.infinity.tradefinanceservices.dto.LetterOfCreditsActionDTO;

public class LetterOfCreditsBusinessDelegateImpl implements LetterOfCreditsBusinessDelegate{

	@Override
	public LetterOfCreditsActionDTO rejectLetterOfCredit(DataControllerRequest request) throws ApplicationException {
		LetterOfCreditsBackendDelegate letterOfCreditsBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(LetterOfCreditsBackendDelegate.class);
		LetterOfCreditsActionDTO letterOfCredit = letterOfCreditsBackendDelegate.rejectLetterOfCredit(request);
		return letterOfCredit; 
	}

	@Override
	public LetterOfCreditsActionDTO withdrawLetterOfCredit(DataControllerRequest request) throws ApplicationException {
		LetterOfCreditsBackendDelegate letterOfCreditsBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(LetterOfCreditsBackendDelegate.class);
		LetterOfCreditsActionDTO letterOfCredit = letterOfCreditsBackendDelegate.withdrawLetterOfCredit(request);
		return letterOfCredit; 
	}

	@Override
	public List<ApprovalRequestDTO> fetchLetterOfCreditDetails(DataControllerRequest request) throws ApplicationException {
		LetterOfCreditsBackendDelegate letterOfCreditsBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(LetterOfCreditsBackendDelegate.class);
		List<ApprovalRequestDTO> letterOfCredit = letterOfCreditsBackendDelegate.fetchLetterOfCreditDetails(request);
		return letterOfCredit;
	}

}
