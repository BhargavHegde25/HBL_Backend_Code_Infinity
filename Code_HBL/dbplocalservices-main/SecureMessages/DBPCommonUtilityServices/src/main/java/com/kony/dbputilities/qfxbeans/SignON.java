package com.kony.dbputilities.qfxbeans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "serverStatus", "serverDate", "language", "fi", "intuitBuid", "intuitUserId" })
public class SignON {
    @XmlElement(name = "LANGUAGE")
    private String language;
    @XmlElement(name = "INTU.BID")
    private long intuitBuid;
    @XmlElement(name = "INTU.USERID")
    private String intuitUserId;
    @XmlElement(name = "DTSERVER")
    private String serverDate;
    @XmlElement(name = "STATUS")
    private Status serverStatus;
    @XmlElement(name = "FI")
    private FinancialInstitution fi;

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public long getIntuitBuid() {
        return this.intuitBuid;
    }

    public void setIntuitBuid(long intuitBuid) {
        this.intuitBuid = intuitBuid;
    }

    public String getIntuitUserId() {
        return this.intuitUserId;
    }

    public void setIntuitUserId(String intuitUserId) {
        this.intuitUserId = intuitUserId;
    }

    public String getServerDate() {
        return this.serverDate;
    }

    public void setServerDate(String serverDate) {
        this.serverDate = serverDate;
    }

    public Status getServerStatus() {
        return this.serverStatus;
    }

    public void setServerStatus(Status serverStatus) {
        this.serverStatus = serverStatus;
    }

    public FinancialInstitution getFi() {
        return this.fi;
    }

    public void setFi(FinancialInstitution fi) {
        this.fi = fi;
    }
}
