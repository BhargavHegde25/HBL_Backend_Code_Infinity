package com.temenos.dbx.product.approvalmatrixservices.javaservice;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.temenos.dbx.product.approvalmatrixservices.resource.api.ApprovalMatrixResource;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * 
 * @author KH2387
 * @version 1.0 Implements the {@link JavaService2}
 * Java Service end point to fetch all the Approval matrix rules
 */
public class IsApprovalMatrixDisabledOperation implements JavaService2  {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request, DataControllerResponse response) throws Exception
		{
		Log4j2Configurator.getInstance();
		Result result = new Result();
		try {
			ApprovalMatrixResource approvalMatrixResource = DBPAPIAbstractFactoryImpl.getInstance()
	                .getFactoryInstance(ResourceFactory.class).getResource(ApprovalMatrixResource.class);
	
	        result = approvalMatrixResource.isApprovalMatrixDisabled(methodID, inputArray, request, response);
		} catch(Exception e) {
			alert.prepareError("Caught exception at invoke of java service : ", e).log();
			return ErrorCodeEnum.ERR_29007.setErrorCode(result);
		}
        return result;
	}
}
