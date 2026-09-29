package com.temenos.dbx.datamigrationservices.resource.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.dbx.datamigrationservices.resource.api.MigrateApprovalMatrixResource;
import com.temenos.dbx.product.approvalsframework.approvalmatrix.businessdelegate.api.ApprovalMatrixBusinessDelegate;
import com.temenos.dbx.product.approvalsframework.approvalsframeworkcommons.dto.ApprovalRuleDTO;
import com.temenos.dbx.product.approvalsframework.approvalsframeworkcommons.util.ApprovalUtilities;
import com.temenos.dbx.product.businessdelegate.api.ApproverBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.AccountBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.FeatureActionBusinessDelegate;
import com.temenos.dbx.product.commons.dto.FeatureActionDTO;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.dto.AccountActionApproverListDTO;
import com.temenos.dbx.product.signatorygroupservices.backenddelegate.api.ApprovalModeBackendDelegate;
import com.temenos.dbx.product.signatorygroupservices.backenddelegate.api.SignatoryGroupBackendDelegate;
import com.temenos.dbx.product.signatorygroupservices.dto.ApprovalModeDTO;
import com.temenos.dbx.product.signatorygroupservices.dto.SignatoryGroupDTO;

public class MigrateApprovalMatrixResourceImpl implements MigrateApprovalMatrixResource {
	private static final LoggerUtil logger = new LoggerUtil(MigrateApprovalMatrixResourceImpl.class);
	
	private static Properties migrationProps = new Properties();

    static {
        try (InputStream inputStream = URLFinder.class.getClassLoader()
                .getResourceAsStream("DBPServiceURLs.properties");) {
        	migrationProps.load(inputStream);
        } catch (FileNotFoundException e) {
        	logger.error("error while reading properties file", e);
        } catch (IOException e) {
        	logger.error("error while reading properties file", e);
        }
    }

