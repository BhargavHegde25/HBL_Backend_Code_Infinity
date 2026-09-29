package com.kony.dbputilities.demoservices;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.customersecurityservices.CreateCustomerAddress;
import com.kony.dbputilities.customersecurityservices.CreateCustomerBusinessType;
import com.kony.dbputilities.customersecurityservices.CreateCustomerCommunication;
import com.kony.dbputilities.customersecurityservices.PasswordHistoryManagement;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.organisation.CreateEmployee;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.contract.backenddelegate.api.ContractBackendDelegate;
import com.temenos.dbx.product.contract.resource.api.ContractResource;
import com.temenos.dbx.product.dto.CustomerLegalEntityDTO;
import com.temenos.dbx.product.payeeservices.businessdelegate.api.BillPayPayeeBusinessDelegate;
import com.temenos.dbx.product.payeeservices.businessdelegate.api.InterBankPayeeBusinessDelegate;
import com.temenos.dbx.product.payeeservices.businessdelegate.api.InternationalPayeeBusinessDelegate;
import com.temenos.dbx.product.payeeservices.businessdelegate.api.IntraBankPayeeBusinessDelegate;
import com.temenos.dbx.product.payeeservices.dto.BillPayPayeeDTO;
import com.temenos.dbx.product.payeeservices.dto.InterBankPayeeDTO;
import com.temenos.dbx.product.payeeservices.dto.InternationalPayeeDTO;
import com.temenos.dbx.product.payeeservices.dto.IntraBankPayeeDTO;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.CustomerActionsBusinessDelegate;
import com.temenos.dbx.product.utils.DTOUtils;
import com.temenos.dbx.product.utils.ThreadExecutor;

public class DemoDataSBService implements JavaService2, DemoDataService {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {

        Result result = new Result();
        Map<String, String> inputmap = HelperMethods.getInputParamMap(inputArray);

        DemoDataSB helper = new DemoDataSB();
        helper.init(dcRequest);
        helper.setUserName(inputmap.get("newUsername") + "SB");
        createDemoUserContract(methodId, inputArray, dcRequest, dcResponse, helper);
        String userId = createSBCustomer(dcRequest, inputmap, helper);
        createCustomerAccounts(dcRequest, userId, helper);
        createUserActionLimits(dcRequest, helper, userId);
        createLimitGroupLimits(userId, helper, dcRequest);
        createPhone(dcRequest, userId, helper);
        createNotifications(dcRequest, userId);
        createUserSecurityQuestions(dcRequest, userId);
        createPayees(dcRequest, userId, helper);
        createPayPersons(dcRequest, userId, helper);
        createExternalAccounts(dcRequest, userId, helper);
        createCards(dcRequest, userId, helper);
        createUserAlerts(dcRequest, userId, helper);
        createAccountAlerts(dcRequest, userId, helper);
        createUserBill(dcRequest, helper);

        /**
         * transactions starts
         */
        createRecentChkDeposits(dcRequest, helper);
        createRecentCardlessWithdrawal(dcRequest, helper);
        createTransactionsForLoanAccount(dcRequest, helper);
        createTransactionsForSavingsAccount(dcRequest, helper);
        createTransactionsForDepositAccount(dcRequest, helper);
        createTransactionsForCheckingAccount1(dcRequest, helper);
        createTransactionsForCheckingAccount2(dcRequest, helper);
        createTransactionsForCreditCardAccount1(dcRequest, helper);
        createTransactionsForCreditCardAccount2(dcRequest, helper);
        createTransactionsForCard(dcRequest, helper);
        /**
         * transactions ends
         */
        updateDemoCustomerDefaultAccounts(dcRequest, userId, helper);
        createMessages(dcRequest, helper);
        createAccountStatements(dcRequest, helper);
        DemoDataWealthService demoDataWealthService = new DemoDataWealthService();
		demoDataWealthService.createEntryInCustomerLegalEntity(dcRequest, userId);

        DemoDataACHServices achData = new DemoDataACHServices();
        achData.init(dcRequest, helper.getSBOrgId(), userId, helper);

        return result;
    }

    private void createUserActionLimits(DataControllerRequest dcRequest, DemoDataSB helper, String userId) {

        CustomerActionsBusinessDelegate businessDelegate =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(CustomerActionsBusinessDelegate.class);
        Set<String> accountsSet = new HashSet<>();
        accountsSet.add(helper.getAccountNum("Business Advantage Savings"));
        accountsSet.add(helper.getAccountNum("Progress Business Checking"));
        accountsSet.add(helper.getAccountNum("Pro Business Checking"));
        accountsSet.add(helper.getAccountNum("Business Platinum MasterCard"));
        try {
            businessDelegate.createCustomerActions(userId, helper.getContractId(), helper.getPrimaryCoreCustomerId(),
                    "GROUP_ADMINISTRATOR", accountsSet, dcRequest.getHeaderMap());
        } catch (ApplicationException e) {
           
            alert.prepareError(e.toString()).log();
        }
        accountsSet = new HashSet<>();
        accountsSet.add(helper.getAccountNum("Business Gold Credit Card"));
        accountsSet.add(helper.getAccountNum("Business Direct Term Deposit"));
        accountsSet.add(helper.getAccountNum("Business Advantage Term Loan"));
        try {
            businessDelegate.createCustomerActions(userId, helper.getContractId(), helper.getSecondaryCoreCustomerId(),
                    "GROUP_ADMINISTRATOR", accountsSet, dcRequest.getHeaderMap());
        } catch (ApplicationException e) {
        	alert.prepareError(e.toString()).log();
        }
    }

    private void createLimitGroupLimits(String userId, DemoDataSB helper, DataControllerRequest dcRequest) {
        CustomerActionsBusinessDelegate bd =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(CustomerActionsBusinessDelegate.class);
        try {
            bd.createCustomerLimitGroupLimits(userId, helper.getContractId(), helper.getPrimaryCoreCustomerId(),
                    dcRequest.getHeaderMap());
            bd.createCustomerLimitGroupLimits(userId, helper.getContractId(), helper.getSecondaryCoreCustomerId(),
                    dcRequest.getHeaderMap());
        } catch (Exception e) {
        	alert.prepareError(e.toString()).log();
        }
    }

