/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.javaservice;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.temenos.infinity.tradefinanceservices.resource.api.CorporatePayeesResource;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import java.util.HashMap;

public class getCorporatePayeesOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String s, Object[] objects, DataControllerRequest dataControllerRequest,
                         DataControllerResponse dataControllerResponse) throws Exception {
		Log4j2Configurator.getInstance();
        CorporatePayeesResource payeesResource = DBPAPIAbstractFactoryImpl.getResource(CorporatePayeesResource.class);
        HashMap<String, Object> inputParams = (HashMap<String, Object>) objects[1];
        try {
            return payeesResource.getCorporatePayees(inputParams, dataControllerRequest);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke : " + e).log();
            return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
        }
    }
}
