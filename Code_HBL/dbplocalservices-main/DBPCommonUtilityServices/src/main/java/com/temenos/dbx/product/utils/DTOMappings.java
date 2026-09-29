package com.temenos.dbx.product.utils;

import java.util.HashMap;
import java.util.Map;

import com.kony.dbputilities.util.URLConstants;
import com.temenos.dbx.mfa.dto.MFAServiceDTO;
import com.temenos.dbx.product.dto.AccountTypeDTO;
import com.temenos.dbx.product.dto.AccountsDTO;
import com.temenos.dbx.product.dto.AddressDTO;
import com.temenos.dbx.product.dto.AllAccountsViewDTO;
import com.temenos.dbx.product.dto.AlternateIdentity;
import com.temenos.dbx.product.dto.ApplicationDTO;
import com.temenos.dbx.product.dto.BackendIdentifierDTO;
import com.temenos.dbx.product.dto.BankDTO;
import com.temenos.dbx.product.dto.BusinessSignatoryDTO;
import com.temenos.dbx.product.dto.Citizenship;
import com.temenos.dbx.product.dto.ContactReference;
import com.temenos.dbx.product.dto.ContractCustomersDTO;
import com.temenos.dbx.product.dto.Country;
import com.temenos.dbx.product.dto.CredentialCheckerDTO;
import com.temenos.dbx.product.dto.CustomRoleActionLimitDTO;
import com.temenos.dbx.product.dto.CustomRoleDTO;
import com.temenos.dbx.product.dto.CustomerAccountsDTO;
import com.temenos.dbx.product.dto.CustomerActionDTO;
import com.temenos.dbx.product.dto.CustomerActionsProcDTO;
import com.temenos.dbx.product.dto.CustomerAddressDTO;
import com.temenos.dbx.product.dto.CustomerAddressViewDTO;
import com.temenos.dbx.product.dto.CustomerBusinessTypeDTO;
import com.temenos.dbx.product.dto.CustomerCommunicationDTO;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.CustomerEmploymentDetailsDTO;
import com.temenos.dbx.product.dto.CustomerExpensesDTO;
import com.temenos.dbx.product.dto.CustomerFlagStatus;
import com.temenos.dbx.product.dto.CustomerGroupDTO;
import com.temenos.dbx.product.dto.CustomerLegalEntityDTO;
import com.temenos.dbx.product.dto.CustomerPreferenceDTO;
import com.temenos.dbx.product.dto.CustomerPreferencesIdentityAttributesDTO;
import com.temenos.dbx.product.dto.CustomerSecurityQuestionsViewDTO;
import com.temenos.dbx.product.dto.Customership;
import com.temenos.dbx.product.dto.Employment;
import com.temenos.dbx.product.dto.ExcludedCustomerAccountsDTO;
import com.temenos.dbx.product.dto.ExtensionData;
import com.temenos.dbx.product.dto.FeatureActionDTO;
import com.temenos.dbx.product.dto.FeedBackStatusDTO;
import com.temenos.dbx.product.dto.MemberGroupDTO;
import com.temenos.dbx.product.dto.Occupation;
import com.temenos.dbx.product.dto.OrganisationDTO;
import com.temenos.dbx.product.dto.OrganisationEmployeesViewDTO;
import com.temenos.dbx.product.dto.OrganisationFeaturesDTO;
import com.temenos.dbx.product.dto.OrganizationAccountsDTO;
import com.temenos.dbx.product.dto.OrganizationAddressDTO;
import com.temenos.dbx.product.dto.OrganizationCommunicationDTO;
import com.temenos.dbx.product.dto.OrganizationEmployeesDTO;
import com.temenos.dbx.product.dto.OrganizationMembershipDTO;
import com.temenos.dbx.product.dto.OtherRiskIndicator;
import com.temenos.dbx.product.dto.OtherSourceOfIncome;
import com.temenos.dbx.product.dto.PartyAddress;
import com.temenos.dbx.product.dto.PartyAssessment;
import com.temenos.dbx.product.dto.PartyClassification;
import com.temenos.dbx.product.dto.PartyIdentifier;
import com.temenos.dbx.product.dto.PartyLifeCycle;
import com.temenos.dbx.product.dto.PartyNames;
import com.temenos.dbx.product.dto.PartyPosition;
import com.temenos.dbx.product.dto.PasswordHistoryDTO;
import com.temenos.dbx.product.dto.PasswordLockoutSettingsDTO;
import com.temenos.dbx.product.dto.Region;
import com.temenos.dbx.product.dto.Residence;
import com.temenos.dbx.product.dto.TaxDetails;
import com.temenos.dbx.product.dto.Vulnerability;

public class DTOMappings {

    private static Map<String, Class<?>> dtoClassTableMappings = new HashMap<String, Class<?>>();

    private static Map<Class<?>, Map<String, String>> dtoObjectPropertyMappings =
            new HashMap<Class<?>, Map<String, String>>();

    /**
     * @return the dtoMappings
     */
    public static Map<String, String> getDTOObjectPropertyMappings(Class<?> className) {
        return dtoObjectPropertyMappings.get(className);
    }

    /**
     * @return the dtoTableMappings
     */
    public static Class<?> getDTOCalssFromTableName(String tableName) {
        if (dtoClassTableMappings.containsKey(tableName)) {
            return dtoClassTableMappings.get(tableName);
        }
        return null;
    }