	@Override
	public Result createOrUpdateApprovalRule(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {

		Result result = new Result();
		String contractId = "";
		String cif = "";
		String actionId = "";
		String limitTypeId = "";
		String limits = "";
		try {
			if (StringUtils.isBlank(dcRequest.getParameter("contractId"))) {
				ErrorCodeEnum.ERR_10205.setErrorCode(result, "10205", "Missing contractId in request");
				return result;
			}
			if (StringUtils.isBlank(dcRequest.getParameter("cif"))) {
				ErrorCodeEnum.ERR_10205.setErrorCode(result, "10205", "Missing cif in request");
				return result;
			}
			if (StringUtils.isBlank(dcRequest.getParameter("actionId"))) {
				ErrorCodeEnum.ERR_10205.setErrorCode(result, "10205", "ActionId is a mandatory input");
				return result;
			}
			if (StringUtils.isBlank(dcRequest.getParameter("limitTypeId"))) {
				ErrorCodeEnum.ERR_10205.setErrorCode(result, "10205", "Missing limitTypeId in request");
				return result;
			}
			if (StringUtils.isBlank(dcRequest.getParameter("limits"))) {
				ErrorCodeEnum.ERR_10205.setErrorCode(result, "10205", "Missing limits in request");
				return result;
			}
			contractId = dcRequest.getParameter("contractId");
			cif = dcRequest.getParameter("cif");
			String accountId = StringUtils.isBlank(dcRequest.getParameter("accountId")) ? "" : dcRequest.getParameter("accountId");
			actionId = dcRequest.getParameter("actionId");
			limitTypeId = dcRequest.getParameter("limitTypeId");
			limits = dcRequest.getParameter("limits");
			
			// Approval mode validation	
			validateRuleLimits(dcRequest);
			logger.debug("*************** limits :"+limits);
			
			Map<String, Object> postParametersMap = new HashMap<>();
			postParametersMap.put("contractId", contractId);
			postParametersMap.put("cif", cif);
			postParametersMap.put("accountId", accountId);
			postParametersMap.put("actionId", actionId);
			postParametersMap.put("limitTypeId", limitTypeId);
			postParametersMap.put("limits", limits);
			
			logger.debug("*************** postParametersMap :"+postParametersMap);
			result = createOrUpdateApprovalRule(postParametersMap, dcRequest);
			
			logger.debug("*************** final result :"+ResultToJSON.convert(result));

			if (result == null || !result.hasParamByName("opstatus") || !result.getOpstatusParamValue().equals("0")) {
				ErrorCodeEnum.ERR_10205.setErrorCode(result, "10205", "Error while trying to create Approval Rule");
				result.addParam(new Param("status", "Failure", "string"));
				return result;
			}
			if (result.hasParamByName("dbpErrMsg")) {
				result.addParam(new Param("status", "Failure", "string"));
				return result;
			}
		} catch (Exception e) {
			result.addParam(new Param("FailureReason", e.getMessage(), "string"));
			ErrorCodeEnum.ERR_10205.setErrorCode(result, "10205", "Error while trying to create Approval Rule");
		}
		return result;
	}

	private String stringifyForVelocityTemplate(String str) {
		if (StringUtils.isBlank(str))
			return "\"\"";
		if (str.contains("\\"))
			str = str.replace("\\", "\\\\");
		return "\"" + str.replace("\"", "\\\"") + "\"";
	}
	
	private void validateRuleLimits(DataControllerRequest dcRequest) throws ApplicationException {
		
		String contractId = dcRequest.getParameter("contractId");
		String coreCustomerId = dcRequest.getParameter("cif");
		String accountId = StringUtils.isBlank(dcRequest.getParameter("accountId")) ? "" : dcRequest.getParameter("accountId");
		String actionId = dcRequest.getParameter("actionId");
		String limitsString = dcRequest.getParameter("limits");

		ApprovalModeBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(ApprovalModeBackendDelegate.class);
		ApprovalModeDTO approvalMode = backendDelegate.fetchApprovalMode(coreCustomerId, contractId,
				dcRequest.getHeaderMap(), dcRequest);

		Boolean isGroupMatrix = approvalMode.getIsGroupLevel();

		JSONArray limitsJSON = new JSONArray(limitsString);

		for (Object limitObj : limitsJSON) {
			JSONObject limitJSON = (JSONObject) limitObj;

			String lowerLimitStr = limitJSON.optString("lowerlimit", null);
			String upperLimitStr = limitJSON.optString("upperlimit", null);

			if (StringUtils.isBlank(lowerLimitStr)) {
				throw new ApplicationException(ErrorCodeEnum.ERR_87107);
			}
			if (StringUtils.isBlank(upperLimitStr)) {
				throw new ApplicationException(ErrorCodeEnum.ERR_87108);
			}
			
			String groupList = limitJSON.optString("groupList", null);
			String groupRule = limitJSON.optString("groupRule", null);
			String numberOfApprovals = limitJSON.optString("numberOfApprovals", null);
			JSONArray approversJSON = limitJSON.has("approvers") ? limitJSON.getJSONArray("approvers") : null;			

			if (!isGroupMatrix && numberOfApprovals != null && approversJSON != null) { // User Level
				validateUserLevelRules(limitJSON, contractId, coreCustomerId, actionId, accountId, dcRequest.getHeaderMap());
			} else if (isGroupMatrix && groupList != null && groupRule != null) { // Group Level
				validateSGLevelRules(limitJSON, contractId, dcRequest.getHeaderMap());
			} else {
				// TODO: throw error - invalid approvers list
				throw new ApplicationException(ErrorCodeEnum.ERR_87115,"Given Limit values are not applicable for the Approval mode!");
			}
		}
	}
	
	private void validateUserLevelRules(JSONObject limitJSON, String contractId, String coreCustomerId, String actionId,
			String accountId, Map<String, Object> headerMap) throws ApplicationException {

		String numberOfApprovals = limitJSON.optString("numberOfApprovals", "0");
		int numberOfApprovalsVal = Integer.parseInt(numberOfApprovals);
		if (numberOfApprovalsVal == -1 || numberOfApprovals.equals("-1")) {
			limitJSON.put("approvalruleId", migrationProps.getProperty("approvalruleId.all"));
		} else {
			limitJSON.put("approvalruleId", migrationProps.getProperty("approvalruleId." + numberOfApprovals));
		}
		if (numberOfApprovalsVal == 0) {
			limitJSON.remove("numberOfApprovals");
			limitJSON.put("numberOfApprovals", "0");
			limitJSON.put("approvers", "[]");
		} else {
			
			AccountActionApproverListDTO inputDTO = new AccountActionApproverListDTO();

			ApproverBusinessDelegate approverBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ApproverBusinessDelegate.class);

			AccountBusinessDelegate accountBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(AccountBusinessDelegate.class);
			List<String> accountIdList = new ArrayList<>();
			if (StringUtils.isEmpty(accountId)) {
				accountIdList = accountBusinessDelegate.fetchCifAccounts(coreCustomerId, contractId);
			} else {
				accountIdList.add(accountId);
			}
			String accountIds = String.join(",", accountIdList);

			if (StringUtils.isBlank(actionId) || StringUtils.isBlank(contractId) || StringUtils.isBlank(coreCustomerId)
					|| StringUtils.isBlank(accountIds)) {
				throw new ApplicationException(ErrorCodeEnum.ERR_10710);
			}

			FeatureActionBusinessDelegate featureActionBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(FeatureActionBusinessDelegate.class);
			FeatureActionDTO featureActionDTO = featureActionBusinessDelegate.getFeatureActionById(actionId);

			inputDTO.setContractId(contractId);
			inputDTO.setCif(coreCustomerId);
			inputDTO.setAccountId(accountIds);
			inputDTO.setApprovalActionList(featureActionDTO.getApproveFeatureAction());
			inputDTO.setFeatureId(featureActionDTO.getFeatureId());

			List<AccountActionApproverListDTO> approverList = approverBusinessDelegate
					.getAccountActionApproverList(inputDTO, headerMap, URLConstants.ACCOUNT_ACTION_APPROVERS_PROC);
			
			logger.error("********** approverList :"+approverList);
			
			Set<String> approversSet = new HashSet<String>();
			if (approverList == null) {
				throw new ApplicationException(ErrorCodeEnum.ERR_10711);
			} else if (approverList.isEmpty()) {
				throw new ApplicationException(ErrorCodeEnum.ERR_10712);
			} else {
				for (AccountActionApproverListDTO approver : approverList) {
					approversSet.add(approver.getId());
				}
			}
			logger.error("********** approversSet :"+approversSet);
			JSONArray approversJSON = limitJSON.has("approvers") ? limitJSON.getJSONArray("approvers") : null;
			if (approversJSON != null) {

				if (numberOfApprovalsVal > approversJSON.length()) { // If the count of numberOfApprovals are greater than in approvers list
					throw new ApplicationException(ErrorCodeEnum.ERR_87111);
				}

				for (Object obj : approversJSON) {
					JSONObject approverJSON = (JSONObject) obj;
					String approverId = approverJSON.optString("approverId", null);
					if (approverId == null || !approversSet.contains(approverId)) {
						throw new ApplicationException(ErrorCodeEnum.ERR_87111);
					}
				}
			}
		}
	}
	
