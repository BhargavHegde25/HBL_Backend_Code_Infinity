package com.temenos.infinity.api.loanspayoff.eventlogs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.exceptions.MiddlewareException;
import com.konylabs.middleware.registry.AppRegistryException;
import com.temenos.infinity.api.loanspayoff.constants.EventLogConstants;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class EventLogUtils implements EventLogConstants {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    public JsonObject buildEventData(FabricRequestManager fabricRequestManager, JsonObject request,
            JsonObject response) {
        JsonObject EventLog = new JsonObject();

        // Read Audit log Properties and set the required parameters
        JsonObject EventLogProperties = null;
        try {
            EventLogProperties = LoadEventLogProperties(fabricRequestManager);
        } catch (FileNotFoundException e) {
            alert.prepareError("Unable to load audit log properties").log();
        }

        // Read operation data from request manager
        String ServiceId = StringUtils.EMPTY;
        String ObjectId = StringUtils.EMPTY;
        String OperationId = StringUtils.EMPTY;
        try {
            ServiceId = fabricRequestManager.getServicesManager().getOperationData().getServiceId();
            ObjectId = fabricRequestManager.getServicesManager().getOperationData().getObjectId();
            OperationId = fabricRequestManager.getServicesManager().getOperationData().getOperationId();
        } catch (AppRegistryException e) {
            alert.prepareError("Unable to Get operation Data" + e).log();
        }

        // Set Producer
        String producer = StringUtils.EMPTY;
        try {

            producer = fabricRequestManager.getServicesManager().getOperationData().getServiceId() + "/"
                    + fabricRequestManager.getServicesManager().getOperationData().getObjectId() + "/"
                    + fabricRequestManager.getServicesManager().getOperationData().getOperationId();
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String[] feilds = className.split("\\.");
            className = null;
            if (feilds.length > 0)
                className = feilds[feilds.length - 1];
            if (className != null)
                producer = producer + "/" + className;
            EventLog.addProperty(PARAM_PRODUCER, producer);
        } catch (Exception e) {
            producer = "";
            alert.prepareError("unable to get producer").log();
        }

        // Get Customer id and name from identity session
        Map<String, Object> customer = getCustomerMap(fabricRequestManager);
        String customerName = getCustomerName(customer);
        EventLog.addProperty(PARAM_CUSTOMER_NAME, customerName);

        // Read Service details
        JsonArray ServiceData = new JsonArray();
        JsonObject ObjectData = new JsonObject();
        if (EventLogProperties.has(ServiceId))
            ServiceData = EventLogProperties.get(ServiceId).getAsJsonArray();

        if (ServiceData != null && ServiceData.size() > 0) {
            for (JsonElement jsonElement : ServiceData) {
                if (!jsonElement.isJsonNull())
                    if (jsonElement.getAsJsonObject().has(ObjectId))
                        ObjectData = (JsonObject) jsonElement.getAsJsonObject().get(ObjectId);
            }
        }

        // Check the request is for validate
        if (request != null && request.has(PARAM_VALIDATE)) {
            String validate = request.get(PARAM_VALIDATE).getAsString();
            if (StringUtils.isNotBlank(validate) && validate.equalsIgnoreCase(PARAM_TRUE)) {
                alert.prepareError("Validate Request. Aborting Events").log();
                return new JsonObject();
            }
        }

        // Check enable event
        if (ObjectData.has(PARAM_ENABLE_EVENT)) {
            if (!ObjectData.get(PARAM_ENABLE_EVENT).getAsString().equalsIgnoreCase(PARAM_TRUE)) {
                alert.prepareError("Event is not enabled for the object ").log();
                return new JsonObject();
            } else {
                EventLog.addProperty(PARAM_ENABLE_EVENT, PARAM_TRUE);
            }
        }

        // Check the operation is included in properties
        JsonArray OperationData = new JsonArray();
        String EventSubType = StringUtils.EMPTY;
        String logResponse = StringUtils.EMPTY;
        boolean OperationAvailable = false;
        if (ObjectData.has(PARAM_INCLUDED_OPERTAIONS)) {
            OperationData = ObjectData.get(PARAM_INCLUDED_OPERTAIONS).getAsJsonArray();
            for (JsonElement jsonElement : OperationData) {
                if (!jsonElement.isJsonNull()) {
                    JsonObject Operation = jsonElement.getAsJsonObject();
                    if (Operation.has(PARAM_OPERATION)
                            && Operation.get(PARAM_OPERATION).getAsString().equalsIgnoreCase(OperationId)) {

                        EventSubType = Operation.get(PARAM_EVENT_SUBTYPE).getAsString();
                        logResponse = Operation.get(LOG_RESPONSE).getAsString();
                        OperationAvailable = true;
                        break;
                    }
                }
            }

        }

        // Return empty object if operation not available
        if (!OperationAvailable) {
            alert.prepareError("Operation Not available in properties. Skipping the log").log();
            return new JsonObject();
        }

        // Get the event type and sub type details
        if (ObjectData.has(PARAM_EVENT_TYPE)) {
            EventLog.addProperty(PARAM_EVENT_TYPE, ObjectData.get(PARAM_EVENT_TYPE).getAsString());
        }
        EventLog.addProperty(PARAM_EVENT_SUBTYPE, EventSubType);
        EventLog.addProperty(LOG_RESPONSE, logResponse);

        // Read Excluded fields
        JsonArray ExcludedFields = new JsonArray();
        if (ObjectData.has(PARAM_EXCLUDED_FIELDS))
            ExcludedFields = ObjectData.get(PARAM_EXCLUDED_FIELDS).getAsJsonArray();

        // Read Masking Fields
        JsonArray MaskedFields = new JsonArray();
        if (ObjectData.has(PARAM_MASKED_FIELDS))
            MaskedFields = ObjectData.get(PARAM_MASKED_FIELDS).getAsJsonArray();

        // Read Masking Logic
        String maskingLogic = StringUtils.EMPTY;
        if (EventLogProperties.has(PARAM_MASKING_LOGIC))
            maskingLogic = EventLogProperties.get(PARAM_MASKING_LOGIC).getAsString();

        // Process GET requests
        if (logResponse.equalsIgnoreCase(PARAM_FALSE)) {

            // Exclude the from request
            if (ExcludedFields != null && ExcludedFields.size() > 0) {
                for (JsonElement jsonElement : ExcludedFields) {
                    String Field = jsonElement.getAsString();
                    if (request != null && !request.entrySet().isEmpty()) {
                        if (request.has(Field)) {
                            request.remove(Field);
                        }
                    } else {
                        break;
                    }
                }
            }

            // Mask the fields from request
            if (MaskedFields != null && MaskedFields.size() > 0) {
                for (JsonElement jsonElement : MaskedFields) {
                    String Field = jsonElement.getAsString();
                    if (request != null && !request.entrySet().isEmpty()) {
                        if (request.has(Field)) {
                            String MaskedValue = MaskFields(request.get(Field).getAsString(), maskingLogic);
                            request.remove(Field);
                            request.addProperty(Field, MaskedValue);
                        }
                    } else {
                        break;
                    }
                }
            }

            // Read the main array to calculate no of records in response
            if (response != null && !response.entrySet().isEmpty()) {
                Set<Map.Entry<String, JsonElement>> entries = response.entrySet();
                for (Map.Entry<String, JsonElement> entry : entries) {
                    if (response.get(entry.getKey()).isJsonArray()) {
                        response.addProperty("noOf" + entry.getKey(),
                                response.get(entry.getKey()).getAsJsonArray().size());
                        response.remove(entry.getKey());
                        break;
                    }
                }
            }
        } else {
            // Get the inner array of response - getAccountDetails - Accounts -
            // 1 level
            String ArrayName = StringUtils.EMPTY;
            JsonArray records = new JsonArray();
            JsonObject record = new JsonObject();
            if (response != null && !response.entrySet().isEmpty()) {
                Set<Map.Entry<String, JsonElement>> entries = response.entrySet();
                for (Map.Entry<String, JsonElement> entry : entries) {
                    if (response.get(entry.getKey()).isJsonArray()) {
                        ArrayName = entry.getKey();
                        records = response.get(entry.getKey()).getAsJsonArray();
                        if (records.size() > 0)
                            if (records.get(0).isJsonObject())
                                record = records.get(0).getAsJsonObject();
                    }
                }
            }
            // Exclude the fields from request and response
            if (ExcludedFields != null && ExcludedFields.size() > 0) {
                for (JsonElement jsonElement : ExcludedFields) {
                    String Field = jsonElement.getAsString();
                    if (response != null && !response.entrySet().isEmpty()) {
                        if (response.has(Field)) {
                            response.remove(Field);
                        }
                        if (record != null && !record.entrySet().isEmpty()) {
                            if (record.has(Field)) {
                                record.remove(Field);
                            }
                        }
                    }
                    if (request != null && !request.entrySet().isEmpty()) {
                        if (request.has(Field)) {
                            request.remove(Field);
                        }
                    }
                }
            }

            // Mask the fields from request and response
            if (MaskedFields != null && MaskedFields.size() > 0) {
                for (JsonElement jsonElement : MaskedFields) {
                    String Field = jsonElement.getAsString();
                    if (response != null && !response.entrySet().isEmpty()) {
                        if (response.has(Field)) {
                            String MaskedValue = MaskFields(response.get(Field).getAsString(), maskingLogic);
                            response.remove(Field);
                            response.addProperty(Field, MaskedValue);
                        }
                        if (record != null && !record.entrySet().isEmpty()) {
                            if (record.has(Field)) {
                                String MaskedValue = MaskFields(record.get(Field).getAsString(), maskingLogic);
                                record.remove(Field);
                                record.addProperty(Field, MaskedValue);
                            }
                        }
                    }
                    if (request != null && !request.entrySet().isEmpty()) {
                        if (request.has(Field)) {
                            String MaskedValue = MaskFields(request.get(Field).getAsString(), maskingLogic);
                            request.remove(Field);
                            request.addProperty(Field, MaskedValue);
                        }
                    }
                }
            }
            // Remove the old array and add new array
            if (StringUtils.isNotBlank(ArrayName)) {
                response.remove(ArrayName);
                if (records.size() > 0) {
                    records.remove(0);
                    records.add(record);
                    response.add(ArrayName, records);
                }
            }
        }

        // Set Event Status
        String opstatus = StringUtils.EMPTY;
        String StatusID = PARAM_SID_EVENT_FAILURE;
        if (response != null && !response.entrySet().isEmpty()) {
            if (response.has(PARAM_OP_STATUS)) {
                opstatus = response.get(PARAM_OP_STATUS).getAsString();
            }

            if (opstatus.equals("0") && !response.has(PARAM_DBP_ERR_CODE)) {
                StatusID = PARAM_SID_EVENT_SUCCESS;
            }
        }
        EventLog.addProperty("StatusID", StatusID);

        // Build CustomParams
        JsonObject customParam = new JsonObject();
        customParam.addProperty("serviceId", ServiceId);
        customParam.addProperty("ObjectId", ObjectId);
        customParam.addProperty("OperationId", OperationId);

        EventLog.add("request", request);
        EventLog.add("response", response);
        EventLog.add("customParams", customParam);
        EventLog.addProperty("ProcessError", "false");

        return EventLog;
    }

    // Read properties file load in hashmap
    public JsonObject LoadEventLogProperties(FabricRequestManager fabricRequestManager) throws FileNotFoundException {

        JsonObject EventLogProperties = new JsonObject();
        InputStream inputStream = EventLogUtils.class.getClassLoader().getResourceAsStream("LoanPayoffEventLog.json");
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            alert.prepareError("Unable to read file" + e).log();
        }finally {
        	if (inputStream!=null) {
        		try {
        			inputStream.close();			
        		}
        		catch(Exception e)
        		{
        			alert.prepareError(e.toString()).log();
        		}
        	}
        }

        EventLogProperties = new Gson().fromJson(bufferedReader, JsonObject.class);

        return EventLogProperties;
    }

    // Masking fields
    /*
     * Input - AccountNumber - 100829839 OutPut - AccountNumber - XXXXX9839
     */
    public String MaskFields(String value, String maskingLogic) {
        String Maskedvalue = StringUtils.EMPTY;
        switch (maskingLogic) {
        case MASK_LAST_4_DIGITS:
            if (value != null && !value.isEmpty()) {
                String lastFourDigits;
                if (value.length() > 4)
                    lastFourDigits = value.substring(value.length() - 4);
                else
                    lastFourDigits = value;

                Maskedvalue = "XXXX" + lastFourDigits;
            }
        case MASK_FIRST_4_DIGITS:
            if (value != null && !value.isEmpty()) {
                String firstFourDigits;
                if (value.length() > 4)
                    firstFourDigits = value.substring(0, 4);
                else
                    firstFourDigits = value;

                Maskedvalue = firstFourDigits + "XXXX";
            }
        default:
            break;
        }
        return Maskedvalue;
    }

    /*
     * Fetches the customer attributes from the identity session
     */
    public static Map<String, Object> getCustomerMap(FabricRequestManager request) {

        try {
            Map<String, Object> customer = request.getServicesManager().getIdentityHandler().getUserAttributes();
            return customer;
        } catch (MiddlewareException e) {
            alert.prepareError("Error while fetching customer attributes from the identity session", e).log();
        } catch (NullPointerException e) {
            alert.prepareError(e.toString()).log();
        }
        return null;
    }

    /*
     * Fetches the Customer_Name from the customer map and returns it
     */
    public static String getCustomerName(Map<String, Object> customer) {

        try {
            return customer.get("UserName").toString();
        } catch (NullPointerException e) {
            alert.prepareError("Error while fetching UserName from customer map", e).log();
        }
        return null;
    }

}
