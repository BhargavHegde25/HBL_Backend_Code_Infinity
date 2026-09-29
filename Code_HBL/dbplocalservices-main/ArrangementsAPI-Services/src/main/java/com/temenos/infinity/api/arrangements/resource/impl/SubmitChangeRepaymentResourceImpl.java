package com.temenos.infinity.api.arrangements.resource.impl;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.constants.ErrorCodeEnum;
import com.temenos.infinity.api.arrangements.dto.MortgageRepaymentDTO;
import com.temenos.infinity.api.arrangements.resource.api.SubmitChangeRepaymentResource;
import com.temenos.infinity.api.arrangements.businessdelegate.api.SubmitChangeRepaymentBusinessDelegate;


public class SubmitChangeRepaymentResourceImpl implements SubmitChangeRepaymentResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Result SubmitChangeRepaymentDay(MortgageRepaymentDTO changeRepaymentday, DataControllerRequest request, HashMap<String, Object> headerMap) {
		Result result = new Result();

		
		SubmitChangeRepaymentBusinessDelegate repaymentDayBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(SubmitChangeRepaymentBusinessDelegate.class);
		
       
			try {
				changeRepaymentday = repaymentDayBusinessDelegate.SubmitChangeRepaymentDay(changeRepaymentday, headerMap);

				if (StringUtils.isBlank(changeRepaymentday.getId())) {

					return ErrorCodeEnum.ERR_20055.setErrorCode(new Result());
				}
				if (StringUtils.isNotBlank(changeRepaymentday.getErrorMessage())) {
					String dbpErrCode = changeRepaymentday.getCode();
					String dbpErrMessage = changeRepaymentday.getErrorMessage();
					if (StringUtils.isNotBlank(dbpErrMessage)) {
						String msg = ErrorCodeEnum.ERR_20061.getMessage(changeRepaymentday.getId(),
								changeRepaymentday.getStatus(), dbpErrMessage);
						return ErrorCodeEnum.ERR_20061.setErrorCode(new Result(), msg);
					}
				}
				JSONObject repaymentDayDTO = new JSONObject(changeRepaymentday);
				result = JSONToResult.convert(repaymentDayDTO.toString());

			} catch (Exception e) {
				alert.prepareError(e.toString()).log();
				diagnostic.prepareDebug("Failed to update repaymentDay in OMS " + e).log();
				return ErrorCodeEnum.ERR_20052.setErrorCode(new Result());
			}
		
		return result;
	}

	@Override
	public Result SubmitChangeRepaymentAccount(MortgageRepaymentDTO changeRepaymentAccount, DataControllerRequest request, HashMap<String, Object> headerMap) {
		Result result = new Result();

		
		SubmitChangeRepaymentBusinessDelegate repaymentDayBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(SubmitChangeRepaymentBusinessDelegate.class);
		
       
			try {
				changeRepaymentAccount = repaymentDayBusinessDelegate.SubmitChangeRepaymentAccount(changeRepaymentAccount, headerMap);

				if (StringUtils.isBlank(changeRepaymentAccount.getId())) {

					return ErrorCodeEnum.ERR_20055.setErrorCode(new Result());
				}
				if (StringUtils.isNotBlank(changeRepaymentAccount.getErrorMessage())) {
					String dbpErrCode = changeRepaymentAccount.getCode();
					String dbpErrMessage = changeRepaymentAccount.getErrorMessage();
					if (StringUtils.isNotBlank(dbpErrMessage)) {
						String msg = ErrorCodeEnum.ERR_20061.getMessage(changeRepaymentAccount.getId(),
								changeRepaymentAccount.getStatus(), dbpErrMessage);
						return ErrorCodeEnum.ERR_20061.setErrorCode(new Result(), msg);
					}
				}
				JSONObject repaymentDayDTO = new JSONObject(changeRepaymentAccount);
				result = JSONToResult.convert(repaymentDayDTO.toString());

			} catch (Exception e) {
				alert.prepareError(e.toString()).log();
				diagnostic.prepareDebug("Failed to update repaymentDay in OMS " + e).log();
				return ErrorCodeEnum.ERR_20052.setErrorCode(new Result());
			}
		
		return result;
	}

}
