package com.hbl.productservicesExtn.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.hbl.productservicesExtn.utills.HBLUtility;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHCommonsBusinessDelegate;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHFileBusinessDelegate;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHTransactionBusinessDelegate;
import com.temenos.dbx.product.approvalservices.businessdelegate.api.ApprovalQueueBusinessDelegate;
import com.temenos.dbx.product.approvalservices.dto.BBRequestDTO;
import com.temenos.dbx.product.approvalservices.resource.impl.ApprovalQueueResourceImpl;
import com.temenos.dbx.product.approvalsframework.approvalrequest.resource.api.ApprovalRequestResource;
import com.temenos.dbx.product.bulkpaymentservices.backenddelegate.api.BulkPaymentRecordBackendDelegate;
import com.temenos.dbx.product.bulkpaymentservices.businessdelegate.api.BulkPaymentRecordBusinessDelegate;
import com.temenos.dbx.product.bulkpaymentservices.resource.api.BulkPaymentRecordResource;
import com.temenos.dbx.product.commons.businessdelegate.api.AccountBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.CustomerBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.FeatureActionBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.TransactionLimitsBusinessDelegate;
import com.temenos.dbx.product.commons.dto.CustomerAccountsDTO;
import com.temenos.dbx.product.commons.dto.FeatureActionDTO;
import com.temenos.dbx.product.commons.dto.TransactionStatusDTO;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.FeatureAction;
import com.temenos.dbx.product.constants.TransactionStatusEnum;
import com.temenos.dbx.product.signatorygroupservices.businessdelegate.api.ApprovalModeBusinessDelegate;
import com.temenos.dbx.product.signatorygroupservices.businessdelegate.api.SignatoryGroupBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.BillPayTransactionBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.DomesticWireTransactionBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.GeneralTransactionsBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.InterBankFundTransferBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.InternationalFundTransferBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.InternationalWireTransactionBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.IntraBankFundTransferBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.OwnAccountFundTransferBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.P2PTransactionBusinessDelegate;

import eu.bitwalker.useragentutils.UserAgent;

public class ApprovalQueueResourceImplExtn extends ApprovalQueueResourceImpl{
	private static final Logger LOG = LogManager.getLogger(ApprovalQueueResourceImpl.class);

