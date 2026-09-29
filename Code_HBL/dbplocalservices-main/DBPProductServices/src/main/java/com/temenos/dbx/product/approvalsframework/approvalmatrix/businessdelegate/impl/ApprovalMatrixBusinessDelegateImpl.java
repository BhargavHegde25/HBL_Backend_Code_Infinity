package com.temenos.dbx.product.approvalsframework.approvalmatrix.businessdelegate.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.approvalservices.businessdelegate.api.ApprovalQueueBusinessDelegate;
import com.temenos.dbx.product.approvalsframework.approvalmatrix.backenddelegate.api.ApprovalMatrixBackendDelegate;
import com.temenos.dbx.product.approvalsframework.approvalmatrix.businessdelegate.api.ApprovalMatrixBusinessDelegate;
import com.temenos.dbx.product.approvalsframework.approvalsframeworkcommons.dto.ApprovalRuleDTO;
import com.temenos.dbx.product.approvalsframework.approvalsframeworkcommons.util.ApprovalUtilities;
import com.temenos.dbx.product.commons.businessdelegate.api.ContractBusinessDelegate;
import com.temenos.dbx.product.commons.dto.FeatureActionDTO;
import com.temenos.dbx.product.commons.dto.LimitsDTO;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.FeatureAction;
import com.temenos.dbx.product.constants.TransactionStatusEnum;
import com.temenos.dbx.product.contract.backenddelegate.api.ContractBackendDelegate;
import com.temenos.dbx.product.dto.ContractDTO;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import com.temenos.dbx.product.commons.dto.TransactionStatusDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

import java.util.*;

