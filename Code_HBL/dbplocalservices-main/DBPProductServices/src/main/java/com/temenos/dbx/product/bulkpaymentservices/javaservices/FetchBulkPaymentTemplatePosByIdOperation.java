package com.temenos.dbx.product.bulkpaymentservices.javaservices;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.bulkpaymentservices.resource.api.BulkPaymentTemplateResource;

public class FetchBulkPaymentTemplatePosByIdOperation implements JavaService2 {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();

		Result result;

		try {
			// Initializing of BulkPaymentPOResource through Abstract factory method
			BulkPaymentTemplateResource bulkPaymentTemplateResource = DBPAPIAbstractFactoryImpl.getResource(BulkPaymentTemplateResource.class);
			result = bulkPaymentTemplateResource.fetchPOsByTemplateId(methodID, inputArray, request, response);
		} catch (Exception e) {
			alert.prepareError("Error occured while invoking fetchPOsByTemplateId: ", e).log();
			return ErrorCodeEnum.ERR_28019.setErrorCode(new Result());
		}

		return result;
	}
}
