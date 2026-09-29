package com.temenos.infinity.api.chequemanagement.resource.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.chequemanagement.businessdelegate.api.ChequeManagementBusinessDelegate;
import com.temenos.infinity.api.chequemanagement.dto.ApprovalRequestDTO;
import com.temenos.infinity.api.chequemanagement.dto.ChequeBookAction;
import com.temenos.infinity.api.chequemanagement.resource.api.ChequeManagementResource;

public class ChequeManagementResourceImpl implements ChequeManagementResource{

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	public Result rejectChequeBook(DataControllerRequest request) {
        Result result = new Result();
        try {

            ChequeManagementBusinessDelegate chequeManagementBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(ChequeManagementBusinessDelegate.class);
            ChequeBookAction chequeBookAction = chequeManagementBusinessDelegate.rejectChequeBook(request);
            JSONObject responseObj = new JSONObject(chequeBookAction);
            result = JSONToResult.convert(responseObj.toString());

        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            return ErrorCodeEnum.ERR_26021.setErrorCode(new Result());
        }
        return result;
	}
	
	@Override
	public Result withdrawCheque(DataControllerRequest request) {
		Result result = new Result();
		String requestId = request.getParameter("requestId") != null ? request.getParameter("requestId") : "";
		if(StringUtils.isBlank(requestId)) {
			return ErrorCodeEnum.ERR_10503.setErrorCode(new Result());
		}
		try {
			ChequeManagementBusinessDelegate chequeManagementBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ChequeManagementBusinessDelegate.class);
			ChequeBookAction chequeBookAction = chequeManagementBusinessDelegate.withdrawCheque(request);
			JSONObject chequeBookActionDTO = new JSONObject(chequeBookAction);
			result = JSONToResult.convert(chequeBookActionDTO.toString());
		}
		catch(Exception e) {
			alert.prepareError(e.toString()).log();
			return ErrorCodeEnum.ERR_26021.setErrorCode(new Result());
		}
		return result;
		
	}

	@Override
	public Result fetchChequeDetails(DataControllerRequest request) {
		Result result = new Result();
		String requestId = request.getParameter("transactionIds") != null ? request.getParameter("transactionIds") : "";
		if(StringUtils.isBlank(requestId)) {
			return ErrorCodeEnum.ERR_10503.setErrorCode(new Result());
		}
		try {
			ChequeManagementBusinessDelegate chequeManagementBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ChequeManagementBusinessDelegate.class);
			List<ApprovalRequestDTO> chequeBooks = chequeManagementBusinessDelegate.fetchChequeDetails(request);
			JSONObject chequeBooksDTO = new JSONObject();
			chequeBooksDTO.put("ApprovalRequestDTO", chequeBooks);
			result = JSONToResult.convert(chequeBooksDTO.toString());
		}
		catch(Exception e) {
			alert.prepareError(e.toString()).log();
			return ErrorCodeEnum.ERR_26021.setErrorCode(new Result());
		}
		return result;
	}

}
