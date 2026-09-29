package com.temenos.dbx.product.approvalmatrixservices.resource.impl;

import java.util.List;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.dbx.product.approvalmatrixservices.businessdelegate.api.ApprovalRuleBusinessDelegate;
import com.temenos.dbx.product.approvalmatrixservices.dto.ApprovalRuleDTO;
import com.temenos.dbx.product.approvalmatrixservices.resource.api.ApprovalRuleResource;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;

/**
 * 
 * @author KH2387
 * @version 1.0 Extends the {@link ApprovalRuleResource}
 */
public class ApprovalRuleResourceImpl implements ApprovalRuleResource {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Result getApprovalRules(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {


		Result result = null;
        ApprovalRuleBusinessDelegate approvalRulesBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ApprovalRuleBusinessDelegate.class);
        List<ApprovalRuleDTO> approvalRulesDTO = approvalRulesBusinessDelegate.getApprovalRules();
        if (approvalRulesDTO == null) {
        	alert.prepareError("Exception occured as approvalRulesDTO is NULL").log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
        JSONArray rulesJSONArr = new JSONArray(approvalRulesDTO);
        JSONObject approvalRulesList = new JSONObject();
        approvalRulesList.put("ApprovalRules", rulesJSONArr);
        try {
            result = JSONToResult.convert(approvalRulesList.toString());
        } catch (Exception e) {
            alert.prepareError("Exception occured while marshalling approvalRulesDTO to JSON string", e).log();
            ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
        }

        return result;
	}

}
