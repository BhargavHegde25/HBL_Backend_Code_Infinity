package com.bct.javaservices;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bct.custom.dto.QRTransactionDTO;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;
import com.dbp.core.util.JSONUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hbl.productservicesExtn.constants.HBLConstants;
import com.hbl.productservicesExtn.dto.IntraBankFundTransferBackendDTOExtn;
import com.hbl.productservicesExtn.impl.IntraBankFundTransferBackendDelegateImplExtn;
import com.hbl.productservicesExtn.utills.GenerateNCHLPayload;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.TokenUtils;
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
import com.temenos.dbx.product.commons.businessdelegate.api.AccountBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.CustomerBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.TransactionLimitsBusinessDelegate;
import com.temenos.dbx.product.commons.dto.CustomerAccountsDTO;
import com.temenos.dbx.product.commons.dto.TransactionStatusDTO;
import com.temenos.dbx.product.commonsutils.AuditLog;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.FeatureAction;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.constants.TransactionStatusEnum;
import com.temenos.dbx.product.payeeservices.constants.PayeeVerificationBackendServicesHelper;
import com.temenos.dbx.product.transactionservices.backenddelegate.api.InterBankFundTransferBackendDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.InterBankFundTransferBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.IntraBankFundTransferBusinessDelegate;
import com.temenos.dbx.product.transactionservices.dto.InterBankFundTransferBackendDTO;
import com.temenos.dbx.product.transactionservices.dto.InterBankFundTransferDTO;
import com.temenos.dbx.product.transactionservices.dto.IntraBankFundTransferDTO;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class QRInterBankFundTransfer {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(QRInterBankFundTransfer.class);

	CustomerBusinessDelegate customerDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(CustomerBusinessDelegate.class);
	ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(ApplicationBusinessDelegate.class);
	TransactionLimitsBusinessDelegate limitsDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(TransactionLimitsBusinessDelegate.class);
	InterBankFundTransferBusinessDelegate interbanktransferDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(InterBankFundTransferBusinessDelegate.class);
	InterBankFundTransferBackendDelegate interbankBackendDelegate = DBPAPIAbstractFactoryImpl
			.getBackendDelegate(InterBankFundTransferBackendDelegate.class);
	AccountBusinessDelegate accountBusinessDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(AccountBusinessDelegate.class);
	ApprovalQueueBusinessDelegate approvalQueueDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
	AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
			.getFactoryInstance(BusinessDelegateFactory.class)
			.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);

	private String PayableAccountId = "";
	private static final String REVERSE_TRANSACTION_SERVICE = "HBL-T24ISPaymentOrders";
	private static final String REVERSE_TRANSACTION_OPEARATION = "reverseTransaction";
	private static final String GET_TRANSACTION_STAUS_SERVICE_ORCH = "T24-IS-TransactOrch";
	private static final String GET_TRANSACTION_STAUS_OPERATION = "getTransactionStatus";
	private static final String PARKING_ACCOUNT_TRANSFER = "PARKING_ACCOUNT_TRANSFER";

	public Result createTransaction(String methodID, Object[] inputArray, Map<String, Object> inputParams,
			DataControllerRequest request, DataControllerResponse response, QRTransactionDTO qrTransactionDto) {
		@SuppressWarnings("unchecked")
		// Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		InterBankFundTransferDTO interbankDTO = null;
		Result result = new Result();
		Double amount = null;

		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String createdby = CustomerSession.getCustomerId(customer);
		LOG.debug("QRInterBankFundTransfer createdby:::" + createdby);
		String featureActionId = null;

		String legalEntityId = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(request);
		if (StringUtils.isEmpty(legalEntityId))
			legalEntityId = (String) customer.get("legalEntityId");
		LOG.debug("QRInterBankFundTransfer legalEntityId:::" + legalEntityId);
		String validate = inputParams.get("validate") == null ? null : inputParams.get("validate").toString();
		LOG.debug("QRInterBankFundTransfer validate:::" + validate);
		String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getValue(Constants.PAYMENT_BACKEND);
		if (PAYMENT_BACKEND.equalsIgnoreCase("STUB")) {
			if ("true".equalsIgnoreCase(validate)) {
				return stubValidateResponse(inputParams);
			} else {
				return stubCommitResponse(inputParams);
			}
		}
		LOG.debug("QRInterBankFundTransfer inputParams:::" + inputParams);
		String amountValue = inputParams.get("amount").toString();
		String fromAccountNumber = inputParams.get("fromAccountNumber") != null
				? inputParams.get("fromAccountNumber").toString()
				: inputParams.get("debtorAccount").toString();
		inputParams.put("fromAccountNumber", fromAccountNumber);
		String toAccountNumber = inputParams.get("toAccountNumber") != null
				? inputParams.get("toAccountNumber").toString()
				: inputParams.get("creditorAccount").toString();
		inputParams.put("toAccountNumber", toAccountNumber);
		inputParams.put("ExternalAccountNumber", toAccountNumber);
		String beneficiaryName = inputParams.get("beneficiaryName") != null
				? inputParams.get("beneficiaryName").toString()
				: inputParams.get("creditorName").toString();
		inputParams.put("beneficiaryName", beneficiaryName);
		String transactionsNotes = inputParams.get("transactionsNotes") != null
				? inputParams.get("transactionsNotes").toString()
				: inputParams.get("remarks").toString();
		inputParams.put("transactionsNotes", transactionsNotes);
		String frequencyType = inputParams.get("frequencyType") == null ? null
				: inputParams.get("frequencyType").toString();
		String bankId = inputParams.get("bankId") != null ? inputParams.get("bankId").toString()
				: inputParams.get("creditorAgent") != null ? inputParams.get("creditorAgent").toString() : "";
		inputParams.put("bankId", bankId);
		inputParams.put("creditorAgent", bankId);
		String creditorBranch = inputParams.get("creditorBranch") != null ? inputParams.get("creditorBranch").toString()
				: HBLConstants.CIPS_CREDITOR_BRANCH;

		// Receiver Bank name get it from database beneficiaryBankName based on bank id
		String beneficiaryBankName = request.getParameter("beneficiaryBankName");
		if (StringUtils.isNotBlank(bankId)) {
			JSONObject branchObj = getBranchDetails(bankId, request);
			LOG.debug("InterBankFundTransferResourceImplExtn:branchObj:" + branchObj);
			beneficiaryBankName = branchObj.optString("bank_name");
			creditorBranch = branchObj.optString("branch_cd");
		}
		inputParams.put("beneficiaryBankName", beneficiaryBankName);
		inputParams.put("creditorBranch", creditorBranch);
		request.addRequestParam_("beneficiaryBankName", beneficiaryBankName);

		CustomerAccountsDTO account = accountBusinessDelegate.getAccountDetails(createdby, fromAccountNumber);
		String contractId = account.getContractId();
		String coreCustomerId = account.getCoreCustomerId();
		String companyId = account.getOrganizationId();
		LOG.debug("QRInterBankFundTransfer contractId:::" + contractId);
		LOG.debug("QRInterBankFundTransfer coreCustomerId:::" + coreCustomerId);
		LOG.debug("QRInterBankFundTransfer companyId:::" + companyId);

		String baseCurrency;
		try {
			baseCurrency = LegalEntityUtil.getCurrencyForLegalEntity(legalEntityId);
			if (StringUtils.isEmpty(baseCurrency)) {
				baseCurrency = application.getBaseCurrencyFromCache();
			}
		} catch (Exception e) {
			alert.prepareError("Exception occured while fetching base currency from Legal Entity" + e).log();
			return ErrorCodeEnum.ERR_27001.setErrorCode(new Result());
		}
		LOG.debug("QRInterBankFundTransfer baseCurrency:::" + baseCurrency);

		String transactionCurrency = inputParams.get("transactionCurrency") != null
				? inputParams.get("transactionCurrency").toString()
				: baseCurrency;
		inputParams.put("transactionCurrency", transactionCurrency);
		String serviceCharge = inputParams.get("serviceCharge") != null ? inputParams.get("serviceCharge").toString()
				: null;

		if (amountValue == null || amountValue == "") {
			return ErrorCodeEnum.ERR_12031.setErrorCode(new Result());
		}

		featureActionId = FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE;

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
		LOG.debug("QRInterBankFundTransfer inputParams:::" + inputParams);

		try {
			interbankDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), InterBankFundTransferDTO.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: ", e).log();
			return ErrorCodeEnum.ERR_28021.setErrorCode(new Result());
		}

		String date = interbankDTO.getScheduledDate() == null
				? (interbankDTO.getProcessingDate() == null
						? (interbankDTO.getFrequencyStartDate() == null ? application.getServerTimeStamp()
								: interbankDTO.getFrequencyStartDate())
						: interbankDTO.getProcessingDate())
				: interbankDTO.getScheduledDate();

		String backendid = inputParams.get("transactionId") == null
				|| (StringUtils.isEmpty(inputParams.get("transactionId").toString())) ? null
						: inputParams.get("transactionId").toString();
		String beneficiaryId = inputParams.get("beneficiaryId") == null ? null
				: inputParams.get("beneficiaryId").toString();
		String requestid = "";

		if ("true".equalsIgnoreCase(validate)) {
			InterBankFundTransferBackendDTO interBankFundTransferBackendDTO = new InterBankFundTransferBackendDTO();
			interBankFundTransferBackendDTO = interBankFundTransferBackendDTO.convert(interbankDTO);

			InterBankFundTransferDTO validateinterbankDTO = interbankBackendDelegate
					.validateTransaction(interBankFundTransferBackendDTO, request);
			try {
				result = JSONToResult.convert(new JSONObject(validateinterbankDTO).toString());
				return result;
			} catch (JSONException e) {
				alert.prepareError(
						"Error occured while converting the response from Line of Business service for interbank transfer: ",
						e).log();
				return ErrorCodeEnum.ERR_21217.setErrorCode(new Result());
			}
		}
		String payeeVerificationStatus = "";
		String payeeVerificationErrMsg = "";
		verifyPayeeAndUpdateDB(request, inputParams, result, payeeVerificationStatus, payeeVerificationErrMsg, false);
		payeeVerificationStatus = null != result.getParamValueByName("payeeVerificationStatus")
				? result.getParamValueByName("payeeVerificationStatus")
				: "";
		payeeVerificationErrMsg = null != result.getParamValueByName("payeeVerificationErrMsg")
				? result.getParamValueByName("payeeVerificationErrMsg")
				: "";
		if (payeeVerificationStatus.equalsIgnoreCase("Failure")) {
			return result;
		}
		String convertedAmount = inputParams.get("convertedAmount") != null
				? inputParams.get("convertedAmount").toString()
				: null;
		TransactionStatusDTO transactionStatusDTO = new TransactionStatusDTO();
		transactionStatusDTO.setCustomerId(createdby);
		transactionStatusDTO.setCompanyId(companyId);
		transactionStatusDTO.setAccountId(fromAccountNumber);
		transactionStatusDTO.setAmount(amount);
		transactionStatusDTO.setStatus(TransactionStatusEnum.NEW);
		transactionStatusDTO.setDate(date);
		transactionStatusDTO.setTransactionCurrency("NPR");
		transactionStatusDTO.setFeatureActionID(featureActionId);
		transactionStatusDTO.setConfirmationNumber(backendid);
		transactionStatusDTO.setServiceCharge(serviceCharge);
		transactionStatusDTO.setConvertedAmount(convertedAmount);

		LOG.debug("InterBankFundTransferResourceImplExtn: transactionStatusDTO:" + transactionStatusDTO.toString());
		TransactionStatusEnum transactionStatus = TransactionStatusEnum.SENT;
		LOG.debug("QRInterBankFundTransfer: transactionStatus:::" + transactionStatus.toString());
		interbankDTO.setStatus(transactionStatus.getStatus());
		qrTransactionDto.setStatus(transactionStatus.getStatus());
		if (transactionStatusDTO.getRequestId() != null) {
			interbankDTO.setRequestId(transactionStatusDTO.getRequestId());
		}
		LOG.debug("InterBankFundTransferResourceImplExtn: transactionStatusDTO:" + transactionStatusDTO.getRequestId());

		String confirmationNumber = (StringUtils.isEmpty(backendid))
				? Constants.REFERENCE_KEY + transactionStatusDTO.getRequestId()
				: backendid;
		LOG.debug("QRInterBankFundTransfer: confirmationNumber:::" + confirmationNumber);
		interbankDTO.setConfirmationNumber(confirmationNumber);
		interbankDTO.setAmount(amount);
		interbankDTO.setServiceCharge(transactionStatusDTO.getServiceCharge());
		LOG.debug("InterBankFundTransferResourceImplExtn: transactionStatusDTO:"
				+ transactionStatusDTO.getServiceCharge());
		interbankDTO.setLegalEntityId(legalEntityId);
		interbankDTO.setBankId(bankId);
		if (inputParams.get("beneficiaryBankName") != null)
			interbankDTO.setBankName(inputParams.get("beneficiaryBankName").toString());
		String channel = "";
		try {
			UserAgentUtil ua = new UserAgentUtil(request);
			channel = ua.getChannel();
		} catch (Exception e) {
			return ErrorCodeEnum.ERR_10163.setErrorCode(new Result());
		}
		LOG.debug("HBL::QRInterBankFundTransfer channel:::" + channel);
		if (channel.equalsIgnoreCase("desktop")) {
			channel = HBLConstants.ONLINE_BANKING;
		} else if (channel.equalsIgnoreCase("mobile")) {
			channel = HBLConstants.MOBILE_BANKING;
		}
		interbankDTO.setPaidBy(channel);
		String requestId = HelperMethods.getRandomNumericString(10);
		interbankDTO.setRequestId(requestId);
		interbankDTO.setProfileId("QRPayment");
		Date scheduledDate = new Date();
		SimpleDateFormat scheduledDateformatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String scheduledDateStr = scheduledDateformatter.format(scheduledDate);
		interbankDTO.setScheduledDate(scheduledDateStr);
		LOG.debug("HBL::QRInterBankFundTransfer scheduledDateStr:::" + scheduledDateStr);
		interbankDTO.setFrequencyStartDate("");
		interbankDTO.setFrequencyEndDate("");
		HashMap<String, Object> payload = null;
		try {
			payload = payloadForActualService(inputParams, requestId);
		} catch (Exception e) {
			return ErrorCodeEnum.ERR_10703.setErrorCode(result, e.getMessage());
		}
		interbankDTO.setExternalApiPayload((HashMap<String, Object>) payload.get("externalServicepayload"));
		String payloadString = new Gson().toJson(payload.get("externalServicepayload"));
		LOG.debug("QRInterBankFundTransfer: payloadString:::" + payloadString);
		qrTransactionDto.setExternalServiceRequest(payloadString);
		InterBankFundTransferDTO interbankdbxDTO = createTransactionAtDBX(interbankDTO);
		LOG.debug("QRInterBankFundTransfer: interbankdbxDTO:::" + interbankdbxDTO.toString());

		if (interbankdbxDTO.getDbpErrCode() != null || interbankdbxDTO.getDbpErrMsg() != null) {
			result.addParam(new Param("dbpErrCode", interbankdbxDTO.getDbpErrCode()));
			result.addParam(new Param("dbpErrMsg", interbankdbxDTO.getDbpErrMsg()));
			return result;
		}

		interbankdbxDTO.setValidate(validate);

		InterBankFundTransferBackendDTO interbankBackendDTO = new InterBankFundTransferBackendDTO();
		interbankBackendDTO = interbankBackendDTO.convert(interbankdbxDTO);

		String creditValueDate = inputParams.get("creditValueDate") == null ? ""
				: inputParams.get("creditValueDate").toString();
		String totalAmount = inputParams.get("totalAmount") == null ? "" : inputParams.get("totalAmount").toString();
		String exchangeRate = inputParams.get("exchangeRate") == null ? "" : inputParams.get("exchangeRate").toString();
		String intermediaryBicCode = inputParams.get("intermediaryBicCode") == null ? ""
				: inputParams.get("intermediaryBicCode").toString();
		String clearingCode = inputParams.get("clearingCode") == null ? "" : inputParams.get("clearingCode").toString();
		String e2eReference = inputParams.get("e2eReference") == null ? "" : inputParams.get("e2eReference").toString();
		String overrides = inputParams.get("overrides") == null ? "" : inputParams.get("overrides").toString();
		// String beneficiaryBankName = inputParams.get("beneficiaryBankName") == null ?
		// ""
		// : inputParams.get("beneficiaryBankName").toString();
		String beneficiaryAddressLine2 = inputParams.get("beneficiaryAddressLine2") == null ? ""
				: inputParams.get("beneficiaryAddressLine2").toString();
		String beneficiaryPhone = inputParams.get("beneficiaryPhone") == null ? ""
				: inputParams.get("beneficiaryPhone").toString();
		String beneficiaryEmail = inputParams.get("beneficiaryEmail") == null ? ""
				: inputParams.get("beneficiaryEmail").toString();
		String beneficiaryState = inputParams.get("beneficiaryState") == null ? ""
				: inputParams.get("beneficiaryState").toString();
		String beneficiaryAddressLine1 = inputParams.get("beneficiaryAddressLine1") == null ? ""
				: inputParams.get("beneficiaryAddressLine1").toString();
		String beneficiaryCity = inputParams.get("beneficiaryCity") == null ? ""
				: inputParams.get("beneficiaryCity").toString();
		String beneficiaryZipcode = inputParams.get("beneficiaryZipcode") == null ? ""
				: inputParams.get("beneficiaryZipcode").toString();
		String beneficiarycountry = inputParams.get("beneficiarycountry") == null ? ""
				: inputParams.get("beneficiarycountry").toString();
		String localInstrumentProprietary = inputParams.get("localInstrumentProprietary") == null ? ""
				: inputParams.get("localInstrumentProprietary").toString();
		String purposeCode = inputParams.get("purposeCode") == null ? "" : inputParams.get("purposeCode").toString();
		String clearingIdentifierCode = inputParams.get("clearingIdentifierCode") == null ? ""
				: inputParams.get("clearingIdentifierCode").toString();
		String serviceLevelProprietary = inputParams.get("serviceLevelProprietary") == null ? ""
				: inputParams.get("serviceLevelProprietary").toString();

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
					"Error occured while converting the response from Line of Business service for interbank transfer: ",
					e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		String transactionid = interbankdbxDTO.getTransactionId();
		InterBankFundTransferDTO interbanktransactionDTO = new InterBankFundTransferDTO();

		String createWithPaymentId = inputParams.get("createWithPaymentId") == null ? ""
				: inputParams.get("createWithPaymentId").toString();

		if (transactionStatus == TransactionStatusEnum.SENT) {
			if (StringUtils.isEmpty(backendid)
					|| (StringUtils.isNotBlank(backendid) && createWithPaymentId.equalsIgnoreCase("true"))) {
				if (StringUtils.isNotBlank(backendid) && createWithPaymentId.equalsIgnoreCase("true")) {
					interbankBackendDTO.setTransactionId(backendid);
					String charges = inputParams.get("charges") == null ? null : inputParams.get("charges").toString();
					interbankBackendDTO.setCharges(charges);
				} else {
					interbankBackendDTO.setTransactionId(null);
				}
				LOG.debug("QRInterBankFundTransfer InterBankFundTransfer DBXDB interBankTransTxID:::" + transactionid);
				try {
					payload = payloadForActualService(inputParams, requestId);
				} catch (Exception e) {
					return ErrorCodeEnum.ERR_10703.setErrorCode(result, e.getMessage());
				}
				interbankBackendDTO
						.setExternalApiPayload((HashMap<String, Object>) payload.get("externalServicepayload"));
				interbankBackendDTO
						.setInternalApiPayload((HashMap<String, Object>) payload.get("internalServicePayload"));
				interbanktransactionDTO = createTransactionWithoutApproval(interbankBackendDTO, request,
						qrTransactionDto);
				if (interbanktransactionDTO == null) {
					qrTransactionDto.setStatus(TransactionStatusEnum.FAILED.getStatus());
					interbanktransferDelegate.updateStatusUsingTransactionId(transactionid,
							TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
					return ErrorCodeEnum.ERR_12601.setErrorCode(result);
				}
			} else {
				String frequency = StringUtils.isEmpty(interbankDTO.getFrequencyTypeId()) ? null
						: interbankDTO.getFrequencyTypeId();
				interbanktransactionDTO = interbanktransferDelegate.approveTransaction(backendid, request, frequency);
				if (interbanktransactionDTO == null) {
					qrTransactionDto.setStatus(TransactionStatusEnum.FAILED.getStatus());
					interbanktransferDelegate.updateStatusUsingTransactionId(transactionid,
							TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
					return ErrorCodeEnum.ERR_29020.setErrorCode(result);
				}
			}
			LOG.debug("QRInterBankFundTransfer:transactionid:" + transactionid);
			LOG.debug("QRInterBankFundTransfer:interbanktransactionDTO.getDbpErrMsg():"
					+ interbanktransactionDTO.getDbpErrMsg());
			String externalServiceResponse = interbanktransactionDTO.getExternalServiceResponse() != null
					? interbanktransactionDTO.getExternalServiceResponse().toString()
					: null;
//			String externalServicePayload = interbanktransactionDTO.getExternalApiPayload() != null
//					? interbanktransactionDTO.getExternalApiPayload().toString()
//					: null;
			String dbpErrMsg = interbanktransactionDTO.getDbpErrMsg();
			String dbpErrCode = interbanktransactionDTO.getDbpErrCode();
			String status = interbanktransactionDTO.getStatus();
			String paymentId = interbanktransactionDTO.getPaymentId();
			String paymentRequestId = interbanktransactionDTO.getRequestId();
			String paymentTransactionsNotes = interbanktransactionDTO.getPaymentNote();
			Map<String, Object> updateParams = new HashMap<String, Object>();
			updateParams.put("requestId", paymentRequestId);
			updateParams.put("paymentId", paymentId);
			updateParams.put("transactionsNotes", paymentTransactionsNotes);
			// updateParams.put("externalServicePayload", externalServicePayload);
			updateParams.put("externalServiceResponse", externalServiceResponse);
			if (interbanktransactionDTO.getDbpErrCode() != null || interbanktransactionDTO.getDbpErrMsg() != null) {
				LOG.debug("QRInterBankFundTransfer:interbanktransactionDTO.getDbpErrCode():"
						+ interbanktransactionDTO.getDbpErrCode());
				result.addParam(new Param("referenceId", paymentId));
				if (dbpErrMsg.equalsIgnoreCase(TransactionStatusEnum.REVERSED.getMessage())
						|| dbpErrMsg.equalsIgnoreCase(TransactionStatusEnum.REVERSAL_FAILED.getMessage())) {
					result.addParam(new Param("referenceId", paymentId));
					result.addParam(new Param("paymentId", paymentId));
					result.addParam(new Param("legalEntityId", legalEntityId));
				}
				result.addParam(new Param("message", dbpErrMsg));
				result.addParam(new Param("errorDetails", interbanktransactionDTO.getErrorDetails()));
				result.addParam(new Param("dbpErrCode", dbpErrCode));
				result.addParam(new Param("dbpErrMsg", dbpErrMsg));
				result.addParam(new Param("status", status));
				result.addParam(new Param("externalServiceResponse", externalServiceResponse));
				qrTransactionDto.setStatus(TransactionStatusEnum.FAILED.getStatus());
				updateStatusUsingTransactionId(transactionid, TransactionStatusEnum.FAILED.getStatus(),
						confirmationNumber, updateParams);
				LOG.debug("QRInterBankFundTransfer:interbanktransactionDTO.final result::" + result);
				return result;
			}

			if (interbanktransactionDTO.getReferenceId() == null
					|| "".equals(interbanktransactionDTO.getReferenceId())) {
				qrTransactionDto.setStatus(TransactionStatusEnum.FAILED.getStatus());
				interbanktransferDelegate.updateStatusUsingTransactionId(transactionid,
						TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
				return ErrorCodeEnum.ERR_12601.setErrorCode(result);
			}
			interbankDTO = interbanktransactionDTO;
			qrTransactionDto.setStatus(TransactionStatusEnum.EXECUTED.getStatus());
			qrTransactionDto.setReferenceId(interbanktransactionDTO.getReferenceId());
			qrTransactionDto.setTransactRefId(interbanktransactionDTO.getPaymentId());
			qrTransactionDto.setDescription(transactionStatus.getMessage());
			updateStatusUsingTransactionId(transactionid, TransactionStatusEnum.EXECUTED.getStatus(),
					interbanktransactionDTO.getReferenceId(), updateParams);
			result.appendResult(JSONToResult.convert(interbanktransactionDTO.getExternalServiceResponse().toString()));
			result.addParam(new Param("referenceId", interbanktransactionDTO.getReferenceId()));
			result.addParam(new Param("paymentId", interbanktransactionDTO.getPaymentId()));
			result.addParam(new Param("requestId", interbanktransactionDTO.getRequestId()));
			result.addParam(new Param("transactionsNotes", interbanktransactionDTO.getPaymentNote()));
			result.addParam(new Param("status", transactionStatus.getStatus()));
			result.addParam(new Param("message", transactionStatus.getMessage()));
			result.addParam(new Param("legalEntityId", legalEntityId));
			result.addParam(new Param("httpStatusCode", "200"));
			result.addParam(new Param("externalServiceResponse", externalServiceResponse));
		}

		if (interbankDTO.getOverrides() != null) {
			result.addParam(new Param("overrides", interbankDTO.getOverrides()));
		}
		if (interbankDTO.getOverrideList() != null) {
			result.addParam(new Param("overrideList", interbankDTO.getOverrideList()));
		}

		if (interbankDTO.getCharges() != null) {
			result.addParam(new Param("charges", interbankDTO.getCharges()));
		}
		if (interbankDTO.getExchangeRate() != null) {
			result.addParam(new Param("exchangeRate", interbankDTO.getExchangeRate()));
		}
		if (interbankDTO.getTotalAmount() != null) {
			result.addParam(new Param("totalAmount", interbankDTO.getTotalAmount()));
		}
		if (interbankDTO.getMessageDetails() != null) {
			result.addParam(new Param("messageDetails", interbankDTO.getMessageDetails()));
		}
		if (interbankDTO.getQuoteCurrency() != null) {
			result.addParam(new Param("quoteCurrency", interbankDTO.getQuoteCurrency()));
		}
		try {
			_logTransaction(request, response, inputArray, result, transactionStatus,
					transactionStatusDTO.getConfirmationNumber(), interbankdbxDTO, requestid);
		} catch (Exception e) {
			alert.prepareError("Error occured while audit logging.", e).log();
		}

		// ADP-7058 update additional meta data
		try {
			approvalQueueDelegate.updateAdditionalMetaForApprovalRequest(transactionStatusDTO.getRequestId(), request);
		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
		}
		LOG.debug("HBL:QRInterBankFundTransfer:Final result:" + ResultToJSON.convert(result));

		return result;
	}

	public HashMap<String, Object> payloadForActualServiceOld(Map<String, Object> inputParams, String transactionid) {
		HashMap<String, Object> externalServicepayload = new HashMap<String, Object>();
		HashMap<String, Object> payload = new HashMap<String, Object>();
		String transactionCurrency = inputParams.get("transactionCurrency") != null
				? inputParams.get("transactionCurrency").toString()
				: "";
		if (transactionCurrency.equalsIgnoreCase("NPR")) {
			externalServicepayload.put("amount", inputParams.get("transactionAmount"));
		} else if (transactionCurrency.equalsIgnoreCase("USD")) {
			externalServicepayload.put("amount", inputParams.get("convertedAmount"));
		}
		externalServicepayload.put("currency", "NPR"/* inputParams.get("transactionCurrency") */);
		externalServicepayload.put("debtorAgent", inputParams.get("debtorAgent"));
		externalServicepayload.put("debtorBranch", inputParams.get("debtorBranch"));
		externalServicepayload.put("debtorName", inputParams.get("debtorName"));
		externalServicepayload.put("debtorAccount", inputParams.get("fromAccountNumber"));
		externalServicepayload.put("creditorAgent", inputParams.get("bankId"));
		externalServicepayload.put("creditorBranch",
				inputParams.get("creditorBranch") != null ? inputParams.get("creditorBranch").toString() : "1");
		externalServicepayload.put("creditorName", inputParams.get("beneficiaryName"));
		externalServicepayload.put("creditorAccount", inputParams.get("toAccountNumber"));
		externalServicepayload.put("remarks", inputParams.get("transactionsNotes"));
		// Extra data for Intrabank Transaction won't used in external service call
		if (StringUtils.isNotBlank(transactionid)) {
			inputParams.put("interbankDbxTransactionId", transactionid);
			externalServicepayload.put("interbankDbxTransactionId", transactionid);
		}
		payload.put("internalServicePayload", inputParams);
		payload.put("externalServicepayload", externalServicepayload);
		LOG.debug("InterBankFundTransferResourceImplExtn:payloadForActualService:" + payload);
		return payload;

	}

	public HashMap<String, Object> payloadForActualService(Map<String, Object> inputParams, String requestId)
			throws Exception {
		HashMap<String, Object> payload = new HashMap<String, Object>();
		HashMap<String, Object> internalPayload = (HashMap<String, Object>) inputParams;
		internalPayload.put("transactionRefNo", requestId);
		String transactionCurrency = inputParams.get("transactionCurrency") != null
				? inputParams.get("transactionCurrency").toString()
				: "";
		String amount = "";
		if (transactionCurrency.equalsIgnoreCase("NPR")) {
			amount = inputParams.get("transactionAmount").toString();
		} else if (transactionCurrency.equalsIgnoreCase("USD")) {
			amount = inputParams.get("convertedAmount").toString();
		}
		HashMap<String, Object> externalServicepayload = internalPayload;
		externalServicepayload.put("amount", amount);
		try {
			externalServicepayload = GenerateNCHLPayload.prepareOtherBankTransferPayload(externalServicepayload, null);
		} catch (ApplicationException e) {
			throw new Exception(e.getMessage());
		}
		externalServicepayload.put("transactionRefNo", requestId);
		payload.put("internalServicePayload", internalPayload);
		payload.put("externalServicepayload", externalServicepayload);
		return payload;

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
			String eventSubType = "QRPayment";
			String producer = "Transactions/POST(createTransfer)";
			String statusID = "";
			boolean isSMEUser = CustomerSession.IsBusinessUser(customer);
			String fromAccountNumber = "";
			String toAccountNumber = "";

			if (request.containsKeyInRequest("validate")) {
				String validate = request.getParameter("validate");
				if (StringUtils.isNotBlank(validate) && validate.equalsIgnoreCase("true"))
					return;
			}

			if (request.containsKeyInRequest("fromAccountNumber")) {
				fromAccountNumber = request.getParameter("fromAccountNumber");
			}
			if (request.containsKeyInRequest("toAccountNumber")) {
				toAccountNumber = request.getParameter("toAccountNumber");
			}
			;

			JsonObject customParams = new JsonObject();
			String status = "";
			customParams.addProperty(Constants.REFERENCEID, result.getParamValueByName(Constants.REFERENCEID));
			customParams = auditLog.buildCustomParamsForAlertEngine(fromAccountNumber, toAccountNumber, customParams);

//			eventSubType = auditLog.deriveSubTypeForExternalTransfer(isScheduled, frequencyType,
//					FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE);
			List<Param> params = result.getAllParams();
			for (Param param : params) {
				if (request.containsKeyInRequest(param.getName())) {
					continue;
				} else {
					customParams.addProperty(param.getName(), param.getValue());
				}
			}
			String bankId = request.getParameter("bankId") != null ? request.getParameter("bankId")
					: request.getParameter("creditorAgent") != null ? request.getParameter("creditorAgent") : "";
			String beneficiaryBankName = request.getParameter("beneficiaryBankName");
			customParams.addProperty("FirstName",
					customer.get("FullName") != null ? customer.get("FullName").toString() : "");
			customParams = auditLog.buildCustomParamsForAlertEngine(fromAccountNumber, toAccountNumber, customParams);
			customParams.addProperty("toAccountName", request.getParameter("beneficiaryName"));
			customParams.addProperty("bankName", "Himalayan Bank Limited");
			customParams.addProperty("receiverBankName", beneficiaryBankName);
			customParams.addProperty("customerName",
					customer.get("FullName") != null ? customer.get("FullName").toString() : "");
			customParams.addProperty("toAccountNumber",
					maskAccountNumber(toAccountNumber, 0, toAccountNumber.length() - 4, 'X'));
			customParams.addProperty("fromAccountNumber",
					maskAccountNumber(fromAccountNumber, 0, toAccountNumber.length() - 4, 'X'));
			String scheduledDate = result.getParamValueByName("scheduledDate");
			String amount = result.getParamValueByName(Constants.AMOUNT);
			String transactionAmount = result.getParamValueByName("transactionAmount");
			String serviceCharge = result.getParamValueByName("serviceCharge");
			amount = formatAmount(amount);
			transactionAmount = formatAmount(transactionAmount);
			serviceCharge = formatAmount(serviceCharge);
			String transactionCurrency = request.getParameter("transactionCurrency");
			if (StringUtils.isNotBlank(transactionCurrency)) {
				customParams.addProperty(Constants.AMOUNT, transactionCurrency + " " + amount);
				customParams.addProperty("transactionAmount", transactionCurrency + " " + transactionAmount);
				customParams.addProperty("serviceCharge", transactionCurrency + " " + serviceCharge);
			}
			if (scheduledDate != null)
				customParams.addProperty("scheduledDate",
						scheduledDate.length() > 10 ? scheduledDate.substring(0, 10) : scheduledDate);
			if (transactionStatus.toString().contains("DENIED")) {
				statusID = Constants.SID_EVENT_FAILURE;
				customParams.addProperty(Constants.REFERENCEID, result.getParamValueByName(Constants.REFERENCEID));
			} else {
				switch (transactionStatus) {
				case SENT:
					referenceId = customParams.get("referenceId").getAsString();
					if (interbankDTO == null) {
						statusID = Constants.SID_EVENT_FAILURE;
						status = Constants.STATUS_FAIL.toLowerCase();
					}
					if (interbankDTO.getDbpErrMsg() != null && !interbankDTO.getDbpErrMsg().isEmpty()) {
						statusID = Constants.SID_EVENT_FAILURE;
						status = Constants.STATUS_FAIL.toLowerCase();
					}
					if (referenceId == null || "".equals(referenceId)) {
						statusID = Constants.SID_EVENT_FAILURE;
						status = Constants.STATUS_FAIL.toLowerCase();
					} else {
						statusID = Constants.SID_EVENT_SUCCESS;
						status = Constants.STATUS_SUCCESS.toLowerCase();
						customParams.addProperty(Constants.REFERENCEID, referenceId);
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
			if (isSMEUser) {
				customParams.addProperty("approvedBy", "N/A");
				customParams.addProperty("rejectedBy", "N/A");
			}

			AdminUtil.addAdminUserNameRoleIfAvailable(customParams, request);

			EventsDispatcher.dispatch(request, response, eventType, eventSubType, producer, statusID, null,
					CustomerSession.getCustomerId(customer), null, customParams);
		} catch (Exception e) {
			alert.prepareError("Error while pushing to Audit Engine." + e).log();
		}
	}

	private Result stubCommitResponse(Map<String, Object> inputParams) {
		Result result = new Result();
		result.addStringParam("referenceId", String.format("%06d", new java.util.Date().getTime() % 1000000));
		result.addStringParam("status", "Sent");
		result.addStringParam("message", "Success! Your transaction has been completed");
		result.addStringParam("totalAmount",
				inputParams.containsKey("totalAmount") && inputParams.get("totalAmount") != null
						? inputParams.get("totalAmount").toString()
						: "");
		result.addStringParam("charges", "");
		result.addStringParam("fromAccountNumber",
				inputParams.containsKey("fromAccountNumber") && inputParams.get("fromAccountNumber") != null
						? inputParams.get("fromAccountNumber").toString()
						: "");
		result.addStringParam("scheduledDate",
				inputParams.containsKey("scheduledDate") && inputParams.get("scheduledDate") != null
						? inputParams.get("scheduledDate").toString()
						: "");
		result.addStringParam("swiftCode",
				inputParams.containsKey("swiftCode") && inputParams.get("swiftCode") != null
						? inputParams.get("swiftCode").toString()
						: "");
		result.addStringParam("isScheduled",
				inputParams.containsKey("isScheduled") && inputParams.get("isScheduled") != null
						? inputParams.get("isScheduled").toString()
						: "");
		result.addStringParam("fromAccountCurrency",
				inputParams.containsKey("fromAccountCurrency") && inputParams.get("fromAccountCurrency") != null
						? inputParams.get("fromAccountCurrency").toString()
						: "");
		result.addStringParam("frequencyType",
				inputParams.containsKey("frequencyType") && inputParams.get("frequencyType") != null
						? inputParams.get("frequencyType").toString()
						: "");
		result.addStringParam("toAccountNumber",
				inputParams.containsKey("toAccountNumber") && inputParams.get("toAccountNumber") != null
						? inputParams.get("toAccountNumber").toString()
						: "");
		result.addStringParam("transactionCurrency",
				inputParams.containsKey("transactionCurrency") && inputParams.get("transactionCurrency") != null
						? inputParams.get("transactionCurrency").toString()
						: "");
		result.addStringParam("transactionType",
				inputParams.containsKey("transactionType") && inputParams.get("transactionType") != null
						? inputParams.get("transactionType").toString()
						: "");
		result.addStringParam("beneficiaryName",
				inputParams.containsKey("beneficiaryName") && inputParams.get("beneficiaryName") != null
						? inputParams.get("beneficiaryName").toString()
						: "");
		result.addStringParam("paidBy",
				inputParams.containsKey("paidBy") && inputParams.get("paidBy") != null
						? inputParams.get("paidBy").toString()
						: "");
		result.addStringParam("serviceName", "INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE");
		result.addStringParam("transactionAmount",
				inputParams.containsKey("amount") && inputParams.get("amount") != null
						? inputParams.get("amount").toString()
						: "");
		result.addStringParam("featureActionId", "INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE");
		result.addStringParam("transactionId", "123");
		result.addStringParam("beneficiaryBankName",
				inputParams.containsKey("beneficiaryBankName") && inputParams.get("beneficiaryBankName") != null
						? inputParams.get("beneficiaryBankName").toString()
						: "");
		result.addStringParam("beneficiaryAddressLine1",
				inputParams.containsKey("beneficiaryAddressLine1") && inputParams.get("beneficiaryAddressLine1") != null
						? inputParams.get("beneficiaryAddressLine1").toString()
						: "");
		result.addStringParam("beneficiaryAddressLine2",
				inputParams.containsKey("beneficiaryAddressLine2") && inputParams.get("beneficiaryAddressLine2") != null
						? inputParams.get("beneficiaryAddressLine2").toString()
						: "");
		result.addStringParam("beneficiaryCity",
				inputParams.containsKey("beneficiaryCity") && inputParams.get("beneficiaryCity") != null
						? inputParams.get("beneficiaryCity").toString()
						: "");
		result.addStringParam("beneficiaryZipcode",
				inputParams.containsKey("beneficiaryZipcode") && inputParams.get("beneficiaryZipcode") != null
						? inputParams.get("beneficiaryZipcode").toString()
						: "");
		result.addStringParam("beneficiarycountry",
				inputParams.containsKey("beneficiarycountry") && inputParams.get("beneficiarycountry") != null
						? inputParams.get("beneficiarycountry").toString()
						: "");
		// result.addStringParam("messageDetails",
		// "[{\\\"id\\\":\\\"PI-CHARGE.BEARER.NOT.ALLOWED\\\",\\\"message\\\":\\\"Auto:CHARGE
		// BEARER IS NOT ALLOWED FOR THE PRODUCT\\\"}]");
		return result;
	}

	private Result stubValidateResponse(Map<String, Object> inputParams) {
		Result result = new Result();
		result.addStringParam("referenceId", "12345");
		result.addStringParam("status", "success");
		result.addStringParam("totalAmount", inputParams.get("amount").toString());
		result.addStringParam("charges", "");
		// result.addStringParam("messageDetails",
		// "[{\\\"id\\\":\\\"PI-CHARGE.BEARER.NOT.ALLOWED\\\",\\\"message\\\":\\\"Auto:CHARGE
		// BEARER IS NOT ALLOWED FOR THE PRODUCT\\\"}]");
		return result;
	}

	protected Result verifyPayeeAndUpdateDB(DataControllerRequest request, Map<String, Object> inputParams,
			Result result, String payeeVerificationStatus, String payeeVerificationErrMsg,
			boolean isInternationalAccount) {
		String verifyPayee = inputParams.get("payeeVerification") == null ? null
				: inputParams.get("payeeVerification").toString();
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String userId = CustomerSession.getCustomerId(customer);
		String payeeVerificationName = "";
		if ("true".equalsIgnoreCase(verifyPayee)) {
			payeeVerificationStatus = "Success";
			Result payeeVerificationResult = PayeeVerificationBackendServicesHelper.fetchVerifyPayeeResponse(request,
					inputParams);
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
			JSONObject payeeObjects = new JSONObject();
			try {
				payeeObjects = interbankBackendDelegate.getPayeeIDToUpdatePayeeVerificationStatus(
						inputParams.get("toAccountNumber").toString(), false, isInternationalAccount, userId);
				if (payeeObjects.has("externalaccount") && payeeObjects.getJSONArray("externalaccount").length() > 0) {
					JSONArray externalAccountArray = payeeObjects.getJSONArray("externalaccount");
					JSONObject externalAccountObject = externalAccountArray.getJSONObject(0);
					String idValue = externalAccountObject.getString("Id");
					interbankBackendDelegate.updatePayeeVerificationStatus(idValue, payeeVerificationStatus);
				}
			} catch (JSONException e) {
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

	public InterBankFundTransferDTO updateStatusUsingTransactionId(String transactionId, String status,
			String confirmationNumber, Map<String, Object> extraParams) {

		List<InterBankFundTransferDTO> interbankfundtransferdto = null;

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_INTERBANKFUNDTRANSFERS_UPDATE;

		// Map<String, Object> requestParams = new HashMap<String, Object>();
		extraParams.put("transactionId", transactionId);
		extraParams.put("status", status);
		extraParams.put("confirmationNumber", confirmationNumber);
		alert.prepareError("InterBankFundTransferDTO updateStatusUsingTransactionId :requestParams:" + extraParams)
				.log();

		try {
			String updateResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(extraParams).build().getResponse();
			JSONObject jsonRsponse = new JSONObject(updateResponse);
			JSONArray interbankJsonArray = CommonUtils.getFirstOccuringArray(jsonRsponse);
			interbankfundtransferdto = JSONUtils.parseAsList(interbankJsonArray.toString(),
					InterBankFundTransferDTO.class);
		}

		catch (JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while updating the interbankfundtransfer", jsonExp).log();
			return null;
		} catch (Exception exp) {
			alert.prepareError("Excpetion occured while updating the interbankfundtransfer", exp).log();
			return null;
		}

		if (interbankfundtransferdto != null && interbankfundtransferdto.size() != 0)
			return interbankfundtransferdto.get(0);

		return null;
	}

	public InterBankFundTransferDTO createTransactionWithoutApproval(
			InterBankFundTransferBackendDTO interBankFundTransferBackendDTO, DataControllerRequest request,
			QRTransactionDTO qrTransactionDto) {
		LOG.debug("QRInterBankFundTransfer createTransactionWithoutApproval:::");
		String serviceName = HBLConstants.INTER_BANK_FUND_TRANSFER_LINE_OF_BUSINESS_SERVICE;
		String operationName = HBLConstants.INTER_BANK_FUND_TRANSFER_BACKEND_WITHOUT_APPROVER;
		String paymentType = "CIPS";

		String createResponse = null;
		InterBankFundTransferDTO interbankfundtransferdto = new InterBankFundTransferDTO();

		Map<String, Object> externalApiPayload = interBankFundTransferBackendDTO.getExternalApiPayload();
		Map<String, Object> internalApiPayload = interBankFundTransferBackendDTO.getInternalApiPayload();

//		try {
//			externalApiPayload = JSONUtils.parseAsMap(new JSONObject(externalApiPayload).toString(), String.class,
//					Object.class);
//
//		} catch (IOException e) {
//			alert.prepareError("Error occured while fetching the input params: ", e).log();
//			return null;
//		}
//		@SuppressWarnings("unchecked")
//		Map<String, Object> transactionData = (Map<String, Object>) externalApiPayload.get("transactionData");
//		externalApiPayload.remove("transactionData");
		LOG.debug("createTransactionWithoutApproval transactionData:::" + internalApiPayload);
		String requestId = interBankFundTransferBackendDTO.getRequestId();
		internalApiPayload.put("requestId", requestId);

		// need to add logic
		String isPersonalQROtherBankParkingEnabled = EnvironmentConfigurationsHandler
				.getServerProperty("PARKING_ACC_TRANSFER_PERSONALQR_OTHERBANK_ENABLED");
		LOG.debug("isPersonalQROtherBankParkingEnabled:::" + isPersonalQROtherBankParkingEnabled);
		if (isPersonalQROtherBankParkingEnabled.equals("true")) {
			Result intraBankTransferResult = createIntraBankTransaction(internalApiPayload, externalApiPayload,
					request);
			interbankfundtransferdto.setRequestId(requestId);
			String paymentOrderId = intraBankTransferResult.getParamValueByName("referenceId");
			Map<String, Object> requestParams = new HashMap<String, Object>();
			requestParams.put("paymentOrderId", paymentOrderId);
			String transactionStatus = intraBankTransferResult.getParamValueByName("transactionStatus");
			LOG.debug("QRInterBankFundTransfer:paymentOrderId:" + paymentOrderId);
			LOG.debug("QRInterBankFundTransfer:transactionStatus:" + transactionStatus);
			interbankfundtransferdto.setExternalApiPayload((HashMap<String, Object>) externalApiPayload);
			String payloadString = new Gson().toJson(externalApiPayload);
			qrTransactionDto.setExternalServiceRequest(payloadString);
			if (StringUtils.isNotBlank(transactionStatus) && transactionStatus.equalsIgnoreCase("success")) {
				/*
				 * Checking Transaction current status
				 */
				String intraBankDbxTransID = intraBankTransferResult.getParamValueByName("intraBankDbxTransID");
				externalApiPayload.put("debitReferenceId", paymentOrderId);
				externalApiPayload.put("intraBankDbxTransID", intraBankDbxTransID);
				externalApiPayload.put("paymentSystemId",
						intraBankTransferResult.getParamValueByName("paymentSystemId"));
				LOG.debug("QRInterBankFundTransfer:externalApiPayload:" + externalApiPayload);
				interbankfundtransferdto.setExternalApiPayload((HashMap<String, Object>) externalApiPayload);
				payloadString = new Gson().toJson(externalApiPayload);
				qrTransactionDto.setExternalServiceRequest(payloadString);
				JSONObject paymentObj = getTransactionStatus(requestParams, request);
				transactionStatus = paymentObj != null ? paymentObj.optString("currentStatus") : "";
				LOG.debug("QRInterBankFundTransfer:current transactionStatus:" + transactionStatus);
				if (StringUtils.isNotBlank(transactionStatus) && !transactionStatus.equalsIgnoreCase("Complete")) {
					intraBankTransferResult
							.addParam(new Param("dbpErrCode", ErrorCodeEnum.ERR_21210.getErrorCodeAsString()));
					intraBankTransferResult.addParam(new Param("dbpErrMsg", ErrorCodeEnum.ERR_21210.getMessage()));
					intraBankTransferResult
							.addParam(new Param("errorDetails", "Payment Order cannot be completed at this movement."));
					interbankfundtransferdto.setStatus("Payment Order Cannot be Completed.");
					interbankfundtransferdto.setPaymentNote("PAYMENT_ORDER_NOT_COMPLETED");
					interbankfundtransferdto.setErrorDetails("Payment Order cannot be completed at this movement.");
					interbankfundtransferdto.setMessage("Payment Order cannot be completed at this movement.");
					interbankfundtransferdto.setPaymentId(paymentOrderId);
					qrTransactionDto.setTransactRefId(paymentOrderId);
					qrTransactionDto.setStatus("failed");
					qrTransactionDto.setDescription("Payment Order cannot be completed at this movement.");
					// interbankfundtransferdto.setExternalApiPayload(null);
				} else if (StringUtils.isNotBlank(transactionStatus)
						&& transactionStatus.equalsIgnoreCase("Complete")) {
					intraBankTransferResult.addParam(new Param("paymentSystemId",
							paymentObj != null ? paymentObj.optString("paymentSystemId") : ""));
				}
			}

			if (intraBankTransferResult.getParamValueByName("dbpErrCode") == null
					&& StringUtils.isNotBlank(paymentOrderId) && StringUtils.isNotBlank(transactionStatus)) {
				String intraBankDbxTransID = intraBankTransferResult.getParamValueByName("intraBankDbxTransID");
				externalApiPayload.put("debitReferenceId", paymentOrderId);
				externalApiPayload.put("intraBankDbxTransID", intraBankDbxTransID);
				externalApiPayload.put("paymentSystemId",
						intraBankTransferResult.getParamValueByName("paymentSystemId"));
				LOG.debug("QRInterBankFundTransfer:externalApiPayload:" + externalApiPayload);
				interbankfundtransferdto.setExternalApiPayload((HashMap<String, Object>) externalApiPayload);
				payloadString = new Gson().toJson(externalApiPayload);
				qrTransactionDto.setExternalServiceRequest(payloadString);
				LOG.debug("InterBankFundTransferBackendDelegateImplExtn:externalApiPayload:" + externalApiPayload);
				JSONObject batchDetails = null;
				JSONArray transactionDetailList = null;
				paymentType = externalApiPayload.get("paymentType") != null
						? externalApiPayload.get("paymentType").toString()
						: "";
				String token = externalApiPayload.get("token").toString();
				if (paymentType.equalsIgnoreCase("IPS")) {
					operationName = HBLConstants.INTER_BANK_FUND_TRANSFER_POST_IPS_BATCH_TRANSFERS_OP;
					batchDetails = (JSONObject) externalApiPayload.get("nchlIpsBatchDetail");
					transactionDetailList = (JSONArray) externalApiPayload.get("nchlIpsTransactionDetailList");
				} else {
					operationName = HBLConstants.INTER_BANK_FUND_TRANSFER_CIPS_BATCH_TRANSFERS_OP;
					batchDetails = (JSONObject) externalApiPayload.get("cipsBatchDetail");
					transactionDetailList = (JSONArray) externalApiPayload.get("cipsTransactionDetailList");
				}
				JSONObject transactionDetailListObj = transactionDetailList.getJSONObject(0);
				LOG.debug("InterBankFundTransferBackendDelegateImplExtn:externalApiPayload:" + externalApiPayload);

				externalApiPayload = new HashMap<String, Object>();
				externalApiPayload.put("token", token);
				splitRequestPayload(batchDetails, externalApiPayload);
				splitRequestPayload(transactionDetailListObj, externalApiPayload);
				LOG.debug(
						"InterBankFundTransferBackendDelegateImplExtn:final externalApiPayload:" + externalApiPayload);
				payloadString = new Gson().toJson(externalApiPayload);
				LOG.debug("InterBankFundTransferBackendDelegateImplExtn:payloadString:::" + payloadString);
				qrTransactionDto.setExternalServiceRequest(payloadString);
				try {
					createResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
							.withOperationId(operationName).withRequestParameters(externalApiPayload)
							.withRequestHeaders(request.getHeaderMap()).withDataControllerRequest(request).build()
							.getResponse();
					LOG.debug("InterBankFundTransferBackendDelegateImplExtn:createResponse:" + createResponse);
					qrTransactionDto.setExternalServiceResponse(createResponse);
					interbankfundtransferdto = processExtenalServiceResponseNew(createResponse,
							interbankfundtransferdto, request, qrTransactionDto);// JSONUtils.parse(createResponse,
					// InterBankFundTransferDTO.class);
					if (interbankfundtransferdto.getTransactionId() != null
							&& !"".equals(interbankfundtransferdto.getTransactionId())) {
						interbankfundtransferdto.setReferenceId(interbankfundtransferdto.getTransactionId());
						qrTransactionDto.setReferenceId(interbankfundtransferdto.getTransactionId());
						interbankfundtransferdto.setPaymentNote("PAYMENT_COMPLETED");
						interbankfundtransferdto.setPaymentId(interbankfundtransferdto.getTransactionId());
					}
				} catch (JSONException e) {
					alert.prepareError("Failed to create interbank transaction: ", e).log();
					interbankfundtransferdto.setErrorDetails(e.toString());
					qrTransactionDto.setErrmsg(e.toString());
					JSONObject errorObj = new JSONObject();
					errorObj.put("errorMessage", e.toString());
					interbankfundtransferdto.setExternalServiceResponse(errorObj);
					qrTransactionDto.setExternalServiceResponse(e.toString());
					interbankfundtransferdto = processReversalTransaction(interbankfundtransferdto, request);
				} catch (Exception e) {
					alert.prepareError("Caught exception at create interbank transaction: ", e).log();
					interbankfundtransferdto.setErrorDetails(e.toString());
					qrTransactionDto.setErrmsg(e.toString());
					JSONObject errorObj = new JSONObject();
					errorObj.put("errorMessage", e.toString());
					interbankfundtransferdto.setExternalServiceResponse(errorObj);
					qrTransactionDto.setExternalServiceResponse(e.toString());
					interbankfundtransferdto = processReversalTransaction(interbankfundtransferdto, request);
				}
			} else {
				interbankfundtransferdto.setDbpErrCode(intraBankTransferResult.getParamValueByName("dbpErrCode"));
				interbankfundtransferdto.setDbpErrMsg(intraBankTransferResult.getParamValueByName("dbpErrMsg"));
				interbankfundtransferdto.setErrorDetails(intraBankTransferResult.getParamValueByName("errorDetails"));
				interbankfundtransferdto.setExternalApiPayload(null);
				qrTransactionDto.setErrmsg(intraBankTransferResult.getParamValueByName("dbpErrMsg"));
			}
		} else {
			LOG.debug("isPersonalQROtherBankParkingEnabled inside else:::" + isPersonalQROtherBankParkingEnabled);
			String paymentOrderId = null;
			interbankfundtransferdto.setRequestId(requestId);
			interbankfundtransferdto.setExternalApiPayload((HashMap<String, Object>) externalApiPayload);
			LOG.debug("InterBankFundTransferBackendDelegateImplExtn:externalApiPayload:" + externalApiPayload);
			String payloadString = new Gson().toJson(externalApiPayload);
			qrTransactionDto.setExternalServiceRequest(payloadString);
			JSONObject batchDetails = null;
			JSONArray transactionDetailList = null;
			paymentType = externalApiPayload.get("paymentType") != null
					? externalApiPayload.get("paymentType").toString()
					: "";
			String token = externalApiPayload.get("token").toString();
			if (paymentType.equalsIgnoreCase("IPS")) {
				operationName = HBLConstants.INTER_BANK_FUND_TRANSFER_POST_IPS_BATCH_TRANSFERS_OP;
				batchDetails = (JSONObject) externalApiPayload.get("nchlIpsBatchDetail");
				transactionDetailList = (JSONArray) externalApiPayload.get("nchlIpsTransactionDetailList");
			} else {
				operationName = HBLConstants.INTER_BANK_FUND_TRANSFER_CIPS_BATCH_TRANSFERS_OP;
				batchDetails = (JSONObject) externalApiPayload.get("cipsBatchDetail");
				transactionDetailList = (JSONArray) externalApiPayload.get("cipsTransactionDetailList");
			}
			JSONObject transactionDetailListObj = transactionDetailList.getJSONObject(0);
			LOG.debug("InterBankFundTransferBackendDelegateImplExtn:externalApiPayload:" + externalApiPayload);

			externalApiPayload = new HashMap<String, Object>();
			externalApiPayload.put("token", token);
			splitRequestPayload(batchDetails, externalApiPayload);
			splitRequestPayload(transactionDetailListObj, externalApiPayload);
			LOG.debug("InterBankFundTransferBackendDelegateImplExtn:final externalApiPayload:" + externalApiPayload);
			payloadString = new Gson().toJson(externalApiPayload);
			LOG.debug("InterBankFundTransferBackendDelegateImplExtn:payloadString:::" + payloadString);
			qrTransactionDto.setExternalServiceRequest(payloadString);
			try {
				createResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
						.withOperationId(operationName).withRequestParameters(externalApiPayload)
						.withRequestHeaders(request.getHeaderMap()).withDataControllerRequest(request).build()
						.getResponse();
				LOG.debug("InterBankFundTransferBackendDelegateImplExtn:createResponse:" + createResponse);
				qrTransactionDto.setExternalServiceResponse(createResponse);
				interbankfundtransferdto = processExtenalServiceResponseNew(createResponse, interbankfundtransferdto,
						request, qrTransactionDto);// JSONUtils.parse(createResponse, InterBankFundTransferDTO.class);
				if (interbankfundtransferdto.getTransactionId() != null
						&& !"".equals(interbankfundtransferdto.getTransactionId())) {
					interbankfundtransferdto.setReferenceId(interbankfundtransferdto.getTransactionId());
					qrTransactionDto.setReferenceId(interbankfundtransferdto.getTransactionId());
					interbankfundtransferdto.setPaymentNote("PAYMENT_COMPLETED");
					interbankfundtransferdto.setPaymentId(interbankfundtransferdto.getTransactionId());

				}
			} catch (JSONException e) {
				alert.prepareError("Failed to create interbank transaction: ", e).log();
				interbankfundtransferdto.setErrorDetails(e.toString());
				qrTransactionDto.setErrmsg(e.toString());
				JSONObject errorObj = new JSONObject();
				errorObj.put("errorMessage", e.toString());
				interbankfundtransferdto.setExternalServiceResponse(errorObj);
				qrTransactionDto.setExternalServiceResponse(e.toString());
				// interbankfundtransferdto =
				// processReversalTransaction(interbankfundtransferdto, request);
			} catch (Exception e) {
				alert.prepareError("Caught exception at create interbank transaction: ", e).log();
				interbankfundtransferdto.setErrorDetails(e.toString());
				qrTransactionDto.setErrmsg(e.toString());
				JSONObject errorObj = new JSONObject();
				errorObj.put("errorMessage", e.toString());
				interbankfundtransferdto.setExternalServiceResponse(errorObj);
				qrTransactionDto.setExternalServiceResponse(e.toString());
				// interbankfundtransferdto =
				// processReversalTransaction(interbankfundtransferdto, request);
			}
		}
		return interbankfundtransferdto;
	}

	public InterBankFundTransferDTO processExtenalServiceResponse(String response,
			InterBankFundTransferDTO interbankfundtransferdto, DataControllerRequest request) {
		JSONObject externalResponseObj = new JSONObject(response);
		interbankfundtransferdto.setExternalServiceResponse(externalResponseObj);
		String paymentType = interbankfundtransferdto.getExternalApiPayload().get("paymentType") != null
				? interbankfundtransferdto.getExternalApiPayload().get("paymentType").toString()
				: "";
		LOG.debug("InterBankFundTransferBackendDelegateImplExtn:externalResponseObj:" + externalResponseObj);
		JSONArray cipsTxnResponseList = externalResponseObj.has("cipsTxnResponseList")
				? externalResponseObj.getJSONArray("cipsTxnResponseList")
				: new JSONArray();
		LOG.debug("InterBankFundTransferBackendDelegateImplExtn:cipsTxnResponseList:" + cipsTxnResponseList);
		String transactionId = "";
		String status = "";
		String creditStatus = "";
		String responseCode = "";
		if (cipsTxnResponseList.length() > 0) {
			transactionId = cipsTxnResponseList.getJSONObject(0).getString("id");
			status = cipsTxnResponseList.getJSONObject(0).getString("responseMessage");
			creditStatus = cipsTxnResponseList.getJSONObject(0).getString("creditStatus");
			responseCode = cipsTxnResponseList.getJSONObject(0).getString("responseCode");
			if (paymentType.equalsIgnoreCase("CIPS") && creditStatus.equalsIgnoreCase("000")
					&& responseCode.equalsIgnoreCase("000")) {
				interbankfundtransferdto.setTransactionId(transactionId);
				interbankfundtransferdto.setStatus(status);
				interbankfundtransferdto.setMessage(creditStatus);
			} else if (paymentType.equalsIgnoreCase("IPS") && creditStatus.equalsIgnoreCase("ENTR")
					&& responseCode.equalsIgnoreCase("ENTR")) {
				interbankfundtransferdto.setTransactionId(transactionId);
				interbankfundtransferdto.setStatus(status);
				interbankfundtransferdto.setMessage(creditStatus);
			} else {
				interbankfundtransferdto = processReversalTransaction(interbankfundtransferdto, request);
			}
		} else {
			interbankfundtransferdto = processReversalTransaction(interbankfundtransferdto, request);
		}
		return interbankfundtransferdto;
	}

	public InterBankFundTransferDTO processReversalTransaction(InterBankFundTransferDTO interbankfundtransferdto,
			DataControllerRequest request) {
		Map<String, Object> externalApiPayload = interbankfundtransferdto.getExternalApiPayload();
		Map<String, Object> reverseTxPayload = new HashMap<String, Object>();
		String debitReferenceId = externalApiPayload.get("debitReferenceId") != null
				? externalApiPayload.get("debitReferenceId").toString()
				: "";
		reverseTxPayload.put("intraBankDbxTransID", externalApiPayload.get("intraBankDbxTransID"));
		reverseTxPayload.put("paymentReferenceId", externalApiPayload.get("paymentSystemId"));
		IntraBankFundTransferDTO reversalResponse = reverseTransaction(reverseTxPayload, request);
		String status = "";
		String message = "";
		interbankfundtransferdto.setMessage(reversalResponse.getMessage());
		if (reversalResponse.getDbpErrCode() != null || reversalResponse.getDbpErrMsg() != null) {
			interbankfundtransferdto.setTransactionId(null);
			interbankfundtransferdto.setConfirmationNumber(externalApiPayload.get("debitReferenceId").toString());
			interbankfundtransferdto.setDbpErrCode(reversalResponse.getDbpErrCode());
			interbankfundtransferdto.setDbpErrMsg(reversalResponse.getDbpErrMsg());
			interbankfundtransferdto.setPaymentNote("REVERSAL_FAILED");
			interbankfundtransferdto.setPaymentId(debitReferenceId);
			status = reversalResponse.getStatus();
			message = reversalResponse.getErrorDetails();
		} else if (reversalResponse.getStatus().equalsIgnoreCase(TransactionStatusEnum.REVERSED.getStatus())) {
			LOG.debug("QRInterBankFundTransfer:reverseTransaction: debitReferenceId:" + debitReferenceId);
			interbankfundtransferdto.setTransactionId(null);
			// interbankfundtransferdto.setConfirmationNumber(externalApiPayload.get("debitReferenceId").toString());
			interbankfundtransferdto.setPaymentId(debitReferenceId);
			interbankfundtransferdto.setDbpErrCode(ErrorCodeEnum.ERR_12601.getErrorCodeAsString());
			interbankfundtransferdto.setDbpErrMsg(TransactionStatusEnum.REVERSED.getMessage());
			interbankfundtransferdto.setPaymentNote("REVERSAL_COMPLETED");
			// transferDtO.setTransactionts(externalApiPayload.get("debitReferenceId").toString());
			message = TransactionStatusEnum.REVERSED.getMessage();
		} else { // Reversal failed
			interbankfundtransferdto.setTransactionId(null);
			// interbankfundtransferdto.setConfirmationNumber(externalApiPayload.get("debitReferenceId").toString());
			interbankfundtransferdto.setPaymentId(debitReferenceId);
			interbankfundtransferdto.setDbpErrCode(ErrorCodeEnum.ERR_12601.getErrorCodeAsString());
			interbankfundtransferdto.setDbpErrMsg(TransactionStatusEnum.REVERSAL_FAILED.getMessage());
			interbankfundtransferdto.setPaymentNote("REVERSAL_FAILED");
			// transferDtO.setTransactionts(externalApiPayload.get("debitReferenceId").toString());
			message = TransactionStatusEnum.REVERSAL_FAILED.getMessage();
		}
		interbankfundtransferdto.setMessage(message);
		interbankfundtransferdto.setStatus(reversalResponse.getStatus());

		return interbankfundtransferdto;
	}

	public Result createIntraBankTransaction(Map<String, Object> inputParams, Map<String, Object> externalApiPayload,
			DataControllerRequest request) {
		IntraBankFundTransferDTO intrabankDTO = null;
		IntraBankFundTransferBusinessDelegate intrabankTransactionDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(IntraBankFundTransferBusinessDelegate.class);
		IntraBankFundTransferBackendDTOExtn intrabankBackendDTO = new IntraBankFundTransferBackendDTOExtn();
		IntraBankFundTransferDTO intrabanktransactionDTO = new IntraBankFundTransferDTO();
		Result result = new Result();
		LOG.debug("QRInterBankFundTransfer createIntraBankTransaction:::" + inputParams);
		String featureActionId = FeatureAction.INTRA_BANK_FUND_TRANSFER_CREATE;

		String serviceName = inputParams.get("serviceName") != null ? inputParams.get("serviceName").toString() : "";

		if (serviceName.equalsIgnoreCase(FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE)) {
			PayableAccountId = EnvironmentConfigurationsHandler
					.getServerProperty("CONNECTIPS_QR_PAY_PAYABLE_ACCNOUNT_NO");
		}

		LOG.debug("QRInterBankFundTransfer PayableAccountId:::" + PayableAccountId);
		String PayableAccountHolderName = "PayableAccountHolderName";
		inputParams.put("featureActionId", featureActionId);
		inputParams.put("serviceName", serviceName);
		inputParams.put("transactionType", PARKING_ACCOUNT_TRANSFER);
		inputParams.put("toAccountNumber", PayableAccountId);
		inputParams.put("ExternalAccountNumber", PayableAccountId);
		inputParams.put("beneficiaryName", PayableAccountHolderName);
		inputParams.put("transactionId", inputParams.get("interbankDbxTransactionId"));
		inputParams.put("validate", "false");
		LOG.debug("QRInterBankFundTransfer inputParams for Payment Order:::" + inputParams);

		try {
			intrabankDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), IntraBankFundTransferDTO.class);
			if (inputParams.get("beneficiaryBankName") != null)
				intrabankDTO.setBeneficiaryBankName(inputParams.get("beneficiaryBankName").toString());
		} catch (IOException e) {
			LOG.error("Error occured at QRInterBankFundTransfer while fetching the input params: " + e.getMessage());
			return ErrorCodeEnum.ERR_28021.setErrorCode(new Result());
		}
		IntraBankFundTransferDTO intrabankdbxDTO = intrabankTransactionDelegate.createTransactionAtDBX(intrabankDTO);
		if (intrabankdbxDTO == null) {
			LOG.error("Error occured while creating entry into the DBX table: ");
			return ErrorCodeEnum.ERR_29016.setErrorCode(new Result());
		}
		if (intrabankdbxDTO.getDbpErrCode() != null || intrabankdbxDTO.getDbpErrMsg() != null) {
			result.addParam(new Param("dbpErrCode", intrabankdbxDTO.getDbpErrCode()));
			result.addParam(new Param("dbpErrMsg", intrabankdbxDTO.getDbpErrMsg()));
			return result;
		}
		String intraBankDbxTransID = intrabankdbxDTO.getTransactionId();
		LOG.debug("InterBankFundTransferBackendDelegateImplExtn:createIntraBankTransaction:intraBankTransTxID:"
				+ intraBankDbxTransID);
		String confirmationNumber = null;
		intrabankBackendDTO = intrabankBackendDTO.convert(intrabankdbxDTO);
		intrabankBackendDTO.setTransactionId(null);
		intrabankBackendDTO.setChargeCurrency("NPR");
		intrabankBackendDTO.setChargeType("TRANSACTIONFEE");
		intrabankBackendDTO.setChargeName("Transaction Fee");
		intrabankBackendDTO.setChargeAmount(intrabankdbxDTO.getServiceCharge());
		intrabankBackendDTO.setTransactionCurrency("NPR");
		intrabankBackendDTO.setExchangeRate(intrabankDTO.getExchangeRate());
		if (inputParams.get("paymentType") != null) {
			intrabankBackendDTO.setPaymentType(inputParams.get("paymentType").toString());
		}
		if (inputParams.get("transactionAmount") != null)
			intrabankBackendDTO.setTotalAmount(inputParams.get("transactionAmount").toString());
		try {
			String jsonString = JSONUtils.stringify(externalApiPayload);
			intrabankBackendDTO.setAdditionalInformation(new JSONObject(jsonString));
		} catch (JSONException | IOException e) {
			LOG.debug("QRInterBankFundTransfer:createIntraBankTransaction:JSONException:" + e.getMessage());
		}
		LOG.debug("QRInterBankFundTransfer:createIntraBankTransaction:getAdditionalInformation():"
				+ intrabankBackendDTO.getAdditionalInformation());

		IntraBankFundTransferBackendDelegateImplExtn intrabankfundBackendDelegateExtn = new IntraBankFundTransferBackendDelegateImplExtn();
		intrabanktransactionDTO = intrabankfundBackendDelegateExtn.createTransactionWithoutApproval(intrabankBackendDTO,
				request);
		String status = "";
		LOG.debug("QRInterBankFundTransfer:createIntraBankTransaction:intrabanktransactionDTO:"
				+ intrabanktransactionDTO.toString());
		LOG.debug("QRInterBankFundTransfer:createIntraBankTransaction:intrabanktransactionDTO.getReferenceId():"
				+ intrabanktransactionDTO.getReferenceId());
		if (intrabanktransactionDTO == null) {
			status = TransactionStatusEnum.FAILED.getStatus();
			ErrorCodeEnum.ERR_12601.setErrorCode(result);
		}
		if (intrabanktransactionDTO.getDbpErrCode() != null || intrabanktransactionDTO.getDbpErrMsg() != null) {
			status = TransactionStatusEnum.FAILED.getStatus();
			result.addParam(new Param("errorDetails", intrabanktransactionDTO.getErrorDetails()));
			result.addParam(new Param("dbpErrCode", intrabanktransactionDTO.getDbpErrCode()));
			result.addParam(new Param("dbpErrMsg", intrabanktransactionDTO.getDbpErrMsg()));

		} else if (intrabanktransactionDTO.getReferenceId() == null
				|| "".equals(intrabanktransactionDTO.getReferenceId())) {
			status = TransactionStatusEnum.FAILED.getStatus();
			ErrorCodeEnum.ERR_12601.setErrorCode(result);
		} else {
			confirmationNumber = intrabanktransactionDTO.getReferenceId();
			status = TransactionStatusEnum.EXECUTED.getStatus();
			result.addParam(new Param("referenceId", confirmationNumber));
			result.addParam(new Param("intraBankDbxTransID", intraBankDbxTransID));
			result.addParam(new Param("paymentSystemId", intrabanktransactionDTO.getPaymentId()));
			result.addParam(new Param("status", TransactionStatusEnum.EXECUTED.getStatus()));
			result.addParam(new Param("message", TransactionStatusEnum.EXECUTED.getMessage()));
			result.addParam(new Param("transactionStatus", intrabanktransactionDTO.getStatus()));
		}
		LOG.debug("QRInterBankFundTransfer:createIntraBankTransaction:status:" + status);
		LOG.debug("QRInterBankFundTransfer:createIntraBankTransaction:confirmationNumber:" + confirmationNumber);
		Map<String, Object> confirmationDetails = new HashMap<String, Object>();
		confirmationDetails.put("confirmationNumber", confirmationNumber);
		confirmationDetails.put("status", status);
		confirmationDetails.put("transactionId", intraBankDbxTransID);
		confirmationDetails.put("paymentSystemId", intrabanktransactionDTO.getPaymentId());
		confirmationDetails.put("paymentId", confirmationNumber);
		updateIntraBankTransaction(confirmationDetails);

		return result;

	}

	public IntraBankFundTransferDTO updateIntraBankTransaction(Map<String, Object> confirmationDetails) {

		List<IntraBankFundTransferDTO> intrabankfundtransferdto = null;

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_INTRABANKTRANSFERS_UPDATE;
		LOG.debug("InterBankFundTransferBackendDelegateImplExtn:updateIntraBankTransaction:confirmationDetails:"
				+ confirmationDetails);
		try {
			String updateResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(confirmationDetails).build().getResponse();
			JSONObject jsonRsponse = new JSONObject(updateResponse);
			JSONArray intrabankJsonArray = CommonUtils.getFirstOccuringArray(jsonRsponse);
			intrabankfundtransferdto = JSONUtils.parseAsList(intrabankJsonArray.toString(),
					IntraBankFundTransferDTO.class);
		}

		catch (JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while updating the intrabanktransaction", jsonExp).log();
			return null;
		} catch (Exception exp) {
			alert.prepareError("Excpetion occured while updating the intrabanktransaction", exp).log();
			return null;
		}

		if (intrabankfundtransferdto != null && intrabankfundtransferdto.size() != 0)
			return intrabankfundtransferdto.get(0);

		return null;
	}

	public IntraBankFundTransferDTO reverseTransaction(Map<String, Object> inputParams, DataControllerRequest request) {
		Result result = new Result();
		IntraBankFundTransferDTO intrabankDTO = new IntraBankFundTransferDTO();
		String status = "";
		String message = "";
		String intraBankDbxTransID = inputParams.get("intraBankDbxTransID") != null
				? inputParams.get("intraBankDbxTransID").toString()
				: "";
		try {
			result = callInternalServiceAndGetResult(REVERSE_TRANSACTION_SERVICE, REVERSE_TRANSACTION_OPEARATION,
					inputParams, request.getHeaderMap());
			JSONObject reverseTxResponse = new JSONObject(ResultToJSON.convert(result));
			LOG.debug("reverseTransaction response:" + reverseTxResponse);
			if (result.getParamValueByName("dbpErrCode") != null || result.getParamValueByName("dbpErrMsg") != null) {
				status = TransactionStatusEnum.REVERSAL_FAILED.getStatus();
				message = TransactionStatusEnum.FAILED.getStatus();
				intrabankDTO.setDbpErrCode(result.getParamValueByName("dbpErrCode"));
				intrabankDTO.setDbpErrMsg(result.getParamValueByName("dbpErrMsg"));
			}
			String reversalStatus = result.getParamValueByName("status");
			String id = result.getParamValueByName("id");
			String transactionMessage = result.getParamValueByName("message");
			if (StringUtils.isNotBlank(reversalStatus) && reversalStatus.equalsIgnoreCase("success")) {
				message = TransactionStatusEnum.REVERSED.getStatus();
				status = TransactionStatusEnum.REVERSED.getStatus();
				intrabankDTO.setReferenceId(id);
			} else {
				status = TransactionStatusEnum.REVERSAL_FAILED.getStatus();
				message = transactionMessage;
			}
		} catch (DBPApplicationException e) {
			LOG.debug("Exception Occured while reversing the transaction" + e.toString());
			status = TransactionStatusEnum.REVERSAL_FAILED.getStatus();
			message = e.getLocalizedMessage();
			intrabankDTO.setDbpErrCode(ErrorCodeEnum.ERR_12601.getErrorCodeAsString());
			intrabankDTO.setDbpErrMsg(TransactionStatusEnum.REVERSAL_FAILED.getMessage());
			intrabankDTO.setErrorDetails(message);
		}
		intrabankDTO.setErrorDetails(message);
		intrabankDTO.setStatus(status);
		intrabankDTO.setMessage(message);
		Map<String, Object> confirmationDetails = new HashMap<String, Object>();
		confirmationDetails.put("status", status);
		confirmationDetails.put("transactionId", intraBankDbxTransID);
		updateIntraBankTransaction(confirmationDetails);
		return intrabankDTO;

	}

	private Result callInternalServiceAndGetResult(String serviceid, String operationid, Map<String, Object> inputmap,
			Map<String, Object> headers) throws DBPApplicationException {
		LOG.debug("HBL::QRInterBankFundTransfer: callInternalServiceAndGetString: inputmap:" + inputmap.toString());
		Result res = DBPServiceExecutorBuilder.builder().withOperationId(operationid).withRequestParameters(inputmap)
				.withServiceId(serviceid).withRequestHeaders(headers).build().getResult();
		LOG.debug("HBL::QRInterBankFundTransfer: callInternalServiceAndGetString: response:"
				+ res.getHttpStatusCodeParamValue());

		return res;
	}

	@SuppressWarnings("deprecation")
	public JSONObject getTransactionStatus(Map<String, Object> requestParameters,
			DataControllerRequest dataControllerRequest) {
		JSONArray resArray = new JSONArray();
		String serviceName = GET_TRANSACTION_STAUS_SERVICE_ORCH;
		String operationName = GET_TRANSACTION_STAUS_OPERATION;
		String orchServiceResponse = null;
		JSONObject paymentObj = new JSONObject();

		try {
			diagnostic.prepareDebug("getTransactionStatus:OrchService:requestParameters:" + requestParameters).log();
			orchServiceResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null, operationName,
					requestParameters, null, dataControllerRequest);
			dataControllerRequest.addRequestParam_(TemenosConstants.FLOW_TYPE, TemenosConstants.PRE_LOGIN_FLOW);
			String authToken = TokenUtils.getT24AuthToken(dataControllerRequest);
			diagnostic.prepareDebug("getTransactionStatus:OrchService:Authorization:" + authToken).log();
			// dataControllerRequest.getHeaderMap().put("Authorization", authToken);
			orchServiceResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(requestParameters)
					.withRequestHeaders(dataControllerRequest.getHeaderMap())
					.withDataControllerRequest(dataControllerRequest).build().getResponse();
			JSONObject transactionResponse = new JSONObject(orchServiceResponse);
			diagnostic.prepareDebug("getTransactionStatus:OrchService:transactionResponse:" + transactionResponse)
					.log();
			resArray = transactionResponse.getJSONArray("LoopDataset");
			paymentObj = processOrchServceResponse(resArray);
			return paymentObj;
		} catch (JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured getTransactionStatus: ", jsonExp).log();
			return paymentObj;
		} catch (Exception exp) {
			alert.prepareError("Excpetion occured getTransactionStatus: ", exp).log();
			return paymentObj;
		}
	}

	public JSONObject processOrchServceResponse(JSONArray resArray) {
		String status = "";
		JSONObject obj = new JSONObject();
		if (resArray != null) {
			for (int i = 0; i < resArray.length(); i++) {
				obj = resArray.getJSONObject(i);
				status = obj.getString("currentStatus");
				if (status.equalsIgnoreCase("Complete")) {
					break;
				}
			}
		}
		return obj;
	}

	public InterBankFundTransferDTO processExtenalServiceResponseNew(String response,
			InterBankFundTransferDTO interbankfundtransferdto, DataControllerRequest request,
			QRTransactionDTO qrTransactionDto) {
		JSONObject externalResponseObj = new JSONObject(response);
		interbankfundtransferdto.setExternalServiceResponse(externalResponseObj);
		String paymentType = interbankfundtransferdto.getExternalApiPayload().get("paymentType") != null
				? interbankfundtransferdto.getExternalApiPayload().get("paymentType").toString()
				: "";
		LOG.debug("InterBankFundTransferBackendDelegateImplExtn:externalResponseObj:" + externalResponseObj);
		JSONArray cipsTxnResponseList = externalResponseObj.has("cipsTxnResponseList")
				? externalResponseObj.getJSONArray("cipsTxnResponseList")
				: new JSONArray();
		LOG.debug("InterBankFundTransferBackendDelegateImplExtn:cipsTxnResponseList:" + cipsTxnResponseList);
		String transactionId = "";
		String status = "";
		String creditStatus = "";
		String responseCode = "";
		if (cipsTxnResponseList.length() > 0) {
			transactionId = cipsTxnResponseList.getJSONObject(0).getString("id");
			status = cipsTxnResponseList.getJSONObject(0).getString("responseMessage");
			creditStatus = cipsTxnResponseList.getJSONObject(0).getString("creditStatus");
			responseCode = cipsTxnResponseList.getJSONObject(0).getString("responseCode");
			if (paymentType.equalsIgnoreCase("CIPS") && creditStatus.equalsIgnoreCase("000")
					&& responseCode.equalsIgnoreCase("000")) {
				interbankfundtransferdto.setTransactionId(transactionId);
				interbankfundtransferdto.setStatus(status);
				interbankfundtransferdto.setMessage(creditStatus);
				qrTransactionDto.setReferenceId(transactionId);
				qrTransactionDto.setStatus(status);
				qrTransactionDto.setDescription(creditStatus);
			} else if (paymentType.equalsIgnoreCase("IPS") && creditStatus.equalsIgnoreCase("ENTR")
					&& responseCode.equalsIgnoreCase("ENTR")) {
				interbankfundtransferdto.setTransactionId(transactionId);
				interbankfundtransferdto.setStatus(status);
				interbankfundtransferdto.setMessage(creditStatus);
				qrTransactionDto.setReferenceId(transactionId);
				qrTransactionDto.setStatus(status);
				qrTransactionDto.setDescription(creditStatus);
			} else {
				interbankfundtransferdto.setMessage(status);
				qrTransactionDto.setDescription(status);
				interbankfundtransferdto = processReversalTransactionNew(interbankfundtransferdto, request,
						qrTransactionDto);
			}
		} else {
			interbankfundtransferdto = processReversalTransactionNew(interbankfundtransferdto, request,
					qrTransactionDto);
		}
		return interbankfundtransferdto;

	}

	public InterBankFundTransferDTO processReversalTransactionNew(InterBankFundTransferDTO interbankfundtransferdto,
			DataControllerRequest request, QRTransactionDTO qrTransactionDto) {
		/*
		 * interbankfundtransferdto.setDbpErrCode(ErrorCodeEnum.ERR_12601.
		 * getErrorCodeAsString());
		 * interbankfundtransferdto.setDbpErrMsg(TransactionStatusEnum.REVERSAL_FAILED.
		 * getMessage()); interbankfundtransferdto.setPaymentNote("FAILED AT NCHL");
		 * interbankfundtransferdto.setStatus(TransactionStatusEnum.FAILED.getStatus());
		 * interbankfundtransferdto.setErrorDetails("FAILED AT NCHL");
		 * interbankfundtransferdto.setPaymentId(interbankfundtransferdto.getRequestId()
		 * );
		 */

		interbankfundtransferdto.setTransactionId(null);
		interbankfundtransferdto.setPaymentId(interbankfundtransferdto.getRequestId());
		qrTransactionDto.setTransactRefId(interbankfundtransferdto.getRequestId());
		interbankfundtransferdto.setDbpErrCode(ErrorCodeEnum.ERR_12601.getErrorCodeAsString());
		interbankfundtransferdto.setDbpErrMsg(TransactionStatusEnum.REVERSED.getMessage());
		qrTransactionDto.setErrmsg(TransactionStatusEnum.REVERSED.getMessage());
		interbankfundtransferdto.setPaymentNote("FAILED AT NCHL");
		qrTransactionDto.setDescription("FAILED AT NCHL");
		interbankfundtransferdto.setMessage(TransactionStatusEnum.REVERSED.getMessage());
		interbankfundtransferdto.setStatus(TransactionStatusEnum.REVERSED.getStatus());
		qrTransactionDto.setStatus(TransactionStatusEnum.REVERSED.getStatus());

		return interbankfundtransferdto;
	}

	public JSONObject getBranchDetails(String bankId, DataControllerRequest dcRequest) {
		JSONArray legacycustomers = new JSONArray();
		String serviceName = "HBLMerchantCRUDService";
		String operationName = "dbxdb_branchdetails_get";
		JSONObject branchObj = new JSONObject();

		try {
			Map<String, Object> inputParams = new HashMap<>();
			String filter = "bank_cd" + DBPUtilitiesConstants.EQUAL + "'" + bankId + "'";
			String bankName = "bank_name";
			String branchCode = "branch_cd";
			inputParams.put(DBPUtilitiesConstants.FILTER, filter);
			inputParams.put(DBPUtilitiesConstants.SELECT, bankName + "," + branchCode);
			Result response = com.kony.dbx.util.CommonUtils.callIntegrationService(dcRequest, inputParams,
					dcRequest.getHeaderMap(), serviceName, operationName, false);
			if (response != null) {
				JSONObject responseObj = new JSONObject(ResultToJSON.convert(response));
				legacycustomers = responseObj.getJSONArray("branchdetails");
				if (legacycustomers != null && legacycustomers.length() > 0) {
					branchObj = legacycustomers.getJSONObject(0);
				}
			}
		} catch (DBPApplicationException e) {
			alert.prepareError("Exception occured while getting the legacy customers in BulkAutoEnrollRetailCustomer"
					+ e.getMessage());
		} catch (Exception e) {
			alert.prepareError("Exception occured while getting the legacy customers in BulkAutoEnrollRetailCustomer"
					+ e.getMessage());
		}
		return branchObj;
	}

	public String formatAmount(String amount) {
		if (StringUtils.isNotBlank(amount)) {
			try {
				BigDecimal decimalAmount = new BigDecimal(amount).setScale(2);
				amount = String.valueOf(decimalAmount);
			} catch (Exception e) {
				LOG.error("Exception Occured in formatAmount:", e);
			}
		}
		return amount;
	}

	public String maskAccountNumber(String data, int fromIndex, int toIndex, char maskWith) {
		StringBuilder maskedPart = new StringBuilder();
		if (StringUtils.isNotBlank(data)) {
			for (int i = fromIndex; i < toIndex; i++)
				maskedPart.append(maskWith);
			return data.replace(data.substring(fromIndex, toIndex), maskedPart.toString());
		}
		return data;
	}

	public void splitRequestPayload(JSONObject requestJson, Map<String, Object> requestMap) {
		if (requestJson != null) {
			Iterator<String> iter = requestJson.keys();
			while (iter.hasNext()) {
				String key = iter.next();
				String value = requestJson.optString(key);
				requestMap.put(key, value);
			}
		}

	}

	public InterBankFundTransferDTO createTransactionAtDBX(InterBankFundTransferDTO interbankfundtransferdto) {

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_INTERBANKFUNDTRANSFERS_CREATE;
		String createResponse = null;

		Map<String, Object> requestParameters;
		Map<String, Object> internalServicePayload;
		Map<String, Object> externalServicePayload;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(interbankfundtransferdto).toString(), String.class,
					Object.class);
			internalServicePayload = interbankfundtransferdto.getInternalApiPayload();
			externalServicePayload = interbankfundtransferdto.getExternalApiPayload();
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			return null;
		}
		try {
			requestParameters.put("createdts",
					new SimpleDateFormat(Constants.TIMESTAMP_FORMAT).parse(application.getServerTimeStamp()));
			requestParameters.put("transactionId", HelperMethods.getUniqueNumericString(13));
			JSONObject jsonPayload;
			if (internalServicePayload != null) {
				jsonPayload = new JSONObject(internalServicePayload);
				requestParameters.put("internalServicePayload", jsonPayload.toString());
			}
			if (externalServicePayload != null) {
				jsonPayload = new JSONObject(externalServicePayload);
				requestParameters.put("externalServicePayload", jsonPayload.toString());
			}
			requestParameters.put("scheduledDate", Timestamp.valueOf(interbankfundtransferdto.getScheduledDate()));
			alert.prepareError("requestParameters while insertion::: " + requestParameters).log();

			createResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(requestParameters).build().getResponse();

			JSONObject response = new JSONObject(createResponse);
			JSONArray resposneArray = CommonUtils.getFirstOccuringArray(response);

			interbankfundtransferdto = JSONUtils.parse(resposneArray.getJSONObject(0).toString(),
					InterBankFundTransferDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Failed to create interbank transaction entry into interbanktransfers table: " + e)
					.log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at create interbank transaction entry: " + e).log();
			return null;
		}

		return interbankfundtransferdto;
	}

}
