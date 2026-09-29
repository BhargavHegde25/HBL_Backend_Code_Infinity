package com.infinity.dbx.temenos.transactions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.constants.TransactionType;
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
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class HBLAccountStatementPreprocessor extends TemenosBasePreProcessor {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    /** yyyy-MM-dd */
    private static final DateTimeFormatter ISO_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final String PARAM_TITLE = "title";

    /** A title starting with one of these is a monthly statement download. */
    private static final String[] MONTH_NAMES = { "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December" };

    ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);

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
            String toDate = request.getParameter(Constants.PARAM_SEARCH_END_DATE);
            if (StringUtils.isBlank(fromDate)) {
                String noOfDays = CommonUtils.getProperty(TemenosConstants.TEMENOS_PROPERTIES_FILE,
                        TemenosConstants.PROP_PREFIX_TEMENOS, TransactionConstants.PROP_SECTION_TRANSACTIONS,
                        TransactionConstants.PROP_NUMBER_OF_DAYS);

                int numberOfDays = Integer.parseInt(noOfDays);
                String currentDate = CommonUtils.convertDateToYYYYMMDD(application.getServerTimeStamp());

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                LocalDate date = LocalDate.parse(currentDate, formatter);

                LocalDate newDate = date.minusDays(numberOfDays);
                fromDate = newDate.format(formatter);
                params.put(Constants.PARAM_SEARCH_START_DATE, CommonUtils.convertDateToYYYYMMDD(fromDate));

            }

            if (StringUtils.isBlank(toDate)) {
                params.put(Constants.PARAM_SEARCH_END_DATE, CommonUtils.convertDateToYYYYMMDD(application.getServerTimeStamp()));
            }

            // HBL Changes - zero pad yyyy-M-d, and pin a monthly statement to the 1st.
            // Fail-open: on any problem the value is left exactly as it arrived.
            applyHblDateRules(params, request);

            String offset = CommonUtils.getParamValue(params, TransactionConstants.PARAM_OFFSET);
            // String limit = CommonUtils.getParamValue(params, TransactionConstants.PARAM_LIMIT);

            String limit = "";

            ServicesManager servicesManager = request.getServicesManager();
            OperationData op = servicesManager.getOperationData();
            String operationId = op.getOperationId();
            String transactionCodesParam;
            String transactionTypeParam = CommonUtils.getParamValue(params, Constants.PARAM_TRANSACTION_TYPE);

            if (operationId.equals("getCompletedTransactions")) {
                if (StringUtils.equalsIgnoreCase(transactionTypeParam, "Pending")) {
                    // Requested is for Pending Transactions download, so do not execute pending service
                    result.addOpstatusParam(0);
                    return Boolean.FALSE;
                }
                int TRANSACTIONS_LIMIT = 10;
                try {
                    TransactionsCountProperties transactionsCountProperties = new
                            TransactionsCountProperties(request, "Posted");
                    TRANSACTIONS_LIMIT = Integer.parseInt(TransactionsCountProperties.getValue("config_value"));
                } catch (Exception e) {
                    alert.prepareError(e.getMessage()).log();
                }
                limit = Integer.toString(TRANSACTIONS_LIMIT);
                params.put("limit", String.valueOf(limit));
            }
            else if (operationId.equals("getPendingTransactions")) {
                if (StringUtils.equalsIgnoreCase(transactionTypeParam, "Posted")) {
                    // Requested is for Posted Transactions download, so do not execute pending service
                    result.addOpstatusParam(0);
                    return Boolean.FALSE;
                }
                int TRANSACTIONS_LIMIT = 10;
                try {
                    TransactionsCountProperties transactionsCountProperties = new
                            TransactionsCountProperties(request, "Pending");
                    TRANSACTIONS_LIMIT = Integer.parseInt(TransactionsCountProperties.getValue("config_value"));
                } catch (Exception e) {
                    alert.prepareError("exception=" + e.getMessage()).log();
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

            // Transaction Code

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

    // ===================================================================
    // HBL date rules
    // ===================================================================

    /**
     * Two rules, nothing else:
     *
     * <ol>
     * <li>Zero pad an unpadded ISO date - 2026-6-14 becomes 2026-06-14. Applied to
     * both searchStartDate and searchEndDate.</li>
     * <li>A title naming a month (June_2026, July_2026, ...) is a monthly statement,
     * so searchStartDate is pinned to the 1st of that month - 2026-6-14 becomes
     * 2026-06-01. The year comes from the title when it carries one, otherwise
     * from the date itself.</li>
     * </ol>
     *
     * <p>Component order is never changed. Anything unreadable is left as it arrived.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void applyHblDateRules(HashMap params, DataControllerRequest request) {
        String rawStart = null;
        String rawEnd = null;
        try {
            rawStart = readParam(params, request, Constants.PARAM_SEARCH_START_DATE);
            rawEnd = readParam(params, request, Constants.PARAM_SEARCH_END_DATE);
            String title = readParam(params, request, PARAM_TITLE);

            // 1. zero pad
            String start = padIsoDate(rawStart);
            String end = padIsoDate(rawEnd);

            // 2. monthly statement starts on the 1st
            int month = monthFromTitle(title);
            if (month > 0) {
                int year = yearFromTitle(title);
                if (year <= 0) {
                    year = yearFromIsoDate(start);
                }
                if (year > 0) {
                    String firstOfMonth = LocalDate.of(year, month, 1).format(ISO_DATE);
                    if (!StringUtils.equals(firstOfMonth, start)) {
                        alert.prepareError("HBL## monthly statement '" + title + "' started at " + start
                                + ", pinned to " + firstOfMonth).log();
                    }
                    start = firstOfMonth;
                }
            }

            if (StringUtils.isNotBlank(start)) {
                params.put(Constants.PARAM_SEARCH_START_DATE, start);
            }
            if (StringUtils.isNotBlank(end)) {
                params.put(Constants.PARAM_SEARCH_END_DATE, end);
            }

            diagnostic.prepareDebug("HBL## title=" + title + " dates [" + rawStart + " / " + rawEnd
                    + "] -> [" + start + " / " + end + "]").log();

        } catch (Exception e) {
            // Never let date handling break the enquiry.
            alert.prepareError("HBL## date rules skipped for [" + rawStart + " / " + rawEnd + "]: "
                    + e.getMessage(), e).log();
        }
    }

    /**
     * Reads a value from the forwarded params first, then the request. The channel
     * posts a JSON body, so request.getParameter alone is not reliable here.
     */
    @SuppressWarnings("rawtypes")
    private String readParam(HashMap params, DataControllerRequest request, String key) {
        String value = null;
        try {
            value = CommonUtils.getParamValue(params, key);
        } catch (Exception e) {
            // fall through to the request
        }
        if (StringUtils.isBlank(value) && request != null) {
            try {
                value = request.getParameter(key);
            } catch (Exception e) {
                // leave blank
            }
        }
        return value;
    }

    /**
     * yyyy-M-d / yyyy-MM-d / yyyy-M-dd -> yyyy-MM-dd. The order of the components is
     * never touched. A value that is not a real yyyy-M-d date is returned unchanged.
     */
    private String padIsoDate(String value) {
        if (StringUtils.isBlank(value)) {
            return value;
        }
        String v = value.trim();
        String[] p = v.split("-");
        if (p.length != 3 || p[0].trim().length() != 4) {
            return v;
        }
        try {
            int year = Integer.parseInt(p[0].trim());
            int month = Integer.parseInt(p[1].trim());
            int day = Integer.parseInt(p[2].trim());
            // LocalDate.of rejects 2026-02-30 rather than rolling it over.
            return LocalDate.of(year, month, day).format(ISO_DATE);
        } catch (Exception e) {
            return v;
        }
    }

    /** 1-12 when the title starts with a month name (June, June_2026, "July 2026"); 0 otherwise. */
    private int monthFromTitle(String title) {
        if (StringUtils.isBlank(title)) {
            return 0;
        }
        String head = title.trim().split("[_\\s-]")[0];
        for (int i = 0; i < MONTH_NAMES.length; i++) {
            if (StringUtils.equalsIgnoreCase(MONTH_NAMES[i], head)) {
                return i + 1;
            }
        }
        return 0;
    }

    /** The 4 digit year carried by the title (June_2026 -> 2026); 0 when it has none. */
    private int yearFromTitle(String title) {
        if (StringUtils.isBlank(title)) {
            return 0;
        }
        String[] parts = title.trim().split("[_\\s-]");
        for (int i = 0; i < parts.length; i++) {
            String p = parts[i].trim();
            if (p.length() == 4) {
                try {
                    int year = Integer.parseInt(p);
                    if (year >= 1900 && year <= 9999) {
                        return year;
                    }
                } catch (NumberFormatException e) {
                    // not a year, keep looking
                }
            }
        }
        return 0;
    }

    /** Leading year of a yyyy-... value; 0 when unreadable. */
    private int yearFromIsoDate(String isoDate) {
        if (StringUtils.isBlank(isoDate) || isoDate.trim().length() < 4) {
            return 0;
        }
        try {
            return Integer.parseInt(isoDate.trim().substring(0, 4));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}