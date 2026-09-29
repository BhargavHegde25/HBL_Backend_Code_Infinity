package com.temenos.infinity.api.arrangements.javaservice;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.temenos.infinity.api.arrangements.resource.api.InfinityAccountsResource;
import com.temenos.infinity.api.arrangements.utils.CommonUtils;
import com.kony.dbputilities.util.Log4j2Configurator;

public class GetInfinityAccountsOperation implements JavaService2{


    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        InfinityAccountsResource infinityUserManagementResource = DBPAPIAbstractFactoryImpl.getResource(InfinityAccountsResource.class);
        CommonUtils.setCompanyIdToRequest(request);
        return infinityUserManagementResource.getInfinityAccounts(methodId, inputArray, request, response );
    }

}
