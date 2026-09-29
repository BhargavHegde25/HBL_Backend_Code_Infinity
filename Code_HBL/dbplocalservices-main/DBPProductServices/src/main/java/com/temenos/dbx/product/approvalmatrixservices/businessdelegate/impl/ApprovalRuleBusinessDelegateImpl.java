package com.temenos.dbx.product.approvalmatrixservices.businessdelegate.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;
import com.dbp.core.util.JSONUtils;
import com.temenos.dbx.product.approvalmatrixservices.businessdelegate.api.ApprovalRuleBusinessDelegate;
import com.temenos.dbx.product.approvalmatrixservices.dto.ApprovalRuleDTO;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;

/**
 * 
 * @author KH2387
 * @version 1.0 Implements the {@link ApprovalRuleBusinessDelegate}
 */
public class ApprovalRuleBusinessDelegateImpl implements ApprovalRuleBusinessDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	/**
	 * method to get the Approval rules
	 * 
	 * @return list of {@link ApprovalRuleDTO}
	 */
	@Override
	public List<ApprovalRuleDTO> getApprovalRules() {

		List<ApprovalRuleDTO> approvalRules = new ArrayList<>();

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_APPROVALRULE_GET;

		HashMap<String, Object> requestParameters = new HashMap<>();
		HashMap<String, Object> requestHeaders = new HashMap<>();

		String approvalRulesResponse = null;
		JSONArray records = null;
		try {
			approvalRulesResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
					operationName, requestParameters, requestHeaders, "");
			JSONObject approvalRulesJSON = new JSONObject(approvalRulesResponse);
			records = approvalRulesJSON.getJSONArray("approvalrule");
		} catch (JSONException e) {
			alert.prepareError("Unable to fetch approvalrule: " + e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at getAllRules method: " + e).log();
			return null;
		}

		try {
			approvalRules = JSONUtils.parseAsList(records.toString(), ApprovalRuleDTO.class);
		} 
		catch (IOException e) {
			alert.prepareError("Caught exception while parsing list: " + e).log();
			return null;
		}
		catch(NullPointerException e) {
			alert.prepareError("NullPointer Exception for records: " + e).log();
			return null;
		}
		
		return approvalRules;
	}

}
