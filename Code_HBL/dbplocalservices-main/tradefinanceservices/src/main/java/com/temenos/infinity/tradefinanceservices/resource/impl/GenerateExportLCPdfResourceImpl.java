/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.resource.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.memorymgmt.CorporateManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.GetExportLetterOfCreditsByIdBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.InitiateDownloadExportLCBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.constants.Constants;
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.temenos.infinity.tradefinanceservices.dto.ExportLOCDTO;
import com.temenos.infinity.tradefinanceservices.resource.api.GenerateExportLCPdfResource;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import java.util.HashMap;
import java.util.Map;

import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.PREFIX_EXPORT_LOC;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.generateTradeFinanceFileID;

public class GenerateExportLCPdfResourceImpl implements GenerateExportLCPdfResource {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    public Result initiateExportLCpdf(Object[] inputArray, DataControllerRequest request) {
        byte[] bytes = new byte[0];
        Result result = new Result();
        GetExportLetterOfCreditsByIdBusinessDelegate exportBusinessDelegate = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(GetExportLetterOfCreditsByIdBusinessDelegate.class);
        InitiateDownloadExportLCBusinessDelegate pdfBusinessDelegate = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(InitiateDownloadExportLCBusinessDelegate.class);

        @SuppressWarnings("unchecked")
        Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
        String srmsRequestId = inputParams.get("exportLCId").toString();
        if (StringUtils.isBlank(srmsRequestId)) {
            alert.prepareError("SRMS RequestId is missing").log();
            return ErrorCodeEnum.ERRTF_29055.setErrorCode(new Result(), Constants.PROVIDE_MANDATORY_FIELDS);
        }

        Map<String, Object> customer = CustomerSession.getCustomerMap(request);
        String customerId = CustomerSession.getCustomerId(customer);
        try {
            if (StringUtils.isNotBlank(srmsRequestId)) {
                ExportLOCDTO exportdto = exportBusinessDelegate.getExportLetterOfCreditById(srmsRequestId, request);
                if (!StringUtils.isNotBlank(customerId) && customerId.equalsIgnoreCase(exportdto.getCustomerId())) {
                    diagnostic.prepareInfo("Failed validating customerId.").log();
                    return ErrorCodeEnum.ERRTF_29070.setErrorCode(new Result());
                }
                if (StringUtils.isBlank(exportdto.getApplicant())) {
                    alert.prepareError("Requested Record Not Found").log();
                    return ErrorCodeEnum.ERRTF_29057.setErrorCode(result);
                }
                bytes = pdfBusinessDelegate.getRecordPDFAsBytes(exportdto, request);
            }
            String fileId = generateTradeFinanceFileID(PREFIX_EXPORT_LOC);
            MemoryManager.saveIntoCache(fileId + new CorporateManager(request).getSessionId(), Base64.encodeBase64String(bytes), 120);
            result.addParam("fileId", fileId);
            return result;
        } catch (Exception e) {
            alert.prepareError("Error while generating the trade finance file", e).log();
        }
        return ErrorCodeEnum.ERRTF_29056.setErrorCode(new Result());
    }

}
