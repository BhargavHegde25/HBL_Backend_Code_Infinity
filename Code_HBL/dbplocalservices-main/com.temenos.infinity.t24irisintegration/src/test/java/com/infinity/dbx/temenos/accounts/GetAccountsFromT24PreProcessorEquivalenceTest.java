package com.infinity.dbx.temenos.accounts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Test;
import org.mockito.MockedStatic;

import com.hbl.infinity.accounts.perf.GetListCacheTestSupport;
import com.hbl.infinity.accounts.perf.GetListPerfConstants;
import com.hbl.infinity.accounts.perf.PermissionVersionService;
import com.infinity.dbx.dbp.jwt.auth.Authentication;
import com.infinity.dbx.temenos.accounts.legacy.LegacyGetAccountsFromT24PreProcessor;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.TokenUtils;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.processor.IdentityHandler;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

/**
 * Runs the pre-optimisation copy ({@link LegacyGetAccountsFromT24PreProcessor}) and the current
 * {@link getAccountsFromT24PreProcessor} on the same requests and checks that the return value, the params sent to
 * T24, the request parameters (including Authorization) and the DB calls are identical, while the current version
 * signs one JWT per request instead of two.
 * <p>
 * The mocked token generator returns a value derived from its inputs (flow type and issuer), like the claims of a
 * real JWT, so "same Authorization" means "same token claims".
 */
public class GetAccountsFromT24PreProcessorEquivalenceTest {

    private static final String LOGIN_USER = "1000000001";

    @Test
    public void contractCustomersLookup() throws Exception {
        Scenario s = new Scenario();
        assertSameWithFewerTokens(s);
    }

    @Test
    public void contractCustomersLookupWithAutoSyncFalse() throws Exception {
        Scenario s = new Scenario();
        s.contractCustomers = "{\"contractcustomers\":[{\"coreCustomerId\":\"100100\",\"autoSyncAccounts\":\"true\"},"
                + "{\"coreCustomerId\":\"100200\",\"autoSyncAccounts\":\"false\"}]}";
        assertSameWithFewerTokens(s);
    }

    @Test
    public void noContractCustomersReturnsFalse() throws Exception {
        Scenario s = new Scenario();
        s.contractCustomers = "{\"contractcustomers\":[]}";
        assertSameWithFewerTokens(s);
    }

    @Test
    public void membershipIdGiven() throws Exception {
        Scenario s = new Scenario();
        s.requestParams.put("Membership_id", "100100");
        assertSameWithFewerTokens(s);
    }

    @Test
    public void coreCustomerIdListGiven() throws Exception {
        Scenario s = new Scenario();
        s.requestParams.put("coreCustomerIdList", "100100 100200");
        assertSameWithFewerTokens(s);
    }

    @Test
    public void preLoginUser() throws Exception {
        Scenario s = new Scenario();
        s.requestParams.put("loginUserId", "PreLogin-2000000002");
        assertSameWithFewerTokens(s);
    }

    @Test
    public void blankCompanyIdFallsBackToBranch() throws Exception {
        Scenario s = new Scenario();
        s.companyId = "";
        assertSameWithFewerTokens(s);
    }

    @Test
    public void tokenGenerationFailsTheSameWay() throws Exception {
        Scenario s = new Scenario();
        s.tokenFails = true;
        Outcome legacy = run(s, true);
        Outcome current = run(s, false);
        assertEquals(legacy.toString(), current.toString());
    }

    // ---------------------------------------------------------------- getList cache (switch on)

    @After
    public void uninstallCache() {
        GetListCacheTestSupport.uninstall();
    }

    @Test
    public void cacheColdThenWarmGivesTheSameRequestWithoutTheDbCall() throws Exception {
        GetListCacheTestSupport.install();
        Scenario s = new Scenario();
        s.contractCustomers = "{\"contractcustomers\":[{\"coreCustomerId\":\"100100\",\"autoSyncAccounts\":\"true\"},"
                + "{\"coreCustomerId\":\"100200\",\"autoSyncAccounts\":\"false\"}]}";
        s.cacheOn = true;
        Outcome legacy = run(s, true);
        Outcome cold = run(s, false);
        Outcome warm = run(s, false);
        assertEquals(legacy.toString(), cold.toString());
        assertEquals(legacy.withoutCalls(), warm.withoutCalls());
        assertTrue("warm call must not read contract customers: " + warm.calls, warm.calls.isEmpty());
        assertEquals("current signs one JWT", 1, warm.tokensGenerated);
    }