	private void validateSGLevelRules(JSONObject limitJSON, String contractId, Map<String, Object> headersMap) throws ApplicationException {
		
		SignatoryGroupBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(SignatoryGroupBackendDelegate.class);
    	List<SignatoryGroupDTO> signatoryGroups = backendDelegate.fetchSignatoryGroups("",contractId,headersMap);
		
		if(signatoryGroups==null) {
			throw new ApplicationException(ErrorCodeEnum.ERR_21028);
		}
		Set <String> groupSet = new HashSet<String>();
		signatoryGroups.forEach((group)->{
			if(StringUtils.isNotBlank(group.getSignatoryGroupId())) {
				groupSet.add(group.getSignatoryGroupId());
			}
		});
		
		
		String groupList = limitJSON.optString("groupList", "[]");
		String groupRule = limitJSON.optString("groupRule", "[]");
		JSONArray groupListJSON = new JSONArray(groupList);
		JSONArray groupRuleJSON = new JSONArray(groupRule);
		
		//Validating the groupIds
		for (Object groupId : groupListJSON) {
			if(!groupSet.contains((String) groupId)) { // If SG Ids doesn't match 
				throw new ApplicationException(ErrorCodeEnum.ERR_21031);
			}
		}
		
		for (Object obj : groupRuleJSON) {
			JSONArray ruleJSON = (JSONArray) obj;
			if(ruleJSON.length() != groupListJSON.length()) { // If SG list count doesn't match with the rules array count 
				throw new ApplicationException(ErrorCodeEnum.ERR_87111);
			}
		}
	}
	
