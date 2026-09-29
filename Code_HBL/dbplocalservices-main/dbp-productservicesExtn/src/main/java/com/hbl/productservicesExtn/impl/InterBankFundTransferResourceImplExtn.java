package com.hbl.productservicesExtn.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonObject;
import com.hbl.productservicesExtn.constants.HBLConstants;
import com.hbl.productservicesExtn.utills.GenerateNCHLPayload;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.UserAgentUtil;
import com.kony.objectserviceutils.EventsDispatcher;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.approvalservices.businessdelegate.api.ApprovalQueueBusinessDelegate;
import com.temenos.dbx.product.approvalservices.businessdelegate.api.ApproversBusinessDelegate;
import com.temenos.dbx.product.commons.backenddelegate.api.TransactionLimitsBackendDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.AccountBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.ContractBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.CustomerBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.TransactionLimitsBusinessDelegate;
import com.temenos.dbx.product.commons.dto.CustomerAccountsDTO;
import com.temenos.dbx.product.commons.dto.TransactionStatusDTO;
import com.temenos.dbx.product.commonsutils.AuditLog;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.commonsutils.LogEvents;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.FeatureAction;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.constants.TransactionStatusEnum;
import com.temenos.dbx.product.payeeservices.constants.PayeeVerificationBackendServicesHelper;
import com.temenos.dbx.product.transactionservices.backenddelegate.api.InterBankFundTransferBackendDelegate;
import com.temenos.dbx.product.transactionservices.backenddelegate.api.IntraBankFundTransferBackendDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.InterBankFundTransferBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.IntraBankFundTransferBusinessDelegate;
import com.temenos.dbx.product.transactionservices.dto.InterBankFundTransferBackendDTO;
import com.temenos.dbx.product.transactionservices.dto.InterBankFundTransferDTO;
import com.temenos.dbx.product.transactionservices.dto.IntraBankFundTransferBackendDTO;
import com.temenos.dbx.product.transactionservices.dto.IntraBankFundTransferDTO;
import com.temenos.dbx.product.transactionservices.resource.impl.InterBankFundTransferResourceImpl;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class InterBankFundTransferResourceImplExtn extends InterBankFundTransferResourceImpl{
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	CustomerBusinessDelegate customerDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(CustomerBusinessDelegate.class);
	ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(ApplicationBusinessDelegate.class);
	TransactionLimitsBusinessDelegate limitsDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(TransactionLimitsBusinessDelegate.class);
	InterBankFundTransferBusinessDelegate interbanktransferDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(InterBankFundTransferBusinessDelegate.class);
	InterBankFundTransferBackendDelegate interbankBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(InterBankFundTransferBackendDelegate.class);
	AccountBusinessDelegate accountBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AccountBusinessDelegate.class);
	ApprovalQueueBusinessDelegate approvalQueueDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
	AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
	
	private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(InterBankFundTransferResourceImplExtn.class);
	@Override
	public Result createTransaction(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
 
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		LOG.debug("InterBankFundTransferResourceImplExtn:inputParams:"+inputParams);
		InterBankFundTransferDTO interbankDTO = null;
		Result result = new Result();
		Double amount = null;

		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String createdby = CustomerSession.getCustomerId(customer);
		LOG.debug("InterBankFundTransferResourceImplExtn:createdby:"+createdby);
		String featureActionId = null;

		String legalEntityId = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(request);
		if(StringUtils.isEmpty(legalEntityId))
			legalEntityId=(String) customer.get("legalEntityId");
		LOG.debug("InterBankFundTransferResourceImplExtn:legalEntityId:"+legalEntityId);
		String validate = inputParams.get("validate") == null ? null : inputParams.get("validate").toString();
		LOG.debug("InterBankFundTransferResourceImplExtn:validate:"+validate);
		String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getValue(Constants.PAYMENT_BACKEND);
		if(PAYMENT_BACKEND.equalsIgnoreCase("STUB")) {
			if("true".equalsIgnoreCase(validate)) {
				return stubValidateResponse(inputParams);
			}
			else {
				return stubCommitResponse(inputParams);
			}
		}

		String amountValue = inputParams.get("amount").toString();
		String fromAccountNumber = inputParams.get("fromAccountNumber")!=null?inputParams.get("fromAccountNumber").toString():inputParams.get("debtorAccount").toString();
		inputParams.put("fromAccountNumber", fromAccountNumber);
		String toAccountNumber = inputParams.get("toAccountNumber")!=null?inputParams.get("toAccountNumber").toString():inputParams.get("creditorAccount").toString();
		inputParams.put("toAccountNumber", toAccountNumber);
		inputParams.put("ExternalAccountNumber", toAccountNumber);
		String beneficiaryName = inputParams.get("beneficiaryName")!=null?inputParams.get("beneficiaryName").toString():inputParams.get("creditorName").toString();
		inputParams.put("beneficiaryName", beneficiaryName);
		String transactionsNotes = inputParams.get("transactionsNotes")!=null?inputParams.get("transactionsNotes").toString():inputParams.get("remarks").toString();
		inputParams.put("transactionsNotes", transactionsNotes);
		String frequencyType = inputParams.get("frequencyType") == null ? null : inputParams.get("frequencyType").toString();
		String bankId = inputParams.get("bankId")!=null?inputParams.get("bankId").toString():inputParams.get("creditorAgent")!=null?inputParams.get("creditorAgent").toString():"";
		inputParams.put("bankId", bankId);
		inputParams.put("creditorAgent", bankId);
		String creditorBranch = inputParams.get("creditorBranch")!=null?inputParams.get("creditorBranch").toString():HBLConstants.CIPS_CREDITOR_BRANCH;
		
		//Receiver Bank name get it from database beneficiaryBankName based on bank id
		String beneficiaryBankName = request.getParameter("beneficiaryBankName");
		if(StringUtils.isNotBlank(bankId)) {
			JSONObject branchObj = getBranchDetails(bankId, request);
			LOG.debug("InterBankFundTransferResourceImplExtn:branchObj:"+branchObj);
			beneficiaryBankName= branchObj.optString("bank_name");
			creditorBranch=branchObj.optString("branch_cd");
		}
		inputParams.put("beneficiaryBankName", beneficiaryBankName);
		inputParams.put("creditorBranch", creditorBranch);
		request.addRequestParam_("beneficiaryBankName", beneficiaryBankName);

		CustomerAccountsDTO account = accountBusinessDelegate.getAccountDetails(createdby, fromAccountNumber);
		String contractId = account.getContractId();
		String coreCustomerId = account.getCoreCustomerId();
		String companyId = account.getOrganizationId();


		String baseCurrency;
		try {
			baseCurrency= LegalEntityUtil.getCurrencyForLegalEntity(legalEntityId);
			if(StringUtils.isEmpty(baseCurrency)){
				baseCurrency  = application.getBaseCurrencyFromCache();
			}
		} catch (Exception e) {
			alert.prepareError("Exception occured while fetching base currency from Legal Entity"+e).log();
			return  ErrorCodeEnum.ERR_27001.setErrorCode(new Result());
		}
		LOG.debug("InterBankFundTransferResourceImplExtn:baseCurrency:"+baseCurrency);

		String transactionCurrency = inputParams.get("transactionCurrency") != null ?inputParams.get("transactionCurrency").toString() : baseCurrency;
		inputParams.put("transactionCurrency", transactionCurrency);
		String serviceCharge = inputParams.get("serviceCharge") != null ? inputParams.get("serviceCharge").toString() : null;
		


		if (amountValue == null || amountValue == "") {
			return ErrorCodeEnum.ERR_12031.setErrorCode(new Result());
		}

		featureActionId = FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE;

		if (!authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(createdby, featureActionId,
				fromAccountNumber, CustomerSession.IsCombinedUser(customer))) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}

		try {
			amount = Double.parseDouble(amountValue);
		} catch (NumberFormatException e) {
			alert.prepareError("Invalid amount value", e).log();
			return ErrorCodeEnum.ERR_10624.setErrorCode(new Result());
		}
		
		fromAccountNumber = inputParams.get("fromAccountNumber").toString();
		inputParams.put("featureActionId", featureActionId);
		inputParams.put("companyId", companyId);
		inputParams.put("roleId", customerDelegate.getUserContractCustomerRole(contractId, coreCustomerId, createdby));
		inputParams.put("createdby", createdby);
		inputParams.put("legalEntityId", legalEntityId);
		LOG.debug("InterBankFundTransferResourceImplExtn:inputParams:"+inputParams);

		try {
			interbankDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), InterBankFundTransferDTO.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: ", e).log();
			return ErrorCodeEnum.ERR_28021.setErrorCode(new Result());
		}
		interbankDTO.setLegalEntityId(legalEntityId);
		interbankDTO.setBankId(bankId);
		if(inputParams.get("beneficiaryBankName")!=null)
		interbankDTO.setBankName(inputParams.get("beneficiaryBankName").toString());
		String channel="";
		try {
		UserAgentUtil ua = new UserAgentUtil(request);
		channel=ua.getChannel();
		}catch (Exception e) {
			return ErrorCodeEnum.ERR_10163.setErrorCode(new Result());
		}
		LOG.debug("HBL::InterBankFundTransferResourceImplExtn::channel:"+channel);
		if(channel.equalsIgnoreCase("desktop")) {
			channel=HBLConstants.ONLINE_BANKING;
		}else if(channel.equalsIgnoreCase("mobile")){
			channel=HBLConstants.MOBILE_BANKING;
		}
		inputParams.put("paidBy", channel);
		interbankDTO.setPaidBy(channel);
		String requestId=HelperMethods.getRandomNumericString(10);
		interbankDTO.setRequestId(requestId);
		inputParams.put("requestId", requestId);
		alert.prepareError("InterBankFundTransferResourceImplExtn:createTransaction: interbankDTO.getIsScheduled():"+ interbankDTO.getIsScheduled()).log();
		InterBankFundTransferBackendDTO interbankBackendDTO = new InterBankFundTransferBackendDTO();
		String transactionid ="";
		
		String date = interbankDTO.getScheduledDate() == null
				? (interbankDTO.getProcessingDate() == null
						? (interbankDTO.getFrequencyStartDate() == null ? application.getServerTimeStamp()
								: interbankDTO.getFrequencyStartDate())
						: interbankDTO.getProcessingDate())
				: interbankDTO.getScheduledDate();

		String backendid = inputParams.get("transactionId") == null || (StringUtils.isEmpty(inputParams.get("transactionId").toString())) ? null : inputParams.get("transactionId").toString();
		String beneficiaryId = inputParams.get("beneficiaryId") == null ? null : inputParams.get("beneficiaryId").toString();
		String requestid = "";
					
		if("true".equalsIgnoreCase(validate)) {
			InterBankFundTransferBackendDTO interBankFundTransferBackendDTO = new InterBankFundTransferBackendDTO();
			interBankFundTransferBackendDTO = interBankFundTransferBackendDTO.convert(interbankDTO);
			
			InterBankFundTransferDTO validateinterbankDTO = interbankBackendDelegate.validateTransaction(interBankFundTransferBackendDTO,request);
			try {
				 result = JSONToResult.convert(new JSONObject(validateinterbankDTO).toString());
				 return result;
			} catch (JSONException e) {
				alert.prepareError("Error occured while converting the response from Line of Business service for interbank transfer: ", e).log();
				return ErrorCodeEnum.ERR_21217.setErrorCode(new Result());
			}
		}
		String payeeVerificationStatus = "";
		String payeeVerificationErrMsg = "";
		verifyPayeeAndUpdateDB(request, inputParams ,result, payeeVerificationStatus, payeeVerificationErrMsg, false);
		payeeVerificationStatus= null != result.getParamValueByName("payeeVerificationStatus") ? result.getParamValueByName("payeeVerificationStatus") : "";
		payeeVerificationErrMsg= null !=result.getParamValueByName("payeeVerificationErrMsg")? result.getParamValueByName("payeeVerificationErrMsg") : "";
		if(payeeVerificationStatus.equalsIgnoreCase("Failure")) {
			return result;
		}
		String convertedAmount=inputParams.get("convertedAmount") != null ? inputParams.get("convertedAmount").toString() : null;
		TransactionStatusDTO transactionStatusDTO = new TransactionStatusDTO();
		transactionStatusDTO.setCustomerId(createdby);
		transactionStatusDTO.setCompanyId(companyId);
		transactionStatusDTO.setAccountId(fromAccountNumber);
		transactionStatusDTO.setAmount(amount);
		transactionStatusDTO.setStatus(TransactionStatusEnum.NEW);
		transactionStatusDTO.setDate(date);
		transactionStatusDTO.setTransactionCurrency("NPR"); /*if CCY Payments, amount value got converted and no need of exchange conversion. hence explicitly assigning transaction currency as NPR to avoid exchange convertion */
		transactionStatusDTO.setFeatureActionID(featureActionId);
		transactionStatusDTO.setConfirmationNumber(backendid);
		transactionStatusDTO.setServiceCharge(serviceCharge);
		transactionStatusDTO.setConvertedAmount(convertedAmount);

		transactionStatusDTO = approvalQueueDelegate.validateForApprovals(transactionStatusDTO, request);	
		LOG.debug("InterBankFundTransferResourceImplExtn: transactionStatusDTO:"+transactionStatusDTO.toString());
		if(transactionStatusDTO == null) {			
			return ErrorCodeEnum.ERR_29018.setErrorCode(new Result());
		}
		LOG.debug("InterBankFundTransferResourceImplExtn: transactionStatusDTO.getDbpErrCode():"+transactionStatusDTO.getDbpErrCode());
		if (transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null) {
			result.addParam(new Param("dbpErrCode", transactionStatusDTO.getDbpErrCode()));
			result.addParam(new Param("dbpErrMsg", transactionStatusDTO.getDbpErrMsg()));
			result.addParam(new Param("status", ErrorCodeEnum.ERR_10420.getMessage()));
			return result;
		}
		TransactionStatusEnum transactionStatus = transactionStatusDTO.getStatus();
		LOG.debug("InterBankFundTransferResourceImplExtn: transactionStatus:"+transactionStatus.toString());
		boolean isSelfApproved = transactionStatusDTO.isSelfApproved();

		interbankDTO.setStatus(transactionStatus.getStatus());
		if(transactionStatusDTO.getRequestId()!=null) {
		interbankDTO.setRequestId(transactionStatusDTO.getRequestId());
		}
		String confirmationNumber = (StringUtils.isEmpty(backendid)) ? Constants.REFERENCE_KEY + transactionStatusDTO.getRequestId() : backendid;
		LOG.debug("InterBankFundTransferResourceImplExtn: confirmationNumber:"+confirmationNumber);
		interbankDTO.setConfirmationNumber(confirmationNumber);

		try {
			if(transactionCurrency!=baseCurrency) {
				/*if CCY Payments, amount value got converted from UI and no need of do exchange conversion. */
					interbankDTO.setAmount(Double.parseDouble(transactionStatusDTO.getTransactionAmount()));
					//interbankDTO.setAmount(_getConvertedAmount(Double.parseDouble(amountValue),transactionCurrency,baseCurrency,request));
			}
			else
				interbankDTO.setAmount(amount);
		} catch (NumberFormatException e) {
			alert.prepareError("Invalid amount value", e).log();
			return ErrorCodeEnum.ERR_27017.setErrorCode(new Result());
		}
		interbankDTO.setServiceCharge(transactionStatusDTO.getServiceCharge());
		
		/*interbankDTO.setLegalEntityId(legalEntityId);
		interbankDTO.setBankId(bankId);
		if(inputParams.get("beneficiaryBankName")!=null)
		interbankDTO.setBankName(inputParams.get("beneficiaryBankName").toString());
		//interbankDTO.setTransactionId(null);
		String channel="";
		try {
		UserAgentUtil ua = new UserAgentUtil(request);
		channel=ua.getChannel();
		}catch (Exception e) {
			return ErrorCodeEnum.ERR_10163.setErrorCode(new Result());
		}
		LOG.debug("HBL::InterBankFundTransferResourceImplExtn::channel:"+channel);
		if(channel.equalsIgnoreCase("desktop")) {
			channel=HBLConstants.ONLINE_BANKING;
		}else if(channel.equalsIgnoreCase("mobile")){
			channel=HBLConstants.MOBILE_BANKING;
		}
		interbankDTO.setPaidBy(channel);
		String requestId=HelperMethods.getRandomNumericString(10);
		interbankDTO.setRequestId(requestId);
		*/
		if(interbankDTO.getIsScheduled().equals("true") || interbankDTO.getIsScheduled().equals("1")) {
			String scheduledDt = interbankDTO.getScheduledDate();
			Date scheduledDate = HelperMethods.getFormattedTimeStamp(scheduledDt);
			InterBankFundTransferDTO interbankdbxDTO = null;
			//if (scheduledDate.after(new Date())) {
				interbankDTO.setStatus("Scheduled");
				HashMap<String,Object> servicePayload = null;
						try{
							servicePayload=(HashMap<String, Object>) payloadForActualService(inputParams, requestId).get("internalServicePayload");
						}catch (Exception e) {
							return ErrorCodeEnum.ERR_10703.setErrorCode(result,e.getMessage());
						}
				interbankDTO.setInternalApiPayload(servicePayload);
				interbankdbxDTO = interbanktransferDelegate.createTransactionAtDBX(interbankDTO);
			//}
			if(interbankdbxDTO == null) {
				alert.prepareError("Error occured while creating entry into the DBX table: ").log();
				return ErrorCodeEnum.ERR_29016.setErrorCode(new Result());
			}
			if(interbankdbxDTO.getDbpErrCode() != null || interbankdbxDTO.getDbpErrMsg() != null) {
				result.addParam(new Param("dbpErrCode", interbankdbxDTO.getDbpErrCode()));
				result.addParam(new Param("dbpErrMsg", interbankdbxDTO.getDbpErrMsg()));
			}
			if(StringUtils.isNotBlank(interbankdbxDTO.getTransactionId())) {
				transactionid = interbankdbxDTO.getTransactionId();
		        result.addParam(new Param("referenceId", interbankdbxDTO.getTransactionId()));
		        result.addParam(new Param("paymentId", interbankdbxDTO.getRequestId()));
		        result.addParam(new Param("amount", String.valueOf(amount)));
		        result.addParam(new Param("serviceCharge", serviceCharge));
		        result.addParam(new Param("transactionAmount", interbankdbxDTO.getTransactionAmount()));
		        result.addParam(new Param(Constants.FREQUENCYTYPE, frequencyType));
		        result.addParam(new Param("scheduledDate", scheduledDt));
		        result.addParam(new Param("requestId", interbankdbxDTO.getRequestId()));
		        result.addParam(new Param("transactionsNotes", interbankdbxDTO.getPaymentNote()));
				result.addParam(new Param("status", interbankdbxDTO.getStatus()));
				result.addParam(new Param("isScheduled", "1"));
		        result.addParam(new Param("message", "Your transaction has been scheduled successfully."));
		        result.addParam(new Param("legalEntityId", legalEntityId));
		        result.addParam(new Param("httpStatusCode", "200"));
		        Map updateParams = new HashMap<String, Object>();
				updateParams.put("internalServiceResponse",getJsonObjectFromResult(result).toString());
				updateStatusUsingTransactionId(transactionid,  "Scheduled", confirmationNumber, updateParams);
				try {
					_logTransaction(request,response,inputArray,result,transactionStatus,transactionStatusDTO.getConfirmationNumber(),interbankdbxDTO,requestid);
				} catch(Exception e) {
					alert.prepareError("Error occured while audit logging.",e).log();
				}
			}
			return result;
		}
		HashMap<String, Object> payload= null;
		try {
			payload = payloadForActualService(inputParams, requestId);
		}catch (Exception e) {
			return ErrorCodeEnum.ERR_10703.setErrorCode(result,e.getMessage());
		}
		interbankDTO.setExternalApiPayload((HashMap<String, Object>) payload.get("externalServicepayload"));
		InterBankFundTransferDTO interbankdbxDTO = interbanktransferDelegate.createTransactionAtDBX(interbankDTO);
		if(interbankdbxDTO == null) {
			alert.prepareError("Error occured while creating entry into the DBX table: ").log();
			return ErrorCodeEnum.ERR_29016.setErrorCode(new Result());
		}
		if(interbankdbxDTO.getDbpErrCode() != null || interbankdbxDTO.getDbpErrMsg() != null) {
			result.addParam(new Param("dbpErrCode", interbankdbxDTO.getDbpErrCode()));
			result.addParam(new Param("dbpErrMsg", interbankdbxDTO.getDbpErrMsg()));
			return result;
		}
		
		interbankdbxDTO.setValidate(validate);
		
		interbankBackendDTO = interbankBackendDTO.convert(interbankdbxDTO);
				
		String creditValueDate = inputParams.get("creditValueDate") == null ? "" : inputParams.get("creditValueDate").toString();
        String totalAmount = inputParams.get("totalAmount") == null ? "" : inputParams.get("totalAmount").toString(); 
        String exchangeRate = inputParams.get("exchangeRate") == null ? "" : inputParams.get("exchangeRate").toString();
        String intermediaryBicCode = inputParams.get("intermediaryBicCode") == null ? "" : inputParams.get("intermediaryBicCode").toString();
        String clearingCode = inputParams.get("clearingCode") == null ? "" : inputParams.get("clearingCode").toString();
        String e2eReference = inputParams.get("e2eReference") == null ? "" : inputParams.get("e2eReference").toString();
        String overrides = inputParams.get("overrides") == null ? "" : inputParams.get("overrides").toString();
        //String beneficiaryBankName = inputParams.get("beneficiaryBankName") == null ? "" : inputParams.get("beneficiaryBankName").toString();
        String beneficiaryAddressLine2 = inputParams.get("beneficiaryAddressLine2") == null ? "" : inputParams.get("beneficiaryAddressLine2").toString();
        String beneficiaryPhone = inputParams.get("beneficiaryPhone") == null ? "" : inputParams.get("beneficiaryPhone").toString();
        String beneficiaryEmail = inputParams.get("beneficiaryEmail") == null ? "" : inputParams.get("beneficiaryEmail").toString();
        String beneficiaryState = inputParams.get("beneficiaryState") == null ? "" : inputParams.get("beneficiaryState").toString();
		String beneficiaryAddressLine1= inputParams.get("beneficiaryAddressLine1") == null ? "" : inputParams.get("beneficiaryAddressLine1").toString();
		String beneficiaryCity= inputParams.get("beneficiaryCity") == null ? "" : inputParams.get("beneficiaryCity").toString();
		String beneficiaryZipcode= inputParams.get("beneficiaryZipcode") == null ? "" : inputParams.get("beneficiaryZipcode").toString();
		String beneficiarycountry= inputParams.get("beneficiarycountry") == null ? "" : inputParams.get("beneficiarycountry").toString();
		String localInstrumentProprietary= inputParams.get("localInstrumentProprietary") == null ? "" : inputParams.get("localInstrumentProprietary").toString();
		String purposeCode= inputParams.get("purposeCode") == null ? "" : inputParams.get("purposeCode").toString();
		String clearingIdentifierCode= inputParams.get("clearingIdentifierCode") == null ? "" : inputParams.get("clearingIdentifierCode").toString();
		String serviceLevelProprietary= inputParams.get("serviceLevelProprietary") == null ? "" : inputParams.get("serviceLevelProprietary").toString();
       
		interbankBackendDTO.setCreditValueDate(creditValueDate);
		interbankBackendDTO.setTotalAmount(totalAmount);
		interbankBackendDTO.setExchangeRate(exchangeRate);
		interbankBackendDTO.setIntermediaryBicCode(intermediaryBicCode);
		interbankBackendDTO.setClearingCode(clearingCode);
		interbankBackendDTO.setE2eReference(e2eReference);
		interbankBackendDTO.setBeneficiaryId(beneficiaryId);
		interbankBackendDTO.setOverrides(overrides);
		interbankBackendDTO.setBeneficiaryBankName(beneficiaryBankName);
		interbankBackendDTO.setBeneficiaryAddressLine2(beneficiaryAddressLine2);
		interbankBackendDTO.setBeneficiaryAddressLine1(beneficiaryAddressLine1);
		interbankBackendDTO.setBeneficiaryPhone(beneficiaryPhone);
		interbankBackendDTO.setBeneficiaryEmail(beneficiaryEmail);
		interbankBackendDTO.setBeneficiaryState(beneficiaryState);
		interbankBackendDTO.setBeneficiaryCity(beneficiaryCity);
		interbankBackendDTO.setBeneficiaryZipcode(beneficiaryZipcode);
		interbankBackendDTO.setBeneficiarycountry(beneficiarycountry);
		interbankBackendDTO.setLocalInstrumentProprietary(localInstrumentProprietary);
		interbankBackendDTO.setPurposeCode(purposeCode);
		interbankBackendDTO.setClearingIdentifierCode(clearingIdentifierCode);
		interbankBackendDTO.setServiceLevelProprietary(serviceLevelProprietary);
		interbankBackendDTO.setPayeeVerificationStatus(payeeVerificationStatus);
		interbankBackendDTO.setPayeeVerificationErrMsg(payeeVerificationErrMsg);
		interbankBackendDTO.setRequestId(interbankDTO.getRequestId());

		try {
			/*interbankBackendDTO.setAmount(Double.parseDouble(interbankBackendDTO.getTransactionAmount()));
			interbankdbxDTO.setAmount(Double.parseDouble(interbankBackendDTO.getTransactionAmount()));
			*/
			interbankBackendDTO.setAmount(interbankBackendDTO.getAmount());
			interbankdbxDTO.setAmount(interbankBackendDTO.getAmount());
		} catch (Exception e) {
			alert.prepareError("Invalid amount value", e).log();
			return ErrorCodeEnum.ERR_27017.setErrorCode(new Result());
		}
		
		try {
			String responseObj = new JSONObject(interbankBackendDTO).toString();
			result = JSONToResult.convert(responseObj);
		} catch (JSONException e) {
			alert.prepareError(
					"Error occured while converting the response from Line of Business service for interbank transfer: ",e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		transactionid = interbankdbxDTO.getTransactionId();
        InterBankFundTransferDTO interbanktransactionDTO = new InterBankFundTransferDTO();

        String createWithPaymentId = inputParams.get("createWithPaymentId") == null ? ""
                : inputParams.get("createWithPaymentId").toString();

		if(transactionStatus == TransactionStatusEnum.SENT ) {
		    if (StringUtils.isEmpty(backendid)
                    || (StringUtils.isNotBlank(backendid) && createWithPaymentId.equalsIgnoreCase("true"))) {
		        if(StringUtils.isNotBlank(backendid) && createWithPaymentId.equalsIgnoreCase("true")){
		            interbankBackendDTO.setTransactionId(backendid);
		            String charges = inputParams.get("charges") == null ? null : inputParams.get("charges").toString();
		            interbankBackendDTO.setCharges(charges);
                }else{
                    interbankBackendDTO.setTransactionId(null);
                }
		        LOG.debug("InterBankFundTransferResourceImplExtn:InterBankFundTransfer DBXDB interBankTransTxID:"+transactionid);
		        try {
		        payload=payloadForActualService(inputParams, requestId);
		        }catch (Exception e) {
		        	return ErrorCodeEnum.ERR_10703.setErrorCode(result,e.getMessage());
				}
		        interbankBackendDTO.setExternalApiPayload((HashMap<String, Object>) payload.get("externalServicepayload"));
		        interbankBackendDTO.setInternalApiPayload((HashMap<String, Object>) payload.get("internalServicePayload"));
				interbanktransactionDTO = interbankBackendDelegate.createTransactionWithoutApproval(interbankBackendDTO, request);					
				if(interbanktransactionDTO == null) {	
					interbanktransferDelegate.updateStatusUsingTransactionId(transactionid, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
					return ErrorCodeEnum.ERR_12601.setErrorCode(result);
				}
			}
			else {
				String frequency = StringUtils.isEmpty(interbankDTO.getFrequencyTypeId()) ? null : interbankDTO.getFrequencyTypeId();
				interbanktransactionDTO  = interbanktransferDelegate.approveTransaction(backendid, request, frequency);
				if(interbanktransactionDTO == null) {	
					interbanktransferDelegate.updateStatusUsingTransactionId(transactionid, TransactionStatusEnum.FAILED.getStatus(),confirmationNumber);
					return ErrorCodeEnum.ERR_29020.setErrorCode(result);
				}		
			}
		    LOG.debug("InterBankFundTransferResourceImplExtn:transactionid:"+transactionid);
		    LOG.debug("InterBankFundTransferResourceImplExtn:interbanktransactionDTO.getDbpErrMsg():"+interbanktransactionDTO.getDbpErrMsg());
		    String externalServiceResponse= interbanktransactionDTO.getExternalServiceResponse()!=null? interbanktransactionDTO.getExternalServiceResponse().toString():null;
		   // HashMap<String,Object> externalServicePayload= interbanktransactionDTO.getExternalApiPayload();
		    String dbpErrMsg=interbanktransactionDTO.getDbpErrMsg();
			String dbpErrCode=interbanktransactionDTO.getDbpErrCode();
			String status=interbanktransactionDTO.getStatus();
			String paymentId=interbanktransactionDTO.getPaymentId();
			String paymentRequestId=interbanktransactionDTO.getRequestId();
			String paymentTransactionsNotes=interbanktransactionDTO.getPaymentNote();
			Map<String, Object> updateParams = new HashMap<String, Object>();
			updateParams.put("requestId", paymentRequestId);
			updateParams.put("paymentId", paymentId);
			updateParams.put("transactionsNotes", paymentTransactionsNotes);
			//LOG.debug("InterBankFundTransferResourceImplExtn:interbanktransactionDTO.externalServicePayload():"+externalServicePayload);
			//updateParams.put("externalServicePayload", externalServicePayload!=null?new JSONObject(externalServicePayload).toString():null);
			updateParams.put("externalServiceResponse", externalServiceResponse);
		    if(interbanktransactionDTO.getDbpErrCode() != null || interbanktransactionDTO.getDbpErrMsg() != null) {
				LOG.debug("InterBankFundTransferResourceImplExtn:interbanktransactionDTO.getDbpErrCode():"+interbanktransactionDTO.getDbpErrCode());
				result.addParam(new Param("referenceId",paymentId));
				if(dbpErrMsg.equalsIgnoreCase(TransactionStatusEnum.REVERSED.getMessage()) || dbpErrMsg.equalsIgnoreCase(TransactionStatusEnum.REVERSAL_FAILED.getMessage())) {
				result.addParam(new Param("referenceId",paymentId));
				result.addParam(new Param("paymentId", paymentId));
				result.addParam(new Param("legalEntityId", legalEntityId));
				}
				result.addParam(new Param("message",dbpErrMsg));
				result.addParam(new Param("errorDetails", interbanktransactionDTO.getErrorDetails()));
				result.addParam(new Param("dbpErrCode", dbpErrCode));
				result.addParam(new Param("dbpErrMsg", dbpErrMsg));
				result.addParam(new Param("status", status));
				result.addParam(new Param("externalServiceResponse",  externalServiceResponse));
				updateStatusUsingTransactionId(transactionid,  TransactionStatusEnum.FAILED.getStatus(), confirmationNumber, updateParams);
				LOG.debug("InterBankFundTransferResourceImplExtn:interbanktransactionDTO.final result::"+result);
				return result;
			}	
			
			if(interbanktransactionDTO.getReferenceId() == null || "".equals(interbanktransactionDTO.getReferenceId())) {
				interbanktransferDelegate.updateStatusUsingTransactionId(transactionid, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
				return ErrorCodeEnum.ERR_12601.setErrorCode(result);
			}
			interbankDTO = interbanktransactionDTO;
			updateStatusUsingTransactionId(transactionid,  TransactionStatusEnum.EXECUTED.getStatus(), interbanktransactionDTO.getReferenceId(), updateParams);
	        result.appendResult(JSONToResult.convert(interbanktransactionDTO.getExternalServiceResponse().toString()));
	        result.addParam(new Param("referenceId", interbanktransactionDTO.getReferenceId()));
	        result.addParam(new Param("paymentId", interbanktransactionDTO.getPaymentId()));
	        result.addParam(new Param("requestId", interbanktransactionDTO.getRequestId()));
	        result.addParam(new Param("transactionsNotes", interbanktransactionDTO.getPaymentNote()));
			result.addParam(new Param("status", transactionStatus.getStatus()));
	        result.addParam(new Param("message", transactionStatus.getMessage()));
	        result.addParam(new Param("legalEntityId", legalEntityId));
	        result.addParam(new Param("httpStatusCode", "200"));
	        result.addParam(new Param("externalServiceResponse",  externalServiceResponse));
	        
		}
		else if(transactionStatus == TransactionStatusEnum.PENDING){
			requestid = transactionStatusDTO.getRequestId();
			String pendingrefId = null;
			interbankdbxDTO.setCreditValueDate(creditValueDate);
			interbankdbxDTO.setTotalAmount(totalAmount);
			interbankdbxDTO.setExchangeRate(exchangeRate);
			interbankdbxDTO.setIntermediaryBicCode(intermediaryBicCode);
			interbankdbxDTO.setClearingCode(clearingCode);
			interbankdbxDTO.setE2eReference(e2eReference);
			if (StringUtils.isEmpty(backendid)
                    || (StringUtils.isNotBlank(backendid) && createWithPaymentId.equalsIgnoreCase("true"))) {
			    if(StringUtils.isNotBlank(backendid) && createWithPaymentId.equalsIgnoreCase("true")){
			        interbankdbxDTO.setTransactionId(backendid);
			        String charges = inputParams.get("charges") == null ? null : inputParams.get("charges").toString();
			        interbankdbxDTO.setCharges(charges);
                } 
			    InterBankFundTransferDTO interbankpendingtransactionDTO = interbanktransferDelegate.createPendingTransaction(interbankdbxDTO, request);
				if(interbankpendingtransactionDTO == null)
				{
					interbanktransferDelegate.updateStatusUsingTransactionId(transactionid, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
					alert.prepareError("Error occured while creating entry into the backend table: ").log();
					return ErrorCodeEnum.ERR_29017.setErrorCode(new Result());
				}
				if(interbankpendingtransactionDTO.getDbpErrCode() != null || interbankpendingtransactionDTO.getDbpErrMsg() != null) {
					interbanktransferDelegate.updateStatusUsingTransactionId(transactionid, TransactionStatusEnum.FAILED.getStatus(),confirmationNumber);
					result.addParam(new Param("errorDetails", interbankpendingtransactionDTO.getErrorDetails()));
					return ErrorCodeEnum.ERR_00000.setErrorCode(result, interbankpendingtransactionDTO.getDbpErrMsg());
				}
				backendid = interbankpendingtransactionDTO.getReferenceId();
				interbankDTO= interbankpendingtransactionDTO;
			}
				pendingrefId = backendid;
				interbanktransferDelegate.updateStatusUsingTransactionId(transactionid,transactionStatus.toString(), backendid);
				transactionStatusDTO = approvalQueueDelegate.updateBackendIdInApprovalQueue(requestid, backendid, isSelfApproved, featureActionId, request);
				if(transactionStatusDTO == null) 
				{							
					interbankBackendDelegate.deleteTransactionWithoutApproval(backendid, null, frequencyType, request);
					interbanktransferDelegate.updateStatusUsingTransactionId(transactionid, TransactionStatusEnum.FAILED.getStatus(),backendid);
					return ErrorCodeEnum.ERR_29019.setErrorCode(new Result());
				}	
				if(transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null) {
					interbankBackendDelegate.deleteTransactionWithoutApproval(backendid, null, frequencyType, request);
					interbanktransferDelegate.updateStatusUsingTransactionId(transactionid, TransactionStatusEnum.FAILED.getStatus(), backendid);
					result.addParam(new Param("dbpErrCode", transactionStatusDTO.getDbpErrCode()));
					result.addParam(new Param("dbpErrMsg", transactionStatusDTO.getDbpErrMsg()));
					return result;
				}
				
				transactionStatus = transactionStatusDTO.getStatus();
				backendid = transactionStatusDTO.getConfirmationNumber();
				
			
			//code snippet being added for alerts on inter transfer
			try {
				LogEvents.pushAlertsForApprovalRequests( featureActionId, request, response,inputParams, null,  
						backendid, requestid, CustomerSession.getCustomerName(customer),null);
			} catch (Exception e) {
				alert.prepareError("Failed at pushAlertsForApprovalRequests "+e).log();
			}
			
			result.addParam(new Param("requestId", requestid));
			
			
			if (transactionStatus == TransactionStatusEnum.APPROVED) {
				result.addParam(new Param("status", TransactionStatusEnum.SENT.getStatus()));
				result.addParam(new Param("message", TransactionStatusEnum.SENT.getMessage()));
				result.addParam(new Param("referenceId", backendid));
			}
			else {
				result.addParam(new Param("status", transactionStatus.getStatus()));
				result.addParam(new Param("message", transactionStatus.getMessage()));
				result.addParam(new Param("referenceId", pendingrefId));
				interbanktransferDelegate.updateStatusUsingTransactionId(transactionid,transactionStatus.toString(), pendingrefId);
			}
		}
		else if(transactionStatus == TransactionStatusEnum.APPROVED){
			result.addParam(new Param("referenceId", transactionStatusDTO.getConfirmationNumber()));
	        result.addParam(new Param("status", TransactionStatusEnum.SENT.getStatus()));
	        result.addParam(new Param("message", TransactionStatusEnum.SENT.getMessage()));
		}

		if(interbankDTO.getOverrides() != null) {
			result.addParam(new Param("overrides",interbankDTO.getOverrides()));
		}
		if(interbankDTO.getOverrideList() != null) {
			result.addParam(new Param("overrideList",interbankDTO.getOverrideList()));
		}
			
		if(interbankDTO.getCharges() != null) {
			result.addParam(new Param("charges",interbankDTO.getCharges()));
		}
		if(interbankDTO.getExchangeRate() != null) {
			result.addParam(new Param("exchangeRate",interbankDTO.getExchangeRate()));
		}
		if(interbankDTO.getTotalAmount() != null) {
			result.addParam(new Param("totalAmount",interbankDTO.getTotalAmount()));
		}
		if(interbankDTO.getMessageDetails() != null) {
            result.addParam(new Param("messageDetails", interbankDTO.getMessageDetails()));
        }
		if(interbankDTO.getQuoteCurrency() != null) {
            result.addParam(new Param("quoteCurrency", interbankDTO.getQuoteCurrency()));
        }
		try {
			_logTransaction(request,response,inputArray,result,transactionStatus,transactionStatusDTO.getConfirmationNumber(),interbankdbxDTO,requestid);
		} catch(Exception e) {
			alert.prepareError("Error occured while audit logging.",e).log();
		}

		// ADP-7058 update additional meta data
		try{
			approvalQueueDelegate.updateAdditionalMetaForApprovalRequest(transactionStatusDTO.getRequestId(), request);
		} catch(Exception e){
			alert.prepareError(e.toString()).log();
		}
		LOG.debug("HBL:InterBankFundTransferResourceImplExtn:Final result:" + ResultToJSON.convert(result));

		return result;
	}
	private double _getConvertedAmount(Double amount, String transactionCurrency, String baseCurrency, DataControllerRequest request) {
		TransactionLimitsBackendDelegate transactionLimitsBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(TransactionLimitsBackendDelegate.class);
		return transactionLimitsBackendDelegate.fetchNewConvertedAmount(amount, transactionCurrency, baseCurrency, request);
}

	public HashMap<String, Object>  payloadForActualServiceOld(Map<String, Object> inputParams, String transactionid) {
		HashMap<String, Object>externalServicepayload = new HashMap<String, Object>();
		HashMap<String, Object>payload = new HashMap<String, Object>();
		String transactionCurrency=inputParams.get("transactionCurrency")!=null?inputParams.get("transactionCurrency").toString():"";
		if(transactionCurrency.equalsIgnoreCase("NPR")) {
		externalServicepayload.put("amount", inputParams.get("transactionAmount"));
		}else if(transactionCurrency.equalsIgnoreCase("USD")) {
		externalServicepayload.put("amount", inputParams.get("convertedAmount"));
		}
		externalServicepayload.put("currency", "NPR"/*inputParams.get("transactionCurrency")*/);
		externalServicepayload.put("debtorAgent", inputParams.get("debtorAgent"));
		externalServicepayload.put("debtorBranch", inputParams.get("debtorBranch"));
		externalServicepayload.put("debtorName", inputParams.get("debtorName"));
		externalServicepayload.put("debtorAccount", inputParams.get("fromAccountNumber"));
		externalServicepayload.put("creditorAgent", inputParams.get("bankId"));
		externalServicepayload.put("creditorBranch", inputParams.get("creditorBranch")!=null?inputParams.get("creditorBranch").toString(): "1");
		externalServicepayload.put("creditorName", inputParams.get("beneficiaryName"));
		externalServicepayload.put("creditorAccount", inputParams.get("toAccountNumber"));
		externalServicepayload.put("remarks", inputParams.get("transactionsNotes"));
		// Extra data for Intrabank Transaction won't used in external service call
		if(StringUtils.isNotBlank(transactionid)) {
		inputParams.put("interbankDbxTransactionId", transactionid);
		externalServicepayload.put("interbankDbxTransactionId", transactionid);
		}
		payload.put("internalServicePayload", inputParams);
		payload.put("externalServicepayload", externalServicepayload);
		LOG.debug("InterBankFundTransferResourceImplExtn:payloadForActualService:"+payload);
		return payload;
		
	}
	public HashMap<String, Object>  payloadForActualService(Map<String, Object> inputParams, String requestId) throws Exception {
		HashMap<String, Object> payload = new HashMap<String, Object>();
		HashMap<String, Object>internalPayload = (HashMap<String, Object>) inputParams;
		internalPayload.put("transactionRefNo", requestId);	
		String transactionCurrency=inputParams.get("transactionCurrency")!=null?inputParams.get("transactionCurrency").toString():"";
		String amount= "";
		if(transactionCurrency.equalsIgnoreCase("NPR")) {
			amount=inputParams.get("transactionAmount").toString();
		}else if(transactionCurrency.equalsIgnoreCase("USD")) {
			amount=inputParams.get("convertedAmount").toString();
		}
		HashMap<String, Object> externalServicepayload = internalPayload;
		externalServicepayload.put("amount", amount);
		try {
		externalServicepayload = GenerateNCHLPayload.prepareOtherBankTransferPayload(externalServicepayload, null);
		}catch (ApplicationException e) {
			throw new Exception(e.getMessage());
		}
		externalServicepayload.put("transactionRefNo", requestId);
		payload.put("internalServicePayload", internalPayload);
		payload.put("externalServicepayload", externalServicepayload);
		return payload;
		
	}
@Override
public Result updateStatus(String MethodID, Object[] inputArray, DataControllerRequest dcRequest,
		DataControllerResponse dcResponse) {

	Result result = new Result();
	InterBankFundTransferDTO interbanktransferDTO = null;

	@SuppressWarnings("unchecked")
	HashMap<String, Object> requestParams = (HashMap<String, Object>) inputArray[1];

	String transactionId = (String) requestParams.get("transactionId");
	String confirmationNumber = (String) requestParams.get("confirmationNumber");
	String status = (String) requestParams.get("status");

	if (transactionId == null || transactionId == "") {
		return ErrorCodeEnum.ERR_12026.setErrorCode(result);
	}
	if (confirmationNumber == null || confirmationNumber == "") {
		return ErrorCodeEnum.ERR_20698.setErrorCode(result);
	}
	if (status == null || status == "") {
		return ErrorCodeEnum.ERR_20699.setErrorCode(result);
	}

	try {
		// Initialize the required BusinessDelegate class to perform the operation
		InterBankFundTransferBusinessDelegate interbanktransferDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(InterBankFundTransferBusinessDelegate.class);
		interbanktransferDTO = interbanktransferDelegate.updateStatusUsingTransactionId(transactionId, status, confirmationNumber);
		if (interbanktransferDTO == null) {
			return ErrorCodeEnum.ERR_12000.setErrorCode(result);
		}
		JSONObject interbanktransferJSON = new JSONObject(interbanktransferDTO);
		result = JSONToResult.convert(interbanktransferJSON.toString());
	} catch (Exception exp) {
		alert.prepareError("Exception occured while invoking interbanktransaction business delegate", exp).log();
		return ErrorCodeEnum.ERR_12000.setErrorCode(result);
	}
	return result;
}

@Override
public Result processResponseFromLineOfBusiness(Result result, DataControllerRequest request,
		DataControllerResponse response) {
	/*
	HashMap<String, Object> inputParams = new HashMap<String, Object>();

	String transactionId = request.getParameter("dbxtransactionId");
	Param referenceId = result.getParamByName("referenceId");
	TransactionStatusEnum status = null;
	String confirmationNumber = "";

	InterBankFundTransferBusinessDelegate interbankBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
			.getFactoryInstance(BusinessDelegateFactory.class)
			.getBusinessDelegate(InterBankFundTransferBusinessDelegate.class);

	if (referenceId != null) {
		status = TransactionStatusEnum.EXECUTED;
		confirmationNumber = referenceId.getValue();
	} else {
		status = TransactionStatusEnum.FAILED;
	}
	inputParams.put("status", status.getStatus());
	interbankBusinessDelegate.updateStatusUsingTransactionId(transactionId, status.getStatus(), confirmationNumber);

	result.addParam(new Param("dbxtransactionId", transactionId));
	*/
	return result;
}

@Override
public Result editTransaction(String methodID, Object[] inputArray, DataControllerRequest request,
		DataControllerResponse response) {

	@SuppressWarnings("unchecked")
	Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];

	InterBankFundTransferBusinessDelegate interbankTransactionDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(InterBankFundTransferBusinessDelegate.class);
	InterBankFundTransferBackendDelegate interbankBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(InterBankFundTransferBackendDelegate.class);
	AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
	ApprovalQueueBusinessDelegate approvalQueueDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
	AccountBusinessDelegate accountBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AccountBusinessDelegate.class);
	
	String featureActionId = FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE;
	InterBankFundTransferDTO interbankDTO = null;
	Result result = new Result();
	String confirmationNumber = null;

	Map<String, Object> customer = CustomerSession.getCustomerMap(request);

	String createdby = CustomerSession.getCustomerId(customer);
	String legalEntityId = (String) customer.get("legalEntityId");
	boolean isBusinessUser = CustomerSession.IsBusinessUser(customer);

	Object transactionIdParam = inputParams.get("confirmationNumber");
	Object confirmationNumberParam = inputParams.get("transactionId") == null ? transactionIdParam
			: inputParams.get("transactionId");

	String fromAccountNumber = inputParams.get("fromAccountNumber").toString();
	
	CustomerAccountsDTO account = accountBusinessDelegate.getAccountDetails(createdby, fromAccountNumber);
	String contractId = account.getContractId();
	String coreCustomerId = account.getCoreCustomerId();
	String companyId = account.getOrganizationId();
	
	if (confirmationNumberParam != null && !"".equals(confirmationNumberParam.toString())) {
		confirmationNumber = confirmationNumberParam.toString();
		interbankDTO = interbankTransactionDelegate.fetchExecutedTranscationEntry(confirmationNumber + "", Arrays.asList(companyId), createdby, legalEntityId);
		if (interbankDTO == null) {
			interbankDTO = interbankBackendDelegate.fetchTransactionById(confirmationNumber, request);
			if(interbankDTO == null) {
				alert.prepareError("Record doesn't exist").log();
				return ErrorCodeEnum.ERR_12003.setErrorCode(result);
			}else{
			    interbankDTO.setCompanyId(companyId);
			    interbankDTO.setCreatedby(createdby);
			    interbankDTO.setFeatureActionId(featureActionId);
                if (StringUtils.isNotBlank(inputParams.get("amount").toString())) {
                    interbankDTO.setAmount(Double.parseDouble(inputParams.get("amount").toString()));
                    interbankDTO.setTransactionAmount(inputParams.get("amount").toString());
                }
			    interbankDTO = interbankTransactionDelegate.createTransactionAtDBX(interbankDTO);
			}
		}
	} else {
		alert.prepareError("confirmationNumber is missing in the payload which is mandatory for edit").log();
		return ErrorCodeEnum.ERR_12026.setErrorCode(new Result());
	}

	String transactionId = interbankDTO.getTransactionId();
	String oldRequestId = interbankDTO.getRequestId();

	// Authorization checks on transcationId
	/*
	if (interbankDTO.getCompanyId() != null && !interbankDTO.getCompanyId().equals(companyId)) {
		return ErrorCodeEnum.ERR_12001.setErrorCode(result);
	} else if (!isBusinessUser && !CustomerSession.IsCombinedUser(customer)
			&& !interbankDTO.getCreatedby().equals(createdby)) {
		return ErrorCodeEnum.ERR_12001.setErrorCode(result);
	}
	*/
	// Authorization checks on prevfromAccountNumber and featureactionId
	String prevfromAccountNumber = interbankDTO.getFromAccountNumber();

	if (!authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(createdby, featureActionId,
			prevfromAccountNumber, CustomerSession.IsCombinedUser(customer))) {
		return ErrorCodeEnum.ERR_12001.setErrorCode(result);
	}

	InterBankFundTransferDTO requestDTO = null;
	inputParams.put("createdby", createdby);
	inputParams.put("companyId", companyId);
	inputParams.put("roleId", customerDelegate.getUserContractCustomerRole(contractId, coreCustomerId, createdby));

	try {
		requestDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), InterBankFundTransferDTO.class);
		requestDTO.setConfirmationNumber(null);
		requestDTO.setTransactionId(null);
		interbankDTO = interbankDTO.updateValues(requestDTO);
	} catch (IOException e) {
		alert.prepareError("Error occured while fetching the input params: " , e).log();
		return ErrorCodeEnum.ERR_10549.setErrorCode(new Result());
	}

	if (interbankDTO == null) {
		alert.prepareError("Error occured while fetching the input params: ").log();
		return ErrorCodeEnum.ERR_10549.setErrorCode(new Result());
	}

	// Authorization checks on newfromAccountNumber and featureactionId
	String newfromAccountNumber = interbankDTO.getFromAccountNumber();

	if (!authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(createdby, featureActionId,
			newfromAccountNumber, CustomerSession.IsCombinedUser(customer))) {
		return ErrorCodeEnum.ERR_12001.setErrorCode(result);
	}

	String date = interbankDTO.getScheduledDate() == null
			? (interbankDTO.getProcessingDate() == null
					? (interbankDTO.getFrequencyStartDate() == null ? application.getServerTimeStamp()
							: interbankDTO.getFrequencyStartDate())
					: interbankDTO.getProcessingDate())
			: interbankDTO.getScheduledDate();

	// Transaction limit checks on amount and featureactionId
	Double amount = interbankDTO.getAmount();
	String transactionAmount = inputParams.get("transactionAmount") != null ? inputParams.get("transactionAmount").toString() : null;
    String serviceCharge = inputParams.get("serviceCharge") != null ? inputParams.get("serviceCharge").toString() : null;
    
    if("0".equals(transactionAmount) || "0.0".equals(transactionAmount) || "0.00".equals(transactionAmount)) {
		transactionAmount = amount + "";
	}
	
	String validate = inputParams.get("validate") == null ? null : inputParams.get("validate").toString();
	
	if("true".equalsIgnoreCase(validate)) {
		InterBankFundTransferBackendDTO backendObj = new InterBankFundTransferBackendDTO();
		backendObj =  backendObj.convert(interbankDTO);
		InterBankFundTransferDTO validateownaccountDTO = interbankBackendDelegate.validateTransaction(backendObj,request);
		try {
			 result = JSONToResult.convert(new JSONObject(validateownaccountDTO).toString());
			 return result;
		} catch (JSONException e) {
			alert.prepareError("Error occured while converting the response from Line of Business service for ownaccount transfer: ", e).log();
			return ErrorCodeEnum.ERR_21217.setErrorCode(new Result());
		}
	}
	
	TransactionStatusDTO transactionStatusDTO = new TransactionStatusDTO();
	transactionStatusDTO.setCustomerId(createdby);
	transactionStatusDTO.setCompanyId(companyId);
	transactionStatusDTO.setAccountId(fromAccountNumber);
	transactionStatusDTO.setAmount(amount);
	transactionStatusDTO.setStatus(TransactionStatusEnum.NEW);
	transactionStatusDTO.setDate(date);
	transactionStatusDTO.setTransactionCurrency(interbankDTO.getTransactionCurrency());
	transactionStatusDTO.setFeatureActionID(featureActionId);
	transactionStatusDTO.setRequestId(oldRequestId);
	transactionStatusDTO.setServiceCharge(serviceCharge);
	
	transactionStatusDTO = approvalQueueDelegate.validateForApprovals(transactionStatusDTO, request);
	
	if(transactionStatusDTO == null)
		return ErrorCodeEnum.ERR_29018.setErrorCode(new Result());
	
	if(transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null){
		result.addParam(new Param("dbpErrCode", transactionStatusDTO.getDbpErrCode()));
		result.addParam(new Param("dbpErrMsg", transactionStatusDTO.getDbpErrMsg()));
		return result;
	}

	TransactionStatusEnum transactionStatus = transactionStatusDTO.getStatus();
	
	interbankDTO.setStatus(transactionStatus.getStatus());
	//interbankDTO.setRequestId(null);
	interbankDTO.setConfirmationNumber(confirmationNumber + "");
	interbankDTO.setTransactionAmount(transactionStatusDTO.getTransactionAmount());
	interbankDTO.setServiceCharge(transactionStatusDTO.getServiceCharge());
	try {
		interbankDTO.setAmount(transactionStatusDTO.getAmount().doubleValue());
	} catch (NumberFormatException e) {
		alert.prepareError("Invalid amount value", e).log();
		return ErrorCodeEnum.ERR_27017.setErrorCode(new Result());
	}

	InterBankFundTransferBackendDTO interbankBackendDTO = new InterBankFundTransferBackendDTO();
	interbankBackendDTO = interbankBackendDTO.convert(interbankDTO);

	try {
		interbankBackendDTO.setAmount(Double.parseDouble(interbankBackendDTO.getTransactionAmount()));
	} catch (Exception e) {
		alert.prepareError("Invalid amount value", e).log();
		return ErrorCodeEnum.ERR_27017.setErrorCode(new Result());
	}
	
	String requestObj = null;
	try {
		requestObj = new JSONObject(interbankBackendDTO).toString();
		result = JSONToResult.convert(requestObj);
	} catch (JSONException e) {
		alert.prepareError("Error occured while converting the response from Line of Business service for interbank transfer: ", e).log();
		return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
	}

	String referenceId = null;
	InterBankFundTransferDTO resDTO = null;
	
	String overrides = inputParams.get("overrides") == null ? "" : inputParams.get("overrides").toString();
	interbankBackendDTO.setOverrides(overrides);

	if (transactionStatus == TransactionStatusEnum.SENT) {
		if (confirmationNumber != null && !confirmationNumber.startsWith(Constants.REFERENCE_KEY)) {
			interbankBackendDTO.setTransactionId(confirmationNumber);
			resDTO = interbankBackendDelegate.editTransactionWithoutApproval(interbankBackendDTO, request);
		}
		
		if (resDTO == null) {
			interbankTransactionDelegate.updateStatusUsingTransactionId(transactionId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
			return ErrorCodeEnum.ERR_12600.setErrorCode(result);
		}

		if (resDTO.getDbpErrMsg() != null && !resDTO.getDbpErrMsg().isEmpty()) {
			interbankTransactionDelegate.updateStatusUsingTransactionId(transactionId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
			result.addParam(new Param("errorDetails", resDTO.getErrorDetails()));
			return ErrorCodeEnum.ERR_00000.setErrorCode(result, resDTO.getDbpErrMsg());
		}
		
		referenceId = resDTO.getReferenceId();

		if(StringUtils.isEmpty(referenceId)) {
			interbankTransactionDelegate.updateStatusUsingTransactionId(transactionId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
			return ErrorCodeEnum.ERR_12602.setErrorCode(result);
		}
		
		interbankDTO = resDTO;
		interbankDTO.setConfirmationNumber(referenceId);
		interbankTransactionDelegate.updateStatusUsingTransactionId(transactionId, TransactionStatusEnum.EXECUTED.getStatus(), interbankDTO.getReferenceId());
		
		result.addParam(new Param("referenceId", resDTO.getReferenceId()));
		result.addParam(new Param("status", transactionStatus.getStatus()));
        result.addParam(new Param("message", transactionStatus.getMessage()));
		
	}
	else if(transactionStatus == TransactionStatusEnum.PENDING){

		String requestId = transactionStatusDTO.getRequestId();
		referenceId = confirmationNumber;
		interbankBackendDTO.setTransactionId(confirmationNumber);
		InterBankFundTransferDTO pendingtransactionDTO = interbankBackendDelegate.editTransactionWithApproval(interbankBackendDTO, request);
		if(pendingtransactionDTO == null)
		{
			interbankTransactionDelegate.updateStatusUsingTransactionId(transactionId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
			alert.prepareError("Error occured while creating entry into the backend table: ").log();
			return ErrorCodeEnum.ERR_29017.setErrorCode(new Result());
		}
		if(pendingtransactionDTO.getDbpErrCode() != null || pendingtransactionDTO.getDbpErrMsg() != null) {
			interbankTransactionDelegate.updateStatusUsingTransactionId(transactionId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
			result.addParam(new Param("errorDetails", pendingtransactionDTO.getErrorDetails()));
			return ErrorCodeEnum.ERR_00000.setErrorCode(result, pendingtransactionDTO.getDbpErrMsg());
		}
		
		String backendid = pendingtransactionDTO.getReferenceId();
        interbankTransactionDelegate.updateStatusUsingTransactionId(transactionId,transactionStatus.toString(), backendid);
        transactionStatusDTO = approvalQueueDelegate.updateBackendIdInApprovalQueue(requestId, backendid, transactionStatusDTO.isSelfApproved(), featureActionId, request);
        if(transactionStatusDTO == null) 
		{							
			interbanktransferDelegate.updateStatusUsingTransactionId(transactionId, TransactionStatusEnum.FAILED.getStatus(),backendid);
			return ErrorCodeEnum.ERR_29019.setErrorCode(new Result());
		}	
		if(transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null) {
			interbanktransferDelegate.updateStatusUsingTransactionId(transactionId, TransactionStatusEnum.FAILED.getStatus(), backendid);
			result.addParam(new Param("dbpErrCode", transactionStatusDTO.getDbpErrCode()));
			result.addParam(new Param("dbpErrMsg", transactionStatusDTO.getDbpErrMsg()));
			return result;
		}
		transactionStatus=transactionStatusDTO.getStatus();
		interbankTransactionDelegate.updateStatusUsingTransactionId(transactionId,transactionStatus.toString(), backendid);
		interbankDTO.setConfirmationNumber(backendid);
		interbankDTO.setStatus(transactionStatus.toString());
		
		result.addParam(new Param("requestId", requestId));
		result.addParam(new Param("referenceId", backendid));
		if(pendingtransactionDTO.getMessageDetails() != null) {
            result.addParam(new Param("messageDetails", pendingtransactionDTO.getMessageDetails()));
        }
		
		interbankDTO.setRequestId(requestId);
		if (transactionStatus == TransactionStatusEnum.APPROVED) {
			result.addParam(new Param("status", TransactionStatusEnum.SENT.getStatus()));
			result.addParam(new Param("message", TransactionStatusEnum.SENT.getMessage()));
		}
		else {
			result.addParam(new Param("status", transactionStatus.getStatus()));
			result.addParam(new Param("message", transactionStatus.getMessage()));
		}

	}
	else if(transactionStatus == TransactionStatusEnum.APPROVED){
		result.addParam(new Param("referenceId", transactionStatusDTO.getConfirmationNumber()));
		result.addParam(new Param("status", TransactionStatusEnum.SENT.getStatus()));
		result.addParam(new Param("message", TransactionStatusEnum.SENT.getMessage()));
	}
	if(StringUtils.isNotEmpty(referenceId)) {
		interbankDTO.setTransactionId(transactionId);
		interbankTransactionDelegate.updateTransactionAtDBX(interbankDTO);
	}
	if(interbankDTO.getMessageDetails() != null) {
        result.addParam(new Param("messageDetails", interbankDTO.getMessageDetails()));
    }
	if(interbankDTO.getQuoteCurrency() != null) {
        result.addParam(new Param("quoteCurrency", interbankDTO.getQuoteCurrency()));
    }

	// ADP-7058 update additional meta data
	try{
		approvalQueueDelegate.updateAdditionalMetaForApprovalRequest(transactionStatusDTO.getRequestId(), request);
	} catch(Exception e){
		alert.prepareError(e.toString()).log();
	}

	return result;
}

@Override
public Result cancelScheduledTransactionOccurrence(String methodID, Object[] inputArray,
		DataControllerRequest request, DataControllerResponse response) {

	Result result = new Result();
	String requestId = "";

	String featureActionId = FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL;

	Map<String, Object> customer = CustomerSession.getCustomerMap(request);

	String createdby = CustomerSession.getCustomerId(customer);
	boolean isSMEUser = CustomerSession.IsBusinessUser(customer);

	InterBankFundTransferDTO interbankDTO = null;

	@SuppressWarnings("unchecked")
	Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];

	Object transactionIdParam = inputParams.get("confirmationNumber");
	Object confirmationNumberParam = inputParams.get("transactionId") == null ? transactionIdParam
			: inputParams.get("transactionId");
	String confirmationNumber = null;
	
	ContractBusinessDelegate contractDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);
	List<String> contracts = contractDelegate.fetchContractCustomers(createdby);
	contracts.add(CustomerSession.getCompanyId(customer));

	if (confirmationNumberParam != null && !"".equals(confirmationNumberParam.toString())) {
		confirmationNumber = confirmationNumberParam.toString();
		interbankDTO = interbanktransferDelegate.fetchExecutedTranscationEntry(confirmationNumber, contracts, createdby,null);
		if (interbankDTO == null) {
			interbankDTO = interbankBackendDelegate.fetchTransactionById(confirmationNumber, request);
			if(interbankDTO == null) {
				alert.prepareError("Record doesn't exist").log();
				return ErrorCodeEnum.ERR_12003.setErrorCode(result);
			}
			interbankDTO.setFeatureActionId(FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE);
			interbankDTO.setCreatedby(createdby);
			CustomerAccountsDTO account = accountBusinessDelegate.getAccountDetails(createdby, interbankDTO.getFromAccountNumber());
			String companyId = account.getOrganizationId();
			interbankDTO.setCompanyId(companyId);
			String transactionId = interbankDTO.getTransactionId();
			interbankDTO.setConfirmationNumber(transactionId);
			interbankDTO.setTransactionId(null);
			InterBankFundTransferDTO dbxDTO = interbanktransferDelegate.createTransactionAtDBX(interbankDTO);
			if(dbxDTO != null) {
				interbankDTO.setTransactionId(dbxDTO.getTransactionId());
				interbankDTO.setRequestId(dbxDTO.getRequestId());
			}
		}
	} else {
		alert.prepareError(
				"confirmationNumber or transactionId is missing in the payload which is mandatory for cancel occurrence").log();
		return ErrorCodeEnum.ERR_12026.setErrorCode(result);
	}

	String oldrequestId = interbankDTO.getRequestId();
	String transactionId = interbankDTO.getTransactionId();

	// Authorization checks on transcationId
	/*
	if (interbankDTO.getCompanyId() != null && ! contracts.contains(interbankDTO.getCompanyId())) {
		return ErrorCodeEnum.ERR_12001.setErrorCode(result);
	} else if (!isSMEUser && !CustomerSession.IsCombinedUser(customer)
			&& !interbankDTO.getCreatedby().equals(createdby)) {
		return ErrorCodeEnum.ERR_12001.setErrorCode(result);
	}*/

	// Authorization checks on prevfromAccountNumber and featureactionId
	String prevfromAccountNumber = interbankDTO.getFromAccountNumber();

	if (!authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(createdby, featureActionId,
			prevfromAccountNumber, CustomerSession.IsCombinedUser(customer))) {
		return ErrorCodeEnum.ERR_12001.setErrorCode(result);
	}
	
	TransactionStatusDTO transactionStatusDTO = new TransactionStatusDTO();
	transactionStatusDTO.setCustomerId(createdby);
	transactionStatusDTO.setCompanyId(interbankDTO.getCompanyId());
	transactionStatusDTO.setAccountId(interbankDTO.getFromAccountNumber());
	transactionStatusDTO.setFeatureActionID(featureActionId);
	transactionStatusDTO.setRequestId(oldrequestId);
	transactionStatusDTO.setAmount(interbankDTO.getAmount());
	transactionStatusDTO.setServiceCharge(interbankDTO.getServiceCharge());
	transactionStatusDTO.setTransactionCurrency(interbankDTO.getTransactionCurrency());
	
	transactionStatusDTO = approvalQueueDelegate.validateForApprovals(transactionStatusDTO, request);			
	if(transactionStatusDTO == null) {			
		return ErrorCodeEnum.ERR_29018.setErrorCode(new Result());
	}
	if(transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null){
		result.addParam(new Param("dbpErrCode", transactionStatusDTO.getDbpErrCode()));
		result.addParam(new Param("dbpErrMsg", transactionStatusDTO.getDbpErrMsg()));
		return result;
	}
	TransactionStatusEnum transactionStatus = transactionStatusDTO.getStatus();
	boolean isSelfApproved = transactionStatusDTO.isSelfApproved();
	
	if (transactionStatus == TransactionStatusEnum.SENT) {
		InterBankFundTransferDTO interbanktransactionDTO = new InterBankFundTransferDTO();
		interbanktransactionDTO = interbankBackendDelegate.cancelTransactionWithoutApproval(confirmationNumber, request);					
		if(interbanktransactionDTO == null) {	
			return ErrorCodeEnum.ERR_12600.setErrorCode(result);
		}
		if(interbanktransactionDTO.getDbpErrCode() != null || interbanktransactionDTO.getDbpErrMsg() != null) {
			return ErrorCodeEnum.ERR_00000.setErrorCode(result, interbanktransactionDTO.getDbpErrMsg());
		}
		String referenceId = interbanktransactionDTO.getReferenceId();
		if(referenceId == null || "".equals(referenceId)) {
			return ErrorCodeEnum.ERR_12601.setErrorCode(result);
		}
		result.addParam(new Param("referenceId", referenceId));
		result.addParam(new Param("status", transactionStatus.getStatus()));
        result.addParam(new Param("message", transactionStatus.getMessage()));
	}
	else if(transactionStatus == TransactionStatusEnum.PENDING){
		requestId = transactionStatusDTO.getRequestId();
		InterBankFundTransferDTO interbankpendingtransactionDTO = new InterBankFundTransferDTO();
		interbankpendingtransactionDTO = interbanktransferDelegate.cancelTransactionWithApproval(confirmationNumber, transactionId, request);					
		if(interbankpendingtransactionDTO == null)
		{
			alert.prepareError("Error occured while creating entry into the backend table: ").log();
			return ErrorCodeEnum.ERR_29017.setErrorCode(new Result());
		}
		if(interbankpendingtransactionDTO.getDbpErrCode() != null || interbankpendingtransactionDTO.getDbpErrMsg() != null) {
			return ErrorCodeEnum.ERR_00000.setErrorCode(result, interbankpendingtransactionDTO.getDbpErrMsg());
		}
		confirmationNumber = interbankpendingtransactionDTO.getReferenceId();
		transactionStatusDTO = approvalQueueDelegate.updateBackendIdInApprovalQueue(requestId, confirmationNumber, isSelfApproved, featureActionId, request);
		if(transactionStatusDTO == null) 
		{							
			return ErrorCodeEnum.ERR_29019.setErrorCode(new Result());
		}	
		if(transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null) {
			result.addParam(new Param("dbpErrCode", transactionStatusDTO.getDbpErrCode()));
			result.addParam(new Param("dbpErrMsg", transactionStatusDTO.getDbpErrMsg()));
			return result;
		}
		transactionStatus = transactionStatusDTO.getStatus();
		confirmationNumber = transactionStatusDTO.getConfirmationNumber();
		interbanktransferDelegate.updateRequestId(transactionId, requestId);
		result.addParam(new Param("requestId", requestId));
		result.addParam(new Param("referenceId", confirmationNumber));
		
        if (transactionStatus == TransactionStatusEnum.APPROVED) {
			result.addParam(new Param("status", TransactionStatusEnum.SENT.getStatus()));
			result.addParam(new Param("message", TransactionStatusEnum.SENT.getMessage()));
		}
		else {
			result.addParam(new Param("status", transactionStatus.getStatus()));
			result.addParam(new Param("message", transactionStatus.getMessage()));
		}

	}
	
	result.addParam(new Param("transactionId", confirmationNumber+""));
	try {
		LogEvents.pushAlertsForApprovalRequests( featureActionId, request, response,inputParams, null, confirmationNumber, requestId, CustomerSession.getCustomerName(customer),null);
	} catch (Exception e) {
		alert.prepareError("Failed at pushAlertsForApprovalRequests "+e).log();
	}

	// ADP-7058 update additional meta data
	try{
		approvalQueueDelegate.updateAdditionalMetaForApprovalRequest(transactionStatusDTO.getRequestId(), request);
	} catch(Exception e){
		alert.prepareError(e.toString()).log();
	}
	
	return result;
}

@Override
public Result deleteTransaction(String methodID, Object[] inputArray, DataControllerRequest request,
		DataControllerResponse response) {

	Result result = new Result();

	String featureActionId = FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL;
	String requestId = "";

	Map<String, Object> customer = CustomerSession.getCustomerMap(request);

	String createdby = CustomerSession.getCustomerId(customer);
	String legalEntityId = (String) customer.get("legalEntityId");
	boolean isSMEUser = CustomerSession.IsBusinessUser(customer);

	InterBankFundTransferDTO interbankDTO = null;
	
	String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getValue(Constants.PAYMENT_BACKEND);
	if (PAYMENT_BACKEND.equalsIgnoreCase("STUB")) {
		return stubDeleteTransactionResponse();
	}
	
	@SuppressWarnings("unchecked")
	Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];

	Object transactionIdParam = inputParams.get("confirmationNumber");
	Object confirmationNumberParam = inputParams.get("transactionId") == null ? transactionIdParam : inputParams.get("transactionId");
	String transactionId = null;
	String confirmationNumber = null;
	String transactionType = null;
	String frequencyType = inputParams.get("frequencyType") == null ? null : inputParams.get("frequencyType").toString();
	String paymentType = inputParams.get("paymentType") == null ? null : inputParams.get("paymentType").toString();
	
	ContractBusinessDelegate contractDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);
	List<String> contracts = contractDelegate.fetchContractCustomers(createdby);
	contracts.add(CustomerSession.getCompanyId(customer));

	if (confirmationNumberParam != null && !"".equals(confirmationNumberParam.toString())) {
		confirmationNumber = confirmationNumberParam.toString();
				InterBankFundTransferBusinessDelegateImplExtn interbanktransferDelegate = new InterBankFundTransferBusinessDelegateImplExtn();
			interbankDTO = interbanktransferDelegate.fetchExecutedTranscationEntryInDBX(confirmationNumber, contracts, createdby, legalEntityId);
			if (interbankDTO == null) {
					alert.prepareError("Record doesn't exist").log();
					return ErrorCodeEnum.ERR_12003.setErrorCode(result);
				}
			paymentType=interbankDTO.getPaymentType();
			transactionId=interbankDTO.getRequestId();
			if(StringUtils.isNotBlank(paymentType) && paymentType.equalsIgnoreCase("CIPS")) {
			String fromAccountNumber=interbankDTO.getFromAccountNumber();
			if (!authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(createdby, featureActionId,
					fromAccountNumber, CustomerSession.IsCombinedUser(customer))) {
				return ErrorCodeEnum.ERR_12001.setErrorCode(result);
			}
			//interbanktransferDelegate.deleteTransactionAtDBX(transactionId);
			Map <String, Object> requestParams=new HashMap<String, Object>();
			updateStatusUsingTransactionId(confirmationNumber, TransactionStatusEnum.CANCELLED.getStatus(), confirmationNumber, requestParams);
			result.addParam(new Param("referenceId", transactionId));
			result.addParam(new Param("status", TransactionStatusEnum.CANCELLED.getStatus()));
	        result.addParam(new Param("message", "Your Transaction #"+transactionId+" has been "+ TransactionStatusEnum.CANCELLED.getMessage()));
	        result.addParam(new Param("transactionId", confirmationNumber+""));
	    	inputParams.put("amount", interbankDTO.getAmount());
	    	inputParams.put("scheduledDate", interbankDTO.getScheduledDate());
	    	inputParams.put("fromAccountNumber", interbankDTO.getFromAccountNumber());
	    	inputParams.put("toAccountNumber", interbankDTO.getToAccountNumber());
	    	inputParams.put("isScheduled", interbankDTO.getIsScheduled());
	    	inputParams.put("Frequency", interbankDTO.getFrequencyTypeId());
	    	inputParams.put("NoofRecurrences", interbankDTO.getNumberOfRecurrences());
	    	inputParams.put("TransferDate", interbankDTO.getScheduledDate());
	    	inputParams.put("EndBy", interbankDTO.getFrequencyEndDate());
	    	try {
	    		LogEvents.pushAlertsForApprovalRequests( featureActionId, request, response,inputParams, null, confirmationNumber, requestId, CustomerSession.getCustomerName(customer),null);
	    	} catch (Exception e) {
	    		alert.prepareError("Failed at pushAlertsForApprovalRequests "+e).log();
	    	}
	    	return result;
		}
		interbankDTO = interbanktransferDelegate.fetchExecutedTranscationEntry(confirmationNumber, contracts, createdby, legalEntityId);
		if (interbankDTO == null) {
			interbankDTO = interbankBackendDelegate.fetchTransactionById(confirmationNumber, request);
			if(interbankDTO == null) {
				alert.prepareError("Record doesn't exist").log();
				return ErrorCodeEnum.ERR_12003.setErrorCode(result);
			}
			interbankDTO.setFeatureActionId(FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE);
			interbankDTO.setCreatedby(createdby);
			CustomerAccountsDTO account = accountBusinessDelegate.getAccountDetails(createdby, interbankDTO.getFromAccountNumber());
			String companyId = account.getOrganizationId();
			interbankDTO.setCompanyId(companyId);
			transactionId = interbankDTO.getTransactionId();
			interbankDTO.setConfirmationNumber(transactionId);
			interbankDTO.setTransactionId(null);
			InterBankFundTransferDTO dbxDTO = interbanktransferDelegate.createTransactionAtDBX(interbankDTO);
			if(dbxDTO != null) {
				interbankDTO.setTransactionId(dbxDTO.getTransactionId());
				interbankDTO.setRequestId(dbxDTO.getRequestId());
			}
		}

	} else {
		alert.prepareError("confirmationNumber or transactionId is missing in the payload which is mandatory for delete").log();
		return ErrorCodeEnum.ERR_12026.setErrorCode(result);
	}

	transactionId = interbankDTO.getTransactionId();
	transactionType = interbankDTO.getTransactionType();
	String oldrequestId = interbankDTO.getRequestId();

	// Authorization checks on transcationId
	/*
	if (interbankDTO.getCompanyId() != null && ! contracts.contains(interbankDTO.getCompanyId())) {
		return ErrorCodeEnum.ERR_12001.setErrorCode(result);
	} else if (!isSMEUser && !CustomerSession.IsCombinedUser(customer)
			&& !interbankDTO.getCreatedby().equals(createdby)) {
		return ErrorCodeEnum.ERR_12001.setErrorCode(result);
	}
	*/
	
	// Authorization checks on prevfromAccountNumber and featureactionId
	String prevfromAccountNumber = interbankDTO.getFromAccountNumber();

	if (!authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(createdby, featureActionId,
			prevfromAccountNumber, CustomerSession.IsCombinedUser(customer))) {
		return ErrorCodeEnum.ERR_12001.setErrorCode(result);
	}
	
	TransactionStatusDTO transactionStatusDTO = new TransactionStatusDTO();
	transactionStatusDTO.setCustomerId(createdby);
	transactionStatusDTO.setCompanyId(interbankDTO.getCompanyId());
	transactionStatusDTO.setAccountId(interbankDTO.getFromAccountNumber());
	transactionStatusDTO.setFeatureActionID(featureActionId);
	transactionStatusDTO.setRequestId(oldrequestId);
	transactionStatusDTO.setAmount(interbankDTO.getAmount());
	transactionStatusDTO.setServiceCharge(interbankDTO.getServiceCharge());
	transactionStatusDTO.setTransactionCurrency(interbankDTO.getTransactionCurrency());
	
	transactionStatusDTO = approvalQueueDelegate.validateForApprovals(transactionStatusDTO, request);			
	if(transactionStatusDTO == null) {			
		return ErrorCodeEnum.ERR_29018.setErrorCode(new Result());
	}
	if(transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null){
		result.addParam(new Param("dbpErrCode", transactionStatusDTO.getDbpErrCode()));
		result.addParam(new Param("dbpErrMsg", transactionStatusDTO.getDbpErrMsg()));
		return result;
	}
	TransactionStatusEnum transactionStatus = transactionStatusDTO.getStatus();
	boolean isSelfApproved = transactionStatusDTO.isSelfApproved();
	
	if (transactionStatus == TransactionStatusEnum.SENT) {
		InterBankFundTransferDTO interbanktransactionDTO = new InterBankFundTransferDTO();
		interbanktransactionDTO = interbankBackendDelegate.deleteTransactionWithoutApproval(confirmationNumber, transactionType, frequencyType, request);					
		if(interbanktransactionDTO == null) {	
			return ErrorCodeEnum.ERR_12600.setErrorCode(result);
		}
		if(interbanktransactionDTO.getDbpErrCode() != null || interbanktransactionDTO.getDbpErrMsg() != null) {
			return ErrorCodeEnum.ERR_00000.setErrorCode(result, interbanktransactionDTO.getDbpErrMsg());
		}
		String referenceId = interbanktransactionDTO.getReferenceId();
		if(referenceId == null || "".equals(referenceId)) {
			return ErrorCodeEnum.ERR_12601.setErrorCode(result);
		}
		interbanktransferDelegate.deleteTransactionAtDBX(transactionId);
		result.addParam(new Param("referenceId", referenceId));
		result.addParam(new Param("status", transactionStatus.getStatus()));
        result.addParam(new Param("message", transactionStatus.getMessage()));
	}
	else if(transactionStatus == TransactionStatusEnum.PENDING){
		requestId = transactionStatusDTO.getRequestId();
		InterBankFundTransferDTO interbankpendingtransactionDTO = new InterBankFundTransferDTO();
		interbankpendingtransactionDTO = interbanktransferDelegate.deleteTransactionWithApproval(confirmationNumber, transactionType, frequencyType, transactionId, request);					
		if(interbankpendingtransactionDTO == null)
		{
			alert.prepareError("Error occured while creating entry into the backend table: ").log();
			return ErrorCodeEnum.ERR_29017.setErrorCode(new Result());
		}
		if(interbankpendingtransactionDTO.getDbpErrCode() != null || interbankpendingtransactionDTO.getDbpErrMsg() != null) {
			return ErrorCodeEnum.ERR_00000.setErrorCode(result, interbankpendingtransactionDTO.getDbpErrMsg());
		}
		confirmationNumber = interbankpendingtransactionDTO.getReferenceId();
		transactionStatusDTO = approvalQueueDelegate.updateBackendIdInApprovalQueue(requestId, confirmationNumber, isSelfApproved, featureActionId, request);
		if(transactionStatusDTO == null) 
		{							
			return ErrorCodeEnum.ERR_29019.setErrorCode(new Result());
		}	
		if(transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null) {
			result.addParam(new Param("dbpErrCode", transactionStatusDTO.getDbpErrCode()));
			result.addParam(new Param("dbpErrMsg", transactionStatusDTO.getDbpErrMsg()));
			return result;
		}
		transactionStatus = transactionStatusDTO.getStatus();
		confirmationNumber = transactionStatusDTO.getConfirmationNumber();
		interbanktransferDelegate.updateRequestId(transactionId, requestId);
		result.addParam(new Param("requestId", requestId));
		result.addParam(new Param("referenceId", confirmationNumber));

        if (transactionStatus == TransactionStatusEnum.APPROVED) {
			result.addParam(new Param("status", TransactionStatusEnum.SENT.getStatus()));
			result.addParam(new Param("message", TransactionStatusEnum.SENT.getMessage()));
		}
		else {
			result.addParam(new Param("status", transactionStatus.getStatus()));
			result.addParam(new Param("message", transactionStatus.getMessage()));
		}
	}
	 
	result.addParam(new Param("transactionId", confirmationNumber+""));
	inputParams.put("amount", interbankDTO.getAmount());
	inputParams.put("scheduledDate", interbankDTO.getScheduledDate());
	inputParams.put("fromAccountNumber", interbankDTO.getFromAccountNumber());
	inputParams.put("toAccountNumber", interbankDTO.getToAccountNumber());
	inputParams.put("isScheduled", interbankDTO.getIsScheduled());
	inputParams.put("Frequency", interbankDTO.getFrequencyTypeId());
	inputParams.put("NoofRecurrences", interbankDTO.getNumberOfRecurrences());
	inputParams.put("TransferDate", interbankDTO.getScheduledDate());
	inputParams.put("EndBy", interbankDTO.getFrequencyEndDate());
	try {
		LogEvents.pushAlertsForApprovalRequests( featureActionId, request, response,inputParams, null, confirmationNumber, requestId, CustomerSession.getCustomerName(customer),null);
	} catch (Exception e) {
		alert.prepareError("Failed at pushAlertsForApprovalRequests "+e).log();
	}

	// ADP-7058 update additional meta data
	try{
		approvalQueueDelegate.updateAdditionalMetaForApprovalRequest(transactionStatusDTO.getRequestId(), request);
	} catch(Exception e){
		alert.prepareError(e.toString()).log();
	}
	return result;
}

/**
 * Logs Other Bank Transfer status in auditactivity
 * 
 * @param request
 * @param response
 * @param result
 * @param transactionStatus
 * @param interbankDTO
 * @param referenceId
 * @param requestId
 */
private void _logTransaction(DataControllerRequest request, DataControllerResponse response, Object[] inputArray,
		Result result, TransactionStatusEnum transactionStatus, String referenceId,
		InterBankFundTransferDTO interbankDTO, String requestId) {

	String enableEvents = EnvironmentConfigurationsHandler.getValue(Constants.ENABLE_EVENTS, request);
	if (enableEvents == null || enableEvents.equalsIgnoreCase(Constants.FALSE))
		return;
	try {
		ApproversBusinessDelegate approversBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(ApproversBusinessDelegate.class);
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);

		AuditLog auditLog = new AuditLog();

		String eventType = Constants.MAKE_TRANSFER;
		String eventSubType = "";
		String producer = "Transactions/POST(createTransfer)";
		String statusID = "";
		String frequencyType = result.getParamValueByName(Constants.FREQUENCYTYPE);
		String isScheduled = result.getParamValueByName(Constants.ISSCHEDULED);
		boolean isSMEUser = CustomerSession.IsBusinessUser(customer);
		String fromAccountNumber = "";
		String toAccountNumber = "";
		
		if (request.containsKeyInRequest("validate")) {
            String validate = request.getParameter("validate");
            if(StringUtils.isNotBlank(validate) && validate.equalsIgnoreCase("true"))
                return;
        }

		if (request.containsKeyInRequest("fromAccountNumber")) {
			fromAccountNumber = request.getParameter("fromAccountNumber");
		}
		if (request.containsKeyInRequest("toAccountNumber")) {
			toAccountNumber = request.getParameter("toAccountNumber");
		}
		

		JsonObject customParams = new JsonObject();
		String status="";
		customParams.addProperty(Constants.REFERENCEID, result.getParamValueByName(Constants.REFERENCEID));
		customParams = auditLog.buildCustomParamsForAlertEngine(fromAccountNumber, toAccountNumber, customParams);
		alert.prepareError("InterBankFundTransferResourceImplExtn:_logTransaction:eventType:"+eventType+",eventSubType:"+eventSubType+",statusID:"+statusID+",customParams:"+customParams+",transactionStatus:"+transactionStatus).log();
		eventSubType = auditLog.deriveSubTypeForExternalTransfer(isScheduled, frequencyType,
				FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE);
		alert.prepareError("InterBankFundTransferResourceImplExtn:_logTransaction:eventType:"+eventType+",eventSubType:"+eventSubType).log();
		alert.prepareError("InterBankFundTransferResourceImplExtn:_logTransaction:result:"+ResultToJSON.convert(result)).log();
		List<Param> params = result.getAllParams();
		for (Param param : params) {
			if (request.containsKeyInRequest(param.getName())) {
				continue;
			} else {
				customParams.addProperty(param.getName(), param.getValue());
			}
		}
		String bankId = request.getParameter("bankId")!=null?request.getParameter("bankId"):request.getParameter("creditorAgent")!=null?request.getParameter("creditorAgent"):"";
		String beneficiaryBankName = request.getParameter("beneficiaryBankName");
		customParams.addProperty("FirstName", customer.get("FullName")!=null?customer.get("FullName").toString():"");
		customParams = auditLog.buildCustomParamsForAlertEngine(fromAccountNumber, toAccountNumber, customParams);
		customParams.addProperty("toAccountName", request.getParameter("beneficiaryName"));
		customParams.addProperty("bankName", "Himalayan Bank Limited");
		customParams.addProperty("receiverBankName", beneficiaryBankName);
		customParams.addProperty("customerName", customer.get("FullName")!=null?customer.get("FullName").toString():"");
		customParams.addProperty("toAccountNumber", maskAccountNumber(toAccountNumber, 0, toAccountNumber.length()-4, 'X'));
		customParams.addProperty("fromAccountNumber", maskAccountNumber(fromAccountNumber, 0, toAccountNumber.length()-4, 'X'));
		String scheduledDate= result.getParamValueByName("scheduledDate");
		String amount= result.getParamValueByName(Constants.AMOUNT);
		String transactionAmount= result.getParamValueByName("transactionAmount");
		String serviceCharge= result.getParamValueByName("serviceCharge");
		amount=formatAmount(amount);
		transactionAmount=formatAmount(transactionAmount);
		serviceCharge=formatAmount(serviceCharge);
		String transactionCurrency= request.getParameter("transactionCurrency"); 
		if(StringUtils.isNotBlank(transactionCurrency)) {
		customParams.addProperty(Constants.AMOUNT, transactionCurrency+ " "+amount);
		customParams.addProperty("transactionAmount", transactionCurrency+ " "+transactionAmount);
		customParams.addProperty("serviceCharge", transactionCurrency+ " "+serviceCharge);
		}
		if(scheduledDate!=null)
		customParams.addProperty("scheduledDate", scheduledDate.length()>10?scheduledDate.substring(0, 10):scheduledDate);
		if (transactionStatus.toString().contains("DENIED")) {
			statusID = Constants.SID_EVENT_FAILURE;
			customParams.addProperty(Constants.REFERENCEID, result.getParamValueByName(Constants.REFERENCEID));
		} else {
			switch (transactionStatus) {
			case SENT:
				referenceId = customParams.get("referenceId").getAsString();
				if (interbankDTO == null) {
					statusID = Constants.SID_EVENT_FAILURE;
					status= Constants.STATUS_FAIL.toLowerCase();
				}
				if (interbankDTO.getDbpErrMsg() != null && !interbankDTO.getDbpErrMsg().isEmpty()) {
					statusID = Constants.SID_EVENT_FAILURE;
					status= Constants.STATUS_FAIL.toLowerCase();
				}
				if (referenceId == null || "".equals(referenceId)) {
					statusID = Constants.SID_EVENT_FAILURE;
					status= Constants.STATUS_FAIL.toLowerCase();
				} else {
					statusID = Constants.SID_EVENT_SUCCESS;
					customParams.addProperty(Constants.REFERENCEID, referenceId);
					status= Constants.STATUS_SUCCESS.toLowerCase();
					if (isScheduled.equals("1")) 
						status= "scheduled";
					if (isSMEUser) {
						customParams.addProperty(Constants.APPROVERS, "Pre-Approved");
						customParams.addProperty("approvedBy", "Pre-Approved");
					}
					customParams.addProperty("status", status);
				}
				break;
			case PENDING:
				statusID = Constants.SID_EVENT_SUCCESS;
				customParams.addProperty(Constants.REFERENCEID, referenceId);
				eventSubType = Constants.PENDING_APPROVAL_ + eventSubType;
				List<String> approvers = approversBusinessDelegate.getRequestApproversList(requestId);
				if (approvers == null) {
					customParams.addProperty(Constants.APPROVERS, "");
				} else {
					customParams.addProperty(Constants.APPROVERS, approvers.toString());
				}
				break;
			default:
				break;
			}
		}
		alert.prepareError("InterBankFundTransferResourceImplExtn:_logTransaction:isSMEUser:"+isSMEUser).log();
		if (isSMEUser) {
			customParams.addProperty("approvedBy", "N/A");
			customParams.addProperty("rejectedBy", "N/A");
		}
		
		AdminUtil.addAdminUserNameRoleIfAvailable(customParams, request);
		alert.prepareError("InterBankFundTransferResourceImplExtn:_logTransaction:eventType:"+eventType+",eventSubType:"+eventSubType+",statusID:"+statusID+",customParams:"+customParams).log();
		EventsDispatcher.dispatch(request, response, eventType, eventSubType, producer, statusID, null,
                CustomerSession.getCustomerId(customer), null,  customParams);
	} catch (Exception e) {
		alert.prepareError("Error while pushing to Audit Engine."+e).log();
	}
}

