package com.hbl.productservicesExtn.api;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.transactionservices.resource.api.InterBankFundTransferResource;

public interface InterBankFundTransferResourceExtn extends InterBankFundTransferResource{
	Result createInterBankFundTransaction(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);

}
