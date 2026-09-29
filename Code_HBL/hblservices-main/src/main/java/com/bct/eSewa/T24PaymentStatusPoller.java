package com.bct.eSewa;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.controller.DataControllerRequest;

/**
 * Retrieves and classifies the T24 / TPH payment-order status for an eSewa load.
 *
 * Replaces eSewaLoadAmountService.getTransactionStatus() + processOrchServceResponse().
 * Fixes, relative to that code:
 *   - ONE orchestration call per attempt (the old code called it twice, discarding the first).
 *   - Never logs the T24 auth token.
 *   - Matches the LoopDataset row on the payment order id instead of breaking on the
 *     first row whose currentStatus == "Complete".
 *   - NEVER returns a blank status. A blank was the reason the old guard failed open.
 *   - Distinguishes a business status from a technical failure.
 *   - Bounded polling against a wall-clock deadline (TPH sets "Placed" immediately and
 *     settles a few seconds later).
 *
 * Nothing in this class mutates existing code or state.
 */
public final class T24PaymentStatusPoller {

    private static final Logger logger = LogManager.getLogger(T24PaymentStatusPoller.class);

    private static final String T24_STATUS_SERVICE   = "T24-IS-TransactOrch";
    private static final String T24_STATUS_OPERATION = "getTransactionStatus";

    /** Candidate keys in a LoopDataset row that may carry the payment order id. */
    private static final String[] ORDER_ID_KEYS =
            { "paymentOrderId", "paymentId", "id", "transactionReference", "orderId" };

    public enum Classification { SUCCESS, PENDING, FAILURE, UNKNOWN }

    public enum OutcomeKind { BUSINESS_STATUS, TECHNICAL_ERROR, EMPTY_DATASET, MALFORMED }

    // ---------------------------------------------------------------- outcome

    public static final class Outcome {
        public final OutcomeKind    kind;
        public final Classification classification;
        public final String         rawStatus;
        public final String         paymentSystemId;
        public final int            attempts;
        public final long           elapsedMs;
        public final String         detail;

        Outcome(OutcomeKind kind, Classification classification, String rawStatus,
                String paymentSystemId, int attempts, long elapsedMs, String detail) {
            this.kind            = kind;
            this.classification  = classification;
            this.rawStatus       = rawStatus  == null ? "" : rawStatus;
            this.paymentSystemId = paymentSystemId == null ? "" : paymentSystemId;
            this.attempts        = attempts;
            this.elapsedMs       = elapsedMs;
            this.detail          = detail == null ? "" : detail;
        }

        /** The ONLY condition under which an eSewa load may be started. */
        public boolean mayLoadEsewa() {
            return kind == OutcomeKind.BUSINESS_STATUS && classification == Classification.SUCCESS;
        }

        /** Terminal failure in TPH: no load, no reversal, nothing to compensate. */
        public boolean isTerminalFailure() {
            return kind == OutcomeKind.BUSINESS_STATUS && classification == Classification.FAILURE;
        }

        @Override
        public String toString() {
            return "T24Outcome[kind=" + kind + ", class=" + classification + ", status=" + rawStatus
                    + ", attempts=" + attempts + ", elapsedMs=" + elapsedMs
                    + (detail.isEmpty() ? "" : ", detail=" + detail) + "]";
        }
    }

    // ---------------------------------------------------------------- config

    public static final class PollConfig {
        public final int    maxAttempts;
        public final long   initialDelayMs;
        public final long   intervalMs;
        public final double backoffMultiplier;
        public final String successValues;
        public final String pendingValues;
        public final String failureValues;

        PollConfig(int maxAttempts, long initialDelayMs, long intervalMs, double backoffMultiplier,
                   String successValues, String pendingValues, String failureValues) {
            this.maxAttempts       = maxAttempts;
            this.initialDelayMs    = initialDelayMs;
            this.intervalMs        = intervalMs;
            this.backoffMultiplier = backoffMultiplier;
            this.successValues     = successValues;
            this.pendingValues     = pendingValues;
            this.failureValues     = failureValues;
        }

