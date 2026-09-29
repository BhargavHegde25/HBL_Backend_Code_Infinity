package com.temenos.infinity.api.accountsweeps.javaservice;

import com.infinity.dbx.temenos.TemenosBaseService;
import com.infinity.dbx.temenos.fileutil.PDFGeneratorTransactionAcknowledgement;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;
import com.kony.dbputilities.util.Log4j2Configurator;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class DownloadAccountSweepPDF extends TemenosBaseService {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
                         DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();

        Result result = new Result();
        try {

            Result localResult = (Result) super.invoke(methodId, inputArray, request, response);
            HashMap<String, Object> params = (HashMap<String, Object>) inputArray[1];
            if (params == null) {
                CommonUtils.setErrMsg(localResult, "No input parameters provided");
                CommonUtils.setOpStatusError(localResult);
                return localResult;
            }

            result = new Result();
            JSONObject transaction=new JSONObject(params);
            LinkedHashMap<String, Object> transactDetails = transforTransactionDate(transaction);
                TemenosUtils temenosUtils = TemenosUtils.getInstance();
                byte[] bytes = new PDFGeneratorTransactionAcknowledgement(transactDetails).generateFileAndGetByteArray();
                String fileId = TransferUtils.getUniqueNumericString(10);
                temenosUtils.saveIntoCache(fileId, bytes, 120);
                response.setStatusCode(HttpStatus.SC_OK);
                result.addParam(new Param("fileId",fileId));
                alert.prepareError("Download PDF Data has been stored successfully").log();

        } catch (Exception e) {
            Result errorResult = new Result();
            alert.prepareError("Exception Occured while DownloadPDF" + e).log();
            CommonUtils.setOpStatusError(result);
            CommonUtils.setErrMsg(errorResult, e.getMessage());
            return errorResult;
        }
        return result;
    }


    public LinkedHashMap<String, Object> transforTransactionDate(JSONObject transactionData) {
        LinkedHashMap<String, Object> finalTxnObject = new LinkedHashMap<String, Object>();
        // Set Reference and From details
        if (transactionData.has("confirmationNumber")) {
            finalTxnObject.put("Reference Number", transactionData.get("confirmationNumber").toString());
        }
        // Set Debit Details
        if (transactionData.has("primaryAccountNumber")) {
            finalTxnObject.put("Primary Account Number", MaskFields(transactionData.get("primaryAccountNumber").toString()));
        }
        if (transactionData.has("primaryAccountName")) {
            finalTxnObject.put("Primary Account Name", transactionData.get("primaryAccountName").toString());
        }

        if (transactionData.has("secondaryAccountNumber")) {
            finalTxnObject.put("Secondary Account Number", MaskFields(transactionData.get("secondaryAccountNumber").toString()));
        }

        if (transactionData.has("secondaryAccountName")) {
            finalTxnObject.put("Secondary Account Name", transactionData.get("secondaryAccountName").toString());
        }

        if (transactionData.has("belowSweepAmount") && !transactionData.get("belowSweepAmount").equals("")) {
            finalTxnObject.put("Minimum Lower Balance", transactionData.get("belowSweepAmount").toString()+ " " + transactionData.get("currencyCode").toString());
        }
        if (transactionData.has("aboveSweepAmount")&& !transactionData.get("aboveSweepAmount").equals("")) {
            finalTxnObject.put("Minimum Upper balance", transactionData.get("aboveSweepAmount").toString()+ " " + transactionData.get("currencyCode").toString());
        }
        if (transactionData.has("currencyCode")) {
            finalTxnObject.put("Payment Currency", transactionData.get("currencyCode").toString());
        }
        if (transactionData.has("frequency")) {
                finalTxnObject.put("Frequency Type", transactionData.get("frequency").toString());
        }

        if (transactionData.has("startDate")) {
            finalTxnObject.put("Start Date", transactionData.get("startDate").toString());
        }

        String endDate=String.valueOf(transactionData.get("endDate"));
        if (transactionData.has("endDate") && !endDate.equals("")) {
            finalTxnObject.put("End Date", endDate);
        } else {
            finalTxnObject.put("End Date", "End Manually");
        }


        if ((transactionData.has("aboveSweepAmount") && !transactionData.get("aboveSweepAmount").equals(""))
                && (transactionData.has("belowSweepAmount")&& !transactionData.get("belowSweepAmount").equals(""))) {
            finalTxnObject.put("Sweep Condition", "Both");
        } else {
            if (transactionData.has("aboveSweepAmount") && !transactionData.get("aboveSweepAmount").equals("")) {
                finalTxnObject.put("Sweep Condition", "Above");
            }
            if (transactionData.has("belowSweepAmount") && !transactionData.get("belowSweepAmount").equals("")) {
                finalTxnObject.put("Sweep Condition", "Below");
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
