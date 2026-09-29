package com.temenos.infinity.tradefinanceservices.backenddelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.LetterOfCreditsBackendDelegate;
import com.temenos.infinity.tradefinanceservices.config.TradeFinanceAPIServices;
import com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants;
import com.temenos.infinity.tradefinanceservices.dto.ApprovalRequestDTO;
import com.temenos.infinity.tradefinanceservices.dto.LetterOfCreditsActionDTO;
import com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils;

public class LetterOfCreditsBackendDelegateImpl implements LetterOfCreditsBackendDelegate{

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public LetterOfCreditsActionDTO rejectLetterOfCredit(DataControllerRequest request) throws ApplicationException {
		LetterOfCreditsActionDTO letterOfCreditDto = new LetterOfCreditsActionDTO();

        HashMap<String, Object> inputMap = new HashMap<String, Object>();
        
        if (StringUtils.isNotBlank(request.getParameter("requestId"))) {
        	inputMap.put("serviceRequestId", request.getParameter("requestId"));
		}
        if (StringUtils.isNotBlank(request.getParameter("comments"))) {
			inputMap.put("comments", request.getParameter("comments"));
		}

        // Set Header Map
        HashMap<String, Object> headerMap = new HashMap<>();
        headerMap.put("X-Kony-Authorization", request.getHeader("X-Kony-Authorization"));

        // SRMS Call
        String letterOfCreditResponse = null;
        JSONObject Response = new JSONObject();
        try {
        	letterOfCreditResponse = DBPServiceExecutorBuilder.builder()
                    .withServiceId(TradeFinanceAPIServices.SERVICEREQUESTJAVASERVICE_REJECT.getServiceName())
                    .withOperationId(TradeFinanceAPIServices.SERVICEREQUESTJAVASERVICE_REJECT.getOperationName())
                    .withRequestParameters(inputMap).withRequestHeaders(headerMap).withDataControllerRequest(request)
                    .build().getResponse();

        } catch (Exception e) {
            alert.prepareError("Unable to reject cheque book request order " + e).log();
          //  throw new ApplicationException(ErrorCodeEnum.ERRCHQ_26019);
        }

        if (StringUtils.isNotBlank(letterOfCreditResponse)) {
            Response = new JSONObject(letterOfCreditResponse);
        }

        if (Response.has(TradeFinanceConstants.PARAM_REQUEST_ID)
                && StringUtils.isNotBlank(Response.getString(TradeFinanceConstants.PARAM_REQUEST_ID))) {
        	letterOfCreditDto.setRequestId(Response.getString("requestId"));
        	letterOfCreditDto.setComments(Response.getString("comments"));
        }
        else {
            if (Response.has("dbpErrCode"))
            	letterOfCreditDto.setDbpErrCode(Response.getInt("dbpErrCode"));
            if (Response.has("dbpErrMsg"))
            	letterOfCreditDto.setDbpErrMsg(Response.getString("dbpErrMsg"));
        }
        return letterOfCreditDto;
	}

	@Override
	public LetterOfCreditsActionDTO withdrawLetterOfCredit(DataControllerRequest request) throws ApplicationException {
		LetterOfCreditsActionDTO letterOfCreditDto = new LetterOfCreditsActionDTO();

        HashMap<String, Object> inputMap = new HashMap<String, Object>();
        
        if (StringUtils.isNotBlank(request.getParameter("requestId"))) {
        	inputMap.put("serviceRequestId", request.getParameter("requestId"));
		}
        if (StringUtils.isNotBlank(request.getParameter("comments"))) {
			inputMap.put("comments", request.getParameter("comments"));
		}

        // Set Header Map
        HashMap<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put("X-Kony-Authorization", request.getHeader("X-Kony-Authorization"));

        // SRMS Call
        
        String letterOfCreditResponse = null;
        JSONObject Response = new JSONObject();
        try {
        	letterOfCreditResponse = DBPServiceExecutorBuilder.builder()
                    .withServiceId(TradeFinanceAPIServices.SERVICEREQUESTJAVASERVICE_WITHDRAW.getServiceName())
                    .withOperationId(TradeFinanceAPIServices.SERVICEREQUESTJAVASERVICE_WITHDRAW.getOperationName())
                    .withRequestParameters(inputMap).withRequestHeaders(headerMap).withDataControllerRequest(request)
                    .build().getResponse();

        } catch (Exception e) {
            alert.prepareError("Error occurred withdrawing cheque book request " + e).log();
           // throw new ApplicationException(ErrorCodeEnum.ERRCHQ_26016);
        }

        if (StringUtils.isNotBlank(letterOfCreditResponse)) {
            alert.prepareError("Transact cheque book Response " + letterOfCreditResponse).log();
            Response = new JSONObject(letterOfCreditResponse);
        }

        if (Response.has("requestId") && StringUtils.isNotBlank(Response.getString("requestId"))) {

        	letterOfCreditDto.setRequestId(Response.getString("requestId"));
        	letterOfCreditDto.setComments(Response.getString("comments"));
        } else {
            if (Response.has("dbpErrCode"))
            	letterOfCreditDto.setDbpErrCode(Response.getInt("dbpErrCode"));
            if (Response.has("dbpErrMsg"))
            	letterOfCreditDto.setDbpErrMsg(Response.getString("dbpErrMsg"));
        }

        return letterOfCreditDto;
	}

