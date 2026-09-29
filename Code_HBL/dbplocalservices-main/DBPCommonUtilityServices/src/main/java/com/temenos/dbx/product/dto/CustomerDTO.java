package com.temenos.dbx.product.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.dbx.product.api.DBPDTOEXT;
import com.temenos.dbx.product.utils.DTOUtils;

public class CustomerDTO implements DBPDTOEXT {

    /**
     * 
     */
    private static final long serialVersionUID = 2492515660704055991L;

    private String id;
    private String classification_id;
    private String customerType_id;
    private boolean isCombinedUser;
    private String firstName;
    private String middleName;
    private String lastName;
    private String fullName;
    private String status_id;
    private String userName;
    private String password;
    private String unsuccessfulLoginAttempts;
    private int lockCount;
    private String organization_Id;
    private String salutation;
    private String gender;
    private String dateOfBirth;
    private String LegalEntityId;
    private String drivingLicenseNumber;
    private String ssn;
    private String cvv;
    private String token;
    private String pin;
    private String preferredContactMethod;
    private String preferredContactTime;
    private String maritalStatus_id;
    private String spouseName;
    private String noOfDependents;
    private String employementStatus_id;
    private String userCompany;
    private String securityImage_id;
    private String location_id;
    private Boolean isOlbAllowed;
    private Boolean isStaffMember;
    private String countryCode;
    private String userImage;
    private String userImageURL;
    private String olbEnrolmentStatus_id;
    private String otp;
    private String preferedOtpMethod;
    private String otpGenaratedts;
    private String validDate;
    private Boolean isUserAccountLocked;
    private Boolean isPinSet;
    private Boolean isEnrolledForOlb;
    private Boolean isAssistConsented;
    private Boolean isPhoneEnabled;
    private Boolean isEmailEnabled;
    private Boolean isEnrolled;
    private Boolean isSuperAdmin;
    private String currentLoginTime;
    private String lastlogintime;
    private String iDType_id;
    private String iDValue;
    private String iDState;
    private String iDCountry;
    private String iDIssueDate;
    private String iDExpiryDate;
    private Boolean isCoreIdentityScope;
    private Boolean is_MemberEligibile;
    private String memberEligibilityData;
    private Boolean is_BBOA;
    private String creditUnionMemberSince;
    private String ationProfile_id;
    private String registrationLink;
    private String regLinkResendCount;
    private String regLinkValidity;
    private String areDepositTermsAccepted;
    private String areAccountStatementTermsAccepted;
    private String areUserAlertsTurnedOn;
    private Boolean isBillPaySupported;
    private Boolean isBillPayActivated;
    private Boolean isP2PSupported;
    private Boolean isP2PActivated;
    private Boolean isWireTransferEligible;
    private Boolean isWireTransferActivated;
    private String lockedOn;
    private Boolean isEagreementSigned;
    private String mothersMaidenName;
    private String addressValidationStatus;
    private String Product;
    private String eligbilityCriteria;
    private String reason;
    private String applicantChannel;
    private String documentsSubmitted;
    private String createdby;
    private String modifiedby;
    private String createdts;
    private String lastmodifiedts;
    private String synctimestamp;
    private Boolean softdeleteflag;
    private String bank_id;
    private String session_id;
    private String maritalStatus;
    private String spouseFirstName;
    private String spouseLastName;
    private String employmentInfo;
    private Boolean isEngageProvisioned;
    private String defaultLanguage;
    private Boolean isVIPCustomer;
    private String organizationType;
    private String taxID;
    private String street;
    private String isEnrolledFromSpotlight;
    private List<CustomerCommunicationDTO> customerCommuncation;
    private List<CustomerAddressDTO> customerAddress;
    private List<CustomerEmploymentDetailsDTO> employmentDetails;
    private List<CustomerExpensesDTO> customerExpensesDTOs;
    private List<OtherSourceOfIncome> otherSourceOfIncomes;
    private List<CustomerFlagStatus> customerFlagStatus;
    private List<BackendIdentifierDTO> backendIdentifiers;
    private String SFDC_accountId;
    private boolean isNew;
    private boolean isChanged;
    private String source;
    private String operation;
    private String detailToBeUpdated;
    private String communicationIDToBeDeleted;
    private String addressIDToBeDeleted;
    private String title;
    private String companyLegalUnit;
    private String homeLegalEntity;
    private String defaultLegalEntity;
    private String isQRPaymentActivated;
    private String sbaEnrolmentStatus;

	public String getSbaEnrolmentStatus() {
		return sbaEnrolmentStatus;
	}

	public void setSbaEnrolmentStatus(String sbaEnrolmentStatus) {
		this.sbaEnrolmentStatus = sbaEnrolmentStatus;
	}

	private String applicationId;
	
	public String getIsQRPaymentActivated() {
		return isQRPaymentActivated;
	}

	public void setIsQRPaymentActivated(String isQRPaymentActivated) {
		this.isQRPaymentActivated = isQRPaymentActivated;
	}

    /**
     * @return the isChanged
     */
    public Boolean getIsChanged() {
        return isChanged;
    }

    /**
     * @param isChanged
     *            the isChanged to set
     */
    public void setIsChanged(Boolean isChanged) {
        this.isChanged = isChanged;
    }

