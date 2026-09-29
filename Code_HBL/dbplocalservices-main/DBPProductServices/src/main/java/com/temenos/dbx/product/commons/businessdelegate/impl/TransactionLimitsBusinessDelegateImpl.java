package com.temenos.dbx.product.commons.businessdelegate.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.kony.dbp.exception.ApplicationException;
import com.kony.dbp.handler.LimitsHandler;
import com.kony.dbputilities.util.LegalEntityUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.achservices.dto.ACHFileDTO;
import com.temenos.dbx.product.approvalmatrixservices.businessdelegate.api.ApprovalMatrixBusinessDelegate;
import com.temenos.dbx.product.approvalmatrixservices.dto.ApprovalMatrixDTO;
import com.temenos.dbx.product.approvalmatrixservices.dto.ApprovalMatrixStatusDTO;
import com.temenos.dbx.product.approvalmatrixservices.dto.SignatoryGroupMatrixDTO;
import com.temenos.dbx.product.approvalservices.businessdelegate.api.ApproversBusinessDelegate;
import com.temenos.dbx.product.commons.backenddelegate.api.TransactionLimitsBackendDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.AccountBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.ContractBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.CustomerBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.FeatureActionBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.LimitGroupBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.TransactionLimitsBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.UserRoleBusinessDelegate;
import com.temenos.dbx.product.commons.dto.ApplicationDTO;
import com.temenos.dbx.product.commons.dto.CustomerAccountsDTO;
import com.temenos.dbx.product.commons.dto.LimitsDTO;
import com.temenos.dbx.product.commons.dto.TransactionStatusDTO;
import com.temenos.dbx.product.commons.dto.UserLimitsDTO;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.FeatureAction;
import com.temenos.dbx.product.constants.TransactionStatusEnum;

/**
 * 
 * @author KH2174
 * @version 1.0
 * TransactionLimitsBusinessDelegateImpl implements {@link TransactionLimitsBusinessDelegate}
 */

public class TransactionLimitsBusinessDelegateImpl implements TransactionLimitsBusinessDelegate {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	ApprovalMatrixBusinessDelegate approvalMatrixBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalMatrixBusinessDelegate.class);
	ApplicationBusinessDelegate applicationBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);
	CustomerBusinessDelegate customerDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(CustomerBusinessDelegate.class);
	ContractBusinessDelegate contractDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);
	UserRoleBusinessDelegate userRoleBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(UserRoleBusinessDelegate.class);
	ApproversBusinessDelegate approversBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApproversBusinessDelegate.class);
	AccountBusinessDelegate accountBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AccountBusinessDelegate.class);
