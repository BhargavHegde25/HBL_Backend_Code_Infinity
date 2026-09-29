package com.temenos.infinity.product.bulkpaymentservices.javaservices;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.product.bulkpaymentservices.resource.api.BulkPaymentTemplateResource;

public class FetchBulkPaymentTemplateOperation implements JavaService2 {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();

		Result result;

		try {
			// Initializing of BulkPaymentPOResource through Abstract factory method
			BulkPaymentTemplateResource bulkPaymentTemplateResource = DBPAPIAbstractFactoryImpl.getResource(BulkPaymentTemplateResource.class);
			result = bulkPaymentTemplateResource.fetchBulkPaymentTemplates(methodID, inputArray, request, response);
		} catch (Exception e) {
			alert.prepareError("Error occured while invoking fetchBulkPaymentTemplates: ", e).log();
			return ErrorCodeEnum.ERR_28007.setErrorCode(new Result());
		}

		return result;
	}

}
