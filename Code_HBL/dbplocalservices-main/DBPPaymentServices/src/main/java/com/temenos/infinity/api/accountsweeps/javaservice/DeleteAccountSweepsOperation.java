package com.temenos.infinity.api.accountsweeps.javaservice;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.accountsweeps.resource.api.AccountSweepsResource;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

public class DeleteAccountSweepsOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
                         DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();

        Result result= new Result();
        try {
            AccountSweepsResource accountSweepsResource =  DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(AccountSweepsResource.class);
            result=accountSweepsResource.deleteSweep(methodID, inputArray, request, response);
        }
        catch (Exception e){
            alert.prepareError("Exception occured while deleting sweep"+e).log();
            return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
        }

        return result;
    }
}
