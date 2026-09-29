package com.infinity.dbx.temenos.transactions;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.google.gson.Gson;
import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.infinity.dbx.temenos.dto.TransactDate;
import com.infinity.dbx.temenos.transactions.TransactionConstants;
import com.infinity.dbx.temenos.transfers.TransferConstants;
import com.kony.dbx.objects.Account;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

import net.minidev.json.parser.ParseException;

public class GetPastTransactionsPreProcessor extends TemenosBasePreProcessor  {
	
	@SuppressWarnings("unused")
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response, Result result)
			throws Exception {
	try {	
		super.execute(params, request, response, result);
		alert.prepareError("Initial check##").log();
		String accounts = getAccountsInCache( request);
		alert.prepareError("Initial accounts##"+accounts).log();
		String period = null != params.get(TransactionConstants.TRANSACTION_PERIOD)
				? params.get(TransactionConstants.TRANSACTION_PERIOD).toString()
				: "";
		TransactDate transactDate = TransactionUtils.getTransactDate(request);
		alert.prepareError("Initial transactDate:"+transactDate).log();
		Calendar calender = Calendar.getInstance();
		calender.setTime(transactDate.getCurrentWorkingDate());
		if(period != "" && period != null) {
		   calender.add(Calendar.MONTH, -Integer.parseInt(period));
		   calender.add(Calendar.DATE, -1);
		}
		else {
			calender.add(Calendar.MONTH, -6);
			calender.add(Calendar.DATE, -1);
		}
		Date currentWorkingDate = calender.getTime();
		DateFormat dateFormat = new SimpleDateFormat("YYYYMMdd");
		String strCurrentWorkingDate = dateFormat.format(currentWorkingDate);
		alert.prepareError("Initial strCurrentWorkingDate:"+strCurrentWorkingDate).log();
		
		//params.clear();
		params.remove(TransactionConstants.PARAM_USER_ID);
		params.put(TransactionConstants.PARAM_EXECUTION_DATE, strCurrentWorkingDate);
		alert.prepareError("Initial params:"+params).log();
		if(accounts!=null) {
			params.put(TransactionConstants.PARAM_DEBIT_ACCOUNT_ID,accounts);
		}
		else {
			result.addOpstatusParam(0);
			result.addHttpStatusCodeParam(200);
			//result.addErrMsgParam("Missing Accounts");
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
		} catch (Exception e) {
			result.addOpstatusParam(0);
			result.addHttpStatusCodeParam(200);
			e.printStackTrace();
			alert.prepareError("Exception :"+e).log();
		     return Boolean.FALSE;
 	  }
	}
	
	public static String getAccountsInCache(DataControllerRequest request) throws ParseException {
        String accountsInCacheGson = CommonUtils.retreiveFromSession(TransactionConstants.CONSTANT_ACCOUNTS,
                request) != null
                        ? CommonUtils.retreiveFromSession(TransactionConstants.CONSTANT_ACCOUNTS, request).toString()
                        : "";
        alert.prepareError("accountsInCacheGson:"+accountsInCacheGson);
                        
        Gson gson = new Gson();
                		
        Type AccountMapType = new TypeToken<HashMap<String, Account>>() {
        }.getType();
        HashMap<String, Account> accountsInCache = gson.fromJson(accountsInCacheGson, AccountMapType);
        StringBuilder finalAccount = new StringBuilder();
        if (accountsInCache != null) {
        	@SuppressWarnings("rawtypes")
    		Set keys = accountsInCache.keySet();
            @SuppressWarnings("rawtypes")
    		Iterator i = keys.iterator();
            while (i.hasNext()) {
            	finalAccount.append(i.next()+"%20");
            }
        }
        else {
        	finalAccount=null;
        }
        
        return finalAccount.toString();
    }

}
