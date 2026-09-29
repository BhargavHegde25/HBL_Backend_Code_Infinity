package com.hbl.infinity.accounts.perf;

import java.util.concurrent.TimeUnit;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

/**
 * Per-request stage timer for one getList component. When {@value GetListPerfConstants#PROP_TIMING_LOG} is off
 * (the default) every method returns immediately and nothing is measured or logged.
 * <p>
 * When it is on, {@link #finish()} writes one line such as
 * {@code getList timing [getAccountsFromT24PostProcessor] thread=... total=41ms bundleConfig=12ms defaultAccount=9ms ...}.
 * The line is written at INFO, or at WARN when INFO is disabled, so that switching the property on is enough to see
 * it. The line never contains customer data.
 * <p>
 * Not thread-safe: create one instance per request and component.
 */
public final class GetListTimer {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    private static final GetListTimer DISABLED = new GetListTimer(null, false);

    private final String component;
    private final boolean enabled;
    private final long startNanos;
    private long lastNanos;
    private final StringBuilder stages;

    private GetListTimer(String component, boolean enabled) {
        this.component = component;
        this.enabled = enabled;
        this.startNanos = enabled ? System.nanoTime() : 0L;
        this.lastNanos = startNanos;
        this.stages = enabled ? new StringBuilder() : null;
    }

    /**
     * Starts a timer for a component. Reads the timing switch once.
     *
     * @param component component name, one of the {@code COMPONENT_*} constants
     * @return a running timer, or a no-op timer when timing is off
     */
    public static GetListTimer start(String component) {
        return GetListPerfConfig.isTimingLogEnabled() ? new GetListTimer(component, true) : DISABLED;
    }

    /**
     * Records the time since the previous mark (or since start) under the given stage name.
     *
     * @param stage short stage name, without spaces
     */
    public void mark(String stage) {
        if (!enabled) {
            return;
        }
        long now = System.nanoTime();
        stages.append(' ').append(stage).append('=').append(toMillis(now - lastNanos)).append("ms");
        lastNanos = now;
    }

    /**
     * Writes the timing summary line. Call once, at the end of the component (also on error paths).
     */
    public void finish() {
        if (!enabled) {
            return;
        }
        String line = "getList timing [" + component + "] thread=" + Thread.currentThread().getName() + " total="
                + toMillis(System.nanoTime() - startNanos) + "ms" + stages;
        if (diagnostic.isInfoEnabled()) {
            diagnostic.prepareInfo(line).log();
        } else {
            alert.prepareWarn(line).log();
        }
    }

    private static long toMillis(long nanos) {
        return TimeUnit.NANOSECONDS.toMillis(nanos);
    }
}
