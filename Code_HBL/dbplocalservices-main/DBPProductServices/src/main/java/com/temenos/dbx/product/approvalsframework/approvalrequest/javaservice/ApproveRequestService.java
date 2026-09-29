package com.temenos.dbx.product.approvalsframework.approvalrequest.javaservice;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.approvalsframework.approvalrequest.resource.api.ApprovalRequestResource;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import java.util.Map;

public class ApproveRequestService implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    @Override
    public Object invoke(String method, Object[] inputArray, DataControllerRequest dcRequest, DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();
        Map<String, Object> inputMap = (Map<String, Object>) inputArray[1];
        Result result = new Result();
        ApprovalRequestResource approvalRequestResource = DBPAPIAbstractFactoryImpl.getResource(ApprovalRequestResource.class);
        try {
            result = approvalRequestResource.approveRequest(inputMap, dcRequest);
        } catch (ApplicationException ae) {
            return ae.getErrorCodeEnum().setErrorCode(result);
        } catch (Exception e) {
            alert.prepareError("Internal error occurred at ApproveRequestService exec: " + e).log();
            return ErrorCodeEnum.ERR_87420.setErrorCode(result);
        }
        return result;
    }
}
