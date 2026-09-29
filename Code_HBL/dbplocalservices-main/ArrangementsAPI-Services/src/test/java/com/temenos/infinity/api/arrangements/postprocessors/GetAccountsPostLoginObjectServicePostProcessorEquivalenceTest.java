package com.temenos.infinity.api.arrangements.postprocessors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import org.junit.Test;
import org.mockito.MockedStatic;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.api.processor.HeadersHandler;
import com.konylabs.middleware.api.processor.IdentityHandler;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.ContractBackendDelegate;
import com.temenos.dbx.eum.product.limitsandpermissions.backenddelegate.api.LimitsAndPermissionsBackendDelegate;
import com.temenos.dbx.eum.product.limitsandpermissions.dto.ActionLimitsDTO;
import com.temenos.dbx.product.dto.FeatureActionLimitsDTO;
import com.temenos.dbx.product.utils.ThreadExecutor;
import com.temenos.infinity.api.arrangements.postprocessors.legacy.LegacyGetAccountsPostLoginObjectServicePostProcessor;
import com.temenos.infinity.api.arrangements.utils.ObjectServiceHelperMethods;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;

/**
 * Runs the pre-optimisation copy ({@link LegacyGetAccountsPostLoginObjectServicePostProcessor}) and the current
 * {@link GetAccountsPostLoginObjectServicePostProcessor} on the same getList responses with the same mocked DB
 * calls. Everything observable must be identical (response payload, ACCOUNTS cache write, events, background
 * feature-action inserts, every DB call with its inputs), except one intended difference: user_customers_proc is
 * only called when MOCK_MORTGAGE_RESPONSE is "Yes", because only that branch reads its result.
 * <p>
 * The background feature-action task is run synchronously so its DB calls and inserts are compared too.
 */
public class GetAccountsPostLoginObjectServicePostProcessorEquivalenceTest {

    private static final String LOGIN_USER = "1000000001";
    private static final String CORE_A = "100100";
    private static final String CORE_B = "100200";

    @Test
    public void accountsWithMembershipId() throws Exception {
        Scenario s = new Scenario();
        assertEquivalent(s);
        // Fixture sanity: the run must really exercise permissions, new feature actions and the cache write.
        Outcome current = run(s, false);
        assertTrue(current.response, current.response.contains("\"actions\":\"[\\\"")
                && current.response.contains("NEW_ACC_ACT"));
        assertTrue("background inserts expected\n" + current, !current.inserts.isEmpty());
        assertTrue("ACCOUNTS cache write expected", current.cacheWrites.size() == 1
                && current.cacheWrites.get(0).startsWith("ACCOUNTS" + LOGIN_USER + "="));
    }

    @Test
    public void accountsWithoutMembershipIdUseContractLookup() throws Exception {
        Scenario s = new Scenario();
        s.response = response(false);
        assertEquivalent(s);
    }

    @Test
    public void actionsFalse() throws Exception {
        Scenario s = new Scenario();
        s.request.addProperty("actions", "false");
        assertEquivalent(s);
    }

    @Test
    public void requestWithMembershipIdSkipsCacheWrite() throws Exception {
        Scenario s = new Scenario();
        s.request.addProperty("Membership_id", CORE_A);
        assertEquivalent(s);
    }

    @Test
    public void mockMortgageEnabledStillCallsUserCustomers() throws Exception {
        Scenario s = new Scenario();
        s.mockMortgage = "Yes";
        Outcome legacy = run(s, true);
        Outcome current = run(s, false);
        assertEquals(legacy.toString(), current.toString());
        assertTrue(current.calls.stream().anyMatch(c -> c.startsWith(URLConstants.USER_CUSTOMERS_PROC)));
    }

    @Test
    public void eventsEnabled() throws Exception {
        Scenario s = new Scenario();
        s.enableEvents = "true";
        assertEquivalent(s);
    }

    @Test
    public void noNewFeatureActions() throws Exception {
        Scenario s = new Scenario();
        s.newFeatureActions = null;
        assertEquivalent(s);
    }

    @Test
    public void emptyAccountList() throws Exception {
        Scenario s = new Scenario();
        s.response = JsonParser.parseString("{\"Accounts\":[],\"opstatus\":0}").getAsJsonObject();
        assertEquivalent(s);
    }

