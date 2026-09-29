package com.infinity.dbx.temenos.fileutil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.infinity.dbx.temenos.TemenosBaseService;
import com.infinity.dbx.temenos.transfers.TransferConstants;
import com.infinity.dbx.temenos.transfers.TransferUtils;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

public class DownloadPDF extends TemenosBaseService {
    private static final String TRANSACTIONS_ARRAY_KEY = "Transactions";
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {

        Result result = new Result();
        try {

            Result localResult = (Result) super.invoke(methodId, inputArray, request, response);
            HashMap<String, Object> params = (HashMap<String, Object>) inputArray[1];
            String transactionType = CommonUtils.getParamValue(params, Constants.PARAM_TRANSACTION_TYPE);
            String transactionId = CommonUtils.getParamValue(params, Constants.PARAM_TRANSACTION_ID);
            String contentType = CommonUtils.getParamValue(params, Constants.CONTENT_TYPE);

            if (params == null || transactionId == null) {
                CommonUtils.setErrMsg(localResult, "No input parameters provided");
                CommonUtils.setOpStatusError(localResult);
                return localResult;
            }

            HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
            String serviceName = TransferConstants.T24_SERVICE_NAME_TRANSACTION;
            String operationName = null;
            request.addRequestParam_(TransferConstants.PARAM_TRANSACTION_ID, transactionId);
            String srmsFlag = CommonUtils.getServerEnvironmentProperty(TransferConstants.PARAM_PAYMENT_BACKEND,
                    request);

			// If SRMS deployed get payment info from SRMS
			if (srmsFlag.equalsIgnoreCase("SRMS") || srmsFlag.equalsIgnoreCase("SRMS_MOCK")) {
				String srmsSserviceName = TransferConstants.SRMS_SERVICE_NAME;
				String srmsOperationName = TransferConstants.SRMS_OPERATION_NAME;
				result = CommonUtils.callIntegrationService(request, params, serviceHeaders, srmsSserviceName,
						srmsOperationName, false);
			} else if (Constants.FREQUENCY_ONCE.equalsIgnoreCase(transactionType)) {
				operationName = TransferConstants.PAYMENTORDER_DETAILS_OPERATION_NAME;
				result = CommonUtils.callIntegrationService(request, params, serviceHeaders, serviceName, operationName,
						false);
			} else {
				operationName = TransferConstants.STANDINGORDER_DETAILS_OPERATION_NAME;
				result = CommonUtils.callIntegrationService(request, params, serviceHeaders, serviceName, operationName,
						false);
			}

            JSONObject transaction = ResultToJSON
                    .convertRecord(result.getDatasetById(TRANSACTIONS_ARRAY_KEY).getRecord(0));
            LinkedHashMap<String, Object> transactDetails = transforTransactionDate(transaction, transactionType);

            result = new Result();
            if(contentType != null && contentType.equalsIgnoreCase("pdf")) {
                TemenosUtils temenosUtils = TemenosUtils.getInstance();
                byte[] bytes = new PDFGeneratorTransactionAcknowledgement(transactDetails).generateFileAndGetByteArray();
                String fileId = TransferUtils.getUniqueNumericString(10);
                temenosUtils.saveIntoCache(fileId, bytes, 120);
                response.setStatusCode(HttpStatus.SC_OK);
                result.addParam(new Param("fileId",fileId));
                alert.prepareError("Download PDF Data has been stored successfully").log();
            }
            else {
            	result.addParam(new Param("pdf",
                        new PDFGeneratorTransactionAcknowledgement(transactDetails).generateFileAndGetBase64(),
                          Constants.PARAM_DATATYPE_STRING));
            }
            
        } catch (Exception e) {
            Result errorResult = new Result();
            alert.prepareError("Exception Occured while DownloadPDF" + e).log();
            CommonUtils.setOpStatusError(result);
            CommonUtils.setErrMsg(errorResult, e.getMessage());
            return errorResult;
        }
        return result;
    }
    
    private Map<String, String> getCustomHeaders() {
		Map<String, String> customHeaders = new HashMap<>();
        customHeaders.put(HttpHeaders.CONTENT_TYPE, "application/pdf");
        customHeaders.put("Content-Disposition", "attachment; filename=\"Transaction Report.pdf\"");
        return customHeaders;
	}

