package com.hbl.infinity.accounts.perf;

import java.util.HashMap;
import java.util.Map;

/**
 * What GetAccountsPostLoginObjectServicePostProcessor reads from the database: the final "actions" string of each
 * account (after new feature actions are merged, exactly as attached to the response) and the per-account details
 * from {@code corecustomeraccounts_details_get_proc}. Stored only when every read succeeded, no new feature action
 * was found (so nothing was written in the background) and every account in the response has its details row.
 */
public final class ObjectPostSnapshot extends GetListSnapshotCache.Snapshot {

    private Map<String, String> actionsByAccount;
    private Map<String, Map<String, String>> accountDetails;

    ObjectPostSnapshot() {
    }

    /**
     * @param actionsByAccount account id to its "actions" JSON-array string
     * @param accountDetails   account id to its details map
     */
    public ObjectPostSnapshot(Map<String, String> actionsByAccount, Map<String, Map<String, String>> accountDetails) {
        this.actionsByAccount = actionsByAccount;
        this.accountDetails = accountDetails;
    }

    /** @return a new mutable copy of account id to "actions" string */
    public Map<String, String> getActionsByAccount() {
        return new HashMap<>(actionsByAccount);
    }

    /** @return a new mutable deep copy of account id to details */
    public Map<String, Map<String, String>> getAccountDetails() {
        Map<String, Map<String, String>> copy = new HashMap<>();
        for (Map.Entry<String, Map<String, String>> entry : accountDetails.entrySet()) {
            copy.put(entry.getKey(), entry.getValue() == null ? null : new HashMap<>(entry.getValue()));
        }
        return copy;
    }

    @Override
    protected boolean isComplete() {
        return actionsByAccount != null && accountDetails != null;
    }
}
