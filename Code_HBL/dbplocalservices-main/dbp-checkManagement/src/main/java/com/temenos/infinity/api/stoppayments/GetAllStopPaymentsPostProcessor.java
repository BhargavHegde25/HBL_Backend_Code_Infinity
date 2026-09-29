package com.temenos.infinity.api.stoppayments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.infinity.dbx.temenos.TemenosBasePostProcessor;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.objects.Account;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.chequemanagement.constants.ErrorCodeEnum;
import com.temenos.infinity.api.commons.exception.ApplicationException;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class GetAllStopPaymentsPostProcessor extends TemenosBasePostProcessor {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	private static HashMap<String, String> stopIndicatorMap = new HashMap<String, String>() {
		private static final long serialVersionUID = 6752239390914679337L;

		{
			put("YES", "Success");
		}
	};
    String sortKey = null;

    @Override
    public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {
    	
        TemenosUtils temenosUtils = TemenosUtils.getInstance();
        HashMap<String, Account> accounts = temenosUtils.getAccountsMapFromCache(request);
        String status = result.getParamValueByName("status");
        //String id = result.getParamValueByName("id");
        
        String ErrMsg = result.getParamValueByName(StopPaymentConstants.PARAM_ERROR_MESSAGE) != "" ? result.getParamValueByName(StopPaymentConstants.PARAM_ERROR_MESSAGE) : "";
        
        Dataset stopPaymentsBody = result.getDatasetById(StopCheckPaymentUtils.PARAM_BODY);
        List<Record> stoPaymentsRecords = stopPaymentsBody != null ? stopPaymentsBody.getAllRecords() : null;
        if (stoPaymentsRecords.isEmpty()  && StringUtils.isEmpty(ErrMsg)) {
            alert.prepareError("Stop Payments empty return result " + result.getAllParams()).log();
            return TemenosUtils.getEmptyResult(StopPaymentConstants.PARAM_TRANSACTIONS_RESULT);
        } else if(!StringUtils.isEmpty(ErrMsg) && status.equalsIgnoreCase("failed")){
        	result = new Result();
        	result.addErrMsgParam(ErrMsg);
        	result.addStringParam("status", "falied");
        	result.addOpstatusParam(-1);
        	return result;
        }
        
        List<Record> stopPaymentsRecordsFinals = new ArrayList<Record>();
        Record stoPaymentsRecord = stoPaymentsRecords != null ? stoPaymentsRecords.get(0) : new Record();
        Dataset stopPayments = stoPaymentsRecord != null ? stoPaymentsRecord.getDatasetById("stops") : new Dataset();
        Dataset stopPaymentsRemarks = stoPaymentsRecord != null ? stoPaymentsRecord.getDatasetById("remarks") : new Dataset();
        List<Record> stopPaymentsRecords = stopPayments != null ? stopPayments.getAllRecords() : null;
        List<Record> stopPaymentsRemarksRecords = stopPaymentsRemarks != null ? stopPaymentsRemarks.getAllRecords() : null;
        
        JSONArray records = new JSONArray();
    	String fromAccountNumber = stoPaymentsRecord.hasParamByName("accountId") ? stoPaymentsRecord.getParamValueByName("accountId") : "";
    	String currencyId = stoPaymentsRecord.hasParamByName("currencyId") ? stoPaymentsRecord.getParamValueByName("currencyId") : "";
    	String chequeDate = stoPaymentsRecord.hasParamByName("chequeDate") ? stoPaymentsRecord.getParamValueByName("chequeDate") : "";
    	String transactionStopStatus = "Success";
    	int indexval = 0;
        for (Record product : stopPaymentsRecords) {
        	
        	Record stopPayment = new Record();

            //String transactionStopStatus = CommonUtils.getParamValue(product, "stopIndicator");
            String transactionDate = CommonUtils.getParamValue(product, "stopDate");
            String checkReason = CommonUtils.getParamValue(product, "stopTypeId");
            String firstChequeId = CommonUtils.getParamValue(product, "firstChequeId");
            String lastChequeId = CommonUtils.getParamValue(product, "lastChequeId");
            String beneficiaryId = CommonUtils.getParamValue(product, "beneficiaryId");
            String amountFrom = CommonUtils.getParamValue(product, "amountFrom");
            String amountTo = CommonUtils.getParamValue(product, "amountTo");
            
            diagnostic.debug("amountFrom ##"+ amountFrom);
            diagnostic.debug("amountTo ##"+ amountTo);
            
            if (accounts != null && StringUtils.isNotBlank(fromAccountNumber)) {
                Account account = accounts.containsKey(fromAccountNumber) ? accounts.get(fromAccountNumber) : null;
                if (account != null) {
                	stopPayment.addParam(new Param(StopCheckPaymentUtils.PARAM_NICK_NAME, account.getAccountName(),
                            Constants.PARAM_DATATYPE_STRING));
                }
            }
            JSONObject obj = ResultToJSON.convertRecord(product);
            Double charges = 0.0;
            JSONArray chargesArray = obj.getJSONArray("charges");
            for (int i = 0; i < chargesArray.length(); i++) {
                Double charge = 0.0;
                if ((String) ((JSONObject) chargesArray.get(i)).get("chargeAmount") != null)
                	charge = ((JSONObject) chargesArray.get(i)).getDouble("chargeAmount");
                
                charges = charges + charge;
                
            }
            stopPayment.addParam(StopPaymentConstants.PARAM_FEE, String.valueOf(charges));
            
         /*   String remarks = "";
            diagnostic.debug("obj ##"+ obj.toString());
            JSONArray remarksArray = obj.has("remarks") ? obj.getJSONArray("remarks"):new JSONArray();
            diagnostic.debug("remarksArray ##"+ remarksArray.toString());
            for (int i = 0; i < remarksArray.length(); i++) {
            	String remark = "";
                if ((String) ((JSONObject) remarksArray.get(i)).get("remark") != null)
                	remark = (String) ((JSONObject) remarksArray.get(i)).get("remark");
                diagnostic.debug("remark ##"+ remark);
                remarks = remarks + remark;
                if(i != remarksArray.length()-1) {
                	remarks = remarks + " ";
                }
                
            }
            
            if (StringUtils.isBlank(remarks)) {
            	remarks = "-";
            }
            */
            if (!StringUtils.isBlank(firstChequeId) && !StringUtils.isBlank(lastChequeId)) {
                //checkNumber = firstChequeId + " to " + lastChequeId;
            	stopPayment.addParam(StopPaymentConstants.PARAM_REQUEST_TYPE, StopPaymentConstants.PARAM_REQUEST_TYPE_SERIES);
            	stopPayment.addParam(StopPaymentConstants.PARAM_AMOUNT, !StringUtils.isBlank(amountFrom)
            			? !StringUtils.isBlank(amountTo) ? amountFrom + " to " + amountTo : amountFrom + " to -"
            					: "-");
            	stopPayment.addParam(StopPaymentConstants.PARAM_CHEQUE_NUMBER, firstChequeId);
            	stopPayment.addParam(StopPaymentConstants.PARAM_CHEQUE_NUMBER_TWO, lastChequeId); 
            } else if (!StringUtils.isBlank(firstChequeId) && StringUtils.isBlank(lastChequeId)) {
            	//checkNumber = firstChequeId;
            	stopPayment.addParam(StopPaymentConstants.PARAM_REQUEST_TYPE, StopPaymentConstants.PARAM_REQUEST_TYPE_SINGLE);
            	stopPayment.addParam(StopPaymentConstants.PARAM_AMOUNT, !StringUtils.isBlank(amountFrom) ? amountFrom : "-");
            	stopPayment.addParam(StopPaymentConstants.PARAM_CHEQUE_NUMBER, firstChequeId);
            	stopPayment.addParam(StopPaymentConstants.PARAM_CHEQUE_NUMBER_TWO, "");
			}
            
            //stopPayment.addParam(StopPaymentConstants.PARAM_STATUS_DESCRIPTION, stopIndicatorMap.get(transactionStopStatus));
            stopPayment.addParam(StopPaymentConstants.PARAM_STATUS_DESCRIPTION, transactionStopStatus);
            stopPayment.addParam(StopPaymentConstants.PARAM_CHEQUE_TRANSACTION_DATE, transactionDate); 
            stopPayment.addParam(StopPaymentConstants.CHECK_REASON, StopCheckPaymentUtils.convertCheckReasonToInfinity(checkReason));
            stopPayment.addParam(StopPaymentConstants.PARAM_ID, fromAccountNumber);
            stopPayment.addParam(StopPaymentConstants.PARAM_CHECK_DATE_OF_ISSUE, chequeDate); 
            stopPayment.addParam(StopPaymentConstants.PARAM_PAYEE_NAME, beneficiaryId);
            stopPayment.addParam(StopPaymentConstants.PARAM_PAYMENT_STOP_ACCOUNT, fromAccountNumber);
            stopPayment.addParam(StopPaymentConstants.PARAM_TRANSACTION_NOTES, getRemarks(stopPaymentsRemarksRecords, indexval));
            stopPayment.addParam("transactionCurrency", currencyId);
            //stopPaymentsRecordsFinals.add(stopPayment);
            records.put(ResultToJSON.convertRecord(stopPayment));
            indexval ++;
        }

/*    
        
        List<JSONObject> jsonValues = new ArrayList();
        for (int i = 0; i < stopPaymentsRecordsFinals.size(); i++) {
            jsonValues.add(ResultToJSON.convertRecord(stopPaymentsRecordsFinals.get(i)));
        }
        
        
        sortKey = request.getParameter(StopPaymentConstants.PARAM_SORY_BY);
        List<JSONObject> amountRecords = new ArrayList(); 
        List<JSONObject> nonAmountRecords = new ArrayList();
        if(jsonValues != null && jsonValues.size()>0) {
            if (sortKey.equals(StopPaymentConstants.PARAM_AMOUNT)) {
                for (JSONObject obj : jsonValues) {
                    if (obj.has(StopPaymentConstants.PARAM_AMOUNT)) {
                        amountRecords.add(obj);
                    } else {
                        nonAmountRecords.add(obj);
                    }
                }
                records = DoubleSort(amountRecords, request.getParameter(StopPaymentConstants.PARAM_ORDER));
                for (int i = 0; i < nonAmountRecords.size(); i++) {
                    records.put(nonAmountRecords.get(i));
                }
            } else {
                records = sort(jsonValues, request.getParameter(StopPaymentConstants.PARAM_ORDER));
            }
        }
        */
         
        JSONObject responseObj = new JSONObject();
        Result finalResult = new Result();
        responseObj.put(StopPaymentConstants.PARAM_TRANSACTIONS_RESULT, records);
        finalResult = JSONToResult.convert(responseObj.toString());
        finalResult.addOpstatusParam(0);
        finalResult.addHttpStatusCodeParam(200);
        finalResult.addStringParam("success", "Records found");
        result = finalResult;
            
        return result;
    }
    
    
   
    public JSONArray sort(List<JSONObject> jsonValues, String order) {

        JSONArray sortedJsonArray = new JSONArray();

        Collections.sort(jsonValues, new Comparator<JSONObject>() {

            @Override
            public int compare(JSONObject a, JSONObject b) {
                String valA = new String();
                String valB = new String();

                try {
                    valA = (String) a.get(sortKey);
                    valB = (String) b.get(sortKey);
                } catch (JSONException e) {
                    alert.prepareError("Caught exception at invoke of sorting: " + e).log();
                }

                if (order.equals("asc")) {
                    return valA.compareTo(valB);
                } else {
                    return valB.compareTo(valA);
                }
            }
        });

        for (int i = 0; i < jsonValues.size(); i++) {
            sortedJsonArray.put(jsonValues.get(i));
        }
        return sortedJsonArray;
    }

    public JSONArray DoubleSort(List<JSONObject> jsonValues, String order) {

        JSONArray sortedJsonArray = new JSONArray();

        if (order.equals("desc")) {
            jsonValues.sort((o1, o2) -> Double.compare(Double.parseDouble((String) o2.get(sortKey)),
                    Double.parseDouble((String) o1.get(sortKey))));
        } else {
            jsonValues.sort((o1, o2) -> Double.compare(Double.parseDouble((String) o1.get(sortKey)),
                    Double.parseDouble((String) o2.get(sortKey))));
        }
        for (int i = 0; i < jsonValues.size(); i++) {
            sortedJsonArray.put(jsonValues.get(i));
        }
        return sortedJsonArray;
    }

    public JSONArray filterRecords(JSONArray records, String offset, String limit) {
        JSONArray filteredJSONArray = new JSONArray();
        int startIndex = Integer.parseInt(offset);
        int lastIndex = startIndex + Integer.parseInt(limit);
        for (int i = startIndex; i < lastIndex && i < records.length(); i++) {
            filteredJSONArray.put(records.get(i));
        }
        return filteredJSONArray;
    }
    
    
	public String getRemarks(List<Record> stopPaymentsRemarksRecords, int index) {

		String remark = "";
		diagnostic.debug("index ##" + index);
		diagnostic.debug("stopPaymentsRemarksRecords size ##" + stopPaymentsRemarksRecords.size());
		for (int i = 0; i < stopPaymentsRemarksRecords.size(); i++) {
			Record product = stopPaymentsRemarksRecords.get(i);
			JSONObject obj = ResultToJSON.convertRecord(product);
			diagnostic.debug("obj index ##" + obj.toString());
			if (i == index) {
				if (obj.has("remark"))
					remark = obj.getString("remark");
				else
					remark = obj.getString("remark");
			}
		}
		return remark;

	}
    
}
