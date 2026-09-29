package com.hbl.infinity.accounts.perf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Test;

public class PerfExecutorTest {

    @Test
    public void returnsTheResultOnAPoolThread() throws Exception {
        PerfExecutor executor = new PerfExecutor(2);
        String thread = executor.call(() -> Thread.currentThread().getName(), 1000);
        assertTrue(thread.startsWith("hbl-getlist-perf-"));
    }

    @Test
    public void slowTaskTimesOut() throws Exception {
        PerfExecutor executor = new PerfExecutor(2);
        long start = System.nanoTime();
        try {
            executor.call(() -> {
                Thread.sleep(5000);
                return "late";
            }, 50);
            fail("expected a timeout");
        } catch (TimeoutException expected) {
            assertTrue(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start) < 2000);
        }
    }

    @Test
    public void taskExceptionIsRethrownUnwrapped() throws Exception {
        PerfExecutor executor = new PerfExecutor(2);
        try {
            executor.call(() -> {
                throw new IllegalStateException("boom");
            }, 1000);
            fail("expected the task's exception");
        } catch (IllegalStateException expected) {
            assertEquals("boom", expected.getMessage());
        }
    }

    @Test
    public void saturatedPoolRunsWorkOnTheCaller() throws Exception {
        PerfExecutor executor = new PerfExecutor(1);
        CountDownLatch release = new CountDownLatch(1);
        // One running task plus a full queue (4) saturate a pool of one thread.
        for (int i = 0; i < 5; i++) {
            executor.submit(() -> {
                try {
                    release.await(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        String caller = Thread.currentThread().getName();
        assertEquals(caller, executor.call(() -> Thread.currentThread().getName(), 50));
        String[] submitted = new String[1];
        executor.submit(() -> submitted[0] = Thread.currentThread().getName());
        assertEquals(caller, submitted[0]);
        release.countDown();
    }

    @Test
    public void callerRunsExecutorUsesTheCallingThread() throws Exception {
        PerfExecutor executor = PerfExecutor.callerRuns();
        String caller = Thread.currentThread().getName();
        assertEquals(caller, executor.call(() -> Thread.currentThread().getName(), 1));
        assertNotEquals("", caller);
    }
}