    static {
        Map<String, String> map = new HashMap<>();
        map.put("id", "id");
        map.put("classification_id", "Classification_id");
        map.put("customerType_id", "CustomerType_id");
        map.put("isCombinedUser", "isCombinedUser");
        map.put("firstName", "FirstName");
        map.put("middleName", "MiddleName");
        map.put("lastName", "LastName");
        map.put("fullName", "FullName");
        map.put("status_id", "Status_id");
        map.put("userName", "UserName");
        map.put("password", "Password");
        map.put("unsuccessfulLoginAttempts", "unsuccessfulLoginAttempts");
        map.put("lockCount", "lockCount");
        map.put("organization_Id", "Organization_Id");
        map.put("salutation", "Salutation");
        map.put("gender", "Gender");
        map.put("title", "Title");
        map.put("dateOfBirth", "DateOfBirth");
        map.put("drivingLicenseNumber", "DrivingLicenseNumber");
        map.put("ssn", "Ssn");
        map.put("cvv", "Cvv");
        map.put("token", "Token");
        map.put("pin", "Pin");
        map.put("preferredContactMethod", "PreferredContactMethod");
        map.put("preferredContactTime", "PreferredContactTime");
        map.put("maritalStatus_id", "MaritalStatus_id");
        map.put("spouseName", "SpouseName");
        map.put("noOfDependents", "NoOfDependents");
        map.put("employementStatus_id", "EmployementStatus_id");
        map.put("userCompany", "UserCompany");
        map.put("securityImage_id", "SecurityImage_id");
        map.put("location_id", "Location_id");
        map.put("isOlbAllowed", "IsOlbAllowed");
        map.put("isStaffMember", "IsStaffMember");
        map.put("countryCode", "CountryCode");
        map.put("userImage", "UserImage");
        map.put("userImageURL", "UserImageURL");
        map.put("olbEnrolmentStatus_id", "OlbEnrolmentStatus_id");
        map.put("otp", "Otp");
        map.put("preferedOtpMethod", "PreferedOtpMethod");
        map.put("otpGenaratedts", "OtpGenaratedts");
        map.put("validDate", "ValidDate");
        map.put("isUserAccountLocked", "isUserAccountLocked");
        map.put("isPinSet", "IsPinSet");
        map.put("isEnrolledForOlb", "IsEnrolledForOlb");
        map.put("isAssistConsented", "IsAssistConsented");
        map.put("isPhoneEnabled", "IsPhoneEnabled");
        map.put("isEmailEnabled", "IsEmailEnabled");
        map.put("isEnrolled", "isEnrolled");
        map.put("isSuperAdmin", "isSuperAdmin");
        map.put("currentLoginTime", "CurrentLoginTime");
        map.put("lastlogintime", "Lastlogintime");
        map.put("iDType_id", "IDType_id");
        map.put("iDValue", "IDValue");
        map.put("iDState", "IDState");
        map.put("iDCountry", "IDCountry");
        map.put("iDIssueDate", "IDIssueDate");
        map.put("iDExpiryDate", "IDExpiryDate");
        map.put("isCoreIdentityScope", "IsCoreIdentityScope");
        map.put("is_MemberEligibile", "Is_MemberEligibile");
        map.put("memberEligibilityData", "MemberEligibilityData");
        map.put("is_BBOA", "Is_BBOA");
        map.put("creditUnionMemberSince", "CreditUnionMemberSince");
        map.put("ationProfile_id", "AtionProfile_id");
        map.put("registrationLink", "RegistrationLink");
        map.put("regLinkResendCount", "RegLinkResendCount");
        map.put("regLinkValidity", "RegLinkValidity");
        map.put("areDepositTermsAccepted", "areDepositTermsAccepted");
        map.put("areAccountStatementTermsAccepted", "areAccountStatementTermsAccepted");
        map.put("areUserAlertsTurnedOn", "areUserAlertsTurnedOn");
        map.put("isBillPaySupported", "isBillPaySupported");
        map.put("isBillPayActivated", "isBillPayActivated");
        map.put("isP2PSupported", "isP2PSupported");
        map.put("isP2PActivated", "isP2PActivated");
        map.put("isWireTransferEligible", "isWireTransferEligible");
        map.put("isWireTransferActivated", "isWireTransferActivated");
        map.put("lockedOn", "lockedOn");
        map.put("isEagreementSigned", "isEagreementSigned");
        map.put("mothersMaidenName", "MothersMaidenName");
        map.put("addressValidationStatus", "AddressValidationStatus");
        map.put("Product", "Product");
        map.put("eligbilityCriteria", "EligbilityCriteria");
        map.put("reason", "Reason");
        map.put("applicantChannel", "ApplicantChannel");
        map.put("documentsSubmitted", "DocumentsSubmitted");
        map.put("createdby", "createdby");
        map.put("modifiedby", "modifiedby");
        map.put("createdts", "createdts");
        map.put("lastmodifiedts", "lastmodifiedts");
        map.put("synctimestamp", "synctimestamp");
        map.put("softdeleteflag", "softdeleteflag");
        map.put("bank_id", "Bank_id");
        map.put("session_id", "Session_id");
        map.put("maritalStatus", "MaritalStatus");
        map.put("spouseFirstName", "SpouseFirstName");
        map.put("spouseLastName", "SpouseLastName");
        map.put("employmentInfo", "EmploymentInfo");
        map.put("isEngageProvisioned", "isEngageProvisioned");
        map.put("defaultLanguage", "DefaultLanguage");
        map.put("isVIPCustomer", "isVIPCustomer");
        map.put("organizationType", "organizationType");
        map.put("taxID", "taxID");
        map.put("isEnrolledFromSpotlight", "isEnrolledFromSpotlight");
        map.put("SFDC_accountId", "SFDC_accountId");
        dtoObjectPropertyMappings.put(CustomerDTO.class, map);
    }

