package com.kony.dbp.cardservices.resource.impl;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.security.SecureRandom;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.kony.dbp.cardservices.businessdelegate.api.CardServicesBusinessDelegate;
import com.kony.dbp.cardservices.dto.CardStatementsDTO;
import com.kony.dbp.cardservices.dto.CardTransactionsDTO;
import com.kony.dbp.cardservices.dto.CardsProductsDTO;
import com.kony.dbp.cardservices.resource.api.CardServicesResource;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
public class CardServicesResourceImpl implements CardServicesResource{
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final String CARD_STATUS = "Active";
	private static final String CARD_ACTION = "Activate";
	private static final String CARD_TYPE = "Credit";
	private static final boolean CARD_IS_INTERNATIONAL = true;
	private static final String CARD_REWARD_POINTS = "0";
	private static final String CARD_REASON = "";

	@Override
	public Result activateCards(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		Result result = new Result();

		// Initialization of business Delegate Class
		CardServicesBusinessDelegate cardsBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(CardServicesBusinessDelegate.class);

try {
			
			@SuppressWarnings("unchecked")
			Map < String, String > inputParams = (HashMap < String, String > ) inputArray[1];
			if(inputParams.get("cardId")!=null&&inputParams.get("cvv")!=null&&inputParams.get("userId")!=null&&(!inputParams.get("cardId").equals(""))&&(!inputParams.get("cvv").equals(""))&&(!inputParams.get("userId").equals("")))
			{
			result= (Result) cardsBusinessDelegate.activateCards(inputParams);
			}
			else
			{
				result.addParam(DBPUtilitiesConstants.MSG_STATUS, DBPUtilitiesConstants.ERROR);
				return ErrorCodeEnum.ERR_10240.setErrorCode(result);
			}

			if (result == null) {
				result.addParam(DBPUtilitiesConstants.MSG_STATUS, DBPUtilitiesConstants.ERROR);
				return ErrorCodeEnum.ERR_12000.setErrorCode(result);
			}
		} catch (Exception e) {
			alert.prepareError("Caught exception at activateCards method: " + e).log();
			result.addParam(DBPUtilitiesConstants.MSG_STATUS, DBPUtilitiesConstants.ERROR);
			return ErrorCodeEnum.ERR_12000.setErrorCode(result);
		}

		return result;
}
	@Override
	public Result getStatements(String card_id, String month, String userId) {
		Result result = new Result();
		// Initialization of business Delegate Class
		CardServicesBusinessDelegate cardsBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(CardServicesBusinessDelegate.class);
		try {
			List<CardStatementsDTO> statementsResponse = cardsBusinessDelegate.getStatements(card_id,month,userId);
            String statementsStr = JSONUtils.stringifyCollectionWithTypeInfo(statementsResponse, CardStatementsDTO.class);
            JSONArray StatementsJSONArr = new JSONArray(statementsStr);
            if (statementsStr.length() <= 0) {
                return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
            }
            JSONObject resultJSON = new JSONObject();
            resultJSON.put("CardStatements", StatementsJSONArr);
            result = JSONToResult.convert(resultJSON.toString());
			
		} catch (Exception e) {
			alert.prepareError("Caught exception at activateCards method: " + e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(result);
		}

		return result;
	}
	@Override
	public Result fetchCardTransactions(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response)
	{

		Result result = new Result();
		// Initialization of business Delegate Class
		CardServicesBusinessDelegate cardsBusinessDelegate =   DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(CardServicesBusinessDelegate.class);

		try {
			String userId = HelperMethods.getUserIdFromSession(request);
			Map < String, Object > inputParams = (HashMap < String, Object > ) inputArray[1];			
			List<CardTransactionsDTO> CardTransactionsDTO = cardsBusinessDelegate.fetchCardTransactions(inputParams);

			if (CardTransactionsDTO == null) {
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
			}
			JSONArray rulesJSONArr = new JSONArray(CardTransactionsDTO);
			JSONObject responseObj = new JSONObject();
			responseObj.put("cardTransactions", rulesJSONArr);
			result = JSONToResult.convert(responseObj.toString());
		} catch (Exception e) {
			alert.prepareError("Caught exception at fetchCardTransactions method: " + e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(result);
		}

		return result;
	}
	@Override
	public Result applyForDebitCard(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		Result result = new Result();

		// Initialization of business Delegate Class
		CardServicesBusinessDelegate cardsBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(CardServicesBusinessDelegate.class);

try {
			
			@SuppressWarnings("unchecked")
			Map < String, Object > inputParams = (HashMap < String, Object > ) inputArray[1];
			if(inputParams.get("accountId")!=null&&inputParams.get("pinNumber")!=null&&inputParams.get("userId")!=null&&(!inputParams.get("cardProductName").equals(""))&&(!inputParams.get("accountId").equals(""))&&(!inputParams.get("userId").equals("")))
			{
				HashMap < String, Object > input=new HashMap < String, Object >();
				SecureRandom rand = new SecureRandom();
				long first14 = (long) (rand.nextFloat() * 100000000000000L);
				long cardnumber = 5200000000000000L + first14;
				input.put("account_id",inputParams.get("accountId"));
				input.put("pinNumber",inputParams.get("pinNumber"));
				input.put("User_id",inputParams.get("userId"));
				input.put("cardProductName",inputParams.get("cardProductName"));
				input.put("cardNumber", cardnumber);
				input.put("cardType", "Debit");
				input.put("card_Status", "Issued");
				input.put("action", "Activate");
			//	input.put("billingAddress", "Merrion St, Leeds, West Yorkshire, United Kingdom");
				input.put("withdrawlLimit", inputParams.get("withdrawlLimit"));
				input.put("reason", "");
				input.put("serviceProvider", "visa");
				input.put("withdrawalMinLimit", inputParams.get("withdrawalMinLimit"));
				input.put("withdrawalMaxLimit", inputParams.get("withdrawalMaxLimit"));
				input.put("withdrawalStepLimit", inputParams.get("withdrawalStepLimit"));
				input.put("purchaseLimit", inputParams.get("purchaseLimit"));
				input.put("purchaseMinLimit", inputParams.get("purchaseMinLimit"));
				input.put("purchaseMaxLimit", inputParams.get("purchaseMaxLimit"));
				input.put("purchaseStepLimit", inputParams.get("purchaseStepLimit"));
				input.put("isInternational", "1");
				input.put("cardHolderName", inputParams.get("cardHolderName"));
				input.put("currentBalance", inputParams.get("currentBalance"));
				input.put("availableBalance", inputParams.get("availableBalance"));
				input.put("rewardsPoint", "0");
				input.put("bankName", inputParams.get("bankName"));
				input.put("currencyCode", inputParams.get("currencyCode"));
				input.put("billingAddress", inputParams.get("billingAddress"));
				input.put("cardDisplayName", inputParams.get("cardDisplayName"));
				//int first3 = (int) (Math.random() * 1000L);
				String s=String.valueOf(cardnumber);
				String strCVV=s.substring(13);
				int cvv=Integer.parseInt(strCVV);
				input.put("cvv", cvv);
				Calendar calendar = Calendar.getInstance();
		        String year = String.valueOf(calendar.get(Calendar.YEAR)+3);
		        int dateInt = (calendar.get(Calendar.DATE));
		        String date = (dateInt <= 9) ? ("0" + String.valueOf(dateInt)) : String.valueOf(dateInt);
		        int mon = (calendar.get(Calendar.MONTH) + 1);
		        String month = (mon <= 9) ? ("0" + String.valueOf(mon)) : String.valueOf(mon);
		        String expiryDate = year + "-" + month + "-" + date;
		        input.put("expirationDate", expiryDate);
			result= (Result) cardsBusinessDelegate.applyForDebitCard(input);
			}
			else
			{
				result.addParam(DBPUtilitiesConstants.MSG_STATUS, DBPUtilitiesConstants.ERROR);
				return ErrorCodeEnum.ERR_10240.setErrorCode(result);
			}

			if (result == null) {
				result.addParam(DBPUtilitiesConstants.MSG_STATUS, DBPUtilitiesConstants.ERROR);
				return ErrorCodeEnum.ERR_12000.setErrorCode(result);
			}
		} catch (Exception e) {
			alert.prepareError("Caught exception at applyForDebitCard method: " + e).log();
			result.addParam(DBPUtilitiesConstants.MSG_STATUS, DBPUtilitiesConstants.ERROR);
			return ErrorCodeEnum.ERR_12000.setErrorCode(result);
		}
return result;
	}
@Override
public Result getCardProducts(String methodID, Object[] inputArray, DataControllerRequest request,
		DataControllerResponse response)
{

	Result result = new Result();
	// Initialization of business Delegate Class
	CardServicesBusinessDelegate cardsBusinessDelegate =   DBPAPIAbstractFactoryImpl.getInstance()
			.getFactoryInstance(BusinessDelegateFactory.class)
			.getBusinessDelegate(CardServicesBusinessDelegate.class);

	try {
	//	String userId = HelperMethods.getUserIdFromSession(request);
		List<CardsProductsDTO> cardsProductsDTO ;
		Map < String, Object > inputParams = (HashMap < String, Object > ) inputArray[1];		
		if(inputParams.get("accountType")!=null&&(!inputParams.get("accountType").equals("")))
		{
			 cardsProductsDTO = cardsBusinessDelegate.getCardProducts(inputParams);
		}
		else
		{
			return ErrorCodeEnum.ERR_10240.setErrorCode(new Result());
		}

		
		if (cardsProductsDTO == null) {
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		JSONArray rulesJSONArr = new JSONArray(cardsProductsDTO);
		JSONObject responseObj = new JSONObject();
		responseObj.put("cardProducts", rulesJSONArr);
		result = JSONToResult.convert(responseObj.toString());
	} catch (Exception e) {
		alert.prepareError("Caught exception at fetchCardTransactions method: " + e).log();
		return ErrorCodeEnum.ERR_12000.setErrorCode(result);
	}

	return result;
}

public Result updateCardTransaction(String methodID, Object[] inputArray, DataControllerRequest request,
		DataControllerResponse response) {

	Result result = new Result();

	// Initialization of business Delegate Class
	CardServicesBusinessDelegate cardsBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
			.getFactoryInstance(BusinessDelegateFactory.class)
			.getBusinessDelegate(CardServicesBusinessDelegate.class);

try {
		
		@SuppressWarnings("unchecked")
		Map < String, String > inputParams = (HashMap < String, String > ) inputArray[1];
		if(inputParams.get("transactionId")!=null&&(!inputParams.get("transactionId").equals("")))
		{
		result= (Result) cardsBusinessDelegate.updateCardTransaction(inputParams);
		}
		else
		{
			result.addParam(DBPUtilitiesConstants.MSG_STATUS, DBPUtilitiesConstants.ERROR);
			return ErrorCodeEnum.ERR_10240.setErrorCode(result);
		}

		if (result == null) {
			result.addParam(DBPUtilitiesConstants.MSG_STATUS, DBPUtilitiesConstants.ERROR);
			return ErrorCodeEnum.ERR_12000.setErrorCode(result);
		}
	} catch (Exception e) {
		alert.prepareError("Caught exception at activateCards method: " + e).log();
		result.addParam(DBPUtilitiesConstants.MSG_STATUS, DBPUtilitiesConstants.ERROR);
		return ErrorCodeEnum.ERR_12000.setErrorCode(result);
	}

	return result;
}
@Override
	public Result applyForCreditCard(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();

		// Initialization of business Delegate Class
		CardServicesBusinessDelegate cardsBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(CardServicesBusinessDelegate.class);

		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
			if (inputParams.get("userId") != null && (!inputParams.get("cardProductName").equals(""))
					&& (!inputParams.get("userId").equals("") && (!inputParams.get("creditLimit").equals("")))) {
				HashMap<String, Object> input = new HashMap<String, Object>();
				SecureRandom rand = new SecureRandom();
				long first16 = (long) (rand.nextFloat() * 100000000000000L);
				long cardnumber = 5200000000000000L + first16;
				input.put("User_id", inputParams.get("userId"));
				input.put("cardProductName", inputParams.get("cardProductName"));
				input.put("cardNumber", cardnumber);
				input.put("cardType", CARD_TYPE);
				input.put("card_Status", CARD_STATUS);
				input.put("action", CARD_ACTION);
				input.put("reason", CARD_REASON);
				input.put("isInternational", CARD_IS_INTERNATIONAL);
				input.put("cardHolderName", inputParams.get("cardHolderName"));
				input.put("creditLimit", inputParams.get("creditLimit"));
				input.put("availableCredit", inputParams.get("creditLimit"));
				input.put("rewardsPoint", CARD_REWARD_POINTS);
				input.put("bankName", inputParams.get("bankName"));
				input.put("currencyCode", inputParams.get("currencyCode"));
				input.put("billingAddress", inputParams.get("billingAddress"));
				input.put("protectionEnabled", inputParams.get("creditCardProtection"));
				input.put("cardDisplayName", inputParams.get("nameOnCard"));
				int first3 = (int) (rand.nextFloat() * 1000L);
				input.put("cvv", first3);
				int pinNumber = (int) (rand.nextFloat() * 10000L);
				input.put("pinNumber", pinNumber);
				Calendar calendar = Calendar.getInstance();
				String year = String.valueOf(calendar.get(Calendar.YEAR) + getCreditCardExpirationYears());
				int dateInt = (calendar.get(Calendar.DATE));
				String date = (dateInt <= 9) ? ("0" + String.valueOf(dateInt)) : String.valueOf(dateInt);
				int mon = (calendar.get(Calendar.MONTH) + 1);
				String month = (mon <= 9) ? ("0" + String.valueOf(mon)) : String.valueOf(mon);
				String expiryDate = year + "-" + month + "-" + date;
				input.put("expirationDate", expiryDate);
				result = (Result) cardsBusinessDelegate.applyForCreditCard(input);
			} else {
				result.addParam(DBPUtilitiesConstants.MSG_STATUS, DBPUtilitiesConstants.ERROR);
				return ErrorCodeEnum.ERR_10240.setErrorCode(result);
			}

			if (result == null) {
				result.addParam(DBPUtilitiesConstants.MSG_STATUS, DBPUtilitiesConstants.ERROR);
				return ErrorCodeEnum.ERR_12000.setErrorCode(result);
			}
		} catch (Exception e) {
			alert.prepareError("Caught exception at applyForCreditCard method: " + e).log();
			result.addParam(DBPUtilitiesConstants.MSG_STATUS, DBPUtilitiesConstants.ERROR);
			return ErrorCodeEnum.ERR_12000.setErrorCode(result);
		}
		return result;
	}

	private int getCreditCardExpirationYears() {
		ServicesManager serviceManager;
		try {
			serviceManager = ServicesManagerHelper.getServicesManager();
			ConfigurableParametersHelper configurableParametersHelper = serviceManager
					.getConfigurableParametersHelper();
			String creditCardExpirationYears = configurableParametersHelper
					.getServerProperty("CREDIT_CARD_EXPIRATION_YEARS");
			if (StringUtils.isNotBlank(creditCardExpirationYears))
				return Integer.valueOf(creditCardExpirationYears);
		} catch (Exception e) {
			alert.prepareError(e.getMessage()).log();
		}
		return 4;
	}

}