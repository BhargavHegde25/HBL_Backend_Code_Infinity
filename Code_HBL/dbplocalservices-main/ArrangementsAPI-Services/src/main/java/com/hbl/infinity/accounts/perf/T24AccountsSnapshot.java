package com.hbl.infinity.accounts.perf;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * The balance cache entry: the complete response of ArrangementT24ISAccounts.getAccountsByCoreCustomerIdList (T24
 * accounts and balances, after the T24 pre- and postprocessors), exactly as GetAccountsOperation received it.
 */
public final class T24AccountsSnapshot extends GetListSnapshotCache.Snapshot {

    private String response;

    T24AccountsSnapshot() {
    }

    /**
     * @param response the integration service response, as returned by DBPServiceExecutor.getResponse()
     */
    public T24AccountsSnapshot(String response) {
        this.response = response;
    }

    /** @return the integration service response */
    public String getResponse() {
        return response;
    }

    @Override
    protected boolean isComplete() {
        return StringUtils.isNotBlank(response);
    }

    /**
     * Only a clean, settled response may be reused: opstatus 0, no error, at least one account, and no account
     * marked isNew (a new account is still being registered, so the next response will differ).
     *
     * @param response the integration service response
     * @return true when the response may be stored in the balance cache
     */
    public static boolean isStorable(String response) {
        if (StringUtils.isBlank(response)) {
            return false;
        }
        try {
            JsonElement parsed = new JsonParser().parse(response);
            if (!parsed.isJsonObject()) {
                return false;
            }
            JsonObject root = parsed.getAsJsonObject();
            if (!root.has("opstatus") || !root.get("opstatus").isJsonPrimitive()
                    || !"0".equals(root.get("opstatus").getAsString())) {
                return false;
            }
            if (root.has("dbpErrCode") || root.has("dbpErrMsg") || root.has("errmsg")) {
                return false;
            }
            if (!root.has("Accounts") || !root.get("Accounts").isJsonArray()
                    || root.getAsJsonArray("Accounts").size() == 0) {
                return false;
            }
            for (JsonElement account : root.getAsJsonArray("Accounts")) {
                if (!account.isJsonObject()) {
                    return false;
                }
                JsonElement isNew = account.getAsJsonObject().get("isNew");
                if (isNew != null && !isNew.isJsonNull() && "true".equalsIgnoreCase(isNew.getAsString())) {
                    return false;
                }
            }
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }
}
