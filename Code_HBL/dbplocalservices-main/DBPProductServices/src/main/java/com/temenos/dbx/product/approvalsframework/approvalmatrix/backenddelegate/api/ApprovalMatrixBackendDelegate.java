package com.temenos.dbx.product.approvalsframework.approvalmatrix.backenddelegate.api;

import com.dbp.core.api.BackendDelegate;
import org.json.JSONArray;

import java.util.Set;

public interface ApprovalMatrixBackendDelegate extends BackendDelegate {

    /**
     * @param contractId
     * @param coreCustomerIds
     * @param featureActionIds
     * @param accountIds
     * @param isGroupMatrix
     * @param isDefaultDisabled
     * @param legalEntityCurrency
     * @return
     * @description Initialize the default approval matrix and approval matrix template for a given contract and core customer (under a given legal entity)
     */
    public boolean createOrUpdateDefaultApprovalMatrixEntryInDBXDB(String contractId, Set<String> coreCustomerIds, Set<String> featureActionIds, Set<String> accountIds, boolean isGroupMatrix, boolean isDefaultDisabled, String legalEntityCurrency);

    /**
     * @param contractCifMapJSON
     * @return
     * @description Fetch the composite approval mode for the approval matrix configured for a given contract cif map
     * @note contract cif map JSON sample structure: [{"contractId":"4204010299","cifs":[{"id":"1605506"},{"id":"1065631"}]}]
     */
    public JSONArray fetchCompositeApprovalModeFromDBXDB(JSONArray contractCifMapJSON);

    /**
     * @param coreCustomerId
     * @param contractId
     * @return
     * @description Fetch the approval mode for the approval matrix configured for a given contract and core customer
     */
    public JSONArray fetchApprovalModeFromDBXDB(String coreCustomerId, String contractId);

    public Boolean updateApprovalModeInDBXDB(String coreCustomerId, String contractId, boolean isGroupLevel);

    /**
     * @param contractCifMapJSON
     * @return true - approval matrix is enabled, false - approval matrix is disabled
     * @description fetch the composite status (enabled/disabled) of the approval matrix for the given contract cif map
     * @note contract cif map JSON sample structure: [{"contractId":"4204010299","cifs":[{"id":"1605506"},{"id":"1065631"}]}]
     */
    public JSONArray fetchCompositeApprovalMatrixStatusFromDBXDB(JSONArray contractCifMapJSON,Set<String> featureActionIds);

    /**
     * @param coreCustomerId
     * @param contractId
     * @return
     * @description fetch the approval matrix status (enabled/disabled) of the approval matrix for the given contract id and core customer id
     */
    public JSONArray fetchApprovalMatrixStatusFromDBXDB(String coreCustomerId, String contractId);

    /**
     * @param contractId
     * @param coreCustomerId
     * @param featureActionId
     * @param accountIds
     * @param limitTypeId
     * @param limits
     * @return
     * @desription configures the approval rule for the given contract id, core customer id, feature action id, account id's (optional) and limit type id (MAX_TRANSACTION_LIMIT, DAILY_LIMIT, WEEKLY_LIMIT, NON_MONETARY_LIMIT)
     * @note limits JSONArray sample structure :
     * for non-monetary limit type (group-based approval): [{"groupRule":"[[0,2],[2,0]]","groupList":"[8624f111-b80f-11ed-9e32-3814283bc7ef,8ddd47fc-b80f-11ed-9e32-3814283bc7ef]","lowerlimit":-1,"upperlimit":-1}]
     * for monetary limit type (user-based approval): [{"approvalruleId":"ANY_ONE","approvers":[{"approverId":"4823956416"},{"approverId":"4823956417"}],"lowerlimit":-1,"upperlimit":10},{"approvalruleId":"ANY_TWO","approvers":[{"approverId":"4823956417"},{"approverId":"4823956418"},{"approverId":"4823956419"}],"lowerlimit":10,"upperlimit":95},{"approvalruleId":"ANY_THREE","approvers":[{"approverId":"4823956417"},{"approverId":"4823956418"},{"approverId":"4823956419"},{"approverId":"4823956420"}],"lowerlimit":95,"upperlimit":-1}]
     */
    public boolean createOrUpdateApprovalRuleInDBXDB(String contractId, String coreCustomerId, String featureActionId, Set<String> accountIds, String limitTypeId, JSONArray limits);

    /**
     * @param contractCifMap
     * @param featureActionIds
     * @param accountId
     * @return
     * @description fetch the approval matrix rules configured for the given set of feature action id's and the contract cif map
     * @note contract cif map JSON sample structure: [{"contractId":"4204010299","cifs":[{"id":"1605506"},{"id":"1065631"}]}]
     */
    public JSONArray fetchCompositeApprovalMatrixRulesForContractCifMap(JSONArray contractCifMap, Set<String> featureActionIds, String accountId);
}
