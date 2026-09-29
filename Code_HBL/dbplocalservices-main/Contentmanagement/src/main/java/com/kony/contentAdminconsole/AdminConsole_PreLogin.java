package com.kony.contentAdminconsole;

import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class AdminConsole_PreLogin implements DataPreProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @SuppressWarnings("rawtypes")
    @Override
    public boolean execute(HashMap inputArgs, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance, Result result) throws Exception {

    	try {
        	String backendToken = AdminUtil.getAdminToken();
            requestInstance.getHeaderMap().put("backendToken", backendToken);
            return true;
        } catch (Exception e) {
            alert.prepareError("Unhandled Exception. Returning false from pre-processor", e).log();
            ErrorCodeEnum.ERR_12000.setErrorCode(result);
        }
        return false;
    }
}