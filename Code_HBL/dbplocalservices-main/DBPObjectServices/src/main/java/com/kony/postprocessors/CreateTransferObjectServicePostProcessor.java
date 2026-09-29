package com.kony.postprocessors;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonObject;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.memorymgmt.SessionMap;
import com.kony.memorymgmt.TransactionManager;
import com.kony.utilities.HelperMethods;
import com.kony.utilities.ObjectServiceHelperMethods;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;

public class CreateTransferObjectServicePostProcessor implements ObjectServicePostProcessor, ObjectServicesConstants {

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
            
            // Store Transaction Id into cache
            String referenceId = "";
            if (ObjectServiceHelperMethods.hasKey(responsePayload, "referenceId"))
                referenceId = HelperMethods.getStringFromJsonObject(responsePayload, "referenceId", true);

            StoreTransactionIdInSession(requestManager, responseManager, referenceId);

            JsonObject customParams = new JsonObject();
            String statusId = PARAM_SID_EVENT_FAILURE;
            String opstatus = "";
            String customerid = HelperMethods.getCustomerIdFromSession(requestManager);
            String eventSubType = "";
            String eventType = PARAM_MAKE_TRANSFER;
            String producer = "Transactions/POST(createTransfer)";
            String transactionType = "";
            JsonObject mfaAttributes;
            String serviceName = "";
            String frequencyType = "";
            String isScheduled = "";
            String enableEvents = ObjectServiceHelperMethods.getConfigurableParameters(PARAM_ENABLE_EVENTS,
                    requestManager);
            String fromAccountNumber = "";

            if (ObjectServiceHelperMethods.hasKey(requestPayload, PARAM_FROM_ACCOUNT_NUMBER))
                fromAccountNumber = HelperMethods.getStringFromJsonObject(requestPayload, PARAM_FROM_ACCOUNT_NUMBER,
                        true);
            if (ObjectServiceHelperMethods.hasKey(responsePayload, PARAM_OP_STATUS)) {
                opstatus = HelperMethods.getStringFromJsonObject(responsePayload, PARAM_OP_STATUS, true);
            }

            if (opstatus.equals("0") && !ObjectServiceHelperMethods.hasKey(responsePayload, PARAM_DBP_ERR_CODE)) {
                statusId = PARAM_SID_EVENT_SUCCESS;
            }

