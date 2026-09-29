package com.kony.postprocessors;

import java.util.concurrent.Callable;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.memorymgmt.UserDetailsManager;
import com.kony.objectserviceutils.Callutil;
import com.kony.utilities.CallableUtil;
import com.kony.utilities.HelperMethods;
import com.kony.utilities.ObjectServiceHelperMethods;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;
import com.temenos.dbx.product.utils.ThreadExecutor;

public class UpdateCustomerDetailsObjectServicePostProcessor
		implements ObjectServicePostProcessor, ObjectServicesConstants {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private static final String PARAM_ID = "id";
	private static final String IS_PRIMARY = "isPrimary";

	@Override
	public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager) throws Exception {
		Log4j2Configurator.getInstance();

		try {

			String customerid = HelperMethods.getCustomerIdFromSession(requestManager);
			PayloadHandler responsePayloadHandler = responseManager.getPayloadHandler();
			PayloadHandler requestPayloadHandler = requestManager.getPayloadHandler();
			JsonObject responsePayload = responsePayloadHandler.getPayloadAsJson().getAsJsonObject();
			JsonObject requestPayload = requestPayloadHandler.getPayloadAsJson().getAsJsonObject();
			JsonObject customParams = new JsonObject();
			String opstatus = "";
			String enableEvents = ObjectServiceHelperMethods.getConfigurableParameters(PARAM_ENABLE_EVENTS,
					requestManager);
			String eventType = PARAM_PROFILE_UPDATE;
			String eventSubType = "";
			String producer = "user/updateCustomerDetails";
			String statusId = PARAM_SID_EVENT_FAILURE;

			diagnostic.prepareDebug("ENABLE_EVENTS=" + enableEvents).log();
			alert.prepareError("UpdateCustomerDetailsObjectServicePostProcessor triggered").log();

			if (ObjectServiceHelperMethods.hasKey(responsePayload, PARAM_OP_STATUS)) {
				opstatus = HelperMethods.getStringFromJsonObject(responsePayload, PARAM_OP_STATUS, true);
			}

			if (opstatus.equals("0") && !ObjectServiceHelperMethods.hasKey(responsePayload, PARAM_DBP_ERR_CODE)) {
				statusId = PARAM_SID_EVENT_SUCCESS;
			}

			if (enableEvents != null && enableEvents.equalsIgnoreCase(PARAM_TRUE)) {
				UserDetailsManager userDetailsManager = new UserDetailsManager(requestManager);
				String cachedUserDetails = userDetailsManager.getUserDetailsFromSession();
				JsonElement cachedUserDetailsElement = this.getDetails(cachedUserDetails);

				if (ObjectServiceHelperMethods.hasKey(requestPayload, PARAM_EMAILIDS)) {
					customParams = getFirstRecordFromPayload(requestPayload, PARAM_EMAILIDS, PARAM_EMAILIDS,
							cachedUserDetailsElement, "UpdatedEmail", PARAM_VALUE);
					eventSubType = PARAM_EMAIL_CHANGE;
				}

				else if (ObjectServiceHelperMethods.hasKey(requestPayload, PARAM_PHONE_NUMBERS)) {
					pushPhoneUpdateEvent(requestManager, responseManager);
					customParams = getFirstRecordFromPayload(requestPayload, PARAM_PHONE_NUMBERS, PARAM_CONTACTNUMBERS,
							cachedUserDetailsElement, "UpdatedPhone", "phoneNumber");
					eventSubType = PARAM_PHONE_CHANGE;
				}

				else if (ObjectServiceHelperMethods.hasKey(requestPayload, PARAM_ADDRESSES)) {
					eventSubType = PARAM_ADDRESS_CHANGE;
				}
				if (!eventSubType.equals("")) {
					try {
						ObjectServiceHelperMethods
								.execute(new ObjectServiceHelperMethods(requestManager, responseManager, eventType,
										eventSubType, producer, statusId, null, customerid, customParams));
					} catch (Exception e2) {
						alert.prepareError("Exception Occured while invoking objectServiceHelperMethods").log();
					}
				}
				//userDetailsManager.deleteUserDetailsFromCache();
			}
		} catch (Exception ex) {
			alert.prepareError("exception occured in update customer details object service post processor=", ex).log();
		}

	}

	private JsonObject getFirstRecordFromPayload(JsonObject requestPayload, String requestPayloadKey, String cacheKey,
			JsonElement cachedUserDetailsElement, String customParamsKey, String customParamsValue) {
		JsonElement eventsElement = null;
		JsonArray contactdetails = new JsonArray();
		String contactDetailsString = HelperMethods.getStringFromJsonObject(requestPayload, requestPayloadKey, true);
		eventsElement = this.getDetails(contactDetailsString);
		if (eventsElement != null && eventsElement.isJsonArray()) {
			contactdetails = eventsElement.getAsJsonArray();
		}
		JsonObject firstRecord = contactdetails.get(0).getAsJsonObject();
		String id = HelperMethods.getStringFromJsonObject(firstRecord, PARAM_ID, false);
		String isPrimary = HelperMethods.getStringFromJsonObject(firstRecord, IS_PRIMARY, false);

		JsonObject customParams = getCustomParams(id, checkIsPrimary(isPrimary), cacheKey, cachedUserDetailsElement);
		customParams.addProperty(customParamsKey,
				HelperMethods.getStringFromJsonObject(firstRecord, customParamsValue, true));
		diagnostic.prepareDebug("customparams " + customParams).log();
		return customParams;
	}

	private boolean checkIsPrimary(String isPrimary) {
		return (isPrimary.equals("true") || isPrimary.equals("1"));
	}

	private JsonObject getCustomParams(String id, boolean isPrimary, String key, JsonElement cachedUserDetailsElement) {
		JsonArray cachedEmails = new JsonArray();
		JsonObject customparams = new JsonObject();
		if (cachedUserDetailsElement != null && cachedUserDetailsElement.isJsonObject()) {
			try {
				cachedEmails = cachedUserDetailsElement.getAsJsonObject().get(key).getAsJsonArray();
				String value = null;
				if (StringUtils.isBlank(id) && isPrimary) {
					for (JsonElement ele : cachedEmails) {
						if (ele.isJsonObject()) {
							JsonObject obj = ele.getAsJsonObject();
							if (obj.get(IS_PRIMARY) != null && obj.get(IS_PRIMARY).getAsString().equals("true")) {
								value = HelperMethods.getStringFromJsonObject(obj, "Value", true);
								if (key.equals(PARAM_CONTACTNUMBERS)) {
									String phoneCountryCode = HelperMethods.getStringFromJsonObject(obj,
											"phoneCountryCode", false);
									value = phoneCountryCode != null
											? new StringBuilder().append(phoneCountryCode).append(value).toString()
											: value;
								}
								break;
							}
						}
					}
				} else if (StringUtils.isNotBlank(id)) {
					for (JsonElement ele : cachedEmails) {
						if (ele.isJsonObject()) {
							JsonObject obj = ele.getAsJsonObject();
							if (obj.get(PARAM_ID) != null && obj.get(PARAM_ID).getAsString().equals(id)) {
								value = HelperMethods.getStringFromJsonObject(obj, "Value", true);
								if (key.equals(PARAM_CONTACTNUMBERS)) {
									String phoneCountryCode = HelperMethods.getStringFromJsonObject(obj,
											"phoneCountryCode", false);
									value = phoneCountryCode != null
											? new StringBuilder().append(phoneCountryCode).append(value).toString()
											: value;
								}
								break;
							}
						}
					}
				}
				if (StringUtils.isNotBlank(value)) {
					String customParamsKey = key.equals(PARAM_CONTACTNUMBERS) ? "externalphone" : "externalemail";
					customparams.addProperty(customParamsKey, value);
					customparams.addProperty("includepreferredcontact", true);
				}
			} catch (Exception e) {
				alert.prepareError("Unable to get cached userdetails ", e).log();
			}
		}
		return customparams;
	}

	private static void pushPhoneUpdateEvent(FabricRequestManager fabreq, FabricResponseManager fabresp) {

		Callable<CallableUtil> call = new Callutil(fabreq, fabresp);
		try {
			ThreadExecutor.getExecutor(fabreq).execute(call);

		} catch (Exception e) {
			alert.prepareError("Error in invoking an event", e).log();
		}

	}

	private JsonElement getDetails(String input) {
		try {
			return new JsonParser().parse(input);
		} catch (Exception e) {
			diagnostic.prepareDebug("unable to convert string to array", e).log();
		}
		return null;
	}

}