    /**
     * @return the isNew
     */
    public Boolean getIsNew() {
        return isNew;
    }
    public String getCompanyLegalUnit() {
        return companyLegalUnit;
    }

    public void setCompanyLegalUnit(String companyLegalUnit) {
        this.companyLegalUnit = companyLegalUnit;
    }

    /**
     * @param isNew
     *            the isNew to set
     */
    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public CustomerDTO() {

    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the classification_id
     */
    public String getClassification_id() {
        return classification_id;
    }

    /**
     * @param classification_id
     *            the classification_id to set
     */
    public void setClassification_id(String classification_id) {
        this.classification_id = classification_id;
    }

    /**
     * @return the customerType_id
     */
    public String getCustomerType_id() {
        return customerType_id;
    }

    /**
     * @param customerType_id
     *            the customerType_id to set
     */
    public void setCustomerType_id(String customerType_id) {
        this.customerType_id = customerType_id;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName
     *            the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    /**
     * @return the application ID
     */
    public String getApplicationID() {
        return applicationId;
    }

    /**
     * @param application ID
     *            the application ID to set
     */
    public void setApplicationID(String applicationId) {
        this.applicationId = applicationId;
    }

    /**
     * @return the middleName
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * @param middleName
     *            the middleName to set
     */
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName
     *            the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @param fullName
     *            the fullName to set
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * @return the status_id
     */
    public String getStatus_id() {
        return status_id;
    }

    /**
     * @param status_id
     *            the status_id to set
     */
    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName
     *            the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     *            the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the unsuccessfulLoginAttempts
     */
    public String getUnsuccessfulLoginAttempts() {
        return unsuccessfulLoginAttempts;
    }

    /**
     * @param unsuccessfulLoginAttempts
     *            the unsuccessfulLoginAttempts to set
     */
    public void setUnsuccessfulLoginAttempts(String unsuccessfulLoginAttempts) {
        this.unsuccessfulLoginAttempts = unsuccessfulLoginAttempts;
    }

    /**
     * @return the lockCount
     */
    public int getLockCount() {
        return lockCount;
    }

    /**
     * @param lockCount
     *            the lockCount to set
     */
    public void setLockCount(int lockCount) {
        this.lockCount = lockCount;
    }

    /**
     * @return the organization_Id
     */
    public String getOrganization_Id() {
        return organization_Id;
    }

    /**
     * @param organization_Id
     *            the organization_Id to set
     */
    public void setOrganization_Id(String organization_Id) {
        this.organization_Id = organization_Id;
    }

    /**
     * @return the salutation
     */
    public String getSalutation() {
        return salutation;
    }

    /**
     * @param salutation
     *            the salutation to set
     */
    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    /**
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender
     *            the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return the dateOfBirth
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * @param dateOfBirth
     *            the dateOfBirth to set
     */
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    /**
     * @return the LegalEntityId
     */
    
    public String getLegalEntityId() {
        return LegalEntityId;
    }

    /**
     * @param LegalEntityId
     *            the LegalEntityId to set
     */
    public void setLegalEntityId(String LegalEntityId) {
        this.LegalEntityId = LegalEntityId;
    }
    

    /**
     * @return the drivingLicenseNumber
     */
    public String getDrivingLicenseNumber() {
        return drivingLicenseNumber;
    }

    /**
     * @param drivingLicenseNumber
     *            the drivingLicenseNumber to set
     */
    public void setDrivingLicenseNumber(String drivingLicenseNumber) {
        this.drivingLicenseNumber = drivingLicenseNumber;
    }

    /**
     * @return the ssn
     */
    public String getSsn() {
        return ssn;
    }

    /**
     * @param ssn
     *            the ssn to set
     */
    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    /**
     * @return the cvv
     */
    public String getCvv() {
        return cvv;
    }

    /**
     * @param cvv
     *            the cvv to set
     */
    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token
     *            the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return the pin
     */
    public String getPin() {
        return pin;
    }

    /**
     * @param pin
     *            the pin to set
     */
    public void setPin(String pin) {
        this.pin = pin;
    }

    /**
     * @return the preferredContactMethod
     */
    public String getPreferredContactMethod() {
        return preferredContactMethod;
    }

    /**
     * @param preferredContactMethod
     *            the preferredContactMethod to set
     */
    public void setPreferredContactMethod(String preferredContactMethod) {
        this.preferredContactMethod = preferredContactMethod;
    }

    /**
     * @return the preferredContactTime
     */
    public String getPreferredContactTime() {
        return preferredContactTime;
    }

    /**
     * @param preferredContactTime
     *            the preferredContactTime to set
     */
    public void setPreferredContactTime(String preferredContactTime) {
        this.preferredContactTime = preferredContactTime;
    }

    /**
     * @return the maritalStatus_id
     */
    public String getMaritalStatus_id() {
        return maritalStatus_id;
    }

    /**
     * @param maritalStatus_id
     *            the maritalStatus_id to set
     */
    public void setMaritalStatus_id(String maritalStatus_id) {
        this.maritalStatus_id = maritalStatus_id;
    }

    /**
     * @return the spouseName
     */
    public String getSpouseName() {
        return spouseName;
    }

    /**
     * @param spouseName
     *            the spouseName to set
     */
    public void setSpouseName(String spouseName) {
        this.spouseName = spouseName;
    }

    /**
     * @return the noOfDependents
     */
    public int getNoOfDependents() {
        if (noOfDependents != null) {
            return Integer.parseInt(noOfDependents);
        } else
            return 0;
    }

    /**
     * @param noOfDependents
     *            the noOfDependents to set
     */
    public void setNoOfDependents(String noOfDependents) {
        this.noOfDependents = noOfDependents;
    }

    /**
     * @return the employementStatus_id
     */
    public String getEmployementStatus_id() {
        return employementStatus_id;
    }

    /**
     * @param employementStatus_id
     *            the employementStatus_id to set
     */
    public void setEmployementStatus_id(String employementStatus_id) {
        this.employementStatus_id = employementStatus_id;
    }

    /**
     * @return the userCompany
     */
    public String getUserCompany() {
        return userCompany;
    }

    /**
     * @param userCompany
     *            the userCompany to set
     */
    public void setUserCompany(String userCompany) {
        this.userCompany = userCompany;
    }

    /**
     * @return the securityImage_id
     */
    public String getSecurityImage_id() {
        return securityImage_id;
    }

    /**
     * @param securityImage_id
     *            the securityImage_id to set
     */
    public void setSecurityImage_id(String securityImage_id) {
        this.securityImage_id = securityImage_id;
    }

    /**
     * @return the location_id
     */
    public String getLocation_id() {
        return location_id;
    }

    /**
     * @param location_id
     *            the location_id to set
     */
    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    /**
     * @return the isOlbAllowed
     */
    public Boolean getIsOlbAllowed() {
        return isOlbAllowed;
    }

    /**
     * @param isOlbAllowed
     *            the isOlbAllowed to set
     */
    public void setIsOlbAllowed(Boolean isOlbAllowed) {
        this.isOlbAllowed = isOlbAllowed;
    }

    /**
     * @return the isStaffMember
     */
    public Boolean getIsStaffMember() {
        return isStaffMember;
    }

    /**
     * @param isStaffMember
     *            the isStaffMember to set
     */
    public void setIsStaffMember(Boolean isStaffMember) {
        this.isStaffMember = isStaffMember;
    }

    /**
     * @return the countryCode
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * @param countryCode
     *            the countryCode to set
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * @return the userImage
     */
    public String getUserImage() {
        return userImage;
    }

    /**
     * @param userImage
     *            the userImage to set
     */
    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    /**
     * @return the userImageURL
     */
    public String getUserImageURL() {
        return userImageURL;
    }

    /**
     * @param userImageURL
     *            the userImageURL to set
     */
    public void setUserImageURL(String userImageURL) {
        this.userImageURL = userImageURL;
    }

    /**
     * @return the olbEnrolmentStatus_id
     */
    public String getOlbEnrolmentStatus_id() {
        return olbEnrolmentStatus_id;
    }

    /**
     * @param olbEnrolmentStatus_id
     *            the olbEnrolmentStatus_id to set
     */
    public void setOlbEnrolmentStatus_id(String olbEnrolmentStatus_id) {
        this.olbEnrolmentStatus_id = olbEnrolmentStatus_id;
    }

    /**
     * @return the otp
     */
    public String getOtp() {
        return otp;
    }

    /**
     * @param otp
     *            the otp to set
     */
    public void setOtp(String otp) {
        this.otp = otp;
    }

    /**
     * @return the preferedOtpMethod
     */
    public String getPreferedOtpMethod() {
        return preferedOtpMethod;
    }

    /**
     * @param preferedOtpMethod
     *            the preferedOtpMethod to set
     */
    public void setPreferedOtpMethod(String preferedOtpMethod) {
        this.preferedOtpMethod = preferedOtpMethod;
    }

    /**
     * @return the otpGenaratedts
     */
    public String getOtpGenaratedts() {
        return otpGenaratedts;
    }

    /**
     * @param otpGenaratedts
     *            the otpGenaratedts to set
     */
    public void setOtpGenaratedts(String otpGenaratedts) {
        this.otpGenaratedts = otpGenaratedts;
    }

    /**
     * @return the validDate
     */
    public String getValidDate() {
        return validDate;
    }

    /**
     * @param validDate
     *            the validDate to set
     */
    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    /**
     * @return the isUserAccountLocked
     */
    public Boolean getIsUserAccountLocked() {
        return isUserAccountLocked;
    }

    /**
     * @param isUserAccountLocked
     *            the isUserAccountLocked to set
     */
    public void setIsUserAccountLocked(Boolean isUserAccountLocked) {
        this.isUserAccountLocked = isUserAccountLocked;
    }

    /**
     * @return the isPinSet
     */
    public Boolean getIsPinSet() {
        return isPinSet;
    }

    /**
     * @param isPinSet
     *            the isPinSet to set
     */
    public void setIsPinSet(Boolean isPinSet) {
        this.isPinSet = isPinSet;
    }

    /**
     * @return the isEnrolledForOlb
     */
    public Boolean getIsEnrolledForOlb() {
        return isEnrolledForOlb;
    }

    /**
     * @param isEnrolledForOlb
     *            the isEnrolledForOlb to set
     */
    public void setIsEnrolledForOlb(Boolean isEnrolledForOlb) {
        this.isEnrolledForOlb = isEnrolledForOlb;
    }

    /**
     * @return the isAssistConsented
     */
    public Boolean getIsAssistConsented() {
        return isAssistConsented;
    }

    /**
     * @param isAssistConsented
     *            the isAssistConsented to set
     */
    public void setIsAssistConsented(Boolean isAssistConsented) {
        this.isAssistConsented = isAssistConsented;
    }

    /**
     * @return the isPhoneEnabled
     */
    public Boolean getIsPhoneEnabled() {
        return isPhoneEnabled;
    }

    /**
     * @param isPhoneEnabled
     *            the isPhoneEnabled to set
     */
    public void setIsPhoneEnabled(Boolean isPhoneEnabled) {
        this.isPhoneEnabled = isPhoneEnabled;
    }

    /**
     * @return the isEmailEnabled
     */
    public Boolean getIsEmailEnabled() {
        return isEmailEnabled;
    }

    /**
     * @param isEmailEnabled
     *            the isEmailEnabled to set
     */
    public void setIsEmailEnabled(Boolean isEmailEnabled) {
        this.isEmailEnabled = isEmailEnabled;
    }

    /**
     * @return the isEnrolled
     */
    public Boolean getIsEnrolled() {
        return isEnrolled;
    }

    /**
     * @param isEnrolled
     *            the isEnrolled to set
     */
    public void setIsEnrolled(Boolean isEnrolled) {
        this.isEnrolled = isEnrolled;
    }

    /**
     * @return the isSuperAdmin
     */
    public Boolean getIsSuperAdmin() {
        return isSuperAdmin;
    }

    /**
     * @param isSuperAdmin
     *            the isSuperAdmin to set
     */
    public void setIsSuperAdmin(Boolean isSuperAdmin) {
        this.isSuperAdmin = isSuperAdmin;
    }

    /**
     * @return the currentLoginTime
     */
    public String getCurrentLoginTime() {
        return currentLoginTime;
    }

    /**
     * @param currentLoginTime
     *            the currentLoginTime to set
     */
    public void setCurrentLoginTime(String currentLoginTime) {
        this.currentLoginTime = currentLoginTime;
    }

    /**
     * @return the lastlogintime
     */
    public String getLastlogintime() {
        return lastlogintime;
    }

    /**
     * @param lastlogintime
     *            the lastlogintime to set
     */
    public void setLastlogintime(String lastlogintime) {
        this.lastlogintime = lastlogintime;
    }

    /**
     * @return the iDType_id
     */
    public String getiDType_id() {
        return iDType_id;
    }

    /**
     * @param iDType_id
     *            the iDType_id to set
     */
    public void setiDType_id(String iDType_id) {
        this.iDType_id = iDType_id;
    }

    /**
     * @return the iDValue
     */
    public String getiDValue() {
        return iDValue;
    }

    /**
     * @param iDValue
     *            the iDValue to set
     */
    public void setiDValue(String iDValue) {
        this.iDValue = iDValue;
    }

    /**
     * @return the iDState
     */
    public String getiDState() {
        return iDState;
    }

    /**
     * @param iDState
     *            the iDState to set
     */
    public void setiDState(String iDState) {
        this.iDState = iDState;
    }

    /**
     * @return the iDCountry
     */
    public String getiDCountry() {
        return iDCountry;
    }

    /**
     * @param iDCountry
     *            the iDCountry to set
     */
    public void setiDCountry(String iDCountry) {
        this.iDCountry = iDCountry;
    }

    /**
     * @return the iDIssueDate
     */
    public String getiDIssueDate() {
        return iDIssueDate;
    }

    /**
     * @param iDIssueDate
     *            the iDIssueDate to set
     */
    public void setiDIssueDate(String iDIssueDate) {
        this.iDIssueDate = iDIssueDate;
    }

    /**
     * @return the iDExpiryDate
     */
    public String getiDExpiryDate() {
        return iDExpiryDate;
    }

    /**
     * @param iDExpiryDate
     *            the iDExpiryDate to set
     */
    public void setiDExpiryDate(String iDExpiryDate) {
        this.iDExpiryDate = iDExpiryDate;
    }

    /**
     * @return the isCoreIdentityScope
     */
    public Boolean getIsCoreIdentityScope() {
        return isCoreIdentityScope;
    }

    /**
     * @param isCoreIdentityScope
     *            the isCoreIdentityScope to set
     */
    public void setIsCoreIdentityScope(Boolean isCoreIdentityScope) {
        this.isCoreIdentityScope = isCoreIdentityScope;
    }

    /**
     * @return the is_MemberEligibile
     */
    public Boolean getIs_MemberEligibile() {
        return is_MemberEligibile;
    }

    /**
     * @param is_MemberEligibile
     *            the is_MemberEligibile to set
     */
    public void setIs_MemberEligibile(Boolean is_MemberEligibile) {
        this.is_MemberEligibile = is_MemberEligibile;
    }

    /**
     * @return the memberEligibilityData
     */
    public String getMemberEligibilityData() {
        return memberEligibilityData;
    }

    /**
     * @param memberEligibilityData
     *            the memberEligibilityData to set
     */
    public void setMemberEligibilityData(String memberEligibilityData) {
        this.memberEligibilityData = memberEligibilityData;
    }

    /**
     * @return the is_BBOA
     */
    public Boolean getIs_BBOA() {
        return is_BBOA;
    }

    /**
     * @param is_BBOA
     *            the is_BBOA to set
     */
    public void setIs_BBOA(Boolean is_BBOA) {
        this.is_BBOA = is_BBOA;
    }

    /**
     * @return the creditUnionMemberSince
     */
    public String getCreditUnionMemberSince() {
        return creditUnionMemberSince;
    }

    /**
     * @param creditUnionMemberSince
     *            the creditUnionMemberSince to set
     */
    public void setCreditUnionMemberSince(String creditUnionMemberSince) {
        this.creditUnionMemberSince = creditUnionMemberSince;
    }

    /**
     * @return the ationProfile_id
     */
    public String getAtionProfile_id() {
        return ationProfile_id;
    }

    /**
     * @param ationProfile_id
     *            the ationProfile_id to set
     */
    public void setAtionProfile_id(String ationProfile_id) {
        this.ationProfile_id = ationProfile_id;
    }

    /**
     * @return the registrationLink
     */
    public String getRegistrationLink() {
        return registrationLink;
    }

    /**
     * @param registrationLink
     *            the registrationLink to set
     */
    public void setRegistrationLink(String registrationLink) {
        this.registrationLink = registrationLink;
    }

    /**
     * @return the regLinkResendCount
     */
    public String getRegLinkResendCount() {
        return regLinkResendCount;
    }

    /**
     * @param regLinkResendCount
     *            the regLinkResendCount to set
     */
    public void setRegLinkResendCount(String regLinkResendCount) {
        this.regLinkResendCount = regLinkResendCount;
    }

    /**
     * @return the regLinkValidity
     */
    public String getRegLinkValidity() {
        return regLinkValidity;
    }

    /**
     * @param regLinkValidity
     *            the regLinkValidity to set
     */
    public void setRegLinkValidity(String regLinkValidity) {
        this.regLinkValidity = regLinkValidity;
    }

    /**
     * @return the areDepositTermsAccepted
     */
    public String getAreDepositTermsAccepted() {
        return areDepositTermsAccepted;
    }

    /**
     * @param areDepositTermsAccepted
     *            the areDepositTermsAccepted to set
     */
    public void setAreDepositTermsAccepted(String areDepositTermsAccepted) {
        this.areDepositTermsAccepted = areDepositTermsAccepted;
    }

    /**
     * @return the areAccountStatementTermsAccepted
     */
    public String getAreAccountStatementTermsAccepted() {
        return areAccountStatementTermsAccepted;
    }

    /**
     * @param areAccountStatementTermsAccepted
     *            the areAccountStatementTermsAccepted to set
     */
    public void setAreAccountStatementTermsAccepted(String areAccountStatementTermsAccepted) {
        this.areAccountStatementTermsAccepted = areAccountStatementTermsAccepted;
    }

    /**
     * @return the areUserAlertsTurnedOn
     */
    public String getAreUserAlertsTurnedOn() {
        return areUserAlertsTurnedOn;
    }

    /**
     * @param areUserAlertsTurnedOn
     *            the areUserAlertsTurnedOn to set
     */
    public void setAreUserAlertsTurnedOn(String areUserAlertsTurnedOn) {
        this.areUserAlertsTurnedOn = areUserAlertsTurnedOn;
    }

    /**
     * @return the isBillPaySupported
     */
    public Boolean getIsBillPaySupported() {
        return isBillPaySupported;
    }

    /**
     * @param isBillPaySupported
     *            the isBillPaySupported to set
     */
    public void setIsBillPaySupported(Boolean isBillPaySupported) {
        this.isBillPaySupported = isBillPaySupported;
    }

    /**
     * @return the isBillPayActivated
     */
    public Boolean getIsBillPayActivated() {
        return isBillPayActivated;
    }

    /**
     * @param isBillPayActivated
     *            the isBillPayActivated to set
     */
    public void setIsBillPayActivated(Boolean isBillPayActivated) {
        this.isBillPayActivated = isBillPayActivated;
    }

    /**
     * @return the isP2PSupported
     */
    public Boolean getIsP2PSupported() {
        return isP2PSupported;
    }

    /**
     * @param isP2PSupported
     *            the isP2PSupported to set
     */
    public void setIsP2PSupported(Boolean isP2PSupported) {
        this.isP2PSupported = isP2PSupported;
    }

    /**
     * @return the isP2PActivated
     */
    public Boolean getIsP2PActivated() {
        return isP2PActivated;
    }

    /**
     * @param isP2PActivated
     *            the isP2PActivated to set
     */
    public void setIsP2PActivated(Boolean isP2PActivated) {
        this.isP2PActivated = isP2PActivated;
    }

    /**
     * @return the isWireTransferEligible
     */
    public Boolean getIsWireTransferEligible() {
        return isWireTransferEligible;
    }

    /**
     * @param isWireTransferEligible
     *            the isWireTransferEligible to set
     */
    public void setIsWireTransferEligible(Boolean isWireTransferEligible) {
        this.isWireTransferEligible = isWireTransferEligible;
    }

    /**
     * @return the isWireTransferActivated
     */
    public Boolean getIsWireTransferActivated() {
        return isWireTransferActivated;
    }

    /**
     * @param isWireTransferActivated
     *            the isWireTransferActivated to set
     */
    public void setIsWireTransferActivated(Boolean isWireTransferActivated) {
        this.isWireTransferActivated = isWireTransferActivated;
    }

    /**
     * @return the lockedOn
     */
    public String getLockedOn() {
        return lockedOn;
    }

    /**
     * @param lockedOn
     *            the lockedOn to set
     */
    public void setLockedOn(String lockedOn) {
        this.lockedOn = lockedOn;
    }

    /**
     * @return the isEagreementSigned
     */
    public Boolean getIsEagreementSigned() {
        return isEagreementSigned;
    }

    /**
     * @param isEagreementSigned
     *            the isEagreementSigned to set
     */
    public void setIsEagreementSigned(Boolean isEagreementSigned) {
        this.isEagreementSigned = isEagreementSigned;
    }

    /**
     * @return the mothersMaidenName
     */
    public String getMothersMaidenName() {
        return mothersMaidenName;
    }

    /**
     * @param mothersMaidenName
     *            the mothersMaidenName to set
     */
    public void setMothersMaidenName(String mothersMaidenName) {
        this.mothersMaidenName = mothersMaidenName;
    }

    /**
     * @return the addressValidationStatus
     */
    public String getAddressValidationStatus() {
        return addressValidationStatus;
    }

    /**
     * @param addressValidationStatus
     *            the addressValidationStatus to set
     */
    public void setAddressValidationStatus(String addressValidationStatus) {
        this.addressValidationStatus = addressValidationStatus;
    }

    /**
     * @return the product
     */
    public String getProduct() {
        return Product;
    }

    /**
     * @param product
     *            the product to set
     */
    public void setProduct(String product) {
        Product = product;
    }

    /**
     * @return the eligbilityCriteria
     */
    public String getEligbilityCriteria() {
        return eligbilityCriteria;
    }

    /**
     * @param eligbilityCriteria
     *            the eligbilityCriteria to set
     */
    public void setEligbilityCriteria(String eligbilityCriteria) {
        this.eligbilityCriteria = eligbilityCriteria;
    }

    /**
     * @return the reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * @param reason
     *            the reason to set
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * @return the applicantChannel
     */
    public String getApplicantChannel() {
        return applicantChannel;
    }

    /**
     * @param applicantChannel
     *            the applicantChannel to set
     */
    public void setApplicantChannel(String applicantChannel) {
        this.applicantChannel = applicantChannel;
    }

    /**
     * @return the documentsSubmitted
     */
    public String getDocumentsSubmitted() {
        return documentsSubmitted;
    }

    /**
     * @param documentsSubmitted
     *            the documentsSubmitted to set
     */
    public void setDocumentsSubmitted(String documentsSubmitted) {
        this.documentsSubmitted = documentsSubmitted;
    }

    /**
     * @return the createdby
     */
    public String getCreatedby() {
        return createdby;
    }

    /**
     * @param createdby
     *            the createdby to set
     */
    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    /**
     * @return the modifiedby
     */
    public String getModifiedby() {
        return modifiedby;
    }

    /**
     * @param modifiedby
     *            the modifiedby to set
     */
    public void setModifiedby(String modifiedby) {
        this.modifiedby = modifiedby;
    }

    /**
     * @return the createdts
     */
    public String getCreatedts() {
        return createdts;
    }

    /**
     * @param createdts
     *            the createdts to set
     */
    public void setCreatedts(String createdts) {
        this.createdts = createdts;
    }

    /**
     * @return the lastmodifiedts
     */
    public String getLastmodifiedts() {
        return lastmodifiedts;
    }

    /**
     * @param lastmodifiedts
     *            the lastmodifiedts to set
     */
    public void setLastmodifiedts(String lastmodifiedts) {
        this.lastmodifiedts = lastmodifiedts;
    }

    /**
     * @return the synctimestamp
     */
    public String getSynctimestamp() {
        return synctimestamp;
    }

    /**
     * @param synctimestamp
     *            the synctimestamp to set
     */
    public void setSynctimestamp(String synctimestamp) {
        this.synctimestamp = synctimestamp;
    }

    /**
     * @return the softdeleteflag
     */
    public Boolean getSoftdeleteflag() {
        return softdeleteflag;
    }

    /**
     * @param softdeleteflag
     *            the softdeleteflag to set
     */
    public void setSoftdeleteflag(Boolean softdeleteflag) {
        this.softdeleteflag = softdeleteflag;
    }

    /**
     * @return the bank_id
     */
    public String getBank_id() {
        return bank_id;
    }

    /**
     * @param bank_id
     *            the bank_id to set
     */
    public void setBank_id(String bank_id) {
        this.bank_id = bank_id;
    }

    /**
     * @return the session_id
     */
    public String getSession_id() {
        return session_id;
    }

    /**
     * @param session_id
     *            the session_id to set
     */
    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    /**
     * @return the maritalStatus
     */
    public String getMaritalStatus() {
        return maritalStatus;
    }

    /**
     * @param maritalStatus
     *            the maritalStatus to set
     */
    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    /**
     * @return the spouseFirstName
     */
    public String getSpouseFirstName() {
        return spouseFirstName;
    }

    /**
     * @param spouseFirstName
     *            the spouseFirstName to set
     */
    public void setSpouseFirstName(String spouseFirstName) {
        this.spouseFirstName = spouseFirstName;
    }

    /**
     * @return the spouseLastName
     */
    public String getSpouseLastName() {
        return spouseLastName;
    }

    /**
     * @param spouseLastName
     *            the spouseLastName to set
     */
    public void setSpouseLastName(String spouseLastName) {
        this.spouseLastName = spouseLastName;
    }

    /**
     * @return the employmentInfo
     */
    public String getEmploymentInfo() {
        return employmentInfo;
    }

    /**
     * @param employmentInfo
     *            the employmentInfo to set
     */
    public void setEmploymentInfo(String employmentInfo) {
        this.employmentInfo = employmentInfo;
    }

    /**
     * @return the isEngageProvisioned
     */
    public Boolean getIsEngageProvisioned() {
        return isEngageProvisioned;
    }

    /**
     * @param isEngageProvisioned
     *            the isEngageProvisioned to set
     */
    public void setIsEngageProvisioned(Boolean isEngageProvisioned) {
        this.isEngageProvisioned = isEngageProvisioned;
    }

    /**
     * @return the defaultLanguage
     */
    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    /**
     * @param defaultLanguage
     *            the defaultLanguage to set
     */
    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    /**
     * @return the isVIPCustomer
     */
    public Boolean getIsVIPCustomer() {
        return isVIPCustomer;
    }

    /**
     * @param isVIPCustomer
     *            the isVIPCustomer to set
     */
    public void setIsVIPCustomer(Boolean isVIPCustomer) {
        this.isVIPCustomer = isVIPCustomer;
    }

    /**
     * @return the customerCommuncation
     */
    public List<CustomerCommunicationDTO> getCustomerCommuncation() {
        return customerCommuncation != null ? customerCommuncation : new ArrayList<CustomerCommunicationDTO>();
    }

    /**
     * @param customerCommuncation
     *            the customerCommuncation to set
     */
    public void setCustomerCommuncation(CustomerCommunicationDTO customerCommuncation) {

        if (this.customerCommuncation == null) {
            this.customerCommuncation = new ArrayList<CustomerCommunicationDTO>();
        }

        this.customerCommuncation.add(customerCommuncation);
    }

    /**
     * @param customerCommuncation
     *            the customerCommuncation to set
     */
    public void setCustomerCommuncations(List<CustomerCommunicationDTO> customerCommuncation) {
        this.customerCommuncation = customerCommuncation;
    }

    /**
     * @return the customerAddress
     */
    public List<CustomerAddressDTO> getCustomerAddress() {
        return customerAddress != null ? customerAddress : new ArrayList<CustomerAddressDTO>();
    }

    /**
     * @param customerAddress
     *            the customerAddress to set
     */
    public void setCustomerAddress(CustomerAddressDTO customerAddress) {

        if (this.customerAddress == null) {
            this.customerAddress = new ArrayList<CustomerAddressDTO>();

        }
        this.customerAddress.add(customerAddress);

    }

    /**
     * @param customerAddress
     *            the customerAddress to set
     */
    public void setCustomerAddresses(List<CustomerAddressDTO> customerAddress) {
        this.customerAddress = customerAddress;
    }

    /**
     * @return the employmentDetails
     */
    public List<CustomerEmploymentDetailsDTO> getEmploymentDetails() {
        return employmentDetails;
    }

    /**
     * @param employmentDetails
     *            the employmentDetails to set
     */
    public void setEmploymentDetails(CustomerEmploymentDetailsDTO employmentDetails) {

        if (this.employmentDetails == null) {
            this.employmentDetails = new ArrayList<CustomerEmploymentDetailsDTO>();
        }
        this.employmentDetails.add(employmentDetails);
    }

    /**
     * @return the customerExpensesDTOs
     */
    public List<CustomerExpensesDTO> getCustomerExpensesDTOs() {
        return customerExpensesDTOs;
    }

    /**
     * @param customerExpensesDTOs
     *            the customerExpensesDTOs to set
     */
    public void setCustomerExpensesDTO(CustomerExpensesDTO customerExpensesDTO) {

        if (this.customerExpensesDTOs == null) {
            this.customerExpensesDTOs = new ArrayList<CustomerExpensesDTO>();
        }
        this.customerExpensesDTOs.add(customerExpensesDTO);
    }

    /**
     * @return the otherSourceOfIncomes
     */
    public List<OtherSourceOfIncome> getOtherSourceOfIncomes() {
        return otherSourceOfIncomes;
    }

    /**
     * @param otherSourceOfIncomes
     *            the otherSourceOfIncomes to set
     */
    public void setOtherSourceOfIncome(OtherSourceOfIncome otherSourceOfIncome) {

        if (this.otherSourceOfIncomes == null) {
            this.otherSourceOfIncomes = new ArrayList<OtherSourceOfIncome>();
        }
        this.otherSourceOfIncomes.add(otherSourceOfIncome);
    }

    /**
     * @return the backendIdentifiers
     */
    public List<BackendIdentifierDTO> getBackendIdentifiers() {
        return backendIdentifiers;
    }

    /**
     * @param backendIdentifiers
     *            the backendIdentifiers to set
     */
    public void setBackendIdentifiers(BackendIdentifierDTO backendIdentifier) {

        if (this.backendIdentifiers == null) {
            this.backendIdentifiers = new ArrayList<BackendIdentifierDTO>();
        }

        backendIdentifiers.add(backendIdentifier);
    }

    @Override
    public boolean persist(Map<String, Object> input, Map<String, Object> headers) {
        if (!isNew && isChanged) {
            return !ServiceCallHelper.invokeServiceAndGetJson(input, headers, URLConstants.CUSTOMER_UPDATE)
                    .has("errmsg");
        }

        if (isNew) {
            return !ServiceCallHelper.invokeServiceAndGetJson(input, headers, URLConstants.CUSTOMER_CREATE)
                    .has("errmsg");
        }

        return true;

    }

    @Override
    public Object loadDTO(String id) {

        String filter = "id" + DBPUtilitiesConstants.EQUAL + id;
        List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();

        DTOUtils.loadDTOListfromDB(exts, filter, URLConstants.CUSTOMER_GET, true, true);

        if (exts != null && exts.size() > 0) {
            return exts.get(0);
        }

        return null;
    }

    @Override
    public Object loadDTO() {

        if (StringUtils.isNotBlank(userName)) {
            String filter = "UserName" + DBPUtilitiesConstants.EQUAL + "'" + userName + "'";
            List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();

            DTOUtils.loadDTOListfromDB(exts, filter, URLConstants.CUSTOMER_GET, true, true);

            if (exts != null && exts.size() > 0) {
                return exts.get(0);
            }
        } else if (StringUtils.isNotBlank(id)) {
            String filter = "id" + DBPUtilitiesConstants.EQUAL + id;
            List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();

            DTOUtils.loadDTOListfromDB(exts, filter, URLConstants.CUSTOMER_GET, true, true);

            if (exts != null && exts.size() > 0) {
                return exts.get(0);
            }

        } else if (StringUtils.isNotBlank(firstName)) {
        	String filter = "FirstName" + DBPUtilitiesConstants.EQUAL + "'" + firstName + "'";
            List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();

            DTOUtils.loadDTOListfromDB(exts, filter, URLConstants.CUSTOMER_GET, true, true);

            if (exts != null && exts.size() > 0) {
                return exts;
            }
        }

        return null;
    }

    /**
     * @return the customerFlagStatus
     */
    public List<CustomerFlagStatus> getCustomerFlagStatus() {
        return customerFlagStatus;
    }

    /**
     * @param customerFlagStatus
     *            the customerFlagStatus to set
     */
    public void setCustomerFlagStatus(CustomerFlagStatus customerFlagStatus) {
        if (this.customerFlagStatus == null) {
            this.customerFlagStatus = new ArrayList<CustomerFlagStatus>();
        }

        this.customerFlagStatus.add(customerFlagStatus);
    }

    /**
     * @return the taxID
     */
    public String getTaxID() {
        return taxID;
    }

    /**
     * @param taxID
     *            the taxID to set
     */
    public void setTaxID(String taxID) {
        this.taxID = taxID;
    }

    /**
     * @return the isCombinedUser
     */
    public boolean isCombinedUser() {
        return isCombinedUser;
    }

    /**
     * @param isCombinedUser
     *            the isCombinedUser to set
     */
    public void setCombinedUser(boolean isCombinedUser) {
        this.isCombinedUser = isCombinedUser;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * @return the isEnrolledFromSpotlight
     */
    public String getIsEnrolledFromSpotlight() {
        return isEnrolledFromSpotlight;
    }

    /**
     * @param isEnrolledFromSpotlight
     *            the isEnrolledFromSpotlight to set
     */
    public void setIsEnrolledFromSpotlight(String isEnrolled) {
        isEnrolledFromSpotlight = isEnrolled;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getDetailToBeUpdated() {
        return detailToBeUpdated;
    }

    public void setDetailToBeUpdated(String detailToBeUpdated) {
        this.detailToBeUpdated = detailToBeUpdated;
    }

    public String getCommunicationIDToBeDeleted() {
        return communicationIDToBeDeleted;
    }

    public void setCommunicationIDToBeDeleted(String communicationIDToBeDeleted) {
        this.communicationIDToBeDeleted = communicationIDToBeDeleted;
    }

    public String getAddressIDToBeDeleted() {
        return addressIDToBeDeleted;
    }

    public void setAddressIDToBeDeleted(String addressIDToBeDeleted) {
        this.addressIDToBeDeleted = addressIDToBeDeleted;
    }

	public String getSalesForceIdentifier() {
		return SFDC_accountId;
	}

	public void setSalesForceIdentifier(String salesForceIdentifier) {
		this.SFDC_accountId = salesForceIdentifier;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHomeLegalEntity() {
		return homeLegalEntity;
	}

	public void setHomeLegalEntity(String homeLegalEntity) {
		this.homeLegalEntity = homeLegalEntity;
	}

	public String getDefaultLegalEntity() {
		return defaultLegalEntity;
	}

	public void setDefaultLegalEntity(String defaultLegalEntity) {
		this.defaultLegalEntity = defaultLegalEntity;
	}
	
}
