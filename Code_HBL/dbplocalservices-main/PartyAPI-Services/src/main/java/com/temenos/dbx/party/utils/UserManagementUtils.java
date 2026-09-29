//package com.temenos.dbx.party.utils;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import org.apache.commons.lang3.StringUtils;
//
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import com.kony.dbputilities.util.HelperMethods;
//import com.kony.dbputilities.util.logger.LoggerUtil;
//import com.temenos.dbx.product.dto.ContactAddress;
//import com.temenos.dbx.product.dto.ContactPoint;
//import com.temenos.dbx.product.dto.ElectronicAddress;
//import com.temenos.dbx.product.dto.PartyDTO;
//import com.temenos.dbx.product.dto.PartyNames;
//import com.temenos.dbx.product.dto.PhoneAddress;
//import com.temenos.dbx.product.dto.TaxDetails;
//import com.temenos.dbx.product.utils.DTOUtils;
//
//public class UserManagementUtils {
//
//	private static LoggerUtil loggerUtil = new LoggerUtil(UserManagementUtils.class);
//	public static PartyDTO buildPartyfromCreateInput(Map<String, String> inputMap) {
//
//		PartyDTO partyDTO = new PartyDTO();
//
//		JsonObject jsonObject = new JsonObject();
//
//		JsonArray contactPoints = new JsonArray();
//
//		if(StringUtils.isNotBlank(inputMap.get("phoneAddress"))) {
//			try {
//				JsonArray jsonArray = new JsonParser().parse(inputMap.get("phoneAddress")).getAsJsonArray();
//				for(int i=0; i<jsonArray.size(); i++) {
//					JsonObject contactPoint = new JsonObject();
//					contactPoint.add("phoneAddress", jsonArray.get(i).getAsJsonObject());
//					contactPoint.addProperty("startDate", PartyMappings.getCurrentDate());
//					contactPoint.addProperty("contactPointType", "Phone Address");
//					contactPoints.add(contactPoint);
//				}
//
//
//			}catch(Exception e) {
//				loggerUtil.error("Error while parsing phoneAdderss ", e);
//			}
//
//			inputMap.remove("phoneAddress");
//		}
//
//		if(StringUtils.isNotBlank(inputMap.get("partyNames"))) {
//			JsonArray partyNames = new JsonArray();
//			try {
//				JsonArray jsonArray = new JsonParser().parse(inputMap.get("partyNames")).getAsJsonArray();
//				for(int i=0; i<jsonArray.size(); i++) {
//					JsonObject partyName = jsonArray.get(i).getAsJsonObject();
//					partyName.addProperty("startDate", PartyMappings.getCurrentDate());
//					partyNames.add(partyName);
//				}
//
//				jsonObject.add("partyNames", partyNames);
//
//			}catch(Exception e) {
//				loggerUtil.error("Error while parsing partyNames ", e);
//			}
//
//			inputMap.remove("partyNames");
//		}
//
//		if(StringUtils.isNotBlank(inputMap.get("electronicAddress"))) {
//			try {
//				JsonArray jsonArray = new JsonParser().parse(inputMap.get("electronicAddress")).getAsJsonArray();
//				for(int i=0; i<jsonArray.size(); i++) {
//					JsonObject contactPoint = new JsonObject();
//					contactPoint.add("electronicAddress", jsonArray.get(i).getAsJsonObject());
//					contactPoint.addProperty("startDate", PartyMappings.getCurrentDate());
//					contactPoint.addProperty("contactPointType", "Electronic Address");
//					contactPoints.add(contactPoint);
//				}
//
//				inputMap.remove("electronicAddress");
//			}catch(Exception e) {
//				loggerUtil.error("Error while parsing electronicAddress ", e);
//			}
//
//			inputMap.remove("electronicAddress");
//		}
//
//		jsonObject.add("contactPoints", contactPoints);
//
//
//
//		if(StringUtils.isNotBlank(inputMap.get(DTOConstants.CONTACTADDRESS))) {
//			try {
//				JsonArray jsonArray = new JsonParser().parse(inputMap.get(DTOConstants.CONTACTADDRESS)).getAsJsonArray();
//				for(int i=0; i<jsonArray.size(); i++) {
//					JsonObject contactAddress = jsonArray.get(i).getAsJsonObject();
//					contactAddress.addProperty("startDate", PartyMappings.getCurrentDate());
//				}
//
//				inputMap.remove(DTOConstants.CONTACTADDRESS);
//				jsonObject.add(DTOConstants.CONTACTADDRESS, jsonArray);
//			}catch(Exception e) {
//				loggerUtil.error("Error while parsing phoneAdderss ", e);
//			}
//		}
//
//
//		for(Entry<String, String>  entry : inputMap.entrySet()) {
//			try {
//				jsonObject.add(entry.getKey(), new JsonParser().parse(entry.getValue()));
//			}catch (Exception e) {
//				jsonObject.addProperty(entry.getKey(), entry.getValue());
//			}
//		}
//
//		partyDTO.loadFromJson(jsonObject);
//
//		loggerUtil.debug("party built from input "+ partyDTO.toStringJson().toString());
//
//
//		return partyDTO;
//	}
//	public static PartyDTO buildPartyfromUpdateInput(Map<String, String> inputMap, JsonObject jsonObject) {
//
//
//		PartyDTO partyDTO = new PartyDTO();
//
//		partyDTO.loadFromJson(jsonObject);
//
//
//		if(StringUtils.isNotBlank(inputMap.get("phoneAddress"))) {
//
//			try {
//				JsonArray jsonArray = new JsonParser().parse(inputMap.get("phoneAddress")).getAsJsonArray();
//
//				for(int j=0; j<jsonArray.size(); j++) {
//
//					List<ContactPoint> contactPoints = partyDTO.getContactPoints() != null? partyDTO.getContactPoints() : new ArrayList<ContactPoint>();
//
//					boolean isNew = true;
//					JsonObject jsonObject2 = jsonArray.get(j).getAsJsonObject();
//
//					if(!jsonObject2.get("startDate").isJsonNull() && !jsonObject2.get("phoneAddressType").isJsonNull() && StringUtils.isNotBlank(jsonObject2.get("startDate").getAsString()) && StringUtils.isNotBlank(jsonObject2.get("phoneAddressType").getAsString())) {
//
//						for(int i=0; i<contactPoints.size(); i++) {
//
//							ContactPoint contactPoint = contactPoints.get(i);
//
//							if(StringUtils.isNoneBlank(contactPoint.getContactPointType()) && contactPoint.getContactPointType().equals("Phone Address")) {
//								PhoneAddress phoneAddress = contactPoint.getPhoneAddress();
//
//								if(StringUtils.isNotBlank(contactPoint.getStartDate()) && contactPoint.getStartDate().equals(jsonObject2.get("startDate").getAsString())
//										&& StringUtils.isNotBlank(phoneAddress.getPhoneAddressType()) && phoneAddress.getPhoneAddressType().equals(jsonObject2.get("phoneAddressType").getAsString())) {
//									DTOUtils.copy(phoneAddress, DTOUtils.loadJsonObjectIntoObject(jsonObject2, PhoneAddress.class, false));
//									isNew = false;
//									break;
//								}
//							}
//
//						}
//
//						if(isNew) {
//							ContactPoint contactPoint = new ContactPoint();
//							PhoneAddress phoneAddress = new PhoneAddress();
//							phoneAddress.loadFromJson(jsonObject2);
//
//							contactPoint.setPhoneAddress(phoneAddress);
//							contactPoint.setStartDate(PartyMappings.getCurrentDate());
//							contactPoint.setContactPointType("Phone Address");
//							partyDTO.setContactPoint(contactPoint);
//						}
//					}
//				}
//
//			}catch(Exception e) {
//				loggerUtil.error("Error while parsing phoneAdderss ", e);
//			}
//
//			inputMap.remove("phoneAddress");
//		}
//
//
//		if(StringUtils.isNotBlank(inputMap.get("electronicAddress"))) {
//
//			try {
//				JsonArray jsonArray = new JsonParser().parse(inputMap.get("electronicAddress")).getAsJsonArray();
//
//				for(int j=0; j<jsonArray.size(); j++) {
//
//					List<ContactPoint> contactPoints = partyDTO.getContactPoints() != null? partyDTO.getContactPoints() : new ArrayList<ContactPoint>();
//					JsonObject jsonObject2 = jsonArray.get(j).getAsJsonObject();
//
//					boolean isNew = true;
//					if(!jsonObject2.get("startDate").isJsonNull() && !jsonObject2.get("electronicCommunicationType").isJsonNull() && StringUtils.isNotBlank(jsonObject2.get("startDate").getAsString()) && StringUtils.isNotBlank(jsonObject2.get("electronicCommunicationType").getAsString())) {
//
//						for(int i=0; i<contactPoints.size(); i++) {
//
//							ContactPoint contactPoint = contactPoints.get(i);
//
//							if(StringUtils.isNoneBlank(contactPoint.getContactPointType()) && contactPoint.getContactPointType().equals("Electronic Address")) {
//								ElectronicAddress electronicAddress = contactPoint.getElectronicAddress();
//
//								if(StringUtils.isNotBlank(contactPoint.getStartDate()) && contactPoint.getStartDate().equals(jsonObject2.get("startDate").getAsString())
//										&& StringUtils.isNotBlank(electronicAddress.getElectronicCommunicationType()) && electronicAddress.getElectronicCommunicationType().equals(jsonObject2.get("electronicCommunicationType").getAsString())) {
//									DTOUtils.copy(electronicAddress, DTOUtils.loadJsonObjectIntoObject(jsonObject2, ElectronicAddress.class, false));
//									isNew = false;
//									break;
//								}
//
//							}
//
//						}
//
//						if(isNew) {
//							ContactPoint contactPoint = new ContactPoint();
//							ElectronicAddress electronicAddress = new ElectronicAddress();
//							electronicAddress.loadFromJson(jsonObject2);
//
//							contactPoint.setElectronicAddress(electronicAddress);
//							contactPoint.setStartDate(PartyMappings.getCurrentDate());
//							contactPoint.setContactPointType("Electronic Address");
//							partyDTO.setContactPoint(contactPoint);
//						}
//					}
//				}
//
//			}catch(Exception e) {
//				loggerUtil.error("Error while parsing electronicAddress ", e);
//			}
//
//			inputMap.remove("electronicAddress");
//		}
//
//		if(StringUtils.isNotBlank(inputMap.get("partyNames"))) {
//
//			try {
//				JsonArray jsonArray = new JsonParser().parse(inputMap.get("partyNames")).getAsJsonArray();
//
//				for(int j=0; j<jsonArray.size(); j++) {
//
//					List<PartyNames> partyNames = partyDTO.getPartyNames() != null? partyDTO.getPartyNames() : new ArrayList<PartyNames>();
//					JsonObject jsonObject2 = jsonArray.get(j).getAsJsonObject();
//					boolean isNew = true;
//					for(int i=0; i<partyNames.size(); i++) {
//
//						PartyNames partyName = partyNames.get(i);
//
//						if(StringUtils.isNotBlank(partyName.getStartDate()) && partyName.getStartDate().equals(jsonObject2.get("startDate").getAsString())) {
//							DTOUtils.copy(partyName, DTOUtils.loadJsonObjectIntoObject(jsonObject2, PartyNames.class, false));
//							isNew = false;
//							break;
//						}
//					}
//
//					if(isNew) {
//						PartyNames partyName = new PartyNames();
//						partyName.loadFromJson(jsonObject2);
//						partyName.setStartDate(PartyMappings.getCurrentDate());
//						partyDTO.setPartyNames(partyName);
//						break;
//					}
//
//				}
//
//			}catch(Exception e) {
//				loggerUtil.error("Error while parsing partyNames ", e);
//			}
//
//			inputMap.remove("partyNames");
//		}
//
//
//		if(StringUtils.isNotBlank(inputMap.get(DTOConstants.CONTACTADDRESS))) {
//
//			try {
//				JsonArray jsonArray = new JsonParser().parse(inputMap.get(DTOConstants.CONTACTADDRESS)).getAsJsonArray();
//
//				for(int j=0; j<jsonArray.size(); j++) {
//
//					List<ContactAddress> contactAddress = partyDTO.getContactAddress() != null? partyDTO.getContactAddress() : new ArrayList<ContactAddress>();
//					JsonObject jsonObject2 = jsonArray.get(j).getAsJsonObject();
//
//					boolean isNew = true;
//					if(!jsonObject2.get("startDate").isJsonNull() && !jsonObject2.get("physicalAddressType").isJsonNull() && StringUtils.isNotBlank(jsonObject2.get("startDate").getAsString()) && StringUtils.isNotBlank(jsonObject2.get("physicalAddressType").getAsString())) {
//
//						for(int i=0; i<contactAddress.size(); i++) {
//
//							ContactAddress contactAddress2 = contactAddress.get(i);
//
//							if(StringUtils.isNotBlank(contactAddress2.getStartDate()) && contactAddress2.getStartDate().equals(jsonObject2.get("startDate").getAsString())
//									&& StringUtils.isNotBlank(contactAddress2.getPhysicalAddressType()) && contactAddress2.getPhysicalAddressType().equals(jsonObject2.get("physicalAddressType").getAsString())) {
//								DTOUtils.copy(contactAddress2, DTOUtils.loadJsonObjectIntoObject(jsonObject2, ContactAddress.class, false));
//								isNew = false;
//								break;
//							}
//
//						}
//
//						if(isNew) {
//							ContactAddress contactAddress2 = new ContactAddress();
//							contactAddress2.loadFromJson(jsonObject2);
//							contactAddress2.setStartDate(PartyMappings.getCurrentDate());
//							partyDTO.setContactAddress(contactAddress2);
//						}
//
//					}
//				}
//
//			}catch(Exception e) {
//				loggerUtil.error("Error while parsing contactAddress ", e);
//			}
//
//			inputMap.remove(DTOConstants.CONTACTADDRESS);
//		}
//
//
//		buildPartyfromUpdateInput(inputMap, partyDTO);
//
//
//		return partyDTO;
//
//	}
//	private static void buildPartyfromUpdateInput(Map<String, String> inputMap, PartyDTO partyDTO) {
//
//		List<PartyNames> partyNames = partyDTO.getPartyNames() != null? partyDTO.getPartyNames() : new ArrayList<PartyNames>();
//
//		String firstName = inputMap.get("FirstName");
//		String lastName = inputMap.get("LastName");
//		String startDate = StringUtils.isNotBlank(inputMap.get("ApplicationStartDate"))? inputMap.get("ApplicationStartDate") : PartyMappings.getCurrentDate();
//
//		if(partyNames.size() >0) {
//			if(StringUtils.isNotBlank(firstName)) {
//				partyNames.get(0).setFirstName(firstName);
//			}
//
//			if(StringUtils.isNotBlank(lastName)) {
//				partyNames.get(0).setLastName(lastName);
//			}
//		}
//		else {
//			PartyNames names = new PartyNames();
//			names.setStartDate(startDate);
//			names.setFirstName(firstName);
//			names.setLastName(lastName);
//			partyDTO.setPartyNames(names);
//		}
//
//		if(StringUtils.isNotBlank(inputMap.get("DateOfBirth"))) {
//			partyDTO.setDateOfBirth(inputMap.get("DateOfBirth"));
//		}
//
//		String emailID = inputMap.get("EmailAddress");
//		if(StringUtils.isNotBlank(emailID)) {
//			List<ContactPoint> contactPoints = partyDTO.getPartyNames() != null? partyDTO.getContactPoints() : new ArrayList<ContactPoint>();
//			boolean isNew = true;
//			if(contactPoints.size()>0) {
//				for(ContactPoint contactPoint : contactPoints) {
//					if(contactPoint.getContactPointType().equals("Electronic Address")) {
//						contactPoints.get(0).getElectronicAddress().setElectronicAddress(emailID);
//						isNew = false;
//					}
//				}
//			}
//
//			if(isNew) {
//				ContactPoint contactPoint = new ContactPoint();
//				contactPoint.setContactPointType("Electronic Address");
//				contactPoint.setStartDate(startDate);
//				ElectronicAddress electronicAddress = new ElectronicAddress();
//				electronicAddress.setElectronicAddress(emailID);
//				electronicAddress.setElectronicAddressType("Email");
//				electronicAddress.setElectronicCommunicationType("Primary");
//				contactPoint.setElectronicAddress(electronicAddress);
//				partyDTO.setContactPoint(contactPoint);
//			}
//		}
//
//		String phoneNumber = inputMap.get("PhoneNumber");
//		String countryCode = inputMap.get("CountryCode");
//		if(StringUtils.isNotBlank(emailID)) {
//			List<ContactPoint> contactPoints = partyDTO.getPartyNames() != null? partyDTO.getContactPoints() : new ArrayList<ContactPoint>();
//
//			boolean isNew = true;
//			if(contactPoints.size()>0) {
//				for(ContactPoint contactPoint : contactPoints) {
//					if(contactPoint.getContactPointType().equals("Phone Address")) {
//						contactPoints.get(0).getPhoneAddress().setNationalPhoneNumber(phoneNumber);
//						contactPoints.get(0).getPhoneAddress().setIddPrefixPhone(countryCode);
//						isNew = false;
//					}
//				}
//			}
//
//			if(isNew) {
//				ContactPoint contactPoint = new ContactPoint();
//				contactPoint.setContactPointType("Phone Address");
//				contactPoint.setStartDate(startDate);
//				PhoneAddress phoneAddress = new PhoneAddress();
//				phoneAddress.setPhoneAddressType("Mobile Number");
//				phoneAddress.setIddPrefixPhone(countryCode);
//				phoneAddress.setNationalPhoneNumber(phoneNumber);
//				contactPoint.setPhoneAddress(phoneAddress);
//				partyDTO.setContactPoint(contactPoint);
//			}
//		}
//
//		String partyStatus = inputMap.get("PartyStatus");
//
//		if(StringUtils.isNotBlank(partyStatus)) {
//			partyDTO.setPartyStatus(partyStatus);
//		}
//
//		countryCode = inputMap.get("Country");
//		String addressLine1 = inputMap.get("AddressLine1");
//		String addressLine2 = inputMap.get("AddressLine2");
//		String town = inputMap.get("City");
//		String countrySubdivision = inputMap.get("State");
//		String postalOrZipCode = inputMap.get("ZipCode");
//
//
//		if(StringUtils.isNotBlank(countryCode) || StringUtils.isNotBlank(addressLine1) || StringUtils.isNotBlank(addressLine2) || StringUtils.isNotBlank(town) || StringUtils.isNotBlank(countrySubdivision) || StringUtils.isNotBlank(postalOrZipCode) ) {
//			List<ContactAddress> contactAddresses = partyDTO.getPartyNames() != null? partyDTO.getContactAddress() : new ArrayList<ContactAddress>();
//
//			if(contactAddresses.size()>0) {
//				ContactAddress contactAddress = contactAddresses.get(0);
//				if(StringUtils.isNotBlank(countryCode)) {
//					contactAddress.setCountryCode(countryCode);
//				}
//
//				if(StringUtils.isNotBlank(addressLine1)) {
//					contactAddress.setFlatNumber(addressLine1);
//				}
//
//				if( StringUtils.isNotBlank(addressLine2)) {
//					contactAddress.setBuildingName(addressLine2);
//				}
//
//				if( StringUtils.isNotBlank(town)) {
//					contactAddress.setTown(town);
//				}
//
//				if(StringUtils.isNotBlank(countrySubdivision) ) {
//					contactAddress.setCountrySubdivision(countrySubdivision);
//				}
//
//				if(StringUtils.isNotBlank(postalOrZipCode)) {
//					contactAddress.setPostalOrZipCode(postalOrZipCode);
//				}
//
//			}
//			else {
//				ContactAddress contactAddress = new ContactAddress();
//				contactAddress.setPhysicalAddressType("Home");
//				contactAddress.setReliabilityType("Confirmed");
//				contactAddress.setCountryCode(countryCode);
//				contactAddress.setFlatNumber(addressLine1);
//				contactAddress.setBuildingName(addressLine2);
//				contactAddress.setTown(town);
//				contactAddress.setCountrySubdivision(countrySubdivision);
//				contactAddress.setPostalOrZipCode(postalOrZipCode);
//				partyDTO.setContactAddress(contactAddress);
//			}
//		}
//
//
//
//		//		partyObj.put("TaxId", party.getTaxId());
//		//		partyObj.put("IdentityType", party.getIdentityType());
//		//		partyObj.put("IdentityNumber", party.getIdentityNumber());
//		//		partyObj.put("IssuingCountry", getCountryCode(party.getCountry()));
//		//		partyObj.put("IssuingState", party.getIssuingState());
//		//		partyObj.put("IssueDate", party.getIssueDate());
//		//		partyObj.put("ExpiryDate", party.getExpiryDate());
//
//	}
//
//
//
//
//
//}
