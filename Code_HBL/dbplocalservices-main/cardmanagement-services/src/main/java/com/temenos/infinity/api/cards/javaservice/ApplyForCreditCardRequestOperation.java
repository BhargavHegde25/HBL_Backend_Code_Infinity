package com.temenos.infinity.api.cards.javaservice;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.kony.dbputilities.util.Log4j2Configurator;

import java.security.SecureRandom;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.temenos.infinity.api.cards.constants.OperationName;
import com.temenos.infinity.api.cards.constants.ServiceId;
import com.temenos.infinity.api.cards.resource.api.CardServicesResource;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * 
 * @author KH2394
 * @version 1.0
 * Java Service end point to Apply For New Card
 */
public class ApplyForCreditCardRequestOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();

		Result result = new Result();
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
				input.put("cardType", "Credit");
				input.put("card_Status", "Active");
				input.put("action", "Activate");
				input.put("reason", "");
				input.put("isInternational", true);
				input.put("cardHolderName", inputParams.get("cardHolderName"));
				input.put("creditLimit", inputParams.get("creditLimit"));
				input.put("availableCredit", inputParams.get("creditLimit"));
				input.put("rewardsPoint", "0");
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
				result = applyForCreditCard(input);
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
	
	public Result applyForCreditCard(HashMap<String, Object> inputparams) {
		Result result=new Result();
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationNameForCreate = OperationName.DB_CARDS_CREATE;
		try
		{
			String addCards=DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null).withOperationId(operationNameForCreate).withRequestParameters(inputparams).withRequestHeaders(null).withDataControllerRequest(null).build().getResponse();
			JSONObject response =  new JSONObject(addCards);
			if(response.has("card")) {
				if(response.getJSONArray("card").length() > 0) {
					result.addParam("cardNumber", response.getJSONArray("card").getJSONObject(0).getString("cardNumber"));
				}
			}
			inputparams.clear();
		}
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while fetching the cards",jsonExp).log();
			return null;
		}
		catch(Exception exp) {
			alert.prepareError("Exception occured while fetching the cards",exp).log();
			return null;
		}
		result.addParam(DBPUtilitiesConstants.MSG_STATUS, DBPUtilitiesConstants.SUCCESS);
		result.addParam("successmsg", "Card Created Successfully");
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
