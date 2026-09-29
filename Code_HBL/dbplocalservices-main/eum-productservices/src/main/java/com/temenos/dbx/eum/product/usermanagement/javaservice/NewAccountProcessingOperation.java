package com.temenos.dbx.eum.product.usermanagement.javaservice;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.eum.product.usermanagement.javaservice.NewAccountProcessingOperation;
import com.temenos.dbx.eum.product.usermanagement.resource.api.InfinityUserManagementResource;
import com.kony.dbputilities.util.Log4j2Configurator;

public class NewAccountProcessingOperation implements JavaService2 {

    LoggerUtil logger = new LoggerUtil(NewAccountProcessingOperation.class);

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();

        Result result = new Result();
        try {
            InfinityUserManagementResource resource =
                    DBPAPIAbstractFactoryImpl.getResource(InfinityUserManagementResource.class);
            result = resource.processOpenedNewAccounts(methodID, inputArray, dcRequest, dcResponse);
        } catch (ApplicationException e) {
            e.getErrorCodeEnum().setErrorCode(result);
        } catch (Exception e) {
            ErrorCodeEnum.ERR_10406.setErrorCode(result);
        }

        return result;
    }

}
