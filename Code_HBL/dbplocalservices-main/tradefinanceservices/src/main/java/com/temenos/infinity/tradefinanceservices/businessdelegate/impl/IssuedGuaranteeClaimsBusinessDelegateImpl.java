/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.IssuedGuaranteeClaimsBackendDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.IssuedGuaranteeClaimsBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants;
import com.temenos.infinity.tradefinanceservices.dto.GuaranteesDTO;
import com.temenos.infinity.tradefinanceservices.dto.IssuedGuaranteeClaimsDTO;
import com.temenos.infinity.tradefinanceservices.utils.ExcelBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

import static com.temenos.infinity.tradefinanceservices.utils.PdfGenerator.*;

public class IssuedGuaranteeClaimsBusinessDelegateImpl implements IssuedGuaranteeClaimsBusinessDelegate, ExcelBusinessDelegate, TradeFinanceConstants {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    IssuedGuaranteeClaimsBackendDelegate claimsBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(
            IssuedGuaranteeClaimsBackendDelegate.class);

    @Override
    public IssuedGuaranteeClaimsDTO createClaim(IssuedGuaranteeClaimsDTO guaranteeClaimsDTO, DataControllerRequest request) {
        return claimsBackendDelegate.createClaim(guaranteeClaimsDTO, request);
    }

    public IssuedGuaranteeClaimsDTO getClaimById(String claimsSRMSId, DataControllerRequest request) {
        return claimsBackendDelegate.getClaimById(claimsSRMSId, request);
    }

    public List<IssuedGuaranteeClaimsDTO> getClaims(DataControllerRequest request) {
        return claimsBackendDelegate.getClaims(request);
    }

    @Override
    public List<IssuedGuaranteeClaimsDTO> getList(DataControllerRequest request) throws ApplicationException {
        return claimsBackendDelegate.getClaims(request);
    }

    public IssuedGuaranteeClaimsDTO updateClaim(IssuedGuaranteeClaimsDTO guaranteeClaimsDTO, IssuedGuaranteeClaimsDTO previousGuaranteeClaimsDTO, boolean isMergeRequired, DataControllerRequest request) {
        return claimsBackendDelegate.updateClaim(guaranteeClaimsDTO, previousGuaranteeClaimsDTO, isMergeRequired, request);
    }

    public byte[] generateIssuedGuaranteeClaim(IssuedGuaranteeClaimsDTO claimsDTO, GuaranteesDTO guaranteeDTO, DataControllerRequest request) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        Document document = new Document();
        document.setPageSize(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, outStream);
        addHeadersAndFootersPDF(writer, document, "Claim Received", true);

        try {
            document.open();

            document.add(addCustomerDetailsPDF(request));
            document.add(_issuanceSummaryDetails(guaranteeDTO));
            document.add(_claimDetails(claimsDTO, request));
            if (StringUtils.isNotBlank(claimsDTO.getDocuments())) {
                document.add(_documentDetails(claimsDTO, request));
            }
            if (StringUtils.isNotBlank(claimsDTO.getPaymentStatus())) {
                document.add(_paymentDetails(claimsDTO, request));
            }
        } catch (Exception e) {
            alert.prepareError("Error occurred while creating pdf ", e).log();
            return null;
        } finally {
            document.close();
        }