//	ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);
	LimitGroupBusinessDelegate limitGroupBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(LimitGroupBusinessDelegate.class);
	FeatureActionBusinessDelegate featureActionDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(FeatureActionBusinessDelegate.class);
	TransactionLimitsBackendDelegate transactionLimitsBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(TransactionLimitsBackendDelegate.class);
	
	@Override
	public TransactionStatusDTO validateForLimits(String userId, String companyId, String accountId, String featureActionID, Double amount,
			TransactionStatusEnum transactionStatus, String date, String transactionCurrency, String serviceCharge, DataControllerRequest request)  {
	
		TransactionStatusDTO result = new TransactionStatusDTO();
		List<String> limitTypes = new ArrayList<>();

		CustomerAccountsDTO account = accountBusinessDelegate.getAccountDetails(userId, accountId);
		String contractId = account.getContractId();
		String coreCustomerId = account.getCoreCustomerId();

		String legalEntityId = null;
		try {
			legalEntityId = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(request);
		}catch(Exception e){
			alert.prepareError(e.toString()).log();
		}
		
		//String baseCurrency = application.getBaseCurrencyFromCache();
		String baseCurrency = approvalMatrixBusinessDelegate.fetchUserApprovalCurrency(coreCustomerId,contractId,accountId);
		if(StringUtils.isEmpty(baseCurrency))
			baseCurrency = "USD";
		if(StringUtils.isEmpty(transactionCurrency))
			transactionCurrency = baseCurrency;
		if(StringUtils.isEmpty(serviceCharge))
			serviceCharge = "0.0";
		
		// we need to fetch the service charge and the converted amount using a validate
		// call and populate here
		Double charges = 0.0;
		Double totalAmount = 0.0;
		Double convertedAmount = 0.0;
		

		result.setTransactionAmount(amount + "");
		result.setServiceCharge(serviceCharge);

		if (!transactionCurrency.equalsIgnoreCase(baseCurrency)) {
			try {
				convertedAmount = getNewConvertedAmount(amount, transactionCurrency, baseCurrency, request);
				convertedAmount = (double) Math.round(convertedAmount * 100.0)/100.0;
				
				/*
				 * commented "charges", as it will be taken care of in next release,
				 *  when totaldebitAmount value is considered.
				 */
				//charges = Double.parseDouble(serviceCharge);
				
				totalAmount = convertedAmount + charges;
			}catch (Exception e) {
				alert.prepareError("Failed to fetch converted amount", e).log();
				result.setDbpErrCode(ErrorCodeEnum.ERR_27016.getErrorCodeAsString());
				result.setDbpErrMsg(ErrorCodeEnum.ERR_27016.getMessage());
				return result;
			}
		} else {
			totalAmount = amount + charges;
		}

		result.setAmount(totalAmount);
		amount = totalAmount;
		
		//Validating contract-coreCustomer level limits for users
		if(StringUtils.isNotEmpty(contractId) && StringUtils.isNotEmpty(coreCustomerId)) {
			
			LimitsDTO contractCutsomerLimitsDTO = contractDelegate.fetchLimits(contractId, coreCustomerId, featureActionID, legalEntityId);
			LimitsDTO exhaustedDTO = contractDelegate.fetchExhaustedLimits(contractId, coreCustomerId, featureActionID, date);
			
			if(contractCutsomerLimitsDTO == null 
					|| exhaustedDTO == null 
					|| exhaustedDTO.getDbpErrCode() != null 
					|| exhaustedDTO.getDbpErrMsg() != null) {
				
				alert.prepareError("Failed to fetch organization limits").log();
				String dbpErrCode = exhaustedDTO.getDbpErrCode() == null 
						? ErrorCodeEnum.ERR_12508.getErrorCodeAsString() 
								: exhaustedDTO.getDbpErrCode();
				String dbpErrMsg = exhaustedDTO.getDbpErrMsg() == null 
						? ErrorCodeEnum.ERR_12508.getMessage()
								: exhaustedDTO.getDbpErrMsg();
				result.setDbpErrCode(dbpErrCode);
				result.setDbpErrMsg(dbpErrMsg);
				return result;
			}
			
			Double newDailyValue = amount + exhaustedDTO.getDailyLimit();
			Double newWeeklyValue = amount + exhaustedDTO.getWeeklyLimit();
			//Getting feature level limit for min amount per transaction
			Double minAmountPerTrLimit = contractCutsomerLimitsDTO.getMinTransactionLimit();
			Double perTrLimit = contractCutsomerLimitsDTO.getMaxTransactionLimit();
			Double dailyLimit = contractCutsomerLimitsDTO.getDailyLimit();
			Double weeklyLimit = contractCutsomerLimitsDTO.getWeeklyLimit();
			diagnostic.debug("HBL::TransactionLimitsBusinessDelegateImpl::minAmountPerTrLimit"+minAmountPerTrLimit);
			if(Double.compare(amount, minAmountPerTrLimit) >= 0) {
			if(Double.compare(amount, perTrLimit) <= 0) {
				if(Double.compare(newDailyValue, dailyLimit) <= 0 ) {
					if(Double.compare(newWeeklyValue, weeklyLimit) <= 0) {
						// all company level limits are satisfied
					}
					else {
						result.setStatus(TransactionStatusEnum.DENIED_WEEKLY);
						result.setDbpErrCode(ErrorCodeEnum.ERR_12506.getErrorCodeAsString());
						result.setDbpErrMsg(ErrorCodeEnum.ERR_12506.getMessage());
						return result;
					}
				}
				else {
					result.setStatus(TransactionStatusEnum.DENIED_DAILY);
					result.setDbpErrCode(ErrorCodeEnum.ERR_12505.getErrorCodeAsString());
					result.setDbpErrMsg(ErrorCodeEnum.ERR_12505.getMessage());
					return result;
				}
			}
			else {
				result.setStatus(TransactionStatusEnum.DENIED_MAX_TRANSACTION);
				result.setDbpErrCode(ErrorCodeEnum.ERR_12504.getErrorCodeAsString());
				result.setDbpErrMsg(ErrorCodeEnum.ERR_12504.getMessage());
				return result;
			}
			}
			else {
				result.setStatus(TransactionStatusEnum.DENIED_MIN_TRANSACTION);
				result.setDbpErrCode(ErrorCodeEnum.ERR_12512.getErrorCodeAsString());
				result.setDbpErrMsg(ErrorCodeEnum.ERR_12512.getMessage());
				return result;
			}
		
			//Validating role level limits for business users
			
			String userRole = customerDelegate.getUserContractCustomerRole(contractId, coreCustomerId, userId);
			if(userRole == null) {
				alert.prepareError("Error while fetching user Role").log();
				String dbpErrCode = ErrorCodeEnum.ERR_12509.getErrorCodeAsString();
				String dbpErrMsg =  ErrorCodeEnum.ERR_12509.getMessage();
				result.setDbpErrCode(dbpErrCode);
				result.setDbpErrMsg(dbpErrMsg);
				return result;
			}
					
			LimitsDTO roleLimitsDTO = userRoleBusinessDelegate.fetchLimits(userRole, featureActionID);
			exhaustedDTO = userRoleBusinessDelegate.fetchExhaustedLimits(contractId, coreCustomerId, userRole, featureActionID, date, userId);
			
			if(roleLimitsDTO == null 
					|| exhaustedDTO == null 
					|| exhaustedDTO.getDbpErrCode() != null 
					|| exhaustedDTO.getDbpErrMsg() != null ) {
				
				alert.prepareError("Error while fetching role limits").log();
				String dbpErrCode = exhaustedDTO.getDbpErrCode() == null 
						? ErrorCodeEnum.ERR_12510.getErrorCodeAsString() 
								: exhaustedDTO.getDbpErrCode();
				String dbpErrMsg = exhaustedDTO.getDbpErrMsg() == null 
						? ErrorCodeEnum.ERR_12510.getMessage()
								: exhaustedDTO.getDbpErrMsg();
				result.setDbpErrCode(dbpErrCode);
				result.setDbpErrMsg(dbpErrMsg);
				return result;
			}
			
			newDailyValue = amount + exhaustedDTO.getDailyLimit();
			newWeeklyValue = amount + exhaustedDTO.getWeeklyLimit();
			
			perTrLimit = roleLimitsDTO.getMaxTransactionLimit();
			dailyLimit = roleLimitsDTO.getDailyLimit();
			weeklyLimit = roleLimitsDTO.getWeeklyLimit();
			
			if(Double.compare(amount, perTrLimit) <= 0) {
				if(Double.compare(newDailyValue, dailyLimit) <= 0 ) {
					if(Double.compare(newWeeklyValue, weeklyLimit) <= 0) {
						// all role level limits are satisfied
					}
					else {
						result.setStatus(TransactionStatusEnum.DENIED_WEEKLY);
						result.setDbpErrCode(ErrorCodeEnum.ERR_12506.getErrorCodeAsString());
						result.setDbpErrMsg(ErrorCodeEnum.ERR_12506.getMessage());
						return result;
					}
				}
				else {
					result.setStatus(TransactionStatusEnum.DENIED_DAILY);
					result.setDbpErrCode(ErrorCodeEnum.ERR_12505.getErrorCodeAsString());
					result.setDbpErrMsg(ErrorCodeEnum.ERR_12505.getMessage());
					return result;
				}
			}
			else {
				result.setStatus(TransactionStatusEnum.DENIED_MAX_TRANSACTION);
				result.setDbpErrCode(ErrorCodeEnum.ERR_12504.getErrorCodeAsString());
				result.setDbpErrMsg(ErrorCodeEnum.ERR_12504.getMessage());
				return result;
			}
		}
		
		//Validating customer level limits for both retail and business users
			/*		
			UserLimitsDTO userLimitsDTO = customerDelegate.fetchCustomerLimits(userId, featureActionID, accountId);
			LimitsDTO exhaustedLimitsDTO = customerDelegate.fetchExhaustedLimits(userId, featureActionID, date);
			
			if(userLimitsDTO == null 
					|| exhaustedLimitsDTO == null
					|| exhaustedLimitsDTO.getDbpErrCode() != null 
					|| exhaustedLimitsDTO.getDbpErrMsg() != null) {
				
				alert.prepareError("Error while fetching user limits").log();
				String dbpErrCode = exhaustedLimitsDTO.getDbpErrCode() == null 
						? ErrorCodeEnum.ERR_12511.getErrorCodeAsString() 
								: exhaustedLimitsDTO.getDbpErrCode();
				String dbpErrMsg = exhaustedLimitsDTO.getDbpErrMsg() == null 
						? ErrorCodeEnum.ERR_12511.getMessage()
								: exhaustedLimitsDTO.getDbpErrMsg();
				result.setDbpErrCode(dbpErrCode);
				result.setDbpErrMsg(dbpErrMsg);
				return result;
			}
			
			if(Double.compare(userLimitsDTO.getMinTransactionLimit(), amount) > 0) {
				result.setStatus(TransactionStatusEnum.DENIED_MIN_TRANSACTION);
				result.setDbpErrCode(ErrorCodeEnum.ERR_12512.getErrorCodeAsString());
				result.setDbpErrMsg(ErrorCodeEnum.ERR_12512.getMessage());
				return result;
			}
			
			Double newDailyValue = amount + exhaustedLimitsDTO.getDailyLimit();
			Double newWeeklyValue = amount + exhaustedLimitsDTO.getWeeklyLimit();
			
			Double perTrLimit = userLimitsDTO.getMaxTransactionLimit();
			Double dailyLimit = userLimitsDTO.getDailyLimit();
			Double weeklyLimit = userLimitsDTO.getWeeklyLimit();
			
			if(Double.compare(amount, perTrLimit) <= 0) {
				if(Double.compare(newDailyValue, dailyLimit) <= 0 ) {
					if(Double.compare(newWeeklyValue, weeklyLimit) <= 0) {
						// all user level limits are satisfied
					}
					else {
						result.setStatus(TransactionStatusEnum.DENIED_WEEKLY);
						result.setDbpErrCode(ErrorCodeEnum.ERR_12506.getErrorCodeAsString());
						result.setDbpErrMsg(ErrorCodeEnum.ERR_12506.getMessage());
						return result;
					}
				}
				else {
					result.setStatus(TransactionStatusEnum.DENIED_DAILY);
					result.setDbpErrCode(ErrorCodeEnum.ERR_12505.getErrorCodeAsString());
					result.setDbpErrMsg(ErrorCodeEnum.ERR_12505.getMessage());
					return result;
				}
			}
			else {
				result.setStatus(TransactionStatusEnum.DENIED_MAX_TRANSACTION);
				result.setDbpErrCode(ErrorCodeEnum.ERR_12504.getErrorCodeAsString());
				result.setDbpErrMsg(ErrorCodeEnum.ERR_12504.getMessage());
				return result;
			}
			*/
		
			//Validating Limit Group limits for both retail and business users
		String limitGroupId = featureActionDelegate.getLimitGroupId(featureActionID,legalEntityId);
		if(limitGroupId == null) {
			alert.prepareError("Error while fetching the limit group id").log();
			String dbpErrCode = ErrorCodeEnum.ERR_12519.getErrorCodeAsString();
			String dbpErrMsg =  ErrorCodeEnum.ERR_12519.getMessage();
			result.setDbpErrCode(dbpErrCode);
			result.setDbpErrMsg(dbpErrMsg);
			return result;
		}

		LimitsDTO groupLimitsDTO = limitGroupBusinessDelegate.fetchLimits(userId, contractId, coreCustomerId, limitGroupId);
		LimitsDTO exhaustedgroupLimitsDTO = limitGroupBusinessDelegate.fetchExhaustedLimits(contractId, coreCustomerId, userId, limitGroupId, date);
		if(groupLimitsDTO == null || exhaustedgroupLimitsDTO == null || exhaustedgroupLimitsDTO.getDbpErrCode() != null || exhaustedgroupLimitsDTO.getDbpErrMsg() != null) {
			alert.prepareError("Error while fetching the limits for limit groups").log();
			String dbpErrCode = exhaustedgroupLimitsDTO.getDbpErrCode() == null ? ErrorCodeEnum.ERR_12518.getErrorCodeAsString() : exhaustedgroupLimitsDTO.getDbpErrCode();
			String dbpErrMsg = exhaustedgroupLimitsDTO.getDbpErrMsg() == null ? ErrorCodeEnum.ERR_12518.getMessage() : exhaustedgroupLimitsDTO.getDbpErrMsg();
			result.setDbpErrCode(dbpErrCode);
			result.setDbpErrMsg(dbpErrMsg);
			return result;
		}

		Double newLimitGroupDailyValue = amount + exhaustedgroupLimitsDTO.getDailyLimit();
		Double newLimitGroupWeeklyValue = amount + exhaustedgroupLimitsDTO.getWeeklyLimit();

		Double limitgroupPerTrLimit = groupLimitsDTO.getMaxTransactionLimit();
		Double limitgroupDailyLimit = groupLimitsDTO.getDailyLimit();
		Double limitgroupWeeklyLimit = groupLimitsDTO.getWeeklyLimit();

		if(Double.compare(amount, limitgroupPerTrLimit) <= 0) {
			if(Double.compare(newLimitGroupDailyValue, limitgroupDailyLimit) <= 0 ) {
				if(Double.compare(newLimitGroupWeeklyValue, limitgroupWeeklyLimit) <= 0) {
					// all user level limits are satisfied
				}
				else {
					result.setStatus(TransactionStatusEnum.DENIED_WEEKLY);
					result.setDbpErrCode(ErrorCodeEnum.ERR_12506.getErrorCodeAsString());
					result.setDbpErrMsg(ErrorCodeEnum.ERR_12506.getMessage());
					return result;
				}
			}
			else {
				result.setStatus(TransactionStatusEnum.DENIED_DAILY);
				result.setDbpErrCode(ErrorCodeEnum.ERR_12505.getErrorCodeAsString());
				result.setDbpErrMsg(ErrorCodeEnum.ERR_12505.getMessage());
				return result;
			}
		}
		else {
			result.setStatus(TransactionStatusEnum.DENIED_MAX_TRANSACTION);
			result.setDbpErrCode(ErrorCodeEnum.ERR_12504.getErrorCodeAsString());
			result.setDbpErrMsg(ErrorCodeEnum.ERR_12504.getMessage());
			return result;
		}
		//check for Auto denial limits of a user
		UserLimitsDTO userLimitsDTO = customerDelegate.fetchCustomerLimits(userId, featureActionID, accountId);
		LimitsDTO exhaustedDTO = customerDelegate.fetchExhaustedLimits(userId, featureActionID, date, accountId);

		if(exhaustedDTO == null || exhaustedDTO.getDbpErrCode() != null || exhaustedDTO.getDbpErrMsg() != null) {
			alert.prepareError("Error while fetching user limits").log();
			String dbpErrCode = exhaustedDTO.getDbpErrCode() == null
					? ErrorCodeEnum.ERR_12511.getErrorCodeAsString()
					: exhaustedDTO.getDbpErrCode();
			String dbpErrMsg = exhaustedDTO.getDbpErrMsg() == null
					? ErrorCodeEnum.ERR_12511.getMessage()
					: exhaustedDTO.getDbpErrMsg();
			result.setDbpErrCode(dbpErrCode);
			result.setDbpErrMsg(dbpErrMsg);
			return result;
		}

		Double newDailyValue = amount + exhaustedDTO.getDailyLimit();
		Double newWeeklyValue = amount + exhaustedDTO.getWeeklyLimit();

		Double autoDenialPerTrLimit = userLimitsDTO.getAutoDeniedTransactionLimit();
		Double autoDenialDailyLimit = userLimitsDTO.getAutoDeniedDailyLimit();
		Double autoDenialWeeklyLimit = userLimitsDTO.getAutoDeniedWeeklyLimit();

		if(Double.compare(amount, autoDenialPerTrLimit) <= 0) {
			if(Double.compare(newDailyValue, autoDenialDailyLimit) <= 0 ) {
				if(Double.compare(newWeeklyValue, autoDenialWeeklyLimit) <= 0) {
					// all auto denial limits are satisfied
				}
				else {
					result.setStatus(TransactionStatusEnum.DENIED_AD_WEEKLY);
					result.setDbpErrCode(ErrorCodeEnum.ERR_12503.getErrorCodeAsString());
					result.setDbpErrMsg(ErrorCodeEnum.ERR_12503.getMessage());
					return result;
				}
			}
			else {
				result.setStatus(TransactionStatusEnum.DENIED_AD_DAILY);
				result.setDbpErrCode(ErrorCodeEnum.ERR_12502.getErrorCodeAsString());
				result.setDbpErrMsg(ErrorCodeEnum.ERR_12502.getMessage());
				return result;
			}
		}
		else {
			result.setStatus(TransactionStatusEnum.DENIED_AD_MAX_TRANSACTION);
			result.setDbpErrCode(ErrorCodeEnum.ERR_12501.getErrorCodeAsString());
			result.setDbpErrMsg(ErrorCodeEnum.ERR_12501.getMessage());
			return result;
		}

		if(account != null) {
			List<ApprovalMatrixStatusDTO> status = approvalMatrixBusinessDelegate.fetchApprovalMatrixStatus(contractId, Arrays.asList(coreCustomerId));

			if(status!= null && status.size() > 0 && status.get(0).getIsDisabled()) {
				result.setStatus(TransactionStatusEnum.SENT);
				return result;
			}
		}
				
		if(transactionStatus.equals(TransactionStatusEnum.NEW)) {
			//check for pre approve limits of a user
			Double apreApprovePerTrLimit = userLimitsDTO.getPreApprovedTransactionLimit();
			Double apreApproveDailyLimit = userLimitsDTO.getPreApprovedDailyLimit();
			Double apreApproveWeeklyLimit = userLimitsDTO.getPreApprovedWeeklyLimit();
			if(Double.compare(amount, apreApprovePerTrLimit) > 0){
				//needs approval
				limitTypes.add(Constants.MAX_TRANSACTION_LIMIT);
			}
			if(Double.compare(newDailyValue, apreApproveDailyLimit) > 0 ) {
				//needs approval
				limitTypes.add(Constants.DAILY_LIMIT);
			}
			if(Double.compare(newWeeklyValue, apreApproveWeeklyLimit) > 0) {
				//needs approval
				limitTypes.add(Constants.WEEKLY_LIMIT);
			}
		}
		List<ApprovalMatrixDTO> approvalmatrices = new ArrayList<> ();

		if (limitTypes.size() > 0) {
			approvalmatrices.addAll(approvalMatrixBusinessDelegate.fetchApprovalMatrix(contractId, coreCustomerId, accountId, featureActionID, ""));
			// if account level rules are not present, then retrieve from the template tables
			Set<String> limits = new HashSet<> ();
			for (ApprovalMatrixDTO matrix : approvalmatrices) {
				limits.add(matrix.getLimitTypeId());
			}
			if((approvalmatrices == null ||approvalmatrices.size() == 0) || !limits.contains(Constants.MAX_TRANSACTION_LIMIT) || !limits.contains(Constants.DAILY_LIMIT) || !limits.contains(Constants.WEEKLY_LIMIT)) {
				List<String> limitValues = new ArrayList<> ();
				if(!limits.contains(Constants.MAX_TRANSACTION_LIMIT)) {
					limitValues.add(Constants.MAX_TRANSACTION_LIMIT);
				}
				if(!limits.contains(Constants.DAILY_LIMIT)) {
					limitValues.add(Constants.DAILY_LIMIT);
				}
				if(!limits.contains(Constants.WEEKLY_LIMIT)) {
					limitValues.add(Constants.WEEKLY_LIMIT);
				}
				for(String limit : limitValues) {
					approvalmatrices.addAll(approvalMatrixBusinessDelegate.fetchApprovalMatrixTemplate(contractId, coreCustomerId, featureActionID, limit));
				}
			}
			List<String> validApprovers = approversBusinessDelegate.getAccountActionApproverList(contractId, coreCustomerId, accountId, featureActionID);
			return _fetchStatusFromLimitTypes(amount, limitTypes, approvalmatrices, validApprovers, userId, exhaustedDTO, result,request); // Modified as part of ADP-2810
		}
		else {
			//all conditions satisfied including pre approval limits if business user
			result.setStatus(TransactionStatusEnum.SENT);
			return result;
		}
	}

	@Override
	public TransactionStatusDTO validateLimitsForMonetaryAction(String customerId, String contractId, String coreCustomerId, String featureActionId, String accountId, Double transactionAmount, String serviceCharge, String transactionDate, String transactionCurrency, TransactionStatusEnum status, DataControllerRequest dcRequest) throws ApplicationException {
		TransactionStatusDTO limitsValidationStatus = new TransactionStatusDTO();
//		String baseCurrency = LegalEntityUtil.getLegalEntityCurrencyForCifAndContract(coreCustomerId, contractId);
		String baseCurrency = "USD";
		if(StringUtils.isEmpty(baseCurrency)){
			// TODO: throw error for no currency info found for legal entity
		}
		if(StringUtils.isEmpty(transactionCurrency)){
			transactionCurrency = baseCurrency;
		}
		if(StringUtils.isEmpty(serviceCharge)) {
			serviceCharge = "0.0";
		}

		// we need to fetch the service charge and the converted amount using a validate
		// call and populate here
		Double charges = 0.0;
		Double totalAmount = 0.0;
		Double convertedAmount = 0.0;
		
		limitsValidationStatus.setTransactionAmount(transactionAmount + "");
		limitsValidationStatus.setServiceCharge(serviceCharge);

		if (!transactionCurrency.equalsIgnoreCase(baseCurrency)) {
			try {
				convertedAmount = getNewConvertedAmount(transactionAmount, transactionCurrency, baseCurrency, dcRequest);
				convertedAmount = (double) Math.round(convertedAmount * 100.0)/100.0;
				totalAmount = convertedAmount + charges;
			}catch (Exception e) {
				alert.prepareError("Failed to fetch converted amount", e).log();
				limitsValidationStatus.setDbpErrCode(ErrorCodeEnum.ERR_27016.getErrorCodeAsString());
				limitsValidationStatus.setDbpErrMsg(ErrorCodeEnum.ERR_27016.getMessage());
				return limitsValidationStatus;
			}
		} else {
			totalAmount = transactionAmount + charges;
		}

		limitsValidationStatus.setAmount(totalAmount);
		transactionAmount = totalAmount;

		// fetch master limits
		Map<String, Object> masterLimitsMap = LimitsHandler.fetchCustomerAccountLevelMasterLimits(customerId, featureActionId);
		if(masterLimitsMap == null){
			throw new ApplicationException(ErrorCodeEnum.ERR_87301);
		}
		// FORMAT: <limitTypeId, value>
		Map<String, Double> contractLimitsMap = masterLimitsMap.containsKey(Constants.CONTRACT_LIMITS) ? (Map<String, Double>) masterLimitsMap.get(Constants.CONTRACT_LIMITS) : null;
		if(contractLimitsMap == null){
			throw new ApplicationException(ErrorCodeEnum.ERR_87302);
		}
		// FORMAT: <contractId, <coreCustomerId, <roleId, <limitTypeId, value>>>>
		Map<String, Map<String, Map<String, Map<String, Double>>>> roleLimitsMap = masterLimitsMap.containsKey(Constants.ROLE_LIMITS) ? (Map<String, Map<String, Map<String, Map<String, Double>>>>) masterLimitsMap.get(Constants.ROLE_LIMITS) : null;
		if(roleLimitsMap == null){
			throw new ApplicationException(ErrorCodeEnum.ERR_87303);
		}
		// FORMAT: <accountId,<limitTypeId, value>>
		Map<String, Map<String, Double>> customerLimitsMap = masterLimitsMap.containsKey(Constants.CUSTOMER_LIMITS) ? (Map<String, Map<String, Double>>) masterLimitsMap.get(Constants.CUSTOMER_LIMITS) : null;
		if(customerLimitsMap == null){
			throw new ApplicationException(ErrorCodeEnum.ERR_87305);
		}
		// FORMAT: <limitTypeId, value>
		Map<String, Double> customerLimits = customerLimitsMap.containsKey(accountId) ? customerLimitsMap.get(accountId) : null;
		if(customerLimits == null){
			throw new ApplicationException(ErrorCodeEnum.ERR_87306);
		}
		Map.Entry<String, Map<String, Double>> roleLimitMapEntry = roleLimitsMap.containsKey(contractId) ? (roleLimitsMap.get(contractId).containsKey(coreCustomerId) ? roleLimitsMap.get(contractId).get(coreCustomerId).entrySet().iterator().next() : null) : null;
		if(roleLimitMapEntry == null){
			throw new ApplicationException(ErrorCodeEnum.ERR_87307);
		}
		String roleId = roleLimitMapEntry.getKey();
		Map<String, Double> roleLimitValues = roleLimitMapEntry.getValue();

		if(customerLimits == null){
			// TODO: throw error for no limits found for associated accountId
		}
		String limitGroupId = masterLimitsMap.containsKey(Constants.LIMITGROUPID) ? (String) masterLimitsMap.get(Constants.LIMITGROUPID) : null;
		if(limitGroupId == null){
			throw new ApplicationException(ErrorCodeEnum.ERR_87308);
		}

		// Contract-Level Max limits check - START ------------------------------------------------------------------------------------------------------
		LimitsDTO exhaustedDTO = contractDelegate.fetchExhaustedLimits(contractId, coreCustomerId, featureActionId, transactionDate);
		// TEMP STUBBING
		exhaustedDTO.setDbpErrCode(null);
		exhaustedDTO.setDbpErrMsg(null);
		if(exhaustedDTO == null || exhaustedDTO.getDbpErrCode() != null || exhaustedDTO.getDbpErrMsg() != null) {
			// TODO: improve error code handling for exhausted limits
			alert.prepareError("Failed to fetch contract-level exhausted limits").log();
			String dbpErrCode = exhaustedDTO.getDbpErrCode() == null ? ErrorCodeEnum.ERR_12508.getErrorCodeAsString() : exhaustedDTO.getDbpErrCode();
			String dbpErrMsg = exhaustedDTO.getDbpErrMsg() == null ? ErrorCodeEnum.ERR_12508.getMessage() : exhaustedDTO.getDbpErrMsg();
			limitsValidationStatus.setDbpErrCode(dbpErrCode);
			limitsValidationStatus.setDbpErrMsg(dbpErrMsg);
			return limitsValidationStatus;
		}

		Double contractMaxLimit = contractLimitsMap.get(Constants.MAX_TRANSACTION_LIMIT);
		Double contractDailyLimit = contractLimitsMap.get(Constants.DAILY_LIMIT);
		Double contractWeeklyLimit = contractLimitsMap.get(Constants.WEEKLY_LIMIT);

		Double contractExhaustedDaily = transactionAmount + exhaustedDTO.getDailyLimit();
		Double contractExhaustedWeekly = transactionAmount + exhaustedDTO.getWeeklyLimit();

		if(Double.compare(transactionAmount, contractMaxLimit) > 0){
			throw new ApplicationException(ErrorCodeEnum.ERR_87309);
		}
		if(Double.compare(contractExhaustedDaily, contractDailyLimit) > 0){
			throw new ApplicationException(ErrorCodeEnum.ERR_87310);
		}
		if(Double.compare(contractExhaustedWeekly, contractWeeklyLimit) > 0){
			throw new ApplicationException(ErrorCodeEnum.ERR_87311);
		}
		// Contract-Level Max limits check - END ----------------------------------------------------------------------------------------------------------

		// Role-Level Max limits check - START ------------------------------------------------------------------------------------------------------------
		exhaustedDTO = userRoleBusinessDelegate.fetchExhaustedLimits(contractId, coreCustomerId, roleId, featureActionId, transactionDate, customerId);
		// TEMP STUBBING
		exhaustedDTO.setDbpErrCode(null);
		exhaustedDTO.setDbpErrMsg(null);
		if(exhaustedDTO == null || exhaustedDTO.getDbpErrCode() != null || exhaustedDTO.getDbpErrMsg() != null ) {
			// TODO: improve error code handling for exhausted limits
			alert.prepareError("Error while fetching role limits").log();
			String dbpErrCode = exhaustedDTO.getDbpErrCode() == null ? ErrorCodeEnum.ERR_12510.getErrorCodeAsString() : exhaustedDTO.getDbpErrCode();
			String dbpErrMsg = exhaustedDTO.getDbpErrMsg() == null ? ErrorCodeEnum.ERR_12510.getMessage() : exhaustedDTO.getDbpErrMsg();
			limitsValidationStatus.setDbpErrCode(dbpErrCode);
			limitsValidationStatus.setDbpErrMsg(dbpErrMsg);
			return limitsValidationStatus;
		}

		Double roleMaxLimit = roleLimitValues.containsKey(Constants.MAX_TRANSACTION_LIMIT) ? roleLimitValues.get(Constants.MAX_TRANSACTION_LIMIT) : 0.0;
		Double roleDailyLimit = roleLimitValues.containsKey(Constants.DAILY_LIMIT) ? roleLimitValues.get(Constants.DAILY_LIMIT) : 0.0;
		Double roleWeeklyLimit = roleLimitValues.containsKey(Constants.WEEKLY_LIMIT) ? roleLimitValues.get(Constants.WEEKLY_LIMIT) : 0.0;

		Double roleExhaustedDaily = transactionAmount + exhaustedDTO.getDailyLimit();
		Double roleExhaustedWeekly = transactionAmount + exhaustedDTO.getWeeklyLimit();

		if(Double.compare(transactionAmount, roleMaxLimit) > 0){
			throw new ApplicationException(ErrorCodeEnum.ERR_87312);
		}
		if(Double.compare(roleExhaustedDaily, roleDailyLimit) > 0){
			throw new ApplicationException(ErrorCodeEnum.ERR_87313);
		}
		if(Double.compare(roleExhaustedWeekly, roleWeeklyLimit) > 0){
			throw new ApplicationException(ErrorCodeEnum.ERR_87314);
		}
		// Role-Level Max limits check - END ----------------------------------------------------------------------------------------------------------

		// LimitGroup-Level Max limits check - START --------------------------------------------------------------------------------------------------
		LimitsDTO groupLimitsDTO = limitGroupBusinessDelegate.fetchLimits(customerId, contractId, coreCustomerId, limitGroupId);
		// TEMP STUBBING
		groupLimitsDTO.setMaxTransactionLimit(10000.0);
		groupLimitsDTO.setDailyLimit(500000.0);
		groupLimitsDTO.setWeeklyLimit(1000000.0);
		if(groupLimitsDTO == null){
			throw new ApplicationException(ErrorCodeEnum.ERR_87304);
		}
		exhaustedDTO = limitGroupBusinessDelegate.fetchExhaustedLimits(contractId, coreCustomerId, customerId, limitGroupId, transactionDate);
		// TEMP STUBBING
		exhaustedDTO.setDbpErrCode(null);
		exhaustedDTO.setDbpErrMsg(null);

		if(exhaustedDTO == null || exhaustedDTO.getDbpErrCode() != null || exhaustedDTO.getDbpErrMsg() != null) {
			alert.prepareError("Error while fetching the limits for limit groups").log();
			String dbpErrCode = exhaustedDTO.getDbpErrCode() == null ? ErrorCodeEnum.ERR_12518.getErrorCodeAsString() : exhaustedDTO.getDbpErrCode();
			String dbpErrMsg = exhaustedDTO.getDbpErrMsg() == null ? ErrorCodeEnum.ERR_12518.getMessage() : exhaustedDTO.getDbpErrMsg();
			limitsValidationStatus.setDbpErrCode(dbpErrCode);
			limitsValidationStatus.setDbpErrMsg(dbpErrMsg);
			return limitsValidationStatus;
		}

		Double limitGroupExhaustedDaily = transactionAmount + exhaustedDTO.getDailyLimit();
		Double limitGroupExhaustedWeekly = transactionAmount + exhaustedDTO.getWeeklyLimit();

		Double limitGroupMaxLimit = groupLimitsDTO.getMaxTransactionLimit();
		Double limitGroupDailyLimit = groupLimitsDTO.getDailyLimit();
		Double limitGroupWeeklyLimit = groupLimitsDTO.getWeeklyLimit();

		if(Double.compare(transactionAmount, limitGroupMaxLimit) > 0){
			throw new ApplicationException(ErrorCodeEnum.ERR_87315);
		}
		if(Double.compare(limitGroupExhaustedDaily, limitGroupDailyLimit) > 0){
			throw new ApplicationException(ErrorCodeEnum.ERR_87316);
		}
		if(Double.compare(limitGroupExhaustedWeekly, limitGroupWeeklyLimit) > 0){
			throw new ApplicationException(ErrorCodeEnum.ERR_87317);
		}
		// LimitGroup-Level Max limits check - END ---------------------------------------------------------------------------------------------------

		// Customer-Level AUTO-DENY limits check - START ---------------------------------------------------------------------------------------------------
		exhaustedDTO = customerDelegate.fetchExhaustedLimits(customerId, featureActionId, transactionDate, accountId);
		// TEMP STUBBING
		exhaustedDTO.setDbpErrCode(null);
		exhaustedDTO.setDbpErrMsg(null);

		if(exhaustedDTO == null || exhaustedDTO.getDbpErrCode() != null || exhaustedDTO.getDbpErrMsg() != null) {
			// TODO: improve error handling
			alert.prepareError("Error while fetching user limits").log();
			String dbpErrCode = exhaustedDTO.getDbpErrCode() == null ? ErrorCodeEnum.ERR_12511.getErrorCodeAsString() : exhaustedDTO.getDbpErrCode();
			String dbpErrMsg = exhaustedDTO.getDbpErrMsg() == null ? ErrorCodeEnum.ERR_12511.getMessage() : exhaustedDTO.getDbpErrMsg();
			limitsValidationStatus.setDbpErrCode(dbpErrCode);
			limitsValidationStatus.setDbpErrMsg(dbpErrMsg);
			return limitsValidationStatus;
		}
		Double userADMaxLimit = customerLimits.containsKey(Constants.AUTO_DENIED_TRANSACTION_LIMIT) ? customerLimits.get(Constants.AUTO_DENIED_TRANSACTION_LIMIT) : 0.0;
		Double userADDailyLimit = customerLimits.containsKey(Constants.AUTO_DENIED_DAILY_LIMIT) ? customerLimits.get(Constants.AUTO_DENIED_DAILY_LIMIT) : 0.0;
		Double userADWeeklyLimit = customerLimits.containsKey(Constants.AUTO_DENIED_WEEKLY_LIMIT) ? customerLimits.get(Constants.AUTO_DENIED_WEEKLY_LIMIT) : 0.0;

		Double newExhaustedDaily = transactionAmount + exhaustedDTO.getDailyLimit();
		Double newExhaustedWeekly = transactionAmount + exhaustedDTO.getWeeklyLimit();

		if(Double.compare(transactionAmount, userADMaxLimit) > 0){
			throw new ApplicationException(ErrorCodeEnum.ERR_12501);
		}
		if(Double.compare(newExhaustedDaily, userADDailyLimit) > 0){
			throw new ApplicationException(ErrorCodeEnum.ERR_12502);
		}
		if(Double.compare(newExhaustedWeekly, userADWeeklyLimit) > 0){
			throw new ApplicationException(ErrorCodeEnum.ERR_12503);
		}
		// Customer-Level Max limits check - END -----------------------------------------------------------------------------------------------------

		// if control reaches this point, that means all the limits validation is a success
		limitsValidationStatus.setUserExhaustedLimits(exhaustedDTO);
		limitsValidationStatus.setUserAccountLevelLimits(customerLimits);
		return limitsValidationStatus;
	}

	
	@Deprecated
	private Double getConvertedAmount(Double amount, String transactionCurrency, String baseCurrency, DataControllerRequest request ) {
		try{
			 return transactionLimitsBackendDelegate.fetchConvertedAmount(transactionCurrency, amount.toString(), request);
		}catch(Exception e) {
			alert.prepareError("Failed to fetch converted amount", e).log();
			return null;
		}
	}
	private Double getNewConvertedAmount(Double amount, String transactionCurrency, String baseCurrency, DataControllerRequest request){
		try{
			TransactionLimitsBackendDelegate transactionLimitsBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(TransactionLimitsBackendDelegate.class);
			return transactionLimitsBackendDelegate.fetchNewConvertedAmount(amount, transactionCurrency, baseCurrency, request);
		}catch(Exception e){
			return null;
		}
	}


	/**
	 * 
	 * @param featureActionID
	 * @param customerId
	 * @param accountId
	 * @param companyId
	 * @param amount
	 * @param limitTypes
	 * @param customerId 
	 * @param result
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	private TransactionStatusDTO _fetchStatusFromLimitTypes(Double amount, List<String> limitTypes, List<ApprovalMatrixDTO> approvalmatrices, List<String> actualApprovers, String customerId, LimitsDTO exhaustedDTO, TransactionStatusDTO result,DataControllerRequest request) {
		
		HashMap<String, String> groupMatrixmap = new HashMap<String,String>();
		HashMap<String, List<LimitRange>> matrices = new HashMap<String, List<LimitRange>>();
		HashMap<String, Double> exhaustedAmount = new HashMap<String, Double>();
		List<String> approvalMatrixIds = new ArrayList<String>();
		HashMap<String, Boolean> stpConfigMap = _validateSTPForLimits();
		
		if(CollectionUtils.isEmpty(approvalmatrices)) {
			if(stpConfigMap.containsValue(false)) {
				alert.prepareError("No approvalMatrix entry found for this featureactionId").log();
				result.setStatus(TransactionStatusEnum.DENIED_INVALID_APPROVAL_MATRIX);
				result.setDbpErrCode(ErrorCodeEnum.ERR_12520.getErrorCodeAsString());
				result.setDbpErrMsg(ErrorCodeEnum.ERR_12520.getMessage());
				return result;
			}else {
				result.setStatus(TransactionStatusEnum.SENT);
				return result;
			}
		}
		
		for(ApprovalMatrixDTO approvalmatrix: approvalmatrices) {
			groupMatrixmap.put(approvalmatrix.getId(), approvalmatrix.getIsGroupMatrix());
			String key = approvalmatrix.getLimitTypeId();
			List<LimitRange> limitRanges = matrices.get(key);
			
			if(limitRanges == null) {
				limitRanges = new ArrayList<LimitRange>();
				switch (key) {
					case Constants.MAX_TRANSACTION_LIMIT:
						matrices.put(Constants.MAX_TRANSACTION_LIMIT, limitRanges);
						exhaustedAmount.put(Constants.MAX_TRANSACTION_LIMIT, amount);
						break;
					case Constants.DAILY_LIMIT:
						matrices.put(Constants.DAILY_LIMIT, limitRanges);
						exhaustedAmount.put(Constants.DAILY_LIMIT, amount + exhaustedDTO.getDailyLimit());
						break;
					case Constants.WEEKLY_LIMIT:
						matrices.put(Constants.WEEKLY_LIMIT, limitRanges);
						exhaustedAmount.put(Constants.WEEKLY_LIMIT, amount + exhaustedDTO.getWeeklyLimit());
						break;
					default:
							break;
				}
			}
			limitRanges.add(new LimitRange(approvalmatrix.getId(),Double.parseDouble(approvalmatrix.getLowerlimit()),Double.parseDouble(approvalmatrix.getUpperlimit()), approvalmatrix.getApprovalruleId()));
		}
		
		Map<String,SignatoryGroupMatrixDTO> matrixmap = new HashMap<String,SignatoryGroupMatrixDTO>();
		for(String limitType: limitTypes) {
			
			List<LimitRange> limitranges = matrices.get(limitType);
			amount = exhaustedAmount.get(limitType);
			
			if(CollectionUtils.isEmpty(limitranges)) {
				if(!stpConfigMap.get(limitType)) {
					alert.prepareError("No limitranges found for featureactionId"+ limitType).log();
					result.setStatus(TransactionStatusEnum.DENIED_INVALID_APPROVAL_MATRIX);
					result.setDbpErrCode(ErrorCodeEnum.ERR_12527.getErrorCodeAsString());
					result.setDbpErrMsg(ErrorCodeEnum.ERR_12527.getMessage());
					return result;
				}else {
					alert.prepareError("Approval matrix is not set for "+ limitType +", by default the rule is NO_APPROVAL, so we need to process this transaction").log();
					approvalMatrixIds.add(Constants.NO_APPROVAL);
					continue;
				}
			}
			
			for(LimitRange limitrange:limitranges) {
				
				boolean isAlreadyAbove = false;
				boolean isAlreadyUpto = false;
				Double lowerLimit =  limitrange.getLowerLimit();
				Double upperLimit =  limitrange.getUpperLimit();
				String matrixId = limitrange.getId();
				
				if(Double.compare(-1, lowerLimit) == 0 && Double.compare(-1, upperLimit) == 0) {
					if(Constants.NO_APPROVAL.equalsIgnoreCase(limitrange.getRuleId())) {
						if(!stpConfigMap.get(limitType)) {
							alert.prepareError("Approval matrix is not configured, Please reconfigure and try again").log();
							result.setStatus(TransactionStatusEnum.DENIED_INVALID_APPROVAL_MATRIX);
							result.setDbpErrCode(ErrorCodeEnum.ERR_29033.getErrorCodeAsString());
							result.setDbpErrMsg(ErrorCodeEnum.ERR_29033.getMessage());
							return result;
						}
						else{
							alert.prepareError("Approval matrix is not updated yet, by default the rule is NO_APPROVAL, so we need to process this transaction").log();
							approvalMatrixIds.add(Constants.NO_APPROVAL);
							break;
						}
					}
					else {
						alert.prepareError("Approval matrix is not set for this contract-CoreCustomerId, account and actionId").log();
						result.setStatus(TransactionStatusEnum.DENIED_INVALID_APPROVAL_MATRIX);
						result.setDbpErrCode(ErrorCodeEnum.ERR_12521.getErrorCodeAsString());
						result.setDbpErrMsg(ErrorCodeEnum.ERR_12521.getMessage());
					}
					return result;
				}
				
				if(Double.compare(-1, lowerLimit) == 0) {
					if(! isAlreadyUpto) {
						lowerLimit = new Double(0);
						isAlreadyUpto = true;
					} else {
						alert.prepareError("Invalid approval matrix entries for amount ranges, multiple Upto ranges cannot exist").log();
						result.setStatus(TransactionStatusEnum.DENIED_INVALID_APPROVAL_MATRIX);
						result.setDbpErrCode(ErrorCodeEnum.ERR_12522.getErrorCodeAsString());
						result.setDbpErrMsg(ErrorCodeEnum.ERR_12522.getMessage());
						return result;
					}
				}
				
				if(Double.compare(-1, upperLimit) == 0) {
					if(! isAlreadyAbove) {
						upperLimit = Double.MAX_VALUE;
						isAlreadyAbove = true;
					} else {
						alert.prepareError("Invalid approval matrix entries for amount ranges, multiple Above ranges cannot exist").log();
						result.setStatus(TransactionStatusEnum.DENIED_INVALID_APPROVAL_MATRIX);
						result.setDbpErrCode(ErrorCodeEnum.ERR_12523.getErrorCodeAsString());
						result.setDbpErrMsg(ErrorCodeEnum.ERR_12523.getMessage());
						return result;
					}
				}
				
				if(Double.compare(amount, lowerLimit) > 0 && Double.compare(amount, upperLimit) <= 0) {
					
					if(Constants.NO_APPROVAL.equalsIgnoreCase(limitrange.getRuleId())) {
						approvalMatrixIds.add(Constants.NO_APPROVAL);
					}
					else {
						
						List<String> approverIds = new ArrayList<>();
						
						if(groupMatrixmap.get(matrixId).equalsIgnoreCase(Constants.FALSE)) {
							approverIds = approvalMatrixBusinessDelegate.fetchApproverIds(matrixId);
							if(approverIds == null || approverIds.size() ==0)
								approverIds = approvalMatrixBusinessDelegate.fetchApproverIdsFromTemplate(matrixId,request) ;
						}else {
							SignatoryGroupMatrixDTO signatoryGroupMatrixDTO = approvalMatrixBusinessDelegate.fetchSignatoryGroupMatrix(matrixId);
							if(signatoryGroupMatrixDTO == null)
								signatoryGroupMatrixDTO = approvalMatrixBusinessDelegate.fetchSignatoryGroupMatrixFromTemplate(matrixId);
																										
							matrixmap.put(signatoryGroupMatrixDTO.getApprovalMatrixId(), signatoryGroupMatrixDTO);
							approverIds = approvalMatrixBusinessDelegate.fetchUserOfGroupList(signatoryGroupMatrixDTO.getGroupList());
							approvalMatrixIds.add(matrixId);
							
							ApplicationDTO applicationDTO = applicationBusinessDelegate.properties();
							if(approverIds != null && applicationDTO != null && applicationDTO.isSelfApprovalEnabled() && approverIds.contains(customerId)) {
		                        result.setSelfApproved(true);
							}
							break;
						}
						
						if(approverIds == null) {
							alert.prepareError("Failed to fetch approverIds for the respective matrix entry").log();
							result.setStatus(TransactionStatusEnum.DENIED_INVALID_APPROVAL_MATRIX);
							result.setDbpErrCode(ErrorCodeEnum.ERR_12524.getErrorCodeAsString());
							result.setDbpErrMsg(ErrorCodeEnum.ERR_12524.getMessage());
							return result;
						}
						
						if(actualApprovers == null) {
							alert.prepareError("Failed to fetch approverIds for the respective matrix entry").log();
							result.setStatus(TransactionStatusEnum.DENIED_INVALID_APPROVAL_MATRIX);
							result.setDbpErrCode(ErrorCodeEnum.ERR_12524.getErrorCodeAsString());
							result.setDbpErrMsg(ErrorCodeEnum.ERR_12524.getMessage());
							return result;
						}
						
						//are approversId still valid
						Set<String> validApprovers = actualApprovers.stream().distinct().filter(approverIds::contains).collect(Collectors.toSet());
						
						if(validApprovers.size() != approverIds.size()) {
							alert.prepareError("Fetched approverIds are not valid in the current approval matrix").log();
							result.setStatus(TransactionStatusEnum.DENIED_INVALID_APPROVAL_MATRIX);
							result.setDbpErrCode(ErrorCodeEnum.ERR_12525.getErrorCodeAsString());
							result.setDbpErrMsg(ErrorCodeEnum.ERR_12525.getMessage());
							return result;
						}
						
						approvalMatrixIds.add(matrixId);
						
						ApplicationDTO applicationDTO = applicationBusinessDelegate.properties();
						if(applicationDTO != null && applicationDTO.isSelfApprovalEnabled() && approverIds.contains(customerId)) {
	                        result.setSelfApproved(true);
						}
						else {
			            	alert.prepareError("Error while fetching Application record").log();
			            }
					}
					
					break;
				}
			}
			
		}

		if(approvalMatrixIds.size() == limitTypes.size()) {
			approvalMatrixIds.removeAll(Arrays.asList(Constants.NO_APPROVAL));
			
			if(approvalMatrixIds.size() > 0) {
				result.setStatus(TransactionStatusEnum.PENDING);
				Map<String, SignatoryGroupMatrixDTO> sigApprovalmatrices = getApprovalMatrices(approvalMatrixIds,matrixmap);
				result.setSignatoryGroupMatrices(sigApprovalmatrices);
				result.setApprovalMatrixIds(approvalMatrixIds);
			}
			else {
				result.setStatus(TransactionStatusEnum.SENT);
			}
			return result;
		}
		else {
			alert.prepareError("No approvalMatrix ids found for this ammount").log();
			result.setStatus(TransactionStatusEnum.DENIED_INVALID_APPROVAL_MATRIX);
			result.setDbpErrCode(ErrorCodeEnum.ERR_12526.getErrorCodeAsString());
			result.setDbpErrMsg(ErrorCodeEnum.ERR_12526.getMessage());
			return result;
		}
		 	
	}
	
	private Map<String, SignatoryGroupMatrixDTO>  getApprovalMatrices(List<String> approvalMatrixIds,	Map<String,SignatoryGroupMatrixDTO> matrixmap) {
		
		Map<String,SignatoryGroupMatrixDTO> sigApprovalmatrices= new HashMap<String, SignatoryGroupMatrixDTO>();

		approvalMatrixIds.forEach((matrixId)->{
			
			if(matrixmap.containsKey(matrixId)) {
				matrixmap.get(matrixId).setGroupMatrix(true);
				sigApprovalmatrices.put(matrixId, matrixmap.get(matrixId));
			}else {
				SignatoryGroupMatrixDTO matrixDto = new SignatoryGroupMatrixDTO();
				matrixDto.setGroupMatrix(false);
				sigApprovalmatrices.put(matrixId, matrixDto);
			}
		});
		
		return sigApprovalmatrices;
	}
	
	private class LimitRange{
		
		private double lowerLimit;
		private double upperLimit;
		private String ruleId;
		private String id;
		
		LimitRange(String id, double lowerLimit, double upperLimit, String ruleId) {
			this.lowerLimit = lowerLimit;
			this.upperLimit = upperLimit;
			this.id = id;
			this.ruleId = ruleId;
		}

		public double getLowerLimit() {
			return lowerLimit;
		}

		public double getUpperLimit() {
			return upperLimit;
		}

		public String getId() {
			return id;
		}
		
		public String getRuleId() {
			return ruleId;
		}

	}
	
	@Override
	public TransactionStatusDTO validateOffsetLimitsForACHFile(ACHFileDTO achfileDTOValidate, String userId, String companyId
			, Map<String, Double> offsetDetails, TransactionStatusEnum fileStatus, DataControllerRequest request) {
	 
		TransactionStatusDTO status = new TransactionStatusDTO();
		 String featureActionId = FeatureAction.ACH_FILE_UPLOAD;
		 List<String> matrixIds = new ArrayList<String>();
		 Map<String, SignatoryGroupMatrixDTO> sigGrpMatrices = new HashMap<String, SignatoryGroupMatrixDTO>();
		 List<String> approvalAccounts = new ArrayList<String>();
		 
		 boolean isStatusPending = false;
		 
		 for (String key : offsetDetails.keySet()) {
			 String debitAccount = key.split("_")[0];
			 String date = key.split("_")[1];
			 Double totalAmount = offsetDetails.get(key);			 

			 if(totalAmount == 0.0) {
				 continue;
			 }
			 
			 TransactionStatusDTO transactionStatusDTO = validateForLimits(userId, companyId,
					 debitAccount, featureActionId, totalAmount, fileStatus, date, null, null, request);
			 TransactionStatusEnum transactionStatus = transactionStatusDTO.getStatus();

			 try {
				 if(status.getAmount() == null) {
					 status.setAmount(0.0);
				 }
				 if(status.getServiceCharge() == null) {
					 status.setServiceCharge("0.0");
				 }
				 if(status.getTransactionAmount() == null) {
					 status.setTransactionAmount("0.0");
				 }
				 status.setAmount(
							Double.sum(status.getAmount(), transactionStatusDTO.getAmount())
						 );
				 status.setServiceCharge( 
						 Double.sum(
						 Double.parseDouble(status.getServiceCharge()), 
						 Double.parseDouble(transactionStatusDTO.getServiceCharge())
						 )+ "");
				 status.setTransactionAmount(
						 Double.sum(
						 Double.parseDouble(status.getTransactionAmount()), 
						 Double.parseDouble(transactionStatusDTO.getTransactionAmount())
						 )+ "");
			}
			 catch(Exception e) {
				 alert.prepareError("Error while converting amount", e).log();
			 }

			 if (transactionStatus.equals(TransactionStatusEnum.PENDING)) {
				 isStatusPending = true;
				 matrixIds.addAll(transactionStatusDTO.getApprovalMatrixIds());
				 sigGrpMatrices.putAll(transactionStatusDTO.getSignatoryGroupMatrices());
				 approvalAccounts.add(debitAccount);
				 if(transactionStatusDTO.isSelfApproved()){
					 status.setSelfApproved(transactionStatusDTO.isSelfApproved());
				 }
			 } else if (transactionStatus.equals(TransactionStatusEnum.DENIED_AD_MAX_TRANSACTION)
					 || transactionStatus.equals(TransactionStatusEnum.DENIED_AD_DAILY)
					 || transactionStatus.equals(TransactionStatusEnum.DENIED_AD_WEEKLY)
					 || transactionStatus.equals(TransactionStatusEnum.DENIED_MAX_TRANSACTION)
					 || transactionStatus.equals(TransactionStatusEnum.DENIED_MIN_TRANSACTION)
					 || transactionStatus.equals(TransactionStatusEnum.DENIED_DAILY)
					 || transactionStatus.equals(TransactionStatusEnum.DENIED_WEEKLY)
					 || transactionStatus.equals(TransactionStatusEnum.DENIED_INVALID_APPROVAL_MATRIX)) {
				 alert.prepareError("This transaction is denied").log();
				 status.setStatus(transactionStatus);
				 status.setDbpErrCode(transactionStatusDTO.getDbpErrCode());
				 status.setDbpErrMsg(transactionStatusDTO.getDbpErrMsg());
				 return status;
			 }
		 }
		 
		 if(isStatusPending) {
			 status.setStatus(TransactionStatusEnum.PENDING);
			 status.setApprovalMatrixIds(matrixIds.stream().distinct().collect(Collectors.toList()));
			 status.setSignatoryGroupMatrices(sigGrpMatrices);
			 //achfileDTOValidate.setApprovalAccounts(String.join(",", approvalAccounts));
			 status.setAccountId(String.join(",", approvalAccounts));
		 } else {
			 status.setStatus(TransactionStatusEnum.SENT);
		 }
		 
		 return status;
	}
	
	@Override
	public TransactionStatusDTO validateForLimitsForNonMonetaryActions(TransactionStatusDTO inputTransactionStatusDTO,DataControllerRequest request) {
	
		String userId = inputTransactionStatusDTO.getCustomerId();
		String accountId = inputTransactionStatusDTO.getAccountId(); 
		String featureActionID = inputTransactionStatusDTO.getFeatureActionID(); 
		TransactionStatusDTO result = new TransactionStatusDTO();
		ApproversBusinessDelegate approversBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApproversBusinessDelegate.class);
		AccountBusinessDelegate accountBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AccountBusinessDelegate.class);
		String contractId;
		String coreCustomerId;
		
		Set<String> signatoryGroupFeatureActionIds = new HashSet<>(Arrays.asList(FeatureAction.SIGNATORY_GROUP_CREATE_EDIT,FeatureAction.SIGNATORY_GROUP_DELETE));
		Set<String> approvalMatrixFeatureActionIds = new HashSet<>(Arrays.asList(FeatureAction.APPROVAL_MATRIX_MANAGE));
		Set<String> combinedNewActionsIds = new HashSet<String>();
		combinedNewActionsIds.addAll(signatoryGroupFeatureActionIds);
		combinedNewActionsIds.addAll(approvalMatrixFeatureActionIds);
		
		if (combinedNewActionsIds.contains(featureActionID)) {
			contractId =inputTransactionStatusDTO.getContractId();
			coreCustomerId =inputTransactionStatusDTO.getCoreCustomerId();
		}else {
			CustomerAccountsDTO account = accountBusinessDelegate.getAccountDetails(userId, accountId);
			 contractId = account.getContractId();
			 coreCustomerId = account.getCoreCustomerId();
		}
			
		List<ApprovalMatrixStatusDTO> status = approvalMatrixBusinessDelegate.fetchApprovalMatrixStatus(contractId, Arrays.asList(coreCustomerId));
		if(status!= null && status.size() > 0 && status.get(0).getIsDisabled()) {
			result.setStatus(TransactionStatusEnum.SENT);
			return result;
		}
		
		boolean isTemplateDerived = false;
		List<ApprovalMatrixDTO> approvalmatrices = approvalMatrixBusinessDelegate.fetchApprovalMatrix(contractId, coreCustomerId, accountId, featureActionID, "");
		if (approvalmatrices == null || approvalmatrices.size() == 0) {
			isTemplateDerived = true;
			approvalmatrices = approvalMatrixBusinessDelegate.fetchApprovalMatrixTemplate(contractId, coreCustomerId, featureActionID, Constants.NON_MONETARY_LIMIT);
		}
		List<String> validApprovers = approversBusinessDelegate.getAccountActionApproverList(contractId, coreCustomerId, accountId, featureActionID);
		return _fetchStatusFromLimitTypesForNonMonetaryActions(approvalmatrices, validApprovers, userId, result, isTemplateDerived,inputTransactionStatusDTO,request); // Modified as part of ADP-2810
	}
	
	private TransactionStatusDTO _fetchStatusFromLimitTypesForNonMonetaryActions(List<ApprovalMatrixDTO> approvalmatrices, List<String> actualApprovers, String customerId, TransactionStatusDTO result, boolean isTemplateDerived , TransactionStatusDTO inputTransactionStatusDTO,DataControllerRequest request) {
		
		HashMap<String, String> groupMatrixmap = new HashMap<String,String>();
		List<String> approvalMatrixIds = new ArrayList<String>();
		
		approvalmatrices.forEach((entry)->{
			groupMatrixmap.put(entry.getId(), entry.getIsGroupMatrix());
		});
		
		if(approvalmatrices.size() <= 0) {
			alert.prepareError("No approvalMatrix entry found for this featureactionId").log();
			result.setStatus(TransactionStatusEnum.DENIED_INVALID_APPROVAL_MATRIX);
			result.setDbpErrCode(ErrorCodeEnum.ERR_12520.getErrorCodeAsString());
			result.setDbpErrMsg(ErrorCodeEnum.ERR_12520.getMessage());
			return result;
		}
		
		List<LimitRange> limitranges = new ArrayList<LimitRange>();
		for(ApprovalMatrixDTO approvalmatrix: approvalmatrices) {
			if(Constants.NON_MONETARY_LIMIT.equals(approvalmatrix.getLimitTypeId())) {
				limitranges.add(new LimitRange(approvalmatrix.getId(),Double.parseDouble(approvalmatrix.getLowerlimit()),Double.parseDouble(approvalmatrix.getUpperlimit()), approvalmatrix.getApprovalruleId()));
			}
		}
		
		if(limitranges.size() <= 0) {
			alert.prepareError("No limitranges found for given non monetary featureactionId").log();
			result.setStatus(TransactionStatusEnum.DENIED_INVALID_APPROVAL_MATRIX);
			result.setDbpErrCode(ErrorCodeEnum.ERR_12527.getErrorCodeAsString());
			result.setDbpErrMsg(ErrorCodeEnum.ERR_12527.getMessage());
			return result;
		}
		
		Map<String,SignatoryGroupMatrixDTO> matrixmap = new HashMap<String,SignatoryGroupMatrixDTO>();
		for(LimitRange limitrange:limitranges) {
			Double lowerLimit =  limitrange.getLowerLimit();
			Double upperLimit =  limitrange.getUpperLimit();
			String matrixId = limitrange.getId();
			
			HashMap<String, Boolean> stpConfigMap = _validateSTPForLimits();
 			
			if(Double.compare(-1, lowerLimit) == 0 && Double.compare(-1, upperLimit) == 0) {
				if(Constants.NO_APPROVAL.equalsIgnoreCase(limitrange.getRuleId())) {
					if(!stpConfigMap.get(Constants.AM_NON_MONETORY_NO_RULES_ALLOW_STP)) {
						alert.prepareError("Approval Matrix is not configured, Please configure and try again.").log();
						result.setStatus(TransactionStatusEnum.DENIED_INVALID_APPROVAL_MATRIX);
						result.setDbpErrCode(ErrorCodeEnum.ERR_29033.getErrorCodeAsString());
						result.setDbpErrMsg(ErrorCodeEnum.ERR_29033.getMessage());
						return result;
					}
					alert.prepareError("Approval matrix is not updated yet, by default the rule is NO_APPROVAL, so we need to process this transaction").log();
					approvalMatrixIds.add(Constants.NO_APPROVAL);
				}
				else {
					
					List<String> approverIds = new ArrayList<>();
					
					if(groupMatrixmap.get(matrixId).equalsIgnoreCase(Constants.FALSE)) {
						approverIds= ((isTemplateDerived) ? approvalMatrixBusinessDelegate.fetchApproverIdsFromTemplate(matrixId,request)
						: approvalMatrixBusinessDelegate.fetchApproverIds(matrixId));

						
					}else {
						SignatoryGroupMatrixDTO signatoryGroupMatrixDTO = ((isTemplateDerived) ? approvalMatrixBusinessDelegate.fetchSignatoryGroupMatrixFromTemplate(matrixId)
								: approvalMatrixBusinessDelegate.fetchSignatoryGroupMatrix(matrixId));
						matrixmap.put(signatoryGroupMatrixDTO.getApprovalMatrixId(), signatoryGroupMatrixDTO);
						approverIds = approvalMatrixBusinessDelegate.fetchUserOfGroupList(signatoryGroupMatrixDTO.getGroupList());
					
						if (!approvalMatrixIds.contains(matrixId)) {
							approvalMatrixIds.add(matrixId);
						}
						
						ApplicationDTO applicationDTO = applicationBusinessDelegate.properties();
						if(approverIds != null && applicationDTO != null && applicationDTO.isSelfApprovalEnabled() && approverIds.contains(customerId)) {
	                        result.setSelfApproved(true);
						}
						
						break;
					}
					
					if(approverIds == null) {
						alert.prepareError("Failed to fetch approverIds for the respective matrix entry").log();
						result.setStatus(TransactionStatusEnum.DENIED_INVALID_APPROVAL_MATRIX);
						result.setDbpErrCode(ErrorCodeEnum.ERR_12524.getErrorCodeAsString());
						result.setDbpErrMsg(ErrorCodeEnum.ERR_12524.getMessage());
						return result;
					}
					
					if(actualApprovers == null) {
						alert.prepareError("Failed to fetch approverIds for the respective matrix entry").log();
						result.setStatus(TransactionStatusEnum.DENIED_INVALID_APPROVAL_MATRIX);
						result.setDbpErrCode(ErrorCodeEnum.ERR_12524.getErrorCodeAsString());
						result.setDbpErrMsg(ErrorCodeEnum.ERR_12524.getMessage());
						return result;
					}
					
					//are approversId still valid
					Set<String> validApprovers = actualApprovers.stream().distinct().filter(approverIds::contains).collect(Collectors.toSet());
					
					if(validApprovers.size() != approverIds.size()) {
						alert.prepareError("Fetched approverIds are not valid in the current approval matrix").log();
						result.setStatus(TransactionStatusEnum.DENIED_INVALID_APPROVAL_MATRIX);
						result.setDbpErrCode(ErrorCodeEnum.ERR_12525.getErrorCodeAsString());
						result.setDbpErrMsg(ErrorCodeEnum.ERR_12525.getMessage());
						return result;
					}
					
					if (!approvalMatrixIds.contains(matrixId)) {
						approvalMatrixIds.add(matrixId);
					}
					
					ApplicationDTO applicationDTO = applicationBusinessDelegate.properties();
					if(applicationDTO != null && applicationDTO.isSelfApprovalEnabled() && approverIds.contains(customerId)) {
                        result.setSelfApproved(true);
					}
					else {
		            	alert.prepareError("Error while fetching Application record").log();
		            }
				}
			}
			else {
				alert.prepareError("Approval matrix is not set for this contract-CoreCustomerId, account and actionId").log();
				result.setStatus(TransactionStatusEnum.DENIED_INVALID_APPROVAL_MATRIX);
				result.setDbpErrCode(ErrorCodeEnum.ERR_12521.getErrorCodeAsString());
				result.setDbpErrMsg(ErrorCodeEnum.ERR_12521.getMessage());
				return result;
			}
		}
			
		if(approvalMatrixIds.size() == 1) {
			approvalMatrixIds.removeAll(Arrays.asList(Constants.NO_APPROVAL));
			
			if(approvalMatrixIds.size() > 0) {
				result.setStatus(TransactionStatusEnum.PENDING);
				Map<String, SignatoryGroupMatrixDTO> sigmatrices = getApprovalMatrices(approvalMatrixIds,matrixmap);
				result.setSignatoryGroupMatrices(sigmatrices);
				result.setApprovalMatrixIds(approvalMatrixIds);
			}
			else {
				result.setStatus(TransactionStatusEnum.SENT);
			}
			return result;
		}
		else {
			alert.prepareError("No matching Approval matrix rule found for this request").log();
			result.setStatus(TransactionStatusEnum.DENIED_INVALID_APPROVAL_MATRIX);
			result.setDbpErrCode(ErrorCodeEnum.ERR_12526.getErrorCodeAsString());
			result.setDbpErrMsg(ErrorCodeEnum.ERR_12526.getMessage());
			return result;
		}
		 	
	}

	@Deprecated
	private HashMap<String, Boolean> _validateSTPForLimits() {
		HashMap<String, Boolean> stpConfigMap = new HashMap<>();
		String configParamValue = null;		
		configParamValue = EnvironmentConfigurationsHandler.getValue(Constants.AM_MAX_LIMIT_NO_RULES_ALLOW_STP);
		if("false".equalsIgnoreCase(configParamValue)) {
			stpConfigMap.put(Constants.MAX_TRANSACTION_LIMIT, false);
		}
		else {
			stpConfigMap.put(Constants.MAX_TRANSACTION_LIMIT, true);
		}
		configParamValue = EnvironmentConfigurationsHandler.getValue(Constants.AM_DAILY_LIMIT_NO_RULES_ALLOW_STP);
		if("false".equalsIgnoreCase(configParamValue)) {
			stpConfigMap.put(Constants.DAILY_LIMIT, false);
		}
		else {
			stpConfigMap.put(Constants.DAILY_LIMIT, true);
		}
		configParamValue = EnvironmentConfigurationsHandler.getValue(Constants.AM_WEEKLY_LIMIT_NO_RULES_ALLOW_STP);
		if("false".equalsIgnoreCase(configParamValue)) {
			stpConfigMap.put(Constants.WEEKLY_LIMIT, false);
		}
		else {
			stpConfigMap.put(Constants.WEEKLY_LIMIT, true);
		}
		configParamValue = EnvironmentConfigurationsHandler.getValue(Constants.AM_NON_MONETORY_NO_RULES_ALLOW_STP);
		if("false".equalsIgnoreCase(configParamValue)) {
			stpConfigMap.put(Constants.AM_NON_MONETORY_NO_RULES_ALLOW_STP, false);
		}
		else {
			stpConfigMap.put(Constants.AM_NON_MONETORY_NO_RULES_ALLOW_STP, true);
		}
		return stpConfigMap;
	}
	
}