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
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.InwardCollectionAmendmentsBackendDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.InwardCollectionAmendmentsBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.dto.InwardCollectionAmendmentsDTO;
import com.temenos.infinity.tradefinanceservices.dto.InwardCollectionsDTO;
import com.temenos.infinity.tradefinanceservices.utils.ExcelBusinessDelegate;
import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.darkFont;
import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.lightFont;
import static com.temenos.infinity.tradefinanceservices.utils.PdfGenerator.*;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.*;

public class InwardCollectionAmendmentsBusinessDelegateImpl implements InwardCollectionAmendmentsBusinessDelegate, ExcelBusinessDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private InwardCollectionAmendmentsBackendDelegate requestBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(InwardCollectionAmendmentsBackendDelegate.class);

    @Override
    public InwardCollectionAmendmentsDTO createInwardCollectionAmendment(InwardCollectionAmendmentsDTO inputDTO, DataControllerRequest request) {
        return requestBackendDelegate.createInwardCollectionAmendment(inputDTO, request);
    }

    @Override
    public List<InwardCollectionAmendmentsDTO> getInwardCollectionAmendments(DataControllerRequest request) {
        return requestBackendDelegate.getInwardCollectionAmendments(request);
    }

    @Override
    public InwardCollectionAmendmentsDTO getInwardCollectionAmendmentById(String amendmentSrmsId, DataControllerRequest request) {
        return requestBackendDelegate.getInwardCollectionAmendmentById(amendmentSrmsId, request);
    }

    @Override
    public InwardCollectionAmendmentsDTO updateInwardCollectionAmendment(InwardCollectionAmendmentsDTO amendmentDetails, DataControllerRequest request) {
        return requestBackendDelegate.updateInwardCollectionAmendment(amendmentDetails, request);
    }

    @Override
    public List<InwardCollectionAmendmentsDTO> getList(DataControllerRequest request) throws ApplicationException {
        return requestBackendDelegate.getInwardCollectionAmendments(request);
    }

    @Override
    public byte[] generateInwardCollectionAmendment(InwardCollectionAmendmentsDTO amendmentDTO, InwardCollectionsDTO collectionDTO, DataControllerRequest request) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outStream);
        addHeadersAndFootersPDF(writer, document, "Inward Collection - Amendment", true);

        try {
            document.open();

            document.add(addCustomerDetailsPDF(request));
            document.add(collectionSummary(collectionDTO));
            document.add(amendmentOverview(amendmentDTO));
            document.add(draweeConsent(amendmentDTO));
            document.add(amendmentDetails(amendmentDTO));

        } catch (Exception e) {
            alert.prepareError("Error occurred while creating pdf ", e).log();
        } finally {
            document.close();
        }

        return outStream.toByteArray();
    }

    private Element collectionSummary(InwardCollectionsDTO collectionDTO) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(_noBorderHeadingCell("Collection Summary", darkFont));

        table.addCell(_noBorderCell("Drawer Details:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(collectionDTO.getDrawerName()) ? collectionDTO.getDrawerName() : "NA", darkFont));

        table.addCell(_noBorderCell("Transaction Ref:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(collectionDTO.getCollectionSrmsId()) ? collectionDTO.getCollectionSrmsId() : "NA", darkFont));

        table.addCell(_noBorderCell("Amount:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(collectionDTO.getAmount()) ? collectionDTO.getCurrency() + " " + formatAmount(collectionDTO.getAmount()) : "NA", darkFont));

        table.addCell(_noBorderCell("Tenor Type:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(collectionDTO.getTenorType()) ? collectionDTO.getTenorType() : "NA", darkFont));

        table.addCell(_noBorderCell("Maturity Date:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(collectionDTO.getMaturityDate()) ? _formatDate(collectionDTO.getMaturityDate()) + " - " + getMaturityPeriod(collectionDTO.getMaturityDate()) : "NA", darkFont));

        table.addCell(_noBorderCell("Remitting Bank Details:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(collectionDTO.getRemittingBank()) ? collectionDTO.getRemittingBank() : "NA", darkFont));

        return table;
    }

    private Element amendmentOverview(InwardCollectionAmendmentsDTO amendmentDTO) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(_noBorderHeadingCell("Amendment Overview", darkFont));

        table.addCell(_noBorderCell("Status:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(amendmentDTO.getStatus()) ? amendmentDTO.getStatus() : "NA", darkFont));

        table.addCell(_noBorderCell("Received On:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(amendmentDTO.getReceivedOn()) ? _formatDate(amendmentDTO.getReceivedOn()) : "NA", darkFont));

        table.addCell(_noBorderCell("Amendment No:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(amendmentDTO.getAmendmentNo()) ? amendmentDTO.getAmendmentNo() : "NA", darkFont));

        table.addCell(_noBorderCell("Amendment Reference:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(amendmentDTO.getAmendmentSrmsId()) ? amendmentDTO.getAmendmentSrmsId() : "NA", darkFont));

        return table;
    }

    private Element amendmentDetails(InwardCollectionAmendmentsDTO amendmentDTO) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(_noBorderHeadingCell("Amendment Requested", darkFont));

        table.addCell(_noBorderCell("Amount:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(amendmentDTO.getAmendAmount()) ? amendmentDTO.getCurrency() + " " + formatAmount(amendmentDTO.getAmendAmount()) : "NA", darkFont));

        table.addCell(_noBorderCell("Maturity Date:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(amendmentDTO.getAmendMaturityDate()) ? _formatDate(amendmentDTO.getAmendMaturityDate()) + " - " + getMaturityPeriod(amendmentDTO.getAmendMaturityDate()) : "NA", darkFont));

        table.addCell(_noBorderCell("Tenor Type:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(amendmentDTO.getAmendTenorType()) ? amendmentDTO.getAmendTenorType() : "NA", darkFont));

        table.addCell(_noBorderCell("Usance Details:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(amendmentDTO.getAmendUsanceDetails()) ? amendmentDTO.getAmendUsanceDetails() : "NA", darkFont));

        table.addCell(_noBorderCell("Remitting Bank Details:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(amendmentDTO.getAmendRemittingBank()) ? amendmentDTO.getAmendRemittingBank() : "NA", darkFont));

        table.addCell(_noBorderCell("Documents:", lightFont));
        if (StringUtils.isNotBlank(amendmentDTO.getAmendDocuments())) {
            JSONArray documents = new JSONArray(amendmentDTO.getAmendDocuments());
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
        table.addCell(_noBorderCell(StringUtils.isNotBlank(amendmentDTO.getMessageFromBank()) ? amendmentDTO.getMessageFromBank() : "NA", darkFont));

        return table;
    }

    private Element draweeConsent(InwardCollectionAmendmentsDTO amendmentDTO) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(_noBorderHeadingCell("Drawee Consent", darkFont));

        table.addCell(_noBorderCell("Drawee Acknowledgement:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(amendmentDTO.getDraweeAcknowledgement()) ? amendmentDTO.getDraweeAcknowledgement() : "NA", darkFont));

        if (StringUtils.isNotBlank(amendmentDTO.getDraweeAcknowledgement()) && (amendmentDTO.getDraweeAcknowledgement().equals(PARAM_STATUS_REJECTED) || amendmentDTO.getDraweeAcknowledgement().equals(PARAM_STATUS_REJECT))) {
            table.addCell(_noBorderCell("Reason for Rejection:", lightFont));
            table.addCell(_noBorderCell(StringUtils.isNotBlank(amendmentDTO.getReasonForRejection()) ? amendmentDTO.getReasonForRejection() : "NA", darkFont));
        }

        table.addCell(_noBorderCell("Message to Bank:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(amendmentDTO.getMessageToBank()) ? amendmentDTO.getMessageToBank() : "NA", darkFont));

        return table;
    }
}
