package com.temenos.infinity.api.arrangements.javaservice;

import java.util.HashMap;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.objects.Account;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.constants.ErrorCodeEnum;
import com.temenos.infinity.api.arrangements.dto.PartialRepaymentDTO;
import com.temenos.infinity.api.arrangements.resource.api.SubmitPartialRepaymentResource;
import com.temenos.infinity.api.arrangements.utils.CommonUtils;

public class SubmitPartialRepaymentServiceRequestOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		try {
			Result result = new Result();
			SubmitPartialRepaymentResource submitPartialRepaymentResource = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(ResourceFactory.class).getResource(SubmitPartialRepaymentResource.class);
			TemenosUtils temenosUtils = TemenosUtils.getInstance();
			HashMap<String, Account> accounts = temenosUtils.getAccountsMapFromCache(request);

			String accountId = request.getParameter("accountID") != null ? request.getParameter("accountID") : "";

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
			
			PartialRepaymentDTO partialRepayment = constructPayLoad(request);
			
			String paymentDetailsStr = partialRepayment.getPaymentDetails();
			JSONArray paymentDetails = new JSONArray(paymentDetailsStr);
			if(paymentDetails!=null && paymentDetails.length()>0) {
				JSONObject paymentDetailObj= paymentDetails.getJSONObject(0);
				String acccountNumber = paymentDetailObj.getString("accountNumber");
				String loanNumber = paymentDetailObj.getString("loanAccount");
				if(accounts==null || accounts.get(acccountNumber)==null || accounts.get(loanNumber)==null) {
					result.addStringParam("errmsg", "Invalid Account Number");
					result.addOpstatusParam(0);
					result.addHttpStatusCodeParam(200);
					return result;
				}
			}
			
			
			String companyid = CommonUtils.getCompanyId(request);
			// Set Header Map
			HashMap<String, Object> headerMap = new HashMap<String, Object>();
			headerMap.put("X-Kony-Authorization", request.getHeader("X-Kony-Authorization"));
			headerMap.put("X-Kony-ReportingParams", request.getHeader("X-Kony-ReportingParams"));
			headerMap.put("companyid", companyid);
			 result = submitPartialRepaymentResource.SubmitPartialRepayment(partialRepayment, request,headerMap);
			return result;
		} catch (Exception e) {
			alert.prepareError("Unable to create order : " + e).log();
			return ErrorCodeEnum.ERR_20058.setErrorCode(new Result());
		}
	}
	
	public static PartialRepaymentDTO constructPayLoad(DataControllerRequest request) {
		PartialRepaymentDTO partialRepayment = new PartialRepaymentDTO();
		String facilityName = request.getParameter("facilityName") != null ? request.getParameter("facilityName") : "";
		String arrangementId = request.getParameter("arrangementId") != null ? request.getParameter("arrangementId") : "";
		String customerName = request.getParameter("customerName") != null
				? request.getParameter("customerName")
				: "";
		String numOfLoans = request.getParameter("numOfLoans") != null
				? request.getParameter("numOfLoans")
				: "";
		String currentOutstandingBalanceCurrency = request.getParameter("currentOutstandingBalanceCurrency") != null ? request.getParameter("currentOutstandingBalanceCurrency") : "";
		String currentOutstandingBalanceAmount = request.getParameter("currentOutstandingBalanceAmount") != null ? request.getParameter("currentOutstandingBalanceAmount") : "";
		String amountPaidToDate = request.getParameter("amountPaidToDate") != null ? request.getParameter("amountPaidToDate") : "";
		String supportingDocumentIds = request.getParameter("supportingDocumentIds") != null ? request.getParameter("supportingDocumentIds") : "";
		String requestDetails = request.getParameter("requestDetails") != null ? request.getParameter("requestDetails") : "";
		String paymentDetails = request.getParameter("paymentDetails") != null ? request.getParameter("paymentDetails") : "";
		String accountId = request.getParameter("accountID") != null ? request.getParameter("accountID") : "";
		String customerId = request.getParameter("customerId") != null ? request.getParameter("customerId") : "";

		partialRepayment.setFacilityName(facilityName);
		partialRepayment.setArrangementId(arrangementId);
		partialRepayment.setCustomerName(customerName);
		partialRepayment.setNumOfLoans(numOfLoans);
		partialRepayment.setCurrentOutstandingBalanceCurrency(currentOutstandingBalanceCurrency);
		partialRepayment.setCurrentOutstandingBalanceAmount(currentOutstandingBalanceAmount);
		partialRepayment.setAmountPaidToDate(amountPaidToDate);
		partialRepayment.setSupportingDocumentIds(supportingDocumentIds);
		partialRepayment.setRequestDetails(requestDetails);
		partialRepayment.setPaymentDetails(paymentDetails);
		partialRepayment.setAccountId(accountId);
		partialRepayment.setCustomerId(customerId);
		return partialRepayment;
	}
}
