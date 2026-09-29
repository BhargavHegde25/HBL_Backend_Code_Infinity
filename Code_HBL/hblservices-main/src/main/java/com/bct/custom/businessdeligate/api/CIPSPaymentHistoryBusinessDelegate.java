package com.bct.custom.businessdeligate.api;

import java.util.Map;

import org.json.JSONArray;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.temenos.dbx.transaction.dto.TransactionDTO;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public interface CIPSPaymentHistoryBusinessDelegate extends BusinessDelegate {
	public JSONArray getCIPSPaymentHistory(String biilerId, Map<String, Object> inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException;
	public TransactionDTO getCIPSTransactionById(Map<String, Object> inputParams, DataControllerRequest request)throws ApplicationException;;
}
