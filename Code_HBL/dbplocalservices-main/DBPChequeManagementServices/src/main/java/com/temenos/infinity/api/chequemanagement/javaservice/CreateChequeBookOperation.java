package com.temenos.infinity.api.chequemanagement.javaservice;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.chequemanagement.dto.ChequeBook;
import com.temenos.infinity.api.chequemanagement.resource.api.CreateChequeBookResource;

/**
 * 
 * @author smugesh
 * @version Java Service to create the cheque book request in order management micro services
 * 
 */
public class CreateChequeBookOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		try {
		    CreateChequeBookResource chequeBookResource = DBPAPIAbstractFactoryImpl
					.getResource(CreateChequeBookResource.class);
			ChequeBook chequeBook = constructPayload(request);

			Result result = chequeBookResource.createChequeBook(chequeBook, request);
			return result;
		} catch (Exception e) { 
			alert.prepareError("Unable to create order : "+e).log();
			return ErrorCodeEnum.ERR_26021.setErrorCode(new Result()); 
		}

	}
	
	public static ChequeBook constructPayload(DataControllerRequest request){
	    ChequeBook chequeBook = new ChequeBook();
	    //Get input params from request object
	    String accountId = request.getParameter("accountID") != null ? request.getParameter("accountID") : "";
	    String validate = request.getParameter("validate") != null ? request.getParameter("validate") : "";
	    String note = request.getParameter("note") != null ? request.getParameter("note") : "";
	    String chequeIssueId = request.getParameter("chequeIssueId") != null ? request.getParameter("chequeIssueId") : "";
	    
	    String numberOfLeaves = request.getParameter("numberOfLeaves") != null ? request.getParameter("numberOfLeaves") : "";
	    String numberOfChequeBooks = request.getParameter("numberOfChequeBooks") != null ? request.getParameter("numberOfChequeBooks") : "";
	    String deliveryType = request.getParameter("deliveryType") != null ? request.getParameter("deliveryType") : "";
	    String address = request.getParameter("address") != null ? request.getParameter("address") : "";
	    String fees = request.getParameter("fees") != null ? request.getParameter("fees") : "";
	    String signatoryApprovalRequired = request.getParameter("signatoryApprovalRequired") != null ? request.getParameter("signatoryApprovalRequired") : "";
	    
	    chequeBook.setAccountID(accountId);
	    chequeBook.setValidate(validate);
	    chequeBook.setChequeStatus("70");
	    chequeBook.setNote(note);
	    chequeBook.setChequeIssueId(chequeIssueId);
	    chequeBook.setNumberOfLeaves(numberOfLeaves);
	    chequeBook.setNumberOfChequeBooks(numberOfChequeBooks);
	    chequeBook.setDeliveryType(deliveryType);
	    chequeBook.setAddress(address);
	    chequeBook.setFees(fees);
	    chequeBook.setSignatoryApprovalRequired(signatoryApprovalRequired);
	    
	    return chequeBook;
	}

}
