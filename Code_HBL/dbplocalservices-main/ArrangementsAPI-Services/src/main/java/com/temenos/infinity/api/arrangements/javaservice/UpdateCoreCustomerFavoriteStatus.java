package com.temenos.infinity.api.arrangements.javaservice;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.constants.ErrorCodeEnum;
import com.temenos.infinity.api.arrangements.resource.api.ArrangementsResource;

/**
 * 
 * @author smugesh
 * @version 1.0 Java Service end point to fetch all the accounts of a particular customer
 */

public class UpdateCoreCustomerFavoriteStatus implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {  	
		Log4j2Configurator.getInstance();
        Result result = new Result();  
        ArrangementsResource AccountsResource = DBPAPIAbstractFactoryImpl.getResource(ArrangementsResource.class);
        try {
        	 String coreCustomerId = request.getParameter("coreCustomerId");
        	 diagnostic.prepareDebug("******coreCustomerId from Request"+coreCustomerId).log();
        
        	ServicesManager servicesManager = request.getServicesManager();
        	String customerId = (String) servicesManager.getIdentityHandler().getUserAttributes().get("customer_id");
            result = AccountsResource.updateCoreCustomerFavoriteStatus(request,customerId);
        } catch (Exception e) {
            alert.prepareError("Unable to fetch records from Backend" , e).log();
            return ErrorCodeEnum.ERR_20041.setErrorCode(new Result());
        }
        return result;
    }

}
