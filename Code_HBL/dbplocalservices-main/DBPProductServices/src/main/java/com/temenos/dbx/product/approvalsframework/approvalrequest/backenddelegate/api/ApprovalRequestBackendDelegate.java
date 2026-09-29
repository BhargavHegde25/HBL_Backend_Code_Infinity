package com.temenos.dbx.product.approvalsframework.approvalrequest.backenddelegate.api;

import com.dbp.core.api.BackendDelegate;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Set;

public interface ApprovalRequestBackendDelegate extends BackendDelegate {

    /**
     * @param customerId
     * @param contractCifTalliedMatrixIds
     * @param confirmationNumber
     * @param featureActionId
     * @param accountId
     * @param transactionAmount
     * @param serviceCharges
     * @param additionalMetaInfo
     * @param comments
     * @return the request id(s) generated, along with the approval matrix ids tallied up with the request(s), including approves information.
     * @description creates a new request(s) in DBXDB given the list of contracts, coreCustomers and the matrix ids for which the request satisfies
     * @note contractCifMapTalliedMatrixIds structure:
     * [
     *      {
     *          "contractId":"353234134",
     *          "cifs":[
     *              {"id":"23212", "matrixIds":[{"id":"34"}, {"id":"12"},...]},
     *              {"id":"84613", "matrixIds":[{"id":"9"}]},...
     *          ]
     *      },...
     * ]
     */
    public JSONArray createNewRequestForTalliedMatrixIdsInDBXDB(String customerId, JSONArray contractCifTalliedMatrixIds, String confirmationNumber, String featureActionId, String accountId, String transactionAmount, String serviceCharges, JSONObject additionalMetaInfo, String comments);

    /**
     *
     * @param customerId
     * @param requestsJSON
     * @return
     */
    public JSONArray approveRequestsInDBXDB(String customerId, JSONArray requestsJSON);

    /**
     *
     * @param customerId
     * @param requestsJSON
     * @return
     */
    public JSONArray rejectRequestsInDBXDB(String customerId, JSONArray requestsJSON);

    /**
     *
     * @param customerId
     * @param requestsJSON
     * @return
     */
    public JSONArray withdrawRequestsInDBXDB(String customerId, JSONArray requestsJSON);

    /**
     * @param requestIds
     * @param customerId
     * @return
     */
    public JSONArray fetchRequestsInApprovalQueueFromDBXDB(Set<String> requestIds, String customerId);

    /**
     * @param assocRequestIds
     * @param customerId
     * @return
     */
    public JSONArray fetchAssociatedRequestsInApprovalQueueFromDBXDB(Set<String> assocRequestIds, String customerId);

    public JSONArray fetchRequestsWithApprovalMatrixInfoFromDBXBD(Set<String> requestIds, boolean isAssociationId, JSONArray contractCifMapJSON, boolean isActiveRulesFetch);

    /**
     * @param requestIds
     * @param customerId
     * @return
     */
    public JSONArray fetchActedRequestIdsInHistoryQueueFromDBXDB(Set<String> requestIds, String customerId);

    /**
     * @param featureActionIds
     * @return
     */
    public JSONArray fetchApproveFeatureActionForFeatureActionIdsFromDBXDB(Set<String> featureActionIds);

    /**
     * @param customerId
     * @return
     */
    public JSONArray fetchContractCifMapForCustomerFromDBXDB(String customerId);

    /**
     * @param requestId
     * @param isAssociationId
     * @return
     */
    public JSONArray fetchPendingApproversInfoForRequestFromDBXDB(String requestId, boolean isAssociationId);

    /**
     * @param requestId
     * @param isAssociationId
     * @return
     */
    public JSONArray fetchRequestHistoryInfoFromDBXDB(String requestId, boolean isAssociationId);

    /**
     * @param customerId
     * @param permittedFeatureActionIds
     * @return
     */
    public JSONArray fetchAllPendingRequestsFromDBXDB(String customerId, Set<String> permittedFeatureActionIds);

    /**
     * @param customerId
     * @param permittedFeatureActionIds
     * @return
     */
    public JSONArray fetchAllRequestHistoryFromDBXDB(String customerId, Set<String> permittedFeatureActionIds);

    /**
     * @param customerId
     * @param permittedFeatureActionIds
     * @return
     */
    public JSONArray fetchAllPendingApprovalsFromDBXDB(String customerId, Set<String> permittedFeatureActionIds);

    /**
     * @param customerId
     * @param permittedFeatureActionIds
     * @return
     */
    public JSONArray fetchAllApprovalHistoryFromDBXDB(String customerId, Set<String> permittedFeatureActionIds);

    /**
     * @param customerId
     * @param permittedFeatureActionIds
     * @return
     */
    public JSONArray fetchAllApprovalRequestsCountsFromDBXDB(String customerId, Set<String> permittedFeatureActionIds);
}
