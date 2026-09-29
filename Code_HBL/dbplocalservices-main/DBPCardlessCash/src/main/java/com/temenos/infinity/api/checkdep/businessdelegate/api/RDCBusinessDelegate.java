package com.temenos.infinity.api.checkdep.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.infinity.api.checkdep.dto.RDCDTO;
import com.konylabs.middleware.controller.DataControllerRequest;

public interface RDCBusinessDelegate extends BusinessDelegate{

	/**
	 * @author eivanov
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return
	 */
	public RDCDTO createRDC(RDCDTO createDTO, DataControllerRequest request);
}