package com.hbl.infinity.accounts.perf;

import org.apache.commons.lang3.StringUtils;

/**
 * What getAccountsFromT24PostProcessor reads from the database when nothing is pending: the accounts
 * NewAccountProcessing allows, and the customer's default account. Stored only when NewAccountProcessing reported
 * no new account and a default account exists, that is, when the request wrote nothing.
 */
public final class T24PostSnapshot extends GetListSnapshotCache.Snapshot {

    private String accounts;
    private String defaultAccountId;

    T24PostSnapshot() {
    }

    /**
     * @param accounts         the "accounts" value returned by NewAccountProcessing
     * @param defaultAccountId the account id returned by the default-account lookup
     */
    public T24PostSnapshot(String accounts, String defaultAccountId) {
        this.accounts = accounts;
        this.defaultAccountId = defaultAccountId;
    }

    /** @return the "accounts" value returned by NewAccountProcessing */
    public String getAccounts() {
        return accounts;
    }

    /** @return the customer's default account id */
    public String getDefaultAccountId() {
        return defaultAccountId;
    }

    @Override
    protected boolean isComplete() {
        return StringUtils.isNotBlank(accounts) && StringUtils.isNotBlank(defaultAccountId);
    }
}
