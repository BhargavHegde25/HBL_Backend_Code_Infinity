package com.infinity.dbx.temenos.accounts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import com.dbp.core.fabric.extn.DBPServiceExecutor;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hbl.infinity.accounts.perf.BundleConfigCache;
import com.infinity.dbx.temenos.accounts.legacy.LegacyGetAccountsFromT24PostProcessor;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbputilities.customersecurityservices.createOrgEmployeeAccounts;
import com.kony.dbputilities.util.BundleConfigurationHandler;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.processor.IdentityHandler;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

/**
 * Runs the pre-optimisation copy ({@link LegacyGetAccountsFromT24PostProcessor}) and the current
 * {@link getAccountsFromT24PostProcessor} on the same synthetic T24 responses, with the same mocked Fabric and DB
 * calls, and checks that everything observable is identical: the returned Result, the request parameters written,
 * the session write, and every downstream call with its inputs.
 */
public class GetAccountsFromT24PostProcessorEquivalenceTest {

    private static final String LOGIN_USER = "1000000001";
    private static final String CORE_A = "100100";
    private static final String CORE_B = "100200";

    // ---------------------------------------------------------------- scenarios

    @Test
    public void retailUserWithDefaultAccount() throws Exception {
        assertSame(baseScenario(), 4);
    }

    @Test
    public void multiCoreCustomerWithNewAccounts() throws Exception {
        Scenario s = baseScenario();
        s.t24 = t24Response(
                product("200001", "SAV01", "1001", "NPR", CORE_A, "Owner"),
                product("200002", "CUR01", "1002", "USD", CORE_B, "Owner"),
                product("200003", "SAV01", "1001", "NPR", CORE_B, "Owner"));
        s.napAccounts = "200001,200002,200003";
        s.napNewAccounts = CORE_B + ":200002|" + CORE_B + ":200003";
        s.defaultActions.put(CORE_B, "TRANSFER_VIEW,ACCOUNT_VIEW,BILL_PAY");
        assertSame(s, 3);
    }

    @Test
    public void noDefaultAccountSetsFirstSavingsAsDefault() throws Exception {
        Scenario s = baseScenario();
        s.defaultAccountResponse = "{\"customeraccounts\":[]}";
        assertSame(s, 4);
    }

    @Test
    public void actionsFalseSkipsDefaultActions() throws Exception {
        Scenario s = baseScenario();
        s.napNewAccounts = CORE_A + ":100002";
        s.requestParams.put("actions", "false");
        assertSame(s, 4);
    }

    @Test
    public void membershipIdSkipsSessionWrite() throws Exception {
        Scenario s = baseScenario();
        s.requestParams.put("Membership_id", CORE_A);
        assertSame(s, 4);
    }

    @Test
    public void jointAccountIsRemovedFromResponse() throws Exception {
        Scenario s = baseScenario();
        JsonObject joint = product("100009", "SAV01", "1001", "NPR", CORE_A, "Owner");
        JsonObject secondHolder = new JsonObject();
        secondHolder.addProperty("customerId", "999999");
        secondHolder.addProperty("roleDisplayName", "Joint Holder");
        secondHolder.addProperty("customerRole", "JOINT");
        joint.getAsJsonArray("customerDetails").add(secondHolder);
        s.t24 = t24Response(product("100001", "SAV01", "1001", "NPR", CORE_A, "Owner"), joint);
        s.napAccounts = "100001,100009";
        assertSame(s, 1);
    }

    @Test
    public void noAccessProductIsRemoved() throws Exception {
        Scenario s = baseScenario();
        s.bundle.put("ACCOUNTS_TYPES_NOACCESS", "{\"CUR01\":\"x\"}");
        assertSame(s, 3);
    }

    @Test
    public void missingNoAccessConfigKeepsAllAccounts() throws Exception {
        Scenario s = baseScenario();
        s.bundle.remove("ACCOUNTS_TYPES_NOACCESS");
        assertSame(s, 4);
    }

    @Test
    public void invalidNoAccessConfigKeepsAllAccounts() throws Exception {
        Scenario s = baseScenario();
        s.bundle.put("ACCOUNTS_TYPES_NOACCESS", "not json");
        assertSame(s, 4);
    }

