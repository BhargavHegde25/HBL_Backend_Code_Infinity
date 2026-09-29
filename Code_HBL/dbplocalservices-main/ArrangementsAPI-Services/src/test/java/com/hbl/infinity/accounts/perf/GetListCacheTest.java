package com.hbl.infinity.accounts.perf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class GetListCacheTest {

    private Object nextValue;
    private RuntimeException nextFailure;
    private long delayMs;
    private String written;

    private final GetListCache.Backend backend = new GetListCache.Backend() {
        @Override
        public Object get(String key) {
            sleep();
            if (nextFailure != null) {
                throw nextFailure;
            }
            return nextValue;
        }

        @Override
        public void put(String key, String value, int ttlSeconds) {
            sleep();
            if (nextFailure != null) {
                throw nextFailure;
            }
            written = key + "=" + value + "@" + ttlSeconds;
        }
    };

    private final GetListCache cache = new GetListCache(backend, new PerfExecutor(2), () -> 100);

    @Test
    public void stringValueIsAHit() {
        nextValue = "v";
        GetListCache.Lookup lookup = cache.read("k");
        assertTrue(lookup.isHit());
        assertFalse(lookup.isError());
        assertEquals("v", lookup.getValue());
    }

    @Test
    public void nullOrBlankIsAMiss() {
        assertMiss(cache.read("k"));
        nextValue = " ";
        assertMiss(cache.read("k"));
    }

    @Test
    public void nonStringValueMeansTheCacheIsNotAvailable() {
        // MemoryManager.getFromCache returns new Object() when Fabric has no ResultCache.
        nextValue = new Object();
        assertError(cache.read("k"));
    }

    @Test
    public void exceptionIsAnError() {
        nextFailure = new IllegalStateException("down");
        assertError(cache.read("k"));
    }

    @Test
    public void slowReadIsAnError() {
        nextValue = "v";
        delayMs = 2000;
        long start = System.nanoTime();
        assertError(cache.read("k"));
        assertTrue(System.nanoTime() - start < 1_500_000_000L);
    }

    @Test
    public void writeAndWaitReportsTheOutcome() {
        assertTrue(cache.writeAndWait("k", "v", 60));
        assertEquals("k=v@60", written);
        nextFailure = new IllegalStateException("down");
        assertFalse(cache.writeAndWait("k", "v", 60));
        nextFailure = null;
        delayMs = 2000;
        assertFalse(cache.writeAndWait("k", "v", 60));
    }

    @Test
    public void failedWriteNeverThrows() {
        GetListCache inline = new GetListCache(backend, PerfExecutor.callerRuns(), () -> 100);
        nextFailure = new IllegalStateException("down");
        inline.write("k", "v", 60);
    }

    private void sleep() {
        if (delayMs > 0) {
            try {
                Thread.sleep(delayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static void assertMiss(GetListCache.Lookup lookup) {
        assertFalse(lookup.isHit());
        assertFalse(lookup.isError());
    }

    private static void assertError(GetListCache.Lookup lookup) {
        assertFalse(lookup.isHit());
        assertTrue(lookup.isError());
    }
}
