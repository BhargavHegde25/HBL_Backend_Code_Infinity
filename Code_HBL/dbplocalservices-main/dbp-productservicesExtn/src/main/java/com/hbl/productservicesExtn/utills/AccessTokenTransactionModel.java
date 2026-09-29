package com.hbl.productservicesExtn.utills;


public class AccessTokenTransactionModel {
    /* MODEL CLASSES */
    public class TokenResponse {

        private String refresh_token;
        private String access_token;
        private String expires_in;
        private String responseCode;
        private String responseMessage;
        private String responseData;
        private String responseErrors;
        private String error;
        private String error_description;

        public String getRefreshToken() {
            return refresh_token;
        }

        public String getAccessToken() {
            return access_token;
        }

        public String getResponseCode() {
            return responseCode;
        }

        public void setResponseCode(String responseCode) {
            this.responseCode = responseCode;
        }

        public String getResponseMessage() {
            return responseMessage;
        }

        public void setResponseMessage(String responseMessage) {
            this.responseMessage = responseMessage;
        }

        public String getResponseData() {
            return responseData;
        }

        public void setResponseData(String responseData) {
            this.responseData = responseData;
        }

        public String getResponseErrors() {
            return responseErrors;
        }

        public void setResponseErrors(String responseErrors) {
            this.responseErrors = responseErrors;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getErrorDescription() {
            return error_description;
        }

        public void setErrorDescription(String error_description) {
            this.error_description = error_description;
        }

        public String getTokenExpiry() {
            return expires_in;
        }
    }

    public class TransactionInfo {

        private String participantCode;
        private String participantService;
        private String countryCode;
        private String requestUniqueId;
        private double amount;
        private double chargeAmount;
        private String purpose;
        private String remarks;
        private String currency;
        private Double conversionRate;
        private String txnType;
        private PayerDetail payerDetail;
        private PayeeDetail payeeDetail;
        private Object props;

        public String getParticipantCode() {
            return participantCode;
        }

        public void setParticipantCode(String participantCode) {
            this.participantCode = participantCode;
        }

        public String getParticipantService() {
            return participantService;
        }

