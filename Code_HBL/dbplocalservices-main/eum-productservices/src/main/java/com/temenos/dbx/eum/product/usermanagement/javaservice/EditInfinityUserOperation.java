package com.temenos.dbx.eum.product.usermanagement.javaservice;
import com.hbl.infinity.accounts.perf.invalidation.GetListCacheInvalidator;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.eum.product.usermanagement.resource.api.InfinityUserManagementResource;

/**
 * 
 *
 */
public class EditInfinityUserOperation implements JavaService2{
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	LoggerUtil logger = new LoggerUtil(EditInfinityUserOperation.class);
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
    	Result result = new Result();
        InfinityUserManagementResource infinityUserManagementResource = DBPAPIAbstractFactoryImpl.getResource(InfinityUserManagementResource.class);
        try {
        return infinityUserManagementResource.editInfinityUser(methodId, inputArray, request, response);
        }
        catch (ApplicationException e) {
            e.getErrorCodeEnum().setErrorCode(result);
        } catch (Exception e) {
            ErrorCodeEnum.ERR_10819.setErrorCode(result);
            alert.prepareError("Error",e).log();
        }
        return result;
    }

}
