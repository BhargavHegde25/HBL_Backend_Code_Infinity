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
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.LetterOfCreditDrawingsBackendDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.LetterOfCreditDrawingsBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants;
import com.temenos.infinity.tradefinanceservices.dto.DrawingsDTO;
import com.temenos.infinity.tradefinanceservices.utils.ExcelBusinessDelegate;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

import static com.temenos.infinity.tradefinanceservices.utils.PdfGenerator.*;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.*;

public class LetterOfCreditDrawingsBusinessDelegateImpl
        implements LetterOfCreditDrawingsBusinessDelegate, ExcelBusinessDelegate, TradeFinanceConstants {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public DrawingsDTO getImportDrawingDetailsById(DataControllerRequest request, String srmsRequestOrderId)
            throws ApplicationException, com.kony.dbp.exception.ApplicationException {
        LetterOfCreditDrawingsBackendDelegate drawingDetails = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(LetterOfCreditDrawingsBackendDelegate.class);
        DrawingsDTO drawingDetailsResponse = drawingDetails.getImportDrawingDetailsById(request, srmsRequestOrderId);
        return drawingDetailsResponse;
    }

    @Override
    public List<DrawingsDTO> getImportDrawings(DrawingsDTO drawingsDTO, DataControllerRequest request)
            throws ApplicationException, com.kony.dbp.exception.ApplicationException {
        LetterOfCreditDrawingsBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(LetterOfCreditDrawingsBackendDelegate.class);
        List<DrawingsDTO> drawings = orderBackendDelegate.getImportDrawingsFromSRMS(drawingsDTO, request);
        return drawings;
    }

    @Override
    public List<DrawingsDTO> getList(DataControllerRequest request) throws ApplicationException {
        LetterOfCreditDrawingsBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(LetterOfCreditDrawingsBackendDelegate.class);
        try {
            return orderBackendDelegate.getImportDrawingsFromSRMS(null, request);
        } catch (com.kony.dbp.exception.ApplicationException e) {
            alert.prepareError("Runtime Exception").log();
        }
        return null;
    }

    @Override
    public DrawingsDTO createDrawings(DrawingsDTO drawings, DataControllerRequest request)
            throws ApplicationException, com.kony.dbp.exception.ApplicationException {
        LetterOfCreditDrawingsBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(LetterOfCreditDrawingsBackendDelegate.class);
        drawings = orderBackendDelegate.createDrawingsOrder(drawings, request);
        return drawings;
    }

    @Override
    public DrawingsDTO updateDrawings(DrawingsDTO drawings, DataControllerRequest request)
            throws ApplicationException, com.kony.dbp.exception.ApplicationException {
        LetterOfCreditDrawingsBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(LetterOfCreditDrawingsBackendDelegate.class);
        drawings = orderBackendDelegate.updateDrawingsOrder(drawings, request);
        return drawings;
    }

    @Override
    public byte[] generateImportDrawingPdf(DrawingsDTO drawingDetails, DataControllerRequest request) {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outStream);
        addHeadersAndFootersPDF(writer, document, "Import - Drawing", true);

        try {
            document.open();

            document.add(addCustomerDetailsPDF(request));
            document.add(_lcSummary(drawingDetails));
            document.add(_drawingDetails(drawingDetails));
            document.add(_documentStatus(drawingDetails));
            String paymentStatus = StringUtils.isNotBlank(drawingDetails.getPaymentStatus())
                    ? drawingDetails.getPaymentStatus()
                    : "";
            document.add(_paymentDetails(drawingDetails, paymentStatus, request));
        } catch (Exception e) {
            alert.prepareError("Error occurred while creating pdf ", e).log();
            return null;
        } finally {
            document.close();
        }
        return outStream.toByteArray();
    }

    public PdfPTable _lcSummary(DrawingsDTO drawingDetails) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(_noBorderHeadingCell("LC Summary", darkFont));

        table.addCell(_noBorderCell("Beneficiary:", lightFont));
        if (StringUtils.isNotBlank(drawingDetails.getBeneficiaryName())) {
            table.addCell(_noBorderCell(drawingDetails.getBeneficiaryName(), darkFont));
        }

        table.addCell(_noBorderCell("Reference Number:", lightFont));
        if (StringUtils.isNotBlank(drawingDetails.getLcReferenceNo())) {
            table.addCell(_noBorderCell(drawingDetails.getLcReferenceNo(), darkFont));
        }

        table.addCell(_noBorderCell("Credit Amount:", lightFont));
        if (StringUtils.isNotBlank(drawingDetails.getLcAmount())) {
            String lcAmount = drawingDetails.getLcAmount();
            if (StringUtils.isNotBlank(drawingDetails.getLcCurrency())) {
                lcAmount = getCurrencySymbol(drawingDetails.getLcCurrency()) + " " + formatAmount(lcAmount);
            }
            table.addCell(_noBorderCell(lcAmount, darkFont));
        }

        table.addCell(_noBorderCell("Issue Date:", lightFont));
        if (StringUtils.isNotBlank(drawingDetails.getLcIssueDate())) {
            table.addCell(_noBorderCell(_formatDate(drawingDetails.getLcIssueDate()), darkFont));
        }

        table.addCell(_noBorderCell("Expiry Date:", lightFont));
        if (StringUtils.isNotBlank(drawingDetails.getLcExpiryDate())) {
            table.addCell(_noBorderCell(_formatDate(drawingDetails.getLcExpiryDate()), darkFont));
        }

        table.addCell(_noBorderCell("Payment Terms:", lightFont));
        if (StringUtils.isNotBlank(drawingDetails.getLcType())) {
            table.addCell(_noBorderCell(drawingDetails.getLcType(), darkFont));
        }


        return table;
    }

    public PdfPTable _drawingDetails(DrawingsDTO drawingDetails) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(_noBorderHeadingCell("Drawing Details", darkFont));

        table.addCell(_noBorderCell("Drawing Amount:", lightFont));
        if (StringUtils.isNotBlank(drawingDetails.getDrawingAmount())) {
            String drawingAmount = drawingDetails.getDrawingAmount();
            if (StringUtils.isNotBlank(drawingDetails.getDrawingCurrency())) {
                drawingAmount = getCurrencySymbol(drawingDetails.getDrawingCurrency()) + " "
                        + formatAmount(drawingAmount);
            }
            table.addCell(_noBorderCell(drawingAmount, darkFont));
        }

        table.addCell(_noBorderCell("Creation Date:", lightFont));
        if (StringUtils.isNotBlank(drawingDetails.getDrawingCreationDate())) {
            table.addCell(_noBorderCell(_formatDate(drawingDetails.getDrawingCreationDate()), darkFont));
        }

        table.addCell(_noBorderCell("Document Status:", lightFont));
        if (StringUtils.isNotBlank(drawingDetails.getDocumentStatus())) {
            table.addCell(_noBorderCell(drawingDetails.getDocumentStatus(), darkFont));
        }

        table.addCell(_noBorderCell("Presenter:", lightFont));
        if (StringUtils.isNotBlank(drawingDetails.getPresentorName())) {
            table.addCell(_noBorderCell(drawingDetails.getPresentorName(), darkFont));
        }

        table.addCell(_noBorderCell("Presenter Reference:", lightFont));
        if (StringUtils.isNotBlank(drawingDetails.getPresentorReference())) {
            table.addCell(_noBorderCell(drawingDetails.getPresentorReference(), darkFont));
        }

        table.addCell(_noBorderCell("Forward Contract:", lightFont));
        if (StringUtils.isNotBlank(drawingDetails.getForwardContact())) {
            table.addCell(_noBorderCell(drawingDetails.getForwardContact(), darkFont));
        }
        table.addCell(_noBorderCell("Shipping Guarantee Reference:", lightFont));
        if (StringUtils.isNotBlank(drawingDetails.getShippingGuaranteeReference())) {
            table.addCell(_noBorderCell(drawingDetails.getShippingGuaranteeReference(), darkFont));
        }
        table.addCell(_noBorderCell("Message From Bank:", lightFont));
        if (StringUtils.isNotBlank(drawingDetails.getMessageFromBank())) {
            table.addCell(_noBorderCell(drawingDetails.getMessageFromBank(), darkFont));
        }


        return table;
    }

    public PdfPTable _documentStatus(DrawingsDTO drawingDetails) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        ;
        table.addCell(_noBorderHeadingCell("Document Status", darkFont));

        table.addCell(_noBorderCell("Total Documents:", lightFont));
        if (StringUtils.isNotBlank(drawingDetails.getTotalDocuments())) {
            table.addCell(_noBorderCell(drawingDetails.getTotalDocuments(), darkFont));
        }

        table.addCell(_noBorderCell("Document Name:", lightFont));
        if (StringUtils.isNotBlank(drawingDetails.getDocumentName())) {
            String documents[] = drawingDetails.getDocumentName().split("\\|\\|");
            for (int i = 0; i < documents.length; i++) {
                if (StringUtils.isNotBlank(documents[i])) {
                    table.addCell(_noBorderCell(documents[i].trim(), darkFont));
                    if (i != documents.length - 1) {

                        table.addCell(_noBorderCell("", darkFont));
                    }
                }
            }
        }

        table.addCell(_noBorderCell("Document Status:", lightFont));
        if (StringUtils.isNotBlank(drawingDetails.getDocumentStatus())) {
            table.addCell(_noBorderCell(drawingDetails.getDocumentStatus(), darkFont));
        }

        table.addCell(_noBorderCell("Discrepancies:", lightFont));
        if (StringUtils.isNotBlank(drawingDetails.getDiscrepancies())) {
            String discrepancies[] = drawingDetails.getDiscrepancies().split("\\|\\|");
            for (int i = 0; i < discrepancies.length; i++) {
                if (StringUtils.isNotBlank(discrepancies[i])) {
                    table.addCell(_noBorderCell(discrepancies[i].trim(), darkFont));
                    if (i != discrepancies.length - 1) {

                        table.addCell(_noBorderCell("", darkFont));
                    }
                }
            }
        }

        table.addCell(_noBorderCell("Discrepancies Acceptance:", lightFont));
        String acceptance = "NA";
        if (StringUtils.isNotBlank(drawingDetails.getStatus())
                && StringUtils.isNotBlank(drawingDetails.getAcceptance())) {
            acceptance = drawingDetails.getStatus().equalsIgnoreCase(PARAM_STATUS_NEW) ? "NA"
                    : drawingDetails.getAcceptance();
        }
        table.addCell(_noBorderCell(acceptance, darkFont));


        return table;
    }

    public PdfPTable _paymentDetails(DrawingsDTO drawingDetails, String paymentStatus, DataControllerRequest request) {
        boolean newAccount = paymentStatus.equalsIgnoreCase(PARAM_STATUS_NEW);
        boolean pendingAccount = paymentStatus.equalsIgnoreCase(PARAM_STATUS_PENDING_WITH_BANK);
        boolean settledAccount = paymentStatus.equalsIgnoreCase(PARAM_STATUS_SETTLED);
        boolean rejectedAccount = paymentStatus.equalsIgnoreCase(REJECTED_BY_BANK);
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        ;
        table.addCell(_noBorderHeadingCell("Payment Details", darkFont));

        if (settledAccount || pendingAccount || rejectedAccount) {
            table.addCell(_noBorderCell("Payment Status:", lightFont));
            table.addCell(_noBorderCell(paymentStatus, darkFont));

        }
        if (settledAccount) {
            table.addCell(_noBorderCell("Payment Date:", lightFont));
            if (StringUtils.isNotBlank(drawingDetails.getPaymentDate())) {
                table.addCell(_noBorderCell(_formatDate(drawingDetails.getPaymentDate()), darkFont));
            }

        }
        if (rejectedAccount) {
            table.addCell(_noBorderCell("Rejected Date:", lightFont));
            if (StringUtils.isNotBlank(drawingDetails.getRejectedDate())) {
                table.addCell(_noBorderCell(_formatDate(drawingDetails.getRejectedDate()), darkFont));
            }

        }
        table.addCell(_noBorderCell("Total Amount to be Paid:", lightFont));
        if (StringUtils.isNotBlank(drawingDetails.getTotalAmountToBePaid())) {
            String payAmount = drawingDetails.getTotalAmountToBePaid();
            if (StringUtils.isNotBlank(drawingDetails.getDrawingAmount())) {
                payAmount = getCurrencySymbol(drawingDetails.getDrawingCurrency()) + " " + formatAmount(payAmount);
            }
            table.addCell(_noBorderCell(payAmount, darkFont));
        }

        table.addCell(_noBorderCell("Amount to be debited from:", lightFont));
        String maskedAccount = "NA";
        if (!newAccount && StringUtils.isNotBlank(drawingDetails.getAccountToBeDebited())) {
            String accountToBeDebited = drawingDetails.getAccountToBeDebited();
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

        table.addCell(_noBorderCell("Your message to bank:", lightFont));
        String messageToBank = "NA";
        if (!newAccount && StringUtils.isNotBlank(drawingDetails.getMessageToBank())) {
            messageToBank = drawingDetails.getMessageToBank();
        }
        table.addCell(_noBorderCell(messageToBank, darkFont));


        return table;
    }
}
