package com.temenos.infinity.api.cards.businessdelegate.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonObject;
import com.temenos.infinity.api.cards.businessdelegate.api.CardServicesBusinessDelegate;
import com.temenos.infinity.api.cards.dto.CardStatementsDTO;
import com.temenos.infinity.api.cards.dto.CardTransactionsDTO;
import com.temenos.infinity.api.cards.dto.CardsDTO;
import com.temenos.infinity.api.cards.dto.CardsProductsDTO;
import com.temenos.infinity.api.cards.constants.Constants;
import com.temenos.infinity.api.cards.constants.OperationName;
import com.temenos.infinity.api.cards.constants.ServiceId;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;


/**
 * 
 * @author KH2394
 * @version 1.0 Extends the {@link CardServicesBusinessDelegate}
 */
public class CardServicesDBXBusinessDelegateImpl implements CardServicesBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
     @Override
 	public Result activateCards(Map<String,String> inputparams, Map<String, Object> headersMap) {


		List<CardsDTO> cardsDTOs = null;
		Result res=new Result();
		HashMap<String, Object> hashMap = new HashMap<>();
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationNameForUpdate = OperationName.DB_CARDS_UPDATE;
		String operationNameForGet = OperationName.DB_CARDS_GET;

		try {
			String filter="User_id" + " eq " + inputparams.get("userId") + " and " + "id"
					+ " eq " + inputparams.get("cardId");
			hashMap.put("$filter", filter);
			@SuppressWarnings("deprecation")
			String cardNumber = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
					operationNameForGet, hashMap, null, "");
			JSONObject jsonRsponse = new JSONObject(cardNumber);
			JSONArray countJsonArr = jsonRsponse.getJSONArray("card");
			JSONObject cardNumberdata = countJsonArr.getJSONObject(0);
			Long cardNumberVal=cardNumberdata.getLong("cardNumber");
			cardsDTOs = JSONUtils.parseAsList(countJsonArr.toString(), CardsDTO.class);
			hashMap.clear();
			if(inputparams.get("oldcvv")!=null&&(!inputparams.get("oldcvv").equals("")))
			{
				String filtercvv = "User_id" + " eq " + inputparams.get("userId") + " and " + "cardNumber"
						+ " eq " + cardNumberVal+" and "+"("+"cvv" + " eq " + inputparams.get("cvv") + " or "+"cvv" + " eq " + inputparams.get("oldcvv")+")";
						hashMap.put("$filter", filtercvv);
				@SuppressWarnings("deprecation")
				String updateCards = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
						operationNameForGet, hashMap, null, "");

				JSONObject jsonRs = new JSONObject(updateCards);
				JSONArray countJsonArray = jsonRs.getJSONArray("card");
				cardsDTOs = JSONUtils.parseAsList(countJsonArray.toString(), CardsDTO.class);
				hashMap.clear();
				if(countJsonArray.length()>=2)
				{
				
					
					for(int i=0;i<countJsonArray.length();i++)
					{
						JSONObject rec = countJsonArray.getJSONObject(i);
						String cvv=Integer.toString(rec.getInt("cvv"));
						String cardStatus=rec.getString("card_Status");
						String cardid=Integer.toString(rec.getInt("Id"));
						if(cvv.equals(inputparams.get("oldcvv"))&&cardid.equals(inputparams.get("cardId"))&&(!cardStatus.equals("Expired")))
						{

							hashMap.put("Id", rec.getInt("Id"));
							hashMap.put("card_Status", "Expired");
							//hashMap.put("action", "Activated");
							hashMap.put("reason", "Card Expired");
							@SuppressWarnings("deprecation")
							String activateCards = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
									operationNameForUpdate, hashMap, null, "");
							hashMap.clear();

						}
						else if(cvv.equals(inputparams.get("cvv"))&&(!cardStatus.equals("Active")))
						{
							hashMap.put("Id", rec.getInt("Id"));
							hashMap.put("card_Status", "Active");
							//hashMap.put("action", "Activated");
							hashMap.put("reason", "Card Activated");
							@SuppressWarnings("deprecation")
							String activateCards = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
									operationNameForUpdate, hashMap, null, "");
							hashMap.clear();

						}
						else
						{
							res.addParam(DBPUtilitiesConstants.MSG_STATUS, DBPUtilitiesConstants.ERROR);
							return ErrorCodeEnum.ERR_21016.setErrorCode(res);
						}
					}
				}
				else
				{
					res.addParam(DBPUtilitiesConstants.MSG_STATUS, DBPUtilitiesConstants.ERROR);
					return ErrorCodeEnum.ERR_21016.setErrorCode(res);
				}
			}

			else
			{
				String filterNewCard = "User_id" + " eq " + inputparams.get("userId") + " and " + "cardNumber"+ " eq " + cardNumberVal+" and "+"cvv" + " eq " + inputparams.get("cvv");
				hashMap.put("$filter", filterNewCard);

				@SuppressWarnings("deprecation")
				String updateCards = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
						operationNameForGet, hashMap, null, "");

				JSONObject jsonRes = new JSONObject(updateCards);
				JSONArray countJsonArray = jsonRes.getJSONArray("card");
				cardsDTOs = JSONUtils.parseAsList(countJsonArray.toString(), CardsDTO.class);
				hashMap.clear();
				if(countJsonArray.length()>0)
				{
					for(int i=0;i<countJsonArray.length();i++)
					{
						JSONObject rec = countJsonArray.getJSONObject(i);
						if(rec.getString("card_Status").equals("Issued"))
						{

							hashMap.put("Id", rec.getInt("Id"));
							hashMap.put("card_Status", "Active");
							//hashMap.put("action", "Activated");
							hashMap.put("reason", "Card Activated");
							@SuppressWarnings("deprecation")
							String activateCards = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
									operationNameForUpdate, hashMap, null, "");
						}
						else
						{
							res.addParam(DBPUtilitiesConstants.MSG_STATUS, DBPUtilitiesConstants.ERROR);
							return ErrorCodeEnum.ERR_21015.setErrorCode(res);
						}

					}
				}
				else
				{
					res.addParam(DBPUtilitiesConstants.MSG_STATUS, DBPUtilitiesConstants.ERROR);
					return ErrorCodeEnum.ERR_21016.setErrorCode(res);
				}

			}
		}

		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while fetching the cards",jsonExp).log();
			return null;
		}
		catch(Exception exp) {
			alert.prepareError("Exception occured while fetching the cards",exp).log();
			return null;
		}

		res.addParam("message", "Card Activated Successfully");
		res.addParam("code", "");
		res.addParam("orderId", "");
		res.addParam(DBPUtilitiesConstants.MSG_STATUS, DBPUtilitiesConstants.SUCCESS);
		
		
		return res;
	}
  @Override
	public List<CardStatementsDTO> getStatements(String card_id, String month, String userId) throws ApplicationException {
		
	  CardServicesBusinessDelegate businessDelegate = new CardServicesBusinessDelegateImpl();
	  return businessDelegate.getStatements(card_id, month, userId);

	}
  
  @Override
	public List<CardTransactionsDTO> fetchCardTransactions(Map<String, Object> inputParams) {
	  CardServicesBusinessDelegate businessDelegate = new CardServicesBusinessDelegateImpl();
	  return businessDelegate.fetchCardTransactions(inputParams);
	}
	public Result applyForDebitCard(Map<String,Object> inputparams, Map<String, Object> headersMap) throws Exception {
		
		Result res=new Result();
		List<CardsDTO> cardsDTOs = null;
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationNameForCreate = OperationName.DB_CARDS_CREATE;
		try
		{
	//String addCards=DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null, operationNameForCreate, inputparams, null, "");
			
			String addCards=DBPServiceExecutorBuilder.builder().
	withServiceId(serviceName).
	withObjectId(null).
	withOperationId(operationNameForCreate).
	withRequestParameters(inputparams).
	withRequestHeaders(null).
	withDataControllerRequest(null).
	build().getResponse();
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
		res.addParam(DBPUtilitiesConstants.MSG_STATUS, DBPUtilitiesConstants.SUCCESS);
		res.addParam("message", "Card Created Successfully");
		res.addParam("code", "");
		res.addParam("orderId", "");
		return res;
	}
	@Override
	public List<CardsProductsDTO> getCardProducts(Map<String,Object> inputparams) throws ApplicationException {
		
		CardServicesBusinessDelegate businessDelegate = new CardServicesBusinessDelegateImpl();
		  return businessDelegate.getCardProducts(inputparams);

	}
	public Result updateCardTransaction(Map<String,String> inputparams) {

		CardServicesBusinessDelegate businessDelegate = new CardServicesBusinessDelegateImpl();
		  return businessDelegate.updateCardTransaction(inputparams);
	}
	@Override
	public Result applyForCreditCard(Map<String, Object> inputparams, Map<String, Object> headersMap) {
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
	@Override
	public Result updateCard(Map<String, Object> headerMap, Map<String, Object> requestParams, String legalEntityId) {
		Result result = new Result();
		try {
			String claimsToken = (String) headerMap.get(DBPUtilitiesConstants.X_KONY_AUTHORIZATION);
			String serviceName = "CardsNonProductServices";
			String operationName = "updateCard";
			JSONObject serviceResponseJSON = Utilities.convertStringToJSON((String)requestParams.get("requestBody"));
			Map<String, Object> params = new HashMap<>();
			params.put("Action", serviceResponseJSON.opt("Action"));
			params.put("cardId", serviceResponseJSON.opt("cardId"));
			params.put("Reason", serviceResponseJSON.opt("Reason"));
			params.put("withdrawlLimit", serviceResponseJSON.opt("withdrawlLimit"));
			params.put("purchaseLimit", serviceResponseJSON.opt("purchaseLimit"));
			if("PinChange".equals(serviceResponseJSON.opt("Action"))) {
				params.put("newPin", serviceResponseJSON.opt("newPin"));
			}
			params.put("legalEntityId", legalEntityId);
			String resultStr = DBPServiceExecutorBuilder.builder().withServiceId(serviceName)
					.withOperationId(operationName).withRequestParameters(params).withRequestHeaders(headerMap)
					.withFabricAuthToken(claimsToken).build().getResponse();
			JSONObject crudResponse = new JSONObject(resultStr);
			JSONObject resultJSON = new JSONObject();
			if (crudResponse.has("opstatus") && crudResponse.getInt("opstatus") == 0 && !(crudResponse.has("errorMessage") || crudResponse.has("errmsg"))) {
				JSONArray cardArray = crudResponse.has("card") ? crudResponse.getJSONArray("card") : new JSONArray();
				JSONObject cardObject;
				String cardId;
				if (cardArray.length() > 0) {
					cardObject = cardArray.getJSONObject(0);
					cardId = cardObject.getString("Id");
				} else {
					cardId = "";
				}
				resultJSON.put("code", "");
				resultJSON.put("orderId", "");
				resultJSON.put("opstatus", crudResponse.getInt("opstatus"));
				resultJSON.put("message", "Request updated successfully");
				resultJSON.put("status", crudResponse.getString("status"));
				resultJSON.put("httpStatusCode", crudResponse.getInt("httpStatusCode"));
				result = JSONToResult.convert(resultJSON.toString());
			}else {
				result =JSONToResult.convert(new JSONObject(resultStr).toString()); 
				result.addStringParam("dbperrmsg", ErrorCodeEnum.ERR_12000.getMessage());
			}
		} catch (Exception e) {
			alert.prepareError("Exception occured invoking the service " + e).log();
			 return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		return result;

	}
}

  