package com.kony.adminconsole.service.customermanagement;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.campaign.resource.CustomerManagementResource;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetOnboardingApplications implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

		@Override
		public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
				DataControllerResponse responseInstance) throws Exception {
			try {
				CustomerManagementResource customerManagementResource = DBPAPIAbstractFactoryImpl.getResource(CustomerManagementResource.class);
				return customerManagementResource.getCustomerApplications(methodID, inputArray, requestInstance, responseInstance, false);
			} catch (Exception e) {
				String errorMsg = "Error : " + e.toString() + "..." + e.getStackTrace()[0].toString();
				Result errorResult = new Result();
				alert.prepareError("Runtime Exception.Exception Trace:" + errorMsg).log();
				return errorResult;
			}
		}

}
