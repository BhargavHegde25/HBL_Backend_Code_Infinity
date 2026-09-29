package com.temenos.dbx.product.approvalsframework.approvalsframeworkcommons.util;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRequestDTO;
import com.temenos.dbx.product.approvalservices.dto.BBRequestDTO;
import com.temenos.dbx.product.approvalsframework.approvalsframeworkcommons.dto.ApprovalRuleDTO;
import com.temenos.dbx.product.approvalsframework.approvalsframeworkcommons.dto.RequestDTO;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.FeatureAction;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.constants.TransactionStatusEnum;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

public class ApprovalUtilities {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    public static Map<String, JSONObject> fetchFeatureActionsEligibleForApproval(String legalEntityId) {
        String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
        String operationName = OperationName.DB_FEATUREACTION_GET;
        Map<String, Object> requestParameters = new HashMap<String, Object>();
        Map<String, JSONObject> featureActionObjectMap = new HashMap<>();

        requestParameters.put(DBPUtilitiesConstants.FILTER, "companyLegalUnit" + DBPUtilitiesConstants.EQUAL + legalEntityId +
                DBPUtilitiesConstants.AND + "approveFeatureAction" + DBPUtilitiesConstants.NOT_EQ + "null");

        try {

            String response = DBPServiceExecutorBuilder.builder()
                    .withServiceId(serviceName)
                    .withObjectId(null)
                    .withOperationId(operationName)
                    .withRequestParameters(requestParameters)
                    .build().getResponse();

            JSONObject resObj = new JSONObject(response);
            if (resObj.has("opstatus") && resObj.getInt("opstatus") == 0) {
                JSONArray resArr = resObj.getJSONArray("featureaction");
                if (resArr.length() > 0) {
                    for (Object obj : resArr) {
                        JSONObject featureActionObj = (JSONObject) obj;
                        featureActionObjectMap.put(featureActionObj.getString("id"), featureActionObj);
                    }
                    return featureActionObjectMap;
                } else {
                    alert.prepareError("FEATUREACTION table entry not found for the COMPANYLEGALUNIT: " + legalEntityId).log();
                    return null;
                }
            } else {
                alert.prepareError("Failed to read FEATUREACTION table.").log();
                return null;
            }
        } catch (JSONException je) {
            alert.prepareError("Exception occured while parsing FEATUREACTION_GET operation JSON response: ", je).log();
            return null;
        } catch (DBPApplicationException dbpE) {
            alert.prepareError("Exception occured while reading FEATUREACTION table: " + dbpE).log();
            return null;
        }
    }

    public static List<ApprovalRuleDTO> fetchRuleLimitsFromJSONArray(JSONArray limitsJSON) throws ApplicationException, JSONException {
        List<ApprovalRuleDTO> limitRules = new ArrayList<>();
        ApprovalRuleDTO limitRule;
        for (Object limitObj : limitsJSON) {
            JSONObject limitJSON = (JSONObject) limitObj;
            String approvalRuleId = limitJSON.optString("approvalruleId", null);
            String lowerLimitStr = limitJSON.optString("lowerlimit", null);
            String upperLimitStr = limitJSON.optString("upperlimit", null);
            Double lowerLimit = null, upperLimit = null;
            try {
                lowerLimit = Double.parseDouble(lowerLimitStr);
            } catch (NumberFormatException nme) {
                throw new ApplicationException(ErrorCodeEnum.ERR_87107);
            }
            try {
                upperLimit = Double.parseDouble(upperLimitStr);
            } catch (NumberFormatException nme) {
                throw new ApplicationException(ErrorCodeEnum.ERR_87108);
            }
            JSONArray approversJSON = limitJSON.has("approvers") ? limitJSON.getJSONArray("approvers") : null;
            Set<String> approvers = null;
            if (approversJSON != null) {
                approvers = new HashSet<>();
                for (Object obj : approversJSON) {
                    JSONObject approverJSON = (JSONObject) obj;
                    String approverId = approverJSON.optString("approverId", null);
                    if (approverId == null) {
                        throw new ApplicationException(ErrorCodeEnum.ERR_87111);
                    }
                    approvers.add(approverId);
                }
            }
            String groupRule = limitJSON.optString("groupRule", null);
            String groupList = limitJSON.optString("groupList", null);

            if (approvalRuleId == null) {
                // either approverIds or (groupList and groupRule) can be null
                if (!(((approvers == null || approvers.isEmpty()) && !(groupList == null && groupRule == null)) || (!(approvers == null || approvers.isEmpty()) && (groupList == null && groupRule == null)))) {
                    // TODO: throw error - invalid approvers list
                    throw new ApplicationException(ErrorCodeEnum.ERR_87115);
                }
            }

            limitRule = new ApprovalRuleDTO(lowerLimit, upperLimit, approvalRuleId, groupList, groupRule, approvers);
            limitRules.add(limitRule);
        }
        return limitRules;
    }

