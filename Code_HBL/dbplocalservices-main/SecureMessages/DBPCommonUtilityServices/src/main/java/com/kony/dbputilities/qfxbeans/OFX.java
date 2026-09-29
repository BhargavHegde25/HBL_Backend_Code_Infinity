package com.kony.dbputilities.qfxbeans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "OFX")
@XmlAccessorType(XmlAccessType.FIELD)
public class OFX {
    @XmlElement(name = "SIGNONMSGSRSV1")
    private SignonMessageService signonMessageService;
    @XmlElement(name = "BANKMSGSRSV1")
    private BankMessageService bankingMessageService;
    @XmlElement(name = "BANKMSGSET")
    private BankMessageSet bankingMessageSet;

    public SignonMessageService getSignonMessageService() {
        return this.signonMessageService;
    }

    public void setSignonMessageService(SignonMessageService signonMessageService) {
        this.signonMessageService = signonMessageService;
    }

    public BankMessageService getBankingMessageService() {
        return this.bankingMessageService;
    }

    public void setBankingMessageService(BankMessageService bankingMessageService) {
        this.bankingMessageService = bankingMessageService;
    }

    public BankMessageSet getBankingMessageSet() {
        return this.bankingMessageSet;
    }

    public void setBankingMessageSet(BankMessageSet bankingMessageSet) {
        this.bankingMessageSet = bankingMessageSet;
    }
}