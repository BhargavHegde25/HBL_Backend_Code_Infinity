package com.temenos.dbx.product.usermanagement.javaservice;

import com.hbl.infinity.accounts.perf.invalidation.GetListCacheInvalidator;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.temenos.dbx.product.usermanagement.resource.api.InfinityUserManagementResource;
import com.kony.dbputilities.util.Log4j2Configurator;

/**
 * 
 *
 */
public class ApplyCustomRoleOperation implements JavaService2{


    /**
     *
     */
    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
        try {
            return invokeOperation(methodId, inputArray, request, response);
        } finally {
            // getList cache: this operation changes data getList returns. Runs even after a part-way
            // failure, because some rows may already be written.
            GetListCacheInvalidator.permissionsChanged();
        }
    }

    private Object invokeOperation(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        InfinityUserManagementResource infinityUserManagementResource = DBPAPIAbstractFactoryImpl.getResource(InfinityUserManagementResource.class);
        return infinityUserManagementResource.applyCustomRole(methodId, inputArray, request, response);
    }
}