        return outStream.toByteArray();
    }

    public PdfPTable _issuanceSummaryDetails(GuaranteesDTO guaranteeDTO) {
        PdfPTable table = new PdfPTable(2);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(_noBorderHeadingCell("Guarantee & Standby LC Summary", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Beneficiary:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(guaranteeDTO.getBeneficiaryName()) ? guaranteeDTO.getBeneficiaryName() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Transaction Ref:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(guaranteeDTO.getGuaranteesSRMSId()) ? guaranteeDTO.getGuaranteesSRMSId() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Product Type:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(guaranteeDTO.getProductType()) ? guaranteeDTO.getProductType() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("GT & SBLC Type:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(guaranteeDTO.getGuaranteeAndSBLCType()) ? guaranteeDTO.getGuaranteeAndSBLCType() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Amount:", lightFont));
        if (StringUtils.isNotBlank(guaranteeDTO.getAmount()) && StringUtils.isNotBlank(guaranteeDTO.getCurrency())) {
            table.addCell(_noBorderCell(guaranteeDTO.getCurrency() + " " + TradeFinanceCommonUtils.formatAmount(guaranteeDTO.getAmount()), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }
        table.completeRow();

        table.addCell(_noBorderCell("Issue Date:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(guaranteeDTO.getIssueDate()) ? guaranteeDTO.getIssueDate() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Expiry Type:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(guaranteeDTO.getExpiryType()) ? guaranteeDTO.getExpiryType() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Expiry Date:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(guaranteeDTO.getExpiryDate()) ? guaranteeDTO.getExpiryDate() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Advising Bank:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(guaranteeDTO.getAdvisingBank()) ? guaranteeDTO.getAdvisingBank() : "NA", darkFont));
        table.completeRow();

        return table;
    }

    public PdfPTable _claimDetails(IssuedGuaranteeClaimsDTO claimsDTO, DataControllerRequest request) {
        PdfPTable table = new PdfPTable(2);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(_noBorderHeadingCell("Claim Details", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Claim Status:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getClaimStatus()) ? claimsDTO.getClaimStatus() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Claim Ref No:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getClaimsSRMSId()) ? claimsDTO.getClaimsSRMSId() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Claim Type:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getClaimType()) ? claimsDTO.getClaimType() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Claim Amount:", lightFont));
        if (StringUtils.isNotBlank(claimsDTO.getClaimAmount()) && StringUtils.isNotBlank(claimsDTO.getClaimCurrency())) {
            table.addCell(_noBorderCell(claimsDTO.getClaimCurrency() + " " + TradeFinanceCommonUtils.formatAmount(claimsDTO.getClaimAmount()), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }
        table.completeRow();

        table.addCell(_noBorderCell("Received On:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getReceivedOn()) ? claimsDTO.getReceivedOn() : "NA", darkFont));
        table.completeRow();

        table.addCell(_noBorderCell("Expected Settlement Date:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getExpectedSettlementDate()) ? claimsDTO.getExpectedSettlementDate() : "NA", darkFont));
        table.completeRow();

        if (StringUtils.isNotBlank(claimsDTO.getClaimType()) && claimsDTO.getClaimType().equals(PARAM_DEMAND)) {
            table.addCell(_noBorderCell("Demand Type:", lightFont));
            table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getDemandType()) ? claimsDTO.getDemandType() : "NA", darkFont));
            table.completeRow();


            if (StringUtils.isNotBlank(claimsDTO.getDemandType()) && claimsDTO.getDemandType().equals(PARAM_PAY_OR_EXTEND)) {
                table.addCell(_noBorderCell("New Settlement Date:", lightFont));
                table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getNewExtensionDate()) ? claimsDTO.getNewExtensionDate() : "NA", darkFont));
                table.completeRow();
            }
            table.addCell(_noBorderCell("Documents:", lightFont));
            if (StringUtils.isNotBlank(claimsDTO.getDocuments())) {
                JSONArray documents = new JSONArray(claimsDTO.getDocuments());
                String docName = "";
                for (int i = 0; i < documents.length(); i++) {
                    docName += documents.getJSONObject(i).getString("documentName") + "\n";
                }
                table.addCell(_noBorderCell(docName, darkFont));
            } else {
                table.addCell(_noBorderCell("NA", darkFont));
            }
            table.completeRow();

            table.addCell(_noBorderCell("Other Demand Details:", lightFont));
            table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getOtherDemandDetails()) ? claimsDTO.getOtherDemandDetails() : "NA", darkFont));
            table.completeRow();
        } else if (StringUtils.isNotBlank(claimsDTO.getClaimType()) && claimsDTO.getClaimType().equals(PARAM_PRESENTATION)) {
            table.addCell(_noBorderCell("Presentation Details:", lightFont));
            table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getPresentationDetails()) ? claimsDTO.getPresentationDetails() : "NA", darkFont));
            table.completeRow();
        }
        table.addCell(_noBorderCell("Message from Bank:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getMessageFromBank()) ? claimsDTO.getMessageFromBank() : "NA", darkFont));
        table.completeRow();

        return table;
    }

    public PdfPTable _documentDetails(IssuedGuaranteeClaimsDTO claimsDTO, DataControllerRequest request) {
        PdfPTable table = new PdfPTable(2);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);

        if (StringUtils.isNotBlank(claimsDTO.getClaimType()) && claimsDTO.getClaimType().equals(PARAM_DEMAND)) {

            table.addCell(_noBorderHeadingCell("Demand Details", darkFont));
            table.completeRow();

            table.addCell(_noBorderCell("Applicant Acceptance:", lightFont));
            table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getClaimAcceptance()) ? claimsDTO.getClaimAcceptance() : "NA", darkFont));
            table.completeRow();

            if (StringUtils.isNotBlank(claimsDTO.getClaimAcceptance()) && claimsDTO.getClaimAcceptance().equals(PARAM_ACCEPTED_TO_PAY)) {
                table.addCell(_noBorderCell("Amount to debited from:", lightFont));
                String maskedDebitAccount = "NA";
                if (StringUtils.isNotBlank(claimsDTO.getDebitAccount())) {
                    Map<String, Object> customer = CustomerSession.getCustomerMap(request);
                    maskedDebitAccount = TradeFinanceCommonUtils.getMaskedAccountDetails(CustomerSession.getCustomerId(customer), claimsDTO.getDebitAccount());
                }
                table.addCell(_noBorderCell(maskedDebitAccount, darkFont));
                table.completeRow();

                table.addCell(_noBorderCell("Request Overdraft:", lightFont));
                table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getRequestedOverdraft()) ? claimsDTO.getRequestedOverdraft() : "NA", darkFont));
                table.completeRow();
            }
            if (StringUtils.isNotBlank(claimsDTO.getClaimAcceptance()) && claimsDTO.getClaimAcceptance().equals(PARAM_ACCEPTED_TO_EXTEND)) {

                table.addCell(_noBorderCell("New Extension Date:", lightFont));
                table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getNewExtensionDate()) ? claimsDTO.getNewExtensionDate() : "NA", darkFont));
                table.completeRow();
            }
            if (StringUtils.isNotBlank(claimsDTO.getClaimAcceptance()) && claimsDTO.getClaimAcceptance().equals(PARAM_STATUS_REJECTED)) {

                table.addCell(_noBorderCell("Reason for Rejection:", lightFont));
                table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getReasonForRejection()) ? claimsDTO.getReasonForRejection() : "NA", darkFont));
                table.completeRow();
            }
        } else if (StringUtils.isNotBlank(claimsDTO.getClaimType()) && claimsDTO.getClaimType().equals(PARAM_PRESENTATION)) {

            table.addCell(_noBorderHeadingCell("Presentation Details", darkFont));
            table.completeRow();

            JSONArray documents = StringUtils.isNotBlank(claimsDTO.getDocuments()) ? new JSONArray(claimsDTO.getDocuments()) : new JSONArray();
            table.addCell(_noBorderCell("Total Documents:", lightFont));
            table.addCell(_noBorderCell(String.valueOf(documents.length()), darkFont));
            table.completeRow();

            table.addCell(_noBorderCell("Documents:", lightFont));
            if (StringUtils.isNotBlank(claimsDTO.getDocuments())) {
                String docName = "";
                for (int i = 0; i < documents.length(); i++) {
                    docName += documents.getJSONObject(i).getString("documentName") + "\n";
                }
                table.addCell(_noBorderCell(docName, darkFont));
            } else {
                table.addCell(_noBorderCell("NA", darkFont));
            }
            table.completeRow();

            table.addCell(_noBorderCell("Document Status:", lightFont));
            table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getDocumentStatus()) ? claimsDTO.getDocumentStatus() : "NA", darkFont));
            table.completeRow();

            if (StringUtils.isNotBlank(claimsDTO.getDocumentStatus()) && claimsDTO.getDocumentStatus().equals(PARAM_DISCREPANT) && StringUtils.isNotBlank(claimsDTO.getDiscrepancyDetails())) {
                JSONArray discrepancies = new JSONArray(claimsDTO.getDiscrepancyDetails());
                for (int i = 0; i < discrepancies.length(); i++) {
                    table.addCell(_noBorderCell("Discrepancy " + (i + 1) + ":", lightFont));
                    table.addCell(_noBorderCell(discrepancies.getString(i), darkFont));
                    table.completeRow();
                }
            }
            if (StringUtils.isNotBlank(claimsDTO.getDiscrepancyAcceptance())) {
                table.addCell(_noBorderCell("Applicant Acceptance:", lightFont));
                table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getDiscrepancyAcceptance()) ? claimsDTO.getDiscrepancyAcceptance() : "NA", darkFont));
                table.completeRow();
            }
            if (StringUtils.isNotBlank(claimsDTO.getDiscrepancyAcceptance()) && claimsDTO.getDiscrepancyAcceptance().equals(PARAM_STATUS_REJECTED)) {
                table.addCell(_noBorderCell("Reason for Rejection:", lightFont));
                table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getReasonForRejection()) ? claimsDTO.getReasonForRejection() : "NA", darkFont));
                table.completeRow();
            } else {
                table.addCell(_noBorderCell("Amount to debited from:", lightFont));
                String maskedDebitAccount = "NA";
                if (StringUtils.isNotBlank(claimsDTO.getDebitAccount())) {
                    Map<String, Object> customer = CustomerSession.getCustomerMap(request);
                    maskedDebitAccount = TradeFinanceCommonUtils.getMaskedAccountDetails(CustomerSession.getCustomerId(customer), claimsDTO.getDebitAccount());
                }
                table.addCell(_noBorderCell(maskedDebitAccount, darkFont));
                table.completeRow();

                table.addCell(_noBorderCell("Request Overdraft:", lightFont));
                table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getRequestedOverdraft()) ? claimsDTO.getRequestedOverdraft() : "NA", darkFont));
                table.completeRow();
            }


        }
        table.addCell(_noBorderCell("Message to Bank:", lightFont));
        table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getMessageToBank()) ? claimsDTO.getMessageToBank() : "NA", darkFont));
        table.completeRow();

        return table;
    }

    public PdfPTable _paymentDetails(IssuedGuaranteeClaimsDTO claimsDTO, DataControllerRequest request) {
        PdfPTable table = new PdfPTable(2);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(_noBorderHeadingCell("Payment Details", darkFont));
        table.completeRow();

        if (StringUtils.isNotBlank(claimsDTO.getClaimStatus()) && claimsDTO.getClaimStatus().equals(PARAM_STATUS_REJECTED)) {
            table.addCell(_noBorderCell("Payment Status:", lightFont));
            table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getPaymentStatus()) ? claimsDTO.getPaymentStatus() : "NA", darkFont));
            table.completeRow();

            table.addCell(_noBorderCell("Rejected Date:", lightFont));
            table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getRejectedDate()) ? claimsDTO.getRejectedDate() : "NA", darkFont));
            table.completeRow();

            table.addCell(_noBorderCell("Reason for Rejection:", lightFont));
            table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getReasonForRejection()) ? claimsDTO.getReasonForRejection() : "NA", darkFont));
            table.completeRow();
        }
        if (StringUtils.isNotBlank(claimsDTO.getClaimStatus()) && claimsDTO.getClaimStatus().equals(PARAM_STATUS_CLAIMHONOURED)) {

            table.addCell(_noBorderCell("Payment Status:", lightFont));
            table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getPaymentStatus()) ? claimsDTO.getPaymentStatus() : "NA", darkFont));
            table.completeRow();

            table.addCell(_noBorderCell("Settled Date:", lightFont));
            table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getSettledDate()) ? claimsDTO.getSettledDate() : "NA", darkFont));
            table.completeRow();

            table.addCell(_noBorderCell("Total Amount To Be Paid:", lightFont));
            if (StringUtils.isNotBlank(claimsDTO.getClaimAmount()) && StringUtils.isNotBlank(claimsDTO.getClaimCurrency())) {
                table.addCell(_noBorderCell(claimsDTO.getClaimCurrency() + " " + TradeFinanceCommonUtils.formatAmount(claimsDTO.getClaimAmount()), darkFont));
            } else {
                table.addCell(_noBorderCell("NA", darkFont));
            }
            table.completeRow();

            table.addCell(_noBorderCell("Claim Debit Account:", lightFont));
            String maskedDebitAccount = "NA";
            if (StringUtils.isNotBlank(claimsDTO.getDebitAccount())) {
                Map<String, Object> customer = CustomerSession.getCustomerMap(request);
                maskedDebitAccount = TradeFinanceCommonUtils.getMaskedAccountDetails(CustomerSession.getCustomerId(customer), claimsDTO.getDebitAccount());
            }
            table.addCell(_noBorderCell(maskedDebitAccount, darkFont));
            table.completeRow();
        }
        if (StringUtils.isNotBlank(claimsDTO.getClaimStatus()) && (claimsDTO.getClaimStatus().equals(PARAM_STATUS_CLAIMHONOURED_APPLICANTREJECTED) || claimsDTO.getClaimStatus().equals(PARAM_STATUS_CLAIMHONOURED_PENDINGCONSENT))) {

            table.addCell(_noBorderCell("Payment Status:", lightFont));
            table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getPaymentStatus()) ? claimsDTO.getPaymentStatus() : "NA", darkFont));
            table.completeRow();

            table.addCell(_noBorderCell("Settled Date:", lightFont));
            table.addCell(_noBorderCell(StringUtils.isNotBlank(claimsDTO.getSettledDate()) ? claimsDTO.getSettledDate() : "NA", darkFont));
            table.completeRow();

            table.addCell(_noBorderCell("Total Amount To Be Paid:", lightFont));
            if (StringUtils.isNotBlank(claimsDTO.getClaimAmount()) && StringUtils.isNotBlank(claimsDTO.getClaimCurrency())) {
                table.addCell(_noBorderCell(claimsDTO.getClaimCurrency() + " " + TradeFinanceCommonUtils.formatAmount(claimsDTO.getClaimAmount()), darkFont));
            } else {
                table.addCell(_noBorderCell("NA", darkFont));
            }
            table.completeRow();
        }
        return table;
    }
}