    @Test
    public void userCustomersFailureNoLongerFailsTheRequest() throws Exception {
        Scenario s = new Scenario();
        s.userCustomersFails = true;
        Outcome legacy = run(s, true);
        Outcome current = run(s, false);
        assertTrue("legacy fails when the unused user_customers_proc fails", legacy.exception != null);
        assertEquals("current is unaffected", null, current.exception);
        Scenario ok = new Scenario();
        assertEquals(run(ok, false).toString(), current.toString());
    }

    // ---------------------------------------------------------------- fixtures

    private static final class Scenario {
        JsonObject request = new JsonObject();
        JsonObject response = response(true);
        String mockMortgage = "No";
        String enableEvents = "false";
        Map<String, Set<String>> newFeatureActions = map("F1", "NEW_ACC_ACT", "NEW_GLOBAL");
        boolean userCustomersFails;
    }

    private static JsonObject response(boolean withMembershipId) {
        JsonArray accounts = new JsonArray();
        accounts.add(account("Account_id", "100001", "Savings", withMembershipId ? CORE_A : null));
        accounts.add(account("account_id", "100002", "Checking", withMembershipId ? CORE_A : null));
        JsonObject mortgage = account("Account_id", "100003", "mortgageFacility", withMembershipId ? CORE_B : null);
        accounts.add(mortgage);
        JsonObject card = account("Account_id", "100004", "Loan", withMembershipId ? CORE_B : null);
        card.addProperty("creditCardNumber", "4111111111111111");
        accounts.add(card);
        JsonObject root = new JsonObject();
        root.add("Accounts", accounts);
        root.addProperty("opstatus", "0");
        root.addProperty("httpStatusCode", "200");
        return root;
    }

    private static JsonObject account(String idKey, String id, String type, String membershipId) {
        JsonObject a = new JsonObject();
        a.addProperty(idKey, id);
        a.addProperty("accountType", type);
        a.addProperty("displayName", "Display " + id);
        a.addProperty("eStatementEnable", id.endsWith("1") ? "true" : "");
        if (membershipId != null) {
            a.addProperty("Membership_id", membershipId);
        }
        return a;
    }

    private static Map<String, Set<String>> map(String key, String... values) {
        Map<String, Set<String>> m = new HashMap<>();
        m.put(key, new HashSet<>(Arrays.asList(values)));
        return m;
    }

