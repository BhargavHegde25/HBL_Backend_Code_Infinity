package com.infinity.dbx.temenos.bulkpaymentservices.postprocessor;


import com.infinity.dbx.temenos.accounts.AccountsConstants;
import com.kony.dbx.BasePostProcessor;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.kony.dbputilities.util.Log4j2Configurator;

public class UploadFilePostProcessor extends BasePostProcessor implements AccountsConstants {


	@Override
	public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {		
		Log4j2Configurator.getInstance();
       
		return null;
	}

}