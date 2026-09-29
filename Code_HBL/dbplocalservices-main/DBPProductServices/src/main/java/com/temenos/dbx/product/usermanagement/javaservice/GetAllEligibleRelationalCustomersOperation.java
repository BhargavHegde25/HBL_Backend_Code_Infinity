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
 * Fetches the elligible relative customers for the CIF
 * 
 * @author sowmya.vandanapu
 * @version 1.0
 * @since 2021.01
 */
public class GetAllEligibleRelationalCustomersOperation implements JavaService2 {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        Result result = new Result();
        try {
            InfinityUserManagementResource resource =
                    DBPAPIAbstractFactoryImpl.getResource(InfinityUserManagementResource.class);
            result = resource.getAllEligibleRelationalCustomers(methodId, inputArray, request,
                    response);
        } catch (ApplicationException e) {
            alert.prepareError("Exeption occured while fetching the relative customers" + e.getMessage()).log();
            e.getErrorCodeEnum().setErrorCode(result);
        } catch (Exception e) {
            alert.prepareError("Exeption occured while fetching the relative customers" + e.getMessage()).log();
            ErrorCodeEnum.ERR_10770.setErrorCode(result);
        }
        return result;
    }

}