    private static JsonObject dbResponse(Scenario s, String url, Map<String, Object> input) {
        String filter = String.valueOf(input.get("$filter"));
        if (URLConstants.CONTRACT_CUSTOMERS_GET.equals(url)) {
            return json("{\"contractcustomers\":[{\"coreCustomerId\":\"" + CORE_A + "\",\"contractId\":\"C1\"},"
                    + "{\"coreCustomerId\":\"" + CORE_B + "\",\"contractId\":\"C2\"}]}");
        }
        if (URLConstants.CONTRACTCORECUSTOMER_GET.equals(url)) {
            return json("{\"contractcorecustomers\":[{\"contractId\":\"" + (filter.contains(CORE_A) ? "C1" : "C2")
                    + "\"}]}");
        }
        if (URLConstants.CONTRACT_GET.equals(url)) {
            return json("{\"contract\":[{\"servicedefinitionId\":\"SD-" + filter.substring(filter.length() - 2)
                    + "\"}]}");
        }
        if (URLConstants.CUSTOMER_GROUP_GET.equals(url)) {
            return json("{\"customergroup\":[{\"Group_id\":\"G-" + (filter.contains(CORE_A) ? "A" : "B") + "\"}]}");
        }
        if (URLConstants.CORECUSTOMER_ACCOUNTS_DETAILS_GET_PROC.equals(url)) {
            return json("{\"records\":["
                    + "{\"accountId\":\"100001\",\"coreCustomerId\":\"" + CORE_A + "\",\"coreCustomerName\":\"Core A\","
                    + "\"isBusinessAccount\":\"false\",\"eStatementEnable\":\"true\",\"favouriteStatus\":\"1\","
                    + "\"email\":\"a@test.local\",\"accountStatus\":\"ACTIVE\",\"nickName\":\"Salary\",\"vpaId\":\"v1\"},"
                    + "{\"accountId\":\"100002\",\"coreCustomerId\":\"" + CORE_A + "\",\"coreCustomerName\":\"Core A\","
                    + "\"isBusinessAccount\":\"false\",\"eStatementEnable\":\"false\",\"isSweepCreated\":\"true\","
                    + "\"email\":\"a@test.local\",\"accountStatus\":\"ACTIVE\"},"
                    + "{\"accountId\":\"100004\",\"coreCustomerId\":\"" + CORE_B + "\",\"coreCustomerName\":\"Core B\","
                    + "\"isBusinessAccount\":\"true\",\"consentStatus\":\"GIVEN\"}]}");
        }
        if (URLConstants.USER_CUSTOMERS_PROC.equals(url)) {
            if (s.userCustomersFails) {
                throw new IllegalStateException("db down");
            }
            return json("{\"records\":[{\"coreCustomerId\":\"" + CORE_A + "\",\"isBusiness\":\"false\"}]}");
        }
        if (URLConstants.USER_ACCOUNTACTIONS_GET_PROC.equals(url)) {
            if (CORE_A.equals(input.get("_coreCustomerId"))) {
                return json("{\"records\":["
                        + "{\"Account_id\":\"100001\",\"Customer_id\":\"" + LOGIN_USER + "\",\"Action_id\":\"ACC_VIEW\"},"
                        + "{\"Account_id\":\"100001\",\"Customer_id\":\"" + LOGIN_USER + "\",\"Action_id\":\"TRANSFER\"},"
                        + "{\"Account_id\":\"100002\",\"Customer_id\":\"" + LOGIN_USER + "\",\"Action_id\":\"ACC_VIEW\"},"
                        + "{\"Account_id\":\"100002\",\"Customer_id\":\"OTHER\",\"Action_id\":\"TRANSFER\"}]}");
            }
            return json("{\"records\":[{\"Account_id\":\"100004\",\"Customer_id\":\"" + LOGIN_USER
                    + "\",\"Action_id\":\"ACC_VIEW\"}]}");
        }
        if (URLConstants.EXCLUDED_CUSTOMER_ACTION_LIMITS_GET.equals(url)) {
            if (filter.contains(CORE_A)) {
                return json("{\"excludedcustomeraction\":["
                        + "{\"featureId\":\"F1\",\"Action_id\":\"TRANSFER\",\"Account_id\":\"100002\"},"
                        + "{\"featureId\":\"F1\",\"Action_id\":\"G_ACT\",\"Account_id\":\"\"},"
                        + "{\"featureId\":\"F1\",\"action_id\":\"ACC_VIEW\",\"account_id\":\"100001\"}]}");
            }
            return json("{}");
        }
        if (URLConstants.CUSTOMER_ACTION_LIMITS_GET.equals(url)) {
            return json("{\"customeraction\":[{\"featureId\":\"F1\",\"Action_id\":\"ACC_VIEW\",\"Account_id\":\"100001\"}]}");
        }
        if (URLConstants.CUSTOMER_LIMIT_GROUP_LIMITS_GET.equals(url)) {
            return json("{\"customerlimitgrouplimits\":[]}");
        }
        throw new AssertionError("unexpected DB call " + url);
    }

    private static FeatureActionLimitsDTO featureActionLimits(Scenario s) {
        FeatureActionLimitsDTO dto = new FeatureActionLimitsDTO();
        dto.setGlobalLevelActions(new HashSet<>(Arrays.asList("G_ACT", "NEW_GLOBAL")));
        dto.setAccountLevelActions(new HashSet<>(Arrays.asList("ACC_VIEW", "TRANSFER", "NEW_ACC_ACT")));
        dto.setMonetaryActions(new HashSet<>(Arrays.asList("TRANSFER")));
        dto.setFeatureaction(map("F1", "ACC_VIEW", "TRANSFER", "G_ACT", "NEW_ACC_ACT", "NEW_GLOBAL"));
        dto.setNewFeatureAction(s.newFeatureActions == null ? null : new HashMap<>(s.newFeatureActions));
        dto.setNewMonetaryActionLimits(new HashMap<>());
        dto.setActionsInfo(new HashMap<>());
        return dto;
    }

    private static JsonObject json(String text) {
        return JsonParser.parseString(text).getAsJsonObject();
    }

    // ---------------------------------------------------------------- harness

