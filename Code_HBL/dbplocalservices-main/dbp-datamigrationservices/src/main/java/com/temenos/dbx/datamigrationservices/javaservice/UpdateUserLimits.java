package com.temenos.dbx.datamigrationservices.javaservice;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.datamigrationservices.resource.api.MigrateInfinityUserResource;

public class UpdateUserLimits implements JavaService2{
	LoggerUtil logger = new LoggerUtil(UpdateUserLimits.class);
    /**
     *
     */
    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
    	Result result = new Result();
    	MigrateInfinityUserResource infinityUserManagementResource = DBPAPIAbstractFactoryImpl.getResource(MigrateInfinityUserResource.class);
        try {
        	logger.error("**************** UpdateUserLimits ******");
        	return infinityUserManagementResource.editInfinityUser(methodId, inputArray, request, response);
        }
        catch (ApplicationException e) {
            e.getErrorCodeEnum().setErrorCode(result);
            logger.error("Error",e);
        } catch (Exception e) {
            ErrorCodeEnum.ERR_10819.setErrorCode(result);
            logger.error("Error",e);
        }
        return result;
    }
}
