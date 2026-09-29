package com.temenos.dbx.product.achservices.javaservices;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.dbx.product.achservices.resource.api.ACHTransactionRecordResource;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

public class FetchACHTransactionRecordsOperation implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodName, Object[] inputArray, DataControllerRequest dataControllerRequest,
                         DataControllerResponse dataControllerResponse) throws Exception {
		Log4j2Configurator.getInstance();


        Result result ;

        try {
            //Initializing of ACHTransactionResource through Abstract factory method
            ACHTransactionRecordResource achResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(ACHTransactionRecordResource.class);

            result  = achResource.fetchACHTransactionRecords( methodName, inputArray, dataControllerRequest, dataControllerResponse);
        }
        catch(Exception e) {
            alert.prepareError("Error occurred while fetching ACHTransactionRecords: ", e).log();
            return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
        }
        return result;

    }
}