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
import com.infinity.dbx.temenos.transfers.TransferUtils;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

/**
 * @author mahesh.bikkina
 *
 */
public class DownloadPartialRepaymentAccountReqAckPDF extends TemenosBaseService {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {

        Result result = new Result();
        try {

            Result localResult = (Result) super.invoke(methodId, inputArray, request, response);
            HashMap<String, Object> params = (HashMap<String, Object>) inputArray[1];
            String contentType = CommonUtils.getParamValue(params, Constants.CONTENT_TYPE);
    
            if (params == null) {
                CommonUtils.setErrMsg(localResult, "No input parameters provided");
                CommonUtils.setOpStatusError(localResult);
                return localResult;
            }

           
            LinkedHashMap<String, Object> mortgageFacilityDetails = mortgageFacilityDetails(request);
            JSONArray mortgageCurrentValues = getCurrentValues(request);
            JSONArray mortgageSimulatedValues = getSimulatedValues(request);
            LinkedHashMap<String, Object> paymentDetails = paymentDetails(request);

            result = new Result();
            if(contentType != null && contentType.equalsIgnoreCase("pdf")) {
                TemenosUtils temenosUtils = TemenosUtils.getInstance();
                byte[] bytes = new PDFGeneratorPartialRepaymentAck(paymentDetails,mortgageSimulatedValues,mortgageCurrentValues,mortgageFacilityDetails).generateFileAndGetByteArray();
                String fileId = TransferUtils.getUniqueNumericString(10);
                temenosUtils.saveIntoCache(fileId, bytes, 120);
                response.setStatusCode(HttpStatus.SC_OK);
                result.addParam(new Param("fileId",fileId));
                alert.prepareError("Download PDF Data has been stored successfully").log();
            }
            else {
            	result.addParam(new Param("pdf",
                        new PDFGeneratorPartialRepaymentAck(paymentDetails,mortgageSimulatedValues,mortgageCurrentValues,mortgageFacilityDetails).generateFileAndGetBase64(),
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
    
    private JSONArray getCurrentValues(DataControllerRequest request) {
    	JSONArray currentValues = new JSONArray();
    	JSONObject currentValuesObj = new JSONObject();
    	String facilityName = request.getParameter("facilityName");
		String currentInstallmentAmount = request.getParameter("currentInstallmentAmount") != null ? request.getParameter("currentInstallmentAmount") : "";
		if(StringUtils.isNotBlank(facilityName)) {
			currentValuesObj.put("Installment Amount", currentInstallmentAmount);
		}
		String currentNextRepaymentDate = request.getParameter("currentNextRepaymentDate") != null ? request.getParameter("currentNextRepaymentDate") : "";
		if(StringUtils.isNotBlank(facilityName)) {
			currentValuesObj.put("Next Repayment Date", currentNextRepaymentDate);
		}
		String currentEndDate = request.getParameter("currentEndDate") != null ? request.getParameter("currentEndDate") : "";
		if(StringUtils.isNotBlank(facilityName)) {
			currentValuesObj.put("EndDate", currentEndDate);
		}
		
    	currentValues.put(currentValuesObj);
    	return currentValues;
    }
    private JSONArray getSimulatedValues(DataControllerRequest request) {
    	JSONArray simulatedValues = new JSONArray();
    	JSONObject simulatedValuesObj = new JSONObject();
    	String facilityName = request.getParameter("facilityName");
		String simulatedInstallmentAmount = request.getParameter("installmentAmount") != null ? request.getParameter("installmentAmount") : "";
		if(StringUtils.isNotBlank(facilityName)) {
			simulatedValuesObj.put("Installment Amount", simulatedInstallmentAmount);
		}
		String simulatedNextRepaymentDate = request.getParameter("nextRepaymentDate") != null ? request.getParameter("nextRepaymentDate") : "";
		if(StringUtils.isNotBlank(facilityName)) {
			simulatedValuesObj.put("Next Repayment Date", simulatedNextRepaymentDate);
		}
		String simulatedEndDate = request.getParameter("endDate") != null ? request.getParameter("endDate") : "";
		if(StringUtils.isNotBlank(facilityName)) {
			simulatedValuesObj.put("EndDate", simulatedEndDate);
		}
		
		simulatedValues.put(simulatedValuesObj);
    	return simulatedValues;
    }
    
    private Map<String, String> getCustomHeaders() {
		Map<String, String> customHeaders = new HashMap<>();
        customHeaders.put(HttpHeaders.CONTENT_TYPE, "application/pdf");
        customHeaders.put("Content-Disposition", "attachment; filename=\"Change Repayment Account Report.pdf\"");
        return customHeaders;
	}
    
    public LinkedHashMap<String, Object> mortgageFacilityDetails(DataControllerRequest request) {
    	LinkedHashMap<String, Object> mortgageFacilitydetails = new LinkedHashMap<String, Object>();
		
		String facilityName = request.getParameter("facilityName") != null ? request.getParameter("facilityName") : "";
		if(StringUtils.isNotBlank(facilityName)) {
			mortgageFacilitydetails.put("Facility Name", facilityName);
		}
		String numberOfLoans = request.getParameter("numberOfLoans") != null ? request.getParameter("numberOfLoans") : "";
		if(StringUtils.isNotBlank(facilityName)) {
			mortgageFacilitydetails.put("Number Of Loans", numberOfLoans);
		}
		String outstandingBalance = request.getParameter("outstandingBalance") != null ? request.getParameter("outstandingBalance") : "";
		if(StringUtils.isNotBlank(facilityName)) {
			mortgageFacilitydetails.put("Outstanding Balance", outstandingBalance);
		}
		String amountPaidtoDate = request.getParameter("amountPaidtoDate") != null ? request.getParameter("amountPaidtoDate") : "";
		if(StringUtils.isNotBlank(facilityName)) {
			mortgageFacilitydetails.put("Amount Paid to Date", amountPaidtoDate);
		}
		String reductionIn = request.getParameter("reductionIn") != null ? request.getParameter("reductionIn") : "";
		if(StringUtils.isNotBlank(facilityName)) {
			mortgageFacilitydetails.put("ReductionIn", reductionIn);
		}
		String amount = request.getParameter("amount") != null ? request.getParameter("amount") : "";
		if(StringUtils.isNotBlank(facilityName)) {
			mortgageFacilitydetails.put("Amount", amount);
		}
		

		
		return mortgageFacilitydetails;
	} 
    
    public LinkedHashMap<String, Object> paymentDetails(DataControllerRequest request) {
    	LinkedHashMap<String, Object> paymentdetails = new LinkedHashMap<String, Object>();
    	
    	String facilityName = request.getParameter("facilityName") != null ? request.getParameter("facilityName") : "";
		
		String totalRepaymentAmount = request.getParameter("totalRepaymentAmount") != null ? request.getParameter("totalRepaymentAmount") : "";
		if(StringUtils.isNotBlank(facilityName)) {
			paymentdetails.put("Total Repayment Amount", totalRepaymentAmount);
		}
		String creditValueDate = request.getParameter("creditValueDate") != null ? request.getParameter("creditValueDate") : "";
		if(StringUtils.isNotBlank(facilityName)) {
			paymentdetails.put("Credit Value Date", creditValueDate);
		}
		String transactionFee = request.getParameter("transactionFee") != null ? request.getParameter("transactionFee") : "";
		if(StringUtils.isNotBlank(facilityName)) {
			paymentdetails.put("Transaction Fee", transactionFee);
		}
		String exchangeRate = request.getParameter("exchangeRate") != null ? request.getParameter("exchangeRate") : "";
		if(StringUtils.isNotBlank(facilityName)) {
			paymentdetails.put("Exchange Rate", exchangeRate);
		}
		String accountHolderName = request.getParameter("accountHolderName") != null ? request.getParameter("accountHolderName") : "";
		if(StringUtils.isNotBlank(facilityName)) {
			paymentdetails.put("Account Holder Name", accountHolderName);
		}
		String loanAccount = request.getParameter("loanAccNumber") != null ? request.getParameter("loanAccNumber") : "";
		if(StringUtils.isNotBlank(facilityName)) {
			paymentdetails.put("Loan Account", loanAccount);
		}
		String accountNumber = request.getParameter("accountNumber") != null ? request.getParameter("accountNumber") : "";
		if(StringUtils.isNotBlank(facilityName)) {
			paymentdetails.put("Account Number", accountNumber);
		}
		String payOn = request.getParameter("payOn") != null ? request.getParameter("payOn") : "";
		if(StringUtils.isNotBlank(facilityName)) {
			paymentdetails.put("Pay On", payOn);
		}
		String notes = request.getParameter("notes") != null ? request.getParameter("notes") : "";
		if(StringUtils.isNotBlank(facilityName)) {
			paymentdetails.put("Notes", notes);
		}
		String supportingDocuments = request.getParameter("supportingDocuments") != null ? request.getParameter("supportingDocuments") : "";
		if(StringUtils.isNotBlank(facilityName)) {
			paymentdetails.put("SupportingDocuments", supportingDocuments);
		}
		

		
		return paymentdetails;
	}  

}