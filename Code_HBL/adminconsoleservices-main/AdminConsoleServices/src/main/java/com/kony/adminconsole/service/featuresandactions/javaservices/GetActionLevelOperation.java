package com.kony.adminconsole.service.featuresandactions.javaservices;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.service.featuresandactions.resource.api.FeaturesAndActionsResource;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.PermissionName;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class GetActionLevelOperation implements JavaService2{
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = new Result();
		try {
			FeaturesAndActionsResource featuresAndActionsResource = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(ResourceFactory.class).getResource(FeaturesAndActionsResource.class);
			result = featuresAndActionsResource.fetchActionLevels(methodId, inputArray, request, response);
		}
		catch(Exception e) {
			alert.prepareError("Caught exception at invoke of GetActionLevelOperation: ", e).log();
			return ErrorCodeEnum.ERR_21998.setErrorCode(new Result()); 
		}
		return result;
	}

}
