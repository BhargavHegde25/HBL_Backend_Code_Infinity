package com.infinity.dbx.temenos.bulkpaymentservices.postprocessor;

import java.util.List;
import java.util.ListIterator;
import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.google.gson.JsonObject;
import com.infinity.dbx.temenos.accounts.AccountsConstants;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.BasePostProcessor;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.dbx.product.constants.Constants;

public class FetchPOPostProcessor extends BasePostProcessor implements AccountsConstants  {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	public Result execute(Result result, DataControllerRequest request, DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		try {	
			JSONArray duplicatePaymentOrders = new JSONArray();
			List<Dataset> paymentOrders = result.getAllDatasets();
            if(paymentOrders !=null 
            	&& paymentOrders.size()>0 
            	&& paymentOrders.get(0)!=null 
            	&& paymentOrders.get(0).getAllRecords()!=null 
            	&& paymentOrders.get(0).getAllRecords().size() > 0)
            {         	
			 ListIterator<Record> recordItr = result.getAllDatasets().get(0).getAllRecords().listIterator();
			 while(recordItr.hasNext()) { 
				 Record record = recordItr.next();
				 record.addParam(new Param("recordId", request.getParameter("recordId")));
				 
				 if(record.getParam("accountId")!=null
						 && record.getParam("accountNumber")==null )
				 {
					 record.addParam(new Param("accountNumber", record.getParam("accountId").getValue()));
				 }
				 
				 if(record.getParam("paymentMethod")!=null
						 && StringUtils.equals(record.getParam("paymentMethod").getValue(), "ACOTHER")
						 && record.getParam("creditAccountId")!=null
						 && record.getParam("creditAccountName")!=null
						 && record.getParam("accountNumber")==null
						 && record.getParam("recipientName")==null )
				 {
					 record.addParam(new Param("accountNumber", record.getParam("creditAccountId").getValue()));
					 record.addParam(new Param("recipientName", record.getParam("creditAccountName").getValue()));
				 }
				 
				 
				 ListIterator<Param> paramItr = record.getAllParams().listIterator();
				 while (paramItr.hasNext()) {
					 Param param = paramItr.next();
					 if (param.getName().equals(TemenosConstants.STATUS) && !(param.getValue().isEmpty()||param.getValue()==null)) {
						 
						 String statusOld = param.getValue().toString().toUpperCase();
						 if (StringUtils.equals(statusOld, TemenosConstants.WAITMASTER_AUTH)) {
							 param.setValue(TemenosConstants.READY_FOR_EXECUTION);							 
						 }
						 else if (StringUtils.equals(statusOld, TemenosConstants.WAITMASTER_INAU)) {
							 param.setValue(TemenosConstants.MODIFIED);							 
						 }
						 else if (StringUtils.equals(statusOld, TemenosConstants.WAITMASTER)) {
							 param.setValue(TemenosConstants.READY_FOR_EXECUTION);							 
						 }
						 else if (StringUtils.equals(statusOld, TemenosConstants.CREATED)) {
							 param.setValue(TemenosConstants.NEWLY_ADDED);							 
						 }
						 else if (StringUtils.equals(statusOld, TemenosConstants.ERROR)) {
							 param.setValue(TemenosConstants.IN_ERROR);							 
						 }
						 else if (StringUtils.equals(statusOld, TemenosConstants.REJECTED)) {
							 param.setValue(TemenosConstants.REJECT);							 
						 }
						 else if (StringUtils.equals(statusOld, TemenosConstants.CANCELLED)) {
							 param.setValue(TemenosConstants.CANCEL);							 
						 }
						 else if (StringUtils.equals(statusOld, TemenosConstants.COMPLETED)) {
							 param.setValue(TemenosConstants.PROCESSING_COMPLETED);							 
						 }
						 else if ((StringUtils.equals(statusOld, TemenosConstants.PLACED))
								   ||(StringUtils.equals(statusOld, TemenosConstants.CHILDCOMPLETED))
								   ||(StringUtils.equals(statusOld, TemenosConstants.PROCESSED))
								   ||(StringUtils.equals(statusOld, TemenosConstants.WAREHOUSED))
								   ||(StringUtils.equals(statusOld, TemenosConstants.SENDORDER))
								   ||(StringUtils.equals(statusOld, TemenosConstants.AWAITINGACK))
								   ||(StringUtils.equals(statusOld, TemenosConstants.RESERVEFUNDS))
								   ||(StringUtils.equals(statusOld, TemenosConstants.ORDERAPPROVED))
								   ||(StringUtils.equals(statusOld, TemenosConstants.READYFOREXECUTION))
								   ||(StringUtils.equals(statusOld, TemenosConstants.FRAUDCHECK))
								   ||(StringUtils.equals(statusOld, TemenosConstants.FRAUDCHECKCOMPLETE))
								   ||(StringUtils.equals(statusOld, TemenosConstants.FRAUDCHECKFAILED))){
							 param.setValue(TemenosConstants.PROCESSING);							 
						 }
						 
					 }
					 if (param.getName().equals(TemenosConstants.PAYMENTSTATUS) && !(param.getValue().isEmpty()||param.getValue()==null)) {
						 
						 String paymentstatusOld = param.getValue().toString().toUpperCase();
						 if (StringUtils.equals(paymentstatusOld, TemenosConstants.ACCP)) {
							 param.setValue(TemenosConstants.PAYMENT_ACCEPTED);							 
						 }
						 else if (StringUtils.equals(paymentstatusOld, TemenosConstants.ACSC)) {
							 param.setValue(TemenosConstants.SETTLEMENT_ACCEPTED);							 
						 }
						 else if (StringUtils.equals(paymentstatusOld, TemenosConstants.ACSP)) {
							 param.setValue(TemenosConstants.SETTLEMENT_IN_PROCESS);							 
						 }
						 else if (StringUtils.equals(paymentstatusOld, TemenosConstants.ACWC)) {
							 param.setValue(TemenosConstants.ACCEPTED_WITH_CHANGE);							 
						 }
						 else if (StringUtils.equals(paymentstatusOld, TemenosConstants.PNDG)) {
							 param.setValue(TemenosConstants.PAYMENT_PENDING);							 
						 }
						 else if (StringUtils.equals(paymentstatusOld, TemenosConstants.RJCT)) {
							 param.setValue(TemenosConstants.PAYMENT_REJECTED);							 
						 }
						 
						 
					 }
					 
					 if (param.getName().equals(TemenosConstants.RECORD_STATUS) &&
						 param.getValue().equals(TemenosConstants.INAU) && 
						 !(param.getValue().isEmpty()||param.getValue()==null)) {
						 
						 record.getParam(TemenosConstants.STATUS).setValue(TemenosConstants.MODIFIED);
						 
						 JSONObject jsonObject = new JSONObject();
							jsonObject.put(TemenosConstants.PAYMENT_ORDER_ID,record.getParam(TemenosConstants.PAYMENT_ORDER_ID).getValue());
							duplicatePaymentOrders.put(jsonObject);
					 }
					 
				 }
				 
				 if(record.getParam("sourceType")!=null						
						 && record.getParam("versionNumber")!=null )						
				 {
					if(StringUtils.equalsIgnoreCase(record.getParam("sourceType").getValue(), "UPLOAD"))
					{
						if(StringUtils.equals(record.getParam("versionNumber").getValue(), "1")
								&& !StringUtils.equals(record.getParam(TemenosConstants.STATUS).getValue(),TemenosConstants.IN_ERROR)
								&& StringUtils.equals(record.getParam(TemenosConstants.STATUS).getValue(),TemenosConstants.READY_FOR_EXECUTION))							
						{
							 record.getParam(TemenosConstants.STATUS).setValue(TemenosConstants.READY_FOR_EXECUTION);
						}
						
						else if(!StringUtils.equals(record.getParam("versionNumber").getValue(), "1")
								&& !StringUtils.equals(record.getParam(TemenosConstants.STATUS).getValue(),TemenosConstants.IN_ERROR)
								&& StringUtils.equals(record.getParam(TemenosConstants.STATUS).getValue(),TemenosConstants.READY_FOR_EXECUTION))
						{
							 record.getParam(TemenosConstants.STATUS).setValue(TemenosConstants.MODIFIED);
						}
					}
					else if(StringUtils.equalsIgnoreCase(record.getParam("sourceType").getValue(), "Manual")) {	
						if(!StringUtils.equals(record.getParam(TemenosConstants.STATUS).getValue(),TemenosConstants.IN_ERROR)
								&& StringUtils.equals(record.getParam(TemenosConstants.STATUS).getValue(),TemenosConstants.READY_FOR_EXECUTION))
						{
						record.getParam(TemenosConstants.STATUS).setValue(TemenosConstants.NEWLY_ADDED);	
						}
					}
				 }
				 
			 }
				for (int i = 0; i < duplicatePaymentOrders.length(); i++) {

					String duplicatePoId = (String) duplicatePaymentOrders.getJSONObject(i)
							.get(TemenosConstants.PAYMENT_ORDER_ID);

					JSONObject responseObj = new JSONObject(ResultToJSON.convert(result));
					JSONArray jsonArray = TemenosUtils.getFirstOccuringArray(responseObj);
					for (int j = 0; j < jsonArray.length(); j++) {

						if (StringUtils.equals(
								(String) jsonArray.getJSONObject(j).get(TemenosConstants.PAYMENT_ORDER_ID),
								duplicatePoId)
								&& StringUtils.equals(
										(String) jsonArray.getJSONObject(j).get(TemenosConstants.STATUS),
										TemenosConstants.READY_FOR_EXECUTION)) {
							jsonArray.remove(j);
						}
					}
					
					JSONObject resultObject = new JSONObject();
					resultObject.put(Constants.PAYMENTORDERS, jsonArray);
					result = JSONToResult.convert(resultObject.toString());									
				}
          }
		}
		catch(Exception e) {
			alert.prepareError("Error occured while invoking post processor for FetchPOPostProcessor: ", e).log();
			return null;
		}
		return result;
	}
}