    public LinkedHashMap<String, Object> transforTransactionDate(JSONObject transactionData, String transactionType) {
        // Check Transaction Type
        boolean isSameBankTransfer = transactionData.has("serviceName") &&
                (transactionData.getString("serviceName").equals("TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE")
                        || transactionData.getString("serviceName").equals("INTRA_BANK_FUND_TRANSFER_CREATE"));
    	LinkedHashMap<String, Object> finalTxnObject = new LinkedHashMap<String, Object>();
        // Set Reference and From details
    	 alert.prepareError("HBL:DownloadPDF:transforTransactionData:"+transactionData).log();
        if (transactionData.has("transactionId")) {
            finalTxnObject.put("Reference Number", transactionData.get("transactionId").toString());
        }
        // Set Debit Details
        if (transactionData.has("fromAccountNumber")) {
            finalTxnObject.put("From Account Number", MaskFields(transactionData.get("fromAccountNumber").toString()));
        }
        if (transactionData.has("fromAccountName")) {
            finalTxnObject.put("From Account Name", transactionData.get("fromAccountName").toString());
        }
        if (transactionData.has("description")) {
            finalTxnObject.put("Notes", transactionData.get("description").toString());
        }
        if (transactionData.has("notes")) {
            finalTxnObject.put("Notes", transactionData.get("notes").toString());
        }
        // Set Credit Details

        if (transactionData.has("toAccountNumber")) {
            finalTxnObject.put("To Account Number", MaskFields(transactionData.get("toAccountNumber").toString()));
        }
        if (transactionData.has("beneficiaryName")) {
            finalTxnObject.put("Beneficiary Name", transactionData.get("beneficiaryName").toString());
        }else {
            if (transactionData.has("toAccountName")) {
                finalTxnObject.put("To Account Name", transactionData.get("toAccountName").toString());
            }
        }
        if (transactionData.has("frequencyType")) {
            finalTxnObject.put("Frequency", transactionData.get("frequencyType").toString());
        }
        
        if (transactionData.has("amount")) {
            finalTxnObject.put("Amount", transactionData.get("amount").toString() + " " + transactionData.get("paymentCurrencyId").toString());
        }
         
        if (transactionData.has("paymentCurrencyId")) {
            finalTxnObject.put("Payment Currency", transactionData.get("paymentCurrencyId").toString());
        }
        
        if (transactionData.has("indicativeRate") && !isSameBankTransfer) {
            finalTxnObject.put("Exchange Rate", transactionData.get("indicativeRate").toString());
        }
        
        // Set Charge information
        if (transactionData.has("charges") && StringUtils.isNotBlank(transactionData.get("charges").toString()) && !isSameBankTransfer) {
        	finalTxnObject.put("Charges Breakdown:", "  ");
            String ChargesString = transactionData.get("charges").toString();
            JSONArray chargesArray = new JSONArray(ChargesString);
            if (chargesArray.length() > 0) {
            	int i=0;
                for (Object currChargesObject : chargesArray) {
                    if (currChargesObject instanceof JSONObject) {
                        JSONObject currentChargesJSON = (JSONObject) currChargesObject;

                        if ((currentChargesJSON.has("chargeAmount")
                                && StringUtils.isNotBlank(currentChargesJSON.get("chargeAmount").toString()))
                                && (currentChargesJSON.has("chargeCurrencyId")
                                        && StringUtils.isNotBlank(currentChargesJSON.getString("chargeCurrencyId")))
                                && (currentChargesJSON.has("chargeName")
                                        && StringUtils.isNotBlank(currentChargesJSON.getString("chargeName")))) {

                            finalTxnObject.put("  "+ ++i + "." + currentChargesJSON.getString("chargeName"),
                            		currentChargesJSON.get("chargeAmount").toString() + " "
                                            + currentChargesJSON.getString("chargeCurrencyId"));
                        }
                    }
                }
            }
        }
        
        // Set Payment specific Params
        if (transactionData.has("BICId")) {
            finalTxnObject.put("Swift / BIC", transactionData.get("BICId").toString());
        }

        if (transactionData.has("scheduledDate")) {
            finalTxnObject.put("Send On", transactionData.get("scheduledDate").toString());
        }

        if (transactionData.has("paymentType")) {
            finalTxnObject.put("Payment Medium", transactionData.get("paymentType").toString());
        }

        if (transactionData.has("paidBy") && !isSameBankTransfer) {
            finalTxnObject.put("Fees Paid By", transactionData.get("paidBy").toString());
        }
        // Set STO Specific params
        if (transactionData.has("frequencyStartDate")) {
            finalTxnObject.put("Frequency Start Date", transactionData.get("frequencyStartDate").toString());
        }
        if (transactionData.has("frequencyEndDate")) {
            finalTxnObject.put("Frequency End Date", transactionData.get("frequencyEndDate").toString());
        }
        if (transactionData.has("payeeCurrency")) {
            finalTxnObject.put("Currency", transactionData.get("payeeCurrency").toString());
        }

        // Set Payment Medium
        if ((transactionData.has("serviceName"))) {
            String serviceName = transactionData.get("serviceName").toString();
            if (serviceName.equalsIgnoreCase("INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE"))
                finalTxnObject.put("Payment Method", "International");
            else if (serviceName.equalsIgnoreCase("INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE"))
                finalTxnObject.put("Payment Method", "Domestic");
            else if (serviceName.equalsIgnoreCase("INTRA_BANK_FUND_TRANSFER_CREATE"))
                finalTxnObject.put("Payment Method", "Within Bank");
            else if (serviceName.equalsIgnoreCase("P2P_CREATE"))
                finalTxnObject.put("Payment Method", "Person to Person");
            else
                finalTxnObject.put("Payment Method", "Own Account Transfer");
        }

        
        if (transactionData.has("totalDebitAmount") && StringUtils.isNotBlank(transactionData.get("totalDebitAmount").toString())) {
            finalTxnObject.put("Total Debit Amount", transactionData.get("totalDebitAmount").toString() + " " + transactionData.get("paymentCurrencyId").toString());
        }
        //Add beneficiary information
        if (transactionData.has("beneficiaryPhone")) {
            finalTxnObject.put("Beneficiary Phone", transactionData.get("beneficiaryPhone").toString());
        }
        if (transactionData.has("beneficiaryEmail")) {
            finalTxnObject.put("Beneficiary Email", transactionData.get("beneficiaryEmail").toString());
        }
        //Own account transfers download transactions
        if(transactionData.has("paymentType") && transactionData.getString("paymentType").equalsIgnoreCase("OWN_ACCOUNT_TRANSFER")) {
        	if(finalTxnObject.containsKey("Payment Currency")){
           	 	finalTxnObject.remove("Payment Currency");
           	 }
        	if(finalTxnObject.containsKey("Payment Medium")){
           	 	finalTxnObject.remove("Payment Medium");
           	 }
        }
        //Domestic/ CIPS Transfer changes
        if(transactionData.has("paymentType") && transactionData.getString("paymentType").equalsIgnoreCase("CIPS")) {
        	JSONObject additionalInfo = transactionData.has("additionalInformation")?transactionData.getJSONObject("additionalInformation"):new JSONObject();
        	String totalDebitAmountVal= transactionData.get("totalDebitAmount")!=null? transactionData.optString("totalDebitAmount"):null;
        	BigDecimal totalDebitAmount = totalDebitAmountVal!=null? new BigDecimal(totalDebitAmountVal).setScale(2):null;
        	String amountVal= transactionData.get("amount")!=null? transactionData.optString("amount"):null;
        	BigDecimal amount = amountVal!=null? new BigDecimal(amountVal).setScale(2):null;
        	if (transactionData.has("creditorAccount")) {
                finalTxnObject.put("To Account Number", MaskFields(transactionData.get("creditorAccount").toString()));
            }
        	if (transactionData.has("creditorName")) {
                finalTxnObject.put("To Account Name", transactionData.get("creditorName").toString());
            }
        	if(finalTxnObject.containsKey("Amount")){
           	 	finalTxnObject.remove("Amount");
           	 }
        	if(finalTxnObject.containsKey("Payment Currency")){
           	 	finalTxnObject.remove("Payment Currency");
           	 }
        	if(finalTxnObject.containsKey("Total Debit Amount")){
           	 	finalTxnObject.remove("Total Debit Amount");
           	 }
       	 	//finalTxnObject.put("Payment Currency", transactionData.get("debitCurrency").toString());
       	 	if(finalTxnObject.containsKey("Payment Medium")){
       	 	finalTxnObject.remove("Payment Medium");
       	 	}
       	 	finalTxnObject.put("Payment Method", transactionData.getString("paymentType"));
       		if (transactionData.has("indicativeRate")) {
       			finalTxnObject.put("Exchange Rate", transactionData.get("indicativeRate").toString());
       		}
       		if (totalDebitAmount!=null) {
                finalTxnObject.put("Transaction Amount", totalDebitAmount + " " + transactionData.get("debitCurrency").toString());
            }
       		if (transactionData.has("charges") && StringUtils.isNotBlank(transactionData.get("charges").toString())) {
            	finalTxnObject.put("Charges Breakdown:", "  ");
                String ChargesString = transactionData.get("charges").toString();
                JSONArray chargesArray = new JSONArray(ChargesString);
                if (chargesArray.length() > 0) {
                	int i=0;
                    for (Object currChargesObject : chargesArray) {
                        if (currChargesObject instanceof JSONObject) {
                            JSONObject currentChargesJSON = (JSONObject) currChargesObject;

                            if ((currentChargesJSON.has("chargeAmount")
                                    && StringUtils.isNotBlank(currentChargesJSON.get("chargeAmount").toString()))
                                    && (currentChargesJSON.has("chargeCurrencyId")
                                            && StringUtils.isNotBlank(currentChargesJSON.getString("chargeCurrencyId")))
                                    && (currentChargesJSON.has("chargeName")
                                            && StringUtils.isNotBlank(currentChargesJSON.getString("chargeName")))) {

                                finalTxnObject.put("  "+ ++i + "." + currentChargesJSON.getString("chargeName"),
                                		currentChargesJSON.get("chargeAmount").toString() + " "
                                                + currentChargesJSON.getString("chargeCurrencyId"));
                            }
                        }
                    }
                }
            }
       		if(amount!=null) {
               finalTxnObject.put("Total Debit Amount", amount + " " + transactionData.get("paymentCurrencyId").toString());
          	}
          
       }
        return finalTxnObject;
    }

    public String MaskFields(String value) {
        String Maskedvalue = StringUtils.EMPTY;
        if (value != null && !value.isEmpty()) {
            String lastFourDigits;
            if (value.length() > 4)
                lastFourDigits = value.substring(value.length() - 4);
            else
                lastFourDigits = value;

            Maskedvalue = "XXXX" + lastFourDigits;
        }
        return Maskedvalue;
    }

}