	@Override
	public LetterOfCreditsActionDTO approveLetterOfCredit(LetterOfCreditsActionDTO letterOfCredit,
			DataControllerRequest request) throws ApplicationException {
		  Map<String, Object> inputMap = new HashMap<>();
	        inputMap.put("serviceRequestId", letterOfCredit.getRequestId());
	        inputMap.put("signatoryApproved", "true");
	        inputMap.put("comments", letterOfCredit.getComments());

	        // Set Header Map
	        HashMap<String, Object> headerMap = new HashMap<String, Object>();
	        headerMap.put("X-Kony-Authorization", request.getHeader("X-Kony-Authorization"));
	        headerMap.put("X-Kony-ReportingParams", request.getHeader("X-Kony-ReportingParams"));

	        // Making a call to order request API
	        String letterOfCreditResponse = null;
	        JSONObject Response = new JSONObject();
	        try {
	        	letterOfCreditResponse = DBPServiceExecutorBuilder.builder()
	                    .withServiceId(TradeFinanceAPIServices.SERVICEREQUESTJAVA_APPROVE.getServiceName())
	                    .withOperationId(TradeFinanceAPIServices.SERVICEREQUESTJAVA_APPROVE.getOperationName())
	                    .withRequestParameters(inputMap).withRequestHeaders(headerMap).withDataControllerRequest(request)
	                    .build().getResponse();

	        } catch (Exception e) {
	            alert.prepareError("Unable to create cheque book request order " + e).log();
	          //  throw new ApplicationException(ErrorCodeEnum.ERRCHQ_26011);
	        }

	        if (StringUtils.isNotBlank(letterOfCreditResponse)) {
	            alert.prepareError("OMS Response " + letterOfCreditResponse).log();
	            Response = new JSONObject(letterOfCreditResponse);
	        }
	        LetterOfCreditsActionDTO letterOfCreditOrder = new LetterOfCreditsActionDTO();
	        //@todo output mapping
	        if (Response.has(TradeFinanceConstants.PARAM_ORDER_ID)
	                && StringUtils.isNotBlank(Response.getString(TradeFinanceConstants.PARAM_ORDER_ID))) {

	        }
	        return letterOfCreditOrder;

		}

