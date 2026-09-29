# getList performance: deploy notes

API: `POST /services/data/v1/Holdings/operations/DigitalArrangements/getList`

This work speeds up getList without changing its response. It was delivered in five phases on `feature/bhargav`:

| Phase | What it does | Needs the switch? |
|---|---|---|
| 1 | Removes wasted work in the getList classes: one JWT per request instead of two, no debug serialisation, no token/payload logging, config parsed once | No, always on |
| 2 | Caches the Admin "DBP" bundle configuration (TTL, failed loads never cached) | No, always on |
| 3 | Snapshot cache: replays what getList read from the database when nothing it depends on has changed | **Yes**, `HBL_GETLIST_CACHE_ENABLED` |
| 4 | Invalidation: writers in online banking, HBL services and Spotlight make cached snapshots obsolete | Runs always; only matters when the switch is on |
| 5 | Tests, `tools/getlist-diff.ps1`, `tools/getlist-timing.ps1`, these notes | - |
| Optional | Balance cache: reuses the whole T24 accounts response, balances included, for a few seconds | **Yes**, its own switch `HBL_GETLIST_BAL_CACHE_ENABLED` |

**No Fabric change is needed.** No object service, integration service, operation, Java service, preprocessor,
postprocessor, binding, endpoint or mapping was added, renamed or changed. The four getList classes keep their
names, packages and signatures.

---

## 1. What to build and deploy

Build and deploy **all** of these together. The invalidation hooks call a new class in
`DBPCommonUtilityServices`, so every app that bundles it needs the new jar.

| Code base | Modules changed |
|---|---|
| `dbplocalservices-main` | `DBPCommonUtilityServices`, `com.temenos.infinity.t24irisintegration`, `ArrangementsAPI-Services`, `eum-productservices`, `DBPProductServices`, `DBPNonProductServices`, `UserManagementAPI-Services` |
| `hblservices-main` | `HBLServices` (default account, nickname, consent hooks) |
| `adminconsoleservices-main` | `AdminConsoleServices` (Spotlight hooks) |

Build and run the tests (unit tests are skipped by default; `-DskipUnitTests=false` runs them):

```
cd Code_HBL/dbplocalservices-main
mvn -pl DBPCommonUtilityServices,com.temenos.infinity.t24irisintegration,ArrangementsAPI-Services,eum-productservices,DBPProductServices,DBPNonProductServices,UserManagementAPI-Services -am clean install -DskipUnitTests=false

cd ../hblservices-main && mvn clean install
cd ../adminconsoleservices-main && mvn clean install
```

---

## 2. Server properties

All are optional. A missing, blank or invalid value uses the default and, if invalid, logs one warning. Every
property is read on each request (no restart), except the pool size.

| Property | Default | Meaning |
|---|---|---|
| `HBL_GETLIST_CACHE_ENABLED` | `false` | Master switch of the snapshot cache (phase 3). Anything but `true` runs the original code. |
| `HBL_GETLIST_ENT_TTL_SECONDS` | `300` | How long a snapshot is kept. Also the longest time a change made by an unhooked writer (section 5) stays invisible. Must be > 0. |
| `HBL_GETLIST_PERMVER_TTL_SECONDS` | `86400` | How long a version token is kept. Must be > 0. |
| `HBL_GETLIST_CACHE_TIMEOUT_MS` | `50` | Longest wait for one cache read before using the database instead. Must be > 0. |
| `HBL_GETLIST_PARALLEL_POOL_SIZE` | `16` | Threads used for cache calls. Read once, when the pool is created (restart to change). Must be > 0. |
| `HBL_GETLIST_BAL_CACHE_ENABLED` | `false` | Switch of the optional balance cache (section 3a). Independent of `HBL_GETLIST_CACHE_ENABLED`. |
| `HBL_GETLIST_BAL_TTL_SECONDS` | `45` | How long a T24 accounts response (with balances) is reused. Must be > 0. |
| `HBL_BUNDLE_CONFIG_TTL_SECONDS` | `600` | How long the DBP bundle configuration is cached (phase 2). `0` turns that cache off. |
| `HBL_GETLIST_TIMING_LOG` | `false` | `true` logs one timing line per getList class per request (no customer data). For QA only. |

---

## 3. Rollout and rollback

**Rollout**

1. Deploy everything in section 1 with the switch off (the default). getList behaves as before, plus the
   phase 1 and 2 savings.
2. On QA, run the checklist in section 4 with the switch off and then on.
3. Turn the switch on in production only when the checklist passes, in particular the Spotlight test (4.4).

**Rollback**

