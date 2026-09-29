package com.bct.eSewa;

import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.bct.custom.constants.HBLURLConstants;
import com.bct.utilities.HBLCommonUtility;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.utils.CustomerSessionsUtil;

/**
 * Durable state for an eSewa load, stored in the EXISTING esewaTransactionPendingLog table.
 * No DDL is required. Three existing columns are repurposed:
 *
 *   id                  -> "ESL" + referenceId.  The table's PRIMARY KEY therefore becomes the
 *                          idempotency lock: a second insert for the same T24 payment order
 *                          fails on duplicate key. Atomic, cross-node, crash-safe.
 *   BatchId             -> the lifecycle state. Verified written-but-never-read across com/bct,
 *                          and the value it used to hold ("FT"+random) was never sent to eaSewa.
 *   RawResponse         -> a small JSON journal (attempts, last error, statuses, timings).
 *
 * Status / TransactionStatus keep carrying eSewa's own literals so that existing readers are
 * unaffected. OriginatingUniqueId keeps carrying the T24 referenceId, exactly as today.
 *
 * If DDL access is ever granted, THIS FILE is the only one that changes.
 */
public final class EsewaLoadStateDao {

    private static final Logger logger = LogManager.getLogger(EsewaLoadStateDao.class);

    private static final String ID_PREFIX = "ESL";
    private static final int    ID_MAX    = 50;

    /** Create + Get already exist in Fabric. Update must be added (same pattern as the Log table). */
    private static final String OP_UPDATE = "dbxdb_esewaTransactionPendingLog_update";

    private static final String DATASET = "esewaTransactionPendingLog";

    // ---------------------------------------------------------------- states

    public enum State {
        T24_CHECK_PENDING,
        T24_COMPLETED,
        T24_FAILED(true),
        T24_PENDING_UNRESOLVED,
        T24_STATUS_UNKNOWN,
        ESEWA_SKIPPED_NO_BUDGET,
        ESEWA_LOAD_INITIATED,
        ESEWA_SUCCESS(true),
        ESEWA_PENDING,
        ESEWA_OUTCOME_UNKNOWN,
        ESEWA_FAILED_CONFIRMED,
        REVERSAL_PENDING,
        REVERSED(true),
        REVERSAL_FAILED,
        MANUAL_INTERVENTION;

        private final boolean terminal;
        State()                 { this.terminal = false; }
        State(boolean terminal) { this.terminal = terminal; }
        public boolean isTerminal() { return terminal; }

        public static State parse(String raw) {
            if (StringUtils.isBlank(raw)) { return null; }
            for (State s : values()) {
                if (s.name().equalsIgnoreCase(raw.trim())) { return s; }
            }
            return null; // legacy "FT..." values and anything unrecognised
        }
    }

    /** States the reconciler must pick up. MANUAL_INTERVENTION is excluded: a human owns it. */
    public static final State[] WORK_STATES = {
            State.T24_CHECK_PENDING,
            State.T24_PENDING_UNRESOLVED,
            State.T24_STATUS_UNKNOWN,
            State.ESEWA_SKIPPED_NO_BUDGET,
            State.ESEWA_LOAD_INITIATED,
            State.ESEWA_PENDING,
            State.ESEWA_OUTCOME_UNKNOWN,
            State.ESEWA_FAILED_CONFIRMED,
            State.REVERSAL_PENDING,
            State.REVERSAL_FAILED
    };

    public enum ClaimResult { CLAIMED, ALREADY_OWNED, ERROR }

    // ---------------------------------------------------------------- identity

    /** Deterministic primary key. This is the whole idempotency mechanism. */
    public static String idFor(String referenceId) {
        String raw = ID_PREFIX + referenceId;
        if (raw.length() <= ID_MAX) {
            return raw;
        }
        return ID_PREFIX + sha256Hex(referenceId).substring(0, ID_MAX - ID_PREFIX.length());
    }

    // ---------------------------------------------------------------- claim

