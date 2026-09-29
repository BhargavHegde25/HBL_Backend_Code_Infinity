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

import java.util.HashMap;

/**
 * @author naveen.yerra
 */
public class EditAccountSweepsOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    AccountSweepsResource accountSweepsResource =  DBPAPIAbstractFactoryImpl.getInstance()
            .getFactoryInstance(ResourceFactory.class).getResource(AccountSweepsResource.class);
    @Override
    public Object invoke(String s, Object[] objects, DataControllerRequest dataControllerRequest,
                         DataControllerResponse dataControllerResponse) throws Exception {
		Log4j2Configurator.getInstance();
        HashMap<String, Object> inputMap = (HashMap<String, Object>) objects[1];
        try {
            return accountSweepsResource.editSweep(inputMap, dataControllerRequest);
        } catch(Exception e) {
            alert.prepareError("Exception occurred while creating sweep", e).log();
            return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
        }
    }
}
