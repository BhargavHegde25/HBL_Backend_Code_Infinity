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
import com.konylabs.middleware.dataobject.ResultToJSON;


public class DownloadChangeRepaymentDayReqAckPDF extends TemenosBaseService {
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
            String loanDetails = CommonUtils.getParamValue(params, "loanDetails");

            if (params == null) {
                CommonUtils.setErrMsg(localResult, "No input parameters provided");
                CommonUtils.setOpStatusError(localResult);
                return localResult;
            }

           
            LinkedHashMap<String, Object> repaymentDayDetails = changeRepaymentDayDetails(request);
            
            JSONArray loanDetailsJson = new JSONArray(loanDetails);
			
			

            result = new Result();
            if(contentType != null && contentType.equalsIgnoreCase("pdf")) {
                TemenosUtils temenosUtils = TemenosUtils.getInstance();
                byte[] bytes = new PDFGeneratorChangeRepaymentDayReqAck(repaymentDayDetails,loanDetailsJson).generateFileAndGetByteArray();
                String fileId = TransferUtils.getUniqueNumericString(10);
                temenosUtils.saveIntoCache(fileId, bytes, 120);
                response.setStatusCode(HttpStatus.SC_OK);
                result.addParam(new Param("fileId",fileId));
                alert.prepareError("Download PDF Data has been stored successfully").log();
            }
            else {
            	result.addParam(new Param("pdf",
                        new PDFGeneratorChangeRepaymentDayReqAck(repaymentDayDetails,loanDetailsJson).generateFileAndGetBase64(),
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
        customHeaders.put("Content-Disposition", "attachment; filename=\"Change Repayment Day Report.pdf\"");
        return customHeaders;
	}
    public LinkedHashMap<String, Object> changeRepaymentDayDetails(DataControllerRequest request) {
    	LinkedHashMap<String, Object> finalRepayementObject = new LinkedHashMap<String, Object>();
		
		String referenceNumber = request.getParameter("referenceNumber") != null ? request.getParameter("referenceNumber") : "";
		if(StringUtils.isNotBlank(referenceNumber)) {
			finalRepayementObject.put("Reference Number", referenceNumber);
		}
		String facilityName = request.getParameter("facilityName") != null ? request.getParameter("facilityName") : "";
		if(StringUtils.isNotBlank(referenceNumber)) {
			finalRepayementObject.put("Facility Name", facilityName);
		}
		String numberOfLoans = request.getParameter("numberOfLoans") != null ? request.getParameter("numberOfLoans") : "";
		if(StringUtils.isNotBlank(referenceNumber)) {
			finalRepayementObject.put("Number Of Loans", numberOfLoans);
		}
		String outstandingBalance = request.getParameter("outstandingBalance") != null ? request.getParameter("outstandingBalance") : "";
		if(StringUtils.isNotBlank(referenceNumber)) {
			finalRepayementObject.put("Outstanding Balance", outstandingBalance);
		}
		String maturityDate = request.getParameter("maturityDate") != null ? request.getParameter("maturityDate") : "";
		if(StringUtils.isNotBlank(referenceNumber)) {
			finalRepayementObject.put("Maturity Date", maturityDate);
		}
		

		
		return finalRepayementObject;
	}


    
   

}
