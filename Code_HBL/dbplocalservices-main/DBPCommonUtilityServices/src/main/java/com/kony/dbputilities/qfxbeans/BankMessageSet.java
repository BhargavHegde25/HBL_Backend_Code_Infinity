package com.kony.dbputilities.qfxbeans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class BankMessageSet {
    @XmlElement(name = "BANKMSGSETV1")
    private BankMessageSetV bankMessageSetV1;

    public BankMessageSetV getBankMessageSetV1() {
        return bankMessageSetV1;
    }

    public void setBankMessageSetV1(BankMessageSetV bankMessageSetV1) {
        this.bankMessageSetV1 = bankMessageSetV1;
    }

}