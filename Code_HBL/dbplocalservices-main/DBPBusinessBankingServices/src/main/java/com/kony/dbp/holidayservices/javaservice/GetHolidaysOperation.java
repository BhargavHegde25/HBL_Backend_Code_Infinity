package com.kony.dbp.holidayservices.javaservice;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.holidayservices.javaservice.GetHolidaysOperation;
import com.kony.dbp.holidayservices.resource.api.HolidayServicesResource;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * 
 * @author KH2394
 * @version 1.0
 * Java Service end point to get holidays of current year
 */
public class GetHolidaysOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();

		Result result = new Result();
		try {
			//Initializing of HolidaysResource through Abstract factory method
			HolidayServicesResource ruleResource = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(ResourceFactory.class).getResource(HolidayServicesResource.class);

			result  = ruleResource.getHolidays(methodID, inputArray, request, response);
		}
		catch(Exception e) {
			alert.prepareError("Caught exception at invoke of GetHolidaysOperation: "+e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}

		return result;
	}
}
