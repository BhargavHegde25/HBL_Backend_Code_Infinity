package com.kony.dbputilities.qfxbeans;

import java.util.HashMap;
import java.util.Map;

public class QFXHelper {

    /**
     * Transaction response fields
     */
    public static final String AMOUNT = "amount";
    public static final String TRANS_ID = "transactionId";
    public static final String TRANS_DATE = "transactionDate";
    public static final String TRANS_TYPE = "transactionType";
    public static final String TO_ACCT_NAME = "toAccountName";
    public static final String PAYEE_NAME = "payeeNickName";
    public static final String DESCRIPTION = "description";
    public static final String CHK_NUM = "checkNumber";
    /**
     * Accounts response fields
     * 
     */
    public static final String ACCT_ID = "accountID";
    public static final String ACCT_TYPE = "accountType";
    public static final String BANK_ID = "bsbNum";
    public static final String AVAILABLE_BAL = "availableBalance";
    public static final String CURR_BAL = "currentBalance";

    public Map<String, String> getTransactionTypesMap() {
        HashMap<String, String> transactionTypes = new HashMap<>();
        transactionTypes.put("Credit", "CREDIT");
        transactionTypes.put("Withdrawal", "DEBIT");
        transactionTypes.put("Interest", "INT");
        transactionTypes.put("Deposit", "DEP");
        transactionTypes.put("CheckWithdrawal", "CHECK");
        transactionTypes.put("POS", "POS");
        transactionTypes.put("Fee", "FEE");
        transactionTypes.put("Tax", "DEBIT");
        transactionTypes.put("CardPayment", "DIRECTDEBIT");
        transactionTypes.put("StopCheckPaymentRequest", "HOLD");
        transactionTypes.put("ReceivedRequest", "OTHER");
        transactionTypes.put("ReceivedP2P", "OTHER");
        transactionTypes.put("Loan", "DEBIT");
        transactionTypes.put("Request", "OTHER");
        transactionTypes.put("Cardless", "CASH");
        transactionTypes.put("P2P", "PAYMENT");
        transactionTypes.put("BillPay", "PAYMENT");
        transactionTypes.put("Wire", "PAYMENT");
        transactionTypes.put("InternetTransaction", "PAYMENT");
        transactionTypes.put("ExternalTransfer", "XFER");
        transactionTypes.put("InternalTransfer", "XFER");
        return transactionTypes;
    }

    public Map<String, String> getAccountTypesMap() {
        HashMap<String, String> accountTypes = new HashMap<>();
        accountTypes.put("Checking", "CHECKING");
        accountTypes.put("Savings", "SAVINGS");
        accountTypes.put("Deposit", "SAVINGS");
        accountTypes.put("Share", "CHECKING");

        return accountTypes;
    }
}
