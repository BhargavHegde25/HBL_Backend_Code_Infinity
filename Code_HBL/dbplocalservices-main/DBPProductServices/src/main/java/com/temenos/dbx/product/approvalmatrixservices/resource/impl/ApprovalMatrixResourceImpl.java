package com.temenos.dbx.product.approvalmatrixservices.resource.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.*;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.approvalmatrixservices.businessdelegate.api.ApprovalMatrixBusinessDelegate;
import com.temenos.dbx.product.approvalmatrixservices.dto.ApprovalMatrixCifDTO;
import com.temenos.dbx.product.approvalmatrixservices.dto.ApprovalMatrixDTO;
import com.temenos.dbx.product.approvalmatrixservices.dto.ApprovalMatrixOutputDTO;
import com.temenos.dbx.product.approvalmatrixservices.dto.ApprovalMatrixStatusDTO;
import com.temenos.dbx.product.approvalmatrixservices.dto.CustomerApprovalMatrixDTO;
import com.temenos.dbx.product.approvalmatrixservices.dto.SignatoryGroupMatrixDTO;
import com.temenos.dbx.product.approvalmatrixservices.resource.api.ApprovalMatrixResource;
import com.temenos.dbx.product.commons.businessdelegate.api.AccountBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.dbx.product.commons.dto.LimitsDTO;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.FeatureAction;
import com.temenos.dbx.product.contract.backenddelegate.api.ContractCoreCustomerBackendDelegate;
import com.temenos.dbx.product.dto.CustomerActionDTO;
import com.temenos.dbx.product.signatorygroupservices.businessdelegate.api.ApprovalModeBusinessDelegate;
import com.temenos.dbx.product.utils.InfinityConstants;

