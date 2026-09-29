package com.kony.dbputilities.qfxbeans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "transactionUid", "status", "statement" })
@XmlAccessorType(XmlAccessType.FIELD)
public class StatementWraper {
    @XmlElement(name = "STMTRS")
    private Statement statement;
    @XmlElement(name = "TRNUID")
    private long transactionUid;
    @XmlElement(name = "STATUS")
    private Status status;

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Statement getStatement() {
        return this.statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public long getTransactionUid() {
        return this.transactionUid;
    }

    public void setTransactionUid(long transactionUid) {
        this.transactionUid = transactionUid;
    }
}