        /**
         * Reads Fabric Server Properties, following the convention already used in
         * eSewaLoadAmountService. Every fallback is the conservative one: fewer attempts,
         * shorter budget. A misconfiguration must degrade toward NOT loading.
         */
        public static PollConfig from(DataControllerRequest request) {
            ConfigurableParametersHelper p = null;
            try {
                p = request.getServicesManager().getConfigurableParametersHelper();
            } catch (Exception e) {
                logger.warn("T24PaymentStatusPoller: server properties unavailable, using defaults");
            }
            return new PollConfig(
                    asInt(p,    "ESEWA_T24_STATUS_MAX_ATTEMPTS",       5),
                    asLong(p,   "ESEWA_T24_STATUS_INITIAL_DELAY_MS",   0L),
                    asLong(p,   "ESEWA_T24_STATUS_RETRY_INTERVAL_MS",  2000L),
                    asDouble(p, "ESEWA_T24_STATUS_BACKOFF_MULTIPLIER", 1.15d),
                    asStr(p,    "ESEWA_T24_STATUS_SUCCESS_VALUES",     "Complete"),
                    asStr(p,    "ESEWA_T24_STATUS_PENDING_VALUES",     "Placed"),
                    asStr(p,    "ESEWA_T24_STATUS_FAILURE_VALUES",     "Fail,Failed,Rejected,Cancelled,Reversed"));
        }
    }

    private final PollConfig cfg;

    public T24PaymentStatusPoller(PollConfig cfg) { this.cfg = cfg; }

    public static T24PaymentStatusPoller forRequest(DataControllerRequest request) {
        return new T24PaymentStatusPoller(PollConfig.from(request));
    }

    // ---------------------------------------------------------------- polling

    /**
     * Polls until the status resolves, the attempt count is spent, or the deadline is reached.
     * Returns as soon as the status is SUCCESS or a terminal FAILURE - it does not wait out
     * the remaining attempts.
     *
     * @param deadlineNanos System.nanoTime() value past which no further attempt may START.
     *                      The caller computes this so that enough budget remains for eSewa.
     */
    public Outcome poll(String paymentOrderId, DataControllerRequest request, long deadlineNanos) {
        final long startedAt = System.nanoTime();
        int attempt = 0;
        Outcome last = null;
        long nextDelay = cfg.initialDelayMs;

        while (attempt < cfg.maxAttempts) {

            if (nextDelay > 0) {
                if (!withinDeadline(deadlineNanos, nextDelay)) {
                    logger.warn("T24 poll: deadline reached before attempt {} for order {}",
                            attempt + 1, paymentOrderId);
                    break;
                }
                sleep(nextDelay);
            }
            if (System.nanoTime() >= deadlineNanos) {
                logger.warn("T24 poll: deadline reached for order {}", paymentOrderId);
                break;
            }

            attempt++;
            last = attemptOnce(paymentOrderId, request, attempt, startedAt);
            logger.info("T24 poll attempt {}/{} order={} -> {}",
                    attempt, cfg.maxAttempts, paymentOrderId, last);

            if (last.mayLoadEsewa() || last.isTerminalFailure()) {
                return last;
            }
            if (last.kind == OutcomeKind.BUSINESS_STATUS
                    && last.classification == Classification.UNKNOWN) {
                // An unrecognised status is a contract or configuration defect, not a
                // business outcome. Stop polling and let reconciliation + alerting handle it.
                logger.error("T24 poll: UNRECOGNISED status '{}' for order {} - treating as unresolved",
                        last.rawStatus, paymentOrderId);
                return last;
            }

            nextDelay = (attempt == 1 && cfg.initialDelayMs == 0L)
                    ? cfg.intervalMs
                    : (long) (nextDelay * cfg.backoffMultiplier);
        }

        long elapsed = msSince(startedAt);
        if (last != null) {
            // Preserve the ACTUAL cause recorded by the final attempt. Replacing it with a
            // generic message discarded the one field needed to diagnose the transaction.
            String cause = StringUtils.isBlank(last.detail)
                    ? "budget or attempts exhausted"
                    : "budget or attempts exhausted after: " + last.detail;
            return new Outcome(last.kind, last.classification, last.rawStatus,
                    last.paymentSystemId, attempt, elapsed, cause);
        }
        return new Outcome(OutcomeKind.TECHNICAL_ERROR, Classification.UNKNOWN, "", "",
                attempt, elapsed, "no attempt could be started within the budget");
    }

    /** Single status read - used by the reconciler, which does its own scheduling. */
    public Outcome checkOnce(String paymentOrderId, DataControllerRequest request) {
        return attemptOnce(paymentOrderId, request, 1, System.nanoTime());
    }

    // ---------------------------------------------------------------- internals

    private Outcome attemptOnce(String paymentOrderId, DataControllerRequest request,
                                int attempt, long startedAt) {
        String responseJson;
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("paymentOrderId", paymentOrderId);

            responseJson = DBPServiceExecutorBuilder.builder()
                    .withServiceId(T24_STATUS_SERVICE)
                    .withObjectId(null)
                    .withOperationId(T24_STATUS_OPERATION)
                    .withRequestParameters(params)
                    .withRequestHeaders(request.getHeaderMap())
                    .withDataControllerRequest(request)
                    .build()
                    .getResponse();
        } catch (Exception e) {
            logger.warn("T24 status call failed for order {}: {}", paymentOrderId, e.toString());
            return new Outcome(OutcomeKind.TECHNICAL_ERROR, Classification.UNKNOWN, "", "",
                    attempt, msSince(startedAt), e.getClass().getSimpleName());
        }