    /**
     * Claims ownership of this payment order by inserting the row BEFORE anything moves.
     * CLAIMED       -> this thread owns the transaction and may proceed.
     * ALREADY_OWNED -> another request, node or the reconciler owns it. Do NOT call eSewa.
     * ERROR         -> persistence unavailable. Do NOT call eSewa (fail safe).
     *
     * Duplicate detection deliberately does not trust the Fabric error text: on ANY failure
     * the row is read back by id. Present = duplicate, absent = genuine error.
     */
    public ClaimResult claim(DataControllerRequest request, String referenceId, String amount,
                             String esewaId, String sourceAccountNo, String receiverName,
                             String swiftCode, String fee, String channel, String purpose,
                             String senderName) {
        String id = idFor(referenceId);
        String ts = timestamp();

        Map<String, Object> in = new HashMap<String, Object>();
        in.put("id", id);
        in.put("Customer_id",        customerId(request));
        in.put("username",           userName(request));
        in.put("core_identifier",    coreIdentifier(request));
        in.put("BatchId",            State.T24_CHECK_PENDING.name());   // lifecycle state
        in.put("OriginatingUniqueId", referenceId);
        in.put("SwiftCode",          nvl(swiftCode));
        in.put("EsewaId",            nvl(esewaId));
        in.put("EsewaReceiverName",  nvl(receiverName));
        in.put("Amount",             nvl(amount));
        in.put("Fee",                nvl(fee));
        in.put("SourceAccountNo",    nvl(sourceAccountNo));
        in.put("SenderName",         nvl(senderName));
        in.put("SenderMobileNo",     "");
        in.put("SenderAddress",      "");
        in.put("TransactionPurpose", nvl(purpose));
        in.put("Status",             "");
        in.put("ResponseCode",       "");
        in.put("StatusCode",         "");
        in.put("TransactionStatus",  "");
        in.put("TransactionDetailOriginatingUniqueId", "");
        in.put("RawResponse",        journal(0, null, null, null));
        in.put("statuscheck",        "1");
        in.put("channelName",        nvl(channel));
        in.put("TransactionDate",    ts);
        in.put("createdby",          coreIdentifier(request));
        in.put("modifiedby",         coreIdentifier(request));
        in.put("createdts",          ts);
        in.put("lastmodifiedts",     ts);
        in.put("synctimestamp",      ts);
        in.put("softdeleteflag",     "0");

        boolean inserted = false;
        try {
            String response = DBPServiceExecutorBuilder.builder()
                    .withServiceId(HBLURLConstants.HBL_OLB_CRUD_OPERATION_SERVICE)
                    .withOperationId(HBLURLConstants.ESEWA_TRANSACTION_PENDING_LOG_CREATE)
                    .withRequestParameters(in)
                    .withRequestHeaders(request.getHeaderMap())
                    .build()
                    .getResponse();
            JSONObject json = new JSONObject(StringUtils.defaultIfBlank(response, "{}"));
            inserted = !json.has("errmsg") && !json.has("dbpErrMsg");
            if (!inserted) {
                logger.warn("eSewa claim insert rejected for {} - verifying by read-back", id);
            }
        } catch (Exception e) {
            logger.warn("eSewa claim insert threw for {} ({}) - verifying by read-back",
                    id, e.getClass().getSimpleName());
        }

        if (inserted) {
            logger.info("eSewa claim CLAIMED id={} order={}", id, referenceId);
            return ClaimResult.CLAIMED;
        }

        JSONObject existing = findByReferenceId(request, referenceId);
        if (existing != null) {
            logger.warn("eSewa claim ALREADY_OWNED id={} state={}", id, stateOf(existing));
            return ClaimResult.ALREADY_OWNED;
        }
        logger.error("eSewa claim ERROR id={} - row absent after failed insert; refusing to load", id);
        return ClaimResult.ERROR;
    }

    // ---------------------------------------------------------------- transitions

