package com.temenos.dbx.datamigrationservices.javaservice;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.datamigrationservices.resource.api.MigrateApprovalMatrixResource;

public class CreateApprovalMatrixRule implements JavaService2 {
	LoggerUtil logger = new LoggerUtil(CreateApprovalMatrixRule.class);
    @Override
    public Object invoke(String method, Object[] inputArray, DataControllerRequest dcRequest, DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        MigrateApprovalMatrixResource approvalMatrixResource = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(ResourceFactory.class).getResource(MigrateApprovalMatrixResource.class);
        try {
            result = approvalMatrixResource.createOrUpdateApprovalRule(method, inputArray, dcRequest, dcResponse);
        } catch (ApplicationException ae) {
        	logger.error("Exception occured while creating a approval matrix rule " , ae);
            return ae.getErrorCodeEnum().setErrorCode(result);
        }
        return result;
    }
}