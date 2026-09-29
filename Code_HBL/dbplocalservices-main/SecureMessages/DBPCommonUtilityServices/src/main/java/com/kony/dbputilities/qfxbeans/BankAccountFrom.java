package com.kony.dbputilities.qfxbeans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "bankId", "accountId", "accountType" })
public class BankAccountFrom {
    @XmlElement(name = "ACCTTYPE")
    private String accountType;
    @XmlElement(name = "ACCTID")
    private String accountId;
    @XmlElement(name = "BANKID")
    private long bankId;

    public String getAccountType() {
        return this.accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public long getBankId() {
        return this.bankId;
    }

    public void setBankId(long bankId) {
        this.bankId = bankId;
    }
}