- Immediate, no deploy: set `HBL_GETLIST_CACHE_ENABLED=false`. The next request runs the original code;
  stored snapshots are ignored and expire on their own.
- Full: revert the `feature/bhargav` merges and redeploy.

---

## 3a. Balance cache (optional, for performance testing)

With `HBL_GETLIST_BAL_CACHE_ENABLED=true`, `GetAccountsOperation` reuses the complete response of the T24
integration call (`ArrangementT24ISAccounts.getAccountsByCoreCustomerIdList`) for
`HBL_GETLIST_BAL_TTL_SECONDS` (default 45 s). A hit skips T24 and both T24 processors; the object postprocessor
(permissions, details) still runs.

- **Balances can be up to the TTL old** while it is on, for example right after a transfer. The bank must approve
  that before it is used in production; it was built for performance testing.
- Only for a customer's own session (not for administrators), keyed on customer, company, `Membership_id`,
  `coreCustomerIdList`, `actions` and `loginUserId`, plus the same version tokens as the snapshots, so every
  invalidation hook (section 5) clears it too.
- Stored only when the response is clean: `opstatus` 0, no error, at least one account, no account with
  `isNew=true`.
- The session copy of the accounts that transfer and bill pay validate against (`<userId>_Accounts`, one hour) is
  written by the call that filled the entry, so it is still there during a hit.
- With `HBL_GETLIST_TIMING_LOG=true` the log shows `balanceCacheStore` on the call that filled the entry and
  `balanceCacheLookup` without `integrationService` on a hit.

Performance-test combinations: both switches off (baseline), `HBL_GETLIST_CACHE_ENABLED` only, both on.

---

## 4. QA checklist

Tools are in `tools/`; copy `tools/users.example.csv` to `tools/users.csv` and fill it in (see section 7).
Captured responses contain customer data: keep them on your machine and delete them afterwards.

### 4.1 Baseline and speed

- [ ] Switch off: `getlist-timing.ps1` for a few users, 20 runs each, on QA and on staging. Note min, median, p90.
- [ ] Switch on: the same runs. Expect the first call about the same as before, later calls faster.
- [ ] With `HBL_GETLIST_TIMING_LOG=true`, the log shows `snapshotStore` on a user's first call with the switch on
      and `snapshotLookup` without database stages on the next one. **If `snapshotStore` never appears**, the
      database responses do not carry `opstatus: 0` and nothing is cached (safe, but no speed-up): report it.

### 4.2 Same response

- [ ] `getlist-diff.ps1` for at least 10 users (retail, multi-CIF, business, a user with excluded actions, a
      user with a new account): capture `off`, turn the switch on, capture `on`, compare. **Zero differences**
      (ignore balance fields only if balances moved between captures).
- [ ] Login (`Accounts/getAccountsPostLogin`) and every other operation bound to the same classes behave as before.

### 4.3 Changes show up

- [ ] Change a nickname, the default account, a consent, e-statement settings: the next getList shows it.
- [ ] Edit a user's permissions or a contract in online-banking user management: the next getList shows it.
- [ ] New T24 account: shown at once with default actions and `isNew=true`; the `customeraccounts` row is
      inserted in the background, as before.

### 4.4 Spotlight (shared cache)

- [ ] Switch on. Load the account list of a test customer twice.
- [ ] In Spotlight, remove an action from that customer (or their group).
- [ ] Load the account list again: **the action is gone immediately.** If it only disappears after about
      `HBL_GETLIST_ENT_TTL_SECONDS`, Spotlight and online banking do not share the cache store: lower the TTL or
      keep the switch off in production.

### 4.5 Transactions and failure

- [ ] Transfer, bill pay and eSewa load right after getList: validation passes and balances are fresh
      (with the balance cache off, balances always come live from T24).
- [ ] Balance cache on: a second getList within the TTL shows `balanceCacheLookup` and no `integrationService`
      in the timing log; after a transfer, the balance updates within `HBL_GETLIST_BAL_TTL_SECONDS`.
- [ ] Cache down or blocked: getList still answers correctly (slower).
- [ ] Load test: 50 concurrent users for 15 minutes; p90 on target; no error spike after a Spotlight change
      (every customer reads the database once after it).
- [ ] Switch off again: identical to the baseline.

---

## 5. What makes a cached snapshot obsolete

A snapshot is only ever stored for a request that wrote nothing and whose reads all succeeded: no new account, a
default account already set, no new feature action, every account with its details row, every database response
with `opstatus: 0`. `MOCK_MORTGAGE_RESPONSE=Yes` bypasses the cache.

