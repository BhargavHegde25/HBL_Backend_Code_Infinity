package com.bct.eSewa;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.bct.eSewa.EsewaLoadStateDao.ClaimResult;
import com.bct.eSewa.EsewaLoadStateDao.State;
import com.bct.utilities.HBLCommonUtility;
import com.bct.utilities.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbx.util.CommonUtils;
import com.kony.eum.dbputilities.kms.KMSUtil;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.konylabs.middleware.registry.AppRegistryException;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;

/**
 * eSewa wallet load, orchestrated safely.
 *
 * Bind the Fabric operation to THIS class instead of eSewaLoadAmountService.
 * Nothing in the existing codebase is modified; rollback is re-pointing the
 * binding.
 *
 * Sequence: 1. Reject a blank referenceId. Without it there is no idempotency
 * key and no T24 status. 2. Claim the transaction (deterministic primary key)
 * BEFORE anything moves. 3. Poll T24/TPH within a deadline that reserves enough
 * budget for the eSewa call. 4. Load eSewa ONLY on an explicit Complete, and
 * only with budget left to finish. 5. Resolve every outcome - including a
 * timeout - into a durable state.
 *
 * Rules that are structural, not incidental: - A blank, unknown, errored or
 * unreachable T24 status NEVER loads eSewa. - "Placed" is never a failure and
 * never triggers a reversal. It is racing the payment engine; reversing it can
 * silently lose the customer's money. - An eSewa timeout is an absence of
 * information: no retry, no reversal. Resolved by status enquiry in the
 * reconciler. - A reversal is attempted only when BOTH legs are terminal and
 * known, and its outcome is recorded rather than logged and discarded.
 */
public class eSewaLoadAmountService implements JavaService2 {

	private static final Logger logger = LogManager.getLogger(ESewaLoadAmountServiceExtn.class);

	private static final String REVERSAL_SERVICE = "HBLCreateExternalTransfers";
	private static final String REVERSAL_OPERATION = "reverseTransaction";

	/** eSewa itself is still settling - the wallet may yet be credited. */
	private static final String MSG_PROCESSING = "Your top-up is being processed. If your account was debited it will be completed "
			+ "or refunded automatically. Please check back shortly.";
	/**
	 * T24 was not confirmed Complete inside the request window. Under the current policy the
	 * wallet is credited ONLY during the live request, so this can no longer end in a
	 * successful top-up - it can only be refunded. The message must not promise completion.
	 */
	private static final String MSG_NOT_COMPLETED = "Your top-up could not be completed in time. If your account was debited "
			+ "the amount will be refunded automatically. Please try again.";
	private static final String MSG_DECLINED = "Your top-up could not be completed. Your account has not been debited.";
	private static final String MSG_UNAVAILABLE = "We are unable to process your top-up right now. Please try again later.";
	private static final String MSG_REVERSED = "Sorry, your top-up could not be processed. The amount has been refunded to your account.";
	private static final String MSG_REVERSAL_FAILED = "Sorry, your top-up could not be processed. If your account was debited, our team is "
			+ "processing your refund.";

