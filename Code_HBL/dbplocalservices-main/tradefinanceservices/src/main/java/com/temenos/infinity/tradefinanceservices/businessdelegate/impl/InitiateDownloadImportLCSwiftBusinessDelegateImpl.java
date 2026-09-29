/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.infinity.dbx.temenos.transactions.TransactionConstants;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbputilities.fileutil.PDFGenerator;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.lowagie.text.*;
import com.lowagie.text.html.WebColors;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.GetLetterOfCreditsByIdBackendDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.InitiateDownloadImportLCSwiftBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.dto.LetterOfCreditsDTO;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.*;

public class InitiateDownloadImportLCSwiftBusinessDelegateImpl implements InitiateDownloadImportLCSwiftBusinessDelegate {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public byte[] generateImportLCSwiftPdf(String srmsRequestId, DataControllerRequest request) throws IOException {

        GetLetterOfCreditsByIdBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(GetLetterOfCreditsByIdBackendDelegate.class);
        LetterOfCreditsDTO letterOfCredits;
        try {
            letterOfCredits = backendDelegate.getImportLCById(request.getParameter("srmsReqOrderID"), request);
            if (StringUtils.isNotBlank(letterOfCredits.getErrorMessage()) || StringUtils.isNotBlank(letterOfCredits.getErrorCode())) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Document document = new Document();
        document.setPageSize(PageSize.A4);
        PdfWriter.getInstance(document, os);
        addSwiftHeadersAndFootersPDF(document);
        try {
            document.open();
            document.add(_messageDetais());
            document.add(lcInfo(letterOfCredits, request));
        } catch (Exception e) {
            alert.prepareError("Error occurred while creating pdf ", e).log();
            return null;
        } finally {
            os.close();
            document.close();
        }
        return os.toByteArray();
    }

    boolean isEuropianGeography = com.kony.dbputilities.util.HelperMethods.isEuropieanGeography();
    NumberFormat amountFormat = isEuropianGeography ? NumberFormat.getInstance(new Locale("da", "DK"))
            : NumberFormat.getInstance(new Locale("en", "US"));

    public PdfPTable _messageDetais() {

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.addCell(_noBorderHeadingCell("Incoming Message's Tag Details", darkerFont));

        table.addCell(_noBorderCell("Message Key", darkerFont));
        table.addCell(_noBorderCell("R2022010968244-1408501                ISSUE GUARANTEES & STANDBY LC", darkerFont));

        table.addCell(_noBorderCell("Message Type", darkerFont));
        table.addCell(_noBorderCell("760", darkerFont));

        table.addCell(_noBorderCell("Receiver Date And Time", darkerFont));
        table.addCell(_noBorderCell("20220109 03:54:45", darkerFont));

        table.addCell(_noBorderCell("Sender", darkerFont));
        table.addCell(_noBorderCell("AEIBSGSGXXXX", darkerFont));

        table.addCell(_noBorderCell("Receiver", darkerFont));
        table.addCell(_noBorderCell("DEMOGBPXAXXX", darkerFont));

        return table;
    }

