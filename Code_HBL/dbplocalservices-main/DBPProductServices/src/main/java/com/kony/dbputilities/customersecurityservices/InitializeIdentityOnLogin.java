package com.kony.dbputilities.customersecurityservices;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.processor.IdentityHandler;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.utils.CustomerSessionsUtil;

public class InitializeIdentityOnLogin implements JavaService2 {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {

        Result result = new Result();

        ServicesManager servicesManager = request.getServicesManager();
        IdentityHandler identityHandler = servicesManager.getIdentityHandler();
        if (identityHandler == null) {
            alert.prepareError("Identity Handler is Null").log();
            result.addHttpStatusCodeParam(201);
            result.addOpstatusParam(0);
        } else {
            alert.prepareError("Identity Handler worked.").log();
            CustomerSessionsUtil.getLoggedInUserAttributesMap(request);
            identityHandler.getSecurityAttributes();
            result.addHttpStatusCodeParam(200);
            result.addOpstatusParam(0);
        }

        return result;
    }

}