Its key contains a global version token, a per-customer version token and a fingerprint of the inputs (customer,
company/legal entity, "actions" flag, account and core-customer ids from T24). It stops being used when:

| Event | How |
|---|---|
| T24 returns a different set of accounts (new or closed account) | Different fingerprint |
| Online-banking writers (hooked, section 5.1) | Version bump |
| Spotlight writers (hooked, section 5.2) | Global version bump, if the cache store is shared (4.4) |
| Anything else | Expiry after `HBL_GETLIST_ENT_TTL_SECONDS` (default 5 minutes) |

### 5.1 Hooked in online banking and HBL services

The bump runs in a `finally` block after the operation, so a part-way failure still invalidates.

- **All customers:** contract create, update, status change and enrol; business and retail contract creation;
  new contract features; custom role create, update, delete, apply and apply-to-users; customer-role and
  service-definition limits and permissions; Infinity user edit, status update, primary retail contract
  assignment and create-with-contract; business user action limits and group. Both the `eum-productservices`
  and `DBPProductServices` copies.
- **Logged-in customer:** account settings (`ArrangementsAPI-Services`, `UserManagementAPI-Services`,
  `DBPNonProductServices`), favourite status, core-customer favourite status, new account opening.
- **One customer (HBL services):** default account reset, account nickname, cross-border consent.

### 5.2 Hooked in Spotlight

Global bump after: customer actions update, customer groups update, group create, group edit, feature manage,
upgrade roles and permissions, upgrade user, business banking service limits, business type delete, member
create, campaign customer-group mapping and unmapping.

### 5.3 Not hooked (bounded by the snapshot lifetime)

| Writer | Reason |
|---|---|
| Fabric services bound directly to the database (no Java code) | Hooking them needs a Fabric change |
| Database scripts, batch jobs, manual SQL | Outside the application |
| `CoreBankingCustomersManageScheduler` (Spotlight) | Runs on a timer; a bump per run would empty the cache for everybody |
| Login | Authentication code left unchanged |
| Spotlight internal-user roles and permissions | Bank staff data, not used by getList |

---

## 6. Failure cases and their tests

The rule: a request may become slower, never wrong, and never fails where the original code would succeed. When
the cache cannot be used, the class runs the original code for that request.

| # | Situation | Behaviour | Test(s) |
|---|---|---|---|
| F1 | Switch off, missing or invalid | Original code | `switchOffOrMissingOrInvalidGivesNoSession`, `cacheSwitchIsOffUnlessExactlyTrue`, `switchOffIgnoresStoredSnapshots`, all non-cache equivalence tests |
| F2 | Cache unreachable or slower than the timeout | Original code for this request; one warning per minute at most | `unreadableCacheGivesNoSession`, `slowReadIsAnError`, `nonStringValueMeansTheCacheIsNotAvailable`, `cacheDownRunsTodaysCode` (3 classes) |
| F3 | Version token missing | Created; the request continues as a cold call | `missingVersionsAreCreatedOnFirstUse` |
| F4 | Snapshot missing, expired or for an old version | Original code, then stored | `customerBumpMakesStoredSnapshotUnreachable`, `globalBumpMakesEverySnapshotUnreachable`, `versionBumpMakesTheNextCallReadTheDatabase` (3 classes) |
| F5 | Snapshot corrupt, other schema or incomplete | Treated as missing | `corruptOrForeignValuesAreTreatedAsMisses`, `incompleteSnapshotsAreNotStored` |
| F6, F7 | Rebuild lock | Not applicable: no lock (a miss simply runs the original code) | - |
| F8 | A database read fails | Original code already ran; the result is not stored | `cacheDoesNotStoreAFailedDbRead`, `cacheDoesNotStoreAnEmptyLookup`, `cacheDoesNotStoreAnEmptyNewAccountProcessingResult` |
| F9 | Thread pool saturated | Work runs on the request thread | `saturatedPoolRunsWorkOnTheCaller` |
| F10 | T24 call fails | Unchanged: T24 is never cached | Existing behaviour, `GetAccountsOperation` unchanged |
| F11 | New account from T24 | Original code (new fingerprint); nothing stored until the account is registered | `newAccountInTheResponseMissesTheCache`, `differentT24AccountsMissTheCache`, `cacheDoesNotStoreWhenThereAreNewAccounts`, `cacheDoesNotStoreWhileAnAccountHasNoDetailsRow` |
| F12, F13 | Background sync jobs fail | Not applicable: no new background jobs (the original background inserts are unchanged) | - |
| F14 | Snapshot write fails | Response unaffected; next call reads the database | `readAndStoreNeverThrowWhenTheCacheFails`, `failedWriteNeverThrows` |
| F15 | `ACCOUNTS` cache write fails | Unchanged original behaviour | Existing behaviour |
| F16 | Bump fails inside a writer | The writer's operation is unaffected; snapshot expires with its TTL | `neverThrowsWhenTheCacheIsDown`, `bumpNeverThrowsWhenTheCacheIsDown` |
| F17 | Lost bump from concurrent increments | Not applicable: tokens are random, not counters | `tokensAreUnique` |
| F18 | Bundle configuration load fails | Returned as today, never cached | `failedBundleLoadIsNotCached`, `emptyResultIsReturnedButNotCached`, `handlerExceptionPropagatesAndIsNotCached` |
| F19 | `actions=false`, `Membership_id`, `coreCustomerIdList` in the request | Same semantics in both paths | `cacheWithActionsFalseIsKeptApart`, `cacheWithMembershipIdStillSkipsTheAccountsCacheWrite`, `cacheIsNotUsedWhenMembershipIdIsGiven`, `cacheIsNotUsedWhenCoreCustomersAreGiven` |
| F20 | Login and other operations bound to the same classes | Same code paths | QA checklist 4.2 |
| F21 | Balance cache: miss, corrupt, unreachable or unclean response | T24 called live, as today; unclean responses never stored | `T24AccountsSnapshotTest`, `balanceCacheHasItsOwnSwitch`, `balanceEntriesAreClearedByTheSameBumps`, `corruptOrForeignValuesAreTreatedAsMisses` |
| F22 | Unexpected exception in the cache code | Caught; original code for this request | `readAndStoreNeverThrowWhenTheCacheFails`, `cacheDownRunsTodaysCode`, `GetListSnapshotCache.open` catches every runtime error |

