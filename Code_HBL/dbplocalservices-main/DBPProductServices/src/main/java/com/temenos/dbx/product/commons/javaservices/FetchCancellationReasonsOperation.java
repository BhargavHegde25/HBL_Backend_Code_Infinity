package com.temenos.dbx.product.commons.javaservices;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.dbx.product.commons.resource.api.CancellationReasonsResource;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class FetchCancellationReasonsOperation implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();


		Result result;
		
		try {
			//Initializing of BulkPaymentFileResource through Abstract factory method
			CancellationReasonsResource cancellationReasonsResource = DBPAPIAbstractFactoryImpl.getResource(CancellationReasonsResource.class);
			result = cancellationReasonsResource.fetchCancellationReasons(methodID, inputArray, request, response);
		}
		catch(Exception e) {
			alert.prepareError("Error occured while invoking fetchCancellationReasons: ", e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		return result;
	}
	

}
