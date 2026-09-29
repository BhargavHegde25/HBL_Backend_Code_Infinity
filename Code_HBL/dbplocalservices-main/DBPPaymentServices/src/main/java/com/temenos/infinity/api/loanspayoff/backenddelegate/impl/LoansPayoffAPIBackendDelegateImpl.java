package com.temenos.infinity.api.loanspayoff.backenddelegate.impl;

import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.commons.invocation.Executor;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.loanspayoff.backenddelegate.api.LoansPayoffAPIBackendDelegate;
import com.temenos.infinity.api.loanspayoff.config.LoansPayoffAPIServices;
import com.temenos.infinity.api.loanspayoff.constants.ErrorCodeEnum;
import com.temenos.infinity.api.loanspayoff.dto.BillDetails;
import com.temenos.infinity.api.loanspayoff.dto.LoanDTO;

public class LoansPayoffAPIBackendDelegateImpl implements LoansPayoffAPIBackendDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static String Payment_Backend = EnvironmentConfigurationsHandler.getValue("PAYMENT_BACKEND");
    @Override
    public LoanDTO createSimulationT24(String arrangementId, String activityId, String productId, String effectiveDate,
            String backendToken) throws ApplicationException {
        LoanDTO outputDto = new LoanDTO();
        try {
            Map<String, Object> inputMap = new HashMap<>();
            Map<String, Object> headerMap = new HashMap<>();
            inputMap.put("arrangementId", arrangementId);
            inputMap.put("activityId", activityId);
            inputMap.put("productId", productId);
            inputMap.put("effectiveDate", effectiveDate);
            // String backendToken = TokenGenerator.generateAuthToken(T24CertificateConstants.BACKEND,
            // ServerConfigurations.DBP_HOST_URL.getValue(), LoggedInUserHandler.getUserDetails().getUserName(),
            // LoggedInUserHandler.getUserDetails().getUserId(), T24CertificateConstants.ROLEID,
            // T24CertificateConstants.TOKEN_VALIDITY, false);
            // String backendToken =
            // "eyJraWQiOiJLT05ZIiwiYWxnIjoiUlMyNTYifQ.eyJhdWQiOiJJUklTIiwic3ViIjoiZGJwb2xidXNlciIsInJvbGVJZCI6IklORklOSVRZLlJFVEFJTCIsImlzcyI6IkZhYnJpYyIsImRieFVzZXJJZCI6IjEwMjY1NDAiLCJleHAiOjE2ODA3OTUwNzMsImlhdCI6MTU4MDc5MzI3MywidXNlcklkIjoiZGJwb2xidXNlciIsImp0aSI6Ijg0ZDBjOTY3LTA1NWItNDYyZS1iOWU5LTc2Y2UxZjYwM2M1MCJ9.ie5b4g-uwdzIpIVbmwhy4AE-z0kXRTOPR_46ZVndaxjCoRpBjqtsgp5SI3Ovlnt-K4xoolWXvwXk4pEmpbOrytuNLnJdI3QeoCp4jumu-68eznkLc9GHlnmNU7zbjtRjiNp-8zW1K_Rt6BCs-xcXKtKSzEdlya4LOqrtMgAdj16jIAO8uYRK9YKD0BZFSouRdP_cPJk0ahBuk14Xji4T_8q1VcFfXKqasggKwJWrPkckQ8fBnyOJpXSND3GZ2rpewGyHVxkHU8b3Qpvcw3cT34rlqCGZzhISDUzUeGBuEGPfZYdyH3hA0G_dwGlsGsSSjIQH-pe-NRSL9B4ws7NruA";
            headerMap.put("backendToken", backendToken);
            String status;
            JSONObject serviceResponseJSON = null;
            if("MOCK".equals(Payment_Backend) || "SRMS_MOCK".equals(Payment_Backend)) {
            	status = "success";
            }
            else {
	            String serviceResponse =
	                    Executor.invokeService(LoansPayoffAPIServices.LOANSERVICEJSON_CREATESIMULATION, inputMap, headerMap);
	            serviceResponseJSON = Utilities.convertStringToJSON(serviceResponse);
	            if (serviceResponseJSON == null) {
	                throw new ApplicationException(ErrorCodeEnum.ERR_20041);
	            }
	            JSONObject data = serviceResponseJSON.getJSONObject("header");        
	            status = data.getString("status");
            }
            if (status.equalsIgnoreCase("success")) {
                outputDto.setStatus("success");
            } else {
                outputDto.setStatus("failure");
                alert.prepareError(outputDto.toString()).log();
                if (serviceResponseJSON.getJSONObject("override") != null
                        && !serviceResponseJSON.getJSONObject("override").toString().equals("{}")) {
                    JSONObject override = serviceResponseJSON.getJSONObject("override");
                    outputDto.setBackendOverride(override.toString());
                }
                if (serviceResponseJSON.getJSONObject("error") != null
                        && !serviceResponseJSON.getJSONObject("error").toString().equals("{}")) {
                    JSONObject error = serviceResponseJSON.getJSONObject("error");
                    outputDto.setBackendError(error.toString());
                }

            }
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20400);
        }
        return outputDto;
    }

    @Override
    public BillDetails getBillDetailsT24(String arrangementId, String billType, String paymentDate, String backendToken)
            throws ApplicationException {
        BillDetails outputDto = new BillDetails();
        try {
            Map<String, Object> inputMap = new HashMap<>();
            Map<String, Object> headerMap = new HashMap<>();
            inputMap.put("arrangementId", arrangementId);
            inputMap.put("billType", billType);
            inputMap.put("paymentDate", paymentDate);
            // String backendToken = TokenGenerator.generateAuthToken(T24CertificateConstants.BACKEND,
            // ServerConfigurations.DBP_HOST_URL.getValue(), LoggedInUserHandler.getUserDetails().getUserName(),
            // LoggedInUserHandler.getUserDetails().getUserId(), T24CertificateConstants.ROLEID,
            // T24CertificateConstants.TOKEN_VALIDITY, false);
            // String backendToken =
            // "eyJraWQiOiJLT05ZIiwiYWxnIjoiUlMyNTYifQ.eyJhdWQiOiJJUklTIiwic3ViIjoiZGJwb2xidXNlciIsInJvbGVJZCI6IklORklOSVRZLlJFVEFJTCIsImlzcyI6IkZhYnJpYyIsImRieFVzZXJJZCI6IjEwMjY1NDAiLCJleHAiOjE2ODA3OTUwNzMsImlhdCI6MTU4MDc5MzI3MywidXNlcklkIjoiZGJwb2xidXNlciIsImp0aSI6Ijg0ZDBjOTY3LTA1NWItNDYyZS1iOWU5LTc2Y2UxZjYwM2M1MCJ9.ie5b4g-uwdzIpIVbmwhy4AE-z0kXRTOPR_46ZVndaxjCoRpBjqtsgp5SI3Ovlnt-K4xoolWXvwXk4pEmpbOrytuNLnJdI3QeoCp4jumu-68eznkLc9GHlnmNU7zbjtRjiNp-8zW1K_Rt6BCs-xcXKtKSzEdlya4LOqrtMgAdj16jIAO8uYRK9YKD0BZFSouRdP_cPJk0ahBuk14Xji4T_8q1VcFfXKqasggKwJWrPkckQ8fBnyOJpXSND3GZ2rpewGyHVxkHU8b3Qpvcw3cT34rlqCGZzhISDUzUeGBuEGPfZYdyH3hA0G_dwGlsGsSSjIQH-pe-NRSL9B4ws7NruA";
            headerMap.put("backendToken", backendToken);
            ObjectMapper mapper = new ObjectMapper();
            String serviceResponse;
            if("MOCK".equals(Payment_Backend) || "SRMS_MOCK".equals(Payment_Backend)) {
            	  serviceResponse =
                         Executor.invokeService(LoansPayoffAPIServices.LOANMOCKSERVICE_GETBILLDETAILS, inputMap, headerMap);
            	  return  mapper.readValue(serviceResponse.toString(), BillDetails.class);
            	  
            }else {
             serviceResponse =
                    Executor.invokeService(LoansPayoffAPIServices.LOANSERVICEJSON_GETBILLDETAILS, inputMap, headerMap);
            }
            JSONObject serviceResponseJSON = Utilities.convertStringToJSON(serviceResponse);
            if (serviceResponseJSON == null) {
                throw new ApplicationException(ErrorCodeEnum.ERR_20041);
            }
           
            if (serviceResponseJSON.getJSONArray("body") != null
                    && serviceResponseJSON.getJSONArray("body").length() > 0
                    && serviceResponseJSON.getJSONObject("error").toString().equals("{}")) {
                if (serviceResponseJSON.getJSONArray("body").get(0) != null) {
                    JSONObject body = (JSONObject) serviceResponseJSON.getJSONArray("body").get(0);
                    outputDto = mapper.readValue(body.toString(), BillDetails.class);
                }
            } else {

                if (serviceResponseJSON.getJSONObject("override") != null
                        && !serviceResponseJSON.getJSONObject("override").toString().equals("{}")) {
                    JSONObject override = serviceResponseJSON.getJSONObject("override");
                    outputDto.setBackendOverride(override.toString());
                }
                if (serviceResponseJSON.getJSONObject("error") != null
                        && !serviceResponseJSON.getJSONObject("error").toString().equals("{}")) {
                    JSONObject error = serviceResponseJSON.getJSONObject("error");
                    alert.prepareError(error.toString()).log();
                    outputDto.setBackendError(error.toString());
                }
            }
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20400);
        }
        return outputDto;
    }
}
