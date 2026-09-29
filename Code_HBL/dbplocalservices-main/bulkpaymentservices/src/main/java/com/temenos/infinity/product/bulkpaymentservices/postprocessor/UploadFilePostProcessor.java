package com.temenos.infinity.product.bulkpaymentservices.postprocessor;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.infinity.dbx.temenos.accounts.AccountsConstants;
import com.kony.dbx.BasePostProcessor;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class UploadFilePostProcessor extends BasePostProcessor implements AccountsConstants {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {		
		Log4j2Configurator.getInstance();
       
		return null;
	}

}