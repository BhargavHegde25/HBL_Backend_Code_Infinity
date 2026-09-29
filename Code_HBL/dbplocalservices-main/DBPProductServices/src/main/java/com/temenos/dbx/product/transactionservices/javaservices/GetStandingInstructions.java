package com.temenos.dbx.product.transactionservices.javaservices;

import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.constants.TransactionBackendServicesHelper;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class GetStandingInstructions implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        Result transactionResult = new Result();
        HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
        @SuppressWarnings("unchecked")
        HashMap<String, Object> params = (HashMap<String, Object>) inputArray[1];
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        String serviceName = inputParams.get("serviceName");
        if(inputParams.get("standingOrderId")!=null){
            params.put("standingOrderId",inputParams.get("standingOrderId"));
        }
        transactionResult = TransactionBackendServicesHelper.fetchBackendResponse(serviceName, request, serviceHeaders,
                params);
        if (transactionResult == null) {
            alert.prepareError("Error occured while invoking GetStandingInstructions: ").log();
            return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
        }
        return transactionResult;
    }
}