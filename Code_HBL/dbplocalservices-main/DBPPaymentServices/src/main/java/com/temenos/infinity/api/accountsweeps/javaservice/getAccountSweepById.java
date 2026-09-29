package com.temenos.infinity.api.accountsweeps.javaservice;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.accountsweeps.constants.ErrorCodeEnum;
import com.temenos.infinity.api.accountsweeps.resource.api.AccountSweepsResource;
import com.kony.dbputilities.util.Log4j2Configurator;

import java.util.HashMap;

/**
 * @author naveen.yerra
 */
public class getAccountSweepById implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String s, Object[] objects, DataControllerRequest dataControllerRequest,
                         DataControllerResponse dataControllerResponse) throws Exception {
		Log4j2Configurator.getInstance();
        AccountSweepsResource sweepsResource = DBPAPIAbstractFactoryImpl.getResource(AccountSweepsResource.class);
        try {
            HashMap<String, Object> input = (HashMap<String, Object>) objects[1];
            return sweepsResource.getSweepByAccountId(input, dataControllerRequest);
        } catch (Exception e) {
            alert.prepareError("Error occurred while fetching sweep.", e).log();
            return ErrorCodeEnum.ERR_2000.setErrorCode(new Result());
        }
    }
}
