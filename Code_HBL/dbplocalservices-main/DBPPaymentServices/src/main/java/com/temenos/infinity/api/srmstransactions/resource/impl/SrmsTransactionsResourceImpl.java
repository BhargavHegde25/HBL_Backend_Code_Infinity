package com.temenos.infinity.api.srmstransactions.resource.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.srmstransactions.businessdelegate.api.SrmsTransactionsBusinessDelegate;
import com.temenos.infinity.api.srmstransactions.constants.ErrorCodeEnum;
import com.temenos.infinity.api.srmstransactions.dto.SRMSTransactionDTO;
import com.temenos.infinity.api.srmstransactions.dto.SessionMap;
import com.temenos.infinity.api.srmstransactions.resource.api.SrmsTransactionsResource;
import com.temenos.infinity.api.srmstransactions.utils.MemoryManagerUtils;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class SrmsTransactionsResourceImpl implements SrmsTransactionsResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    final String INTERNAL_BANK_ACCOUNTS = "INTERNAL_BANK_ACCOUNTS";
    final String PARAM_CUSTOMER_ID = "customer_id";
    final String ONE_TIME_TRANSFER_TYPE = "OneTimeTransfer";

    @Override
    public Result getSrmsOneTimeTransactionsResource(String txnPeriod, String firstRecordNumber, String lastRecordNumber,
            DataControllerRequest request) throws ApplicationException {
        Result result = new Result();
        SRMSTransactionDTO inputDTO = new SRMSTransactionDTO();
        List<SRMSTransactionDTO> oneTimeTransactions = null;

        // Get Customer Id
        String customerId = "";
        try {
            customerId = (String) request.getServicesManager().getIdentityHandler().getUserAttributes()
                    .get(PARAM_CUSTOMER_ID);
        } catch (Exception e) {
            alert.prepareError("Unable to fetch the customer id from session" + e).log();
        }

        // Set Account Numbers from Cache
        String internalAccounts = getSRMSAccounts(customerId);
        alert.prepareError("SRMS Account Ids" + internalAccounts).log();
        if (StringUtils.isNotBlank(internalAccounts)) {
            inputDTO.setAccountId(internalAccounts);
        } else {
            return ErrorCodeEnum.ERR_32045.setErrorCode(new Result());
        }

        // Set Type
        inputDTO.setType(ONE_TIME_TRANSFER_TYPE);

        // Set Transaction Period with system date
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        if (StringUtils.isNotBlank(txnPeriod)) {
            calender.add(Calendar.MONTH, -Integer.parseInt(txnPeriod));
            calender.add(Calendar.DATE, -1);
        } else {
            calender.add(Calendar.MONTH, -6);
            calender.add(Calendar.DATE, -1);
        }
        Date fromDate = calender.getTime();
        String dateFrom = formatter.format(fromDate);
        String dateTo = formatter.format(date);
        
        inputDTO.setDateFrom(dateFrom);
        inputDTO.setDateTo(dateTo);
        //Set Page Number
        inputDTO.setLastRecordNumber(lastRecordNumber);
        
        //Calling Business Delegate
        try {

            SrmsTransactionsBusinessDelegate srmsBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(SrmsTransactionsBusinessDelegate.class);
            oneTimeTransactions = srmsBusinessDelegate.getSRMSOneTimeTransfers(inputDTO, request);
            JSONObject responseObj = new JSONObject();
            responseObj.put("Transactions", oneTimeTransactions);
            result = JSONToResult.convert(responseObj.toString());

        } catch (Exception e) {
            alert.prepareError("Failed to fetch srms Transactions - Resource " + e).log();
            return ErrorCodeEnum.ERR_32044.setErrorCode(new Result());
        }
        return result;
    }

    public SessionMap getInternalBankAccountsFromSession(String customerId) {
        SessionMap internalAccntsMap = (SessionMap) MemoryManagerUtils.retrieve(INTERNAL_BANK_ACCOUNTS + customerId);
        return internalAccntsMap;
    }
    
    public String getSRMSAccounts(String customerId) {
        SessionMap internalAccntsMap = getInternalBankAccountsFromSession(customerId);
        if (StringUtils.isNotBlank(internalAccntsMap.toString())) {
            JSONObject accounts = new JSONObject(internalAccntsMap.toString());
            Set<String> accIds = accounts.keySet();
            StringBuilder sb = new StringBuilder();
            for (Object s : accIds) {
                if (sb.length() == 0)
                    sb.append(s.toString());
                else
                    sb.append("," + s.toString());
            }
            return sb.toString();
        }
        return "";
    }

    @Override
    public Result getSrmsOneTimeTransactionByIdResource(String transactionId, DataControllerRequest request)
            throws ApplicationException {

        SRMSTransactionDTO inputDTO = new SRMSTransactionDTO();
        List<SRMSTransactionDTO> oneTimeTransactions = null;
        Result result = new Result();
        inputDTO.setTransactionId(transactionId);
      //Calling Business Delegate
        try {

            SrmsTransactionsBusinessDelegate srmsBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(SrmsTransactionsBusinessDelegate.class);
            oneTimeTransactions = srmsBusinessDelegate.getSRMSOneTimeTransferById(inputDTO, request);
            JSONObject responseObj = new JSONObject();
            responseObj.put("Transactions", oneTimeTransactions);
            result = JSONToResult.convert(responseObj.toString());

        } catch (Exception e) {
            alert.prepareError("Failed to fetch srms Transactions - Resource " + e).log();
            return ErrorCodeEnum.ERR_32044.setErrorCode(new Result());
        }
        return result;
    }

}