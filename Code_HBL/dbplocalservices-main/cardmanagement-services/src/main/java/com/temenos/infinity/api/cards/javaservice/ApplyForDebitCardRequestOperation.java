package com.temenos.infinity.api.cards.javaservice;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.kony.dbputilities.util.Log4j2Configurator;

import java.security.SecureRandom;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.temenos.infinity.api.cards.constants.OperationName;
import com.temenos.infinity.api.cards.constants.ServiceId;
import com.temenos.infinity.api.cards.resource.api.CardServicesResource;
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
public class ApplyForDebitCardRequestOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();

		Result result = new Result();
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
		        
		        String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
				String operationNameForCreate = OperationName.DB_CARDS_CREATE;
				
				String addCards=DBPServiceExecutorBuilder.builder().
						withServiceId(serviceName).
						withObjectId(null).
						withOperationId(operationNameForCreate).
						withRequestParameters(input).
						withRequestHeaders(null).
						withDataControllerRequest(null).
						build().getResponse();
				
				result.addParam(DBPUtilitiesConstants.MSG_STATUS, DBPUtilitiesConstants.SUCCESS);
				result.addParam("successmsg", "Card Created Successfully");
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
}
