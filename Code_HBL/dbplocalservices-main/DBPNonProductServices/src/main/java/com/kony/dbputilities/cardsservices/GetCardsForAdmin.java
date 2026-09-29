package com.kony.dbputilities.cardsservices;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetCardsForAdmin implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CARDS_GET);
        }
        if (HelperMethods.hasRecords(result)) {
            postProcess(inputParams, dcRequest, result);
        }
        return result;
    }

    private void postProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException, ParseException {
        List<Record> cards = result.getAllDatasets().get(0).getAllRecords();
        for (Record card : cards) {
            setMaskedCardNumber(card);
            updateCardUserName(dcRequest, card, inputParams.get("userName"));
            setAccountDetails(dcRequest, card);
            updateDateFormat(card);
            card.removeParamByName("cardNumber");
        }
    }

    private void updateDateFormat(Record card) {
        String expirationDate = HelperMethods.getFieldValue(card, "expirationDate");
        try {
            if (StringUtils.isNotBlank(expirationDate)) {
                card.addParam(new Param("expirationDate",
                        HelperMethods.convertDateFormat(expirationDate, "yyyy-MM-dd'T'hh:mm:ss'Z'"), "String"));
            }
        } catch (Exception e) {

        }
    }

    private void updateCardUserName(DataControllerRequest dcRequest, Record card, String userName) {
        String cardHolderName = HelperMethods.getFieldValue(card, "cardHolderName");
        if (StringUtils.isNotBlank(cardHolderName)) {
            userName = cardHolderName.replace(" ", ".");
        }
        card.addParam(new Param("username", userName, "String"));
    }

    private void setAccountDetails(DataControllerRequest dcRequest, Record card)
            throws HttpCallException, ParseException {
        String accountId = HelperMethods.getFieldValue(card, "account_id");
        Record account = getAccount(dcRequest, accountId);
        card.addParam(
                new Param("maskedAccountNumber", getMaskedAccountNumber(accountId), DBPUtilitiesConstants.STRING_TYPE));
        card.addParam(new Param("accountName", HelperMethods.getFieldValue(account, "accountName"),
                DBPUtilitiesConstants.STRING_TYPE));
        card.addParam(new Param("interestRate", HelperMethods.getFieldValue(account, "interestRate"),
                DBPUtilitiesConstants.STRING_TYPE));
        card.addParam(new Param("rewardPointBalance", HelperMethods.getFieldValue(account, "availableBalance"),
                DBPUtilitiesConstants.STRING_TYPE));
        card.addParam(new Param("issuedOn", HelperMethods
                .convertDateFormat(HelperMethods.getFieldValue(account, "openingDate"), "yyyy-MM-dd'T'hh:mm:ss'Z'"),
                DBPUtilitiesConstants.STRING_TYPE));
        card.addParam(new Param("currentBalance", HelperMethods.getFieldValue(account, "currentBalance"),
                DBPUtilitiesConstants.STRING_TYPE));
        card.addParam(new Param("CurrentDueAmount", HelperMethods.getFieldValue(account, "currentAmountDue"),
                DBPUtilitiesConstants.STRING_TYPE));
        card.addParam(new Param("currentDueDate", HelperMethods
                .convertDateFormat(HelperMethods.getFieldValue(account, "dueDate"), "yyyy-MM-dd'T'hh:mm:ss'Z'"),
                DBPUtilitiesConstants.STRING_TYPE));
        card.addParam(new Param("minimumDueAmount", HelperMethods.getFieldValue(account, "minimumDue"),
                DBPUtilitiesConstants.STRING_TYPE));
        card.addParam(new Param("lastStatementBalance", HelperMethods.getFieldValue(account, "lastStatementBalance"),
                DBPUtilitiesConstants.STRING_TYPE));
        card.addParam(new Param("lastPaymentDate", HelperMethods
                .convertDateFormat(HelperMethods.getFieldValue(account, "lastPaymentDate"), "yyyy-MM-dd'T'hh:mm:ss'Z'"),
                DBPUtilitiesConstants.STRING_TYPE));
        card.addParam(new Param("lastStatementPayment", HelperMethods.getFieldValue(account, "lastPaymentAmount"),
                DBPUtilitiesConstants.STRING_TYPE));
    }

    private Record getAccount(DataControllerRequest dcRequest, String accountId) throws HttpCallException {
        String filter = "Account_id" + DBPUtilitiesConstants.EQUAL + accountId;
        Result account = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.ACCOUNTS_GET);
        if (HelperMethods.hasRecords(account)) {
            return account.getAllDatasets().get(0).getRecord(0);
        }
        return null;
    }

    private String getMaskedAccountNumber(String accountNumber) {
    	if(accountNumber.length()>6)
    	{
    	int accLength=accountNumber.length();
        String initialsOfAccount = accountNumber.substring(0, 2);
        String maskedAccountNumber = accountNumber.substring(3, accLength-5).replaceAll("^[0-9]*$", "XXXXXXXXX");
        String lastDigits = accountNumber.substring(accLength-4, accLength);
        return initialsOfAccount.concat(maskedAccountNumber).concat(lastDigits);
    	}
    	return accountNumber;
    }

    private void setMaskedCardNumber(Record card) {
        String cardNumber = HelperMethods.getFieldValue(card, "cardNumber");
        String initialsOfCard = cardNumber.substring(0, 2);
        String maskedCardNumber = cardNumber.substring(2, 12).replaceAll("^[0-9]*$", "XXXXXXXXXX");
        String lastDigits = cardNumber.substring(12, 16);
        String fullCardNumber = initialsOfCard.concat(maskedCardNumber).concat(lastDigits);
        card.addParam(new Param("maskedCardNumber", fullCardNumber, DBPUtilitiesConstants.STRING_TYPE));
    }

    private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {
        String adminId = HelperMethods.getAPIUserIdFromSession(dcRequest);
        if (HelperMethods.isAdmin(dcRequest, adminId)) {
            String filter = "User_id" + DBPUtilitiesConstants.EQUAL + getUserId(dcRequest, inputParams.get("userName"));
            inputParams.put(DBPUtilitiesConstants.FILTER, filter);
            return true;
        } else {
            HelperMethods.setValidationMsg("logged in user is not admin", dcRequest, result);
            return false;
        }
    }

    private String getUserId(DataControllerRequest dcRequest, String userName) throws HttpCallException {
        String filter = "UserName" + DBPUtilitiesConstants.EQUAL + userName;
        Result user = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.USER_GET);
        return HelperMethods.getFieldValue(user, "id");
    }
}
