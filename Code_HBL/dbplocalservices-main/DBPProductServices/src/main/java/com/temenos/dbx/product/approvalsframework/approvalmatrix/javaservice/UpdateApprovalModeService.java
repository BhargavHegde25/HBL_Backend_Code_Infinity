package com.temenos.dbx.product.approvalsframework.approvalmatrix.javaservice;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.approvalsframework.approvalmatrix.resource.api.ApprovalMatrixResource;
import com.kony.dbputilities.util.Log4j2Configurator;

import java.util.Map;

public class UpdateApprovalModeService implements JavaService2 {
    @Override
    public Object invoke(String method, Object[] inputArray, DataControllerRequest dcRequest, DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();
        Map<String, Object> requestMap = (Map<String, Object>) inputArray[1];
        Result result = new Result();
        ApprovalMatrixResource approvalMatrixResource = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(ResourceFactory.class).getResource(ApprovalMatrixResource.class);
        try {
            result = approvalMatrixResource.updateApprovalMode(requestMap, dcRequest);
        } catch (ApplicationException ae) {
            return ae.getErrorCodeEnum().setErrorCode(result);
        }
        return result;
    }
}
