package com.kony.dbputilities.qfxbeans;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class BankMessageService {
    @XmlElement(name = "STMTTRNRS", type = StatementWraper.class)
    private ArrayList<StatementWraper> statementwrapperList;
    private StatementWraper statementwrapper;

    public StatementWraper getStatementwrapper() {
        return this.statementwrapper;
    }

    public void setStatementwrapper(StatementWraper statementwrapper) {
        this.statementwrapper = statementwrapper;
    }

    public void addStatementwrapper(StatementWraper statementwrapper) {
        if (null == statementwrapperList) {
            statementwrapperList = new ArrayList<>();
        }
        this.statementwrapperList.add(statementwrapper);
    }
}