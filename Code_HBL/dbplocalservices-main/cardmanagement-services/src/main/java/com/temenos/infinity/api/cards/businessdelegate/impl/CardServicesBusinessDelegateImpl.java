package com.temenos.infinity.api.cards.businessdelegate.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;
import com.dbp.core.util.JSONUtils;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.cards.businessdelegate.api.CardServicesBusinessDelegate;
import com.temenos.infinity.api.cards.constants.Constants;
import com.temenos.infinity.api.cards.constants.OperationName;
import com.temenos.infinity.api.cards.constants.ServiceId;
import com.temenos.infinity.api.cards.dto.CardStatementsDTO;
import com.temenos.infinity.api.cards.dto.CardTransactionsDTO;
import com.temenos.infinity.api.cards.dto.CardsDTO;
import com.temenos.infinity.api.cards.dto.CardsProductsDTO;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;


/**
 * 
 * @author KH2394
 * @version 1.0 Extends the {@link CardServicesBusinessDelegate}
 */
public class CardServicesBusinessDelegateImpl implements CardServicesBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
     @Override
 	public Result activateCards(Map<String,String> inputparams, Map<String, Object> headersMap) {
    	 return invokeSRMS(ServiceCallHelper.convertMap(inputparams), headersMap);
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
				String filter = "Card_id" + " eq " + cardNumber + " and "+"month eq "+month;
			 	inputhashMap.put("$filter", filter);
			
				@SuppressWarnings("deprecation")
				String response = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
						operationName, inputhashMap, null, "");
	
				JSONObject serviceResponseJSON = new JSONObject(response);
				if(serviceResponseJSON==null)
			    	 throw new ApplicationException(ErrorCodeEnum.ERR_10018);
				JSONArray data = serviceResponseJSON.getJSONArray("cardstatements");
				JSONObject statements=new JSONObject();
				JSONArray statementArray = new JSONArray();
				if(data.length()>0) {
					statements=data.getJSONObject(0);
				    statementArray.put(statements);   
				}
			    
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
	public Result applyForDebitCard(Map<String,Object> inputparams, Map<String,Object> headersMap) throws Exception {
		return invokeSRMS(inputparams, headersMap);

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
	public Result applyForCreditCard(Map<String, Object> inputparams, Map<String, Object> headersMap) {
		return invokeSRMS(inputparams, headersMap);
	}
	@Override
	public Result updateCard(Map<String, Object> headerMap, Map<String, Object> requestParams, String legalEntityId) {
		return invokeSRMS(requestParams, headerMap);
	}
	
	private Result invokeSRMS(Map<String, Object> inputParams, Map<String, Object> headerParams) {
		Result result = new Result();
		try {
		String claimsToken = (String) headerParams.get("x-kony-authorization");
		String serviceName = "ServiceRequestJavaService";
		String operationName = "createOrder";

		String resultStr = DBPServiceExecutorBuilder.builder()
                    .withServiceId(serviceName)
                    .withOperationId(operationName)
                    .withRequestParameters(inputParams).withRequestHeaders(headerParams)
                    .withFabricAuthToken(claimsToken).build().getResponse();
		result = JSONToResult.convert(new JSONObject(resultStr).toString());
		}catch (Exception e) {
			alert.prepareError("Exception occured invoking the service " + e).log();
			 return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		return result;
	}
}

  