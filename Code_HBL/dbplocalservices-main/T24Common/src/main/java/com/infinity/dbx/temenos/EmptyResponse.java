package com.infinity.dbx.temenos;

import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
 
public class EmptyResponse extends TemenosBaseService{
    private static String TRANSACTION = "Transactions";
	
	public Result invoke(String methodId, Object[] inputArray, 
			DataControllerRequest request, DataControllerResponse response) throws Exception {

		// First run iPayBaseService
		Result result=new Result();
		result.addParam(new Param(Constants.PARAM_OP_STATUS, 
				Constants.PARAM_OP_STATUS_OK, Constants.PARAM_DATATYPE_INT));
		result.addDataset(new Dataset(TRANSACTION));
		return result;
	}

}
