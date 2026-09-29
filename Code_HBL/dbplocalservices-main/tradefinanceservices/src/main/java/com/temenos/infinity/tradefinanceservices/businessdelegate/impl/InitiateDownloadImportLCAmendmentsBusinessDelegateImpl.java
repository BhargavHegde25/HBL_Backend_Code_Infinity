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
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.GetAmendmentsLetterOfCreditsBackendDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.InitiateDownloadImportLCAmendmentsBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.dto.LetterOfCreditsAmendmentDTO;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.darkFont;
import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.lightFont;
import static com.temenos.infinity.tradefinanceservices.utils.PdfGenerator.*;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.*;

public class InitiateDownloadImportLCAmendmentsBusinessDelegateImpl
        implements InitiateDownloadImportLCAmendmentsBusinessDelegate {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    public byte[] generateExportDrawingPdf(String srmsRequestId, DataControllerRequest request) {
        GetAmendmentsLetterOfCreditsBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(GetAmendmentsLetterOfCreditsBackendDelegate.class);
        LetterOfCreditsAmendmentDTO letterOfCredits;
        try {
            letterOfCredits = backendDelegate.getAmendmentsById(srmsRequestId, request);
            if (StringUtils.isNotBlank(letterOfCredits.getErrorMessage()) || StringUtils.isNotBlank(letterOfCredits.getErrorCode())) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outStream);
        addHeadersAndFootersPDF(writer, document, "Import - Amendment", true);

        try {
            document.open();

            document.add(addCustomerDetailsPDF(request));
            document.add(_lcSummary(letterOfCredits));
            document.add(_amendmentDetails(letterOfCredits));
            document.add(_amendmentRequestedDetails(letterOfCredits));
            document.add(_amendmentCharges(letterOfCredits, request));
        } catch (Exception e) {
            alert.prepareError("Error occurred while creating pdf ", e).log();
            return null;
        } finally {
            document.close();
        }
        return outStream.toByteArray();
    }

    public PdfPTable _lcSummary(LetterOfCreditsAmendmentDTO amendmentDto) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(_noBorderHeadingCell("LC Summary", darkFont));

        table.addCell(_noBorderCell("Beneficiary:", lightFont));
        if (StringUtils.isNotBlank(amendmentDto.getBeneficiaryName())) {
            table.addCell(_noBorderCell(amendmentDto.getBeneficiaryName(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Reference Number:", lightFont));
        if (StringUtils.isNotBlank(amendmentDto.getLcReferenceNo())) {
            table.addCell(_noBorderCell(amendmentDto.getLcReferenceNo(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Credit Amount:", lightFont));
        if (StringUtils.isNotBlank(String.valueOf(amendmentDto.getLcAmount()))
                && StringUtils.isNotBlank(amendmentDto.getLcCurrency())) {
            table.addCell(_noBorderCell(getCurrencySymbol(amendmentDto.getLcCurrency()) + " "
                    + formatAmount(String.valueOf(amendmentDto.getLcAmount())), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Issue Date:", lightFont));
        if (StringUtils.isNotBlank(amendmentDto.getIssueDate())) {
            table.addCell(_noBorderCell(_formatDate(amendmentDto.getIssueDate()), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Expiry Date:", lightFont));
        if (StringUtils.isNotBlank(amendmentDto.getExpiryDate())) {
            table.addCell(_noBorderCell(_formatDate(amendmentDto.getExpiryDate()), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Payment Terms:", lightFont));
        if (StringUtils.isNotBlank(amendmentDto.getPaymentTerms())) {
            table.addCell(_noBorderCell(amendmentDto.getPaymentTerms(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }


        return table;
    }

    public PdfPTable _amendmentDetails(LetterOfCreditsAmendmentDTO amendmentDto) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(_noBorderHeadingCell("Amendment Details", darkFont));

        table.addCell(_noBorderCell("Amendment Reference:", lightFont));
        if (StringUtils.isNotBlank(amendmentDto.getAmendmentReference())) {
            table.addCell(_noBorderCell(amendmentDto.getAmendmentReference(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Amended On:", lightFont));
        if (StringUtils.isNotBlank(amendmentDto.getAmendmentDate())) {
            table.addCell(_noBorderCell(_formatDate(amendmentDto.getAmendmentDate()), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Amendment Status:", lightFont));
        if (StringUtils.isNotBlank(amendmentDto.getAmendStatus())) {
            table.addCell(_noBorderCell(amendmentDto.getAmendStatus(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Approved On:", lightFont));
        if (StringUtils.isNotBlank(amendmentDto.getAmendmentApprovedDate())) {
            table.addCell(_noBorderCell(_formatDate(amendmentDto.getAmendmentApprovedDate()), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }


        return table;
    }

    public PdfPTable _amendmentRequestedDetails(LetterOfCreditsAmendmentDTO amendDto) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.addCell(_noBorderHeadingCell("Amendments Requested", darkFont));

        table.addCell(_noBorderCell("Expiry Date:", lightFont));
        if (StringUtils.isNotBlank(amendDto.getAmendmentExpiryDate())) {
            table.addCell(_noBorderCell(_formatDate(amendDto.getAmendmentExpiryDate()), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Latest Shipment Date:", lightFont));
        if (StringUtils.isNotBlank(amendDto.getLatestShippingDate())) {
            table.addCell(_noBorderCell(_formatDate(amendDto.getLatestShippingDate()), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Period Of Presentation:", lightFont));
        if (StringUtils.isNotBlank(amendDto.getPresentationPeriod())) {
            table.addCell(_noBorderCell(amendDto.getPresentationPeriod(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("LC Amount:", lightFont));
        if (StringUtils.isNotBlank(String.valueOf(amendDto.getLcAmount()))) {
            if (Double.parseDouble(amendDto.getCreditAmount()) <= amendDto.getLcAmount())
                table.addCell(_noBorderCell("Increase to \n" + getCurrencySymbol(amendDto.getLcCurrency()) + " " + formatAmount(String.valueOf(amendDto.getLcAmount())), darkFont));
            else
                table.addCell(_noBorderCell("Decrease to \n" + getCurrencySymbol(amendDto.getLcCurrency()) + " " + formatAmount(String.valueOf(amendDto.getLcAmount())), darkFont));

        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Other Amendments:", lightFont));
        if (StringUtils.isNotBlank(amendDto.getOtherAmendments())) {
            table.addCell(_noBorderCell(amendDto.getOtherAmendments(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }


        return table;
    }

    public PdfPTable _amendmentCharges(LetterOfCreditsAmendmentDTO amendDto, DataControllerRequest request) {
        AccountBusinessDelegate actDelegate = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(AccountBusinessDelegate.class);
        Map<String, Object> customer = CustomerSession.getCustomerMap(request);
        String customerId = CustomerSession.getCustomerId(customer);
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.addCell(_noBorderHeadingCell("Amendment Charges", darkFont));

        table.addCell(_noBorderCell("Charges will be paid by:", lightFont));
        if (StringUtils.isNotBlank(amendDto.getAmendCharges())) {
            table.addCell(_noBorderCell(amendDto.getAmendCharges(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Charges Debit Account:", lightFont));
        if (StringUtils.isNotBlank(amendDto.getChargesAccount())) {
            String acc_no = amendDto.getChargesAccount();
            CustomerAccountsDTO account = actDelegate.getAccountDetails(customerId, acc_no);
            String acc_name = ((account != null) && (StringUtils.isNotBlank(account.getAccountName())))
                    ? account.getAccountName()
                    : "";
            String maskedAccNo = "X" + acc_no.substring(acc_no.length() - 4);
            String accountName = StringUtils.join(acc_name, "....-", maskedAccNo);
            table.addCell(_noBorderCell(accountName, darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Charges Paid:", lightFont));
        if (StringUtils.isNotBlank(amendDto.getChargesPaid())) {
            table.addCell(_noBorderCell(getCurrencySymbol(amendDto.getLcCurrency()) + " " + formatAmount(amendDto.getChargesPaid()), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        return table;
    }
}
