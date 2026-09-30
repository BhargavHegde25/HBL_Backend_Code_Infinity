package com.hbl.infinity.accounts.perf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;

public class GetListSnapshotCacheTest {

    private static final String CUSTOMER = "1000000001";

    private GetListCacheTestSupport.InMemoryBackend backend;

    @Before
    public void installCache() {
        backend = GetListCacheTestSupport.install();
    }

    @After
    public void uninstallCache() {
        GetListCacheTestSupport.uninstall();
    }

    // ---------------------------------------------------------------- switch (F1)

    @Test
    public void switchOffOrMissingOrInvalidGivesNoSession() {
        assertNull(openWithSwitch(null));
        assertNull(openWithSwitch("false"));
        assertNull(openWithSwitch("yes"));
        assertNotNull(openWithSwitch("true"));
    }

    @Test
    public void blankCustomerGivesNoSession() {
        assertNull(open(""));
        assertNull(open(null));
    }

    // ---------------------------------------------------------------- versions (F2, F3, F4)

    @Test
    public void missingVersionsAreCreatedOnFirstUse() {
        GetListSnapshotCache.Session session = open(CUSTOMER, "a");
        assertNotNull(session);
        assertTrue(backend.store.containsKey(GetListPerfConstants.KEY_GLOBAL_VERSION));
        assertTrue(backend.store.containsKey(GetListPerfConstants.KEY_CUSTOMER_VERSION + CUSTOMER));
        assertEquals(session.key(), open(CUSTOMER, "a").key());
    }

    @Test
    public void unreadableCacheGivesNoSession() {
        backend.failing = true;
        assertNull(open(CUSTOMER, "a"));
    }

    @Test
    public void customerBumpMakesStoredSnapshotUnreachable() {
        GetListSnapshotCache.Session before = open(CUSTOMER, "a");
        before.store(new T24PreSnapshot("100100", " "));
        assertNotNull(open(CUSTOMER, "a").read(T24PreSnapshot.class));

        PermissionVersionService.get().bump(CUSTOMER);

        GetListSnapshotCache.Session after = open(CUSTOMER, "a");
        assertNotEquals(before.key(), after.key());
        assertNull(after.read(T24PreSnapshot.class));
    }

    @Test
    public void globalBumpMakesEverySnapshotUnreachable() {
        open(CUSTOMER, "a").store(new T24PreSnapshot("100100", " "));
        open("2000000002", "a").store(new T24PreSnapshot("200100", " "));

        PermissionVersionService.get().bumpGlobal();

        assertNull(open(CUSTOMER, "a").read(T24PreSnapshot.class));
        assertNull(open("2000000002", "a").read(T24PreSnapshot.class));
    }

    @Test
    public void otherCustomersKeepTheirSnapshotsAfterABump() {
        open("2000000002", "a").store(new T24PreSnapshot("200100", " "));
        PermissionVersionService.get().bump(CUSTOMER);
        assertNotNull(open("2000000002", "a").read(T24PreSnapshot.class));
    }

    @Test
    public void bumpNeverThrowsWhenTheCacheIsDown() {
        backend.failing = true;
        PermissionVersionService.get().bump(CUSTOMER);
        PermissionVersionService.get().bumpGlobal();
        PermissionVersionService.get().bump("");
    }

    // ---------------------------------------------------------------- fingerprint

    @Test
    public void differentInputsGiveDifferentSlots() {
        assertNotEquals(open(CUSTOMER, "a").key(), open(CUSTOMER, "b").key());
        assertNotEquals(open(CUSTOMER, "a", "b").key(), open(CUSTOMER, "b", "a").key());
        assertNotEquals(open(CUSTOMER, "ab", "").key(), open(CUSTOMER, "a", "b").key());
        assertNotEquals(open(CUSTOMER, (String) null).key(), open(CUSTOMER, "").key());
        assertNotEquals(open(CUSTOMER, "a").key(), open("2000000002", "a").key());
    }

    @Test
    public void stagesDoNotShareSlots() {
        GetListSnapshotCache.Session pre = openStage(GetListPerfConstants.STAGE_T24_PRE, "a");
        GetListSnapshotCache.Session post = openStage(GetListPerfConstants.STAGE_T24_POST, "a");
        assertNotEquals(pre.key(), post.key());
    }

    // ---------------------------------------------------------------- snapshots (F5, F14)

    @Test
    public void preSnapshotRoundTrip() {
        open(CUSTOMER, "a").store(new T24PreSnapshot("100100%20100200", " 100200 "));
        T24PreSnapshot read = open(CUSTOMER, "a").read(T24PreSnapshot.class);
        assertEquals("100100%20100200", read.getCoreCustomerIdList());
        assertEquals(" 100200 ", read.getExplicitCoreCustomerIdList());
    }

    @Test
    public void postSnapshotRoundTrip() {
        open(CUSTOMER, "a").store(new T24PostSnapshot("100001 100002", "100001"));
        T24PostSnapshot read = open(CUSTOMER, "a").read(T24PostSnapshot.class);
        assertEquals("100001 100002", read.getAccounts());
        assertEquals("100001", read.getDefaultAccountId());
    }

    @Test
    public void objectSnapshotRoundTripKeepsNullsAndGivesCopies() {
        Map<String, String> actions = new HashMap<>();
        actions.put("100001", "[\"TRANSFER\",\"VIEW\"]");
        Map<String, String> details = new HashMap<>();
        details.put("coreCustomerId", "100100");
        details.put("email", null);
        details.put("nickName", "Salary \"main\" <&>");
        Map<String, Map<String, String>> accountDetails = new HashMap<>();
        accountDetails.put("100001", details);

        open(CUSTOMER, "a").store(new ObjectPostSnapshot(actions, accountDetails));
        ObjectPostSnapshot read = open(CUSTOMER, "a").read(ObjectPostSnapshot.class);

        assertEquals(actions, read.getActionsByAccount());
        assertEquals(accountDetails, read.getAccountDetails());
        assertTrue(read.getAccountDetails().get("100001").containsKey("email"));

        read.getActionsByAccount().put("x", "y");
        read.getAccountDetails().get("100001").put("nickName", "changed");
        assertEquals(actions, read.getActionsByAccount());
        assertEquals("Salary \"main\" <&>", read.getAccountDetails().get("100001").get("nickName"));
    }

