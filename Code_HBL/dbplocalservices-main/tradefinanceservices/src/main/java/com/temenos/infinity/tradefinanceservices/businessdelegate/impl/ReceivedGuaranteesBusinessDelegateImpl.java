/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.ReceivedGuaranteesBackendDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.ReceivedGuaranteesBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.dto.ReceivedGuaranteesDTO;
import com.temenos.infinity.tradefinanceservices.utils.ExcelBusinessDelegate;
import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.darkFont;
import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.lightFont;
import static com.temenos.infinity.tradefinanceservices.utils.PdfGenerator.*;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.*;

public class ReceivedGuaranteesBusinessDelegateImpl implements ReceivedGuaranteesBusinessDelegate, ExcelBusinessDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private ReceivedGuaranteesBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(ReceivedGuaranteesBackendDelegate.class);

    @Override
    public ReceivedGuaranteesDTO createReceivedGuarantee(ReceivedGuaranteesDTO inputDTO, DataControllerRequest request) throws DBPApplicationException {
        return orderBackendDelegate.createReceivedGuarantee(inputDTO, request);
    }

    @Override
    public List<ReceivedGuaranteesDTO> getReceivedGuarantees(DataControllerRequest request) {
        return orderBackendDelegate.getReceivedGuarantees(request);
    }

    @Override
    public List<ReceivedGuaranteesDTO> getList(DataControllerRequest request) throws ApplicationException {
        return orderBackendDelegate.getReceivedGuarantees(request);
    }

    @Override
    public ReceivedGuaranteesDTO getReceivedGuaranteeById(String guaranteeSrmsId, DataControllerRequest request) {
        return orderBackendDelegate.getReceivedGuaranteeById(guaranteeSrmsId, request);
    }

    @Override
    public ReceivedGuaranteesDTO updateReceivedGuarantee(ReceivedGuaranteesDTO guaranteeDetails, DataControllerRequest request) {
        return orderBackendDelegate.updateReceivedGuarantee(guaranteeDetails, request);
    }

    @Override
    public byte[] generateReceivedGuarantee(ReceivedGuaranteesDTO guaranteeDetailsDTO, DataControllerRequest request) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outStream);
        addHeadersAndFootersPDF(writer, document, "Received Guarantee & Standby LC", true);

        try {
            document.open();

            document.add(addCustomerDetailsPDF(request));
            document.add(issuanceSummary(guaranteeDetailsDTO));
            document.add(productDetails(guaranteeDetailsDTO));
            document.add(transactionDetails(guaranteeDetailsDTO));
            document.add(applicantDetails(guaranteeDetailsDTO));
            document.add(additionalInstructions(guaranteeDetailsDTO));

        } catch (Exception e) {
            alert.prepareError("Error occurred while creating pdf ", e).log();
            return null;
        } finally {
            document.close();
        }
        return outStream.toByteArray();
    }

    private Element issuanceSummary(ReceivedGuaranteesDTO guaranteeDTO) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(_noBorderHeadingCell("Issuance Summary", darkFont));

        table.addCell(_noBorderCell("Transaction Reference:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(guaranteeDTO.getTransactionReference()) ? guaranteeDTO.getTransactionReference() : "NA", darkFont));

        table.addCell(_noBorderCell("Received On:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(guaranteeDTO.getReceivedOn()) ? _formatDate(guaranteeDTO.getReceivedOn()) : "NA", darkFont));

        table.addCell(_noBorderCell("Request Status:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(guaranteeDTO.getStatus()) ? guaranteeDTO.getStatus() : "NA", darkFont));

        table.addCell(_noBorderCell("Current Self Acceptance:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(guaranteeDTO.getSelfAcceptance()) ? guaranteeDTO.getSelfAcceptance() : "NA", darkFont));

        table.addCell(_noBorderCell("Last Date of Acceptance:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(guaranteeDTO.getSelfAcceptanceDate()) ? _formatDate(guaranteeDTO.getSelfAcceptanceDate()) : "NA", darkFont));

        table.addCell(_noBorderCell("Last Reason for Rejection:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(guaranteeDTO.getReasonForSelfRejection()) ? guaranteeDTO.getReasonForSelfRejection() : "NA", darkFont));

        table.addCell(_noBorderCell("Last Message to bank:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(guaranteeDTO.getMessageToBank()) ? guaranteeDTO.getMessageToBank() : "NA", darkFont));

        return table;
    }

    private Element productDetails(ReceivedGuaranteesDTO guaranteeDTO) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(_noBorderHeadingCell("Product Details", darkFont));

        table.addCell(_noBorderCell("Product Type:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(guaranteeDTO.getProductType()) ? guaranteeDTO.getProductType() : "NA", darkFont));

        table.addCell(_noBorderCell("GT & SBLC Type:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(guaranteeDTO.getLcType()) ? guaranteeDTO.getLcType() : "NA", darkFont));

        table.addCell(_noBorderCell("Related Transaction Reference (Optional):", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(guaranteeDTO.getRelatedTransactionReference()) ? guaranteeDTO.getRelatedTransactionReference() : "NA", darkFont));

        table.addCell(_noBorderCell("Mode of Transaction:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(guaranteeDTO.getModeOfTransaction()) ? guaranteeDTO.getModeOfTransaction() : "NA", darkFont));

        table.addCell(_noBorderCell("Applicant Party:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(guaranteeDTO.getApplicantParty()) ? guaranteeDTO.getApplicantParty() : "NA", darkFont));

        table.addCell(_noBorderCell("Beneficiary:", lightFont));
        String beneficiaryDetails = "";
        if (StringUtils.isNotBlank(guaranteeDTO.getBeneficiaryDetails())) {
            JSONArray beneficiaryList = new JSONArray(guaranteeDTO.getBeneficiaryDetails());
            for (int i = 0; i < beneficiaryList.length(); i++) {
                JSONObject beneficiary = beneficiaryList.getJSONObject(i);
                beneficiaryDetails += beneficiary.getString("beneficiaryName") + " - " + beneficiary.getString("payeeId") + "\n";
            }
        }
        table.addCell(_noBorderCell(StringUtils.isNotBlank(beneficiaryDetails) ? beneficiaryDetails : "NA", darkFont));

        return table;
    }

    private Element transactionDetails(ReceivedGuaranteesDTO guaranteeDTO) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(_noBorderHeadingCell("Transaction Details", darkFont));

        table.addCell(_noBorderCell("Request Amount:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(guaranteeDTO.getAmount()) ? guaranteeDTO.getCurrency() + " " + formatAmount(guaranteeDTO.getAmount()) : "NA", darkFont));

        table.addCell(_noBorderCell("Expected Issue Date (Optional):", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(guaranteeDTO.getExpectedIssueDate()) ? guaranteeDTO.getExpectedIssueDate() : "NA", darkFont));

        table.addCell(_noBorderCell("Expiry Type:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(guaranteeDTO.getExpiryType()) ? guaranteeDTO.getExpiryType() : "NA", darkFont));

        table.addCell(_noBorderCell("Expiry Date (Optional):", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(guaranteeDTO.getExpiryDate()) ? guaranteeDTO.getExpiryDate() : "NA", darkFont));

        table.addCell(_noBorderCell("Expiry Conditions (Optional):", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(guaranteeDTO.getExpiryConditions()) ? guaranteeDTO.getExpiryConditions() : "NA", darkFont));

        table.addCell(_noBorderCell("Auto Extension of Expiry (Optional):", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(guaranteeDTO.getAutoExtensionExpiry()) ? guaranteeDTO.getAutoExtensionExpiry() : "NA", darkFont));

        table.addCell(_noBorderCell("Extension Period:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(guaranteeDTO.getExtensionPeriod()) ? guaranteeDTO.getExtensionPeriod() : "NA", darkFont));

        table.addCell(_noBorderCell("Extension Cap Period (Optional):", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(guaranteeDTO.getExtensionCapPeriod()) ? guaranteeDTO.getExtensionCapPeriod() : "NA", darkFont));

        table.addCell(_noBorderCell("Notification Period (Optional):", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(guaranteeDTO.getNotificationPeriod()) ? guaranteeDTO.getNotificationPeriod() : "NA", darkFont));

        table.addCell(_noBorderCell("Extension Details (Optional):", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(guaranteeDTO.getExtensionDetails()) ? guaranteeDTO.getExtensionDetails() : "NA", darkFont));

        table.addCell(_noBorderCell("Governing Law (Optional):", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(guaranteeDTO.getGoverningLaw()) ? guaranteeDTO.getGoverningLaw() : "NA", darkFont));


        table.addCell(_noBorderCell("Applicable Rules (Optional):", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(guaranteeDTO.getApplicableRules()) ? guaranteeDTO.getApplicableRules() : "NA", darkFont));

        table.addCell(_noBorderCell("Delivery Instructions (Optional):", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(guaranteeDTO.getDeliveryInstructions()) ? guaranteeDTO.getDeliveryInstructions() : "NA", darkFont));

        return table;
    }

    private Element applicantDetails(ReceivedGuaranteesDTO guaranteeDTO) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(_noBorderHeadingCell("Applicant & Issuing Bank Details", darkFont));

        table.addCell(_noBorderSubHeadingCell("Applicant", darkFont));

        table.addCell(_noBorderCell("Applicant Name:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(guaranteeDTO.getApplicantName()) ? guaranteeDTO.getApplicantName() : "NA", darkFont));

        table.addCell(_noBorderCell("Applicant Address:", lightFont));
        String applicantAddress = "";
        if (StringUtils.isNotBlank(guaranteeDTO.getApplicantAddress())) {
            JSONObject addressDetails = new JSONObject(guaranteeDTO.getApplicantAddress());
            applicantAddress = (addressDetails.has("address1") ? !addressDetails.getString("address1").isEmpty() ? addressDetails.getString("address1") : "" : "")
                    + (addressDetails.has("address2") ? !addressDetails.getString("address2").isEmpty() ? ", " + addressDetails.getString("address2") : "" : "")
                    + (addressDetails.has("city") ? !addressDetails.getString("city").isEmpty() ? ", " + addressDetails.getString("city") : "" : "")
                    + (addressDetails.has("state") ? !addressDetails.getString("state").isEmpty() ? ", " + addressDetails.getString("state") : "" : "")
                    + (addressDetails.has("country") ? !addressDetails.getString("country").isEmpty() ? ", " + addressDetails.getString("country") : "" : "")
                    + (addressDetails.has("zipcode") ? !addressDetails.getString("zipcode").isEmpty() ? ", " + addressDetails.getString("zipcode") : "" : "") + ".";
        }
        table.addCell(_noBorderCell(StringUtils.isNotBlank(applicantAddress) ? applicantAddress : "NA", darkFont));

        table.addCell(_noBorderSubHeadingCell("Issuing Bank Details", darkFont));

        table.addCell(_noBorderCell("Bank Name:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(guaranteeDTO.getIssuingBankName()) ? guaranteeDTO.getIssuingBankName() : "NA", darkFont));

        table.addCell(_noBorderCell("Swift/BIC Code:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(guaranteeDTO.getIssuingBankSwiftBicCode()) ? guaranteeDTO.getIssuingBankSwiftBicCode() : "NA", darkFont));

        table.addCell(_noBorderCell("IBAN:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(guaranteeDTO.getIssuingBankIban()) ? guaranteeDTO.getIssuingBankIban() : "NA", darkFont));

        table.addCell(_noBorderCell("Local Code:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(guaranteeDTO.getIssuingBankLocalCode()) ? guaranteeDTO.getIssuingBankLocalCode() : "NA", darkFont));

        table.addCell(_noBorderCell("Bank Address:", lightFont));
        String bankAddress = "";
        if (StringUtils.isNotBlank(guaranteeDTO.getApplicantAddress())) {
            JSONObject addressDetails = new JSONObject(guaranteeDTO.getApplicantAddress());
            bankAddress = (addressDetails.has("address1") ? !addressDetails.getString("address1").isEmpty() ? addressDetails.getString("address1") : "" : "")
                    + (addressDetails.has("address2") ? !addressDetails.getString("address2").isEmpty() ? ", " + addressDetails.getString("address2") : "" : "")
                    + (addressDetails.has("city") ? !addressDetails.getString("city").isEmpty() ? ", " + addressDetails.getString("city") : "" : "")
                    + (addressDetails.has("state") ? !addressDetails.getString("state").isEmpty() ? ", " + addressDetails.getString("state") : "" : "")
                    + (addressDetails.has("country") ? !addressDetails.getString("country").isEmpty() ? ", " + addressDetails.getString("country") : "" : "")
                    + (addressDetails.has("zipcode") ? !addressDetails.getString("zipcode").isEmpty() ? ", " + addressDetails.getString("zipcode") : "" : "") + ".";
        }
        table.addCell(_noBorderCell(StringUtils.isNotBlank(bankAddress) ? bankAddress : "NA", darkFont));

        return table;
    }

    private Element additionalInstructions(ReceivedGuaranteesDTO guaranteeDTO) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(_noBorderHeadingCell("Additional Instructions & Documents", darkFont));

        table.addCell(_noBorderCell("Message from Bank :", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(guaranteeDTO.getMessageFromBank()) ? guaranteeDTO.getMessageFromBank() : "NA", darkFont));

        table.addCell(_noBorderSubHeadingCell("Supporting Documents", darkFont));

        table.addCell(_noBorderCell("Uploaded Files:", lightFont));
        String displayDocuments = "";
        if (StringUtils.isNotBlank(guaranteeDTO.getUploadedDocuments())) {
            JSONArray uploadFiles = new JSONArray(guaranteeDTO.getUploadedDocuments());
            for (int i = 0; i < uploadFiles.length(); i++) {
                JSONObject uploadFile = uploadFiles.getJSONObject(i);
                displayDocuments += uploadFile.getString("documentName") + "\n";
            }
        }
        table.addCell(_noBorderCell(StringUtils.isNotBlank(displayDocuments) ? displayDocuments : "NA", darkFont));

        return table;
    }

}
