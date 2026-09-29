package com.infinity.dbx.temenos.transactions;

import static com.infinity.dbx.temenos.transactions.TransactionConstants.TRANSACTION;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

/*
 * HBL changes - extension of T24CompletedTransactionsPostProcessor (parent is not modified).
 *
 * Recent transactions (getCompletedTransactions, v2.3.0) must show exactly the same
 * description as the E-statement (searchTransactionsTimedHBL, v3.0.0).
 *
 * Flow, only when request transactionType = Posted:
 *   1. Call v3.0.0 on PRIMARY_SERVICE_ID.
 *      On exception / error response -> call the same operation on FALLBACK_SERVICE_ID.
 *      If that also fails -> v2.3.0 data is used as-is (normal parent processing).
 *   2. Parent (T24CompletedTransactionsPostProcessor) processes the v2.3.0 rows as today.
 *   3. Each v2.3.0 row is matched to a v3.0.0 row on transactionReference + amount.
 *      On a unique match, description / narrative / notes / displayName are taken from v3.0.0.
 *      The v3.0.0 description has already been built by the description logic on the v3 side,
 *      so it is copied as-is (re-building it would double the text).
 *      Unmatched or ambiguous rows keep the v2.3.0 values.
 * For any other transactionType only the parent logic runs.
 */
public class HblT24CompletedTransactionsPostProcessor extends T24CompletedTransactionsPostProcessor {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    // v3.0.0 (E-statement) services
    private static final String PRIMARY_SERVICE_ID = "ArrangementsT24ISTransactions";
    private static final String FALLBACK_SERVICE_ID = "ArrangementsT24ISTransCustom";
    private static final String TIMED_OPERATION_ID = "searchTransactionsTimedHBL";

    // v3.0.0 input names (same as the E-statement flow)
    private static final String V3_IN_ACCOUNT_ID = "accountID";
    private static final String V3_IN_START_DATE = "searchStartDate";
    private static final String V3_IN_END_DATE = "searchEndDate";
    private static final String V3_IN_LIMIT = "limit";

    // names the v2.3.0 request may carry (first non-blank wins)
    private static final String[] REQ_ACCOUNT_ID = { "accountID", "accountId", "accountNumber" };
    private static final String[] REQ_START_DATE = { "searchStartDate", "dateFrom", "fromDate", "startDate" };
    private static final String[] REQ_END_DATE = { "searchEndDate", "dateTo", "toDate", "endDate" };
    private static final String[] REQ_LIMIT = { "limit", "page_size" };

    private static final String IN_TRANSACTION_TYPE = "transactionType";
    private static final String TXN_TYPE_POSTED = "Posted";

    // Fabric server property holding the limit
    private static final String PROP_LIMIT = "HBL_TXN_ONE_YEAR_LIMIT";

    // record keys
    private static final String KEY_TXN_REF = "transactionReference";   // v3.0.0
    private static final String KEY_TXN_ID = "transactionId";           // v2.3.0 (same value)
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_NARRATIVE = "narrative";
    private static final String KEY_NOTES = "notes";
    private static final String KEY_DISPLAY_NAME = "displayName";

    @Override
    public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {
        List<V3Row> v3Rows = null;
        try {
            if (isPosted(request) && hasRecords(result)) {
                v3Rows = fetchV3Rows(request);   // null = primary and fallback both failed
            }
        } catch (Throwable t) {
            v3Rows = null;
            alert.prepareError("HBL## v3 enrichment skipped, using v2.3.0 data: " + t).log();
        }

        // existing feature: v2.3.0 rows processed exactly as today
        Result finalResult = super.execute(result, request, response);

        if (v3Rows != null && !v3Rows.isEmpty()) {
            try {
                applyV3Rows(finalResult, v3Rows);
            } catch (Throwable t) {
                alert.prepareError("HBL## v3 values not applied, using v2.3.0 data: " + t).log();
            }
        }
        return finalResult;
    }

    /* ---------------------------------------------------------------- v3 call with fallback */

