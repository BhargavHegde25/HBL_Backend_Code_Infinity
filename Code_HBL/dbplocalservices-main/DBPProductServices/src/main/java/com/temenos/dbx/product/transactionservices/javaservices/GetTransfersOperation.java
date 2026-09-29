package com.temenos.dbx.product.transactionservices.javaservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.dbx.product.constants.TransactionBackendServicesHelper;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class GetTransfersOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        Result transactionResult = new Result();
        HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
        @SuppressWarnings("unchecked")
        HashMap<String, Object> params = (HashMap<String, Object>) inputArray[1];
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        String serviceName = inputParams.get("serviceName");
        if(inputParams.get("paymentOrderId")!=null){
            params.put("paymentOrderId",inputParams.get("paymentOrderId"));
        }
        String Payment_Backend = EnvironmentConfigurationsHandler.getValue("PAYMENT_BACKEND");
        if(serviceName.equalsIgnoreCase("getTransfers")&&Payment_Backend.equalsIgnoreCase("STUB")) {
        	return constructGetTransferSTUBResponse(transactionResult);
        }
        transactionResult = TransactionBackendServicesHelper.fetchBackendResponse(serviceName, request, serviceHeaders,
                params);
        if (transactionResult == null) {
            alert.prepareError("Error occured while invoking GetTransfers: ").log();
            return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
        }
        return transactionResult;
    }
    
    private static Result constructGetTransferSTUBResponse(Result transactionResult) {
		Dataset ds = new Dataset();
		ds.setId("Transactions");
		Record record = new Record();
		record.addStringParam("chargeBearer", "SHA");
		record.addStringParam("amount", "11");
		record.addStringParam("systemId", "BNK23108KGFDGKMJ");
		record.addStringParam("fromAccountNumber", "105929");
		record.addStringParam("scheduledDate", "2022-12-08");
		record.addStringParam("transactionCurrency", "USD");
		record.addStringParam("fromAccountName", "John Kelly");
		record.addStringParam("currentStatus", "WareHouseOrder");
		record.addStringParam("endToEndReference", "NOTPROVIDED");
		record.addStringParam("fromAccountIBAN", "GB31DEMO60161300107662");
		record.addStringParam("transactionId", "12345");
		record.addStringParam("transactionType", "ExternalTransfer");
		record.addStringParam("paymentCurrencyId", "USD");
		record.addStringParam("toAccountName", "Brice Bamford");
		record.addStringParam("fromAccountCurrency", "USD");
		record.addStringParam("ExternalAccountNumber", "104558");
		record.addStringParam("orderingCustomerId", "100600");
		record.addStringParam("toAccountNumber", "104558");
		record.addStringParam("frequencyType", "Once");
		record.addStringParam("paymentStatus", "PNDG");
		record.addStringParam("isScheduled", "false");
		record.addStringParam("paidBy", "Both");
		record.addStringParam("statusDescription", "Scheduled");
		record.addStringParam("pendingApproval", "false");
		record.addStringParam("serviceName", "INTRA_BANK_FUND_TRANSFER_CREATE");
		ds.addRecord(record);

		record = new Record();
		record.addStringParam("chargeBearer", "SHA");
		record.addStringParam("amount", "50");
		record.addStringParam("systemId", "BNK23108MMHMDBCL");
		record.addStringParam("fromAccountNumber", "107557");
		record.addStringParam("scheduledDate", "2022-12-08");
		record.addStringParam("transactionCurrency", "USD");
		record.addStringParam("fromAccountName", "John Kelly");
		record.addStringParam("currentStatus", "WareHouseOrder");
		record.addStringParam("endToEndReference", "NOTPROVIDED");
		record.addStringParam("fromAccountIBAN", "GB53DEMO60161300107557");
		record.addStringParam("transactionId", "12345");
		record.addStringParam("transactionType", "InternalTransfer");
		record.addStringParam("paymentCurrencyId", "USD");
		record.addStringParam("toAccountName", "Brice Bamford");
		record.addStringParam("fromAccountCurrency", "USD");
		record.addStringParam("description", "Test");
		record.addStringParam("orderingCustomerId", "100600");
		record.addStringParam("toAccountNumber", "107662");
		record.addStringParam("frequencyType", "Once");
		record.addStringParam("isScheduled", "false");
		record.addStringParam("paidBy", "Both");
		record.addStringParam("statusDescription", "Scheduled");
		record.addStringParam("pendingApproval", "false");
		record.addStringParam("serviceName", "TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE");
		ds.addRecord(record);

		record = new Record();
		record.addStringParam("chargeBearer", "OUR");
		record.addStringParam("amount", "1");
		record.addStringParam("fromAccountNumber", "105627");
		record.addStringParam("scheduledDate", "2023-04-20");
		record.addStringParam("transactionCurrency", "EUR");
		record.addStringParam("fromAccountName", "John Kelly");
		record.addStringParam("currentStatus", "WareHouseOrder");
		record.addStringParam("endToEndReference", "NOTPROVIDED");
		record.addStringParam("fromAccountIBAN", "GB74DEMO60161300105627");
		record.addStringParam("transactionId", "12345");
		record.addStringParam("transactionType", "ExternalTransfer");
		record.addStringParam("paymentCurrencyId", "EUR");
		record.addStringParam("toAccountName", "International");
		record.addStringParam("fromAccountCurrency", "EUR");
		record.addStringParam("ExternalAccountNumber", "23456789");
		record.addStringParam("orderingCustomerId", "100600");
		record.addStringParam("toAccountNumber", "Brice Bamford");
		record.addStringParam("frequencyType", "Once");
		record.addStringParam("isScheduled", "false");
		record.addStringParam("paidBy", "Self");
		record.addStringParam("statusDescription", "Scheduled");
		record.addStringParam("pendingApproval", "false");
		record.addStringParam("serviceName", "INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE");
		ds.addRecord(record);
		
		record = new Record();
		record.addStringParam("chargeBearer", "OUR");
		record.addStringParam("amount", "5");
		record.addStringParam("systemId", "BNK23108FBMKBFFD");
		record.addStringParam("fromAccountNumber", "107662");
		record.addStringParam("scheduledDate", "2023-04-20");
		record.addStringParam("transactionCurrency", "EUR");
		record.addStringParam("fromAccountName", "John Kelly");
		record.addStringParam("currentStatus", "WareHouseOrder");
		record.addStringParam("endToEndReference", "NOTPROVIDED");
		record.addStringParam("fromAccountIBAN", "GB74DEMO60161300105627");
		record.addStringParam("transactionId", "12345");
		record.addStringParam("transactionType", "ExternalTransfer");
		record.addStringParam("paymentCurrencyId", "EUR");
		record.addStringParam("toAccountName", "Warner Buffet");
		record.addStringParam("fromAccountCurrency", "EUR");
		record.addStringParam("ExternalAccountNumber", "");
		record.addStringParam("orderingCustomerId", "100600");
		record.addStringParam("toAccountNumber", "DE89370400440532013000");
		record.addStringParam("frequencyType", "Once");
		record.addStringParam("isScheduled", "false");
		record.addStringParam("paymentType", "SEPA");
		record.addStringParam("paidBy", "Both");
		record.addStringParam("statusDescription", "Scheduled");
		record.addStringParam("pendingApproval", "false");
		record.addStringParam("serviceName", "INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE");
		ds.addRecord(record);
		
		record = new Record();
		record.addStringParam("chargeBearer", "OUR");
		record.addStringParam("amount", "1");
		record.addStringParam("fromAccountNumber", "105627");
		record.addStringParam("scheduledDate", "2023-04-20");
		record.addStringParam("transactionCurrency", "EUR");
		record.addStringParam("fromAccountName", "John Kelly");
		record.addStringParam("currentStatus", "Placed");
		record.addStringParam("endToEndReference", "NOTPROVIDED");
		record.addStringParam("fromAccountIBAN", "GB74DEMO60161300105627");
		record.addStringParam("transactionId", "12345");
		record.addStringParam("transactionType", "ExternalTransfer");
		record.addStringParam("paymentCurrencyId", "EUR");
		record.addStringParam("toAccountName", "International");
		record.addStringParam("fromAccountCurrency", "EUR");
		record.addStringParam("ExternalAccountNumber", "23456789");
		record.addStringParam("orderingCustomerId", "100600");
		record.addStringParam("toAccountNumber", "Warner Buffet");
		record.addStringParam("frequencyType", "Once");
		record.addStringParam("isScheduled", "false");
		record.addStringParam("paidBy", "Self");
		record.addStringParam("paymentStatus", "PNDG");
		record.addStringParam("statusDescription", "Pending");
		record.addStringParam("pendingApproval", "false");
		record.addStringParam("serviceName", "INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE");
		ds.addRecord(record);
		
		record = new Record();
		record.addStringParam("chargeBearer", "OUR");
		record.addStringParam("amount", "19");
		record.addStringParam("fromAccountNumber", "107662");
		record.addStringParam("scheduledDate", "2023-04-18");
		record.addStringParam("transactionCurrency", "EUR");
		record.addStringParam("fromAccountName", "John Kelly");
		record.addStringParam("currentStatus", "Complete");
		record.addStringParam("endToEndReference", "NOTPROVIDED");
		record.addStringParam("fromAccountIBAN", "GB74DEMO60161300105627");
		record.addStringParam("transactionId", "12345");
		record.addStringParam("transactionType", "ExternalTransfer");
		record.addStringParam("paymentCurrencyId", "EUR");
		record.addStringParam("toAccountName", "Warner Buffet");
		record.addStringParam("fromAccountCurrency", "EUR");
		record.addStringParam("ExternalAccountNumber", "23456789");
		record.addStringParam("orderingCustomerId", "100600");
		record.addStringParam("toAccountNumber", "23456789");
		record.addStringParam("frequencyType", "Once");
		record.addStringParam("isScheduled", "false");
		record.addStringParam("paidBy", "Self");
		record.addStringParam("paymentStatus", "ACSC");
		record.addStringParam("statusDescription", "Completed");
		record.addStringParam("pendingApproval", "false");
		record.addStringParam("serviceName", "INTRA_BANK_FUND_TRANSFER_CREATE");
		ds.addRecord(record);
		
		
		transactionResult.addDataset(ds);

		return transactionResult;
		
	}
}