    static {
        Map<String, String> map = new HashMap<>();
        map.put("id", "id");
        map.put("type_id", "Type_id");
        map.put("customer_id", "Customer_id");
        map.put("isPrimary", "isPrimary");
        map.put("value", "Value");
        map.put("extension", "Extension");
        map.put("description", "Description");
        map.put("isPreferredContactMethod", "IsPreferredContactMethod");
        map.put("preferredContactTime", "PreferredContactTime");
        map.put("createdby", "createdby");
        map.put("modifiedby", "modifiedby");
        map.put("createdts", "createdts");
        map.put("lastmodifiedts", "lastmodifiedts");
        map.put("synctimestamp", "synctimestamp");
        map.put("softdeleteflag", "softdeleteflag");
        map.put("type", "type");
        map.put("countryType", "countryType");
        map.put("receivePromotions", "receivePromotions");
        map.put("phoneCountryCode", "phoneCountryCode");
        map.put("isTypeBusiness", "isTypeBusiness");
        map.put("isAlertsRequired", "isAlertsRequired");

        dtoObjectPropertyMappings.put(CustomerCommunicationDTO.class, map);
    }

    static {
        Map<String, String> map = new HashMap<>();
        map.put("id", "id");
        map.put("customer_id", "Customer_id");
        map.put("isTypeBusiness", "isTypeBusiness");
        map.put("defaultAccountDeposit", "DefaultAccountDeposit");
        map.put("defaultAccountTransfers", "DefaultAccountTransfers");
        map.put("defaultModule_id", "DefaultModule_id");
        map.put("defaultAccountPayments", "DefaultAccountPayments");
        map.put("defaultAccountCardless", "DefaultAccountCardless");
        map.put("defaultAccountBillPay", "DefaultAccountBillPay");
        map.put("defaultToAccountP2P", "DefaultToAccountP2P");
        map.put("defaultFromAccountP2P", "DefaultFromAccountP2P");
        
        map.put("defaultAccountLoanPayment", "DefaultAccountLoanPayment");
        map.put("defaultAccountCardPayment", "DefaultAccountCardPayment");
        map.put("defaultAccountCheckManagement", "DefaultAccountCheckManagement");
        
        map.put("defaultAccountDashboard", "DefaultAccountDashboard");
        map.put("defaultAccountQRPayment", "DefaultAccountQRPayment");
        map.put("defaultAccountCheckDeposit", "DefaultAccountCheckDeposit");
        map.put("defaultAccountCashWithdrawl", "DefaultAccountCashWithdrawl");
        map.put("defaultAccounteSewa", "DefaultAccounteSewa");
        
        map.put("defaultAccountWire", "DefaultAccountWire");
        map.put("areUserAlertsTurnedOn", "areUserAlertsTurnedOn");
        map.put("areDepositTermsAccepted", "areDepositTermsAccepted");
        map.put("areAccountStatementTermsAccepted", "areAccountStatementTermsAccepted");
        map.put("isBillPaySupported", "isBillPaySupported");
        map.put("isP2PSupported", "isP2PSupported");
        map.put("isBillPayActivated", "isBillPayActivated");
        map.put("isP2PActivated", "isP2PActivated");
        map.put("isWireTransferActivated", "isWireTransferActivated");
        map.put("isWireTransferEligible", "isWireTransferEligible");
        map.put("showBillPayFromAccPopup", "ShowBillPayFromAccPopup");
        map.put("sreferedOtpMethod", "PreferedOtpMethod");
        map.put("createdts", "createdts");
        map.put("lastmodifiedts", "lastmodifiedts");
        map.put("synctimestamp", "synctimestamp");
        map.put("softdeleteflag", "softdeleteflag");

        dtoObjectPropertyMappings.put(CustomerPreferenceDTO.class, map);
    }

    static {
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", "Customer_id");
        map.put("address_id", "Address_id");
        map.put("type_id", "Type_id");
        map.put("isPrimary", "isPrimary");
        map.put("durationOfStay", "DurationOfStay");
        map.put("homeOwnership", "HomeOwnership");
        map.put("createdby", "createdby");
        map.put("modifiedby", "modifiedby");
        map.put("createdts", "createdts");
        map.put("lastmodifiedts", "lastmodifiedts");
        map.put("synctimestamp", "synctimestamp");
        map.put("softdeleteflag", "softdeleteflag");
        map.put("isTypeBusiness", "isTypeBusiness");
        dtoObjectPropertyMappings.put(CustomerAddressDTO.class, map);
    }

    static {
        Map<String, String> map = new HashMap<>();
        map.put("customerId", "CustomerId");
        map.put("addressId", "Address_id");
        map.put("addressType", "AddressType");
        map.put("addressLine1", "AddressLine1");
        map.put("addressLine2", "AddressLine2");
        map.put("zipCode", "ZipCode");
        map.put("cityName", "CityName");
        map.put("cityId", "City_id");
        map.put("regionName", "RegionName");
        map.put("regionId", "Region_id");
        map.put("regionCode", "RegionCode");
        map.put("countryName", "CountryName");
        map.put("countryId", "Country_id");
        map.put("countryCode", "CountryCode");
        map.put("isPrimary", "isPrimary");
        map.put("isTypeBusiness", "isTypeBusiness");
        map.put("companyLegalUnit", "companyLegalUnit");
        dtoObjectPropertyMappings.put(CustomerAddressViewDTO.class, map);
    }

    static {
        Map<String, String> map = new HashMap<>();
        map.put("id", "id");
        map.put("region_id", "Region_id");
        map.put("city_id", "City_id");
        map.put("addressLine1", "addressLine1");
        map.put("addressLine2", "addressLine2");
        map.put("addressLine3", "addressLine3");
        map.put("zipCode", "zipCode");
        map.put("latitude", "latitude");
        map.put("logitude", "logitude");
        map.put("isPreferredAddress", "isPreferredAddress");
        map.put("cityName", "cityName");
        map.put("user_id", "User_id");
        map.put("country", "country");
        map.put("type", "type");
        map.put("state", "state");
        map.put("createdby", "createdby");
        map.put("modifiedby", "modifiedby");
        map.put("createdts", "createdts");
        map.put("lastmodifiedts", "lastmodifiedts");
        map.put("synctimestamp", "synctimestamp");
        map.put("softdeleteflag", "softdeleteflag");

        dtoObjectPropertyMappings.put(AddressDTO.class, map);
    }

