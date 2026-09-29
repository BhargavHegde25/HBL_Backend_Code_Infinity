package com.kony.adminconsole.service.contract.javaservices;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.service.contract.resource.api.ApprovalMatrixManageResource;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * @author kaushik.mondal
 *
 */

public class ApprovalMatrixManageOperation implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
			
	private static final String UPDATE_APPROVALMATRIX_STATUS = "updateApprovalMatrixStatus";
	private static final String IS_APPROVALMATRIX_DISABLED = "isApprovalMatrixDisabled";
	private static final String GET_APPROVALMATRIX = "getApprovalMatrix";

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		Log4j2Configurator.getInstance();

		try {

            if (methodID.equalsIgnoreCase(UPDATE_APPROVALMATRIX_STATUS)) {
                return updateApprovalMatrixStatus(methodID, inputArray, requestInstance, responseInstance);
            } else if (methodID.equalsIgnoreCase(IS_APPROVALMATRIX_DISABLED)) {
                return isApprovalMatrixDisabled(methodID, inputArray, requestInstance, responseInstance);
            } else if (methodID.equalsIgnoreCase(GET_APPROVALMATRIX)) {
                return getApprovalMatrix(methodID, inputArray, requestInstance, responseInstance);
            }

        } catch (Exception e) {
            Result errorResult = new Result();
            diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
            ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
            return errorResult;
        }
        return new Result();
	}
	
	private Object updateApprovalMatrixStatus(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = new Result();
        try {
        	ApprovalMatrixManageResource approvalMatrixResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(ApprovalMatrixManageResource.class);
            result = approvalMatrixResource.updateApprovalMatrixStatus(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of updateApprovalMatrixStatus: ", e).log();
            return ErrorCodeEnum.ERR_20513.setErrorCode(new Result());
        }
        return result;
    }
	
	private Object isApprovalMatrixDisabled(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = new Result();
        try {
        	ApprovalMatrixManageResource approvalMatrixResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(ApprovalMatrixManageResource.class);
            result = approvalMatrixResource.isApprovalMatrixDisabled(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of isApprovalMatrixDisabled: ", e).log();
            return ErrorCodeEnum.ERR_20514.setErrorCode(new Result());
        }
        return result;
    }
	
	private Object getApprovalMatrix(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = new Result();
        try {
        	ApprovalMatrixManageResource approvalMatrixResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(ApprovalMatrixManageResource.class);
            result = approvalMatrixResource.getApprovalMatrix(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of getApprovalMatrix: ", e).log();
            return ErrorCodeEnum.ERR_20515.setErrorCode(new Result());
        }
        return result;
    }

}
