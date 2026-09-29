package com.hbl.infinity.accounts.perf;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.IntSupplier;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

/**
 * Timed access to the shared Fabric cache ({@link MemoryManager}) for the getList snapshot cache.
 * <p>
 * Reads wait at most {@value GetListPerfConstants#PROP_CACHE_TIMEOUT_MS} milliseconds. A read tells a miss (the
 * key is not there) apart from an error (the cache threw, timed out or is not available), because the callers
 * treat the two differently. Writes are fire-and-forget. Nothing here ever throws into the caller, and nothing
 * logs keys or values.
 */
public final class GetListCache {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    private static final long WARN_INTERVAL_NANOS = TimeUnit.MINUTES.toNanos(1);
    private static final AtomicLong lastWarnNanos = new AtomicLong(System.nanoTime() - WARN_INTERVAL_NANOS);

    /** Package-private so that tests in this package can install their own. */
    static volatile GetListCache instance;

    /** The cache operations this class needs; the production backend is {@link MemoryManager}. */
    interface Backend {
        Object get(String key);

        void put(String key, String value, int ttlSeconds);
    }

    /** Outcome of one read. */
    public static final class Lookup {
        private static final Lookup MISS = new Lookup(null, false);
        private static final Lookup ERROR = new Lookup(null, true);

        private final String value;
        private final boolean error;

        private Lookup(String value, boolean error) {
            this.value = value;
            this.error = error;
        }

        /** @return true when the key was found */
        public boolean isHit() {
            return value != null;
        }

        /** @return true when the cache could not be read; the caller must not rely on the cache */
        public boolean isError() {
            return error;
        }

        /** @return the cached value, or null when this is not a hit */
        public String getValue() {
            return value;
        }
    }

    private final Backend backend;
    private final PerfExecutor executor;
    private final IntSupplier timeoutMs;

    GetListCache(Backend backend, PerfExecutor executor, IntSupplier timeoutMs) {
        this.backend = backend;
        this.executor = executor;
        this.timeoutMs = timeoutMs;
    }

    /**
     * @return the shared instance backed by {@link MemoryManager}
     */
    public static GetListCache get() {
        GetListCache current = instance;
        if (current == null) {
            synchronized (GetListCache.class) {
                current = instance;
                if (current == null) {
                    current = new GetListCache(new MemoryManagerBackend(), PerfExecutor.get(),
                            GetListPerfConfig::getCacheTimeoutMs);
                    instance = current;
                }
            }
        }
        return current;
    }

    /**
     * Reads one key, waiting at most the configured timeout.
     *
     * @param key cache key
     * @return a hit, a miss, or an error
     */
    public Lookup read(String key) {
        try {
            Object value = executor.call(() -> backend.get(key), timeoutMs.getAsInt());
            if (value == null) {
                return Lookup.MISS;
            }
            if (!(value instanceof String)) {
                // MemoryManager returns a bare Object when the Fabric cache is not available.
                warn("Fabric cache not available");
                return Lookup.ERROR;
            }
            String text = (String) value;
            return StringUtils.isBlank(text) ? Lookup.MISS : new Lookup(text, false);
        } catch (Exception e) {
            warn("Fabric cache read failed or timed out (" + e.getClass().getSimpleName() + ")");
            return Lookup.ERROR;
        }
    }

    /**
     * Writes one key without waiting. A failed write is logged (rate-limited) and otherwise ignored.
     *
     * @param key        cache key
     * @param value      value to store
     * @param ttlSeconds time to live, in seconds
     */
    public void write(String key, String value, int ttlSeconds) {
        try {
            executor.submit(() -> {
                try {
                    backend.put(key, value, ttlSeconds);
                } catch (RuntimeException e) {
                    warn("Fabric cache write failed (" + e.getClass().getSimpleName() + ")");
                }
            });
        } catch (RuntimeException e) {
            warn("Fabric cache write failed (" + e.getClass().getSimpleName() + ")");
        }
    }

    /**
     * Writes one key and waits at most the configured timeout for the write to finish.
     *
     * @param key        cache key
     * @param value      value to store
     * @param ttlSeconds time to live, in seconds
     * @return true when the write finished without error inside the timeout
     */
    public boolean writeAndWait(String key, String value, int ttlSeconds) {
        try {
            executor.call(() -> {
                backend.put(key, value, ttlSeconds);
                return Boolean.TRUE;
            }, timeoutMs.getAsInt());
            return true;
        } catch (Exception e) {
            warn("Fabric cache write failed or timed out (" + e.getClass().getSimpleName() + ")");
            return false;
        }
    }

    /** Logs at most one warning per minute, so a cache outage cannot flood the log. */
    static void warn(String message) {
        long now = System.nanoTime();
        long last = lastWarnNanos.get();
        if (now - last >= WARN_INTERVAL_NANOS && lastWarnNanos.compareAndSet(last, now)) {
            alert.prepareWarn("getList cache: " + message + "; using the database for this request").log();
        }
    }

    private static final class MemoryManagerBackend implements Backend {
        @Override
        public Object get(String key) {
            return MemoryManager.getFromCache(key);
        }

        @Override
        public void put(String key, String value, int ttlSeconds) {
            MemoryManager.saveIntoCache(key, value, ttlSeconds);
        }
    }
}
