package com.kony.dbputilities.usersecurityservices;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.constants.DBPConstants;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.customersecurityservices.CreateCustomerCommunication;
import com.kony.dbputilities.customersecurityservices.PasswordHistoryManagement;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.BCrypt;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class UserSignatureUpload implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);

        if (preProcess(inputParams, dcRequest, result)) {
            String userid = createUser(inputParams, dcRequest);
            createAccounts(inputParams, dcRequest, userid);
            deleteNewUser(dcRequest, inputParams);
        }
        if (!HelperMethods.hasError(result)) {
            Param p = new Param("success", "Record inserted successfully", DBPConstants.FABRIC_STRING_CONSTANT_KEY);
            result.addParam(p);
        }

        return result;
    }

    @SuppressWarnings("rawtypes")
    private void deleteNewUser(DataControllerRequest dcRequest, Map inputParams) {
        String id = (String) inputParams.get("newuser_id");
        deleteUserProducts(dcRequest, id);
        deleteUserPersonalInfo(dcRequest, id);
        deleteUserCreditCheck(dcRequest, id);
        deleteNewUser(dcRequest, id);
    }

    private void deleteUserProducts(DataControllerRequest dcRequest, String userid) {
        try {
            Result result = HelperMethods.callGetApi(dcRequest, "newuser_id" + DBPUtilitiesConstants.EQUAL + userid,
                    HelperMethods.getHeaders(dcRequest), URLConstants.USER_PRODUCTS_GET);
            if (HelperMethods.hasRecords(result)) {
                List<Record> records = result.getAllDatasets().get(0).getAllRecords();
                Map<String, String> input = new HashMap<>();
                for (Record r : records) {
                    String id = r.getParam(DBPUtilitiesConstants.P_ID).getValue();
                    input.put(DBPUtilitiesConstants.P_ID, id);
                    HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                            URLConstants.USER_PRODUCTS_DELETE);
                }
            }
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }
    }

    private void deleteUserPersonalInfo(DataControllerRequest dcRequest, String userid) {
        try {
            Result result = HelperMethods.callGetApi(dcRequest, "newuser_id" + DBPUtilitiesConstants.EQUAL + userid,
                    HelperMethods.getHeaders(dcRequest), URLConstants.PERSONAL_INFO_GET);
            if (HelperMethods.hasRecords(result)) {
                List<Record> records = result.getAllDatasets().get(0).getAllRecords();
                Map<String, String> input = new HashMap<>();
                for (Record r : records) {
                    String id = r.getParam(DBPUtilitiesConstants.P_ID).getValue();
                    input.put(DBPUtilitiesConstants.P_ID, id);
                    HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                            URLConstants.PERSONAL_INFO_DELETE);
                }
            }
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }
    }

    private void deleteUserCreditCheck(DataControllerRequest dcRequest, String userid) {
        try {
            Result result = HelperMethods.callGetApi(dcRequest, "newuser_id" + DBPUtilitiesConstants.EQUAL + userid,
                    HelperMethods.getHeaders(dcRequest), URLConstants.CREDIT_CHECK_GET);
            if (HelperMethods.hasRecords(result)) {
                List<Record> records = result.getAllDatasets().get(0).getAllRecords();
                Map<String, String> input = new HashMap<>();
                for (Record r : records) {
                    String id = r.getParam(DBPUtilitiesConstants.P_ID).getValue();
                    input.put(DBPUtilitiesConstants.P_ID, id);
                    HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                            URLConstants.CREDIT_CHECK_DELETE);
                }
            }
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }
    }

    private void deleteNewUser(DataControllerRequest dcRequest, String userid) {
        try {
            Map<String, String> input = new HashMap<>();
            input.put("id", userid);
            HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.NEW_USER_DELETE);
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private String createUser(Map inputParams, DataControllerRequest dcRequest) {
        Record peronalInfo = getPersonalInfo(dcRequest, (String) inputParams.get("newuser_id"));
        Record newUser = getNewUser(dcRequest, (String) inputParams.get("newuser_id"));
        Map<String, String> userinput = new HashMap<>();
        Map<String, String> customerTypes = HelperMethods.getCustomerTypes();
        userinput.put("id", HelperMethods.generateUniqueCustomerId(dcRequest));
        userinput.put("CustomerType_id", customerTypes.get("Customer"));
        userinput.put("DateOfBirth", HelperMethods.getFieldValue(peronalInfo, "dateOfBirth"));
        userinput.put("FirstName", HelperMethods.getFieldValue(peronalInfo, "userfirstname"));
        userinput.put("LastName", HelperMethods.getFieldValue(peronalInfo, "userlastname"));
        userinput.put("Ssn", HelperMethods.getFieldValue(peronalInfo, "ssn"));
        userinput.put("Gender", HelperMethods.getFieldValue(peronalInfo, "gender"));
        userinput.put("MaritalStatus", HelperMethods.getFieldValue(peronalInfo, "maritalstatus"));
        userinput.put("SpouseFirstName", HelperMethods.getFieldValue(peronalInfo, "spouseFirstName"));
        userinput.put("SpouseLastName", HelperMethods.getFieldValue(peronalInfo, "spouseLastName"));
        userinput.put("NoOfDependents", HelperMethods.getFieldValue(peronalInfo, "spouseLastName"));
        userinput.put("UserName", HelperMethods.getFieldValue(newUser, "userName"));
        String password = HelperMethods.getFieldValue(newUser, "passWord");
        userinput.put("Password", password);
        inputParams.put("Status_id", "SID_CUS_ACTIVE");
        String salt = BCrypt.gensalt(DBPUtilitiesConstants.SALT_NUMBER_OF_ROUNDS);
        String hashedPassword = BCrypt.hashpw(password, salt);
        userinput.put("Password", hashedPassword);

        userinput.put("EmploymentInfo", HelperMethods.getFieldValue(newUser, "employmentInfo"));
        userinput.put("isEnrolled", "true");
        userinput.put("isWireTransferEligible", "true");
        // userinput.put("UserImageURL", "https://retailbanking1.konycloud.com/dbimages/displayPicture.png");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 1);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        userinput.put("ValidDate", formatter.format(cal.getTime()));
        userinput.put("Lastlogintime", formatter.format(cal.getTime()));
        Result user = new Result();
        try {
            user = HelperMethods.callApi(dcRequest, userinput, HelperMethods.getHeaders(dcRequest),
                    URLConstants.USER_CREATE);
        } catch (HttpCallException e1) {
            alert.prepareError(e1.getMessage()).log();
        }

        String userId = "";

        if (HelperMethods.hasRecords(user)) {

            userId = HelperMethods.getFieldValue(user, "id");
            Map<String, String> custCommMap = new HashMap<>();
            custCommMap.put("id", userId);
            custCommMap.put("Phone", HelperMethods.getFieldValue(newUser, "phone"));
            custCommMap.put("Email", HelperMethods.getFieldValue(newUser, "email"));
            try {
                CreateCustomerCommunication.invoke(custCommMap, dcRequest);
            } catch (Exception e) {

            }

            PasswordHistoryManagement pm = new PasswordHistoryManagement(dcRequest, false);
            try {
                pm.makePasswordEntry(dcRequest, userId, userinput.get("Password"));
            } catch (HttpCallException e1) {
                alert.prepareError(e1.getMessage()).log();
            }

            createCustomerCommunication(userId, dcRequest, newUser);
            createAddress(userId, dcRequest, peronalInfo);
            Map<String, String> postParamMapGroup = new HashMap<>();
            postParamMapGroup.put("Customer_id", userId);
            postParamMapGroup.put("Group_id", "DEFAULT_GROUP");
            try {
                HelperMethods.callApi(dcRequest, postParamMapGroup, HelperMethods.getHeaders(dcRequest),
                        URLConstants.CUSTOMER_GROUP_CREATE);
            } catch (HttpCallException e) {
                alert.prepareError(e.getMessage()).log();
            }
            inputParams.put("userName", HelperMethods.getFieldValue(newUser, "userName"));
        }

        return userId;
    }

    private void createAddress(String userId, DataControllerRequest dcRequest, Record peronalInfo) {
        Map<String, String> adrsInput = new HashMap<>();
        Map<String, String> custAdrsInput = new HashMap<>();
        String addressId = "ADD" + HelperMethods.getNewId();
        adrsInput.put("id", addressId);
        adrsInput.put("isPreferredAddress", "1");
        adrsInput.put("createdby", "Infinity User");
        adrsInput.put("modifiedby", "Infinity Dev");
        adrsInput.put("addressLine1", HelperMethods.getFieldValue(peronalInfo, "addressLine1"));
        adrsInput.put("addressLine2", HelperMethods.getFieldValue(peronalInfo, "addressLine2"));
        adrsInput.put("cityName", HelperMethods.getFieldValue(peronalInfo, "city"));
        adrsInput.put("state", HelperMethods.getFieldValue(peronalInfo, "state"));
        adrsInput.put("country", HelperMethods.getFieldValue(peronalInfo, "country"));
        adrsInput.put("zipCode", HelperMethods.getFieldValue(peronalInfo, "zipcode"));

        Result result = new Result();
        try {
            result = HelperMethods.callApi(dcRequest, adrsInput, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ADDRESS_CREATE);
        } catch (HttpCallException e) {
            alert.prepareError(e.getMessage()).log();
        }

        if (HelperMethods.hasRecords(result)) {
            custAdrsInput.put("Customer_id", userId);
            custAdrsInput.put("Address_id", addressId);
            custAdrsInput.put("Type_id", "ADR_TYPE_HOME");
            custAdrsInput.put("isPrimary", "1");
            custAdrsInput.put("createdby", "Infinity User");
            custAdrsInput.put("modifiedby", "Infinity Dev");
            try {
                HelperMethods.callApi(dcRequest, custAdrsInput, HelperMethods.getHeaders(dcRequest),
                        URLConstants.CUSTOMER_ADDRESS_CREATE);
            } catch (HttpCallException e) {
                alert.prepareError(e.getMessage()).log();
            }
        }
    }

    private void createCustomerCommunication(String userId, DataControllerRequest dcRequest, Record newUser) {
        Map<String, String> input = new HashMap<>();
        input.put("Customer_id", userId);
        input.put("isPrimary", "1");
        input.put("Extension", DBPUtilitiesConstants.CUSTOMER_COMMUNICATION_PHONE_DEFAULT_EXTENSION);
        input.put("softdeleteflag", "0");
        input.put("createdby", "Infinity User");
        input.put("modifiedby", "Infinity User");
        input.put("id", HelperMethods.getNewId());
        input.put("Type_id", DBPUtilitiesConstants.COMM_TYPE_PHONE);
        input.put("Value", HelperMethods.getFieldValue(newUser, "phone"));
        CreateCustomerCommunication.invoke(input, dcRequest);
        input.put("id", HelperMethods.getNewId());
        input.put("Type_id", DBPUtilitiesConstants.COMM_TYPE_EMAIL);
        input.put("Value", HelperMethods.getFieldValue(newUser, "email"));
        CreateCustomerCommunication.invoke(input, dcRequest);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void createAccounts(Map inputParams, DataControllerRequest dcRequest, String userid)
            throws HttpCallException {
        String filter = "newuser_id" + DBPUtilitiesConstants.EQUAL + (String) inputParams.get("newuser_id");
        Result temp = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.USER_PRODUCTS_GET);
        SimpleDateFormat idformatter = new SimpleDateFormat("yyMMddHHmmssSSS");
        int accountPreference = -1;
        if (HelperMethods.hasRecords(temp)) {
            for (Record rec : temp.getAllDatasets().get(0).getAllRecords()) {
                JsonObject product = getProductJson(HelperMethods.getParamValue(rec.getParam("product")));
                if (null == getAccoutType(product)) {
                    continue;
                }
                accountPreference++;
                Map accountInput = new HashMap();
                accountInput.put(DBPUtilitiesConstants.PI_USR_ID, userid);
                accountInput.put("Type_id", getAccoutType(product));
                accountInput.put("AccountName", product.get("productName").getAsString());
                accountInput.put("Name", product.get("productName").getAsString());
                accountInput.put("NickName", product.get("productName").getAsString());
                accountInput.put("Account_id", idformatter.format(new Date()));
                accountInput.put("Bank_id", "1");
                accountInput.put("PaymentTerm", "0");
                accountInput.put("PrincipalValue", "0");
                accountInput.put("isPFM", "1");
                accountInput.put("statusDesc", "active");
                accountInput.put("AccountPreference", accountPreference);
                accountInput.put("AvailableBalance", "0");
                accountInput.put("AvailableCredit", "0");
                accountInput.put("CurrentBalance", "0");
                accountInput.put("MinimumDue", "0");
                accountInput.put("SupportCardlessCash", "1");
                accountInput.put("SupportDeposit", "1");
                accountInput.put("SupportBillPay", "1");
                accountInput.put("SupportTransferFrom", "1");
                accountInput.put("SupportTransferTo", "1");
                accountInput.put("OutstandingBalance", "0");
                accountInput.put("ClosingDate", HelperMethods.getCurrentDate());
                accountInput.put("DueDate", HelperMethods.getCurrentDate());
                accountInput.put("MaturityDate", HelperMethods.getCurrentDate());
                accountInput.put("AccountHolder", "{\"fullname\":\"" + accountInput.get("Name") + "\",\"username\":\""
                        + inputParams.get("userName") + "\"}");
                accountInput.put("InterestRate", "0");
                accountInput.put("OpeningDate", HelperMethods.getCurrentDate());
                accountInput.put("ShowTransactions", "1");
                accountInput.put("TransactionLimit", "0");
                accountInput.put("TransferLimit", "0");
                accountInput.put("FavouriteStatus", "1");
                accountInput.put("MaturityOption", "0");
                accountInput.put("RoutingNumber", "0");
                accountInput.put("JointHolders", "Jane Bailey");
                accountInput.put("DividendRate", "0");
                accountInput.put("DividendYTD", "0");
                accountInput.put("LastDividendPaidAmount", "0");
                accountInput.put("LastDividendPaidDate", HelperMethods.getCurrentDate());
                accountInput.put("PreviousYearDividend", "0");
                accountInput.put("BondInterest", "0");
                accountInput.put("BondInterestLastYear", "0");
                accountInput.put("TotalCreditMonths", "0");
                accountInput.put("TotalDebitsMonth", "0");
                accountInput.put("CurrentAmountDue", "0");
                accountInput.put("PaymentDue", "0");
                accountInput.put("LastPaymentAmount", "0");
                accountInput.put("LateFeesDue", "0");
                accountInput.put("CreditLimit", "0");
                accountInput.put("InterestPaidYTD", "0");
                accountInput.put("InterestPaidPreviousYTD", "0");
                accountInput.put("UnpaidInterest", "0");
                accountInput.put("RegularPaymentAmount", "0");
                accountInput.put("DividendPaidYTD", "0");
                accountInput.put("DividendLastPaidAmount", "0");
                accountInput.put("DividendLastPaidDate", HelperMethods.getCurrentDate());
                accountInput.put("PreviousYearsDividends", "0");
                accountInput.put("PendingDeposit", "0");
                accountInput.put("PendingWithdrawal", "0");
                accountInput.put("InterestEarned", "0");
                accountInput.put("maturityAmount", "0");
                accountInput.put("principalBalance", "0");
                accountInput.put("OriginalAmount", "0");
                accountInput.put("payoffAmount", "0");
                accountInput.put("BsbNum", "000000000");
                accountInput.put("PayOffCharge", "0");
                accountInput.put("InterestPaidLastYear", "0");
                accountInput.put("EStatementmentEnable", "0");

                boolean check = "3".equals(product.get("productTypeId").getAsString());
                if (check) {
                    accountInput.put("CreditCardNumber", "978913571");
                    accountInput.put("AvailablePoints", "0");
                    accountInput.put("LastStatementBalance", "0");
                }
                HelperMethods.callApi(dcRequest, accountInput, HelperMethods.getHeaders(dcRequest),
                        URLConstants.ACCOUNTS_CREATE);
            }
        }
    }

    private String getAccoutType(JsonObject product) {
        String accoutType = product.get("productType").getAsString();
        if (StringUtils.isNotBlank(accoutType)) {
            if ("Checking".equalsIgnoreCase(accoutType) || "Checking Account".equalsIgnoreCase(accoutType)) {
                return "1";
            } else if ("Savings".equalsIgnoreCase(accoutType) || "Savings Default".equalsIgnoreCase(accoutType)) {
                return "2";
            } else if ("CreditCard".equalsIgnoreCase(accoutType) || "Credit Card".equalsIgnoreCase(accoutType)) {
                return "3";
            } else if ("Deposit".equalsIgnoreCase(accoutType)) {
                return "4";
            } else if ("Mortgage".equalsIgnoreCase(accoutType)) {
                return "5";
            } else if ("Loan".equalsIgnoreCase(accoutType) || "Turbo Auto Loan".equalsIgnoreCase(accoutType)) {
                return "6";
            } else if ("Current".equalsIgnoreCase(accoutType)) {
                return "7";
            }
        }

        return "2";
    }

    /*
     * private Map<String, String> getAccountTypes(DataControllerRequest dcRequest) throws HttpCallException { Result
     * accountTypes = HelperMethods.callApi(null,HelperMethods.getHeaders(dcRequest), URLConstants.ACCOUNTTYPE_GET);
     * Map<String, String> accountTypeMap = new HashMap<>(); List<Record> types =
     * accountTypes.getAllDatasets().get(0).getAllRecords(); for(Record type: types){
     * accountTypeMap.put(HelperMethods.getFieldValue(type, "TypeID"), HelperMethods.getFieldValue(type,
     * "TypeDescription")); } return accountTypeMap; }
     */

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
        String userid = HelperMethods.getUserIdFromNUOSession(dcRequest);
        inputParams.put("newuser_id", userid);
        return true;
    }

    private JsonObject getProductJson(String product) {
        JsonParser jsonParser = new JsonParser();
        JsonObject prodJson = (JsonObject) jsonParser.parse(product);
        return prodJson;
    }

    private Record getPersonalInfo(DataControllerRequest dcRequest, String userid) {
        Record rec = null;
        String filter = "newuser_id" + DBPUtilitiesConstants.EQUAL + userid;
        Result temp = new Result();
        try {
            temp = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.PERSONAL_INFO_GET);
        } catch (HttpCallException e) {
            alert.prepareError(e.getMessage()).log();
        }

        if (HelperMethods.hasRecords(temp)) {
            Dataset ds = temp.getAllDatasets().get(0);
            if (null != ds && null != ds.getAllRecords() && !ds.getAllRecords().isEmpty()) {
                rec = ds.getAllRecords().get(0);
            }
        }
        return rec;
    }

    private Record getNewUser(DataControllerRequest dcRequest, String userid) {
        Record rec = null;
        String filter = "id" + DBPUtilitiesConstants.EQUAL + userid;
        Result temp = new Result();
        try {
            temp = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.NEW_USER_GET);
        } catch (HttpCallException e) {
            alert.prepareError(e.getMessage()).log();
        }
        if (HelperMethods.hasRecords(temp)) {
            Dataset ds = temp.getAllDatasets().get(0);
            if (null != ds && null != ds.getAllRecords() && !ds.getAllRecords().isEmpty()) {
                rec = ds.getAllRecords().get(0);
            }
        }
        return rec;
    }
}