    static {
        Map<String, String> map = new HashMap<>();
        map.put("id", "id");
        map.put("customer_id", "Customer_id");
        map.put("employmentType", "EmploymentType");
        map.put("currentEmployer", "CurrentEmployer");
        map.put("designation", "Designation");
        map.put("payPeriod", "PayPeriod");
        map.put("grossIncome", "GrossIncome");
        map.put("weekWorkingHours", "WeekWorkingHours");
        map.put("employmentStartDate", "EmploymentStartDate");
        map.put("previousEmployer", "PreviousEmployer");
        map.put("previousDesignation", "PreviousDesignation");
        map.put("otherEmployementType", "OtherEmployementType");
        map.put("otherEmployementDescription", "OtherEmployementDescription");
        map.put("createdby", "createdby");
        map.put("modifiedby", "modifiedby");
        map.put("createdts", "createdts");
        map.put("lastmodifiedts", "lastmodifiedts");
        map.put("synctimestamp", "synctimestamp");
        map.put("softdeleteflag", "softdeleteflag");

        dtoObjectPropertyMappings.put(CustomerEmploymentDetailsDTO.class, map);
    }

    static {
        Map<String, String> map = new HashMap<>();
        map.put("amount", "Amount");
        map.put("id", "id");
        map.put("type", "Type");
        map.put("customer_id", "Customer_id");

        dtoObjectPropertyMappings.put(CustomerExpensesDTO.class, map);
    }

    static {
        Map<String, String> map = new HashMap<>();
        map.put("id", "id");
        map.put("feature_id", "Feature_id");
        map.put("app_id", "App_id");
        map.put("type_id", "Type_id");
        map.put("rrole_id", "Rrole_id");
        map.put("name", "name");
        map.put("description", "description");
        map.put("isAccountLevel", "isAccountLevel");
        map.put("isMFAApplicable", "isMFAApplicable");
        map.put("mfa_id", "MFA_id");
        map.put("termsAndConditions_id", "TermsAndConditions_id");
        map.put("notes", "notes");
        map.put("isPrimary", "isPrimary");
        map.put("displaySequence", "DisplaySequence");
        map.put("createdby", "createdby");
        map.put("modifiedby", "modifiedby");
        map.put("createdts", "createdts");
        map.put("lastmodifiedts", "lastmodifiedts");
        map.put("synctimestamp", "synctimestamp");
        map.put("softdeleteflag", "softdeleteflag");

        dtoObjectPropertyMappings.put(FeatureActionDTO.class, map);
    }

    static {
        Map<String, String> map = new HashMap<>();
        map.put("accountId", "Account_id");
        map.put("isOrganizationAccount", "IsOrganizationAccount");
        map.put("membershipId", "Membership_id");
        map.put("taxId", "Taxid");
        map.put("accountHolder", "AccountHolder");
        map.put("accountName", "AccountName");
        map.put("typeId", "Type_id");
        map.put("arrangementId", "arrangementId");

        dtoObjectPropertyMappings.put(OrganizationAccountsDTO.class, map);
    }

    static {
        Map<String, String> map = new HashMap<>();
        map.put("serviceKey", "serviceKey");
        map.put("serviceName", "serviceName");
        map.put("user_id", "User_id");
        map.put("createddts", "Createddts");
        map.put("retryCount", "retryCount");
        map.put("payload", "payload");
        map.put("securityQuestions", "securityQuestions");
        map.put("isVerified", "isVerified");

        dtoObjectPropertyMappings.put(MFAServiceDTO.class, map);
    }

    static {
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", "Customer_id");
        map.put("customerAnswer", "CustomerAnswer");
        map.put("securityQuestion_id", "SecurityQuestion_id");
        map.put("createdby", "createdby");
        map.put("modifiedby", "modifiedby");
        map.put("createdts", "createdts");
        map.put("lastmodifiedts", "lastmodifiedts");
        map.put("question", "Question");
        map.put("questionStatus_id", "QuestionStatus_id");
        map.put("customerStatus_id", "CustomerStatus_id");

        dtoObjectPropertyMappings.put(CustomerSecurityQuestionsViewDTO.class, map);
    }

