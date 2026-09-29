package com.kony.adminconsole.service.customerrole.javaservices;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.service.customerrole.resource.api.CustomerRoleResource;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * @author kruthi.manojna
 * this method gives common features and actions and restrictive limits from 
 * given role and service definition
 */

public class CommonFeatureActionsOperation implements JavaService2{

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = new Result();
		try {
			CustomerRoleResource roleResource = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(ResourceFactory.class).getResource(CustomerRoleResource.class);
			result = roleResource.getCommonFeatureActions(methodId, inputArray, request, response);
		}
		catch(Exception e) {
			alert.prepareError("Caught exception at invoke of CommonFeatureActionsOperation : ", e).log();
			return ErrorCodeEnum.ERR_21991.setErrorCode(new Result());
		}
		return result;
	}

}
