package com.infinity.dbx.temenos.fileutil;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
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
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

public class DownloadPDF extends TemenosBaseService {
    private static final String TRANSACTIONS_ARRAY_KEY = "Transactions";
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

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
            String paymentType = CommonUtils.getParamValue(params, "paymentType");
            alert.prepareError("Download PDF request Params:"+params).log();
            if (params == null || transactionId == null) {
                CommonUtils.setErrMsg(localResult, "No input parameters provided");
                CommonUtils.setOpStatusError(localResult);
                return localResult;
            }
            //Temporary Fix-PaymentType field expected from frontend
            if(StringUtils.isBlank(paymentType)&& !transactionId.contains("PI"))
            paymentType="NON_CIPS";
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
			} else if(srmsFlag.equalsIgnoreCase("STUB")){
				result=constructSTUBTransactionData(result);
			}
			else if ( Constants.FREQUENCY_ONCE.equalsIgnoreCase(transactionType) && paymentType.equalsIgnoreCase("NON_CIPS")) {
				serviceName="HBLOtherbankTrnsferJavaService";
				operationName = "getCIPSTransactionsById";
				result = CommonUtils.callIntegrationService(request, params, serviceHeaders, serviceName, operationName,
						false);
			}
			else if (Constants.FREQUENCY_ONCE.equalsIgnoreCase(transactionType)) {
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
    
    private Result constructSTUBTransactionData(Result result) {
    	Dataset ds = new Dataset();
		ds.setId("Transactions");
		Record record = new Record();
		record.addStringParam("chargeBearer", "OUR");
		record.addStringParam("amount", "1");
		record.addStringParam("fromAccountNumber", "105627");
		record.addStringParam("scheduledDate", "2023-04-20");
		record.addStringParam("transactionCurrency", "EUR");
		record.addStringParam("fromAccountName", "John Kelly");
		record.addStringParam("currentStatus", "WareHouseOrder");
		record.addStringParam("endToEndReference", "NOTPROVIDED");
		record.addStringParam("fromAccountIBAN", "GB74DEMO60161300105627");
		record.addStringParam("transactionId", "12345");
		record.addStringParam("transactionType", "ExternalTransfer");
		record.addStringParam("paymentCurrencyId", "EUR");
		record.addStringParam("toAccountName", "International");
		record.addStringParam("fromAccountCurrency", "EUR");
		record.addStringParam("ExternalAccountNumber", "23456789");
		record.addStringParam("orderingCustomerId", "100600");
		record.addStringParam("toAccountNumber", "Brice Bamford");
		record.addStringParam("frequencyType", "Once");
		record.addStringParam("isScheduled", "false");
		record.addStringParam("paidBy", "Self");
		record.addStringParam("statusDescription", "Scheduled");
		record.addStringParam("pendingApproval", "false");
		record.addStringParam("serviceName", "INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE");
		ds.addRecord(record);
		result.addDataset(ds);

		return result;
	}

	private Map<String, String> getCustomHeaders() {
		Map<String, String> customHeaders = new HashMap<>();
        customHeaders.put(HttpHeaders.CONTENT_TYPE, "application/pdf");
        customHeaders.put("Content-Disposition", "attachment; filename=\"Transaction Report.pdf\"");
        return customHeaders;
	}

    public LinkedHashMap<String, Object> transforTransactionDate(JSONObject transactionData, String transactionType) {
        boolean isSameBankTransfer = transactionData.has("serviceName") &&
                (transactionData.getString("serviceName").equals("TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE")
                || transactionData.getString("serviceName").equals("INTRA_BANK_FUND_TRANSFER_CREATE"));
        // Check Transaction Type
    	LinkedHashMap<String, Object> finalTxnObject = new LinkedHashMap<String, Object>();
        // Set Reference and From details
        if (transactionData.has("transactionId")) {
            finalTxnObject.put("Reference Number", transactionData.get("transactionId").toString());
        }
        if (transactionData.has("status")) {
            finalTxnObject.put("Payment Status", transactionData.get("status").toString());
        }
        // Set Debit Details
        if (transactionData.has("fromAccountNumber")) {
            finalTxnObject.put("From Account Number", MaskFields(transactionData.get("fromAccountNumber").toString()));
        }
        if (transactionData.has("fromAccountName")) {
            finalTxnObject.put("From Account Name", transactionData.get("fromAccountName").toString());
        }
        if (transactionData.has("description")) {
            finalTxnObject.put("Description", transactionData.get("description").toString());
        }
        if (transactionData.has("notes")) {
            finalTxnObject.put("Description", transactionData.get("notes").toString());
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
        
        if (transactionData.has("toAccountType")) {
            finalTxnObject.put("Bank Name", transactionData.get("toAccountType").toString());
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
        
        if (transactionData.has("transactionAmount")) {
            finalTxnObject.put("Transaction Amount", transactionData.get("transactionAmount").toString() + " " + transactionData.get("paymentCurrencyId").toString());
        }
        
        if (transactionData.has("serviceCharge")) {
            finalTxnObject.put("Service Fee", transactionData.get("serviceCharge").toString() + " " + transactionData.get("paymentCurrencyId").toString());
        }
        //Add beneficiary information
        if (transactionData.has("beneficiaryPhone")) {
            finalTxnObject.put("Beneficiary Phone", transactionData.get("beneficiaryPhone").toString());
        }
        if (transactionData.has("beneficiaryEmail")) {
            finalTxnObject.put("Beneficiary Email", transactionData.get("beneficiaryEmail").toString());
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
