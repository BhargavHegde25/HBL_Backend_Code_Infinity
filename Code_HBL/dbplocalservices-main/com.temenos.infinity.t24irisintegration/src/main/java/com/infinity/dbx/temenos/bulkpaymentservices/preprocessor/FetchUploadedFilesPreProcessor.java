package com.infinity.dbx.temenos.bulkpaymentservices.preprocessor;

import java.util.HashMap;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class FetchUploadedFilesPreProcessor extends TemenosBasePreProcessor {

	@Override
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		Log4j2Configurator.getInstance();

		super.execute(params, request, response, result);

		return Boolean.TRUE;
	}

}
