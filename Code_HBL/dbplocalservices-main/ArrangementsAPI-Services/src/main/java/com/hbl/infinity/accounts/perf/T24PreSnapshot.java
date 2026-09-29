package com.hbl.infinity.accounts.perf;

import org.apache.commons.lang3.StringUtils;

/**
 * What getAccountsFromT24PreProcessor derives from {@code dbxdb_contractcustomers_get}: the two request parameters
 * it sets from the customer's contract customers. Stored only when the lookup returned at least one record.
 */
public final class T24PreSnapshot extends GetListSnapshotCache.Snapshot {

    private String coreCustomerIdList;
    private String explicitCoreCustomerIdList;

    T24PreSnapshot() {
    }

    /**
     * @param coreCustomerIdList         URL-encoded core customer id list, exactly as sent to T24
     * @param explicitCoreCustomerIdList the explicitCoreCustomerIdList request parameter, exactly as set
     */
    public T24PreSnapshot(String coreCustomerIdList, String explicitCoreCustomerIdList) {
        this.coreCustomerIdList = coreCustomerIdList;
        this.explicitCoreCustomerIdList = explicitCoreCustomerIdList;
    }

    /** @return URL-encoded core customer id list */
    public String getCoreCustomerIdList() {
        return coreCustomerIdList;
    }

    /** @return the explicitCoreCustomerIdList request parameter */
    public String getExplicitCoreCustomerIdList() {
        return explicitCoreCustomerIdList;
    }

    @Override
    protected boolean isComplete() {
        return StringUtils.isNotBlank(coreCustomerIdList) && explicitCoreCustomerIdList != null;
    }
}
