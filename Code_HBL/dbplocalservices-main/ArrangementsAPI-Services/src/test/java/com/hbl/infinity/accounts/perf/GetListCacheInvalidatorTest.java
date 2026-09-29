package com.hbl.infinity.accounts.perf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import java.util.function.Supplier;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

import com.hbl.infinity.accounts.perf.invalidation.GetListCacheInvalidator;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;

/**
 * The writers in other modules bump versions through {@link GetListCacheInvalidator} (which writes with
 * MemoryManager directly); getList reads them through {@link PermissionVersionService}. These tests check that the
 * two agree on keys, so a bump really makes the stored snapshots unreachable.
 */
public class GetListCacheInvalidatorTest {

    private static final String CUSTOMER = "1000000001";
    private static final String OTHER = "2000000002";

    private GetListCacheTestSupport.InMemoryBackend backend;
    private MockedStatic<MemoryManager> memoryManager;
    private boolean memoryManagerFails;

    @Before
    public void installCache() {
        backend = GetListCacheTestSupport.install();
        memoryManager = mockStatic(MemoryManager.class);
        memoryManager.when(() -> MemoryManager.saveIntoCache(anyString(), anyString(), anyInt())).thenAnswer(i -> {
            if (memoryManagerFails) {
                throw new IllegalStateException("cache down");
            }
            backend.store.put(i.getArgument(0), i.getArgument(1));
            return null;
        });
    }

    @After
    public void uninstallCache() {
        memoryManager.close();
        GetListCacheTestSupport.uninstall();
    }

    @Test
    public void keysMatchTheSnapshotCache() {
        assertEquals(GetListPerfConstants.KEY_GLOBAL_VERSION, GetListCacheInvalidator.KEY_GLOBAL_VERSION);
        assertEquals(GetListPerfConstants.KEY_CUSTOMER_VERSION, GetListCacheInvalidator.KEY_CUSTOMER_VERSION);
    }

    @Test
    public void globalKeyMatchesTheSpotlightCopy() {
        // adminconsoleservices-main has its own com.hbl.adminconsole.getlistcache.GetListCacheInvalidator (Spotlight
        // does not depend on this module) with this literal; change both together.
        assertEquals("HBLGL:v1:GLOBALVER", GetListCacheInvalidator.KEY_GLOBAL_VERSION);
        assertEquals("HBL_GETLIST_PERMVER_TTL_SECONDS", GetListCacheInvalidator.PROP_PERMVER_TTL_SECONDS);
    }

    @Test
    public void customerChangedHidesOnlyThatCustomersSnapshots() {
        storeFor(CUSTOMER);
        storeFor(OTHER);
        GetListCacheInvalidator.customerChanged(CUSTOMER);
        assertNull(readFor(CUSTOMER));
        assertNotNull(readFor(OTHER));
    }

    @Test
    public void permissionsChangedHidesEverySnapshot() {
        storeFor(CUSTOMER);
        storeFor(OTHER);
        GetListCacheInvalidator.permissionsChanged();
        assertNull(readFor(CUSTOMER));
        assertNull(readFor(OTHER));
    }

    @Test
    public void sessionCustomerChangedUsesTheSessionCustomer() {
        storeFor(CUSTOMER);
        storeFor(OTHER);
        try (MockedStatic<HelperMethods> helper = mockStatic(HelperMethods.class)) {
            helper.when(() -> HelperMethods.getCustomerIdFromSession(any(DataControllerRequest.class)))
                    .thenReturn(CUSTOMER);
            GetListCacheInvalidator.sessionCustomerChanged(mock(DataControllerRequest.class));
        }
        assertNull(readFor(CUSTOMER));
        assertNotNull(readFor(OTHER));
    }

    @Test
    public void sessionCustomerChangedWithoutSessionHidesEverySnapshot() {
        storeFor(CUSTOMER);
        storeFor(OTHER);
        try (MockedStatic<HelperMethods> helper = mockStatic(HelperMethods.class)) {
            helper.when(() -> HelperMethods.getCustomerIdFromSession(any(DataControllerRequest.class)))
                    .thenThrow(new IllegalStateException("no session"));
            GetListCacheInvalidator.sessionCustomerChanged(mock(DataControllerRequest.class));
        }
        assertNull(readFor(CUSTOMER));
        assertNull(readFor(OTHER));
    }

    @Test
    public void blankCustomerChangesNothing() {
        storeFor(CUSTOMER);
        GetListCacheInvalidator.customerChanged(" ");
        GetListCacheInvalidator.customerChanged(null);
        assertNotNull(readFor(CUSTOMER));
    }

    @Test
    public void neverThrowsWhenTheCacheIsDown() {
        memoryManagerFails = true;
        GetListCacheInvalidator.customerChanged(CUSTOMER);
        GetListCacheInvalidator.permissionsChanged();
        GetListCacheInvalidator.sessionCustomerChanged(null);
    }

    @Test
    public void tokensAreUnique() {
        assertNotEquals(GetListCacheInvalidator.newToken(), GetListCacheInvalidator.newToken());
    }

    private void storeFor(String customer) {
        withSwitchOn(() -> GetListSnapshotCache.open(GetListPerfConstants.STAGE_T24_PRE, customer, "a"))
                .store(new T24PreSnapshot("100100", " "));
    }

    private T24PreSnapshot readFor(String customer) {
        return withSwitchOn(() -> GetListSnapshotCache.open(GetListPerfConstants.STAGE_T24_PRE, customer, "a"))
                .read(T24PreSnapshot.class);
    }

    private static GetListSnapshotCache.Session withSwitchOn(Supplier<GetListSnapshotCache.Session> action) {
        try (MockedStatic<EnvironmentConfigurationsHandler> env = mockStatic(EnvironmentConfigurationsHandler.class)) {
            env.when(() -> EnvironmentConfigurationsHandler.getServerProperty(anyString()))
                    .thenAnswer(i -> GetListPerfConstants.PROP_CACHE_ENABLED.equals(i.getArgument(0)) ? "true" : null);
            return action.get();
        }
    }
}
