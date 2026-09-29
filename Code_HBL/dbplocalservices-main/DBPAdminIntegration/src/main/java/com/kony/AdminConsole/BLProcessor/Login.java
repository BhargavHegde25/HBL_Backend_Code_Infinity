package com.kony.AdminConsole.BLProcessor;

import com.kony.AdminConsole.Utilities.AdminConsoleOperations;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class Login implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
        Result result = new Result();

        String authToken = AdminConsoleOperations.login(requestInstance);
        result.addParam(new Param("X-Kony-Authorization", authToken, "String"));
        return result;
    }

}
