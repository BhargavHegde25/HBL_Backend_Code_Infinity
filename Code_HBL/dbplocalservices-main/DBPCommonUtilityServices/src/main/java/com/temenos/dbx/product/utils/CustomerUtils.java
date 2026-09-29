package com.temenos.dbx.product.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.util.BundleConfigurationHandler;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.dto.AddressDTO;
import com.temenos.dbx.product.dto.CustomerAddressDTO;
import com.temenos.dbx.product.dto.CustomerCommunicationDTO;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.CustomerEmploymentDetailsDTO;
import com.temenos.dbx.product.dto.CustomerFlagStatus;

public class CustomerUtils {

    private static Alert alert;

    private static void initLOG() {
        if (alert == null) {
            alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
        }
    }

    public static CustomerDTO buildCustomerDTO(String customerID, Map<String, String> inputParamMap,
            DataControllerRequest dcRequest) {
        initLOG();

        CustomerDTO customerDTO = new CustomerDTO();

        if (StringUtils.isNotBlank(customerID)) {
            customerDTO = (CustomerDTO) DTOUtils.buildDTOFromDatabase(customerDTO, customerID, true);
        } else
            customerID = HelperMethods.generateUniqueCustomerId(dcRequest);

        if (customerDTO == null) {
            customerDTO = new CustomerDTO();
            customerDTO.setId(customerID);
            customerDTO.setIsNew(true);
        }

        customerDTO.setId(customerID);

        if (StringUtils.isBlank(customerDTO.getUserName())) {
            Map<String, String> bundleConfigurations = BundleConfigurationHandler
                    .fetchBundleConfigurations(BundleConfigurationHandler.BUDLENAME_C360, dcRequest);
            customerDTO.setUserName(HelperMethods.getUniqueNumericString(
                    Integer.valueOf(bundleConfigurations.get(BundleConfigurationHandler.USERNAME_LENGTH))));
        }

        Field[] fields = CustomerDTO.class.getDeclaredFields();
        Map<String, String> map = DTOMappings.getDTOObjectPropertyMappings(CustomerDTO.class);

        String personalInformation = inputParamMap.get(DTOConstants.PERSONAL_INFORMATION);
        String contactInformation = inputParamMap.get(DTOConstants.CONTACT_INFORMATION);
        String legalEntityId = inputParamMap.get(DTOConstants.IP_LEGALENTITYID);
        if (StringUtils.isBlank(personalInformation)) {
            personalInformation = dcRequest.getParameter(DTOConstants.PERSONAL_INFORMATION);
        }
        if (StringUtils.isBlank(contactInformation)) {
            contactInformation = dcRequest.getParameter(DTOConstants.CONTACT_INFORMATION);
        }
        if (StringUtils.isBlank(legalEntityId)) {
        	legalEntityId = dcRequest.getParameter(DTOConstants.IP_LEGALENTITYID);
        }
        if (StringUtils.isBlank(legalEntityId)) {
        	legalEntityId = inputParamMap.get(DTOConstants.COMPANYID);
        }
        if (StringUtils.isBlank(legalEntityId)) {
        	legalEntityId = dcRequest.getParameter(DTOConstants.COMPANYID);
        }
        customerDTO.setCompanyLegalUnit(legalEntityId);

        inputParamMap.putAll(HelperMethods.getRecordMap(personalInformation));
        inputParamMap.putAll(HelperMethods.getRecordMap(contactInformation));

        String fieldName;
        Field field;
        for (int i = 0; i < fields.length; i++) {
            field = fields[i];
            field.setAccessible(true);
            fieldName = map.get(field.getName());
            try {
                if (inputParamMap.containsKey(fieldName) && inputParamMap.get(fieldName) != null
                        && StringUtils.isNotBlank(inputParamMap.get(fieldName))) {
                    field.set(customerDTO, inputParamMap.get(fieldName));
                    customerDTO.setIsChanged(true);
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                alert.prepareError("Caught exception while Building customerDTO: ", e).log();
            }
            field.setAccessible(false);
        }
        if (StringUtils.isNotBlank(inputParamMap.get("firstName"))) {
            customerDTO.setFirstName(inputParamMap.get("firstName"));
        }

        if (StringUtils.isNotBlank(inputParamMap.get("middleName"))) {
            customerDTO.setMiddleName(inputParamMap.get("middleName"));
        }
        
        if (StringUtils.isNotBlank(inputParamMap.get("gender"))) {
            customerDTO.setGender(inputParamMap.get("gender"));
        }
        
        if (StringUtils.isNotBlank(inputParamMap.get("title"))) {
            customerDTO.setTitle(inputParamMap.get("title"));
        }

        if (StringUtils.isNotBlank(inputParamMap.get("lastName"))) {
            customerDTO.setLastName(inputParamMap.get("lastName"));
        }

        if (StringUtils.isNotBlank(inputParamMap.get("password"))) {
            customerDTO.setPassword(inputParamMap.get("password"));
        }
        
        if (StringUtils.isNotBlank(inputParamMap.get("dateofBirth"))) {
            customerDTO.setDateOfBirth(inputParamMap.get("dateofBirth"));
        }
        if (StringUtils.isNotBlank(inputParamMap.get("dateOfBirth"))) {
            customerDTO.setDateOfBirth(inputParamMap.get("dateOfBirth"));
        }
        if (StringUtils.isNotBlank(inputParamMap.get("street"))) {
            customerDTO.setStreet(inputParamMap.get("street"));
        }

        if (StringUtils.isNotBlank(inputParamMap.get("SSN"))) {
            customerDTO.setSsn(inputParamMap.get("SSN"));
        }
        if (StringUtils.isNotBlank(inputParamMap.get("applicationId"))) {
            customerDTO.setApplicationID(inputParamMap.get("applicationId"));
        }

        if (StringUtils.isNotBlank(inputParamMap.get("emailAddress"))) {
            inputParamMap.put(DTOConstants.EMAIL, inputParamMap.get("emailAddress"));
        }
        if ((StringUtils.isNotBlank(inputParamMap.get("phoneNumber")))
                && inputParamMap.containsKey("source") && StringUtils.isNotBlank(inputParamMap.get("source")) && !(inputParamMap.get("source").toString().contentEquals("UserManagement"))) {
            inputParamMap.put(DTOConstants.PHONE, inputParamMap.get("phoneNumber"));
        }

        String addressInformation = inputParamMap.get(DTOConstants.ADDRESS_INFORMATION);
        if (StringUtils.isBlank(addressInformation)) {
            addressInformation = dcRequest.getParameter(DTOConstants.ADDRESS_INFORMATION);
        }

        if (StringUtils.isNotBlank(addressInformation)) {
            try {
                JsonObject jsonObject = new JsonParser().parse(addressInformation).getAsJsonObject();
                AddressDTO addressDTO = new AddressDTO();
                CustomerAddressDTO customerAddressDTO = new CustomerAddressDTO();
                customerAddressDTO.setAddressDTO(addressDTO);
                addressDTO.setId(HelperMethods.getNewId());
                customerAddressDTO.setAddress_id(addressDTO.getId());
                customerAddressDTO.setCustomer_id(customerDTO.getId());
                customerAddressDTO.setIsNew(true);
                customerAddressDTO.setCompanyLegalUnit(legalEntityId);
                addressDTO.setIsNew(true);
                if (jsonObject.has("addressLine1") && !jsonObject.get("addressLine1").isJsonNull()) {
                    addressDTO.setAddressLine1(jsonObject.get("addressLine1").getAsString());
                }
                if (jsonObject.has("addressLine2") && !jsonObject.get("addressLine2").isJsonNull()) {
                    addressDTO.setAddressLine1(jsonObject.get("addressLine2").getAsString());
                }
                if (jsonObject.has("city") && !jsonObject.get("city").isJsonNull()) {
                    addressDTO.setCityName(jsonObject.get("city").getAsString());
                }
                if (jsonObject.has("state") && !jsonObject.get("state").isJsonNull()) {
                    addressDTO.setState(jsonObject.get("state").getAsString());
                }
                if (jsonObject.has("zipcode") && !jsonObject.get("zipcode").isJsonNull()) {
                    addressDTO.setZipCode(jsonObject.get("zipcode").getAsString());
                }
                if (jsonObject.has("country") && !jsonObject.get("country").isJsonNull()) {
                    addressDTO.setCountry(jsonObject.get("country").getAsString());
                }
                customerDTO.setCustomerAddress(customerAddressDTO);
            } catch (Exception e) {
                alert.prepareError("exception while reading address Information ", e).log();
            }
        }

        String identityInformation = inputParamMap.get(DTOConstants.IDENTITY_INFORMATION);
        if (StringUtils.isBlank(identityInformation)) {
            identityInformation = dcRequest.getParameter(DTOConstants.IDENTITY_INFORMATION);
        }

        if (StringUtils.isNotBlank(identityInformation)) {

            try {
                JsonObject jsonObject = new JsonParser().parse(identityInformation).getAsJsonObject();
                if (jsonObject.has("idType") && !jsonObject.get("idType").isJsonNull()) {
                    customerDTO.setiDType_id(jsonObject.get("idType").getAsString());
                }
                if (jsonObject.has("idValue") && !jsonObject.get("idValue").isJsonNull()) {
                    customerDTO.setiDValue(jsonObject.get("idValue").getAsString());
                }
                if (jsonObject.has("idState") && !jsonObject.get("idState").isJsonNull()) {
                    customerDTO.setiDState(jsonObject.get("idState").getAsString());
                }
                if (jsonObject.has("issueDate") && !jsonObject.get("issueDate").isJsonNull()) {
                    customerDTO.setiDIssueDate(jsonObject.get("issueDate").getAsString());
                }
                if (jsonObject.has("expiryDate") && !jsonObject.get("expiryDate").isJsonNull()) {
                    customerDTO.setiDExpiryDate(jsonObject.get("expiryDate").getAsString());
                }
            } catch (Exception e) {
                alert.prepareError("exception while reading Identity Information ", e).log();
            }
        }

        List<CustomerCommunicationDTO> communicationList = customerDTO.getCustomerCommuncation();

        if (inputParamMap.containsKey(DTOConstants.PHONE)
                && StringUtils.isNotBlank(inputParamMap.get(DTOConstants.PHONE))) {
            CustomerCommunicationDTO communicationDTO = new CustomerCommunicationDTO();
            communicationDTO.setCustomer_id(customerDTO.getId());
            communicationDTO.setId("CUS_" + HelperMethods.getNumericId());
            communicationDTO.setType_id(HelperMethods.getCommunicationTypes().get(DTOConstants.PHONE));
            communicationDTO.setIsPrimary(true);
            communicationDTO.setExtension(DBPUtilitiesConstants.CUSTOMER_COMMUNICATION_PHONE_DEFAULT_EXTENSION);
            communicationDTO.setCreatedts(HelperMethods.getCurrentDate());
            if (StringUtils.isNotBlank(inputParamMap.get("PhoneNumberId"))) {
                communicationDTO.setId(inputParamMap.get("PhoneNumberId"));
            }

            String phone = inputParamMap.get(DTOConstants.PHONE);
            String extenstion = "1";

            if (phone.contains("-")) {
                extenstion = phone.substring(0, phone.indexOf('-'));
            }

            boolean isNew = true;
            if (communicationList != null) {
                for (int i = 0; i < communicationList.size(); i++) {
                    if (communicationList.get(i).getId().equals(communicationDTO.getId())) {
                        communicationDTO = communicationList.get(i);
                        isNew = false;
                        break;
                    } else if (communicationList.get(i).getType_id()
                            .equals(HelperMethods.getCommunicationTypes().get(DTOConstants.PHONE))) {
                        communicationDTO = communicationList.get(i);
                        isNew = false;
                    }
                }
            }

            communicationDTO.setValue(phone);
            communicationDTO.setPhoneCountryCode(extenstion);
            communicationDTO.setIsChanged(true);
            communicationDTO.setIsNew(isNew);
            communicationDTO.setCompanyLegalUnit(legalEntityId);
            if (isNew) {
                customerDTO.setCustomerCommuncation(communicationDTO);
            }
        }
        
        if (inputParamMap.containsKey(InfinityConstants.phoneNumber)
                && StringUtils.isNotBlank(inputParamMap.get(InfinityConstants.phoneNumber))) {
            CustomerCommunicationDTO communicationDTO = new CustomerCommunicationDTO();
            communicationDTO.setCustomer_id(customerDTO.getId());
            communicationDTO.setId("CUS_" + HelperMethods.getNumericId());
            communicationDTO.setType_id(HelperMethods.getCommunicationTypes().get(DTOConstants.PHONE));
            communicationDTO.setIsPrimary(true);
            communicationDTO.setExtension(DBPUtilitiesConstants.CUSTOMER_COMMUNICATION_PHONE_DEFAULT_EXTENSION);
            communicationDTO.setCreatedts(HelperMethods.getCurrentDate());
            communicationDTO.setPhoneCountryCode(inputParamMap.get(InfinityConstants.phoneCountryCode));

            String phone = inputParamMap.get(InfinityConstants.phoneNumber);
            String extenstion = "1";

            if (phone.contains("-")) {
                extenstion = phone.substring(0, phone.indexOf('-'));
            }
            else {
                phone = communicationDTO.getPhoneCountryCode() +"-"+phone;
                extenstion = inputParamMap.get(InfinityConstants.phoneCountryCode);
            }
            boolean isNew = true;
            if (communicationList != null) {
                for (int i = 0; i < communicationList.size(); i++) {
                    if (communicationList.get(i).getId().equals(communicationDTO.getId())) {
                        communicationDTO = communicationList.get(i);
                        isNew = false;
                        break;
                    } else if (communicationList.get(i).getType_id()
                            .equals(HelperMethods.getCommunicationTypes().get(DTOConstants.PHONE))) {
                        communicationDTO = communicationList.get(i);
                        isNew = false;
                    }
                }
            }

            communicationDTO.setValue(phone);
            communicationDTO.setPhoneCountryCode(extenstion);
            communicationDTO.setIsChanged(true);
            communicationDTO.setIsNew(isNew);
            communicationDTO.setCompanyLegalUnit(legalEntityId);
            if (isNew) {
                customerDTO.setCustomerCommuncation(communicationDTO);
            }
        }

        if (inputParamMap.containsKey(DTOConstants.EMAIL)
                && StringUtils.isNotBlank(inputParamMap.get(DTOConstants.EMAIL))) {

            CustomerCommunicationDTO communicationDTO = new CustomerCommunicationDTO();
            communicationDTO.setCustomer_id(customerDTO.getId());
            communicationDTO.setId("CUS_" + HelperMethods.getNumericId());
            communicationDTO.setIsPrimary(true);
            communicationDTO.setCreatedts(HelperMethods.getCurrentDate());
            communicationDTO.setType_id(HelperMethods.getCommunicationTypes().get(DTOConstants.EMAIL));

            if (StringUtils.isNotBlank(inputParamMap.get("ElectronicId"))) {
                communicationDTO.setId(inputParamMap.get("ElectronicId"));
            }

            boolean isNew = true;
            if (communicationList != null) {
                for (int i = 0; i < communicationList.size(); i++) {
                    if (communicationList.get(i).getId().equals(communicationDTO.getId())) {
                        communicationDTO = communicationList.get(i);
                        isNew = false;
                        break;
                    } else if (communicationList.get(i).getType_id()
                            .equals(HelperMethods.getCommunicationTypes().get(DTOConstants.EMAIL))) {
                        communicationDTO = communicationList.get(i);
                        isNew = false;
                    }
                }
            }

            communicationDTO.setValue(inputParamMap.get(DTOConstants.EMAIL));
            communicationDTO.setIsNew(isNew);
            communicationDTO.setIsChanged(true);
            communicationDTO.setCompanyLegalUnit(legalEntityId);
            if (isNew) {
                customerDTO.setCustomerCommuncation(communicationDTO);
            }
        }
        
        if (inputParamMap.containsKey(InfinityConstants.email)
                && StringUtils.isNotBlank(inputParamMap.get(InfinityConstants.email))) {

            CustomerCommunicationDTO communicationDTO = new CustomerCommunicationDTO();
            communicationDTO.setCustomer_id(customerDTO.getId());
            communicationDTO.setId("CUS_" + HelperMethods.getNumericId());
            communicationDTO.setIsPrimary(true);
            communicationDTO.setCreatedts(HelperMethods.getCurrentDate());
            communicationDTO.setType_id(HelperMethods.getCommunicationTypes().get(DTOConstants.EMAIL));

            boolean isNew = true;
            if (communicationList != null) {
                for (int i = 0; i < communicationList.size(); i++) {
                    if (communicationList.get(i).getId().equals(communicationDTO.getId())) {
                        communicationDTO = communicationList.get(i);
                        isNew = false;
                        break;
                    } else if (communicationList.get(i).getType_id()
                            .equals(HelperMethods.getCommunicationTypes().get(DTOConstants.EMAIL))) {
                        communicationDTO = communicationList.get(i);
                        isNew = false;
                    }
                }
            }

            communicationDTO.setValue(inputParamMap.get(InfinityConstants.email));
            communicationDTO.setIsNew(isNew);
            communicationDTO.setIsChanged(true);
            communicationDTO.setCompanyLegalUnit(legalEntityId);
            if (isNew) {
                customerDTO.setCustomerCommuncation(communicationDTO);
            }
        }

        List<CustomerEmploymentDetailsDTO> customerEmploymentDetailsDTOs = customerDTO.getEmploymentDetails();

        if (inputParamMap.containsKey("EmployementStatus_id")
                && StringUtils.isNotBlank(inputParamMap.get("EmployementStatus_id"))) {
            CustomerEmploymentDetailsDTO employmentDetailsDTO = new CustomerEmploymentDetailsDTO();
            employmentDetailsDTO.setCustomer_id(customerID);
            employmentDetailsDTO.setId(HelperMethods.getNewId());
            employmentDetailsDTO.setCreatedts(HelperMethods.getCurrentDate());
            if (StringUtils.isNotBlank(inputParamMap.get("EmploymentTypeId"))) {
                employmentDetailsDTO.setId(inputParamMap.get("EmploymentTypeId"));
            }

            boolean isNew = true;
            if (customerEmploymentDetailsDTOs != null) {
                for (int i = 0; i < customerEmploymentDetailsDTOs.size(); i++) {
                    if (customerEmploymentDetailsDTOs.get(i).getId().equals(employmentDetailsDTO.getId())) {
                        employmentDetailsDTO = customerEmploymentDetailsDTOs.get(i);
                        isNew = false;
                        break;
                    }
                }

                if (customerEmploymentDetailsDTOs.size() == 1) {
                    employmentDetailsDTO = customerEmploymentDetailsDTOs.get(0);
                    isNew = false;
                }

            }
            employmentDetailsDTO.setEmploymentType(inputParamMap.get("EmployementStatus_id"));
            employmentDetailsDTO.setIsChanged(true);
            employmentDetailsDTO.setIsNew(isNew);
            if (isNew) {
                customerDTO.setEmploymentDetails(employmentDetailsDTO);
            }
        }

        List<CustomerAddressDTO> customerAddressDTOs = customerDTO.getCustomerAddress();

        if (StringUtils.isNotBlank(inputParamMap.get(DTOConstants.ADDRESS))
                && StringUtils.isNotBlank(inputParamMap.get(DTOConstants.CUSTOMER_ADDRESS))) {

            String addresses = inputParamMap.get(DTOConstants.ADDRESS);
            String customerAddresses = inputParamMap.get(DTOConstants.CUSTOMER_ADDRESS);
            JsonArray addressarray = new JsonParser().parse(addresses).getAsJsonArray();
            JsonArray customerAddressarray = new JsonParser().parse(customerAddresses).getAsJsonArray();
            int i = 0;

            while (i < addressarray.size() && i < customerAddressarray.size()) {
                JsonObject jsonObject = addressarray.get(i).getAsJsonObject();
                AddressDTO addressDTO =
                        (AddressDTO) DTOUtils.loadJsonObjectIntoObject(jsonObject, AddressDTO.class, true);
                if (addressDTO != null && StringUtils.isBlank(addressDTO.getId())) {
                    addressDTO.setId(HelperMethods.getNewId());
                }
                jsonObject = customerAddressarray.get(i).getAsJsonObject();
                CustomerAddressDTO customerAddressDTO = (CustomerAddressDTO) DTOUtils
                        .loadJsonObjectIntoObject(jsonObject, CustomerAddressDTO.class, true);
                if (customerAddressDTO != null) {
                	customerAddressDTO.setCompanyLegalUnit(legalEntityId);
                    customerAddressDTO.setAddressDTO(addressDTO);
                    customerAddressDTO.setAddress_id(addressDTO.getId());
                    customerAddressDTO.setCustomer_id(customerID);
                    customerAddressDTO.setCreatedts(HelperMethods.getCurrentDate());
                    customerAddressDTO.setIsChanged(true);
                    customerAddressDTO.getAddressDTO().setIsChanged(true);
                    boolean isNew = true;
                    if (customerAddressDTOs != null) {
                        for (int j = 0; j < customerAddressDTOs.size(); j++) {
                            if (customerAddressDTOs.get(j).getAddress_id().equals(customerAddressDTO.getAddress_id())) {
                                DTOUtils.copy(customerAddressDTOs.get(j).getAddressDTO(),
                                        customerAddressDTO.getAddressDTO());
                                addressDTO.setId(customerAddressDTO.getAddress_id());
                                isNew = false;
                                break;
                            }
                        }
                    }

                    customerAddressDTO.setIsNew(isNew);
                    addressDTO.setIsNew(isNew);

                    if (isNew) {
                        customerDTO.setCustomerAddress(customerAddressDTO);
                    }
                }
            }

        } else if (StringUtils.isNotBlank(inputParamMap.get(DTOConstants.CUSTOMER_ADDRESS))) {

            CustomerAddressDTO customerAddressDTO = new CustomerAddressDTO();
            String customerAddress = inputParamMap.get(DTOConstants.CUSTOMER_ADDRESS);
            JsonArray array = new JsonParser().parse(customerAddress).getAsJsonArray();
            for (int i = 0; i < array.size(); i++) {
                JsonObject jsonObject = array.get(i).getAsJsonObject();
                customerAddressDTO = new CustomerAddressDTO();
                customerAddressDTO = (CustomerAddressDTO) DTOUtils.loadJsonObjectIntoObject(jsonObject,
                        CustomerAddressDTO.class, true);

                if (customerAddressDTO != null) {
                    customerAddressDTO.setCustomer_id(customerID);
                    boolean isNew = true;
                    if (customerAddressDTOs != null) {
                        for (int j = 0; j < customerAddressDTOs.size(); j++) {
                            if (customerAddressDTOs.get(j).getAddress_id().equals(customerAddressDTO.getAddress_id())) {
                                DTOUtils.copy(customerAddressDTOs.get(j).getAddressDTO(),
                                        customerAddressDTO.getAddressDTO());
                                isNew = false;
                                break;
                            }
                        }
                    }
                    customerAddressDTO.setIsNew(isNew);
                    customerAddressDTO.getAddressDTO().setIsNew(isNew);

                    if (isNew) {
                        customerDTO.setCustomerAddress(customerAddressDTO);
                    }
                }
            }
        } else if (StringUtils.isNotBlank(inputParamMap.get(DTOConstants.ADDRESS))) {

            String customerAddress = inputParamMap.get(DTOConstants.ADDRESS);

            JsonArray array = new JsonParser().parse(customerAddress).getAsJsonArray();
            CustomerAddressDTO customerAddressDTO;
            AddressDTO addressDTO;
            JsonObject jsonObject;
            for (int i = 0; i < array.size(); i++) {
                jsonObject = array.get(i).getAsJsonObject();
                customerAddressDTO = new CustomerAddressDTO();
                addressDTO = (AddressDTO) DTOUtils.loadJsonObjectIntoObject(jsonObject, AddressDTO.class, true);
                if (addressDTO != null && addressDTO.getId() == null) {
                    addressDTO.setId(HelperMethods.getNewId());
                    customerAddressDTO.setCompanyLegalUnit(legalEntityId);
                    customerAddressDTO.setCustomer_id(customerID);
                    customerAddressDTO.setAddress_id(addressDTO.getId());
                    customerAddressDTO.setAddressDTO(addressDTO);
                    customerAddressDTO.setIsChanged(true);
                    addressDTO.setIsChanged(true);
                    boolean isNew = true;
                    if (customerAddressDTOs != null) {
                        for (int j = 0; j < customerAddressDTOs.size(); j++) {
                            if (customerAddressDTOs.get(j).getAddress_id().equals(customerAddressDTO.getAddress_id())) {
                                DTOUtils.copy(customerAddressDTOs.get(j).getAddressDTO(),
                                        customerAddressDTO.getAddressDTO());
                                addressDTO.setId(customerAddressDTO.getAddress_id());
                                isNew = false;
                                break;
                            }
                        }
                    }
                    customerAddressDTO.setIsNew(isNew);
                    addressDTO.setIsNew(isNew);
                    if (isNew) {
                        customerDTO.setCustomerAddress(customerAddressDTO);
                    }
                }
            }
        } else if (StringUtils.isNotBlank(inputParamMap.get(DTOConstants.ADDRESSES))
                || StringUtils.isNotBlank(inputParamMap.get("addresses"))) {

            String customerAddress = inputParamMap.get(DTOConstants.ADDRESSES);

            if (StringUtils.isBlank(customerAddress)) {
                customerAddress = inputParamMap.get("addresses");
            }

            JsonArray array = new JsonParser().parse(customerAddress).getAsJsonArray();

            CustomerAddressDTO customerAddressDTO;
            AddressDTO addressDTO;
            JsonObject jsonObject;
            for (int i = 0; i < array.size(); i++) {
                jsonObject = array.get(i).getAsJsonObject();
                customerAddressDTO = new CustomerAddressDTO();
                addressDTO = new AddressDTO();

                if (jsonObject.has("Addr_type") && !jsonObject.get("Addr_type").isJsonNull()) {
                    customerAddressDTO.setType_id(jsonObject.get("Addr_type").getAsString());
                }
                if (jsonObject.has("isPrimary") && !jsonObject.get("isPrimary").isJsonNull()) {
                    customerAddressDTO.setIsPrimary("1".equals(jsonObject.get("isPrimary").getAsString()));
                }
                if (jsonObject.has("Addr_id") && !jsonObject.get("Addr_id").isJsonNull()) {
                    customerAddressDTO.setAddress_id(jsonObject.get("Addr_id").getAsString());
                }
                if (jsonObject.has("ZipCode") && !jsonObject.get("ZipCode").isJsonNull()) {
                    addressDTO.setZipCode(jsonObject.get("ZipCode").getAsString());
                }
                if (jsonObject.has("addrLine1") && !jsonObject.get("addrLine1").isJsonNull()) {
                    addressDTO.setAddressLine1(jsonObject.get("addrLine1").getAsString());
                }
                if (jsonObject.has("addrLine2") && !jsonObject.get("addrLine2").isJsonNull()) {
                    addressDTO.setAddressLine2(jsonObject.get("addrLine2").getAsString());
                }
                if (jsonObject.has("City_id") && !jsonObject.get("City_id").isJsonNull()) {
                    addressDTO.setCity_id(jsonObject.get("City_id").getAsString());
                }
                if (jsonObject.has("CityName") && !jsonObject.get("CityName").isJsonNull()) {
                    addressDTO.setCityName(jsonObject.get("CityName").getAsString());
                }

                if (jsonObject.has("isTypeBusiness") && !jsonObject.get("isTypeBusiness").isJsonNull()
                        && ("1".equals(jsonObject.get("isTypeBusiness").getAsString())
                                || Boolean.parseBoolean(jsonObject.get("isTypeBusiness").getAsString()))) {
                    customerAddressDTO.setIsTypeBusiness("1");
                }

                if (jsonObject.has("Region_id") && !jsonObject.get("Region_id").isJsonNull()) {
                    addressDTO.setRegion_id(jsonObject.get("Region_id").getAsString());
                }
                if (jsonObject.has("countryCode") && !jsonObject.get("countryCode").isJsonNull()) {
                    addressDTO.setCountry(jsonObject.get("countryCode").getAsString());
                }

                customerAddressDTO.setCustomer_id(customerID);
                customerAddressDTO.setCompanyLegalUnit(legalEntityId);
                customerAddressDTO.setAddressDTO(addressDTO);
                boolean isNew = true;
                if (customerAddressDTOs != null && customerAddressDTO.getAddress_id() != null) {
                    for (int j = 0; j < customerAddressDTOs.size(); j++) {
                        if (customerAddressDTOs.get(j).getAddress_id().equals(customerAddressDTO.getAddress_id())) {
                            DTOUtils.copy(customerAddressDTOs.get(j).getAddressDTO(),
                                    customerAddressDTO.getAddressDTO());
                            DTOUtils.copy(customerAddressDTOs.get(j),
                                    customerAddressDTO);
                            addressDTO = customerAddressDTOs.get(j).getAddressDTO();
                            customerAddressDTO = customerAddressDTOs.get(j);
                            addressDTO.setId(customerAddressDTO.getAddress_id());
                            isNew = false;
                            break;
                        }
                    }
                }

                if (customerAddressDTO.getIsPrimary()) {
                    removePrimary(customerAddressDTOs);
                    customerAddressDTO.setIsPrimary(true);
                }
                customerAddressDTO.setIsNew(isNew);
                addressDTO.setIsNew(isNew);
                customerAddressDTO.setIsChanged(true);
                addressDTO.setIsChanged(true);

                if (isNew) {
                    if (StringUtils.isBlank(customerAddressDTO.getAddress_id())) {
                        customerAddressDTO.setAddress_id(HelperMethods.getNewId());
                    }

                    addressDTO.setId(customerAddressDTO.getAddress_id());
                    customerDTO.setCustomerAddress(customerAddressDTO);
                }
            }
        }

        if (StringUtils.isNotBlank(inputParamMap.get(DTOConstants.EMPLYMNET_DETAILS))) {

            String emplymentDetails = inputParamMap.get(DTOConstants.EMPLYMNET_DETAILS);

            JsonArray array = new JsonParser().parse(emplymentDetails).getAsJsonArray();

            CustomerEmploymentDetailsDTO employmentDetailsDTO;

            JsonObject jsonObject;
            for (int i = 0; i < array.size(); i++) {
                jsonObject = array.get(i).getAsJsonObject();

                employmentDetailsDTO = (CustomerEmploymentDetailsDTO) DTOUtils.loadJsonObjectIntoObject(jsonObject,
                        CustomerEmploymentDetailsDTO.class, true);

                if (employmentDetailsDTO != null && employmentDetailsDTO.getId() == null) {
                    employmentDetailsDTO.setId(HelperMethods.getNewId());
                    employmentDetailsDTO.setCustomer_id(customerID);
                    employmentDetailsDTO.setCreatedts(HelperMethods.getCurrentDate());

                    boolean isNew = true;
                    if (customerEmploymentDetailsDTOs != null) {
                        for (int j = 0; j < customerEmploymentDetailsDTOs.size(); j++) {
                            if (customerEmploymentDetailsDTOs.get(j).getId().equals(employmentDetailsDTO.getId())) {
                                employmentDetailsDTO = customerEmploymentDetailsDTOs.get(j);
                                isNew = false;
                                break;
                            }
                        }
                    }

                    employmentDetailsDTO.setEmploymentType(inputParamMap.get("EmployementStatus_id"));
                    employmentDetailsDTO.setIsChanged(true);
                    employmentDetailsDTO.setIsNew(isNew);

                    if (isNew) {
                        customerDTO.setEmploymentDetails(employmentDetailsDTO);
                    }
                }
            }
        }

        String maritalStauts = inputParamMap.get("MaritalStatus_id");

        if (StringUtils.isBlank(maritalStauts)) {
            maritalStauts = dcRequest.getParameter("MaritalStatus_id");
        }

        if (StringUtils.isNotBlank(maritalStauts) && !maritalStauts.equals(customerDTO.getMaritalStatus_id())) {
            customerDTO.setIsChanged(true);
            customerDTO.setMaritalStatus_id(maritalStauts);
        }

        String salutation = inputParamMap.get("Salutation");

        if (StringUtils.isBlank(salutation)) {
            salutation = dcRequest.getParameter("Salutation");
        }

        if (StringUtils.isNotBlank(salutation) && !salutation.equals(customerDTO.getSalutation())) {
            customerDTO.setIsChanged(true);
            customerDTO.setSalutation(salutation);
        }

        String eagreementStatus = inputParamMap.get("eagreementStatus");

        if (StringUtils.isBlank(eagreementStatus)) {
            eagreementStatus = dcRequest.getParameter("eagreementStatus");
        }

        if (StringUtils.isNotBlank(eagreementStatus)) {
            if ("1".equals(eagreementStatus)) {
                customerDTO.setIsEagreementSigned(true);
            } else {
                customerDTO.setIsEagreementSigned(false);
            }

            customerDTO.setIsChanged(true);
        }

        String preferredContactMethod = inputParamMap.get("preferredContactMethod");

        if (StringUtils.isBlank(preferredContactMethod)) {
            preferredContactMethod = dcRequest.getParameter("preferredContactMethod");
        }

        if (StringUtils.isNotBlank(preferredContactMethod)) {
            customerDTO.setPreferredContactMethod(preferredContactMethod);
            customerDTO.setIsChanged(true);
        }

        String preferredContactTime = inputParamMap.get("preferredContactTime");

        if (StringUtils.isBlank(preferredContactTime)) {
            preferredContactTime = dcRequest.getParameter("preferredContactTime");
        }

        if (StringUtils.isNotBlank(preferredContactTime)) {
            customerDTO.setPreferredContactTime(preferredContactTime);
            customerDTO.setIsChanged(true);
        }

        String listOfRemovedRisks = inputParamMap.get("listOfRemovedRisks");
        if (StringUtils.isBlank(listOfRemovedRisks)) {
            listOfRemovedRisks = dcRequest.getParameter("listOfRemovedRisks");
        }

        String listOfAddedRisks = inputParamMap.get("listOfAddedRisks");
        if (StringUtils.isBlank(listOfAddedRisks)) {
            listOfAddedRisks = dcRequest.getParameter("listOfAddedRisks");
        }

        JsonObject json = new JsonObject();
        if (StringUtils.isNotBlank(listOfAddedRisks)) {
            try {
                json.add("ListAddedRisk", new JsonParser().parse(listOfAddedRisks).getAsJsonArray());
            } catch (Exception e) {
            }
        }
        if (StringUtils.isNotBlank(listOfRemovedRisks)) {
            try {
                json.add("ListRemovedRisk", new JsonParser().parse(listOfRemovedRisks).getAsJsonArray());
            } catch (Exception e) {
            }
        }

        List<CustomerFlagStatus> customerFlagStatus = customerDTO.getCustomerFlagStatus();
        Set<String> list = HelperMethods.getRiskAcceptedValues();
        if (json.has("ListRemovedRisk") && json.get("ListRemovedRisk").isJsonArray()) {
            JsonArray jsonArray = json.getAsJsonArray("ListRemovedRisk");
            if (customerFlagStatus != null && customerFlagStatus.size() > 0 && jsonArray != null
                    && !jsonArray.isJsonNull()) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    try {
                        String riskValue = jsonArray.get(i).getAsString();
                        if (StringUtils.isNotBlank(riskValue) && list.contains(riskValue)) {
                            for (CustomerFlagStatus customerFlagStatus2 : customerFlagStatus) {
                                if (riskValue.equals(customerFlagStatus2.getStatus_id())) {
                                    customerFlagStatus2.setChanged(true);
                                }
                            }
                        }
                    } catch (Exception ex) {
                        ex.getMessage();
                    }
                }
            }

        }

