package com.hbl.javaservices;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetAuthorizedUsernameByDeviceID implements JavaService2 {
	private static LoggerUtil logger = new LoggerUtil(GetAuthorizedUsernameByDeviceID.class);
	public static String channelID = "CH_ID_MOB";

	@SuppressWarnings("deprecation")
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse reponse) throws Exception {

		String deviceBindProp = "";
		deviceBindProp = EnvironmentConfigurationsHandler.getServerProperty("MOCK_DEVICE_BINDING");
		logger.debug("deviceBindProp :" + deviceBindProp);
		
		Result result = new Result();
		Record userRecord = null;
		String deviceId = request.getParameter("deviceId");
		String customerId = "";
		String username = "";
		String deviceStatus = "";
		logger.debug("deviceId :" + deviceId);

		if (StringUtils.isNotBlank(deviceBindProp) && deviceBindProp.equalsIgnoreCase("1")) {
			// Mock response
			deviceStatus = "0";
		} else {
			userRecord = getUserRecordByDeviceID(request, deviceId);
			if (userRecord == null) {
				// userRecord is empty, so it is allowed - Fresh login in this device
				deviceStatus = "0";
			} else {
				customerId = userRecord.getParamValueByName("Customer_id");
				username = userRecord.getParamValueByName("createdby");
				deviceStatus = String.valueOf(getDeviceDetailsForUser(request, customerId, deviceId));
			}
		}

		result.setParam(new Param("username", username));
		result.setParam(new Param("deviceStatus", deviceStatus));
		result.setParam(new Param("opstatus", "0"));
		result.setParam(new Param("httpStatusCode", "200"));
		return result;
	}

	private int getDeviceDetailsForUser(DataControllerRequest request, String customerId, String deviceId) {
		int status = 1;
		try {
			if (customerId == "") {
				// customerId is empty, so it is allowed - Fresh login in this device
				status = 0;
				return status;
			} else {
				// customerId is not empty
				HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
				HashMap<String, Object> svcParams = new HashMap<String, Object>();
				svcParams.put(Constants.PARAM_DOLLAR_FILTER,
						"Customer_id eq '" + customerId + "' and Channel_id eq '" + channelID + "'");

				Result deviceinfo = CommonUtils.callIntegrationService(request, svcParams, svcHeaders, "HBLCustomCRUD",
						"dbxdb_customerdevice_get", false);

				Dataset deviceInfoDs = deviceinfo.getDatasetById("customerdevice");
				if (deviceInfoDs != null) {
					int recordsSize = deviceInfoDs.getAllRecords().size();
					logger.debug("getDeviceDetailsForUser recordsSize :" + recordsSize);
					if (recordsSize > 0) {
						for (int i = 0; i < recordsSize; i++) {
							Record deviceInfo = deviceInfoDs.getRecord(i);
							String devID = deviceInfo.getParamValueByName("id");
							String devStatus = deviceInfo.getParamValueByName("Status_id");
							if (devID.equalsIgnoreCase(deviceId)
									&& devStatus.equalsIgnoreCase("SID_DEVICE_REGISTERED")) {
								// Device id has a valid registration associated with customerId
								status = 0;
								break;
							}
						}
					}
				}
			}
		} catch (Exception e) {

		}
		return status;

	}

	private Record getUserRecordByDeviceID(DataControllerRequest request, String deviceID) {

		Record userRecord = null;
		try {

			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
			HashMap<String, Object> svcParams = new HashMap<String, Object>();
			svcParams.put(Constants.PARAM_DOLLAR_FILTER,
					"id eq '" + deviceID + "' and Channel_id eq '" + channelID + "'");

			Result deviceinfo = CommonUtils.callIntegrationService(request, svcParams, svcHeaders, "HBLCustomCRUD",
					"dbxdb_customerdevice_get", false);

			Dataset deviceInfoDs = deviceinfo.getDatasetById("customerdevice");

			if (deviceInfoDs != null && deviceInfoDs.getAllRecords().size() > 0) {
				userRecord = deviceInfoDs.getRecord(0);
			}

			logger.debug("getUsernameByDeviceID username :" + userRecord);
		} catch (Exception e) {
			logger.error("Exception getUsernameByDeviceID ", e);
		}
		return userRecord;

	}

}
