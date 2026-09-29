/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.temenos.dbx.product.commons.businessdelegate.api.AccountBusinessDelegate;
import com.temenos.dbx.product.commons.dto.CustomerAccountsDTO;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.InwardCollectionsBackendDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.InwardCollectionsBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.dto.InwardCollectionsDTO;
import com.temenos.infinity.tradefinanceservices.utils.ExcelBusinessDelegate;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.darkFont;
import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.lightFont;
import static com.temenos.infinity.tradefinanceservices.utils.PdfGenerator.*;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.*;

public class InwardCollectionsBusinessDelegateImpl implements InwardCollectionsBusinessDelegate, ExcelBusinessDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private final InwardCollectionsBackendDelegate requestBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(InwardCollectionsBackendDelegate.class);
    private final AccountBusinessDelegate accountBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AccountBusinessDelegate.class);

    @Override
    public InwardCollectionsDTO createInwardCollection(InwardCollectionsDTO inputDTO, DataControllerRequest request) {
        return requestBackendDelegate.createInwardCollection(inputDTO, request);
    }

    @Override
    public List<InwardCollectionsDTO> getInwardCollections(DataControllerRequest request) {
        return requestBackendDelegate.getInwardCollections(request);
    }

    @Override
    public InwardCollectionsDTO getInwardCollectionById(String collectionSrmsId, DataControllerRequest request) {
        return requestBackendDelegate.getInwardCollectionById(collectionSrmsId, request);
    }

    @Override
    public InwardCollectionsDTO updateInwardCollection(InwardCollectionsDTO amendmentDetails, DataControllerRequest request) {
        return requestBackendDelegate.updateInwardCollection(amendmentDetails, request);
    }

    @Override
    public List<InwardCollectionsDTO> getList(DataControllerRequest request) throws ApplicationException {
        return requestBackendDelegate.getInwardCollections(request);
    }

    public byte[] generateInwardCollection(InwardCollectionsDTO inputDTO, DataControllerRequest request) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outStream);
        addHeadersAndFootersPDF(writer, document, "Inward Collection", true);
        try {
            document.open();
            document.add(addCustomerDetailsPDF(request));
            document.add(_collectionOverview(inputDTO));
            if (StringUtils.isNotBlank(inputDTO.getStatus()) && !inputDTO.getStatus().equals(PARAM_STATUS_NEW) && !inputDTO.getStatus().equals(PARAM_STATUS_RETURNED_by_BANK)) {
                document.add(_draweeConsent(inputDTO, request));
            }
            document.add(_collectionDetails(inputDTO));
            document.close();
            return outStream.toByteArray();
        } catch (Exception e) {
            alert.prepareError("Error occurred while creating pdf ", e).log();
            return null;
        } finally {
            document.close();
        }
    }

    public PdfPTable _collectionOverview(InwardCollectionsDTO inputDTO) {
        PdfPTable table = new PdfPTable(2);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(_noBorderHeadingCell("Collection Overview", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Transaction Ref:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(inputDTO.getCollectionSrmsId()) ? inputDTO.getCollectionSrmsId() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Document Number:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(inputDTO.getDocumentNo()) ? inputDTO.getDocumentNo() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Received On:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(inputDTO.getReceivedOn()) ? _formatDate(inputDTO.getReceivedOn()) : "NA", darkFont));
        table.completeRow();

        String status = inputDTO.getStatus();
        table.addCell(_noBorderCell("Status:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(status) ? status : "NA", darkFont));
        table.completeRow();

        if (StringUtils.isNotBlank(status) && (status.equals(PARAM_STATUS_NEW) || status.equals(PARAM_STATUS_RETURNED_by_BANK))) {
            table.addCell(_noBorderCell("Drawee Acknowledgement:", lightFont));
            table.addCell(_noBorderCell(StringUtils.isNotBlank(inputDTO.getDraweeAcknowledgement()) ? inputDTO.getDraweeAcknowledgement() : "NA", darkFont));
            table.completeRow();

            if (StringUtils.isNotBlank(inputDTO.getTenorType()) && inputDTO.getTenorType().equals("Usance")) {
                table.addCell(_noBorderCell("Usance Acceptance Eligible:", lightFont));
                table.addCell(_noBorderCell(StringUtils.isNotBlank(inputDTO.getUsanceAcceptanceEligibility()) ? inputDTO.getUsanceAcceptanceEligibility() : "NA", darkFont));
                table.completeRow();
            }
            if (status.equals(PARAM_STATUS_RETURNED_by_BANK)) {
                table.addCell(_noBorderCell("Documents against Acceptance:", lightFont));
                table.addCell(_noBorderCell(StringUtils.isNotBlank(inputDTO.getDocumentsAgainstAcceptance()) ? inputDTO.getDocumentsAgainstAcceptance() : "NA", darkFont));
                table.completeRow();
            }
        }

        return table;
    }

    public PdfPTable _draweeConsent(InwardCollectionsDTO inputDTO, DataControllerRequest request) {
        Map<String, Object> customer = CustomerSession.getCustomerMap(request);
        String customerId = CustomerSession.getCustomerId(customer);
        String status = inputDTO.getStatus();

        PdfPTable table = new PdfPTable(2);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(_noBorderHeadingCell("Drawee Consent", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Drawee Acknowledgement:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(inputDTO.getDraweeAcknowledgement()) ? inputDTO.getDraweeAcknowledgement() : "NA", darkFont));
        table.completeRow();

        if (!status.equals(PARAM_STATUS_SETTLED) && !status.equals(PARAM_STATUS_REJECTED)) {
            if (StringUtils.isNotBlank(inputDTO.getTenorType()) && inputDTO.getTenorType().equals("Usance")) {
                table.addCell(_noBorderCell("Usance Acceptance:", lightFont));
                table.addCell(_noBorderCell(StringUtils.isNotBlank(inputDTO.getUsanceAcceptance()) ? inputDTO.getUsanceAcceptance() : "NA", darkFont));
                table.completeRow();

                table.addCell(_noBorderCell("Usance Acceptance Date:", lightFont));
                table.addCell(_noBorderCell(StringUtils.isNotBlank(inputDTO.getUsanceAcceptanceDate()) ? inputDTO.getUsanceAcceptanceDate() : "NA", darkFont));
                table.completeRow();
            }

            if (StringUtils.isNotBlank(inputDTO.getBillExchangeStatus())) {
                table.addCell(_noBorderCell("Bill of Exchange Status:", lightFont));
                table.addCell(_noBorderCell(StringUtils.isNotBlank(inputDTO.getBillExchangeStatus()) ? inputDTO.getBillExchangeStatus() : "NA", darkFont));
                table.completeRow();
            }
        }

        if (!status.equals(PARAM_STATUS_REJECTED)) {
            table.addCell(_noBorderCell("Amount debit from:", lightFont));
            String maskedAmountDebitAccount = "NA";
            if (StringUtils.isNotBlank(inputDTO.getDebitAmountFrom())) {
                String amountDebitAccount = inputDTO.getDebitAmountFrom();
                CustomerAccountsDTO account = accountBusinessDelegate.getAccountDetails(customerId, amountDebitAccount);
                String accountType = ((account != null) && StringUtils.isNotBlank(account.getAccountType())) ? (account.getAccountType() + "...") : "";
                maskedAmountDebitAccount = accountType + amountDebitAccount.substring(amountDebitAccount.length() - 4);
            }
            table.addCell(_noBorderCell(maskedAmountDebitAccount, darkFont));
            table.completeRow();

            table.addCell(_noBorderCell("Charges debit from:", lightFont));
            String maskedChargesDebitAccount = "NA";
            if (StringUtils.isNotBlank(inputDTO.getDebitAmountFrom())) {
                String chargesDebitAccount = inputDTO.getDebitAmountFrom();
                CustomerAccountsDTO account = accountBusinessDelegate.getAccountDetails(customerId, chargesDebitAccount);
                String accountType = ((account != null) && StringUtils.isNotBlank(account.getAccountType())) ? (account.getAccountType() + "...") : "";
                maskedChargesDebitAccount = accountType + chargesDebitAccount.substring(chargesDebitAccount.length() - 4);
            }
            table.addCell(_noBorderCell(maskedChargesDebitAccount, darkFont));
            table.completeRow();
        } else {
            table.addCell(_noBorderCell("Rejected Date:", lightFont));
            table.addCell(_noBorderCell(StringUtils.isNotBlank(inputDTO.getRejectedDate()) ? inputDTO.getRejectedDate() : "NA", darkFont));
            table.completeRow();

            table.addCell(_noBorderCell("Reason for Rejection:", lightFont));
            table.addCell(_noBorderCell(StringUtils.isNotBlank(inputDTO.getReasonForRejection()) ? inputDTO.getReasonForRejection() : "NA", darkFont));
            table.completeRow();
        }

        table.addCell(_noBorderCell("Message to Bank:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(inputDTO.getMessageToBank()) ? inputDTO.getMessageToBank() : "NA", darkFont));
        table.completeRow();

        return table;
    }

    public PdfPTable _collectionDetails(InwardCollectionsDTO inputDTO) {
        PdfPTable table = new PdfPTable(2);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(_noBorderHeadingCell("Collection Details", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Drawer Details:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(inputDTO.getDrawerName()) ? inputDTO.getDrawerName() : "NA", darkFont));
        table.completeRow();


        table.addCell(_noBorderCell("Amount:", lightFont));
        if (StringUtils.isNotBlank(inputDTO.getCurrency()) && StringUtils.isNotBlank(inputDTO.getAmount())) {
            table.addCell(_noBorderCell(inputDTO.getCurrency() + " " + formatAmount(inputDTO.getAmount()), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }
        table.completeRow();

        table.addCell(_noBorderCell("Charges:", lightFont));
        if (StringUtils.isNotBlank(inputDTO.getCurrency()) && StringUtils.isNotBlank(inputDTO.getCharges())) {
            table.addCell(_noBorderCell(inputDTO.getCurrency() + " " + formatAmount(inputDTO.getCharges()), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }
        table.completeRow();

        table.addCell(_noBorderCell("Maturity Date:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(inputDTO.getMaturityDate()) ? _formatDate(inputDTO.getMaturityDate()) + " - " + getMaturityPeriod(inputDTO.getMaturityDate()) : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Tenor Type:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(inputDTO.getTenorType()) ? inputDTO.getTenorType() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Usance Details:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(inputDTO.getUsanceDetails()) ? inputDTO.getUsanceDetails() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("INCO Terms:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(inputDTO.getIncoTerms()) ? inputDTO.getIncoTerms() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Remitting Bank Details:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(inputDTO.getRemittingBank()) ? inputDTO.getRemittingBank() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Documents:", lightFont));
        if (StringUtils.isNotBlank(inputDTO.getDocumentsUploaded())) {
            JSONArray documents = new JSONArray(inputDTO.getDocumentsUploaded());
            String docName = "";
            for (int i = 0; i < documents.length(); i++) {
                docName += documents.getJSONObject(i).getString("documentName") + "\n";
            }
            table.addCell(_noBorderCell(docName, darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }
        table.completeRow();

        table.addCell(_noBorderCell("Message from Bank:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(inputDTO.getMessageFromBank()) ? inputDTO.getMessageFromBank() : "NA", darkFont));
        table.completeRow();

        return table;
    }
}
