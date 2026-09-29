package com.kony.dbputilities.demoservices;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
//import org.omg.CORBA.portable.ApplicationException;

import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.customersecurityservices.CreateCustomerCommunication;
import com.kony.dbputilities.customersecurityservices.PasswordHistoryManagement;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.iban4j.CountryCode;
import com.kony.dbputilities.iban4j.Iban;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.MailHelper;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.dto.CustomerLegalEntityDTO;
import com.temenos.dbx.product.utils.DTOUtils;
import com.temenos.dbx.product.utils.ThreadExecutor;

public class DemoDataUKService implements JavaService2, DemoDataService {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
    	return ErrorCodeEnum.ERR_99999.setErrorCode(new Result());
    }

    private void doLoginAndSetToken(DataControllerRequest dcRequest, String userName, String password)
            throws HttpCallException {
        dcRequest.getHeaderMap().put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
    }

    private void createRecentCardlessWithdrawal(DataControllerRequest dcRequest, String userId, DemoDataUK helper)
            throws HttpCallException {
        Map<String, String> input = helper.getRecentCardLessWithdrawal1DemoData();
        Calendar cal = Calendar.getInstance();
        String today = getDateString(cal.getTime());
        String transactionDate = HelperMethods.getFormattedTimeStamp(cal.getTime(), FORMAT);
        input.put("Type_id", "6");
        input.put("fromAccountNumber", helper.getAccountNum("GB23 MIDL 0700 9312 3456 70"));
        input.put("createdDate", today);
        input.put("transactionDate", transactionDate);
        input.put("scheduledDate", today);
        addDateField(input, "cashlessOTPValidDate");
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_ACCOUNT_TRANSACTION_CREATE);

        input = helper.getRecentCardLessWithdrawal2DemoData();
        input.put("Type_id", "6");
        input.put("fromAccountNumber", helper.getAccountNum("GB65 MIDL 0700 9312 3456 90"));
        input.put("createdDate", today);
        input.put("transactionDate", transactionDate);
        input.put("scheduledDate", today);
        addDateField(input, "cashlessOTPValidDate");
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_ACCOUNT_TRANSACTION_CREATE);

        input = helper.getRecentCardLessWithdrawal3DemoData();
        input.put("Type_id", "6");
        input.put("fromAccountNumber", helper.getAccountNum("GB23 MIDL 0700 9312 3456 70"));
        input.put("createdDate", today);
        input.put("transactionDate", transactionDate);
        input.put("scheduledDate", today);
        addDateField(input, "cashlessOTPValidDate");
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_ACCOUNT_TRANSACTION_CREATE);
    }

    private void createUserBill(DataControllerRequest dcRequest, String userId, DemoDataUK helper)
            throws HttpCallException {
        Map<String, String> input = helper.getUserBill1DemoData();
        input.put("Payee_id", helper.getPayee("payee4"));
        input.put("Account_id", helper.getAccountNum("GB23 MIDL 0700 9312 3456 70"));
        input.put("billerMaster_id", "4");
        Result bill = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_BILL_CREATE);
        helper.addBill("bill1", HelperMethods.getFieldValue(bill, "id"));

        input = helper.getUserBill2DemoData();
        input.put("Payee_id", helper.getPayee("payee1"));
        input.put("Account_id", helper.getAccountNum("GB65 MIDL 0700 9312 3456 90"));
        input.put("billerMaster_id", "4");
        bill = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_BILL_CREATE);
        helper.addBill("bill2", HelperMethods.getFieldValue(bill, "id"));
    }

    private void createTransactionsForSavingsAccount(DataControllerRequest dcRequest, String userId, DemoDataUK helper)
            throws HttpCallException {

        Map<String, String> input = helper.getInternalTransfer_SAV4DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getInterest_SAV8DemoData();
        insertTransaction(input, dcRequest, helper);

    }

    private void createTransactionsForCurrentAccount(DataControllerRequest dcRequest, String userId, DemoDataUK helper)
            throws HttpCallException {

        List<Map<String, String>> dataList = getCurrentAccountData(helper);
        for (Map<String, String> data : dataList) {
            insertTransaction(data, dcRequest, helper);
        }
    }

    private void insertTransaction(Map<String, String> input, DataControllerRequest dcRequest, DemoDataUK helper)
            throws HttpCallException {
        input.put("toAccountNumber", helper.getAccountNum(input.get("toAccountNumber")));
        input.put("fromAccountNumber", helper.getAccountNum(input.get("fromAccountNumber")));
        String accNumber = input.get("toExternalAccountNumber");
        accNumber = accNumber.replaceAll("\\s+", "");
        input.put("IBAN", accNumber);
        input.remove("toExternalAccountNumber");
        input.put("Type_id", helper.getTransactionId(input.get("transactionType")));
        input.put("Payee_id", helper.getPayee(input.get("Payee_id")));
        input.put("Person_Id", helper.getPerson(input.get("Person_Id")));
        input.put("Bill_id", helper.getBill(input.get("Bill_id")));
        addDateField(input, "createdDate");
        addDateFieldwithoutTime(input, "transactionDate");
        addDateField(input, "scheduledDate");
        addDateField(input, "frequencyStartDate");
        addDateField(input, "frequencyEndDate");
        HelperMethods.removeNullValues(input);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_ACCOUNT_TRANSACTION_CREATE);
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

    @SuppressWarnings({ "static-access" })
    private String createCustomer(DataControllerRequest dcRequest, Map<String, String> inputmap, DemoDataUK helper)
            throws HttpCallException {
        Calendar usercal = Calendar.getInstance();
        String newUserName = inputmap.get("newUsername");
        String newPassword = inputmap.get("Password");
        String newEmail = inputmap.get("email");
        String phoneNumber = inputmap.get("phoneNumber");
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

        input.put("UserName", newUserName);
        input.put("Password", newPassword);
        input.put("Status_id", "SID_CUS_ACTIVE");

        input.put("Bank_id", "1");

        input.put("isEnrolled", "true");
        input.put("CountryCode", inputmap.get("countryCode"));
        input.put("ValidDate", getDateString(usercal.getTime()));
        input.put("Lastlogintime", HelperMethods.getFormattedTimeStamp(new Date(), null));
        input.put("CurrentLoginTime", HelperMethods.getFormattedTimeStamp(new Date(), null));
        if (inputmap.get("firstName") != null) {
            input.put("FirstName", inputmap.get("firstName"));
        }

        if (inputmap.get("lastName") != null) {
            input.put("LastName", inputmap.get("lastName"));
        }

        Result result = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.USER_CREATE);
        CustomerLegalEntityDTO customerLegalEntityDTO = new CustomerLegalEntityDTO();
		customerLegalEntityDTO.setId(HelperMethods.getNewId());
		customerLegalEntityDTO.setCustomer_id(id);
		customerLegalEntityDTO.setNew(true);
		customerLegalEntityDTO.setStatus_id(HelperMethods.getCustomerStatus().get("ACTIVE"));
		customerLegalEntityDTO.setLegalEntityId("GB0010001");
		customerLegalEntityDTO.persist(DTOUtils.getParameterMap(customerLegalEntityDTO, true), dcRequest.getHeaderMap());
		
        if (StringUtils.isBlank(HelperMethods.getFieldValue(result, "id"))) {
            return null;
        }
        PasswordHistoryManagement pm = new PasswordHistoryManagement(dcRequest, false);
        pm.makePasswordEntry(dcRequest, id, newPassword);

        input = new HashMap<>();
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

        input = new HashMap<>();
        input = helper.getCustomerPreferenceDemoData();
        input.put("id", String.valueOf(HelperMethods.getNumericId()));
        input.put("Customer_id", id);

        result = new Result();
        result = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERPREFERENCE_CREATE);

        // CreateUserAdminConsoleService adminService = new CreateUserAdminConsoleService();
        // adminService.createNewUserAtAdminConsole(newUserName, URLConstants.EURO_USER_GROUP, dcRequest);

        try {
            ThreadExecutor.getExecutor(dcRequest).execute(new Callable<Result>() {
                @Override
                public Result call() throws ApplicationException {
                    Map<String, String> postParamMapGroup = new HashMap<>();
                    postParamMapGroup.put("Customer_id", id);
                    postParamMapGroup.put("Group_id", "DEFAULT_GROUP");
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

        return id;
    }
    /*
     * private String getClaimsToken(String userName, String password,DataControllerRequest dcRequest) throws
     * HttpCallException { Map<String, String> headers = new HashMap<String, String>(); Map<String, String> input = new
     * HashMap<String, String>(); headers.put("X-Kony-App-Secret",
     * URLFinder.getPathUrl(URLConstants.DBP_SECRET,dcRequest)); headers.put("X-Kony-App-Key",
     * URLFinder.getPathUrl(URLConstants.DBP_KEY,dcRequest)); headers.put(HttpHeaders.CONTENT_TYPE,
     * "application/x-www-form-urlencoded"); input.put("username", userName); input.put("password", password);
     * HttpConnector httpClient = new HttpConnector(); JsonObject response =
     * httpClient.invokeHttpPost(URLFinder.getPathUrl(URLConstants.CUSTOM_LOGIN, dcRequest), input, headers); if(null !=
     * response.get("claims_token")){ return response.getAsJsonObject("claims_token").get("value").getAsString(); }
     * return ""; }
     */

    private void createUserSecurityQuestions(DataControllerRequest dcRequest, String userId, DemoDataUK helper)
            throws HttpCallException {
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

    private void createAccounts(DataControllerRequest dcRequest, String userId, String userName, DemoDataUK helper)
            throws HttpCallException {
        String accountNum = null;
        Calendar cal = Calendar.getInstance();
        Map<String, String> input = helper.getAccount1DemoData();
        accountNum = getDummyAccountId();
        Iban iban = new Iban.Builder().countryCode(CountryCode.DE).bankCode(accountNum.substring(0, 9))
                .accountNumber(accountNum.substring(9, accountNum.length())).build();
        helper.addAccountNum(input.get("IBAN"), accountNum);
        cal.add(Calendar.DATE, -1);
        input.put("IBAN", iban.toString().replace("DE", "UK"));
        input.put("Account_id", accountNum);
        input.put("User_id", userId);
        input.put("Type_id", helper.getAccountId(input.get("Type_id")));
        input.put("Product_id", "8");
        input.put("Bank_id", "1");
        input.put("LastPaymentDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        String accHolderName = "{\"username\": \"" + userName + "\", \"fullname\": \"" + input.get("AccountHolder")
                + "\"}";
        input.put("AccountHolder", accHolderName);
        String jointHolderNames = null;
        String accHolder2 = input.get("AccountHolder2");
        jointHolderNames = "[" + "{\"username\": \"" + "john.joe" + "\", \"fullname\": \"" + accHolder2 + "\"}" + "]";
        input.put("JointHolders", jointHolderNames);

        HelperMethods.removeNullValues(input);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_ACCOUNTS_CREATE);

        input = helper.getAccount2DemoData();
        accountNum = getDummyAccountId();
        iban = new Iban.Builder().countryCode(CountryCode.DE).bankCode(accountNum.substring(0, 9))
                .accountNumber(accountNum.substring(9, accountNum.length())).build();
        helper.addAccountNum(input.get("IBAN"), accountNum);
        cal.add(Calendar.DATE, -1);
        input.put("IBAN", iban.toString().replace("DE", "UK"));
        input.put("Account_id", accountNum);
        input.put("User_id", userId);
        input.put("Type_id", helper.getAccountId(input.get("Type_id")));
        input.put("Product_id", "11");
        input.put("Bank_id", "1");
        input.put("LastPaymentDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        input.put("LastPaymentDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        accHolderName = "{\"username\": \"" + userName + "\", \"fullname\": \"" + input.get("AccountHolder") + "\"}";
        input.put("AccountHolder", accHolderName);
        accHolder2 = input.get("AccountHolder2");
        jointHolderNames = "[" + "{\"username\": \"" + "john.joe" + "\", \"fullname\": \"" + accHolder2 + "\"}" + "]";
        input.put("JointHolders", jointHolderNames);

        HelperMethods.removeNullValues(input);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_ACCOUNTS_CREATE);
    }

    private void createMembership1(DataControllerRequest dcRequest, String userId, String userName, DemoDataUK helper)
            throws HttpCallException {

        Map<String, String> input = new HashMap<>();
        input = helper.getMembershipData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.MEMBERSHIP_CREATE);

        String membershipId = input.get("id");

        input = new HashMap<>();
        String accountNum = null;
        Calendar cal = Calendar.getInstance();
        input = helper.getAccount1DemoData();
        accountNum = getDummyAccountId();
        Iban iban = new Iban.Builder().countryCode(CountryCode.DE).bankCode(accountNum.substring(0, 9))
                .accountNumber(accountNum.substring(9, accountNum.length())).build();
        helper.addAccountNum(input.get("IBAN"), accountNum);
        cal.add(Calendar.DATE, -1);
        input.put("IBAN", iban.toString().replace("DE", "UK"));
        input.put("Account_id", accountNum);
        input.put("Type_id", helper.getAccountId(input.get("Type_id")));
        input.put("Product_id", "8");
        input.put("Bank_id", "1");
        input.put("LastPaymentDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        String accHolderName = "{\"username\": \"" + userName + "\", \"fullname\": \"" + input.get("AccountHolder")
                + "\"}";
        input.put("AccountHolder", accHolderName);
        String jointHolderNames = null;
        String accHolder2 = input.get("AccountHolder2");
        jointHolderNames = "[" + accHolderName + "," + "{\"username\": \"" + "john.joe" + "\", \"fullname\": \""
                + accHolder2 + "\"}" + "]";
        input.put("JointHolders", jointHolderNames);

        HelperMethods.removeNullValues(input);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_ACCOUNTS_CREATE);

        String accountId = input.get("Account_id");

        input = new HashMap<>();
        input.put("id", HelperMethods.getNumericId() + "");
        input.put("Membership_id", membershipId);
        input.put("Account_id", accountId);
        input.put("accountName", "Savings account");
        input.put("Taxid", HelperMethods.getTaxid() + "");
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.MEMBERSHIP_ACCOUNTS_CREATE);

        input = new HashMap<>();
        input.put("Account_id", accountNum);
        input.put("TaxId", HelperMethods.getTaxid() + "");
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.ACCOUNTS_UPDATE);
        return;
    }

    private void createMembership2(DataControllerRequest dcRequest, String userId, String userName, DemoDataUK helper)
            throws HttpCallException {

        Map<String, String> input = new HashMap<>();
        input = helper.getMembershipData();
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.MEMBERSHIP_CREATE);

        String membershipId = input.get("id");

        input = new HashMap<>();
        String accountNum = null;
        Calendar cal = Calendar.getInstance();
        input = helper.getAccount2DemoData();
        accountNum = getDummyAccountId();
        Iban iban = new Iban.Builder().countryCode(CountryCode.DE).bankCode(accountNum.substring(0, 9))
                .accountNumber(accountNum.substring(9, accountNum.length())).build();
        helper.addAccountNum(input.get("IBAN"), accountNum);
        cal.add(Calendar.DATE, -1);
        input.put("IBAN", iban.toString().replace("DE", "UK"));
        input.put("Account_id", accountNum);
        input.put("Type_id", helper.getAccountId(input.get("Type_id")));
        input.put("Product_id", "11");
        input.put("Bank_id", "1");
        input.put("LastPaymentDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        input.put("LastPaymentDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        String accHolderName = "{\"username\": \"" + userName + "\", \"fullname\": \"" + input.get("AccountHolder")
                + "\"}";
        input.put("AccountHolder", accHolderName);
        String accHolder2 = input.get("AccountHolder2");
        String jointHolderNames = "[" + accHolderName + "," + "{\"username\": \"" + "john.joe" + "\", \"fullname\": \""
                + accHolder2 + "\"}" + "]";
        input.put("JointHolders", jointHolderNames);

        HelperMethods.removeNullValues(input);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_ACCOUNTS_CREATE);

        String accountId = input.get("Account_id");

        input = new HashMap<>();
        input.put("id", HelperMethods.getNumericId() + "");
        input.put("Membership_id", membershipId);
        input.put("Account_id", accountId);
        input.put("accountName", "Current account");
        input.put("Taxid", HelperMethods.getTaxid() + "");
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.MEMBERSHIP_ACCOUNTS_CREATE);

        input = new HashMap<>();
        input.put("Account_id", accountNum);
        input.put("TaxId", HelperMethods.getTaxid() + "");
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.ACCOUNTS_UPDATE);
        return;
    }

    private void createPayees(DataControllerRequest dcRequest, String userId, DemoDataUK helper)
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

    private void createPayPersons(DataControllerRequest dcRequest, String userId, DemoDataUK helper)
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

    private void createExternalAccounts(DataControllerRequest dcRequest, String userId, DemoDataUK helper)
            throws HttpCallException {

        String accNumber = null;

        Map<String, String> input = helper.getExtAccount1DemoData();
        input.put("User_id", userId);
        accNumber = input.get("accountNumber");
        accNumber = accNumber.replaceAll("\\s+", "");
        input.put("IBAN", accNumber);
        helper.addExternalAccountNum(input.get("beneficiaryName"), input.get("accountNumber"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_EXT_ACCOUNTS_CREATE);

        input = helper.getExtAccount2DemoData();
        input.put("User_id", userId);
        accNumber = input.get("accountNumber");
        accNumber = accNumber.replaceAll("\\s+", "");
        input.put("IBAN", accNumber);
        helper.addExternalAccountNum(input.get("beneficiaryName"), input.get("accountNumber"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_EXT_ACCOUNTS_CREATE);

        input = helper.getExtAccount3DemoData();
        input.put("User_id", userId);
        accNumber = input.get("accountNumber");
        accNumber = accNumber.replaceAll("\\s+", "");
        input.put("IBAN", accNumber);
        helper.addExternalAccountNum(input.get("beneficiaryName"), input.get("accountNumber"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_EXT_ACCOUNTS_CREATE);

        input = helper.getExtAccount4DemoData();
        input.put("User_id", userId);
        accNumber = input.get("accountNumber");
        accNumber = accNumber.replaceAll("\\s+", "");
        input.put("IBAN", accNumber);
        helper.addExternalAccountNum(input.get("beneficiaryName"), input.get("accountNumber"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_EXT_ACCOUNTS_CREATE);

        input = helper.getExtAccount5DemoData();
        input.put("User_id", userId);
        accNumber = input.get("accountNumber");
        accNumber = accNumber.replaceAll("\\s+", "");
        input.put("IBAN", accNumber);
        helper.addExternalAccountNum(input.get("beneficiaryName"), input.get("accountNumber"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_EXT_ACCOUNTS_CREATE);
    }

    private void createCards(DataControllerRequest dcRequest, String userId, DemoDataUK helper)
            throws HttpCallException {

        Map<String, String> input = helper.getCard1DemoData();
        input.put("User_id", userId);
        input.put("account_id", helper.getAccountNum("GB23 MIDL 0700 9312 3456 70"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_CARDS_CREATE);

        input = helper.getCard2DemoData();
        input.put("User_id", userId);
        input.put("account_id", helper.getAccountNum("GB65 MIDL 0700 9312 3456 90"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_CARDS_CREATE);

        input = helper.getCard3DemoData();
        input.put("User_id", userId);
        input.put("account_id", helper.getAccountNum("GB23 MIDL 0700 9312 3456 70"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_CARDS_CREATE);

        input = helper.getCard5DemoData();
        input.put("User_id", userId);
        input.put("account_id", helper.getAccountNum("GB23 MIDL 0700 9312 3456 70"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_CARDS_CREATE);

        input = helper.getCard4DemoData();
        input.put("User_id", userId);
        input.put("account_id", helper.getAccountNum("GB23 MIDL 0700 9312 3456 70"));
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        int dateInt = (calendar.get(Calendar.DATE));
        String date = (dateInt <= 9) ? ("0" + String.valueOf(dateInt)) : String.valueOf(dateInt);
        int mon = (calendar.get(Calendar.MONTH) + 1);
        String month = (mon <= 9) ? ("0" + String.valueOf(mon)) : String.valueOf(mon);
        String expiryDate = year + "-" + month + "-" + date;
        input.put("expirationDate", expiryDate);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_CARDS_CREATE);

        input = helper.getCard6DemoData();
        input.put("User_id", userId);
        input.put("account_id", helper.getAccountNum("GB65 MIDL 0700 9312 3456 90"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_CARDS_CREATE);

        input = helper.getCard7DemoData();
        input.put("User_id", userId);
        input.put("account_id", helper.getAccountNum("GB23 MIDL 0700 9312 3456 70"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_CARDS_CREATE);

        input = helper.getCard8DemoData();
        input.put("User_id", userId);
        input.put("account_id", helper.getAccountNum("GB65 MIDL 0700 9312 3456 90"));
        input.put("expirationDate", expiryDate);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_CARDS_CREATE);

    }

    private void createUserAlerts(DataControllerRequest dcRequest, String userId, DemoDataUK helper)
            throws HttpCallException {

        Map<String, String> input = helper.geUserAlertDemoData();
        input.put("User_id", userId);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_ALERTS_CREATE);
    }

    private void createAccountAlerts(DataControllerRequest dcRequest, String userId, DemoDataUK helper)
            throws HttpCallException {

        Map<String, String> input = helper.geAccountAlert1DemoData();
        input.put("User_id", userId);
        input.put("AccountNumber", helper.getAccountNum("GB23 MIDL 0700 9312 3456 70"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_USER_ACC_ALERT_CREATE);

        input = helper.geAccountAlert2DemoData();
        input.put("User_id", userId);
        input.put("AccountNumber", helper.getAccountNum("GB65 MIDL 0700 9312 3456 90"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_USER_ACC_ALERT_CREATE);

        input = helper.geAccountAlert3DemoData();
        input.put("User_id", userId);
        input.put("AccountNumber", helper.getAccountNum("GB23 MIDL 0700 9312 3456 70"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_USER_ACC_ALERT_CREATE);

    }

    private void createNotifications(DataControllerRequest dcRequest, String userId, DemoDataUK helper)
            throws HttpCallException {
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

    private void updateDemoUserDefaultAccounts(DataControllerRequest dcRequest, String userId, DemoDataUK helper)
            throws HttpCallException {
        Map<String, String> input = new HashMap<>();
        input.put("Id", userId);
        input.put("default_account_cardless", helper.getAccountNum("GB23 MIDL 0700 9312 3456 70"));
        input.put("default_account_billPay", helper.getAccountNum("GB65 MIDL 0700 9312 3456 90"));
        input.put("default_to_account_p2p", helper.getAccountNum("GB23 MIDL 0700 9312 3456 70"));
        input.put("default_from_account_p2p", helper.getAccountNum("GB65 MIDL 0700 9312 3456 90"));
        input.put("default_account_deposit", helper.getAccountNum("GB23 MIDL 0700 9312 3456 70"));
        input.put("default_account_payments", helper.getAccountNum("GB65 MIDL 0700 9312 3456 90"));
        input.put("default_account_transfers", helper.getAccountNum("GB23 MIDL 0700 9312 3456 70"));
        input.put("default_account_wire", helper.getAccountNum("GB65 MIDL 0700 9312 3456 90"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.USER_UPDATE);
    }

    private void updateDemoCustomerDefaultAccounts(DataControllerRequest dcRequest, String userId, DemoDataUK helper)
            throws HttpCallException {
        Map<String, String> input = new HashMap<>();

        String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + userId;

        Result result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERPREFERENCE_GET);

        input.put("id", HelperMethods.getFieldValue(result, "id"));

        input.put("DefaultAccountCardless", helper.getAccountNum("GB23 MIDL 0700 9312 3456 70"));
        input.put("DefaultAccountBillPay", helper.getAccountNum("GB65 MIDL 0700 9312 3456 90"));
        input.put("DefaultToAccountP2P", helper.getAccountNum("GB23 MIDL 0700 9312 3456 70"));
        input.put("DefaultFromAccountP2P", helper.getAccountNum("GB65 MIDL 0700 9312 3456 90"));
        input.put("DefaultAccountDeposit", helper.getAccountNum("GB23 MIDL 0700 9312 3456 70"));
        input.put("DefaultAccountPayments", helper.getAccountNum("GB65 MIDL 0700 9312 3456 90"));
        input.put("DefaultAccountTransfers", helper.getAccountNum("GB23 MIDL 0700 9312 3456 70"));
        input.put("DefaultAccountWire", helper.getAccountNum("GB65 MIDL 0700 9312 3456 90"));

        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERPREFERENCE_UPDATE);
    }

    private void createMessages(DataControllerRequest dcRequest, String userId, DemoDataUK helper)
            throws HttpCallException {
        Map<String, String> input = new HashMap<>();
        input.put("Account_id", helper.getAccountNum("GB65 MIDL 0700 9312 3456 90"));
        input.put("Category_id", "1");
        input.put("Subcategory_id", "1");
        input.put("status", "Inbox");
        input.put("subject", "I want to open a new account");
        input.put("message",
                "Dear customer, We value your relationship. We understand that you want to nominate your wife as a beneficiary in your account. We request you to kindly download the form available on our website and send the details to our customer care department through email or fax. We will enroll her as a beneficiary and provide the confirmation.Thanks Customer Team");
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_MESSAGE_CREATE);

        input.put("Account_id", helper.getAccountNum("GB23 MIDL 0700 9312 3456 70"));
        input.put("Category_id", "1");
        input.put("Subcategory_id", "3");
        input.put("status", "Inbox");
        input.put("subject", "Errorneous debit entry is edited");
        input.put("message",
                "Dear customer, We have found an erroneous debit entry in your account. We have identified that it is because of some manual error while entering the data by one of our executives. We have rectified the issue and we appologize for the inconvenience caused to you.Thanks Customer Team");
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_MESSAGE_CREATE);

    }

    private void createAccountStatements(DataControllerRequest dcRequest, String userId, DemoDataUK helper)
            throws HttpCallException {
        Calendar date1 = new GregorianCalendar(2017, Calendar.NOVEMBER, 1);
        Calendar date2 = new GregorianCalendar(2017, Calendar.DECEMBER, 1);

        Map<String, String> input = new HashMap<>();
        input.put("Account_id", helper.getAccountNum("GB23 MIDL 0700 9312 3456 70"));
        input.put("description", "Statement for November");
        input.put("statementLink", "http://pmqa.konylabs.net/KonyWebBanking/account-statement-pdf.pdf");
        input.put("month", getDateString(date1.getTime()));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_ACCOUNT_STATEMENT_CREATE);

        input.put("Account_id", helper.getAccountNum("GB65 MIDL 0700 9312 3456 90"));
        input.put("description", "Statement for December");
        input.put("statementLink", "http://pmqa.konylabs.net/KonyWebBanking/account-statement-pdf.pdf");
        input.put("month", getDateString(date2.getTime()));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_ACCOUNT_STATEMENT_CREATE);

    }

    private List<Map<String, String>> getCurrentAccountData(DemoDataUK helper) {
        List<Map<String, String>> dataList = Arrays.asList(helper.getWithdrawal_Current47DemoData(),
                helper.getExternalTransfer_Current48DemoData(), helper.getPOS_Current49DemoData(),
                helper.getBillPay_Current50DemoData(), helper.getInternetTransaction_Current51DemoData(),
                helper.getReceivedP2P_Current52DemoData(), helper.getCredit_Current53DemoData(),
                helper.getExternalTransfer_Current54DemoData(), helper.getP2P_Current55DemoData(),
                helper.getInterest_Current56DemoData(), helper.getLoan_Current57DemoData(),
                helper.getCardless_Current58DemoData(), helper.getExternalTransfer_Current59DemoData(),
                helper.getInternalTransfer_Current60DemoData(), helper.getPayroll_Current61DemoData());
        return dataList;
    }

    private void sendEmail(String fromId, String subject, String toId, String userName, String password,
            String userFirstName, String country, DataControllerRequest dcRequest) {
        try {
            Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
            new MailHelper(dcRequest);
            MailHelper.sendMail(fromId, subject, toId, userName, password, userFirstName, country, dcRequest);
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }
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
        SimpleDateFormat idformatter = new SimpleDateFormat("yyyyMMuddHHmmssSSS");
        return idformatter.format(new Date());
    }

    private String createAddress(DataControllerRequest dcRequest, String userId, DemoDataUK helper)
            throws HttpCallException {
        Map<String, String> input = helper.getAddress1DemoData();

        input.put("User_id", userId);

        String id = UUID.randomUUID().toString();
        input.put("id", id);

        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.ADDRESS_CREATE);

        return id;
    }

    private void createCustomerAddress(DataControllerRequest dcRequest, String userId, String addId, DemoDataUK helper)
            throws HttpCallException {

        Map<String, String> input = new HashMap<>();
        input.put("Customer_id", userId);
        input.put("Address_id", addId);

        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMER_ADDRESS_CREATE);

    }

    private void createPhone(DataControllerRequest dcRequest, String userId, DemoDataUK helper)
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

    @SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
    private void createCardStatements(DataControllerRequest dcRequest, DemoDataUK helper)
            throws HttpCallException {
        Date date = new Date();
        int month = date.getMonth();
        int year = date.getYear();
        year = year + 1900;
        Map inputParam = new HashMap();
        Result result = HelperMethods.callApi(dcRequest, inputParam, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARD_STATEMENTS_GET);
        if (result.getAllDatasets().get(0).getAllRecords().size() == 0) {
            String[] cards = { "4213280205276450", "4213280203144800", "4541982333084990", "5314000000004360" };
            String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
                    "October", "November", "December" };
            for (int i = 0; i < cards.length; i++) {
                for (int j = 0; j < months.length; j++) {
                    Map inputParam1 = new HashMap();
                    inputParam1.put("description", "Statement for " + months[j]);
                    inputParam1.put("statementLink",
                            "https://retailbanking1.konycloud.com/dbimages/account-statement-pdf.pdf");
                    inputParam1.put("Card_id", cards[i]);
                    inputParam1.put("month", ((year - 1) + "-" + (j > 8 ? (j + 1) : ("0" + (j + 1)))));
                    HelperMethods.callApi(dcRequest, inputParam1, HelperMethods.getHeaders(dcRequest),
                            URLConstants.DMS_CARD_STATEMENTS_CREATE);
                }
            }
            for (int i = 0; i < cards.length; i++) {
                for (int j = month; j >= 0; j--) {
                    Map inputParam1 = new HashMap();
                    inputParam1.put("description", "Statement for " + months[j]);
                    inputParam1.put("statementLink",
                            "https://retailbanking1.konycloud.com/dbimages/account-statement-pdf.pdf");
                    inputParam1.put("Card_id", cards[i]);
                    inputParam1.put("month", ((year) + "-" + (j > 8 ? (j + 1) : ("0" + (j + 1)))));
                    HelperMethods.callApi(dcRequest, inputParam1, HelperMethods.getHeaders(dcRequest),
                            URLConstants.DMS_CARD_STATEMENTS_CREATE);
                }
            }

        }
    }

    private void createTransactionsForCard(DataControllerRequest dcRequest,
            DemoDataUK helper) throws HttpCallException {
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
