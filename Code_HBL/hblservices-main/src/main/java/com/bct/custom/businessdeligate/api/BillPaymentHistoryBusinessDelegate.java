package com.bct.custom.businessdeligate.api;

import java.util.Map;

import org.json.JSONArray;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.transaction.dto.TransactionDTO;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public interface BillPaymentHistoryBusinessDelegate extends BusinessDelegate {

	public JSONArray getBillPaymentHistory(String biilerId, Map<String, Object> inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException;

	public TransactionDTO getBillTransactionById(Map<String, Object> inputParams, DataControllerRequest request)throws ApplicationException;;
}
