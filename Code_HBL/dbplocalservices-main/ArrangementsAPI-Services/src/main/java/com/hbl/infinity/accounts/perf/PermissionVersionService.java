package com.hbl.infinity.accounts.perf;

import java.util.function.IntSupplier;

import org.apache.commons.lang3.StringUtils;

import com.hbl.infinity.accounts.perf.invalidation.GetListCacheInvalidator;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

/**
 * Version tokens that make cached getList snapshots obsolete.
 * <p>
 * Every snapshot key contains the current global token and the current token of its customer. Replacing a token
 * ({@link #bump(String)} for one customer, {@link #bumpGlobal()} for everybody) means the next getList builds new
 * keys, misses, and reads the database again; the old snapshots are never read again and expire on their own.
 * <p>
 * Tokens are random values, not counters, so two concurrent bumps can never produce the same token and no atomic
 * increment is needed. If two requests create a missing token at the same moment, the last write wins and a
 * snapshot stored under the other token is simply never read.
 */
public final class PermissionVersionService {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    /** Package-private so that tests in this package can install their own. */
    static volatile PermissionVersionService instance;

    private final GetListCache cache;
    private final IntSupplier ttlSeconds;

    PermissionVersionService(GetListCache cache, IntSupplier ttlSeconds) {
        this.cache = cache;
        this.ttlSeconds = ttlSeconds;
    }

    /**
     * @return the shared instance
     */
    public static PermissionVersionService get() {
        PermissionVersionService current = instance;
        if (current == null) {
            synchronized (PermissionVersionService.class) {
                current = instance;
                if (current == null) {
                    current = new PermissionVersionService(GetListCache.get(),
                            GetListPerfConfig::getPermVerTtlSeconds);
                    instance = current;
                }
            }
        }
        return current;
    }

    /**
     * Makes every cached getList snapshot of one customer obsolete. Never throws: a failure is logged and the
     * snapshots then expire after {@value GetListPerfConstants#PROP_ENT_TTL_SECONDS} at the latest.
     *
     * @param customerId Infinity customer id
     */
    public void bump(String customerId) {
        if (StringUtils.isBlank(customerId)) {
            return;
        }
        replace(GetListPerfConstants.KEY_CUSTOMER_VERSION + customerId);
    }

    /**
     * Makes every cached getList snapshot of every customer obsolete, for changes that are not tied to one
     * customer (for example a feature or service-definition change). Never throws.
     */
    public void bumpGlobal() {
        replace(GetListPerfConstants.KEY_GLOBAL_VERSION);
    }

    /**
     * @param customerId Infinity customer id
     * @return the customer's current token (created when missing), or null when the cache cannot be read
     */
    String customerVersion(String customerId) {
        return currentVersion(GetListPerfConstants.KEY_CUSTOMER_VERSION + customerId);
    }

    /**
     * @return the current global token (created when missing), or null when the cache cannot be read
     */
    String globalVersion() {
        return currentVersion(GetListPerfConstants.KEY_GLOBAL_VERSION);
    }

    private String currentVersion(String key) {
        GetListCache.Lookup lookup = cache.read(key);
        if (lookup.isError()) {
            return null;
        }
        if (lookup.isHit()) {
            return lookup.getValue();
        }
        String token = newToken();
        cache.write(key, token, ttlSeconds.getAsInt());
        return token;
    }

    private void replace(String key) {
        try {
            if (!cache.writeAndWait(key, newToken(), ttlSeconds.getAsInt())) {
                alert.prepareError("getList cache: version bump could not be written; cached permissions expire "
                        + "within HBL_GETLIST_ENT_TTL_SECONDS").log();
            }
        } catch (RuntimeException e) {
            alert.prepareError("getList cache: version bump failed; cached permissions expire within "
                    + "HBL_GETLIST_ENT_TTL_SECONDS", e).log();
        }
    }

    static String newToken() {
        return GetListCacheInvalidator.newToken();
    }
}