    public static JSONArray fetchJSONArrayFromLimitRule(List<ApprovalRuleDTO> limitRules) {
        JSONArray limitsJSON = new JSONArray();
        for (ApprovalRuleDTO limitRule : limitRules) {
            JSONObject limitJSON = new JSONObject();
            if (limitRule.getApprovalruleId() != null) {
                limitJSON.put("approvalruleId", limitRule.getApprovalruleId());
            }
            limitJSON.put("upperlimit", limitRule.getUpperLimit());
            limitJSON.put("lowerlimit", limitRule.getLowerLimit());
            if (limitRule.getGroupList() != null) {
                limitJSON.put("groupList", limitRule.getGroupList());
            }
            if (limitRule.getGroupRule() != null) {
                limitJSON.put("groupRule", limitRule.getGroupRule());
            }
            // parse approvers into JSONArray
            if (limitRule.getApprovers() != null) {
                JSONArray approversJSON = new JSONArray();
                JSONObject approverJSON;
                for (String approverId : limitRule.getApprovers()) {
                    approverJSON = new JSONObject();
                    approverJSON.put("approverId", approverId);
                    approversJSON.put(approverJSON);
                }
                limitJSON.put("approvers", approversJSON);
            }
            limitsJSON.put(limitJSON);
        }
        return limitsJSON;
    }

    /**
     * @param limitTypeId
     * @param limitRules
     * @param maxLimitValue
     * @return
     * @description performs a rule limit ranges validation - to check if the limit ranges defined are mutually exclusive and exhaustive
     */
    public static List<ApprovalRuleDTO> validateAndSortApprovalRules(String limitTypeId, List<ApprovalRuleDTO> limitRules, Double maxLimitValue) {
        List<ApprovalRuleDTO> sortedLimits = limitRules.stream().sorted(Comparator.comparing(ApprovalRuleDTO::getLowerLimit)).collect(Collectors.toList());
        int limitRanges = sortedLimits.size();
        Double prevUpperLimit = null, upperLimit, lowerLimit;
        if (limitTypeId.compareTo(Constants.NON_MONETARY_LIMIT) == 0) {
            // No need for limit ranges validation for non-monetary limits
            return sortedLimits;
        }
        for (int i = 0; i < limitRanges; i++) {
            lowerLimit = sortedLimits.get(i).getLowerLimit();
            upperLimit = sortedLimits.get(i).getUpperLimit();
            if(lowerLimit == -1.0 && upperLimit > 0) {
            	//Fine as this is the first limit.
            }else if(lowerLimit > 0 && upperLimit == -1.0) {
            	//Fine as this is the last limit
            }
            else if(lowerLimit.compareTo(maxLimitValue) > 0 || upperLimit.compareTo(maxLimitValue) > 0) {
                // upperLimit or lowerLimit should not exceed the maxLimit if and only if it is a monetary limit
                return null;
            }
            if (prevUpperLimit == null) {
                // initialize the previous upper limit
                prevUpperLimit = upperLimit;
            }
            if (i == 0) { // validating the first limit
                if (lowerLimit == 0) {
                    if (upperLimit != -1 && limitRanges == 1) {
                        // 0 ABOVE RANGE(0, -1) valid for only one limit range
                        return null;
                    }
                } else if (lowerLimit > 0) {
                    // for the first rule range, lower limit cannot be more than 0
                    return null;
                } else if (lowerLimit == -1 && upperLimit == -1 && limitRanges != 1) {
                    // NO RULE RANGE(-1, -1) valid for only one limit range
                    return null;
                }
            } else if (i == limitRanges - 1) { // validating the last limit
                if (lowerLimit.compareTo(prevUpperLimit) != 0) {
                    // current lower limit should not be different from the previous upper limit
                    return null;
                }
                if (upperLimit != -1) {
                    // for the last limit range, the upper limit must be -1
                    return null;
                }
            } else {
                if (lowerLimit.compareTo(upperLimit) >= 0) {
                    // for the mid-limit ranges, the lowerLimit cannot be greater than upperLimit
                    return null;
                } else if (lowerLimit.compareTo(prevUpperLimit) != 0) {
                    // current lower limit should not be different from the previous upper limit
                    return null;
                }
            }
            prevUpperLimit = upperLimit;
        }
        return sortedLimits;
    }

