package com.temenos.dbx.usermanagement.javaservice;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.usermanagement.resource.api.PartyRelationsUserManagementResource;

public class PartyRelationsGetOperation implements JavaService2 
{
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();

		Result result = new Result();
		try {
			PartyRelationsUserManagementResource customerResource = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(ResourceFactory.class).getResource(PartyRelationsUserManagementResource.class);
			result = customerResource.partyRelationsGet(methodID, inputArray, dcRequest, dcResponse);
		} catch (Exception e) {
			alert.prepareError("Caught exception while getting partyrelations: ",  e).log();
		}

		return result;
	}
}
