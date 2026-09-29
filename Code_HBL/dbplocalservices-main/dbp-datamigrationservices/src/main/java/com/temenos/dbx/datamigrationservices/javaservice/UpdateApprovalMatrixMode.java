package com.temenos.dbx.datamigrationservices.javaservice;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.datamigrationservices.resource.api.MigrateApprovalModeResource;

public class UpdateApprovalMatrixMode  implements JavaService2 {
	LoggerUtil logger = new LoggerUtil(UpdateApprovalMatrixMode.class);
    @Override
    public Object invoke(String method, Object[] inputArray, DataControllerRequest dcRequest, DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        MigrateApprovalModeResource approvalModeResource = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(ResourceFactory.class).getResource(MigrateApprovalModeResource.class);
        try {
            result = approvalModeResource.updateApprovalMode(method, inputArray, dcRequest, dcResponse);
        } catch (ApplicationException ae) {
        	logger.error("Exception occured while updating approval matrix mode " , ae);
            return ae.getErrorCodeEnum().setErrorCode(result);
        }
        return result;
    }
}