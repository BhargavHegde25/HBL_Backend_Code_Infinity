package com.temenos.dbx.transaction.javaservice;

import java.util.HashMap;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.infinity.dbx.dbp.jwt.auth.utils.CommonUtils;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class CreateBulkLineItems implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        Result result = new Result();
        try {
            //For T24 Service Call
            HashMap<String, Object> params = (HashMap<String, Object>) inputArray[1];
            HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
            String serviceName = "dbpRbLocalServicesdb";
            String operationName = "dbxdb_bulkwirefilelineitems_create";
            result = CommonUtils.callIntegrationService(request, params, serviceHeaders, serviceName, operationName,
                        true);
        } catch (Exception e) {
            alert.prepareError("Caught exception in invoke method : " + e).log();
            return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
        }

        return result;
    }

}