Also covered: `cacheIsNotUsedForMockMortgage`, `cacheKeepsCompaniesApart`, `stagesDoNotShareSlots`,
`differentInputsGiveDifferentSlots`, `cacheColdThenWarmForBusinessAccountWithExcludedActions`, and the Phase 1
equivalence tests that run the original classes against the new ones.

---

## 7. Tools

Both scripts are written for Windows PowerShell 5.1 and were tested in PowerShell 7 against a stand-in server. They take a users CSV (`tools/users.example.csv`):

| Column | Meaning |
|---|---|
| `label` | Short name used in output and file names; no personal data |
| `token` | An existing claims token (`X-Kony-Authorization`), for users whose login needs MFA |
| `username`, `password` | Used with `-AuthUrl` and `-IdentityProvider` when there is no token |

Tokens and passwords are never printed or saved.

```powershell
# Same response, switch off vs on (cold and warm)
.\getlist-diff.ps1 -Mode Capture -Label off -BaseUrl https://<fabric-host>/ -UsersCsv users.csv -OutDir out
#   ... set HBL_GETLIST_CACHE_ENABLED=true ...
.\getlist-diff.ps1 -Mode Capture -Label on  -BaseUrl https://<fabric-host>/ -UsersCsv users.csv -OutDir out
.\getlist-diff.ps1 -Mode Compare -OutDir out -Left off -Right on

# Timing: 20 calls per user, min / median / p90 / max
.\getlist-timing.ps1 -BaseUrl https://<fabric-host>/ -UsersCsv users.csv -Runs 20

# Signing in with username/password instead of tokens
... -AuthUrl https://<fabric-host>/authService/100000002 -IdentityProvider <IdentityServiceName> -AppKey <key> -AppSecret <secret>
```

`getlist-diff.ps1 -Mode Compare` exits with 1 when any field differs; `getlist-timing.ps1` exits with 1 when any
call does not return HTTP 200.

---

## 8. Known defects found (reported, not fixed)

These are in shared or product code outside the scope of this work. Each needs its own approved change.

| Where | Defect |
|---|---|
| `TemenosBasePreProcessor` (both copies) | Logs the T24 `Authorization` token at ERROR level on every call |
| `getAccountsFromT24PreProcessor` | `autoSyncAccounts == "false"` compares references, so it is never true; `explicitCoreCustomerIdList` is always blank. Kept as is, because fixing it changes which accounts go to new-account processing |
| `InfinityUserManagementBusinessDelegateImpl` / `…BackendDelegateImpl.processNewAccounts` | Log the user's account sets and the full proc response at ERROR level on every getList |
| `processNewAccounts` background thread | Swallows every exception silently, so a failed new-account insert leaves no trace |
| `TemenosBasePreProcessor` | Exists in both `T24Common` and `com.temenos.infinity.t24irisintegration` with different code; which one Fabric loads depends on which jars are deployed and their order. Confirm only one is deployed |
