package com.temenos.dbx.product.approvalsframework.approvalmatrix.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.approvalsframework.approvalsframeworkcommons.dto.ApprovalRuleDTO;
import com.temenos.dbx.product.commons.dto.FeatureActionDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;

public interface ApprovalMatrixBusinessDelegate extends BusinessDelegate {

    /**
     * @param contractCifMap
     * @return
     * @throws ApplicationException
     * @description returns the composite approval mode for the given contractCifMap
     * @note compositeApprovalMode structure: <contractId, <cifId, isGroupLevel>>
     */
    public Map<String, Map<String, Boolean>> fetchCompositeApprovalModeForContractCifMap(Map<String, Map<String, Map<String, String>>> contractCifMap) throws ApplicationException;

    /**
     * @param coreCustomerId
     * @param contractId
     * @return
     * @throws ApplicationException
     * @description returns the approval mode for the given contract id and core customer id
     */
    public Boolean fetchApprovalModeForContractAndCif(String coreCustomerId, String contractId) throws ApplicationException;

    /**
     * @param contractCifMap
     * @return
     * @throws ApplicationException
     * @description returns the composite approval matrix status for the given contractCifMap
     * @note compositeMatrixStatus structure: <contractId, <cifId, isDisabled>>
     */
    public Map<String, Map<String, Boolean>> fetchCompositeApprovalMatrixStatusForContractCifMap(Map<String, Map<String, Map<String, String>>> contractCifMap,Set<FeatureActionDTO> featureActionIds) throws ApplicationException;

    /**
     * @param coreCustomerId
     * @param contractId
     * @return
     * @throws ApplicationException
     * @description returns the approval matrix status for the given contract id and core customer id
     */
    public Boolean fetchApprovalMatrixStatusForContractAndCif(String coreCustomerId, String contractId) throws ApplicationException;

    /**
     * @param contractId
     * @param coreCustomerIds
     * @param featureActionIds
     * @param accountIds
     * @param legalEntityId
     * @return
     * @throws ApplicationException
     * @description creates the default approval matrix for the given contract id and the set of core customer id's (during contract and cif enrolment)
     */
    public boolean createOrUpdateDefaultApprovalMatrixEntry(String contractId, Set<String> coreCustomerIds, Set<String> featureActionIds, Set<String> accountIds, String legalEntityId) throws ApplicationException;

    /**
     * @param contractId
     * @param coreCustomerId
     * @param featureActionId
     * @param accountIds
     * @param limitTypeId
     * @param limitRules
     * @return
     * @throws ApplicationException
     * @description creates an approval rule as per the definition for the given contract id, core customer id, feature action id, account id's (optional), and limit type id's (MAX_TRANSACTION_LIMIT, DAILY_LIMIT, WEEKLY_LIMIT, NON_MONETARY_LIMIT)
     */
    public boolean createOrUpdateApprovalRule(String contractId, String coreCustomerId, String featureActionId, Set<String> accountIds, String limitTypeId, List<ApprovalRuleDTO> limitRules) throws ApplicationException;

    /**
     * @param contractCifMap
     * @param featureActionIds
     * @param accountId
     * @return approvalMatrixCompositeMap
     * @description fetch the composite approval matrix structure for the given contractCifMap and featureActionIds.
     * The map is grouped on a per-contract, per-cif, per-featureAction and per-limitType basis (further on per-account basis for a monetary action).
     * @note Map Skeleton Structure: <contractId, <cifId, <featureActionId, <limitTypeId, <accountId <matrixId, <approvalruleId|upperlimit|lowerlimit|groupList|groupRule|approverIds, value>>>>>>>
     */
    public Map<String, Map<String, Map<String, Map<String, Map<String, Map<String, Map<String, String>>>>>>> fetchCompositeApprovalMatrixRulesForContractCifMap(Map<String, Map<String, Map<String, String>>> contractCifMap, Set<FeatureActionDTO> featureActionIds, String accountId) throws ApplicationException;

    /**
     * @param coreCustomerId
     * @param contractId
     * @param isGroupLevel
     * @return
     * @description update the approval mode for the given contract id and core customer id
     * @note isGroupLevel = true (group-based approval mode)
     * isGroupLevel = false (user-based approval mode)
     */
    public Boolean updateApprovalMode(String coreCustomerId, String contractId, Boolean isGroupLevel) throws ApplicationException;

   

    public Result createOrUpdateApprovalRuleWithApprovalRuleCheck(String contractId, String coreCustomerId,
			String featureActionId, Set<String> accountIds, String limitTypeId, JSONArray limitRules,
			DataControllerRequest dcRequest) throws ApplicationException;
}