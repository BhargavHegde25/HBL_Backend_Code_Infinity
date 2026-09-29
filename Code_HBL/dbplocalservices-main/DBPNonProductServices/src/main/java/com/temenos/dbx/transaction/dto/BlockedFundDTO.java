package com.temenos.dbx.transaction.dto;

import com.dbp.core.api.DBPDTO;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class BlockedFundDTO implements DBPDTO {
    
    private static final long serialVersionUID = -6570098244266983919L;
    private String accountID;
    private String searchStartDate;
    private String searchEndDate;
    private String lockedEventId;
    private String lockReason;
    private String fromDate;
    private String toDate;
    private String lockedAmount;
    private String transactionReference;
    
    public BlockedFundDTO() {
        super();
        // TODO Auto-generated constructor stub
    }

    public BlockedFundDTO(String accountID, String searchStartDate, String searchEndDate, String lockedEventId,
            String lockReason, String fromDate, String toDate, String lockedAmount, String transactionReference) {
        super();
        this.accountID = accountID;
        this.searchStartDate = searchStartDate;
        this.searchEndDate = searchEndDate;
        this.lockedEventId = lockedEventId;
        this.lockReason = lockReason;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.lockedAmount = lockedAmount;
        this.transactionReference = transactionReference;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getSearchStartDate() {
        return searchStartDate;
    }

    public void setSearchStartDate(String searchStartDate) {
        this.searchStartDate = searchStartDate;
    }

    public String getSearchEndDate() {
        return searchEndDate;
    }

    public void setSearchEndDate(String searchEndDate) {
        this.searchEndDate = searchEndDate;
    }

    public String getLockedEventId() {
        return lockedEventId;
    }

    public void setLockedEventId(String lockedEventId) {
        this.lockedEventId = lockedEventId;
    }

    public String getLockReason() {
        return lockReason;
    }

    public void setLockReason(String lockReason) {
        this.lockReason = lockReason;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getLockedAmount() {
        return lockedAmount;
    }

    public void setLockedAmount(String lockedAmount) {
        this.lockedAmount = lockedAmount;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((accountID == null) ? 0 : accountID.hashCode());
        result = prime * result + ((fromDate == null) ? 0 : fromDate.hashCode());
        result = prime * result + ((lockReason == null) ? 0 : lockReason.hashCode());
        result = prime * result + ((lockedAmount == null) ? 0 : lockedAmount.hashCode());
        result = prime * result + ((lockedEventId == null) ? 0 : lockedEventId.hashCode());
        result = prime * result + ((searchEndDate == null) ? 0 : searchEndDate.hashCode());
        result = prime * result + ((searchStartDate == null) ? 0 : searchStartDate.hashCode());
        result = prime * result + ((toDate == null) ? 0 : toDate.hashCode());
        result = prime * result + ((transactionReference == null) ? 0 : transactionReference.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BlockedFundDTO other = (BlockedFundDTO) obj;
        if (accountID == null) {
            if (other.accountID != null)
                return false;
        } else if (!accountID.equals(other.accountID))
            return false;
        if (fromDate == null) {
            if (other.fromDate != null)
                return false;
        } else if (!fromDate.equals(other.fromDate))
            return false;
        if (lockReason == null) {
            if (other.lockReason != null)
                return false;
        } else if (!lockReason.equals(other.lockReason))
            return false;
        if (lockedAmount == null) {
            if (other.lockedAmount != null)
                return false;
        } else if (!lockedAmount.equals(other.lockedAmount))
            return false;
        if (lockedEventId == null) {
            if (other.lockedEventId != null)
                return false;
        } else if (!lockedEventId.equals(other.lockedEventId))
            return false;
        if (searchEndDate == null) {
            if (other.searchEndDate != null)
                return false;
        } else if (!searchEndDate.equals(other.searchEndDate))
            return false;
        if (searchStartDate == null) {
            if (other.searchStartDate != null)
                return false;
        } else if (!searchStartDate.equals(other.searchStartDate))
            return false;
        if (toDate == null) {
            if (other.toDate != null)
                return false;
        } else if (!toDate.equals(other.toDate))
            return false;
        if (transactionReference == null) {
            if (other.transactionReference != null)
                return false;
        } else if (!transactionReference.equals(other.transactionReference))
            return false;
        return true;
    }

}
