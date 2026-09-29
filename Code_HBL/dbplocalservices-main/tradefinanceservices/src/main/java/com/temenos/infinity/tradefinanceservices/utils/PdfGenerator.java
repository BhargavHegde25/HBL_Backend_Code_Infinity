/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.utils;

import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.memorymgmt.CorporateManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.*;
import static com.temenos.infinity.tradefinanceservices.utils.PdfGenerator.FontStyle.*;

/**
 * @author k.meiyazhagan
 */
public class PdfGenerator {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final int numColumns = 3;
    private static String pdfHeading = "TradeFinance Document";

    public enum FontStyle {
        REGULAR_LIGHT(new Font(Font.HELVETICA, 10, Font.NORMAL, lightFont), 6),
        REGULAR_DARK(new Font(Font.HELVETICA, 10, Font.NORMAL, darkFont), 6),
        REGULARBOLD_DARK(new Font(Font.HELVETICA, 10, Font.BOLD, darkFont), 6),
        SUBHEADING_LIGHT(new Font(Font.HELVETICA, 11, Font.NORMAL, lightFont), 6),
        SUBHEADING_DARK(new Font(Font.HELVETICA, 11, Font.NORMAL, darkFont), 6),
        REGULAR_CLICKABLE(new Font(Font.HELVETICA, 10, Font.NORMAL, blueFont), 6),
        SUBHEADING_BOLD_DARK(new Font(Font.HELVETICA, 11, Font.BOLD, lightFont), 10),
        HEADING(new Font(Font.HELVETICA, 13, Font.BOLD, darkFont), 16);

        private final Font font;
        private final int paddingTop;

        FontStyle(Font f, int paddingTop) {
            this.font = f;
            this.paddingTop = paddingTop;
        }

        public Font getFont() {
            return font;
        }

        public int getPaddingTop() {
            return this.paddingTop;
        }
    }


