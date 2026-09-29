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
			
			Map<String, String> requestparam = new HashMap<>();
			Iterator<String> paramItr = request.getParameterNames();
			while (paramItr.hasNext()) {
				String key = paramItr.next();
				requestparam.put(key, request.getParameter(key));
			}
			result = cardsBusinessDelegate.activateCards(requestparam, request.getHeaderMap());
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
			
			Map<String, Object> requestparam = new HashMap<>();
			Iterator<String> paramItr = request.getParameterNames();
			while (paramItr.hasNext()) {
				String key = paramItr.next();
				requestparam.put(key, request.getParameter(key));
			}
			result= cardsBusinessDelegate.applyForDebitCard(requestparam, request.getHeaderMap());
			
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
			Map<String, Object> requestparam = new HashMap<>();
			Iterator<String> paramItr = request.getParameterNames();
			while (paramItr.hasNext()) {
				String key = paramItr.next();
				requestparam.put(key, request.getParameter(key));
			}
				result = cardsBusinessDelegate.applyForCreditCard(requestparam, request.getHeaderMap());
			
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
		Result result = new Result();
		// Initialization of business Delegate Class
		CardServicesBusinessDelegate cardsBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(CardServicesBusinessDelegate.class);
		try {
			Map<String, Object> requestparam = new HashMap<>();
			Iterator<String> paramItr = request.getParameterNames();
			while (paramItr.hasNext()) {
				String key = paramItr.next();
				requestparam.put(key, request.getParameter(key));
			}
			String legalEntityId = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(request);
			result = (Result) cardsBusinessDelegate.updateCard(request.getHeaderMap(), requestparam, legalEntityId);
		} catch (Exception e) {
			alert.prepareError("Caught exception at activateCards method: " + e).log();
			result.addParam(DBPUtilitiesConstants.MSG_STATUS, DBPUtilitiesConstants.ERROR);
			return ErrorCodeEnum.ERR_21118.setErrorCode(result);
		}
		return result;
	}

}