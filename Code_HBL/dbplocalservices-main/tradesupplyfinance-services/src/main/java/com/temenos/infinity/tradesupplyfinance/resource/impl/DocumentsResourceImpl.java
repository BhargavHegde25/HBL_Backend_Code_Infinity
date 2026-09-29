/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.resource.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.infinity.tradesupplyfinance.businessdelegate.api.DocumentsBusinessDelegate;
import com.temenos.infinity.tradesupplyfinance.constants.ErrorCodeEnum;
import com.temenos.infinity.tradesupplyfinance.resource.api.DocumentsResource;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.regex.Pattern;

import static com.temenos.infinity.tradesupplyfinance.constants.TradeSupplyFinanceConstants.PREFIX_TRADESUPPLYFINANCE_DOCUMENT;
import static com.temenos.infinity.tradesupplyfinance.utils.TradeSupplyFinanceCommonUtils.fetchCustomerIdFromSession;

/**
 * @author k.meiyazhagan
 */
public class DocumentsResourceImpl implements DocumentsResource {

    private static final int INDEX_FILENAME = 0;
    private static final int INDEX_FILETYPE = 1;
    private static final int INDEX_PRODUCT = 1;
    private static final int INDEX_FILECONTENTS = 2;
    private static final int MAX_ALLOWED_FILE_SIZE = 25;
    private static final Pattern validFileContent = Pattern.compile("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$");