    @Test
    public void cacheDoesNotStoreAnEmptyLookup() throws Exception {
        GetListCacheTestSupport.install();
        Scenario s = new Scenario();
        s.contractCustomers = "{\"contractcustomers\":[]}";
        s.cacheOn = true;
        assertNeverCached(s);
    }

    @Test
    public void cacheKeepsCompaniesApart() throws Exception {
        GetListCacheTestSupport.install();
        Scenario s = new Scenario();
        s.cacheOn = true;
        run(s, false);
        s.companyId = "NP0010002";
        s.contractCustomers = "{\"contractcustomers\":[{\"coreCustomerId\":\"300100\"}]}";
        Outcome legacy = run(s, true);
        assertEquals(legacy.toString(), run(s, false).toString());
    }

    @Test
    public void cacheIsNotUsedWhenCoreCustomersAreGiven() throws Exception {
        GetListCacheTestSupport.install();
        Scenario s = new Scenario();
        s.cacheOn = true;
        s.requestParams.put("coreCustomerIdList", "100100 100200");
        assertNeverCached(s);
    }

    @Test
    public void cacheDownRunsTodaysCode() throws Exception {
        GetListCacheTestSupport.install().failing = true;
        Scenario s = new Scenario();
        s.cacheOn = true;
        assertNeverCached(s);
    }

    @Test
    public void versionBumpMakesTheNextCallReadTheDatabase() throws Exception {
        GetListCacheTestSupport.install();
        Scenario s = new Scenario();
        s.cacheOn = true;
        run(s, false);
        PermissionVersionService.get().bump(LOGIN_USER);
        Outcome legacy = run(s, true);
        Outcome current = run(s, false);
        assertEquals(legacy.toString(), current.toString());
    }

    /** Runs today's code twice with the switch on: both runs must equal legacy, calls included. */
    private static void assertNeverCached(Scenario s) throws Exception {
        Outcome legacy = run(s, true);
        assertEquals(legacy.toString(), run(s, false).toString());
        assertEquals(legacy.toString(), run(s, false).toString());
    }

    // ---------------------------------------------------------------- harness

    private static final class Scenario {
        Map<String, String> requestParams = new HashMap<>();
        String companyId = "NP0010001";
        String contractCustomers = "{\"contractcustomers\":[{\"coreCustomerId\":\"100100\"},{\"coreCustomerId\":\"100200\"}]}";
        boolean tokenFails;
        /** Switch HBL_GETLIST_CACHE_ENABLED on. */
        boolean cacheOn;

        Scenario() {
            requestParams.put("current_appID", "ArrangementT24ISAccounts");
        }
    }

    private static final class Outcome {
        String returned;
        String exception;
        Map<String, Object> params;
        Map<String, String> requestParams;
        List<String> calls = new ArrayList<>();
        String result;
        int tokensGenerated;

        @Override
        public String toString() {
            return "returned=" + returned + "\nexception=" + exception + "\nparams=" + params + "\nrequest="
                    + requestParams + "\ncalls=" + calls + "\nresult=" + result;
        }

        /** Everything except the DB calls, for comparing a cached run with a database run. */
        String withoutCalls() {
            return "returned=" + returned + "\nexception=" + exception + "\nparams=" + params + "\nrequest="
                    + requestParams + "\nresult=" + result;
        }
    }

