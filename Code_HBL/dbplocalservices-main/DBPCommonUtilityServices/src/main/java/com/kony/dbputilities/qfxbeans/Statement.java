package com.kony.dbputilities.qfxbeans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Statement {
    @XmlElement(name = "CURDEF")
    private String currency;
    @XmlElement(name = "BANKACCTFROM")
    private BankAccountFrom bankAccountForm;
    @XmlElement(name = "BANKTRANLIST")
    private BankTransactionList bankTransactionList;
    @XmlElement(name = "LEDGERBAL")
    private LedgerBalance ledgerBalance;
    @XmlElement(name = "AVAILBAL")
    private AvailableBalance availableBalance;

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BankAccountFrom getBankAccountForm() {
        return this.bankAccountForm;
    }

    public void setBankAccountForm(BankAccountFrom bankAccountForm) {
        this.bankAccountForm = bankAccountForm;
    }

    public BankTransactionList getBankTransactionList() {
        return this.bankTransactionList;
    }

    public void setBankTransactionList(BankTransactionList bankTransactionList) {
        this.bankTransactionList = bankTransactionList;
    }

    public LedgerBalance getLedgerBalance() {
        return this.ledgerBalance;
    }

    public void setLedgerBalance(LedgerBalance ledgerBalance) {
        this.ledgerBalance = ledgerBalance;
    }

    public AvailableBalance getAvailableBalance() {
        return this.availableBalance;
    }

    public void setAvailableBalance(AvailableBalance availableBalance) {
        this.availableBalance = availableBalance;
    }
}