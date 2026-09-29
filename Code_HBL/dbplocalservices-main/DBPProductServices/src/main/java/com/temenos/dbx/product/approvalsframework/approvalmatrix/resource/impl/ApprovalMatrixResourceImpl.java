package com.temenos.dbx.product.approvalsframework.approvalmatrix.resource.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.approvalsframework.approvalmatrix.businessdelegate.api.ApprovalMatrixBusinessDelegate;
import com.temenos.dbx.product.approvalsframework.approvalmatrix.resource.api.ApprovalMatrixResource;
import com.temenos.dbx.product.approvalsframework.approvalsframeworkcommons.dto.ApprovalRuleDTO;
import com.temenos.dbx.product.approvalsframework.approvalsframeworkcommons.util.ApprovalUtilities;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.FeatureAction;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.*;

public class ApprovalMatrixResourceImpl implements ApprovalMatrixResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    ApprovalMatrixBusinessDelegate approvalMatrixBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalMatrixBusinessDelegate.class);

    @Override
    public Result fetchApprovalMatrix(Map<String, Object> requestMap, DataControllerRequest dcRequest) throws ApplicationException {
        return null;
    }

    @Override
    public Result createOrUpdateApprovalRule(Map<String, Object> requestMap, DataControllerRequest dcRequest) throws ApplicationException {
        Result result = new Result();
        String contractId = requestMap.containsKey("contractId") ? (String) requestMap.get("contractId") : null;
        String coreCustomerId = requestMap.containsKey("cif") ? (String) requestMap.get("cif") : null;
        String featureActionId = requestMap.containsKey("actionId") ? (String) requestMap.get("actionId") : null;
        String accountIds = requestMap.containsKey("accountId") ? (String) requestMap.get("accountId") : null;
        Set<String> accountIdsList = (!StringUtils.isEmpty(accountIds)) ? new HashSet<>(Arrays.asList(accountIds.split(","))) : new HashSet<>();
        String limitTypeId = requestMap.containsKey("limitTypeId") ? (String) requestMap.get("limitTypeId") : null;
        JSONArray limits;
        String limitsFromService = requestMap.containsKey("limits") ? (String) requestMap.get("limits") : "{}";
        try {
            limits = requestMap.containsKey("limits") ? new JSONArray((String)limitsFromService.replace("\\\"","\"") ) : null;
        } catch (JSONException je) {
            alert.prepareError("Invalid format for limit ranges: " + requestMap.get("limits") + " : " + je).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_87113);
        }
        if (contractId == null) {
            throw new ApplicationException(ErrorCodeEnum.ERR_87101);
        }
        if (coreCustomerId == null) {
            throw new ApplicationException(ErrorCodeEnum.ERR_87102);
        }
        if (featureActionId == null) {
            throw new ApplicationException(ErrorCodeEnum.ERR_87103);
        }
        if (limitTypeId == null) {
            throw new ApplicationException(ErrorCodeEnum.ERR_87105);
        }
        Set<String> permittedLimitTypeIds = new HashSet<>(Arrays.asList(Constants.MAX_TRANSACTION_LIMIT, Constants.DAILY_LIMIT, Constants.WEEKLY_LIMIT, Constants.NON_MONETARY_LIMIT));
        if (!permittedLimitTypeIds.contains(limitTypeId)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_87106);
        }
        if (limits == null || limits.isEmpty()) {
            throw new ApplicationException(ErrorCodeEnum.ERR_87114);
        }
        if (!_isAllowedForApprovalMatrixMods(contractId, coreCustomerId, dcRequest)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10097);
        }
        return  approvalMatrixBusinessDelegate.createOrUpdateApprovalRuleWithApprovalRuleCheck(contractId, coreCustomerId, featureActionId, accountIdsList, limitTypeId, limits,dcRequest);
        
		/*
		 * if (ruleUpdateStatus == false) { throw new
		 * ApplicationException(ErrorCodeEnum.ERR_87199); } else {
		 * result.addParam("status", "Success"); return result; }
		 */
    }

    @Override
    public Result deleteApprovalRule(Map<String, Object> requestMap, DataControllerRequest dcRequest) throws ApplicationException {
        Result result = new Result();
        String contractId = requestMap.containsKey("contractId") ? (String) requestMap.get("contractId") : null;
        String coreCustomerId = requestMap.containsKey("cif") ? (String) requestMap.get("cif") : null;
        String featureActionId = requestMap.containsKey("actionId") ? (String) requestMap.get("actionId") : null;
        String accountIds = requestMap.containsKey("accountId") ? (String) requestMap.get("accountId") : null;
        Set<String> accountIdsList = (!StringUtils.isEmpty(accountIds)) ? new HashSet<>(Arrays.asList(accountIds.split(","))) : new HashSet<>();
        String limitTypeId = requestMap.containsKey("limitTypeId") ? (String) requestMap.get("limitTypeId") : null;
        if (contractId == null) {
            throw new ApplicationException(ErrorCodeEnum.ERR_87101);
        }
        if (coreCustomerId == null) {
            throw new ApplicationException(ErrorCodeEnum.ERR_87102);
        }
        if (featureActionId == null) {
            throw new ApplicationException(ErrorCodeEnum.ERR_87103);
        }
        Set<String> permittedLimitTypeIds = new HashSet<>(Arrays.asList(Constants.MAX_TRANSACTION_LIMIT, Constants.DAILY_LIMIT, Constants.WEEKLY_LIMIT, Constants.NON_MONETARY_LIMIT));
        if (limitTypeId == null || !permittedLimitTypeIds.contains(limitTypeId)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_87115);
        }
        if (!_isAllowedForApprovalMatrixMods(contractId, coreCustomerId, dcRequest)) {
            return ErrorCodeEnum.ERR_10097.setErrorCode(result);
        }
        List<ApprovalRuleDTO> limitsArray = new ArrayList<>();
        // For deleting a rule, set the rule to the DEFAULT RULE type
        ApprovalRuleDTO limitItem = new ApprovalRuleDTO(-1.0, -1.0, "NO_APPROVAL", null, null, null);
        limitsArray.add(limitItem);
        boolean ruleDeleteStatus = approvalMatrixBusinessDelegate.createOrUpdateApprovalRule(contractId, coreCustomerId, featureActionId, accountIdsList, limitTypeId, limitsArray);
        if (ruleDeleteStatus == false) {
            return ErrorCodeEnum.ERR_87199.setErrorCode(result);
        } else {
            result.addParam("status", "Success");
            return result;
        }
    }

    @Override
    public Result updateApprovalMode(Map<String, Object> requestMap, DataControllerRequest dcRequest) throws ApplicationException {
        Result result = new Result();
        String contractId = requestMap.containsKey("contractId") ? (String) requestMap.get("contractId") : null;
        String coreCustomerId = requestMap.containsKey("coreCustomerId") ? (String) requestMap.get("coreCustomerId") : null;
        Boolean isGroupLevel = requestMap.containsKey("isGroupLevel") ? (Boolean) requestMap.get("isGroupLevel") : false;

        if (contractId == null) {
            throw new ApplicationException(ErrorCodeEnum.ERR_87101);
        }
        if (coreCustomerId == null) {
            throw new ApplicationException(ErrorCodeEnum.ERR_87102);
        }

        Boolean isGroupMatrix = approvalMatrixBusinessDelegate.fetchApprovalModeForContractAndCif(coreCustomerId, contractId);

        if (isGroupLevel == isGroupMatrix) {
            // cannot update approval mode with the same mode definition
            result.addParam("status", "Update Failed");
            return result;
        }
        Boolean updateStatus = approvalMatrixBusinessDelegate.updateApprovalMode(coreCustomerId, contractId, isGroupLevel);
        if (updateStatus) {
            result.addParam("status", "Updated Successfully");
        } else {
            result.addParam("status", "Update Failed");
        }
        return result;
    }

    @Override
    public Result fetchApprovalMode(Map<String, Object> requestMap, DataControllerRequest dcRequest) throws ApplicationException {
        Result result = new Result();
        String contractId = requestMap.containsKey("contractId") ? (String) requestMap.get("contractId") : null;
        String coreCustomerId = requestMap.containsKey("coreCustomerId") ? (String) requestMap.get("coreCustomerId") : null;

        if (contractId == null) {
            throw new ApplicationException(ErrorCodeEnum.ERR_87101);
        }
        if (coreCustomerId == null) {
            throw new ApplicationException(ErrorCodeEnum.ERR_87102);
        }

        Map<String, Map<String, Map<String, String>>> contractCifMap = new HashMap<>();
        contractCifMap.put(contractId, new HashMap<>());
        contractCifMap.get(contractId).put(coreCustomerId, new HashMap<>());
        contractCifMap.get(contractId).get(coreCustomerId).put("companyId", contractId + "_" + coreCustomerId);

        Map<String, Map<String, Boolean>> compositeApprovalModeRes = approvalMatrixBusinessDelegate.fetchCompositeApprovalModeForContractCifMap(contractCifMap);

        result.addParam("contractId", contractId);
        result.addParam("coreCustomerId", coreCustomerId);
        result.addParam("isGroupLevel", String.valueOf(compositeApprovalModeRes.get(contractId).get(coreCustomerId)));
        return result;
    }

    @Override
    public Result fetchApprovalMatrixStatus(Map<String, Object> requestMap, DataControllerRequest dcRequest) throws ApplicationException {
        Result result = new Result();
        String contractId = requestMap.containsKey("contractId") ? (String) requestMap.get("contractId") : null;
        String coreCustomerId = requestMap.containsKey("cif") ? (String) requestMap.get("cif") : null;

        if (contractId == null) {

        }
        if (coreCustomerId == null) {

        }

        Map<String, Map<String, Map<String, String>>> contractCifMap = new HashMap<>();
        contractCifMap.put(contractId, new HashMap<>());
        contractCifMap.get(contractId).put(coreCustomerId, new HashMap<>());
        contractCifMap.get(contractId).get(coreCustomerId).put("companyId", contractId + "_" + coreCustomerId);

        Map<String, Map<String, Boolean>> compositeApprovalMatrixStatusRes = approvalMatrixBusinessDelegate.fetchCompositeApprovalMatrixStatusForContractCifMap(contractCifMap,null);

        result.addParam("contractId", contractId);
        result.addParam("cif", coreCustomerId);
        result.addParam("isDisabled", String.valueOf(compositeApprovalMatrixStatusRes.get(contractId).get(coreCustomerId)));
        return result;
    }

    private boolean _isAllowedForApprovalMatrixMods(String contractId, String coreCustomerId, DataControllerRequest dcRequest) throws ApplicationException {
        Map<String, Object> customerSessionMap = CustomerSession.getCustomerMap(dcRequest);
        if (CustomerSession.IsAPIUser(customerSessionMap)) {
            // ADMIN user is always allowed to perform operations on managing approval matrix
            return true;
        }
        // 1st - check if the logged-in user is part of the contract or cif
        // 2nd - check if logged-in user has APPROVAL_MATRIX_MANAGE permission
        String customerId = CustomerSession.getCustomerId(customerSessionMap);
        Map<String, Map<String, Map<String, String>>> contractCifMap = ApprovalUtilities.fetchContractCifMapForCustomer(customerId);
        if (contractCifMap == null) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10789);
        }
        if (!contractCifMap.containsKey(contractId)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_21030);
        } else if (!contractCifMap.get(contractId).containsKey(coreCustomerId)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_21030);
        }
        Set<String> permittedFeatureActionIds = CustomerSession.getPermittedActionIdsSet(dcRequest);
        if (permittedFeatureActionIds == null || permittedFeatureActionIds.isEmpty()) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10815);
        }
        if (!permittedFeatureActionIds.contains(FeatureAction.APPROVAL_MATRIX_MANAGE)) {
            return false;
        }
        return true;
    }
}
