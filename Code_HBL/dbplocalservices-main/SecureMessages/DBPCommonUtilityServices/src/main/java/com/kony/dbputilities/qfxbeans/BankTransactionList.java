package com.kony.dbputilities.qfxbeans;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class BankTransactionList {
    @XmlElement(name = "DTSTART")
    private String transactionStartDate;
    @XmlElement(name = "DTEND")
    private String transactionEndDate;
    @XmlElement(name = "STMTTRN")
    private ArrayList<Transaction> bankTransactionList;

    public String getTransactionStartDate() {
        return this.transactionStartDate;
    }

    public void setTransactionStartDate(String transactionStartDate) {
        this.transactionStartDate = transactionStartDate;
    }

    public String getTransactionEndDate() {
        return this.transactionEndDate;
    }

    public void setTransactionEndDate(String transactionEndDate) {
        this.transactionEndDate = transactionEndDate;
    }

    public ArrayList<Transaction> getBankTransactionList() {
        return this.bankTransactionList;
    }

    public void setBankTransactionList(ArrayList<Transaction> bankTransactionList) {
        this.bankTransactionList = bankTransactionList;
    }
}