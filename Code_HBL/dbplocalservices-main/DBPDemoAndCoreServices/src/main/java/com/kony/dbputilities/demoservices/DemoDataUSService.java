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
import com.kony.dbputilities.customersecurityservices.CreateCustomerCommunication;
import com.kony.dbputilities.customersecurityservices.PasswordHistoryManagement;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.AdminUtil;
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
import com.temenos.dbx.product.contract.backenddelegate.api.ContractBackendDelegate;
import com.temenos.dbx.product.contract.resource.api.ContractResource;
import com.temenos.dbx.product.dto.CustomerLegalEntityDTO;
import com.temenos.dbx.product.payeeservices.businessdelegate.api.BillPayPayeeBusinessDelegate;
import com.temenos.dbx.product.payeeservices.businessdelegate.api.InterBankPayeeBusinessDelegate;
import com.temenos.dbx.product.payeeservices.businessdelegate.api.InternationalPayeeBusinessDelegate;
import com.temenos.dbx.product.payeeservices.businessdelegate.api.IntraBankPayeeBusinessDelegate;
import com.temenos.dbx.product.payeeservices.businessdelegate.api.P2PPayeeBusinessDelegate;
import com.temenos.dbx.product.payeeservices.dto.BillPayPayeeDTO;
import com.temenos.dbx.product.payeeservices.dto.InterBankPayeeDTO;
import com.temenos.dbx.product.payeeservices.dto.InternationalPayeeDTO;
import com.temenos.dbx.product.payeeservices.dto.IntraBankPayeeDTO;
import com.temenos.dbx.product.payeeservices.dto.P2PPayeeDTO;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.CustomerActionsBusinessDelegate;
import com.temenos.dbx.product.utils.DTOUtils;
import com.temenos.dbx.product.utils.ThreadExecutor;

public class DemoDataUSService implements JavaService2, DemoDataService {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputmap = HelperMethods.getInputParamMap(inputArray);
        DemoDataUSNew helper = new DemoDataUSNew();
        helper.init(dcRequest);
        helper.setUserName(inputmap.get("newUsername"));
        createDemoUserContract(methodId, inputArray, dcRequest, dcResponse, helper);
        String userId = createCustomer(dcRequest, inputmap, helper);
        if (StringUtils.isBlank(userId)) {
            HelperMethods.setValidationMsg("Username already exists.", dcRequest, result);
            result.addStringParam("errmsg", "Username already exists.");
            return result;
        }
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
        createTransactionsForLoanAccount(dcRequest, helper);
        createTransactionsForSavingsAccount(dcRequest, userId, helper);
        createTransactionsForDepositAccount(dcRequest, helper);
        createTransactionsForCheckingAccount(dcRequest, helper);
        createTransactionsForCreditCardAccount(dcRequest, helper);
        createTransactionsForCard(dcRequest, helper);
        createInvestmentTransactions(dcRequest, helper);
        /**
         * transactions ends
         */
        updateDemoCustomerDefaultAccounts(dcRequest, userId, helper);
        createMessages(dcRequest, helper);
        createAccountStatements(dcRequest, helper);
        createCardStatements(dcRequest, helper);
        DemoDataWealthService demoDataWealthService = new DemoDataWealthService();
		demoDataWealthService.createEntryInCustomerLegalEntity(dcRequest, userId);

        DemoDataSBService smallBusinessDemoCreation = new DemoDataSBService();
        smallBusinessDemoCreation.invoke(methodId, inputArray, dcRequest, dcResponse);

