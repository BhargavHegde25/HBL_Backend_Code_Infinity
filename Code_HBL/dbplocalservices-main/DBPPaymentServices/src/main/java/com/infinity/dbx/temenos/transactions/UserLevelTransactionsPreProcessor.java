package com.infinity.dbx.temenos.transactions;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import static com.infinity.dbx.temenos.transactions.TransactionConstants.*;
import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.objects.Account;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.net.URLEncoder;
public class UserLevelTransactionsPreProcessor extends TemenosBasePreProcessor {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {

		super.execute(params, request, response, result);
		String firstRecordNumber = CommonUtils.getParamValue(params, PARAM_FIRST_RECORD_NUMBER);
		String lastRecordNumber = CommonUtils.getParamValue(params, PARAM_LAST_RECORD_NUMBER);
		if (firstRecordNumber == StringUtils.EMPTY || lastRecordNumber == StringUtils.EMPTY) {
			diagnostic.prepareDebug(ERR_EMPTY_REQUEST).log();
			params.put(PARAM_PAGE_START,PARAM_PAGE_START_DEF_VALUE);
			params.put(PARAM_TRANSACTION_COUNT,PARAM_TRANSACTION_COUNT_DEF_VALUE);
		} else {

			int page_start = (Integer.parseInt(firstRecordNumber) / Integer.parseInt(lastRecordNumber)) + 1;
			params.put(PARAM_PAGE_START, String.valueOf(page_start));
			params.put(PARAM_TRANSACTION_COUNT,String.valueOf(page_start*Integer.parseInt(lastRecordNumber)));
		}

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
        
		return Boolean.TRUE;
	}
}