        if (json.has("ListAddedRisk") && json.get("ListAddedRisk").isJsonArray()) {
            JsonArray jsonArray = json.getAsJsonArray("ListAddedRisk");
            if (jsonArray != null && !jsonArray.isJsonNull()) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    try {
                        String riskValue = jsonArray.get(i).getAsString();
                        if (StringUtils.isNotBlank(riskValue) && list.contains(riskValue)) {
                            CustomerFlagStatus customerFlagStatus2 = new CustomerFlagStatus();
                            customerFlagStatus2.setCustomer_id(customerID);
                            customerFlagStatus2.setStatus_id(riskValue);
                            customerFlagStatus2.setNew(true);
                            customerDTO.setCustomerFlagStatus(customerFlagStatus2);
                        }
                    } catch (Exception ex) {
                        ex.getMessage();
                    }
                }
            }

        }

        String phonenumbers = inputParamMap.get("PhoneNumbers");
        if (StringUtils.isBlank(phonenumbers)) {
            phonenumbers = dcRequest.getParameter("PhoneNumbers");
        }

        if (StringUtils.isBlank(phonenumbers)) {
            phonenumbers = dcRequest.getParameter("phoneNumbers");
        }
        if (StringUtils.isBlank(phonenumbers)) {
            phonenumbers = dcRequest.getParameter("phoneNumbers");
        }

        JsonArray phones = new JsonArray();
        if (StringUtils.isNotBlank(phonenumbers)) {
            try {
                phones = new JsonParser().parse(phonenumbers).getAsJsonArray();
            } catch (Exception e) {
            }
        }
        if (phones != null && phones.size() > 0) {
            for (int i = 0; i < phones.size(); i++) {
                JsonObject jsonObject = phones.get(i).getAsJsonObject();
                String phone = "";

                if (jsonObject.has("value") && !jsonObject.get("value").isJsonNull()) {
                    phone = jsonObject.get("value").getAsString();
                }

                if (jsonObject.has("phoneNumber") && !jsonObject.get("phoneNumber").isJsonNull()) {
                    phone = jsonObject.get("phoneNumber").getAsString();
                }

                String phoneCountryCode = "";

                if (jsonObject.has("phoneCountryCode") && !jsonObject.get("phoneCountryCode").isJsonNull()) {
                    phoneCountryCode = jsonObject.get("phoneCountryCode").getAsString();
                }

                boolean isPrimary = false;
                if (jsonObject.has("isPrimary") && !jsonObject.get("isPrimary").isJsonNull()) {
                    isPrimary = "1".equals(jsonObject.get("isPrimary").getAsString());
                }

                boolean isAlertsRequired = false;
                if (jsonObject.has("isAlertsRequired") && !jsonObject.get("isAlertsRequired").isJsonNull()) {
                    isAlertsRequired = "1".equals(jsonObject.get("isAlertsRequired").getAsString());
                }

                String extension = "";

                if (jsonObject.has("Extension") && !jsonObject.get("Extension").isJsonNull()) {
                    extension = jsonObject.get("Extension").getAsString();
                }

                CustomerCommunicationDTO communicationDTO = new CustomerCommunicationDTO();
                communicationDTO.setId("CUS_" + HelperMethods.getNumericId());
                communicationDTO.setCustomer_id(customerDTO.getId());

                if (jsonObject.has("id") && !jsonObject.get("id").isJsonNull()) {
                    String id = jsonObject.get("id").getAsString();
                    communicationDTO.setId(id);
                }

                String isBusinessType = "0";
                if (jsonObject.has("isTypeBusiness") && !jsonObject.get("isTypeBusiness").isJsonNull()
                        && ("1".equals(jsonObject.get("isTypeBusiness").getAsString())
                                || Boolean.parseBoolean(jsonObject.get("isTypeBusiness").getAsString()))) {
                    isBusinessType = "1";
                }

                if (StringUtils.isBlank(phoneCountryCode) || "null".equals(phoneCountryCode)) {
                    phoneCountryCode = "1";
                    if (phone.contains("-")) {
                        phoneCountryCode = phone.substring(0, phone.indexOf('-'));
                    } else {
                        phone = phoneCountryCode + "-" + phone;
                    }
                } else {
                    phone = phoneCountryCode + "-" + phone;
                }

                boolean isNew = true;
                if (communicationList != null && communicationDTO.getId() != null) {
                    for (int j = 0; j < communicationList.size(); j++) {
                        if (communicationList.get(j).getId().equals(communicationDTO.getId()) || communicationList.get(j).getValue().equals(communicationDTO.getValue())) {
                            communicationDTO = communicationList.get(j);
                            isNew = false;
                            break;
                        }
                    }
                }

                communicationDTO.setIsTypeBusiness(isBusinessType);
                communicationDTO.setType_id(HelperMethods.getCommunicationTypes().get(DTOConstants.PHONE));
                communicationDTO.setValue(phone);
                communicationDTO.setPhoneCountryCode(extension);
                communicationDTO.setExtension(extension);
                communicationDTO.setIsNew(isNew);
                communicationDTO.setIsChanged(true);
                communicationDTO.setCompanyLegalUnit(legalEntityId);
                if (isAlertsRequired) {
                    removeAlertRequired(customerDTO.getCustomerCommuncation(), communicationDTO.getType_id());
                }
                communicationDTO.setIsAlertsRequired(isAlertsRequired);

                if (isPrimary) {
                    removePrimary(customerDTO.getCustomerCommuncation(), communicationDTO.getType_id());
                }
                                
                addLegalUnit(customerDTO.getCustomerCommuncation(),legalEntityId);
                
                communicationDTO.setIsPrimary(isPrimary);

                if (isNew) {
                    customerDTO.setCustomerCommuncation(communicationDTO);
                }
            }
        }

        String emailIds = inputParamMap.get("EmailIds");
        if (StringUtils.isBlank(emailIds)) {
            emailIds = dcRequest.getParameter("EmailIds");
        }

        if (StringUtils.isBlank(emailIds)) {
            emailIds = inputParamMap.get("emailIds");
        }

        if (StringUtils.isBlank(emailIds)) {
            emailIds = dcRequest.getParameter("emailIds");
        }

        JsonArray emails = new JsonArray();
        if (StringUtils.isNotBlank(emailIds)) {
            try {
                emails = new JsonParser().parse(emailIds).getAsJsonArray();
            } catch (Exception e) {
            }
        }

        if (emails != null && emails.size() > 0) {
            for (int i = 0; i < emails.size(); i++) {
                JsonObject jsonObject = emails.get(i).getAsJsonObject();

                String email = "";

                if (jsonObject.has("value") && !jsonObject.get("value").isJsonNull()) {
                    email = jsonObject.get("value").getAsString();
                }

                if (jsonObject.has("email") && !jsonObject.get("email").isJsonNull()) {
                    email = jsonObject.get("email").getAsString();
                }

                boolean isPrimary = false;
                if (jsonObject.has("isPrimary") && !jsonObject.get("isPrimary").isJsonNull()) {
                    isPrimary = "1".equals(jsonObject.get("isPrimary").getAsString());
                }

                boolean isAlertsRequired = false;
                if (jsonObject.has("isAlertsRequired") && !jsonObject.get("isAlertsRequired").isJsonNull()) {
                    isAlertsRequired = "1".equals(jsonObject.get("isAlertsRequired").getAsString());
                }

                String extension = "";

                if (jsonObject.has("Extension") && !jsonObject.get("Extension").isJsonNull()) {
                    extension = jsonObject.get("Extension").getAsString();
                }

                CustomerCommunicationDTO communicationDTO = new CustomerCommunicationDTO();
                communicationDTO.setId("CUS_" + HelperMethods.getNumericId());
                communicationDTO.setCustomer_id(customerDTO.getId());

                if (jsonObject.has("id") && !jsonObject.get("id").isJsonNull()) {
                    String id = jsonObject.get("id").getAsString();
                    communicationDTO.setId(id);
                }

                String isTypeBusiness = "0";
                if (jsonObject.has("isTypeBusiness") && !jsonObject.get("isTypeBusiness").isJsonNull()
                        && ("1".equals(jsonObject.get("isTypeBusiness").getAsString())
                                || Boolean.parseBoolean(jsonObject.get("isTypeBusiness").getAsString()))) {
                    isTypeBusiness = "1";
                }

                boolean isNew = true;
                if (communicationList != null && communicationDTO.getId() != null) {
                    for (int j = 0; j < communicationList.size(); j++) {
                        if (communicationList.get(j).getId().equals(communicationDTO.getId()) || communicationList.get(j).getValue().equals(communicationDTO.getValue())) {
                            communicationDTO = communicationList.get(j);
                            isNew = false;
                            break;
                        }
                    }
                }

                communicationDTO.setType_id(HelperMethods.getCommunicationTypes().get(DTOConstants.EMAIL));
                communicationDTO.setIsTypeBusiness(isTypeBusiness);
                communicationDTO.setValue(email);
                communicationDTO.setIsNew(isNew);
                communicationDTO.setIsChanged(true);
                communicationDTO.setCompanyLegalUnit(legalEntityId);
                if (isPrimary) {
                    removePrimary(customerDTO.getCustomerCommuncation(), communicationDTO.getType_id());
                }
                if (isAlertsRequired) {
                    removeAlertRequired(customerDTO.getCustomerCommuncation(), communicationDTO.getType_id());
                }
                communicationDTO.setIsAlertsRequired(isAlertsRequired);
                communicationDTO.setExtension(extension);
                communicationDTO.setIsPrimary(isPrimary);
                addLegalUnit(customerDTO.getCustomerCommuncation(),legalEntityId);
                if (isNew) {

                    customerDTO.setCustomerCommuncation(communicationDTO);
                }
            }
        }

        String deleteAddressID = inputParamMap.get("deleteAddressID");
        if (StringUtils.isBlank(deleteAddressID)) {
            deleteAddressID = dcRequest.getParameter("deleteAddressID");
        }

        String deleteCommunicationID = inputParamMap.get("deleteCommunicationID");
        if (StringUtils.isBlank(deleteCommunicationID)) {
            deleteCommunicationID = dcRequest.getParameter("deleteCommunicationID");
        }

        List<CustomerCommunicationDTO> communicationDTOs = customerDTO.getCustomerCommuncation();

        if (StringUtils.isNotBlank(deleteCommunicationID) && communicationDTOs != null
                && communicationDTOs.size() > 0) {
            for (CustomerCommunicationDTO communicationDTO : communicationDTOs) {
                if (communicationDTO.getId().equals(deleteCommunicationID)) {
                    communicationDTO.setIsdeleted(true);
                    break;
                }
            }
        }

        List<CustomerAddressDTO> addressDTOs = customerDTO.getCustomerAddress();

        if (StringUtils.isNotBlank(deleteAddressID) && addressDTOs != null && addressDTOs.size() > 0) {
            for (CustomerAddressDTO addressDTO : addressDTOs) {
                if ((addressDTO.getAddress_id().equals(deleteAddressID)
                        || addressDTO.getCreatedts().equals(deleteCommunicationID))) {
                    addressDTO.setIsdeleted(true);
                    addressDTO.getAddressDTO().setIsdeleted(true);
                    break;
                }
            }
        }

        String taxId = inputParamMap.get("TaxId");

        if (StringUtils.isNotBlank(taxId)) {
            customerDTO.setSsn(taxId);
            customerDTO.setIsChanged(true);
        }

        if (StringUtils.isNotBlank(inputParamMap.get("DateOfBirth"))) {
            customerDTO.setDateOfBirth(HelperMethods.getFormattedLocaleDate(inputParamMap.get("DateOfBirth"), "yyyy-MM-dd"));
        }

        String status = inputParamMap.get("status");
        if (StringUtils.isBlank(status)) {
            status = dcRequest.getParameter("status");
        }

        if (StringUtils.isNotBlank(status) && status.equals("ACTIVE")) {
                customerDTO.setStatus_id(HelperMethods.getCustomerStatus().get("ACTIVE"));
                customerDTO.setCustomerType_id(HelperMethods.getCustomerTypes().get("Retail"));
        }

        String identities = inputParamMap.get("identities");
        if (StringUtils.isBlank(identities)) {
            identities = dcRequest.getParameter("identities");
        }

        JsonArray identiti = new JsonArray();
        if (StringUtils.isNotBlank(identities)) {
            try {
                identiti = new JsonParser().parse(identities).getAsJsonArray();
            } catch (Exception e) {
            }
        }

        if (identiti != null && identiti.size() > 0) {
            JsonObject jsonObject = identiti.get(0).getAsJsonObject();
            if (jsonObject.has("IdentityType") && !jsonObject.get("IdentityType").isJsonNull()
                    && StringUtils.isNotBlank(jsonObject.get("IdentityType").getAsString())) {

                if (HelperMethods.getCustomerIdentifierMapping()
                        .containsKey(jsonObject.get("IdentityType").getAsString())) {
                    customerDTO.setiDType_id(HelperMethods.getCustomerIdentifierMapping()
                            .get(jsonObject.get("IdentityType").getAsString()));
                } else
                    customerDTO.setiDType_id(jsonObject.get("IdentityType").getAsString());
            }

            if (jsonObject.has("IdentityNumber") && !jsonObject.get("IdentityNumber").isJsonNull()
                    && StringUtils.isNotBlank(jsonObject.get("IdentityNumber").getAsString())) {
                customerDTO.setiDValue(jsonObject.get("IdentityNumber").getAsString());
            }

            if (jsonObject.has("IssuingCountry") && !jsonObject.get("IssuingCountry").isJsonNull()
                    && StringUtils.isNotBlank(jsonObject.get("IssuingCountry").getAsString())) {
                customerDTO.setiDCountry(jsonObject.get("IssuingCountry").getAsString());
            }
            if (jsonObject.has("IssuingState") && !jsonObject.get("IssuingState").isJsonNull()
                    && StringUtils.isNotBlank(jsonObject.get("IssuingState").getAsString())) {
                customerDTO.setiDState(jsonObject.get("IssuingState").getAsString());
            }

            if (jsonObject.has("IssueDate") && !jsonObject.get("IssueDate").isJsonNull()
                    && StringUtils.isNotBlank(jsonObject.get("IssueDate").getAsString())) {
                customerDTO.setiDIssueDate(
                        HelperMethods.getFormattedLocaleDate(jsonObject.get("IssueDate").getAsString(), "yyyy-MM-dd"));

            }

            if (jsonObject.has("ExpiryDate") && !jsonObject.get("ExpiryDate").isJsonNull()
                    && StringUtils.isNotBlank(jsonObject.get("ExpiryDate").getAsString())) {
                customerDTO.setiDExpiryDate(
                        HelperMethods.getFormattedLocaleDate(jsonObject.get("ExpiryDate").getAsString(), "yyyy-MM-dd"));
            }
        }

        if(StringUtils.isNotBlank(customerDTO.getDateOfBirth())){
            customerDTO.setDateOfBirth(HelperMethods.getFormattedLocaleDate(customerDTO.getDateOfBirth(), "yyyy-MM-dd"));
        }
        
        return customerDTO;
    }

    private static void addLegalUnit(List<CustomerCommunicationDTO> customerCommuncation,String legalEntityId) {	
    	if (customerCommuncation != null && customerCommuncation.size() > 0) {
            for (CustomerCommunicationDTO communicationDTO : customerCommuncation) {
                	communicationDTO.setCompanyLegalUnit(legalEntityId);
            }
        }
		
	}

    private static void removePrimary(List<CustomerAddressDTO> customerAddressDTOs) {
        if (customerAddressDTOs != null) {
            for (CustomerAddressDTO customerAddressDTO : customerAddressDTOs) {
                if (customerAddressDTO.getIsPrimary()) {
                    customerAddressDTO.setIsPrimary(false);
                    customerAddressDTO.setIsChanged(true);
                }
            }
        }
    }

    private static void removePrimary(List<CustomerCommunicationDTO> customerCommuncation, String type_id) {
        if (customerCommuncation != null && customerCommuncation.size() > 0) {
            for (CustomerCommunicationDTO communicationDTO : customerCommuncation) {
                if (type_id.equals(communicationDTO.getType_id()) && communicationDTO.getIsPrimary()) {
                    communicationDTO.setIsPrimary(false);
                    communicationDTO.setIsChanged(true);
                }
            }
        }
    }

    private static void removeAlertRequired(List<CustomerCommunicationDTO> customerCommuncation, String type_id) {
        if (customerCommuncation != null && customerCommuncation.size() > 0) {
            for (CustomerCommunicationDTO communicationDTO : customerCommuncation) {
                if (type_id.equals(communicationDTO.getType_id()) && communicationDTO.getIsAlertsRequired()) {
                    communicationDTO.setIsAlertsRequired(false);
                    communicationDTO.setIsChanged(true);
                }
            }
        }
    }

    public static JsonObject getJsonForT24(CustomerDTO customerDTO) {
        Map<String, String> inpMap = new HashMap<String, String>();

        if (customerDTO.getCustomerCommuncation() != null) {
            List<CustomerCommunicationDTO> communicationDTOs = customerDTO.getCustomerCommuncation();

            for (CustomerCommunicationDTO communicationDTO : communicationDTOs) {

                if (communicationDTO.getType_id().equals(HelperMethods.getCommunicationTypes().get("Email"))) {
                    inpMap.put("email", communicationDTO.getValue());
                    break;
                }
            }

            for (CustomerCommunicationDTO communicationDTO : communicationDTOs) {

                if (communicationDTO.getType_id().equals(HelperMethods.getCommunicationTypes().get("Phone"))) {
                    inpMap.put("phone", communicationDTO.getValue());
                    break;
                }
            }

        }

        JsonObject jsonObject = new JsonObject();

        jsonObject.add("header", new JsonObject());
        JsonObject body = new JsonObject();

        if (inpMap.containsKey("Email") || inpMap.containsKey("phone")) {
            JsonArray communicationDevices = new JsonArray();
            JsonObject communicationDevice = new JsonObject();
            if (inpMap.containsKey("phone")) {
                communicationDevice.addProperty("phoneNumber", inpMap.get("phone"));
            }

            if (inpMap.containsKey("email")) {
                communicationDevice.addProperty("email", inpMap.get("email"));
            }

            communicationDevices.add(communicationDevice);
            body.add("communicationDevices", communicationDevices);
            jsonObject.add("body", body);
        }

        return jsonObject;
    }

    public static CustomerDTO buildCustomerDTOforUpdate(String customerID, Map<String, String> inputParams,
            DataControllerRequest dcRequest, boolean isCoreCustomerIdPresent, boolean iS_Integrated) {
        
        CustomerDTO customerDTO = buildCustomerDTO(customerID, inputParams, dcRequest);
        
        if(!iS_Integrated || !isCoreCustomerIdPresent) {
            return customerDTO;
        }
        List<CustomerAddressDTO> addressDTOs = customerDTO.getCustomerAddress();

        if (addressDTOs.size() > 0) {
            for(int i=0; i<addressDTOs.size(); i++) {
                CustomerAddressDTO addressDTO = addressDTOs.get(i);
                if (addressDTO.getIsPrimary() && !addressDTO.getIsNew()) {
                    addressDTO.setIsChanged(false);
                    addressDTO.setIsNew(false);
                    addressDTO.setIsdeleted(true);
                    addressDTO.getAddressDTO().setIsChanged(false);
                    addressDTO.getAddressDTO().setIsNew(false);
                    addressDTO.getAddressDTO().setIsdeleted(true);
                }
                else if(addressDTO.getIsPrimary()){
                    addressDTOs.remove(i);
                    i--;
                }
            }
        }
        
        List<CustomerCommunicationDTO> communicationDTOs = customerDTO.getCustomerCommuncation();
        if (communicationDTOs.size() > 0) {
            for(int i=0; i<communicationDTOs.size(); i++) {
                CustomerCommunicationDTO communicationDTO = communicationDTOs.get(i);
                if (communicationDTO.getIsPrimary() && !communicationDTO.getIsNew()) {
                    communicationDTO.setIsChanged(false);
                    communicationDTO.setIsNew(false);
                    communicationDTO.setIsdeleted(true);
                }
                else if(communicationDTO.getIsPrimary()){
                    communicationDTOs.remove(i);
                    i--;
                }
            }
        }
        
        return customerDTO;
    }

}
