package com.temenos.dbx.product.achservices.javaservices;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.dbx.product.achservices.resource.api.ACHFileSubRecordResource;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class FetchACHFileSubRecordsOperation implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodName, Object[] inputArray, DataControllerRequest dataControllerRequest,
                         DataControllerResponse dataControllerResponse) throws Exception {
		Log4j2Configurator.getInstance();

        Result result = new Result();
        try {
            //Initializing of ACHFileSubRecordResource through Abstract factory method
            ACHFileSubRecordResource achFileSubRecordResource = DBPAPIAbstractFactoryImpl.getResource(ACHFileSubRecordResource.class);
            result  = achFileSubRecordResource.fetchACHFileSubrecords(methodName, inputArray, dataControllerRequest, dataControllerResponse);
        }
        catch(Exception e) {
            alert.prepareError("Error occurred while fetching ACHFileSubRecords: ", e).log();
            return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
        }
        
        return result;
    }

}