    @Test
    public void incompleteSnapshotsAreNotStored() {
        int before = backend.store.size();
        GetListSnapshotCache.Session session = open(CUSTOMER, "a");
        int afterOpen = backend.store.size();
        session.store(new T24PreSnapshot("", " "));
        session.store(new T24PostSnapshot("100001", ""));
        session.store(new T24PostSnapshot("", "100001"));
        session.store(new ObjectPostSnapshot(null, new HashMap<>()));
        session.store(null);
        assertTrue(afterOpen >= before);
        assertEquals(afterOpen, backend.store.size());
    }

    @Test
    public void corruptOrForeignValuesAreTreatedAsMisses() {
        GetListSnapshotCache.Session session = open(CUSTOMER, "a");
        backend.store.put(session.key(), "{not json");
        assertNull(session.read(T24PreSnapshot.class));
        backend.store.put(session.key(), "[1,2]");
        assertNull(session.read(T24PreSnapshot.class));
        backend.store.put(session.key(), "{\"schemaVersion\":99,\"coreCustomerIdList\":\"1\","
                + "\"explicitCoreCustomerIdList\":\" \"}");
        assertNull(session.read(T24PreSnapshot.class));
        backend.store.put(session.key(), "{\"schemaVersion\":1,\"coreCustomerIdList\":\"\"}");
        assertNull(session.read(T24PreSnapshot.class));
    }

    @Test
    public void readAndStoreNeverThrowWhenTheCacheFails() {
        GetListSnapshotCache.Session session = open(CUSTOMER, "a");
        backend.failing = true;
        assertNull(session.read(T24PreSnapshot.class));
        session.store(new T24PreSnapshot("100100", " "));
    }

    // ---------------------------------------------------------------- balance cache

    @Test
    public void balanceCacheHasItsOwnSwitch() {
        assertNull(openBalancesWith(null, null));
        assertNull(openBalancesWith("true", null));
        assertNull(openBalancesWith("true", "false"));
        assertNotNull(openBalancesWith(null, "true"));
        assertNotNull(openBalancesWith("false", "true"));
    }

    @Test
    public void balanceEntriesAreClearedByTheSameBumps() {
        GetListSnapshotCache.Session before = openBalancesWith(null, "true");
        before.store(new T24AccountsSnapshot("{\"Accounts\":[]}"));
        assertNotNull(openBalancesWith(null, "true").read(T24AccountsSnapshot.class));
        PermissionVersionService.get().bump(CUSTOMER);
        assertNull(openBalancesWith(null, "true").read(T24AccountsSnapshot.class));
    }

    @Test
    public void balanceSlotIsSeparateFromSnapshotSlots() {
        GetListSnapshotCache.Session balances = openBalancesWith("true", "true");
        assertNotEquals(open(CUSTOMER, "a").key(), balances.key());
    }

    // ---------------------------------------------------------------- helpers

    private static GetListSnapshotCache.Session openBalancesWith(String cacheSwitch, String balanceSwitch) {
        try (MockedStatic<EnvironmentConfigurationsHandler> env = mockStatic(EnvironmentConfigurationsHandler.class)) {
            env.when(() -> EnvironmentConfigurationsHandler.getServerProperty(anyString())).thenAnswer(i -> {
                String key = i.getArgument(0);
                if (GetListPerfConstants.PROP_CACHE_ENABLED.equals(key)) {
                    return cacheSwitch;
                }
                return GetListPerfConstants.PROP_BAL_CACHE_ENABLED.equals(key) ? balanceSwitch : null;
            });
            return GetListSnapshotCache.openBalances(CUSTOMER, "a");
        }
    }

    private GetListSnapshotCache.Session open(String customer, String... parts) {
        return withSwitchOn(() -> GetListSnapshotCache.open(GetListPerfConstants.STAGE_T24_PRE, customer, parts));
    }

    private GetListSnapshotCache.Session openStage(String stage, String... parts) {
        return withSwitchOn(() -> GetListSnapshotCache.open(stage, CUSTOMER, parts));
    }

    private static GetListSnapshotCache.Session openWithSwitch(String value) {
        try (MockedStatic<EnvironmentConfigurationsHandler> env = mockStatic(EnvironmentConfigurationsHandler.class)) {
            env.when(() -> EnvironmentConfigurationsHandler.getServerProperty(anyString()))
                    .thenAnswer(i -> GetListPerfConstants.PROP_CACHE_ENABLED.equals(i.getArgument(0)) ? value : null);
            return GetListSnapshotCache.open(GetListPerfConstants.STAGE_T24_PRE, CUSTOMER, "a");
        }
    }

    private static GetListSnapshotCache.Session withSwitchOn(
            java.util.function.Supplier<GetListSnapshotCache.Session> action) {
        try (MockedStatic<EnvironmentConfigurationsHandler> env = mockStatic(EnvironmentConfigurationsHandler.class)) {
            env.when(() -> EnvironmentConfigurationsHandler.getServerProperty(anyString()))
                    .thenAnswer(i -> GetListPerfConstants.PROP_CACHE_ENABLED.equals(i.getArgument(0)) ? "true" : null);
            return action.get();
        }
    }
}
