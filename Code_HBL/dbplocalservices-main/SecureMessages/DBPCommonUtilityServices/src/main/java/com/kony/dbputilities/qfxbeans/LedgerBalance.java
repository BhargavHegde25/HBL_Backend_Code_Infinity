package com.kony.dbputilities.qfxbeans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class LedgerBalance {
    @XmlElement(name = "BALAMT")
    private double ledgerBalanceAmount;
    @XmlElement(name = "DTASOF")
    private String ledgerBalanceDate;

    public double getLedgerBalanceAmount() {
        return this.ledgerBalanceAmount;
    }

    public void setLedgerBalanceAmount(double ledgerBalanceAmount) {
        this.ledgerBalanceAmount = ledgerBalanceAmount;
    }

    public String getLedgerBalanceDate() {
        return this.ledgerBalanceDate;
    }

    public void setLedgerBalanceDate(String ledgerBalanceDate) {
        this.ledgerBalanceDate = ledgerBalanceDate;
    }
}