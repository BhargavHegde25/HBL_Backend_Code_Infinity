package com.kony.dbputilities.qfxbeans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class SignonMessageService {
    @XmlElement(name = "SONRS")
    private SignON signon;

    public SignON getSignon() {
        return this.signon;
    }

    public void setSignon(SignON signon) {
        this.signon = signon;
    }
}
