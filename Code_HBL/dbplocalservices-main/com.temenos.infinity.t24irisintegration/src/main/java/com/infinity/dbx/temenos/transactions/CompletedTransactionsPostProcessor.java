package com.infinity.dbx.temenos.transactions;

import static com.infinity.dbx.temenos.transactions.TransactionConstants.*;

import java.util.List;

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

public class CompletedTransactionsPostProcessor extends BasePostProcessor {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    @Override
    public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {
        try {
        	
        	diagnostic.prepareDebug("START CompletedTransactionsPostProcessor").log();
            TemenosUtils temenosUtils = TemenosUtils.getInstance();
            temenosUtils.loadTransactionTypeProperties(request);
            Dataset transactionsDS = result.getDatasetById(TRANSACTION);
            List<Record> transactionRecords = transactionsDS != null ? transactionsDS.getAllRecords() : null;
            if (transactionRecords == null || transactionRecords.isEmpty()) {
                diagnostic.prepareDebug(ERR_EMPTY_RESPONSE).log();
                Result transactionResult = TemenosUtils.getEmptyResult(TRANSACTION);
                if(StringUtils.isNotBlank(result.getParamValueByName(TransactionConstants.PARAM_PAGE_START_COMPLETED))) {
                	transactionResult.addParam(TransactionConstants.PARAM_PAGE_START_COMPLETED, result.getParamValueByName(TransactionConstants.PARAM_PAGE_START_COMPLETED));
                }
                if(StringUtils.isNotBlank(result.getParamValueByName(TransactionConstants.PARAM_PAGE_SIZE_COMPLETED))) {
                	transactionResult.addParam(TransactionConstants.PARAM_PAGE_SIZE_COMPLETED, result.getParamValueByName(TransactionConstants.PARAM_PAGE_SIZE_COMPLETED));
                }
                if(StringUtils.isNotBlank(result.getParamValueByName(TransactionConstants.PARAM_TOTAL_SIZE_COMPLETED))) {
                	transactionResult.addParam(TransactionConstants.PARAM_TOTAL_SIZE_COMPLETED, result.getParamValueByName(TransactionConstants.PARAM_TOTAL_SIZE_COMPLETED));
                }
                return transactionResult;
            } else {
                if (transactionRecords.size() != 0) {
                    for (Record record : transactionRecords) {
                        record.addParam(PARAM_STATUS_DESCRIPTION, PARAM_VALUE_SUCCESSFUL);
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
