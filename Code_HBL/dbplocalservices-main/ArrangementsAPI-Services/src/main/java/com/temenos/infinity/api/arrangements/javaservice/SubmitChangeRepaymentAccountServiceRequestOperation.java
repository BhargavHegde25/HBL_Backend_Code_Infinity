package com.temenos.infinity.api.arrangements.javaservice;

import java.util.HashMap;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.objects.Account;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.constants.ErrorCodeEnum;
import com.temenos.infinity.api.arrangements.dto.MortgageRepaymentDTO;
import com.temenos.infinity.api.arrangements.resource.api.SubmitChangeRepaymentResource;

public class SubmitChangeRepaymentAccountServiceRequestOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		try {
			
			Result result = new Result();
			SubmitChangeRepaymentResource submitChangeRepaymentResource = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(ResourceFactory.class).getResource(SubmitChangeRepaymentResource.class);
			String accountId = request.getParameter("accountID") != null ? request.getParameter("accountID") : "";
			
			TemenosUtils temenosUtils = TemenosUtils.getInstance();
			HashMap<String, Account> accounts = temenosUtils.getAccountsMapFromCache(request);


			if (accounts != null && StringUtils.isNotEmpty(accountId)) {
				Account account = accounts.get(accountId);
				
				//If account not found in cache
				if (account == null) {
					result.addStringParam("errmsg", "Invalid Account Number");
					result.addOpstatusParam(0);
					result.addHttpStatusCodeParam(200);
					return result;
				}
			}
			MortgageRepaymentDTO changeRepaymentAccount = constructPayLoad(request);

			// Set Header Map
			HashMap<String, Object> headerMap = new HashMap<String, Object>();
			headerMap.put("X-Kony-Authorization", request.getHeader("X-Kony-Authorization"));
			headerMap.put("X-Kony-ReportingParams", request.getHeader("X-Kony-ReportingParams"));

			 result = submitChangeRepaymentResource.SubmitChangeRepaymentAccount(changeRepaymentAccount, request,headerMap);
			return result;
		} catch (Exception e) {
			alert.prepareError("Unable to create order : " + e).log();
			return ErrorCodeEnum.ERR_20058.setErrorCode(new Result());
		}
	}
	
	public static MortgageRepaymentDTO constructPayLoad(DataControllerRequest request) {
		MortgageRepaymentDTO changeRepaymentAccount = new MortgageRepaymentDTO();
		
		String facilityName = request.getParameter("facilityName") != null ? request.getParameter("facilityName") : "";
		String arrangementId = request.getParameter("arrangementId") != null ? request.getParameter("arrangementId") : "";
		String loanName = request.getParameter("loanName") != null
				? request.getParameter("loanName")
				: "";
		String loanAccountNumber = request.getParameter("loanAccountNumber") != null
				? request.getParameter("loanAccountNumber")
				: "";
		String supportingDocumentIds = request.getParameter("supportingDocumentIds") != null ? request.getParameter("supportingDocumentIds") : "";
		String requestDetails = request.getParameter("requestDetails") != null ? request.getParameter("requestDetails") : "";
		String accountId = request.getParameter("accountID") != null ? request.getParameter("accountID") : "";
		String customerName = request.getParameter("customerName") != null ? request.getParameter("customerName") : "";
		String customerId = request.getParameter("customerId") != null ? request.getParameter("customerId") : "";

		changeRepaymentAccount.setFacilityName(facilityName);
		changeRepaymentAccount.setArrangementId(arrangementId);
		changeRepaymentAccount.setCustomerName(customerName);
		changeRepaymentAccount.setLoanName(loanName);
		changeRepaymentAccount.setLoanAccountNumber(loanAccountNumber);
		changeRepaymentAccount.setSupportingDocumentIds(supportingDocumentIds);
		changeRepaymentAccount.setRequestDetails(requestDetails);
		changeRepaymentAccount.setAccountId(accountId);
		changeRepaymentAccount.setCustomerId(customerId);
		return changeRepaymentAccount;
	}
}
