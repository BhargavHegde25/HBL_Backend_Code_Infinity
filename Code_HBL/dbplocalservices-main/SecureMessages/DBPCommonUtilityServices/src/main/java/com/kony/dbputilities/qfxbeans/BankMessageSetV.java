package com.kony.dbputilities.qfxbeans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class BankMessageSetV {

    @XmlElement(name = "MSGSETCORE")
    private String messageStore;

    public String getMessageStore() {
        return messageStore;
    }

    public void setMessageStore(String messageStore) {
        this.messageStore = messageStore;
    }
}