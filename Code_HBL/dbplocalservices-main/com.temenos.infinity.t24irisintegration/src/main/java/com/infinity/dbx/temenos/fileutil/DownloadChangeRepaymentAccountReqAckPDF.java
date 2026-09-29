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

import com.infinity.dbx.temenos.TemenosBaseService;
import com.infinity.dbx.temenos.transfers.TransferUtils;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class DownloadChangeRepaymentAccountReqAckPDF extends TemenosBaseService {
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

           
            LinkedHashMap<String, Object> repaymentAccountDetails = changeRepaymentAccountDetails(request);
            
            
			
			

            result = new Result();
            if(contentType != null && contentType.equalsIgnoreCase("pdf")) {
                TemenosUtils temenosUtils = TemenosUtils.getInstance();
                byte[] bytes = new PDFGeneratorChangeRepaymentAccountReqAck(repaymentAccountDetails).generateFileAndGetByteArray();
                String fileId = TransferUtils.getUniqueNumericString(10);
                temenosUtils.saveIntoCache(fileId, bytes, 120);
                response.setStatusCode(HttpStatus.SC_OK);
                result.addParam(new Param("fileId",fileId));
                alert.prepareError("Download PDF Data has been stored successfully").log();
            }
            else {
            	result.addParam(new Param("pdf",
                        new PDFGeneratorChangeRepaymentAccountReqAck(repaymentAccountDetails).generateFileAndGetBase64(),
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
        customHeaders.put("Content-Disposition", "attachment; filename=\"Change Repayment Account Report.pdf\"");
        return customHeaders;
	}
    public LinkedHashMap<String, Object> changeRepaymentAccountDetails(DataControllerRequest request) {
    	LinkedHashMap<String, Object> finalRepayementObject = new LinkedHashMap<String, Object>();
		
		String referenceNumber = request.getParameter("referenceNumber") != null ? request.getParameter("referenceNumber") : "";
		if(StringUtils.isNotBlank(referenceNumber)) {
			finalRepayementObject.put("Reference Number", referenceNumber);
		}
		String loanName = request.getParameter("loanName") != null ? request.getParameter("loanName") : "";
		if(StringUtils.isNotBlank(referenceNumber)) {
			finalRepayementObject.put("Loan Name", loanName);
		}
		String newRepaymentAccountHolderName = request.getParameter("newRepaymentAccountHolderName") != null ? request.getParameter("newRepaymentAccountHolderName") : "";
		if(StringUtils.isNotBlank(referenceNumber)) {
			finalRepayementObject.put("New Repayment Account Holder Name", newRepaymentAccountHolderName);
		}
		String newRepaymentAccountNumber = request.getParameter("newRepaymentAccountNumber") != null ? request.getParameter("newRepaymentAccountNumber") : "";
		if(StringUtils.isNotBlank(referenceNumber)) {
			finalRepayementObject.put("New Repayment Account Number", newRepaymentAccountNumber);
		}
		String bankName = request.getParameter("bankName") != null ? request.getParameter("bankName") : "";
		if(StringUtils.isNotBlank(referenceNumber)) {
			finalRepayementObject.put("Bank Name", bankName);
		}
		

		
		return finalRepayementObject;
	}


    
   

}