	private Result createOrUpdateApprovalRule(Map<String, Object> requestMap, DataControllerRequest dcRequest) throws ApplicationException {
        Result result = new Result();
        String contractId = requestMap.containsKey("contractId") ? (String) requestMap.get("contractId") : null;
        String coreCustomerId = requestMap.containsKey("cif") ? (String) requestMap.get("cif") : null;
        String featureActionId = requestMap.containsKey("actionId") ? (String) requestMap.get("actionId") : null;
        String accountIds = requestMap.containsKey("accountId") ? (String) requestMap.get("accountId") : null;
        Set<String> accountIdsList = (!StringUtils.isEmpty(accountIds)) ? new HashSet<>(Arrays.asList(accountIds.split(","))) : new HashSet<>();
        String limitTypeId = requestMap.containsKey("limitTypeId") ? (String) requestMap.get("limitTypeId") : null;
        JSONArray limits;
        String limitsFromService = requestMap.containsKey("limits") ? (String) requestMap.get("limits") : "{}";
        logger.debug("******************* limitsFromService :"+limitsFromService);
        try {
            limits = requestMap.containsKey("limits") ? new JSONArray((String)limitsFromService.replace("\\\"","\"") ) : null;
        } catch (JSONException je) {
            logger.debug("Invalid format for limit ranges: " + requestMap.get("limits")+ " Exception" +je.getMessage());
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
        logger.debug("******************* limits array :"+limits);
        if (limits == null || limits.isEmpty()) {
            throw new ApplicationException(ErrorCodeEnum.ERR_87114);
        }
//        if (!_isAllowedForApprovalMatrixMods(contractId, coreCustomerId, dcRequest)) {
//            throw new ApplicationException(ErrorCodeEnum.ERR_10097);
//        }
        List<ApprovalRuleDTO> limitsArray;
        limitsArray = ApprovalUtilities.fetchRuleLimitsFromJSONArray(limits);
        ApprovalMatrixBusinessDelegate approvalMatrixBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalMatrixBusinessDelegate.class);
        boolean ruleUpdateStatus = approvalMatrixBusinessDelegate.createOrUpdateApprovalRule(contractId, coreCustomerId, featureActionId, accountIdsList, limitTypeId, limitsArray);
        logger.debug("******************* ruleUpdateStatus :"+ruleUpdateStatus);
        if (ruleUpdateStatus == false) {
            throw new ApplicationException(ErrorCodeEnum.ERR_87199);
        } else {
            result.addParam("status", "Success");
            result.addOpstatusParam(0);
            result.addHttpStatusCodeParam(0);
            logger.debug("******************* ruleUpdateStatus result :"+ResultToJSON.convert(result));
            return result;
        }
    }

}
