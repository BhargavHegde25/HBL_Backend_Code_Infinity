/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.impl;

import java.util.List;

import com.dbp.core.api.DBPDTO;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.LetterOfCreditDrawingsBackendDelegate;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.LetterOfCreditSwiftsAndAdvicesBackendDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.LetterOfCreditSwiftsAndAdvicesBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.dto.DrawingsDTO;
import com.temenos.infinity.tradefinanceservices.dto.SwiftsAndAdvisesDTO;

public class LetterOfCreditSwiftsAndAdvicesBusinessDelegateImpl implements LetterOfCreditSwiftsAndAdvicesBusinessDelegate{

	@Override
	public SwiftsAndAdvisesDTO createSwiftsAndAdvises(SwiftsAndAdvisesDTO SwiftsAndAdvises, DBPDTO drawings,
			DataControllerRequest request) throws ApplicationException, com.temenos.infinity.api.commons.exception.ApplicationException {
		LetterOfCreditSwiftsAndAdvicesBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(LetterOfCreditSwiftsAndAdvicesBackendDelegate.class);
		SwiftsAndAdvises = orderBackendDelegate.createSwiftsAndAdvises(SwiftsAndAdvises, drawings, request);
		return SwiftsAndAdvises;
	}

	@Override
	public List<SwiftsAndAdvisesDTO> getSwiftsAndAdvises(DataControllerRequest request) throws ApplicationException {
		LetterOfCreditSwiftsAndAdvicesBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(LetterOfCreditSwiftsAndAdvicesBackendDelegate.class);
		List<SwiftsAndAdvisesDTO> SwiftsAndAdvises = orderBackendDelegate.getSwiftsAndAdvises(request);
		return SwiftsAndAdvises;
	}

}
