/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.GuaranteeLCAmendmentsBackendDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.GuaranteeLCAmendmentsBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants;
import com.temenos.infinity.tradefinanceservices.dto.GuaranteeLCAmendmentsDTO;
import com.temenos.infinity.tradefinanceservices.utils.ExcelBusinessDelegate;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import static com.temenos.infinity.tradefinanceservices.utils.PdfGenerator.*;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.*;

public class GuaranteeLCAmendmentsBusinessDelegateImpl implements ExcelBusinessDelegate, GuaranteeLCAmendmentsBusinessDelegate, TradeFinanceConstants {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    Color lightFont = new Color(114, 114, 114);
    Color darkFont = new Color(66, 66, 66);

    @Override
    public List<GuaranteeLCAmendmentsDTO> getGuaranteeLCAmendments(DataControllerRequest request) {
        GuaranteeLCAmendmentsBackendDelegate requestBackend = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(GuaranteeLCAmendmentsBackendDelegate.class);
        return requestBackend.getGuaranteeLCAmendments(request);
    }

    public List<GuaranteeLCAmendmentsDTO> getList(DataControllerRequest request) {
        GuaranteeLCAmendmentsBackendDelegate requestBackend = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(GuaranteeLCAmendmentsBackendDelegate.class);
        return requestBackend.getGuaranteeLCAmendments(request);
    }

    @Override
    public GuaranteeLCAmendmentsDTO createGuaranteeLCAmendment(GuaranteeLCAmendmentsDTO guaranteeReqPayloadDTO,
                                                               DataControllerRequest request) {
        GuaranteeLCAmendmentsBackendDelegate requestBackend = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(GuaranteeLCAmendmentsBackendDelegate.class);
        return requestBackend.createGuaranteeLCAmendment(guaranteeReqPayloadDTO, request);
    }

    @Override
    public GuaranteeLCAmendmentsDTO getGuaranteeLCAmendmentById(String amendmentSRMSId,
                                                                DataControllerRequest request) {
        GuaranteeLCAmendmentsBackendDelegate requestBackend = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(GuaranteeLCAmendmentsBackendDelegate.class);
        return requestBackend.getGuaranteeLCAmendmentById(amendmentSRMSId, request);
    }

    public GuaranteeLCAmendmentsDTO updateGuaranteeAmendmentByBank(GuaranteeLCAmendmentsDTO guaranteeReqPayloadDTO,
                                                                   DataControllerRequest request) {
        GuaranteeLCAmendmentsBackendDelegate requestBackend = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(GuaranteeLCAmendmentsBackendDelegate.class);
        return requestBackend.updateGuaranteeAmendment(guaranteeReqPayloadDTO, new JSONObject(), request);
    }

    public GuaranteeLCAmendmentsDTO updateGuaranteeAmendment(GuaranteeLCAmendmentsDTO guaranteeReqPayloadDTO, JSONObject inputObj,
                                                             DataControllerRequest request) {
        GuaranteeLCAmendmentsBackendDelegate requestBackend = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(GuaranteeLCAmendmentsBackendDelegate.class);
        return requestBackend.updateGuaranteeAmendment(guaranteeReqPayloadDTO, inputObj, request);
    }

