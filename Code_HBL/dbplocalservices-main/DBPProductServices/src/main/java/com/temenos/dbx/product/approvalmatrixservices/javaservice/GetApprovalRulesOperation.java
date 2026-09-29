package com.temenos.dbx.product.approvalmatrixservices.javaservice;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.dbx.product.approvalmatrixservices.resource.api.ApprovalRuleResource;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.kony.dbputilities.util.Log4j2Configurator;

/**
 * 
 * @author KH2387
 * @version 1.0 Implements the {@link JavaService2}
 * Java Service end point to fetch all the Approval matrix rules
 */
public class GetApprovalRulesOperation implements JavaService2  {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Log4j2Configurator.getInstance();
		
		ApprovalRuleResource approvalRulesResource = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(ResourceFactory.class).getResource(ApprovalRuleResource.class);

        Result result = approvalRulesResource.getApprovalRules(methodID, inputArray, request, response);

        return result;
	}
}
