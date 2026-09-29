package com.temenos.dbx.transaction.integrationpreprocessors;
import java.util.HashMap;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class CreateDisputePayAPersonTransactionPreProcessor implements DataPreProcessor2 {
    @Override
    @SuppressWarnings("unchecked")
    public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
            Result result) throws Exception {
    	if(DBPUtilitiesConstants.TRANSACTION_TYPE_P2P.equalsIgnoreCase(request.getParameter("transactionType")))
    		return true;
    	result.addParam(new Param("opstatus","0","int"));
        return false;
    }
}
