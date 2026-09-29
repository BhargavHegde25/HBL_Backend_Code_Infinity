package com.temenos.dbx.product.achservices.javaservices;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.dbx.product.achservices.resource.api.ACHTemplateSubRecordResource;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

public class FetchACHTemplateSubRecordsOperation implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String s, Object[] inputArray, DataControllerRequest dataControllerRequest,
                         DataControllerResponse dataControllerResponse) throws Exception {
		Log4j2Configurator.getInstance();
        Result result = new Result();

        try {
            //Initializing of ACHTemplateRecordResource through Abstract factory method
            ACHTemplateSubRecordResource achResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(ACHTemplateSubRecordResource.class);

            result  = achResource.fetchACHTemplateSubRecord(inputArray, dataControllerRequest);
        }
        catch(Exception e) {
            alert.prepareError("Error occurred while fetching ACHTemplateSubRecords: ", e).log();
            return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
        }
        return result;

    }
}
