package com.temenos.dbx.product.usermanagement.javaservice;

import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.usermanagement.resource.api.InfinityUserManagementResource;
import com.temenos.dbx.product.usermanagement.resource.api.PushExternalEventResource;
import com.temenos.dbx.product.utils.InfinityConstants;

/**
 * 
 *
 */
public class CreateInfinityUserOperation implements JavaService2 {

    /**
     *
     */
    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        InfinityUserManagementResource infinityUserManagementResource =
                DBPAPIAbstractFactoryImpl.getResource(InfinityUserManagementResource.class);
        Object result =
                infinityUserManagementResource.createInfinityUserWithContract(methodId, inputArray, request, response);
        // The below details are used to push the event.
        Result userResult = (Result) result;
        String userName = userResult.getParamValueByName(InfinityConstants.userName);
        String activationCode = userResult.getParamValueByName(InfinityConstants.activationCode);
        request.addRequestParam_(InfinityConstants.userId, userName);
        request.addRequestParam_(InfinityConstants.activationCode, activationCode);
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        inputParams.put(InfinityConstants.userId, userName);
        inputParams.put(InfinityConstants.activationCode, activationCode);
        PushExternalEventResource resource = DBPAPIAbstractFactoryImpl.getResource(PushExternalEventResource.class);
        resource.pushUserIdAndActivationCode(methodId, inputArray, request, response);

        return result;
    }
}