            if (enableEvents != null && enableEvents.equalsIgnoreCase(PARAM_TRUE)) {
                if (ObjectServiceHelperMethods.hasKey(requestPayload, PARAM_TRANSACTION_TYPE)) {
                    transactionType = HelperMethods
                            .getStringFromJsonObject(requestPayload, PARAM_TRANSACTION_TYPE, true).toLowerCase();
                    if (ObjectServiceHelperMethods.hasKey(requestPayload, PARAM_FREQUENCY_TYPE))
						frequencyType = HelperMethods
								.getStringFromJsonObject(requestPayload, PARAM_FREQUENCY_TYPE, true).toLowerCase();
					else
						frequencyType = "Once";
                    if(ObjectServiceHelperMethods.hasKey(requestPayload, PARAM_IS_SCHEDULED))
                        isScheduled = HelperMethods.getStringFromJsonObject(requestPayload, PARAM_IS_SCHEDULED, true);
                    else
                        isScheduled = "0";
                }
                mfaAttributes = requestPayload.getAsJsonObject(PARAM_MFA_ATRIBUTES);

                if (mfaAttributes != null && ObjectServiceHelperMethods.hasKey(mfaAttributes, PARAM_SERVICE_NAME)) {
                    serviceName = HelperMethods.getStringFromJsonObject(mfaAttributes, PARAM_SERVICE_NAME, true);
                }

                if ((requestPayload != null && requestPayload.has("validate"))
                        && requestPayload.get("validate").getAsString().equalsIgnoreCase(PARAM_TRUE))
                    return;
                // Handling audit log in java resource layer for business/retail
                if (transactionType.equalsIgnoreCase(PARAM_EXTERNAL_TRANSFER)
                        || transactionType.equalsIgnoreCase(PARAM_INTERNAL_TRANSFER))
                    return;

                switch (transactionType) {
                // Handling audit log in java resource layer for business/retail
                // use-cases

                /*case PARAM_INTERNAL_TRANSFER:
                    if (statusId.equals(PARAM_SID_EVENT_SUCCESS)) {
                        customParams = buildCustomParamsForAlertEngine(requestPayload, responsePayload);
                    }
                    eventSubType = deriveSubTypeForInternalTransfer(isScheduled, frequencyType);
                    this.createMoneyMovementRecord(requestManager, responseManager, statusId);
                    break;*/
                 

                case PARAM_EXTERNAL_TRANSFER:
                    if (statusId.equals(PARAM_SID_EVENT_SUCCESS)) {
                        customParams = buildCustomParamsForAlertEngine(requestPayload, responsePayload);
                    }
                    if (StringUtils.isBlank(serviceName) 
                            && (requestPayload != null && requestPayload.has("serviceName")))
                        serviceName = requestPayload.get("serviceName").getAsString();
                    eventSubType = deriveSubTypeForExternalTransfer(isScheduled, frequencyType, serviceName);
                    this.createMoneyMovementRecord(requestManager, responseManager, statusId);
                    break;

                case PARAM_BILLPAY_TRANSFER:

                    eventSubType = deriveSubTypeForBillPayment(requestPayload);

                    break;

                case PARAM_P2P_TRANSFER:
                    if (statusId.equalsIgnoreCase(PARAM_SID_EVENT_SUCCESS)) {
                        customParams = buildCustomParamsForAlertEngine(requestPayload, responsePayload);
                    }

                    eventSubType = deriveSubTypeForP2PTransfer(requestPayload);
                    this.createMoneyMovementRecord(requestManager, responseManager, statusId);
                    break;

                /*
                 * case PARAM_WIRE_TRANSFER:
                 * 
                 * eventSubType = deriveSubTypeForWireTransfer(requestPayload,
                 * serviceName); this.createMoneyMovementRecord(requestManager,
                 * responseManager, statusId); break;
                 */

                case PARAM_DEPOSIT_TRANSFER:
                    eventSubType = PARAM_RDC_TRANSFER;
                    break;

                default:
                    break;
                }

                customParams = this.addTransactionDate(customParams);
                if (!eventSubType.equals("")) {
                    try {
                    	
                    	AdminUtil.addAdminUserNameRoleIfAvailable(customParams, requestManager);
                    	
                        ObjectServiceHelperMethods
                                .execute(new ObjectServiceHelperMethods(requestManager, responseManager, eventType,
                                        eventSubType, producer, statusId, fromAccountNumber, customerid, customParams));
                    } catch (Exception e2) {
                        alert.prepareError("Exception Occured while invoking objectServiceHelperMethods").log();
                    }
                }
            }

        } catch (Exception ex) {
            diagnostic.prepareDebug("exception occured in Create transfer object service post processor", ex).log();
        }

    }

    private void createMoneyMovementRecord(FabricRequestManager requestManager, FabricResponseManager responseManager,
            String statusId) {
        try {

            new CreateMoneyMovementRecord().create(requestManager, responseManager, statusId);
        } catch (Exception e) {
            alert.prepareError("exception in createmoneymovement record", e).log();
        }
    }

    private JsonObject addTransactionDate(JsonObject customParams) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            customParams.addProperty("transactionDate", formatter.format(new Date()));
        } catch (Exception ez) {

            alert.prepareError("dateFormatter failed" + ez).log();
        }
        return customParams;
    }

    private String deriveSubTypeForInternalTransfer(String isScheduled, String frequencyType) {
        if (isScheduled.equals("1") && frequencyType.equalsIgnoreCase(PARAM_ONCE)) {
            return PARAM_SCHEDULED_OWN_ACCOUNT_TRANSFER;
        } else if (isScheduled.equals("1")) {
            return PARAM_RECURRING_OWN_ACCOUNT_TRANSFER;
        } else if (isScheduled.equals("0") && frequencyType.equalsIgnoreCase(PARAM_ONCE)) {
            return PARAM_ONETIME_OWN_ACCOUNT_TRANSFER;
        }
        return "";
    }

    private String deriveSubTypeForExternalTransfer(String isScheduled, String frequencyType, String serviceName) {
        if (serviceName.equals("INTER_BANK_ACCOUNT_FUND_TRANSFER")
                || serviceName.equals("INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE")) {

            if (isScheduled.equals("1") && frequencyType.equalsIgnoreCase(PARAM_ONCE)) {
                return PARAM_SCHEDULED_OTHER_BANK_TRANSFER;
            } else if (isScheduled.equals("1")) {
                return PARAM_RECURRING_OTHER_BANK_TRANSFER;
            } else if (isScheduled.equals("0") && frequencyType.equalsIgnoreCase(PARAM_ONCE)) {
                return PARAM_ONETIME_OTHER_BANK_TRANSFER;
            }
        } else if (serviceName.equals("INTRA_BANK_FUND_TRANSFER")
                || serviceName.equals("INTRA_BANK_FUND_TRANSFER_CREATE")) {

            if (isScheduled.equals("1") && frequencyType.equalsIgnoreCase(PARAM_ONCE)) {
                return PARAM_SCHEDULED_SAMEBANK_OTHER_ACCOUNT_TRANSFER;
            } else if (isScheduled.equals("1")) {
                return PARAM_RECURRING_SAMEBANK_OTHER_ACCOUNT_TRANSFER;
            } else if (isScheduled.equals("0") && frequencyType.equalsIgnoreCase(PARAM_ONCE)) {
                return PARAM_ONETIME_SAMEBANK_OTHER_ACCOUNT_TRANSFER;
            }
        } else {// in this case service name will be SERVICE_ID_4
            if (isScheduled.equals("1") && frequencyType.equalsIgnoreCase(PARAM_ONCE)) {
                return PARAM_SCHEDULED_INTERNATIONAL_BANK_TRANSFER;
            } else if (isScheduled.equals("1")) {
                return PARAM_RECURRING_INTERNATIONAL_BANK_TRANSFER;
            } else if (isScheduled.equals("0") && frequencyType.equalsIgnoreCase(PARAM_ONCE)) {
                return PARAM_ONETIME_INTERNATIONAL_BANK_TRANSFER;
            }
        }
        return "";
    }

    private String deriveSubTypeForP2PTransfer(JsonObject requestPayload) {
        String frequencyType = "";
        String isScheduled = "";
        if (ObjectServiceHelperMethods.hasKey(requestPayload, PARAM_FREQUENCY_TYPE)) {
            frequencyType = HelperMethods.getStringFromJsonObject(requestPayload, PARAM_FREQUENCY_TYPE, true);
        }
        if (ObjectServiceHelperMethods.hasKey(requestPayload, PARAM_IS_SCHEDULED)) {
            isScheduled = HelperMethods.getStringFromJsonObject(requestPayload, PARAM_IS_SCHEDULED, true);
        }
        String eventSubType = "";
        if (isScheduled.equals("1") && frequencyType.equalsIgnoreCase(PARAM_ONCE)) {
            eventSubType = "SCHEDULED_";
        } else if (isScheduled.equals("1")) {
            eventSubType = "RECURRING_";
        } else if (isScheduled.equals("0") && frequencyType.equalsIgnoreCase(PARAM_ONCE)) {
            eventSubType = "ONETIME_";
        }

        if (ObjectServiceHelperMethods.hasKey(requestPayload, PARAM_PERSON_ID)) {
            eventSubType = eventSubType + PARAM_REGISTERED_P2P_TRANSFER;
            return eventSubType;
        } else {
            eventSubType = eventSubType + PARAM_NON_REGISTERED_P2P_TRANSFER;
            return eventSubType;
        }
    }

    private String deriveSubTypeForWireTransfer(JsonObject requestPayload, String serviceName) {
        String wireAccountType = "";
        if (ObjectServiceHelperMethods.hasKey(requestPayload, PARAM_PAYEE_ID)) {
            if (serviceName.equals("DOMESTIC_WIRE_TRANSFER")) {
                return PARAM_REG_DOM_WIRE_TRANSFER;
            } else {
                return PARAM_REG_INTERNATIONAL_WIRE_TRANSFER;
            }

        } else {

            if (ObjectServiceHelperMethods.hasKey(requestPayload, PARAM_WIRE_ACCOUNT_TYPE)) {
                wireAccountType = HelperMethods.getStringFromJsonObject(requestPayload, PARAM_WIRE_ACCOUNT_TYPE, true);
            }
            if (wireAccountType.equalsIgnoreCase(PARAM_DOMESTIC)) {
                return PARAM_NON_REG_DOM_WIRE_TRANSFER;
            } else {
                return PARAM_NON_REG_INTERNATIONAL_WIRE_TRANSFER;
            }
        }
    }

    JsonObject buildCustomParamsForAlertEngine(JsonObject requestPayload, JsonObject responsePayload) {
        JsonObject customParams = new JsonObject();
        if (ObjectServiceHelperMethods.hasKey(requestPayload, PARAM_FROM_ACCOUNT_NUMBER)) {

            String fromAccountNumber = HelperMethods.getStringFromJsonObject(requestPayload, PARAM_FROM_ACCOUNT_NUMBER,
                    true);
            if (fromAccountNumber != null && !fromAccountNumber.isEmpty()) {

                String lastFourDigits;
                if (fromAccountNumber.length() > 4)
                    lastFourDigits = fromAccountNumber.substring(fromAccountNumber.length() - 4);
                else
                    lastFourDigits = fromAccountNumber;

                customParams.addProperty("MaskedFromAccount", "XXXX" + lastFourDigits);
            }
        }

        if (ObjectServiceHelperMethods.hasKey(requestPayload, PARAM_TO_ACCOUNT_NUMBER)) {
            String toAccountNumber = HelperMethods.getStringFromJsonObject(requestPayload, PARAM_TO_ACCOUNT_NUMBER,
                    true);
            if (toAccountNumber != null && !toAccountNumber.isEmpty()) {

                String lastFourDigits;
                if (toAccountNumber.length() > 4)
                    lastFourDigits = toAccountNumber.substring(toAccountNumber.length() - 4);
                else
                    lastFourDigits = toAccountNumber;

                customParams.addProperty("MaskedToAccount", "XXXX" + lastFourDigits);
            }
        }

        customParams.addProperty(PARAM_SERVER_DATE, new Date().toString());
        if (ObjectServiceHelperMethods.hasKey(responsePayload, PARAM_REFERENCE_ID)) {
            customParams.addProperty("RefNumber",
                    HelperMethods.getStringFromJsonObject(responsePayload, PARAM_REFERENCE_ID, true));
        }

        // code Works for If it needs to be pushed to alert engine
        return customParams;
    }

    String deriveSubTypeForBillPayment(JsonObject requestPayload) {
        if (ObjectServiceHelperMethods.hasKey(requestPayload, PARAM_PAYEE_ID)) {
            return PARAM_REGISTERED_BILL_PAYMENT;
        } else {
            return PARAM_NON_REGISTERED_BILL_PAYMENT;
        }
    }
    
    private static void StoreTransactionIdInSession(FabricRequestManager fabricRequestManager,
            FabricResponseManager fabricResponseManager, String referenceId) {
        SessionMap transactionsMap = new SessionMap();
        transactionsMap.addKey(referenceId);
        TransactionManager transManager = new TransactionManager(fabricRequestManager, fabricResponseManager);
        transManager.saveTransactionsIntoSession(transactionsMap);
    }
}