    public static Map<String, Boolean> fetchSTPConfigForApprovalMatrix() {
        Map<String, Boolean> stpConfigMap = new HashMap<>();
        String configParamValue = null;
        configParamValue = EnvironmentConfigurationsHandler.getValue(Constants.AM_MAX_LIMIT_NO_RULES_ALLOW_STP);
        if ("false".equalsIgnoreCase(configParamValue)) {
            stpConfigMap.put(Constants.AM_MAX_LIMIT_NO_RULES_ALLOW_STP, false);
        } else {
            stpConfigMap.put(Constants.AM_MAX_LIMIT_NO_RULES_ALLOW_STP, true);
        }
        configParamValue = EnvironmentConfigurationsHandler.getValue(Constants.AM_DAILY_LIMIT_NO_RULES_ALLOW_STP);
        if ("false".equalsIgnoreCase(configParamValue)) {
            stpConfigMap.put(Constants.AM_DAILY_LIMIT_NO_RULES_ALLOW_STP, false);
        } else {
            stpConfigMap.put(Constants.AM_DAILY_LIMIT_NO_RULES_ALLOW_STP, true);
        }
        configParamValue = EnvironmentConfigurationsHandler.getValue(Constants.AM_WEEKLY_LIMIT_NO_RULES_ALLOW_STP);
        if ("false".equalsIgnoreCase(configParamValue)) {
            stpConfigMap.put(Constants.AM_WEEKLY_LIMIT_NO_RULES_ALLOW_STP, false);
        } else {
            stpConfigMap.put(Constants.AM_WEEKLY_LIMIT_NO_RULES_ALLOW_STP, true);
        }
        configParamValue = EnvironmentConfigurationsHandler.getValue(Constants.AM_NON_MONETORY_NO_RULES_ALLOW_STP);
        if ("false".equalsIgnoreCase(configParamValue)) {
            stpConfigMap.put(Constants.AM_NON_MONETORY_NO_RULES_ALLOW_STP, false);
        } else {
            stpConfigMap.put(Constants.AM_NON_MONETORY_NO_RULES_ALLOW_STP, true);
        }
        return stpConfigMap;
    }

    public static JSONArray fetchJSONFromContractCifMap(Map<String, Map<String, Map<String, String>>> contractCifMap) {
        JSONObject contractObj, cifObj;
        JSONArray cifArray, contractCifMapJSON;
        contractCifMapJSON = new JSONArray();
        for (Map.Entry<String, Map<String, Map<String, String>>> contractEntry : contractCifMap.entrySet()) {
            String contractId = contractEntry.getKey();
            contractObj = new JSONObject();
            cifArray = new JSONArray();
            contractObj.put("contractId", contractId);
            for (Map.Entry<String, Map<String, String>> cifEntry : contractEntry.getValue().entrySet()) {
                String cifId = cifEntry.getKey();
                cifObj = new JSONObject();
                cifObj.put("id", cifId);
                String companyId = cifEntry.getValue().containsKey("companyId") ? cifEntry.getValue().get("companyId") : (contractId + "_" + cifId);
                cifObj.put("companyId", companyId);
                cifArray.put(cifObj);
            }
            contractObj.put("cifs", cifArray);
            contractCifMapJSON.put(contractObj);
        }
        return contractCifMapJSON;
    }

