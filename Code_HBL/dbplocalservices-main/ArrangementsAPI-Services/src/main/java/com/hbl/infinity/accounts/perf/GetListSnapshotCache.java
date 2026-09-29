package com.hbl.infinity.accounts.perf;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.function.IntSupplier;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Entry point of the getList snapshot cache.
 * <p>
 * A snapshot holds what one getList class read from the database for one customer. It is stored under a key built
 * from the stage, the customer, the global and customer version tokens ({@link PermissionVersionService}) and a
 * fingerprint of every input the class used for those reads. The class replays a snapshot only when all of these
 * match, so the same inputs and the same database state give the same response, whether it comes from the cache
 * or not.
 * <p>
 * {@link #open} returns null whenever the cache must not be used for this request: the switch
 * {@value GetListPerfConstants#PROP_CACHE_ENABLED} is off, the customer is unknown, or the version tokens cannot be
 * read. Callers then run today's code unchanged.
 */
public final class GetListSnapshotCache {

    private static final Gson GSON = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
    private static final char SEPARATOR = '\u0001';
    private static final String NULL_PART = "\u0000";

    private GetListSnapshotCache() {
    }

    /**
     * Opens the snapshot slot of one stage for this request.
     *
     * @param stage            one of the {@code GetListPerfConstants.STAGE_*} values
     * @param customerId       Infinity customer id
     * @param fingerprintParts every input the stage's database reads depend on
     * @return the slot, or null when the cache must not be used for this request
     */
    public static Session open(String stage, String customerId, String... fingerprintParts) {
        try {
            if (!GetListPerfConfig.isCacheEnabled()) {
                return null;
            }
            return open(PermissionVersionService.get(), GetListCache.get(), GetListPerfConfig::getEntTtlSeconds,
                    stage, customerId, fingerprintParts);
        } catch (RuntimeException | LinkageError e) {
            GetListCache.warn("snapshot cache unavailable (" + e.getClass().getSimpleName() + ")");
            return null;
        }
    }

    static Session open(PermissionVersionService versions, GetListCache cache, IntSupplier ttlSeconds, String stage,
            String customerId, String... fingerprintParts) {
        if (StringUtils.isBlank(customerId)) {
            return null;
        }
        String globalVersion = versions.globalVersion();
        if (globalVersion == null) {
            return null;
        }
        String customerVersion = versions.customerVersion(customerId);
        if (customerVersion == null) {
            return null;
        }
        String key = GetListPerfConstants.CACHE_KEY_PREFIX + stage + ":" + customerId + ":" + globalVersion + ":"
                + customerVersion + ":" + fingerprint(fingerprintParts);
        return new Session(key, cache, ttlSeconds.getAsInt());
    }

    /**
     * @param parts inputs, in a fixed order; null is distinct from an empty string
     * @return a hex SHA-256 digest of the parts
     */
    static String fingerprint(String... parts) {
        StringBuilder joined = new StringBuilder();
        for (String part : parts) {
            joined.append(part == null ? NULL_PART : part).append(SEPARATOR);
        }
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(joined.toString().getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder(digest.length * 2);
            for (byte b : digest) {
                hex.append(Character.forDigit((b >> 4) & 0xF, 16)).append(Character.forDigit(b & 0xF, 16));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            // Every Java runtime ships SHA-256.
            throw new IllegalStateException(e);
        }
    }

    /** Base class of every stored snapshot. */
    public abstract static class Snapshot {
        /** Raised whenever a snapshot class changes shape, so old entries are ignored. */
        static final int SCHEMA_VERSION = 1;

        private int schemaVersion = SCHEMA_VERSION;

        /**
         * @return true when every field the replay needs is present
         */
        protected abstract boolean isComplete();

        final boolean isUsable() {
            return schemaVersion == SCHEMA_VERSION && isComplete();
        }
    }

    /** The snapshot slot of one stage, for one request. */
    public static final class Session {
        private final String key;
        private final GetListCache cache;
        private final int ttlSeconds;

        Session(String key, GetListCache cache, int ttlSeconds) {
            this.key = key;
            this.cache = cache;
            this.ttlSeconds = ttlSeconds;
        }

        /**
         * @param type snapshot class
         * @return the stored snapshot, or null when it is missing, unreadable, from another schema or incomplete
         */
        public <T extends Snapshot> T read(Class<T> type) {
            try {
                GetListCache.Lookup lookup = cache.read(key);
                if (!lookup.isHit()) {
                    return null;
                }
                T snapshot = GSON.fromJson(lookup.getValue(), type);
                return snapshot != null && snapshot.isUsable() ? snapshot : null;
            } catch (RuntimeException e) {
                GetListCache.warn("unreadable snapshot ignored (" + e.getClass().getSimpleName() + ")");
                return null;
            }
        }

        /**
         * Stores a snapshot without waiting. Incomplete snapshots are not stored. Never throws.
         *
         * @param snapshot what the stage read from the database
         */
        public void store(Snapshot snapshot) {
            try {
                if (snapshot != null && snapshot.isUsable()) {
                    cache.write(key, GSON.toJson(snapshot), ttlSeconds);
                }
            } catch (RuntimeException e) {
                GetListCache.warn("snapshot not stored (" + e.getClass().getSimpleName() + ")");
            }
        }

        String key() {
            return key;
        }
    }
}
