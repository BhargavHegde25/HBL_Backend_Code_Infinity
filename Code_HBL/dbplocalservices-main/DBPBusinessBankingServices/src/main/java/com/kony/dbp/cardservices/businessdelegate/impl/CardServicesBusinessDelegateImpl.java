package com.kony.dbp.cardservices.businessdelegate.impl;
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
import com.kony.dbp.cardservices.businessdelegate.api.CardServicesBusinessDelegate;
import com.kony.dbp.cardservices.dto.CardStatementsDTO;
import com.kony.dbp.cardservices.dto.CardTransactionsDTO;
import com.kony.dbp.cardservices.dto.CardsDTO;
import com.kony.dbp.cardservices.dto.CardsProductsDTO;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;


/**
 * 
 * @author KH2394
 * @version 1.0 Extends the {@link CardServicesBusinessDelegate}
 */
public class CardServicesBusinessDelegateImpl implements CardServicesBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
     @Override
 	public Result activateCards(Map<String,String> inputparams) {


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

		res.addParam("successmsg", "Card Activated Successfully");
		res.addParam(DBPUtilitiesConstants.MSG_STATUS, DBPUtilitiesConstants.SUCCESS);
		return res;
	}
  @Override
	public List<CardStatementsDTO> getStatements(String card_id, String month, String userId) throws ApplicationException {
		List<CardStatementsDTO> cardStatementsDTO = null;
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.CARDSTATEMENT_GET;
		String operationNameForGet = OperationName.DB_CARDS_GET;

		HashMap<String, Object> hashMap = new HashMap<>();
		HashMap<String, Object> inputhashMap = new HashMap<>();
		try {
			//Filter to check the passed cardNumber belongs to user
			String cardsFilter = "User_id" + " eq " + userId + " and " + "Id"
			 + " eq " + card_id;
			hashMap.put("$filter", cardsFilter);

			@SuppressWarnings("deprecation")
			String getCards = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
					operationNameForGet, hashMap, null, "");

			JSONObject jsonRsponse = new JSONObject(getCards);
			JSONArray countJsonArray = jsonRsponse.getJSONArray("card");
			hashMap.clear();
			if(countJsonArray.length()>0)
			{
				JSONObject card = countJsonArray.getJSONObject(0);
				String cardNumber = card.getString("cardNumber");
				String filter = "Card_id" + " eq " + cardNumber;
			 	inputhashMap.put("$filter", filter);
			
				@SuppressWarnings("deprecation")
				String response = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
						operationName, inputhashMap, null, "");
	
				JSONObject serviceResponseJSON = new JSONObject(response);
				JSONArray data = serviceResponseJSON.getJSONArray("cardstatements");
				JSONObject statements=data.getJSONObject(0);
				JSONArray statementArray = new JSONArray();
				statementArray.put(statements);
			    if(serviceResponseJSON==null)
			    	 throw new ApplicationException(ErrorCodeEnum.ERR_10018);
			    
				 cardStatementsDTO= JSONUtils.parseAsList(statementArray.toString(), CardStatementsDTO.class);
			}
