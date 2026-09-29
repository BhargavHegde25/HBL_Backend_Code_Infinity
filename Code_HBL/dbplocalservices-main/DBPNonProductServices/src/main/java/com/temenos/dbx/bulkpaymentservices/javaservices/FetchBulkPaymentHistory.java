package com.temenos.dbx.bulkpaymentservices.javaservices;

import java.util.List;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.temenos.dbx.product.commons.businessdelegate.api.ContractBusinessDelegate;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.bulkpaymentservices.utilities.BulkPaymentRecordDBOperations;

public class FetchBulkPaymentHistory implements JavaService2 {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request, DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		Result result;	
		
		try {
			BulkPaymentRecordDBOperations bulkPaymentRecordDBOperations = new BulkPaymentRecordDBOperations();
			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			String createdBy = CustomerSession.getCustomerId(customer);
			
			ContractBusinessDelegate contractDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);
			List<String> contracts = contractDelegate.fetchContractCustomers(createdBy);
			contracts.add(CustomerSession.getCompanyId(customer));
			
			result = bulkPaymentRecordDBOperations.fetchBulkPaymentHistory(contracts,createdBy);
		}
		catch(Exception e) {
			alert.prepareError("Error occured while invoking fetchUploadedFiles", e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		
		return result;
	}

}
