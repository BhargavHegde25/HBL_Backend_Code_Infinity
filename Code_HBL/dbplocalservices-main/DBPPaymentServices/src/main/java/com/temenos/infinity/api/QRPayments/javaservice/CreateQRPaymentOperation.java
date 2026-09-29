package com.temenos.infinity.api.QRPayments.javaservice;

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
import com.temenos.infinity.api.QRPayments.constants.ErrorCodeEnum;
import com.temenos.infinity.api.QRPayments.resource.api.QRPaymentResource;

public class CreateQRPaymentOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	Result result = new Result();

	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		try {
			// Initializing of QRPaymentResource through Abstract factory method
			QRPaymentResource QRPayment = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(ResourceFactory.class).getResource(QRPaymentResource.class);
			result = QRPayment.createQRPayment(methodID, inputArray, request, response);
			return result;
		}

		catch (Exception e) {
			alert.prepareError(e.toString()).log();
			return ErrorCodeEnum.ERR_20041.setErrorCode(new Result());
		}
	}
}