	@Override
	public List<ApprovalRequestDTO> fetchLetterOfCreditDetails(DataControllerRequest request)
			throws ApplicationException {
		// TODO Auto-generated method stub
		List<ApprovalRequestDTO> letterOfCredit = new ArrayList<ApprovalRequestDTO>();
		String  transactionIds= request.getParameter("transactionIds");
        Map<String, Object> requestParameters = new HashMap<String, Object>();
        requestParameters.put("serviceRequestIds", transactionIds);
        
        try {
			String jResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(TradeFinanceAPIServices.SERVICEREQUESTJAVA_GETLETTEROFCREDITDETAILS.getServiceName()).
                    withOperationId(TradeFinanceAPIServices.SERVICEREQUESTJAVA_GETLETTEROFCREDITDETAILS.getOperationName()).
	        		withRequestParameters(requestParameters).
	        		withRequestHeaders(request.getHeaderMap()).
	        		withDataControllerRequest(request).
	        		build().getResponse();
			JSONObject jsonResponse = new JSONObject(jResponse);
			JSONArray Orders = TradeFinanceCommonUtils.getFirstOccuringArray(jsonResponse);
			
			
			for (int i = 0; i < Orders.length(); i++) {
				ApprovalRequestDTO order = new ApprovalRequestDTO();
	            JSONObject singleOrder = Orders.getJSONObject(i);
	            if (singleOrder.has(TradeFinanceConstants.PARAM_INPUT_PAYLOAD)) {
	                JSONObject payload = singleOrder.getJSONObject(TradeFinanceConstants.PARAM_INPUT_PAYLOAD);
	                        
					String accountId = payload.has(TradeFinanceConstants.PARAM_ACCOUNTID) ? payload.getString(TradeFinanceConstants.PARAM_ACCOUNTID) : "";
	        		if (StringUtils.isNotBlank(accountId)) {
						order.setAccountId(accountId);
					}
	        		
	        		String lcReferenceNo =payload.has(TradeFinanceConstants.PARAM_LC_REFERENCE_NO) ? payload.getString(TradeFinanceConstants.PARAM_LC_REFERENCE_NO) : "";
	        		if(StringUtils.isNotBlank(lcReferenceNo)) {
	        			order.setLcReferenceNo(lcReferenceNo);
	        		}
	        		Double lcAmount = payload.has(TradeFinanceConstants.PARAM_LC_AMOUNT) ? payload.getDouble(TradeFinanceConstants.PARAM_LC_AMOUNT) : 0.0;
	        			order.setLcAmount(lcAmount);
	        			
	        		String lcCurrency =payload.has(TradeFinanceConstants.PARAM_LC_CURRENCY) ? payload.getString(TradeFinanceConstants.PARAM_LC_CURRENCY) : "";
	        		if(StringUtils.isNotBlank(lcCurrency)) {
	        			order.setLcCurrency(lcCurrency);
	        		}
	        		
	        		String availableWith1 =payload.has(TradeFinanceConstants.PARAM_AVAILABLEWITH1) ? payload.getString(TradeFinanceConstants.PARAM_AVAILABLEWITH1) : "";
	        		if(StringUtils.isNotBlank(availableWith1)) {
	        			order.setAvailableWith1(availableWith1);
	        		}
	        		
	        		String availableWith2 =payload.has(TradeFinanceConstants.PARAM_AVAILABLEWITH2) ? payload.getString(TradeFinanceConstants.PARAM_AVAILABLEWITH2) : "";
	        		if(StringUtils.isNotBlank(availableWith2)) {
	        			order.setAvailableWith2(availableWith2);
	        		}	
	        		
	        		String availableWith3=payload.has(TradeFinanceConstants.PARAM_AVAILABLEWITH3) ? payload.getString(TradeFinanceConstants.PARAM_AVAILABLEWITH3) : "";
	        		if(StringUtils.isNotBlank(availableWith3)) {
	        			order.setAvailableWith3(availableWith3);
	        		}
	        		
	        		String availableWith4=payload.has(TradeFinanceConstants.PARAM_AVAILABLEWITH4) ? payload.getString(TradeFinanceConstants.PARAM_AVAILABLEWITH4) : "";
	        		if(StringUtils.isNotBlank(availableWith4)) {
	        			order.setAvailableWith4(availableWith4);
	        		}
	        		
	        		 String issueDate=payload.has(TradeFinanceConstants.PARAM_ISSUEDATE) ? payload.getString(TradeFinanceConstants.PARAM_ISSUEDATE) : "";
	        		if(StringUtils.isNotBlank(issueDate)) {
	        			order.setIssueDate(issueDate);
	        		}
	        		
	        		 String expiryDate=payload.has(TradeFinanceConstants.PARAM_EXPIRYDATE) ? payload.getString(TradeFinanceConstants.PARAM_EXPIRYDATE) : "";
	        		if(StringUtils.isNotBlank(expiryDate)) {
	        			order.setExpiryDate(expiryDate);
	        		}
	        		
	        		 String expiryPlace=payload.has(TradeFinanceConstants.PARAM_EXPIRYPLACE) ? payload.getString(TradeFinanceConstants.PARAM_EXPIRYPLACE) : "";
	        		if(StringUtils.isNotBlank(expiryPlace)) {
	        			order.setExpiryPlace(expiryPlace);
	        		}
	        		
	        		
	        		 String beneficiaryName = payload.has(TradeFinanceConstants.PARAM_BENEFICIARYNAME) ? payload.getString(TradeFinanceConstants.PARAM_BENEFICIARYNAME) : "";
	        		if(StringUtils.isNotBlank(beneficiaryName)) {
	        			order.setBeneficiaryName(beneficiaryName);
	        		}
	        		
	        		 String beneficiaryAddressLine1 = payload.has(TradeFinanceConstants.PARAM_BENEFICIARYADDRESSLINE1) ? payload.getString(TradeFinanceConstants.PARAM_BENEFICIARYADDRESSLINE1) : "";
	        		if(StringUtils.isNotBlank(beneficiaryAddressLine1)) {
	        			order.setBeneficiaryAddressLine1(beneficiaryAddressLine1);
	        		}
	        		
	        		 String beneficiaryAddressLine2 = payload.has(TradeFinanceConstants.PARAM_BENEFICIARYADDRESSLINE2) ? payload.getString(TradeFinanceConstants.PARAM_BENEFICIARYADDRESSLINE2) : "";
	        		if(StringUtils.isNotBlank(beneficiaryAddressLine2)) {
	        			order.setBeneficiaryAddressLine2(beneficiaryAddressLine2);
	        		}
	        		
	        		 String beneficiaryPostCode = payload.has(TradeFinanceConstants.PARAM_BENEFICIARYPOSTCODE) ? payload.getString(TradeFinanceConstants.PARAM_BENEFICIARYPOSTCODE) : "";
	        		if(StringUtils.isNotBlank(beneficiaryPostCode)) {
	        			order.setBeneficiaryPostCode(beneficiaryPostCode);
	        		}
	        		
	        		 String beneficiaryCountry = payload.has(TradeFinanceConstants.PARAM_BENEFICIARYCOUNTRY) ? payload.getString(TradeFinanceConstants.PARAM_BENEFICIARYCOUNTRY) : "";
	        		if(StringUtils.isNotBlank(beneficiaryCountry)) {
	        			order.setBeneficiaryCountry(beneficiaryCountry);
	        		}
	        		 String beneficiaryCity = payload.has(TradeFinanceConstants.PARAM_BENEFICIARYCITY) ? payload.getString(TradeFinanceConstants.PARAM_BENEFICIARYCITY) : "";
	        		if(StringUtils.isNotBlank(beneficiaryCity)) {
	        			order.setBeneficiaryCity(beneficiaryCity);
	        		}
	        		 String beneficiaryState = payload.has(TradeFinanceConstants.PARAM_BENEFICIARYSTATE) ? payload.getString(TradeFinanceConstants.PARAM_BENEFICIARYSTATE) : "";
	        		if(StringUtils.isNotBlank(beneficiaryState)) {
	        			order.setBeneficiaryState(beneficiaryState);
	        		}
	        		 String beneficiaryBank = payload.has(TradeFinanceConstants.PARAM_BENEFICIARYBANK) ? payload.getString(TradeFinanceConstants.PARAM_BENEFICIARYBANK) : "";
	        		if(StringUtils.isNotBlank(beneficiaryBank)) {
	        			order.setBeneficiaryBank(beneficiaryBank);
	        		}
	        		
					String beneficiaryBankAdressLine1 = payload.has(TradeFinanceConstants.PARAM_BENEFICIARYADDRESSLINE1) ? payload.getString(TradeFinanceConstants.PARAM_BENEFICIARYADDRESSLINE1) : "";
	        		if(StringUtils.isNotBlank(beneficiaryBankAdressLine1)) {
	        			order.setBeneficiaryAddressLine1(beneficiaryAddressLine1);
	        		}
					String beneficiaryBankAdressLine2 = payload.has(TradeFinanceConstants.PARAM_BENEFICIARYADDRESSLINE2) ? payload.getString(TradeFinanceConstants.PARAM_BENEFICIARYADDRESSLINE2) : "";
	        		if(StringUtils.isNotBlank(beneficiaryBankAdressLine2)) {
	        			order.setBeneficiaryAddressLine2(beneficiaryAddressLine2);
	        		}
	        		
	        		 String beneficiaryBankCity = payload.has(TradeFinanceConstants.PARAM_BENEFICIARY_BANK_CITY) ? payload.getString(TradeFinanceConstants.PARAM_BENEFICIARY_BANK_CITY) : "";
	        		if(StringUtils.isNotBlank(beneficiaryBankCity)) {
	        			order.setBeneficiaryBankCity(beneficiaryBankCity);
	        		}
	        		
	        		 String beneficiaryBankState = payload.has(TradeFinanceConstants.PARAM_BENEFICIARY_BANK_STATE) ? payload.getString(TradeFinanceConstants.PARAM_BENEFICIARY_BANK_STATE) : "";
	        		if(StringUtils.isNotBlank(beneficiaryBankState)) {
	        			order.setBeneficiaryBankState(beneficiaryBankState);
	        		}
	        		
	        		 String partialShipments = payload.has(TradeFinanceConstants.PARAM_PARTIAL_SHIPMENT) ? payload.getString(TradeFinanceConstants.PARAM_PARTIAL_SHIPMENT) : "";
	        		if(StringUtils.isNotBlank(partialShipments)) {
	        			order.setPartialShipments(partialShipments);
	        		}
	        		
	        		 String incoTerms = payload.has(TradeFinanceConstants.PARAM_INCO_TERMS) ? payload.getString(TradeFinanceConstants.PARAM_INCO_TERMS) : "";
	        		if(StringUtils.isNotBlank(incoTerms)) {
	        			order.setIncoTerms(incoTerms);
	        		}
	        		
	        		
	        		 String confirmationInstruction = payload.has(TradeFinanceConstants.PARAM_CONFIRMATION_INSTRUCTION) ? payload.getString(TradeFinanceConstants.PARAM_CONFIRMATION_INSTRUCTION) : "";
	        		if(StringUtils.isNotBlank(confirmationInstruction)) {
	        			order.setConfirmationInstruction(confirmationInstruction);
	        		}
	        		
	        		 String transferable = payload.has(TradeFinanceConstants.PARAM_TRANSFERABLE) ? payload.getString(TradeFinanceConstants.PARAM_TRANSFERABLE) : "";
	        		if(StringUtils.isNotBlank(transferable)) {
	        			order.setTransferable(transferable);
	        		}
	        		
	        		 String standByLC = payload.has(TradeFinanceConstants.PARAM_STAND_BY_LC) ? payload.getString(TradeFinanceConstants.PARAM_STAND_BY_LC) : "";
	        		if(StringUtils.isNotBlank(standByLC)) {
	        			order.setStandByLC(standByLC);
	        		}
	        		
	        		 String isDraft = payload.has(TradeFinanceConstants.PARAM_IS_DRAFT) ? payload.getString(TradeFinanceConstants.PARAM_IS_DRAFT) : "";
	        		if(StringUtils.isNotBlank(isDraft)) {
	        			order.setIsDraft(isDraft);
	        		}
	        		
	        		 String additionalPayableCurrency = payload.has(TradeFinanceConstants.PARAM_ADDITIONAL_PAYABLE_CURRENCY) ? payload.getString(TradeFinanceConstants.PARAM_ADDITIONAL_PAYABLE_CURRENCY) : "";
	        		if(StringUtils.isNotBlank(additionalPayableCurrency)) {
	        			order.setAdditionalPayableCurrency(additionalPayableCurrency);
	        		}
	        		
	        		
	        	     String flowType = payload.has(TradeFinanceConstants.PARAM_INCO_TERMS) ? payload.getString(TradeFinanceConstants.PARAM_INCO_TERMS) : "";
	        		if(StringUtils.isNotBlank(flowType)) {
	        			order.setFlowType(flowType);
	        		}
	        		
	        	    
	        	     
	        	     String srmsReqOrderID = payload.has(TradeFinanceConstants.PARAM_SRMS_ReqOrderID) ? payload.getString(TradeFinanceConstants.PARAM_SRMS_ReqOrderID) : "";
	        		if(StringUtils.isNotBlank(srmsReqOrderID)) {
	        			order.setSrmsReqOrderID(srmsReqOrderID);
	        			order.setTransactionId(srmsReqOrderID);
	        		}
	            }
	            String transactionId = singleOrder.has(TradeFinanceConstants.PARAM_TRANS_ID) ? singleOrder.getString(TradeFinanceConstants.PARAM_TRANS_ID) : "";
        		if (StringUtils.isNotBlank(transactionId)) {
					order.setTransactionId(transactionId);
				}
        		
        		String partyId = singleOrder.has(TradeFinanceConstants.PARAM_PARTY_ID) ? singleOrder.getString(TradeFinanceConstants.PARAM_PARTY_ID) : "";
        		if (StringUtils.isNotBlank(partyId)) {
					order.setSentBy(partyId);
				}
        		
        		String sentDate = singleOrder.has(TradeFinanceConstants.PARAM_ORDER_PROCESSED_TIME) ? singleOrder.getString(TradeFinanceConstants.PARAM_ORDER_PROCESSED_TIME) : "";
        		if (StringUtils.isNotBlank(sentDate)) {
					order.setSentDate(sentDate);
				}
        		
        		String type = singleOrder.has(TradeFinanceConstants.PARAM_TYPE) ? singleOrder.getString(TradeFinanceConstants.PARAM_TYPE) : "";
        		if (StringUtils.isNotBlank(type)) {
        			if("LetterOfCredits".equalsIgnoreCase(type)) {
        				order.setRequestType("New Request");
        			}else {
        				order.setRequestType(type);
        			}
				}
        		
        		letterOfCredit.add(order);
			}   
	        
		}
		catch(Exception exp) {
			alert.prepareError("Exception occurred while fetching the cheque details",exp).log();
			//throw new ApplicationException(ErrorCodeEnum.ERRCHQ_26020);
		}
        
        return letterOfCredit;
	
	}

}
