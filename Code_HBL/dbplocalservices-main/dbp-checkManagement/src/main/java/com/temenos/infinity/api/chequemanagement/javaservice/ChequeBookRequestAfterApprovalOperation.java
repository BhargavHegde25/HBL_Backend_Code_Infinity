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
import com.temenos.infinity.api.chequemanagement.dto.ChequeBook;
import com.temenos.infinity.api.chequemanagement.resource.api.CreateChequeBookResource;

public class ChequeBookRequestAfterApprovalOperation implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		try {
		    CreateChequeBookResource chequeBookResource = DBPAPIAbstractFactoryImpl
					.getResource(CreateChequeBookResource.class);

			Result result = chequeBookResource.executeChequeBookRequestAfterApproval(methodId, inputArray, request, response);
			return result;
		} catch (Exception e) { 
			alert.prepareError("Unable to create order : "+e).log();
			return ErrorCodeEnum.ERR_26021.setErrorCode(new Result()); 
		}
	}
	
}