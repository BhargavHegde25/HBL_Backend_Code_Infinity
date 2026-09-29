package com.infinity.dbx.temenos.constants;

/**
 * @author Aditya Mankal
 *
 */
public enum TransactionType {

    DEPOSIT("Deposit"),
    WITHDRAWAL("Withdrawal"),
    CHECKS("Cheque"),
    TRANSFERS("Transfers"),
    BILL_PAY("BillPay"),
    PERSON_TO_PERSON_DEBITS("Person-To-Person-Debits"),
    PERSON_TO_PERSON_CREDITS("Person-To-Person-Credits");

    private String value;

    TransactionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    /**
     * @param value
     * @return
     */
    public static TransactionType getTransactionType(String value) {
        for (TransactionType transactionType : TransactionType.values()) {
            if (transactionType.value.equals(value)) {
                return transactionType;
            }
        }
        throw new IllegalArgumentException("Invalid Transaction Type");
    }

}