//			else
//			{
//				//throw new ApplicationException(ErrorCodeEnum.ERR_10018);
//			}
	 }

		catch (ApplicationException e) {
          alert.prepareError(e.toString()).log();
          throw e;
      } catch (Exception e) {
          alert.prepareError(e.toString()).log();
          throw new ApplicationException(ErrorCodeEnum.ERR_10700);
      }
	  
		return cardStatementsDTO;

	}
  
  @Override
	public List<CardTransactionsDTO> fetchCardTransactions(Map<String, Object> inputParams) {

		List<CardTransactionsDTO> CardTransactionsDTO = null;

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_FETCH_CARDTRANSACTION_PROC;
	    String operationNameForGet = OperationName.DB_CARDS_GET;
	    
	
	    HashMap<String, Object> hashMap = new HashMap<>();
	    Map<String, Object> requestParams = new HashMap<String, Object>();
		try {
			//Filter to check the passed cardNumber belongs to user
			String filter = "User_id" + " eq " + inputParams.get("userId") + " and " + "Id"
			      + " eq " + inputParams.get("cardId");
			hashMap.put("$filter", filter);

			@SuppressWarnings("deprecation")
			String getCards = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
				 operationNameForGet, hashMap, null, "");

		    JSONObject jsonRsponse = new JSONObject(getCards);
			JSONArray countJsonArray = jsonRsponse.getJSONArray("card");
			hashMap.clear();
			if(countJsonArray.length()>0)
			{
				JSONObject card = countJsonArray.getJSONObject(0);
				String cardNumber = card.getString("cardNumber");
				requestParams.put(Constants.CARDNUMBER, cardNumber);
				requestParams.put(Constants.OFFSET, inputParams.get("offset"));
				requestParams.put(Constants.SIZE, inputParams.get("limit"));
		
				if(inputParams.get("order")!=null) {
					requestParams.put(Constants.ORDER, inputParams.get("order"));
				}
				else {
					requestParams.put(Constants.ORDER, "DESC");
				}
				
				if(inputParams.get("sortBy")!=null) {
					requestParams.put(Constants.SORTBY, inputParams.get("sortBy"));
				}
				else {
					requestParams.put(Constants.SORTBY, "transactionDate");
				}
			
//				JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(requestParams, request.getHeaderMap(),
//						"/services/dbpRbLocalServicesdb/dbxdb_fetch_cardtransaction_proc");
				
				@SuppressWarnings("deprecation")
				String response = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
						operationName, requestParams, null, "");
				JSONObject serviceResponseJSON = new JSONObject(response);
				JSONArray data = serviceResponseJSON.getJSONArray("records");
				CardTransactionsDTO= JSONUtils.parseAsList(data.toString(), CardTransactionsDTO.class);
			}    	   
//			else
//			{
//				//throw new ApplicationException(ErrorCodeEnum.ERR_10018);
//			}
		}

		catch(JSONException jsonExp) {
			alert.prepareError("JSONException occured while fetching card transactions",jsonExp).log();
			return null;
		}
		catch(Exception exp) {
			alert.prepareError("Exception occured while fetching card transactions",exp).log();
			return null;
		}

		return CardTransactionsDTO;
	}
	public Result applyForDebitCard(Map<String,Object> inputparams) throws Exception {
		
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
		res.addParam("successmsg", "Card Created Successfully");
		return res;
	}
	@Override
	public List<CardsProductsDTO> getCardProducts(Map<String,Object> inputparams) throws ApplicationException {
		List<CardsProductsDTO> cardProductsDTO = null;
		HashMap<String, Object> hashMap = new HashMap<>();
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationNameForGetCardProductsView = OperationName.DB_CARDPRODDUCTSVIEW_GET;
		
		Result res=new Result();
		String filter="accountType" + " eq " + inputparams.get("accountType");
		hashMap.put("$filter", filter);
		
		
	 try
	 {
		 @SuppressWarnings("deprecation")
			String response = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
					operationNameForGetCardProductsView, hashMap, null, "");
		   JSONObject serviceResponseJSON = new JSONObject(response);
			JSONArray data = serviceResponseJSON.getJSONArray("cardproductsview");
		    if(serviceResponseJSON==null)
		    	 throw new ApplicationException(ErrorCodeEnum.ERR_10018);
		    
		    cardProductsDTO= JSONUtils.parseAsList(data.toString(), CardsProductsDTO.class);
		
	 }

		catch (Exception e) {
          alert.prepareError(e.toString()).log();
          throw new ApplicationException(ErrorCodeEnum.ERR_10700);
      }
	  
		return cardProductsDTO;

	}
	public Result updateCardTransaction(Map<String,String> inputparams) {


		List<CardsDTO> cardsDTOs = null;
		Result res=new Result();
		HashMap<String, Object> hashMap = new HashMap<>();
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationNameForUpdate = OperationName.CARDSTRANSACTION_UPDATE;
		hashMap.put("transactionReferenceNumber", inputparams.get("transactionId"));
		hashMap.put("disputedescription", inputparams.get("disputeDescription"));
		hashMap.put("disputeReason", inputparams.get("disputereason"));
		hashMap.put("isdisputed", 1);
		try
		{
		String activateCards = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
				operationNameForUpdate, hashMap, null, "");
		}
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while fetching the cards",jsonExp).log();
			return null;
		}
		catch(Exception exp) {
			alert.prepareError("Exception occured while fetching the cards",exp).log();
			return null;
		}
		hashMap.clear();
		res.addParam(DBPUtilitiesConstants.MSG_STATUS, DBPUtilitiesConstants.SUCCESS);
		res.addParam("successmsg", "Card Updated Successfully");
		return res;
	}
	@Override
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
}

  