    public PdfPTable lcInfo(LetterOfCreditsDTO lcDto, DataControllerRequest request) {
        JSONObject bundleConfig = TemenosUtils.getBundleConfigurations(TransactionConstants.DBP_BUNDLE,
                "SWIFT_TAG", request);
        String swiftEnable = bundleConfig.get("configurations").toString();
        JSONArray swift = new JSONArray(swiftEnable);
        JSONObject swiftBundle = (JSONObject) swift.get(0);
        String swiftEnable2 = swiftBundle.get("config_value").toString();
        JSONObject s = new JSONObject(swiftEnable2);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(_noBorderCell("Message", darkFont, WebColors.getRGBColor("#e3e3e3")));
        table.addCell(_noBorderCell("", darkFont, WebColors.getRGBColor("#e3e3e3")));

        table.addCell(
                _noBorderCell("{1:F01L20AXXX0001000001}{2:I760AABBCC0XXXXN}{3:{108:XXX988033452878}}{4:", darkFont, 6));

        table.addCell(_noBorderCell("", darkFont));

        table.addCell(_noBorderCell(":" + s.get("sequenceOfTotal") + ":       Sequence of Total", darkFont, WebColors.getRGBColor("#f6f7ef")));
        table.addCell(_noBorderCell("1/1", lightFont, WebColors.getRGBColor("#f6f7ef")));

        table.addCell(_noBorderCell(":" + s.get("paymentTerms") + ":       Form Of Documentary Credit:", darkFont));
        table.addCell(_noBorderCell("NA", lightFont));

        table.addCell(_noBorderCell(":" + s.get("referenceNumber") + ":       Transaction Reference Number:", darkFont, WebColors.getRGBColor("#f6f7ef")));
        if (StringUtils.isNotBlank(lcDto.getLcReferenceNo())) {
            table.addCell(_noBorderCell((lcDto.getLcReferenceNo()), lightFont, WebColors.getRGBColor("#f6f7ef")));
        } else {
            table.addCell(_noBorderCell("N/A", lightFont, WebColors.getRGBColor("#f6f7ef")));
        }

        table.addCell(_noBorderCell(":" + s.get("issueDate") + ":       Date Of Issue:", darkFont));
        if (StringUtils.isNotBlank(lcDto.getIssueDate())) {
            table.addCell(_noBorderCell(_formatDate(lcDto.getIssueDate()), lightFont));
        } else {
            table.addCell(_noBorderCell("NA", lightFont));
        }

        table.addCell(_noBorderCell(":" + s.get("applicableRules") + ":     Applicable Rules:", darkFont, WebColors.getRGBColor("#f6f7ef")));
        table.addCell(_noBorderCell("N/A", lightFont, WebColors.getRGBColor("#f6f7ef")));

        table.addCell(_noBorderCell(":" + s.get("expiryDate") + ":       Date and Place of Expiry:", darkFont));
        if (StringUtils.isNotBlank(lcDto.getExpiryDate()) && StringUtils.isNotBlank(lcDto.getExpiryPlace())) {
            table.addCell(_noBorderCell((_formatDate(lcDto.getExpiryDate())) + "\n" + (lcDto.getExpiryPlace()), lightFont));
        } else {
            table.addCell(_noBorderCell("NA", lightFont));
        }

        table.addCell(_noBorderCell(":" + s.get("applicant") + ":       Applicant:", darkFont, WebColors.getRGBColor("#f6f7ef")));
        table.addCell(_noBorderCell("N/A", lightFont, WebColors.getRGBColor("#f6f7ef")));

        table.addCell(_noBorderCell(":" + s.get("beneficiaryName") + ":         Beneficiary:", darkFont));
        if (StringUtils.isNotBlank(lcDto.getBeneficiaryName())) {
            table.addCell(_noBorderCell(lcDto.getBeneficiaryName() + "\n" + (lcDto.getBeneficiaryAddressLine1()) + "\n" + (lcDto.getBeneficiaryCity()) + "\n" + (lcDto.getBeneficiaryState()) + "\n" + (lcDto.getBeneficiaryPostCode()), lightFont));
        } else {
            table.addCell(_noBorderCell("NA", lightFont));
        }

        table.addCell(_noBorderCell(":" + s.get("currencyAmount") + ":     Currency Code,Amount:", darkFont, WebColors.getRGBColor("#f6f7ef")));
        if (StringUtils.isNotBlank(String.valueOf(lcDto.getLcAmount())) && StringUtils.isNotBlank(String.valueOf(lcDto.getLcCurrency()))) {
            table.addCell(_noBorderCell(_getCurrencySymbol(lcDto.getLcCurrency()) + " " + _formatAmount(String.valueOf(lcDto.getLcAmount())), lightFont, WebColors.getRGBColor("#f6f7ef")));
        } else {
            table.addCell(_noBorderCell("N/A", lightFont, WebColors.getRGBColor("#f6f7ef")));
        }

        table.addCell(_noBorderCell(":" + s.get("tolerancePercentage") + ":       Percentage Credit Amount Tolerance:", darkFont));
        if (StringUtils.isNotBlank(lcDto.getTolerancePercentage())) {
            table.addCell(_noBorderCell(lcDto.getTolerancePercentage(), lightFont));
        } else {
            table.addCell(_noBorderCell("NA", lightFont));
        }

        table.addCell(_noBorderCell(":" + s.get("availableWith") + ":     Available With:", darkFont, WebColors.getRGBColor("#f6f7ef")));
        if (StringUtils.isNotBlank(String.valueOf(lcDto.getAvailableWith1()))) {
            table.addCell(_noBorderCell(lcDto.getAvailableWith1() + "\n" + (lcDto.getAvailableWith2()) + "\n" + (lcDto.getAvailableWith3()) + "\n" + (lcDto.getAvailableWith4()), lightFont, WebColors.getRGBColor("#f6f7ef")));
        } else {
            table.addCell(_noBorderCell("N/A", lightFont, WebColors.getRGBColor("#f6f7ef")));
        }

        table.addCell(_noBorderCell(":" + s.get("draftsAt") + ":       Draft At:", darkFont));
        table.addCell(_noBorderCell("NA", lightFont));

        table.addCell(_noBorderCell(":" + s.get("drawee") + ":     Drawee:", darkFont, WebColors.getRGBColor("#f6f7ef")));
        table.addCell(_noBorderCell("N/A", lightFont, WebColors.getRGBColor("#f6f7ef")));

        table.addCell(_noBorderCell(":" + s.get("partialShipment") + ":       Partial Shipment:", darkFont));
        if (StringUtils.isNotBlank(lcDto.getPartialShipments())) {
            table.addCell(_noBorderCell(lcDto.getPartialShipments(), lightFont));
        } else {
            table.addCell(_noBorderCell("NA", lightFont));
        }

        table.addCell(_noBorderCell(":" + s.get("transhipment") + ":     Transhipment:", darkFont, WebColors.getRGBColor("#f6f7ef")));
        if (StringUtils.isNotBlank(lcDto.getTransshipment())) {
            table.addCell(_noBorderCell(lcDto.getTransshipment(), lightFont, WebColors.getRGBColor("#f6f7ef")));
        } else {
            table.addCell(_noBorderCell("N/A", lightFont, WebColors.getRGBColor("#f6f7ef")));
        }

        table.addCell(_noBorderCell(":" + s.get("placeOfTakingInCharge") + ":       Load on Board/Dispatch/Take Charge:", darkFont));
        if (StringUtils.isNotBlank(lcDto.getPlaceOfTakingIncharge())) {
            table.addCell(_noBorderCell(lcDto.getPlaceOfTakingIncharge(), lightFont));
        } else {
            table.addCell(_noBorderCell("NA", lightFont));
        }

        table.addCell(_noBorderCell(":" + s.get("portOfLoading") + ":     Port Of Loading/Airport of:", darkFont, WebColors.getRGBColor("#f6f7ef")));
        if (StringUtils.isNotBlank(lcDto.getPortOfLoading())) {
            table.addCell(_noBorderCell(lcDto.getPortOfLoading(), lightFont, WebColors.getRGBColor("#f6f7ef")));
        } else {
            table.addCell(_noBorderCell("N/A", lightFont, WebColors.getRGBColor("#f6f7ef")));
        }

        table.addCell(_noBorderCell(":" + s.get("portOfDischarge") + ":       Port Of Discharge/Airport:", darkFont));
        if (StringUtils.isNotBlank(lcDto.getPortOfDischarge())) {
            table.addCell(_noBorderCell(lcDto.getPortOfDischarge(), lightFont));
        } else {
            table.addCell(_noBorderCell("NA", lightFont));
        }

        table.addCell(_noBorderCell(":" + s.get("placeOfFinalDelivery") + ":     For Transportation to:", darkFont, WebColors.getRGBColor("#f6f7ef")));
        if (StringUtils.isNotBlank(lcDto.getPlaceOfFinalDelivery())) {
            table.addCell(_noBorderCell(lcDto.getPlaceOfFinalDelivery(), lightFont, WebColors.getRGBColor("#f6f7ef")));
        } else {
            table.addCell(_noBorderCell("N/A", lightFont, WebColors.getRGBColor("#f6f7ef")));
        }

        table.addCell(_noBorderCell(":" + s.get("latestShipmentDate") + ":       Latest Date Of Shipment:", darkFont));
        if (StringUtils.isNotBlank(lcDto.getLatestShippingDate())) {
            table.addCell(_noBorderCell(_formatDate(lcDto.getLatestShippingDate()), lightFont));
        } else {
            table.addCell(_noBorderCell("NA", lightFont));
        }

        table.addCell(_noBorderCell(":" + s.get("incoTerms") + ":     Shipment Period:", darkFont, WebColors.getRGBColor("#f6f7ef")));
        if (StringUtils.isNotBlank(lcDto.getIncoTerms())) {
            table.addCell(_noBorderCell(lcDto.getIncoTerms(), lightFont, WebColors.getRGBColor("#f6f7ef")));
        } else {
            table.addCell(_noBorderCell("N/A", lightFont, WebColors.getRGBColor("#f6f7ef")));
        }

        table.addCell(_noBorderCell(":" + s.get("descriptionOfGoods") + ":       Description Of Goods and/or Service:", darkFont));
        if (StringUtils.isNotBlank(lcDto.getDescriptionOfGoods())) {
            table.addCell(_noBorderCell(lcDto.getDescriptionOfGoods(), lightFont));
        } else {
            table.addCell(_noBorderCell("NA", lightFont));
        }

        table.addCell(_noBorderCell(":" + s.get("documentsRequired") + ":     Documents Required:", darkFont, WebColors.getRGBColor("#f6f7ef")));
        if (StringUtils.isNotBlank(lcDto.getDocumentsRequired())) {
            table.addCell(_noBorderCell(lcDto.getDocumentsRequired(), lightFont, WebColors.getRGBColor("#f6f7ef")));
        } else {
            table.addCell(_noBorderCell("N/A", lightFont, WebColors.getRGBColor("#f6f7ef")));
        }

        table.addCell(_noBorderCell(":" + s.get("additionalCondition") + ":       Additional Conditions:", darkFont));
        if (StringUtils.isNotBlank(lcDto.getOtherAdditionalConditions())) {
            table.addCell(_noBorderCell(lcDto.getOtherAdditionalConditions(), lightFont));
        } else {
            table.addCell(_noBorderCell("NA", lightFont));
        }

        table.addCell(_noBorderCell(":" + s.get("detailsofcharges") + ":     Details of Charges:", darkFont, WebColors.getRGBColor("#f6f7ef")));
        table.addCell(_noBorderCell("N/A", lightFont, WebColors.getRGBColor("#f6f7ef")));

        table.addCell(_noBorderCell(":" + s.get("periodforPresentation") + ":         Period for Presentation in Days:", darkFont));
        if (StringUtils.isNotBlank(lcDto.getPresentationPeriod())) {
            table.addCell(_noBorderCell(lcDto.getPresentationPeriod(), lightFont));
        } else {
            table.addCell(_noBorderCell("NA", lightFont));
        }

        table.addCell(_noBorderCell(":" + s.get("confirmationInstructions") + ":       Confirmation Instruction:", darkFont, WebColors.getRGBColor("#f6f7ef")));
        if (StringUtils.isNotBlank(lcDto.getConfirmationInstruction())) {
            table.addCell(_noBorderCell(lcDto.getConfirmationInstruction(), lightFont, WebColors.getRGBColor("#f6f7ef")));
        } else {
            table.addCell(_noBorderCell("N/A", lightFont, WebColors.getRGBColor("#f6f7ef")));
        }

        table.addCell(_noBorderCell(":" + s.get("reimbursingBank") + ":       Reimbursing Bank:", darkFont));
        table.addCell(_noBorderCell("NA", lightFont));

        table.addCell(_noBorderCell(":" + s.get("instructionsToThePay") + ":       Instructions to the PAY/APT/NEG BNK:", darkFont, WebColors.getRGBColor("#f6f7ef")));
        table.addCell(_noBorderCell("N/A", lightFont, WebColors.getRGBColor("#f6f7ef")));

        table.addCell(_noBorderCell(":" + s.get("senderToReceiverInformation") + ":         Sender to Reciever information:", darkFont));
        table.addCell(_noBorderCell("NA", lightFont));

        table.addCell(_noBorderCell("-}", darkFont));

        return table;
    }

