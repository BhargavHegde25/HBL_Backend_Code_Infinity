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

public class LinkExistingUserToContractOperation implements JavaService2 {

	LoggerUtil logger = new LoggerUtil(LinkExistingUserToContractOperation.class);

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		try {
            MigrateInfinityUserResource migrationResource =
                    DBPAPIAbstractFactoryImpl.getResource(MigrateInfinityUserResource.class);
            return migrationResource.linkUserToContract(methodId, inputArray, request, response);
        } catch (ApplicationException e) {
            e.setError(result);
            logger.error("Exception occured while creating a contract " , e);
            return result;
        } catch (Exception e) {
        	ErrorCodeEnum.ERR_10351.setErrorCode(result);
            logger.error("Exception occured while creating a contract " , e);
            return result;
        }
	}

}
