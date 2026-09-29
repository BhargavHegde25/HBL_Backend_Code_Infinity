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
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.dbx.product.approvalmatrixservices.businessdelegate.api.ApprovalMatrixBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.OrganisationBusinessDelegate;
import com.temenos.dbx.product.commons.dto.LimitsDTO;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.dto.CustomerLegalEntityDTO;
//import org.omg.CORBA.portable.ApplicationException;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbp.handler.LimitsHandler;
import com.kony.dbputilities.customersecurityservices.CreateCustomerAddress;
import com.kony.dbputilities.customersecurityservices.CreateCustomerCommunication;
import com.kony.dbputilities.customersecurityservices.PasswordHistoryManagement;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.organisation.CreateEmployee;
import com.kony.dbputilities.organisation.CreateOrganisationFeaturesAndActions;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.utils.DTOUtils;
import com.temenos.dbx.product.utils.ThreadExecutor;

public class DemoDataMBService implements JavaService2, DemoDataService {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {

        DemoDataMB helper = new DemoDataMB();
        helper.init(dcRequest);

        Map<String, String> inputmap = HelperMethods.getInputParamMap(inputArray);

        createMBOrganisation(dcRequest, inputmap.get("newUsername"), helper);

        String userId = createMBCustomer(dcRequest, inputmap, helper);

        // create accounts for both organization and customer
        createAccounts(dcRequest, userId, inputmap.get("newUsername") + "MB", helper);
        createCustomerActionLimits(dcRequest, userId, helper);
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
        createTransactionsForLoanAccount(dcRequest, helper);
        createTransactionsForSavingsAccount(dcRequest, helper);
        createTransactionsForDepositAccount(dcRequest, helper);
        createTransactionsForCheckingAccount(dcRequest, helper);
        createTransactionsForCreditCardAccount(dcRequest, helper);
        createTransactionsForCard(dcRequest, helper);
        /**
         * transactions ends
         */
        updateDemoCustomerDefaultAccounts(dcRequest, userId, helper);
        createMessages(dcRequest, helper);
        createAccountStatements(dcRequest, helper);

        return userId;

    }

    private void createMBOrganisation(DataControllerRequest dcRequest, String username, DemoDataMB helper)
            throws HttpCallException {

        username = username + "MB";
        Map<String, String> input = new HashMap<>();
        String orgid = HelperMethods.generateUniqueOrganisationId(dcRequest);
        input.put("id", orgid);
        input.put("Type_Id", "TYPE_ID_BUSINESS");
        input.put("Name", "Davis Furniture Inc." + "_" + username);

        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.ORGANISATION_CREATE);

        helper.setMBOrgId(orgid);

