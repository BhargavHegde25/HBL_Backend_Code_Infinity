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
 * Generated the activation code and username for infinity user
 * 
 * @author sowmya.vandanapu
 * @since 2021.01
 * @version 1.0
 */
public class InfinityUserActivationCodeUserNameSendOperation implements JavaService2 {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();

        Result result = new Result();
        try {
            InfinityUserManagementResource resource =
                    DBPAPIAbstractFactoryImpl.getResource(InfinityUserManagementResource.class);
            result = resource.generateInfinityUserActivationCodeAndUsername(methodID, inputArray, dcRequest, dcResponse);
        } catch (ApplicationException e) {
            e.getErrorCodeEnum().setErrorCode(result);
            alert.prepareError("Exception occured while generating the username and activation code" + e.getStackTrace()).log();
        } catch (Exception e) {
            alert.prepareError("Exception occured while generating the username and activation code" + e.getStackTrace()).log();
            ErrorCodeEnum.ERR_10795.setErrorCode(result);
        }

        return result;
    }
}