    public static Map<String, Map<String, Map<String, String>>> fetchContractCifMapFromJSON(JSONArray contractCifMapJSON) {
        Map<String, Map<String, Map<String, String>>> contractCifMap = new HashMap<>();
        for (Object obj : contractCifMapJSON) {
            JSONObject contractObj = (JSONObject) obj;
            String contractId = contractObj.optString("contractId", null);
            if (contractId != null) {
                contractCifMap.put(contractId, new HashMap<>());
            }
            JSONArray cifsJSON = contractObj.getJSONArray("cifs");
            if (cifsJSON != null) {
                for (Object obj2 : cifsJSON) {
                    JSONObject cifJSON = (JSONObject) obj2;
                    String cifId = cifJSON.optString("id", null);
                    String companyId = cifJSON.optString("companyId", null);
                    if (companyId == null) {
                        companyId = contractId + "_" + cifId;
                    }
                    if (cifId != null) {
                        contractCifMap.get(contractId).put(cifId, new HashMap<>());
                        contractCifMap.get(contractId).get(cifId).put("companyId", companyId);
                    }
                }
            }
        }
        return contractCifMap;
    }

    /**
     * @param requestsJSON
     * @return List<RequestDTO> a list of all the requests created, along with the approval matrix rules associated with each request
     */
    public static List<RequestDTO> fetchCompositeRequestListFromJSON(JSONArray requestsJSON) {
        Map<String, RequestDTO> trackedRequests = new HashMap<>();
        RequestDTO requestPlaceholder;
        List<RequestDTO> requests = new ArrayList<>();
        for (Object obj : requestsJSON) {
            JSONObject requestObj = (JSONObject) obj;
            String requestId = requestObj.optString("requestId", null);
            String assocRequestId = requestObj.optString("assocRequestId", null);
            String transactionId = requestObj.optString("transactionId", null);
            String accountId = requestObj.optString("accountId", null);
            String createdBy = requestObj.optString("createdby", null);
            String createdts = requestObj.optString("createdts", null);
            String requiredSets = requestObj.optString("requiredSets", "-1");
            String receivedSets = requestObj.optString("receivedSets", "-1");
            String isGroupMatrix = requestObj.optString("isGroupMatrix", "0");
            String additionalMetaInfo = requestObj.optString("additionalMeta", "{}");
            String contractId = requestObj.optString("contractId", null);
            String cifId = requestObj.optString("coreCustomerId", null);
            String actionId = requestObj.optString("featureActionId", null);
            String status = requestObj.optString("status", null);
            String approvalMatrixId = requestObj.optString("approvalMatrixId", null);
            String approvalruleId = requestObj.optString("approvalruleId", null);
            if (approvalruleId == null) {
                approvalruleId = "";
            }
            String receivedApprovals = requestObj.optString("receivedApprovals", null);
            String groupList = requestObj.optString("groupList", null);
            String pendingGroupList = requestObj.optString("pendingGroupList", null);
            String groupRuleValue = requestObj.optString("groupRuleValue", null);
            String approverIds = requestObj.optString("approverIds", null);
            Integer isGroupRuleApproved = requestObj.optInt("isGroupRuleApproved", 0);


            if (!trackedRequests.containsKey(requestId)) {
                trackedRequests.put(requestId, new RequestDTO(requestId, assocRequestId, transactionId, actionId, cifId, contractId, accountId, status, createdBy, createdts, Integer.parseInt(isGroupMatrix), Integer.parseInt(requiredSets), Integer.parseInt(receivedSets), additionalMetaInfo));
            }
            requestPlaceholder = trackedRequests.get(requestId);
            Map<String, ApprovalRuleDTO> matrixRules = requestPlaceholder.getMatrixInfo();
            if (matrixRules == null) {
                matrixRules = new HashMap<>();
            }
            if (!matrixRules.containsKey(approvalMatrixId)) {
                matrixRules.put(approvalMatrixId, new ApprovalRuleDTO(approvalruleId, groupList, pendingGroupList, groupRuleValue, approverIds != null ? new HashSet<>(Arrays.asList(approverIds.split(","))) : null, Integer.parseInt(receivedApprovals), isGroupRuleApproved));
            }
            requestPlaceholder.setMatrixInfo(matrixRules);
        }
        for (Map.Entry<String, RequestDTO> trackedRequestEntry : trackedRequests.entrySet()) {
            requests.add(trackedRequestEntry.getValue());
        }
        return requests;
    }

