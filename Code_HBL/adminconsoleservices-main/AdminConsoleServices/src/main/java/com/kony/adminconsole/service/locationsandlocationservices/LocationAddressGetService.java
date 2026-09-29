package com.kony.adminconsole.service.locationsandlocationservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class LocationAddressGetService implements JavaService2 {

	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {

		String latitude = "";
		String longitude = "";
		Result result = new Result();
		JSONObject locationJSON = null;

		try {
			if (StringUtils.isEmpty(requestInstance.getParameter("latitude"))) {
				return ErrorCodeEnum.ERR_20645.setErrorCode(result);
			}
			latitude = requestInstance.getParameter("latitude");
			if (StringUtils.isEmpty(requestInstance.getParameter("longitude"))) {
				return ErrorCodeEnum.ERR_20644.setErrorCode(result);
			}
			longitude = requestInstance.getParameter("longitude");

			Map<String, String> postParametersMap = new HashMap<String, String>();
			postParametersMap.put("_latitude", latitude);
			postParametersMap.put("_longitude", longitude);
			String readResponse = Executor.invokeService(ServiceURLEnum.LOCATIONDETAILS_VIEW_READ, postParametersMap,
					null, requestInstance);
			JSONObject readEndpointResponse = CommonUtilities.getStringAsJSONObject(readResponse);
			if (readEndpointResponse == null || !readEndpointResponse.has(FabricConstants.OPSTATUS)
					|| readEndpointResponse.getInt(FabricConstants.OPSTATUS) != 0) {
				return ErrorCodeEnum.ERR_20372.setErrorCode(result);
			}
			JSONArray locationRangeDetailJSONArray = (JSONArray) readEndpointResponse.get("locationdetails_view");
			for (int i = 0; i < locationRangeDetailJSONArray.length(); i++) {
				locationJSON = locationRangeDetailJSONArray.getJSONObject(i);
				if (latitude.equalsIgnoreCase(locationJSON.getString("latitude"))
						&& longitude.equalsIgnoreCase(locationJSON.getString("longitude"))) {

					String addrLine1 = locationJSON.getString("addressLine1");
					String formattedAddress = addrLine1;
					if (locationJSON.has("addressLine2")) {

						String addrLine2 = locationJSON.getString("addressLine2");
						if (addrLine2 != null && StringUtils.isNotBlank(addrLine2)) {
							formattedAddress = formattedAddress + ", " + addrLine2;
						}
					}
					if (locationJSON.has("addressLine3")) {

						String addrLine3 = locationJSON.getString("addressLine3");
						if (addrLine3 != null && StringUtils.isNotBlank(addrLine3)) {
							formattedAddress = formattedAddress + ", " + addrLine3;
						}
					}
					if (locationJSON.has("city")) {
						String city = locationJSON.getString("city");
						if (city != null && StringUtils.isNotBlank(city)) {
							formattedAddress = formattedAddress + ", " + city;
						}
					}
					String region = locationJSON.getString("region");
					if (region != null && StringUtils.isNotBlank(region)) {
						formattedAddress = formattedAddress + ", " + region;
					}
					String country = locationJSON.getString("country");
					if (country != null && StringUtils.isNotBlank(country)) {
						formattedAddress = formattedAddress + ", " + country;
					}

					String locationId = locationJSON.getString("status");

					result.addParam(new Param("formattedAddress", formattedAddress, FabricConstants.STRING));
					result.addParam(new Param("status", locationId, FabricConstants.STRING));

					return result;
				}

			}
		} catch (Exception e) {
			result.addParam("message", e.getMessage());
			return ErrorCodeEnum.ERR_20001.setErrorCode(result);

		}
		result.addParam("message", "No address found for the specified location");
		return result;

	}

}
