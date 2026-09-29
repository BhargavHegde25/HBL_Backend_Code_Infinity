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
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.ExportLCAmendmentBackendDelegate;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.GetExportLetterOfCreditsByIdBackendDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.InitiateDownloadExportLCAmendmentsBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.dto.ExportLCAmendmentsDTO;
import com.temenos.infinity.tradefinanceservices.dto.ExportLOCDTO;
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

public class InitiateDownloadExportLCAmendmentsBusinessDelegateImpl implements InitiateDownloadExportLCAmendmentsBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public byte[] generateExportLCAmendmentsPdf(String srmsRequestId, DataControllerRequest request) {
        ExportLCAmendmentBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(ExportLCAmendmentBackendDelegate.class);
        ExportLCAmendmentsDTO letterOfCredits = null;
        try {
            letterOfCredits = backendDelegate.getExportLCAmendmentById(srmsRequestId, request);
            if (StringUtils.isNotBlank(letterOfCredits.getErrorMessage()) || StringUtils.isNotBlank(letterOfCredits.getErrorCode())) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

        //Call GetExportLCby id to get values
        GetExportLetterOfCreditsByIdBackendDelegate exportbyidbackend = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(GetExportLetterOfCreditsByIdBackendDelegate.class);
        ExportLOCDTO exportlcdto = null;
        try {
            exportlcdto = exportbyidbackend.getExportLetterOfCreditById(letterOfCredits.getExportlcSRMSRequestId(), request);
            if (StringUtils.isNotBlank(letterOfCredits.getErrorMessage()) || StringUtils.isNotBlank(letterOfCredits.getErrorCode())) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outStream);
        addHeadersAndFootersPDF(writer, document, "Export - Amendment", true);

        try {
            document.open();

            document.add(addCustomerDetailsPDF(request));
            document.add(_lcSummary(exportlcdto));
            document.add(_amendmentDetails(letterOfCredits, request));
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

    //Adding LC Data from ExportLC
    public PdfPTable _lcSummary(ExportLOCDTO exportlcdto) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidthPercentage(100);
        table.addCell(_noBorderHeadingCell("LC Summary", darkFont));

        table.addCell(_noBorderCell("Beneficiary:", lightFont));
        if (StringUtils.isNotBlank(exportlcdto.getBeneficiaryName())) {
            table.addCell(_noBorderCell(exportlcdto.getBeneficiaryName(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Reference Number:", lightFont));
        if (StringUtils.isNotBlank(exportlcdto.getLcReferenceNo())) {
            table.addCell(_noBorderCell(exportlcdto.getLcReferenceNo(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Credit Amount:", lightFont));
        if (StringUtils.isNotBlank(String.valueOf(exportlcdto.getAmount()))
                && StringUtils.isNotBlank(exportlcdto.getCurrency())) {
            table.addCell(_noBorderCell(getCurrencySymbol(exportlcdto.getCurrency()) + " "
                    + formatAmount(String.valueOf(exportlcdto.getAmount())), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Issue Date:", lightFont));
        if (StringUtils.isNotBlank(exportlcdto.getIssueDate())) {
            table.addCell(_noBorderCell(_formatDate(exportlcdto.getIssueDate()), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Expiry Date:", lightFont));
        if (StringUtils.isNotBlank(exportlcdto.getExpiryDate())) {
            table.addCell(_noBorderCell(_formatDate(exportlcdto.getExpiryDate()), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Payment Terms:", lightFont));
        if (StringUtils.isNotBlank(exportlcdto.getPaymentTerms())) {
            table.addCell(_noBorderCell(exportlcdto.getPaymentTerms(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        return table;
    }

    //Adding Amendments Details in the PdfPTable
    public PdfPTable _amendmentDetails(ExportLCAmendmentsDTO amendmentDto, DataControllerRequest request) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(_noBorderHeadingCell("Amendment Details", darkFont));

        table.addCell(_noBorderCell("Amendment Reference:", lightFont));
        if (StringUtils.isNotBlank(amendmentDto.getAmendmentReferenceNo())) {
            table.addCell(_noBorderCell(amendmentDto.getAmendmentReferenceNo(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }


        table.addCell(_noBorderCell("Amendment No:", lightFont));
        if (StringUtils.isNotBlank(amendmentDto.getAmendmentNo())) {
            table.addCell(_noBorderCell((amendmentDto.getAmendmentNo()), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }


        table.addCell(_noBorderCell("Received On:", lightFont));
        if (StringUtils.isNotBlank(amendmentDto.getAmendmentReceivedDate())) {
            table.addCell(_noBorderCell(_formatDate(amendmentDto.getAmendmentReceivedDate()), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Amendment Status:", lightFont));
        if (StringUtils.isNotBlank(amendmentDto.getAmendmentStatus())) {
            table.addCell(_noBorderCell(amendmentDto.getAmendmentStatus(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Acceptance/Rejecton Date:", lightFont));
        if (StringUtils.isNotBlank(amendmentDto.getSelfAcceptanceDate())) {
            table.addCell(_noBorderCell(_formatDate(amendmentDto.getSelfAcceptanceDate()), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Self Acceptance:", lightFont));
        if (StringUtils.isNotBlank(amendmentDto.getSelfAcceptance())) {
            table.addCell(_noBorderCell(amendmentDto.getSelfAcceptance(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }


        return table;
    }

    //Adding Amendment Details using ExportLCAmendmentsDTO
    public PdfPTable _amendmentRequestedDetails(ExportLCAmendmentsDTO amendDto) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(_noBorderHeadingCell("Amendments Requested", darkFont));

        table.addCell(_noBorderCell("Expiry Date:", lightFont));
        if (StringUtils.isNotBlank(amendDto.getLcExpiryDate())) {
            table.addCell(_noBorderCell(_formatDate(amendDto.getLcExpiryDate()), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Latest Shipment Date:", lightFont));
        if (StringUtils.isNotBlank(amendDto.getLatestShipmentDate())) {
            table.addCell(_noBorderCell(_formatDate(amendDto.getLatestShipmentDate()), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Period Of Presentation:", lightFont));
        if (StringUtils.isNotBlank(amendDto.getPeriodOfPresentation())) {
            table.addCell(_noBorderCell(amendDto.getPeriodOfPresentation(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("LC Amount:", lightFont));
        if (StringUtils.isNotBlank(String.valueOf(amendDto.getNewLcAmount()))) {
            //table.addCell(_noBorderCell(amendDto.getNewLcAmount(), darkFont));
            if (Double.parseDouble(amendDto.getOldLcAmount()) <= Double.parseDouble(amendDto.getNewLcAmount()))
                table.addCell(_noBorderCell(getCurrencySymbol(amendDto.getLcCurrency()) + " " + amendDto.getNewLcAmount() + "\n" + "LC Amount is increased by " + getCurrencySymbol(amendDto.getLcCurrency()) + " " + formatAmount(String.valueOf(amendDto.getNewLcAmount())), darkFont));
            else
                table.addCell(_noBorderCell("LC Amount is decreased by \n" + getCurrencySymbol(amendDto.getLcCurrency()) + " " + formatAmount(String.valueOf(amendDto.getNewLcAmount())), darkFont));

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


    public PdfPTable _amendmentCharges(ExportLCAmendmentsDTO amendDto, DataControllerRequest request) {
        AccountBusinessDelegate actDelegate = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(AccountBusinessDelegate.class);
        Map<String, Object> customer = CustomerSession.getCustomerMap(request);
        String customerId = CustomerSession.getCustomerId(customer);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(_noBorderHeadingCell("Amendment Charges", darkFont));

        table.addCell(_noBorderCell("Charges will be paid by:", lightFont));
        if (StringUtils.isNotBlank(amendDto.getAmendmentChargesPayer())) {
            table.addCell(_noBorderCell(amendDto.getAmendmentChargesPayer(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Charges Debit Account:", lightFont));
        if (StringUtils.isNotBlank(amendDto.getChargesDebitAccount())) {
            String acc_no = amendDto.getChargesDebitAccount();
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

        return table;
    }

}