    public boolean markState(DataControllerRequest request, String referenceId, State state,
                             Map<String, Object> extraFields, String note, int attempts) {
        Map<String, Object> in = new HashMap<String, Object>();
        if (extraFields != null) { in.putAll(extraFields); }
        in.put("id", idFor(referenceId));
        in.put("BatchId", state.name());
        in.put("RawResponse", journal(attempts, state.name(), note,
                str(extraFields, "TransactionStatus"),
                str(extraFields, "StatusCode"),          // T24 currentStatus
                str(extraFields, "__kind"),
                -1L));
        in.remove("__kind");                             // journal-only, never a column
        in.put("statuscheck", state == State.ESEWA_SUCCESS ? "0" : "1");
        in.put("lastmodifiedts", timestamp());

        try {
            Result r = CommonUtils.callIntegrationService(request, new HashMap<String, Object>(in),
                    new HashMap<String, Object>(), HBLURLConstants.HBL_OLB_CRUD_OPERATION_SERVICE,
                    OP_UPDATE, false);
            String err = r == null ? "no result" : r.getParamValueByName("errmsg");
            if (StringUtils.isNotBlank(err)) {
                logger.error("eSewa state update FAILED order={} state={} err={}", referenceId, state, err);
                return false;
            }
            logger.info("eSewa state order={} -> {}{}", referenceId, state,
                    StringUtils.isBlank(note) ? "" : " (" + note + ")");
            return true;
        } catch (Exception e) {
            logger.error("eSewa state update threw order={} state={}: {}", referenceId, state, e.toString());
            return false;
        }
    }

    // ---------------------------------------------------------------- reads

    public JSONObject findByReferenceId(DataControllerRequest request, String referenceId) {
        // NOTE: string literals are quoted. Unquoted literals are why some existing
        // OData filters in this codebase are unreliable.
        String filter = "id eq '" + escape(idFor(referenceId)) + "'";
        List<JSONObject> rows = query(request, filter, 1);
        return rows.isEmpty() ? null : rows.get(0);
    }

    /** Rows in a non-terminal state, for the reconciler. */
    public List<JSONObject> findWork(DataControllerRequest request, int limit) {
        StringBuilder f = new StringBuilder("(");
        for (int i = 0; i < WORK_STATES.length; i++) {
            if (i > 0) { f.append(" or "); }
            f.append("BatchId eq '").append(WORK_STATES[i].name()).append("'");
        }
        f.append(")");
        return query(request, f.toString(), limit);
    }

