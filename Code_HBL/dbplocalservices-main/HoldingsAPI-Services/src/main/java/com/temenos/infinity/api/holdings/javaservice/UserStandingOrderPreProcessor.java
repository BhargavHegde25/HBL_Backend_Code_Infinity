package com.temenos.infinity.api.holdings.javaservice;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.infinity.dbx.temenos.transactions.TransactionConstants;
import com.kony.dbx.objects.Account;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class UserStandingOrderPreProcessor extends TemenosBasePreProcessor {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean execute(  HashMap params, DataControllerRequest request,
            DataControllerResponse response,
            Result result) throws Exception {
		Log4j2Configurator.getInstance();

        try {
            diagnostic.prepareDebug("In " + UserStandingOrderPreProcessor.class.getName()).log();

            super.execute(params, request, response, result);

            TemenosUtils temenosUtils = TemenosUtils.getInstance();
            HashMap<String, Account> accounts = temenosUtils.getAccountsMapFromCache(request);
            Iterator<Entry<String, Account>> hmIterator = accounts.entrySet().iterator(); 
            String spaceSeperatedAccounts = "";
            while (hmIterator.hasNext()) {
            	@SuppressWarnings("rawtypes")
				Map.Entry mapElement = (Map.Entry)hmIterator.next(); 
               spaceSeperatedAccounts = spaceSeperatedAccounts+" "+mapElement.getKey().toString();
            }
            spaceSeperatedAccounts = spaceSeperatedAccounts.trim();
            params.put(TransactionConstants.ACCID,URLEncoder.encode(spaceSeperatedAccounts, "UTF-8"));
            params.remove(TransactionConstants.USERID);
            
            

        } catch (Exception e) {
            alert.prepareError("Exception in " + UserPaymentOrderPreProcessor.class.getName(), e).log();
        }

        return Boolean.TRUE;
    }
}
