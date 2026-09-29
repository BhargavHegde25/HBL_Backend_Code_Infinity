package com.temenos.dbx.datamigrationservices.javaservice;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.datamigrationservices.resource.api.MigrateContractResource;

public class CreateContract implements JavaService2 {
	LoggerUtil logger = new LoggerUtil(CreateContract.class);

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
        Result result = new Result();
        try {
        	MigrateContractResource resource = DBPAPIAbstractFactoryImpl.getResource(MigrateContractResource.class);
			result = resource.createContract(methodId, inputArray, request, response);
        } catch (ApplicationException e) {
        	logger.error("Exception occured while creating a contract " , e);
            e.getErrorCodeEnum().setErrorCode(result);
        } catch (Exception e) {
            logger.error("Exception occured while creating a contract " , e);
            ErrorCodeEnum.ERR_10351.setErrorCode(result);
        }

        return result;
    }
}
