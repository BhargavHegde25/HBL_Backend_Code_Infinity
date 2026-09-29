package com.temenos.infinity.api.arrangements.preprocessors;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.infinity.dbx.temenos.dto.TransactDate;
import com.infinity.dbx.temenos.transactions.TransactionConstants;
import com.infinity.dbx.temenos.transactions.TransactionUtils;
import com.temenos.infinity.api.arrangements.constants.UserAccountSettingConstants;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbputilities.util.TransactionsCountProperties;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class GetBlockedFundsPreProcessor extends TemenosBasePreProcessor {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final DateFormat DATE_FORMATTER = new SimpleDateFormat(
			TransactionConstants.TRANSACTION_DATE_FORMAT);
    @SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
    public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
            Result result) throws Exception {
		Log4j2Configurator.getInstance();

        try {
            String accountId = params.get(UserAccountSettingConstants.ACCOUNTID) != null
                    ? params.get(UserAccountSettingConstants.ACCOUNTID).toString() : "";
            if (StringUtils.isBlank(accountId)) {
                result.addOpstatusParam(0);
                result.addHttpStatusCodeParam(200);
                result.addErrMsgParam("Misssing input param " + UserAccountSettingConstants.ACCOUNTID);
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
            String filterValue = UserAccountSettingConstants.ACCOUNT_ID + "=" + accountId;
            String lockReason = params.get(UserAccountSettingConstants.BLOCKED_FUNDS_LOCK_REASON) != null
                    ? params.get(UserAccountSettingConstants.BLOCKED_FUNDS_LOCK_REASON).toString() : "";
            String lockedEventId = params.get(UserAccountSettingConstants.BLOCKED_FUNDS_LOCKED_EVENT_ID) != null
                    ? params.get(UserAccountSettingConstants.BLOCKED_FUNDS_LOCKED_EVENT_ID).toString() : "";

            String searchStartDate="";
            String searchEndDate="";
            String searchDateRange = params.get("searchDateRange")!=null? params.get("searchDateRange").toString():"";
            if(searchDateRange!=null && !"".equals(searchDateRange) && !"CustomDateRange".equalsIgnoreCase(searchDateRange)) {
                TransactDate transactDate = TransactionUtils.getTransactDate(request);
    			Calendar calender = Calendar.getInstance();
    			if(transactDate.getCurrentWorkingDate()!=null && !"".equals(transactDate.getCurrentWorkingDate())) {
    				calender.setTime(transactDate.getCurrentWorkingDate());
    			}else {
    				calender.setTime(new Date());
    			}
    			
    			Date currentWorkingDate = calender.getTime();
    			searchEndDate = DATE_FORMATTER.format(currentWorkingDate);
     			Calendar startDateCal = Calendar.getInstance();
    			startDateCal.setTime(transactDate.getCurrentWorkingDate());
    			startDateCal.add(Calendar.DAY_OF_MONTH, getSubtractDays(searchDateRange));
    			Date startDate = startDateCal.getTime();
    			searchStartDate = DATE_FORMATTER.format(startDate);
    			
    		//	params.put("endDate", todayDate);
            }else {
                 searchStartDate = params.get(UserAccountSettingConstants.SEARCH_START_DATE_PARAM) != null
                        ? params.get(UserAccountSettingConstants.SEARCH_START_DATE_PARAM).toString() : "";
                 searchEndDate = params.get(UserAccountSettingConstants.SEARCH_END_DATE_PARAM) != null
                        ? params.get(UserAccountSettingConstants.SEARCH_END_DATE_PARAM).toString() : "";
            }

            

            if (StringUtils.isNotBlank(lockReason)) {
                if (Pattern.matches("^[a-zA-Z0-9_\\s]+$", lockReason)) {
                    filterValue += "&" + UserAccountSettingConstants.BLOCKED_FUNDS_LOCK_REASON + "=" + lockReason;
                } else {
                    return Boolean.FALSE;
                } 
            }
            if (StringUtils.isNotBlank(lockedEventId)) {
                if (Pattern.matches("^[a-zA-Z0-9]+$", lockedEventId)) {
                    filterValue += "&" + UserAccountSettingConstants.BLOCKED_FUNDS_EVENT_ID + "=" + lockedEventId;
                } else {
                    return Boolean.FALSE;
                }
            }
            if (StringUtils.isNotBlank(searchStartDate) && StringUtils.isNotBlank(searchEndDate)) {
                filterValue += "&" + UserAccountSettingConstants.BLOCKED_FUNDS_DATE_RANGE + "=" + searchStartDate.replace("-", "") + " "
                        + searchEndDate.replace("-", "");
            }
            
            //Added for for  pagination during blocked funds search
            String pageStart = params.get("pageStart")!=null?params.get("pageStart").toString():"";
            if(pageStart.equals("")) {
            	pageStart = "1";
            }
            filterValue += "&page_start="+pageStart;
            
	        String limit = "";
	        String requestType = params.get("requestType")!=null?params.get("requestType").toString():"";
	        if(requestType != null && requestType.equals("search")) {
	        	int perPageTransactionsLimit = 10;
	        	 try {
					TransactionsCountProperties transactionsCountProperties = new TransactionsCountProperties(request,"perPage"); 
					perPageTransactionsLimit =Integer.parseInt(TransactionsCountProperties.getValue("config_value"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					alert.prepareError(e.getMessage()).log();
				}
	   		  limit = Integer.toString(perPageTransactionsLimit);
	   		filterValue += "&page_size="+limit;
	        }
	        
            //End of changes for pagination
	        
            params.put(UserAccountSettingConstants.PARAM_FILTER, filterValue);
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
    	case "7days": days=-7; break;
    	case "14days": days=-14; break;
    	case "1month": days=-30; break;
    	case "2month": days=-60; break;
    	case "3month": days=-90; break;
    	case "6month": days=-180; break;
    	case "12month": days=-360; break;
     	}
    	return days;
    }
}