    private List<V3Row> fetchV3Rows(DataControllerRequest request) {
        String accountId = firstParam(request, REQ_ACCOUNT_ID);
        if (accountId.isEmpty()) {
            alert.prepareError("HBL## v3 skipped: account id not found in request").log();
            return null;
        }

        Map<String, Object> params = new HashMap<>();
        params.put(V3_IN_ACCOUNT_ID, accountId);
        putIfNotBlank(params, V3_IN_START_DATE, firstParam(request, REQ_START_DATE));
        putIfNotBlank(params, V3_IN_END_DATE, firstParam(request, REQ_END_DATE));
        String limit = firstParam(request, REQ_LIMIT);
        putIfNotBlank(params, V3_IN_LIMIT, limit.isEmpty() ? serverProperty(request, PROP_LIMIT) : limit);
        alert.prepareError("HBL## v3 request params: " + params).log();

        JSONArray rows = callWithValidation(PRIMARY_SERVICE_ID, params, request);
        if (rows == null) {
            alert.prepareError("HBL## " + PRIMARY_SERVICE_ID + "/" + TIMED_OPERATION_ID
                    + " failed. Retrying with " + FALLBACK_SERVICE_ID).log();
            rows = callWithValidation(FALLBACK_SERVICE_ID, params, request);
        }
        if (rows == null) {
            alert.prepareError("HBL## primary and fallback v3 services failed, using v2.3.0 data").log();
            return null;
        }
        return toV3Rows(rows);
    }

    /* Returns the Transactions array, or null on exception / error response. */
    private JSONArray callWithValidation(String serviceId, Map<String, Object> params,
            DataControllerRequest request) {
        try {
            String raw = callService(serviceId, TIMED_OPERATION_ID, params, request);
            if (StringUtils.isBlank(raw)) {
                alert.prepareError("HBL## " + serviceId + " returned empty response").log();
                return null;
            }
            JSONObject json = new JSONObject(raw);
            String error = responseError(json);
            if (error != null) {
                alert.prepareError("HBL## " + serviceId + " error response: " + error).log();
                return null;
            }
            JSONArray arr = json.optJSONArray(TRANSACTION);
            if (arr == null) {
                alert.prepareError("HBL## " + serviceId + " response has no " + TRANSACTION + " array").log();
                return null;
            }
            return arr;
        } catch (Throwable t) {
            alert.prepareError("HBL## " + serviceId + "/" + TIMED_OPERATION_ID + " failed ("
                    + t.getClass().getSimpleName() + ": " + t.getMessage() + ")").log();
            return null;
        }
    }

    private String callService(String serviceId, String operationId, Map<String, Object> serviceParams,
            DataControllerRequest dcRequest) throws Exception {
        return DBPServiceExecutorBuilder.builder()
                .withServiceId(serviceId)
                .withOperationId(operationId)
                .withRequestParameters(serviceParams)
                .withRequestHeaders(dcRequest.getHeaderMap())
                .withDataControllerRequest(dcRequest)
                .build()
                .getResponse();
    }

    /* Fabric often returns errors without throwing. Returns null when the response is OK. */
    private String responseError(JSONObject json) {
        String op = clean(json.optString("opstatus"));
        if (!op.isEmpty() && !"0".equals(op)) {
            return "opstatus=" + op + " " + clean(json.optString("errmsg"));
        }
        String http = clean(json.optString("httpStatusCode"));
        if (!http.isEmpty() && !"0".equals(http) && !http.startsWith("2")) {
            return "httpStatusCode=" + http;
        }
        for (String key : new String[] { "errmsg", "dbpErrMsg", "errorMessage" }) {
            String msg = clean(json.optString(key));
            if (!msg.isEmpty()) {
                return key + "=" + msg;
            }
        }
        return null;
    }

    /* ---------------------------------------------------------------- matching */

    private static final class V3Row {
        final String ref;
        final String amount;
        final String description;
        final String narrative;
        final String notes;
        final String displayName;

        V3Row(String ref, String amount, String description, String narrative, String notes, String displayName) {
            this.ref = ref;
            this.amount = amount;
            this.description = description;
            this.narrative = narrative;
            this.notes = notes;
            this.displayName = displayName;
        }

        boolean sameText(V3Row o) {
            return description.equals(o.description) && narrative.equals(o.narrative)
                    && notes.equals(o.notes) && displayName.equals(o.displayName);
        }
    }

    private static final V3Row AMBIGUOUS = new V3Row("", "", "", "", "", "");