public class ApprovalMatrixResourceImpl implements ApprovalMatrixResource
{
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
			.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);

	ApprovalMatrixBusinessDelegate approvalMatrixBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalMatrixBusinessDelegate.class);

	@Override
	public Result fetchApprovalMatrix(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		String AC_LEVEL_CONST = "ACCOUNT_LEVEL";
		String CIF_LEVEL_CONST = "CUSTOMERID_LEVEL";
		
		
		ApprovalMatrixBusinessDelegate approvalMatrixBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(ApprovalMatrixBusinessDelegate.class);

		@SuppressWarnings("unchecked")


		Map<String, Object> inputParams =  (HashMap<String, Object>)inputArray[1];
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String contractId = (inputParams.get("contractId")==null)?"":inputParams.get("contractId").toString();
		String cif = (inputParams.get("cif")==null)?"":inputParams.get("cif").toString();
		String inputAccountId = (inputParams.get("accountId")==null)?"":inputParams.get("accountId").toString();
		String inputLimitTypeId =(inputParams.get("limitTypeId")==null)?"":inputParams.get("limitTypeId").toString();
		String inputActionId = (inputParams.get("actionId")==null)?"":inputParams.get("actionId").toString();

		if (StringUtils.isEmpty(contractId)) {
			return ErrorCodeEnum.ERR_21135.setErrorCode(new Result());
		}	

		if (StringUtils.isEmpty(cif)) {
			return ErrorCodeEnum.ERR_21136.setErrorCode(new Result());
		}	

		if( !CustomerSession.IsAPIUser(customer) ) {
			AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
			String user_id = CustomerSession.getCustomerId(customer);
			if(!checkIfUserCanAccessContractAndCif(user_id, cif, contractId)) {
				return ErrorCodeEnum.ERR_12007.setErrorCode(result);
			}
			String featureAction = Constants.APPROVAL_MATRIX_VIEW;

			// TODO : check if core customer id belongs to cif and user has access to core customer id
			if (!authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(user_id, featureAction, null,
					CustomerSession.IsCombinedUser(customer))) {
				return ErrorCodeEnum.ERR_10139.setErrorCode(result);
			}			
		}

		String filter = InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL + cif
				+ DBPUtilitiesConstants.AND + InfinityConstants.contractId + DBPUtilitiesConstants.EQUAL
				+ contractId;

		Map<String, Object> input = new HashMap<String, Object>();

		input.put(DBPUtilitiesConstants.FILTER, filter);
		JsonObject dcresponse = ServiceCallHelper.invokeServiceAndGetJson(input, request.getHeaderMap(),
				URLConstants.APPROVAL_MODE_GET);
		JsonArray actions = new JsonArray();
		Boolean isgrouplevel = false;
		if (dcresponse.has(DBPDatasetConstants.APPROVAL_MODE)) {
			JsonElement jsonElement = dcresponse.get(DBPDatasetConstants.APPROVAL_MODE);
			if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
				actions = jsonElement.getAsJsonArray();  
				for (int i = 0; i < actions.size(); i++) {
					JsonObject jsonObject2 = actions.get(i).getAsJsonObject();
					isgrouplevel = jsonObject2.get("isGroupLevel").getAsBoolean();

				}
			}
		}

		Boolean isAccountLevel = StringUtils.isEmpty(inputAccountId) ? false : true;
		

		Map<String, JSONObject> featureActionMap = approvalMatrixBusinessDelegate.fetchFeatureActionsEligibleForApproval();
		Set<String> actionSetEligibleForApproval = featureActionMap.keySet();
		List<CustomerActionDTO> customerActionsList = approvalMatrixBusinessDelegate
				.fetchCustomerActions(cif, contractId, actionSetEligibleForApproval);

		//getting unique actionId's

		Set <String> customerActionsSet= new HashSet<>();

		for(int i=0;i<customerActionsList.size();i++){
			customerActionsSet.add(customerActionsList.get(i).getActionId());
		}
		Map<String, Set<String>> contractCoreCustomerDetailsMap = new HashMap<>();

		for(CustomerActionDTO customerAction : customerActionsList) {
			
			String actionLevel = featureActionMap.containsKey(customerAction.getActionId()) 
					? featureActionMap.get(customerAction.getActionId()).getString("actionlevelId")
							: AC_LEVEL_CONST;
			
			if(contractCoreCustomerDetailsMap.containsKey(customerAction.getAccountId()) 
					&& actionLevel.equalsIgnoreCase(AC_LEVEL_CONST) ) {
				contractCoreCustomerDetailsMap.get(customerAction.getAccountId()).add(customerAction.getActionId());
			} else if (contractCoreCustomerDetailsMap.containsKey(CIF_LEVEL_CONST) && actionLevel.equalsIgnoreCase(CIF_LEVEL_CONST)) {
				contractCoreCustomerDetailsMap.get(CIF_LEVEL_CONST).add(customerAction.getActionId());
			} else {
				Set<String> values = new HashSet<>();
				values.add(customerAction.getActionId());
				if (customerAction.getAccountId() != null && actionLevel.equalsIgnoreCase(AC_LEVEL_CONST)) {
					contractCoreCustomerDetailsMap.put(customerAction.getAccountId(), values);
				} else {
					contractCoreCustomerDetailsMap.put(CIF_LEVEL_CONST, values);
				}

			}
		}

		List<String> actionIds = new ArrayList<>();

		if (StringUtils.isEmpty(inputActionId)) {
			actionIds.addAll(customerActionsSet);
		} else {
			actionIds.add(inputActionId);
		}

		Map<String, LimitsDTO>  companyLimitsDTO = approvalMatrixBusinessDelegate.fetchLimits(contractId, cif, actionIds);	
		Map<String , String> companyLimitsMap = new HashMap<>();
		for(String action: companyLimitsDTO.keySet()) {
			if(companyLimitsDTO!=null) {
				companyLimitsMap.put(action + "_"+ Constants.MAX_TRANSACTION_LIMIT, 
						String.valueOf(companyLimitsDTO.get(action).getMaxTransactionLimit()));
				companyLimitsMap.put(action + "_"+ Constants.WEEKLY_LIMIT, 
						String.valueOf(companyLimitsDTO.get(action).getWeeklyLimit()));
				companyLimitsMap.put(action + "_"+ Constants.DAILY_LIMIT, 
						String.valueOf(companyLimitsDTO.get(action).getDailyLimit()));
			}
		}

		List<String> accountIds= new ArrayList<> ();
		if (isAccountLevel) {
			accountIds.add(inputAccountId);
		} else {
			accountIds = new ArrayList<> (contractCoreCustomerDetailsMap.keySet());
			accountIds.remove(CIF_LEVEL_CONST);
		}

		List<ApprovalMatrixDTO> approvalMatrixTemplateList = approvalMatrixBusinessDelegate
				.fetchApprovalMatrixTemplate(contractId, cif, String.join(",", actionIds), inputLimitTypeId);
		List<ApprovalMatrixDTO> approvalMatrixDTOList = null;
		List<ApprovalMatrixDTO> approvalMatrixList = new ArrayList<> ();
		List<ApprovalMatrixDTO> approvalMatrixTemplateAccountList = new ArrayList<> ();
		List<ApprovalMatrixDTO> accountLevelTemplates = new ArrayList<ApprovalMatrixDTO>();

		try {
			if (accountIds != null && approvalMatrixTemplateList != null && contractCoreCustomerDetailsMap !=null) {

				List<ApprovalMatrixDTO> accountDetails = approvalMatrixBusinessDelegate.fetchAccountDetails(contractId, cif,
						String.join(",", accountIds));

				for (int accountIndex = 0; accountIndex < accountIds.size(); accountIndex++) {

					for (int i = 0; i < approvalMatrixTemplateList.size(); i++) {
						ApprovalMatrixDTO approvalMatrixDTO = new ApprovalMatrixDTO();
						Map<String, Object> accountTemplate = JSONUtils.parseAsMap(
								new JSONObject(approvalMatrixTemplateList.get(i)).toString(), String.class, Object.class);
						
						if (approvalMatrixTemplateList.get(i).getIsAccountLevel().equals("true") 
								&& featureActionMap.get(approvalMatrixTemplateList.get(i).getActionId())
												.getString("actionlevelId").equalsIgnoreCase(AC_LEVEL_CONST)) {
							accountTemplate.put(Constants.ACCOUNTID, accountIds.get(accountIndex));
							ApprovalMatrixDTO account = accountDetails.stream().filter
									(p -> p.getAccountId().equals(accountTemplate.get(Constants.ACCOUNTID)))
									.collect(Collectors.toList()).get(0);
							accountTemplate.put("accountName", account.getAccountName());
							accountTemplate.put("accountType", account.getAccountType());
							accountTemplate.put("ownershipType", account.getOwnershipType());
							accountTemplate.put("maxAmount", companyLimitsMap
									.get(accountTemplate.get("actionId") + "_" + accountTemplate.get("limitTypeId")));
							accountTemplate.put("isAccountLevel", "1");
						}
						else {
							accountTemplate.put("isAccountLevel", "0");
							accountTemplate.put(Constants.ACCOUNTID, "");
						}

						approvalMatrixDTO = (ApprovalMatrixDTO) JSONUtils.parse
								(new JSONObject(accountTemplate).toString(), ApprovalMatrixDTO.class);

						if(contractCoreCustomerDetailsMap.get(accountIds.get(accountIndex)).contains(accountTemplate.get("actionId"))
								|| contractCoreCustomerDetailsMap.get(CIF_LEVEL_CONST).contains(accountTemplate.get("actionId"))) {
							accountLevelTemplates.add(approvalMatrixDTO);
						}
					}
				}
			}
		} catch (Exception e) {
			alert.prepareError("Error while assigning approval matrix template records to each account : " + e).log();
			return ErrorCodeEnum.ERR_21037.setErrorCode(new Result()); 
		}

		if (isgrouplevel) {
			approvalMatrixDTOList = approvalMatrixBusinessDelegate
					.fetchApprovalMatrixSignatory(contractId, cif, inputAccountId, inputActionId, inputLimitTypeId);
			approvalMatrixDTOList = this.addMaxAmount(approvalMatrixDTOList,companyLimitsMap);
			approvalMatrixDTOList = this.merge(accountLevelTemplates, approvalMatrixDTOList);

			if (approvalMatrixDTOList == null) {
				return ErrorCodeEnum.ERR_21007.setErrorCode(new Result());
			}
			if (approvalMatrixDTOList.size() == 0) {
				return ErrorCodeEnum.ERR_21007.setErrorCode(new Result());
			}

			SortedSet<String> featureActions = _filterFeatureActions(accountLevelTemplates);
			HashMap<String, String> actionTypeMap = _getActionType(accountLevelTemplates);
			accountLevelTemplates = this.addMaxAmount(accountLevelTemplates,companyLimitsMap);

			for(String account:accountIds) {
				List<ApprovalMatrixDTO> list = approvalMatrixDTOList.stream().filter
						(p-> p.getAccountId().equals(account)).collect(Collectors.toList());
				list.sort(Comparator.comparing(ApprovalMatrixDTO::getLimitTypeId).thenComparing(ApprovalMatrixDTO :: getActionId));
				approvalMatrixList.addAll(list);

				List<ApprovalMatrixDTO> list2 = accountLevelTemplates.stream().filter
						(p-> p.getAccountId().equals(account)).collect(Collectors.toList());
				list2.sort(Comparator.comparing(ApprovalMatrixDTO::getLimitTypeId).thenComparing(ApprovalMatrixDTO :: getActionId));
				approvalMatrixTemplateAccountList.addAll(list2);
			}

			List<ApprovalMatrixDTO> list3 = accountLevelTemplates.stream().filter
					(p-> p.getAccountId().equals("")).collect(Collectors.toList());
			list3.sort(Comparator.comparing(ApprovalMatrixDTO::getLimitTypeId).thenComparing(ApprovalMatrixDTO :: getActionId));
			approvalMatrixTemplateAccountList.addAll(list3);

			ApprovalMatrixOutputDTO approvalMatrixOutputDTO = approvalMatrixList.get(0)
					.convertToApprovalMatrixSignatoryOutputDTO(approvalMatrixList);
			ApprovalMatrixOutputDTO templateAccountOutputDTO = approvalMatrixTemplateAccountList.get(0)
					.convertToApprovalMatrixSignatoryOutputDTO(approvalMatrixTemplateAccountList);
			return getApprovalMatrixResult(cif,inputAccountId,featureActions,approvalMatrixOutputDTO,
					templateAccountOutputDTO,actionTypeMap, true);
		} else {	

			approvalMatrixDTOList = approvalMatrixBusinessDelegate
					.fetchApprovalMatrix(contractId, cif, inputAccountId, inputActionId, inputLimitTypeId);
			approvalMatrixDTOList = this.addMaxAmount(approvalMatrixDTOList,companyLimitsMap);
			approvalMatrixDTOList = this.merge(accountLevelTemplates, approvalMatrixDTOList);
			if (approvalMatrixDTOList == null) {
				return ErrorCodeEnum.ERR_21007.setErrorCode(new Result());
			}
			if (approvalMatrixDTOList.size() == 0) {
				return ErrorCodeEnum.ERR_21007.setErrorCode(new Result());
			}

			SortedSet<String> featureActions = _filterFeatureActions(accountLevelTemplates);
			HashMap<String, String> actionTypeMap = _getActionType(accountLevelTemplates);
			accountLevelTemplates = this.addMaxAmount(accountLevelTemplates,companyLimitsMap);

			for(String account:accountIds) {
				List<ApprovalMatrixDTO> list =approvalMatrixDTOList.stream().filter
						(p-> p.getAccountId().equals(account)).collect(Collectors.toList());
				list.sort(Comparator.comparing(ApprovalMatrixDTO::getLimitTypeId).thenComparing(ApprovalMatrixDTO :: getActionId));
				approvalMatrixList.addAll(list);

				List<ApprovalMatrixDTO> list2 =accountLevelTemplates.stream().filter
						(p-> p.getAccountId().equals(account)).collect(Collectors.toList());
				list2.sort(Comparator.comparing(ApprovalMatrixDTO::getLimitTypeId).thenComparing(ApprovalMatrixDTO :: getActionId));
				approvalMatrixTemplateAccountList.addAll(list2);
			}

			List<ApprovalMatrixDTO> list3 = accountLevelTemplates.stream().filter
					(p-> p.getAccountId().equals("")).collect(Collectors.toList());
			list3.sort(Comparator.comparing(ApprovalMatrixDTO::getLimitTypeId).thenComparing(ApprovalMatrixDTO :: getActionId));
			approvalMatrixTemplateAccountList.addAll(list3);

			ApprovalMatrixOutputDTO approvalMatrixOutputDTO = approvalMatrixList.get(0)
					.convertToApprovalMatrixOutputDTO(approvalMatrixList);
			ApprovalMatrixOutputDTO templateAccountOutputDTO = approvalMatrixTemplateAccountList.get(0)
					.convertToApprovalMatrixOutputDTO(approvalMatrixTemplateAccountList);
			return getApprovalMatrixResult(cif,inputAccountId,featureActions,approvalMatrixOutputDTO, 
					templateAccountOutputDTO,actionTypeMap, false);
		}
	}

	private SortedSet<String> _filterFeatureActions(List<ApprovalMatrixDTO> approvalMatrixDTOList) {
		SortedSet<String> featureActions = new TreeSet<>();

		for( ApprovalMatrixDTO approvalMatrixDTO : approvalMatrixDTOList ) {
			featureActions.add(approvalMatrixDTO.getActionId());
		}
		return featureActions;
	}

	private HashMap<String,String> _getActionType(List<ApprovalMatrixDTO> approvalMatrixDTOList) {
		HashMap<String,String> actionTypeMap = new HashMap<>();

		for( ApprovalMatrixDTO approvalMatrixDTO : approvalMatrixDTOList ) {
			actionTypeMap.put(approvalMatrixDTO.getActionId(),approvalMatrixDTO.getActionType());
		}
		return actionTypeMap;
	}

	private Result getApprovalMatrixResult(String cif, String accountId, SortedSet<String> featureActions, 
			ApprovalMatrixOutputDTO approvalMatrixOutputDTO, ApprovalMatrixOutputDTO accountOutputDTO, 
			HashMap<String,String> actionTypeMap, boolean isGroupLevel) {
		Result result = new Result();
		JSONObject approvalMatrixJSON = new  JSONObject(approvalMatrixOutputDTO);
		if(!StringUtils.isEmpty(cif) && StringUtils.isEmpty(accountId)) {
			ApprovalMatrixCifDTO approvalMatrixCifDTO = approvalMatrixOutputDTO.getCifs().get(0);
			ApprovalMatrixCifDTO approvalMatrixCifDTO1 = accountOutputDTO.getCifs().get(0);
			approvalMatrixCifDTO1.addCommonApprovalMatrix(featureActions,actionTypeMap);
			approvalMatrixCifDTO.setCommon(approvalMatrixCifDTO1.getCommon());
			approvalMatrixJSON = new JSONObject(approvalMatrixCifDTO);
		}

		else if(!StringUtils.isEmpty(accountId)) {
			approvalMatrixJSON = ((JSONObject) approvalMatrixJSON.getJSONArray("cifs").get(0));
		}
		else {
			//no processing required
		}
		approvalMatrixJSON.put("approvalMode", "0");
		if(isGroupLevel)
			approvalMatrixJSON.put("approvalMode", "1");

		result = JSONToResult.convert(approvalMatrixJSON.toString());
		return result;		
	}


	@Override
	public Result updateApprovalMatrixEntry(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws ApplicationException {
		Result result = new Result();

		AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = 
				DBPAPIAbstractFactoryImpl.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
		ApprovalMatrixBusinessDelegate approvalMatrixBusinessDelegate = 
				DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalMatrixBusinessDelegate.class);
		AccountBusinessDelegate accountBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AccountBusinessDelegate.class); 		

		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String user_id = CustomerSession.getCustomerId(customer);
		String featureAction = FeatureAction.APPROVAL_MATRIX_MANAGE;

		String contractId = request.getParameter(Constants.CONTRACTID);
		String cif = request.getParameter(Constants.CIF);
		String limitTypeId =(request.getParameter("limitTypeId")==null)?"":request.getParameter("limitTypeId").toString();
		String actionId =(request.getParameter("actionId")==null)?"":request.getParameter("actionId").toString();

		// Perform Legal Entity Check for CIF and ContractID
		String legalEntityId = null;
		String legalEntityCurrency = null;
		try{
			legalEntityId = LegalEntityUtil.getLegalEntityForCifAndContract(cif, contractId);
		} catch (ApplicationException ae){
			return ae.getErrorCodeEnum().setErrorCode(new Result());
		}

		legalEntityCurrency = LegalEntityUtil.getCurrencyForLegalEntity(legalEntityId);

		List<CustomerActionDTO> customerActionsList = approvalMatrixBusinessDelegate.fetchCustomerActionsByUserID(cif, contractId, user_id);

		Map<String, List<String>> customerActionMap = new HashMap<>();

		// forming a map with key as action id and value as list of account Id to check in case of account level update
		for(CustomerActionDTO customerAction: customerActionsList) {
			if(customerActionMap.containsKey(customerAction.getActionId())) {
				customerActionMap.get(customerAction.getActionId()).add(customerAction.getAccountId());
			}
			else {
				List <String> accounts = new ArrayList<>();
				accounts.add(customerAction.getAccountId());
				customerActionMap.put(customerAction.getActionId(), accounts);
			}
		}

		if( !CustomerSession.IsAPIUser(customer) ) {
			//checking if the core customer has access to the given actionId
			if(!customerActionMap.containsKey(featureAction)) {
				return ErrorCodeEnum.ERR_12001.setErrorCode(result);
			}
		}

		String filter = InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL + cif
				+ DBPUtilitiesConstants.AND + InfinityConstants.contractId + DBPUtilitiesConstants.EQUAL
				+ contractId;

		Map<String, Object> input = new HashMap<String, Object>();

		input.put(DBPUtilitiesConstants.FILTER, filter);
		JsonObject dcresponse = ServiceCallHelper.invokeServiceAndGetJson(input, request.getHeaderMap(),
				URLConstants.APPROVAL_MODE_GET);
		JsonArray actions = new JsonArray();
		Boolean isgrouplevel = false;
		int approvalMode = 0;
		if (dcresponse.has(DBPDatasetConstants.APPROVAL_MODE)) {
			JsonElement jsonElement = dcresponse.get(DBPDatasetConstants.APPROVAL_MODE);
			if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
				actions = jsonElement.getAsJsonArray();
				for (int i = 0; i < actions.size(); i++) {
					JsonObject jsonObject2 = actions.get(i).getAsJsonObject();
					isgrouplevel = jsonObject2.get("isGroupLevel").getAsBoolean();

				}
			}
		}


		if (StringUtils.isEmpty(contractId)) {
			return ErrorCodeEnum.ERR_21135.setErrorCode(new Result());
		}

		if (StringUtils.isEmpty(cif)) {
			return ErrorCodeEnum.ERR_21136.setErrorCode(new Result());
		}

		Boolean isAccountLevel = false;
		List<String> accountIds = new ArrayList<>();
		if(StringUtils.isEmpty(request.getParameter(Constants.ACCOUNTID))) {
			accountIds = accountBusinessDelegate.fetchCifAccounts(cif, contractId); 
			isAccountLevel = false;
		}
		else {
			accountIds.add(request.getParameter(Constants.ACCOUNTID));
			isAccountLevel = true;
		}

		if(StringUtils.isEmpty(limitTypeId)) {
			limitTypeId = Constants.MAX_TRANSACTION_LIMIT + ","+ Constants.DAILY_LIMIT +
					"," + Constants.WEEKLY_LIMIT + "," + Constants.NON_MONETARY_LIMIT;	
		}

		JSONObject status = new JSONObject();
		JSONObject templateStatus = new JSONObject();
		List<ApprovalMatrixDTO> TemplateDTOList = null;
		List<ApprovalMatrixDTO> approvalMatrixTemplateList = null;
		Map<String,String> accountLevelMap = new HashMap<>();
		boolean accountFeature = true;
		try {
			List<ApprovalMatrixDTO> templateList = approvalMatrixBusinessDelegate
					.fetchApprovalMatrixTemplateRecords(cif);

			ContractCoreCustomerBackendDelegate contractCoreCustomerBD = DBPAPIAbstractFactoryImpl
					.getBackendDelegate(ContractCoreCustomerBackendDelegate.class);

			approvalMode = isgrouplevel ? 1 : 0;

			Set<String> customerActions = null;

			if (StringUtils.isEmpty(actionId)) {
				Map<String, Set<String>> contractCoreCustomerDetailsMap = contractCoreCustomerBD
						.getCoreCustomerAccountsFeaturesActions(contractId, cif, request.getHeaderMap());

				customerActions = contractCoreCustomerDetailsMap.get("actions");
				customerActions = approvalMatrixBusinessDelegate.getActionWithApproveFeatureAction(customerActions,request.getHeaderMap());
			} else {
				customerActions = new HashSet<>();
				customerActions.add(actionId);
			}
			/*
			//checking if core customer has access to the given customer action(s)
			for(String customerAction: customerActions) {
				if(!customerActionMap.containsKey(customerAction)) {
					return ErrorCodeEnum.ERR_21243.setErrorCode(new Result());
				}
			}

			//checking if the account has access to the given actions
	        if(isAccountLevel) {
	        	for(String customerAction: customerActions) {
	        		List<String> accounts =  customerActionMap.get(customerAction);
					if(!accounts.contains(accountIds.get(0))) {
						return ErrorCodeEnum.ERR_21244.setErrorCode(new Result());
					}
				}

	        }*/

			if (templateList == null || templateList.size() == 0) {

				approvalMatrixBusinessDelegate
				.createApprovalMatrixTemplateDefaultEntry(contractId,customerActions.toArray(new String[0]), cif, approvalMode, legalEntityId);
			}
			approvalMatrixTemplateList = approvalMatrixBusinessDelegate
					.fetchApprovalMatrixTemplate(contractId, cif, String.join(",", customerActions), limitTypeId);
			for(ApprovalMatrixDTO template: approvalMatrixTemplateList) {
				accountLevelMap.put(template.getActionId(), template.getIsAccountLevel());
			}
			//Changed Condition -- Review.
			accountFeature = accountLevelMap.get(request.getParameter(Constants.ACTIONID)) != null 
					&& (accountLevelMap.get(request.getParameter(Constants.ACTIONID)).equals("1")
							|| accountLevelMap.get(request.getParameter(Constants.ACTIONID)).equals("true"))? true : false;

			if (!isAccountLevel) {
				approvalMatrixBusinessDelegate
				.callApprovalMatrixTemplateCleanupProc(contractId,customerActions.toArray(new String[0]), cif, limitTypeId);
			}


		} catch (Exception e) {
			alert.prepareError("Error while fetching approval matrix template records : " + e).log();
			return new Result();
		}

		if(isgrouplevel) {
			List<ApprovalMatrixDTO> approvalMatrixDTOList = null;


			if(accountFeature) {
				approvalMatrixDTOList= this._populateApprovalSignatoryMatrixDetails(request, accountIds, legalEntityCurrency);
			}

			if (!isAccountLevel || !accountFeature) {
				TemplateDTOList = this._populateApprovalSignatoryMatrixTemplateDetails(request, legalEntityCurrency);

				if (TemplateDTOList == null) {
					return ErrorCodeEnum.ERR_21112.setErrorCode(result);
				}
			}


			if (accountFeature && approvalMatrixDTOList == null) {
				return ErrorCodeEnum.ERR_21112.setErrorCode(result);
			}
			if(accountFeature) {
				status = approvalMatrixBusinessDelegate.updateApprovalSignatoryMatrixEntry(approvalMatrixDTOList,accountIds, legalEntityId);
			}

		} else {
			List<ApprovalMatrixDTO> approvalMatrixDTOList = null;
			if (accountFeature) {
				approvalMatrixDTOList = this._populateApprovalMatrixDetails(request, accountIds, legalEntityCurrency);
			}

			if (!isAccountLevel || !accountFeature) {
				TemplateDTOList = this._populateApprovalMatrixTemplateDetails(request, legalEntityCurrency);

				if (TemplateDTOList == null) {
					return ErrorCodeEnum.ERR_21112.setErrorCode(result);
				}
			}

			if (accountFeature && approvalMatrixDTOList == null) {
				return ErrorCodeEnum.ERR_21112.setErrorCode(result);
			}

			if(accountFeature) {
				status = approvalMatrixBusinessDelegate.updateApprovalMatrixEntry(approvalMatrixDTOList,accountIds, legalEntityId);
			}

		}
		if((status!= null &&status.has("Success") &&status.getBoolean("Success")) || !accountFeature ) {
			result.addStringParam("Success", "Successful");	        
		}
		else {

			switch (status.getInt("ErrorCode")) {
			case 1:
				return ErrorCodeEnum.ERR_21000.setErrorCode(result);
			case 2:
				return ErrorCodeEnum.ERR_21001.setErrorCode(result);
			case 3:
				return ErrorCodeEnum.ERR_21002.setErrorCode(result);
			case 4:
				return ErrorCodeEnum.ERR_21003.setErrorCode(result);
			case 5:
				return ErrorCodeEnum.ERR_21009.setErrorCode(result);
			}
		}

		if (!isAccountLevel || !accountFeature) {
			if (isgrouplevel) {
				templateStatus = approvalMatrixBusinessDelegate.updateApprovalSignatoryMatrixTemplateEntry(TemplateDTOList, approvalMode);
			} else {
				templateStatus = approvalMatrixBusinessDelegate.updateApprovalMatrixTemplateEntry(TemplateDTOList,accountIds, approvalMode);
			}
			if (!templateStatus.getBoolean("Success")) {
				switch (status.getInt("ErrorCode")) {
				case 1:
					return ErrorCodeEnum.ERR_21035.setErrorCode(result);
				case 2:
					return ErrorCodeEnum.ERR_21000.setErrorCode(result);
				}

			}
		}
		return result;
	}

	/**
	 * method to populate ApprovalMatrix Details
	 * @param companyId 
	 * 
	 * @param DataControllerRequest
	 *            request
	 * @return List of {@link ApprovalMatrixDTO}
	 */
	protected List<ApprovalMatrixDTO> _populateApprovalMatrixDetails(DataControllerRequest request, List<String> accounts, String legalEntityCurrency) {
		try {
			List<ApprovalMatrixDTO> approvalMatrixDTOList = new ArrayList<ApprovalMatrixDTO>();		
			JSONArray limits = new JSONArray(request.getParameter(Constants.LIMITS));

			for( int accountIndex = 0; accountIndex < accounts.size(); accountIndex++) {
				for (int i = 0; i < limits.length(); i++) {		
					ApprovalMatrixDTO approvalMatrixDTO = new ApprovalMatrixDTO();
					approvalMatrixDTO.setContractId(request.getParameter(Constants.CONTRACTID));
					approvalMatrixDTO.setCifId(request.getParameter(Constants.CIF));
					approvalMatrixDTO.setAccountId(accounts.get(accountIndex));
					approvalMatrixDTO.setActionId(request.getParameter(Constants.ACTIONID));
					approvalMatrixDTO.setLimitTypeId(request.getParameter(Constants.LIMITTYPEID));
					JSONObject limit = limits.getJSONObject(i);
					approvalMatrixDTO.setLowerlimit(limit.getString(Constants.LOWERLIMIT));
					approvalMatrixDTO.setUpperlimit(limit.getString(Constants.UPPERLIMIT));
					approvalMatrixDTO.setApprovalruleId(limit.getString(Constants.APPROVALRULEID));

					JSONArray approvers = limit.getJSONArray(Constants.APPROVERS);				
					if(approvers.length() == 0 && ! Constants.NO_APPROVAL.equalsIgnoreCase(approvalMatrixDTO.getApprovalruleId())) {
						return null;
					}
					List<CustomerApprovalMatrixDTO> customerApprovalMatrixDTOList = new ArrayList<CustomerApprovalMatrixDTO>();
					for (int j = 0; j < approvers.length(); j++) {
						JSONObject approver = approvers.getJSONObject(j);
						CustomerApprovalMatrixDTO customerApprovalMatrixDTO = new CustomerApprovalMatrixDTO();
						customerApprovalMatrixDTO.setCustomerId(approver.getString(Constants.APPROVERID));
						customerApprovalMatrixDTOList.add(customerApprovalMatrixDTO);
					}
					approvalMatrixDTO.setCustomerApprovalMatrixDTO(customerApprovalMatrixDTOList);
					approvalMatrixDTO.setCurrency(legalEntityCurrency);
					approvalMatrixDTOList.add(i,approvalMatrixDTO);			
				}           	
			}

			return approvalMatrixDTOList;
		} catch (JSONException e) {			
			alert.prepareError(" failed at _populateApprovalMatrixDetails" , e).log();
			return null;
		}
		catch (Exception e) {			
			alert.prepareError(" failed at _populateApprovalMatrixDetails" , e).log();
			return null;
		}
	}

	/**
	 * method to populate ApprovalMatrixTemplate Details
	 * 
	 * @param companyId
	 * 
	 * @param DataControllerRequest
	 *            request
	 * @return List of {@link ApprovalMatrixTemplateDTO}
	 */

	protected List<ApprovalMatrixDTO> _populateApprovalMatrixTemplateDetails(DataControllerRequest request, String legalEntityCurrency) {
		try {
			List<ApprovalMatrixDTO> templateDTOList = new ArrayList<ApprovalMatrixDTO>();
			JSONArray limits = new JSONArray(request.getParameter(Constants.LIMITS));

			for (int i = 0; i < limits.length(); i++) {
				ApprovalMatrixDTO templateDTO = new ApprovalMatrixDTO();
				templateDTO.setContractId(request.getParameter(Constants.CONTRACTID));
				templateDTO.setCifId(request.getParameter(Constants.CIF));
				templateDTO.setActionId(request.getParameter(Constants.ACTIONID));
				templateDTO.setLimitTypeId(request.getParameter(Constants.LIMITTYPEID));
				templateDTO.setIsGroupMatrix("0");
				JSONObject limit = limits.getJSONObject(i);
				templateDTO.setLowerlimit(limit.getString(Constants.LOWERLIMIT));
				templateDTO.setUpperlimit(limit.getString(Constants.UPPERLIMIT));
				templateDTO.setApprovalruleId(limit.getString(Constants.APPROVALRULEID));

				JSONArray approvers = limit.getJSONArray(Constants.APPROVERS);
				if (approvers.length() == 0
						&& !Constants.NO_APPROVAL.equalsIgnoreCase(templateDTO.getApprovalruleId())) {
					return null;
				}
				List<CustomerApprovalMatrixDTO> customerApprovalMatrixDTOList = new ArrayList<CustomerApprovalMatrixDTO>();
				for (int j = 0; j < approvers.length(); j++) {
					JSONObject approver = approvers.getJSONObject(j);
					CustomerApprovalMatrixDTO customerApprovalMatrixDTO = new CustomerApprovalMatrixDTO();
					customerApprovalMatrixDTO.setCustomerId(approver.getString(Constants.APPROVERID));
					customerApprovalMatrixDTOList.add(customerApprovalMatrixDTO);
				}
				templateDTO.setCustomerApprovalMatrixDTO(customerApprovalMatrixDTOList);
				templateDTO.setCurrency(legalEntityCurrency);
				templateDTOList.add(i, templateDTO);
			}

			return templateDTOList;
		} catch (JSONException e) {
			alert.prepareError(" failed at _populateApprovalMatrixTemplateDetails", e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError(" failed at _populateApprovalMatrixTemplateDetails", e).log();
			return null;
		}
	}

	/**
	 * method to populate ApprovalMatrix & SignatortyMatrix Details
	 * @param companyId 
	 * 
	 * @param DataControllerRequest
	 *            request
	 * @return List of {@link ApprovalMatrixDTO}
	 */
	protected List<ApprovalMatrixDTO> _populateApprovalSignatoryMatrixDetails(DataControllerRequest request, List<String> accounts, String legalEntityCurrency) {
		try {
			List<ApprovalMatrixDTO> approvalMatrixDTOList = new ArrayList<ApprovalMatrixDTO>();		
			JSONArray limits = new JSONArray(request.getParameter(Constants.LIMITS));

			for( int accountIndex = 0; accountIndex < accounts.size(); accountIndex++) {
				for (int i = 0; i < limits.length(); i++) {		
					ApprovalMatrixDTO approvalMatrixDTO = new ApprovalMatrixDTO();
					approvalMatrixDTO.setContractId(request.getParameter(Constants.CONTRACTID));
					approvalMatrixDTO.setCifId(request.getParameter(Constants.CIF));
					approvalMatrixDTO.setAccountId(accounts.get(accountIndex));
					approvalMatrixDTO.setActionId(request.getParameter(Constants.ACTIONID));
					approvalMatrixDTO.setLimitTypeId(request.getParameter(Constants.LIMITTYPEID));
					approvalMatrixDTO.setIsGroupMatrix("1");
					JSONObject limit = limits.getJSONObject(i);
					approvalMatrixDTO.setLowerlimit(limit.getString(Constants.LOWERLIMIT));
					approvalMatrixDTO.setUpperlimit(limit.getString(Constants.UPPERLIMIT));

					SignatoryGroupMatrixDTO signatoryGroupMatrixDTO = new SignatoryGroupMatrixDTO();
					signatoryGroupMatrixDTO.setGroupList(limit.getString(Constants.GROUPLIST));
					signatoryGroupMatrixDTO.setGroupRule(limit.getString(Constants.GROUPRULE));

					signatoryGroupMatrixDTO = _updateParticipatingGroups(signatoryGroupMatrixDTO);
					if(signatoryGroupMatrixDTO.getGroupRule()
							.replaceAll("\\s", "").equals("[]") 
							|| signatoryGroupMatrixDTO.getGroupList().replaceAll("\\s", "").equals("[]")) {
						approvalMatrixDTO.setApprovalruleId(Constants.NO_APPROVAL);
						signatoryGroupMatrixDTO.setGroupList("[]");
						signatoryGroupMatrixDTO.setGroupRule("[]");
					}

					approvalMatrixDTO.setSignatoryGroupMatrixDTO(signatoryGroupMatrixDTO);
					approvalMatrixDTO.setCurrency(legalEntityCurrency);
					approvalMatrixDTOList.add(i,approvalMatrixDTO);			
				}           	
			}

			return approvalMatrixDTOList;
		} catch (JSONException e) {			
			alert.prepareError(" failed at _populateApprovalMatrixDetails" , e).log();
			return null;
		}
		catch (Exception e) {			
			alert.prepareError(" failed at _populateApprovalMatrixDetails" , e).log();
			return null;
		}
	}

	private SignatoryGroupMatrixDTO _updateParticipatingGroups(SignatoryGroupMatrixDTO signatoryGroupMatrixDTO) {

		Set<Integer> participatingGroupIndex = new HashSet<Integer>();

		String groupList = signatoryGroupMatrixDTO.getGroupList();
		String groupRule = signatoryGroupMatrixDTO.getGroupRule();

		if(StringUtils.isEmpty(groupList) || StringUtils.isEmpty(groupRule) || !Pattern.matches("(.)*[1-9](.)*",groupRule)) {
			signatoryGroupMatrixDTO.setGroupList("[]");
			signatoryGroupMatrixDTO.setGroupRule("[]");
			return signatoryGroupMatrixDTO;
		}

		groupList = groupList.replaceAll("\\s+","");
		groupList = groupList.substring(1, groupList.length()-1);
		List<String> groups = Arrays.asList(groupList.split(","));

		groupRule = groupRule.replaceAll("\\s+","");
		groupRule = groupRule.replace("[", "");
		groupRule = groupRule.substring(0, groupRule.length() - 2);
		String strList[] = groupRule.split("],");

		int rowLength = strList.length;
		int colLength = strList[0].split(",").length;

		List<List<String>> groupRules = new ArrayList<List<String>>();
		for (int i = 0; i < rowLength; i++) {
			String single_int[] = strList[i].split(",");
			groupRules.add(Arrays.asList(single_int));
			for (int j = 0; j < colLength; j++) {
				if(Integer.parseInt(single_int[j]) != 0) {
					participatingGroupIndex.add(j);
				}
			}
		}

		String[] finalGroupList = new String[participatingGroupIndex.size()];
		String[][] finalGroupRule = new String[rowLength][participatingGroupIndex.size()]; 
		int k = 0;
		for (int i = 0; i < colLength; i++) {
			if(participatingGroupIndex.contains(i)) {
				finalGroupList[k] = groups.get(i);

				for (int j = 0; j < rowLength; j++) {
					finalGroupRule[j][k] = groupRules.get(j).get(i);
				}

				k++;
			}
		}

		signatoryGroupMatrixDTO.setGroupList("[" + String.join(",", finalGroupList) + "]");
		signatoryGroupMatrixDTO.setGroupRule(Arrays.deepToString(finalGroupRule));

		Arrays.deepToString(finalGroupRule);
		return signatoryGroupMatrixDTO;
	}

	/**
	 * method to populate ApprovalMatrixTemplate & SignatortyMatrixTemplate Details
	 *
	 * @param DataControllerRequest
	 *            request
	 * @return List of {@link ApprovalMatrixTemplateDTO}
	 */
	protected List<ApprovalMatrixDTO> _populateApprovalSignatoryMatrixTemplateDetails(DataControllerRequest request, String legalEntityCurrency) {
		try {
			List<ApprovalMatrixDTO> templateDTOList = new ArrayList<ApprovalMatrixDTO>();
			JSONArray limits = new JSONArray(request.getParameter(Constants.LIMITS));

			for (int i = 0; i < limits.length(); i++) {
				ApprovalMatrixDTO templateDTO = new ApprovalMatrixDTO();
				templateDTO.setContractId(request.getParameter(Constants.CONTRACTID));
				templateDTO.setCifId(request.getParameter(Constants.CIF));
				templateDTO.setActionId(request.getParameter(Constants.ACTIONID));
				templateDTO.setLimitTypeId(request.getParameter(Constants.LIMITTYPEID));
				templateDTO.setIsGroupMatrix("1");
				JSONObject limit = limits.getJSONObject(i);
				templateDTO.setLowerlimit(limit.getString(Constants.LOWERLIMIT));
				templateDTO.setUpperlimit(limit.getString(Constants.UPPERLIMIT));

				SignatoryGroupMatrixDTO signatoryGroupMatrixDTO = new SignatoryGroupMatrixDTO();
				signatoryGroupMatrixDTO.setGroupList(limit.getString(Constants.GROUPLIST));
				signatoryGroupMatrixDTO.setGroupRule(limit.getString(Constants.GROUPRULE));

				signatoryGroupMatrixDTO = _updateParticipatingGroups(signatoryGroupMatrixDTO);
				if(signatoryGroupMatrixDTO.getGroupRule()
						.replaceAll("\\s", "").equals("[]") 
						|| signatoryGroupMatrixDTO.getGroupList().replaceAll("\\s", "").equals("[]")) {
					templateDTO.setApprovalruleId(Constants.NO_APPROVAL);
					signatoryGroupMatrixDTO.setGroupList("[]");
					signatoryGroupMatrixDTO.setGroupRule("[]");
				}

				templateDTO.setSignatoryGroupMatrixDTO(signatoryGroupMatrixDTO);
				templateDTO.setCurrency(legalEntityCurrency);
				templateDTOList.add(i, templateDTO);
			}

			return templateDTOList;
		} catch (JSONException e) {
			alert.prepareError(" failed at _populateApprovalSignatoryMatrixTemplateDetails", e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError(" failed at _populateApprovalSignatoryMatrixTemplateDetails", e).log();
			return null;
		}
	}

	/**
	 * method to disable ApprovalMatrix Details
	 * @param companyId 
	 * 
	 * @param DataControllerRequest
	 *            request
	 * @return Input JSON
	 */
	public Result updateApprovalMatrixStatus(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams =  (HashMap<String, Object>)inputArray[1];
		String disabled = inputParams.get("disable") != null ? inputParams.get("disable").toString() : null;
		if (StringUtils.isEmpty(disabled))
			return ErrorCodeEnum.ERR_29003.setErrorCode(new Result());
		if("true".equals(disabled.toLowerCase()))
			result=disableApprovalMatrix(methodID, inputArray, request, response);
		else if("false".equals(disabled.toLowerCase()))
			result=enableApprovalMatrix(methodID, inputArray, request, response);
		else
			return ErrorCodeEnum.ERR_29004.setErrorCode(new Result());
		return result;
	}
	/**
	 * method to disable ApprovalMatrix Details
	 * @param companyId 
	 * 
	 * @param DataControllerRequest
	 *            request
	 * @return Input JSON
	 */
	public Result disableApprovalMatrix(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		@SuppressWarnings("unchecked")

		Map<String, Object> inputParams =  (HashMap<String, Object>)inputArray[1];
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String contractId = inputParams.get("contractId") != null ? inputParams.get("contractId").toString() : null;
		String cif = (inputParams.get("cif")==null)?"":inputParams.get("cif").toString();

		if (StringUtils.isEmpty(contractId))
			return ErrorCodeEnum.ERR_29001.setErrorCode(new Result());

		if (!CustomerSession.IsAPIUser(customer))
		{
			String user_id = CustomerSession.getCustomerId(customer);
			boolean hasAccess = false;
			if (StringUtils.isNotBlank(cif)) {
				hasAccess = checkIfUserCanAccessContractAndCif(user_id, cif, contractId);
			} else {
				hasAccess = checkIfUserCanAccessContract(user_id, contractId);
			}
			if(!hasAccess) {
				return ErrorCodeEnum.ERR_12007.setErrorCode(result);
			}
			String featureAction = FeatureAction.APPROVAL_MATRIX_MANAGE;
			if(! authorizationChecksBusinessDelegate
					.isUserAuthorizedForFeatureAction(user_id,featureAction,null, CustomerSession.IsCombinedUser(customer))) {
				return ErrorCodeEnum.ERR_10139.setErrorCode(result);
			}
		}

		List<String> cifList=new ArrayList<String>();
		if(StringUtils.isEmpty(cif)) {
			cifList = approvalMatrixBusinessDelegate.getCifList(contractId);
			if(cifList.size()==0) {
				return ErrorCodeEnum.ERR_29002.setErrorCode(result);
			}
		}
		else {
			cifList.add(cif);
			List<ApprovalMatrixStatusDTO> list=approvalMatrixBusinessDelegate.fetchApprovalMatrixStatus(contractId,cifList);
			if(list == null) {
				return ErrorCodeEnum.ERR_29006.setErrorCode(result);
			}
		}

		boolean status=false;
		status = approvalMatrixBusinessDelegate.disableApprovalMatrix(contractId,cifList);
		if(status) {
			result.addStringParam("contractId",contractId);
			result.addStringParam("cif", String.join(",",cifList));
			result.addStringParam("isDisabled", "true");			
		}
		else {
			return ErrorCodeEnum.ERR_29000.setErrorCode(result);			
		}
		return result;
	}

	/**
	 * method to enable ApprovalMatrix Details
	 * @param companyId 
	 * 
	 * @param DataControllerRequest
	 *            request
	 * @return Input JSON
	 */
	public Result enableApprovalMatrix(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		@SuppressWarnings("unchecked")

		Map<String, Object> inputParams =  (HashMap<String, Object>)inputArray[1];
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String contractId = inputParams.get("contractId") != null ? inputParams.get("contractId").toString() : null;
		String cif = (inputParams.get("cif")==null)?"":inputParams.get("cif").toString();
		if (StringUtils.isEmpty(contractId))
			return ErrorCodeEnum.ERR_29001.setErrorCode(new Result());

		if (!CustomerSession.IsAPIUser(customer)) 
		{
			String user_id = CustomerSession.getCustomerId(customer);
			boolean hasAccess = false;
			if (StringUtils.isNotBlank(cif)) {
				hasAccess = checkIfUserCanAccessContractAndCif(user_id, cif, contractId);
			} else {
				hasAccess = checkIfUserCanAccessContract(user_id, contractId);
			}
			if(!hasAccess) {
				return ErrorCodeEnum.ERR_12007.setErrorCode(result);
			}
			String featureAction = FeatureAction.APPROVAL_MATRIX_MANAGE;
			if(! authorizationChecksBusinessDelegate
					.isUserAuthorizedForFeatureAction(user_id,featureAction,null, CustomerSession.IsCombinedUser(customer))) {
				return ErrorCodeEnum.ERR_10139.setErrorCode(result);
			}
		}

		List<String> cifList=new ArrayList<String>();
		if(StringUtils.isEmpty(cif)) {
			cifList = approvalMatrixBusinessDelegate.getCifList(contractId);
			if(cifList.size()==0) {
				return ErrorCodeEnum.ERR_29002.setErrorCode(result);
			}
		}
		else {
			cifList.add(cif);
			List<ApprovalMatrixStatusDTO> list=approvalMatrixBusinessDelegate.fetchApprovalMatrixStatus(contractId,cifList);
			if(list==null) {
				return ErrorCodeEnum.ERR_29006.setErrorCode(result);
			}
		}

		boolean status=false;
		status = approvalMatrixBusinessDelegate.enableApprovalMatrix(contractId,cifList);

		if(status) {
			result.addStringParam("contractId",contractId);
			result.addStringParam("cif", String.join(",",cifList));
			result.addStringParam("isDisabled", "false");			
		}
		else {
			return ErrorCodeEnum.ERR_29000.setErrorCode(result);
		}
		return result;
	}

	/**
	 * method to enable ApprovalMatrix Details
	 * @param companyId 
	 * 
	 * @param DataControllerRequest
	 *            request
	 * @return Input JSON
	 */
	public Result isApprovalMatrixDisabled(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		String flag="true";
		@SuppressWarnings("unchecked")

		Map<String, Object> inputParams =  (HashMap<String, Object>)inputArray[1];
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String contractId = inputParams.get("contractId") != null ? inputParams.get("contractId").toString() : null;
		String cif = (inputParams.get("cif")==null)?"":inputParams.get("cif").toString();
		if (StringUtils.isEmpty(contractId))
			return ErrorCodeEnum.ERR_29001.setErrorCode(new Result());

		if (!CustomerSession.IsAPIUser(customer))
		{
			String user_id = CustomerSession.getCustomerId(customer);
			boolean hasAccess = false;
			if (StringUtils.isNotBlank(cif)) {
				hasAccess = checkIfUserCanAccessContractAndCif(user_id, cif, contractId);
			} else {
				hasAccess = checkIfUserCanAccessContract(user_id, contractId);
			}
			if(!hasAccess) {
				return ErrorCodeEnum.ERR_12007.setErrorCode(result);
			}
			String featureAction = Constants.APPROVAL_MATRIX_VIEW;
			if(! authorizationChecksBusinessDelegate
					.isUserAuthorizedForFeatureAction(user_id,featureAction,null, CustomerSession.IsCombinedUser(customer))) {
				return ErrorCodeEnum.ERR_10139.setErrorCode(result);
			}
		}


		List<String> cifList=new ArrayList<String>();
		if(StringUtils.isEmpty(cif)) {
			cifList = approvalMatrixBusinessDelegate.getCifList(contractId);
			if(cifList.size() == 0)
				return ErrorCodeEnum.ERR_29002.setErrorCode(result);
		}
		else {
			cifList.add(cif);
		}

		List<ApprovalMatrixStatusDTO> status=new ArrayList<ApprovalMatrixStatusDTO>();
		status = approvalMatrixBusinessDelegate.fetchApprovalMatrixStatus(contractId,cifList);
		if(status == null) {
			return ErrorCodeEnum.ERR_29006.setErrorCode(result);
		}

		result.addStringParam("contractId",contractId);
		result.addStringParam("cif", String.join(",",cifList));

		if( status.size() < cifList.size())
			flag="false";
		else 
		{
			for (int j = 0; j < status.size(); j++) {
				if(status.get(j).getIsDisabled() == false) {
					flag="false";
					break;
				}
			}
		}
		result.addStringParam("isDisabled", flag);
		return result;
	}


	private List<ApprovalMatrixDTO> merge(List<ApprovalMatrixDTO> templateList, List<ApprovalMatrixDTO> accountList) {

		List<ApprovalMatrixDTO> mergedList = new ArrayList<ApprovalMatrixDTO>();

		if (accountList == null || accountList.size() == 0)
			return templateList; 

		if (templateList == null || templateList.size() == 0)
			return accountList;

		try {

			Map<String, List<ApprovalMatrixDTO>> accountListMap = new HashMap<>();
			Map<String, List<ApprovalMatrixDTO>> templateListMap = new HashMap<>();

			for (ApprovalMatrixDTO accountObj : accountList) {

				if (null != accountObj.getIsAccountLevel() && accountObj.getIsAccountLevel().equals("true")) {
					accountObj.setIsAccountLevel("1");
				} else if (accountObj.getIsAccountLevel().equals("false")) {
					accountObj.setIsAccountLevel("0");
				}

				String key = accountObj.getCifId() == null && accountObj.getActionId() == null 
						&& accountObj.getAccountId() == null && accountObj.getLimitTypeId() == null 
						&& accountObj.getIsAccountLevel() == null 
						? ""  : accountObj.getCifId() + "_" + accountObj.getAccountId() + "_" 
						+ accountObj.getActionId().toString() + "_" + accountObj.getLimitTypeId() 
						+ "_" + accountObj.getIsAccountLevel();

				if (accountListMap.containsKey(key)) {
					accountListMap.get(key).add(accountObj);
				} else {
					List<ApprovalMatrixDTO> valueList = new ArrayList<>();
					valueList.add(accountObj);
					accountListMap.put(key, valueList);
				}

			}

			for (ApprovalMatrixDTO templateObj : templateList) {

				String key = templateObj.getCifId() == null && templateObj.getActionId() == null 
						&& templateObj.getAccountId() == null && templateObj.getLimitTypeId() == null 
						&& templateObj.getIsAccountLevel() == null
						? "" : templateObj.getCifId() + "_" + templateObj.getAccountId() + "_" 
						+ templateObj.getActionId().toString() + "_" + templateObj.getLimitTypeId() + "_" 
						+ templateObj.getIsAccountLevel();

				if (templateListMap.containsKey(key)) {
					templateListMap.get(key).add(templateObj);
				} else {
					List<ApprovalMatrixDTO> valueList = new ArrayList<>();
					valueList.add(templateObj);
					templateListMap.put(key, valueList);
				}

			}

			Set<String> keys = new HashSet<>();
			keys.addAll(templateListMap.keySet());
			keys.addAll(accountListMap.keySet());

			for (String key : keys) {
				String [] values = key.split("\\_");
				String isAccountLevel = values[values.length-1];
				// not adding cif level feature actions to merged list
				if (isAccountLevel.equals("1")) {
					if (accountListMap.containsKey(key)) {
						mergedList.addAll(accountListMap.get(key));
					} else {
						mergedList.addAll(templateListMap.get(key));
					}
				}
			}

		} catch (Exception e) {
			alert.prepareError("Failed in merging the lists", e).log();
			return mergedList;
		}
		return mergedList;
	}

	@SuppressWarnings("deprecation")
	@Override
	public Result fetchApprovalMatrixByContractId(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		@SuppressWarnings("unchecked")

		Map<String, Object> inputParams =  (HashMap<String, Object>)inputArray[1];
		String contractId = (inputParams.get("contractId")==null)?"":inputParams.get("contractId").toString();

		if (StringUtils.isEmpty(contractId)) {
			return ErrorCodeEnum.ERR_21135.setErrorCode(new Result());
		}	

		List<String> cifs= new ArrayList<> ();

		result.addStringParam("contractId",contractId);
		cifs = approvalMatrixBusinessDelegate.getCifList(contractId);

		JSONArray jsonarray=new JSONArray();

		try {
			if (cifs != null) {
				for (int cifIndex = 0; cifIndex < cifs.size(); cifIndex++) {
					ApprovalModeBusinessDelegate approvalModeBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalModeBusinessDelegate.class);
					boolean isGroupRule = approvalModeBusinessDelegate.isGroupLevel(cifs.get(cifIndex));
					int approvalMode = isGroupRule ? 1 : 0;
					inputParams.put("cif", cifs.get(cifIndex));
					inputArray[1]=new Object();
					inputArray[1]=inputParams;
					Result cifdetails = fetchApprovalMatrix(methodID, inputArray, request, response);
					cifdetails.addStringParam("approvalMode",String.valueOf(approvalMode));
					JSONObject responseJson = new JSONObject(ResultToJSON.convert(cifdetails));
					jsonarray.put(responseJson);
				}
				Dataset DetailsArray = HelperMethods.constructDatasetFromJSONArray(jsonarray);
				DetailsArray.setId("cif");
				result.addDataset(DetailsArray);
			}
		} catch (Exception e) {
			alert.prepareError("Error while fetching approval matrix records for each cif : " + e).log();
			return ErrorCodeEnum.ERR_21038.setErrorCode(new Result()); 
		}

		return result;

	}

	private List<ApprovalMatrixDTO> addMaxAmount(List<ApprovalMatrixDTO> accountList, Map<String , String> companyLimitsMap) {

		if(accountList !=null && companyLimitsMap!=null) {
			for(ApprovalMatrixDTO account : accountList) {
				account.setMaxAmount(companyLimitsMap.get(account.getActionId()+"_"+account.getLimitTypeId()));
			}
		}
		return accountList;
	}
	
	private static boolean checkIfUserCanAccessContractAndCif(String customerId, String cif, String contractId) {
		Map<String, Object> params = new HashMap<>();
		params.put(DBPUtilitiesConstants.FILTER, "customerId" + DBPUtilitiesConstants.EQUAL + customerId);
		try {
		JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(params, new HashMap<>(), URLConstants.CONTRACT_CUSTOMERS_GET);
		Set<String> contracts = new HashSet<>();
		Set<String> cifs = new HashSet<>();
    	if(response != null && response.has("contractcustomers")){
			JsonArray responseArr = JSONUtil.getJsonArrary(response, "contractcustomers");
			for(JsonElement jsonEle : responseArr) {
				JsonObject current = jsonEle.getAsJsonObject();
				contracts.add(current.get("contractId").getAsString());
				cifs.add(current.get("coreCustomerId").getAsString());
			}
		}
    	if(contracts.contains(contractId) && cifs.contains(cif))
    		return true;
		} catch(Exception e) {
			return false;
		}
		return false;
	}
	private static boolean checkIfUserCanAccessContract(String customerId, String contractId) {
		Map<String, Object> params = new HashMap<>();
		params.put(DBPUtilitiesConstants.FILTER, "customerId" + DBPUtilitiesConstants.EQUAL + customerId);
		try {
		JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(params, new HashMap<>(), URLConstants.CONTRACT_CUSTOMERS_GET);
		Set<String> contracts = new HashSet<>();
    	if(response != null && response.has("contractcustomers")){
			JsonArray responseArr = JSONUtil.getJsonArrary(response, "contractcustomers");
			for(JsonElement jsonEle : responseArr) {
				JsonObject current = jsonEle.getAsJsonObject();
				contracts.add(current.get("contractId").getAsString());
			}
		}
    	if(contracts.contains(contractId))
    		return true;
		} catch(Exception e) {
			return false;
		}
		return false;
	}
}