public class ApprovalMatrixBusinessDelegateImpl implements ApprovalMatrixBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    ContractBusinessDelegate contractBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);
    ApprovalMatrixBackendDelegate approvalMatrixBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(ApprovalMatrixBackendDelegate.class);

    @Override
    public Map<String, Map<String, Boolean>> fetchCompositeApprovalModeForContractCifMap(Map<String, Map<String, Map<String, String>>> contractCifMap) throws ApplicationException {
        Map<String, Map<String, Boolean>> compositeApprovalModeMap = new HashMap<>();

        JSONArray approvalModeResponse = approvalMatrixBackendDelegate.fetchCompositeApprovalModeFromDBXDB(ApprovalUtilities.fetchJSONFromContractCifMap(contractCifMap));

        if (approvalModeResponse == null) {
            throw new ApplicationException(ErrorCodeEnum.ERR_87214);
        }
        for (Object obj : approvalModeResponse) {
            JSONObject approvalModeObj = (JSONObject) obj;
            String contractId = approvalModeObj.optString("contractId", null);
            String coreCustomerId = approvalModeObj.optString("coreCustomerId", null);
            Boolean isGroupLevel = approvalModeObj.optBoolean("isGroupLevel", false);
            if (contractId != null && coreCustomerId != null) {
                compositeApprovalModeMap.put(contractId, new HashMap<>());
                compositeApprovalModeMap.get(contractId).put(coreCustomerId, isGroupLevel);
            }
        }
        return compositeApprovalModeMap;
    }

    @Override
    public Boolean fetchApprovalModeForContractAndCif(String coreCustomerId, String contractId) throws ApplicationException {
        JSONArray approvalModeRes = approvalMatrixBackendDelegate.fetchApprovalModeFromDBXDB(coreCustomerId, contractId);
        JSONObject approvalModeJSON = (JSONObject) approvalModeRes.get(0);
        return approvalModeJSON.optBoolean("isGroupMatrix", false);
    }

    @Override
    public Map<String, Map<String, Boolean>> fetchCompositeApprovalMatrixStatusForContractCifMap(Map<String, Map<String, Map<String, String>>> contractCifMap,Set<FeatureActionDTO> featureActionDTOs) throws ApplicationException {
        Map<String, Map<String, Boolean>> compositeApprovalMatrixStatusMap = new HashMap<>();
        Set<String> featureActionIds = new HashSet<>();
        for (Map.Entry<String, Map<String, Map<String, String>>> contractEntry : contractCifMap.entrySet()) {
            for (Map.Entry<String, Map<String, String>> cifEntry : contractEntry.getValue().entrySet()) {
                for (FeatureActionDTO featureActionDTO : featureActionDTOs) {
                    String featureActionId = featureActionDTO.getFeatureActionId();
                    featureActionIds.add(featureActionId);
                }
            }
        }
        JSONArray approvalMatrixStatusResponse = approvalMatrixBackendDelegate.fetchCompositeApprovalMatrixStatusFromDBXDB(ApprovalUtilities.fetchJSONFromContractCifMap(contractCifMap),featureActionIds);
        if (approvalMatrixStatusResponse == null) {
            throw new ApplicationException(ErrorCodeEnum.ERR_87215);
        }
        for (Object obj : approvalMatrixStatusResponse) {
            JSONObject approvalModeObj = (JSONObject) obj;
            String contractId = approvalModeObj.optString("contractId", null);
            String coreCustomerId = approvalModeObj.optString("coreCustomerId", null);
            Boolean isDisabled = approvalModeObj.optBoolean("isDisabled", false);
            if (contractId != null) {
                if (!compositeApprovalMatrixStatusMap.containsKey(contractId)) {
                    compositeApprovalMatrixStatusMap.put(contractId, new HashMap<>());
                }
                if (coreCustomerId != null) {
                    if (!compositeApprovalMatrixStatusMap.get(contractId).containsKey(coreCustomerId)) {
                        compositeApprovalMatrixStatusMap.get(contractId).put(coreCustomerId, isDisabled);
                    }
                }
            }
        }
        return compositeApprovalMatrixStatusMap;
    }

    @Override
    public Boolean fetchApprovalMatrixStatusForContractAndCif(String coreCustomerId, String contractId) throws ApplicationException {
        JSONArray approvalMatrixStatusResponse = approvalMatrixBackendDelegate.fetchApprovalMatrixStatusFromDBXDB(coreCustomerId, contractId);
        JSONObject approvalMatrixStatusJSON = approvalMatrixStatusResponse.getJSONObject(0);
        return approvalMatrixStatusJSON.optBoolean("isDisabled", true);
    }

    @Override
    public boolean createOrUpdateDefaultApprovalMatrixEntry(String contractId, Set<String> coreCustomerIds, Set<String> featureActionIds, Set<String> accountIds, String legalEntityId) throws ApplicationException {
        boolean isGroupMatrix = false;
        boolean isDefaultDisabled = false;
        String legalEntityCurrency = LegalEntityUtil.getCurrencyForLegalEntity(legalEntityId);
        try {
            String defaultApprovalMode = EnvironmentConfigurationsHandler.getValue(Constants.AM_MODE_DEFAULT_SIGN_GROUP);
            String requiredEntityTypes = EnvironmentConfigurationsHandler.getValue(Constants.AM_REQUIRE_APPROVALS_FOR_ENTITY_TYPE);

            if (defaultApprovalMode == null || defaultApprovalMode.equalsIgnoreCase("FALSE")) {
                isGroupMatrix = false;
            } else {
                isGroupMatrix = true;
            }
            List<String> elibilityList = new ArrayList<>();

            if (requiredEntityTypes != null && !requiredEntityTypes.isEmpty()) {
                elibilityList = Arrays.asList(requiredEntityTypes.split("\\s*,\\s*"));
            }

            ContractBackendDelegate contractBD = DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractBackendDelegate.class);
            ContractDTO contractDTO = new ContractDTO();
            contractDTO.setId(contractId);

            contractDTO = contractBD.getContractDetails(contractDTO, null);
            if (contractDTO == null) {
                isDefaultDisabled = true;
            } else {
                String serviceType = contractDTO.getServiceType();

                if (elibilityList != null && elibilityList.contains(serviceType)) {
                    isDefaultDisabled = false;
                } else {
                    isDefaultDisabled = true;
                }
            }
            return approvalMatrixBackendDelegate.createOrUpdateDefaultApprovalMatrixEntryInDBXDB(contractId, coreCustomerIds, featureActionIds, accountIds, isGroupMatrix, isDefaultDisabled, legalEntityCurrency);
        } catch (Exception e) {

        }
        return false;
    }

    @Override
    public boolean createOrUpdateApprovalRule(String contractId, String coreCustomerId, String featureActionId, Set<String> accountIds, String limitTypeId, List<ApprovalRuleDTO> limitRules) throws ApplicationException {
        LimitsDTO contractLimits = contractBusinessDelegate.fetchLimits(contractId, coreCustomerId, featureActionId, "ALL");
        if (contractLimits == null) {
            // TODO: throw exception - could not fetch contract-level limits
            throw new ApplicationException(ErrorCodeEnum.ERR_10775);
        }
        Double maxLimit;
        switch (limitTypeId) {
            case Constants.MAX_TRANSACTION_LIMIT:
                maxLimit = contractLimits.getMaxTransactionLimit();
                break;
            case Constants.DAILY_LIMIT:
                maxLimit = contractLimits.getDailyLimit();
                break;
            case Constants.WEEKLY_LIMIT:
                maxLimit = contractLimits.getWeeklyLimit();
                break;
            default:
                maxLimit = null;
        }
        List<ApprovalRuleDTO> sortedLimitRules = ApprovalUtilities.validateAndSortApprovalRules(limitTypeId, limitRules, maxLimit);
        if (sortedLimitRules == null) {
            // validation of limits failed - invalid limits
            throw new ApplicationException(ErrorCodeEnum.ERR_87114);
        }
        return approvalMatrixBackendDelegate.createOrUpdateApprovalRuleInDBXDB(contractId, coreCustomerId, featureActionId, accountIds, limitTypeId, ApprovalUtilities.fetchJSONArrayFromLimitRule(sortedLimitRules));
    }
    @Override
	public Result createOrUpdateApprovalRuleWithApprovalRuleCheck(String contractId, String coreCustomerId,
			String featureActionId, Set<String> accountIds, String limitTypeId, JSONArray limits, DataControllerRequest dcRequest) throws ApplicationException {
    	Map<String, Object> customer = CustomerSession.getCustomerMap(dcRequest);
    	Result result = new Result();

        boolean isAPIUser = CustomerSession.IsAPIUser(customer);
    	if (!isAPIUser) {
			try {
				int pendingRecordsExists = ApprovalUtilities.isApprovalMatrixChangeRequestPendingInQueue(
						FeatureAction.APPROVAL_MATRIX_MANAGE, contractId + "_" + coreCustomerId,
						featureActionId + "_" + limitTypeId);
				if (pendingRecordsExists == 1) {
					diagnostic.prepareDebug("Approval Matrix - Pending request in Queue"
							+ " isApprovalMatrixChangeRequestPendingInQueue returned 1 with companyId as " + contractId
							+ "_" + coreCustomerId + " and transactionId as " + featureActionId + "_" + limitTypeId)
							.log();
                    return ErrorCodeEnum.ERR_87410.setErrorCode(new Result());
				} else if (pendingRecordsExists == -1) {
					alert.prepareError("Approval Matrix - Failed to check pending request in Queue").log();
					diagnostic.prepareDebug("Approval Matrix - Failed to check pending request in Queue"
							+ " isApprovalMatrixChangeRequestPendingInQueue returned -1 with companyId as " + contractId
							+ "_" + coreCustomerId + " and transactionId as " + featureActionId + "_" + limitTypeId)
							.log();
                    return ErrorCodeEnum.ERR_87411.setErrorCode(new Result());
				}
			} catch (Exception e) {
				alert.prepareError("Approval Matrix - Failed to check pending request in Queue" + e).log();
			}
		}
    	List<ApprovalRuleDTO> limitRules;
    	limitRules = ApprovalUtilities.fetchRuleLimitsFromJSONArray(limits);
		LimitsDTO contractLimits = contractBusinessDelegate.fetchLimits(contractId, coreCustomerId, featureActionId,
				"ALL");
		
		if (contractLimits == null) {
			// TODO: throw exception - could not fetch contract-level limits
			throw new ApplicationException(ErrorCodeEnum.ERR_10775);
		}
		Double maxLimit;
		switch (limitTypeId) {
		case Constants.MAX_TRANSACTION_LIMIT:
			maxLimit = contractLimits.getMaxTransactionLimit();
			break;
		case Constants.DAILY_LIMIT:
			maxLimit = contractLimits.getDailyLimit();
			break;
		case Constants.WEEKLY_LIMIT:
			maxLimit = contractLimits.getWeeklyLimit();
			break;
		default:
			maxLimit = null;
		}
		List<ApprovalRuleDTO> sortedLimitRules = ApprovalUtilities.validateAndSortApprovalRules(limitTypeId, limitRules,
				maxLimit);
		if (sortedLimitRules == null) {
			// validation of limits failed - invalid limits
			throw new ApplicationException(ErrorCodeEnum.ERR_87114);
		}
		
		String userId = CustomerSession.getCustomerId(customer);
		UUID uuidValue = UUID.randomUUID();
		
		TransactionStatusEnum transactionStatus = TransactionStatusEnum.SENT;
		boolean isSelfApproved = false;
		if (!isAPIUser) {
			TransactionStatusDTO transactionStatusDTO = new TransactionStatusDTO();
			transactionStatusDTO.setFeatureActionID(FeatureAction.APPROVAL_MATRIX_MANAGE);
			transactionStatusDTO.setApprovalMatrixFeatureActionID(featureActionId);
			transactionStatusDTO.setCustomerId(userId);
			transactionStatusDTO.setCompanyId(contractId + "_" + coreCustomerId);
			transactionStatusDTO.setContractId(contractId);
			transactionStatusDTO.setCoreCustomerId(coreCustomerId);
			transactionStatusDTO.setLimitTypeId(limitTypeId);
			transactionStatusDTO.setLimits(limits.toString());
			transactionStatusDTO.setConfirmationNumber(featureActionId+"_"+limitTypeId);
			ObjectMapper objectMapper = new ObjectMapper();
			String accountIdsjsonString = null;
			try {
				accountIdsjsonString = objectMapper.writeValueAsString(accountIds);
			} catch (Exception e) {
				alert.prepareError("Exception occurred while reading MANAGEAPPROVALMATRIX table: " + e).log();
			}
			transactionStatusDTO.setAccountIds(accountIdsjsonString);
			ApprovalQueueBusinessDelegate approvalQueueDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
			
			transactionStatusDTO = approvalQueueDelegate.validateForApprovals(transactionStatusDTO, dcRequest);
			if (transactionStatusDTO == null) {
				return ErrorCodeEnum.ERR_29018.setErrorCode(new Result());
			}
			if (transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null) {
				result.addParam(new Param("dbpErrCode", transactionStatusDTO.getDbpErrCode()));
				result.addParam(new Param("dbpErrMsg", transactionStatusDTO.getDbpErrMsg()));
				return result;
			}
			transactionStatus = transactionStatusDTO.getStatus();
			isSelfApproved = transactionStatusDTO.isSelfApproved();
		}
		if (transactionStatus == TransactionStatusEnum.SENT) {
			boolean ruleUpdateStatus = approvalMatrixBackendDelegate.createOrUpdateApprovalRuleInDBXDB(contractId,
					coreCustomerId, featureActionId, accountIds, limitTypeId,
					ApprovalUtilities.fetchJSONArrayFromLimitRule(sortedLimitRules));
			if (ruleUpdateStatus == false) {
				throw new ApplicationException(ErrorCodeEnum.ERR_87199);
			} else {
				result.addParam("status", "Success");
				return result;
			}
		}
		if (transactionStatus == TransactionStatusEnum.APPROVED) {
			boolean ruleUpdateStatus = approvalMatrixBackendDelegate.createOrUpdateApprovalRuleInDBXDB(contractId,
					coreCustomerId, featureActionId, accountIds, limitTypeId,
					ApprovalUtilities.fetchJSONArrayFromLimitRule(sortedLimitRules));
			if (ruleUpdateStatus == false) {
				throw new ApplicationException(ErrorCodeEnum.ERR_87199);
			} else {
				result.addParam("status", "Success");
				result.addOpstatusParam(0);
				result.addHttpStatusCodeParam(0);
				return result;
			}
		}
		if (transactionStatus == TransactionStatusEnum.PENDING) {
			result.addParam("transactionStatus", transactionStatus.toString());
			//TO-DO
			result.addParam("referenceId", uuidValue.toString());
			result.addParam("isSelfApprovalFlag", isSelfApproved ? "true" : "false");
			return result;
		}
		return result; // Need to correct here how to return the exception case

	}

    @Override
    public Map<String, Map<String, Map<String, Map<String, Map<String, Map<String, Map<String, String>>>>>>> fetchCompositeApprovalMatrixRulesForContractCifMap(Map<String, Map<String, Map<String, String>>> contractCifMap, Set<FeatureActionDTO> featureActionDTOs, String accountId) throws ApplicationException {
        JSONArray contractCifMapJSON = ApprovalUtilities.fetchJSONFromContractCifMap(contractCifMap);
        Set<String> featureActionIds = new HashSet<>();
        Map<String, Map<String, Map<String, Map<String, Map<String, Map<String, Map<String, String>>>>>>> approvalMatrixCompositeMap = new HashMap<>();

        // prepare placeholders in the approvalMatrixCompositeMap
        for (Map.Entry<String, Map<String, Map<String, String>>> contractEntry : contractCifMap.entrySet()) {
            String contractId = contractEntry.getKey();
            approvalMatrixCompositeMap.put(contractId, new HashMap<>());
            for (Map.Entry<String, Map<String, String>> cifEntry : contractEntry.getValue().entrySet()) {
                String cifId = cifEntry.getKey();
                approvalMatrixCompositeMap.get(contractId).put(cifId, new HashMap<>());
                for (FeatureActionDTO featureActionDTO : featureActionDTOs) {
                    String featureActionId = featureActionDTO.getFeatureActionId();
                    featureActionIds.add(featureActionId);
                    approvalMatrixCompositeMap.get(contractId).get(cifId).put(featureActionId, new HashMap<>());
                    if (featureActionDTO.getTypeId().equalsIgnoreCase(Constants.MONETARY_ACTIONTYPE)) {
                        approvalMatrixCompositeMap.get(contractId).get(cifId).get(featureActionId).put(Constants.MAX_TRANSACTION_LIMIT, new HashMap<>());
                        approvalMatrixCompositeMap.get(contractId).get(cifId).get(featureActionId).put(Constants.DAILY_LIMIT, new HashMap<>());
                        approvalMatrixCompositeMap.get(contractId).get(cifId).get(featureActionId).put(Constants.WEEKLY_LIMIT, new HashMap<>());
                    } else {
                        approvalMatrixCompositeMap.get(contractId).get(cifId).get(featureActionId).put(Constants.NON_MONETARY_LIMIT, new HashMap<>());
                    }
                }
            }
        }

        JSONArray approvalRulesJSONArray = approvalMatrixBackendDelegate.fetchCompositeApprovalMatrixRulesForContractCifMap(contractCifMapJSON, featureActionIds, accountId);

        if (approvalRulesJSONArray == null) {
            throw new ApplicationException(ErrorCodeEnum.ERR_87213);
        }

        for (Object obj : approvalRulesJSONArray) {
            JSONObject matrixObjJSON = (JSONObject) obj;
            String matrixId = matrixObjJSON.optString("matrixId", null);
            String contractId = matrixObjJSON.optString("contractId", null);
            String cifId = matrixObjJSON.optString("coreCustomerId", null);
            String actionId = matrixObjJSON.optString("actionId", null);
            String accId = matrixObjJSON.optString("accountId", null);
            String limitTypeId = matrixObjJSON.optString("limitTypeId", null);
            String approvalruleId = matrixObjJSON.optString("approvalruleId", null);
            String upperLimit = matrixObjJSON.optString("upperlimit", null);
            String lowerLimit = matrixObjJSON.optString("lowerlimit", null);
            String groupList = matrixObjJSON.optString("groupList", null);
            String groupRule = matrixObjJSON.optString("groupRule", null);
            String approverIds = matrixObjJSON.optString("customerIds", null);
            Integer isGroupMatrix = matrixObjJSON.optInt("isGroupMatrix", 0);

            if (!(approvalruleId != null && approvalruleId.equalsIgnoreCase("NO_APPROVAL"))) {
                if (isGroupMatrix == 1) {
                    if (StringUtils.isEmpty(groupList) || StringUtils.isEmpty(groupRule)) {
                        throw new ApplicationException(ErrorCodeEnum.ERR_87115);
                    }
                } else {
                    if (StringUtils.isEmpty(approverIds)) {
                        throw new ApplicationException(ErrorCodeEnum.ERR_87115);
                    }
                }
            }

            if (!approvalMatrixCompositeMap.containsKey(contractId)) {
                approvalMatrixCompositeMap.put(contractId, new HashMap<>());
            }
            if (!approvalMatrixCompositeMap.get(contractId).containsKey(cifId)) {
                approvalMatrixCompositeMap.get(contractId).put(cifId, new HashMap<>());
            }
            if (!approvalMatrixCompositeMap.get(contractId).get(cifId).containsKey(actionId)) {
                approvalMatrixCompositeMap.get(contractId).get(cifId).put(actionId, new HashMap<>());
            }
            if (!approvalMatrixCompositeMap.get(contractId).get(cifId).get(actionId).containsKey(limitTypeId)) {
                approvalMatrixCompositeMap.get(contractId).get(cifId).get(actionId).put(limitTypeId, new HashMap<>());
            }
            if (accId == null) {
                accId = Constants.CUSTOMERID_LEVEL;
            }
            if (!approvalMatrixCompositeMap.get(contractId).get(cifId).get(actionId).get(limitTypeId).containsKey(accId)) {
                approvalMatrixCompositeMap.get(contractId).get(cifId).get(actionId).get(limitTypeId).put(accId, new HashMap<>());
            }
            if (!approvalMatrixCompositeMap.get(contractId).get(cifId).get(actionId).get(limitTypeId).get(accId).containsKey(matrixId)) {
                approvalMatrixCompositeMap.get(contractId).get(cifId).get(actionId).get(limitTypeId).get(accId).put(matrixId, new HashMap<>());
            }
            approvalMatrixCompositeMap.get(contractId).get(cifId).get(actionId).get(limitTypeId).get(accId).get(matrixId).put("approvalruleId", approvalruleId);
            approvalMatrixCompositeMap.get(contractId).get(cifId).get(actionId).get(limitTypeId).get(accId).get(matrixId).put("upperlimit", upperLimit);
            approvalMatrixCompositeMap.get(contractId).get(cifId).get(actionId).get(limitTypeId).get(accId).get(matrixId).put("lowerlimit", lowerLimit);
            approvalMatrixCompositeMap.get(contractId).get(cifId).get(actionId).get(limitTypeId).get(accId).get(matrixId).put("groupList", groupList);
            approvalMatrixCompositeMap.get(contractId).get(cifId).get(actionId).get(limitTypeId).get(accId).get(matrixId).put("groupRule", groupRule);
            approvalMatrixCompositeMap.get(contractId).get(cifId).get(actionId).get(limitTypeId).get(accId).get(matrixId).put("approverIds", approverIds);
        }
        return approvalMatrixCompositeMap;
    }

    @Override
    public Boolean updateApprovalMode(String coreCustomerId, String contractId, Boolean isGroupLevel) throws ApplicationException {
        Boolean updateStatus = approvalMatrixBackendDelegate.updateApprovalModeInDBXDB(coreCustomerId, contractId, isGroupLevel);
        if (updateStatus == null) {
            throw new ApplicationException(ErrorCodeEnum.ERR_87116);
        }
        return updateStatus;
    }
}
