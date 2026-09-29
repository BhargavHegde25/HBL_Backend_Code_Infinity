package com.hbl.infinity.accounts.perf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

import org.junit.Test;
import org.mockito.MockedStatic;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;

public class GetListPerfConfigTest {

    @Test
    public void timingLogDefaultsToOffWhenPropertyMissing() {
        assertFalse(withProperty(null));
        assertFalse(withProperty(""));
    }

    @Test
    public void timingLogAcceptsTrueAndFalseInAnyCase() {
        assertTrue(withProperty("true"));
        assertTrue(withProperty(" TRUE "));
        assertFalse(withProperty("false"));
    }

    @Test
    public void invalidValueFallsBackToDefault() {
        assertFalse(withProperty("yes"));
        assertFalse(withProperty("1"));
    }

    @Test
    public void unreadablePropertiesFallBackToDefault() {
        try (MockedStatic<EnvironmentConfigurationsHandler> env = mockStatic(EnvironmentConfigurationsHandler.class)) {
            env.when(() -> EnvironmentConfigurationsHandler.getServerProperty(anyString()))
                    .thenThrow(new IllegalStateException("no Fabric"));
            assertFalse(GetListPerfConfig.isTimingLogEnabled());
        }
    }

    @Test
    public void timerIsSafeWhenDisabledAndWhenEnabled() {
        try (MockedStatic<EnvironmentConfigurationsHandler> env = mockStatic(EnvironmentConfigurationsHandler.class)) {
            env.when(() -> EnvironmentConfigurationsHandler.getServerProperty(GetListPerfConstants.PROP_TIMING_LOG))
                    .thenReturn("false");
            GetListTimer off = GetListTimer.start(GetListPerfConstants.COMPONENT_T24_POST);
            off.mark("stage");
            off.finish();

            env.when(() -> EnvironmentConfigurationsHandler.getServerProperty(GetListPerfConstants.PROP_TIMING_LOG))
                    .thenReturn("true");
            GetListTimer on = GetListTimer.start(GetListPerfConstants.COMPONENT_T24_POST);
            on.mark("stage");
            on.finish();
        }
    }

    @Test
    public void bundleConfigTtlDefaultsTo600Seconds() {
        assertEquals(600, bundleTtlWithProperty(null));
        assertEquals(600, bundleTtlWithProperty(" "));
    }

    @Test
    public void bundleConfigTtlAcceptsZeroAndPositiveValues() {
        assertEquals(0, bundleTtlWithProperty("0"));
        assertEquals(120, bundleTtlWithProperty(" 120 "));
    }

    @Test
    public void invalidBundleConfigTtlFallsBackToDefault() {
        assertEquals(600, bundleTtlWithProperty("-1"));
        assertEquals(600, bundleTtlWithProperty("ten"));
        assertEquals(600, bundleTtlWithProperty("99999999999"));
    }

    private static int bundleTtlWithProperty(String value) {
        try (MockedStatic<EnvironmentConfigurationsHandler> env = mockStatic(EnvironmentConfigurationsHandler.class)) {
            env.when(() -> EnvironmentConfigurationsHandler.getServerProperty(
                    GetListPerfConstants.PROP_BUNDLE_CONFIG_TTL_SECONDS)).thenReturn(value);
            return GetListPerfConfig.getBundleConfigTtlSeconds();
        }
    }

    private static boolean withProperty(String value) {
        try (MockedStatic<EnvironmentConfigurationsHandler> env = mockStatic(EnvironmentConfigurationsHandler.class)) {
            env.when(() -> EnvironmentConfigurationsHandler.getServerProperty(GetListPerfConstants.PROP_TIMING_LOG))
                    .thenReturn(value);
            return GetListPerfConfig.isTimingLogEnabled();
        }
    }
}
