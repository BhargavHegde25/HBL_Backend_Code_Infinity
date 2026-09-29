package com.infinity.dbx.temenos.user;

import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.infinity.dbx.temenos.TemenosPreLoginBasePreProcessor;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetUserIdBySSNPreProcessor extends TemenosPreLoginBasePreProcessor{

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	@Override
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response, Result result)
			throws Exception {
		
		super.execute(params, request, response, result);
		alert.prepareError("GetUserIdBySSNPreProcessor").log();
		String taxId = params.get("Taxid") != null ? params.get("Taxid").toString() : "";
		alert.prepareError("Taxid " + taxId).log();
		if("".equalsIgnoreCase(taxId)) {
			alert.prepareError("GetUserIdBySSNPreProcessor false").log();
			result.addOpstatusParam(0);
			result.addHttpStatusCodeParam(200);
			return Boolean.FALSE;
		}
		
		return Boolean.TRUE;
	}

}