	private final EsewaLoadStateDao dao = new EsewaLoadStateDao();

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {

		final long startedAt = System.nanoTime();
		final Result result = new Result();

		final String referenceId = request.getParameter("referenceId");
		final String frmAccNumber = request.getParameter("frmAccNumber");
		final String amount = request.getParameter("amount");
		final String eSewaId = request.getParameter("eSewaId");
		final String fee = request.getParameter("Fee");
		final String paymentDesc = request.getParameter("paymentDesc");
		final String field1 = request.getParameter("field1");
		final String field2 = request.getParameter("field2");
		final String transactionId = request.getParameter("transactionId");
		final String paymentRefParam = request.getParameter("paymentReferenceId");

		String frmAccName = request.getParameter("frmAccName");
		if (StringUtils.isBlank(frmAccName)) {
			frmAccName = "NA";
		}

		logger.info("eSewa load request order={} account={} amount={} channel={}", referenceId, mask(frmAccNumber),
				amount, safeChannel(request));

		// -------------------------------------------------- 1. mandatory reference
		if (StringUtils.isBlank(referenceId)) {
			logger.error("eSewa load rejected: referenceId is blank - no idempotency key, no T24 status");
			return decline(result, MSG_UNAVAILABLE);
		}

		final Budget budget = Budget.from(request, startedAt);
		final String swiftCode = serverProperty(request, "ESEWA_SWIFT_CODE");
		final String channel = safeChannel(request);

		// -------------------------------------------------- 2. claim (idempotency +
		// crash recovery)
		ClaimResult claim = dao.claim(request, referenceId, amount, eSewaId, frmAccNumber, field1, swiftCode, fee,
				channel, paymentDesc, frmAccName);

		if (claim == ClaimResult.ALREADY_OWNED) {
			JSONObject existing = dao.findByReferenceId(request, referenceId);
			State existingState = EsewaLoadStateDao.stateOf(existing);
			logger.warn("eSewa load duplicate order={} existingState={}", referenceId, existingState);
			return respondForExistingState(result, existingState);
		}
		if (claim == ClaimResult.ERROR) {
			// Fail safe: without a durable record we could not recover from a timeout.
			logger.error("eSewa load aborted order={}: state store unavailable", referenceId);
			return decline(result, MSG_UNAVAILABLE);
		}

		// -------------------------------------------------- 3. T24 / TPH status
		T24PaymentStatusPoller.Outcome t24 = T24PaymentStatusPoller.forRequest(request).poll(referenceId, request,
				budget.t24DeadlineNanos());

		logger.info("eSewa load order={} t24={}", referenceId, t24);

		if (t24.isTerminalFailure()) {
			dao.markState(request, referenceId, State.T24_FAILED, t24Fields(t24),
					"TPH terminal failure: currentStatus=" + t24.rawStatus
							+ (StringUtils.isBlank(t24.detail) ? "" : " (" + t24.detail + ")"),
					t24.attempts);
			return decline(result, MSG_DECLINED, "T24_FAILED");
		}

		if (!t24.mayLoadEsewa()) {
			State unresolved = (t24.kind == T24PaymentStatusPoller.OutcomeKind.BUSINESS_STATUS)
					? State.T24_PENDING_UNRESOLVED
					: State.T24_STATUS_UNKNOWN;
			logger.warn("eSewa load NOT started order={} state={} t24={}", referenceId, unresolved, t24);
			dao.markState(request, referenceId, unresolved, t24Fields(t24),
					"not Complete within the request window: currentStatus='" + t24.rawStatus
							+ "' kind=" + t24.kind + " attempts=" + t24.attempts
							+ (StringUtils.isBlank(t24.detail) ? "" : " - " + t24.detail),
					t24.attempts);
			// No reversal here: T24 may not have posted yet. The reconciler settles it -
			// and under the current policy it REFUNDS; it never loads eSewa later.
			return processing(result, MSG_NOT_COMPLETED, unresolved.name());
		}

		// -------------------------------------------------- 4. budget gate
		if (!budget.hasRoomForEsewa()) {
			logger.warn("eSewa load NOT started order={}: only {}ms left, need {}ms", referenceId, budget.remainingMs(),
					budget.esewaReserveMs);
			dao.markState(request, referenceId, State.ESEWA_SKIPPED_NO_BUDGET, t24Fields(t24),
					"T24 Complete but only " + budget.remainingMs() + "ms left, need "
							+ budget.esewaReserveMs + "ms - load not started", t24.attempts);
			return processing(result, MSG_NOT_COMPLETED, State.ESEWA_SKIPPED_NO_BUDGET.name());
		}

		dao.markState(request, referenceId, State.ESEWA_LOAD_INITIATED, t24Fields(t24),
				"T24 Complete, calling eSewa", t24.attempts);

		// -------------------------------------------------- 5. eSewa load
		EsewaPaymentResponse resData;
		try {
			String raw = EsewaValidationClient.performPaymentRequest(request, frmAccNumber, frmAccName, paymentDesc,
					field1, field2, amount, eSewaId, referenceId,
					serverProperty(request, "ESEWA_SERVER_DATA_ENCRYPTION_PUBLICKEY"),
					serverProperty(request, "ESEWA_CLIENT_SIGNATURE_PRIVATEKEY"),
					serverProperty(request, "ESEWA_CLIENT_DATA_ENCRYPTION_PRIVATE_KEY"),
					serverProperty(request, "ESEWA_SERVER_SIGNATURE_PUBLICKEY"),
					serverProperty(request, "ESEWA_CLINET_ID"), swiftCode,
					serverProperty(request, "ESEWA_BASE_URL") + "/api/auth/load");

			resData = parse(raw);
			if (resData == null) {
				return unknownEsewaOutcome(request, result, referenceId, "unparseable or empty response");
			}
		} catch (Exception e) {
			// Timeout, HTTP error, crash mid-call: we do NOT know whether the wallet was
			// credited. Never retry, never reverse. The claim row already exists, so the
			// reconciler can resolve this by status enquiry.
			return unknownEsewaOutcome(request, result, referenceId, e.getClass().getSimpleName());
		}

		return handleEsewaOutcome(request, result, resData, referenceId, transactionId, paymentRefParam,
				t24, swiftCode, eSewaId, field1, amount, fee, frmAccNumber, frmAccName, paymentDesc,
				channel);
	}