    public static void addSwiftHeadersAndFootersPDF(Document document) {
        Image logo = null;
        try (InputStream ipStream = PDFGenerator.class.getClassLoader().getResourceAsStream(LOGO_INFINITY)) {
            logo = Image.getInstance(IOUtils.toByteArray(Objects.requireNonNull(ipStream)));
            logo.scaleAbsoluteHeight(23);
            logo.scaleAbsoluteWidth(70);
        } catch (Exception e) {
            alert.prepareError("Error occurred while creating trade finance details pdf ", e).log();
        }
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.addCell(_noBorderCell("Message Key", darkerFont));
        table.addCell(_noBorderCell("R2022010968244-1408501                ISSUE GUARANTEES & STANDBY LC", darkerFont));


        Phrase phrase = new Phrase();
        phrase.add(new Chunk(logo, 0, 0, true));
        SimpleDateFormat formatDate1 = new SimpleDateFormat("dd MMMMM yyyy");
        SimpleDateFormat formatdate2 = new SimpleDateFormat("HH:mm:ss");
        Date date1 = new Date();
        Date date2 = new Date();
        String str2 = String.format(formatdate2.format(date2));
        String str1 = String.format(formatDate1.format(date1));
        // heading
        Paragraph date = new Paragraph(addSpacesPrefix(str1, 157 - str1.length()), new Font(Font.HELVETICA, 9, Font.NORMAL, darkFont));
        phrase.add(date);
        Paragraph time = new Paragraph(addSpacesPrefix(str2, 205 - str2.length()), new Font(Font.HELVETICA, 9, Font.NORMAL, darkFont));
        phrase.add(time);


        HeaderFooter header = new HeaderFooter(phrase, false);
        header.setBorderColor(java.awt.Color.WHITE);
        document.setHeader(header);

        Paragraph pageNumber = new Paragraph(addSpacesPrefix("Page ", 176), new Font(Font.HELVETICA, 10, Font.NORMAL, darkFont));
        HeaderFooter footer = new HeaderFooter(new Phrase(pageNumber), true);
        footer.setBorderColor(java.awt.Color.WHITE);
        document.setFooter(footer);

    }

