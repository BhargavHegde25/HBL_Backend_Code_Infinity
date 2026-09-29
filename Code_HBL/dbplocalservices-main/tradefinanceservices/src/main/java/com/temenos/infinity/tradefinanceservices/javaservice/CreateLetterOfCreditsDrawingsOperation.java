/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.javaservice;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.temenos.infinity.tradefinanceservices.dto.DrawingsDTO;
import com.temenos.infinity.tradefinanceservices.resource.api.LetterOfCreditDrawingsResource;
/**
 * 
 *
 * @version Java Service to create the letter of credit Drawings request in order management micro services
 * 
 */
public class CreateLetterOfCreditsDrawingsOperation implements JavaService2{
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		LetterOfCreditDrawingsResource drawingsCreateOrder = DBPAPIAbstractFactoryImpl
                .getResource(LetterOfCreditDrawingsResource.class);
        DrawingsDTO drawingsOrder = constructPayload(request);    
        Result result = null;
        try {
        result= drawingsCreateOrder.createLetterOfCreditDrawings(drawingsOrder, request);
        }catch(Exception e) {
        	alert.prepareError("Error" + e).log();
        	return ErrorCodeEnum.ERRTF_29045.setErrorCode(new Result(),"Failed to Create Drawing for the Letter of Credit");
        }
        return result;
	}
	private DrawingsDTO constructPayload(DataControllerRequest request) {
		// TODO Auto-generated method stub
		 DrawingsDTO drawings = new DrawingsDTO();

		 String lcReferenceNo = request.getParameter("lcReferenceNo") != null ? request.getParameter("lcReferenceNo") : "";
		 String lcType = request.getParameter("lcType") != null ? request.getParameter("lcType") : "";
		 String drawingReferenceNo = request.getParameter("drawingReferenceNo") != null ? request.getParameter("drawingReferenceNo") : "";
		 String beneficiaryName = request.getParameter("beneficiaryName") != null ? request.getParameter("beneficiaryName") : "";
		 String documentStatus = request.getParameter("documentStatus") != null ? request.getParameter("documentStatus") : "";
		 String drawingCurrency = request.getParameter("drawingCurrency") != null ? request.getParameter("drawingCurrency") : "";
		 String drawingAmount = request.getParameter("drawingAmount") != null ? request.getParameter("drawingAmount") : "";
//		 drawings.setLcReferenceNo(lcReferenceNo);
//		 drawings.setLcType(lcType);
		 drawings.setDrawingReferenceNo(drawingReferenceNo);
		 drawings.setBeneficiaryName(beneficiaryName);
		 drawings.setDocumentStatus(documentStatus);
		 drawings.setDrawingCurrency(drawingCurrency);
		 drawings.setDrawingAmount(drawingAmount);
		 
//		 String lcAmount = request.getParameter("lcAmount") != null ? request.getParameter("lcAmount") : "";
//		 String lcCurrency = request.getParameter("lcCurrency") != null ? request.getParameter("lcCurrency") : "";
//		 String lcIssueDate = request.getParameter("lcIssueDate") != null ? request.getParameter("lcIssueDate") : "";
//		 String lcExpiryDate = request.getParameter("lcExpiryDate") != null ? request.getParameter("lcExpiryDate") : "";
//		 String lcPaymentTerms = request.getParameter("lcPaymentTerms") != null ? request.getParameter("lcPaymentTerms") : "";

//		 drawings.setLcAmount(lcAmount);
//		 drawings.setLcCurrency(lcCurrency);
//		 drawings.setLcIssueDate(lcIssueDate);
//		 drawings.setLcExpiryDate(lcExpiryDate);
//		 drawings.setLcPaymentTerms(lcPaymentTerms);
		 
		 String presentorReference = request.getParameter("presentorReference") != null ? request.getParameter("presentorReference") : "";
		 String presentorName = request.getParameter("presentorName") != null ? request.getParameter("presentorName") : "";
		 String documentsReceived = request.getParameter("documentsReceived") != null ? request.getParameter("documentsReceived") : "";
		 String forwardContact = request.getParameter("forwardContact") != null ? request.getParameter("forwardContact") : "";
		 String shippingGuaranteeReference = request.getParameter("shippingGuaranteeReference") != null ? request.getParameter("shippingGuaranteeReference") : "";
		 
		 drawings.setPresentorReference(presentorReference);
		 drawings.setPresentorName(presentorName);
		 drawings.setDocumentsReceived(documentsReceived);
		 drawings.setForwardContact(forwardContact);
		 drawings.setShippingGuaranteeReference(shippingGuaranteeReference);
		 
		 String totalDocuments = request.getParameter("totalDocuments") != null ? request.getParameter("totalDocuments") : "";
		 String documentName = request.getParameter("documentName") != null ? request.getParameter("documentName") : "";
		 String discrepancies = request.getParameter("discrepancies") != null ? request.getParameter("discrepancies") : "";
	 
		 drawings.setTotalDocuments(totalDocuments);
		 drawings.setDocumentName(documentName);
		 drawings.setDiscrepancies(discrepancies);
		 
		 String totalAmountToBePaid = request.getParameter("totalAmountToBePaid") != null ? request.getParameter("totalAmountToBePaid") : "";
		 String messageFromBank = request.getParameter("messageFromBank") != null ? request.getParameter("messageFromBank") : "";
		 String totalPaidAmount = request.getParameter("totalPaidAmount") != null ? request.getParameter("totalPaidAmount") : "";
		 
		 drawings.setTotalAmountToBePaid(totalAmountToBePaid);
		 drawings.setMessageFromBank(messageFromBank);
		 drawings.setTotalPaidAmount(totalPaidAmount);
		 

		 String flowType = request.getParameter("flowType") != null ? request.getParameter("flowType") : "";
		 String status = request.getParameter("status") != null ? request.getParameter("status") : "";
		 String lcSrmsRequestID = request.getParameter("lcSrmsRequestID") != null ? request.getParameter("lcSrmsRequestID") : "";
		 
		 drawings.setStatus(status);
		 drawings.setFlowType(flowType);
		 drawings.setLcSrmsReqOrderID(lcSrmsRequestID);
		 
		 return drawings;
	}
	
}
