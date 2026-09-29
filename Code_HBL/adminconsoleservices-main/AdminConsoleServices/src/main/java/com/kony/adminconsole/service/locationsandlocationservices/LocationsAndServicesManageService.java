package com.kony.adminconsole.service.locationsandlocationservices;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/**
 * Service to manage the Location, their schedules and their corresponding Location Records(Create,Update,Delete)
 *
 * @author Aditya Mankal
 * 
 */
public class LocationsAndServicesManageService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    private static final String DELETE_LOCATION_METHOD_NAME = "DeleteLocation";
    private static final String UPDATE_LOCATION_AND_LOCATION_SERVICES_METHOD_NAME = "UpdateLocationAndLocationServices";
//    public static final Pattern CODE_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s]*$");
    private static final int PHONE_NUMBER_MAX_DIGITS = 21;
    private static final int LOCATION_CODE_MAX_CHARS = 10;
    private static final int LOCATION_NAME_MAX_CHARS = 100;
    private static final int LOCATION_DISPLAY_NAME_MAX_CHARS = 100;
    private static final int LOCATION_DESCRIPTION_MAX_CHARS = 200;
    private static final int ADDRESS_MAX_CHARS = 80;
    private static final int ZIPCODE_MAX_CHARS = 20;
    

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        try {

            diagnostic.prepareDebug("Method Id:" + methodID).log();

            // Fetch Logged-in User Details
            UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(requestInstance);
            String loggedInUserId = userDetailsBeanInstance.getId();

            // Read Input Parameters
            String locationId = requestInstance.getParameter("Location_id");
            String locationStatusId = requestInstance.getParameter("Status_id");
            String locationOfflineOnlineStatus = requestInstance.getParameter("Location_OfflineOnlineStatus");
            String locationDetailsString = requestInstance.getParameter("Location_Details");
            String serviceDetailsString = requestInstance.getParameter("Service_Details");
            String addressDetailsString = requestInstance.getParameter("Address_Details");
            String scheduleDetailsString = requestInstance.getParameter("Schedule_Details");
            String customerSegmentString = requestInstance.getParameter("CustomerSegment_Details");
            String supportedCurrencyString = requestInstance.getParameter("SupportedCurrency_Details");
            String workScheduleId = null, addressId = null;

            JSONObject addressDetailsJSONObject = CommonUtilities.getStringAsJSONObject(addressDetailsString);
            JSONObject scheduleDetailsJSONObject = CommonUtilities.getStringAsJSONObject(scheduleDetailsString);
            JSONObject locationDetailsJSONObject = CommonUtilities.getStringAsJSONObject(locationDetailsString);
            JSONObject serviceDetailsJSONObject = CommonUtilities.getStringAsJSONObject(serviceDetailsString);
            JSONArray customerSegmentJSONArray = CommonUtilities.getStringAsJSONArray(customerSegmentString);
            JSONArray supportedCurrencyJSONArray = CommonUtilities.getStringAsJSONArray(supportedCurrencyString);

            Result processedResult = new Result();
            boolean isCreateRequest = false;

            if (StringUtils.equalsIgnoreCase(methodID, UPDATE_LOCATION_AND_LOCATION_SERVICES_METHOD_NAME)) {
                // Update Request

                // Validate Location Id
                if (StringUtils.isBlank(locationId)) {
                    ErrorCodeEnum.ERR_20363.setErrorCode(processedResult);
                    Param errorParam = new Param("validationError", "Location ID cannot be null.",
                            FabricConstants.STRING);
                    processedResult.addParam(errorParam);
                    return processedResult;
                }

                if (StringUtils.isNotBlank(locationOfflineOnlineStatus)) {
                    // Request is to Toggle Location Status.
                    diagnostic.prepareDebug("Request is to Toggle Location Status. Proceeding Further..").log();
                    processedResult.addRecord(toggleLocationStatus(locationId, locationOfflineOnlineStatus,
                            loggedInUserId, requestInstance));
                    return processedResult;
                }

                // Validate Work Schedule Id
                if (scheduleDetailsJSONObject != null) {
                    workScheduleId = scheduleDetailsJSONObject.optString("WorkScheduleID");
                    if (StringUtils.isBlank(workScheduleId)) {
                        ErrorCodeEnum.ERR_20363.setErrorCode(processedResult);
                        Param errorParam = new Param("validationError", " Work Schedule ID cannot be null.",
                                FabricConstants.STRING);
                        processedResult.addParam(errorParam);
                        return processedResult;
                    }
                }

                // Validate Address Id
                if (addressDetailsJSONObject != null) {
                    addressId = addressDetailsJSONObject.optString("AddressID");
                    if (StringUtils.isBlank(addressId)) {
                        ErrorCodeEnum.ERR_20363.setErrorCode(processedResult);
                        Param errorParam = new Param("validationError", " Address ID cannot be null.",
                                FabricConstants.STRING);
                        processedResult.addParam(errorParam);
                        return processedResult;
                    }

                }

            } else if (StringUtils.equalsIgnoreCase(methodID, DELETE_LOCATION_METHOD_NAME)) {
                // Deactivate Location
                if (StringUtils.isBlank(locationId)) {
                    ErrorCodeEnum.ERR_20363.setErrorCode(processedResult);
                    Param errorParam = new Param("Error: Invalid Input. Location ID cannot be null.", "ErrorMessage",
                            FabricConstants.STRING);
                    processedResult.addParam(errorParam);
                    return processedResult;
                }
                processedResult
                        .addRecord(toggleLocationStatus(locationId, "Deactivate", loggedInUserId, requestInstance));
                // Deactivating location when delete location is called.
                return processedResult;

            } else {
                // Create Request. Generate Random ID values
                isCreateRequest = true;
                locationId = String.valueOf(CommonUtilities.getNumericId());
                workScheduleId = CommonUtilities.getNewId().toString();
                addressId = CommonUtilities.getNewId().toString();
            }

            // Set Location Address
            if (addressDetailsJSONObject != null) {
                Record manageLocationAddressResponseRecord = manageLocationAddress(addressDetailsJSONObject, addressId,
                        loggedInUserId, isCreateRequest, requestInstance);
                processedResult.addRecord(manageLocationAddressResponseRecord);
            }

            // Set Location Schedule
            if (scheduleDetailsJSONObject != null) {
                // Set Location Work Schedule
                Record locationWorkScheduleParam = setLocationWorkSchedule(workScheduleId, isCreateRequest,
                        scheduleDetailsJSONObject, loggedInUserId, requestInstance);
                processedResult.addRecord(locationWorkScheduleParam);

                // Set Location Day Schedule
                Record locationDayScheduleParam = setLocationDaySchedule(workScheduleId, scheduleDetailsJSONObject,
                        isCreateRequest, loggedInUserId, requestInstance);
                processedResult.addRecord(locationDayScheduleParam);
            }

            // Set Location Definition
            Record locationDefinitionRecord = setLocationDetails(locationDetailsJSONObject, locationId, addressId,
                    workScheduleId, locationStatusId, loggedInUserId, isCreateRequest, requestInstance);
            processedResult.addRecord(locationDefinitionRecord);

            // Set Location Services
            if (serviceDetailsJSONObject != null) {
                Record locationServicesRecord = setLocationServices(serviceDetailsJSONObject, locationId,
                        loggedInUserId, requestInstance);
                processedResult.addRecord(locationServicesRecord);
            }

            // Set Location Customer Segments
            if (customerSegmentString != null && locationDetailsJSONObject != null
                    && locationDetailsJSONObject.optString("Type_id").equals("Branch")) {
                Record customerSegmentsRecord = setLocationCustomerSegments(customerSegmentJSONArray, locationId,
                        loggedInUserId, requestInstance);
                processedResult.addRecord(customerSegmentsRecord);
            }

            // Set Location Currencies
            if (supportedCurrencyString != null && locationDetailsJSONObject != null
                    && locationDetailsJSONObject.optString("Type_id").equals("ATM")) {
                Record locationCurrenciesRecord = setLocationCurrencies(supportedCurrencyJSONArray, locationId,
                        loggedInUserId, requestInstance);
                processedResult.addRecord(locationCurrenciesRecord);
            }

            // Return Result
            return processedResult;
        } catch (ApplicationException e) {
            Result errorResult = new Result();
            alert.prepareError("Application Exception. Checked Involved Operations. Exception Trace:", e).log();
            e.getErrorCodeEnum().setErrorCode(errorResult);
            return errorResult;
        } catch (Exception e) {
            Result errorResult = new Result();
            ErrorCodeEnum.ERR_20355.setErrorCode(errorResult);
            return errorResult;
        }
    }

    /**
     * Method to create Location Schedule
     * 
     * @param workScheduleId
     * @param loggedInUserId
     * @param weekDayStartTime
     * @param weekDayEndTime
     * @param weekEndStartTime
     * @param weekEndEndTime
     * @param listOfAddedWeekendDays
     * @param requestInstance
     * @return Status Param
     * @throws ApplicationException
     */
    private Param createLocationSchedule(String workScheduleId, String loggedInUserId, String weekDayStartTime,
            String weekDayEndTime, String weekEndStartTime, String weekEndEndTime, List<String> listOfAddedWeekendDays,
            DataControllerRequest requestInstance) throws ApplicationException {

        Param statusParam = new Param("status", "Success", FabricConstants.STRING);

        String weekDayStartHour = StringUtils.isNotBlank(weekDayStartTime) ? weekDayStartTime.substring(0,2) : "";
        String weekDayEndHour = StringUtils.isNotBlank(weekDayEndTime) ? weekDayEndTime.substring(0,2) : "";
        String weekEndStartHour = StringUtils.isNotBlank(weekEndStartTime) ? weekEndStartTime.substring(0,2) : "";
        String weekEndEndHour = StringUtils.isNotBlank(weekEndEndTime) ? weekEndEndTime.substring(0,2) : "";
        
        boolean isValidLocationData = true;
        StringBuffer errorMessageBuffer = new StringBuffer();
        errorMessageBuffer.append("ERROR:");
        
        // Prepare Input Map
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("WorkSchedule_id", workScheduleId);
        inputMap.put("createdby", loggedInUserId);
        inputMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());

        String currDayName = StringUtils.EMPTY;
        String currDayScheduleId = StringUtils.EMPTY;
        String operationResponse = StringUtils.EMPTY;
        JSONObject operationResponseJSON = null;

        // Create Day-Schedule for 7 days of the Week
        for (DayOfWeek dayObject : DayOfWeek.values()) {
            currDayName = dayObject.name();
            currDayScheduleId = CommonUtilities.getNewId().toString();
            inputMap.put("id", currDayScheduleId);
            inputMap.put("WeekDayName", currDayName);

            if (isWeekDay(currDayName)) {
                // Weekday
	      	    if (StringUtils.isBlank(weekDayStartTime)) {
	            	isValidLocationData = false;
	                errorMessageBuffer.append("Weekday Start Time cannot be null.");
	            } else if (!Pattern.matches("^(\\d{2}:\\d{2}:\\d{2})$", weekDayStartTime)) {
	            	isValidLocationData = false;
	                errorMessageBuffer.append("Weekday Start Time contains malicious characters or incorrect format");
	            } else if (NumberUtils.isParsable(weekDayStartHour) && NumberUtils.isParsable(weekDayEndHour)) {
	            	if (Integer.parseInt(weekDayStartHour) > Integer.parseInt(weekDayEndHour)) {
	                  	isValidLocationData = false;
	                    errorMessageBuffer.append("Insert valid time");
	                } else {                            
	                	inputMap.put("StartTime", weekDayStartTime);
	                }
	            }
	              
	            if (StringUtils.isBlank(weekDayEndTime)) {
                	isValidLocationData = false;
                    errorMessageBuffer.append("Weekday End Time cannot be null.");
                } else if (!Pattern.matches("^(\\d{2}:\\d{2}:\\d{2})$", weekDayEndTime)) {
                	isValidLocationData = false;
                    errorMessageBuffer.append("Weekday End Time contains malicious characters or incorrect format");
                } else if (NumberUtils.isParsable(weekDayStartHour) && NumberUtils.isParsable(weekDayEndHour)) {
                	if (Integer.parseInt(weekDayStartHour) > Integer.parseInt(weekDayEndHour)) {
                		isValidLocationData = false;
                        errorMessageBuffer.append("Insert valid time");
                    } else {
                	 inputMap.put("EndTime", weekDayEndTime);
                    }
                }                            
		          
		        if (!isValidLocationData) {
		           diagnostic.prepareDebug("Invalid Location Details: Info:" + errorMessageBuffer.toString()).log();
		           throw new ApplicationException(ErrorCodeEnum.ERR_20357);
		        }
            } else {
                // Weekend
                if (!listOfAddedWeekendDays.contains(currDayName))
                    continue;

                if (StringUtils.isNoneBlank(weekEndStartTime)) {
                	if (!Pattern.matches("^(\\d{2}:\\d{2}:\\d{2})$", weekEndStartTime)) {
                    	isValidLocationData = false;
                        errorMessageBuffer.append("Weekend Start Time contains malicious characters or incorrect format");
                    } else if (NumberUtils.isParsable(weekEndStartHour) && NumberUtils.isParsable(weekEndEndHour)) {
                    	if (Integer.parseInt(weekEndStartHour) > Integer.parseInt(weekEndEndHour)) {
                        	isValidLocationData = false;
                            errorMessageBuffer.append("Insert valid time");
                        } else {                            
                    	 inputMap.put("StartTime", weekEndStartTime);
                    	}
                    }
               } 

                if (StringUtils.isNoneBlank(weekEndEndTime)) {
                	if (!Pattern.matches("^(\\d{2}:\\d{2}:\\d{2})$", weekEndEndTime)) {
                    	isValidLocationData = false;
                        errorMessageBuffer.append("Weekend End Time contains malicious characters or incorrect format");
                    } else if (NumberUtils.isParsable(weekEndStartHour) && NumberUtils.isParsable(weekEndEndHour)) {
                    	if (Integer.parseInt(weekEndStartHour) > Integer.parseInt(weekEndEndHour)) {
                        	isValidLocationData = false;
                            errorMessageBuffer.append("Insert valid time");
                        } else {                            
                		    inputMap.put("EndTime", weekEndEndTime);
                    	}
                    }
                }      
                
                
                if (!isValidLocationData) {
 		           diagnostic.prepareDebug("Invalid Location Details: Info:" + errorMessageBuffer.toString()).log();
 		           throw new ApplicationException(ErrorCodeEnum.ERR_20357);
 		        }
            }
            operationResponse = Executor.invokeService(ServiceURLEnum.DAYSCHEDULE_CREATE, inputMap, null,
                    requestInstance);
            operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);

            if (operationResponseJSON == null || !operationResponseJSON.has(FabricConstants.OPSTATUS)
                    || operationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
                    || !operationResponseJSON.has("dayschedule")) {
                alert.prepareError("Failed CRUD Operation").log();
                throw new ApplicationException(ErrorCodeEnum.ERR_20362);

            }
            diagnostic.prepareDebug("Successful CRUD Operation").log();
        }

        return statusParam;

    }

    /**
     * Method to set the Location Day Schedule
     * 
     * @param workScheduleId
     * @param scheduleDetailsJSONObject
     * @param isCreateRequest
     * @param loggedInUserId
     * @param requestInstance
     * @throws ApplicationException
     */
    private Record setLocationDaySchedule(String workScheduleId, JSONObject scheduleDetailsJSONObject,
            boolean isCreateRequest, String loggedInUserId, DataControllerRequest requestInstance)
            throws ApplicationException {

        Record operationRecord = new Record();
        operationRecord.setId("daySchedule");

        Param statusParam = new Param("status", "Success", FabricConstants.STRING);
        operationRecord.addParam(statusParam);

        String weekDayStartTime = scheduleDetailsJSONObject.optString("WeekDayStartTime");
        String weekDayEndTime = scheduleDetailsJSONObject.optString("WeekDayEndTime");
        String weekEndStartTime = scheduleDetailsJSONObject.optString("WeekEndStartTime");
        String weekEndEndTime = scheduleDetailsJSONObject.optString("WeekEndEndTime");

        String weekDayStartHour = StringUtils.isNotBlank(weekDayStartTime) ? weekDayStartTime.substring(0,2) : "";
        String weekDayEndHour = StringUtils.isNotBlank(weekDayEndTime) ? weekDayEndTime.substring(0,2) : "";
        String weekEndStartHour = StringUtils.isNotBlank(weekEndStartTime) ? weekEndStartTime.substring(0,2) : "";
        String weekEndEndHour = StringUtils.isNotBlank(weekEndEndTime) ? weekEndEndTime.substring(0,2) : "";
        	
        ArrayList<String> listOfAddedWeekendDays = new ArrayList<String>();
        ArrayList<String> listOfRemovedWeekendDays = new ArrayList<String>();

        if (scheduleDetailsJSONObject.has("WeekendWorkingDays")) {
            JSONObject weekendWorkingDaysJSON = scheduleDetailsJSONObject.getJSONObject("WeekendWorkingDays");
            listOfAddedWeekendDays = CommonUtilities
                    .getStringElementsOfJSONArrayToArrayList(weekendWorkingDaysJSON.getJSONArray("AddedDays"));
            listOfRemovedWeekendDays = CommonUtilities
                    .getStringElementsOfJSONArrayToArrayList(weekendWorkingDaysJSON.getJSONArray("RemovedDays"));
        }

        Map<String, String> inputMap = new HashMap<>();
        String operationResponse = StringUtils.EMPTY;
        JSONObject operationResponseJSON = null;

        boolean isValidLocationData = true;
        StringBuffer errorMessageBuffer = new StringBuffer();
        errorMessageBuffer.append("ERROR:");
        
        if (isCreateRequest == true) {
            // Create Location Schedule
            statusParam = createLocationSchedule(workScheduleId, loggedInUserId, weekDayStartTime, weekDayEndTime,
                    weekEndStartTime, weekEndEndTime, listOfAddedWeekendDays, requestInstance);
        } else {
            // Update Location Schedule

            inputMap.clear();
            inputMap.put(ODataQueryConstants.FILTER, "WorkSchedule_id eq '" + workScheduleId + "'");

            // Fetch Existing Day-Schedule Association
            operationResponse = Executor.invokeService(ServiceURLEnum.DAYSCHEDULE_READ, inputMap, null,
                    requestInstance);
            operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);
            if (operationResponseJSON == null || !operationResponseJSON.has(FabricConstants.OPSTATUS)
                    || operationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
                    || !operationResponseJSON.has("dayschedule")) {
                alert.prepareError("Failed CRUD Operation").log();
                throw new ApplicationException(ErrorCodeEnum.ERR_20361);
            }

            diagnostic.prepareDebug("Successful CRUD Operation").log();

            JSONObject currDayScheduleObject = null;
            String currDayName = StringUtils.EMPTY;
            String currDayScheduleId = StringUtils.EMPTY;
            JSONArray dayscheduleArray = operationResponseJSON.getJSONArray("dayschedule");

            if (dayscheduleArray == null || dayscheduleArray.length() == 0) {
                // Indicates that no schedule has been associated. Associate Day Schedule(s)
                statusParam = createLocationSchedule(workScheduleId, loggedInUserId, weekDayStartTime, weekDayEndTime,
                        weekEndStartTime, weekEndEndTime, listOfAddedWeekendDays, requestInstance);
            } else {

                List<String> weekDays = getWeekDays();// List contains the 5 weekdays

                // Traverse Day-Schedule Array - Update Existing Day Schedule
                for (Object object : dayscheduleArray) {

                    if (object instanceof JSONObject) {

                        currDayScheduleObject = (JSONObject) object;
                        currDayScheduleId = currDayScheduleObject.optString("id");
                        currDayName = currDayScheduleObject.getString("WeekDayName");

                        inputMap.clear();
                        inputMap.put("id", currDayScheduleId);
                        inputMap.put("WorkSchedule_id", workScheduleId);

                        if (!isWeekDay(currDayName)) {

                            // Current Day is a Weekend
                            if (listOfAddedWeekendDays.contains(currDayName)) {
                                // Existing weekend. Indicates only a schedule update
                                listOfAddedWeekendDays.remove(currDayName);
                            }

                            if (listOfRemovedWeekendDays.contains(currDayName)) {
                                // Existing weekend. Indicates that it was removed
                                listOfRemovedWeekendDays.remove(currDayName);
                                Executor.invokeService(ServiceURLEnum.DAYSCHEDULE_DELETE, inputMap, null,
                                        requestInstance);
                                continue;
                            }

                            if (StringUtils.isNoneBlank(weekEndStartTime)) {
                            	if (!Pattern.matches("^(\\d{2}:\\d{2}:\\d{2})$", weekEndStartTime)) {
                            		isValidLocationData = false;
                                    errorMessageBuffer.append("Weekend Start Time contains malicious characters or incorrect format");
                                } else if (NumberUtils.isParsable(weekEndStartHour) && NumberUtils.isParsable(weekEndEndHour)) {
                                	if (Integer.parseInt(weekEndStartHour) > Integer.parseInt(weekEndEndHour)) {
                                		isValidLocationData = false;
	                                    errorMessageBuffer.append("Insert valid time");
	                                } else {      
	                                	inputMap.put("StartTime", weekEndStartTime);
                                	}
                                }
                           } 
                           
                            if (StringUtils.isNoneBlank(weekEndEndTime)) {
                            	if (!Pattern.matches("^(\\d{2}:\\d{2}:\\d{2})$", weekEndEndTime)) {
                                	isValidLocationData = false;
                                    errorMessageBuffer.append("Weekend End Time contains malicious characters or incorrect format");
                                } else if (NumberUtils.isParsable(weekEndStartHour) && NumberUtils.isParsable(weekEndEndHour)) {
                                	if (Integer.parseInt(weekEndStartHour) > Integer.parseInt(weekEndEndHour)) {
	                                	isValidLocationData = false;
	                                    errorMessageBuffer.append("Insert valid time");
	                                } else {    
	                            		inputMap.put("EndTime", weekEndEndTime);
                                	}
                                }
                            }
                            
                            if (!isValidLocationData) {
                                diagnostic.prepareDebug("Invalid Location Details: Info:" + errorMessageBuffer.toString()).log();
                                throw new ApplicationException(ErrorCodeEnum.ERR_20357);
                            }
                            
                        } else {
                            // Current Day is a Weekday
                        	if (StringUtils.isBlank(weekDayStartTime)) {
                            	isValidLocationData = false;
                                errorMessageBuffer.append("Weekday Start Time cannot be null.");
                            } else if (!Pattern.matches("^(\\d{2}:\\d{2}:\\d{2})$", weekDayStartTime)) {
                            	isValidLocationData = false;
                                errorMessageBuffer.append("Weekday Start Time contains malicious characters or incorrect format");
                            } else if (NumberUtils.isParsable(weekDayStartHour) && NumberUtils.isParsable(weekDayEndHour)) {
                            	if (Integer.parseInt(weekDayStartHour) > Integer.parseInt(weekDayEndHour)) {
                                  	isValidLocationData = false;
	                                errorMessageBuffer.append("Insert valid time");
	                            } else {                     
	                            	inputMap.put("StartTime", weekDayStartTime);
	                            }
                            }
                            if (StringUtils.isBlank(weekDayEndTime)) {
                            	isValidLocationData = false;
                                errorMessageBuffer.append("Weekday End Time cannot be null.");
                            } else if (!Pattern.matches("^(\\d{2}:\\d{2}:\\d{2})$", weekDayEndTime)) {
                            	isValidLocationData = false;
                                errorMessageBuffer.append("Weekday End Time contains malicious characters or incorrect format");
                            } else if (NumberUtils.isParsable(weekDayStartHour) && NumberUtils.isParsable(weekDayEndHour)) {
                            	if (Integer.parseInt(weekDayStartHour) > Integer.parseInt(weekDayEndHour)) {
                            		isValidLocationData = false;
	                                errorMessageBuffer.append("Insert valid time");
	                            } else {
	                            	inputMap.put("EndTime", weekDayEndTime);
	                            }
                            }                            
                            
                            
                            if (!isValidLocationData) {
                                diagnostic.prepareDebug("Invalid Location Details: Info:" + errorMessageBuffer.toString()).log();
                                throw new ApplicationException(ErrorCodeEnum.ERR_20357);
                            }

                        }
                        inputMap.put("WeekDayName", currDayName);
                        inputMap.put("modifiedby", loggedInUserId);
                        inputMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());

                        operationResponse = Executor.invokeService(ServiceURLEnum.DAYSCHEDULE_UPDATE, inputMap, null,
                                requestInstance);
                        operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);

                        if (operationResponseJSON == null || !operationResponseJSON.has(FabricConstants.OPSTATUS)
                                || operationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
                                || !operationResponseJSON.has("dayschedule")) {
                            alert.prepareError("Failed CRUD Operation:" + ServiceURLEnum.DAYSCHEDULE_UPDATE.getServiceURL()).log();
                            throw new ApplicationException(ErrorCodeEnum.ERR_20360);
                        }
                        alert.prepareError("Successful CRUD Operation:" + ServiceURLEnum.DAYSCHEDULE_UPDATE.getServiceURL()).log();

                        weekDays.remove(currDayName);// Remove the current Day name from the list
                    }
                }

                // Handling the newly added weekend days
                if (StringUtils.isNotBlank(weekEndStartTime) && StringUtils.isNotBlank(weekEndEndTime)) {

                    for (String currWeekend : listOfAddedWeekendDays) {
                        currDayScheduleId = CommonUtilities.getNewId().toString();
                        inputMap.clear();
                        inputMap.put("id", currDayScheduleId);
                        inputMap.put("StartTime", weekEndStartTime);
                        inputMap.put("EndTime", weekEndEndTime);
                        inputMap.put("WeekDayName", currWeekend);
                        inputMap.put("WorkSchedule_id", workScheduleId);
                        inputMap.put("createdby", loggedInUserId);
                        inputMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
                        operationResponse = Executor.invokeService(ServiceURLEnum.DAYSCHEDULE_CREATE, inputMap, null,
                                requestInstance);
                        operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);
                        if (operationResponseJSON == null || !operationResponseJSON.has(FabricConstants.OPSTATUS)
                                || operationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
                                || !operationResponseJSON.has("dayschedule")) {
                            alert.prepareError("Failed CRUD Operation:" + ServiceURLEnum.DAYSCHEDULE_CREATE.getServiceURL()).log();
                            throw new ApplicationException(ErrorCodeEnum.ERR_20362);
                        }
                        diagnostic.prepareDebug("Successful CRUD Operation:" + ServiceURLEnum.DAYSCHEDULE_CREATE.getServiceURL()).log();
                    }
                }

                diagnostic.prepareDebug("Pending WeekDays:" + weekDays).log();
                // Handle the remaining Week Days - CORNER Case
                if (StringUtils.isNotBlank(weekDayStartTime) && StringUtils.isNotBlank(weekDayEndTime)) {

                    for (String currWeekDay : weekDays) {
                        currDayScheduleId = CommonUtilities.getNewId().toString();
                        inputMap.clear();
                        inputMap.put("id", currDayScheduleId);
                        inputMap.put("StartTime", weekDayStartTime);
                        inputMap.put("EndTime", weekDayEndTime);
                        inputMap.put("WeekDayName", currWeekDay);
                        inputMap.put("WorkSchedule_id", workScheduleId);
                        inputMap.put("createdby", loggedInUserId);
                        inputMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
                        operationResponse = Executor.invokeService(ServiceURLEnum.DAYSCHEDULE_CREATE, inputMap, null,
                                requestInstance);
                        operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);
                        if (operationResponseJSON == null || !operationResponseJSON.has(FabricConstants.OPSTATUS)
                                || operationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
                                || !operationResponseJSON.has("dayschedule")) {
                            alert.prepareError("Failed CRUD Operation:" + ServiceURLEnum.DAYSCHEDULE_CREATE.getServiceURL()).log();
                            throw new ApplicationException(ErrorCodeEnum.ERR_20362);
                        }
                        diagnostic.prepareDebug("Successful CRUD Operation:" + ServiceURLEnum.DAYSCHEDULE_CREATE.getServiceURL()).log();
                    }
                }

            }
        }
        return operationRecord;

    }

    /**
     * Method to get a List containing names of WeekDays
     * 
     * @return List containing names of WeekDays
     */
    private List<String> getWeekDays() {
        List<String> weekdays = new ArrayList<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            if (isWeekDay(day.name())) {
                weekdays.add(day.name());
            }
        }
        return weekdays;
    }

    /**
     * Method to set the location Work Schedule
     * 
     * @param workScheduleId
     * @param isCreateRequest
     * @param scheduleDetailsJSONObject
     * @param userId
     * @param requestInstance
     * @throws ApplicationException
     */
    private Record setLocationWorkSchedule(String workScheduleId, boolean isCreateRequest,
            JSONObject scheduleDetailsJSONObject, String userId, DataControllerRequest requestInstance)
            throws ApplicationException {

        Record operationRecord = new Record();
        operationRecord.setId("workSchedule");

        Param statusParam = new Param("status", "Success", FabricConstants.STRING);
        operationRecord.addParam(statusParam);

        // Prepare Input Map
        Map<String, String> inputMap = new HashMap<>();

        if (scheduleDetailsJSONObject.has("WorkScheduleDescription")) {

            EventEnum operation = null;
            ErrorCodeEnum errorCode = null;
            String manageWorkScheduleResponse = StringUtils.EMPTY;

            inputMap.put("id", workScheduleId);
            inputMap.put("Description", scheduleDetailsJSONObject.optString("WorkScheduleDescription"));

            if (isCreateRequest) {
                operation = EventEnum.CREATE;
                errorCode = ErrorCodeEnum.ERR_20359;
                inputMap.put("createdby", userId);
                inputMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
                manageWorkScheduleResponse = Executor.invokeService(ServiceURLEnum.WORKSCHEDULE_CREATE, inputMap, null,
                        requestInstance);
            } else {
                operation = EventEnum.UPDATE;
                errorCode = ErrorCodeEnum.ERR_20358;
                inputMap.put("modifiedby", userId);
                inputMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());
                manageWorkScheduleResponse = Executor.invokeService(ServiceURLEnum.WORKSCHEDULE_UPDATE, inputMap, null,
                        requestInstance);
            }

            JSONObject manageWorkScheduleResponseJSON = CommonUtilities
                    .getStringAsJSONObject(manageWorkScheduleResponse);
            if (manageWorkScheduleResponseJSON != null && manageWorkScheduleResponseJSON.has(FabricConstants.OPSTATUS)
                    && manageWorkScheduleResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
                statusParam.setValue("Success");
                diagnostic.prepareDebug("Successful CRUD Operation").log();
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.LOCATIONS, operation,
                        ActivityStatusEnum.SUCCESSFUL, "WorkSchedule ID:" + workScheduleId);
            } else {
                alert.prepareError("Failed CRUD Operation").log();
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.LOCATIONS, operation,
                        ActivityStatusEnum.FAILED, "WorkSchedule ID:" + workScheduleId);
                throw new ApplicationException(errorCode);
            }
        }

        return operationRecord;
    }

    /**
     * Method to set the Location Details
     * 
     * @param locationDetailsJSONObject
     * @param locationId
     * @param locationAddressId
     * @param workScheduleId
     * @param locationStatusId
     * @param loggedInUserId
     * @param isCreateRequest
     * @param requestInstance
     * @return operation Record
     * @throws ApplicationException
     */
    private Record setLocationDetails(JSONObject locationDetailsJSONObject, String locationId, String locationAddressId,
            String workScheduleId, String locationStatusId, String loggedInUserId, boolean isCreateRequest,
            DataControllerRequest requestInstance) throws ApplicationException {

        Record operationRecord = new Record();
        operationRecord.setId("locationDetails");

        Param statusParam = new Param("status", "Success", FabricConstants.STRING);
        operationRecord.addParam(statusParam);

        boolean isValidLocationData = true;
        StringBuffer errorMessageBuffer = new StringBuffer();
        errorMessageBuffer.append("ERROR:");

        // Validate Inputs and Prepare Input Map
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("id", locationId);

        // Read Inputs
        if (locationDetailsJSONObject != null) {

            String locationName = locationDetailsJSONObject.optString("Name");
            String locationDisplayName = locationDetailsJSONObject.optString("DisplayName");
            String locationDescription = locationDetailsJSONObject.optString("Description");
            String locationTypeId = locationDetailsJSONObject.optString("Type_id");
            String locationCode = locationDetailsJSONObject.optString("Code");
            String locationEmail = locationDetailsJSONObject.optString("Email");
            String locationIsMainBranch = locationDetailsJSONObject.optString("IsMainBranch");
            String locationPhoneNumber = locationDetailsJSONObject.optString("PhoneNumber");
            String locationURL = locationDetailsJSONObject.optString("WebsiteURL");
            String locationBankType = locationDetailsJSONObject.optString("BankType");

            //Description validation
            if (locationDescription.length() > LOCATION_DESCRIPTION_MAX_CHARS) {
                isValidLocationData = false;
                errorMessageBuffer.append("Description can have a maximum of " + LOCATION_DESCRIPTION_MAX_CHARS + " characters.");
            }else if (StringUtils.isBlank(locationDescription)) {
            	isValidLocationData = false;
                errorMessageBuffer.append("Description cannot be null.");
            }else if (!Pattern.matches("^([a-zA-Z0-9 ]+)$", locationDescription)) {
            	isValidLocationData = false;
                errorMessageBuffer.append("Description contains malicious characters");
            } else {
                inputMap.put("Description", locationDescription);
            }
            //Phone Number validation
            if (locationPhoneNumber.length() >= PHONE_NUMBER_MAX_DIGITS) {
                isValidLocationData = false;
                errorMessageBuffer.append("Phone Number can have a maximum of " + PHONE_NUMBER_MAX_DIGITS + " digits.");
            }else if (StringUtils.isBlank(locationPhoneNumber)) {
            	isValidLocationData = false;
                errorMessageBuffer.append("Phone Number cannot be null.");
            }else if (!isValidPhoneNumber(locationPhoneNumber)) {
            	isValidLocationData = false;
                errorMessageBuffer.append("Phone number contains malicious characters");
            } else {
                inputMap.put("PhoneNumber", locationPhoneNumber);
            }
            //Location code validation
            if (locationCode.length() > LOCATION_CODE_MAX_CHARS) {
                isValidLocationData = false;
                errorMessageBuffer.append("Location code can have a maximum of " + LOCATION_CODE_MAX_CHARS + " characters.");
            }else if (StringUtils.isBlank(locationCode)) {
            	isValidLocationData = false;
                errorMessageBuffer.append("Location code cannot be null.");
            }else if (!Pattern.matches("^(\\d{1,10})$", locationCode)) {
            	isValidLocationData = false;
                errorMessageBuffer.append("Location Code contains malicious characters.");
            }else {
                inputMap.put("Code", locationCode);
            }
            //Location name validation
            if (locationName.length() > LOCATION_NAME_MAX_CHARS) {
                isValidLocationData = false;
                errorMessageBuffer.append("Location name can have a maximum of of " + LOCATION_NAME_MAX_CHARS + " characters.");
            }else if (StringUtils.isBlank(locationName)) {
            	isValidLocationData = false;
                errorMessageBuffer.append("Location name cannot be null.");
            }else if (!Pattern.matches("^([a-zA-Z0-9 ]+)$", locationName)) {
            	isValidLocationData = false;
                errorMessageBuffer.append("Name contains malicious characters.");
            } else {
                inputMap.put("Name", locationName);
            }

            //Display name validation
            if (locationDisplayName.length() > LOCATION_DISPLAY_NAME_MAX_CHARS) {
                isValidLocationData = false;
                errorMessageBuffer.append("Location Display name can have a maximum of of " + LOCATION_DISPLAY_NAME_MAX_CHARS + " characters.");
            }else if (StringUtils.isBlank(locationDisplayName)) {
            	isValidLocationData = false;
                errorMessageBuffer.append("Display name cannot be null.");
            }else if (!Pattern.matches("^([a-zA-Z0-9 ]+)$", locationDisplayName)) {
            	isValidLocationData = false;
                errorMessageBuffer.append("Display Name contains malicious characters");
            } else {
                inputMap.put("DisplayName", locationDisplayName);
            }

            //Location Email address validation
            if (StringUtils.isBlank(locationEmail)) {
            	isValidLocationData = false;
                errorMessageBuffer.append("Email address cannot be null.");
            }else 
            if (!isValidEmailAddress(locationEmail)) {
                isValidLocationData = false;
                errorMessageBuffer.append("Invalid Email address format.");
            }else {
                inputMap.put("EmailId", locationEmail);
            }

          //Type Id validation
            if (StringUtils.isNotBlank(locationTypeId)) {
            	String typeId = locationTypeId.equalsIgnoreCase("Branch") || locationTypeId.equalsIgnoreCase("ATM") ? locationTypeId : "";
            	if(StringUtils.isNotBlank(typeId)) {
            		inputMap.put("Type_id", typeId);
            	}else {
                	isValidLocationData = false;
                    errorMessageBuffer.append("Type Id has invalid value.");
                } 
            } else {
            	isValidLocationData = false;
                errorMessageBuffer.append("Type Id cannot be null.");
            }
            
            if (StringUtils.isNoneBlank(locationURL)) 
                inputMap.put("WebsiteUrl", locationURL);
            if (locationTypeId.equalsIgnoreCase("Branch")) {
            	//Main Branch validation
            	if (StringUtils.isNotBlank(locationIsMainBranch)) {
                	String mainBranch = locationIsMainBranch.equalsIgnoreCase("false") || locationIsMainBranch.equalsIgnoreCase("true") ? locationIsMainBranch : "";
                	if(StringUtils.isNotBlank(mainBranch)) {
                		inputMap.put("IsMainBranch", locationIsMainBranch);
                	}else {
                		isValidLocationData = false;
                        errorMessageBuffer.append("Main Branch has invalid value.");
                    } 
                }       
            	//Bank Type validation
            if (StringUtils.isNotBlank(locationBankType)) {
                    String isMobile = locationBankType.equals("Mobile") ? "1" : locationBankType.equals("Physical") ? "0" : "";
                    if(StringUtils.isNotBlank(isMobile)) {
                inputMap.put("isMobile", isMobile);
                    } else {
    	            	isValidLocationData = false;
    	                errorMessageBuffer.append("Bank Type has invalid value.");
                    } 
                } else {
                	isValidLocationData = false;
                    errorMessageBuffer.append("Bank Type cannot be null.");
                }  
            }
            

            if (!isValidLocationData) {
                diagnostic.prepareDebug("Invalid Location Details: Info:" + errorMessageBuffer.toString()).log();
                throw new ApplicationException(ErrorCodeEnum.ERR_20357);

            }
        }

        if (StringUtils.isNotBlank(workScheduleId))
            inputMap.put("WorkSchedule_id", workScheduleId);
        if (StringUtils.isNotBlank(locationAddressId))
            inputMap.put("Address_id", locationAddressId);
        if (StringUtils.isNotBlank(locationStatusId))
            inputMap.put("Status_id", locationStatusId);

        if (isCreateRequest) {
            inputMap.put("createdby", loggedInUserId);
            inputMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
        } else {
            inputMap.put("modifiedby", loggedInUserId);
            inputMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());
        }

        // Create/Update Location
        EventEnum eventEnum = null;
        String manageLocationResponse = StringUtils.EMPTY;
        if (isCreateRequest) {
            manageLocationResponse = Executor.invokeService(ServiceURLEnum.LOCATION_CREATE, inputMap, null,
                    requestInstance);
            eventEnum = EventEnum.CREATE;
        } else {
            manageLocationResponse = Executor.invokeService(ServiceURLEnum.LOCATION_UPDATE, inputMap, null,
                    requestInstance);
            eventEnum = EventEnum.UPDATE;
        }

        // Verify Operation Status
        JSONObject manageLocationResponseJSON = CommonUtilities.getStringAsJSONObject(manageLocationResponse);
        if (manageLocationResponseJSON == null || !manageLocationResponseJSON.has(FabricConstants.OPSTATUS)
                || manageLocationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
            statusParam.setValue("Failure");
            alert.prepareError("Failed CRUD Operation").log();
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.LOCATIONS, eventEnum,
                    ActivityStatusEnum.FAILED, "Location Id" + locationId);
        } else {
            diagnostic.prepareDebug("Successful CRUD Operation").log();
            statusParam.setValue("Success");
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.LOCATIONS, eventEnum,
                    ActivityStatusEnum.SUCCESSFUL, "Location Id" + locationId);
        }
        return operationRecord;
    }

    /**
     * Method to set Location Address
     * 
     * @param addressDetailsJSONObject
     * @param addressId
     * @param loggedInUserId
     * @param isCreateRequest
     * @param requestInstance
     * @return
     * @throws ApplicationException
     */
    private Record manageLocationAddress(JSONObject addressDetailsJSONObject, String addressId, String loggedInUserId,
            boolean isCreateRequest, DataControllerRequest requestInstance) throws ApplicationException {

        Record operationRecord = new Record();
        operationRecord.setId("locationAddress");

        Param statusParam = new Param("status", "Success", FabricConstants.STRING);
        operationRecord.addParam(statusParam);

        Param addressIdParam = new Param("AddressIDParam", addressId, FabricConstants.STRING);
        operationRecord.addParam(addressIdParam);

        boolean isValidLocationData = true;
        StringBuffer errorMessageBuffer = new StringBuffer();
        errorMessageBuffer.append("ERROR:");

        String locationCityId = addressDetailsJSONObject.optString("City_id");
        String locationAddressLine1 = addressDetailsJSONObject.optString("addressLine1");
        String locationAddressLine2 = addressDetailsJSONObject.optString("addressLine2");
        String locationAddressLine3 = addressDetailsJSONObject.optString("addressLine3");
        String locationZipCode = addressDetailsJSONObject.optString("zipCode");
        String locationRegionId = addressDetailsJSONObject.optString("Region_id");
        String locationLongitude = addressDetailsJSONObject.optString("longitude");
        String locationLattitude = addressDetailsJSONObject.optString("latitude");
        String locationCityName = addressDetailsJSONObject.optString("cityName");
        String locationCountryId = addressDetailsJSONObject.optString("Country_id");

        Map<String, String> inputMap = new HashMap<String, String>();
        inputMap.put("id", addressId);

        if (locationAddressLine1.length() > ADDRESS_MAX_CHARS) {
            alert.prepareError("Address can have a maximum of " + ADDRESS_MAX_CHARS + " digits.").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20356);
        }
        if(!isValidParam(locationAddressLine1)|| !isValidParam(locationAddressLine2) || !isValidParam(locationAddressLine3)) {
        	isValidLocationData = false;
            errorMessageBuffer.append("Location Address contains malicious characters");
        }
        if (StringUtils.isBlank(locationAddressLine1)) {
        	isValidLocationData = false;
            errorMessageBuffer.append("Address cannot be null.");      
        } else {
            inputMap.put("addressLine1", locationAddressLine1);
        }
        if (StringUtils.isNotBlank(locationAddressLine2)) {
            inputMap.put("addressLine2", locationAddressLine2);
        }else {
        	inputMap.put("addressLine2",StringUtils.EMPTY+" ");
        }
        if (StringUtils.isNotBlank(locationAddressLine3)) {
            inputMap.put("addressLine3", locationAddressLine3);
        }
        if (StringUtils.isBlank(locationLattitude)) {
        	isValidLocationData = false;
            errorMessageBuffer.append("Lattitude cannot be null.");      
        } else if (!Pattern.matches("^([-]?(\\d+)((\\.\\d{1,6})?))$", locationLattitude)) {
        	isValidLocationData = false;
            errorMessageBuffer.append("Lattitude contains malicious characters");
        } else if (Double.parseDouble(locationLattitude) < -90 || Double.parseDouble(locationLattitude) > 90 ) {
        	isValidLocationData = false;
            errorMessageBuffer.append("Latitude should range from -90 to 90.");
        }else {
            inputMap.put("latitude", locationLattitude);
        }
        if (StringUtils.isBlank(locationLongitude)) {
        	isValidLocationData = false;
            errorMessageBuffer.append("Longitude cannot be null.");      
        } else if (!Pattern.matches("^([-]?(\\d+)((\\.\\d{1,6})?))$", locationLongitude)) {
        	isValidLocationData = false;
            errorMessageBuffer.append("Longitude contains malicious characters");
        } else if (Double.parseDouble(locationLongitude) < -180 || Double.parseDouble(locationLongitude) > 180 ) {
        	isValidLocationData = false;
            errorMessageBuffer.append("Longitude should range from -180 to 180.");
        }else {
            inputMap.put("logitude", locationLongitude);
        }
        if (locationZipCode.length() > ZIPCODE_MAX_CHARS) {
            alert.prepareError("Zip Code can have a maximum of " + ZIPCODE_MAX_CHARS + " digits.").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20356);
        } else if (StringUtils.isBlank(locationZipCode)) {
        	isValidLocationData = false;
            errorMessageBuffer.append("Zip Code cannot be null.");            
        } else if (!Pattern.matches("^([a-zA-Z0-9]+)$", locationZipCode)) {
        	isValidLocationData = false;
        	errorMessageBuffer.append("Zip Code contains malicious characters");
        } else {
            inputMap.put("zipCode", locationZipCode);
        }
        if (StringUtils.isBlank(locationRegionId)) {
        	isValidLocationData = false;
            errorMessageBuffer.append("Region Id cannot be null.");            
        } else if (!Pattern.matches("^([a-zA-Z]{2}-[a-zA-Z0-9]{1,3})$", locationRegionId)) {
        	isValidLocationData = false;
        	errorMessageBuffer.append("Region Id contains malicious characters");
        } else {
            inputMap.put("Region_id", locationRegionId);
        }
        if (StringUtils.isBlank(locationCountryId)) {
        	isValidLocationData = false;
            errorMessageBuffer.append("Country Id cannot be null.");            
        } else if (!Pattern.matches("^([a-zA-Z]{2})$", locationCountryId)) {
        	isValidLocationData = false;
        	errorMessageBuffer.append("Country Id contains malicious characters");
        } else {
        	inputMap.put("Country_id", locationCountryId);
        }
        if (StringUtils.isNotBlank(locationCityId)) {
            inputMap.put("City_id", locationCityId);
        }
        if (StringUtils.isNotBlank(locationCityName)) {
            inputMap.put("cityName", locationCityName);
        } else {
        	isValidLocationData = false;
            errorMessageBuffer.append("City Name cannot be null.");        
        }
        if (!isValidLocationData) {
            diagnostic.prepareDebug("Invalid Location Details: Info:" + errorMessageBuffer.toString()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20357);

        }

        EventEnum eventEnum = null;
        ErrorCodeEnum errorCode;
        String manageAddressReponse = StringUtils.EMPTY;
        if (isCreateRequest) {
            eventEnum = EventEnum.CREATE;
            errorCode = ErrorCodeEnum.ERR_20353;
            inputMap.put("createdby", loggedInUserId);
            inputMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
            manageAddressReponse = Executor.invokeService(ServiceURLEnum.ADDRESS_CREATE, inputMap, null,
                    requestInstance);
        } else {
            eventEnum = EventEnum.UPDATE;
            errorCode = ErrorCodeEnum.ERR_20354;
            inputMap.put("modifiedby", loggedInUserId);
            inputMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());
            manageAddressReponse = Executor.invokeService(ServiceURLEnum.ADDRESS_UPDATE, inputMap, null,
                    requestInstance);
        }
        JSONObject manageAddressReponseJSON = CommonUtilities.getStringAsJSONObject(manageAddressReponse);
        if (manageAddressReponseJSON == null || !manageAddressReponseJSON.has(FabricConstants.OPSTATUS)
                || manageAddressReponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
            alert.prepareError("Failed CRUD Operation").log();
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.LOCATIONS, eventEnum,
                    ActivityStatusEnum.FAILED, "Address Id:" + addressId);
            throw new ApplicationException(errorCode);
        } else {
            diagnostic.prepareDebug("Successful CRUD Operation").log();
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.LOCATIONS, eventEnum,
                    ActivityStatusEnum.SUCCESSFUL, "Address Id:" + addressId);
        }

        return operationRecord;
    }

    /**
     * Method to set the Location Services
     * 
     * @param serviceDetailsJSONObject
     * @param locationId
     * @param userId
     * @param requestInstance
     * @return
     * @throws ApplicationException
     */
    private Record setLocationServices(JSONObject serviceDetailsJSONObject, String locationId, String userId,
            DataControllerRequest requestInstance) throws ApplicationException {

        Record operationRecord = new Record();
        operationRecord.setId("locationServices");

        String currServiceId = StringUtils.EMPTY;
        String operationResponse = StringUtils.EMPTY;
        JSONObject operationResponseJSON = null;

        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("Location_id", locationId);

        // Traverse List of Added Services
        JSONArray listOfAddedServices = serviceDetailsJSONObject.optJSONArray("AddedServices");
        if (listOfAddedServices != null && listOfAddedServices.length() > 0) {
            inputMap.put("createdby", userId);

            for (int indexVar = 0; indexVar < listOfAddedServices.length(); indexVar++) {
                currServiceId = listOfAddedServices.optString(indexVar);
                inputMap.put("facility_id", currServiceId);
                inputMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
                operationResponse = Executor.invokeService(ServiceURLEnum.LOCATIONFACILITY_CREATE, inputMap, null,
                        requestInstance);
                operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);

                if (operationResponseJSON == null || !operationResponseJSON.has(FabricConstants.OPSTATUS)
                        || operationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
                    alert.prepareError("Failed CRUD Operation").log();
                    throw new ApplicationException(ErrorCodeEnum.ERR_20351);
                } else {
                    alert.prepareError("Successful CRUD Operation").log();
                }

            }

        }

        // Traverse List of Removed Services
        inputMap.clear();
        inputMap.put("Location_id", locationId);
        JSONArray listOfRemovedServices = serviceDetailsJSONObject.optJSONArray("RemovedServices");
        if (listOfRemovedServices != null & listOfRemovedServices.length() > 0) {

            for (int indexVar = 0; indexVar < listOfRemovedServices.length(); indexVar++) {
                currServiceId = listOfRemovedServices.optString(indexVar);
                inputMap.put("facility_id", currServiceId);
                operationResponse = Executor.invokeService(ServiceURLEnum.LOCATIONFACILITY_DELETE, inputMap, null,
                        requestInstance);
                operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);

                if (operationResponseJSON == null || !operationResponseJSON.has(FabricConstants.OPSTATUS)
                        || operationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
                    alert.prepareError("Failed CRUD Operation").log();
                    throw new ApplicationException(ErrorCodeEnum.ERR_20352);
                } else {
                    alert.prepareError("Successful CRUD Operation").log();
                }

            }
        }

        operationRecord.addParam(new Param("status", "Success", FabricConstants.STRING));
        return operationRecord;
    }

    /**
     * Method to set the Customer Location Segment
     * 
     * @param customersegmentJSONArray
     * @param locationId
     * @param userId
     * @param requestInstance
     * @return operation Record
     * @throws ApplicationException
     */
    private Record setLocationCustomerSegments(JSONArray customersegmentJSONArray, String locationId, String userId,
            DataControllerRequest requestInstance) throws ApplicationException {

        Record operationRecord = new Record();
        operationRecord.setId("setLocationCustomerSegment");
        operationRecord.addParam(new Param("Status", "Success", FabricConstants.STRING));

        String customersegmentName = StringUtils.EMPTY;
        String operationResponse = StringUtils.EMPTY;
        JSONObject operationResponseJSON = null;
        Map<String, String> inputMap = new HashMap<>();

        operationResponse = Executor.invokeService(ServiceURLEnum.CUSTOMERSEGMENT_READ, new HashMap<String, String>(),
                null, requestInstance);
        operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);
        Map<String, String> customersegmentMap = new HashMap<>();
        if (operationResponseJSON != null && operationResponseJSON.has(FabricConstants.OPSTATUS)
                && operationResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
                && operationResponseJSON.has("customersegment")) {
            diagnostic.prepareDebug("Successful CRUD Operation").log();
            JSONArray readResponseJSONArray = operationResponseJSON.getJSONArray("customersegment");
            for (int i = 0; i < readResponseJSONArray.length(); ++i) {
                customersegmentMap.put(readResponseJSONArray.getJSONObject(i).optString("type"),
                        readResponseJSONArray.getJSONObject(i).optString("id"));
            }
        } else {
            diagnostic.prepareDebug("Failed CRUD Operation").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20349);
        }
        
        // fetching previous location customer segments
        inputMap.put(ODataQueryConstants.FILTER, "Location_id eq '" + locationId + "'");
        operationResponse = Executor.invokeService(ServiceURLEnum.LOCATIONCUSTOMERSEGMENT_READ, inputMap, null,
                requestInstance);
        operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);
        JSONArray operationResponseJSONArray = operationResponseJSON.getJSONArray("locationcustomersegment");
        
        // deleting previous location customer segments
        inputMap = new HashMap<String, String>();
        inputMap.put("Location_id", locationId);
        for (int i = 0; i < operationResponseJSONArray.length(); i++) {
        	inputMap.put("segment_id", operationResponseJSONArray.getJSONObject(i).optString("segment_id"));
	        operationResponse = Executor.invokeService(ServiceURLEnum.LOCATIONCUSTOMERSEGMENT_DELETE, inputMap, null,
	                requestInstance);
	        operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);
	        if (operationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0)
	            throw new ApplicationException(ErrorCodeEnum.ERR_20335); 
        }

        // creating new location customer segments
        for (int indexVar = 0; indexVar < customersegmentJSONArray.length(); indexVar++) {
            inputMap.clear();
            customersegmentName = customersegmentJSONArray.get(indexVar).toString();
            inputMap.put("Location_id", locationId);
            inputMap.put("segment_id", customersegmentMap.get(customersegmentName));

            operationResponse = Executor.invokeService(ServiceURLEnum.LOCATIONCUSTOMERSEGMENT_CREATE, inputMap, null,
                    requestInstance);
            operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);

            if (operationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.LOCATIONS, EventEnum.CREATE,
                        ActivityStatusEnum.FAILED, "Failed to map Customersegment and Location. LocationID: "
                                + locationId + " segment_id: " + customersegmentMap.get(customersegmentName));
                throw new ApplicationException(ErrorCodeEnum.ERR_20347);
            } else {
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.LOCATIONS, EventEnum.CREATE,
                        ActivityStatusEnum.SUCCESSFUL, "Successfully mapped Customersegment and Location. LocationID: "
                                + locationId + " segment_id: " + customersegmentMap.get(customersegmentName));
            }
        }

        return operationRecord;
    }

    /**
     * Method to set the Location Currencies
     * 
     * @param currencyJSONArray
     * @param locationID
     * @param userID
     * @param requestInstance
     * @return
     * @throws ApplicationException
     */
    private Record setLocationCurrencies(JSONArray currencyJSONArray, String locationID, String userID,
            DataControllerRequest requestInstance) throws ApplicationException {

        Record operationRecord = new Record();
        operationRecord.setId("setLocationCurrency");
        operationRecord.addParam(new Param("Status", "Success", FabricConstants.STRING));

        // fetching all currencies
        String operationResponse = Executor.invokeService(ServiceURLEnum.CURRENCY_READ, new HashMap<String, String>(),
                null, requestInstance);
        JSONObject operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);

        Map<String, String> inputMap = new HashMap<>();
        if (operationResponseJSON != null && operationResponseJSON.has(FabricConstants.OPSTATUS)
                && operationResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
                && operationResponseJSON.has("currency")) {
            diagnostic.prepareDebug("Successful CRUD Operation").log();
            JSONArray readResponseJSONArray = operationResponseJSON.getJSONArray("currency");
            for (int i = 0; i < readResponseJSONArray.length(); ++i) {
                inputMap.put(readResponseJSONArray.getJSONObject(i).optString("code"),
                        readResponseJSONArray.getJSONObject(i).optString("code"));
            }
        } else {
            alert.prepareError("Failed CRUD Operation").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20348);
        }

        // fetching previous location currencies
        Map<String, String> postParametersMap = new HashMap<String, String>();
        postParametersMap.put(ODataQueryConstants.FILTER, "Location_id eq '" + locationID + "'");
        operationResponse = Executor.invokeService(ServiceURLEnum.LOCATIONCURRENCY_READ, postParametersMap, null,
                requestInstance);
        operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);
        JSONArray operationResponseJSONArray = operationResponseJSON.getJSONArray("locationcurrency");
        
        // deleting previous location currencies
        postParametersMap = new HashMap<String, String>();
        postParametersMap.put("Location_id", locationID);
        for (int i = 0; i < operationResponseJSONArray.length(); i++) {
        	postParametersMap.put("currency_code", operationResponseJSONArray.getJSONObject(i).optString("currency_code"));
	        operationResponse = Executor.invokeService(ServiceURLEnum.LOCATIONCURRENCY_DELETE, postParametersMap, null,
	                requestInstance);
	        operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);
	        if (operationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0)
	            throw new ApplicationException(ErrorCodeEnum.ERR_20336); 
        }
        
        // creating new location currencies
        postParametersMap = new HashMap<String, String>();
        postParametersMap.put("Location_id", locationID);
        String currencyCode = StringUtils.EMPTY;
        for (int indexVar = 0; indexVar < currencyJSONArray.length(); indexVar++) {
            currencyCode = currencyJSONArray.get(indexVar).toString();

            postParametersMap.put("currency_code", inputMap.get(currencyCode));

            operationResponse = Executor.invokeService(ServiceURLEnum.LOCATIONCURRENCY_CREATE, postParametersMap, null,
                    requestInstance);
            operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);

            if (operationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.LOCATIONS, EventEnum.CREATE,
                        ActivityStatusEnum.FAILED, "Failed to map Currency and Location. LocationID: " + locationID
                                + " currency_code: " + inputMap.get(currencyCode));
                throw new ApplicationException(ErrorCodeEnum.ERR_20346);
            } else {
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.LOCATIONS, EventEnum.CREATE,
                        ActivityStatusEnum.SUCCESSFUL, "Successfully mapped Currency and Location. LocationID: "
                                + locationID + " currency_code: " + inputMap.get(currencyCode));
            }

        }

        return operationRecord;
    }

    private boolean isWeekDay(String day) {
        if (StringUtils.isBlank(day))
            return false;
        //if (day.equalsIgnoreCase("SATURDAY") || day.equalsIgnoreCase("SUNDAY"))
        if (day.equalsIgnoreCase("SATURDAY"))
            return false;
        return true;
    }
    
    

    /**
     * Method to toggle the Location status
     * 
     * @param locationId
     * @param locationStatus
     * @param loggedInUserId
     * @param requestInstance
     * @return operation Record
     * @throws ApplicationException
     */
    private Record toggleLocationStatus(String locationId, String locationStatus, String loggedInUserId,
            DataControllerRequest requestInstance) throws ApplicationException {

        if (StringUtils.isBlank(locationId) || StringUtils.isBlank(locationStatus)) {
            alert.prepareError("Location Id and Location Status are mandatory inputs to toggle Location Status").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20350);
        }

        Record operationRecord = new Record();
        operationRecord.setId("setLocationStatus");

        Param statusParam = new Param("status", "Success", FabricConstants.STRING);
        operationRecord.addParam(statusParam);

        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("id", locationId);
        if (StringUtils.equalsIgnoreCase(locationStatus, "Deactivate")) {
            inputMap.put("softdeleteflag", "1");
        } else {
            inputMap.put("softdeleteflag", "0");
        }
        inputMap.put("modifiedby", loggedInUserId);
        inputMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());

        String operationResponse = Executor.invokeService(ServiceURLEnum.LOCATION_UPDATE, inputMap, null,
                requestInstance);

        JSONObject operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);
        if (operationResponseJSON == null || !operationResponseJSON.has(FabricConstants.OPSTATUS)
                || operationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
            alert.prepareError("Failed CRUD Operation").log();
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.LOCATIONS, EventEnum.UPDATE,
                    ActivityStatusEnum.FAILED, "Location update failed. LocationID: " + locationId);
            throw new ApplicationException(ErrorCodeEnum.ERR_20350);
        } else {
            diagnostic.prepareDebug("Successful CRUD Operation").log();
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.LOCATIONS, EventEnum.UPDATE,
                    ActivityStatusEnum.SUCCESSFUL, "Location updated successful. LocationID:" + locationId);
        }

        return operationRecord;
    }
    public static boolean isValidParam(String s) {
    	
    	Pattern scriptPattern = Pattern.compile("\\<.*?\\>");
        
    	Matcher scriptMatcher = scriptPattern.matcher(s);
    	if(scriptMatcher.find())
    	{
    		return false;
    	}
 
    	return true;
    }
	public static boolean isValidPhoneNumber(String s) {
	    	
    	Pattern scriptPattern = Pattern.compile("^[\\+]\\d{1,3}[-][\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$");

    	Matcher scriptMatcher = scriptPattern.matcher(s);
    	if(scriptMatcher.find())
    	{
    		return true;
    	}
 
    	return false;
    }
	
	public static boolean isValidEmailAddress(String s) {
    	
    	Pattern scriptPattern = Pattern.compile("^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$");
        
    	Matcher scriptMatcher = scriptPattern.matcher(s);
    	if(scriptMatcher.find())
    	{
    		return true;
    	}
 
    	return false;
    }
}

