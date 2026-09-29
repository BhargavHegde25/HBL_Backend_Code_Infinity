package com.temenos.dbx.usermanagement.backenddelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.infinity.dbx.dbp.jwt.auth.AuthConstants;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.temenos.dbx.party.utils.PartyMappings;
import com.temenos.dbx.party.utils.PartyPropertyConstants;
import com.temenos.dbx.party.utils.PartyURLFinder;
import com.temenos.dbx.party.utils.PartyUtils;
import com.temenos.dbx.product.dto.BackendIdentifierDTO;
import com.temenos.dbx.product.dto.CustomerCommunicationDTO;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.PartyAddress;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.BackendIdentifiersBackendDelegate;
import com.temenos.dbx.product.usermanagement.backenddelegate.api.CommunicationBackendDelegate;
import com.temenos.dbx.product.usermanagement.backenddelegate.impl.CommunicationBackendDelegateImpl;
import com.temenos.dbx.product.utils.DTOConstants;
import com.temenos.dbx.product.utils.DTOUtils;
import com.temenos.dbx.product.utils.HTTPOperations;
import com.temenos.dbx.product.utils.InfinityConstants;

public class PartyCommunicationBackendDelegateImpl implements CommunicationBackendDelegate {
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    @Override
    public DBXResult get(CustomerCommunicationDTO dto, Map<String, Object> headerMap) {
        LoggerUtil logger = new LoggerUtil(PartyCommunicationBackendDelegateImpl.class);

        DBXResult dbxResult = new DBXResult();
        if (StringUtils.isBlank(dto.getCustomer_id())) {
            return dbxResult;
        }

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(dto.getCustomer_id());
        customerDTO = (CustomerDTO) customerDTO.loadDTO();

        String partyID = null;
        if (customerDTO != null) {

            if (HelperMethods.getBusinessUserTypes().contains(customerDTO.getCustomerType_id())
                    || customerDTO.isCombinedUser()) {
                return new CommunicationBackendDelegateImpl().get(dto, headerMap);
            }

            BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
            backendIdentifierDTO.setCustomer_id(dto.getCustomer_id());
            backendIdentifierDTO.setBackendType(DTOConstants.PARTY);
            backendIdentifierDTO.setCompanyLegalUnit(dto.getCompanyLegalUnit());

            try {
                dbxResult = DBPAPIAbstractFactoryImpl.getBackendDelegate(BackendIdentifiersBackendDelegate.class)
                        .get(backendIdentifierDTO, headerMap);
            } catch (ApplicationException e1) {
                // TODO Auto-generated catch block
                alert.prepareError("Error while fetching backend identifier for customer ID " + dto.getCustomer_id()).log();
            }
        } else {
            partyID = dto.getCustomer_id();
        }

        try {
            if (dbxResult.getResponse() != null) {
                BackendIdentifierDTO identifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();
                partyID = identifierDTO.getBackendId();
                dbxResult = new DBXResult();
            }
            String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) +
                    PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_COMMUNICATION,
                            partyID);

