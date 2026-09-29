package com.infinity.dbx.temenos.transactions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.constants.TransactionType;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.objects.Account;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
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

            String fromDate = request.getParameter(Constants.PARAM_SEARCH_START_DATE);
            if (StringUtils.isBlank(fromDate)) {
                String noOfDays = CommonUtils.getProperty(TemenosConstants.TEMENOS_PROPERTIES_FILE,
                        TemenosConstants.PROP_PREFIX_TEMENOS, TransactionConstants.PROP_SECTION_TRANSACTIONS,
                        TransactionConstants.PROP_NUMBER_OF_DAYS);
                fromDate = TransactionUtils.getMinusDays(noOfDays);
            }
            params.put(Constants.PARAM_SEARCH_START_DATE, CommonUtils.convertDateToYYYYMMDD(fromDate));
            String offset = CommonUtils.getParamValue(params, TransactionConstants.PARAM_OFFSET);
            String limit = CommonUtils.getParamValue(params, TransactionConstants.PARAM_LIMIT);
            if (StringUtils.isNotBlank(offset) && StringUtils.isNotBlank(limit)) {
                int page_start = (Integer.parseInt(offset) / Integer.parseInt(limit)) + 1;
                params.put(TransactionConstants.PARAM_PAGE_START, String.valueOf(page_start));
            }
            alert.prepareError("Params obtained:" + params).log();
            // TransactionUtils.getT24TransactionType(params, request);

            // Transaction Code
            String transactionCodesParam;
            String transactionTypeParam = CommonUtils.getParamValue(params, Constants.PARAM_TRANSACTION_TYPE);
            if (StringUtils.isBlank(transactionTypeParam)
                    || StringUtils.equalsIgnoreCase(transactionTypeParam, TransactionConstants.PARAM_VALUE_ALL)
                    || StringUtils.equalsIgnoreCase(transactionTypeParam, TransactionConstants.PARAM_VALUE_BOTH)) {
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
}