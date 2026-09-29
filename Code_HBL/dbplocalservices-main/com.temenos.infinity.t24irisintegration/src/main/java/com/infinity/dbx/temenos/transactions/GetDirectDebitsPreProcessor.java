package com.infinity.dbx.temenos.transactions;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.objects.Account;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

import net.minidev.json.parser.ParseException;

public class GetDirectDebitsPreProcessor extends TemenosBasePreProcessor {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean execute(  HashMap params, DataControllerRequest request,
            DataControllerResponse response,
            Result result) throws Exception {

        try {
            diagnostic.prepareDebug("In " + GetDirectDebitsPreProcessor.class.getName()).log();

            super.execute(params, request, response, result);
            String accounts = getAccountsInCache( request);
            if(accounts!=null) {
    			params.put(TransactionConstants.ACCID,accounts);
    		}
    		else {
    			result.addOpstatusParam(0);
    			result.addHttpStatusCodeParam(200);
    			result.addErrMsgParam("Missing Accounts");
    			return Boolean.FALSE;
    		}            

        } catch (Exception e) {
            alert.prepareError("Exception in " + GetDirectDebitsPreProcessor.class.getName(), e).log();
        }

        return Boolean.TRUE;
    }
    
    public static String getAccountsInCache(DataControllerRequest request) throws ParseException {
        String accountsInCacheGson = CommonUtils.retreiveFromSession(TransactionConstants.CONSTANT_ACCOUNTS,
                request) != null
                        ? CommonUtils.retreiveFromSession(TransactionConstants.CONSTANT_ACCOUNTS, request).toString()
                        : "";
                        
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
