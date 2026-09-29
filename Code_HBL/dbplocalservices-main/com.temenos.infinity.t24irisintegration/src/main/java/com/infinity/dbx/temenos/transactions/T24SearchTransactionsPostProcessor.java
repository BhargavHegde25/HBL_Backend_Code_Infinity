package com.infinity.dbx.temenos.transactions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.infinity.dbx.temenos.TemenosBasePostProcessor;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class T24SearchTransactionsPostProcessor extends TemenosBasePostProcessor {
    @Override
    public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {
	Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

        try {
            TemenosUtils temenosUtils = TemenosUtils.getInstance();
            temenosUtils.loadTransactionTypeProperties(request);
            Dataset transactionsDS = result.getDatasetById(TransactionConstants.TRANSACTION);
            List<Record> transactionRecords = transactionsDS != null ? transactionsDS.getAllRecords() : null;
            if (transactionRecords == null || transactionRecords.isEmpty()) {
                diagnostic.prepareDebug(TransactionConstants.ERR_EMPTY_RESPONSE).log();
                return TemenosUtils.getEmptyResult(TransactionConstants.TRANSACTION);
            } else {
            	
    			
                if (transactionRecords.size() != 0) {
                    for (Record record : transactionRecords) {
                       /* if ((record.getParamValueByName(PARAM_TRANSACTION_ID)==null) || record.getParamValueByName(PARAM_TRANSACTION_ID).contains("DUMMY")){
                            return TemenosUtils.getEmptyResult(TransactionConstants.TRANSACTION);
                        }*/
                    	
                    	String displayName = record.getParamValueByName("displayName");
                    	String narrative = record.getParamValueByName("narrative");
                    	String description = "";
                    	if(displayName!=null && !"".equals(displayName)) {
                    		description = displayName;
                    	}
                    	if(narrative!=null && !"".equals(narrative)) {
                    		description = description+" "+narrative;
                    	}
                    	description = description.trim();
                    	record.addParam("description", description);
                    	
                    	alert.prepareError("**********SearchTransactionsPostProcessor:displayName:"+displayName+",narrative:"+narrative+",description:"+description).log();
                        record.addParam(PARAM_STATUS_DESCRIPTION, TransactionConstants.PARAM_VALUE_SUCCESSFUL);
                        String transactionType = record.getParamValueByName(PARAM_TRANSACTION_TYPE);
                        if (transactionType != StringUtils.EMPTY) {
                            transactionType = temenosUtils.transactionTypesMap.get(transactionType);
                            if (transactionType == null) {
                                transactionType = TransactionConstants.TRANS_TYPE_OTHERS;
                            }
                        }
                        record.addParam(PARAM_TRANSACTION_TYPE, transactionType);
                    }
                    /*** Sorting the results to display latest ones first *** Start ***/
                    
    			    List<Record> sortableList = new ArrayList<>(transactionRecords);
    			    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    			    sortableList.sort((r1, r2) -> {
    			        String d1 = r1.getParamValueByName("transactionDate");
    			        String d2 = r2.getParamValueByName("transactionDate");

    			        if (d1 == null) return 1;
    			        if (d2 == null) return -1;

    			        LocalDate date1 = LocalDate.parse(d1, formatter);
    			        LocalDate date2 = LocalDate.parse(d2, formatter);

    			        // Descending (newest → oldest)
    			        return date2.compareTo(date1);
    			    });
    			    Dataset sortedDataset = new Dataset();
    			    sortedDataset.setId(TransactionConstants.TRANSACTION);

    			    // 2. Add all sorted records to the new dataset
    			    for (Record rec : sortableList) {
    			        sortedDataset.addRecord(rec);
    			    }
    			    result.removeDatasetById(TransactionConstants.TRANSACTION);
    			    result.addDataset(sortedDataset);
    			    
    			    /** End ***/
                }

            }

        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            CommonUtils.setErrMsg(result, e.toString());
        }
        return result;
    }
}