        if (StringUtils.isBlank(responseJson)) {
            return new Outcome(OutcomeKind.EMPTY_DATASET, Classification.UNKNOWN, "", "",
                    attempt, msSince(startedAt), "empty response body");
        }

        JSONArray rows;
        try {
            JSONObject root = new JSONObject(responseJson);
            rows = root.optJSONArray("LoopDataset");
        } catch (Exception e) {
            return new Outcome(OutcomeKind.MALFORMED, Classification.UNKNOWN, "", "",
                    attempt, msSince(startedAt), "unparseable response");
        }

        if (rows == null || rows.length() == 0) {
            // CRITICAL: the old code returned an empty JSONObject here, producing a blank
            // status that slipped past the guard and allowed the load to proceed.
            return new Outcome(OutcomeKind.EMPTY_DATASET, Classification.UNKNOWN, "", "",
                    attempt, msSince(startedAt), "LoopDataset absent or empty");
        }

        JSONObject row = selectRow(rows, paymentOrderId);
        if (row == null) {
            return new Outcome(OutcomeKind.EMPTY_DATASET, Classification.UNKNOWN, "", "",
                    attempt, msSince(startedAt),
                    "no row matched order id among " + rows.length() + " rows");
        }

        String status          = row.optString("currentStatus", "");
        String paymentSystemId = row.optString("paymentSystemId", "");

        if (StringUtils.isBlank(status)) {
            return new Outcome(OutcomeKind.MALFORMED, Classification.UNKNOWN, "", paymentSystemId,
                    attempt, msSince(startedAt), "row carried no currentStatus");
        }

        return new Outcome(OutcomeKind.BUSINESS_STATUS, classify(status), status, paymentSystemId,
                attempt, msSince(startedAt), null);
    }

    /**
     * Picks the row belonging to this payment order. The old code broke on the first row
     * whose status was "Complete" regardless of which payment it described.
     */
    private JSONObject selectRow(JSONArray rows, String paymentOrderId) {
        for (int i = 0; i < rows.length(); i++) {
            JSONObject candidate = rows.optJSONObject(i);
            if (candidate == null) {
                continue;
            }
            for (String key : ORDER_ID_KEYS) {
                String value = candidate.optString(key, "");
                if (StringUtils.isNotBlank(value) && value.equalsIgnoreCase(paymentOrderId)) {
                    return candidate;
                }
            }
        }
        if (rows.length() == 1) {
            // Single row and no id field present: unambiguous, safe to use.
            return rows.optJSONObject(0);
        }
        logger.error("T24 status: {} rows returned and none matched order {} - refusing to guess",
                rows.length(), paymentOrderId);
        return null;
    }

    private Classification classify(String status) {
        if (contains(cfg.successValues, status)) { return Classification.SUCCESS; }
        if (contains(cfg.failureValues, status)) { return Classification.FAILURE; }
        if (contains(cfg.pendingValues, status)) { return Classification.PENDING; }
        return Classification.UNKNOWN;
    }

    private static boolean contains(String csv, String value) {
        if (StringUtils.isBlank(csv)) { return false; }
        for (String token : csv.split(",")) {
            if (token.trim().equalsIgnoreCase(value.trim())) { return true; }
        }
        return false;
    }

    private static boolean withinDeadline(long deadlineNanos, long plannedDelayMs) {
        return System.nanoTime() + (plannedDelayMs * 1_000_000L) < deadlineNanos;
    }

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    private static long msSince(long startNanos) {
        return (System.nanoTime() - startNanos) / 1_000_000L;
    }

    private static String asStr(ConfigurableParametersHelper p, String key, String def) {
        try {
            String v = p == null ? null : p.getServerProperty(key);
            return StringUtils.isBlank(v) ? def : v.trim();
        } catch (Exception e) { return def; }
    }

    private static int asInt(ConfigurableParametersHelper p, String key, int def) {
        try { return Integer.parseInt(asStr(p, key, String.valueOf(def))); }
        catch (Exception e) { return def; }
    }

    private static long asLong(ConfigurableParametersHelper p, String key, long def) {
        try { return Long.parseLong(asStr(p, key, String.valueOf(def))); }
        catch (Exception e) { return def; }
    }

    private static double asDouble(ConfigurableParametersHelper p, String key, double def) {
        try { return Double.parseDouble(asStr(p, key, String.valueOf(def))); }
        catch (Exception e) { return def; }
    }
}
