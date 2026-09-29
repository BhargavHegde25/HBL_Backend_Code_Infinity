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
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.infinity.dbx.temenos.dto.TransactDate;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.infinity.dbx.temenos.transactions.TransactionConstants;
import com.infinity.dbx.temenos.transactions.TransactionUtils;
import com.kony.dbx.objects.Account;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * <p>
 * Pre-Processor class for User Payment Orders
 * </p>
 * 
 * @author Aditya Mankal
 *
 */
public class UserPaymentOrderPreProcessor extends TemenosBasePreProcessor {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    private static final DateFormat DATE_FORMATTER =
            new SimpleDateFormat(TransactionConstants.TRANSACTIONS_DATE_BACKEND_FORMAT);

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public boolean execute(HashMap params, DataControllerRequest request,
            DataControllerResponse response,
            Result result) throws Exception {
		Log4j2Configurator.getInstance();

        try {
            diagnostic.prepareDebug("In " + UserPaymentOrderPreProcessor.class.getName()).log();

            super.execute(params, request, response, result);

            TemenosUtils temenosUtils = TemenosUtils.getInstance();
            HashMap<String, Account> accounts = temenosUtils.getAccountsMapFromCache(request);
            Iterator<Entry<String, Account>> hmIterator = accounts.entrySet().iterator(); 
            String spaceSeperatedAccounts = "";
            while (hmIterator.hasNext()) {
            	Map.Entry mapElement = (Map.Entry)hmIterator.next(); 
               spaceSeperatedAccounts = spaceSeperatedAccounts+" "+mapElement.getKey().toString();
            }
            spaceSeperatedAccounts = spaceSeperatedAccounts.trim();
            params.put(TransactionConstants.DEBITACCID,URLEncoder.encode(spaceSeperatedAccounts,"UTF-8"));
            params.remove(TransactionConstants.USERID);
            
            
            // Get Transact Date
         	TransactDate transactDate = TransactionUtils.getTransactDate(request);

         	
         	Calendar calender = Calendar.getInstance();
         	calender.setTime(transactDate.getCurrentWorkingDate());
         	Date todaysDate = calender.getTime();

            String todaysDateString = DATE_FORMATTER.format(todaysDate);

            // Compute Future Date
            calender.add(Calendar.DAY_OF_MONTH, TransactionConstants.SCHEDULED_TRANSACTIONS_FETCH_WINDOW);
            Date futureDate = calender.getTime();
            String futureDateString = DATE_FORMATTER.format(futureDate);

            params.put(TransactionConstants.SEARCH_START_DATE_PARAM,todaysDateString);
            params.put(TransactionConstants.SEARCH_END_DATE_PARAM,futureDateString);

        } catch (Exception e) {
            alert.prepareError("Exception in " + UserPaymentOrderPreProcessor.class.getName(), e).log();
        }

        return Boolean.TRUE;
    }
}
