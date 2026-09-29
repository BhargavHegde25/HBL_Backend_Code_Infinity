package com.kony.postprocessors;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonObject;
import com.kony.memorymgmt.SessionMap;
import com.kony.memorymgmt.TransactionManager;
import com.kony.utilities.HelperMethods;
import com.kony.utilities.ObjectServiceHelperMethods;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;

public class UpdateTransactionObjectServicePostProcessor
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
			
            // Store Transaction Id into cache
            String referenceId = "";
            if (ObjectServiceHelperMethods.hasKey(responsePayload, "referenceId"))
                referenceId = HelperMethods.getStringFromJsonObject(responsePayload, "referenceId", true);

            StoreTransactionIdInSession(requestManager, responseManager, referenceId);

			
			if(responsePayload != null && responsePayload.has(PARAM_VALIDATE)){
	            String validate = responsePayload.get(PARAM_VALIDATE).getAsString();
	            if(StringUtils.isNotBlank(validate) && validate.equalsIgnoreCase(PARAM_TRUE)){
	                alert.prepareError("Validate Request. Aborting Events").log();
	                return ; 
	            }
	        } 

			String opstatus = "";
			String enableEvents = ObjectServiceHelperMethods.getConfigurableParameters(PARAM_ENABLE_EVENTS,
					requestManager);
			String customerid = HelperMethods.getCustomerIdFromSession(requestManager);
			String eventSubType = "";
			String eventType = PARAM_MAKE_TRANSFER;
			String producer = "Transactions/PATCH";
			String statusId = PARAM_SID_EVENT_FAILURE;
			String transactionType = "";
			if (ObjectServiceHelperMethods.hasKey(responsePayload, PARAM_OP_STATUS)) {
				opstatus = HelperMethods.getStringFromJsonObject(responsePayload, PARAM_OP_STATUS, true);
			}

			if (opstatus.equals("0") && !ObjectServiceHelperMethods.hasKey(responsePayload, PARAM_DBP_ERR_CODE)) {
				statusId = PARAM_SID_EVENT_SUCCESS;
			}

			diagnostic.prepareDebug("ENABLE_EVENTS=" + enableEvents).log();

			if (enableEvents != null && enableEvents.equalsIgnoreCase(PARAM_TRUE)) {
				if (ObjectServiceHelperMethods.hasKey(requestPayload, PARAM_TRANSACTION_TYPE)) {
					transactionType = HelperMethods
							.getStringFromJsonObject(requestPayload, PARAM_TRANSACTION_TYPE, true).toLowerCase();
				}

				switch (transactionType) {
				case PARAM_INTERNAL_TRANSFER:
					eventSubType = deriveSubTypeForInternalTransfer(requestPayload);
					break;

				case PARAM_EXTERNAL_TRANSFER:
					eventSubType = deriveSubTypeForExternalTransfer(requestPayload);
					break;

				case PARAM_BILLPAY_TRANSFER:

					eventSubType = deriveSubTypeForBillPayment(requestPayload);

					break;

				case PARAM_P2P_TRANSFER:
					eventSubType = deriveSubTypeForP2PTransfer(requestPayload);

					break;

				case PARAM_WIRE_TRANSFER:

					eventSubType = deriveSubTypeForWireTransfer(requestPayload);
					break;

				case PARAM_DEPOSIT_TRANSFER:

					eventSubType = PARAM_RDC_TRANSFER;
					break;

				default:
					break;
				}
				customParams.addProperty("transactionDate",
						new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(new Date()));

				if (!eventSubType.equals("")) {
					try {

						ObjectServiceHelperMethods
								.execute(new ObjectServiceHelperMethods(requestManager, responseManager, eventType,
										"UPDATE_" + eventSubType, producer, statusId, null, customerid, customParams));
					} catch (Exception e2) {
						alert.prepareError("Exception Occured while invoking objectServiceHelperMethods").log();
					}
				}

			}

		} catch (Exception ex) {
			alert.prepareError("exception occured in ObjectService PostProcessor while deleting Transaction=", ex).log();
		}

	}

	String deriveSubTypeForWireTransfer(JsonObject requestPayload) {
		JsonObject mfaAttributes = requestPayload.getAsJsonObject(PARAM_MFA_ATRIBUTES);
		String serviceName = "";

		if (mfaAttributes != null && ObjectServiceHelperMethods.hasKey(mfaAttributes, PARAM_SERVICE_NAME)) {
			serviceName = HelperMethods.getStringFromJsonObject(mfaAttributes, PARAM_SERVICE_NAME, true);
		}
		if (ObjectServiceHelperMethods.hasKey(requestPayload, PARAM_PAYEE_ID)) {
			if (serviceName.equals("DOMESTIC_WIRE_TRANSFER")) {
				return PARAM_REG_DOM_WIRE_TRANSFER;
			} else {
				return PARAM_REG_INTERNATIONAL_WIRE_TRANSFER;
			}

		} else {

			String wireAccountType = "";
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

	String deriveSubTypeForP2PTransfer(JsonObject requestPayload) {
		String frequencyType = "";
		String isScheduled = "";
		String eventSubType = "";
		if (ObjectServiceHelperMethods.hasKey(requestPayload, PARAM_FREQUENCY_TYPE)) {
			frequencyType = HelperMethods.getStringFromJsonObject(requestPayload, PARAM_FREQUENCY_TYPE, true);
		}
		if (ObjectServiceHelperMethods.hasKey(requestPayload, PARAM_IS_SCHEDULED)) {
			isScheduled = HelperMethods.getStringFromJsonObject(requestPayload, PARAM_IS_SCHEDULED, true);
		}
		if (isScheduled.equalsIgnoreCase("1") && frequencyType.equalsIgnoreCase(PARAM_ONCE)) {
			eventSubType = "SCHEDULED_";
		} else if (isScheduled.equals("1")) {
			eventSubType = "RECURRING_";
		} else if (isScheduled.equals("0") && frequencyType.equalsIgnoreCase(PARAM_ONCE)) {
			eventSubType = "ONETIME_";
		}
		if (ObjectServiceHelperMethods.hasKey(requestPayload, PARAM_PERSON_ID)) {
			return eventSubType + PARAM_REGISTERED_P2P_TRANSFER;
		} else {
			return eventSubType + PARAM_NON_REGISTERED_P2P_TRANSFER;
		}
	}

	String deriveSubTypeForBillPayment(JsonObject requestPayload) {
		if (ObjectServiceHelperMethods.hasKey(requestPayload, PARAM_PAYEE_ID)) {
			return PARAM_REGISTERED_BILL_PAYMENT;
		} else {
			return PARAM_NON_REGISTERED_BILL_PAYMENT;
		}
	}

	String deriveSubTypeForInternalTransfer(JsonObject requestPayload) {
		JsonObject mfaAttributes;
		mfaAttributes = requestPayload.getAsJsonObject(PARAM_MFA_ATRIBUTES);
		String serviceName = "";
		if (mfaAttributes != null && ObjectServiceHelperMethods.hasKey(mfaAttributes, PARAM_SERVICE_NAME)) {
			serviceName = HelperMethods.getStringFromJsonObject(mfaAttributes, PARAM_SERVICE_NAME, true);
		}
		String isScheduled = "";
		String frequencyType = "";
        if (serviceName.equals("TRANSFER_BETWEEN_OWN_ACCOUNT")
                || serviceName.equals("TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE")) {
			// checking type of internal own accounts transfer
			if (ObjectServiceHelperMethods.hasKey(requestPayload, PARAM_FREQUENCY_TYPE)) {
				frequencyType = HelperMethods.getStringFromJsonObject(requestPayload, PARAM_FREQUENCY_TYPE, true);
			}
			if (ObjectServiceHelperMethods.hasKey(requestPayload, PARAM_IS_SCHEDULED)) {
				isScheduled = isScheduledConversion(HelperMethods.getStringFromJsonObject(requestPayload, PARAM_IS_SCHEDULED, true));
			}

			if (isScheduled.equals("1") && frequencyType.equalsIgnoreCase(PARAM_ONCE)) {
				return PARAM_SCHEDULED_OWN_ACCOUNT_TRANSFER;
			} else if (isScheduled.equals("1")) {
				return PARAM_RECURRING_OWN_ACCOUNT_TRANSFER;
			} else if (isScheduled.equals("0") && frequencyType.equalsIgnoreCase(PARAM_ONCE)) {
				return PARAM_ONETIME_OWN_ACCOUNT_TRANSFER;
			}
		}
		return "";
	}

	String deriveSubTypeForExternalTransfer(JsonObject requestPayload) { 
		JsonObject mfaAttributes;
		String serviceName = "";
		mfaAttributes = requestPayload.getAsJsonObject(PARAM_MFA_ATRIBUTES);
		
		if (mfaAttributes != null && ObjectServiceHelperMethods.hasKey(mfaAttributes, PARAM_SERVICE_NAME)) {
			serviceName = HelperMethods.getStringFromJsonObject(mfaAttributes, PARAM_SERVICE_NAME, true);
		}
		String frequencyType = "";
		String isScheduled = "";
		if (ObjectServiceHelperMethods.hasKey(requestPayload, PARAM_FREQUENCY_TYPE)) {
			frequencyType = HelperMethods.getStringFromJsonObject(requestPayload, PARAM_FREQUENCY_TYPE, true);
		}
		if (ObjectServiceHelperMethods.hasKey(requestPayload, PARAM_IS_SCHEDULED)) {
			isScheduled = isScheduledConversion(HelperMethods.getStringFromJsonObject(requestPayload, PARAM_IS_SCHEDULED, true));
		}
        if (serviceName.equals("INTER_BANK_ACCOUNT_FUND_TRANSFER")
                || serviceName.equals("INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE")) {
			return deriveSubTypeForOtherAccountTransfers(isScheduled, frequencyType);
        } else if (serviceName.equals("INTRA_BANK_FUND_TRANSFER")
                || serviceName.equals("INTRA_BANK_FUND_TRANSFER_CREATE")) {
			// checking type of internal other person transfer
			return deriveSubTypeForOthersInSameBank(isScheduled, frequencyType);
		} else {// in this case service name will be SERVICE_ID_4
			return deriveSubTypeForInternationalTransfer(isScheduled, frequencyType);
		}
	}

	String deriveSubTypeForOtherAccountTransfers(String isScheduled, String frequencyType) {
		if (isScheduled.equals("1") && frequencyType.equalsIgnoreCase(PARAM_ONCE)) {
			return PARAM_SCHEDULED_OTHER_BANK_TRANSFER;
		} else if (isScheduled.equals("1")) {
			return PARAM_RECURRING_OTHER_BANK_TRANSFER;
		} else if (isScheduled.equals("0") && frequencyType.equalsIgnoreCase(PARAM_ONCE)) {
			return PARAM_ONETIME_OTHER_BANK_TRANSFER;
		}
		return "";
	}

	String deriveSubTypeForOthersInSameBank(String isScheduled, String frequencyType) {
		if (isScheduled.equals("1") && frequencyType.equalsIgnoreCase(PARAM_ONCE)) {
			return PARAM_SCHEDULED_SAMEBANK_OTHER_ACCOUNT_TRANSFER;
		} else if (isScheduled.equals("1")) {
			return PARAM_RECURRING_SAMEBANK_OTHER_ACCOUNT_TRANSFER;
		} else if (isScheduled.equals("0") && frequencyType.equalsIgnoreCase(PARAM_ONCE)) {
			return PARAM_ONETIME_SAMEBANK_OTHER_ACCOUNT_TRANSFER;
		}
		return "";
	}

	String deriveSubTypeForInternationalTransfer(String isScheduled, String frequencyType) {
		if (isScheduled.equals("1") && frequencyType.equalsIgnoreCase(PARAM_ONCE)) {
			return PARAM_SCHEDULED_INTERNATIONAL_BANK_TRANSFER;
		} else if (isScheduled.equals("1")) {
			return PARAM_RECURRING_INTERNATIONAL_BANK_TRANSFER;
		} else if (isScheduled.equals("0") && frequencyType.equalsIgnoreCase(PARAM_ONCE)) {
			return PARAM_ONETIME_INTERNATIONAL_BANK_TRANSFER;
		}
		return "";
	}
	
    String isScheduledConversion(String isScheduled) {
        if (StringUtils.isNotBlank(isScheduled)) {
            switch (isScheduled) {
            case "true":
                isScheduled = "1";
                break;
            case "false":
                isScheduled = "0";
                break;
            }
        }
        return isScheduled;
    }
    
    private static void StoreTransactionIdInSession(FabricRequestManager fabricRequestManager,
            FabricResponseManager fabricResponseManager, String referenceId) {
        SessionMap transactionsMap = new SessionMap();
        transactionsMap.addKey(referenceId);
        TransactionManager transManager = new TransactionManager(fabricRequestManager, fabricResponseManager);
        transManager.saveTransactionsIntoSession(transactionsMap);
    }

}