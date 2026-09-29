package com.infinity.dbx.temenos.transfers;

import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class CreateOneTimeTransferPreProcessor extends TemenosBasePreProcessor {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	@SuppressWarnings("rawtypes")
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			super.execute(params, request);
	} catch (Exception e) {
		alert.prepareError("Exception occurred in Create OneTimeTransfer PreProcessor:" + e).log();
		return false;
	}
	return Boolean.TRUE;
	}
}