    @Test
    public void invalidTransferConfigGivesNoTransferFlags() throws Exception {
        Scenario s = baseScenario();
        s.bundle.put("TRANSFER_SUPPORTED_ACCOUNTS", "not json");
        assertSame(s, 4);
    }

    @Test
    public void missingTransferConfigFailsTheSameWay() throws Exception {
        Scenario s = baseScenario();
        s.bundle.remove("TRANSFER_SUPPORTED_ACCOUNTS");
        assertSame(s, -1);
    }

    @Test
    public void defaultActionsFailureIsHandledTheSameWay() throws Exception {
        Scenario s = baseScenario();
        s.napNewAccounts = CORE_A + ":100002";
        s.defaultActionsFail = true;
        assertSame(s, 4);
    }

    @Test
    public void newAccountProcessingReturnsNoAccounts() throws Exception {
        Scenario s = baseScenario();
        s.napAccounts = "";
        assertSame(s, 0);
    }

    @Test
    public void emptyT24Response() throws Exception {
        Scenario s = baseScenario();
        s.t24 = "{\"opstatus\":0,\"httpStatusCode\":200}";
        assertSame(s, 0);
    }

    @Test
    public void preLoginFlowWithoutLoginUser() throws Exception {
        Scenario s = baseScenario();
        s.requestParams.remove("loginUserId");
        s.requestParams.put("loginUserId", "");
        assertSame(s, 4);
    }

    // ---------------------------------------------------------------- bundle config cache

    @Test
    public void warmBundleCacheGivesSameResultWithoutReloading() throws Exception {
        assertSameWithWarmBundleCache(baseScenario());
    }

    @Test
    public void warmBundleCacheKeepsNoAccessFiltering() throws Exception {
        Scenario s = baseScenario();
        s.bundle.put("ACCOUNTS_TYPES_NOACCESS", "{\"CUR01\":\"x\"}");
        assertSameWithWarmBundleCache(s);
    }

    @Test
    public void failedBundleLoadIsNotCached() throws Exception {
        Scenario s = baseScenario();
        // The handler returns an empty map when the Admin call fails.
        s.bundle.clear();
        Outcome legacy = run(s, (r, req) -> new LegacyGetAccountsFromT24PostProcessor().execute(r, req, null));
        Outcome first = run(s, (r, req) -> new getAccountsFromT24PostProcessor().execute(r, req, null));
        Outcome second = run(s, (r, req) -> new getAccountsFromT24PostProcessor().execute(r, req, null), true);
        assertEquals(legacy.toString(), first.toString());
        assertEquals(legacy.toString(), second.toString());
        assertEquals("a failed load must be retried on the next request", 1, second.bundleLoads);
    }

    // ---------------------------------------------------------------- fixtures

    private static final class Scenario {
        String t24;
        String napAccounts;
        String napNewAccounts = "";
        Map<String, String> defaultActions = new HashMap<>();
        boolean defaultActionsFail;
        String defaultAccountResponse;
        Map<String, String> bundle = new HashMap<>();
        Map<String, String> accountTypes = new HashMap<>();
        Map<String, String> requestParams = new HashMap<>();
    }

    private static Scenario baseScenario() {
        Scenario s = new Scenario();
        s.t24 = t24Response(
                product("100001", "SAV01", "1001", "NPR", CORE_A, "Owner"),
                product("100002", "CUR01", "1002", "NPR", CORE_A, "Owner"),
                product("100003", "FD01", "6001", "NPR", CORE_A, "Owner"),
                product("100004", "LN01", "3001", "USD", CORE_A, "Owner"));
        s.napAccounts = "100001,100002,100003,100004";
        s.defaultActions.put(CORE_A, "ACCOUNT_VIEW,TRANSFER_VIEW");
        s.defaultAccountResponse = "{\"customeraccounts\":[{\"Account_id\":\"100002\"}]}";
        s.bundle.put("TRANSFER_SUPPORTED_ACCOUNTS", "{\"1001\":\"Dr,Cr\",\"1002\":\"Dr\",\"6001\":\"\"}");
        s.bundle.put("ACCOUNTS_TYPES_NOACCESS", "{\"XX99\":\"x\"}");
        s.accountTypes.put("SAV01", "Savings");
        s.accountTypes.put("CUR01", "Checking");
        s.accountTypes.put("FD01", "Deposit");
        s.accountTypes.put("LN01", "Loan");
        s.requestParams.put("loginUserId", LOGIN_USER);
        s.requestParams.put("explicitCoreCustomerIdList", " ");
        return s;
    }

