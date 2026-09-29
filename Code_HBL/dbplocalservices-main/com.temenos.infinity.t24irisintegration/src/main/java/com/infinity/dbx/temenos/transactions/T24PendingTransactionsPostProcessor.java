package com.infinity.dbx.temenos.transactions;

import static com.infinity.dbx.temenos.transactions.TransactionConstants.ERR_EMPTY_RESPONSE;
import static com.infinity.dbx.temenos.transactions.TransactionConstants.PARAM_VALUE_PENDING;
import static com.infinity.dbx.temenos.transactions.TransactionConstants.TRANSACTION;
import static com.infinity.dbx.temenos.transactions.TransactionConstants.TRANS_TYPE_OTHERS;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.BasePostProcessor;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class T24PendingTransactionsPostProcessor extends BasePostProcessor {

	static final Map<Integer, String> TRANSACTION_TYPE = new HashMap<>();
	static {
		TRANSACTION_TYPE.put(1, "NA");
	}

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		try {
			TemenosUtils temenosUtils = TemenosUtils.getInstance();
			temenosUtils.loadTransactionTypeProperties(request);
			Dataset transactionsDS = result.getDatasetById(TRANSACTION);
			List<Record> transactionRecords = transactionsDS != null ? transactionsDS.getAllRecords() : null;
			if (transactionRecords == null || transactionRecords.isEmpty()) {
				diagnostic.prepareDebug(ERR_EMPTY_RESPONSE).log();
				Result transactionResult = TemenosUtils.getEmptyResult(TRANSACTION);
				if (StringUtils.isNotBlank(result.getParamValueByName(TransactionConstants.PARAM_PAGE_START_PENDING))) {
					transactionResult.addParam(TransactionConstants.PARAM_PAGE_START_PENDING,
							result.getParamValueByName(TransactionConstants.PARAM_PAGE_START_PENDING));
				}
				if (StringUtils.isNotBlank(result.getParamValueByName(TransactionConstants.PARAM_PAGE_SIZE_PENDING))) {
					transactionResult.addParam(TransactionConstants.PARAM_PAGE_SIZE_PENDING,
							result.getParamValueByName(TransactionConstants.PARAM_PAGE_SIZE_PENDING));
				}
				if (StringUtils.isNotBlank(result.getParamValueByName(TransactionConstants.PARAM_TOTAL_SIZE_PENDING))) {
					transactionResult.addParam(TransactionConstants.PARAM_TOTAL_SIZE_PENDING,
							result.getParamValueByName(TransactionConstants.PARAM_TOTAL_SIZE_PENDING));
				}
				return transactionResult;
			} else {
				if (transactionRecords.size() != 0) {
					for (Record record : transactionRecords) {
						record.addParam(PARAM_STATUS_DESCRIPTION, PARAM_VALUE_PENDING);
						String transactionType = record.getParamValueByName(PARAM_TRANSACTION_TYPE);
						if (transactionType != StringUtils.EMPTY) {
							transactionType = temenosUtils.transactionTypesMap.get(transactionType);
							if (transactionType == null) {
								transactionType = TRANS_TYPE_OTHERS;
							}
						}
						record.addParam(PARAM_TRANSACTION_TYPE, transactionType);
						
                    	String narrative = record.getParamValueByName("notes");
                    	Param descriptionParam = record.getParam("description");
                    	diagnostic.prepareDebug("***********narrative:"+narrative).log();
                    	if(narrative!=null && !"".equals(narrative)) {
                    		String displayName = descriptionParam.getValue();
                    		if(displayName==null) displayName="";
                    		descriptionParam.setValue(displayName+" "+narrative);
                    		diagnostic.prepareDebug("***********descriptionParam:"+descriptionParam.getValue()).log();
                    	}
                    	record.addParam(descriptionParam);
						
					}
				}
			}
		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
			CommonUtils.setErrMsg(result, e.toString());
		}
		return result;
	}
}
