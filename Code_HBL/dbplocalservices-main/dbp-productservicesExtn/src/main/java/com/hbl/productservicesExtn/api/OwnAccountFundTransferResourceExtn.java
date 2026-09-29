package com.hbl.productservicesExtn.api;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.transactionservices.resource.api.OwnAccountFundTransferResource;

public interface OwnAccountFundTransferResourceExtn extends OwnAccountFundTransferResource {
	public Result createTransaction(String methodID, Object[] inputArray, DataControllerRequest request, 
			DataControllerResponse response);

}