    @Override
    public byte[] generatePdfGuaranteeLcAmendment(JSONObject amendmentData, DataControllerRequest request) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, os);
        addHeadersAndFootersPDF(writer, document, "Issued GT & SBLC - Amendment", true);

        try {
            document.open();

            document.add(addCustomerDetailsPDF(request));
            document.add(_lcSummaryDetails(amendmentData));
            document.add(_amendmentDetails(amendmentData));
            document.add(_amendmentRequestedDetails(amendmentData));
            if (amendmentData.has("SwiftsAndAdvices") && amendmentData.getJSONArray("SwiftsAndAdvices").length() > 0) {
                document.add(_amendmentAdviceDetails(amendmentData.getJSONArray("SwiftsAndAdvices")));
            }
        } catch (Exception e) {
            alert.prepareError("Error occurred while creating pdf. Error: ", e).log();
            return null;
        } finally {
            document.close();
            if (os != null) {
                try {
                    os.close();
                } catch (Exception e) {
                    alert.prepareError("Error occurred while closing input stream. " + e).log();
                }
            }
        }

        return os.toByteArray();
    }

    boolean isEuropeanGeography = com.kony.dbputilities.util.HelperMethods.isEuropieanGeography();
    NumberFormat amountFormat = isEuropeanGeography ? NumberFormat.getInstance(new Locale("da", "DK"))
            : NumberFormat.getInstance(new Locale("en", "US"));

    private PdfPTable _lcSummaryDetails(JSONObject amendmentData) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(_noBorderHeadingCell("LC Summary", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Beneficiary Details:", lightFont));
        table.addCell(_noBorderCell(amendmentData.has("benificiaryName") ? amendmentData.getString("benificiaryName") : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Transaction Ref:", lightFont));
        table.addCell(_noBorderCell(amendmentData.has("guaranteesReference") ? amendmentData.getString("guaranteesReference") : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Product Type:", lightFont));
        table.addCell(_noBorderCell(amendmentData.has("productType") ? amendmentData.getString("productType") : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("GT & SBLC Type:", lightFont));
        table.addCell(_noBorderCell(amendmentData.has("billType") ? amendmentData.getString("billType") : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Amount:", lightFont));
        table.addCell(_noBorderCell(amendmentData.has("amount") ? amendmentData.has("currency") ? amendmentData.getString("currency") + " " + formatAmount(amendmentData.getString("amount")) : formatAmount(amendmentData.getString("amount")) : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Issue Date:", lightFont));
        table.addCell(_noBorderCell(amendmentData.has("issueDate") ? _formatDate(amendmentData.getString("issueDate")) : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Expiry Type:", lightFont));
        table.addCell(_noBorderCell(amendmentData.has("expiryType") ? amendmentData.getString("expiryType") : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Expiry Date:", lightFont));
        table.addCell(_noBorderCell(amendmentData.has("expiryDate") ? _formatDate(amendmentData.getString("expiryDate")) : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Instructing Party:", lightFont));
        table.addCell(_noBorderCell(amendmentData.has("instructingParty") ? amendmentData.getString("instructingParty") : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Applicant Party:", lightFont));
        table.addCell(_noBorderCell(amendmentData.has("applicantParty") ? amendmentData.getString("applicantParty") : "NA", darkFont));
        table.completeRow();
        return table;
    }

    private PdfPTable _amendmentDetails(JSONObject amendmentData) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(_noBorderHeadingCell("Amendment Details", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Status:", lightFont));
        table.addCell(_noBorderCell(amendmentData.has("amendStatus") ? amendmentData.getString("amendStatus") : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Approved Date:", lightFont));
        table.addCell(_noBorderCell(amendmentData.has("approvedDate") ? _formatDate(amendmentData.getString("approvedDate")) : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Amendment No:", lightFont));
        table.addCell(_noBorderCell(amendmentData.has("amendmentNo") ? amendmentData.getString("amendmentNo") : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Requested Date:", lightFont));
        table.addCell(_noBorderCell(amendmentData.has("amendRequestedDate") ? _formatDate(amendmentData.getString("amendRequestedDate")) : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Amendment Reference:", lightFont));
        table.addCell(_noBorderCell(amendmentData.has("amendmentReference") ? amendmentData.getString("amendmentReference") : "NA", darkFont));
        table.completeRow();

        return table;
    }

    private PdfPTable _amendmentRequestedDetails(JSONObject amendmentData) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(_noBorderHeadingCell("Amendment Requested", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Amendment Effective Date:", lightFont));
        table.addCell(_noBorderCell(amendmentData.has("amendmentEffectiveDate") ? _formatDate(amendmentData.getString("amendmentEffectiveDate")) : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Amount:", lightFont));
        table.addCell(_noBorderCell(amendmentData.has("amendAmount") ? amendmentData.has("currency") ? amendmentData.getString("currency") + " " + formatAmount(amendmentData.getString("amendAmount")) : formatAmount(amendmentData.getString("amendAmount")) : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Expiry Type:", lightFont));
        table.addCell(_noBorderCell(amendmentData.has("amendExpiryType") ? amendmentData.getString("amendExpiryType") : "NA", darkFont));
        table.completeRow();

        String respectiveBeneficiaryDetails = "NA";
        try {
            JSONArray beneficiariesList = new JSONArray(amendmentData.getString("beneficiaryDetails"));
            for (int i = 0; i < beneficiariesList.length(); i++) {
                try {
                    JSONObject beneficiaryDetails = new JSONObject(beneficiariesList.get(i).toString());
                    if (beneficiaryDetails.getString("beneficiaryName").equals(amendmentData.getString("benificiaryName"))) {
                        respectiveBeneficiaryDetails = (beneficiaryDetails.getString("beneficiaryName"))
                                + (beneficiaryDetails.has("address1") ? !beneficiaryDetails.getString("address1").isEmpty() ? ", " + beneficiaryDetails.getString("address1") : "" : "")
                                + (beneficiaryDetails.has("address2") ? !beneficiaryDetails.getString("address2").isEmpty() ? ", " + beneficiaryDetails.getString("address2") : "" : "")
                                + (beneficiaryDetails.has("state") ? !beneficiaryDetails.getString("state").isEmpty() ? ", " + beneficiaryDetails.getString("state") : "" : "")
                                + (beneficiaryDetails.has("country") ? !beneficiaryDetails.getString("country").isEmpty() ? ", " + beneficiaryDetails.getString("country") : "" : "")
                                + (beneficiaryDetails.has("zipcode") ? !beneficiaryDetails.getString("zipcode").isEmpty() ? ", " + beneficiaryDetails.getString("zipcode") : "" : "") + ".";
                        break;
                    }
                } catch (JSONException e) {
                    alert.prepareError("Error occurred at validating beneficiary index: ", i).log();
                }
            }
        } catch (Exception e) {
            alert.prepareError("Failed to parse beneficiaryDetails field").log();
        }

        table.addCell(_noBorderCell("Beneficiary Details:", lightFont));
        table.addCell(_noBorderCell(respectiveBeneficiaryDetails, darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Amendment Details:", lightFont));
        table.addCell(_noBorderCell(amendmentData.has("amendDetails") ? amendmentData.getString("amendDetails") : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Message/ Response to Bank:", lightFont));
        table.addCell(_noBorderCell(amendmentData.has("messageToBank") ? amendmentData.getString("messageToBank") : "NA", darkFont));
        table.completeRow();
        return table;
    }

    private PdfPTable _amendmentAdviceDetails(JSONArray swiftsAndAdvices) {
        PdfPTable superTable = new PdfPTable(1);
        superTable.setHorizontalAlignment(Element.ALIGN_LEFT);
        superTable.addCell(_noBorderHeadingCell("Amendment Advice", darkFont));
        superTable.completeRow();

        PdfPTable innerTable = new PdfPTable(4);
        innerTable.setHorizontalAlignment(Element.ALIGN_LEFT);
        innerTable.addCell(_borderHeadingCell("Advice Name", darkFont));
        innerTable.addCell(_borderHeadingCell("Date", darkFont));
        innerTable.addCell(_borderHeadingCell("Advice Party", darkFont));
        innerTable.addCell(_borderHeadingCell("Message", darkFont));
        innerTable.completeRow();

        for (int i = 0; i < swiftsAndAdvices.length(); i++) {
            JSONObject swiftsAdvise = swiftsAndAdvices.getJSONObject(i);
            innerTable.addCell(_borderCell(swiftsAdvise.has("adviceName") ? swiftsAdvise.getString("adviceName") : "NA", darkFont));
            innerTable.addCell(_borderCell(swiftsAdvise.has("messageDate") ? swiftsAdvise.getString("messageDate") : "NA", darkFont));
            innerTable.addCell(_borderCell(swiftsAdvise.has("receiver") ? swiftsAdvise.getString("receiver") : "NA", darkFont));
            innerTable.addCell(_borderCell(swiftsAdvise.has("message") ? swiftsAdvise.getString("message") : "NA", darkFont));
            innerTable.completeRow();
        }
        innerTable.completeRow();
        PdfPCell finalCell = new PdfPCell();
        finalCell.addElement(innerTable);
        finalCell.setBorder(Rectangle.NO_BORDER);
        finalCell.setPaddingBottom(10);
        superTable.addCell(finalCell);
        return superTable;
    }

}
