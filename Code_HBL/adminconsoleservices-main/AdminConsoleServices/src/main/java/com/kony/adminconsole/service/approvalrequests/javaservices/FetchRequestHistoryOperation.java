package com.kony.adminconsole.service.approvalrequests.javaservices;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.service.approvalrequests.resource.api.ApprovalRequestsResource;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

/**
 * @description API endpoint to get all the requests created by the logged in user
 *
 * @author Sourav Ray Chaudhuri
 */
public class FetchRequestHistoryOperation implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String method, Object[] inputArray, DataControllerRequest dataControllerRequest, DataControllerResponse dataControllerResponse) throws Exception{
		Log4j2Configurator.getInstance();

        try {
            ApprovalRequestsResource resource = DBPAPIAbstractFactoryImpl.getInstance().
                    getFactoryInstance(ResourceFactory.class).getResource(ApprovalRequestsResource.class);

            return resource.getAllRequestHistory(dataControllerRequest);
        } catch (Exception e) {
            alert.prepareError("Exception occurred in invoking FetchRequestHistoryOperation: " + e).log();
            return ErrorCodeEnum.ERR_20001.setErrorCode(new Result());
        }
    }
}