    public static PdfPCell _noBorderHeadingCell(String text, java.awt.Color fontColor) {
        Paragraph cellText = new Paragraph(text, new Font(Font.HELVETICA, 7, Font.BOLD, fontColor));
        PdfPCell cell = new PdfPCell();
        cell.addElement(cellText);
        cell.setColspan(2);
        cell.setBorder(0);
        cell.setPaddingTop(4);
        cell.setPaddingBottom(4);
        return cell;
    }

    public static PdfPCell _noBorderCell(String text, java.awt.Color fontColor) {
        Paragraph cellText = new Paragraph(text, new Font(Font.HELVETICA, 6, Font.BOLD, fontColor));
        PdfPCell cell = new PdfPCell();
        cell.addElement(cellText);
        cell.setBorder(0);
        cell.setPaddingTop(4);
        cell.setPaddingBottom(4);
        return cell;
    }

    public static PdfPCell _noBorderCell(String text, java.awt.Color fontColor, java.awt.Color backGroundColor) {
        Paragraph cellText = new Paragraph(text, new Font(Font.HELVETICA, 7, Font.BOLD, fontColor));
        PdfPCell cell = new PdfPCell();
        cell.addElement(cellText);
        cell.setBorder(0);
        cell.setPaddingTop(4);
        cell.setPaddingBottom(4);
        cell.setBackgroundColor(backGroundColor);
        return cell;
    }

