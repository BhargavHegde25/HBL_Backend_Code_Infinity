/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.temenos.dbx.product.commons.businessdelegate.api.AccountBusinessDelegate;
import com.temenos.dbx.product.commons.dto.CustomerAccountsDTO;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.ExportLetterOfCreditsDrawingsBackendDelegate;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.GetExportLetterOfCreditsByIdBackendDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.ExportLetterOfCreditsDrawingsBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants;
import com.temenos.infinity.tradefinanceservices.dto.ExportLCDrawingsDTO;
import com.temenos.infinity.tradefinanceservices.dto.ExportLOCDTO;
import com.temenos.infinity.tradefinanceservices.utils.ExcelBusinessDelegate;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.temenos.infinity.tradefinanceservices.utils.PdfGenerator.*;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.*;

public class ExportLetterOfCreditsDrawingsBusinessDelegateImpl implements ExportLetterOfCreditsDrawingsBusinessDelegate, ExcelBusinessDelegate, TradeFinanceConstants {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public ExportLCDrawingsDTO createExportDrawing(ExportLCDrawingsDTO inputDTO, DataControllerRequest request) {
        ExportLetterOfCreditsDrawingsBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(ExportLetterOfCreditsDrawingsBackendDelegate.class);
        return backendDelegate.createExportDrawing(inputDTO, request);
    }

    @Override
    public ExportLCDrawingsDTO updateExportLetterOfCreditDrawing(ExportLCDrawingsDTO exportPayloadDTO,
                                                                 DataControllerRequest request) {
        ExportLetterOfCreditsDrawingsBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(ExportLetterOfCreditsDrawingsBackendDelegate.class);

        return backendDelegate.updateExportLetterOfCreditDrawing(exportPayloadDTO, request);
    }

    @Override
    public List<ExportLCDrawingsDTO> getExportLetterOfCreditDrawings(DataControllerRequest request) {
        ExportLetterOfCreditsDrawingsBackendDelegate exportBackendDelegate = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(ExportLetterOfCreditsDrawingsBackendDelegate.class);
        List<ExportLCDrawingsDTO> exportDrawings = exportBackendDelegate.getExportLetterOfCreditDrawings(request);
        return exportDrawings;
    }

    public List<ExportLCDrawingsDTO> getList(DataControllerRequest request) {
        ExportLetterOfCreditsDrawingsBackendDelegate exportBackendDelegate = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(ExportLetterOfCreditsDrawingsBackendDelegate.class);
        return exportBackendDelegate.getExportLetterOfCreditDrawings(request);
    }

    @Override
    public ExportLCDrawingsDTO getExportLetterOfCreditDrawingById(DataControllerRequest request,
                                                                  String drawingSRMSRequestId) {
        ExportLetterOfCreditsDrawingsBackendDelegate exportBackendDelegate = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(ExportLetterOfCreditsDrawingsBackendDelegate.class);
        ExportLCDrawingsDTO exportDrawing = exportBackendDelegate.getExportLetterOfCreditDrawingById(request,
                drawingSRMSRequestId);
        return exportDrawing;
    }

    public byte[] generateExportDrawingPdf(ExportLCDrawingsDTO drawingDetails, DataControllerRequest request) {
        GetExportLetterOfCreditsByIdBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(GetExportLetterOfCreditsByIdBackendDelegate.class);
        ExportLOCDTO exportDetails = backendDelegate.getExportLetterOfCreditById(drawingDetails.getExportLCId(),
                request);

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outStream);
        addHeadersAndFootersPDF(writer, document, "Export LC Drawing", false);

