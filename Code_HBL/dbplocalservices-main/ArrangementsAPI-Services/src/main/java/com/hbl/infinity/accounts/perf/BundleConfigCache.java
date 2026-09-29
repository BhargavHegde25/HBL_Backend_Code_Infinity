package com.hbl.infinity.accounts.perf;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.LongSupplier;

import com.kony.dbputilities.util.BundleConfigurationHandler;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

/**
 * JVM-wide TTL cache in front of {@link BundleConfigurationHandler#fetchBundleConfigurations(String, DataControllerRequest)}.
 * <p>
 * The Admin bundle configurations depend only on the bundle name (not on the user or the request), so one entry
 * per bundle is shared by all requests. The TTL comes from
 * {@value GetListPerfConstants#PROP_BUNDLE_CONFIG_TTL_SECONDS} and is read on every call, so setting it to 0 turns
 * the cache off on the next request.
 * <p>
 * Contract, identical to calling the handler directly:
 * <ul>
 * <li>every caller gets its own mutable map, so a caller can never change what another request sees;</li>
 * <li>a failed load (the handler logs it and returns an empty map, or returns null) is returned as-is and is
 * <b>never cached</b>, so the next request tries again, exactly as today.</li>
 * </ul>
 * Concurrent requests that miss at the same moment may each load the bundle once; that only happens when an entry
 * is missing or has expired, and every load returns the same data.
 */
public final class BundleConfigCache {

    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    private static final BundleConfigCache INSTANCE =
            new BundleConfigCache(System::nanoTime, BundleConfigurationHandler::fetchBundleConfigurations);

    /** Loads a bundle; the production loader is the handler itself. */
    interface BundleLoader {
        Map<String, String> load(String bundleName, DataControllerRequest request);
    }

    private final Map<String, Entry> entries = new ConcurrentHashMap<>();
    private final LongSupplier clock;
    private final BundleLoader loader;

    BundleConfigCache(LongSupplier clock, BundleLoader loader) {
        this.clock = clock;
        this.loader = loader;
    }

    /**
     * Drop-in replacement for {@link BundleConfigurationHandler#fetchBundleConfigurations(String, DataControllerRequest)}.
     *
     * @param bundleName bundle name, for example {@link BundleConfigurationHandler#BUDLENAME_DBP}
     * @param request    current request, used only when the bundle has to be loaded
     * @return a new mutable map of the bundle's configurations, or whatever the handler returned when the load failed
     */
    public static Map<String, String> fetchBundleConfigurations(String bundleName, DataControllerRequest request) {
        return INSTANCE.get(bundleName, request, GetListPerfConfig.getBundleConfigTtlSeconds());
    }

    /**
     * Removes every cached bundle, so the next call loads from the Admin service again.
     */
    public static void invalidateAll() {
        INSTANCE.entries.clear();
    }

    Map<String, String> get(String bundleName, DataControllerRequest request, int ttlSeconds) {
        if (ttlSeconds <= 0 || bundleName == null) {
            return loader.load(bundleName, request);
        }
        // The handler matches bundle names ignoring case, so the cache does too.
        String key = bundleName.toUpperCase(Locale.ROOT);
        long now = clock.getAsLong();
        Entry entry = entries.get(key);
        if (entry != null && now - entry.loadedAtNanos < TimeUnit.SECONDS.toNanos(ttlSeconds)) {
            return new HashMap<>(entry.configurations);
        }

        Map<String, String> loaded = loader.load(bundleName, request);
        if (loaded != null && !loaded.isEmpty()) {
            entries.put(key, new Entry(Collections.unmodifiableMap(new HashMap<>(loaded)), now));
            if (diagnostic.isDebugEnabled()) {
                diagnostic.prepareDebug("BundleConfigCache: loaded bundle " + key + " (" + loaded.size() + " keys)")
                        .log();
            }
        }
        return loaded;
    }

    private static final class Entry {
        private final Map<String, String> configurations;
        private final long loadedAtNanos;

        private Entry(Map<String, String> configurations, long loadedAtNanos) {
            this.configurations = configurations;
            this.loadedAtNanos = loadedAtNanos;
        }
    }
}
