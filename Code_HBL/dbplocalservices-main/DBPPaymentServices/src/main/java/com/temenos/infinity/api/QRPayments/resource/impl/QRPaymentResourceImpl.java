package com.temenos.infinity.api.QRPayments.resource.impl;

import static com.temenos.infinity.api.QRPayments.constants.Constants.INTERNAL_BANK_ACCOUNTS;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.infinity.api.QRPayments.constants.FeatureAction;
import com.temenos.infinity.api.QRPayments.businessdelegate.api.QRPaymentBusinessDelegate;
import com.temenos.infinity.api.QRPayments.constants.ErrorCodeEnum;
import com.temenos.infinity.api.QRPayments.dto.QRPaymentDTO;
import com.temenos.infinity.api.QRPayments.resource.api.QRPaymentResource;
import com.temenos.infinity.api.srmstransactions.dto.SessionMap;
import com.temenos.infinity.api.srmstransactions.utils.MemoryManagerUtils;

public class QRPaymentResourceImpl implements QRPaymentResource {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	QRPaymentBusinessDelegate QRPaymentBusinessDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(QRPaymentBusinessDelegate.class);

	public Result createQRPayment(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		QRPaymentDTO QRPaymentDTO = null;
		QRPaymentDTO QRPaymentOutput = null;
		Result result = new Result();
		// Validating customerId from session
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String customerId = CustomerSession.getCustomerId(customer);
		if (customerId == null)
			return ErrorCodeEnum.ERR_12603.setErrorCode(result);
		String createdby = CustomerSession.getCustomerId(customer);
		String legalEntityId = (String) customer.get("legalEntityId");
		String fromAccountNumber = inputParams.get("fromAccountNumber").toString();
		String featureActionId = null;
		// Validating Account Number for the customer
		if (!validateAccount(fromAccountNumber, customerId)) {
			alert.prepareError("User is not Authorized for the account number").log();
			return ErrorCodeEnum.ERR_12604.setErrorCode(result);
		}
		double amountValue = Double
				.parseDouble(String.valueOf(inputParams.get("amount").equals("") ? 0.0 : inputParams.get("amount")));
		String toAccountNumber = inputParams.get("toAccountNumber").toString();
		// Validating amount value
		if (amountValue <= 0.0)
			return ErrorCodeEnum.ERR_27017.setErrorCode(new Result());
		// Validating fromAccountNumber and toAccountNumber
		if ((fromAccountNumber != null && fromAccountNumber != "") && (toAccountNumber != null && toAccountNumber != "")
				&& fromAccountNumber.equals(toAccountNumber)) {
			return ErrorCodeEnum.ERR_12307.setErrorCode(new Result());
		}
		// Validating fromAcountCurrency and transactionCurrency
		if (!(inputParams.get("fromAccountCurrency").equals(inputParams.get("transactionCurrency"))))
			return ErrorCodeEnum.ERR_12602.setErrorCode(new Result());

		featureActionId = FeatureAction.QR_PAYMENTS_CREATE;

		AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);

		if (!authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(createdby, featureActionId,
				fromAccountNumber, CustomerSession.IsCombinedUser(customer))) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}

		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String str = formatter.format(date);
		inputParams.put("date", str);
		try {
			QRPaymentDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), QRPaymentDTO.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: ", e).log();
			return ErrorCodeEnum.ERR_28021.setErrorCode(new Result());
		}
		QRPaymentOutput = QRPaymentBusinessDelegate.createQRPayment(QRPaymentDTO, request);
		if (QRPaymentOutput.getDbpErrCode() != null || QRPaymentOutput.getDbpErrMsg() != null) {
			result.addParam(new Param("errorDetails", QRPaymentOutput.getErrorDetails()));
			return ErrorCodeEnum.ERR_00000.setErrorCode(result);
		}
		if (QRPaymentOutput.getReferenceId() == null || "".equals(QRPaymentOutput.getReferenceId())) {
			return ErrorCodeEnum.ERR_12601.setErrorCode(result);
		}
		String responseObj = new JSONObject(QRPaymentDTO).toString();
		result = JSONToResult.convert(responseObj);
		result.addParam("referenceId", QRPaymentOutput.getReferenceId());
		result.addParam("status", "Success");
		return result;
	}

	private boolean validateAccount(String accountId, String customerId) {
		SessionMap accountMap = getInternalAccountsFromSession(customerId);
		if (!accountMap.hasKey(accountId))
			return false;
		String accountType = accountMap.getAttributeValueForKey(accountId, "accountType");
		return accountType.equalsIgnoreCase("Savings") || accountType.equalsIgnoreCase("Checking");
	}

	private SessionMap getInternalAccountsFromSession(String customerId) {
		return (SessionMap) MemoryManagerUtils.retrieve(INTERNAL_BANK_ACCOUNTS + customerId);
	}

}
