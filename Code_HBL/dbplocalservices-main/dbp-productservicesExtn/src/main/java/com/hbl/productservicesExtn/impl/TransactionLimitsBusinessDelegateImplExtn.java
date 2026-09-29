package com.hbl.productservicesExtn.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.supercsv.cellprocessor.ParseDouble;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.transactionslimitengine.utils.TransactionsLimitConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.hbl.productservicesExtn.constants.HBLConstants;
import com.hbl.productservicesExtn.constants.HBLEnums;
import com.hbl.productservicesExtn.dto.CumulativeTransactionAmount;
import com.hbl.productservicesExtn.dto.LimitsDTOExtn;
import com.hbl.productservicesExtn.dto.UserLimitsDTOExtn;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbputilities.util.BundleConfigurationHandler;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
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
import com.temenos.dbx.product.commons.businessdelegate.api.UserRoleBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.impl.TransactionLimitsBusinessDelegateImpl;
import com.temenos.dbx.product.commons.dto.ApplicationDTO;
import com.temenos.dbx.product.commons.dto.CustomerAccountsDTO;
import com.temenos.dbx.product.commons.dto.TransactionStatusDTO;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.TransactionStatusEnum;
import com.temenos.dbx.product.forexservices.resource.api.ForexResource;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;

public class TransactionLimitsBusinessDelegateImplExtn extends TransactionLimitsBusinessDelegateImpl{
	private static final Logger LOG = LogManager.getLogger(TransactionLimitsBusinessDelegateImplExtn.class);
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
	UserRoleBusinessDelegateImplExtn userRoleBusinessDelegateExtn = new UserRoleBusinessDelegateImplExtn();
	ContractBusinessDelegateImplExtn contractDelegateExtn = new ContractBusinessDelegateImplExtn();
	LimitGroupBusinessDelegateImplExtn limitGroupBusinessDelegateExtn = new LimitGroupBusinessDelegateImplExtn();
	CustomerBusinessDelegateImplExtn customerDelegateExtn = new CustomerBusinessDelegateImplExtn();
	public static String channel =HBLConstants.ONLINE_BANKING;
	@Override
	public TransactionStatusDTO validateForLimits(String userId, String companyId, String accountId, String featureActionID, Double amount,
			TransactionStatusEnum transactionStatus, String date, String transactionCurrency, String serviceCharge, DataControllerRequest request)  {
	
		TransactionStatusDTO result = new TransactionStatusDTO();
		List<String> limitTypes = new ArrayList<>();
		channel = HBLConstants.ONLINE_BANKING;
		CustomerAccountsDTO account = accountBusinessDelegate.getAccountDetails(userId, accountId);
		String contractId = account.getContractId();
		String coreCustomerId = account.getCoreCustomerId();

		String legalEntityId = null;
		try {
			legalEntityId = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(request);
		}catch(Exception e){
			LOG.error(e);
		}
		
		//String baseCurrency = application.getBaseCurrencyFromCache();
		String baseCurrency = approvalMatrixBusinessDelegate.fetchUserApprovalCurrency(coreCustomerId,contractId,accountId);
		if(StringUtils.isEmpty(baseCurrency))
			baseCurrency = EnvironmentConfigurationsHandler.getServerProperty("HBL_BASE_CURRENCY");
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
		LOG.debug("HBL::TransactionLimitsBusinessDelegateImplExtn::transactionCurrency"+transactionCurrency+":baseCurrency:"+baseCurrency);
		if (!transactionCurrency.equalsIgnoreCase(baseCurrency)) {
			try {
				//convertedAmount = getNewConvertedAmount(amount, transactionCurrency, baseCurrency, request);
				//convertedAmount = (double) Math.round(convertedAmount * 100.0)/100.0;
				convertedAmount=getConvertedAmount(amount, transactionCurrency, baseCurrency, request);
				
				/*
				 * commented "charges", as it will be taken care of in next release,
				 *  when totaldebitAmount value is considered.
				 */
				//charges = Double.parseDouble(serviceCharge);
				LOG.debug("HBL::TransactionLimitsBusinessDelegateImplExtn::convertedAmount:"+convertedAmount);
				totalAmount = convertedAmount; //+ charges;
			}catch (Exception e) {
				LOG.error("Failed to fetch converted amount", e);
				result.setDbpErrCode(ErrorCodeEnum.ERR_27016.getErrorCodeAsString());
				result.setDbpErrMsg(ErrorCodeEnum.ERR_27016.getMessage());
				return result;
			}
		} else {
			totalAmount = amount; //+ charges;
		}

		result.setAmount(totalAmount);
		amount = totalAmount;
		
		//Validating contract-coreCustomer level limits for users
		LOG.debug("HBL::TransactionLimitsBusinessDelegateImplExtn::contractId"+contractId+":coreCustomerId:"+coreCustomerId);
		if(StringUtils.isNotEmpty(contractId) && StringUtils.isNotEmpty(coreCustomerId)) {
		return validateGlobalLimits(amount, contractId, coreCustomerId, date, channel, result, request);
		}else {
		result.setDbpErrCode( ErrorCodeEnum.ERR_12511.getErrorCodeAsString());
		result.setDbpErrMsg(ErrorCodeEnum.ERR_12511.getMessage());
		}
		return result;
	}
		/*if(StringUtils.isNotEmpty(contractId) && StringUtils.isNotEmpty(coreCustomerId)) {
			ContractBusinessDelegateImplExtn newContractDelegate= new ContractBusinessDelegateImplExtn();
			LimitsDTOExtn contractCutsomerLimitsDTO = newContractDelegate.fetchAllLimits(contractId, coreCustomerId, featureActionID, legalEntityId);
			LimitsDTOExtn exhaustedDTO = contractDelegateExtn.fetchExhaustedLimits(contractId, coreCustomerId, featureActionID, date, channel);
			LOG.debug("HBL::TransactionLimitsBusinessDelegateImplExtn::contractCutsomerLimitsDTO"+contractCutsomerLimitsDTO.toString());
			LOG.debug("HBL::TransactionLimitsBusinessDelegateImplExtn::exhaustedDTO(Limits used):"+exhaustedDTO.toString());
			if(contractCutsomerLimitsDTO == null 
					|| exhaustedDTO == null 
					|| exhaustedDTO.getDbpErrCode() != null 
					|| exhaustedDTO.getDbpErrMsg() != null) {
				
				LOG.error("Failed to fetch organization limits");
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
			Double newMonthlyValue = amount + exhaustedDTO.getMonthlyLimit();
			LOG.debug("HBL::TransactionLimitsBusinessDelegateImplExtn::enteredAmount"+amount);
			LOG.debug("HBL::TransactionLimitsBusinessDelegateImplExtn::minAmountPerTrLimit"+minAmountPerTrLimit);
			LOG.debug("HBL::TransactionLimitsBusinessDelegateImplExtn::exhaustedDTO:getDailylimit"+exhaustedDTO.getDailyLimit());
			LOG.debug("HBL::TransactionLimitsBusinessDelegateImplExtn::exhaustedDTO:getWeeklyLimit"+exhaustedDTO.getWeeklyLimit());
			LOG.debug("HBL::TransactionLimitsBusinessDelegateImplExtn::exhaustedDTO:getMonthlyLimit"+exhaustedDTO.getMonthlyLimit());
			LOG.debug("HBL::TransactionLimitsBusinessDelegateImplExtn::contractCutsomerLimitsDTO:ContractAction:getMaxTransactionLimit"+contractCutsomerLimitsDTO.getMaxTransactionLimit());
			LOG.debug("HBL::TransactionLimitsBusinessDelegateImplExtn::contractCutsomerLimitsDTO:ContractAction:getDailyLimit"+contractCutsomerLimitsDTO.getDailyLimit());
			LOG.debug("HBL::TransactionLimitsBusinessDelegateImplExtn::contractCutsomerLimitsDTO:ContractAction:getWeeklyLimit"+contractCutsomerLimitsDTO.getWeeklyLimit());
			LOG.debug("HBL::TransactionLimitsBusinessDelegateImplExtn::contractCutsomerLimitsDTO:ContractAction:getMonthlyLimit"+contractCutsomerLimitsDTO.getMonthlyLimit());
			Double perTrLimit = contractCutsomerLimitsDTO.getMaxTransactionLimit();
			Double dailyLimit = contractCutsomerLimitsDTO.getDailyLimit();
			Double weeklyLimit = contractCutsomerLimitsDTO.getWeeklyLimit();
			Double monthlyLimit = contractCutsomerLimitsDTO.getMonthlyLimit();
			
			if(Double.compare(amount, minAmountPerTrLimit) >= 0) {
			if(Double.compare(amount, perTrLimit) <= 0) {
				if(Double.compare(newDailyValue, dailyLimit) <= 0 ) {
					if(Double.compare(newMonthlyValue, monthlyLimit) <= 0) {
						// all company level limits are satisfied
						LOG.debug("HBL::TransactionLimitsBusinessDelegateImpl::ContractAction::validation success:");
					}
					else {
						//result.setStatus(TransactionStatusEnum.DENIED_MONTHLY);
						result.setDbpErrCode(HBLEnums.ERR_12506.getErrorCodeAsString());
						result.setDbpErrMsg(HBLEnums.ERR_12506.getErrorMsg());
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
				LOG.error("Error while fetching user Role");
				String dbpErrCode = ErrorCodeEnum.ERR_12509.getErrorCodeAsString();
				String dbpErrMsg =  ErrorCodeEnum.ERR_12509.getMessage();
				result.setDbpErrCode(dbpErrCode);
				result.setDbpErrMsg(dbpErrMsg);
				return result;
			}
			
			LimitsDTOExtn roleLimitsDTO = userRoleBusinessDelegateExtn.fetchLimits(userRole, featureActionID);
			LOG.debug("HBL::UserRoleBusinessDelegateImplExtn::GroupActionsLimits: roleLimitsDTO:"+ roleLimitsDTO.toString());
			exhaustedDTO = userRoleBusinessDelegateExtn.fetchExhaustedLimits(contractId, coreCustomerId, userRole, featureActionID, date, userId, channel);
			
			if(roleLimitsDTO == null 
					|| exhaustedDTO == null 
					|| exhaustedDTO.getDbpErrCode() != null 
					|| exhaustedDTO.getDbpErrMsg() != null ) {
				
				LOG.error("Error while fetching role limits");
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
			newMonthlyValue = amount + exhaustedDTO.getMonthlyLimit();
			LOG.debug("HBL::TransactionLimitsBusinessDelegateImpl::Group Actions Limits:enterdAmount"+amount);
			LOG.debug("HBL::TransactionLimitsBusinessDelegateImpl:::Group Actions Limits:exhausted:getDailyLimit"+exhaustedDTO.getDailyLimit());
			LOG.debug("HBL::TransactionLimitsBusinessDelegateImpl:::Group Actions Limits:exhausted:getWeeklyLimit"+exhaustedDTO.getWeeklyLimit());
			LOG.debug("HBL::TransactionLimitsBusinessDelegateImpl:::Group Actions Limits:exhausted:getMonthlyLimit"+exhaustedDTO.getMonthlyLimit());
			
			
			perTrLimit = roleLimitsDTO.getMaxTransactionLimit();
			dailyLimit = roleLimitsDTO.getDailyLimit();
			weeklyLimit = roleLimitsDTO.getWeeklyLimit();
			monthlyLimit = roleLimitsDTO.getMonthlyLimit();
			LOG.debug("HBL::TransactionLimitsBusinessDelegateImpl::contractCutsomerLimitsDTO:Group Actions Limits:perTrLimit:"+perTrLimit);
			LOG.debug("HBL::TransactionLimitsBusinessDelegateImpl::contractCutsomerLimitsDTO:Group Actions Limits:dailyLimit:"+dailyLimit);
			LOG.debug("HBL::TransactionLimitsBusinessDelegateImpl::contractCutsomerLimitsDTO:Group Actions Limits:weeklyLimit:"+weeklyLimit);
			LOG.debug("HBL::TransactionLimitsBusinessDelegateImpl::contractCutsomerLimitsDTO:Group Actions Limits:monthlyLimit:"+monthlyLimit);
			
			if(Double.compare(amount, perTrLimit) <= 0) {
				if(Double.compare(newDailyValue, dailyLimit) <= 0 ) {
					if(Double.compare(newMonthlyValue, monthlyLimit) <= 0) {
						// all role level limits are satisfied
						LOG.debug("HBL::TransactionLimitsBusinessDelegateImpl::GroupActionsLimits::validation success:");
					}
					else {
						//result.setStatus(TransactionStatusEnum.DENIED_MONTHLY);
						result.setDbpErrCode(HBLEnums.ERR_12506.getErrorCodeAsString());
						result.setDbpErrMsg(HBLEnums.ERR_12506.getErrorMsg());
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
		
			//Validating Limit Group limits for both retail and business users
		String limitGroupId = featureActionDelegate.getLimitGroupId(featureActionID,legalEntityId);
		LOG.debug("HBL::TransactionLimitsBusinessDelegateImpl::getLimitGroupId::featureActionID::"+featureActionID+":limitGroupId:"+limitGroupId);
		if(limitGroupId == null) {
			LOG.error("Error while fetching the limit group id");
			String dbpErrCode = ErrorCodeEnum.ERR_12519.getErrorCodeAsString();
			String dbpErrMsg =  ErrorCodeEnum.ERR_12519.getMessage();
			result.setDbpErrCode(dbpErrCode);
			result.setDbpErrMsg(dbpErrMsg);
			return result;
		}

		LimitsDTOExtn groupLimitsDTO = limitGroupBusinessDelegateExtn.fetchLimits(userId, contractId, coreCustomerId, limitGroupId);
		LimitsDTOExtn exhaustedgroupLimitsDTO = limitGroupBusinessDelegateExtn.fetchExhaustedLimits(contractId, coreCustomerId, userId, limitGroupId, date,channel);
		if(groupLimitsDTO == null || exhaustedgroupLimitsDTO == null || exhaustedgroupLimitsDTO.getDbpErrCode() != null || exhaustedgroupLimitsDTO.getDbpErrMsg() != null) {
			LOG.error("Error while fetching the limits for limit groups");
			String dbpErrCode = exhaustedgroupLimitsDTO.getDbpErrCode() == null ? ErrorCodeEnum.ERR_12518.getErrorCodeAsString() : exhaustedgroupLimitsDTO.getDbpErrCode();
			String dbpErrMsg = exhaustedgroupLimitsDTO.getDbpErrMsg() == null ? ErrorCodeEnum.ERR_12518.getMessage() : exhaustedgroupLimitsDTO.getDbpErrMsg();
			result.setDbpErrCode(dbpErrCode);
			result.setDbpErrMsg(dbpErrMsg);
			return result;
		}
		LOG.debug("HBL::TransactionLimitsBusinessDelegateImpl::customergrouplimits:enterdAmount"+amount);
		LOG.debug("HBL::TransactionLimitsBusinessDelegateImpl:::customergrouplimits:exhausted:getDailyLimit"+exhaustedgroupLimitsDTO.getDailyLimit());
		LOG.debug("HBL::TransactionLimitsBusinessDelegateImpl:::customergrouplimits:exhausted:getWeeklyLimit"+exhaustedgroupLimitsDTO.getWeeklyLimit());
		LOG.debug("HBL::TransactionLimitsBusinessDelegateImpl:::customergrouplimits:exhausted:getMonthlyLimit"+exhaustedgroupLimitsDTO.getMonthlyLimit());

		Double newLimitGroupDailyValue = amount + exhaustedgroupLimitsDTO.getDailyLimit();
		Double newLimitGroupWeeklyValue = amount + exhaustedgroupLimitsDTO.getWeeklyLimit();
		Double newLimitGroupMonthlyValue = amount + exhaustedgroupLimitsDTO.getMonthlyLimit();

		Double limitgroupPerTrLimit = groupLimitsDTO.getMaxTransactionLimit();
		Double limitgroupDailyLimit = groupLimitsDTO.getDailyLimit();
		Double limitgroupWeeklyLimit = groupLimitsDTO.getWeeklyLimit();
		Double limitgroupMonthlyLimit = groupLimitsDTO.getMonthlyLimit();
		LOG.debug("HBL::TransactionLimitsBusinessDelegateImpl::contractCutsomerLimitsDTO:customergrouplimits:limitgroupPerTrLimit:"+limitgroupPerTrLimit);
		LOG.debug("HBL::TransactionLimitsBusinessDelegateImpl::contractCutsomerLimitsDTO:customergrouplimits:limitgroupDailyLimit:"+limitgroupDailyLimit);
		LOG.debug("HBL::TransactionLimitsBusinessDelegateImpl::contractCutsomerLimitsDTO:customergrouplimits:limitgroupWeeklyLimit:"+limitgroupWeeklyLimit);
		LOG.debug("HBL::TransactionLimitsBusinessDelegateImpl::contractCutsomerLimitsDTO:customergrouplimits:limitgroupMonthlyLimit:"+limitgroupMonthlyLimit);

		if(Double.compare(amount, limitgroupPerTrLimit) <= 0) {
			if(Double.compare(newLimitGroupDailyValue, limitgroupDailyLimit) <= 0 ) {
				if(Double.compare(newLimitGroupMonthlyValue, limitgroupMonthlyLimit) <= 0) {
					// all user level limits are satisfied
					LOG.debug("HBL::TransactionLimitsBusinessDelegateImpl::customergrouplimits::validation success:");
				}
				else {
					//result.setStatus(TransactionStatusEnum.DENIED_MONTHLY);
					result.setDbpErrCode(HBLEnums.ERR_12506.getErrorCodeAsString());
					result.setDbpErrMsg(HBLEnums.ERR_12506.getErrorMsg());
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
		UserLimitsDTOExtn userLimitsDTO = customerDelegateExtn.fetchCustomerLimits(userId, featureActionID, accountId);
		LimitsDTOExtn exhaustedDTO = customerDelegateExtn.fetchExhaustedLimits(userId, featureActionID, date, accountId, channel);

		if(exhaustedDTO == null || exhaustedDTO.getDbpErrCode() != null || exhaustedDTO.getDbpErrMsg() != null) {
			LOG.error("Error while fetching user limits");
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
		Double newMonthlyValue = amount + exhaustedDTO.getMonthlyLimit();
		LOG.debug("HBL::TransactionLimitsBusinessDelegateImpl::customerAccountActionLimits:enterdAmount"+amount);
		LOG.debug("HBL::TransactionLimitsBusinessDelegateImpl:::customerAccountActionLimits:exhausted:getDailyLimit"+exhaustedDTO.getDailyLimit());
		LOG.debug("HBL::TransactionLimitsBusinessDelegateImpl:::customerAccountActionLimits:exhausted:getWeeklyLimit"+exhaustedDTO.getWeeklyLimit());
		LOG.debug("HBL::TransactionLimitsBusinessDelegateImpl:::customerAccountActionLimits:exhausted:getMonthlyLimit"+exhaustedDTO.getMonthlyLimit());
		Double autoDenialPerTrLimit = userLimitsDTO.getAutoDeniedTransactionLimit();
		Double autoDenialDailyLimit = userLimitsDTO.getAutoDeniedDailyLimit();
		Double autoDenialWeeklyLimit = userLimitsDTO.getAutoDeniedWeeklyLimit();
		Double autoDenialMonthlyLimit = userLimitsDTO.getAutoDeniedWeeklyLimit();
		
		LOG.debug("HBL::TransactionLimitsBusinessDelegateImpl::contractCutsomerLimitsDTO:customerAccountActionLimits:autoDenialPerTrLimit:"+autoDenialPerTrLimit);
		LOG.debug("HBL::TransactionLimitsBusinessDelegateImpl::contractCutsomerLimitsDTO:customerAccountActionLimits:autoDenialDailyLimit:"+autoDenialDailyLimit);
		LOG.debug("HBL::TransactionLimitsBusinessDelegateImpl::contractCutsomerLimitsDTO:customerAccountActionLimits:autoDenialWeeklyLimit:"+autoDenialWeeklyLimit);
		LOG.debug("HBL::TransactionLimitsBusinessDelegateImpl::contractCutsomerLimitsDTO:customerAccountActionLimits:autoDenialMonthlyLimit:"+autoDenialMonthlyLimit);

		if(Double.compare(amount, autoDenialPerTrLimit) <= 0) {
			if(Double.compare(newDailyValue, autoDenialDailyLimit) <= 0 ) {
				if(Double.compare(newMonthlyValue, autoDenialMonthlyLimit) <= 0) {
					// all auto denial limits are satisfied
					LOG.debug("HBL::TransactionLimitsBusinessDelegateImpl::customerAccountActionLimits::validation success:");
				}
				else {
					//result.setStatus(TransactionStatusEnum.DENIED_AD_MONTHLY);
					result.setDbpErrCode(HBLEnums.ERR_12503.getErrorCodeAsString());
					result.setDbpErrMsg(HBLEnums.ERR_12503.getErrorMsg());
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
			LOG.debug("HBL::ApprovalMatrixStatusDTO status::"+status.get(0).getIsDisabled());
			if(status!= null && status.size() > 0 && status.get(0).getIsDisabled()) {
				// all company level limits are satisfied
				LOG.debug("HBL::TransactionLimitsBusinessDelegateImpl::ContractAction::validation success:");
				//result.setStatus(TransactionStatusEnum.SENT);
				return validateGlobalLimits(amount, contractId, coreCustomerId, date, channel, result, request);
			}
		}
		LOG.debug("HBL::ApprovalMatrixStatusDTO transactionStatus::"+transactionStatus.toString());
		if(transactionStatus.equals(TransactionStatusEnum.NEW)) {
			//check for pre approve limits of a user
			Double apreApprovePerTrLimit = userLimitsDTO.getPreApprovedTransactionLimit();
			Double apreApproveDailyLimit = userLimitsDTO.getPreApprovedDailyLimit();
			Double apreApproveWeeklyLimit = userLimitsDTO.getPreApprovedWeeklyLimit();
			Double apreApproveMonthlyLimit = userLimitsDTO.getPreApprovedWeeklyLimit();
			if(Double.compare(amount, apreApprovePerTrLimit) > 0){
				//needs approval
				limitTypes.add(Constants.MAX_TRANSACTION_LIMIT);
			}
			if(Double.compare(newDailyValue, apreApproveDailyLimit) > 0 ) {
				//needs approval
				limitTypes.add(Constants.DAILY_LIMIT);
			}
			if(Double.compare(newMonthlyValue, apreApproveMonthlyLimit) > 0) {
				//needs approval
				limitTypes.add("MONTHLY_LIMIT");
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
					limitValues.add("MONTHLY_LIMIT");
				}
				for(String limit : limitValues) {
					approvalmatrices.addAll(approvalMatrixBusinessDelegate.fetchApprovalMatrixTemplate(contractId, coreCustomerId, featureActionID, limit));
				}
			}
			List<String> validApprovers = approversBusinessDelegate.getAccountActionApproverList(contractId, coreCustomerId, accountId, featureActionID);
			return _fetchStatusFromLimitTypes(amount, limitTypes, approvalmatrices, validApprovers, userId, exhaustedDTO, result, request); // Modified as part of ADP-2810
		}
		else {
			//all conditions satisfied including pre approval limits if business user
			result.setStatus(TransactionStatusEnum.SENT);
			return result;
		}
	}
	*/
	private Double getNewConvertedAmount(Double amount, String transactionCurrency, String baseCurrency, DataControllerRequest request){
		try{
			TransactionLimitsBackendDelegate transactionLimitsBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(TransactionLimitsBackendDelegate.class);
			return transactionLimitsBackendDelegate.fetchNewConvertedAmount(amount, transactionCurrency, baseCurrency, request);
		}catch(Exception e){
			return null;
		}
	}
	private Double getConvertedAmount(Double amount, String transactionCurrency, String baseCurrency, DataControllerRequest request){
		Double convertedAmount = null;
		try{
			String market="10 1";
			Map<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put("baseCurrencyCode", baseCurrency);
			inputParams.put("quoteCurrencyCode", transactionCurrency);
			inputParams.put("market", market);
			inputParams.put("companyCode", "NP0010001");
			String methodId = "fetchCurrencyRates";
			Object [] inputArray = new Object[2];
			inputArray[0] = inputParams;
			inputArray[1] = inputParams;
			DataControllerResponse response = null;
			Result result = fetchCurrencyRates(methodId, inputArray, request, response);
			String dbpErrMsg = result.getParamValueByName("dbpErrMsg");
			String dbpErrCode = result.getParamValueByName("dbpErrCode");
			LOG.debug("HBL::fetchCurrencyRates:dbpErrMsg:" + dbpErrMsg);
			if (StringUtils.isBlank(dbpErrCode) && StringUtils.isBlank(dbpErrMsg)) {
				String responseStr = ResultToJSON.convert(result);
				JSONObject responseObj = new JSONObject(responseStr);
				String currenceCode = responseObj.optString("code");
				String currenceName = responseObj.optString("name");
				JSONObject ratesObj = getCurrencyMarketValues(responseObj);
				String buyRate = ratesObj.optString("buyRate");
				 convertedAmount = calculateConvertedAmount(amount, buyRate,  transactionCurrency, baseCurrency, request);
			}
		}catch(Exception e){
			LOG.debug("HBL::Exception Occured at TransactionLimitsBusinessDelegateImplExtn:getConvertedAmount:" + e);
			return null;
		}
		return convertedAmount;
	}
		public Double calculateConvertedAmount(Double amount, String convertionRate, String transactionCurrency, String baseCurrency, DataControllerRequest dcr){
			Double convertedAmount = null;
			try {
				if(amount == null || StringUtils.isBlank(convertionRate)){
					return null;
				}
				else{
					//Converting transacton currency to Base Currency amount
					Double exchangeRate = Double.parseDouble(convertionRate);
					convertedAmount = exchangeRate * amount;
					convertedAmount= (double) Math.round(convertedAmount * 100.0)/100.0;
					}
			}
			catch (Exception e) {
				LOG.error("Failed to fetch converted amount: "+ e);
				return null;
			}
			return convertedAmount;
		}
	public JSONObject getCurrencyMarketValues(JSONObject response) {
		JSONObject currencyMarketObj = null;
		JSONArray currencyMarkets = response.optJSONArray("markets");
		if (currencyMarkets != null && currencyMarkets.length() > 0) {
			for (int i = 0; i < currencyMarkets.length(); i++) {
				currencyMarketObj = currencyMarkets.getJSONObject(i);
				String currencyMarket = currencyMarketObj.optString("market");
				if (StringUtils.isNotBlank(currencyMarket) && currencyMarket.equalsIgnoreCase("TT")) {
					return currencyMarketObj;
				}
			}
		}
		return currencyMarketObj;
	}
	public Result fetchCurrencyRates(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = null;
		try {
			ForexResource forexResource = DBPAPIAbstractFactoryImpl.getResource(ForexResource.class);
			result = forexResource.fetchCurrencyRates(methodID, inputArray, request, response);
		}
		catch(Exception e) {
			LOG.error("Error occured while invoking fetchCurrencyRates: "+ e);
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		
		return result;
	}
	/*
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
private TransactionStatusDTO _fetchStatusFromLimitTypes(Double amount, List<String> limitTypes, List<ApprovalMatrixDTO> approvalmatrices, List<String> actualApprovers, String customerId, LimitsDTOExtn exhaustedDTO, TransactionStatusDTO result, DataControllerRequest dcRequest) {
		
		HashMap<String, String> groupMatrixmap = new HashMap<String,String>();
		HashMap<String, List<LimitRange>> matrices = new HashMap<String, List<LimitRange>>();
		HashMap<String, Double> exhaustedAmount = new HashMap<String, Double>();
		List<String> approvalMatrixIds = new ArrayList<String>();
		HashMap<String, Boolean> stpConfigMap = _validateSTPForLimits();
		
		if(CollectionUtils.isEmpty(approvalmatrices)) {
			if(stpConfigMap.containsValue(false)) {
				LOG.error("No approvalMatrix entry found for this featureactionId");
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
						matrices.put("MONTHLY_LIMIT", limitRanges);
						exhaustedAmount.put("MONTHLY_LIMIT", amount + exhaustedDTO.getMonthlyLimit());
						break;
						
					case "MB_MAX_TRANSACTION_LIMIT":
						matrices.put("MB_MAX_TRANSACTION_LIMIT", limitRanges);
						exhaustedAmount.put("MB_MAX_TRANSACTION_LIMIT", amount + exhaustedDTO.getMaxMBTransactionLimit());
						break;
					case "MB_DAILY_LIMIT":
						matrices.put("MB_DAILY_LIMIT", limitRanges);
						exhaustedAmount.put("MB_DAILY_LIMIT", amount + exhaustedDTO.getDailyMBLimit());
						break;
					case "MB_WEEKLY_LIMIT":
						matrices.put("MB_WEEKLY_LIMIT", limitRanges);
						exhaustedAmount.put("MB_WEEKLY_LIMIT", amount + exhaustedDTO.getWeeklyMBLimit());
						matrices.put("MB_MONTHLY_LIMIT", limitRanges);
						exhaustedAmount.put("MB_MONTHLY_LIMIT", amount + exhaustedDTO.getMonthlyMBLimit());
						break;
					case "MB_MIN_TRANSACTION_LIMIT":
						matrices.put("MB_MIN_TRANSACTION_LIMIT", limitRanges);
						exhaustedAmount.put("MB_MIN_TRANSACTION_LIMIT", amount + exhaustedDTO.getMinMBTransactionLimit());
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
					LOG.error("No limitranges found for featureactionId"+ limitType);
					result.setStatus(TransactionStatusEnum.DENIED_INVALID_APPROVAL_MATRIX);
					result.setDbpErrCode(ErrorCodeEnum.ERR_12527.getErrorCodeAsString());
					result.setDbpErrMsg(ErrorCodeEnum.ERR_12527.getMessage());
					return result;
				}else {
					LOG.error("Approval matrix is not set for "+ limitType +", by default the rule is NO_APPROVAL, so we need to process this transaction");
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
							LOG.error("Approval matrix is not configured, Please reconfigure and try again");
							result.setStatus(TransactionStatusEnum.DENIED_INVALID_APPROVAL_MATRIX);
							result.setDbpErrCode(ErrorCodeEnum.ERR_29033.getErrorCodeAsString());
							result.setDbpErrMsg(ErrorCodeEnum.ERR_29033.getMessage());
							return result;
						}
						else{
							LOG.error("Approval matrix is not updated yet, by default the rule is NO_APPROVAL, so we need to process this transaction");
							approvalMatrixIds.add(Constants.NO_APPROVAL);
							break;
						}
					}
					else {
						LOG.error("Approval matrix is not set for this contract-CoreCustomerId, account and actionId");
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
						LOG.error("Invalid approval matrix entries for amount ranges, multiple Upto ranges cannot exist");
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
						LOG.error("Invalid approval matrix entries for amount ranges, multiple Above ranges cannot exist");
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
								approverIds = approvalMatrixBusinessDelegate.fetchApproverIdsFromTemplate(matrixId, dcRequest) ;
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
							LOG.error("Failed to fetch approverIds for the respective matrix entry");
							result.setStatus(TransactionStatusEnum.DENIED_INVALID_APPROVAL_MATRIX);
							result.setDbpErrCode(ErrorCodeEnum.ERR_12524.getErrorCodeAsString());
							result.setDbpErrMsg(ErrorCodeEnum.ERR_12524.getMessage());
							return result;
						}
						
						if(actualApprovers == null) {
							LOG.error("Failed to fetch approverIds for the respective matrix entry");
							result.setStatus(TransactionStatusEnum.DENIED_INVALID_APPROVAL_MATRIX);
							result.setDbpErrCode(ErrorCodeEnum.ERR_12524.getErrorCodeAsString());
							result.setDbpErrMsg(ErrorCodeEnum.ERR_12524.getMessage());
							return result;
						}
						
						//are approversId still valid
						Set<String> validApprovers = actualApprovers.stream().distinct().filter(approverIds::contains).collect(Collectors.toSet());
						
						if(validApprovers.size() != approverIds.size()) {
							LOG.error("Fetched approverIds are not valid in the current approval matrix");
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
			            	LOG.error("Error while fetching Application record");
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
			LOG.error("No approvalMatrix ids found for this ammount");
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
		stpConfigMap.put("MONTHLY_LIMIT", false);
	}
	else {
		stpConfigMap.put(Constants.WEEKLY_LIMIT, true);
		stpConfigMap.put("MONTHLY_LIMIT", true);
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
*/
public TransactionStatusDTO validateForMobileLimits(String userId, String companyId, String accountId, String featureActionID, Double amount,
		TransactionStatusEnum transactionStatus, String date, String transactionCurrency, String serviceCharge, DataControllerRequest request)  {
	TransactionStatusDTO result = new TransactionStatusDTO();
	List<String> limitTypes = new ArrayList<>();
	channel=HBLConstants.MOBILE_BANKING;
	CustomerAccountsDTO account = accountBusinessDelegate.getAccountDetails(userId, accountId);
	String contractId = account.getContractId();
	String coreCustomerId = account.getCoreCustomerId();

	String legalEntityId = null;
	try {
		legalEntityId = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(request);
	}catch(Exception e){
		LOG.error(e);
	}
	
	//String baseCurrency = application.getBaseCurrencyFromCache();
	String baseCurrency = approvalMatrixBusinessDelegate.fetchUserApprovalCurrency(coreCustomerId,contractId,accountId); 
	if(StringUtils.isEmpty(baseCurrency))
		baseCurrency = EnvironmentConfigurationsHandler.getServerProperty("HBL_BASE_CURRENCY");
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
	LOG.debug("HBL:Mobile:TransactionLimitsBusinessDelegateImplExtn::transactionCurrency"+transactionCurrency+":baseCurrency:"+baseCurrency);
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
			LOG.error("Failed to fetch converted amount", e);
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
	LOG.debug("HBL::Mobile:TransactionLimitsBusinessDelegateImplExtn::contractId" + contractId + ":coreCustomerId:"+ coreCustomerId);
	if (StringUtils.isNotEmpty(contractId) && StringUtils.isNotEmpty(coreCustomerId)) {
		return validateGlobalLimits(amount, contractId, coreCustomerId, date, channel, result, request);
	} else {
		result.setDbpErrCode(ErrorCodeEnum.ERR_12511.getErrorCodeAsString());
		result.setDbpErrMsg(ErrorCodeEnum.ERR_12511.getMessage());
	}
	return result;
}
	/*if(StringUtils.isNotEmpty(contractId) && StringUtils.isNotEmpty(coreCustomerId)) {
		ContractBusinessDelegateImplExtn newContractDelegate= new ContractBusinessDelegateImplExtn();
		LimitsDTOExtn contractCutsomerLimitsDTO = newContractDelegate.fetchAllLimits(contractId, coreCustomerId, featureActionID, legalEntityId);
		
		LimitsDTOExtn exhaustedDTO = contractDelegateExtn.fetchExhaustedLimits(contractId, coreCustomerId, featureActionID, date, channel);
		LOG.debug("HBL:Mobile:TransactionLimitsBusinessDelegateImplExtn::contractCutsomerLimitsDTO"+contractCutsomerLimitsDTO.toString());
		LOG.debug("HBL:Mobile:TransactionLimitsBusinessDelegateImplExtn::exhaustedDTO(Limits used):"+exhaustedDTO.toString());
		if(contractCutsomerLimitsDTO == null 
				|| exhaustedDTO == null 
				|| exhaustedDTO.getDbpErrCode() != null 
				|| exhaustedDTO.getDbpErrMsg() != null) {
			
			LOG.error("Failed to fetch organization limits");
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
		
		Double newDailyValue = amount + exhaustedDTO.getDailyMBLimit();
		Double newWeeklyValue = amount + exhaustedDTO.getWeeklyMBLimit();
		//Getting feature level limit for min amount per transaction
		Double minMBAmountPerTrLimit = contractCutsomerLimitsDTO.getMinMBTransactionLimit();
		Double newMonthlyValue = amount + exhaustedDTO.getMonthlyMBLimit();
		LOG.debug("HBL:MB:TransactionLimitsBusinessDelegateImpl::enteredAmount"+amount);
		LOG.debug("HBL:MB:TransactionLimitsBusinessDelegateImpl::exhaustedDTO:getDailyLimit"+exhaustedDTO.getDailyMBLimit());
		LOG.debug("HBL:MB:TransactionLimitsBusinessDelegateImpl::exhaustedDTO:getWeeklyLimit"+exhaustedDTO.getWeeklyMBLimit());
		LOG.debug("HBL:MB:TransactionLimitsBusinessDelegateImpl::exhaustedDTO:getMonthlyLimit"+exhaustedDTO.getMonthlyMBLimit());
		LOG.debug("HBL:MB:TransactionLimitsBusinessDelegateImpl::contractCutsomerLimitsDTO:getMaxTransactionLimit"+contractCutsomerLimitsDTO.getMaxMBTransactionLimit());
		LOG.debug("HBL:MB:TransactionLimitsBusinessDelegateImpl::contractCutsomerLimitsDTO:getDailyMBLimit"+contractCutsomerLimitsDTO.getDailyMBLimit());
		LOG.debug("HBL:MB:TransactionLimitsBusinessDelegateImpl::contractCutsomerLimitsDTO:getWeeklyLimit"+contractCutsomerLimitsDTO.getWeeklyMBLimit());
		LOG.debug("HBL:MB:TransactionLimitsBusinessDelegateImpl::contractCutsomerLimitsDTO:getMonthlyLimit"+contractCutsomerLimitsDTO.getMonthlyMBLimit());
		Double perTrLimit = contractCutsomerLimitsDTO.getMaxMBTransactionLimit();
		Double dailyLimit = contractCutsomerLimitsDTO.getDailyMBLimit();
		Double weeklyLimit = contractCutsomerLimitsDTO.getWeeklyMBLimit();
		Double monthlyLimit = contractCutsomerLimitsDTO.getMonthlyMBLimit();
		if(Double.compare(amount, minMBAmountPerTrLimit) >= 0) {
		if(Double.compare(amount, perTrLimit) <= 0) {
			if(Double.compare(newDailyValue, dailyLimit) <= 0 ) {
				if(Double.compare(newMonthlyValue, monthlyLimit) <= 0) {
					// all company level limits are satisfied
				}
				else {
					//result.setStatus(TransactionStatusEnum.DENIED_MONTHLY);
					result.setDbpErrCode(HBLEnums.ERR_12506.getErrorCodeAsString());
					result.setDbpErrMsg(HBLEnums.ERR_12506.getErrorMsg());
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
			LOG.error("Error while fetching user Role");
			String dbpErrCode = ErrorCodeEnum.ERR_12509.getErrorCodeAsString();
			String dbpErrMsg =  ErrorCodeEnum.ERR_12509.getMessage();
			result.setDbpErrCode(dbpErrCode);
			result.setDbpErrMsg(dbpErrMsg);
			return result;
		}
		
		LimitsDTOExtn roleLimitsDTO = userRoleBusinessDelegateExtn.fetchLimits(userRole, featureActionID);
		exhaustedDTO = userRoleBusinessDelegateExtn.fetchExhaustedLimits(contractId, coreCustomerId, userRole, featureActionID, date, userId, channel);
		
		if(roleLimitsDTO == null 
				|| exhaustedDTO == null 
				|| exhaustedDTO.getDbpErrCode() != null 
				|| exhaustedDTO.getDbpErrMsg() != null ) {
			
			LOG.error("Error while fetching role limits");
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
		LOG.debug("HBL:MB:TransactionLimitsBusinessDelegateImpl::RoleLevelLimits:enterdAmount"+amount);
		LOG.debug("HBL:MB:TransactionLimitsBusinessDelegateImpl:::RoleLevelLimits:exhausted:getDailyLimit"+exhaustedDTO.getDailyMBLimit());
		LOG.debug("HBL:MB:TransactionLimitsBusinessDelegateImpl:::RoleLevelLimits:exhausted:getWeeklyLimit"+exhaustedDTO.getWeeklyMBLimit());
		LOG.debug("HBL:MB:TransactionLimitsBusinessDelegateImpl:::RoleLevelLimits:exhausted:getMonthlyLimit"+exhaustedDTO.getMonthlyMBLimit());
		
		
		
		newDailyValue = amount + exhaustedDTO.getDailyMBLimit();
		newWeeklyValue = amount + exhaustedDTO.getWeeklyMBLimit();
		newMonthlyValue = amount + exhaustedDTO.getMonthlyMBLimit();
		
		perTrLimit = roleLimitsDTO.getMaxMBTransactionLimit();
		dailyLimit = roleLimitsDTO.getDailyMBLimit();
		weeklyLimit = roleLimitsDTO.getWeeklyMBLimit();
		monthlyLimit = roleLimitsDTO.getMonthlyMBLimit();
		LOG.debug("HBL:MB:TransactionLimitsBusinessDelegateImpl::RoleLevelLimits:perTrLimit:"+perTrLimit);
		LOG.debug("HBL:MB:TransactionLimitsBusinessDelegateImpl::cRoleLevelLimits:dailyLimit:"+dailyLimit);
		LOG.debug("HBL:MB:TransactionLimitsBusinessDelegateImpl::RoleLevelLimits:weeklyLimit:"+weeklyLimit);
		LOG.debug("HBL:MB:TransactionLimitsBusinessDelegateImpl::RoleLevelLimits:monthlyLimit:"+monthlyLimit);
		
		if(Double.compare(amount, perTrLimit) <= 0) {
			if(Double.compare(newDailyValue, dailyLimit) <= 0 ) {
				if(Double.compare(newMonthlyValue, monthlyLimit) <= 0) {
					// all role level limits are satisfied
				}
				else {
					//result.setStatus(TransactionStatusEnum.DENIED_MONTHLY);
					result.setDbpErrCode(HBLEnums.ERR_12506.getErrorCodeAsString());
					result.setDbpErrMsg(HBLEnums.ERR_12506.getErrorMsg());
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
			
			LOG.error("Error while fetching user limits");
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
		*//*/
	
		//Validating Limit Group limits for both retail and business users
	String limitGroupId = featureActionDelegate.getLimitGroupId(featureActionID,legalEntityId);
	if(limitGroupId == null) {
		LOG.error("Error while fetching the limit group id");
		String dbpErrCode = ErrorCodeEnum.ERR_12519.getErrorCodeAsString();
		String dbpErrMsg =  ErrorCodeEnum.ERR_12519.getMessage();
		result.setDbpErrCode(dbpErrCode);
		result.setDbpErrMsg(dbpErrMsg);
		return result;
	}

	LimitsDTOExtn groupLimitsDTO = limitGroupBusinessDelegateExtn.fetchLimits(userId, contractId, coreCustomerId, limitGroupId);
	LimitsDTOExtn exhaustedgroupLimitsDTO = limitGroupBusinessDelegateExtn.fetchExhaustedLimits(contractId, coreCustomerId, userId, limitGroupId, date, channel);
	if(groupLimitsDTO == null || exhaustedgroupLimitsDTO == null || exhaustedgroupLimitsDTO.getDbpErrCode() != null || exhaustedgroupLimitsDTO.getDbpErrMsg() != null) {
		LOG.error("Error while fetching the limits for limit groups");
		String dbpErrCode = exhaustedgroupLimitsDTO.getDbpErrCode() == null ? ErrorCodeEnum.ERR_12518.getErrorCodeAsString() : exhaustedgroupLimitsDTO.getDbpErrCode();
		String dbpErrMsg = exhaustedgroupLimitsDTO.getDbpErrMsg() == null ? ErrorCodeEnum.ERR_12518.getMessage() : exhaustedgroupLimitsDTO.getDbpErrMsg();
		result.setDbpErrCode(dbpErrCode);
		result.setDbpErrMsg(dbpErrMsg);
		return result;
	}
	LOG.debug("HBL:MB:TransactionLimitsBusinessDelegateImpl::LimitGrouplimits:enterdAmount"+amount);
	LOG.debug("HBL:MB:TransactionLimitsBusinessDelegateImpl:::LimitGrouplimits:exhausted:getDailyLimit"+exhaustedgroupLimitsDTO.getDailyMBLimit());
	LOG.debug("HBL:MB:TransactionLimitsBusinessDelegateImpl:::LimitGrouplimits:exhausted:getWeeklyLimit"+exhaustedgroupLimitsDTO.getWeeklyMBLimit());
	LOG.debug("HBL:MB:TransactionLimitsBusinessDelegateImpl:::LimitGrouplimits:exhausted:getMonthlyLimit"+exhaustedgroupLimitsDTO.getMonthlyMBLimit());
	

	Double newLimitGroupDailyValue = amount + exhaustedgroupLimitsDTO.getDailyMBLimit();
	Double newLimitGroupWeeklyValue = amount + exhaustedgroupLimitsDTO.getWeeklyMBLimit();
	Double newLimitGroupMonthlyValue = amount + exhaustedgroupLimitsDTO.getMonthlyMBLimit();

	Double limitgroupPerTrLimit = groupLimitsDTO.getMaxMBTransactionLimit();
	Double limitgroupDailyLimit = groupLimitsDTO.getDailyMBLimit();
	Double limitgroupWeeklyLimit = groupLimitsDTO.getWeeklyMBLimit();
	Double limitgroupMonthlyLimit = groupLimitsDTO.getMonthlyMBLimit();
	LOG.debug("HBL:MB:TransactionLimitsBusinessDelegateImpl::LimitGrouplimits:perTrLimit:"+limitgroupPerTrLimit);
	LOG.debug("HBL:MB:TransactionLimitsBusinessDelegateImpl::LimitGrouplimits:dailyLimit:"+limitgroupDailyLimit);
	LOG.debug("HBL:MB:TransactionLimitsBusinessDelegateImpl::LimitGrouplimits:weeklyLimit:"+limitgroupWeeklyLimit);
	LOG.debug("HBL:MB:TransactionLimitsBusinessDelegateImpl::LimitGrouplimits:monthlyLimit:"+limitgroupMonthlyLimit);

	if(Double.compare(amount, limitgroupPerTrLimit) <= 0) {
		if(Double.compare(newLimitGroupDailyValue, limitgroupDailyLimit) <= 0 ) {
			if(Double.compare(newLimitGroupMonthlyValue, limitgroupMonthlyLimit) <= 0) {
				// all user level limits are satisfied
			}
			else {
				//result.setStatus(TransactionStatusEnum.DENIED_MONTHLY);
				result.setDbpErrCode(HBLEnums.ERR_12506.getErrorCodeAsString());
				result.setDbpErrMsg(HBLEnums.ERR_12506.getErrorMsg());
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
	UserLimitsDTOExtn userLimitsDTO = customerDelegateExtn.fetchCustomerLimits(userId, featureActionID, accountId);
	LimitsDTOExtn exhaustedDTO = customerDelegateExtn.fetchExhaustedLimits(userId, featureActionID, date, accountId, channel);

	if(exhaustedDTO == null || exhaustedDTO.getDbpErrCode() != null || exhaustedDTO.getDbpErrMsg() != null) {
		LOG.error("Error while fetching user limits");
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
	LOG.debug("HBL:MB:TransactionLimitsBusinessDelegateImpl::AutoDenialLimits of a user:enterdAmount"+amount);
	LOG.debug("HBL:MB:TransactionLimitsBusinessDelegateImpl:::AutoDenialLimits of a user:exhausted:getDailyLimit"+exhaustedDTO.getDailyMBLimit());
	LOG.debug("HBL:MB:TransactionLimitsBusinessDelegateImpl:::AutoDenialLimits of a user:exhausted:getWeeklyLimit"+exhaustedDTO.getWeeklyMBLimit());
	LOG.debug("HBL:MB:TransactionLimitsBusinessDelegateImpl:::AutoDenialLimits of a user:exhausted:getMonthlyLimit"+exhaustedDTO.getMonthlyMBLimit());
	Double newDailyValue = amount + exhaustedDTO.getDailyMBLimit();
	Double newWeeklyValue = amount + exhaustedDTO.getWeeklyMBLimit();
	Double newMonthlyValue = amount + exhaustedDTO.getMonthlyMBLimit();

	Double autoDenialPerTrLimit = userLimitsDTO.getAutoDeniedMBTransactionLimit();
	Double autoDenialDailyLimit = userLimitsDTO.getAutoDeniedMBDailyLimit();
	Double autoDenialWeeklyLimit = userLimitsDTO.getAutoDeniedMBWeeklyLimit();
	Double autoDenialMonthlyLimit = userLimitsDTO.getAutoDeniedMBWeeklyLimit();
	LOG.debug("HBL:MB:TransactionLimitsBusinessDelegateImpl::AutoDenialLimits of a user:perTrLimit:"+autoDenialPerTrLimit);
	LOG.debug("HBL:MB:TransactionLimitsBusinessDelegateImpl::AutoDenialLimits of a user:dailyLimit:"+autoDenialDailyLimit);
	LOG.debug("HBL:MB:TransactionLimitsBusinessDelegateImpl::AutoDenialLimits of a user:weeklyLimit:"+autoDenialWeeklyLimit);
	LOG.debug("HBL:MB:TransactionLimitsBusinessDelegateImpl::AutoDenialLimits of a user:monthlyLimit:"+autoDenialMonthlyLimit);

	if(Double.compare(amount, autoDenialPerTrLimit) <= 0) {
		if(Double.compare(newDailyValue, autoDenialDailyLimit) <= 0 ) {
			if(Double.compare(newMonthlyValue, autoDenialMonthlyLimit) <= 0) {
				// all auto denial limits are satisfied
			}
			else {
				//result.setStatus(TransactionStatusEnum.DENIED_AD_MONTHLY);
				result.setDbpErrCode(HBLEnums.ERR_12503.getErrorCodeAsString());
				result.setDbpErrMsg(HBLEnums.ERR_12503.getErrorMsg());
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
		LOG.debug("HBL::ApprovalMatrixStatusDTO status::"+status.get(0).getIsDisabled());
		if(status!= null && status.size() > 0 && status.get(0).getIsDisabled()) {
			//result.setStatus(TransactionStatusEnum.SENT);
			//return result;
			return validateGlobalLimits(amount, contractId, coreCustomerId, date, channel, result, request);
		}
	}
	LOG.debug("HBL::ApprovalMatrixStatusDTO transactionStatus::"+transactionStatus.toString());
	if(transactionStatus.equals(TransactionStatusEnum.NEW)) {
		//check for pre approve limits of a user
		Double apreApprovePerTrLimit = userLimitsDTO.getPreApprovedTransactionLimit();
		Double apreApproveDailyLimit = userLimitsDTO.getPreApprovedDailyLimit();
		Double apreApproveWeeklyLimit = userLimitsDTO.getPreApprovedWeeklyLimit();
		Double apreApproveMonthlyLimit = userLimitsDTO.getPreApprovedWeeklyLimit();
		if(Double.compare(amount, apreApprovePerTrLimit) > 0){
			//needs approval
			limitTypes.add(HBLConstants.MB_MAX_TRANSACTION_LIMIT);
		}
		if(Double.compare(newDailyValue, apreApproveDailyLimit) > 0 ) {
			//needs approval
			limitTypes.add(HBLConstants.MB_DAILY_LIMIT);
		}
		if(Double.compare(newMonthlyValue, apreApproveMonthlyLimit) > 0) {
			//needs approval
			limitTypes.add(HBLConstants.MB_MONTHLY_LIMIT);
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
			if(!limits.contains(HBLConstants.MB_MAX_TRANSACTION_LIMIT)) {
				limitValues.add(HBLConstants.MB_MAX_TRANSACTION_LIMIT);
			}
			if(!limits.contains(HBLConstants.MB_DAILY_LIMIT)) {
				limitValues.add(HBLConstants.MB_DAILY_LIMIT);
			}
			if(!limits.contains(HBLConstants.MB_WEEKLY_LIMIT)) {
				//limitValues.add(HBLConstants.MB_WEEKLY_LIMIT);
				limitValues.add(HBLConstants.MB_MONTHLY_LIMIT);
			}
			for(String limit : limitValues) {
				approvalmatrices.addAll(approvalMatrixBusinessDelegate.fetchApprovalMatrixTemplate(contractId, coreCustomerId, featureActionID, limit));
			}
		}
		List<String> validApprovers = approversBusinessDelegate.getAccountActionApproverList(contractId, coreCustomerId, accountId, featureActionID);
		return _fetchStatusFromLimitTypes(amount, limitTypes, approvalmatrices, validApprovers, userId, exhaustedDTO, result, request); // Modified as part of ADP-2810
	}
	else {
		//all conditions satisfied including pre approval limits if business user
		result.setStatus(TransactionStatusEnum.SENT);
		return result;
	}
}
*/
private static TransactionStatusDTO validateGlobalLimits(Double amount, String contractId, String coreCustomerId, String scheduledDate, String channel, TransactionStatusDTO result, DataControllerRequest request) {
	Map <String,Object> inputMap= new HashMap<String, Object>();
	inputMap.put("companyId", contractId + "_" + coreCustomerId);
	inputMap.put("scheduledDate", scheduledDate);
	inputMap.put("channel", channel);
	try {
	JsonObject globalLimit = CumulativeTransactionAmount.GetCumulativeTransactionAmount(inputMap);
	LOG.error("Error while fetching global limits"+globalLimit);
	if(globalLimit == null || globalLimit.get(TransactionsLimitConstants.DBPERRMSG) != null || globalLimit.get(TransactionsLimitConstants.DBPERRCODE) != null ) {
		String dbpErrCode =  ErrorCodeEnum.ERR_12511.getErrorCodeAsString();
		String dbpErrMsg =  "Error while fetching exhausted global limits.";
		result.setDbpErrCode(dbpErrCode);
		result.setDbpErrMsg(dbpErrMsg);
		return result;
	}else {
		Double exchaustedDailyLimit = globalLimit.get("dailyLimit").getAsDouble();
		Double exchaustedMonthlyLimit = globalLimit.get("monthlyLimit").getAsDouble();
		 Double newDailyValue = amount + exchaustedDailyLimit;
		 Double newMonthlyValue = amount + exchaustedMonthlyLimit;
		 Double dailyLimit;
		 Double monthlyLimit;
		 Double minLimit;
		 Double perTrLimit;
		 JSONObject globalLimits = getGlobalLimitsConfiguration(request, channel);
		 dailyLimit=globalLimits.getDouble("dailyLimit");
		 monthlyLimit=globalLimits.getDouble("monthlyLimit");
		 minLimit=globalLimits.getDouble("minLimit");
		 perTrLimit=globalLimits.getDouble("prTransLimit");
		if(Double.compare(amount, minLimit) >= 0) {
			if(Double.compare(amount, perTrLimit) <= 0) {
				if(Double.compare(newDailyValue, dailyLimit) <= 0 ) {
					if(Double.compare(newMonthlyValue, monthlyLimit) <= 0) {
						LOG.debug("HBL::TransactionLimitsBusinessDelegateImpl::ContractAction::validation success:");
						result.setStatus(TransactionStatusEnum.SENT);
						LOG.debug("HBL::TransactionStatus:"+ result.getStatus().toString());
						return result;
					}
					else {
						result.setDbpErrCode(HBLEnums.ERR_12617.getErrorCodeAsString());
						result.setDbpErrMsg(HBLEnums.ERR_12617.getErrorMsg());
						return result;
					}
				}
				else {
					result.setStatus(TransactionStatusEnum.DENIED_DAILY);
					result.setDbpErrCode(HBLEnums.ERR_12616.getErrorCodeAsString());
					result.setDbpErrMsg(HBLEnums.ERR_12616.getErrorMsg());
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
		}
	}catch (Exception e) {
		LOG.error("Exception occured while fetching global limits",e);
	}
	return null;
}
private static JSONObject getGlobalLimitsConfiguration(DataControllerRequest request, String channel){

	String globalLimitsString = BundleConfigurationHandler
			.fetchConfigurationValueOnKey(BundleConfigurationHandler.BUNDLEID_DBP, "CUSTOMER_GLOBAL_LIMITS", request.getHeaderMap());
	JSONObject globalLimits = new JSONObject();
	JSONObject limits= new JSONObject();
	 Double dailyLimit=null;
	 Double monthlyLimit=null;
	 Double minLimit=null;
	 Double prTransLimit =null;
	try {
		globalLimits = new JSONObject(globalLimitsString);
	 LOG.debug("getGlobalLimitConfiguration globalLimits::::" + globalLimits);
	if(channel.equalsIgnoreCase("OnlineBanking")) {
		 String olbCustomerMinLimit= globalLimits.has("ONLINE_BANKING_MIN_LIMT")?globalLimits.getString("ONLINE_BANKING_MIN_LIMT"):"";
		 minLimit = Double.parseDouble(olbCustomerMinLimit);
		 String olbCustomerPrTransLimit= globalLimits.has("ONLINE_BANKING_PR_TXN_LIMIT")?globalLimits.getString("ONLINE_BANKING_PR_TXN_LIMIT"):"";
		 prTransLimit = Double.parseDouble(olbCustomerPrTransLimit);
		 String olbCustomerDailyLimit= globalLimits.has("ONLINE_BANKING_MAX_DAILY_LIMIT")?globalLimits.getString("ONLINE_BANKING_MAX_DAILY_LIMIT"):"";
		 dailyLimit=Double.parseDouble(olbCustomerDailyLimit);
		 String olbCustomerMonthlyLimit= globalLimits.has("ONLINE_BANKING_MAX_MONTHLY_LIMIT")?globalLimits.getString("ONLINE_BANKING_MAX_MONTHLY_LIMIT"):"";
		 monthlyLimit = Double.parseDouble(olbCustomerMonthlyLimit);
	 }
	 else if (channel.equalsIgnoreCase("MobileBanking")) {
		 String mbCustomerMinLimit= globalLimits.has("MOBILE_BANKING_MIN_LIMT")?globalLimits.getString("MOBILE_BANKING_MIN_LIMT"):"";
		 minLimit = Double.parseDouble(mbCustomerMinLimit);
		 String mbCustomerPrTransLimit= globalLimits.has("MOBILE_BANKING_PR_TXN_LIMIT")?globalLimits.getString("MOBILE_BANKING_PR_TXN_LIMIT"):"";
		 prTransLimit = Double.parseDouble(mbCustomerPrTransLimit);
		 String mbCustomerDailyLimit= globalLimits.has("MOBILE_BANKING_MAX_DAILY_LIMIT")?globalLimits.getString("MOBILE_BANKING_MAX_DAILY_LIMIT"):"";
		 dailyLimit=Double.parseDouble(mbCustomerDailyLimit);
		 String mbCustomerMonthlyLimit= globalLimits.has("MOBILE_BANKING_MAX_MONTHLY_LIMIT")?globalLimits.getString("MOBILE_BANKING_MAX_MONTHLY_LIMIT"):"";
		 monthlyLimit=Double.parseDouble(mbCustomerMonthlyLimit);
	 }
	limits.put("dailyLimit", dailyLimit);
	limits.put("monthlyLimit", monthlyLimit);
	limits.put("minLimit", minLimit);
	limits.put("prTransLimit", prTransLimit);
	}catch (JSONException e) {
		 LOG.debug("JSONException occured while fetching the globalLimitConfigurations:", e);
	}catch (Exception e) {
		 LOG.debug("exception occured while fetching the globalLimitConfigurations:", e);
	}
	
	return limits;
}

	

}
