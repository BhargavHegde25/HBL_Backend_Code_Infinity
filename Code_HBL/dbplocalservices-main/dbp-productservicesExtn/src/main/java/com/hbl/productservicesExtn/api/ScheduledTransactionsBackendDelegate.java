package com.hbl.productservicesExtn.api;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import com.dbp.core.api.BackendDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.transactionservices.dto.InterBankFundTransferDTO;

public interface ScheduledTransactionsBackendDelegate extends BackendDelegate{
	public List<InterBankFundTransferDTO> GetScheduledTransactions(Map<String, Object> inputMap, DataControllerRequest request)throws ApplicationException;
}
