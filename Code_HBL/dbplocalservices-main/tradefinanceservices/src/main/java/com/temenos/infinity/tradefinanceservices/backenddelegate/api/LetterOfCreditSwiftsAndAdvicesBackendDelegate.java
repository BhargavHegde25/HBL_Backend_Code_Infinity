/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.api;

import java.util.List;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.api.DBPDTO;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.dto.DrawingsDTO;
import com.temenos.infinity.tradefinanceservices.dto.SwiftsAndAdvisesDTO;

public interface LetterOfCreditSwiftsAndAdvicesBackendDelegate extends BackendDelegate{

	SwiftsAndAdvisesDTO createSwiftsAndAdvises(SwiftsAndAdvisesDTO SwiftsAndAdvises, DBPDTO drawings, DataControllerRequest request)	throws ApplicationException, com.temenos.infinity.api.commons.exception.ApplicationException;
	
	List<SwiftsAndAdvisesDTO> getSwiftsAndAdvises(DataControllerRequest request)	throws ApplicationException;
	
}
