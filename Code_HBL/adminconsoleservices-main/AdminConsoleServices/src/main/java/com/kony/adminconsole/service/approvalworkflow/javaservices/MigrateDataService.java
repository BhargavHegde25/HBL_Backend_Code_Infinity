package com.kony.adminconsole.service.approvalworkflow.javaservices;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.service.approvalworkflow.resource.api.MigrateDataResource;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

public class MigrateDataService implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
                         DataControllerResponse responseInstance) throws Exception {
		Log4j2Configurator.getInstance();
        Result result = new Result();
        try {
            MigrateDataResource migrateResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(MigrateDataResource.class);
            result = migrateResource.migrateData(methodID, inputArray, requestInstance, responseInstance);
            result.addParam(new Param("Status", "Data Migration Success", FabricConstants.STRING));
        }catch(Exception exception) {
            alert.prepareError("Exception", exception).log();
            result.addParam(new Param("Status", "Data Migration Failure", FabricConstants.STRING));
            result.addParam(new Param("Exception Message", exception.getMessage(), FabricConstants.STRING));
            return result;
        }
        return result;
    }
}