	/**
	 * Audit mapping for the T24 leg. StatusCode carries T24's currentStatus; Status is left
	 * for eSewa's own literal, so one pending-log row shows both legs unambiguously.
	 * StatusCode was previously a pure duplicate of Status and is read by nothing.
	 */
	private static Map<String, Object> t24Fields(T24PaymentStatusPoller.Outcome t24) {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("StatusCode", nvl(t24.rawStatus));
		m.put("__kind", String.valueOf(t24.kind));   // journal only, stripped before the write
		return m;
	}

	// ---------------------------------------------------------------- outcome
	// handling

	private Object handleEsewaOutcome(DataControllerRequest request, Result result, EsewaPaymentResponse resData,
			String referenceId, String transactionId, String paymentRefParam,
			T24PaymentStatusPoller.Outcome t24, String swiftCode,
			String eSewaId, String receiverName, String amount, String fee, String frmAccNumber, String frmAccName,
			String paymentDesc, String channel) {

		final String paymentSystemId = t24.paymentSystemId;
		final int t24Attempts = t24.attempts;
		final String status = nvl(resData.getStatus());
		final String responseCode = nvl(resData.getResponse_code());
		final String txStatus = detail(resData, "status");
		final String uniqueId = detail(resData, "originating");
		final String esewaTxnId = detail(resData, "transaction");

		logger.info("eSewa load order={} responseCode={} transactionStatus={} esewaTxnId={}", referenceId, responseCode,
				txStatus, esewaTxnId);

		// Audit mapping: Status = eSewa, StatusCode = T24. Do NOT overwrite StatusCode with
		// the eSewa status or the T24 leg is lost from the row.
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("Status", status);
		fields.put("ResponseCode", responseCode);
		fields.put("StatusCode", nvl(t24.rawStatus));
		fields.put("TransactionStatus", txStatus);
		fields.put("TransactionDetailOriginatingUniqueId", esewaTxnId);

		// ---- success
		if ("ELR000".equalsIgnoreCase(responseCode) && "COMPLETE".equalsIgnoreCase(txStatus)) {
			dao.markState(request, referenceId, State.ESEWA_SUCCESS, fields, "load complete", t24Attempts);

			// The daily/monthly limit checks read esewaTransactionLog, so it must still be
			// written.
			writeTransactionLog(request, referenceId, swiftCode, eSewaId, receiverName, amount, uniqueId, status,
					responseCode, resData, frmAccNumber, frmAccName, paymentDesc, txStatus, esewaTxnId, channel, fee);

			try {
				eSewaLoadAmountService.sendeSewaEmailToCustomer(request, result, resData);
			} catch (Exception e) {
				logger.warn("eSewa confirmation email failed order={}: {}", referenceId, e.toString());
			}

//			result.setParam (new Param("transaction_id", esewaTxnId));
//			result.setParam(new Param("response_code", responseCode));
//			result.setParam(new Param("transaction_status", txStatus));
//			result.setParam(new Param("originating_unique_id", uniqueId));
//			result.setParam(new Param("responseData", String.valueOf(resData)));
//			result.setParam(new Param("status", status));
			result.addParam(new Param("transaction_id", esewaTxnId));
			result.addParam(new Param("response_code", responseCode));
			result.addParam(new Param("transaction_status", txStatus));
			result.addParam(new Param("originating_unique_id", uniqueId));
			result.addParam(new Param("responseData", String.valueOf(resData)));
			result.addParam(new Param("status", status));
			result.addParam(new Param("opstatus", "0"));
			result.addParam(new Param("httpStatusCode", "200"));
//			ok(result);
			logger.error("eSewa result "+ResultToJSON.convert(result));
			return result;
		}

		// ---- terminal eSewa failure: both legs known, reversal is correct and safe
		if ("FAILED".equalsIgnoreCase(txStatus) || "NOT_FOUND".equalsIgnoreCase(txStatus)) {
			dao.markState(request, referenceId, State.ESEWA_FAILED_CONFIRMED, fields,
					"eSewa terminal failure: transaction_status=" + txStatus
							+ " response_code=" + responseCode
							+ (StringUtils.isBlank(resData.getMessage()) ? "" : " msg=" + resData.getMessage()),
					t24Attempts);
			writeTransactionLog(request, referenceId, swiftCode, eSewaId, receiverName, amount, uniqueId, status,
					responseCode, resData, frmAccNumber, frmAccName, paymentDesc, txStatus, esewaTxnId, channel, fee);

			String reversalKey = StringUtils.isNotBlank(paymentSystemId) ? paymentSystemId : nvl(paymentRefParam);
			boolean reversed = false;

			if (StringUtils.isBlank(reversalKey)) {
				// Not attempted: no key to reverse against. The reconciler re-reads
				// paymentSystemId from T24, so leave this as owed rather than failed.
				logger.warn("eSewa reversal deferred order={}: no paymentSystemId - reconciler will retry",
						referenceId);
				dao.markState(request, referenceId, State.ESEWA_FAILED_CONFIRMED, fields,
						"reversal deferred - no paymentSystemId available", t24Attempts);
			} else {
				reversed = reverse(request, referenceId, reversalKey, transactionId);
				dao.markState(request, referenceId, reversed ? State.REVERSED : State.REVERSAL_FAILED, fields,
						reversed ? "reversal confirmed" : "reversal not confirmed - reconciler will retry",
						t24Attempts);
				if (!reversed) {
					logger.error("eSewa reversal FAILED order={} paymentSystemId={} - reconciler will retry",
							referenceId, reversalKey);
				}
			}
			try {
				eSewaLimitCheck.createEsewaTransactionReprocessLog(request, uniqueId);
			} catch (Exception ignored) {
				/* best effort */ }

			String customerMsg = reversed ? MSG_REVERSED : MSG_REVERSAL_FAILED;
			result.setParam(new Param("status", "FAILED"));
			result.setParam(new Param("reason", reversed ? "ESEWA_FAILED_REVERSED" : "ESEWA_FAILED_REVERSAL_PENDING"));
			result.setParam(new Param("response_code", responseCode));
			result.setParam(new Param("transaction_status", txStatus));
			result.setParam(new Param("esewa_message", nvl(resData.getMessage())));
			result.setParam(new Param("originating_unique_id", uniqueId));
			result.setParam(new Param("transaction_id", esewaTxnId));
			result.setParam(new Param("message", customerMsg));
			result.addParam("errmsg", customerMsg);
			return ok(result);
		}

		// ---- eSewa in flight
		if ("PENDING".equalsIgnoreCase(txStatus) || "AMBIGUOUS".equalsIgnoreCase(txStatus)
				|| "PARTIAL_COMPLETE".equalsIgnoreCase(txStatus)) {
			dao.markState(request, referenceId, State.ESEWA_PENDING, fields,
					"awaiting eSewa: transaction_status=" + txStatus + " response_code=" + responseCode,
					t24Attempts);
			writeTransactionLog(request, referenceId, swiftCode, eSewaId, receiverName, amount, uniqueId, status,
					responseCode, resData, frmAccNumber, frmAccName, paymentDesc, txStatus, esewaTxnId, channel, fee);
			return processing(result);
		}

		// ---- anything else: unknown, treat as unresolved (never as success, never as
		// reversal)
		logger.error("eSewa load order={} UNRECOGNISED outcome responseCode={} transactionStatus={}", referenceId,
				responseCode, txStatus);
		dao.markState(request, referenceId, State.ESEWA_OUTCOME_UNKNOWN, fields,
				"unrecognised eSewa outcome: transaction_status='" + txStatus + "' response_code='" + responseCode
						+ "'" + (StringUtils.isBlank(resData.getMessage()) ? "" : " msg=" + resData.getMessage()),
				t24Attempts);
		return processing(result);
	}