    private static void assertSameWithFewerTokens(Scenario s) throws Exception {
        Outcome legacy = run(s, true);
        Outcome current = run(s, false);
        assertEquals(legacy.toString(), current.toString());
        assertEquals("legacy signs two JWTs", 2, legacy.tokensGenerated);
        assertEquals("current signs one JWT", 1, current.tokensGenerated);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static Outcome run(Scenario s, boolean legacy) throws Exception {
        Outcome out = new Outcome();
        Map<String, String> requestParams = new HashMap<>(s.requestParams);
        DataControllerRequest request = mockRequest(requestParams);
        HashMap params = new HashMap();
        Result result = new Result();
        AtomicInteger tokens = new AtomicInteger();

        try (MockedStatic<CommonUtils> cu = mockStatic(CommonUtils.class);
                MockedStatic<TokenUtils> tu = mockStatic(TokenUtils.class);
                MockedStatic<LegalEntityUtil> le = mockStatic(LegalEntityUtil.class);
                MockedStatic<HelperMethods> hm = mockStatic(HelperMethods.class);
                MockedStatic<com.temenos.infinity.api.arrangements.utils.CommonUtils> acu = mockStatic(
                        com.temenos.infinity.api.arrangements.utils.CommonUtils.class);
                MockedStatic<EnvironmentConfigurationsHandler> env = mockStatic(
                        EnvironmentConfigurationsHandler.class);
                MockedStatic<Authentication> auth = mockStatic(Authentication.class);
                MockedStatic<ResultToJSON> rtj = mockStatic(ResultToJSON.class)) {

            // Fabric's ResultToJSON needs server-only classes; the legacy code only uses it for log strings.
            rtj.when(() -> ResultToJSON.convert(any(Result.class)))
                    .thenAnswer(i -> ResultCanonical.of(i.getArgument(0)));

            cu.when(() -> CommonUtils.getBackendIdFromCache(any(DataControllerRequest.class), anyString(),
                    anyString(), anyString())).thenReturn("100100");
            tu.when(() -> TokenUtils.getT24AuthToken(any(DataControllerRequest.class))).thenAnswer(i -> {
                tokens.incrementAndGet();
                if (s.tokenFails) {
                    return "";
                }
                return "JWT[" + requestParams.get(TemenosConstants.FLOW_TYPE) + "|" + requestParams.get("issuer")
                        + "]";
            });
            le.when(() -> LegalEntityUtil.getLegalEntityIdFromSessionOrCache(any(DataControllerRequest.class)))
                    .thenReturn("NP0010001");
            hm.when(() -> HelperMethods.getCustomerIdFromSession(any(DataControllerRequest.class)))
                    .thenReturn(LOGIN_USER);
            acu.when(() -> com.temenos.infinity.api.arrangements.utils.CommonUtils
                    .getCompanyId(any(DataControllerRequest.class))).thenReturn(s.companyId);
            env.when(() -> EnvironmentConfigurationsHandler.getServerProperty(anyString()))
                    .thenAnswer(i -> "BRANCH_ID_REFERENCE".equals(i.getArgument(0)) ? "NP0010099"
                            : GetListPerfConstants.PROP_CACHE_ENABLED.equals(i.getArgument(0)) && s.cacheOn ? "true"
                                    : null);
            auth.when(Authentication::getInstance).thenReturn(null);
            cu.when(() -> CommonUtils.callIntegrationService(any(), anyMap(), any(), anyString(), anyString(),
                    anyBoolean())).thenAnswer(i -> {
                        out.calls.add(i.getArgument(3) + "." + i.getArgument(4)
                                + new TreeMap<>((Map<String, Object>) i.getArgument(1)));
                        return JSONToResult.convert(s.contractCustomers);
                    });

            try {
                boolean returned = legacy
                        ? new LegacyGetAccountsFromT24PreProcessor().execute(params, request, null, result)
                        : new getAccountsFromT24PreProcessor().execute(params, request, null, result);
                out.returned = String.valueOf(returned);
            } catch (Exception e) {
                out.exception = e.toString();
            }
        }
        out.params = new TreeMap<>(params);
        out.requestParams = new TreeMap<>(requestParams);
        out.result = ResultCanonical.of(result);
        out.tokensGenerated = tokens.get();
        return out;
    }

    private static DataControllerRequest mockRequest(Map<String, String> params) throws Exception {
        DataControllerRequest request = mock(DataControllerRequest.class);
        when(request.getParameter(anyString())).thenAnswer(i -> params.get(i.getArgument(0)));
        when(request.containsKeyInRequest(anyString())).thenAnswer(i -> params.containsKey(i.getArgument(0)));
        doAnswer(i -> {
            params.put(i.getArgument(0), i.getArgument(1));
            return null;
        }).when(request).addRequestParam_(anyString(), any());
        Map<String, Object> headers = new HashMap<>();
        when(request.getHeaderMap()).thenReturn(headers);
        IdentityHandler identityHandler = mock(IdentityHandler.class);
        when(identityHandler.getUserAttributes()).thenReturn(new HashMap<>());
        ServicesManager servicesManager = mock(ServicesManager.class);
        when(servicesManager.getIdentityHandler()).thenReturn(identityHandler);
        when(request.getServicesManager()).thenReturn(servicesManager);
        return request;
    }
}