        input.clear();
        input = helper.getMBOrganisationCommunication();
        Map<String, String> communicationTypes = HelperMethods.getCommunicationTypes();
        input = getCommunicationInformation(input);
        for (String key : input.keySet()) {
            String value = input.get(key);
            if (!StringUtils.isBlank(value)) {
                Map<String, String> inputParams = new HashMap<>();
                inputParams.put("Type_id", communicationTypes.get(key));
                inputParams.put("Organization_id", orgid);
                inputParams.put("Sequence", "1");
                inputParams.put("Value", value);
                inputParams.put("Extension", "Personal");
                HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                        URLConstants.ORGANISATIONCOMMUNICATION_CREATE);
            }
        }

        input.clear();
        String addrid = UUID.randomUUID().toString();
        input.put("id", addrid);
        input.put("addressLine1", "7380");
        input.put("addressLine2", "West Sand Lake Road,");
        input.put("cityName", "Orlando");
        input.put("state", "Florida");
        input.put("country", "USA");
        input.put("zipCode", "32819");
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.ADDRESS_CREATE);

        input.clear();
        input.put("Organization_id", orgid);
        input.put("Address_id", addrid);

        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.ORGANISATIONADDRESS_CREATE);

        input.clear();
        input.put("id", HelperMethods.getNumericId() + "");
        input.put("Organization_id", orgid);
        input.put("Taxid", HelperMethods.getTaxid() + "");
        input.put("Membership_id", HelperMethods.getNumericId() + "");

        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.ORGANISATIONMEMBERSHIP_CREATE);

        input.clear();

        input.put("FirstName", "Michael");
        input.put("MidleName", "");
        input.put("LastName", "Davis");
        input.put("DateOfBirth", "1985-05-05");
        input.put("Email", "michael.davis@gmail.com");
        input.put("Phone", "4258303691");
        input.put("Ssn", "906545256");
        input.put("Organization_id", orgid);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.ORGANISATIONOWNER_CREATE);

        // input.clear();
        //
        // String serviceLimits = "[{\"Service_id\":\"SERVICE_ID_1\", \"Name\":\"Intra
        // Bank Fund Transfer\", \"MaxTransactionLimit\":\"10000\",
        // \"MaxDailyLimit\":\"1000\"}, {\"Service_id\":\"SERVICE_ID_2\",
        // \"Name\":\"Transfer between customer's accounts\",
        // \"MaxTransactionLimit\":\"10000\", \"MaxDailyLimit\":\"1000\"},
        // {\"Service_id\":\"SERVICE_ID_3\", \"Name\":\"Interbank Account to Account
        // Fund Transfer\", \"MaxTransactionLimit\":\"10000\",
        // \"MaxDailyLimit\":\"1000\"}, {\"Service_id\":\"SERVICE_ID_4\",
        // \"Name\":\"Internation Account to Account Fund Transfer \",
        // \"MaxTransactionLimit\":\"10000\", \"MaxDailyLimit\":\"1000\"},
        // {\"Service_id\":\"SERVICE_ID_5\", \"Name\":\"Domestic Wire Transfer\",
        // \"MaxTransactionLimit\":\"10000\", \"MaxDailyLimit\":\"1000\"},
        // {\"Service_id\":\"SERVICE_ID_6\", \"Name\":\"International Wire Transfer\",
        // \"MaxTransactionLimit\":\"10000\", \"MaxDailyLimit\":\"1000\"},
        // {\"Service_id\":\"SERVICE_ID_7\", \"Name\":\"Bill Pay\",
        // \"MaxTransactionLimit\":\"5000\", \"MaxDailyLimit\":\"1000\"},
        // {\"Service_id\":\"SERVICE_ID_8\", \"Name\":\"P2P\",
        // \"MaxTransactionLimit\":\"1000\", \"MaxDailyLimit\":\"1000\"} ] ";
        // input.put("Customer_id", mbId);
        // input.put("Role_id", "GROUP_MICRO_ADMINISTRATOR");
        // input.put("services", serviceLimits);
        //
        // AdminUtil.invokeAPI(input, URLConstants.ADMIN_TRANSACTION_LIMITS_EDIT,
        // dcRequest);
    }

    @SuppressWarnings({ "static-access" })
    private String createMBCustomer(DataControllerRequest dcRequest, Map<String, String> inputmap, DemoDataMB helper)
            throws HttpCallException {
        Calendar usercal = Calendar.getInstance();
        String newUserName = inputmap.get("newUsername");
        String newPassword = inputmap.get("Password");
        String newEmail = inputmap.get("email");
        String phoneNumber = null;
        if (inputmap.get("phoneNumber") != null)
            phoneNumber = inputmap.get("phoneNumber").trim();
        String[] split = newEmail.split("@");
        if (split[1].equalsIgnoreCase("temenos.com")) {
            usercal.add(Calendar.DATE, 90);
        } else if (!(split[1].equalsIgnoreCase("kony.com") || split[1].equalsIgnoreCase("dieboldnixdorf.com")
                || split[1].equalsIgnoreCase("diebold.com"))) {
            usercal.add(Calendar.DATE, 15);
        } else {
            usercal.add(Calendar.DATE, 5000);
        }
        Map<String, String> input = helper.getAdminCustomerDemoData();

        String id = HelperMethods.generateUniqueCustomerId(dcRequest);
        input.put("id", id);

        input.put("UserName", newUserName + "MB");
        input.put("Password", newPassword);

        input.put("Bank_id", "1");

        input.put("CustomerType_id", "TYPE_ID_BUSINESS");
        input.put("isEnrolled", "true");
        input.put("CountryCode", inputmap.get("countryCode"));
        input.put("ValidDate", getDateString(usercal.getTime()));
        input.put("Organization_Id", helper.getMBOrgId());

        input.put("Lastlogintime", HelperMethods.getFormattedTimeStamp(new Date(), null));
        input.put("CurrentLoginTime", HelperMethods.getFormattedTimeStamp(new Date(), null));
        if (inputmap.get("firstName") != null) {
            input.put("FirstName", inputmap.get("firstName"));
        }

        if (inputmap.get("lastName") != null) {
            input.put("LastName", inputmap.get("lastName"));
        }
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.USER_CREATE);
        
        CustomerLegalEntityDTO customerLegalEntityDTO = new CustomerLegalEntityDTO();
		customerLegalEntityDTO.setId(HelperMethods.getNewId());
		customerLegalEntityDTO.setCustomer_id(id);
		customerLegalEntityDTO.setNew(true);
		customerLegalEntityDTO.setStatus_id(HelperMethods.getCustomerStatus().get("ACTIVE"));
		customerLegalEntityDTO.setLegalEntityId("GB0010001");
		customerLegalEntityDTO.persist(DTOUtils.getParameterMap(customerLegalEntityDTO, true), dcRequest.getHeaderMap());

        input.clear();
        input.put("id", id);
        input.put("Organization_Id", helper.getMBOrgId());
        input.put("Is_Owner", "true");

        CreateEmployee.invoke(input, dcRequest);

        PasswordHistoryManagement pm = new PasswordHistoryManagement(dcRequest, false);
        pm.makePasswordEntry(dcRequest, id, newPassword);

        input.clear();
        input = helper.getAdminCustomerCommunicationDemoData();
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
        input = helper.getAdminCustomerPreferenceDemoData();
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
                    postParamMapGroup.put("Group_id", "GROUP_MICRO_ADMINISTRATOR");
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
        return id;
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

    private static Set<String> getActionsListCreated(Result result) {
        String actionList = null;
        Set<String> hashSet = null;
        if (HelperMethods.hasRecords(result)) {
            actionList = result.getAllDatasets().get(0).getRecord(0).getParamValueByName("actionslist");
        }
        if (StringUtils.isNotBlank(actionList)) {
            hashSet = HelperMethods.splitString(actionList, DBPUtilitiesConstants.ACTIONS_SEPERATOR);
        } else {
            hashSet = new HashSet<>();
        }
        return hashSet;

    }

    private static String[] checkForMonetaryActionsAndconvertHashsetToStringArray(Set<String> hashSet,
            DataControllerRequest dcRequest) {
        String[] actionsList = new String[0];
        Map<String, String> input = new HashMap<>();
        Result result = new Result();
        StringBuilder actionsString = new StringBuilder();
        for (String action : hashSet) {
            actionsString.append(action);
            actionsString.append(",");
        }
        if (actionsString.length() > 0)
            actionsString.replace(actionsString.length() - 1, actionsString.length(), "");

        input.put("_featureActions", actionsString.toString());

        try {
            result = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                    URLConstants.GET_MONETARY_ACTIONS);
        } catch (HttpCallException e) {
            alert.prepareError(e.getMessage()).log();
        }

        String monetaryActionsString = HelperMethods.getFieldValue(result, "monetaryActions");
        if (StringUtils.isNotBlank(monetaryActionsString)) {
            actionsList = monetaryActionsString.split(",");
        }

        return actionsList;

    }

    private void createAccounts(DataControllerRequest dcRequest, String userId, String userName, DemoDataMB helper)
            throws HttpCallException {

        new DemoDataMB();

        Map<String, String> input = new HashMap<>();
        String features =
                "[\"ACCOUNT_AGGREGATION\",\"ACCOUNT_SETTINGS\",\"ALERT_MANAGEMENT\",\"APPLICANT_MANAGEMENT\",\"APPROVAL_MATRIX\",\"BILL_PAY\",\"CARD_MANAGEMENT\",\"CHECK_MANAGEMENT\",\"DISPUTE_TRANSACTIONS\",\"DOMESTIC_WIRE_TRANSFER\",\"FEEDBACK\",\"INTERNATIONAL_ACCOUNT_FUND_TRANSFER\",\"INTERNATIONAL_WIRE_TRANSFER\",\"INTER_BANK_ACCOUNT_FUND_TRANSFER\",\"INTRA_BANK_FUND_TRANSFER\",\"LOGIN\",\"MANAGE_E_STATEMENTS\",\"MESSAGES\",\"NAO\",\"NOTIFICATION\",\"P2P\",\"PASSWORD_UPDATE\",\"PAYEE_MANAGEMENT\",\"PROFILE_SETTINGS\",\"RDC\",\"RECIPIENT_MANAGEMENT\",\"RESET_SECURITY_QUESTIONS\",\"TRANSACTION_MANAGEMENT\",\"TRANSFER_BETWEEN_OWN_ACCOUNT\",\"USERNAME_UPDATE\",\"USER_MANAGEMENT\",\"PERSONAL_FINANCE_MANAGEMENT\",\"BULK_UPLOAD_FILES\"]";
        input.put("features", features);
        input.put("id", helper.getMBOrgId());
        // creates both organization features and organization action limits
        Result result = CreateOrganisationFeaturesAndActions.invoke(input, dcRequest, "TYPE_ID_BUSINESS");
        Set<String> hashSet = getActionsListCreated(result);

        String[] actionIds = checkForMonetaryActionsAndconvertHashsetToStringArray(hashSet, dcRequest);

        String accountNum = null;
        String accountName = null;
        Calendar cal = Calendar.getInstance();
        input.clear();
        input = helper.getMBAccount1DemoData();
        accountNum = getDummyAccountId();
        accountName = input.get("AccountName");
        helper.addAccountNum(accountName, accountNum);
        cal.add(Calendar.DATE, -1);
        input.put("Account_id", accountNum);
        // input.put("AccountName", input.get("AccountName") + "-X" + accountNum.substring(accountNum.length() - 4));
        input.put("Type_id", helper.getAccountId(input.get("Type_id")));
        input.put("Product_id", "8");
        input.put("Bank_id", "1");
        input.put("isBusinessAccount", "1");
        input.put("Organization_id", helper.getMBOrgId());
        input.put("LastPaymentDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        input.put("DividendLastPaidDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        input.put("EStatementmentEnable", "false");
        input.put("ownership", "Single");

        HelperMethods.removeNullValues(input);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_ACCOUNTS_CREATE);

        input.clear();
        input.put("id", HelperMethods.getNumericId() + "");
        input.put("Customer_id", userId);
        input.put("Account_id", accountNum);
        input.put("AccountName", accountName + "-X" + accountNum.substring(accountNum.length() - 4));
        input.put("IsOrganizationAccount", "true");
        input.put("IsOrgAccountUnLinked", "false");
        input.put("Organization_id", helper.getMBOrgId());

        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERACCOUNTS_CREATE);

        input = helper.getMBAccount2DemoData();
        accountNum = getDummyAccountId();
        accountName = input.get("AccountName");
        helper.addAccountNum(accountName, accountNum);
        cal.add(Calendar.DATE, -1);
        input.put("Account_id", accountNum);
        // input.put("AccountName", input.get("AccountName") + "-X" + accountNum.substring(accountNum.length() - 4));
        input.put("Type_id", helper.getAccountId(input.get("Type_id")));
        input.put("Product_id", "11");
        input.put("Bank_id", "1");
        input.put("isBusinessAccount", "1");
        input.put("Organization_id", helper.getMBOrgId());
        input.put("LastPaymentDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        input.put("DividendLastPaidDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        input.put("EStatementmentEnable", "false");
        input.put("ownership", "Single");

        HelperMethods.removeNullValues(input);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_ACCOUNTS_CREATE);

        input.clear();
        input.put("id", HelperMethods.getNumericId() + "");
        input.put("Customer_id", userId);
        input.put("Account_id", accountNum);
        input.put("AccountName", accountName + "-X" + accountNum.substring(accountNum.length() - 4));
        input.put("IsOrganizationAccount", "true");
        input.put("IsOrgAccountUnLinked", "false");
        input.put("Organization_id", helper.getMBOrgId());

        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERACCOUNTS_CREATE);

        input = helper.getMBAccount3DemoData();
        accountNum = getDummyAccountId();
        accountName = input.get("AccountName");
        helper.addAccountNum(accountName, accountNum);
        cal.add(Calendar.DATE, -1);
        input.put("Account_id", accountNum);
        // input.put("AccountName", input.get("AccountName") + "-X" + accountNum.substring(accountNum.length() - 4));
        input.put("Type_id", helper.getAccountId(input.get("Type_id")));
        input.put("Product_id", "2");
        input.put("Bank_id", "1");
        input.put("isBusinessAccount", "1");
        input.put("Organization_id", helper.getMBOrgId());
        input.put("LastPaymentDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        input.put("DividendLastPaidDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        input.put("EStatementmentEnable", "false");
        input.put("ownership", "Single");

        HelperMethods.removeNullValues(input);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_ACCOUNTS_CREATE);

        input.clear();
        input.put("id", HelperMethods.getNumericId() + "");
        input.put("Customer_id", userId);
        input.put("Account_id", accountNum);
        input.put("AccountName", accountName + "-X" + accountNum.substring(accountNum.length() - 4));
        input.put("IsOrganizationAccount", "true");
        input.put("IsOrgAccountUnLinked", "false");
        input.put("Organization_id", helper.getMBOrgId());

        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERACCOUNTS_CREATE);
        input = helper.getMBAccount4DemoData();
        accountNum = getDummyAccountId();
        accountName = input.get("AccountName");
        helper.addAccountNum(accountName, accountNum);
        cal.add(Calendar.DATE, -1);
        input.put("Account_id", accountNum);
        // input.put("AccountName", input.get("AccountName") + "-X" + accountNum.substring(accountNum.length() - 4));
        input.put("Type_id", helper.getAccountId(input.get("Type_id")));
        input.put("Product_id", "5");
        input.put("Bank_id", "1");
        input.put("isBusinessAccount", "1");
        input.put("Organization_id", helper.getMBOrgId());
        input.put("LastPaymentDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        input.put("DividendLastPaidDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        input.put("EStatementmentEnable", "false");
        input.put("ownership", "Single");

        HelperMethods.removeNullValues(input);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_ACCOUNTS_CREATE);

        input.clear();
        input.put("id", HelperMethods.getNumericId() + "");
        input.put("Customer_id", userId);
        input.put("Account_id", accountNum);
        input.put("AccountName", accountName + "-X" + accountNum.substring(accountNum.length() - 4));
        input.put("IsOrganizationAccount", "true");
        input.put("IsOrgAccountUnLinked", "false");
        input.put("Organization_id", helper.getMBOrgId());

        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERACCOUNTS_CREATE);

        input = helper.getMBAccount5DemoData();
        accountNum = getDummyAccountId();
        accountName = input.get("AccountName");
        helper.addAccountNum(accountName, accountNum);
        cal.add(Calendar.DATE, -1);
        input.put("Account_id", accountNum);
        input.put("Type_id", helper.getAccountId(input.get("Type_id")));
        input.put("AccountName", input.get("AccountName") + "-X" + accountNum.substring(accountNum.length() - 4));
        input.put("Product_id", "4");
        input.put("Bank_id", "1");
        input.put("isBusinessAccount", "1");
        input.put("Organization_id", helper.getMBOrgId());
        input.put("LastPaymentDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        input.put("DividendLastPaidDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        input.put("EStatementmentEnable", "false");
        input.put("ownership", "Single");

        HelperMethods.removeNullValues(input);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_ACCOUNTS_CREATE);

        input.clear();
        input.put("id", HelperMethods.getNumericId() + "");
        input.put("Customer_id", userId);
        input.put("Account_id", accountNum);
        input.put("AccountName", accountName + "-X" + accountNum.substring(accountNum.length() - 4));
        input.put("IsOrganizationAccount", "true");
        input.put("IsOrgAccountUnLinked", "false");
        input.put("Organization_id", helper.getMBOrgId());

        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERACCOUNTS_CREATE);

        createApprovalMatrixForMBUser(helper, actionIds, userId);

    }

    private void createApprovalMatrixForMBUser(DemoDataMB helper, String[] actionIds, String approver) {
        try {
            String companyId = helper.getMBOrgId();

            String[] accounts = new String[5];

            accounts[0] = helper.getAccountNum("Rewards Savings");
            accounts[1] = helper.getAccountNum("Core Checking");
            accounts[2] = helper.getAccountNum("Freedom Credit Card");
            accounts[3] = helper.getAccountNum("12 Months Term Deposit");
            accounts[4] = helper.getAccountNum("Turbo Auto Loan");

            String[] limitTypeIds = { "MAX_TRANSACTION_LIMIT", "WEEKLY_LIMIT", "DAILY_LIMIT" };

            String approvalruleId = "ALL";
            double lowerlimit = 0, upperlimit = 0;

            StringBuilder approvalmatrixValues = new StringBuilder();
            StringBuilder approvalIds = new StringBuilder();

            OrganisationBusinessDelegate organizationDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class)
                    .getBusinessDelegate(OrganisationBusinessDelegate.class);

            for (String accountId : accounts) {
                for (String actionId : actionIds) {
                    LimitsDTO companyLimitsDTO = organizationDelegate.fetchLimits(companyId, actionId);
                    for (String limitTypeId : limitTypeIds) {
                        switch (limitTypeId) {
                            case Constants.MAX_TRANSACTION_LIMIT:
                                upperlimit = companyLimitsDTO.getMaxTransactionLimit();
                                break;
                            case Constants.DAILY_LIMIT:
                                upperlimit = companyLimitsDTO.getDailyLimit();
                                break;
                            case Constants.WEEKLY_LIMIT:
                                upperlimit = companyLimitsDTO.getWeeklyLimit();
                                break;
                            default:
                                break;
                        }
                        // name,companyId,actionId,accountId,approvalruleId,limitTypeId,lowerlimit,upperlimit
                        approvalmatrixValues.append("\"").append(companyId).append("-").append(actionId).append("\"")
                                .append(";");
                        approvalmatrixValues.append("\"").append(companyId).append("\"").append(";");
                        approvalmatrixValues.append("\"").append(actionId).append("\"").append(";");
                        approvalmatrixValues.append("\"").append(accountId).append("\"").append(";");
                        approvalmatrixValues.append("\"").append(approvalruleId).append("\"").append(";");
                        approvalmatrixValues.append("\"").append(limitTypeId).append("\"").append(";");
                        approvalmatrixValues.append("\"").append(lowerlimit).append("\"").append(";");
                        approvalmatrixValues.append("\"").append(upperlimit).append("\"").append(",");

                        approvalIds.append(approver).append(",");
                    }
                }
            }
            approvalmatrixValues.deleteCharAt(approvalmatrixValues.length() - 1);
            approvalIds.deleteCharAt(approvalIds.length() - 1);

            ApprovalMatrixBusinessDelegate approvalMatrixBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class)
                    .getBusinessDelegate(ApprovalMatrixBusinessDelegate.class);

            approvalMatrixBusinessDelegate.callCreateApprovalMatrixStoredProc(approvalmatrixValues.toString(),
                    approvalIds.toString());
        } catch (Exception e) {
            alert.prepareError("Error occured while creating approval matrix" + e.getMessage()).log();
        }
    }

    private void createCustomerActionLimits(DataControllerRequest dcRequest, String userId, DemoDataMB helper) {

        Result result = new Result();
        JSONArray actions = new JSONArray();
        JSONArray accounts = new JSONArray();
        JSONArray featuresJSON = null;

        Map<String, String> generatedAccounts = helper.getGeneratedAccounts();

        for (Entry<String, String> accountsEntry : generatedAccounts.entrySet()) {
            JSONObject json = new JSONObject();
            json.put("accountName", accountsEntry.getKey());
            json.put("accountId", accountsEntry.getValue());
            accounts.put(json);
        }

        String features =
                "[{\"featureName\":\"Transaction management\",\"featureID\":\"TRANSACTION_MANAGEMENT\",\"featureDescription\":\"Transaction management\",\"Actions\":[{\"actionType\":\"NON_MONETARY\",\"isEnabled\":\"true\",\"actionId\":\"TRANSACTION_MANAGEMENT_DELETE\",\"actionDescription\":\"Delete Transaction\",\"actionName\":\"Delete Transaction\",\"isAccountLevel\":\"false\"}]},{\"featureName\":\"Applicant\",\"featureID\":\"APPLICANT_MANAGEMENT\",\"featureDescription\":\"Applicant\",\"Actions\":[{\"actionType\":\"NON_MONETARY\",\"isEnabled\":\"true\",\"actionId\":\"APPLICANT_MANAGEMENT_CREATE\",\"actionDescription\":\"Create Applicant\",\"actionName\":\"Create Applicant\",\"isAccountLevel\":\"false\"}]},{\"featureName\":\"Bulk upload of files\",\"featureID\":\"BULK_UPLOAD_FILES\",\"featureDescription\":\"Bulk upload of files\",\"Actions\":[{\"actionType\":\"NON_MONETARY\",\"isEnabled\":\"true\",\"actionId\":\"WIRE_FILES\",\"actionDescription\":\"Wire transfer files\",\"actionName\":\"Wire transfer files\",\"isAccountLevel\":\"false\"}]},{\"featureName\":\"Reset Security Questions\",\"featureID\":\"RESET_SECURITY_QUESTIONS\",\"featureDescription\":\"Reset Security Questions\",\"Actions\":[{\"actionType\":\"NON_MONETARY\",\"isEnabled\":\"true\",\"actionId\":\"RESET_SECURITY_QUESTIONS\",\"actionDescription\":\"Reset Security Questions\",\"actionName\":\"Reset Security Questions\",\"isAccountLevel\":\"false\"}]},{\"featureName\":\"Payee Management\",\"featureID\":\"PAYEE_MANAGEMENT\",\"featureDescription\":\"Payee Management\",\"Actions\":[{\"actionType\":\"NON_MONETARY\",\"isEnabled\":\"true\",\"actionId\":\"PAYEE_MANAGEMENT_CREATE\",\"actionDescription\":\"Create Payee\",\"actionName\":\"Create Payee\",\"isAccountLevel\":\"false\"},{\"actionType\":\"NON_MONETARY\",\"isEnabled\":\"true\",\"actionId\":\"PAYEE_MANAGEMENT_DELETE\",\"actionDescription\":\"Delete Payee\",\"actionName\":\"Delete Payee\",\"isAccountLevel\":\"false\"},{\"actionType\":\"NON_MONETARY\",\"isEnabled\":\"true\",\"actionId\":\"PAYEE_MANAGEMENT_EDIT\",\"actionDescription\":\"Edit Payee\",\"actionName\":\"Edit Payee\",\"isAccountLevel\":\"false\"},{\"actionType\":\"NON_MONETARY\",\"isEnabled\":\"true\",\"actionId\":\"PAYEE_MANAGEMENT_VIEW\",\"actionDescription\":\"View Payee\",\"actionName\":\"View Payee\",\"isAccountLevel\":\"false\"}]},{\"featureName\":\"Recipient Management\",\"featureID\":\"RECIPIENT_MANAGEMENT\",\"featureDescription\":\"Recipient Management\",\"Actions\":[{\"actionType\":\"NON_MONETARY\",\"isEnabled\":\"true\",\"actionId\":\"RECIPIENT_MANAGEMENT_VIEW\",\"actionDescription\":\"View Recipient\",\"actionName\":\"View Recipient\",\"isAccountLevel\":\"false\"}]},{\"featureName\":\"User Management\",\"featureID\":\"USER_MANAGEMENT\",\"featureDescription\":\"Manage the Business Online access of the other user\",\"Actions\":[{\"actionType\":\"NON_MONETARY\",\"isEnabled\":\"true\",\"actionId\":\"USER_MANAGEMENT\",\"actionDescription\":\"Manage the Business Online access of the other user\",\"actionName\":\"User Management\",\"isAccountLevel\":\"false\"},{\"actionType\":\"NON_MONETARY\",\"isEnabled\":\"true\",\"actionId\":\"USER_MANAGEMENT_ACTIVATE_OR_SUSPEND\",\"actionDescription\":\"User management activate or suspend\",\"actionName\":\"User management activate or suspend\",\"isAccountLevel\":\"false\"},{\"actionType\":\"NON_MONETARY\",\"isEnabled\":\"true\",\"actionId\":\"USER_MANAGEMENT_CREATE\",\"actionDescription\":\"User management create\",\"actionName\":\"User management create\",\"isAccountLevel\":\"false\"},{\"actionType\":\"NON_MONETARY\",\"isEnabled\":\"true\",\"actionId\":\"USER_MANAGEMENT_EDIT\",\"actionDescription\":\"User management edit\",\"actionName\":\"User management edit\",\"isAccountLevel\":\"false\"},{\"actionType\":\"NON_MONETARY\",\"isEnabled\":\"true\",\"actionId\":\"USER_MANAGEMENT_VIEW\",\"actionDescription\":\"User management view\",\"actionName\":\"User management view\",\"isAccountLevel\":\"false\"}]},{\"featureName\":\"Approval Matrix\",\"featureID\":\"APPROVAL_MATRIX\",\"featureDescription\":\"Approval Matrix\",\"Actions\":[{\"actionType\":\"NON_MONETARY\",\"isEnabled\":\"true\",\"actionId\":\"APPROVAL_MATRIX_MANAGE\",\"actionDescription\":\"Approval matrix manage\",\"actionName\":\"Approval matrix manage\",\"isAccountLevel\":\"false\"},{\"actionType\":\"NON_MONETARY\",\"isEnabled\":\"true\",\"actionId\":\"APPROVAL_MATRIX_VIEW\",\"actionDescription\":\"Approval matrix view\",\"actionName\":\"Approval matrix view\",\"isAccountLevel\":\"false\"}]},{\"featureName\":\"Bill Payment Service\",\"featureID\":\"BILL_PAY\",\"featureDescription\":\"Pay your bills instantly\",\"Actions\":[{\"actionType\":\"NON_MONETARY\",\"isEnabled\":\"true\",\"actionId\":\"BILL_PAY_ACTIVATE_OR_DEACTIVATE\",\"actionDescription\":\"Activate or Deactivate Bill Pay\",\"actionName\":\"Activate or Deactivate Bill Pay\",\"isAccountLevel\":\"false\"},{\"actionType\":\"NON_MONETARY\",\"isEnabled\":\"true\",\"actionId\":\"BILL_PAY_BULK\",\"actionDescription\":\"Bulk Bill Pay\",\"actionName\":\"Bulk Bill Pay\",\"isAccountLevel\":\"true\",\"Accounts\":[{\"id\":\"200402063722030\",\"isEnabled\":\"true\"},{\"id\":\"200402063722372\",\"isEnabled\":\"true\"},{\"id\":\"200402063722728\",\"isEnabled\":\"true\"},{\"id\":\"200402063723321\",\"isEnabled\":\"true\"},{\"id\":\"200402063724099\",\"isEnabled\":\"true\"}]},{\"actionType\":\"MONETARY\",\"isEnabled\":\"true\",\"actionId\":\"BILL_PAY_CREATE\",\"actionDescription\":\"Bill Payment Service\",\"actionName\":\"Bill Payment Service\",\"isAccountLevel\":\"true\",\"Accounts\":[{\"id\":\"200402063722030\",\"isEnabled\":\"true\",\"limits\":{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"0\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"500\",\"PRE_APPROVED_DAILY_LIMIT\":\"0\",\"AUTO_DENIED_DAILY_LIMIT\":\"1000\",\"PRE_APPROVED_WEEKLY_LIMIT\":\"0\",\"AUTO_DENIED_WEEKLY_LIMIT\":\"5000\"}},{\"id\":\"200402063722372\",\"isEnabled\":\"true\",\"limits\":{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"0\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"500\",\"PRE_APPROVED_DAILY_LIMIT\":\"0\",\"AUTO_DENIED_DAILY_LIMIT\":\"1000\",\"PRE_APPROVED_WEEKLY_LIMIT\":\"0\",\"AUTO_DENIED_WEEKLY_LIMIT\":\"5000\"}},{\"id\":\"200402063722728\",\"isEnabled\":\"true\",\"limits\":{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"0\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"500\",\"PRE_APPROVED_DAILY_LIMIT\":\"0\",\"AUTO_DENIED_DAILY_LIMIT\":\"1000\",\"PRE_APPROVED_WEEKLY_LIMIT\":\"0\",\"AUTO_DENIED_WEEKLY_LIMIT\":\"5000\"}},{\"id\":\"200402063723321\",\"isEnabled\":\"true\",\"limits\":{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"0\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"500\",\"PRE_APPROVED_DAILY_LIMIT\":\"0\",\"AUTO_DENIED_DAILY_LIMIT\":\"1000\",\"PRE_APPROVED_WEEKLY_LIMIT\":\"0\",\"AUTO_DENIED_WEEKLY_LIMIT\":\"5000\"}},{\"id\":\"200402063724099\",\"isEnabled\":\"true\",\"limits\":{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"0\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"500\",\"PRE_APPROVED_DAILY_LIMIT\":\"0\",\"AUTO_DENIED_DAILY_LIMIT\":\"1000\",\"PRE_APPROVED_WEEKLY_LIMIT\":\"0\",\"AUTO_DENIED_WEEKLY_LIMIT\":\"5000\"}}]}]},{\"featureName\":\"Manage Debit and Credit Cards\",\"featureID\":\"CARD_MANAGEMENT\",\"featureDescription\":\"View card information, activate or deactivate a card, request replacement for a card, report a lost or stolen card, cancel the usage of a card, and request a new PIN for a card\",\"Actions\":[{\"actionType\":\"NON_MONETARY\",\"isEnabled\":\"true\",\"actionId\":\"CARD_MANAGEMENT_CHANGE_PIN\",\"actionDescription\":\"Change debit card pin online or raise a request to change credit card pin\",\"actionName\":\"Card Management – Change PIN\",\"isAccountLevel\":\"false\"},{\"actionType\":\"NON_MONETARY\",\"isEnabled\":\"true\",\"actionId\":\"CARD_MANAGEMENT_CREATE_CARD_REQUEST\",\"actionDescription\":\"Card Management Create Card Request\",\"actionName\":\"Card Management – Create card request\",\"isAccountLevel\":\"false\"},{\"actionType\":\"NON_MONETARY\",\"isEnabled\":\"true\",\"actionId\":\"CARD_MANAGEMENT_LOCK_CARD\",\"actionDescription\":\"Temporarily lock or disable any debit or credit card\",\"actionName\":\"Card Management – Lock Card\",\"isAccountLevel\":\"false\"},{\"actionType\":\"NON_MONETARY\",\"isEnabled\":\"true\",\"actionId\":\"CARD_MANAGEMENT_PARTIAL_UPDATE\",\"actionDescription\":\"Card management partial update\",\"actionName\":\"Card Management – Partial update\",\"isAccountLevel\":\"false\"},{\"actionType\":\"NON_MONETARY\",\"isEnabled\":\"true\",\"actionId\":\"CARD_MANAGEMENT_REPLACE_CARD\",\"actionDescription\":\"Request a replacement for any of the saved debit or credit card\",\"actionName\":\"Card Management – Replace Card\",\"isAccountLevel\":\"false\"},{\"actionType\":\"NON_MONETARY\",\"isEnabled\":\"true\",\"actionId\":\"CARD_MANAGEMENT_UNLOCK_CARD\",\"actionDescription\":\"Unlock a temporarily locked card\",\"actionName\":\"Card Management – Unlock Card\",\"isAccountLevel\":\"false\"}]},{\"featureName\":\"Profile Settings\",\"featureID\":\"PROFILE_SETTINGS\",\"featureDescription\":\"Profile Settings\",\"Actions\":[{\"actionType\":\"NON_MONETARY\",\"isEnabled\":\"true\",\"actionId\":\"PROFILE_SETTINGS_UPDATE\",\"actionDescription\":\"Update Profile Settings\",\"actionName\":\"Update Profile Settings\",\"isAccountLevel\":\"false\"},{\"actionType\":\"NON_MONETARY\",\"isEnabled\":\"true\",\"actionId\":\"PROFILE_SETTINGS_VIEW\",\"actionDescription\":\"View Profile Settings\",\"actionName\":\"View Profile Settings\",\"isAccountLevel\":\"false\"}]},{\"featureName\":\"Notification\",\"featureID\":\"NOTIFICATION\",\"featureDescription\":\"Notification\",\"Actions\":[{\"actionType\":\"NON_MONETARY\",\"isEnabled\":\"true\",\"actionId\":\"NOTIFICATION_UPDATE\",\"actionDescription\":\"Update Notification\",\"actionName\":\"Update Notification\",\"isAccountLevel\":\"false\"}]},{\"featureName\":\"Customer requests and messages\",\"featureID\":\"MESSAGES\",\"featureDescription\":\"Customer requests and messages\",\"Actions\":[{\"actionType\":\"NON_MONETARY\",\"isEnabled\":\"true\",\"actionId\":\"MESSAGES_CREATE\",\"actionDescription\":\"Create Message\",\"actionName\":\"Create Message\",\"isAccountLevel\":\"false\"},{\"actionType\":\"NON_MONETARY\",\"isEnabled\":\"true\",\"actionId\":\"MESSAGES_DELETE\",\"actionDescription\":\"Delete Message\",\"actionName\":\"Delete Message\",\"isAccountLevel\":\"false\"},{\"actionType\":\"NON_MONETARY\",\"isEnabled\":\"true\",\"actionId\":\"MESSAGES_REPLY\",\"actionDescription\":\"Reply Message\",\"actionName\":\"Reply Message\",\"isAccountLevel\":\"false\"},{\"actionType\":\"NON_MONETARY\",\"isEnabled\":\"true\",\"actionId\":\"MESSAGES_VIEW\",\"actionDescription\":\"View Message\",\"actionName\":\"View Message\",\"isAccountLevel\":\"false\"}]},{\"featureName\":\"Feedback\",\"featureID\":\"FEEDBACK\",\"featureDescription\":\"Feedback\",\"Actions\":[{\"actionType\":\"NON_MONETARY\",\"isEnabled\":\"true\",\"actionId\":\"FEEDBACK_SUBMIT\",\"actionDescription\":\"Submit Feedback\",\"actionName\":\"Submit Feedback\",\"isAccountLevel\":\"false\"}]},{\"featureName\":\"Intra Bank Fund Transfer\",\"featureID\":\"INTRA_BANK_FUND_TRANSFER\",\"featureDescription\":\"Fund Transfer to other members of the Credit Union. Member to member Transfer\",\"Actions\":[{\"actionType\":\"MONETARY\",\"isEnabled\":\"true\",\"actionId\":\"INTRA_BANK_FUND_TRANSFER_CREATE\",\"actionDescription\":\"Fund Transfer to other members of the Credit Union. Member to member Transfer\",\"actionName\":\"Intra Bank Fund Transfer - Create\",\"isAccountLevel\":\"true\",\"Accounts\":[{\"id\":\"200402063722030\",\"isEnabled\":\"true\",\"limits\":{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"0\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"500\",\"PRE_APPROVED_DAILY_LIMIT\":\"0\",\"AUTO_DENIED_DAILY_LIMIT\":\"1000\",\"PRE_APPROVED_WEEKLY_LIMIT\":\"0\",\"AUTO_DENIED_WEEKLY_LIMIT\":\"5000\"}},{\"id\":\"200402063722372\",\"isEnabled\":\"true\",\"limits\":{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"0\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"500\",\"PRE_APPROVED_DAILY_LIMIT\":\"0\",\"AUTO_DENIED_DAILY_LIMIT\":\"1000\",\"PRE_APPROVED_WEEKLY_LIMIT\":\"0\",\"AUTO_DENIED_WEEKLY_LIMIT\":\"5000\"}},{\"id\":\"200402063722728\",\"isEnabled\":\"true\",\"limits\":{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"0\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"500\",\"PRE_APPROVED_DAILY_LIMIT\":\"0\",\"AUTO_DENIED_DAILY_LIMIT\":\"1000\",\"PRE_APPROVED_WEEKLY_LIMIT\":\"0\",\"AUTO_DENIED_WEEKLY_LIMIT\":\"5000\"}},{\"id\":\"200402063723321\",\"isEnabled\":\"true\",\"limits\":{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"0\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"500\",\"PRE_APPROVED_DAILY_LIMIT\":\"0\",\"AUTO_DENIED_DAILY_LIMIT\":\"1000\",\"PRE_APPROVED_WEEKLY_LIMIT\":\"0\",\"AUTO_DENIED_WEEKLY_LIMIT\":\"5000\"}},{\"id\":\"200402063724099\",\"isEnabled\":\"true\",\"limits\":{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"0\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"500\",\"PRE_APPROVED_DAILY_LIMIT\":\"0\",\"AUTO_DENIED_DAILY_LIMIT\":\"1000\",\"PRE_APPROVED_WEEKLY_LIMIT\":\"0\",\"AUTO_DENIED_WEEKLY_LIMIT\":\"5000\"}}]}]},{\"featureName\":\"Domestic Wire Transfer\",\"featureID\":\"DOMESTIC_WIRE_TRANSFER\",\"featureDescription\":\"Wire Transfers within the country\",\"Actions\":[{\"actionType\":\"MONETARY\",\"isEnabled\":\"true\",\"actionId\":\"DOMESTIC_WIRE_TRANSFER_CREATE\",\"actionDescription\":\"Wire Transfers within the country\",\"actionName\":\"Wire Transfers within the country\",\"isAccountLevel\":\"true\",\"Accounts\":[{\"id\":\"200402063722030\",\"isEnabled\":\"true\",\"limits\":{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"0\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"500\",\"PRE_APPROVED_DAILY_LIMIT\":\"0\",\"AUTO_DENIED_DAILY_LIMIT\":\"1000\",\"PRE_APPROVED_WEEKLY_LIMIT\":\"0\",\"AUTO_DENIED_WEEKLY_LIMIT\":\"5000\"}},{\"id\":\"200402063722372\",\"isEnabled\":\"true\",\"limits\":{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"0\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"500\",\"PRE_APPROVED_DAILY_LIMIT\":\"0\",\"AUTO_DENIED_DAILY_LIMIT\":\"1000\",\"PRE_APPROVED_WEEKLY_LIMIT\":\"0\",\"AUTO_DENIED_WEEKLY_LIMIT\":\"5000\"}},{\"id\":\"200402063722728\",\"isEnabled\":\"true\",\"limits\":{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"0\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"500\",\"PRE_APPROVED_DAILY_LIMIT\":\"0\",\"AUTO_DENIED_DAILY_LIMIT\":\"1000\",\"PRE_APPROVED_WEEKLY_LIMIT\":\"0\",\"AUTO_DENIED_WEEKLY_LIMIT\":\"5000\"}},{\"id\":\"200402063723321\",\"isEnabled\":\"true\",\"limits\":{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"0\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"500\",\"PRE_APPROVED_DAILY_LIMIT\":\"0\",\"AUTO_DENIED_DAILY_LIMIT\":\"1000\",\"PRE_APPROVED_WEEKLY_LIMIT\":\"0\",\"AUTO_DENIED_WEEKLY_LIMIT\":\"5000\"}},{\"id\":\"200402063724099\",\"isEnabled\":\"true\",\"limits\":{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"0\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"500\",\"PRE_APPROVED_DAILY_LIMIT\":\"0\",\"AUTO_DENIED_DAILY_LIMIT\":\"1000\",\"PRE_APPROVED_WEEKLY_LIMIT\":\"0\",\"AUTO_DENIED_WEEKLY_LIMIT\":\"5000\"}}]}]},{\"featureName\":\"International Account to Account Fund Transfer \",\"featureID\":\"INTERNATIONAL_ACCOUNT_FUND_TRANSFER\",\"featureDescription\":\"Fund Transfer to Accounts in International Banks and Credit Unions\",\"Actions\":[{\"actionType\":\"MONETARY\",\"isEnabled\":\"true\",\"actionId\":\"INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE\",\"actionDescription\":\"Fund Transfer to Accounts in International Banks and Credit Unions\",\"actionName\":\"Fund Transfer to Accounts in International Banks and Credit Unions\",\"isAccountLevel\":\"true\",\"Accounts\":[{\"id\":\"200402063722030\",\"isEnabled\":\"true\",\"limits\":{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"0\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"500\",\"PRE_APPROVED_DAILY_LIMIT\":\"0\",\"AUTO_DENIED_DAILY_LIMIT\":\"1000\",\"PRE_APPROVED_WEEKLY_LIMIT\":\"0\",\"AUTO_DENIED_WEEKLY_LIMIT\":\"5000\"}},{\"id\":\"200402063722372\",\"isEnabled\":\"true\",\"limits\":{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"0\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"500\",\"PRE_APPROVED_DAILY_LIMIT\":\"0\",\"AUTO_DENIED_DAILY_LIMIT\":\"1000\",\"PRE_APPROVED_WEEKLY_LIMIT\":\"0\",\"AUTO_DENIED_WEEKLY_LIMIT\":\"5000\"}},{\"id\":\"200402063722728\",\"isEnabled\":\"true\",\"limits\":{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"0\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"500\",\"PRE_APPROVED_DAILY_LIMIT\":\"0\",\"AUTO_DENIED_DAILY_LIMIT\":\"1000\",\"PRE_APPROVED_WEEKLY_LIMIT\":\"0\",\"AUTO_DENIED_WEEKLY_LIMIT\":\"5000\"}},{\"id\":\"200402063723321\",\"isEnabled\":\"true\",\"limits\":{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"0\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"500\",\"PRE_APPROVED_DAILY_LIMIT\":\"0\",\"AUTO_DENIED_DAILY_LIMIT\":\"1000\",\"PRE_APPROVED_WEEKLY_LIMIT\":\"0\",\"AUTO_DENIED_WEEKLY_LIMIT\":\"5000\"}},{\"id\":\"200402063724099\",\"isEnabled\":\"true\",\"limits\":{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"0\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"500\",\"PRE_APPROVED_DAILY_LIMIT\":\"0\",\"AUTO_DENIED_DAILY_LIMIT\":\"1000\",\"PRE_APPROVED_WEEKLY_LIMIT\":\"0\",\"AUTO_DENIED_WEEKLY_LIMIT\":\"5000\"}}]}]},{\"featureName\":\"Person to Person Transfer\",\"featureID\":\"P2P\",\"featureDescription\":\"Send money to anyone using their mobile number or email id\",\"Actions\":[{\"actionType\":\"MONETARY\",\"isEnabled\":\"true\",\"actionId\":\"P2P_CREATE\",\"actionDescription\":\"Person to Person Transfer\",\"actionName\":\"Person to Person Transfer\",\"isAccountLevel\":\"true\",\"Accounts\":[{\"id\":\"200402063722030\",\"isEnabled\":\"true\",\"limits\":{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"0\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"500\",\"PRE_APPROVED_DAILY_LIMIT\":\"0\",\"AUTO_DENIED_DAILY_LIMIT\":\"1000\",\"PRE_APPROVED_WEEKLY_LIMIT\":\"0\",\"AUTO_DENIED_WEEKLY_LIMIT\":\"5000\"}},{\"id\":\"200402063722372\",\"isEnabled\":\"true\",\"limits\":{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"0\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"500\",\"PRE_APPROVED_DAILY_LIMIT\":\"0\",\"AUTO_DENIED_DAILY_LIMIT\":\"1000\",\"PRE_APPROVED_WEEKLY_LIMIT\":\"0\",\"AUTO_DENIED_WEEKLY_LIMIT\":\"5000\"}},{\"id\":\"200402063722728\",\"isEnabled\":\"true\",\"limits\":{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"0\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"500\",\"PRE_APPROVED_DAILY_LIMIT\":\"0\",\"AUTO_DENIED_DAILY_LIMIT\":\"1000\",\"PRE_APPROVED_WEEKLY_LIMIT\":\"0\",\"AUTO_DENIED_WEEKLY_LIMIT\":\"5000\"}},{\"id\":\"200402063723321\",\"isEnabled\":\"true\",\"limits\":{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"0\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"500\",\"PRE_APPROVED_DAILY_LIMIT\":\"0\",\"AUTO_DENIED_DAILY_LIMIT\":\"1000\",\"PRE_APPROVED_WEEKLY_LIMIT\":\"0\",\"AUTO_DENIED_WEEKLY_LIMIT\":\"5000\"}},{\"id\":\"200402063724099\",\"isEnabled\":\"true\",\"limits\":{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"0\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"500\",\"PRE_APPROVED_DAILY_LIMIT\":\"0\",\"AUTO_DENIED_DAILY_LIMIT\":\"1000\",\"PRE_APPROVED_WEEKLY_LIMIT\":\"0\",\"AUTO_DENIED_WEEKLY_LIMIT\":\"5000\"}}]}]},{\"featureName\":\"Transfer between Customer's Accounts\",\"featureID\":\"TRANSFER_BETWEEN_OWN_ACCOUNT\",\"featureDescription\":\"Fund Transfer between different eligible accounts of a member\",\"Actions\":[{\"actionType\":\"MONETARY\",\"isEnabled\":\"true\",\"actionId\":\"TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE\",\"actionDescription\":\"Fund Transfer between different eligible accounts of a member\",\"actionName\":\"Transfer between Customer's Accounts - Create Transaction\",\"isAccountLevel\":\"true\",\"Accounts\":[{\"id\":\"200402063722030\",\"isEnabled\":\"true\",\"limits\":{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"0\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"500\",\"PRE_APPROVED_DAILY_LIMIT\":\"0\",\"AUTO_DENIED_DAILY_LIMIT\":\"1000\",\"PRE_APPROVED_WEEKLY_LIMIT\":\"0\",\"AUTO_DENIED_WEEKLY_LIMIT\":\"5000\"}},{\"id\":\"200402063722372\",\"isEnabled\":\"true\",\"limits\":{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"0\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"500\",\"PRE_APPROVED_DAILY_LIMIT\":\"0\",\"AUTO_DENIED_DAILY_LIMIT\":\"1000\",\"PRE_APPROVED_WEEKLY_LIMIT\":\"0\",\"AUTO_DENIED_WEEKLY_LIMIT\":\"5000\"}},{\"id\":\"200402063722728\",\"isEnabled\":\"true\",\"limits\":{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"0\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"500\",\"PRE_APPROVED_DAILY_LIMIT\":\"0\",\"AUTO_DENIED_DAILY_LIMIT\":\"1000\",\"PRE_APPROVED_WEEKLY_LIMIT\":\"0\",\"AUTO_DENIED_WEEKLY_LIMIT\":\"5000\"}},{\"id\":\"200402063723321\",\"isEnabled\":\"true\",\"limits\":{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"0\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"500\",\"PRE_APPROVED_DAILY_LIMIT\":\"0\",\"AUTO_DENIED_DAILY_LIMIT\":\"1000\",\"PRE_APPROVED_WEEKLY_LIMIT\":\"0\",\"AUTO_DENIED_WEEKLY_LIMIT\":\"5000\"}},{\"id\":\"200402063724099\",\"isEnabled\":\"true\",\"limits\":{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"0\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"500\",\"PRE_APPROVED_DAILY_LIMIT\":\"0\",\"AUTO_DENIED_DAILY_LIMIT\":\"1000\",\"PRE_APPROVED_WEEKLY_LIMIT\":\"0\",\"AUTO_DENIED_WEEKLY_LIMIT\":\"5000\"}}]}]},{\"featureName\":\"International Wire Transfer\",\"featureID\":\"INTERNATIONAL_WIRE_TRANSFER\",\"featureDescription\":\"International Wire Transfer\",\"Actions\":[{\"actionType\":\"MONETARY\",\"isEnabled\":\"true\",\"actionId\":\"INTERNATIONAL_WIRE_TRANSFER_CREATE\",\"actionDescription\":\"International Wire Transfers\",\"actionName\":\"International Wire Transfers \",\"isAccountLevel\":\"true\",\"Accounts\":[{\"id\":\"200402063722030\",\"isEnabled\":\"true\",\"limits\":{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"0\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"500\",\"PRE_APPROVED_DAILY_LIMIT\":\"0\",\"AUTO_DENIED_DAILY_LIMIT\":\"1000\",\"PRE_APPROVED_WEEKLY_LIMIT\":\"0\",\"AUTO_DENIED_WEEKLY_LIMIT\":\"5000\"}},{\"id\":\"200402063722372\",\"isEnabled\":\"true\",\"limits\":{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"0\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"500\",\"PRE_APPROVED_DAILY_LIMIT\":\"0\",\"AUTO_DENIED_DAILY_LIMIT\":\"1000\",\"PRE_APPROVED_WEEKLY_LIMIT\":\"0\",\"AUTO_DENIED_WEEKLY_LIMIT\":\"5000\"}},{\"id\":\"200402063722728\",\"isEnabled\":\"true\",\"limits\":{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"0\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"500\",\"PRE_APPROVED_DAILY_LIMIT\":\"0\",\"AUTO_DENIED_DAILY_LIMIT\":\"1000\",\"PRE_APPROVED_WEEKLY_LIMIT\":\"0\",\"AUTO_DENIED_WEEKLY_LIMIT\":\"5000\"}},{\"id\":\"200402063723321\",\"isEnabled\":\"true\",\"limits\":{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"0\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"500\",\"PRE_APPROVED_DAILY_LIMIT\":\"0\",\"AUTO_DENIED_DAILY_LIMIT\":\"1000\",\"PRE_APPROVED_WEEKLY_LIMIT\":\"0\",\"AUTO_DENIED_WEEKLY_LIMIT\":\"5000\"}},{\"id\":\"200402063724099\",\"isEnabled\":\"true\",\"limits\":{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"0\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"500\",\"PRE_APPROVED_DAILY_LIMIT\":\"0\",\"AUTO_DENIED_DAILY_LIMIT\":\"1000\",\"PRE_APPROVED_WEEKLY_LIMIT\":\"0\",\"AUTO_DENIED_WEEKLY_LIMIT\":\"5000\"}}]}]},{\"featureName\":\"Interbank Account to Account Fund Transfer\",\"featureID\":\"INTER_BANK_ACCOUNT_FUND_TRANSFER\",\"featureDescription\":\"Fund Transfer to Accounts in other domestic Banks and Credit Unions\",\"Actions\":[{\"actionType\":\"MONETARY\",\"isEnabled\":\"true\",\"actionId\":\"INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE\",\"actionDescription\":\"Fund Transfer to Accounts in other domestic Banks and Credit Unions\",\"actionName\":\"Fund Transfer to Accounts in other domestic Banks and Credit Unions\",\"isAccountLevel\":\"true\",\"Accounts\":[{\"id\":\"200402063722030\",\"isEnabled\":\"true\",\"limits\":{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"0\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"500\",\"PRE_APPROVED_DAILY_LIMIT\":\"0\",\"AUTO_DENIED_DAILY_LIMIT\":\"1000\",\"PRE_APPROVED_WEEKLY_LIMIT\":\"0\",\"AUTO_DENIED_WEEKLY_LIMIT\":\"5000\"}},{\"id\":\"200402063722372\",\"isEnabled\":\"true\",\"limits\":{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"0\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"500\",\"PRE_APPROVED_DAILY_LIMIT\":\"0\",\"AUTO_DENIED_DAILY_LIMIT\":\"1000\",\"PRE_APPROVED_WEEKLY_LIMIT\":\"0\",\"AUTO_DENIED_WEEKLY_LIMIT\":\"5000\"}},{\"id\":\"200402063722728\",\"isEnabled\":\"true\",\"limits\":{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"0\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"500\",\"PRE_APPROVED_DAILY_LIMIT\":\"0\",\"AUTO_DENIED_DAILY_LIMIT\":\"1000\",\"PRE_APPROVED_WEEKLY_LIMIT\":\"0\",\"AUTO_DENIED_WEEKLY_LIMIT\":\"5000\"}},{\"id\":\"200402063723321\",\"isEnabled\":\"true\",\"limits\":{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"0\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"500\",\"PRE_APPROVED_DAILY_LIMIT\":\"0\",\"AUTO_DENIED_DAILY_LIMIT\":\"1000\",\"PRE_APPROVED_WEEKLY_LIMIT\":\"0\",\"AUTO_DENIED_WEEKLY_LIMIT\":\"5000\"}},{\"id\":\"200402063724099\",\"isEnabled\":\"true\",\"limits\":{\"PRE_APPROVED_TRANSACTION_LIMIT\":\"0\",\"AUTO_DENIED_TRANSACTION_LIMIT\":\"500\",\"PRE_APPROVED_DAILY_LIMIT\":\"0\",\"AUTO_DENIED_DAILY_LIMIT\":\"1000\",\"PRE_APPROVED_WEEKLY_LIMIT\":\"0\",\"AUTO_DENIED_WEEKLY_LIMIT\":\"5000\"}}]}]}]";

        features = features.replace("200402063722030", helper.getAccountNum("Rewards Savings"));
        features = features.replace("200402063722372", helper.getAccountNum("Core Checking"));
        features = features.replace("200402063722728", helper.getAccountNum("Freedom Credit Card"));
        features = features.replace("200402063723321", helper.getAccountNum("12 Months Term Deposit"));
        features = features.replace("200402063724099", helper.getAccountNum("Turbo Auto Loan"));

        featuresJSON = new JSONArray(features);
        try {

            LimitsHandler.modifyCustomerActionLimits(null, null, helper.getMBOrgId(), "GROUP_MICRO_ADMINISTRATOR",
                    actions,
                    accounts, featuresJSON, userId, dcRequest, result);
        } catch (Exception e) {
            alert.prepareError("Exception occured when creating customer action limits").log();
        }
    }

    public String getAccessToken(DataControllerRequest dcRequest) throws HttpCallException {
        return AdminUtil.getAdminToken(dcRequest);
    }

    private void createOrganisationAccounts(DataControllerRequest dcRequest, String mbId, String orgid,
            DemoDataMB helper, Map<String, String> input2) throws HttpCallException {
        String accountNum = null;
        Calendar.getInstance();
        Map<String, String> input = helper.getMBAccount1DemoData();
        accountNum = helper.getAccountNum(input.get("Name"));

        input = new HashMap<>();
        input.put("id", HelperMethods.getNumericId() + "");
        input.put("Membership_id", input2.get("Membership_id"));
        input.put("Account_id", accountNum);
        input.put("accountName", "Rewards Savings");
        input.put("Taxid", input2.get("Taxid"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.MEMBERSHIP_ACCOUNTS_CREATE);

        input = new HashMap<>();
        input.put("id", HelperMethods.getNumericId() + "");
        input.put("Account_id", accountNum);
        input.put("AccountName", "Rewards Savings");
        input.put("IsOrganizationAccount", "true");
        input.put("IsOrgAccountUnLinked", "false");
        input.put("Organization_id", orgid);

        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERACCOUNTS_CREATE);

        input = new HashMap<>();
        input.put("id", HelperMethods.getNumericId() + "");
        input.put("Customer_id", mbId);
        input.put("Account_id", accountNum);
        input.put("AccountName", "Rewards Savings");
        input.put("IsOrganizationAccount", "false");
        input.put("IsOrgAccountUnLinked", "false");

        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERACCOUNTS_CREATE);

        input = helper.getMBAccount2DemoData();
        accountNum = helper.getAccountNum(input.get("Name"));

        input = new HashMap<>();
        input.put("id", HelperMethods.getNumericId() + "");
        input.put("Membership_id", input2.get("Membership_id"));
        input.put("Account_id", accountNum);
        input.put("accountName", "Core Checking");
        input.put("Taxid", input2.get("Taxid"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.MEMBERSHIP_ACCOUNTS_CREATE);

        input = new HashMap<>();
        input.put("id", HelperMethods.getNumericId() + "");
        input.put("Account_id", accountNum);
        input.put("AccountName", "Core Checking");
        input.put("IsOrganizationAccount", "true");
        input.put("IsOrgAccountUnLinked", "false");
        input.put("Organization_id", orgid);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERACCOUNTS_CREATE);

        input = new HashMap<>();
        input.put("id", HelperMethods.getNumericId() + "");
        input.put("Customer_id", mbId);
        input.put("Account_id", accountNum);
        input.put("AccountName", "Core Checking");
        input.put("IsOrganizationAccount", "false");
        input.put("IsOrgAccountUnLinked", "false");

        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERACCOUNTS_CREATE);

        input = helper.getMBAccount3DemoData();
        accountNum = helper.getAccountNum(input.get("Name"));

        input = new HashMap<>();
        input.put("id", HelperMethods.getNumericId() + "");
        input.put("Membership_id", input2.get("Membership_id"));
        input.put("Account_id", accountNum);
        input.put("accountName", "Freedom Credit Card");
        input.put("Taxid", input2.get("Taxid"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.MEMBERSHIP_ACCOUNTS_CREATE);

        input = new HashMap<>();
        input.put("id", HelperMethods.getNumericId() + "");
        input.put("Account_id", accountNum);
        input.put("AccountName", "Freedom Credit Card");
        input.put("IsOrganizationAccount", "true");
        input.put("IsOrgAccountUnLinked", "false");
        input.put("Organization_id", orgid);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERACCOUNTS_CREATE);

        input = new HashMap<>();
        input.put("id", HelperMethods.getNumericId() + "");
        input.put("Customer_id", mbId);
        input.put("Account_id", accountNum);
        input.put("AccountName", "Freedom Credit Card");
        input.put("IsOrganizationAccount", "false");
        input.put("IsOrgAccountUnLinked", "false");

        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERACCOUNTS_CREATE);

        input = helper.getMBAccount4DemoData();
        accountNum = helper.getAccountNum(input.get("Name"));

        input = new HashMap<>();
        input.put("id", HelperMethods.getNumericId() + "");
        input.put("Membership_id", input2.get("Membership_id"));
        input.put("Account_id", accountNum);
        input.put("accountName", "12 Months Term Deposit");
        input.put("Taxid", input2.get("Taxid"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.MEMBERSHIP_ACCOUNTS_CREATE);

        input = new HashMap<>();
        input.put("id", HelperMethods.getNumericId() + "");
        input.put("Account_id", accountNum);
        input.put("AccountName", "12 Months Term Deposit");
        input.put("IsOrganizationAccount", "true");
        input.put("IsOrgAccountUnLinked", "false");
        input.put("Organization_id", orgid);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERACCOUNTS_CREATE);

        input = new HashMap<>();
        input.put("id", HelperMethods.getNumericId() + "");
        input.put("Customer_id", mbId);
        input.put("Account_id", accountNum);
        input.put("AccountName", "Freedom Credit Card");
        input.put("IsOrganizationAccount", "false");
        input.put("IsOrgAccountUnLinked", "false");

        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERACCOUNTS_CREATE);

        input = helper.getMBAccount5DemoData();
        accountNum = helper.getAccountNum(input.get("Name"));

        input = new HashMap<>();
        input.put("id", HelperMethods.getNumericId() + "");
        input.put("Membership_id", input2.get("Membership_id"));
        input.put("Account_id", accountNum);
        input.put("accountName", "Turbo Auto Loan");
        input.put("Taxid", input2.get("Taxid"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.MEMBERSHIP_ACCOUNTS_CREATE);

        input = new HashMap<>();
        input.put("id", HelperMethods.getNumericId() + "");
        input.put("Account_id", accountNum);
        input.put("AccountName", "Turbo Auto Loan");
        input.put("IsOrganizationAccount", "true");
        input.put("IsOrgAccountUnLinked", "false");
        input.put("Organization_id", orgid);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERACCOUNTS_CREATE);

        input = new HashMap<>();
        input.put("id", HelperMethods.getNumericId() + "");
        input.put("Customer_id", mbId);
        input.put("Account_id", accountNum);
        input.put("AccountName", "Turbo Auto Loan");
        input.put("IsOrganizationAccount", "false");
        input.put("IsOrgAccountUnLinked", "false");

        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERACCOUNTS_CREATE);
    }

    private static Map<String, String> getCommunicationInformation(Map<String, String> inputParams) {

        Map<String, String> map = new HashMap<>();
        map.put("Phone", inputParams.get("Phone"));
        map.put("Email", inputParams.get("Email"));
        return map;
    }

    private void createUserBill(DataControllerRequest dcRequest, DemoDataMB helper) throws HttpCallException {
        Map<String, String> input = helper.getUserBill1DemoData();
        input.put("Payee_id", helper.getPayee("payee4"));
        input.put("Account_id", helper.getAccountNum("Core Checking"));
        input.put("billerMaster_id", "4");
        Result bill = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_BILL_CREATE);
        helper.addBill("bill1", HelperMethods.getFieldValue(bill, "id"));

        input = helper.getUserBill2DemoData();
        input.put("Payee_id", helper.getPayee("payee1"));
        input.put("Account_id", helper.getAccountNum("Core Checking"));
        input.put("billerMaster_id", "4");
        bill = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_BILL_CREATE);
        helper.addBill("bill2", HelperMethods.getFieldValue(bill, "id"));
    }

    private void createTransactionsForSavingsAccount(DataControllerRequest dcRequest, DemoDataMB helper)
            throws HttpCallException {
        new DemoDataMB();
        Map<String, String> input = helper.getCheckDeposit_MBSAV2DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getCardlessWithdrawl_MBSAVDemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getExternalTransfer_MBSAV3DemoData();
        input.put("toExternalAccountNumber", helper.getExternalAccountNum(input.get("beneficiaryName")));
        insertTransaction(input, dcRequest, helper);

        input = helper.getRemoteCheckDeposit_MBSAV4DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getInterest_MBSAV6DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getRemoteCheckDeposit_MBSAV7DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getCredit_MBSAV8DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getFee_MBSAV9DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getExternalTransfer_MBSAV11DemoData();
        input.put("toExternalAccountNumber", helper.getExternalAccountNum(input.get("beneficiaryName")));
        insertTransaction(input, dcRequest, helper);

        input = helper.getLoan_MBSAV12DemoData();
        insertTransaction(input, dcRequest, helper);
    }

    private void createTransactionsForDepositAccount(DataControllerRequest dcRequest, DemoDataMB helper)
            throws HttpCallException {

        DemoDataMB mbhelper = new DemoDataMB();
        Map<String, String> input = mbhelper.getInterestCredit_MBDeposit1DemoData();
        insertTransaction(input, dcRequest, helper);

        input = mbhelper.getInterestCredit_MBDeposit2DemoData();
        insertTransaction(input, dcRequest, helper);

        input = mbhelper.getInterestCredit_MBDeposit3DemoData();
        insertTransaction(input, dcRequest, helper);

        input = mbhelper.getDeposit_MBDeposit4DemoData();
        insertTransaction(input, dcRequest, helper);
    }

    private void createTransactionsForCheckingAccount(DataControllerRequest dcRequest, DemoDataMB helper)
            throws HttpCallException {

        List<Map<String, String>> dataList = getCheckingAccountData(helper);
        for (Map<String, String> data : dataList) {
            insertTransaction(data, dcRequest, helper);
        }
    }

    private void createTransactionsForCreditCardAccount(DataControllerRequest dcRequest, DemoDataMB helper)
            throws HttpCallException {

        List<Map<String, String>> dataList = getCreditAccountData(helper);
        for (Map<String, String> data : dataList) {
            insertTransaction(data, dcRequest, helper);
        }
    }

    private void insertTransaction(Map<String, String> input, DataControllerRequest dcRequest, DemoDataMB helper)
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

    private void createTransactionsForLoanAccount(DataControllerRequest dcRequest, DemoDataMB helper)
            throws HttpCallException {

        DemoDataMB mbhelper = new DemoDataMB();

        Map<String, String> input = mbhelper.getLoanPayment_MBLoan1DemoData();
        insertTransaction(input, dcRequest, helper);

        input = mbhelper.getLoanPayment_MBLoan2DemoData();
        insertTransaction(input, dcRequest, helper);

        input = mbhelper.getLoanPayment_MBLoan3DemoData();
        insertTransaction(input, dcRequest, helper);

        input = mbhelper.getTax_MBLoan4DemoData();
        insertTransaction(input, dcRequest, helper);

        input = mbhelper.getLoanPayment_MBLoan5DemoData();
        insertTransaction(input, dcRequest, helper);

        input = mbhelper.getLoanPayment_MBLoan6DemoData();
        insertTransaction(input, dcRequest, helper);

        input = mbhelper.getLoanPayment_MBLoan7DemoData();
        insertTransaction(input, dcRequest, helper);

        input = mbhelper.getLoanPayment_MBLoan8DemoData();
        insertTransaction(input, dcRequest, helper);

        input = mbhelper.getLoanPayment_MBLoan9DemoData();
        insertTransaction(input, dcRequest, helper);

        input = mbhelper.getLoanPayment_MBLoan10DemoData();
        insertTransaction(input, dcRequest, helper);

        input = mbhelper.getLoanPayment_MBLoan11DemoData();
        insertTransaction(input, dcRequest, helper);

        input = mbhelper.getCredit_MBLoan12DemoData();
        insertTransaction(input, dcRequest, helper);
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

    private void createPayees(DataControllerRequest dcRequest, String userId, DemoDataMB helper)
            throws HttpCallException {

        Result rs = null;
        Map<String, String> input = helper.getPayee1DemoData();
        input.put("User_Id", userId);
        input.put("billermaster_id", "6");
        input.put("transitDays", "3");
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_PAYEE_CREATE);
        helper.addPayee("payee1", getFieldValue(rs, "Id"));

        input = helper.getPayee2DemoData();
        input.put("User_Id", userId);
        input.put("billermaster_id", "1");
        input.put("transitDays", "3");
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_PAYEE_CREATE);
        helper.addPayee("payee2", getFieldValue(rs, "Id"));

        input = helper.getPayee3DemoData();
        input.put("User_Id", userId);
        input.put("billermaster_id", "1");
        input.put("transitDays", "3");
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_PAYEE_CREATE);
        helper.addPayee("payee3", getFieldValue(rs, "Id"));

        input = helper.getPayee4DemoData();
        input.put("User_Id", userId);
        input.put("billermaster_id", "4");
        input.put("transitDays", "3");
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_PAYEE_CREATE);
        helper.addPayee("payee4", getFieldValue(rs, "Id"));

        input = helper.getPayee5DemoData();
        input.put("User_Id", userId);
        input.put("billermaster_id", "8");
        input.put("transitDays", "3");
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_PAYEE_CREATE);
        helper.addPayee("payee5", getFieldValue(rs, "Id"));

        input = helper.getPayee6DemoData();
        input.put("User_Id", userId);
        input.put("billermaster_id", "7");
        input.put("transitDays", "3");
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_PAYEE_CREATE);
        helper.addPayee("payee6", getFieldValue(rs, "Id"));

    }

    private void createPayPersons(DataControllerRequest dcRequest, String userId, DemoDataMB helper)
            throws HttpCallException {

        Result rs = null;
        Map<String, String> input = helper.getPerson1DemoData();
        input.put("User_id", userId);
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_PAYPERSON_CREATE);
        helper.addPerson("person1", getFieldValue(rs, "id"));

        input = helper.getPerson2DemoData();
        input.put("User_id", userId);
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_PAYPERSON_CREATE);
        helper.addPerson("person2", getFieldValue(rs, "id"));

        input = helper.getPerson3DemoData();
        input.put("User_id", userId);
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_PAYPERSON_CREATE);
        helper.addPerson("person3", getFieldValue(rs, "id"));

        input = helper.getPerson4DemoData();
        input.put("User_id", userId);
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_PAYPERSON_CREATE);
        helper.addPerson("person4", getFieldValue(rs, "id"));
    }

    private void createExternalAccounts(DataControllerRequest dcRequest, String userId, DemoDataMB helper)
            throws HttpCallException {
        Map<String, String> input = helper.getExtAccount1DemoData();
        input.put("User_id", userId);
        helper.addExternalAccountNum(input.get("beneficiaryName"), input.get("accountNumber"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_EXT_ACCOUNTS_CREATE);

        input = helper.getExtAccount2DemoData();
        input.put("User_id", userId);
        helper.addExternalAccountNum(input.get("beneficiaryName"), input.get("accountNumber"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_EXT_ACCOUNTS_CREATE);

        input = helper.getExtAccount3DemoData();
        input.put("User_id", userId);
        helper.addExternalAccountNum(input.get("beneficiaryName"), input.get("accountNumber"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_EXT_ACCOUNTS_CREATE);

        input = helper.getExtAccount4DemoData();
        input.put("User_id", userId);
        helper.addExternalAccountNum(input.get("beneficiaryName"), input.get("accountNumber"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_EXT_ACCOUNTS_CREATE);

        input = helper.getExtAccount5DemoData();
        input.put("User_id", userId);
        helper.addExternalAccountNum(input.get("beneficiaryName"), input.get("accountNumber"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_EXT_ACCOUNTS_CREATE);
    }

    private void createCards(DataControllerRequest dcRequest, String userId, DemoDataMB helper)
            throws HttpCallException {

        Map<String, String> input = helper.getCard1DemoData();
        input.put("User_id", userId);
        input.put("account_id", helper.getAccountNum("Rewards Savings"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_CARDS_CREATE);

        input = helper.getCard2DemoData();
        input.put("User_id", userId);
        input.put("account_id", helper.getAccountNum("Core Checking"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_CARDS_CREATE);

        input = helper.getCard3DemoData();
        input.put("User_id", userId);
        input.put("account_id", helper.getAccountNum("Freedom Credit Card"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_CARDS_CREATE);

    }

    private void createUserAlerts(DataControllerRequest dcRequest, String userId, DemoDataMB helper)
            throws HttpCallException {

        Map<String, String> input = helper.geUserAlertDemoData();
        input.put("User_id", userId);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_ALERTS_CREATE);
    }

    private void createAccountAlerts(DataControllerRequest dcRequest, String userId, DemoDataMB helper)
            throws HttpCallException {

        Map<String, String> input = helper.geAccountAlert1DemoData();
        input.put("User_id", userId);
        input.put("AccountNumber", helper.getAccountNum("Rewards Savings"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_USER_ACC_ALERT_CREATE);

        input = helper.geAccountAlert2DemoData();
        input.put("User_id", userId);
        input.put("AccountNumber", helper.getAccountNum("Core Checking"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_USER_ACC_ALERT_CREATE);

        input = helper.geAccountAlert3DemoData();
        input.put("User_id", userId);
        input.put("AccountNumber", helper.getAccountNum("Freedom Credit Card"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_USER_ACC_ALERT_CREATE);

    }

    private void createPhone(DataControllerRequest dcRequest, String userId, DemoDataMB helper)
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

    private void updateDemoUserDefaultAccounts(DataControllerRequest dcRequest, String userId, DemoDataMB helper)
            throws HttpCallException {
        Map<String, String> input = new HashMap<>();
        input.put("Id", userId);
        input.put("default_account_cardless", helper.getAccountNum("Rewards Savings"));
        input.put("default_account_billPay", helper.getAccountNum("Rewards Savings"));
        input.put("default_to_account_p2p", helper.getAccountNum("Rewards Savings"));
        input.put("default_from_account_p2p", helper.getAccountNum("Rewards Savings"));
        input.put("default_account_deposit", helper.getAccountNum("Rewards Savings"));
        input.put("default_account_payments", helper.getAccountNum("Core Checking"));
        input.put("default_account_transfers", helper.getAccountNum("Core Checking"));
        input.put("default_account_wire", helper.getAccountNum("Core Checking"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.USER_UPDATE);
    }

    private void updateDemoCustomerDefaultAccounts(DataControllerRequest dcRequest, String userId, DemoDataMB helper)
            throws HttpCallException {
        Map<String, String> input = new HashMap<>();

        String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + userId;

        Result result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERPREFERENCE_GET);

        input.put("id", HelperMethods.getFieldValue(result, "id"));

        input.put("DefaultAccountCardless", helper.getAccountNum("Rewards Savings"));
        input.put("DefaultAccountBillPay", helper.getAccountNum("Rewards Savings"));
        input.put("DefaultToAccountP2P", helper.getAccountNum("Rewards Savings"));
        input.put("DefaultFromAccountP2P", helper.getAccountNum("Rewards Savings"));
        input.put("DefaultAccountDeposit", helper.getAccountNum("Rewards Savings"));
        input.put("DefaultAccountPayments", helper.getAccountNum("Core Checking"));
        input.put("DefaultAccountTransfers", helper.getAccountNum("Core Checking"));
        input.put("DefaultAccountWire", helper.getAccountNum("Core Checking"));

        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERPREFERENCE_UPDATE);
    }

    private void createMessages(DataControllerRequest dcRequest, DemoDataMB helper) throws HttpCallException {
        Map<String, String> input = new HashMap<>();
        input.put("Account_id", helper.getAccountNum("Rewards Savings"));
        input.put("Category_id", "1");
        input.put("Subcategory_id", "1");
        input.put("status", "Inbox");
        input.put("subject", "I want to open a new account");
        input.put("message",
                "Dear customer, We value your relationship. We understand that you want to nominate your wife as a beneficiary in your account. We request you to kindly download the form available on our website and send the details to our customer care department through email or fax. We will enroll her as a beneficiary and provide the confirmation.Thanks Customer Team");
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_MESSAGE_CREATE);

        input.put("Account_id", helper.getAccountNum("Core Checking"));
        input.put("Category_id", "1");
        input.put("Subcategory_id", "3");
        input.put("status", "Inbox");
        input.put("subject", "Errorneous debit entry is edited");
        input.put("message",
                "Dear customer, We have found an erroneous debit entry in your account. We have identified that it is because of some manual error while entering the data by one of our executives. We have rectified the issue and we appologize for the inconvenience caused to you.Thanks Customer Team");
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_MESSAGE_CREATE);

        input.put("Account_id", helper.getAccountNum("Freedom Credit Card"));
        input.put("Category_id", "3");
        input.put("Subcategory_id", "16");
        input.put("status", "Inbox");
        input.put("subject", "Concern of OTPs");
        input.put("message",
                "Dear Customer, Thank you for providing your valuable feedback to reduce the number of digits in OTP from 8 to 4 or 6. We have shared your feedback to the right team in our IT department. We will definitely consider your feedback. Looking forward to serve you.Thanks Customer Care team");
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_MESSAGE_CREATE);
    }

    private void createAccountStatements(DataControllerRequest dcRequest, DemoDataMB helper) throws HttpCallException {
        Calendar date1 = new GregorianCalendar(2017, Calendar.NOVEMBER, 1);
        Calendar date2 = new GregorianCalendar(2017, Calendar.DECEMBER, 1);

        Map<String, String> input = new HashMap<>();
        input.put("Account_id", helper.getAccountNum("Rewards Savings"));
        input.put("description", "Statement for November");
        input.put("statementLink", "http://pmqa.konylabs.net/KonyWebBanking/account-statement-pdf.pdf");
        input.put("month", getDateString(date1.getTime()));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_ACCOUNT_STATEMENT_CREATE);

        input.put("Account_id", helper.getAccountNum("Core Checking"));
        input.put("description", "Statement for December");
        input.put("statementLink", "http://pmqa.konylabs.net/KonyWebBanking/account-statement-pdf.pdf");
        input.put("month", getDateString(date2.getTime()));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_ACCOUNT_STATEMENT_CREATE);

        input.put("Account_id", helper.getAccountNum("Freedom Credit Card"));
        input.put("description", "Statement for November");
        input.put("statementLink", "http://pmqa.konylabs.net/KonyWebBanking/account-statement-pdf.pdf");
        input.put("month", getDateString(date1.getTime()));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_ACCOUNT_STATEMENT_CREATE);

        input.put("Account_id", helper.getAccountNum("12 Months Term Deposit"));
        input.put("description", "Statement for December");
        input.put("statementLink", "http://pmqa.konylabs.net/KonyWebBanking/account-statement-pdf.pdf");
        input.put("month", getDateString(date2.getTime()));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_ACCOUNT_STATEMENT_CREATE);

        input.put("Account_id", helper.getAccountNum("Turbo Auto Loan"));
        input.put("description", "Statement for November");
        input.put("statementLink", "http://pmqa.konylabs.net/KonyWebBanking/account-statement-pdf.pdf");
        input.put("month", getDateString(date1.getTime()));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_ACCOUNT_STATEMENT_CREATE);
    }

    private List<Map<String, String>> getCreditAccountData(DemoDataMB helper) {
        DemoDataMB mbhelper = new DemoDataMB();
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
        return Arrays.asList(mbhelper.getPOS_MBCC1DemoData(), mbhelper.getPOS_MBCC3DemoData(),
                mbhelper.getInternetTransaction_MBCC4DemoData(), mbhelper.getBillPay_MBCC5DemoData(),
                mbhelper.getPOS_MBCC6DemoData(), mbhelper.getCardPayment_MBCC7DemoData(),
                mbhelper.getPOS_MBCC8DemoData(), mbhelper.getPOS_MBCC9DemoData(), mbhelper.getPOS_MBCC10DemoData(),
                mbhelper.getInternetTransaction_MBCC11DemoData(), mbhelper.getInternetTransaction_MBCC12DemoData(),
                mbhelper.getPOS_MBCC13DemoData(), mbhelper.getCredit_MBCC14DemoData(),
                mbhelper.getBillPay_MBCC15DemoData(), mbhelper.getPOS_MBCC16DemoData(),
                mbhelper.getInternetTransaction_MBCC17DemoData(), mbhelper.getPOS_MBCC18DemoData(),
                mbhelper.getPOS_MBCC19DemoData(), mbhelper.getPOS_MBCC20DemoData(),
                mbhelper.getInternetTransaction_MBCC21DemoData(), mbhelper.getInternetTransaction_MBCC22DemoData(),
                mbhelper.getPOS_MBCC23DemoData(), mbhelper.getCredit_MBCC24DemoData(),
                mbhelper.getBillPay_MBCC25DemoData(), mbhelper.getPOS_MBCC26DemoData(),
                mbhelper.getInternetTransaction_MBCC27DemoData(), mbhelper.getPOS_MBCC28DemoData(),
                mbhelper.getPOS_MBCC29DemoData(), mbhelper.getPOS_MBCC30DemoData(), mbhelper.getPOS_MBCC31DemoData(),
                mbhelper.getInternetTransaction_MBCC32DemoData(), mbhelper.getPOS_MBCC33DemoData(),
                mbhelper.getPOS_MBCC34DemoData(), mbhelper.getPOS_MBCC35DemoData(),
                mbhelper.getInternetTransaction_MBCC36DemoData(), mbhelper.getInternetTransaction_MBCC37DemoData(),
                mbhelper.getPOS_MBCC38DemoData(), mbhelper.getCredit_MBCC39DemoData(),
                mbhelper.getBillPay_MBCC40DemoData(), mbhelper.getPOS_MBCC41DemoData(),
                mbhelper.getInternetTransaction_MBCC42DemoData(), mbhelper.getPOS_MBCC43DemoData(),
                mbhelper.getPOS_MBCC44DemoData(), mbhelper.getPOS_MBCC45DemoData(),
                mbhelper.getInternetTransaction_MBCC46DemoData(), mbhelper.getInternetTransaction_MBCC47DemoData(),
                mbhelper.getPOS_MBCC48DemoData(), mbhelper.getCredit_MBCC49DemoData(),
                mbhelper.getBillPay_MBCC50DemoData());
    }

    private List<Map<String, String>> getCheckingAccountData(DemoDataMB helper) {

        new DemoDataMB();

        /*
         * helper.getPOS_MBChecking51DemoData(), helper.getCredit_MBChecking52DemoData(),
         * helper.getBillPay_MBChecking53DemoData(), helper.getPOS_MBChecking54DemoData(),
         * helper.getInternetTransaction_MBChecking55DemoData(), helper.getPOS_MBChecking56DemoData(),
         * helper.getPOS_MBChecking57DemoData(), helper.getPOS_MBChecking58DemoData(),
         * helper.getInternetTransaction_MBChecking59DemoData(), helper.getInternetTransaction_MBChecking60DemoData(),
         * helper.getPOS_MBChecking61DemoData(), helper.getCredit_MBChecking62DemoData(),
         * helper.getBillPay_MBChecking63DemoData(), helper.getPOS_MBChecking64DemoData(),
         * helper.getInternetTransaction_MBChecking65DemoData(), helper.getCredit_MBChecking66DemoData(),
         * helper.getPOS_MBChecking67DemoData(), helper.getPOS_MBChecking68DemoData(),
         * helper.getPOS_MBChecking69DemoData(), helper.getCredit_MBChecking70DemoData(),
         * helper.getBillPay_MBChecking71DemoData(), helper.getPOS_MBChecking72DemoData(),
         * helper.getInternetTransaction_MBChecking73DemoData(), helper.getPOS_MBChecking74DemoData(),
         * helper.getPOS_MBChecking75DemoData(), helper.getPOS_MBChecking76DemoData(),
         * helper.getPOS_MBChecking77DemoData(), helper.getInternetTransaction_MBChecking78DemoData(),
         * helper.getPOS_MBChecking79DemoData(), helper.getPOS_MBChecking80DemoData(),
         * helper.getPOS_MBChecking81DemoData(), helper.getInternetTransaction_MBChecking82DemoData(),
         * helper.getInternetTransaction_MBChecking83DemoData(), helper.getPOS_MBChecking84DemoData(),
         * helper.getCredit_MBChecking85DemoData(), helper.getBillPay_MBChecking86DemoData(),
         * helper.getPOS_MBChecking87DemoData(), helper.getInternetTransaction_MBChecking88DemoData(),
         * helper.getPOS_MBChecking89DemoData(), helper.getPOS_MBChecking90DemoData(),
         * helper.getPOS_MBChecking91DemoData(), helper.getInternetTransaction_MBChecking92DemoData(),
         * helper.getInternetTransaction_MBChecking93DemoData(), helper.getPOS_MBChecking94DemoData(),
         * helper.getCredit_MBChecking95DemoData(), helper.getBillPay_MBChecking96DemoData(),
         * helper.getPOS_MBChecking97DemoData(), helper.getInternetTransaction_MBChecking98DemoData(),
         * helper.getPOS_MBChecking99DemoData(), helper.getPOS_MBChecking100DemoData(),
         * helper.getPOS_MBChecking101DemoData(), helper.getInternetTransaction_MBChecking102DemoData(),
         * helper.getInternetTransaction_MBChecking103DemoData(), helper.getPOS_MBChecking104DemoData(),
         * helper.getCredit_MBChecking105DemoData(), helper.getBillPay_MBChecking106DemoData(),
         * helper.getPOS_MBChecking107DemoData(), helper.getInternetTransaction_MBChecking108DemoData(),
         * helper.getPOS_MBChecking109DemoData(), helper.getPOS_MBChecking110DemoData(),
         * helper.getPOS_MBChecking111DemoData(), helper.getInternetTransaction_MBChecking112DemoData(),
         * helper.getInternetTransaction_MBChecking113DemoData(), helper.getPOS_MBChecking114DemoData(),
         * helper.getCredit_MBChecking115DemoData(), helper.getBillPay_MBChecking116DemoData(),
         * helper.getPOS_MBChecking117DemoData(), helper.getInternetTransaction_MBChecking118DemoData(),
         * helper.getPOS_MBChecking119DemoData(), helper.getCredit_MBChecking120DemoData(),
         * helper.getInternetTransaction_MBChecking121DemoData(), helper.getInternetTransaction_MBChecking122DemoData(),
         * helper.getPOS_MBChecking123DemoData(), helper.getCredit_MBChecking124DemoData(),
         * helper.getBillPay_MBChecking125DemoData(), helper.getPOS_MBChecking126DemoData(),
         * helper.getInternetTransaction_MBChecking127DemoData(), helper.getPOS_MBChecking128DemoData(),
         * helper.getPOS_MBChecking129DemoData(), helper.getPOS_MBChecking130DemoData(),
         * helper.getInternetTransaction_MBChecking131DemoData(), helper.getInternetTransaction_MBChecking132DemoData(),
         * helper.getPOS_MBChecking133DemoData(), helper.getCredit_MBChecking134DemoData(),
         * helper.getBillPay_MBChecking135DemoData(), helper.getPOS_MBChecking136DemoData(),
         * helper.getInternetTransaction_MBChecking137DemoData(), helper.getPOS_MBChecking138DemoData(),
         * helper.getPOS_MBChecking139DemoData(), helper.getPOS_MBChecking140DemoData(),
         * helper.getInternetTransaction_MBChecking141DemoData(), helper.getInternetTransaction_MBChecking142DemoData(),
         * helper.getPOS_MBChecking143DemoData(), helper.getCredit_MBChecking144DemoData(),
         * helper.getBillPay_MBChecking145DemoData(), helper.getPOS_MBChecking146DemoData(),
         * helper.getInternetTransaction_MBChecking147DemoData(), helper.getPOS_MBChecking148DemoData(),
         * helper.getPOS_MBChecking149DemoData(), helper.getPOS_MBChecking150DemoData(),
         * helper.getInternetTransaction_MBChecking151DemoData(), helper.getInternetTransaction_MBChecking152DemoData(),
         * helper.getPOS_MBChecking153DemoData(), helper.getCredit_MBChecking154DemoData(),
         * helper.getBillPay_MBChecking155DemoData(), helper.getPOS_MBChecking156DemoData(),
         * helper.getInternetTransaction_MBChecking157DemoData(), helper.getPOS_MBChecking158DemoData(),
         * helper.getPOS_MBChecking159DemoData(), helper.getPOS_MBChecking160DemoData(),
         * helper.getInternetTransaction_MBChecking161DemoData(), helper.getCredit_MBChecking162DemoData(),
         * helper.getInternetTransaction_MBChecking163DemoData(), helper.getPOS_MBChecking164DemoData(),
         * helper.getCredit_MBChecking165DemoData(), helper.getBillPay_MBChecking166DemoData(),
         * helper.getPOS_MBChecking167DemoData(), helper.getInternetTransaction_MBChecking168DemoData(),
         * helper.getPOS_MBChecking169DemoData(), helper.getPOS_MBChecking170DemoData(),
         * helper.getPOS_MBChecking171DemoData(), helper.getInternetTransaction_MBChecking172DemoData(),
         * helper.getInternetTransaction_MBChecking173DemoData(), helper.getPOS_MBChecking174DemoData(),
         * helper.getCredit_MBChecking175DemoData(), helper.getBillPay_MBChecking176DemoData(),
         * helper.getPOS_MBChecking177DemoData(), helper.getInternetTransaction_MBChecking178DemoData(),
         * helper.getPOS_MBChecking179DemoData(), helper.getPOS_MBChecking180DemoData(),
         * helper.getPOS_MBChecking181DemoData(), helper.getInternetTransaction_MBChecking182DemoData(),
         * helper.getPOS_MBChecking183DemoData(), helper.getPOS_MBChecking184DemoData(),
         * helper.getPOS_MBChecking185DemoData(), helper.getInternetTransaction_MBChecking186DemoData(),
         * helper.getInternetTransaction_MBChecking187DemoData(), helper.getPOS_MBChecking188DemoData(),
         * helper.getCredit_MBChecking189DemoData(), helper.getBillPay_MBChecking190DemoData(),
         * helper.getPOS_MBChecking191DemoData(), helper.getInternetTransaction_MBChecking192DemoData(),
         * helper.getPOS_MBChecking193DemoData(), helper.getPOS_MBChecking194DemoData(),
         * helper.getPOS_MBChecking195DemoData(), helper.getInternetTransaction_MBChecking196DemoData(),
         * helper.getInternetTransaction_MBChecking197DemoData(), helper.getCardPayment_MBChecking198DemoData(),
         * helper.getBillPay_MBChecking199DemoData(), helper.getCredit_MBChecking200DemoData(),
         * helper.getExternalTransfer_MBChecking201DemoData(), helper.getCredit_MBChecking202DemoData(),
         * helper.getPOS_MBChecking203DemoData(), helper.getCheckDeposit_MBChecking204DemoData(),
         * helper.getExternalTransfer_MBChecking205DemoData(), helper.getBillPay_MBChecking206DemoData(),
         * helper.getBillPay_MBChecking207DemoData(), helper.getInternalTransfer_MBChecking208DemoData(),
         * helper.getP2P_MBChecking209DemoData(), helper.getP2P_MBChecking210DemoData());
         */
        return (Arrays.asList(helper.getCredit_MBChecking1DemoData(), helper.getCardLessWithDrawl_MBChecking1DemoData(),
                helper.getCardLessWithDrawl_MBChecking2DemoData(),
                // helper.getInternalTransfer_MBChecking2DemoData(),
                // helper.getInternalTransfer_MBChecking3DemoData(),
                // helper.getInternalTransfer_MBChecking4DemoData(),
                helper.getCheckDeposit_MBChecking5DemoData(), helper.getP2P_MBChecking6DemoData(),
                helper.getInterest_MBChecking7DemoData(), helper.getWithdrawal_MBChecking8DemoData(),
                helper.getP2P_MBChecking9DemoData(), helper.getInternetTransaction_MBChecking10DemoData(),
                helper.getPOS_MBChecking11DemoData(), helper.getPOS_MBChecking12DemoData(),
                helper.getPOS_MBChecking13DemoData(), helper.getInternetTransaction_MBChecking14DemoData(),
                helper.getInternetTransaction_MBChecking15DemoData(), helper.getPOS_MBChecking16DemoData(),
                helper.getCredit_MBChecking17DemoData(), helper.getBillPay_MBChecking18DemoData(),
                helper.getPOS_MBChecking19DemoData(), helper.getInternetTransaction_MBChecking20DemoData(),
                helper.getPOS_MBChecking21DemoData(), helper.getPOS_MBChecking22DemoData(),
                helper.getPOS_MBChecking23DemoData(), helper.getPOS_MBChecking24DemoData(),
                helper.getInternetTransaction_MBChecking25DemoData(), helper.getPOS_MBChecking26DemoData(),
                helper.getPOS_MBChecking27DemoData(), helper.getPOS_MBChecking28DemoData(),
                helper.getInternetTransaction_MBChecking29DemoData(),
                helper.getInternetTransaction_MBChecking30DemoData(), helper.getPOS_MBChecking31DemoData(),
                helper.getCredit_MBChecking32DemoData(), helper.getBillPay_MBChecking33DemoData(),
                helper.getPOS_MBChecking34DemoData(), helper.getInternetTransaction_MBChecking35DemoData(),
                helper.getPOS_MBChecking36DemoData(), helper.getPOS_MBChecking37DemoData(),
                helper.getPOS_MBChecking38DemoData(), helper.getInternetTransaction_MBChecking39DemoData(),
                helper.getInternetTransaction_MBChecking40DemoData(), helper.getPOS_MBChecking41DemoData(),
                helper.getCredit_MBChecking42DemoData(), helper.getBillPay_MBChecking43DemoData(),
                helper.getPOS_MBChecking44DemoData(), helper.getInternetTransaction_MBChecking45DemoData(),
                helper.getPOS_MBChecking46DemoData(), helper.getPOS_MBChecking47DemoData(),
                helper.getPOS_MBChecking48DemoData(), helper.getInternetTransaction_MBChecking49DemoData(),
                helper.getInternetTransaction_MBChecking50DemoData()));
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

    private void createTransactionsForCard(DataControllerRequest dcRequest,
            DemoDataMB helper) throws HttpCallException {
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

}
