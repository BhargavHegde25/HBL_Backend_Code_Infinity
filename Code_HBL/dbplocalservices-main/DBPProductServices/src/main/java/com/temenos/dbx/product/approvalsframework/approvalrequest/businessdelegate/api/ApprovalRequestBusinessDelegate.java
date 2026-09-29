package com.temenos.dbx.product.approvalsframework.approvalrequest.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRequestDTO;
import com.temenos.dbx.product.approvalsframework.approvalsframeworkcommons.dto.PendingRequestApproversDTO;
import com.temenos.dbx.product.approvalsframework.approvalsframeworkcommons.dto.RequestDTO;
import com.temenos.dbx.product.approvalsframework.approvalsframeworkcommons.dto.RequestHistoryDTO;
import com.temenos.dbx.product.commons.dto.FeatureActionDTO;
import com.temenos.dbx.product.constants.TransactionStatusEnum;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ApprovalRequestBusinessDelegate extends BusinessDelegate {
    /**
     * @param customerId
     * @param contractCifMap
     * @param featureActionDTO
     * @param accountId
     * @param statusEnum
     * @param exhaustedAmounts
     * @return {Map<String, Map<String, Set<String>>>} contractCifTalliedMatrixIds - a set of approval matrix ids per contract and cif basis, which satisfy the rule criteria for the given request
     * @throws ApplicationException
     * @description fetches the approval rules for the given contract-cif map, and makes a decision on whether approvals are needed or not.
     * Based on this decision, it returns a composite map of matrix/rule id(s) for which the request satisfies at a per-contract and cif basis.
     */
    public Map<String, Map<String, Set<String>>> validateForApprovalRules(String customerId, Map<String, Map<String, Map<String, String>>> contractCifMap, FeatureActionDTO featureActionDTO, String actionTypeId, boolean isAccountLevel, String accountId, TransactionStatusEnum statusEnum, Set<String> limitTypeIdsToCheck, Map<String, Double> exhaustedAmounts) throws ApplicationException;

    /**
     * @param customerId                  logged in customerId
     * @param contractCifTalliedMatrixIds
     * @param confirmationNumber          a unique id which serves as an identification in the base feature table
     * @param featureActionId
     * @param accountId
     * @param transactionAmount
     * @param serviceCharges
     * @param additionalMetaInfo
     * @param comments
     * @return Map<String, Map < String, Map < String, Object>>> compositeRequestMap - contains the request id(s), along with the approval matrix ids tagged for those requests
     * @throws ApplicationException
     * @description creates a new request(s) in approval queue as per the contractCifTalliedMatrixIds definition.
     * contractCifTalliedMatrixIds is composite map of list of approval matrix ids which have been tagged on a per-contract and cif basis for request creation
     * They are those rule id's for which the request satisfies.
     */
    public List<RequestDTO> createNewBBRequestForTalliedMatrixIds(String customerId, Map<String, Map<String, Set<String>>> contractCifTalliedMatrixIds, String confirmationNumber, String featureActionId, String accountId, String transactionAmount, String serviceCharges, JSONObject additionalMetaInfo, String comments) throws ApplicationException;

    /**
     * @param requestIds
     * @param isAssociationId
     * @param contractCifMap
     * @param isActiveRulesFetch
     * @return
     */
    public List<RequestDTO> fetchRequestsWithApprovalMatrixInfo(Set<String> requestIds, boolean isAssociationId, Map<String, Map<String, Map<String, String>>> contractCifMap, boolean isActiveRulesFetch) throws ApplicationException;

    /**
     * @param featureActionIds
     * @return
     */
    public Map<String, String> fetchApproveFeatureActionForFeatureActionIds(Set<String> featureActionIds);

    public List<RequestDTO> approveCompositeRequests(String customerId, Set<String> permittedActionIdsSet, List<RequestDTO> requests, String comments) throws ApplicationException;

    public List<RequestDTO> rejectCompositeRequests(String customerId, Set<String> permittedActionIdsSet, List<RequestDTO> requests, String comments) throws ApplicationException;

    public List<RequestDTO> withdrawCompositeRequests(String customerId, Set<String> permittedActionIdsSet, List<RequestDTO> requests, String comments) throws ApplicationException;

    public Set<String> fetchActedRequestIdsInHistoryQueueForCustomer(Set<String> requestIds, String customerId);

    public Map<String, Map<String, Map<String, String>>> fetchContractCifMapForCustomer(String customerId) throws ApplicationException;

    public List<PendingRequestApproversDTO> fetchPendingApproversInfoForRequest(String requestId, boolean isAssociationId) throws ApplicationException;

    public List<RequestHistoryDTO> fetchRequestHistoryInfo(String requestId, boolean isAssociationId) throws ApplicationException;

    public List<ApprovalRequestDTO> fetchAllPendingRequests(String customerId, Set<String> permittedFeatureActionIds);

    public List<ApprovalRequestDTO> fetchAllRequestHistory(String customerId, Set<String> permittedFeatureActionIds);

    public List<ApprovalRequestDTO> fetchAllPendingApprovals(String customerId, Set<String> permittedFeatureActionIds);

    public List<ApprovalRequestDTO> fetchAllApprovalHistory(String customerId, Set<String> permittedFeatureActionIds);

    public Map<String, Object> fetchAllApprovalRequestsCounts(String customerId, Set<String> permittedFeatureActionIds);
}