    private List<JSONObject> query(DataControllerRequest request, String filter, int limit) {
        List<JSONObject> out = new ArrayList<JSONObject>();
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put(Constants.PARAM_DOLLAR_FILTER, filter);
            if (limit > 0) { params.put("$top", String.valueOf(limit)); }

            Result result = CommonUtils.callIntegrationService(request,
                    new HashMap<String, Object>(params), new HashMap<String, Object>(),
                    HBLURLConstants.HBL_OLB_CRUD_OPERATION_SERVICE,
                    HBLURLConstants.ESEWA_TRANSACTION_PENDING_LOG_GET, false);

            Dataset ds = result == null ? null : result.getDatasetById(DATASET);
            if (ds == null || ds.getAllRecords() == null) { return out; }

            for (int i = 0; i < ds.getAllRecords().size(); i++) {
                Record rec = ds.getRecord(i);
                JSONObject row = new JSONObject();
                putIfPresent(row, rec, "id");
                putIfPresent(row, rec, "BatchId");
                putIfPresent(row, rec, "OriginatingUniqueId");
                putIfPresent(row, rec, "Customer_id");
                putIfPresent(row, rec, "username");
                putIfPresent(row, rec, "core_identifier");
                putIfPresent(row, rec, "EsewaId");
                putIfPresent(row, rec, "SwiftCode");
                putIfPresent(row, rec, "Amount");
                putIfPresent(row, rec, "Fee");
                putIfPresent(row, rec, "SourceAccountNo");
                putIfPresent(row, rec, "SenderName");
                putIfPresent(row, rec, "EsewaReceiverName");
                putIfPresent(row, rec, "TransactionPurpose");
                putIfPresent(row, rec, "Status");
                putIfPresent(row, rec, "TransactionStatus");
                putIfPresent(row, rec, "TransactionDetailOriginatingUniqueId");
                putIfPresent(row, rec, "RawResponse");
                putIfPresent(row, rec, "channelName");
                putIfPresent(row, rec, "createdts");
                putIfPresent(row, rec, "lastmodifiedts");
                out.add(row);
            }
        } catch (Exception e) {
            logger.error("eSewa state query failed [{}]: {}", filter, e.toString());
        }
        return out;
    }

    /**
     * True when esewaTransactionLog already carries a row for this reference. Used by the
     * reconciler so a load logged by the live request is not logged a second time (which
     * would double-count the customer's daily/monthly limits).
     */
    public boolean transactionLogExists(DataControllerRequest request, String referenceId) {
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put(Constants.PARAM_DOLLAR_FILTER,
                    "OriginatingUniqueId eq '" + escape(referenceId) + "'");
            Result r = CommonUtils.callIntegrationService(request,
                    new HashMap<String, Object>(params), new HashMap<String, Object>(),
                    "HBLMerchantCRUDService", "dbxdb_esewaTransactionLog_get", false);
            Dataset ds = r == null ? null : r.getDatasetById("esewaTransactionLog");
            return ds != null && ds.getAllRecords() != null && !ds.getAllRecords().isEmpty();
        } catch (Exception e) {
            logger.warn("transactionLogExists check failed order={}: {}", referenceId, e.toString());
            return true;   // fail safe: assume it exists rather than risk a duplicate row
        }
    }

    // ------------------------------------------------- esewaTransactionLog (headless)

    /**
     * Writes the esewaTransactionLog row for a load performed by the RECONCILER.
     *
     * EsewaValidationClient.esewaTransactionLog(...) cannot be reused from a Fabric job:
     * it reads customer_id / UserName from the session at :736-738, which throws with no
     * logged-in user and is swallowed at :788-791, silently producing no row. Without this
     * method a deferred load would never appear in the customer's transaction history
     * (eSewaGetTransactions reads this table) and would never count toward the daily or
     * monthly limits (eSewaLimitCheck reads the same table).
     *
     * Identity is taken from the claim row, which captured it while a session existed.
     */
    public boolean writeTransactionLogFromRow(DataControllerRequest request, JSONObject row,
                                              String status, String responseCode, String txStatus,
                                              String esewaTxnId, String rawResponse) {
        String referenceId = row.optString("OriginatingUniqueId", "");
        try {
            String ts = timestamp();
            long n = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;

            Map<String, Object> in = new HashMap<String, Object>();
            in.put("id", "REQ" + n);
            in.put("Customer_id",     row.optString("Customer_id", ""));
            in.put("username",        row.optString("username", ""));
            in.put("core_identifier", row.optString("core_identifier", ""));
            in.put("BatchId",         referenceId);      // legacy shape: what eSewa received
            in.put("SwiftCode",       row.optString("SwiftCode", ""));
            in.put("EsewaId",         row.optString("EsewaId", ""));
            in.put("EsewaReceiverName", row.optString("EsewaReceiverName", ""));
            in.put("Fee",             row.optString("Fee", ""));
            in.put("Amount",          row.optString("Amount", ""));
            in.put("OriginatingUniqueId", referenceId);
            in.put("Status",          nvl(status));
            in.put("ResponseCode",    nvl(responseCode));
            in.put("StatusCode",      nvl(status));
            in.put("RawResponse",     nvl(rawResponse));
            in.put("SourceAccountNo", row.optString("SourceAccountNo", ""));
            in.put("SenderName",      row.optString("SenderName", ""));
            in.put("SenderMobileNo",  "");
            in.put("SenderAddress",   "");
            in.put("TransactionPurpose", row.optString("TransactionPurpose", ""));
            in.put("TransactionStatus",  nvl(txStatus));
            in.put("TransactionDetailOriginatingUniqueId", nvl(esewaTxnId));
            in.put("statuscheck",     "COMPLETE".equalsIgnoreCase(nvl(status)) ? "0" : "1");
            in.put("channelName",     row.optString("channelName", ""));
            in.put("TransactionDate", ts);
            in.put("createdby",       row.optString("core_identifier", ""));
            in.put("modifiedby",      row.optString("core_identifier", ""));
            in.put("createdts",       ts);
            in.put("lastmodifiedts",  ts);
            in.put("synctimestamp",   ts);
            in.put("softdeleteflag",  "0");

            String resp = DBPServiceExecutorBuilder.builder()
                    .withServiceId(HBLURLConstants.HBL_OLB_CRUD_OPERATION_SERVICE)
                    .withOperationId(HBLURLConstants.ESEWA_TRANSACTION_LOG_CREATE)
                    .withRequestParameters(in)
                    .withRequestHeaders(request.getHeaderMap())
                    .build()
                    .getResponse();

            JSONObject j = new JSONObject(StringUtils.defaultIfBlank(resp, "{}"));
            boolean ok = !j.has("errmsg") && !j.has("dbpErrMsg");
            if (ok) {
                logger.info("esewaTransactionLog written by reconciler order={}", referenceId);
            } else {
                logger.error("esewaTransactionLog write FAILED (reconciler) order={} resp={}",
                        referenceId, resp);
            }
            return ok;
        } catch (Exception e) {
            logger.error("esewaTransactionLog write threw (reconciler) order={}: {}",
                    referenceId, e.toString());
            return false;
        }
    }

    public static State stateOf(JSONObject row) {
        return row == null ? null : State.parse(row.optString("BatchId", ""));
    }

    public static int attemptsOf(JSONObject row) {
        try {
            return new JSONObject(row.optString("RawResponse", "{}")).optInt("attempts", 0);
        } catch (Exception e) { return 0; }
    }

    // ---------------------------------------------------------------- helpers

    private static String journal(int attempts, String state, String note, String esewaStatus) {
        return journal(attempts, state, note, esewaStatus, null, null, -1L);
    }

    /**
     * Audit journal stored in RawResponse. "ts" is the authoritative last-touched time for
     * the reconciler's backoff, because lastmodifiedts is DEFAULT_GENERATED only (no
     * "on update CURRENT_TIMESTAMP") and cannot be relied on.
     */
    private static String journal(int attempts, String state, String note, String esewaStatus,
                                  String t24Status, String kind, long elapsedMs) {
        JSONObject j = new JSONObject();
        j.put("attempts", attempts);
        j.put("state", nvl(state));
        j.put("note", nvl(note));
        j.put("esewaStatus", nvl(esewaStatus));
        j.put("t24Status", nvl(t24Status));
        j.put("kind", nvl(kind));
        if (elapsedMs >= 0) { j.put("elapsedMs", elapsedMs); }
        j.put("ts", timestamp());
        String s = j.toString();
        return s.length() > 4900 ? s.substring(0, 4900) : s;   // RawResponse is varchar(5000)
    }

    /** Last-touched time from the journal; the reconciler uses this rather than lastmodifiedts. */
    public static String journalTimestamp(JSONObject row) {
        try {
            return new JSONObject(row.optString("RawResponse", "{}")).optString("ts", "");
        } catch (Exception e) { return ""; }
    }

    private static String str(Map<String, Object> m, String key) {
        if (m == null) { return null; }
        Object v = m.get(key);
        return v == null ? null : String.valueOf(v);
    }

    private static void putIfPresent(JSONObject row, Record rec, String name) {
        try {
            String v = rec.getParamValueByName(name);
            if (v != null) { row.put(name, v); }
        } catch (Exception ignored) { /* column absent */ }
    }

    private static String customerId(DataControllerRequest request) {
        try {
            ServicesManager sm = request.getServicesManager();
            return nvl((String) sm.getIdentityHandler().getUserAttributes().get("customer_id"));
        } catch (Exception e) { return ""; }
    }

    private static String userName(DataControllerRequest request) {
        try {
            return nvl((String) CustomerSessionsUtil.getLoggedInUserAttributesMap(request).get("UserName"));
        } catch (Exception e) { return ""; }
    }

    private static String coreIdentifier(DataControllerRequest request) {
        try { return nvl(HBLCommonUtility.getCoreBackendId(request)); }
        catch (Exception e) { return ""; }
    }

    /**
     * MySQL-safe timestamp: "yyyy-MM-dd HH:mm:ss".
     * LocalDateTime.toString() emits an ISO 'T' separator, which the Fabric/OData layer
     * does not reliably pass through to a TIMESTAMP column - that is why lastmodifiedts
     * was never changing on state updates.
     */
    static String timestamp() {
        return LocalDateTime.now().withNano(0).format(TS_FMT);
    }

    private static final DateTimeFormatter TS_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static String escape(String s) {
        return s == null ? "" : s.replace("'", "''");
    }

    private static String nvl(String s) { return s == null ? "" : s; }

    private static String sha256Hex(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] d = md.digest(input.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : d) { sb.append(String.format("%02x", b)); }
            return sb.toString();
        } catch (Exception e) {
            return Integer.toHexString(input.hashCode());
        }
    }
}
