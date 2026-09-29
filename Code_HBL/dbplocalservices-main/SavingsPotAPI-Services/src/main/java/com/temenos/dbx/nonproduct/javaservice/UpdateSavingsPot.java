package com.temenos.dbx.nonproduct.javaservice;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.nonproduct.resource.api.SavingsPotResource;

public class UpdateSavingsPot implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();

		Result result = new Result();
		try {
			//Initializing of HolidaysResource through Abstract factory method
			SavingsPotResource savingsPotResource = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(ResourceFactory.class).getResource(SavingsPotResource.class);

			result  = savingsPotResource.updateSavingsPot(methodID, inputArray, request, response);
		}
		catch(Exception e) {
			alert.prepareError("Caught exception at invoke : "+e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}

		return result;
	}
}
