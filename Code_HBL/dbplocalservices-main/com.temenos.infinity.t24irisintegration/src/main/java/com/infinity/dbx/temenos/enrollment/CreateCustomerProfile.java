/**
 * 
 */
package com.infinity.dbx.temenos.enrollment;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;

/**
 * @author Gopinath Vaddepally - KH2453
 *
 */
public class CreateCustomerProfile {

	private static class Holder {
		static final CreateCustomerProfile instance = new CreateCustomerProfile();
	}

	public static CreateCustomerProfile getInstance() {
		return Holder.instance;
	}

	private CreateCustomerProfile() {

	}

	/**
	 * @see adds customer contact information
	 * @param customerResult
	 * @param customerProfile
	 */
	public void addContactInformation(Record customerRecord, JsonObject customerProfile) {
		JsonObject contactInformation = new JsonObject();
		if (customerRecord != null) {
			Dataset communicationDevices = customerRecord
					.getDatasetById(EnrollmentConstants.PARAM_COMMUNICATION_DEVICES_DS);
			if (communicationDevices != null && communicationDevices.getAllRecords().size() > 0) {
				List<Record> allCommunicationRecords = communicationDevices.getAllRecords();
				for (Record communicationRecord : allCommunicationRecords) {
					String smsNumber = communicationRecord.getParamValueByName(EnrollmentConstants.PARAM_SMS_NUMBER);
					if (StringUtils.isNotBlank(smsNumber)) {
						contactInformation.addProperty(EnrollmentConstants.PARAM_PHONE_NUMBER, smsNumber);
					}
					String customerEmail = communicationRecord.getParamValueByName(EnrollmentConstants.PARAM_EMAIL);
					if (StringUtils.isNotBlank(customerEmail)) {
						contactInformation.addProperty(EnrollmentConstants.PARAM_EMAIL_ADDRESS, customerEmail);
					}
				}
			}
		}
		customerProfile.add(EnrollmentConstants.PARAM_CONTACT_INFORMATION, contactInformation);
	}

	/**
	 * adding customer profile information
	 * 
	 * @param customerRecord
	 * @param customerProfile
	 */
	public void addPersonalInformation(Record customerRecord, JsonObject customerProfile, String username) {
		JsonObject personalInformation = new JsonObject();
		if (customerRecord != null) {
			/*
			 * personalInformation.addProperty(EnrollmentConstants.PARAM_USERNAME,
			 * username); personalInformation.addProperty(EnrollmentConstants.
			 * PARAM_LAST_NAME_CAMEL_CASE,
			 * String.valueOf(customerRecord.getParamValueByName(EnrollmentConstants.
			 * PARAM_LAST_NAME_CAMEL_CASE)));
			 */
			personalInformation.addProperty(EnrollmentConstants.PARAM_FIRST_NAME, String.valueOf(
					customerRecord.getParamValueByName(EnrollmentConstants.PARAM_CUSTOMER_NAME).split(" ")[0]));
			/*
			 * String dob = ""; if
			 * (StringUtils.isNotBlank(customerRecord.getParamValueByName(
			 * EnrollmentConstants.PARAM_DATE_OF_BIRTH))) { DateTimeFormatter ofPattern =
			 * DateTimeFormatter.ofPattern("dd-MM-yyyy"); dob =
			 * LocalDate.parse(customerRecord.getParamValueByName(EnrollmentConstants.
			 * PARAM_DATE_OF_BIRTH)).format(ofPattern); }
			 * personalInformation.addProperty(EnrollmentConstants.
			 * PARAM_DATE_OF_BIRTH_SMALL_O, dob);
			 * personalInformation.addProperty(EnrollmentConstants.PARAM_SSN_UPPER_CASE,
			 * String.valueOf(customerRecord.getParamValueByName(EnrollmentConstants.
			 * PARAM_SOCIAL_SECURITY_ID)));
			 */
		}
		customerProfile.add(EnrollmentConstants.PARAM_PERSONAL_INFORMATION, personalInformation);
	}