    protected static final ArrayList<String> validFileExtensions = new ArrayList<>(Arrays.asList("application/pdf", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/vnd.ms-excel",
            "text/csv", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/x-zip-compressed", "image/bmp", "image/jpeg", "image/png", "image/tiff"));
    private static final DocumentsBusinessDelegate orderBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(DocumentsBusinessDelegate.class);
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Result uploadDocument(DataControllerRequest request) {
        Result result = new Result();

        String referenceId = PREFIX_TRADESUPPLYFINANCE_DOCUMENT + CommonUtils.generateUniqueID(32);
        ArrayList<String> failedUploads = new ArrayList<>();
        ArrayList<String> successfulUploads = new ArrayList<>();

        String customerId = fetchCustomerIdFromSession(request);
        if (StringUtils.isBlank(customerId))
            return ErrorCodeEnum.ERR_30008.setErrorCode(result);

        String inputDocumentsField = request.getParameter("uploadedattachments");
        if (StringUtils.isBlank(inputDocumentsField)) {
            diagnostic.prepareDebug("Mandatory fields are missing").log();
            return ErrorCodeEnum.ERR_30004.setErrorCode(result);
        }

        String[] documentsToUpload = inputDocumentsField.split(",");
        if (ArrayUtils.isEmpty(documentsToUpload)) {
            alert.prepareError("Invalid Input Payload").log();
            return ErrorCodeEnum.ERR_30001.setErrorCode(result);
        }

        try {
            for (String document : documentsToUpload) {
                String[] documentDetails = document.split("~");
                if (documentDetails.length != 3) {
                    alert.prepareError("File input is incorrect for performing upload operation").log();
                    failedUploads.add(documentDetails[INDEX_FILENAME]);
                    continue;
                }

                String uploadedFileName = documentDetails[INDEX_FILENAME];
                String fileExtension = documentDetails[INDEX_FILETYPE];
                String fileContents = documentDetails[INDEX_FILECONTENTS];
                if (!isFileSizeValid(fileContents) || Boolean.FALSE.equals(validateFileExtension(fileExtension, uploadedFileName))) {
                    failedUploads.add(uploadedFileName);
                    continue;
                }
                result = orderBusinessDelegate.uploadDocumentDBXDB(referenceId, customerId, uploadedFileName,
                        fileExtension, fileContents, request);
                if (result.hasParamByName("status") && Boolean.parseBoolean(result.getParamValueByName("status"))) {
                    successfulUploads.add(uploadedFileName);
                } else {
                    failedUploads.add(uploadedFileName);
                }
            }

        } catch (Exception e) {
            diagnostic.prepareDebug("Failed to upload document. Error: " + e).log();
            return ErrorCodeEnum.ERR_30011.setErrorCode(result);
        } finally {
            result.addParam("failedUploads", StringUtils.join(failedUploads, ","));
            result.addParam("successfulUploads", StringUtils.join(successfulUploads, ","));
            if (!successfulUploads.isEmpty())
                result.addParam("documentReference", referenceId);
        }
        return result;
    }

    @Override
    public Result fetchDocument(DataControllerRequest request) {
        Result result = new Result();
        String customerId = fetchCustomerIdFromSession(request);
        if (StringUtils.isBlank(customerId)) {
            diagnostic.prepareDebug("Failed to fetch Customer ID").log();
            return ErrorCodeEnum.ERR_30008.setErrorCode(result);
        }

        String referenceId = request.getParameter("documentReference");
        if (StringUtils.isBlank(referenceId)) {
            diagnostic.prepareDebug("Mandatory fields are missing").log();
            return ErrorCodeEnum.ERR_30004.setErrorCode(result);
        }

        try {
            result = orderBusinessDelegate.fetchDocumentDBXDB(customerId, referenceId, request);
            return result;
        } catch (Exception e) {
            diagnostic.prepareDebug("Failed to fetch document. Error: " + e).log();
            return ErrorCodeEnum.ERR_30012.setErrorCode(result);
        }
    }

    @Override
    public Result deleteDocument(DataControllerRequest request) {
        Result result = new Result();
        Map<String, Object> customer = CustomerSession.getCustomerMap(request);
        String customerId = CustomerSession.getCustomerId(customer);
        if (StringUtils.isBlank(customerId)) {
            diagnostic.prepareDebug("Failed to fetch Customer ID").log();
            return ErrorCodeEnum.ERR_30008.setErrorCode(result);
        }

        String referenceId = request.getParameter("documentReference");
        if (StringUtils.isBlank(referenceId)) {
            diagnostic.prepareDebug("Mandatory fields are missing").log();
            return ErrorCodeEnum.ERR_30004.setErrorCode(result);
        }

        try {
            result = orderBusinessDelegate.deleteDocumentDBXDB(customerId, referenceId, request);
            return result;
        } catch (Exception e) {
            diagnostic.prepareDebug("Failed to delete document. Error: " + e).log();
            return ErrorCodeEnum.ERR_30013.setErrorCode(result);
        }
    }

    private Boolean validateFileExtension(String fileExtension, String uploadedFileName) {
        Pattern regexFileName = Pattern.compile("^[-_() A-Za-z0-9]+\\.(pdf|xlsx|xls|csv|png|jpeg|doc|docx|bmp|zip|tiff){0,250}$");
        if (!validFileExtensions.contains(fileExtension)
                || !regexFileName.matcher(uploadedFileName).matches()) {
            return false;
        }

        if (fileExtension.equalsIgnoreCase("application/pdf")) {
            String nameExtension = uploadedFileName.substring(uploadedFileName.length() - 3);
            if (!nameExtension.equalsIgnoreCase("pdf")) {
                return false;
            }
        }

        if (fileExtension.equalsIgnoreCase("image/jpeg")) {
            String nameExtension = uploadedFileName.substring(uploadedFileName.length() - 4);
            return nameExtension.equalsIgnoreCase("jpeg");
        }

        return true;
    }

    private static boolean isFileSizeValid(String base64string) {
        double fileSizeInMB = -1;
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64string);
            fileSizeInMB = (double) decodedBytes.length / (1024 * 1024);
        } catch (Exception e) {
            diagnostic.prepareDebug("Error validating file:" + e).log();
        }
        return fileSizeInMB != -1 && fileSizeInMB <= MAX_ALLOWED_FILE_SIZE;
    }
}
