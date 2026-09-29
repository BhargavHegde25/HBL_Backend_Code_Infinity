package com.infinity.dbx.temenos.transactions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.infinity.dbx.temenos.dto.TransactDate;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.objects.Account;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class LoanScheduleTransactionPreProcessor extends TemenosBasePreProcessor {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	private static final DateFormat DATE_FORMATTER = new SimpleDateFormat(
			TransactionConstants.TRANSACTIONS_DATE_BACKEND_FORMAT);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			super.execute(params, request, response, result);

			String offset = CommonUtils.getParamValue(params, TransactionConstants.PARAM_OFFSET);
			String limit = CommonUtils.getParamValue(params, TransactionConstants.PARAM_LIMIT);
			if (StringUtils.isNotBlank(offset) && StringUtils.isNotBlank(limit)) {
				int page_start = (Integer.parseInt(offset) / Integer.parseInt(limit)) + 1;
				params.put(TransactionConstants.PARAM_PAGE_START, String.valueOf(page_start));
			}
			String accountId = CommonUtils.getParamValue(params, "accountID");
			TemenosUtils temenosUtils = TemenosUtils.getInstance();
			HashMap<String, Account> accounts = temenosUtils.getAccountsMapFromCache(request);
			Account account = accounts != null ? accounts.get(accountId) : null;

			String arrangementID = account.getArrangementId();
			params.put(TransactionConstants.PARAM_ARRANGEMENTID, arrangementID);

			// TODO need to get Transact date
			try {
				String isFutureRequired = request.getParameter("isFutureRequired");
				if (StringUtils.isNotBlank(isFutureRequired)) {
					if (isFutureRequired.equalsIgnoreCase("false")) {
						TransactDate transactDate = TransactionUtils.getTransactDate(request);
						Calendar calender = Calendar.getInstance();
						calender.setTime(transactDate.getCurrentWorkingDate());
						Date currentWorkingDate = calender.getTime();
						String todayDate = DATE_FORMATTER.format(currentWorkingDate);
						params.put("endDate", todayDate);
					} else {
						params.put(TransactionConstants.PARAM_PAGE_START,TransactionConstants.FUTURE_START);
						params.put(TransactionConstants.PARAM_LIMIT,TransactionConstants.FUTURE_LIMIT);
						params.put("endDate", "");
					}
				}
				else {
					params.put(TransactionConstants.PARAM_PAGE_START,TransactionConstants.FUTURE_START);
					params.put(TransactionConstants.PARAM_LIMIT,TransactionConstants.FUTURE_LIMIT);
					params.put("endDate", "");
				}
			} catch (Exception ex) {
				params.put("endDate", "");
			}
		} catch (Exception e) {
			alert.prepareError("Exception in " + LoanScheduleTransactionPreProcessor.class.getName(), e).log();
		}
		return Boolean.TRUE;
	}
}