	/**
	 * adding address information
	 * 
	 * @param customerRecord
	 * @param customerProfile
	 */
	public void addAddressInformation(Record customerRecord, JsonObject customerProfile) {
		JsonObject addressIndormation = new JsonObject();
		if (customerRecord != null) {
			/**
			 * add streets
			 */
			Dataset streets = customerRecord.getDatasetById(EnrollmentConstants.PARAM_STREETS);
			if (streets != null && streets.getAllRecords().size() > 0) {
				ArrayList<String> streetList = new ArrayList<>();
				List<Record> allStreets = streets.getAllRecords();
				for (Record record : allStreets) {
					streetList.add(record.getParamValueByName(EnrollmentConstants.PARAM_STREET));
				}
				addressIndormation.addProperty(EnrollmentConstants.PARAM_ADDRESS_LINE_1, String.join(",", streetList));
			}
			/**
			 * add cities
			 */
			Dataset cities = customerRecord.getDatasetById(EnrollmentConstants.PARAM_ADDRESS_CITIES);
			if (cities != null && cities.getAllRecords().size() > 0) {
				ArrayList<String> citiesList = new ArrayList<>();
				List<Record> allCities = cities.getAllRecords();
				for (Record record : allCities) {
					citiesList.add(record.getParamValueByName(EnrollmentConstants.PARAM_ADDRESS_CITY));
				}
				addressIndormation.addProperty(EnrollmentConstants.PARAM_CITY, String.join(",", citiesList));
				
			}
			/*
			 * add state
			 */
			addressIndormation.addProperty(EnrollmentConstants.PARAM_STATE, "Berlin");
			/**
			 * add countries
			 */
			Dataset countries = customerRecord.getDatasetById(EnrollmentConstants.PARAM_COUNTRIES);
			if (countries != null && countries.getAllRecords().size() > 0) {
				ArrayList<String> countriesList = new ArrayList<>();
				List<Record> allCountries = countries.getAllRecords();
				for (Record record : allCountries) {
					countriesList.add(record.getParamValueByName(EnrollmentConstants.PARAM_COUNTRY));
				}
				addressIndormation.addProperty(EnrollmentConstants.PARAM_COUNTRY, String.join(",", countriesList));
			}
			/**
			 * add postal codes
			 */
			Dataset postCodes = customerRecord.getDatasetById(EnrollmentConstants.PARAM_POST_CODES);
			if (postCodes != null && postCodes.getAllRecords().size() > 0) {
				ArrayList<String> postCodesList = new ArrayList<>();
				List<Record> allPostCodes = postCodes.getAllRecords();
				for (Record record : allPostCodes) {
					postCodesList.add(record.getParamValueByName(EnrollmentConstants.PARAM_POST_CODE));
				}
				addressIndormation.addProperty(EnrollmentConstants.PARAM_ZIP_CODE, String.join(",", postCodesList));
			}
		}
		customerProfile.add(EnrollmentConstants.PARAM_ADDRESS_INFORMATION, addressIndormation);
	}

	/**
	 * adding backend identifies infomation
	 * 
	 * @param customerRecord
	 * @param customerProfile
	 */
	public void addBackendIdentifiersInformation(Record customerRecord, JsonObject customerProfile) {
		JsonArray backendArray = new JsonArray();
		if (customerRecord != null) {
			JsonObject backendIdentifiersInfo = new JsonObject();
			backendIdentifiersInfo.addProperty(EnrollmentConstants.PARAM_BACKEND_ID,
					String.valueOf(customerRecord.getParamValueByName(EnrollmentConstants.PARAM_CUSTOMER_ID)));
			backendIdentifiersInfo.addProperty(EnrollmentConstants.PARAM_SEQUENCE_NUMBER, "1");
			backendIdentifiersInfo.addProperty(EnrollmentConstants.PARAM_BACKEND_TYPE,
					EnrollmentConstants.CONSTANT_T24);
			backendIdentifiersInfo.addProperty(EnrollmentConstants.PARAM_IDENTIFIER_NAME,
					EnrollmentConstants.PARAM_CUSTOMER_ID);
			backendArray.add(backendIdentifiersInfo);
		}
		customerProfile.add(EnrollmentConstants.PARAM_BACKEND_IDENTIFIERS_INFO, backendArray);
	}

	/**
	 * adding customer communicatin info
	 * 
	 * @param customerRecord
	 * @param customerProfile
	 */
	public void addCommunicationInfo(Record customerRecord, JsonObject customerProfile) {
		JsonObject coreCommunication = new JsonObject();
		JsonArray phone = new JsonArray();
		JsonArray email = new JsonArray();
		JsonObject contact = new JsonObject();
		if (customerRecord != null) {
			Dataset communicationDevices = customerRecord
					.getDatasetById(EnrollmentConstants.PARAM_COMMUNICATION_DEVICES_DS);
			if (communicationDevices != null && communicationDevices.getAllRecords().size() > 0) {
				List<Record> allCommunicationRecords = communicationDevices.getAllRecords();
				for (Record communicationRecord : allCommunicationRecords) {
					String smsNumber = communicationRecord.getParamValueByName(EnrollmentConstants.PARAM_SMS_NUMBER);
					if (StringUtils.isNotBlank(smsNumber)) {
						smsNumber = smsNumber.replaceAll(" ", "-");
						contact = new JsonObject();
						contact.addProperty(TemenosConstants.PARAM_UNMASKED, smsNumber);
						contact.addProperty(TemenosConstants.PARAM_IS_PRIMARY, Constants.TRUE);
						phone.add(contact);
					}
					String customerEmail = communicationRecord.getParamValueByName(EnrollmentConstants.PARAM_EMAIL);
					if (StringUtils.isNotBlank(customerEmail)) {
						contact = new JsonObject();
						contact.addProperty(TemenosConstants.PARAM_UNMASKED, customerEmail);
						contact.addProperty(TemenosConstants.PARAM_IS_PRIMARY, Constants.TRUE);
						email.add(contact);
					}
				}
			}
		}
		coreCommunication.add(TemenosConstants.PARAM_PHONE, phone);
		coreCommunication.add(TemenosConstants.PARAM_EMAIL, email);
		customerProfile.add(EnrollmentConstants.PARAM_CORE_COMMUNICATION, coreCommunication);
	}
}
