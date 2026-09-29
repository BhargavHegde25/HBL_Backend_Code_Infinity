package com.kony.dbputilities.qfxbeans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "transactionType", "transactionPostedDate", "userInitiatedTransactionDate", "amount",
        "transactionId", "checkNum", "name", "memo" })
public class Transaction {
    @XmlElement(name = "TRNTYPE")
    private String transactionType;
    @XmlElement(name = "TRNAMT")
    private double amount;
    @XmlElement(name = "FITID")
    private String transactionId;
    @XmlElement(name = "NAME")
    private String name;
    @XmlElement(name = "MEMO")
    private String memo;
    @XmlElement(name = "DTPOSTED")
    private String transactionPostedDate;
    @XmlElement(name = "DTUSER")
    private String userInitiatedTransactionDate;
    @XmlElement(name = "CHECKNUM")
    private String checkNum;

    public String getCheckNum() {
        return checkNum;
    }

    public void setCheckNum(String checkNum) {
        this.checkNum = checkNum;
    }

    public String getTransactionType() {
        return this.transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getTransactionPostedDate() {
        return this.transactionPostedDate;
    }

    public void setTransactionPostedDate(String transactionPostedDate) {
        this.transactionPostedDate = transactionPostedDate;
    }

    public String getUserInitiatedTransactionDate() {
        return this.userInitiatedTransactionDate;
    }

    public void setUserInitiatedTransactionDate(String userInitiatedTransactionDate) {
        this.userInitiatedTransactionDate = userInitiatedTransactionDate;
    }

    public Transaction() {
    }

    public Transaction(String transactionType, double amount, String transactionId, String name, String memo,
            String transactionPostedDate, String userInitiatedTransactionDate) {
        this.transactionType = transactionType;
        this.amount = amount;
        this.transactionId = transactionId;
        this.name = name;
        this.memo = memo;
        this.transactionPostedDate = transactionPostedDate;
        this.userInitiatedTransactionDate = userInitiatedTransactionDate;
    }
}
