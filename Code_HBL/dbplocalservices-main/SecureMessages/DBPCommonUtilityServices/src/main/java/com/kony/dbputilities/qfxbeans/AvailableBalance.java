package com.kony.dbputilities.qfxbeans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class AvailableBalance {
    @XmlElement(name = "BALAMT")
    private double balanceAmount;
    @XmlElement(name = "DTASOF")
    private String balanceDate;

    public double getBalanceAmount() {
        return this.balanceAmount;
    }

    public void setBalanceAmount(double balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public String getBalanceDate() {
        return this.balanceDate;
    }

    public void setBalanceDate(String balanceDate) {
        this.balanceDate = balanceDate;
    }
}