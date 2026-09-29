package com.kony.eum.AdminConsole.BLProcessor;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.utils.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetPasswordRules implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {


    	try {
            return AdminUtil.invokeAPI(HelperMethods.getInputParamMap(inputArray), URLConstants.GET_PASSWORD_RULES, requestInstance);
        } catch (Exception e) {
           alert.prepareError(e.toString()).log();
           return new Result();
        }

    }

}
