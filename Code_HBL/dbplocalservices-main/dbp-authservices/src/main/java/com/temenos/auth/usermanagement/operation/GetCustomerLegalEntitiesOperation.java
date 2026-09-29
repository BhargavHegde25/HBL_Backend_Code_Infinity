package com.temenos.auth.usermanagement.operation;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.auth.usermanagement.resource.api.AuthUserManagementResource;
import com.temenos.auth.usermanagement.resource.impl.AuthUserManagementResourceImpl;

public class GetCustomerLegalEntitiesOperation implements JavaService2 {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodId, Object[] inputArray,
			DataControllerRequest requestInstance, DataControllerResponse responseInstance)
			throws Exception {
		try {
			AuthUserManagementResource resource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class)
                    .getResource(AuthUserManagementResource.class);
			return resource.getCustomerActiveLegalEntities(methodId, inputArray, requestInstance,
					responseInstance);
			
		} catch (ApplicationException e) {
			alert.prepareError("Exception while getting customer legal entities" , e.getMessage()).log();
			return e.getErrorCodeEnum().setErrorCode(new Result());
		}
		 catch (Exception e) {
			 alert.prepareError("Exception while getting customer legal entities" , e).log();
			 return ErrorCodeEnum.ERR_10220.setErrorCode(new Result());
		}
	}

}
