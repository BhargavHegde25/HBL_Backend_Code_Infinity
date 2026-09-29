package com.temenos.infinity.api.cards.javaservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.cards.constants.OperationName;
import com.temenos.infinity.api.cards.constants.ServiceId;
import com.temenos.infinity.api.cards.dto.CardsDTO;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

public class ActivateCardRequestOperation implements JavaService2{
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = new Result();
		try {
			
			@SuppressWarnings("unchecked")
			Map < String, String > inputParams = (HashMap < String, String > ) inputArray[1];
			if(inputParams.get("cardId")!=null&&inputParams.get("cvv")!=null&&inputParams.get("userId")!=null&&(!inputParams.get("cardId").equals(""))&&(!inputParams.get("cvv").equals(""))&&(!inputParams.get("userId").equals("")))
			{
			result= activateCards(inputParams);
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
}