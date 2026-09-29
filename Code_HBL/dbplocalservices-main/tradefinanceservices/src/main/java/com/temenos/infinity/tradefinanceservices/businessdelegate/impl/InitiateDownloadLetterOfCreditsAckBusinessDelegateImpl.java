/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.impl;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.InitiateDownloadLetterOfCreditsAckBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.dto.LetterOfCreditsDTO;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import java.io.ByteArrayOutputStream;

import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.darkFont;
import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.lightFont;
import static com.temenos.infinity.tradefinanceservices.utils.PdfGenerator.*;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.*;

public class InitiateDownloadLetterOfCreditsAckBusinessDelegateImpl
        implements InitiateDownloadLetterOfCreditsAckBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    public byte[] getRecordPDFAsBytes(LetterOfCreditsDTO importLcDTO, String customerId, DataControllerRequest request) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outStream);
        addHeadersAndFootersPDF(writer, document, "Import Letter of Credit", true);

        try {
            document.open();

            document.add(addCustomerDetailsPDF(request));
            document.add(_creditDetails(importLcDTO, customerId));
            document.add(_beneficiaryDetails(importLcDTO));
            document.add(_shipmentDetails(importLcDTO));
            document.add(_documentAndTerms(importLcDTO));
        } catch (Exception e) {
            alert.prepareError("Error occurred while creating pdf ", e).log();
            return null;
        } finally {
            document.close();
        }
        return outStream.toByteArray();
    }

    private Element _creditDetails(LetterOfCreditsDTO importLcDTO, String customerId) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(_noBorderHeadingCell("Credit Details", darkFont));

        table.addCell(_noBorderCell("Reference Number", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(importLcDTO.getLcReferenceNo()) ?
                importLcDTO.getLcReferenceNo() : "NA", darkFont));

        table.addCell(_noBorderCell("Credit Amount", lightFont));
        table.addCell(_noBorderCell(getAmountWithCurrency(importLcDTO.getLcCurrency(), String.valueOf(importLcDTO.getLcAmount()), false), darkFont));

        table.addCell(_noBorderCell("Tolerance Percentage (Optional):", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(importLcDTO.getTolerancePercentage()) ?
                importLcDTO.getTolerancePercentage() + " %" : "NA", darkFont));

        table.addCell(_noBorderCell("Maximum Credit Amount (Optional):", lightFont));
        table.addCell(_noBorderCell(getAmountWithCurrency(importLcDTO.getLcCurrency(), String.valueOf(importLcDTO.getMaximumCreditAmount()), false), darkFont));

        table.addCell(_noBorderCell("Additional Payment Amount:", lightFont));
        table.addCell(_noBorderCell(getAmountWithCurrency(importLcDTO.getLcCurrency(), String.valueOf(importLcDTO.getAdditionalAmountPayable()), false), darkFont));

        table.addCell(_noBorderCell("Payment Terms:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(importLcDTO.getPaymentTerms()) ?
                importLcDTO.getPaymentTerms() : "NA", darkFont));

        table.addCell(_noBorderCell("Available with:", lightFont));
        String availableWith = (StringUtils.isNotBlank(importLcDTO.getAvailableWith1()) ? importLcDTO.getAvailableWith1() + "\n" : "") +
                (StringUtils.isNotBlank(importLcDTO.getAvailableWith2()) ? importLcDTO.getAvailableWith2() + "\n" : "") +
                (StringUtils.isNotBlank(importLcDTO.getAvailableWith3()) ? importLcDTO.getAvailableWith3() + "\n" : "") +
                (StringUtils.isNotBlank(importLcDTO.getAvailableWith4()) ? importLcDTO.getAvailableWith4() + "\n" : "");
        table.addCell(_noBorderCell(StringUtils.isNotBlank(availableWith) ? availableWith : "NA", darkFont));

        table.addCell(_noBorderCell("Issue Date:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(importLcDTO.getIssueDate()) ?
                _formatDate(importLcDTO.getIssueDate()) : "NA", darkFont));

        table.addCell(_noBorderCell("Expiry Date:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(importLcDTO.getExpiryDate()) ?
                _formatDate(importLcDTO.getExpiryDate()) : "NA", darkFont));

        table.addCell(_noBorderCell("Charges Account:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(importLcDTO.getChargesAccount()) ?
                getMaskedAccountDetails(customerId, importLcDTO.getChargesAccount()) : "NA", darkFont));

        table.addCell(_noBorderCell("Margin Account:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(importLcDTO.getMarginAccount()) ?
                getMaskedAccountDetails(customerId, importLcDTO.getMarginAccount()) : "NA", darkFont));

        table.addCell(_noBorderCell("Commission Account:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(importLcDTO.getCommisionAccount()) ?
                getMaskedAccountDetails(customerId, importLcDTO.getCommisionAccount()) : "NA", darkFont));

        table.addCell(_noBorderCell("Message To Bank (Optional):", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(importLcDTO.getMessageToBank()) ?
                importLcDTO.getMessageToBank() : "NA", darkFont));

        return table;
    }

    private Element _beneficiaryDetails(LetterOfCreditsDTO importLcDTO) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(_noBorderHeadingCell("Beneficiary Details", darkFont));

        table.addCell(_noBorderCell("Beneficiary Name:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(importLcDTO.getBeneficiaryName()) ?
                importLcDTO.getBeneficiaryName() : "NA", darkFont));

        table.addCell(_noBorderCell("Beneficiary Address (Optional):", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(importLcDTO.getBeneficiaryAddressLine1()) ?
                importLcDTO.getBeneficiaryAddressLine1() : "NA", darkFont));

        table.addCell(_noBorderCell("Beneficiary Bank (Optional):", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(importLcDTO.getBeneficiaryBank()) ?
                importLcDTO.getBeneficiaryBank() : "NA", darkFont));

        table.addCell(_noBorderCell("Beneficiary Bank Address (Optional):", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(importLcDTO.getBeneficiaryBankAdressLine1()) ?
                importLcDTO.getBeneficiaryBankAdressLine1() : "NA", darkFont));

        return table;
    }

    private Element _shipmentDetails(LetterOfCreditsDTO importLcDTO) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(_noBorderHeadingCell("Shipment Details", darkFont));

        table.addCell(_noBorderCell("Place of taking in charge (Optional):", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(importLcDTO.getPlaceOfTakingIncharge()) ?
                importLcDTO.getPlaceOfTakingIncharge() : "NA", darkFont));

        table.addCell(_noBorderCell("Port of Loading (Optional):", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(importLcDTO.getPortOfLoading()) ?
                importLcDTO.getPortOfLoading() : "NA", darkFont));

        table.addCell(_noBorderCell("Port of Discharge (Optional):", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(importLcDTO.getPortOfDischarge()) ?
                importLcDTO.getPortOfDischarge() : "NA", darkFont));

        table.addCell(_noBorderCell("Place of Final Delivery (Optional):", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(importLcDTO.getPlaceOfFinalDelivery()) ?
                importLcDTO.getPlaceOfFinalDelivery() : "NA", darkFont));

        table.addCell(_noBorderCell("Latest Shipment Date (Optional):", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(importLcDTO.getLatestShippingDate()) ?
                _formatDate(importLcDTO.getLatestShippingDate()) : "NA", darkFont));

        table.addCell(_noBorderCell("Transhipment (Optional):", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(importLcDTO.getTransshipment()) ?
                importLcDTO.getTransshipment() : "NA", darkFont));

        table.addCell(_noBorderCell("Partial Shipment (Optional):", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(importLcDTO.getPartialShipments()) ?
                importLcDTO.getPartialShipments() : "NA", darkFont));

        table.addCell(_noBorderCell("Inco Terms:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(importLcDTO.getIncoTerms()) ?
                importLcDTO.getIncoTerms() : "NA", darkFont));

        table.addCell(_noBorderCell("Mode of Shipment (Optional):", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(importLcDTO.getModeOfShipment()) ?
                importLcDTO.getModeOfShipment() : "NA", darkFont));

        return table;
    }

    private Element _documentAndTerms(LetterOfCreditsDTO importLcDTO) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(_noBorderHeadingCell("Documents and Terms", darkFont));

        table.addCell(_noBorderCell("Description of Goods (Optional):", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(importLcDTO.getDescriptionOfGoods()) ?
                importLcDTO.getDescriptionOfGoods() : "NA", darkFont));

        table.addCell(_noBorderCell("Documents Required (Optional):", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(importLcDTO.getDocumentsRequired()) ?
                importLcDTO.getDocumentsRequired() : "NA", darkFont));

        table.addCell(_noBorderCell("Additional Conditions Code (Optional):", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(importLcDTO.getAdditionalConditionsCode()) ?
                importLcDTO.getAdditionalConditionsCode() : "NA", darkFont));

        table.addCell(_noBorderCell("Other Additional Conditions (Optional):", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(importLcDTO.getOtherAdditionalConditions()) ?
                importLcDTO.getOtherAdditionalConditions() : "NA", darkFont));

        table.addCell(_noBorderCell("Charges:", lightFont));
        table.addCell(_noBorderCell(getAmountWithCurrency(importLcDTO.getLcCurrency(), importLcDTO.getDocumentCharges(), false), darkFont));

        table.addCell(_noBorderCell("Confirmation Instructions:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(importLcDTO.getConfirmationInstruction()) ?
                importLcDTO.getConfirmationInstruction() : "NA", darkFont));

        table.addCell(_noBorderCell("Transferable:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(importLcDTO.getTransferable()) ?
                importLcDTO.getTransferable() : "NA", darkFont));

        table.addCell(_noBorderCell("Stand by LC:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(importLcDTO.getStandByLC()) ?
                importLcDTO.getStandByLC() : "NA", darkFont));

        table.addCell(_noBorderCell("Uploaded Files:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(importLcDTO.getFileToUpload()) ?
                importLcDTO.getFileToUpload() : "NA", darkFont));

        return table;
    }
}
