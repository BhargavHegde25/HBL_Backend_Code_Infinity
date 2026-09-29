package com.temenos.infinity.api.chequemanagement.javaservice;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.chequemanagement.resource.api.ChequeManagementResource;

public class RejectChequeBookOperation implements JavaService2{
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = new Result();
		try {
			ChequeManagementResource chequeManagementResource = DBPAPIAbstractFactoryImpl
					.getResource(ChequeManagementResource.class);
			result = chequeManagementResource.rejectChequeBook(request);
			return result;
		} catch (Exception e) { 
			alert.prepareError("Unable to Reject ChequeBook : "+e).log();
			return ErrorCodeEnum.ERR_26021.setErrorCode(new Result()); 
		}

	}

}
