package com.hbl.infinity.accounts.perf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

public class BundleConfigCacheTest {

    private static final int TTL = 600;

    private final AtomicLong clock = new AtomicLong(1_000L);
    private final List<String> loads = new ArrayList<>();
    private Map<String, String> nextLoad = bundle("TRANSFER_SUPPORTED_ACCOUNTS", "{}");
    private RuntimeException nextFailure;

    private final BundleConfigCache cache = new BundleConfigCache(clock::get, (name, request) -> {
        loads.add(name);
        if (nextFailure != null) {
            throw nextFailure;
        }
        return nextLoad == null ? null : new HashMap<>(nextLoad);
    });

    @Test
    public void secondCallWithinTtlIsServedFromCache() {
        Map<String, String> first = cache.get("DBP", null, TTL);
        advanceSeconds(TTL - 1);
        Map<String, String> second = cache.get("DBP", null, TTL);
        assertEquals(1, loads.size());
        assertEquals(first, second);
    }

    @Test
    public void entryIsReloadedAfterTtl() {
        cache.get("DBP", null, TTL);
        advanceSeconds(TTL);
        nextLoad = bundle("TRANSFER_SUPPORTED_ACCOUNTS", "{\"1001\":\"Dr\"}");
        Map<String, String> reloaded = cache.get("DBP", null, TTL);
        assertEquals(2, loads.size());
        assertEquals("{\"1001\":\"Dr\"}", reloaded.get("TRANSFER_SUPPORTED_ACCOUNTS"));
    }

    @Test
    public void zeroTtlCallsTheHandlerEveryTime() {
        cache.get("DBP", null, 0);
        cache.get("DBP", null, 0);
        assertEquals(2, loads.size());
    }

    @Test
    public void loweringTheTtlAppliesToExistingEntries() {
        cache.get("DBP", null, TTL);
        advanceSeconds(61);
        cache.get("DBP", null, 60);
        assertEquals(2, loads.size());
    }

    @Test
    public void emptyResultIsReturnedButNotCached() {
        nextLoad = new HashMap<>();
        assertTrue(cache.get("DBP", null, TTL).isEmpty());
        nextLoad = bundle("K", "V");
        assertEquals("V", cache.get("DBP", null, TTL).get("K"));
        assertEquals(2, loads.size());
    }

    @Test
    public void nullResultIsReturnedButNotCached() {
        nextLoad = null;
        assertNull(cache.get("UNKNOWN", null, TTL));
        assertNull(cache.get("UNKNOWN", null, TTL));
        assertEquals(2, loads.size());
    }

    @Test
    public void handlerExceptionPropagatesAndIsNotCached() {
        nextFailure = new IllegalStateException("admin down");
        try {
            cache.get("DBP", null, TTL);
            fail("expected the handler's exception");
        } catch (IllegalStateException expected) {
            // Same as calling the handler directly.
        }
        nextFailure = null;
        assertEquals("{}", cache.get("DBP", null, TTL).get("TRANSFER_SUPPORTED_ACCOUNTS"));
        assertEquals(2, loads.size());
    }

    @Test
    public void everyCallerGetsItsOwnMutableCopy() {
        Map<String, String> first = cache.get("DBP", null, TTL);
        Map<String, String> second = cache.get("DBP", null, TTL);
        assertNotSame(first, second);
        second.put("TRANSFER_SUPPORTED_ACCOUNTS", "changed by a caller");
        second.put("EXTRA", "x");
        Map<String, String> third = cache.get("DBP", null, TTL);
        assertEquals("{}", third.get("TRANSFER_SUPPORTED_ACCOUNTS"));
        assertNull(third.get("EXTRA"));
    }

    @Test
    public void callerChangesToTheLoadedMapDoNotReachTheCache() {
        Map<String, String> loaded = cache.get("DBP", null, TTL);
        loaded.put("EXTRA", "x");
        assertNull(cache.get("DBP", null, TTL).get("EXTRA"));
    }

    @Test
    public void bundleNamesAreMatchedIgnoringCaseAndKeptApart() {
        cache.get("DBP", null, TTL);
        cache.get("dbp", null, TTL);
        assertEquals(1, loads.size());
        nextLoad = bundle("C360_KEY", "v");
        assertEquals("v", cache.get("C360", null, TTL).get("C360_KEY"));
        assertEquals(2, loads.size());
        assertNull(cache.get("DBP", null, TTL).get("C360_KEY"));
    }

    @Test
    public void nullBundleNameGoesStraightToTheHandler() {
        cache.get(null, null, TTL);
        cache.get(null, null, TTL);
        assertEquals(2, loads.size());
        assertSame(null, loads.get(0));
    }

    private void advanceSeconds(long seconds) {
        clock.addAndGet(TimeUnit.SECONDS.toNanos(seconds));
    }

    private static Map<String, String> bundle(String key, String value) {
        Map<String, String> map = new HashMap<>();
        map.put(key, value);
        return map;
    }
}
