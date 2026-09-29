package com.kony.dbputilities.qfxbeans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class FinancialInstitution {
    @XmlElement(name = "ORG")
    private String org;
    @XmlElement(name = "FID")
    private long financialId;

    public String getOrg() {
        return this.org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public long getFinancialId() {
        return this.financialId;
    }

    public void setFinancialId(long financialId) {
        this.financialId = financialId;
    }
}