	private Object unknownEsewaOutcome(DataControllerRequest request, Result result, String referenceId,
			String reason) {
		logger.error("eSewa load order={} OUTCOME UNKNOWN ({}) - no retry, no reversal", referenceId, reason);
		dao.markState(request, referenceId, State.ESEWA_OUTCOME_UNKNOWN, null, reason, 0);
		return processing(result);
	}

	// ---------------------------------------------------------------- reversal

	/** @return true only when the reversal is confirmed successful. */
	private boolean reverse(DataControllerRequest request, String referenceId, String paymentReferenceId,
			String transactionId) {
		if (StringUtils.isBlank(paymentReferenceId)) {
			logger.error("eSewa reversal skipped order={}: no paymentSystemId available", referenceId);
			return false;
		}
		try {
			Map<String, Object> in = new HashMap<String, Object>();
			in.put("paymentReferenceId", paymentReferenceId);
			in.put("referenceId", referenceId);
			in.put("transactionId", nvl(transactionId));
			request.addRequestParam_("paymentReferenceId", paymentReferenceId);
			request.addRequestParam_("referenceId", referenceId);
			request.addRequestParam_("transactionId", nvl(transactionId));

			Result r = CommonUtils.callIntegrationService(request, new HashMap<String, Object>(in),
					request.getHeaderMap(), REVERSAL_SERVICE, REVERSAL_OPERATION, true);

			if (r == null) {
				return false;
			}
			if (StringUtils.isNotBlank(r.getParamValueByName("dbpErrCode"))
					|| StringUtils.isNotBlank(r.getParamValueByName("dbpErrMsg"))) {
				return false;
			}
			String status = nvl(r.getParamValueByName("status"));
			return "Reversed".equalsIgnoreCase(status) || "success".equalsIgnoreCase(status);
		} catch (Exception e) {
			logger.error("eSewa reversal threw order={}: {}", referenceId, e.toString());
			return false;
		}
	}

