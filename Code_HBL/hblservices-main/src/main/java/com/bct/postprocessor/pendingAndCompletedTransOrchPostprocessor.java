package com.bct.postprocessor;

import static com.infinity.dbx.temenos.transactions.TransactionConstants.TRANSACTION;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.BasePostProcessor;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class pendingAndCompletedTransOrchPostprocessor extends BasePostProcessor{
	Logger logger = LogManager.getLogger(pendingAndCompletedTransOrchPostprocessor.class);
    @Override
    public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {
        try {
        	
        	TemenosUtils temenosUtils = TemenosUtils.getInstance();
            temenosUtils.loadTransactionTypeProperties(request);
            Dataset transactionsDS = result.getDatasetById(TRANSACTION);
            List<Record> transactionRecords = transactionsDS != null ? transactionsDS.getAllRecords() : null;
            
            if (transactionRecords.size() != 0) {
                for (Record record : transactionRecords) {
                	
                	String toAccountName = record.getParamValueByName("toAccountName");
                	String toAccountNumber = record.getParamValueByName("toAccountNumber");
                	
                	if(StringUtils.isNotBlank(toAccountName) && StringUtils.isNotBlank(toAccountNumber)) {
                		if(toAccountNumber.length() > 5) {
                		record.addParam("transactionComments", toAccountName +"..."+toAccountNumber.substring(toAccountNumber.length() - 4));
                		//record.addParam("transactionComments", toAccountName);
                		}
                	}
                	else {
                		record.addParam("transactionComments", "...");
                	}
    			}  
             }  
            
            
            
				/*
				 * HashMap<String, Account> accounts =
				 * temenosUtils.getAccountsMapFromCache(request); if (accounts != null) {
				 * Iterator <String> it = accounts.keySet().iterator();
				 * 
				 * while (it.hasNext()) { String key = it.next(); Account obj =
				 * accounts.get(key); logger.debug("Account Object:"+obj.getAccountId()); }
				 * logger.debug("Accounts ###"+ accounts.toString()); } else {
				 * logger.debug("Accounts in else ###"); }
				 */
        } catch (Exception e) {
            logger.error(e);
            CommonUtils.setErrMsg(result, e.toString());
        }
        return result;
    }


}
