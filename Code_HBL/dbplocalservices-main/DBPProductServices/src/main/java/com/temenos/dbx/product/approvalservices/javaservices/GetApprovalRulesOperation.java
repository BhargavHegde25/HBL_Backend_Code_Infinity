package com.temenos.dbx.product.approvalservices.javaservices;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.dbx.product.approvalservices.resource.api.ApprovalRuleResource;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * 
 * @author KH2174
 * @version 1.0
 * Java Service end point to fetch all the Approval matrix rule
 */

public class GetApprovalRulesOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		
		Result result = new Result();
		try {
			//Initializing of ApprovalRuleResource through Abstract factory method
			ApprovalRuleResource ruleResource = DBPAPIAbstractFactoryImpl.getInstance()
	                .getFactoryInstance(ResourceFactory.class).getResource(ApprovalRuleResource.class);
			
			result  = ruleResource.getRules(methodID, inputArray, request, response);
		}
		catch(Exception e) {
			alert.prepareError("Caught exception at invoke of GetApprovalRulesOperation: ", e).log();
		}
		
		return result;
	}
	
}
