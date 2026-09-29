package com.temenos.infinity.tradefinanceservices.businessdelegate.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.UpdateLetterOfCreditsBackendDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.UpdateLetterOfCreditsBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.dto.LetterOfCreditsDTO;

public class UpdateLetterOfCreditsBusinessDelegateImpl implements UpdateLetterOfCreditsBusinessDelegate {

	@Override
	public LetterOfCreditsDTO updateLetterOfCredits( LetterOfCreditsDTO letterOfCredits, DataControllerRequest request)
			throws ApplicationException{
		UpdateLetterOfCreditsBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(UpdateLetterOfCreditsBackendDelegate.class);
		return orderBackendDelegate.updateLetterOfCreditsOrder(letterOfCredits, request);
	}
	
	
}
