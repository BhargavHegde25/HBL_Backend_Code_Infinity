package com.kony.adminconsole.service.usermanagement.javaservices;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.service.usermanagement.resource.api.InternalUserManagementResource;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

/**
 * Service to manage the Users(edit Details,edit Role,assign/remove permissions)
 *
 * @author Aditya Mankal, Akhil Alahari
 * 
 */
public class ManageUsersService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
		Log4j2Configurator.getInstance();
    	Result result = new Result();
		try {
			InternalUserManagementResource roleResource = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(ResourceFactory.class).getResource(InternalUserManagementResource.class);
			result = roleResource.editInternalUser(methodID, inputArray, requestInstance, responseInstance);
		}
		catch(Exception exception) {
			alert.prepareError("Exception", exception).log();
			result.addParam(new Param("Status", "Edit Failed", FabricConstants.STRING));
			result.addParam(new Param("Exception Message", exception.getMessage(), FabricConstants.STRING));
            return result;
        }
		return result;
    }
    	
}