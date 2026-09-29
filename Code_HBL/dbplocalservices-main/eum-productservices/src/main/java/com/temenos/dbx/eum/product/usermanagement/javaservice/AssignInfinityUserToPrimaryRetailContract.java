package com.temenos.dbx.eum.product.usermanagement.javaservice;
import com.hbl.infinity.accounts.perf.invalidation.GetListCacheInvalidator;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.eum.product.usermanagement.resource.api.InfinityUserManagementResource;
import com.temenos.dbx.product.usermanagement.javaservice.ResetPasswordOperation;

public class AssignInfinityUserToPrimaryRetailContract implements JavaService2 {
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    LoggerUtil logger = new LoggerUtil(ResetPasswordOperation.class);

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
        try {
            return invokeOperation(methodID, inputArray, request, response);
        } finally {
            // getList cache: this operation changes data getList returns. Runs even after a part-way
            // failure, because some rows may already be written.
            GetListCacheInvalidator.permissionsChanged();
        }
    }

    private Object invokeOperation(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();

        logger = new LoggerUtil(this.getClass());
        
         Result result = new Result();
            try {
                InfinityUserManagementResource resource = DBPAPIAbstractFactoryImpl.getResource(InfinityUserManagementResource.class);
                result = resource.assignInfinityUserToPrimaryRetailContract(methodID, inputArray, request, response);
            } catch (Exception e) {
                alert.prepareError("Caught exception while updating Customer Preferences: ",  e).log();
            }

            return result;
    }

}