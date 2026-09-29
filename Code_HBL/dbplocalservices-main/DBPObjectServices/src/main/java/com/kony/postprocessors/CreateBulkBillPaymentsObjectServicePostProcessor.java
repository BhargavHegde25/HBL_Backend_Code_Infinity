package com.kony.postprocessors;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.utilities.HelperMethods;
import com.kony.utilities.ObjectServiceHelperMethods;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;

public class CreateBulkBillPaymentsObjectServicePostProcessor
        implements ObjectServicePostProcessor, ObjectServicesConstants {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager) throws Exception {
		Log4j2Configurator.getInstance();

        try {
            PayloadHandler responsePayloadHandler = responseManager.getPayloadHandler();
            PayloadHandler requestPayloadHandler = requestManager.getPayloadHandler();
            JsonObject responsePayload = responsePayloadHandler.getPayloadAsJson().getAsJsonObject();
            JsonObject requestPayload = requestPayloadHandler.getPayloadAsJson().getAsJsonObject();
            JsonObject customParams = new JsonObject();
            String opstatus = "";
            String customerid = HelperMethods.getCustomerIdFromSession(requestManager);
            String eventType = PARAM_MAKE_TRANSFER;
            String eventSubType = PARAM_BULK_BILL_PAYMENT;
            String producer = "Transations/createBulkBillPay";
            String statusId = PARAM_SID_EVENT_FAILURE;
            double totalAmount = 0.0;
            String enableEvents = ObjectServiceHelperMethods.getConfigurableParameters(PARAM_ENABLE_EVENTS,
                    requestManager);

            if (ObjectServiceHelperMethods.hasKey(responsePayload, PARAM_OP_STATUS)) {
                opstatus = HelperMethods.getStringFromJsonObject(responsePayload, PARAM_OP_STATUS, true);
            }

            if (opstatus.equals("0") && !ObjectServiceHelperMethods.hasKey(responsePayload, PARAM_DBP_ERR_CODE)) {
                statusId = PARAM_SID_EVENT_SUCCESS;
            }
            diagnostic.prepareDebug("ENABLE_EVENTS=" + enableEvents).log();

            if (enableEvents != null && enableEvents.equalsIgnoreCase(PARAM_TRUE)) {

                if (ObjectServiceHelperMethods.hasKey(requestPayload, PARAM_BULK_BILLPAY_STRING)) {

                    String bulkBillPayString = HelperMethods.getStringFromJsonObject(requestPayload,
                            PARAM_BULK_BILLPAY_STRING, true);
                    JSONArray bulkBillPayments = new JSONArray(bulkBillPayString);
                    if (bulkBillPayments.length() > 0) {
                        JSONObject temp1 = bulkBillPayments.getJSONObject(0);
                        customParams.addProperty(PARAM_BILLER_NAME, temp1.getString(PARAM_PAYEE_ID));
                        customParams.addProperty(PARAM_NO_OF_OTHER_BILLERS, bulkBillPayments.length() - 1);
                    }

                    for (int i = 0; i < bulkBillPayments.length(); i++) {
                        JSONObject payment = bulkBillPayments.getJSONObject(i);
                        String paymentAmount = payment.getString(PARAM_PAID_AMOUNT);
                        totalAmount += Double.parseDouble(paymentAmount);
                    }
                    customParams.addProperty(PARAM_AMOUNT, Double.toString(totalAmount));
                    if (responsePayload.get(PARAM_TRANSACTIONS) != null) {
                        customParams.add(PARAM_ALERTS_TABULAR_DATA,
                                responsePayload.get(PARAM_TRANSACTIONS).getAsJsonObject());
                    }

                }
                try {
                    customParams.addProperty("transactionDate",
                            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(new Date()));
                    
                    AdminUtil.addAdminUserNameRoleIfAvailable(customParams, requestManager);
                    
                    ObjectServiceHelperMethods.execute(new ObjectServiceHelperMethods(requestManager, responseManager,
                            eventType, eventSubType, producer, statusId, null, customerid, customParams));
                } catch (Exception e2) {
                    alert.prepareError("Exception Occured while invoking objectServiceHelperMethods").log();
                }
            }

        } catch (Exception ex) {
            alert.prepareError("exception occured in ObjectService PostProcessor while Creating Bulk Bill Payement=", ex).log();
        }

    }

}