        public void setParticipantService(String participantService) {
            this.participantService = participantService;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getRequestUniqueId() {
            return requestUniqueId;
        }

        public void setRequestUniqueId(String requestUniqueId) {
            this.requestUniqueId = requestUniqueId;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public double getChargeAmount() {
            return chargeAmount;
        }

        public void setChargeAmount(double chargeAmount) {
            this.chargeAmount = chargeAmount;
        }

        public String getPurpose() {
            return purpose;
        }

        public void setPurpose(String purpose) {
            this.purpose = purpose;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public Double getConversionRate() {
            return conversionRate;
        }

        public void setConversionRate(Double conversionRate) {
            this.conversionRate = conversionRate;
        }

        public String getTxnType() {
            return txnType;
        }

        public void setTxnType(String txnType) {
            this.txnType = txnType;
        }

        public PayerDetail getPayerDetail() {
            return payerDetail;
        }

        public void setPayerDetail(PayerDetail payerDetail) {
            this.payerDetail = payerDetail;
        }

        public PayeeDetail getPayeeDetail() {
            return payeeDetail;
        }

        public void setPayeeDetail(PayeeDetail payeeDetail) {
            this.payeeDetail = payeeDetail;
        }

        public Object getProps() {
            return props;
        }

        public void setProps(Object props) {
            this.props = props;
        }

        public class Address {

            private String country;
            private String city;
            private String geoCode;
            private String location;

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getGeoCode() {
                return geoCode;
            }

            public void setGeoCode(String geoCode) {
                this.geoCode = geoCode;
            }

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
            }
        }

        public class DeviceInformation {

            private String mobile;
            private String geoCode;
            private String location;
            private String ip;
            private String os;
            private String teleCom;

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getGeoCode() {
                return geoCode;
            }

            public void setGeoCode(String geoCode) {
                this.geoCode = geoCode;
            }

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
            }

            public String getIp() {
                return ip;
            }

            public void setIp(String ip) {
                this.ip = ip;
            }

            public String getOs() {
                return os;
            }

            public void setOs(String os) {
                this.os = os;
            }

            public String getTeleCom() {
                return teleCom;
            }

            public void setTeleCom(String teleCom) {
                this.teleCom = teleCom;
            }
        }

        public class AccountDetail {

            private String vpa;
            private String accountNumber;
            private String bankCode;
            private String branchCode;
            private String accountType;

            public String getVpa() {
                return vpa;
            }

            public void setVpa(String vpa) {
                this.vpa = vpa;
            }

            public String getAccountNumber() {
                return accountNumber;
            }

            public void setAccountNumber(String accountNumber) {
                this.accountNumber = accountNumber;
            }

            public String getBankCode() {
                return bankCode;
            }

            public void setBankCode(String bankCode) {
                this.bankCode = bankCode;
            }

            public String getBranchCode() {
                return branchCode;
            }

            public void setBranchCode(String branchCode) {
                this.branchCode = branchCode;
            }

            public String getAccountType() {
                return accountType;
            }

            public void setAccountType(String accountType) {
                this.accountType = accountType;
            }
        }

        public class PayerDetail {

            private String name;
            private String type;
            private String accountType;
            private AccountDetail accountDetail;
            private String identificationType;
            private String identificationNumber;
            private Address address;
            private DeviceInformation deviceInformation;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getAccountType() {
                return accountType;
            }

            public void setAccountType(String accountType) {
                this.accountType = accountType;
            }

            public AccountDetail getAccountDetail() {
                return accountDetail;
            }

            public void setAccountDetail(AccountDetail accountDetail) {
                this.accountDetail = accountDetail;
            }

            public String getIdentificationType() {
                return identificationType;
            }

            public void setIdentificationType(String identificationType) {
                this.identificationType = identificationType;
            }

            public String getIdentificationNumber() {
                return identificationNumber;
            }

            public void setIdentificationNumber(String identificationNumber) {
                this.identificationNumber = identificationNumber;
            }

            public Address getAddress() {
                return address;
            }

            public void setAddress(Address address) {
                this.address = address;
            }

            public DeviceInformation getDeviceInformation() {
                return deviceInformation;
            }

            public void setDeviceInformation(DeviceInformation deviceInformation) {
                this.deviceInformation = deviceInformation;
            }
        }

        public class PayeeDetail {

            private String name;
            private String type;
            private String accountType;
            private AccountDetail accountDetail;
            private String payeeRelationShip;
            private String mobileNumber;
            private String email;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getAccountType() {
                return accountType;
            }

            public void setAccountType(String accountType) {
                this.accountType = accountType;
            }

            public AccountDetail getAccountDetail() {
                return accountDetail;
            }

            public void setAccountDetail(AccountDetail accountDetail) {
                this.accountDetail = accountDetail;
            }

            public String getPayeeRelationShip() {
                return payeeRelationShip;
            }

            public void setPayeeRelationShip(String payeeRelationShip) {
                this.payeeRelationShip = payeeRelationShip;
            }

            public String getMobileNumber() {
                return mobileNumber;
            }

            public void setMobileNumber(String mobileNumber) {
                this.mobileNumber = mobileNumber;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }
        }
    }

    public class ConsentInfo {

        private String participantCode;
        private String participantService;
        private String vpaId;
        private String direction;
        private String instrument;
        private String consent;
        private String uniqueTransactingId;
        private String fullName;
        private String documentNumber;
        private String documentType;
        private String issuedDate;
        private String issuedPlace;
        private String bankCode;

        // Constructors, getters, and setters for ConsentInfo class
        public ConsentInfo() {
        }

        public ConsentInfo(String participantCode, String participantService,
                String vpaId, String direction, String instrument,
                String consent, String uniqueTransactingId, String fullName,
                String documentNumber,
                String documentType, String issuedDate, String issuedPlace,
                String bankCode) {
            this.participantCode = participantCode;
            this.participantService = participantService;
            this.vpaId = vpaId;
            this.direction = direction;
            this.instrument = instrument;
            this.consent = consent;
            this.uniqueTransactingId = uniqueTransactingId;
            this.fullName = fullName;
            this.documentNumber = documentNumber;
            this.documentType = documentType;
            this.issuedDate = issuedDate;
            this.issuedPlace = issuedPlace;
            this.bankCode = bankCode;
        }

        public String getParticipantCode() {
            return participantCode;
        }

        public void setParticipantCode(String participantCode) {
            this.participantCode = participantCode;
        }

        public String getParticipantService() {
            return participantService;
        }

        public void setParticipantService(String participantService) {
            this.participantService = participantService;
        }

        public String getVpaId() {
            return vpaId;
        }

        public void setVpaId(String vpaId) {
            this.vpaId = vpaId;
        }

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        public String getInstrument() {
            return instrument;
        }

        public void setInstrument(String instrument) {
            this.instrument = instrument;
        }