        try {
            document.open();

            document.add(addCustomerDetailsPDF(request));
            document.add(_lcSummary(exportDetails));
            document.add(_drawingDetails(drawingDetails, request));
            document.add(_documentDetails(drawingDetails));
            document.add(_paymentDetails(drawingDetails, request));
        } catch (Exception e) {
            alert.prepareError("Error occurred while creating pdf ", e).log();
            return null;
        } finally {
            document.close();
        }
        return outStream.toByteArray();
    }

    public PdfPTable _lcSummary(ExportLOCDTO drawingDetails) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(_noBorderHeadingCell("LC Summary", darkFont));

        table.addCell(_noBorderCell("Applicant:", lightFont));
        if (StringUtils.isNotBlank(drawingDetails.getApplicant())) {
            table.addCell(_noBorderCell(drawingDetails.getApplicant(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("LC Ref No:", lightFont));
        if (StringUtils.isNotBlank(drawingDetails.getLcReferenceNo())) {
            table.addCell(_noBorderCell(drawingDetails.getLcReferenceNo(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Credit Amount:", lightFont));
        if (StringUtils.isNotBlank(drawingDetails.getAmount())
                && StringUtils.isNotBlank(drawingDetails.getCurrency())) {
            table.addCell(_noBorderCell(getCurrencySymbol(drawingDetails.getCurrency()) + " "
                    + formatAmount(drawingDetails.getAmount()), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("LC Type:", lightFont));
        if (StringUtils.isNotBlank(drawingDetails.getLcType())) {
            table.addCell(_noBorderCell(drawingDetails.getLcType(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Issue Date:", lightFont));
        if (StringUtils.isNotBlank(drawingDetails.getIssueDate())) {
            table.addCell(_noBorderCell(_formatDate(drawingDetails.getIssueDate()), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Expiry Date:", lightFont));
        if (StringUtils.isNotBlank(drawingDetails.getExpiryDate())) {
            table.addCell(_noBorderCell(_formatDate(drawingDetails.getExpiryDate()), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Currency:", lightFont));
        if (StringUtils.isNotBlank(drawingDetails.getCurrency())) {
            table.addCell(_noBorderCell(drawingDetails.getCurrency(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Issuing Bank:", lightFont));
        if (StringUtils.isNotBlank(drawingDetails.getIssuingBank())) {
            table.addCell(_noBorderCell(drawingDetails.getIssuingBank(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        return table;
    }

    public PdfPTable _drawingDetails(ExportLCDrawingsDTO drawingDetails, DataControllerRequest request) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(_noBorderHeadingCell("Drawing Details", darkFont));

        table.addCell(_noBorderCell("Drawing Status:", lightFont));
        if (StringUtils.isNotBlank(drawingDetails.getStatus())) {
            table.addCell(_noBorderCell(drawingDetails.getStatus(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Drawing Amount:", lightFont));
        if (StringUtils.isNotBlank(drawingDetails.getDrawingAmount())
                && StringUtils.isNotBlank(drawingDetails.getCurrency())) {
            table.addCell(_noBorderCell(getCurrencySymbol(drawingDetails.getCurrency()) + " "
                    + formatAmount(drawingDetails.getDrawingAmount()), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Currency:", lightFont));
        if (StringUtils.isNotBlank(drawingDetails.getCurrency())) {
            table.addCell(_noBorderCell(drawingDetails.getCurrency(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Credit Account:", lightFont));
        String maskedAccount = "NA";
        if (StringUtils.isNotBlank(drawingDetails.getCreditAccount())) {
            String accountToBeDebited = drawingDetails.getCreditAccount();
            Map<String, Object> customer = CustomerSession.getCustomerMap(request);
            String customerId = CustomerSession.getCustomerId(customer);
            AccountBusinessDelegate actDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(AccountBusinessDelegate.class);
            CustomerAccountsDTO account = actDelegate.getAccountDetails(customerId, accountToBeDebited);
            String accountType = ((account != null) && (StringUtils.isNotBlank(account.getAccountType())))
                    ? account.getAccountType()
                    : "";
            maskedAccount = accountType + "..." + accountToBeDebited.substring(accountToBeDebited.length() - 4);
        }
        table.addCell(_noBorderCell(maskedAccount, darkFont));

        table.addCell(_noBorderCell("Message to Bank:", lightFont));
        if (StringUtils.isNotBlank(drawingDetails.getMessageToBank())) {
            table.addCell(_noBorderCell(drawingDetails.getMessageToBank(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        return table;
    }

    public PdfPTable _documentDetails(ExportLCDrawingsDTO drawingDetails) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(_noBorderHeadingCell("Documents & Status", darkFont));

        table.addCell(_noBorderCell("Total Documents / Discrepancies:", lightFont));
        if (StringUtils.isNotBlank(drawingDetails.getTotalDocuments())) {
            table.addCell(_noBorderCell(drawingDetails.getTotalDocuments(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Document Type:", lightFont));
        if (StringUtils.isNotBlank(drawingDetails.getDocumentStatus())) {
            table.addCell(_noBorderCell(drawingDetails.getDocumentStatus(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Uploaded Documents:", lightFont));
        if (StringUtils.isNotBlank(drawingDetails.getUploadedDocuments())) {
            String str = "";
            JSONArray arr = new JSONArray(drawingDetails.getUploadedDocuments());
            for (int i = 0; i < arr.length(); i++) {
                str += arr.get(i) + "\n";
            }
            table.addCell(_noBorderCell(str, darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Discrepant Details:", lightFont));
        if (StringUtils.isNotBlank(drawingDetails.getDiscrepencies())) {
            String str = "";
            JSONArray discrepencies = new JSONArray(drawingDetails.getDiscrepencies());
            for (int i = 0; i < discrepencies.length(); i++) {
                JSONObject obj = discrepencies.getJSONObject(i);
                Iterator<String> discrepenciesKeys = obj.keys();
                while (discrepenciesKeys.hasNext()) {
                    str += (String) discrepenciesKeys.next() + "\n";
                }
            }
            table.addCell(_noBorderCell(str, darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        return table;
    }

    public PdfPTable _paymentDetails(ExportLCDrawingsDTO drawingDetails, DataControllerRequest request) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(_noBorderHeadingCell("Payment Details", darkFont));

        table.addCell(_noBorderCell("Payment Status:", lightFont));
        if (StringUtils.isNotBlank(drawingDetails.getPaymentStatus())) {
            table.addCell(_noBorderCell(drawingDetails.getPaymentStatus(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Payment Date:", lightFont));
        if (StringUtils.isNotBlank(drawingDetails.getPaymentDate())) {
            table.addCell(_noBorderCell(_formatDate(drawingDetails.getPaymentDate()), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Total Amount to be Paid:", lightFont));
        if (StringUtils.isNotBlank(drawingDetails.getTotalAmount())
                && StringUtils.isNotBlank(drawingDetails.getCurrency())) {
            table.addCell(_noBorderCell(getCurrencySymbol(drawingDetails.getCurrency()) + " "
                    + formatAmount(drawingDetails.getTotalAmount()), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Credit Account:", lightFont));
        String maskedAccount = "NA";
        if (StringUtils.isNotBlank(drawingDetails.getCreditAccount())) {
            String accountToBeDebited = drawingDetails.getCreditAccount();
            Map<String, Object> customer = CustomerSession.getCustomerMap(request);
            String customerId = CustomerSession.getCustomerId(customer);
            AccountBusinessDelegate actDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(AccountBusinessDelegate.class);
            CustomerAccountsDTO account = actDelegate.getAccountDetails(customerId, accountToBeDebited);
            String accountType = ((account != null) && (StringUtils.isNotBlank(account.getAccountType())))
                    ? account.getAccountType()
                    : "";
            maskedAccount = accountType + "..." + accountToBeDebited.substring(accountToBeDebited.length() - 4);
        }
        table.addCell(_noBorderCell(maskedAccount, darkFont));

        return table;
    }

}
