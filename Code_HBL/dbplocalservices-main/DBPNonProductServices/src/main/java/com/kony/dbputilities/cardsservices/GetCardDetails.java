package com.kony.dbputilities.cardsservices;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.dbx.product.constants.FeatureAction;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetCardDetails implements JavaService2 {
    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);
        
        AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
        boolean hasBusinessPermission = authorizationChecksBusinessDelegate.isUserAuthorizedForPayeeOperations(FeatureAction.CARD_MANAGEMENT, "1", dcRequest.getHeaderMap(), dcRequest);
        boolean hasRetailPermission = authorizationChecksBusinessDelegate.isUserAuthorizedForPayeeOperations(FeatureAction.CARD_MANAGEMENT, "0", dcRequest.getHeaderMap(), dcRequest);
        if(!hasBusinessPermission && !hasRetailPermission ) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
		}
        
        if (preProcess(inputParams, dcRequest, result, hasRetailPermission, hasBusinessPermission)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CARDS_GET);
        }
        if (HelperMethods.hasRecords(result)) {
            postProcess(dcRequest, result);
        }
        return result;
    }

    private void postProcess(DataControllerRequest dcRequest, Result result) throws HttpCallException, ParseException {
        List<Record> cards = result.getAllDatasets().get(0).getAllRecords();
        for (Record card : cards) {
            setMaskedCardNumber(card);
            setAccountDetails(dcRequest, card);
            setIsExpiringFlag(card);
        }
    }

    private void setAccountDetails(DataControllerRequest dcRequest, Record card) throws HttpCallException {
        String accountId = HelperMethods.getFieldValue(card, "account_id");
        Result accountDetails = getAccountName(dcRequest, accountId);
        String accountName = HelperMethods.getFieldValue(accountDetails, "accountName");
        String currencyCode = HelperMethods.getFieldValue(accountDetails, "currencyCode");
        card.addParam(
                new Param("maskedAccountNumber", getMaskedAccountNumber(accountId), DBPUtilitiesConstants.STRING_TYPE));
        card.addParam(new Param("accountName", accountName, DBPUtilitiesConstants.STRING_TYPE));
        card.addParam(new Param("transactionCurrency", currencyCode, DBPUtilitiesConstants.STRING_TYPE));
        card.addParam(new Param("currencyCode", currencyCode, DBPUtilitiesConstants.STRING_TYPE));
    }

    private Result getAccountName(DataControllerRequest dcRequest, String accountId) throws HttpCallException {
        String filter = "Account_id" + DBPUtilitiesConstants.EQUAL + accountId;
        return HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.ACCOUNTS_GET);
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
        String initialsOfCard = cardNumber.substring(0, 4);
        String maskedCardNumber = cardNumber.substring(5, 11).replaceAll("^[0-9]*$", "XXXXXXXX");
        String lastDigits = cardNumber.substring(12, 16);
        String fullCardNumber = initialsOfCard.concat(maskedCardNumber).concat(lastDigits);
        card.addParam(new Param("maskedCardNumber", fullCardNumber, DBPUtilitiesConstants.STRING_TYPE));
    }
    @SuppressWarnings("deprecation")
	private void  setIsExpiringFlag(Record card) throws ParseException
    {
    String expirationDate=HelperMethods.getFieldValue(card, "expirationDate");
    String args[]=expirationDate.trim().split("-");
    int expYear=Integer.parseInt(args[0]);
    int expMonth=Integer.parseInt(args[1]);
    Calendar calendar1 = Calendar.getInstance();
    int currYear=calendar1.get(Calendar.YEAR);
	int currDate = (calendar1.get(Calendar.DATE));
	int currMonth = (calendar1.get(Calendar.MONTH) + 1);
	int numOfDaysInCurrentMonth = new Date(currYear, currMonth, 0).getDate();
	if((expYear==currYear)&&(expMonth==currMonth)&&((numOfDaysInCurrentMonth-currDate)<21))
	{
		card.addParam(new Param("isExpiring", "1", DBPUtilitiesConstants.STRING_TYPE));
	}
	else
	{
		card.addParam(new Param("isExpiring", "0", DBPUtilitiesConstants.STRING_TYPE));
	}
    
    }
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result, boolean hasRetailPermission, boolean hasBusinessPermission) {
        String userId = HelperMethods.getUserIdFromSession(dcRequest);
        String filter = "User_id" + DBPUtilitiesConstants.EQUAL + userId;
        if(hasRetailPermission && !hasBusinessPermission) {
        	filter = filter + DBPUtilitiesConstants.AND + "( isTypeBusiness" + DBPUtilitiesConstants.EQUAL + "0" + DBPUtilitiesConstants.OR +  "isTypeBusiness"+ DBPUtilitiesConstants.EQUAL + "null )";
        }
        if(hasBusinessPermission && !hasRetailPermission) {
        	filter = filter + DBPUtilitiesConstants.AND + "isTypeBusiness" + DBPUtilitiesConstants.EQUAL + "1";
        }
        inputParams.put(DBPUtilitiesConstants.FILTER, filter);
        return true;
    }
}
