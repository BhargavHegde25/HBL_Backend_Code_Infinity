package com.kony.adminconsole.service.customermanagement;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.utilities.DBPServices;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class SendUnlockLinkToCustomer implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
    private static final String INPUT_USERNAME = "username";

    @Override
    public Object invoke(String methodId, Object[] input, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        Result processedResult = new Result();
        try {

            String username = requestInstance.getParameter(INPUT_USERNAME);
            JSONObject unlockResponse = DBPServices.sendUnlockLinkToCustomer(username, requestInstance);
            if (unlockResponse == null || !unlockResponse.has(FabricConstants.OPSTATUS)
                    || unlockResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_20718.setErrorCode(processedResult);
                if (unlockResponse != null) {
                    processedResult.addParam(
                            new Param("FailureReason", String.valueOf(unlockResponse), FabricConstants.STRING));
                }
            }
            processedResult.addParam(new Param("BackendResponse", unlockResponse.toString()));
            processedResult.addParam(new Param("status", "Success", FabricConstants.STRING));
        } catch (Exception e) {
            alert.prepareError("Unexpected error has occurred. " + e.getMessage()).log();
            ErrorCodeEnum.ERR_20718.setErrorCode(processedResult);
            processedResult.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
        }
        return processedResult;
    }

}
