/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.temenos.dbx.product.commons.businessdelegate.api.AccountBusinessDelegate;
import com.temenos.dbx.product.commons.dto.CustomerAccountsDTO;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.ReceivedGuaranteeClaimsBackendDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.PaymentAdviceBussinessDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.ReceivedGuaranteeClaimsBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.dto.PaymentAdviceDTO;
import com.temenos.infinity.tradefinanceservices.dto.ReceivedGuaranteeClaimsDTO;
import com.temenos.infinity.tradefinanceservices.utils.ExcelBusinessDelegate;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.temenos.infinity.tradefinanceservices.utils.PdfGenerator.*;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.*;

public class ReceivedGuaranteeClaimsBusinessDelegateImpl implements ReceivedGuaranteeClaimsBusinessDelegate, ExcelBusinessDelegate {
    ReceivedGuaranteeClaimsBackendDelegate claimsBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(
            ReceivedGuaranteeClaimsBackendDelegate.class);
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private final PaymentAdviceBussinessDelegate paymentAdviceBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(PaymentAdviceBussinessDelegate.class);

    public ReceivedGuaranteeClaimsDTO createClaim(ReceivedGuaranteeClaimsDTO ReceivedGuaranteeClaimsDTO,
                                                  HashMap<String, Object> inputParams, DataControllerRequest request) {
        return claimsBackendDelegate.createClaim(ReceivedGuaranteeClaimsDTO, inputParams, request);
    }

    public List<ReceivedGuaranteeClaimsDTO> getClaims(DataControllerRequest request) {
        return claimsBackendDelegate.getClaims(request);
    }

    @Override
    public ReceivedGuaranteeClaimsDTO getClaimsById(String claimsSRMSId, DataControllerRequest request) {
        return claimsBackendDelegate.getClaimsById(claimsSRMSId, request);
    }

    public ReceivedGuaranteeClaimsDTO updateGuaranteeClaims(ReceivedGuaranteeClaimsDTO inputClaimsDTO, boolean isMergeRequired,
                                                            ReceivedGuaranteeClaimsDTO initiatedClaimsDTO, DataControllerRequest request) {
        return claimsBackendDelegate.updateGuaranteeClaims(inputClaimsDTO, isMergeRequired, initiatedClaimsDTO, request);
    }

    @Override
    public List<ReceivedGuaranteeClaimsDTO> getList(DataControllerRequest request) throws ApplicationException {
        return claimsBackendDelegate.getClaims(request);
    }

