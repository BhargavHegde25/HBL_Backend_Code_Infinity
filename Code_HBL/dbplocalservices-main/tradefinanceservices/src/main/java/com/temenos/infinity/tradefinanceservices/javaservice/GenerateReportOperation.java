/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.javaservice;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.OutwardCollectionAmendmentsBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.OutwardCollectionsBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.temenos.infinity.tradefinanceservices.dto.OutwardCollectionAmendmentsDTO;
import com.temenos.infinity.tradefinanceservices.dto.OutwardCollectionsDTO;
import com.temenos.infinity.tradefinanceservices.utils.PdfContentStyle;
import com.temenos.infinity.tradefinanceservices.utils.PdfGenerator;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;
import com.kony.dbputilities.util.Log4j2Configurator;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum.ERRTF_29056;
import static com.temenos.infinity.tradefinanceservices.utils.PdfGenerator.FontStyle.*;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.*;

/**
 * @author k.meiyazhagan
 */
public class GenerateReportOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private String orderId;
    private String prefix;
    private DataControllerRequest request;
    private static String pdfTitle = MODULENAME_TRADEFINANCE;
    private static LinkedHashMap<PdfContentStyle, PdfContentStyle> reportData = new LinkedHashMap<>();

    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request, DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        Result result = new Result();
        this.request = request;
        this.orderId = request.getParameter("orderId");
        if (StringUtils.isBlank(orderId)) {
            alert.prepareError("Input params are missing").log();
            return ErrorCodeEnum.ERRTF_29049.setErrorCode(result);
        }

        try {
            this._retrieveData(methodId);
            result.addParam("fileId", PdfGenerator.generatePdf(request, prefix, pdfTitle, reportData));
        } catch (Exception e) {
            alert.prepareError("Error generating trade finance report", e).log();
            result.addErrMsgParam(ERRTF_29056.getErrorMessage());
        }
        return result;
    }

    private void _retrieveData(String methodId) {
        Map<String, Object> customer = CustomerSession.getCustomerMap(request);
        String customerId = CustomerSession.getCustomerId(customer);
        try {
            switch (methodId) {
                case GENERATEOUTWARDCOLLECTIONREPORT_METHODID:
                    pdfTitle = MODULENAME_OUTWARD_COLLECTION;
                    prefix = PREFIX_OUTWARD_COLLECTION;
                    OutwardCollectionsDTO outwardCollectionDTO = DBPAPIAbstractFactoryImpl.getBusinessDelegate(OutwardCollectionsBusinessDelegate.class).getCollectionById(orderId, this.request);
                    reportData = new LinkedHashMap<>();
                    reportData.put(new PdfContentStyle("Collection Overview", HEADING), null);
                    reportData.put(new PdfContentStyle("Transaction Ref:", REGULAR_LIGHT), new PdfContentStyle(outwardCollectionDTO.getCollectionReference()));
                    reportData.put(new PdfContentStyle("Updated On:", REGULAR_LIGHT), new PdfContentStyle(outwardCollectionDTO.getUpdatedOnFormatted()));
                    reportData.put(new PdfContentStyle("Status:", REGULAR_LIGHT), new PdfContentStyle(outwardCollectionDTO.getStatus(), REGULARBOLD_DARK));
                    reportData.put(new PdfContentStyle("Collection Details", HEADING), null);
                    reportData.put(new PdfContentStyle("Document Number:", REGULAR_LIGHT), new PdfContentStyle(outwardCollectionDTO.getDocumentNo()));
                    reportData.put(new PdfContentStyle("Created On:", REGULAR_LIGHT), new PdfContentStyle(outwardCollectionDTO.getCreatedOnFormatted()));
                    reportData.put(new PdfContentStyle("Tenor Type:", REGULAR_LIGHT), new PdfContentStyle(outwardCollectionDTO.getTenorType()));
                    reportData.put(new PdfContentStyle("Usance Days:", REGULAR_LIGHT), new PdfContentStyle(outwardCollectionDTO.getUsanceDays()));
                    reportData.put(new PdfContentStyle("Usance Details:", REGULAR_LIGHT), new PdfContentStyle(outwardCollectionDTO.getUsanceDetails()));
                    reportData.put(new PdfContentStyle("Allow Usance Acceptance:", REGULAR_LIGHT), new PdfContentStyle(outwardCollectionDTO.getAllowUsanceAcceptance()));
                    reportData.put(new PdfContentStyle("Amount & Account Details", HEADING), null);
                    reportData.put(new PdfContentStyle("Request Amount:", REGULAR_LIGHT), new PdfContentStyle(getAmountWithCurrency(outwardCollectionDTO.getCurrency(), outwardCollectionDTO.getAmount(), false)));
                    reportData.put(new PdfContentStyle("Amount Credit Account:", REGULAR_LIGHT), new PdfContentStyle(getMaskedAccountDetails(customerId, outwardCollectionDTO.getCreditAccount())));
                    reportData.put(new PdfContentStyle("Charges Debit Account:", REGULAR_LIGHT), new PdfContentStyle(getMaskedAccountDetails(customerId, outwardCollectionDTO.getDebitAccount())));
                    reportData.put(new PdfContentStyle("Drawee & Collecting Bank Details", HEADING), null);
                    reportData.put(new PdfContentStyle("Drawee Details", SUBHEADING_BOLD_DARK), null);
                    reportData.put(new PdfContentStyle("Drawee Name:", REGULAR_LIGHT), new PdfContentStyle(outwardCollectionDTO.getDraweeName()));
                    reportData.put(new PdfContentStyle("Drawee Address:", REGULAR_LIGHT), new PdfContentStyle(_getAddress(outwardCollectionDTO.getDraweeAddress())));
                    reportData.put(new PdfContentStyle("Collecting Bank Details", SUBHEADING_BOLD_DARK), null);
                    reportData.put(new PdfContentStyle("Bank Name:", REGULAR_LIGHT), new PdfContentStyle(outwardCollectionDTO.getCollectingBank()));
                    reportData.put(new PdfContentStyle("Swift/BIC Code:", REGULAR_LIGHT), new PdfContentStyle(outwardCollectionDTO.getSwiftOrBicCode()));
                    reportData.put(new PdfContentStyle("Bank Address:", REGULAR_LIGHT), new PdfContentStyle(_getAddress(outwardCollectionDTO.getCollectingBankAddress())));
                    reportData.put(new PdfContentStyle("Documents & Bank Instructions", HEADING), null);
                    reportData.put(new PdfContentStyle("Upload & Physical Document Counts", SUBHEADING_BOLD_DARK), null);
                    reportData.put(new PdfContentStyle("Uploaded Documents:", REGULAR_LIGHT), new PdfContentStyle(_getDocumentsList(outwardCollectionDTO.getUploadDocuments())));
                    reportData.put(new PdfContentStyle("Physical Document Details:", REGULAR_LIGHT), new PdfContentStyle(_getPhysicalDocuments(outwardCollectionDTO.getPhysicalDocuments())));
                    reportData.put(new PdfContentStyle("Bank Instructions", SUBHEADING_BOLD_DARK), null);
                    reportData.put(new PdfContentStyle("Inco Terms:", REGULAR_LIGHT), new PdfContentStyle(outwardCollectionDTO.getIncoTerms()));
                    reportData.put(new PdfContentStyle("Delivery Instructions (Optional):", REGULAR_LIGHT), new PdfContentStyle(outwardCollectionDTO.getDeliveryInstructions()));
                    reportData.put(new PdfContentStyle("Other Collection Details (Optional):", REGULAR_LIGHT), new PdfContentStyle(outwardCollectionDTO.getOtherCollectionDetails()));
                    reportData.put(new PdfContentStyle("Message to Bank (Optional):", REGULAR_LIGHT), new PdfContentStyle(outwardCollectionDTO.getMessageToBank()));
                    reportData.put(new PdfContentStyle("Instructions for Bills:", REGULAR_LIGHT), new PdfContentStyle(_getInstructions(outwardCollectionDTO.getInstructionsForBills())));
                    break;
                case GENERATEOUTWARDAMENDMENTREPORT_METHODID:
                    pdfTitle = MODULENAME_OUTWARD_COLLECTION_AMENDMENT;
                    prefix = PREFIX_OUTWARD_COLLECTION_AMENDMENT;
                    OutwardCollectionAmendmentsDTO outwardAmendmentDTO = DBPAPIAbstractFactoryImpl.getBusinessDelegate(OutwardCollectionAmendmentsBusinessDelegate.class).getAmendmentById(orderId, this.request);
                    OutwardCollectionsDTO outwardCollectionDto = DBPAPIAbstractFactoryImpl.getBusinessDelegate(OutwardCollectionsBusinessDelegate.class).getCollectionById(outwardAmendmentDTO.getCollectionReference(), this.request);
                    reportData = new LinkedHashMap<>();
                    reportData.put(new PdfContentStyle("Collection Summary", HEADING), null);
                    reportData.put(new PdfContentStyle("Drawee Details:", REGULAR_LIGHT), new PdfContentStyle(outwardCollectionDto.getDraweeName()));
                    reportData.put(new PdfContentStyle("Transaction Ref:", REGULAR_LIGHT), new PdfContentStyle(outwardCollectionDto.getCollectionReference()));
                    reportData.put(new PdfContentStyle("Amount:", REGULAR_LIGHT), new PdfContentStyle(getAmountWithCurrency(outwardCollectionDto.getCurrency(), outwardCollectionDto.getAmount(), false)));
                    reportData.put(new PdfContentStyle("Tenor Type:", REGULAR_LIGHT), new PdfContentStyle(outwardCollectionDto.getTenorType()));
                    reportData.put(new PdfContentStyle("Maturity Date:", REGULAR_LIGHT), new PdfContentStyle(_formatDate(outwardCollectionDto.getMaturityDate())));
                    reportData.put(new PdfContentStyle("Collecting Bank Details:", REGULAR_LIGHT), new PdfContentStyle(outwardCollectionDto.getCollectingBank()));
                    reportData.put(new PdfContentStyle("Amendment Overview", HEADING), null);
                    reportData.put(new PdfContentStyle("Status:", REGULAR_LIGHT), new PdfContentStyle(outwardAmendmentDTO.getStatus(), REGULARBOLD_DARK));
                    reportData.put(new PdfContentStyle("Updated On:", REGULAR_LIGHT), new PdfContentStyle(_formatDate(outwardAmendmentDTO.getUpdatedOn())));
                    reportData.put(new PdfContentStyle("Amendment Number:", REGULAR_LIGHT), new PdfContentStyle(outwardAmendmentDTO.getAmendmentNo()));
                    reportData.put(new PdfContentStyle("Requested On:", REGULAR_LIGHT), new PdfContentStyle(_formatDate(outwardAmendmentDTO.getRequestedOn())));
                    reportData.put(new PdfContentStyle("Amendment Reference:", REGULAR_LIGHT), new PdfContentStyle(outwardAmendmentDTO.getAmendmentReference()));
                    reportData.put(new PdfContentStyle("Amendment Requested", HEADING), null);
                    reportData.put(new PdfContentStyle("Document Number:", REGULAR_LIGHT), new PdfContentStyle(outwardAmendmentDTO.getDocumentNo()));
                    reportData.put(new PdfContentStyle("Tenor Type:", REGULAR_LIGHT), new PdfContentStyle(outwardAmendmentDTO.getAmendTenorType()));
                    reportData.put(new PdfContentStyle("Usance Days:", REGULAR_LIGHT), new PdfContentStyle(outwardAmendmentDTO.getUsanceDays()));
                    reportData.put(new PdfContentStyle("Usance Details:", REGULAR_LIGHT), new PdfContentStyle(outwardAmendmentDTO.getUsanceDetails()));
                    reportData.put(new PdfContentStyle("Allow Usance Acceptance:", REGULAR_LIGHT), new PdfContentStyle(outwardAmendmentDTO.getAllowUsanceAcceptance()));
                    reportData.put(new PdfContentStyle("Amount & Account Details", SUBHEADING_BOLD_DARK), null);
                    reportData.put(new PdfContentStyle("Amount:", REGULAR_LIGHT), new PdfContentStyle(getAmountWithCurrency(outwardAmendmentDTO.getCurrency(), outwardAmendmentDTO.getAmount(), false)));
                    reportData.put(new PdfContentStyle("Amount Credit Account:", REGULAR_LIGHT), new PdfContentStyle(getMaskedAccountDetails(customerId, outwardAmendmentDTO.getCreditAccount())));
                    reportData.put(new PdfContentStyle("Charges Debit Account:", REGULAR_LIGHT), new PdfContentStyle(getMaskedAccountDetails(customerId, outwardAmendmentDTO.getChargesDebitAccount())));
                    reportData.put(new PdfContentStyle("Document & Bank Instructions", HEADING), null);
                    reportData.put(new PdfContentStyle("Uploaded Documents:", REGULAR_LIGHT), new PdfContentStyle(_getDocumentsList(outwardAmendmentDTO.getUploadDocuments())));
                    reportData.put(new PdfContentStyle("Physical Document Details:", REGULAR_LIGHT), new PdfContentStyle(_getPhysicalDocuments(outwardAmendmentDTO.getPhysicalDocuments())));
                    reportData.put(new PdfContentStyle("Other Collection Details:", REGULAR_LIGHT), new PdfContentStyle(outwardAmendmentDTO.getOtherCollectionDetails()));
                    reportData.put(new PdfContentStyle("Message to Bank:", REGULAR_LIGHT), new PdfContentStyle(outwardAmendmentDTO.getMessageToBank()));
                    break;
            }
        } catch (Exception e) {
            alert.prepareError("Failed to fetch details", e).log();
            reportData = null;
        }
    }

    private String _getDocumentsList(String uploadedDocuments) {
        StringBuilder result = new StringBuilder();
        for (Object obj : new JSONArray(uploadedDocuments)) {
            result.append(new JSONObject(obj.toString()).getString("documentName")).append("\n");
        }
        return result.toString();
    }

    private String _getPhysicalDocuments(String physicalDocuments) {
        StringBuilder result = new StringBuilder();
        for (Object obj : new JSONArray(physicalDocuments)) {
            JSONObject doc = new JSONObject(obj.toString());
            result.append(doc.getString("documentTitle"))
                    .append(" (").append(doc.getString("originalsCount")).append(", ")
                    .append(doc.getString("copiesCount")).append(")").append("\n");
        }
        return result.toString();
    }

    private String _getInstructions(String billInstructions) {
        StringBuilder result = new StringBuilder();
        byte count = 0;
        for (Object obj : new JSONArray(billInstructions)) {
            count++;
            result.append(count).append(". ").append(obj).append("\n");
        }
        return result.toString();
    }

    private String _getAddress(String address) {
        JSONObject obj = new JSONObject(address);
        return obj.getString("address1").concat(", ")
                .concat(obj.getString("address2")).concat(", ")
                .concat(obj.getString("address2")).concat(", ")
                .concat(obj.getString("state")).concat(", ")
                .concat(obj.getString("country")).concat(", ")
                .concat(obj.getString("zipcode")).concat(".");
    }
}
