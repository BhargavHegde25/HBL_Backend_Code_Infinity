package com.temenos.infinity.api.cards.resource.impl;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
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
import com.temenos.infinity.api.cards.businessdelegate.api.CardServicesBusinessDelegate;
import com.temenos.infinity.api.cards.dto.CardStatementsDTO;
import com.temenos.infinity.api.cards.dto.CardTransactionsDTO;
import com.temenos.infinity.api.cards.dto.CardsProductsDTO;
import com.temenos.infinity.api.cards.resource.api.CardServicesResource;
import com.temenos.infinity.api.commons.utils.Utilities;
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
import com.kony.dbputilities.util.LegalEntityUtil;
public class CardServicesDBXResourceImpl implements CardServicesResource{
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
			JSONObject serviceResponseJSON = Utilities.convertStringToJSON((String)inputParams.get("requestBody"));
			inputParams.put("cardId",serviceResponseJSON.optString("cardId"));
			inputParams.put("cvv",serviceResponseJSON.optString("cvv"));
			inputParams.put("oldcvv",serviceResponseJSON.optString("oldcvv"));
			inputParams.remove("requestBody");
			if(inputParams.get("cardId")!=null&&inputParams.get("cvv")!=null&&inputParams.get("userId")!=null&&(!inputParams.get("cardId").equals(""))&&(!inputParams.get("cvv").equals(""))&&(!inputParams.get("userId").equals("")))
			{
			result= (Result) cardsBusinessDelegate.activateCards(inputParams, null);
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
		
		CardServicesResource resource = new CardServicesResourceImpl();
		return resource.getStatements(card_id, month, userId);	
	}
	@Override
	public Result fetchCardTransactions(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response)
	{

		CardServicesResource resource = new CardServicesResourceImpl();
		return resource.fetchCardTransactions(methodID, inputArray, request, response);
		
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
			JSONObject serviceResponseJSON = Utilities.convertStringToJSON((String)inputParams.get("requestBody"));
			if(serviceResponseJSON.opt("accountId")!=null&&serviceResponseJSON.opt("pinNumber")!=null&&inputParams.get("userId")!=null&&(!serviceResponseJSON.opt("cardProductName").equals(""))&&(!serviceResponseJSON.opt("accountId").equals(""))&&(!inputParams.get("userId").equals("")))
			{
				HashMap < String, Object > input=new HashMap < String, Object >();
				SecureRandom rand = new SecureRandom();
				long first14 = (long) (rand.nextFloat() * 100000000000000L);
				long cardnumber = 5200000000000000L + first14;
				input.put("account_id",serviceResponseJSON.opt("accountId"));
				input.put("pinNumber",serviceResponseJSON.opt("pinNumber"));
				input.put("User_id",inputParams.get("userId"));
				input.put("cardProductName",serviceResponseJSON.opt("cardProductName"));
				input.put("cardNumber", cardnumber);
				input.put("cardType", "Debit");
				input.put("card_Status", "Issued");
				input.put("action", "Activate");
			//	input.put("billingAddress", "Merrion St, Leeds, West Yorkshire, United Kingdom");
				input.put("withdrawlLimit", serviceResponseJSON.opt("withdrawlLimit"));
				input.put("reason", "");
				input.put("serviceProvider", "visa");
				input.put("withdrawalMinLimit", serviceResponseJSON.opt("withdrawalMinLimit"));
				input.put("withdrawalMaxLimit", serviceResponseJSON.opt("withdrawalMaxLimit"));
				input.put("withdrawalStepLimit", serviceResponseJSON.opt("withdrawalStepLimit"));
				input.put("purchaseLimit", serviceResponseJSON.opt("purchaseLimit"));
				input.put("purchaseMinLimit", serviceResponseJSON.opt("purchaseMinLimit"));
				input.put("purchaseMaxLimit", serviceResponseJSON.opt("purchaseMaxLimit"));
				input.put("purchaseStepLimit", serviceResponseJSON.opt("purchaseStepLimit"));
				input.put("isInternational", "1");
				input.put("cardHolderName", serviceResponseJSON.opt("cardHolderName"));
				input.put("currentBalance", serviceResponseJSON.opt("currentBalance"));
				input.put("availableBalance", serviceResponseJSON.opt("availableBalance"));
				input.put("rewardsPoint", "0");
				input.put("bankName", serviceResponseJSON.opt("bankName"));
				input.put("currencyCode", serviceResponseJSON.opt("currencyCode"));
				input.put("billingAddress", serviceResponseJSON.opt("billingAddress"));
				input.put("cardDisplayName", serviceResponseJSON.opt("cardDisplayName"));
				input.put("legalEntityId",LegalEntityUtil.getLegalEntityIdFromSessionOrCache(request));
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
			result= (Result) cardsBusinessDelegate.applyForDebitCard(input, null);
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

	CardServicesResource resource = new CardServicesResourceImpl();
	return resource.getCardProducts(methodID, inputArray, request, response);
}

public Result updateCardTransaction(String methodID, Object[] inputArray, DataControllerRequest request,
		DataControllerResponse response) {

	CardServicesResource resource = new CardServicesResourceImpl();
	return resource.updateCardTransaction(methodID, inputArray, request, response);
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
				input.put("legalEntityId",LegalEntityUtil.getLegalEntityIdFromSessionOrCache(request));
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
				result = (Result) cardsBusinessDelegate.applyForCreditCard(input, null);
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

	@Override
	public Result updateCard(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		CardServicesResource resource = new CardServicesResourceImpl();
		return resource.updateCard(methodId, inputArray, request, response);
	}

}