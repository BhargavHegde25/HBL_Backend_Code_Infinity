package com.temenos.infinity.api.docmanagement.resource.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonObject;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.ErrorConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.MWConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.bank.businessdelegate.api.BankBusinessDelegate;
import com.temenos.dbx.filesgenerator.businessdelegate.api.TransactionReportPDFGeneratorBD;
import com.temenos.dbx.product.utils.DTOUtils;
import com.temenos.dbx.product.utils.InfinityConstants;
import com.temenos.infinity.api.docmanagement.businessdelegate.api.TransactionReportBusinessDelegate;
import com.temenos.dbx.transaction.dto.BankDTO;
import com.temenos.dbx.transaction.dto.TransactionDTO;
import com.temenos.infinity.api.docmanagement.resource.api.TransactionReportDownloadResource;

public class TransactionReportDownloadResourceImpl implements TransactionReportDownloadResource {

    public static final String TRANSACTION_ID = "transactionId";
    public static final String FILE_ID = "fileId";

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Result generateTransactionReport(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) {
        final int SIZE_OF_RANDOM_GENERATED_STRING = 10;
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        try {

            String transactionId =
                    StringUtils.isNotBlank(inputParams.get(TRANSACTION_ID)) ? inputParams.get(TRANSACTION_ID)
                            : request.getParameter(TRANSACTION_ID);

            String authToken = request.getHeaderMap().get(InfinityConstants.x_kony_authorization)!=null? request.getHeaderMap().get(InfinityConstants.x_kony_authorization).toString(): (String) request.getHeaderMap().get(DBPUtilitiesConstants.X_KONY_AUTHORIZATION);

            if (StringUtils.isBlank(transactionId) || StringUtils.isBlank(authToken)) {
                return ErrorCodeEnum.ERR_13525.setErrorCode(new Result(), ErrorConstants.PROVIDE_MANDATORY_FIELDS);
            }

            TransactionReportBusinessDelegate transReportBusinessdelegate = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class)
                    .getBusinessDelegate(TransactionReportBusinessDelegate.class);

            TransactionDTO transaction = transReportBusinessdelegate
                    .getBillTransactionById(transactionId, authToken, request);

            String fileId = HelperMethods.getUniqueNumericString(SIZE_OF_RANDOM_GENERATED_STRING);
            JsonObject transactionJson = DTOUtils.getJsonObjectFromObject(transaction);
            MemoryManager.saveIntoCache(fileId, transactionJson.toString());
            result.addParam(FILE_ID, fileId, DBPUtilitiesConstants.STRING_TYPE);
        } catch (Exception e) {
            alert.prepareError("Error while generating transaction report", e).log();
            ErrorCodeEnum.ERR_13525.setErrorCode(result);
        }
        return result;
    }

    public Map<String, String> getResponseHeaders() {
        Map<String, String> customHeaders = new HashMap<>();
        customHeaders.put(HttpHeaders.CONTENT_TYPE, "application/pdf");
        customHeaders.put("Content-Disposition", "attachment; filename=\"Transaction Report.pdf\"");
        return customHeaders;
    }

    @Override
    public Result downloadTransactionReport(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) {
        byte[] bytes = null;
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        String fileId = StringUtils.isNotBlank(inputParams.get(FILE_ID)) ? inputParams.get(FILE_ID)
                : request.getParameter(FILE_ID);

        if (StringUtils.isBlank(fileId)) {
            return ErrorCodeEnum.ERR_13525.setErrorCode(new Result(), ErrorConstants.PROVIDE_MANDATORY_FIELDS);
        }
        try {

            String reportString = (String) MemoryManager.getFromCache(fileId);
            TransactionDTO transaction = JSONUtils.parse(reportString, TransactionDTO.class);
            BankBusinessDelegate bankDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(BankBusinessDelegate.class);
            BankDTO bank = bankDelegate.getBank();
            if (transaction != null && bank != null) {
                TransactionReportPDFGeneratorBD reportGeneratorDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                        .getFactoryInstance(BusinessDelegateFactory.class)
                        .getBusinessDelegate(TransactionReportPDFGeneratorBD.class);
                bytes = reportGeneratorDelegate.generateFileAsByte(transaction, bank);
                response.getHeaders().putAll(getResponseHeaders());
                response.setAttribute(MWConstants.CHUNKED_RESULTS_IN_JSON,
                        new BufferedHttpEntity(new ByteArrayEntity(bytes)));
                response.setStatusCode(HttpStatus.SC_OK);
                return new Result();
            }
        } catch (IOException e) {
            alert.prepareError("Error while generating transaction report", e).log();

        }
        return ErrorCodeEnum.ERR_13525.setErrorCode(new Result());
    }

}
