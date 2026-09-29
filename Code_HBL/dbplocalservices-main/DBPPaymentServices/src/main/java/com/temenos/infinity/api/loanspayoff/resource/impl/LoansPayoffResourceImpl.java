package com.temenos.infinity.api.loanspayoff.resource.impl;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.loanspayoff.businessdelegate.api.LoansPayoffBusinessDelegate;
import com.temenos.infinity.api.loanspayoff.constants.ErrorCodeEnum;
import com.temenos.infinity.api.loanspayoff.dto.BillDetails;
import com.temenos.infinity.api.loanspayoff.dto.LoanDTO;
import com.temenos.infinity.api.loanspayoff.resource.api.LoansPayoffResource;

public class LoansPayoffResourceImpl implements LoansPayoffResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Result createSimulation(String arrangementId, String activityId, String productId, String effectiveDate,String backendToken) {

		Result result = new Result();

		try {

			LoansPayoffBusinessDelegate simulateBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(LoansPayoffBusinessDelegate.class);
			LoanDTO OutputLoanDTO = simulateBusinessDelegate.createSimulation(arrangementId, activityId, productId,
					effectiveDate,backendToken);
			if (OutputLoanDTO.getBackendError() != null && OutputLoanDTO.getBackendError().length() > 0) {
				Result errorResult = ErrorCodeEnum.ERR_20002.setErrorCode(new Result());
				errorResult.addParam(new Param("backendError", OutputLoanDTO.getBackendError(), "string"));
				if (OutputLoanDTO.getBackendOverride() != null && OutputLoanDTO.getBackendOverride().length() > 0) {
					errorResult.addParam(new Param("backendOverride", OutputLoanDTO.getBackendOverride(), "string"));

				}
				return errorResult;

			}
			if (OutputLoanDTO.getBackendOverride() != null && OutputLoanDTO.getBackendOverride().length() > 0) {
				Result overrideResult = ErrorCodeEnum.ERR_20002.setErrorCode(new Result());
				overrideResult.addParam(new Param("backendOverride", OutputLoanDTO.getBackendOverride(), "string"));
				if (OutputLoanDTO.getBackendError() != null && OutputLoanDTO.getBackendError().length() > 0) {
					overrideResult.addParam(new Param("backendError", OutputLoanDTO.getBackendError(), "string"));

				}
				return overrideResult;

			}
			JSONObject responseObj = new JSONObject(OutputLoanDTO);
			result = JSONToResult.convert(responseObj.toString());

		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
			return ErrorCodeEnum.ERR_20002.setErrorCode(new Result());
		}
		return result;
	}

	@Override
	public Result getBillDetails(String arrangementId, String billType, String paymentDate,String backendToken) {

		Result result = new Result();

		try {

			LoansPayoffBusinessDelegate getBillDetailsBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(LoansPayoffBusinessDelegate.class);
			BillDetails OutputDTO = getBillDetailsBusinessDelegate.getBillDetails(arrangementId, billType, paymentDate,backendToken);
			if (OutputDTO.getBackendError() != null && OutputDTO.getBackendError().length() > 0) {
				Result errorResult = ErrorCodeEnum.ERR_20041.setErrorCode(new Result());
				errorResult.addParam(new Param("backendError", OutputDTO.getBackendError(), "string"));
				return errorResult;

			}
			if (OutputDTO.getBackendOverride() != null && OutputDTO.getBackendOverride().length() > 0) {
				Result overrideResult = ErrorCodeEnum.ERR_20041.setErrorCode(new Result());
				overrideResult.addParam(new Param("backendOverride", OutputDTO.getBackendOverride(), "string"));
				return overrideResult;

			}
			JSONObject responseObj = new JSONObject(OutputDTO);
			result = JSONToResult.convert(responseObj.toString());

		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
			return ErrorCodeEnum.ERR_20041.setErrorCode(new Result());
		}
		return result;
	}

}