        public String getConsent() {
            return consent;
        }

        public void setConsent(String consent) {
            this.consent = consent;
        }

        public String getUniqueTransactingId() {
            return uniqueTransactingId;
        }

        public void setUniqueTransactingId(String uniqueTransactingId) {
            this.uniqueTransactingId = uniqueTransactingId;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getDocumentNumber() {
            return documentNumber;
        }

        public void setDocumentNumber(String documentNumber) {
            this.documentNumber = documentNumber;
        }

        public String getDocumentType() {
            return documentType;
        }

        public void setDocumentType(String documentType) {
            this.documentType = documentType;
        }

        public String getIssuedDate() {
            return issuedDate;
        }

        public void setIssuedDate(String issuedDate) {
            this.issuedDate = issuedDate;
        }

        public String getIssuedPlace() {
            return issuedPlace;
        }

        public void setIssuedPlace(String issuedPlace) {
            this.issuedPlace = issuedPlace;
        }

        public String getBankCode() {
            return bankCode;
        }

        public void setBankCode(String bankCode) {
            this.bankCode = bankCode;
        }
    }

    public class PaymentRequestInfo {

        private String participantCode;
        private String participantService;
        private String orgRequestUniqueId;
        private String endToEndTxnId;
        private double amount;
        private Object props; // This can be adjusted based on the actual data type

        // Constructors for PaymentRequestInfo class
        public PaymentRequestInfo() {
        }

        public PaymentRequestInfo(String participantCode,
                String participantService, String orgRequestUniqueId,
                String endToEndTxnId, double amount, Object props) {
            this.participantCode = participantCode;
            this.participantService = participantService;
            this.orgRequestUniqueId = orgRequestUniqueId;
            this.endToEndTxnId = endToEndTxnId;
            this.amount = amount;
            this.props = props;
        }

        // Getters and setters for PaymentRequestInfo class
        public String getParticipantCode() {
            return participantCode;
        }

        public void setParticipantCode(String participantCode) {
            this.participantCode = participantCode;
        }

        public String getParticipantService() {
            return participantService;
        }

        public void setParticipantService(String participantService) {
            this.participantService = participantService;
        }

        public String getOrgRequestUniqueId() {
            return orgRequestUniqueId;
        }

        public void setOrgRequestUniqueId(String orgRequestUniqueId) {
            this.orgRequestUniqueId = orgRequestUniqueId;
        }

        public String getEndToEndTxnId() {
            return endToEndTxnId;
        }

        public void setEndToEndTxnId(String endToEndTxnId) {
            this.endToEndTxnId = endToEndTxnId;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public Object getProps() {
            return props;
        }

        public void setProps(Object props) {
            this.props = props;
        }
    }

    public class LimitCheckInfo {

        private String vpa;
        private String service;
        private double amount;
        private String instrument;
        private String purposeCode;
        private String mcc;

        public String getVpa() {
            return vpa;
        }

        public void setVpa(String vpa) {
            this.vpa = vpa;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getInstrument() {
            return instrument;
        }

        public void setInstrument(String instrument) {
            this.instrument = instrument;
        }

        public String getPurposeCode() {
            return purposeCode;
        }

        public void setPurposeCode(String purposeCode) {
            this.purposeCode = purposeCode;
        }

        public String getMcc() {
            return mcc;
        }

        public void setMcc(String mcc) {
            this.mcc = mcc;
        }
    }

    /*  might delete later*/
    public class CountryInfo {

        private String countryCode;

        public CountryInfo(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }
    }

    public class PaymentInfo {

        private String vpa;
        private String service;
        private double amount;
        private String instrument;
        private String purposeCode;
        private String mcc;

        public PaymentInfo(String vpa, String service, double amount,
                String instrument, String purposeCode, String mcc) {
            this.vpa = vpa;
            this.service = service;
            this.amount = amount;
            this.instrument = instrument;
            this.purposeCode = purposeCode;
            this.mcc = mcc;
        }

        public String getVpa() {
            return vpa;
        }

        public void setVpa(String vpa) {
            this.vpa = vpa;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getInstrument() {
            return instrument;
        }

        public void setInstrument(String instrument) {
            this.instrument = instrument;
        }

        public String getPurposeCode() {
            return purposeCode;
        }

        public void setPurposeCode(String purposeCode) {
            this.purposeCode = purposeCode;
        }

        public String getMcc() {
            return mcc;
        }

        public void setMcc(String mcc) {
            this.mcc = mcc;
        }
    }

}