    public static PdfPCell _noBorderCell(String text, java.awt.Color fontColor, int fontSize) {
        Paragraph cellText = new Paragraph(text, new Font(Font.HELVETICA, fontSize, Font.BOLD, fontColor));
        PdfPCell cell = new PdfPCell();
        cell.addElement(cellText);
        cell.setBorder(0);
        cell.setPaddingTop(4);
        cell.setPaddingBottom(4);
        return cell;
    }

    public static String addSpacesPrefix(String input, int count) {
        char[] repeatArr = new char[count];
        Arrays.fill(repeatArr, ' ');
        input = new String(repeatArr) + input;
        return input;
    }

    public String _getCurrencySymbol(String currencyCode) {
        String symbol;
        switch (currencyCode) {
            case "INR":
                symbol = "₹";
                break;
            case "EUR":
                symbol = "€";
                break;
            case "GBP":
                symbol = "£";
                break;
            case "USD":
                symbol = "$";
                break;
            default:
                symbol = "";
        }
        return symbol;
    }

    public String _formatDate(String dateInString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(dateInString);
        } catch (Exception e) {
            alert.prepareError("Error in formatting date. ", e).log();
        }
        SimpleDateFormat dt1 = new SimpleDateFormat("yyMMdd");
        return dt1.format(date);
    }

    public String _formatAmount(String amount) {
        double amountInDouble = Double.parseDouble(amount);
        amountFormat.setMinimumFractionDigits(2);
        return amountFormat.format(amountInDouble);
    }
}