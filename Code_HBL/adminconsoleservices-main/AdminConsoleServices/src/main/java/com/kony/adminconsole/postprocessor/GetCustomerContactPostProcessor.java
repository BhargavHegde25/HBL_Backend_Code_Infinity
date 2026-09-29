package com.kony.adminconsole.postprocessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetCustomerContactPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Log4j2Configurator.getInstance();

		try {

			// Address
			String residenceType = null;
			String countryOfResidence = null;
			String CountryName = null;
			String partyId = StringUtils.EMPTY;
			if(result.getParamByName("errmsg")!=null)
				return null;
			result.addParam("Status", "Successful");
			Record AddressRecord = result.getRecordById("Addresses");
			if(AddressRecord==null)
				return null;
			Dataset AddressDs = new Dataset("Addresses");
			AddressDs.setId("Addresses");
			AddressDs.addRecord(AddressRecord);
			result.addDataset(AddressDs);
			Dataset ResidenceDs = result.getDatasetById("residences");
			if (ResidenceDs != null) {
				List<Record> residenceList = ResidenceDs.getAllRecords();
				if (residenceList.size() != 0) {
					for (Record record : residenceList) {
						residenceType = record.getParamValueByName("residenceType");
						partyId = record.getParamValueByName("partyId");
						if (residenceType.equalsIgnoreCase("1")) {
							residenceType = "ADR_TYPE_HOME";
						} else {
							residenceType = "ADR_TYPE_WORK";
						}

						countryOfResidence = record.getParamValueByName("countryOfResidence");

						if (countryOfResidence.equalsIgnoreCase("1")) {
							countryOfResidence = "IND";
							CountryName = "India";
						} else if (countryOfResidence.equalsIgnoreCase("2")) {
							countryOfResidence = "US";
							CountryName = "United States";

						}
					}
				}

			}
			if (AddressDs != null) {
				List<Record> recordsList = AddressDs.getAllRecords();
				if (recordsList.size() != 0) {
					for (Record record : recordsList) {
						if (record.getAllParams().size() != 0) {
							record.addParam("AddressType", residenceType);
							record.addParam("CountryCode", countryOfResidence);
							record.addParam("CountryName", CountryName);
							record.addParam("CityName", record.getParamValueByName("town"));
							record.addParam("ZipCode", record.getParamValueByName("postalOrZipCode"));
							String floor = record.getParamValueByName("floor");
							String buildingNumber = record.getParamValueByName("buildingNumber");
							String buildingName = record.getParamValueByName("buildingName");
							String AddressLine1 = floor + " floor" + " " + buildingNumber + " " + buildingName;
							String AddressLine2 = record.getParamValueByName("streetName");
							record.addParam("RegionName", record.getParamValueByName("town"));
							record.addParam("AddressLine1", AddressLine1);
							record.addParam("AddressLine2", AddressLine2);
							record.addParam("isPrimary", "true");
							String customerId = getCustomerId(partyId, request);

							record.addParam("Customer_id", customerId);
						}
					}
				}
			}

			// Email Ids
			Record EmailIdsRecord = result.getRecordById("EmailIds");
			Dataset emailDs = new Dataset("EmailIds");
			emailDs.setId("EmailIds");
			emailDs.addRecord(EmailIdsRecord);
			result.addDataset(emailDs);
			List<Record> recordEmail = emailDs.getAllRecords();
			if (recordEmail.size() != 0) {
				for (Record record : recordEmail) {
					if (record.getAllParams().size() != 0) {
						if (record.getParamValueByName("electronicAddressType").equalsIgnoreCase("1")) {
							record.addParam("Extension", "Personal");
							record.addParam("isPrimary", "true");
							record.addParam("Type_id", "COMM_TYPE_EMAIL");
							record.addParam("Value", record.getParamValueByName("electronicAddress"));
							String customerId = getCustomerId(partyId, request);
							record.addParam("Customer_id", customerId);
						}
					}

				}
			}

			// ContactNumbers
			Record contactRecord = result.getRecordById("ContactNumbers");
			Dataset ContactNumbersDs = new Dataset("ContactNumbers");
			ContactNumbersDs.setId("ContactNumbers");
			ContactNumbersDs.addRecord(contactRecord);
			result.addDataset(ContactNumbersDs);
			List<Record> recordContacts = ContactNumbersDs.getAllRecords();
			if (recordContacts.size() != 0) {
				for (Record record : recordContacts) {
					if (record.getAllParams().size() != 0) {
						record.addParam("Extension", "Home");
						record.addParam("isPrimary", "true");
						record.addParam("Type_id", "COMM_TYPE_PHONE");
						String intPhone = record.getParamValueByName("internationalPhoneNo");
						String natPhone = record.getParamValueByName("nationalPhoneNo");
						if (StringUtils.isNotBlank(natPhone)) {
							record.addParam("Value", natPhone);
						} else if (StringUtils.isNotBlank(intPhone)) {
							record.addParam("Value", intPhone);
						}
						record.addParam("Customer_id", getCustomerId(partyId, request));

					}
				}
			}

			return result;
		} catch (Exception e) {
			result.addParam(new Param("FailureReason", e.getMessage()));
			ErrorCodeEnum.ERR_20001.setErrorCode(result);
			return result;
		}
	}

	private String getCustomerId(String BackendId, DataControllerRequest requestInstance) {

		Map<String, String> postParamsMap = new HashMap<>();
		postParamsMap.put(ODataQueryConstants.FILTER, "BackendId eq '" + BackendId + "'");
		postParamsMap.put(ODataQueryConstants.SELECT, "Customer_id");

		String readBackendIdentifierResponse = Executor.invokeService(ServiceURLEnum.BACKENDIDENTIFIER_READ,
				postParamsMap, null, requestInstance);
		if (readBackendIdentifierResponse != null) {
			JSONObject serviceResponseJSON = CommonUtilities.getStringAsJSONObject(readBackendIdentifierResponse);

			JSONArray serviceResponseArray = serviceResponseJSON.optJSONArray("backendidentifier");
			if (serviceResponseArray.length() != 0) {
				String id = serviceResponseArray.optJSONObject(0).optString("Customer_id");
				return id;
			}
		}
		return null;
	}

}