    private void createDemoUserContract(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse, DemoDataSB helper) throws HttpCallException, ApplicationException {
        String addr1 = HelperMethods.getNewId();
        String addr2 = HelperMethods.getNewId();
        Map<String, String> inputParams = new HashMap<>();
        inputParams = getMembershipAddress1();
        inputParams.put("id", addr1);
        HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                URLConstants.ADDRESS_CREATE);
        inputParams.clear();
        inputParams = getMembershipAddress2();
        inputParams.put("id", addr2);
        HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                URLConstants.ADDRESS_CREATE);
        inputParams.clear();
        long memberId1 = HelperMethods.getNumericId(7);
        long memberId2 = HelperMethods.getNumericId(7);
        inputParams = getMembershipDetails1();
        inputParams.put("id", String.valueOf(memberId1));
        inputParams.put("addressId", addr1);
        inputParams.put("name", helper.getUserName());
        inputParams.put("taxId", String.valueOf(HelperMethods.getNumericId(9)));
        helper.setPrimaryCoreCustomerId(inputParams.get("id"));
        helper.setCoreCustomerName(inputParams.get("name"));
        HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                URLConstants.MEMBERSHIP_CREATE);
        inputParams.clear();
        inputParams = getMembershipDetails2();
        inputParams.put("id", String.valueOf(memberId2));
        inputParams.put("addressId", addr2);
        inputParams.put("name", inputParams.get("name") + "-" + helper.getUserName());
        inputParams.put("taxId", String.valueOf(HelperMethods.getNumericId(9)));
        helper.setSecondaryCoreCustomerId(inputParams.get("id"));
        HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                URLConstants.MEMBERSHIP_CREATE);

        inputParams.clear();
        inputParams.put("id", HelperMethods.getNewId());
        inputParams.put("membershipId", String.valueOf(memberId1));
        inputParams.put("relatedMebershipId", String.valueOf(memberId2));
        inputParams.put("relationshipName", "Child");
        HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                URLConstants.MEMBERSHIPRELATION_CREATE);
        inputParams.clear();
        inputParams.put("id", HelperMethods.getNewId());
        inputParams.put("membershipId", String.valueOf(memberId2));
        inputParams.put("relatedMebershipId", String.valueOf(memberId1));
        inputParams.put("relationshipName", "Parent");
        HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                URLConstants.MEMBERSHIPRELATION_CREATE);

        long memberId3 = HelperMethods.getNumericId(7);
        inputParams.clear();
        inputParams = getMembershipDetails3();
        inputParams.put("id", String.valueOf(memberId3));
        inputParams.put("addressId", addr2);
        inputParams.put("name", inputParams.get("name") + "-" + helper.getUserName());
        inputParams.put("taxId", String.valueOf(HelperMethods.getNumericId(9)));
        HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                URLConstants.MEMBERSHIP_CREATE);

        inputParams.clear();
        inputParams.put("id", HelperMethods.getNewId());
        inputParams.put("membershipId", String.valueOf(memberId1));
        inputParams.put("relatedMebershipId", String.valueOf(memberId3));
        inputParams.put("relationshipName", "Employee");
        HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                URLConstants.MEMBERSHIPRELATION_CREATE);

        inputParams.clear();
        inputParams.put("id", HelperMethods.getNewId());
        inputParams.put("membershipId", String.valueOf(memberId3));
        inputParams.put("relatedMebershipId", String.valueOf(memberId1));
        inputParams.put("relationshipName", "Parent");
        HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                URLConstants.MEMBERSHIPRELATION_CREATE);

        memberId3 = HelperMethods.getNumericId(7);
        inputParams.clear();
        inputParams = getMembershipDetails4();
        inputParams.put("id", String.valueOf(memberId3));
        inputParams.put("addressId", addr2);
        inputParams.put("name", inputParams.get("name") + "-" + helper.getUserName());
        inputParams.put("taxId", String.valueOf(HelperMethods.getNumericId(9)));
        HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                URLConstants.MEMBERSHIP_CREATE);

        inputParams.clear();
        inputParams.put("id", HelperMethods.getNewId());
        inputParams.put("membershipId", String.valueOf(memberId1));
        inputParams.put("relatedMebershipId", String.valueOf(memberId3));
        inputParams.put("relationshipName", "Employee");
        HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                URLConstants.MEMBERSHIPRELATION_CREATE);

        inputParams.clear();
        inputParams.put("id", HelperMethods.getNewId());
        inputParams.put("membershipId", String.valueOf(memberId3));
        inputParams.put("relatedMebershipId", String.valueOf(memberId1));
        inputParams.put("relationshipName", "Parent");
        HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                URLConstants.MEMBERSHIPRELATION_CREATE);

        memberId3 = HelperMethods.getNumericId(7);
        inputParams.clear();
        inputParams = getMembershipDetails5();
        inputParams.put("id", String.valueOf(memberId3));
        inputParams.put("addressId", addr2);
        inputParams.put("name", inputParams.get("name") + "-" + helper.getUserName());
        inputParams.put("taxId", String.valueOf(HelperMethods.getNumericId(9)));
        HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                URLConstants.MEMBERSHIP_CREATE);

        inputParams.clear();
        inputParams.put("id", HelperMethods.getNewId());
        inputParams.put("membershipId", String.valueOf(memberId2));
        inputParams.put("relatedMebershipId", String.valueOf(memberId3));
        inputParams.put("relationshipName", "Employee");
        HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                URLConstants.MEMBERSHIPRELATION_CREATE);

        inputParams.clear();
        inputParams.put("id", HelperMethods.getNewId());
        inputParams.put("membershipId", String.valueOf(memberId3));
        inputParams.put("relatedMebershipId", String.valueOf(memberId2));
        inputParams.put("relationshipName", "Parent");
        HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                URLConstants.MEMBERSHIPRELATION_CREATE);

        memberId3 = HelperMethods.getNumericId(7);
        inputParams.clear();
        inputParams = getMembershipDetails6();
        inputParams.put("id", String.valueOf(memberId3));
        inputParams.put("addressId", addr2);
        inputParams.put("name", inputParams.get("name") + "-" + helper.getUserName());
        inputParams.put("taxId", String.valueOf(HelperMethods.getNumericId(9)));
        HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                URLConstants.MEMBERSHIP_CREATE);

        inputParams.clear();
        inputParams.put("id", HelperMethods.getNewId());
        inputParams.put("membershipId", String.valueOf(memberId2));
        inputParams.put("relatedMebershipId", String.valueOf(memberId3));
        inputParams.put("relationshipName", "Employee");
        HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                URLConstants.MEMBERSHIPRELATION_CREATE);

        inputParams.clear();
        inputParams.put("id", HelperMethods.getNewId());
        inputParams.put("membershipId", String.valueOf(memberId3));
        inputParams.put("relatedMebershipId", String.valueOf(memberId2));
        inputParams.put("relationshipName", "Parent");
        HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                URLConstants.MEMBERSHIPRELATION_CREATE);

        createAccounts(dcRequest, "", helper, new HashMap<>());

        Map<String, String> contractPayloadMap = new HashMap<>();
        contractPayloadMap.put("contractName", "Temenos Global - " + helper.getUserName());
        contractPayloadMap.put("serviceDefinitionName", "SME Online Banking");
        contractPayloadMap.put("serviceDefinitionId", "707dfea8-d0fe-4154-89c3-e7d7ef2ee16a");
        contractPayloadMap.put("isDefaultActionsEnabled", "true");
        contractPayloadMap.put("faxId", "1231-1231");
        contractPayloadMap.put("communication",
                "[{\"phoneNumber\":\"9999999999\",\"phoneCountryCode\":\"+91\",\"email\":\"TemenosGlobal@temenos.com\"}]");
        contractPayloadMap.put("address",
                "[{\"country\":\"UnitedStates\",\"cityName\":\"Dallas\",\"state\":\"Texas\",\"zipCode\":\"75230\",\"addressLine1\":\"7777ForestLane,Dallas,TX,USA\",\"addressLine2\":\"\"}]");
        JsonArray contractCustomersJsonArray = new JsonArray();
        JsonObject contractCustomer = new JsonObject();
        contractCustomer.addProperty("isPrimary", "true");
        contractCustomer.addProperty("isBusiness", "true");
        contractCustomer.addProperty("coreCustomerId", helper.getPrimaryCoreCustomerId());
        contractCustomer.addProperty("coreCustomerName", helper.getUserName());
        JsonArray accounts = new JsonArray();
        accounts.add(getValidAccountJsonObject(helper.getAccount1DemoData(), helper));
        accounts.add(getValidAccountJsonObject(helper.getAccount2DemoData(), helper));
        accounts.add(getValidAccountJsonObject(helper.getAccount3DemoData(), helper));
        accounts.add(getValidAccountJsonObject(helper.getAccount4DemoData(), helper));
        contractCustomer.add("accounts", accounts);
        contractCustomer.add("features", CreateDemoData.getFeaturesList(dcRequest));
        contractCustomersJsonArray.add(contractCustomer);

        contractCustomer = new JsonObject();
        contractCustomer.addProperty("isPrimary", "false");
        contractCustomer.addProperty("isBusiness", "true");
        contractCustomer.addProperty("coreCustomerId", helper.getSecondaryCoreCustomerId());
        contractCustomer.addProperty("coreCustomerName",
                getMembershipDetails2().get("name") + "-" + helper.getUserName());
        accounts = new JsonArray();
        accounts.add(getValidAccountJsonObject(helper.getAccount5DemoData(), helper));
        accounts.add(getValidAccountJsonObject(helper.getAccount6DemoData(), helper));
        accounts.add(getValidAccountJsonObject(helper.getAccount7DemoData(), helper));
        contractCustomer.add("accounts", accounts);
        contractCustomer.add("features", CreateDemoData.getFeaturesList(dcRequest));
        contractCustomersJsonArray.add(contractCustomer);

        contractPayloadMap.put("contractCustomers", contractCustomersJsonArray.toString());
        ContractResource resource = DBPAPIAbstractFactoryImpl.getResource(ContractResource.class);
        inputArray[1] = contractPayloadMap;
        dcRequest.addRequestParam_("isDefaultActionsEnabled", "true");
        Result contractResponse = resource.createContract(methodId, inputArray, dcRequest, dcResponse);
        helper.setContractId(contractResponse.getParamValueByName("contractId"));
        ContractBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractBackendDelegate.class);
        backendDelegate.updateContractStatus(helper.getContractId(), DBPUtilitiesConstants.CONTRACT_STATUS_ACTIVE,
                dcRequest.getHeaderMap());
    }

    private JsonObject getValidAccountJsonObject(Map<String, String> inputParams, DemoDataSB helper) {
        JsonObject account = new JsonObject();
        account.addProperty("accountId", helper.getAccountNum(inputParams.get("AccountName")));
        account.addProperty("accountType", inputParams.get("Type_id"));
        account.addProperty("accountName", inputParams.get("AccountName"));
        account.addProperty("typeId", helper.getAccountTypes().get(inputParams.get("Type_id")));
        account.addProperty("ownerType", "Owner");
        return account;
    }

    private Map<String, String> getMembershipAddress1() {
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

    private Map<String, String> getMembershipAddress2() {
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

    private Map<String, String> getMembershipDetails1() {
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

    private Map<String, String> getMembershipDetails2() {
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

    private Map<String, String> getMembershipDetails6() {
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

    private Map<String, String> getMembershipDetails3() {
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

    private Map<String, String> getMembershipDetails4() {
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

    private Map<String, String> getMembershipDetails5() {
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

    public String getAccessToken(DataControllerRequest dcRequest) {
        return AdminUtil.getAdminToken(dcRequest);
    }

    private void createRecentChkDeposits(DataControllerRequest dcRequest, DemoDataSB helper) throws HttpCallException {
        Map<String, String> input = helper.getRecentChkDeposit1DemoData();
        Calendar cal = Calendar.getInstance();
        String today = getDateString(cal.getTime());
        String transactionDate = HelperMethods.getFormattedTimeStamp(cal.getTime(), FORMAT);
        input.put("Type_id", "4");
        input.put("toAccountNumber", helper.getAccountNum("Business Advantage Savings"));
        input.put("createdDate", today);
        input.put("transactionDate", transactionDate);
        input.put("scheduledDate", today);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_ACCOUNT_TRANSACTION_CREATE);

        input = helper.getRecentChkDeposit2DemoData();
        input.put("Type_id", "4");
        input.put("toAccountNumber", helper.getAccountNum("Business Advantage Savings"));
        input.put("createdDate", today);
        input.put("transactionDate", transactionDate);
        input.put("scheduledDate", today);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_ACCOUNT_TRANSACTION_CREATE);
    }

    private void createRecentCardlessWithdrawal(DataControllerRequest dcRequest, DemoDataSB helper)
            throws HttpCallException {
        Map<String, String> input = helper.getRecentCardLessWithdrawal1DemoData();
        Calendar cal = Calendar.getInstance();
        String today = getDateString(cal.getTime());
        String transactionDate = HelperMethods.getFormattedTimeStamp(cal.getTime(), FORMAT);
        input.put("Type_id", "6");
        input.put("fromAccountNumber", helper.getAccountNum("Business Advantage Savings"));
        input.put("createdDate", today);
        input.put("transactionDate", transactionDate);
        input.put("scheduledDate", today);
        addDateField(input, "cashlessOTPValidDate");
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_ACCOUNT_TRANSACTION_CREATE);

        input = helper.getRecentCardLessWithdrawal2DemoData();
        input.put("Type_id", "6");
        input.put("fromAccountNumber", helper.getAccountNum("Business Advantage Savings"));
        input.put("createdDate", today);
        input.put("transactionDate", transactionDate);
        input.put("scheduledDate", today);
        addDateField(input, "cashlessOTPValidDate");
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_ACCOUNT_TRANSACTION_CREATE);

        input = helper.getRecentCardLessWithdrawal3DemoData();
        input.put("Type_id", "6");
        input.put("fromAccountNumber", helper.getAccountNum("Business Advantage Savings"));
        input.put("createdDate", today);
        input.put("transactionDate", transactionDate);
        input.put("scheduledDate", today);
        addDateField(input, "cashlessOTPValidDate");
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_ACCOUNT_TRANSACTION_CREATE);
    }

    private void createUserBill(DataControllerRequest dcRequest, DemoDataSB helper) throws HttpCallException {
        Map<String, String> input = helper.getUserBill1DemoData();
        input.put("Payee_id", helper.getPayee("payee4"));
        input.put("Account_id", helper.getAccountNum("Pro Business Checking"));
        input.put("billerMaster_id", "4");
        Result bill = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_BILL_CREATE);
        helper.addBill("bill1", HelperMethods.getFieldValue(bill, "id"));

        input = helper.getUserBill2DemoData();
        input.put("Payee_id", helper.getPayee("payee1"));
        input.put("Account_id", helper.getAccountNum("Pro Business Checking"));
        input.put("billerMaster_id", "4");
        bill = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_BILL_CREATE);
        helper.addBill("bill2", HelperMethods.getFieldValue(bill, "id"));
    }

    private void createTransactionsForSavingsAccount(DataControllerRequest dcRequest, DemoDataSB helper)
            throws HttpCallException {
        Map<String, String> input = helper.getInternalTransfer_SBSAV1DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getCheckDeposit_SBSAV2DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getExternalTransfer_SBSAV3DemoData();
        input.put("toExternalAccountNumber", helper.getExternalAccountNum(input.get("beneficiaryName")));
        insertTransaction(input, dcRequest, helper);

        input = helper.getRemoteCheckDeposit_SBSAV4DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getInternalTransfer_SBSAV5DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getInterest_SBSAV6DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getRemoteCheckDeposit_SBSAV7DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getCredit_SBSAV8DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getInternalTransfer_SBSAV9DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getExternalTransfer_SBSAV10DemoData();
        input.put("toExternalAccountNumber", helper.getExternalAccountNum(input.get("beneficiaryName")));
        insertTransaction(input, dcRequest, helper);

        input = helper.getLoan_SBSAV11DemoData();
        insertTransaction(input, dcRequest, helper);
    }

    private void createTransactionsForDepositAccount(DataControllerRequest dcRequest, DemoDataSB helper)
            throws HttpCallException {
        Map<String, String> input = helper.getInterestCredit_SBDeposit1DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getInterestCredit_SBDeposit2DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getInterestCredit_SBDeposit3DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getDeposit_SBDeposit4DemoData();
        insertTransaction(input, dcRequest, helper);
    }

    private void createTransactionsForCheckingAccount1(DataControllerRequest dcRequest, DemoDataSB helper)
            throws HttpCallException {

        List<Map<String, String>> dataList = getCheckingAccountData1(helper);
        for (Map<String, String> data : dataList) {
            insertTransaction(data, dcRequest, helper);
        }
    }

    private void createTransactionsForCheckingAccount2(DataControllerRequest dcRequest, DemoDataSB helper)
            throws HttpCallException {

        List<Map<String, String>> dataList = getCheckingAccountData2(helper);
        for (Map<String, String> data : dataList) {
            insertTransaction(data, dcRequest, helper);
        }
    }

    private void createTransactionsForCreditCardAccount1(DataControllerRequest dcRequest, DemoDataSB helper)
            throws HttpCallException {

        List<Map<String, String>> dataList = getCreditAccountData1(helper);
        for (Map<String, String> data : dataList) {
            insertTransaction(data, dcRequest, helper);
        }
    }

    private void createTransactionsForCreditCardAccount2(DataControllerRequest dcRequest, DemoDataSB helper)
            throws HttpCallException {

        List<Map<String, String>> dataList = getCreditAccountData2(helper);
        for (Map<String, String> data : dataList) {
            insertTransaction(data, dcRequest, helper);
        }
    }

    private void insertTransaction(Map<String, String> input, DataControllerRequest dcRequest, DemoDataSB helper)
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

    private void addDateField(Map<String, String> input, String field) {
        String value = input.get(field);
        if (StringUtils.isNotBlank(value)) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, Integer.parseInt(value));
            input.put(field, getDateString(cal.getTime()));
        } else if (input.containsKey(field)) {
            input.remove(field);
        }
    }

    private void createTransactionsForLoanAccount(DataControllerRequest dcRequest, DemoDataSB helper)
            throws HttpCallException {
        Map<String, String> input = helper.getLoanPayment_SBLoan1DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getLoanPayment_SBLoan2DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getLoanPayment_SBLoan3DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getTax_SBLoan4DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getLoanPayment_SBLoan5DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getLoanPayment_SBLoan6DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getLoanPayment_SBLoan7DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getLoanPayment_SBLoan8DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getLoanPayment_SBLoan9DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getLoanPayment_SBLoan10DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getLoanPayment_SBLoan11DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getCredit_SBLoan12DemoData();
        insertTransaction(input, dcRequest, helper);
    }

    private String createSBCustomer(DataControllerRequest dcRequest, Map<String, String> inputmap, DemoDataSB helper)
            throws HttpCallException {
        Calendar usercal = Calendar.getInstance();
        String newUserName = inputmap.get("newUsername");
        String newPassword = inputmap.get("Password");
        String newEmail = inputmap.get("email");
        String phoneNumber = null;
        if (inputmap.get("phoneNumber") != null)
            phoneNumber = "+" + inputmap.get("phoneNumber").trim();
        String[] split = newEmail.split("@");
        if (split[1].equalsIgnoreCase("temenos.com")) {
            usercal.add(Calendar.DATE, 90);
        } else if (!(split[1].equalsIgnoreCase("kony.com") || split[1].equalsIgnoreCase("dieboldnixdorf.com")
                || split[1].equalsIgnoreCase("diebold.com"))) {
            usercal.add(Calendar.DATE, 15);
        } else {
            usercal.add(Calendar.DATE, 5000);
        }
        Map<String, String> input = helper.getCustomerDemoData();

        String id = HelperMethods.generateUniqueCustomerId(dcRequest);
        input.put("id", id);

        input.put("UserName", newUserName + "SB");
        input.put("Password", newPassword);
        helper.setUserName(newUserName + "SB");
        input.put("Bank_id", "1");

        input.put("CustomerType_id", "TYPE_ID_BUSINESS");
        input.put("isEnrolled", "true");
        input.put("CountryCode", inputmap.get("countryCode"));
        input.put("ValidDate", getDateString(usercal.getTime()));
        input.put("Lastlogintime", HelperMethods.getFormattedTimeStamp(new Date(), null));
        input.put("CurrentLoginTime", HelperMethods.getFormattedTimeStamp(new Date(), null));
        input.put("companyLegalUnit", "GB0010001");
        input.put("homeLegalEntity", "GB0010001");
        input.put("defaultLegalEntity", "GB0010001");

        if (inputmap.get("firstName") != null) {
            input.put("FirstName", inputmap.get("firstName"));
        }

        if (inputmap.get("lastName") != null) {
            input.put("LastName", inputmap.get("lastName"));
        }

        input.put("Organization_Id", helper.getSBOrgId());
        input.put("organizationType", "BUSINESS_TYPE_1");

        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.USER_CREATE);
        
        CustomerLegalEntityDTO customerLegalEntityDTO = new CustomerLegalEntityDTO();
		customerLegalEntityDTO.setId(HelperMethods.getNewId());
		customerLegalEntityDTO.setCustomer_id(id);
		customerLegalEntityDTO.setNew(true);
		customerLegalEntityDTO.setStatus_id(HelperMethods.getCustomerStatus().get("ACTIVE"));
		customerLegalEntityDTO.setLegalEntityId("GB0010001");
		customerLegalEntityDTO.persist(DTOUtils.getParameterMap(customerLegalEntityDTO, true), dcRequest.getHeaderMap());
		
        
        // organization employee create

        input.clear();
        input.put("id", id);
        input.put("Organization_Id", helper.getSBOrgId());
        input.put("Is_Owner", "true");
        input.put("isAuthSignatory", "true");

        CreateEmployee.invoke(input, dcRequest);

        input.clear();
        input.put("id", id);
        input.put("businessTypeId", "BUSINESS_TYPE_1");
        input.put("authSignatoryType", "TYPE_ID_OWNER");

        CreateCustomerBusinessType.invoke(input, dcRequest);

        PasswordHistoryManagement pm = new PasswordHistoryManagement(dcRequest, false);
        pm.makePasswordEntry(dcRequest, id, newPassword);

        input.clear();
        input = helper.getCustomerCommunicationDemoData();
        input.put("Email", newEmail);
        if (phoneNumber != null) {
            input.put("Phone", phoneNumber);
        }
        input.put("id", id);
        try {
            CreateCustomerCommunication.invoke(input, dcRequest);
        } catch (Exception e) {

            alert.prepareError(e.getMessage()).log();
        }

        input.clear();
        input = helper.getCustomerPreferenceDemoData();
        input.put("id", String.valueOf(HelperMethods.getNumericId()));
        input.put("Customer_id", id);

        new Result();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERPREFERENCE_CREATE);

        try {
            ThreadExecutor.getExecutor(dcRequest).execute(new Callable<Result>() {
                @Override
                public Result call() throws ApplicationException {
                    Map<String, String> postParamMapGroup = new HashMap<>();
                    postParamMapGroup.put("Customer_id", id);
                    postParamMapGroup.put("Group_id", "GROUP_ADMINISTRATOR");
                    postParamMapGroup.put("coreCustomerId", helper.getPrimaryCoreCustomerId());
                    postParamMapGroup.put("contractId", helper.getContractId());
                    postParamMapGroup.put("companyLegalUnit", "GB0010001");
                    try {
                        return HelperMethods.callApi(dcRequest, postParamMapGroup, HelperMethods.getHeaders(dcRequest),
                                URLConstants.CUSTOMER_GROUP_CREATE);
                    } catch (HttpCallException e) {
                        alert.prepareError(e.getMessage()).log();
                    }

                    return new Result();
                }

            });
        } catch (InterruptedException e) {
            alert.prepareError(e.getMessage()).log();
            Thread.currentThread().interrupt();
        }

        try {
            ThreadExecutor.getExecutor(dcRequest).execute(new Callable<Result>() {
                @Override
                public Result call() throws ApplicationException {
                    Map<String, String> postParamMapGroup = new HashMap<>();
                    postParamMapGroup.put("Customer_id", id);
                    postParamMapGroup.put("Group_id", "GROUP_ADMINISTRATOR");
                    postParamMapGroup.put("coreCustomerId", helper.getSecondaryCoreCustomerId());
                    postParamMapGroup.put("contractId", helper.getContractId());
                    postParamMapGroup.put("companyLegalUnit", "GB0010001");
                    try {
                        return HelperMethods.callApi(dcRequest, postParamMapGroup, HelperMethods.getHeaders(dcRequest),
                                URLConstants.CUSTOMER_GROUP_CREATE);
                    } catch (HttpCallException e) {
                        alert.prepareError(e.getMessage()).log();
                    }

                    return new Result();
                }

            });
        } catch (InterruptedException e) {
            alert.prepareError(e.getMessage()).log();
            Thread.currentThread().interrupt();
        }

        input.clear();
        input = helper.getAddress1DemoData();
        input.put("id", id);

        CreateCustomerAddress.invoke(input, dcRequest);

        input.clear();
        input.put("id", String.valueOf(HelperMethods.getNumericId()));
        input.put("Customer_id", id);
        input.put("BackendId", id);
        input.put("BackendType", "CAMP");

        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.BACKENDIDENTIFIER_CREATE);
        input.clear();
        input.put("id", String.valueOf(HelperMethods.getNumericId()));
        input.put("Customer_id", id);
        input.put("BackendId", helper.getPrimaryCoreCustomerId());
        input.put("BackendType", "CORE");

        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.BACKENDIDENTIFIER_CREATE);

        input.clear();
        input.put("id", HelperMethods.getNewId());
        input.put("contractId", helper.getContractId());
        input.put("customerId", id);
        input.put("coreCustomerId", helper.getPrimaryCoreCustomerId());
        input.put("isAdmin", "true");
        input.put("isOwner", "true");
        input.put("isPrimary", "true");
        input.put("isAuthSignatory", "true");
        input.put("companyLegalUnit","GB0010001");
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CONTRACT_CUSTOMERS_CREATE);

        input.clear();
        input.put("id", HelperMethods.getNewId());
        input.put("contractId", helper.getContractId());
        input.put("customerId", id);
        input.put("coreCustomerId", helper.getSecondaryCoreCustomerId());
        input.put("isAdmin", "true");
        input.put("isOwner", "true");
        input.put("isPrimary", "false");
        input.put("isAuthSignatory", "true");
        input.put("companyLegalUnit","GB0010001");
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CONTRACT_CUSTOMERS_CREATE);

        return id;
    }

    private void createUserSecurityQuestions(DataControllerRequest dcRequest, String userId) throws HttpCallException {
        Map<String, String> input = new HashMap<>();
        input.put("User_id", userId);
        input.put("answer", "warner");
        input.put("question", "1");
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_USER_SECURITY_CREATE);
        input.put("answer", "dog");
        input.put("question", "2");
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_USER_SECURITY_CREATE);
        input.put("answer", "egg");
        input.put("question", "3");
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_USER_SECURITY_CREATE);
        input.put("answer", "benz");
        input.put("question", "4");
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_USER_SECURITY_CREATE);
        input.put("answer", "tom");
        input.put("question", "5");
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_USER_SECURITY_CREATE);
    }

    private void createCustomerAccounts(DataControllerRequest dcRequest, String userId, DemoDataSB helper)
            throws HttpCallException {

        Map<String, String> input = new HashMap<>();
        input.put("id", HelperMethods.getNumericId() + "");
        input.put("Customer_id", userId);
        input.put("Account_id", helper.getAccountNum(helper.getAccount1DemoData().get("AccountName")));
        input.put("contractId", helper.getContractId());
        input.put("coreCustomerId", helper.getPrimaryCoreCustomerId());
        input.put("AccountName",
                helper.getAccount1DemoData().get("AccountName") + "-X"
                        + helper.getAccountNum(helper.getAccount1DemoData().get("AccountName")).substring(
                                helper.getAccountNum(helper.getAccount1DemoData().get("AccountName")).length() - 4));
        input.put("IsOrganizationAccount", "true");
        input.put("IsOrgAccountUnLinked", "false");
        input.put("Organization_id", helper.getSBOrgId());
        input.put("accountType", "Savings");

        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERACCOUNTS_CREATE);
        input.clear();
        input.put("id", HelperMethods.getNumericId() + "");
        input.put("Customer_id", userId);
        input.put("Account_id", helper.getAccountNum(helper.getAccount2DemoData().get("AccountName")));
        input.put("contractId", helper.getContractId());
        input.put("coreCustomerId", helper.getPrimaryCoreCustomerId());
        input.put("AccountName",
                helper.getAccount2DemoData().get("AccountName") + "-X"
                        + helper.getAccountNum(helper.getAccount2DemoData().get("AccountName")).substring(
                                helper.getAccountNum(helper.getAccount2DemoData().get("AccountName")).length() - 4));
        input.put("IsOrganizationAccount", "true");
        input.put("IsOrgAccountUnLinked", "false");
        input.put("Organization_id", helper.getSBOrgId());
        input.put("accountType", "Checking");

        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERACCOUNTS_CREATE);

        input.clear();
        input.put("id", HelperMethods.getNumericId() + "");
        input.put("Customer_id", userId);
        input.put("Account_id", helper.getAccountNum(helper.getAccount3DemoData().get("AccountName")));
        input.put("contractId", helper.getContractId());
        input.put("coreCustomerId", helper.getPrimaryCoreCustomerId());
        input.put("AccountName",
                helper.getAccount3DemoData().get("AccountName") + "-X"
                        + helper.getAccountNum(helper.getAccount3DemoData().get("AccountName")).substring(
                                helper.getAccountNum(helper.getAccount3DemoData().get("AccountName")).length() - 4));
        input.put("IsOrganizationAccount", "true");
        input.put("IsOrgAccountUnLinked", "false");
        input.put("Organization_id", helper.getSBOrgId());
        input.put("accountType", "Checking");

        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERACCOUNTS_CREATE);

        input.clear();
        input.put("id", HelperMethods.getNumericId() + "");
        input.put("Customer_id", userId);
        input.put("Account_id", helper.getAccountNum(helper.getAccount4DemoData().get("AccountName")));
        input.put("contractId", helper.getContractId());
        input.put("coreCustomerId", helper.getPrimaryCoreCustomerId());
        input.put("AccountName",
                helper.getAccount4DemoData().get("AccountName") + "-X"
                        + helper.getAccountNum(helper.getAccount4DemoData().get("AccountName")).substring(
                                helper.getAccountNum(helper.getAccount4DemoData().get("AccountName")).length() - 4));
        input.put("IsOrganizationAccount", "true");
        input.put("IsOrgAccountUnLinked", "false");
        input.put("Organization_id", helper.getSBOrgId());
        input.put("accountType", "CreditCard");

        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERACCOUNTS_CREATE);
        input.clear();
        input.put("id", HelperMethods.getNumericId() + "");
        input.put("Customer_id", userId);
        input.put("Account_id", helper.getAccountNum(helper.getAccount5DemoData().get("AccountName")));
        input.put("contractId", helper.getContractId());
        input.put("coreCustomerId", helper.getSecondaryCoreCustomerId());
        input.put("AccountName",
                helper.getAccount5DemoData().get("AccountName") + "-X"
                        + helper.getAccountNum(helper.getAccount5DemoData().get("AccountName")).substring(
                                helper.getAccountNum(helper.getAccount5DemoData().get("AccountName")).length() - 4));
        input.put("IsOrganizationAccount", "true");
        input.put("IsOrgAccountUnLinked", "false");
        input.put("Organization_id", helper.getSBOrgId());
        input.put("accountType", "CreditCard");

        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERACCOUNTS_CREATE);

        input.clear();
        input.put("id", HelperMethods.getNumericId() + "");
        input.put("Customer_id", userId);
        input.put("Account_id", helper.getAccountNum(helper.getAccount6DemoData().get("AccountName")));
        input.put("contractId", helper.getContractId());
        input.put("coreCustomerId", helper.getSecondaryCoreCustomerId());
        input.put("AccountName",
                helper.getAccount6DemoData().get("AccountName") + "-X"
                        + helper.getAccountNum(helper.getAccount6DemoData().get("AccountName")).substring(
                                helper.getAccountNum(helper.getAccount6DemoData().get("AccountName")).length() - 4));
        input.put("IsOrganizationAccount", "true");
        input.put("IsOrgAccountUnLinked", "false");
        input.put("Organization_id", helper.getSBOrgId());
        input.put("accountType", "Deposit");

        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERACCOUNTS_CREATE);

        input.clear();
        input.put("id", HelperMethods.getNumericId() + "");
        input.put("Customer_id", userId);
        input.put("Account_id", helper.getAccountNum(helper.getAccount7DemoData().get("AccountName")));
        input.put("contractId", helper.getContractId());
        input.put("coreCustomerId", helper.getSecondaryCoreCustomerId());
        input.put("AccountName",
                helper.getAccount7DemoData().get("AccountName") + "-X"
                        + helper.getAccountNum(helper.getAccount7DemoData().get("AccountName")).substring(
                                helper.getAccountNum(helper.getAccount7DemoData().get("AccountName")).length() - 4));
        input.put("IsOrganizationAccount", "true");
        input.put("IsOrgAccountUnLinked", "false");
        input.put("Organization_id", helper.getSBOrgId());
        input.put("accountType", "Loan");

        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERACCOUNTS_CREATE);
    }

    private void createAccounts(DataControllerRequest dcRequest, String userId, DemoDataSB helper,
            Map<String, String> inputmap)
            throws HttpCallException {

        Map<String, String> input = new HashMap<>();
        // String features =
        // "[\"ACCOUNT_AGGREGATION\",\"CUSTOM_ROLES\",\"ACCOUNT_SETTINGS\",\"ACH_COLLECTION\",\"ACH_FILES\",\"ACH_PAYMENT\",\"ALERT_MANAGEMENT\",\"APPLICANT_MANAGEMENT\",\"APPROVAL_MATRIX\",\"BILL_PAY\",\"CARD_MANAGEMENT\",\"CHECK_MANAGEMENT\",\"DISPUTE_TRANSACTIONS\",\"DOMESTIC_WIRE_TRANSFER\",\"FEEDBACK\",\"INTERNATIONAL_ACCOUNT_FUND_TRANSFER\",\"INTERNATIONAL_WIRE_TRANSFER\",\"INTER_BANK_ACCOUNT_FUND_TRANSFER\",\"INTRA_BANK_FUND_TRANSFER\",\"LOGIN\",\"MANAGE_E_STATEMENTS\",\"MESSAGES\",\"NAO\",\"NOTIFICATION\",\"PASSWORD_UPDATE\",\"PAYEE_MANAGEMENT\",\"PROFILE_SETTINGS\",\"RDC\",\"RECIPIENT_MANAGEMENT\",\"RESET_SECURITY_QUESTIONS\",\"TRANSACTION_MANAGEMENT\",\"TRANSFER_BETWEEN_OWN_ACCOUNT\",\"USERNAME_UPDATE\",\"USER_MANAGEMENT\",\"PERSONAL_FINANCE_MANAGEMENT\",\"BULK_UPLOAD_FILES\",\"BULK_PAYMENT_REQUEST\",\"BULK_PAYMENT_FILES\",\"BULK_PAYMENT_TEMPLATE\"]";
        // input.put("features", features);
        // input.put("id", helper.getSBOrgId());
        // // creates both organization features and organization action limits
        // Result result = CreateOrganisationFeaturesAndActions.invoke(input, dcRequest, "TYPE_ID_BUSINESS");
        // Set<String> hashSet = getActionsListCreated(result);
        // String[] actionIds = checkForMonetaryActionsAndconvertHashsetToStringArray(hashSet, dcRequest);

        String accountNum = null;
        String accountName = null;
        String newUserName = inputmap.get("newUsername");
        Calendar cal = Calendar.getInstance();
        input.clear();
        input = helper.getAccount1DemoData();
        accountNum = getDummyAccountId();
        accountName = input.get("AccountName");
        helper.addAccountNum(accountName, accountNum);
        cal.add(Calendar.DATE, -1);
        input.put("Account_id", accountNum);
        // input.put("AccountName", input.get("AccountName") + "-X" +
        // accountNum.substring(accountNum.length() - 4));
        input.put("Type_id", helper.getAccountId(input.get("Type_id")));
        input.put("Product_id", "8");
        input.put("Bank_id", "1");
        input.put("isBusinessAccount", "1");
        input.put("Organization_id", helper.getSBOrgId());
        input.put("LastPaymentDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        input.put("DividendLastPaidDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        input.put("EStatementmentEnable", "false");
        input.put("ownership", "Single");

        String accHolderName =
                "{\"username\": \"" + newUserName + "SB" + "\", \"fullname\": \"" + input.get("AccountHolder")
                        + "\"}";
        input.put("AccountHolder", accHolderName);

        HelperMethods.removeNullValues(input);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_ACCOUNTS_CREATE);

        input.clear();
        input.put("id", HelperMethods.getNewId());
        input.put("membershipId", helper.getPrimaryCoreCustomerId());
        input.put("accountId", accountNum);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.MEMBERSHIP_ACCOUNTS_CREATE);

        input = helper.getAccount2DemoData();
        accountNum = getDummyAccountId();
        accountName = input.get("AccountName");
        helper.addAccountNum(accountName, accountNum);
        cal.add(Calendar.DATE, -1);
        input.put("Account_id", accountNum);
        // input.put("AccountName", input.get("AccountName") + "-X" +
        // accountNum.substring(accountNum.length() - 4));
        input.put("Type_id", helper.getAccountId(input.get("Type_id")));
        input.put("Product_id", "11");
        input.put("Bank_id", "1");
        input.put("isBusinessAccount", "1");
        input.put("Organization_id", helper.getSBOrgId());
        input.put("LastPaymentDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        input.put("DividendLastPaidDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        input.put("EStatementmentEnable", "false");
        input.put("ownership", "Single");
        accHolderName =
                "{\"username\": \"" + newUserName + "SB" + "\", \"fullname\": \"" + input.get("AccountHolder")
                        + "\"}";
        input.put("AccountHolder", accHolderName);

        HelperMethods.removeNullValues(input);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_ACCOUNTS_CREATE);

        input.clear();
        input.put("id", HelperMethods.getNewId());
        input.put("membershipId", helper.getPrimaryCoreCustomerId());
        input.put("accountId", accountNum);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.MEMBERSHIP_ACCOUNTS_CREATE);

        input = helper.getAccount3DemoData();
        accountNum = getDummyAccountId();
        accountName = input.get("AccountName");
        helper.addAccountNum(accountName, accountNum);
        cal.add(Calendar.DATE, -1);
        input.put("Account_id", accountNum);
        // input.put("AccountName", input.get("AccountName") + "-X" +
        // accountNum.substring(accountNum.length() - 4));
        input.put("Type_id", helper.getAccountId(input.get("Type_id")));
        input.put("Product_id", "2");
        input.put("Bank_id", "1");
        input.put("isBusinessAccount", "1");
        input.put("Organization_id", helper.getSBOrgId());
        input.put("LastPaymentDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        input.put("DividendLastPaidDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        input.put("EStatementmentEnable", "false");
        input.put("ownership", "Single");
        accHolderName =
                "{\"username\": \"" + newUserName + "SB" + "\", \"fullname\": \"" + input.get("AccountHolder")
                        + "\"}";
        input.put("AccountHolder", accHolderName);

        HelperMethods.removeNullValues(input);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_ACCOUNTS_CREATE);

        input.clear();
        input.put("id", HelperMethods.getNewId());
        input.put("membershipId", helper.getPrimaryCoreCustomerId());
        input.put("accountId", accountNum);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.MEMBERSHIP_ACCOUNTS_CREATE);

        input = helper.getAccount4DemoData();
        accountNum = getDummyAccountId();
        accountName = input.get("AccountName");
        helper.addAccountNum(accountName, accountNum);
        cal.add(Calendar.DATE, -1);
        input.put("Account_id", accountNum);
        // input.put("AccountName", input.get("AccountName") + "-X" +
        // accountNum.substring(accountNum.length() - 4));
        input.put("Type_id", helper.getAccountId(input.get("Type_id")));
        input.put("Product_id", "5");
        input.put("Bank_id", "1");
        input.put("isBusinessAccount", "1");
        input.put("Organization_id", helper.getSBOrgId());
        input.put("LastPaymentDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        input.put("DividendLastPaidDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        input.put("EStatementmentEnable", "false");
        input.put("ownership", "Single");
        accHolderName =
                "{\"username\": \"" + newUserName + "SB" + "\", \"fullname\": \"" + input.get("AccountHolder")
                        + "\"}";
        input.put("AccountHolder", accHolderName);

        HelperMethods.removeNullValues(input);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_ACCOUNTS_CREATE);

        input.clear();
        input.put("id", HelperMethods.getNewId());
        input.put("membershipId", helper.getPrimaryCoreCustomerId());
        input.put("accountId", accountNum);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.MEMBERSHIP_ACCOUNTS_CREATE);

        input = helper.getAccount5DemoData();
        accountNum = getDummyAccountId();
        accountName = input.get("AccountName");
        helper.addAccountNum(accountName, accountNum);
        cal.add(Calendar.DATE, -1);
        input.put("Account_id", accountNum);
        // input.put("AccountName", input.get("AccountName") + "-X" +
        // accountNum.substring(accountNum.length() - 4));
        input.put("Type_id", helper.getAccountId(input.get("Type_id")));
        input.put("Product_id", "4");
        input.put("Bank_id", "1");
        input.put("isBusinessAccount", "1");
        input.put("Organization_id", helper.getSBOrgId());
        input.put("LastPaymentDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        input.put("DividendLastPaidDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        input.put("EStatementmentEnable", "false");
        input.put("ownership", "Single");
        accHolderName =
                "{\"username\": \"" + newUserName + "SB" + "\", \"fullname\": \"" + input.get("AccountHolder")
                        + "\"}";
        input.put("AccountHolder", accHolderName);

        HelperMethods.removeNullValues(input);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_ACCOUNTS_CREATE);

        input.clear();
        input.put("id", HelperMethods.getNewId());
        input.put("membershipId", helper.getSecondaryCoreCustomerId());
        input.put("accountId", accountNum);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.MEMBERSHIP_ACCOUNTS_CREATE);

        input = helper.getAccount6DemoData();
        accountNum = getDummyAccountId();
        accountName = input.get("AccountName");
        helper.addAccountNum(accountName, accountNum);
        cal.add(Calendar.DATE, -1);
        input.put("Account_id", accountNum);
        // input.put("AccountName", input.get("AccountName") + "-X" +
        // accountNum.substring(accountNum.length() - 4));
        input.put("Type_id", helper.getAccountId(input.get("Type_id")));
        input.put("Product_id", "4");
        input.put("Bank_id", "1");
        input.put("isBusinessAccount", "1");
        input.put("Organization_id", helper.getSBOrgId());
        input.put("LastPaymentDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        input.put("DividendLastPaidDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        input.put("EStatementmentEnable", "false");
        input.put("ownership", "Single");
        accHolderName =
                "{\"username\": \"" + newUserName + "SB" + "\", \"fullname\": \"" + input.get("AccountHolder")
                        + "\"}";
        input.put("AccountHolder", accHolderName);

        HelperMethods.removeNullValues(input);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_ACCOUNTS_CREATE);

        input.clear();
        input.put("id", HelperMethods.getNewId());
        input.put("membershipId", helper.getSecondaryCoreCustomerId());
        input.put("accountId", accountNum);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.MEMBERSHIP_ACCOUNTS_CREATE);

        input = helper.getAccount7DemoData();
        accountNum = getDummyAccountId();
        accountName = input.get("AccountName");
        helper.addAccountNum(accountName, accountNum);
        cal.add(Calendar.DATE, -1);
        input.put("Account_id", accountNum);
        // input.put("AccountName", input.get("AccountName") + "-X" +
        // accountNum.substring(accountNum.length() - 4));
        input.put("Type_id", helper.getAccountId(input.get("Type_id")));
        input.put("Product_id", "4");
        input.put("Bank_id", "1");
        input.put("isBusinessAccount", "1");
        input.put("Organization_id", helper.getSBOrgId());
        input.put("LastPaymentDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        input.put("DividendLastPaidDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        input.put("EStatementmentEnable", "false");
        input.put("ownership", "Single");
        accHolderName =
                "{\"username\": \"" + newUserName + "SB" + "\", \"fullname\": \"" + input.get("AccountHolder")
                        + "\"}";
        input.put("AccountHolder", accHolderName);

        HelperMethods.removeNullValues(input);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_ACCOUNTS_CREATE);

        input.clear();
        input.put("id", HelperMethods.getNewId());
        input.put("membershipId", helper.getSecondaryCoreCustomerId());
        input.put("accountId", accountNum);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.MEMBERSHIP_ACCOUNTS_CREATE);

        // createApprovalMatrixForSBUser(helper, actionIds, userId);

    }

    private void createPayees(DataControllerRequest dcRequest, String userId, DemoDataSB helper)
            throws HttpCallException {

        Result rs = null;
        Map<String, String> input = helper.getPayee1DemoData();
        input.put("User_Id", userId);
        input.put("billermaster_id", "6");
        input.put("transitDays", "3");
        input.put("organizationId", helper.getSBOrgId());
        input.put("Id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.PAYEE_CREATE);
        helper.addPayee("payee1", getFieldValue(rs, "Id"));
        createBillPayInfinityBeneficiary(userId, getFieldValue(rs, "Id"), helper);

        input = helper.getPayee2DemoData();
        input.put("User_Id", userId);
        input.put("billermaster_id", "1");
        input.put("transitDays", "3");
        input.put("organizationId", helper.getSBOrgId());
        input.put("Id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.PAYEE_CREATE);
        helper.addPayee("payee2", getFieldValue(rs, "Id"));
        createBillPayInfinityBeneficiary(userId, getFieldValue(rs, "Id"), helper);

        input = helper.getPayee3DemoData();
        input.put("User_Id", userId);
        input.put("billermaster_id", "1");
        input.put("transitDays", "3");
        input.put("organizationId", helper.getSBOrgId());
        input.put("Id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.PAYEE_CREATE);
        helper.addPayee("payee3", getFieldValue(rs, "Id"));
        createBillPayInfinityBeneficiary(userId, getFieldValue(rs, "Id"), helper);

        input = helper.getPayee4DemoData();
        input.put("User_Id", userId);
        input.put("billermaster_id", "4");
        input.put("transitDays", "3");
        input.put("organizationId", helper.getSBOrgId());
        input.put("Id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.PAYEE_CREATE);
        helper.addPayee("payee4", getFieldValue(rs, "Id"));
        createBillPayInfinityBeneficiary(userId, getFieldValue(rs, "Id"), helper);

        input = helper.getPayee5DemoData();
        input.put("User_Id", userId);
        input.put("billermaster_id", "8");
        input.put("transitDays", "3");
        input.put("organizationId", helper.getSBOrgId());
        input.put("Id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.PAYEE_CREATE);
        helper.addPayee("payee5", getFieldValue(rs, "Id"));
        createBillPayInfinityBeneficiary(userId, getFieldValue(rs, "Id"), helper);

        input = helper.getPayee6DemoData();
        input.put("User_Id", userId);
        input.put("billermaster_id", "7");
        input.put("transitDays", "3");
        input.put("organizationId", helper.getSBOrgId());
        input.put("Id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.PAYEE_CREATE);
        helper.addPayee("payee6", getFieldValue(rs, "Id"));
        createBillPayInfinityBeneficiary(userId, getFieldValue(rs, "Id"), helper);

    }

    private void createPayPersons(DataControllerRequest dcRequest, String userId, DemoDataSB helper)
            throws HttpCallException {

        Result rs = null;
        Map<String, String> input = helper.getPerson1DemoData();
        input.put("User_id", userId);
        input.put("id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_PAYPERSON_CREATE);
        helper.addPerson("person1", getFieldValue(rs, "id"));

        input = helper.getPerson2DemoData();
        input.put("User_id", userId);
        input.put("id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_PAYPERSON_CREATE);
        helper.addPerson("person2", getFieldValue(rs, "id"));

        input = helper.getPerson3DemoData();
        input.put("User_id", userId);
        input.put("id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_PAYPERSON_CREATE);
        helper.addPerson("person3", getFieldValue(rs, "id"));

        input = helper.getPerson4DemoData();
        input.put("User_id", userId);
        input.put("id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_PAYPERSON_CREATE);
        helper.addPerson("person4", getFieldValue(rs, "id"));
    }

    private void createExternalAccounts(DataControllerRequest dcRequest, String userId, DemoDataSB helper)
            throws HttpCallException {
        Result rs = null;
        Map<String, String> input = helper.getExtAccount1DemoData();

        input.put("User_id", userId);
        input.put("organizationId", helper.getSBOrgId());
        helper.addExternalAccountNum(input.get("beneficiaryName"), input.get("accountNumber"));
        input.put("Id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.EXT_ACCOUNTS_CREATE);
        createInterBankInfinityBeneficiary(userId, getFieldValue(rs, "Id"), helper);

        input = helper.getExtAccount2DemoData();
        input.put("User_id", userId);
        input.put("organizationId", helper.getSBOrgId());
        helper.addExternalAccountNum(input.get("beneficiaryName"), input.get("accountNumber"));
        input.put("Id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.EXT_ACCOUNTS_CREATE);
        createInterBankInfinityBeneficiary(userId, getFieldValue(rs, "Id"), helper);

        input = helper.getExtAccount3DemoData();
        input.put("User_id", userId);
        input.put("organizationId", helper.getSBOrgId());
        helper.addExternalAccountNum(input.get("beneficiaryName"), input.get("accountNumber"));
        input.put("Id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.EXT_ACCOUNTS_CREATE);
        createInterBankInfinityBeneficiary(userId, getFieldValue(rs, "Id"), helper);

        input = helper.getExtAccount4DemoData();
        input.put("User_id", userId);
        input.put("organizationId", helper.getSBOrgId());
        helper.addExternalAccountNum(input.get("beneficiaryName"), input.get("accountNumber"));
        input.put("Id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.EXT_ACCOUNTS_CREATE);
        createIntraBankInfinityBeneficiary(userId, getFieldValue(rs, "Id"), helper);

        input = helper.getExtAccount5DemoData();
        input.put("User_id", userId);
        input.put("organizationId", helper.getSBOrgId());
        helper.addExternalAccountNum(input.get("beneficiaryName"), input.get("accountNumber"));
        input.put("Id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.EXT_ACCOUNTS_CREATE);
        createInternationalInfinityBeneficiary(userId, getFieldValue(rs, "Id"), helper);
    }

    private void createCards(DataControllerRequest dcRequest, String userId, DemoDataSB helper)
            throws HttpCallException {

        Map<String, String> input = helper.getCard1DemoData();
        input.put("User_id", userId);
        input.put("isTypeBusiness", "1");
        input.put("account_id", helper.getAccountNum("Business Advantage Savings"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.CARDS_CREATE);

        input = helper.getCard2DemoData();
        input.put("User_id", userId);
        input.put("isTypeBusiness", "1");
        input.put("account_id", helper.getAccountNum("Pro Business Checking"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.CARDS_CREATE);

        input = helper.getCard3DemoData();
        input.put("User_id", userId);
        input.put("isTypeBusiness", "1");
        input.put("account_id", helper.getAccountNum("Business Platinum MasterCard"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.CARDS_CREATE);

    }

    private void createUserAlerts(DataControllerRequest dcRequest, String userId, DemoDataSB helper)
            throws HttpCallException {

        Map<String, String> input = helper.geUserAlertDemoData();
        input.put("User_id", userId);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_ALERTS_CREATE);
    }

    private void createAccountAlerts(DataControllerRequest dcRequest, String userId, DemoDataSB helper)
            throws HttpCallException {

        Map<String, String> input = helper.geAccountAlert1DemoData();
        input.put("User_id", userId);
        input.put("AccountNumber", helper.getAccountNum("Business Advantage Savings"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_USER_ACC_ALERT_CREATE);

        input = helper.geAccountAlert2DemoData();
        input.put("User_id", userId);
        input.put("AccountNumber", helper.getAccountNum("Pro Business Checking"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_USER_ACC_ALERT_CREATE);

        input = helper.geAccountAlert3DemoData();
        input.put("User_id", userId);
        input.put("AccountNumber", helper.getAccountNum("Business Platinum MasterCard"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_USER_ACC_ALERT_CREATE);

    }

    private void createPhone(DataControllerRequest dcRequest, String userId, DemoDataSB helper)
            throws HttpCallException {
        Map<String, String> input = helper.getPhone1DemoData();
        input.put("user_id", userId);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.PHONE_CREATE);

        String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + userId + DBPUtilitiesConstants.AND + "Type_id"
                + DBPUtilitiesConstants.EQUAL + "COMM_TYPE_PHONE";
        Result result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMER_COMMUNICATION_GET);

        if (HelperMethods.hasRecords(result)) {
            Map<String, String> inputParams = new HashMap<>();

            inputParams.put("id", HelperMethods.getFieldValue(result, "id"));
            inputParams.put("countryType", input.get("countryType"));
            inputParams.put("Extension", input.get("extension"));
            inputParams.put("isPrimary", input.get("isPrimary"));
            inputParams.put("Value", HelperMethods.getFieldValue(result, "Value"));
            inputParams.put("receivePromotions", input.get("receivePromotions"));
            inputParams.put("type", input.get("type"));
            HelperMethods.removeNullValues(inputParams);
            HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CUSTOMER_COMMUNICATION_UPDATE);
        }

    }

    private void createNotifications(DataControllerRequest dcRequest, String userId) throws HttpCallException {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Map<String, String> input = new HashMap<>();
        String today = getDateString(cal.getTime());
        input.put("user_id", userId);
        input.put("notification_id", "14");
        input.put("isRead", "0");
        input.put("receivedDate", today);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_USER_NOTIFICATION_CREATE);

        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -5);
        today = getDateString(cal.getTime());
        input.put("user_id", userId);
        input.put("notification_id", "8");
        input.put("isRead", "0");
        input.put("receivedDate", today);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_USER_NOTIFICATION_CREATE);

        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -3);
        today = getDateString(cal.getTime());
        input.put("user_id", userId);
        input.put("notification_id", "15");
        input.put("isRead", "0");
        input.put("receivedDate", today);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_USER_NOTIFICATION_CREATE);

        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -12);
        today = getDateString(cal.getTime());
        input.put("user_id", userId);
        input.put("notification_id", "19");
        input.put("isRead", "0");
        input.put("receivedDate", today);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_USER_NOTIFICATION_CREATE);

        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -17);
        today = getDateString(cal.getTime());
        input.put("user_id", userId);
        input.put("notification_id", "20");
        input.put("isRead", "0");
        input.put("receivedDate", today);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_USER_NOTIFICATION_CREATE);
    }

    private void updateDemoCustomerDefaultAccounts(DataControllerRequest dcRequest, String userId, DemoDataSB helper)
            throws HttpCallException {
        Map<String, String> input = new HashMap<>();

        String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + userId;

        Result result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERPREFERENCE_GET);

        input.put("id", HelperMethods.getFieldValue(result, "id"));

        input.put("DefaultAccountCardless", helper.getAccountNum("Business Advantage Savings"));
        input.put("DefaultAccountBillPay", helper.getAccountNum("Business Advantage Savings"));
        input.put("DefaultToAccountP2P", helper.getAccountNum("Business Advantage Savings"));
        input.put("DefaultFromAccountP2P", helper.getAccountNum("Business Advantage Savings"));
        input.put("DefaultAccountDeposit", helper.getAccountNum("Business Advantage Savings"));
        input.put("DefaultAccountPayments", helper.getAccountNum("Pro Business Checking"));
        input.put("DefaultAccountTransfers", helper.getAccountNum("Pro Business Checking"));
        input.put("DefaultAccountWire", helper.getAccountNum("Pro Business Checking"));

        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERPREFERENCE_UPDATE);
    }

    private void createMessages(DataControllerRequest dcRequest, DemoDataSB helper) throws HttpCallException {
        Map<String, String> input = new HashMap<>();
        input.put("Account_id", helper.getAccountNum("Business Advantage Savings"));
        input.put("Category_id", "1");
        input.put("Subcategory_id", "1");
        input.put("status", "Inbox");
        input.put("subject", "I want to open a new account");
        input.put("message",
                "Dear customer, We value your relationship. We understand that you want to nominate your wife as a beneficiary in your account. We request you to kindly download the form available on our website and send the details to our customer care department through email or fax. We will enroll her as a beneficiary and provide the confirmation.Thanks Customer Team");
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_MESSAGE_CREATE);

        input.put("Account_id", helper.getAccountNum("Pro Business Checking"));
        input.put("Category_id", "1");
        input.put("Subcategory_id", "3");
        input.put("status", "Inbox");
        input.put("subject", "Errorneous debit entry is edited");
        input.put("message",
                "Dear customer, We have found an erroneous debit entry in your account. We have identified that it is because of some manual error while entering the data by one of our executives. We have rectified the issue and we appologize for the inconvenience caused to you.Thanks Customer Team");
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_MESSAGE_CREATE);

        input.put("Account_id", helper.getAccountNum("Business Platinum MasterCard"));
        input.put("Category_id", "3");
        input.put("Subcategory_id", "16");
        input.put("status", "Inbox");
        input.put("subject", "Concern of OTPs");
        input.put("message",
                "Dear Customer, Thank you for providing your valuable feedback to reduce the number of digits in OTP from 8 to 4 or 6. We have shared your feedback to the right team in our IT department. We will definitely consider your feedback. Looking forward to serve you.Thanks Customer Care team");
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_MESSAGE_CREATE);
    }

    private void createAccountStatements(DataControllerRequest dcRequest, DemoDataSB helper) throws HttpCallException {
        Calendar date1 = new GregorianCalendar(2017, Calendar.NOVEMBER, 1);
        Calendar date2 = new GregorianCalendar(2017, Calendar.DECEMBER, 1);

        Map<String, String> input = new HashMap<>();
        input.put("Account_id", helper.getAccountNum("Business Advantage Savings"));
        input.put("description", "Statement for November");
        input.put("statementLink", "http://pmqa.konylabs.net/KonyWebBanking/account-statement-pdf.pdf");
        input.put("month", getDateString(date1.getTime()));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_ACCOUNT_STATEMENT_CREATE);

        input.put("Account_id", helper.getAccountNum("Pro Business Checking"));
        input.put("description", "Statement for December");
        input.put("statementLink", "http://pmqa.konylabs.net/KonyWebBanking/account-statement-pdf.pdf");
        input.put("month", getDateString(date2.getTime()));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_ACCOUNT_STATEMENT_CREATE);

        input.put("Account_id", helper.getAccountNum("Progress Business Checking"));
        input.put("description", "Statement for November");
        input.put("statementLink", "http://pmqa.konylabs.net/KonyWebBanking/account-statement-pdf.pdf");
        input.put("month", getDateString(date1.getTime()));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_ACCOUNT_STATEMENT_CREATE);

        input.put("Account_id", helper.getAccountNum("Business Direct Term Deposit"));
        input.put("description", "Statement for December");
        input.put("statementLink", "http://pmqa.konylabs.net/KonyWebBanking/account-statement-pdf.pdf");
        input.put("month", getDateString(date2.getTime()));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_ACCOUNT_STATEMENT_CREATE);

        input.put("Account_id", helper.getAccountNum("Business Advantage Term Loan"));
        input.put("description", "Statement for November");
        input.put("statementLink", "http://pmqa.konylabs.net/KonyWebBanking/account-statement-pdf.pdf");
        input.put("month", getDateString(date1.getTime()));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_ACCOUNT_STATEMENT_CREATE);
    }

    private List<Map<String, String>> getCreditAccountData1(DemoDataSB helper) {

        /*
         * helper.getPOS_CC51DemoData(), helper.getInternetTransaction_CC52DemoData(), helper.getPOS_CC53DemoData(),
         * helper.getPOS_CC54DemoData(), helper.getPOS_CC55DemoData(), helper.getInternetTransaction_CC56DemoData(),
         * helper.getInternetTransaction_CC57DemoData(), helper.getPOS_CC58DemoData(), helper.getCredit_CC59DemoData(),
         * helper.getBillPay_CC60DemoData(), helper.getPOS_CC61DemoData(), helper.getInternetTransaction_CC62DemoData(),
         * helper.getPOS_CC63DemoData(), helper.getPOS_CC64DemoData(), helper.getPOS_CC65DemoData(),
         * helper.getInternetTransaction_CC66DemoData(), helper.getInternetTransaction_CC67DemoData(),
         * helper.getPOS_CC68DemoData(), helper.getCredit_CC69DemoData(), helper.getBillPay_CC70DemoData(),
         * helper.getPOS_CC71DemoData(), helper.getInternetTransaction_CC72DemoData(), helper.getPOS_CC73DemoData(),
         * helper.getPOS_CC74DemoData(), helper.getPOS_CC75DemoData(), helper.getCredit_CC76DemoData(),
         * helper.getBillPay_CC77DemoData(), helper.getPOS_CC78DemoData(), helper.getInternetTransaction_CC79DemoData(),
         * helper.getPOS_CC80DemoData(), helper.getPOS_CC81DemoData(), helper.getPOS_CC82DemoData(),
         * helper.getPOS_CC83DemoData(), helper.getInternetTransaction_CC84DemoData(), helper.getPOS_CC85DemoData(),
         * helper.getPOS_CC86DemoData(), helper.getPOS_CC87DemoData(), helper.getInternetTransaction_CC88DemoData(),
         * helper.getInternetTransaction_CC89DemoData(), helper.getPOS_CC90DemoData(), helper.getCredit_CC91DemoData(),
         * helper.getBillPay_CC92DemoData(), helper.getPOS_CC93DemoData(), helper.getInternetTransaction_CC94DemoData(),
         * helper.getPOS_CC95DemoData(), helper.getPOS_CC96DemoData(), helper.getPOS_CC97DemoData(),
         * helper.getInternetTransaction_CC98DemoData(), helper.getInternetTransaction_CC99DemoData(),
         * helper.getPOS_CC100DemoData(), helper.getCredit_CC101DemoData(), helper.getBillPay_CC102DemoData(),
         * helper.getPOS_CC103DemoData(), helper.getInternetTransaction_CC104DemoData(), helper.getPOS_CC105DemoData(),
         * helper.getPOS_CC106DemoData(), helper.getPOS_CC107DemoData(), helper.getInternetTransaction_CC108DemoData(),
         * helper.getInternetTransaction_CC109DemoData(), helper.getPOS_CC110DemoData(),
         * helper.getCredit_CC111DemoData(), helper.getBillPay_CC112DemoData(), helper.getPOS_CC113DemoData(),
         * helper.getInternetTransaction_CC114DemoData(), helper.getPOS_CC115DemoData(), helper.getPOS_CC116DemoData(),
         * helper.getPOS_CC117DemoData(), helper.getInternetTransaction_CC118DemoData(),
         * helper.getInternetTransaction_CC119DemoData(), helper.getPOS_CC120DemoData(),
         * helper.getCredit_CC121DemoData(), helper.getBillPay_CC122DemoData(), helper.getPOS_CC123DemoData(),
         * helper.getInternetTransaction_CC124DemoData(), helper.getPOS_CC125DemoData(),
         * helper.getInternetTransaction_CC126DemoData(), helper.getCardPayment_CC127DemoData(),
         * helper.getPOS_CC128DemoData(), helper.getCredit_CC129DemoData(), helper.getBillPay_CC130DemoData(),
         * helper.getPOS_CC131DemoData(), helper.getInternetTransaction_CC132DemoData(), helper.getPOS_CC133DemoData(),
         * helper.getPOS_CC134DemoData(), helper.getPOS_CC135DemoData(), helper.getInternetTransaction_CC136DemoData(),
         * helper.getInternetTransaction_CC137DemoData(), helper.getPOS_CC138DemoData(),
         * helper.getCredit_CC139DemoData(), helper.getBillPay_CC140DemoData(), helper.getPOS_CC141DemoData(),
         * helper.getInternetTransaction_CC142DemoData(), helper.getPOS_CC143DemoData(), helper.getPOS_CC144DemoData(),
         * helper.getPOS_CC145DemoData(), helper.getInternetTransaction_CC146DemoData(),
         * helper.getInternetTransaction_CC147DemoData(), helper.getPOS_CC148DemoData(),
         * helper.getCredit_CC149DemoData(), helper.getBillPay_CC150DemoData(), helper.getPOS_CC151DemoData(),
         * helper.getInternetTransaction_CC152DemoData(), helper.getPOS_CC153DemoData(), helper.getPOS_CC154DemoData(),
         * helper.getPOS_CC155DemoData(), helper.getInternetTransaction_CC156DemoData(),
         * helper.getInternetTransaction_CC157DemoData(), helper.getPOS_CC158DemoData(),
         * helper.getCredit_CC159DemoData(), helper.getBillPay_CC160DemoData(), helper.getPOS_CC161DemoData(),
         * helper.getInternetTransaction_CC162DemoData(), helper.getPOS_CC163DemoData(), helper.getPOS_CC164DemoData(),
         * helper.getPOS_CC165DemoData(), helper.getInternetTransaction_CC166DemoData(),
         * helper.getInternetTransaction_CC167DemoData(), helper.getPOS_CC168DemoData(),
         * helper.getCredit_CC169DemoData(), helper.getBillPay_CC170DemoData(), helper.getPOS_CC171DemoData(),
         * helper.getInternetTransaction_CC172DemoData(), helper.getPOS_CC173DemoData(), helper.getPOS_CC174DemoData(),
         * helper.getPOS_CC175DemoData(), helper.getInternetTransaction_CC176DemoData(),
         * helper.getInternetTransaction_CC177DemoData(), helper.getPOS_CC178DemoData(),
         * helper.getCredit_CC179DemoData(), helper.getBillPay_CC180DemoData(), helper.getPOS_CC181DemoData(),
         * helper.getInternetTransaction_CC182DemoData(), helper.getPOS_CC183DemoData(), helper.getPOS_CC184DemoData(),
         * helper.getPOS_CC185DemoData(), helper.getInternetTransaction_CC186DemoData(), helper.getPOS_CC187DemoData(),
         * helper.getPOS_CC188DemoData(), helper.getPOS_CC189DemoData(), helper.getInternetTransaction_CC190DemoData(),
         * helper.getInternetTransaction_CC191DemoData(), helper.getPOS_CC192DemoData(),
         * helper.getCredit_CC193DemoData(), helper.getBillPay_CC194DemoData(), helper.getPOS_CC195DemoData(),
         * helper.getInternetTransaction_CC196DemoData(), helper.getPOS_CC197DemoData(), helper.getPOS_CC198DemoData(),
         * helper.getPOS_CC199DemoData(), helper.getInternetTransaction_CC200DemoData(),
         * helper.getInternetTransaction_CC201DemoData(), helper.getCardPayment_CC202DemoData(),
         */

        return Arrays.asList(helper.getPOS_CC1DemoData(), helper.getInternalTransfer_CC2DemoData(),
                helper.getPOS_CC3DemoData(), helper.getInternetTransaction_CC4DemoData(),
                helper.getBillPay_CC5DemoData(), helper.getPOS_CC6DemoData(), helper.getCardPayment_CC7DemoData(),
                helper.getPOS_CC8DemoData(), helper.getPOS_CC9DemoData(), helper.getPOS_CC10DemoData(),
                helper.getInternetTransaction_CC11DemoData(), helper.getInternetTransaction_CC12DemoData(),
                helper.getPOS_CC13DemoData(), helper.getCredit_CC14DemoData(), helper.getBillPay_CC15DemoData(),
                helper.getPOS_CC16DemoData(), helper.getInternetTransaction_CC17DemoData(),
                helper.getPOS_CC18DemoData(), helper.getPOS_CC19DemoData(), helper.getPOS_CC20DemoData(),
                helper.getInternetTransaction_CC21DemoData(), helper.getInternetTransaction_CC22DemoData(),
                helper.getPOS_CC23DemoData(), helper.getCredit_CC24DemoData(), helper.getBillPay_CC25DemoData(),
                helper.getPOS_CC26DemoData(), helper.getInternetTransaction_CC27DemoData(),
                helper.getPOS_CC28DemoData(), helper.getPOS_CC29DemoData(), helper.getPOS_CC30DemoData(),
                helper.getPOS_CC31DemoData(), helper.getInternetTransaction_CC32DemoData(),
                helper.getPOS_CC33DemoData(), helper.getPOS_CC34DemoData(), helper.getPOS_CC35DemoData(),
                helper.getInternetTransaction_CC36DemoData(), helper.getInternetTransaction_CC37DemoData(),
                helper.getPOS_CC38DemoData(), helper.getCredit_CC39DemoData(), helper.getBillPay_CC40DemoData(),
                helper.getPOS_CC41DemoData(), helper.getInternetTransaction_CC42DemoData(),
                helper.getPOS_CC43DemoData(), helper.getPOS_CC44DemoData(), helper.getPOS_CC45DemoData(),
                helper.getInternetTransaction_CC46DemoData(), helper.getInternetTransaction_CC47DemoData(),
                helper.getPOS_CC48DemoData(), helper.getCredit_CC49DemoData(), helper.getBillPay_CC50DemoData());

    }

    private List<Map<String, String>> getCreditAccountData2(DemoDataSB helper) {
        /*
         * helper.getPOS_SBCC51DemoData(), helper.getInternetTransaction_SBCC52DemoData(),
         * helper.getPOS_SBCC53DemoData(), helper.getPOS_SBCC54DemoData(), helper.getPOS_SBCC55DemoData(),
         * helper.getInternetTransaction_SBCC56DemoData(), helper.getInternetTransaction_SBCC57DemoData(),
         * helper.getPOS_SBCC58DemoData(), helper.getCredit_SBCC59DemoData(), helper.getBillPay_SBCC60DemoData(),
         * helper.getPOS_SBCC61DemoData(), helper.getInternetTransaction_SBCC62DemoData(),
         * helper.getPOS_SBCC63DemoData(), helper.getPOS_SBCC64DemoData(), helper.getPOS_SBCC65DemoData(),
         * helper.getInternetTransaction_SBCC66DemoData(), helper.getInternetTransaction_SBCC67DemoData(),
         * helper.getPOS_SBCC68DemoData(), helper.getCredit_SBCC69DemoData(), helper.getBillPay_SBCC70DemoData(),
         * helper.getPOS_SBCC71DemoData(), helper.getInternetTransaction_SBCC72DemoData(),
         * helper.getPOS_SBCC73DemoData(), helper.getPOS_SBCC74DemoData(), helper.getPOS_SBCC75DemoData(),
         * helper.getCredit_SBCC76DemoData(), helper.getBillPay_SBCC77DemoData(), helper.getPOS_SBCC78DemoData(),
         * helper.getInternetTransaction_SBCC79DemoData(), helper.getPOS_SBCC80DemoData(),
         * helper.getPOS_SBCC81DemoData(), helper.getPOS_SBCC82DemoData(), helper.getPOS_SBCC83DemoData(),
         * helper.getInternetTransaction_SBCC84DemoData(), helper.getPOS_SBCC85DemoData(),
         * helper.getPOS_SBCC86DemoData(), helper.getPOS_SBCC87DemoData(),
         * helper.getInternetTransaction_SBCC88DemoData(), helper.getInternetTransaction_SBCC89DemoData(),
         * helper.getPOS_SBCC90DemoData(), helper.getCredit_SBCC91DemoData(), helper.getBillPay_SBCC92DemoData(),
         * helper.getPOS_SBCC93DemoData(), helper.getInternetTransaction_SBCC94DemoData(),
         * helper.getPOS_SBCC95DemoData(), helper.getPOS_SBCC96DemoData(), helper.getPOS_SBCC97DemoData(),
         * helper.getInternetTransaction_SBCC98DemoData(), helper.getInternetTransaction_SBCC99DemoData(),
         * helper.getPOS_SBCC100DemoData(), helper.getCredit_SBCC101DemoData(), helper.getBillPay_SBCC102DemoData(),
         * helper.getPOS_SBCC103DemoData(), helper.getInternetTransaction_SBCC104DemoData(),
         * helper.getPOS_SBCC105DemoData(), helper.getPOS_SBCC106DemoData(), helper.getPOS_SBCC107DemoData(),
         * helper.getInternetTransaction_SBCC108DemoData(), helper.getInternetTransaction_SBCC109DemoData(),
         * helper.getPOS_SBCC110DemoData(), helper.getCredit_SBCC111DemoData(), helper.getBillPay_SBCC112DemoData(),
         * helper.getPOS_SBCC113DemoData(), helper.getInternetTransaction_SBCC114DemoData(),
         * helper.getPOS_SBCC115DemoData(), helper.getPOS_SBCC116DemoData(), helper.getPOS_SBCC117DemoData(),
         * helper.getInternetTransaction_SBCC118DemoData(), helper.getInternetTransaction_SBCC119DemoData(),
         * helper.getPOS_SBCC120DemoData(), helper.getCredit_SBCC121DemoData(), helper.getBillPay_SBCC122DemoData(),
         * helper.getPOS_SBCC123DemoData(), helper.getInternetTransaction_SBCC124DemoData(),
         * helper.getPOS_SBCC125DemoData(), helper.getInternetTransaction_SBCC126DemoData(),
         * helper.getCardPayment_SBCC127DemoData(), helper.getPOS_SBCC128DemoData(), helper.getCredit_SBCC129DemoData(),
         * helper.getBillPay_SBCC130DemoData(), helper.getPOS_SBCC131DemoData(),
         * helper.getInternetTransaction_SBCC132DemoData(), helper.getPOS_SBCC133DemoData(),
         * helper.getPOS_SBCC134DemoData(), helper.getPOS_SBCC135DemoData(),
         * helper.getInternetTransaction_SBCC136DemoData(), helper.getInternetTransaction_SBCC137DemoData(),
         * helper.getPOS_SBCC138DemoData(), helper.getCredit_SBCC139DemoData(), helper.getBillPay_SBCC140DemoData(),
         * helper.getPOS_SBCC141DemoData(), helper.getInternetTransaction_SBCC142DemoData(),
         * helper.getPOS_SBCC143DemoData(), helper.getPOS_SBCC144DemoData(), helper.getPOS_SBCC145DemoData(),
         * helper.getInternetTransaction_SBCC146DemoData(), helper.getInternetTransaction_SBCC147DemoData(),
         * helper.getPOS_SBCC148DemoData(), helper.getCredit_SBCC149DemoData(), helper.getBillPay_SBCC150DemoData(),
         * helper.getPOS_SBCC151DemoData(), helper.getInternetTransaction_SBCC152DemoData(),
         * helper.getPOS_SBCC153DemoData(), helper.getPOS_SBCC154DemoData(), helper.getPOS_SBCC155DemoData(),
         * helper.getInternetTransaction_SBCC156DemoData(), helper.getInternetTransaction_SBCC157DemoData(),
         * helper.getPOS_SBCC158DemoData(), helper.getCredit_SBCC159DemoData(), helper.getBillPay_SBCC160DemoData(),
         * helper.getPOS_SBCC161DemoData(), helper.getInternetTransaction_SBCC162DemoData(),
         * helper.getPOS_SBCC163DemoData(), helper.getPOS_SBCC164DemoData(), helper.getPOS_SBCC165DemoData(),
         * helper.getInternetTransaction_SBCC166DemoData(), helper.getInternetTransaction_SBCC167DemoData(),
         * helper.getPOS_SBCC168DemoData(), helper.getCredit_SBCC169DemoData(), helper.getBillPay_SBCC170DemoData(),
         * helper.getPOS_SBCC171DemoData(), helper.getInternetTransaction_SBCC172DemoData(),
         * helper.getPOS_SBCC173DemoData(), helper.getPOS_SBCC174DemoData(), helper.getPOS_SBCC175DemoData(),
         * helper.getInternetTransaction_SBCC176DemoData(), helper.getInternetTransaction_SBCC177DemoData(),
         * helper.getPOS_SBCC178DemoData(), helper.getCredit_SBCC179DemoData(), helper.getBillPay_SBCC180DemoData(),
         * helper.getPOS_SBCC181DemoData(), helper.getInternetTransaction_SBCC182DemoData(),
         * helper.getPOS_SBCC183DemoData(), helper.getPOS_SBCC184DemoData(), helper.getPOS_SBCC185DemoData(),
         * helper.getInternetTransaction_SBCC186DemoData(), helper.getPOS_SBCC187DemoData(),
         * helper.getPOS_SBCC188DemoData(), helper.getPOS_SBCC189DemoData(),
         * helper.getInternetTransaction_SBCC190DemoData(), helper.getInternetTransaction_SBCC191DemoData(),
         * helper.getPOS_SBCC192DemoData(), helper.getCredit_SBCC193DemoData(), helper.getBillPay_SBCC194DemoData(),
         * helper.getPOS_SBCC195DemoData(), helper.getInternetTransaction_SBCC196DemoData(),
         * helper.getPOS_SBCC197DemoData(), helper.getPOS_SBCC198DemoData(), helper.getPOS_SBCC199DemoData(),
         * helper.getInternetTransaction_SBCC200DemoData(), helper.getInternetTransaction_SBCC201DemoData(),
         * helper.getCardPayment_SBCC202DemoData(),
         */
        return Arrays.asList(helper.getPOS_CC1DemoData(), helper.getInternalTransfer_SBCC2DemoData(),
                helper.getPOS_SBCC3DemoData(), helper.getInternetTransaction_SBCC4DemoData(),
                helper.getBillPay_SBCC5DemoData(), helper.getPOS_SBCC6DemoData(), helper.getCardPayment_SBCC7DemoData(),
                helper.getPOS_SBCC8DemoData(), helper.getPOS_SBCC9DemoData(), helper.getPOS_SBCC10DemoData(),
                helper.getInternetTransaction_SBCC11DemoData(), helper.getInternetTransaction_SBCC12DemoData(),
                helper.getPOS_SBCC13DemoData(), helper.getCredit_SBCC14DemoData(), helper.getBillPay_SBCC15DemoData(),
                helper.getPOS_SBCC16DemoData(), helper.getInternetTransaction_SBCC17DemoData(),
                helper.getPOS_SBCC18DemoData(), helper.getPOS_SBCC19DemoData(), helper.getPOS_SBCC20DemoData(),
                helper.getInternetTransaction_SBCC21DemoData(), helper.getInternetTransaction_SBCC22DemoData(),
                helper.getPOS_SBCC23DemoData(), helper.getCredit_SBCC24DemoData(), helper.getBillPay_SBCC25DemoData(),
                helper.getPOS_SBCC26DemoData(), helper.getInternetTransaction_SBCC27DemoData(),
                helper.getPOS_SBCC28DemoData(), helper.getPOS_SBCC29DemoData(), helper.getPOS_SBCC30DemoData(),
                helper.getPOS_SBCC31DemoData(), helper.getInternetTransaction_SBCC32DemoData(),
                helper.getPOS_SBCC33DemoData(), helper.getPOS_SBCC34DemoData(), helper.getPOS_SBCC35DemoData(),
                helper.getInternetTransaction_SBCC36DemoData(), helper.getInternetTransaction_SBCC37DemoData(),
                helper.getPOS_SBCC38DemoData(), helper.getCredit_SBCC39DemoData(), helper.getBillPay_SBCC40DemoData(),
                helper.getPOS_SBCC41DemoData(), helper.getInternetTransaction_SBCC42DemoData(),
                helper.getPOS_SBCC43DemoData(), helper.getPOS_SBCC44DemoData(), helper.getPOS_SBCC45DemoData(),
                helper.getInternetTransaction_SBCC46DemoData(), helper.getInternetTransaction_SBCC47DemoData(),
                helper.getPOS_SBCC48DemoData(), helper.getCredit_SBCC49DemoData(), helper.getBillPay_SBCC50DemoData());
    }

    private List<Map<String, String>> getCheckingAccountData2(DemoDataSB helper) {
        /*
         * helper.getPOS_SBChecking31DemoData(), helper.getCredit_Checking32DemoData(),
         * helper.getBillPay_Checking33DemoData(), helper.getPOS_Checking34DemoData(),
         * helper.getInternetTransaction_Checking35DemoData(), helper.getPOS_Checking36DemoData(),
         * helper.getPOS_Checking37DemoData(), helper.getPOS_Checking38DemoData(),
         * helper.getInternetTransaction_Checking39DemoData(), helper.getInternetTransaction_Checking40DemoData(),
         * helper.getPOS_Checking41DemoData(), helper.getCredit_Checking42DemoData(),
         * helper.getBillPay_Checking43DemoData(), helper.getPOS_Checking44DemoData(),
         * helper.getInternetTransaction_Checking45DemoData(), helper.getPOS_Checking46DemoData(),
         * helper.getPOS_Checking47DemoData(), helper.getPOS_Checking48DemoData(),
         * helper.getInternetTransaction_Checking49DemoData(), helper.getInternetTransaction_Checking50DemoData(),
         * helper.getPOS_Checking51DemoData(), helper.getCredit_Checking52DemoData(),
         * helper.getBillPay_Checking53DemoData(), helper.getPOS_Checking54DemoData(),
         * helper.getInternetTransaction_Checking55DemoData(), helper.getPOS_Checking56DemoData(),
         * helper.getPOS_Checking57DemoData(), helper.getPOS_Checking58DemoData(),
         * helper.getInternetTransaction_Checking59DemoData(), helper.getInternetTransaction_Checking60DemoData(),
         * helper.getPOS_Checking61DemoData(), helper.getCredit_Checking62DemoData(),
         * helper.getBillPay_Checking63DemoData(), helper.getPOS_Checking64DemoData(),
         * helper.getInternetTransaction_Checking65DemoData(), helper.getCredit_Checking66DemoData(),
         * helper.getPOS_Checking67DemoData(), helper.getPOS_Checking68DemoData(), helper.getPOS_Checking69DemoData(),
         * helper.getCredit_Checking70DemoData(), helper.getBillPay_Checking71DemoData(),
         * helper.getPOS_Checking72DemoData(), helper.getInternetTransaction_Checking73DemoData(),
         * helper.getPOS_Checking74DemoData(), helper.getPOS_Checking75DemoData(), helper.getPOS_Checking76DemoData(),
         * helper.getPOS_Checking77DemoData(), helper.getInternetTransaction_Checking78DemoData(),
         * helper.getPOS_Checking79DemoData(), helper.getPOS_Checking80DemoData(), helper.getPOS_Checking81DemoData(),
         * helper.getInternetTransaction_Checking82DemoData(), helper.getInternetTransaction_Checking83DemoData(),
         * helper.getPOS_Checking84DemoData(), helper.getCredit_Checking85DemoData(),
         * helper.getBillPay_Checking86DemoData(), helper.getPOS_Checking87DemoData(),
         * helper.getInternetTransaction_Checking88DemoData(), helper.getPOS_Checking89DemoData(),
         * helper.getPOS_Checking90DemoData(), helper.getPOS_Checking91DemoData(),
         * helper.getInternetTransaction_Checking92DemoData(), helper.getInternetTransaction_Checking93DemoData(),
         * helper.getPOS_Checking94DemoData(), helper.getCredit_Checking95DemoData(),
         * helper.getBillPay_Checking96DemoData(), helper.getPOS_Checking97DemoData(),
         * helper.getInternetTransaction_Checking98DemoData(), helper.getPOS_Checking99DemoData(),
         * helper.getPOS_Checking100DemoData(), helper.getPOS_Checking101DemoData(),
         * helper.getInternetTransaction_Checking102DemoData(), helper.getInternetTransaction_Checking103DemoData(),
         * helper.getPOS_Checking104DemoData(), helper.getCredit_Checking105DemoData(),
         * helper.getBillPay_Checking106DemoData(), helper.getPOS_Checking107DemoData(),
         * helper.getInternetTransaction_Checking108DemoData(), helper.getPOS_Checking109DemoData(),
         * helper.getPOS_Checking110DemoData(), helper.getPOS_Checking111DemoData(),
         * helper.getInternetTransaction_Checking112DemoData(), helper.getInternetTransaction_Checking113DemoData(),
         * helper.getPOS_Checking114DemoData(), helper.getCredit_Checking115DemoData(),
         * helper.getBillPay_Checking116DemoData(), helper.getPOS_Checking117DemoData(),
         * helper.getInternetTransaction_Checking118DemoData(), helper.getPOS_Checking119DemoData(),
         * helper.getCredit_Checking120DemoData(), helper.getInternetTransaction_Checking121DemoData(),
         * helper.getInternetTransaction_Checking122DemoData(), helper.getPOS_Checking123DemoData(),
         * helper.getCredit_Checking124DemoData(), helper.getBillPay_Checking125DemoData(),
         * helper.getPOS_Checking126DemoData(), helper.getInternetTransaction_Checking127DemoData(),
         * helper.getPOS_Checking128DemoData(), helper.getPOS_Checking129DemoData(),
         * helper.getPOS_Checking130DemoData(), helper.getInternetTransaction_Checking131DemoData(),
         * helper.getInternetTransaction_Checking132DemoData(), helper.getPOS_Checking133DemoData(),
         * helper.getCredit_Checking134DemoData(), helper.getBillPay_Checking135DemoData(),
         * helper.getPOS_Checking136DemoData(), helper.getInternetTransaction_Checking137DemoData(),
         * helper.getPOS_Checking138DemoData(), helper.getPOS_Checking139DemoData(),
         * helper.getPOS_Checking140DemoData(), helper.getInternetTransaction_Checking141DemoData(),
         * helper.getInternetTransaction_Checking142DemoData(), helper.getPOS_Checking143DemoData(),
         * helper.getCredit_Checking144DemoData(), helper.getBillPay_Checking145DemoData(),
         * helper.getPOS_Checking146DemoData(), helper.getInternetTransaction_Checking147DemoData(),
         * helper.getPOS_Checking148DemoData(), helper.getPOS_Checking149DemoData(),
         * helper.getPOS_Checking150DemoData(), helper.getInternetTransaction_Checking151DemoData(),
         * helper.getInternetTransaction_Checking152DemoData(), helper.getPOS_Checking153DemoData(),
         * helper.getCredit_Checking154DemoData(), helper.getBillPay_Checking155DemoData(),
         * helper.getPOS_Checking156DemoData(), helper.getInternetTransaction_Checking157DemoData(),
         * helper.getPOS_Checking158DemoData(), helper.getPOS_Checking159DemoData(),
         * helper.getPOS_Checking160DemoData(), helper.getInternetTransaction_Checking161DemoData(),
         * helper.getCredit_Checking162DemoData(), helper.getInternetTransaction_Checking163DemoData(),
         * helper.getPOS_Checking164DemoData(), helper.getCredit_Checking165DemoData(),
         * helper.getBillPay_Checking166DemoData(), helper.getPOS_Checking167DemoData(),
         * helper.getInternetTransaction_Checking168DemoData(), helper.getPOS_Checking169DemoData(),
         * helper.getPOS_Checking170DemoData(), helper.getPOS_Checking171DemoData(),
         * helper.getInternetTransaction_Checking172DemoData(), helper.getInternetTransaction_Checking173DemoData(),
         * helper.getPOS_Checking174DemoData(), helper.getCredit_Checking175DemoData(),
         * helper.getBillPay_Checking176DemoData(), helper.getPOS_Checking177DemoData(),
         * helper.getInternetTransaction_Checking178DemoData(), helper.getPOS_Checking179DemoData(),
         * helper.getPOS_Checking180DemoData(), helper.getPOS_Checking181DemoData(),
         * helper.getInternetTransaction_Checking182DemoData(), helper.getPOS_Checking183DemoData(),
         * helper.getPOS_Checking184DemoData(), helper.getPOS_Checking185DemoData(),
         * helper.getInternetTransaction_Checking186DemoData(), helper.getInternetTransaction_Checking187DemoData(),
         * helper.getPOS_Checking188DemoData(), helper.getCredit_Checking189DemoData(),
         * helper.getBillPay_Checking190DemoData(), helper.getPOS_Checking191DemoData(),
         * helper.getInternetTransaction_Checking192DemoData(), helper.getPOS_Checking193DemoData(),
         * helper.getPOS_Checking194DemoData(), helper.getPOS_Checking195DemoData(),
         * helper.getInternetTransaction_Checking196DemoData(), helper.getInternetTransaction_Checking197DemoData(),
         * helper.getCardPayment_Checking198DemoData(), helper.getBillPay_Checking199DemoData(),
         * helper.getCredit_Checking200DemoData(), helper.getExternalTransfer_Checking201DemoData(),
         * helper.getCredit_Checking202DemoData(), helper.getPOS_Checking203DemoData(),
         * helper.getCheckDeposit_Checking204DemoData(), helper.getExternalTransfer_Checking205DemoData(),
         */
        return Arrays.asList(helper.getCredit_SBChecking1DemoData(), helper.getInternalTransfer_SBChecking2DemoData(),
                helper.getInternalTransfer_SBChecking3DemoData(), helper.getInternalTransfer_SBChecking4DemoData(),
                helper.getCheckDeposit_SBChecking5DemoData(), helper.getBillPay_SBChecking6DemoData(),
                helper.getInterest_SBChecking7DemoData(), helper.getWithdrawal_SBChecking8DemoData(),
                helper.getBillPay_SBChecking9DemoData(), helper.getInternetTransaction_SBChecking10DemoData(),
                helper.getCredit_SBChecking11DemoData(), helper.getCredit_SBChecking12DemoData(),
                helper.getPOS_SBChecking13DemoData(), helper.getInternetTransaction_SBChecking14DemoData(),
                helper.getInternetTransaction_SBChecking15DemoData(), helper.getCredit_SBChecking16DemoData(),
                helper.getCredit_SBChecking17DemoData(), helper.getBillPay_SBChecking18DemoData(),
                helper.getPOS_SBChecking19DemoData(), helper.getInternetTransaction_SBChecking20DemoData(),
                helper.getCredit_SBChecking21DemoData(), helper.getCredit_SBChecking22DemoData(),
                helper.getCredit_SBChecking23DemoData(), helper.getPOS_SBChecking24DemoData(),
                helper.getInternetTransaction_SBChecking25DemoData(), helper.getCredit_SBChecking26DemoData(),
                helper.getCredit_SBChecking27DemoData(), helper.getPOS_SBChecking28DemoData(),
                helper.getCardPayment_SBChecking29DemoData(), helper.getBillPay_SBChecking30DemoData());
    }

    private List<Map<String, String>> getCheckingAccountData1(DemoDataSB helper) {
        /*
         * helper.getPOS_Checking51DemoData(), helper.getCredit_Checking52DemoData(),
         * helper.getBillPay_Checking53DemoData(), helper.getPOS_Checking54DemoData(),
         * helper.getInternetTransaction_Checking55DemoData(), helper.getPOS_Checking56DemoData(),
         * helper.getPOS_Checking57DemoData(), helper.getPOS_Checking58DemoData(),
         * helper.getInternetTransaction_Checking59DemoData(), helper.getInternetTransaction_Checking60DemoData(),
         * helper.getPOS_Checking61DemoData(), helper.getCredit_Checking62DemoData(),
         * helper.getBillPay_Checking63DemoData(), helper.getPOS_Checking64DemoData(),
         * helper.getInternetTransaction_Checking65DemoData(), helper.getCredit_Checking66DemoData(),
         * helper.getPOS_Checking67DemoData(), helper.getPOS_Checking68DemoData(), helper.getPOS_Checking69DemoData(),
         * helper.getCredit_Checking70DemoData(), helper.getBillPay_Checking71DemoData(),
         * helper.getPOS_Checking72DemoData(), helper.getInternetTransaction_Checking73DemoData(),
         * helper.getPOS_Checking74DemoData(), helper.getPOS_Checking75DemoData(), helper.getPOS_Checking76DemoData(),
         * helper.getPOS_Checking77DemoData(), helper.getInternetTransaction_Checking78DemoData(),
         * helper.getPOS_Checking79DemoData(), helper.getPOS_Checking80DemoData(), helper.getPOS_Checking81DemoData(),
         * helper.getInternetTransaction_Checking82DemoData(), helper.getInternetTransaction_Checking83DemoData(),
         * helper.getPOS_Checking84DemoData(), helper.getCredit_Checking85DemoData(),
         * helper.getBillPay_Checking86DemoData(), helper.getPOS_Checking87DemoData(),
         * helper.getInternetTransaction_Checking88DemoData(), helper.getPOS_Checking89DemoData(),
         * helper.getPOS_Checking90DemoData(), helper.getPOS_Checking91DemoData(),
         * helper.getInternetTransaction_Checking92DemoData(), helper.getInternetTransaction_Checking93DemoData(),
         * helper.getPOS_Checking94DemoData(), helper.getCredit_Checking95DemoData(),
         * helper.getBillPay_Checking96DemoData(), helper.getPOS_Checking97DemoData(),
         * helper.getInternetTransaction_Checking98DemoData(), helper.getPOS_Checking99DemoData(),
         * helper.getPOS_Checking100DemoData(), helper.getPOS_Checking101DemoData(),
         * helper.getInternetTransaction_Checking102DemoData(), helper.getInternetTransaction_Checking103DemoData(),
         * helper.getPOS_Checking104DemoData(), helper.getCredit_Checking105DemoData(),
         * helper.getBillPay_Checking106DemoData(), helper.getPOS_Checking107DemoData(),
         * helper.getInternetTransaction_Checking108DemoData(), helper.getPOS_Checking109DemoData(),
         * helper.getPOS_Checking110DemoData(), helper.getPOS_Checking111DemoData(),
         * helper.getInternetTransaction_Checking112DemoData(), helper.getInternetTransaction_Checking113DemoData(),
         * helper.getPOS_Checking114DemoData(), helper.getCredit_Checking115DemoData(),
         * helper.getBillPay_Checking116DemoData(), helper.getPOS_Checking117DemoData(),
         * helper.getInternetTransaction_Checking118DemoData(), helper.getPOS_Checking119DemoData(),
         * helper.getCredit_Checking120DemoData(), helper.getInternetTransaction_Checking121DemoData(),
         * helper.getInternetTransaction_Checking122DemoData(), helper.getPOS_Checking123DemoData(),
         * helper.getCredit_Checking124DemoData(), helper.getBillPay_Checking125DemoData(),
         * helper.getPOS_Checking126DemoData(), helper.getInternetTransaction_Checking127DemoData(),
         * helper.getPOS_Checking128DemoData(), helper.getPOS_Checking129DemoData(),
         * helper.getPOS_Checking130DemoData(), helper.getInternetTransaction_Checking131DemoData(),
         * helper.getInternetTransaction_Checking132DemoData(), helper.getPOS_Checking133DemoData(),
         * helper.getCredit_Checking134DemoData(), helper.getBillPay_Checking135DemoData(),
         * helper.getPOS_Checking136DemoData(), helper.getInternetTransaction_Checking137DemoData(),
         * helper.getPOS_Checking138DemoData(), helper.getPOS_Checking139DemoData(),
         * helper.getPOS_Checking140DemoData(), helper.getInternetTransaction_Checking141DemoData(),
         * helper.getInternetTransaction_Checking142DemoData(), helper.getPOS_Checking143DemoData(),
         * helper.getCredit_Checking144DemoData(), helper.getBillPay_Checking145DemoData(),
         * helper.getPOS_Checking146DemoData(), helper.getInternetTransaction_Checking147DemoData(),
         * helper.getPOS_Checking148DemoData(), helper.getPOS_Checking149DemoData(),
         * helper.getPOS_Checking150DemoData(), helper.getInternetTransaction_Checking151DemoData(),
         * helper.getInternetTransaction_Checking152DemoData(), helper.getPOS_Checking153DemoData(),
         * helper.getCredit_Checking154DemoData(), helper.getBillPay_Checking155DemoData(),
         * helper.getPOS_Checking156DemoData(), helper.getInternetTransaction_Checking157DemoData(),
         * helper.getPOS_Checking158DemoData(), helper.getPOS_Checking159DemoData(),
         * helper.getPOS_Checking160DemoData(), helper.getInternetTransaction_Checking161DemoData(),
         * helper.getCredit_Checking162DemoData(), helper.getInternetTransaction_Checking163DemoData(),
         * helper.getPOS_Checking164DemoData(), helper.getCredit_Checking165DemoData(),
         * helper.getBillPay_Checking166DemoData(), helper.getPOS_Checking167DemoData(),
         * helper.getInternetTransaction_Checking168DemoData(), helper.getPOS_Checking169DemoData(),
         * helper.getPOS_Checking170DemoData(), helper.getPOS_Checking171DemoData(),
         * helper.getInternetTransaction_Checking172DemoData(), helper.getInternetTransaction_Checking173DemoData(),
         * helper.getPOS_Checking174DemoData(), helper.getCredit_Checking175DemoData(),
         * helper.getBillPay_Checking176DemoData(), helper.getPOS_Checking177DemoData(),
         * helper.getInternetTransaction_Checking178DemoData(), helper.getPOS_Checking179DemoData(),
         * helper.getPOS_Checking180DemoData(), helper.getPOS_Checking181DemoData(),
         * helper.getInternetTransaction_Checking182DemoData(), helper.getPOS_Checking183DemoData(),
         * helper.getPOS_Checking184DemoData(), helper.getPOS_Checking185DemoData(),
         * helper.getInternetTransaction_Checking186DemoData(), helper.getInternetTransaction_Checking187DemoData(),
         * helper.getPOS_Checking188DemoData(), helper.getCredit_Checking189DemoData(),
         * helper.getBillPay_Checking190DemoData(), helper.getPOS_Checking191DemoData(),
         * helper.getInternetTransaction_Checking192DemoData(), helper.getPOS_Checking193DemoData(),
         * helper.getPOS_Checking194DemoData(), helper.getPOS_Checking195DemoData(),
         * helper.getInternetTransaction_Checking196DemoData(), helper.getInternetTransaction_Checking197DemoData(),
         * helper.getCardPayment_Checking198DemoData(), helper.getBillPay_Checking199DemoData(),
         * helper.getCredit_Checking200DemoData(), helper.getExternalTransfer_Checking201DemoData(),
         * helper.getCredit_Checking202DemoData(), helper.getPOS_Checking203DemoData(),
         * helper.getCheckDeposit_Checking204DemoData(), helper.getExternalTransfer_Checking205DemoData(),
         */
        return Arrays.asList(helper.getCredit_Checking1DemoData(), helper.getInternalTransfer_Checking2DemoData(),
                helper.getInternalTransfer_Checking3DemoData(), helper.getInternalTransfer_Checking4DemoData(),
                helper.getCheckDeposit_Checking5DemoData(), helper.getBillPay_Checking6DemoData(),
                helper.getInterest_Checking7DemoData(), helper.getWithdrawal_Checking8DemoData(),
                helper.getBillPay_Checking9DemoData(), helper.getInternetTransaction_Checking10DemoData(),
                helper.getCredit_Checking11DemoData(), helper.getCredit_Checking12DemoData(),
                helper.getPOS_Checking13DemoData(), helper.getInternetTransaction_Checking14DemoData(),
                helper.getInternetTransaction_Checking15DemoData(), helper.getCredit_Checking16DemoData(),
                helper.getCredit_Checking17DemoData(), helper.getBillPay_Checking18DemoData(),
                helper.getPOS_Checking19DemoData(), helper.getInternetTransaction_Checking20DemoData(),
                helper.getCredit_Checking21DemoData(), helper.getCredit_Checking22DemoData(),
                helper.getCredit_Checking23DemoData(), helper.getPOS_Checking24DemoData(),
                helper.getInternetTransaction_Checking25DemoData(), helper.getCredit_Checking26DemoData(),
                helper.getCredit_Checking27DemoData(), helper.getPOS_Checking28DemoData());
    }

    private String getFieldValue(Result result, String fieldName) {
        String id = null;
        if (HelperMethods.hasRecords(result)) {
            Dataset ds = result.getAllDatasets().get(0);
            id = ds.getRecord(0).getParam(fieldName).getValue();
        }
        return id;
    }

    private String getDateString(Date d) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return formatter.format(d);
    }

    private String getDummyAccountId() {
        SimpleDateFormat idformatter = new SimpleDateFormat("yyMMddHHmmssSSS");
        return idformatter.format(new Date());
    }

    private void addDateFieldwithoutTime(Map<String, String> input, String field) {
        String value = input.get(field);
        if (StringUtils.isNotBlank(value)) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, Integer.parseInt(value));
            input.put(field, HelperMethods.getFormattedTimeStamp(cal.getTime(), FORMAT));
        } else if (input.containsKey(field)) {
            input.remove(field);
        }
    }

    @SuppressWarnings("unchecked")
    private void createTransactionsForCard(DataControllerRequest dcRequest,
            DemoDataSB helper) throws HttpCallException {
        Map<String, String> input = helper.getCardTransaction1DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction2DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction3DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction4DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction5DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction6DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction7DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction8DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction9DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction10DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction11DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction12DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction13DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction14DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction15DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction16DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction17DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction18DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction19DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction20DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction21DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction22DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction23DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction24DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction25DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction26DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction27DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction28DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction29DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction30DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction31DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction32DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction33DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction34DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction35DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction36DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction37DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction38DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction39DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction40DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction41DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction42DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction43DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction44DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction45DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction46DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction47DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction48DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction49DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction50DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction51DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction52DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction53DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction54DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction55DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction56DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction57DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction58DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction59DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction60DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction61DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction62DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction63DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction64DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction65DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction66DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction67DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction68DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction69DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction70DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction71DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction72DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction73DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction74DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction75DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction76DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction77DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction78DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction79DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction80DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction81DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction82DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction83DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction84DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction85DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction86DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction87DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction88DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);
        input = helper.getCardTransaction89DemoData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARDTRANSACTIONS_CREATE);

    }

    private void createBillPayInfinityBeneficiary(String userId, String payeeId, DemoDataSB helper)
            throws HttpCallException {
        BillPayPayeeBusinessDelegate billPayPayeeDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(BillPayPayeeBusinessDelegate.class);

        BillPayPayeeDTO billPayPayeeDTO = new BillPayPayeeDTO();
        billPayPayeeDTO.setPayeeId(payeeId);
        billPayPayeeDTO.setCreatedBy(userId);
        billPayPayeeDTO.setContractId(helper.getContractId());
        billPayPayeeDTO.setCif(helper.getPrimaryCoreCustomerId());
        billPayPayeeDelegate.createPayeeAtDBX(billPayPayeeDTO);
    }

    private void createInterBankInfinityBeneficiary(String userId, String payeeId, DemoDataSB helper)
            throws HttpCallException {
        InterBankPayeeBusinessDelegate interBankPayeeDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(InterBankPayeeBusinessDelegate.class);

        InterBankPayeeDTO interBankPayeeDTO = new InterBankPayeeDTO();
        interBankPayeeDTO.setPayeeId(payeeId);
        interBankPayeeDTO.setCreatedBy(userId);
        interBankPayeeDTO.setContractId(helper.getContractId());
        interBankPayeeDTO.setCif(helper.getPrimaryCoreCustomerId());
        interBankPayeeDelegate.createPayeeAtDBX(interBankPayeeDTO);
    }

    private void createIntraBankInfinityBeneficiary(String userId, String payeeId, DemoDataSB helper)
            throws HttpCallException {
        IntraBankPayeeBusinessDelegate intraBankPayeeDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(IntraBankPayeeBusinessDelegate.class);

        IntraBankPayeeDTO intraBankPayeeDTO = new IntraBankPayeeDTO();
        intraBankPayeeDTO.setPayeeId(payeeId);
        intraBankPayeeDTO.setCreatedBy(userId);
        intraBankPayeeDTO.setContractId(helper.getContractId());
        intraBankPayeeDTO.setCif(helper.getPrimaryCoreCustomerId());
        intraBankPayeeDelegate.createPayeeAtDBX(intraBankPayeeDTO);
    }

    private void createInternationalInfinityBeneficiary(String userId, String payeeId, DemoDataSB helper)
            throws HttpCallException {
        InternationalPayeeBusinessDelegate internationalPayeeDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(InternationalPayeeBusinessDelegate.class);

        InternationalPayeeDTO internationalPayeeDTO = new InternationalPayeeDTO();
        internationalPayeeDTO.setPayeeId(payeeId);
        internationalPayeeDTO.setCreatedBy(userId);
        internationalPayeeDTO.setContractId(helper.getContractId());
        internationalPayeeDTO.setCif(helper.getPrimaryCoreCustomerId());
        internationalPayeeDelegate.createPayeeAtDBX(internationalPayeeDTO);
    }

}