    static {
        dtoClassTableMappings.put(URLConstants.FEATURE_ACTION_GET, FeatureActionDTO.class);
        dtoClassTableMappings.put(URLConstants.ORGANISATIONFEATURES_GET, OrganisationFeaturesDTO.class);
        dtoClassTableMappings.put(URLConstants.MFA_SERVICE_GET, MFAServiceDTO.class);
        dtoClassTableMappings.put(URLConstants.CUSTOMERSECURITYQUESTION_VIEW, CustomerSecurityQuestionsViewDTO.class);
        dtoClassTableMappings.put(URLConstants.CUSTOMER_GET, CustomerDTO.class);
        dtoClassTableMappings.put(URLConstants.CUSTOMER_COMMUNICATION_GET, CustomerCommunicationDTO.class);
        dtoClassTableMappings.put(URLConstants.BACKENDIDENTIFIER_GET, BackendIdentifierDTO.class);
        dtoClassTableMappings.put(URLConstants.CUSTOMER_ADDRESS_GET, CustomerAddressDTO.class);
        dtoClassTableMappings.put(URLConstants.ADDRESS_GET, AddressDTO.class);
        dtoClassTableMappings.put(URLConstants.CUSTOMER_EMP_DETAILS_GET, CustomerEmploymentDetailsDTO.class);
        dtoClassTableMappings.put(URLConstants.CUSTOMER_EXPENSES_GET, CustomerExpensesDTO.class);
        dtoClassTableMappings.put(URLConstants.OTHER_SOURCSE_OF_INCOME_GET, OtherSourceOfIncome.class);
        dtoClassTableMappings.put(URLConstants.CUSTOMERFLAG_GET, CustomerFlagStatus.class);
        dtoClassTableMappings.put(URLConstants.PASSWORDHISTORY_GET, PasswordHistoryDTO.class);
        dtoClassTableMappings.put(URLConstants.CREDENTIAL_CHECKER_GET, CredentialCheckerDTO.class);
        dtoClassTableMappings.put(URLConstants.BANK_GET, BankDTO.class);
        dtoClassTableMappings.put(URLConstants.FEEDBACKSTATUS_GET, FeedBackStatusDTO.class);
        dtoClassTableMappings.put(URLConstants.CUSTOMERPREFERENCE_GET, CustomerPreferenceDTO.class);
        dtoClassTableMappings.put(URLConstants.CUSTOMER_ADDRESS_VIEW_GET, CustomerAddressViewDTO.class);
        dtoClassTableMappings.put(URLConstants.PASSWORDLOCKOUTSETTINGS_GET, PasswordLockoutSettingsDTO.class);
        dtoClassTableMappings.put(URLConstants.CUSTOMERACCOUNTS_GET, CustomerAccountsDTO.class);
        dtoClassTableMappings.put(URLConstants.REGION_GET, Region.class);
        dtoClassTableMappings.put(URLConstants.COUNTRY_GET, Country.class);
        dtoClassTableMappings.put(URLConstants.CONTRACT_CUSTOMERS_GET, ContractCustomersDTO.class);
        dtoClassTableMappings.put(URLConstants.EXCLUDEDCUSTOMERACCOUNTS_GET, ExcludedCustomerAccountsDTO.class);
        dtoClassTableMappings.put(URLConstants.CUSTOMERLEGALENTITY_GET, CustomerLegalEntityDTO.class);
        
        dtoClassTableMappings.put("extensionData", ExtensionData.class);
        dtoClassTableMappings.put("partyNames", PartyNames.class);
        dtoClassTableMappings.put("citizenships", Citizenship.class);
        dtoClassTableMappings.put("alternateIdentity", AlternateIdentity.class);
        dtoClassTableMappings.put("partyAssessment", PartyAssessment.class);
        dtoClassTableMappings.put("partyIdentifiers", PartyIdentifier.class);
        dtoClassTableMappings.put("partyPosition", PartyPosition.class);
        dtoClassTableMappings.put("partyLifeCycle", PartyLifeCycle.class);
        dtoClassTableMappings.put("occupation", Occupation.class);
        dtoClassTableMappings.put("employments", Employment.class);
        dtoClassTableMappings.put("otherRiskIndicator", OtherRiskIndicator.class);
        dtoClassTableMappings.put("residence", Residence.class);
        dtoClassTableMappings.put("partyClassification", PartyClassification.class);
        dtoClassTableMappings.put("customership", Customership.class);
        dtoClassTableMappings.put("vulnerability", Vulnerability.class);
        dtoClassTableMappings.put("taxDetails", TaxDetails.class);
        dtoClassTableMappings.put("addresses", PartyAddress.class);
        dtoClassTableMappings.put("contactReferences", ContactReference.class);

        // dtoClassTableMappings.put(URLConstants.ORGANISATION_ACCOUNTS_GET,
        // OrganizationAccountsDTO.class);
    }

    static {
        Map<String, String> map = new HashMap<>();
        map.put("id", "id");
        map.put("customer_id", "Customer_id");
        map.put("sequenceNumber", "sequenceNumber");
        map.put("backendId", "BackendId");
        map.put("backendType", "BackendType");
        map.put("identifier_name", "identifier_name");
        map.put("createdts", "createdts");
        map.put("lastmodifiedts", "lastmodifiedts");
        map.put("contractId", "contractId");
        map.put("companyId", "CompanyId");
        map.put("contractTypeId", "contractTypeId");
        map.put("companyLegalUnit", "companyLegalUnit");
        dtoObjectPropertyMappings.put(BackendIdentifierDTO.class, map);
    }
    
	static {
		Map<String, String> map = new HashMap<>();
		map.put("id", "id");
		map.put("Customer_id", "Customer_id");
		map.put("Status_id", "Status_id");
		map.put("legalEntityId", "legalEntityId");		
		map.put("createdts", "createdts");
		map.put("modifiedts", "modifiedts");	
		dtoObjectPropertyMappings.put(CustomerLegalEntityDTO.class, map);
	}

    static {
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", "Customer_id");
        map.put("status_id", "Status_id");
        map.put("createdby", "createdby");
        map.put("modifiedby", "modifiedby");
        map.put("createdts", "createdts");
        map.put("lastmodifiedts", "lastmodifiedts");
        map.put("synctimestamp", "synctimestamp");
        map.put("softdeleteflag", "softdeleteflag");

        dtoObjectPropertyMappings.put(CustomerFlagStatus.class, map);
    }

    static {
        Map<String, String> map = new HashMap<>();
        map.put("id", "id");
        map.put("incomeInfo_id", "IncomeInfo_id");
        map.put("sourceType", "SourceType");
        map.put("payPeriod", "PayPeriod");
        map.put("grossIncome", "GrossIncome");
        map.put("weekWorkingHours", "WeekWorkingHours");
        map.put("customer_id", "Customer_id");
        map.put("sourceOfIncomeName", "SourceOfIncomeName");
        map.put("sourceofIncomeDescription", "SourceofIncomeDescription");
        map.put("createdby", "createdby");
        map.put("modifiedby", "modifiedby");
        map.put("createdts", "createdts");
        map.put("lastmodifiedts", "lastmodifiedts");
        map.put("synctimestamp", "synctimestamp");
        map.put("softdeleteflag", "softdeleteflag");

        dtoObjectPropertyMappings.put(OtherSourceOfIncome.class, map);
    }