    private List<V3Row> toV3Rows(JSONArray arr) {
        List<V3Row> rows = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject o = arr.optJSONObject(i);
            if (o == null) {
                continue;
            }
            String ref = clean(o.optString(KEY_TXN_REF));
            if (ref.isEmpty()) {
                ref = clean(o.optString(KEY_TXN_ID));
            }
            String amount = normAmount(o.optString(KEY_AMOUNT));
            if (ref.isEmpty() || amount.isEmpty()) {
                continue;
            }
            String displayName = clean(o.optString(KEY_DISPLAY_NAME));
            String description = clean(o.optString(KEY_DESCRIPTION));
            if (description.isEmpty()) {
                description = displayName;   // no narrative / notes -> displayName
            }
            rows.add(new V3Row(ref, amount, description,
                    clean(o.optString(KEY_NARRATIVE)), clean(o.optString(KEY_NOTES)), displayName));
        }
        return rows;
    }

    private void applyV3Rows(Result finalResult, List<V3Row> v3Rows) {
        Dataset ds = finalResult != null ? finalResult.getDatasetById(TRANSACTION) : null;
        List<Record> records = ds != null ? ds.getAllRecords() : null;
        if (records == null || records.isEmpty()) {
            return;
        }

        // index on transactionReference + amount; same key with different text -> ambiguous
        Map<String, V3Row> index = new HashMap<>(v3Rows.size() * 2);
        for (V3Row row : v3Rows) {
            String key = row.ref + "|" + row.amount;
            V3Row existing = index.get(key);
            if (existing == null) {
                index.put(key, row);
            } else if (existing != AMBIGUOUS && !existing.sameText(row)) {
                index.put(key, AMBIGUOUS);
            }
        }

        // phase 1: resolve all matches (nothing written yet)
        List<Record> matchedRecords = new ArrayList<>();
        List<V3Row> matchedRows = new ArrayList<>();
        for (Record record : records) {
            String ref = clean(record.getParamValueByName(KEY_TXN_REF));
            if (ref.isEmpty()) {
                ref = clean(record.getParamValueByName(KEY_TXN_ID));
            }
            String amount = normAmount(record.getParamValueByName(KEY_AMOUNT));
            if (ref.isEmpty() || amount.isEmpty()) {
                continue;
            }
            V3Row match = index.get(ref + "|" + amount);
            if (match == null || match == AMBIGUOUS || match.description.isEmpty()) {
                continue;   // keep v2.3.0 values
            }
            matchedRecords.add(record);
            matchedRows.add(match);
        }

        // phase 2: write v3 values
        for (int i = 0; i < matchedRecords.size(); i++) {
            Record record = matchedRecords.get(i);
            V3Row v3 = matchedRows.get(i);
            setValue(record, KEY_DESCRIPTION, v3.description);
            setValue(record, KEY_NARRATIVE, v3.narrative);
            setValue(record, KEY_NOTES, v3.notes);
            setValue(record, KEY_DISPLAY_NAME, v3.displayName);
        }
        alert.prepareError("HBL## v3 values applied " + matchedRecords.size() + "/" + records.size()
                + " (v3 rows " + v3Rows.size() + ")").log();
    }

    /* ---------------------------------------------------------------- helpers */

    private boolean isPosted(DataControllerRequest request) {
        String type = request != null ? clean(request.getParameter(IN_TRANSACTION_TYPE)) : "";
        return TXN_TYPE_POSTED.equalsIgnoreCase(type);
    }

    private boolean hasRecords(Result result) {
        Dataset ds = result != null ? result.getDatasetById(TRANSACTION) : null;
        return ds != null && ds.getAllRecords() != null && !ds.getAllRecords().isEmpty();
    }

    private String firstParam(DataControllerRequest request, String[] names) {
        for (String name : names) {
            String v = clean(request.getParameter(name));
            if (!v.isEmpty()) {
                return v;
            }
        }
        return "";
    }

    private String serverProperty(DataControllerRequest request, String key) {
        try {
            ConfigurableParametersHelper helper = request.getServicesManager().getConfigurableParametersHelper();
            return helper != null ? clean(helper.getServerProperty(key)) : "";
        } catch (Exception e) {
            alert.prepareError("HBL## unable to read server property " + key + ": " + e).log();
            return "";
        }
    }

    private void putIfNotBlank(Map<String, Object> params, String key, String value) {
        if (value != null && !value.isEmpty()) {
            params.put(key, value);
        }
    }

    /* Signed, so a debit and its reversal (e.g. -11 / 11) stay separate. */
    private String normAmount(String amount) {
        String amt = clean(amount).replace(",", "");
        if (amt.isEmpty()) {
            return "";
        }
        try {
            return new BigDecimal(amt).stripTrailingZeros().toPlainString();
        } catch (Exception e) {
            return amt;
        }
    }

    private String clean(String value) {
        if (value == null) {
            return "";
        }
        String v = value.trim();
        return "null".equalsIgnoreCase(v) ? "" : v;
    }

    /* Updates an existing param (blank allowed) or adds it when there is a value. */
    private void setValue(Record record, String name, String value) {
        String v = value == null ? "" : value;
        Param param = record.getParam(name);
        if (param != null) {
            param.setValue(v);
        } else if (!v.isEmpty()) {
            record.addParam(name, v);
        }
    }
}