	// ---------------------------------------------------------------- budget

	static final class Budget {
		final long startedAtNanos;
		final long totalBudgetMs;
		final long esewaReserveMs;

		Budget(long startedAtNanos, long totalBudgetMs, long esewaReserveMs) {
			this.startedAtNanos = startedAtNanos;
			this.totalBudgetMs = totalBudgetMs;
			this.esewaReserveMs = esewaReserveMs;
		}

		static Budget from(DataControllerRequest request, long startedAtNanos) {
			long total = longProperty(request, "ESEWA_REQUEST_TOTAL_BUDGET_MS", 30000L);
			long reserve = longProperty(request, "ESEWA_LOAD_RESERVE_MS", 20000L);
			if (reserve >= total) {
				reserve = Math.max(1000L, total / 2);
			}
			return new Budget(startedAtNanos, total, reserve);
		}

		/**
		 * No T24 attempt may START after this point; the remainder belongs to eSewa.
		 */
		long t24DeadlineNanos() {
			return startedAtNanos + ((totalBudgetMs - esewaReserveMs) * 1_000_000L);
		}

		long remainingMs() {
			return totalBudgetMs - ((System.nanoTime() - startedAtNanos) / 1_000_000L);
		}

		/** The guard that makes it impossible to start a load that cannot finish. */
		boolean hasRoomForEsewa() {
			return remainingMs() >= esewaReserveMs;
		}
	}

