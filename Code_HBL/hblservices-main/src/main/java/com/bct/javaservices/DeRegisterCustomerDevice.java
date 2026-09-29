package com.bct.javaservices;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bct.utilities.HBLCommonUtility;
import com.infinity.dbx.temenos.accounts.AccountsConstants;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.MWConstants;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

public class DeRegisterCustomerDevice implements JavaService2 {
	private static final Logger LOG = LogManager.getLogger(DeRegisterCustomerDevice.class);
	private static final String CHANNEL_MOBILE = "CH_ID_MOB";
	private String playstoreLink = "";
	private String appstoreLink = "";

	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse responseInstance) throws Exception {

		Result processedResult = new Result();
		try {
			playstoreLink = EnvironmentConfigurationsHandler.getValue("PLAYSTORELINK");
			appstoreLink = EnvironmentConfigurationsHandler.getValue("APPSTORELINK");
			HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
			LOG.debug("HBL:DeRegisterCustomerDevice :");
			String deviceID = request.getParameter("Device_id");
			String customerID = request.getParameter("Customer_id");
			// String statusID = request.getParameter("Status_id");
			if (StringUtils.isBlank(customerID)) {
				ErrorCodeEnum.ERR_20613.setErrorCode(processedResult);
				Param statusParam = new Param("Status", "Update failed", MWConstants.STRING);
				processedResult.addParam(statusParam);
				return processedResult;

			}
			if (StringUtils.isBlank(deviceID)) {
				HelperMethods.setValidationMsgwithCode("Invalid Device Id", ErrorCodeEnum.ERR_10219.getErrorCodeAsString(), processedResult);
				return processedResult;
			}

			JSONArray customerDeviceInfo = getCustomerDeviceInfo(request, customerID, deviceID);
			LOG.debug("HBL:DeRegisterCustomerDevice customerDeviceInfo:" + customerDeviceInfo);
			if (customerDeviceInfo.length() > 0) {
				try {
					Result insertRes = createDeviceInfo(request, deviceID, customerDeviceInfo);					
					String insertStatus = insertRes.getParamValueByName("OperationCode");
					if (insertStatus == "0") {
						//processedResult.addParam(new Param("status", "success", MWConstants.STRING));
						Result delResult = deleteDeviceInfo(request, deviceID, customerID);
						LOG.debug("delResult : "+ delResult);
						String deleteStatus = delResult.getParamValueByName("OperationCode");
						if (deleteStatus == "0") {
							LOG.debug("updated customerdevice successfully");
							JSONObject customerInfo = getCustomerInfoFromUsername(request, null, customerID);
							customerInfo.put("emailTemplate", "HBL_DEVICE_DEREGISTERATION");
							customerInfo.put("deviceId", deviceID);
							customerInfo.put("playstoreLink", playstoreLink);
							customerInfo.put("appstoreLink", appstoreLink);
							boolean isMailSent = HBLCommonUtility.triggerEmail(request, customerInfo);
							if (isMailSent) {
								new Param("UpdateResponse", String.valueOf(delResult), MWConstants.STRING);
								Param statusParam = new Param("Status", "Update successful", MWConstants.STRING);
								processedResult.addParam(statusParam);
							} else {
								LOG.debug("HBL:DeRegisterCustomerDevice:Failed to send an email");
								new Param("UpdateResponse", String.valueOf(delResult), MWConstants.STRING);
								Param statusParam = new Param("Status", "Failed to send an email", MWConstants.STRING);
								processedResult.addParam(statusParam);
							}
						} else {
							LOG.debug("HBL:DeRegisterCustomerDevice:Failed to delete record2");
							new Param("UpdateResponse", String.valueOf(delResult), MWConstants.STRING);
							Param statusParam = new Param("Status", "Failed to deregister device", MWConstants.STRING);
							processedResult.addParam(statusParam);
						}
					} else {
						LOG.debug("HBL:DeRegisterCustomerDevice:Failed to insert record");
						processedResult.addParam(new Param("status", "failed", MWConstants.STRING));
						HelperMethods.setValidationMsgwithCode("Failed to DeRegistered device.",
								ErrorCodeEnum.ERR_10220.getErrorCodeAsString(), processedResult);
					}
				} catch (Exception e) {
					processedResult.addParam(new Param("status", "failed", MWConstants.STRING));
					processedResult.addParam(new Param("errorMessage", e.getLocalizedMessage(), MWConstants.STRING));
				}
			} else {
				HelperMethods.setValidationMsgwithCode("No Registered/Active Devices found", ErrorCodeEnum.ERR_10220.getErrorCodeAsString(), processedResult);
			}
			
		} catch (Exception e) {
			LOG.error("HBL: DeRegisterCustomerDevice:Runtime Exception.Exception Trace:", e.getMessage());
			ErrorCodeEnum.ERR_20001.setErrorCode(processedResult);
		}
		processedResult.setParam(new Param("opstatus", "0"));
		processedResult.setParam(new Param("httpStatusCode", "200"));
		return processedResult;
	}

	public Object invoke2(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse responseInstance) throws Exception {

		Result processedResult = new Result();
		try {
			playstoreLink = EnvironmentConfigurationsHandler.getValue("PLAYSTORELINK");
			appstoreLink = EnvironmentConfigurationsHandler.getValue("APPSTORELINK");
			HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
			LOG.debug("HBL:DeRegisterCustomerDevice :");
			String deviceID = request.getParameter("Device_id");
			String customerID = request.getParameter("Customer_id");
			String statusID = request.getParameter("Status_id");
			if (StringUtils.isBlank(customerID)) {
				ErrorCodeEnum.ERR_20613.setErrorCode(processedResult);
				Param statusParam = new Param("Status", "Update failed", MWConstants.STRING);
				processedResult.addParam(statusParam);
				return processedResult;

			}
			if (StringUtils.isBlank(deviceID)) {
				HelperMethods.setValidationMsgwithCode("Invalid Device Id",
						ErrorCodeEnum.ERR_10219.getErrorCodeAsString(), processedResult);
				return processedResult;
			}

			Map<String, Object> postParametersMap = new HashMap<String, Object>();
			postParametersMap.put("id", deviceID);
			postParametersMap.put("Customer_id", customerID);

			if (!StringUtils.isEmpty(statusID)) {
				postParametersMap.put("Status_id", statusID);
			}
			postParametersMap.put("lastmodifiedts", getISOFormattedLocalTimestamp());

			String serviceName = TemenosConstants.SERVICE_BACKEND_CERTIFICATE;
			String operationName = "dbxdb_customerdevice_update";
			Result updateEndpointResponse = CommonUtils.callIntegrationService(request, postParametersMap,
					serviceHeaders, serviceName, operationName, false);
			String errMessage = updateEndpointResponse.getParamValueByName(AccountsConstants.PARAM_ERROR_MESSAGE);
			LOG.debug("HBL:DeRegisterCustomerDevice updateEndpointResponse :" +updateEndpointResponse);
			if (StringUtils.isNotBlank(errMessage)) {
				LOG.error("Failed to update customer device information:" + errMessage);
				processedResult.addParam(
						new Param("UpdateResponse", String.valueOf(updateEndpointResponse), MWConstants.STRING));
				processedResult.addParam(new Param("Status", "Failed", MWConstants.STRING));
				processedResult.addParam(
						new Param("errorMessage", ErrorCodeEnum.ERR_20691.getErrorCodeAsString(), MWConstants.INT));

			} else if (StringUtils.isNotEmpty(updateEndpointResponse.getParamValueByName("updatedRecords"))) {
				if (Integer.parseInt(updateEndpointResponse.getParamValueByName("updatedRecords")) > 0) {
					LOG.debug("updated customerdevice successfully");
					JSONObject customerInfo = getCustomerInfoFromUsername(request, null, customerID);
					customerInfo.put("emailTemplate", "HBL_DEVICE_DEREGISTERATION");
					customerInfo.put("deviceId", deviceID);
					customerInfo.put("playstoreLink", playstoreLink);
					customerInfo.put("appstoreLink", appstoreLink);
					boolean isMailSent = HBLCommonUtility.triggerEmail(request, customerInfo);
					if (isMailSent) {
						new Param("UpdateResponse", String.valueOf(updateEndpointResponse), MWConstants.STRING);
						Param statusParam = new Param("Status", "Update successful", MWConstants.STRING);
						processedResult.addParam(statusParam);
					} else {
						LOG.debug("HBL:DeRegisterCustomerDevice:Failed to send an email");
						new Param("UpdateResponse", String.valueOf(updateEndpointResponse), MWConstants.STRING);
						Param statusParam = new Param("Status", "Failed to send an email", MWConstants.STRING);
						processedResult.addParam(statusParam);
					}
				} else {
					LOG.debug("HBL:DeRegisterCustomerDevice:Failed to send an email");
					new Param("UpdateResponse", String.valueOf(updateEndpointResponse), MWConstants.STRING);
					Param statusParam = new Param("Status", "Failed to deregister device", MWConstants.STRING);
					processedResult.addParam(statusParam);
				}
			}
		} catch (Exception e) {
			LOG.error("HBL: DeRegisterCustomerDevice:Runtime Exception.Exception Trace:", e.getMessage());
			ErrorCodeEnum.ERR_20001.setErrorCode(processedResult);
		}
		processedResult.setParam(new Param("opstatus", "0"));
		processedResult.setParam(new Param("httpStatusCode", "200"));
		return processedResult;
	}

	public static String getISOFormattedLocalTimestamp() {
		String localDateTime;
		if (LocalDateTime.now().getSecond() == 0) {
			localDateTime = LocalDateTime.now().plusSeconds(1).withNano(0).toString();
		} else {
			localDateTime = LocalDateTime.now().withNano(0).toString();
		}
		return localDateTime;
	}

	// @Override
	public Object invoke1(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		LOG.debug("HBL:DeRegisterCustomerDevice :");
		// String deviceId = request.getParameter("deviceId");
		String UserName = request.getParameter("customerUsername");
		String deviceId = request.getParameter("deviceId");
		Result result = new Result();
		if (deviceId != null) {
			JSONObject customerInfo = getCustomerInfoFromUsername(request, UserName, null);
			String customerId = customerInfo.getString("id");
			JSONArray customerDeviceInfo = getCustomerDeviceInfo(request, customerId, deviceId);
			LOG.debug("HBL:DeRegisterCustomerDevice customerDeviceInfo:" + customerDeviceInfo);
			if (customerDeviceInfo.length() > 0) {
				try {
					Integer flagUpdate = deRegisterDevice(request, customerId, customerDeviceInfo);
					if (flagUpdate == customerDeviceInfo.length()) {
						// result.setParam(new Param("status", "success"));
						result.addParam(new Param("status", "success", MWConstants.STRING));
					} else {
						result.addParam(new Param("status", "failed", MWConstants.STRING));
						HelperMethods.setValidationMsgwithCode("Failed to DeRegistered device.",
								ErrorCodeEnum.ERR_10220.getErrorCodeAsString(), result);
					}
				} catch (Exception e) {
					result.addParam(new Param("status", "failed", MWConstants.STRING));
					result.addParam(new Param("errorMessage", e.getLocalizedMessage(), MWConstants.STRING));
				}
			} else {
				HelperMethods.setValidationMsgwithCode("No Registered/Active Devices found",
						ErrorCodeEnum.ERR_10220.getErrorCodeAsString(), result);
			}

		} else {
			HelperMethods.setValidationMsgwithCode("Invalid Device Id", ErrorCodeEnum.ERR_10219.getErrorCodeAsString(),
					result);
		}

		result.setParam(new Param("opstatus", "0"));
		result.setParam(new Param("httpStatusCode", "200"));

		return result;
	}

	public JSONArray getCustomerDeviceInfo(DataControllerRequest request, String customerId, String deviceId)
			throws Exception {
		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		JSONArray array = new JSONArray();
		HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
		String serviceName = TemenosConstants.SERVICE_BACKEND_CERTIFICATE;
		String operationName = "dbxdb_customerdevice_get";
		String filter = "id" + DBPUtilitiesConstants.EQUAL + deviceId + DBPUtilitiesConstants.AND + "Channel_id"
				+ DBPUtilitiesConstants.EQUAL + CHANNEL_MOBILE + DBPUtilitiesConstants.AND + "Customer_id"
				+ DBPUtilitiesConstants.EQUAL + customerId;
		inputParams.put("$filter", filter);
		Result result = CommonUtils.callIntegrationService(request, inputParams, serviceHeaders, serviceName,
				operationName, false);

		String errMessage = result.getParamValueByName(AccountsConstants.PARAM_ERROR_MESSAGE);
		if (StringUtils.isNotBlank(errMessage)) {
			LOG.error("Couldn't get customer device info due to : " + errMessage);
		} else {
			LOG.debug("getCustomerDeviceInfo result : " + result.getDatasetById("customerdevice").toString());
			Dataset customerDeviceDataset = result.getDatasetById("customerdevice");
			// Converting dataset to json array
			array = ResultToJSON.convertDataset(customerDeviceDataset);
		}
		return array;

	}

	public Integer deRegisterDevice(DataControllerRequest request, String customerId, JSONArray customerDeviceInfo)
			throws Exception {
		int count = 0;
		int recordsCount = customerDeviceInfo.length();
		for (int i = 0; i < recordsCount; i++) {
			HashMap<String, Object> inputParams = new HashMap<String, Object>();
			HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
			inputParams.put("Customer_id", customerId);
			inputParams.put("Status_id", "SID_DEVICE_DE-REGISTERED");
			inputParams.put("id", customerDeviceInfo.getJSONObject(i).getString("id"));
			LOG.debug("update customerdevice inputParams:" + inputParams);
			String serviceName = TemenosConstants.SERVICE_BACKEND_CERTIFICATE;
			String operationName = "dbxdb_customerdevice_update";
			Result result = CommonUtils.callIntegrationService(request, inputParams, serviceHeaders, serviceName,
					operationName, false);
			String errMessage = result.getParamValueByName(AccountsConstants.PARAM_ERROR_MESSAGE);
			if (StringUtils.isNotBlank(errMessage)) {
				LOG.error("Couldn't update customerdevice table due to : " + errMessage);
			} else if (StringUtils.isNotEmpty(result.getParamValueByName("updatedRecords"))) {
				if (Integer.parseInt(result.getParamValueByName("updatedRecords")) > 0) {
					LOG.debug("updated customerdevice successfully");
					count++;
				}
			}
		}
		return count;
	}

	public JSONObject getCustomerInfoFromUsername(DataControllerRequest request, String UserName, String customerId) {

		JSONObject customerInfo = new JSONObject();
		String filter = "";
		try {
			if (StringUtils.isNotBlank(UserName)) {
				filter = CommonUtils.buildOdataCondition(TemenosConstants.PARAM_USERNAME, Constants.EQUAL, UserName);
			} else if (StringUtils.isNotBlank(customerId)) {
				filter = CommonUtils.buildOdataCondition("id", Constants.EQUAL, customerId);
			}
			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
			HashMap<String, Object> svcParams = new HashMap<String, Object>();

			svcParams.put(Constants.PARAM_DOLLAR_FILTER, filter);
			Result result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders,
					Constants.DBX_DB_SERVICE_NAME, TemenosConstants.OP_CUSTOMER_GET, false);
			Dataset customerDataset = result.getDatasetById(TemenosConstants.DS_CUSTOMER);

			if (null != customerDataset) {
				String customerid = customerDataset.getRecord(0).getParamValueByName("id");
				// Converting dataset to json array
				JSONArray array = ResultToJSON.convertDataset(customerDataset);
				customerInfo = array.getJSONObject(0);
			}
			LOG.debug("getCustomerIDFromUsername customerInfo:" + customerInfo);
		} catch (Exception e) {
			LOG.error("Error while retrieving CustomerIDFromUsername for Customer " + UserName);
		}
		return customerInfo;
	}

	public static Result createDeviceInfo(DataControllerRequest dcRequest, String deviceId, JSONArray customerDeviceInfo) {
		Result result = new Result();
		JSONObject custDevRec = new JSONObject();
		LOG.debug("createDeviceInfo customerDeviceInfo:" + customerDeviceInfo);
		
		if (customerDeviceInfo != null) {
			custDevRec = customerDeviceInfo.getJSONObject(0);
			LOG.debug("createDeviceInfo custDevRec :" + custDevRec);
		} 

		Map<String, Object> postParametersMap = new HashMap<>();
		String currTS = getISOFormattedLocalTimestamp();
		String plainTS = currTS.replaceAll(":", "").replaceAll("-", "").replaceAll(" ", "");

		postParametersMap.put("id", deviceId + "_DR_" + plainTS);
		postParametersMap.put("Customer_id", custDevRec.getString("Customer_id"));
		postParametersMap.put("Status_id", "SID_DEVICE_DE-REGISTERED");
		
		String lastUsedIP = "";
		try {
			lastUsedIP = custDevRec.getString("LastUsedIp");
		} catch (Exception e) {
			
		}

		//String createdBy = custDevRec.getString("createdby");//?
		//String modifiedBy = custDevRec.getString("modifiedby");//?
		String createdTs = custDevRec.getString("createdts");
		String syncTimestamp = custDevRec.getString("synctimestamp");
		String lastLoginTime = custDevRec.getString("LastLoginTime");
		String operatingSystem = custDevRec.getString("OperatingSystem");
		String channelId = custDevRec.getString("Channel_id");
		String appId = custDevRec.getString("appid");
		String deviceName = custDevRec.getString("DeviceName");
		
		postParametersMap.put("DeviceName", StringUtils.defaultString(deviceName, ""));
		postParametersMap.put("LastLoginTime", Timestamp.valueOf(lastLoginTime));
		postParametersMap.put("OperatingSystem", StringUtils.defaultString(operatingSystem, ""));
		postParametersMap.put("Channel_id", StringUtils.defaultString(channelId, ""));
		postParametersMap.put("appid", StringUtils.defaultString(appId, ""));
		postParametersMap.put("LastUsedIp", StringUtils.defaultString(lastUsedIP, ""));
		//postParametersMap.put("createdby", StringUtils.defaultString(createdBy, ""));
		//postParametersMap.put("modifiedby", StringUtils.defaultString(modifiedBy, ""));
		postParametersMap.put("createdts", Timestamp.valueOf(createdTs));
		postParametersMap.put("synctimestamp", Timestamp.valueOf(syncTimestamp));
		postParametersMap.put("lastmodifiedts", currTS);

		
		postParametersMap.forEach((key, value) -> LOG.debug("createDeviceInfo : " + key + "=" + value));

		
		String serviceName = TemenosConstants.SERVICE_BACKEND_CERTIFICATE;
		String operationName = "dbxdb_customerdevice_create";
		HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
		Result deviceinfo;
		try {
			deviceinfo = CommonUtils.callIntegrationService(dcRequest, postParametersMap, serviceHeaders, serviceName,
					operationName, false);
			if (deviceinfo == null || HelperMethods.hasError(deviceinfo) || (HelperMethods.hasRecords(deviceinfo)
					&& deviceinfo.getParamValueByName(MWConstants.OPSTATUS) != "0")) {
				LOG.error("Failed to register a device!");
				result.addParam(new Param("RegisterResponse", (deviceinfo != null) ? String.valueOf(deviceinfo.getAllRecords())
								: "Failed to register a device!", MWConstants.STRING));
				result.addParam(new Param("Status", "Failed", MWConstants.STRING));
				result.addParam(new Param("OperationCode", ErrorCodeEnum.ERR_20691.getErrorCodeAsString(), MWConstants.INT));
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Param statusParam = new Param("Status", "Successful", MWConstants.STRING);
		result.addParam(new Param("OperationCode", "0", MWConstants.INT));
		result.addParam(statusParam);
		return result;
	}

	public Result deleteDeviceInfo(DataControllerRequest dcRequest, String deviceId, String customerId) {
		Result result = new Result();
		String serviceName = TemenosConstants.SERVICE_BACKEND_CERTIFICATE;
		String url = "dbxdb_customerdevice_delete";
		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
		inputParams.put("id", deviceId);
		inputParams.put("Customer_id", customerId);
		try {
			Result deviceInfo = CommonUtils.callIntegrationService(dcRequest, inputParams, serviceHeaders, serviceName,
					url, false);
			if (deviceInfo == null || HelperMethods.hasError(deviceInfo) || deviceInfo.getParamValueByName(MWConstants.OPSTATUS) != "0") {
				LOG.error("Failed to delete a device!");
				result.addParam(new Param("Status", "Failed", MWConstants.STRING));
				result.addParam(new Param("OperationCode", ErrorCodeEnum.ERR_20691.getErrorCodeAsString(), MWConstants.INT));
				return result;
			}

			Param statusParam = new Param("Status", "Successful", MWConstants.STRING);
			result.addParam(new Param("OperationCode", "0", MWConstants.INT));
			result.addParam(statusParam);
		} catch (Exception e) {
			LOG.error("deleteDeviceInfo :exception: " + e.getLocalizedMessage());
		}
		return result;
	}
}
