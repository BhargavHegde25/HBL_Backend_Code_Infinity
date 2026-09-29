package com.temenos.dbx.product.javaservice;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.resource.api.OrganizationGroupActionLimitsResource;

/**
 * 
 * @author KH2627
 * @version 1.0 Java Service end point to get organization group action limits
 */

public class OrganizationGroupActionLimitsGetServiceOperation implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();

        Result result = new Result();
        try {
            OrganizationGroupActionLimitsResource orgGroupActionLimits = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(OrganizationGroupActionLimitsResource.class);
            result = orgGroupActionLimits.getOrganizationGroupActionLimits(methodID, inputArray, dcRequest, dcResponse);
        } catch (Exception e) {
            alert.prepareError("Caught exception while creating Customer: " + e).log();
        }

        return result;
    }
}