	// ---------------------------------------------------------------- responses

	private static Object ok(Result r) {
		r.setParam(new Param("opstatus", "0"));
		r.setParam(new Param("httpStatusCode", "200"));
		return r;
	}

	private static Object processing(Result r) {
		return processing(r, MSG_PROCESSING, "ESEWA_PENDING");
	}

	/** @param reason machine-readable cause so the channel can tell the outcomes apart. */
	private static Object processing(Result r, String message, String reason) {
		r.setParam(new Param("status", "PROCESSING"));
		r.setParam(new Param("reason", nvl(reason)));
		r.setParam(new Param("message", message));
		r.addParam("errmsg", message);
		return ok(r);
	}

	private static Object decline(Result r, String message) {
		return decline(r, message, "DECLINED");
	}

	private static Object decline(Result r, String message, String reason) {
		r.setParam(new Param("response_code", ErrorCodeEnum.ERR_21210.getErrorCodeAsString()));
		r.setParam(new Param("status", "FAILED"));
		r.setParam(new Param("reason", nvl(reason)));
		r.setParam(new Param("message", message));
		r.addParam("errmsg", message);
		return ok(r);
	}

	private static Object respondForExistingState(Result r, State state) {
		if (state == State.ESEWA_SUCCESS) {
			r.setParam(new Param("status", "COMPLETE"));
			r.setParam(new Param("message", "This top-up has already been completed."));
			return ok(r);
		}
		if (state == State.T24_FAILED || state == State.REVERSED) {
			return decline(r, MSG_DECLINED);
		}
		return processing(r);
	}

	// ---------------------------------------------------------------- helpers

	private void writeTransactionLog(DataControllerRequest request, String referenceId, String swiftCode,
			String eSewaId, String receiverName, String amount, String uniqueId, String status, String responseCode,
			EsewaPaymentResponse resData, String frmAccNumber, String frmAccName, String paymentDesc, String txStatus,
			String esewaTxnId, String channel, String fee) {
		try {
			String statuscheck = "COMPLETE".equalsIgnoreCase(status) ? "0" : "1";
			EsewaValidationClient.esewaTransactionLog(request, referenceId, // BatchId: the value actually sent to eSewa
					nvl(swiftCode), nvl(eSewaId), nvl(receiverName), nvl(amount),
					StringUtils.defaultIfBlank(uniqueId, referenceId), status, responseCode, status,
					String.valueOf(resData), nvl(frmAccNumber), nvl(frmAccName), "", "", nvl(paymentDesc), txStatus,
					esewaTxnId, statuscheck, nvl(channel), nvl(fee));
		} catch (Exception e) {
			logger.error("esewaTransactionLog write FAILED order={}: {}", referenceId, e.toString());
		}
	}

	private static EsewaPaymentResponse parse(String raw) {
		if (StringUtils.isBlank(raw) || "Fail".equalsIgnoreCase(raw.trim())) {
			return null;
		}
		try {
			EsewaPaymentResponse r = new ObjectMapper().readValue(raw, EsewaPaymentResponse.class);
			return (r == null || r.getEsewa_load_transaction_detail() == null
					|| r.getEsewa_load_transaction_detail().isEmpty()) ? null : r;
		} catch (Exception e) {
			return null;
		}
	}

	private static String detail(EsewaPaymentResponse r, String which) {
		try {
			EsewaPaymentResponse.EsewaLoadTransactionDetail d = r.getEsewa_load_transaction_detail().get(0);
			if ("status".equals(which)) {
				return nvl(d.getTransaction_status());
			}
			if ("originating".equals(which)) {
				return nvl(d.getOriginating_unique_id());
			}
			return nvl(d.getTransaction_id());
		} catch (Exception e) {
			return "";
		}
	}

