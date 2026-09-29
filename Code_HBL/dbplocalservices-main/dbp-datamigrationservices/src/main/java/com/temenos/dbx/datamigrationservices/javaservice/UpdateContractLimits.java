package com.temenos.dbx.datamigrationservices.javaservice;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.dbx.datamigrationservices.resource.api.MigrateContractResource;

public class UpdateContractLimits implements JavaService2 {
	LoggerUtil logger = new LoggerUtil(UpdateContractLimits.class);

    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
        Result result = new Result();
        try {
        	logger.debug("************ UpdateContractLimits ***** ");
            MigrateContractResource resource = DBPAPIAbstractFactoryImpl.getResource(MigrateContractResource.class);
            result = resource.editContract(methodId, inputArray, request, response);
        } catch (ApplicationException e) {
            e.getErrorCodeEnum().setErrorCode(result);
            logger.error("Exception occured while creating a contract " + e.getStackTrace());
        } catch (Exception e) {
            logger.error("Exception occured while creating a contract " + e.getStackTrace());
            ErrorCodeEnum.ERR_10351.setErrorCode(result);
        }

        return result;
    }
}
