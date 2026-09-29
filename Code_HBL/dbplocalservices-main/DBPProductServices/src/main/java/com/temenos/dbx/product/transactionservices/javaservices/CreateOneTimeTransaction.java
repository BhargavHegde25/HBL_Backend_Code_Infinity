package com.temenos.dbx.product.transactionservices.javaservices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.factory.ResourceFactory;
import com.infinity.dbx.dbp.jwt.auth.utils.CommonUtils;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.ErrorConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.constants.FeatureAction;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.payeeservices.constants.PayeeVerificationBackendServicesHelper;
import com.temenos.dbx.product.transactionservices.resource.api.InterBankFundTransferResource;
import com.temenos.dbx.product.transactionservices.resource.api.InternationalFundTransferResource;
import com.temenos.dbx.product.transactionservices.resource.api.IntraBankFundTransferResource;

public class CreateOneTimeTransaction implements JavaService2{
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	public static final int FILENAME_INDEX = 0;
	public static final int FILETYPE_INDEX = 1;
	public static final int FILECONTENTS_INDEX = 2;
	public static final int UNIQUE_ID_LENGTH = 32;
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		String referenceId = null;
		Result transactionResult = new Result();
		String operationName = null;
		String payeeVerificationStatus = "";
		String payeeVerificationErrMsg = "";
		HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
		try {
			HashMap<String, Object> params = (HashMap<String, Object>) inputArray[1];
			Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
			String serviceDifferentiator = inputParams.get("serviceName");
			String serviceName = ServiceId.DBP_TRANSACTION_SERVICES;
			String verifyPayee = (String) params.get("payeeVerification");
			String isValidate = (String) params.get("validate");
			if (StringUtils.isBlank(isValidate) || (StringUtils.isNotBlank(isValidate) && isValidate.equalsIgnoreCase("false"))){
				if ("true".equalsIgnoreCase(verifyPayee)) {
					payeeVerificationStatus = "Success";
					Result payeeVerificationResult = PayeeVerificationBackendServicesHelper.fetchVerifyPayeeResponse(request, params);
					if (payeeVerificationResult == null) {
						alert.prepareError("Error occured while invoking payee verification services: ").log();
						return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
					} else {
						payeeVerificationStatus = payeeVerificationResult.getParamValueByName("payeeVerificationStatus");
						payeeVerificationErrMsg = payeeVerificationResult.getParamValueByName("payeeVerificationErrMsg");
					}
				} else if ("false".equalsIgnoreCase(verifyPayee)) {
					payeeVerificationStatus = "Skipped";
				} 
			}
			if ("".equals(payeeVerificationErrMsg)) {
				if (FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE.equalsIgnoreCase(serviceDifferentiator)) {
					operationName = OperationName.CREATE_INTERNATIONAL_FUND_TRANSACTION;
					transactionResult = CommonUtils.callIntegrationService(request, params, serviceHeaders, serviceName,
							operationName, true);
				} else if (FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE
						.equalsIgnoreCase(serviceDifferentiator)) {
					operationName = OperationName.CREATE_INTER_BANK_FUND_TRANSACTION;
					transactionResult = CommonUtils.callIntegrationService(request, params, serviceHeaders, serviceName,
							operationName, true);
				} else if (FeatureAction.INTRA_BANK_FUND_TRANSFER_CREATE.equalsIgnoreCase(serviceDifferentiator)) {
					operationName = OperationName.CREATE_INTRA_BANK_FUND_TRANSACTION;
					transactionResult = CommonUtils.callIntegrationService(request, params, serviceHeaders, serviceName,
							operationName, true);
				} else {
					return ErrorCodeEnum.ERR_29021.setErrorCode(new Result());
				}
			}
			
		}
		catch (Exception e) {
			alert.prepareError("Error occured while invoking CreateOneTimeTransaction: ", e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		if (!"".equals(payeeVerificationStatus)) {
			transactionResult.addParam(new Param("payeeVerificationStatus", payeeVerificationStatus));
		}
		if (!"".equals(payeeVerificationErrMsg))
			transactionResult.addParam(new Param("payeeVerificationErrMsg", payeeVerificationErrMsg));
		return transactionResult;
	}
}