    private static final class Outcome {
        String returned;
        String exception;
        String response;
        List<String> payloadUpdates = new ArrayList<>();
        List<String> cacheWrites = new ArrayList<>();
        List<String> calls = new ArrayList<>();
        List<String> inserts = new ArrayList<>();
        int events;

        @Override
        public String toString() {
            return "returned=" + returned + "\nexception=" + exception + "\nresponse=" + response + "\nupdates="
                    + payloadUpdates + "\ncache=" + cacheWrites + "\ncalls=" + calls + "\ninserts=" + inserts
                    + "\nevents=" + events;
        }
    }

    private static void assertEquivalent(Scenario s) throws Exception {
        Outcome legacy = run(s, true);
        Outcome current = run(s, false);
        assertEquals("run failed", null, legacy.exception);
        List<String> legacyCallsWithoutUserCustomers = legacy.calls.stream()
                .filter(c -> !c.startsWith(URLConstants.USER_CUSTOMERS_PROC)).collect(Collectors.toList());
        assertTrue("current must not call user_customers_proc",
                current.calls.stream().noneMatch(c -> c.startsWith(URLConstants.USER_CUSTOMERS_PROC)));
        legacy.calls.clear();
        legacy.calls.addAll(legacyCallsWithoutUserCustomers);
        assertEquals(legacy.toString(), current.toString());
    }

