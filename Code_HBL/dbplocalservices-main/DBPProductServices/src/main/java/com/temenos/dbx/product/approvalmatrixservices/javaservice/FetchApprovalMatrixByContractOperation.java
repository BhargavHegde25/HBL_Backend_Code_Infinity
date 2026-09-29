package com.temenos.dbx.product.approvalmatrixservices.javaservice;

import com.temenos.dbx.product.approvalmatrixservices.resource.api.ApprovalMatrixResource;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

/**
 * 
 * @author KH9450
 * @version 1.0 Implements the {@link JavaService2}
 * Java Service end point to fetch all the Approval matrix records
 */
public class FetchApprovalMatrixByContractOperation implements JavaService2  {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request, DataControllerResponse response) throws Exception
	{
		Log4j2Configurator.getInstance();
		final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
		final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
		try 
		{	
			ApprovalMatrixResource approvalMatrixResource = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(ResourceFactory.class).getResource(ApprovalMatrixResource.class);
			Result result = approvalMatrixResource.fetchApprovalMatrixByContractId(methodID, inputArray, request, response);
			return result;
		} 
		catch(Exception e) 
		{
			alert.prepareError("Error occured while invoking FetchApprovalMatrixOperation: ",e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
	}
}