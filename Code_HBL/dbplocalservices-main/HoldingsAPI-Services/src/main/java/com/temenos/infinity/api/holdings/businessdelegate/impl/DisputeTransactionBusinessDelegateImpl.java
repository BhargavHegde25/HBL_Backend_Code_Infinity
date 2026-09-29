package com.temenos.infinity.api.holdings.businessdelegate.impl;

import java.util.Map;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;
import com.temenos.infinity.api.holdings.constants.OperationName;
import com.temenos.infinity.api.holdings.constants.ServiceId;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.holdings.businessdelegate.api.DisputeTransactionBusinessDelegate;

public class DisputeTransactionBusinessDelegateImpl implements DisputeTransactionBusinessDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Result createDisputeCoreTransaction(Map<String, Object> disputeRequest, Map<String, Object> request) {

		String serviceName = ServiceId.DISPUTE_TRANSACTION_JAVA;
		String operationName = OperationName.TRANSFER_UPDATE_OPERATION;

		Result result = new Result();

		try {
			String fetchResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null, operationName,
					disputeRequest, request, request.get(DBPUtilitiesConstants.X_KONY_AUTHORIZATION_VALUE).toString());
			result = JSONToResult.convert(fetchResponse.toString());
			return result;

		} catch (Exception e) {
			alert.prepareError("Caught exception at invoke : " + e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
	}

	@Override
	public Result createDisputeBillPayTransaction(Map<String, Object> disputeRequest, Map<String, Object> request) {
		String serviceName = ServiceId.DISPUTE_TRANSACTION_JAVA;
		String operationName = OperationName.TRANSFER_UPDATE_OPERATION;

		Result result = new Result();

		try {
			String fetchResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null, operationName,
					disputeRequest, request, request.get(DBPUtilitiesConstants.X_KONY_AUTHORIZATION_VALUE).toString());
			result = JSONToResult.convert(fetchResponse.toString());
			return result;

		} catch (Exception e) {
			alert.prepareError("Caught exception at invoke : " + e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
	}

	@Override
	public Result crearteDisputePayAPersonTransaction(Map<String, Object> disputeRequest,
			Map<String, Object> request) {
		// TODO Auto-generated method stub
		String serviceName = ServiceId.DISPUTE_TRANSACTION_JAVA;
		String operationName = OperationName.TRANSFER_UPDATE_OPERATION;

		Result result = new Result();

		try {
			String fetchResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null, operationName,
					disputeRequest, request, request.get(DBPUtilitiesConstants.X_KONY_AUTHORIZATION_VALUE).toString());
			result = JSONToResult.convert(fetchResponse.toString());
			return result;

		} catch (Exception e) {
			alert.prepareError("Caught exception at invoke : " + e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
	}

	@Override
	public Result crearteDisputeCardTransaction(Map<String, Object> disputeRequest, Map<String, Object> request) {

		// TODO Auto-generated method stub
		String serviceName = ServiceId.DBP_CARD_SERVICE;
		String operationName = OperationName.CARD_TRANSFER_UPDATE_OPERATION;

		Result result = new Result();

		try {
			String fetchResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null, operationName,
					disputeRequest, request, request.get(DBPUtilitiesConstants.X_KONY_AUTHORIZATION_VALUE).toString());
			result = JSONToResult.convert(fetchResponse.toString());
			return result;

		} catch (Exception e) {
			alert.prepareError("Caught exception at invoke : " + e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
	}


}
