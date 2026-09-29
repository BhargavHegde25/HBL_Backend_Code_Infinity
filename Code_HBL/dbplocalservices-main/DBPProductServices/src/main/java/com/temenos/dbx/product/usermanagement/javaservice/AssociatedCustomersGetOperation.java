package com.temenos.dbx.product.usermanagement.javaservice;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.usermanagement.resource.api.InfinityUserManagementResource;
import com.kony.dbputilities.util.Log4j2Configurator;

/**
 * Fetches the customers associated with the logged in user
 * 
 * @author sowmya.vandanapu
 * @since 2021.01
 * @version 1.0 Fetches the customers with which the user is associated
 */
public class AssociatedCustomersGetOperation implements JavaService2 {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();

        Result result = new Result();
        try {
            InfinityUserManagementResource resource =
                    DBPAPIAbstractFactoryImpl.getResource(InfinityUserManagementResource.class);
            result = resource.getAssociatedCustomers(methodID, inputArray, dcRequest, dcResponse);
        } catch (ApplicationException e) {
            e.setError(result);
            alert.prepareError("Exception occured while fetching the associated customers info " + e.getStackTrace()).log();
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching the associated customers info " + e.getStackTrace()).log();
            ErrorCodeEnum.ERR_10763.setErrorCode(result);
        }

        return result;
    }
}