	CustomerBusinessDelegate custDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(CustomerBusinessDelegate.class);
	BulkPaymentRecordResource bulkPaymentRecordResource = DBPAPIAbstractFactoryImpl.getResource(BulkPaymentRecordResource.class);
	ACHFileBusinessDelegate achFileBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ACHFileBusinessDelegate.class);
	ACHTransactionBusinessDelegate achTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ACHTransactionBusinessDelegate.class);
	BillPayTransactionBusinessDelegate billPayTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(BillPayTransactionBusinessDelegate.class);
	P2PTransactionBusinessDelegate p2PTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(P2PTransactionBusinessDelegate.class);
	InterBankFundTransferBusinessDelegate interBankFundTransferBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(InterBankFundTransferBusinessDelegate.class);
	IntraBankFundTransferBusinessDelegate intraBankFundTransferBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(IntraBankFundTransferBusinessDelegate.class);
	DomesticWireTransactionBusinessDelegate domesticWireTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(DomesticWireTransactionBusinessDelegate.class);
	InternationalWireTransactionBusinessDelegate internationalWireTransactionBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(InternationalWireTransactionBusinessDelegate.class);
	InternationalFundTransferBusinessDelegate internationalFundTransferBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(InternationalFundTransferBusinessDelegate.class);
	OwnAccountFundTransferBusinessDelegate ownAccountFundTransferBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(OwnAccountFundTransferBusinessDelegate.class);
	ApprovalQueueBusinessDelegate approvalQueueBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
	ApplicationBusinessDelegate applicationBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);
	BulkPaymentRecordBusinessDelegate bulkPaymentRecordBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(BulkPaymentRecordBusinessDelegate.class);
	BulkPaymentRecordBackendDelegate bulkPaymentRecordBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(BulkPaymentRecordBackendDelegate.class);
	GeneralTransactionsBusinessDelegate generalTransactionsBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(GeneralTransactionsBusinessDelegate.class);
	FeatureActionBusinessDelegate FeatureActionBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(FeatureActionBusinessDelegate.class);
	ACHCommonsBusinessDelegate aCHCommonsBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ACHCommonsBusinessDelegate.class);
	SignatoryGroupBusinessDelegate signatoryGroupBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(SignatoryGroupBusinessDelegate.class);
	ApprovalRequestResource approvalRequestResource = DBPAPIAbstractFactoryImpl.getResource(ApprovalRequestResource.class);

	@Override
	public Result validateForApprovals(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws IOException {
		LOG.debug("HBL::ApprovalQueueResourceImplExtn:: validateForLimits");
		TransactionLimitsBusinessDelegate limitsDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(TransactionLimitsBusinessDelegate.class);
		TransactionLimitsBusinessDelegateImplExtn limitsDelegateExtn = new TransactionLimitsBusinessDelegateImplExtn();
		Result result = new Result();
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams =  (HashMap<String, Object>)inputArray[1];

		String actionId = inputParams.containsKey("featureActionID") ? (String) inputParams.get("featureActionID") : null;

		FeatureActionDTO  actionDTO = FeatureActionBusinessDelegate.getFeatureActionById(actionId);

		boolean isAccountLevel = actionDTO.getIsAccountLevel().equalsIgnoreCase("true") ? true : false;

		/*
		allowedFeatureActionIds contains all the create action id's which the new approvals framework supports
		*/
		//Set<String> allowedFeatureActionIds = new HashSet<>(Arrays.asList(FeatureAction.ACH_PAYMENT_CREATE, FeatureAction.BILL_PAY_CREATE));
		Set<String> allowedFeatureActionIds = new HashSet<>(Arrays.asList(FeatureAction.ACH_PAYMENT_CREATE));

		// Temporary Channelling Mechanism for ValidateForApprovals V2.0 -  invocation point for customer-level feature actions
		if (!isAccountLevel || allowedFeatureActionIds.contains(actionId)) {
			try{
				result = approvalRequestResource.validateForApprovals(inputParams, request);
			} catch (ApplicationException ae) {
				return ae.getErrorCodeEnum().setErrorCode(result);
			} catch (Exception e) {
				LOG.error("Error occurred while invoking new validateForApprovals!: " + e);
				return ErrorCodeEnum.ERR_12000.setErrorCode(result);
			}
		} else {
			String offsetDetails = inputParams.get("offsetDetails") == null? null : inputParams.get("offsetDetails").toString();
			inputParams.remove("offsetDetails");
			LOG.debug("HBL:: TSR-773647 ApprovalQueueResourceImpl: validateForApprovals: inputParams:"+inputParams.toString());
			if(inputParams.get("status")!=null) {
			if(StringUtils.isNotBlank(inputParams.get("status").toString()) && !inputParams.get("status").toString().equals("NEW")) { 
			JSONObject StatusObj = new JSONObject(inputParams.get("status").toString());
			String StatusVal = StatusObj.getString("status").toString().toUpperCase();
			if(StatusVal != null && StatusVal.equals("NEW")) {
				inputParams.put("status", "NEW");
			}
			}
		}
			TransactionStatusDTO inputTransactionStatusDTO = null;
			try {
				inputTransactionStatusDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), TransactionStatusDTO.class);
				LOG.debug("TSR-773647::InputParams after Parse:"+inputParams);
			} catch (IOException e) {
				LOG.error("Error occured while fetching the input params: " + e);
				return ErrorCodeEnum.ERR_10549.setErrorCode(new Result());
			}

			String featureActionId = inputTransactionStatusDTO.getFeatureActionID();
			String featureActionlist = CustomerSession.getPermittedActionIds(request, Arrays.asList(featureActionId));
			if (StringUtils.isEmpty(featureActionlist)) {
				LOG.error("feature List is missing");
				return ErrorCodeEnum.ERR_12040.setErrorCode(result);
			}


			String confirmationNumber = inputTransactionStatusDTO.getConfirmationNumber();
			String oldRequestId = inputTransactionStatusDTO.getRequestId();

			FeatureActionDTO  featureActionDTO = FeatureActionBusinessDelegate.getFeatureActionById(featureActionId);
			if(StringUtils.isBlank(featureActionDTO.getApproveFeatureAction())) {
				result.addParam(new Param("status", TransactionStatusEnum.SENT.toString()));
				result.addParam(new Param("confirmationNumber", confirmationNumber));
				return result;
			}
			TransactionStatusDTO transactionStatusDTO = null;
			if(Constants.NON_MONETARY.equals(featureActionDTO.getTypeId())) {
				transactionStatusDTO = limitsDelegate.validateForLimitsForNonMonetaryActions(inputTransactionStatusDTO, request);

			}
			else if(StringUtils.isNotBlank(offsetDetails)) {
				Map<String, Double> offsetDetailsMap = JSONUtils.parseAsMap(offsetDetails, String.class, Double.class);
				inputTransactionStatusDTO.setOffsetDetails(offsetDetailsMap);
				transactionStatusDTO = limitsDelegate.validateOffsetLimitsForACHFile(null, inputTransactionStatusDTO.getCustomerId(),
						inputTransactionStatusDTO.getCompanyId(), inputTransactionStatusDTO.getOffsetDetails(), inputTransactionStatusDTO.getStatus(), request);

			}else {
				LOG.debug("HBL::ApprovalQueueResourceImpl::before validateForLimits: inputTransactionStatusDTO.getConvertedAmount()"+inputTransactionStatusDTO.getConvertedAmount());
				LOG.debug("HBL::ApprovalQueueResourceImpl::getDeviceInfo:"+HBLUtility.getDeviceInfo(request).toString());
				String channel=HBLUtility.getDeviceInfo(request).get("channel_id").toString();
				request.addRequestParam_("convertedAmount",inputTransactionStatusDTO.getConvertedAmount());
				if(channel.equalsIgnoreCase("desktop")) {
				transactionStatusDTO = limitsDelegate.validateForLimits(inputTransactionStatusDTO.getCustomerId(), inputTransactionStatusDTO.getCompanyId(),
						inputTransactionStatusDTO.getAccountId(), inputTransactionStatusDTO.getFeatureActionID(), inputTransactionStatusDTO.getAmount(),
						inputTransactionStatusDTO.getStatus(), inputTransactionStatusDTO.getDate(), inputTransactionStatusDTO.getTransactionCurrency(),
						inputTransactionStatusDTO.getServiceCharge(), request);
				}else {
					transactionStatusDTO = limitsDelegateExtn.validateForMobileLimits(inputTransactionStatusDTO.getCustomerId(), inputTransactionStatusDTO.getCompanyId(),
							inputTransactionStatusDTO.getAccountId(), inputTransactionStatusDTO.getFeatureActionID(), inputTransactionStatusDTO.getAmount(),
							inputTransactionStatusDTO.getStatus(), inputTransactionStatusDTO.getDate(), inputTransactionStatusDTO.getTransactionCurrency(),
							inputTransactionStatusDTO.getServiceCharge(), request);
					
				}

			}

			if(transactionStatusDTO == null)
			{
				LOG.error("Failed to validate limits");
				return ErrorCodeEnum.ERR_29013.setErrorCode(result);
			}

			TransactionStatusEnum transactionStatus = transactionStatusDTO.getStatus();

			if(transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null) {
				result.addParam(new Param("dbpErrCode", transactionStatusDTO.getDbpErrCode()));
				result.addParam(new Param("dbpErrMsg", transactionStatusDTO.getDbpErrMsg()));
				if(transactionStatus != null) {
					result.addParam(new Param("status", transactionStatus.toString()));
				}
				return result;
			}

			if(!Constants.NON_MONETARY.equals(featureActionDTO.getTypeId())) {
				try {
					String amountValue = "0.0";
					String serviceCharge = "0.0";
					String transactionAmount = "0.0";
					amountValue = transactionStatusDTO.getAmount() == null ? amountValue : transactionStatusDTO.getAmount().toString();
					serviceCharge = transactionStatusDTO.getServiceCharge() == null ? serviceCharge : transactionStatusDTO.getServiceCharge();
					transactionAmount = transactionStatusDTO.getTransactionAmount() == null ? transactionAmount : transactionStatusDTO.getTransactionAmount();
					result.addParam(new Param("amount", amountValue));
					result.addParam(new Param("transactionAmount", transactionAmount));
					result.addParam(new Param("serviceCharge", serviceCharge));
				} catch (Exception e) {
					LOG.error("Failed to parse amount");
					return ErrorCodeEnum.ERR_27017.setErrorCode(result);
				}
			}

			result.addParam(new Param("status", transactionStatus.toString()));
			result.addParam(new Param("confirmationNumber", confirmationNumber));
			if(transactionStatus == TransactionStatusEnum.PENDING) {

				ApprovalModeBusinessDelegate approvalModeBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalModeBusinessDelegate.class);
				AccountBusinessDelegate accountBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AccountBusinessDelegate.class);
				String accountId = null;
				String coreCustomerId = null;
				String legalEntityId = null;
				boolean isGroupLevel = false;
				CustomerAccountsDTO account = null;

				if(StringUtils.isNotBlank(offsetDetails) && transactionStatusDTO.getAccountId() != null) {
					//For ACH File Transactions
					Set<String> cifs = new HashSet<String>();
					String[] accountIds = transactionStatusDTO.getAccountId().split(",");

					accountId = transactionStatusDTO.getAccountId().split(",")[0];
					account = accountBusinessDelegate.getAccountDetails(inputTransactionStatusDTO.getCustomerId(), accountId);
					coreCustomerId = account.getCoreCustomerId();
					isGroupLevel = approvalModeBusinessDelegate.isGroupLevel(coreCustomerId);

					cifs.add(coreCustomerId);

					// To verify if ach file batches contains different accounts which has different types of approval matrix(user based & signatory group based)
					for(int i = 1; i<accountIds.length; i++) {
						account = accountBusinessDelegate.getAccountDetails(inputTransactionStatusDTO.getCustomerId(), accountIds[i]);
						coreCustomerId = account.getCoreCustomerId();
						if(!cifs.contains(coreCustomerId)) {
							if(isGroupLevel != approvalModeBusinessDelegate.isGroupLevel(coreCustomerId)) {
								LOG.error("File contains multiple accounts which belongs to different type of approval matrices");
								return ErrorCodeEnum.ERR_12528.setErrorCode(result);
							}
							cifs.add(coreCustomerId);
						}
					}

					accountId = transactionStatusDTO.getAccountId();
				}
				else {
					//For Other types of transactions
					accountId = inputTransactionStatusDTO.getAccountId();
					account = accountBusinessDelegate.getAccountDetails(inputTransactionStatusDTO.getCustomerId(), accountId);
					coreCustomerId = account.getCoreCustomerId();
					isGroupLevel = approvalModeBusinessDelegate.isGroupLevel(coreCustomerId);
				}

				try {
					legalEntityId = LegalEntityUtil.getLegalEntityForCifAndContract(account.getCoreCustomerId(), account.getContractId());
				} catch (ApplicationException e) {
					return e.getErrorCodeEnum().setErrorCode(new Result());
				}
				List<String> matrixIds = transactionStatusDTO.getApprovalMatrixIds();

				BBRequestDTO bbRequestDTO = new BBRequestDTO(null, confirmationNumber, matrixIds, inputTransactionStatusDTO.getFeatureActionID(),
						inputTransactionStatusDTO.getCompanyId(), accountId, transactionStatus.getStatus(), inputTransactionStatusDTO.getCustomerId(), 0, 0, legalEntityId);
				bbRequestDTO.setIsSelfApproved(String.valueOf(transactionStatusDTO.isSelfApproved()));
				bbRequestDTO.setSignatoryGroupMatrices(transactionStatusDTO.getSignatoryGroupMatrices());

				if(isGroupLevel) {
					bbRequestDTO.setIsGroupMatrix("true");
				}

				String requestId = null;
				if(StringUtils.isEmpty(oldRequestId))
					requestId = approvalQueueBusinessDelegate.addTransactionToApprovalQueue(bbRequestDTO);
				else {
					bbRequestDTO.setRequestId(oldRequestId);
					requestId = approvalQueueBusinessDelegate.updateTransactionInApprovalQueue(bbRequestDTO);
				}

				result.addParam(new Param("isSelfApproved", String.valueOf(transactionStatusDTO.isSelfApproved())));
				result.addParam(new Param("requestId", requestId));
			}
		}
		return result;
	}
	

}
