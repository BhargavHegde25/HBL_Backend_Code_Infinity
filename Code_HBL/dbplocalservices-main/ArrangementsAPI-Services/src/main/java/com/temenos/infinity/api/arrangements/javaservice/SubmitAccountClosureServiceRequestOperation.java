package com.temenos.infinity.api.arrangements.javaservice;

import java.util.HashMap;
import com.kony.dbputilities.util.Log4j2Configurator;


import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.constants.ErrorCodeEnum;
import com.temenos.infinity.api.arrangements.dto.AccountClosureDTO;
import com.temenos.infinity.api.arrangements.resource.api.AccountClosureResource;

public class SubmitAccountClosureServiceRequestOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		try {
			
			Result result = new Result();
			AccountClosureResource accountClosureResource = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(ResourceFactory.class).getResource(AccountClosureResource.class);
			
			AccountClosureDTO accountClosureDTO = constructPayLoad(request);

			// Set Header Map
			HashMap<String, Object> headerMap = new HashMap<String, Object>();
			headerMap.put("X-Kony-Authorization", request.getHeader("X-Kony-Authorization"));
			headerMap.put("X-Kony-ReportingParams", request.getHeader("X-Kony-ReportingParams"));

			 result = accountClosureResource.CloseAccount(accountClosureDTO, request,headerMap);
			return result;
		} catch (Exception e) {
			alert.prepareError("Unable to create order : " + e).log();
			return ErrorCodeEnum.ERR_20058.setErrorCode(new Result());
		}
	}
	
	public static AccountClosureDTO constructPayLoad(DataControllerRequest request) {
		AccountClosureDTO accountClosureDTO = new AccountClosureDTO();
		
		String accountName = request.getParameter("accountName") != null ? request.getParameter("accountName") : "";
		String accountNumber = request.getParameter("accountNumber") != null ? request.getParameter("accountNumber") : "";
		String accountType = request.getParameter("accountType") != null
				? request.getParameter("accountType")
				: "";
		String currentBalance = request.getParameter("currentBalance") != null
				? request.getParameter("currentBalance")
				: "";
		String closingReason = request.getParameter("closingReason") != null ? request.getParameter("closingReason") : "";
		String iBAN = request.getParameter("IBAN") != null ? request.getParameter("IBAN") : "";
		String sWIFTCode = request.getParameter("SWIFTCode") != null ? request.getParameter("SWIFTCode") : "";
		String supportingDocumentData = request.getParameter("supportingDocumentData") != null ? request.getParameter("supportingDocumentData") : "";
		String loginUserId = HelperMethods.getCustomerIdFromSession(request);

		accountClosureDTO.setAccountName(accountName);
		accountClosureDTO.setAccountNumber(accountNumber);
		accountClosureDTO.setAccountType(accountType);
		accountClosureDTO.setCurrentBalance(currentBalance);
		accountClosureDTO.setClosingReason(closingReason);
		accountClosureDTO.setIBAN(iBAN);
		accountClosureDTO.setSWIFTCode(sWIFTCode);
		accountClosureDTO.setSupportingDocumentData(supportingDocumentData);
		accountClosureDTO.setCustomerid(loginUserId);
		
		return accountClosureDTO;
	}
}
