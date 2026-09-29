package com.temenos.dbx.party.javaservice;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.party.resource.api.AuditActivityUpdateResource;

/**
 * 
 * @author KH2627
 * @version 1.0 Java Service to update audit logs with customerId and
 *          coreCustomerId for the provided partyId
 * 
 */

public class AuditActivityUpdateOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();

		Result result = new Result();
		try {
			AuditActivityUpdateResource auditActivityUpdateResource = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(ResourceFactory.class).getResource(AuditActivityUpdateResource.class);
			result = auditActivityUpdateResource.updatePartyAuditLogsWithCustomerInformation(methodID, inputArray,
					dcRequest, dcResponse);
		} catch (Exception e) {
			alert.prepareError("Exception occured while update party customer information" + e).log();
		}

		return result;
	}

}