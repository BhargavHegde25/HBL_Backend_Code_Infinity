package com.kony.adminconsole.licensing.javaservice;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.licensing.resource.api.LicensingResource;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetCountOfUsers implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	LicensingResource licensingResource = DBPAPIAbstractFactoryImpl.getInstance()
			.getFactoryInstance(ResourceFactory.class).getResource(LicensingResource.class);

	@Override
	public Object invoke(String method, Object[] inputArray, DataControllerRequest dataControllerRequest,
			DataControllerResponse dataControllerResponse) throws Exception {
		Log4j2Configurator.getInstance();

		try {

			Result noOfUsers = licensingResource.getCountOfUsers(method, inputArray, dataControllerRequest,
					dataControllerResponse);

			return noOfUsers;
		} catch (Exception e) {
			alert.prepareError("Exception occurred while invoking getCountOfUsers: ", e).log();
			return ErrorCodeEnum.ERR_22199.setErrorCode(new Result());

		}
	}

}
