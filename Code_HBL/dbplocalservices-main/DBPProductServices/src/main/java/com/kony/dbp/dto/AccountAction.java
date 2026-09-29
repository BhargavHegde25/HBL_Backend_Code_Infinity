package com.kony.dbp.dto;

import java.util.HashSet;
import java.util.Set;

public class AccountAction {

    private String accountId;
    private boolean isBusinessAccount = true;
    private String membershipId;
    private Set<String> addedActions = new HashSet<>();
    private Set<String> removedActions = new HashSet<>();

    public String getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
    }

    public AccountAction() {

    }

    public String getAccountId() {
        return accountId;
    }

    public boolean isBusinessAccount() {
        return isBusinessAccount;
    }

    public void setIsBusinessAccount(boolean isBusinessAccount) {
        this.isBusinessAccount = isBusinessAccount;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Set<String> getAddedActions() {
        return addedActions;
    }

    public void setAddedActions(Set<String> addedActions) {
        this.addedActions = addedActions;
    }

    public Set<String> getRemovedActions() {
        return removedActions;
    }

    public void setRemovedActions(Set<String> removedActions) {
        this.removedActions = removedActions;
    }
}