	/** Retained for ad-hoc single-column updates; the T24 leg uses t24Fields(...). */
	@SuppressWarnings("unused")
	private static Map<String, Object> field(String key, String value) {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put(key, nvl(value));
		return m;
	}

	private static String safeChannel(DataControllerRequest request) {
		try {
			return nvl(eSewaLimitCheck.getCurrentChannel(request));
		} catch (Exception e) {
			return "";
		}
	}

	private static String mask(String accountNumber) {
		try {
			if (StringUtils.isBlank(accountNumber) || accountNumber.length() < 5) {
				return "****";
			}
			return HBLCommonUtility.maskAccountNumber(accountNumber, 0, accountNumber.length() - 4, "X");
		} catch (Exception e) {
			return "****";
		}
	}

	static String serverProperty(DataControllerRequest request, String key) {
		try {
			ServicesManager sm = request.getServicesManager();
			ConfigurableParametersHelper p = sm.getConfigurableParametersHelper();
			return nvl(p.getServerProperty(key));
		} catch (Exception e) {
			return "";
		}
	}

	static long longProperty(DataControllerRequest request, String key, long def) {
		try {
			String v = serverProperty(request, key);
			return StringUtils.isBlank(v) ? def : Long.parseLong(v.trim());
		} catch (Exception e) {
			return def;
		}
	}

	private static String nvl(String s) {
		return s == null ? "" : s;
	}
	
	public static void sendeSewaEmailToCustomer(DataControllerRequest request, Result result, EsewaPaymentResponse res)
			throws HttpCallException, AppRegistryException {
		String customerId = ArrangementsUtils.getUserAttributeFromIdentity(request, "customer_id");
		logger.debug("customerId :###" + customerId);
		String email = Utils.customerEmailFromSession(request);
		logger.debug("email :###" + email);

		// Transaction Id
		String transactionId = res.getEsewa_load_transaction_detail().get(0).getTransaction_id();
		// Transaction Status
		String transactionStatus = res.getEsewa_load_transaction_detail().get(0).getTransaction_status();
		// Transaction Purpose
		String transactionPurpose = request.getParameter("paymentDesc");
		// Transaction Amount
		String transactionAmount = request.getParameter("amount");
		// Transaction Date
		Date date = new Date();
		String transactionDate = new SimpleDateFormat("yyyy/MM/dd").format(date);
		logger.debug("requestDate ##:" + transactionDate);
		// Reference Id
		String referenceId = res.getEsewa_load_transaction_detail().get(0).getOriginating_unique_id();
		// Bank Name
		String bankName = "Himalayan Bank Ltd";
		// Account Name
		String accountName = request.getParameter("frmAccName");
		// Account Number
		String accountNumber = request.getParameter("frmAccNumber");
		// Receiver Name
		String receiverName = request.getParameter("field1");
		// eSewa Id
		String eSewaId = request.getParameter("eSewaId");
		String emailTemplate = "emailTempleteeSewaLoad";
		accountNumber = HBLCommonUtility.maskAccountNumber(accountNumber, 0, accountNumber.length() - 4, "X");

		logger.debug("triggerEmail value:" + email);
		Map<String, String> input = new HashMap<>();
		input.put("Subscribe", "true");
		input.put("EmailType", emailTemplate);
		JSONObject addContext = new JSONObject();
		addContext.put("transactionId", transactionId);
		addContext.put("transactionStatus", transactionStatus);
		addContext.put("transactionPurpose", transactionPurpose);
		addContext.put("transactionAmount", transactionAmount);
		addContext.put("transactionDate", transactionDate);
		addContext.put("referenceId", referenceId);
		addContext.put("bankName", bankName);
		addContext.put("accountName", accountName);
		addContext.put("accountNumber", accountNumber);
		addContext.put("receiverName", receiverName);
		addContext.put("eSewaId", eSewaId);

		input.put("AdditionalContext", KMSUtil.getOTPContent(null, null, addContext));
		input.put("Email", email);
		Map<String, String> headers = HelperMethods.getHeaders(request);
		headers.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
		HelperMethods.callApi(request, input, headers, URLConstants.DBX_SEND_EMAIL_ORCH);
	}
}


