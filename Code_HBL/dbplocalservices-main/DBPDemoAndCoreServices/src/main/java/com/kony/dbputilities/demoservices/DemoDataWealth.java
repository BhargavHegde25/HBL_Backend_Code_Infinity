package com.kony.dbputilities.demoservices;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonObject;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DemoDataWealth {

    private Map<String, String> generatedAccounts;
    private Map<String, String> generatedPayees;
    private Map<String, String> generatedPersons;
    private Map<String, String> generatedBills;
    private Map<String, String> accountTypes;
    private Map<String, String> transactionTypes;
    private Map<String, String> generatedExternalAccounts;
    private String smallOrgId;
    public static String membershipId;
    public static String taxId;
    public static String membershipName;
    private String coreCustomerName;
    private String contractId;
    private String userName;
    private String primaryCoreCustomerId;
    private String secondaryCoreCustomerId;

    public String getCoreCustomerName() {
        return coreCustomerName;
    }

    public void setCoreCustomerName(String coreCustomerName) {
        this.coreCustomerName = coreCustomerName;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPrimaryCoreCustomerId() {
        return primaryCoreCustomerId;
    }

    public void setPrimaryCoreCustomerId(String primaryCoreCustomerId) {
        this.primaryCoreCustomerId = primaryCoreCustomerId;
    }

    public String getSecondaryCoreCustomerId() {
        return secondaryCoreCustomerId;
    }

    public void setSecondaryCoreCustomerId(String secondaryCoreCustomerId) {
        this.secondaryCoreCustomerId = secondaryCoreCustomerId;
    }

    public Map<String, String> getGeneratedPayees() {
        return generatedPayees;
    }

    public void setGeneratedPayees(Map<String, String> generatedPayees) {
        this.generatedPayees = generatedPayees;
    }

    public Map<String, String> getGeneratedPersons() {
        return generatedPersons;
    }

    public void setGeneratedPersons(Map<String, String> generatedPersons) {
        this.generatedPersons = generatedPersons;
    }

    public Map<String, String> getGeneratedBills() {
        return generatedBills;
    }

    public void setGeneratedBills(Map<String, String> generatedBills) {
        this.generatedBills = generatedBills;
    }

    public Map<String, String> getAccountTypes() {
        return accountTypes;
    }

    public void setAccountTypes(Map<String, String> accountTypes) {
        this.accountTypes = accountTypes;
    }

    public Map<String, String> getTransactionTypes() {
        return transactionTypes;
    }

    public void setTransactionTypes(Map<String, String> transactionTypes) {
        this.transactionTypes = transactionTypes;
    }

    public Map<String, String> getGeneratedExternalAccounts() {
        return generatedExternalAccounts;
    }

    public void setGeneratedExternalAccounts(Map<String, String> generatedExternalAccounts) {
        this.generatedExternalAccounts = generatedExternalAccounts;
    }

    public String getSmallOrgId() {
        return smallOrgId;
    }

    public void setSmallOrgId(String smallOrgId) {
        this.smallOrgId = smallOrgId;
    }

    public static String getMembershipId() {
        return membershipId;
    }

    public static void setMembershipId(String membershipId) {
        DemoDataWealth.membershipId = membershipId;
    }

    public static String getTaxId() {
        return taxId;
    }

    public static void setTaxId(String taxId) {
        DemoDataWealth.taxId = taxId;
    }

    public static String getMembershipName() {
        return membershipName;
    }

    public static void setMembershipName(String membershipName) {
        DemoDataWealth.membershipName = membershipName;
    }

    public void setGeneratedAccounts(Map<String, String> generatedAccounts) {
        this.generatedAccounts = generatedAccounts;
    }

    static {
        SimpleDateFormat idFormatter = new SimpleDateFormat("yyMMddHHmmssSSS");
        membershipId = idFormatter.format(new Date());
        taxId = idFormatter.format(new Date());
        membershipName = "org" + membershipId;
    }

    public void addAccountNum(String key, String accountNum) {
        if (null == generatedAccounts) {
            generatedAccounts = new HashMap<>();
        }
        generatedAccounts.put(key, accountNum);
    }

    public String getAccountNum(String key) {
        if (null != generatedAccounts) {
            return generatedAccounts.get(key);
        }
        return null;
    }

    public void addExternalAccountNum(String key, String accountNum) {
        if (null == generatedExternalAccounts) {
            generatedExternalAccounts = new HashMap<>();
        }
        generatedExternalAccounts.put(key, accountNum);
    }

    public String getExternalAccountNum(String key) {
        if (null != generatedExternalAccounts) {
            return generatedExternalAccounts.get(key);
        }
        return null;
    }

    public void addPerson(String key, String personId) {
        if (null == generatedPersons) {
            generatedPersons = new HashMap<>();
        }
        generatedPersons.put(key, personId);
    }

    public String getPerson(String key) {
        if (null != generatedPersons) {
            return generatedPersons.get(key);
        }
        return null;
    }

    public void addPayee(String key, String payeeId) {
        if (null == generatedPayees) {
            generatedPayees = new HashMap<>();
        }
        generatedPayees.put(key, payeeId);
    }

    public String getPayee(String key) {
        if (null != generatedPayees) {
            return generatedPayees.get(key);
        }
        return null;
    }

    public void addBill(String key, String billId) {
        if (null == generatedBills) {
            generatedBills = new HashMap<>();
        }
        generatedBills.put(key, billId);
    }

    public String getBill(String key) {
        if (null != generatedBills) {
            return generatedBills.get(key);
        }
        return null;
    }

    public String getAccountId(String accountType) {
        if (null != accountTypes) {
            return accountTypes.get(accountType);
        }
        return null;
    }

    public String getTransactionId(String transType) {
        if (null != transactionTypes) {
            return transactionTypes.get(transType);
        }
        return null;
    }

    public void init(DataControllerRequest dcRequest) throws HttpCallException {
        this.accountTypes = getAccountTypes(dcRequest, "US");
        this.transactionTypes = getTransTypes(dcRequest);
    }

    public Map<String, String> getGeneratedAccounts() {
        return generatedAccounts;
    }

    public String getFieldValue(Result result, String fieldName) {
        String id = null;
        if (HelperMethods.hasRecords(result)) {
            Dataset ds = result.getAllDatasets().get(0);
            id = ds.getRecord(0).getParam(fieldName).getValue();
        }
        return id;
    }

    private Map<String, String> getAccountTypes(DataControllerRequest dcRequest, String countryCode)
            throws HttpCallException {
        Result accountTypes = HelperMethods.callGetApi(dcRequest, null, HelperMethods.getHeaders(dcRequest),
                URLConstants.ACCOUNTTYPE_GET);
        Map<String, String> accountTypeMap = new HashMap<>();
        List<Record> types = accountTypes.getAllDatasets().get(0).getAllRecords();
        for (Record type : types) {
            accountTypeMap.put(HelperMethods.getFieldValue(type, "TypeDescription"),
                    HelperMethods.getFieldValue(type, "TypeID"));
        }
        return accountTypeMap;
    }

    private Map<String, String> getTransTypes(DataControllerRequest dcRequest) throws HttpCallException {
        Result accountTypes = HelperMethods.callApi(dcRequest, null, HelperMethods.getHeaders(dcRequest),
                URLConstants.TRANSACTION_TYPE_GET);
        Map<String, String> accountTypeMap = new HashMap<>();
        List<Record> types = accountTypes.getAllDatasets().get(0).getAllRecords();
        for (Record type : types) {
            accountTypeMap.put(HelperMethods.getFieldValue(type, "description"),
                    HelperMethods.getFieldValue(type, "Id"));
        }
        return accountTypeMap;
    }

    public JsonObject getValidAccountJsonObject(Map<String, String> inputParams, DemoDataWealth helper) {
        JsonObject account = new JsonObject();
        account.addProperty("accountId", helper.getAccountNum(inputParams.get("AccountName")));
        account.addProperty("accountType", inputParams.get("Type_id"));
        account.addProperty("accountName", inputParams.get("AccountName"));
        account.addProperty("typeId", helper.getAccountTypes().get(inputParams.get("Type_id")));
        account.addProperty("ownerType", "Owner");
        return account;
    }

    public void addDateField(Map<String, String> input, String field) {
        String value = input.get(field);
        if (StringUtils.isNotBlank(value)) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, Integer.parseInt(value));
            input.put(field, getDateString(cal.getTime()));
        } else if (input.containsKey(field)) {
            input.remove(field);
        }
    }

    private String getDateString(Date d) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return formatter.format(d);
    }

    public void insertTransaction(Map<String, String> input, DataControllerRequest dcRequest, DemoDataWealth helper)
            throws HttpCallException {
        input.put("toAccountNumber", helper.getAccountNum(input.get("toAccountNumber")));
        input.put("fromAccountNumber", helper.getAccountNum(input.get("fromAccountNumber")));
        input.put("toExternalAccountNumber", helper.getExternalAccountNum(input.get("toExternalAccountNumber")));
        input.put("Type_id", helper.getTransactionId(input.get("transactionType")));
        input.put("Payee_id", helper.getPayee(input.get("Payee_id")));
        input.put("Person_Id", helper.getPerson(input.get("Person_Id")));
        input.put("Bill_id", helper.getBill(input.get("Bill_id")));
        input.put("transactionCurrency", "USD");
        addDateField(input, "createdDate");
        addDateFieldwithoutTime(input, "transactionDate");
        addDateField(input, "scheduledDate");
        addDateField(input, "frequencyStartDate");
        addDateField(input, "frequencyEndDate");
        insertServiceName(input);
        HelperMethods.removeNullValues(input);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_ACCOUNT_TRANSACTION_CREATE);
    }

    private void insertServiceName(Map<String, String> input) {
        String typeId = input.get("Type_id");
        String serviceName = "";
        if (typeId != null) {
            switch (typeId) {
                case "1":
                    serviceName = "TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE";
                    break;
                case "2":
                    serviceName = "BILL_PAY_CREATE";
                    break;
                case "3":
                    if (input.containsKey("beneficiaryName")) {
                        String beneficiaryName = input.get("beneficiaryName");
                        if ("Alex Sion".equalsIgnoreCase(beneficiaryName)
                                || "Tom Purnell".equalsIgnoreCase(beneficiaryName)
                                || "John Bailey".equalsIgnoreCase(beneficiaryName))
                            serviceName = "INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE";
                        else if ("Dan Markus".equalsIgnoreCase(beneficiaryName))
                            serviceName = "INTRA_BANK_FUND_TRANSFER_CREATE";
                        else if ("Henry James".equalsIgnoreCase(beneficiaryName))
                            serviceName = "INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE";
                        else
                            serviceName = "";
                    }
                    break;
                case "5":
                    serviceName = "P2P_CREATE";
                    break;
                default:
                    serviceName = "";
                    break;
            }
            input.put("serviceName", serviceName);
        }

    }

    private void addDateFieldwithoutTime(Map<String, String> input, String field) {
        String value = input.get(field);
        if (StringUtils.isNotBlank(value)) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, Integer.parseInt(value));
            input.put(field, HelperMethods.getFormattedTimeStamp(cal.getTime(), DemoDataService.FORMAT));
        } else if (input.containsKey(field)) {
            input.remove(field);
        }
    }

    public Map<String, String> getMembershipAddress1() {
        Map<String, String> inputParams = new HashMap<>();
        inputParams.put("id", HelperMethods.getNewId());
        inputParams.put("addressLine1", "1-25, ramnagar, Gajuwaka");
        inputParams.put("addressLine2", "Vizag");
        inputParams.put("cityName", "Visakhapatnam");
        inputParams.put("state", "Andhra Pradesh");
        inputParams.put("country", "India");
        inputParams.put("zipCode", "530012");
        return inputParams;
    }

    public Map<String, String> getMembershipAddress2() {
        Map<String, String> inputParams = new HashMap<>();
        inputParams.put("id", HelperMethods.getNewId());
        inputParams.put("addressLine1", "1-25, ramnagar, Gajuwaka");
        inputParams.put("addressLine2", "Vizag");
        inputParams.put("cityName", "Visakhapatnam");
        inputParams.put("state", "Andhra Pradesh");
        inputParams.put("country", "India");
        inputParams.put("zipCode", "530012");
        return inputParams;
    }

    public Map<String, String> getMembershipDetails1() {
        Map<String, String> inputParams = new HashMap<>();
        inputParams.put("id", "1242426043");
        inputParams.put("name", "Temenos India Pvt Limited");
        inputParams.put("firstName", "Temenos");
        inputParams.put("lastName", "India");
        inputParams.put("dateOfBirth", "1993-05-09");
        inputParams.put("ssn", "9745035942");
        inputParams.put("taxId", "3176435942");
        inputParams.put("phone", "+91-9908059547");
        inputParams.put("email", "temenos.india@temenos.com");
        inputParams.put("addressId", "865cc2fa-4d69-41cd-9c87-225a81c222b8");
        inputParams.put("status", "");
        inputParams.put("industry", "Banking services");
        inputParams.put("isBusinessType", "1");
        inputParams.put("faxId", "123-123");
        return inputParams;
    }

    public Map<String, String> getMembershipDetails2() {
        Map<String, String> inputParams = new HashMap<>();
        inputParams.put("id", "8764626043");
        inputParams.put("name", "Kony India Pvt Limited");
        inputParams.put("firstName", "Kony");
        inputParams.put("lastName", "India");
        inputParams.put("dateOfBirth", "1995-08-09");
        inputParams.put("ssn", "9199035942");
        inputParams.put("taxId", "9303135942");
        inputParams.put("phone", "+91-9912312345");
        inputParams.put("email", "kony.india@temenos.com");
        inputParams.put("addressId", "ca7f54ce-8da1-4604-a0e9-619b8e0e0ece");
        inputParams.put("status", "");
        inputParams.put("industry", "Banking services");
        inputParams.put("isBusinessType", "1");
        inputParams.put("faxId", "123-123");
        return inputParams;
    }

    public Map<String, String> getMembershipDetails6() {
        Map<String, String> inputParams = new HashMap<>();
        inputParams.put("id", "8764626043");
        inputParams.put("name", "Jacob Sebastian");
        inputParams.put("firstName", "Jacob");
        inputParams.put("lastName", "Sebastian");
        inputParams.put("dateOfBirth", "1995-08-09");
        inputParams.put("ssn", "9199035942");
        inputParams.put("taxId", "9303135942");
        inputParams.put("phone", "+91-9912312345");
        inputParams.put("email", "jacob.sebastian@temenos.com");
        inputParams.put("addressId", "ca7f54ce-8da1-4604-a0e9-619b8e0e0ece");
        inputParams.put("status", "");
        inputParams.put("industry", "Banking services");
        inputParams.put("isBusinessType", "1");
        inputParams.put("faxId", "123-123");
        return inputParams;
    }

    public Map<String, String> getMembershipDetails3() {
        Map<String, String> inputParams = new HashMap<>();
        inputParams.put("id", "8764626043");
        inputParams.put("name", "Messi Argentina");
        inputParams.put("firstName", "Messi");
        inputParams.put("lastName", "Argentina");
        inputParams.put("dateOfBirth", "1995-08-09");
        inputParams.put("ssn", "9199035942");
        inputParams.put("taxId", "9303135942");
        inputParams.put("phone", "+91-9912312345");
        inputParams.put("email", "messi.argentina@temenos.com");
        inputParams.put("addressId", "ca7f54ce-8da1-4604-a0e9-619b8e0e0ece");
        inputParams.put("status", "");
        inputParams.put("industry", "Banking services");
        inputParams.put("isBusinessType", "1");
        inputParams.put("faxId", "123-123");
        return inputParams;
    }

    public Map<String, String> getMembershipDetails4() {
        Map<String, String> inputParams = new HashMap<>();
        inputParams.put("id", "8764626043");
        inputParams.put("name", "Micheal Logan");
        inputParams.put("firstName", "Micheal");
        inputParams.put("lastName", "Logan");
        inputParams.put("dateOfBirth", "1995-08-09");
        inputParams.put("ssn", "9199035942");
        inputParams.put("taxId", "9303135942");
        inputParams.put("phone", "+91-9912312345");
        inputParams.put("email", "micheal.logan@temenos.com");
        inputParams.put("addressId", "ca7f54ce-8da1-4604-a0e9-619b8e0e0ece");
        inputParams.put("status", "");
        inputParams.put("industry", "Banking services");
        inputParams.put("isBusinessType", "1");
        inputParams.put("faxId", "123-123");
        return inputParams;
    }

    public Map<String, String> getMembershipDetails5() {
        Map<String, String> inputParams = new HashMap<>();
        inputParams.put("id", "8764626043");
        inputParams.put("name", "Luna Sofia");
        inputParams.put("firstName", "Luna");
        inputParams.put("lastName", "Sofia");
        inputParams.put("dateOfBirth", "1995-08-09");
        inputParams.put("ssn", "9199035942");
        inputParams.put("taxId", "9303135942");
        inputParams.put("phone", "+91-9912312345");
        inputParams.put("email", "luna.sofia@temenos.com");
        inputParams.put("addressId", "ca7f54ce-8da1-4604-a0e9-619b8e0e0ece");
        inputParams.put("status", "");
        inputParams.put("industry", "Banking services");
        inputParams.put("isBusinessType", "1");
        inputParams.put("faxId", "123-123");
        return inputParams;
    }

    public String getDummyAccountId() {
        SimpleDateFormat idformatter = new SimpleDateFormat("yyMMddHHmmssSSS");
        return idformatter.format(new Date());
    }

    public Map<String, String> getUserDemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("addressLine1", "7380,");
        inputParam.put("addressLine2", "West Sand Lake Road,");
        inputParam.put("city", "Orlando");
        inputParam.put("state", "Florida");
        inputParam.put("country", "United States of America");
        inputParam.put("zipcode", "32819");
        inputParam.put("userFirstName", "John");
        inputParam.put("userLastName", "Bailey");
        // inputParam.put("userImageURL",
        // "https://retailbanking1.konycloud.com/dbimages/displayPicture.png");
        inputParam.put("secondaryphone", "+91-8729899218");
        inputParam.put("secondaryphone2", "+91-8729899218");
        inputParam.put("secondaryemail2", "johnb@yahoo.com");
        inputParam.put("phone", "+91-4258303691");
        inputParam.put("email", "john.bailey@gmail.com");
        inputParam.put("ssn", "065452564");
        inputParam.put("areUserAlertsTurnedOn", "0");
        inputParam.put("areDepositTermsAccepted", "0");
        inputParam.put("areAccountStatementTermsAccepted", "0");
        inputParam.put("isPhoneEnabled", "0");
        inputParam.put("isBillPaySupported", "1");
        inputParam.put("isP2PSupported", "1");
        inputParam.put("isBillPayActivated", "1");
        inputParam.put("isP2PActivated", "1");
        inputParam.put("isWireTransferEligible", "1");
        inputParam.put("isWireTransferActivated", "1");
        inputParam.put("isEmailEnabled", "1");
        inputParam.put("showBillPayFromAccPopup", "1");
        inputParam.put("lastlogintime", "2018-08-19");
        inputParam.put("dateOfBirth", "1985-05-05");
        return inputParam;
    }

    public Map<String, String> getCustomerDemoData() {
        Map<String, String> inputParam = new HashMap<>();

        inputParam.put("FirstName", "John");
        inputParam.put("LastName", "Jones");
        inputParam.put("Ssn", "906545256");
        inputParam.put("areUserAlertsTurnedOn", "0");
        inputParam.put("areDepositTermsAccepted", "0");
        inputParam.put("areAccountStatementTermsAccepted", "0");
        inputParam.put("IsPhoneEnabled", "0");
        inputParam.put("isBillPaySupported", "1");
        inputParam.put("isP2PSupported", "1");
        inputParam.put("isBillPayActivated", "1");
        inputParam.put("isP2PActivated", "1");
        inputParam.put("isWireTransferEligible", "1");
        inputParam.put("isWireTransferActivated", "1");
        inputParam.put("IsEmailEnabled", "1");
        inputParam.put("isEagreementSigned", "false");
        inputParam.put("Status_id", "SID_CUS_ACTIVE");

        inputParam.put("Lastlogintime", "2012-02-11");
        inputParam.put("DateOfBirth", "1985-05-05");
        return inputParam;
    }

    public Map<String, String> getWealthCustomerData() {
        Map<String, String> inputParam = new HashMap<>();

        inputParam.put("FirstName", "Jane");
        inputParam.put("LastName", "Gray");
        inputParam.put("Ssn", "9564882376");
        inputParam.put("areUserAlertsTurnedOn", "0");
        inputParam.put("areDepositTermsAccepted", "0");
        inputParam.put("areAccountStatementTermsAccepted", "0");
        inputParam.put("IsPhoneEnabled", "0");
        inputParam.put("isBillPaySupported", "1");
        inputParam.put("isP2PSupported", "1");
        inputParam.put("isBillPayActivated", "1");
        inputParam.put("isP2PActivated", "1");
        inputParam.put("isWireTransferEligible", "1");
        inputParam.put("isWireTransferActivated", "1");
        inputParam.put("IsEmailEnabled", "1");
        inputParam.put("isEagreementSigned", "false");
        inputParam.put("Status_id", "SID_CUS_ACTIVE");
        inputParam.put("Lastlogintime", "2019-03-12");
        inputParam.put("DateOfBirth", "1985-05-05");
        return inputParam;
    }

    public Map<String, String> getCustomerCommunicationDemoData() {
        Map<String, String> inputParam = new HashMap<>();

        inputParam.put("Email", "linda.jones@gmail.com");
        inputParam.put("Phone", "+91-4258303691");
        return inputParam;
    }

    public Map<String, String> getCustomerPreferenceDemoData() {
        Map<String, String> inputParam = new HashMap<>();

        inputParam.put("isBillPaySupported", "1");
        inputParam.put("isP2PSupported", "1");
        inputParam.put("isBillPayActivated", "1");
        inputParam.put("isP2PActivated", "1");
        inputParam.put("isWireTransferEligible", "1");
        inputParam.put("isWireTransferActivated", "1");
        inputParam.put("ShowBillPayFromAccPopup", "1");
        return inputParam;
    }

    public Map<String, String> getAddress1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("Region_id", "US-NY");
        inputParam.put("isPreferredAddress", "1");
        inputParam.put("City_id", "CITY1437");
        inputParam.put("cityName", "Fairfield");
        inputParam.put("addressLine1", "244");
        inputParam.put("addressLine2", "US-46");
        inputParam.put("zipCode", "7004");
        inputParam.put("country", "USA");
        inputParam.put("state", "New Jersey");
        inputParam.put("type", "home");
        return inputParam;
    }

    public Map<String, String> getPhone1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("receivePromotions", "1");
        inputParam.put("isPrimary", "1");
        inputParam.put("phoneNumber", "+91-4258303691");
        inputParam.put("extension", "Mobile");
        inputParam.put("countryType", "Domestic");
        inputParam.put("type", "Mobile");
        return inputParam;
    }

    public Map<String, String> getMembershipData() {
        Map<String, String> input = new HashMap<>();
        input.put("id", HelperMethods.getNumericId() + "");
        input.put("Status_id", "SID_CUS_NEW");
        return input;
    }

    public Map<String, String> getSBOrganisationCommunication() {
        Map<String, String> inputParam = new HashMap<>();

        inputParam.put("Email", "linda.jones@gmail.com");
        inputParam.put("Phone", "+91-5417543010");
        return inputParam;
    }

    public Map<String, String> getAccount1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("Type_id", "Savings");
        inputParam.put("AccountName", "Business Advantage Savings");
        inputParam.put("Name", "Business Advantage Savings");
        inputParam.put("NickName", "Business Savings");
        inputParam.put("OpeningDate", "2000-10-10");
        inputParam.put("closingDate", "");
        inputParam.put("StatusDesc", "Active");
        inputParam.put("FavouriteStatus", "1");
        inputParam.put("AvailableBalance", "510852.74");
        inputParam.put("CurrentBalance", "529061.09");
        inputParam.put("PendingDeposit", "41300");
        inputParam.put("PendingWithdrawal", "23091.65");
        inputParam.put("lastStatementBalance", "");
        inputParam.put("ShowTransactions", "1");
        inputParam.put("SupportBillPay", "1");
        inputParam.put("SupportDeposit", "1");
        inputParam.put("SupportTransferFrom", "1");
        inputParam.put("SupportTransferTo", "1");
        inputParam.put("SupportCardlessCash", "1");
        inputParam.put("TransactionLimit", "10000");
        inputParam.put("TransferLimit", "10000");
        inputParam.put("IsPFM", "1");
        inputParam.put("EStatementmentEnable", "0");
        inputParam.put("AccountPreference", "0");
        inputParam.put("CurrencyCode", "USD");
        inputParam.put("AccountHolder", "John Jones");
        inputParam.put("RoutingNumber", "648721691");
        inputParam.put("SwiftCode", "CTBAUS124");
        inputParam.put("BsbNum", "123456789");
        inputParam.put("JointHolders", "");
        inputParam.put("DividendRate", "0");
        inputParam.put("DividendYTD", "0");
        inputParam.put("LastDividendPaidAmount", "0");
        inputParam.put("lastDividendPaidDate", "");
        inputParam.put("PreviousYearDividend", "0");
        inputParam.put("DividendPaidYTD", "0");
        inputParam.put("DividendLastPaidAmount", "0");
        inputParam.put("dividendLastPaidDate", "");
        inputParam.put("PreviousYearsDividends", "0");
        inputParam.put("availableCredit", "");
        inputParam.put("dueDate", "");
        inputParam.put("minimumDue", "");
        inputParam.put("outstandingBalance", "");
        inputParam.put("creditCardNumber", "");
        inputParam.put("availablePoints", "");
        inputParam.put("currentAmountDue", "");
        inputParam.put("lastPaymentAmount", "");
        inputParam.put("lateFeesDue", "");
        inputParam.put("creditLimit", "");
        inputParam.put("interestRate", "");
        inputParam.put("maturityDate", "");
        inputParam.put("maturityOption", "");
        inputParam.put("bondInterest", "");
        inputParam.put("bondInterestLastYear", "");
        inputParam.put("TotalCreditMonths", "");
        inputParam.put("TotalDebitsMonth", "");
        inputParam.put("interestEarned", "");
        inputParam.put("maturityAmount", "");
        inputParam.put("paymentTerm", "");
        inputParam.put("principalValue", "");
        inputParam.put("paymentDue", "");
        inputParam.put("interestPaidYTD", "");
        inputParam.put("interestPaidPreviousYTD", "");
        inputParam.put("unpaidInterest", "");
        inputParam.put("regularPaymentAmount", "");
        inputParam.put("principalBalance", "");
        inputParam.put("originalAmount", "");
        inputParam.put("payoffAmount", "");
        inputParam.put("payOffCharge", "");
        inputParam.put("interestPaidLastYear", "");
        inputParam.put("bankName", "");
        inputParam.put("AccountHolder2", "John Doe");
        return inputParam;
    }

    public Map<String, String> getAccount2DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("Type_id", "Checking");
        inputParam.put("AccountName", "Progress Business Checking");
        inputParam.put("Name", "Progress Business Checking");
        inputParam.put("NickName", "Business Checking - Pine Brook Branch");
        inputParam.put("OpeningDate", "2001-09-10");
        inputParam.put("closingDate", "");
        inputParam.put("StatusDesc", "Active");
        inputParam.put("FavouriteStatus", "1");
        inputParam.put("AvailableBalance", "30000");
        inputParam.put("CurrentBalance", "17416.91");
        inputParam.put("PendingDeposit", "5916.91");
        inputParam.put("PendingWithdrawal", "18500");
        inputParam.put("lastStatementBalance", "");
        inputParam.put("ShowTransactions", "1");
        inputParam.put("SupportBillPay", "1");
        inputParam.put("SupportDeposit", "1");
        inputParam.put("SupportTransferFrom", "1");
        inputParam.put("SupportTransferTo", "1");
        inputParam.put("SupportCardlessCash", "1");
        inputParam.put("TransactionLimit", "10000");
        inputParam.put("TransferLimit", "10000");
        inputParam.put("IsPFM", "1");
        inputParam.put("EStatementmentEnable", "0");
        inputParam.put("AccountPreference", "1");
        inputParam.put("CurrencyCode", "USD");
        inputParam.put("AccountHolder", "John Jones");
        inputParam.put("RoutingNumber", "648721615");
        inputParam.put("SwiftCode", "CTBAUS124");
        inputParam.put("BsbNum", "123456789");
        inputParam.put("JointHolders", "");
        inputParam.put("DividendRate", "0");
        inputParam.put("DividendYTD", "0");
        inputParam.put("LastDividendPaidAmount", "0");
        inputParam.put("lastDividendPaidDate", "");
        inputParam.put("PreviousYearDividend", "0");
        inputParam.put("DividendPaidYTD", "0");
        inputParam.put("DividendLastPaidAmount", "0");
        inputParam.put("dividendLastPaidDate", "");
        inputParam.put("PreviousYearsDividends", "0");
        inputParam.put("availableCredit", "");
        inputParam.put("dueDate", "");
        inputParam.put("minimumDue", "");
        inputParam.put("outstandingBalance", "");
        inputParam.put("creditCardNumber", "");
        inputParam.put("availablePoints", "");
        inputParam.put("currentAmountDue", "");
        inputParam.put("lastPaymentAmount", "");
        inputParam.put("lateFeesDue", "43");
        inputParam.put("creditLimit", "");
        inputParam.put("interestRate", "");
        inputParam.put("maturityDate", "");
        inputParam.put("maturityOption", "");
        inputParam.put("bondInterest", "0");
        inputParam.put("bondInterestLastYear", "0");
        inputParam.put("TotalCreditMonths", "370");
        inputParam.put("TotalDebitsMonth", "734");
        inputParam.put("interestEarned", "23");
        inputParam.put("maturityAmount", "");
        inputParam.put("paymentTerm", "");
        inputParam.put("principalValue", "");
        inputParam.put("paymentDue", "");
        inputParam.put("interestPaidYTD", "");
        inputParam.put("interestPaidPreviousYTD", "");
        inputParam.put("unpaidInterest", "");
        inputParam.put("regularPaymentAmount", "");
        inputParam.put("principalBalance", "");
        inputParam.put("originalAmount", "");
        inputParam.put("payoffAmount", "");
        inputParam.put("payOffCharge", "");
        inputParam.put("interestPaidLastYear", "");
        inputParam.put("bankName", "");
        inputParam.put("AccountHolder2", "John Doe");
        return inputParam;
    }

    public Map<String, String> getAccount3DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("Type_id", "Checking");
        inputParam.put("AccountName", "Pro Business Checking");
        inputParam.put("Name", "Pro Business Checking");
        inputParam.put("NickName", "Business Checking - Pine Brook Branch");
        inputParam.put("OpeningDate", "2017-09-10");
        inputParam.put("closingDate", "");
        inputParam.put("StatusDesc", "Active");
        inputParam.put("FavouriteStatus", "1");
        inputParam.put("AvailableBalance", "300303.12");
        inputParam.put("CurrentBalance", "287720.03");
        inputParam.put("PendingDeposit", "5916.91");
        inputParam.put("PendingWithdrawal", "18500");
        inputParam.put("lastStatementBalance", "");
        inputParam.put("ShowTransactions", "1");
        inputParam.put("SupportBillPay", "1");
        inputParam.put("SupportDeposit", "1");
        inputParam.put("SupportTransferFrom", "1");
        inputParam.put("SupportTransferTo", "1");
        inputParam.put("SupportCardlessCash", "1");
        inputParam.put("TransactionLimit", "10000");
        inputParam.put("TransferLimit", "10000");
        inputParam.put("IsPFM", "1");
        inputParam.put("EStatementmentEnable", "0");
        inputParam.put("AccountPreference", "1");
        inputParam.put("CurrencyCode", "USD");
        inputParam.put("AccountHolder", "John Jones");
        inputParam.put("RoutingNumber", "648721615");
        inputParam.put("SwiftCode", "CTBAUS124");
        inputParam.put("BsbNum", "123456789");
        inputParam.put("JointHolders", "");
        inputParam.put("DividendRate", "0");
        inputParam.put("DividendYTD", "0");
        inputParam.put("LastDividendPaidAmount", "0");
        inputParam.put("lastDividendPaidDate", "");
        inputParam.put("PreviousYearDividend", "0");
        inputParam.put("DividendPaidYTD", "0");
        inputParam.put("DividendLastPaidAmount", "0");
        inputParam.put("dividendLastPaidDate", "");
        inputParam.put("PreviousYearsDividends", "0");
        inputParam.put("availableCredit", "");
        inputParam.put("dueDate", "");
        inputParam.put("minimumDue", "");
        inputParam.put("outstandingBalance", "");
        inputParam.put("creditCardNumber", "");
        inputParam.put("availablePoints", "");
        inputParam.put("currentAmountDue", "");
        inputParam.put("lastPaymentAmount", "");
        inputParam.put("lateFeesDue", "43");
        inputParam.put("creditLimit", "");
        inputParam.put("interestRate", "");
        inputParam.put("maturityDate", "");
        inputParam.put("maturityOption", "");
        inputParam.put("bondInterest", "0");
        inputParam.put("bondInterestLastYear", "0");
        inputParam.put("TotalCreditMonths", "370");
        inputParam.put("TotalDebitsMonth", "734");
        inputParam.put("interestEarned", "23");
        inputParam.put("maturityAmount", "");
        inputParam.put("paymentTerm", "");
        inputParam.put("principalValue", "");
        inputParam.put("paymentDue", "");
        inputParam.put("interestPaidYTD", "");
        inputParam.put("interestPaidPreviousYTD", "");
        inputParam.put("unpaidInterest", "");
        inputParam.put("regularPaymentAmount", "");
        inputParam.put("principalBalance", "");
        inputParam.put("originalAmount", "");
        inputParam.put("payoffAmount", "");
        inputParam.put("payOffCharge", "");
        inputParam.put("interestPaidLastYear", "");
        inputParam.put("bankName", "");
        inputParam.put("AccountHolder2", "John Doe");
        return inputParam;
    }

    public Map<String, String> getAccount4DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("Type_id", "CreditCard");
        inputParam.put("AccountName", "Business Platinum MasterCard");
        inputParam.put("Name", "Business Platinum MasterCard");
        inputParam.put("NickName", "Platinum Credit Card");
        inputParam.put("OpeningDate", "2016-10-10");
        inputParam.put("closingDate", "");
        inputParam.put("StatusDesc", "Active");
        inputParam.put("FavouriteStatus", "0");
        inputParam.put("AvailableBalance", "0");
        inputParam.put("CurrentBalance", "1301.38");
        inputParam.put("PendingDeposit", "");
        inputParam.put("PendingWithdrawal", "");
        inputParam.put("lastStatementBalance", "1051.38");
        inputParam.put("ShowTransactions", "1");
        inputParam.put("SupportBillPay", "1");
        inputParam.put("SupportDeposit", "1");
        inputParam.put("SupportTransferFrom", "1");
        inputParam.put("SupportTransferTo", "1");
        inputParam.put("SupportCardlessCash", "0");
        inputParam.put("TransactionLimit", "10000");
        inputParam.put("TransferLimit", "10000");
        inputParam.put("IsPFM", "1");
        inputParam.put("EStatementmentEnable", "0");
        inputParam.put("AccountPreference", "2");
        inputParam.put("CurrencyCode", "USD");
        inputParam.put("AccountHolder", "Linda Jones");
        inputParam.put("RoutingNumber", "648721691");
        inputParam.put("SwiftCode", "CTBAUS124");
        inputParam.put("BsbNum", "123456789");
        inputParam.put("JointHolders", "Susan Davis");
        inputParam.put("DividendRate", "");
        inputParam.put("DividendYTD", "");
        inputParam.put("LastDividendPaidAmount", "");
        inputParam.put("lastDividendPaidDate", "");
        inputParam.put("PreviousYearDividend", "");
        inputParam.put("DividendPaidYTD", "");
        inputParam.put("DividendLastPaidAmount", "");
        inputParam.put("dividendLastPaidDate", "");
        inputParam.put("PreviousYearsDividends", "");
        inputParam.put("availableCredit", "6698.62");
        inputParam.put("dueDate", "43324");
        inputParam.put("minimumDue", "276");
        inputParam.put("outstandingBalance", "1301.38");
        inputParam.put("creditCardNumber", "4541982333084990");
        inputParam.put("availablePoints", "35");
        inputParam.put("currentAmountDue", "-2399.65");
        inputParam.put("lastPaymentAmount", "50");
        inputParam.put("lateFeesDue", "10");
        inputParam.put("creditLimit", "8000");
        inputParam.put("interestRate", "14.62");
        inputParam.put("maturityDate", "");
        inputParam.put("maturityOption", "");
        inputParam.put("bondInterest", "");
        inputParam.put("bondInterestLastYear", "");
        inputParam.put("TotalCreditMonths", "");
        inputParam.put("TotalDebitsMonth", "");
        inputParam.put("interestEarned", "");
        inputParam.put("maturityAmount", "");
        inputParam.put("paymentTerm", "");
        inputParam.put("principalValue", "");
        inputParam.put("paymentDue", "28");
        inputParam.put("interestPaidYTD", "");
        inputParam.put("interestPaidPreviousYTD", "");
        inputParam.put("unpaidInterest", "");
        inputParam.put("regularPaymentAmount", "");
        inputParam.put("principalBalance", "");
        inputParam.put("originalAmount", "");
        inputParam.put("payoffAmount", "");
        inputParam.put("payOffCharge", "");
        inputParam.put("interestPaidLastYear", "");
        inputParam.put("bankName", "");
        inputParam.put("AccountHolder2", "John Doe");
        inputParam.put("JointHolders", "Susan Davis");
        return inputParam;
    }

    public Map<String, String> getAccount5DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("Type_id", "CreditCard");
        inputParam.put("AccountName", "Business Gold Credit Card");
        inputParam.put("Name", "Business Gold Credit Card");
        inputParam.put("NickName", "Gold Credit Card");
        inputParam.put("OpeningDate", "2016-10-10");
        inputParam.put("closingDate", "");
        inputParam.put("StatusDesc", "Active");
        inputParam.put("FavouriteStatus", "0");
        inputParam.put("AvailableBalance", "0");
        inputParam.put("CurrentBalance", "2507.35");
        inputParam.put("PendingDeposit", "");
        inputParam.put("PendingWithdrawal", "");
        inputParam.put("lastStatementBalance", "2051.38");
        inputParam.put("ShowTransactions", "1");
        inputParam.put("SupportBillPay", "1");
        inputParam.put("SupportDeposit", "1");
        inputParam.put("SupportTransferFrom", "1");
        inputParam.put("SupportTransferTo", "1");
        inputParam.put("SupportCardlessCash", "0");
        inputParam.put("TransactionLimit", "10000");
        inputParam.put("TransferLimit", "10000");
        inputParam.put("IsPFM", "1");
        inputParam.put("EStatementmentEnable", "0");
        inputParam.put("AccountPreference", "2");
        inputParam.put("CurrencyCode", "USD");
        inputParam.put("AccountHolder", "Michael Davis");
        inputParam.put("RoutingNumber", "648721691");
        inputParam.put("SwiftCode", "CTBAUS124");
        inputParam.put("BsbNum", "123456789");
        inputParam.put("JointHolders", "Linda Jones");
        inputParam.put("DividendRate", "");
        inputParam.put("DividendYTD", "");
        inputParam.put("LastDividendPaidAmount", "");
        inputParam.put("lastDividendPaidDate", "");
        inputParam.put("PreviousYearDividend", "");
        inputParam.put("DividendPaidYTD", "");
        inputParam.put("DividendLastPaidAmount", "");
        inputParam.put("dividendLastPaidDate", "");
        inputParam.put("PreviousYearsDividends", "");
        inputParam.put("availableCredit", "5492.65");
        inputParam.put("dueDate", "43324");
        inputParam.put("minimumDue", "2051.38");
        inputParam.put("outstandingBalance", "-2399.65");
        inputParam.put("creditCardNumber", "4541982333084990");
        inputParam.put("availablePoints", "35");
        inputParam.put("currentAmountDue", "2051.38");
        inputParam.put("lastPaymentAmount", "25");
        inputParam.put("lateFeesDue", "10");
        inputParam.put("creditLimit", "18000");
        inputParam.put("interestRate", "14.62");
        inputParam.put("maturityDate", "");
        inputParam.put("maturityOption", "");
        inputParam.put("bondInterest", "");
        inputParam.put("bondInterestLastYear", "");
        inputParam.put("TotalCreditMonths", "");
        inputParam.put("TotalDebitsMonth", "");
        inputParam.put("interestEarned", "");
        inputParam.put("maturityAmount", "");
        inputParam.put("paymentTerm", "");
        inputParam.put("principalValue", "");
        inputParam.put("paymentDue", "28");
        inputParam.put("interestPaidYTD", "");
        inputParam.put("interestPaidPreviousYTD", "");
        inputParam.put("unpaidInterest", "");
        inputParam.put("regularPaymentAmount", "");
        inputParam.put("principalBalance", "");
        inputParam.put("originalAmount", "");
        inputParam.put("payoffAmount", "");
        inputParam.put("payOffCharge", "");
        inputParam.put("interestPaidLastYear", "");
        inputParam.put("bankName", "");
        inputParam.put("", "");
        inputParam.put("AccountHolder2", "John Doe");
        return inputParam;
    }

    public Map<String, String> getAccount6DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("Type_id", "Deposit");
        inputParam.put("AccountName", "Business Direct Term Deposit");
        inputParam.put("Name", "Business Direct Term Deposit");
        inputParam.put("NickName", "Business Direct Term Deposit");
        inputParam.put("OpeningDate", "2017-08-11");
        inputParam.put("closingDate", "");
        inputParam.put("StatusDesc", "Active");
        inputParam.put("FavouriteStatus", "0");
        inputParam.put("AvailableBalance", "0");
        inputParam.put("CurrentBalance", "5000");
        inputParam.put("PendingDeposit", "");
        inputParam.put("PendingWithdrawal", "");
        inputParam.put("lastStatementBalance", "");
        inputParam.put("ShowTransactions", "1");
        inputParam.put("SupportBillPay", "0");
        inputParam.put("SupportDeposit", "0");
        inputParam.put("SupportTransferFrom", "0");
        inputParam.put("SupportTransferTo", "0");
        inputParam.put("SupportCardlessCash", "0");
        inputParam.put("TransactionLimit", "");
        inputParam.put("TransferLimit", "");
        inputParam.put("IsPFM", "0");
        inputParam.put("EStatementmentEnable", "0");
        inputParam.put("AccountPreference", "4");
        inputParam.put("CurrencyCode", "USD");
        inputParam.put("AccountHolder", "Linda Jones");
        inputParam.put("RoutingNumber", "648721691");
        inputParam.put("SwiftCode", "CTBAUS124");
        inputParam.put("BsbNum", "123456789");
        inputParam.put("JointHolders", "Susan Davis");
        inputParam.put("DividendRate", "0.45");
        inputParam.put("DividendYTD", "34");
        inputParam.put("LastDividendPaidAmount", "34");
        inputParam.put("lastDividendPaidDate", "43108");
        inputParam.put("PreviousYearDividend", "70");
        inputParam.put("DividendPaidYTD", "34");
        inputParam.put("DividendLastPaidAmount", "34");
        inputParam.put("dividendLastPaidDate", "43108");
        inputParam.put("PreviousYearsDividends", "70");
        inputParam.put("availableCredit", "");
        inputParam.put("dueDate", "");
        inputParam.put("minimumDue", "");
        inputParam.put("outstandingBalance", "");
        inputParam.put("creditCardNumber", "null");
        inputParam.put("availablePoints", "0");
        inputParam.put("currentAmountDue", "");
        inputParam.put("lastPaymentAmount", "");
        inputParam.put("lateFeesDue", "23");
        inputParam.put("creditLimit", "");
        inputParam.put("interestRate", "0.45");
        inputParam.put("maturityDate", "2018-08-08");
        inputParam.put("maturityOption", "Renew");
        inputParam.put("bondInterest", "");
        inputParam.put("bondInterestLastYear", "");
        inputParam.put("TotalCreditMonths", "");
        inputParam.put("TotalDebitsMonth", "");
        inputParam.put("interestEarned", "45");
        inputParam.put("maturityAmount", "5600");
        inputParam.put("paymentTerm", "4");
        inputParam.put("principalValue", "");
        inputParam.put("paymentDue", "");
        inputParam.put("interestPaidYTD", "");
        inputParam.put("interestPaidPreviousYTD", "");
        inputParam.put("unpaidInterest", "");
        inputParam.put("regularPaymentAmount", "");
        inputParam.put("principalBalance", "");
        inputParam.put("originalAmount", "");
        inputParam.put("payoffAmount", "");
        inputParam.put("payOffCharge", "");
        inputParam.put("interestPaidLastYear", "");
        inputParam.put("bankName", "");
        inputParam.put("AccountHolder2", "John Doe");
        return inputParam;
    }

    public Map<String, String> getAccount7DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("Type_id", "Loan");
        inputParam.put("AccountName", "Business Advantage Term Loan");
        inputParam.put("Name", "Business Advantage Term Loan");
        inputParam.put("NickName", "Business Advantage Term Loan");
        inputParam.put("OpeningDate", "2016-10-10");
        inputParam.put("closingDate", "2026-10-10");
        inputParam.put("StatusDesc", "Active");
        inputParam.put("FavouriteStatus", "0");
        inputParam.put("AvailableBalance", "0");
        inputParam.put("CurrentBalance", "30000");
        inputParam.put("PendingDeposit", "");
        inputParam.put("PendingWithdrawal", "");
        inputParam.put("lastStatementBalance", "");
        inputParam.put("ShowTransactions", "1");
        inputParam.put("SupportBillPay", "0");
        inputParam.put("SupportDeposit", "0");
        inputParam.put("SupportTransferFrom", "0");
        inputParam.put("SupportTransferTo", "1");
        inputParam.put("SupportCardlessCash", "0");
        inputParam.put("TransactionLimit", "");
        inputParam.put("TransferLimit", "");
        inputParam.put("IsPFM", "0");
        inputParam.put("EStatementmentEnable", "0");
        inputParam.put("AccountPreference", "3");
        inputParam.put("CurrencyCode", "USD");
        inputParam.put("AccountHolder", "Linda Jones");
        inputParam.put("RoutingNumber", "648721691");
        inputParam.put("SwiftCode", "CTBAUS124");
        inputParam.put("BsbNum", "123456789");
        inputParam.put("JointHolders", "");
        inputParam.put("DividendRate", "");
        inputParam.put("DividendYTD", "");
        inputParam.put("LastDividendPaidAmount", "");
        inputParam.put("lastDividendPaidDate", "");
        inputParam.put("PreviousYearDividend", "");
        inputParam.put("DividendPaidYTD", "");
        inputParam.put("DividendLastPaidAmount", "");
        inputParam.put("dividendLastPaidDate", "");
        inputParam.put("PreviousYearsDividends", "");
        inputParam.put("availableCredit", "30000");
        inputParam.put("dueDate", "43324");
        inputParam.put("minimumDue", "");
        inputParam.put("outstandingBalance", "16760.2288358465");
        inputParam.put("creditCardNumber", "");
        inputParam.put("availablePoints", "");
        inputParam.put("currentAmountDue", "736");
        inputParam.put("lastPaymentAmount", "368");
        inputParam.put("lateFeesDue", "11");
        inputParam.put("creditLimit", "");
        inputParam.put("interestRate", "2.25");
        inputParam.put("maturityDate", "");
        inputParam.put("maturityOption", "");
        inputParam.put("bondInterest", "");
        inputParam.put("bondInterestLastYear", "");
        inputParam.put("TotalCreditMonths", "");
        inputParam.put("TotalDebitsMonth", "");
        inputParam.put("interestEarned", "");
        inputParam.put("maturityAmount", "");
        inputParam.put("paymentTerm", "");
        inputParam.put("principalValue", "");
        inputParam.put("paymentDue", "13");
        inputParam.put("interestPaidYTD", "242.302407608797");
        inputParam.put("interestPaidPreviousYTD", "289");
        inputParam.put("unpaidInterest", "");
        inputParam.put("regularPaymentAmount", "");
        inputParam.put("principalBalance", "16024.2288358465");
        inputParam.put("originalAmount", "20000");
        inputParam.put("payoffAmount", "16773.2288358465");
        inputParam.put("payOffCharge", "749");
        inputParam.put("interestPaidLastYear", "289");
        inputParam.put("bankName", "");
        inputParam.put("AccountHolder2", "John Doe");
        return inputParam;
    }

    public Map<String, String> getInvestmentAccount1Data() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("Type_id", "Investment");
        inputParam.put("AccountType", "Investment");
        inputParam.put("Name", "Investment Account1");
        inputParam.put("AccountName", "Investment Account1");
        inputParam.put("NickName", "Investment Account1");
        inputParam.put("OpeningDate", "2000-10-10");
        inputParam.put("ClosingDate", "");
        inputParam.put("StatusDesc", "Active");
        inputParam.put("FavouriteStatus", "1");
        inputParam.put("AvailableBalance", "45000");
        inputParam.put("CurrentBalance", "63208.35");
        inputParam.put("PendingDeposit", "41300.00");
        inputParam.put("PendingWithdrawal", "23091.65");
        inputParam.put("LastStatementBalance", "");
        inputParam.put("ShowTransactions", "1");
        inputParam.put("SupportBillPay", "1");
        inputParam.put("SupportDeposit", "1");
        inputParam.put("SupportTransferFrom", "1");
        inputParam.put("SupportTransferTo", "1");
        inputParam.put("SupportCardlessCash", "1");
        inputParam.put("TransactionLimit", "10000");
        inputParam.put("TransferLimit", "10000");
        inputParam.put("IsPFM", "1");
        inputParam.put("EStatementEnable", "0");
        inputParam.put("AccountPreference", "0");
        inputParam.put("CurrencyCode", "USD");
        inputParam.put("AccountHolder", "John");
        inputParam.put("RoutingNumber", "");
        inputParam.put("SwiftCode", "");
        inputParam.put("BsbNum", "");
        inputParam.put("JointHolders", "");
        inputParam.put("DividendRate", "0");
        inputParam.put("DividendYTD", "0");
        inputParam.put("LastDividendPaidAmount", "0");
        inputParam.put("LastDividendPaidDate", "");
        inputParam.put("PreviousYearDividend", "0");
        inputParam.put("DividendPaidYTD", "0");
        inputParam.put("DividendLastPaidAmount", "0");
        inputParam.put("DividendLastPaidDate", "");
        inputParam.put("PreviousYearsDividends", "0");
        inputParam.put("AvailableCredit", "");
        inputParam.put("DueDate", "");
        inputParam.put("MinimumDue", "");
        inputParam.put("OutstandingBalance", "");
        inputParam.put("CreditCardNumber", "");
        inputParam.put("AvailablePoints", "");
        inputParam.put("CurrentAmountDue", "");
        inputParam.put("LastPaymentAmount", "");
        inputParam.put("LateFeesDue", "");
        inputParam.put("CreditLimit", "");
        inputParam.put("InterestRate", "");
        inputParam.put("MaturityDate", "");
        inputParam.put("MaturityOption", "");
        inputParam.put("BondInterest", "");
        inputParam.put("BondInterestLastYear", "");
        inputParam.put("TotalCreditMonths", "");
        inputParam.put("TotalDebitsMonth", "");
        inputParam.put("InterestEarned", "");
        inputParam.put("MaturityAmount", "");
        inputParam.put("PaymentTerm", "");
        inputParam.put("PrincipalValue", "");
        inputParam.put("PaymentDue", "");
        inputParam.put("InterestPaidYTD", "");
        inputParam.put("InterestPaidPreviousYTD", "");
        inputParam.put("UnpaidInterest", "");
        inputParam.put("RegularPaymentAmount", "");
        inputParam.put("PrincipalBalance", "");
        inputParam.put("OriginalAmount", "");
        inputParam.put("PayoffAmount", "");
        inputParam.put("PayOffCharge", "");
        inputParam.put("InterestPaidLastYear", "");
        inputParam.put("BankName", "");
        return inputParam;
    }

    public Map<String, String> getInvestmentAccount2Data() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("Type_id", "Investment");
        inputParam.put("AccountType", "Investment");
        inputParam.put("Name", "Investment Account2");
        inputParam.put("AccountName", "Investment Account2");
        inputParam.put("NickName", "Investment Account2");
        inputParam.put("OpeningDate", "2000-10-10");
        inputParam.put("ClosingDate", "");
        inputParam.put("StatusDesc", "Active");
        inputParam.put("FavouriteStatus", "1");
        inputParam.put("AvailableBalance", "35000.00");
        inputParam.put("CurrentBalance", "53208.35");
        inputParam.put("PendingDeposit", "41300.00");
        inputParam.put("PendingWithdrawal", "23091.65");
        inputParam.put("LastStatementBalance", "");
        inputParam.put("ShowTransactions", "1");
        inputParam.put("SupportBillPay", "1");
        inputParam.put("SupportDeposit", "1");
        inputParam.put("SupportTransferFrom", "1");
        inputParam.put("SupportTransferTo", "1");
        inputParam.put("SupportCardlessCash", "1");
        inputParam.put("TransactionLimit", "50000");
        inputParam.put("TransferLimit", "50000");
        inputParam.put("IsPFM", "1");
        inputParam.put("EStatementEnable", "0");
        inputParam.put("AccountPreference", "0");
        inputParam.put("CurrencyCode", "USD");
        inputParam.put("AccountHolder", "John");
        inputParam.put("RoutingNumber", "");
        inputParam.put("SwiftCode", "");
        inputParam.put("BsbNum", "");
        inputParam.put("JointHolders", "");
        inputParam.put("DividendRate", "0");
        inputParam.put("DividendYTD", "0");
        inputParam.put("LastDividendPaidAmount", "0");
        inputParam.put("LastDividendPaidDate", "");
        inputParam.put("PreviousYearDividend", "0");
        inputParam.put("DividendPaidYTD", "0");
        inputParam.put("DividendLastPaidAmount", "0");
        inputParam.put("DividendLastPaidDate", "");
        inputParam.put("PreviousYearsDividends", "0");
        inputParam.put("AvailableCredit", "");
        inputParam.put("DueDate", "");
        inputParam.put("MinimumDue", "");
        inputParam.put("OutstandingBalance", "");
        inputParam.put("CreditCardNumber", "");
        inputParam.put("AvailablePoints", "");
        inputParam.put("CurrentAmountDue", "");
        inputParam.put("LastPaymentAmount", "");
        inputParam.put("LateFeesDue", "");
        inputParam.put("CreditLimit", "");
        inputParam.put("InterestRate", "");
        inputParam.put("MaturityDate", "");
        inputParam.put("MaturityOption", "");
        inputParam.put("BondInterest", "");
        inputParam.put("BondInterestLastYear", "");
        inputParam.put("TotalCreditMonths", "");
        inputParam.put("TotalDebitsMonth", "");
        inputParam.put("InterestEarned", "");
        inputParam.put("MaturityAmount", "");
        inputParam.put("PaymentTerm", "");
        inputParam.put("PrincipalValue", "");
        inputParam.put("PaymentDue", "");
        inputParam.put("InterestPaidYTD", "");
        inputParam.put("InterestPaidPreviousYTD", "");
        inputParam.put("UnpaidInterest", "");
        inputParam.put("RegularPaymentAmount", "");
        inputParam.put("PrincipalBalance", "");
        inputParam.put("OriginalAmount", "");
        inputParam.put("PayoffAmount", "");
        inputParam.put("PayOffCharge", "");
        inputParam.put("InterestPaidLastYear", "");
        inputParam.put("BankName", "");
        return inputParam;
    }

    public Map<String, String> getInternalTransfer_SAV1DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternalTransfer");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "Business Advantage Savings");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "36000");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online transfer from Core Checking Masked Account Number");
        inputParam.put("notes", "Development fund");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "510852.74");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCheckDeposit_SAV2DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "CheckDeposit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Advantage Savings");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "5300");
        inputParam.put("checkNumber", "9812");
        inputParam.put("description", "Check 9812");
        inputParam.put("notes", "Insurance refund");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "510852.74");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getExternalTransfer_SAV3DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "ExternalTransfer");
        inputParam.put("fromAccountNumber", "Business Advantage Savings");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "98287890273");
        inputParam.put("amount", "-6450");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online external fund transfer to Alex Sion Suppliers Inc.");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "510852.74");
        inputParam.put("createdDate", "-9");
        inputParam.put("transactionDate", "-9");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getRemoteCheckDeposit_SAV4DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "RemoteCheckDeposit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Advantage Savings");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "50000");
        inputParam.put("checkNumber", "19805");
        inputParam.put("description", "Check 19805");
        inputParam.put("notes", "Payment from USAFoods");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "517302.74");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternalTransfer_SAV5DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternalTransfer");
        inputParam.put("fromAccountNumber", "Business Advantage Savings");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-40000");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online transfer to Core Checking Masked Account Number");
        inputParam.put("notes", "Transfer for salary disbursements");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "467302.74");
        inputParam.put("createdDate", "-11");
        inputParam.put("transactionDate", "-11");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInterest_SAV6DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "Interest");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "1008.78");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Interest earned");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "507302.74");
        inputParam.put("createdDate", "-12");
        inputParam.put("transactionDate", "-12");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getRemoteCheckDeposit_SAV7DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "RemoteCheckDeposit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Advantage Savings");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "14500");
        inputParam.put("checkNumber", "19802");
        inputParam.put("description", "Check 19802");
        inputParam.put("notes", "Received from Jane Distributors");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "506293.96");
        inputParam.put("createdDate", "-13");
        inputParam.put("transactionDate", "-13");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_SAV8DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Advantage Savings");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "385.61");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Card reward points");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "491793.96");
        inputParam.put("createdDate", "-14");
        inputParam.put("transactionDate", "-14");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternalTransfer_SAV9DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternalTransfer");
        inputParam.put("fromAccountNumber", "Business Advantage Savings");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "15000");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online transfer  to Core Checking Masked Account Number");
        inputParam.put("notes", "Development fund");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "491408.35");
        inputParam.put("createdDate", "-16");
        inputParam.put("transactionDate", "-16");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getExternalTransfer_SAV10DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "ExternalTransfer");
        inputParam.put("fromAccountNumber", "Business Advantage Savings");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "29475930375");
        inputParam.put("amount", "-500");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Wire out TRN:2017082900259510 SERVICE REF:007047 BNF: ABA Inc");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "476408.35");
        inputParam.put("createdDate", "-17");
        inputParam.put("transactionDate", "-17");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getLoan_SAV11DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "Loan");
        inputParam.put("fromAccountNumber", "Business Advantage Savings");
        inputParam.put("toAccountNumber", "Business Advantage Term Loan");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-23091.65");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "J P Morgan Chase loan payment ACH ID:XXXXX18921");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "476908.35");
        inputParam.put("createdDate", "-18");
        inputParam.put("transactionDate", "");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "1");
        inputParam.put("scheduledDate", "+14");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_Checking1DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "Credit ");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "5916.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment from Acme Inc.: 1921");
        inputParam.put("notes", "Bulk Order");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "211395.03");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternalTransfer_Checking2DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternalTransfer");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "Business Platinum Credit Card");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-2500");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online transfer Freedom Credit Card Masked Card Number");
        inputParam.put("notes", "Jan credit card bill");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "205478.12");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternalTransfer_Checking3DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternalTransfer");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "Business Advantage Savings");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-16000");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online transfer Rewards Savings Masked Account Number");
        inputParam.put("notes", "Development fund");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "207978.12");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternalTransfer_Checking4DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternalTransfer");
        inputParam.put("fromAccountNumber", "Business Advantage Savings");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "50000");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online transfer Rewards Savings Masked Account Number");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "223978.12");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCheckDeposit_Checking5DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "CheckDeposit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "2006");
        inputParam.put("checkNumber", "32174");
        inputParam.put("description", "Check 32174");
        inputParam.put("notes", "Payment ");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "173978.12");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_Checking6DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "Bill Pay");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-30");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "TFC Traders Inc. transfer Conf# 2fqjh376b; Arthur");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "171972.12");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInterest_Checking7DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "Interest");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "176.25");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Interest credit Jan");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "172002.12");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getWithdrawal_Checking8DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "Withdrawal");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-200");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "ATM-KonyBank Austin 70123");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "171825.87");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_Checking9DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "Bill Pay");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-1000");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to City Waterworks");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "172025.87");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_Checking10DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-12000.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amaze Supplies");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "173025.87");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_Checking11DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "8650.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Deposit");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "185025.99");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_Checking12DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34653");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Deposit");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "176375.08");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_Checking13DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-750.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS HEB Traders #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "141722.08");
        inputParam.put("createdDate", "-9");
        inputParam.put("transactionDate", "-9");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_Checking14DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "142472.99");
        inputParam.put("createdDate", "-9");
        inputParam.put("transactionDate", "-9");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_Checking15DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "142510.18");
        inputParam.put("createdDate", "-9");
        inputParam.put("transactionDate", "-9");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_Checking16DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "45624");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Deposit");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "142548.22");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_Checking17DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "58245");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Deposit");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "96924.22");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_Checking18DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "38679.22");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_Checking19DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "38807.2");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_Checking20DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment to TFC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "38875.39");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_Checking21DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment from Jane Cal: 12345");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "38995.51");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_Checking22DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "60.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment from Ashton.: 19452");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "38914.6");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_Checking23DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "345");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment from Esther Green.: 19453");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "38853.69");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_Checking24DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "38508.69");
        inputParam.put("createdDate", "-11");
        inputParam.put("transactionDate", "-11");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_Checking25DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "38576.88");
        inputParam.put("createdDate", "-11");
        inputParam.put("transactionDate", "-11");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_Checking26DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment from Steve.: 19452");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "38697");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_Checking27DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment from Allison Green.: 19453");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "38616.09");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_Checking28DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "38535.18");
        inputParam.put("createdDate", "-12");
        inputParam.put("transactionDate", "-12");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC1DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "300");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1849");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1301.38");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternalTransfer_CC2DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternalTransfer");
        inputParam.put("fromAccountNumber", "Rewards Savings");
        inputParam.put("toAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-50");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online payment from SAV 18");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1001.38");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC3DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1051.38");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC4DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1017.17");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_CC5DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1098.08");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC6DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "970.1");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCardPayment_CC7DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "CardPayment");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-4000");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment - Thank You");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "901.91");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC8DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4901.91");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC9DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4883.72");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC10DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS HEB Round Rock #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4688.69");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC11DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4612.78");
        inputParam.put("createdDate", "-9");
        inputParam.put("transactionDate", "-9");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC12DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4575.59");
        inputParam.put("createdDate", "-9");
        inputParam.put("transactionDate", "-9");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC13DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4537.55");
        inputParam.put("createdDate", "-9");
        inputParam.put("transactionDate", "-9");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_CC14DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4503.34");
        inputParam.put("createdDate", "-9");
        inputParam.put("transactionDate", "-9");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_CC15DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4584.25");
        inputParam.put("createdDate", "-9");
        inputParam.put("transactionDate", "-9");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC16DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4456.27");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC17DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4388.08");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC18DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4267.96");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC19DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4249.77");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC20DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4054.74");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC21DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3978.83");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC22DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3941.64");
        inputParam.put("createdDate", "-11");
        inputParam.put("transactionDate", "-11");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC23DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3903.6");
        inputParam.put("createdDate", "-11");
        inputParam.put("transactionDate", "-11");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_CC24DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3869.39");
        inputParam.put("createdDate", "-11");
        inputParam.put("transactionDate", "-11");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_CC25DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3950.3");
        inputParam.put("createdDate", "-11");
        inputParam.put("transactionDate", "-11");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC26DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3822.32");
        inputParam.put("createdDate", "-11");
        inputParam.put("transactionDate", "-11");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC27DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3754.13");
        inputParam.put("createdDate", "-12");
        inputParam.put("transactionDate", "-12");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC28DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3634.01");
        inputParam.put("createdDate", "-12");
        inputParam.put("transactionDate", "-12");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC29DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3615.82");
        inputParam.put("createdDate", "-12");
        inputParam.put("transactionDate", "-12");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC30DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3420.79");
        inputParam.put("createdDate", "-12");
        inputParam.put("transactionDate", "-12");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC31DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3344.88");
        inputParam.put("createdDate", "-12");
        inputParam.put("transactionDate", "-12");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC32DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3276.69");
        inputParam.put("createdDate", "-13");
        inputParam.put("transactionDate", "-13");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC33DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3156.57");
        inputParam.put("createdDate", "-13");
        inputParam.put("transactionDate", "-13");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC34DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3138.38");
        inputParam.put("createdDate", "-13");
        inputParam.put("transactionDate", "-13");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC35DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2943.35");
        inputParam.put("createdDate", "-13");
        inputParam.put("transactionDate", "-13");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC36DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2867.44");
        inputParam.put("createdDate", "-13");
        inputParam.put("transactionDate", "-13");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC37DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2830.25");
        inputParam.put("createdDate", "-14");
        inputParam.put("transactionDate", "-14");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC38DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2792.21");
        inputParam.put("createdDate", "-14");
        inputParam.put("transactionDate", "-14");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_CC39DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2758");
        inputParam.put("createdDate", "-14");
        inputParam.put("transactionDate", "-14");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_CC40DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2838.91");
        inputParam.put("createdDate", "-14");
        inputParam.put("transactionDate", "-14");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC41DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2710.93");
        inputParam.put("createdDate", "-14");
        inputParam.put("transactionDate", "-14");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC42DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2642.74");
        inputParam.put("createdDate", "-15");
        inputParam.put("transactionDate", "-15");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC43DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2522.62");
        inputParam.put("createdDate", "-15");
        inputParam.put("transactionDate", "-15");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC44DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2504.43");
        inputParam.put("createdDate", "-15");
        inputParam.put("transactionDate", "-15");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC45DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2309.4");
        inputParam.put("createdDate", "-15");
        inputParam.put("transactionDate", "-15");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC46DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2233.49");
        inputParam.put("createdDate", "-15");
        inputParam.put("transactionDate", "-15");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC47DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2196.3");
        inputParam.put("createdDate", "-16");
        inputParam.put("transactionDate", "-16");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC48DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2158.26");
        inputParam.put("createdDate", "-16");
        inputParam.put("transactionDate", "-16");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_CC49DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2124.05");
        inputParam.put("createdDate", "-16");
        inputParam.put("transactionDate", "-16");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_CC50DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2204.96");
        inputParam.put("createdDate", "-16");
        inputParam.put("transactionDate", "-16");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC51DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2076.98");
        inputParam.put("createdDate", "-16");
        inputParam.put("transactionDate", "-16");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC52DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2008.79");
        inputParam.put("createdDate", "-16");
        inputParam.put("transactionDate", "-16");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC53DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1888.67");
        inputParam.put("createdDate", "-17");
        inputParam.put("transactionDate", "-17");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC54DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1870.48");
        inputParam.put("createdDate", "-17");
        inputParam.put("transactionDate", "-17");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC55DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1675.45");
        inputParam.put("createdDate", "-17");
        inputParam.put("transactionDate", "-17");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC56DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1599.54");
        inputParam.put("createdDate", "-17");
        inputParam.put("transactionDate", "-17");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC57DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1562.35");
        inputParam.put("createdDate", "-17");
        inputParam.put("transactionDate", "-17");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC58DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1524.31");
        inputParam.put("createdDate", "-17");
        inputParam.put("transactionDate", "-17");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_CC59DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1490.1");
        inputParam.put("createdDate", "-18");
        inputParam.put("transactionDate", "-18");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_CC60DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1571.01");
        inputParam.put("createdDate", "-18");
        inputParam.put("transactionDate", "-18");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC61DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1443.03");
        inputParam.put("createdDate", "-18");
        inputParam.put("transactionDate", "-18");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC62DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1374.84");
        inputParam.put("createdDate", "-18");
        inputParam.put("transactionDate", "-18");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC63DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1254.72");
        inputParam.put("createdDate", "-18");
        inputParam.put("transactionDate", "-18");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC64DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1236.53");
        inputParam.put("createdDate", "-18");
        inputParam.put("transactionDate", "-18");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC65DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1041.5");
        inputParam.put("createdDate", "-19");
        inputParam.put("transactionDate", "-19");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC66DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "965.590000000001");
        inputParam.put("createdDate", "-19");
        inputParam.put("transactionDate", "-19");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC67DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "928.400000000001");
        inputParam.put("createdDate", "-19");
        inputParam.put("transactionDate", "-19");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC68DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "890.360000000001");
        inputParam.put("createdDate", "-19");
        inputParam.put("transactionDate", "-19");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_CC69DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "856.150000000001");
        inputParam.put("createdDate", "-19");
        inputParam.put("transactionDate", "-19");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_CC70DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "937.060000000001");
        inputParam.put("createdDate", "-19");
        inputParam.put("transactionDate", "-19");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC71DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "809.080000000001");
        inputParam.put("createdDate", "-20");
        inputParam.put("transactionDate", "-20");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC72DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "740.890000000001");
        inputParam.put("createdDate", "-20");
        inputParam.put("transactionDate", "-20");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC73DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "620.770000000001");
        inputParam.put("createdDate", "-20");
        inputParam.put("transactionDate", "-20");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC74DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "602.580000000001");
        inputParam.put("createdDate", "-20");
        inputParam.put("transactionDate", "-20");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC75DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "407.550000000001");
        inputParam.put("createdDate", "-20");
        inputParam.put("transactionDate", "-20");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_CC76DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "331.640000000001");
        inputParam.put("createdDate", "-21");
        inputParam.put("transactionDate", "-21");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_CC77DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "412.550000000001");
        inputParam.put("createdDate", "-21");
        inputParam.put("transactionDate", "-21");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC78DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "284.570000000001");
        inputParam.put("createdDate", "-21");
        inputParam.put("transactionDate", "-21");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC79DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "216.380000000001");
        inputParam.put("createdDate", "-21");
        inputParam.put("transactionDate", "-21");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC80DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "96.2600000000009");
        inputParam.put("createdDate", "-21");
        inputParam.put("transactionDate", "-21");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC81DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "78.0700000000009");
        inputParam.put("createdDate", "-21");
        inputParam.put("transactionDate", "-21");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC82DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-116.959999999999");
        inputParam.put("createdDate", "-22");
        inputParam.put("transactionDate", "-22");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC83DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-192.869999999999");
        inputParam.put("createdDate", "-22");
        inputParam.put("transactionDate", "-22");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC84DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-261.059999999999");
        inputParam.put("createdDate", "-22");
        inputParam.put("transactionDate", "-22");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC85DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-381.179999999999");
        inputParam.put("createdDate", "-22");
        inputParam.put("transactionDate", "-22");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC86DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-399.369999999999");
        inputParam.put("createdDate", "-22");
        inputParam.put("transactionDate", "-22");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC87DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-594.399999999999");
        inputParam.put("createdDate", "-22");
        inputParam.put("transactionDate", "-22");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC88DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-670.309999999999");
        inputParam.put("createdDate", "-23");
        inputParam.put("transactionDate", "-23");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC89DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-707.499999999999");
        inputParam.put("createdDate", "-23");
        inputParam.put("transactionDate", "-23");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC90DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-745.539999999999");
        inputParam.put("createdDate", "-23");
        inputParam.put("transactionDate", "-23");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_CC91DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-779.749999999999");
        inputParam.put("createdDate", "-23");
        inputParam.put("transactionDate", "-23");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_CC92DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-698.839999999999");
        inputParam.put("createdDate", "-23");
        inputParam.put("transactionDate", "-23");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC93DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-826.819999999999");
        inputParam.put("createdDate", "-23");
        inputParam.put("transactionDate", "-23");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC94DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-895.009999999999");
        inputParam.put("createdDate", "-24");
        inputParam.put("transactionDate", "-24");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC95DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1015.13");
        inputParam.put("createdDate", "-24");
        inputParam.put("transactionDate", "-24");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC96DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1033.32");
        inputParam.put("createdDate", "-24");
        inputParam.put("transactionDate", "-24");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC97DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1228.35");
        inputParam.put("createdDate", "-24");
        inputParam.put("transactionDate", "-24");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC98DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1304.26");
        inputParam.put("createdDate", "-24");
        inputParam.put("transactionDate", "-24");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC99DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1341.45");
        inputParam.put("createdDate", "-24");
        inputParam.put("transactionDate", "-24");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC100DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1379.49");
        inputParam.put("createdDate", "-25");
        inputParam.put("transactionDate", "-25");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_CC101DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1413.7");
        inputParam.put("createdDate", "-25");
        inputParam.put("transactionDate", "-25");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_CC102DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1332.79");
        inputParam.put("createdDate", "-25");
        inputParam.put("transactionDate", "-25");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC103DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1460.77");
        inputParam.put("createdDate", "-25");
        inputParam.put("transactionDate", "-25");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC104DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1528.96");
        inputParam.put("createdDate", "-25");
        inputParam.put("transactionDate", "-25");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC105DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1649.08");
        inputParam.put("createdDate", "-25");
        inputParam.put("transactionDate", "-25");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC106DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1667.27");
        inputParam.put("createdDate", "-26");
        inputParam.put("transactionDate", "-26");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC107DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1862.3");
        inputParam.put("createdDate", "-26");
        inputParam.put("transactionDate", "-26");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC108DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1938.21");
        inputParam.put("createdDate", "-26");
        inputParam.put("transactionDate", "-26");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC109DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1975.4");
        inputParam.put("createdDate", "-26");
        inputParam.put("transactionDate", "-26");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC110DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2013.44");
        inputParam.put("createdDate", "-26");
        inputParam.put("transactionDate", "-26");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_CC111DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2047.65");
        inputParam.put("createdDate", "-26");
        inputParam.put("transactionDate", "-26");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_CC112DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1966.74");
        inputParam.put("createdDate", "-27");
        inputParam.put("transactionDate", "-27");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC113DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2094.72");
        inputParam.put("createdDate", "-27");
        inputParam.put("transactionDate", "-27");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC114DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2162.91");
        inputParam.put("createdDate", "-27");
        inputParam.put("transactionDate", "-27");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC115DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2283.03");
        inputParam.put("createdDate", "-27");
        inputParam.put("transactionDate", "-27");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC116DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2301.22");
        inputParam.put("createdDate", "-27");
        inputParam.put("transactionDate", "-27");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC117DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2496.25");
        inputParam.put("createdDate", "-27");
        inputParam.put("transactionDate", "-27");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC118DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2572.16");
        inputParam.put("createdDate", "-28");
        inputParam.put("transactionDate", "-28");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC119DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2609.35");
        inputParam.put("createdDate", "-28");
        inputParam.put("transactionDate", "-28");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC120DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2647.39");
        inputParam.put("createdDate", "-28");
        inputParam.put("transactionDate", "-28");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_CC121DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2681.6");
        inputParam.put("createdDate", "-28");
        inputParam.put("transactionDate", "-28");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_CC122DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2600.69");
        inputParam.put("createdDate", "-28");
        inputParam.put("transactionDate", "-28");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC123DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2728.67");
        inputParam.put("createdDate", "-28");
        inputParam.put("transactionDate", "-28");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC124DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2796.86");
        inputParam.put("createdDate", "-28");
        inputParam.put("transactionDate", "-28");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC125DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2916.98");
        inputParam.put("createdDate", "-29");
        inputParam.put("transactionDate", "-29");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC126DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2935.17");
        inputParam.put("createdDate", "-29");
        inputParam.put("transactionDate", "-29");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCardPayment_CC127DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "CardPayment");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-4800");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment - Thank You");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2972.36");
        inputParam.put("createdDate", "-29");
        inputParam.put("transactionDate", "-29");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC128DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1827.64");
        inputParam.put("createdDate", "-29");
        inputParam.put("transactionDate", "-29");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_CC129DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1793.43");
        inputParam.put("createdDate", "-29");
        inputParam.put("transactionDate", "-29");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_CC130DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1874.34");
        inputParam.put("createdDate", "-29");
        inputParam.put("transactionDate", "-29");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC131DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1746.36");
        inputParam.put("createdDate", "-30");
        inputParam.put("transactionDate", "-30");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC132DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1678.17");
        inputParam.put("createdDate", "-30");
        inputParam.put("transactionDate", "-30");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC133DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1558.05");
        inputParam.put("createdDate", "-30");
        inputParam.put("transactionDate", "-30");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC134DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1539.86");
        inputParam.put("createdDate", "-30");
        inputParam.put("transactionDate", "-30");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC135DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1344.83");
        inputParam.put("createdDate", "-30");
        inputParam.put("transactionDate", "-30");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC136DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1268.92");
        inputParam.put("createdDate", "-30");
        inputParam.put("transactionDate", "-30");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC137DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1231.73");
        inputParam.put("createdDate", "-31");
        inputParam.put("transactionDate", "-31");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC138DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1193.69");
        inputParam.put("createdDate", "-31");
        inputParam.put("transactionDate", "-31");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_CC139DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1159.48");
        inputParam.put("createdDate", "-31");
        inputParam.put("transactionDate", "-31");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_CC140DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1240.39");
        inputParam.put("createdDate", "-31");
        inputParam.put("transactionDate", "-31");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC142DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1112.41");
        inputParam.put("createdDate", "-31");
        inputParam.put("transactionDate", "-31");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC143DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1044.22");
        inputParam.put("createdDate", "-31");
        inputParam.put("transactionDate", "-31");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC144DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "924.1");
        inputParam.put("createdDate", "-32");
        inputParam.put("transactionDate", "-32");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC145DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "905.91");
        inputParam.put("createdDate", "-32");
        inputParam.put("transactionDate", "-32");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC146DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "710.88");
        inputParam.put("createdDate", "-32");
        inputParam.put("transactionDate", "-32");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC147DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "634.97");
        inputParam.put("createdDate", "-32");
        inputParam.put("transactionDate", "-32");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC148DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "597.78");
        inputParam.put("createdDate", "-32");
        inputParam.put("transactionDate", "-32");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC149DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "559.74");
        inputParam.put("createdDate", "-32");
        inputParam.put("transactionDate", "-32");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_CC150DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "525.53");
        inputParam.put("createdDate", "-33");
        inputParam.put("transactionDate", "-33");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_CC151DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "606.44");
        inputParam.put("createdDate", "-33");
        inputParam.put("transactionDate", "-33");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC153DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "478.46");
        inputParam.put("createdDate", "-33");
        inputParam.put("transactionDate", "-33");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC154DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "410.27");
        inputParam.put("createdDate", "-33");
        inputParam.put("transactionDate", "-33");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC155DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "290.15");
        inputParam.put("createdDate", "-33");
        inputParam.put("transactionDate", "-33");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC156DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "271.96");
        inputParam.put("createdDate", "-33");
        inputParam.put("transactionDate", "-33");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC157DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "76.9300000000001");
        inputParam.put("createdDate", "-34");
        inputParam.put("transactionDate", "-34");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC158DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1.0200000000001");
        inputParam.put("createdDate", "-34");
        inputParam.put("transactionDate", "-34");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC159DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-36.1699999999999");
        inputParam.put("createdDate", "-34");
        inputParam.put("transactionDate", "-34");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC160DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-74.2099999999999");
        inputParam.put("createdDate", "-34");
        inputParam.put("transactionDate", "-34");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_CC161DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-108.42");
        inputParam.put("createdDate", "-34");
        inputParam.put("transactionDate", "-34");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_CC163DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-27.5099999999999");
        inputParam.put("createdDate", "-34");
        inputParam.put("transactionDate", "-34");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC164DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-155.49");
        inputParam.put("createdDate", "-34");
        inputParam.put("transactionDate", "-34");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC165DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-223.68");
        inputParam.put("createdDate", "-35");
        inputParam.put("transactionDate", "-35");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC166DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-343.8");
        inputParam.put("createdDate", "-35");
        inputParam.put("transactionDate", "-35");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC167DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-361.99");
        inputParam.put("createdDate", "-35");
        inputParam.put("transactionDate", "-35");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC168DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-557.02");
        inputParam.put("createdDate", "-35");
        inputParam.put("transactionDate", "-35");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC169DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-632.93");
        inputParam.put("createdDate", "-35");
        inputParam.put("transactionDate", "-35");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC171DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-670.12");
        inputParam.put("createdDate", "-35");
        inputParam.put("transactionDate", "-35");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC172DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-708.16");
        inputParam.put("createdDate", "-36");
        inputParam.put("transactionDate", "-36");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_CC173DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-742.37");
        inputParam.put("createdDate", "-36");
        inputParam.put("transactionDate", "-36");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_CC174DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-661.46");
        inputParam.put("createdDate", "-36");
        inputParam.put("transactionDate", "-36");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC175DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-789.44");
        inputParam.put("createdDate", "-36");
        inputParam.put("transactionDate", "-36");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC176DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-857.63");
        inputParam.put("createdDate", "-36");
        inputParam.put("transactionDate", "-36");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC177DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-977.75");
        inputParam.put("createdDate", "-36");
        inputParam.put("transactionDate", "-36");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC179DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-995.94");
        inputParam.put("createdDate", "-37");
        inputParam.put("transactionDate", "-37");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC180DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1190.97");
        inputParam.put("createdDate", "-37");
        inputParam.put("transactionDate", "-37");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC181DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1266.88");
        inputParam.put("createdDate", "-37");
        inputParam.put("transactionDate", "-37");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC182DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1304.07");
        inputParam.put("createdDate", "-37");
        inputParam.put("transactionDate", "-37");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC183DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1342.11");
        inputParam.put("createdDate", "-37");
        inputParam.put("transactionDate", "-37");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_CC184DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1376.32");
        inputParam.put("createdDate", "-37");
        inputParam.put("transactionDate", "-37");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_CC185DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1295.41");
        inputParam.put("createdDate", "-38");
        inputParam.put("transactionDate", "-38");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC186DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1423.39");
        inputParam.put("createdDate", "-38");
        inputParam.put("transactionDate", "-38");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC188DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1491.58");
        inputParam.put("createdDate", "-38");
        inputParam.put("transactionDate", "-38");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC189DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1611.7");
        inputParam.put("createdDate", "-38");
        inputParam.put("transactionDate", "-38");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC190DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1629.89");
        inputParam.put("createdDate", "-38");
        inputParam.put("transactionDate", "-38");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC191DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1824.92");
        inputParam.put("createdDate", "-38");
        inputParam.put("transactionDate", "-38");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC192DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1900.83");
        inputParam.put("createdDate", "-39");
        inputParam.put("transactionDate", "-39");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC193DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2020.95");
        inputParam.put("createdDate", "-39");
        inputParam.put("transactionDate", "-39");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC195DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2039.14");
        inputParam.put("createdDate", "-39");
        inputParam.put("transactionDate", "-39");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC196DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2234.17");
        inputParam.put("createdDate", "-39");
        inputParam.put("transactionDate", "-39");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC197DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2310.08");
        inputParam.put("createdDate", "-39");
        inputParam.put("transactionDate", "-39");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC198DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2347.27");
        inputParam.put("createdDate", "-39");
        inputParam.put("transactionDate", "-39");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC199DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2385.31");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_CC200DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2419.52");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_CC201DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2338.61");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC203DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2466.59");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC204DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2534.78");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC205DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2654.9");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC206DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2673.09");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getPOS_CC207DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2868.12");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC208DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2944.03");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInternetTransaction_CC209DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2981.22");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCardPayment_CC210DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "CardPayment");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-3019.26");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment - Thank You");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-3019.26");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getBillPay_CC211DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Business Platinum MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-162.51");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Electricity Bill");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "1");
        inputParam.put("scheduledDate", "+14");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getUserBill1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("billDueDate", "2019-05-18");
        inputParam.put("paidDate", "2017-12-20");
        inputParam.put("description", "AT&T_Mobile Phone");
        inputParam.put("dueAmount", "1200.00");
        inputParam.put("paidAmount", "1080.00");
        inputParam.put("balanceAmount", "120.00");
        inputParam.put("minimumDue", "10.00");
        inputParam.put("ebillURL", "https://retailbanking1.konycloud.com/dbimages/billpay_ebill.png");
        inputParam.put("statusDesc", "TRANSACTION_STATUS_SUCCESSFUL");
        inputParam.put("billGeneratedDate", "2016-12-21");
        return inputParam;
    }

    public Map<String, String> getUserBill2DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("billDueDate", "2019-09-21");
        inputParam.put("paidDate", "2017-09-02");
        inputParam.put("description", "Electricity - ABC");
        inputParam.put("dueAmount", "1570.00");
        inputParam.put("paidAmount", "1000.00");
        inputParam.put("balanceAmount", "570.00");
        inputParam.put("minimumDue", "10.00");
        inputParam.put("ebillURL", "https://retailbanking1.konycloud.com/dbimages/billpay_ebill.png");
        inputParam.put("statusDesc", "TRANSACTION_STATUS_SUCCESSFUL");
        inputParam.put("billGeneratedDate", "2016-09-18");
        return inputParam;
    }

    public Map<String, String> getPayee1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("accountNumber", "7790396982957");
        inputParam.put("addressLine1", "ABC Energy LLC");
        inputParam.put("addressLine2", "200 East Post Road");
        inputParam.put("state", "Florida");
        inputParam.put("cityName", "Orlando");
        inputParam.put("nickName", "Electricity - ABC");
        inputParam.put("companyName", "ABC Energy");
        inputParam.put("email", "electricity@gmail.com");
        inputParam.put("firstName", "Electricity");
        inputParam.put("lastName", "Bill");
        inputParam.put("name", "ABC Energy");
        inputParam.put("eBillEnable", "1");
        inputParam.put("isAutoPayEnabled", "0");
        inputParam.put("nameOnBill", "John Bailey");
        inputParam.put("phone", "+91-4351348905");
        inputParam.put("zipCode", "759634");
        inputParam.put("softDelete", "0");
        return inputParam;
    }

    public Map<String, String> getPayee2DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("accountNumber", "158930572850356");
        inputParam.put("addressLine1", "American Family Insurance");
        inputParam.put("addressLine2", "70 Bowman Street");
        inputParam.put("state", "Texas");
        inputParam.put("cityName", "Austin");
        inputParam.put("nickName", "Family Insurance");
        inputParam.put("companyName", "American Family Insurance");
        inputParam.put("email", "american@gmail.com");
        inputParam.put("firstName", "Family");
        inputParam.put("lastName", "Insurance");
        inputParam.put("name", "American Family Insurance");
        inputParam.put("eBillEnable", "0");
        inputParam.put("isAutoPayEnabled", "0");
        inputParam.put("nameOnBill", "John Bailey");
        inputParam.put("phone", "+91-1501273981");
        inputParam.put("zipCode", "459667");
        inputParam.put("softDelete", "0");
        return inputParam;
    }

    public Map<String, String> getPayee3DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("accountNumber", "6890396982929");
        inputParam.put("addressLine1", "CITI Business");
        inputParam.put("addressLine2", "15 Boltonfield St");
        inputParam.put("state", "Florida");
        inputParam.put("cityName", "Orlando");
        inputParam.put("nickName", "Citi Credit Card");
        inputParam.put("companyName", "CitiBank Credit Cards");
        inputParam.put("email", "citiBank@gmail.com");
        inputParam.put("firstName", "Citi");
        inputParam.put("lastName", "Card");
        inputParam.put("name", "CitiBank Credit Cards");
        inputParam.put("eBillEnable", "1");
        inputParam.put("isAutoPayEnabled", "0");
        inputParam.put("nameOnBill", "John Bailey");
        inputParam.put("phone", "+91-1273981272");
        inputParam.put("zipCode", "759634");
        inputParam.put("softDelete", "0");
        return inputParam;
    }

    public Map<String, String> getPayee4DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("accountNumber", "6890396982939");
        inputParam.put("addressLine1", "AT&T");
        inputParam.put("addressLine2", "12 Down St");
        inputParam.put("state", "Texas");
        inputParam.put("cityName", "Dallas");
        inputParam.put("nickName", "AT&T_Mobile Phone");
        inputParam.put("companyName", "AT&T Inc");
        inputParam.put("email", "cableBill@gmail.com");
        inputParam.put("firstName", "My Mobile");
        inputParam.put("lastName", "Bill");
        inputParam.put("name", "AT&T Mobile");
        inputParam.put("eBillEnable", "1");
        inputParam.put("isAutoPayEnabled", "0");
        inputParam.put("nameOnBill", "John Bailey");
        inputParam.put("phone", "+91-938899898");
        inputParam.put("zipCode", "42110");
        inputParam.put("softDelete", "0");
        return inputParam;
    }

    public Map<String, String> getPayee5DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("accountNumber", "11433468761745");
        inputParam.put("addressLine1", "514S");
        inputParam.put("addressLine2", "Magnolia St.");
        inputParam.put("state", "Florida");
        inputParam.put("cityName", "Orlando");
        inputParam.put("nickName", "City Water Works");
        inputParam.put("companyName", "Virginia Home Rentals");
        inputParam.put("email", "rent@gmail.com");
        inputParam.put("firstName", "Water");
        inputParam.put("lastName", "Works");
        inputParam.put("name", "Water Works");
        inputParam.put("eBillEnable", "1");
        inputParam.put("isAutoPayEnabled", "0");
        inputParam.put("nameOnBill", "John Bailey");
        inputParam.put("phone", "");
        inputParam.put("zipCode", "42110");
        inputParam.put("softDelete", "0");
        return inputParam;
    }

    public Map<String, String> getPayee6DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("accountNumber", "9611932872351");
        inputParam.put("addressLine1", "44 Shirley Ave");
        inputParam.put("addressLine2", "West Chicago");
        inputParam.put("state", "Texas");
        inputParam.put("cityName", "San Antonio");
        inputParam.put("nickName", "City Utilities Company");
        inputParam.put("companyName", "Beam Teleservices");
        inputParam.put("email", "wifi@gmail.com");
        inputParam.put("firstName", "City");
        inputParam.put("lastName", "Utilities");
        inputParam.put("name", "Beam Teleservices");
        inputParam.put("eBillEnable", "0");
        inputParam.put("isAutoPayEnabled", "0");
        inputParam.put("nameOnBill", "John Bailey");
        inputParam.put("phone", "+91-4523746832");
        inputParam.put("zipCode", "50098");
        inputParam.put("softDelete", "0");
        return inputParam;
    }

    public Map<String, String> getPerson1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("email", "judy.blume@gmail.com");
        inputParam.put("secondaryEmail", "jblume@gmail.com");
        inputParam.put("secondoryPhoneNumber", "+91-4567483920");
        inputParam.put("primaryContactForSending", "+91-8790146356");
        inputParam.put("firstName", "Judy");
        inputParam.put("lastName", "Blume");
        inputParam.put("phone", "+91-8790146356");
        inputParam.put("name", "Judy Blume");
        inputParam.put("nickName", "Judy");
        return inputParam;
    }

    public Map<String, String> getPerson2DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("primaryContactForSending", "6565178276");
        inputParam.put("firstName", "Arthur");
        inputParam.put("lastName", "Miller");
        inputParam.put("phone", "+91-5638294057");
        inputParam.put("name", "Arthur Miller");
        inputParam.put("nickName", "Miller");
        return inputParam;
    }

    public Map<String, String> getPerson3DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("email", "francis@gmail.com");
        inputParam.put("primaryContactForSending", "fordfrancis@gmail.com");
        inputParam.put("firstName", "Francis");
        inputParam.put("lastName", "Ford");
        inputParam.put("name", "Francis Ford");
        inputParam.put("nickName", "Francis F");
        return inputParam;
    }

    public Map<String, String> getPerson4DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("email", "peter@gmail.com");
        inputParam.put("secondaryEmail", "JacksonP@yahoo.com");
        inputParam.put("primaryContactForSending", "peter@gmail.com");
        inputParam.put("firstName", "Peter");
        inputParam.put("lastName", "Jackson");
        inputParam.put("name", "Peter Jackson");
        inputParam.put("nickName", "Peter J");
        return inputParam;
    }

    public Map<String, String> getExtAccount1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("nickName", "Alex Sion");
        inputParam.put("beneficiaryName", "Alex Sion");
        inputParam.put("accountType", "Savings");
        inputParam.put("countryName", "USA");
        inputParam.put("firstName", "Alex");
        inputParam.put("lastName", "Sion");
        inputParam.put("bankName", "National Bank");
        inputParam.put("notes", " ");
        inputParam.put("routingNumber", "765678987");
        inputParam.put("swiftCode", "0");
        inputParam.put("isInternationalAccount", "0");
        inputParam.put("isSameBankAccount", "0");
        inputParam.put("isVerified", "1");
        inputParam.put("createdOn", "2017-10-10");
        inputParam.put("accountNumber", "98287890273");
        return inputParam;
    }

    public Map<String, String> getExtAccount2DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("nickName", "Tom Purnell");
        inputParam.put("beneficiaryName", "Tom Purnell");
        inputParam.put("accountType", "Checking");
        inputParam.put("countryName", "USA");
        inputParam.put("firstName", "Tom");
        inputParam.put("lastName", "Purnell");
        inputParam.put("bankName", "CitiBank");
        inputParam.put("notes", " ");
        inputParam.put("routingNumber", "765678987");
        inputParam.put("swiftCode", "0");
        inputParam.put("isInternationalAccount", "0");
        inputParam.put("isSameBankAccount", "0");
        inputParam.put("isVerified", "1");
        inputParam.put("createdOn", "2016-11-07");
        inputParam.put("accountNumber", "5528789466");
        return inputParam;
    }

    public Map<String, String> getExtAccount3DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("nickName", "My BoA Account");
        inputParam.put("beneficiaryName", "John Bailey");
        inputParam.put("accountType", "Checking");
        inputParam.put("countryName", "USA");
        inputParam.put("firstName", "John");
        inputParam.put("lastName", "Bailey");
        inputParam.put("bankName", "Bank of America");
        inputParam.put("notes", " ");
        inputParam.put("routingNumber", "765678922");
        inputParam.put("swiftCode", "0");
        inputParam.put("isInternationalAccount", "0");
        inputParam.put("isSameBankAccount", "0");
        inputParam.put("isVerified", "0");
        inputParam.put("createdOn", "2018-01-07");
        inputParam.put("accountNumber", "29475930375");
        return inputParam;
    }

    public Map<String, String> getExtAccount4DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("nickName", "Dan Markus");
        inputParam.put("beneficiaryName", "Dan Markus");
        inputParam.put("accountType", "Checking");
        inputParam.put("countryName", "USA");
        inputParam.put("firstName", "Dan");
        inputParam.put("lastName", "Markus");
        inputParam.put("bankName", "Infinity");
        inputParam.put("notes", " ");
        inputParam.put("routingNumber", "0");
        inputParam.put("swiftCode", "0");
        inputParam.put("isInternationalAccount", "0");
        inputParam.put("isSameBankAccount", "1");
        inputParam.put("isVerified", "1");
        inputParam.put("createdOn", "2015-10-13");
        inputParam.put("accountNumber", "47834984272");
        return inputParam;
    }

    public Map<String, String> getExtAccount5DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("nickName", "Henry");
        inputParam.put("beneficiaryName", "Henry James");
        inputParam.put("accountType", "Savings");
        inputParam.put("countryName", "India");
        inputParam.put("firstName", "Henry");
        inputParam.put("lastName", "James");
        inputParam.put("bankName", "ICICI bank");
        inputParam.put("notes", " ");
        inputParam.put("routingNumber", "0");
        inputParam.put("swiftCode", "CTBAUS12");
        inputParam.put("isInternationalAccount", "1");
        inputParam.put("isSameBankAccount", "0");
        inputParam.put("isVerified", "1");
        inputParam.put("createdOn", "2017-12-28");
        inputParam.put("accountNumber", "64314698173");
        return inputParam;
    }

    public Map<String, String> getCard1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("action", "Activate");
        inputParam.put("card_Status", "Active");
        inputParam.put("availableCredit", "87161.00");
        inputParam.put("serviceProvider", "Visa");
        inputParam.put("billingAddress", "2076, Burnett Ln, Austin, Texas, 78790");
        inputParam.put("cardProductName", "Gold Debit Card");
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("expirationDate", "2019-12-12");
        inputParam.put("pinNumber", "1578");
        inputParam.put("withdrawlLimit", "214");
        inputParam.put("reason", "");
        inputParam.put("cardHolderName", "John Bailey");
        inputParam.put("secondaryCardHolder", "John Dane");
        inputParam.put("isInternational", "1");
        return inputParam;
    }

    public Map<String, String> getCard2DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("action", "Activate");
        inputParam.put("card_Status", "Active");
        inputParam.put("availableCredit", "12380");
        inputParam.put("serviceProvider", "Master Card");
        inputParam.put("billingAddress", "2076, Burnett Ln, Austin, Texas, 78791");
        inputParam.put("cardProductName", "Eazee Food Card");
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("expirationDate", "2018-12-11");
        inputParam.put("pinNumber", "1579");
        inputParam.put("withdrawlLimit", "524");
        inputParam.put("reason", "");
        inputParam.put("cardHolderName", "John Bailey");
        inputParam.put("secondaryCardHolder", "John Doe");
        return inputParam;
    }

    public Map<String, String> getCard3DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("action", "Activate");
        inputParam.put("card_Status", "Active");
        inputParam.put("availableCredit", "8513");
        inputParam.put("serviceProvider", "Master Card");
        inputParam.put("billingAddress", "2076, Burnett Ln, Austin, Texas, 78792");
        inputParam.put("cardProductName", "My Platinum Credit Card");
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("expirationDate", "2020-09-09");
        inputParam.put("pinNumber", "1580");
        inputParam.put("creditLimit", "20000");
        inputParam.put("reason", "");
        inputParam.put("cardHolderName", "John Bailey");
        inputParam.put("secondaryCardHolder", "John Doe");
        return inputParam;
    }

    public Map<String, String> geUserAlertDemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("bankingIDChange", "0");
        inputParam.put("communicationChange", "1");
        inputParam.put("dealsExpiring", "1");
        inputParam.put("newDealsAvailable", "1");
        inputParam.put("newPayeeAdded", "0");
        inputParam.put("passwordChange", "1");
        inputParam.put("passwordExpired", "1");
        inputParam.put("payeeDetailsUpdated", "1");
        return inputParam;
    }

    public Map<String, String> geAccountAlert1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("creditLimit", "0");
        inputParam.put("debitLimit", "1");
        inputParam.put("isEnabled", "1");
        inputParam.put("minimumBalance", "1");
        inputParam.put("successfulTransfer", "0");
        inputParam.put("checkClearance", "1");
        inputParam.put("balanceUpdate_PeriodId", "5");
        inputParam.put("PayementDueReminder_PeriodId", "4");
        inputParam.put("depositMaturityReminder_PeriodId", "4");
        return inputParam;
    }

    public Map<String, String> geAccountAlert2DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("creditLimit", "0");
        inputParam.put("debitLimit", "1");
        inputParam.put("isEnabled", "1");
        inputParam.put("minimumBalance", "1");
        inputParam.put("successfulTransfer", "1");
        inputParam.put("checkClearance", "1");
        inputParam.put("balanceUpdate_PeriodId", "5");
        inputParam.put("PayementDueReminder_PeriodId", "4");
        inputParam.put("depositMaturityReminder_PeriodId", "4");
        return inputParam;
    }

    public Map<String, String> geAccountAlert3DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("creditLimit", "0");
        inputParam.put("debitLimit", "1");
        inputParam.put("isEnabled", "1");
        inputParam.put("minimumBalance", "1");
        inputParam.put("successfulTransfer", "0");
        inputParam.put("checkClearance", "1");
        inputParam.put("balanceUpdate_PeriodId", "5");
        inputParam.put("PayementDueReminder_PeriodId", "4");
        inputParam.put("depositMaturityReminder_PeriodId", "4");
        return inputParam;
    }

    public Map<String, String> geAccountAlert4DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("creditLimit", "0");
        inputParam.put("debitLimit", "1");
        inputParam.put("isEnabled", "1");
        inputParam.put("minimumBalance", "1");
        inputParam.put("successfulTransfer", "0");
        inputParam.put("checkClearance", "1");
        inputParam.put("balanceUpdate_PeriodId", "5");
        inputParam.put("PayementDueReminder_PeriodId", "4");
        inputParam.put("depositMaturityReminder_PeriodId", "4");
        return inputParam;
    }

    public Map<String, String> getRecentChkDeposit1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("amount", "500");
        inputParam.put("checkNumber", "19805");
        inputParam.put("description", "Check 19805");
        inputParam.put("notes", "From Mom");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "5941.78");
        inputParam.put("fromAccountBalance", "5941.78");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("frequencyType", "Once");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    public Map<String, String> getRecentChkDeposit2DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("amount", "1450");
        inputParam.put("checkNumber", "19802");
        inputParam.put("description", "Check 19802");
        inputParam.put("notes", "Received from Jane for rent");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "5940");
        inputParam.put("fromAccountBalance", "5940");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("frequencyType", "Once");
        inputParam.put("isScheduled", "0");
        return inputParam;
    }

    public Map<String, String> getRecentCardLessWithdrawal1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("amount", "500");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Cardless withdrawal to Self");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("toAccountBalance", "37000");
        inputParam.put("fromAccountBalance", "5792");
        inputParam.put("cashlessMode", "Self");
        inputParam.put("cashlessOTP", "789429");
        inputParam.put("cashlessOTPValidDate", "1");
        inputParam.put("cashWithdrawalTransactionStatus", "pending");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("frequencyType", "Once");
        inputParam.put("frequencyStartDate", "2018-08-31");
        inputParam.put("frequencyEndDate", "2018-08-31");
        inputParam.put("isScheduled", "0");
        inputParam.put("fromAccountNumber", "Progress Business Checking");

        return inputParam;
    }

    public Map<String, String> getRecentCardLessWithdrawal2DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("amount", "750");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Cardless withdrawal to Henry");
        inputParam.put("notes", "To plumber");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("toAccountBalance", "62782");
        inputParam.put("fromAccountBalance", "5020");
        inputParam.put("cashlessMode", "others");
        inputParam.put("cashlessOTP", "572839");
        inputParam.put("cashlessOTPValidDate", "1");
        inputParam.put("cashWithdrawalTransactionStatus", "pending");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("frequencyType", "Once");
        inputParam.put("frequencyStartDate", "2018-08-29");
        inputParam.put("frequencyEndDate", "2018-08-29");
        inputParam.put("isScheduled", "0");
        inputParam.put("fromAccountNumber", "Progress Business Checking");
        return inputParam;
    }

    public Map<String, String> getRecentCardLessWithdrawal3DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("amount", "200");
        inputParam.put("checkNumber", "0");
        inputParam.put("description", "Cardless withdrawal to Jane Doe");
        inputParam.put("notes", "Misc expenses");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("toAccountBalance", "25649");
        inputParam.put("fromAccountBalance", "9834");
        inputParam.put("cashlessMode", "others");
        inputParam.put("cashlessOTP", "324692");
        inputParam.put("cashlessOTPValidDate", "1");
        inputParam.put("cashWithdrawalTransactionStatus", "pending");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("frequencyType", "Once");
        inputParam.put("frequencyStartDate", "2018-08-13");
        inputParam.put("frequencyEndDate", "2018-08-13");
        inputParam.put("isScheduled", "0");
        inputParam.put("fromAccountNumber", "Progress Business Checking");
        return inputParam;
    }

    public Map<String, String> getLoanPayment_Loan1DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "LoanPayment");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Advantage Term Loan");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "368");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "16024.2288358465");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getLoanPayment_Loan2DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "LoanPayment");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Advantage Term Loan");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "368");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "16361.5509278567");
        inputParam.put("createdDate", "-37");
        inputParam.put("transactionDate", "-37");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getLoanPayment_Loan3DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "LoanPayment");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Advantage Term Loan");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "368");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "16698.2417246231");
        inputParam.put("createdDate", "-67");
        inputParam.put("transactionDate", "-67");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getTax_Loan4DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "Tax");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Advantage Term Loan");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "10");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "County tax");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "17034.3024076088");
        inputParam.put("createdDate", "-97");
        inputParam.put("transactionDate", "-97");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getLoanPayment_Loan5DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "LoanPayment");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Advantage Term Loan");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "368");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "17034.3024076088");
        inputParam.put("createdDate", "-97");
        inputParam.put("transactionDate", "-97");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getLoanPayment_Loan6DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "LoanPayment");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Advantage Term Loan");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "368");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "17369.7341560662");
        inputParam.put("createdDate", "-127");
        inputParam.put("transactionDate", "-127");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getLoanPayment_Loan7DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "LoanPayment");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Advantage Term Loan");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "368");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "17704.5381470405");
        inputParam.put("createdDate", "-157");
        inputParam.put("transactionDate", "-157");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getLoanPayment_Loan8DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "LoanPayment");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Advantage Term Loan");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "368");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "18038.7155553741");
        inputParam.put("createdDate", "-187");
        inputParam.put("transactionDate", "-187");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getLoanPayment_Loan9DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "LoanPayment");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Advantage Term Loan");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "368");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "18372.2675537109");
        inputParam.put("createdDate", "-217");
        inputParam.put("transactionDate", "-217");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getLoanPayment_Loan10DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "LoanPayment");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Advantage Term Loan");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "368");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "18705.1953125");
        inputParam.put("createdDate", "-247");
        inputParam.put("transactionDate", "-247");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getLoanPayment_Loan11DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "LoanPayment");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Advantage Term Loan");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "1000");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Principal Payment");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "19037.5");
        inputParam.put("createdDate", "-277");
        inputParam.put("transactionDate", "-277");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getCredit_Loan12DemoData() {
        Map<String, String> inputParam = new HashMap<String, String>();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Advantage Term Loan");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-20000");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "New loan setup");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "20000");
        inputParam.put("createdDate", "-317");
        inputParam.put("transactionDate", "-317");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInterestCredit_Deposit1DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InterestCredit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Direct Term Deposit");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "0.23");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Interest on Term Deposit");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInterestCredit_Deposit2DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InterestCredit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Direct Term Deposit");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "0.18");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Interest on Term Deposit");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "");
        inputParam.put("createdDate", "-14");
        inputParam.put("transactionDate", "-14");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getInterestCredit_Deposit3DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "InterestCredit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Direct Term Deposit");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "0.17");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Interest on Term Deposit");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "");
        inputParam.put("createdDate", "-21");
        inputParam.put("transactionDate", "-21");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map<String, String> getDeposit_Deposit4DemoData() {
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("transactionType", "Deposit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Direct Term Deposit");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "5000");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Principal Deposit");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "");
        inputParam.put("createdDate", "-28");
        inputParam.put("transactionDate", "-28");
        inputParam.put("viewReportLink", "http://retailbanking1.konycloud.com/dbimages/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_Checking2_1DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit ");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "5916.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment from Acme Inc.: 1921");
        inputParam.put("notes", "Bulk Order");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "287720.03");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternalTransfer_Checking2_2DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternalTransfer");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "Business Platinum Credit Card");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-2500");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online transfer Freedom Credit Card Masked Card Number");
        inputParam.put("notes", "Jan credit card bill");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "281803.12");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternalTransfer_Checking2_3DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternalTransfer");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "Business Advantage Savings");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-16000");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online transfer Rewards Savings Masked Account Number");
        inputParam.put("notes", "Development fund");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "284303.12");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternalTransfer_Checking2_4DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternalTransfer");
        inputParam.put("fromAccountNumber", "Business Advantage Savings");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "50000");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online transfer Rewards Savings Masked Account Number");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "300303.12");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCheckDeposit_Checking2_5DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "CheckDeposit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "2006");
        inputParam.put("checkNumber", "32174");
        inputParam.put("description", "Check 32174");
        inputParam.put("notes", "Payment ");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "250303.12");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getBillPay_Checking2_6DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Bill Pay");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-30");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "TFC Traders Inc. transfer Conf# 2fqjh376b; Arthur");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "248297.12");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInterest_Checking2_7DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Interest");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "176.25");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Interest credit Jan");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "248327.12");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getWithdrawal_Checking2_8DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Withdrawal");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-200");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "ATM-KonyBankAustin70123");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "248150.87");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getBillPay_Checking2_9DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Bill Pay");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-1000");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to City Waterworks");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "248350.87");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_Checking2_10DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-12000.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amaze Supplies");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "249350.87");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_Checking2_11DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "8650.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Deposit");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "261350.99");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_Checking2_12DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34653");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Deposit");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "252700.08");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_Checking2_13DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-750.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS HEB Traders #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "218047.08");
        inputParam.put("createdDate", "-9");
        inputParam.put("transactionDate", "-9");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_Checking2_14DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "218797.99");
        inputParam.put("createdDate", "-9");
        inputParam.put("transactionDate", "-9");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_Checking2_15DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "218835.18");
        inputParam.put("createdDate", "-9");
        inputParam.put("transactionDate", "-9");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_Checking2_16DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "45624");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Deposit");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "218873.22");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_Checking2_17DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "58245");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Deposit");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "173249.22");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getBillPay_Checking2_18DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "115004.22");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_Checking2_19DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "115132.2");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_Checking2_20DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment to TFC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "115200.39");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_Checking2_21DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment from Jane Cal: 12345");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "115320.51");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_Checking2_22DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "60.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment from Ashton.: 19452");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "115239.6");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_Checking2_23DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "345");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment from Esther Green.: 19453");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "115178.69");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_Checking2_24DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "114833.69");
        inputParam.put("createdDate", "-11");
        inputParam.put("transactionDate", "-11");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_Checking2_25DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amazon.com");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "114901.88");
        inputParam.put("createdDate", "-11");
        inputParam.put("transactionDate", "-11");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_Checking2_26DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment from Steve.: 19452");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "115022");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_Checking2_27DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment from Allison Green.: 19453");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "114941.09");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_Checking2_28DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "114860.18");
        inputParam.put("createdDate", "-12");
        inputParam.put("transactionDate", "-12");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCardPayment_Checking2_29DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "CardPayment");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "3019.26");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment - Thank You");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "114936.09");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getBillPay_Checking2_30DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-869.14");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Kony Bank credit card bill payment");
        inputParam.put("notes", "Citi Credit Card Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "111916.83");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_Checking2_31DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "20");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Temporary credit adjustment Card XX0203 Claim 180111112749");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "112785.97");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getExternalTransfer_Checking2_32DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "ExternalTransfer");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-195");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online External Fund Transfer to Tom Burnett");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "112765.97");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_Checking2_33DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit ");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "2416.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bakers Paradise Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "112960.97");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_Checking2_34DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-206.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Best Buy 5th Avenue");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "110544.06");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCheckDeposit_Checking2_35DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "CheckDeposit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "2200");
        inputParam.put("checkNumber", "19806");
        inputParam.put("description", "Check 19806");
        inputParam.put("notes", "From Dan");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "110750.25");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getExternalTransfer_Checking2_36DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "ExternalTransfer");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-500");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "WireTransfer to JennaHastings");
        inputParam.put("notes", "For Euro Trip");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "108550.25");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_Checking2_37DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "234");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment from Jane Cal: 12345");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "109050.25");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getBillPay_Checking2_38DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-40.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Checkcard 2378 Vonage *Price+Taxes 982-221-8387 Recurring");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Monthly");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "108816.25");
        inputParam.put("createdDate", "-140");
        inputParam.put("transactionDate", "-140");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "1");
        inputParam.put("scheduledDate", "+7");
        inputParam.put("frequencyStartDate", "-140");
        inputParam.put("frequencyEndDate", "+140");
        return inputParam;
    }

    public Map getExternalTransfer_Checking2_39DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "ExternalTransfer");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-100");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online External Fund Transfer to JohnBailey");
        inputParam.put("notes", "Transfer to my BoA Account for school fee");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Monthly");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "108856.44");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "1");
        inputParam.put("scheduledDate", "+7");
        inputParam.put("frequencyStartDate", "+7");
        inputParam.put("frequencyEndDate", "+372");
        return inputParam;
    }

    public Map getBillPay_Checking2_40DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-35");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "TFC Traders Inc.  Conf# 2fqjh376b; Francis");
        inputParam.put("notes", "Coffee at Star Bucks");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "108956.44");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "1");
        inputParam.put("scheduledDate", "+21");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_Checking2_41DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "35");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment from Y Green.: 19453");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "108991.44");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_Checking2_42DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment from Jane Cal: 12345");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "108956.44");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_Checking2_43DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "60.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment from Ashton.: 19452");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "108875.53");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_Checking2_44DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "345");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment from Esther Green.: 19453");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "108814.62");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_Checking2_45DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment from Jane Cal: 12345");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "108469.62");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_Checking2_46DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "60.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment from Ashton.: 19452");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "108388.71");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_Checking2_47DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "345");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment from Esther Green.: 19453");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "108327.8");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getBillPay_Checking2_48DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Bill Pay");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-1000");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to City Waterworks");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "107982.8");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_Checking2_49DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-12000.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amaze Supplies");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "108982.8");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_Checking2_50DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "8650.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Deposit");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "120982.92");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getBillPay_Checking2_51DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "112332.01");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_Checking2_52DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "60.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment from Ashton.: 19452");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "112459.99");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_Checking2_53DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "345");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment from Esther Green.: 19453");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "112399.08");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getBillPay_Checking2_54DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Bill Pay");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-1000");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to City Waterworks");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "112054.08");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_Checking2_55DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-12000.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amaze Supplies");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "113054.08");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_Checking2_56DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "8650.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Deposit");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "125054.2");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getBillPay_Checking2_57DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "116403.29");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_Checking2_58DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment from Jane Cal: 12345");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "116531.27");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_Checking2_59DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "60.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment from Ashton.: 19452");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "116450.36");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternalTransfer_Checking2_60DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternalTransfer");
        inputParam.put("fromAccountNumber", "Business Advantage Savings");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "50000");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online transfer Rewards Savings Masked Account Number");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "167341.7");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCheckDeposit_Checking2_61DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "CheckDeposit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "2006");
        inputParam.put("checkNumber", "32174");
        inputParam.put("description", "Check 32174");
        inputParam.put("notes", "Payment ");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "117341.7");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getBillPay_Checking2_62DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Bill Pay");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-30");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "TFC Traders Inc. transfer Conf# 2fqjh376b; Arthur");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "115335.7");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInterest_Checking2_63DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Interest");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "176.25");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Interest creditJan");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "115365.7");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getWithdrawal_Checking2_64DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Withdrawal");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-200");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "ATM-KonyBankAustin70123");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "115189.45");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getBillPay_Checking2_65DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Bill Pay");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-1000");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to City Waterworks");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "115389.45");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_Checking2_66DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "345");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment from Esther Green.: 19453");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "116389.45");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_Checking2_67DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "60.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment from Ashton.: 19452");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "116044.45");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_Checking2_68DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "345");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment from Esther Green.: 19453");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "115983.54");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getBillPay_Checking2_69DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Bill Pay");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-1000");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to City Waterworks");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "115638.54");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_Checking2_70DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-12000.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on Amaze Supplies");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "116638.54");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_Checking2_71DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "8650.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Deposit");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "128638.66");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getBillPay_Checking2_72DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Pro Business Checking");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Jan Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "119987.75");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_Checking2_73DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment from Jane Cal: 12345");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "120115.73");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_Checking2_74DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "60.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment from Ashton.: 19452");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "120034.82");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_Checking2_75DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Pro Business Checking");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "345");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment from Esther Green.: 19453");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "119973.91");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_1DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "480.97");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Shell Oil 2798739008");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2507.35");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternalTransfer_CC2_2DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternalTransfer");
        inputParam.put("fromAccountNumber", "Rewards Savings");
        inputParam.put("toAccountNumber", "Business Gold MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-25");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online payment from SAV 18");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2026.38");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_3DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2051.38");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_4DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Gold MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2017.17");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getBillPay_CC2_5DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2098.08");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_6DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1970.1");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCardPayment_CC2_7DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "CardPayment");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Gold MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-3000");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment - Thank You");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1901.91");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_8DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4901.91");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_9DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4883.72");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_10DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS HEB Round Rock #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4688.69");
        inputParam.put("createdDate", "-8");
        inputParam.put("transactionDate", "-8");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_11DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4612.78");
        inputParam.put("createdDate", "-9");
        inputParam.put("transactionDate", "-9");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_12DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4575.59");
        inputParam.put("createdDate", "-9");
        inputParam.put("transactionDate", "-9");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_13DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4537.55");
        inputParam.put("createdDate", "-9");
        inputParam.put("transactionDate", "-9");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_CC2_14DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Gold MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4503.34");
        inputParam.put("createdDate", "-9");
        inputParam.put("transactionDate", "-9");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getBillPay_CC2_15DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4584.25");
        inputParam.put("createdDate", "-9");
        inputParam.put("transactionDate", "-9");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_16DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4456.27");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_17DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4388.08");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_18DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4267.96");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_19DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4249.77");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_20DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "4054.74");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_21DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3978.83");
        inputParam.put("createdDate", "-10");
        inputParam.put("transactionDate", "-10");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_22DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3941.64");
        inputParam.put("createdDate", "-11");
        inputParam.put("transactionDate", "-11");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_23DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3903.6");
        inputParam.put("createdDate", "-11");
        inputParam.put("transactionDate", "-11");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_CC2_24DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Gold MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3869.39");
        inputParam.put("createdDate", "-11");
        inputParam.put("transactionDate", "-11");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getBillPay_CC2_25DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3950.3");
        inputParam.put("createdDate", "-11");
        inputParam.put("transactionDate", "-11");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_26DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3822.32");
        inputParam.put("createdDate", "-11");
        inputParam.put("transactionDate", "-11");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_27DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3754.13");
        inputParam.put("createdDate", "-12");
        inputParam.put("transactionDate", "-12");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_28DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3634.01");
        inputParam.put("createdDate", "-12");
        inputParam.put("transactionDate", "-12");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_29DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3615.82");
        inputParam.put("createdDate", "-12");
        inputParam.put("transactionDate", "-12");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_30DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3420.79");
        inputParam.put("createdDate", "-12");
        inputParam.put("transactionDate", "-12");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_31DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3344.88");
        inputParam.put("createdDate", "-12");
        inputParam.put("transactionDate", "-12");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_32DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3276.69");
        inputParam.put("createdDate", "-13");
        inputParam.put("transactionDate", "-13");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_33DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3156.57");
        inputParam.put("createdDate", "-13");
        inputParam.put("transactionDate", "-13");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_34DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "3138.38");
        inputParam.put("createdDate", "-13");
        inputParam.put("transactionDate", "-13");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_35DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2943.35");
        inputParam.put("createdDate", "-13");
        inputParam.put("transactionDate", "-13");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_36DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2867.44");
        inputParam.put("createdDate", "-13");
        inputParam.put("transactionDate", "-13");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_37DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2830.25");
        inputParam.put("createdDate", "-14");
        inputParam.put("transactionDate", "-14");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_38DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2792.21");
        inputParam.put("createdDate", "-14");
        inputParam.put("transactionDate", "-14");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_CC2_39DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Gold MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2758");
        inputParam.put("createdDate", "-14");
        inputParam.put("transactionDate", "-14");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getBillPay_CC2_40DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2838.91");
        inputParam.put("createdDate", "-14");
        inputParam.put("transactionDate", "-14");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_41DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2710.93");
        inputParam.put("createdDate", "-14");
        inputParam.put("transactionDate", "-14");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_42DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2642.74");
        inputParam.put("createdDate", "-15");
        inputParam.put("transactionDate", "-15");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_43DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2522.62");
        inputParam.put("createdDate", "-15");
        inputParam.put("transactionDate", "-15");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_44DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2504.43");
        inputParam.put("createdDate", "-15");
        inputParam.put("transactionDate", "-15");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_45DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2309.4");
        inputParam.put("createdDate", "-15");
        inputParam.put("transactionDate", "-15");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_46DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2233.49");
        inputParam.put("createdDate", "-15");
        inputParam.put("transactionDate", "-15");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_47DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2196.3");
        inputParam.put("createdDate", "-16");
        inputParam.put("transactionDate", "-16");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_48DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2158.26");
        inputParam.put("createdDate", "-16");
        inputParam.put("transactionDate", "-16");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_CC2_49DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Gold MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2124.05");
        inputParam.put("createdDate", "-16");
        inputParam.put("transactionDate", "-16");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getBillPay_CC2_50DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2204.96");
        inputParam.put("createdDate", "-16");
        inputParam.put("transactionDate", "-16");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_51DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2076.98");
        inputParam.put("createdDate", "-16");
        inputParam.put("transactionDate", "-16");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_52DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "2008.79");
        inputParam.put("createdDate", "-16");
        inputParam.put("transactionDate", "-16");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_53DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1888.67");
        inputParam.put("createdDate", "-17");
        inputParam.put("transactionDate", "-17");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_54DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1870.48");
        inputParam.put("createdDate", "-17");
        inputParam.put("transactionDate", "-17");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_55DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1675.45");
        inputParam.put("createdDate", "-17");
        inputParam.put("transactionDate", "-17");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_56DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1599.54");
        inputParam.put("createdDate", "-17");
        inputParam.put("transactionDate", "-17");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_57DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1562.35");
        inputParam.put("createdDate", "-17");
        inputParam.put("transactionDate", "-17");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_58DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1524.31");
        inputParam.put("createdDate", "-17");
        inputParam.put("transactionDate", "-17");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_CC2_59DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Gold MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1490.1");
        inputParam.put("createdDate", "-18");
        inputParam.put("transactionDate", "-18");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getBillPay_CC2_60DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1571.01");
        inputParam.put("createdDate", "-18");
        inputParam.put("transactionDate", "-18");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_61DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1443.03");
        inputParam.put("createdDate", "-18");
        inputParam.put("transactionDate", "-18");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_62DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1374.84");
        inputParam.put("createdDate", "-18");
        inputParam.put("transactionDate", "-18");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_63DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1254.72");
        inputParam.put("createdDate", "-18");
        inputParam.put("transactionDate", "-18");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_64DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1236.53");
        inputParam.put("createdDate", "-18");
        inputParam.put("transactionDate", "-18");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_65DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1041.5");
        inputParam.put("createdDate", "-19");
        inputParam.put("transactionDate", "-19");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_66DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "965.590000000001");
        inputParam.put("createdDate", "-19");
        inputParam.put("transactionDate", "-19");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_67DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "928.400000000001");
        inputParam.put("createdDate", "-19");
        inputParam.put("transactionDate", "-19");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_68DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "890.360000000001");
        inputParam.put("createdDate", "-19");
        inputParam.put("transactionDate", "-19");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_CC2_69DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Gold MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "856.150000000001");
        inputParam.put("createdDate", "-19");
        inputParam.put("transactionDate", "-19");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getBillPay_CC2_70DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "937.060000000001");
        inputParam.put("createdDate", "-19");
        inputParam.put("transactionDate", "-19");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_71DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "809.080000000001");
        inputParam.put("createdDate", "-20");
        inputParam.put("transactionDate", "-20");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_72DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "740.890000000001");
        inputParam.put("createdDate", "-20");
        inputParam.put("transactionDate", "-20");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_73DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "620.770000000001");
        inputParam.put("createdDate", "-20");
        inputParam.put("transactionDate", "-20");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_74DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "602.580000000001");
        inputParam.put("createdDate", "-20");
        inputParam.put("transactionDate", "-20");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_75DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "407.550000000001");
        inputParam.put("createdDate", "-20");
        inputParam.put("transactionDate", "-20");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_CC2_76DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Gold MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "331.640000000001");
        inputParam.put("createdDate", "-21");
        inputParam.put("transactionDate", "-21");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getBillPay_CC2_77DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "412.550000000001");
        inputParam.put("createdDate", "-21");
        inputParam.put("transactionDate", "-21");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_78DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "284.570000000001");
        inputParam.put("createdDate", "-21");
        inputParam.put("transactionDate", "-21");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_79DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "216.380000000001");
        inputParam.put("createdDate", "-21");
        inputParam.put("transactionDate", "-21");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_80DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "96.2600000000009");
        inputParam.put("createdDate", "-21");
        inputParam.put("transactionDate", "-21");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_81DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "78.0700000000009");
        inputParam.put("createdDate", "-21");
        inputParam.put("transactionDate", "-21");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_82DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-116.959999999999");
        inputParam.put("createdDate", "-22");
        inputParam.put("transactionDate", "-22");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_83DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-192.869999999999");
        inputParam.put("createdDate", "-22");
        inputParam.put("transactionDate", "-22");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_84DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-261.059999999999");
        inputParam.put("createdDate", "-22");
        inputParam.put("transactionDate", "-22");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_85DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-381.179999999999");
        inputParam.put("createdDate", "-22");
        inputParam.put("transactionDate", "-22");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_86DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-399.369999999999");
        inputParam.put("createdDate", "-22");
        inputParam.put("transactionDate", "-22");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_87DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-594.399999999999");
        inputParam.put("createdDate", "-22");
        inputParam.put("transactionDate", "-22");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_88DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-670.309999999999");
        inputParam.put("createdDate", "-23");
        inputParam.put("transactionDate", "-23");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_89DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-707.499999999999");
        inputParam.put("createdDate", "-23");
        inputParam.put("transactionDate", "-23");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_90DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-745.539999999999");
        inputParam.put("createdDate", "-23");
        inputParam.put("transactionDate", "-23");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_CC2_91DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Gold MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-779.749999999999");
        inputParam.put("createdDate", "-23");
        inputParam.put("transactionDate", "-23");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getBillPay_CC2_92DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-698.839999999999");
        inputParam.put("createdDate", "-23");
        inputParam.put("transactionDate", "-23");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_93DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-826.819999999999");
        inputParam.put("createdDate", "-23");
        inputParam.put("transactionDate", "-23");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_94DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-895.009999999999");
        inputParam.put("createdDate", "-24");
        inputParam.put("transactionDate", "-24");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_95DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1015.13");
        inputParam.put("createdDate", "-24");
        inputParam.put("transactionDate", "-24");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_96DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1033.32");
        inputParam.put("createdDate", "-24");
        inputParam.put("transactionDate", "-24");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_97DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1228.35");
        inputParam.put("createdDate", "-24");
        inputParam.put("transactionDate", "-24");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_98DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1304.26");
        inputParam.put("createdDate", "-24");
        inputParam.put("transactionDate", "-24");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_99DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1341.45");
        inputParam.put("createdDate", "-24");
        inputParam.put("transactionDate", "-24");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_100DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1379.49");
        inputParam.put("createdDate", "-25");
        inputParam.put("transactionDate", "-25");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_CC2_101DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Gold MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1413.7");
        inputParam.put("createdDate", "-25");
        inputParam.put("transactionDate", "-25");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getBillPay_CC2_102DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1332.79");
        inputParam.put("createdDate", "-25");
        inputParam.put("transactionDate", "-25");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_103DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1460.77");
        inputParam.put("createdDate", "-25");
        inputParam.put("transactionDate", "-25");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_104DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1528.96");
        inputParam.put("createdDate", "-25");
        inputParam.put("transactionDate", "-25");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_105DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1649.08");
        inputParam.put("createdDate", "-25");
        inputParam.put("transactionDate", "-25");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_106DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1667.27");
        inputParam.put("createdDate", "-26");
        inputParam.put("transactionDate", "-26");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_107DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1862.3");
        inputParam.put("createdDate", "-26");
        inputParam.put("transactionDate", "-26");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_108DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1938.21");
        inputParam.put("createdDate", "-26");
        inputParam.put("transactionDate", "-26");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_109DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1975.4");
        inputParam.put("createdDate", "-26");
        inputParam.put("transactionDate", "-26");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_110DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2013.44");
        inputParam.put("createdDate", "-26");
        inputParam.put("transactionDate", "-26");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_CC2_111DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Gold MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2047.65");
        inputParam.put("createdDate", "-26");
        inputParam.put("transactionDate", "-26");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getBillPay_CC2_112DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1966.74");
        inputParam.put("createdDate", "-27");
        inputParam.put("transactionDate", "-27");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_113DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2094.72");
        inputParam.put("createdDate", "-27");
        inputParam.put("transactionDate", "-27");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_114DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2162.91");
        inputParam.put("createdDate", "-27");
        inputParam.put("transactionDate", "-27");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_115DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2283.03");
        inputParam.put("createdDate", "-27");
        inputParam.put("transactionDate", "-27");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_116DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2301.22");
        inputParam.put("createdDate", "-27");
        inputParam.put("transactionDate", "-27");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_117DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2496.25");
        inputParam.put("createdDate", "-27");
        inputParam.put("transactionDate", "-27");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_118DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2572.16");
        inputParam.put("createdDate", "-28");
        inputParam.put("transactionDate", "-28");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_119DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2609.35");
        inputParam.put("createdDate", "-28");
        inputParam.put("transactionDate", "-28");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_120DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2647.39");
        inputParam.put("createdDate", "-28");
        inputParam.put("transactionDate", "-28");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_CC2_121DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Gold MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2681.6");
        inputParam.put("createdDate", "-28");
        inputParam.put("transactionDate", "-28");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getBillPay_CC2_122DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2600.69");
        inputParam.put("createdDate", "-28");
        inputParam.put("transactionDate", "-28");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_123DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2728.67");
        inputParam.put("createdDate", "-28");
        inputParam.put("transactionDate", "-28");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_124DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2796.86");
        inputParam.put("createdDate", "-28");
        inputParam.put("transactionDate", "-28");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_125DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2916.98");
        inputParam.put("createdDate", "-29");
        inputParam.put("transactionDate", "-29");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_126DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2935.17");
        inputParam.put("createdDate", "-29");
        inputParam.put("transactionDate", "-29");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCardPayment_CC2_127DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "CardPayment");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Gold MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-4800");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment - Thank You");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2972.36");
        inputParam.put("createdDate", "-29");
        inputParam.put("transactionDate", "-29");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_128DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1827.64");
        inputParam.put("createdDate", "-29");
        inputParam.put("transactionDate", "-29");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_CC2_129DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Gold MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1793.43");
        inputParam.put("createdDate", "-29");
        inputParam.put("transactionDate", "-29");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getBillPay_CC2_130DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1874.34");
        inputParam.put("createdDate", "-29");
        inputParam.put("transactionDate", "-29");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_131DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1746.36");
        inputParam.put("createdDate", "-30");
        inputParam.put("transactionDate", "-30");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_132DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1678.17");
        inputParam.put("createdDate", "-30");
        inputParam.put("transactionDate", "-30");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_133DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1558.05");
        inputParam.put("createdDate", "-30");
        inputParam.put("transactionDate", "-30");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_134DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1539.86");
        inputParam.put("createdDate", "-30");
        inputParam.put("transactionDate", "-30");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_135DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1344.83");
        inputParam.put("createdDate", "-30");
        inputParam.put("transactionDate", "-30");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_136DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1268.92");
        inputParam.put("createdDate", "-30");
        inputParam.put("transactionDate", "-30");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_137DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1231.73");
        inputParam.put("createdDate", "-31");
        inputParam.put("transactionDate", "-31");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_138DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1193.69");
        inputParam.put("createdDate", "-31");
        inputParam.put("transactionDate", "-31");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_CC2_139DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Gold MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1159.48");
        inputParam.put("createdDate", "-31");
        inputParam.put("transactionDate", "-31");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getBillPay_CC2_140DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1240.39");
        inputParam.put("createdDate", "-31");
        inputParam.put("transactionDate", "-31");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_142DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1112.41");
        inputParam.put("createdDate", "-31");
        inputParam.put("transactionDate", "-31");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_143DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1044.22");
        inputParam.put("createdDate", "-31");
        inputParam.put("transactionDate", "-31");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_144DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "924.1");
        inputParam.put("createdDate", "-32");
        inputParam.put("transactionDate", "-32");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_145DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "905.91");
        inputParam.put("createdDate", "-32");
        inputParam.put("transactionDate", "-32");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_146DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "710.88");
        inputParam.put("createdDate", "-32");
        inputParam.put("transactionDate", "-32");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_147DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "634.97");
        inputParam.put("createdDate", "-32");
        inputParam.put("transactionDate", "-32");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_148DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "597.78");
        inputParam.put("createdDate", "-32");
        inputParam.put("transactionDate", "-32");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_149DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "559.74");
        inputParam.put("createdDate", "-32");
        inputParam.put("transactionDate", "-32");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_CC2_150DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Gold MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "525.53");
        inputParam.put("createdDate", "-33");
        inputParam.put("transactionDate", "-33");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getBillPay_CC2_151DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "606.44");
        inputParam.put("createdDate", "-33");
        inputParam.put("transactionDate", "-33");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_153DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "478.46");
        inputParam.put("createdDate", "-33");
        inputParam.put("transactionDate", "-33");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_154DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "410.27");
        inputParam.put("createdDate", "-33");
        inputParam.put("transactionDate", "-33");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_155DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "290.15");
        inputParam.put("createdDate", "-33");
        inputParam.put("transactionDate", "-33");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_156DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "271.96");
        inputParam.put("createdDate", "-33");
        inputParam.put("transactionDate", "-33");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_157DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "76.9300000000001");
        inputParam.put("createdDate", "-34");
        inputParam.put("transactionDate", "-34");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_158DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "1.0200000000001");
        inputParam.put("createdDate", "-34");
        inputParam.put("transactionDate", "-34");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_159DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-36.1699999999999");
        inputParam.put("createdDate", "-34");
        inputParam.put("transactionDate", "-34");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_160DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-74.2099999999999");
        inputParam.put("createdDate", "-34");
        inputParam.put("transactionDate", "-34");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_CC2_161DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Gold MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-108.42");
        inputParam.put("createdDate", "-34");
        inputParam.put("transactionDate", "-34");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getBillPay_CC2_163DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-27.5099999999999");
        inputParam.put("createdDate", "-34");
        inputParam.put("transactionDate", "-34");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_164DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-155.49");
        inputParam.put("createdDate", "-34");
        inputParam.put("transactionDate", "-34");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_165DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-223.68");
        inputParam.put("createdDate", "-35");
        inputParam.put("transactionDate", "-35");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_166DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-343.8");
        inputParam.put("createdDate", "-35");
        inputParam.put("transactionDate", "-35");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_167DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-361.99");
        inputParam.put("createdDate", "-35");
        inputParam.put("transactionDate", "-35");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_168DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-557.02");
        inputParam.put("createdDate", "-35");
        inputParam.put("transactionDate", "-35");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_169DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-632.93");
        inputParam.put("createdDate", "-35");
        inputParam.put("transactionDate", "-35");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_171DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-670.12");
        inputParam.put("createdDate", "-35");
        inputParam.put("transactionDate", "-35");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_172DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-708.16");
        inputParam.put("createdDate", "-36");
        inputParam.put("transactionDate", "-36");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_CC2_173DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Gold MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-742.37");
        inputParam.put("createdDate", "-36");
        inputParam.put("transactionDate", "-36");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getBillPay_CC2_174DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-661.46");
        inputParam.put("createdDate", "-36");
        inputParam.put("transactionDate", "-36");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_175DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-789.44");
        inputParam.put("createdDate", "-36");
        inputParam.put("transactionDate", "-36");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_176DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-857.63");
        inputParam.put("createdDate", "-36");
        inputParam.put("transactionDate", "-36");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_177DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-977.75");
        inputParam.put("createdDate", "-36");
        inputParam.put("transactionDate", "-36");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_179DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-995.94");
        inputParam.put("createdDate", "-37");
        inputParam.put("transactionDate", "-37");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_180DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1190.97");
        inputParam.put("createdDate", "-37");
        inputParam.put("transactionDate", "-37");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_181DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1266.88");
        inputParam.put("createdDate", "-37");
        inputParam.put("transactionDate", "-37");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_182DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1304.07");
        inputParam.put("createdDate", "-37");
        inputParam.put("transactionDate", "-37");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_183DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1342.11");
        inputParam.put("createdDate", "-37");
        inputParam.put("transactionDate", "-37");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_CC2_184DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Gold MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1376.32");
        inputParam.put("createdDate", "-37");
        inputParam.put("transactionDate", "-37");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getBillPay_CC2_185DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1295.41");
        inputParam.put("createdDate", "-38");
        inputParam.put("transactionDate", "-38");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_186DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1423.39");
        inputParam.put("createdDate", "-38");
        inputParam.put("transactionDate", "-38");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_188DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1491.58");
        inputParam.put("createdDate", "-38");
        inputParam.put("transactionDate", "-38");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_189DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1611.7");
        inputParam.put("createdDate", "-38");
        inputParam.put("transactionDate", "-38");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_190DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1629.89");
        inputParam.put("createdDate", "-38");
        inputParam.put("transactionDate", "-38");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_191DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1824.92");
        inputParam.put("createdDate", "-38");
        inputParam.put("transactionDate", "-38");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_192DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-1900.83");
        inputParam.put("createdDate", "-39");
        inputParam.put("transactionDate", "-39");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_193DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2020.95");
        inputParam.put("createdDate", "-39");
        inputParam.put("transactionDate", "-39");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_195DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2039.14");
        inputParam.put("createdDate", "-39");
        inputParam.put("transactionDate", "-39");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_196DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2234.17");
        inputParam.put("createdDate", "-39");
        inputParam.put("transactionDate", "-39");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_197DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2310.08");
        inputParam.put("createdDate", "-39");
        inputParam.put("transactionDate", "-39");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_198DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2347.27");
        inputParam.put("createdDate", "-39");
        inputParam.put("transactionDate", "-39");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_199DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "34.21");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS WholeFoods #1838");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2385.31");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCredit_CC2_200DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "Credit");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Gold MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-80.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Refund from TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2419.52");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getBillPay_CC2_201DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "127.98");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Electricity Bill");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2338.61");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_203DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "68.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "The Home Depot #0919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2466.59");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_204DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "120.12");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Online Payment on TRC Traders");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2534.78");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_205DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "18.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS Nutrition 250 Store 23919");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2654.9");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_206DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "195.03");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS GRD Suppliers 5th Avenue 23121");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2673.09");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getPOS_CC2_207DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "POS");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "75.91");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "POS BelItaliano #91999");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2868.12");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_208DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "37.19");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2944.03");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getInternetTransaction_CC2_209DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "InternetTransaction");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "38.04");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Uber Technologies Inc 866-576-1039 CA");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-2981.22");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCardPayment_CC2_210DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "CardPayment");
        inputParam.put("fromAccountNumber", "");
        inputParam.put("toAccountNumber", "Business Gold MasterCard");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-3019.26");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Payment - Thank You");
        inputParam.put("notes", "");
        inputParam.put("statusDesc", "Successful");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "-3019.26");
        inputParam.put("createdDate", "-40");
        inputParam.put("transactionDate", "-40");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "0");
        inputParam.put("scheduledDate", "");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getBillPay_CC2_211DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("transactionType", "BillPay");
        inputParam.put("fromAccountNumber", "Business Gold MasterCard");
        inputParam.put("toAccountNumber", "");
        inputParam.put("toExternalAccountNumber", "");
        inputParam.put("amount", "-162.51");
        inputParam.put("checkNumber", "");
        inputParam.put("description", "Bill Pay to ABCEnergy");
        inputParam.put("notes", "Electricity Bill");
        inputParam.put("statusDesc", "Pending");
        inputParam.put("frequencyType", "Once");
        inputParam.put("toAccountBalance", "");
        inputParam.put("fromAccountBalance", "");
        inputParam.put("createdDate", "-7");
        inputParam.put("transactionDate", "-7");
        inputParam.put("viewReportLink", "http://pmqa.konylabs.net/KonyWebBanking/view_report.png");
        inputParam.put("numberOfRecurrences", "0");
        inputParam.put("isScheduled", "1");
        inputParam.put("scheduledDate", "+14");
        inputParam.put("frequencyStartDate", "");
        inputParam.put("frequencyEndDate", "");
        return inputParam;
    }

    public Map getCardTransaction1DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43959");
        inputParam.put("transactionTime", "11:30:23");
        inputParam.put("transactionDescription", "Sunny Bee, Clark Ave");
        //// inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "SUNNY BEE");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Grocery");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "Sunny Bee, Clark Ave");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-3");
        inputParam.put("transactionReferenceNumber", "92315215");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS1422");
        return inputParam;
    }

    public Map getCardTransaction2DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43959");
        inputParam.put("transactionTime", "11:30:25");
        inputParam.put("transactionDescription", "SRV CHGS");
        //// inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "SUNNY BEE");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Grocery");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "F");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "Sunny Bee, Clark Ave");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-0.015");
        inputParam.put("transactionReferenceNumber", "92315324");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "Y");
        inputParam.put("taxPercentage", "0.5");
        inputParam.put("transactionTaxAmount", "0.01");
        inputParam.put("transactionTerminalID", "POS1422");
        return inputParam;
    }

    public Map getCardTransaction3DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43959");
        inputParam.put("transactionTime", "13:03:48");
        inputParam.put("transactionDescription", "Amazon US");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "AMAZON");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Online Shopping");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "Amazon Online");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-25");
        inputParam.put("transactionReferenceNumber", "AM0242555");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "OL94242");
        return inputParam;
    }

    public Map getCardTransaction4DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43961");
        inputParam.put("transactionTime", "08:05:48");
        inputParam.put("transactionDescription", "Total Gas");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "TOTAL GAS");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Fuel");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "Total Gas, Mark St");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-8.25");
        inputParam.put("transactionReferenceNumber", "53536396363");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS225252");
        return inputParam;
    }

    public Map getCardTransaction5DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43963");
        inputParam.put("transactionTime", "10:00:35");
        inputParam.put("transactionDescription", "Starbucks");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "STARTBUCKS");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "Startbucks, Tower plaza");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-1.25");
        inputParam.put("transactionReferenceNumber", "242647758");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "QR235673888");
        return inputParam;
    }

    public Map getCardTransaction6DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43964");
        inputParam.put("transactionTime", "15:03:24");
        inputParam.put("transactionDescription", "Payment Received");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "ACE BANK");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Finance");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "ACE BANK");
        inputParam.put("transactionIndicator", "C");
        inputParam.put("transactionAmount", "350");
        inputParam.put("transactionReferenceNumber", "352362623");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "OL253536262");
        return inputParam;
    }

    public Map getCardTransaction7DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43964");
        inputParam.put("transactionTime", "20:15:54");
        inputParam.put("transactionDescription", "Ramsay's");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "RAMSAY RESTO");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "Ramsay's Resto, Beverly Ave");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-125");
        inputParam.put("transactionReferenceNumber", "2532626266");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS367575886");
        return inputParam;
    }

    public Map getCardTransaction8DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43964");
        inputParam.put("transactionTime", "20:15:54");
        inputParam.put("transactionDescription", "SRV CHGS");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "RAMSAY RESTO");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "F");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "Ramsay's Resto, Beverly Ave");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-3.125");
        inputParam.put("transactionReferenceNumber", "2532626268");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "Y");
        inputParam.put("taxPercentage", "2.5");
        inputParam.put("transactionTaxAmount", "1.25");
        inputParam.put("transactionTerminalID", "POS367575886");
        return inputParam;
    }

    public Map getCardTransaction9DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43965");
        inputParam.put("transactionTime", "06:00:10");
        inputParam.put("transactionDescription", "Netflix");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "NETFLIX");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Entertainment");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "Netflix");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-10");
        inputParam.put("transactionReferenceNumber", "NF2215262677");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "Y");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "OL21511666");
        return inputParam;
    }

    public Map getCardTransaction10DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43966");
        inputParam.put("transactionTime", "10:03:24");
        inputParam.put("transactionDescription", "Sunny Bee, Clark Ave");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "SUNNY BEE");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Grocery");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "Sunny Bee, Clark Ave");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-18");
        inputParam.put("transactionReferenceNumber", "92315215");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS1422");
        return inputParam;
    }

    public Map getCardTransaction11DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43966");
        inputParam.put("transactionTime", "10:03:25");
        inputParam.put("transactionDescription", "SRVC CHGS");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "SUNNY BEE");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Grocery");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "F");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "Sunny Bee, Clark Ave");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-0.09");
        inputParam.put("transactionReferenceNumber", "92315324");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "Y");
        inputParam.put("taxPercentage", "0.5");
        inputParam.put("transactionTaxAmount", "0.1");
        inputParam.put("transactionTerminalID", "POS1422");
        return inputParam;
    }

    public Map getCardTransaction12DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43967");
        inputParam.put("transactionTime", "07:00:10");
        inputParam.put("transactionDescription", "Uber");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "UBER");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Travel");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "U");
        inputParam.put("transactionDetailDescription", "Uber Transportation");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-10");
        inputParam.put("transactionReferenceNumber", "2141564637");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "235232637378");
        return inputParam;
    }

    public Map getCardTransaction13DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43967");
        inputParam.put("transactionTime", "07:30:02");
        inputParam.put("transactionDescription", "Burger King, Airport");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "BURGER KING");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "U");
        inputParam.put("transactionDetailDescription", "Burger King, Airport");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-3.25");
        inputParam.put("transactionReferenceNumber", "12415178");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS99255636");
        return inputParam;
    }

    public Map getCardTransaction14DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43968");
        inputParam.put("transactionTime", "20:18:32");
        inputParam.put("transactionDescription", "Uber");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "UBER");
        inputParam.put("transactionMerchantCity", "Manchester");
        inputParam.put("merchantCategory", "Travel");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "U");
        inputParam.put("transactionDetailDescription", "Uber UK");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-15");
        inputParam.put("transactionReferenceNumber", "79264485");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "0.91");
        inputParam.put("exchangeCurrency", "EUR");
        inputParam.put("exchangeAmount", "13.65");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "324678959");
        return inputParam;
    }

    public Map getCardTransaction15DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43969");
        inputParam.put("transactionTime", "07:00:10");
        inputParam.put("transactionDescription", "Uber");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "UBER");
        inputParam.put("transactionMerchantCity", "Manchester");
        inputParam.put("merchantCategory", "Travel");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "U");
        inputParam.put("transactionDetailDescription", "Uber UK");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-5");
        inputParam.put("transactionReferenceNumber", "3534643747");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "0.91");
        inputParam.put("exchangeCurrency", "EUR");
        inputParam.put("exchangeAmount", "4.55");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "6357458488");
        return inputParam;
    }

    public Map getCardTransaction16DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43969");
        inputParam.put("transactionTime", "12:30:10");
        inputParam.put("transactionDescription", "Ormer Mayfair Restaurant");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "ORMER GROUPS");
        inputParam.put("transactionMerchantCity", "Manchester");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "P");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "U");
        inputParam.put("transactionDetailDescription", "Ormer Mayfair, Piccadilly");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-380");
        inputParam.put("transactionReferenceNumber", "OM124151626");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "0.91");
        inputParam.put("exchangeCurrency", "EUR");
        inputParam.put("exchangeAmount", "345.72");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS9215151");
        return inputParam;
    }

    public Map getCardTransaction17DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43969");
        inputParam.put("transactionTime", "17:30:10");
        inputParam.put("transactionDescription", "Holiday Inn, London");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "Holiday INN GROUPS");
        inputParam.put("transactionMerchantCity", "Manchester");
        inputParam.put("merchantCategory", "Accomodation");
        inputParam.put("transactionStatus", "P");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "U");
        inputParam.put("transactionDetailDescription", "Holiday Inn, UK");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-100");
        inputParam.put("transactionReferenceNumber", "3444853894253785");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "0.91");
        inputParam.put("exchangeCurrency", "EUR");
        inputParam.put("exchangeAmount", "90.98");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS163789232");
        return inputParam;
    }

    public Map getCardTransaction18DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43969");
        inputParam.put("transactionTime", "18:00:10");
        inputParam.put("transactionDescription", "Uber");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "UBER");
        inputParam.put("transactionMerchantCity", "Manchester");
        inputParam.put("merchantCategory", "Travel");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "U");
        inputParam.put("transactionDetailDescription", "Uber UK");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-5");
        inputParam.put("transactionReferenceNumber", "3534643747");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "0.91");
        inputParam.put("exchangeCurrency", "EUR");
        inputParam.put("exchangeAmount", "4.55");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "6357458488");
        return inputParam;
    }

    public Map getCardTransaction19DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43970");
        inputParam.put("transactionTime", "07:00:10");
        inputParam.put("transactionDescription", "Uber");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "UBER");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Travel");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "U");
        inputParam.put("transactionDetailDescription", "Uber Transportation");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-15");
        inputParam.put("transactionReferenceNumber", "354654648");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "991241521515");
        return inputParam;
    }

    public Map getCardTransaction20DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "5314000000004360");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43971");
        inputParam.put("transactionTime", "10:03:24");
        inputParam.put("transactionDescription", "Cosco");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "COSCO MART");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Shopping");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "U");
        inputParam.put("transactionDetailDescription", "COSCO, US");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-40");
        inputParam.put("transactionReferenceNumber", "241512516361");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS2515167888");
        return inputParam;
    }

    public Map getCardTransaction21DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43959");
        inputParam.put("transactionTime", "11:30:23");
        inputParam.put("transactionDescription", "Cash Withdrawal ATM");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "ACE BANK ATM");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Finance");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Cash WDWL ATM");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-200");
        inputParam.put("transactionReferenceNumber", "223262786");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "ATM2527454848");
        return inputParam;
    }

    public Map getCardTransaction22DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43959");
        inputParam.put("transactionTime", "11:30:25");
        inputParam.put("transactionDescription", "SRVC CHGS ATM 1%");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "ACE BANK ATM");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Finance");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Cash WDWL ATM");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-2");
        inputParam.put("transactionReferenceNumber", "223262790");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "ATM2527454848");
        return inputParam;
    }

    public Map getCardTransaction23DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43959");
        inputParam.put("transactionTime", "13:03:48");
        inputParam.put("transactionDescription", "Taco Bell");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "TACO BELL");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Taco Bell, Clar Ave");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-3.5");
        inputParam.put("transactionReferenceNumber", "78346262788");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS166437373");
        return inputParam;
    }

    public Map getCardTransaction24DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43961");
        inputParam.put("transactionTime", "08:05:48");
        inputParam.put("transactionDescription", "Toys R Us");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "TOYS R US");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Shopping");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Toysrus, Total Mall");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-15.45");
        inputParam.put("transactionReferenceNumber", "3252262622");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS532362622");
        return inputParam;
    }

    public Map getCardTransaction25DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43963");
        inputParam.put("transactionTime", "10:00:35");
        inputParam.put("transactionDescription", "Old Mac Bakery");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "OLD MAC BAKERY");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Old Mac Bakery");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-2.6");
        inputParam.put("transactionReferenceNumber", "353262623623");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS125162472");
        return inputParam;
    }

    public Map getCardTransaction26DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43964");
        inputParam.put("transactionTime", "20:15:54");
        inputParam.put("transactionDescription", "Pizza Hut");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "PIZZA HUT");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Pizza Hut, Clive St");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-5.42");
        inputParam.put("transactionReferenceNumber", "5326262623626");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS2423262");
        return inputParam;
    }

    public Map getCardTransaction27DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43964");
        inputParam.put("transactionTime", "20:45:54");
        inputParam.put("transactionDescription", "Coldstone Creamery");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "COLDSTONE CREAMERY");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "ColdStone Creamery, Clive St");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-8.3");
        inputParam.put("transactionReferenceNumber", "315125125215");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS186859542");
        return inputParam;
    }

    public Map getCardTransaction28DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43965");
        inputParam.put("transactionTime", "06:00:10");
        inputParam.put("transactionDescription", "Max Life Insurance");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "MAX LIFE INSURANCE");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Investment");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Maxlife Insurance 242515156");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-550");
        inputParam.put("transactionReferenceNumber", "241241251331613");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "OL32363623623");
        return inputParam;
    }

    public Map getCardTransaction29DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43965");
        inputParam.put("transactionTime", "15:03:24");
        inputParam.put("transactionDescription", "Total Gas");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "TOTAL GAS");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Fuel");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Total Gas, Mark St");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-4");
        inputParam.put("transactionReferenceNumber", "15645844848");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS225252");
        return inputParam;
    }

    public Map getCardTransaction30DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43966");
        inputParam.put("transactionTime", "10:03:24");
        inputParam.put("transactionDescription", "Sunny Bee, Clark Ave");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "SUNNY BEE");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Grocery");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Sunny Bee, Clark Ave");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-3");
        inputParam.put("transactionReferenceNumber", "92315215");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS1422");
        return inputParam;
    }

    public Map getCardTransaction31DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43966");
        inputParam.put("transactionTime", "10:03:25");
        inputParam.put("transactionDescription", "Sunny Bee SRV CHG");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "SUNNY BEE");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Grocery");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "F");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Sunny Bee, Clark Ave");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-0.015");
        inputParam.put("transactionReferenceNumber", "92315324");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS1422");
        return inputParam;
    }

    public Map getCardTransaction32DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43967");
        inputParam.put("transactionTime", "07:00:10");
        inputParam.put("transactionDescription", "Cash Withdrawal ATM");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "ACE BANK ATM");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Finance");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Cash WDWL ATM");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-45");
        inputParam.put("transactionReferenceNumber", "223246446");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "ATM2527454848");
        return inputParam;
    }

    public Map getCardTransaction33DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43967");
        inputParam.put("transactionTime", "07:30:02");
        inputParam.put("transactionDescription", "SRVC CHGS ATM 1%");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "ACE BANK ATM");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Finance");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Cash WDWL ATM");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-0.8");
        inputParam.put("transactionReferenceNumber", "225252790");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "ATM2527454848");
        return inputParam;
    }

    public Map getCardTransaction34DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43968");
        inputParam.put("transactionTime", "20:18:32");
        inputParam.put("transactionDescription", "Uber");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "UBER");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Travel");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Uber Transportation");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-4.5");
        inputParam.put("transactionReferenceNumber", "114141564");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS5315");
        return inputParam;
    }

    public Map getCardTransaction35DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43969");
        inputParam.put("transactionTime", "07:00:10");
        inputParam.put("transactionDescription", "Apple Music");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "APPLE MUSIC");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Entertainment");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Apple Music Bill 01114");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-12");
        inputParam.put("transactionReferenceNumber", "323623632636326");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "OL1515125125");
        return inputParam;
    }

    public Map getCardTransaction36DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43969");
        inputParam.put("transactionTime", "12:30:10");
        inputParam.put("transactionDescription", "Starbucks");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "STARTBUCKS");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Startbucks, Tower plaza");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-1.25");
        inputParam.put("transactionReferenceNumber", "241252323");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "QR23524225");
        return inputParam;
    }

    public Map getCardTransaction37DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43969");
        inputParam.put("transactionTime", "17:30:10");
        inputParam.put("transactionDescription", "HardRock Café");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "HARDROCK CAFÉ");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Hardrock Café, Marine Drive");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-14.5");
        inputParam.put("transactionReferenceNumber", "125151252156");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS1423623678");
        return inputParam;
    }

    public Map getCardTransaction38DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43970");
        inputParam.put("transactionTime", "07:00:10");
        inputParam.put("transactionDescription", "Amazon US");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "AMAZON");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Online");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Amazon Online");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-25");
        inputParam.put("transactionReferenceNumber", "AM02425514");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "OL94242");
        return inputParam;
    }

    public Map getCardTransaction39DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43970");
        inputParam.put("transactionTime", "18:00:10");
        inputParam.put("transactionDescription", "Mark Hospital");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "MARK HOSPITAL");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Health");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Mark Hospital, Sacramento");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-800");
        inputParam.put("transactionReferenceNumber", "6474262623");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS014166348");
        return inputParam;
    }

    public Map getCardTransaction40DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43971");
        inputParam.put("transactionTime", "10:03:24");
        inputParam.put("transactionDescription", "Cash Withdrawal ATM");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "ACE BANK ATM");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Finance");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Cash WDWL ATM");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-200");
        inputParam.put("transactionReferenceNumber", "353734232");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "ATM2527454848");
        return inputParam;
    }

    public Map getCardTransaction41DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43971");
        inputParam.put("transactionTime", "10:03:24");
        inputParam.put("transactionDescription", "SRVC CHGS ATM 1%");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "ACE BANK ATM");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Finance");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Cash WDWL ATM");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-1.2");
        inputParam.put("transactionReferenceNumber", "353734233");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "ATM2527454848");
        return inputParam;
    }

    public Map getCardTransaction42DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43974");
        inputParam.put("transactionTime", "12:30:10");
        inputParam.put("transactionDescription", "Starbucks");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "STARTBUCKS");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "P");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Startbucks, Tower plaza");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-3.45");
        inputParam.put("transactionReferenceNumber", "252626271");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "QR23521551");
        return inputParam;
    }

    public Map getCardTransaction43DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43974");
        inputParam.put("transactionTime", "17:30:10");
        inputParam.put("transactionDescription", "Total Gas");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "TOTAL GAS");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Fuel");
        inputParam.put("transactionStatus", "P");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Total Gas, Mark St");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-6.2");
        inputParam.put("transactionReferenceNumber", "65745845832");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS222362");
        return inputParam;
    }

    public Map getCardTransaction44DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280205276450");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43974");
        inputParam.put("transactionTime", "07:00:10");
        inputParam.put("transactionDescription", "Amazon US");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "AMAZON");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Online");
        inputParam.put("transactionStatus", "P");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Amazon Online");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-125");
        inputParam.put("transactionReferenceNumber", "AM02422142");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "OL943161");
        return inputParam;
    }

    public Map getCardTransaction45DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43959");
        inputParam.put("transactionTime", "11:30:23");
        inputParam.put("transactionDescription", "Sunny Bee, Clark Ave");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "SUNNY BEE");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Grocery");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "Sunny Bee, Clark Ave");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-3");
        inputParam.put("transactionReferenceNumber", "92315215");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS1422");
        return inputParam;
    }

    public Map getCardTransaction46DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43959");
        inputParam.put("transactionTime", "11:30:25");
        inputParam.put("transactionDescription", "SRV CHGS");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "SUNNY BEE");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Grocery");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "F");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "Sunny Bee, Clark Ave");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-0.015");
        inputParam.put("transactionReferenceNumber", "92315324");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "Y");
        inputParam.put("taxPercentage", "0.5");
        inputParam.put("transactionTaxAmount", "0.01");
        inputParam.put("transactionTerminalID", "POS1422");
        return inputParam;
    }

    public Map getCardTransaction47DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43959");
        inputParam.put("transactionTime", "13:03:48");
        inputParam.put("transactionDescription", "Amazon US");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "AMAZON");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Online Shopping");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "Amazon Online");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-25");
        inputParam.put("transactionReferenceNumber", "AM0242555");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "OL94242");
        return inputParam;
    }

    public Map getCardTransaction48DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43961");
        inputParam.put("transactionTime", "08:05:48");
        inputParam.put("transactionDescription", "Total Gas");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "TOTAL GAS");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Fuel");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "Total Gas, Mark St");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-8.25");
        inputParam.put("transactionReferenceNumber", "53536396363");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS225252");
        return inputParam;
    }

    public Map getCardTransaction49DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43963");
        inputParam.put("transactionTime", "10:00:35");
        inputParam.put("transactionDescription", "Starbucks");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "STARTBUCKS");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "Startbucks, Tower plaza");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-1.25");
        inputParam.put("transactionReferenceNumber", "242647758");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "QR235673888");
        return inputParam;
    }

    public Map getCardTransaction50DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43964");
        inputParam.put("transactionTime", "15:03:24");
        inputParam.put("transactionDescription", "Payment Received");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "ACE BANK");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Finance");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "ACE BANK");
        inputParam.put("transactionIndicator", "C");
        inputParam.put("transactionAmount", "350");
        inputParam.put("transactionReferenceNumber", "352362623");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "OL253536262");
        return inputParam;
    }

    public Map getCardTransaction51DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43964");
        inputParam.put("transactionTime", "20:15:54");
        inputParam.put("transactionDescription", "Ramsay's");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "RAMSAY RESTO");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "Ramsay's Resto, Beverly Ave");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-125");
        inputParam.put("transactionReferenceNumber", "2532626266");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS367575886");
        return inputParam;
    }

    public Map getCardTransaction52DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43964");
        inputParam.put("transactionTime", "20:15:54");
        inputParam.put("transactionDescription", "SRV CHGS");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "RAMSAY RESTO");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "F");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "Ramsay's Resto, Beverly Ave");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-3.125");
        inputParam.put("transactionReferenceNumber", "2532626268");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "Y");
        inputParam.put("taxPercentage", "2.5");
        inputParam.put("transactionTaxAmount", "1.25");
        inputParam.put("transactionTerminalID", "POS367575886");
        return inputParam;
    }

    public Map getCardTransaction53DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43965");
        inputParam.put("transactionTime", "06:00:10");
        inputParam.put("transactionDescription", "Netflix");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "NETFLIX");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Entertainment");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "Netflix");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-10");
        inputParam.put("transactionReferenceNumber", "NF2215262677");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "Y");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "OL21511666");
        return inputParam;
    }

    public Map getCardTransaction54DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43966");
        inputParam.put("transactionTime", "10:03:24");
        inputParam.put("transactionDescription", "Sunny Bee, Clark Ave");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "SUNNY BEE");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Grocery");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "Sunny Bee, Clark Ave");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-18");
        inputParam.put("transactionReferenceNumber", "92315215");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS1422");
        return inputParam;
    }

    public Map getCardTransaction55DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43966");
        inputParam.put("transactionTime", "10:03:25");
        inputParam.put("transactionDescription", "SRVC CHGS");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "SUNNY BEE");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Grocery");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "F");
        inputParam.put("transactionCategory", "B");
        inputParam.put("transactionDetailDescription", "Sunny Bee, Clark Ave");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-0.09");
        inputParam.put("transactionReferenceNumber", "92315324");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "Y");
        inputParam.put("taxPercentage", "0.5");
        inputParam.put("transactionTaxAmount", "0.1");
        inputParam.put("transactionTerminalID", "POS1422");
        return inputParam;
    }

    public Map getCardTransaction56DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43967");
        inputParam.put("transactionTime", "07:00:10");
        inputParam.put("transactionDescription", "Uber");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "UBER");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Travel");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "U");
        inputParam.put("transactionDetailDescription", "Uber Transportation");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-10");
        inputParam.put("transactionReferenceNumber", "2141564637");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "235232637378");
        return inputParam;
    }

    public Map getCardTransaction57DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43967");
        inputParam.put("transactionTime", "07:30:02");
        inputParam.put("transactionDescription", "Burger King, Airport");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "BURGER KING");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "U");
        inputParam.put("transactionDetailDescription", "Burger King, Airport");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-3.25");
        inputParam.put("transactionReferenceNumber", "12415178");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS99255636");
        return inputParam;
    }

    public Map getCardTransaction58DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43968");
        inputParam.put("transactionTime", "20:18:32");
        inputParam.put("transactionDescription", "Uber");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "UBER");
        inputParam.put("transactionMerchantCity", "Manchester");
        inputParam.put("merchantCategory", "Travel");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "U");
        inputParam.put("transactionDetailDescription", "Uber UK");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-15");
        inputParam.put("transactionReferenceNumber", "79264485");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "0.91");
        inputParam.put("exchangeCurrency", "EUR");
        inputParam.put("exchangeAmount", "13.65");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "324678959");
        return inputParam;
    }

    public Map getCardTransaction59DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43969");
        inputParam.put("transactionTime", "07:00:10");
        inputParam.put("transactionDescription", "Uber");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "UBER");
        inputParam.put("transactionMerchantCity", "Manchester");
        inputParam.put("merchantCategory", "Travel");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "U");
        inputParam.put("transactionDetailDescription", "Uber UK");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-5");
        inputParam.put("transactionReferenceNumber", "3534643747");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "0.91");
        inputParam.put("exchangeCurrency", "EUR");
        inputParam.put("exchangeAmount", "4.55");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "6357458488");
        return inputParam;
    }

    public Map getCardTransaction60DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43969");
        inputParam.put("transactionTime", "12:30:10");
        inputParam.put("transactionDescription", "Ormer Mayfair Restaurant");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "ORMER GROUPS");
        inputParam.put("transactionMerchantCity", "Manchester");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "P");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "U");
        inputParam.put("transactionDetailDescription", "Ormer Mayfair, Piccadilly");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-580");
        inputParam.put("transactionReferenceNumber", "OM124151626");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "0.91");
        inputParam.put("exchangeCurrency", "EUR");
        inputParam.put("exchangeAmount", "345.72");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS9215151");
        return inputParam;
    }

    public Map getCardTransaction61DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43969");
        inputParam.put("transactionTime", "17:30:10");
        inputParam.put("transactionDescription", "Holiday Inn, London");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "Holiday INN GROUPS");
        inputParam.put("transactionMerchantCity", "Manchester");
        inputParam.put("merchantCategory", "Accomodation");
        inputParam.put("transactionStatus", "P");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "U");
        inputParam.put("transactionDetailDescription", "Holiday Inn, UK");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-390");
        inputParam.put("transactionReferenceNumber", "3444853894253785");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "0.91");
        inputParam.put("exchangeCurrency", "EUR");
        inputParam.put("exchangeAmount", "90.98");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS163789232");
        return inputParam;
    }

    public Map getCardTransaction62DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43969");
        inputParam.put("transactionTime", "18:00:10");
        inputParam.put("transactionDescription", "Uber");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "UBER");
        inputParam.put("transactionMerchantCity", "Manchester");
        inputParam.put("merchantCategory", "Travel");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "U");
        inputParam.put("transactionDetailDescription", "Uber UK");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-5");
        inputParam.put("transactionReferenceNumber", "3534643747");
        inputParam.put("transactionCurrencyCode", "USD");
        inputParam.put("transactionExchangeRate", "0.91");
        inputParam.put("exchangeCurrency", "EUR");
        inputParam.put("exchangeAmount", "4.55");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "6357458488");
        return inputParam;
    }

    public Map getCardTransaction63DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43970");
        inputParam.put("transactionTime", "07:00:10");
        inputParam.put("transactionDescription", "Uber");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "UBER");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Travel");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "U");
        inputParam.put("transactionDetailDescription", "Uber Transportation");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-15");
        inputParam.put("transactionReferenceNumber", "354654648");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "991241521515");
        return inputParam;
    }

    public Map getCardTransaction64DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43971");
        inputParam.put("transactionTime", "10:03:24");
        inputParam.put("transactionDescription", "Cosco");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "COSCO MART");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Shopping");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "U");
        inputParam.put("transactionDetailDescription", "COSCO, US");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-40");
        inputParam.put("transactionReferenceNumber", "241512516361");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS2515167888");
        return inputParam;
    }

    public Map getCardTransaction65DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43959");
        inputParam.put("transactionTime", "11:30:23");
        inputParam.put("transactionDescription", "Cash Withdrawal ATM");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "ACE BANK ATM");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Finance");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Cash WDWL ATM");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-200");
        inputParam.put("transactionReferenceNumber", "223262786");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "ATM2527454848");
        return inputParam;
    }

    public Map getCardTransaction66DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43959");
        inputParam.put("transactionTime", "11:30:25");
        inputParam.put("transactionDescription", "SRVC CHGS ATM 1%");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "ACE BANK ATM");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Finance");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Cash WDWL ATM");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-2");
        inputParam.put("transactionReferenceNumber", "223262790");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "ATM2527454848");
        return inputParam;
    }

    public Map getCardTransaction67DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43959");
        inputParam.put("transactionTime", "13:03:48");
        inputParam.put("transactionDescription", "Taco Bell");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "TACO BELL");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Taco Bell, Clar Ave");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-3.5");
        inputParam.put("transactionReferenceNumber", "78346262788");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS166437373");
        return inputParam;
    }

    public Map getCardTransaction68DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43961");
        inputParam.put("transactionTime", "08:05:48");
        inputParam.put("transactionDescription", "Toys R Us");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "TOYS R US");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Shopping");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Toysrus, Total Mall");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-15.45");
        inputParam.put("transactionReferenceNumber", "3252262622");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS532362622");
        return inputParam;
    }

    public Map getCardTransaction69DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43963");
        inputParam.put("transactionTime", "10:00:35");
        inputParam.put("transactionDescription", "Old Mac Bakery");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "OLD MAC BAKERY");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Old Mac Bakery");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-2.6");
        inputParam.put("transactionReferenceNumber", "353262623623");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS125162472");
        return inputParam;
    }

    public Map getCardTransaction70DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43964");
        inputParam.put("transactionTime", "20:15:54");
        inputParam.put("transactionDescription", "Pizza Hut");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "PIZZA HUT");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Pizza Hut, Clive St");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-5.42");
        inputParam.put("transactionReferenceNumber", "5326262623626");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS2423262");
        return inputParam;
    }

    public Map getCardTransaction71DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43964");
        inputParam.put("transactionTime", "20:45:54");
        inputParam.put("transactionDescription", "Coldstone Creamery");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "COLDSTONE CREAMERY");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "ColdStone Creamery, Clive St");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-8.3");
        inputParam.put("transactionReferenceNumber", "315125125215");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS186859542");
        return inputParam;
    }

    public Map getCardTransaction72DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43965");
        inputParam.put("transactionTime", "06:00:10");
        inputParam.put("transactionDescription", "Max Life Insurance");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "MAX LIFE INSURANCE");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Investment");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Maxlife Insurance 242515156");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-550");
        inputParam.put("transactionReferenceNumber", "241241251331613");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "OL32363623623");
        return inputParam;
    }

    public Map getCardTransaction73DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43965");
        inputParam.put("transactionTime", "15:03:24");
        inputParam.put("transactionDescription", "Total Gas");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "TOTAL GAS");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Fuel");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Total Gas, Mark St");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-4");
        inputParam.put("transactionReferenceNumber", "15645844848");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS225252");
        return inputParam;
    }

    public Map getCardTransaction74DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43966");
        inputParam.put("transactionTime", "10:03:24");
        inputParam.put("transactionDescription", "Sunny Bee, Clark Ave");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "SUNNY BEE");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Grocery");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Sunny Bee, Clark Ave");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-3");
        inputParam.put("transactionReferenceNumber", "92315215");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS1422");
        return inputParam;
    }

    public Map getCardTransaction75DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43966");
        inputParam.put("transactionTime", "10:03:25");
        inputParam.put("transactionDescription", "Sunny Bee SRV CHG");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "SUNNY BEE");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Grocery");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "F");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Sunny Bee, Clark Ave");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-0.015");
        inputParam.put("transactionReferenceNumber", "92315324");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS1422");
        return inputParam;
    }

    public Map getCardTransaction76DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43967");
        inputParam.put("transactionTime", "07:00:10");
        inputParam.put("transactionDescription", "Cash Withdrawal ATM");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "ACE BANK ATM");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Finance");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Cash WDWL ATM");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-45");
        inputParam.put("transactionReferenceNumber", "223246446");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "ATM2527454848");
        return inputParam;
    }

    public Map getCardTransaction77DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43967");
        inputParam.put("transactionTime", "07:30:02");
        inputParam.put("transactionDescription", "SRVC CHGS ATM 1%");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "ACE BANK ATM");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Finance");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Cash WDWL ATM");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-0.8");
        inputParam.put("transactionReferenceNumber", "225252790");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "ATM2527454848");
        return inputParam;
    }

    public Map getCardTransaction78DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43968");
        inputParam.put("transactionTime", "20:18:32");
        inputParam.put("transactionDescription", "Uber");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "UBER");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Travel");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Uber Transportation");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-4.5");
        inputParam.put("transactionReferenceNumber", "114141564");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS5315");
        return inputParam;
    }

    public Map getCardTransaction79DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43969");
        inputParam.put("transactionTime", "07:00:10");
        inputParam.put("transactionDescription", "Apple Music");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "APPLE MUSIC");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Entertainment");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Apple Music Bill 01114");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-12");
        inputParam.put("transactionReferenceNumber", "323623632636326");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "OL1515125125");
        return inputParam;
    }

    public Map getCardTransaction80DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43969");
        inputParam.put("transactionTime", "12:30:10");
        inputParam.put("transactionDescription", "Starbucks");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "STARTBUCKS");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Startbucks, Tower plaza");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-1.25");
        inputParam.put("transactionReferenceNumber", "241252323");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "QR23524225");
        return inputParam;
    }

    public Map getCardTransaction81DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43969");
        inputParam.put("transactionTime", "17:30:10");
        inputParam.put("transactionDescription", "HardRock Café");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "HARDROCK CAFÉ");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Hardrock Café, Marine Drive");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-14.5");
        inputParam.put("transactionReferenceNumber", "125151252156");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS1423623678");
        return inputParam;
    }

    public Map getCardTransaction82DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43970");
        inputParam.put("transactionTime", "07:00:10");
        inputParam.put("transactionDescription", "Amazon US");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "AMAZON");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Online");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Amazon Online");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-25");
        inputParam.put("transactionReferenceNumber", "AM02425514");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "OL94242");
        return inputParam;
    }

    public Map getCardTransaction83DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43970");
        inputParam.put("transactionTime", "18:00:10");
        inputParam.put("transactionDescription", "Mark Hospital");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "MARK HOSPITAL");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Health");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Mark Hospital, Sacramento");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-800");
        inputParam.put("transactionReferenceNumber", "6474262623");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS014166348");
        return inputParam;
    }

    public Map getCardTransaction84DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43971");
        inputParam.put("transactionTime", "10:03:24");
        inputParam.put("transactionDescription", "Cash Withdrawal ATM");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "ACE BANK ATM");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Finance");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Cash WDWL ATM");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-200");
        inputParam.put("transactionReferenceNumber", "353734232");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "ATM2527454848");
        return inputParam;
    }

    public Map getCardTransaction85DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43971");
        inputParam.put("transactionTime", "10:03:24");
        inputParam.put("transactionDescription", "SRVC CHGS ATM 1%");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "ACE BANK ATM");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Finance");
        inputParam.put("transactionStatus", "C");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Cash WDWL ATM");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-1.2");
        inputParam.put("transactionReferenceNumber", "353734233");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "ATM2527454848");
        return inputParam;
    }

    public Map getCardTransaction86DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43974");
        inputParam.put("transactionTime", "07:00:10");
        inputParam.put("transactionDescription", "Uber");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "UBER");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Travel");
        inputParam.put("transactionStatus", "P");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Uber Transportation");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-4");
        inputParam.put("transactionReferenceNumber", "5474583232");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "3252362623626");
        return inputParam;
    }

    public Map getCardTransaction87DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43974");
        inputParam.put("transactionTime", "10:03:24");
        inputParam.put("transactionDescription", "Sunny Bee, Clark Ave");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "SUNNY BEE");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Grocery");
        inputParam.put("transactionStatus", "P");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Sunny Bee, Clark Ave");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-11");
        inputParam.put("transactionReferenceNumber", "22362722");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS1453");
        return inputParam;
    }

    public Map getCardTransaction88DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4541982333084990");
        inputParam.put("cardType", "Credit");
        inputParam.put("transactionDate", "43974");
        inputParam.put("transactionTime", "10:03:25");
        inputParam.put("transactionDescription", "SRVC CHGS");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "SUNNY BEE");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Grocery");
        inputParam.put("transactionStatus", "P");
        inputParam.put("transactionType", "F");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Sunny Bee, Clark Ave");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-0.055");
        inputParam.put("transactionReferenceNumber", "22362724");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "Y");
        inputParam.put("taxPercentage", "0.5");
        inputParam.put("transactionTaxAmount", "0.1");
        inputParam.put("transactionTerminalID", "POS1453");
        return inputParam;
    }

    public Map getCardTransaction89DemoData() {
        Map inputParam = new HashMap();
        inputParam.put("cardNumber", "4213280203144800");
        inputParam.put("cardType", "Debit");
        inputParam.put("transactionDate", "43974");
        inputParam.put("transactionTime", "20:15:54");
        inputParam.put("transactionDescription", "Pizza Hut");
        // inputParam.put("transactionBalance", "");
        inputParam.put("transactionMerchantAddressName", "PIZZA HUT");
        inputParam.put("transactionMerchantCity", "Sacramento");
        inputParam.put("merchantCategory", "Food & Drinks");
        inputParam.put("transactionStatus", "P");
        inputParam.put("transactionType", "T");
        inputParam.put("transactionCategory", "");
        inputParam.put("transactionDetailDescription", "Pizza Hut, Clive St");
        inputParam.put("transactionIndicator", "D");
        inputParam.put("transactionAmount", "-8.35");
        inputParam.put("transactionReferenceNumber", "5769742523");
        inputParam.put("transactionCurrencyCode", "USD");
        // inputParam.put("transactionExchangeRate", "");
        inputParam.put("exchangeCurrency", "");
        // inputParam.put("exchangeAmount", "");
        inputParam.put("transactionTaxIndicator", "N");
        // inputParam.put("taxPercentage", "");
        // inputParam.put("transactionTaxAmount", "");
        inputParam.put("transactionTerminalID", "POS24221512");
        return inputParam;
    }
}