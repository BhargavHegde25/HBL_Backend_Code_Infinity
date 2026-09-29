package com.kony.eum.dbputilities.customersecurityservices;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.eum.dbputilities.customersecurityservices.CheckDbxUserEnrolled;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.temenos.dbx.eum.product.usermanagement.javaservice.CheckUserEnrolledOperation;

public class CheckDbxUserEnrolled implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
       return new CheckUserEnrolledOperation().invoke(methodID, inputArray, dcRequest, dcResponse);
    }
}