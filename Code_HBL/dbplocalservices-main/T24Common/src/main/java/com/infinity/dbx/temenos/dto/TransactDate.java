package com.infinity.dbx.temenos.dto;

import java.util.Date;

public class TransactDate {

    private String companyId;
    private Date lastWorkingDate;
    private Date nextWorkingDate;
    private Date currentWorkingDate;

    /**
     * 
     */
    public TransactDate() {
        super();
    }

    public TransactDate(String companyId, Date lastWorkingDate, Date nextWorkingDate, Date currentWorkingDate) {
        super();
        this.setCompanyId(companyId);
        this.lastWorkingDate = lastWorkingDate;
        this.nextWorkingDate = nextWorkingDate;
        this.currentWorkingDate = currentWorkingDate;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Date getLastWorkingDate() {
        return lastWorkingDate;
    }

    public void setLastWorkingDate(Date lastWorkingDate) {
        this.lastWorkingDate = lastWorkingDate;
    }

    public Date getNextWorkingDate() {
        return nextWorkingDate;
    }

    public void setNextWorkingDate(Date nextWorkingDate) {
        this.nextWorkingDate = nextWorkingDate;
    }

    public Date getCurrentWorkingDate() {
        return currentWorkingDate;
    }

    public void setCurrentWorkingDate(Date currentWorkingDate) {
        this.currentWorkingDate = currentWorkingDate;
    }

}