    public static JSONArray fetchJSONFromCompositeRequestList(List<RequestDTO> requests) {
        JSONArray requestsJSON = new JSONArray();
        for (RequestDTO request : requests) {
            JSONObject requestJSON = new JSONObject();
            requestJSON.put(Constants.REQUESTID, request.getRequestId());
            requestJSON.put(Constants.ASSOCREQUESTID, request.getAssocRequestId());
            requestJSON.put(Constants.ISGROUPMATRIX, request.getIsGroupMatrix().toString());
            requestJSON.put("actingGroupsCSV", request.getActingSignatoryGroups() != null ? String.join(",", request.getActingSignatoryGroups()) : "");
            requestJSON.put("comments", request.getComments());
            JSONArray matrixData = new JSONArray();
            for (Map.Entry<String, ApprovalRuleDTO> ruleEntry : request.getMatrixInfo().entrySet()) {
                ApprovalRuleDTO rule = ruleEntry.getValue();
                JSONObject matrixEntryJSON = new JSONObject();
                matrixEntryJSON.put("matrixId", ruleEntry.getKey());
                matrixEntryJSON.put(Constants.PENDINGGROUPLIST, rule.getPendingGroupList());
                matrixEntryJSON.put(Constants.GROUPRULEVALUE, rule.getGroupRuleValue());
                matrixEntryJSON.put("isApproved", rule.getIsApproved() != null ? rule.getIsApproved() ? "1" : "0" : null);
                matrixEntryJSON.put("receivedApprovals", rule.getReceivedApprovals());
                matrixData.put(matrixEntryJSON);
            }
            requestJSON.put("matrixData", matrixData);
            requestsJSON.put(requestJSON);
        }
        return requestsJSON;
    }

    public static Integer fetchRequiredApprovalsCountForApprovalRule(String approvalruleId) {
        switch (approvalruleId) {
            case "ALL":
                return -1;
            case "ANY_ONE":
                return 1;
            case "ANY_TWO":
                return 2;
            case "ANY_THREE":
                return 3;
            default:
                return null;
        }
    }

