package com.kony.dbputilities.customersecurityservices.preprocessor;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonObject;
import com.kony.dbputilities.mfa.preprocessors.TransactionsMFAPreProcessor;
import com.kony.objectserviceutils.EventsDispatcher;
import com.kony.postprocessors.ObjectServicesConstants;
import com.kony.utilities.HelperMethods;
import com.kony.utilities.ObjectServiceHelperMethods;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;

public class UpdateTransactionObjectServicePreProcessor
		implements ObjectServicePreProcessor, ObjectServicesConstants, ObjectProcessorTask {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager,
			FabricRequestChain requestChain) throws Exception {
		Log4j2Configurator.getInstance();
		PayloadHandler responsePayloadHandler = responseManager.getPayloadHandler();
		PayloadHandler requestPayloadHandler = requestManager.getPayloadHandler();
		JsonObject responsePayload = responsePayloadHandler.getPayloadAsJson().getAsJsonObject();
		JsonObject requestPayload = requestPayloadHandler.getPayloadAsJson().getAsJsonObject();
		JsonObject customParams = new JsonObject();
		String enableEvents = ObjectServiceHelperMethods.getConfigurableParameters(PARAM_ENABLE_EVENTS, requestManager);
		String customerid = HelperMethods.getCustomerIdFromSession(requestManager);
		String eventSubType = "";
		String eventType = PARAM_MAKE_TRANSFER;
		String producer = "";
		String statusId = PARAM_SID_EVENT_SUCCESS;
		String transactionType = "";
		String serviceName = "";
		if (ObjectServiceHelperMethods.hasKey(responsePayload, PARAM_DBP_ERR_CODE)) {
			statusId = PARAM_SID_EVENT_FAILURE;
		}

		diagnostic.prepareDebug("ENABLE_EVENTS=" + enableEvents).log();

		if (enableEvents != null && !"".equalsIgnoreCase(enableEvents) && enableEvents.equals(PARAM_TRUE)) {
			if (ObjectServiceHelperMethods.hasKey(requestPayload, PARAM_TRANSACTION_TYPE)) {
				transactionType = HelperMethods.getStringFromJsonObject(requestPayload, PARAM_TRANSACTION_TYPE, true)
						.toLowerCase();
			}
			JsonObject mfaAttributes = requestPayload.getAsJsonObject(PARAM_MFA_ATRIBUTES);

			if (mfaAttributes != null && ObjectServiceHelperMethods.hasKey(mfaAttributes, PARAM_SERVICE_NAME)) {

				serviceName = HelperMethods.getStringFromJsonObject(mfaAttributes, PARAM_SERVICE_NAME, true);
			}
			switch (transactionType) {
			case PARAM_INTERNAL_TRANSFER:
				eventSubType = deriveSubTypeForInternalTransfer(serviceName, requestPayload);
				break;

			case PARAM_EXTERNAL_TRANSFER:

				eventSubType = deriveSubTypeForExternalTransfer(serviceName, requestPayload);
				break;

			case PARAM_BILLPAY_TRANSFER:

				eventSubType = deriveSubTypeForBillPayment(requestPayload);

				break;

			case PARAM_P2P_TRANSFER:

				eventSubType = deriveSubTypeForP2PTransfer(requestPayload);

				break;

			case PARAM_WIRE_TRANSFER:

				eventSubType = deriveSubTypeForWireTransfer(serviceName, requestPayload);
				break;

			case PARAM_DEPOSIT_TRANSFER:

				eventSubType = PARAM_RDC_TRANSFER;
				break;

			default:
				break;
			}
			customParams.addProperty("transactionDate",
					new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(new Date()));

			if (!eventSubType.equals("") && !ObjectServiceHelperMethods.hasKey(responsePayload, PARAM_REFERENCE_ID)
					&& ObjectServiceHelperMethods.hasKey(responsePayload, PARAM_MFA_TYPE)) {
				EventsDispatcher.dispatch(requestManager, responseManager, eventType, "UPDATE_" + eventSubType + "_MFA",
						producer, statusId, null, customerid, customParams);
			}

		}

	}

	@Override
	public boolean process(FabricRequestManager requestManager, FabricResponseManager responseManager)
			throws Exception {
		boolean status = true;
		try {
			status = new TransactionsMFAPreProcessor().process(requestManager, responseManager);
			execute(requestManager, responseManager, null);
		} catch (Exception e) {
			alert.prepareError("Exception occured in update transaction pre processor", e).log();
		}
		return status;
	}

	String deriveSubTypeForInternalTransfer(String serviceName, JsonObject requestPayload) {
		if (serviceName.equals("TRANSFER_BETWEEN_OWN_ACCOUNT")) {
			// checking type of internal own accounts transfer
			String frequencyType = HelperMethods.getStringFromJsonObject(requestPayload, PARAM_FREQUENCY_TYPE, true);
			String isScheduled = HelperMethods.getStringFromJsonObject(requestPayload, PARAM_IS_SCHEDULED, true);

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

	String deriveSubTypeForWireTransfer(String serviceName, JsonObject requestPayload) {
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
			if (wireAccountType.equals(PARAM_DOMESTIC)) {
				return PARAM_NON_REG_DOM_WIRE_TRANSFER;

			} else {
				return PARAM_NON_REG_INTERNATIONAL_WIRE_TRANSFER;
			}
		}
	}

	String deriveSubTypeForBillPayment(JsonObject requestPayload) {
		if (ObjectServiceHelperMethods.hasKey(requestPayload, PARAM_PAYEE_ID)) {
			return PARAM_REGISTERED_BILL_PAYMENT;
		} else {
			return PARAM_NON_REGISTERED_BILL_PAYMENT;
		}

	}

	String deriveSubTypeForP2PTransfer(JsonObject requestPayload) {
		String frequencyType = "";
		String isScheduled = "";
		String eventSubType = "";
		if (ObjectServiceHelperMethods.hasKey(requestPayload, PARAM_FREQUENCY_TYPE)) {
			frequencyType = HelperMethods.getStringFromJsonObject(requestPayload, PARAM_FREQUENCY_TYPE, true);
		}
		if (ObjectServiceHelperMethods.hasKey(requestPayload, PARAM_FREQUENCY_TYPE)) {
			isScheduled = HelperMethods.getStringFromJsonObject(requestPayload, PARAM_IS_SCHEDULED, true);
		}
		if (isScheduled.equals("1") && frequencyType.equalsIgnoreCase(PARAM_ONCE)) {
			eventSubType = "SCHEDULED_";
		} else if (isScheduled.equals("1")) {
			eventSubType = "RECURRING_";
		} else if (isScheduled.equals("0") && frequencyType.equalsIgnoreCase(PARAM_ONCE)) {
			eventSubType = "ONETIME_";
		}
		if (ObjectServiceHelperMethods.hasKey(requestPayload, PARAM_PERSON_ID)) {
			eventSubType = eventSubType + PARAM_REGISTERED_P2P_TRANSFER;
		} else {
			eventSubType = eventSubType + PARAM_NON_REGISTERED_P2P_TRANSFER;
		}
		return eventSubType;
	}

	String deriveSubTypeForExternalTransfer(String serviceName, JsonObject requestPayload) {
		String frequencyType = HelperMethods.getStringFromJsonObject(requestPayload, PARAM_FREQUENCY_TYPE, true);
		String isScheduled = HelperMethods.getStringFromJsonObject(requestPayload, PARAM_IS_SCHEDULED, true);
		if (serviceName.equals("INTER_BANK_ACCOUNT_FUND_TRANSFER")) {
			return subTypeForExternalTransfer(isScheduled, frequencyType);
		} else if (serviceName.equals("INTRA_BANK_FUND_TRANSFER")) {
			// checking type of internal other person transfer
			return subTypeForInternalTransfer(isScheduled, frequencyType);
		} else {// in this case service name will be SERVICE_ID_4
			return subTypeForInternationalTransfer(isScheduled, frequencyType);
		}
	}

	String subTypeForInternalTransfer(String isScheduled, String frequencyType) {
		if (isScheduled.equals("1") && frequencyType.equalsIgnoreCase(PARAM_ONCE)) {
			return PARAM_SCHEDULED_SAMEBANK_OTHER_ACCOUNT_TRANSFER;
		} else if (isScheduled.equals("1")) {
			return PARAM_RECURRING_SAMEBANK_OTHER_ACCOUNT_TRANSFER;
		} else if (isScheduled.equals("0") && frequencyType.equalsIgnoreCase(PARAM_ONCE)) {
			return PARAM_ONETIME_SAMEBANK_OTHER_ACCOUNT_TRANSFER;
		}
		return "";
	}

	String subTypeForExternalTransfer(String isScheduled, String frequencyType) {
		if (isScheduled.equals("1") && frequencyType.equalsIgnoreCase(PARAM_ONCE)) {
			return PARAM_SCHEDULED_OTHER_BANK_TRANSFER;
		} else if (isScheduled.equals("1")) {
			return PARAM_RECURRING_OTHER_BANK_TRANSFER;
		} else if (isScheduled.equals("0") && frequencyType.equalsIgnoreCase(PARAM_ONCE)) {
			return PARAM_ONETIME_OTHER_BANK_TRANSFER;
		}
		return "";
	}

	String subTypeForInternationalTransfer(String isScheduled, String frequencyType) {
		if (isScheduled.equals("1") && frequencyType.equalsIgnoreCase(PARAM_ONCE)) {
			return PARAM_SCHEDULED_INTERNATIONAL_BANK_TRANSFER;
		} else if (isScheduled.equals("1")) {
			return PARAM_RECURRING_INTERNATIONAL_BANK_TRANSFER;
		} else if (isScheduled.equals("0") && frequencyType.equalsIgnoreCase(PARAM_ONCE)) {
			return PARAM_ONETIME_INTERNATIONAL_BANK_TRANSFER;
		}
		return "";
	}

}