    private static JsonObject product(String accountId, String productId, String categoryId, String currency,
            String coreCustomerId, String role) {
        JsonObject p = new JsonObject();
        p.addProperty("accountId", accountId);
        p.addProperty("productId", productId);
        p.addProperty("categoryId", categoryId);
        p.addProperty("currencyCode", currency);
        p.addProperty("accountType", productId);
        p.addProperty("accountName", "Account " + accountId);
        p.addProperty("arrangementId", "AA" + accountId);
        p.addProperty("accountHolder", "Test Holder");
        p.addProperty("accountIBAN", "NP00" + accountId);
        p.addProperty("availableFunds", "1500.25");
        p.addProperty("principalBalance", "9000.00");
        p.addProperty("productDescription", "Product " + productId);
        p.addProperty("bankName", "HBL");
        p.addProperty("statement", accountId.endsWith("1") ? "ESTATEMENT" : "PAPER");
        if (accountId.endsWith("3")) {
            p.addProperty("customerReference", "My deposit");
        }
        if (accountId.endsWith("4")) {
            p.addProperty("portfolioId", "PF1");
        }
        JsonArray details = new JsonArray();
        JsonObject d = new JsonObject();
        d.addProperty("customerId", coreCustomerId);
        d.addProperty("roleDisplayName", role);
        d.addProperty("customerRole", "OWNER");
        details.add(d);
        p.add("customerDetails", details);
        return p;
    }

    private static String t24Response(JsonObject... products) {
        JsonArray productArray = new JsonArray();
        for (JsonObject p : products) {
            productArray.add(p);
        }
        JsonObject holder = new JsonObject();
        holder.add("products", productArray);
        JsonArray accounts = new JsonArray();
        accounts.add(holder);
        JsonObject root = new JsonObject();
        root.add("Accounts", accounts);
        root.addProperty("opstatus", 0);
        root.addProperty("httpStatusCode", 200);
        return root.toString();
    }

    // ---------------------------------------------------------------- harness

    /** Everything a run can affect outside its own stack frame. */
    private static final class Outcome {
        String result;
        int accountCount;
        String exception;
        Map<String, String> requestParams;
        List<String> calls = new ArrayList<>();
        List<String> sessionWrites = new ArrayList<>();
        /** Calls that reached BundleConfigurationHandler; not part of the compared output. */
        int bundleLoads;

        @Override
        public String toString() {
            return "result=" + result + "\nexception=" + exception + "\nparams=" + requestParams + "\ncalls="
                    + calls + "\nsession=" + sessionWrites;
        }
    }

    private interface PostProcessor {
        Result execute(Result result, DataControllerRequest request) throws Exception;
    }

    /**
     * @param expectedAccounts number of accounts expected in the response, or -1 when both must fail
     */
    private static void assertSame(Scenario s, int expectedAccounts) throws Exception {
        Outcome legacy = run(s, (r, req) -> new LegacyGetAccountsFromT24PostProcessor().execute(r, req, null));
        Outcome current = run(s, (r, req) -> new getAccountsFromT24PostProcessor().execute(r, req, null));
        assertEquals(legacy.toString(), current.toString());
        if (expectedAccounts < 0) {
            assertTrue("both runs should fail", legacy.exception != null);
        } else {
            assertTrue("run failed: " + legacy.exception, legacy.exception == null);
            assertEquals("fixture sanity: accounts in response\n" + legacy + "\ninput="
                    + ResultCanonical.of(JSONToResult.convert(s.t24)), expectedAccounts, legacy.accountCount);
        }
    }

