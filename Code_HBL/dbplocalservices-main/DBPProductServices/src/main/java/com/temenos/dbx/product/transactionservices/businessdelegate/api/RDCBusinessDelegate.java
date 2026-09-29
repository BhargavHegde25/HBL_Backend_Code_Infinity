package com.temenos.dbx.product.transactionservices.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.dbx.product.transactionservices.dto.RDCDTO;
import com.konylabs.middleware.controller.DataControllerRequest;

public interface RDCBusinessDelegate extends BusinessDelegate{

	/**
	 * @author sharath.prabhala
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return
	 */
	public RDCDTO createRDC(RDCDTO createDTO, DataControllerRequest request);
}