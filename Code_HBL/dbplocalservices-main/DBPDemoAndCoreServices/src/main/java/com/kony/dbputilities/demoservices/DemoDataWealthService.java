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
import java.util.UUID;
import java.util.concurrent.Callable;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.customersecurityservices.CreateCustomerAddress;
import com.kony.dbputilities.customersecurityservices.CreateCustomerCommunication;
import com.kony.dbputilities.customersecurityservices.PasswordHistoryManagement;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
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

@SuppressWarnings("unchecked")
public class DemoDataWealthService implements JavaService2, DemoDataService {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {

        Result result = new Result();
        Map<String, String> inputmap = HelperMethods.getInputParamMap(inputArray);

        DemoDataWealth helper = new DemoDataWealth();
        helper.init(dcRequest);
        helper.setUserName(inputmap.get("newUsername"));
        /**
         * Creating user contract and user with
         * 
         * User 1 - Wealth Banking - Retail and Wealth Online Banking - Retail and Wealth Role
         * 
         */
        createRetailWealthDemoUserContract(methodId, inputArray, dcRequest, dcResponse, helper);
        String userId = createRetailWealthCustomer(dcRequest, inputmap, helper);
        createCustomerAccounts(dcRequest, userId, helper);
        createUserActionLimits(dcRequest, helper, userId);
        createRequisites(dcRequest, userId, helper);
        createEntryInCustomerLegalEntity(dcRequest,userId);
        result.addStringParam("reatilWealthCustomer", helper.getUserName() + "RW");

        /**
         * Creating user contract and user with
         * 
         * User 2 - Wealth Banking - Wealth Online Banking - Wealth Role
         * 
         */

        createWealthDemoUserContract(methodId, inputArray, dcRequest, dcResponse, helper);
        userId = createWealthCustomer(dcRequest, inputmap, helper);
        createWealthCustomerAccounts(dcRequest, userId, helper);
        createWealthUserActionLimits(dcRequest, helper, userId);
        createRequisites(dcRequest, userId, helper);
        createEntryInCustomerLegalEntity(dcRequest,userId);
        result.addStringParam("wealthCustomer", helper.getUserName() + "W");
        return result;
    }
    public void createEntryInCustomerLegalEntity(DataControllerRequest dcRequest, String userId) throws HttpCallException {
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("id",UUID.randomUUID().toString());
        inputMap.put("Customer_id",userId);
        inputMap.put("Status_id","SID_CUS_ACTIVE");
        inputMap.put("legalEntityId", "GB0010001");
        HelperMethods.callApi(dcRequest, inputMap, HelperMethods.getHeaders(dcRequest), URLConstants.CUSTOMERLEGALENTITY_CREATE);
		
	}

    private void createRequisites(DataControllerRequest dcRequest, String userId, DemoDataWealth helper) {

        try {

            createLimitGroupLimits(userId, helper, dcRequest);

            createPhone(dcRequest, userId, helper);
            // createNotifications(dcRequest, userId);
            createUserSecurityQuestions(dcRequest, userId);
            createPayees(dcRequest, userId, helper);
            createPayPersons(dcRequest, userId, helper);
            createExternalAccounts(dcRequest, userId, helper);
            createCards(dcRequest, userId, helper);
            createUserAlerts(dcRequest, userId, helper);
            createAccountAlerts(dcRequest, userId, helper);
            createUserBill(dcRequest, helper);

            /**
             * Transaction starts
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
            //createTransactionsForCard(dcRequest, helper);

            /**
             * Transaction ends
             */

            updateDemoCustomerDefaultAccounts(dcRequest, userId, helper);
            createMessages(dcRequest, helper);
            createAccountStatements(dcRequest, helper);

        } catch (Exception e) {
            alert.prepareError("Exception occured while creating Demo user" + e.getMessage()).log();
        }
    }

    private void updateDemoCustomerDefaultAccounts(DataControllerRequest dcRequest, String userId,
            DemoDataWealth helper)
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

