package com.hbl.infinity.accounts.perf;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Test-only: replaces the Fabric cache behind the getList snapshot cache with an in-memory map, and runs all cache
 * work on the calling thread so that Mockito static mocks (which are per thread) apply and writes are visible at
 * once. Always call {@link #uninstall()} when done.
 */
public final class GetListCacheTestSupport {

    /** In-memory cache; set {@link #failing} to simulate an unreachable cache. */
    public static final class InMemoryBackend implements GetListCache.Backend {
        public final Map<String, String> store = new ConcurrentHashMap<>();
        public volatile boolean failing;
        public int reads;
        public int writes;

        @Override
        public Object get(String key) {
            reads++;
            if (failing) {
                throw new IllegalStateException("cache down");
            }
            return store.get(key);
        }

        @Override
        public void put(String key, String value, int ttlSeconds) {
            writes++;
            if (failing) {
                throw new IllegalStateException("cache down");
            }
            store.put(key, value);
        }
    }

    private GetListCacheTestSupport() {
    }

    /**
     * @return the installed in-memory backend
     */
    public static InMemoryBackend install() {
        InMemoryBackend backend = new InMemoryBackend();
        PerfExecutor executor = PerfExecutor.callerRuns();
        GetListCache cache = new GetListCache(backend, executor, () -> 1000);
        PerfExecutor.instance = executor;
        GetListCache.instance = cache;
        PermissionVersionService.instance = new PermissionVersionService(cache, () -> 86400);
        return backend;
    }

    /** Restores the production singletons (created again on next use). */
    public static void uninstall() {
        PermissionVersionService.instance = null;
        GetListCache.instance = null;
        PerfExecutor.instance = null;
    }
}