        // LW65h$S&H@=>
        sendEmail("InfinityDemos@temenos.com", "Your Credentials for Infinity Digital Banking", inputmap.get("email"),
                inputmap.get("newUsername"), inputmap.get("newPassword"), inputmap.get("firstName"),
                inputmap.get("countryCode"), dcRequest);
        result.addStringParam("UserName", inputmap.get("newUsername") + "," + inputmap.get("newUsername") + "SB");
        return result;
    }

    private void createInvestmentTransactions(DataControllerRequest dcRequest, DemoDataUSNew helper) {
        try {
            Map<String, String> input = helper.getInvestmentTransaction1();
            insertTransaction(input, dcRequest, helper);
            input = helper.getInvestmentTransaction2();
            insertTransaction(input, dcRequest, helper);
        } catch (Exception e) {
            alert.prepareError("Exception occured while creating investment transactions").log();
        }
    }

    private void createLimitGroupLimits(String userId, DemoDataUSNew helper, DataControllerRequest dcRequest) {
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

    public String getAccessToken(DataControllerRequest dcRequest) throws HttpCallException {
        return AdminUtil.getAdminToken(dcRequest);
    }

    private void createUserBill(DataControllerRequest dcRequest, DemoDataUSNew helper) throws HttpCallException {
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

    private void createTransactionsForSavingsAccount(DataControllerRequest dcRequest, String userId,
            DemoDataUSNew helper) throws HttpCallException {
        Map<String, String> input = helper.getCheckDeposit_SAV2DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getCardlessWithdrawl_SAVDemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getExternalTransfer_SAV3DemoData();
        input.put("toExternalAccountNumber", helper.getExternalAccountNum(input.get("beneficiaryName")));
        insertTransaction(input, dcRequest, helper);

        input = helper.getRemoteCheckDeposit_SAV4DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getInterest_SAV6DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getRemoteCheckDeposit_SAV7DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getCredit_SAV8DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getFee_SAV9DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getExternalTransfer_SAV11DemoData();
        input.put("toExternalAccountNumber", helper.getExternalAccountNum(input.get("beneficiaryName")));
        insertTransaction(input, dcRequest, helper);

        input = helper.getLoan_SAV12DemoData();
        insertTransaction(input, dcRequest, helper);
    }

    private void createTransactionsForDepositAccount(DataControllerRequest dcRequest, DemoDataUSNew helper)
            throws HttpCallException {
        Map<String, String> input = helper.getInterestCredit_Deposit1DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getInterestCredit_Deposit2DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getInterestCredit_Deposit3DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getDeposit_Deposit4DemoData();
        insertTransaction(input, dcRequest, helper);
    }

    private void createTransactionsForCheckingAccount(DataControllerRequest dcRequest, DemoDataUSNew helper)
            throws HttpCallException {

        List<Map<String, String>> dataList = getCheckingAccountData(helper);
        for (Map<String, String> data : dataList) {
            insertTransaction(data, dcRequest, helper);
        }
    }

    private void createTransactionsForCreditCardAccount(DataControllerRequest dcRequest, DemoDataUSNew helper)
            throws HttpCallException {

        List<Map<String, String>> dataList = getCreditAccountData(helper);
        for (Map<String, String> data : dataList) {
            insertTransaction(data, dcRequest, helper);
        }
    }

    @SuppressWarnings("unchecked")
    private void createTransactionsForCard(DataControllerRequest dcRequest, DemoDataUSNew helper)
            throws HttpCallException {
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

    private void insertTransaction(Map<String, String> input, DataControllerRequest dcRequest, DemoDataUSNew helper)
            throws HttpCallException {
        input.put("toAccountNumber", helper.getAccountNum(input.get("toAccountNumber")));
        input.put("fromAccountNumber", helper.getAccountNum(input.get("fromAccountNumber")));
        input.put("toExternalAccountNumber", input.get("toExternalAccountNumber"));
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
        HelperMethods.removeNullValues(input);
        insertServiceName(input);
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

    private void createTransactionsForLoanAccount(DataControllerRequest dcRequest, DemoDataUSNew helper)
            throws HttpCallException {
        Map<String, String> input = helper.getLoanPayment_Loan1DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getLoanPayment_Loan2DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getLoanPayment_Loan3DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getTax_Loan4DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getLoanPayment_Loan5DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getLoanPayment_Loan6DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getLoanPayment_Loan7DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getLoanPayment_Loan8DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getLoanPayment_Loan9DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getLoanPayment_Loan10DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getLoanPayment_Loan11DemoData();
        insertTransaction(input, dcRequest, helper);

        input = helper.getCredit_Loan12DemoData();
        insertTransaction(input, dcRequest, helper);
    }

    private String createCustomer(DataControllerRequest dcRequest, Map<String, String> inputmap, DemoDataUSNew helper)
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

        input.put("UserName", newUserName);
        input.put("Password", newPassword);
        input.put("Status_id", "SID_CUS_ACTIVE");

        input.put("Bank_id", "1");

        input.put("isEnrolled", "true");
        input.put("CountryCode", "United States of America");
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
        Result result = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.USER_CREATE);
        String userid = HelperMethods.getFieldValue(result, "id");
        CustomerLegalEntityDTO customerLegalEntityDTO = new CustomerLegalEntityDTO();
		customerLegalEntityDTO.setId(HelperMethods.getNewId());
		customerLegalEntityDTO.setCustomer_id(id);
		customerLegalEntityDTO.setNew(true);
		customerLegalEntityDTO.setStatus_id(HelperMethods.getCustomerStatus().get("ACTIVE"));
		customerLegalEntityDTO.setLegalEntityId("GB0010001");
		customerLegalEntityDTO.persist(DTOUtils.getParameterMap(customerLegalEntityDTO, true), dcRequest.getHeaderMap());
		
        PasswordHistoryManagement pm = new PasswordHistoryManagement(dcRequest, false);
        pm.makePasswordEntry(dcRequest, userid, newPassword);

        if (StringUtils.isBlank(HelperMethods.getFieldValue(result, "id"))) {
            return null;
        }
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

        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERPREFERENCE_CREATE);

        try {
            ThreadExecutor.getExecutor(dcRequest).execute(new Callable<Result>() {
                @Override
                public Result call() throws ApplicationException {
                    Map<String, String> postParamMapGroup = new HashMap<>();
                    postParamMapGroup.put("Customer_id", id);
                    postParamMapGroup.put("Group_id", "DEFAULT_GROUP");
                    postParamMapGroup.put("coreCustomerId", helper.getPrimaryCoreCustomerId());
                    postParamMapGroup.put("contractId", helper.getContractId());
                    postParamMapGroup.put("companyLegalUnit","GB0010001");
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
                    postParamMapGroup.put("Group_id", "DEFAULT_GROUP");
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
        input.put("id", userid);

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

        return userid;
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

    private void createCustomerAccounts(DataControllerRequest dcRequest, String userId, DemoDataUSNew helper)
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

        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERACCOUNTS_CREATE);
        input.put("accountType", "CreditCard");

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
        input.put("accountType", "Deposit");

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

        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERACCOUNTS_CREATE);

        input.clear();
        input.put("id", HelperMethods.getNumericId() + "");
        input.put("Customer_id", userId);
        input.put("Account_id", helper.getAccountNum(helper.getInvestmentSavingsAccount().get("AccountName")));
        input.put("contractId", helper.getContractId());
        input.put("coreCustomerId", helper.getPrimaryCoreCustomerId());
        input.put("AccountName",
                helper.getInvestmentSavingsAccount().get("AccountName") + "-X"
                        + helper.getAccountNum(helper.getInvestmentSavingsAccount().get("AccountName")).substring(
                                helper.getAccountNum(helper.getInvestmentSavingsAccount().get("AccountName")).length()
                                        - 4));

        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERACCOUNTS_CREATE);

        input.clear();
        input.put("id", HelperMethods.getNumericId() + "");
        input.put("Customer_id", userId);
        input.put("Account_id", helper.getAccountNum(helper.getInvestmentCashAccount1().get("AccountName")));
        input.put("contractId", helper.getContractId());
        input.put("coreCustomerId", helper.getPrimaryCoreCustomerId());
        input.put("AccountName",
                helper.getInvestmentCashAccount1().get("AccountName") + "-X"
                        + helper.getAccountNum(helper.getInvestmentCashAccount1().get("AccountName")).substring(
                                helper.getAccountNum(helper.getInvestmentCashAccount1().get("AccountName")).length()
                                        - 4));

        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERACCOUNTS_CREATE);

        input.clear();
        input.put("id", HelperMethods.getNumericId() + "");
        input.put("Customer_id", userId);
        input.put("Account_id", helper.getAccountNum(helper.getInvestmentCashAccount2().get("AccountName")));
        input.put("contractId", helper.getContractId());
        input.put("coreCustomerId", helper.getPrimaryCoreCustomerId());
        input.put("AccountName",
                helper.getInvestmentCashAccount2().get("AccountName") + "-X"
                        + helper.getAccountNum(helper.getInvestmentCashAccount2().get("AccountName")).substring(
                                helper.getAccountNum(helper.getInvestmentCashAccount2().get("AccountName")).length()
                                        - 4));

        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERACCOUNTS_CREATE);

    }

    private void createAccounts(DataControllerRequest dcRequest, String userId, String userName, DemoDataUSNew helper)
            throws HttpCallException {
        String accountNum = null;
        Calendar cal = Calendar.getInstance();
        Map<String, String> input = helper.getAccount1DemoData();
        accountNum = getDummyAccountId();
        helper.addAccountNum(input.get("Name"), accountNum);
        cal.add(Calendar.DATE, -1);
        input.put("Account_id", accountNum);
        // input.put("AccountName",input.get("AccountName") + "-X" +
        // accountNum.substring(accountNum.length()-4));
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
        jointHolderNames = "[" + "{\"username\": \"" + "john.Doe" + "\", \"fullname\": \"" + accHolder2 + "\"}" + "]";
        input.put("JointHolders", jointHolderNames);
        input.put("DividendLastPaidDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        input.put("EStatementmentEnable", "false");
        input.put("ownership", "Single");

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
        helper.addAccountNum(input.get("Name"), accountNum);
        cal.add(Calendar.DATE, -1);
        input.put("Account_id", accountNum);
        // input.put("AccountName",input.get("AccountName") + "-X" +
        // accountNum.substring(accountNum.length()-4));
        input.put("User_id", userId);
        input.put("Type_id", helper.getAccountId(input.get("Type_id")));
        input.put("Product_id", "11");
        input.put("Bank_id", "1");
        input.put("LastPaymentDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        accHolderName = "{\"username\": \"" + userName + "\", \"fullname\": \"" + input.get("AccountHolder") + "\"}";
        input.put("AccountHolder", accHolderName);
        accHolder2 = input.get("AccountHolder2");
        jointHolderNames = "[" + "{\"username\": \"" + "john.Doe" + "\", \"fullname\": \"" + accHolder2 + "\"}" + "]";
        input.put("JointHolders", jointHolderNames);
        input.put("DividendLastPaidDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        input.put("EStatementmentEnable", "false");
        input.put("ownership", "Single");

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
        helper.addAccountNum(input.get("Name"), accountNum);
        cal.add(Calendar.DATE, -1);
        input.put("Account_id", accountNum);
        // input.put("AccountName",input.get("AccountName") + "-X" +
        // accountNum.substring(accountNum.length()-4));
        input.put("User_id", userId);
        input.put("Type_id", helper.getAccountId(input.get("Type_id")));
        input.put("Product_id", "2");
        input.put("Bank_id", "1");
        input.put("LastPaymentDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        accHolderName = "{\"username\": \"" + userName + "\", \"fullname\": \"" + input.get("AccountHolder") + "\"}";
        input.put("AccountHolder", accHolderName);
        accHolder2 = input.get("AccountHolder2");
        jointHolderNames = "[" + "{\"username\": \"" + "john.Doe" + "\", \"fullname\": \"" + accHolder2 + "\"}" + "]";
        input.put("JointHolders", jointHolderNames);
        input.put("DividendLastPaidDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        input.put("EStatementmentEnable", "false");
        input.put("ownership", "Single");

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
        helper.addAccountNum(input.get("Name"), accountNum);
        cal.add(Calendar.DATE, -1);
        input.put("Account_id", accountNum);
        // input.put("AccountName",input.get("AccountName") + "-X" +
        // accountNum.substring(accountNum.length()-4));
        input.put("User_id", userId);
        input.put("Type_id", helper.getAccountId(input.get("Type_id")));
        input.put("Product_id", "5");
        input.put("Bank_id", "1");
        input.put("LastPaymentDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        accHolderName = "{\"username\": \"" + userName + "\", \"fullname\": \"" + input.get("AccountHolder") + "\"}";
        input.put("AccountHolder", accHolderName);
        accHolder2 = input.get("AccountHolder2");
        jointHolderNames = "[" + "{\"username\": \"" + "john.Doe" + "\", \"fullname\": \"" + accHolder2 + "\"}" + "]";
        input.put("JointHolders", jointHolderNames);
        input.put("DividendLastPaidDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        input.put("EStatementmentEnable", "false");
        input.put("ownership", "Single");

        HelperMethods.removeNullValues(input);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_ACCOUNTS_CREATE);

        input.clear();
        input.put("id", HelperMethods.getNewId());
        input.put("membershipId", helper.getSecondaryCoreCustomerId());
        input.put("accountId", accountNum);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.MEMBERSHIP_ACCOUNTS_CREATE);

        input = helper.getAccount5DemoData();
        accountNum = getDummyAccountId();
        helper.addAccountNum(input.get("Name"), accountNum);
        cal.add(Calendar.DATE, -1);
        input.put("Account_id", accountNum);
        // input.put("AccountName",input.get("AccountName") + "-X" +
        // accountNum.substring(accountNum.length()-4));
        input.put("User_id", userId);
        input.put("Type_id", helper.getAccountId(input.get("Type_id")));
        input.put("Product_id", "4");
        input.put("Bank_id", "1");
        input.put("LastPaymentDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        accHolderName = "{\"username\": \"" + userName + "\", \"fullname\": \"" + input.get("AccountHolder") + "\"}";
        input.put("AccountHolder", accHolderName);
        accHolder2 = input.get("AccountHolder2");
        jointHolderNames = "[" + "{\"username\": \"" + "john.Doe" + "\", \"fullname\": \"" + accHolder2 + "\"}" + "]";
        input.put("JointHolders", jointHolderNames);
        input.put("DividendLastPaidDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        input.put("EStatementmentEnable", "false");
        input.put("ownership", "Single");

        HelperMethods.removeNullValues(input);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_ACCOUNTS_CREATE);

        input.clear();
        input.put("id", HelperMethods.getNewId());
        input.put("membershipId", helper.getSecondaryCoreCustomerId());
        input.put("accountId", accountNum);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.MEMBERSHIP_ACCOUNTS_CREATE);

        input = helper.getInvestmentSavingsAccount();
        accountNum = getDummyAccountId();
        helper.addAccountNum(input.get("Name"), accountNum);
        cal.add(Calendar.DATE, -1);
        input.put("Account_id", accountNum);
        // input.put("AccountName",input.get("AccountName") + "-X" +
        // accountNum.substring(accountNum.length()-4));
        input.put("User_id", userId);
        input.put("Type_id", "2");
        input.put("Product_id", "4");
        input.put("Bank_id", "1");
        input.put("LastPaymentDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        accHolderName = "{\"username\": \"" + userName + "\", \"fullname\": \"" + input.get("AccountHolder") + "\"}";
        input.put("AccountHolder", accHolderName);
        accHolder2 = input.get("AccountHolder2");
        jointHolderNames = "[" + "{\"username\": \"" + "john.Doe" + "\", \"fullname\": \"" + accHolder2 + "\"}" + "]";
        input.put("JointHolders", jointHolderNames);
        input.put("DividendLastPaidDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        input.put("EStatementmentEnable", "false");
        input.put("ownership", "Single");

        HelperMethods.removeNullValues(input);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_ACCOUNTS_CREATE);

        input.clear();
        input.put("id", HelperMethods.getNewId());
        input.put("membershipId", helper.getPrimaryCoreCustomerId());
        input.put("accountId", accountNum);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.MEMBERSHIP_ACCOUNTS_CREATE);

        input = helper.getInvestmentCashAccount1();
        accountNum = getDummyAccountId();
        helper.addAccountNum(input.get("Name"), accountNum);
        cal.add(Calendar.DATE, -1);
        input.put("Account_id", accountNum);
        // input.put("AccountName",input.get("AccountName") + "-X" +
        // accountNum.substring(accountNum.length()-4));
        input.put("User_id", userId);
        input.put("Type_id", "8");
        input.put("Product_id", "4");
        input.put("Bank_id", "1");
        input.put("LastPaymentDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        accHolderName = "{\"username\": \"" + userName + "\", \"fullname\": \"" + input.get("AccountHolder") + "\"}";
        input.put("AccountHolder", accHolderName);
        accHolder2 = input.get("AccountHolder2");
        jointHolderNames = "[" + "{\"username\": \"" + "john.Doe" + "\", \"fullname\": \"" + accHolder2 + "\"}" + "]";
        input.put("JointHolders", jointHolderNames);
        input.put("DividendLastPaidDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        input.put("EStatementmentEnable", "false");
        input.put("ownership", "Single");

        HelperMethods.removeNullValues(input);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_ACCOUNTS_CREATE);

        input.clear();
        input.put("id", HelperMethods.getNewId());
        input.put("membershipId", helper.getPrimaryCoreCustomerId());
        input.put("accountId", accountNum);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.MEMBERSHIP_ACCOUNTS_CREATE);

        input = helper.getInvestmentCashAccount2();
        accountNum = getDummyAccountId();
        helper.addAccountNum(input.get("Name"), accountNum);
        cal.add(Calendar.DATE, -1);
        input.put("Account_id", accountNum);
        // input.put("AccountName",input.get("AccountName") + "-X" +
        // accountNum.substring(accountNum.length()-4));
        input.put("User_id", userId);
        input.put("Type_id", "8");
        input.put("Product_id", "4");
        input.put("Bank_id", "1");
        input.put("LastPaymentDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        accHolderName = "{\"username\": \"" + userName + "\", \"fullname\": \"" + input.get("AccountHolder") + "\"}";
        input.put("AccountHolder", accHolderName);
        accHolder2 = input.get("AccountHolder2");
        jointHolderNames = "[" + "{\"username\": \"" + "john.Doe" + "\", \"fullname\": \"" + accHolder2 + "\"}" + "]";
        input.put("JointHolders", jointHolderNames);
        input.put("DividendLastPaidDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), null));
        input.put("EStatementmentEnable", "false");
        input.put("ownership", "Single");

        HelperMethods.removeNullValues(input);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_ACCOUNTS_CREATE);

        input.clear();
        input.put("id", HelperMethods.getNewId());
        input.put("membershipId", helper.getPrimaryCoreCustomerId());
        input.put("accountId", accountNum);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.MEMBERSHIP_ACCOUNTS_CREATE);
    }

    private void createPayees(DataControllerRequest dcRequest, String userId, DemoDataUSNew helper)
            throws HttpCallException {

        Result rs = null;
        Map<String, String> input = helper.getPayee1DemoData();
        input.put("User_Id", userId);
        input.put("billermaster_id", "6");
        input.put("transitDays", "3");
        input.put("Id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_PAYEE_CREATE);
        helper.addPayee("payee1", getFieldValue(rs, "Id"));
        createBillPayInfinityBeneficiary(userId, getFieldValue(rs, "Id"), helper);

        input = helper.getPayee2DemoData();
        input.put("User_Id", userId);
        input.put("billermaster_id", "1");
        input.put("transitDays", "3");
        input.put("Id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_PAYEE_CREATE);
        helper.addPayee("payee2", getFieldValue(rs, "Id"));
        createBillPayInfinityBeneficiary(userId, getFieldValue(rs, "Id"), helper);

        input = helper.getPayee3DemoData();
        input.put("User_Id", userId);
        input.put("billermaster_id", "1");
        input.put("transitDays", "3");
        input.put("Id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_PAYEE_CREATE);
        helper.addPayee("payee3", getFieldValue(rs, "Id"));
        createBillPayInfinityBeneficiary(userId, getFieldValue(rs, "Id"), helper);

        input = helper.getPayee4DemoData();
        input.put("User_Id", userId);
        input.put("billermaster_id", "4");
        input.put("transitDays", "3");
        input.put("Id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_PAYEE_CREATE);
        helper.addPayee("payee4", getFieldValue(rs, "Id"));
        createBillPayInfinityBeneficiary(userId, getFieldValue(rs, "Id"), helper);

        input = helper.getPayee5DemoData();
        input.put("User_Id", userId);
        input.put("billermaster_id", "8");
        input.put("transitDays", "3");
        input.put("Id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_PAYEE_CREATE);
        helper.addPayee("payee5", getFieldValue(rs, "Id"));
        createBillPayInfinityBeneficiary(userId, getFieldValue(rs, "Id"), helper);

        input = helper.getPayee6DemoData();
        input.put("User_Id", userId);
        input.put("billermaster_id", "7");
        input.put("transitDays", "3");
        input.put("Id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_PAYEE_CREATE);
        helper.addPayee("payee6", getFieldValue(rs, "Id"));
        createBillPayInfinityBeneficiary(userId, getFieldValue(rs, "Id"), helper);

    }

    private void createPayPersons(DataControllerRequest dcRequest, String userId, DemoDataUSNew helper)
            throws HttpCallException {

        Result rs = null;
        Map<String, String> input = helper.getPerson1DemoData();
        input.put("User_id", userId);
        input.put("id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_PAYPERSON_CREATE);
        helper.addPerson("person1", getFieldValue(rs, "id"));
        createP2PInfinityBeneficiary(userId, getFieldValue(rs, "id"), helper);

        input = helper.getPerson2DemoData();
        input.put("User_id", userId);
        input.put("id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_PAYPERSON_CREATE);
        helper.addPerson("person2", getFieldValue(rs, "id"));
        createP2PInfinityBeneficiary(userId, getFieldValue(rs, "id"), helper);

        input = helper.getPerson3DemoData();
        input.put("User_id", userId);
        input.put("id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_PAYPERSON_CREATE);
        helper.addPerson("person3", getFieldValue(rs, "id"));
        createP2PInfinityBeneficiary(userId, getFieldValue(rs, "id"), helper);

        input = helper.getPerson4DemoData();
        input.put("User_id", userId);
        input.put("id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_PAYPERSON_CREATE);
        helper.addPerson("person4", getFieldValue(rs, "id"));
        createP2PInfinityBeneficiary(userId, getFieldValue(rs, "id"), helper);
    }

    private void createExternalAccounts(DataControllerRequest dcRequest, String userId, DemoDataUSNew helper)
            throws HttpCallException {
        Result rs = null;

        Map<String, String> input = helper.getExtAccount1DemoData();
        input.put("User_id", userId);
        helper.addExternalAccountNum(input.get("beneficiaryName"), input.get("accountNumber"));
        input.put("Id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_EXT_ACCOUNTS_CREATE);
        createInterBankInfinityBeneficiary(userId, getFieldValue(rs, "Id"), helper);

        input = helper.getExtAccount2DemoData();
        input.put("User_id", userId);
        helper.addExternalAccountNum(input.get("beneficiaryName"), input.get("accountNumber"));
        input.put("Id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_EXT_ACCOUNTS_CREATE);
        createInterBankInfinityBeneficiary(userId, getFieldValue(rs, "Id"), helper);

        input = helper.getExtAccount3DemoData();
        input.put("User_id", userId);
        helper.addExternalAccountNum(input.get("beneficiaryName"), input.get("accountNumber"));
        input.put("Id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_EXT_ACCOUNTS_CREATE);
        createInterBankInfinityBeneficiary(userId, getFieldValue(rs, "Id"), helper);

        input = helper.getExtAccount4DemoData();
        input.put("User_id", userId);
        helper.addExternalAccountNum(input.get("beneficiaryName"), input.get("accountNumber"));
        input.put("Id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_EXT_ACCOUNTS_CREATE);
        createIntraBankInfinityBeneficiary(userId, getFieldValue(rs, "Id"), helper);

        input = helper.getExtAccount5DemoData();
        input.put("User_id", userId);
        helper.addExternalAccountNum(input.get("beneficiaryName"), input.get("accountNumber"));
        input.put("Id", HelperMethods.getRandomNumericString(8));
        rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_EXT_ACCOUNTS_CREATE);
        createInternationalInfinityBeneficiary(userId, getFieldValue(rs, "Id"), helper);
    }

    private void createCards(DataControllerRequest dcRequest, String userId, DemoDataUSNew helper)
            throws HttpCallException {
        Map<String, String> input = helper.getCard1DemoData();

        input.put("User_id", userId);
        input.put("account_id", helper.getAccountNum("Rewards Savings"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_CARDS_CREATE);

        input = helper.getCard2DemoData();
        input.put("User_id", userId);
        input.put("account_id", helper.getAccountNum("Core Checking"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_CARDS_CREATE);

        input = helper.getCard5DemoData();
        input.put("User_id", userId);
        input.put("account_id", helper.getAccountNum("Freedom Credit Card"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_CARDS_CREATE);

        input = helper.getCard3DemoData();
        input.put("User_id", userId);
        input.put("account_id", helper.getAccountNum("Freedom Credit Card"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_CARDS_CREATE);

        input = helper.getCard4DemoData();
        input.put("User_id", userId);
        input.put("account_id", helper.getAccountNum("Freedom Credit Card"));
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
        input.put("account_id", helper.getAccountNum("Rewards Savings"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_CARDS_CREATE);

        input = helper.getCard7DemoData();
        input.put("User_id", userId);
        input.put("account_id", helper.getAccountNum("Freedom Credit Card"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_CARDS_CREATE);

        input = helper.getCard8DemoData();
        input.put("User_id", userId);
        input.put("account_id", helper.getAccountNum("Core Checking"));
        input.put("expirationDate", expiryDate);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_CARDS_CREATE);

    }

    private void createUserAlerts(DataControllerRequest dcRequest, String userId, DemoDataUSNew helper)
            throws HttpCallException {

        Map<String, String> input = helper.geUserAlertDemoData();
        input.put("User_id", userId);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.DMS_ALERTS_CREATE);
    }

    private void createAccountAlerts(DataControllerRequest dcRequest, String userId, DemoDataUSNew helper)
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

    private void createPhone(DataControllerRequest dcRequest, String userId, DemoDataUSNew helper)
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void updateDemoCustomerDefaultAccounts(DataControllerRequest dcRequest, String userId, DemoDataUSNew helper)
            throws HttpCallException {
        Map<String, String> input = new HashMap();

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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void createMessages(DataControllerRequest dcRequest, DemoDataUSNew helper) throws HttpCallException {
        Map<String, String> input = new HashMap();
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void createAccountStatements(DataControllerRequest dcRequest, DemoDataUSNew helper)
            throws HttpCallException {
        Calendar date1 = new GregorianCalendar(2017, Calendar.NOVEMBER, 1);
        Calendar date2 = new GregorianCalendar(2017, Calendar.DECEMBER, 1);

        Map<String, String> input = new HashMap();
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

    private List<Map<String, String>> getCreditAccountData(DemoDataUSNew helper) {
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
         * helper.getBillPay_CC203DemoData());
         */
        return Arrays.asList(helper.getPOS_CC1DemoData(),
                // helper.getInternalTransfer_CC2DemoData(),
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

    private List<Map<String, String>> getCheckingAccountData(DemoDataUSNew helper) {
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
         * helper.getBillPay_Checking206DemoData(), helper.getBillPay_Checking207DemoData(),
         * helper.getInternalTransfer_Checking208DemoData(), helper.getP2P_Checking209DemoData(),
         * helper.getP2P_Checking210DemoData())
         */
        return Arrays.asList(helper.getCredit_Checking1DemoData(), helper.getCardLessWithDrawl_Checking1DemoData(),
                helper.getCardLessWithDrawl_Checking2DemoData(),
                // helper.getInternalTransfer_Checking2DemoData(),
                // helper.getInternalTransfer_Checking3DemoData(),
                // helper.getInternalTransfer_Checking4DemoData(),
                helper.getCheckDeposit_Checking5DemoData(), helper.getP2P_Checking6DemoData(),
                helper.getInterest_Checking7DemoData(), helper.getWithdrawal_Checking8DemoData(),
                helper.getP2P_Checking9DemoData(), helper.getInternetTransaction_Checking10DemoData(),
                helper.getPOS_Checking11DemoData(), helper.getPOS_Checking12DemoData(),
                helper.getPOS_Checking13DemoData(), helper.getInternetTransaction_Checking14DemoData(),
                helper.getInternetTransaction_Checking15DemoData(), helper.getPOS_Checking16DemoData(),
                helper.getCredit_Checking17DemoData(), helper.getBillPay_Checking18DemoData(),
                helper.getPOS_Checking19DemoData(), helper.getInternetTransaction_Checking20DemoData(),
                helper.getPOS_Checking21DemoData(), helper.getPOS_Checking22DemoData(),
                helper.getPOS_Checking23DemoData(), helper.getPOS_Checking24DemoData(),
                helper.getInternetTransaction_Checking25DemoData(), helper.getPOS_Checking26DemoData(),
                helper.getPOS_Checking27DemoData(), helper.getPOS_Checking28DemoData(),
                helper.getInternetTransaction_Checking29DemoData(), helper.getInternetTransaction_Checking30DemoData(),
                helper.getPOS_Checking31DemoData(), helper.getCredit_Checking32DemoData(),
                helper.getBillPay_Checking33DemoData(), helper.getPOS_Checking34DemoData(),
                helper.getInternetTransaction_Checking35DemoData(), helper.getPOS_Checking36DemoData(),
                helper.getPOS_Checking37DemoData(), helper.getPOS_Checking38DemoData(),
                helper.getInternetTransaction_Checking39DemoData(), helper.getInternetTransaction_Checking40DemoData(),
                helper.getPOS_Checking41DemoData(), helper.getCredit_Checking42DemoData(),
                helper.getBillPay_Checking43DemoData(), helper.getPOS_Checking44DemoData(),
                helper.getInternetTransaction_Checking45DemoData(), helper.getPOS_Checking46DemoData(),
                helper.getPOS_Checking47DemoData(), helper.getPOS_Checking48DemoData(),
                helper.getInternetTransaction_Checking49DemoData(), helper.getInternetTransaction_Checking50DemoData());
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

    private String getDummyAccountId() {
        SimpleDateFormat idformatter = new SimpleDateFormat("yyMMddHHmmssSSS");
        return idformatter.format(new Date());
    }

    @SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
    private void createCardStatements(DataControllerRequest dcRequest, DemoDataUSNew helper) throws HttpCallException {
        Date date = new Date();
        int month = date.getMonth();
        int year = date.getYear();
        year = year + 1900;
        Map inputParam = new HashMap();
        Result result = HelperMethods.callApi(dcRequest, inputParam, HelperMethods.getHeaders(dcRequest),
                URLConstants.DMS_CARD_STATEMENTS_GET);
        if (!HelperMethods.hasRecords(result)) {
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

    private void createDemoUserContract(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse, DemoDataUSNew helper) throws HttpCallException, ApplicationException {
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
        helper.setPrimaryCoreCustomerName(inputParams.get("name"));
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
        inputParams.put("relationshipName", "Spouse");
        HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                URLConstants.MEMBERSHIPRELATION_CREATE);
        inputParams.clear();
        inputParams.put("id", HelperMethods.getNewId());
        inputParams.put("membershipId", String.valueOf(memberId2));
        inputParams.put("relatedMebershipId", String.valueOf(memberId1));
        inputParams.put("relationshipName", "Spouse");
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
        inputParams.put("relationshipName", "Child");
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
        inputParams.put("relationshipName", "Child");
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
        inputParams.put("relationshipName", "Child");
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
        inputParams.put("relationshipName", "Child");
        HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                URLConstants.MEMBERSHIPRELATION_CREATE);

        inputParams.clear();
        inputParams.put("id", HelperMethods.getNewId());
        inputParams.put("membershipId", String.valueOf(memberId3));
        inputParams.put("relatedMebershipId", String.valueOf(memberId2));
        inputParams.put("relationshipName", "Parent");
        HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                URLConstants.MEMBERSHIPRELATION_CREATE);

        createAccounts(dcRequest, "", helper.getUserName(), helper);

        Map<String, String> contractPayloadMap = new HashMap<>();
        contractPayloadMap.put("contractName", helper.getUserName());
        contractPayloadMap.put("serviceDefinitionName", "Retail Online Banking");
        contractPayloadMap.put("serviceDefinitionId", "5801fa32-a416-45b6-af01-b22e2de93777");
        contractPayloadMap.put("faxId", "1231-1231");
        contractPayloadMap.put("isDefaultActionsEnabled", "true");
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
        accounts.add(getValidAccountJsonObject(helper.getInvestmentSavingsAccount(), helper));
        accounts.add(getValidAccountJsonObject(helper.getInvestmentCashAccount1(), helper));
        accounts.add(getValidAccountJsonObject(helper.getInvestmentCashAccount2(), helper));
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
        accounts.add(getValidAccountJsonObject(helper.getAccount4DemoData(), helper));
        accounts.add(getValidAccountJsonObject(helper.getAccount5DemoData(), helper));
        contractCustomer.add("accounts", accounts);
        contractCustomer.add("features", CreateDemoData.getFeaturesList(dcRequest));
        contractCustomersJsonArray.add(contractCustomer);

        contractPayloadMap.put("contractCustomers", contractCustomersJsonArray.toString());
        ContractResource resource = DBPAPIAbstractFactoryImpl.getResource(ContractResource.class);
        Object[] inputArray1 = new Object[3];
        inputArray1[1] = contractPayloadMap;
        dcRequest.addRequestParam_("isDefaultActionsEnabled", "true");
        Result contractResponse = resource.createContract(methodId, inputArray1, dcRequest, dcResponse);
        helper.setContractId(contractResponse.getParamValueByName("contractId"));
        ContractBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(ContractBackendDelegate.class);
        backendDelegate.updateContractStatus(helper.getContractId(), DBPUtilitiesConstants.CONTRACT_STATUS_ACTIVE,
                dcRequest.getHeaderMap());
    }

    private JsonObject getValidAccountJsonObject(Map<String, String> inputParams, DemoDataUSNew helper) {
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
        inputParams.put("name", "Alaska");
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

    private void createUserActionLimits(DataControllerRequest dcRequest, DemoDataUSNew helper, String userId) {

        CustomerActionsBusinessDelegate businessDelegate =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(CustomerActionsBusinessDelegate.class);
        Set<String> accountsSet = new HashSet<>();
        accountsSet.add(helper.getAccountNum("Rewards Savings"));
        accountsSet.add(helper.getAccountNum("Core Checking"));
        accountsSet.add(helper.getAccountNum("Freedom Credit Card"));
        accountsSet.add(helper.getAccountNum("Investment Savings Account"));
        accountsSet.add(helper.getAccountNum("Investment Cash Account1"));
        accountsSet.add(helper.getAccountNum("Investment Cash Account2"));
        try {
            businessDelegate.createCustomerActions(userId, helper.getContractId(), helper.getPrimaryCoreCustomerId(),
                    "DEFAULT_GROUP", accountsSet, dcRequest.getHeaderMap());
        } catch (ApplicationException e) {
        	alert.prepareError(e.toString()).log();
        }
        accountsSet = new HashSet<>();
        accountsSet.add(helper.getAccountNum("Turbo Auto Loan"));
        accountsSet.add(helper.getAccountNum("12 Months Term Deposit"));
        try {
            businessDelegate.createCustomerActions(userId, helper.getContractId(), helper.getSecondaryCoreCustomerId(),
                    "DEFAULT_GROUP", accountsSet, dcRequest.getHeaderMap());
        } catch (ApplicationException e) {
        	alert.prepareError(e.toString()).log();
        }
    }

    private void createBillPayInfinityBeneficiary(String userId, String payeeId, DemoDataUSNew helper)
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

    private void createP2PInfinityBeneficiary(String userId, String payeeId, DemoDataUSNew helper)
            throws HttpCallException {
        P2PPayeeBusinessDelegate p2pPayeeDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(P2PPayeeBusinessDelegate.class);

        P2PPayeeDTO p2pPayeeDTO = new P2PPayeeDTO();
        p2pPayeeDTO.setPayeeId(payeeId);
        p2pPayeeDTO.setCreatedBy(userId);
        p2pPayeeDTO.setContractId(helper.getContractId());
        p2pPayeeDTO.setCif(helper.getPrimaryCoreCustomerId());
        p2pPayeeDelegate.createPayeeAtDBX(p2pPayeeDTO);
    }

    private void createInterBankInfinityBeneficiary(String userId, String payeeId, DemoDataUSNew helper)
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

    private void createIntraBankInfinityBeneficiary(String userId, String payeeId, DemoDataUSNew helper)
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

    private void createInternationalInfinityBeneficiary(String userId, String payeeId, DemoDataUSNew helper)
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