    /**
     * Runs legacy, then the current class with a cold bundle cache, then the current class again with the cache
     * warmed by the previous run. All three must give the same output, and the warm run must not load the bundle.
     */
    private static void assertSameWithWarmBundleCache(Scenario s) throws Exception {
        Outcome legacy = run(s, (r, req) -> new LegacyGetAccountsFromT24PostProcessor().execute(r, req, null));
        Outcome cold = run(s, (r, req) -> new getAccountsFromT24PostProcessor().execute(r, req, null));
        Outcome warm = run(s, (r, req) -> new getAccountsFromT24PostProcessor().execute(r, req, null), true);
        assertEquals(legacy.toString(), cold.toString());
        assertEquals(legacy.toString(), warm.toString());
        assertEquals(1, cold.bundleLoads);
        assertEquals(0, warm.bundleLoads);
    }

    private static Outcome run(Scenario s, PostProcessor processor) throws Exception {
        return run(s, processor, false);
    }

    /**
     * @param warmBundleCache when false the bundle cache is cleared first, so every run starts from a cold cache
     */
    @SuppressWarnings("unchecked")
    private static Outcome run(Scenario s, PostProcessor processor, boolean warmBundleCache) throws Exception {
        if (!warmBundleCache) {
            BundleConfigCache.invalidateAll();
        }
        Outcome out = new Outcome();
        Map<String, String> params = new HashMap<>(s.requestParams);
        DataControllerRequest request = mockRequest(params);

        TemenosUtils temenosUtils = mock(TemenosUtils.class);
        temenosUtils.accountTypesMap = new HashMap<>(s.accountTypes);
        doAnswer(i -> {
            out.sessionWrites.add(i.getArgument(0) + "=" + i.getArgument(1));
            return null;
        }).when(temenosUtils).insertIntoSession(anyString(), any(), any(DataControllerRequest.class));

        try (MockedStatic<TemenosUtils> tu = mockStatic(TemenosUtils.class, CALLS_REAL_METHODS);
                MockedStatic<BundleConfigurationHandler> bch = mockStatic(BundleConfigurationHandler.class);
                MockedStatic<CommonUtils> cu = mockStatic(CommonUtils.class);
                MockedStatic<DBPServiceExecutorBuilder> sb = mockStatic(DBPServiceExecutorBuilder.class);
                MockedStatic<HelperMethods> hm = mockStatic(HelperMethods.class);
                MockedStatic<ResultToJSON> rtj = mockStatic(ResultToJSON.class);
                MockedConstruction<createOrgEmployeeAccounts> accountsHelper = mockConstruction(
                        createOrgEmployeeAccounts.class, (m, ctx) -> when(
                                m.getExistingAccounts(anyString(), anyString(), any()))
                                .thenReturn(JSONToResult.convert(
                                        "{\"customeraccounts\":[{\"id\":\"CA-1\",\"Account_id\":\"100001\"}]}")))) {

            // Fabric's ResultToJSON needs server-only classes; the legacy code only uses it for debug strings.
            rtj.when(() -> ResultToJSON.convert(any(Result.class)))
                    .thenAnswer(i -> ResultCanonical.of(i.getArgument(0)));
            tu.when(TemenosUtils::getInstance).thenReturn(temenosUtils);
            bch.when(() -> BundleConfigurationHandler.fetchBundleConfigurations(eq("DBP"), any()))
                    .thenAnswer(i -> {
                        out.bundleLoads++;
                        return new HashMap<>(s.bundle);
                    });
            // Plain static mocks: with CALLS_REAL_METHODS the stubbing call would run the real method and the
            // stub would attach to whatever static it calls internally. Pure helpers call through explicitly.
            cu.when(() -> CommonUtils.getParamValue(any(Record.class), anyString())).thenCallRealMethod();
            hm.when(() -> HelperMethods.hasRecords(any(Result.class))).thenCallRealMethod();
            hm.when(() -> HelperMethods.getFieldValue(any(Record.class), anyString())).thenCallRealMethod();
            cu.when(() -> CommonUtils.callIntegrationService(any(), anyMap(), any(), anyString(), anyString(),
                    anyBoolean())).thenAnswer(i -> {
                        String op = i.getArgument(4);
                        Map<String, Object> input = i.getArgument(1);
                        out.calls.add(op + new TreeMap<>(input));
                        return integrationResponse(s, op, input);
                    });

            DBPServiceExecutorBuilder builder = mock(DBPServiceExecutorBuilder.class, i -> {
                if (i.getMethod().getReturnType() == DBPServiceExecutorBuilder.class) {
                    if ("withRequestParameters".equals(i.getMethod().getName())) {
                        out.calls.add("executor" + new TreeMap<>((Map<String, Object>) i.getArgument(0)));
                    }
                    return i.getMock();
                }
                return null;
            });
            DBPServiceExecutor executor = mock(DBPServiceExecutor.class);
            when(executor.getResponse()).thenReturn(s.defaultAccountResponse);
            doAnswer(i -> executor).when(builder).build();
            sb.when(DBPServiceExecutorBuilder::builder).thenReturn(builder);

            hm.when(() -> HelperMethods.getHeaders(any(DataControllerRequest.class))).thenReturn(new HashMap<>());
            hm.when(() -> HelperMethods.callApi(any(DataControllerRequest.class), anyMap(), anyMap(), anyString()))
                    .thenAnswer(i -> {
                        out.calls.add("callApi:" + i.getArgument(3) + new TreeMap<>((Map<String, Object>) i
                                .getArgument(1)));
                        return JSONToResult.convert("{\"updatedRecords\":\"1\"}");
                    });

            try {
                Result result = processor.execute(JSONToResult.convert(s.t24), request);
                out.result = ResultCanonical.of(result);
                out.accountCount = result.getDatasetById("Accounts") == null ? 0
                        : result.getDatasetById("Accounts").getAllRecords().size();
            } catch (Exception | Error e) {
                out.exception = e.toString();
                if (e instanceof Error) {
                    e.printStackTrace();
                }
            }
        }
        out.requestParams = new TreeMap<>(params);
        return out;
    }

