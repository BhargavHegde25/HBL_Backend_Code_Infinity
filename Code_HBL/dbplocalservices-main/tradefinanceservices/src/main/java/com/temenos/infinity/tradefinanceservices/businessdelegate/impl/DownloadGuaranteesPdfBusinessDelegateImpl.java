/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.impl;


import com.konylabs.middleware.controller.DataControllerRequest;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.DownloadGuaranteesPdfBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.dto.GuaranteesDTO;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.darkFont;
import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.lightFont;
import static com.temenos.infinity.tradefinanceservices.utils.PdfGenerator.*;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.*;

public class DownloadGuaranteesPdfBusinessDelegateImpl implements DownloadGuaranteesPdfBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final String CHARGE = "CHARGE";
    private static final String COMMISSION = "COMMISSION";
    private static final String CASH_MARGIN = "CASH MARGIN";

    @Override
    public byte[] generateGuaranteesPdf(GuaranteesDTO guaranteesDTO, DataControllerRequest request) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter pdfWriter = PdfWriter.getInstance(document, os);
        addHeadersAndFootersPDF(pdfWriter, document, "Issued Guarantees & SBLC", true);

        try {
            document.open();

            document.add(addCustomerDetailsPDF(request));
            document.add(_issuanceSummary(guaranteesDTO));
            document.add(_productDetails(guaranteesDTO));
            document.add(_transactionDetails(guaranteesDTO));
            document.add(_beneficiaryAndAdvisingBankDetails(guaranteesDTO));
            document.add(_bankInstructionsAndDocuments(guaranteesDTO, request));
            document.add(_clausesAndConditions(guaranteesDTO));

            document.close();
            return os.toByteArray();
        } catch (Exception e) {
            alert.prepareError("Error occurred while creating pdf ", e).log();
            return null;
        } finally {
            try {
                os.close();
            } catch (Exception e) {
                alert.prepareError("Error occurred while closing input stream. " + e).log();
            }
        }
    }

    //Adding Guarantees Data
    private PdfPTable _issuanceSummary(GuaranteesDTO guaranteesDTO) {

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(_noBorderHeadingCell("Issuance Summary", darkFont));

        table.addCell(_noBorderCell("Transaction Reference:", lightFont));
        if (StringUtils.isNotBlank(guaranteesDTO.getGuaranteesSRMSId())) {
            table.addCell(_noBorderCell(guaranteesDTO.getGuaranteesSRMSId(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Created On:", lightFont));
        if (StringUtils.isNotBlank(guaranteesDTO.getCreatedOn())) {
            table.addCell(_noBorderCell(_formatDate(guaranteesDTO.getCreatedOn()), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Created By:", lightFont));
        if (StringUtils.isNotBlank(guaranteesDTO.getBeneficiaryName())) {
            table.addCell(_noBorderCell(guaranteesDTO.getBeneficiaryName(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Issuance Status:", lightFont));
        if (StringUtils.isNotBlank(guaranteesDTO.getStatus())) {
            table.addCell(_noBorderCell(guaranteesDTO.getStatus(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }
        return table;
    }

    public PdfPTable _productDetails(GuaranteesDTO guaranteesDTO) {

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(_noBorderHeadingCell("Product Details", darkFont));

        table.addCell(_noBorderCell("Product Type:", lightFont));
        if (StringUtils.isNotBlank(guaranteesDTO.getProductType())) {
            table.addCell(_noBorderCell(guaranteesDTO.getProductType(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("GuaranteeAndSBLCType:", lightFont));
        if (StringUtils.isNotBlank(guaranteesDTO.getGuaranteeAndSBLCType())) {
            table.addCell(_noBorderCell(guaranteesDTO.getGuaranteeAndSBLCType(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Related Transaction Reference:", lightFont));
        if (StringUtils.isNotBlank(guaranteesDTO.getGuaranteesReferenceNo())) {
            table.addCell(_noBorderCell(guaranteesDTO.getGuaranteesReferenceNo(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Mode of Transaction:", lightFont));
        if (StringUtils.isNotBlank(guaranteesDTO.getModeOfTransaction())) {
            table.addCell(_noBorderCell(guaranteesDTO.getModeOfTransaction(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Instructing Party:", lightFont));
        if (StringUtils.isNotBlank(guaranteesDTO.getInstructingParty())) {
            table.addCell(_noBorderCell(guaranteesDTO.getInstructingParty(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Applicant Party (Optional):", lightFont));
        if (StringUtils.isNotBlank(guaranteesDTO.getApplicantParty())) {
            table.addCell(_noBorderCell(guaranteesDTO.getApplicantParty(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        return table;
    }

    private PdfPTable _transactionDetails(GuaranteesDTO guaranteesDTO) {

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(_noBorderHeadingCell("Transaction Details", darkFont));

        table.addCell(_noBorderCell("Request Amount:", lightFont));
        if (StringUtils.isNotBlank(guaranteesDTO.getAmount())) {
            table.addCell(_noBorderCell(getCurrencySymbol(guaranteesDTO.getCurrency()) + " " + guaranteesDTO.getAmount(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Expected Issue Date (Optional):", lightFont));
        if (StringUtils.isNotBlank(guaranteesDTO.getIssueDate())) {
            table.addCell(_noBorderCell(guaranteesDTO.getIssueDate(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Expiry Type:", lightFont));
        if (StringUtils.isNotBlank(guaranteesDTO.getExpiryType())) {
            table.addCell(_noBorderCell(guaranteesDTO.getExpiryType(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Expiry Date (Optional):", lightFont));
        if (StringUtils.isNotBlank(guaranteesDTO.getExpiryDate())) {
            table.addCell(_noBorderCell(guaranteesDTO.getExpiryDate(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Expiry Conditions (Optional):", lightFont));
        if (StringUtils.isNotBlank(guaranteesDTO.getExpiryCondition())) {
            table.addCell(_noBorderCell(guaranteesDTO.getExpiryCondition(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Auto Extension of Expiry (Optional):", lightFont));
        if (StringUtils.isNotBlank(guaranteesDTO.getExtendExpiryDate())) {
            table.addCell(_noBorderCell(guaranteesDTO.getExtendExpiryDate(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Extension Period:", lightFont));
        if (StringUtils.isNotBlank(guaranteesDTO.getExtensionCapPeriod())) {
            table.addCell(_noBorderCell(guaranteesDTO.getExtensionCapPeriod(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Extension Cap Period (Optional):", lightFont));
        if (StringUtils.isNotBlank(guaranteesDTO.getExtensionCapPeriod())) {
            table.addCell(_noBorderCell(guaranteesDTO.getExtensionCapPeriod(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Notification Period (Optional):", lightFont));
        if (StringUtils.isNotBlank(guaranteesDTO.getNotificationPeriod())) {
            table.addCell(_noBorderCell(guaranteesDTO.getNotificationPeriod(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Extension Details (Optional):", lightFont));
        if (StringUtils.isNotBlank(guaranteesDTO.getExtensionDetails())) {
            table.addCell(_noBorderCell(guaranteesDTO.getExtensionDetails(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Governing Law (Optional):", lightFont));
        if (StringUtils.isNotBlank(guaranteesDTO.getGoverningLaw())) {
            table.addCell(_noBorderCell(guaranteesDTO.getGoverningLaw(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Delivery Instructions (Optional):", lightFont));
        if (StringUtils.isNotBlank(guaranteesDTO.getDeliveryInstructions())) {
            table.addCell(_noBorderCell(guaranteesDTO.getDeliveryInstructions(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Other Instructions (Optional):", lightFont));
        if (StringUtils.isNotBlank(guaranteesDTO.getOtherInstructions())) {
            table.addCell(_noBorderCell(guaranteesDTO.getOtherInstructions(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        return table;
    }

    private PdfPTable _beneficiaryAndAdvisingBankDetails(GuaranteesDTO guaranteesDTO) {

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(_noBorderHeadingCell("Beneficiary & Advising Bank Details", darkFont));

        if (StringUtils.isNotBlank(guaranteesDTO.getBeneficiaryDetails())) {
            JSONArray beneficiaryArray = new JSONArray(guaranteesDTO.getBeneficiaryDetails());

            for (int i = 0; i < beneficiaryArray.length(); i++) {
                JSONObject payeeObject = beneficiaryArray.getJSONObject(i);
                table.addCell(_noBorderHeadingCell("Beneficiary " + (i + 1), lightFont));

                table.addCell(_noBorderCell("Beneficiary Name:", lightFont));
                if (payeeObject.getString("beneficiaryName") != null && StringUtils.isNotBlank(payeeObject.getString("beneficiaryName"))) {
                    table.addCell(_noBorderCell(payeeObject.getString("beneficiaryName"), darkFont));
                } else {
                    table.addCell(_noBorderCell("NA", darkFont));
                }

                String address = "";
                if (payeeObject.getString("address1") != null && StringUtils.isNotBlank(payeeObject.getString("address1"))) {
                    address = payeeObject.getString("address1");
                }
                if (payeeObject.getString("address2") != null && StringUtils.isNotBlank(payeeObject.getString("address2"))) {
                    address = address + " " + payeeObject.getString("address2");
                }
                if (payeeObject.getString("city") != null && StringUtils.isNotBlank(payeeObject.getString("city"))) {
                    address = address + " " + payeeObject.getString("city");
                }
                if (payeeObject.getString("state") != null && StringUtils.isNotBlank(payeeObject.getString("state"))) {
                    address = address + " " + payeeObject.getString("state");
                }
                if (payeeObject.getString("country") != null && StringUtils.isNotBlank(payeeObject.getString("country"))) {
                    address = address + " " + payeeObject.getString("country");
                }
                if (payeeObject.getString("zipcode") != null && StringUtils.isNotBlank(payeeObject.getString("zipcode"))) {
                    address = address + ", " + payeeObject.getString("zipcode");
                }

                table.addCell(_noBorderCell("Beneficiary Address (Optional):", lightFont));
                if (StringUtils.isNotBlank(address)) {
                    table.addCell(_noBorderCell(address, darkFont));
                } else {
                    table.addCell(_noBorderCell("NA", darkFont));
                }
            }
        } else {
            table.addCell(_noBorderHeadingCell("Beneficiary", lightFont));
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderHeadingCell("Advising Bank Details (Optional)", lightFont));
        table.addCell(_noBorderCell("Bank Name:", lightFont));
        if (StringUtils.isNotBlank(guaranteesDTO.getBankName())) {
            table.addCell(_noBorderCell(guaranteesDTO.getBankName(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Swift/BIC Code:", lightFont));
        if (StringUtils.isNotBlank(guaranteesDTO.getSwiftCode())) {
            table.addCell(_noBorderCell(guaranteesDTO.getSwiftCode(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("IBAN:", lightFont));
        if (StringUtils.isNotBlank(guaranteesDTO.getIban())) {
            table.addCell(_noBorderCell(guaranteesDTO.getIban(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Local Code:", lightFont));
        if (StringUtils.isNotBlank(guaranteesDTO.getLocalCode())) {
            table.addCell(_noBorderCell(guaranteesDTO.getLocalCode(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        String address = "";

        if (StringUtils.isNotBlank(guaranteesDTO.getBankAddress1())) {
            address = guaranteesDTO.getBankAddress1();
        }
        if (StringUtils.isNotBlank(guaranteesDTO.getBankAddress2())) {
            address = address + " " + guaranteesDTO.getBankAddress2();
        }
        if (StringUtils.isNotBlank(guaranteesDTO.getBankCity())) {
            address = address + " " + guaranteesDTO.getBankCity();
        }
        if (StringUtils.isNotBlank(guaranteesDTO.getBankState())) {
            address = address + " " + guaranteesDTO.getBankState();
        }
        if (StringUtils.isNotBlank(guaranteesDTO.getBankCountry())) {
            address = address + " " + guaranteesDTO.getBankCountry();
        }
        if (StringUtils.isNotBlank(guaranteesDTO.getBankZipCode())) {
            address = address + ", " + guaranteesDTO.getBankZipCode();
        }

        table.addCell(_noBorderCell("Bank Address:", lightFont));
        if (StringUtils.isNotBlank(address)) {
            table.addCell(_noBorderCell(address, darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        return table;
    }

    private PdfPTable _bankInstructionsAndDocuments(GuaranteesDTO guaranteesDTO, DataControllerRequest request) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(_noBorderHeadingCell("Bank Instructions & Documents", darkFont));
        table.addCell(_noBorderHeadingCell("Settlement Account (Optional)", lightFont));

        String chargeAccount = null;
        String commissionAccount = null;
        String cashMarginAccount = null;

        if (StringUtils.isNotBlank(guaranteesDTO.getInstructionCurrencies())) {
            JSONArray accountDetails = new JSONArray(guaranteesDTO.getInstructionCurrencies());
            for (int i = 0; i < accountDetails.length(); i++) {
                JSONObject accountObject = accountDetails.getJSONObject(i);
                if (accountObject != null && accountObject.getString("accountType").equalsIgnoreCase(CHARGE)) {
                    chargeAccount = getMaskedAccountDetails(fetchCustomerFromSession(request), accountObject.getString("account"));
                }
                if (accountObject != null && accountObject.getString("accountType").equalsIgnoreCase(COMMISSION)) {
                    commissionAccount = getMaskedAccountDetails(fetchCustomerFromSession(request), accountObject.getString("account"));
                }
                if (accountObject != null && accountObject.getString("accountType").equalsIgnoreCase(CASH_MARGIN)) {
                    cashMarginAccount = getMaskedAccountDetails(fetchCustomerFromSession(request), accountObject.getString("account"));
                }
            }
        }

        table.addCell(_noBorderCell("Currency:", lightFont));
        if (StringUtils.isNotBlank(guaranteesDTO.getCurrency())) {
            table.addCell(_noBorderCell(guaranteesDTO.getCurrency() + " - " + getCurrencySymbol(guaranteesDTO.getCurrency()), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Charge Account:", lightFont));
        if (StringUtils.isNotBlank(chargeAccount)) {
            table.addCell(_noBorderCell(chargeAccount, darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Commission Account:", lightFont));
        if (StringUtils.isNotBlank(commissionAccount)) {
            table.addCell(_noBorderCell(commissionAccount, darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Cash Margin Account:", lightFont));
        if (StringUtils.isNotBlank(cashMarginAccount)) {
            table.addCell(_noBorderCell(cashMarginAccount, darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }


        table.addCell(_noBorderHeadingCell("Additional Details", lightFont));
        table.addCell(_noBorderCell("Limit Instructions (Optional):", lightFont));
        if (StringUtils.isNotBlank(guaranteesDTO.getLimitInstructions())) {
            JSONObject limitInstructions = new JSONObject(guaranteesDTO.getLimitInstructions());
            table.addCell(_noBorderCell(limitInstructions.getString("Limit iD") + " - " + limitInstructions.getString("Limit Currency") + " - " + limitInstructions.getString("Os Amount"), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Other Instructions (Optional):", lightFont));
        if (StringUtils.isNotBlank(guaranteesDTO.getOtherInstructions())) {
            table.addCell(_noBorderCell(guaranteesDTO.getOtherInstructions(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderCell("Message to Bank (Optional):", lightFont));
        if (StringUtils.isNotBlank(guaranteesDTO.getMessageToBank())) {
            table.addCell(_noBorderCell(guaranteesDTO.getMessageToBank(), darkFont));
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }

        table.addCell(_noBorderHeadingCell("Supporting Documents", lightFont));
        table.addCell(_noBorderCell("Uploaded files:", lightFont));
        if (StringUtils.isNotBlank(guaranteesDTO.getDocumentName())) {
            JSONArray documents = new JSONArray(guaranteesDTO.getDocumentName());
            String docName = documents.join("\n");
            if (StringUtils.isNotBlank(docName)) {
                table.addCell(_noBorderCell(docName.replace('"', ' '), darkFont));
            } else {
                table.addCell(_noBorderCell("NA", darkFont));
            }
        }
        return table;
    }

    private PdfPTable _clausesAndConditions(GuaranteesDTO guaranteesDTO) {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        table.addCell(_noBorderHeadingCell("Clauses & Conditions", darkFont));

        if (StringUtils.isNotBlank(guaranteesDTO.getClauseConditions())) {
            JSONArray clauseAndConditionsArray = new JSONArray(guaranteesDTO.getClauseConditions());
            for (int i = 0; i < clauseAndConditionsArray.length(); i++) {
                JSONObject clause = clauseAndConditionsArray.getJSONObject(i);
                if (clause.get("clauseDescription") != null && StringUtils.isNotBlank(clause.getString("clauseDescription"))) {
                    table.addCell(_noBorderCell(clause.getString("clauseDescription"), darkFont));
                } else {
                    table.addCell(_noBorderCell("NA", darkFont));
                }
            }
        } else {
            table.addCell(_noBorderCell("NA", darkFont));
        }
        return table;
    }
}