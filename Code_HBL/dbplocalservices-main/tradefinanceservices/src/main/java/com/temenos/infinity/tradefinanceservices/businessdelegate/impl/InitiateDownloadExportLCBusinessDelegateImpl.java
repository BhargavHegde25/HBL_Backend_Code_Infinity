/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.impl;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.InitiateDownloadExportLCBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.dto.ExportLOCDTO;
import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import java.io.ByteArrayOutputStream;

import static com.temenos.infinity.tradefinanceservices.utils.PdfGenerator.*;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.*;

public class InitiateDownloadExportLCBusinessDelegateImpl implements InitiateDownloadExportLCBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public byte[] getRecordPDFAsBytes(ExportLOCDTO exportdto, DataControllerRequest request) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outStream);
        addHeadersAndFootersPDF(writer, document, "Export Letter of Credit", true);

        try {
            document.open();

            document.add(addCustomerDetailsPDF(request));
            document.add(_createLCDetails(exportdto));
            document.add(_createBeneficiaryDetails(exportdto));
            document.add(_createShippingDetails(exportdto));
            document.add(_createDocumentsTerms(exportdto));

            document.close();
            return outStream.toByteArray();
        } catch (Exception e) {
            alert.prepareError("Error occurred while creating trade finance details pdf ", e).log();
            return null;
        }
    }

    private PdfPTable _createLCDetails(ExportLOCDTO exportdto) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(_noBorderHeadingCell("LC Details", darkFont));

        table.addCell(_noBorderCell("LC Reference Number:", lightFont));
        if (StringUtils.isNotBlank(exportdto.getLcReferenceNo())) {
            table.addCell(_noBorderCell(exportdto.getLcReferenceNo(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("LC Type:", lightFont));
        if (StringUtils.isNotBlank(exportdto.getLcType())) {
            table.addCell(_noBorderCell(exportdto.getLcType(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Payment Terms:", lightFont));
        if (StringUtils.isNotBlank(exportdto.getPaymentTerms())) {
            table.addCell(_noBorderCell(exportdto.getPaymentTerms(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Issuing Bank Reference:", lightFont));
        if (StringUtils.isNotBlank(exportdto.getIssuingBankReference())) {
            table.addCell(_noBorderCell(exportdto.getIssuingBankReference(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Advising Bank Reference:", lightFont));
        if (StringUtils.isNotBlank(exportdto.getAdvisingBankReference())) {
            table.addCell(_noBorderCell(exportdto.getAdvisingBankReference(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Utilized LC Amount:", lightFont));
        if (StringUtils.isNotBlank(exportdto.getUtilizedLCAmount()) && StringUtils.isNotBlank(exportdto.getCurrency())) {
            table.addCell(_noBorderCell(getCurrencySymbol(exportdto.getCurrency()) + " " + formatAmount(exportdto.getUtilizedLCAmount()), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Applicant:", lightFont));
        if (StringUtils.isNotBlank(exportdto.getApplicant())) {
            table.addCell(_noBorderCell(exportdto.getApplicant(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Applicant Address:", lightFont));
        if (StringUtils.isNotBlank(exportdto.getApplicantaddress())) {
            table.addCell(_noBorderCell(exportdto.getApplicantaddress(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Issuing Bank:", lightFont));
        if (StringUtils.isNotBlank(exportdto.getIssuingBank())) {
            table.addCell(_noBorderCell(exportdto.getIssuingBank(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Issuing Bank Address:", lightFont));
        if (StringUtils.isNotBlank(exportdto.getIssuingbankaddress())) {
            table.addCell(_noBorderCell(exportdto.getIssuingbankaddress(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Issue Date:", lightFont));
        if (StringUtils.isNotBlank(exportdto.getIssueDate())) {
            table.addCell(_noBorderCell(_formatDate(exportdto.getIssueDate()), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Expiry Date:", lightFont));
        if (StringUtils.isNotBlank(exportdto.getExpiryDate())) {
            table.addCell(_noBorderCell(_formatDate(exportdto.getExpiryDate()), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("LC Amount:", lightFont));
        if (StringUtils.isNotBlank(exportdto.getCurrency()) && StringUtils.isNotBlank(exportdto.getAmount())) {
            table.addCell(_noBorderCell(getCurrencySymbol(exportdto.getCurrency()) + " " + formatAmount(exportdto.getAmount()), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        return table;
    }

    private PdfPTable _createBeneficiaryDetails(ExportLOCDTO exportdto) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(_noBorderHeadingCell("Beneficiary Details", darkFont));

        table.addCell(_noBorderCell("Beneficiary Name:", lightFont));
        if (StringUtils.isNotBlank(exportdto.getBeneficiaryName())) {
            table.addCell(_noBorderCell(exportdto.getBeneficiaryName(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Beneficiary Address:", lightFont));
        if (StringUtils.isNotBlank(exportdto.getBeneficiaryAddress())) {
            table.addCell(_noBorderCell(exportdto.getBeneficiaryAddress(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        return table;
    }

    private PdfPTable _createShippingDetails(ExportLOCDTO exportdto) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(_noBorderHeadingCell("Good and Shipping Details", darkFont));

        table.addCell(_noBorderCell("Goods Description:", lightFont));
        if (StringUtils.isNotBlank(exportdto.getGoodsDescription())) {
            table.addCell(_noBorderCell(exportdto.getGoodsDescription(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Additional Conditions:", lightFont));
        if (StringUtils.isNotBlank(exportdto.getAdditionalConditions())) {
            table.addCell(_noBorderCell(exportdto.getAdditionalConditions(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Confirm Instructions:", lightFont));
        if (StringUtils.isNotBlank(exportdto.getConfirmInstructions())) {
            table.addCell(_noBorderCell(exportdto.getConfirmInstructions(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Latest Shipping Date:", lightFont));
        if (StringUtils.isNotBlank(exportdto.getLatestShipmentDate())) {
            table.addCell(_noBorderCell(_formatDate(exportdto.getLatestShipmentDate()), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        return table;
    }

    private PdfPTable _createDocumentsTerms(ExportLOCDTO exportdto) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(_noBorderHeadingCell("Documents and Terms", darkFont));

        table.addCell(_noBorderCell("Document Name:", lightFont));
        if (StringUtils.isNotBlank(exportdto.getDocumentName())) {
            table.addCell(_noBorderCell(exportdto.getDocumentName(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Uploaded Files:", lightFont));
        if (StringUtils.isNotBlank(exportdto.getUploadedFiles())) {
            table.addCell(_noBorderCell(exportdto.getUploadedFiles(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        return table;
    }
}