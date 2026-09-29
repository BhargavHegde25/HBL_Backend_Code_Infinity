package com.infinity.dbx.temenos.transactions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.infinity.dbx.temenos.dto.TransactDate;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.constants.UserAccountSettingConstants;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class GetBlockedFundsPreProcessor extends TemenosBasePreProcessor {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final DateFormat DATE_FORMATTER = new SimpleDateFormat(
			TransactionConstants.TRANSACTIONS_DATE_BACKEND_FORMAT);
    @SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
    public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
            Result result) throws Exception {

        try {
            String accountId = params.get(TransactionConstants.ACCOUNTID) != null
                    ? params.get(TransactionConstants.ACCOUNTID).toString() : "";
            if (StringUtils.isBlank(accountId)) {
                result.addOpstatusParam(0);
                result.addHttpStatusCodeParam(200);
                result.addErrMsgParam("Misssing input param " + TransactionConstants.ACCOUNTID);
                return Boolean.FALSE;
            }
            super.execute(params, request, response, result);
            TemenosUtils temenosUtils = TemenosUtils.getInstance();
            boolean accAvailable = temenosUtils.checkCustomerAccount(request, accountId);
            if (!accAvailable) {
                result.addOpstatusParam(0);
                result.addHttpStatusCodeParam(200);
                result.addErrMsgParam("Account " + accountId + " is not related to customer");
                return Boolean.FALSE;
            }

            // Construct the filter variable(Iris query Parameters)
            String filterValue = TransactionConstants.ACCOUNT_ID + "=" + accountId;
            String lockReason = params.get(TransactionConstants.BLOCKED_FUNDS_LOCK_REASON) != null
                    ? params.get(TransactionConstants.BLOCKED_FUNDS_LOCK_REASON).toString() : "";
            String lockedEventId = params.get(TransactionConstants.BLOCKED_FUNDS_LOCKED_EVENT_ID) != null
                    ? params.get(TransactionConstants.BLOCKED_FUNDS_LOCKED_EVENT_ID).toString() : "";
            
            String searchStartDate="";
            String searchEndDate="";
            String searchDateRange = params.get("searchDateRange")!=null? params.get("searchDateRange").toString():"";
            if(searchDateRange!=null && !"CustomDateRange".equalsIgnoreCase(searchDateRange)) {
                TransactDate transactDate = TransactionUtils.getTransactDate(request);
    			Calendar calender = Calendar.getInstance();
    			calender.setTime(transactDate.getCurrentWorkingDate());
    			Date currentWorkingDate = calender.getTime();
    			searchEndDate = DATE_FORMATTER.format(currentWorkingDate);
     			Calendar startDateCal = Calendar.getInstance();
    			startDateCal.setTime(transactDate.getCurrentWorkingDate());
    			startDateCal.add(Calendar.DAY_OF_MONTH, -getSubtractDays(searchDateRange));
    			Date endDate = startDateCal.getTime();
    			searchStartDate = DATE_FORMATTER.format(currentWorkingDate);
    			
    		//	params.put("endDate", todayDate);
            }else {
                 searchStartDate = params.get(UserAccountSettingConstants.SEARCH_START_DATE_PARAM) != null
                        ? params.get(UserAccountSettingConstants.SEARCH_START_DATE_PARAM).toString() : "";
                 searchEndDate = params.get(UserAccountSettingConstants.SEARCH_END_DATE_PARAM) != null
                        ? params.get(UserAccountSettingConstants.SEARCH_END_DATE_PARAM).toString() : "";
            }
			/*
			 * String searchStartDate =
			 * params.get(TransactionConstants.SEARCH_START_DATE_PARAM) != null ?
			 * params.get(TransactionConstants.SEARCH_START_DATE_PARAM).toString() : "";
			 * String searchEndDate = params.get(TransactionConstants.SEARCH_END_DATE_PARAM)
			 * != null ? params.get(TransactionConstants.SEARCH_END_DATE_PARAM).toString() :
			 * "";
			 */

            if (StringUtils.isNotBlank(lockReason)) {
                if (Pattern.matches("^[a-zA-Z0-9_\\s]+$", lockReason)) {
                    filterValue += "&" + TransactionConstants.BLOCKED_FUNDS_LOCK_REASON + "=" + lockReason;
                } else {
                    return Boolean.FALSE;
                } 
            }
            if (StringUtils.isNotBlank(lockedEventId)) {
                if (Pattern.matches("^[a-zA-Z0-9]+$", lockedEventId)) {
                    filterValue += "&" + TransactionConstants.BLOCKED_FUNDS_EVENT_ID + "=" + lockedEventId;
                } else {
                    return Boolean.FALSE;
                }
            }
            if (StringUtils.isNotBlank(searchStartDate) && StringUtils.isNotBlank(searchEndDate)) {
                filterValue += "&" + TransactionConstants.BLOCKED_FUNDS_DATE_RANGE + "=" + searchStartDate.replace("-", "") + " "
                        + searchEndDate.replace("-", "");
            }
            params.put(TransactionConstants.PARAM_FILTER, filterValue);
            return Boolean.TRUE;
        } catch (Exception e) {
            alert.prepareError("Exception occured in GetBlockedFunds PreProcessor" + e).log();
            return Boolean.FALSE;
        }
    }
    private int getSubtractDays(String customDateRange) {
    	int days = 0;
    	
    	switch(customDateRange) 
    	{
    	case "7days": days=7; break;
    	case "14days": days=14; break;
    	case "1month": days=30; break;
    	case "2month": days=60; break;
    	case "3month": days=90; break;
    	case "6month": days=180; break;
    	case "12month": days=360; break;
     	}
    	return days;
    }
}