    public static String generatePdf(DataControllerRequest request, String prefix, String pdfTitle, LinkedHashMap<PdfContentStyle, PdfContentStyle> reportData) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outStream);
        pdfHeading = pdfTitle;
        addHeadersAndFootersPDF(writer, document, pdfHeading, true);

        try {
            document.open();
            document.add(addCustomerDetailsPDF(request));

            PdfPTable table = new PdfPTable(numColumns);
            table.setWidthPercentage(100);
            table.setHorizontalAlignment(Element.ALIGN_LEFT);

            for (Map.Entry<PdfContentStyle, PdfContentStyle> entry : reportData.entrySet()) {
                table.addCell(_customCell(new PdfPCell(), entry.getKey(), numColumns - 2));
                if (entry.getValue() != null) {
                    table.addCell(_customCell(new PdfPCell(), entry.getValue(), numColumns - 1));
                }
                table.completeRow();
            }
            document.add(table);

        } catch (Exception e) {
            alert.prepareError("Error occurred while creating pdf ", e).log();
        } finally {
            outStream.close();
            document.close();
        }

        byte[] pdfBytes = outStream.toByteArray();
        String fileId = TradeFinanceCommonUtils.generateTradeFinanceFileID(prefix);
        MemoryManager.saveIntoCache(fileId + new CorporateManager(request).getSessionId(), Base64.encodeBase64String(pdfBytes), 120);
        return fileId;
    }

    public static Document addHeadersAndFootersPDF(PdfWriter writer, Document document, String heading, boolean isTermsAndConditionsRequired) {
        try {
            writer.setPageEvent(new PdfEventHandler());
            document.setPageSize(PageSize.A4);
            document.setMargins(40, 40, 80, 70);

            if (StringUtils.isNotBlank(heading)) {
                pdfHeading = heading;
            }

            if (isTermsAndConditionsRequired) {
                String termsAndConditions = "Terms & Conditions: This disclaimer informs readers that the views,thoughts, and opinions expressed in the text belongs solely to the author, and not necessarily to the author's employer, organization, committee or other group or individual.";
                Phrase tcPhrase = new Phrase(new Paragraph(termsAndConditions, FontStyle.REGULAR_DARK.getFont()));
                HeaderFooter footer = new HeaderFooter(tcPhrase, false);
                footer.setBorder(Rectangle.TOP);
                footer.setBorderColor(Color.LIGHT_GRAY);
                document.setFooter(footer);
            }
        } catch (Exception e) {
            alert.prepareError("Error occurred while creating trade finance details pdf ", e).log();
        }

        return document;
    }

    public static Element addCustomerDetailsPDF(DataControllerRequest request) {
        final Map<String, Object> customer = CustomerSession.getCustomerMap(request);

        PdfPTable superTable = new PdfPTable(2);
        superTable.setWidthPercentage(100);

        PdfPTable subTable = new PdfPTable(1);
        subTable.setWidthPercentage(100);

        // Name - Fetching FirstName and LastName from customer details
        subTable.addCell(_customerDetailsCell(customer.get("FirstName") + " " + customer.get("LastName"), HEADING, 1));
        subTable.completeRow();
        // Customer Address - MOCK
        subTable.addCell(_customerDetailsCell("Ohio/West Virginia Markets" + "\n" + "P O Box 260180", FontStyle.SUBHEADING_LIGHT, 1));
        subTable.completeRow();
        // Phone Number - Fetching from customer details
        subTable.addCell(_customerDetailsCell(customer.get("phoneCountryCode") + " " + customer.get("Phone"), FontStyle.SUBHEADING_LIGHT, 1));
        subTable.completeRow();

        superTable.addCell(_customerTableCell(subTable, new PdfPCell()));

        subTable = new PdfPTable(3);
        subTable.setWidthPercentage(100);
        // Bank name - MOCK
        subTable.addCell(_customerDetailsCell("Bank Name:", FontStyle.REGULAR_LIGHT, 1));
        subTable.addCell(_customerDetailsCell("Infinity Digital Banking ", SUBHEADING_DARK, 2));
        subTable.completeRow();
        // Customer care - MOCK
        subTable.addCell(_customerDetailsCell("Customer Care:", FontStyle.REGULAR_LIGHT, 1));
        subTable.addCell(_customerDetailsCell("10000020000", FontStyle.REGULAR_DARK, 2));
        subTable.completeRow();
        // Bank Address - MOCK
        subTable.addCell(_customerDetailsCell("Address:", FontStyle.REGULAR_LIGHT, 1));
        subTable.addCell(_customerDetailsCell("Ohio/West Virginia Markets" + "\n" + "P O Box 260180", FontStyle.REGULAR_DARK, 2));
        subTable.completeRow();
        // Email - Fetching from customer details
        subTable.addCell(_customerDetailsCell("Email:", FontStyle.REGULAR_LIGHT, 1));
        subTable.addCell(_customerDetailsCell(String.valueOf(customer.get("email")), FontStyle.REGULAR_CLICKABLE, 2));
        subTable.completeRow();
        // Website - MOCK
        subTable.addCell(_customerDetailsCell("Website:", FontStyle.REGULAR_LIGHT, 1));
        subTable.addCell(_customerDetailsCell("dbxdigitalbanking.com", FontStyle.REGULAR_CLICKABLE, 2));
        subTable.completeRow();

        superTable.addCell(_customerTableCell(subTable, new PdfPCell()));

        return superTable;
    }

    private static PdfPCell _customerTableCell(PdfPTable table, PdfPCell cell) {
        cell.addElement(table);
        cell.setPaddingTop(20);
        cell.setBorder(Rectangle.BOTTOM);
        cell.setBorderColor(Color.GRAY);
        cell.setPaddingBottom(10);
        return cell;
    }

    private static PdfPCell _customCell(PdfPCell cell, PdfContentStyle styleObj, int colSpan) {
        cell.addElement(new Paragraph(StringUtils.isNotBlank(styleObj.data) ? styleObj.data : "NA", styleObj.fontStyle.getFont()));
        cell.setBorderColor(Color.WHITE);
        cell.setColspan(styleObj.isHeading ? numColumns : colSpan);
        cell.setPaddingTop(styleObj.fontStyle.getPaddingTop());
        cell.setPaddingBottom(6);
        return cell;
    }

    private static PdfPCell _customerDetailsCell(String text, FontStyle style, int colSpan) {
        PdfPCell cell = new PdfPCell();
        cell.addElement(new Paragraph(text, style.getFont()));
        cell.setBorderColor(Color.WHITE);
        cell.setColspan(colSpan);
        cell.setPaddingTop(3);
        cell.setPaddingBottom(3);
        return cell;
    }

    public static PdfPCell _noBorderHeadingCell(String text, Color fontColor) {
        Paragraph cellText = new Paragraph(text, new Font(Font.HELVETICA, 11, Font.BOLD, fontColor));
        PdfPCell cell = new PdfPCell();
        cell.addElement(cellText);
        cell.setColspan(2);
        cell.setBorderColor(Color.WHITE);
        cell.setPaddingTop(16);
        cell.setPaddingBottom(6);
        return cell;
    }

    public static PdfPCell _noBorderSubHeadingCell(String text, Color fontColor) {
        Paragraph cellText = new Paragraph(text, new Font(Font.HELVETICA, 10, Font.NORMAL, fontColor));
        PdfPCell cell = new PdfPCell();
        cell.addElement(cellText);
        cell.setColspan(2);
        cell.setBorderColor(Color.WHITE);
        cell.setPaddingTop(16);
        cell.setPaddingBottom(6);
        return cell;
    }

    public static PdfPCell _borderHeadingCellTable(String text, Color fontColor) {
        Paragraph cellText = new Paragraph(text, new Font(Font.HELVETICA, 11, Font.BOLD, fontColor));
        PdfPCell cell = new PdfPCell();
        cell.addElement(cellText);
        cell.setColspan(2);
        cell.setBorder(Rectangle.BOTTOM);
        cell.setBorderColor(Color.LIGHT_GRAY);
        cell.setPaddingTop(16);
        cell.setPaddingBottom(10);
        return cell;
    }

    public static PdfPCell _borderHeadingCell(String text, Color fontColor) {
        Paragraph cellText = new Paragraph(text, new Font(Font.HELVETICA, 10, Font.BOLD, fontColor));
        PdfPCell cell = new PdfPCell();
        cell.addElement(cellText);
        cell.setBorder(Rectangle.BOTTOM);
        cell.setBorderColor(Color.LIGHT_GRAY);
        cell.setPaddingTop(6);
        cell.setPaddingBottom(6);
        return cell;
    }

    public static PdfPCell _noBorderCell(String text, Color fontColor) {
        Paragraph cellText = new Paragraph(text, new Font(Font.HELVETICA, 10, Font.NORMAL, fontColor));
        PdfPCell cell = new PdfPCell();
        cell.addElement(cellText);
        cell.setBorderColor(Color.WHITE);
        cell.setPaddingTop(6);
        cell.setPaddingBottom(6);
        return cell;
    }

    public static PdfPCell _borderCell(String text, Color fontColor) {
        Paragraph cellText = new Paragraph(text, new Font(Font.HELVETICA, 10, Font.NORMAL, fontColor));
        PdfPCell cell = new PdfPCell();
        cell.addElement(cellText);
        cell.setBorder(Rectangle.BOTTOM);
        cell.setBorderColor(Color.LIGHT_GRAY);
        cell.setPaddingTop(6);
        cell.setPaddingBottom(10);
        return cell;
    }

    private static class PdfEventHandler implements PdfPageEvent {
        BaseFont baseFont = null;
        float pageTop;
        float pageLeft;
        float pageRight;
        float pageBottom;
        PdfTemplate template;
        PdfContentByte cb;

        @Override
        public void onOpenDocument(PdfWriter pdfWriter, Document document) {
            try {
                baseFont = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
                cb = pdfWriter.getDirectContent();
                template = cb.createTemplate(50, 50);
                pageTop = document.top();
                pageLeft = document.left();
                pageRight = document.right();
                pageBottom = document.bottom();
            } catch (IOException e) {
                // do nothing
            }
        }

        @Override
        public void onStartPage(PdfWriter pdfWriter, Document document) {
            Image logo;
            try (InputStream inputStream = PdfGenerator.class.getClassLoader().getResourceAsStream(LOGO_INFINITY)) {
                logo = Image.getInstance(IOUtils.toByteArray(Objects.requireNonNull(inputStream)));
            } catch (IOException e) {
                alert.prepareError("Error occurred while adding image in pdf ", e).log();
                throw new RuntimeException(e);
            }
            logo.scaleAbsolute(97, 30);
            logo.setAbsolutePosition(pageLeft, pageTop);
            cb.beginText();
            cb.addImage(logo);
            cb.endText();

            cb.beginText();
            cb.setFontAndSize(baseFont, 15);
            cb.setColorFill(darkFont);
            cb.showTextAligned(Element.ALIGN_RIGHT, pdfHeading, pageRight, pageTop, 0);
            cb.endText();
        }

        @Override
        public void onEndPage(PdfWriter pdfWriter, Document document) {
            String pageNumber = "Page " + pdfWriter.getPageNumber() + " of ";
            float textWidth = baseFont.getWidthPoint(pageNumber, 10);
            cb.beginText();
            cb.setFontAndSize(baseFont, 10);
            cb.setColorFill(darkFont);
            cb.setTextMatrix(pageRight - textWidth, pageBottom - 30);
            cb.showText(pageNumber);
            cb.endText();
            cb.addTemplate(template, pageRight, pageBottom - 30);
        }

        @Override
        public void onCloseDocument(PdfWriter pdfWriter, Document document) {
            template.beginText();
            template.setFontAndSize(baseFont, 10);
            template.showText(String.valueOf(pdfWriter.getPageNumber() - 1));
            template.endText();
        }

        @Override
        public void onParagraph(PdfWriter pdfWriter, Document document, float v) {

        }

        @Override
        public void onParagraphEnd(PdfWriter pdfWriter, Document document, float v) {

        }

        @Override
        public void onChapter(PdfWriter pdfWriter, Document document, float v, Paragraph paragraph) {

        }

        @Override
        public void onChapterEnd(PdfWriter pdfWriter, Document document, float v) {

        }

        @Override
        public void onSection(PdfWriter pdfWriter, Document document, float v, int i, Paragraph paragraph) {

        }

        @Override
        public void onSectionEnd(PdfWriter pdfWriter, Document document, float v) {

        }

        @Override
        public void onGenericTag(PdfWriter pdfWriter, Document document, Rectangle rectangle, String s) {

        }
    }
}
