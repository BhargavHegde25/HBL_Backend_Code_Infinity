package com.infinity.dbx.temenos.paylater;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.objects.Account;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetTransactionsPreProcessor extends TemenosBasePreProcessor {
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		TemenosUtils temenosUtils = TemenosUtils.getInstance();

		HashMap<String, Account> accounts = temenosUtils.getAccountsMapFromCache(request);
		String accountId = CommonUtils.getParamValue(params, PayLaterConstants.PARAM_ACCOUNT_ID);
		if (accounts != null) {
			Account account = accounts.get(accountId);
			String accountType = account.getAccountType();
			if (accountType.equalsIgnoreCase(PayLaterConstants.PARAM_SPROUT)) {
				/*
				 * Date today = new Date(); Calendar cal = new GregorianCalendar();
				 * cal.setTime(today); cal.add(Calendar.DAY_OF_MONTH, -30); Date today30 =
				 * cal.getTime(); SimpleDateFormat simpleDateFormat = new
				 * SimpleDateFormat(PayLaterConstants.T24_DATE_FORMATE); String startDate =
				 * simpleDateFormat.format(today30); params.put(PayLaterConstants.DATE_FROM,
				 * startDate);
				 */

				return Boolean.TRUE;
			}
		}
		result.addOpstatusParam(0);
		return Boolean.FALSE;
	}
}