    static {
        Map<String, String> map = new HashMap<>();
        map.put("id", "id");
        map.put("customer_id", "Customer_id");
        map.put("previousPassword", "PreviousPassword");
        map.put("createdby", "createdby");
        map.put("modifiedby", "modifiedby");
        map.put("createdts", "createdts");
        map.put("lastmodifiedts", "lastmodifiedts");
        map.put("synctimestamp", "synctimestamp");
        map.put("softdeleteflag", "softdeleteflag");

        dtoObjectPropertyMappings.put(PasswordHistoryDTO.class, map);
    }

    // static {
    // Map<String, String> map = new HashMap<>();
    // map.put("id", "id");
    // map.put("customer_id", "Customer_id");
    // map.put("defaultAccountDeposit", "DefaultAccountDeposit");
    // map.put("defaultAccountTransfers", "DefaultAccountTransfers");
    // map.put("defaultModule_id", "DefaultModule_id");
    // map.put("defaultAccountPayments", "DefaultAccountPayments");
    // map.put("defaultAccountCardless", "DefaultAccountCardless");
    // map.put("defaultAccountBillPay", "DefaultAccountBillPay");
    // map.put("defaultToAccountP2P", "DefaultToAccountP2P");
    // map.put("defaultFromAccountP2P", "DefaultFromAccountP2P");
    // map.put("defaultAccountWire", "DefaultAccountWire");
    // map.put("areUserAlertsTurnedOn", "areUserAlertsTurnedOn");
    // map.put("areDepositTermsAccepted", "areDepositTermsAccepted");
    // map.put("areAccountStatementTermsAccepted", "areAccountStatementTermsAccepted");
    // map.put("isBillPaySupported", "isBillPaySupported");
    // map.put("isP2PSupported", "isP2PSupported");
    // map.put("isBillPayActivated", "isBillPayActivated");
    // map.put("isP2PActivated", "isP2PActivated");
    // map.put("isWireTransferActivated", "isWireTransferActivated");
    // map.put("isWireTransferEligible", "isWireTransferEligible");
    // map.put("showBillPayFromAccPopup", "ShowBillPayFromAccPopup");
    // map.put("sreferedOtpMethod", "PreferedOtpMethod");
    // map.put("createdts", "createdts");
    // map.put("lastmodifiedts", "lastmodifiedts");
    // map.put("synctimestamp", "synctimestamp");
    // map.put("softdeleteflag", "softdeleteflag");
    //
    // dtoObjectPropertyMappings.put(PasswordHistoryDTO.class, map);
    // }