            PartyUtils.addJWTAuthHeader(headerMap, AuthConstants.PRE_LOGIN_FLOW);
            DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET,
                    partyURL, null, headerMap);

            JsonObject jsonObject = new JsonParser().parse((String) response.getResponse()).getAsJsonObject();
            if (jsonObject.has(DTOConstants.PARTY_ADDRESS)
                    && !jsonObject.get(DTOConstants.PARTY_ADDRESS).isJsonNull()) {
                List<PartyAddress> contactPoints =
                        PartyAddress.loadFromJsonArray(jsonObject.get(DTOConstants.PARTY_ADDRESS).getAsJsonArray());
                List<CustomerCommunicationDTO> communicationDTOs = new ArrayList<CustomerCommunicationDTO>();
                for (PartyAddress contactPoint : contactPoints) {
                    CustomerCommunicationDTO communicationDTO = new CustomerCommunicationDTO();
					if (contactPoint.getCommunicationNature().equals(DTOConstants.PHONE)) {
						communicationDTO.setType_id(HelperMethods.getCommunicationTypes().get(DTOConstants.PHONE));
						if (contactPoint.getPrimary().contentEquals("true")) {
							communicationDTO.setIsPrimary(true);
						} else {
							communicationDTO.setIsPrimary(false);
						}
						String extension = PartyMappings.getCustomerPhoneTypeMapping()
								.get(contactPoint.getAddressType());
						extension = StringUtils.isNotBlank(extension) ? extension : DTOConstants.PHONE;
						communicationDTO.setExtension(extension);
						communicationDTO.setId(contactPoint.getAddressesReference());
						String phoneNo = contactPoint.getPhoneNo();
						String phoneCountryCode = "", phone = "";
						if (phoneNo.contains("-")) {
							phoneCountryCode = phoneNo.substring(0, phoneNo.indexOf("-"));
							phone = phoneNo.substring(phoneNo.indexOf("-") + 1);
							communicationDTO.setValue(phone);
							communicationDTO.setPhoneCountryCode(phoneCountryCode);
						} else {
							communicationDTO.setValue(contactPoint.getPhoneNo());
							communicationDTO.setPhoneCountryCode(contactPoint.getIddPrefixPhone());
						}
						communicationDTOs.add(communicationDTO);
					} else if (contactPoint.getCommunicationNature().equals(DTOConstants.ELECTRONIC)
                            && contactPoint.getCommunicationType().equals(DTOConstants.EMAIL)) {
                        communicationDTO.setType_id(HelperMethods.getCommunicationTypes().get(DTOConstants.EMAIL));
                        communicationDTO.setValue(contactPoint.getElectronicAddress());
                        if (contactPoint.getPrimary().contentEquals("true")) {
                            communicationDTO.setIsPrimary(true);
                        } else {
                            communicationDTO.setIsPrimary(false);
                        }

                        communicationDTO.setId(contactPoint.getAddressesReference());
                        communicationDTOs.add(communicationDTO);
                    }
                }
                dbxResult.setResponse(communicationDTOs);
            }

        } catch (Exception e) {
            alert.prepareError("Caught exception while getting Party Comunication Details: ", e).log();
            return dbxResult;
        }

        return dbxResult;
    }

    @Override
    public DBXResult create(CustomerCommunicationDTO inputDTO,
            Map<String, Object> headersMap) {

        DBXResult dbxResult = new DBXResult();

        CustomerCommunicationDTO resultDTO = null;
        if (null == inputDTO || StringUtils.isBlank(inputDTO.getId())) {
            return dbxResult;
        }
        Map<String, Object> inputParams = DTOUtils.getParameterMap(inputDTO, true);
        HelperMethods.removeNullValues(inputParams);
        JsonObject communicationJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                URLConstants.CUSTOMERCOMMUNICATION_CREATE);
        if (JSONUtil.isJsonNotNull(communicationJson)
                && JSONUtil.hasKey(communicationJson, DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION)
                && communicationJson.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION).isJsonArray()) {
            JsonArray communicationArray =
                    communicationJson.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION).getAsJsonArray();
            JsonObject object =
                    communicationArray.size() > 0 ? communicationArray.get(0).getAsJsonObject() : new JsonObject();
            resultDTO = (CustomerCommunicationDTO) DTOUtils.loadJsonObjectIntoObject(object,
                    CustomerCommunicationDTO.class, true);

            dbxResult.setResponse(resultDTO);
        }

        return dbxResult;
    }

    @Override
    public DBXResult getPrimaryCommunication(CustomerCommunicationDTO customerCommunicationDTO,
            Map<String, Object> headerMap) {
        LoggerUtil logger = new LoggerUtil(PartyCommunicationBackendDelegateImpl.class);

        DBXResult dbxResult = new DBXResult();

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customerCommunicationDTO.getCustomer_id());
        customerDTO = (CustomerDTO) customerDTO.loadDTO();

        if (customerDTO == null) {
            return dbxResult;
        }

        if (HelperMethods.getBusinessUserTypes().contains(customerDTO.getCustomerType_id())
                || customerDTO.isCombinedUser()) {
            return new CommunicationBackendDelegateImpl().getPrimaryCommunication(customerCommunicationDTO, headerMap);
        }

        JsonObject result = new JsonObject();
        dbxResult.setResponse(result);

        if (StringUtils.isBlank(customerCommunicationDTO.getCustomer_id())) {
            return dbxResult;
        }

        BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
        backendIdentifierDTO.setCustomer_id(customerCommunicationDTO.getCustomer_id());
        backendIdentifierDTO.setBackendType(DTOConstants.PARTY);

        try {
            dbxResult = DBPAPIAbstractFactoryImpl.getBackendDelegate(BackendIdentifiersBackendDelegate.class)
                    .get(backendIdentifierDTO, headerMap);
        } catch (ApplicationException e1) {
            // TODO Auto-generated catch block
            alert.prepareError("Error while fetching backend identifier for customer ID "
                    + customerCommunicationDTO.getCustomer_id()).log();
        }

        try {
            if (dbxResult.getResponse() != null) {
                BackendIdentifierDTO identifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();

                String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) +
                        PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_COMMUNICATION,
                                identifierDTO.getBackendId());

                dbxResult = new DBXResult();
                PartyUtils.addJWTAuthHeader(headerMap, AuthConstants.PRE_LOGIN_FLOW);
                DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET,
                        partyURL, null, headerMap);

                JsonObject jsonObject = new JsonParser().parse((String) response.getResponse()).getAsJsonObject();

                JsonObject resultJson = new JsonObject();
                JsonArray resultJsonArray = new JsonArray();

                if (jsonObject.has(DTOConstants.PARTY_ADDRESS)
                        && !jsonObject.get(DTOConstants.PARTY_ADDRESS).isJsonNull()) {
                    List<PartyAddress> contactPoints =
                            PartyAddress.loadFromJsonArray(jsonObject.get(DTOConstants.PARTY_ADDRESS).getAsJsonArray());
                    CustomerCommunicationDTO communicationDTO = new CustomerCommunicationDTO();
                    for (PartyAddress contactPoint : contactPoints) {
                        if (contactPoint.getCommunicationNature().equals(DTOConstants.PHONE)
                                && Boolean.parseBoolean(contactPoint.getPrimary())) {
                            communicationDTO.setType_id(HelperMethods.getCommunicationTypes().get(DTOConstants.PHONE));
                            communicationDTO.setValue(contactPoint.getPhoneNo());
                            communicationDTO.setPhoneCountryCode(contactPoint.getIddPrefixPhone());
                            communicationDTO.setIsPrimary(true);
                            communicationDTO.setExtension(PartyMappings.getCustomerPhoneTypeMapping()
                                    .get(contactPoint.getAddressType()));
                            communicationDTO.setId(contactPoint.getAddressesReference());
                            resultJson = DTOUtils.getJsonObjectFromObject(communicationDTO, true);
                            resultJsonArray.add(resultJson);
                        } else if (contactPoint.getCommunicationNature().equals(DTOConstants.ELECTRONIC) &&
                                contactPoint.getCommunicationType().equals(DTOConstants.EMAIL)
                                && Boolean.parseBoolean(contactPoint.getPrimary())) {

                            communicationDTO.setType_id(HelperMethods.getCommunicationTypes().get(DTOConstants.EMAIL));
                            communicationDTO.setValue(contactPoint.getElectronicAddress());
                            communicationDTO.setIsPrimary(true);
                            communicationDTO.setId(contactPoint.getAddressesReference());
                            resultJson = DTOUtils.getJsonObjectFromObject(communicationDTO, true);
                            resultJsonArray.add(resultJson);
                        }
                    }

                }

                result.add(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION, resultJson);

                dbxResult.setResponse(result);
            }

        } catch (Exception e) {
            alert.prepareError("Caught exception while getting Party Contact Details: ", e).log();
            return dbxResult;
        }

        return dbxResult;
    }

    @Override
    public DBXResult getPrimaryCommunicationForLogin(CustomerCommunicationDTO customerCommunicationDTO,
			Map<String, Object> headerMap) {
		LoggerUtil logger = new LoggerUtil(PartyCommunicationBackendDelegateImpl.class);

		DBXResult dbxResult = new DBXResult();

		JsonObject result = new JsonObject();
		dbxResult.setResponse(result);

		if (StringUtils.isBlank(customerCommunicationDTO.getCustomer_id())) {
			return dbxResult;
		}

		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setId(customerCommunicationDTO.getCustomer_id());
		customerDTO = (CustomerDTO) customerDTO.loadDTO();

		if (customerDTO != null) {

			if (HelperMethods.getBusinessUserTypes().contains(customerDTO.getCustomerType_id())
					|| customerDTO.isCombinedUser()) {
				return new CommunicationBackendDelegateImpl().getPrimaryCommunicationForLogin(customerCommunicationDTO,
						headerMap);
			}
		}
		BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
		backendIdentifierDTO.setCustomer_id(customerCommunicationDTO.getCustomer_id());
		backendIdentifierDTO.setBackendType(DTOConstants.PARTY);

		try {
			dbxResult = DBPAPIAbstractFactoryImpl.getBackendDelegate(BackendIdentifiersBackendDelegate.class)
					.get(backendIdentifierDTO, headerMap);
		} catch (ApplicationException e1) {
			// TODO Auto-generated catch block
			alert.prepareError("Error while fetching backend identifier for customer ID "
					+ customerCommunicationDTO.getCustomer_id()).log();
		}

		try {
			String partyID = null;
			JsonObject resultJson = new JsonObject();
			if (dbxResult.getResponse() != null) {
				BackendIdentifierDTO identifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();
				partyID = identifierDTO.getBackendId();
				String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL)
						+ PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_COMMUNICATION, partyID);

				dbxResult = new DBXResult();
				PartyUtils.addJWTAuthHeader(headerMap, AuthConstants.PRE_LOGIN_FLOW);
				DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET, partyURL, null,
						headerMap);

				JsonObject jsonObject = new JsonParser().parse((String) response.getResponse()).getAsJsonObject();

				if (jsonObject.has(DTOConstants.PARTY_ADDRESS)
						&& !jsonObject.get(DTOConstants.PARTY_ADDRESS).isJsonNull()) {
					List<PartyAddress> contactPoints = PartyAddress
							.loadFromJsonArray(jsonObject.get(DTOConstants.PARTY_ADDRESS).getAsJsonArray());
					for (PartyAddress contactPoint : contactPoints) {
						if (contactPoint.getCommunicationNature().equals(DTOConstants.PHONE)
								&& Boolean.parseBoolean(contactPoint.getPrimary())) {
							resultJson.addProperty(DTOConstants.PHONE, contactPoint.getPhoneNo());
							resultJson.addProperty(DTOConstants.PHONECOUNTRYCODE, contactPoint.getIddPrefixPhone());
						} else if (contactPoint.getCommunicationNature().equals(DTOConstants.ELECTRONIC)
								&& contactPoint.getCommunicationType().equals(DTOConstants.EMAIL)
								&& Boolean.parseBoolean(contactPoint.getPrimary())) {
							resultJson.addProperty(DTOConstants.EMAIL, contactPoint.getElectronicAddress());
						}
					}
				}

				if (jsonObject.has(DTOConstants.PARTY_ADDRESS)
						&& !jsonObject.get(DTOConstants.PARTY_ADDRESS).isJsonNull()) {
					List<PartyAddress> contactAddress = PartyAddress
							.loadFromJsonArray(jsonObject.get(DTOConstants.PARTY_ADDRESS).getAsJsonArray());
					for (PartyAddress contactAddres : contactAddress) {
						if (Boolean.parseBoolean(contactAddres.getPrimary())
								&& contactAddres.getCommunicationNature().equals(DTOConstants.PHYSICAL)) {
							resultJson.addProperty("addressLine1", contactAddres.getBuildingName());
							resultJson.addProperty("addressLine2", contactAddres.getStreetName());
							resultJson.addProperty("zipCode", contactAddres.getPostalOrZipCode());
							resultJson.addProperty("state", contactAddres.getCountrySubdivision());
							resultJson.addProperty("country", contactAddres.getCountryCode());
							resultJson.addProperty("countrycode", contactAddres.getCountryCode());
							resultJson.addProperty("CountryCode", contactAddres.getCountryCode());
							resultJson.addProperty("cityName", contactAddres.getTown());
							resultJson.addProperty("Region_id", contactAddres.getCountrySubdivision());
							resultJson.addProperty("City_id", contactAddres.getTown());
							break;
						}
					}

				}

				result.add(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION, resultJson);
				dbxResult.setResponse(result);
			} else {
				partyID = customerCommunicationDTO.getCustomer_id();
				Map<String, Object> inputParams = new HashMap<>();
				String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerCommunicationDTO.getCustomer_id()
						+ DBPUtilitiesConstants.AND + "isPrimary" + DBPUtilitiesConstants.EQUAL + "1";

				inputParams.put(DBPUtilitiesConstants.FILTER, filter);

				JsonObject communicationJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
						URLConstants.CUSTOMER_COMMUNICATION_GET);

				if (communicationJson.has(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION)
						&& !communicationJson.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION).isJsonNull()) {

					JsonArray array = communicationJson.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION)
							.getAsJsonArray();
					for (int i = 0; i < array.size(); i++) {
						JsonObject communication = array.get(i).getAsJsonObject();
						if (communication.has("Type_id") && communication.get("Type_id").getAsString()
								.equals(DBPUtilitiesConstants.COMM_TYPE_EMAIL)) {
							resultJson.add(DTOConstants.EMAIL, communication.get("Value"));
						} else if (communication.has("Type_id") && communication.get("Type_id").getAsString()
								.equals(DBPUtilitiesConstants.COMM_TYPE_PHONE)) {
							resultJson.add(DTOConstants.PHONE, communication.get("Value"));
							resultJson.add(DTOConstants.PHONECOUNTRYCODE, communication.get("phoneCountryCode"));
						}
					}
					result.add(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION, resultJson);
					dbxResult.setResponse(result);
				}
			}

		} catch (Exception e) {
			alert.prepareError("Caught exception while getting Party Contact Details: ", e).log();
			return dbxResult;
		}

		return dbxResult;
	}

    @Override
    public DBXResult getCommunication(CustomerCommunicationDTO customerCommunicationDTO,
            Map<String, Object> headerMap) {
        LoggerUtil logger = new LoggerUtil(PartyCommunicationBackendDelegateImpl.class);

        DBXResult dbxResult = new DBXResult();

        JsonObject result = new JsonObject();
        dbxResult.setResponse(result);

        if (StringUtils.isBlank(customerCommunicationDTO.getCustomer_id())) {
            return dbxResult;
        }

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customerCommunicationDTO.getCustomer_id());
        customerDTO = (CustomerDTO) customerDTO.loadDTO();

        if (customerDTO == null) {
            return dbxResult;
        }

        if (HelperMethods.getBusinessUserTypes().contains(customerDTO.getCustomerType_id())
                || customerDTO.isCombinedUser()) {
            return new CommunicationBackendDelegateImpl().getCommunication(customerCommunicationDTO, headerMap);
        }

        BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
        backendIdentifierDTO.setCustomer_id(customerCommunicationDTO.getCustomer_id());
        backendIdentifierDTO.setBackendType(DTOConstants.PARTY);

        try {
            dbxResult = DBPAPIAbstractFactoryImpl.getBackendDelegate(BackendIdentifiersBackendDelegate.class)
                    .get(backendIdentifierDTO, headerMap);
        } catch (ApplicationException e1) {
            // TODO Auto-generated catch block
            alert.prepareError("Error while fetching backend identifier for customer ID "
                    + customerCommunicationDTO.getCustomer_id()).log();
        }

        try {
            if (dbxResult.getResponse() != null) {
                BackendIdentifierDTO identifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();

                String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) +
                        PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_COMMUNICATION,
                                identifierDTO.getBackendId());

                dbxResult = new DBXResult();
                PartyUtils.addJWTAuthHeader(headerMap, AuthConstants.PRE_LOGIN_FLOW);
                DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET,
                        partyURL, null, headerMap);

                JsonObject jsonObject = new JsonParser().parse((String) response.getResponse()).getAsJsonObject();

                JsonObject resultJson = new JsonObject();
                JsonArray resultJsonArray = new JsonArray();

                if (jsonObject.has(DTOConstants.PARTY_ADDRESS)
                        && !jsonObject.get(DTOConstants.PARTY_ADDRESS).isJsonNull()) {
                    List<PartyAddress> contactPoints =
                            PartyAddress.loadFromJsonArray(jsonObject.get(DTOConstants.PARTY_ADDRESS).getAsJsonArray());
                    for (PartyAddress contactPoint : contactPoints) {
                        CustomerCommunicationDTO communicationDTO = new CustomerCommunicationDTO();
                        if (contactPoint.getCommunicationNature().equals(DTOConstants.PHONE)) {
                            communicationDTO.setType_id(HelperMethods.getCommunicationTypes().get(DTOConstants.PHONE));
                            communicationDTO.setValue(contactPoint.getPhoneNo());
                            communicationDTO.setPhoneCountryCode(contactPoint.getIddPrefixPhone());
                            if (Boolean.parseBoolean(contactPoint.getPrimary())) {
                                communicationDTO.setIsPrimary(true);
                            } else {
                                communicationDTO.setIsPrimary(false);
                            }
                            communicationDTO.setExtension(PartyMappings.getCustomerPhoneTypeMapping()
                                    .get(contactPoint.getAddressType()));
                            communicationDTO.setId(contactPoint.getAddressesReference());
                            resultJson = DTOUtils.getJsonObjectFromObject(communicationDTO, true);
                            resultJsonArray.add(resultJson);
                        } else if (contactPoint.getCommunicationNature().equals(DTOConstants.ELECTRONIC)
                                && contactPoint.getCommunicationType().equals(DTOConstants.EMAIL)) {
                            communicationDTO.setType_id(HelperMethods.getCommunicationTypes().get(DTOConstants.EMAIL));
                            communicationDTO.setValue(contactPoint.getElectronicAddress());
                            if (Boolean.parseBoolean(contactPoint.getPrimary())) {
                                communicationDTO.setIsPrimary(true);
                            } else {
                                communicationDTO.setIsPrimary(false);
                            }
                            communicationDTO.setId(contactPoint.getAddressesReference());
                            resultJson = DTOUtils.getJsonObjectFromObject(communicationDTO, true);
                            resultJsonArray.add(resultJson);
                        }

                    }

                    result.add(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION, resultJsonArray);

                    dbxResult.setResponse(result);
                }
            }

        } catch (Exception e) {
            alert.prepareError("Caught exception while getting Party Contact Details: ", e).log();
            return dbxResult;
        }

        return dbxResult;
    }

    @Override
    public DBXResult getPrimaryCommunicationDetails(CustomerCommunicationDTO customerCommunicationDTO,
            Map<String, Object> headerMap) {
        LoggerUtil logger = new LoggerUtil(PartyCommunicationBackendDelegateImpl.class);
        DBXResult dbxResult = new DBXResult();
        JsonObject result = new JsonObject();
        dbxResult.setResponse(result);

        if (StringUtils.isBlank(customerCommunicationDTO.getCustomer_id())) {
            return dbxResult;
        }

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customerCommunicationDTO.getCustomer_id());
        customerDTO = (CustomerDTO) customerDTO.loadDTO();

        if (customerDTO == null) {
            return dbxResult;
        }

        if (HelperMethods.getBusinessUserTypes().contains(customerDTO.getCustomerType_id())
                || customerDTO.isCombinedUser()) {
            return new CommunicationBackendDelegateImpl().getPrimaryCommunicationForLogin(customerCommunicationDTO,
                    headerMap);
        }

        BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
        backendIdentifierDTO.setCustomer_id(customerCommunicationDTO.getCustomer_id());
        backendIdentifierDTO.setBackendType(DTOConstants.PARTY);

        try {
            dbxResult = DBPAPIAbstractFactoryImpl.getBackendDelegate(BackendIdentifiersBackendDelegate.class)
                    .get(backendIdentifierDTO, headerMap);
        } catch (ApplicationException e1) {
            alert.prepareError("Exception occured while fetching party backendidentifier ID"
                    + customerCommunicationDTO.getCustomer_id()).log();
        }

        String partyID = null;
        try {
            if (dbxResult.getResponse() != null) {
                BackendIdentifierDTO identifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();
                partyID = identifierDTO.getBackendId();
            } else
                partyID = customerCommunicationDTO.getCustomer_id();

            String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) +
                    PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_COMMUNICATION,
                            partyID);

            dbxResult = new DBXResult();
            PartyUtils.addJWTAuthHeader(headerMap, AuthConstants.PRE_LOGIN_FLOW);
            DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET,
                    partyURL, null, headerMap);

            JsonObject jsonObject = new JsonParser().parse((String) response.getResponse()).getAsJsonObject();

            JsonObject resultJson = new JsonObject();

            if (jsonObject.has(DTOConstants.PARTY_ADDRESS)
                    && !jsonObject.get(DTOConstants.PARTY_ADDRESS).isJsonNull()) {
                List<PartyAddress> contactPoints =
                        PartyAddress.loadFromJsonArray(jsonObject.get(DTOConstants.PARTY_ADDRESS).getAsJsonArray());
                for (PartyAddress contactPoint : contactPoints) {
                    if (contactPoint.getCommunicationNature().equals(DTOConstants.PHONE)
                            && Boolean.parseBoolean(contactPoint.getPrimary())) {
                        resultJson.addProperty(DTOConstants.PHONE,
                                contactPoint.getPhoneNo());
                        resultJson.addProperty(DTOConstants.PHONECOUNTRYCODE,
                                contactPoint.getIddPrefixPhone());
                        resultJson.addProperty(DTOConstants.PHONE,
                                (contactPoint.getIddPrefixPhone() != null ? contactPoint.getIddPrefixPhone() : "")
                                        + resultJson.get(DTOConstants.PHONE).getAsString());
                    } else if (contactPoint.getCommunicationNature().equals(DTOConstants.ELECTRONIC) &&
                            contactPoint.getCommunicationType().equals(DTOConstants.EMAIL)
                            && Boolean.parseBoolean(contactPoint.getPrimary())) {
                        resultJson.addProperty(DTOConstants.EMAIL,
                                contactPoint.getElectronicAddress());
                    }
                }
            }
            result.add(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION, resultJson);
            dbxResult.setResponse(result);
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching the Party communication details", e).log();
            return dbxResult;
        }
        return dbxResult;
    }

    @Override
    public DBXResult getCommunicationDetails(CustomerCommunicationDTO customerCommunicationDTO,
            Map<String, Object> headerMap) {
        LoggerUtil logger = new LoggerUtil(PartyCommunicationBackendDelegateImpl.class);

        DBXResult dbxResult = new DBXResult();

        JsonObject result = new JsonObject();
        dbxResult.setResponse(result);

        if (StringUtils.isBlank(customerCommunicationDTO.getCustomer_id())) {
            return dbxResult;
        }

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customerCommunicationDTO.getCustomer_id());
        customerDTO = (CustomerDTO) customerDTO.loadDTO();

        if (customerDTO == null) {
            return dbxResult;
        }

        if (HelperMethods.getBusinessUserTypes().contains(customerDTO.getCustomerType_id())
                || customerDTO.isCombinedUser()) {
            return new CommunicationBackendDelegateImpl().getCommunication(customerCommunicationDTO, headerMap);
        }

        BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
        backendIdentifierDTO.setCustomer_id(customerCommunicationDTO.getCustomer_id());
        backendIdentifierDTO.setBackendType(DTOConstants.PARTY);

        try {
            dbxResult = DBPAPIAbstractFactoryImpl.getBackendDelegate(BackendIdentifiersBackendDelegate.class)
                    .get(backendIdentifierDTO, headerMap);
        } catch (ApplicationException e1) {
            // TODO Auto-generated catch block
            alert.prepareError("Error while fetching backend identifier for customer ID "
                    + customerCommunicationDTO.getCustomer_id()).log();
        }

        try {
            if (dbxResult.getResponse() != null) {
                BackendIdentifierDTO identifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();

                String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) +
                        PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_COMMUNICATION,
                                identifierDTO.getBackendId());

                dbxResult = new DBXResult();
                PartyUtils.addJWTAuthHeader(headerMap, AuthConstants.PRE_LOGIN_FLOW);
                DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET,
                        partyURL, null, headerMap);

                JsonObject jsonObject = new JsonParser().parse((String) response.getResponse()).getAsJsonObject();

                JsonObject resultJson = new JsonObject();
                JsonArray resultJsonArray = new JsonArray();

                if (jsonObject.has(DTOConstants.PARTY_ADDRESS)
                        && !jsonObject.get(DTOConstants.PARTY_ADDRESS).isJsonNull()) {
                    List<PartyAddress> contactPoints =
                            PartyAddress.loadFromJsonArray(jsonObject.get(DTOConstants.PARTY_ADDRESS).getAsJsonArray());
                    for (PartyAddress contactPoint : contactPoints) {
                        CustomerCommunicationDTO communicationDTO = new CustomerCommunicationDTO();
                        if (contactPoint.getCommunicationNature().equals(DTOConstants.PHONE)) {
                            communicationDTO.setType_id(HelperMethods.getCommunicationTypes().get(DTOConstants.PHONE));
                            communicationDTO.setValue(
                                    (contactPoint.getIddPrefixPhone() != null ? contactPoint.getIddPrefixPhone() : "")
                                            + contactPoint.getPhoneNo());
                            communicationDTO.setPhoneCountryCode(contactPoint.getIddPrefixPhone());
                            if (Boolean.parseBoolean(contactPoint.getPrimary())) {
                                communicationDTO.setIsPrimary(true);
                            } else {
                                communicationDTO.setIsPrimary(false);
                            }
                            communicationDTO.setId(contactPoint.getAddressesReference());
                            communicationDTO.setExtension(PartyMappings.getCustomerPhoneTypeMapping()
                                    .get(contactPoint.getAddressType()));
                            resultJson = DTOUtils.getJsonObjectFromObject(communicationDTO, true);
                            resultJsonArray.add(resultJson);
                        } else if (contactPoint.getCommunicationNature().equals(DTOConstants.ELECTRONIC)
                                && contactPoint.getCommunicationType().equals(DTOConstants.EMAIL)) {
                            communicationDTO.setType_id(HelperMethods.getCommunicationTypes().get(DTOConstants.EMAIL));
                            communicationDTO.setValue(contactPoint.getElectronicAddress());
                            if (Boolean.parseBoolean(contactPoint.getPrimary())) {
                                communicationDTO.setIsPrimary(true);
                            } else {
                                communicationDTO.setIsPrimary(false);
                            }
                            communicationDTO.setId(contactPoint.getAddressesReference());
                            resultJson = DTOUtils.getJsonObjectFromObject(communicationDTO, true);
                            resultJsonArray.add(resultJson);
                        }

                    }

                    result.add(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION, resultJsonArray);

                    dbxResult.setResponse(result);
                }
            }

        } catch (Exception e) {
            alert.prepareError("Caught exception while getting Party Contact Details: ", e).log();
            return dbxResult;
        }

        return dbxResult;
    }

    @Override
    public DBXResult getPrimaryMFACommunicationDetails(CustomerCommunicationDTO customerCommunicationDTO, Map<String, Object> headerMap) {
        
        LoggerUtil logger = new LoggerUtil(PartyCommunicationBackendDelegateImpl.class);
        DBXResult dbxResult = new DBXResult();
        JsonObject result = new JsonObject();
        dbxResult.setResponse(result);

        if (StringUtils.isBlank(customerCommunicationDTO.getCustomer_id())) {
            return dbxResult;
        }

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customerCommunicationDTO.getCustomer_id());
        customerDTO = (CustomerDTO) customerDTO.loadDTO();

        if (customerDTO == null) {
            return dbxResult;
        }

        if (HelperMethods.getBusinessUserTypes().contains(customerDTO.getCustomerType_id())
                || customerDTO.isCombinedUser()) {
            return new CommunicationBackendDelegateImpl().getPrimaryCommunicationForLogin(customerCommunicationDTO,
                    headerMap);
        }

        BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
        backendIdentifierDTO.setCustomer_id(customerCommunicationDTO.getCustomer_id());
		if (StringUtils.isNotBlank(customerCommunicationDTO.getCompanyLegalUnit())) {
			backendIdentifierDTO.setCompanyLegalUnit(customerCommunicationDTO.getCompanyLegalUnit());
		}
        backendIdentifierDTO.setBackendType(DTOConstants.PARTY);

        try {
            dbxResult = DBPAPIAbstractFactoryImpl.getBackendDelegate(BackendIdentifiersBackendDelegate.class)
                    .get(backendIdentifierDTO, headerMap);
        } catch (ApplicationException e1) {
            alert.prepareError("Exception occured while fetching party backendidentifier ID"
                    + customerCommunicationDTO.getCustomer_id()).log();
        }

        String partyID = null;
        try {
        	BackendIdentifierDTO identifierDTO = new BackendIdentifierDTO() ;
            if (dbxResult.getResponse() != null) {
                identifierDTO = (BackendIdentifierDTO) dbxResult.getResponse();
                partyID = identifierDTO.getBackendId();
            } else
                partyID = customerCommunicationDTO.getCustomer_id();
			if (StringUtils.isNotBlank(identifierDTO.getCompanyLegalUnit())) {
				headerMap.put("companyId", identifierDTO.getCompanyLegalUnit());
			}
            String partyURL = URLFinder.getServerRuntimeProperty(URLConstants.PARTY_HOST_URL) +
                    PartyURLFinder.getServiceUrl(PartyPropertyConstants.PARTY_COMMUNICATION,
                            partyID);

            dbxResult = new DBXResult();
            PartyUtils.addJWTAuthHeader(headerMap, AuthConstants.PRE_LOGIN_FLOW);
            alert.prepareError("headerMapparty"+headerMap).log();
            DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET,
                    partyURL, null, headerMap);

            JsonObject jsonObject = new JsonParser().parse((String) response.getResponse()).getAsJsonObject();

            JsonArray communication = new JsonArray();
            JsonObject obj = new JsonObject();
            if (jsonObject.has(DTOConstants.PARTY_ADDRESS)
                    && !jsonObject.get(DTOConstants.PARTY_ADDRESS).isJsonNull()) {
                List<PartyAddress> contactPoints =
                        PartyAddress.loadFromJsonArray(jsonObject.get(DTOConstants.PARTY_ADDRESS).getAsJsonArray());
                for (PartyAddress contactPoint : contactPoints) {
                    if (contactPoint.getCommunicationNature().equals(DTOConstants.PHONE)
                            && Boolean.parseBoolean(contactPoint.getPrimary())) {
                        obj = new JsonObject();
                        obj.addProperty(DTOConstants.PHONE,
                                contactPoint.getPhoneNo());
                        obj.addProperty(DTOConstants.PHONECOUNTRYCODE,
                                contactPoint.getIddPrefixPhone());
                        obj.addProperty(InfinityConstants.Value,
                                StringUtils.isNotBlank(contactPoint.getIddPrefixPhone()) ? contactPoint.getIddPrefixPhone() +"-"+contactPoint.getPhoneNo() : contactPoint.getPhoneNo());
                        obj.addProperty("Type_id", DBPUtilitiesConstants.COMM_TYPE_PHONE);
                        communication.add(obj);
                    } else if (contactPoint.getCommunicationNature().equals(DTOConstants.ELECTRONIC) &&
                            contactPoint.getCommunicationType().equals(DTOConstants.EMAIL)
                            && Boolean.parseBoolean(contactPoint.getPrimary())) {
                        obj = new JsonObject();
                        obj.addProperty(DTOConstants.EMAIL,
                                contactPoint.getElectronicAddress());
                        obj.addProperty(InfinityConstants.Value,
                                contactPoint.getElectronicAddress());
                        obj.addProperty("Type_id", DBPUtilitiesConstants.COMM_TYPE_EMAIL);
                        communication.add(obj);
                    }
                }
            }
            result.add(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION, communication);
            dbxResult.setResponse(result);
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching the Party communication details", e).log();
            return dbxResult;
        }
        return dbxResult;
    }

}
