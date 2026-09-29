package com.infinity.dbx.temenos.transactions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.constants.TransactionType;
import com.infinity.dbx.temenos.utils.ServerConfigurations;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbputilities.util.TransactionsCountProperties;
import com.kony.dbx.objects.Account;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.api.OperationData;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class AccountlevelTransactionsPreProcessor extends TemenosBasePreProcessor {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    @SuppressWarnings("unchecked")
    public boolean execute(@SuppressWarnings("rawtypes") HashMap params, DataControllerRequest request,
            DataControllerResponse response, Result result) throws Exception {

        try {
            super.execute(params, request, response, result);
            diagnostic.prepareDebug("In AccountlevelTransactionsPreProcessor").log();
            String operationID = request.getServicesManager().getOperationData().getOperationId();
            if(operationID.equalsIgnoreCase("getPendingTransactions")) {
            	diagnostic.prepareDebug("findDatePriorToNintyDays:"+ findDatePriorToNintyDays());
            	params.put(Constants.PARAM_SEARCH_START_DATE, findDatePriorToNintyDays());
            }else if(operationID.equalsIgnoreCase("getCompletedTransactions")) {
            	params.put(Constants.PARAM_SEARCH_MINIMUM_AMOUNT, "1");
            }
            // Check Admin Permission when the request comes from micro services
            if (StringUtils.isNotBlank(request.getHeader(TransactionConstants.PARAM_TRANSACTION_PERMISSION))
                    && (request.getHeader(TransactionConstants.PARAM_TRANSACTION_PERMISSION)
                            .equals(TransactionConstants.PARAM_ADMIN))) {
                if (StringUtils.isBlank(CommonUtils.getParamValue(params, TransactionConstants.ACCOUNTID))) {
                    params.put(TransactionConstants.ACCOUNTID,
                            CommonUtils.getParamValue(params, TransactionConstants.ACCOUNT_NUMBER));
                }
            } else {
                TemenosUtils temenosUtils = TemenosUtils.getInstance();
                HashMap<String, Account> accounts = temenosUtils.getAccountsMapFromCache(request);
                if (accounts != null) {
                    if (StringUtils.isBlank(CommonUtils.getParamValue(params, TransactionConstants.ACCOUNTID))) {
                        params.put(TransactionConstants.ACCOUNTID,
                                CommonUtils.getParamValue(params, TransactionConstants.ACCOUNT_NUMBER));
                    }

                    String accountId = CommonUtils.getParamValue(params, TransactionConstants.ACCOUNTID);
                    String[] accountIds = null;
                    if (accountId.contains("-")) {
                        accountIds = accountId.split("-");
                        if (accountIds != null && accountIds.length > 0) {
                            accountId = accountIds[1];
                        }
                    }

                    Account account = accounts.get(accountId);
                    String accountType = account.getAccountType();
                    if (TemenosConstants.ACCOUNT_TYPE_SPROUT.equalsIgnoreCase(accountType)) {
                        result.addOpstatusParam(0);
                        return Boolean.FALSE;
                    }
                } else {
                    // Return false when user not logged in properly
                    result.addOpstatusParam(0);
                    return Boolean.FALSE;
                }
            }

			/*
			 * String fromDate = request.getParameter(Constants.PARAM_SEARCH_START_DATE); if
			 * (StringUtils.isBlank(fromDate)) { String noOfDays =
			 * CommonUtils.getProperty(TemenosConstants.TEMENOS_PROPERTIES_FILE,
			 * TemenosConstants.PROP_PREFIX_TEMENOS,
			 * TransactionConstants.PROP_SECTION_TRANSACTIONS,
			 * TransactionConstants.PROP_NUMBER_OF_DAYS); fromDate =
			 * TransactionUtils.getMinusDays(noOfDays); }
			 * params.put(Constants.PARAM_SEARCH_START_DATE,
			 * CommonUtils.convertDateToYYYYMMDD(fromDate));
			 */
            String offset = CommonUtils.getParamValue(params, TransactionConstants.PARAM_OFFSET);
            // String limit = CommonUtils.getParamValue(params, TransactionConstants.PARAM_LIMIT);
             
             String limit = "";
             
             ServicesManager servicesManager = request.getServicesManager();
             OperationData op = servicesManager.getOperationData();
             String operationId = op.getOperationId();
             String transactionCodesParam;
             String transactionTypeParam = CommonUtils.getParamValue(params, Constants.PARAM_TRANSACTION_TYPE);           
             
             
              if(operationId.equals("getCompletedTransactions")) {
            	  if(StringUtils.equalsIgnoreCase(transactionTypeParam, "Pending")) {
            		  //Requested is for Pending Transactions download, so do not execute pending service
                      result.addOpstatusParam(0);
                      return Boolean.FALSE;
            	  }
              	 int TRANSACTIONS_LIMIT = 10;
             	  try {
					TransactionsCountProperties transactionsCountProperties = new
							  TransactionsCountProperties(request, "Posted");
					 TRANSACTIONS_LIMIT = Integer.parseInt(TransactionsCountProperties.getValue("config_value"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					alert.prepareError(e.getMessage()).log();
				}
             	 limit = Integer.toString(TRANSACTIONS_LIMIT);
             	 params.put("limit", String.valueOf(limit));
              }
              else if(operationId.equals("getPendingTransactions")) {
            	  if(StringUtils.equalsIgnoreCase(transactionTypeParam, "Posted")) {
            		  //Requested is for Posted Transactions download, so do not execute pending service
                      result.addOpstatusParam(0);
                      return Boolean.FALSE;
            	  }
            	  int TRANSACTIONS_LIMIT = 10;
             	 try {
					TransactionsCountProperties transactionsCountProperties = new
							  TransactionsCountProperties(request, "Pending");
							   TRANSACTIONS_LIMIT =Integer.parseInt(TransactionsCountProperties.getValue("config_value"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					alert.prepareError(e.getMessage()).log();
				}
          			  limit = Integer.toString(TRANSACTIONS_LIMIT);
          			 params.put("limit", String.valueOf(limit));
          		}
              
             
             if (StringUtils.isNotBlank(offset) && StringUtils.isNotBlank(limit)) {
                 int page_start = (Integer.parseInt(offset) / Integer.parseInt(limit)) + 1;
                 params.put(TransactionConstants.PARAM_PAGE_START, String.valueOf(page_start));
             }
            alert.prepareError("Params obtained:" + params).log();
            // TransactionUtils.getT24TransactionType(params, request);

            if (StringUtils.isBlank(transactionTypeParam)
                    || StringUtils.equalsIgnoreCase(transactionTypeParam, TransactionConstants.PARAM_VALUE_ALL)
                    || StringUtils.equalsIgnoreCase(transactionTypeParam, TransactionConstants.PARAM_VALUE_BOTH)
                    || StringUtils.equalsIgnoreCase(transactionTypeParam, "Posted")
                    || StringUtils.equalsIgnoreCase(transactionTypeParam, "Pending")) {
                transactionCodesParam = StringUtils.EMPTY;
            } else {
                transactionTypeParam = TransactionUtils.getTransactionType(transactionTypeParam);
                TransactionType transactionType = TransactionType.getTransactionType(transactionTypeParam);
                List<Integer> transactionCodes = new ArrayList<>(
                        TransactionUtils.getTransactTransactionCodes(transactionType, request));
                StringBuilder stringBuilder = new StringBuilder();
                for (Integer code : transactionCodes) {
                    stringBuilder.append(code + "+");
                }
                transactionCodesParam = StringUtils.trim(stringBuilder.toString());
            }

            params.put(Constants.PARAM_TRANSACTION_TYPE, transactionCodesParam);
            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }

    }
    
	public String findDatePriorToNintyDays() throws ParseException {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dt = dateFormat.format(new Date());
		diagnostic.prepareDebug("dt:"+ dt);
		// Parse the given date string into a Date object.
		// Note: This can throw a ParseException.
		Date myDate = dateFormat.parse(dt);

		// Use the Calendar class to subtract one day
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(myDate);
		calendar.add(Calendar.DAY_OF_YEAR, -90);

		// Use the date formatter to produce a formatted date string
		Date datePriorToNintyDays = calendar.getTime();
		String result = dateFormat.format(datePriorToNintyDays);
		diagnostic.prepareDebug("dt result:"+ result);

		return result;
	}
}