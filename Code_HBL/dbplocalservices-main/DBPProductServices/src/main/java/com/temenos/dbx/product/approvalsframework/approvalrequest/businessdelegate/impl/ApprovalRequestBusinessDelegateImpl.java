package com.temenos.dbx.product.approvalsframework.approvalrequest.businessdelegate.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbx.objects.Customer;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRequestDTO;
import com.temenos.dbx.product.approvalservices.dto.BBRequestDTO;
import com.temenos.dbx.product.approvalsframework.approvalmatrix.businessdelegate.api.ApprovalMatrixBusinessDelegate;
import com.temenos.dbx.product.approvalsframework.approvalrequest.backenddelegate.api.ApprovalRequestBackendDelegate;
import com.temenos.dbx.product.approvalsframework.approvalrequest.businessdelegate.api.ApprovalRequestBusinessDelegate;
import com.temenos.dbx.product.approvalsframework.approvalsframeworkcommons.dto.*;
import com.temenos.dbx.product.approvalsframework.approvalsframeworkcommons.util.ApprovalUtilities;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.commons.dto.FeatureActionDTO;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.TransactionStatusEnum;
import com.temenos.dbx.product.signatorygroupservices.businessdelegate.api.SignatoryGroupBusinessDelegate;
import com.temenos.dbx.product.signatorygroupservices.dto.CustomerSignatoryGroupDTO;
import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ApprovalRequestBusinessDelegateImpl implements ApprovalRequestBusinessDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    ApprovalMatrixBusinessDelegate approvalMatrixBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalMatrixBusinessDelegate.class);
    ApprovalRequestBackendDelegate approvalRequestBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(ApprovalRequestBackendDelegate.class);
    SignatoryGroupBusinessDelegate signatoryGroupBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(SignatoryGroupBusinessDelegate.class);
    ApplicationBusinessDelegate applicationBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);

    @Override
    public Map<String, Map<String, Set<String>>> validateForApprovalRules(String customerId, Map<String, Map<String, Map<String, String>>> contractCifMap, FeatureActionDTO featureActionDTO, String actionTypeId, boolean isAccountLevel, String accountId, TransactionStatusEnum statusEnum, Set<String> limitTypeIdsToCheck, Map<String, Double> exhaustedAmounts) throws ApplicationException {

        Map<String, Map<String, Map<String, Map<String, Map<String, Map<String, Map<String, String>>>>>>> compositeRulesMap = approvalMatrixBusinessDelegate.fetchCompositeApprovalMatrixRulesForContractCifMap(contractCifMap, new HashSet<>(Arrays.asList(featureActionDTO)), accountId);

        /**
         * @note fetch the composite approvalmatrixstatus for the given contractcifmap. If for any one of the contract, (the approvalmatrix status is disabled or no rule is set) and STP is disabled
         * <contractId, <cifId, isDisabled(true/false)>>
         */
        Map<String, Map<String, Boolean>> approvalMatrixStatus = approvalMatrixBusinessDelegate.fetchCompositeApprovalMatrixStatusForContractCifMap(contractCifMap,new HashSet<>(Arrays.asList(featureActionDTO)));

        /**
         * @note fetch the STP config set up for approvalmatrix
         */
        Map<String, Boolean> STPConfig = ApprovalUtilities.fetchSTPConfigForApprovalMatrix();
        
        //To findout this is edit flow
        Set<String> featureActionIds = new HashSet<>();
        for (Map.Entry<String, Map<String, Map<String, String>>> contractEntry : contractCifMap.entrySet()) {
            for (Map.Entry<String, Map<String, String>> cifEntry : contractEntry.getValue().entrySet()) {
            	Set<FeatureActionDTO> featureActionDTOs =new HashSet<>(Arrays.asList(featureActionDTO));
                for (FeatureActionDTO featureActionDTOObj : featureActionDTOs) {
                    String featureActionId = featureActionDTOObj.getFeatureActionId();
                    featureActionIds.add(featureActionId);
                }
            }
        }
        String featureActionIDtemp=String.join(",", featureActionIds);
        // In case of edit flow contractCifMap is coming as below and taking only third one which has valid company id"{9667503401={={companyId=9667503401_}}, 3645002553={={companyId=3645002553_}}, 4170544305={191441={companyId=4170544305_191441}}}";
        if(featureActionIDtemp.contains("EDIT_RECEPIENT_OPTIONAL")){
        	JSONObject totalContractCifMap = new JSONObject(contractCifMap);
        	// Create a new validContractCifMap to store valid objects
            Map<String, Map<String, Map<String, String>>> validContractCifMap = new HashMap<>();
            // Iterate through the contractCifMap to find valid objects and add them to validContractCifMap
            for (String outerKey : totalContractCifMap.keySet()) {
                JSONObject innerObject = totalContractCifMap.getJSONObject(outerKey);
                for (String middleKey : innerObject.keySet()) {
                	if(!middleKey.isEmpty()) {
                    JSONObject innerInnerObject = innerObject.getJSONObject(middleKey);
                    for (String innermostKey : innerInnerObject.keySet()) {
                        String innermostObject = innerInnerObject.getString(innermostKey);
                        String[] parts = innermostObject.split("_");
                        if (parts.length == 2) {
                            // Create a new valid object with the same structure
                            Map<String, Map<String, String>> validInnerMap = new HashMap<>();
                            Map<String, String> validInnermostMap = new HashMap<>();
                            validInnermostMap.put("companyId", innermostObject);
                            validInnerMap.put(middleKey, validInnermostMap);
                            validContractCifMap.put(outerKey, validInnerMap);
                        }
                    }
                }
              }
            }
            if (!validContractCifMap.isEmpty()) {
            	contractCifMap.clear();
            	contractCifMap.putAll(validContractCifMap);
            }
        }

        /**
         * @note talliedMatrixIds are the set of matrix ids for which the requests are to be created
         * Contains a set of matrix rule id's which are tallied for the given request, per contract and cif basis
         */
        Map<String, Map<String, Set<String>>> contractCifTalliedMatrixIds = new HashMap<>();
        for (Map.Entry<String, Map<String, Map<String, String>>> contractEntry : contractCifMap.entrySet()) {
            String contractId = contractEntry.getKey();
            if (!contractCifTalliedMatrixIds.containsKey(contractId)) {
                contractCifTalliedMatrixIds.put(contractId, new HashMap<>());
            }
            for (Map.Entry<String, Map<String, String>> cifEntry : contractEntry.getValue().entrySet()) {
                String cifId = cifEntry.getKey();
                if (!contractCifTalliedMatrixIds.get(contractId).containsKey(cifId)) {
                    contractCifTalliedMatrixIds.get(contractId).put(cifId, new HashSet<>());
                }
                /**
                 * check whether for the given contractId and cifId, the approvalmatrix is disabled or not
                 * if approval matrix is disabled, check whether STP is allowed
                 */
                Boolean isMatrixDisabled = approvalMatrixStatus.get(contractId).get(cifId);
                if (isMatrixDisabled) {
                    // continue over to next cif for checking the approval matrix rules
                    continue;
                }

                if (compositeRulesMap.get(contractId).get(cifId).get(featureActionDTO.getFeatureActionId()).containsKey(Constants.NON_MONETARY_LIMIT)) {
                    /**
                     * @note if the account id under this category is Constants.CUSTOMERID_LEVEL, then the feature action is a customer-level action
                     */
                    // MAP BOILERPLATE: <matrixId, <approvalruleId/upperlimit/lowerlimit/groupList/groupRule/approverIds, value>>
                    Map<String, Map<String, String>> matrixRulesMap = new HashMap<>();
                    if (compositeRulesMap.get(contractId).get(cifId).get(featureActionDTO.getFeatureActionId()).get(Constants.NON_MONETARY_LIMIT).containsKey(Constants.CUSTOMERID_LEVEL)) {
                        matrixRulesMap = compositeRulesMap.get(contractId).get(cifId).get(featureActionDTO.getFeatureActionId()).get(Constants.NON_MONETARY_LIMIT).get(Constants.CUSTOMERID_LEVEL);
                    } else if (compositeRulesMap.get(contractId).get(cifId).get(featureActionDTO.getFeatureActionId()).get(Constants.NON_MONETARY_LIMIT).containsKey(accountId)) {
                        matrixRulesMap = compositeRulesMap.get(contractId).get(cifId).get(featureActionDTO.getFeatureActionId()).get(Constants.NON_MONETARY_LIMIT).get(accountId);
                    }
                    if (!matrixRulesMap.isEmpty()) {
                        for (Map.Entry<String, Map<String, String>> matrixIdsEntry : matrixRulesMap.entrySet()) {
                            String matrixId = matrixIdsEntry.getKey();
                            Map<String, String> matrixRuleData = matrixIdsEntry.getValue();
                            String approvalruleId = matrixRuleData.get("approvalruleId");
                            if (approvalruleId == null) {
                                approvalruleId = "";
                            }
                            /**
                             * @note for a non-monetary action, the clause for approval required is either a yes or a no
                             * Hence, if approval is not required, the approvalruleId will be NO_APPROVAL. If not, then approval is needed.
                             */
                            if (!approvalruleId.equalsIgnoreCase(Constants.NO_APPROVAL)) {
                                contractCifTalliedMatrixIds.get(contractId).get(cifId).add(matrixId);
                            }
                        }
                    } else {
                        // NON-MONETARY-LIMIT type does not contain any rule for a customer-level action or for an account-level action for that specific action. Hence, check the STP config
                        if (!STPConfig.get(Constants.AM_NON_MONETORY_NO_RULES_ALLOW_STP)) {
                            throw new ApplicationException(ErrorCodeEnum.ERR_29033);
                        }
                    }
                } else {
                    for (Map.Entry<String, Map<String, Map<String, Map<String, String>>>> monetaryLimitRulesEntry : compositeRulesMap.get(contractId).get(cifId).get(featureActionDTO.getFeatureActionId()).entrySet()) {
                        String limitTypeId = monetaryLimitRulesEntry.getKey();
                        Double amountToCheck = exhaustedAmounts.get(limitTypeId);
                        Map<String, Map<String, Map<String, String>>> accountLimitsMap = monetaryLimitRulesEntry.getValue();
                        if (!accountLimitsMap.containsKey(accountId)) {
                            // No limits are set at the account-level for the given account id. Hence, check the STP config set for max, daily and weekly type rules
                            switch (limitTypeId) {
                                case Constants.MAX_TRANSACTION_LIMIT:
                                    if (!STPConfig.get(Constants.AM_MAX_LIMIT_NO_RULES_ALLOW_STP)) {
                                        throw new ApplicationException(ErrorCodeEnum.ERR_29033);
                                    }
                                    break;
                                case Constants.DAILY_LIMIT:
                                    if (!STPConfig.get(Constants.AM_DAILY_LIMIT_NO_RULES_ALLOW_STP)) {
                                        throw new ApplicationException(ErrorCodeEnum.ERR_29033);
                                    }
                                    break;
                                case Constants.WEEKLY_LIMIT:
                                    if (!STPConfig.get(Constants.AM_WEEKLY_LIMIT_NO_RULES_ALLOW_STP)) {
                                        throw new ApplicationException(ErrorCodeEnum.ERR_29033);
                                    }
                                    break;
                            }
                        } else {
                            Map<String, Map<String, String>> matrixIdsMap = accountLimitsMap.get(accountId);
                            for (Map.Entry<String, Map<String, String>> matrixIdsEntry : matrixIdsMap.entrySet()) {
                                String matrixId = matrixIdsEntry.getKey();
                                Map<String, String> matrixRuleData = matrixIdsEntry.getValue();
                                String upperLimitStr = matrixRuleData.get("upperlimit");
                                String lowerLimitStr = matrixRuleData.get("lowerlimit");
                                String approvalruleId = matrixRuleData.get("approvalruleId");
                                String approverIds = matrixRuleData.get("approverIds");
                                String groupList = matrixRuleData.get("groupList");
                                String groupRule = matrixRuleData.get("groupRule");
                                Double upperLimit = 0.0, lowerLimit = 0.0;
                                if (approvalruleId == null) {
                                    approvalruleId = "";
                                }
                                try {
                                    upperLimit = Double.parseDouble(upperLimitStr);
                                    lowerLimit = Double.parseDouble(lowerLimitStr);
                                } catch (NumberFormatException nme) {
                                    throw new ApplicationException(ErrorCodeEnum.ERR_87212);
                                }
                                /**
                                 * @note for a monetary action, the clause for approval required is dependent on the limit ranges specified
                                 * Hence, if the amount lies in the range of upper limit and lower limit, then that rule is tallied up
                                 * @case1 if lowerlimit = -1, upperlimit = X -> amount <= X
                                 * @case2 if lowerlimit = X, upperlimit = -1 -> amount > X
                                 * @case3 if lowerlimit = X, upperlimit = Y -> amount > X && amount <= Y
                                 */
                                if (!approvalruleId.equalsIgnoreCase(Constants.NO_APPROVAL)) {
                                    if (lowerLimit == -1.0) {
                                        if (amountToCheck.compareTo(upperLimit) <= 0) {
                                            contractCifTalliedMatrixIds.get(contractId).get(cifId).add(matrixId);
                                        }
                                    } else if (upperLimit == -1) {
                                        if (amountToCheck.compareTo(lowerLimit) > 0) {
                                            contractCifTalliedMatrixIds.get(contractId).get(cifId).add(matrixId);
                                        }
                                    } else {
                                        if (amountToCheck.compareTo(lowerLimit) > 0 && amountToCheck.compareTo(upperLimit) <= 0) {
                                            contractCifTalliedMatrixIds.get(contractId).get(cifId).add(matrixId);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return contractCifTalliedMatrixIds;
    }

    @Override
    public List<RequestDTO> createNewBBRequestForTalliedMatrixIds(String customerId, Map<String, Map<String, Set<String>>> contractCifTalliedMatrixIds, String confirmationNumber, String featureActionId, String accountId, String transactionAmount, String serviceCharges, JSONObject additionalMetaInfo, String comments) throws ApplicationException {
        // parse the map into a JSONArray
        JSONArray contractCifTalliedMatrixIdsJSON, cifsJSON, matrixIdsJSON;
        JSONObject contractJSON, cifJSON, matrixIdJSON;
        contractCifTalliedMatrixIdsJSON = new JSONArray();
        for (Map.Entry<String, Map<String, Set<String>>> contractIdsEntry : contractCifTalliedMatrixIds.entrySet()) {
            contractJSON = new JSONObject();
            contractJSON.put("contractId", contractIdsEntry.getKey());
            cifsJSON = new JSONArray();
            for (Map.Entry<String, Set<String>> cifIdsEntry : contractIdsEntry.getValue().entrySet()) {
                cifJSON = new JSONObject();
                cifJSON.put("id", cifIdsEntry.getKey());
                matrixIdsJSON = new JSONArray();
                for (String matrixId : cifIdsEntry.getValue()) {
                    matrixIdJSON = new JSONObject();
                    matrixIdJSON.put("id", matrixId);
                    matrixIdsJSON.put(matrixIdJSON);
                }
                cifJSON.put("matrixIds", matrixIdsJSON);
                cifsJSON.put(cifJSON);
            }
            contractJSON.put("cifs", cifsJSON);
            contractCifTalliedMatrixIdsJSON.put(contractJSON);
        }
        JSONArray createBBRequestResponse = approvalRequestBackendDelegate.createNewRequestForTalliedMatrixIdsInDBXDB(customerId, contractCifTalliedMatrixIdsJSON, confirmationNumber, featureActionId, accountId, transactionAmount, serviceCharges, additionalMetaInfo, comments);
        if (createBBRequestResponse == null) {
            throw new ApplicationException(ErrorCodeEnum.ERR_87404);
        }

        return ApprovalUtilities.fetchCompositeRequestListFromJSON(createBBRequestResponse);
    }

    @Override
    public List<RequestDTO> fetchRequestsWithApprovalMatrixInfo(Set<String> requestIds, boolean isAssociationId, Map<String, Map<String, Map<String, String>>> contractCifMap, boolean isActiveRulesFetch) throws ApplicationException {
        JSONArray contractCifMapJSON = null;
        if (contractCifMap != null) {
            contractCifMapJSON = ApprovalUtilities.fetchJSONFromContractCifMap(contractCifMap);
        }
        JSONArray requestsArray = approvalRequestBackendDelegate.fetchRequestsWithApprovalMatrixInfoFromDBXBD(requestIds, isAssociationId, contractCifMapJSON, isActiveRulesFetch);
        if (requestsArray == null) {

        }
        return ApprovalUtilities.fetchCompositeRequestListFromJSON(requestsArray);
    }

    @Override
    public Map<String, String> fetchApproveFeatureActionForFeatureActionIds(Set<String> featureActionIds) {
        JSONArray featureActionsJSON = approvalRequestBackendDelegate.fetchApproveFeatureActionForFeatureActionIdsFromDBXDB(featureActionIds);
        Map<String, String> actionsMap = new HashMap<>();
        for (Object obj : featureActionsJSON) {
            JSONObject featureActionObj = (JSONObject) obj;
            String featureActionId = featureActionObj.optString("id", null);
            String approveFeatureActionId = featureActionObj.optString("approveFeatureAction", null);
            if (featureActionId != null && approveFeatureActionId != null) {
                if (!actionsMap.containsKey(featureActionId)) {
                    actionsMap.put(featureActionId, approveFeatureActionId);
                }
            }
        }
        return actionsMap;
    }

    @Override
    public List<RequestDTO> approveCompositeRequests(String customerId, Set<String> permittedActionIdsSet, List<RequestDTO> requests, String comments) throws ApplicationException {
        // Maintain a set of requests and the actions performed on them
        List<RequestDTO> actedRequests = new ArrayList<>();

        Map<String, String> approveActionIdsMap = this.fetchApproveFeatureActionForFeatureActionIds(permittedActionIdsSet);

        List<CustomerSignatoryGroupDTO> customerSignatoryGroups = signatoryGroupBusinessDelegate.fetchCustomerSignatoryGroups(customerId);
        // <groupId, groupName>
        Map<String, String> customerSignatoryNamesMap = new HashMap<>();
        for (CustomerSignatoryGroupDTO cusSigGrpDTO : customerSignatoryGroups) {
            customerSignatoryNamesMap.put(cusSigGrpDTO.getSignatoryGroupId(), cusSigGrpDTO.getSignatoryGroupName());
        }
        Set<String> customerSignatoryGroupIds = customerSignatoryNamesMap.keySet();

        Set<String> requestIds = new HashSet<>(requests.stream().map((a) -> (a.getRequestId())).collect(Collectors.toList()));
        Set<String> actedRequestIds = fetchActedRequestIdsInHistoryQueueForCustomer(requestIds, customerId);

        boolean isSelfApprovalEnabled = applicationBusinessDelegate.getIsSelfApprovalEnabledFromCache();

        for (RequestDTO request : requests) {
            String requestId = request.getRequestId();
            String requestStatus = request.getStatus();
            switch (requestStatus.toUpperCase()) {
                case "APPROVED":
                case "EXECUTED":
                case "WITHDRAWN":
                case "FAILED":
                case "REJECTED":
                    continue;
                default:
                    break;
            }
            // Check if user has already acted on the request
            if (actedRequestIds.contains(requestId)) {
                // skip request approval process, as user has already acted on the request
                continue;
            }

            String featureActionId = request.getFeatureActionId();
            String approveActionId = approveActionIdsMap.containsKey(featureActionId) ? approveActionIdsMap.get(featureActionId) : null;
            if (approveActionId == null) {
                // no approve action id associated for the given featureActionId
                continue;
            }
            if (!permittedActionIdsSet.contains(approveActionId)) {
                // customer does not have 'approve' permission for this featureAction
                continue;
            }
            if (customerId.equalsIgnoreCase(request.getCreatedBy())) {
                if (!isSelfApprovalEnabled) {
                    // self-approval is turned off. Hence, cannot proceed with request approval
                    continue;
                } else {
                    request.setComments(Constants.DEFAULT_COMMENT_SELFAPPROVED);
                }
            } else {
                request.setComments(comments);
            }

            Integer isGroupMatrix = request.getIsGroupMatrix();
            Map<String, ApprovalRuleDTO> matrixRules = request.getMatrixInfo();
            boolean hasCustomerActedOnRequest = false;
            if (isGroupMatrix == 1) {
                for (Map.Entry<String, ApprovalRuleDTO> ruleEntry : matrixRules.entrySet()) {
                    ApprovalRuleDTO rule = ruleEntry.getValue();
                    if (rule.getIsGroupRuleApproved() == 1) {
                        // this particular rule has already been satisfied, skip over to the next rule
                        continue;
                    }

                    String groupRuleValueStr = rule.getGroupRuleValue();
                    List<String> pendingGroupList = _getListFromString(rule.getPendingGroupList());
                    List<String> actualGroupList = _getListFromString(rule.getGroupList());

                    if (pendingGroupList == null || pendingGroupList.isEmpty()) {
                        alert.prepareError("Not a valid Pending Group List").log();
                    }
                    if (actualGroupList == null || actualGroupList.isEmpty()) {
                        alert.prepareError("Not a valid Group List").log();
                    }

                    int[][] groupRuleValueMatrix = _get2DMatrixFromString(groupRuleValueStr);
                    if (groupRuleValueMatrix == null || groupRuleValueMatrix.length == 0) {
                        alert.prepareError("Not a valid Group Rule Value").log();
                        // TODO: throw error
                    }

                    List<Integer> groupColumnIndices = new ArrayList<Integer>();
                    for (String id : customerSignatoryGroupIds) {
                        if (!pendingGroupList.contains(id)) {
                            continue;
                        }
                        int index = actualGroupList.indexOf(id);
                        if (index > -1) {
                            if (request.getActingSignatoryGroups() == null) {
                                request.setActingSignatoryGroups(new HashSet<>());
                            }
                            request.getActingSignatoryGroups().add(id);
                            groupColumnIndices.add(index);
                        }
                    }
                    if (groupColumnIndices.isEmpty()) {
                        // user is not part of any signatory group associated in the rule. Hence, skip this rule
                        continue;
                    }
                    hasCustomerActedOnRequest = true;

                    // if control reaches this point, that means that the approver is eligible to approve the request. Hence, increment the received approvals counter
                    int[][] updatedGroupRuleValueMatrix = _decrementValues(groupRuleValueMatrix, groupColumnIndices);
                    List<String> updatedPendingGroupList = _getPendingGroupList(actualGroupList, updatedGroupRuleValueMatrix);
                    String updatedGroupRuleValueString = Arrays.deepToString(updatedGroupRuleValueMatrix);
                    rule.setPendingGroupList(updatedPendingGroupList.toString().replaceAll("\\s+", ""));
                    rule.setGroupRuleValue(updatedGroupRuleValueString);

                    /**
                     * @note increment user approval count for that particular rule
                     */
                    rule.setReceivedApprovals(rule.getReceivedApprovals() + 1);

                    if (isApprovalRuleSatisfied(updatedGroupRuleValueMatrix)) {
                        // this particular rule has been satisfied. increase the received approvals counts in requestapprovalmatrix
                        rule.setIsApproved(true);
                    } else {
                        rule.setIsApproved(false);
                    }
                }
            } else {
                // user-based rule validation
                for (Map.Entry<String, ApprovalRuleDTO> ruleEntry : matrixRules.entrySet()) {
                    ApprovalRuleDTO rule = ruleEntry.getValue();

                    Set<String> approverIds = rule.getApprovers();

                    if (approverIds == null || approverIds.isEmpty()) {
                        // TODO: throw error - approvers list cannot be empty
                    }
                    if (!approverIds.contains(customerId)) {
                        //skip this rule in the approval process
                        continue;
                    }

                    String approvalruleId = rule.getApprovalruleId();
                    if (approvalruleId == null) {
                        // TODO: THROW error: for user-based matrix, approvalruleId cannot be null
                    }
                    Integer requiredApprovals = ApprovalUtilities.fetchRequiredApprovalsCountForApprovalRule(approvalruleId);
                    if (requiredApprovals == null) {
                        // TODO: throw error: invalid approval rule id found!
                    }
                    if (requiredApprovals == -1) {
                        requiredApprovals = approverIds.size();
                    }
                    if (requiredApprovals == rule.getReceivedApprovals()) {
                        // this request rule has already received the number of required approvals. Hence, skip this rule validation
                        continue;
                    }
                    hasCustomerActedOnRequest = true;

                    /**
                     * @note increment user approval count for that particular rule
                     */
                    rule.setReceivedApprovals(rule.getReceivedApprovals() + 1);

                    if (rule.getReceivedApprovals() == requiredApprovals) {
                        rule.setIsApproved(true);
                    } else {
                        rule.setIsApproved(false);
                    }
                }
            }
            if (hasCustomerActedOnRequest) {
                actedRequests.add(request);
            }
        }
        if (actedRequests.isEmpty()) {
            // logged-in user is not authorized to act on any of the given requests
            throw new ApplicationException(ErrorCodeEnum.ERR_12001);
        }
        JSONArray requestsArr = ApprovalUtilities.fetchJSONFromCompositeRequestList(actedRequests);
        JSONArray approvalResponse = approvalRequestBackendDelegate.approveRequestsInDBXDB(customerId, requestsArr);
        // invoke backend delegate to approve the requests
        if (approvalResponse == null) {
            throw new ApplicationException(ErrorCodeEnum.ERR_87405);
        }

        return ApprovalUtilities.fetchCompositeRequestListFromJSON(approvalResponse);
    }

    @Override
    public List<RequestDTO> rejectCompositeRequests(String customerId, Set<String> permittedActionIdsSet, List<RequestDTO> requests, String comments) throws ApplicationException {
        // Maintain a set of requests and the actions performed on them
        List<RequestDTO> actedRequests = new ArrayList<>();

        Map<String, String> approveActionIdsMap = this.fetchApproveFeatureActionForFeatureActionIds(permittedActionIdsSet);

        List<CustomerSignatoryGroupDTO> customerSignatoryGroups = signatoryGroupBusinessDelegate.fetchCustomerSignatoryGroups(customerId);
        // <groupId, groupName>
        Map<String, String> customerSignatoryNamesMap = new HashMap<>();
        for (CustomerSignatoryGroupDTO cusSigGrpDTO : customerSignatoryGroups) {
            customerSignatoryNamesMap.put(cusSigGrpDTO.getSignatoryGroupId(), cusSigGrpDTO.getSignatoryGroupName());
        }
        Set<String> customerSignatoryGroupIds = customerSignatoryNamesMap.keySet();

        Set<String> requestIds = new HashSet<>(requests.stream().map((a) -> (a.getRequestId())).collect(Collectors.toList()));
        Set<String> actedRequestIds = fetchActedRequestIdsInHistoryQueueForCustomer(requestIds, customerId);

        for (RequestDTO request : requests) {
            String requestId = request.getRequestId();
            String requestStatus = request.getStatus();
            switch (requestStatus.toUpperCase()) {
                case "APPROVED":
                case "EXECUTED":
                case "WITHDRAWN":
                case "FAILED":
                case "REJECTED":
                    continue;
                default:
                    break;
            }
            // Check if user has already acted on the request
            if (actedRequestIds.contains(requestId)) {
                // TODO: skip request approval process, as user has already acted on the request
                continue;
            }

            String featureActionId = request.getFeatureActionId();
            String approveActionId = approveActionIdsMap.containsKey(featureActionId) ? approveActionIdsMap.get(featureActionId) : null;
            if (approveActionId == null) {
                // TODO: error - no approve action id found for the given featureActionId
                continue;
            }
            if (permittedActionIdsSet.contains(approveActionId)) {
                // TODO: violation - customer does not have approve permission for this featureAction
            }
            else if(permittedActionIdsSet.contains("ALLOW")) {
            	
            }else {
            	continue;
            }

            request.setComments(comments);

            Integer isGroupMatrix = request.getIsGroupMatrix();
            Map<String, ApprovalRuleDTO> matrixRules = request.getMatrixInfo();

            boolean hasCustomerActedOnRequest = false;
            if (isGroupMatrix == 1) {
                // group-based approval rule validation
                for (Map.Entry<String, ApprovalRuleDTO> ruleEntry : matrixRules.entrySet()) {
                    ApprovalRuleDTO rule = ruleEntry.getValue();

                    List<String> pendingGroupList = _getListFromString(rule.getPendingGroupList());
                    List<String> actualGroupList = _getListFromString(rule.getGroupList());

                    if (pendingGroupList == null || pendingGroupList.isEmpty()) {
                        alert.prepareError("Not a valid Pending Group List").log();
                    }
                    if (actualGroupList == null || actualGroupList.isEmpty()) {
                        alert.prepareError("Not a valid Group List").log();
                    }

                    List<Integer> groupColumnIndices = new ArrayList<Integer>();
                    for (String id : customerSignatoryGroupIds) {
                        if (!pendingGroupList.contains(id)) {
                            continue;
                        }
                        int index = actualGroupList.indexOf(id);
                        if (index > -1) {
                            if (request.getActingSignatoryGroups() == null) {
                                request.setActingSignatoryGroups(new HashSet<>());
                            }
                            request.getActingSignatoryGroups().add(id);
                            groupColumnIndices.add(index);
                        }
                    }
                    if (groupColumnIndices.isEmpty()) {
                        // TODO: user is not part of any signatory group associated in the rule. Hence, skip this rule
                        continue;
                    }
                    hasCustomerActedOnRequest = true;
                    rule.setIsApproved(false);
                }
            } else {
                // user-based rule validation
                for (Map.Entry<String, ApprovalRuleDTO> ruleEntry : matrixRules.entrySet()) {
                    ApprovalRuleDTO rule = ruleEntry.getValue();
                    Set<String> approverIds = rule.getApprovers();

                    if (approverIds == null || approverIds.isEmpty()) {
                        // TODO: throw error - approvers list cannot be empty
                    }
                    if (!approverIds.contains(customerId)) {
                        // TODO: skip this rule in the approval process
                        continue;
                    }

                    String approvalruleId = rule.getApprovalruleId();
                    if (approvalruleId == null) {
                        // TODO: THROW error: for user-based matrix, approvalruleId cannot be null
                    }

                    hasCustomerActedOnRequest = true;
                    rule.setIsApproved(false);
                }
            }
            if (hasCustomerActedOnRequest) {
                actedRequests.add(request);
            }
        }
        if (actedRequests.isEmpty()) {
            throw new ApplicationException(ErrorCodeEnum.ERR_12001);
        }
        JSONArray requestsArr = ApprovalUtilities.fetchJSONFromCompositeRequestList(actedRequests);
        JSONArray rejectionResponse = approvalRequestBackendDelegate.rejectRequestsInDBXDB(customerId, requestsArr);
        // invoke backend delegate to approve the requests
        if (rejectionResponse == null) {
            throw new ApplicationException(ErrorCodeEnum.ERR_87406);
        }

        return ApprovalUtilities.fetchCompositeRequestListFromJSON(rejectionResponse);
    }

    @Override
    public List<RequestDTO> withdrawCompositeRequests(String customerId, Set<String> permittedActionIdsSet, List<RequestDTO> requests, String comments) throws ApplicationException {
        // Maintain a set of requests and the actions performed on them
        List<RequestDTO> actedRequests = new ArrayList<>();

        Set<String> requestIds = new HashSet<>(requests.stream().map((a) -> (a.getRequestId())).collect(Collectors.toList()));
        Set<String> actedRequestIds = fetchActedRequestIdsInHistoryQueueForCustomer(requestIds, customerId);

        for (RequestDTO request : requests) {
            String requestId = request.getRequestId();
            String requestStatus = request.getStatus();
            switch (requestStatus.toUpperCase()) {
                case "APPROVED":
                case "EXECUTED":
                case "WITHDRAWN":
                case "FAILED":
                case "REJECTED":
                    continue;
                default:
                    break;
            }
            if (!customerId.equals(request.getCreatedBy())) {
                // the customer is not the creator, hence continue on with next request check
                continue;
            }
            // Check if user has already acted on the request
			/*
			 * if (actedRequestIds.contains(requestId)) { // skip request approval process,
			 * as user has already acted on the request continue; }
			 */

            String featureActionId = request.getFeatureActionId();
            if (permittedActionIdsSet.contains(featureActionId)) {
                // customer has 'create' permission for this featureAction (which is necessary for withdraw action)
            }
            else if(permittedActionIdsSet.contains("ALLOW")){
            	
            }else {
            	continue;
            }
            request.setComments(comments);
            actedRequests.add(request);
        }
        if (actedRequests.isEmpty()) {
            throw new ApplicationException(ErrorCodeEnum.ERR_12001);
        }
        JSONArray requestsArr = ApprovalUtilities.fetchJSONFromCompositeRequestList(actedRequests);
        JSONArray withdrawalResponse = approvalRequestBackendDelegate.withdrawRequestsInDBXDB(customerId, requestsArr);
        // invoke backend delegate to approve the requests
        if (withdrawalResponse == null) {
            throw new ApplicationException(ErrorCodeEnum.ERR_87407);
        }

        return ApprovalUtilities.fetchCompositeRequestListFromJSON(withdrawalResponse);
    }

    @Override
    public Set<String> fetchActedRequestIdsInHistoryQueueForCustomer(Set<String> requestIds, String customerId) {
        JSONArray actedRequests = approvalRequestBackendDelegate.fetchActedRequestIdsInHistoryQueueFromDBXDB(requestIds, customerId);
        Set<String> actedRequestIds = new HashSet<>();
        for (Object obj : actedRequests) {
            JSONObject reqObj = (JSONObject) obj;
            actedRequestIds.add(reqObj.optString("requestId", null));
        }
        return actedRequestIds;
    }

    @Override
    public Map<String, Map<String, Map<String, String>>> fetchContractCifMapForCustomer(String customerId) throws ApplicationException {
        JSONArray contractCifData = approvalRequestBackendDelegate.fetchContractCifMapForCustomerFromDBXDB(customerId);
        Map<String, Map<String, Map<String, String>>> contractCifMap = new HashMap<>();
        for (Object obj : contractCifData) {
            JSONObject contractData = (JSONObject) obj;
            String contractId = contractData.optString("contractId", null);
            String cifId = contractData.optString("coreCustomerId", null);
            if (!contractCifMap.containsKey(contractId)) {
                contractCifMap.put(contractId, new HashMap<>());
            }
            if (!contractCifMap.get(contractId).containsKey(cifId)) {
                contractCifMap.get(contractId).put(cifId, new HashMap<>());
            }
            if (!contractCifMap.get(contractId).get(cifId).containsKey("companyId")) {
                contractCifMap.get(contractId).get(cifId).put("companyId", contractId + "_" + cifId);
            }
        }
        return contractCifMap;
    }

    @Override
    public List<PendingRequestApproversDTO> fetchPendingApproversInfoForRequest(String requestId, boolean isAssociationId) throws ApplicationException {
        List<PendingRequestApproversDTO> pendingRequestApproversDTOList = new ArrayList<>();
        Map<String, PendingRequestApproversDTO> requestsTracker = new HashMap<>();

        JSONArray pendingRequestApproversJSON = approvalRequestBackendDelegate.fetchPendingApproversInfoForRequestFromDBXDB(requestId, isAssociationId);

        for (Object obj : pendingRequestApproversJSON) {
            JSONObject pendingRequestJSON = (JSONObject) obj;
            String request_id = pendingRequestJSON.optString("requestId", null);
            String assocRequestId = pendingRequestJSON.optString("assocRequestId", null);
            String featureActionId = pendingRequestJSON.optString("featureActionId", null);
            String status = pendingRequestJSON.optString("status", null);
            Integer isGroupMatrix = pendingRequestJSON.optInt("isGroupMatrix", 0);
            String approvalRuleId = pendingRequestJSON.optString("approvalruleId", null);
            String approvalRuleName = pendingRequestJSON.optString("approvalruleName", null);
            String approvalMatrixId = pendingRequestJSON.optString("approvalMatrixId", null);
            String limitTypeId = pendingRequestJSON.optString("limitTypeId", null);
            String groupList = pendingRequestJSON.optString("groupList", null);
            String groupRule = pendingRequestJSON.optString("groupRule", null);
            String pendingGroupList = pendingRequestJSON.optString("pendingGroupList", null);
            String groupRuleValue = pendingRequestJSON.optString("groupRuleValue", null);
            Integer isGroupRuleApproved = pendingRequestJSON.optInt("isGroupRuleApproved", -1);
            Integer receivedApprovals = pendingRequestJSON.optInt("receivedApprovals", -1);
            String signatoryGroupId = pendingRequestJSON.optString("signatoryGroupId", null);
            String signatoryGroupName = pendingRequestJSON.optString("signatoryGroupName", null);
            String groupApproversListJSONStr = pendingRequestJSON.optString("groupApproversList", null);
            String approversListJSONStr = pendingRequestJSON.optString("approversList", null);

            if (!status.equalsIgnoreCase("PENDING")) {
                continue;
            }
            if (!requestsTracker.containsKey(request_id)) {
                requestsTracker.put(request_id, new PendingRequestApproversDTO(request_id, assocRequestId, featureActionId, status, isGroupMatrix));
            }

            PendingRequestApproversDTO requestApproversDTO = requestsTracker.get(request_id);
            if (isGroupMatrix == 1) {
                Map<String, SignatoryGroupRuleDTO> signatoryGroupRuleDTOMap = requestApproversDTO.getPendingGroupRules();
                if (signatoryGroupRuleDTOMap == null) {
                    signatoryGroupRuleDTOMap = new HashMap<>();
                }
                if (!signatoryGroupRuleDTOMap.containsKey(approvalMatrixId)) {
                    signatoryGroupRuleDTOMap.put(approvalMatrixId, new SignatoryGroupRuleDTO(approvalRuleId, approvalRuleName, limitTypeId, groupList, groupRule, pendingGroupList, groupRuleValue, isGroupRuleApproved));
                }

                SignatoryGroupRuleDTO signatoryGroupRuleDTO = signatoryGroupRuleDTOMap.get(approvalMatrixId);

                List<SignatoryGroupDTO> signatoryGroupDTOList = signatoryGroupRuleDTO.getGroupDTOList();
                SignatoryGroupDTO signatoryGroupDTO = new SignatoryGroupDTO(signatoryGroupId, signatoryGroupName, null);
                try {
                    JSONArray signatoryApproversJSON = new JSONArray(groupApproversListJSONStr);
                    List<ApproverDTO> signatoryUsers = new ArrayList<>();
                    for (Object obj1 : signatoryApproversJSON) {
                        JSONObject userJSON = (JSONObject) obj1;
                        ApproverDTO customer = new ApproverDTO();
                        customer.setUserName(userJSON.optString("userName", null));
                        customer.setCustomerId(userJSON.optString("customerId", null));
                        customer.setRoleName(userJSON.optString("role", null));
                        customer.setFirstName(userJSON.optString("firstName", null));
                        customer.setLastName(userJSON.optString("lastName", null));
                        signatoryUsers.add(customer);
                    }
                    signatoryGroupDTO.setSignatoryApprovers(signatoryUsers);
                } catch (JSONException je) {

                }
                if (signatoryGroupDTOList == null || signatoryGroupDTOList.isEmpty()) {
                    signatoryGroupDTOList = new ArrayList<>(Arrays.asList(signatoryGroupDTO));
                } else {
                    signatoryGroupDTOList.add(signatoryGroupDTO);
                }

                signatoryGroupRuleDTO.setGroupDTOList(signatoryGroupDTOList);
                signatoryGroupRuleDTOMap.put(approvalMatrixId, signatoryGroupRuleDTO);
                requestApproversDTO.setPendingGroupRules(signatoryGroupRuleDTOMap);
            } else {

                List<ApproverDTO> approvers = new ArrayList<>();
                try {
                    JSONArray approversJSON = new JSONArray(approversListJSONStr);

                    Integer requiredApprovals = ApprovalUtilities.fetchRequiredApprovalsCountForApprovalRule(approvalRuleId);
                    if (requiredApprovals == -1) {
                        // the number of approvals received + the number of pending approvers
                        requiredApprovals = receivedApprovals + approversJSON.length();
                    }
                    if (requiredApprovals == receivedApprovals) {
                        continue;
                    }
                    for (Object obj1 : approversJSON) {
                        JSONObject userJSON = (JSONObject) obj1;
                        ApproverDTO customer = new ApproverDTO();
                        customer.setUserName(userJSON.optString("userName", null));
                        customer.setCustomerId(userJSON.optString("customerId", null));
                        customer.setRoleName(userJSON.optString("role", null));
                        customer.setFirstName(userJSON.optString("firstName", null));
                        customer.setLastName(userJSON.optString("lastName", null));
                        approvers.add(customer);
                    }
                } catch (JSONException je) {

                }
                Map<String, UserRuleDTO> userRuleDTOMap = requestApproversDTO.getPendingUserRules();

                if (userRuleDTOMap == null) {
                    userRuleDTOMap = new HashMap<>();
                }
                if (!userRuleDTOMap.containsKey(approvalMatrixId)) {
                    userRuleDTOMap.put(approvalMatrixId, new UserRuleDTO(approvalRuleId, approvalRuleName, limitTypeId, receivedApprovals));
                }

                UserRuleDTO userRuleDTO = userRuleDTOMap.get(approvalMatrixId);
                userRuleDTO.setApprovers(approvers);

                userRuleDTOMap.put(approvalMatrixId, userRuleDTO);
                requestApproversDTO.setPendingUserRules(userRuleDTOMap);
            }
        }

        for (Map.Entry<String, PendingRequestApproversDTO> trackedRequestsEntry : requestsTracker.entrySet()) {
            pendingRequestApproversDTOList.add(trackedRequestsEntry.getValue());
        }

        return pendingRequestApproversDTOList;
    }

    @Override
    public List<RequestHistoryDTO> fetchRequestHistoryInfo(String requestId, boolean isAssociationId) throws ApplicationException {
        List<RequestHistoryDTO> requestHistoryDTOList = new ArrayList<>();

        JSONArray requestHistoryJSON = approvalRequestBackendDelegate.fetchRequestHistoryInfoFromDBXDB(requestId, isAssociationId);

        for (Object obj : requestHistoryJSON) {
            JSONObject historyData = (JSONObject) obj;
            String approvalId = historyData.optString("approvalId", null);
            String request_id = historyData.optString("requestId", null);
            String assocRequestId = historyData.optString("assocRequestId", null);
            String transactionId = historyData.optString("transactionId", null);
            String featureActionId = historyData.optString("featureActionId", null);
            String companyId = historyData.optString("companyId", null);
            String requestActedBy = historyData.optString("requestActedBy", null);
            String requestActionts = historyData.optString("requestActionts", null);
            String requestStatus = historyData.optString("requestStatus", null);
            String action = historyData.optString("action", null);
            String comments = historyData.optString("comments", null);
            String groupName = historyData.optString("groupName", null);
            String userName = historyData.optString("userName", null);
            String firstName = historyData.optString("firstName", null);
            String lastName = historyData.optString("lastName", null);
            String fullName = historyData.optString("fullName", null);
            String roleId = historyData.optString("roleId", null);
            String roleName = historyData.optString("roleName", null);

            Customer customer = new Customer();
            customer.setFirstName(firstName);
            customer.setLastName(lastName);
            customer.setUserName(userName);
            customer.setRole(roleName);

            RequestHistoryDTO requestHistoryDTO = new RequestHistoryDTO(approvalId, request_id, assocRequestId, transactionId, featureActionId, requestStatus, action, comments, companyId, requestActedBy, groupName, requestActionts, customer);
            requestHistoryDTOList.add(requestHistoryDTO);
        }

        return requestHistoryDTOList;
    }

    @Override
    public List<ApprovalRequestDTO> fetchAllPendingRequests(String customerId, Set<String> permittedFeatureActionIds) {
        diagnostic.prepareDebug("Inside fetchAllPendingRequests : fetchAllPendingRequestsFromDBXDB : _fetch_all_pendingrequests_proc : " + "_featureActionIds : " + String.join(",", permittedFeatureActionIds)).log();
        diagnostic.prepareDebug("Inside fetchAllPendingRequests : fetchAllPendingRequestsFromDBXDB : _fetch_all_pendingrequests_proc : " + "_customerId : " + customerId).log();

        JSONArray pendingRequestsJSON = approvalRequestBackendDelegate.fetchAllPendingRequestsFromDBXDB(customerId, permittedFeatureActionIds);
        try {
			List<ApprovalRequestDTO> pendingRequests = JSONUtils.parseAsList(pendingRequestsJSON.toString(), ApprovalRequestDTO.class);
			return pendingRequests;
		} catch (IOException e) {
			alert.prepareError("Error while converting JSON Array to DTO").log();
			return null;
		}
    }

    @Override
    public List<ApprovalRequestDTO> fetchAllRequestHistory(String customerId, Set<String> permittedFeatureActionIds) {
    	diagnostic.prepareDebug("Inside fetchAllRequestHistory : fetchAllRequestHistoryFromDBXDB : _fetch_all_requesthistory_proc : " + "_featureActionIds : " + String.join(",", permittedFeatureActionIds)).log();
        diagnostic.prepareDebug("Inside fetchAllRequestHistory : fetchAllRequestHistoryFromDBXDB : _fetch_all_requesthistory_proc : " + "_customerId : " + customerId).log();

    	JSONArray requestHistoryJSON = approvalRequestBackendDelegate.fetchAllRequestHistoryFromDBXDB(customerId, permittedFeatureActionIds);
        try {
			List<ApprovalRequestDTO> approvalRequests = JSONUtils.parseAsList(requestHistoryJSON.toString(), ApprovalRequestDTO.class);
			return approvalRequests;
		} catch (IOException e) {
			alert.prepareError("Error while converting JSON Array to DTO").log();
			return null;
		}
        
    }

    @Override
    public List<ApprovalRequestDTO> fetchAllPendingApprovals(String customerId, Set<String> permittedFeatureActionIds) {
        diagnostic.prepareDebug("Inside fetchAllPendingApprovals : fetchAllPendingApprovalsFromDBXDB : _fetch_all_pendingapprovals_proc : " + "_featureActionIds : " + String.join(",", permittedFeatureActionIds)).log();
        diagnostic.prepareDebug("Inside fetchAllPendingApprovals : fetchAllPendingApprovalsFromDBXDB : _fetch_all_pendingapprovals_proc : " + "_customerId : " + customerId).log();

    	JSONArray pendingApprovalsJSON = approvalRequestBackendDelegate.fetchAllPendingApprovalsFromDBXDB(customerId, permittedFeatureActionIds);
        try {
			List<ApprovalRequestDTO> pendingApprovals = JSONUtils.parseAsList(pendingApprovalsJSON.toString(), ApprovalRequestDTO.class);
			return pendingApprovals;
		} catch (IOException e) {
			alert.prepareError("Error while converting JSON Array to DTO").log();
			return null;
		}
    }

    @Override
    public List<ApprovalRequestDTO> fetchAllApprovalHistory(String customerId, Set<String> permittedFeatureActionIds) {
        diagnostic.prepareDebug("Inside fetchAllApprovalHistory : fetchAllApprovalHistoryFromDBXDB : _fetch_all_approvalhistory_proc : " + "_featureActionIds : " + String.join(",", permittedFeatureActionIds)).log();
        diagnostic.prepareDebug("Inside fetchAllApprovalHistory : fetchAllApprovalHistoryFromDBXDB : _fetch_all_approvalhistory_proc : " + "_customerId : " + customerId).log();
    	
        JSONArray approvalHistoryJSON = approvalRequestBackendDelegate.fetchAllApprovalHistoryFromDBXDB(customerId, permittedFeatureActionIds);
        try {
			List<ApprovalRequestDTO> approvalHistory = JSONUtils.parseAsList(approvalHistoryJSON.toString(), ApprovalRequestDTO.class);
			return approvalHistory;
		} catch (IOException e) {
			alert.prepareError("Error while converting JSON Array to DTO").log();
			return null;
		}
    }

    @Override
    public Map<String, Object> fetchAllApprovalRequestsCounts(String customerId, Set<String> permittedFeatureActionIds) {
        //diagnostic.prepareDebug("Inside fetchAllApprovalRequestsCounts "+"customerId: " + customerId).log();
        //diagnostic.prepareDebug("Inside fetchAllApprovalRequestsCounts "+"permittedFeatureActionIds: " + permittedFeatureActionIds.toString()).log();
        diagnostic.prepareDebug("Inside fetchAllApprovalRequestsCountsFromDBXDB _fetch_all_approvalrequests_counts_proc "+"customerId: " + customerId).log();
        diagnostic.prepareDebug("Inside fetchAllApprovalRequestsCountsFromDBXDB _fetch_all_approvalrequests_counts_proc "+"permittedFeatureActionIds: " + String.join(",", permittedFeatureActionIds)).log();

        Map<String, Object> countsMap = new HashMap<>();
        JSONArray approvalRequestsCounts = approvalRequestBackendDelegate.fetchAllApprovalRequestsCountsFromDBXDB(customerId, permittedFeatureActionIds);

        for (Object obj : approvalRequestsCounts) {
            JSONObject countObj = (JSONObject) obj;
            String limitGroupId = countObj.optString("limitGroupId", null);
            String limitGroupName = countObj.optString("limitGroupName", null);
            String featureActionId = countObj.optString("featureActionId", null);
            String featureActionName = countObj.optString("featureActionName", null);
            String featureId = countObj.optString("featureId", null);
            String featureName = countObj.optString("featureName", null);
            String actionType = countObj.optString("typeId", null);
            String requestType = countObj.optString("requestType", null);
            Integer count = countObj.optInt("count", 0);

            if (!countsMap.containsKey(limitGroupId)) {
                countsMap.put(limitGroupId, new HashMap<String, Object>());
            }
            Map<String, Object> limitGroupCountData = (Map<String, Object>) countsMap.get(limitGroupId);
            if (limitGroupCountData.containsKey("limitGroupName")) {
                limitGroupCountData.put("limitGroupName", limitGroupName);
            }
            if (!limitGroupCountData.containsKey(featureActionId)) {
                limitGroupCountData.put(featureActionId, new HashMap<String, Object>());
            }
            Map<String, Object> featureActionCountsData = (Map<String, Object>) limitGroupCountData.get(featureActionId);
            if (!featureActionCountsData.containsKey("featureActionName")) {
                featureActionCountsData.put("featureActionName", featureActionName);
            }
            if (!featureActionCountsData.containsKey("featureId")) {
                featureActionCountsData.put("featureId", featureId);
            }
            if (!featureActionCountsData.containsKey("featureName")) {
                featureActionCountsData.put("featureName", featureName);
            }
            if (!featureActionCountsData.containsKey("actionType")) {
                featureActionCountsData.put("actionType", actionType);
            }
            if (!featureActionCountsData.containsKey("myApprovalsPending")) {
                featureActionCountsData.put("myApprovalsPending", 0);
            }
            if (!featureActionCountsData.containsKey("myApprovalsHistory")) {
                featureActionCountsData.put("myApprovalsHistory", 0);
            }
            if (!featureActionCountsData.containsKey("myRequestsPending")) {
                featureActionCountsData.put("myRequestsPending", 0);
            }
            if (!featureActionCountsData.containsKey("myRequestHistory")) {
                featureActionCountsData.put("myRequestHistory", 0);
            }

            switch (requestType) {
                case Constants.PENDING_REQUEST: featureActionCountsData.put("myRequestsPending", count); break;
                case Constants.PENDING_APPROVAL: featureActionCountsData.put("myApprovalsPending", count); break;
                case Constants.REQUEST_HISTORY: featureActionCountsData.put("myRequestHistory", count); break;
                case Constants.APPROVAL_HISTORY: featureActionCountsData.put("myApprovalsHistory", count); break;
                default: break;
            }

        }
        return countsMap;
    }

    /*
     * Parse String value to List
     */
    private List<String> _getListFromString(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        str = str.replaceAll("\\s+", "");
        str = str.substring(1, str.length() - 1);
        return Arrays.asList(str.split(","));
    }

    /*
     * Parse String value to 2D Matrix
     */
    private int[][] _get2DMatrixFromString(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        str = str.replaceAll("\\s+", "");
        str = str.replace("[", "");
        str = str.substring(0, str.length() - 2);
        String strList[] = str.split("],");

        int rowLength = strList.length;
        int colLength = strList[0].split(",").length;
        int matrix[][] = new int[rowLength][colLength];

        for (int i = 0; i < rowLength; i++) {
            String single_int[] = strList[i].split(",");
            for (int j = 0; j < colLength; j++) {
                matrix[i][j] = Integer.parseInt(single_int[j]);
            }
        }

        return matrix;
    }

    /*
     * Removes Group Id from actualGroupList to get the pending group list
     */
    private List<String> _getPendingGroupList(List<String> actualGroupList, int[][] updatedGroupRuleValueMatrix) {
        int rowLength = updatedGroupRuleValueMatrix.length;
        int columnLength = updatedGroupRuleValueMatrix[0].length;
        List<Integer> col = new ArrayList<Integer>();
        for (int i = 0; i < columnLength; i++) {
            int count = 0;
            for (int j = 0; j < rowLength; j++) {
                if (updatedGroupRuleValueMatrix[j][i] == 0) {
                    count++;
                }
            }
            if (count == rowLength) {
                col.add(i);
            }
        }

        List<String> pendingGroupIds = new ArrayList<String>();
        for (int i = 0; i < actualGroupList.size(); i++) {
            if (!col.contains(i)) {
                pendingGroupIds.add(actualGroupList.get(i));
            }
        }

        return pendingGroupIds;
    }

    /*
     * Decrements matrix element value based on groupColumnIndices
     */
    private int[][] _decrementValues(int[][] groupRuleValueMatrix, List<Integer> groupColumnIndices) {
        for (int i = 0; i < groupRuleValueMatrix.length; i++) {
            for (int j = 0; j < groupColumnIndices.size(); j++) {
                if (groupRuleValueMatrix[i][groupColumnIndices.get(j)] > 0) {
                    groupRuleValueMatrix[i][groupColumnIndices.get(j)] = groupRuleValueMatrix[i][groupColumnIndices.get(j)] - 1;
                }
            }
        }
        return groupRuleValueMatrix;
    }

    private boolean isApprovalRuleSatisfied(int[][] updatedGroupRuleValueMatrix) {
        for (int row = 0; row < updatedGroupRuleValueMatrix.length; row++) {
            int count = 0;
            for (int col = 0; col < updatedGroupRuleValueMatrix[row].length; col++) {
                if (updatedGroupRuleValueMatrix[row][col] == 0) {
                    count++;
                }
            }
            if (count == updatedGroupRuleValueMatrix[row].length) {
                return true;
            }
        }
        return false;
    }
}