    private void createMessages(DataControllerRequest dcRequest, DemoDataWealth helper) throws HttpCallException {
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

    private void createAccountStatements(DataControllerRequest dcRequest, DemoDataWealth helper)
            throws HttpCallException {
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

    private void createTransactionsForCard(DataControllerRequest dcRequest,
            DemoDataWealth helper) throws HttpCallException {
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

    private List<Map<String, String>> getCreditAccountData1(DemoDataWealth helper) {
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

    private List<Map<String, String>> getCreditAccountData2(DemoDataWealth helper) {
        return Arrays.asList(helper.getPOS_CC2_1DemoData(), helper.getInternalTransfer_CC2_2DemoData(),
                helper.getPOS_CC2_3DemoData(), helper.getInternetTransaction_CC2_4DemoData(),
                helper.getBillPay_CC2_5DemoData(), helper.getPOS_CC2_6DemoData(), helper.getCardPayment_CC2_7DemoData(),
                helper.getPOS_CC2_8DemoData(), helper.getPOS_CC2_9DemoData(), helper.getPOS_CC2_10DemoData(),
                helper.getInternetTransaction_CC2_11DemoData(), helper.getInternetTransaction_CC2_12DemoData(),
                helper.getPOS_CC2_13DemoData(), helper.getCredit_CC2_14DemoData(), helper.getBillPay_CC2_15DemoData(),
                helper.getPOS_CC2_16DemoData(), helper.getInternetTransaction_CC2_17DemoData(),
                helper.getPOS_CC2_18DemoData(), helper.getPOS_CC2_19DemoData(), helper.getPOS_CC2_20DemoData(),
                helper.getInternetTransaction_CC2_21DemoData(), helper.getInternetTransaction_CC2_22DemoData(),
                helper.getPOS_CC2_23DemoData(), helper.getCredit_CC2_24DemoData(), helper.getBillPay_CC2_25DemoData(),
                helper.getPOS_CC2_26DemoData(), helper.getInternetTransaction_CC2_27DemoData(),
                helper.getPOS_CC2_28DemoData(), helper.getPOS_CC2_29DemoData(), helper.getPOS_CC2_30DemoData(),
                helper.getPOS_CC2_31DemoData(), helper.getInternetTransaction_CC2_32DemoData(),
                helper.getPOS_CC2_33DemoData(), helper.getPOS_CC2_34DemoData(), helper.getPOS_CC2_35DemoData(),
                helper.getInternetTransaction_CC2_36DemoData(), helper.getInternetTransaction_CC2_37DemoData(),
                helper.getPOS_CC2_38DemoData(), helper.getCredit_CC2_39DemoData(), helper.getBillPay_CC2_40DemoData(),
                helper.getPOS_CC2_41DemoData(), helper.getInternetTransaction_CC2_42DemoData(),
                helper.getPOS_CC2_43DemoData(), helper.getPOS_CC2_44DemoData(), helper.getPOS_CC2_45DemoData(),
                helper.getInternetTransaction_CC2_46DemoData(), helper.getInternetTransaction_CC2_47DemoData(),
                helper.getPOS_CC2_48DemoData(), helper.getCredit_CC2_49DemoData(), helper.getBillPay_CC2_50DemoData());
    }

    private List<Map<String, String>> getCheckingAccountData2(DemoDataWealth helper) {
        return Arrays.asList(helper.getCredit_Checking2_1DemoData(), helper.getInternalTransfer_Checking2_2DemoData(),
                helper.getInternalTransfer_Checking2_3DemoData(), helper.getInternalTransfer_Checking2_4DemoData(),
                helper.getCheckDeposit_Checking2_5DemoData(), helper.getBillPay_Checking2_6DemoData(),
                helper.getInterest_Checking2_7DemoData(), helper.getWithdrawal_Checking2_8DemoData(),
                helper.getBillPay_Checking2_9DemoData(), helper.getInternetTransaction_Checking2_10DemoData(),
                helper.getCredit_Checking2_11DemoData(), helper.getCredit_Checking2_12DemoData(),
                helper.getPOS_Checking2_13DemoData(), helper.getInternetTransaction_Checking2_14DemoData(),
                helper.getInternetTransaction_Checking2_15DemoData(), helper.getCredit_Checking2_16DemoData(),
                helper.getCredit_Checking2_17DemoData(), helper.getBillPay_Checking2_18DemoData(),
                helper.getPOS_Checking2_19DemoData(), helper.getInternetTransaction_Checking2_20DemoData(),
                helper.getCredit_Checking2_21DemoData(), helper.getCredit_Checking2_22DemoData(),
                helper.getCredit_Checking2_23DemoData(), helper.getPOS_Checking2_24DemoData(),
                helper.getInternetTransaction_Checking2_25DemoData(), helper.getCredit_Checking2_26DemoData(),
                helper.getCredit_Checking2_27DemoData(), helper.getPOS_Checking2_28DemoData(),
                helper.getCardPayment_Checking2_29DemoData(), helper.getBillPay_Checking2_30DemoData());
    }

    private List<Map<String, String>> getCheckingAccountData1(DemoDataWealth helper) {
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

    private void createTransactionsForCheckingAccount1(DataControllerRequest dcRequest, DemoDataWealth helper)
            throws HttpCallException {

        List<Map<String, String>> dataList = getCheckingAccountData1(helper);
        for (Map<String, String> data : dataList) {
            helper.insertTransaction(data, dcRequest, helper);
        }
    }

    private void createTransactionsForCheckingAccount2(DataControllerRequest dcRequest, DemoDataWealth helper)
            throws HttpCallException {

        List<Map<String, String>> dataList = getCheckingAccountData2(helper);
        for (Map<String, String> data : dataList) {
            helper.insertTransaction(data, dcRequest, helper);
        }
    }

    private void createTransactionsForCreditCardAccount1(DataControllerRequest dcRequest, DemoDataWealth helper)
            throws HttpCallException {

        List<Map<String, String>> dataList = getCreditAccountData1(helper);
        for (Map<String, String> data : dataList) {
            helper.insertTransaction(data, dcRequest, helper);
        }
    }

    private void createTransactionsForCreditCardAccount2(DataControllerRequest dcRequest, DemoDataWealth helper)
            throws HttpCallException {

        List<Map<String, String>> dataList = getCreditAccountData2(helper);
        for (Map<String, String> data : dataList) {
            helper.insertTransaction(data, dcRequest, helper);
        }
    }

    private void createTransactionsForDepositAccount(DataControllerRequest dcRequest, DemoDataWealth helper)
            throws HttpCallException {
        Map<String, String> input = helper.getInterestCredit_Deposit1DemoData();
        helper.insertTransaction(input, dcRequest, helper);

        input = helper.getInterestCredit_Deposit2DemoData();
        helper.insertTransaction(input, dcRequest, helper);

        input = helper.getInterestCredit_Deposit3DemoData();
        helper.insertTransaction(input, dcRequest, helper);

        input = helper.getDeposit_Deposit4DemoData();
        helper.insertTransaction(input, dcRequest, helper);
    }

    private void createTransactionsForSavingsAccount(DataControllerRequest dcRequest, DemoDataWealth helper)
            throws HttpCallException {
        Map<String, String> input = helper.getInternalTransfer_SAV1DemoData();
        helper.insertTransaction(input, dcRequest, helper);

        input = helper.getCheckDeposit_SAV2DemoData();
        helper.insertTransaction(input, dcRequest, helper);

        input = helper.getExternalTransfer_SAV3DemoData();
        input.put("toExternalAccountNumber", helper.getExternalAccountNum(input.get("beneficiaryName")));
        helper.insertTransaction(input, dcRequest, helper);

        input = helper.getRemoteCheckDeposit_SAV4DemoData();
        helper.insertTransaction(input, dcRequest, helper);

        input = helper.getInternalTransfer_SAV5DemoData();
        helper.insertTransaction(input, dcRequest, helper);

        input = helper.getInterest_SAV6DemoData();
        helper.insertTransaction(input, dcRequest, helper);

        input = helper.getRemoteCheckDeposit_SAV7DemoData();
        helper.insertTransaction(input, dcRequest, helper);

        input = helper.getCredit_SAV8DemoData();
        helper.insertTransaction(input, dcRequest, helper);

        input = helper.getInternalTransfer_SAV9DemoData();
        helper.insertTransaction(input, dcRequest, helper);

        input = helper.getExternalTransfer_SAV10DemoData();
        input.put("toExternalAccountNumber", helper.getExternalAccountNum(input.get("beneficiaryName")));
        helper.insertTransaction(input, dcRequest, helper);

        input = helper.getLoan_SAV11DemoData();
        helper.insertTransaction(input, dcRequest, helper);
    }

    private void createTransactionsForLoanAccount(DataControllerRequest dcRequest, DemoDataWealth helper)
            throws HttpCallException {
        Map<String, String> input = helper.getLoanPayment_Loan1DemoData();
        helper.insertTransaction(input, dcRequest, helper);

        input = helper.getLoanPayment_Loan2DemoData();
        helper.insertTransaction(input, dcRequest, helper);

        input = helper.getLoanPayment_Loan3DemoData();
        helper.insertTransaction(input, dcRequest, helper);

        input = helper.getTax_Loan4DemoData();
        helper.insertTransaction(input, dcRequest, helper);

        input = helper.getLoanPayment_Loan5DemoData();
        helper.insertTransaction(input, dcRequest, helper);

        input = helper.getLoanPayment_Loan6DemoData();
        helper.insertTransaction(input, dcRequest, helper);

        input = helper.getLoanPayment_Loan7DemoData();
        helper.insertTransaction(input, dcRequest, helper);

        input = helper.getLoanPayment_Loan8DemoData();
        helper.insertTransaction(input, dcRequest, helper);

        input = helper.getLoanPayment_Loan9DemoData();
        helper.insertTransaction(input, dcRequest, helper);

        input = helper.getLoanPayment_Loan10DemoData();
        helper.insertTransaction(input, dcRequest, helper);

        input = helper.getLoanPayment_Loan11DemoData();
        helper.insertTransaction(input, dcRequest, helper);

        input = helper.getCredit_Loan12DemoData();
        helper.insertTransaction(input, dcRequest, helper);
    }

    private void createRecentCardlessWithdrawal(DataControllerRequest dcRequest, DemoDataWealth helper)
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
        helper.addDateField(input, "cashlessOTPValidDate");
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_ACCOUNT_TRANSACTION_CREATE);

        input = helper.getRecentCardLessWithdrawal2DemoData();
        input.put("Type_id", "6");
        input.put("fromAccountNumber", helper.getAccountNum("Business Advantage Savings"));
        input.put("createdDate", today);
        input.put("transactionDate", transactionDate);
        input.put("scheduledDate", today);
        helper.addDateField(input, "cashlessOTPValidDate");
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_ACCOUNT_TRANSACTION_CREATE);

        input = helper.getRecentCardLessWithdrawal3DemoData();
        input.put("Type_id", "6");
        input.put("fromAccountNumber", helper.getAccountNum("Business Advantage Savings"));
        input.put("createdDate", today);
        input.put("transactionDate", transactionDate);
        input.put("scheduledDate", today);
        helper.addDateField(input, "cashlessOTPValidDate");
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_ACCOUNT_TRANSACTION_CREATE);
    }

    private void createRecentChkDeposits(DataControllerRequest dcRequest, DemoDataWealth helper)
            throws HttpCallException {
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

    private void createUserBill(DataControllerRequest dcRequest, DemoDataWealth helper) throws HttpCallException {
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

    private void createPayees(DataControllerRequest dcRequest, String userId, DemoDataWealth helper)
            throws HttpCallException {

        Result rs = null;
        Map<String, String> input = helper.getPayee1DemoData();
        input.put("User_Id", userId);
        input.put("billermaster_id", "6");
        input.put("transitDays", "3");
        input.put("Id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.PAYEE_CREATE);
        helper.addPayee("payee1", helper.getFieldValue(rs, "Id"));
        createBillPayInfinityBeneficiary(userId, helper.getFieldValue(rs, "Id"), helper);

        input = helper.getPayee2DemoData();
        input.put("User_Id", userId);
        input.put("billermaster_id", "1");
        input.put("transitDays", "3");
        input.put("Id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.PAYEE_CREATE);
        helper.addPayee("payee2", helper.getFieldValue(rs, "Id"));
        createBillPayInfinityBeneficiary(userId, helper.getFieldValue(rs, "Id"), helper);

        input = helper.getPayee3DemoData();
        input.put("User_Id", userId);
        input.put("billermaster_id", "1");
        input.put("transitDays", "3");
        input.put("Id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.PAYEE_CREATE);
        helper.addPayee("payee3", helper.getFieldValue(rs, "Id"));
        createBillPayInfinityBeneficiary(userId, helper.getFieldValue(rs, "Id"), helper);

        input = helper.getPayee4DemoData();
        input.put("User_Id", userId);
        input.put("billermaster_id", "4");
        input.put("transitDays", "3");
        input.put("Id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.PAYEE_CREATE);
        helper.addPayee("payee4", helper.getFieldValue(rs, "Id"));
        createBillPayInfinityBeneficiary(userId, helper.getFieldValue(rs, "Id"), helper);

        input = helper.getPayee5DemoData();
        input.put("User_Id", userId);
        input.put("billermaster_id", "8");
        input.put("transitDays", "3");
        input.put("Id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.PAYEE_CREATE);
        helper.addPayee("payee5", helper.getFieldValue(rs, "Id"));
        createBillPayInfinityBeneficiary(userId, helper.getFieldValue(rs, "Id"), helper);

        input = helper.getPayee6DemoData();
        input.put("User_Id", userId);
        input.put("billermaster_id", "7");
        input.put("transitDays", "3");
        input.put("Id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.PAYEE_CREATE);
        helper.addPayee("payee6", helper.getFieldValue(rs, "Id"));
        createBillPayInfinityBeneficiary(userId, helper.getFieldValue(rs, "Id"), helper);

    }

    private void createBillPayInfinityBeneficiary(String userId, String payeeId, DemoDataWealth helper)
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

    private void createPayPersons(DataControllerRequest dcRequest, String userId, DemoDataWealth helper)
            throws HttpCallException {

        Result rs = null;
        Map<String, String> input = helper.getPerson1DemoData();
        input.put("User_id", userId);
        input.put("id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_PAYPERSON_CREATE);
        helper.addPerson("person1", helper.getFieldValue(rs, "id"));

        input = helper.getPerson2DemoData();
        input.put("User_id", userId);
        input.put("id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_PAYPERSON_CREATE);
        helper.addPerson("person2", helper.getFieldValue(rs, "id"));

        input = helper.getPerson3DemoData();
        input.put("User_id", userId);
        input.put("id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_PAYPERSON_CREATE);
        helper.addPerson("person3", helper.getFieldValue(rs, "id"));

        input = helper.getPerson4DemoData();
        input.put("User_id", userId);
        input.put("id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_PAYPERSON_CREATE);
        helper.addPerson("person4", helper.getFieldValue(rs, "id"));
    }

    private void createExternalAccounts(DataControllerRequest dcRequest, String userId, DemoDataWealth helper)
            throws HttpCallException {
        Result rs = null;
        Map<String, String> input = helper.getExtAccount1DemoData();

        input.put("User_id", userId);
        helper.addExternalAccountNum(input.get("beneficiaryName"), input.get("accountNumber"));
        input.put("Id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.EXT_ACCOUNTS_CREATE);
        createInterBankInfinityBeneficiary(userId, helper.getFieldValue(rs, "Id"), helper);

        input = helper.getExtAccount2DemoData();
        input.put("User_id", userId);
        helper.addExternalAccountNum(input.get("beneficiaryName"), input.get("accountNumber"));
        input.put("Id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.EXT_ACCOUNTS_CREATE);
        createInterBankInfinityBeneficiary(userId, helper.getFieldValue(rs, "Id"), helper);

        input = helper.getExtAccount3DemoData();
        input.put("User_id", userId);
        helper.addExternalAccountNum(input.get("beneficiaryName"), input.get("accountNumber"));
        input.put("Id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.EXT_ACCOUNTS_CREATE);
        createInterBankInfinityBeneficiary(userId, helper.getFieldValue(rs, "Id"), helper);

        input = helper.getExtAccount4DemoData();
        input.put("User_id", userId);
        helper.addExternalAccountNum(input.get("beneficiaryName"), input.get("accountNumber"));
        input.put("Id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.EXT_ACCOUNTS_CREATE);
        createIntraBankInfinityBeneficiary(userId, helper.getFieldValue(rs, "Id"), helper);

        input = helper.getExtAccount5DemoData();
        input.put("User_id", userId);
        helper.addExternalAccountNum(input.get("beneficiaryName"), input.get("accountNumber"));
        input.put("Id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.EXT_ACCOUNTS_CREATE);
        createInternationalInfinityBeneficiary(userId, helper.getFieldValue(rs, "Id"), helper);
    }

    private void createInterBankInfinityBeneficiary(String userId, String payeeId, DemoDataWealth helper)
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

    private void createIntraBankInfinityBeneficiary(String userId, String payeeId, DemoDataWealth helper)
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

    private void createInternationalInfinityBeneficiary(String userId, String payeeId, DemoDataWealth helper)
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

    private void createCards(DataControllerRequest dcRequest, String userId, DemoDataWealth helper)
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

    private void createUserAlerts(DataControllerRequest dcRequest, String userId, DemoDataWealth helper)
            throws HttpCallException {

        Map<String, String> input = helper.geUserAlertDemoData();
        input.put("User_id", userId);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_ALERTS_CREATE);
    }

    private void createAccountAlerts(DataControllerRequest dcRequest, String userId, DemoDataWealth helper)
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

    private void createPhone(DataControllerRequest dcRequest, String userId, DemoDataWealth helper)
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

    private void createLimitGroupLimits(String userId, DemoDataWealth helper, DataControllerRequest dcRequest) {
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

    private void createUserActionLimits(DataControllerRequest dcRequest, DemoDataWealth helper, String userId) {

        CustomerActionsBusinessDelegate businessDelegate =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(CustomerActionsBusinessDelegate.class);
        Set<String> accountsSet = new HashSet<>();
        accountsSet.add(helper.getAccountNum("Business Advantage Savings"));
        accountsSet.add(helper.getAccountNum("Progress Business Checking"));
        accountsSet.add(helper.getAccountNum("Pro Business Checking"));
        accountsSet.add(helper.getAccountNum("Business Platinum MasterCard"));
        accountsSet.add(helper.getAccountNum("Investment Account1"));
        try {
            businessDelegate.createCustomerActions(userId, helper.getContractId(), helper.getPrimaryCoreCustomerId(),
                    "a759860a-683a-4d41-81f8-fbd97d53b608", accountsSet, dcRequest.getHeaderMap());
        } catch (ApplicationException e) {
        	alert.prepareError(e.toString()).log();
        }
    }

    private void createWealthUserActionLimits(DataControllerRequest dcRequest, DemoDataWealth helper, String userId) {

        CustomerActionsBusinessDelegate businessDelegate =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(CustomerActionsBusinessDelegate.class);
        Set<String> accountsSet = new HashSet<>();
        accountsSet = new HashSet<>();
        accountsSet.add(helper.getAccountNum("Business Gold Credit Card"));
        accountsSet.add(helper.getAccountNum("Business Direct Term Deposit"));
        accountsSet.add(helper.getAccountNum("Business Advantage Term Loan"));
        accountsSet.add(helper.getAccountNum("Investment Account2"));
        try {
            businessDelegate.createCustomerActions(userId, helper.getContractId(), helper.getSecondaryCoreCustomerId(),
                    "4dd6183f-61d2-410f-8a4f-871af67ac933", accountsSet, dcRequest.getHeaderMap());
        } catch (ApplicationException e) {
        	alert.prepareError(e.toString()).log();
        }
    }

    private void createCustomerAccounts(DataControllerRequest dcRequest, String userId, DemoDataWealth helper)
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
        input.put("accountType", "CreditCard");

        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERACCOUNTS_CREATE);

        input.clear();
        input.put("id", HelperMethods.getNumericId() + "");
        input.put("Customer_id", userId);
        input.put("Account_id", helper.getAccountNum(helper.getInvestmentAccount1Data().get("AccountName")));
        input.put("contractId", helper.getContractId());
        input.put("coreCustomerId", helper.getPrimaryCoreCustomerId());
        input.put("AccountName",
                helper.getAccount7DemoData().get("AccountName") + "-X"
                        + helper.getAccountNum(helper.getInvestmentAccount1Data().get("AccountName")).substring(
                                helper.getAccountNum(helper.getInvestmentAccount1Data().get("AccountName")).length()
                                        - 4));
        input.put("IsOrganizationAccount", "true");
        input.put("IsOrgAccountUnLinked", "false");
        input.put("accountType", "Investment");

        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERACCOUNTS_CREATE);

    }

    public void createWealthCustomerAccounts(DataControllerRequest dcRequest, String userId,
            DemoDataWealth helper)
            throws HttpCallException {
        Map<String, String> input = new HashMap<>();
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
        input.put("accountType", "Loan");

        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERACCOUNTS_CREATE);

        input.clear();
        input.put("id", HelperMethods.getNumericId() + "");
        input.put("Customer_id", userId);
        input.put("Account_id", helper.getAccountNum(helper.getInvestmentAccount2Data().get("AccountName")));
        input.put("contractId", helper.getContractId());
        input.put("coreCustomerId", helper.getSecondaryCoreCustomerId());
        input.put("AccountName",
                helper.getAccount7DemoData().get("AccountName") + "-X"
                        + helper.getAccountNum(helper.getInvestmentAccount2Data().get("AccountName")).substring(
                                helper.getAccountNum(helper.getInvestmentAccount2Data().get("AccountName")).length()
                                        - 4));
        input.put("IsOrganizationAccount", "true");
        input.put("IsOrgAccountUnLinked", "false");
        input.put("accountType", "Investment");

        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERACCOUNTS_CREATE);
    }

    private String createRetailWealthCustomer(DataControllerRequest dcRequest, Map<String, String> inputmap,
            DemoDataWealth helper)
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

        input.put("UserName", newUserName + "RW");
        input.put("Password", newPassword);
        input.put("Bank_id", "1");

        input.put("isEnrolled", "true");
        input.put("CountryCode", inputmap.get("countryCode"));
        input.put("ValidDate", getDateString(usercal.getTime()));
        input.put("Lastlogintime", HelperMethods.getFormattedTimeStamp(new Date(), null));
        input.put("CurrentLoginTime", HelperMethods.getFormattedTimeStamp(new Date(), null));
        input.put("companyLegalUnit", "GB0010001");
        input.put("homeLegalEntity","GB0010001");
        input.put("defaultLegalEntity","GB0010001");

        if (inputmap.get("firstName") != null) {
            input.put("FirstName", inputmap.get("firstName"));
        }

        if (inputmap.get("lastName") != null) {
            input.put("LastName", inputmap.get("lastName"));
        }
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.USER_CREATE);

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
                    postParamMapGroup.put("Group_id", "a759860a-683a-4d41-81f8-fbd97d53b608");
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
        input.put("companyLegalUnit", "GB0010001");
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CONTRACT_CUSTOMERS_CREATE);

        return id;
    }

    private String createWealthCustomer(DataControllerRequest dcRequest, Map<String, String> inputmap,
            DemoDataWealth helper)
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
        Map<String, String> input = helper.getWealthCustomerData();

        String id = HelperMethods.generateUniqueCustomerId(dcRequest);
        input.put("id", id);

        input.put("UserName", newUserName + "W");
        input.put("Password", newPassword);
        input.put("Bank_id", "1");

        input.put("isEnrolled", "true");
        input.put("CountryCode", inputmap.get("countryCode"));
        input.put("ValidDate", getDateString(usercal.getTime()));
        input.put("Lastlogintime", HelperMethods.getFormattedTimeStamp(new Date(), null));
        input.put("CurrentLoginTime", HelperMethods.getFormattedTimeStamp(new Date(), null));
        input.put("companyLegalUnit", "GB0010001");
        input.put("homeLegalEntity","GB0010001");
        input.put("defaultLegalEntity","GB0010001");

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
                    postParamMapGroup.put("Group_id", "4dd6183f-61d2-410f-8a4f-871af67ac933");
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
        input.put("BackendId", helper.getSecondaryCoreCustomerId());
        input.put("BackendType", "CORE");

        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.BACKENDIDENTIFIER_CREATE);

        input.clear();
        input.put("id", HelperMethods.getNewId());
        input.put("contractId", helper.getContractId());
        input.put("customerId", id);
        input.put("coreCustomerId", helper.getSecondaryCoreCustomerId());
        input.put("isAdmin", "true");
        input.put("isOwner", "true");
        input.put("isPrimary", "false");
        input.put("isAuthSignatory", "true");
        input.put("companyLegalUnit", "GB0010001");
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CONTRACT_CUSTOMERS_CREATE);

        return id;
    }

    private String getDateString(Date d) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return formatter.format(d);
    }

    private void createMembershipData(DataControllerRequest dcRequest, DemoDataWealth helper) throws HttpCallException {
        String addr1 = HelperMethods.getNewId();
        String addr2 = HelperMethods.getNewId();
        Map<String, String> inputParams = new HashMap<>();
        inputParams = helper.getMembershipAddress1();
        inputParams.put("id", addr1);
        HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                URLConstants.ADDRESS_CREATE);
        inputParams.clear();
        inputParams = helper.getMembershipAddress2();
        inputParams.put("id", addr2);
        HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                URLConstants.ADDRESS_CREATE);
        inputParams.clear();
        long memberId1 = HelperMethods.getNumericId(7);
        long memberId2 = HelperMethods.getNumericId(7);
        inputParams = helper.getMembershipDetails1();
        inputParams.put("id", String.valueOf(memberId1));
        inputParams.put("addressId", addr1);
        inputParams.put("name", helper.getUserName());
        inputParams.put("taxId", String.valueOf(HelperMethods.getNumericId(9)));
        helper.setPrimaryCoreCustomerId(inputParams.get("id"));
        helper.setCoreCustomerName(inputParams.get("name"));
        HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                URLConstants.MEMBERSHIP_CREATE);
        inputParams.clear();
        inputParams = helper.getMembershipDetails2();
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
        inputParams = helper.getMembershipDetails3();
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
        inputParams = helper.getMembershipDetails4();
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
        inputParams = helper.getMembershipDetails5();
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
        inputParams = helper.getMembershipDetails6();
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

    }

    private void createRetailWealthDemoUserContract(String methodId, Object[] inputArray,
            DataControllerRequest dcRequest,
            DataControllerResponse dcResponse, DemoDataWealth helper) throws HttpCallException, ApplicationException {

        createMembershipData(dcRequest, helper);

        createAccounts(dcRequest, "", helper, new HashMap<>());

        Map<String, String> contractPayloadMap = new HashMap<>();
        contractPayloadMap.put("contractName", "Temenos Global - " + helper.getUserName() + "RW");
        contractPayloadMap.put("serviceDefinitionName", "Retail and wealth online banking");
        contractPayloadMap.put("serviceDefinitionId", "f85d8392-9afe-4128-b23e-a370f138784f");
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
        contractCustomer.addProperty("coreCustomerName", helper.getUserName() + "RW");
        JsonArray accounts = new JsonArray();
        accounts.add(helper.getValidAccountJsonObject(helper.getAccount1DemoData(), helper));
        accounts.add(helper.getValidAccountJsonObject(helper.getAccount2DemoData(), helper));
        accounts.add(helper.getValidAccountJsonObject(helper.getAccount3DemoData(), helper));
        accounts.add(helper.getValidAccountJsonObject(helper.getAccount4DemoData(), helper));
        accounts.add(helper.getValidAccountJsonObject(helper.getInvestmentAccount1Data(), helper));
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

    private void createWealthDemoUserContract(String methodId, Object[] inputArray,
            DataControllerRequest dcRequest,
            DataControllerResponse dcResponse, DemoDataWealth helper) throws HttpCallException, ApplicationException {

        Map<String, String> contractPayloadMap = new HashMap<>();
        contractPayloadMap.put("contractName", "Temenos Global - " + helper.getUserName() + "W");
        contractPayloadMap.put("serviceDefinitionName", "Wealth Online Banking");
        contractPayloadMap.put("serviceDefinitionId", "90356097-7fdf-4b8c-89bd-8a1065338a97");
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
        contractCustomer.addProperty("coreCustomerId", helper.getSecondaryCoreCustomerId());
        contractCustomer.addProperty("coreCustomerName", helper.getUserName() + "W");
        JsonArray accounts = new JsonArray();
        accounts.add(helper.getValidAccountJsonObject(helper.getAccount5DemoData(), helper));
        accounts.add(helper.getValidAccountJsonObject(helper.getAccount6DemoData(), helper));
        accounts.add(helper.getValidAccountJsonObject(helper.getAccount7DemoData(), helper));
        accounts.add(helper.getValidAccountJsonObject(helper.getInvestmentAccount2Data(), helper));
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

    private void createAccounts(DataControllerRequest dcRequest, String userId, DemoDataWealth helper,
            Map<String, String> inputmap)
            throws HttpCallException {

        Map<String, String> input = new HashMap<>();

        String accountNum = null;
        String accountName = null;
        String newUserName = inputmap.get("newUsername");
        Calendar cal = Calendar.getInstance();
        input.clear();
        input = helper.getAccount1DemoData();
        accountNum = helper.getDummyAccountId();
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
        accountNum = helper.getDummyAccountId();
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
        accountNum = helper.getDummyAccountId();
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
        accountNum = helper.getDummyAccountId();
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
        accountNum = helper.getDummyAccountId();
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
        accountNum = helper.getDummyAccountId();
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
        accountNum = helper.getDummyAccountId();
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

        input = helper.getInvestmentAccount1Data();
        accountNum = helper.getDummyAccountId();
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

        input = helper.getInvestmentAccount2Data();
        accountNum = helper.getDummyAccountId();
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

    }
}
