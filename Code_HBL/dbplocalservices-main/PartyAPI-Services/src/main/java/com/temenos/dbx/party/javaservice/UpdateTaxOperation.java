package com.temenos.dbx.party.javaservice;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.party.resource.api.DueDiligenceResource;

/**
 * Update Citizenship and Tax Operation Java Service is the logic block for
 * updating the combined list of Citizenship and Tax of a Customer.
 * 
 * @author KH2117-ManojDasari
 */
public class UpdateTaxOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = new Result();
		try {
			DueDiligenceResource dueDiligenceResource = DBPAPIAbstractFactoryImpl
					.getResource(DueDiligenceResource.class);
			result = dueDiligenceResource.updateTaxDetail(methodID, inputArray, dcRequest, dcResponse);
		} catch (Exception e) {
			alert.prepareError("Caught exception while updating Citizenship: ", e).log();
		}
		return result;
	}
}