private Result stubCommitResponse(Map<String, Object> inputParams) {
	Result result = new Result();
	result.addStringParam("referenceId", String.format("%06d", new java.util.Date().getTime()%1000000));
    result.addStringParam("status", "Sent");
	result.addStringParam("message", "Success! Your transaction has been completed");
    result.addStringParam("totalAmount",inputParams.containsKey("totalAmount") && inputParams.get("totalAmount")!=null?inputParams.get("totalAmount").toString():"");
    result.addStringParam("charges", "");
    result.addStringParam("fromAccountNumber",inputParams.containsKey("fromAccountNumber") && inputParams.get("fromAccountNumber")!=null? inputParams.get("fromAccountNumber").toString():"");
	result.addStringParam("scheduledDate", inputParams.containsKey("scheduledDate") && inputParams.get("scheduledDate")!=null?inputParams.get("scheduledDate").toString():"");
    result.addStringParam("swiftCode", inputParams.containsKey("swiftCode") && inputParams.get("swiftCode")!=null?inputParams.get("swiftCode").toString():"");
    result.addStringParam("isScheduled", inputParams.containsKey("isScheduled") && inputParams.get("isScheduled")!=null?inputParams.get("isScheduled").toString():"");
    result.addStringParam("fromAccountCurrency", inputParams.containsKey("fromAccountCurrency") && inputParams.get("fromAccountCurrency")!=null?inputParams.get("fromAccountCurrency").toString():"");
    result.addStringParam("frequencyType", inputParams.containsKey("frequencyType") && inputParams.get("frequencyType")!=null?inputParams.get("frequencyType").toString():"");
    result.addStringParam("toAccountNumber", inputParams.containsKey("toAccountNumber") && inputParams.get("toAccountNumber")!=null?inputParams.get("toAccountNumber").toString():"");
    result.addStringParam("transactionCurrency", inputParams.containsKey("transactionCurrency") && inputParams.get("transactionCurrency")!=null?inputParams.get("transactionCurrency").toString():"");
    result.addStringParam("transactionType",inputParams.containsKey("transactionType") && inputParams.get("transactionType")!=null?inputParams.get("transactionType").toString():"");
    result.addStringParam("beneficiaryName", inputParams.containsKey("beneficiaryName") && inputParams.get("beneficiaryName")!=null?inputParams.get("beneficiaryName").toString():"");
    result.addStringParam("paidBy", inputParams.containsKey("paidBy") && inputParams.get("paidBy")!=null?inputParams.get("paidBy").toString():"");
    result.addStringParam("serviceName", "INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE");
    result.addStringParam("transactionAmount", inputParams.containsKey("amount") && inputParams.get("amount")!=null?inputParams.get("amount").toString():"");
	result.addStringParam("featureActionId", "INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE");
	result.addStringParam("transactionId", "123");
    result.addStringParam("beneficiaryBankName",inputParams.containsKey("beneficiaryBankName")&& inputParams.get("beneficiaryBankName")!=null? inputParams.get("beneficiaryBankName").toString():"");
	result.addStringParam("beneficiaryAddressLine1", inputParams.containsKey("beneficiaryAddressLine1") && inputParams.get("beneficiaryAddressLine1")!=null?inputParams.get("beneficiaryAddressLine1").toString():"");
	result.addStringParam("beneficiaryAddressLine2", inputParams.containsKey("beneficiaryAddressLine2") && inputParams.get("beneficiaryAddressLine2")!=null?inputParams.get("beneficiaryAddressLine2").toString():"");
	result.addStringParam("beneficiaryCity", inputParams.containsKey("beneficiaryCity") && inputParams.get("beneficiaryCity")!=null?inputParams.get("beneficiaryCity").toString():"");
	result.addStringParam("beneficiaryZipcode", inputParams.containsKey("beneficiaryZipcode") && inputParams.get("beneficiaryZipcode")!=null?inputParams.get("beneficiaryZipcode").toString():"");
	result.addStringParam("beneficiarycountry", inputParams.containsKey("beneficiarycountry") && inputParams.get("beneficiarycountry")!=null?inputParams.get("beneficiarycountry").toString():"");
    //result.addStringParam("messageDetails", "[{\\\"id\\\":\\\"PI-CHARGE.BEARER.NOT.ALLOWED\\\",\\\"message\\\":\\\"Auto:CHARGE BEARER IS NOT ALLOWED FOR THE PRODUCT\\\"}]");
    return result;
}
public JSONObject getBranchDetails(String bankId, DataControllerRequest dcRequest) {
	JSONArray legacycustomers = new JSONArray();
	String serviceName = "HBLMerchantCRUDService";
	String operationName = "dbxdb_branchdetails_get";
	String benefecieryBankName ="";
	JSONObject branchObj = new JSONObject();
	
	try {
	Map<String, Object> inputParams = new HashMap<>();
	String filter = "bank_cd" + DBPUtilitiesConstants.EQUAL + "'" + bankId+ "'";
	String bankName = "bank_name";
	String branchCode = "branch_cd";
	inputParams.put(DBPUtilitiesConstants.FILTER, filter);
	inputParams.put(DBPUtilitiesConstants.SELECT, bankName+","+branchCode);
	Result response = com.kony.dbx.util.CommonUtils.callIntegrationService(dcRequest, inputParams, dcRequest.getHeaderMap(), serviceName,operationName, false);
	if (response != null) {
		JSONObject responseObj = new JSONObject(ResultToJSON.convert(response));
		 legacycustomers = responseObj.getJSONArray("branchdetails");
		 if(legacycustomers!=null && legacycustomers.length()>0) {
			 branchObj=legacycustomers.getJSONObject(0);
		 }
	}
	}catch (DBPApplicationException e) {
		alert.prepareError("Exception occured while getting the legacy customers in BulkAutoEnrollRetailCustomer"+e.getMessage());
	}catch (Exception e) {
		alert.prepareError("Exception occured while getting the legacy customers in BulkAutoEnrollRetailCustomer"+e.getMessage());
	}
	return branchObj;
}
private Result stubValidateResponse(Map<String, Object> inputParams) {
	Result result = new Result();
	result.addStringParam("referenceId", "12345");
    result.addStringParam("status", "success");
    result.addStringParam("totalAmount",inputParams.get("amount").toString());
    result.addStringParam("charges", "");
    //result.addStringParam("messageDetails", "[{\\\"id\\\":\\\"PI-CHARGE.BEARER.NOT.ALLOWED\\\",\\\"message\\\":\\\"Auto:CHARGE BEARER IS NOT ALLOWED FOR THE PRODUCT\\\"}]");
    return result;
}
private Result stubDeleteTransactionResponse() {
	
	Result result = new Result();
	result.addStringParam("transactionId", String.format("%06d", new java.util.Date().getTime()%1000000));
    result.addStringParam("status", "Sent");
    result.addStringParam("message","Success! Your transaction has been completed");
    //result.addStringParam("messageDetails", "[{\\\"id\\\":\\\"PI-CHARGE.BEARER.NOT.ALLOWED\\\",\\\"message\\\":\\\"Auto:CHARGE BEARER IS NOT ALLOWED FOR THE PRODUCT\\\"}]");
    return result;

}
protected Result verifyPayeeAndUpdateDB(DataControllerRequest request,Map<String, Object> inputParams,Result result, String payeeVerificationStatus, String payeeVerificationErrMsg, boolean isInternationalAccount) {
	String verifyPayee = inputParams.get("payeeVerification") == null ? null : inputParams.get("payeeVerification").toString();
	Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String userId = CustomerSession.getCustomerId(customer);
	String payeeVerificationName="";
		if ("true".equalsIgnoreCase(verifyPayee)) {
			payeeVerificationStatus = "Success";
			Result payeeVerificationResult = PayeeVerificationBackendServicesHelper.fetchVerifyPayeeResponse(request, inputParams);
			if (payeeVerificationResult == null) {
				alert.prepareError("Error occured while invoking payee verification services: ").log();
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
			} else {
				payeeVerificationStatus = payeeVerificationResult.getParamValueByName("payeeVerificationStatus");
				payeeVerificationErrMsg = payeeVerificationResult.getParamValueByName("payeeVerificationErrMsg");
				payeeVerificationName = payeeVerificationResult.getParamValueByName("payeeVerificationName");
			}
		} else if ("false".equalsIgnoreCase(verifyPayee)) {
			payeeVerificationStatus = "Skipped";
		} 
	if (!"".equals(payeeVerificationStatus)) {
		result.addParam(new Param("payeeVerificationStatus", payeeVerificationStatus));
		JSONObject payeeObjects= new JSONObject();
		try {
        payeeObjects  = interbankBackendDelegate.getPayeeIDToUpdatePayeeVerificationStatus(inputParams.get("toAccountNumber").toString(), false, isInternationalAccount, userId);
        if(payeeObjects.has("externalaccount") && payeeObjects.getJSONArray("externalaccount").length() > 0) {
        JSONArray externalAccountArray = payeeObjects.getJSONArray("externalaccount");
        JSONObject externalAccountObject =externalAccountArray.getJSONObject(0);
        String idValue = externalAccountObject.getString("Id");
        interbankBackendDelegate.updatePayeeVerificationStatus(idValue,payeeVerificationStatus);
		}
		}
		catch (JSONException e) {
			alert.prepareError("Error occured while updating payeeVerification status in DB ", e).log();
			return ErrorCodeEnum.ERR_29000.setErrorCode(new Result());
		}
	}
	if (!"".equals(payeeVerificationErrMsg)) {
		result.addParam(new Param("payeeVerificationErrMsg", payeeVerificationErrMsg));
		result.addParam(new Param("payeeVerificationName", payeeVerificationName));
		return result;
	}
	return result;
}
public InterBankFundTransferDTO updateStatusUsingTransactionId(String transactionId, String status, String confirmationNumber, Map<String, Object> extraParams) {

	List<InterBankFundTransferDTO> interbankfundtransferdto = null;
	
	String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
	String operationName = OperationName.DB_INTERBANKFUNDTRANSFERS_UPDATE;
	extraParams.put("transactionId", transactionId);
	extraParams.put("status", status);
	extraParams.put("confirmationNumber", confirmationNumber);
	alert.prepareError("InterBankFundTransferDTO updateStatusUsingTransactionId :requestParams:"+extraParams).log();
	
	try {
		String updateResponse = DBPServiceExecutorBuilder.builder().
				withServiceId(serviceName).
				withObjectId(null).
				withOperationId(operationName).
				withRequestParameters(extraParams).
				build().getResponse();
		JSONObject jsonRsponse = new JSONObject(updateResponse);
		JSONArray interbankJsonArray = CommonUtils.getFirstOccuringArray(jsonRsponse);
		interbankfundtransferdto = JSONUtils.parseAsList(interbankJsonArray.toString(), InterBankFundTransferDTO.class);
	}
	
	catch(JSONException jsonExp) {
		alert.prepareError("JSONExcpetion occured while updating the interbankfundtransfer",jsonExp).log();
		return null;
	}
	catch(Exception exp) {
		alert.prepareError("Excpetion occured while updating the interbankfundtransfer",exp).log();
		return null;
	}
	
	if(interbankfundtransferdto != null && interbankfundtransferdto.size() != 0)
		return interbankfundtransferdto.get(0);
	
	return null;
}
public JSONObject getJsonObjectFromResult(Result result){
    List<Param> params = result.getAllParams();
    JSONObject obj= new JSONObject();
    for(int i=0;i<params.size();i++) {
    obj.put(params.get(i).getName(), params.get(i).getValue());
    }
    return obj;
	}
public String formatAmount(String amount){
	 if(StringUtils.isNotBlank(amount)) {
	try {
	 BigDecimal decimalAmount= new BigDecimal(amount).setScale(2);
    amount=String.valueOf(decimalAmount);
	 }catch (Exception e) {
		 LOG.error("Exception Occured in formatAmount:",e);
	}
	 }
	 return amount;
}
public String maskAccountNumber(String data, int fromIndex, int toIndex, char maskWith) {
   StringBuilder maskedPart = new StringBuilder();
   if(StringUtils.isNotBlank(data)) {
   for(int i=fromIndex; i<toIndex; i++)
       maskedPart.append(maskWith);
   return data.replace(data.substring(fromIndex, toIndex), maskedPart.toString());
   }
   return data;
}
}