    static {
        Map<String, String> map = new HashMap<>();
        map.put("id", "id");
        map.put("typeId", "Type_Id");
        map.put("name", "Name");
        map.put("description", "Description");
        map.put("businessTypeId", "BusinessType_id");
        map.put("statusId", "StatusId");
        map.put("faxId", "FaxId");
        map.put("createdby", "createdby");
        map.put("createdts", "createdts");
        map.put("rejectedby", "rejectedby");
        map.put("rejectedts", "rejectedts");
        map.put("rejectedReason", "rejectedReason");

        dtoObjectPropertyMappings.put(OrganisationDTO.class, map);
    }
    static {
        Map<String, String> map = new HashMap<>();
        map.put("accountId", "Account_id");
        map.put("accountName", "AccountName");
        map.put("accountType", "accountType");
        map.put("accountHolderName", "AccountHolder");
        map.put("membershipId", "Membership_id");
        map.put("typeId", "Type_id");
        map.put("taxId", "Taxid");
        map.put("membershipName", "name");
        map.put("arrangementId", "arrangementId");

        dtoObjectPropertyMappings.put(AllAccountsViewDTO.class, map);
    }
    static {
        Map<String, String> map = new HashMap<>();
        map.put("accountId", "Account_id");
        map.put("organizationId", "Organization_id");
        map.put("isBusinessAccount", "isBusinessAccount");
        map.put("accountHolder", "AccountHolder");
        map.put("accountName", "AccountName");
        map.put("typeId", "Type_id");
        map.put("statusDescription", "StatusDesc");
        map.put("membershipId", "Membership_id");
        map.put("arrangementId", "arrangementId");
        map.put("taxId", "TaxId");
        map.put("membershipName", "MembershipName");
        map.put("userId", "User_id");
        map.put("accountType", "accountType");
        dtoObjectPropertyMappings.put(AccountsDTO.class, map);
    }
    static {
        Map<String, String> map = new HashMap<>();
        map.put("accountTypeId", "TypeID");
        map.put("accountTypeDescription", "TypeDescription");
        map.put("accountTypeDisplayName", "displayName");
        dtoObjectPropertyMappings.put(AccountTypeDTO.class, map);
    }
    static {
        Map<String, String> map = new HashMap<>();
        map.put("id", "id");
        map.put("organisationId", "Organization_id");
        map.put("membershipId", "Membership_id");
        map.put("taxId", "Taxid");
        dtoObjectPropertyMappings.put(OrganizationMembershipDTO.class, map);
    }
    static {
        Map<String, String> map = new HashMap<>();
        map.put("synctimestamp", "synctimestamp");
        map.put("status_id", "status");
        map.put("softdeleteflag", "softdeleteflag");
        map.put("parent_id", "parent_id");
        map.put("organization_id", "organization_id");
        map.put("name", "name");
        map.put("modifiedby", "modifiedby");
        map.put("lastmodifiedts", "lastmodifiedts");
        map.put("id", "id");
        map.put("description", "description");
        map.put("createdts", "createdts");
        map.put("createdby", "createdby");
        map.put("userName", "userName");
        map.put("parentRoleName", "parentRoleName");
        map.put("statusValue", "statusValue");
        dtoObjectPropertyMappings.put(CustomRoleDTO.class, map);
    }
    static {
        Map<String, String> map = new HashMap<>();
        map.put("account_id", "account_id");
        map.put("action_id", "action_id");
        map.put("createdby", "createdby");
        map.put("createdts", "createdts");
        map.put("customRole_id", "customRole_id");
        map.put("id", "id");
        map.put("isAllowed", "isAllowed");
        map.put("lastmodifiedts", "lastmodifiedts");
        map.put("limitType_id", "limitType_id");
        map.put("modifiedby", "modifiedby");
        map.put("softdeleteflag", "softdeleteflag");
        map.put("value", "value");
        map.put("featureName", "featureName");
        map.put("featureDescription", "featureDescription");
        map.put("accountName", "accountName");
        map.put("isAccountLevel", "isAccountLevel");
        map.put("actionType", "actionType");
        map.put("actionName", "actionName");
        map.put("featureId", "featureId");
        map.put("actionDescription", "actionDescription");
        dtoObjectPropertyMappings.put(CustomRoleActionLimitDTO.class, map);
    }
    static {
        Map<String, String> map = new HashMap<>();
        map.put("id", "id");
        map.put("organizationId", "Organization_id");
        map.put("addressId", "Address_id");
        map.put("durationOfStay", "DurationOfStay");
        map.put("isPrimary", "IsPrimary");
        map.put("createdBy", "createdby");
        map.put("modifiedBy", "modifiedby");
        dtoObjectPropertyMappings.put(OrganizationAddressDTO.class, map);
    }
    static {
        Map<String, String> map = new HashMap<>();
        map.put("id", "id");
        map.put("typeId", "Type_id");
        map.put("organizationId", "Organization_id");
        map.put("sequence", "Sequence");
        map.put("value", "Value");
        map.put("extension", "Extension");
        map.put("description", "Description");
        map.put("isPrefferedContactMethod", "IsPreferredContactMethod");
        map.put("prefferedContactTime", "PreferredContactTime");
        map.put("createdBy", "createdby");
        map.put("modifiedBy", "createdby");
        dtoObjectPropertyMappings.put(OrganizationCommunicationDTO.class, map);
    }
    static {
        Map<String, String> map = new HashMap<>();
        map.put("businessTypeId", "BusinessType_id");
        map.put("signatoryId", "Signatory_id");
        dtoObjectPropertyMappings.put(BusinessSignatoryDTO.class, map);
    }
    static {
        Map<String, String> map = new HashMap<>();
        map.put("id", "id");
        map.put("organizationId", "Organization_id");
        map.put("customerId", "Customer_id");
        map.put("isAdmin", "Is_Admin");
        map.put("isOwner", "Is_Owner");
        map.put("isAuthSignatory", "isAuthSignatory");
        map.put("createdBy", "createdby");
        map.put("modifiedBy", "modifiedby");
        dtoObjectPropertyMappings.put(OrganizationEmployeesDTO.class, map);
    }
    static {
        Map<String, String> map = new HashMap<>();
        map.put("id", "id");
        map.put("customerId", "Customer_id");
        map.put("membershipId", "Membership_id");
        map.put("accountId", "Account_id");
        map.put("organizationId", "Organization_id");
        map.put("accountName", "AccountName");
        map.put("favouriteStatus", "FavouriteStatus");
        map.put("isViewAllowed", "IsViewAllowed");
        map.put("isDepositAllowed", "IsDepositAllowed");
        map.put("isWithdrawAllowed", "IsWithdrawAllowed");
        map.put("isOrganizationAccount", "IsOrganizationAccount");
        map.put("isOrgAccountUnLinked", "IsOrgAccountUnLinked");
        map.put("createdBy", "createdby");
        map.put("createdts", "createdts");
        map.put("lastmodifiedts", "lastmodifiedts");
        dtoObjectPropertyMappings.put(CustomerAccountsDTO.class, map);
    }
    static {
        Map<String, String> map = new HashMap<>();
        map.put("customerId", "Customer_id");
        map.put("groupId", "Group_id");
        map.put("createdBy", "createdby");
        map.put("modifiedBy", "modifiedby");
        dtoObjectPropertyMappings.put(CustomerGroupDTO.class, map);
    }
    static {
        Map<String, String> map = new HashMap<>();
        map.put("isAccountCentricCore", "isAccountCentricCore");
        dtoObjectPropertyMappings.put(ApplicationDTO.class, map);
    }
    static {
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", "customer_id");
        map.put("custcomm_value", "custcomm_value");
        map.put("statusId", "Status_id");
        map.put("firstName", "FirstName");
        map.put("middleName", "MiddleName");
        map.put("lastName", "LastName");
        map.put("userName", "UserName");
        map.put("drivingLicenseNumber", "DrivingLicenseNumber");
        map.put("dateOfBirth", "DateOfBirth");
        map.put("ssn", "Ssn");
        dtoObjectPropertyMappings.put(OrganisationEmployeesViewDTO.class, map);
    }
    static {
        Map<String, String> map = new HashMap<>();
        map.put("id", "id");
        map.put("userName", "UserName");
        map.put("linktype", "linktype");
        map.put("createdts", "createdts");
        map.put("retryCount", "retryCount");
        dtoObjectPropertyMappings.put(CredentialCheckerDTO.class, map);
    }
    static {
        Map<String, String> map = new HashMap<>();
        map.put("customerId", "Customer_id");
        map.put("businessTypeId", "BusinessType_id");
        map.put("signatoryTypeId", "SignatoryType_id");
        map.put("createdBy", "createdby");
        map.put("modifiedBy", "modifiedby");
        dtoObjectPropertyMappings.put(CustomerBusinessTypeDTO.class, map);
    }

    static {
        Map<String, String> map = new HashMap<>();
        map.put("id", "id");
        map.put("description", "Description");
        map.put("oauth2", "Oauth2");
        map.put("identityProvider", "IdentityProvider");
        dtoObjectPropertyMappings.put(BankDTO.class, map);
    }

    static {
        Map<String, String> map = new HashMap<>();
        map.put("id", "id");
        map.put("description", "Description");
        map.put("oauth2", "Oauth2");
        map.put("identityProvider", "IdentityProvider");
        dtoObjectPropertyMappings.put(BankDTO.class, map);
    }

