/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.ReceivedGuaranteeAmendmentsBackendDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.ReceivedGuaranteeAmendmentsBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.dto.ReceivedAmendmentsDTO;
import com.temenos.infinity.tradefinanceservices.dto.ReceivedGuaranteesDTO;
import com.temenos.infinity.tradefinanceservices.utils.ExcelBusinessDelegate;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.List;

import static com.temenos.infinity.tradefinanceservices.utils.PdfGenerator.addCustomerDetailsPDF;
import static com.temenos.infinity.tradefinanceservices.utils.PdfGenerator.addHeadersAndFootersPDF;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.*;

public class ReceivedGuaranteeAmendmentsBusinessDelegateImpl implements ReceivedGuaranteeAmendmentsBusinessDelegate, ExcelBusinessDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private final ReceivedGuaranteeAmendmentsBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(ReceivedGuaranteeAmendmentsBackendDelegate.class);
    Color lightFont = new Color(114, 114, 114);
    Color darkFont = new Color(66, 66, 66);

    @Override
    public ReceivedAmendmentsDTO createReceivedAmendment(ReceivedAmendmentsDTO inputDto, DataControllerRequest request) {
        return orderBackendDelegate.createReceivedAmendment(inputDto, request);
    }

    @Override
    public ReceivedAmendmentsDTO updateReceivedAmendment(ReceivedAmendmentsDTO amendmentDetails, DataControllerRequest request) {
        return orderBackendDelegate.updateReceivedAmendment(amendmentDetails, request);
    }

    @Override
    public List<ReceivedAmendmentsDTO> getReceivedAmendments(DataControllerRequest request) {
        return orderBackendDelegate.getReceivedAmendments(request);
    }

    @Override
    public List<ReceivedAmendmentsDTO> getList(DataControllerRequest request) throws ApplicationException {
        return orderBackendDelegate.getReceivedAmendments(request);
    }

    @Override
    public ReceivedAmendmentsDTO getReceivedAmendmentById(String amendmentSrmsId, DataControllerRequest request) {
        return orderBackendDelegate.getReceivedAmendmentById(amendmentSrmsId, request);
    }

    @Override
    public byte[] generateReceivedAmendment(ReceivedAmendmentsDTO amendmentsDTO, ReceivedGuaranteesDTO receivedGuaranteeDTO, DataControllerRequest request) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outStream);
        addHeadersAndFootersPDF(writer, document, "Received GT & SBLC Amendment", true);
        try {
            document.open();
            document.add(addCustomerDetailsPDF(request));
            document.add(_issuanceSummaryDetails(amendmentsDTO, receivedGuaranteeDTO));
            document.add(_amendmentDetails(amendmentsDTO));
            document.add(_amendmentRequestedDetails(amendmentsDTO));
            document.add(_documentDetails(amendmentsDTO));
        } catch (Exception e) {
            alert.prepareError("Error occurred while creating pdf ", e).log();
            return null;
        } finally {
            document.close();
        }
        return outStream.toByteArray();
    }

    public PdfPTable _issuanceSummaryDetails(ReceivedAmendmentsDTO amendmentsDTO, ReceivedGuaranteesDTO receivedGuaranteeDTO) {
        PdfPTable table = new PdfPTable(2);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(_noBorderHeadingCell("Guarantee & Standby LC Summary", darkFont));
        table.addCell(_noBorderCell("", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Applicant:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(amendmentsDTO.getApplicant()) ? amendmentsDTO.getApplicant() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Transaction Ref:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(receivedGuaranteeDTO.getGuaranteeSrmsId()) ? receivedGuaranteeDTO.getGuaranteeSrmsId() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Product Type:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(receivedGuaranteeDTO.getProductType()) ? receivedGuaranteeDTO.getProductType() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("GT & SBLC Type:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(receivedGuaranteeDTO.getLcType()) ? receivedGuaranteeDTO.getLcType() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Amount:", lightFont));
        if (StringUtils.isNotBlank(receivedGuaranteeDTO.getAmount()) && StringUtils.isNotBlank(receivedGuaranteeDTO.getCurrency())) {
            table.addCell(_noBorderCell(receivedGuaranteeDTO.getCurrency() + " " + formatAmount(receivedGuaranteeDTO.getAmount()), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }
        table.completeRow();

        table.addCell(_noBorderCell("Expiry Type:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(receivedGuaranteeDTO.getExpiryType()) ? receivedGuaranteeDTO.getExpiryType() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Issue Date:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(receivedGuaranteeDTO.getExpectedIssueDate()) ? receivedGuaranteeDTO.getExpectedIssueDate() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Expiry Date:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(receivedGuaranteeDTO.getExpiryDate()) ? receivedGuaranteeDTO.getExpiryDate() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Issuing Bank:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(receivedGuaranteeDTO.getIssuingBankName()) ? receivedGuaranteeDTO.getIssuingBankName() : "NA", darkFont));
        table.completeRow();

        return table;
    }

    public PdfPTable _amendmentDetails(ReceivedAmendmentsDTO amendmentsDTO) {
        PdfPTable table = new PdfPTable(2);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(_noBorderHeadingCell("Amendment Details", darkFont));
        table.addCell(_noBorderCell("", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Status:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(amendmentsDTO.getStatus()) ? amendmentsDTO.getStatus() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Amendment No:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(amendmentsDTO.getAmendmentNo()) ? amendmentsDTO.getAmendmentNo() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Received On:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(amendmentsDTO.getReceivedOn()) ? _formatDate(amendmentsDTO.getReceivedOn()) : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Amendment Reference:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(amendmentsDTO.getAmendmentSrmsId()) ? amendmentsDTO.getAmendmentSrmsId() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Amendment Charges:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(amendmentsDTO.getAmendmentCharges()) ? amendmentsDTO.getAmendmentCharges() : "NA", darkFont));
        table.completeRow();

        return table;
    }

    public PdfPTable _amendmentRequestedDetails(ReceivedAmendmentsDTO amendmentsDTO) {
        PdfPTable table = new PdfPTable(2);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(_noBorderHeadingCell("Amendment Requested", darkFont));
        table.addCell(_noBorderCell("", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Date of Amount Change:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(amendmentsDTO.getDateOfAmountChange()) ? amendmentsDTO.getDateOfAmountChange() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Amount:", lightFont));
        if (StringUtils.isNotBlank(amendmentsDTO.getAmount()) && StringUtils.isNotBlank(amendmentsDTO.getCurrency())) {
            table.addCell(_noBorderCell(amendmentsDTO.getCurrency() + " " + formatAmount(amendmentsDTO.getAmount()), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }
        table.completeRow();

        table.addCell(_noBorderCell("Expiry Type:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(amendmentsDTO.getAmendExpiryType()) ? amendmentsDTO.getAmendExpiryType() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Beneficiary Details:", lightFont));
        if (StringUtils.isNotBlank(amendmentsDTO.getBeneficiaryDetails())) {
            JSONObject beneficiaryDetails = (new JSONArray(amendmentsDTO.getBeneficiaryDetails())).getJSONObject(0);
            String beneficiary = beneficiaryDetails.getString("address1") + "\n";
            beneficiary += beneficiaryDetails.getString("address2") + "\n";
            beneficiary += beneficiaryDetails.getString("city") + ", " + beneficiaryDetails.getString("state") + ", " + beneficiaryDetails.getString("country") + ", " + beneficiaryDetails.getString("zipcode");
            table.addCell(_noBorderCell(beneficiary, darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }
        table.completeRow();

        if (StringUtils.isNotBlank(amendmentsDTO.getOtherAmendments())) {
            JSONObject otherAmendments = new JSONObject(amendmentsDTO.getOtherAmendments());
            for (String key : otherAmendments.keySet()) {
                String value = (String) otherAmendments.get(key);
                table.addCell(_noBorderCell(key, lightFont));
                table.addCell(_noBorderCell(StringUtils.isNotBlank(value) ? value : "NA", darkFont));
                table.completeRow();
            }
        }

        return table;
    }

    public PdfPTable _documentDetails(ReceivedAmendmentsDTO amendmentsDTO) {
        PdfPTable table = new PdfPTable(2);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(_noBorderHeadingCell("Additional Instructions & Documents", darkFont));
        table.addCell(_noBorderCell("", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Message from Bank:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(amendmentsDTO.getMessageFromBank()) ? amendmentsDTO.getMessageFromBank() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Uploaded Files:", lightFont));
        if (StringUtils.isNotBlank(amendmentsDTO.getSupportingDocuments())) {
            JSONArray documents = new JSONArray(amendmentsDTO.getSupportingDocuments());
            String docName = "";
            for (int i = 0; i < documents.length(); i++) {
                docName += documents.getJSONObject(i).getString("documentName") + "\n";
            }
            table.addCell(_noBorderCell(docName, darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }
        table.completeRow();

        return table;
    }

    public PdfPCell _noBorderHeadingCell(String text, Color fontColor) {
        com.lowagie.text.Font font = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 11, com.lowagie.text.Font.BOLD, fontColor);
        Phrase cellText = new Phrase(text, font);
        PdfPCell cell = new PdfPCell();
        cell.addElement(cellText);
        cell.setBorder(0);
        cell.setPaddingTop(16);
        cell.setPaddingBottom(6);
        return cell;
    }

    public PdfPCell _noBorderCell(String text, Color fontColor) {
        com.lowagie.text.Font font = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 10, Font.NORMAL, fontColor);
        Phrase cellText = new Phrase(text, font);
        PdfPCell cell = new PdfPCell();
        cell.addElement(cellText);
        cell.setBorder(0);
        cell.setPaddingTop(6);
        cell.setPaddingBottom(6);
        return cell;
    }

}