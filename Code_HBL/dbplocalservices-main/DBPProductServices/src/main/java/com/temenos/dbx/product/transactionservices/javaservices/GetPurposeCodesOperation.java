package com.temenos.dbx.product.transactionservices.javaservices;

import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.transactionservices.resource.api.GeneralTransactionsResource;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class GetPurposeCodesOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        Result result = new Result();
        HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
		/*
		 * @SuppressWarnings("unchecked") HashMap<String, Object> params =
		 * (HashMap<String, Object>) inputArray[1]; Map<String, String> inputParams =
		 * HelperMethods.getInputParamMap(inputArray); String serviceName =
		 * inputParams.get("serviceName");
		 */
        try {
        	GeneralTransactionsResource generalTransactionsResource = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(ResourceFactory.class).getResource(GeneralTransactionsResource.class);
			
			result  = generalTransactionsResource.getPurposeCodesById(methodID, inputArray, request, response);
		}catch (Exception e) {
			alert.prepareError("Caught exception at invoke of GetPurposeCodesOperation: ", e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
			
		}
        return result; 
    }
}