    private static Result integrationResponse(Scenario s, String op, Map<String, Object> input) throws Exception {
        if ("NewAccountProcessing".equals(op)) {
            JsonObject r = new JsonObject();
            r.addProperty("accounts", s.napAccounts);
            r.addProperty("newAccounts", s.napNewAccounts);
            return JSONToResult.convert(r.toString());
        }
        if ("dbxdb_fetch_default_account_actions_proc".equals(op)) {
            if (s.defaultActionsFail) {
                throw new IllegalStateException("db down");
            }
            String actions = s.defaultActions.get(String.valueOf(input.get("_coreCustomerId")));
            return JSONToResult.convert("{\"records\":[{\"defaultAccountActions\":\"" + actions + "\"}]}");
        }
        throw new AssertionError("unexpected integration call " + op);
    }

    private static DataControllerRequest mockRequest(Map<String, String> params) throws Exception {
        DataControllerRequest request = mock(DataControllerRequest.class);
        when(request.getParameter(anyString())).thenAnswer(i -> params.get(i.getArgument(0)));
        doAnswer(i -> {
            params.put(i.getArgument(0), i.getArgument(1));
            return null;
        }).when(request).addRequestParam_(anyString(), any());
        when(request.getHeaderMap()).thenReturn(new HashMap<>());

        Map<String, Object> userAttributes = new HashMap<>();
        userAttributes.put("backendIdentifiers",
                "{\"T24\":[{\"BackendId\":\"" + CORE_A + "\",\"sequence_number\":\"1\"}]}");
        IdentityHandler identityHandler = mock(IdentityHandler.class);
        when(identityHandler.getUserAttributes()).thenReturn(userAttributes);
        ConfigurableParametersHelper parameters = mock(ConfigurableParametersHelper.class);
        when(parameters.getServerProperty("HBL_IMAGES_APP_URL")).thenReturn("https://images.test");
        ServicesManager servicesManager = mock(ServicesManager.class);
        when(servicesManager.getIdentityHandler()).thenReturn(identityHandler);
        when(servicesManager.getConfigurableParametersHelper()).thenReturn(parameters);
        when(request.getServicesManager()).thenReturn(servicesManager);
        return request;
    }
}
