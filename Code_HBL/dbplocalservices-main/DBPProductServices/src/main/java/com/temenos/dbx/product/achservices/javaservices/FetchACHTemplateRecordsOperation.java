package com.temenos.dbx.product.achservices.javaservices;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.dbx.product.achservices.resource.api.ACHTemplateRecordResource;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

public class FetchACHTemplateRecordsOperation implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String s, Object[] inputArray, DataControllerRequest dataControllerRequest,
                         DataControllerResponse dataControllerResponse) throws Exception {
		Log4j2Configurator.getInstance();


            Result result = new Result();

            try {
                //Initializing of ACHTemplateResource through Abstract factory method
                ACHTemplateRecordResource achResource = DBPAPIAbstractFactoryImpl.getInstance()
                        .getFactoryInstance(ResourceFactory.class).getResource(ACHTemplateRecordResource.class);

                result  = achResource.fetchACHTemplateRecord(inputArray, dataControllerRequest);
            }
            catch(Exception e) {
                alert.prepareError("Error occurred while fetching ACHTemplateRecords: ", e).log();
                return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
            }
            return result;

        }
    }