    public byte[] generateReceivedGuaranteeClaim(ReceivedGuaranteeClaimsDTO claimsDTO, DataControllerRequest request) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outStream);
        addHeadersAndFootersPDF(writer, document, "Claim Initiated", true);
        try {
            document.open();
            document.add(addCustomerDetailsPDF(request));
            document.add(_issuanceSummaryDetails(claimsDTO));
            document.add(_claimDetails(claimsDTO, request));
            if (StringUtils.isNotBlank(claimsDTO.getReturnedDocuments())) {
                document.add(_documentDetails(claimsDTO));
            }
            if (StringUtils.isNotBlank(claimsDTO.getPaymentStatus())) {
                document.add(_paymentDetails(claimsDTO, request));
            }
            request.addRequestParam_("orderId", claimsDTO.getClaimsSRMSId());
            List<PaymentAdviceDTO> paymentAdvices = paymentAdviceBusinessDelegate.getPaymentAdvice(request);
            if (paymentAdvices.size() > 0) {
                document.add(_paymentAdviceDetails(paymentAdvices));
            }
            document.close();
            return outStream.toByteArray();
        } catch (Exception e) {
            alert.prepareError("Error occurred while creating pdf ", e).log();
            return null;
        } finally {
            document.close();
        }
    }

    public PdfPTable _issuanceSummaryDetails(ReceivedGuaranteeClaimsDTO claimsDTO) {
        PdfPTable table = new PdfPTable(2);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(_noBorderHeadingCell("Guarantee & Standby LC Summary", darkFont));
        table.addCell(_noBorderCell("", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Applicant:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getBeneficiaryName()) ? claimsDTO.getBeneficiaryName() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Transaction Ref:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getGuaranteesSRMSId()) ? claimsDTO.getGuaranteesSRMSId() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Product Type:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getProductType()) ? claimsDTO.getProductType() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("GT & SBLC Type:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getGuaranteeAndSBLCType()) ? claimsDTO.getGuaranteeAndSBLCType() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Amount:", lightFont));
        if (StringUtils.isNotBlank(claimsDTO.getAmount()) && StringUtils.isNotBlank(claimsDTO.getClaimCurrency())) {
            table.addCell(_noBorderCell(claimsDTO.getClaimCurrency() + " " + formatAmount(claimsDTO.getAmount()), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }
        table.completeRow();

        table.addCell(_noBorderCell("Issue Date:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getIssueDate()) ? claimsDTO.getIssueDate() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Expiry Type:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getExpiryType()) ? claimsDTO.getExpiryType() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Expiry Date:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getExpiryDate()) ? claimsDTO.getExpiryDate() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Issuing Bank:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getIssuingBank()) ? claimsDTO.getIssuingBank() : "NA", darkFont));
        table.completeRow();

        return table;
    }

    public PdfPTable _claimDetails(ReceivedGuaranteeClaimsDTO claimsDTO, DataControllerRequest request) {
        PdfPTable table = new PdfPTable(2);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(_noBorderHeadingCell("Claim Details", darkFont));
        table.addCell(_noBorderCell("", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Claim Status:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getStatus()) ? claimsDTO.getStatus() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Claim Ref No:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getClaimsSRMSId()) ? claimsDTO.getClaimsSRMSId() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Claim Created Date:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getCreatedOn()) ? claimsDTO.getCreatedOn() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Amount:", lightFont));
        if (StringUtils.isNotBlank(claimsDTO.getClaimAmount()) && StringUtils.isNotBlank(claimsDTO.getClaimCurrency())) {
            table.addCell(_noBorderCell(claimsDTO.getClaimCurrency() + " " + formatAmount(claimsDTO.getClaimAmount()), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }
        table.completeRow();

        table.addCell(_noBorderCell("Claim Credit Account:", lightFont));
        String maskedClaimCreditAccount = "NA";
        if (StringUtils.isNotBlank(claimsDTO.getClaimCreditAccount())) {
            String claimCreditAccount = claimsDTO.getClaimCreditAccount();
            Map<String, Object> customer = CustomerSession.getCustomerMap(request);
            String customerId = CustomerSession.getCustomerId(customer);
            AccountBusinessDelegate actDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AccountBusinessDelegate.class);
            CustomerAccountsDTO account = actDelegate.getAccountDetails(customerId, claimCreditAccount);
            String accountType = ((account != null) && (org.apache.commons.lang.StringUtils.isNotBlank(account.getAccountType())))
                    ? account.getAccountType()
                    : "";
            maskedClaimCreditAccount = accountType + "..." + claimCreditAccount.substring(claimCreditAccount.length() - 4);
        }
        table.addCell(_noBorderCell(maskedClaimCreditAccount, darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Charges Debit Account:", lightFont));
        String maskedChargesDebitAccount = "NA";
        if (StringUtils.isNotBlank(claimsDTO.getChargesDebitAccount())) {
            String chargesDebitAccount = claimsDTO.getChargesDebitAccount();
            Map<String, Object> customer = CustomerSession.getCustomerMap(request);
            String customerId = CustomerSession.getCustomerId(customer);
            AccountBusinessDelegate actDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AccountBusinessDelegate.class);
            CustomerAccountsDTO account = actDelegate.getAccountDetails(customerId, chargesDebitAccount);
            String accountType = ((account != null) && (org.apache.commons.lang.StringUtils.isNotBlank(account.getAccountType())))
                    ? account.getAccountType()
                    : "";
            maskedChargesDebitAccount = accountType + "..." + chargesDebitAccount.substring(chargesDebitAccount.length() - 4);
        }
        table.addCell(_noBorderCell(maskedChargesDebitAccount, darkFont));
        table.completeRow();

        String demandType = StringUtils.isNotBlank(claimsDTO.getDemandType()) ? claimsDTO.getDemandType() : "NA";
        table.addCell(_noBorderCell("Demand Type:", lightFont));
        table.addCell(_noBorderCell(demandType, darkFont));
        table.completeRow();

        if (demandType.equals("Pay/Extend")) {
            table.addCell(_noBorderCell("New Extension Date:", lightFont));
            table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getNewExtensionDate()) ? claimsDTO.getNewExtensionDate() : "NA", darkFont));
            table.completeRow();
        }

        table.addCell(_noBorderCell("Uploaded Documents:", lightFont));
        if (StringUtils.isNotBlank(claimsDTO.getDocumentInformation())) {
            JSONArray documents = new JSONArray(claimsDTO.getDocumentInformation());
            String docName = "";
            for (int i = 0; i < documents.length(); i++) {
                docName += documents.getJSONObject(i).getString("documentName") + "\n";
            }
            table.addCell(_noBorderCell(docName, darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }
        table.completeRow();

        if (claimsDTO.getProductType().equals("Standby LC")) {
            table.addCell(_noBorderCell("Physical Document Details:", lightFont));
            if (StringUtils.isNotBlank(claimsDTO.getPhysicalDocuments())) {
                JSONArray physicalDocs = new JSONArray(claimsDTO.getPhysicalDocuments());
                String physicalDocValue = "";
                for (int i = 0; i < physicalDocs.length(); i++) {
                    JSONObject physicalDocObject = physicalDocs.getJSONObject(i);
                    String docTitle = physicalDocObject.getString("documentTitle");
                    String originalsCount = physicalDocObject.getString("originalsCount") + (physicalDocObject.getString("originalsCount").equals("Will not submit") ? "" : " Originals");
                    String copiesCount = physicalDocObject.getString("copiesCount") + (physicalDocObject.getString("copiesCount").equals("Will not submit") ? "" : " Copies");
                    physicalDocValue += docTitle + " (" + originalsCount + ", " + copiesCount + ")" + "\n";
                }
                if (StringUtils.isNotBlank(physicalDocValue)) {
                    table.addCell(_noBorderCell(physicalDocValue, darkFont));
                } else {
                    table.addCell(_noBorderCell("NA", darkFont));
                }
            } else {
                table.addCell(_noBorderCell("NA", darkFont));
            }
            table.completeRow();

            table.addCell(_noBorderCell("Forward Despite Any Discrepancies:", lightFont));
            if (StringUtils.isNotBlank(claimsDTO.getForwardDocuments())) {
                String forwardDocuments = claimsDTO.getForwardDocuments();
                if (forwardDocuments.equalsIgnoreCase("Selected")) {
                    forwardDocuments += "\n(Kindly forward documents to issuing bank as presented despite any discrepancy(ies) noted)";
                }
                table.addCell(_noBorderCell(forwardDocuments, darkFont));
            } else {
                table.addCell(_noBorderCell("NA", darkFont));
            }
            table.completeRow();
        }

        table.addCell(_noBorderCell("Other Demand Details:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getOtherDemandDetails()) ? claimsDTO.getOtherDemandDetails() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Message To Bank:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getMessageToBank()) ? claimsDTO.getMessageToBank() : "NA", darkFont));
        table.completeRow();

        return table;
    }

    public PdfPTable _documentDetails(ReceivedGuaranteeClaimsDTO claimsDTO) {
        PdfPTable table = new PdfPTable(2);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(_noBorderHeadingCell("Documents & Status", darkFont));
        table.addCell(_noBorderCell("", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Total Documents/Discrepancies:", lightFont));
        if (StringUtils.isNotBlank(claimsDTO.getReturnedDocuments())) {
            String[] returnedDocs = claimsDTO.getReturnedDocuments().split(",");
            table.addCell(_noBorderCell(String.valueOf(returnedDocs.length), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }
        table.completeRow();

        table.addCell(_noBorderCell("Documents:", lightFont));
        if (StringUtils.isNotBlank(claimsDTO.getReturnedDocuments())) {
            String[] returnedDocs = claimsDTO.getReturnedDocuments().split(",");
            table.addCell(_noBorderCell(String.join("\n", returnedDocs), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }
        table.completeRow();

        table.addCell(_noBorderCell("Document Status:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getDocumentStatus()) ? claimsDTO.getDocumentStatus() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Message From Bank:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getMessageFromBank()) ? claimsDTO.getMessageFromBank() : "NA", darkFont));
        table.completeRow();

        return table;
    }

    public PdfPTable _paymentDetails(ReceivedGuaranteeClaimsDTO claimsDTO, DataControllerRequest request) {
        PdfPTable table = new PdfPTable(2);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(_noBorderHeadingCell("Payment Details", darkFont));
        table.addCell(_noBorderCell("", darkFont));
        table.completeRow();

        String paymentStatus = claimsDTO.getPaymentStatus();
        table.addCell(_noBorderCell("Payment Status:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(paymentStatus) ? paymentStatus : "NA", darkFont));
        table.completeRow();

        if (paymentStatus.equalsIgnoreCase(PARAM_STATUS_REJECTED)) {
            table.addCell(_noBorderCell("Rejected Date:", lightFont));
            table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getPaymentSettledDate()) ? claimsDTO.getPaymentSettledDate() : "NA", darkFont));
            table.completeRow();

            table.addCell(_noBorderCell("Reason for Rejection:", lightFont));
            table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getReasonForReturn()) ? claimsDTO.getReasonForReturn() : "NA", darkFont));
            table.completeRow();
        } else {
            if (paymentStatus.equalsIgnoreCase(PARAM_STATUS_DONE)) {
                table.addCell(_noBorderCell("Payment Date:", lightFont));
                table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getPaymentSettledDate()) ? claimsDTO.getPaymentSettledDate() : "NA", darkFont));
                table.completeRow();
            }

            table.addCell(_noBorderCell("Total Amount To Be Paid:", lightFont));
            if (StringUtils.isNotBlank(claimsDTO.getClaimAmount()) && StringUtils.isNotBlank(claimsDTO.getClaimCurrency())) {
                table.addCell(_noBorderCell(claimsDTO.getClaimCurrency() + " " + formatAmount(claimsDTO.getClaimAmount()), darkFont));
            } else {
                table.addCell(_noBorderCell("NA", darkFont));
            }
            table.completeRow();

            table.addCell(_noBorderCell("Claim Credit Account:", lightFont));
            String maskedClaimCreditAccount = "NA";
            if (StringUtils.isNotBlank(claimsDTO.getClaimCreditAccount())) {
                String claimCreditAccount = claimsDTO.getClaimCreditAccount();
                Map<String, Object> customer = CustomerSession.getCustomerMap(request);
                String customerId = CustomerSession.getCustomerId(customer);
                AccountBusinessDelegate actDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AccountBusinessDelegate.class);
                CustomerAccountsDTO account = actDelegate.getAccountDetails(customerId, claimCreditAccount);
                String accountType = ((account != null) && (org.apache.commons.lang.StringUtils.isNotBlank(account.getAccountType())))
                        ? account.getAccountType()
                        : "";
                maskedClaimCreditAccount = accountType + "..." + claimCreditAccount.substring(claimCreditAccount.length() - 4);
            }
            table.addCell(_noBorderCell(maskedClaimCreditAccount, darkFont));
            table.completeRow();
        }

        return table;
    }

    public PdfPTable _paymentAdviceDetails(List<PaymentAdviceDTO> paymentAdvices) {
        PdfPTable superPdfPTable = new PdfPTable(1);
        superPdfPTable.setWidthPercentage(100);

        superPdfPTable.addCell(_borderHeadingCellTable("Payment Advices", darkFont));

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);

        table.addCell(_borderHeadingCell("Advice Name", darkFont));
        table.addCell(_borderHeadingCell("Date", darkFont));
        table.addCell(_borderHeadingCell("Advice Party", darkFont));
        table.addCell(_borderHeadingCell("Message", darkFont));

        for (PaymentAdviceDTO object : paymentAdvices) {
            table.addCell(_borderCell(StringUtils.isNotBlank(object.getAdviceName()) ? object.getAdviceName() : "NA", darkFont));
            table.addCell(_borderCell(StringUtils.isNotBlank(object.getPaymentDate()) ? object.getPaymentDate() : "NA", darkFont));
            table.addCell(_borderCell(StringUtils.isNotBlank(object.getAdvisingBank()) ? object.getAdvisingBank() : "NA", darkFont));
            table.addCell(_borderCell(StringUtils.isNotBlank(object.getMessage()) ? object.getMessage() : "NA", darkFont));
        }

        PdfPCell subPdfPTable = new PdfPCell();
        subPdfPTable.addElement(table);
        subPdfPTable.setBorderColor(Color.WHITE);
        subPdfPTable.setPaddingBottom(10);
        superPdfPTable.addCell(subPdfPTable);
        return superPdfPTable;
    }
}
