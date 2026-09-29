package com.temenos.dbx.product.approvalmatrixservices.javaservice;

import com.kony.dbp.exception.ApplicationException;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.dbx.product.approvalmatrixservices.resource.api.ApprovalMatrixResource;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class UpdateApprovalMatrixOperation  implements JavaService2  {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request, DataControllerResponse response) throws Exception{
		Log4j2Configurator.getInstance();
		Result result = null;
		try {
			ApprovalMatrixResource approvalMatrixResource = DBPAPIAbstractFactoryImpl.getInstance()
			        .getFactoryInstance(ResourceFactory.class).getResource(ApprovalMatrixResource.class);
			result = approvalMatrixResource.updateApprovalMatrixEntry(methodID, inputArray, request, response);			
		} catch (ApplicationException ae) {
			return ae.getErrorCodeEnum().setErrorCode(result);
		}
		catch (Exception e) {
			alert.prepareError("UpdateApprovalMatrixOperation failed :", e).log();
		}
		return result;
	}
}
