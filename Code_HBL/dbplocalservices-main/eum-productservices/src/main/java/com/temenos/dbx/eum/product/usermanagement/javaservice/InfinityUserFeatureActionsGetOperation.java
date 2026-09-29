package com.temenos.dbx.eum.product.usermanagement.javaservice;
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
import com.temenos.dbx.eum.product.usermanagement.javaservice.InfinityUserFeatureActionsGetOperation;
import com.temenos.dbx.eum.product.usermanagement.resource.api.InfinityUserManagementResource;

/**
 * Fetches the actions associated to the CIF for the logged in user
 * 
 * @author sowmya.vandanapu
 * @since 2021.01
 * @version 1.0
 */
public class InfinityUserFeatureActionsGetOperation implements JavaService2 {
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    LoggerUtil logger = new LoggerUtil(InfinityUserFeatureActionsGetOperation.class);

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();

        Result result = new Result();
        try {
            InfinityUserManagementResource resource =
                    DBPAPIAbstractFactoryImpl.getResource(InfinityUserManagementResource.class);
            result = resource.getInfinityUserFeatureActions(methodID, inputArray, dcRequest, dcResponse);
        } catch (ApplicationException e) {
            e.setError(result);
            alert.prepareError("Exception occured while fetching the actions " + e).log();
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching the actions " + e).log();
            ErrorCodeEnum.ERR_10788.setErrorCode(result);
        }

        return result;
    }
}