    @SuppressWarnings("unchecked")
    private static Outcome run(Scenario s, boolean legacy) throws Exception {
        Outcome out = new Outcome();
        JsonObject requestPayload = s.request.deepCopy();
        JsonObject responsePayload = s.response.deepCopy();

        PayloadHandler requestPayloadHandler = mock(PayloadHandler.class);
        when(requestPayloadHandler.getPayloadAsJson()).thenReturn(requestPayload);
        FabricRequestManager requestManager = mock(FabricRequestManager.class);
        when(requestManager.getPayloadHandler()).thenReturn(requestPayloadHandler);
        when(requestManager.getHeadersHandler()).thenReturn(mock(HeadersHandler.class));
        Map<String, Object> userAttributes = new HashMap<>();
        userAttributes.put("legalEntityId", "NP0010001");
        IdentityHandler identityHandler = mock(IdentityHandler.class);
        when(identityHandler.getUserAttributes()).thenReturn(userAttributes);
        ServicesManager servicesManager = mock(ServicesManager.class);
        when(servicesManager.getIdentityHandler()).thenReturn(identityHandler);
        when(requestManager.getServicesManager()).thenReturn(servicesManager);

        PayloadHandler responsePayloadHandler = mock(PayloadHandler.class);
        when(responsePayloadHandler.getPayloadAsJson()).thenReturn(responsePayload);
        doAnswer(i -> {
            out.payloadUpdates.add(String.valueOf((JsonElement) i.getArgument(0)));
            return null;
        }).when(responsePayloadHandler).updatePayloadAsJson(any());
        FabricResponseManager responseManager = mock(FabricResponseManager.class);
        when(responseManager.getPayloadHandler()).thenReturn(responsePayloadHandler);

        ContractBackendDelegate contractDelegate = mock(ContractBackendDelegate.class);
        when(contractDelegate.getRestrictiveFeatureActionLimits(anyString(), anyString(), anyString(), anyString(),
                anyString(), anyMap(), anyBoolean(), anyString(), anyString())).thenAnswer(i -> {
                    out.calls.add("restrictiveFeatureActionLimits" + Arrays.asList(i.getArguments()).subList(0, 5));
                    return featureActionLimits(s);
                });
        LimitsAndPermissionsBackendDelegate permissionsDelegate = mock(LimitsAndPermissionsBackendDelegate.class);
        when(permissionsDelegate.addActionsToCustomer(any(), anyMap())).thenAnswer(i -> {
            ActionLimitsDTO dto = i.getArgument(0);
            out.inserts.add(dto.getContractId() + "|" + dto.getCustomerId() + "|" + dto.getCoreCustomerId() + "|"
                    + dto.getFeatureId() + "|" + dto.getActionId() + "|" + dto.getRoleId() + "|" + dto.getAccountId()
                    + "|" + dto.isAccountLevel() + "|" + dto.isMonetory() + "|" + dto.getLimitGroupId());
            return true;
        });
        ThreadExecutor executor = mock(ThreadExecutor.class);
        when(executor.execute(any(Callable.class))).thenAnswer(i -> {
            ((Callable<?>) i.getArgument(0)).call();
            return null;
        });

        try (MockedStatic<HelperMethods> hm = mockStatic(HelperMethods.class);
                MockedStatic<MemoryManager> mm = mockStatic(MemoryManager.class);
                MockedStatic<ServicesManagerHelper> smh = mockStatic(ServicesManagerHelper.class);
                MockedStatic<EnvironmentConfigurationsHandler> env = mockStatic(
                        EnvironmentConfigurationsHandler.class);
                MockedStatic<DBPAPIAbstractFactoryImpl> factory = mockStatic(DBPAPIAbstractFactoryImpl.class);
                MockedStatic<ThreadExecutor> te = mockStatic(ThreadExecutor.class);
                MockedStatic<ObjectServiceHelperMethods> osh = mockStatic(ObjectServiceHelperMethods.class);
                MockedStatic<Log4j2Configurator> log = mockStatic(Log4j2Configurator.class)) {

            Map<String, String> identity = new HashMap<>();
            identity.put("customer_id", LOGIN_USER);
            hm.when(() -> HelperMethods.getCustomerFromAPIDBPIdentityService(any(FabricRequestManager.class)))
                    .thenReturn(identity);
            hm.when(() -> HelperMethods.isAuthenticationCheckRequiredForService(anyMap())).thenReturn(true);
            hm.when(() -> HelperMethods.getCustomerIdFromSession(any(FabricRequestManager.class)))
                    .thenReturn(LOGIN_USER);
            hm.when(() -> HelperMethods.getHeaders(any(FabricRequestManager.class))).thenReturn(new HashMap<>());
            hm.when(() -> HelperMethods.getHeaders(any(HeadersHandler.class))).thenReturn(new HashMap<>());
            hm.when(() -> HelperMethods.getStringFromJsonObject(any(), anyString(), anyBoolean()))
                    .thenCallRealMethod();
            hm.when(() -> HelperMethods.callApiJson(any(FabricRequestManager.class), anyMap(), anyMap(), anyString()))
                    .thenAnswer(i -> {
                        String url = i.getArgument(3);
                        Map<String, Object> input = i.getArgument(1);
                        out.calls.add(url + new TreeMap<>(input));
                        return dbResponse(s, url, input);
                    });
            mm.when(() -> MemoryManager.saveIntoCache(anyString(), anyString(), anyInt())).thenAnswer(i -> {
                out.cacheWrites.add(i.getArgument(0) + "=" + i.getArgument(1) + " ttl=" + i.getArgument(2));
                return null;
            });
            smh.when(ServicesManagerHelper::getServicesManager).thenReturn(servicesManager);
            env.when(() -> EnvironmentConfigurationsHandler.getServerAppProperty(anyString(),
                    any(ServicesManager.class))).thenAnswer(
                            i -> "MOCK_MORTGAGE_RESPONSE".equals(i.getArgument(0)) ? s.mockMortgage : "");
            factory.when(() -> DBPAPIAbstractFactoryImpl.getBackendDelegate(eq(ContractBackendDelegate.class)))
                    .thenReturn(contractDelegate);
            factory.when(() -> DBPAPIAbstractFactoryImpl
                    .getBackendDelegate(eq(LimitsAndPermissionsBackendDelegate.class))).thenAnswer(i -> {
                        out.calls.add("getBackendDelegate(LimitsAndPermissions)");
                        return permissionsDelegate;
                    });
            te.when(ThreadExecutor::getExecutor).thenReturn(executor);
            osh.when(() -> ObjectServiceHelperMethods.getConfigurableParameters(anyString(), any()))
                    .thenReturn(s.enableEvents);
            osh.when(() -> ObjectServiceHelperMethods.hasKey(any(), anyString())).thenCallRealMethod();
            osh.when(() -> ObjectServiceHelperMethods.execute(any())).thenAnswer(i -> {
                out.events++;
                return null;
            });

            try {
                boolean returned = legacy
                        ? new LegacyGetAccountsPostLoginObjectServicePostProcessor().process(requestManager,
                                responseManager)
                        : new GetAccountsPostLoginObjectServicePostProcessor().process(requestManager,
                                responseManager);
                out.returned = String.valueOf(returned);
            } catch (Exception e) {
                out.exception = e.toString();
            }
        }
        out.response = responsePayload.toString();
        return out;
    }
}