    public static Map<String, Map<String, Map<String, String>>> fetchContractCifMapForCustomer(String customerId) {
        Map<String, Object> reqParams = new HashMap<>();
        reqParams.put(DBPUtilitiesConstants.FILTER, "customerId" + DBPUtilitiesConstants.EQUAL + customerId);
        try {
            String response = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
                    .withObjectId(null)
                    .withOperationId(OperationName.DB_CONTRACTCUSTOMERS_GET)
                    .withRequestParameters(reqParams)
                    .build().getResponse();
            JSONObject responseObj = new JSONObject(response);
            if (responseObj.has("opstatus") && responseObj.getInt("opstatus") == 0) {
                if (responseObj.has("contractcustomers")) {
                    JSONArray contractCifData = responseObj.getJSONArray("contractcustomers");
                    if (contractCifData.isEmpty()) {
                        // TODO: throw error: customer not associated with any contract or cif
                    }
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
            }
        } catch (JSONException je) {

        } catch (DBPApplicationException dbpae) {

        }
        return null;
    }
    
    public static List<BBRequestDTO> convertApprovalRequestDTOToBBRequestDTO(List<ApprovalRequestDTO> requests){
		
    	List<BBRequestDTO> newRequests = new ArrayList<>();
    	
    	for(ApprovalRequestDTO request: requests) {
    		
    		BBRequestDTO bbRequest = new BBRequestDTO();
    		
    		bbRequest.setRequestId(request.getCompositeRequestIds());
			bbRequest.setAssocRequestId(request.getAssocRequestId());
			bbRequest.setCompanyLegalUnit(request.getCompanyLegalUnit());
			bbRequest.setTransactionId(request.getTransactionId());
			bbRequest.setStatus(request.getAssocStatus());
			bbRequest.setFeatureActionId(request.getFeatureActionId());
			bbRequest.setIsGroupMatrix(request.getIsGroupMatrix());
			bbRequest.setAmICreator(request.getAmICreator());
			bbRequest.setAmIApprover(request.getAmIApprover());
			bbRequest.setActedByMeAlready(request.getActedByMeAlready());
			bbRequest.setReceivedApprovals(request.getReceivedApprovals());
			bbRequest.setRequiredApprovals(request.getRequiredApprovals());
			bbRequest.setAdditionalMeta(request.getAdditionalMeta());
			
			newRequests.add(bbRequest);
    	}
    	
    	return newRequests;
    	
    	
    }

	

    public static String getCustomerId(DataControllerRequest request) {
        String customerId = null;

        try {
            Map<String, Object> customer = CustomerSession.getCustomerMap(request);
            customerId = CustomerSession.getCustomerId(customer);
        }
        catch (Exception e) {
            alert.prepareError("Failed to get customer id from CustomerSession").log();
            diagnostic.prepareDebug("Failed to get customer id from CustomerSession" + e).log();
        }

        try {
            if(customerId == null || customerId.isEmpty()) {
                customerId = request.getServicesManager().getIdentityHandler().getUserId();
            }
        }
        catch (Exception e) {
            alert.prepareError("Failed to get customer id from identity handler").log();
            diagnostic.prepareDebug("Failed to get customer id from identity handler" + e).log();
        }

        if(customerId == null || customerId.isEmpty()) {
            alert.prepareError("Failed to get customer id").log();
        }

        return customerId;
    }

    public static Object getCurrentLoggedInUserPermissions(DataControllerRequest request) {
        Object permissionsObj = null;

        try {
            permissionsObj = LegalEntityUtil.getUserCurrentLegalEntityFeaturePermissions(request);
        }
        catch (Exception e) {
            alert.prepareError("Failed to get customer id from customerSecurityAttributes" , e).log();
            diagnostic.prepareDebug("Failed to get customer id from customerSecurityAttributes" , e).log();
        }

        return permissionsObj;
    }
    
	public static int isApprovalMatrixChangeRequestPendingInQueue(String featureAction, String companyId,
			String transactionId) {
		try {
			String filter = "";
			Map<String, Object> inputParams = new HashMap<>();
			Map<String, Object> headerMap = new HashMap<String, Object>();
			filter = filter + "featureActionId" + DBPUtilitiesConstants.EQUAL + FeatureAction.APPROVAL_MATRIX_MANAGE
					+ DBPUtilitiesConstants.AND + "status" + DBPUtilitiesConstants.EQUAL + TransactionStatusEnum.PENDING
					+ DBPUtilitiesConstants.AND + "companyId" + DBPUtilitiesConstants.EQUAL + companyId
					+ DBPUtilitiesConstants.AND + "transactionId" + DBPUtilitiesConstants.EQUAL + transactionId;
			String select = "requestId";
			inputParams.put(DBPUtilitiesConstants.FILTER, filter);
			inputParams.put(DBPUtilitiesConstants.SELECT, select);

			JsonObject bbrequestJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
					URLConstants.BB_REQUEST_GET);

			if (JSONUtil.isJsonNotNull(bbrequestJson) && JSONUtil.hasKey(bbrequestJson, "bbrequest")
					&& bbrequestJson.get("bbrequest").isJsonArray()) {
				JsonArray bbrequestIdsArray = bbrequestJson.get("bbrequest").getAsJsonArray();
				if (bbrequestIdsArray.size() >= 1) {
					return 1;
				}
			}
			return 0;
		} catch (Exception e) {
			alert.prepareError("Failed at isApprovalMatrixChangeRequestPendingInQueue", e).log();
			diagnostic.prepareDebug("Failed at isApprovalMatrixChangeRequestPendingInQueue", e).log();
			return -1;
		}
	}
}