    static {
        Map<String, String> map = new HashMap<>();
        map.put("id", "id");
        map.put("UserName", "UserName");
        map.put("feedbackID", "feedbackID");
        map.put("createdts", "createdts");
        map.put("status", "status");
        map.put("deviceID", "deviceID");
        map.put("customerID", "customerID");
        dtoObjectPropertyMappings.put(FeedBackStatusDTO.class, map);
    }
    static {
        Map<String, String> map = new HashMap<>();
        map.put("id", "id");
        map.put("typeId", "Type_id");
        dtoObjectPropertyMappings.put(MemberGroupDTO.class, map);
    }

    static {
        Map<String, String> map = new HashMap<>();
        map.put("id", "id");
        map.put("country_id", "Country_id");
        map.put("code", "Code");
        map.put("name", "Name");
        map.put("createdby", "createdby");
        map.put("modifiedby", "modifiedby");
        map.put("createdts", "createdts");
        map.put("lastmodifiedts", "lastmodifiedts");
        map.put("synctimestamp", "synctimestamp");
        map.put("softdeleteflag", "softdeleteflag");

        dtoObjectPropertyMappings.put(MemberGroupDTO.class, map);
    }

    static {
        Map<String, String> map = new HashMap<>();
        map.put("id", "id");
        map.put("country_id", "Country_id");
        map.put("code", "Code");
        map.put("name", "Name");
        map.put("createdby", "createdby");
        map.put("modifiedby", "modifiedby");
        map.put("createdts", "createdts");
        map.put("lastmodifiedts", "lastmodifiedts");
        map.put("synctimestamp", "synctimestamp");
        map.put("softdeleteflag", "softdeleteflag");

        dtoObjectPropertyMappings.put(Region.class, map);
    }

    static {
        Map<String, String> map = new HashMap<>();
        map.put("id", "id");
        map.put("code", "Code");
        map.put("name", "Name");
        map.put("createdby", "createdby");
        map.put("modifiedby", "modifiedby");
        map.put("createdts", "createdts");
        map.put("lastmodifiedts", "lastmodifiedts");
        map.put("synctimestamp", "synctimestamp");
        map.put("softdeleteflag", "softdeleteflag");

        dtoObjectPropertyMappings.put(Country.class, map);
    }

    static {
        Map<String, String> map = new HashMap<>();
        map.put("customerId", "Customer_id");
        map.put("accountId", "Account_id");
        map.put("isAllowed", "isAllowed");
        map.put("actionId", "Action_id");
        map.put("isAccountLevel", "isAccountLevel");
        map.put("featureStatusId", "Feature_Status_id");
        map.put("featureId", "Feature_id");
        map.put("roleTypeId", "RoleType_id");
        map.put("limitTypeId", "LimitType_id");
        map.put("value", "value");

        dtoObjectPropertyMappings.put(CustomerActionsProcDTO.class, map);
    }

    static {
        Map<String, String> map = new HashMap<>();
        map.put("customerId", "customerId");
        map.put("defaultAccountCardless", "default_account_cardless");
        map.put("defaultAccountWire", "default_account_wire");
        map.put("defaultFromAccountP2P", "default_from_account_p2p");
        map.put("showBillpayFromAccountPopup", "showBillPayFromAccPopup");
        map.put("defaultAccountTransfers", "default_account_transfers");
        map.put("defaultToAccountP2P", "default_to_account_p2p");
        map.put("defaultAccountPayments", "default_account_payments");
        map.put("defaultAccountDeposit", "default_account_deposit");
        map.put("defaultAccountBillPay", "default_account_billPay");
        
        map.put("defaultAccountLoanpayment", "default_account_loanpayment");
        map.put("defaultAccountCardpayment", "default_account_cardpayment");
        map.put("defaultAccountCheckmanagement", "default_account_checkmanagement");
        
        map.put("defaultAccountDashboard", "default_account_dashboard");
        map.put("defaultAccountQRPayment", "default_account_qrpayment");
        map.put("defaultAccountCheckDeposit", "default_account_checkdeposit");
        map.put("defaultAccountCashWithdrawl", "default_account_cashwithdrawl");
        map.put("defaultAccounteSewa", "default_account_esewa");
		
        map.put("phone", "Phone");
        map.put("email", "Email");
        map.put("country", "country");
        map.put("zipCode", "zipCode");
        map.put("latitude", "latitude");
        map.put("isPreferredAddress", "isPreferredAddress");
        map.put("longitude", "logitude");
        map.put("addressLine1", "addressLine1");
        map.put("addressLine2", "addressLine2");
        map.put("addressLine3", "addressLine3");
        map.put("state", "state");

        dtoObjectPropertyMappings.put(CustomerPreferencesIdentityAttributesDTO.class, map);
    }

    static {
        Map<String, String> map = new HashMap<>();
        map.put("id", "customerId");
        map.put("roleTpeId", "RoleType_id");
        map.put("customerId", "Customer_id");
        map.put("contractId", "contractId");
        map.put("coreCustomerId", "coreCustomerId");
        map.put("featureId", "featureId");
        map.put("actionId", "Action_id");
        map.put("accountId", "Account_id");
        map.put("isAllowed", "isAllowed");
        map.put("policyId", "policyId");
        map.put("limitGroupId", "limitGroupId");
        map.put("limitTypeId", "LimitType_id");
        map.put("value", "value");
        map.put("createdby", "createdby");
        map.put("modifiedby", "modifiedby");
        map.put("createdts", "createdts");
        map.put("lastmodifiedts", "lastmodifiedts");
        map.put("synctimestamp", "synctimestamp");
        map.put("softdeleteflag", "softdeleteflag");

        dtoObjectPropertyMappings.put(CustomerActionDTO.class, map);
    }
}